package com.sleepysim;

import java.util.ArrayList;

public interface Node
{
    void send_message(Message msg, Integer from, ArrayList<Integer> to);
    ArrayList<Message> receive_message();
    String request_signature(Message msg);
    boolean check_signature(Integer id, byte[] signature, byte[] data);
    void update_chain(Block b);
    ArrayList<Transaction> provide_history();
    void print_log();
    void run();
}
