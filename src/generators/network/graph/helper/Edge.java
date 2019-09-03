package generators.network.graph.helper;

import algoanim.util.Node;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * Represents an edge in a graph. This edge is comparable and can
 * therefore used in Sets and priority lists.
 */
public class Edge  implements Comparable<Edge> {
	/**
	 * The first node of the edge ie. start.
	 */
	public Node node1;
	/**
	 * The second node of the edge ie. target.
	 */
	public Node node2;
	/**
	 * The weight of the edge.
	 */
	public int weight;

	/**
	 * Create a new edge
	 * 
	 * @param node1 The first node of the edge ie. start
	 * @param node2 The second node of the edge ie. target
	 * @param weight The weight of the edge
	 */
	public Edge(Node node1, Node node2, int weight) {
		this.node1 = node1;
		this.node2 = node2;
		this.weight = weight;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Edge) {
			Edge e = (Edge)o;
			if(e.node1 == this.node1 && e.node2 == this.node2 && e.weight == this.weight) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return node1.hashCode() + node2.hashCode() + weight;
	}

	@Override
	public int compareTo(Edge cmp) {
		if (!(cmp instanceof Edge)) {
			throw new ClassCastException("An Edge Object was expected.");
		} else {
			Edge cmp2 = (Edge)cmp;
			// order by weight
			if(cmp2.weight < this.weight) {
				return 1;
			} else if(cmp2.weight > this.weight) {
				return -1;
			} else {
				// if the edge weight is equal we also have to check if the objects are really equal
				if(this.equals(cmp2)) {
					return 0;
				} else{
					return 1;
				}
			}
		}
	}
}
