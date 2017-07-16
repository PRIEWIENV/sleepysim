package com.sleepysim;

import javafx.util.Pair;
import org.bouncycastle.asn1.eac.UnsignedInteger;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller
{
    public Protocol protocol;

    Controller(Protocol protocol)
    {
        this.protocol = protocol;
    }

    public void run()
    {
        while (!protocol.run());
    }
}
