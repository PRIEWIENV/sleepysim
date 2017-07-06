package com.sleepysim;

public class Corrupted_node implements Node {
    private Adversary adversary;
    @Override
    public void send_message(Message msg) {

    }

    @Override
    public Message receive_message() {
        return null;
    }

    @Override
    public String request_signature() {
        return null;
    }

    @Override
    public boolean check_signature() {
        return false;
    }

    @Override
    public void one_round() {

    }
}
