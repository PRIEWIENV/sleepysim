package com.sleepysim;


import javafx.util.Pair;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class Selfish_adversary implements Adversary
{
    private Integer n;
    private ArrayList<Pair<Integer, PrivateKey>> secret_key_table;
    private ArrayList<PublicKey> public_key_table;
    private Chain chain;
    private Integer D;
    private Integer T;
    private HashMap<byte[],Block> latest_blocks;
    private ArrayList<Transaction> mem_pool;
    private ArrayList<Corrupted_node> corrupt_nodes;
    private ArrayList<Block> public_main_block;
    private ArrayList<Integer> honest_nodes;
    private Block private_main_block;
    private ArrayList<Block> private_chain;
    private Integer public_chain_length;
    private Integer private_chain_length;
    private Network_control net;
    private Controller controller;
    /**
     * A naive adversary, you should break consistency if you have more node than honest
     * @param n number of corrupted nodes
     * @param secret_key_table secret key for each corrupted node, the Integer is node id, and String is the corresponding secret key
     * @param public_key_table public keys
     */
    public Selfish_adversary(Integer n,Controller controller,Boolean[] is_corrupted,ArrayList<Pair<Integer, PrivateKey>> secret_key_table, ArrayList<PublicKey> public_key_table,ArrayList<Corrupted_node> corrupt,Integer T, Network_control net)
    {
        this.n = n;
        this.T=T;
        this.honest_nodes = new ArrayList<>();
        for(int i = 0; i < n; ++i)
            if(!is_corrupted[i])
                this.honest_nodes.add(i);
        this.secret_key_table = secret_key_table;
        this.public_key_table = public_key_table;
        this.corrupt_nodes=corrupt;
        this.private_main_block=null;
        this.public_chain_length=1;
        this.private_chain_length=0;
        this.net=net;
        this.controller=controller;
        latest_blocks = new HashMap<>();
        chain = new Chain();

        mem_pool = new ArrayList<>();
        public_main_block = new ArrayList<>();
        private_chain = new ArrayList<>();
    }

    public boolean duplicate(Transaction e)
    {
        return false;
    }//same as naive


    public boolean check_validity(Block e, Integer round)//same as naive
    {
        if(!chain.chain.containsKey(e.get_last_hash()) && e.get_last_hash() != null)
            return false;//currently orphan blocks not considered
        if(e.get_time_stamp() > round)
            return false; // future blocks
        if(chain.chain.get(e.get_last_hash()) != null && e.get_time_stamp() < chain.chain.get(e.get_last_hash()).get_time_stamp())
            return false;
        if(!controller.is_leader(e.get_creator(), e.get_time_stamp()))
            return false;
        return true;
    }

    public Integer get_length(Block e)//same as naive
    {
        Integer length=0;
        while(e!=null)
        {
            ++length;
            e=chain.chain.get(e.get_last_hash());
        }
        return length;
    }

    public void update_public(Block e)// same as naive
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

    public void update_private(Block e)// same as naive
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

    public void disclose_all(Integer round)// send all the private chain out to honest nodes
    {
        for(Block e: private_chain)
        {
            for(Integer r: honest_nodes) {
                Message msg=new Message(new Honest_message(Honest_message.annonce_block, e));
                Message_to_send msg2 = new Message_to_send(msg, corrupt_nodes.get(0).request_id(),r, round+1,-1);
                net.receive_message_from_corrupted(msg2);
            }
        }
    }

    public void disclose_block(Block e,Integer round)// only send one private block out
    {
        Message msg=new Message(new Honest_message(Honest_message.annonce_block, e));
        for(Integer r: honest_nodes) {
            Message_to_send msg2 = new Message_to_send(msg, corrupt_nodes.get(0).request_id(),r, round+1,-1);
            net.receive_message_from_corrupted(msg2);
        }
    }

    /**
     * Your adversary algorithm
     */
    @Override
    public ArrayList<Block> run(Integer round)
    {
        //when others find a block
        ArrayList<Message_to_send> msg = net.send_message_to_corrupted();
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
                            if(chain.chain.containsKey(b.get_current_hash()))continue;
                            Integer pre=private_chain_length-public_chain_length;
                            update_public(b);
                            if(pre==0) // now, lag behind, then quit
                            {
                                private_chain.clear();
                                private_main_block=null;
                                private_chain_length=0;
                            }
                            else
                            {
                                if(pre==1)//now, draw, then disclose the lastest block
                                {
                                    disclose_block(private_main_block,round);
                                }
                                else
                                {
                                    if(pre==2)//now, ahead of one block, then disclose all
                                    {
                                        disclose_all(round);
                                        private_chain_length=0;
                                    }
                                    else//now, ahead of more than one, so only disclose the most original one
                                    {
                                        if(pre>2)disclose_block(private_chain.get(0),round);
                                    }
                                }
                            }
                        }
                        else continue;
                    }
                    else
                    {
                        if(!duplicate((Transaction) m.ctx)) mem_pool.add((Transaction) m.ctx);
                    }
                }
        }
        //the above is about updating information
        //the following is about when the attacker finds a block
        for(Corrupted_node n: corrupt_nodes)
        {
            if(controller.is_leader(n.request_id(), -1))
            {
                byte [] sig=null;
                byte [] hashvalue=null;
                byte [] prehash = null;
                Integer pre=private_chain_length-public_chain_length;
                if(private_main_block==null)
                {
                    if(public_main_block.size()!=0) prehash=public_main_block.get(0).get_last_hash();
                    else prehash=null;
                    try {
                        sig = Signature_tool.generate_signature(n.request_private_key(),
                                To_byte_array.to_byte_array(new Honest_node.Signature_elements(prehash, mem_pool, round)));
                        hashvalue = Hash.hash(To_byte_array.to_byte_array(new Honest_node.Hash_elements(prehash, mem_pool, round, n.request_id(), sig)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    prehash=private_main_block.get_current_hash();
                    try {
                        sig = Signature_tool.generate_signature(n.request_private_key(),
                                To_byte_array.to_byte_array(new Honest_node.Signature_elements(prehash, mem_pool, round)));
                        hashvalue = Hash.hash(To_byte_array.to_byte_array(new Honest_node.Hash_elements(prehash, mem_pool, round, n.request_id(), sig)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Block newblock=new Block(prehash, hashvalue, mem_pool, round, n.request_id(), sig);
                update_private(newblock);
                ++private_chain_length;
                if(pre==0 && private_chain_length==2)//currently lead only 1
                {
                    disclose_all(round);
                    private_chain_length=0;
                }
                mem_pool.clear();
                ArrayList<Block> b = new ArrayList<>();
                b.add(newblock);
                return b;
            }
        }
        //if leading T blocks, the broadcast the message and cause inconsistency
        if(private_chain_length > public_chain_length && (public_chain_length-get_length(private_chain.get(0))+1) > T)
        {
            disclose_all(round);
            ArrayList<Block> report = new ArrayList<>();
            Block cur = private_main_block;
            while(cur != null)
            {
                report.add(cur);
                cur = chain.chain.get(cur.get_last_hash());
            }
            return report;
        }
        return null;
    }
};
