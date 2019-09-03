package generators.compression.huffman.pregeneration;

public class PreGenTreeNode {

	private PreGenTreeNode parent;
	private PreGenTreeNode leftNode;
	private PreGenTreeNode rightNode;
	
	private int frequency;

	private int id;

	public PreGenTreeNode(int id, int frequency, PreGenTreeNode parent,
			PreGenTreeNode leftNode, PreGenTreeNode rightNode) {

		this.parent = parent;
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.frequency = frequency;
		this.id = id;
	}

	public PreGenTreeNode getParent() {
		return parent;
	}

	public void setParent(PreGenTreeNode parent) {
		this.parent = parent;
	}

	public PreGenTreeNode getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(PreGenTreeNode leftNode) {
		this.leftNode = leftNode;
	}

	public PreGenTreeNode getRightNode() {
		return rightNode;
	}

	public void setRightNode(PreGenTreeNode rightNode) {
		this.rightNode = rightNode;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
