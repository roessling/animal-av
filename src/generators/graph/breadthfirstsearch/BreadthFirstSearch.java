package generators.graph.breadthfirstsearch;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
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

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class BreadthFirstSearch implements Generator {
  /**
   * The concrete language object used for creating output
   */
  private Language             lang;

  /**
   * The Node Labels
   */
  private String[]             labels;

  /**
   * The adjacency matrix
   */
  private int[][]              adjacencyMatrix;

  /**
   * The start node
   */
  private String               startNode;

  /**
   * The target node
   */
  private String               targetNode;

  /**
   * The positions of nodes
   */
  private int[][]              positions;

  /**
   * Globally defined text properties
   */
  private TextProperties       textProps;

  /**
   * Globally defined source code properties
   */
  private SourceCodeProperties sourceCodeProps;

  /**
   * Globally defined rect properties
   */
  private RectProperties       rectProps;

  /**
   * Globally defined array properties
   */
  private ArrayProperties      arrayProperties;

  public BreadthFirstSearch() {
    init();
  }

  public void init() {
    lang = new AnimalScript("Breadth First Search (BFS)",
        "Thu Huong Luu, Benedikt Hiemenz", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    labels = (String[]) primitives.get("labels");
    adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
    startNode = (String) primitives.get("startNode");
    targetNode = (String) primitives.get("targetNode");
    positions = (int[][]) primitives.get("positions");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    rectProps = (RectProperties) props.getPropertiesByName("rectProperties");
    arrayProperties = (ArrayProperties) props
        .getPropertiesByName("arrayProperties");

    Node[] graphNodes = new Node[labels.length];
    for (int i = 0; i < labels.length; i++) {
      graphNodes[i] = new Coordinates(positions[i][0] + 500,
          positions[i][1] + 150);
    }

    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.YELLOW);
    graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.YELLOW);

    Graph g = lang.newGraph("graph", adjacencyMatrix, graphNodes, labels, null,
        graphProps);
    g.setStartNode(g.getNode(labelToInt(labels, startNode)));
    g.setTargetNode(g.getNode(labelToInt(labels, targetNode)));

    start(g);

    lang.finalizeGeneration();
    return lang.toString();
  }

  /**
   * Initializes the animation. Shows a start page with a description. Then,
   * shows the graph and the source code and calls the BFS algorithm.
   * 
   * @param graph
   *          the graph
   */
  public void start(Graph graph) {
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    graph.hide();

    // show the header with a heading surrounded by a rectangle
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30), "Breitensuche",
        "header", null, headerProps);

    Rect hRect = lang.newRect(new Offset(-5, -5, "header",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
        null, rectProps);

    // setup the start page with the description
    lang.nextStep();
    header.show();
    hRect.show();

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 14));
    lang.newText(
        new Coordinates(10, 100),
        "Die Breitensuche  wird genutzt um einen bestimmten Knoten innerhalb eines Graphen zu ",
        "description1", null, textProps);
    lang.newText(
        new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
        "finden. Dabei wird zuerst jedes Level abgesucht, bevor man tiefer geht. ",
        "description2", null, textProps);

    lang.nextStep();
    lang.newText(new Offset(0, 50, "description2", AnimalScript.DIRECTION_NW),
        "Der grobe Ablauf ist wie folgt:", "description3", null, textProps);

    lang.nextStep();
    lang.newText(
        new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
        "1. Beginne beim Startknoten, speichere ihn in einer Warteschlange ab und markiere ihn als gesehen.",
        "description4", null, textProps);

    lang.nextStep();
    lang.newText(
        new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
        "2. Solange die Warteschlange nicht leer ist, entnimm einen Knoten vom Beginn.",
        "description5", null, textProps);
    lang.newText(
        new Offset(40, 25, "description5", AnimalScript.DIRECTION_NW),
        "- Falls dieser Knoten dem gesuchten Element entspricht, brich die Suche ab und liefere 'gefunden' zurueck.",
        "description6", null, textProps);
    lang.newText(
        new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
        "- Anderenfalls haenge alle bisher unmarkierten Nachfolger dieses Knotens, die sich noch nicht ",
        "description7", null, textProps);
    lang.newText(
        new Offset(10, 25, "description7", AnimalScript.DIRECTION_NW),
        "in der Warteschlange befinden, ans Ende der Warteschlange an und markiere sie als gesehen.",
        "description8", null, textProps);

    lang.nextStep();
    lang.newText(
        new Offset(-50, 25, "description8", AnimalScript.DIRECTION_NW),
        "3. Wenn die Warteschlange leer ist, dann wurde jeder Knoten innerhalb des Graphen bereits untersucht.",
        "description9", null, textProps);
    lang.newText(new Offset(15, 25, "description9", AnimalScript.DIRECTION_NW),
        "Beende die Suche und liefere 'nicht gefunden' zurueck.",
        "description10", null, textProps);

    // hide the description and show the graph and the code
    lang.nextStep("StartSeite");

    // this creates a blank page
    lang.hideAllPrimitives();
    header.show();
    hRect.show();

    SourceCode src = lang.newSourceCode(new Coordinates(10, 100), "sourceCode",
        null, sourceCodeProps);
    src.addCodeLine("BFS(start_node, goal_node) {", null, 0, null); // 0
    src.addCodeLine(
        "for(all nodes i) visited[i] = false;      // anfangs sind keine Knoten besucht",
        null, 1, null); // 1
    src.addCodeLine("", null, 0, null);
    src.addCodeLine(
        "queue.push(start_node);               // mit Start-Knoten beginnen",
        null, 1, null); // 3
    src.addCodeLine("visited[start_node] = true;", null, 1, null); // 4
    src.addCodeLine("", null, 0, null);
    src.addCodeLine(
        "while(! queue.empty() ) {              // solange queue nicht leer ist ...",
        null, 1, null); // 6
    src.addCodeLine(
        "node = queue.pop();                 // ... erstes Element von der queue nehmen",
        null, 2, null); // 7
    src.addCodeLine(
        "if(node == goal_node) {            // testen, ob Ziel-Knoten gefunden",
        null, 2, null); // 8
    src.addCodeLine("return true;", null, 3, null); // 9
    src.addCodeLine("}", null, 2, null); // 10
    src.addCodeLine("", null, 0, null);
    src.addCodeLine(
        "foreach(child of neighbours(node)) {      // alle Nachfolge-Knoten, ...",
        null, 2, null); // 12
    src.addCodeLine(
        "if(visited[child] == false) {         // ... die noch nicht besucht wurden ...",
        null, 3, null); // 13
    src.addCodeLine(
        "queue.push(child);                // ... zur queue hinzufuegen...",
        null, 4, null); // 14
    src.addCodeLine(
        "visited[child] = true;              // ... und als bereits gesehen markieren",
        null, 4, null); // 15
    src.addCodeLine("}", null, 3, null); // 16
    src.addCodeLine("}", null, 2, null); // 17
    src.addCodeLine("}", null, 1, null); // 18
    src.addCodeLine(
        "return false;                            // Knoten kann nicht erreicht werden",
        null, 1, null); // 19
    src.addCodeLine("}", null, 0, null); // 20

    lang.nextStep("Quellcode");

    graph.show();

    // ~~~ the Visited Array ~~~
    String[] v = new String[graph.getSize()];
    for (int i = 0; i < graph.getSize(); i++) {
      v[i] = graph.getNodeLabel(i);
    }
    StringArray visited = lang.newStringArray(new Offset(10, 380, "sourceCode",
        AnimalScript.DIRECTION_NW), v, "visited", null, arrayProperties);

    lang.newText(new Offset(-10, -16, "visited", AnimalScript.DIRECTION_NW),
        "Bereits besucht:", "visitedHeader", null, textProps);

    // ~~~ the Queue ~~~
    String[] q = new String[graph.getSize()];
    for (int i = 0; i < q.length; i++)
      q[i] = "  ";
    StringArray queue = lang.newStringArray(new Offset(0, 50, "visited",
        AnimalScript.DIRECTION_NW), q, "queue", null, arrayProperties);

    lang.newText(new Offset(-10, -16, "queue", AnimalScript.DIRECTION_NW),
        "Zu besuchen:", "queueHeader", null, textProps);

    SourceCode legende = lang.newSourceCode(new Offset(0, 40, "queueHeader",
        AnimalScript.DIRECTION_NW), "legende", null, sourceCodeProps);
    legende.addCodeLine("Legende:", null, 0, null);
    legende.addCodeLine("Bereits besuchte Knoten: Gelb", null, 1, null);
    legende.addCodeLine("Aktuell betrachteter Knoten: Cyan", null, 1, null);
    legende.addCodeLine("Zielknoten: Gruen", null, 1, null);
    lang.nextStep("Start des Algorithmus");

    // call the BFS algorithm
    bfs(graph, queue, visited, src);

    lang.newText(
        new Offset(250, -10, "legende", AnimalScript.DIRECTION_NW),
        "Sei der Graph mit G = (V,E) gegeben, wobei V die Knotenmenge, E die Kantenmenge, so benoetigt die Breitensuche O(|V| + |E|) = O(n).",
        "complexity1", null, textProps);
    lang.newText(new Offset(0, 15, "complexity1", AnimalScript.DIRECTION_NW),
        "Erklaerung:", "complexity2", null, textProps);
    lang.newText(
        new Offset(0, 15, "complexity2", AnimalScript.DIRECTION_NW),
        "Das liegt daran, dass jeder Knoten genau einmal in Q eingefuegt [ Laufzeit: O(|V|) ] und",
        "complexity3", null, textProps);
    lang.newText(
        new Offset(0, 15, "complexity3", AnimalScript.DIRECTION_NW),
        "jede Kante {u, v} einmal bei den Nachbarn von u und einmal bei den Nachbarn von v betrachtet wird [ Laufzeit: O(|E|) ].",
        "complexity4", null, textProps);

    lang.newRect(
        new Offset(-10, -30, "complexity1", AnimalScript.DIRECTION_NW),
        new Offset(110, 5, "complexity4", "SE"), "rRect", null, rectProps);

    lang.nextStep("Ergebnisanzeige");
  }

  private void bfs(Graph g, StringArray queue, StringArray visited,
      SourceCode src) {
    /**
     * number of queue elements
     */
    int queueElements = 0;

    /**
     * List with visited paths
     */
    ArrayList<Node[]> visitedPaths = new ArrayList<Node[]>();

    /**
     * Graph to highlight the observed node and the path at the end
     */
    Graph clone = clone(g);

    for (int i = 0; i < clone.getSize(); i++) { // hide all elements
      if (!clone.getNodeLabel(i).equals(
          clone.getNodeLabel(clone.getTargetNode())))
        clone.hideNode(i, null, null);
      else
        clone.highlightNode(i, null, null); // target node is marked
      for (Node node : getNeighbours(clone, clone.getNode(i))) {
        clone.hideEdge(clone.getNode(i), node, null, null);
      }
    }

    /**
     * Array to mark whether nodes where visited or not
     */
    boolean[] visitedBool = new boolean[g.getSize()];

    for (int i = 0; i < visitedBool.length; i++)
      // set all to false
      visitedBool[i] = false;

    src.highlight(1);
    lang.nextStep();

    pushQueue(queue, g.getNodeLabel(g.getStartNode()), queueElements); // put
                                                                       // start-node
                                                                       // in
                                                                       // queue
    ++queueElements;
    visitedBool[g.getPositionForNode(g.getStartNode())] = true;
    src.unhighlight(1);
    src.highlight(3);
    lang.nextStep();

    visited.highlightCell(g.getPositionForNode(g.getStartNode()), null, null); // start-node
                                                                               // is
                                                                               // marked
                                                                               // visited
    g.highlightNode(g.getStartNode(), null, null);

    src.highlight(4);
    lang.nextStep();

    src.unhighlight(3);
    src.unhighlight(4);
    src.highlight(6);
    clone.hideNode(g.getPositionForNode(g.getStartNode()), null, null);
    lang.nextStep("Initializierung");

    while (!queue.getData(0).equals("  ")) { // while queue is not empty

      MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
          "multipleChoiceQuestion" + UUID.randomUUID());
      mcq.setPrompt("Welcher Knoten wird als naechstes bearbeitet?");
      for (int i = 0; i < labels.length; i++) {
        if (queue.getData(0).equals(labels[i]))
          mcq.addAnswer(labels[i], 1, "Genau! =) " + labels[i]
              + " ist das erste Elemnt in der Queue!");
        else
          mcq.addAnswer(labels[i], 0,
              "Leider nein! =( Die richtige Antwort lautet " + queue.getData(0)
                  + " da dies das erste Element in der Queue ist.");
      }
      mcq.setGroupID("First question group");
      lang.addMCQuestion(mcq);
      lang.nextStep();

      String label = popQueue(queue); // pop first element
      --queueElements;
      int index = labelToInt(visited, label);
      Node node = g.getNodeForIndex(index);
      clone.showNode(index, null, null);
      clone.unhighlightNode(index, null, null);
      src.highlight(7);
      lang.nextStep("Betrachteter Knoten: " + label);

      src.unhighlight(7);
      src.highlight(8);
      lang.nextStep();

      if (node.equals(g.getTargetNode())) { // test whether observed node
        // is target node
        src.highlight(9);
        markPath(clone, visitedPaths);
        lang.newText(new Offset(240, 0, "queue", AnimalScript.DIRECTION_NW),
            "Es wurde ein Pfad zum Knoten gefunden! =)", "nodeFound", null,
            textProps);
        return;
      }
      src.unhighlight(8);
      src.highlight(12);
      lang.nextStep();

      for (Node child : getNeighbours(g, node)) { // for all neighbors
        src.highlight(13);
        lang.nextStep();

        if (visitedBool[g.getPositionForNode(child)] == false) { // which were
                                                                 // not visited
                                                                 // before
          g.highlightEdge(node, child, null, null);
          visitedPaths.add(new Node[] { node, child });

          pushQueue(queue, g.getNodeLabel(child), queueElements); // push on
                                                                  // queue
          ++queueElements;
          visitedBool[g.getPositionForNode(child)] = true;
          src.highlight(14);
          lang.nextStep();

          visited.highlightCell(g.getPositionForNode(child), null, null); // mark
                                                                          // as
                                                                          // visited
          g.highlightNode(child, null, null);
          src.unhighlight(14);
          src.highlight(15);
          lang.nextStep();

          src.unhighlight(15);
        }
        src.unhighlight(13);
        lang.nextStep();
      }
      src.unhighlight(12);
      clone.hideNode(index, null, null);
      lang.nextStep();
    }
    src.unhighlight(6);
    src.highlight(19);
    lang.newText(new Offset(240, 0, "queue", AnimalScript.DIRECTION_NW),
        "Es wurde kein Pfad zum Knoten gefunden! =(", "nodeNotFound", null,
        textProps);
  }

  /**
   * pop first element of queue
   * 
   * @param queue
   *          the queue
   * @return the first element
   */
  private String popQueue(StringArray queue) {
    String first = queue.getData(0);
    for (int i = 0; i < queue.getLength() - 1; i++) {
      queue.put(i, queue.getData(i + 1), null, null);
    }
    queue.put(queue.getLength() - 1, "  ", null, null);
    return first;
  }

  /**
   * push element at the given position in the queue
   * 
   * @param queue
   *          the queue
   * @param node
   *          the new node
   * @param index
   *          the position to put node
   */
  private void pushQueue(StringArray queue, String node, int index) {
    queue.put(index, node, null, null);
  }

  /**
   * returns all neighbours of node
   * 
   * @param g
   *          the graph
   * @param node
   *          the node
   * @return list of neighbours
   */
  private ArrayList<Node> getNeighbours(Graph g, Node node) {
    ArrayList<Node> succ = new ArrayList<Node>();
    int[] edges = g.getEdgesForNode(node);

    for (int i = 0; i < edges.length; i++) {
      if (edges[i] == 1) {
        succ.add(g.getNode(i));
      }
    }
    return succ;
  }

  /**
   * returns the index of the node with given label
   * 
   * @param q
   *          list with all labels
   * @param l
   *          the label
   * @return the index
   */
  private int labelToInt(StringArray q, String l) {
    for (int i = 0; i < q.getLength(); i++) {
      String label = q.getData(i);
      if (label.equals(l))
        return i;
    }
    return -1;
  }

  /**
   * returns the index of the node with given label
   * 
   * @param labels
   *          list with all labels
   * @param l
   *          the label
   * @return the index
   */
  private int labelToInt(String[] labels, String l) {
    for (int i = 0; i < labels.length; i++) {
      if (labels[i].equals(l))
        return i;
    }
    return -1;
  }

  /**
   * returns a clone of given graph
   * 
   * @param g
   *          the graph
   * @return the clone
   */
  private Graph clone(Graph g) {
    int[][] graphAdjacencyMatrix = g.getAdjacencyMatrix();

    String[] labels = new String[g.getSize()];
    Node[] graphNodes = new Node[g.getSize()];

    for (int i = 0; i < g.getSize(); i++) {
      labels[i] = g.getNodeLabel(i);
      graphNodes[i] = g.getNode(i);
    }

    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
    graphProps
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
    graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.CYAN);
    graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);

    Graph clone = lang.newGraph("clone", graphAdjacencyMatrix, graphNodes,
        labels, null, graphProps);
    clone.setStartNode(g.getStartNode());
    clone.setTargetNode(g.getTargetNode());
    return clone;
  }

  /**
   * mark path between start and target node (backwards);
   * 
   * @param g
   *          the graph
   * @param paths
   *          visited paths
   */
  private void markPath(Graph g, ArrayList<Node[]> paths) {
    Node search = g.getTargetNode();
    g.showNode(g.getTargetNode(), null, null);
    g.highlightNode(g.getTargetNode(), null, null);
    while (!search.equals(g.getStartNode())) {
      for (Node[] path : paths) {
        if (path[1].equals(search)) {
          g.showEdge(path[0], path[1], null, null);
          g.highlightEdge(path[0], path[1], null, null);
          g.showNode(path[0], null, null);
          g.highlightNode(path[0], null, null);
          search = path[0];
          break;
        }
      }
    }
  }

  public String getName() {
    return "Breitensuche (BFS)";
  }

  public String getAlgorithmName() {
    return "Breitensuche";
  }

  public String getAnimationAuthor() {
    return "Thu Huong Luu, Benedikt Hiemenz";
  }

  public String getDescription() {
    return "Die Breitensuche  wird genutzt um einen bestimmten Knoten innerhalb eines Graphen zu <br>"
        + "finden. Dabei wird zuerst jedes Level abgesucht, bevor man tiefer geht. <br>"
        + "<br>"
        + "Der grobe Ablauf ist wie folgt: <br>"
        + "1. Beginne beim Startknoten, speichere ihn in einer Warteschlange ab und markiere ihn als gesehen.<br>"
        + "2. Solange die Warteschlange nicht leer ist, entnimm einen Knoten vom Beginn.<br>"
        + "&nbsp &nbsp &nbsp &nbsp     - Falls das gesuchte Element gefunden wurde, brich die Suche ab und liefere \"gefunden\" zur&Xumlck.<br>"
        + "&nbsp &nbsp &nbsp &nbsp     - Anderenfalls h&Xamlnge alle bisher unmarkierten Nachfolger dieses Knotens, die sich noch nicht <br>"
        + "       in der Warteschlange befinden, ans Ende der Warteschlange an und markiere sie als gesehen.<br>"
        + "3. Wenn die Warteschlange leer ist, dann wurde jeder Knoten innerhalb des Graphen bereits untersucht.<br>"
        + "     Beende die Suche und liefere \"nicht gefunden\" zur&Xumlck.";
  }

  public String getCodeExample() {
    return "BFS(start_node, goal_node) {" + "\n"
        + "	for(all nodes i) visited[i] = false; 	" + "\n" + "\n"
        + "	queue.push(start_node);" + "\n" + "	visited[start_node] = true;"
        + "\n" + "\n" + "	while(! queue.empty() ) { " + "\n"
        + "		node = queue.pop();               " + "\n"
        + "		if(node == goal_node) {         " + "\n" + "			return true;"
        + "\n" + "		}" + "\n" + "\n"
        + "		foreach(child of neighbours(node)) {          " + "\n"
        + "			if(visited[child] == false) { " + "\n"
        + "				queue.push(child);               " + "\n"
        + "				visited[child] = true;           " + "\n" + "			}" + "\n"
        + "		}" + "\n" + "	}" + "\n" + "	return false;                        "
        + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}