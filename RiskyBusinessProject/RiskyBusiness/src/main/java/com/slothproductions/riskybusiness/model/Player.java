package com.slothproductions.riskybusiness.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

final public class Player implements java.io.Serializable {
    private static final long serialVersionUID = -677559079L;

    protected class ImmutablePlayerAccounting implements java.io.Serializable {
    }

    public ImmutablePlayer immutable;
    private int points;
    private Map<Resource, Integer> resources;
    private ArrayList<Edge.ImmutableEdge> immutableEdges;
    private ArrayList<Vertex.ImmutableVertex> immutableVertices;
    private ArrayList<MilitaryUnit.ImmutableMilitaryUnit> immutableMilitaryUnits;
    /* TODO: These should go away */
    private ArrayList<Edge> edges;
    private ArrayList<MilitaryUnit> militaryUnits;
    private ArrayList<Building> buildings;
    /* End of list */
    protected Map<Resource, Integer> trades;
    final Board board;
    final String name;

    /* TODO: Implpement to player id */
    public class ImmutablePlayer implements java.io.Serializable {
        private static final long serialVersionUID = -242346795L;
        public int resource(Resource r) {
            return resources.get(r);
        }

        public int getPoints() {
            return points;
        }

        public ArrayList<Edge.ImmutableEdge> getEdges() {
            return immutableEdges;
        }

        public ArrayList<Vertex.ImmutableVertex> getVertices() {
            return immutableVertices;
        }

        public ArrayList<MilitaryUnit.ImmutableMilitaryUnit> getMilitaryUnit() {
            return immutableMilitaryUnits;
        }

        final public String getName() {
            return name;
        }
    }

    public Player(Board b, String n) {
        board = b;
        name = n;
        immutable = new ImmutablePlayer();
        points = 0;
        resources = new EnumMap<Resource, Integer>(Resource.class);
        immutableEdges = new ArrayList<Edge.ImmutableEdge>();
        immutableVertices = new ArrayList<Vertex.ImmutableVertex>();
        immutableMilitaryUnits = new ArrayList<MilitaryUnit.ImmutableMilitaryUnit>();
        edges = new ArrayList<Edge>();
        buildings = new ArrayList<Building>();
        militaryUnits = new ArrayList<MilitaryUnit>();
        trades = new EnumMap<Resource, Integer>(Resource.class);
        for (Resource r : Resource.values())
            if (r == Resource.GOLD)
                trades.put(r, 2);
            else
                trades.put(r, 4);
    }

    /* Used for private trades. TODO: Possibly also for public trades */
    public Trade initiate_trade(Map<String, Object> arguments) {
        GameAction.ActionWrapper wrapper = board.effect(this, GameAction.PRIVATE_TRADE, arguments);
        if (wrapper != null && wrapper.el instanceof Trade) {
            return (Trade)wrapper.el;
        }
        return null;
    }

    public Boolean effect(GameAction action, Map<String, Object> arguments) {
        if (action == GameAction.PRIVATE_TRADE) throw new InvalidParameterException();
        GameAction.ActionWrapper wrapper = board.effect(this, action, arguments);
        if (wrapper == null) {
            return false;
        } else if (action == GameAction.BUILD_ROAD) {
            if (wrapper.el instanceof Edge.ImmutableEdge) {
                immutableEdges.add((Edge.ImmutableEdge)wrapper.el);
            }
        } /* TODO: etc for other actions */
        return true;
    }

    protected void addResources(ArrayList<Resource> gains) {
        for (Resource r : gains)
            resources.put(r, resources.get(r) + 1);
    }

    protected void addResources(Map<Resource, Integer> gained) {
        for (Resource r : gained.keySet()) {
            resources.put(r, resources.get(r) + gained.get(r));
        }
    }

    protected boolean hasResources(Map<Resource, Integer> needed) {
        for (Resource r : needed.keySet()) {
            if (needed.get(r) > resources.get(r))
                return false;
        }
        return true;
    }

    protected boolean hasResources(Resource r, Integer a) {
        return (a <= resources.get(r));
    }

    protected void takeResources(Map<Resource, Integer> needed) {
        for (Resource r : needed.keySet()) {
            resources.put(r, resources.get(r) - needed.get(r));
        }
    }

    public boolean canBuildInitial(Vertex v, int i) {
        boolean canBuild = false;
        if (v.building.type == BuildingType.EMPTY) {
            canBuild = true;
            for (Vertex vertex : v.adjacent)
                if (vertex.building.type != BuildingType.EMPTY)
                    canBuild = false;
        }
        return canBuild;
    }

    public boolean canBuildInitial(Edge e, int i) {
        boolean canBuild = false;
        if (!e.road) {
            for (Vertex v : e.vertices)
                if (v.building == buildings.get(i - 1))
                    canBuild = true;
        }
        return canBuild;
    }

    public void buildInitial(Vertex v, int i) {
        v.building = new Building(BuildingType.SETTLEMENT, v, this);
        if (i == 2) {
            ArrayList<Resource> resources = new ArrayList<Resource>();
            for (Hex h : v.hexagons)
                resources.add(h.type);
            addResources(resources);
        }
    }

    public void buildInitial(Edge e, int i) {
        e.road = true;
        e.owner = this;
    }

    public ArrayList<GameAction> getActions(Vertex v, boolean filter) {
        ArrayList<GameAction> actions = new ArrayList<GameAction>();

        if (v.building.type == BuildingType.EMPTY) {
            boolean canBuild = true;
            for (Vertex vertex : v.adjacent)
                if (vertex.building.type != BuildingType.EMPTY)
                    canBuild = false;
            if (canBuild) {
                boolean hasRoad = false;
                for (Edge e : v.edges)
                    if (e.getOwner() == this)
                        hasRoad = true;
                if (hasRoad)
                    actions.add(GameAction.BUILD_SETTLEMENT);
            }
        }

        if (v.building.type == BuildingType.SETTLEMENT) {
            actions.add(GameAction.BUILD_CITY);
        }

        if (v.building.owner == this) {
            if (v.building.type == BuildingType.CITY && v.building.numSoldiersBuilt < 2)
                actions.add(GameAction.BUILD_MILITARY_UNIT);
            else if (v.building.type == BuildingType.SETTLEMENT && v.building.numSoldiersBuilt < 1)
                actions.add(GameAction.BUILD_MILITARY_UNIT);
        }

//        if (v.military != null && v.military.haveBonusMoved + v.military.haveNotMoved > 0)
//            actions.add(GameAction.MOVE_MILITARY_UNIT);

        if (v.military != null && v.military.haveNotMoved > 0) {
            boolean canAttack = false;
        }
        if (filter) {
            for (GameAction action : actions) {
                if (!hasResources(action.resourcesNeeded)) {
                    actions.remove(action);
                }
            }
        }
        return actions;
    }

    public ArrayList<GameAction> getActions(Edge e, boolean filter) {
        ArrayList<GameAction> actions = new ArrayList<GameAction>();

        if (!e.road) {
            boolean canBuild = false;
            for (Vertex v : e.getVertices()) {
                if (v.building.owner == this)
                    canBuild = true;
            }
            if (canBuild)
                actions.add(GameAction.BUILD_ROAD);
        }
        if (filter) {
            for (GameAction action : actions) {
                if (!hasResources(action.resourcesNeeded)) {
                    actions.remove(action);
                }
            }
        }
        return actions;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public ArrayList<MilitaryUnit> getMilitaryUnit() {
        return militaryUnits;
    }
}
