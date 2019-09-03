package generators.graph.depthfirstsearch;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

public class DepthFirstSearch extends AnnotatedAlgorithm implements Generator {

  private String               assignments = "Assignments";
  private String               compares    = "Compares";

  private Graph                graph;
  private Graph                drawGraph;
  private String[]             color       = new String[10];
  private Integer[]            predecessor = new Integer[10];
  private Integer[]            distance    = new Integer[10];
  private Integer[]            finished    = new Integer[10];
  private int                  time;
  private StringMatrix         stringMatrix;
  // private SourceCode sc;

  MsTiming                     tShort      = new MsTiming(100);
  MsTiming                     tLong       = new MsTiming(200);

  private RectProperties       rectProps;
  private TextProperties       textProps;
  // private GraphProperties graphProps;
  private MatrixProperties     matrixProps;
  private SourceCodeProperties scProps;

  @Override
  public String getAnimationAuthor() {
    return "Gamze Canova, Stephan Moczygemba";
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public String getAnnotatedSrc() {
    return "DFS(G)									@label(\"header1\") @declare(\"String\", \"u\") @declare(\"String\", \"visited\") @declare(\"String\", \"d\") @declare(\"String\", \"f\") @declare(\"int\", \"time\")  \n"
        + " for each node u \u2208 G do			@label(\"For1\") \n" + // current
                                                                 // inspected
                                                                 // node u
        "   visited[u] := false					@label(\"visitedFalse\") @inc(\""
        + assignments
        + "\") \n"
        + "   d[u] := \u221E						@label(\"varDistance\") @inc(\""
        + assignments
        + "\") \n"
        + "   f[u] := \u221E						@label(\"varFinishingTime\") @inc(\""
        + assignments
        + "\") \n"
        + "   \u03C0[u] := nil					@label(\"varPi\") @inc(\""
        + assignments
        + "\") \n"
        + "   time := 0							@label(\"init1\") @inc(\""
        + assignments
        + "\") \n"
        + "  for each node u \u2208 G	 			@label(\"For2\") \n"
        + "   do if visited[u] = false			@label(\"if1\") @inc(\""
        + compares
        + "\") \n"
        + "    then DFS-VISIT(u)					@label(\"thenDfsVisit\")\n"
        + "\n"
        +

        "DFS-VISIT(u)							@label(\"header2\")\n"
        + "  visited[u] := true 					@label(\"visitedTrue\") @inc(\""
        + assignments
        + "\") @set(\"visited\", \"true\") \n"
        + "  time := time + 1						@label(\"timeInc1\") @inc(\""
        + assignments
        + "\") @inc(\"time\") \n"
        + "  d[u] := time							@label(\"setTime\") @inc(\""
        + assignments
        + "\") @eval(\"d\", \"time\") \n"
        + "  for each v \u2208 Adj[u]				@label(\"For3\") \n"
        + "   do if visited[v] = false then		@label(\"if2\") @inc(\""
        + compares
        + "\") \n"
        + "    \u03C0[v] := u						@label(\"then2\") @inc(\""
        + assignments
        + "\") \n"
        + "    DFS-VISIT(v)						@label(\"endIf\") \n"
        +
        // "visited[u] := true" +
        "  time := time + 1						@label(\"timeInc2\") @inc(\"time\") @inc(\""
        + assignments
        + "\") \n"
        + "  f[u] := time							@label(\"fInc\") @inc(\""
        + assignments
        + "\") \n";

  }

  /**
   * initalization method
   */
  public void init() {
    super.init();

    Font font = new Font("SansSerif", Font.PLAIN, 16);

    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font);

    // instantiate source code primitive to work with
    sourceCode = lang.newSourceCode(new Coordinates(300, 120), "sumupCode",
        null, scProps);

    // setup complexity
    vars.declare("int", compares);
    vars.setGlobal(compares);
    vars.declare("int", assignments);
    vars.setGlobal(assignments);

    Text text = lang.newText(new Coordinates(300, 20), "...", "complexity",
        null);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Compares: ");
    tu.addToken(vars.getVariable(compares));
    tu.addToken(" - Assignments: ");
    tu.addToken(vars.getVariable(assignments));
    tu.update();

    // parsing anwerfen
    parse();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();

    graph = (Graph) primitives.get("graph");
    //
    //
    //
    // Node A = new Coordinates(80,100);
    // Node B = new Coordinates(150,100);
    // Node C = new Coordinates(80,220);
    // Node D = new Coordinates(150,220);
    // Node E = new Coordinates(200,250);
    // Node[] nodes = {A,B,C,D,E};
    //
    // //A = 0
    // //B = 1
    // //C = 2
    // //D = 3
    // //E = 4
    //
    // int[][] adjazenzMatrix = new int[5][5];
    // //0. Zeile
    // adjazenzMatrix[0][0] = 0;
    // adjazenzMatrix[0][1] = 1;
    // adjazenzMatrix[0][2] = 0;
    // adjazenzMatrix[0][3] = 0;
    // adjazenzMatrix[0][4] = 0;
    //
    // //1. Zeile
    // adjazenzMatrix[1][0] = 0;
    // adjazenzMatrix[1][1] = 0;
    // adjazenzMatrix[1][2] = 1;
    // adjazenzMatrix[1][3] = 1;
    // adjazenzMatrix[1][4] = 0;
    //
    // //2. Zeile
    // adjazenzMatrix[2][0] = 0;
    // adjazenzMatrix[2][1] = 0;
    // adjazenzMatrix[2][2] = 0;
    // adjazenzMatrix[2][3] = 1;
    // adjazenzMatrix[2][4] = 0;
    //
    // //3. Zeile
    // adjazenzMatrix[3][0] = 0;
    // adjazenzMatrix[3][1] = 0;
    // adjazenzMatrix[3][2] = 0;
    // adjazenzMatrix[3][3] = 0;
    // adjazenzMatrix[3][4] = 1;
    //
    // //4. Zeile
    // adjazenzMatrix[0][0] = 0;
    // adjazenzMatrix[0][1] = 0;
    // adjazenzMatrix[0][2] = 0;
    // adjazenzMatrix[0][3] = 0;
    // adjazenzMatrix[0][4] = 0;
    //
    //
    // String[] nodeLabels = {"A", "B", "C", "D", "E"};
    // DisplayOptions options = null;
    // GraphProperties graphprops = new GraphProperties();
    // graph = new Graph(null, "Graph", adjazenzMatrix, nodes, nodeLabels,
    // options, graphprops);

    textProps = new TextProperties();

    Object tempProperty = (Object) props.getPropertiesByName("header");

    /* just to get a large and bold title for whatever font is assigned */
    if (tempProperty.equals("Serif")) {
      textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
          Font.BOLD, 24));
    } else if (tempProperty.equals("Monospaced")) {
      textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.BOLD, 24));
    } else {
      textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "SansSerif", Font.BOLD, 24));
    }
    textProps = (TextProperties) props.getPropertiesByName("header");

    rectProps = new RectProperties();
    rectProps = (RectProperties) props.getPropertiesByName("headerRect");

    matrixProps = new MatrixProperties();
    matrixProps = (MatrixProperties) props.getPropertiesByName("matrix");

    scProps = new SourceCodeProperties();
    scProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");

    //
    dfs();

    System.out
        .println("\n\n\n========================== CODE ===========================\n\n"
            + lang.toString());

    return lang.toString();
  }

  private void dfs() {

    // ======================================================================================
    // =================================== NO ANIMATION, MATRIX GENERATION
    // ===============================
    // ======================================================================================

    // TextProperties textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("SansSerif", 1, 24));
    lang.newText(new Coordinates(50, 40), "Depth-First-Search", "header", null,
        textProps);
    // RectProperties rectProperties = new RectProperties();
    // rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.PINK);
    // rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY,
    // Boolean.TRUE);
    // rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null,
        rectProps);

    /*
     * displaying problems: 1. Graph: ELEMHIGHLIGHT_PROPERTY doesn't work,
     * ELEMHIGHLIGHT_PROPERTY = HIGHLIGHTCOLOR_PROPERTY 2. Matrix:
     * .highlightElem() doesn't work -> same effect as highlightCell() [same
     * animal script code will be produced that highlights both] 3. Matrix:
     * CELLHIGHLIGHT_PROPERTY macht nichts, ist immer gelb 3. Graph:
     * highlightNode() no duration delay is allowed! Otherwise you got a
     * IndexOutOfBoundsException
     */

    /* overview table for dfs */
    String[][] matrixData = new String[11][4];
    matrixData[0][0] = "x";
    matrixData[0][1] = "d[x]";
    matrixData[0][2] = "f[x]";
    matrixData[0][3] = "\u03C0[x]"; /* \u03C0 pi symbol */

    /* read out user's graph - limit it to 10 nodes max. */
    String[] labels = new String[10];
    Node[] nodes = new Node[10];
    // Node lastNode = null;
    // String lastLabel = "";
    for (int i = 0; i < 10; i++) {
      if (i < graph.getSize()) {
        labels[i] = graph.getNodeLabel(i);
        matrixData[i + 1][0] = graph.getNodeLabel(i); /*
                                                       * set nodes for overview
                                                       * table
                                                       */
        nodes[i] = graph.getNode(i);
        // lastNode = graph.getNode(i);
        // lastLabel= graph.getNodeLabel(i);
      } else {
        nodes[i] = new Coordinates(-1000, 200); /*
                                                 * cheat, empty nodes should not
                                                 * be displayed
                                                 */
        labels[i] = "";
      }
    }
    /* adjacency matrix needs to be of same size of nodes */
    int[][] extendedAdjacencyMatrix = new int[10][10];
    for (int i = 0; i < 10; i++) {
      for (int k = 0; k < 10; k++) {
        if (i < graph.getAdjacencyMatrix().length
            && k < graph.getAdjacencyMatrix().length) {
          extendedAdjacencyMatrix[i][k] = graph.getAdjacencyMatrix()[i][k];
        }
        System.out.println(i + " " + k + ": " + extendedAdjacencyMatrix[i][k]);
      }
    }

    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN); /*
                                                                                  * does
                                                                                  * nothing
                                                                                  * yet
                                                                                  * ...
                                                                                  */
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, Boolean.TRUE);
    drawGraph = lang.newGraph("graph", extendedAdjacencyMatrix, nodes, labels,
        null, graphProps);

    // MatrixProperties matrixProps = new MatrixProperties();
    // matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
    // Color.BLUE);
    // matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
    // Color.GREEN);
    // matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    stringMatrix = lang.newStringMatrix(new Offset(50, 30, "graph",
        AnimalScript.DIRECTION_SW), matrixData, "matrix", null, matrixProps);

    // ======================================================================================
    // =================================== ANIMATION STARTING
    // ===============================
    // ======================================================================================

    exec("header1");

    // nötig?????

    String u;
    /*
     * vars.set("u", u); vars.set("d", "\u221E"); vars.set("f", "\u221E");
     * vars.set("pi", "nil");
     */
    lang.nextStep();

    int i = 0;
    for (i = 0; i < graph.getSize(); i++) {

      exec("For1");

      lang.nextStep();

      drawGraph.highlightNode(i, null, null);
      stringMatrix.highlightElemColumnRange(i + 1, 0, 3, null, null);

      // u immer gleich aktuellem u setzen
      u = graph.getNodeLabel(i);
      vars.set("u", u);

      exec("visitedFalse");
      vars.set("visited", "false");
      color[i] = "white";

      // visited = 0 wird in annotation erledigt
      lang.nextStep();

      exec("varDistance");
      distance[i] = Integer.MAX_VALUE;
      stringMatrix.put(i + 1, 1, "\u221E", null, null); /*
                                                         * \u221E infinity
                                                         * symbol
                                                         */
      vars.set("d", "\u221E");
      lang.nextStep();

      exec("varFinishingTime");
      finished[i] = Integer.MAX_VALUE;
      stringMatrix.put(i + 1, 2, "\u221E", null, null);
      vars.set("f", "\u221E");
      lang.nextStep();

      exec("varPi");
      predecessor[i] = null;
      stringMatrix.put(i + 1, 3, "nil", null, null);
      // vars.set("pi", "nil");
      lang.nextStep();

      exec("init1");
      time = 0;
      vars.set("time", String.valueOf(time));

      lang.nextStep();

      drawGraph.unhighlightNode(i, null, null);
      stringMatrix.unhighlightElemColumnRange(i + 1, 0, 3, null, null);

      // ?? lang.nextStep();
    }

    exec("For1");
    stringMatrix.unhighlightElemColumnRange(i, 0, 3, null, null);
    // System.out.println("Graph size: " + graph.getSize());
    lang.nextStep();

    // time = 0;
    exec("For2");
    for (i = 0; i < graph.getSize(); i++) {
      lang.nextStep();

      u = graph.getNodeLabel(i);
      vars.set("u", u);
      drawGraph.highlightNode(i, null, null);

      exec("if1");

      lang.nextStep();

      if (color[i].equals("white")) {
        exec("thenDfsVisit");
        lang.nextStep();

        // hmmmm
        exec("header2");
        dfsVisit(i);

        exec("For2");
      } else {
        exec("For2");
      }
      drawGraph.unhighlightNode(i, null, null);
    }
    exec("header1");

  }

  private void dfsVisit(int i) {
    /*
     * at least no duration delay is allowed! Otherwise you got a
     * IndexOutOfBoundsException ...
     */
    String v = null;
    lang.nextStep();
    drawGraph.highlightNode(i, null, null);
    String u = graph.getNodeLabel(i);
    vars.set("u", u);
    // sc.unhighlight(18);
    exec("visitedTrue");
    // variable wird in annotation gesetzt
    color[i] = "grey";
    lang.nextStep();

    exec("timeInc1");
    // variable wird in annotation inkrementiert
    time += 1;
    lang.nextStep();

    exec("setTime");
    distance[i] = time;
    vars.set("d", String.valueOf(time));

    stringMatrix.highlightCell(i + 1, 1, null, null);
    stringMatrix.highlightElem(i + 1, 1, null, null);
    stringMatrix.put(i + 1, 1, Integer.toString(distance[i]), null, null);

    lang.nextStep();

    stringMatrix.unhighlightElem(i + 1, 1, null, null);

    exec("For3");
    lang.nextStep();

    int hasEdgeToNode;
    boolean forEntered = false;
    for (int k = 0; k < graph.getEdgesForNode(i).length; k++) {
      hasEdgeToNode = graph.getEdgesForNode(i)[k];
      if (hasEdgeToNode == 1) { /* there is an edge between these two nodes */
        forEntered = true;
        exec("if2");

        // sc.toggleHighlight(15, 16);
        System.out.println("Looking at edge of " + i + ": " + k);
        drawGraph.highlightEdge(i, k, null, tShort);

        // nun betrachte Knoten v (nachbar von u)
        v = drawGraph.getNodeLabel(i);

        lang.nextStep();
        if (color[k].equals("white")) {

          exec("then2");
          // vars.set("pi", v);
          predecessor[k] = i;

          stringMatrix.highlightCell(k + 1, 3, null, null);
          stringMatrix.highlightElem(k + 1, 3, null, null);
          stringMatrix.put(k + 1, 3, graph.getNodeLabel(i), null, null);

          lang.nextStep();

          stringMatrix.unhighlightElem(k + 1, 3, null, null);
          drawGraph.unhighlightNode(i, null, null);
          // drawGraph.unhighlightEdge(i, k, null, tShort);
          exec("endIf");
          lang.nextStep();
          exec("header2");

          dfsVisit(k);

          v = drawGraph.getNodeLabel(i);
          vars.set("u", v);
          drawGraph.highlightNode(i, null, null);

          exec("For3");
          lang.nextStep();
        } else {
          drawGraph.unhighlightEdge(i, k, null, tShort);

          exec("For3");
          lang.nextStep();
        }
        forEntered = false;
      }
    }

    if (forEntered)
      lang.nextStep(); // if forEntered then avoid double next step (for loop
                       // not entered)

    exec("timeInc2");
    v = drawGraph.getNodeLabel(i);
    vars.set("u", v);
    drawGraph.highlightNode(i, null, null);
    color[i] = "black";

    // System.out.println("Setting " + i + " to: black; \t Time: " + time);
    time += 1;
    // time in annotation inkrementiert
    lang.nextStep();

    exec("fInc");
    finished[i] = time;
    vars.set("f", String.valueOf(time));

    stringMatrix.highlightCell(i + 1, 2, null, null);
    stringMatrix.highlightElem(i + 1, 2, null, null);
    stringMatrix.put(i + 1, 2, Integer.toString(finished[i]), null, null);

    lang.nextStep();

    drawGraph.unhighlightNode(i, null, null);

    // hmm =)
    // sc.unhighlight(20);
    stringMatrix.unhighlightElem(i + 1, 2, null, null);

  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  /**
   * description of this animation
   */
  public String getDescription() {
    return "Depth-first search (DFS) is an algorithm for traversing or searching a tree, tree structure, or graph. One starts at the root (selecting some node as the root in the graph case) and explores as far as possible along each branch before backtracking.<br>Formally, DFS is an uninformed search that progresses by expanding the first child node of the search tree that appears and thus going deeper and deeper until a goal node is found, or until it hits a node that has no children. Then the search backtracks [note, that these edges are not marked in our animation], returning to the most recent node it hasn't finished exploring. In a non-recursive implementation, all freshly expanded nodes are added to a stack for exploration. <em>(Source: Wikipedia, 06/11/2010).</em> <p><b>Please note</b> that the animation works just up to a <b>maximum of <u>10 nodes</u>.</b>The remaining red-colored edges represent the <b>depth-first-search tree</b> of the given graph.</p> <br> <br> <h2>Graph Example</h2> <br><br> "
        + "%graphscript <br><br>"
        + "graph 4 directed <br><br>"
        + "graphcoordinates at 10 30 <br><br>"
        + "node A at 80 63 <br>"
        + "node B at 150 63 <br>"
        + "node C at 80 180 <br>"
        + "node D at 150 180 <br><br>"
        + "edge A B <br>"
        + "edge B C <br>"
        + "edge B D <br>" + "edge C D <br>";
  }

  /**
   * the type of this animation
   */
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  /**
   * title to get displayed
   */
  public String getName() {
    return "DepthFirstSearch [annotation based]";
  }

  /**
   * Name of the Algorithm
   */
  public String getAlgorithmName() {
    return "Depth-First Search";
  }
}