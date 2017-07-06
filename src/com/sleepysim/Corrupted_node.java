package com.sleepysim;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Corrupted_node implements Node
{
    private Adversary adversary;
    private Integer id;
    private String secret_key;
    private final ArrayList<String> public_key;

    /**
     * Corrupted node is controlled by adversary
     * @param id Your unique id
     * @param adversary Your god
     */

    public Corrupted_node(Integer id, Adversary adversary, String secret_key, ArrayList<String> public_key)
    {
        this.id = id;
        this.adversary = adversary;
        this.secret_key = secret_key;
        this.public_key = public_key;
    }

    /**
     * Send message to someone
     * @param msg message
     * @param from your id, indicate who send this message
     * @param to destination
     */
    @Override
    public void send_message(Message msg, Integer from, ArrayList<Integer> to)
    {
        //some code goes here
        //for adversary team
    }

    /**
     * Get message from honest node
     * See message_to_send for details
     * @return a list of intercepted message
     */
    public ArrayList<Message_to_send> intercept_message()
    {
        //some code goes here
        //for adversary team
        return null;
    }

    /**
     * Receive message from other node
     * @return a list of message
     */
    @Override
    public ArrayList<Message> receive_message()
    {
        //some code goes here
        //for adversary team
        return null;
    }

    /**
     * generate a signature of msg, signed by you
     * You should call signature class
     * @param msg message to sign
     * @return a signature
     */
    @Override
    public String request_signature(Message msg)
    {
        //some code goes here
        //for adversary team
        return null;
    }

    @Override
    public boolean check_signature(Integer id, String signature)
    {
        //some code goes here
        //for adversary team
        return false;
    }

    @Override
    public void run()
    {
        adversary.run(this);
    }
}
