package com.sleepysim;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Signature
{
    private ArrayList<String> secret_key_table, public_key_table;

    public Signature(ArrayList<String> secret_key_table, ArrayList<String> public_key_table)
    {
        //some code goes here
        //for framework team
    }

    String generate_signature(Node node, Message msg)
    {
        //some code goes here
        //for framework team
        return null;
    }

    boolean check_signature(int node_id, String sig)
    {
        //some code goes here
        //for framework team
        return false;
    }

    String get_public_key(int node_id)
    {
        //some code goes here
        //for framework team
        return null;
    }
}
