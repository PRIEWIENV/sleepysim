package com.sleepysim;

public interface Node {
    void send_message(Message msg);
    Message receive_message();
    String request_signature();
    boolean check_signature();
    void one_round();
}
