package com.sleepysim;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main
{
    //test the influence of the number of adversary nodes on the protocol.
    public static void testAdversary(int lowerbound, int upbound){
        for(int i = lowerbound; i <= upbound; i++){
            Controller controller = new Controller(100, i, 1, 10, 0.0001);
            controller.run();
        }
    }

    //test the influence of mining difficulty on the protocol.
    public static void testDifficulty(int adversary_count){
        double difficulty = 0.0001;
        for(int i = 1; i <= 4; i++){
            System.out.println("Now we have " + adversary_count + " adversary nodes and the difficulty is " + difficulty + "!");
            Controller controller = new Controller(100, adversary_count, 1, 10, difficulty);
            controller.run();
            difficulty *= 10;
        }
    }

    //test the influence of delay time on the protocol.
    public static void testDelay(int adversary_count, double difficulty){
        for(int i = 1; i <= 5; i++){
            System.out.println("Now delay is " + i + "!");
            Controller controller = new Controller(100, adversary_count, i, 10, difficulty);
            controller.run();
        }
    }

    public static void main(String[] args)
    {
	// write your code here
        Security.addProvider(new BouncyCastleProvider());
        testAdversary(40, 60);
        testDifficulty(60);
        testDelay(60, 0.0001);
    }
}
