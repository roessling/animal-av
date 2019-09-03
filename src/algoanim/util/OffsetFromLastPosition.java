package algoanim.util;

/**
 * This is a concrete kind of a <code>Node</code>. The coordinates are
 * measured as a displacement from the last node used>.
 * For example, to place an element 20 pixels to the right and 10 pixels
 * of the last coordinate referenced, you would use the following:
 * <code>Node n = new OffsetFromLastPosition(20, 10);</code>
 * @author Guido Roessling
 * @version 0.7 20110311
 */
public class OffsetFromLastPosition extends Node {
  /**
   * The x-axis offset.
   */
  private int x = 0;

  /**
   * The y-axis offset.
   */
  private int y = 0;

  /**
   * Creates a new Offset instance at a distance of (xCoordinate, yCoordinate)
   * in direction "targetDirection" from the base primitive "reference".
   * @param dx
   *          the x-axis offset.
   * @param dy
   *          the y-axis offset.
   */
  public OffsetFromLastPosition(int dx, int dy) {
    x = dx;
    y = dy;
  }
  
  /**
   * Returns the x-axis offset.
   * 
   * @return the x-axis offset.
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the y-axis offset.
   * 
   * @return the y-axis offset.
   */
  public int getY() {
    return y;
  }
}
