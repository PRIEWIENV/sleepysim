package com.sleepysim;

import java.io.Serializable;
import java.util.HashMap;

public class Chain implements Serializable
{
    public HashMap<byte[], Block> chain;
    Chain()
    {
        chain = new HashMap<>();
    }
}
