package com.sleepysim;

import java.io.Serializable;
import java.util.ArrayList;

public class Message_to_send implements Serializable
{
    private final Message message;
    private final Integer sender_id;
    private final Integer target_id;
    private final Integer send_time;
    private final Integer uid;

    /**
     * constructor
     * This class records message that intercepted by adversary, and provide sufficient additional information
     * Adversary cannot change these parameters
     * @param message the message to deliver
     * @param sender_id who is the sender
     * @param target_id who is the destination
     * @param send_time when this message sent
     * @param uid the unique id of this message
     */
    public Message_to_send(Message message, int sender_id, Integer target_id, int send_time, int uid)
    {
        this.message = message;
        this.sender_id = sender_id;
        this.target_id = target_id;
        this.send_time = send_time;
        this.uid = uid;
    }

    public Message get_message()
    {
        return message;
    }

    public Integer get_sender_id()
    {
        return sender_id;
    }

    public Integer get_target_id()
    {
        return target_id;
    }

    public Integer get_send_time()
    {
        return send_time;
    }

    public Integer get_uid()
    {
        return uid;
    }
}
