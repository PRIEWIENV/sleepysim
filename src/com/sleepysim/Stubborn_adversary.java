package com.sleepysim;

import javafx.util.Pair;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class Stubborn_adversary implements Adversary{
    private Integer n;
    private ArrayList<Pair<Integer, PrivateKey>> secret_key_table;
    private ArrayList<PublicKey> public_key_table;
    private Chain chain;
    private Integer D;
    private Integer T;
    private Integer L;
    private HashMap<byte[],Block> latest_blocks;
    private ArrayList<Transaction> mem_pool;
    private ArrayList<Corrupted_node> corrupt_nodes;
    private ArrayList<Block> public_main_block;
    private ArrayList<Integer> honest_nodes;
    private Block private_main_block;
    private ArrayList<Block> private_chain;
    private Integer public_chain_length;
    private Integer private_chain_length;
    /**
     * A naive adversary, you should break consistency if you have more node than honest
     * @param n number of corrupted nodes
     * @param secret_key_table secret key for each corrupted node, the Integer is node id, and String is the corresponding secret key
     * @param public_key_table public keys
     */
    public Stubborn_adversary(Integer n, Integer L,ArrayList<Integer> honest_nodes,ArrayList<Pair<Integer, PrivateKey>> secret_key_table, ArrayList<PublicKey> public_key_table,ArrayList<Corrupted_node> corrupt, Block genesis, Integer D, Integer T)
    {
        this.n = n;
        this.D=D;
        this.T=T;
        this.L=L;
        this.honest_nodes=honest_nodes;
        this.secret_key_table = secret_key_table;
        this.public_key_table = public_key_table;
        this.corrupt_nodes=corrupt;
        this.private_main_block=null;
        this.public_chain_length=1;
        this.private_chain_length=0;
        chain.chain.put(genesis.get_current_hash(),genesis);
        latest_blocks.put(genesis.get_current_hash(),genesis);
    }

    public boolean duplicate(Transaction e)
    {
        return false;
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

    public boolean Isleader(Integer id, Integer round, Integer D)
    {
        try {
            byte [] tmp1=To_byte_array.to_byte_array(id);
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

    public boolean check_validity(Block e, Integer round)
    {
        if(!chain.chain.containsKey(e.get_last_hash())) return false;//currently orphan blocks not considered
        if(e.get_time_stamp() >= round)return false; // future blocks
        if(chain.chain.get(e.get_last_hash()) != null && e.get_time_stamp() < chain.chain.get(e.get_last_hash()).get_time_stamp())return false;
        if(!Isleader(e.get_creator(),round,D))return false;
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

    public void disclose_all(Integer round)
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

    public void disclose_block(Block e,Integer round)
    {
        Message msg=new Message(e);
        for(Corrupted_node n: corrupt_nodes)
        {
            n.send_message_corrputed(msg,n.request_id(),honest_nodes,round,-1);//-1 for own
        }
    }

    /**
     * Your adversary algorithm
     */
    @Override
    public ArrayList<Block> run(Integer round)
    {
        //when others find a block
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
                            if(chain.chain.containsKey(b.get_current_hash()))continue;
                            Integer pre=private_chain_length-public_chain_length;
                            update_public(b);
                            if(pre>=-L) // Trail-stubborn, only quit after lagging L blocks
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
                                    if(pre==2)//L-lead, only release one block
                                    {
                                        disclose_block(private_chain.get(0),round);
                                    }
                                    else//now, ahead of more than one, so only disclose the most original one
                                    {
                                        disclose_block(private_chain.get(0),round);
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
        }
        //the following is about when the attacker finds a block
        for(Corrupted_node n: corrupt_nodes)
        {
            if(Isleader(n.request_id(),round,D))
            {
                byte [] sig=null;
                byte [] hashvalue=null;
                byte [] prehash = null;
                Integer pre=private_chain_length-public_chain_length;
                if(private_main_block==null)
                {
                    prehash=public_main_block.get(0).get_current_hash();
                    try {
                        sig = Signature_tool.generate_signature(n.request_private_key(),
                                To_byte_array.to_byte_array(new Honest_node.Signature_elements(prehash, mem_pool, round)));
                        hashvalue = Hash.hash(To_byte_array.to_byte_array(new Honest_node.Hash_elements(prehash, mem_pool, round, n.request_id(), sig)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Block newblock=new Block(prehash, hashvalue, mem_pool, round, n.request_id(), sig);
                    update_private(newblock);
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
                    Block newblock=new Block(prehash, hashvalue, mem_pool, round, n.request_id(), sig);
                    update_private(newblock);
                }
                ++private_chain_length;
                if(pre==0 && private_chain_length==2)//Equal fork stubborn
                {
                    //not reveal the block, still stick to the private chain
                }
                mem_pool.clear();
                break;
            }
        }
        //if leading T blocks, the broadcast the message and cause inconsistency
        if(private_chain_length-public_chain_length>=T)
        {
            disclose_all(round);
        }
        return null;
    }
}
