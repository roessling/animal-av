package generators.framework;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class GeneratorNode extends DefaultMutableTreeNode {
  public GeneratorNode(String content) {
    super(content);
  }
  @SuppressWarnings("unchecked")
  public void add(MutableTreeNode newChild) {
    if (!allowsChildren) {
      throw new IllegalStateException("node does not allow children");
    } else if (newChild == null) {
      throw new IllegalArgumentException("new child is null");
    } else if (isNodeAncestor(newChild)) {
      throw new IllegalArgumentException("new child is an ancestor");
    }
    MutableTreeNode oldParent = (MutableTreeNode) newChild.getParent();

    if (oldParent != null) {
      oldParent.remove(newChild);
    }
    newChild.setParent(this);
    if (children == null) {
      children = new Vector<TreeNode>();
    }    
    int targetIndex = 0, nrChildren = children.size();
    boolean found = nrChildren == 0;
    String myName = newChild.toString();
    /*
    System.err.println("Inserting '" + myName +"': ");
    if (found)
      System.err.println("\tno children in parent " + oldParent +", insert @0");
    */
    for (int i = 0; i < nrChildren && !found; i++) {
      Object child = children.elementAt(i);
      found = (child == null);
      String compareTo = child.toString();
      //      System.err.print("\t< " +compareTo + "?" + 
      //          (compareTo.compareToIgnoreCase(myName) > 0));
      if (found = (found || (compareTo.compareToIgnoreCase(myName) > 0))) {
        targetIndex = i;
	//      System.err.println(" - " + found);
      }
    }
    if (!found)
      targetIndex = nrChildren;
    //    System.err.println("\tinserting at position " + targetIndex);
    children.insertElementAt(newChild, targetIndex);
    /*
    System.err.println("\t\torder now: ");
    for (Object o : children)
      System.err.println("\t\t\t" +o.toString());
    */
  }
}
