package com.cgonzalez;

public class CoreSwitch extends Node {
    int id;
    int aggrEdges[];
    int k;
    public CoreSwitch(int id, int k) {
        this.id = id;
        aggrEdges = new int[k];
        this.k=k;
    }
    public void addAggrEdge(int id){
        for (int i=0;i<k;i++){
            if (aggrEdges[i]==0){
                aggrEdges[i]=id;
            }
        }
    }
}
