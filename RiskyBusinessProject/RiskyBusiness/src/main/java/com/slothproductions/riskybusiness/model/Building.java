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
    final protected Hex hex;
    private int health;
    final public ImmutableBuilding immutable;


    public Building(Hex h, BuildingType t) {
        type = t;
        hex = h;
        health = 5;
        immutable = new ImmutableBuilding();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int h) {
        health = h;
    }

    final BuildingType getType() {
        return type;
    }
}
