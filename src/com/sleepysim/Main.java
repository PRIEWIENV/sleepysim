package com.sleepysim;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main
{
    public static void main(String[] args)
    {
	// write your code here
        Security.addProvider(new BouncyCastleProvider());
        Controller controller = new Controller(100, 40, 1, 10, 0.001);
        controller.run();
    }
}
