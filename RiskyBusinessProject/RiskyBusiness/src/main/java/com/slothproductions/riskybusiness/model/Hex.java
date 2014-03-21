package com.slothproductions.riskybusiness.model;

import java.util.*;

public class Hex {
    static private int count = 0;
	final public Resource type;
	final public int index;
	final public int roll;
    final private Board board;

	protected List<Vertex> vertices;
	protected List<Edge> edges;
	protected List<Hex> adjacent;
    private boolean locked;

	public Hex(Board b, Resource t, int r) {
        board = b;
		index = ++count;
		type = t;
		roll = r;
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		adjacent = new ArrayList<Hex>();
        board.addHex(this);
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

    /*final protected void setRoll(int r) {
        roll = r;
    }*/

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

