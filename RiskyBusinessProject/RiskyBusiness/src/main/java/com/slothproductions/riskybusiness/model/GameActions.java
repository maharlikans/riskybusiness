package com.slothproductions.riskybusiness.model;

/**
 * Created by riv on 16.03.14.
 */

public enum GameActions {
    ESTABLISH_SETTLEMENT(new ResourceDescription[]{new ResourceDescription(5, Resource.WOOL)});

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

    GameActions(ResourceDescription[] needed) {
        this.resources_needed = needed;
    }
}
