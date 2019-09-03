package generators.graph.helpersGraph;

public class Edge {
	
	public Node first;
	public Node second;
	public int weight;
	
	public Edge(Node first, Node second) {
		this.first = first;
		this.second = second;
		this.weight = 1;
	}

}
