package generators.graph.warshall;

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

public class WarshallMTBE implements Generator {

  Language                            lang;
  Node                                node;
  MatrixProperties                    matrixprops;
  RectProperties                      rectprops;
  GraphProperties                     graphprops;
  CircleProperties                    circleprops;
  PolylineProperties                  arcprops;
  TextProperties                      textprops;
  IntMatrix                           matrix;
  String                              name;
  int[][]                             graphAdjacencyMatrix;
  static Node[]                       graphNodes;
  static String[]                     labels;
  DisplayOptions                      display;
  public SourceCode                   sc;                                                                                           // to
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

  private static final String         DESCRIPTION = "Takes a given graph in form of an adjacencymatrix and calculates"
                                                      + " the corresponding reachability graph. In an outer loop every node"
                                                      + "is considered as a transitnode. Then in a first inner loop each"
                                                      + " possible source node is used to add all paths which go trough the transitnode"
                                                      + " to another node (which is done in the inner most loop). When all"
                                                      + " possible transitnodes are processed the reachability graph is completed.";

  public void showGraph(Graph graph) {
    graphNodes = new Node[graph.getSize()];
    labels = new String[graph.getSize()];
    for (int i = 0; i < graph.getSize(); i++) {
      graphNodes[i] = graph.getNode(i);
      labels[i] = graph.getNodeLabel(i);
    }
    graphprops = (GraphProperties) animationProperties
        .getPropertiesByName("graph");
    gr = lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(), graphNodes,
        labels, graph.getDisplayOptions(), graphprops);

  }

  public void WarshallAlg(Graph graph) {

    Text text5 = lang.newText(new Coordinates(50, 260), "", "text", display,
        textprops);
    Text text4 = lang.newText(new Coordinates(180, 100), "", "text", display,
        textprops);
    Text text = lang.newText(new Coordinates(180, 130), "", "text", display,
        textprops);
    Text text2 = lang.newText(new Coordinates(180, 160), "", "text", display,
        textprops);
    Text text3 = lang.newText(new Coordinates(180, 190), "", "text", display,
        textprops);
    lang.newRect(new Coordinates(500, 300), new Coordinates(40, 80), "matrix",
        display, rectprops);
    IntMatrix matrix = lang.newIntMatrix(new Coordinates(60, 100),
        graph.getAdjacencyMatrix(), "matrix", null, matrixprops);
    showGraph(graph);
    TicksTiming timing = new TicksTiming(30);

    int y = 0, x = 0, j = 0, temp = 0;
    showSourceCode();
    lang.nextStep();
    sc.highlight(0);
    lang.nextStep();
    sc.toggleHighlight(0, 1);
    lang.nextStep();
    sc.toggleHighlight(1, 2);
    text4.setText("X= " + x + "    Y= " + y + "     J= " + j, null, timing);
    lang.nextStep();
    sc.toggleHighlight(2, 3);
    // Anfang for(1)
    for (y = 0; y < matrix.getNrRows(); y++) {
      sc.highlight(3);
      x = 0;
      j = 0;
      text4.setText("X= " + x + "    Y= " + y + "     J= " + j, null, timing);
      lang.nextStep();
      // Anfang for (2)
      for (x = 0; x < (matrix.getNrCols()); x++) {
        j = 0;
        sc.unhighlight(3);
        sc.highlight(4);
        text4.setText("X= " + x + "    Y= " + y + "     J= " + j, null, timing);
        lang.nextStep();

        if (matrix.getElement(x, y) > 0) {
          gr.highlightNode(x, null, null);
          gr.highlightNode(y, null, null);
          sc.unhighlight(4);
          sc.highlight(5);
          text.setText(
              "A(x,y)=A(" + x + "," + y + ")=" + matrix.getElement(x, y),
              timing, timing);
          text5.setText("A(x,y)=A(" + x + "," + y
              + ") ist groesser als null = " + matrix.getElement(x, y), timing,
              timing);
          matrix.highlightCell(x, y, null, timing);
          lang.nextStep();
          text5.setText("", null, null);

          for (j = 0; j < matrix.getNrCols(); j++) {
            sc.unhighlight(5);
            sc.highlight(6);
            text4.setText("X= " + x + "    Y= " + y + "     J= " + j, null,
                timing);
            lang.nextStep();
            sc.toggleHighlight(6, 7);

            if (matrix.getElement(y, j) > 0) {
              gr.highlightNode(j, null, null);
              sc.highlight(7);
              matrix.highlightCell(y, j, null, timing);
              text2.setText(
                  "A(y,j)=A(" + y + "," + j + ")=" + matrix.getElement(y, j),
                  null, timing);
              text5.setText("A(y,j)=A(" + y + "," + j
                  + ") ist groesser als null = " + matrix.getElement(y, j),
                  null, timing);
              lang.nextStep();
              text5.setText("", null, null);
              sc.toggleHighlight(7, 8);
              matrix.unhighlightCell(x, y, timing, timing);
              matrix.unhighlightCell(y, j, timing, timing);
              matrix.highlightCell(x, j, timing, timing);

              if ((matrix.getElement(x, j) == 0)
                  || ((matrix.getElement(x, y) + matrix.getElement(y, j)) < matrix
                      .getElement(x, j))) {
                sc.highlight(8);
                sc.unhighlight(7);
                temp = matrix.getElement(x, y) + matrix.getElement(y, j);
                text3.setText(
                    "A(x,j)=A(" + x + "," + j + ")=" + matrix.getElement(x, j),
                    null, timing);
                lang.nextStep();
                sc.toggleHighlight(8, 9);
                // matrix.highlightCell(x, j,null, timing);
                matrix.put(x, j, temp, null, timing);
                gr.setEdgeWeight(x, j, matrix.getElement(x, j), null, null);
                gr.getEdgeWeight(x, j);
                gr.highlightEdge(x, j, null, null);
                matrix.highlightElem(x, j, timing, timing);
                text3.setText(
                    "A(x,j)=A(" + x + "," + j + ")=" + matrix.getElement(x, j),
                    null, timing);
                text5.setText("A(x,j)=A(" + x + "," + j + ")= A(" + x + "," + y
                    + ") +A(" + y + "," + j + ") = " + matrix.getElement(x, j),
                    null, timing);
                lang.nextStep();
                gr.unhighlightNode(x, null, null);
                gr.unhighlightNode(y, null, null);
                gr.unhighlightNode(j, null, null);
                gr.unhighlightEdge(x, j, null, null);
                text3.setText("", null, null);
                text5.setText("", null, null);
                matrix.unhighlightCell(x, j, null, timing);
                sc.toggleHighlight(9, 6);

              } // end if (3)
              else {
                sc.highlight(8);
                sc.unhighlight(7);
                text3.setText(
                    "A(x,j)=A(" + x + "," + j + ")=" + matrix.getElement(x, j),
                    null, timing);
                text5.setText("A(x,j)=A(" + x + "," + j
                    + ") ist nicht gleich null = " + matrix.getElement(x, j),
                    null, timing);
                lang.nextStep();
                matrix.unhighlightCell(x, j, null, timing);
                text3.setText("", null, null);
                text5.setText("", null, null);
                sc.toggleHighlight(8, 6);

              } // end else (3)
              text2.setText("", null, null);
            }// end if (2)

            else {
              sc.unhighlight(6);
              sc.highlight(7);
              matrix.highlightCell(y, j, null, timing);
              text2.setText(
                  "A(y,j)=A(" + y + "," + j + ")=" + matrix.getElement(y, j),
                  null, timing);
              text5.setText(
                  "A(y,j)=A(" + y + "," + j
                      + ") ist nicht groesser als null = "
                      + matrix.getElement(y, j), null, timing);
              lang.nextStep();
              matrix.unhighlightCell(y, j, null, timing);
              text2.setText("", null, null);
              text5.setText("", null, null);
              sc.toggleHighlight(7, 6);
            } // end else (2)

          }// end for (3)

          text.setText("", null, null);
        }// end if (1)

        else {
          sc.unhighlight(4);
          sc.highlight(5);
          matrix.highlightCell(x, y, timing, timing);
          text.setText(
              "A(x,y)=A(" + x + "," + y + ")=" + matrix.getElement(x, y),
              timing, timing);
          text5.setText("A(x,y)=A(" + x + "," + y
              + ") nicht groesser als null = " + matrix.getElement(x, y),
              timing, timing);
          lang.nextStep();
          matrix.unhighlightCell(x, y, timing, timing);
          text5.setText("", null, null);
          text.setText("", null, null);
          sc.toggleHighlight(5, 4);
        }// end else (1)
        sc.toggleHighlight(6, 4);
        lang.nextStep();
      }// end for (2)
      sc.toggleHighlight(4, 3);
      lang.nextStep();
    } // end for (1)

  }

  public void init() { // initialize the main elements
    // Generate a new Language instance for content creation
    // Parameter: Animation title, author, width, height
    lang = new AnimalScript("Warshall Animation", "Madieha + Bouchra", 620, 480);
    // Activate step control
    lang.setStepMode(true);
    // create array properties with default values
    matrixprops = new MatrixProperties();
    textprops = new TextProperties();
    rectprops = new RectProperties();
    arcprops = new PolylineProperties();
    circleprops = new CircleProperties();

    circleprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    circleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

    arcprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);

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

    rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);

  }

  public void showSourceCode() {
    // first, set the visual properties for the source code
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.CYAN);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 16));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // now, create the source code entity
    sc = lang.newSourceCode(new Coordinates(40, 300), "sourceCode", null,
        scProps);
    // add a code line
    // parameters: code it self; name(can be null); indentation level; display
    // options
    sc.addCodeLine("public void Warshall(int[][] a) {", null, 0, null);
    sc.addCodeLine("int N=a.length;", null, 1, null);
    sc.addCodeLine("int y=0, x=0,j=0;", null, 1, null);
    sc.addCodeLine("for (y= 0; y< N; y++) ", null, 1, null);
    sc.addCodeLine("for (x=0 ; x< N; x++)", null, 2, null);
    sc.addCodeLine("if  (a[x][y]>0]", null, 3, null);
    sc.addCodeLine("for (j=0; j<N ;j++)", null, 4, null);
    sc.addCodeLine("if (a[y][j]>0)", null, 5, null);
    sc.addCodeLine("if ((a[x][j]==0 || (a[x][y]+a[y][j]<a[x][j]))", null, 5,
        null);
    sc.addCodeLine("a[x][j]= a[x][y]+a[y][j];", null, 6, null);
    sc.addCodeLine("}", null, 0, null);
  }

  public String getCodeExample() {
    return "Animates Warshall with Source Code + Highlighting"; // to give
                                                                // readers an
                                                                // impression
  }

  public Locale getContentLocale() {
    return Locale.US; // US-English
  }

  public String getDescription() {
    return DESCRIPTION; // description
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
    return "Warshall"; // the title to be displayed
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
      String Element = (String) iter.nextElement();
      if (Element.contains("graph"))
        gr = (Graph) primitives.get(Element);

    }

    animationProperties = props;
    WarshallAlg(gr);
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {

    return "Warshall Algorithm";
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