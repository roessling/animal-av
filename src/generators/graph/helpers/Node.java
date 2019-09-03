package generators.graph.helpers;

/**
 * Encapsulates the functionality of a node of a graph.
 * 
 * @author chollubetz
 * 
 */
public class Node implements Comparable<Node> {
  String label;

  public Node(String label) {
    this.label = label;
  }

  /**
   * Returns the label of the node.
   * 
   * @return the label of the node
   */
  public String getLabel() {
    return label;
  }

  public boolean equals(Node node) {
    return label.equals(node.getLabel());
  }

  @Override
  public int compareTo(Node o) {
    return label.compareTo(((Node) o).getLabel());
  }

  public String toString() {
    return label;
  }
}
