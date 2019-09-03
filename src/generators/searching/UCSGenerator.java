package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

/**
 * A class representing an annotated node also consisting of associated
 * pathCosts and a predecessor besides its name.
 */
class UCSNode {

  private Integer name;
  private String  label;
  private UCSNode predecessor;
  private int     pathCost;

  public UCSNode(int n, String l, UCSNode pred, int costs) {
    this.name = n;
    this.label = l;
    this.predecessor = pred;
    this.pathCost = costs;
  }

  public Integer getName() {
    return name;
  }

  public String getLabel() {
    return label;
  }

  public UCSNode getPredecessor() {
    return predecessor;
  }

  public int getPathCost() {
    return pathCost;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !(o instanceof UCSNode))
      return false;
    else
      return name.equals(((UCSNode) o).getName());
  }

  public String toString() {
    return this.getLabel();
  }
}

/**
 * A class modeling the Animation of the UniformCostSearch algorithm.
 */
public class UCSGenerator implements Generator {

  private Language      lang;

  private static String SOURCE_CODE = "function uniformCostSearch(Graph graph, node start, node goal):\n"
                                        + // 0
                                        "    start.predecessor := None\n"
                                        + // 1
                                        "    start.pathCost := 0\n"
                                        + // 2
                                        "    explored := []\n"
                                        + // 3
                                        "    frontier := [ start ]      // realized as a priority queue in this demo\n\n"
                                        + // 4, 5
                                        "    do:\n"
                                        + // 6
                                        "        currentnode := frontier.pop()\n"
                                        + // 7
                                        "        explored := explored.add(currentnode)\n\n"
                                        + // 8, 9
                                        "        if currentnode is goal:\n"
                                        + // 10
                                        "            return pathTo(currentnode)\n\n"
                                        + // 11, 12
                                        "       for each neighbor currentNeighbor of currentnode:\n\n"
                                        + // 13, 14
                                        "            if currentNeighbor is not in explored:\n\n"
                                        + // 15, 16
                                        "                if currentNeighbor is not in frontier:\n"
                                        + // 17
                                        "                    frontier.add(currentNeighbor) with correct predecessor and pathCost\n\n"
                                        + // 18, 19
                                        "                else if currentNeighbor is in frontier with higher path cost:\n"
                                        + // 20
                                        "                    frontier.update(currentNeighbor) with new predecessor and pathCost\n\n"
                                        + // 21, 22
                                        "                else:\n"
                                        + // 23
                                        "                    skip, since the currentNeighbor is already in the frontier with better pathCost\n\n"
                                        + // 24, 25
                                        "    while frontier is not empty\n\n" + // 26,
                                                                                // 27
                                        "return failure\n"; // 28

  private Text          header;
  private Text          subHeader;
  private Text          frontierName;
  private Text          frontierVal;
  private Text          exploredName;
  private Text          exploredVal;
  private Text          currnodeName;
  private Text          currnodeVal;
  private Text          currNeighborName;
  private Text          currNeighborVal;
  private Text[]        nodePredVals;
  private Text[]        nodePathCostVals;
  private Graph         graph;
  private SourceCode    src;

  private Color         nodeEdgeHighlightColor;

  public UCSGenerator() {
    init();
  }

  UCSGenerator(Language l) {
    this.lang = l;
    this.lang.setStepMode(true);
  }

