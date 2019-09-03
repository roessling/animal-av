package generators.graph.breadthfirstsearch;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

//import sun.misc.Queue;

public class BFS implements Generator {

  protected Language                  lang;
  Node                                node;
  GraphProperties                     graphprops;
  TextProperties                      textprops;
  static Node[]                       graphNodes;
  static String[]                     labels;
  MatrixProperties                    matrixprops;
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
  int                                 indexstart;
  int                                 size;
  public int                          inc     = 0;
  SourceCodeProperties                scProps = new SourceCodeProperties();
  LinkedList<Node>                    Set     = new LinkedList<Node>();

  // Vector Set = new Vector(120, 55);
  // Queue Set = new Queue();

  public void showGraph(Graph graph) {

    int size = graph.getSize();
    graphNodes = new Node[size];
    labels = new String[size];

    for (int i = 0; i < graph.getSize(); i++) {

      graphNodes[i] = graph.getNode(i);
      labels[i] = graph.getNodeLabel(i);

    }

    graphprops = (GraphProperties) animationProperties
        .getPropertiesByName("graph");
    gr = lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(), graphNodes,
        labels, graph.getDisplayOptions(), graphprops);

  }

  public void bfs(Node start, Graph gra) {

    showGraph(gra);
    showSourceCode();
    Text text5 = lang.newText(new Coordinates(600, 400), "", "text", display,
        textprops);
    size = gra.getSize();
    graphNodes = new Node[size];
    labels = new String[size];
    distance = new int[size];
    predecessor = new Node[size];
    besucht = new boolean[size];

    // StringBuffer sb= new StringBuffer(gra.getSize());
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
        // Set.enqueue(gr.getNode(indexstart));
        Set.addLast(gr.getNode(indexstart));
      } else {
        distance[i] = 5000;
        besucht[i] = false;
        predecessor[i] = null;
        distancenode = lang.newSourceCode(new Coordinates(80, 545 + inc),
            "distancenode" + i, null, scProps);
        distancenode.addCodeLine("*", null, 6 + (3 * i), null);
      }
      text5.setText("Initiasierung Distance Alle Knoten ", null, null);
    }
    lang.nextStep();
    sc.unhighlight(1);
    sc.unhighlight(2);
    sc.unhighlight(3);
    sc.toggleHighlight(5, 7);
    text5.setText(
        "Initiasieren der Queue mit StartKnoten : "
            + gr.getNodeLabel(indexstart), null, null);
    lang.nextStep();
    sc.toggleHighlight(7, 8);
    int x = 0;
    distancenode = lang.newSourceCode(new Coordinates(600, 440), "distancenode"
        + x, null, scProps);
    distancenode.addCodeLine("Die Reihenfolge der Travisierung ist : ", null,
        0, null);
    while (!Set.isEmpty()) {
      sc.highlight(8);
      text5
          .setText(
              "Prüfe ob die Queue leer ist, andernfalls entferne nächste Knoten vo der Queue",
              null, null);
      lang.nextStep();
      sc.toggleHighlight(8, 9);
      Node n = null;
      // try {
      n = (Node) Set.removeFirst();
      // } catch (InterruptedException e) {
      // e.printStackTrace();
      // }
      int y = getByName(n);

      distancenode = lang.newSourceCode(new Coordinates(980, 440),
          "distancenode" + x, null, scProps);
      distancenode.addCodeLine(gr.getNodeLabel(y), null, x, null);
      x++;
      text5.setText(
          "Die nächste noch nicht besucht Knoten " + gr.getNodeLabel(y)
              + " wird aus dem Queue entfernt", null, null);
      gr.highlightNode(y, null, null);
      if (y != indexstart)
        gr.highlightEdge(getByName(predecessor[y]), y, null, null);
      // int [] edgeForNode = gr.getEdgesForNode(y);

      gr.highlightNode(y, null, null);
      labelnode = lang.newSourceCode(new Coordinates(50, 578 + inc),
          "labelnode", null, scProps);
      labelnode.addCodeLine(gr.getNodeLabel(y), null, 0, null);
      labelnode = lang.newSourceCode(new Coordinates(50, 578 + inc),
          "labelnode", null, scProps);
      labelnode.addCodeLine("|", null, 4, null);
      lang.nextStep();
      text5
          .setText(
              "Alle Knoten die auf die Kanten der aktuellen Knoten verweisen werden überprüft",
              null, null);
      sc.toggleHighlight(9, 10);
      for (int j = 0; j < gr.getSize(); j++) {

        if (((gr.getEdgeWeight(y, j) == 1) || (gr.getEdgeWeight(j, y) == 1))
            && (besucht[j] == false)) {
          sc.unhighlight(15);
          sc.highlight(10);
          lang.nextStep();
          gr.highlightEdge(y, j, null, null);
          gr.highlightEdge(j, y, null, null);
          gr.highlightNode(j, null, null);
          text5.setText("", null, null);
          sc.toggleHighlight(10, 15);
          distance[j] = distance[y] + 1;
          predecessor[j] = gr.getNode(y);

          if ((besucht[j] == false) && (j != y)) {
            text5.setText("füge Knoten " + gr.getNodeLabel(j)
                + " in die Queue ein", null, null);
            Set.addLast(gr.getNode(j));
          }
          besucht[j] = true;
          lang.nextStep();
          if (j != y)
            gr.unhighlightNode(j, null, null);

          CheckpointUtils.checkpointEvent(this, "visited", new Variable("bfs",
              gr.getNodeLabel(j)));
        }

        distancenode = lang.newSourceCode(new Coordinates(80, 580 + inc),
            "distancenode" + j, null, scProps);
        if (distance[j] < 5000)
          distancenode.addCodeLine("" + distance[j], null, 6 + (j * 3), null);
        else
          distancenode.addCodeLine("*", null, 6 + (j * 3), null);

      }

      gr.highlightNode(y, null, null);
      sc.unhighlight(10);
      sc.unhighlight(15);
      besucht[y] = true;
      lang.addLine("************************************************************");
      inc = inc + 20;
    }

    CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
        "distance", distance));

  }

  /*
   * private String[] convertNodesToNames(Graph g){ String[] strings=new
   * String[g.getSize()];
   * 
   * for (int pos = 0; pos < strings.length; pos++) strings[pos++] =
   * g.getNodeLabel(pos);
   * 
   * return strings; }
   */

  public int getByName(Node node) {

    for (int i = 0; i < gr.getSize(); i++) {
      if (node == gr.getNode(i)) {
        return i;
      }
    }
    return -1;
  }

  public void init() { // initialize the main elements

    // Generate a new Language instance for content creation
    // Parameter: Animation title, author, width, height
    lang = new AnimalScript("BFS Animation", "Madieha + Bouchra", 620, 480);
    // Activate step control
    lang.setStepMode(true);
    // create array properties with default values
    matrixprops = new MatrixProperties();
    textprops = new TextProperties();
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

    textprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
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
    sc = lang.newSourceCode(new Coordinates(40, 50), "sourceCode", null,
        scProps);
    labelnode = lang.newSourceCode(new Coordinates(50, 530), "labelnode", null,
        scProps);
    distancenode = lang.newSourceCode(new Coordinates(55, 600), "distancenode",
        null, scProps);

    // add a code line6
    // parameters: code it self; name(can be null); indentation level; display
    // options
    sc.addCodeLine("public void BFS(Node start,Graph G) {", null, 0, null);
    sc.addCodeLine("for(Node u :g.getNode()){", null, 1, null);
    sc.addCodeLine("Vorgänger[u]== nil;", null, 2, null);
    sc.addCodeLine("Distance[u] == 9000;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("start.distance = 0;", null, 1, null);
    sc.addCodeLine("Q = null", null, 1, null);
    sc.addCodeLine("Enqueue(Q,s);", null, 1, null);
    sc.addCodeLine("while(!Q.isEmpty(){", null, 1, null);
    sc.addCodeLine("u = Dequeue(Q);", null, 2, null);
    sc.addCodeLine("for(Edge e:u.getEdges()){", null, 3, null);
    sc.addCodeLine("v = e.getDestinations();", null, 4, null);
    sc.addCodeLine("if (besucht[v] == false){", null, 4, null);
    sc.addCodeLine("distance[v]== distance[u]+1", null, 5, null);
    sc.addCodeLine("vorgaenger[v]==u;", null, 5, null);
    sc.addCodeLine("Enqueue(Q,v);", null, 5, null);
    sc.addCodeLine("}", null, 4, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("besucht[u]== true;", null, 3, null);
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
    return "Animates BFS with Source Code + Highlighting"; // description
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
    return "BFS"; // the title to be displayed
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init(); // ensure all properties are set up :-)
    Enumeration<String> iter = primitives.keys();
    while (iter.hasMoreElements()) {
      // for (iter= primitives.keys();iter.hasMoreElements();){
      String Element = iter.nextElement();
      if (Element.contains("graph"))
        gr = (Graph) primitives.get(Element);

    }
    // System.out.println("rrrrrrrrr"+gr.getProperties().get("directed"));
    animationProperties = props;

    bfs(gr.getStartNode(), gr);

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {

    return "Breadth-First Search";
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
