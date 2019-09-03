package generators.graph.helpersGraph;

public class Packet {
	
	public Node lastNode;
	public int sumDistance;
	
	public Packet(Node ln, int s) {
		this.lastNode = ln;
		this.sumDistance = s;
	}

}
