package generators.graph.helpers;

import algoanim.util.Coordinates;

public class MyNode {

  private Coordinates coordinates;
  private String      name;
  private boolean     visited = false;

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isVisited() {
    return visited;
  }

  public void setVisited(boolean visited) {
    this.visited = visited;
  }

  public MyNode(String pName, Coordinates pCoord) {
    name = pName;
    coordinates = pCoord;
    coordinates = pCoord;
    coordinates = pCoord;
  }

  public MyNode() {

  }

}