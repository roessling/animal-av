package generators.graph.helpers;

/**
 * Encapsulates the position in a drawable area.
 * 
 * @author chollubetz
 * 
 */
public class Position {
  int x, y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Return the x coordinate.
   * 
   * @return the x coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the y coordinate.
   * 
   * @return the y coordinate
   */
  public int getY() {
    return y;
  }
}
