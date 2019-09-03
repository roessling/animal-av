package generators.network.routing;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.MyNode;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class DistanceVectorRouting extends AnnotatedAlgorithm implements
    ValidatingGenerator {

  // private final String author = "Christian Rosskopf";
  // private final String title = "Distance Vector Routing";
  // private final int resX = 640;
  // private final int resY = 480;

  private final int               self         = 0;
  private final int               notConnected = Integer.MAX_VALUE;
  private final String[]          names        = { "A", "B", "C", "D", "E",
      "F", "G", "H", "I", "J"                 };
  private final Coordinates[]     coordinates  = { new Coordinates(40, 50),
      new Coordinates(360, 50), new Coordinates(40, 250),
      new Coordinates(360, 250), new Coordinates(100, 150),
      new Coordinates(300, 150)               };

  private boolean                 update       = false;
  private boolean                 first        = true;
  private int                     table[][];
  private ArrayList<MyNode>       nodesList;

  private String[][]              adjacency;

  // private Language lang;

  private ArrayList<StringMatrix> grids        = new ArrayList<StringMatrix>();
  private Text                    titleText;
  private Text                    statusText;
  private Text                    calc;
  private Text                    changed;
  // private SourceCode sourceCode;
  private Graph                   graph;

  // Variables for Displayoptions
  private Color                   highlightShortest;
  private Color                   highlightNew;
  private Color                   highlightUpdated;

  private int                     numberOfNodes;
  // private int[][] adjacencyMatrix;

  private Color                   sourceCodeColor;
  private Color                   sourceCodeHighlightColor;
  private Font                    sourceCodeFont;
  private Color                   sourceCodeContextColor;
  private Boolean                 sourceCodeItalic;
  private Boolean                 sourceCodeBold;

  // private Font textFont;
  private Color                   textColor;

  private Color                   statusTextColor;
  private Font                    statusTextFont;

  private Color                   matrixNameColor;
  private Color                   matrixColor;
  private Color                   matrixElementColor;

  public DistanceVectorRouting() {

  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    System.out.println("RUN GENERATE");
    init();
    validateInput(arg0, arg1);
    // for(String s: arg1.keySet())System.out.println(s);

    // User Definied Properties
    // matrix
    matrixColor = (Color) arg0.get("matrix", "color");
    matrixElementColor = (Color) arg0.get("matrix", "elementColor");
    // titleText
    textColor = (Color) arg0.get("titleText", "color");
    // textFont = (Font) arg0.get("titleText", "font");
    // statusText
    statusTextColor = (Color) arg0.get("statusText", "color");
    statusTextFont = (Font) arg0.get("statusText", "font");
    // sourceCode
    sourceCodeBold = (Boolean) arg0.get("sourceCode", "bold");
    sourceCodeColor = (Color) arg0.get("sourceCode", "color");
    sourceCodeHighlightColor = (Color) arg0.get("sourceCode", "highlightColor");
    sourceCodeFont = (Font) arg0.get("sourceCode", "font");
    sourceCodeContextColor = (Color) arg0.get("sourceCode", "contextColor");
    sourceCodeItalic = (Boolean) arg0.get("sourceCode", "italic");
    // User definied primitives
    numberOfNodes = (Integer) arg1.get("NumberOfNodes");
    System.out.println("Using " + numberOfNodes + " nodes");
    // Read from Integer Matrix

    adjacency = (String[][]) arg1.get("stringMatrix");
    if (adjacency != null) {
      for (String[] s : adjacency) {
        for (String s1 : s)
          System.out.print(s1 + " ");
        System.out.println();
      }
      System.out.println();
    } else {
      System.out.println("IS NULL !");
    }

    highlightShortest = (Color) arg1.get("HighlightShortest");
    highlightNew = (Color) arg1.get("HighlightNew");
    highlightUpdated = (Color) arg1.get("HighlightUpdated");
    matrixNameColor = (Color) arg1.get("MatrixNameColor");

    // Reading variables finished.
    // Start Building the animation
    initMatrices();

    initTitle();
    initGraph();
    initSourceCode();
    parse();
    exec("init");
    initGrids();
    initStatusField();
    // Algorithm Initialization
    initRoutingTables();
    getDistanceVectors();
    // Algorithm starts
    calculateRoutingDistances();

    // printToFile(lang);
    return lang.getAnimationCode();
  }

  @Override
  public String getAlgorithmName() {
    return "Distance Vector Routing";
  }

  @Override
  public String getAnimationAuthor() {
    return "Christian Rosskopf";
  }

  @Override
  public String getCodeExample() {
    return "No code example but a short introduction to the properties:\n"
        + "\"Number Of Nodes\" sets the size of the graph. Only values between 1 and 6 are allowed"
        + "\n \"stringMatrix\" is used to set the adjacency matrix of the grapg. Because a undirected graph is used only the upper"
        + "\n right corner must be filled. \"-\" and \"#\" are used for non existing edges. The other fields should contain integers"
        + "\n If the size of the graph is smaller, the string matrix can be reduced, but doesn't have to.";

  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return "Animation shows Distance Vector Routing";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  @Override
  public String getName() {
    return "DistanceVectorRouting with Annotations";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    System.out.println("RUN INIT");
    super.init();
    // lang = new AnimalScript(title,author,resX,resY);
    // lang.setStepMode(true);

  }

  private void initMatrices() {
    // change to int[][] primitive
    // Need to translate the adjacency matrix: int[][] primitive didn't work
    table = translate(adjacency);
    System.out.println("RT-Size: " + table.length);

    // Create nodes with the adjacency matrix
    nodesList = new ArrayList<MyNode>();
    for (int i = 0; i < numberOfNodes; i++) {
      MyNode n = new MyNode(names[i], i, table, highlightUpdated,
          highlightShortest);
      nodesList.add(n);
      print(n.getRoutingTable(), n.getName());
    }

    // Give every node some neighbour information
    MyNode[] nodes = new MyNode[nodesList.size()];
    nodes = nodesList.toArray(nodes);
    for (MyNode n : nodesList)
      n.initNeighbours(nodes);
  }

  /**
   * Creates the titlefield
   */
  private void initTitle() {
    TextProperties prop = new TextProperties();
    prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED,
        Font.BOLD, 16));
    prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);
    titleText = lang.newText(new Coordinates(20, 20),
        "Distance-Vector-Routing", "title", null, prop);
  }

  /**
   * Creates the status field
   */
  private void initStatusField() {
    TextProperties prop = new TextProperties();

    prop.set(AnimationPropertiesKeys.FONT_PROPERTY, statusTextFont);
    prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, statusTextColor);

    statusText = lang.newText(new Coordinates(20, 700), "", "status", null,
        prop);
    calc = lang.newText(
        new Offset(0, 10, statusText, AnimalScript.DIRECTION_SW), "", "calc",
        null, prop);
    changed = lang.newText(new Offset(0, 10, calc, AnimalScript.DIRECTION_SW),
        "", "changed", null, prop);
  }

  /**
   * Creates the displayed sourcecode
   */
  private void initSourceCode() {
    SourceCodeProperties sourceProps = new SourceCodeProperties();
    sourceProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sourceCodeColor);
    sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sourceCodeFont);
    sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        sourceCodeHighlightColor);
    sourceProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        sourceCodeContextColor);
    sourceProps.set(AnimationPropertiesKeys.BOLD_PROPERTY, sourceCodeBold);
    sourceProps.set(AnimationPropertiesKeys.ITALIC_PROPERTY, sourceCodeItalic);
    sourceCode = lang.newSourceCode(new Offset(0, 20, graph,
        AnimalScript.DIRECTION_SW), "source", null, sourceProps);

  }

  /**
   * Creates the graphdisplay
   */
  private void initGraph() {
    GraphProperties gp = new GraphProperties();
    gp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
    gp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    gp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.MAGENTA);
    gp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    gp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

    // use hardcoded positions
    Node[] graphNodes = new Node[numberOfNodes];
    for (int i = 0; i < numberOfNodes; i++) {
      graphNodes[i] = coordinates[i];
    }

    // get node names
    String[] labels = createLabels(numberOfNodes);
    int[][] graphAdjacencyMatrix = new int[numberOfNodes][numberOfNodes];
    for (int i = 0; i < graphAdjacencyMatrix.length; i++) {
      for (int j = 0; j < graphAdjacencyMatrix[i].length; j++) {
        if (table[i][j] != self && table[i][j] != notConnected)
          graphAdjacencyMatrix[i][j] = table[i][j];
        else
          graphAdjacencyMatrix[i][j] = 0;
      }
    }

    graph = lang.newGraph("Graph", graphAdjacencyMatrix, graphNodes, labels,
        null, gp);
  }

  /**
   * Creates the displayed grids/matrixes
   */
  private void initGrids() {
    StringMatrix m1 = null;

    MatrixProperties mp = new MatrixProperties();
    // userdefinied properties
    mp.set(AnimationPropertiesKeys.COLOR_PROPERTY, matrixColor);
    mp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, matrixElementColor);
    // hardcodedproperties
    mp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    mp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.YELLOW);
    mp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
    mp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    int node_index = 0;
    int k = 0;
    int l = 0;
    // create and calculate gridposition.
    for (int m = 0; m < numberOfNodes; m++) {
      String[][] stringValues = convertToString(
          emptyRoutingTableInitialization(node_index), names[m]);
      node_index++;
      l++;
      if (node_index == 4) {
        k = 1;
        l = 1;
      }
      if (first) {
        m1 = lang
            .newStringMatrix(new Offset(200, (l - 1) * 40 * numberOfNodes,
                titleText, AnimalScript.DIRECTION_NE), stringValues, "g1",
                null, mp);
        first = false;
        grids.add(m1);
      } else {
        m1 = lang.newStringMatrix(
            new Offset(200 + (k * 60 * numberOfNodes), (l - 1) * 40
                * numberOfNodes, titleText, AnimalScript.DIRECTION_NE),
            stringValues, "g1", null, mp);
        // int i = grids.size();
        grids.add(m1);
      }
      lang.addLine("setGridFont \"" + m1.getName() + "[0][0]\" "
          + "font Monospaced size 18 bold");
      lang.addLine("setGridColor \"" + m1.getName() + "[0][0]\" "
          + getColorString(matrixNameColor));
    }
    lang.nextStep();

  }

  private void initRoutingTables() {
    // exec("init");
    // lang.nextStep();

    exec("initRT");
    sourceCode.highlight(0, 0, true);
    for (int node = 0; node < numberOfNodes; node++) {
      MyNode n = nodesList.get(node);
      StringMatrix m = grids.get(node);
      graph.highlightNode(node, null, null);
      statusText.setText("Initialize routingtable for Node " + n.getName(),
          null, null);
      int[][] rt = n.getRoutingTable();
      for (int i = 0; i < rt.length; i++) {
        for (int j = 0; j < rt.length; j++) {
          if (rt[i][j] != notConnected && rt[i][j] != self) {
            m.put(i + 1, j + 1, String.valueOf(rt[i][j]), null, null);
            lang.addLine("setGridColor \"" + m.getName() + "[" + (i + 1) + "]["
                + (j + 1) + "]\" " + getColorString(highlightNew));
          }
        }
      }
      lang.nextStep();
      graph.unhighlightNode(node, null, null);
    }

    statusText.setText("Get Distance Vector", null, null);

  }

  /**
   * Creates the animantion of calculating the routing distance
   */
  private void calculateRoutingDistances() {
    exec("start");
    lang.nextStep();

    exec("setBool");
    vars.setGlobal("changed");
    update = Boolean.valueOf(vars.get("changed"));
    sourceCode.highlight(4, 0, true);

    statusText.setText("Initialize boolean changed with true", null, null);
    changed.setText("changed = true", null, null);
    changed.changeColor(null, Color.GREEN, null, null);
    lang.nextStep();
    vars.declare("int", "Round", "0");

    // Enter the loop. as long as at least one node has a new shortest way send
    // distance vectors and update routing table
    while (update) {
      int round = 0;// ///////////////
      statusText.setText("changed == true", null, null);
      exec("while");
      lang.nextStep();

      exec("resetBool");
      sourceCode.highlight(6, 0, true);
      sourceCode.highlight(18, 0, true);
      update = Boolean.valueOf(vars.get("changed"));
      statusText.setText("Set changed to false", null, null);
      changed.setText("changed = false", null, null);
      changed.changeColor(null, Color.RED, null, null);
      lang.nextStep();

      // Simulate sending of distance vector to neighbors
      int counter = 0;
      exec("sendDV");
      for (MyNode n : nodesList) {
        animateSending(n, n.getNeighbours());

      }

      // Update own routing table
      for (MyNode n : nodesList) {
        // Proccess Neighbour Distance Vectors
        statusText.setText("Update Routing Table of Node " + n.getName(), null,
            null);
        exec("for");
        vars.set("currentNode", n.getName());
        graph.highlightNode(n.getIndex(), null, null);
        lang.nextStep();

        exec("updateRT");
        sourceCode.highlight(9, 0, true);
        sourceCode.highlight(17, 0, true);
        vars.set("OldDistanceVector", n.getDistanceVectorAsText());
        n.processNeighbourInfo(lang, grids.get(counter), statusText, calc, vars);
        vars.discard("ThisRoundRoutingTableUpdates");

        // Update own DistanceVector
        unhghilightGrid(grids.get(counter));
        exec("updateDV");
        boolean test = n.createUpdatedDistanceVector(lang, statusText, calc,
            grids.get(n.getIndex()).getName());
        vars.set("NewDistanceVector", n.getNewDistanceVectorAsText());
        CheckpointUtils.checkpointEvent(this, "NewDistanceVector",
            new Variable("distance", n.getNewDistanceVectorAsText()),
            new Variable("currentNode", n.getName()));// /////////////////////////

        lang.nextStep();

        exec("if");
        lang.nextStep();
        // has distance vector changed?
        if (test) {
          exec("setTrue");
          sourceCode.highlight(12, 0, true);
          update = Boolean.valueOf(vars.get("changed"));
          statusText.setText(
              "Distance Vector changed to " + n.getNewDistanceVectorAsText(),
              null, null);
          CheckpointUtils.checkpointEvent(this, "change", new Variable(
              "distance", n.getNewDistanceVectorAsText()));// ////////////////////
          changed.setText("changed = true", null, null);
          changed.changeColor(null, Color.GREEN, null, null);
        } else {
          exec("skip");
          sourceCode.highlight(12, 0, true);
          sourceCode.highlight(14, 0, true);
          sourceCode.highlight(16, 0, true);
          statusText.setText(
              "Distance Vector is still " + n.getNewDistanceVectorAsText(),
              null, null);
          CheckpointUtils.checkpointEvent(this, "noChange", new Variable(
              "distance", n.getNewDistanceVectorAsText()));
        }
        lang.nextStep();

        sourceCode.unhighlight(12);
        sourceCode.unhighlight(14);
        sourceCode.unhighlight(16);

        sourceCode.unhighlight(13);
        sourceCode.unhighlight(15);

        statusText.setText("", null, null);
        graph.unhighlightNode(n.getIndex(), null, null);
        counter++;
      }
      round++;
      CheckpointUtils.checkpointEvent(this, "Round", new Variable("round",
          round));
      vars.discard("currentNode");
      sourceCode.unhighlight(17);
      sourceCode.unhighlight(18);
      sourceCode.unhighlight(9);
      // Switch to new distance vector
      for (MyNode n : nodesList)
        n.switchDistanceVector();
      // unhighlight all
      unhighlightAllGrids();

    }

    exec("end");
    sourceCode.unhighlight(4);
    sourceCode.unhighlight(6);
    statusText.setText("All Routes Calculated", null, null);
    changed.setText("", null, null);
    highlightShortestRoutes();

  }

  /**
   * Creates animation for displaying the DistanceVector of a node
   */
  private void getDistanceVectors() {
    exec("initDV");
    unhighlightAllGrids();
    for (int i = 0; i < numberOfNodes; i++) {
      MyNode n = nodesList.get(i);
      graph.highlightNode(i, null, null);
      statusText.setText("Distance Vector for Node " + n.getName() + " is "
          + ArrayToText(n.getDistanceVector()), null, null);
      lang.nextStep();
      graph.unhighlightNode(i, null, null);
    }
    statusText.setText("", null, null);
    sourceCode.unhighlight(0);
  }

  /**
   * Creates a animation of sending distance vectors
   * 
   * @param n
   * @param arrayList
   */
  private void animateSending(MyNode n, ArrayList<MyNode> arrayList) {
    graph.highlightNode(n.getIndex(), null, null);
    String targets = "";
    for (MyNode m : arrayList) {
      if (n.getIndex() < m.getIndex())
        graph.highlightEdge(n.getIndex(), m.getIndex(), null, null);
      else
        graph.highlightEdge(m.getIndex(), n.getIndex(), null, null);
      targets += m.getName() + ", ";
    }
    if (targets.length() > 2)
      targets = targets.substring(0, targets.length() - 2);
    statusText.setText("Node " + n.getName() + " sends Distance Vector "
        + ArrayToText(n.getDistanceVector()) + " to " + targets, null, null);

    lang.nextStep();
    // unhighlight
    graph.unhighlightNode(n.getIndex(), null, null);
    for (MyNode m : arrayList) {
      if (n.getIndex() < m.getIndex())
        graph.unhighlightEdge(n.getIndex(), m.getIndex(), null, null);
      else
        graph.unhighlightEdge(m.getIndex(), n.getIndex(), null, null);
    }
    statusText.setText("", null, null);
  }

  /**
   * Display the shortest way at end of the animation
   */
  private void highlightShortestRoutes() {
    for (MyNode n : nodesList) {
      int[][] rt = n.getRoutingTable();
      int highlightIndex = 0;
      int smallest;
      for (int i = 0; i < rt.length; i++) {
        smallest = Integer.MAX_VALUE;
        for (int j = 0; j < rt[i].length; j++) {
          if (rt[i][j] < smallest) {
            smallest = rt[i][j];
            highlightIndex = j;
          }
        }
        if (i != n.getIndex())
          lang.addLine("setGridColor \"" + grids.get(n.getIndex()).getName()
              + "[" + (i + 1) + "][" + (highlightIndex + 1) + "]\" "
              + getColorString(highlightShortest));
      }
    }

  }

  private void unhighlightAllGrids() {
    lang.nextStep();
    for (StringMatrix s : grids) {
      lang.addLine("setGridColor \"" + s.getName() + "[][]\" "
          + getColorString(matrixColor));
      lang.addLine("setGridColor \"" + s.getName() + "[0][0]\" "
          + getColorString(matrixNameColor));
    }
    lang.nextStep();
  }

  private void unhghilightGrid(StringMatrix m) {
    lang.addLine("setGridColor \"" + m.getName() + "[][]\" "
        + getColorString(matrixColor));
    lang.addLine("setGridColor \"" + m.getName() + "[0][0]\" "
        + getColorString(matrixNameColor));
  }

  private String[] createLabels(int size) {
    String[] labels = new String[size];
    for (int i = 0; i < size; i++)
      labels[i] = names[i];
    return labels;
  }

  /**
   * Transforms a distanceVector to a String for output
   * 
   * @param distanceVector
   *          the distanceVector
   * @return the vector as text
   */
  private String ArrayToText(int[] distanceVector) {
    String s = "[";
    for (int i : distanceVector) {
      switch (i) {
        case self:
          s += "#, ";
          break;
        case notConnected:
          s += "#, ";
          break;
        default:
          s += i + ", ";
      }
    }
    s = s.substring(0, s.length() - 2);
    s += "]";
    return s;
  }

  /**
   * Initialises the empty routing table of a node. the own fields and fields of
   * non-neighbors are set to # other fields are empty
   * 
   * @param node_index
   * @return
   */
  private String[][] emptyRoutingTableInitialization(int node_index) {
    String[][] table = new String[numberOfNodes][numberOfNodes];
    for (int i = 0; i < numberOfNodes; i++) {
      for (int j = 0; j < numberOfNodes; j++) {
        table[i][j] = "#";
      }
    }

    for (MyNode n : nodesList.get(node_index).getNeighbours()) {
      int column = n.getIndex();
      for (int i = 0; i < table.length; i++)
        if (i != node_index)
          table[i][column] = " ";
    }
    return table;
  }

  /**
   * Converts a routingtable to the displayed routing table with information
   * about the route
   * 
   * @param values
   * @param name
   * @return
   */
  private String[][] convertToString(String[][] values, String name) {
    String[][] s = new String[values.length + 1][values.length + 1];

    for (int i = 0; i < values.length; i++) {
      s[0][i + 1] = "via " + names[i];
      s[i + 1][0] = "to " + names[i] + " ";
    }
    for (int i = 0; i < values.length; i++) {
      for (int j = 0; j < values[i].length; j++) {
        s[i + 1][j + 1] = values[i][j];
      }
    }
    s[0][0] = name;
    return s;
  }

  /**
   * Translates String[][] to integer[][] and sets "-" and "#" to not connected
   * values
   * 
   * @param adjacency
   * @return
   */
  private int[][] translate(String[][] adjacency) {
    System.out.println(numberOfNodes);
    System.out.println(adjacency.length);
    int[][] matrix = new int[numberOfNodes][numberOfNodes];
    for (int i = 0; i < numberOfNodes; i++) {
      for (int j = i; j < numberOfNodes; j++) {
        if (adjacency[i][j].equals("-") || adjacency[i][j].equals("#")) {
          matrix[i][j] = notConnected;
          matrix[j][i] = notConnected;
        } else {
          matrix[i][j] = Integer.valueOf(adjacency[i][j]);
          matrix[j][i] = Integer.valueOf(adjacency[i][j]);
        }
      }
    }
    print(matrix, "adj");
    return matrix;
  }

  private String getColorString(Color c) {
    return "color (" + c.getRed() + " , " + c.getGreen() + " , " + c.getBlue()
        + ")";
  }

  // private void printInit() {
  // for(MyNode n : nodesList) {
  // print(n.getRoutingTable(), n.getName());
  // }
  //
  // for(MyNode n : nodesList) {
  // printDV(n.getDistanceVector(),n.getName());
  // }
  // }

  // private void printDV(int[] a_dv2, String string) {
  // System.out.println(string + " sends:");
  // for (int x : a_dv2) {
  // if (x == self) {
  // System.out.print("X" + " |");
  // } else if (x == notConnected) {
  // System.out.print("-" + " |");
  // } else {
  // System.out.print(x + " |");
  // }
  // }
  // System.out.println();
  //
  // }

  private void print(int[][] rt, String name) {
    System.out.println("RT of :" + name);
    System.out.print("  |");
    for (int i = 0; i < rt.length; i++)
      System.out.print(" " + names[i] + " |");
    System.out.println();
    for (int i = 0; i <= rt.length; i++)
      System.out.print("----");
    System.out.println();
    for (int i = 0; i < rt.length; i++) {
      int[] x = rt[i];
      System.out.print(names[i] + " |");
      for (int y : x) {

        if (y == self)
          System.out.print(" X |");
        else if (y == notConnected)
          System.out.print(" - |");
        else
          System.out.print(" " + y + " |");
      }
      System.out.println();
    }
    System.out.println();
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    System.out.println("Checking Values");
    // for (AnimationProperties p : props) {
    // // do nothing
    // }

    int number = (Integer) primitives.get("NumberOfNodes");
    if (number < 1 || number > 6)
      createDialog("Expected between 1 and 6 nodes! Received " + number
          + " nodes!");

    adjacency = (String[][]) primitives.get("stringMatrix");
    if (adjacency == null)
      createDialog("Adjacency Matrix is null!");
    int length = adjacency.length;
    if (length < number)
      createDialog("Adjacency matrix is to small: Expecting size " + number
          + " but received " + length);
    for (String[] s : adjacency)
      if (s.length != length)
        createDialog("Matrix is not quadratic! Size should be " + length + "x"
            + length);

    for (int i = 0; i < adjacency.length; i++) {
      for (int j = i; j < adjacency[i].length; j++) {
        if (!adjacency[i][j].equals("#") && !adjacency[i][j].equals("-")) {
          try {
            if (Integer.valueOf(adjacency[i][j]) <= 0)
              createDialog("Illegal Value \"" + adjacency[i][j]
                  + "\" at field [" + i + "][" + j
                  + "]! Expecting positive values!");
          } catch (NumberFormatException e) {
            createDialog("Illegal Value \"" + adjacency[i][j] + "\" at field ["
                + i + "][" + j + "]! Only numbers, # and - are allowed!");
          }
        }
      }
    }
    return true;
  }

  /**
   * Displays the error message as JDialog and throws IllegalArgumentException
   * 
   * @param text
   */
  private void createDialog(String text) {
    JOptionPane.showMessageDialog(null, text, "Error",
        JOptionPane.ERROR_MESSAGE);
    throw new IllegalArgumentException(text);
  }

  @Override
  public String getAnnotatedSrc() {
    return "Initialization 											@label(\"init\")\n"
        + "  Init Routingtables; 										@label(\"initRT\")\n"
        + "  Init Distance Vector; 									@label(\"initDV\")\n"
        + "\n"
        +

        "Start Algorithm											@label(\"start\")  @declare(\"int\", \"UpdatesInRoutingTables\", \"0\")  @declare(\"int\", \"UpdatedDistanceVectors\", \"0\")\n"
        + "  boolean changed = true 									@label(\"setBool\") @declare(\"String\", \"changed\", \"true\")  \n"
        + "  while (changed) { 										@label(\"while\") @inc(\"Round\") @highlight(\"endWhile\") \n"
        + "    changed = false; 										@label(\"resetBool\") @set(\"changed\", \"false\")\n"
        + "    Every node sends Distance Vector to its Neighbours; 	@label(\"sendDV\")\n"
        + "    for (Node n : Graph) { 								@label(\"for\") @declare(\"String\", \"currentNode\") @highlight(\"endFor\")\n"
        + "      Update RoutingTable with received Distance Vectors;	@label(\"updateRT\") @declare(\"String\", \"OldDistanceVector\") \n"
        + "      Update Distance Vector; 								@label(\"updateDV\") @declare(\"String\", \"NewDistanceVector\") \n"
        + "      if (distanceVector.hasChanged()) { 					@label(\"if\")\n"
        + "        changed = true; 									@label(\"setTrue\") @inc(\"UpdatedDistanceVectors\") @set(\"changed\", \"true\") @discard(\"OldDistanceVector\") @discard(\"NewDistanceVector\") \n"
        + "      } else { 											@label(\"else\") @highlight(\"endElse\")\n"
        + "        skip; 												@label(\"skip\") @discard(\"OldDistanceVector\") @discard(\"NewDistanceVector\") \n"
        + "      } 													@label(\"endElse\")\n"
        + "    } 														@label(\"endFor\")\n"
        + "  } 														@label(\"endWhile\")\n"
        + "Finished: Display Shortest Routes 							@label(\"end\")\n";
  }

}
