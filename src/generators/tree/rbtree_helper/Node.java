package generators.tree.rbtree_helper;

import generators.graphics.helpers.Coordinate;

/**
 * Represents a node of the Red-Black Tree (Class Tree.java)
 *
 */
public class Node {

	private Node leftChild;
	private Node rightChild;
	private Node parent;
	private boolean isRed;
	private int key;
	private int id = -1;
	private int layer;
	private int positionInLayer;
	private static int identifier = 0;
	private Coordinate position;

	public Node(Node lChild, Node rChild, int key, boolean isRed) {
		this.setLeftChild(lChild);
		this.setRightChild(rChild);
		this.setKey(key);
		this.setRed(isRed);
		if (!(this instanceof Nil)) {
			this.setId();
		}
		setPosition(new Coordinate(0, 0));
	}
	private void setId() {
		this.id = identifier;
		identifier = identifier + 1;
	}
	
	public int getId() {
		return id;
	}


	public Node getLeftChild() {
		return leftChild;
	}
	
	/**
	 * Returns the sibling of the current node
	 * @return The sibling as Node
	 */
	public Node getSibling() {
		if (this instanceof Nil) {
			return Tree.nil;
		}
		Node tempNode = null;
		if (this.getParent().getLeftChild() == this) {
			tempNode = this.getParent().getRightChild();
		} 
		if (this.getParent().getRightChild() == this) {
			tempNode = this.getParent().getLeftChild();
		}
		
		if (tempNode == null) {
			return Tree.nil;
		}
		return tempNode;
		
	}

	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	public Node getRightChild() {
		return rightChild;
	}

	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

	public boolean isRed() {
		return isRed;
	}

	public void setRed(boolean isRed) {
		this.isRed = isRed;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getSize() {
		//return size;
		return (getLeftChild().getSize() + getRightChild().getSize() + 1);
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getPositionInLayer() {
		return positionInLayer;
	}

	public void setPositionInLayer(int positionInLayer) {
		this.positionInLayer = positionInLayer;
	}

	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}
	
	@Override
	public String toString() {
		if (this instanceof Nil) {
			return "NIL";
		}
		return key + "|" + getSize();
		
	}

	public static void resetIDCounter() {
		identifier = 0;
	}

}
