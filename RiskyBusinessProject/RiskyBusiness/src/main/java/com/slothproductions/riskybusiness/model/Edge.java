package com.slothproductions.riskybusiness.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Edge {
	final public List<Hexagon> hexagons;
	final public List<Vertex> vertices;
	protected Player owner;

	public Edge(Hexagon h1, Hexagon h2, Vertex v1, Vertex v2) {
		hexagons = Collections.unmodifiableList(Arrays.asList(h1, h2));
		vertices = Collections.unmodifiableList(Arrays.asList(v1, v2));
	}
}
