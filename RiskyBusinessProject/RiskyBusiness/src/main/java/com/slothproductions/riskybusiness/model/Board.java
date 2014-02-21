package com.slothproductions.riskybusiness.model;

import java.util.*;

public class Board {

    public static enum Resource {
        LUMBER, BRICK, WOOL, GRAIN, ORE, DESERT
    };

    public ArrayList<Hex> hexes;
    public ArrayList<Edge> edges;
    public ArrayList<Vertex> vertices;
    public ArrayList<ArrayList<Hex>> diceRolls;

    public Board (int numPlayers) {
        hexes = new ArrayList<Hex>();
        // This is hard coded for now. It is the number of concentric rings of
        // hexes that make up the board.
        int radius = 3;

        // TODO: Add a fair generation of dice rolls that avoids adjacent 6 & 8.



        ArrayList<Resource> resources = generateResources();

        // Initializing values for the innermost ring with a single hex. This
        // is done manually as there is no pattern for this one.
        Hex center = new Hex(0, resources.get(0), -1);
        hexes.add(center);
        int index = 1;

        for (int counter = 1; counter < radius; counter++) {
            int innerIndex = (counter == 1) ? 0 : (counter - 2) * (counter - 1) * 3 + 1;
            int minIndex = index;
            int maxIndex = counter * (counter + 1) * 3;
            for (; index <= maxIndex; index++) {
                Hex current = new Hex(index, resources.get(index), -1);
                if ((index - minIndex) % counter == 0) { // On a corner of the ring
                    current.adjacent.add(hexes.get(innerIndex));
                    hexes.get(innerIndex).adjacent.add(current);
                } else {
                    current.adjacent.add(hexes.get(innerIndex));
                    hexes.get(innerIndex).adjacent.add(current);
                    innerIndex = (index != maxIndex) ? innerIndex + 1 : (counter - 2) * (counter - 1) * 3 + 1;
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

        int[] rolls = generateRolls();
        for (int counter = 0; counter <= 18; counter++)
            hexes.get(counter).roll = rolls[counter];
    }

    /**
     * Generates a random list of resources, and returns it. The first resource is always desert.
     * @return Resource[] a list of type Resource
     */
    ArrayList<Resource> generateResources() {
        ArrayList<Resource> resources = new ArrayList<Resource>(19);

        resources.add(Resource.DESERT);

        for (int i = 0; i < 3; i++) {
            resources.add(Resource.BRICK);
            resources.add(Resource.ORE);
        }
        for (int i = 0; i < 4; i ++) {
            resources.add(Resource.GRAIN);
            resources.add(Resource.LUMBER);
            resources.add(Resource.WOOL);
        }

        Collections.shuffle(resources);

        return resources;
    }

    int[] generateRolls() {
        int[] rollsArray = {
                8, 9, 10, 2, 5, 3, 9, 10, 12, 11, 8, 4, 11, 3, 6, 4, 6, 5
        };

        ArrayList<Integer> rolls = new ArrayList<Integer>();
        for (int i : rollsArray)
            rolls.add(i);
        Collections.shuffle(rolls);

        int[] ret = new int[19];

        for (int counter = 0; counter <= 18; counter++) {
            if (hexes.get(counter).type == Resource.DESERT) {
                ret[counter] = -1;
            } else {
                while (true) {
                    int current = rolls.remove(0);
                    boolean adjacent = false;
                    if (current == 6 || current == 8)
                        for (Hex h : hexes.get(counter).adjacent) {
                            if (h.roll == 6 || h.roll == 8)
                                adjacent = true;
                        }
                    if (!adjacent) {
                        ret[counter] = current;
                        hexes.get(counter).roll = current;
                        break;
                    } else {
                        rolls.add(current);
                    }
                }
                Collections.shuffle(rolls);
            }
        }
        return ret;
    }
}
