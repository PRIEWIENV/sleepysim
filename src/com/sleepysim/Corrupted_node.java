package com.sleepysim;

import java.awt.Frame;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Corrupted_node implements Node
{
    private Adversary adversary;
    private Integer id;
    private PrivateKey secret_key;
    private final ArrayList<PublicKey> public_key;
    private Framework framework;
    private Integer num_node;

    /**
     * Corrupted node is controlled by adversary
     * @param id Your unique id
     * @param adversary Your god
     */

    public Corrupted_node(Integer id, Adversary adversary, PrivateKey secret_key, ArrayList<PublicKey> public_key, Framework framework, Integer num_node)
    {
        this.id = id;
        this.adversary = adversary;
        this.secret_key = secret_key;
        this.public_key = public_key;
        this.framework = framework;
        this.num_node = num_node;
    }

    public static int byteArrayToInt(byte[] b)
    {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public boolean Isleader(Integer round, Integer D)
    {
        try {
            byte [] tmp1=To_byte_array.to_byte_array(public_key.get(id));
            byte [] tmp2=To_byte_array.to_byte_array(round);
            byte[] combined = new byte[tmp1.length + tmp2.length];
            for (int i = 0; i < combined.length; ++i)
            {
                combined[i] = i < tmp1.length ? tmp1[i] : tmp2[i - tmp1.length];
            }
            return byteArrayToInt(combined)<=D?true:false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
        //ignored for corrupted_nodes
    }
    public void send_message_corrputed(Message msg, Integer from,ArrayList<Integer> to,Integer round, Integer uid)
    {
        for(int i=0;i<to.size();++i) {
            Message_to_send tmp=new Message_to_send(msg,from,to.get(i),round,uid);
            framework.receive_message_from_corrupted(tmp);
        }
    }
    /**
     * Get message from honest node
     * See message_to_send for details
     * @return a list of intercepted message
     */
    public ArrayList<Message_to_send> intercept_message()
    {
        return framework.send_message_to_corrupted();
    }

    /**
     * Receive message from other node
     * @return a list of message
     */
    @Override
    public ArrayList<Message> receive_message()
    {
        //ignored for corrupted_node
        return null;
    }

    /**
     * generate a signature of msg, signed by you
     * You should call signature class
     * @param msg message to sign
     * @return a signature
     */
    @Override
    public byte[] request_signature(Message msg)
    {
        try {
            return Signature_tool.generate_signature(secret_key,To_byte_array.to_byte_array(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return Signature_tool.check_signature(public_key.get(id),signature,data);
    }

    /**
     * the network has a new Block, you have to update your chain
     * @param b the new block
     */
    @Override
    public void update_chain(Block b)
    {
        //ignored for corrupted nodes, adversary will do this
    }
    /**
     * Provide history Transactions to Controller, Controller will call this function when it need
     * @return history
     */
    @Override
    public ArrayList<Transaction> provide_history()
    {
        //ignored
        return null;
    }

    /**
     * directly call adversary to take action
     */
    @Override
    public ArrayList<Block> run(Integer round)
    {
        return adversary.run(this, round);
    }

    /**
     * Print log for debug
     */
    public void print_log()
    {

    }
}
