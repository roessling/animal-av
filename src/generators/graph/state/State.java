package generators.graph.state;

import java.util.ArrayList;
import java.util.List;

import generators.graph.utils.PREdge;
import generators.graph.utils.PRNode;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class State {

	private Integer flow = null;
	private List<NodeState> nodes = new ArrayList<NodeState>();
	private List<EdgeState> edges = new ArrayList<EdgeState>();
	private NodeState activeNode = null;
	private List<EdgeState> possibleEdges = new ArrayList<EdgeState>();
	private List<NodeState> activeNodes = new ArrayList<NodeState>();
	private String caption;
	private EdgeState minEdge = null;

	public State(String caption, Integer flow, List<PRNode> nodes,
			List<PREdge> edges, PRNode activeNode, List<PRNode> activeNodes,
			PREdge minEdge, List<PREdge> possibleEdges) {
		this.caption = caption;
		if (flow != null)
			this.flow = new Integer(flow);

		this.nodes = new ArrayList<NodeState>();
		if (nodes != null)
			for (PRNode node : nodes) {
				this.nodes.add(new NodeState(node));
			}

		this.edges = new ArrayList<EdgeState>();
		if (edges != null)
			for (PREdge edge : edges) {
				this.edges.add(new EdgeState(edge));
			}
		if (activeNode != null)
			this.activeNode = new NodeState(activeNode);

		this.activeNodes = new ArrayList<NodeState>();
		if (activeNodes != null)
			for (PRNode node : activeNodes) {
				this.activeNodes.add(new NodeState(node));
			}

		if (minEdge != null)
			this.minEdge = new EdgeState(minEdge);

		this.possibleEdges = new ArrayList<EdgeState>();
		if (possibleEdges != null)
			for (PREdge edge : possibleEdges) {
				this.possibleEdges.add(new EdgeState(edge));
			}
	}

	/**
	 * returns a string
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n[");
		sb.append("\tcaption=\t" + this.caption + "\n");
		sb.append("\tflow=\t" + this.flow + "\n");
		sb.append(this.nodesToString("nodes", this.nodes));
		sb.append(this.edgesToString("edges", this.edges));
		sb.append(this.nodesToString("activeNodes", this.activeNodes));
		sb.append("\tactiveNode=\n\t\t" + this.activeNode + "\n");
		sb.append(this.edgesToString("possibleEdges", this.possibleEdges));
		sb.append("\tminEdge=\n\t\t" + this.minEdge + "\n");
		sb.append("]\n");
		return sb.toString();
	}

	/**
	 * returns nodes to string
	 * 
	 * @param caption
	 * @param nodes
	 * @return
	 */
	private String nodesToString(String caption, List<NodeState> nodes) {
		StringBuffer sb = new StringBuffer();
		sb.append("\t" + caption + "=[\n");
		for (NodeState node : nodes) {
			sb.append("\t\t" + node.toString() + "\n");
		}
		sb.append("\t\t]");
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * returns edges to string
	 * 
	 * @param caption
	 * @param edges
	 * @return
	 */
	private String edgesToString(String caption, List<EdgeState> edges) {
		StringBuffer sb = new StringBuffer();
		sb.append("\t" + caption + "=[\n");
		for (EdgeState edge : edges) {
			sb.append("\t\t" + edge.toString() + "\n");
		}
		sb.append("\t\t]");
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * returns nodes
	 * 
	 * @return
	 */
	public List<NodeState> getNodes() {
		return this.nodes;
	}

	/**
	 * returns node labels
	 * 
	 * @return
	 */
	public String[] getNodeLabels() {
		String labels[] = new String[this.nodes.size()];
		int i = 0;
		for (NodeState node : this.nodes) {
			labels[i] = node.getName();
			i++;
		}
		return labels;
	}



	/**
	 * returns Adjacency Matrix
	 * 
	 * @return
	 */
	public int[][] getAdjacencyMatrix() {
		int adjMatrix[][] = new int[this.nodes.size()][this.nodes.size()];
		for (NodeState node : this.nodes) {
			for (EdgeState edge : this.edges) {
				if (edge.getStart().equals(node)) {
					adjMatrix[this.nodes.indexOf(node)][this.nodes.indexOf(edge
							.getEnd())] = edge.getCapacity();
				} else if (edge.getEnd().equals(node)) {
					adjMatrix[this.nodes.indexOf(edge.getStart())][this.nodes
							.indexOf(node)] = edge.getCapacity();
				}
			}
		}
		return adjMatrix;
	}



	/**
	 * returns nodes
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Node[] getCoordinates(int x, int y) {
		int start_x = x;
		int start_y = y;
		int delta = 100;
		
		Coordinates nodes[] = new Coordinates[this.nodes.size()];
		switch (this.nodes.size()) {
		case 2:
			nodes[0] = new Coordinates(start_x, start_y);
			nodes[1] = new Coordinates(start_x + 3 * delta, start_y);
			break;
		case 6:
			nodes[0] = new Coordinates(start_x, start_y);
			nodes[1] = new Coordinates(start_x + delta, start_y - delta);
			nodes[2] = new Coordinates(start_x + delta, start_y + delta);
			nodes[3] = new Coordinates(start_x + 2 * delta, start_y - delta);
			nodes[4] = new Coordinates(start_x + 2 * delta, start_y + delta);
			nodes[5] = new Coordinates(start_x + 3 * delta, start_y);
			break;
		case 4:
//			x,y
//			x+d,y
//			x+d,y-d
//			x+d,y+d
		default:
			break;
		} 
		
		// TODO
//		nodes[0]
//		nodes[8]
		return nodes;
	}

	/**
	 * returns Adjacency Matrix
	 * 
	 * @param reverse
	 * @return
	 */
	public int[][] getAdjacencyMatrix(boolean reverse) {
		int adjMatrix[][] = new int[this.nodes.size()][this.nodes.size()];
		for (NodeState node : this.nodes) {
			for (EdgeState edge : this.edges) {
				if (edge.isReverse()) {
					if (edge.getStart().equals(node)) {
						adjMatrix[this.nodes.indexOf(edge.getEnd())][this.nodes
								.indexOf(node)] = edge.getReverseResidualFlow();
					} else if (edge.getEnd().equals(node)) {
						adjMatrix[this.nodes
								.indexOf(node)][this.nodes.indexOf(edge.getStart())] = edge.getReverseResidualFlow();
					}
				} else {
					if (edge.getStart().equals(node)) {
						adjMatrix[this.nodes.indexOf(node)][this.nodes
								.indexOf(edge.getEnd())] = edge
								.getResidualFlow();
					} else if (edge.getEnd().equals(node)) {
						adjMatrix[this.nodes.indexOf(edge.getStart())][this.nodes
								.indexOf(node)] = edge.getResidualFlow();
					}
				}
			}
		}
		return adjMatrix;
	}

	/**
	 * returns caption
	 * 
	 * @return
	 */
	public String getCaption() {
		// TODO Auto-generated method stub
		return caption;
	}
	/**
	 * returns flow
	 * 
	 * @return
	 */
	public Integer getFlow() {
		return flow;
	}

	/**
	 * returns possible edges
	 * 
	 * @return
	 */
	public List<EdgeState> getPossibleEdges() {
		// TODO Auto-generated method stub
		return possibleEdges;
	}

	/**
	 * returns edges
	 * 
	 * @return
	 */
	public List<EdgeState> getEdges() {
		// TODO Auto-generated method stub
		return this.edges;
	}

	/**
	 * returns active node
	 * 
	 * @return
	 */
	public NodeState getActiveNode() {
		return activeNode;
	}

	/**
	 * returns minimal edge
	 * 
	 * @return
	 */
	public EdgeState getMinimalEdge() {
		// TODO Auto-generated method stub
		return minEdge;
	}
}
