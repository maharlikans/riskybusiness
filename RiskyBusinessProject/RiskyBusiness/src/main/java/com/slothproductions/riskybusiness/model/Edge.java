package com.slothproductions.riskybusiness.model;

import java.util.*;
import com.slothproductions.riskybusiness.lib.exception.AccessLevelException;

public class Edge {
	static private int count = 0;
	final protected List<Hex> hexagons;
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

    public Edge(Board board, Hex h1, Hex h2, Vertex v1, Vertex v2) {
    	index = ++count;
        ArrayList<Hex> tmp = new ArrayList<Hex>();
        ArrayList<Vertex> tmp2 = new ArrayList<Vertex>();
        tmp.add(h1);
        tmp2.add(v1);
        immutable = new ImmutableEdge();
        owner = null;
    	h1.addEdge(this);
    	if (h2 != null) {
            tmp.add(h2);
            h2.addEdge(this);
        }
    	v1.addEdge(this);
    	if (v2 != null) {
            tmp2.add(v2);
            v2.addEdge(this);
        }
        hexagons = Collections.unmodifiableList(tmp);
        vertices = Collections.unmodifiableList(tmp2);
    	board.addEdge(this);
    }

    public List<Hex> getHexagons() {
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
