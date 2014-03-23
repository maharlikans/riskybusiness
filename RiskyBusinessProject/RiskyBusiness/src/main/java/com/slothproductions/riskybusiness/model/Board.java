package com.slothproductions.riskybusiness.model;

import android.util.Log;

import java.security.SecureRandom;
import java.util.*;

public class Board {
    protected class PlayerAccounting {
        protected class ImmutablePlayerAccounting {
            public int resource(Resource r) {
                return resources.get(r);
            }

            public ArrayList<Edge.ImmutableEdge> getEdges() {
                return immutableEdges;
            }

            public ArrayList<Vertex.ImmutableVertex> getVertices() {
                return immutableVertices;
            }

            public ArrayList<MilitaryUnit.ImmutableMilitaryUnit> getMilitaryUnit() {
                return immutableMilitaryUnits;
            }
        }

        protected PlayerAccounting() {
            resources = new HashMap<Resource, Integer>();
            immutableEdges = new ArrayList<Edge.ImmutableEdge>();
            immutableVertices = new ArrayList<Vertex.ImmutableVertex>();
            immutableMilitaryUnits = new ArrayList<MilitaryUnit.ImmutableMilitaryUnit>();
            edges = new ArrayList<Edge>();
            vertices = new ArrayList<Vertex>();
            militaryUnits = new ArrayList<MilitaryUnit>();
        }

        private Map<Resource, Integer> resources;
        private ArrayList<Edge.ImmutableEdge> immutableEdges;
        private ArrayList<Vertex.ImmutableVertex> immutableVertices;
        private ArrayList<MilitaryUnit.ImmutableMilitaryUnit> immutableMilitaryUnits;
        private ArrayList<Edge> edges;
        private ArrayList<Vertex> vertices;
        private ArrayList<MilitaryUnit> militaryUnits;
    }	

    private final String TAG = "BOARDDATA";
    private boolean locked = false;

    final private Random prng;

    //hexes was changed to public because i need to access it somehow
    public List<Hex> hexes;
    protected List<Vertex> vertices;
    protected List<MilitaryUnit> militaryUnits;
    protected List<Edge> edges;
    protected List<Player> players;
    protected List<ArrayList<Hex>> diceRolls;

    private Map<Player, PlayerAccounting> playerAccounting;

    protected void addHex(Hex h) {
        if (!locked) {
            hexes.add(h);
        } else {
            throw new RuntimeException();
        }
    }

    protected void addVertex(Vertex v) {
    	if (!locked) {
    		vertices.add(v);
    	} else {
			throw new RuntimeException(); 
    	}
    }	
    
    protected void addEdge(Edge e) {
    	if (!locked) {
    		edges.add(e);
    	} else {
			throw new RuntimeException(); 
    	}
    }
    
	final protected void lock() {
		locked = true;
		hexes = Collections.unmodifiableList(hexes);
		vertices = Collections.unmodifiableList(vertices);
		edges = Collections.unmodifiableList(edges);
	}

    private int ringUpperIndex(int ring) {
        return ring * (ring + 1) * 3;
    }

