package generators.graph.helpers;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import algoanim.primitives.generators.Language;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class LongestDagPathCreator {

  private Language lang;

  private int[][]  input;
  private Color    highlightColorSource;
  private Color    highlightColorAdjacency;

  /**
   * Creates a class that is able to visualize the behavior of an algorithm
   * finding the length of the longest path in a dag.
   */
  public LongestDagPathCreator(Language lang, int[][] input,
      Color highlightColorSource, Color highlightColorAdjacency) {
    this.lang = lang;
    lang.setStepMode(true);

    this.input = input;
    this.highlightColorSource = highlightColorSource;
    this.highlightColorAdjacency = highlightColorAdjacency;
  }

  /**
   * Performs the longest path in a DAG algorithm on the given graph and returns
   * the language containing all the commands.
   * 
   * @return the language containing all the commands
   * @throws Exception
   */
  public Language perform() throws Exception {
    new Label(new Position(5, 5), 30, 400, lang,
        "Longest path in a DAG - Algorithm");

    DirectedGraph inputGraph = buildInputGraph(input);
    Map<Node, Integer> x = buildInputX(inputGraph);
    Node first = determineFirstNode(inputGraph);

    intro();
    longestPath(inputGraph, first, x);
    outro();

    return lang;
  }

  /**
   * Determines the first node in a way of searching a node with no incoming
   * edges.
   * 
   * @param inputGraph
   *          the graph
   * @return one first node
   */
  private Node determineFirstNode(DirectedGraph inputGraph) {
    for (Node currentNode : inputGraph.getNodes())
      if (inputGraph.getIndegreeOf(currentNode) == 0)
        return currentNode;
    return null;
  }

  /**
   * Builds and returns a graph out of the given adjacency matrix.
   * 
   * @return the graph
   */
  public static DirectedGraph buildInputGraph(int[][] input) {
    DirectedGraph result = new DirectedGraph();
    for (int r = 0; r < input.length; r++)
      for (int c = 0; c < input[0].length; c++)
        if (input[r][c] != 0)
          result.addEdge("" + r, "" + c, input[r][c]);
    return result;
  }

  /**
   * Initializes a Map called X for the algorithm.
   * 
   * @param inputGraph
   *          the graph for which X should be produced
   * @return X for the graph
   */
  private Map<Node, Integer> buildInputX(DirectedGraph inputGraph) {
    Map<Node, Integer> x = new TreeMap<Node, Integer>();

    // x_0, ..., x_n = 0
    for (Node currentNode : inputGraph.getNodes())
      x.put(currentNode, 0);

    return x;
  }

  /**
   * Visualizes the intro.
   */
  private void intro() {
    TextBlock tb = new TextBlock(new Position(5, 45), lang, 20, 75);
    tb.insertText("This animation demonstrates the process of an algorithm finding the length of the longest path in a DAG.\nA DAG is an Directed Acyclic Graph. This restriction is important to ensure, that an algorithm can perform in polynomial time. The proposed algorithm can only perform on such graphs. Otherwise it will endlessly loop.\nThe general problem is np-hard.");
    lang.nextStep("Intro");
    tb.hide();
  }

  /**
   * Visualizes the outro.
   */
  private void outro() {
    TextBlock tb = new TextBlock(new Position(5, 45), lang, 20, 75);
    tb.insertText("As we could see, the algorithm performed as wanted.\n"
        + "The algorithm performs in O(n^2), because the graph is as DAG.\n"
        + "Other algorithms, that perform on any graphs, are np-hard.\n"
        + "It is obvious, that a circle, whose included edges have positive weights, keep the algorithm calculating, because a path through such a circle increases the length of the path continously.");
    lang.nextStep("Outro");
    tb.hide();
  }

  /**
   * Performs the longest path in a dag algorithm to the given values.
   * 
   * @param dg
   *          a graph that should be a dag
   * @param firstNode
   *          the first node from which the algorithm will start to perform
   * @param x
   *          the initialized x-valkues
   */
  public void longestPath(DirectedGraph dg, Node firstNode, Map<Node, Integer> x) {
    SourceCodeWrapper scw = prepareSourceCodes(new Position(5, 200), lang);
    Console console = new Console(new Position(5, 45), lang, 7);
    AdjacencyMatrix am = new AdjacencyMatrix(dg, firstNode, lang, new Position(
        450, 5), highlightColorAdjacency);
    TableWrapper tw = new TableWrapper(lang, new Position(am.getSW().getX(), am
        .getSW().getY() + 30), dg.getNodes().size() - 1, dg.getNodes().size());
    console.writeLine("we start the algorithm by calling longestPath");
    scw.markSourceCodeLines(0);
    lang.nextStep("Introduction");

    console.writeLine("the graph will be visualized by an adjacency matrix");
    am.draw();
    lang.nextStep();

    console
        .writeLine("and there are a few helper variable, we will visualize in a table");
    tw.draw();
    scw.markSourceCodeLines(2, 3, 4);
    lang.nextStep();

    List<Node> v_withoutFirst = new LinkedList<Node>(dg.getNodes());
    v_withoutFirst.remove(firstNode);

    Map<Node, Integer> p = new TreeMap<Node, Integer>();

    // p_1, ..., p_n = p_i.indegree()
    for (Node n_i : v_withoutFirst)
      p.put(n_i, dg.getIndegreeOf(n_i));

    // Q = {v_0}
    Set<Node> Q = new TreeSet<Node>();

    console.writeLine("Q is initialized empty");
    scw.markSourceCodeLines(6);
    lang.nextStep("Initialization");

    tw.addQ(Q);
    lang.nextStep();

    console
        .writeLine("every p_i will be initialized to the number of incoming edges to v_i");
    scw.markSourceCodeLines(8, 9);
    lang.nextStep();

    tw.addP(p.values());
    lang.nextStep();

    console.writeLine("every x_i will be initialized to zero");
    scw.markSourceCodeLines(8, 10);
    lang.nextStep();

    tw.addX(x.values());
    lang.nextStep();

    Q.add(firstNode);

    console.writeLine("the first node gets added to Q");
    scw.markSourceCodeLines(13);
    lang.nextStep();

    tw.addQ(Q);
    lang.nextStep();

    console.writeLine("while Q is not empty");
    scw.markSourceCodeLines(15);
    lang.nextStep("Main-Loop");

    while (Q.size() != 0) {
      am.unHighlightAll();
      Node v_i = getFirst(Q);
      console.writeLine("get one node of Q");
      am.highlightFromNode(v_i);
      scw.markSourceCodeLines(15, 16);
      lang.nextStep();
      Q.remove(v_i);
      console
          .writeLine("and remove it from Q (this can be seen in the next iteration)");
      scw.markSourceCodeLines(15, 17);
      lang.nextStep();
      Set<Pair<Node, Integer>> outGoingNeighborsOfVI = dg
          .getOutgoingNeighbors(v_i);
      console.writeLine("for each edge, that begins in v_i");
      scw.markSourceCodeLines(15, 18);
      for (Pair<Node, Integer> v_j : outGoingNeighborsOfVI)
        am.highlightToNode(v_j.getFirst());
      lang.nextStep();

      for (Pair<Node, Integer> v_j : outGoingNeighborsOfVI) {

        x.put(v_j.getFirst(),
            Math.max(x.get(v_j.getFirst()), x.get(v_i) + v_j.getSecond()));
        p.put(v_j.getFirst(), p.get(v_j.getFirst()) - 1);
        if (p.get(v_j.getFirst()) <= 0)
          Q.add(v_j.getFirst());
      }

      scw.markSourceCodeLines(15, 18, 19);
      console
          .writeLine("x_j gets the max of the current value and of x_i + the weight from v_i to v_j");
      tw.addX(x.values());
      lang.nextStep();

      scw.markSourceCodeLines(15, 18, 20);
      console
          .writeLine("p_j gets decreased by one, because one of it incoming edges was processed");
      tw.addP(p.values());
      lang.nextStep();

      scw.markSourceCodeLines(15, 18, 21, 22, 23);
      if (Q.size() > 0)
        console.writeLine("if p_j is now zero or smaller, it is added to Q");
      else {
        console
            .writeLine("now Q is empty, so the loop will not be executed again");
        am.unHighlightAll();
        scw.markSourceCodeLines(15);
      }
      tw.addQ(Q);
      if (Q.size() > 0)
        lang.nextStep();
      else
        lang.nextStep("Algorithm-Finish");
    }
    console
        .writeLine("on the last state of X (the line on the bottom), you can see");
    console.writeLine("the length of the longest path from v_"
        + firstNode.label + " to v_i");
    scw.markSourceCodeLines();
    lang.nextStep();
    scw.hide();
    console.hide();
    am.hide();
    tw.hide();
  }

  /**
   * Places the source code of lees algorithm on a given position.
   * 
   * @param position
   *          the position to place the source code
   * @param lang
   *          the language to draw on
   * @return the wrapper for the source code
   */
  private SourceCodeWrapper prepareSourceCodes(Position position, Language lang) {
    Coordinates position1 = new Coordinates(position.getX(), position.getY());

    SourceCodeProperties scp = new SourceCodeProperties();
    scp.set("bold", false);
    scp.set("color", new Color(50, 50, 100));
    scp.set("highlightColor", highlightColorSource);
    scp.set("hidden", false);
    // scp.set("font", "SansSerif");
    scp.set("name", "sourceCode");
    scp.set("contextColor", new Color(0, 0, 0));
    scp.set("italic", false);
    scp.set("depth", 1);
    scp.set("row", 1);
    scp.set("size", 10);
    scp.set("indentation", 1);
    List<String> sources = new LinkedList<String>();

    sources.add("longestPath(DirectedGraph dag, Node firstNode) {"); // 0
    sources.add("  Nodes V = dag.allNodes().without(firstNode)"); // 1
    sources.add("  Integers p"); // 2
    sources.add("  Integers x"); // 3
    sources.add("  Nodes Q"); // 4
    sources.add(""); // 5
    sources.add("  Nodes Q = {}"); // 6
    sources.add(""); // 7
    sources.add("  foreach (v_i in V) {"); // 8
    sources.add("    p_i = indegreeOf(v_i))"); // 9
    sources.add("    x_i = 0;"); // 10
    sources.add("  }"); // 11
    sources.add(""); // 12
    sources.add("  Q.add(firstNode)"); // 13
    sources.add(""); // 14
    sources.add("  while (Q.size() != 0) {"); // 15
    sources.add("    v_i = pickAny(Q)"); // 16
    sources.add("    Q.remove(v_i)"); // 17
    sources.add("    foreach ((v_i, v_j) in dag.allEdges()) {"); // 18
    sources.add("      x_j = max( x_j , x_i + weight(edge(x_i, x_j)) )"); // 19
    sources.add("      --p_j"); // 20
    sources.add("      if (p_j <= 0)"); // 21
    sources.add("        Q.add(v_j);"); // 22
    sources.add("    }"); // 23
    sources.add("  }"); // 24
    sources.add("}"); // 25

    return new SourceCodeWrapper(position1, lang, sources, scp);
  }

  /**
   * Helper function. Prints all the items if an iterable.
   * 
   * @param i
   *          the iterable containing the items
   */
  public static void printAllOf(Iterable<? extends Object> i) {
    for (Object o : i)
      System.out.println(o);
    System.out.println();
  }

  /**
   * Helper function. Returns the first item of an iterable.
   * 
   * @param s
   *          the iterable
   * @return the first item of the iterable
   */
  public static <T> T getFirst(Iterable<T> s) {
    try {
      for (T o : s)
        return o;
    } catch (Exception e) {
    }
    return null;
  }

}
