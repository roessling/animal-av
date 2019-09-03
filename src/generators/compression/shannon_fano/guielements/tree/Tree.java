package generators.compression.shannon_fano.guielements.tree;

import generators.compression.shannon_fano.style.ShannonFanoStyle;

import java.util.ArrayList;

import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import algoanim.util.Timing;

/**
 * This class represents the tree.
 * 
 * @author Sebastian Fach
 */
public class Tree {

	private Language lang;
	private ShannonFanoStyle style;
	private Primitive offsetReference;

	/**
	 * the y step size between two layers of the tree
	 */
	public static final int STEP_SIZE_Y = 60;
	/**
	 * the x step size between two nodes in the initial layer of the tree
	 */
	public static final int STEP_SIZE_X = 55;

	public static final int OFFSET_TOP = 0;

	public static final int MOVE_DOWN_DURATION = 1000; // in ms

	public static final int SHOW_NEW_PARENT_DELAY = 1000; // in ms

	private int lastNodeId;
	private int lastSetId = 1000000;

	private AbstractNode root;
	private ArrayList<AbstractNode> nodes = new ArrayList<AbstractNode>();

	public Tree(Language lang, ShannonFanoStyle style, Primitive offsetReference) {
		this.lang = lang;
		this.style = style;
		this.offsetReference = offsetReference;
	}

	public Language getLang() {
		return lang;
	}

	public ShannonFanoStyle getStyle() {
		return style;
	}

	public Primitive getOffsetReference() {
		return offsetReference;
	}

	public AbstractNode getRoot() {
		return root;
	}

	public void setRoot(AbstractNode root) {
		this.root = root;
	}

	public int nextNodeId() {
		return ++lastNodeId;
	}

	public int nextSetId() {
		return ++lastSetId;
	}

	public boolean add(AbstractNode node) {
		return nodes.add(node);
	}

	// public int size() {
	// return nodes.size();
	// }

	public ArrayList<AbstractNode> getNodes(int startIndex, int endIndex) {
		ArrayList<AbstractNode> result = new ArrayList<AbstractNode>(endIndex - startIndex);
		for (int i = startIndex; i <= endIndex; i++) {
			result.add(nodes.get(i));
		}
		return result;
	}

	public ArrayList<AbstractNode> getNodes() {
		return nodes;
	}

	// public TreeNode createLeaf(int id, int frequency, float probability, char
	// symbol, int centerX, int centerY) {
	// TreeNode newNode = new TreeNode(id, frequency, probability, symbol,
	// centerX, centerY,
	// offsetReference, lang, style);
	// nodes.add(newNode);
	// return newNode;
	// }
	//
	// public TreeNode createNode(int id, int frequency, float probability, int
	// centerX, int centerY) {
	// TreeNode newNode = new TreeNode(id, frequency, probability, centerX,
	// centerY,
	// offsetReference, lang, style);
	// nodes.add(newNode);
	// return newNode;
	// }

	// public NodeSet createSet(int id, int startIndex, int endIndex) {
	// NodeSet newSet = new NodeSet(id, getNodes(startIndex, endIndex),
	// offsetReference, lang, style);
	// sets.add(newSet);
	// return newSet;
	// }

	// public String getSymbol(int id) {
	//
	// String result = null;
	//
	// for (TreeLeafSymbol symbol : leafSymbols) {
	//
	// if (symbol.getID() == id) {
	// result = Character.toString(symbol.getSymbol());
	// }
	// }
	//
	// return result;
	// }

	// int xPosition = (id - 1) * X_STEP_SIZE;

