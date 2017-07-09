package com.sleepysim;

import java.util.ArrayList;

public class Block
{
    private Byte[] last_hash, current_hash;
    private ArrayList<Transaction> txs;
    private Integer time_stamp;
    private Integer creator;
    private String signature;
    Block(Byte[] last_hash, Byte[] current_hash, ArrayList<Transaction> txs, Integer time_stamp, Integer creator
    , String signature)
    {

    }
}
