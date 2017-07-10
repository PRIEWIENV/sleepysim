package com.sleepysim;

import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable
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
    public final Byte[] get_last_hash()
    {
        return last_hash;
    }
    public final Byte[] get_current_hash()
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
    public final Byte[] get_signature()
    {
        return signature;
    }
    public final ArrayList<Transaction> get_txs() { return txs; }
}
