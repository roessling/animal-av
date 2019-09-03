package generators.graph.helpersGraph;

import java.util.ArrayList;
import java.util.HashMap;

import generators.graph.helpersGraph.Neighbour;
import generators.graph.helpersGraph.Packet;

public class Node {
	
	public char name;
	public ArrayList<Neighbour> neighbours;
	public HashMap<Character, Integer> routingTable;
	public Node via;
	public Packet packet;
	public boolean visited;
	
	public Node(char name) {
		this.name = name;
		this.neighbours = new ArrayList<>();
		this.via = null;
		this.packet = null;
		this.visited = false;
	}

}
