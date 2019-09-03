package generators.compression.huffman.guielements.tree;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * Small helper class for node positioning in the tree. The class holds the
 * layer of the node (bottom is layer 1), the xCoordinate of the node and the
 * circle primitive of the node.
 * 
 */
public class TreeNode {

	private Circle circle;
	private Text nodeNumber;
	private Text nodeFreq;
	private int layer;
	private int xCoordinate;
	private int id;

	private TreeNode leftNode;
	private TreeNode rightNode;
	private TreeNode parent;

	private TreeEdge upwardEdge;

	public int getID() {
		return id;
	}

	public int getLayer() {
		return layer;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public Circle getCircle() {
		return circle;
	}

	public TreeNode getLeftNode() {
		return leftNode;
	}

	public TreeNode getRightNode() {
		return rightNode;
	}

	public void setLeftNode(TreeNode leftNode) {
		this.leftNode = leftNode;
	}

	public void setRightNode(TreeNode rightNode) {
		this.rightNode = rightNode;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public TreeEdge getUpwardEdge() {
		return upwardEdge;
	}

	public void setUpwardEdge(TreeEdge upwardEdge) {
		this.upwardEdge = upwardEdge;
	}

	public TreeNode(Circle circle, Text nodeNumber, Text nodeFreq, int layer,
			int xCoordinate, int id) {
		this.circle = circle;
		this.nodeNumber = nodeNumber;
		this.nodeFreq = nodeFreq;
		this.layer = layer;
		this.xCoordinate = xCoordinate;
		this.id = id;
	}

	public void hide() {
		circle.hide();
		nodeNumber.hide();
		nodeFreq.hide();
	}

	public void show() {
		circle.show();
		nodeNumber.show();
		nodeFreq.show();
	}

	public Circle moveTo(Offset offset, Language lang, TreeNode parent, Timing duration) {

		Circle alignDummy = lang.newCircle(offset, 15, "alignDummy", null);
		alignDummy.hide();

		Offset circleOffset = new Offset(offset.getX() - 15,
				offset.getY() - 15, offset.getRef(), offset.getDirection());
		circle.moveTo(null, null, circleOffset, null, duration);
		
		String freq = nodeFreq.getText();
		int centerCorrection = freq.length() * 5;
		Offset nodeFreqOffset = new Offset(-centerCorrection, -8, alignDummy,
				AnimalScript.DIRECTION_C);
		nodeFreq.moveTo(null, null, nodeFreqOffset, null, duration);

		String number = nodeNumber.getText();
		centerCorrection = number.length() * 2;
		Offset nodeNoOffset = new Offset(-centerCorrection, -14, alignDummy,
				AnimalScript.DIRECTION_C);

		nodeNumber.moveTo(null, null, nodeNoOffset, null, duration);

		if (upwardEdge != null) {
			upwardEdge.moveTo(alignDummy, this, parent, lang, duration);
		}

		return alignDummy;
	}
}
