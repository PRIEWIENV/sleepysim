package com.sleepysim;

public class Transaction
{
    private Integer from, to;
    private Integer amount;
    Transaction(Integer from, Integer to, Integer amount)
    {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
    public Integer get_from()
    {
        return from;
    }
    public Integer get_to()
    {
        return to;
    }
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
