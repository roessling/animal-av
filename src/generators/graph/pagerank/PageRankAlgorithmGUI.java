/**
 * 
 */
package generators.graph.pagerank;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author marc
 *
 */
public class PageRankAlgorithmGUI extends PageRankGUI {
	private GraphInfoGUI guiGraph;
	
	// The currently highlighted node
	private Node highlightedNode;
	
	private MatrixProperties guiStringMatrixProperties;
	// the edge weight matrix (lower left matrix)
	private StringMatrix guiEdgeWeights;
	// the random surfer weight matrix (lower right matrix)
	private StringMatrix guiRandomSurferWeights;
	
	// The node weights matrix (upper matrix)
	private StringMatrix guiNodeWeights;
	
	private SourceCodeProperties sourceCodeProperties;
	// The source code
	private SourceCode sourceCode;

	private TextProperties normalTextProperties;
	// The text showing the current iteration
	private Text iterationText;
	// The text showing the termination constraint
	private Text terminationConstraintText;
	// The text showing the jump operator
	private Text edgeJumpOperatorText;
	// The text showing 1 - the jump operator
	private Text randomSurferJumpOperatorText;
	
	private Variables variables;
	
	private PageRankAlgorithm algorithm;
	
	private TerminationInterface terminationMethod;
	
	private double jumpOperator;
	
	private boolean danglingNodes;
	
	// The current iteration
	private int iteration;
	
	public PageRankAlgorithmGUI(Language lang, Text header, GraphInfoGUI guiGraph, TerminationInterface terminationMethod, boolean danglingNodes, double jumpOperator, MatrixProperties guiStringMatrixProperties, SourceCodeProperties sourceCodeProperties, TextProperties normalTextProperties) {
		super(lang, header);
		this.guiGraph = guiGraph;
		this.terminationMethod = terminationMethod;
		this.danglingNodes = danglingNodes;
		this.jumpOperator = jumpOperator;
		this.sourceCodeProperties = sourceCodeProperties;
		this.guiStringMatrixProperties = guiStringMatrixProperties;
		this.normalTextProperties = normalTextProperties;
		
		this.iteration = 0;
		
		this.createAlgorithm();
		this.hide();
	}
	
	public void createAlgorithm() {
		this.algorithm = new PageRankAlgorithm(this);
		
		this.initWeightComponents();
		this.initSourceCode();
	}	

