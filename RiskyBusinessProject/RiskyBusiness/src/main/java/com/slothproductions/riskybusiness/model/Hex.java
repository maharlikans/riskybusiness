package com.slothproductions.riskybusiness.model;

import java.util.*;
import com.slothproductions.riskybusiness.model.Board.Resource;

public class Hex {
	public Resource type;
	public int index;
	public int roll;

	ArrayList<Vertex> vertices;
	ArrayList<Edge> edges;
	ArrayList<Hex> adjacent;

	public Hex(int index, Resource type, int roll) {
		this.index = index;
		this.type = type;
		this.roll = roll;
		this.vertices = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();
		this.adjacent = new ArrayList<Hex>();
	}
}
