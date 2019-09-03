package generators.searching.helpers;


import java.util.StringTokenizer;

/**
 * <p>
 * Utility class for parsing a string representation of a tree into a java
 * object tree model represented by a custom node class. The parser can also be
 * used for validating the textual tree representation.
 * </p>
 * 
 * @see Node
 * 
 * @author Andrej Felde (andrej.felde@stud.tu-darmstadt.de)
 * @author Thomas Hesse (thomas.hesse@stud.tu-darmstadt.de)
 */
public class TreeParser {

  private StringTokenizer st;
  private int             nodeCount;
  private boolean         error;

  /**
   * <p>
   * Default constructor which initializes the variables for this class.
   * </p>
   */
  public TreeParser() {
    this.error = false;
    this.nodeCount = 0;
  }

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
   *          - The textual representation of a tree as pointed out in this
   *          documentation which will be parsed into a java object tree model
   *          represented by a custom node class.
   * @return root node of the tree
   * 
   * @see Node
   */
  public Node parseText(String tree) {
    this.validate(tree);
    st = new StringTokenizer(tree, "{} ", true);
    String n0 = st.nextToken();
    if (Character.isDigit(n0.charAt(0))) { // root is a leaf
      return new Node("n" + nodeCount, null, Integer.valueOf(n0));
    }
    Node root = new Node("n" + nodeCount, null);
    buildTree(root);
    return root;
  }

  /**
   * <p>
   * This method should be called after the method {@link #parseText(String)}.
   * This method will return always true (inidicates that there is an error) if
   * the {@link #parseText(String)} method was not called before calling this
   * method.
   * </p>
   * 
   * @return true if an error occurs and false if not
   */
  public boolean isValid() {
    return !error;
  }

  private void validate(String tree) {
    StringTokenizer st = new StringTokenizer(tree, "{} ", true);
    String token = st.nextToken();
    if (!Character.isLetter(token.charAt(0))) {
      error = true;
      return;
    }
    st = this.validateScope(st);
    if (st.hasMoreTokens()) {
      error = true;
    }
  }

  private StringTokenizer validateScope(StringTokenizer st) {
    StringTokenizer st2 = st;
    String token = st2.nextToken();
    while (token.equals(" ")) { // skip spaces
      token = st2.nextToken();
    }
    if (!token.equals("{")) { // Non-optional {
      error = true;
    }
    token = st2.nextToken();
    while (token.equals(" ")) { // skip spaces
      token = st2.nextToken();
    }
    // there must be at least one digit or letter in the scope
    if (!(Character.isLetter(token.charAt(0)) || Character.isDigit(token
        .charAt(0)))) {
      error = true;
    }
    while (!token.equals("}")) {
      // System.out.println(token);
      if (Character.isLetter(token.charAt(0))) {
        st2 = this.validateScope(st2);
      } else if (!(Character.isDigit(token.charAt(0)) || token.equals(" "))) {
        error = true;
        break;
      }
      if (!st2.hasMoreTokens()) {
        error = true;
        break;
      }
      token = st2.nextToken();
    }
    return st2;
  }

  private void buildTree(Node parent) {
    String token;
    Node node = parent;
    while (st.hasMoreTokens()) {
      token = st.nextToken();
      if (Character.isLetter(token.charAt(0))) {
        nodeCount++;
        node = new Node("n" + nodeCount, parent);
      } else if (token.equals("{")) {
        buildTree(node);
      } else if (Character.isDigit(token.charAt(0))) {
        nodeCount++;
        new Node("n" + nodeCount, parent, Integer.valueOf(token));
      } else if (token.equals("}")) {
        return;
      }
    }
  }
}