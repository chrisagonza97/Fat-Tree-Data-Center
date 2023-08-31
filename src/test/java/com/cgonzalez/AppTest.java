package com.cgonzalez;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     *  checking if nodes are where they are supposed to be, for k=4
     */
    /*@Test
    public void checkNodeLocation()
    {
        FatTree testTree = new FatTree(4,10,3,3);
        //first node is of type core switch
        assertTrue( testTree.tree[0] instanceof CoreSwitch);
        //fourth node is of type core switch
        assertTrue( testTree.tree[3] instanceof CoreSwitch);
        //fifth node is of type AggregateSwitch
        assertTrue( testTree.tree[4] instanceof AggregateSwitch);
        //12th is also aggr
        assertTrue( testTree.tree[11] instanceof AggregateSwitch);
        //13th is edge
        assertTrue( testTree.tree[12] instanceof EdgeSwitch);
        //20th node is edge
        assertTrue( testTree.tree[19] instanceof EdgeSwitch);
        //21st node is pm
        assertTrue( testTree.tree[20] instanceof PhysicalMachine);
        assertTrue(testTree.firstPm==20);
        //36th is pm
        assertTrue( testTree.tree[35] instanceof PhysicalMachine);
        assertTrue(testTree.lastPm==35);
        //there are 16 PMs
        assertTrue(testTree.pmCount==16);
    }*/

}
