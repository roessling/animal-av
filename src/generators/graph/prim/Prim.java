package generators.graph.prim;

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
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

public class Prim implements Generator {

  protected Language                  lang;
  Node                                node;
  MatrixProperties                    matrixprops;
  RectProperties                      rectprops;
  GraphProperties                     graphprops;
  TextProperties                      textprops;
  IntMatrix                           matrix;
  String                              name;
  int[][]                             graphAdjacencyMatrix;
  static Node[]                       graphNodes;
  static String[]                     labels;
  DisplayOptions                      display;
  public SourceCode                   sc, labelnode, distancenode;         // to
                                                                            // ensure
                                                                            // it
                                                                            // is
                                                                            // visible
                                                                            // out
                                                                            // side
                                                                            // the
                                                                            // method...
  public Graph                        gr;
  public int[]                        nodeIndices;
  public AnimationPropertiesContainer animationProperties;
  Node                                start;
  int[]                               distance;
  Node[]                              predecessor;
  boolean[]                           besucht;
  int                                 indextarjet, indexstart;
  int                                 size;
  public int                          inc     = 0;
  SourceCodeProperties                scProps = new SourceCodeProperties();

  // private Node n1;
  public void showGraph(Graph graph) {
    int size = graph.getSize();
    graphNodes = new Node[size];
    labels = new String[size];
    // int [][] mat= graph.getAdjacencyMatrix();
    for (int i = 0; i < graph.getSize(); i++) {
      graphNodes[i] = graph.getNode(i);
      labels[i] = graph.getNodeLabel(i);
    }

    graphprops = (GraphProperties) animationProperties
        .getPropertiesByName("graph");
    gr = lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(), graphNodes,
        labels, graph.getDisplayOptions(), graphprops);
  }

  public void prim(Node start, Graph gra) {

    showGraph(gra);
    showSourceCode();
    Text text5 = lang.newText(new Coordinates(600, 400), "", "text", display,
        textprops);
    // TicksTiming timing = new TicksTiming(1);
    // TicksTiming timing1 = new TicksTiming(2);
    size = gra.getSize();
    graphNodes = new Node[size];
    labels = new String[size];
    // int [][] mat= gra.getAdjacencyMatrix();
    distance = new int[size];
    predecessor = new Node[size];
    besucht = new boolean[size];
    Vector<Node> set = new Vector<Node>();

    // initialisierung Distance und Alle Knoten sind unbesucht
    sc.highlight(0);
    lang.nextStep();
    sc.toggleHighlight(0, 1);
    sc.highlight(2);
    sc.highlight(3);
    sc.highlight(5);
    for (int i = 0; i < gra.getSize(); i++) {
      distancenode = lang.newSourceCode(new Coordinates(80, 500 + inc),
          "distancenode" + i, null, scProps);
      distancenode.addCodeLine("Distance", null, 0, null);
      distancenode
          .addCodeLine("" + gr.getNodeLabel(i), null, 6 + (3 * i), null);
      graphNodes[i] = gr.getNode(i);
      labels[i] = gr.getNodeLabel(i);
      set.add(gr.getNode(i));
      if (start == gr.getNode(i)) {
        distancenode = lang.newSourceCode(new Coordinates(80, 545 + inc),
            "distancenode" + i, null, scProps);
        distancenode.hide();
        distancenode.addCodeLine("", null, 6 + (3 * i), null);
        distancenode = lang.newSourceCode(new Coordinates(80, 545 + inc),
            "distancenode" + i, null, scProps);
        distancenode.addCodeLine("0", null, 6 + (3 * i), null);

        distance[i] = 0;
        predecessor[i] = null;
        indexstart = i;

      } else {
        distance[i] = 5000;
        besucht[i] = false;
        predecessor[i] = null;
        distancenode = lang.newSourceCode(new Coordinates(80, 545 + inc),
            "distancenode" + i, null, scProps);
        distancenode.addCodeLine("*", null, 6 + (3 * i), null);
      }

      text5.setText("Initialisierung Distance Alle Knoten ", null, null);
    }
    lang.nextStep();
    sc.unhighlight(1);
    sc.unhighlight(2);
    sc.unhighlight(3);
    sc.toggleHighlight(5, 6);
    text5.setText(
        "Füge alle Knoten in  der Set ein. Start with node  "
            + gr.getNodeLabel(indexstart), null, null);
    lang.nextStep();
    sc.toggleHighlight(6, 7);
    while (!set.isEmpty()) {
      sc.highlight(7);
      text5
          .setText(
              "Prüfe ob Set leer ist, andernfalls besuche nächste Knoten des Graphes",
              null, null);
      lang.nextStep();
      sc.toggleHighlight(7, 8);
      Node n = getNeighbour(set);
      int y = getByName(n);
      set.remove(n);
      gr.highlightNode(y, null, null);
      if (y != indexstart)
        gr.highlightEdge(getByName(predecessor[y]), y, null, null);

      if (getByName(predecessor[y]) >= 0)
        CheckpointUtils.checkpointEvent(this, "chooseEdge", new Variable(
            "node1", gr.getNodeLabel(getByName(predecessor[y]))), new Variable(
            "node2", gr.getNodeLabel(y)));

      text5.setText(
          "Die nächste noch nicht besuchte Knoten mit der kleinsten Distance "
              + gr.getNodeLabel(y) + " wird aus dem Set geholt", null, null);

      CheckpointUtils.checkpointEvent(this, "getNextNode", new Variable("node",
          gr.getNodeLabel(y)));// ////////////////////////////////

      lang.nextStep();
      text5.setText("", null, null);
      sc.toggleHighlight(8, 9);
      int[] Edge = gr.getEdgesForNode(y);
      gr.highlightNode(y, null, null);
      labelnode = lang.newSourceCode(new Coordinates(50, 578 + inc),
          "labelnode", null, scProps);
      labelnode.addCodeLine(gr.getNodeLabel(y), null, 0, null);
      labelnode = lang.newSourceCode(new Coordinates(50, 578 + inc),
          "labelnode", null, scProps);
      labelnode.addCodeLine("|", null, 4, null);

      for (int j = 0; j < Edge.length; j++) {
        sc.highlight(9);
        gr.highlightEdge(y, j, null, null);
        gr.highlightNode(j, null, null);
        text5.setText(
            "Alle Knoten auf die Kanten der aktuellen Knoten ("
                + gr.getNodeLabel(y) + ") verweise, werden überprüft", null,
            null);
        if ((gr.getEdgeWeight(y, j) != 0)
            && (gr.getEdgeWeight(y, j) < distance[j]) && (besucht[j] == false)) {
          lang.nextStep();
          sc.toggleHighlight(9, 11);
          sc.highlight(12);
          sc.highlight(13);
          // text5.setText("", null, null);
          distance[j] = gr.getEdgeWeight(y, j);
          predecessor[j] = gr.getNode(y);
          lang.nextStep();
        }

        else if (gr.getEdgeWeight(y, j) != 0) {
          gr.highlightEdge(y, j, null, null);
          lang.nextStep();
        }

        sc.unhighlight(11);
        sc.unhighlight(12);
        sc.unhighlight(13);
        distancenode = lang.newSourceCode(new Coordinates(80, 580 + inc),
            "distancenode" + j, null, scProps);
        if (distance[j] < 5000)
          distancenode.addCodeLine("" + distance[j], null, 6 + (j * 3), null);
        else
          distancenode.addCodeLine("*", null, 6 + (j * 3), null);
        gr.unhighlightEdge(y, j, null, null);
        if ((besucht[j] == false) && (j != y))
          gr.unhighlightNode(j, null, null);
      }
      gr.highlightNode(y, null, null);
      sc.unhighlight(13);
      sc.unhighlight(9);
      besucht[y] = true;
      lang.addLine("************************************************************");
      inc = inc + 20;
    }
    int cost = 0;
    for (int j = 0; j < distance.length; j++) {
      cost = distance[j] + cost;
    }

    CheckpointUtils.checkpointEvent(this, "miniCost", new Variable(
        "costOfTree", cost));// /////////////////////////////////////

    sc.unhighlight(7);
    text5.setText("", null, null);
  }

  public Node getNeighbour(Vector<Node> t) {

    int kleinsteGewicht = 90000;
    Node nodemitKleinsteGewicht = null;
    Enumeration<Node> u = t.elements();
    while (u.hasMoreElements()) {
      Node m = u.nextElement();
      int x = getByName(m);
      if (distance[x] < kleinsteGewicht) {
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

  public Node getpredecessor(Node node) {
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
    matrixprops = new MatrixProperties();
    textprops = new TextProperties();
    rectprops = new RectProperties();
    // Redefine properties: border red, filled with gray
    matrixprops.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);// color
                                                                                // red
    matrixprops.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
    matrixprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    matrixprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.pink); // fill
                                                                        // color
                                                                        // gray
    matrixprops.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.MAGENTA);
    matrixprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    textprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));

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
    labelnode = lang.newSourceCode(new Coordinates(50, 530), "labelnode", null,
        scProps);
    distancenode = lang.newSourceCode(new Coordinates(55, 600), "distancenode",
        null, scProps);

    sc.addCodeLine("public void Prim(Node start,Graph G) {", null, 0, null);
    sc.addCodeLine("for(Node u :g.getNode()){", null, 1, null);
    sc.addCodeLine("Vorgänger[u]== nil;", null, 2, null);
    sc.addCodeLine("Distance[u] == 9000;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("start.distance = 0;", null, 1, null);
    sc.addCodeLine("Q <- G[u]", null, 1, null);
    sc.addCodeLine("while(!Q.isEmpty(){", null, 1, null);
    sc.addCodeLine("Node n = Q.Extract-Min(Q);", null, 2, null);
    sc.addCodeLine("for(Edge e:n.getEdges()){", null, 3, null);
    sc.addCodeLine("v = e.getDestinations();", null, 4, null);
    sc.addCodeLine("if (getEdgeWeight(u,v)<distance[v]){", null, 4, null);
    sc.addCodeLine("distance[v]==getEdgeWeight(u,v)", null, 5, null);
    sc.addCodeLine("predecessor[v]==u;", null, 5, null);
    sc.addCodeLine("}", null, 4, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    labelnode
        .addCodeLine(
            "---------------------------------------------------------------------------",
            null, 0, null);
    labelnode.addCodeLine("|", null, 4, null);

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
    return "Prim"; // the title to be displayed
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init(); // ensure all properties are set up :-)
    Enumeration<String> iter = primitives.keys();
    while (iter.hasMoreElements()) {
      String Element = (String) iter.nextElement();
      if (Element.contains("graph"))
        gr = (Graph) primitives.get(Element);

    }

    animationProperties = props;

    prim(gr.getStartNode(), gr);

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {

    return "Prim Algorithm";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public String getAnimationAuthor() {
    return "Bouchra Elfakir";
  }

}
