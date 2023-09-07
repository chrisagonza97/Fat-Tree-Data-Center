package com.cgonzalez;

public class PhysicalMachine extends Node {
    //int id;
    int pod;
    int edgeId;
    public PhysicalMachine(int id, int pod, int edgeId){
        super(id);
        //this.id = id;
        this.pod = pod;
        this.edgeId = edgeId;
    }
}
