package com.sleepysim;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Honest_node implements Node
{
    private Integer id;
    private String secret_key;
    private final ArrayList<String> public_key;
    public Honest_node(Integer id, String secret_key, ArrayList<String> public_key)
    {
        this.secret_key = secret_key;
        this.public_key = public_key;
        //some code goes here
        //for honest team
    }

    @Override
    public void send_message(Message msg, Integer from, ArrayList<Integer> to)
    {
        //some code goes here
        //for honest team
    }

    @Override
    public ArrayList<Message> receive_message()
    {
        //some code goes here
        //for honest team
        return null;
    }

    @Override
    public String request_signature(Message msg)
    {
        //some code goes here
        //for honest team
        return null;
    }

    @Override
    public boolean check_signature(Integer id, String signature)
    {
        //some code goes here
        //for honest team
        return false;
    }

    @Override
    public void run()
    {
        //some code goes here
        //for honest team
    }
}
