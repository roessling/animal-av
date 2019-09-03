package generators.graph.depthfirstsearch;

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
import java.util.Stack;

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

public class DFS implements Generator {
  protected Language                  lang;
  Node                                node;
  GraphProperties                     graphprops;
  TextProperties                      textprops;
  static Node[]                       graphNodes;
  static String[]                     labels;
  MatrixProperties                    matrixprops;
  DisplayOptions                      display;
  public SourceCode                   sc, labelnode, distancenode;
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
  Stack<Node>                         Set     = new Stack<Node>();

  public void showGraph(Graph graph) {
    int size = graph.getSize();
    graphNodes = new Node[size];
    labels = new String[size];
    // int [][] mat = graph.getAdjacencyMatrix();
    for (int i = 0; i < graph.getSize(); i++) {
      graphNodes[i] = graph.getNode(i);
      labels[i] = graph.getNodeLabel(i);
    }
    graphprops = (GraphProperties) animationProperties
        .getPropertiesByName("graph");
    gr = lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(), graphNodes,
        labels, graph.getDisplayOptions(), graphprops);
  }

  public void dfs(Graph gra) {
    showGraph(gra);
    showSourceCode();
    Text text5 = lang.newText(new Coordinates(600, 440), "", "text", display,
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
    sc.highlight(0);
    lang.nextStep();
    sc.toggleHighlight(0, 1);
    sc.highlight(2);
    sc.highlight(3);
    // initialisierung Distance und Alle Knoten sind unbesucht
    for (int i = 0; i < gra.getSize(); i++) {
      distance[i] = 5000;
      besucht[i] = false;
      predecessor[i] = null;
      distancenode = lang.newSourceCode(new Coordinates(80, 500 + inc),
          "distancenode" + i, null, scProps);
      distancenode.addCodeLine("Distance", null, 0, null);
      distancenode
          .addCodeLine("" + gr.getNodeLabel(i), null, 6 + (3 * i), null);
      graphNodes[i] = gr.getNode(i);
      labels[i] = gr.getNodeLabel(i);
      distancenode = lang.newSourceCode(new Coordinates(80, 545 + inc),
          "distancenode" + i, null, scProps);
      distancenode.addCodeLine("*", null, 6 + (3 * i), null);
      text5.setText("Initiasierung Distance Alle Knoten ", null, null);
    }
    int time = 0;
    lang.nextStep();
    sc.unhighlight(1);
    sc.unhighlight(2);
    sc.unhighlight(3);
    int x = 0;
    for (int i = 0; i < gra.getSize(); i++) {
      sc.unhighlight(10);
      if (besucht[i] == false) {
        sc.highlight(6);
        text5.setText("Alle Knoten des Graphes werden besucht ", null, null);
        lang.nextStep();
        Set.push(gr.getNode(i));
        time = time + 1;
        distance[i] = time;
        sc.toggleHighlight(6, 7);
        text5.setText(
            "Initialisieren den Stack mit Knoten : " + gr.getNodeLabel(i),
            null, null);
        lang.nextStep();
      }
      distancenode = lang.newSourceCode(new Coordinates(600, 460),
          "distancenode" + x, null, scProps);
      distancenode.addCodeLine("Die Reihenfolge der Travisierung ist : ", null,
          0, null);
      while (!Set.empty()) {
        Node n = null;
        int nachbarn = 0;
        n = (Node) Set.pop();
        int y = getByName(n);
        Set.push(gr.getNode(y));
        if ((besucht[y] == false) && (nachbarn == 0)) {
          distancenode = lang.newSourceCode(new Coordinates(980, 460),
              "distancenode" + x, null, scProps);
          distancenode.addCodeLine(gr.getNodeLabel(y), null, x, null);
          x++;

          sc.unhighlight(11);
          sc.toggleHighlight(7, 10);
          text5
              .setText(
                  "Prüfe ob den Stack leer ist, andernfalls entferne nächste Knoten von dem Stack",
                  null, null);
          lang.nextStep();
          text5.setText("", null, null);
          sc.toggleHighlight(10, 11);
          text5.setText(
              "Die nächste noch nicht besucht Knoten " + gr.getNodeLabel(y)
                  + " wird aus dem Stack geholt", null, null);
          CheckpointUtils.checkpointEvent(this, "visited", new Variable("dfs",
              gr.getNodeLabel(y)));
          gr.highlightNode(y, null, null);
          int[] Edge = gr.getEdgesForNode(y);
          lang.nextStep();
          labelnode = lang.newSourceCode(new Coordinates(50, 578 + inc),
              "labelnode", null, scProps);
          labelnode.addCodeLine(gr.getNodeLabel(y), null, 0, null);
          labelnode = lang.newSourceCode(new Coordinates(50, 578 + inc),
              "labelnode", null, scProps);
          labelnode.addCodeLine("|", null, 4, null);
          text5.setText(
              "Alle Knoten auf die Kanten der aktuellen Knoten "
                  + gr.getNodeLabel(y) + " verweise, werden überprüft", null,
              null);
          sc.toggleHighlight(11, 12);
          for (int j = 0; j < Edge.length; j++) {
            if ((Edge[j] == 1) && (besucht[j] == false)) {
              time = time + 1;
              distance[j] = time;
              predecessor[j] = gr.getNode(y);
              if ((!Set.contains(gr.getNode(j))) && (besucht[j] == false)
                  && (j != y)) {
                lang.nextStep();
                text5.setText("füge Knoten " + gr.getNodeLabel(j)
                    + " in den Stack ein", null, null);
                gr.highlightEdge(y, j, null, null);
                gr.highlightNode(j, null, null);
                sc.toggleHighlight(12, 14);
                Set.push(gr.getNode(j));
                lang.nextStep();
                text5.setText("", null, null);
                nachbarn = 1;
              }

            }
            sc.unhighlight(14);
            distancenode = lang.newSourceCode(new Coordinates(80, 580 + inc),
                "distancenode" + j, null, scProps);
            if (distance[j] < 5000)
              distancenode.addCodeLine("" + distance[j], null, 6 + (j * 3),
                  null);
            else
              distancenode.addCodeLine("*", null, 6 + (j * 3), null);
            if ((j != y) && (besucht[j]) == false)
              gr.unhighlightNode(j, null, null);
          }
          inc = inc + 20;
        }
        if (nachbarn == 0) {
          Set.pop();
          besucht[y] = true;
        }
        text5.setText("", null, null);
        sc.unhighlight(10);
        sc.unhighlight(14);
        sc.unhighlight(12);
        besucht[y] = true;
        lang.addLine("************************************************************");

      }
      sc.unhighlight(10);
      sc.unhighlight(11);

    }
    CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
        "distance", distance));// ////////////////////

    sc.unhighlight(6);
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
    lang = new AnimalScript("DFS Animation", "Madieha + Bouchra", 620, 480);
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
    sc = lang.newSourceCode(new Coordinates(40, 30), "sourceCode", null,
        scProps);
    labelnode = lang.newSourceCode(new Coordinates(50, 530), "labelnode", null,
        scProps);
    distancenode = lang.newSourceCode(new Coordinates(55, 600), "distancenode",
        null, scProps);
    // add a code line6
    // parameters: code it self; name(can be null); indentation level; display
    // options
    sc.addCodeLine("public void DFS(Node start,Graph G) {", null, 0, null);
    sc.addCodeLine("for(Node u :g.getNode()){", null, 1, null);
    sc.addCodeLine("Vorgänger[u]== nil;", null, 2, null);
    sc.addCodeLine("Distance[u] == 9000;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("time = 0;", null, 1, null);
    sc.addCodeLine("for(Node u :g.getNode()){", null, 1, null);
    sc.addCodeLine("Push(S,u);", null, 2, null);
    sc.addCodeLine("time = time + 1 ;", null, 2, null);
    sc.addCodeLine("Distance[u]= time ;", null, 2, null);
    sc.addCodeLine("while(!S.isEmpty(){", null, 2, null);
    sc.addCodeLine("u = PoP(S);", null, 3, null);
    sc.addCodeLine("for(Edge e:u.getEdges()){", null, 3, null);
    sc.addCodeLine("v = e.getDestinations();", null, 4, null);
    sc.addCodeLine("Push(S,u);", null, 4, null);
    sc.addCodeLine("time = time + 1", null, 4, null);
    sc.addCodeLine("distance[v]= time", null, 4, null);
    sc.addCodeLine("predecessor[v]=u;", null, 4, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("PoP(S)", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
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
    return "Animates DFS with Source Code + Highlighting"; // description
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  public String getName() {
    return "DFS"; // the title to be displayed
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init(); // ensure all properties are set up :-)
    Enumeration<String> iter = primitives.keys();
    while (iter.hasMoreElements()) {
      String Element = iter.nextElement();
      if (Element.contains("graph"))
        gr = (Graph) primitives.get(Element);
    }

    animationProperties = props;

    dfs(gr);

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {

    return "Depth-First Search";
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
