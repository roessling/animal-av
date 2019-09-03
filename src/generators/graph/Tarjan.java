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

//assumptions to AlgoAnimApi:
//-graph nodes are labeled A, B, ... consecutively
//-ArrayBasedStack is based on StringArray (it has no own name)

public class Tarjan implements Generator {
  private Language             lang;
  private Graph                graph;
  private GraphProperties      graphProps;
  private int                  graphOffsetX;
  private int                  graphOffsetY;
  private SourceCodeProperties introAndOutro;
  private SourceCodeProperties sourceCode;
  private StackProperties      stackProps;
  private MatrixProperties     matrixProps;
  private RectProperties       headerBorder;
  private TextProperties       header;
  private SourceCode           sc;

  private Timing               timing;

  /**
   * Set this to true (or remove the specific ifs) if the matrix highlighting
   * works correctly. Currently it does not.
   */
  private static final boolean ENABLE_MATRIX_HIGHLIGHT = true;

  public void init() {
    lang = new AnimalScript("Tarjan's SCC algorithm [EN]", "Kai Schwierczek",
        800, 600);
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
    stackProps = (StackProperties) props.getPropertiesByName("stack");
    matrixProps = (MatrixProperties) props.getPropertiesByName("matrix");
    graphOffsetX = (Integer) primitives.get("offsetGraphX");
    graphOffsetY = (Integer) primitives.get("offsetGraphY");
    headerBorder = (RectProperties) props.getPropertiesByName("headerBorder");
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
    tarjan(graph);

    return lang.toString();
  }

  public String getName() {
    return "Tarjan's SCC algorithm [EN]";
  }

  public String getAlgorithmName() {
    return "Tarjan's SCC algorithm";
  }

  public String getAnimationAuthor() {
    return "Kai Schwierczek";
  }

  public String getDescription() {
    return "Tarjan's SCC algorithm is an algorithm to find strongly connected components (SCCs) in graphs."
        + "\n"
        + "It does so with only one DFS run by making the first node of a SCC a \"root\".";
  }

