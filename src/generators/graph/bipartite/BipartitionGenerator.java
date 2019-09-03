package generators.graph.bipartite;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Stack;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class BipartitionGenerator implements ValidatingGenerator {

  /**
   * the overall language object
   */
  private Language                  lang;
  /**
   * the color in which all highlightings (except sourcecode) are done
   */
  private Color                     highlightColor;
  /**
   * array properties for the setA array
   */
  private ArrayProperties           arrayPropsA;
  /**
   * array properties for the setB array
   */
  private ArrayProperties           arrayPropsB;
  /**
   * globally defined source code properties
   */
  private SourceCodeProperties      sourceCodeProps;
  /**
   * the Color of elements tagged as A
   */
  private Color                     colorA;
  /**
   * the color of elements tagged as B
   */
  private Color                     colorB;
  /**
   * the adjacency matrix of the given graph
   */
  private int[][]                   adjacencyMatrix;
  /**
   * the header text
   */
  private Text                      header;
  /**
   * the colored rect behind the header text
   */
  private Rect                      hRect;
  /**
   * globally defined text properties
   */
  private TextProperties            textProps;
  /**
   * the main graph
   */
  private Graph                     graph;
  private Graph                     graphA;
  private Graph                     graphB;
  /**
   * graph properties of the main graph
   */
  private GraphProperties           graphProps;
  private GraphProperties           graphPropsA;
  private GraphProperties           graphPropsB;
  /**
   * false if the algorithm terminates on a non-bipartite graph, else true
   */
  private boolean                   isBipartite;
  /**
   * the source code of the algorithm
   */
  private SourceCode                src;
  /**
   * shows which elements are tagged as A
   */
  private IntArray                  setA;
  /**
   * shows which elements are tagged as B
   */
  private IntArray                  setB;
  /**
   * indicates how much elements are tagged as A
   */
  private int                       amountTaggedA;
  /**
   * indicates how much elements are tagged as B
   */
  private int                       amountTaggedB;
  /**
   * the stack, that is used by the algorithm
   */
  private Stack<Integer>            stack;
  /**
   * the visualization of the stack
   */
  private IntArray                  stackArray;
  /**
   * indicates how much elements are contained in the stack
   */
  private int                       stackContent;
  /**
   * amount of vertices in the graph
   */
  private int                       nodes;
  /**
   * just a counter for the statistics
   */
  private int                       loopCounter;
  /**
   * memorizes the highest number of elements in stack
   */
  private int                       highestStackContent;
  /**
   * the current spectated vertex
   */
  private Text                      currVertex;
  /**
   * the current spectated neighbour
   */
  private Text                      currNeighbour;
  private Text                      true1;
  private Text                      true2;
  private Text                      false1;
  private Text                      false2;
  private Rect                      cvBG;
  private Rect                      cnBG;
  /**
   * indicates whether a vertex has already been tagged
   */
  private HashMap<Integer, Boolean> isTagged;
  /**
   * shows the Color of one tagged Vertex true = ColorA false = ColorB
   */
  private HashMap<Integer, Boolean> taggedColor;

  public void init() {
    lang = new AnimalScript("Bipartition", "Dominik Pfau", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    highlightColor = (Color) primitives.get("highlightColor");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProps");
    colorA = (Color) primitives.get("colorA");
    colorB = (Color) primitives.get("colorB");
    adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");

    buildGraph();

    start();

    return lang.toString();
  }

  /**
   * initializes the animation, shows a start page with a description. then
   * shows the graph and the source code and calls the algorithm
   */
  private void start() {

    // show the header with a heading surrounded by a rectangle
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    header = lang.newText(new Coordinates(20, 30), "Checking for Bipartition",
        "header", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    hRect = lang.newRect(
        new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5,
            5, "header", "SE"), "hRect", null, rectProps);

    // show the description
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 14));
    lang.newText(
        new Coordinates(10, 100),
        "A bipartite graph is a graph whose vertices can be devided into two disjoint sets A and B such that every edge ",
        "description1", null, textProps);
    lang.newText(
        new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
        "connects a vertex in A to one in B. This algorithm uses depth first search (DFS) to determine if a given graph is",
        "description2", null, textProps);
    lang.newText(
        new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
        "bipartite. While iterating over all vertices (via DFS) it tags them alternately as A or B (depending on how the ",
        "description3", null, textProps);
    lang.newText(
        new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
        "ancestor was tagged). If it finds a vertex which is tagged the same as its neighbour, the algorithm terminates",
        "description4", null, textProps);
    lang.newText(
        new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
        "(which means the graph is not bipartite). Otherwise the graph is bipartite, if the DFS finishes (and two",
        "description5", null, textProps);
    lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
        "independent sets A and B are found)", "description6", null, textProps);

    // hide the description and show the graph & the code
    lang.nextStep();

    lang.hideAllPrimitives(); // produces a blank page
    header.show();
    hRect.show();
    graph.show();

    src = lang.newSourceCode(
        new Offset(0, 30, graph, AnimalScript.DIRECTION_SW), "sourceCode",
        null, sourceCodeProps);
    src.addCodeLine("select starting vertex S and tag with color A", null, 0,
        null);
    src.addCodeLine("push S to stack", null, 0, null);
    src.addCodeLine("while (stack.empty == false)", null, 0, null);
    src.addCodeLine("pop element from stack", null, 1, null);
    src.addCodeLine("for all neighbours", null, 1, null);
    src.addCodeLine("if (untagged)", null, 2, null);
    src.addCodeLine("tag with color (other than ancestor)", null, 3, null);
    src.addCodeLine("push to stack", null, 3, null);
    src.addCodeLine("else if (tagged with same color as ancestor)", null, 2,
        null);
    src.addCodeLine("stop algorithm", null, 3, null);

    // show the arrays and the stack
    arrayPropsA = new ArrayProperties();
    arrayPropsB = new ArrayProperties();
    arrayPropsA.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, colorA);
    arrayPropsA.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayPropsB.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, colorB);
    arrayPropsB.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    setA = lang.newIntArray(new Offset(30, 0, "graph",
        AnimalScript.DIRECTION_NE), new int[nodes - 1], "setA", null,
        arrayPropsA);
    setB = lang.newIntArray(
        new Offset(0, 20, "setA", AnimalScript.DIRECTION_SW),
        new int[nodes - 1], "setB", null, arrayPropsB);
    amountTaggedA = 0;
    amountTaggedB = 0;
    // stack = lang.newArrayBasedStack(new Offset(0, 20, "setB",
    // AnimalScript.DIRECTION_SW), new ArrayList<Integer>(), "stack", null,
    // stackProps, nodes-1);
    ArrayProperties sp = new ArrayProperties();
    sp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    sp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    sp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, highlightColor);
    stackArray = lang.newIntArray(new Offset(0, 20, "setB",
        AnimalScript.DIRECTION_SW), new int[nodes - 1], "stackArray", null, sp);
    stack = new Stack<Integer>();
    stackContent = 0;

    // make the additional texts
    currVertex = lang.newText(new Offset(0, 20, "stackArray",
        AnimalScript.DIRECTION_SW), "currently viewed vertex:", "currVertex",
        null);
    currNeighbour = lang.newText(new Offset(0, 20, "currVertex",
        AnimalScript.DIRECTION_SW), "currently viewed neighbour:",
        "currNeighbour", null);

    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, highlightColor);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    cvBG = lang.newRect(new Offset(-5, -5, "currVertex",
        AnimalScript.DIRECTION_NW), new Offset(20, 5, "currVertex", "SE"),
        "cvBG", null, rp);
    cnBG = lang.newRect(new Offset(-5, -5, "currNeighbour",
        AnimalScript.DIRECTION_NW), new Offset(20, 5, "currNeighbour", "SE"),
        "cnBG", null, rp);
    cvBG.hide();
    cnBG.hide();

    true1 = lang.newText(new Offset(-40, 80, "sourceCode",
        AnimalScript.DIRECTION_NW), "true -->", "true1", null);
    true1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 150,
        0), null, null);
    false1 = lang.newText(new Offset(-50, 80, "sourceCode",
        AnimalScript.DIRECTION_NW), "false -->", "false1", null);
    false1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(200,
        0, 0), null, null);
    true2 = lang.newText(new Offset(-40, 128, "sourceCode",
        AnimalScript.DIRECTION_NW), "true -->", "true2", null);
    true2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 150,
        0), null, null);
    false2 = lang.newText(new Offset(-40, 128, "sourceCode",
        AnimalScript.DIRECTION_NW), "false -->", "false2", null);
    false2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(200,
        0, 0), null, null);
    true1.hide();
    true2.hide();
    false1.hide();
    false2.hide();

    Text setAText = lang.newText(new Offset(0, -17, "setA",
        AnimalScript.DIRECTION_NW), "set A", "setAText", null);
    setAText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, colorA, null,
        null);
    Text setBText = lang.newText(new Offset(0, -17, "setB",
        AnimalScript.DIRECTION_NW), "set B", "setBText", null);
    setBText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, colorB, null,
        null);
    lang.newText(new Offset(0, -17, "stackArray", AnimalScript.DIRECTION_NW),
        "stack", "stackText", null);

    // call the algorithm
    checkForBipartition();
  }

  /**
   * executes the algorithm on 'graph'
   */
  private void checkForBipartition() {

    isBipartite = true;
    loopCounter = 0;
    highestStackContent = 0;

    // "select starting vertex S and tag with color A"
    lang.nextStep("Initialization");
    src.highlight(0);
    int currElem = 0;
    tag(currElem, true);

    // "push S to stack"
    lang.nextStep();
    src.unhighlight(0);
    src.highlight(1);
    pushToStack(currElem + 1);
    stackArray.highlightCell(0, null, null);

    // "while (stack.empty == false)"
    lang.nextStep(String.valueOf(loopCounter + 1) + ". Iteration");
    stackArray.unhighlightCell(0, null, null);
    unhighlightAllCode();
    src.highlight(2);

    while (!stack.isEmpty()) {
      loopCounter++;

      // "pop element from stack"
      lang.nextStep();
      false2.hide();
      unhighlightAllCode();
      src.highlight(3);
      stackArray.highlightCell(0, null, null);

      lang.nextStep();
      stackArray.unhighlightCell(0, null, null);
      cvBG.show();
      currElem = popFromStack();
      showAsCurrVertex(currElem);
      currElem--;

      // "for all neighbours"
      lang.nextStep();
      cvBG.hide();
      unhighlightAllCode();
      src.highlight(4);

      for (Integer vertex : getNeighbours(currElem)) {

        // "if (untagged)"
        lang.nextStep();
        false2.hide();
        unhighlightAllCode();
        src.highlight(5);
        showAsCurrNeighbour(vertex + 1);
        stackArray.unhighlightCell(0, null, null);
        cnBG.show();
        if (!isTagged.get(vertex))
          true1.show();
        else
          false1.show();

        if (!isTagged.get(vertex)) {

          // "tag with color (other than ancestor)"
          lang.nextStep();
          cnBG.hide();
          true1.hide();
          unhighlightAllCode();
          src.highlight(6);
          tag(vertex, !taggedColor.get(currElem));

          // "push to stack"
          lang.nextStep();
          unhighlightAllCode();
          src.highlight(7);
          pushToStack(vertex + 1);
          stackArray.highlightCell(0, null, null);

        } else {

          // "else if (tagged with same color as ancestor)"
          lang.nextStep();
          cnBG.hide();
          false1.hide();
          unhighlightAllCode();
          src.highlight(8);
          if (taggedColor.get(vertex) == taggedColor.get(currElem))
            true2.show();
          else
            false2.show();

          if (taggedColor.get(vertex) == taggedColor.get(currElem)) {

            // "stop algorithm
            lang.nextStep("Stop Algorithm");
            true2.hide();
            unhighlightAllCode();
            src.highlight(9);

            // stop the actual algorithm -> graph not bipartite
            lang.nextStep();
            isBipartite = false;
            break;
          }
        }
      }
      if (!isBipartite)
        break;
    }

    // algorithm is over, show some statistics
    lang.nextStep("Conclusion");
    lang.hideAllPrimitives(); // produces a blank page
    header.show();
    hRect.show();

    if (isBipartite)
      lang.newText(new Coordinates(10, 100), "the given graph is bipartite",
          "outro1", null, textProps);
    else
      lang.newText(new Coordinates(10, 100),
          "the given graph is not bipartite", "outro1", null, textProps);

    lang.newText(new Offset(0, 50, "outro1", AnimalScript.DIRECTION_SW),
        "number of (while)- iterations: " + loopCounter, "outro2", null,
        textProps);
    lang.newText(new Offset(0, 20, "outro2", AnimalScript.DIRECTION_SW),
        "highest number of elements in stack: " + highestStackContent,
        "outro3", null, textProps);
    lang.newText(new Offset(0, 20, "outro3", AnimalScript.DIRECTION_SW),
        "complexity: O(|V|^2), since the algorithm uses DFS", "outro4", null,
        textProps);

    lang.newText(
        new Offset(0, 50, "outro4", AnimalScript.DIRECTION_SW),
        "lots of characteristics apply to a bipartite graph. for example it does not contain any odd cycles. although there are few ",
        "outro5", null, textProps);
    lang.newText(
        new Offset(0, 10, "outro5", AnimalScript.DIRECTION_SW),
        "apllications for bipartite graphs (mostly assignment issues), lots of graph characteristics can be computed with ",
        "outro6", null, textProps);
    lang.newText(new Offset(0, 10, "outro6", AnimalScript.DIRECTION_SW),
        "considerably less effort on bipartite graphs than usual.", "outro7",
        null, textProps);

  }

  /**
   * Returns a List of all the Neighbours of a Node
   * 
   * @param nodeNumber
   * @return ArrayList with Nighbours
   */
  private ArrayList<Integer> getNeighbours(int nodeNumber) {

    ArrayList<Integer> tmp = new ArrayList<Integer>();

    int[] adj = graph.getEdgesForNode(nodeNumber);

    for (int i = 0; i < adj.length; i++) {
      if (adj[i] == 1) {
        tmp.add(i);
        graph.highlightNode(i, null, null);
      }
    }

    return tmp;
  }

  /**
   * tags the vertex n with color A or B
   * 
   * @param n
   * @param isA
   *          true = A, false = B
   */
  private void tag(int n, boolean isA) {

    // graph.hideNode(n, null, null);

    if (isA) {
      graphA.showNode(n, null, null);
      setA.put(amountTaggedA, n + 1, null, null);
      amountTaggedA++;
    } else {
      graphB.showNode(n, null, null);
      setB.put(amountTaggedB, n + 1, null, null);
      amountTaggedB++;
    }
    isTagged.put(n, true);
    taggedColor.put(n, isA);
  }

  /**
   * push method for the stack (both logical and visual)
   * 
   * @param i
   */
  private void pushToStack(int i) {
    stack.push(i);

    for (int x = stackContent; x > 0; x--) {
      stackArray.put(x, stackArray.getData(x - 1), null, null);
    }

    stackArray.put(0, i, null, null);
    stackContent++;
    if (stackContent > highestStackContent)
      highestStackContent = stackContent;
  }

  /**
   * pop method for the stack (both logical and visual)
   * 
   * @return
   */
  private int popFromStack() {
    stackContent--;

    for (int x = 0; x < stackContent; x++) {
      stackArray.put(x, stackArray.getData(x + 1), null, null);
    }
    stackArray.put(stackContent, 0, null, null);

    return stack.pop();
  }

  /**
   * shows the currently viewed vertex (as text)
   * 
   * @param i
   */
  private void showAsCurrVertex(int i) {
    currVertex.setText("currently viewed vertex: " + i, null, null);
  }

  private void showAsCurrNeighbour(int i) {
    currNeighbour.setText("currently viewed neighbour: " + i, null, null);
  }

  /**
   * builds a graph from the adjacencyMatrix
   */
  private void buildGraph() {

    nodes = adjacencyMatrix.length;

    isTagged = new HashMap<Integer, Boolean>();
    taggedColor = new HashMap<Integer, Boolean>();

    // complete the adjacency matrix
    for (int x = 0; x < nodes; x++) {
      for (int y = x; y < nodes; y++) {
        if ((adjacencyMatrix[x][y] == 1) ^ (adjacencyMatrix[y][x] == 1)) {
          adjacencyMatrix[x][y] = 1;
          adjacencyMatrix[y][x] = 1;
        }
      }
    }

    // define the nodes and their positions
    Node[] graphNodes = new Node[nodes];

    Point nodePos;
    for (int i = 0; i < nodes; i++) {
      nodePos = getPolyPoint(new Point(60, 100), 120, i + 1, nodes);
      graphNodes[i] = new Coordinates(nodePos.x, nodePos.y);
    }

    // define the names of the nodes + mark them as untagged
    String[] labels = new String[nodes];
    for (int i = 0; i < nodes; i++) {
      labels[i] = String.valueOf(i + 1);

      isTagged.put(i, false);
    }

    // graph aufbauen, sowie uebernanderliegende graphen unterschiedlicher farbe
    // (um verschiedenfarbigkeit zu erreichen)
    graphProps = new GraphProperties();
    graphProps.set("color", Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProps.set("highlightColor", highlightColor);
    graphProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    graphPropsA = new GraphProperties();
    graphPropsA.set("color", Color.BLACK);
    graphPropsA.set(AnimationPropertiesKeys.FILL_PROPERTY, colorA);
    graphPropsA.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

    graphPropsB = new GraphProperties();
    graphPropsB.set("color", Color.BLACK);
    graphPropsB.set(AnimationPropertiesKeys.FILL_PROPERTY, colorB);
    graphPropsB.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

    graph = lang.newGraph("graph", adjacencyMatrix, graphNodes, labels, null,
        graphProps);
    graph.hide();

    graphA = lang.newGraph("graphA", adjacencyMatrix, graphNodes, labels, null,
        graphPropsA);

    graphB = lang.newGraph("graphB", adjacencyMatrix, graphNodes, labels, null,
        graphPropsB);

    for (int i = 0; i < nodes; i++) {
      graphA.hideNode(i, null, null);
      graphB.hideNode(i, null, null);
    }
  }

  /**
   * Hilfsmethode, rechnet die Koordinaten des X-ten (number) Knotenpunkts für
   * den Graphen aus, so dass dieser ein gleichmäßiges Polygon bildet
   * 
   * @param ref
   *          Referenzpunkt = linker oberer koordinatenpunkt von dem aus der
   *          graph gezeichnet wird
   * @param radius
   *          Radius des äußeren Kreises des zu bildenden Polygons (sollte die
   *          Hälfte der Seitenfläche sein, auf der der graph gezeichnet wird)
   * @param number
   *          X-ter Knotenpunkt
   * @param nodes
   *          anzahl Knoten
   * @return Koordinaten des X-ten Knotens
   */
  private Point getPolyPoint(Point ref, int radius, int number, int nodes) {

    Point origin = new Point(ref.x + radius, ref.y + radius);
    double angle = Math.toRadians((360 / nodes) * number);

    int resX = (int) (radius * Math.sin(angle));
    int resY = (int) (radius * Math.cos(angle));

    return new Point(origin.x + resX, origin.y + resY);
  }

  /**
   * unhighlightes all 10 lines of 'src'
   */
  private void unhighlightAllCode() {
    for (int i = 0; i < 10; i++) {
      src.unhighlight(i);
    }

  }

  public String getName() {
    return "Bipartition";
  }

  public String getAlgorithmName() {
    return "Checking for Bipartition";
  }

  public String getAnimationAuthor() {
    return "Dominik Pfau";
  }

  public String getDescription() {
    return "A bipartite graph is a graph whose vertices can be devided into two disjoint sets A and B such that every edge connects a vertex in A to one in B. "
        + "\n"
        + "This algorithm uses depth first search (DFS) to determine if a given graph is bipartite.";
  }

  public String getCodeExample() {
    return "select starting vertex S and tag with color A" + "\n"
        + "push S to stack" + "\n" + "while stack is not empty" + "\n"
        + "          pop first element from stack" + "\n"
        + "          for all neighbours" + "\n"
        + "                    if (untagged)" + "\n"
        + "                              tag with color (other than ancestor)"
        + "\n" + "                              push to stack" + "\n"
        + "                    else if (tagged with same color as ancestor)"
        + "\n" + "                              stop algorithm";
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

    int[][] aMatrix = (int[][]) arg1.get("adjacencyMatrix");

    if (aMatrix.length != aMatrix[0].length)
      throw new IllegalArgumentException("Invalid Adjacency Matrix!");

    return true;
  }

}