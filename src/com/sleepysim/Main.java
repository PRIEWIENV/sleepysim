package com.sleepysim;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.security.Security;

public class Main
{
    //test the influence of the number of adversary nodes on the protocol.
    public static void testAdversary(int lowerbound, int upbound){
        Controller controller;
        for(int i = lowerbound; i <= upbound; i++){
            for(int j=1;j<=4;++j) {
                Protocol protocol = new Protocol(100, i, j, 10, -1);
                controller = new Controller(protocol);
                controller.run();
                System.err.println(i + " " + j + " " + "finish");
                controller.destroy();
                protocol = null;
                controller = null;
            }
        }
    }

    //test the influence of mining difficulty on the protocol.
    public static void testDifficulty(int adversary_count){
        double difficulty = 0.001;
        Controller controller;
        for(int i = 1; i <= 3; i++){
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
            for (int i = 1; i <= 5; i++)
            {
                System.out.println("Now delay is " + i + "!");
                Protocol protocol = new Protocol(100, adversary_count, i, 10, difficulty);
                controller = new Controller(protocol);
                controller.run();
            }
    }

    public static void main(String[] args)
    {
	// write your code here
        try
        {
            System.setOut(new PrintStream(new FileOutputStream("naive_result" + args[0] + ".txt")));
        }
        catch (FileNotFoundException e)
        {}
        Security.addProvider(new BouncyCastleProvider());
        for(int i = 0; i < 20; ++i)
            testAdversary(40, 60);
    }
}