    public Board (int numPlayers) {
        prng = new SecureRandom();

        hexes = new ArrayList<Hex>();
        // This is hard coded for now. It is the number of concentric rings of
        // hexes that make up the board.
        int radius = 3;

        ArrayList<Resource> resources = generateResources(radius);

        // Initializing values for the innermost ring with a single hex. This
        // is done manually as there is no pattern for this one.
        Hex center = new Hex(0, resources.get(0));
        hexes.add(center);

        for (int index = 1, ring = 1; ring < radius; ring++) {
            int innerIndex = (ring == 1) ? 0 : (ring - 2) * (ring - 1) * 3 + 1;
            int minIndex = index;
            int maxIndex = ringUpperIndex(ring);
            for (; index <= maxIndex; index++) {
                Hex current = new Hex(index, resources.get(index));
                if ((index - minIndex) % ring == 0) { // On a corner of the ring
                    current.adjacent.add(hexes.get(innerIndex));
                    hexes.get(innerIndex).adjacent.add(current);
                } else {
                    current.adjacent.add(hexes.get(innerIndex));
                    hexes.get(innerIndex).adjacent.add(current);
                    innerIndex = (index != maxIndex) ? innerIndex + 1 : (ring - 2) * (ring - 1) * 3 + 1;
                    current.adjacent.add(hexes.get(innerIndex));
                    hexes.get(innerIndex).adjacent.add(current);
                } if (index == maxIndex) {
                    current.adjacent.add(hexes.get(minIndex));
                    hexes.get(minIndex).adjacent.add(current);
                    current.adjacent.add(hexes.get(maxIndex - 1));
                    hexes.get(maxIndex - 1).adjacent.add(current);
                } else if (index != minIndex) {
                    current.adjacent.add(hexes.get(index - 1));
                    hexes.get(index - 1).adjacent.add(current);
                }
                hexes.add(current);
            }
        }

        Log.d(TAG, "Generating Rolls");
        ArrayList<Integer> rolls = generateRolls();
        Log.d(TAG, "Finished Generating Rolls");
        for (int counter = 0; counter <= 18; counter++)
            hexes.get(counter).setRoll(rolls.get(counter));

        Log.d(TAG, "Generating Vertices");
        for (int i1 = 0; i1 <= 18; i1++) {
            for (int i2 = 0; i2 < i1; i2++) {
                for (int i3 = 0; i3 < i2; i3++) {
                    Hex h1 = hexes.get(i1);
                    Hex h2 = hexes.get(i2);
                    Hex h3 = hexes.get(i3);
                    if (h1.isAdjacent(i2) && h2.isAdjacent(i3) && h3.isAdjacent(i1)) {
                        vertices.add(new Vertex(h1, h2, h3));
                    }
                }
            }
        }
        for (int i1 = 0; i1 <= 18; i1++) {
            for (int i2 = 0; i2 < i1; i2++) {
                Hex h1 = hexes.get(i1);
                Hex h2 = hexes.get(i2);
                if (h1.isAdjacent(i2)) {
                    int counter = 0;
                    for (Vertex v : vertices) {
                        if (v.isVertexOf(i1) && v.isVertexOf(i2))
                            counter++;
                    }
                    for (int adding = 0; adding < 2 - counter; adding++)
                        vertices.add(new Vertex(h1, h2, null));
                }
            }
        }
        for (int i1 = 0; i1 <= 18; i1++) {
            Hex h1 = hexes.get(i1);
            int counter = 0;
            for (Vertex v : vertices) {
                if (v.isVertexOf(i1))
                    counter++;
            }
            for (int adding = 0; adding < 6 - counter; adding++)
                vertices.add(new Vertex(h1, null, null));
        }
        Log.d(TAG, "Finished Generating Vertices");

        Log.d(TAG, "Generating Edges");
        for (int i1 = 0; i1 <= vertices.size(); i1++) {
            for (int i2 = 0; i2 < i1; i2++) {
                Vertex v1 = vertices.get(i1);
                Vertex v2 = vertices.get(i2);
                if (v1.isAdjacent(v2)) {
                    ArrayList<Hex> shared = new ArrayList<Hex>();
                    for (Hex h : v1.hexagons) {
                        if (v2.hexagons.contains(h))
                            shared.add(h);
                    }
                    edges.add(new Edge(shared.get(0), shared.get(1), v1, v2));
                }
            }
        }
        Log.d(TAG, "Finished Generating Edges");

    }

    private void shuffleArray(Object[] array) {
        int r;
        Object tmp;
        for (int i=0; i<array.length; i++) {
            r = prng.nextInt()%array.length;
            if (r != i) {
                tmp = array[r];
                array[r] = array[i];
                array[i] = tmp;
            }
        }
    }

    /**
     * Generates a random list of resources, and returns it. The first resource is always gold.
     * @return Resource[] a list of type Resource
     */
    private Resource[] generateResources(int radius) {
        int elements = ringUpperIndex(radius) + 1;
        Resource[] resources = new Resource[elements];

        for(int pos=0; pos<elements; ) {
            resources[pos++] = Resource.GOLD;

            for (int i = 0; pos<elements && i < 3; i++) {
                resources[pos++] = Resource.BRICK;
                if (pos<elements) resources[pos++] = Resource.ORE;
            }
            for (int i = 0; pos<elements && i < 4; i ++) {
                resources[pos++] = Resource.WHEAT;
                if (pos<elements) {
                    resources[pos++] = Resource.SHEEP;
                    if (pos<elements) {
                        resources[pos++] = Resource.WOOD;
                    }
                }
            }
        }

        shuffleArray(resources);

        return resources;
    }

    ArrayList<Integer> generateRolls() {
        int[] rollsArray = {
                2, 3, 3, 4, 4, 5, 5, 9, 9, 10, 10, 11, 11, 12
        };

        ArrayList<Integer> invalid = new ArrayList<Integer>();
        ArrayList<Integer> sixOrEight = new ArrayList<Integer>();
        for (int counter = 0; counter < 4; counter++) {
            int index = (int) (18 * Math.random());
            while (invalid.contains(index)) {
                index = (int) (18 * Math.random());
            }
            invalid.add(index);
            for (Hex h : hexes.get(index).adjacent)
                invalid.add(h.index);
        }

        int goldIndex = 0;
        for (Hex h : hexes)
            if (h.type == Resource.GOLD)
                goldIndex = h.index;


        ArrayList<Integer> ret = new ArrayList<Integer>();
        int arrayCounter = 0;
        for (int counter = 0; counter < 18; counter++) {
            int listIndex = sixOrEight.lastIndexOf(counter);
            if (listIndex != -1) {
                if (listIndex < sixOrEight.size() / 2)
                    ret.add(6);
                else
                    ret.add(8);
            } else if (goldIndex == counter) {
                ret.add(7);
            } else {
                ret.add(rollsArray[arrayCounter]);
                arrayCounter++;
            }
        }
        return ret;
    }
}
