package com.sleepysim;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class Honest_node implements Node
{
    private final Integer id;
    private PrivateKey secret_key;
    private final ArrayList<PublicKey> public_key;
    private static Logger logger;
    private Chain chain;
    private Integer max_length;
    private Block working_branch;
    private Framework framework;
    /**
     * Initialize the node
     * @param id your unique id
     * @param secret_key your secret key
     * @param public_key list of public key
     */
    public Honest_node(Integer id, PrivateKey secret_key, ArrayList<PublicKey> public_key, Framework framework)
    {
        this.secret_key = secret_key;
        this.public_key = public_key;
        this.id = id;
        logger = Logger.getLogger("Honest Node " + id.toString());
        this.chain = new Chain();
        max_length = -1;
        working_branch = null;
        this.framework = framework;
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
        framework.receive_message_from_honest(msg, from, to);
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
        return framework.send_message(id);
    }

    /**
     * sign the message, you should call Signature_tool's function
     * @param msg the message
     * @return
     */
    @Override
    public byte[] request_signature(Message msg)
    {
        //some code goes here
        //for honest team
        try
        {
            return Signature_tool.generate_signature(secret_key, To_byte_array.to_byte_array(msg));
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if the signature is valid
     * @param id id of
     * @param signature
     * @return
     */
    @Override
    public boolean check_signature(Integer id, byte[] signature, byte[] data)
    {
        //some code goes here
        //for honest team
        return Signature_tool.check_signature(public_key.get(id), signature, data);
    }

    /**
     * when comes a new block, check if it's longer
     * @param b
     */
    @Override
    public void update_chain(Block b)
    {
        int length = 0;
        while(b != null)
        {
            length++;
            b = chain.chain.get(b.get_last_hash());
        }
        chain.chain.put(b.get_current_hash(), b);
        if(length > max_length)
        {
            max_length = length;
            working_branch = b;
        }
    }

    /**
     * Provide history Transactions to Controller, Controller will call this function when it need
     * @return history
     */
    @Override
    public ArrayList<Transaction> provide_history()
    {
        ArrayList<Transaction> ret = new ArrayList<>();
        Block curb = working_branch;
        while(curb != null)
        {
            ret.addAll(curb.get_txs());
            curb = chain.chain.get(curb.get_last_hash());
        }
        return ret;
    }

    /**
     * Start a round, you should take action
     */
    @Override
    public void run()
    {
        //some code goes here
        //for honest team
    }

    /**
     * Print log for debug
     */
    @Override
    public void print_log()
    {
        //some code goes here
        //for honest team
    }

}
