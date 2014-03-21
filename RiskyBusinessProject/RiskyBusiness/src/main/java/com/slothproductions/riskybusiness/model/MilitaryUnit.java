package com.slothproductions.riskybusiness.model;

/**
 * Created by riv on 16.03.14.
 */
public class MilitaryUnit {
    final class ImmutableMilitaryUnit {
        public Player getPlayer() {
            return player;
        }

        public int getHealth() {
            return health;
        }

        public Vertex.ImmutableVertex getLocation() {
            return location.immutable;
        }
    }

    final private Player player;
    private int health;
    private Vertex location;

    public MilitaryUnit(Vertex v) {
        player = v.owner;
        location = v;
    }

    public Player getPlayer() {
        return player;
    }

    public int getHealth() {
        return health;
    }
}
