package com.slothproductions.riskybusiness.model;

import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Random;

public class DiceRoll {
    private static Random rng = new SecureRandom();

    public static class RollTriplet {
        public final byte first;
        public final byte second;
        public final byte result;

        protected RollTriplet(int f, int s) {
            if (f >= 1 && f <= 6 && s >= 1 && s <= 6) {
                first = (byte)f;
                second = (byte)s;
                result = (byte)(f+s);
            } else {
                throw new InvalidParameterException();
            }
        }
    }

    public static RollTriplet roll() {
        int r = rng.nextInt(35);
        return new RollTriplet(r/6 + 1, r%6 + 1);
    }
}