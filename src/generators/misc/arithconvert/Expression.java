package generators.misc.arithconvert;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import algoanim.primitives.Graph;
import algoanim.primitives.Text;
import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

/**
 * An arithmetic expression. Can contain any type from literal to complex term.
 * 
 * @author Jannis Weil, Hendrik Wuerz
 */
public abstract class Expression {

	static long currentMaxId = 0;
	/**
	 * Id of this expression (used for unique names)
	 */
	protected long id;

	/**
	 * Horizonatl offset of the elements in text conversion
	 */
	public static int xOffset = 5;
	/**
	 * Radius of the nodes in the graph
	 */
	public static int nodeRadius = 10;

	/**
	 * Padding for text highlighting
	 */
	public static int textHighlightPadding = 5;

	/**
	 * Padding for operator highlighting
	 */
	public static int textOperatorHighlightPadding = 2;

	/**
	 * Push and pop strings (used for text & string conversion)
	 */
	public static String push = "↓";
	public static String pop = "↑";

	protected Node graphNode;

	/**
	 * The graph associated with this expression
	 */
	protected Graph graph;

	/**
	 * The rectangle used for text highlighting of this expression
	 */
	private Rect textHighlight;

	/**
	 * Text coordinate (north west) which is set when createTexts was called.
	 */
	protected Node textNW;

	/**
	 * Text coordinate (south east) which is set when createTexts was called.
	 */
	protected Node textSE;

	/**
	 * Create a new expression with a unique id.
	 */
	public Expression() {
		id = currentMaxId;
		currentMaxId++;
	}

	/**
	 * @return the main name of the given expression.
	 */
	public String getMainTextName() {
		return "Exp_" + String.valueOf(id);
	}

	/**
	 * Returns a printable String representation of that Expression object.
	 */
	public abstract String toString();

	/**
	 * Create a Text representation of this expression and all it's
	 * subexpressions. Also adds these Text elements to the given language
	 * object.
	 * 
	 * @param lang
	 *            the language object which is used to create the text
	 * @param textProperties
	 *            the properties which are given to the Text objects
	 * @param position
	 *            the starting position
	 * @return the last (most right) Text element generated for this Expression
	 */
	public abstract Text createTexts(Language lang,
			TextProperties textProperties, Node position);

	/**
	 * @return the depth of this expression.
	 */
	public abstract int getDepth();

	/**
	 * @return the amount of expressions in this subexpression
	 */
	public abstract int getSubExpressionsCount();

	/**
	 * Recursively create the nodes of this expression which are needed to
	 * create a binary tree.
	 * 
	 * @param position
	 *            the current position in the tree
	 * @param nodes
	 *            the list of nodes (new list to start)
	 * @param labels
	 *            the list of labels (new list to start)
	 * @param connections
	 *            the connections between the nodes
	 */
	protected abstract void createNodes(Coordinates position,
			LinkedList<Node> nodes, LinkedList<String> labels,
			HashMap<Node, Set<Node>> connections);

	/**
	 * Recursively set the specified graph to allow highlighting of nodes.
	 * 
	 * @param g
	 *            the graph which contains the node of this expression
	 */
	protected abstract void setGraph(Graph g);

	/**
	 * Highlight the single node which represents this expression (literal or
	 * operator).
	 */
	public void highlightNode() {
		if (graph != null) {
			graph.highlightNode(graphNode, null, null);
		}
	}

	/**
	 * Unhighlight the single node which represents this expression (literal or
	 * operator).
	 */
	public void unhighlightNode() {
		if (graph != null) {
			graph.unhighlightNode(graphNode, null, null);
		}
	}

	/**
	 * Highlight all the nodes which are in this expression according to the
	 * mode.
	 */
	public abstract void highlightTree(TreehighlightMode mode);

	/**
	 * Unighlight all the nodes which are in this expression addording to the
	 * mode.
	 */
	public abstract void unhighlightTree(TreehighlightMode mode);

	/**
	 * Highlight the text object associated with this expression.
	 * 
	 * @param lang
	 *            the language used to draw a rectangle around the text
	 * @param rp
	 *            the properties of this rectangle
	 */
	public void highlightText(Language lang, RectProperties rp) {
		if (textNW != null && textSE != null) {
			textHighlight = lang.newRect(textNW, textSE, getMainTextName()
					+ "_highrect", null, rp);
		}
	}

