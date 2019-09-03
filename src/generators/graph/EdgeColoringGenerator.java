package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class EdgeColoringGenerator implements ValidatingGenerator {

  /**
   * The concrete language object used for creating output
   */
  private Language                 lang;

  /**
   * the amount of nodes of the graph
   */
  private int                      nodes;

  /**
   * the header text
   */
  private Text                     header;

  /**
   * the rect around the header
   */
  private Rect                     hRect;

  /**
   * globally defined textproperties
   */
  private TextProperties           textProps;

  /**
   * the algorithms sourcecode
   */
  private SourceCode               src;

  /**
   * globally defined source code properties
   */
  private SourceCodeProperties     sourceCodeProps;

  /**
   * the table which shows the colors of the graphs edges
   */
  private StringMatrix             table;

  /**
   * the table which shows the number of nodes and the loop-variables i & k
   */
  private StringMatrix             algoValues;

  /**
   * since the algorithm only uses numbers instead of colors, we can use this to
   * translate the numbers into colors (Strings) for the animation
   */
  private HashMap<Integer, String> colorStrings;
  /**
   * since the algorithm only uses numbers instead of colors, we can use this to
   * translate the numbers into colors for the animation
   */
  private HashMap<Integer, Color>  colors;
  /**
   * the graph object on which the algorithm works
   */
  private Graph                    graph;

  private Graph[]                  graphs;

  private Text                     comment;

  public void init() {
    lang = new AnimalScript("Edge Coloring", "Dominik Pfau", 1000, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    nodes = (Integer) primitives.get("nodes");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProps");

    graphs = new Graph[nodes];
    generateColors();
    generateGraph(nodes);

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
    header = lang.newText(new Coordinates(20, 30),
        "Minimum edge coloring of an even complete graph", "header", null,
        headerProps);
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
        "An even complete graph is a simple undirected graph with an even number of vertices, in which every pair is connected",
        "description1", null, textProps);
    lang.newText(
        new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
        "by a unique edge. An edge coloring of a graph G is a coloring of the edges of G such that adjacent edges (or the",
        "description2", null, textProps);
    lang.newText(
        new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
        "edges bounding different regions) receive different colors. An edge coloring containing the smallest possible number",
        "description3", null, textProps);
    lang.newText(
        new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
        "of colors for a given graph is known as a minimum edge coloring. This algorithm is a simple yet efficient way to produce",
        "description4", null, textProps);
    lang.newText(
        new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
        "a minimum edge coloring using the mathematic modulo-operation. During the algorithm, every color is represented by a",
        "description5", null, textProps);
    lang.newText(
        new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
        "number, which makes it a lot easier to write down. Note that the modulo-operations will sometimes contain values <= 0.",
        "description6", null, textProps);
    lang.newText(
        new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
        "The algorithm will add (n-1) to them (while n = number of vertices), so each number can be matched to a vertex of the",
        "description7", null, textProps);
    lang.newText(new Offset(0, 25, "description7", AnimalScript.DIRECTION_NW),
        "graph.", "description8", null, textProps);

    // hide the description and show the graph & the code
    lang.nextStep("Start");

    lang.hideAllPrimitives(); // produces a blank page
    header.show();
    hRect.show();
    graph.show();

    for (int g = 1; g < nodes; g++) {
      graphs[g].show();
      for (int i = 0; i < nodes; i++) {
        for (int k = 0; k < nodes; k++) {
          graphs[g].hideEdge(i, k, null, null);
        }
      }
    }

    src = lang.newSourceCode(
        new Offset(0, 30, graph, AnimalScript.DIRECTION_SW), "sourceCode",
        null, sourceCodeProps);
    src.addCodeLine("For (i = 1; i < n; i++)", null, 0, null);
    src.addCodeLine("Color Edge [i, n] with Color i", null, 1, null);
    src.addCodeLine("For (k = 1; k < n/2; k++)", null, 1, null);
    src.addCodeLine(
        "Color Edge [(i+k) mod (n-1), (i-k) mod (n-1)] with Color i", null, 2,
        null);

    // the extra text for the modulo-opertaion
    comment = lang.newText(new Offset(20, 0, "sourceCode",
        AnimalScript.DIRECTION_SW), " ", "comment", null);
    comment.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0,
        150, 0), null, null);

    // create the algoValues-table
    String[][] values = new String[1][6];
    values[0][0] = "n =";
    values[0][1] = " ";
    values[0][2] = "i =";
    values[0][3] = " ";
    values[0][4] = "k =";
    values[0][5] = " ";
    algoValues = lang.newStringMatrix(new Offset(0, 30, "sourceCode",
        AnimalScript.DIRECTION_SW), values, "val", null);

    // call the algorithm
    edgeColoring();

  }

  /**
   * executes the edge coloring algorithm on 'graph'
   */
  private void edgeColoring() {

    int n = nodes; // n = anzahl der knoten (kürzere schreibweise)
    int e1 = 0;
    int e2 = 1; // Hilfsvariablen für das Kantenhighlighting (e1, e2)

    int loopCount1 = 0; // Zählervariablen für die Anzahl Durchläufe der beiden
                        // Schleifen
    int loopCount2 = 0;

    makeTable(n); // Farbtabelle initialisieren
    algoValues.put(0, 1, String.valueOf(n), null, null);

    for (int i = 0; i < n - 1; i++) {

      loopCount1++;
      lang.nextStep(String.valueOf(loopCount1) + ". loop");
      unhighlightAllCode();
      comment.hide();
      src.highlight(0);
      updateAlgoValueI(i + 1);

      // Color Edge [i, n] with Color i
      lang.nextStep();
      unhighlightAllCode();
      src.highlight(1);
      e1 = i;
      e2 = n - 1;
      colorEdge(e1, e2, i + 1);
      insertToTable(e1 + 1, e2 + 1, i + 1);

      for (int k = 1; k < n / 2; k++) {

        loopCount2++;
        lang.nextStep();
        unhighlightAllCode();
        comment.hide();
        src.highlight(2);
        updateAlgoValueK(k);

        // Color Edge [(i+k) mod (n-1), (i-k) mod (n-1)] with Color i
        lang.nextStep();
        unhighlightAllCode();
        src.highlight(3);
        showModComment(i, k);
        e1 = (i + k) % (n - 1);
        e2 = (i - k) % (n - 1);
        if (e2 < 0)
          e2 = e2 + (n - 1);
        colorEdge(e1, e2, i + 1);
        insertToTable(e1 + 1, e2 + 1, i + 1);

      }

    }

    // algorithm is over, show some statistics
    lang.nextStep("statistics");
    lang.hideAllPrimitives(); // produces a blank page
    graph.hide();
    table.hide();
    algoValues.hide();
    header.show();
    hRect.show();

    lang.newText(new Coordinates(10, 100), "number of nodes: " + n, "outro1",
        null, textProps);
    lang.newText(new Offset(0, 25, "outro1", AnimalScript.DIRECTION_NW),
        "number of Edges: " + ((n * (n - 1)) / 2), "outro2", null, textProps);
    lang.newText(new Offset(0, 25, "outro2", AnimalScript.DIRECTION_NW),
        "number of colors needed: " + (n - 1), "outro3", null, textProps);

    lang.newText(new Offset(0, 50, "outro3", AnimalScript.DIRECTION_NW),
        "number of iterations " + "(total)", "outro4", null, textProps);
    lang.newText(new Offset(25, 25, "outro4", AnimalScript.DIRECTION_NW),
        "first loop: " + loopCount1, "outro5", null, textProps);
    lang.newText(new Offset(0, 25, "outro5", AnimalScript.DIRECTION_NW),
        "second loop: " + loopCount2, "outro6", null, textProps);

    lang.newText(new Offset(-25, 50, "outro6", AnimalScript.DIRECTION_NW),
        "complexity: O(E), since the algorithm iterates over all edges",
        "outro7", null, textProps);

    lang.newText(
        new Offset(0, 50, "outro7", AnimalScript.DIRECTION_NW),
        "This algorithm can be used to divide an even amount of entities into pairs such that each entity was grouped with",
        "outro8", null, textProps);
    lang.newText(
        new Offset(0, 25, "outro8", AnimalScript.DIRECTION_NW),
        "every single other one exactly once after a minimum number of steps. Imagine you would be assigned with planning",
        "outro9", null, textProps);
    lang.newText(
        new Offset(0, 25, "outro9", AnimalScript.DIRECTION_NW),
        "a sports league for the local table tennis club. To determine a ranking of the best players, every player has to",
        "outro10", null, textProps);
    lang.newText(
        new Offset(0, 25, "outro10", AnimalScript.DIRECTION_NW),
        "play a match against every other player once, while several matches between different players can take place at the",
        "outro11", null, textProps);
    lang.newText(
        new Offset(0, 25, "outro11", AnimalScript.DIRECTION_NW),
        "same time. Now how can you schedule the matches within a minimum number of rounds to be played? Imagine each",
        "outro12", null, textProps);
    lang.newText(
        new Offset(0, 25, "outro12", AnimalScript.DIRECTION_NW),
        "player to be a node in your graph. Every correct edge coloring is now a schedule where an edge means a match between",
        "outro13", null, textProps);
    lang.newText(
        new Offset(0, 25, "outro13", AnimalScript.DIRECTION_NW),
        "two players. Matches to be played in the same round have the same color, while the number of different colors is the",
        "outro14", null, textProps);
    lang.newText(
        new Offset(0, 25, "outro14", AnimalScript.DIRECTION_NW),
        "number of rounds. Since this algorithm operates on numbers, one could also interpret the output as Round number i",
        "outro15", null, textProps);
    lang.newText(new Offset(0, 25, "outro15", AnimalScript.DIRECTION_NW),
        "instead of Color i for this purpose.", "outro16", null, textProps);

  }

  /**
   * Used to color an Edge (e1, e2) in the visualization of the graph (which
   * actually contains of several graphs with different edgeColors)
   * 
   * @param v1
   * @param v2
   * @param color
   */
  private void colorEdge(int v1, int v2, int color) {

    int e1;
    int e2;

    if (v1 < v2) {
      e1 = v1;
      e2 = v2;
    } else {
      e1 = v2;
      e2 = v1;
    }

    graph.hideEdge(e1, e2, null, null);
    graphs[color].showEdge(e1, e2, null, null);
    graphs[color].highlightEdge(e1, e2, null, null);

  }

  /**
   * shows the comment under the sourcecode that should simplify the
   * modulo-operations
   * 
   * @param i
   * @param k
   */
  private void showModComment(int i, int k) {
    comment.setText(
        "//Color Edge [" + String.valueOf(i + 1 + k) + " mod "
            + String.valueOf(nodes - 1) + ", " + String.valueOf(i + 1 - k)
            + " mod " + String.valueOf(nodes - 1) + "] with Color "
            + String.valueOf(i + 1), null, null);
    comment.show();
  }

  /**
   * Hilfsmethode, die benutzt werden soll, um die Farbtabelle "table" mit
   * Einträgen zu füllen
   * 
   * @param e1
   *          Startknoten der Kante
   * @param e2
   *          Zielknoten der Kante
   * @param val
   *          Farbwert (Zahl)
   */
  private void insertToTable(int e1, int e2, int val) {

    table.put(e1, e2, String.valueOf(val), null, null);
    lang.addLine("setGridColor \"table[" + String.valueOf(e1) + "]["
        + String.valueOf(e2) + "]\" textColor " + colorStrings.get(val)
        + " refresh");
    table.put(e2, e1, String.valueOf(val), null, null);
    lang.addLine("setGridColor \"table[" + String.valueOf(e2) + "]["
        + String.valueOf(e1) + "]\" textColor " + colorStrings.get(val)
        + " refresh");
  }

  /**
   * Hilfsmethode zum Update der Anzeige der aktuellen Schleifenvariablen i
   * 
   * @param i
   */
  private void updateAlgoValueI(int i) {
    algoValues.put(0, 3, String.valueOf(i), null, null);
    lang.addLine("setGridColor \"val[0][3]\" textColor " + colorStrings.get(i)
        + " refresh");
  }

  /**
   * Hilfsmethode zum Update der Anzeige der aktuellen Schleifenvariablen k
   * 
   * @param k
   */
  private void updateAlgoValueK(int k) {
    algoValues.put(0, 5, String.valueOf(k), null, null);
  }

  /**
   * unhighlightes all 4 lines of 'src'
   */
  private void unhighlightAllCode() {
    for (int i = 0; i < 4; i++) {
      src.unhighlight(i);
    }

  }

  /**
   * Hilfsmethode, die die Tabelle mit den Kantenfarben initialisiert
   * 
   * @param n
   *          Anzahl der Knoten
   */
  private void makeTable(int n) {

    String[][] tableEntries = new String[n + 1][n + 1];

    tableEntries[0][0] = "Node";
    for (int i = 1; i <= n; i++) {
      tableEntries[0][i] = String.valueOf(i);
      tableEntries[i][0] = String.valueOf(i);
      for (int k = 1; k <= n; k++) {
        tableEntries[i][k] = "";
      }
    }
    table = lang.newStringMatrix(new Offset(50, 0, "graph",
        AnimalScript.DIRECTION_NE), tableEntries, "table", null);
  }

  /**
   * Fills the "Colors"-hashmap with colors
   */
  private void generateColors() {

    colorStrings = new HashMap<Integer, String>();
    colors = new HashMap<Integer, Color>();

    colorStrings.put(1, "green");
    colors.put(1, Color.GREEN);
    colorStrings.put(2, "blue");
    colors.put(2, Color.BLUE);
    colorStrings.put(3, "red");
    colors.put(3, Color.RED);
    colorStrings.put(4, "cyan");
    colors.put(4, Color.CYAN);
    colorStrings.put(5, "orange");
    colors.put(5, Color.ORANGE);
    colorStrings.put(6, "magenta");
    colors.put(6, Color.MAGENTA);
    colorStrings.put(7, "light Gray");
    colors.put(7, Color.LIGHT_GRAY);
    colorStrings.put(8, "pink");
    colors.put(8, Color.PINK);
    colorStrings.put(9, "yellow");
    colors.put(9, Color.YELLOW);
    if (nodes > 9) {
      for (int i = 10; i <= nodes + 9; i++) {
        colorStrings.put(i, "dark Gray");
        colors.put(i, Color.DARK_GRAY);
      }
    }
  }

  /**
   * initializes 'graph' with a new complete graph with the given (even) number
   * of nodes
   * 
   * @param nodes2
   */
  private void generateGraph(int nodes2) {
    // define the edges of the graph
    int[][] graphAdjacencyMatrix = new int[nodes2][nodes2];
    // initialize the adjacency matrix for a complete graph
    for (int i = 0; i < graphAdjacencyMatrix.length; i++)
      for (int j = 0; j < graphAdjacencyMatrix[0].length; j++)
        if (i != j)
          graphAdjacencyMatrix[i][j] = 1;

    // define the nodes and their positions
    Node[] graphNodes = new Node[nodes2];

    Point nodePos;
    for (int i = 0; i < nodes2; i++) {
      nodePos = getPolyPoint(new Point(60, 100), 120, i + 1);
      graphNodes[i] = new Coordinates(nodePos.x, nodePos.y);
    }

    // define the names of the nodes
    String[] labels = new String[nodes2];
    for (int i = 0; i < nodes2; i++) {
      labels[i] = String.valueOf(i + 1);
    }

    GraphProperties graphProps = new GraphProperties();
    graphProps.set("color", Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProps.set("highlightColor", Color.RED);
    graphProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    graph = lang.newGraph("graph", graphAdjacencyMatrix, graphNodes, labels,
        null, graphProps);
    graph.hide();

    // graphs[0] = graph;

    GraphProperties[] gProps = new GraphProperties[nodes];

    // generate more graphs with different edge colors
    for (int i = 1; i < nodes2; i++) {

      gProps[i] = new GraphProperties();
      gProps[i].set("color", Color.BLACK);
      gProps[i].set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
      gProps[i].set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, colors.get(i));
      gProps[i].set("highlightColor", colors.get(i));
      gProps[i].set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
      graphs[i] = lang.newGraph("graph" + String.valueOf(i),
          graphAdjacencyMatrix, graphNodes, labels, null, gProps[i]);
      graphs[i].hide();
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
   * @return Koordinaten des X-ten Knotens
   */
  private Point getPolyPoint(Point ref, int radius, int number) {

    Point origin = new Point(ref.x + radius, ref.y + radius);
    double angle = Math.toRadians((360 / nodes) * number);

    int resX = (int) (radius * Math.sin(angle));
    int resY = (int) (radius * Math.cos(angle));

    return new Point(origin.x + resX, origin.y + resY);
  }

  public String getName() {
    return "Edge Coloring";
  }

  public String getAlgorithmName() {
    return "Minimum edge coloring of an even complete graph";
  }

  public String getAnimationAuthor() {
    return "Dominik Pfau";
  }

  public String getDescription() {
    return "This algorithm produces a minimum edge coloring for any complete graph with even number of vertices. "
        + "\n"
        + "It workes on numbers (which then later can be interpreted as colors) and uses the modulo operatrion.";
  }

  public String getCodeExample() {
    return "For (i = 1; i < n; i++)"
        + "\n"
        + "     Color Edge [i, n] with Color i"
        + "\n"
        + "     For (k = 1; k < n/2; k++)"
        + "\n"
        + "          Color Edge [(i+k) mod (n-1), (i-k) mod (n-1)] with Color i";
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

    int givenNodes = (Integer) arg1.get("nodes");

    if (givenNodes == 0)
      throw new IllegalArgumentException("Number of nodes must not be zero!");

    int rest = givenNodes % 2;
    if (rest == 1)
      throw new IllegalArgumentException("Number of nodes must be even!");

    return true;
  }

}