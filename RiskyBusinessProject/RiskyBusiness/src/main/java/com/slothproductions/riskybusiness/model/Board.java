package com.slothproductions.riskybusiness.model;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

// TODO can you provide a getPlayers method so I can have access to the list of players? Thanks!
public class Board implements java.io.Serializable {
    private static final long serialVersionUID = -915315565L;

    private final String TAG = "BOARDDATA";
    private final String ATTACKTAG = "Attacking Info";
    private final String MOVETAG = "Move Info";

    final private Random prng;

    //hexes was changed to public because i need to access it somehow
    protected List<Hex> hexes;
    protected List<Vertex> vertices;
    protected List<MilitaryUnit> militaryUnits;
    protected List<Edge> edges;
    protected List<Player> players;
    protected List<ArrayList<Hex>> diceRolls;

    private int ringUpperIndex(int ring) {
        return ring * (ring + 1) * 3;
    }

    public Board (String[] playerNames) {
        Log.d(TAG, "Generating board");
        prng = new SecureRandom();
        vertices = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        militaryUnits = new ArrayList<MilitaryUnit>();
        players = new ArrayList<Player>();
        hexes = new ArrayList<Hex>();
        diceRolls = new ArrayList<ArrayList<Hex>>();

        // This is hard coded for now. It is the number of concentric rings of
        // hexes that make up the board.
        int radius = 3;

        Resource[] resources = generateResources(radius);
        int goldIndex = 0;
        for (int counter = 0; counter < resources.length; counter++)
            if (resources[counter] == Resource.GOLD)
                goldIndex = counter;

        // Initializing values for the innermost ring with a single hex. This
        // is done manually as there is no pattern for this one.
        Hex center = new Hex(0, resources[0]);
        hexes.add(center);

        for (int index = 1, ring = 1; ring < radius; ring++) {
            int innerIndex = (ring == 1) ? 0 : (ring - 2) * (ring - 1) * 3 + 1;
            int minIndex = index;
            int maxIndex = ringUpperIndex(ring);
            for (; index <= maxIndex; index++) {
                Hex current = new Hex(index, resources[index]);
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

        int elements = hexes.size();

        Log.d(TAG, "Generating Rolls");
        int[] rolls = generateRolls(goldIndex);
        Log.d(TAG, "Finished Generating Rolls");
        for (int counter = 0; counter < elements; counter++)
            hexes.get(counter).setRoll(rolls[counter]);

        Log.d(TAG, "Generating Vertices");
        for (int i1 = 0; i1 <= 18; i1++) {
            for (int i2 = 0; i2 < i1; i2++) {
                for (int i3 = 0; i3 < i2; i3++) {
                    Hex h1 = hexes.get(i1);
                    Hex h2 = hexes.get(i2);
                    Hex h3 = hexes.get(i3);
                    if (h1.isAdjacent(i2) && h2.isAdjacent(i3) && h3.isAdjacent(i1)) {
                        vertices.add(new Vertex(vertices.size(), h1, h2, h3));
                    }
                }
            }
        }

        for (int i1 = 0; i1 < elements; i1++) {
            for (int i2 = 0; i2 < i1; i2++) {
                Hex h1 = hexes.get(i1);
                Hex h2 = hexes.get(i2);
                if (h1.isAdjacent(i2)) {
                    int counter = 0;
                    for (Vertex v : vertices) {
                        if (v.isVertexOf(h1) && v.isVertexOf(h2)) {
                            counter++;
                        }
                    }
                    for (int adding = 0; adding < 2 - counter; adding++)
                        vertices.add(new Vertex(vertices.size(), h1, h2, null));
                }
            }
        }
        for (int i1 = 0; i1 < elements; i1++) {
            Hex h1 = hexes.get(i1);
            int counter = 0;
            for (Vertex v : vertices) {
                if (v.isVertexOf(h1)) {
                    counter++;
                }
            }
            for (int adding = 0; adding < 6 - counter; adding++) {
                vertices.add(new Vertex(vertices.size(), h1, null, null));
            }
        }
        for (Vertex v1 : vertices)
            for (Vertex v2 : vertices)
                if (v1.index < v2.index && v1.isAdjacent(v2)) {
                    v1.addAdjacent(v2);
                    v2.addAdjacent(v1);
                }
        Log.d(TAG, "Finished Generating Vertices");
        for (Vertex v : vertices) {
            Log.d(TAG, "Vertex " + v.index);
            for (Hex h : v.hexagons)
                Log.d(TAG, "Adjacent to hex " + h.index);
            for (Vertex vertex : v.adjacent)
                Log.d(TAG, "Adjacent to vertex " + vertex.index);
        }

        Log.d(TAG, "Generating Edges");
        for (int i1 = 0; i1 < vertices.size(); i1++) {
            for (int i2 = 0; i2 < i1; i2++) {
                Vertex v1 = vertices.get(i1);
                Vertex v2 = vertices.get(i2);
                if (v1.isAdjacent(v2)) {
                    ArrayList<Hex> shared = new ArrayList<Hex>();
                    for (Hex h : v1.hexagons) {
                        if (v2.hexagons.contains(h))
                            shared.add(h);
                    }
                    if (shared.size() == 2)
                        edges.add(new Edge(edges.size(), shared.get(0), shared.get(1), v1, v2));
                    else
                        edges.add(new Edge(edges.size(), shared.get(0), null, v1, v2));
                }
            }
        }

        Log.d(TAG, "Finished Generating Edges");
        for (Edge e : edges) {
            Log.d(TAG, "Edge " + e.index);
            for (Hex h : e.hexagons)
                if (h != null)
                    Log.d(TAG, "Adjacent to hex " + h.index);
            for (Vertex v : e.vertices)
                if (v != null)
                    Log.d(TAG, "Adjacent to vertex " + v.index);
        }

        Log.d(TAG, "Locking Board Elements");

        hexes = Collections.unmodifiableList(hexes);
        vertices = Collections.unmodifiableList(vertices);
        edges = Collections.unmodifiableList(edges);

        for(Hex h: hexes) {
            h.lock();
        }

        for(Vertex v: vertices) {
            v.lock();
        }

        Log.d(TAG, "Finished Locking Board Elements");

        Log.d(TAG, "Generating Players");
        for(String name: playerNames) {
            Log.d(TAG, "This player is: " + name);
            players.add(new Player(this, name));
        }
        Log.d(TAG, "Finished Generating Players");

        Log.d(TAG, "Constructing diceRolls");
        for (int counter = 0; counter <= 12; counter++)
            diceRolls.add(new ArrayList<Hex>());
        for (Hex h : hexes)
            diceRolls.get(h.roll).add(h);
        Log.d(TAG, "Finished Generating diceRolls");

    }

    private void shuffleArray(Object[] array) {
        int r, c=array.length;
        Object tmp;
        for (int i=0; i<c; i++) {
            r = prng.nextInt(c);
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
        int elements = ringUpperIndex(radius - 1) + 1;
        Resource[] resources = new Resource[elements];

        for(int pos=0; pos<elements; ) {
            resources[pos++] = Resource.GOLD;

            for (int i = 0; pos<elements && i < 3; i++) {
                resources[pos++] = Resource.BRICK;
                if (pos<elements) resources[pos++] = Resource.ORE;
            }
            for (int i = 0; pos<elements && i < 4; i ++) {
                resources[pos++] = Resource.GRAIN;
                if (pos<elements) {
                    resources[pos++] = Resource.WOOL;
                    if (pos<elements) {
                        resources[pos++] = Resource.LUMBER;
                    }
                }
            }
        }

        shuffleArray(resources);

        return resources;
    }

    private int[] generateRolls(int goldIndex) {
        int[] rollsArray = {
                2, 3, 3, 4, 4, 5, 5, 9, 9, 10, 10, 11, 11, 12
        };

        int elements = hexes.size();
        Log.d(TAG, "Generating " + elements + " die rolls.");

        HashSet<Integer> invalid = new HashSet<Integer>();
        invalid.add(goldIndex);
        ArrayList<Integer> sixOrEight = new ArrayList<Integer>();
        for (int counter = 0; counter < 4; counter++) {
            int index = prng.nextInt(elements);
            while (invalid.contains(index)) {
                index = prng.nextInt(elements);
            }
            sixOrEight.add(index);
            for (Hex h : hexes.get(index).adjacent)
                invalid.add(h.index);
            invalid.add(index);
        }

        Log.d(TAG, "Locations of six or 8:");
        for (int i : sixOrEight)
            Log.d(TAG, "Hex " + i + " is a 6 or 8");
        Log.d(TAG, "Gold is located at hex " + goldIndex);


        int ret[] = new int[elements];
        int arrayCounter = 0;
        for (int counter = 0; counter < elements; counter++) {
            int listIndex = sixOrEight.lastIndexOf(counter);
            if (listIndex != -1) {
                ret[counter] = (listIndex < sixOrEight.size() / 2)?6:8;
            } else if (goldIndex == counter) {
                ret[counter] = 7;
            } else {
                ret[counter] = rollsArray[arrayCounter];
                arrayCounter++;
            }
        }

        return ret;
    }

    public void beginTurn(int roll) {
        for (Vertex v : vertices) {
            if (v.getBuilding().getType() != BuildingType.EMPTY) {
                v.getBuilding().reset();
                Log.d(TAG, "Vertex " + v.index + " has a " + v.getBuilding().getType());
                if (v.getBuilding().getPlayer() != null)
                    Log.d(TAG, "owned by player " + v.getBuilding().getPlayer().getName());
            }
            else if (v.getBuilding().getPlayer() != null)
                Log.d(TAG, "Vertex " + v.index + " is Empty owned by player " + v.getBuilding().getPlayer().getName());
            if (v.getMilitary() != null) {
                Log.d(TAG, "Reset military unit at vertex " + v.index);
                v.getMilitary().reset();
            }
        }
        getResources(roll);
    }

    public void getResources(int roll) {
        Log.d(TAG, "Rolled " + roll);
        ArrayList<Hex> rolled = diceRolls.get(roll);
        HashMap<Player, ArrayList<Resource>> gained = new HashMap<Player, ArrayList<Resource>>();
        for (Player p : players)
            gained.put(p, new ArrayList<Resource>());
        for (Hex h : rolled) {
            for (Vertex v : h.vertices) {
                if (v.building.owner == null)
                    continue;
                if (v.building.type == BuildingType.CITY) {
                    Log.d(TAG, "Resource with cities");
                    gained.get(v.building.owner).add(h.type);
                    gained.get(v.building.owner).add(h.type);
                } else if (v.building.type == BuildingType.SETTLEMENT) {
                    gained.get(v.building.owner).add(h.type);
                }
            }
        }
        for (Player p : players)
            p.addResources(gained.get(p));
    }

    public Vertex getVertex(ArrayList<Hex> hexes, int i) {
        Log.d(TAG, "Getting vertex.");
        for (Hex hex : hexes)
            Log.d(TAG, "Given hex " + hex.index);
        Log.d(TAG, "Given i = " + i);
        if (hexes.size() != 1) {
            List<Vertex> vertices = hexes.get(0).vertices;
            for (Vertex vertex : vertices) {
                boolean isGood = vertex.hexagons.size() == hexes.size();
                for (Hex hex : hexes)
                    if (!vertex.hexagons.contains(hex))
                        isGood = false;
                if (isGood) {
                    Log.d(TAG, "Got vertex " + vertex.index);
                    return vertex;
                }
            }
        } else {
            List<Vertex> vertices = hexes.get(0).vertices;
            ArrayList<Vertex> good = new ArrayList<Vertex>();
            for (Vertex vertex : vertices)
                if (vertex.hexagons.size() == 1)
                    good.add(vertex);
            Log.d(TAG, "Got vertex " + good.get(i).index);
            return good.get(i);
        }
        throw new InvalidParameterException("Bad hex list for getVertex.");
    }

    public Edge getEdge(ArrayList<Hex> hexes, int i) {
        Log.d(TAG, "Getting edge.");
        for (Hex hex : hexes)
            Log.d(TAG, "Given hex " + hex.index);
        Log.d(TAG, "Given i = " + i);
        if (hexes.size() > 1) {
            List<Edge> edges = hexes.get(0).edges;
            for (Edge edge : edges)
                if (edge.hexagons.contains(hexes.get(1))) {
                    Log.d(TAG, "Got edge " + edge.index);
                    return edge;
                }
        } else {
            List<Edge> edges = hexes.get(0).edges;
            ArrayList<Edge> good = new ArrayList<Edge>();
            Edge ret;
            for (Edge edge : edges)
                if (edge.hexagons.size() == 1)
                    good.add(edge);
            if (hexes.get(0).index == 9) {
                Log.d(TAG, "Extra case 1 - hex 9.");
                ret = good.get(2 - i);
            } else if (hexes.get(0).index == 7) {
                Log.d(TAG, "Extra case 2 - hex 7.");
                ret = good.get(i);
            } else if (hexes.get(0).index == 8) {
                Log.d(TAG, "Extra case 3 - hex 8.");
                ret = good.get(1 - i);
            } else if (good.size() == 2) {
                Log.d(TAG, "Normal case 1 - 2 edges.");
                ret = good.get(i);
            } else {
                Log.d(TAG, "Normal case 2 - 3 edges.");
                if (i == 0)
                    ret = good.get(0);
                else if (i == 1)
                    ret = good.get(2);
                else
                    ret = good.get(1);
            }
            Log.d(TAG, "Got edge " + ret.index);
            return ret;
        }
        throw new InvalidParameterException("Bad hex list for getEdge.");
    }

    protected GameAction.ActionWrapper effect(Player player, GameAction action, Map<String, Object> arguments) {
        if (player == null || action == null || arguments == null) throw new InvalidParameterException();
        Iterator<Map.Entry<String, GameAction.ArgumentType>> it = action.arguments.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, GameAction.ArgumentType> requiredPair = it.next();
            Object provided = arguments.get(requiredPair.getKey());
            if (provided == null) {
                throw new InvalidParameterException();
            } else {
                boolean invalid = false;
                switch(requiredPair.getValue()) {
                    case PLAYER:
                        if (!(provided instanceof Player) || provided == player) invalid = true; /* Change to ImmutablePlayer later, when available */
                        break;
                    case RESOURCE_TYPE:
                        if (!(provided instanceof Resource)) invalid = true;
                        break;
                    case RESOURCE_QUANTITY:
                        if (!(provided instanceof Integer) || (Integer)provided < 1) invalid = true;
                        break;
                    case MILITARY_QUANTITY:
                        if (!(provided instanceof Integer) || (Integer)provided < 0) invalid = true;
                        break;
                    case EDGE:
                        if (!(provided instanceof Edge)) invalid = true;
                        break;
                    case VERTEX:
                        if (!(provided instanceof Vertex)) invalid = true;
                        break;
                    case TRADE:
                        if (!(provided instanceof EnumMap)) invalid = true;
                        break;
                }
                if (invalid) {
                    throw new InvalidParameterException();
                }
            }
        }

        if (!player.hasResources(action.resourcesNeeded))
            throw new InvalidParameterException();
        player.takeResources(action.resourcesNeeded);

        switch(action) {
            case BUILD_SETTLEMENT: {
                Vertex target = vertices.get(((Vertex) arguments.get("vertex")).getIndex());
                target.building = new Building(BuildingType.SETTLEMENT, target, player);
                player.points++;
                return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.BUILDING, target.building);
            } case REPAIR_SETTLEMENT: {
                Vertex target = vertices.get(((Vertex) arguments.get("vertex")).getIndex());
                target.building.setHealth(Math.max(5, target.building.getHealth() + 2));
                return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.BUILDING, target);
            } case BUILD_CITY: {
                Vertex target = vertices.get(((Vertex) arguments.get("vertex")).getIndex());
                target.building = new Building(BuildingType.CITY, target, player);
                player.points++;
                return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.BUILDING, target.building);
            } case REPAIR_CITY: {
                Vertex target = vertices.get(((Vertex) arguments.get("vertex")).getIndex());
                target.building.setHealth(Math.max(10, target.building.getHealth() + 3));
                return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.BUILDING, target);
            } case BUILD_ROAD: {
                Edge e = edges.get(((Edge) arguments.get("edge")).getIndex());
                e.owner = player;
                e.road = true;
                return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.ROAD, e.immutable);
            } case BUILD_MILITARY_UNIT: {
                Vertex target = vertices.get(((Vertex) arguments.get("vertex")).getIndex());
                if (target.military != null) {
                    target.military.haveNotMoved++;
                    target.military.health++;
                } else {
                    target.military = new MilitaryUnit(player, target);
                }
                target.building.numSoldiersBuilt++;
                return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.BUILDING, target);
            } case BANK_TRADE: {
                EnumMap<Resource, Integer> bought = (EnumMap<Resource, Integer>) arguments.get("bought");
                EnumMap<Resource, Integer> sold = (EnumMap<Resource, Integer>) arguments.get("bought");
                player.takeResources(sold);
                player.addResources(bought);
                return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.TRADE, sold);
            } case PRIVATE_TRADE: {
                if (player.hasResources((Resource) arguments.get("sell_resource_type"), (Integer) arguments.get("sell_amount"))) {
                    Trade t = new Trade(player, (Player) arguments.get("partner"), (Integer) arguments.get("sell_amount"), (Resource) arguments.get("sell_resource_type"), (Integer) arguments.get("buy_amount"), (Resource) arguments.get("buy_resource_type"));
                    return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.TRADE, t);
                }
                break;
            } case FULFILL_PRIVATE_TRADE: {
                Player partner = (Player) arguments.get("player");
                EnumMap<Resource, Integer> bought = (EnumMap<Resource, Integer>) arguments.get("bought");
                EnumMap<Resource, Integer> sold = (EnumMap<Resource, Integer>) arguments.get("bought");
                player.takeResources(sold);
                player.addResources(bought);
                partner.takeResources(bought);
                partner.addResources(sold);
                return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.TRADE, sold);
            } case MOVE_MILITARY_UNIT: {
                Log.d(MOVETAG, "Moving soldier.");
                Vertex to = vertices.get(((Vertex) arguments.get("vertex_to")).getIndex());
                Log.d(MOVETAG, "Target: Vertex " + to.index);
                Vertex from = vertices.get(((Vertex) arguments.get("vertex_from")).getIndex());
                Log.d(MOVETAG, "From: Vertex " + from.index);
                if (!to.isAdjacent(from) || (to.getBuilding().getPlayer() != null && to.getBuilding().getPlayer() != player)) {
                    if (!to.isAdjacent((from)))
                        Log.d(MOVETAG, "Unadjacent target.");
                    Log.d(MOVETAG, "Improper target 1.");
                    return null;
                }
                int numMoving = (Integer) arguments.get("amount");
                for (int i = 0; i < numMoving; i++) {
                    Log.d(MOVETAG, "Number of Soldiers Moving: " + numMoving);
                    boolean bonus = from.military.haveBonusMoved > 0;
                    if (to.military == null) {
                        to.military = new MilitaryUnit(player, to);
                        to.military.haveNotMoved--;
                    } else {
                        if (to.military.getPlayer() != player) {
                            Log.d(TAG, "Improper target 2.");
                            return null;
                        }
                        to.military.health++;
                    }
                    if (bonus) {
                        from.military.haveBonusMoved--;
                        to.military.haveMoved++;
                    } else {
                        from.military.haveNotMoved--;
                        boolean onRoad = false;
                        for (Edge edge : from.edges)
                            if (edge.vertices.contains(to) && edge.owner != null)
                                onRoad = true;
                        Log.d(TAG, "OnRoad: " + onRoad);
                        if (onRoad)
                            to.military.haveBonusMoved++;
                        else
                            to.military.haveMoved++;
                    }
                    from.military.health--;
                    if (from.military.health == 0)
                        from.military = null;
                    Log.d(MOVETAG, "After moving:");
                    Log.d(MOVETAG, "....At target vertex " + to.index + ":");
                    if (to.military != null) {
                        Log.d(MOVETAG, "........" + to.military.getHealth() + " soldiers owned by player " + to.military.getPlayer().getName());
                        Log.d(MOVETAG, "........HaveNotMoved: " + to.military.haveNotMoved);
                        Log.d(MOVETAG, "........HaveMoved: " + to.military.haveMoved);
                        Log.d(MOVETAG, "........HaveBonusMoved: " + to.military.haveBonusMoved);
                    }
                    if (to.building != null) {
                        Log.d(MOVETAG, "........" + to.building.getType() + " owned by player ");
                        if (to.getBuilding().getPlayer() != null)
                            Log.d(MOVETAG, "............Player " + to.getBuilding().getPlayer().getName());
                    }
                    Log.d(TAG, "....At from vertex " + from.index + ":");
                    if (from.military != null) {
                        Log.d(MOVETAG, "...." + from.military.getHealth() + " soldiers owned by player " + from.military.getPlayer().getName());
                        Log.d(MOVETAG, "........HaveNotMoved: " + from.military.haveNotMoved);
                        Log.d(MOVETAG, "........HaveMoved: " + from.military.haveMoved);
                        Log.d(MOVETAG, "........HaveBonusMoved: " + from.military.haveBonusMoved);
                    }
                    if (from.building != null) {
                        Log.d(MOVETAG, "........" + from.building.getType() + " owned by player ");
                        if (from.getBuilding().getPlayer() != null)
                            Log.d(MOVETAG, "............Player " + from.getBuilding().getPlayer().getName());
                    }
                }
                return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.MILITARY_UNIT, to);
            } case ATTACK: {
                Log.d(ATTACKTAG, "Attacking.");
                Vertex to = vertices.get(((Vertex) arguments.get("vertex_to")).getIndex());
                Log.d(ATTACKTAG, "Target: Vertex " + to.index);
                Vertex from = vertices.get(((Vertex) arguments.get("vertex_from")).getIndex());
                Log.d(ATTACKTAG, "From: Vertex " + from.index);
                Integer amount = (Integer) arguments.get("amount");
                if (!to.isAdjacent(from)
                        || ((to.getBuilding().getPlayer() == player || to.getBuilding().getPlayer() == null)
                            && (to.military == null || to.military.getPlayer() == player)))
                    return null;

                if (to.getBuilding().getType() == BuildingType.EMPTY) {
                    int opposing = to.military.getHealth();
                    if (opposing > amount) {
                        to.military.health -= amount;
                    } else if (amount > opposing) {
                        to.military = new MilitaryUnit(player, to);
                        to.military.health = amount - opposing;
                        to.military.haveMoved = to.military.health;
                        to.military.haveBonusMoved = 0;
                        to.military.haveNotMoved = 0;
                    } else {
                        to.military = null;
                    }
                    from.military.health -= amount;
                    from.military.haveNotMoved -= amount;
                } else {
                    int maxGarrison = 1;
                    if (to.getBuilding().getType() == BuildingType.CITY)
                        maxGarrison = 2;
                    int garrison = 0;
                    if (to.getMilitary() != null)
                        garrison = Math.min(to.getMilitary().getHealth() / 3, maxGarrison);
                    int damageToBuilding = Math.max(0, amount - garrison);
                    int damageToUnit = Math.min(amount, garrison);
                    if (damageToUnit != 0)
                        to.getMilitary().health -= damageToUnit;
                    if (to.getBuilding().getHealth() - damageToBuilding > 0) {
                        to.getBuilding().setHealth(to.getBuilding().getHealth() - damageToBuilding);
                        from.military.haveMoved += amount - garrison;
                        from.military.haveNotMoved -= amount;
                        from.military.health -= garrison;
                    } else {
                        if (to.getBuilding().getType() == BuildingType.CITY) {
                            to.getBuilding().type = BuildingType.SETTLEMENT;
                            to.getBuilding().setHealth(3);
                            if (to.getMilitary() == null) {
                                to.getBuilding().owner.points -= 2;
                                player.points++;
                                to.getBuilding().owner = player;
                                to.setMilitary(new MilitaryUnit(player, to));
                                to.getMilitary().health = amount;
                                to.getMilitary().haveNotMoved = 0;
                                to.getMilitary().haveBonusMoved = 0;
                                to.getMilitary().haveMoved = amount;
                                from.military.haveMoved -= amount;
                                from.military.health -= amount;
                            } else {
                                to.getBuilding().owner.points--;
                                to.getMilitary().health -= garrison;
                                from.military.haveMoved += amount - garrison;
                                from.military.haveNotMoved -= amount;
                                from.military.health -= garrison;
                            }
                        } else {
                            to.getBuilding().getPlayer().points -= 1;
                            to.building = new Building(BuildingType.EMPTY, to, null);
                            if (to.getMilitary() == null) {
                                to.setMilitary(new MilitaryUnit(player, to));
                                to.getMilitary().health = amount;
                                to.getMilitary().haveNotMoved = 0;
                                to.getMilitary().haveBonusMoved = 0;
                                to.getMilitary().haveMoved = amount;
                                from.military.haveMoved -= amount;
                                from.military.health -= amount;
                            } else {
                                to.getMilitary().health -= garrison;
                                from.military.haveMoved += amount - garrison;
                                from.military.haveNotMoved -= amount;
                                from.military.health -= garrison;
                            }
                        }
                    }
                }
                if (from.military.health == 0)
                    from.military = null;
                Log.d(ATTACKTAG, "After attacking:");
                Log.d(ATTACKTAG, "....At target vertex " + to.index + ":");
                if (to.military != null) {
                    Log.d(ATTACKTAG, "........" + to.military.getHealth() + " soldiers owned by player " + to.military.getPlayer().getName());
                    Log.d(ATTACKTAG, "........HaveNotMoved: " + to.military.haveNotMoved);
                    Log.d(ATTACKTAG, "........HaveMoved: " + to.military.haveMoved);
                    Log.d(ATTACKTAG, "........HaveBonusMoved: " + to.military.haveBonusMoved);
                }
                if (to.building != null) {
                    Log.d(ATTACKTAG, "........" + to.building.getType() + " owned by player ");
                    if (to.getBuilding().getPlayer() != null)
                        Log.d(ATTACKTAG, "............Player " + to.getBuilding().getPlayer().getName());
                    Log.d(ATTACKTAG, "............Health: " + to.getBuilding().getHealth());
                }
                Log.d(ATTACKTAG, "....At from vertex " + from.index + ":");
                if (from.military != null) {
                    Log.d(ATTACKTAG, "...." + from.military.getHealth() + " soldiers owned by player " + from.military.getPlayer().getName());
                    Log.d(ATTACKTAG, "........HaveNotMoved: " + from.military.haveNotMoved);
                    Log.d(ATTACKTAG, "........HaveMoved: " + from.military.haveMoved);
                    Log.d(ATTACKTAG, "........HaveBonusMoved: " + from.military.haveBonusMoved);
                }
                if (from.building != null) {
                    Log.d(ATTACKTAG, "........" + from.building.getType() + " owned by player ");
                    if (from.getBuilding().getPlayer() != null)
                        Log.d(ATTACKTAG, "............Player " + from.getBuilding().getPlayer().getName());
                    Log.d(ATTACKTAG, "............Health: " + from.getBuilding().getHealth());
                }
                return new GameAction.ActionWrapper(GameAction.ActionWrapper.AssetType.MILITARY_UNIT, to);
            }
        }

        return null;
    }

    /* TODO: For now, password is a 16 bytes integer. Later on, implement composing password
    * somehow, e.g., by asking users to enter a token and hashing them together */
    public static void saveGame(Board board, String password) {
        try {
            /* TODO: Decide on file name. Maybe this is somehow dynamic */
            FileOutputStream fos = new FileOutputStream("game.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            SecureWrapper<Board> wrapper = new SecureWrapper(password, board);
            oos.writeObject(wrapper);
            oos.close();
        } catch (FileNotFoundException e) {
            /* TODO: Do something */
        } catch (IOException e) {
            /* TODO: Do something */
        } catch (InvalidKeyException e) {
            /* TODO: Do something */
        } catch (NoSuchPaddingException e) {
            /* TODO: Do something */
        }  catch (NoSuchAlgorithmException e) {
            /* TODO: Do something */
        }  catch (IllegalBlockSizeException e) {
            /* TODO: Do something */
        }
    }

    /* TODO: For now, password is a 16 bytes integer. Later on, implement composing password
    * somehow, e.g., by asking users to enter a token and hashing them together */
    public static Board openGame(String password) {
        try {
            /* TODO: Decide on file name. Maybe this is somehow dynamic */
            FileInputStream fis = new FileInputStream("game.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            @SuppressWarnings("unchecked")
            SecureWrapper<Board> wrapper = (SecureWrapper<Board>) ois.readObject();
            Board b = wrapper.unwrap(password);
            ois.close();

            /* Delete file */
            new File("game.dat").delete();

            return b;
        } catch (FileNotFoundException e) {
            /* TODO: Do something */
        } catch (IOException e) {
            /* TODO: Do something */
        } catch (ClassNotFoundException e) {
            /* TODO: Do something */
        } catch (RuntimeException e) {
            /* TODO: Do something */
        }
        return null;
    }

    // TODO temporary getter methods
    public List<Player> getPlayers() {
        return players;
    }
    public Hex getHex(int index) {
        return hexes.get(index);
    }
    public int getHexesSize() { return hexes.size(); }
}
