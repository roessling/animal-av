package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimalVariablesGenerator;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import animal.variables.VariableRoles;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class EdmondsAlgorithm implements Generator {
  private Language                      lang;
  private Color                         edgeHighlightColor;
  private Color                         edgeHighlightColor2;
  private SourceCodeProperties          sourceCode;
  private GraphProperties               graphProps;
  private GraphProperties               graphProps2;
  private Graph                         g;
  private Graph                         AGraph;
  private int[][]                       graphCoords;
  private int                           xOffset         = 0;
  private int                           yOffset         = 100;
  private SourceCode                    sc;
  private AnimalVariablesGenerator      variables;
  private ArrayList<ArrayList<Integer>> contractedNodes = new ArrayList<ArrayList<Integer>>();

  private boolean                       Quiz;
  private boolean                       question1       = false;
  private boolean                       question2       = false;

  // IntroOutroTextProps
  private Color                         introColor;
  private Font                          introFont;
  private int                           introSize;
  private boolean                       introBold;
  private boolean                       introItalic;
  // TitleTextProps
  private Color                         titleColor;
  private Color                         titleBoxColor;
  private Font                          titleFont;
  private int                           titleSize;
  private boolean                       titleBold;
  private boolean                       titleItalic;

  public void init() {
    lang = new AnimalScript("Edmonds Algorithm [EN]", "Jan Wiesel", 1024, 768);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    variables = new AnimalVariablesGenerator(lang);

    // Load xml data
    edgeHighlightColor = (Color) primitives.get("highlightColor");
    ;
    edgeHighlightColor2 = (Color) primitives.get("highlightColor2");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    Quiz = (Boolean) primitives.get("quiz");
    g = (Graph) primitives.get("graph");
    int rootId = (Integer) primitives.get("rootNodeId");
    // IntroOutroTextPrimitives
    introColor = (Color) primitives.get("textColor");
    introFont = (Font) primitives.get("textFont");
    introSize = (Integer) primitives.get("textSize");
    introBold = (Boolean) primitives.get("textBold");
    introItalic = (Boolean) primitives.get("textItalic");
    // TitleTextPrimitives
    titleColor = (Color) primitives.get("titleColor");
    titleBoxColor = (Color) primitives.get("titleBoxColor");
    titleFont = (Font) primitives.get("titleFont");
    titleSize = (Integer) primitives.get("titleSize");
    titleBold = (Boolean) primitives.get("titleBold");
    titleItalic = (Boolean) primitives.get("titleItalic");

    // Init Quiz
    if (Quiz) {
      QuestionGroupModel groupInfo = new QuestionGroupModel("Question group", 3);
      lang.addQuestionGroup(groupInfo);
    }

    // Build the JGraph using the adjacency Matrix of g
    graphCoords = new int[g.getSize()][2];
    int maxRightCoord = 0;

    int[][] adjacencyMatrix = g.getAdjacencyMatrix();
    ArrayList<JNode> nodes = new ArrayList<JNode>();
    ArrayList<JEdge> edges = new ArrayList<JEdge>();
    for (int i = 0; i < g.getSize(); i++) {
      ArrayList<Integer> content = new ArrayList<Integer>();
      content.add(i);
      contractedNodes.add(content);

      Coordinates c = (Coordinates) g.getNode(i);
      graphCoords[i][0] = c.getX() + xOffset;
      graphCoords[i][1] = c.getY() + yOffset;
      if (c.getX() > maxRightCoord)
        maxRightCoord = c.getX();
      nodes.add(new JNode(i));
    }
    root = nodes.get(rootId);
    for (int i = 0; i < g.getSize(); i++) {
      for (int j = 0; j < g.getSize(); j++) {
        if (i != j && adjacencyMatrix[i][j] > 0)
          edges
              .add(new JEdge(nodes.get(i), nodes.get(j), adjacencyMatrix[i][j]));
      }
    }
    JGraph jgraph = new JGraph(nodes, edges);

    // Title
    // Title Text
    TextProperties titleProps = new TextProperties();
    titleProps.set("depth", 5);
    int titleStyle = 0;
    if (titleBold)
      titleStyle += Font.BOLD;
    if (titleItalic)
      titleStyle += Font.ITALIC;
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(titleFont.getName(), titleStyle, titleSize));
    titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, titleColor);
    Text title = lang.newText(new Coordinates(40, 40), "Edmonds Algorithm",
        "title", null, titleProps);
    // Title Box
    RectProperties rectProps = new RectProperties();
    rectProps.set("fillColor", titleBoxColor);
    rectProps.set("filled", true);
    rectProps.set("depth", 10);
    lang.newRect(new Offset(-5, -5, title, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, title, AnimalScript.DIRECTION_SE), "titlebox", null,
        rectProps);
    lang.nextStep();

    // Intro Text
    TextProperties textProps = new TextProperties();
    int introStyle = 0;
    if (introBold)
      introStyle += Font.BOLD;
    if (introItalic)
      introStyle += Font.ITALIC;
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(introFont.getName(), introStyle, introSize));
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, introColor);
    Text intro1 = lang
        .newText(
            new Offset(0, 40, title, AnimalScript.DIRECTION_SW),
            "Edmonds Algorithm calculates a minimum spanning tree for directed weighted graphs.",
            "intro1", null, textProps);
    Text intro2 = lang
        .newText(
            new Offset(0, 20, intro1, AnimalScript.DIRECTION_SW),
            "First, all edges directed at the root node are completely removed from the graph. For every other node one edge with minimal",
            "intro2", null, textProps);
    Text intro3 = lang
        .newText(
            new Offset(0, 0, intro2, AnimalScript.DIRECTION_SW),
            "weight that is directed at this node is chosen as a preliminary result. If these chosen edges do not form any cycle an optimal",
            "intro3", null, textProps);
    Text intro4 = lang.newText(new Offset(0, 0, intro3,
        AnimalScript.DIRECTION_SW),
        "spanning tree is already found and the algorithm can terminate.",
        "intro4", null, textProps);
    Text intro5 = lang
        .newText(
            new Offset(0, 10, intro4, AnimalScript.DIRECTION_SW),
            "If however there is at least one cycle, one of the chosen edges inside the cycle has to be replaced. Therefore the algorithm",
            "intro5", null, textProps);
    Text intro6 = lang
        .newText(
            new Offset(0, 0, intro5, AnimalScript.DIRECTION_SW),
            "will recurse on a modified graph. This graph consists off all nodes and edges outside the cycle and one additional pseudo",
            "intro6", null, textProps);
    Text intro7 = lang
        .newText(
            new Offset(0, 0, intro6, AnimalScript.DIRECTION_SW),
            "node representing all the nodes of the cycle. Edges with source inside the cycle and destination outside it are added aswell,",
            "intro7", null, textProps);
    Text intro8 = lang
        .newText(
            new Offset(0, 0, intro7, AnimalScript.DIRECTION_SW),
            "however if this would result in parallel edges only the one with mimimal weight is added. Edges with source outside the",
            "intro8", null, textProps);
    Text intro9 = lang
        .newText(
            new Offset(0, 0, intro8, AnimalScript.DIRECTION_SW),
            "cycle and destination inside it will be added in a similar way, although their weight will be slightly modified. As one of them",
            "intro9", null, textProps);
    Text intro10 = lang
        .newText(
            new Offset(0, 0, intro9, AnimalScript.DIRECTION_SW),
            "will essentially replace one of the chosen cycle edges the weight added in this process is only the difference between the",
            "intro10", null, textProps);
    Text intro11 = lang.newText(new Offset(0, 0, intro10,
        AnimalScript.DIRECTION_SW),
        "swapped edges and not the full weight of the new one.", "intro11",
        null, textProps);
    Text intro12 = lang
        .newText(
            new Offset(0, 10, intro11, AnimalScript.DIRECTION_SW),
            "This recursion and contraction of nodes will continue until no cycles are found anymore (As the number of nodes gets smaller",
            "intro12", null, textProps);
    Text intro13 = lang
        .newText(
            new Offset(0, 0, intro12, AnimalScript.DIRECTION_SW),
            "on every recursion step this will terminate). Every recursion step will then have found the edge that replaces one of the cycle",
            "intro13", null, textProps);
    Text intro14 = lang
        .newText(
            new Offset(0, 0, intro13, AnimalScript.DIRECTION_SW),
            "edges of the previous recursion step. On every step back in the recursion the pseudo nodes are expanded to their former state",
            "intro14", null, textProps);
    Text intro15 = lang
        .newText(
            new Offset(0, 0, intro14, AnimalScript.DIRECTION_SW),
            "and the new chosen edge replaces the specific edge inside the cycle, that enters the exact same node.",
            "intro15", null, textProps);
    Text intro16 = lang
        .newText(
            new Offset(0, 10, intro15, AnimalScript.DIRECTION_SW),
            "Once the recursion has reached its start an optimal spanning tree has been found.",
            "intro16", null, textProps);

    lang.nextStep("Introduction");

    // Hide the Introduction Text
    intro1.hide();
    intro2.hide();
    intro3.hide();
    intro4.hide();
    intro5.hide();
    intro6.hide();
    intro7.hide();
    intro8.hide();
    intro9.hide();
    intro10.hide();
    intro11.hide();
    intro12.hide();
    intro13.hide();
    intro14.hide();
    intro15.hide();
    intro16.hide();

    // Test for reachability
    ArrayList<JNode> reachableNodes = new ArrayList<JNode>();
    reachableNodes.add(root);
    boolean search = true;
    while (search) {
      search = false;
      for (JNode node : new ArrayList<JNode>(reachableNodes))
        for (JEdge edge : edges)
          if (edge.getSource().equals(node)
              && !reachableNodes.contains(edge.getDestination())) {
            search = true;
            reachableNodes.add(edge.getDestination());
          }
    }
    if (reachableNodes.size() < nodes.size()) {
      @SuppressWarnings("unused")
      Text error1 = lang
          .newText(
              new Offset(0, 40, title, AnimalScript.DIRECTION_SW),
              "Not all nodes are reachable from the specified root node. Please check your graph definition.",
              "error1", null, textProps);

      lang.nextStep("Graph Error");
      lang.finalizeGeneration();
      return lang.toString();
    }

    // source code entity
    sc = lang.newSourceCode(new Coordinates(maxRightCoord + 50, 80),
        "sourceCode", null, sourceCode);

    // Add the lines to the SourceCode object.
    sc.addCodeLine("public void EdmondsAlgorithm(Graph g, Root r) {", null, 0,
        null); // 0
    sc.addCodeLine("Remove edges entering Root Node r;", null, 1, null); // 1
    sc.addCodeLine("Graph result = recursion(g, r);", null, 1, null); // 2
    sc.addCodeLine("}", null, 0, null); // 3
    sc.addCodeLine("", null, 0, null); // 4
    sc.addCodeLine("public Graph recursion(Graph g, Root r) {", null, 0, null); // 5
    sc.addCodeLine("Mark one incoming edge with minimal weight for all", null,
        1, null); // 6
    sc.addCodeLine("nodes except r as Graph result;", null, 2, null); // 7
    sc.addCodeLine("if (result has at least one cycle) {", null, 1, null); // 8
    sc.addCodeLine("choose one cycle;", null, 2, null); // 9
    sc.addCodeLine("modify the weight of incoming edges;", null, 2, null); // 10
    sc.addCodeLine("contract the nodes to one pseudo node;", null, 2, null); // 11
    sc.addCodeLine("Graph recResult = recursion(contractedGraph, r);", null, 2,
        null); // 12
    sc.addCodeLine("Expand the contracted Node and restore old weights;", null,
        2, null); // 13
    sc.addCodeLine("Replace edges of result with newly chosen ones", null, 2,
        null); // 14
    sc.addCodeLine("of recResult;", null, 2, null); // 15
    sc.addCodeLine("}", null, 1, null); // 16
    sc.addCodeLine("return result;", null, 1, null); // 17
    sc.addCodeLine("}", null, 0, null); // 18

    // Set the global graph properties
    graphProps = new GraphProperties();
    graphProps.set("directed", true);
    graphProps.set("weighted", true);
    graphProps.set("color", Color.black);
    graphProps.set("fillColor", Color.white);
    graphProps.set("nodeColor", Color.black);
    graphProps.set("edgeColor", Color.black);
    graphProps.set("highlightColor", edgeHighlightColor);
    graphProps.set("depth", 5);
    graphProps2 = new GraphProperties();
    graphProps2.set("directed", true);
    graphProps2.set("weighted", true);
    graphProps2.set("color", Color.black);
    graphProps2.set("fillColor", Color.white);
    graphProps2.set("nodeColor", Color.black);
    graphProps2.set("edgeColor", Color.black);
    graphProps2.set("highlightColor", edgeHighlightColor2);
    graphProps2.set("depth", 5);
    Node[] graphNodes = new Node[g.getSize()];
    String[] labels = new String[g.getSize()];
    for (int i = 0; i < g.getSize(); i++) {
      graphNodes[i] = new Coordinates(graphCoords[i][0], graphCoords[i][1]);
      labels[i] = String.valueOf(i);
    }

    // Add the graph
    AGraph = lang.newGraph("agraph", adjacencyMatrix, graphNodes, labels, null,
        graphProps);

    variables.declare("Root", String.valueOf(root.getId()),
        VariableRoles.FIXED_VALUE);

    sc.highlight(0);
    lang.nextStep("Graph initialized");
    sc.unhighlight(0);

    // Initialize the algorithm
    initEdmond(jgraph);

    lang.nextStep("Minimum Spanning Tree");

    AGraph.hide();

    // Conclusion Text
    Text outro1 = lang
        .newText(
            new Offset(0, 40, title, AnimalScript.DIRECTION_SW),
            "Although it is called Edmonds Algorithm here, Chu and Liu have independently developed an identical version of the algorithm.",
            "outro1", null, textProps);
    Text outro2 = lang
        .newText(
            new Offset(0, 0, outro1, AnimalScript.DIRECTION_SW),
            "Therefore it is sometimes called Chu-Liu or Edmonds/Chu-Liu Algorithm.",
            "outro2", null, textProps);
    Text outro3 = lang
        .newText(
            new Offset(0, 10, outro2, AnimalScript.DIRECTION_SW),
            "As the number of considered nodes in the recursion is reduced by at least one on every recursion step the algorithm is",
            "outro3", null, textProps);
    Text outro4 = lang.newText(new Offset(0, 0, outro3,
        AnimalScript.DIRECTION_SW),
        "guaranteed to terminate eventually, no endless recursion can happen.",
        "outro4", null, textProps);
    Text outro5 = lang
        .newText(
            new Offset(0, 10, outro4, AnimalScript.DIRECTION_SW),
            "It is possible to find a maximum spanning tree aswell. To do so one has to choose the edges with highest weight instead of",
            "outro5", null, textProps);
    Text outro6 = lang.newText(new Offset(0, 0, outro5,
        AnimalScript.DIRECTION_SW),
        "lowest and the modified weight has to be changed accordingly.",
        "outro6", null, textProps);
    @SuppressWarnings("unused")
    Text outro7 = lang
        .newText(
            new Offset(0, 10, outro6, AnimalScript.DIRECTION_SW),
            "To find an optimal spanning tree on an undirected graph please have a look at Prims Algorithm.",
            "outro7", null, textProps);

    lang.nextStep("Conclusion");

    lang.finalizeGeneration();
    return lang.toString();
  }

  // //////// Algorithm start //////////

  /**
   * Class representing a node in a JGraph
   */
  public class JNode implements Comparable<JNode> {
    private int id;

    public JNode(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

    @Override
    public int compareTo(JNode node) {
      if (this.equals(node))
        return 0;
      else if (this.getId() < node.getId())
        return -1;
      else
        return 1;
    }

    public boolean equals(JNode node) {
      return (id == node.getId());
    }
  }

  /**
   * Class representing an edge in a JGraph
   */
  public class JEdge implements Comparable<JEdge> {
    private JNode source;
    private JNode destination;
    private JNode origSource;
    private JNode origDestination;
    private int   weight;

    public JEdge(JNode source, JNode destination, int weight) {
      this.source = source;
      this.destination = destination;
      origSource = source;
      origDestination = destination;
      this.weight = weight;
    }

    public JNode getSource() {
      return source;
    }

    public JNode getDestination() {
      return destination;
    }

    public JNode getOrigSource() {
      return origSource;
    }

    public void setOrigSource(JNode node) {
      origSource = node;
    }

    public JNode getOrigDestination() {
      return origDestination;
    }

    public void setOrigDestination(JNode node) {
      origDestination = node;
    }

    public int getWeight() {
      return weight;
    }

    @Override
    public int compareTo(JEdge edge) {
      if (source.compareTo(edge.getSource()) == 0)
        return destination.compareTo(edge.getDestination());
      else
        return source.compareTo(edge.getSource());
    }

    public boolean equals(JEdge edge) {
      return (source.equals(edge.getSource()) && destination.equals(edge
          .getDestination()));
    }
  }

  /**
   * Class representing a graph consisting of nodes and edges
   */
  public class JGraph {
    private ArrayList<JNode> nodes;
    private ArrayList<JEdge> edges;

    public JGraph() {
      this.nodes = new ArrayList<JNode>();
      this.edges = new ArrayList<JEdge>();
    }

    public JGraph(List<JNode> nodes, List<JEdge> edges) {
      this.nodes = new ArrayList<JNode>(nodes);
      this.edges = new ArrayList<JEdge>(edges);
    }

    public JEdge getEdge(JNode source, JNode destination) {
      for (JEdge e : edges) {
        if (e.getSource().equals(source)
            && e.getDestination().equals(destination))
          return e;
      }
      return null;
    }

    public void addNode(JNode node) {
      nodes.add(node);
    }

    public void removeNode(JNode node) {
      nodes.remove(node);
    }

    public void addEdge(JEdge edge) {
      edges.add(edge);
    }

    public void removeEdge(JEdge edge) {
      edges.remove(edge);
    }

    public ArrayList<JNode> getNodes() {
      return nodes;
    }

    public ArrayList<JEdge> getEdges() {
      return edges;
    }
  }

  private JNode root;
  private int   recursionDepth;

  public void initEdmond(JGraph graph) {
    recursionDepth = -1;

    // Remove edges entering the root node. As the root node does not change
    // during recursion, this only needs to be done once
    AGraph.highlightNode(root.getId(), null, new TicksTiming(250));
    AGraph.unhighlightNode(root.getId(), new TicksTiming(500), new TicksTiming(
        250));
    for (JEdge e : new ArrayList<JEdge>(graph.getEdges())) {
      if (e.getDestination().equals(root)) {
        graph.removeEdge(e);
        AGraph.highlightEdge(e.getSource().getId(), e.getDestination().getId(),
            null, new TicksTiming(250));
        AGraph.unhighlightEdge(e.getSource().getId(), e.getDestination()
            .getId(), new TicksTiming(500), new TicksTiming(250));
        AGraph.hideEdge(e.getSource().getId(), e.getDestination().getId(),
            new TicksTiming(750), new TicksTiming(250));
      }
    }

    sc.highlight(1);
    lang.nextStep();
    sc.unhighlight(1);

    sc.highlight(2);
    sc.highlight(5);

    AGraph.hide();

    JGraph result = doEdmond(graph);

    variables.discard("RecursionDepth");

    AGraph.show();

    for (JEdge e : result.getEdges()) {
      AGraph.highlightEdge(e.getSource().getId(), e.getDestination().getId(),
          null, null);
    }

    sc.highlight(2);
    sc.highlight(3);
    lang.nextStep();
    sc.unhighlight(2);
    sc.unhighlight(3);

    sc.hide();

    for (JEdge e : graph.getEdges()) {
      if (result.getEdges().contains(e))
        AGraph.unhighlightEdge(e.getSource().getId(), e.getDestination()
            .getId(), null, new TicksTiming(250));
      else
        AGraph.hideEdge(e.getSource().getId(), e.getDestination().getId(),
            null, null);
    }
  }

  public JGraph doEdmond(JGraph graph) {
    recursionDepth++;
    variables.declare("RecursionDepth", String.valueOf(recursionDepth));

    // Initialize the result branching with all nodes and no edges
    JGraph branching = new JGraph(graph.getNodes(), new ArrayList<JEdge>());

    // Choose a lowest weight edge for every node except the root as a
    // preliminary result branching
    int quizNode = -1;
    int quizNumEdges = 0;
    for (JNode n : graph.getNodes()) {
      if (n.equals(root))
        continue;
      // find an incoming Edge with lowest weight
      int minWeight = Integer.MAX_VALUE;
      JEdge minEdge = null;
      int quizTemp = 0;
      for (JEdge e : graph.getEdges()) {
        if (!e.getDestination().equals(n))
          continue;
        if (e.getWeight() < minWeight) {
          minWeight = e.getWeight();
          minEdge = e;
          quizTemp = 1;
        } else if (e.getWeight() == minWeight) {
          quizTemp++;
        }
      }

      // Animal: setup values for quiz question
      if (quizNode == -1 && quizTemp > 1) {
        quizNode = n.getId();
        quizNumEdges = quizTemp;
      }
      // Add the lowest weight Edge to the branching
      if (minEdge != null)
        branching.addEdge(minEdge);
    }

    // Animal: Build Graphs
    int[] animalId = new int[g.getSize()];
    Node[] graphNodes = new Node[graph.getNodes().size() + 1];
    String[] labels = new String[graph.getNodes().size() + 1];
    for (int i = 0; i < graph.getNodes().size(); i++) {
      graphNodes[i] = new Coordinates(graphCoords[graph.getNodes().get(i)
          .getId()][0], graphCoords[graph.getNodes().get(i).getId()][1]);
      labels[i] = String.valueOf(graph.getNodes().get(i).getId());
      animalId[graph.getNodes().get(i).getId()] = i;
    }
    graphNodes[graph.getNodes().size()] = new Coordinates(0, 0);
    labels[graph.getNodes().size()] = "NA";

    int[][] adjacencyMatrix = new int[graph.getNodes().size() + 1][graph
        .getNodes().size() + 1];
    for (int i = 0; i < graph.getNodes().size(); i++)
      for (int j = 0; j < graph.getNodes().size(); j++) {
        if (i == j)
          continue;
        if (graph.getEdge(graph.getNodes().get(i), graph.getNodes().get(j)) != null)
          adjacencyMatrix[i][j] = graph.getEdge(graph.getNodes().get(i),
              graph.getNodes().get(j)).getWeight();
      }

    Graph GraphA = lang.newGraph("grapha" + recursionDepth, adjacencyMatrix,
        graphNodes, labels, null, graphProps);
    GraphA.hideNode(graph.getNodes().size(), null, null);
    Graph GraphB = lang.newGraph("graphb" + recursionDepth, adjacencyMatrix,
        graphNodes, labels, null, graphProps2);
    for (int i = 0; i < graph.getNodes().size() + 1; i++) {
      GraphB.hideNode(i, null, null);
      for (int j = 0; j < graph.getNodes().size() + 1; j++) {
        GraphB.hideEdge(i, j, null, null);
      }
    }
    if (Quiz && !question1 && quizNode != -1)
      lang.nextStep("Recurse forward (" + String.valueOf(recursionDepth)
          + ") and Quiz next!");
    else
      lang.nextStep("Recurse forward (" + String.valueOf(recursionDepth) + ")");
    sc.unhighlight(2);
    sc.unhighlight(5);
    sc.unhighlight(12);

    // Animal: highlight chosen edges
    for (JEdge e : branching.getEdges()) {
      GraphA.highlightEdge(animalId[e.getSource().getId()], animalId[e
          .getDestination().getId()], null, new TicksTiming(250));
    }

    sc.highlight(6);
    sc.highlight(7);

    // Animal: Quiz
    if (Quiz && !question1 && quizNode != -1) {
      question1 = true;
      MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel(
          "question1");
      mcqm.setPrompt("On the previous step "
          + quizNumEdges
          + " edges with the same minimum weight were directed at node "
          + quizNode
          + ". Is a particular system used to determine the correct edge or can any of them be chosen randomly?");
      mcqm.setNumberOfTries(1);
      mcqm.addAnswer(
          "Particular System",
          0,
          "Incorrect Answer! Any of the edges with minimum added weight can be chosen with no particular preference. (Although this decision can affect the runtime of the algorithm)");
      mcqm.addAnswer(
          "Random choosing",
          1,
          "Correct Answer! Any of the edges with minimum added weight can be chosen with no particular preference. (Although this decision can affect the runtime of the algorithm)");
      mcqm.setGroupID("Question group");
      lang.addMCQuestion(mcqm);
    }

    lang.nextStep();
    sc.unhighlight(6);
    sc.unhighlight(7);

    // Animal: Highlight Cycles for a short time
    for (JGraph j : getCycles(branching))
      for (JEdge e : j.getEdges()) {
        GraphA.unhighlightEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], null, new TicksTiming(250));
        GraphA.hideEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], new TicksTiming(200), null);
        GraphB.showEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], new TicksTiming(200), null);
        GraphB.highlightEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], new TicksTiming(150), new TicksTiming(
            250));

        GraphB.unhighlightEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], new TicksTiming(800), new TicksTiming(
            250));
        GraphB.hideEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], new TicksTiming(1000), null);
        GraphA.showEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], new TicksTiming(1000), null);
        GraphA.highlightEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], new TicksTiming(950), new TicksTiming(
            250));
      }

    JGraph cycle = getCycle(branching);

    sc.highlight(8);
    if (cycle == null)
      lang.nextStep("No Cycles: Recursion Anchor");
    else
      lang.nextStep();
    sc.unhighlight(8);

    // Check branching for cycles. If none are found the branching is a
    // shortest route tree. Otherwise contract the cycle into one node and
    // recurse further
    if (cycle != null) {
      // Animal: Unhighlight the edges of other cycles
      for (JEdge e : branching.getEdges()) {
        if (!cycle.getEdges().contains(e))
          GraphA.unhighlightEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], null, new TicksTiming(250));
      }

      sc.highlight(9);
      if (Quiz && !question2)
        lang.nextStep("Quiz next!");
      else
        lang.nextStep();
      sc.unhighlight(9);

      ArrayList<int[]> oldWeights = new ArrayList<int[]>();

      // Create the cycle contracted Graph for the recursion
      JGraph contractedGraph = new JGraph();
      // Add each node that is not inside the cycle to the contracted
      // graph
      for (JNode n : graph.getNodes())
        if (!cycle.getNodes().contains(n))
          contractedGraph.addNode(n);
      // Add a new pseudo node representing all nodes of the cycle to the
      // contracted graph
      contractedGraph.addNode(cycle.getNodes().get(0));

      // Add edges to the contracted Graph
      for (JEdge e : graph.getEdges()) {
        // Edge outside cycle
        if (!cycle.getNodes().contains(e.getSource())
            && !cycle.getNodes().contains(e.getDestination()))
          contractedGraph.addEdge(e);
        // Edge entering cycle --> modify cost
        else if (!cycle.getNodes().contains(e.getSource())
            && cycle.getNodes().contains(e.getDestination())) {
          int modWeight = e.getWeight()
              - getIncomingEdge(cycle, e.getDestination()).getWeight();

          // Animal: change weight and save old value
          GraphA
              .setEdgeWeight(animalId[e.getSource().getId()], animalId[e
                  .getDestination().getId()], modWeight, new TicksTiming(500),
                  null);
          GraphB
              .setEdgeWeight(animalId[e.getSource().getId()], animalId[e
                  .getDestination().getId()], modWeight, new TicksTiming(500),
                  null);

          GraphA.hideEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], null, null);
          GraphB.showEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], null, null);
          GraphB.highlightEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], null, new TicksTiming(250));
          GraphB.unhighlightEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], new TicksTiming(750),
              new TicksTiming(250));
          GraphB.hideEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], new TicksTiming(1000), null);
          GraphA.showEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], new TicksTiming(1000), null);

          oldWeights.add(new int[] { animalId[e.getSource().getId()],
              animalId[e.getDestination().getId()], e.getWeight() });

          JEdge edge = new JEdge(e.getSource(), cycle.getNodes().get(0),
              modWeight);
          edge.setOrigDestination(e.getDestination());
          if (contractedGraph.getEdges().contains(edge)) {
            if (edge.getWeight() < contractedGraph.getEdge(edge.getSource(),
                edge.getDestination()).getWeight()) {
              contractedGraph.removeEdge(edge);
              contractedGraph.addEdge(edge);
            }
          } else
            contractedGraph.addEdge(edge);
        }
        // Edge exiting cycle
        else if (cycle.getNodes().contains(e.getSource())
            && !cycle.getNodes().contains(e.getDestination())) {
          JEdge edge = new JEdge(cycle.getNodes().get(0), e.getDestination(),
              e.getWeight());
          edge.setOrigSource(e.getSource());
          if (contractedGraph.getEdges().contains(edge)) {
            if (edge.getWeight() < contractedGraph.getEdge(edge.getSource(),
                edge.getDestination()).getWeight()) {
              contractedGraph.removeEdge(edge);
              contractedGraph.addEdge(edge);
            }
          } else
            contractedGraph.addEdge(edge);
        }
      }

      sc.highlight(10);

      // Animal: Quiz
      if (Quiz && !question2) {
        question2 = true;
        MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel(
            "question2");
        mcqm.setPrompt("On the previous step the weight of all cycle entering edges was changed to reflect the added weight if the respective edge would replace one of the cycle edges. What would be an appropriate equation for the added weight?");
        mcqm.setNumberOfTries(1);
        mcqm.addAnswer(
            "Added Weight = Weight(old Edge)",
            0,
            "Incorrect Answer! The new edge is not just added to the graph but the old one is removed aswell. Therefore the weight of the old edge has to be substracted from the weight of the new one.");
        mcqm.addAnswer(
            "Added Weight = Weight(new Edge)",
            0,
            "Incorrect Answer! The new edge is not just added to the graph but the old one is removed aswell. Therefore the weight of the old edge has to be substracted from the weight of the new one.");
        mcqm.addAnswer(
            "Added Weight = Weight(old Edge) - Weight(new Edge)",
            0,
            "Incorrect Answer! The new edge is not just added to the graph but the old one is removed aswell. Therefore the weight of the old edge has to be substracted from the weight of the new one.");
        mcqm.addAnswer(
            "Added Weight = Weight(new Edge) - Weight(old Edge)",
            1,
            "Correct Answer! The new edge is not just added to the graph but the old one is removed aswell. Therefore the weight of the old edge has to be substracted from the weight of the new one.");
        mcqm.addAnswer(
            "Added Weight = Weight(old Edge) + Weight(new Edge)",
            0,
            "Incorrect Answer! The new edge is not just added to the graph but the old one is removed aswell. Therefore the weight of the old edge has to be substracted from the weight of the new one.");
        mcqm.setGroupID("Question group");
        lang.addMCQuestion(mcqm);
      }

      lang.nextStep();
      sc.unhighlight(10);

      // Animal: Contract Nodes
      ArrayList<int[]> returnOffsets = new ArrayList<int[]>();
      Coordinates destCoords = (Coordinates) GraphA
          .getNodeForIndex(animalId[cycle.getNodes().get(0).getId()]);
      int destX = destCoords.getX();
      int destY = destCoords.getY();
      for (JNode n : cycle.getNodes()) {
        if (n.equals(cycle.getNodes().get(0)))
          continue;
        Coordinates srcCoords = (Coordinates) GraphA.getNodeForIndex(animalId[n
            .getId()]);
        int srcX = srcCoords.getX();
        int srcY = srcCoords.getY();
        returnOffsets.add(new int[] { animalId[n.getId()], srcX - destX,
            srcY - destY });
        Offset offset = new Offset(destX - srcX, destY - srcY, "grapha"
            + recursionDepth, null);
        GraphA.translateNode(animalId[n.getId()] + 1, offset, null,
            new TicksTiming(250));
        GraphB.translateNode(animalId[n.getId()] + 1, offset, null,
            new TicksTiming(250));
      }

      // Animal: hide cycle edges
      for (JEdge e : cycle.getEdges()) {
        GraphA.hideEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], new TicksTiming(250), null);
      }

      sc.highlight(11);
      lang.nextStep();
      sc.unhighlight(11);

      sc.highlight(12);
      sc.highlight(5);

      GraphA.hide();
      GraphB.hide();

      // Recursion will provide replacement edges for edges in the
      // branching and the cycle
      JGraph recResult = doEdmond(contractedGraph);

      recursionDepth--;
      variables.declare("RecursionDepth", String.valueOf(recursionDepth));

      GraphA.show();
      GraphB.show();

      // Animal: Restore old edge weights
      for (int[] oldWeight : oldWeights) {
        GraphA.setEdgeWeight(oldWeight[0], oldWeight[1], oldWeight[2], null,
            null);
        GraphB.setEdgeWeight(oldWeight[0], oldWeight[1], oldWeight[2], null,
            null);
      }

      // Animal: Expand Pseudo-Node
      for (int[] returnOffset : returnOffsets) {

        Offset offset = new Offset(returnOffset[1], returnOffset[2], "grapha"
            + recursionDepth, null);
        GraphA.translateNode(returnOffset[0] + 1, offset, null,
            new TicksTiming(250));
        GraphB.translateNode(returnOffset[0] + 1, offset, null,
            new TicksTiming(250));
      }

      // Animal: show cycle Edges
      for (JEdge e : cycle.getEdges()) {
        GraphB.showEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], null, null);
        GraphB.highlightEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], null, null);
        GraphA.unhighlightEdge(animalId[e.getSource().getId()], animalId[e
            .getDestination().getId()], null, null);
      }

      // Replace edges in the branching with the new chosen ones
      for (JEdge e : recResult.getEdges()) {
        if (e == null)
          continue;

        JEdge newEdge = graph
            .getEdge(e.getOrigSource(), e.getOrigDestination());
        JEdge oldEdge = getIncomingEdge(branching, e.getOrigDestination());
        branching.removeEdge(oldEdge);
        branching.addEdge(newEdge);
      }

      // Animal: still highlight new chosen edges
      for (JEdge e : branching.getEdges())
        if (e == null)
          continue;
        else if (!cycle.getEdges().contains(e))
          GraphA.highlightEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], null, null);

      sc.highlight(13);
      lang.nextStep("Recurse backward (" + String.valueOf(recursionDepth) + ")");
      sc.unhighlight(13);

      // Animal: Replace Edges
      for (JEdge e : cycle.getEdges()) {
        // cycle edges that are still chosen
        if (branching.getEdges().contains(e)) {
          GraphB.unhighlightEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], null, new TicksTiming(250));
          GraphB.hideEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], new TicksTiming(200), null);
          GraphA.showEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], new TicksTiming(200), null);
          GraphA.highlightEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], new TicksTiming(150),
              new TicksTiming(250));
        }
        // cycle edges that are not chosen anymore
        else {
          GraphB.unhighlightEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], null, new TicksTiming(250));
          GraphB.hideEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], new TicksTiming(250), null);
          GraphA.showEdge(animalId[e.getSource().getId()], animalId[e
              .getDestination().getId()], new TicksTiming(250), null);
        }
      }

      sc.highlight(14);
      sc.highlight(15);
      sc.highlight(16);
      lang.nextStep();
      sc.unhighlight(14);
      sc.unhighlight(15);
      sc.unhighlight(16);
    }

    GraphA.hide();
    GraphB.hide();

    // Animal: Quiz
    if (Quiz && recursionDepth == 1) {
      MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel(
          "question3");
      mcqm.setPrompt("Is it possible to utilize the algorithm (barring trivial change) to search for a maximum spanning tree too?");
      mcqm.setNumberOfTries(1);
      mcqm.addAnswer(
          "Yes",
          1,
          "Correct Answer! If the edges with maximum (added) weight are chosen instead of the ones with minimum (added) weight the result will produce a maximum spanning tree.");
      mcqm.addAnswer(
          "No",
          0,
          "Incorrect Answer! If the edges with maximum (added) weight are chosen instead of the ones with minimum (added) weight the result will produce a maximum spanning tree.");
      mcqm.setGroupID("Question group");
      lang.addMCQuestion(mcqm);
    }

    return branching;
  }

  /**
   * Returns a true cycle in the specified graph or null if none exist
   * 
   * @param graph
   *          The specified JGraph
   * @return A JGraph defining a Cycle
   */
  public JGraph getCycle(JGraph graph) {

    for (JNode start : graph.getNodes()) {
      ArrayList<JNode> visitedNodes = new ArrayList<JNode>();
      ArrayList<JEdge> traversedEdges = new ArrayList<JEdge>();
      JNode tempNode = start;
      JEdge tempEdge;
      visitedNodes.add(start);

      while (null != (tempEdge = getIncomingEdge(graph, tempNode))) {
        tempNode = tempEdge.getSource();
        traversedEdges.add(tempEdge);

        // True Cycle found
        if (start.equals(tempNode))
          return new JGraph(visitedNodes, traversedEdges);

        // Cycle found, but start point not part of it
        if (visitedNodes.contains(tempNode))
          break;

        visitedNodes.add(tempNode);
      }
    }

    // No cycle found
    return null;
  }

  /**
   * Returns all true cycle in the specified graph
   * 
   * @param graph
   *          The specified JGraph
   * @return An ArrayList<JGraph> defining all Cycle
   */
  public ArrayList<JGraph> getCycles(JGraph graph) {
    ArrayList<JGraph> result = new ArrayList<JGraph>();

    for (JNode start : graph.getNodes()) {
      ArrayList<JNode> visitedNodes = new ArrayList<JNode>();
      ArrayList<JEdge> traversedEdges = new ArrayList<JEdge>();
      JNode tempNode = start;
      JEdge tempEdge;
      visitedNodes.add(start);

      while (null != (tempEdge = getIncomingEdge(graph, tempNode))) {
        tempNode = tempEdge.getSource();
        traversedEdges.add(tempEdge);

        // True Cycle found
        if (start.equals(tempNode)) {
          result.add(new JGraph(visitedNodes, traversedEdges));
          break;
        }

        // Cycle found, but start point not part of it
        if (visitedNodes.contains(tempNode))
          break;

        visitedNodes.add(tempNode);
      }
    }

    return result;
  }

  /**
   * Returns a JEdge entering the given node in the given graph or null if there
   * are no JEdges with node as destination
   * 
   * @param graph
   *          The specified JGraph
   * @param node
   *          The specified destination JNode
   * @return A JEdge with node as destination or null if not applicable
   */
  public JEdge getIncomingEdge(JGraph graph, JNode node) {
    for (JEdge e : graph.getEdges()) {
      if (e == null)
        return null;
      // A JEdge entering node found
      if (e.getDestination().equals(node))
        return e;
    }

    // No JEdges entering node available
    return null;
  }

  // ///////// Algorithm end ///////////

  public String getName() {
    return "Edmonds Algorithm [EN]";
  }

  public String getAlgorithmName() {
    return "Edmonds Algorithm";
  }

  public String getAnimationAuthor() {
    return "Jan Wiesel";
  }

  public String getDescription() {
    return "Edmonds Algorithm (also known as Chu-Liu Algorithm) calculates a mimimum spanning tree for directed weighted graphs."
        + "<BR>"
        + "For calculating a similar tree on undirected graphs please have a look at Prims Algorithm."
        + "<BR> <BR>"
        + "Please make sure, that every node of the graph is reachable from the specified root node!";
  }

  public String getCodeExample() {
    return

    "public void EdmondsAlgorithm(Graph g, Root r) {" + "\n"
        + "   Remove edges entering Root Node r;" + "\n"
        + "   Graph result = recursion(g, r);" + "\n" + "}" + "\n" + "\n"
        + "public Graph recursion(Graph g, Root r) {" + "\n"
        + "   Mark one incoming edge with minimal weight for all" + "\n"
        + "      nodes except r as Graph result;" + "\n"
        + "   if (result has at least one cycle) {" + "\n"
        + "      choose one cycle;" + "\n"
        + "      modify the weight of incoming edges;" + "\n"
        + "      contract the nodes to one pseudo node;" + "\n"
        + "      Graph recResult = recursion(contractedGraph, r);" + "\n"
        + "      Expand the contracted Node and restore old weights;" + "\n"
        + "      Replace edges of result with newly chosen ones" + "\n"
        + "         of recResult;" + "\n" + "   }" + "\n" + "return result;"
        + "\n" + "}";
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

}
