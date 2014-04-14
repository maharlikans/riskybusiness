package com.slothproductions.riskybusiness.model;

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
    private ArrayList<Edge> edges;
    private ArrayList<Vertex> vertices;
    private ArrayList<MilitaryUnit> militaryUnits;
    final Board board;
    final String name;

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
        vertices = new ArrayList<Vertex>();
        militaryUnits = new ArrayList<MilitaryUnit>();
    }

    public GameAction.ActionWrapper effect(GameAction action, Map<String, Object> arguments) {
        /* TODO: Encapsulate any necessary exceptions */
        return board.effect(this, action, arguments);
    }
}
