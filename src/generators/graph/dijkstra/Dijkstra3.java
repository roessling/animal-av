package generators.graph.dijkstra;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.variables.VariableRoles;

public class Dijkstra3 implements Generator {
  private Language             lang;
  SourceCode                   sourceCode;
  SourceCode                   description;
  private SourceCodeProperties message;
  private SourceCodeProperties result;
  private TextProperties       title;
  private SourceCodeProperties sourceCodeProperties;
  private SourceCodeProperties descriptionProperties;
  private Graph                graph;
  private GraphProperties      gp;
  private Variables            v;
  private RectProperties       titleFrame;
  private RectProperties       infoFrame;
  private SourceCodeProperties table;
  private SourceCode           tableContent;
  // private SourceCode explain;
  private SourceCode           tableH;
  private MatrixProperties     mp;
  private StringMatrix         matrix;
  private Graph                g;

  public void init() {
    lang = new AnimalScript("Dijkstra Algorithm[EN]",
        "Tanya Harizanova,Dimitar Dimitrov", 800, 600);
    lang.setStepMode(true);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    message = (SourceCodeProperties) props.getPropertiesByName("message");
    title = (TextProperties) props.getPropertiesByName("title");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");

    descriptionProperties = (SourceCodeProperties) props
        .getPropertiesByName("description");
    graph = (Graph) primitives.get("graph");
    result = (SourceCodeProperties) props.getPropertiesByName("result");
    titleFrame = (RectProperties) props.getPropertiesByName("titleFrame");
    infoFrame = (RectProperties) props.getPropertiesByName("infoFrame");
    gp = (GraphProperties) props.getPropertiesByName("graphProperties");
    displayTitle();
    displayAlgorithmDescription(100);
    lang.nextStep("Introduction");
    lang.hideAllPrimitives();
    displayTitle();
    displaySourceCode();

    g = lang.newGraph("Graph", graph.getAdjacencyMatrix(), nodes(graph),
        labels(graph), null, gp);
    g.setStartNode(graph.getStartNode());
    g.setTargetNode(graph.getTargetNode());
    table = (SourceCodeProperties) props.getPropertiesByName("table");
    displayTable();
    displayLabel();
    displayRow();
    shortestPath(nodes(g), g.getPositionForNode(g.getStartNode()),
        g.getStartNode(), new HashMap<Node, Integer>(), g, false, 1,
        new HashMap<Node, List<Node>>());

    displaylastPage();
    lang.nextStep("Conclusion");
    return lang.toString();
  }

  public String getName() {
    return "Dijkstra Algorithm";
  }

  public String getAlgorithmName() {
    return "Dijkstra";
  }

  public String getAnimationAuthor() {
    return "Tanya Harizanova, Dimitar Dimitrov";
  }

  public String getDescription() {
    return "Information about this algorithm:"
        + "\n"
        + "\n"
        + "Dijkstra's algorithm is a graph search algorithm that solves the single-source shortest path problem,"
        + "\n"
        + "This algorithm is often used in routing and as a subroutine in other graph algorithms."
        + "\n"
        + "For a given source vertex (node) in the graph, the algorithm finds the path with lowest cost"
        + "\n"
        + "between that vertex and every other vertex. It can also be used for finding costs of shortest paths"
        + "\n"
        + "from a single vertex to a single destination vertex by stopping"
        + "\n"
        + "the algorithm once the shortest path to the destination vertex has been determined."
        + "\n"
        + "Dijkstra's original algorithm does not use a min-priority queue and runs in O(n^2). ";
  }

