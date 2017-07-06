package com.sleepysim;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by xietiancheng on 2017/7/6.
 */
public class Naive_adversary implements Adversary
{
    private Integer n;
    private ArrayList<Pair<Integer, String>> secret_key_table;
    private ArrayList<String> public_key_table;
    public Naive_adversary(Integer n, ArrayList<Pair<Integer, String>> secret_key_table, ArrayList<String> public_key_table)
    {
        this.n = n;
        this.secret_key_table = secret_key_table;
        this.public_key_table = public_key_table;
    }

    @Override
    public void run(Corrupted_node node)
    {
        //some code goes here
        //For adversary team
    }
}
