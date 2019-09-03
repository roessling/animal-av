package generators.graph.utils;


import generators.graph.exceptions.FlowException;
import generators.graph.exceptions.NoResidualEdgesException;

import java.util.ArrayList;
import java.util.List;

import algoanim.util.Coordinates;
import generators.graph.state.State;

public class PRGraph {

	private List<PRNode> nodes;
	private List<PREdge> edges;
	private ArrayList<State> results;
	private int currentFlow = 0;
	private Coordinates[] coordinates;

	public PRGraph(List<PRNode> nodes, List<PREdge> edges) {
		super();
		this.results = new ArrayList<State>();
		this.nodes = nodes;
		this.edges = edges;
		for (PRNode node : nodes) {
			ArrayList<PREdge> ingoingEdges = new ArrayList<PREdge>();
			ArrayList<PREdge> outgoingEdges = new ArrayList<PREdge>();
			for (PREdge edge : edges) {
				if (edge.isStart(node)) {
					outgoingEdges.add(edge);
				} else if (edge.isEnd(node)) {
					ingoingEdges.add(edge);
				}
			}
			node.setIngoingEdges(ingoingEdges);
			node.setOutgoingEdges(outgoingEdges);
		}
				
	}
	
	/**
	 * return nodes of graph
	 * 
	 * @return
	 */
	public List<PRNode> getNodes() {
		return this.nodes;
	}

	public void setNodes(List<PRNode> nodes) {
		this.nodes = nodes;
	}

	public List<PREdge> getEdges() {
		return this.edges;
	}

	public void setEdges(List<PREdge> edges) {
		this.edges = edges;
	}

