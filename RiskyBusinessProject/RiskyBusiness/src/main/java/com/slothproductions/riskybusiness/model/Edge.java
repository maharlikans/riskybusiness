package com.slothproductions.riskybusiness.model;

import com.slothproductions.riskybusiness.lib.exception.AccessLevelException;

import java.io.InvalidObjectException;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Edge {
	final protected List<Hexagon> hexagons;
	final protected List<Vertex> vertices;
    final public ImmutableEdge immutable;
	protected Player owner;

    public final class ImmutableEdge {
        public Player getOwner() {
            return owner;
        }
        protected ImmutableEdge() {

        }
    }

	public Edge(Hexagon h1, Hexagon h2, Vertex v1, Vertex v2) {
		hexagons = Collections.unmodifiableList(Arrays.asList(h1, h2));
		vertices = Collections.unmodifiableList(Arrays.asList(v1, v2));
        immutable = new ImmutableEdge();
        owner = null;
	}

    public List<Hexagon> getHexagons() {
        return hexagons;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player p) {
        owner = p;
    }
}
