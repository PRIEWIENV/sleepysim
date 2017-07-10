package com.sleepysim;

public class Honest_message
{
    /**
     * Assert: message_type == 0 -> typeof ctx is Block
     * Assert: message_type == 1 -> typeof ctx is Transaction
     */
    public static final int annonce_block = 0;
    public static final int annonce_transaction = 1;
    Integer message_type;
    Object ctx;
    Honest_message(Integer message_type, Object ctx)
    {
        this.message_type = message_type;
        this.ctx = ctx;
    }
}
