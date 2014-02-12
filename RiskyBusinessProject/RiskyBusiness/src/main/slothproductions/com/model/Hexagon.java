public class Hexagon {
	final public List<Edge> edges;
	final public List<Vertex> vertices;
	final public List<Hexagon> adjacent_hexagons;

	public Hexagon(Edges[] e, Vertices[] v, Hexagon[] a) {
		edges = Collections.unmodifiableList(e.asList());
		vertices = Collections.unmodifiableList(v.asList());
		adjacent_hexagons = Collections.unmodifiableList(a.asList());
	}
}
