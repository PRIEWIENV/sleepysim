package com.sleepysim;

public class Main
{
    public static void main(String[] args)
    {
	// write your code here
        Controller controller = new Controller(10, 3, 1, 6, 0.2);
        controller.run();
    }
}
