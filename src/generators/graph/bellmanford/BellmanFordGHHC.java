package generators.graph.bellmanford;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.Edge;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class BellmanFordGHHC extends AnnotatedAlgorithm implements
    generators.framework.Generator {

  // private final String author = "Georgi Hadshiyski, Hristo Chonov";
  private final String algorithmName = "Bellman-Ford";
  private final String source_Code   = "Initialization\n   Set all predecessors to undefined\n   Starting Node Distance = 0\n   All other nodes Distance = Infinity\nRelax Edges Repeatedly\n   Repeat totalVertices-1 times\n      For each edge u->v\n         if(distance[u] + weight(u->v) < distance[v])\n            distance[v] = distance[u] + weight(u->v)\n            predecessor[v] = u";
  private Language     lang;                                                                                                                                                                                                                                                                                                                                                                         // The
                                                                                                                                                                                                                                                                                                                                                                                                      // concrete
                                                                                                                                                                                                                                                                                                                                                                                                      // language
                                                                                                                                                                                                                                                                                                                                                                                                      // object
                                                                                                                                                                                                                                                                                                                                                                                                      // used
                                                                                                                                                                                                                                                                                                                                                                                                      // for
                                                                                                                                                                                                                                                                                                                                                                                                      // creating
                                                                                                                                                                                                                                                                                                                                                                                                      // output

  Color                graph_highlightcolor;
  Color                graph_nodecolor;
  Color                graph_fill;
  Color                graph_edgecolor;

  MatrixProperties     maProps;
  GraphProperties      grProps;

  public BellmanFordGHHC() {

  }

  @Override
  public String getAnnotatedSrc() {
    return "Initialization								@label(\"init\")\n"
        + " Set all predecessors to undefined				@label(\"set_predecessors\")\n"
        + " Starting Node Distance = 0						@label(\"startNode_distance\")\n"
        + "	All other nodes Distance = Infinity				@label(\"otherNodes_distances\")\n"
        + "Relax Edges Repeatedly 							@label(\"relax_repeat\")\n"
        + " Repeat totalVertices-1 times					@label(\"repeat_n-1\")\n"
        + "  For each edge u->v								@label(\"for_loop\")\n"
        + "	  if(distance[u] + weight(u->v) < distance[v])	@label(\"condition\")\n"
        + "	   distance[v] = distance[u] + weight(u->v)		@label(\"set_distance\")\n"
        + "	   predecessor[v] = u							@label(\"predV_U\")\n"
        + "													@label(\"unhighlight\")\n";
  }

  public void init() {
    super.init();

    // Create header text
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30),
        "Bellman-Ford Demo by Georgi Hadshiyski & Hristo Chonov", "a", null,
        textProps);
    // Create rectangle around header text
    RectProperties rectProp = new RectProperties();
    lang.newRect(new Offset(-10, -10, header, "NW"), new Offset(10, 10, header,
        "SE"), "j", null, rectProp);
    lang.nextStep();

    // Create SourceCode
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sourceCode = lang.newSourceCode(new Coordinates(15, 180), "source_Code",
        null, scProps);

    maProps = new MatrixProperties();
    maProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 52));
    maProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.YELLOW);
    maProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    maProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    grProps = new GraphProperties();
    grProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        graph_highlightcolor);
    grProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, graph_nodecolor);
    grProps.set(AnimationPropertiesKeys.FILL_PROPERTY, graph_fill);
    grProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, graph_edgecolor);
    grProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.TRUE);
    grProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, Boolean.TRUE);

    parse();

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

    lang = new AnimalScript(algorithmName, "Georgi Hadshiyski, Hristo Chonov",
        640, 480);
    lang.setStepMode(true);

    // Get adjacency matrix
    Graph gr = (Graph) arg1.get("graph");
    int[][] matrix = gr.getAdjacencyMatrix();

    // Get node names
    String[] names = new String[matrix.length];
    for (int i = 0; i < names.length; i++)
      names[i] = gr.getNodeLabel(i);

    // Get node coordinates
    Coordinates[] coord = new Coordinates[matrix.length];
    for (int i = 0; i < coord.length; i++)
      coord[i] = (Coordinates) gr.getNode(i);

    init();
    algorithm(matrix, gr, names, coord);

    return lang.toString();
  }

  private void algorithm(int[][] matrix, Graph gr, String[] names,
      Coordinates[] coord) {

    // Create Graph

    Graph graph = lang.newGraph("BellmanFord", matrix, coord, names, null,
        grProps);

    // Get list of edges
    int totalEdges = 0;
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < graph.getEdgesForNode(i).length; j++)
        if (graph.getEdgesForNode(i)[j] != 0)
          totalEdges++;
    Edge[] edges = new Edge[totalEdges];
    int currentEdge = 0;
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < graph.getEdgesForNode(i).length; j++)
        if (graph.getEdgesForNode(i)[j] != 0) {
          edges[currentEdge] = new Edge(i, j, graph.getEdgesForNode(i)[j]);
          currentEdge++;
        }

    // Create Matrix
    String[][] nodesMatrix = new String[3][matrix.length + 1];
    nodesMatrix[0][0] = "Vertex";
    nodesMatrix[1][0] = "Distance";
    nodesMatrix[2][0] = "Predecessor";
    for (int i = 0; i < matrix.length; i++) {
      nodesMatrix[0][i + 1] = names[i];
      nodesMatrix[1][i + 1] = " ";
      nodesMatrix[2][i + 1] = " ";
    }

    StringMatrix nMatrix = lang.newStringMatrix(new Coordinates(15, 85),
        nodesMatrix, "Nodes", null, maProps);

    // Get starting node
    int source = -1;
    for (int i = 0; i < matrix.length; i++)
      if (gr.getNode(i) == gr.getStartNode())
        source = i;

    // Bellman-Ford Algorithm:

    // Initialization
    int[] distances = new int[matrix.length];
    int[] predecessors = new int[matrix.length];
    for (int i = 0; i < matrix.length; i++) {
      if (i == source)
        distances[i] = 0;
      else
        distances[i] = Integer.MAX_VALUE;
      predecessors[i] = -1;
    }

    // Animate Initialization
    exec("init");

    lang.nextStep();
    exec("set_predecessors");
    for (int i = 0; i < matrix.length; i++)
      nMatrix.put(2, i + 1, "--", null, null);

    lang.nextStep();
    exec("startNode_distance");
    nMatrix.put(1, source + 1, "0", null, null);

    lang.nextStep();
    exec("otherNodes_distances");
    for (int i = 0; i < matrix.length; i++)
      if (i != source) {
        nMatrix.put(1, i + 1, "Inf", null, null);
      }

    lang.nextStep();
    exec("relax_repeat");

    lang.nextStep();

    // Relax edges repeatedly
    for (int i = 0; i < matrix.length - 1; i++) {
      exec("repeat_n-1");

      lang.nextStep();
      for (int j = 0; j < edges.length; j++) {
        graph.highlightEdge(edges[j].from, edges[j].to, null, null);
        exec("for_loop");

        lang.nextStep();
        exec("condition");

        lang.nextStep();
        if (distances[edges[j].from] != Integer.MAX_VALUE
            && distances[edges[j].from] + edges[j].weight < distances[edges[j].to]) {
          nMatrix.put(1, edges[j].to + 1, new Integer(distances[edges[j].from]
              + edges[j].weight).toString(), null, null);
          exec("set_distance");

          lang.nextStep();
          nMatrix.put(2, edges[j].to + 1, gr.getNodeLabel(edges[j].from), null,
              null);
          exec("predV_U");

          lang.nextStep();
          exec("predV_U");
          if (predecessors[edges[j].to] != -1)
            graph.unhighlightEdge(predecessors[edges[j].to], edges[j].to, null,
                null);
          distances[edges[j].to] = distances[edges[j].from] + edges[j].weight;
          predecessors[edges[j].to] = edges[j].from;
        } else if (predecessors[edges[j].to] != edges[j].from)
          graph.unhighlightEdge(edges[j].from, edges[j].to, null, null);
      }
    }
    exec("unhighlight");
    lang.nextStep();

  }

  public String getAlgorithmName() {
    return "Bellman-Ford Algorithm";
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
    return "This generator generates an animation showing how to find single source shortest path in a directed graph using the Bellman-Ford algorithm";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  public String getName() {
    return "Bellman-Ford Shortest Path";
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }
}
