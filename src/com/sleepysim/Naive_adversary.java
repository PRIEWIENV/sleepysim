package com.sleepysim;

import javafx.util.Pair;

import java.util.ArrayList;

public class Naive_adversary implements Adversary
{
    private Integer n;
    private ArrayList<Pair<Integer, String>> secret_key_table;
    private ArrayList<String> public_key_table;

    /**
     * A naive adversary, you should break consistency if you have more node than honest
     * @param n number of corrupted nodes
     * @param secret_key_table secret key for each corrupted node, the Integer is node id, and String is the corresponding secret key
     * @param public_key_table public keys
     */
    public Naive_adversary(Integer n, ArrayList<Pair<Integer, String>> secret_key_table, ArrayList<String> public_key_table)
    {
        this.n = n;
        this.secret_key_table = secret_key_table;
        this.public_key_table = public_key_table;
    }

    /**
     * Your adversary algorithm
     * @param node the instance of corrupted node
     */
    @Override
    public void run(Corrupted_node node)
    {
        //some code goes here
        //For adversary team
    }
}
