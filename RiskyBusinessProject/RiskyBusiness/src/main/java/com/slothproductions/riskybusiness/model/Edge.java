package com.slothproductions.riskybusiness.model;

import java.util.*;

public class Edge implements java.io.Serializable {
    private static final long serialVersionUID = 293823348L;
	final protected List<Hex> hexagons;
	final protected List<Vertex> vertices;
    final public ImmutableEdge immutable;
	final public int index;
    protected Player owner;
	public boolean road;

    public final class ImmutableEdge implements java.io.Serializable {
        private static final long serialVersionUID = -354785748L;
        public Player getOwner() {
            return owner;
        }
        
        public int getIndex() {
            return index;
        }

        protected ImmutableEdge() {
        }
    }

    public Edge(int i, Hex h1, Hex h2, Vertex v1, Vertex v2) {
    	index = i;
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
