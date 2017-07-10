package com.sleepysim;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
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
    private Framework framework;
    private Integer num_node;
    private ArrayList<Integer> all_set;
    /**
     * Initialize the node
     * @param id your unique id
     * @param secret_key your secret key
     * @param public_key list of public key
     */
    public Honest_node(Integer id, PrivateKey secret_key, ArrayList<PublicKey> public_key, Framework framework, Integer num_node)
    {
        this.secret_key = secret_key;
        this.public_key = public_key;
        this.id = id;
        logger = Logger.getLogger("Honest Node " + id.toString());
        this.chain = new Chain();
        max_length = -1;
        working_branch = null;
        this.framework = framework;
        this.num_node = num_node;
        all_set = new ArrayList<>();
        for(int i = 0; i < num_node; ++i)
            all_set.add(i);
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

    private boolean isleader(Integer round)
    {
        return false;
    }

    class Signature_elements implements Serializable
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

    class Hash_elements implements Serializable
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
        if(isleader(round))
        {
            byte[] sign;
            try
            {
                sign = Signature_tool.generate_signature(secret_key,
                        To_byte_array.to_byte_array(new Signature_elements(working_branch.get_current_hash(), txs, round)));
            }
            catch (IOException e)
            {
                logger.log(Level.SEVERE, "Object cannot convert to byte array. Sign");
                return null;
            }
            byte[] current_hash;
            try
            {
                current_hash = Hash.hash(To_byte_array.to_byte_array(new Hash_elements(working_branch.get_current_hash(), txs, round, id, sign)));
            }
            catch (IOException e)
            {
                logger.log(Level.SEVERE, "Object cannot convert to byte array. Hash");
                return null;
            }
            Block b = new Block(working_branch.get_last_hash(), current_hash, txs, round, id, sign);
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
