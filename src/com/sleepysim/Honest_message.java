package com.sleepysim;

public class Honest_message
{
    /**
     * Assert: message_type == 0 -> typeof ctx is Block
     * Assert: message_type == 1 -> typeof ctx is Transaction
     */
    final static Integer annonce_block = 0;
    final static Integer annonce_transaction = 1;
    Integer message_type;
    Object ctx;
}
