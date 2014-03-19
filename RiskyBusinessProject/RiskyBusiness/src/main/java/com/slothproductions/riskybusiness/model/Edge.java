package com.slothproductions.riskybusiness.model;

import java.util.*;
import com.slothproductions.riskybusiness.lib.exception.AccessLevelException;

public class Edge {
	static private int count = 0;
	final protected List<Hexagon> hexagons;
	final protected List<Vertex> vertices;
    final public ImmutableEdge immutable;
	final public int index;
    protected Player owner;
	public boolean road;

    public final class ImmutableEdge {
        public Player getOwner() {
            return owner;
        }
        
        public int getIndex() {
            return index;
        }

        protected ImmutableEdge() {
        }
    }

    public Edge(Board board, Hexagon h1, Hexagon h2, Vertex v1, Vertex v2) {
    	index = ++count;
    	hexagons = Collections.unmodifiableList(Arrays.asList(h1, h2));
    	vertices = Collections.unmodifiableList(Arrays.asList(v1, v2));
        immutable = new ImmutableEdge();
        owner = null;
    	h1.addEdge(this);
    	if (h2 != null) h2.addEdge(this);
    	v1.addEdge(this);
    	if (v2 != null)  v2.addEdge(this);
    	board.addEdge(this);
    }

    public List<Hexagon> getHexagons() {
        return hexagons;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player p) {
        owner = p;
    }
}