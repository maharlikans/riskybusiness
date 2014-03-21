package com.slothproductions.riskybusiness.model;

/**
 * Created by riv on 16.03.14.
 */

public enum GameAction {
    ESTABLISH_SETTLEMENT(new ResourceDescription[]{new ResourceDescription(5, Resource.ORE)});

    final ResourceDescription[] resources_needed;

    static public class ActionWrapper {
        public enum AssetType {
            SETTLEMENT,
            MILITARY_UNIT,
            ROAD,
            RESOURCE;
        }

        AssetType type;
        Object o;
    }

    GameAction(ResourceDescription[] needed) {
        this.resources_needed = needed;
    }
}
