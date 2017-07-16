package com.sleepysim;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main
{
    //test the influence of the number of adversary nodes on the protocol.
    public static void testAdversary(int lowerbound, int upbound){
        Controller controller;
        for(int i = lowerbound; i <= upbound; i++){
            Protocol protocol = new Protocol(100, i, 1, 10, 0.01);
            controller = new Controller(protocol);
            controller.run();
        }
    }

    //test the influence of mining difficulty on the protocol.
    public static void testDifficulty(int adversary_count){
        double difficulty = 0.0001;
        Controller controller;
        for(int i = 1; i <= 4; i++){
            System.out.println("Now we have " + adversary_count + " adversary nodes and the difficulty is " + difficulty + "!");
            Protocol protocol = new Protocol(100, adversary_count, 1, 10, difficulty);
            controller = new Controller(protocol);
            controller.run();
            difficulty *= 10;
        }
    }

    //test the influence of delay time on the protocol.
    public static void testDelay(int adversary_count, double difficulty){
        Controller controller;
        for(int i = 1; i <= 5; i++){
            System.out.println("Now delay is " + i + "!");
            Protocol protocol = new Protocol(100, adversary_count, i, 10, difficulty);
            controller = new Controller(protocol);
            controller.run();
        }
    }

    public static void main(String[] args)
    {
	// write your code here
        Security.addProvider(new BouncyCastleProvider());
        testAdversary(51, 60);
        testDifficulty(60);
        testDelay(60, 0.0001);
    }
}
