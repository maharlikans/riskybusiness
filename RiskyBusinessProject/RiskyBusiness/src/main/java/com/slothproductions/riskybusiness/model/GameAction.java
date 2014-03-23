package com.slothproductions.riskybusiness.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by riv on 16.03.14.
 */

public enum GameAction {
    BUILD_ROAD(new ResourceDescription[] {new ResourceDescription(1, Resource.BRICK), new ResourceDescription(1, Resource.WOOD)}, new HashMap<String, ArgumentType>(){{
        put("edge", ArgumentType.EDGE);
    }}),
    BUILD_SETTLEMENT(new ResourceDescription[] {new ResourceDescription(1, Resource.BRICK), new ResourceDescription(1, Resource.WOOD), new ResourceDescription(1, Resource.SHEEP), new ResourceDescription(1, Resource.WHEAT)}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    UPGRADE_SETTLEMENT_TO_CITY(new ResourceDescription[] {new ResourceDescription(2, Resource.ORE), new ResourceDescription(2, Resource.WHEAT)}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    ESTABLISH_MILITARY_UNIT(new ResourceDescription[] {new ResourceDescription(1, Resource.ORE), new ResourceDescription(1, Resource.SHEEP), new ResourceDescription(1, Resource.WHEAT)}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    MOVE_MILITARY_UNIT(new ResourceDescription[] {}, new HashMap<String, ArgumentType>(){{
        put("vertex_from", ArgumentType.VERTEX);
        put("edge_across", ArgumentType.EDGE);
    }}),
    ATTACK(new ResourceDescription[] {}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    PUBLIC_TRADE(new ResourceDescription[] {}, new HashMap<String, ArgumentType>(){{
        put("sell_resource_type", ArgumentType.RESOURCE_TYPE);
        put("sell_amount", ArgumentType.RESOURCE_QUANTITY);
        put("buy_resource_type", ArgumentType.RESOURCE_TYPE);
        put("buy_amount", ArgumentType.RESOURCE_TYPE);
    }}),
    PRIVATE_TRADE(new ResourceDescription[] {}, new HashMap<String, ArgumentType>(){{
        put("partner", ArgumentType.PLAYER);
        put("sell_resource_type", ArgumentType.RESOURCE_TYPE);
        put("sell_amount", ArgumentType.RESOURCE_QUANTITY);
        put("buy_resource_type", ArgumentType.RESOURCE_TYPE);
        put("buy_amount", ArgumentType.RESOURCE_TYPE);
    }}),
    BANK_TRADE(new ResourceDescription[] {}, new HashMap<String, ArgumentType>(){{
        put("sell_resource_type", ArgumentType.RESOURCE_TYPE);
        put("sell_amount", ArgumentType.RESOURCE_QUANTITY);
        put("buy_resource_type", ArgumentType.RESOURCE_TYPE);
    }})
    ;

    final List<ResourceDescription> resourcesNeeded;
    final Map<String, ArgumentType> arguments;

    /* Used when calling an action */
    public enum ArgumentType {
        PLAYER,
        RESOURCE_TYPE,
        RESOURCE_QUANTITY,
        EDGE,
        VERTEX;
    }

    /* Used for returning the result of an action */
    static public class ActionWrapper {
        public enum AssetType {
            BUILDING,
            MILITARY_UNIT,
            ROAD,
            RESOURCE;
        }

        final public AssetType type;
        final public Object el;

        public ActionWrapper(AssetType t, Object o) {
            type = t;
            el = o;
        }
    }

    GameAction(ResourceDescription[] needed, Map<String, ArgumentType> a) {
        resourcesNeeded = Collections.unmodifiableList(Arrays.asList(needed));
        arguments = Collections.unmodifiableMap(a);
    }
}