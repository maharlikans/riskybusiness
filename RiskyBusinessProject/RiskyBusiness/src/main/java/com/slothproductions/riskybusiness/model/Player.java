package com.slothproductions.riskybusiness.model;

import java.util.ArrayList;
import java.util.Map;

public class Player {
    final private Board.PlayerAccounting.ImmutablePlayerAccounting accounting;

    public Player(Board.PlayerAccounting.ImmutablePlayerAccounting a) {
        accounting = a;
    }

    public void effect(GameAction action) {
        /* TODO: Code to effect action */
    }
}
