package com.sleepysim;

import java.io.IOException;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
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
    private Network_control networkcontrol;
    private Integer num_node;
    private ArrayList<Integer> all_set;
    private Controller controller;
    private HashMap<byte[], Block> store_pool;
    /**
     * Initialize the node
     * @param id your unique id
     * @param secret_key your secret key
     * @param public_key list of public key
     */
    public Honest_node(Integer id, PrivateKey secret_key, ArrayList<PublicKey> public_key, Network_control networkcontrol, Integer num_node,
                       Controller controller)
    {
        this.controller = controller;
        this.secret_key = secret_key;
        this.public_key = public_key;
        this.id = id;
        logger = Logger.getLogger("Honest Node " + id.toString());
        this.chain = new Chain();
        max_length = -1;
        working_branch = null;
        this.networkcontrol = networkcontrol;
        this.num_node = num_node;
        all_set = new ArrayList<>();
        for(int i = 0; i < num_node; ++i)
            all_set.add(i);
        store_pool = new HashMap<>();
        //some code goes here
        //for honest team
    }

    /**
     * send message to others
     * You should call function from networkcontrol, see Network_control.java for details
     * @param msg message
     * @param from your id, do not use other's id
     * @param to destination
     */
    @Override
    public void send_message(Message msg, Integer from, ArrayList<Integer> to)
    {
        //some code goes here
        //for honest team
        networkcontrol.receive_message_from_honest(msg, from, to);
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
        return networkcontrol.send_message(id);
    }

    /**
     * sign the message, you should call Signature_tool's function
     * @param msg the message
     * @return a signature
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
     * @param b the block
     */
    @Override
    public void update_chain(Block b)
    {
        Boolean succ = true;
        int length = 1;
        Block sav = b;
        ArrayList<Block> to_update = new ArrayList<>();
        to_update.add(b);
        while(b != null)
        {
            length++;
            if(b.get_last_hash() == null)
            {
                b = null;
                break;
            }
            if(chain.chain.containsKey(b.get_last_hash()))
            {
                b = chain.chain.get(b.get_last_hash());
            }
            else if(store_pool.containsKey(b.get_last_hash()))
            {
                b = store_pool.get(b.get_last_hash());
            }
            else
            {
                succ = false;
                break;
            }
            to_update.add(b);
        }
        if(!succ)
        {
            store_pool.put(b.get_current_hash(), b);
        }
        else
        {
            for(Block x : to_update)
                chain.chain.put(x.get_current_hash(), x);
            if (length > max_length)
            {
                logger.log(Level.INFO, "Honest node " + id.toString() + " switch branch to " + sav.get_current_hash().toString());
                logger.log(Level.INFO, "Chain length = " + length);
                max_length = length;
                working_branch = sav;
            }
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

    static class Signature_elements implements Serializable
    {
        public byte[] lastHash;
        public ArrayList<Transaction> txs;
        Integer t;
        Signature_elements(byte[] lastHash, ArrayList<Transaction> txs, Integer t)
        {
            this.lastHash = lastHash;
            this.txs = txs;
            this.t = t;
        }
    }

    static class Hash_elements implements Serializable
    {
        public byte[] lashHash;
        public ArrayList<Transaction> txs;
        public Integer t;
        public Integer uid;
        public byte[] sign;
        Hash_elements(byte[] lastHash, ArrayList<Transaction> txs, Integer t, Integer uid, byte[] sign)
        {
            this.lashHash = lastHash;
            this.txs = txs;
            this.t = t;
            this.uid = uid;
            this.sign = sign;
        }
    }

    /**
     * Start a round, you should take action
     */
    @Override
    public ArrayList<Block> run(Integer round)
    {
        //some code goes here
        //for honest team
        ArrayList<Transaction> txs = new ArrayList<>();
        ArrayList<Message> msg_buffer = receive_message();
        logger.log(Level.INFO, "round " + round.toString() + ", message " + msg_buffer.size());
        for (Message x :
                msg_buffer)
        {
            if (x.get_message() instanceof Honest_message)
            {
                Honest_message msg = (Honest_message) x.get_message();
                switch (msg.message_type)
                {
                    case Honest_message.annonce_block:
                        if(msg.ctx instanceof Block)
                        {
                            Block b = (Block)msg.ctx;
                            if(b.get_time_stamp() >= round)
                            {
                                logger.log(Level.WARNING, "Future block, ignore.");
                                continue;
                            }
                            if(chain.chain.get(b.get_last_hash()) != null &&
                                    b.get_time_stamp() < chain.chain.get(b.get_last_hash()).get_time_stamp())
                            {
                                logger.log(Level.WARNING, "Time stamp decrease, ignore.");
                                continue;
                            }
                            try
                            {
                                if (!controller.is_leader(b.get_creator(), b.get_time_stamp()) ||
                                        !Signature_tool.check_signature(public_key.get(b.get_creator()), b.get_signature(),
                                                To_byte_array.to_byte_array(new Signature_elements(b.get_last_hash(), b.get_txs(), b.get_time_stamp()))))
                                {
                                    logger.log(Level.WARNING, "Block creator invalid.");
                                    continue;
                                }
                            }
                            catch(Exception e)
                            {
                                logger.log(Level.WARNING, "Block creator invalid.");
                                continue;
                            }
                            update_chain((Block)msg.ctx);
                            continue;
                        }
                        break;
                    case Honest_message.annonce_transaction:
                        if(msg.ctx instanceof Transaction)
                        {
                            txs.add((Transaction)msg.ctx);
                            continue;
                        }
                        break;
                }
            }
            logger.log(Level.SEVERE, "Inconsistent message. Message type error.");
            return null;
        }
        if(controller.is_leader(id, -1))
        {
            logger.log(Level.INFO, "Honest node " + id.toString() + " is elected as leader.");
            byte[] sign;
            byte[] current_hash;
            byte[] last_hash = null;
            if(working_branch != null)
                last_hash = working_branch.get_current_hash();
            try
            {
                sign = Signature_tool.generate_signature(secret_key,
                        To_byte_array.to_byte_array(new Signature_elements(last_hash, txs, round)));
            }
            catch (IOException e)
            {
                logger.log(Level.SEVERE, "Object cannot convert to byte array. Sign");
                return null;
            }
            try
            {
                current_hash = Hash.hash(To_byte_array.to_byte_array(new Hash_elements(last_hash, txs, round, id, sign)));
            }
            catch (IOException e)
            {
                logger.log(Level.SEVERE, "Object cannot convert to byte array. Hash");
                return null;
            }
            Block b = new Block(last_hash, current_hash, txs, round, id, sign);
            update_chain(b);
            working_branch = b;
            send_message(new Message(new Honest_message(Honest_message.annonce_block, b)), id, all_set);
        }
        ArrayList<Block> report = new ArrayList<>();
        Block cur = working_branch;
        while(cur != null)
        {
            report.add(cur);
            cur = chain.chain.get(cur.get_last_hash());
        }
        return report;
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
