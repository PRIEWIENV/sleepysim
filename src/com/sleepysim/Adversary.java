package com.sleepysim;

import javafx.util.Pair;

import java.util.ArrayList;

public interface Adversary
{
    /**
     * Adversary operates the corrupted node
     * @param node the instance of corrupted node
     */
    public ArrayList<Block> run(Corrupted_node node, Integer round);
}
