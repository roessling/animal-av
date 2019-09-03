package generators.graph.bipartite;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpers.BipartiteEdge;
import generators.graph.helpers.Nodes;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class BipartiteGraph implements ValidatingGenerator {
  private Language              lang;
  private int[][]               adjacencyMatrix;
  private SourceCodeProperties  sourceCodeProps;
  private TextProperties        textProps;

  public Nodes[]                nodes;
  public Node[]                 graphNodes;
  public Node[]                 graphNodes2;
  public BipartiteEdge[]        edges;
  public boolean                bipartite = true;
  // private Text header;
  // private Rect hRect;
  private Text                  ergebnis;
  private SourceCode            sc;
  private Text                  text1;
  private Text                  text2;
  private Text                  text3;
  private Text                  text4;
  private Text                  text5;
  private Text                  text6;
  private Text                  text7;
  private Text                  text8;
  private Text                  text9;
  private GraphProperties       graphProps;
  private GraphProperties       graphProps2;
  private ArrayProperties       arrayProps;
  private ArrayMarkerProperties markerProps;
  private ArrayMarker           i;
  private Text                  neighboursText;
  private Text                  Rekursion;
  private StringArray           Neighbours2;
  private int                   rekCnt;

  public BipartiteGraph() {
    // nothing to be done here...
  }

  public void init() {
    lang = new AnimalScript("Bipartite Graphen", "Admir Agia", 800, 600);
    lang.setStepMode(true);
    bipartite = true; // check this

    arrayProps = new ArrayProperties();

    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK); // color
                                                                                // red
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE); // fill
                                                                        // color
                                                                        // gray

    markerProps = new ArrayMarkerProperties();
    markerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    markerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    textProps = (TextProperties) props.getPropertiesByName("textProperties");
    graphProps = (GraphProperties) props.getPropertiesByName("graphProperties");
    graphProps2 = (GraphProperties) props
        .getPropertiesByName("graphProperties2");
    isBipartite();
    /* createSourceCode(); */
    return lang.toString();
  }

  public void createSourceCode() {
    // SourceCodeProperties scProps = new SourceCodeProperties();
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 12));
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    //
    SourceCode sc = lang.newSourceCode(new Coordinates(20, 100), "sourceCode",
        null, sourceCodeProps);

    sc.addCodeLine("public void isBipartite(){", null, 0, null);
    sc.addCodeLine("colour(nodes[0],1);", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    sc.addCodeLine(" ", null, 0, null);

    sc.addCodeLine("public void colour(Node node,int colour){", null, 0, null);
    sc.addCodeLine("if(isColourable(node, colour)){", null, 1, null);
    sc.addCodeLine("node.colour=colour;", null, 2, null);
    sc.addCodeLine("ArrayList<Node> neighbours = getNeighbours(node);", null,
        2, null);
    sc.addCodeLine("if(colour==1){", null, 2, null);
    sc.addCodeLine("colour=2;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("else{", null, 2, null);
    sc.addCodeLine("colour=1;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("for(int i=0;i<neighbours.size();i++){", null, 2, null);
    sc.addCodeLine("if(neighbours.get(i).colour==0){", null, 3, null);
    sc.addCodeLine("colour(neighbours.get(i),colour);", null, 4, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("else{", null, 1, null);
    sc.addCodeLine("bipartite=false;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    setSc(sc);
  }

  public SourceCode getSc() {
    return sc;
  }

  public void setSc(SourceCode sc) {
    this.sc = sc;
  }

  /*
   * Methode getNeghbour
   */
  public ArrayList<Nodes> getNeighbours(Nodes node) {
    ArrayList<Nodes> neighbours = new ArrayList<Nodes>();
    for (int i = 0; i < edges.length; i++) {
      if (edges[i].begin == node) {
        neighbours.add(edges[i].end);
      }
      if (edges[i].end == node) {
        neighbours.add(edges[i].begin);
      }
    }
    return neighbours;
  }

  public boolean isColourable(Nodes node, int colour) {
    boolean colourable = true;
    ArrayList<Nodes> neighbours = getNeighbours(node);
    for (int i = 0; i < neighbours.size(); i++) {
      if (neighbours.get(i).colour == colour) {
        colourable = false;
      }
    }
    return colourable;
  }

  public void colour(Nodes node, int colour, Node[] graphNodes, Graph g) {
    neighboursText.hide();
    Neighbours2.hide();
    Rekursion.hide();

    Rekursion = lang.newText(new Coordinates(500, 500),
        "Anzahl rekursiver Aufrufe: " + rekCnt, "rekursion", null, textProps);
    rekCnt++;
    i.hide();
    sc.unhighlight(16);
    sc.highlight(4);
    lang.nextStep();
    sc.toggleHighlight(4, 5);
    lang.nextStep();
    if (isColourable(node, colour)) {

      node.colour = colour;
      if (colour == 1) {

        g.highlightNode(graphNodes[node.number], null, null);
      } else {

        Graph n = getNewGraph(g.getNodeLabel(node.number),
            graphNodes[node.number]);
        n.highlightNode(graphNodes[node.number], null, null);

      }
      sc.toggleHighlight(5, 6);
      lang.nextStep();
      sc.toggleHighlight(6, 7);
      ArrayList<Nodes> neighbours = getNeighbours(node);

      String[] Neighbours = new String[neighbours.size()];
      for (int i = 0; i < neighbours.size(); i++) {
        Neighbours[i] = g.getNodeLabel(neighbours.get(i).number);
      }

      Neighbours2 = lang.newStringArray(new Coordinates(630, 400), Neighbours,
          "Neighbours", null, arrayProps);

      neighboursText = lang.newText(new Coordinates(500, 400), "Nachbarn von "
          + g.getNodeLabel(node.number) + ":", "neighbours", null, textProps);

      lang.nextStep();
      sc.toggleHighlight(7, 8);
      lang.nextStep();

      if (colour == 1) {
        sc.toggleHighlight(8, 9);
        lang.nextStep();
        sc.toggleHighlight(9, 10);
        lang.nextStep();
        sc.unhighlight(10);
        // colour = 2;
      } else {
        // colour = 1;
        sc.toggleHighlight(8, 11);
        lang.nextStep();
        sc.toggleHighlight(11, 12);
        lang.nextStep();
        sc.toggleHighlight(12, 13);
        lang.nextStep();
        sc.unhighlight(13);
      }
      boolean fertig = true;
      for (int j = 0; j < neighbours.size(); j++) {

        if (j == 0) {
          i = lang.newArrayMarker(Neighbours2, 0, "i", null, markerProps);
        } else if (fertig == false) {
          neighboursText.hide();
          Neighbours2.hide();
          lang.nextStep();
          Neighbours = new String[neighbours.size()];
          for (int i = 0; i < neighbours.size(); i++) {
            Neighbours[i] = g.getNodeLabel(neighbours.get(i).number);
          }

          Neighbours2 = lang.newStringArray(new Coordinates(630, 400),
              Neighbours, "Neighbours", null, arrayProps);
          neighboursText = lang.newText(new Coordinates(500, 400),
              "Nachbarn von " + g.getNodeLabel(node.number) + ":",
              "neighbours", null, textProps);
          i = lang.newArrayMarker(Neighbours2, j, "i", null, markerProps);
        } else {
          i.increment(null, null);
        }

        sc.highlight(14);
        lang.nextStep();
        sc.toggleHighlight(14, 15);
        lang.nextStep();
        if (neighbours.get(j).colour == 0) {

          sc.toggleHighlight(15, 16);
          lang.nextStep("rekursiver Aufruf");

          colour(neighbours.get(j), colour, graphNodes, g);
          if (j <= neighbours.size() - 1) {
            fertig = false;
          }
          lang.nextStep();
          sc.toggleHighlight(16, 17);
          lang.nextStep();
          sc.unhighlight(17);

        } else {
          sc.toggleHighlight(15, 17);
          lang.nextStep();
          sc.unhighlight(17);
        }

      }
      i.hide();
      sc.highlight(18);
      lang.nextStep();
      sc.toggleHighlight(18, 19);
      lang.nextStep();
      sc.unhighlight(19);
    } else {
      lang.nextStep();
      sc.toggleHighlight(5, 20);
      lang.nextStep();
      sc.toggleHighlight(20, 21);
      lang.nextStep();
      sc.toggleHighlight(21, 22);
      lang.nextStep();
      sc.unhighlight(22);
      bipartite = false;
      return;

    }

    sc.highlight(23);
    lang.nextStep();
    sc.unhighlight(23);

  }

  public void isBipartite() {

    rekCnt = 0;
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30), "Bipartite Graphen", "header", null,
        headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.green);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);
    lang.nextStep();
    // textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // Font.SANS_SERIF, Font.PLAIN, 16));

    text1 = lang.newText(new Coordinates(10, 100),
        "•	    Wir betrachten einen einfachen Graph G=(V,E)", "description1",
        null, textProps);
    text2 = lang
        .newText(
            new Offset(25, 25, "description1", AnimalScript.DIRECTION_NW),
            " Hierbei bezeichnet V die Menge der Knoten und E die Menge der Kanten.",
            "description2", null, textProps);

    lang.nextStep();
    text3 = lang
        .newText(
            new Offset(-25, 25, "description2", AnimalScript.DIRECTION_NW),
            "•	    G heißt bipartit, falls sich seine Knoten in zwei disjunkte Teilmengen A und B aufteilen lassen, ",
            "description3", null, textProps);
    text4 = lang
        .newText(
            new Offset(25, 25, "description3", AnimalScript.DIRECTION_NW),
            " sodass zwischen den Knoten innerhalb beider Teilmengen keine Kanten verlaufen.",
            "algo11", null, textProps);
    lang.nextStep();

    text5 = lang
        .newText(
            new Offset(-25, 25, "algo11", AnimalScript.DIRECTION_NW),
            "•	    Diese Definition ist äquivalent zu folgender Aussage: „ Man kann die Knoten  des Graphen",
            "algo12", null, textProps);
    text6 = lang
        .newText(
            new Offset(25, 25, "algo12", AnimalScript.DIRECTION_NW),
            "mit zwei Farben färben, sodass zwei Knoten, die die selbe Farbe haben, nicht über eine Kante ",
            "algo13", null, textProps);
    text7 = lang.newText(
        new Offset(0, 25, "algo13", AnimalScript.DIRECTION_NW),
        "verbunden sind.“", "algo21", null, textProps);

    lang.nextStep();
    text8 = lang
        .newText(
            new Offset(-25, 25, "algo21", AnimalScript.DIRECTION_NW),
            "•	   Auf dieser Aussage basiert der Algorithmus, mit dem getestet wird, ob ein Graph bipartit ist. ",
            "algo22", null, textProps);

    text9 = lang
        .newText(
            new Offset(25, 25, "algo22", AnimalScript.DIRECTION_NW),
            "Es wird versucht, den Graph mit zwei Farben zu färben. Gelingt es, ist der Graph bipartit. ",
            "algo31", null, textProps);

    neighboursText = lang.newText(new Coordinates(500, 400), " ", "neighbours",
        null, textProps);
    Rekursion = lang.newText(new Coordinates(500, 500), " ", "rekursion", null,
        textProps);

    String[] Neighbours = new String[0];
    Neighbours2 = lang.newStringArray(new Coordinates(630, 400), Neighbours,
        "Neighbours", null, arrayProps);
    i = lang.newArrayMarker(Neighbours2, 0, "", null, markerProps);

    lang.nextStep("Einleitung");

    text1.hide();
    text2.hide();
    text3.hide();
    text4.hide();
    text5.hide();
    text6.hide();
    text7.hide();
    text8.hide();
    text9.hide();

    // graphProps = new GraphProperties();
    // graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.GREEN);
    //
    // graphProps2 = new GraphProperties();
    // graphProps2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // graphProps2.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.RED);

    Graph g = getDefaultGraph();

    createSourceCode();
    lang.nextStep();
    sc.highlight(0);
    lang.nextStep();
    sc.toggleHighlight(0, 1);
    lang.nextStep();
    sc.highlight(4);
    lang.nextStep();
    sc.unhighlight(1);
    colour(nodes[0], 1, graphNodes, g); // hier wird bestimmt ob der Graph
                                        // bipartit ist oder nicht
    lang.nextStep();
    sc.unhighlight(1);
    lang.nextStep();
    neighboursText.hide();
    Neighbours2.hide();
    i.hide();
    if (this.bipartite) {
      ergebnis = lang.newText(new Coordinates(600, 400),
          "Dieser Graph ist bipartit.", "des1", null, textProps);
    } else {
      ergebnis = lang.newText(new Coordinates(600, 400),
          "Dieser Graph ist nicht bipartit.", "des2", null, textProps);
    }
    lang.nextStep("Ergebnis");
    ergebnis.hide();
  }

  private Graph getNewGraph(String label, Node node) {
    int[][] adjacencyMatrix2 = new int[][] { { 0 } };

    String[] labels = { label };

    graphNodes2 = new Node[] { node };

    Graph g = lang.newGraph("graph2", adjacencyMatrix2, graphNodes2, labels,
        null, graphProps2);
    return g;
  }

  private Graph getDefaultGraph() {

    // define the edges of the graph
    // int[][] adjacencyMatrix = new int[][]{{0,1,0,0,0,0,0},
    // {1,0,1,0,1,1,0},
    // {0,1,0,1,0,0,0},
    // {0,0,1,0,1,0,0},
    // {0,1,0,1,0,0,0},
    // {0,1,0,0,0,0,1},
    // {0,0,0,0,0,1,0}};
    setNodesAndEdges(adjacencyMatrix);

    // // define the nodes and their positions
    // graphNodes = new Node[7];
    // graphNodes[0] = new Coordinates(600, 200);
    // graphNodes[1] = new Coordinates(700, 200);
    // graphNodes[2] = new Coordinates(700, 300);
    // graphNodes[3] = new Coordinates(800, 300);
    // graphNodes[4] = new Coordinates(800, 200);
    // graphNodes[5] = new Coordinates(750, 100);
    // graphNodes[6] = new Coordinates(850, 100);

    graphNodes = new Node[adjacencyMatrix.length];

    int x = 600;

    int y = 100;

    for (int i = 0; i < adjacencyMatrix.length; i++) {

      graphNodes[i] = new Coordinates(x, y);

      if (i % 3 == 2) {

        y = y + 100;

        x = 600;

      } else {

        x = x + 100;

      }

    }
    String[] labels = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };

    Graph g = lang.newGraph("graph", adjacencyMatrix, graphNodes, labels, null,
        graphProps);

    return g;
  }

  public void setNodesAndEdges(int[][] adjacencyMatrix) {

    nodes = new Nodes[adjacencyMatrix.length];
    for (int i = 0; i < adjacencyMatrix.length; i++) {
      nodes[i] = new Nodes(i, 0);
    }
    Vector<BipartiteEdge> edgeVector = new Vector<BipartiteEdge>();
    for (int i = 0; i < adjacencyMatrix.length - 1; i++) {
      for (int j = i; j < adjacencyMatrix.length; j++) {
        if (adjacencyMatrix[i][j] != 0) {

          edgeVector.add(new BipartiteEdge(nodes[i], nodes[j]));
        }
      }
    }
    edges = new BipartiteEdge[edgeVector.size()];
    for (int i = 0; i < edgeVector.size(); i++) {
      edges[i] = edgeVector.get(i);
    }
  }

  public String getName() {
    return "Bipartite Graphen";
  }

  public String getAlgorithmName() {
    return "BipartiteGraph";
  }

  public String getAnimationAuthor() {
    return "Admir Agia";
  }

  public String getDescription() {
    return "Wir betrachten einen einfachen Graph G=(V,E). Hierbei bezeichnet V die Menge der Knoten und E die"
        + "\n"
        + "Menge der Kanten. G hei&szlig;t, falls sich seine Knoten in zwei disjunkte Teilmengen A und B aufteilen "
        + "\n"
        + "lassen, sodass zwischen den Knoten innerhalb beider Teilmengen keine Kanten verlaufen. Diese "
        + "\n"
        + "Definition ist &auml;quivalent zu folgender Aussage: &bdquo;Man kann die Knoten des Graphen mit"
        + "\n"
        + "zwei Farben f&auml;rben, sodass zwei Knoten, die die selbe Farbe haben, nicht über eine Kante "
        + "\n"
        + "verbunden sind.&rdquo; Auf dieser Aussage basiert der Algorithmus, mit dem getestet wird, ob ein "
        + "\n"
        + "Graph bipartit ist. Es wird versucht, den Graph mit zwei Farben zu f&auml;rben. Gelingt es,ist der Graph "
        + "\n" + "bipartit.";
  }

  public String getCodeExample() {
    return "public void isBipartite() {" + "\n" + "     colour(nodes[0], 1);"
        + "\n" + "}" + "\n" + "\n"
        + "public void colour(Node node,int colour) {" + "\n"
        + "     if(isColourable(node, colour)) {" + "\n"
        + "          node.colour=colour;" + "\n"
        + "          ArrayList<Node> neighbours = getNeighbours(node);" + "\n"
        + "          if(colour==1) {" + "\n" + "               colour=2;"
        + "\n" + "          }" + "\n" + "          else {" + "\n"
        + "               colour=1;" + "\n" + "          }" + "\n"
        + "          for(int i=0; i&szlig;neighbours.size(); i++) {" + "\n"
        + "               if(neighbours.get(i).colour==0) {" + "\n"
        + "                    colour(neighbours.get(i), colour);" + "\n"
        + "               }" + "\n" + "          }" + "\n" + "     }" + "\n"
        + "     else {" + "\n" + "          bipartite=false;" + "\n" + "     }"
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

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    int[][] matrix = (int[][]) arg1.get("adjacencyMatrix");

    if (matrix.length != matrix[0].length) {
      throw new IllegalArgumentException(
          "Achtung!!! Adjazenzmatrix muss quadratisch(nxn) sein!");
    }

    boolean symetric = true;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        if (matrix[i][j] != matrix[j][i]) {
          symetric = false;
        }
      }
    }

    boolean falscheEingabe = false;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        if (matrix[i][j] != 0 && matrix[i][j] != 1) {
          falscheEingabe = true;
        }
      }
    }

    if (symetric == false) {
      throw new IllegalArgumentException(
          "Achtung!!! Adjazenzmatrix muss symmetrisch sein!");
    }

    if (falscheEingabe == true) {
      throw new IllegalArgumentException(
          "Achtung!!! Die Einträge der Adjazenmatrix müssen 0 oder 1 sein");
    }

    if (matrix.length > 9) {
      throw new IllegalArgumentException("Achtung!!! Der Graph ist zu groß!");
    }

    return true;

  }

}