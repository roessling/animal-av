package generators.graph.kruskal;

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
import java.util.Iterator;
import java.util.Locale;
import java.util.PriorityQueue;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class Kruskal extends AnnotatedAlgorithm {

  // the graph
  private Graph                         graph;

  // Priority queue for the algorithm (used to sort the edges)
  private PriorityQueue<Edge>           edgesQueue;

  // Used after the first iteration on the priority queue
  private ArrayList<Edge>               edgesList;

  // helper data structure for the algorithm
  private ArrayList<ArrayList<Integer>> vertexSets;

  // Properties for the source code
  private SourceCodeProperties          scProps;

  // x-offset of the graph
  private int                           x_offset    = 520;

  // y-offset of the graph
  private int                           y_offset    = 20;

  // edges column of the table
  private SourceCode                    edgeColumn;

  // edges weight column of the table
  private SourceCode                    edgeWeight;

  // components column of the table
  private SourceCode                    componentsColumn;

  private static final String           DESCRIPTION = "Kruskal's algorithm is an algorithm in graph theory that finds a minimum spanning tree for a connected weighted graph. (wikipedia)";

  public Kruskal() {
    lang = new AnimalScript("Kruskal", "David Marx", 640, 480);
    lang.setStepMode(true);
  }

  public Kruskal(Language l) {
    lang = l;
    // set step mode
    lang.setStepMode(true);
  }

  @Override
  public String getAnnotatedSrc() {
    return "Sort all edges, from lowest to highest weight.	@label(\"sort_edges\")\n"
        + "Remove all edges	@label(\"remove_edges\")\n"
        + "Iterate over all Edges.	@label(\"iterate_edges\") \n"
        + "If adding the current edge does not produce a cycle, add it to the resulting tree. @label(\"test_edge\")  \n";
  }

  @Override
  public String generate(AnimationPropertiesContainer anim,
      Hashtable<String, Object> args) {

    // init shows the source code in step 1, so hide it first and show an intro
    init();
    sourceCode.hide();
    showIntro();
    lang.nextStep();

    // show table and graph
    showGraph(args);
    showTable();
    lang.nextStep();

    // "Sort all edges, from lowest to highest weight."
    exec("sort_edges");
    lang.nextStep();

    // fill the table with the edges.
    showSortedEdges();
    lang.nextStep();

    // "Remove all Edges"
    exec("remove_edges");
    lang.nextStep();
    hideEdges();
    lang.nextStep();

    // "Iterate over all Edges."
    exec("iterate_edges");
    lang.nextStep();

    // "If adding the current edge does not produce a cycle, add it to the resulting tree."
    exec("test_edge");
    lang.nextStep();

    // run the algorithm
    run();
    lang.nextStep();

    // show outro
    showOutro();

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
            "Kruskal's algorithm is an algorithm in graph theory that finds a minimum spanning",
            null, 0, null);
    intro.addCodeLine("tree for a connected weighted graph.", null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "This means it finds a subset of the edges that forms a tree that includes",
            null, 0, null);
    intro
        .addCodeLine(
            "every vertex, where the total weight of all the edges in the tree is minimized.",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine(
        "- Wikipedia (http://en.wikipedia.org/wiki/Kruskal's_algorithm):",
        null, 0, null);
    lang.nextStep();

    intro.hide();
    sourceCode.show();

  }

  /**
   * Copies the given input Graph and displays it
   * 
   * @param args
   */
  private void showGraph(Hashtable<String, Object> args) {
    graph = (Graph) args.get("graph");

    edgesQueue = new PriorityQueue<Edge>();
    int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
    for (int i = 0; i < adjacencyMatrix.length; i++) {
      for (int j = 0; j < adjacencyMatrix[i].length; j++) {
        if (adjacencyMatrix[i][j] > 0) {
          Edge e = new Edge(i, j, adjacencyMatrix[i][j]);
          edgesQueue.add(e);
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

    // Set weighted default to true.
    gp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, graph.getProperties()
        .get(AnimationPropertiesKeys.WEIGHTED_PROPERTY));
    graph = lang.newGraph("graph", adjacencyMatrix, graphNodes, labels, null,
        gp);

    graph.moveBy("Translate", x_offset, y_offset, null, null);

  }

  /**
   * hides all edges in the graph
   */
  private void hideEdges() {
    for (Iterator<Edge> iterator = edgesList.iterator(); iterator.hasNext();) {
      Edge e = iterator.next();
      graph.hideEdge(e.from, e.to, null, null);
    }
  }

  private void showTable() {

    edgeWeight = lang.newSourceCode(new Coordinates(20, 170), "sourceCode",
        null, scProps);

    edgeColumn = lang.newSourceCode(new Coordinates(100, 170), "sourceCode",
        null, scProps);

    componentsColumn = lang.newSourceCode(new Coordinates(180, 170),
        "sourceCode", null, scProps);

    edgeWeight.addCodeLine("weight", "", 0, null);
    edgeWeight.addCodeLine("------------------------------------------------",
        "", 0, null);

    edgeColumn.addCodeLine("edges", "", 0, null);
    edgeColumn.addCodeLine("", "", 0, null);

    componentsColumn.addCodeLine("components", "", 0, null);
    componentsColumn.addCodeLine("", "", 0, null);
  }

  /**
   * Prints all edges to the table, ascending in weight.
   */
  private void showSortedEdges() {
    /*
     * Iterate over the priority queue and display the edges in the table. Each
     * edge is put into another list for the second iteration
     */
    edgesList = new ArrayList<Edge>();
    while (!edgesQueue.isEmpty()) {
      Edge e = edgesQueue.poll();
      edgeWeight.addCodeLine(new Integer(e.weight).toString(), "", 0, null);
      edgeColumn.addCodeLine(e.toString(), "", 0, null);
      edgesList.add(e);
    }
    // String str [] = (String []) edgesList.toArray (new String [edgesList.size
    // ()]);
    for (Iterator<Edge> iterator = edgesList.iterator(); iterator.hasNext();) {
      Edge e = iterator.next();
      CheckpointUtils.checkpointEvent(this, "sortedEdges", new Variable(
          "edgefrom", graph.getNodeLabel(e.from)),
          new Variable("edgeto", graph.getNodeLabel(e.to)), new Variable(
              "weight", e.weight));// ////////////////////

    }
  }

  private void run() {
    vertexSets = new ArrayList<ArrayList<Integer>>();
    int currentLine = 2;
    for (Iterator<Edge> iterator = edgesList.iterator(); iterator.hasNext();) {
      Edge e = iterator.next();

      ArrayList<Integer> a = findSet(e.from);
      ArrayList<Integer> b = findSet(e.to);

      /*
       * Kruskals algorithm is implemented using a List of node-lists. Each
       * node-list is a tree. Iterating over the edges, these trees are created
       * / merged based on the edges endpoints A and B: - There are no trees
       * containing A or B: create new trees. - A can be found, B can't be
       * found: add B to the tree containing A (and vice versa) - A and B can be
       * found and they are not in the same tree: merge the trees.
       */
      if (a == null && b == null) {
        ArrayList<Integer> newSet = new ArrayList<Integer>();
        newSet.add(e.from);
        newSet.add(e.to);
        vertexSets.add(newSet);
        graph.highlightNode(e.from, null, null);
        graph.highlightNode(e.to, null, null);
        showEdge(e);
        CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
            "distance", graph.getNodeLabel(e.from)));// ////////////////////
        CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
            "distance", graph.getNodeLabel(e.to)));
      }
      if (a != null && b == null) {
        a.add(e.to);
        graph.highlightNode(e.to, null, null);
        showEdge(e);
        CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
            "distance", graph.getNodeLabel(e.to)));
      }
      if (a == null && b != null) {
        b.add(e.from);
        graph.highlightNode(e.from, null, null);
        showEdge(e);
        CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
            "distance", graph.getNodeLabel(e.from)));
      }
      if (a != null && b != null) {
        if (a != b) {
          a.addAll(b);
          vertexSets.remove(b);
          showEdge(e);
          CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
              "distance", graph.getNodeLabel(e.from)));// ////////////////////
          CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
              "distance", graph.getNodeLabel(e.to)));
        } else {
          edgeColumn.highlight(currentLine);
          graph.hideEdge(e.from, e.to, null, null);
          CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
              "distance", graph.getNodeLabel(e.from)));// ////////////////////
          CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
              "distance", graph.getNodeLabel(e.to)));
        }

      }

      componentsColumn.addCodeLine(componentsToString(), "", 0, null);
      lang.nextStep();
      currentLine++;

    }

  }

  private void showOutro() {
    graph.hide();
    edgeColumn.hide();
    edgeWeight.hide();
    componentsColumn.hide();
    sourceCode.hide();

    SourceCode outro = lang.newSourceCode(new Coordinates(20, 20), "intro",
        null, scProps);

    outro.addCodeLine("Running time:", null, 0, null);
    outro.addCodeLine("", null, 0, null);
    outro
        .addCodeLine(
            "The algorithm can be implemented using a priority queue. Thus Kruskal can run in O( E log E ).",
            null, 0, null);
    outro.addCodeLine("", null, 0, null);
    outro
        .addCodeLine(
            "For a detailled discussion about the running time, visit e.g. http://en.wikipedia.org/wiki/Kruskal's_algorithm",
            null, 0, null);

  }

  /**
   * Shows and highlights the edge
   */
  private void showEdge(Edge e) {
    graph.highlightEdge(e.from, e.to, null, null);
    graph.showEdge(e.from, e.to, null, null);
  }

  /**
   * Returns the current trees of the graph like: {A,B} {C}
   */
  private String componentsToString() {
    String result = "";
    for (int i = 0; i < vertexSets.size(); i++) {
      ArrayList<Integer> set = vertexSets.get(i);
      result += "{";
      for (int j = 0; j < set.size(); j++) {
        result += (char) (set.get(j) + 65);
        if (j < set.size() - 1) {
          result += ", ";
        }
      }
      result += "} ";
    }
    return result;
  }

  /**
   * Finds the set in vertexSets that contains the given node
   */
  private ArrayList<Integer> findSet(int node) {
    for (Iterator<ArrayList<Integer>> iterator = vertexSets.iterator(); iterator
        .hasNext();) {
      ArrayList<Integer> set = iterator.next();
      if (set.contains(node)) {
        return set;
      }
    }
    return null;
  }

  /**
   * helper class for the Algorithm
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
    return "Kruskal";
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
    return "Kruskal";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public String getAnimationAuthor() {
    return "David Marx";
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

    parse();
  }

}