	/**
	 * Recursively set the highlight color of all the nodes in this tree.
	 * 
	 * @param color
	 *            the new highlight color
	 */
	public abstract void setTreeHighlightColor(Color color);

	/**
	 * Unhighlight the text object of this expression.
	 */
	public void unhighlightText() {
		if (textHighlight != null) {
			textHighlight.hide();
		}
	}

	/**
	 * Create a graph from this expression.
	 * 
	 * @param lang
	 *            the language object which is used to create the graph
	 * @param graphProps
	 *            the graph properties
	 * @param position
	 *            the position of the root node
	 * @return the created graph
	 */
	public Graph createGraph(Language lang, GraphProperties graphProps,
			Coordinates position) {
		LinkedList<Node> nodes = new LinkedList<>();
		LinkedList<String> labels = new LinkedList<>();
		HashMap<Node, Set<Node>> connections = new HashMap<>();

		// create the nodes (and labels)
		createNodes(position, nodes, labels, connections);

		int size = nodes.size();

		// build the edges
		int[][] adjencyMatrix = new int[size][size];
		// insert the transitions
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// check whether there is a transition from Node i to Node j
				Set<Node> nextNodes = connections.get(nodes.get(i));
				// Node i is not a leaf
				if (nextNodes != null) {
					// if Node j is a next Node
					if (nextNodes.contains(nodes.get(j))) {
						// draw the connection
						adjencyMatrix[i][j] = 1;
					}
				}
			}
		}
		Node[] nodesArray = nodes.toArray(new Node[size]);
		String[] labelsArray = labels.toArray(new String[size]);

		// create the graph
		Graph g = lang.newGraph(getMainTextName() + "_graph", adjencyMatrix,
				nodesArray, labelsArray, null, graphProps);
		// set the radius of the nodes
		for (int i = 0; i < size; i++) {
			g.setNodeRadius(i, nodeRadius, null, null);
		}
		// tell the sub-expressions which graph object they belong to
		setGraph(g);

		return g;
	}

	/**
	 * @return the coordinates of the left leaf (works before creating the
	 *         graph)
	 */
	public abstract Coordinates getLeftLeafCoordinates(Coordinates from);

	/**
	 * Convert (and animate the conversion) this expression to a LR postfix
	 * arith instruction sequence.
	 * 
	 * @param lang
	 *            the language used to output the result
	 * @param position
	 *            the position of the result
	 * @param stackPosition
	 *            the position where the stack output starts at
	 * @param textProperties
	 *            properties of the result text
	 * @param rp
	 *            the default highlight color indicating the current focus
	 * @param rpOp
	 *            the operator highlight color when we are done with an operator
	 *            (and subexpression)
	 * @param rpNoLiteral
	 *            the highlight color used when we have to push because the
	 *            other argument of a binary expression was no literal
	 * @param rpPushPop
	 *            the highlight color used to display push / pop events
	 * @return the last text element of the sequence
	 */
	public abstract Text convertToLRPostfix(Language lang, Node position,
			Node stackPosition, TextProperties textProperties,
			RectProperties rp, RectProperties rpOp, RectProperties rpNoLiteral,
			RectProperties rpPushPop);

	/**
	 * Convert this expression to lr-Postfix and put the information about the
	 * conversion into info
	 * 
	 * @param info
	 *            the information about the conversion
	 * @param lr
	 *            whether to convert to lr or rl
	 * @param currentStackSize
	 *            the current size of the stack
	 */
	protected abstract void convertToPostfix(ConvertInformation info,
			boolean lr, int currentStackSize);

	/**
	 * Converts this expression to postfix.
	 * 
	 * @param lr
	 *            whether to evaluate lr or rl
	 * @return the information about this conversion
	 */
	public ConvertInformation convertToPostfix(boolean lr) {
		ConvertInformation info = new ConvertInformation();
		convertToPostfix(info, lr, 0);
		return info;
	}

	/**
	 * Get the string representation of an operator
	 * 
	 * @param op
	 *            the operator
	 * @return a string representation of the operator
	 */
	public String getOperatorString(Operator op) {
		switch (op) {
		case Add:
			return "+";
		case Sub:
			return "-";
		case Mul:
			return "*";
		case Div:
			return "/";
		default:
			throw new RuntimeException("Unknown Operator " + op);
		}
	}
}
