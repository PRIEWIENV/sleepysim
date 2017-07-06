package com.sleepysim;

import java.util.ArrayList;

public class Message_to_send
{
    private final Message message;
    private final int sender_id;
    private final Integer target_id;
    private final int send_time;
    private final int uid;
    Message_to_send(Message message, int sender_id, Integer target_id, int send_time, int uid)
    {
        this.message = message;
        this.sender_id = sender_id;
        this.target_id = target_id;
        this.send_time = send_time;
        this.uid = uid;
    }
}