	private void initWeightComponents() {
		// set the edge weights between the nodes
		String[][] edges = new String[guiGraph.numberOfNodes() + 1][guiGraph.numberOfNodes() + 1];
		edges[0][0] = "";
		for (int nodeIndex = 0; nodeIndex < guiGraph.numberOfNodes(); nodeIndex++) {
			edges[0][nodeIndex + 1] = edges[nodeIndex + 1][0] = guiGraph.getNodeLabel(nodeIndex);
		}
		for (int rowIndex = 0; rowIndex < guiGraph.numberOfNodes(); rowIndex++) {
			for (int columnIndex = 0; columnIndex < guiGraph.numberOfNodes(); columnIndex++) {
				edges[rowIndex + 1][columnIndex + 1] = String.valueOf(algorithm.getEdgeWeight(rowIndex, columnIndex));
			}
		}
		
		// set the random surfer edgeWeights
		String[][] randomSurferEdges = new String[guiGraph.numberOfNodes() + 1][guiGraph.numberOfNodes() + 1];
		randomSurferEdges[0][0] = "";
		for (int nodeIndex = 0; nodeIndex < guiGraph.numberOfNodes(); nodeIndex++) {
			randomSurferEdges[0][nodeIndex + 1] = randomSurferEdges[nodeIndex + 1][0] = guiGraph.getNodeLabel(nodeIndex);
		}
		for (int rowIndex = 0; rowIndex < guiGraph.numberOfNodes(); rowIndex++) {
			for (int columnIndex = 0; columnIndex < guiGraph.numberOfNodes(); columnIndex++) {
				randomSurferEdges[rowIndex + 1][columnIndex + 1] = String.valueOf(1d / guiGraph.numberOfNodes());
			}
		}

		// set the weights of the nodes
		String[][] nodes = new String[3][guiGraph.numberOfNodes() + 1];
		nodes[0][0] = "";
		nodes[1][0] = "Alte Knotengewichte (x\u2099)";
		nodes[2][0] = "Aktuelle Knotengewichte (x\u2099\u208A\u2081)";
		for (int nodeIndex = 0; nodeIndex < guiGraph.numberOfNodes(); nodeIndex++) {
			nodes[0][nodeIndex + 1] = guiGraph.getNodeLabel(nodeIndex);
			nodes[1][nodeIndex + 1] = "";
			nodes[2][nodeIndex + 1] = String.valueOf(algorithm.getNodeWeight(guiGraph.getNode(nodeIndex)));
		}
		
		this.variables = lang.newVariables();
		this.variables.declare("String", "DanglingNodes", String.valueOf(this.danglingNodes));
		this.variables.declare("double", "JumpOperator", String.valueOf(this.jumpOperator));
		this.terminationMethod.declareParameters(this.variables);		
		
		this.iterationText = lang.newText(new Offset(50, 0, this.guiGraph.getGraph(), "NE"), String.format("Iteration: %d", iteration), "Iteration", null, normalTextProperties);
		
		this.guiNodeWeights = lang.newStringMatrix(new Offset(0, 20, this.iterationText, "SW"), nodes, "Nodes", null,
				guiStringMatrixProperties);
		for (int column = 0; column < guiNodeWeights.getNrCols(); column++) {			
			this.guiNodeWeights.setGridHighlightFillColor(2, column, Color.blue, null, null);
		}
		
		this.terminationConstraintText = lang.newText(new Offset(20, 20, this.guiNodeWeights, "SW"), "", "TerminationConstraint", null, normalTextProperties);
		
		this.guiEdgeWeights = lang.newStringMatrix(new Offset(50, 20, this.terminationConstraintText, "SW"), edges, "Edges", null,
				guiStringMatrixProperties);
		
		this.edgeJumpOperatorText = lang.newText(new Offset(-70, 0, this.guiEdgeWeights, "W"), "G = " + String.valueOf(jumpOperator), "JumpOperator", null, normalTextProperties);		
		
		this.guiRandomSurferWeights = lang.newStringMatrix(new Offset(120, 0, this.guiEdgeWeights, "NE"), randomSurferEdges, "RandomSurferEdges", null, guiStringMatrixProperties);
		
		this.randomSurferJumpOperatorText = lang.newText(new Offset(15, 0, this.guiEdgeWeights, "E"), "+ (1.0 - " + String.valueOf(jumpOperator) + ")", "OtherJumpOperator", null, normalTextProperties);
	}

