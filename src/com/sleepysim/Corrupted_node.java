package com.sleepysim;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Corrupted_node implements Node
{
    private Adversary adversary;
    private Integer id;
    private PrivateKey secret_key;
    private final ArrayList<PublicKey> public_key;
    private Network_control networkcontrol;
    private Integer num_node;

    public void destroy()
    {
        networkcontrol = null;
        secret_key = null;
        adversary = null;
    }

    /**
     * Corrupted node is controlled by adversary
     * @param id Your unique id
     * @param adversary Your god
     */

    public Corrupted_node(Integer id, Adversary adversary, PrivateKey secret_key, ArrayList<PublicKey> public_key, Network_control networkcontrol, Integer num_node)
    {
        this.id = id;
        this.adversary = adversary;
        this.secret_key = secret_key;
        this.public_key = public_key;
        this.networkcontrol = networkcontrol;
        this.num_node = num_node;
    }

    public PrivateKey request_private_key()
    {
        return secret_key;
    }

    public Integer request_id()
    {
        return id;
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
        //ignored for corrupted nodes
    }
    public void send_message_corrputed(Message msg, Integer from,ArrayList<Integer> to,Integer round, Integer uid)
    {
        for(int i=0;i<to.size();++i) {
            Message_to_send tmp=new Message_to_send(msg,from,to.get(i),round,uid);
            networkcontrol.receive_message_from_corrupted(tmp);
        }
    }
    /**
     * Get message from honest node
     * See message_to_send for details
     * @return a list of intercepted message
     */
    public ArrayList<Message_to_send> intercept_message()
    {
        return networkcontrol.send_message_to_corrupted();
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
    public void update_chain(Block b, Integer round)
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
        //this part ignored in corrupt nodes, adverasry will do the running job
        return null;
    }

    /**
     * Print log for debug
     */
    public void print_log()
    {

    }
}
