package com.cgonzalez;

public class VmPair {
    static int uuid=0;

    int id;
    int firstVmLocation;
    int secondVmLocation;
    int trafficRate;

    VmPair(int firstVmLocation, int secondVmLocation, int trafficRate){
        id=uuid++;
        this.firstVmLocation=firstVmLocation;
        this.secondVmLocation=secondVmLocation;
        this.trafficRate=trafficRate;
    }
}
