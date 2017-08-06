package com.sleepysim;

import java.util.ArrayList;

public interface Node
{
    void send_message(Message msg, Integer from, ArrayList<Integer> to);
    ArrayList<Message> receive_message();
    byte[] request_signature(Message msg);
    boolean check_signature(Integer id, byte[] signature, byte[] data);
    void update_chain(Block b, Integer round);
    ArrayList<Transaction> provide_history();
    void print_log();
    ArrayList<Block> run(Integer round);
    public void destroy();
}
