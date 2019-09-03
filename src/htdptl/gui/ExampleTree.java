package htdptl.gui;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Example tree built out of DefaultMutableTreeNodes. 1999 Marty Hall,
 * http://www.apl.jhu.edu/~hall/java/
 */

public class ExampleTree extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7060070044642678593L;

	public static JTree create(Object[] hierarchy) {		
		DefaultMutableTreeNode root = processHierarchy(hierarchy);
		JTree tree = new JTree(root);
		return tree;
	}

	private static DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
		DefaultMutableTreeNode child;
		for (int i = 1; i < hierarchy.length; i++) {
			Object nodeSpecifier = hierarchy[i];
			if (nodeSpecifier instanceof Object[]) 
				child = processHierarchy((Object[]) nodeSpecifier);
			else {
				child = new DefaultMutableTreeNode(nodeSpecifier); 
			}
			node.add(child);
		}
		return (node);
	}
}