	private void initSourceCode() {
		this.sourceCode = lang.newSourceCode(new Offset(-70, 20, this.guiEdgeWeights, "SW"), "SourceCode", null, sourceCodeProperties);
		this.sourceCode.addCodeLine("Setze die aktuellen Knotengewichte x\u2099\u208A\u2081 auf 1", null, 0, null);
		
		this.sourceCode.addCodeLine("Erzeuge die Kantengewichtsmatrix G:", null, 0, null);
		
		this.sourceCode.addCodeLine("Erzeuge die allgemeine Adjazenzmatrix für den Graphen wie folgt:", null, 1, null);
		this.sourceCode.addCodeLine("Existiert keine Kante von Knoten X zu Y, dann setze den Wert in der Zeile von X und in der Spalte von Y auf 0", null, 2, null);
		this.sourceCode.addCodeLine("Existiert eine Kante von Knoten X zu Knoten Y setze den Wert in der Zeile von X und in der Spalte von Y auf", null, 2, null);
		this.sourceCode.addCodeLine("1 geteilt durch die Summe aller ausgehenden Kanten aus X", null, 3, null);
		
		this.sourceCode.addCodeLine("Erzeuge die Random Surfer-Adjazenzmatrix für den Graphen mit allen Werten gleich 1 geteilt durch die Anzahl existierender Knoten", null, 1, null);
		
		this.sourceCode.addCodeLine("Berechne die Summe der allgemeinen Adjazenzmatrix, multipliziert mit dem Random Surfer-Parameter, und", null, 1, null);
		this.sourceCode.addCodeLine("der Random Surfer-Adjazenzmatrix, multipliziert mit 1 minus dem Random Surfer Jump Operator", null, 2, null);
		
		this.sourceCode.addCodeLine("Wiederhole, solange die Distanz zwischen den alten Knotengewichten und den aktuellen Knotengewichten größer als der vorher festgelegte Threshold ist:", null, 0, null);
		this.sourceCode.addCodeLine("Setze die alten Knotengewichte x\u2099 auf die Werte der aktuellen Knotengewichte x\u2099\u208A\u2081", null, 1, null);
		this.sourceCode.addCodeLine("Wiederhole für jeden Knoten:", null, 1, null);
		
		this.sourceCode.addCodeLine("Berechne das Gewicht des betrachteten Knoten als Produkt aller alten Knotengewichte x\u2099", null, 2, null);
		this.sourceCode.addCodeLine("multipliziert mit der Kantengewichtsmatrix G", null, 3, null);
	}

	public void highlightNodeWeightCalculationStep(Node highlightNode, double weight) {
		this.unhighlightNodeWeightCalculationAll();

		this.guiNodeWeights.put(2, guiGraph.indexOf(highlightNode) + 1, String.valueOf(weight), null, null);
		this.guiGraph.setNodeLabel(highlightNode, String.format("%s (%.3g)", guiGraph.getNodeLabel(highlightNode), weight));
		this.highlightedNode = highlightNode;

		this.highlightNodeWeightCalculationAll();
	}

	public void unhighlightNodeWeightCalculationAll() {
		if (this.highlightedNode == null)
			return;

		// unhighlight graph
		guiGraph.getGraph().unhighlightNode(highlightedNode, null, null);
		for (Node otherNode : guiGraph.getNodes()) {
			if (algorithm.getEdgeWeight(guiGraph.indexOf(otherNode), guiGraph.indexOf(highlightedNode)) != 0d) {
				guiGraph.getGraph().unhighlightEdge(otherNode, highlightedNode, null, null);
			}
		}

		// unhighlight node weight array
		guiNodeWeights.unhighlightCellColumnRange(1, 1, guiGraph.numberOfNodes(), null, null);
		guiNodeWeights.unhighlightCell(2, guiGraph.indexOf(highlightedNode) + 1, null, null);

		// unhighlight edge weight array
		guiEdgeWeights.unhighlightCellRowRange(0, guiGraph.numberOfNodes(), guiGraph.indexOf(highlightedNode) + 1, null, null);
		guiRandomSurferWeights.unhighlightCellRowRange(0, guiGraph.numberOfNodes(), guiGraph.indexOf(highlightedNode) + 1, null, null);
		
		this.highlightedNode = null;
	}

	private void highlightNodeWeightCalculationAll() {
		// unhighlight graph
		guiGraph.getGraph().highlightNode(highlightedNode, null, null);
		for (Node otherNode : guiGraph.getNodes()) {
			if (algorithm.getEdgeWeight(guiGraph.indexOf(otherNode), guiGraph.indexOf(highlightedNode)) != 0d) {
				guiGraph.getGraph().highlightEdge(otherNode, highlightedNode, null, null);
			}
		}

		// unhighlight node weight array
		guiNodeWeights.highlightCellColumnRange(1, 1, guiGraph.numberOfNodes(), null, null);
		guiNodeWeights.highlightCell(2, guiGraph.indexOf(highlightedNode) + 1, null, null);

		// unhighlight edge weight array
		guiEdgeWeights.highlightCellRowRange(0, guiGraph.numberOfNodes(), guiGraph.indexOf(highlightedNode) + 1, null, null);
		guiRandomSurferWeights.highlightCellRowRange(0, guiGraph.numberOfNodes(), guiGraph.indexOf(highlightedNode) + 1, null, null);
	}

