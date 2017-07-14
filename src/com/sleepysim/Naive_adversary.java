package com.sleepysim;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

public class Naive_adversary implements Adversary
{
    private Integer n;
    private ArrayList<Pair<Integer, PrivateKey>> secret_key_table;
    private ArrayList<PublicKey> public_key_table;
    private Chain chain;
    private Integer T;
    private HashMap<byte[],Block> latest_blocks;
    private ArrayList<Transaction> mem_pool;
    private ArrayList<Corrupted_node> corrupt_nodes;
    private ArrayList<Block> public_main_block;
    private ArrayList<Integer> honest_nodes;
    private Block private_main_block;
    private ArrayList<Block> private_chain;
    private Integer public_chain_length;
    private Controller controller;
    private Integer private_chain_length;
    /**
     * A naive adversary, you should break consistency if you have more node than honest
     * @param n number of corrupted nodes
     * @param secret_key_table secret key for each corrupted node, the Integer is node id, and String is the corresponding secret key
     * @param public_key_table public keys
     */
    public Naive_adversary(Integer n, Boolean[] is_corrupted, ArrayList<Pair<Integer, PrivateKey>> secret_key_table,
                           ArrayList<PublicKey> public_key_table, ArrayList<Corrupted_node> corrupt,
                           Integer T, Controller controller)
    {
        this.controller = controller;
        this.n = n;
        this.T=T;
        //this.honest_nodes=honest_nodes;
        this.secret_key_table = secret_key_table;
        this.public_key_table = public_key_table;
        this.corrupt_nodes=corrupt;
        this.private_main_block=null;
        this.public_chain_length=1;
        this.private_chain_length=0;
        latest_blocks = new HashMap<>();
        chain = new Chain();

        mem_pool = new ArrayList<>();
        public_main_block = new ArrayList<>();
        private_chain = new ArrayList<>();
    }

    public boolean duplicate(Transaction e)
    {
        return false;
    }

    public boolean check_validity(Block e, Integer round)
    {
        if(!chain.chain.containsKey(e.get_last_hash())) return false;//currently orphan blocks not considered
        if(e.get_time_stamp() >= round)return false; // future blocks
        if(chain.chain.get(e.get_last_hash()) != null && e.get_time_stamp() < chain.chain.get(e.get_last_hash()).get_time_stamp())return false;
        if(!controller.is_leader(e.get_creator()))return false;
        return true;
    }

    public Integer get_length(Block e)
    {
        Integer length=0;
        while(e!=null)
        {
            ++length;
            e=chain.chain.get(e.get_last_hash());
        }
        return length;
    }

    public void update_public(Block e)
    {
        if(chain.chain.containsKey(e.get_current_hash()));
        else
        {
            chain.chain.put(e.get_current_hash(),e);
            Integer length=get_length(e);
            if(length>public_chain_length)
            {
                public_chain_length=length;
                public_main_block.clear();
                public_main_block.add(e);
            }
            else if(length==public_chain_length)public_main_block.add(e);
        }
        if(latest_blocks.containsKey(e.get_current_hash()));
        else
        {
            latest_blocks.remove(e.get_last_hash());
            latest_blocks.put(e.get_current_hash(),e);
        }
    }

    public void update_private(Block e)
    {
        if(chain.chain.containsKey(e.get_current_hash()));
        else
        {
            chain.chain.put(e.get_current_hash(),e);
            private_main_block=e;
            private_chain_length=get_length(e);
            private_chain.add(e);
        }
    }

    public void send_message(Integer round)
    {
        for(Block e: private_chain)
        {
            Message msg=new Message(e);
            for(Corrupted_node n: corrupt_nodes)
            {
                n.send_message_corrputed(msg,n.request_id(),honest_nodes,round,-1);//-1 for own
            }
        }
    }

    /**
     * Your adversary algorithm
     */
    @Override
    public ArrayList<Block> run(Integer round)
    {
        for(Corrupted_node n: corrupt_nodes) {
            ArrayList<Message_to_send> msg = n.intercept_message();
            for(int j=0;j<msg.size();++j)
            {
                if(msg.get(j).get_message().get_message() instanceof Honest_message)
                {
                    Honest_message m=(Honest_message) msg.get(j).get_message().get_message();
                    if(m.message_type==Honest_message.annonce_block)
                    {
                        Block b=(Block) m.ctx;
                        if(check_validity((Block) m.ctx, round))
                        {
                            update_public(b);
                        }
                        else continue;
                    }
                    else
                    {
                        if(!duplicate((Transaction) m.ctx)) mem_pool.add((Transaction) m.ctx);
                    }
                }
            }
        }
        //the following is about how to attack
        for(Corrupted_node n: corrupt_nodes)
        {
            if(controller.is_leader(n.request_id()))
            {
                byte [] sig=null;
                byte [] hashvalue=null;
                byte [] prehash = null;
                if(private_main_block==null) {
                    try {
                        prehash=public_main_block.get(0).get_current_hash();
                        sig = Signature_tool.generate_signature(n.request_private_key(),
                                To_byte_array.to_byte_array(new Honest_node.Signature_elements(prehash, mem_pool, round)));
                        hashvalue = Hash.hash(To_byte_array.to_byte_array(new Honest_node.Hash_elements(prehash, mem_pool, round, n.request_id(), sig)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        prehash=private_main_block.get_current_hash();
                        sig = Signature_tool.generate_signature(n.request_private_key(),
                                To_byte_array.to_byte_array(new Honest_node.Signature_elements(prehash, mem_pool, round)));
                        hashvalue = Hash.hash(To_byte_array.to_byte_array(new Honest_node.Hash_elements(prehash, mem_pool, round, n.request_id(), sig)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Block newblock=new Block(prehash, hashvalue, mem_pool, round, n.request_id(), sig);
                update_private(newblock);
                mem_pool.clear();
                break;
            }
        }
        //some optimization for the naive method
        if(public_chain_length-private_chain_length>=T)
        {
            private_chain.clear();
            private_main_block=null;
        }
        //if leading T blocks, the broadcast the message
        if(private_chain_length-public_chain_length>=T)
        {
            send_message(round);
            return private_chain;
        }
        return null;
    }
}
