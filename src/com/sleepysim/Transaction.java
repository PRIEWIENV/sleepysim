package com.sleepysim;

import java.io.Serializable;

public class Transaction implements Serializable
{
    private Integer from, to;
    private Integer amount;
    private Integer uid;
    private static Integer next_uid = 0;
    Transaction(Integer from, Integer to, Integer amount)
    {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.uid = next_uid++;
    }
    public Integer get_from()
    {
        return from;
    }
    public Integer get_to()
    {
        return to;
    }
    public Integer get_uid() { return uid; }
    public Integer get_amount()
    {
        return amount;
    }
    @Override
    public String toString()
    {
        return from.toString() + " " + to.toString() + " " + amount.toString();
    }
}
