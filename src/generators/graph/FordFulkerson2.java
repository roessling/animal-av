package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class FordFulkerson2 implements ValidatingGenerator {
  private int[]          node_xPositions;
  private TextProperties standardText;
  private String[]       nodeLabels;
  private int[]          node_yPositions;
  private int[][]        adjacencyMatrix;
  private int            source, target;

  Text                   header;

  class Pair {
    public int a;
    public int b;

    public Pair(int a, int b) {
      this.a = a;
      this.b = b;
    }
  }

  private Language             lang;
  // private GraphProperties graph;
  private SourceCodeProperties sourceCode;
  // private TextProperties StandardText;
  private GraphProperties      graphProps;
  private RectProperties       box;

  public void init() {
    lang = new AnimalScript("Ford Fulkerson [EN]",
        "David Kaufmann, Holger Thies", 800, 600);
    lang.setStepMode(true);
  }

  private void dfsRek(Graph g, int node, Set<Integer> visited,
      Map<Integer, Integer> pred) {
    visited.add(node);
    int[][] adjMat = g.getAdjacencyMatrix();
    for (int i = 0; i < adjMat.length; i++) {
      if (adjMat[node][i] > 0 && !visited.contains(i)) {
        pred.put(i, node);
        dfsRek(g, i, visited, pred);
      }
    }
  }

  private ArrayList<Pair> dfs(Graph g, int s, int t) {
    Set<Integer> visited = new HashSet<Integer>();
    Map<Integer, Integer> pred = new HashMap<Integer, Integer>();
    ArrayList<Pair> path = new ArrayList<Pair>();
    dfsRek(g, s, visited, pred);
    int currNode = t;
    if (!pred.isEmpty()) {
      while (currNode != s) {
        path.add(0, new Pair(pred.get(currNode), currNode));
        currNode = pred.get(currNode);
      }
    }
    return path;
  }

  public void fordFulkersonAlgo(Graph g, SourceCode sc, int s, int t, int maxX,
      int maxY) {
    int min = Integer.MAX_VALUE;
    int maxFlow = 0;
    int foundPaths = 0;
    int foundEdges = 0;
    int[][] adj = g.getAdjacencyMatrix();
    int[][] originalAdj = new int[adj.length][adj.length];
    // save original adjaceny matrix
    for (int i = 0; i < adj.length; i++)
      for (int j = 0; j < adj.length; j++)
        originalAdj[i][j] = adj[i][j];

    Text incrementVar = lang.newText(new Coordinates(maxX + 50, 50), "",
        "incrementVar", null);
    Text maxFlowVar = lang.newText(new Coordinates(maxX + 50, 70),
        "maxFlow: 0", "maxFlowVar", null);
    Coordinates sourcePos = (Coordinates) sc.getUpperLeft();
    Text foundPathText = lang.newText(new Coordinates(sourcePos.getX() + 250,
        sourcePos.getY() + 30), "text", "foundPathText", null);
    Text updateIncrementText = lang.newText(new Coordinates(
        sourcePos.getX() + 250, sourcePos.getY() + 140), "",
        "updateIncrementText", null);
    updateIncrementText.changeColor(null, Color.BLUE, null, null);
    foundPathText.changeColor(null, Color.GREEN, null, null);
    // Find path
    ArrayList<Pair> path = dfs(g, s, t);
    // while augmenting path exists
    while (!path.isEmpty()) {
      foundPaths++;
      min = Integer.MAX_VALUE;
      // Hightlight while
      sc.highlight(1);
      foundPathText.setText("Found augmenting path", null, null);
      foundPathText.show();
      lang.nextStep("Iteration " + foundPaths);
      foundPathText.setText("", null, null);
      sc.unhighlight(1);
      // Unhighlight while
      sc.highlight(2);
      lang.nextStep();
      for (Pair e : path) {
        g.highlightEdge(e.a, e.b, null, null);
      }
      lang.nextStep();
      for (Pair e : path) {
        g.unhighlightEdge(e.a, e.b, null, null);
      }
      sc.unhighlight(2);
      lang.nextStep();
      sc.highlight(4);
      incrementVar.setText("Increment: infinity", null, null);
      incrementVar.changeColor(null, Color.RED, null, null);
      lang.nextStep();
      incrementVar.changeColor(null, Color.BLACK, null, null);
      sc.unhighlight(4);
      sc.highlight(5);
      lang.nextStep();
      sc.unhighlight(5);
      for (Pair e : path) {
        foundEdges++;
        sc.highlight(7);
        String minText = (min == Integer.MAX_VALUE) ? "infinity" : "" + min;
        updateIncrementText.setText(
            " = min(" + minText + "," + g.getEdgeWeight(e.a, e.b) + ")", null,
            null);
        updateIncrementText.show();
        min = Math.min(min, g.getEdgeWeight(e.a, e.b));
        incrementVar.setText("Increment: " + min, null, null);

        incrementVar.changeColor(null, Color.RED, null, null);
        g.highlightEdge(e.a, e.b, null, null);
        lang.nextStep();
        incrementVar.changeColor(null, Color.BLACK, null, null);
        updateIncrementText.setText("", null, null);
        g.unhighlightEdge(e.a, e.b, null, null);
      }
      lang.nextStep();

      sc.unhighlight(7);
      sc.highlight(10);
      lang.nextStep();
      sc.unhighlight(10);
      for (Pair e : path) {
        sc.highlight(11);
        g.highlightEdge(e.b, e.a, null, null);
        g.setEdgeWeight(e.b, e.a, g.getEdgeWeight(e.b, e.a) + min, null, null);
        lang.nextStep();
        sc.unhighlight(11);
        sc.highlight(12);
        g.unhighlightEdge(e.b, e.a, null, null);
        g.highlightEdge(e.a, e.b, null, null);
        g.setEdgeWeight(e.a, e.b, g.getEdgeWeight(e.a, e.b) - min, null, null);
        lang.nextStep();
        sc.unhighlight(12);
        g.unhighlightEdge(e.a, e.b, null, null);

      }
      maxFlow += min;
      maxFlowVar.changeColor(null, Color.RED, null, null);
      maxFlowVar.setText("maxFlow: " + maxFlow, null, null);
      sc.highlight(14);
      lang.nextStep();
      maxFlowVar.changeColor(null, Color.BLACK, null, null);
      sc.unhighlight(14);
      path = dfs(g, s, t);
    }
    lang.nextStep();
    // Highlight while
    sc.highlight(1);
    foundPathText.show();
    foundPathText.changeColor(null, Color.RED, null, null);
    foundPathText.setText("No augmenting path found!", null, null);
    lang.nextStep();
    foundPathText.hide();
    foundPathText.setText("", null, null);
    // Output no path found
    sc.unhighlight(1);
    lang.nextStep();
    // Show Ending Animation
    sc.highlight(16);
    lang.nextStep("Conclusion");
    sc.hide();
    incrementVar.hide();
    maxFlowVar.hide();
    // header.show();
    lang.newText(new Coordinates(20, maxY + 30),
        "The algorithm found a Maximum Flow of " + maxFlow, "finishText", null);
    lang.newText(new Coordinates(20, maxY + 50), "Found paths: " + foundPaths,
        "foundPathsText", null);
    lang.newText(new Coordinates(20, maxY + 70), "Used edges: " + foundEdges,
        "foundPipesText", null);
    lang.newText(
        new Coordinates(20, maxY + 90),
        "Used edges is the total number of edges traversed. If an edge is used multiple times it is counted multiple times.",
        "usedEdges2", null);
    for (int i = 0; i < originalAdj.length; i++) {
      for (int j = 0; j < originalAdj.length; j++) {
        if (originalAdj[i][j] > 0)
          g.setEdgeWeight(i, j,
              g.getEdgeWeight(j, i) + "/" + originalAdj[i][j], null, null);
        else
          g.hideEdge(i, j, null, null);
      }
    }
  }

  public void maxFlow(Graph g, GraphProperties graphProps,
      SourceCodeProperties scProps, TextProperties textProps, int source,
      int target, int maxX, int maxY) {
    // set start animation
    lang.newRect(new Coordinates(10, 20), new Coordinates(220, 49), "box",
        null, box);
    header = lang.newText(new Coordinates(20, 30), "Ford-Fulkerson", "header",
        null, textProps);
    Font f = new Font("Sans Serif", Font.BOLD, 24);
    header.setFont(f, null, null);
    lang.nextStep("Introduction");
    Text startText1 = lang
        .newText(
            new Coordinates(20, 50),
            "The Ford-Fulkerson-Algorithm finds the maximum flow from a source node s to a sink node t in a network graph.",
            "startText1", null, textProps);
    lang.nextStep();
    Text startText2 = lang
        .newText(
            new Coordinates(20, 65),
            "It can e.g. be used in a oil pipeline network to determine the amount of oil that can be transportet via the network",
            "startText2", null, textProps);
    lang.nextStep();
    Text startText3 = lang
        .newText(
            new Coordinates(20, 80),
            "The algorithm's time complexity is in O(|E|f), where |E| is the number of edges and f is the maximum flow.",
            "startText3", null, textProps);
    Text descText = lang.newText(new Coordinates(20, 100),
        "Description of the Algorithm:", "startText", null, textProps);
    lang.nextStep();
    Text step1 = lang
        .newText(
            new Coordinates(20, 120),
            "1) For every edge (u,v) the backward egde (v,u) is added. Its capacity is set to 0.",
            "step1", null, textProps);
    lang.nextStep();
    Text step2 = lang.newText(new Coordinates(20, 140),
        "2) Find a path from source to target node", "step2", null, textProps);
    lang.nextStep();
    Text step3 = lang
        .newText(
            new Coordinates(20, 160),
            "3) Walk through the path and find the minimal edge-capacity for all edges on the path",
            "step3", null, textProps);
    lang.nextStep();
    Text step4 = lang
        .newText(
            new Coordinates(20, 180),
            "4) decrement all edge-capacities on the path by the found minimum and increment the backward-capacity by this value",
            "step4", null, textProps);
    lang.nextStep();
    Text step5 = lang
        .newText(
            new Coordinates(20, 200),
            "5) increment the value of the current maximum flow by the found value",
            "step5", null, textProps);
    lang.nextStep();
    Text step6 = lang.newText(new Coordinates(20, 220),
        "6) repeat until no path from source to target is left", "step6", null,
        textProps);
    lang.nextStep();
    Text step7 = lang.newText(new Coordinates(20, 240),
        "7) Output maximum flow", "step7", null, textProps);
    lang.nextStep();
    startText1.hide();
    startText2.hide();
    startText3.hide();
    descText.hide();
    step1.hide();
    step2.hide();
    step3.hide();
    step4.hide();
    step5.hide();
    step6.hide();
    step7.hide();
    g.show();
    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Coordinates(40, maxY + 20),
        "sourceCode", null, scProps);
    sc.addCodeLine("FordFulkerson(s,t){", null, 0, null);
    sc.addCodeLine("while a path from s to t exists {", null, 1, null);
    sc.addCodeLine("choose any path p from s to t", null, 2, null);
    sc.addCodeLine("// determine amount that can be incremented", null, 2, null);
    sc.addCodeLine("int increment = infinity", null, 2, null);
    sc.addCodeLine("foreach edge e in p {", null, 2, null);
    sc.addCodeLine(
        "// The flow can max be incremented by the remaining capacity for each node",
        null, 3, null);
    sc.addCodeLine("increment = min(increment, e.weight - e.flow)", null, 3,
        null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("// increment flow", null, 2, null);
    sc.addCodeLine("foreach edge (u,v) in p{", null, 2, null);
    sc.addCodeLine("(u,v).flow += increment // increment flow for this node",
        null, 3, null);
    sc.addCodeLine(
        "(v,u).flow -= increment // decrement remaining capacity for this node",
        null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("maxFlow += increment", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("return maxFlow", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    lang.nextStep();
    fordFulkersonAlgo(g, sc, source, target, maxX, maxY);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    if (validateInput(props, primitives)) {
      node_xPositions = (int[]) primitives.get("node_xPositions");
      standardText = (TextProperties) props.getPropertiesByName("standardText");
      sourceCode = (SourceCodeProperties) props
          .getPropertiesByName("sourceCode");
      nodeLabels = (String[]) primitives.get("nodeLabels");
      node_yPositions = (int[]) primitives.get("node_yPositions");
      adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
      source = (Integer) primitives.get("source");
      target = (Integer) primitives.get("target");
      graphProps = (GraphProperties) props.getPropertiesByName("graph");
      box = (RectProperties) props.getPropertiesByName("box");
    } else {
      // set visual properties for graph
      GraphProperties graphProps = new GraphProperties();
      graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
      // graphProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
      graphProps
          .set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
      graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
      graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
          Color.green);
      graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.TRUE);
      graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, Boolean.TRUE);
      int[][] adjacencyMatrix = new int[4][4];
      adjacencyMatrix[0][1] = 4;
      adjacencyMatrix[0][2] = 2;
      adjacencyMatrix[1][2] = 3;
      adjacencyMatrix[1][3] = 1;
      adjacencyMatrix[2][3] = 6;
      nodeLabels = new String[4];
      nodeLabels[0] = "s";
      nodeLabels[1] = "1";
      nodeLabels[2] = "2";
      nodeLabels[3] = "t";
      node_xPositions = new int[] { 30, 230, 230, 430 };
      node_yPositions = new int[] { 160, 60, 260, 160 };
      source = 0;
      target = 3;
      graphProps = (GraphProperties) props.getPropertiesByName("graph");
      box = (RectProperties) props.getPropertiesByName("box");
      standardText = (TextProperties) props.getPropertiesByName("standardText");
      sourceCode = (SourceCodeProperties) props
          .getPropertiesByName("sourceCode");
    }
    Node[] nodes = new Node[node_xPositions.length];
    int maxY = 0;
    int maxX = 0;
    for (int i = 0; i < node_xPositions.length; i++) {
      nodes[i] = new Coordinates(node_xPositions[i], node_yPositions[i]);
      maxY = Math.max(maxY, node_yPositions[i]);
      maxX = Math.max(maxX, node_xPositions[i]);
    }
    Graph g = lang.newGraph("graph", adjacencyMatrix, nodes, nodeLabels, null,
        graphProps);
    g.hide();
    maxFlow(g, graphProps, sourceCode, standardText, source, target, maxX, maxY);
    return lang.toString();
  }

  public String getName() {
    return "Ford Fulkerson [EN]";
  }

  public String getAlgorithmName() {
    return "Ford Fulkerson";
  }

  public String getAnimationAuthor() {
    return "David Kaufmann, Holger Thies";
  }

  public String getDescription() {
    return "The Ford-Fulkerson-Algorithm finds the maximum flow from a source node s to a sink node t in a network graph.<br>"
        + "\n"
        + "It can e.g. be used in a oil pipeline network to determine the amount of oil that can be transportet via the network<br>"
        + "\n"
        + "The algorithm's time complexity is in O(|E|f), where |E| is the number of edges and f is the maximum flow.<br>"
        + "\n"
        + "Description of the Algorithm:<br>"
        + "\n"
        + "1) For every edge (u,v) the backward egde (v,u) is added. Its capacity is set to 0.<br>"
        + "\n"
        + "2) Find a path from source to target node<br>"
        + "\n"
        + "3) Walk through the path and find the minimal edge-capacity for all edges on the path<br>"
        + "\n"
        + "4) decrement all edge-capacities on the path by the found minimum and increment the backward-capacity by this value<br>"
        + "\n"
        + "5) increment the value of the current maximum flow by the found value<br>"
        + "\n"
        + "6) repeat until no path from source to target is left<br>"
        + "\n" + "7) output maximum flow";
  }

  public String getCodeExample() {
    return "FordFulkerson(s,t){ "
        + "\n"
        + " 	while a path from s to t exists {"
        + "\n"
        + " 		choose any path p from s to t"
        + "\n"
        + " 		// determine amount that can be incremented"
        + "\n"
        + " 		int increment = infinity"
        + "\n"
        + " 		foreach edge e in p {"
        + "\n"
        + "			// The flow can max be incremented by the remaining capacity for each node"
        + "\n"
        + "			increment = min(increment, e.weight - e.flow)"
        + "\n"
        + "		}"
        + "\n"
        + "		// increment flow"
        + "\n"
        + "		foreach edge (u,v) in p{"
        + "\n"
        + " 			(u,v).flow += increment // increment flow for this node"
        + "\n"
        + "			(v,u).flow -= increment // decrement remaining capacity for this node"
        + "\n" + "		}" + "\n" + " 		maxFlow += increment" + "\n" + "	}" + "\n"
        + "	return maxFlow" + "\n" + "}";
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
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    node_xPositions = (int[]) primitives.get("node_xPositions");
    standardText = (TextProperties) props.getPropertiesByName("standardText");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    nodeLabels = (String[]) primitives.get("nodeLabels");
    node_yPositions = (int[]) primitives.get("node_yPositions");
    adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
    source = (Integer) primitives.get("source");
    target = (Integer) primitives.get("target");
    graphProps = (GraphProperties) props.getPropertiesByName("graph");
    box = (RectProperties) props.getPropertiesByName("box");
    if (node_xPositions.length == node_yPositions.length
        && node_xPositions.length == nodeLabels.length
        && node_xPositions.length == adjacencyMatrix.length
        && node_xPositions.length == adjacencyMatrix[0].length
        && source < node_xPositions.length && target < node_xPositions.length
        && source >= 0 && target >= 0)
      return true;
    return false;
  }

}