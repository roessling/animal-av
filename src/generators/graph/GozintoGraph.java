package generators.graph;

import java.util.ArrayList;

public class GozintoGraph {
	
	private int[][] adjMatrix;
	private ArrayList<GozintoNode> nodes;
	
	public GozintoGraph(ArrayList<GozintoNode> nodes){
		adjMatrix = new int[nodes.size()][nodes.size()];
		this.nodes = nodes;
	}
	
	public void Insert(GozintoNode source, GozintoNode destination, int weight){
		adjMatrix[source.getNumber() - 1][destination.getNumber() - 1] = weight;
	}

	public int[][] getAdjMatrix() {
		return adjMatrix;
	}

	public void setAdjMatrix(int[][] adjMatrix) {
		this.adjMatrix = adjMatrix;
	}

	public ArrayList<GozintoNode> getNodes() {
		return nodes;
	}
}