  public String getCodeExample() {
    return "int Dijkstra(Node startNode, Node targetNode,HashMap<Node,Integer> group) {"
        + "\n"
        + "Node currentNode //beginn with startNode"
        + "\n"
        + " if currentNode is equal targetNode"
        + "\n"
        + " return pathToTarget"
        + "\n"
        + "while hasNeighbors"
        + "\n"
        + " if group does not contain neighbor already"
        + "\n"
        + " True :put neighbor into the group with the path to it"
        + "\n"
        + "else if path from start to neighbor the shortest path"
        + "\n"
        + "True :OK"
        + "\n"
        + "  False :group put neighbor with the shortest path to start"
        + "\n"
        + "call recursivly the method with the next neighbour";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * this method creates a title and displays it in rectangle;
   */
  public void displayTitle() {

    Text titleText = lang.newText(new Coordinates(450, 10), "Dijkstra",
        "title", null, title);
    lang.newRect(new Offset(-5, -5, titleText, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, titleText, AnimalScript.DIRECTION_SE), "Back", null,
        titleFrame);

  }

  public void displayMatrix() {
    mp = new MatrixProperties();
    mp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    String[][] m = new String[g.getAdjacencyMatrix().length][g
        .getAdjacencyMatrix().length];
    for (int i = 0; i < m.length; i++) {
      for (int j = 0; j < m[i].length; j++) {
        m[i][j] = "";
      }
    }
    matrix = lang.newStringMatrix(new Coordinates(630, 350), m, "matrix", null,
        mp);

  }

  public void displayTable() {
    String resultX = "";

    tableContent = lang.newSourceCode(new Coordinates(600, 320), "matrix",
        null, table);

    for (int i = 0; i < g.getAdjacencyMatrix().length; i++) {
      resultX = resultX + " | to " + g.getNodeLabel(i);

    }

    tableContent.addCodeLine(resultX, null, 1, null);

  }

  public void displayLabel() {
    String label = "From Start Node : "
        + graph.getNodeLabel(graph.getStartNode());

    tableContent = lang.newSourceCode(new Coordinates(480, 305), "matrix",
        null, table);
    tableContent.addCodeLine("Shortest path ", null, 1, null);
    tableContent.addCodeLine(label, null, 1, null);
  }

  public void displayRow() {
    String result = "";

    tableH = lang.newSourceCode(new Coordinates(550, 340), "matrix", null,
        table);

    for (int i = 0; i < g.getAdjacencyMatrix().length; i++) {
      result = "through " + g.getNodeLabel(i);
      tableH.addCodeLine(result, null, 0, null);
      tableH.addCodeLine("", null, 0, null);
    }

  }

  public void displaylastPage() {

    lang.hideAllPrimitives();
    matrix.changeColor("color", Color.WHITE, new TicksTiming(100),
        new TicksTiming(100));
    SourceCodeProperties scp = descriptionProperties;

    displayTitle();
    description = lang.newSourceCode(new Coordinates(10, 150), "description",
        null, scp);

    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("Run time :", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description
        .addCodeLine(
            "Dijkstra's original algorithm does not use a min-priority queue and runs in O(n^2). ",
            null, 0, null);
    description
        .addCodeLine(
            "This is asymptotically the fastest known single-source shortest-path ",
            null, 0, null);
    description
        .addCodeLine(
            "algorithm for arbitrary directed graphs with unbounded nonnegative weights.",
            null, 0, null);
    description
        .addCodeLine(
            "Dijkstra's algorithm is usually the working principle behind link-state routing protocols,",
            null, 0, null);
    description.addCodeLine(" OSPF and IS-IS being the most common ones.",
        null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("Sources:", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("http://de.wikipedia.org/wiki/Dijkstra", null, 0,
        null);

    lang.newRect(new Offset(-5, -5, description, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, description, AnimalScript.DIRECTION_SE), "frame1",
        null, infoFrame);

  }

  /**
   * this method creates a source code and displays it;
   */
  public void displaySourceCode() {

    sourceCode = lang.newSourceCode(new Coordinates(550, 60), "sourceCode",
        null, sourceCodeProperties);

    sourceCode
        .addCodeLine(
            "int Dijkstra(Node startNode, Node targetNode,HashMap<Node,Integer> group(HashMap with already checked nodes as key and a shortest path from start node to them as value) {,",
            null, 0, null);
    sourceCode.addCodeLine("Node currentNode //beginn with startNode", null, 0,
        null);
    sourceCode.addCodeLine("if currentNode is equal targetNode", null, 0, null);
    sourceCode.addCodeLine("return pathToTarget", null, 1, null);
    sourceCode.addCodeLine("while hasNeighbors", null, 0, null);
    sourceCode.addCodeLine("if group does not contain neighbor already", null,
        1, null);
    sourceCode.addCodeLine(
        "True :put neighbor into the group with the path to it", null, 2, null);
    sourceCode.addCodeLine(
        "else if path from start to neighbor the shortest path", null, 1, null);
    sourceCode.addCodeLine("True :OK", null, 2, null);
    sourceCode.addCodeLine(
        "False :group put neighbor with the shortest path to start", null, 2,
        null);
    sourceCode.addCodeLine(
        "call recursivly the method with the next neighbour", null, 0, null);

  }

  /**
   * this method creates an algorithm description and displays it;
   */
  public void displayAlgorithmDescription(int coordinates) {

    description = lang.newSourceCode(new Coordinates(10, coordinates),
        "description", null, descriptionProperties);

    description.addCodeLine("Information about this algorithm:", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description
        .addCodeLine(
            "Dijkstra's algorithm is a graph search algorithm that solves the single-source shortest path problem,",
            null, 0, null);
    description
        .addCodeLine(
            "This algorithm is often used in routing and as a subroutine in other graph algorithms.",
            null, 0, null);
    description
        .addCodeLine(
            "For a given source vertex (node) in the graph, the algorithm finds the path with lowest cost",
            null, 0, null);
    description
        .addCodeLine(
            "between that vertex and every other vertex. It can also be used for finding costs of shortest paths",
            null, 0, null);
    description.addCodeLine(
        "from a single vertex to a single destination vertex by stopping",
        null, 0, null);
    description
        .addCodeLine(
            "the algorithm once the shortest path to the destination vertex has been determined.",
            null, 0, null);

    lang.newRect(new Offset(-5, -5, description, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, description, AnimalScript.DIRECTION_SE), "frame1",
        null, infoFrame);

  }

  public Node[] nodes(Graph graph) {
    Node[] nodes = new Node[graph.getAdjacencyMatrix()[0].length];
    for (int i = 0; i < graph.getAdjacencyMatrix()[0].length; i++) {
      nodes[i] = graph.getNode(i);
    }
    return nodes;
  }

  public String[] labels(Graph graph) {
    String[] nodes = new String[graph.getAdjacencyMatrix()[0].length];
    for (int i = 0; i < graph.getAdjacencyMatrix()[0].length; i++) {
      nodes[i] = graph.getNodeLabel(i);
    }
    return nodes;
  }

  /**
   * @param array
   *          with the nodes
   * @return a list with all nodes
   */
  public List<Node> createListWithNodes(Node[] array) {
    List<Node> result = new ArrayList<Node>();
    for (int i = 0; i < array.length; i++) {
      result.add(array[i]);
    }
    return result;
  }

  /**
   * @param current
   * @param allNodes
   * @param graph
   * @return a list with all collected neighbors for node
   */
  public List<Node> collectTheNeighbors(Node current, List<Node> allNodes,
      Graph graph) {
    List<Node> result = new ArrayList<Node>();
    result.add(current);
    for (int n = 0; n < allNodes.size(); n++) {

      if (graph.getEdgeWeight(current, allNodes.get(n)) != 0) {
        result.add(allNodes.get(n));

      }
    }
    return result;
  }

  /**
   * @param sum
   *          the shortest path this method shows the message in the end with
   *          the shortest path from start node to target node
   */
  public void displayResult(int sum, String startNode, String targetNode) {

    description = lang.newSourceCode(new Coordinates(10, 450), "description",
        null, result);

    description.addCodeLine("The shortest path from start node " + startNode
        + " to target node " + targetNode + " is : " + sum, null, 0, null);
    description.addCodeLine("", null, 0, null);

  }

  /**
   * @param nodeName
   *          //if you dont found the best path show a message it says that this
   *          neighbor not the best one is
   */
  public void displayMessage(String nodeName) {

    description = lang.newSourceCode(new Coordinates(10, 550), "description",
        null, message);

    description.addCodeLine(nodeName, null, 0, null);
    description.addCodeLine("", null, 0, null);
    lang.nextStep();
    description.hide();

  }

  public int shortestPath(Node[] graphNodes, int startPos, Node current,
      HashMap<Node, Integer> group, Graph graph, boolean huhu, int internal,
      HashMap<Node, List<Node>> groupPath) {
    int startNodePosition = startPos;
    int inter = internal;
    // display the title,source code and algorithm description

    sourceCode.highlight(0);
    List<Node> nodes = createListWithNodes(graphNodes);
    v = lang.newVariables();

    displayMatrix();
    Timing timing = new TicksTiming(100);
    // set start and end node

    displayMessage("The current node is set to " + graph.getNodeLabel(current));
    graph.highlightNode(current, timing, timing);
    // source code highlighting
    lang.nextStep();
    sourceCode.unhighlight(0);
    sourceCode.highlight(1);
    lang.nextStep();
    sourceCode.unhighlight(1);
    sourceCode.highlight(2);
    lang.nextStep("" + inter + ". " + "Interaction- set current node "
        + graph.getNodeLabel(current));

    if (current.equals(graph.getTargetNode())) {
      sourceCode.unhighlight(2);
      sourceCode.highlight(3);
      lang.nextStep();
      for (int j = 0; j < groupPath.get(current).size(); j++) {
        graph.highlightNode(groupPath.get(current).get(j), null, timing);

      }

      displayResult(group.get(graph.getTargetNode()),
          graph.getNodeLabel(graph.getStartNode()),
          graph.getNodeLabel(graph.getTargetNode()));
      lang.nextStep("Result");
      return group.get(current);
    }

    int i = 0;
    int in = 1;
    List<Node> listWithNeighbors = collectTheNeighbors(
        graph.getNode(startNodePosition), createListWithNodes(graphNodes),
        graph);

    while (i < listWithNeighbors.size()) {

      showVariablen("i", "" + i + "  in step (" + lang.getStep() + ")");
      // source code highlighting
      sourceCode.unhighlight(2);
      sourceCode.highlight(4);
      lang.nextStep();

      Node neighbor = listWithNeighbors.get(i);
      displayMessage("The next neighbor is " + graph.getNodeLabel(neighbor));
      graph.highlightEdge(current, neighbor, timing, timing);
      graph.highlightNode(neighbor, timing, timing);
      lang.nextStep("" + inter + "." + in + ". "
          + "Interaction - measure distance from node "
          + graph.getNodeLabel(current) + " to node "
          + graph.getNodeLabel(neighbor));

      int path = 0;
      // source code highlighting
      sourceCode.unhighlight(4);
      sourceCode.highlight(5);
      lang.nextStep();

      if (!group.containsKey(neighbor)) {
        // source code highlighting
        sourceCode.unhighlight(5);
        sourceCode.highlight(6);
        lang.nextStep();

        if (current.equals(graph.getStartNode())) {
          path = graph.getEdgeWeight(current, neighbor);
          group.put(neighbor, path);
          matrix.put(graph.getPositionForNode(current),
              graph.getPositionForNode(neighbor), "" + path, timing, timing);
          List<Node> list = new ArrayList<Node>();
          list.add(current);
          list.add(neighbor);
          groupPath.put(neighbor, list);
          String r = "";
          for (int k = 0; k < groupPath.get(neighbor).size(); k++) {
            r = r + graph.getNodeLabel(groupPath.get(neighbor).get(k)) + ",";
          }
          showVariablen("pathTo" + graph.getNodeLabel(neighbor) + "is", r);
          showVariablen("pathFromStartNode-" + graph.getNodeLabel(current)
              + "toNode-" + graph.getNodeLabel(neighbor), "" + path);
          displayMessage("The group does not contains "
              + graph.getNodeLabel(neighbor)
              + " ,put it into the group with shortest path from start node to "
              + graph.getNodeLabel(neighbor) + " set to " + path);
          sourceCode.unhighlight(6);
          graph.unhighlightEdge(current, neighbor, timing, timing);
          graph.unhighlightNode(neighbor, timing, timing);
          lang.nextStep();
        } else {
          int value = 0;
          if (group.get(current) == null) {
            value = graph.getEdgeWeight(current, graph.getStartNode());

            String r = "";
            for (int k = 0; k < groupPath.get(neighbor).size(); k++) {
              r = r + graph.getNodeLabel(groupPath.get(neighbor).get(k)) + ",";
            }
          } else {
            value = group.get(current);

          }
          path = value + graph.getEdgeWeight(current, neighbor);
          group.put(neighbor, path);
          matrix.put(graph.getPositionForNode(current),
              graph.getPositionForNode(neighbor), "" + path, timing, timing);
          if (groupPath.get(current) == null) {
            List<Node> list = new ArrayList<Node>();
            list.add(current);
            list.add(neighbor);
            list.add(graph.getStartNode());

            groupPath.put(neighbor, list);
          } else {

            groupPath.get(current).add(neighbor);
            groupPath.put(neighbor, groupPath.get(current));
          }
          String r = "";
          for (int k = 0; k < groupPath.get(neighbor).size(); k++) {
            r = r + graph.getNodeLabel(groupPath.get(neighbor).get(k)) + ",";
          }
          showVariablen("pathTo" + graph.getNodeLabel(neighbor) + "is", r);

          showVariablen(
              "pathFromStartNode-" + graph.getNodeLabel(graph.getStartNode())
                  + "toNode-" + graph.getNodeLabel(neighbor), "" + path);
          displayMessage("The group does not contains "
              + graph.getNodeLabel(neighbor)
              + " ,put it into the group with shortest path from start node to "
              + graph.getNodeLabel(neighbor) + " set to " + path);
          sourceCode.unhighlight(6);
          graph.unhighlightEdge(current, neighbor, timing, timing);
          graph.unhighlightNode(neighbor, timing, timing);
          lang.nextStep();
        }
      } else {
        displayMessage("The group contains " + graph.getNodeLabel(neighbor)
            + " already!");
        // source code highlighting
        sourceCode.unhighlight(5);
        sourceCode.highlight(7);
        lang.nextStep();
        int value = group.get(current);

        if (group.get(neighbor) > graph.getEdgeWeight(current, neighbor)
            + value) {
          // source code highlighting
          sourceCode.unhighlight(7);
          sourceCode.highlight(9);
          lang.nextStep();

          group.remove(neighbor);
          if (groupPath.get(neighbor).size() == 2) {
            groupPath.get(neighbor).add(current);
          } else {
            groupPath.get(neighbor).remove(groupPath.get(neighbor).size() - 1);
            groupPath.get(neighbor).add(current);
          }
          int newValue = graph.getEdgeWeight(current, neighbor) + value;
          group.put(neighbor, newValue);
          matrix
              .put(graph.getPositionForNode(current),
                  graph.getPositionForNode(neighbor), "" + newValue, timing,
                  timing);
          showVariablen(
              "pathFromStartNode-" + graph.getNodeLabel(graph.getStartNode())
                  + "toNode-" + graph.getNodeLabel(neighbor), "" + newValue);

          displayMessage("New shorter path found,change the path from start node to "
              + graph.getNodeLabel(neighbor)
              + " to the founded shorter path "
              + newValue);
          String r = "";
          for (int k = 0; k < groupPath.get(neighbor).size(); k++) {
            r = r + graph.getNodeLabel(groupPath.get(neighbor).get(k)) + ",";
          }
          showVariablen("pathTo" + graph.getNodeLabel(neighbor) + "is", r);

        }
        // source code highlighting
        sourceCode.unhighlight(7);
        sourceCode.highlight(8);
        graph.unhighlightEdge(current, neighbor, timing, timing);
        graph.unhighlightNode(neighbor, timing, timing);
        lang.nextStep();
        sourceCode.unhighlight(8);
      }

      sourceCode.unhighlight(9);

      i++;
      in++;
      lang.nextStep();

    }
    sourceCode.unhighlight(6);
    lang.nextStep();

    if (startNodePosition < nodes.size() - 1 && huhu == false) {
      startNodePosition = startNodePosition + 1;
    } else {
      if (startNodePosition == nodes.size() - 1) {
        startNodePosition = graph.getPositionForNode(graph.getStartNode()) - 1;
//        huhu = true;
      } else {
        startNodePosition--;
//        huhu = true;
      }
    }
    sourceCode.highlight(10);
    lang.nextStep();
    sourceCode.unhighlight(10);
    inter++;

    return shortestPath(graphNodes, startNodePosition,
        nodes.get(startNodePosition), group, graph, huhu, inter, groupPath);

  }

  public void showVariablen(String key, String value) {

    v.declare("STRING", key, value);
    v.setRole(key, "" + VariableRoles.STEPPER);

  }

}