	public void highlightSourceCode(SourceCodePosition position) {
		switch (position) {
		case NODE_WEIGHT_CREATION:
			sourceCode.highlight(0);
			break;
		case EDGE_WEIGHT_CREATION:
			sourceCode.highlight(1);
			sourceCode.highlight(2);
			sourceCode.highlight(3);
			sourceCode.highlight(4);
			sourceCode.highlight(5);
			break;
		case RANDOM_SURFER_EDGE_WEIGHT_CREATION:
			sourceCode.highlight(1);
			sourceCode.highlight(6);
			break;
		case RANDOM_SURFER_PARAMETER_CREATION:
			sourceCode.highlight(1);
			sourceCode.highlight(7);
			sourceCode.highlight(8);
			break;
		case BEGIN_NEW_ITERATION:
		case EVALUATE_TERMINATION_CONSTRAINT:
			sourceCode.highlight(9);
			break;
		case UPDATE_OLD_NODE_WEIGHTS:
			sourceCode.highlight(10);			
			break;
		case CALCULATE_NODE_WEIGHT:
			sourceCode.highlight(12);
			sourceCode.highlight(13);
			break;
		default:
		}
	}

	public void unhighlightSourceCode() {
		for (int index = 0; index < sourceCode.length(); index++)
			sourceCode.unhighlight(index);
	}
	
	public void setTerminationConstraintText(double[] oldValues, double[] currentValues, TerminationInterface termination) {
		this.terminationConstraintText.setText(termination.toString(oldValues, currentValues), null, null);
		
		this.terminationConstraintText.show();
	}
	
	public void setCurrentNodeWeightsAsOldNodeWeights() {
		for (int i = 0; i < guiGraph.numberOfNodes(); i++) {
			guiNodeWeights.put(1, i + 1, guiNodeWeights.getElement(2, i + 1), null, null);
		}
	}
	
	public void updateIteration() {
		this.iterationText.setText(String.format("Iteration: %d", ++iteration), null, null);
	}
	
	public int getIteration() {
		return this.iteration;
	}
	
	@Override
	public void hide() {
		this.iterationText.hide();
		this.guiGraph.hide();
		this.guiNodeWeights.hide();
		this.guiEdgeWeights.hide();
		this.guiRandomSurferWeights.hide();
		this.terminationConstraintText.hide();
		this.randomSurferJumpOperatorText.hide();
		this.edgeJumpOperatorText.hide();
		this.sourceCode.hide();
		this.terminationConstraintText.hide();
	}
	
	public void hideTerminationConstraintText() {
		this.terminationConstraintText.hide();
	}
	
	public void showGraph() {
		this.guiGraph.show();
	}
	
	public void showSourceCode() {
		this.sourceCode.show();
	}

	public void showNodeWeights() {
		this.iterationText.show();
		this.guiNodeWeights.show();
	}
	
	public void showEdgeWeights() {
		this.guiEdgeWeights.show();
	}
	
	public void showRandomEdgeWeights() {
		this.guiRandomSurferWeights.show();
	}
	
	public void showRandomSurferParameter() {
		this.edgeJumpOperatorText.show();
		this.randomSurferJumpOperatorText.show();
	}
	
	public GraphInfoGUI getGraphInfo() {
		return this.guiGraph;
	}
	
	public TerminationInterface getTerminationMethod() {
		return this.terminationMethod;
	}
	
	public boolean isDanglingNodes() {
		return this.danglingNodes;
	}
	
	public double getJumpOperator() {
		return this.jumpOperator;
	}
	
	public Map<Node, Double> getNodeWeights() {
		Map<Node, Double> result = new HashMap<Node, Double>();
		
		for (Node node : guiGraph.getNodes()) {
			result.put(node, algorithm.getNodeWeight(node));
		}
		
		return result;
	}
	
	@Override
	public void show() {
		this.algorithm.doPageRank();
	}
}
