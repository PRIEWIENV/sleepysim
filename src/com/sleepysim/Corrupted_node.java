package com.sleepysim;

import java.awt.Frame;
import java.lang.reflect.Array;
import java.security.PrivateKey;
import java.util.ArrayList;

public class Corrupted_node implements Node
{
    private Adversary adversary;
    private Integer id;
    private PrivateKey secret_key;
    private final ArrayList<String> public_key;
    private Framework framework;

    /**
     * Corrupted node is controlled by adversary
     * @param id Your unique id
     * @param adversary Your god
     */

    public Corrupted_node(Integer id, Adversary adversary, PrivateKey secret_key, ArrayList<String> public_key,Framework framework)
    {
        this.id = id;
        this.adversary = adversary;
        this.secret_key = secret_key;
        this.public_key = public_key;
        this.framework=framework;
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

    /**
     * check the signature, determine if it's signed by node id
     * @param id id of the possible signer
     * @param signature the sign
     * @return
     */
    @Override
    public boolean check_signature(Integer id, byte[] signature, byte[] data)
    {
        //some code goes here
        //for adversary team
        return false;
    }

    /**
     * the network has a new Block, you have to update your chain
     * @param b the new block
     */
    @Override
    public void update_chain(Block b)
    {
        //some code goes here
    }
    /**
     * Provide history Transactions to Controller, Controller will call this function when it need
     * @return history
     */
    @Override
    public ArrayList<Transaction> provide_history()
    {
        //some code goes here
        return null;
    }

    /**
     * directly call adversary to take action
     */
    @Override
    public void run()
    {
        adversary.run(this);
    }

    /**
     * Print log for debug
     */
    public void print_log()
    {

    }
}