	// /**
	// * determines the position of the new node in the tree and draws the node
	// * and the two edges to the child nodes
	// *
	// * @param firstNodeID
	// * the id of the first child in the queue
	// * @param secondNodeID
	// * the id of the second child in the queue
	// * @param freq
	// * the frequency of the new node in the tree
	// */
	// public void makeParentNode(int firstNodeID, int secondNodeID, int freq) {
	//
	// Node node1 = nodes.get(firstNodeID - 1);
	// Node node2 = nodes.get(secondNodeID - 1);
	//
	// Node leftNode = node1;
	// Node rightNode = node2;
	// if (node1.getxCoordinate() > node2.getxCoordinate()) {
	// rightNode = node1;
	// leftNode = node2;
	// }
	//
	// int layerFirstNode = node1.getLayer();
	// int layerSecondNode = node2.getLayer();
	// Node highestNode = layerFirstNode > layerSecondNode ? node1 : node2;
	//
	// Circle highestNodeCircle = highestNode.getCircle();
	// int highestLayer = highestNode.getLayer();
	// int xPosition = (node1.getxCoordinate() + node2.getxCoordinate()) / 2;
	//
	// int relativePosX = xPosition - highestNode.getxCoordinate();
	//
	// Node parent = drawNode(highestNodeCircle, highestLayer + 1,
	// xPosition, relativePosX, Integer.toString(freq));
	// parent.setLeftNode(leftNode);
	// parent.setRightNode(rightNode);
	// nodes.add(parent);
	//
	// leftNode.setParent(parent);
	// rightNode.setParent(parent);
	//
	// drawEdges(parent, leftNode, rightNode);
	// }

	// /**
	// * draws a new tree node
	// *
	// * @param highestChildCircle
	// * the Circle of the child node in the highest layer
	// * @param layer
	// * the layer of the new node
	// * @param xPosition
	// * the total x position of the new node
	// * @param relativePosX
	// * the x position of the new node relative to the child node in
	// * the highest layer
	// * @param frequency
	// * the frequency of the new node
	// * @return the new node
	// */
	// private Node drawNode(Circle highestChildCircle, int layer,
	// int xPosition, int relativePosX, String frequency) {
	//
	// Offset circleOffset = new Offset(relativePosX, -Y_STEP_SIZE,
	// highestChildCircle, AnimalScript.DIRECTION_C);
	// Circle node = lang.newCircle(circleOffset, 15, "node" + nodes.size(),
	// null, (CircleProperties) shannonFanoStyle
	// .getProperties(ShannonFanoStyle.CIRCLE));
	//
	// Offset nodeFreqOffset = new Offset(0, -8, node,
	// AnimalScript.DIRECTION_C);
	// Text nodeFreq = lang.newText(nodeFreqOffset, frequency,
	// "node" + nodes.size() + "Freq", null,
	// (TextProperties) shannonFanoStyle
	// .getProperties(ShannonFanoStyle.FREQUENCY));
	//
	// Offset nodeNoOffset = new Offset(0, -26, nodeFreq,
	// AnimalScript.DIRECTION_C);
	// Text nodeNumber = lang.newText(nodeNoOffset, Integer.toString(nodes
	// .size() + 1), "node" + (nodes.size() + 1) + "No", null,
	// (TextProperties) shannonFanoStyle
	// .getProperties(ShannonFanoStyle.NUMBER));
	//
	// return new Node(node, nodeNumber, nodeFreq, layer, xPosition,
	// nodes.size() + 1);
	// }

	// /**
	// * draws the 2 edges of a parent node to its children (including the 0s
	// and
	// * 1s on the egdes)
	// *
	// * @param parent
	// * the parent node
	// * @param leftNode
	// * the left child
	// * @param rightNode
	// * the right child
	// */
	// private void drawEdges(Node parent, Node leftNode,
	// Node rightNode) {
	//
	// Offset o1 = new Offset(-11, 11, parent.getCircle(),
	// AnimalScript.DIRECTION_C);
	// Offset o2 = new Offset(0, -15, leftNode.getCircle(),
	// AnimalScript.DIRECTION_C);
	// Polyline edge = lang.newPolyline(new Node[] { o1, o2 },
	// "edge" + nodes.size() + "to" + leftNode.getId(), null);
	//
	// int relativeXzero = (parent.getxCoordinate() - leftNode
	// .getxCoordinate());
	// int relativeYzero = (parent.getLayer() - leftNode.getLayer())
	// * -Y_STEP_SIZE;
	//
	// Text edgeText = lang.newText(new Offset((relativeXzero - 11) / 2 - 6,
	// (relativeYzero - 15 - 11) / 2, leftNode.getCircle(),
	// AnimalScript.DIRECTION_C), "0", "edgeText" + leftNode.getId(),
	// null);
	//
	// TreeEdge treeEdge = new TreeEdge(edge, edgeText);
	// leftNode.setUpwardEdge(treeEdge);
	// edges.add(treeEdge);
	//
	// o1 = new Offset(11, 11, parent.getCircle(), AnimalScript.DIRECTION_C);
	// o2 = new Offset(0, -15, rightNode.getCircle(), AnimalScript.DIRECTION_C);
	// edge = lang.newPolyline(new Node[] { o1, o2 }, "edge" + nodes.size()
	// + "to" + rightNode.getId(), null);
	//
	// int relativeXone = (parent.getxCoordinate() - rightNode
	// .getxCoordinate());
	// int relativeYone = (parent.getLayer() - rightNode.getLayer())
	// * -Y_STEP_SIZE;
	//
	// edgeText = lang.newText(new Offset((relativeXone + 11) / 2,
	// (relativeYone - 15 - 11) / 2 - 3, rightNode.getCircle(),
	// AnimalScript.DIRECTION_C), "1", "edgeText" + rightNode.getId(),
	// null);
	//
	// treeEdge = new TreeEdge(edge, edgeText);
	// rightNode.setUpwardEdge(treeEdge);
	// edges.add(treeEdge);
	// }

