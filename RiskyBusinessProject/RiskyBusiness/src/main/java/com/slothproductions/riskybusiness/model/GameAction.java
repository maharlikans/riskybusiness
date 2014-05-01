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

/*
 * Sample call to Player::effect:
 * (PUBLIC_TRADE, new HashMap<String, Object>{{
 * 		put("sell_resource_type", Resource.GOLD);
 * 		put("sell_amount", new Integer(5));
 * 		put("buy_resource_type", Resource.WOOL);
 *  	put("buy_amount", new Integer(7));
 * }})
 */

public enum GameAction {
    BUILD_ROAD(new EnumMap<Resource, Integer>(Resource.class){{
        put(Resource.BRICK, 1);
        put(Resource.LUMBER, 1);
    }}, new HashMap<String, ArgumentType>(){{
        put("edge", ArgumentType.EDGE);
    }}),
    BUILD_SETTLEMENT(new EnumMap<Resource, Integer>(Resource.class){{
        put(Resource.BRICK, 1);
        put(Resource.LUMBER, 1);
        put(Resource.WOOL, 1);
        put(Resource.GRAIN, 1);
    }}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    REPAIR_SETTLEMENT(new EnumMap<Resource, Integer>(Resource.class){{
        put(Resource.BRICK, 1);
        put(Resource.LUMBER, 1);
    }}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    BUILD_CITY(new EnumMap<Resource, Integer>(Resource.class){{
        put(Resource.ORE, 2);
        put(Resource.GRAIN, 2);
    }}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    REPAIR_CITY(new EnumMap<Resource, Integer>(Resource.class){{
        put(Resource.BRICK, 1);
        put(Resource.ORE, 1);
    }}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    BUILD_MILITARY_UNIT(new EnumMap<Resource, Integer>(Resource.class){{
        put(Resource.ORE, 1);
        put(Resource.WOOL, 1);
        put(Resource.GRAIN, 1);
    }}, new HashMap<String, ArgumentType>(){{
        put("vertex", ArgumentType.VERTEX);
    }}),
    MOVE_MILITARY_UNIT(new EnumMap<Resource, Integer>(Resource.class), new HashMap<String, ArgumentType>(){{
        put("vertex_from", ArgumentType.VERTEX);
        put("vertex_to", ArgumentType.VERTEX);
    }}),
    ATTACK(new EnumMap<Resource, Integer>(Resource.class), new HashMap<String, ArgumentType>(){{
        put("vertex_from", ArgumentType.VERTEX);
        put("vertex_to", ArgumentType.VERTEX);
        put("amount", ArgumentType.MILITARY_QUANTITY);
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
    FULFILL_PRIVATE_TRADE(new EnumMap<Resource, Integer>(Resource.class), new HashMap<String, ArgumentType>(){{
        put("trade", ArgumentType.TRADE);
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
        MILITARY_QUANTITY,
        EDGE,
        VERTEX,
        TRADE;
    }

    /* Used for returning the result of an action */
    static public class ActionWrapper {
        public enum AssetType {
            BUILDING,
            MILITARY_UNIT,
            ROAD,
            TRADE;
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
