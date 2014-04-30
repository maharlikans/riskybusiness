package com.slothproductions.riskybusiness.model;

/**
 * Created by riv on 20.03.14.
 */
public class Building implements java.io.Serializable {
    private static final long serialVersionUID = -349421070L;
    public class ImmutableBuilding implements java.io.Serializable {
        private static final long serialVersionUID = -211440880L;
        final BuildingType getType() {
            return type;
        }

        public int getHealth() {
            return health;
        }
    }

    final protected BuildingType type;
    private int health;
    final public ImmutableBuilding immutable;
    protected int numSoldiersBuilt;
    protected Player owner;
    Vertex vertex;


    public Building(BuildingType t, Vertex v, Player p) {
        type = t;
        vertex = v;
        owner = p;
        if (t == BuildingType.CITY)
            health = 10;
        else if (t == BuildingType.SETTLEMENT)
            health = 5;
        else
            health = 0;
        immutable = new ImmutableBuilding();
        numSoldiersBuilt = 0;
    }

    public int getHealth() {
        return health;
    }

    public int getNumSoldiersBuilt() { return numSoldiersBuilt;}

    public Player getPlayer() {
        return owner;
    }

    public void setHealth(int h) {
        health = h;
    }

    final BuildingType getType() {
        return type;
    }

    public void reset() { numSoldiersBuilt = 0;}
}
