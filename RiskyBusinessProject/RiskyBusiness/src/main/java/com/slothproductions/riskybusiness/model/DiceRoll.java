package com.slothproductions.riskybusiness.model;

import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Random;

public class DiceRoll {


    private static Random randDice1 = new SecureRandom();
    private static Random randDice2 = new SecureRandom();

    private int first;
    private int second;
    private int result;

    public void roll() {
        first = 1 + randDice1.nextInt(6);
        second = 1 + randDice2.nextInt(6);
        result = first+second;
    }

    public int getFirstDice() {return first;}
    public int getSecondDice() {return second;}
    public int getResults() {return result;}
}