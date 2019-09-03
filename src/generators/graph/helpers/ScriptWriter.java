package generators.graph.helpers;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Graph;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class ScriptWriter {

  private Language             lang;
  private Text                 tittle;
  private SourceCode           sc;

  private Graph                graph;
  private Graph                toDoGraph;
  private Graph                graph2;

  private Color                node_highlight_color;
  private Color                start_end_node_color;
  private SourceCodeProperties sourceCode;

  private int                  stepCounter;
  private int                  graphCounter = 0;
  private Color                node_color;
  private TextProperties       comments;
  private int                  highlightedNode;

  private int                  traversedNodes;
  private Text                 traversedText;
  private Text                 comment3;
  private String[]             nodeLabels;
  private String               target;
  private String               start;
  private int[][]              matrix;
  private List<String>         nodesList;
  private Node[]               coordinates;
  private Color                visited_nodes_color;
  private Color                todo_nodes_color;
  private Graph                graph3;

  public ScriptWriter(Language lang, AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    this.lang = lang;
    stepCounter = 0;
    visited_nodes_color = (Color) primitives.get("visited_nodes_color");
    todo_nodes_color = (Color) primitives.get("todo_nodes_color");
    node_highlight_color = (Color) primitives.get("node_highlight_color");
    start_end_node_color = (Color) primitives.get("start_end_node_color");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    start = (String) primitives.get("start");
    matrix = (int[][]) primitives.get("matrix");
    nodeLabels = (String[]) primitives.get("nodeLabels");
    target = (String) primitives.get("target");
    node_color = (Color) primitives.get("node_color");
    comments = (TextProperties) props.getPropertiesByName("comments");
    nodesList = Arrays.asList(nodeLabels);

    this.lang.setStepMode(true);

    placeTittle();
    if (validCoordinates((int[][]) primitives.get("coordinates")))
      coordinates = createCoordinates((int[][]) primitives.get("coordinates"));
    else
      writeCoordinatesError();
  }

  private boolean validCoordinates(int[][] coordinates) {
    if (coordinates.length == matrix.length && coordinates[0].length == 2)
      return true;
    return false;
  }

  private Node[] createCoordinates(int[][] is) {
    Node[] coord = new Node[is.length];
    for (int i = 0; i < is.length; i++) {
      coord[i] = new Offset(is[i][0] + 10, is[i][1] + 10, tittle, "SW");
    }
    return coord;
  }

  public void placeTittle() {
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.BOLD, 24));
    tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    tittle = lang.newText(new Coordinates(20, 30), "Path Search", "header",
        null, tp);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps
        .set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(204, 255, 0));
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    lang.newRect(new Offset(-5, -5, tittle, "NW"), new Offset(5, 5, tittle,
        "SE"), "tittleRect", null, rectProps);
  }

  public void writeInitialization() {
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode description = lang.newSourceCode(
        new Offset(0, 100, tittle, "SW"), "description", null, scProps);
    description.addCodeLine(
        "The algorithm calculates if 2 given Nodes in a graph are connected.",
        null, 1, null); // 0
    description
        .addCodeLine(
            "It works by starting with the given start node and working its way forward",
            null, 0, null);
    description
        .addCodeLine(
            "by traversing the neighbors of each visited node, until either the target",
            null, 0, null); // 3
    description
        .addCodeLine(
            "node is reached or until it traverses all nodes to which the start node has a",
            null, 0, null); // 4
    description.addCodeLine("connection.", null, 0, null); // 5

    lang.nextStep("Initialization");
    description.hide();

    GraphProperties gProps = new GraphProperties();
    gProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        node_highlight_color);
    gProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    gProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
    gProps.set(AnimationPropertiesKeys.FILL_PROPERTY, node_color);
    gProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);
    graph = lang.newGraph("graph", matrix, coordinates, nodeLabels, null,
        gProps);

    gProps = new GraphProperties();
    gProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        node_highlight_color);
    gProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    gProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
    gProps.set(AnimationPropertiesKeys.FILL_PROPERTY, start_end_node_color);
    gProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);
    gProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
    graph2 = lang.newGraph("graph2", matrix, coordinates, nodeLabels, null,
        gProps);

    gProps = new GraphProperties();
    gProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        visited_nodes_color);
    gProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    gProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
    gProps.set(AnimationPropertiesKeys.FILL_PROPERTY, todo_nodes_color);
    gProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);
    gProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
    graph3 = lang.newGraph("graph3", matrix, coordinates, nodeLabels, null,
        gProps);

    lang.nextStep();
    sc = lang.newSourceCode(new Offset(0, 50, graph, "SW"), "source", null,
        sourceCode);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    sc.addCodeLine("public void areConnected(Node start, Node target) {", null,
        0, null); // 0
    sc.addCodeLine("List<Node> toDoList=new ArrayList<Node>();", null, 1, null);
    sc.addCodeLine("List<Node> visited=new ArrayList<Node>();", null, 1, null); // 3
    sc.addCodeLine("toDoList.add(start);", "comment2Anchor", 1, null); // 4
    sc.addCodeLine("while(!toDoList.isEmpty()) {", null, 1, null); // 5
    sc.addCodeLine("Node current=toDoList.get(0);", null, 2, null); // 6
    sc.addCodeLine("visited.add(current);", null, 2, null); // 7
    sc.addCodeLine("toDoList.remove(current);", null, 2, null); // 8
    sc.addCodeLine("for(Node neighbor : current.neighbors()) {", null, 2, null); // 9
    sc.addCodeLine("if(neighbor.equals(target))", null, 3, null); // 10
    sc.addCodeLine("return true;", null, 4, null); // 11
    sc.addCodeLine(
        "if(!toDoList.contains(neighbor) && !visited.contains(neighbor))",
        null, 3, null); // 12
    sc.addCodeLine("toDoList.add(neighbor);", null, 4, null); // 13
    sc.addCodeLine("}", null, 2, null); // 14
    sc.addCodeLine("}", null, 1, null); // 15
    sc.addCodeLine("return false;", null, 1, null); // 16
    sc.addCodeLine("}", null, 0, null); // 17

    lang.nextStep();

  }

  public void writeInitials(Vector<String> toDo) {
    graph.hideNode(nodesList.indexOf(start), null, null);
    graph.hideNode(nodesList.indexOf(target), null, null);
    sc.highlight(0);
    Text comment1 = lang.newText(new Offset(20, 0, sc, "NE"),
        "// Is there a path between " + start + " and " + target, "comment1",
        null, comments);

    nextStep(null);
    comment1.setText("", null, null);
    sc.toggleHighlight(0, 1);
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.ITALIC, 18));
    lang.newText(new Offset(50, 0, graph, "NE"), "To Do List: ", "toDoList",
        null, tp);

    nextStep(null);
    sc.toggleHighlight(1, 2);
    lang.newText(new Offset(50, 100, graph, "NE"), "Visited List: ", "visited",
        null, tp);

    nextStep(null);
    sc.toggleHighlight(2, 3);
    toDoGraph = createGraphFromNodes(toDo, true);
    highlightedNode = nodesList.indexOf(start);
    graph2.highlightNode(highlightedNode, null, null);
    Text comment2 = lang.newText(new Offset(20, 40, sc, "NE"), "// Start with "
        + start, "comment2", null, comments);

    nextStep(null);
    comment2.setText("", null, null);
    sc.toggleHighlight(3, 4);
    traversedNodes = 0;
    TextProperties traversedProps = new TextProperties();
    traversedProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(120,
        120, 120));
    traversedText = lang.newText(new Offset(0, 40, graph, "S"), "",
        "traversedText", null, traversedProps);
  }

  public void writeNextNode(String current, Vector<String> visited,
      Vector<String> toDo) {
    nextStep("Node " + current);
    sc.toggleHighlight(8, 5);
    sc.toggleHighlight(4, 5);
    graph.unhighlightNode(highlightedNode, null, null);
    graph2.unhighlightNode(highlightedNode, null, null);
    highlightedNode = nodesList.indexOf(current);
    graph.highlightNode(highlightedNode, null, null);
    graph2.highlightNode(highlightedNode, null, null);

    nextStep(null);
    sc.toggleHighlight(5, 6);
    // Graph visitedGraph =
    createGraphFromNodes(visited, false);

    nextStep(null);
    sc.toggleHighlight(6, 7);
    toDoGraph.hide();
    if (toDo.size() > 0)
      toDoGraph = createGraphFromNodes(toDo, true);

    nextStep(null);
    sc.toggleHighlight(7, 8);
  }

  public void writeAddNeighborToTODO(Vector<String> toDo, String neighbor) {
    nextStep(null);
    comment3.setText("", null, null);
    sc.toggleHighlight(9, 11);

    nextStep(null);
    sc.toggleHighlight(11, 12);
    if (toDoGraph.getSize() > 0)
      toDoGraph.hide();
    toDoGraph = createGraphFromNodes(toDo, true);

    nextStep(null);
    sc.toggleHighlight(12, 8);
  }

  /**
   * 
   * @param nodes
   * @param isToDoGraph
   *          if it is a graph representing the toDo elements or visited
   *          elements
   * @return a new graph with no edges but nodes only, representing the elements
   *         in the toDo list or the visited list
   */
  private Graph createGraphFromNodes(Vector<String> nodes, boolean isToDoGraph) {
    Graph newGraph;

    int[][] matrix = new int[nodes.size()][nodes.size()];
    Node[] positions = new Node[nodes.size()];
    String[] labels = new String[nodes.size()];
    int xCoordinate = 50;
    int yCoordinate = 20;
    if (!isToDoGraph)
      yCoordinate = 130;
    for (int i = 0; i < nodes.size(); i++) {
      positions[i] = new Offset(xCoordinate, yCoordinate, graph, "NE");
      xCoordinate += 30;
      labels[i] = nodes.get(i);
      for (int j = 0; j < nodes.size(); j++) {
        matrix[i][j] = 0;
      }
    }
    if (isToDoGraph) {
      for (String node : labels) {
        graph.hideNode(nodesList.indexOf(node), null, null);
        graph2.hideNode(nodesList.indexOf(node), null, null);
      }
      GraphProperties gp = new GraphProperties();
      gp.set(AnimationPropertiesKeys.FILL_PROPERTY, todo_nodes_color);
      newGraph = lang.newGraph("toDoGraph" + graphCounter, matrix, positions,
          labels, null, gp);
    } else {
      for (String node : labels) {
        graph.hideNode(nodesList.indexOf(node), null, null);
        graph2.hideNode(nodesList.indexOf(node), null, null);
        graph3.highlightNode(nodesList.indexOf(node), null, null);
      }
      GraphProperties gp = new GraphProperties();
      gp.set(AnimationPropertiesKeys.FILL_PROPERTY, visited_nodes_color);
      newGraph = lang.newGraph("visitedGraph" + graphCounter, matrix,
          positions, labels, null, gp);
    }

    graphCounter++;
    return newGraph;
  }

  public void writeDefaultForNeighbor(String current, String neighbor)
      throws IllegalDirectionException {
    traversedNodes++;
    Node first = coordinates[nodesList.indexOf(current)];
    Node second = coordinates[nodesList.indexOf(neighbor)];
    Polyline path = lang
        .newPolyline(new Node[] { first, second }, "line", null);
    path.hide(new TicksTiming(0));

    nextStep(null);
    Text anchor = lang.newText(first, "", "ha", null);
    animateTransition(path, anchor, neighbor, current);
    traversedText.setText("Traversed Nodes: " + traversedNodes, null, null);
    comment3 = lang.newText(new Offset(20, 145, sc, "NE"), "// Try neighbor "
        + neighbor + " of node " + current, "comment3", new TicksTiming(55),
        comments);
  }

  private void animateTransition(Polyline path, Text anchor, String neighbor,
      String current) throws IllegalDirectionException {
    TriangleProperties trP = new TriangleProperties();
    trP.set(AnimationPropertiesKeys.FILL_PROPERTY, node_highlight_color);
    trP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    Triangle t = lang.newTriangle(new Offset(-3, -3, anchor, "SE"), new Offset(
        3, -3, anchor, "SE"), new Offset(0, 3, anchor, "SE"), "arrow", null,
        trP);

    t.moveVia("C", "translate", path, new TicksTiming(20), new TicksTiming(50));
    t.hide(new TicksTiming(55));
    sc.toggleHighlight(8, 0, false, 9, 0, new TicksTiming(55), null);
    graph.unhighlightNode(highlightedNode, null, null);
    graph2.unhighlightNode(highlightedNode, null, null);
    highlightedNode = nodesList.indexOf(neighbor);
    graph2.highlightNode(highlightedNode, new TicksTiming(55), null);
    graph.highlightNode(highlightedNode, new TicksTiming(55), null);
  }

  public void writeSuccess() {
    nextStep(null);
    comment3.setText("", null, null);
    sc.toggleHighlight(9, 10);
    lang.newText(
        new Offset(20, 160, sc, "NE"),
        "// There is indeed path between node " + start + " and node " + target,
        "comment4", null, comments);
    lang.nextStep();
    addFinalComment();

  }

  public void writeFailed() {
    nextStep(null);
    sc.toggleHighlight(8, 4);

    nextStep(null);
    sc.toggleHighlight(4, 15);
    lang.newText(new Offset(20, 240, sc, "NE"),
        "// There is no path between node " + start + " and node " + target,
        "comment5", null, comments);

    lang.nextStep();
    addFinalComment();
  }

  private void addFinalComment() {
    TextProperties finalCommentProps = new TextProperties();
    finalCommentProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    lang.newText(new Offset(0, 20, sc, "SW"), "Done - " + stepCounter
        + " instructions were needed to complete the path search between "
        + start + " and " + target + ".", "comment6", null, finalCommentProps);

  }

  public void writeNextNeighborIteration() {
    nextStep(null);
    comment3.setText("", null, null);
    sc.toggleHighlight(9, 11);

    nextStep(null);
    sc.toggleHighlight(11, 8);
  }

  private void nextStep(String label) {
    stepCounter++;
    lang.nextStep(label);
  }

  public void writeMatrixError() {
    TextProperties finalCommentProps = new TextProperties();
    finalCommentProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    lang.newText(
        new Offset(0, 20, tittle, "SW"),
        "Error occured. Either matrix not symmetric or Labels count does not correspond to matrix.",
        "comment6", null, finalCommentProps);
  }

  public void writeDuplicatesError() {
    TextProperties finalCommentProps = new TextProperties();
    finalCommentProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    lang.newText(new Offset(0, 20, tittle, "SW"),
        "Error occured. Duplicated Labels detected.", "comment6", null,
        finalCommentProps);

  }

  public void writeCoordinatesError() {
    TextProperties finalCommentProps = new TextProperties();
    finalCommentProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    lang.newText(new Offset(0, 20, tittle, "SW"),
        "Error occured. Invalid Coordinates.", "comment6", null,
        finalCommentProps);
  }
}
