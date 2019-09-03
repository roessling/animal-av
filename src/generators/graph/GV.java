package generators.graph;

//package generators.graph.prim;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleArray;
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

/**
 * @author Dan Le
 * @version 1.0 2012-06-07
 */
public class GV implements Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language             language;

  /**
   * The header text including the headline
   */
  private Text                 header;

  /**
   * The rectangle around the headline
   */
  private Rect                 hRect;

  /**
   * Globally defined text properties
   */
  private TextProperties       textProps;

  /**
   * the source code shown in the animation
   */
  private SourceCode           src;

  /**
   * Globally defined source code properties
   */
  private SourceCodeProperties sourceCodeProps;

  // Pseudo Code
  final String[]               pseudoCode = {
      "void computeShortestPath (Node x) {",// 0
      "	 markAsVisited(x)",// 1
      "	 init_shortest()",// 2
      "	 currentStage.add(x)",// 3
      "	 while(currentStage is not empty){",// 4
      " 	 	for(Node n : currentStage){",// 5
      "    		for (Node y: Adjacent from n){",// 6
      "       		 if (not visited(y)) {",// 7
      "       	     	nextStage.add(y);",// 8
      "       	     	markAsVisited(y);",// 9
      "       	     	nshortest(y) += nshortest(n)",// 10
      "      		  	}else if(y not in currentStage)",// 11
      "       	     	nshortest(y) += nshortest(x)",// 12
      "    		}",// 13
      "    		currentStage = nextStage",// 14
      "    		nextStage.clear()",// 15
      " 	 	}", // 16
      "	 }",// 17
      "}",// 18
      "void computeBetweenness() {",// 19
      "	 temp = zeros(size);",// 20
      "	 resetVisited()",// 21
      "	 while(currentStage is not empty){",// 22
      " 	 	for(Node n : currentStage){",// 23
      "    		for (Node j: Adjacent from n){",// 24
      "       		 if(!visited[j] && !currentStage.contains(j)){",// 25
      "       	     	infMatrix[i][j] = nshortest[j]*(temp[i]+1)/nshortest[i];",// 26
      "       	     	temp[j] += infMatrix[i][j];",// 27
      "       	     	nextStage.add(j);",// 28
      "       		 }",// 29
      "    		}",// 30
      "    		visited[i] = true;",// 31
      " 	 	}",// 32
      " 	 	currentStage.clear();",// 33
      " 	 	currentStage.addAll(nextStage);",// 34
      " 	 	nextStage.clear();",// 35
      "	 }",// 36
      "}"// 37
                                          };

  DoubleArray                  tempArray;
  IntArray                     nshortest;

  void printSourceCode() {
    src = language.newSourceCode(new Coordinates(10, 120), "SourceCode", null,
        sourceCodeProps);
    for (int i = 0; i < pseudoCode.length; i++) {
      src.addCodeLine(pseudoCode[i], null, 0, null);
    }
  }

  public void init() {
    language = new AnimalScript("Girvan Newman Algorithm", "Dan Le", 800, 600);
    language.setStepMode(true);
  }

  /**
   * Default constructor
   * 
   * @param language
   *          the concrete language object used for creating output
   */
  public GV(Language language) {
    this.language = language;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    language.setStepMode(true);
  }

  public GV() {
    language = new AnimalScript("Girvan Newman [EN]", "Dan Le", 1366, 768);
    language.setStepMode(true);
  }

  final String[] desc = {
      "The Girvan-Newman algorithm (named after Michelle Girvan and Mark Newman) is one of the methods used to detect communities in complex systems.",
      "A community consists of a subset of nodes within which the node-node connections are dense,and the edges to nodes in other communities are less dense.",
      "The algorithm detects those communities by successive removing edge that connect those communities.",
      "Edges are measured by betweeness, edge with high betweeness are more likely to connect different communities.",
      "The idea of the algorithm is to sort all edges according to its betweenness. (edges with highest betweenness will be removed to break the graph into smaller path.)",
      "Edge betweenness are computed as the number of shortest paths between pairs of nodes that run along it.",
      " ",
      "So the algorithm will:",
      "	+ 1. find compute the shortest path of every nodes ( vertex betweenness ) using BFS.",
      "	+ 2. compute edge betweenness",
      "	+ 3. remove edge with highest betweenness",
      "	+ 4. repeat until no edge left" };

  public void printDescription() {
    Text[] descArray = new Text[desc.length];
    for (int i = 0; i < desc.length; i++) {
      descArray[i] = language.newText(new Coordinates(10, 30 * (i + 3)),
          desc[i], "desc" + i, null);
      if (i < desc.length - 1)
        language.nextStep();
      else
        language.nextStep("Init");
    }
    for (int i = 0; i < desc.length; i++)
      descArray[i].hide();
  }

  /**
   * Initializes the animation. Shows a start page with a description. Then,
   * shows the graph and the source code and calls the Girvan Newman algorithm.
   * 
   * @param graph
   *          the graph
   */
  public void start(Graph graph) {
    graph.hide();
    // show the header with a heading surrounded by a rectangle
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    header = language.newText(new Coordinates(20, 30),
        "Girvan-Newman Algorithm", "header", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    hRect = language.newRect(new Offset(-5, -5, "header",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
        null, rectProps);

    // setup the start page with the description
    language.nextStep();
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));

    language.hideAllPrimitives();
    printDescription();
    header.show();
    hRect.show();
    src = language.newSourceCode(new Coordinates(390, 50), "sourceCode", null,
        sourceCodeProps);
    printSourceCode();
    language.nextStep();
    // call the prim algorithm
    girvan(graph);
  }

  private int[]   weigh;
  private int[][] excluded_edges;
  private Set<Integer> current_Stage, next_Stage;

  /**
   * Executes the girvan algorithm on the given graph.
   * 
   * @param graph
   *          the graph on which the girvan algorithm should be executed
   */
  private void girvan(Graph graph) {
    graph.show();
    // highlight the code lines with the initialization
    src.highlight(0);
    src.highlight(2);
    src.highlight(3);
    // show the nodes and edges already contained in the spanning tree
    Text currentStage = language.newText(new Coordinates(1200, 470),
        "Current Stage: ", "___abc", null, textProps);
    Text nextStage = language.newText(new Coordinates(1200, 570),
        "Next Stage: ", "___abc", null, textProps);
    // language.newText(new Offset(0, 25,
    // "currentStage",AnimalScript.DIRECTION_NW), "Next Stage:","___abc"
    // , null, textProps);

    // pick a random start node and highlight it
    language.nextStep();
    src.unhighlight(0);
    src.unhighlight(2);
    // src.unhighlight(3);
    src.highlight(6);
    int startNode = (int) Math.round((graph.getSize() - 1) * Math.random());
    graph.highlightNode(startNode, null, null);
    currentStage.setText("CurrentStage: " + graph.getNodeLabel(startNode),
        null, null);
    weigh = new int[graph.getSize()];
    nextStage.setText("NextStage: ", null, null);
    // execute the main loop
    language.nextStep();
    src.unhighlight(6);
    src.highlight(7);
    src.highlight(8);
    src.highlight(10);
    src.highlight(12);
    src.highlight(14);
    src.highlight(15);
    src.highlight(16);
    Set<Integer> vertexes = new HashSet<Integer>();
    vertexes.add(startNode);
    current_Stage = new HashSet<Integer>();
    next_Stage = new HashSet<Integer>();
    next_Stage.add(startNode);
    weigh[startNode] = 1;
    excluded_edges = graph.getAdjacencyMatrix().clone();

    nshortest = language.newIntArray(new Coordinates(1200, 170), weigh,
        "nshortest", null, arrayProps);

    graph_traverse(true, graph, vertexes, currentStage, nextStage);// top_down
    vertexes = new HashSet<Integer>(current_Stage);
    next_Stage = new HashSet<Integer>(current_Stage);
    for (int i = 0; i < graph.getSize(); i++) {
      graph.unhighlightNode(i, null, null);
      for (int j = 0; j < weigh.length; j++) {
        graph.unhighlightEdge(i, j, null, null);
      }
    }
    language.nextStep();

    src.unhighlight(3);
    src.unhighlight(7);
    src.unhighlight(8);
    src.unhighlight(10);
    src.unhighlight(12);
    src.unhighlight(14);
    src.unhighlight(15);
    src.unhighlight(16);

    src.highlight(21);
    src.highlight(22);

    language.nextStep();

    bWeight = new double[weigh.length];
    tempArray = language.newDoubleArray(new Coordinates(1200, 270), bWeight,
        "temp", null, arrayProps);

    graph_traverse(false, graph, vertexes, currentStage, nextStage);// bottom_up
  }

  private void unHighlightAll() {
    for (int i = 0; i < pseudoCode.length; i++)
      src.unhighlight(i);
  }

  private double[] bWeight;

  private void highlightrows(int begin, int end) {
    for (int i = begin; i < end; i++)
      src.highlight(i);
  }

  private ArrayList<Set<Integer>> stage_nodes = new ArrayList<Set<Integer>>();

  private void graph_traverse(boolean topdown, Graph graph,
      Set<Integer> vertexes, Text currentStage, Text nextStage) {
    unHighlightAll();
    int begin, end;
    if (topdown) {
      begin = 5;
      end = 16;
    } else {
      begin = 23;
      end = 32;
    }
    highlightrows(begin, end);
    current_Stage = new HashSet<Integer>(next_Stage);
    bWeight = new double[graph.getSize()];
    // compute edge with the minimal weight
    String temp = "";
    boolean break_condition = !next_Stage.isEmpty();
    int count = stage_nodes.size() - 1;
    while (break_condition) {
      current_Stage = new HashSet<Integer>(next_Stage);
      vertexes.addAll(current_Stage);
      next_Stage.clear();
      temp = "";
      // list of possible edges, e.g. [ (node1, node2, weight), ... ]
      List<ArrayList<Integer>> possibleEdges = getEdges(
          graph.getAdjacencyMatrix(), current_Stage);
      for (ArrayList<Integer> ai : possibleEdges) {
        // char c0 = (char) ('A' +ai.get(0)),c1 = (char) ('A' +ai.get(1));
        if (current_Stage.contains(ai.get(0))
            && current_Stage.contains(ai.get(1))) {
          graph.hideEdge(ai.get(0), ai.get(1), null, null);
          excluded_edges[ai.get(0)][ai.get(1)] = -1;
          excluded_edges[ai.get(1)][ai.get(0)] = -1;
          continue;
        }
        if (excluded_edges[ai.get(0)][ai.get(1)] == -1
            || excluded_edges[ai.get(1)][ai.get(0)] == -1)
          continue;
        boolean skip = false;
        for (int i = 0; i < 2; i++) {
          if (!vertexes.contains(ai.get(i))) {
            temp += " " + graph.getNodeLabel(ai.get(i));
            next_Stage.add(ai.get(i));
          }
        }
        if (skip) {
          excluded_edges[ai.get(0)][ai.get(1)] = -1;
          // excluded_edges[ai.get(1)][ai.get(0)] =-1;
          continue;
        }
        graph.highlightEdge(ai.get(0), ai.get(1), null, null);
        graph.highlightEdge(ai.get(1), ai.get(0), null, null);
        graph.highlightNode(ai.get(0), null, null);
        graph.highlightNode(ai.get(1), null, null);
        if (next_Stage.contains(ai.get(1))) {
          if (topdown) {
            weigh[ai.get(1)] += weigh[ai.get(0)];
            nshortest.hide();
            nshortest = language.newIntArray(new Coordinates(1200, 170), weigh,
                "nshortest", null, arrayProps);
            nshortest.highlightCell(ai.get(1), null, null);
          } else {
            double edge_weight = ((double) weigh[ai.get(1)] * (bWeight[ai
                .get(0)] + 1)) / weigh[ai.get(0)];
            // System.out.println("% " +c0+" - "+c1+"  :"+ edge_weight);
            bWeight[ai.get(1)] += edge_weight;
            tempArray.hide();
            tempArray = language.newDoubleArray(new Coordinates(1200, 270),
                bWeight, "temp", null, arrayProps);
            tempArray.highlightCell(ai.get(1), null, null);

            graph.setEdgeWeight(ai.get(0), ai.get(1),
                String.format("%.2f", edge_weight), null, null);
            graph.setEdgeWeight(ai.get(1), ai.get(0),
                String.format("%.2f", edge_weight), null, null);
            language.nextStep();
          }
        }
        if (!temp.equals(""))
          nextStage.setText(nextStage.getText() + temp, null, null);
        vertexes.add(ai.get(0));
        vertexes.add(ai.get(1));
      }

      count--;
      if (!topdown && count >= 0) {
        if (next_Stage.size() < stage_nodes.get(count).size()) {
          temp = debugSet(stage_nodes.get(count));
          nextStage.setText(nextStage.getText() + temp, null, null);
          language.nextStep();
        }
      }
      if (!topdown)
        next_Stage = (count >= 0) ? new HashSet<Integer>(stage_nodes.get(count))
            : new HashSet<Integer>();
      if (!temp.equals("")) {
        currentStage.setText("CurrentStage: " + temp, null, null);
        language.nextStep();
      }
      break_condition = topdown ? !next_Stage.isEmpty() : count >= 0;
      if (topdown)
        stage_nodes.add(current_Stage);
    }
  }

  private String debugSet(Set<Integer> s) {
    StringBuffer sb = new StringBuffer();
    for (int i : s) {
      char c = (char) ('A' + i);
      sb.append(c + " ");
    }
    sb.append("\n");
    return sb.toString();
  }

  /**
   * Computes the edges that are accessible from the given set of vertexes and
   * that are not contained in the given list.
   * 
   * @param adjacencyMatrix
   *          the graph's adjacency matrix
   * @param vertexes
   *          the vertexes from which the resulting edges should be accessible
   * @return the edges in the form [ (node1, node2, weight), ... ] that are
   *         accessible from the given set of vertexes and that are not
   *         contained in the given list.
   */
  private List<ArrayList<Integer>> getEdges(int[][] adjacencyMatrix,
      Set<Integer> vertexes) {
    List<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
    for (int vertex : vertexes) {
      for (int i = 0; i < adjacencyMatrix.length; i++) {
        int weight = adjacencyMatrix[vertex][i];
        ArrayList<Integer> currentVertex1 = new ArrayList<Integer>();
        currentVertex1.add(vertex);
        currentVertex1.add(i);
        currentVertex1.add(weight);
        ArrayList<Integer> currentVertex2 = new ArrayList<Integer>();
        currentVertex2.add(i);
        currentVertex2.add(vertex);
        currentVertex2.add(weight);
        // check whether the edge is contained in one or another
        // direction and if not, add it to the result list
        if (weight != 0)
          result.add(currentVertex1);
      }
    }
    return result;
  }

  /**
   * Computes a string containing a comma-separated list of names of the given
   * vertexes.
   * 
   * @param vertexes
   *          the vertexes that should be contained in the result string
   * @param graph
   *          the graph, in which the given vertexes are contained
   * @return a string containing a comma-separated list of names of the given
   *         vertexes
   */
  @SuppressWarnings("unused")
  private String getNodeLabels(Set<Integer> vertexes, Graph graph) {
    if (vertexes.size() == 0)
      return "";
    String result = "";
    for (int vertex : vertexes)
      result += graph.getNodeLabel(vertex) + ", ";
    result = result.substring(0, result.length() - 2);
    return result;
  }

  /**
   * Computes a string containing a list of the given edges in the form
   * "(node1, node2, weight), (node1, node2, weight), ...".
   * 
   * @param edges
   *          the edges that should be contained in the result string
   * @param graph
   *          the graph, in which the given edges are contained
   * @return a string containing a list of the given edges in the form
   *         "(node1, node2, weight), (node1, node2, weight), ...".
   */
  @SuppressWarnings("unused")
  private String getEdgeLabels(List<ArrayList<Integer>> edges, Graph graph) {
    if (edges.size() == 0)
      return "";
    String result = "";
    for (ArrayList<Integer> edge : edges)
      result += "(" + graph.getNodeLabel(edge.get(0)) + ", "
          + graph.getNodeLabel(edge.get(1)) + "), ";
    result = result.substring(0, result.length() - 2);
    return result;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // get the user defined primitives and properties
    Graph graph = (Graph) primitives.get("graph");

    int[][] matrix = graph.getAdjacencyMatrix();

    boolean isNull = true;
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix[0].length; j++)
        if (matrix[i][j] != 0)
          isNull = false;

    GraphProperties graphProps = (GraphProperties) props
        .getPropertiesByName("graphProps");
    if (isNull)
      graph = getDefaultGraph(graphProps);
    // create the graph again in order to be able to set the graph properties
    int size = graph.getSize();
    Node[] nodes = new Node[size];
    String[] nodeLabels = new String[size];
    for (int i = 0; i < size; i++) {
      nodes[i] = graph.getNode(i);
      nodeLabels[i] = graph.getNodeLabel(i);
    }
    graph = language.newGraph(graph.getName(), graph.getAdjacencyMatrix(),
        nodes, nodeLabels, graph.getDisplayOptions(), graphProps);
    // (they seem to be the reason for our problems)
    // sourceCodeProps = (SourceCodeProperties) props
    // .getPropertiesByName("sourceCodeProps");
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);

    // I think we do not need to create an object of the class right here
    // PrimAPIGenerator primObject = new PrimAPIGenerator(language);
    start(graph);

    return language.toString();
  }

  private Graph getDefaultGraph(GraphProperties graphProps) {
    // define the edges of the graph
    int[][] graphAdjacencyMatrix =
    // A B C D E F G H I J K
    { { 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },// A
        { 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0 },// B
        { 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0 },// C
        { 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0 },// D
        { 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },// E
        { 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0 },// F
        { 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0 },// G
        { 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0 },// H
        { 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1 },// I
        { 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1 },// J
        { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 } };// K

    // define the nodes and their positions
    Node[] graphNodes = new Node[11];
    Coordinates offset = new Coordinates(150, 0);
    graphNodes[0] = new Coordinates(440, 400);// A
    graphNodes[1] = new Coordinates(440, 250);// B
    graphNodes[2] = new Coordinates(590, 250);// C
    graphNodes[3] = new Coordinates(590, 400);// D
    graphNodes[4] = new Coordinates(440, 550);// E
    graphNodes[5] = new Coordinates(590, 100);// F
    graphNodes[6] = new Coordinates(750, 400);// G
    graphNodes[7] = new Coordinates(590, 550);// H
    graphNodes[8] = new Coordinates(750, 250);// I
    graphNodes[9] = new Coordinates(750, 550);// J
    graphNodes[10] = new Coordinates(890, 400);// K

    for (int i = 0; i < graphNodes.length; i++) {
      Coordinates co = (Coordinates) graphNodes[i];
      graphNodes[i] = new Coordinates(co.getX() + offset.getX(), co.getY()
          + offset.getY());
    }

    // define the names of the nodes
    String[] labels = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" };

    Graph g = language.newGraph("graph", graphAdjacencyMatrix, graphNodes,
        labels, null, graphProps);
    g.hide();

    return g;
  }

  public String getName() {
    return "Girvan Newman [EN]";
  }

  public String getAlgorithmName() {
    return "Girvan Newman [EN]";
  }

  public String getAnimationAuthor() {
    return "Dan Le";
  }

  public String getDescription() {
    return stringAppend(desc);
  }

  public String getCodeExample() {
    return stringAppend(pseudoCode);
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

  private String stringAppend(String[] in) {
    StringBuffer sb = new StringBuffer();
    for (String s : in) {
      sb.append(s + "\n");
    }
    return sb.toString();
  }

  GraphProperties      graphProps  = new GraphProperties();
  ArrayProperties      arrayProps  = new ArrayProperties();
  SourceCodeProperties sourceProps = new SourceCodeProperties();

  private GraphProperties getGraphProperties() {
    return graphProps;
  }

  private void setProperties() {
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
    graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

    sourceCodeProps = new SourceCodeProperties();
    // for Source Code
    sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 12));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  public static void main(String[] args) {
    // Create a new animation
    // name, author, screen width, screen height
    Language l = new AnimalScript("Girvan Newman", "Dan Le", 1920, 1080);
    GV gN = new GV(l);
    gN.setProperties();
    GraphProperties gP = gN.getGraphProperties();
    Graph g = gN.getDefaultGraph(gP);
    gN.printSourceCode();
    gN.start(g);
    System.out.println(l);
  }
}