  public String getCodeExample() {
    return "private int nextIndex;\n"
        + "public void tarjan (Graph g) {\n"
        + "	// initialize variables\n"
        + "	Stack<Integer> stack = new Stack<Integer>();\n"
        + "	nextIndex = 0;\n"
        + "	int[] index = new int[g.getVertexCount()];\n"
        + "	int[] lowlink = new int[g.getVertexCount()];\n"
        + "	boolean[] onStack = new boolean[g.getVertexCount()];\n"
        + "	for (int i = 0; i < index.length; i++)\n"
        + "		index[i] = -1;\n"
        + "	// strongconnect dfs\n"
        + "	for (int vertex = 0; vertex < g.getVertexCount(); vertex++)\n"
        + "		if (index[vertex] == -1)\n"
        + "			strongconnect(vertex, g, stack, index, lowlink, onStack);\n"
        + "}\n"
        + "\n"
        + "public void strongconnect (int vertex, Graph g, Stack<Integer> stack, int[] index, int[] lowlink, boolean[] onStack) {\n"
        + "	index[vertex] = nextIndex++;\n"
        + "	lowlink[vertex] = index[vertex];\n"
        + "	stack.push(vertex);\n"
        + "	onStack[vertex] = true;\n"
        + "	for (int neighbor : g.getOutNeighbors(vertex)) {\n"
        + "		if (index[vertex] == -1) {\n"
        + "			strongconnect(neighbor, g, stack, index, lowlink, onStack);\n"
        + "			lowlink[vertex] = Math.min(lowlink[vertex], lowlink[neighbor]);\n"
        + "		} else if (onStack[neighbor]) {\n"
        + "			lowlink[vertex] = Math.min(lowlink[vertex], index[neighbor]);\n"
        + "	}\n" + "	\n" + "	if (index[i] == lowlink[i]) {\n"
        + "		System.out.print(\"Found SCC: \");\n" + "		int vertex;\n"
        + "		do {\n" + "			vertex = stack.pop();\n"
        + "			onStack[vertex] = false;\n" + "			System.out.print(vertex"
        + "+ \" \");\n" + "		} while (i != vertex);\n"
        + "		System.out.println();\n" + "	}\n" + "}\n";
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
    Tarjan tarjan = new Tarjan();
    tarjan.init();

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
    primitives.put("graph", tarjan.lang.newGraph("graph", adjacencyMatrix,
        nodes,
        new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" },
        null, new GraphProperties()));

    // create properties
    AnimationPropertiesContainer props = new AnimationPropertiesContainer();
    StackProperties stackTwo = new StackProperties();
    stackTwo.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(255,
        64, 64));
    stackTwo.set(AnimationPropertiesKeys.NAME, "stack");
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
    // XXX color properties seem to get ignored
    matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(
        255, 64, 64));
    matrixProps.set(AnimationPropertiesKeys.NAME, "matrix");
    props.add(matrixProps);
    props.add(graphProps);

    System.out.println(tarjan.generate(props, primitives));
  }

  private void showHeader() {
    lang.newRect(new Coordinates(10, 10), new Coordinates(285, 40), "", null,
        headerBorder);
    lang.newText(new Coordinates(15, 20), "Tarjan's SCC algorithm", "", null,
        header);
  }

  // shows an introduction
  private void showIntro() {
    SourceCode intro = lang.newSourceCode(new Coordinates(10, 55), "introText",
        null, introAndOutro);
    intro
        .addCodeLine(
            "Tarjan's SCC algorithm is an algorithm, which finds strongly connected components in a directed graph.",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine("It works with one depth-first search run:", null, 0,
        null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "When a vertex is first visited it gets an index and a lowlink. The index just represents the order the vertices were found.",
            null, 0, null);
    intro
        .addCodeLine(
            "The lowlink on the other side points to the lowest known part of the SCC, in the beginning the vertex itself.",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine(
        "Then all outgoing edges are examined, there are three cases:", null,
        0, null);
    intro
        .addCodeLine(
            "1. The neighbored vertex does not yet have an index. In this case a recursive call follows and the lowlink of the vertex is updated.",
            null, 0, null);
    intro
        .addCodeLine(
            "The SCC expands (the neighboring vertex is connected to this vertex or some vertex before) or a new SCC is found.",
            null, 1, null);
    intro
        .addCodeLine(
            "2. The neighbored vertex does have an index and is currently on the stack. In this case only the lowlink of the vertex is updated.",
            null, 0, null);
    intro
        .addCodeLine(
            "The neighbored vertex belongs to the same SCC and was found before, so this vertex isn't the SCC's \\\"root\\\".",
            null, 1, null);
    intro
        .addCodeLine(
            "3. The neighbored vertex does have an index and is not on the stack anymore. In this case nothing is done.",
            null, 0, null);
    intro.addCodeLine(
        "The neighbored vertex belongs to a already found, complete, SCC.",
        null, 1, null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "After all edges were examined and the lowlink is still equal to this vertex' index a SCC was found with this vertex as its root.",
            null, 0, null);
    intro
        .addCodeLine(
            "Then simply all vertices currently on the stack above this vertex form an SCC.",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine(
        "The algorithm continues till all vertices were visited.", null, 0,
        null);

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
            "simply since a DFS run performs in linear time and the used \\\"labels\\\" (index, lowlink, onStack) are accessed in constant time.",
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
            "Basically there is Kosaraju's and there are path based strongly connected components algorithms.",
            null, 0, null);
    outro.addCodeLine(
        "Both also run in linear time, but Kosaraju's needs two DFS runs.",
        null, 0, null);

    lang.nextStep("Conclusion");
  }

  // generates the source code
  private void generateSourceCode() {
    sc = lang
        .newSourceCode(new Coordinates(10, 55), "source", null, sourceCode);
    sc.addCodeLine("private int nextIndex;", null, 0, null); // 0
    sc.addCodeLine("public void tarjan (Graph g) {", null, 0, null); // 1
    sc.addCodeLine("// initialize variables", null, 1, null); // 2
    sc.addCodeLine("Stack<Integer> stack = new Stack<Integer>();", null, 1,
        null); // 3
    sc.addCodeLine("nextIndex = 0;", null, 1, null); // 4
    sc.addCodeLine("int[] index = new int[g.getVertexCount()];", null, 1, null); // 5
    sc.addCodeLine("int[] lowlink = new int[g.getVertexCount()];", null, 1,
        null); // 6
    sc.addCodeLine("boolean[] onStack = new boolean[g.getVertexCount()];",
        null, 1, null); // 7
    sc.addCodeLine("for (int i = 0; i < index.length; i++)", null, 1, null); // 8
    sc.addCodeLine("index[i] = -1;", null, 2, null); // 9
    sc.addCodeLine("// strongconnect dfs", null, 1, null); // 10
    sc.addCodeLine(
        "for (int vertex = 0; vertex < g.getVertexCount(); vertex++)", null, 1,
        null); // 11
    sc.addCodeLine("if (index[vertex] == -1)", null, 2, null); // 12
    sc.addCodeLine("strongconnect(vertex, g, stack, index, lowlink, onStack);",
        null, 3, null); // 13
    sc.addCodeLine("}", null, 0, null); // 14
    sc.addCodeLine("", null, 0, null); // 15
    sc.addCodeLine(
        "public void strongconnect (int vertex, Graph g, Stack<Integer> stack, int[] index, int[] lowlink, boolean[] onStack) {",
        null, 0, null); // 16
    sc.addCodeLine("index[vertex] = nextIndex++;", null, 1, null); // 17
    sc.addCodeLine("lowlink[vertex] = index[vertex];", null, 1, null); // 18
    sc.addCodeLine("stack.push(vertex);", null, 1, null); // 19
    sc.addCodeLine("onStack[vertex] = true;", null, 1, null); // 20
    sc.addCodeLine("for (int neighbor : g.getOutNeighbors(vertex)) {", null, 1,
        null); // 21
    sc.addCodeLine("if (index[vertex] == -1) {", null, 2, null); // 22
    sc.addCodeLine(
        "strongconnect(neighbor, g, stack, index, lowlink, onStack);", null, 3,
        null); // 23
    sc.addCodeLine(
        "lowlink[vertex] = Math.min(lowlink[vertex], lowlink[neighbor]);",
        null, 3, null); // 24
    sc.addCodeLine("} else if (onStack[neighbor]) {", null, 2, null); // 25
    sc.addCodeLine(
        "lowlink[vertex] = Math.min(lowlink[vertex], index[neighbor]);", null,
        3, null); // 26
    sc.addCodeLine("}", null, 1, null); // 27
    sc.addCodeLine("", null, 1, null); // 28
    sc.addCodeLine("if (index[i] == lowlink[i]) {", null, 1, null); // 29
    sc.addCodeLine("System.out.print(\\\"Found SCC: \\\");", null, 2, null); // 30
    sc.addCodeLine("int vertex;", null, 2, null); // 31
    sc.addCodeLine("do {", null, 2, null); // 32
    sc.addCodeLine("vertex = stack.pop();", null, 3, null); // 33
    sc.addCodeLine("onStack[vertex] = false;", null, 3, null); // 34
    sc.addCodeLine("System.out.print(vertex + \\\" \\\");", null, 3, null); // 35
    sc.addCodeLine("} while (i != vertex);", null, 2, null); // 36
    sc.addCodeLine("System.out.println();", null, 2, null); // 37
    sc.addCodeLine("}", null, 1, null); // 38
    sc.addCodeLine("}", null, 0, null); // 39
  }

  private int nextIndex;

  private void tarjan(Graph g) {
    g.hide();
    showHeader();
    showIntro();

    g.show();
    generateSourceCode();
    SourceCode output = lang.newSourceCode(new Coordinates(330, 520), "output",
        null, sourceCode);

    // initialize variables
    ArrayBasedStack<String> stack = lang.newArrayBasedStack(new Coordinates(
        325, 70), (List<String>) null, "StringArray1", null, stackProps, g
        .getSize());
    StringMatrix matrix = lang.newStringMatrix(new Coordinates(515, 365),
        new String[g.getSize() + 1][4], "matrix", null, matrixProps);
    matrix.put(0, 0, "V", null, null);
    matrix.put(0, 1, "index", null, null);
    matrix.put(0, 2, "lowlink", null, null);
    matrix.put(0, 3, "onStack", null, null);
    for (int i = 0; i < g.getSize(); i++) {
      matrix.put(i + 1, 0, g.getNodeLabel(i), null, null);
      matrix.put(i + 1, 1, "-1", null, null);
      matrix.put(i + 1, 2, "0", null, null);
      matrix.put(i + 1, 3, "false", null, null);
    }

    nextIndex = 0;
    int[] index = new int[g.getSize()];
    int[] lowlink = new int[g.getSize()];
    boolean[] onStack = new boolean[g.getSize()];
    for (int i = 0; i < index.length; i++)
      index[i] = -1;

    sc.highlight(1, 0, true);
    sc.highlight(3);
    sc.highlight(4);
    sc.highlight(5);
    sc.highlight(6);
    sc.highlight(7);
    sc.highlight(8);
    sc.highlight(9);
    lang.nextStep("Initialize variables");
    sc.unhighlight(3);
    sc.unhighlight(4);
    sc.unhighlight(5);
    sc.unhighlight(6);
    sc.unhighlight(7);
    sc.unhighlight(8);
    sc.unhighlight(9);

    sc.highlight(11);
    lang.nextStep("Strongconnect DFS");

    // run strongconnect dfs
    for (int i = 0; i < g.getSize(); i++) {
      sc.toggleHighlight(11, 12);
      lang.nextStep();
      if (index[i] == -1) {
        sc.unhighlight(12);
        sc.highlight(13, 0, true);
        strongconnect(i, g, stack, index, lowlink, onStack, output, matrix);
        sc.toggleHighlight(13, 12);
      }
      sc.toggleHighlight(12, 11);
      lang.nextStep();
    }

    sc.hide();
    g.hide();
    stack.hide();
    output.hide();
    matrix.hide();
    showOutro();
  }

  private void strongconnect(int i, Graph g, ArrayBasedStack<String> stack,
      int[] index, int[] lowlink, boolean[] onStack, SourceCode output,
      StringMatrix matrix) {
    g.highlightNode(i, null, timing);
    if (ENABLE_MATRIX_HIGHLIGHT)
      matrix.highlightCellColumnRange(i + 1, 0, 3, null, null);
    index[i] = nextIndex++;
    lowlink[i] = index[i];
    matrix.put(i + 1, 1, String.valueOf(index[i]), null, null);
    matrix.put(i + 1, 2, String.valueOf(lowlink[i]), null, null);
    sc.highlight(17);
    sc.highlight(18);
    lang.nextStep();
    stack.push(String.valueOf((char) (i + 'A')), null, null);
    matrix.put(i + 1, 3, "true", null, null);
    onStack[i] = true;
    sc.toggleHighlight(17, 19);
    sc.toggleHighlight(18, 20);
    lang.nextStep();
    sc.unhighlight(19);
    sc.unhighlight(20);
    sc.highlight(21);
    lang.nextStep();
    int[] matrixRow = g.getEdgesForNode(i);
    for (int neighbor = 0; neighbor < matrixRow.length; neighbor++) {
      if (matrixRow[neighbor] == 0)
        continue;
      g.highlightEdge(i, neighbor, null, timing);
      if (ENABLE_MATRIX_HIGHLIGHT)
        matrix.highlightCellColumnRange(neighbor + 1, 2, 3, null, null);
      sc.toggleHighlight(21, 22);
      lang.nextStep();
      if (index[neighbor] == -1) {
        sc.toggleHighlight(22, 23);
        lang.nextStep();
        sc.unhighlight(23);
        if (ENABLE_MATRIX_HIGHLIGHT)
          matrix.unhighlightCellColumnRange(i + 1, 0, 3, null, null);
        strongconnect(neighbor, g, stack, index, lowlink, onStack, output,
            matrix);
        if (ENABLE_MATRIX_HIGHLIGHT)
          matrix.highlightCellColumnRange(i + 1, 0, 3, null, null);
        lowlink[i] = Math.min(lowlink[i], lowlink[neighbor]);
        matrix.put(i + 1, 2, String.valueOf(lowlink[i]), null, null);
        sc.highlight(24);
        lang.nextStep();
        sc.unhighlight(24);
      } else {
        sc.toggleHighlight(22, 25);
        lang.nextStep();
        if (onStack[neighbor]) {
          sc.toggleHighlight(25, 26);
          // index or neighbor doesn't matter, both are smaller than this index,
          // so this isn't a root node
          lowlink[i] = Math.min(lowlink[i], index[neighbor]);
          matrix.put(i + 1, 2, String.valueOf(lowlink[i]), null, null);
          lang.nextStep();
          sc.unhighlight(26);
        } else
          sc.unhighlight(25);
      }
      if (ENABLE_MATRIX_HIGHLIGHT)
        matrix.unhighlightCellColumnRange(neighbor + 1, 2, 3, null, null);
      g.unhighlightEdge(i, neighbor, null, timing);
      sc.highlight(21);
    }
    sc.highlight(29);
    lang.nextStep();
    // found SCC?
    if (index[i] == lowlink[i]) {
      sc.unhighlight(29);
      sc.highlight(30);
      sc.highlight(31);
      sc.highlight(32);
      sc.highlight(33);
      sc.highlight(34);
      sc.highlight(35);
      sc.highlight(36);
      sc.highlight(37);
      StringBuilder line = new StringBuilder("Found SCC: ");
      int vertex;
      do {
        vertex = stack.pop(null, null).charAt(0) - 'A';
        onStack[vertex] = false;
        matrix.put(vertex + 1, 3, "false", null, null);
        line.append((char) (vertex + 'A')).append(' ');
      } while (i != vertex);
      int newLine = output.addCodeLine(line.toString(), null, 0, null);
      if (newLine == 0)
        output.highlight(0);
      else
        output.toggleHighlight(newLine - 1, newLine);
      lang.nextStep();
      sc.unhighlight(30);
      sc.unhighlight(31);
      sc.unhighlight(32);
      sc.unhighlight(33);
      sc.unhighlight(34);
      sc.unhighlight(35);
      sc.unhighlight(36);
      sc.unhighlight(37);
    } else
      sc.unhighlight(29);
    g.unhighlightNode(i, null, timing);
    if (ENABLE_MATRIX_HIGHLIGHT)
      matrix.unhighlightCellColumnRange(i + 1, 0, 3, null, null);
    lang.nextStep();
  }
}
