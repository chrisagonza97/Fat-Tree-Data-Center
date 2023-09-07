package com.cgonzalez;

public class AggregateSwitch extends Node {
    //int id;
    int pod;
    int coreEdges[];
    public AggregateSwitch(int id,int pod, int k){
        super(id);
        //this.id = id;
        this.pod = pod;
        coreEdges = new int[k/2];
    }
}
