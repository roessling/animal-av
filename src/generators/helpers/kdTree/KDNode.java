package generators.helpers.kdTree;

import algoanim.util.Coordinates;
import algoanim.util.Node;

/**
 * @author mateusz
 */
public class KDNode {

  private static int nodeCount   = 0;
  private static int depthOfTree = 0;

  int                id;
  int                level;

  int                xValue;
  int                yValue;

  KDNode             pred;
  KDNode             left;
  KDNode             right;

  private KDNode(int id, KDNode pred, int level, int xValue, int yValue) {
    super();
    this.id = id;
    this.pred = pred;
    this.level = level;
    this.xValue = xValue;
    this.yValue = yValue;
  }

  /**
   * Static constructor method. Creates a root node.
   * 
   * @param xValue
   * @param yValue
   * @return a root node for the kD-tree with given values.
   */
  public static KDNode createRoot(int xValue, int yValue) {
    nodeCount = 0;
    return new KDNode(nodeCount++, null, 0, xValue, yValue);
  }

  /**
   * @return the left
   */
  public KDNode getLeft() {
    return this.left;
  }

  /**
   * @return the right
   */
  public KDNode getRight() {
    return this.right;
  }

  public int getxValue() {
    return this.xValue;
  }

  public int getyValue() {
    return this.yValue;
  }

  public int getNodeCount() {
    return nodeCount;
  }

  /**
   * Constructor method. Creates a left child with given values.
   * 
   * @param xValue
   *          the xValue
   * @param yValue
   *          the yValue
   */
  public void createLeftChild(int xValue, int yValue) {
    this.left = new KDNode(nodeCount++, this, incLevel(), xValue, yValue);
  }

  /**
   * Constructor method. Creates a right child with given values.
   * 
   * @param xValue
   *          the xValue
   * @param yValue
   *          the yValue
   */
  public void createRightChild(int xValue, int yValue) {
    this.right = new KDNode(nodeCount++, this, incLevel(), xValue, yValue);
  }

  /**
   * @return
   */
  private int incLevel() {
    if ((this.level + 1) > depthOfTree)
      depthOfTree++;
    return this.level + 1;
  }

  public Node[] getCoordinates(Coordinates coordinates) {
    Coordinates[] result = new Coordinates[nodeCount];
    coordinates.getX();

    for (int i = 0; i < nodeCount; i++) {

    }
    return result;
  }

  public int getDepthOfTree() {
    return depthOfTree;
  }

  public boolean isXplane() {
    return (this.level % 2 == 0);
  }

  public double distanceToCoordinates(int x, int y) {
    int x2 = x, y2 = y;
    x2 -= this.xValue;
    y2 -= this.yValue;
    return Math.sqrt(x2 * x2 + y2 * y2);
  }

  /**
   * Returns the sibling of this node.
   * 
   * @return the KDNode sibling
   */
  public KDNode getSibling() {
    if (this.pred == null)
      throw new IllegalStateException();
    if (this == this.pred.left)
      return this.pred.right;
    else
      return this.pred.left;
  }

  public double distanceInPlane(int x, int y) {
    if (isXplane())
      return Math.abs(this.xValue - x);
    else
      return Math.abs(this.yValue - y);
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "(" + this.xValue + ", " + this.yValue + ")";
  }
}