package generators.tree.id3.tree;

public class Edge {

	private Node from;

	private Node to;

	private String label;



	public Edge(Node from, Node to, String label) {
		this.from = from;
		this.to = to;
		this.label = label;
	}


	public Node getFrom() {
		return from;
	}


	public Node getTo() {
		return to;
	}


	public String getLabel() {
		return label;
	}


	@Override
	public String toString() {
		return "(" + from + " - " + label + " -> " + to + ")";
	}

}