	/**
	 * Hides the whole tree
	 */
	public void hide() {
		for (AbstractNode node : nodes) {
			node.hide();
		}
	}

	/**
	 * Shows the whole tree
	 */
	public void show() {
		for (AbstractNode node : nodes) {
			node.show();
		}
	}

	/**
	 * Highlights the path from the root of the tree to the node with
	 * <code>leaveID</code>
	 * 
	 * @param leaveID
	 *            the end of the highlighted path from the root
	 */
	public void highlightPath(int leaveID) {
		markPath(leaveID, true);
	}

	/**
	 * Unhighlights the path from the root of the tree to the node with
	 * <code>leaveID</code>
	 * 
	 * @param leaveID
	 *            the end of the unhighlighted path from the root
	 */
	public void unHighlightPath(int leaveID) {
		markPath(leaveID, false);
	}

	private void markPath(int leaveID, boolean highlight) {

		AbstractNode leave = null;
		for (AbstractNode node : nodes) {
			if (node.getId() == leaveID) {
				leave = node;
				break;
			}
		}

		AbstractNode node = leave;
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

	public void moveBy(int dx, int dy, Timing duration) {

		for (AbstractNode node : nodes) {
			node.moveBy(dx, dy, duration);
		}

		// HashMap<Integer, Circle> alignDummies = new HashMap<Integer,
		// Circle>();
		//
		// for (Integer id : idToPosition.keySet()) {
		// int xPosition = (idToPosition.get(id) - 1) * X_STEP_SIZE;
		//
		// Offset offset = new Offset(xPosition + 15, treeHeight * Y_STEP_SIZE -
		// 1 + 65, offsetReference,
		// AnimalScript.DIRECTION_SW);
		//
		// TreeNode node = nodes.get(id - 1);
		// TreeNode parent = node.getParent();
		//
		// Circle alignDummy = node.moveTo(offset, lang, parent, duration);
		// alignDummies.put(id, alignDummy);
		//
		// leafSymbols.get(id - 1).moveTo(alignDummy, duration, lang);
		// }
		//
		// for (int i = 2; i <= treeHeight + 1; i++) {
		//
		// ArrayList<TreeNode> layerNodes = getNodesInLayer(i);
		//
		// for (TreeNode node : layerNodes) {
		//
		// TreeNode rightChild = node.getRightNode();
		//
		// int layerDifference = node.getLayer() - rightChild.getLayer();
		//
		// int relativePosY = layerDifference * Y_STEP_SIZE;
		// int relativePosX = node.getxCoordinate() -
		// rightChild.getxCoordinate();
		//
		// Offset circleOffset = new Offset(relativePosX, relativePosY,
		// alignDummies.get(rightChild.getID()),
		// AnimalScript.DIRECTION_C);
		//
		// TreeNode parent = node.getParent();
		//
		// Circle alignDummy = node.moveTo(circleOffset, lang, parent,
		// duration);
		// alignDummies.put(node.getID(), alignDummy);
		// }
		// }
	}

	// private ArrayList<Node> getNodesInLayer(int layer) {
	//
	// ArrayList<Node> layerNodes = new ArrayList<Node>();
	// for (Node node : nodes) {
	// if (node.getLayer() == layer) {
	// layerNodes.add(node);
	// }
	// }
	//
	// return layerNodes;
	// }
}
