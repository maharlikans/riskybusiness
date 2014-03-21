
import java.util.*;
import com.slothproductions.riskybusiness.model.Resource;

public class Hex {
    final protected Resource type;
    final protected int index;
    protected int roll;

	List<Vertex> vertices;
	List<Edge> edges;
	List<Hex> adjacent;

    private boolean locked;

	public Hex(int index, Resource type) {
		this.index = index;
		this.type = type;
		this.vertices = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();
		this.adjacent = new ArrayList<Hex>();
        this.locked = false;
	}

    final protected void addVertex(Vertex v) {
        if (!locked) {
            vertices.add(v);
        } else {
            throw new RuntimeException();
        }
    }

    final protected void addEdge(Edge e) {
        if (!locked) {
            edges.add(e);
        } else {
            throw new RuntimeException();
        }
    }

    final protected void setRoll(int r) {
        roll = r;
    }

    final protected void lock() {
        locked = true;
        edges = Collections.unmodifiableList(edges);
        vertices = Collections.unmodifiableList(vertices);
    }

    final protected boolean isAdjacent(int index) {
        boolean ret = false;
        for (Hex h : adjacent)
            if (h.index == index)
                ret = true;
        return ret;
    }
}
