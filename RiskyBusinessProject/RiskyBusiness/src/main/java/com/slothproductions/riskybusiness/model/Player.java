package com.slothproductions.riskybusiness.model;

import java.util.ArrayList;
import java.util.Map;

public class Player {
    final String name;

    final private Board.PlayerAccounting.ImmutablePlayerAccounting accounting;

    public Player(String n, Board.PlayerAccounting.ImmutablePlayerAccounting a) {
        name = n;
        accounting = a;
    }

    public GameAction.ActionWrapper effect(GameAction action, Map<String, Object> arguments) {
        /* TODO: Encapsulate any necessary exceptions */
        return accounting.getBoard().effect(this, action, arguments);
    }
}
