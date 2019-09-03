package generators.graph.bellmanford;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class BellmanFord extends AnnotatedAlgorithm {

  // the graph
  private Graph                graph;

  // Priority queue for the algorithm
  private ArrayList<Edge>      edges;

  // x-offset of the graph
  private int                  x_offset    = 450;

  // y-offset of the graph
  private int                  y_offset    = 20;

  private SourceCodeProperties scProps;

  // edges column of the table
  private SourceCode[]         table;

  private int[]                distances;

  private int[]                predecessor;

  private static final String  DESCRIPTION = "The Bellman-Ford algorithm, a label correcting algorithm, computes single-source shortest paths in a weighted digraph (where some of the edge weights may be negative). (wikipedia)";

  public BellmanFord() {
    lang = new AnimalScript("Bellman-Ford", "David Marx", 640, 480);
    lang.setStepMode(true);
  }

  public BellmanFord(Language l) {
    lang = l;
    lang.setStepMode(true);
  }

  @Override
  public String getAnnotatedSrc() {
    return "for each vertex:										@label(\"loop1\")	\n"
        + "    distance(v) := infinity, predecessor(v) := none	@label(\"initDistances\")	\n"
        + "    distance(s) := 0									@label(\"initStart\")	\n"
        + "repeat n - 1 times									@label(\"repeat\")	\n"
        + "  for each edge (u,v)								@label(\"loop2\")	\n"
        + "    if distance(u) + weight(u,v) < distance(v)		@label(\"test\")	\n"
        + "      distance(v) := distance(u) + weight(u,v)		@label(\"update1\")	\n"
        + "      predecessor(v) := u	 						@label(\"update2\") \n"
        + "for each edge (u,v)									@label(\"loop3\")	\n"
        + "   if distance(u) + weight(u,v) < distance(v)		@label(\"test2\")	\n"
        + "     STOP: Graph contains a negative-weight cycle	@label(\"stop\")	\n";
  }

  @Override
  public String generate(AnimationPropertiesContainer anim,
      Hashtable<String, Object> args) {

    init();
    sourceCode.hide();
    showIntro();
    lang.nextStep();

    // show source code and graph
    showGraph(args);
    lang.nextStep();

    // show table
    showTable();
    exec("loop1");
    lang.nextStep();

    initialize();
    lang.nextStep();

    run();

    return lang.toString();
  }

  private void showIntro() {
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode intro = lang.newSourceCode(new Coordinates(50, 200), "intro",
        null, scProps);

    intro
        .addCodeLine(
            "The Bellman-Ford algorithm, a label correcting algorithm, computes single-source shortest",
            null, 0, null);
    intro
        .addCodeLine(
            "paths in a weighted digraph (where some of the edge weights may be negative).",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "Dijkstra's algorithm solves the same problem with a lower running time, but requires edge",
            null, 0, null);
    intro
        .addCodeLine(
            "weights to be non-negative. Thus, Bellman-Ford is usually used only when there are negative edge weights.",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine(
        "- Wikipedia (http://en.wikipedia.org/wiki/Bellman-Ford_algorithm):",
        null, 0, null);
    lang.nextStep();

    intro.hide();
    sourceCode.show();
  }

  @Override
  public void init() {
    super.init();

    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    sourceCode = lang.newSourceCode(new Coordinates(20, 20), "sourceCode",
        null, scProps);

    vars.declare("String", "u");
    vars.declare("String", "v");
    vars.declare("String", "distance-U");
    vars.declare("String", "weight-U-V");
    vars.declare("String", "distance-V");

    parse();
  }

  /**
   * Sets distances of all nodes to inf and predecessor to null. Sets distance
   * of start node to 0.
   */
  private void initialize() {
    exec("initDistances");
    int startknoten = 0;
    distances = new int[graph.getSize()];
    predecessor = new int[graph.getSize()];

    for (int i = 0; i < graph.getSize(); i++) {
      if (graph.getStartNode().equals(graph.getNode(i))) {
        startknoten = i;
      }
      table[i].addCodeLine("inf/-", "", 0, null);
      distances[i] = Integer.MAX_VALUE;
      predecessor[i] = -1;
    }
    lang.nextStep();

    exec("initStart");
    distances[startknoten] = 0;
    table[startknoten].addCodeLine("0/-", "", 0, null);
    for (int i = 0; i < graph.getSize(); i++) {
      if (!graph.getStartNode().equals(graph.getNode(i))) {
        table[i].addCodeLine("inf/-", "", 0, null);
      }
    }
    graph.highlightNode(startknoten, null, null);

  }

  /**
   * Copies the given input Graph and displays it
   * 
   * @param args
   */
  private void showGraph(Hashtable<String, Object> args) {
    graph = (Graph) args.get("graph");

    edges = new ArrayList<Edge>();
    int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
    for (int i = 0; i < adjacencyMatrix.length; i++) {
      for (int j = 0; j < adjacencyMatrix[i].length; j++) {
        if (adjacencyMatrix[i][j] > 0) {
          edges.add(new Edge(i, j, adjacencyMatrix[i][j]));
        }
      }
    }
    Node[] graphNodes = new Node[graph.getSize()];
    String[] labels = new String[graph.getSize()];
    for (int i = 0; i < graph.getSize(); i++) {
      graphNodes[i] = graph.getNode(i);
      labels[i] = graph.getNodeLabel(i);
    }
    GraphProperties gp = new GraphProperties();
    gp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
    gp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    gp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
    gp.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, graph.getProperties()
        .get(AnimationPropertiesKeys.DIRECTED_PROPERTY));
    gp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, graph.getProperties()
        .get(AnimationPropertiesKeys.WEIGHTED_PROPERTY));

    Node startknoten = graph.getStartNode();
    Node zielknoten = graph.getTargetNode();

    graph = lang.newGraph("graph", adjacencyMatrix, graphNodes, labels, null,
        gp);
    graph.setStartNode(startknoten);
    graph.setTargetNode(zielknoten);

    graph.moveBy("Translate", x_offset, y_offset, null, null);
  }

  private void showTable() {

    table = new SourceCode[graph.getSize()];

    for (int i = 0; i < graph.getSize(); i++) {
      table[i] = lang.newSourceCode(new Coordinates(i * 60 + 20, 260),
          new Integer(i).toString(), null, scProps);
      table[i].addCodeLine(new Character((char) (65 + i)).toString(), "", 0,
          null);

    }

  }

  /**
   * Runs the algorithm
   */
  private void run() {

    exec("repeat");
    lang.nextStep();
    exec("loop2");
    lang.nextStep();
    exec("test");
    lang.nextStep();

    // Shows the values for "if distance(u) + weight(u,v) < distance(v)"
    Text variables = lang.newText(new Coordinates(370, 113), "", "box", null);
    variables.setFont(new Font("Monospaced", Font.PLAIN, 18), null, null);

    for (int i = 0; i < graph.getSize(); i++) {

      for (int j = 0; j < edges.size(); j++) {

        if (j > 0) {
          Edge lastEdge = edges.get(j - 1);
          graph.unhighlightEdge(lastEdge.from, lastEdge.to, null, null);
        }

        // Highlight current Edge and display values for
        // "if distance(u) + weight(u,v) < distance(v)"
        exec("test");
        Edge e = edges.get(j);

        setVariables(e);
        graph.highlightEdge(e.from, e.to, null, null);
        String newText = handleInf(distances[e.from]) + " + " + e.weight
            + " < " + handleInf(distances[e.to]) + " ?";
        variables.setText(newText, null, null);
        lang.nextStep();

        // If a distance can be improved, show the new
        // distance/predecessor in the table.
        if (distances[e.from] != Integer.MAX_VALUE
            && distances[e.from] + e.weight < distances[e.to]) {
          distances[e.to] = distances[e.from] + e.weight;
          predecessor[e.to] = e.from;
          table[e.to].addCodeLine(handleInf(distances[e.to]) + "/"
              + handlePred(predecessor[e.to]), "", 0, null);
          graph.highlightNode(e.to, null, null);
          exec("update1");
          lang.nextStep();

          exec("update2");
          lang.nextStep();

          graph.unhighlightNode(e.to, null, null);
        }

      }

      Edge lastEdge = edges.get(edges.size() - 1);
      graph.unhighlightEdge(lastEdge.from, lastEdge.to, null, null);

    }

    CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
        "distance", distances));// /////////////////////

    variables.hide();
    exec("loop3");
    lang.nextStep();

    // search for negative edges
    for (int j = 0; j < edges.size(); j++) {

      if (j > 0) {
        Edge lastEdge = edges.get(j - 1);
        graph.unhighlightEdge(lastEdge.from, lastEdge.to, null, null);
      }

      Edge e = edges.get(j);
      graph.highlightEdge(e.from, e.to, null, null);

      // negative edge found. Display message.
      if (distances[e.from] != Integer.MAX_VALUE
          && distances[e.from] + e.weight < distances[e.to]) {
        Text negativecycle = lang.newText(new Coordinates(360, 110),
            "Negative Cycle detected!", "box", null);
        negativecycle.setFont(new Font("Monospaced", Font.PLAIN, 18), null,
            null);
        break;
      }

      Edge lastEdge = edges.get(edges.size() - 1);
      graph.unhighlightEdge(lastEdge.from, lastEdge.to, null, null);

      lang.nextStep();

    }

  }

  private void setVariables(Edge e) {

    vars.set("u", new Character((char) (e.from + 65)).toString());
    vars.set("v", new Character((char) (e.to + 65)).toString());
    vars.set("weight-U-V", new Integer(e.weight).toString());
    vars.set("distance-U", handleInf(distances[e.from]));
    vars.set("distance-V", handleInf(distances[e.to]));
  }

  /**
   * Handles display for Predecessor. "null" should be "-"
   */
  private String handlePred(int i) {
    if (i == -1) {
      return "-";
    } else {
      return new Character((char) (i + 65)).toString();
    }
  }

  /**
   * Handles display for Predecessor. "Integer.MAX_VALUE" should be "inf"
   */
  private String handleInf(int i) {
    if (i == Integer.MAX_VALUE) {
      return "inf";
    } else {
      return new Integer(i).toString();
    }
  }

  /**
   * Helper class
   */
  public class Edge implements Comparable<Edge> {

    int weight;
    int from;
    int to;

    public Edge(int from, int to, int weight) {
      this.weight = weight;
      this.from = from;
      this.to = to;
    }

    @Override
    public int compareTo(Edge o) {
      if (this.weight < o.weight)
        return -1;
      else if (this.weight == o.weight)
        return 0;
      else
        return 1;
    }

    public String toString() {
      return (char) (from + 65) + "->" + (char) (to + 65);
    }

  }

  @Override
  public String getAlgorithmName() {
    return "Bellman-Ford Algorithm";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  @Override
  public String getName() {
    return "BellmanFord";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public String getAnimationAuthor() {
    return "David Marx";
  }

}
