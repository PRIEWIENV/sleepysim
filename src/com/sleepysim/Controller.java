package com.sleepysim;

import javafx.util.Pair;
import org.bouncycastle.asn1.eac.UnsignedInteger;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller
{
    public Protocol protocol;

    Controller(Protocol protocol)
    {
        this.protocol = protocol;
    }

    /**
     * Print necessary info to screen
     * You should include total number of corrupted blocks in longest chain and length of longest chain
     */
    private void print_log()
    {
        //some code goes here
        //for integrate team
    }
    public void run()
    {
        boolean has_inconsistency = false;
        while (!has_inconsistency)
        {
            for (int i = 0; i < node_count; i ++)
            {
                ArrayList <Block> block_list = nodes.get(i).run(round);
                if(block_list != null && minChainlength > block_list.size())
                    minChainlength = block_list.size();
                has_inconsistency |= has_inconsistency(block_list);
            }
            {
                ArrayList <Block> block_list = adversary.run(round);
            }
            //If the chain length is more than 50, then we think that he adversary can not break the consistency!
            if(minChainlength > 50){
                System.out.print("The adversary can not break the consistency!");
                return;
            }
            print_log();
            networkcontrol.next_round();
            round++;
        }
        print_log();
    }
}
