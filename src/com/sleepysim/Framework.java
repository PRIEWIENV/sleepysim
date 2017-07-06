package com.sleepysim;

import java.util.ArrayList;

public class Framework
{
    private Adversary adversary;
    private ArrayList <ArrayList <Message>> message_wait_for_send;
    private Integer node_count, adversary_count;
    private Signature signature;
    private Node node_list[];

    Framework(Integer node_count, Integer adversary_count, Signature signature, Adversary adversary)
    {
        //some code goes here
        //For framework team
        message_wait_for_send = new ArrayList<>();
        for (int i = 0; i < node_count; i ++)
            message_wait_for_send.add(new ArrayList<>());
        this.node_count = node_count;
        this.adversary_count = adversary_count;
        this.signature = signature;
        this.adversary = adversary;
        node_list = new Node[node_count];
        for (int i = 0; i < adversary_count; i ++)
            node_list[i] = new Corrupted_node(i, adversary);
        for (int i = adversary_count; i < node_count; i ++)
            node_list[i] = new Honest_node();
    }

    void run_node(int node_id)
    {
        node_list[node_id].run();
    }

    ArrayList<Message> send_message(Integer target)
    {
        //some code goes here
        //For framework team
        return null;
    }

    void receive_message_from_honest(Message msg, Integer from, ArrayList<Integer> target)
    {
        //some code goes here
        //For framework team
    }

    void receive_message_from_corrupted(Message_to_send msg)
    {
        //some code goes here
        //For framework team
    }
}
