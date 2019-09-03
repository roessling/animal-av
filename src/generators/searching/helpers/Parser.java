package generators.searching.helpers;

import java.util.StringTokenizer;


/**
 * <p>
 * Utility class for parsing a string representation of a tree into a java
 * object tree model.
 * </p>
 * 
 * @see generators.searching.helpers.Node
 * 
 */
public class Parser {

  private StringTokenizer st;

  private int             nodeCount         = 0;

  private int             outerPaddingCount = 0;

  private boolean         wasLeaf           = false;

  /**
   * <p>
   * This method receives a tree in text form which could look like, i.e.
   * 
   * <pre>
   * A {B {1 2} C {3 4}}
   * </pre>
   * 
   * And as a tree this will look as the following:
   * 
   * <pre>
   *        A
   *       / \
   *      /   \
   *     B     C
   *    / \   / \
   *   1   2 3   4
   * </pre>
   * 
   * The root node of the tree is returned afterwards.
   * </p>
   * 
   * @param tree
   * @return root node of the tree
   */
  public Node parseText(String tree) {
    st = new StringTokenizer(tree, "{} ", true);
    String n0 = st.nextToken();
    // root is a leaf
    if (n0.matches("-?\\d+")) {

      return new Node("n" + nodeCount, null, Integer.valueOf(n0));
    }

    Node root = new Node("n" + nodeCount, null);
    buildTree(root);
    return root;
  }

  private void buildTree(Node root) {
    String token;
    Node node = root;

    while (st.hasMoreTokens()) {
      token = st.nextToken();
      if (Character.isLetter(token.charAt(0))) {
        nodeCount++;
        node = new Node("n" + nodeCount, root);
        wasLeaf = false;
      } else if (token.equals("{")) {
        buildTree(node);
      } else if (token.matches("-?\\d+")) {

        nodeCount++;
        wasLeaf = true;
        new Node("n" + nodeCount, root, Integer.valueOf(token));
      } else if (token.equals("}")) {
        if (wasLeaf) {
          outerPaddingCount++;
        }
        return;
      }
    }
  }

  public int getOuterPaddingCount() {
    return outerPaddingCount;
  }
}