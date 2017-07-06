package com.sleepysim;

import java.util.ArrayList;

public class Message_to_send
{
    private final Message message;
    private final Integer sender_id;
    private final Integer target_id;
    private final Integer send_time;
    private final Integer uid;

    Message_to_send(Message message, int sender_id, Integer target_id, int send_time, int uid)
    {
        this.message = message;
        this.sender_id = sender_id;
        this.target_id = target_id;
        this.send_time = send_time;
        this.uid = uid;
    }

    Message get_message()
    {
        return message;
    }

    Integer get_sender_id()
    {
        return sender_id;
    }

    Integer get_target_id()
    {
        return target_id;
    }

    Integer get_send_time()
    {
        return send_time;
    }

    Integer get_uid()
    {
        return uid;
    }
}
