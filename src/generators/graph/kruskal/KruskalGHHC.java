package generators.graph.kruskal;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.Edge;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

// Code written by Georgi Hadshiyski and Hristo Chonov

public class KruskalGHHC extends AnnotatedAlgorithm implements
    generators.framework.Generator {

  private Language lang; // The concrete language object used for creating
                         // output

  /**
   * Default constructor
   */
  public KruskalGHHC() {

  }

  private Edge[]       edges;                                                                                                                                                                                                                                                            // Contains
                                                                                                                                                                                                                                                                                          // all
                                                                                                                                                                                                                                                                                          // edges
                                                                                                                                                                                                                                                                                          // sorted
                                                                                                                                                                                                                                                                                          // by
                                                                                                                                                                                                                                                                                          // weight
  private int[][]      matrix;                                                                                                                                                                                                                                                           // Contains
                                                                                                                                                                                                                                                                                          // the
                                                                                                                                                                                                                                                                                          // adjacency
                                                                                                                                                                                                                                                                                          // matrix
                                                                                                                                                                                                                                                                                          // of
                                                                                                                                                                                                                                                                                          // the
                                                                                                                                                                                                                                                                                          // original
                                                                                                                                                                                                                                                                                          // graph
  private int[][]      resultmatrix;                                                                                                                                                                                                                                                     // Contains
                                                                                                                                                                                                                                                                                          // the
                                                                                                                                                                                                                                                                                          // adjacency
                                                                                                                                                                                                                                                                                          // matrix
                                                                                                                                                                                                                                                                                          // of
                                                                                                                                                                                                                                                                                          // the
                                                                                                                                                                                                                                                                                          // MST
                                                                                                                                                                                                                                                                                          // private
                                                                                                                                                                                                                                                                                          // final
                                                                                                                                                                                                                                                                                          // String
                                                                                                                                                                                                                                                                                          // author
                                                                                                                                                                                                                                                                                          // =
                                                                                                                                                                                                                                                                                          // "Georgi Hadshiyski & Hristo Chonov";
  private final String algorithmName = "Kruskal in undirected graph";
  private final String source_Code   = "Sort all edges according to weight\npos = 0\nwhile (highlightedEdges smaller than totalNodes AND pos smaller than totalEdges)\n   add edge at pos to the Spanning Tree\n   if(cycle exists) remove edge at pos from the Spanning Tree\n   pos++";
  // private Graph gr;
  // private int totalHighlighted; // Contains
  // the
  // current
  // number
  // of
  // highlighted
  // edges
  int                  pos;                                                                                                                                                                                                                                                              // Contains
                                                                                                                                                                                                                                                                                          // the
                                                                                                                                                                                                                                                                                          // current
                                                                                                                                                                                                                                                                                          // position
                                                                                                                                                                                                                                                                                          // in
                                                                                                                                                                                                                                                                                          // the
                                                                                                                                                                                                                                                                                          // array

  Color                graph_highlightcolor;
  Color                graph_nodecolor;
  Color                graph_fill;
  Color                graph_edgecolor;

  @Override
  public String getAnnotatedSrc() {
    return "Sort all edges according to weight						@label(\"sort_edges\")\n"
        + "pos = 0														@label(\"init_pos\")\n"
        + "while (highlightedEdges < totalNodes AND pos < totalEdges)	@label(\"while_header\")\n"
        + "	add edge at pos to the Spanning Tree						@label(\"add_edge\")\n"
        + "	 if(cycle exists) 											@label(\"check_cycle\")\n"
        + "	 	remove edge at pos from the Spanning Tree				@label(\"remove\")\n"
        + "	 pos++														@label(\"inc_pos\")\n"
        + "																@label(\"unhighlight\")";
  }

  @Override
  public void init() {
    super.init();

    pos = 0;

    // Create header text
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30),
        "Kruskal Demo by Georgi Hadshiyski & Hristo Chonov", "a", null,
        textProps);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 22));

    // Create rectangle around header text
    RectProperties rectProp = new RectProperties();
    lang.newRect(new Offset(-10, -10, header, "NW"), new Offset(10, 10, header,
        "SE"), "j", null, rectProp);

    lang.nextStep();

    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sourceCode = lang.newSourceCode(new Coordinates(15, 180), "source_Code",
        null, scProps);

    // parsing anwerfen
    parse();

  }

  private void getMST(String[] names, Coordinates[] coords)
      throws LineNotExistsException {

    // Create an adjency matrix for the result
    resultmatrix = new int[matrix.length][matrix.length];
    for (int i = 0; i < matrix.length; i++)
      Arrays.fill(resultmatrix[i], 0);

    // Create Graph
    GraphProperties grProps = new GraphProperties();

    grProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        graph_highlightcolor);
    grProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, graph_nodecolor);
    grProps.set(AnimationPropertiesKeys.FILL_PROPERTY, graph_fill);
    grProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, graph_edgecolor);

    grProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.TRUE);
    grProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, Boolean.FALSE);
    Graph gr = lang.newGraph("Kruskal", matrix, coords, names, null, grProps);

    lang.nextStep();

    exec("sort_edges");

    lang.nextStep();
    lang.nextStep();
    lang.nextStep();
    lang.nextStep();

    // Create edges array
    int total = 0;
    for (int i = 0; i < matrix.length; i++)
      for (int j = i; j < matrix.length; j++)
        if (gr.getEdgeWeight(i, j) > 0)
          total++;

    edges = new Edge[total];

    int current = 0;
    for (int i = 0; i < matrix.length; i++)
      for (int j = i; j < matrix.length; j++)
        if (gr.getEdgeWeight(i, j) > 0) {
          edges[current] = new Edge(i, j, gr.getEdgeWeight(i, j));
          current++;
        }

    // Sort edges
    Arrays.sort(edges);

    int[] sortedEdges = new int[edges.length];
    for (int i = 0; i < edges.length; i++)
      sortedEdges[i] = edges[i].weight;

    // Create array in animation
    ArrayProperties arrayProps = new ArrayProperties();

    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    IntArray ia = lang.newIntArray(new Coordinates(30, 130), sortedEdges,
        "edgesArray", null, arrayProps);

    lang.nextStep(); // Next step

    // Create position marker
    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "pos");
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ArrayMarker posMarker = lang.newArrayMarker(ia, 0, "pos", null,
        arrayIMProps);

    exec("init_pos");

    lang.nextStep(); // Next step

    try {
      // Start Kruskal Algorithm
      kruskalAlgorithm(gr, edges, posMarker);
      exec("while_header");

      lang.nextStep(); // Next step
      exec("unhighlight");
      posMarker.hide();
      ia.hide();

    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  // The actual Kruskal Algorithm
  private void kruskalAlgorithm(Graph graph, Edge[] edges, ArrayMarker posMarker) {
    int highlightedEdges = 0;
    exec("while_header");

    lang.nextStep(); // Next step
    for (Edge e : edges) {
      if (highlightedEdges + 1 == this.matrix.length)
        break;
      if (highlightedEdges < graph.getAdjacencyMatrix().length)
        exec("add_edge");
      graph.highlightEdge(e.from, e.to, null, null);
      // totalHighlighted++;
      highlightedEdges++;

      lang.nextStep(); // Next step
      exec("check_cycle");

      lang.nextStep(); // Next step
      for (int i = 0; i < edges.length; i++)
        edges[i].visited = false;
      if (!pathExists(e.from, e.to) && !pathExists(e.to, e.from)) {
        resultmatrix[e.from][e.to] = 1;
        resultmatrix[e.to][e.from] = 1;
      } else {
        highlightedEdges--;
        graph.unhighlightEdge(e.from, e.to, null, null);
        // totalHighlighted--;
        exec("remove");

        lang.nextStep(); // Next step
      }
      exec("inc_pos");
      pos++;
      if (pos == edges.length)
        posMarker.moveOutside(null, null);
      else
        posMarker.move(pos, null, null);
      lang.nextStep(); // Next step
    }
  }

  // Checks for a path from a source to a target
  private boolean pathExists(int source, int target) {
    for (int cur : getAllEdges(source))
      if ((getEdge(source, cur) != null && !getEdge(source, cur).visited)
          || (getEdge(cur, source) != null && !getEdge(cur, source).visited)) {
        try {
          getEdge(cur, source).visited = true;
        } catch (Exception e) {
        }
        ;
        try {
          getEdge(source, cur).visited = true;
        } catch (Exception e) {
        }
        ;
        if (cur == target)
          return true;
        else if (pathExists(cur, target) == true)
          return true;
      }
    return false;
  }

  // Returns an edge from the sorted edges array
  private Edge getEdge(int from, int to) {
    for (int j = 0; j < edges.length; j++)
      if (edges[j].from == from && edges[j].to == to)
        return edges[j];
    return null;
  }

  // Returns all target nodes from a source
  private int[] getAllEdges(int source) {
    int current = 0;
    int count = 0;
    for (int i = 0; i < resultmatrix.length; i++)
      if (resultmatrix[i][source] > 0)
        count++;
    int[] result = new int[count];
    for (int i = 0; i < resultmatrix.length; i++)
      if (resultmatrix[i][source] > 0) {
        result[current] = i;
        current++;
      }
    return result;
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    // Set colors
    graph_highlightcolor = (Color) arg0.elementAt(0)
        .getItem(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY).get();
    graph_nodecolor = (Color) arg0.elementAt(0)
        .getItem(AnimationPropertiesKeys.NODECOLOR_PROPERTY).get();
    graph_fill = (Color) arg0.elementAt(0)
        .getItem(AnimationPropertiesKeys.FILL_PROPERTY).get();
    graph_edgecolor = (Color) arg0.elementAt(0)
        .getItem(AnimationPropertiesKeys.EDGECOLOR_PROPERTY).get();

    // Create lang
    lang = new AnimalScript(algorithmName, "Georgi Hadshiyski, Hristo Chonov",
        640, 480);
    lang.setStepMode(true);

    // Create graph and adj Matrix
    Graph gr = (Graph) arg1.get("graph");
    this.matrix = gr.getAdjacencyMatrix();

    // Set Node Names
    String[] names = new String[this.matrix.length];
    for (int i = 0; i < names.length; i++)
      names[i] = gr.getNodeLabel(i);

    // Set Node Coordinates
    Coordinates[] coord = new Coordinates[this.matrix.length];
    for (int i = 0; i < coord.length; i++)
      coord[i] = (Coordinates) gr.getNode(i);

    init();
    getMST(names, coord);

    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Kruskal's Algorithm";
  }

  public String getAnimationAuthor() {
    return "Georgi Hadshiyski, Hristo Chonov";
  }

  public String getCodeExample() {
    return source_Code;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public String getDescription() {
    return "This generator generates an animation showing how to find a Minimum Spanning Tree in an undirected graph using the Kruskal Algorithm";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  public String getName() {
    return "Kruskal MST";
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }
}
