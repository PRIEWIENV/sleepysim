package com.sleepysim;

import java.util.ArrayList;

public class Corrupted_node implements Node
{
    private Adversary adversary;
    private Integer id;

    public Corrupted_node(Integer id, Adversary adversary)
    {
        this.id = id;
        this.adversary = adversary;
    }

    @Override
    public void send_message(Message msg, Integer from, ArrayList<Integer> to)
    {

    }

    @Override
    public Message receive_message()
    {
        return null;
    }

    @Override
    public String request_signature()
    {
        return null;
    }

    @Override
    public boolean check_signature()
    {
        return false;
    }

    @Override
    public void run()
    {

    }
}
