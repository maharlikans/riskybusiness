package com.slothproductions.riskybusiness.model;

import java.util.*;

public class Hex implements java.io.Serializable {
    private static final long serialVersionUID = 286476240L;
	final public Resource type;
	final public int index;
	private int roll;

	protected List<Vertex> vertices;
	protected List<Edge> edges;
	protected List<Hex> adjacent;
    private boolean locked;

	public Hex(int i, Resource t) {
		index = i;
		type = t;
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		adjacent = new ArrayList<Hex>();
	}

    final protected void addVertex(Vertex v) {
        if (!locked) {
            vertices.add(v);
        } else {
            throw new RuntimeException();
        }
    }

    final protected void addEdge(Edge e) {
        if (!locked) {
            edges.add(e);
        } else {
            throw new RuntimeException();
        }
    }

    final protected void addAdjacent(Hex h) {
        if (!locked) {
            adjacent.add(h);
        } else {
            throw new RuntimeException();
        }
    }

    final protected void setRoll(int r) {
        if (!locked) {
            roll = r;
        } else {
            throw new RuntimeException();
        }
    }

    final protected int getRoll() {
        return roll;
    }

    final protected boolean isAdjacent(int index) {
        boolean ret = false;
        for (Hex h : adjacent)
            if (h.index == index)
                ret = true;
        return ret;
    }

    final protected void lock() {
        locked = true;
        vertices = Collections.unmodifiableList(vertices);
        edges = Collections.unmodifiableList(edges);
        adjacent = Collections.unmodifiableList(adjacent);
    }
}

