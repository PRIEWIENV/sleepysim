package com.sleepysim;

public class Message
{
    private Object ctx;
    Message(Object ctx)
    {
        this.ctx = ctx;
    }
    public Object get_message()
    {
        return ctx;
    }
    public void set_message(Object ctx)
    {
        this.ctx = ctx;
    }
}
