package generators.compression.huffman.guielements.tree;

import generators.compression.huffman.style.HuffmanStyle;

import java.util.ArrayList;
import java.util.HashMap;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class Tree {

	private Language lang;

	private HuffmanStyle huffmanStyle;
	/**
	 * the y step size between two layers of the tree (in direction screen
	 * upwards)
	 */
	public static final int Y_STEP_SIZE = -40;
	/**
	 * the x step size between two nodes in the bottom layer of the tree
	 */
	public static final int X_STEP_SIZE = 55;

	/**
	 * the id's of the bottom tree nodes linked to their position from the left
	 * (starting with 1)
	 */
	private HashMap<Integer, Integer> bottomIdToPosition;

	/**
	 * the tree height of the precalculated tree
	 */
	private int treeHeight;

	private ArrayList<TreeNode> nodes;
	private ArrayList<TreeEdge> edges;
	private ArrayList<TreeLeaveSymbol> leaveSymbols;

	private Primitive offsetReference;

	public Tree(Language lang, HuffmanStyle huffmanStyle,
			Primitive offsetReference,
			HashMap<Integer, Integer> bottomIdToPosition, int treeHeight) {

		this.lang = lang;
		this.huffmanStyle = huffmanStyle;
		this.bottomIdToPosition = bottomIdToPosition;
		this.treeHeight = treeHeight;
		this.offsetReference = offsetReference;

		leaveSymbols = new ArrayList<TreeLeaveSymbol>();
		edges = new ArrayList<TreeEdge>();
		nodes = new ArrayList<TreeNode>();
	}

	public int size() {
		return nodes.size();
	}

	public TreeNode getRoot() {
		return nodes.get(nodes.size() - 1);
	}

	public String getSymbol(int id) {

		String result = null;

		for (TreeLeaveSymbol symbol : leaveSymbols) {

			if (symbol.getID() == id) {
				result = Character.toString(symbol.getSymbol());
			}
		}

		return result;
	}

	/**
	 * draws a node in the bottom layer of the tree
	 * 
	 * @param id
	 *            the ID of the new node
	 * @param charac
	 *            the symbol of the new node
	 * @param freq
	 *            the frequency of the new node
	 */
	public void makeBottomNode(int id, char charac, int freq) {

		int xPosition = (bottomIdToPosition.get(id) - 1) * X_STEP_SIZE;
		Offset circleOffset = new Offset(xPosition + 15, treeHeight
				* Y_STEP_SIZE * -1 + 65, offsetReference,
				AnimalScript.DIRECTION_SW);

		Circle node = lang.newCircle(circleOffset, 15, "node" + id, null,
				(CircleProperties) huffmanStyle
						.getProperties(HuffmanStyle.CIRCLE));

		Offset nodeFreqOffset = new Offset(0, -8, node,
				AnimalScript.DIRECTION_C);
		Text nodeFreq = lang.newText(nodeFreqOffset, Integer.toString(freq),
				"node" + id + "Freq", null, (TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.FREQUENCY));

		Offset nodeNoOffset = new Offset(0, -26, nodeFreq,
				AnimalScript.DIRECTION_C);
		Text nodeNumber = lang.newText(nodeNoOffset, Integer.toString(id),
				"node" + id + "No", null, (TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.NUMBER));

		nodes.add(new TreeNode(node, nodeNumber, nodeFreq, 1, xPosition, id));

		Offset rectOffsetUpperLeft = new Offset(0, 2, node,
				AnimalScript.DIRECTION_SW);
		Offset rectOffsetLowerRight = new Offset(0, 22, node,
				AnimalScript.DIRECTION_SE);
		Rect nodeRect = lang.newRect(rectOffsetUpperLeft, rectOffsetLowerRight,
				"node" + id + "Rect", null);

		Offset nodeCharOffset = new Offset(0, -4, nodeRect,
				AnimalScript.DIRECTION_C);
		Text symbol = lang.newText(nodeCharOffset, Character.toString(charac),
				"node" + id + "Char", null, (TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.CHARACTER));

		leaveSymbols.add(new TreeLeaveSymbol(nodeRect, symbol, id, charac));
	}

	/**
	 * determines the position of the new node in the tree and draws the node
	 * and the two edges to the child nodes
	 * 
	 * @param firstNodeID
	 *            the id of the first child in the queue
	 * @param secondNodeID
	 *            the id of the second child in the queue
	 * @param freq
	 *            the frequency of the new node in the tree
	 */
	public void makeParentNode(int firstNodeID, int secondNodeID, int freq) {

		TreeNode node1 = nodes.get(firstNodeID - 1);
		TreeNode node2 = nodes.get(secondNodeID - 1);

		TreeNode leftNode = node1;
		TreeNode rightNode = node2;
		if (node1.getxCoordinate() > node2.getxCoordinate()) {
			rightNode = node1;
			leftNode = node2;
		}

		int layerFirstNode = node1.getLayer();
		int layerSecondNode = node2.getLayer();
		TreeNode highestNode = layerFirstNode > layerSecondNode ? node1 : node2;

		Circle highestNodeCircle = highestNode.getCircle();
		int highestLayer = highestNode.getLayer();
		int xPosition = (node1.getxCoordinate() + node2.getxCoordinate()) / 2;

		int relativePosX = xPosition - highestNode.getxCoordinate();

		TreeNode parent = drawNode(highestNodeCircle, highestLayer + 1,
				xPosition, relativePosX, Integer.toString(freq));
		parent.setLeftNode(leftNode);
		parent.setRightNode(rightNode);
		nodes.add(parent);

		leftNode.setParent(parent);
		rightNode.setParent(parent);

		drawEdges(parent, leftNode, rightNode);
	}

	/**
	 * draws a new tree node
	 * 
	 * @param highestChildCircle
	 *            the Circle of the child node in the highest layer
	 * @param layer
	 *            the layer of the new node
	 * @param xPosition
	 *            the total x position of the new node
	 * @param relativePosX
	 *            the x position of the new node relative to the child node in
	 *            the highest layer
	 * @param frequency
	 *            the frequency of the new node
	 * @return the new node
	 */
	private TreeNode drawNode(Circle highestChildCircle, int layer,
			int xPosition, int relativePosX, String frequency) {

		Offset circleOffset = new Offset(relativePosX, Y_STEP_SIZE,
				highestChildCircle, AnimalScript.DIRECTION_C);
		Circle node = lang.newCircle(circleOffset, 15, "node" + nodes.size(),
				null, (CircleProperties) huffmanStyle
						.getProperties(HuffmanStyle.CIRCLE));

		Offset nodeFreqOffset = new Offset(0, -8, node,
				AnimalScript.DIRECTION_C);
		Text nodeFreq = lang.newText(nodeFreqOffset, frequency,
				"node" + nodes.size() + "Freq", null,
				(TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.FREQUENCY));

		Offset nodeNoOffset = new Offset(0, -26, nodeFreq,
				AnimalScript.DIRECTION_C);
		Text nodeNumber = lang.newText(nodeNoOffset, Integer.toString(nodes
				.size() + 1), "node" + (nodes.size() + 1) + "No", null,
				(TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.NUMBER));

		return new TreeNode(node, nodeNumber, nodeFreq, layer, xPosition,
				nodes.size() + 1);
	}

	/**
	 * draws the 2 edges of a parent node to its children (including the 0s and
	 * 1s on the egdes)
	 * 
	 * @param parent
	 *            the parent node
	 * @param leftNode
	 *            the left child
	 * @param rightNode
	 *            the right child
	 */
	private void drawEdges(TreeNode parent, TreeNode leftNode,
			TreeNode rightNode) {

		Offset o1 = new Offset(-11, 11, parent.getCircle(),
				AnimalScript.DIRECTION_C);
		Offset o2 = new Offset(0, -15, leftNode.getCircle(),
				AnimalScript.DIRECTION_C);
		Polyline edge = lang.newPolyline(new Node[] { o1, o2 },
				"edge" + nodes.size() + "to" + leftNode.getID(), null);

		int relativeXzero = (parent.getxCoordinate() - leftNode
				.getxCoordinate());
		int relativeYzero = (parent.getLayer() - leftNode.getLayer())
				* Y_STEP_SIZE;

		Text edgeText = lang.newText(new Offset((relativeXzero - 11) / 2 - 6,
				(relativeYzero - 15 - 11) / 2, leftNode.getCircle(),
				AnimalScript.DIRECTION_C), "0", "edgeText" + leftNode.getID(),
				null);

		TreeEdge treeEdge = new TreeEdge(edge, edgeText);
		leftNode.setUpwardEdge(treeEdge);
		edges.add(treeEdge);

		o1 = new Offset(11, 11, parent.getCircle(), AnimalScript.DIRECTION_C);
		o2 = new Offset(0, -15, rightNode.getCircle(), AnimalScript.DIRECTION_C);
		edge = lang.newPolyline(new Node[] { o1, o2 }, "edge" + nodes.size()
				+ "to" + rightNode.getID(), null);

		int relativeXone = (parent.getxCoordinate() - rightNode
				.getxCoordinate());
		int relativeYone = (parent.getLayer() - rightNode.getLayer())
				* Y_STEP_SIZE;

		edgeText = lang.newText(new Offset((relativeXone + 11) / 2,
				(relativeYone - 15 - 11) / 2 - 3, rightNode.getCircle(),
				AnimalScript.DIRECTION_C), "1", "edgeText" + rightNode.getID(),
				null);

		treeEdge = new TreeEdge(edge, edgeText);
		rightNode.setUpwardEdge(treeEdge);
		edges.add(treeEdge);
	}

	/**
	 * hides the whole tree
	 */
	public void hide() {

		for (TreeLeaveSymbol symbol : leaveSymbols) {
			symbol.hide();
		}

		for (TreeNode node : nodes) {
			node.hide();
		}

		for (TreeEdge edge : edges) {
			edge.hide();
		}
	}

	/**
	 * shows the whole tree
	 */
	public void show() {

		for (TreeLeaveSymbol symbol : leaveSymbols) {
			symbol.show();
		}

		for (TreeNode node : nodes) {
			node.show();
		}

		for (TreeEdge edge : edges) {
			edge.show();
		}
	}

	/**
	 * highlights the path from the root of the tree to the node with
	 * <code>leaveID</code>
	 * 
	 * @param leaveID
	 *            the end of the highlighted path from the root
	 */
	public void highlightPath(int leaveID) {

		markPath(leaveID, true);
	}

	/**
	 * unhighlights the path from the root of the tree to the node with
	 * <code>leaveID</code>
	 * 
	 * @param leaveID
	 *            the end of the unhighlighted path from the root
	 */
	public void unHighlightPath(int leaveID) {

		markPath(leaveID, false);
	}

	private void markPath(int leaveID, boolean highlight) {

		TreeNode leave = nodes.get(leaveID - 1);

		TreeNode node = leave;
		TreeEdge upwardEdge;
		while ((node != null) && (upwardEdge = node.getUpwardEdge()) != null) {

			if (highlight) {
				upwardEdge.highlight();
			} else {
				upwardEdge.unhighlight();
			}
			node = node.getParent();
		}
	}

	public void moveTo(Primitive offsetReference, Timing duration) {

		HashMap<Integer, Circle> alignDummies = new HashMap<Integer, Circle>();

		for (Integer id : bottomIdToPosition.keySet()) {
			int xPosition = (bottomIdToPosition.get(id) - 1) * X_STEP_SIZE;

			Offset offset = new Offset(xPosition + 15, treeHeight * Y_STEP_SIZE
					* -1 + 65, offsetReference, AnimalScript.DIRECTION_SW);

			TreeNode node = nodes.get(id - 1);
			TreeNode parent = node.getParent();
			
			Circle alignDummy = node.moveTo(offset, lang, parent, duration);
			alignDummies.put(id, alignDummy);
			
			leaveSymbols.get(id - 1).moveTo(alignDummy, duration, lang);
		}

		for (int i = 2; i <= treeHeight + 1; i++) {

			ArrayList<TreeNode> layerNodes = getNodesInLayer(i);

			for (TreeNode node : layerNodes) {

				TreeNode rightChild = node.getRightNode();

				int layerDifference = node.getLayer() - rightChild.getLayer();

				int relativePosY = layerDifference * Y_STEP_SIZE;
				int relativePosX = node.getxCoordinate()
						- rightChild.getxCoordinate();

				Offset circleOffset = new Offset(relativePosX, relativePosY,
						alignDummies.get(rightChild.getID()),
						AnimalScript.DIRECTION_C);
				
				TreeNode parent = node.getParent();
				
				Circle alignDummy = node.moveTo(circleOffset, lang, parent, duration);
				alignDummies.put(node.getID(), alignDummy);
			}
		}
	}

	private ArrayList<TreeNode> getNodesInLayer(int layer) {

		ArrayList<TreeNode> layerNodes = new ArrayList<TreeNode>();
		for (TreeNode node : nodes) {
			if (node.getLayer() == layer) {
				layerNodes.add(node);
			}
		}

		return layerNodes;
	}
}
