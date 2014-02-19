package com.slothproductions.riskybusiness.model;

import java.util.*;

public class Vertex {
	public int index;
	public int owner;
	public int type;
	public int value;

	public ArrayList<Hex> hexes;
	public ArrayList<Edge> edges;

	public Vertex(int index) {
		this.index = index;
		this.owner = -1;
		this.type = 0;
		this.value = 0;
		this.hexes = new ArrayList<Hex>();
		this.edges = new ArrayList<Edge>();
	}
}
