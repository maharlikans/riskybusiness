package com.slothproductions.riskybusiness.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.RuntimeException;

<<<<<<< HEAD
public class Vertex implements java.io.Serializable {
    private static final long serialVersionUID = 298019793L;
=======
/* TODO: Settlement should be its own class, having value and Buildingtype. Rename Settlement to Building */

public class Vertex {
	static private int count = 0;
>>>>>>> Road-Placement
	final protected List<Hex> hexagons;
	protected List<Edge> edges;
	final protected int index;
	protected Player owner;
	protected Building building;
	protected MilitaryUnit military;
	private boolean locked;

    final public class ImmutableVertex implements java.io.Serializable {
        private static final long serialVersionUID = 758148154L;
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

    final protected boolean isVertexOf(Hex h) {
        return hexagons.contains(h);
    }

    final protected boolean isAdjacent(Vertex v) {
        int s = hexagons.size();
        if (s >= 2 && v.hexagons.size() >= 2) {
            for (int i=0, c=0; i<s;i++) {
                if (v.hexagons.contains(hexagons.get(i)) && ++c==2) {
                    return true;
                }
            }
        }
        return false;
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

