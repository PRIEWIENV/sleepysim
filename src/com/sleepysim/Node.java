package com.sleepysim;

import java.util.ArrayList;

public interface Node
{
    void send_message(Message msg, Integer from, ArrayList<Integer> to);
    Message receive_message();
    String request_signature();
    boolean check_signature();
    void one_round();
}
