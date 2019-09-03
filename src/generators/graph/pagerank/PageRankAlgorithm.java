/**
 * 
 */
package generators.graph.pagerank;

import java.util.HashMap;
import java.util.Map;

import algoanim.util.Node;

/**
 * @author marc
 *
 */
public class PageRankAlgorithm {
	private GraphInfoGUI graphInfo;
	
	private PageRankAlgorithmGUI model;
	
	private Map<Node, Double> nodeWeights;
	
	// Zeile -> Spalte
	private double[][] edgeWeights;	
	
	public PageRankAlgorithm(PageRankAlgorithmGUI model) {
		this.model = model;
		this.graphInfo = model.getGraphInfo();
		
		this.edgeWeights = new double[graphInfo.numberOfNodes()][graphInfo.numberOfNodes()];
		
		this.nodeWeights = new HashMap<>();
		for (Node node : graphInfo.getNodes()) {
			nodeWeights.put(node, new Double(1));
		}
		
		int[][] adjacencyMatrix = graphInfo.getGraph().getAdjacencyMatrix();
		
		for (int row = 0; row < graphInfo.numberOfNodes(); row++) {
			int[] outgoingEdges = adjacencyMatrix[row];
			
			// calculate the sum of outgoing weights per node
			int sum = 0;
			for (int edge : outgoingEdges) {
				sum += edge;
			}
			
			for (int column = 0; column < graphInfo.numberOfNodes(); column++) {
				if (sum == 0 && model.isDanglingNodes()) {
					this.edgeWeights[row][column] = 1d / graphInfo.numberOfNodes();
				} else if (sum == 0 && !model.isDanglingNodes()) {
					this.edgeWeights[row][column] = 0;
				} else {
					this.edgeWeights[row][column] = (double)outgoingEdges[column] / sum;
				}
			}
		}
	}
	
	public double getNodeWeight(Node node) {
		return this.nodeWeights.get(node);
	}
	
	public void setNodeWeight(Node node, double weight) {
		this.nodeWeights.put(node, weight);
		
		model.highlightNodeWeightCalculationStep(node, weight);
	}
	
	public double getEdgeWeight(int outgoingNodeIndex, int incomingNodeIndex) {
		return this.edgeWeights[outgoingNodeIndex][incomingNodeIndex];
	}
	
	/**
	 * This method calculates the page rank. Therefore it takes the initial node
	 * weights from the double array {@link #nodeWeights} and the edge weights
	 * between the nodes from the double matrix {@link #edges} where all values
	 * in a column represent the incoming edges for the corresponding node of
	 * the column.
	 */
	public void doPageRank() {
		double[] lastNodeWeights;
		//this.printNodeWeights();
		//this.printEdgeWeights();
		
		model.showGraph();
		model.showSourceCode();
		model.nextStep();
		
		model.showNodeWeights();
		model.highlightSourceCode(SourceCodePosition.NODE_WEIGHT_CREATION);
		model.nextStep("Initialisierung der Knotengewichte");
		
		model.unhighlightSourceCode();
		model.showEdgeWeights();
		model.highlightSourceCode(SourceCodePosition.EDGE_WEIGHT_CREATION);
		model.nextStep("Initialisierung der normalen Kantengewichte");
		
		model.unhighlightSourceCode();
		model.showRandomEdgeWeights();
		model.highlightSourceCode(SourceCodePosition.RANDOM_SURFER_EDGE_WEIGHT_CREATION);
		model.nextStep("Initialisierung der Random Surfer Knotengewichte");
		
		model.unhighlightSourceCode();
		model.showRandomSurferParameter();
		model.highlightSourceCode(SourceCodePosition.RANDOM_SURFER_PARAMETER_CREATION);
		model.nextStep("Initialisierung des Jump-Operators");
		
		model.unhighlightSourceCode();
		model.highlightSourceCode(SourceCodePosition.BEGIN_NEW_ITERATION);
		model.nextStep();
		
		do {
			lastNodeWeights = getCurrentNodeWeights();
			model.hideTerminationConstraintText();
			model.unhighlightSourceCode();
			model.highlightSourceCode(SourceCodePosition.UPDATE_OLD_NODE_WEIGHTS);
			model.setCurrentNodeWeightsAsOldNodeWeights();
			model.updateIteration();
			model.nextStep(String.format("PageRank Iteration %d", model.getIteration()));
			
			for (Node node : graphInfo.getNodes()) {
				double newNodeWeight = calculateNewNodeWeight(node, lastNodeWeights);
				setCurrentNodeWeight(node, newNodeWeight);
				
				model.unhighlightSourceCode();
				model.highlightSourceCode(SourceCodePosition.CALCULATE_NODE_WEIGHT);
				model.nextStep();
			}
			
			model.unhighlightSourceCode();
			model.highlightSourceCode(SourceCodePosition.EVALUATE_TERMINATION_CONSTRAINT);
			model.setTerminationConstraintText(lastNodeWeights, getCurrentNodeWeights(), model.getTerminationMethod());
			model.unhighlightNodeWeightCalculationAll();
			model.nextStep();
			
			//this.printNodeWeights();
		} while (!model.getTerminationMethod().terminate(lastNodeWeights, getCurrentNodeWeights()));
		
		model.hideTerminationConstraintText();
		model.unhighlightSourceCode();
		model.nextStep();
	}

