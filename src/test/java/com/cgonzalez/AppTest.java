package com.cgonzalez;

import static org.junit.Assert.assertTrue; 
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     *  checking if nodes are where they are supposed to be, for k=4
     */
    @Test
    public void checkNodeLocation()
    {
        FatTree testTree = new FatTree(4,10,3,3,10);
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
    }
    @Test
    public void checkCoreEdges(){
        FatTree testTree = new FatTree(4,10,3,3,10);

        CoreSwitch firstCore = (CoreSwitch) testTree.tree[0];
        CoreSwitch lastCore = (CoreSwitch) testTree.tree[3];
        assertTrue(containsElement(firstCore.aggrEdges,4));
        assertFalse(containsElement(firstCore.aggrEdges,5));

        assertTrue(containsElement(lastCore.aggrEdges,5));
        assertFalse(containsElement(lastCore.aggrEdges, 4));
    }
    @Test 
    public void checkAggrEdges(){
        FatTree testTree = new FatTree(4,10,3,3,10);

        AggregateSwitch firstAggr = (AggregateSwitch) testTree.tree[4];
        AggregateSwitch lastAggr = (AggregateSwitch) testTree.tree[11];
        
        
        assertTrue(containsElement(firstAggr.coreEdges,1));

        assertFalse(containsElement(firstAggr.coreEdges,5));
        
        assertFalse(containsElement(firstAggr.coreEdges,2));
  
        assertTrue(containsElement(lastAggr.coreEdges,2));

        assertFalse(containsElement(lastAggr.coreEdges,1));
    }
    @Test
    public void checkDists(){
        FatTree testTree = new FatTree(4,10,3,3,10);

        assertTrue(testTree.tree[0] instanceof CoreSwitch);
        assertTrue(testTree.tree[20] instanceof PhysicalMachine);
        assertTrue(FatTree.distance(testTree.tree[0],testTree.tree[20])==3);

        assertTrue(FatTree.distance(testTree.tree[0],testTree.tree[1])==2);
        assertTrue(FatTree.distance(testTree.tree[0],testTree.tree[2])==4);
        
        assertTrue(FatTree.distance(testTree.tree[20],testTree.tree[21])==2);
        assertTrue(FatTree.distance(testTree.tree[20],testTree.tree[22])==4);
        assertTrue(FatTree.distance(testTree.tree[20],testTree.tree[24])==6);
        assertTrue(FatTree.distance(testTree.tree[20],testTree.tree[6])==4);

        //distances for switches to other switches
        assertTrue(FatTree.distance(testTree.tree[6],testTree.tree[7])==2);
        assertTrue(FatTree.distance(testTree.tree[6],testTree.tree[8])==2);
        assertTrue(FatTree.distance(testTree.tree[6],testTree.tree[9])==4);
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
