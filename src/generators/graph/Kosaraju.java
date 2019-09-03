package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayBasedStack;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.StackProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

// assumptions to AlgoAnimApi:
// -graph nodes are labeled A, B, ... consecutively
// -ArrayBasedStack is based on StringArray (it has no own name)

public class Kosaraju implements Generator {
  private Language             lang;
  private Graph                graph;
  private GraphProperties      graphProps;
  private SourceCodeProperties introAndOutro;
  private SourceCodeProperties sourceCode;
  private StackProperties      stackTwo;
  private StackProperties      stackOne;
  private RectProperties       headerBorder;
  private int                  graphOffsetX;
  private int                  graphOffsetY;
  private int                  transposedGraphOffsetY;
  private MatrixProperties     matrixProps;
  private TextProperties       header;
  private SourceCode           sc;

  private int                  vertexLabelAccessCounter;
  private int                  edgeIteratorListCounter;
  private int                  edgeIteratorMatrixCounter;

  private Timing               timing;

  public void init() {
    lang = new AnimalScript("Kosaraju [EN]", "Kai Schwierczek", 800, 600);
    lang.setStepMode(true);
    timing = new TicksTiming(50);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    graph = (Graph) primitives.get("graph");
    graphProps = (GraphProperties) props.getPropertiesByName("graphProps");
    introAndOutro = (SourceCodeProperties) props
        .getPropertiesByName("introAndOutro");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    stackTwo = (StackProperties) props.getPropertiesByName("stackTwo");
    stackOne = (StackProperties) props.getPropertiesByName("stackOne");
    headerBorder = (RectProperties) props.getPropertiesByName("headerBorder");
    matrixProps = (MatrixProperties) props
        .getPropertiesByName("accessCounterMatrix");
    graphOffsetX = (Integer) primitives.get("offsetGraphX");
    graphOffsetY = (Integer) primitives.get("offsetGraphY");
    transposedGraphOffsetY = (Integer) primitives.get("transposedGraphOffsetY");
    header = (TextProperties) props.getPropertiesByName("header");
    // override Font size and BOLD, because Properties UI doesn't support that
    header.set(
        AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(((Font) header.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .getName(), Font.BOLD, 24));

    graph.hide();
    Node[] movedNodes = new Node[graph.getSize()];
    String[] movedNodeLabels = new String[graph.getSize()];
    for (int i = 0; i < movedNodes.length; i++) {
      Coordinates oldNode = (Coordinates) graph.getNode(i);
      movedNodes[i] = new Coordinates(oldNode.getX() + graphOffsetX,
          oldNode.getY() + graphOffsetY);
      movedNodeLabels[i] = graph.getNodeLabel(i);
    }
    graph = lang.newGraph("movedGraph", graph.getAdjacencyMatrix(), movedNodes,
        movedNodeLabels, graph.getDisplayOptions(), graphProps);
    kosaraju(graph);

    return lang.toString();
  }

  public String getName() {
    return "Kosaraju [EN]";
  }

  public String getAlgorithmName() {
    return "Kosaraju's algorithm";
  }

  public String getAnimationAuthor() {
    return "Kai Schwierczek";
  }

  public String getDescription() {
    return "Kosaraju's algorithm is an algorithm to find strongly connected components (SCCs) in graphs."
        + "\n"
        + "It does so with two DFS runs and creating the transpose graph before the second run.";
  }

  public String getCodeExample() {
    return "public void kusaraju (Graph g) {"
        + "\n"
        + "	// order DFS run"
        + "\n"
        + "	Stack<Integer> stack = new Stack<Integer>();"
        + "\n"
        + "	boolean[] visited = new boolean[g.getVertexCount()];"
        + "\n"
        + "	for (int vertex = 0; vertex < g.getVertexCount(); vertex++)"
        + "\n"
        + "		if (!visited[vertex])"
        + "\n"
        + "			dfs(vertex, g, stack, visited);"
        + "\n"
        + "	// transpose"
        + "\n"
        + "	g.transpose();"
        + "\n"
        + "	// output DFS run"
        + "\n"
        + "	Stack<Integer> stack2 = new Stack<Integer>();"
        + "\n"
        + "	visited = new boolean[g.getVertexCount()];"
        + "\n"
        + "	while (!stack.isEmpty()) {"
        + "\n"
        + "		int vertex = stack.pop();"
        + "\n"
        + "		if (!visited[vertex]) {"
        + "\n"
        + "			dfs(vertex, g, stack2, visited);"
        + "\n"
        + "			System.out.print(\"Found SCC: \");"
        + "\n"
        + "			while (!stack2.isEmpty())\", null, 3, null); // 17"
        + "\n"
        + "				System.out.print(stack2.pop() + \" \");"
        + "\n"
        + "			System.out.println();"
        + "\n"
        + "		}"
        + "\n"
        + "	}"
        + "\n"
        + "}"
        + "\n"
        + "\n"
        + "public void dfs (int vertex, Graph g, Stack<Integer> stack, boolean[] visited) {"
        + "\n" + "	visited[vertex] = true;" + "\n"
        + "	for (int neighbor : g.getOutNeighbors(node))" + "\n"
        + "		if (!visited[neighbor])" + "\n"
        + "			dfs(neighbor, g, stack, visited);" + "\n" + "	stack.push(node)"
        + "\n" + "}" + "\n";
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
    return Generator.JAVA_OUTPUT;
  }

  public static void main(String[] args) {
    // call constructor and init
    Kosaraju kosaraju = new Kosaraju();
    kosaraju.init();

    // create primitives
    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(
        255, 64, 64));
    graphProps.set(AnimationPropertiesKeys.NAME, "graphProps");

    Node[] nodes = new Node[10];
    nodes[0] = new Coordinates(50, 130);
    nodes[1] = new Coordinates(100, 70);
    nodes[2] = new Coordinates(100, 190);
    nodes[3] = new Coordinates(150, 130);
    nodes[4] = new Coordinates(230, 130);
    nodes[5] = new Coordinates(250, 70);
    nodes[6] = new Coordinates(280, 190);
    nodes[7] = new Coordinates(310, 130);
    nodes[8] = new Coordinates(340, 70);
    nodes[9] = new Coordinates(376, 120);
    int[][] adjacencyMatrix = new int[10][10];
    adjacencyMatrix[0][1] = 1;
    adjacencyMatrix[0][3] = 1;
    adjacencyMatrix[1][3] = 1;
    adjacencyMatrix[2][0] = 1;
    adjacencyMatrix[3][2] = 1;
    adjacencyMatrix[3][4] = 1;
    adjacencyMatrix[4][5] = 1;
    adjacencyMatrix[5][4] = 1;
    adjacencyMatrix[6][4] = 1;
    adjacencyMatrix[7][6] = 1;
    adjacencyMatrix[7][9] = 1;
    adjacencyMatrix[8][7] = 1;
    adjacencyMatrix[9][8] = 1;

    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
    primitives.put("offsetGraphX", 400);
    primitives.put("offsetGraphY", 0);
    primitives.put("transposedGraphOffsetY", 200);
    primitives.put("graph", kosaraju.lang.newGraph("graph", adjacencyMatrix,
        nodes,
        new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" },
        null, new GraphProperties()));

    // create properties
    AnimationPropertiesContainer props = new AnimationPropertiesContainer();
    StackProperties stackOne = new StackProperties();
    stackOne.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(255,
        64, 64));
    stackOne.set(AnimationPropertiesKeys.NAME, "stackOne");
    props.add(stackOne);
    StackProperties stackTwo = new StackProperties();
    stackTwo.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(255,
        64, 64));
    stackTwo.set(AnimationPropertiesKeys.NAME, "stackTwo");
    props.add(stackTwo);
    SourceCodeProperties sourceCode = new SourceCodeProperties();
    sourceCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    sourceCode.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    sourceCode.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    sourceCode.set(AnimationPropertiesKeys.NAME, "sourceCode");
    props.add(sourceCode);
    SourceCodeProperties introAndOutro = new SourceCodeProperties();
    introAndOutro.set(AnimationPropertiesKeys.NAME, "introAndOutro");
    props.add(introAndOutro);
    RectProperties headerBorder = new RectProperties();
    headerBorder.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(153,
        153, 255));
    headerBorder.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(153, 153,
        255));
    headerBorder.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    headerBorder.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);
    headerBorder.set(AnimationPropertiesKeys.NAME, "headerBorder");
    props.add(headerBorder);
    TextProperties header = new TextProperties();
    header.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    header.set(AnimationPropertiesKeys.NAME, "header");
    props.add(header);
    MatrixProperties matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    // color properties seem to get ignored
    matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(
        255, 64, 64));
    matrixProps.set(AnimationPropertiesKeys.NAME, "accessCounterMatrix");
    props.add(matrixProps);
    props.add(graphProps);

    System.out.println(kosaraju.generate(props, primitives));
  }

  private void showHeader() {
    lang.newRect(new Coordinates(10, 10), new Coordinates(260, 40), "", null,
        headerBorder);
    lang.newText(new Coordinates(15, 20), "Kosaraju's Algorithm", "", null,
        header);
  }

  // shows an introduction
  private void showIntro() {
    SourceCode intro = lang.newSourceCode(new Coordinates(10, 55), "introText",
        null, introAndOutro);
    intro
        .addCodeLine(
            "Kosaraju's algorithm is an algorithm, which finds strongly connected components in a directed graph.",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine("It works in 3 steps:", null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "It first does one initial depth-first search and pushes the vertices onto a stack when they are finished.",
            null, 0, null);
    intro
        .addCodeLine(
            "Here comes the crucial observation that a vertex finished late is in a source SCC of the graph with merged SCCs.",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "Then the transpose graph is created, simply reversing all edges. So the sources become sinks!",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "Lastly another DFS run on the transposed graph, in the order of the first run's result, is made to output the results.",
            null, 0, null);
    intro
        .addCodeLine(
            "That works, because DFS called on a vertex in a sink finds exactly one SCC.",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "The matrix on the bottom right will show you how often the used vertex label (visited) is accessed, and how often a adjacency",
            null, 0, null);
    intro
        .addCodeLine(
            "matrix or adjacency list would be accessed. Then you can easily compare the counters to the runtime described in the conculsion.",
            null, 0, null);

    lang.nextStep("Introduction");
    intro.hide();
  }

  // shows a conclusion
  private void showOutro() {
    SourceCode outro = lang.newSourceCode(new Coordinates(10, 55), "", null,
        introAndOutro);
    outro.addCodeLine("Complexity", null, 0, null);
    outro.addCodeLine("", null, 0, null);
    outro
        .addCodeLine(
            "If the graph is represented using an adjacency list, the algorithm runs in linear time O(V+E), ",
            null, 0, null);
    outro
        .addCodeLine(
            "since each DFS run and also the transpose operation performs in linear time.",
            null, 0, null);
    outro
        .addCodeLine(
            "This is also a lower bound on this problem, because any algorithm must examine all vertices and edges.",
            null, 0, null);
    outro
        .addCodeLine(
            "If the graph is represented in another way the complexity can get worse (for example O(V*V) with an adjacency matrix).",
            null, 0, null);
    outro.addCodeLine("", null, 0, null);
    outro.addCodeLine("", null, 0, null);
    outro.addCodeLine("Alternative algorithms", null, 0, null);
    outro.addCodeLine("", null, 0, null);
    outro
        .addCodeLine(
            "Basically there is Tarjan's and there are path based strongly connected components algorithms.",
            null, 0, null);
    outro
        .addCodeLine(
            "Both also run in linear time and they usually are preferred, because they don't need two DFS runs.",
            null, 0, null);

    lang.nextStep("Conclusion");
  }

  // generates the source code
  private void generateSourceCode() {
    sc = lang
        .newSourceCode(new Coordinates(10, 55), "source", null, sourceCode);
    sc.addCodeLine("public void kusaraju (Graph g) {", null, 0, null); // 0
    sc.addCodeLine("// order DFS run", null, 1, null); // 1
    sc.addCodeLine("Stack<Integer> stack = new Stack<Integer>();", null, 1,
        null); // 2
    sc.addCodeLine("boolean[] visited = new boolean[g.getVertexCount()];",
        null, 1, null); // 3
    sc.addCodeLine(
        "for (int vertex = 0; vertex < g.getVertexCount(); vertex++)", null, 1,
        null); // 4
    sc.addCodeLine("if (!visited[vertex])", null, 2, null); // 5
    sc.addCodeLine("dfs(vertex, g, stack, visited);", null, 3, null); // 6
    sc.addCodeLine("// transpose", null, 1, null); // 7
    sc.addCodeLine("g.transpose();", null, 1, null); // 8
    sc.addCodeLine("// output DFS run", null, 1, null); // 9
    sc.addCodeLine("Stack<Integer> stack2 = new Stack<Integer>();", null, 1,
        null); // 10
    sc.addCodeLine("visited = new boolean[g.getVertexCount()];", null, 1, null); // 11
    sc.addCodeLine("while (!stack.isEmpty()) {", null, 1, null); // 12
    sc.addCodeLine("int vertex = stack.pop();", null, 2, null); // 13
    sc.addCodeLine("if (!visited[vertex]) {", null, 2, null); // 14
    sc.addCodeLine("dfs(vertex, g, stack2, visited);", null, 3, null); // 15
    sc.addCodeLine("System.out.print(\\\"Found SCC: \\\");", null, 3, null); // 16
    sc.addCodeLine("while (!stack2.isEmpty())", null, 3, null); // 17
    sc.addCodeLine("System.out.print(stack2.pop() + \\\" \\\");", null, 4, null); // 18
    sc.addCodeLine("System.out.println();", null, 3, null); // 19
    sc.addCodeLine("}", null, 2, null); // 20
    sc.addCodeLine("}", null, 1, null); // 21
    sc.addCodeLine("}", null, 0, null); // 22
    sc.addCodeLine("", null, 0, null); // 23
    sc.addCodeLine(
        "public void dfs (int vertex, Graph g, Stack<Integer> stack, boolean[] visited) {",
        null, 0, null); // 24
    sc.addCodeLine("visited[vertex] = true;", null, 1, null); // 25
    sc.addCodeLine("for (int neighbor : g.getOutNeighbors(vertex))", null, 1,
        null); // 26
    sc.addCodeLine("if (!visited[neighbor])", null, 2, null); // 27
    sc.addCodeLine("dfs(neighbor, g, stack, visited);", null, 3, null); // 28
    sc.addCodeLine("stack.push(vertex)", null, 1, null); // 29
    sc.addCodeLine("}", null, 0, null); // 30
  }

  private void kosaraju(Graph g) {
    g.hide();
    showHeader();
    showIntro();

    g.show();
    generateSourceCode();

    // Counters for graph operations.
    // Stack operations all are constant time (push/pop) and depend completely
    // on graph operations (-> vertex label accesses).
    // Label counter counts access to vertex labels (->visited).
    vertexLabelAccessCounter = 0;
    // Matrix counter counts adjacency matrix accesses.
    edgeIteratorMatrixCounter = 0;
    // List counter counts accesses to an adjacency list.
    edgeIteratorListCounter = 0;
    // Edge and vertex numbers.
    int edges = 0;
    int[][] adjMatrix = g.getAdjacencyMatrix();
    for (int i = 0; i < adjMatrix.length; i++)
      for (int j = 0; j < adjMatrix[i].length; j++)
        if (adjMatrix[i][j] != 0)
          edges++;
    int vertices = g.getSize();

    StringMatrix matrix = lang.newStringMatrix(new Coordinates(555, 480),
        new String[2][5], "matrix", null, matrixProps);
    matrix.put(0, 0, "|V|", null, null);
    matrix.put(0, 1, "|E|", null, null);
    matrix.put(0, 2, "label", null, null);
    matrix.put(0, 3, "adj. matrix", null, null);
    matrix.put(0, 4, "adj. list", null, null);
    matrix.put(1, 0, String.valueOf(vertices), null, null);
    matrix.put(1, 1, String.valueOf(edges), null, null);
    matrix.put(1, 2, "0", null, null);
    matrix.put(1, 3, "0", null, null);
    matrix.put(1, 4, "0", null, null);

    ArrayBasedStack<String> stack = lang.newArrayBasedStack(new Coordinates(
        320, 65), (List<String>) null, "StringArray1", null, stackOne, g
        .getSize());
    // Count label creation as label access?
    boolean[] visited = new boolean[g.getSize()];
    sc.highlight(0, 0, true);
    sc.highlight(2);
    sc.highlight(3);
    lang.nextStep("Initial DFS run");
    sc.unhighlight(2);
    sc.toggleHighlight(3, 4);
    lang.nextStep();
    // order dfs
    for (int i = 0; i < g.getSize(); i++) {
      sc.toggleHighlight(4, 5);
      lang.nextStep();
      vertexLabelAccessCounter++;
      matrix.put(1, 2, String.valueOf(vertexLabelAccessCounter), null, null);
      if (!visited[i]) {
        sc.unhighlight(5);
        sc.highlight(6, 0, true);
        dfs(i, g, stack, visited, matrix);
        sc.toggleHighlight(6, 5);
      }
      sc.toggleHighlight(5, 4);
      lang.nextStep();
    }

    // transpose
    sc.toggleHighlight(4, 8);
    int[][] original = g.getAdjacencyMatrix();
    int[][] transposedAdjacencyMatrix = new int[g.getSize()][g.getSize()];
    for (int i = 0; i < g.getSize(); i++)
      for (int j = 0; j < g.getSize(); j++) {
        edgeIteratorMatrixCounter++;
        matrix.put(1, 3, String.valueOf(edgeIteratorMatrixCounter), null, null);
        if (original[j][i] != 0) {
          edgeIteratorListCounter++;
          matrix.put(1, 4, String.valueOf(edgeIteratorListCounter), null, null);
          edges++;
        }
        transposedAdjacencyMatrix[i][j] = original[j][i];
      }

    Node[] transposedNodes = new Node[g.getSize()];
    String[] transposedNodeLabels = new String[g.getSize()];
    for (int i = 0; i < transposedNodes.length; i++) {
      Coordinates oldNode = (Coordinates) g.getNode(i);
      transposedNodes[i] = new Coordinates(oldNode.getX(), oldNode.getY()
          + transposedGraphOffsetY);
      transposedNodeLabels[i] = g.getNodeLabel(i);
      // Count name access as vertex label access?
    }
    Graph transposed = lang.newGraph("transposed", transposedAdjacencyMatrix,
        transposedNodes, transposedNodeLabels, g.getDisplayOptions(),
        g.getProperties());
    lang.nextStep("Transpose");

    // output-dfs
    SourceCode output = lang.newSourceCode(new Coordinates(385, 470), "output",
        null, sourceCode);
    // Count label creation as label access?
    visited = new boolean[g.getSize()];
    ArrayBasedStack<String> stack2 = lang.newArrayBasedStack(new Coordinates(
        320, 265), (List<String>) null, "StringArray2", null, stackTwo,
        transposed.getSize());
    sc.toggleHighlight(8, 10);
    sc.highlight(11);
    lang.nextStep("Output DFS run");
    sc.unhighlight(10);
    sc.toggleHighlight(11, 12);
    while (!stack.isEmpty()) {
      int node = stack.top(null, timing).charAt(0) - 'A';
      sc.toggleHighlight(12, 13);
      lang.nextStep();
      sc.toggleHighlight(13, 14);
      lang.nextStep();
      sc.unhighlight(14);
      vertexLabelAccessCounter++;
      matrix.put(1, 2, String.valueOf(vertexLabelAccessCounter), null, null);
      if (!visited[node]) {
        sc.highlight(15, 0, true);
        dfs(node, transposed, stack2, visited, matrix);
        sc.unhighlight(15);
        sc.highlight(16);
        sc.highlight(17);
        sc.highlight(18);
        sc.highlight(19);
        StringBuilder line = new StringBuilder("Found SCC: ");
        while (!stack2.isEmpty()) {
          line.append(stack2.pop(null, null)).append(' ');
        }
        int newLine = output.addCodeLine(line.toString(), null, 0, null);
        if (newLine == 0)
          output.highlight(0);
        else
          output.toggleHighlight(newLine - 1, newLine);
        lang.nextStep();
        sc.unhighlight(16);
        sc.unhighlight(17);
        sc.unhighlight(18);
        sc.unhighlight(19);
      }
      stack.pop(null, null);
      sc.highlight(12);
      lang.nextStep();
    }

    g.hide();
    transposed.hide();
    stack.hide();
    stack2.hide();
    output.hide();
    sc.hide();
    showOutro();
  }

  private void dfs(int i, Graph g, ArrayBasedStack<String> stack,
      boolean[] visited, StringMatrix matrix) {
    vertexLabelAccessCounter++;
    matrix.put(1, 2, String.valueOf(vertexLabelAccessCounter), null, null);
    visited[i] = true;
    g.highlightNode(i, null, timing);
    sc.highlight(25);
    lang.nextStep();
    sc.toggleHighlight(25, 26);
    lang.nextStep();
    int[] matrixRow = g.getEdgesForNode(i);
    for (int neighbor = 0; neighbor < matrixRow.length; neighbor++) {
      edgeIteratorMatrixCounter++;
      matrix.put(1, 3, String.valueOf(edgeIteratorMatrixCounter), null, null);
      if (matrixRow[neighbor] == 0)
        continue;
      edgeIteratorListCounter++;
      matrix.put(1, 4, String.valueOf(edgeIteratorListCounter), null, null);
      g.highlightEdge(i, neighbor, null, timing);
      sc.toggleHighlight(26, 27);
      lang.nextStep();
      vertexLabelAccessCounter++;
      matrix.put(1, 2, String.valueOf(vertexLabelAccessCounter), null, null);
      if (!visited[neighbor]) {
        // no good way to highlight recursion :/
        sc.toggleHighlight(27, 28);
        lang.nextStep();
        sc.unhighlight(28);
        dfs(neighbor, g, stack, visited, matrix);
        sc.highlight(27);
      }
      g.unhighlightEdge(i, neighbor, null, timing);
      sc.toggleHighlight(27, 26);
    }
    stack.push(String.valueOf((char) (i + 'A')), null, null);
    sc.toggleHighlight(26, 29);
    lang.nextStep();
    sc.unhighlight(29);
  }
}
