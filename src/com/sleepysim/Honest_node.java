package com.sleepysim;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Honest_node implements Node
{
    private final Integer id;
    private String secret_key;
    private final ArrayList<String> public_key;

    /**
     * Initialize the node
     * @param id your unique id
     * @param secret_key your secret key
     * @param public_key list of public key
     */
    public Honest_node(Integer id, String secret_key, ArrayList<String> public_key)
    {
        this.secret_key = secret_key;
        this.public_key = public_key;
        this.id = id;
        //some code goes here
        //for honest team
    }

    /**
     * send message to others
     * You should call function from framework, see Framework.java for details
     * @param msg message
     * @param from your id, do not use other's id
     * @param to destination
     */
    @Override
    public void send_message(Message msg, Integer from, ArrayList<Integer> to)
    {
        //some code goes here
        //for honest team
    }

    /**
     * receive message from other
     * @return list of message
     */
    @Override
    public ArrayList<Message> receive_message()
    {
        //some code goes here
        //for honest team
        return null;
    }

    /**
     * 
     * @param msg
     * @return
     */
    @Override
    public String request_signature(Message msg)
    {
        //some code goes here
        //for honest team
        return null;
    }

    @Override
    public boolean check_signature(Integer id, String signature)
    {
        //some code goes here
        //for honest team
        return false;
    }

    @Override
    public void run()
    {
        //some code goes here
        //for honest team
    }
}
