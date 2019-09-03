//This file was created by group of:
// Quoc Hien Dang
// Thanh Tung Dang 1340183 <thanhtung.nov@gmail.com>

package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class FinalFLP implements Generator {
  private Language       lang;
  private int[][]        adjacencyMatrix;
  private int            n;
  private static int     defaultN     = 8;
  private static int[][] defaultGraph = { { 0, 1, 0, 0, 0, 0, 0, 0 },
      { 1, 0, 1, 1, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0, 2 },
      { 0, 1, 0, 0, 3, 2, 0, 0 }, { 0, 0, 0, 3, 0, 0, 0, 0 },
      { 0, 0, 0, 2, 0, 0, 2, 0 }, { 0, 0, 0, 0, 0, 2, 0, 0 },
      { 0, 0, 2, 0, 0, 0, 0, 0 }     };

  Text                   title, timing;

  public void init() {
    lang = new AnimalScript("Finding the Longest Path on a Tree",
        "Quoc Hien Dang, Thanh Tung Dang", 1024, 768);
    lang.setStepMode(true);
  }

  public void printTitle() {
    title = lang.newText(new Coordinates(0, 15),
        "Algorithm for finding the longest path on the tree.", "title", null);
    title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15), null, null);
    timing = lang.newText(new Coordinates(0, 35),
        "The animation contains: DoubleDxxx steps.", "time", null);
    timing.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 15), null, null);
    timing.changeColor(null, Color.GREEN, null, null);
    lang.nextStep("Description");
  }

  public void printDescription() {
    String[] desc = {
        "Given a weighted, undirected, non-cyclic connected graph. This kind of graph called weighted tree. We",
        "have to find the longest path on the tree.",
        " ",
        "The idea of the algorithm is:",
        " + Finding the longest path start from pre-defined node is simple because of no cycle on the graph.",
        "So the algorithm will find the start of the longest path. ",
        " + Firstly, we find the longest path starting from node 1 (or any node), this path ends with node x.",
        "Called 1x.",
        " + Secondly, find the longest path starting from this node and ending at node y. So now we have the",
        "longest path on the tree.",
        " ",
        "Proving :",
        "  Assume that we have another longer path, starts at x' and ends at y' (with x', y' are different from x) ",
        "called x'y'.Then the longest path start from node 1 must be ended at x' or y', not at x. We will prove that",
        "as below :",
        "Because the graph is a tree, then there must be a path connects those two paths, or they must be",
        "intersected. Assume that they're two nodes: u is on the path 1x, v is on the path x'y'.  u = v if 1x and x'y'",
        "intersected. x'y' is now the longest path, so x'v + vu + ux < x'v + vy', that means, ux < vy'. So the",
        "path 1y' = 1u + uv + vy' > 1u + ux = 1x. In other words, y' is the furthest node from 1 not x. Not as above,",
        "that 1x is the longest path from 1.",
        " ",
        "So, there isn't any longer path, or the longest path must be start at x.",
        " ",
        " ",
        "The longest path start from one pre-defined node can be founded with DFS or BFS." };
    Text[] descArray = new Text[desc.length];
    for (int i = 0; i < desc.length; i++) {
      descArray[i] = lang.newText(new Coordinates(10, 20 * (i + 3)), desc[i],
          "desc" + i, null);
      if (i < desc.length - 1)
        lang.nextStep();
      else
        lang.nextStep("Init");
    }
    for (int i = 0; i < desc.length; i++)
      descArray[i].hide();
  }

  // Deepth First Search to check if the tree is invalid

  public void DFS(int i) {
    visited[i] = 1;
    for (int j = 0; j < n; j++)
      if (visited[j] == 0 && adjacencyMatrix[i][j] > 0)
        DFS(j);
  }

  // number of connected part of this graph
  int numOfConnected() {
    int count = 0;
    for (int i = 0; i < n; i++)
      visited[i] = 0;
    for (int i = 0; i < n; i++)
      if (visited[i] == 0) {
        count++;
        if (count > 1)
          break;
        DFS(i);
      }
    return count;
  }

  // check if graph is an invalid tree: number of connected part = 1,
  // undirected, number of edge = n - 1
  boolean checkInput() {
    int edgeNum = 0;
    for (int i = 0; i < n; i++)
      for (int j = i; j < n; j++) {
        if (adjacencyMatrix[i][j] != adjacencyMatrix[j][i])
          return false;
        if (adjacencyMatrix[i][j] > 0)
          edgeNum++;
      }
    if (edgeNum != n - 1)
      return false;
    if (numOfConnected() != 1)
      return false;
    return true;
  }

  // if invaled
  void setDefaultGraph() {
    adjacencyMatrix = defaultGraph;
    n = defaultN;
  }

  // a warning for invalid graph input
  void createCorrectStep() {
    Text warning = lang.newText(new Coordinates(20, 60),
        "The input graph is invalid, so we use the default graph", "warning",
        null);
    warning.changeColor(null, Color.red, null, null);
    lang.nextStep();
    warning.hide();
  }

  // Variable for Primitives

  int[]                 label;
  int[]                 visited;
  int[]                 trace;
  IntArray              intArLabel, intArVisit;
  Graph                 graph;
  Node[]                nodes;
  ArrayMarker           xMarker, yMarker;
  String[]              nodeNames;
  ArrayProperties       arrayProps  = new ArrayProperties("array properties");
  GraphProperties       graphProps  = new GraphProperties();
  ArrayMarkerProperties ampX        = new ArrayMarkerProperties();
  ArrayMarkerProperties ampY        = new ArrayMarkerProperties();
  SourceCodeProperties  sourceProps = new SourceCodeProperties();
  SourceCode            flp;
  // Pseudo Code
  String[]              pseudoCode  = { "void visit(Node x) {",
      "    for (Node y: Adjacent from x)", "        if (not visited(y)) {",
      "            Label(y) = Label(x) + distance(x,y);",
      "            markAsVisited(y);", "            visit(y);", "        }",
      "}", "Node getMaxLabel() {", "    Node result;",
      "    int maxLabel = -1;", "    forall Node x",
      "        if (Label(x) > maxLabel) {", "            maxLabel = Label(x);",
      "            result = x;", "        }", "    return result;", "}",
      "void findTheLongestPath(Grapth a) {", "    init();",
      "    markAsVisited(a.getNode(0));", "    visit(a.getNode(0));",
      "    x = getMaxLabel();", "    init();", "    markAsVisited(x);",
      "    visit(x);", "    y = getMaxLabel();", "    printLongestPath(x,y);",
      "}"                          };

  // set Properties for Array, Graph...

  void setProperties() {
    // for Array
    arrayProps.set("font", new Font(Font.MONOSPACED, Font.PLAIN, 15));
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

    // for Graph
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLUE);
    graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

    // for Source Code
    sourceProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 12));
    sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    sourceProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // for Marker

    ampX.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
    ampX.set(AnimationPropertiesKeys.LABEL_PROPERTY, "x");
    ampX.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    ampY.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
    ampY.set(AnimationPropertiesKeys.LABEL_PROPERTY, "y");
    ampY.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
  }

  // Drawing graph as step by step
  void drawGraph(int col, int row) {
    nodes = new Node[n];
    nodeNames = new String[n];

    int[] layer = new int[n];
    for (int i = 0; i < n; i++) {
      layer[i] = 0;
      Integer tmp = new Integer(i);
      nodeNames[i] = tmp.toString();
    }
    layer[0] = 1;
    int currentLayer = 1;

    int[] countLayer = new int[n];
    for (int i = 0; i < n; i++)
      countLayer[i] = 0;
    nodes[0] = new Coordinates(col, row);

    int[] pos = new int[n];
    pos[0] = 0;

    while (true) {
      int currentNode = -1;
      int mm = n + 1;
      for (int i = 0; i < n; i++)
        if (layer[i] == currentLayer && pos[i] < mm) {
          currentNode = i;
          mm = pos[i];
        }
      if (currentNode == -1) {
        currentLayer++;
      } else {
        for (int i = 0; i < n; i++)
          if (adjacencyMatrix[currentNode][i] > 0 && layer[i] == 0) {
            layer[i] = currentLayer + 1;
            nodes[i] = new Coordinates(col
                + Math.max(pos[currentNode], countLayer[currentLayer]) * 70,
                row + currentLayer * 70);
            pos[i] = countLayer[currentLayer];
            countLayer[currentLayer]++;
          }
        layer[currentNode] = -1;
      }
      if (currentLayer == n + 1)
        break;
    }
    graph = lang.newGraph("tree", adjacencyMatrix, nodes, nodeNames, null,
        graphProps);

  }

  void drawArrayAndGraph() {
    lang.newText(new Coordinates(400, 90), "Label: ", "text_label", null);
    intArLabel = lang.newIntArray(new Coordinates(450, 90), label,
        "IntArrayLabel", null, arrayProps);
    lang.newText(new Coordinates(400, 150), "Visited: ", "text_visited", null);
    intArVisit = lang.newIntArray(new Coordinates(450, 150), visited,
        "IntArrayVisit", null, arrayProps);
    lang.newText(new Coordinates(450, 180), "Graph :", "text_graph", null);
    drawGraph(460, 230);
    lang.nextStep("First DFS from 0");
  }

  void printSourceCode() {
    flp = lang.newSourceCode(new Coordinates(10, 120), "SourceCode", null,
        sourceProps);
    for (int i = 0; i < pseudoCode.length; i++) {
      flp.addCodeLine(pseudoCode[i], null, 0, null);
    }
  }

  void initialisation() {
    for (int i = 0; i < n; i++) {
      label[i] = 0;
      visited[i] = 0;
    }

    Text intro1 = lang.newText(new Coordinates(10, 60), "Initialisation: ",
        "intro1", null);
    intro1.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15), null, null);
    lang.newText(new Coordinates(10, 80),
        "At first, all labels are set to 0. All node are not visited",
        "intro2", null);
    flp.highlight(19);
  }

  void visit(int i) {
    ArrayMarker x = lang.newArrayMarker(intArLabel, i, "xx" + i, null, ampX);
    ArrayMarker y = lang.newArrayMarker(intArLabel, i, "yy" + i, null, ampY);
    y.hide();
    flp.highlight(0);
    graph.highlightNode(i, null, null);
    intArLabel.highlightCell(i, null, null);
    lang.nextStep();
    flp.unhighlight(0);

    for (int j = 0; j < n; j++) {
      flp.highlight(1);
      if (adjacencyMatrix[i][j] > 0) {
        graph.highlightNode(j, null, null);
        intArVisit.highlightCell(j, null, null);
        y.move(j, null, null);
        y.show();
        flp.unhighlight(1);
        flp.highlight(2);
        lang.nextStep();

        if (visited[j] == 0) {
          visited[j] = 1;
          label[j] = label[i] + adjacencyMatrix[i][j];
          trace[j] = i;

          flp.unhighlight(2);
          flp.highlight(3);
          intArLabel.put(j, label[j], null, null);
          intArLabel.highlightCell(j, null, null);
          lang.nextStep();

          flp.unhighlight(3);
          flp.highlight(4);
          intArVisit.put(j, 1, null, null);
          intArVisit.highlightCell(j, null, null);
          lang.nextStep();

          flp.unhighlight(4);
          flp.highlight(5);
          lang.nextStep();

          flp.unhighlight(5);
          graph.unhighlightNode(i, null, null);
          graph.unhighlightNode(j, null, null);
          intArLabel.unhighlightCell(i, null, null);
          intArLabel.unhighlightCell(j, null, null);
          intArVisit.unhighlightCell(j, null, null);
          y.hide();
          x.hide();

          visit(j);
          graph.highlightNode(i, null, null);
          x.show();
          intArLabel.highlightCell(i, null, null);
        } else {
          graph.unhighlightNode(j, null, null);
          intArVisit.unhighlightCell(j, null, null);
          flp.unhighlight(2);
          flp.highlight(6);
          lang.nextStep();
          flp.unhighlight(6);
          y.hide();
        }
      }
    }
    flp.unhighlight(1);
    flp.highlight(7);
    lang.nextStep();
    x.hide();
    graph.unhighlightNode(i, null, null);
    intArLabel.unhighlightCell(i, null, null);
    flp.unhighlight(7);
  }

  void findTheStartOfThePath() {
    Text intro3 = lang.newText(new Coordinates(300, 550),
        "Now, find the furthest node from node 0", "intro3", null);
    intro3.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15), null, null);
    lang.nextStep();

    intro3.hide();
    flp.unhighlight(19);
    flp.highlight(20);
    intArVisit.highlightCell(0, null, null);
    intArVisit.put(0, 1, null, null);
    lang.nextStep();

    intArVisit.unhighlightCell(0, null, null);
    flp.unhighlight(20);
    flp.highlight(21);
    lang.nextStep();

    flp.unhighlight(21);
    visited[0] = 1;
    xMarker = lang.newArrayMarker(intArLabel, 0, "x", null, ampX);
    xMarker.hide();
    visit(0);
    lang.nextStep("Find start of the longest path");
  }

  int findTheNodeWithMaxLabel() {
    xMarker.show();
    flp.highlight(22);
    Text intro3 = lang.newText(new Coordinates(300, 550),
        "Now, find the node with the max distance from 0", "intro3", null);
    intro3.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15), null, null);
    lang.nextStep();
    flp.unhighlight(22);
    flp.highlight(8);

    int result = -1;
    int maxLabel = -1;
    Integer convert = new Integer(maxLabel);

    lang.nextStep();
    flp.unhighlight(8);
    flp.highlight(10);
    Text text_maxLabel = lang.newText(new Coordinates(550, 230), "maxLabel : "
        + convert.toString(), "maxLabel", null);
    convert = new Integer(result);
    Text text_result = lang.newText(new Coordinates(550, 260), "result : "
        + convert.toString(), "result", null);

    lang.nextStep();
    flp.unhighlight(10);
    for (int i = 0; i < n; i++) {
      flp.highlight(11);
      lang.nextStep();
      flp.unhighlight(11);
      flp.highlight(12);
      intArLabel.highlightCell(i, null, null);
      xMarker.move(i, null, null);
      lang.nextStep();

      if (label[i] > maxLabel) {
        maxLabel = label[i];
        result = i;
        convert = new Integer(maxLabel);
        flp.unhighlight(12);
        flp.highlight(13);
        text_maxLabel.setText("maxLabel : " + convert.toString(), null, null);
        lang.nextStep();
        flp.unhighlight(13);
        flp.highlight(14);
        convert = new Integer(result);
        text_result.setText("result : " + convert.toString(), null, null);
        lang.nextStep();
        flp.unhighlight(14);
      } else {
        flp.unhighlight(12);
        flp.highlight(15);
        lang.nextStep();
        flp.unhighlight(15);
      }
      intArLabel.unhighlightCell(i, null, null);
    }
    flp.highlight(16);
    lang.nextStep();
    flp.unhighlight(16);
    flp.highlight(17);
    lang.nextStep("Second DFS from start node");
    flp.unhighlight(17);
    intro3.hide();
    text_maxLabel.hide();
    text_result.hide();
    xMarker.move(result, null, null);
    return result;
  }

  // find the end of the path

  public void findTheEndOfThePath(int x) {
    xMarker.hide();
    flp.highlight(23);
    Text intro3 = lang.newText(new Coordinates(300, 550),
        "All labels and visited are set to 0", "intro3", null);
    intro3.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15), null, null);
    for (int i = 0; i < n; i++) {
      intArLabel.put(i, 0, null, null);
      intArVisit.put(i, 0, null, null);
      trace[i] = -1;
    }
    lang.nextStep();

    intro3.setText("Now mark node " + x + " as visited", null, null);
    flp.unhighlight(23);
    flp.highlight(24);
    intArVisit.put(x, 1, null, null);
    intArVisit.highlightCell(x, null, null);
    visited[x] = 1;
    lang.nextStep();

    intArVisit.unhighlightCell(x, null, null);
    flp.unhighlight(24);
    flp.highlight(25);
    lang.nextStep();

    intro3.setText("Start from node " + x, null, null);
    flp.unhighlight(25);
    visit(x);
    intro3.hide();
    lang.nextStep("Find end node of the longest path");
  }

  int findTheNodeWithMaxLabel(int x) {
    xMarker.show();
    flp.highlight(26);
    Text intro3 = lang.newText(new Coordinates(300, 550),
        "Now, find the node with the max distance from " + x, "intro3", null);
    intro3.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15), null, null);
    lang.nextStep();
    flp.unhighlight(26);
    flp.highlight(8);

    int result = -1;
    int maxLabel = -1;
    Integer convert = new Integer(maxLabel);

    lang.nextStep();
    flp.unhighlight(8);
    flp.highlight(10);
    Text text_maxLabel = lang.newText(new Coordinates(550, 230), "maxLabel : "
        + convert.toString(), "maxLabel", null);
    convert = new Integer(result);
    Text text_result = lang.newText(new Coordinates(550, 260), "result : "
        + convert.toString(), "result", null);

    lang.nextStep();
    flp.unhighlight(10);
    for (int i = 0; i < n; i++) {
      flp.highlight(11);
      lang.nextStep();
      flp.unhighlight(11);
      flp.highlight(12);
      intArLabel.highlightCell(i, null, null);
      xMarker.move(i, null, null);
      lang.nextStep();

      if (label[i] > maxLabel) {
        maxLabel = label[i];
        result = i;
        convert = new Integer(maxLabel);
        flp.unhighlight(12);
        flp.highlight(13);
        text_maxLabel.setText("maxLabel : " + convert.toString(), null, null);
        lang.nextStep();
        flp.unhighlight(13);
        flp.highlight(14);
        convert = new Integer(result);
        text_result.setText("result : " + convert.toString(), null, null);
        lang.nextStep();
        flp.unhighlight(14);
      } else {
        flp.unhighlight(12);
        flp.highlight(15);
        lang.nextStep();
        flp.unhighlight(15);
      }
      intArLabel.unhighlightCell(i, null, null);
    }
    flp.highlight(16);
    lang.nextStep();
    flp.unhighlight(16);
    flp.highlight(17);
    lang.nextStep("Highlight result");
    flp.unhighlight(17);
    intro3.hide();
    text_maxLabel.hide();
    text_result.hide();
    xMarker.move(result, null, null);
    return result;
  }

  // now highlight the longest path

  public void highlightResult(int x, int initialY) {
    int y = initialY;
    xMarker.hide();
    flp.highlight(27);
    Text intro3 = lang.newText(new Coordinates(300, 550),
        "Finally, the longest path on the tree is from " + y + " to " + x,
        "intro3", null);
    intro3.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15), null, null);
    lang.nextStep();
    flp.unhighlight(27);
    while (y != -1) {
      graph.highlightNode(y, null, null);
      if (trace[y] != -1)
        lang.nextStep();
      else
        lang.nextStep("Summary");
      y = trace[y];
    }
  }

  public void printConclusion() {
    lang.hideAllPrimitives();
    title.show();
    timing.show();
    String[] conc = {
        "Conclusion: ",
        "In order to find the longest path on a tree, we have used the connectivity of this graph, to find",
        "the longest path just by using some simple mathematical comparisons",
        "",
        "The complexity of the algorithm is O(n). Because of a tree with n vertex contains exactly n - 1 edge.",
        "In this animation we used Depth-First-Search to calculate the distance between nodes. Of course, we ",
        "have another choice for this: Breadth-First-Search.",
        " ",
        "For more information, please see : http://en.wikipedia.org/wiki/Tree_(graph_theory)" };
    Text[] conArray = new Text[conc.length];
    for (int i = 0; i < conc.length; i++) {
      conArray[i] = lang.newText(new Coordinates(10, 20 * (i + 3)), conc[i],
          "conc" + i, null);
      lang.nextStep();
    }
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
    n = (Integer) primitives.get("n");

    trace = new int[n];
    visited = new int[n];
    label = new int[n];
    printTitle();

    printDescription();

    if (!checkInput()) {
      createCorrectStep();
      setDefaultGraph();
    }

    setProperties();

    printSourceCode();

    initialisation();

    drawArrayAndGraph();

    findTheStartOfThePath();

    int x = findTheNodeWithMaxLabel();

    findTheEndOfThePath(x);

    int y = findTheNodeWithMaxLabel(x);

    highlightResult(x, y);

    printConclusion();

    String result = lang.toString();
    Integer convert = lang.getStep() + 1;

    return result.replace("DoubleDxxx", convert.toString());
  }

  public String getName() {
    return "Finding the Longest Path on a Tree";
  }

  public String getAlgorithmName() {
    return "Finding the Longest Path on a Tree";
  }

  public String getAnimationAuthor() {
    return "Quoc Hien Dang, Thanh Tung Dang";
  }

  public String getDescription() {
    return "Given a weighted, undirected, non-cyclic connected graph. This kind of graph called weighted tree. We "
        + "\n"
        + "have to find the longest path on the tree. "
        + "\n"
        + "\n"
        + "The idea of the algorithm is:"
        + "\n"
        + " + Finding the longest path start from pre-defined node is simple because of no cycle on the graph. "
        + "\n"
        + "So the algorithm will find the start of the longest path. "
        + "\n"
        + " + Firstly, we find the longest path starting from node 1 (or any node), this path ends with node x. "
        + "\n"
        + "Called 1x."
        + "\n"
        + " + Secondly, find the longest path starting from this node and ending at node y. So now we have the "
        + "\n"
        + "longest path on the tree."
        + "\n"
        + "\n"
        + "Proving :"
        + "\n"
        + "  Assume that we have another longer path, starts at x' and ends at y' (with x', y' are different from x) "
        + "\n"
        + "called x'y'.Then the longest path start from node 1 must be ended at x' or y', not at x. We will prove that "
        + "\n"
        + "as below :"
        + "\n"
        + "Because the graph is a tree, then there must be a path connects those two paths, or they must be"
        + "\n"
        + "intersected. Assume that they're two nodes: u is on the path 1x, v is on the path x'y'.  u = v if 1x and x'y' "
        + "\n"
        + "intersected. x'y' is now the longest path, so x'v + vu + ux < x'v + vy', that means, ux < vy'. So the"
        + "\n"
        + "path 1y' = 1u + uv + vy' > 1u + ux = 1x. In other words, y' is the furthest node from 1 not x. Not as above,"
        + "\n"
        + "that 1x is the longest path from 1."
        + "\n"
        + "\n"
        + "So, there isn't any longer path, or the longest path must be start at x. "
        + "\n"
        + "\n"
        + "\n"
        + "The longest path start from one pre-defined node can be founded with DFS or BFS."
        + "\n";
  }

  public String getCodeExample() {
    return "void visit(Node x) {" + "\n" + "   for (Node y: Adjacent from x)"
        + "\n" + "      if (not visited(y)) {" + "\n"
        + "          Label(y) = Label(x) + distance(x,y);" + "\n"
        + "          markAsVisited(y);" + "\n" + "          visit(y);" + "\n"
        + "      }" + "\n" + "}" + "\n" + "\n" + "Node getMaxLabel() {" + "\n"
        + "    Node result;" + "\n" + "    int maxLabel = -1;" + "\n"
        + "    forall Node x" + "\n" + "        if (Label(x) > maxLabel) {"
        + "\n" + "             maxLabel = Label(x);" + "\n"
        + "             result = x;" + "\n" + "        } " + "\n"
        + "    return result;" + "\n" + "}" + "\n" + "\n"
        + "void findTheLongestPath(Grapth a) {" + "\n" + "   init();" + "\n"
        + "   markAsVisited(a.getNode(0));" + "\n" + "   visit(a.getNode(0));"
        + "\n" + "   x = getMaxLabel();" + "\n" + "   init();" + "\n"
        + "   markAsVisited(x);" + "\n" + "   visit(x);" + "\n"
        + "   y = getMaxLabel();" + "\n" + "   printLongestPath(x,y);" + "\n"
        + "}";
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

}