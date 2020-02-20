package generators.tree;

import java.util.HashSet;
import java.util.Set;

public class TreeNodeFitch {

	private int nodeID;
	private Set<String> label;
	private TreeNodeFitch left;
	private TreeNodeFitch right;

	// constructor for a leaf
	public TreeNodeFitch(int nodeID, String label) {
		this(nodeID, label, null, null);
	}

	// constructor for a tree
	public TreeNodeFitch(int nodeID, String label, 
			TreeNodeFitch left, TreeNodeFitch right) {
		this.nodeID = nodeID;
		this.label = new HashSet<String>();

		if (!label.equals("")) {
			this.label.add(label);
		}
		this.left = left;
		this.right = right;
	}

	public TreeNodeFitch getLeft() {
		return this.left;
	}

	public TreeNodeFitch getRight() {
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
