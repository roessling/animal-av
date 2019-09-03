package generators.graph.helpersGraph;

public class Neighbour {
	
	public Node neighbour;
	public int distance;
	
	public Neighbour(Node n, int d) {
		this.neighbour = n;
		this.distance = d;
	}
	
}
