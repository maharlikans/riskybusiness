package com.slothproductions.riskybusiness.model;

/**
 * Created by riv on 16.03.14.
 */
public class MilitaryUnit implements java.io.Serializable {
    private static final long serialVersionUID = -967850348L;
    final class ImmutableMilitaryUnit {
        public Player getPlayer() {
            return player;
        }

        public int getHealth() {
            return health;
        }

        public int getHaveNotMoved() { return haveNotMoved; }

        public int getHaveBonusMoved() { return haveBonusMoved; }

        public int getHaveMoved() { return haveMoved; }

        public Vertex.ImmutableVertex getLocation() {
            return location.immutable;
        }
    }

    final private Player player;
    private int health;
    private Vertex location;

    // The amount of soldiers in the 3 states. Should sum to health.
    private int haveNotMoved;
    private int haveBonusMoved;
    private int haveMoved;

    public MilitaryUnit(Vertex v) {
        player = v.owner;
        location = v;
        health = 1;
        haveNotMoved = 1;
        haveMoved = 0;
        haveBonusMoved = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public int getHealth() {
        return health;
    }
}
