package com.slothproductions.riskybusiness.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hexagon {
	final protected List<Edge> edges;
	final protected List<Vertex> vertices;
	final protected List<Hexagon> adjacent_hexagons;

	public Hexagon(Edge[] e, Vertex[] v, Hexagon[] a) {
		edges = Collections.unmodifiableList(Arrays.asList(e));
		vertices = Collections.unmodifiableList(Arrays.asList(v));
		adjacent_hexagons = Collections.unmodifiableList(Arrays.asList(a));
	}
}
