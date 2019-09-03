package generators.compression.tunstall.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Node.
 */
public class TreeNode {

	/** The label. */
	public String label;

	/** The frequency. */
	public double frequency;

	/** The right child. */
	public TreeNode parent;

	/** The left child. */
	public TreeNode leftChild;

	/** The right child. */
	public TreeNode rightChild;

	public List<TreeNode> children;

	/**
	 * Instantiates a new node.
	 *
	 * @param label
	 *            the label
	 * @param frequency
	 *            the frequency
	 * @param parent
	 *            the parent
	 * @param leftChild
	 *            the left child
	 * @param rightChild
	 *            the right child
	 */
	public TreeNode(String label, double frequency, TreeNode parent, TreeNode leftChild, TreeNode rightChild) {
		this.label = label;
		this.frequency = frequency;
		this.parent = parent;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	public TreeNode(String label, double frequency, TreeNode parent, List<TreeNode> children) {
		this.label = label;
		this.frequency = frequency;
		this.parent = parent;
		if (children == null) {
			this.children = new ArrayList<>();
		} else {
			this.children = children;
		}
	}

	/**
	 * Checks if is leaf.
	 *
	 * @return true, if is leaf
	 */
	public boolean isLeaf() {
		if (children == null) {
			return (leftChild == null) && (rightChild == null);
		} else {
			return children.isEmpty();
		}
	}

	public static int getTreeHeigth(TreeNode node) {
		if (node.children != null) {
			int heigth = 0;
			for (TreeNode child : node.children) {
				heigth = Math.max(heigth, getTreeHeigth(child));
			}
			return heigth + 1;
		} else {
			int heigth = 0;
			if (node.rightChild != null) {
				heigth = getTreeHeigth(node.rightChild);
			}
			if (node.leftChild != null) {
				heigth = Math.max(heigth, getTreeHeigth(node.leftChild));
			}
			return heigth + 1;
		}
	}

	public static int countNodes(TreeNode node) {
		int count = 0;
		if (node.children != null) {
			for (TreeNode child : node.children) {
				count += countNodes(child);
			}
		} else {
			if (node.rightChild != null) {
				count += countNodes(node.rightChild);
			}
			if (node.leftChild != null) {
				count += countNodes(node.leftChild);
			}
		}
		return count + 1;
	}

	private static void getAllNodesFromTreeInternal(TreeNode node, List<TreeNode> nodes) {
		nodes.add(node);
		if (node.children != null) {
			for (TreeNode child : node.children) {
				getAllNodesFromTreeInternal(child, nodes);
			}
		} else {
			if (node.rightChild != null) {
				getAllNodesFromTreeInternal(node.rightChild, nodes);
			}
			if (node.leftChild != null) {
				getAllNodesFromTreeInternal(node.leftChild, nodes);
			}
		}
	}

	public static List<TreeNode> getAllNodesFromTree(TreeNode node) {
		List<TreeNode> nodes = new ArrayList<>();
		getAllNodesFromTreeInternal(node, nodes);
		return nodes;
	}

	public static int countLeafs(TreeNode tree) {
		List<TreeNode> nodes = getAllNodesFromTree(tree);
		int count = 0;
		for (TreeNode node : nodes) {
			if (node.isLeaf()) {
				count++;
			}
		}
		return count;
	}
}
