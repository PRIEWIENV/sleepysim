package com.sleepysim;

import java.util.ArrayList;

public class Controller
{
    private Framework framework;
    private Signature_tool signature;
    private Adversary adversary;
    private Integer node_count, adversary_count;
    private Integer delay;
    private Integer T;
    private Integer D;
    private ArrayList<Node> nodes;
    /**
     * Create a controller and initialization, you should initial the whole network
     * to initial the network, you have to generate secret-public key pairs for all node
     * then initialize signature class
     * tell every node about their secret key, and all public keys
     *
     * initialize adversary and framework by calling their construction function.
     *
     * You should tell framework about the node and delay, you must initialize all nodes first and store them into a ArrayList
     *
     * some code have been implemented, you can modify if you like.
     *
     * @param node_count total number of nodes
     * @param adversary_count total number of adversary
     * @param delay network max delay
     * @param T inconsistency threshold
     */
    Controller(Integer node_count, Integer adversary_count, Integer delay, Integer T,Integer D)
    {
        this.node_count = node_count;
        this.adversary_count = adversary_count;
        this.delay = delay;
        this.T = T;
        this.D=D;
        //some code goes here
        //for integrate team
    }

    /**
     * Detect inconsistency
     * if the last T+1 blocks are generated by adversary, then return true
     * Otherwise return false
     */
    private boolean has_inconsistency()
    {
        //some code goes here
        //for integrate team
        return false;
    }

    /**
     * Print necessary info to screen
     * You should include total number of corrupted blocks in longest chain and length of longest chain
     */
    private void print_log()
    {
        //some code goes here
        //for integrate team
    }
    public void run()
    {
        while (!has_inconsistency())
        {
            for (int i = 0; i < node_count; i ++)
                nodes.get(i).run();
            print_log();
            framework.next_round();
        }
        print_log();
    }
}
