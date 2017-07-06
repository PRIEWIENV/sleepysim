package com.sleepysim;

import java.util.ArrayList;

public class Framework
{
    private ArrayList <ArrayList <Message>> message_wait_for_send;
    private ArrayList <ArrayList <Message_to_send>> message_buffer;
    private ArrayList <Message_to_send> message_to_corrupted_buffer;
    private ArrayList <Node> node_list;
    private Integer message_count;
    private Integer delay;
    private Integer current_round;

    Framework(Integer delay, ArrayList <Node> node_list)
    {
        //some code goes here
        //For framework team
        int n = node_list.size();
        message_wait_for_send = new ArrayList<>();
        for (int i = 0; i < n; i ++)
            message_wait_for_send.add(new ArrayList<>());
        message_buffer = new ArrayList<>();
        for (int i = 0; i < n; i ++)
            message_buffer.add(new ArrayList<>());
        message_count = 0;
        this.delay = delay;
        this.node_list = node_list;
        current_round = 0;
    }

    void next_round()
    {
        current_round ++;
    }

    ArrayList <Message> send_message(Integer target)
    {
        //some code goes here
        //For framework team
        ArrayList <Message> result = new ArrayList<>();
        result.addAll(message_wait_for_send.get(target));
        message_wait_for_send.clear();
        for (Message_to_send message: message_buffer.get(target))
        {
            if (message.get_send_time() + delay == current_round)
            {
                result.add(message.get_message());
                message_buffer.get(target).remove(message);
            }
        }
        return result;
    }

    ArrayList <Message_to_send> send_message_to_corrupted()
    {
        ArrayList <Message_to_send> result = message_to_corrupted_buffer;
        message_to_corrupted_buffer = new ArrayList<>();
        return result;
    }

    void receive_message_from_honest(Message message, Integer from, ArrayList<Integer> target)
    {
        //some code goes here
        //For framework team
        for (int i = 0; i < target.size(); i ++)
        {
            Message_to_send new_message = new Message_to_send(message, from, target.get(i), current_round, ++ message_count);
            message_to_corrupted_buffer.add(new_message);
            message_buffer.get(target.get(i)).add(new_message);
        }
    }

    void receive_message_from_corrupted(Message_to_send message)
    {
        //some code goes here
        //For framework team
        message_wait_for_send.get(message.get_target_id()).add(message.get_message());
        if (message.get_uid() != -1)
        {
            Integer target = message.get_target_id();
            for (int i = 0; i < message_buffer.get(target).size(); i ++)
                if (message.get_uid().equals(message_buffer.get(target).get(i).get_uid()))
                    message_buffer.get(target).remove(i);
        }
    }
}