	private boolean push(PREdge vw, boolean reverse) {
		Integer delta = null;
		if (reverse)
			delta = Math.min(vw.getEnd().getFlowExcess(),
					vw.getReverseResidualFlow());
		else
			delta = Math.min(vw.getStart().getFlowExcess(),
					vw.getResidualFlow());
		if (reverse) {
			try {
				vw.decreaseCurrentFlow(delta);
			} catch (FlowException e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			try {
				vw.increaseCurrentFlow(delta);
			} catch (FlowException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return true;
	}

	public Integer calcMaxFlow() {
		//System.out.println(nodes);
		currentFlow = 0;
		results.add(new State("START",Math.abs(getStart().getFlowExcess()), this.nodes, this.edges, null, null, null, null));
		initializePushRelabelAlgorithm();
//		results.add(new State("AFTER INITIALIZATION",null, nodes, edges,  null,null, null, null));
		
		List<PRNode> activeNodes = getActiveNodes();

		results.add(new State("GET ACTIVE NODES",Math.abs(getStart().getFlowExcess()), nodes, edges, null, activeNodes,  null,null));
		
		results.add(new State("CHECK FOR ACTIVE NODES",Math.abs(getStart().getFlowExcess()), nodes, edges, null, activeNodes,  null,null));
		PRNode activeNode = null;
		while (activeNodes.size() > 0) {
			
			activeNode = activeNodes.remove(0);

			results.add(new State("GET ACTIVE NODE",Math.abs(getStart().getFlowExcess()), nodes, edges, activeNode, activeNodes,  null,null));
			
			results.add(new State("CHECK ACTIVE NODE",Math.abs(getStart().getFlowExcess()), nodes, edges, activeNode, activeNodes,  null,null));
			while (activeNode.isActive()) {

				// Zeile 9 prep
				List<PREdge> possibleEdges = null;
				try {
					possibleEdges = activeNode.getResidualGraphEdgesWithStart();
				} catch (NoResidualEdgesException e1) {
					e1.printStackTrace();
				}
				results.add(new State("GET POSSIBLE EDGES",Math.abs(getStart().getFlowExcess()), nodes, edges, activeNode, activeNodes,  null, possibleEdges));
				
				// Zeile 9 prep
				PREdge minEdge = activeNode.getMinDistanceEdge(possibleEdges);
				Integer min = activeNode.getDistance();
				boolean reverse = !minEdge.isStart(activeNode);
				if (reverse) {
					min = minEdge.getStart().getDistance();
				} else {
					min = minEdge.getEnd().getDistance();
				}
				
				results.add(new State("GET MIN EDGE",Math.abs(getStart().getFlowExcess()), nodes, edges, activeNode, activeNodes, minEdge, possibleEdges));
				
				
				// Zeile 9
				results.add(new State("CHECK FOR PUSH OR RELABEL",Math.abs(getStart().getFlowExcess()), nodes, edges, activeNode, activeNodes, minEdge, possibleEdges));
				if (min + 1 == activeNode.getDistance()) {
					// Zeile 10+11+12 PUSH
					push(minEdge, reverse);
					
					currentFlow ++;
					
					results.add(new State("AFTER PUSH",Math.abs(getStart().getFlowExcess()), nodes, edges, activeNode, activeNodes, minEdge, possibleEdges));
					
					PRNode newActiveNode = null;
					if (reverse) {
						newActiveNode = minEdge.getStart();
					} else {
						newActiveNode = minEdge.getEnd();
					}
					// Zeile 13
					if (newActiveNode.isActive()
							&& !activeNodes.contains(newActiveNode)
							&& !(newActiveNode.isT() || newActiveNode.isS())) {
						activeNodes.add(newActiveNode);
						results.add(new State("UPDATE ACTIVE NODES",Math.abs(getStart().getFlowExcess()), nodes, edges, activeNode, activeNodes, minEdge, possibleEdges));
					}
				} else { // Zeile 14
					// Zeile 15 RELABEL
					activeNode.setDistance(min + 1);
					results.add(new State("AFTER RELABEL",Math.abs(getStart().getFlowExcess()), nodes, edges, activeNode, activeNodes, null, possibleEdges));
				}
				
				results.add(new State("CHECK ACTIVE NODE",Math.abs(getStart().getFlowExcess()), nodes, edges, activeNode, activeNodes,  null,null));

			}
			
			results.add(new State("CHECK FOR ACTIVE NODES",Math.abs(getStart().getFlowExcess()), nodes, edges, null, activeNodes,  null,null));


		}
		
		results.add(new State("END",Math.abs(getStart().getFlowExcess()), this.nodes, this.edges, null, null, null, null));
		
		//System.out.println(toString());
		
//		System.out.println(results);
		return Math.abs(getStart().getFlowExcess());
	}

	private PRNode getStart() {
		for (PRNode node : nodes) {
			if (node.isS())
				return node;
		}
		return null;
	}

	private void initializePushRelabelAlgorithm() {
		for (PRNode node : nodes) {
			if (node.isS()) {
				node.setDistance(nodes.size());
			}
		}
		results.add(new State("INITIALIZE HEIGHT OF START",null, this.nodes, this.edges, null, null, null, null));
		for (PRNode node : nodes) {
			if (node.isS()) {
				List<PREdge> outgoingEdges = node.getOutgoingEdges();
				for (PREdge edge : outgoingEdges) {
					edge.setCurrentFlow(edge.getCapacity());
				}
			}
		}
		results.add(new State("INITIALIZE FLOW OF START",null, this.nodes, this.edges, null, null, null, null));
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (PRNode node : nodes) {
			sb.append(node.toString());
			sb.append("\n");
			List<PREdge> outgoingEdges = node.getOutgoingEdges();
			if (outgoingEdges.size() > 0) {
				for (PREdge edge : outgoingEdges) {
					sb.append(edge.toString());
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

	public List<PRNode> getActiveNodes() {
		List<PRNode> activeNodes = new ArrayList<PRNode>();
		for (PRNode node : nodes) {
			if (node.isActive()) {
				activeNodes.add(node);
			}
		}
		return activeNodes;
	}

//	private PRNode getEnd() {
//		for (PRNode node : nodes) {
//			if (node.isT())
//				return node;
//		}
//		return null;
//	}

	public ArrayList<State> runPushRelabel() {
		this.calcMaxFlow();
		return this.results;
	}

	public void setCoordinates(Coordinates[] coordinates) {
		this.coordinates = coordinates;
	}

	public Coordinates[] getCoordinates() {
		// TODO Auto-generated method stub
		return coordinates;
	}

}
