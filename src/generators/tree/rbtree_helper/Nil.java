package generators.tree.rbtree_helper;

/**
 * Represents the Nil node of the tree.
 * Each tree has exactly one Nil node (static variable).
 * Each leaf of the tree has two Nil children.
 */
public class Nil extends Node {

	public Nil() {
		// Nil nodes are leafs thus do not have children and are always black 
		super(Tree.nil, Tree.nil, 0, false);
		super.setParent(Tree.nil); 	// does not work 
	}

	@Override
	public void setKey(int key) {

	}

	@Override
	public void setLeftChild(Node leftChild) {

	}

	@Override
	public void setRed(boolean isRed) {

	}
	
	@Override
	public int getSize() {
		return 0;
	}
}
