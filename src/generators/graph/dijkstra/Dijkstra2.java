package generators.graph.dijkstra;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.TicksTiming;

public class Dijkstra2 implements Generator {

  protected Language                  lang;
  Node                                node;
  MatrixProperties                    matrixProps;
  RectProperties                      rectProps;
  GraphProperties                     graphProps;
  CircleProperties                    circleProps;
  PolylineProperties                  arcProps;
  TextProperties                      textProps;
  IntMatrix                           matrix;
  String                              name;
  int[][]                             graphAdjacencyMatrix;
  static Node[]                       graphNodes;
  static String[]                     labels;
  DisplayOptions                      display;
  public SourceCode                   sc, labelNode, distanceNode;

  // to ensure it is visible out side the method...
  public Graph                        gr;
  public int[]                        nodeIndices;
  public AnimationPropertiesContainer animationProperties;
  Node                                start, target;
  int[]                               distance;
  Node[]                              predecessor;
  boolean[]                           visited;
  int                                 indexTarget, indexStart;
  int                                 size;
  public int                          inc     = 0;
  SourceCodeProperties                scProps = new SourceCodeProperties();

  public void showGraph(Graph graph) {
    int size = graph.getSize();
    graphNodes = new Node[size];
    labels = new String[size];
    for (int i = 0; i < graph.getSize(); i++) {
      graphNodes[i] = graph.getNode(i);
      labels[i] = graph.getNodeLabel(i);
    }

    graphProps = (GraphProperties) animationProperties
        .getPropertiesByName("graph");
    /*
     * graphprops.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLUE);
     * graphprops.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
     * graphprops.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLUE);
     * graphprops.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
     * graphprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
     */
    // graphprops.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,Color.green);
    gr = lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(), graphNodes,
        labels, graph.getDisplayOptions(), graphProps);

  }

  public int dij(Node start, Node tarjet, Graph gra) {

    showGraph(gra);
    showSourceCode();
    // IntMatrix matrix = lang.newIntMatrix(new Coordinates(70,40) ,
    // graphAdjacencyMatrix, "matrix", null, matrixprops);
    Text text5 = lang.newText(new Coordinates(600, 400), "", "text", display,
        textProps);
    TicksTiming timing = new TicksTiming(30);
    size = gra.getSize();
    graphNodes = new Node[size];
    labels = new String[size];
    distance = new int[size];
    predecessor = new Node[size];
    visited = new boolean[size];

    Vector<Node> Set = new Vector<Node>();
    sc.highlight(0);
    // initialisierung Distance und Alle Knoten sind unbesucht
    for (int i = 0; i < gra.getSize(); i++) {
      distance[i] = 5000;
      visited[i] = false;
      predecessor[i] = null;
    }
    lang.nextStep();
    sc.toggleHighlight(0, 1);
    for (int i = 0; i < gr.getSize(); i++) {
      distanceNode = lang.newSourceCode(new Coordinates(80, 500 + inc),
          "distancenode" + i, null, scProps);
      distanceNode.addCodeLine("Distance", null, 0, null);
      distanceNode
          .addCodeLine("" + gr.getNodeLabel(i), null, 6 + (3 * i), null);
      graphNodes[i] = gr.getNode(i);
      labels[i] = gr.getNodeLabel(i);
      if (start == gr.getNode(i)) {
        distanceNode = lang.newSourceCode(new Coordinates(80, 545 + inc),
            "distancenode" + i, null, scProps);
        distanceNode.addCodeLine("0", null, 6 + (3 * i), null);
        // lang.nextStep();
        distance[i] = 0;
        text5.setText("der start Knoten mit eine Distanz null initializiert",
            null, null);
        predecessor[i] = null;
        indexStart = i;
        lang.nextStep();
        // text5.hide();
        sc.toggleHighlight(1, 2);
        text5.setText(
            "Danach wird in die Menge der abzuarbeitenden Knoten eingefügt",
            null, null);
        Set.add(start);
      }
      if (i != indexStart) {
        distanceNode = lang.newSourceCode(new Coordinates(80, 545 + inc),
            "distancenode" + i, null, scProps);
        distanceNode.addCodeLine("*", null, 6 + (3 * i), null);
      }
      if (tarjet == gr.getNode(i)) {
        indexTarget = i;

      }
      if (tarjet == start) {

        text5.setText(
            "start und ziel Knoten sind gleich" + gr.getNodeLabel(indexStart)
                + gr.getNodeLabel(indexTarget), null, null);
        return -1;
      }
    }
    lang.nextStep();
    text5.setText("", null, null);
    sc.toggleHighlight(2, 3);
    while (!Set.isEmpty()) {
      lang.nextStep();
      sc.toggleHighlight(3, 4);
      text5.setText(
          "Die nächste noch nicht besucht Knoten wird aus dem set geholt",
          null, null);
      Node n = getNeighbour(Set);
      int y = getByName(n);
      gr.highlightNode(y, null, null);
      lang.nextStep();
      sc.toggleHighlight(4, 5);
      text5.setText(
          "Es wird überprüft ob der aktuelle Knoten der Zielknoten ist", null,
          null);
      if (n == tarjet) {
        lang.nextStep();
        sc.toggleHighlight(5, 6);
        text5.setText(
            "kürzste weg von start knoten  " + gr.getNodeLabel(indexStart)
                + "  zum ziel Knoten  " + gra.getNodeLabel(indexTarget)
                + "  ist " + distance[indexTarget], timing, timing);
        gr.highlightNode(indexTarget, null, null);
        Node vorgaengerziel = getvorgaenger(n);
        y = getByName(vorgaengerziel);
        gr.highlightNode(y, null, null);
        gr.highlightEdge(y, indexTarget, null, null);
        while (vorgaengerziel != start) {
          int x = getByName(vorgaengerziel);
          vorgaengerziel = getvorgaenger(vorgaengerziel);
          y = getByName(vorgaengerziel);
          gr.highlightEdge(y, x, null, null);
          gr.highlightNode(y, null, null);

        }
        CheckpointUtils.checkpointEvent(this, "miniDistance", new Variable(
            "distance", distance[indexTarget]));// ////////////////
        CheckpointUtils.checkpointEvent(this, "nodeDistance", new Variable(
            "distance", distance));// ///////////////////
        return distance[indexTarget];
      }

      lang.nextStep();
      text5.setText("", null, null);
      sc.toggleHighlight(5, 8);

      int[] Edge = gr.getEdgesForNode(y);
      lang.nextStep();
      sc.toggleHighlight(8, 9);
      text5
          .setText(
              "Alle Knoten auf die Kanten der aktuellen Knoten verweise, werden überprüft",
              null, null);
      gr.highlightNode(y, null, null);
      lang.nextStep();
      text5.setText("", null, null);

      labelNode = lang.newSourceCode(new Coordinates(50, 578 + inc),
          "labelnode", null, scProps);
      labelNode.addCodeLine(gr.getNodeLabel(y), null, 0, null);
      labelNode = lang.newSourceCode(new Coordinates(50, 578 + inc),
          "labelnode", null, scProps);
      labelNode.addCodeLine("|", null, 4, null);

      for (int j = 0; j < Edge.length; j++) {

        if (gr.getEdgeWeight(y, j) != 0) {
          sc.toggleHighlight(9, 10);
          gr.highlightEdge(y, j, null, null);
          gr.highlightNode(j, null, null);
          text5.setText("Überprüfen von Knoten  " + gr.getNodeLabel(j), null,
              null);

          if (!Set.contains(gr.getNode(j))) {
            text5.setText("Knoten " + gr.getNodeLabel(j)
                + " wird nocht nicht besucht", null, null);
            lang.nextStep();
            sc.toggleHighlight(10, 11);
            distance[j] = distance[y] + gr.getEdgeWeight(y, j);
            predecessor[j] = gr.getNode(y);
            lang.nextStep();
            sc.toggleHighlight(11, 12);
            text5.setText("Der Knoten wird in die Menge eingefügt", null, null);
            Set.add(gra.getNode(j));
            lang.nextStep();
            sc.unhighlight(12);
          }

          else {
            lang.nextStep();
            sc.unhighlight(10);
            sc.toggleHighlight(12, 13);
            text5
                .setText(
                    "Knoten "
                        + gr.getNodeLabel(j)
                        + " ist schon in die Menge set eingefügt. Es wird überprüft ob der aktuelle Weg kürzer ist",
                    null, null);
            if ((distance[j] > distance[y] + gr.getEdgeWeight(y, j))
                && (visited[j] == false) && (gr.getEdgeWeight(y, j) != 0)) {
              lang.nextStep();
              sc.toggleHighlight(13, 14);
              distance[j] = distance[y] + gr.getEdgeWeight(y, j);
              predecessor[j] = gr.getNode(y);
            }
          }

          lang.addLine("*************************");

          lang.nextStep();

          sc.unhighlight(10);
          sc.unhighlight(12);
          // lang.nextStep();
          sc.unhighlight(13);
          sc.unhighlight(14);
          gr.unhighlightNode(j, null, null);
          gr.unhighlightEdge(y, j, null, null);
          text5.setText("", null, null);
        }

        distanceNode = lang.newSourceCode(new Coordinates(80, 580 + inc),
            "distancenode" + j, null, scProps);
        if (distance[j] < 5000)
          distanceNode.addCodeLine("" + distance[j], null, 6 + (j * 3), null);
        else
          distanceNode.addCodeLine("*", null, 6 + (j * 3), null);
      }
      visited[y] = true;
      gr.unhighlightNode(y, null, null);
      inc = inc + 20;
      // CheckpointUtils.checkpointEvent(this,"nodeDistance",new
      // Variable("distance",distance));/////////////////////
    }

    return 0;
  }

  public Node getNeighbour(Vector<Node> t) {

    int kleinsteGewicht = 90000;
    Node nodemitKleinsteGewicht = null;
    Enumeration<Node> u = t.elements();
    while (u.hasMoreElements()) {
      Node m = u.nextElement();
      int x = getByName(m);
      if ((visited[x] == false) && (distance[x] < kleinsteGewicht)) {
        kleinsteGewicht = distance[x];
        nodemitKleinsteGewicht = m;
      }

    }
    return nodemitKleinsteGewicht;
  }

  public int getByName(Node node) {

    for (int i = 0; i < gr.getSize(); i++) {
      if (node == gr.getNode(i)) {
        return i;

      }

    }

    return -1;
  }

  public Node getvorgaenger(Node node) {
    int x = getByName(node);
    return predecessor[x];

  }

  public void init() { // initialize the main elements
    // Generate a new Language instance for content creation
    // Parameter: Animation title, author, width, height
    lang = new AnimalScript("Dijkstra Animation", "Madieha + Bouchra", 620, 480);
    // Activate step control
    lang.setStepMode(true);
    // create array properties with default values
    matrixProps = new MatrixProperties();
    textProps = new TextProperties();
    rectProps = new RectProperties();
    arcProps = new PolylineProperties();
    circleProps = new CircleProperties();

    circleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

    arcProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);

    // Redefine properties: border red, filled with gray
    matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
    matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.pink);
    matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.MAGENTA);
    matrixProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));

    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.orange);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);

  }

  public void showSourceCode() {
    // first, set the visual properties for the source code

    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.CYAN);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 16));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // now, create the source code entity
    sc = lang.newSourceCode(new Coordinates(40, 100), "sourceCode", null,
        scProps);
    labelNode = lang.newSourceCode(new Coordinates(50, 530), "labelnode", null,
        scProps);
    distanceNode = lang.newSourceCode(new Coordinates(55, 600), "distancenode",
        null, scProps);
    // add a code line6
    // parameters: code it self; name(can be null); indentation level; display
    // options
    sc.addCodeLine("public int Dijkstra(Node start, Node tarjet,) {", null, 0,
        null);
    sc.addCodeLine("start.distance = 0;", null, 1, null);
    sc.addCodeLine("set.add(start);", null, 1, null);
    sc.addCodeLine("while(!set.isEmpty(){", null, 1, null);
    sc.addCodeLine("Node n = set.getNextNeighbour();", null, 1, null);
    sc.addCodeLine("if (n==tarjet){", null, 1, null);
    sc.addCodeLine("return n.distance;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("for(Edge e:n.getEdges()){", null, 1, null);
    sc.addCodeLine("v = e.getDestinations();", null, 2, null);
    sc.addCodeLine("if (!set.contains(v)){", null, 2, null);
    sc.addCodeLine("v.distance = n.distance + e.weight ;", null, 3, null);
    sc.addCodeLine("set.add(v);", null, 3, null);
    sc.addCodeLine("} else if (v.distance > n.distance + e.weight){", null, 2,
        null);
    sc.addCodeLine("v.distance = n.distance + e.weight ;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    labelNode.addCodeLine("----------------------------------------", null, 0,
        null);
    labelNode.addCodeLine("|", null, 4, null);

  }

  public String getCodeExample() {
    return "Straight forward Warshall Algorithm"; // to give readers an
                                                  // impression
  }

  public Locale getContentLocale() {
    return Locale.US; // US-English
  }

  public String getDescription() {
    return "Animates Warshall with Source Code + Highlighting"; // description
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH); // this is
                                                                  // about
                                                                  // sorting!
  }

  public String getName() {
    return "Dijkstra"; // the title to be displayed
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init(); // ensure all properties are set up :-)
    // int matrix[][]={ {0, 1, 0}, {0, 0, 9},{1, 0, 3} };
    // adapt the COLOR to whatever the user chose
    // you could do this for all properties if you wanted to...
    /*
     * int le=matrix.length; graphNodes=new Node[le]; labels=new String[le];
     * showGraph(matrix);
     */
    Enumeration<String> iter;
    for (iter = primitives.keys(); iter.hasMoreElements();) {
      String element = iter.nextElement();
      if (element.contains("graph"))
        gr = (Graph) primitives.get(element);

    }

    animationProperties = props;

    dij(gr.getStartNode(), gr.getTargetNode(), gr);

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {

    return "Dijkstra's Algorithm";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public String getAnimationAuthor() {
    return "Madieha Taddbier, Bouchra Elfakir";
  }

}
