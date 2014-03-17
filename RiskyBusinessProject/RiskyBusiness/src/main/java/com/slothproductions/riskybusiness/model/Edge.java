package com.slothproductions.riskybusiness.model;

import java.util.*;

public class Edge {
	public int index;
	public Player owner;
	public boolean road;

	ArrayList<Vertex> vertices;
	ArrayList<Hex> hexes;

	public Edge (int index) {
		this.index = index;
		this.owner = null;
		this.road = false;
		this.vertices = new ArrayList<Vertex>();
		this.hexes = new ArrayList<Hex>();
	}
}
