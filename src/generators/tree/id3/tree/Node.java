package generators.tree.id3.tree;

public class Node {

	private String label;

	private boolean isLeaf;



	public Node(String label, boolean isLeaf) {
		this.label = label;
		this.isLeaf = isLeaf;
	}


	public String getLabel() {
		return label;
	}


	public boolean isLeaf() {
		return isLeaf;
	}


	@Override
	public String toString() {
		return label;
	}

}
