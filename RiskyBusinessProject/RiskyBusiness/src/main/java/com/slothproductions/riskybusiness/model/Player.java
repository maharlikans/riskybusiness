package com.slothproductions.riskybusiness.model;

import java.util.ArrayList;
import java.util.Map;

public class Player {
    public Map<Resource, Integer> resources;
    public ArrayList<Edge> edges;
    public ArrayList<Vertex> vertices;
    public ArrayList<MilitaryUnit> military_units;

    public void effect(GameActions action) {
        /* TODO: Code to effect action */
    }
}
