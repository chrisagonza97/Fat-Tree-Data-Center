package com.cgonzalez;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class FatTree {
    int uuid = 0;
    int k;
    int vmPairCount;
    int vnfCapacity;
    int vnfCount;
    int firstPm; // index of the first physical machine
    int lastPm; // last
    int pmCount; // how many physical machines are there
    int resourceCapacity;
    Node[] tree;
    int [] vnfs;

    FatTree(int k, int vmPairCount, int vnfCapacity, int vnfCount) {
        this.k = k;
        this.vmPairCount = vmPairCount;
        this.vnfCapacity = vnfCapacity;
        this.vnfCount = vnfCount;
        firstPm = (k * k) / 4 + (k * k / 2) + (k * k / 2);
        lastPm = firstPm + (k * k * k) / 4 - 1;
        pmCount = lastPm - firstPm + 1;

        int treeSize = (k * k / 4) + (k / 2 * k) + (k / 2 * k) + (k * k * k / 4);
        tree = new Node[treeSize];
        vnfs = new int[vnfCount];
        buildTree();//putting nodes in Node Array
        placeVnfs();
    }

    private void placeVnfs() {
        //randomly place VNFs on any node that is not a physical machine
        for (int i=0;i<vnfCount;i++){
            boolean flag=true;
            int randomNode=-1;
            while(flag){
                randomNode = ThreadLocalRandom.current().nextInt(0, firstPm);
                flag = containsElement(vnfs,randomNode);
            }
            vnfs[i] = randomNode;
        }
    }

    public void buildTree() {
        // add k^2/4 core switches to the tree array
        for (int i = 0; i < k * k / 4; i++) {
            addToTree("core", -1, -1);
        }
        // add k/2*k aggregate switches to the tree array
        int podCount = -1;
        int coreCount = 0;
        for (int i = 0; i < k / 2 * k; i++) {
            if (i % (k / 2) == 0) {
                podCount++;
                coreCount = 0;
            }
            addToTree("aggregate", podCount, -1);
            // make edges from aggr switch to core switch and also make edges from core to
            // aggr switch
            for (int j = 0; j < k / 2; j++) {
                AggregateSwitch temp = (AggregateSwitch) tree[uuid - 1];
                temp.coreEdges[j] = coreCount;

                CoreSwitch core = (CoreSwitch) tree[coreCount];
                core.addAggrEdge(uuid - 1);
                coreCount++;
            }
        }
        // add k/2*k edge switches to the tree array
        podCount = -1;
        int edgeId = uuid - 1;// the index before first edge switch
        for (int i = 0; i < k / 2 * k; i++) {
            if (i % (k / 2) == 0) {
                podCount++;
            }
            addToTree("edge", podCount, -1);
        }
        // add k^3/4 physical machines to the tree array
        podCount = -1;
        for (int i = 0; i < k * k * k / 4; i++) {
            if (i % (k * k / 4) == 0) {
                podCount++;
            }
            if (i % (k / 2) == 0) {
                edgeId++;
            }
            addToTree("pm", podCount, edgeId);
        }

    }

    public static int distance(Node one, Node two) {
        int oneId = one.id;
        int twoId = two.id;
        if (oneId == twoId) {
            return 0;
        }
        if (one instanceof CoreSwitch && two instanceof CoreSwitch) {
            CoreSwitch tempOne = (CoreSwitch) one;
            CoreSwitch tempTwo = (CoreSwitch) two;
            // if both cores have an edge to the same Aggr switches then dist is 2,
            // otherwise it is 4
            for (int i = 0; i < tempOne.aggrEdges.length; i++) {
                int aggrId = tempOne.aggrEdges[i];
                for (int j = 0; j < tempTwo.aggrEdges.length; j++) {
                    if (aggrId == tempTwo.aggrEdges[j]) {
                        return 2;
                    }
                }
            }
            return 4;
        }
        if (one instanceof AggregateSwitch && two instanceof AggregateSwitch) {
            AggregateSwitch tempOne = (AggregateSwitch) one;
            AggregateSwitch tempTwo = (AggregateSwitch) two;
            // if both aggr switches are in the same pod the distance is 2
            if (tempOne.pod == tempTwo.pod) {
                return 2;
            }
            // otherwise if both aggr switches have an edge to a similar core switch, dist
            // is 2
            for (int i = 0; i < tempOne.coreEdges.length; i++) {
                int coreId = tempOne.coreEdges[i];
                for (int j = 0; j < tempTwo.coreEdges.length; j++) {
                    if (coreId == tempTwo.coreEdges[j]) {
                        return 2;
                    }
                }
            }
            // otherwise distance will always be 4
            return 4;
        }
        if (one instanceof EdgeSwitch && two instanceof EdgeSwitch) {
            // if they are in the same pod, distance is 2, otherwise, distance is always 4
            EdgeSwitch tempOne = (EdgeSwitch) one;
            EdgeSwitch tempTwo = (EdgeSwitch) two;
            if (tempOne.pod == tempOne.pod) {
                return 2;
            }
            return 4;
        }
        if (one instanceof PhysicalMachine && two instanceof PhysicalMachine) {
            PhysicalMachine tempOne = (PhysicalMachine) one;
            PhysicalMachine tempTwo = (PhysicalMachine) two;
            // if both pms are under the same edge dist is 2;
            if (tempOne.edgeId == tempTwo.edgeId) {
                return 2;
            }
            // otherwise if they are under the same pod dist is 4
            if (tempOne.pod == tempTwo.pod) {
                return 4;
            }
            // otherwise distance is 6
            return 6;
        }
        if (one instanceof CoreSwitch && two instanceof AggregateSwitch
                || one instanceof AggregateSwitch && two instanceof CoreSwitch) {
            // if they have an edge to each other, distance is one, otherwise it is 3.
            if (one instanceof CoreSwitch) {
                CoreSwitch tempOne = (CoreSwitch) one;
                AggregateSwitch tempTwo = (AggregateSwitch) two;
                for (int i = 0; i < tempOne.aggrEdges.length; i++) {
                    if (tempOne.aggrEdges[i] == tempTwo.id) {
                        return 1;
                    }
                }
                return 3;
            } else {
                AggregateSwitch tempOne = (AggregateSwitch) one;
                CoreSwitch tempTwo = (CoreSwitch) two;
                for (int i = 0; i < tempTwo.aggrEdges.length; i++) {
                    if (tempTwo.aggrEdges[i] == tempOne.id) {
                        return 1;
                    }
                }
                return 3;
            }
        }
        if (one instanceof CoreSwitch && two instanceof EdgeSwitch
                || one instanceof EdgeSwitch && two instanceof CoreSwitch) {
            // distance between any core switch and any edge switch is always 2
            return 2;
        }
        if (one instanceof CoreSwitch && two instanceof PhysicalMachine
                || one instanceof PhysicalMachine && two instanceof CoreSwitch) {
            // the distance between any core switch and any PM is always 3
            return 3;
        }
        if (one instanceof AggregateSwitch && two instanceof EdgeSwitch
                || one instanceof EdgeSwitch && two instanceof AggregateSwitch) {
            // if aggr switch and edge switch are in the same pod, then dist is 1, otherwise
            // it is 3
            if (one instanceof AggregateSwitch) {
                AggregateSwitch tempOne = (AggregateSwitch) one;
                EdgeSwitch tempTwo = (EdgeSwitch) two;
                if (tempOne.pod == tempTwo.pod) {
                    return 1;
                }
                return 3;
            } else {
                EdgeSwitch tempOne = (EdgeSwitch) one;
                AggregateSwitch tempTwo = (AggregateSwitch) two;
                if (tempOne.pod == tempTwo.pod) {
                    return 1;
                }
                return 3;
            }
        }
        if (one instanceof AggregateSwitch && two instanceof PhysicalMachine
                || one instanceof PhysicalMachine && two instanceof AggregateSwitch) {
            // if pm is under the pod that the aggr switch is in, dist is 2, otherwise it is
            // 4
            if (one instanceof AggregateSwitch) {
                AggregateSwitch tempOne = (AggregateSwitch) one;
                PhysicalMachine tempTwo = (PhysicalMachine) two;
                if (tempOne.pod == tempTwo.pod) {
                    return 2;
                }
                return 4;
            } else {
                PhysicalMachine tempOne = (PhysicalMachine) one;
                AggregateSwitch tempTwo = (AggregateSwitch) two;
                if (tempOne.pod == tempTwo.pod) {
                    return 2;
                }
                return 4;
            }
        }
        if (one instanceof EdgeSwitch && two instanceof PhysicalMachine
                || one instanceof PhysicalMachine && two instanceof EdgeSwitch) {
            // if pm is directly under edge switch, dist is 1, if edge is in the same pod as
            // PM, dist is 3, otherwise it is 5
            if (one instanceof EdgeSwitch) {
                EdgeSwitch tempOne = (EdgeSwitch) one;
                PhysicalMachine tempTwo = (PhysicalMachine) two;
                if (tempTwo.edgeId == tempOne.id) {
                    return 1;
                }
                if (tempTwo.pod == tempOne.pod) {
                    return 3;
                }
                return 5;
            } else {
                PhysicalMachine tempOne = (PhysicalMachine) one;
                EdgeSwitch tempTwo = (EdgeSwitch) two;
                if (tempOne.edgeId == tempTwo.id) {
                    return 1;
                }
                if (tempOne.pod == tempTwo.pod) {
                    return 3;
                }
                return 5;
            }

        }
        return -1; // program should never reach this line.
    }

    private void addToTree(String string, int pod, int edgeId) {
        switch (string) {
            case "core":
                tree[uuid] = new CoreSwitch(uuid, k);
                break;
            case "aggregate":
                tree[uuid] = new AggregateSwitch(uuid, pod, k);
                break;
            case "edge":
                tree[uuid] = new EdgeSwitch(uuid, pod);
                break;
            case "pm":
                tree[uuid] = new PhysicalMachine(uuid, pod, edgeId);
        }
        uuid++;
    }
    public static boolean containsElement(int[] arr, int target) {
        for (int element : arr) {
            if (element == target) {
                return true;
            }
        }
        return false;
    }
}
