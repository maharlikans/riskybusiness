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
    protected int health;
    private Vertex location;

    // The amount of soldiers in the 3 states. Should sum to health.
    protected int haveNotMoved;
    protected int haveBonusMoved;
    protected int haveMoved;

    public MilitaryUnit(Player p, Vertex v) {
        player = p;
        location = v;
        v.military = this;
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

    public int getHaveNotMoved() { return haveNotMoved;}

    public void reset() {
        haveNotMoved = health;
        haveMoved = 0;
        haveBonusMoved = 0;
    }
}
