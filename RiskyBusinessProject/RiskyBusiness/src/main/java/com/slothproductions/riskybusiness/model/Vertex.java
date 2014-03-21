package com.slothproductions.riskybusiness.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.lang.RuntimeException;

import com.slothproductions.riskybusiness.model.Building.Type;

/* TODO: Settlement should be its own class, having value and Buildingtype. Rename Settlement to Building */

public class Vertex {
	static private int count = 0;
	final protected List<Hex> hexagons;
	protected List<Edge> edges;
	final protected int index;
	protected Player owner;
	protected Building building;
	protected MilitaryUnit military;
	private boolean locked;

    final public class ImmutableVertex {
        public int getIndex() {
            return index;
        }

        public Player getOwner() {
            return owner;
        }
        
        public Building getBuilding() {
            return building;
        }

        public MilitaryUnit getMilitary() {
            return military;
        }        
    }

    final protected ImmutableVertex immutable;
	
    protected Vertex(Hex h1, Hex h2, Hex h3) {
    	locked = false;
		index = ++count;
        hexagons = Collections.unmodifiableList(Arrays.asList(h1, h2, h3));
        owner = null;
        building = null;
        military = null;
        immutable = new ImmutableVertex();
        h1.addVertex(this);
        if (h2 != null) h2.addVertex(this);
        if (h3 != null) h3.addVertex(this);
        edges = new ArrayList<Edge>();
	}
	
	final protected void addEdge(Edge e) {
		if (!locked) {
			edges.add(e);
		} else {
			throw new RuntimeException();
		}
	}
	
	final protected void lock() {
		locked = true;
		edges = Collections.unmodifiableList(edges);
	}

    final protected void setOwner(Player p) {
        owner = p;
    }
    
    final protected void setBuilding(Building b) {
        building = b;
    }
    
    final protected void setMilitary(MilitaryUnit u) {
        military = u;
    }

    final protected boolean isVertexOf(int index) {
        boolean ret = false;
        for (Hex h : hexagons)
            if (h.index == index)
                ret = true;
        return ret;
    }

    final protected boolean isAdjacent(Vertex v) {
        int counter = 0;
        for (Hex h : hexagons)
            if (v.hexagons.contains(h))
                counter++;
        return counter == 2;
    }
}
