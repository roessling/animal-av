package generators.graph.helpers;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;

/**
 * Encapsulates the functionality for adjacency matrix-like outputs in animal.
 * 
 * @author chollubetz
 * 
 */
public class AdjacencyMatrix {

  private DirectedGraph dg;
  private Language      lang;
  private Position      position;
  private Color         highlightColor;

  private Text[]        legend;
  private Text[][]      labels;

  List<Node>            nodes;

  public AdjacencyMatrix(DirectedGraph dg, Node firstNode, Language lang,
      Position position, Color highlightColor) {
    this.dg = dg;
    this.lang = lang;
    this.position = position;
    this.highlightColor = highlightColor;

    nodes = new LinkedList<Node>(dg.getNodes());
  }

  /**
   * Determines the position of the two nodes in the adjacency matrix.
   * 
   * @param first
   *          the first node
   * @param second
   *          the second node
   * @return the position of the nodes in the adjacency matrix
   */
  public Pair<Integer, Integer> positionOf(Node first, Node second) {
    return new Pair<Integer, Integer>(positionOf(first), positionOf(second));
  }

  /**
   * Determines the position of the node in the adjacency matrix.
   * 
   * @param node
   *          the node
   * @return the position of the node in the adjacency matrix
   */
  public Integer positionOf(Node node) {
    for (int i = 0; i < nodes.size(); i++)
      if (nodes.get(i).equals(node))
        return i;
    return -1;
  }

  /**
   * Draws the adjacency matrix.
   */
  public void draw() {
    this.labels = new Text[nodes.size()][nodes.size()];
    this.legend = new Text[nodes.size() * 2];

    int[][] weights = new int[nodes.size()][nodes.size()];

    for (Node currentFromNode : nodes)
      for (Pair<Node, Integer> currentTo : dg
          .getOutgoingNeighbors(currentFromNode)) {
        Node currentToNode = currentTo.getFirst();
        weights[positionOf(currentFromNode)][positionOf(currentToNode)] = currentTo
            .getSecond();
      }

    for (int i = 0; i < nodes.size(); i++) {
      legend[i] = lang.newText(new Coordinates(position.getX() + (i + 1) * 30,
          position.getY()), "v" + i, "", null);
      legend[nodes.size() + i] = lang.newText(new Coordinates(position.getX(),
          position.getY() + (i + 1) * 20), "v" + i, "", null);
    }

    for (int r = 0; r < weights.length; r++)
      for (int c = 0; c < weights[0].length; c++)
        labels[r][c] = lang
            .newText(
                new Coordinates(position.getX() + (c + 1) * 30, position.getY()
                    + (r + 1) * 20), "" + weights[r][c], "", null);
  }

  /**
   * Highlights the weight of the edge between the two given nodes.
   * 
   * @param fromeNode
   *          the node of the source
   * @param toNode
   *          the node of the destination
   */
  public void highlight(Node fromeNode, Node toNode) {
    labels[positionOf(fromeNode)][positionOf(toNode)].changeColor("color",
        highlightColor, null, null);
  }

  /**
   * Highlights the name of the node on the source side.
   * 
   * @param fromeNode
   *          the node of the source
   */
  public void highlightFromNode(Node fromeNode) {
    legend[positionOf(fromeNode) + nodes.size()].changeColor("color",
        highlightColor, null, null);
  }

  /**
   * Highlights the name of the node on the destination side.
   * 
   * @param toNode
   *          the node of the destination
   */
  public void highlightToNode(Node toNode) {
    legend[positionOf(toNode)].changeColor("color", highlightColor, null, null);
  }

  /**
   * Unhighlights all highlighted items.
   */
  public void unHighlightAll() {
    for (int r = 0; r < labels.length; r++)
      for (int c = 0; c < labels[0].length; c++)
        labels[r][c].changeColor("color", Color.BLACK, null, null);
    for (int i = 0; i < nodes.size(); i++) {
      legend[i].changeColor("color", Color.BLACK, null, null);
      legend[nodes.size() + i].changeColor("color", Color.BLACK, null, null);
    }
  }

  /**
   * Hides the adjacency matrix.
   */
  public void hide() {
    for (int r = 0; r < labels.length; r++)
      for (int c = 0; c < labels[0].length; c++)
        labels[r][c].hide();
    for (int i = 0; i < legend.length; i++)
      legend[i].hide();
  }

  /**
   * Returns the South-West position of the adjacency matrix.
   * 
   * @return the South-West position of the adjacency matrix
   */
  public Position getSW() {
    return new Position(position.getX(), position.getY() + (nodes.size() + 1)
        * 20);
  }
}
