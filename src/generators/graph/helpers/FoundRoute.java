package generators.graph.helpers;

import java.util.LinkedList;

public class FoundRoute {
	
	public float cost;
	
	public LinkedList<Integer> nodes;
	
	public FoundRoute() {
		cost = Float.POSITIVE_INFINITY;
		nodes = new LinkedList<Integer>();
	}
}