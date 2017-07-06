package com.sleepysim;

public class Controller
{
    private Framework framework;
    private Signature signature;
    private Adversary adversary;
    private Integer node_count, adversary_count;

    Controller(Integer node_count, Integer adversary_count)
    {
        this.node_count = node_count;
        this.adversary_count = adversary_count;
        this.signature = new Signature(node_count);
        this.framework = new Framework(node_count, adversary_count, signature, adversary);
    }

    boolean has_inconsistency()
    {
        //some code goes here
        //for integrate team
        return false;
    }

    void print_log()
    {
        //some code goes here
        //for integrate team
    }

    void run()
    {
        while (!has_inconsistency())
        {
            for (int i = 0; i < node_count; i ++)
                framework.run_node(i);
            print_log();
        }
    }
}
