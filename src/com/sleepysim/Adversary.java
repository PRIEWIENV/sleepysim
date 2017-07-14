package com.sleepysim;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public interface Adversary
{
    /**
     * Adversary operates the corrupted node
     * @param round current_round
     */
    public ArrayList<Block> run(Integer round);
    public static Boolean[] decide_corrupted(Integer n, Integer adversary_count)
    {
        Boolean[] is_corrupted = new Boolean[n];
        Random random = new Random();
        int cnt = 0;
        for(int i = 0; i < n; ++i)
            is_corrupted[i] = false;
        while (cnt < adversary_count)
        {
            int x = Math.abs(random.nextInt() % n);
            if (!is_corrupted[x])
            {
                is_corrupted[x] = true;
                cnt ++;
            }
        }
        return is_corrupted;
    }
}
