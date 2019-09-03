package generators.compression.shannon_fano.guielements.tree;

import algoanim.primitives.Rect;
import algoanim.util.Timing;

public abstract class AbstractNode {

	/**
	 * Positioning
	 */
	protected Rect boundingRect;
	protected int centerX;
	protected int centerY;

	protected Tree tree;

	/**
	 * The id of the node
	 */
	protected int id;

	/**
	 * The frequency count
	 */
	protected int frequency;

	/**
	 * The probability
	 */
	protected float probability;

	/**
	 * Stores whether the node is currently visible or not
	 */
	protected boolean isVisible = true;

	/**
	 * Child- and parent-node(s)
	 */
	protected AbstractNode leftNode;
	protected AbstractNode rightNode;
	protected AbstractNode parent;

	/**
	 * The edge that leads to this node
	 */
	protected TreeEdge upwardEdge;

	/**
	 * The edges that lead to the left / right child
	 */
	protected TreeEdge leftChildEdge;
	protected TreeEdge rightChildEdge;

	public int getId() {
		return id;
	}

	public int getFrequency() {
		return frequency;
	}

	public float getProbability() {
		return probability;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public Rect getBoundingRect() {
		return boundingRect;
	}

	public AbstractNode getLeftNode() {
		return leftNode;
	}

	public AbstractNode getRightNode() {
		return rightNode;
	}
	
	public void setLeftNode(AbstractNode leftNode, String edgeLabel) {
		setLeftNode(leftNode, edgeLabel, 0);
	}

	public void setLeftNode(AbstractNode leftNode, String edgeLabel, int shiftY) {
		this.leftNode = leftNode;
		if (leftChildEdge != null) {
			leftChildEdge.hide();
		}
		if (leftNode != null) {
			leftNode.setParent(this);
			leftChildEdge = new TreeEdge(this, leftNode, edgeLabel, shiftY, tree);
			leftNode.setUpwardEdge(leftChildEdge);
		}
	}
	
	public void setRightNode(AbstractNode rightNode, String edgeLabel) {
		setRightNode(rightNode, edgeLabel, 0);
	}

	public void setRightNode(AbstractNode rightNode, String edgeLabel, int shiftY) {
		this.rightNode = rightNode;
		if (rightChildEdge != null) {
			rightChildEdge.hide();
		}
		if (rightNode != null) {
			rightNode.setParent(this);
			rightChildEdge = new TreeEdge(this, rightNode, edgeLabel, shiftY, tree);
			rightNode.setUpwardEdge(rightChildEdge);
		}
	}

	public AbstractNode getParent() {
		return parent;
	}

	public void setParent(AbstractNode parent) {
		this.parent = parent;
	}

	public TreeEdge getUpwardEdge() {
		return upwardEdge;
	}

	public void setUpwardEdge(TreeEdge upwardEdge) {
		this.upwardEdge = upwardEdge;
	}

	public TreeEdge getLeftChildEdge() {
		return leftChildEdge;
	}

	public void setLeftChildEdge(TreeEdge leftChildEdge) {
		this.leftChildEdge = leftChildEdge;
	}

	public TreeEdge getRightChildEdge() {
		return rightChildEdge;
	}

	public void setRightChildEdge(TreeEdge rightChildEdge) {
		this.rightChildEdge = rightChildEdge;
	}

	public AbstractNode(int id, Tree tree) {
		this.id = id;
		this.tree = tree;
	}

	/**
	 * Creates primitives for the new node.
	 */
	abstract protected void createPrimitives();

	/**
	 * Creates a bounding rectangle for the node.
	 */
	abstract protected void createBoundingRect();

	/**
	 * Moves the primitives of the node.
	 * 
	 * @param dx
	 *            The x-delta in pixels
	 * @param dy
	 *            The y-delta in pixels
	 * @param duration
	 *            The duration of the animation
	 */
	public void moveBy(int dx, int dy, Timing duration) {
		centerX += dx;
		centerY += dy;
		boundingRect.moveBy(null, dx, dy, null, duration);
		if (rightChildEdge != null) {
			rightChildEdge.moveBy(dx, dy, duration);
		}
		if (leftChildEdge != null) {
			leftChildEdge.moveBy(dx, dy, duration);
		}
	}

	/**
	 * Hides all primitives and child edges of this node
	 */
	abstract public void hide();

	/**
	 * Hides all primitives and child edges of the node delayed by the given
	 * duration.
	 * 
	 * @param delay
	 *            The delay in ms
	 */
	abstract public void hide(int delay);

	/**
	 * Shows all primitives and child edges of the node
	 */
	abstract public void show();

	/**
	 * Shows all primitives and child edges of the node delayed by the given
	 * duration.
	 * 
	 * @param delay
	 *            The delay in ms
	 */
	abstract public void show(int delay);

}
