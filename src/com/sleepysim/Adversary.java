package com.sleepysim;

import javafx.util.Pair;

import java.util.ArrayList;

public interface Adversary
{
    /**
     * Adversary operates the corrupted node
     * @param node the instance of corrupted node
     */
    public void run(Corrupted_node node);
}
