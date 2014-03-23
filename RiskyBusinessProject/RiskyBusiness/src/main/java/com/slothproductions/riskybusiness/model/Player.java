package com.slothproductions.riskybusiness.model;

import java.util.ArrayList;
import java.util.Map;

public class Player {
    final private Board.PlayerAccounting.ImmutablePlayerAccounting accounting;

    public Player(Board.PlayerAccounting.ImmutablePlayerAccounting a) {
        accounting = a;
    }

    public GameAction.ActionWrapper effect(GameAction action, Map<String, Object> arguments) {
        /* TODO: Encapsulate any necessary exceptions */
        return accounting.getBoard().effect(this, action, arguments);
    }
}
