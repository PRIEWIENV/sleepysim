package com.sleepysim;

public class Message
{
    /**
     * context of message, can be any type
     */
    private Object ctx;

    Message(Object ctx)
    {
        this.ctx = ctx;
    }

    /**
     * get message
     * @return the message
     */
    public Object get_message()
    {
        return ctx;
    }

    /**
     * set message
     * @param ctx set the message, typically you cannot do this
     */
    public void set_message(Object ctx)
    {
        this.ctx = ctx;
    }
}
