package generators.graph.floyd;

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

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.IntMatrix;
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

public class Floyd implements Generator {

  protected Language                  lang;
  Node                                node;
  GraphProperties                     graphprops;
  TextProperties                      textprops;
  static Node[]                       graphNodes;
  static String[]                     labels;
  MatrixProperties                    matrixprops;
  public IntMatrix                    matrix  = newIntMatrix();
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

  private IntMatrix newIntMatrix() {

    return null;
  }

  public void floyd(Graph gra) {

    showGraph(gra);
    showSourceCode();
    Text text5 = lang.newText(new Coordinates(640, 400), "", "text", display,
        textprops);
    Text text4 = lang.newText(new Coordinates(640, 420), "", "text", display,
        textprops);
    Text text3 = lang.newText(new Coordinates(640, 440), "", "text", display,
        textprops);
    Text text2 = lang.newText(new Coordinates(640, 480), "", "text", display,
        textprops);
    // TicksTiming timing = new TicksTiming(1);
    // TicksTiming timing1 = new TicksTiming(2);
    size = gra.getSize();
    graphNodes = new Node[size];
    labels = new String[size];
    int[][] mat = gra.getAdjacencyMatrix();

    distance = new int[size];
    predecessor = new Node[size];
    besucht = new boolean[size];
    // initialisierung Distance und Alle Knoten sind unbesucht

    for (int i = 0; i < gra.getSize(); i++) {
      distancenode = lang.newSourceCode(new Coordinates(80, 440 + inc),
          "distancenode" + i, null, scProps);
      distancenode.addCodeLine("Distance", null, 0, null);
      distancenode
          .addCodeLine("" + gr.getNodeLabel(i), null, 6 + (2 * i), null);
      graphNodes[i] = gr.getNode(i);
    }
    IntMatrix matrix = lang.newIntMatrix(new Coordinates(172, 515), mat, "",
        null, matrixprops);
    for (int i = 0; i < gr.getSize(); i++) {
      labelnode = lang.newSourceCode(new Coordinates(70, 503 + inc),
          "labelnode", null, scProps);
      labelnode.addCodeLine(gr.getNodeLabel(i), null, 4, null);
      labelnode = lang.newSourceCode(new Coordinates(60, 503 + inc),
          "labelnode", null, scProps);
      labelnode.addCodeLine("|", null, 6, null);
      inc = inc + 28;
    }
    inc = 0;
    sc.highlight(0);
    lang.nextStep();
    sc.toggleHighlight(0, 5);
    lang.nextStep();
    for (int x = 0; x < gr.getSize(); x++) {
      text4.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.magenta,
          null, null);
      text4.setText("intermidiateNode x : " + gr.getNodeLabel(x), null, null);
      for (int i = 0; i < gr.getSize(); i++) {
        text5.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN,
            null, null);
        text5.setText("Node             i : " + gr.getNodeLabel(i), null, null);
        if (mat[i][x] != 0)
          for (int j = 0; j < gr.getSize(); j++) {
            text3.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
                Color.CYAN, null, null);
            text3.setText("Node             j : " + gr.getNodeLabel(j), null,
                null);
            if (mat[x][j] != 0)
              if ((mat[i][j] == 0) || (mat[i][x] + (mat[x][j]) < mat[i][j])) {
                text2.setText(
                    "Edge(" + gr.getNodeLabel(i) + "," + gr.getNodeLabel(j)
                        + ") wird überprüft ob sie gleich 0 ist"
                        + " oder Edge(" + gr.getNodeLabel(i) + ","
                        + gr.getNodeLabel(x) + ")+Edge(" + gr.getNodeLabel(x)
                        + "," + gr.getNodeLabel(j) + ")" + "<Edge("
                        + gr.getNodeLabel(i) + "," + gr.getNodeLabel(j) + ")",
                    null, null);
                sc.toggleHighlight(5, 7);
                sc.highlight(8);
                gr.highlightEdge(i, x, null, null);
                gr.highlightEdge(x, i, null, null);
                gr.highlightEdge(x, j, null, null);
                gr.highlightEdge(j, x, null, null);
                gr.highlightNode(x, null, null);
                gr.highlightNode(i, null, null);
                gr.highlightNode(j, null, null);
                mat[i][j] = mat[i][x] + mat[x][j];
                matrix.highlightCell(i, j, null, null);
                lang.nextStep();
                text2.setText("", null, null);
                sc.toggleHighlight(7, 9);
                sc.unhighlight(8);
                lang.nextStep();
                sc.unhighlight(9);
                matrix.put(i, j, mat[i][j], null, null);
                matrix.show();
                gr.unhighlightEdge(i, x, null, null);
                gr.unhighlightEdge(x, i, null, null);
                gr.unhighlightEdge(x, j, null, null);
                gr.unhighlightEdge(j, x, null, null);
                gr.unhighlightNode(x, null, null);
                gr.unhighlightNode(i, null, null);
                gr.unhighlightNode(j, null, null);
                matrix.unhighlightCell(i, j, null, null);
              }

          }
        sc.unhighlight(5);

      }
      inc = inc + 20;
    }
    for (int i = 0; i < mat.length; i++) {
      for (int j = 0; j < mat.length; j++) {
        CheckpointUtils.checkpointEvent(this, "findDistance", new Variable(
            "distance", mat[i][j]));// ////////////////////
      }
    }

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
    matrixprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 8);
    matrixprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 20));
    textprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 18));

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
    labelnode = lang.newSourceCode(new Coordinates(60, 530), "labelnode", null,
        scProps);
    distancenode = lang.newSourceCode(new Coordinates(55, 600), "distancenode",
        null, scProps);
    // add a code line6
    // parameters: code it self; name(can be null); indentation level; display
    // options
    sc.addCodeLine("public void Floyd(Graph G) {", null, 0, null);
    sc.addCodeLine("int i, j, x ;", null, 1, null);
    sc.addCodeLine("for (int x=0 ; x<getSize() ; x++) {", null, 1, null);
    sc.addCodeLine("for (int i=0 ; i<getSize() ; i++) {", null, 2, null);
    sc.addCodeLine("if (Edge[i][x]!= 0)", null, 3, null);
    sc.addCodeLine("for (int j=0 ; j<getSize() ; j++){ ", null, 4, null);
    sc.addCodeLine("if (Edge[x][j]!= 0)", null, 5, null);
    sc.addCodeLine("if (Edge[i][j]== 0|| Edge[i][x]+ Edge[x][j]< Edge[i][j])",
        null, 6, null);
    sc.addCodeLine("Edge[i][j]= Edge[i][x]+ Edge[x][j];", null, 7, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    // labelnode.addCodeLine("---------------------------------------------------------------------------",
    // null, 0, null);
    // labelnode.addCodeLine("|", null, 6, null);

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
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH); 
  }

  public String getName() {
    return "Floyd"; // the title to be displayed
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

    floyd(gr);

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {

    return "Floyd Algorithm";
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
