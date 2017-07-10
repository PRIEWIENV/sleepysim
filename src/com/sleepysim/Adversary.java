package com.sleepysim;

import javafx.util.Pair;

import java.util.ArrayList;

public interface Adversary
{
    /**
     * Adversary operates the corrupted node
     * @param round current_round
     */
    public void run(Integer round);
}
