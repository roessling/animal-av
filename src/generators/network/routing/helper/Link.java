package generators.network.routing.helper;

import algoanim.primitives.Graph;
import algoanim.util.Timing;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Represents a connection between two routers in the network (ie. a edge between two nodes)
 */
public class Link {
	private Graph g;
	
	/**
	 * The cost of transmitting via this link
	 */
	public int cost;
	/**
	 * The start point of the link
	 */
	public Router start;
	/**
	 * The end point of the link
	 */
	public Router end; 
	
	/**
	 * Create a new link
	 * 
	 * @param graph The graph object the network is derived from
	 * @param start The start point of the link
	 * @param end The end point of the link
	 * @param cost The cost of transmitting data over this link
	 */
	public Link(Graph graph, Router start, Router end, int cost) {
		this.g = graph;
		this.start = start;
		this.end = end;
		this.cost = cost;
	}
	
	/**
	 * Highlight the link in the network
	 * 
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void highlight(Timing offset, Timing duration) {
		if(g != null) {
			g.highlightEdge(start.getGraphNodeId(), end.getGraphNodeId(), offset, duration);
		}
	}

	/**
	 * Remove the highlight from the link in the network
	 * 
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void unhighlight(Timing offset, Timing duration) {
		if(g != null) {
			g.unhighlightEdge(start.getGraphNodeId(), end.getGraphNodeId(), offset, duration);
		}
	}
}
