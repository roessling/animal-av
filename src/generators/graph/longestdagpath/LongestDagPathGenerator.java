package generators.graph.longestdagpath;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpers.DirectedGraph;
import generators.graph.helpers.LongestDagPathCreator;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;

public class LongestDagPathGenerator implements ValidatingGenerator {
  private Language lang;
  private Color    highlightColorSource;
  private Color    highlightColorAdjacency;
  private int[][]  adjacencyMatrix;

  public void init() {
    lang = new AnimalScript("Longest path in a DAG [EN]",
        "Christian Hollubetz", 800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    highlightColorSource = (Color) primitives.get("highlightColorSource");
    highlightColorAdjacency = (Color) primitives.get("highlightColorAdjacency");
    adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");

    LongestDagPathCreator ldpc = new LongestDagPathCreator(lang,
        adjacencyMatrix, highlightColorSource, highlightColorAdjacency);

    try {
      lang = ldpc.perform();
    } catch (Exception e) {
      e.printStackTrace();
    }

    lang.finalizeGeneration();

    return lang.toString();
  }

  public String getName() {
    return "Longest path in a DAG";
  }

  public String getAlgorithmName() {
    return "Longest path in a DAG";
  }

  public String getAnimationAuthor() {
    return "Christian Hollubetz";
  }

  public String getDescription() {
    return "This animation demonstrates the process of an algorithm finding the length of the longest path in a DAG."
        + "\n"
        + "<br><br>"
        + "\n"
        + "A DAG is an Directed Acyclic Graph. This restriction is important to ensure, that an algorithm can perform in polynomial time."
        + "\n"
        + "<br><br>"
        + "\n"
        + "The proposed algorithm can only perform on such graphs. Otherwise it will endlessly loop."
        + "\n"
        + "The general problem is np-hard."
        + "\n"
        + "The input for this generator has to fulfill a few conditions. The input is made via an adjacency matrix. The adjacency matrix itself is modelled through an int[M][N], where M = N. Each int[m_i] contains the n elements of the m_i-th row. The value of int[v_m][v_n] indicates the weight of the edge between v_m and v_n. If there should be no edge between these two node, the value must be 0. Negative values are not allowed.";
  }

  public String getCodeExample() {
    return "longestPath(DirectedGraph dag, Node firstNode) {" + "\n"
        + "  Nodes V = dag.allNodes().without(firstNode)" + "\n"
        + "  Integers p" + "\n" + "  Integers x" + "\n" + "  Nodes Q\");"
        + "\n" + "\n" + "  Nodes Q = {}" + "\n" + "\n"
        + "  foreach (v_i in V) {" + "\n" + "    p_i = indegreeOf(v_i))" + "\n"
        + "    x_i = 0;" + "\n" + "  }" + "\n" + "\n" + "  Q.add(firstNode)"
        + "\n" + "\n" + "  while (Q.size() != 0) {" + "\n"
        + "    v_i = pickAny(Q)" + "\n" + "    Q.remove(v_i)" + "\n"
        + "    foreach ((v_i, v_j) in dag.allEdges()) {" + "\n"
        + "      x_j = max( x_j , x_i + weight(edge(x_i, x_j)) )" + "\n"
        + "      --p_j" + "\n" + "      if (p_j <= 0)" + "\n"
        + "        Q.add(v_j);" + "\n" + "    }" + "\n" + "  }" + "\n" + "}";
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

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {

    // no empty input
    if (arg1.get("adjacencyMatrix") == null)
      throw new IllegalArgumentException(
          "The adjacency matrix is not allowed to be empty. Please specify it.");

    int[][] adjacencyMatrix = (int[][]) arg1.get("adjacencyMatrix");

    // no unusable small input
    if (adjacencyMatrix.length < 1 || adjacencyMatrix[0].length < 1)
      throw new IllegalArgumentException(
          "The adjacency matrix is unusable small. It must at least be 1x1, but should be larger than 1x1.");

    // adjacency matrix is square
    if (!(adjacencyMatrix.length == adjacencyMatrix[0].length))
      throw new IllegalArgumentException(
          "The adjacency matrix must be square. Currently its dimension is "
              + adjacencyMatrix.length + "x" + adjacencyMatrix[0].length + ".");

    DirectedGraph dg = LongestDagPathCreator.buildInputGraph(adjacencyMatrix);

    // no negative weighted edge
    if (dg.hasNegativeEdgeWeights())
      throw new IllegalArgumentException(
          "The input contains negative weighted edges. Please choose only positive values or choose zero, if there should be no edge.");

    // no cycle
    if (dg.hasCycle())
      throw new IllegalArgumentException(
          "The input contains a cycle. The algorithm can only perform on directed acyclic graphs (DAGs).");

    // the input fulfills all the needs and with it is a correct input
    return true;
  }

}