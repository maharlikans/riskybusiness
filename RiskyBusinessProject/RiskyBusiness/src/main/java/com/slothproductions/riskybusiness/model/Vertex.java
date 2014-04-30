package com.slothproductions.riskybusiness.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.RuntimeException;

public class Vertex implements java.io.Serializable {
    private static final long serialVersionUID = 298019793L;
    final protected List<Hex> hexagons;
    protected List<Vertex> adjacent;
    protected List<Edge> edges;
    final protected int index;
    protected Building building;
    protected MilitaryUnit military;
    private boolean locked;

    final public class ImmutableVertex implements java.io.Serializable {
        protected List<Edge.ImmutableEdge> edges;
        protected List<ImmutableVertex> adjacent;
        private static final long serialVersionUID = 758148154L;
        public int getIndex() {
            return index;
        }

        public Building.ImmutableBuilding getSettlement() {
            return building.immutable;
        }

        public MilitaryUnit getMilitary() {
            return military;
        }

        public List<Edge.ImmutableEdge> getEdges() {
            return edges;
        }

        public List<ImmutableVertex> getAdjacent() {
            return adjacent;
        }

        public Building getBuilding() {
            return building;
        }
    }

    final protected ImmutableVertex immutable;

    final static List<Hex> sharedHexes(Vertex v1, Vertex v2) {
        ArrayList<Hex> shared = new ArrayList<Hex>();
        for (Hex h : v1.hexagons) {
            if (v2.hexagons.contains(h)) {
                shared.add(h);
            }
        }
        return Collections.unmodifiableList(shared);
    }

    protected Vertex(int i, Hex h1, Hex h2, Hex h3) {
        locked = false;
        index = i;
        ArrayList<Hex> tmp = new ArrayList<Hex>();
        tmp.add(h1);
        building = new Building(BuildingType.EMPTY, this, null);
        military = null;
        immutable = new ImmutableVertex();
        h1.addVertex(this);
        if (h2 != null) {
            tmp.add(h2);
            h2.addVertex(this);
        }
        if (h3 != null) {
            tmp.add(h3);
            h3.addVertex(this);
        }
        hexagons = Collections.unmodifiableList(tmp);
        edges = new ArrayList<Edge>();
        adjacent = new ArrayList<Vertex>();
    }

    final protected void addEdge(Edge e) {
        if (!locked) {
            edges.add(e);
            // immutable.edges.add(e.immutable);
        } else {
            throw new RuntimeException();
        }
    }

    final protected void addAdjacent(Vertex v) {
        if (!locked) {
            if (!adjacent.contains(v)) {
                adjacent.add(v);
                // immutable.adjacent.add(v.immutable);
            }
        } else {
            throw new RuntimeException();
        }
    }

    final protected void lock() {
        locked = true;
        edges = Collections.unmodifiableList(edges);
        adjacent = Collections.unmodifiableList(adjacent);
    }

    final protected boolean isVertexOf(Hex h) {
        return hexagons.contains(h);
    }

    final protected boolean isAdjacent(Vertex v) {
        if (v.index == index)
            return false;
        int s = hexagons.size();
        if (s > v.hexagons.size())
            return v.isAdjacent(this);
        if (s == 1) {
            if (v.hexagons.size() == 1)
                return hexagons.get(0) == v.hexagons.get(0);
            else if (v.hexagons.size() == 3)
                return false;
            else {
                ArrayList<Vertex> oneHex = new ArrayList<Vertex>();
                ArrayList<Vertex> twoHex = new ArrayList<Vertex>();
                for (Vertex vertex : hexagons.get(0).vertices)
                    if (vertex.hexagons.size() == 1 && vertex != this)
                        oneHex.add(vertex);
                    else if (vertex.hexagons.size() == 2)
                        twoHex.add(vertex);
                Vertex upper, lower;
                if (twoHex.get(0).index > twoHex.get(1).index) {
                    upper = twoHex.get(0);
                    lower = twoHex.get(1);
                } else {
                    upper = twoHex.get(1);
                    lower = twoHex.get(0);
                }
                if (oneHex.size() == 0)
                    return twoHex.contains(v);
                else
                    if (oneHex.get(0).index > index)
                        return v == lower;
                    else
                        return v == upper;
            }
        } else {
            for (int i = 0, c = 0; i < s; i++) {
                if (v.hexagons.contains(hexagons.get(i)) && ++c == 2) {
                    return true;
                }
            }
            return false;
        }
    }

    // Used to check if settlement can be placed at a given vertex
    final protected boolean isValid() {
        boolean valid = true;
        for (Hex h : hexagons)
            for (Vertex v : h.vertices)
                if (isAdjacent(v) && v.getBuilding() == null)
                    valid = false;
        return valid;
    }

    final protected Building getBuilding() {
        return building;
    }

    final protected void setBuilding(Building b) {
        building = b;
    }

    final protected void setMilitary(MilitaryUnit mu) {
        military = mu;
    }
}