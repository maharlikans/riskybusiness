package com.slothproductions.riskybusiness.model;

import android.graphics.Color;
import android.util.Log;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

final public class Player implements java.io.Serializable {
    private static final long serialVersionUID = -677559079L;

    private static final String TAG = "PLAYER";

    protected class ImmutablePlayerAccounting implements java.io.Serializable {
    }

    public ImmutablePlayer immutable;
    protected int points;
    private Map<Resource, Integer> resources;
    private ArrayList<Edge.ImmutableEdge> immutableEdges;
    private ArrayList<Vertex.ImmutableVertex> immutableVertices;
    private ArrayList<MilitaryUnit.ImmutableMilitaryUnit> immutableMilitaryUnits;
    /* TODO: These should go away */
    private ArrayList<Edge> edges;
    private ArrayList<MilitaryUnit> militaryUnits;
    private ArrayList<Building> buildings;
    /* End of list */
    public Map<Resource, Integer> trades;
    final Board board;
    final String name;
    private int color;

    /* TODO: Implement to player id */
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
        // DONE BY KYLE: INITIALIZE THE RESOURCES CLASS
        for (Resource r : Resource.values()) {
            resources.put(r, 3);
        }
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

    public boolean effect(GameAction action, Map<String, Object> arguments) {
        if (action == GameAction.PRIVATE_TRADE) throw new InvalidParameterException();
        GameAction.ActionWrapper wrapper = board.effect(this, action, arguments);
        if (wrapper == null) {
            Log.d(TAG, "Null effect gotten.");
            return false;
        } else if (action == GameAction.BUILD_ROAD) {
            if (wrapper.el instanceof Edge.ImmutableEdge) {
                immutableEdges.add((Edge.ImmutableEdge)wrapper.el);
            }
        }
        return true;
    }

    protected void addResources(ArrayList<Resource> gains) {
        // TODO remove this test statement
        for (Resource r: gains) {
            Log.d("TAG", "Before adding, player " + name + " has " + resources.get(r) + " " + r.toString());
        }
        for (Resource r: gains) {
            Log.d("TAG", "Adding " + r.toString());
        }
        for (Resource r : gains) {
            resources.put(r, resources.get(r) + 1);
            Log.d("TAG", "Now player " + name + " has " + resources.get(r) + " " + r.toString());
        }

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
        buildings.add(v.building);
        if (i == 2) {
            ArrayList<Resource> resources = new ArrayList<Resource>();
            for (Hex h : v.hexagons)
                resources.add(h.type);
            addResources(resources);
        }
        points++;
    }

    public void buildInitial(Edge e, int i) {
        e.road = true;
        e.owner = this;
    }

    public ArrayList<GameAction> getActions(Vertex v, boolean filter) {
        Log.d(TAG, "Getting actions for " + name);
        ArrayList<GameAction> actions = new ArrayList<GameAction>();

        if (v.building.type == BuildingType.EMPTY && (v.getMilitary() == null || v.getMilitary().getPlayer() == this)) {
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

        if (v.getBuilding().getPlayer() == this && v.building.type == BuildingType.SETTLEMENT) {
            Log.d(TAG, "Player has settlement, access to city unlocked.");
            actions.add(GameAction.BUILD_CITY);
            if (v.getBuilding().getHealth() < 5)
                actions.add(GameAction.REPAIR_SETTLEMENT);
        }

        if (v.getBuilding().getPlayer() == this && v.building.type == BuildingType.CITY) {
            if (v.getBuilding().getHealth() < 10)
                actions.add(GameAction.REPAIR_CITY);
        }

        if (v.getBuilding().getPlayer() == this) {
            Log.d(TAG, "Number of soldiers built here this turn: " + v.getBuilding().getNumSoldiersBuilt());
            Log.d(TAG, "Building type is " + v.getBuilding().getType());
            if (v.getBuilding().getType() == BuildingType.CITY && v.getBuilding().getNumSoldiersBuilt() < 2)
                actions.add(GameAction.BUILD_MILITARY_UNIT);
            else if (v.getBuilding().getType() == BuildingType.SETTLEMENT && v.getBuilding().getNumSoldiersBuilt() < 1)
                actions.add(GameAction.BUILD_MILITARY_UNIT);
        }

        if (v.military != null && v.military.getPlayer() == this && v.military.haveBonusMoved + v.military.haveNotMoved > 0) {
            boolean canMove = false;
            // for (Vertex.ImmutableVertex vertex : v.immutable.getAdjacent()) {
            for (Vertex vertex : v.adjacent) {
                if ((vertex.getBuilding().getPlayer() == null || vertex.getBuilding().getPlayer() == this)
                        && (vertex.getMilitary() == null || vertex.getMilitary().getPlayer() == this))

                    canMove = true;
            }
            if (canMove)
                actions.add(GameAction.MOVE_MILITARY_UNIT);
        }

        if (v.military != null && v.getMilitary().getPlayer() == this && v.military.haveNotMoved > 0) {
            Log.d(TAG, "Checking for targets for: " + v.getMilitary().getPlayer());
            boolean canAttack = false;
            for (Vertex vertex : v.adjacent) {
                if (vertex.military != null && vertex.military.getPlayer() != this) {
                    canAttack = true;
                    Log.d(TAG, "Found adjacent soldier to attack.");
                } if (vertex.getBuilding().getPlayer() != this && vertex.getBuilding().getPlayer() != null) {
                    canAttack = true;
                    Log.d(TAG, "Found adjacent building to attack.");
                }
            }
            if (canAttack)
                actions.add(GameAction.ATTACK);
        }
        if (filter) {
            for (int counter = 0; counter != actions.size(); counter++) {
                if (!hasResources(actions.get(counter).resourcesNeeded)) {
                    actions.remove(counter);
                    counter--;
                }
            }
        }
        return actions;
    }

    public ArrayList<GameAction> getActions(Edge e, boolean filter) {
        ArrayList<GameAction> actions = new ArrayList<GameAction>();

        if (!e.road) {
            Log.d(TAG, "Road is empty.");
            boolean canBuild = false;
            for (Vertex v : e.getVertices()) {
                Log.d(TAG, "Checking adjacent vertex " + v.getIndex());
                Log.d(TAG, "Building is of type: " + v.getBuilding().getType());
                if (v.getBuilding().getPlayer() != null)
                    Log.d(TAG, "Adjacent vertex is owned by player " + v.getBuilding().getPlayer().getName());
                if (v.getBuilding().getPlayer() == this) {
                    Log.d(TAG, "Adjacent building is owned.");
                    canBuild = true;
                } else if (v.getBuilding().getPlayer() == null) {
                    for (Edge edge : v.edges) {
                        if (edge.getOwner() == this)
                            canBuild = true;
                    }
                }
            }
            if (canBuild)
                actions.add(GameAction.BUILD_ROAD);
        }
        if (filter) {
            for (int counter = 0; counter != actions.size(); counter++) {
                if (!hasResources(actions.get(counter).resourcesNeeded)) {
                    actions.remove(counter);
                    counter--;
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

    public Map<Resource, Integer> getResources() {
        return resources;
    }

    public String getName() {
        return name;
    }

    public int getTotalNumberResources() {
        int i = 0;
        Iterator it = resources.entrySet().iterator();
        while (it.hasNext()) {
            i += (Integer)((Map.Entry)it.next()).getValue();
        }
        return i;
    }

    public int getPoints() {
        return points;
    }

    public void setColor(int c) {
        color = c;
    }

    public int getColor() {
        return color;
    }
}
