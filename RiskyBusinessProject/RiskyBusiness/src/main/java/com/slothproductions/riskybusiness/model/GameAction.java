package com.slothproductions.riskybusiness.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by riv on 16.03.14.
 */

public enum GameAction {
    BUILD_ROAD(new EnumMap<Resource, Integer>(Resource.class){{
        put(Resource.BRICK, 1);
        put(Resource.WOOD, 1);
    }}, new HashMap<String, ArgumentType>(){{
        put("edge", ArgumentType.EDGE);
    }}),
    BUILD_SETTLEMENT(new EnumMap<Resource, Integer>(Resource.class){{
        put(Resource.BRICK, 1);
        put(Resource.WOOD, 1);
        put(Resource.SHEEP, 1);
        put(Resource.WHEAT, 1);
    }}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    UPGRADE_SETTLEMENT_TO_CITY(new EnumMap<Resource, Integer>(Resource.class){{
        put(Resource.ORE, 2);
        put(Resource.WHEAT, 2);
    }}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    ESTABLISH_MILITARY_UNIT(new EnumMap<Resource, Integer>(Resource.class){{
        put(Resource.ORE, 1);
        put(Resource.SHEEP, 1);
        put(Resource.WHEAT, 1);
    }}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    MOVE_MILITARY_UNIT(new EnumMap<Resource, Integer>(Resource.class), new HashMap<String, ArgumentType>(){{
        put("vertex_from", ArgumentType.VERTEX);
        put("edge_across", ArgumentType.EDGE);
    }}),
    ATTACK(new EnumMap<Resource, Integer>(Resource.class), new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    PUBLIC_TRADE(new EnumMap<Resource, Integer>(Resource.class), new HashMap<String, ArgumentType>(){{
        put("sell_resource_type", ArgumentType.RESOURCE_TYPE);
        put("sell_amount", ArgumentType.RESOURCE_QUANTITY);
        put("buy_resource_type", ArgumentType.RESOURCE_TYPE);
        put("buy_amount", ArgumentType.RESOURCE_TYPE);
    }}),
    PRIVATE_TRADE(new EnumMap<Resource, Integer>(Resource.class), new HashMap<String, ArgumentType>(){{
        put("partner", ArgumentType.PLAYER);
        put("sell_resource_type", ArgumentType.RESOURCE_TYPE);
        put("sell_amount", ArgumentType.RESOURCE_QUANTITY);
        put("buy_resource_type", ArgumentType.RESOURCE_TYPE);
        put("buy_amount", ArgumentType.RESOURCE_TYPE);
    }}),
    BANK_TRADE(new EnumMap<Resource, Integer>(Resource.class), new HashMap<String, ArgumentType>(){{
        put("sell_resource_type", ArgumentType.RESOURCE_TYPE);
        put("sell_amount", ArgumentType.RESOURCE_QUANTITY);
        put("buy_resource_type", ArgumentType.RESOURCE_TYPE);
    }})
    ;

    final Map<Resource, Integer> resourcesNeeded;
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

    GameAction(Map<Resource, Integer> needed, Map<String, ArgumentType> a) {
        resourcesNeeded = Collections.unmodifiableMap(needed);
        arguments = Collections.unmodifiableMap(a);
    }
}
