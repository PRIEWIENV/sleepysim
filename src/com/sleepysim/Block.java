package com.sleepysim;

import java.util.ArrayList;

public class Block
{
    private Byte[] last_hash, current_hash;
    private ArrayList<Transaction> txs;
    private Integer time_stamp;
    private Integer creator;
    private Byte[] signature;
    Block(Byte[] last_hash, Byte[] current_hash, ArrayList<Transaction> txs, Integer time_stamp, Integer creator
    , Byte[] signature)
    {
        this.last_hash = last_hash;
        this.current_hash = current_hash;
        this.creator = creator;
        this.txs = txs;
        this.time_stamp = time_stamp;
        this.signature = signature;
    }
    public Byte[] get_last_hash()
    {
        return last_hash;
    }
    public Byte[] get_current_hash()
    {
        return current_hash;
    }
    public Integer get_time_stamp()
    {
        return time_stamp;
    }
    public Integer get_creator()
    {
        return creator;
    }
    public Byte[] get_signature()
    {
        return signature;
    }
}
