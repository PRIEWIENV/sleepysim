package com.sleepysim;

import javafx.util.Pair;

import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Protocol {
    private static Logger logger = Logger.getLogger("Protocol");
    //private byte[] D; //difficulty
    private double D;
    private Integer round;
    private Network_control networkcontrol;
    private Signature_tool signature;
    private Adversary adversary;
    private Integer node_count, adversary_count;
    private Integer delay;
    private Integer T;
    private ArrayList<Node> nodes;
    private Boolean[] is_corrupted;
    private double difficulty;
    private Random rand;
    private HashMap<Pair<Integer, Integer>, Double> random_result;
    public void destroy()
    {
        networkcontrol = null;
        signature = null;
        adversary.destroy();
        adversary = null;
        for(int i = 0; i < nodes.size(); ++i)
        {
            nodes.get(i).destroy();
        }
        nodes.clear();
        nodes = null;
    }
    public boolean is_leader(Integer id, Integer round)
    {
        if(round == -1)
            round = this.round;
        try
        {
            /*byte[] b = Hash.hash(To_byte_array.to_byte_array(new Pair<>(id, round)));
            for(int i = 0; i < b.length; ++i)
            {
                if(b[i] != D[i])
                {
                    Boolean result = (int)(b[i] & 0xff) < (int)(D[i] & 0xff);
                    //if(result)
                    //    logger.log(Level.INFO, "Round " + round.toString() + ", elected leader " + id.toString());
                    return result;
                }
            }
            return false;*/
            Pair<Integer, Integer> r = new Pair<>(id, round);
            if(random_result.containsKey(r))
                return random_result.get(r) < D;
            else
            {
                Double new_d = rand.nextDouble();
                random_result.put(r, new_d);
                return new_d < D;
            }
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, "leader election failed, id = " + id.toString());
            e.printStackTrace();
        }
        return false;
    }
    Protocol(Integer node_count, Integer adversary_count, Integer delay, Integer T, double difficulty)
    {
        if(difficulty < 0)
        {
            double v = 0.1;
            double rho = (double)adversary_count / node_count;
            if(rho >= 0.5 - 0.001)
            {
                rho = 0.45;
            }
            double up = (1 - (1 + v) * rho / (1 - rho)) / (2 * node_count * delay);
            double eps = 0.1;
            difficulty = up * (1 - eps);
        }
        System.out.println("Setting: node count = " + node_count + ", adversary count = " + adversary_count + ", delay = " + delay + ", T = " + T + ", difficulty = " + difficulty);
        /*
        BigInteger b = BigInteger.valueOf(2).pow(256).subtract(BigInteger.ONE);
        BigDecimal bd = new BigDecimal(b);
        bd = bd.multiply(BigDecimal.valueOf(difficulty));
        b = bd.toBigInteger();
        D = new byte[256 / 8];
        for(int i = 256 / 8 - 1; i >= 0; --i)
        {
            D[i] = (byte)b.mod(BigInteger.valueOf(1 << 8)).intValue();
            b = b.divide(BigInteger.valueOf(1 << 8));
        }
        */
        rand = new Random();
        D = difficulty;
        random_result = new HashMap<>();
        round = 0;
        this.node_count = node_count;
        this.adversary_count = adversary_count;
        this.delay = delay;
        this.T = T;
        this.nodes = new ArrayList<>();
        is_corrupted = Adversary.decide_corrupted(node_count, adversary_count);

        ArrayList<Pair<Integer, PrivateKey>> secret_key_table = new ArrayList<>();
        try
        {
            KeyPairGenerator key_generator = KeyPairGenerator.getInstance("RSA");
            ArrayList <PublicKey> public_key_table = new ArrayList<>();
            ArrayList <Corrupted_node> corrupted = new ArrayList<>();
            this.networkcontrol = new Network_control(delay, node_count);
            KeyPair new_key = key_generator.generateKeyPair();
            for (int i = 0; i < node_count; i ++)
            {
                public_key_table.add(new_key.getPublic());
                if (is_corrupted[i])
                {
                    Corrupted_node e = new Corrupted_node(i, adversary, new_key.getPrivate(), public_key_table, networkcontrol, node_count);
                    nodes.add(e);
                    secret_key_table.add(new Pair<>(i, new_key.getPrivate()));
                    corrupted.add(e);
                }
                else
                    nodes.add(new Honest_node(i, new_key.getPrivate(), public_key_table, networkcontrol, node_count, this));
                //System.out.println(i);
            }
            //System.out.println("Key generated");
            adversary = new Naive_adversary(node_count, is_corrupted, secret_key_table, public_key_table, corrupted, T, this,networkcontrol);
        }
        catch (NoSuchAlgorithmException e)
        {
        }
        //some code goes here
        //for integrate team
    }
    /**
     * Detect inconsistency
     * if the last T+1 blocks are generated by adversary, then return true
     * Otherwise return false
     */
    public boolean check_terminate(ArrayList <Block> block_list)
    {
        if(block_list == null)
            return false;
        //some code goes here
        //for integrate team
        if (block_list.size() < T + 1)
            return false;
        for (int i = block_list.size() - 1; i > block_list.size() - T - 1; i --)
            if (!is_corrupted[block_list.get(i).get_creator()])
                return false;
        int minChainlength = 1000;
        return true;
    }
    public boolean run()
    {
        //System.out.println("Round: " + round);
        Integer minChainlength = 1000000000;
        Boolean has_inconsistency = false;
        for (int i = 0; i < node_count; i ++)
        {
            ArrayList <Block> block_list = nodes.get(i).run(round);
            if(block_list != null && minChainlength > block_list.size())
                minChainlength = block_list.size();
            has_inconsistency |= check_terminate(block_list);
        }
        {
            ArrayList <Block> block_list = adversary.run(round);
        }
        //If the chain length is more than 100, then we think that he adversary can not break the consistency!
        if(minChainlength > 500){
            System.out.println("The adversary can not break the consistency!");
            return true;
        }
        networkcontrol.next_round();
        round++;
        if(has_inconsistency)
            System.out.println("The adversary success.");
        return has_inconsistency;
    }
}