  private void animUnhighlight(Text text) {
    text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null,
        null);
  }

  private void animUpdate(Text text, Object o) {
    animUpdate(text, o, 0);
  }

  // FIX ME: we don't need this function. Thus, change it's signature!
  private void animUpdate(Text text, Object o, int delay) {
    text.setText(o.toString(), new MsTiming(delay), null);
    text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED,
        new MsTiming(delay), null);
  }

  private void animUnhighlightnodeTexts() {
    for (int i = 0; i < nodePredVals.length; i++) {
      animUnhighlight(nodePredVals[i]);
      animUnhighlight(nodePathCostVals[i]);
    }
  }

  private void animUpdatenodeTexts(UCSNode n) {
    animUpdatenodeTexts(n, 0);
  }

  // FIX ME: we don't need this function. Thus, change it's signature!
  private void animUpdatenodeTexts(UCSNode n, int delay) {
    animUnhighlightnodeTexts();

    String predLabel = (n.getPredecessor() == null ? "None" : n
        .getPredecessor().getLabel());
    animUpdate(nodePredVals[n.getName()], predLabel, delay);
    animUpdate(nodePathCostVals[n.getName()], "" + n.getPathCost(), delay);

  }

  private void animToggleSource(int from, int to, boolean highlight) {
    animToggleSource(from, to, highlight, 0);
  }

  // FIX ME: we don't need this function. Thus, change it's signature!
  private void animToggleSource(int from, int to, boolean highlight, int delay) {
    for (int i = from; i <= to; ++i) {
      if (highlight)
        src.highlight(i, -1, false, new MsTiming(delay), null);
      else
        src.unhighlight(i, -1, false, new MsTiming(delay), null);
    }
  }

  /**
   * suggests coordinates for the text annotations (i.e., predecessor and
   * pathCost texts) of a node with respect to the other nodes' coordinates.
   * 
   * @param nodeCoords
   *          the node for which to estimate the coordinates of the text
   *          annotations
   * @param otherCoords
   *          the coordinates of all the nodes in the graph
   * @param offset
   *          some offset coordinates also to be added to the estimate.
   * @return coordinates for the text annotations such that they most likely do
   *         not interfere with other nodes or their respective text annotations
   */
  private Coordinates estimateCoords(Coordinates nodeCoords,
      Coordinates[] otherCoords, Coordinates offset) {
    // counts the nodes, which are located left, right, ... of the given
    // nodeCoords.
    int left, right, above, below;
    left = right = above = below = 0;

    for (int i = 0; i < otherCoords.length; ++i) {
      if (nodeCoords.getX() < otherCoords[i].getX())
        right++;
      else if (nodeCoords.getX() > otherCoords[i].getX())
        left++;

      if (nodeCoords.getY() < otherCoords[i].getY())
        ++below;
      else if (nodeCoords.getY() > otherCoords[i].getY())
        above++;
    }

    int factorX = (int) Math.signum(left - right);
    int factorY = (int) Math.signum(above - below);
    int deltaX = (factorX >= 0 ? factorX * 50 : factorX * 75);
    int deltaY = (factorY >= 0 ? factorY * 50 : factorY * 75);

    if (left == right && above == below)
      return new Coordinates(nodeCoords.getX() + offset.getX(),
          nodeCoords.getY() + 50 + offset.getY());
    return new Coordinates(nodeCoords.getX() + deltaX + offset.getX(),
        nodeCoords.getY() + deltaY + offset.getY());
  }

  public void animInitSlide(TextProperties headerProps,
      SourceCodeProperties srcProps) {
    // title
    header = lang.newText(new Coordinates(20, 30),
        "Uniform-Cost Search (UCS) Demonstration", "header", null, headerProps);
    header.setFont(new Font("SansSerif", Font.BOLD, 24), null, null);
    lang.nextStep("Introduction");

    // objective
    subHeader = lang.newText(new Coordinates(30, 80), "Objective:",
        "objective", null);
    subHeader.setFont(new Font("SansSerif", Font.BOLD, 20), null, null);
    Text objectiveVal = lang
        .newText(
            new Coordinates(30, 110),
            "Uniform-Cost Search (UCS) (also: Cheapest-First Search) finds the lowest"
                + " cost path from a given start node to a given goal node according to a given graph.",
            "objectiveval", null);
    objectiveVal.setFont(new Font("SansSerif", Font.PLAIN, 16), null, null);
    lang.nextStep("Objective");

    // set up the source code
    src = lang.newSourceCode(new Coordinates(700, 190), "code", null, srcProps);

    String[] code = SOURCE_CODE.split("\n");
    for (int i = 0; i < code.length; i++)
      src.addCodeLine(code[i], null, 1, null);

    lang.nextStep();

    // basic idea
    Text idea = lang.newText(new Coordinates(30, 160), "Basic Idea:", "idea",
        null);
    idea.setFont(new Font("SansSerif", Font.BOLD, 20), null, null);

    String[] texts = {
        "1) The algorithm maintains two lists of nodes: explored and frontier.",
        "The former contains the nodes to which an optimal path from the",
        "start node has already been calculated. The latter contains the neighbors",
        "of all the explored nodes.",
        "",
        "2) The algorithm repeatedly removes the node with the lowest path cost",
        "off the frontier and adds it to the list of explored nodes.", "",
        "3) If this node is the goal node, the path from the start node to it",
        "is reconstructed and the algorithm terminates.", "",
        "4) Otherwise, the algorithm updates the frontier list with the",
        "unexplored neighbours of the newly explored node.", "",
        "5) It continues with the next optimal node from the frontier",
        "until this list is empty, in which case start node and goal node",
        "are not connected and a failure is returned.", "" };

    // marks the start- and end lines of the code fragments to highlight in
    // each step.
    int[] codeLines = { 0, 0, 1, 4, 5, 8, 9, 11, 13, 25, 26, 28 };
    int j = 0;
    Text[] ideaVals = new Text[texts.length];

    boolean showBasicIdea = true;
    for (int i = 0; i < texts.length; ++i) {
      ideaVals[i] = lang.newText(new Coordinates(30, 190 + 25 * i), texts[i],
          "text" + i, null);
      ideaVals[i].setFont(new Font("SansSerif", Font.PLAIN, 16), null, null);
      if (texts[i].equals("")) {
        animToggleSource(codeLines[j], codeLines[j + 1], false);
        j = j + 2;
        animToggleSource(codeLines[j], codeLines[j + 1], true);
        if (showBasicIdea) {
          lang.nextStep("Basic Idea");
          showBasicIdea = false;
        } else
          lang.nextStep();
      }
    }
    // unhighlight code
    animToggleSource(0, 28, false);

    // hide idea and objective
    objectiveVal.hide();
    idea.hide();
    for (Text ideaVal : ideaVals) {
      ideaVal.hide();
    }
  }

  public void animFinalSlide(List<String> path) {
    // hide example header and source code
    src.hide();
    subHeader.setText("Conclusion:", null, null);

    exploredName.hide();
    exploredVal.hide();
    frontierName.hide();
    frontierVal.hide();
    currnodeName.hide();
    currnodeVal.hide();
    currNeighborName.hide();
    currNeighborVal.hide();

    lang.nextStep("Conclusion");

    String pathRepr;
    if (path == null)
      pathRepr = "[] (no path could be found!)";
    else
      pathRepr = path.toString();

    lang.newText(new Coordinates(700, 190),
        "The lowest cost path in the graph is: " + pathRepr, "c1", null)
        .setFont(new Font("SansSerif", Font.PLAIN, 16), null, null);
    lang.newText(new Coordinates(700, 240),
        "A lowest cost path is always found, if the start and the goal node",
        "c2", null).setFont(new Font("SansSerif", Font.PLAIN, 16), null, null);
    lang.newText(
        new Coordinates(700, 265),
        "are connected and if the graph only consists of positive edge weights",
        "c3", null).setFont(new Font("SansSerif", Font.PLAIN, 16), null, null);
  }

  private void animInit(int[][] adjacencyMatrix, int startnodeName,
      Coordinates[] nodeCoords, String[] nodeLabels) {

    // set up header and source code
    header.show();

    // example header
    subHeader.setText("Example:", null, null);

    // set up the graph
    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLUE);
    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        nodeEdgeHighlightColor);

    graph = lang.newGraph("graph", adjacencyMatrix, nodeCoords, nodeLabels,
        null, graphProps);

    // associate two textfields (displaying the predecessor and
    // the pathCosts) with each of the given graph nodes
    nodePredVals = new Text[nodeCoords.length];
    nodePathCostVals = new Text[nodeCoords.length];

    for (int i = 0; i < nodeCoords.length; ++i) {
      // why does something like
      // new Offset(90, 50, nodeCoords[i], AnimalScript.DIRECTION_SE)
      // throw a NullPointerException??

      lang.newText(
          estimateCoords(nodeCoords[i], nodeCoords, new Coordinates(0, 0)),
          "predecessor: ", "node" + i + "pred", null);
      nodePredVals[i] = lang.newText(
          estimateCoords(nodeCoords[i], nodeCoords, new Coordinates(85, 0)),
          "-", "node" + i + "PredVal", null, new TextProperties());
      lang.newText(
          estimateCoords(nodeCoords[i], nodeCoords, new Coordinates(0, 25)),
          "pathCost: ", "node" + i + "PathCost", null);
      nodePathCostVals[i] = lang.newText(
          estimateCoords(nodeCoords[i], nodeCoords, new Coordinates(60, 25)),
          "-", "node" + i + "PathCostVal", null, new TextProperties());
    }

    lang.nextStep("Example");

    // create textfields representing the current explored and frontier list
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));

    exploredName = lang.newText(new Coordinates(40, 700), "explored: ",
        "exploredName", null, textProps);
    exploredVal = lang.newText(new Coordinates(120, 700), "-", "exploredVal",
        null, textProps);
    frontierName = lang.newText(new Coordinates(40, 745), "frontier: ",
        "frontierName", null, textProps);
    frontierVal = lang.newText(new Coordinates(120, 745), "-", "frontierVal",
        null, textProps);

    // create textfields displaying the currentnode and the currentNeighbor
    currnodeName = lang.newText(new Coordinates(400, 700), "currentnode: ",
        "currnodeName", null, textProps);
    currnodeVal = lang.newText(new Coordinates(510, 700), "-", "currnodeVal",
        null, textProps);
    currNeighborName = lang.newText(new Coordinates(400, 745),
        "currentNeighbor: ", "currNeighborName", null, textProps);
    currNeighborVal = lang.newText(new Coordinates(540, 745), "-",
        "currNeighborVal", null, textProps);

    animToggleSource(0, 0, true);

    lang.nextStep();

    // animate the initialization of the explored and frontier list
    animUpdate(exploredVal, "[]");
    animUpdate(frontierVal, "[" + nodeLabels[startnodeName] + "]");

    animToggleSource(0, 0, false);
    animToggleSource(1, 4, true);

    animUpdatenodeTexts(new UCSNode(startnodeName, nodeLabels[startnodeName],
        null, 0));

    lang.nextStep();
  }

  private void animDoBlock(UCSNode currnode, Collection<UCSNode> frontier,
      List<UCSNode> explored) {
    // unhighlight node texts and source code from the previous iteration
    animUnhighlightnodeTexts();
    animToggleSource(26, 26, false);

    animUpdate(frontierVal, frontier);
    animUpdate(exploredVal, explored);
    animUpdate(currnodeVal, currnode.getLabel());

    graph
        .highlightNode(explored.get(explored.size() - 1).getName(), null, null);
    exploredName.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.GREEN, null, null);

    animToggleSource(1, 4, false);
    animToggleSource(6, 8, true);

    lang.nextStep();
  }

  private void animGoalCheckTrue(UCSNode n, int startnodeName, int endnodeName) {
    animUnhighlight(frontierVal);
    animUnhighlight(exploredVal);

    animToggleSource(6, 8, false);
    animToggleSource(10, 10, true);

    lang.nextStep();

    animToggleSource(10, 10, false);
    animToggleSource(11, 11, true);

    animUnhighlight(currnodeVal);

    lang.nextStep();

  }

  private void animGoalCheckFalse(UCSNode n, int endnodeName) {
    animUnhighlight(frontierVal);
    animUnhighlight(exploredVal);

    animToggleSource(6, 8, false);
    animToggleSource(10, 10, true);

    lang.nextStep();

    animUnhighlight(currnodeVal);
  }

  private void animInitFor(UCSNode currnode, int[][] adjacencyMatrix,
      String[] nodeLabels) {
    if (neighbors(currnode, adjacencyMatrix, nodeLabels).size() == 0) {
      animToggleSource(10, 10, false);
      animToggleSource(13, 13, true);
      lang.nextStep();
    }
  }

  private void animNeighborFor(UCSNode currnode, UCSNode neighbor,
      Collection<UCSNode> explored) {
    animToggleSource(10, 10, false);
    animToggleSource(15, 25, false);
    animUnhighlight(frontierVal);
    animUnhighlightnodeTexts();

    animToggleSource(13, 13, true);
    animUpdate(currNeighborVal, neighbor.getLabel());

    // highlight corresponding edges in graph:
    graph.highlightEdge(currnode.getName(), neighbor.getName(), null, null);

    lang.nextStep();
    // unhighlight edge again
    graph.unhighlightEdge(currnode.getName(), neighbor.getName(), null, null);
    animUpdate(exploredVal, explored);
    animToggleSource(13, 13, false);
    animToggleSource(15, 15, true);
    lang.nextStep();

    animUnhighlight(exploredVal);
  }

  private void animNeighborUpdate(UCSNode frontiernode, UCSNode neighbor,
      Collection<UCSNode> frontier) {
    animUnhighlight(currnodeVal);

    animToggleSource(15, 15, false); // unhighlight
    // if !explored.contains(neighbor) statement

    // highlight corresponding code snippet
    if (frontiernode == null) {
      animToggleSource(17, 17, true);
    } else if (frontiernode.getPathCost() > neighbor.getPathCost()) {
      animToggleSource(20, 20, true);
    } else {
      animToggleSource(23, 23, true);
    }

    frontierVal.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED,
        null, null);
    lang.nextStep();

    animUpdate(frontierVal, frontier);

    if (frontiernode == null) {
      animToggleSource(17, 17, false);
      animToggleSource(18, 18, true);
      animUpdatenodeTexts(neighbor);
    } else if (frontiernode.getPathCost() > neighbor.getPathCost()) {
      animToggleSource(20, 20, false);
      animToggleSource(21, 21, true);
      animUpdatenodeTexts(neighbor);
    } else {
      animToggleSource(23, 23, false);
      animToggleSource(24, 24, true);
    }

    lang.nextStep();

  }

  private void animTerminationCheck(Collection<UCSNode> frontier) {
    animUnhighlightnodeTexts();
    animToggleSource(10, 25, false);

    animUpdate(currNeighborVal, "-");
    animUnhighlight(currNeighborVal);

    animUpdate(frontierVal, frontier);
    animToggleSource(26, 26, true);
    lang.nextStep();
  }

  private void animConstructPath(UCSNode goalnode) {
    if (goalnode.getPredecessor() != null)
      graph.highlightEdge(goalnode.getPredecessor().getName(),
          goalnode.getName(), null, null);
  }

  private LinkedList<String> constructPath(UCSNode goalnode) {
    LinkedList<String> res = new LinkedList<String>();
    UCSNode goalnode2 = goalnode;
    while (goalnode2 != null) {
      res.add(0, goalnode2.getLabel());
      animConstructPath(goalnode2);
      goalnode2 = goalnode2.getPredecessor();
    }
    return res;
  }

  private static List<UCSNode> neighbors(UCSNode node, int[][] adjacencyMatrix,
      String[] nodeLabels) {
    ArrayList<UCSNode> res = new ArrayList<UCSNode>();
    for (int i = 0; i < adjacencyMatrix[node.getName()].length; ++i) {
      int edgeCost = adjacencyMatrix[node.getName()][i];
      if (edgeCost > 0)
        res.add(new UCSNode(i, nodeLabels[i], node, node.getPathCost()
            + edgeCost));
    }
    return res;
  }

  public LinkedList<String> uniformCostSearch(int[][] adjacencyMatrix,
      int startnodeName, int endnodeName, Coordinates[] nodeCoords,
      String[] nodeLabels) {

    ArrayList<UCSNode> explored = new ArrayList<UCSNode>();
    PriorityQueue<UCSNode> frontier = new PriorityQueue<UCSNode>(5,
        new Comparator<UCSNode>() {
          @Override
          public int compare(UCSNode n1, UCSNode n2) {
            if (n2 == null || n1.getPathCost() > n2.getPathCost())
              return 1;
            else if (n1.getPathCost() == n2.getPathCost())
              return 0;
            else
              return -1;
          }
        });

    frontier
        .add(new UCSNode(startnodeName, nodeLabels[startnodeName], null, 0));

    // setup ANIMAL specific data structures and animate the above
    // initialization of the UCS algorithm.
    animInit(adjacencyMatrix, startnodeName, nodeCoords, nodeLabels);

    do {
      UCSNode currnode = frontier.poll();
      explored.add(currnode);

      animDoBlock(currnode, frontier, explored);

      if (currnode.getName().equals(endnodeName)) {
        animGoalCheckTrue(currnode, startnodeName, endnodeName);
        return constructPath(currnode);
      }
      animGoalCheckFalse(currnode, endnodeName);

      // highlight for statement in each do-while iteration, even if there
      // are no neighbors!
      animInitFor(currnode, adjacencyMatrix, nodeLabels);

      for (UCSNode neighbor : neighbors(currnode, adjacencyMatrix, nodeLabels)) {

        animNeighborFor(currnode, neighbor, explored);
        if (!explored.contains(neighbor)) {
          UCSNode candidateInFrontier = null;

          // update existing frontier nodes with lower cost neighbor
          for (UCSNode elem : frontier) {
            if (elem.equals(neighbor)
                && elem.getPathCost() > neighbor.getPathCost()) {
              frontier.remove(elem);
              frontier.add(neighbor);
              candidateInFrontier = elem;
            } else if (elem.equals(neighbor)) {
              candidateInFrontier = elem;
            }
          }
          if (candidateInFrontier == null)
            frontier.add(neighbor);

          animNeighborUpdate(candidateInFrontier, neighbor, frontier);
        }
      }
      animTerminationCheck(frontier);
    } while (frontier.size() > 0);

    return null;
  }

  public String getName() {
    return "Uniform-Cost Search (UCS) Demonstration";
  }

  public String getAlgorithmName() {
    return "Uniform-Cost Search (Cheapest-First Search)";
  }

  public String getAnimationAuthor() {
    return "Dominik Bollmann, Sogol Mazaheri";
  }

  public String getDescription() {
    return "The Uniform-Cost Search algorithm is a search algorithm operating on graphs. It computes the lowest-cost path from a given start node to a given end node according to a given graph structure. "
        + "\n"
        + "The algorithm will always find an optimal solution, if the start and the end node are connected and if all edge weights in the graph structure have positive weights.";
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  private void checkParameterValidity(int startnodeName, int endnodeName,
      int[][] adjacencyMatrix, String[] nodeLabels, int[][] nodeCoords) {
    if (nodeLabels.length <= 0 || adjacencyMatrix.length != nodeLabels.length
        || adjacencyMatrix[0].length != nodeLabels.length
        || nodeLabels.length != nodeCoords.length || nodeCoords[0].length != 2)
      throw new IllegalArgumentException("the given node structure is invalid!");
    if (startnodeName < 0 || startnodeName >= nodeLabels.length)
      throw new IllegalArgumentException(
          "the given start node is not in the given node structure");
    if (endnodeName < 0 || endnodeName >= nodeLabels.length)
      throw new IllegalArgumentException(
          "the given end node is not in the given node structure");
  }

  private Coordinates[] prepareCoords(int[][] nodeCoords) {
    Coordinates[] coords = new Coordinates[nodeCoords.length];
    for (int i = 0; i < nodeCoords.length; ++i) {
      coords[i] = new Coordinates(nodeCoords[i][0], nodeCoords[i][1]);
    }
    return coords;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int startnodeName = (Integer) primitives.get("startnode");
    int endnodeName = (Integer) primitives.get("endnode");
    int[][] adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
    String[] nodeLabels = (String[]) primitives.get("nodeLabels");
    int[][] inputnodeCoords = (int[][]) primitives.get("nodeCoords");

    TextProperties headerProps = (TextProperties) props
        .getPropertiesByName("title");
    SourceCodeProperties srcProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    CircleProperties nodeEdgeProps = (CircleProperties) props
        .getPropertiesByName("nodeEdgeHighlightColor");
    nodeEdgeHighlightColor = (Color) nodeEdgeProps.get("color");

    checkParameterValidity(startnodeName, endnodeName, adjacencyMatrix,
        nodeLabels, inputnodeCoords);
    Coordinates[] nodeCoords = prepareCoords(inputnodeCoords);

    animInitSlide(headerProps, srcProps);
    List<String> path = uniformCostSearch(adjacencyMatrix, startnodeName,
        endnodeName, nodeCoords, nodeLabels);
    animFinalSlide(path);

    return lang.toString();
  }

  public void init() {
    lang = new AnimalScript("Uniform-Cost Search (UCS) Demonstration",
        "Dominik Bollmann, Sogol Mazaheri", 800, 600);
    lang.setStepMode(true);
  }
}
