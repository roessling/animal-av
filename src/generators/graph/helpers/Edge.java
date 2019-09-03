package generators.graph.helpers;

/**
 * Encapsulates the functionality for an edge of a graph.
 * 
 * @author chollubetz
 * 
 */
public class Edge implements Comparable<Edge> {
  Node fromNode;
  Node toNode;
  int  weight;

  public Edge(Node fromNode, Node toNode, int weight) {
    this.fromNode = fromNode;
    this.toNode = toNode;
    this.weight = weight;
  }

  /**
   * Returns the source node.
   * 
   * @return the source node
   */
  public Node getFromNode() {
    return fromNode;
  }

  /**
   * Returns the destination node.
   * 
   * @return the destination node
   */
  public Node getToNode() {
    return toNode;
  }

  /**
   * Returns the weight of the edge.
   * 
   * @return the weight of the edge
   */
  public int getWeight() {
    return weight;
  }

  public boolean equals(Edge edge) {
    return fromNode.equals(edge.getFromNode())
        && toNode.equals(edge.getToNode());
  }

  @Override
  public int compareTo(Edge o) {
    return fromNode.compareTo(o.getFromNode()) + 5
        * toNode.compareTo(o.getToNode());
  }

  public String toString() {
    return "{" + fromNode + "," + toNode + "," + weight + "}";
  }
}
