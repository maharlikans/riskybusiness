package com.slothproductions.riskybusiness.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.lang.RuntimeException;

/* TODO: Settlement should be its own class, having value and Buildingtype. Rename Settlement to Building */

public class Vertex {/*
	static private int count = 0;
	final protected List<Hex> hexagons;
	protected List<Edge> edges;
	final protected int index;
	protected Player owner;
	protected Settlement building;
	protected MilitaryUnit military;
	private boolean locked;

    final public class ImmutableVertex {
        public int getOwner() {
            return index;
        }
    	
        public Player getOwner() {
            return owner;
        }
        
        public Settlement getSettlement() {
            return type;
        }

        public Settlement getMilitary() {
            return military;
        }        
    }

	protected SettlementPair settlement;
    final protected ImmutableVertex immutable;
	
    protected Vertex(Board board, Hex h1, Hex h2, Hex h3) {
    	locked = false;
		index = ++count;
        hexagons = Collections.unmodifiableList(Arrays.asList(h1, h2));
        owner = type = military = null;
        immutable = new ImmutableVertex();
        h1.addVertex(this);
        if (h2 != null) h2.addVertex(this);
        if (h3 != null) h3.addVertex(this);
        board.addVertex(this);
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

    final protected SettlementPair getSettlement() {
        return settlement;
    }

    final protected void setOwner(Player p) {
        owner = p;
    }
    
    final protected void setBuilding(Settlement b) {
        building = b;
    }
    
    final protected void setMilitary(MilitaryUnit t) {
        military = u;
    }
*/
}
