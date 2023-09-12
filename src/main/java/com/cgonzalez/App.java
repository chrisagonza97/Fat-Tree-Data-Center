package com.cgonzalez;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
        FatTree tree = new FatTree(4,10,3,3,10);
        tree.setTrafficRange(100, 4000);
        tree.createVmPairs();
        tree.cs2Migration();
    }
}
