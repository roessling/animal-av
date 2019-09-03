package generators.helpers.kdTree;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;

import algoanim.animalscript.AnimalGraphGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * @author mateusz
 */
public class Graph {

  private Coordinates rootCoordinates = new Coordinates(500, 100);

  private class GraphData {

    int[][]  adjacencyMatrix;
    String[] labels;
    Node[]   nodes;
    int[]    levels;

    GraphData(int nodeCount) {
      this.adjacencyMatrix = new int[nodeCount][nodeCount];
      this.labels = new String[nodeCount];
      this.nodes = new Node[nodeCount];
      this.levels = new int[nodeCount];
    }

  }

  private Language                     lang;
  private AnimalGraphGenerator         animalGraphGenerator;
  private String                       name            = "graph";
  @SuppressWarnings("unused")
  private int[][]                      adjacencyMatrix;
  private Node[]                       nodes;
  @SuppressWarnings("unused")
  private String[]                     labels;
  private DisplayOptions               displayOptions;
  private GraphProperties              graphProperties = new GraphProperties();
  private algoanim.primitives.Graph    graph;
  private int                          graphCounter;
  private VisualCircle                 circleInsertion;
  private VisualCircle                 circleBest;
  private AnimationPropertiesContainer animProps;
  private VisualCircle                 circleSubtreeBest;

  public Graph(Language lang, AnimationPropertiesContainer animProps) {
    this.lang = lang;
    this.animalGraphGenerator = new AnimalGraphGenerator(
        (AnimalScript) this.lang);
    this.animProps = animProps;

    this.graphProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.WHITE);
    this.graphProperties.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY,
        Color.WHITE);
    this.graphProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    this.graphProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    this.graphProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);

  }

  /**
   * Draws a new graph based on a adjacency matrix, nodes representing
   * coordinates and labels for the nodes.
   * 
   * @param adjacencyMatrix
   *          representing connections
   * @param nodes
   *          representing coordinates
   * @param labels
   *          labels for the nodes
   * @param onXplane
   *          specifies, if a nodes is on the x-plane
   */
  private void drawGraph(int[][] adjacencyMatrix, Node[] nodes,
      String[] labels, int[] levels) {

    // hide old graph and draw a new one
    if (this.graph != null)
      this.graph.hide();
    this.graph = new algoanim.primitives.Graph(this.animalGraphGenerator,
        this.name + this.graphCounter++, adjacencyMatrix, nodes, labels,
        this.displayOptions, this.graphProperties);

    // highlight all nodes on yPlane
    for (int i = 0; i < levels.length; i++) {
      if ((levels[i] % 2) == 1)
        this.graph.highlightNode(i, null, null);
    }

    this.lang.nextStep();
  }

  /**
   * Draws a graph from a kdnode.
   * 
   * @param kdnode
   */
  public void drawGraph(KDNode kdnode) {
    GraphData graphData = traverseTree(kdnode,
        new GraphData(kdnode.getNodeCount()));
    this.nodes = graphData.nodes;
    moveCircleInsertion(0, false);
    drawGraph(graphData.adjacencyMatrix, graphData.nodes, graphData.labels,
        graphData.levels);

  }

  /**
   * Calculate the properties of the kd-tree starting at this node.
   * 
   * @return a data bean, containing the properties as package visible fields
   */
  private GraphData traverseTree(KDNode kDtree, GraphData graphData) {
    if (kDtree == null)
      return graphData;
    else {

      if (kDtree.pred != null)
        graphData.adjacencyMatrix[kDtree.pred.id][kDtree.id] = 1;
      graphData.labels[kDtree.id] = "" + kDtree.xValue + "," + kDtree.yValue;

      graphData.nodes[kDtree.id] = calcCoordinates(kDtree, graphData.nodes);

      graphData.levels[kDtree.id] = kDtree.level;

      return (traverseTree(kDtree.right, (traverseTree(kDtree.left, graphData))));
    }
  }

  private Node calcCoordinates(KDNode kdnode, Node[] nodes) {
    if (kdnode.pred == null) {
      return this.rootCoordinates;
    } else {
      Coordinates predCoordinates = (Coordinates) nodes[kdnode.pred.id];
      int level = kdnode.level;
      int depthOfTree = kdnode.getDepthOfTree();
      int skew = depthOfTree - level;

      // are we left or right?
      double directionCoefficient = (kdnode.pred.left == kdnode) ? -1 : 1;
      int distance = 50;

      return new Coordinates((int) Math.round(predCoordinates.getX()
          + directionCoefficient
          * (distance + skew * distance + log(skew) * distance)),
          predCoordinates.getY() + distance);
    }
  }

  private double log(int n) {
    if (n == 0)
      return 0;
    return Math.log(n);
  }

  public void highlightEdge(int startNodeId, int endNodeId) {
    Timing edgeHighlightDelay = new TicksTiming(50);
    this.graph.highlightEdge(startNodeId, endNodeId, null, edgeHighlightDelay);
  }

  public void moveCircleInsertion(int nodeId, boolean delayed) {
    if (this.circleInsertion == null)
      this.circleInsertion = new VisualCircle((Coordinates) this.nodes[nodeId],
          this.nodes, this.lang, this.animProps, Color.ORANGE, 30);
    this.circleInsertion.moveToNode((Coordinates) this.nodes[nodeId], delayed);
  }

  public void moveCircleBest(int nodeId, boolean delayed) {
    if (this.circleBest == null)
      this.circleBest = new VisualCircle((Coordinates) this.nodes[nodeId],
          this.nodes, this.lang, this.animProps, Color.GREEN, 20);
    this.circleBest.moveToNode((Coordinates) this.nodes[nodeId], delayed);
  }

  public void moveCircleSubtreeBest(int nodeId, boolean delayed) {
    if (this.circleSubtreeBest == null)
      this.circleSubtreeBest = new VisualCircle(
          (Coordinates) this.nodes[nodeId], this.nodes, this.lang,
          this.animProps, Color.CYAN, 19);
    this.circleSubtreeBest
        .moveToNode((Coordinates) this.nodes[nodeId], delayed);
  }

  public void hideCircleSubtreeBest(int nodeId) {
    if (this.circleSubtreeBest != null)
      this.circleSubtreeBest.hide();
  }

  public VisualCircle getCircleInsertion() {
    return this.circleInsertion;
  }
}