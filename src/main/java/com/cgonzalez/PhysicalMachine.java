package com.cgonzalez;

public class PhysicalMachine extends Node {
    //int id;
    int pod;
    int edgeId;
    int capacityLeft;
    //VmPair vmPairs[];
    public PhysicalMachine(int id, int pod, int edgeId, int capacityLeft){
        super(id);
        //this.id = id;
        this.pod = pod;
        this.edgeId = edgeId;
        this.capacityLeft = capacityLeft;
        //vmPairs = new VmPair[capacityLeft];
    }
    public void addVm(){
        if(capacityLeft==0){
            throw new ArrayStoreException("Physical Machine is at capacity!");
        }
        capacityLeft--;
    }
    public void removeVmPair(){
        capacityLeft++;
    }
}
