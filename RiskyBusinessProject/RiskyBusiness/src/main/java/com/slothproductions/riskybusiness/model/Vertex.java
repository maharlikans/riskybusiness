package com.slothproductions.riskybusiness.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.RuntimeException;

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
        
        public Building.ImmutableBuilding getSettlement() {
            return building.immutable;
        }

        public MilitaryUnit getMilitary() {
            return military;
        }        
    }

    final protected ImmutableVertex immutable;
	
    protected Vertex(Board board, Hex h1, Hex h2, Hex h3) {
    	locked = false;
		index = ++count;
        ArrayList<Hex> tmp = new ArrayList<Hex>();
        tmp.add(h1);
        owner = null;
        building = null;
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
        board.addVertex(this);
        hexagons = Collections.unmodifiableList(tmp);
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

    final protected Building getBuilding() {
        return building;
    }

    final protected void setOwner(Player p) {
        owner = p;
    }
    
    final protected void setBuilding(Building b) {
        building = b;
    }
    
    final protected void setMilitary(MilitaryUnit mu) {
        military = mu;
    }
}