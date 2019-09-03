package generators.graphics.helpers;

import java.util.ArrayList;
import java.util.List;

public class Coordinate {
  private int x;
  private int y;

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public boolean equals(Object o) {
    Coordinate c = (Coordinate) o;
    if (c == null)
      return false;
    return x == c.getX() && y == c.getY();
  }

  public String toString() {
    return "(" + x + "," + y + ")";
  }

  public List<Coordinate> neighbors(int maxX, int maxY) {
    ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();
    if (x > 0 && y > 0)
      neighbors.add(new Coordinate(x - 1, y - 1));
    if (y > 0)
      neighbors.add(new Coordinate(x, y - 1));
    if (y > 0 && x < maxX - 1)
      neighbors.add(new Coordinate(x + 1, y - 1));
    if (x < maxX - 1)
      neighbors.add(new Coordinate(x + 1, y));
    if (x < maxX - 1 && y < maxY - 1)
      neighbors.add(new Coordinate(x + 1, y + 1));
    if (y < maxY - 1)
      neighbors.add(new Coordinate(x, y + 1));
    if (x > 0 && y < maxY - 1)
      neighbors.add(new Coordinate(x - 1, y + 1));
    if (x > 0)
      neighbors.add(new Coordinate(x - 1, y));
    return neighbors;
  }

  public int hashCode() {
    return x * 1000000 + y;
  }
}