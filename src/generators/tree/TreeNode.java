package generators.tree;
import java.util.HashSet;
import java.util.Set;

public class TreeNode {

	private int nodeID;
	private Set<String> label;
	private TreeNode left;
	private TreeNode right;

	// constructor for a leaf
	public TreeNode(int nodeID, String label) {
		this(nodeID, label, null, null);
	}

	// constructor for a tree
	public TreeNode(int nodeID, String label, TreeNode left, TreeNode right) {
		this.nodeID = nodeID;
		this.label = new HashSet<String>();

		if (!label.equals("")) {
			this.label.add(label);
		}
		this.left = left;
		this.right = right;
	}

	public TreeNode getLeft() {
		return this.left;
	}

	public TreeNode getRight() {
		return this.right;
	}

	public Set<String> getLabel() {
		return this.label;
	}
	
	public int getNodeID() {
		return this.nodeID;
	}
	
	public void setNewLabel(String label) {
		this.getLabel().clear();
		this.getLabel().add(label);
	}

	// choose randomly a label and set it
	public void chooseOneLabelRandomly() {
		String tmp = null;
		for (String label : this.getLabel()) {
			tmp = label;
			break;
		}
		this.getLabel().clear();
		this.getLabel().add(tmp);
	}
}
