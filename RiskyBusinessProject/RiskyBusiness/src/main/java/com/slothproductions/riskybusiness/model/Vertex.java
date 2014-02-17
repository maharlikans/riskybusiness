package com.slothproductions.riskybusiness.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Vertex {
	final protected List<Hexagon> hexagons;
	final protected List<Edge> edges;

	class SettlementPair {
		public final Settlement type;
		public final Player owner;
        protected SettlementPair(Settlement t, Player p) {
            type = t;
            owner = p;
        }
	}

    final public class ImmutableVertex {
        public SettlementPair getSettlement() {
            return settlement;
        }
    }

	protected SettlementPair settlement;
    final public ImmutableVertex immutable;
	
	public Vertex(Hexagon h1, Hexagon h2, Hexagon h3, Edge e1, Edge e2, Edge e3) {
        hexagons = Collections.unmodifiableList(Arrays.asList(h1, h2));
		edges = Collections.unmodifiableList(Arrays.asList(e1, e2));
		settlement = null;
        immutable = new ImmutableVertex();
	}

    final public SettlementPair getSettlement() {
        return settlement;
    }

    final public void setSettlement(Settlement t, Player p) {
        settlement = new SettlementPair(t, p);
    }
}