	/**
	 * This method calculates the new weight of the node with the given index
	 * {@code node}. For the calculation the old weights of the node are needed.
	 * 
	 * @param nodeIndex
	 *            The index of the node whose weight should be calculated
	 * @param nodeWeights
	 *            The old weights of all nodes
	 * @return The new weight of the node with the given index.
	 */
	public double calculateNewNodeWeight(Node node, double[] nodeWeights) {
		double[] graphColumnEdges = getIncomingEdgesForNode(node);

		double result = 0;
		for (int index = 0; index < graphInfo.numberOfNodes(); index++) {
			result += nodeWeights[index] * (model.getJumpOperator() * graphColumnEdges[index] + (1 - model.getJumpOperator()) * (1d / graphInfo.numberOfNodes()));
		}

		return result;
	}

	/**
	 * This method sets the weight of the node with a given index
	 * {@code nodeIndex} to the given weight value {@code weight}
	 * 
	 * @param nodeIndex
	 *            The index of the node whose weight should be set
	 * @param weight
	 *            The new weight of the node
	 */
	public void setCurrentNodeWeight(Node node, Double weight) {
		this.setNodeWeight(node, weight);
	}

	/**
	 * This method returns the current node weights in the form of an array,
	 * where the index of the entry is the index of the node whose weight the
	 * value represents.
	 * 
	 * @return An array containing the weights of all nodes
	 */
	public double[] getCurrentNodeWeights() {
		double[] result = new double[graphInfo.numberOfNodes()];

		for (Node node : graphInfo.getNodes()) {
			result[graphInfo.indexOf(node)] = this.getNodeWeight(node);
		}

		return result;
	}

	/**
	 * This method returns an array containing the weights of all incoming edges
	 * to the node in the given column {@code nodeIndex}.
	 * 
	 * @param nodeIndex
	 *            The column of the node whose edge weights should be returned
	 * @return An array containing the edge weights of the given node
	 */
	public double[] getIncomingEdgesForNode(Node node) {
		double[] result = new double[graphInfo.numberOfNodes()];
		
		Map<Node, Double> edgeWeights = this.getIncomingEdgeWeights(node);
		for (Node viewedNode : graphInfo.getNodes()) {
			result[graphInfo.indexOf(viewedNode)] = edgeWeights.get(viewedNode);
		}

		return result;
	}
	
	/**
	 * This method returns all values in the column of the given viewedNode 
	 * @param viewedNode The node whose incoming edgeweights are wanted
	 * @return
	 */
	public Map<Node, Double> getIncomingEdgeWeights(Node viewedNode) {
		int viewedNodeIndex = graphInfo.indexOf(viewedNode);
		Map<Node, Double> result = new HashMap<>();
		
		for (Node node : graphInfo.getNodes()) {
			int index = graphInfo.indexOf(node);
			
			result.put(node, edgeWeights[index][viewedNodeIndex]);
		}
		
		return result;
	}
	
	/**
	 * This method returns all values in the row of the given viewedNode
	 * @param viewedNode
	 * @return
	 */
	public Map<Node, Double> getOutcomingEdgeWeights(Node viewedNode) {
		int viewedNodeIndex = graphInfo.indexOf(viewedNode);
		Map<Node, Double> result = new HashMap<>();
		
		for (Node node : graphInfo.getNodes()) {
			int index = graphInfo.indexOf(node);
			
			result.put(node, edgeWeights[viewedNodeIndex][index]);
		}
		
		return result;
	}
	
	public void printNodeWeights() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Nodeweights: [");
		for (int nodeIndex = 0; nodeIndex < graphInfo.numberOfNodes(); nodeIndex++) {
			sb.append(nodeWeights.get(graphInfo.getNode(nodeIndex)));
			if (nodeIndex < graphInfo.numberOfNodes() - 1) 
				sb.append(", ");
		}
		sb.append("]");
		
		System.out.println(sb.toString());
	}
	
	public void printEdgeWeights() {
		System.out.println("Edgeweights: ");
		for (double[] row : edgeWeights) {
			for (double cell : row) {
				System.out.print(cell + "\t");
			}
			System.out.println();
		}
	}
}
