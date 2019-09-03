package generators.graph.nearestneighbor;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class NearestNeighborAlgorithm implements Generator {
  private Language        lang;
  private TextProperties  text;
  private GraphProperties gprops;
  private int[][]         adjMatrix;

  public NearestNeighborAlgorithm() {
    lang = new AnimalScript("Nearest Neighbor Algorithm",
        "Ahmed Charfi , Jihed Ouni", 800, 600);
    // lang.setStepMode(true);

  }

  public void init() {
    lang = new AnimalScript("Nearest Neighbor Algorithm",
        "Ahmed Charfi , Jihed Ouni", 800, 600);
    // lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    lang.setStepMode(true);

    text = (TextProperties) props.getPropertiesByName("text");
    gprops = (GraphProperties) props.getPropertiesByName("graph");

    // Iterator<String> it = gprops.getAllPropertyNames().iterator();

    // String propName;

    if (text == null)
      text = getDefaultTextProperties();
    if (gprops == null)
      gprops = getDefaultGraphProperties();

    text.set("font", new Font("SansSerif", Font.PLAIN, 16));
    gprops.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

    adjMatrix = (int[][]) primitives.get("adjMatrix");

    if (adjMatrix == null) {

      adjMatrix = getDefaultAdjMatrix(1);
    }

    start();
    findMinTour(props, primitives);

    return lang.toString();
  }

  private void start() {
    lang.setStepMode(true);

    SourceCodeProperties scTitle = new SourceCodeProperties();
    scTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, text.get("font"));
    // new Font( "Monospaced", Font.PLAIN, 18));

    scTitle.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    SourceCode sc = lang.newSourceCode(new Coordinates(80, 30), "title", null,
        scTitle);

    SourceCodeProperties scPresen1 = new SourceCodeProperties();
    scPresen1.set(AnimationPropertiesKeys.FONT_PROPERTY, text.get("font"));

    scPresen1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scPresen1.set(AnimationPropertiesKeys.COLOR_PROPERTY, text.get("color"));
    SourceCode sc1 = lang.newSourceCode(new Coordinates(90, 120),
        "Presnetation1", null, scPresen1);

    sc.addCodeLine("Nearest Neighbour Algorithm:", null, 0, null);
    sc1.addCodeLine("", null, 0, null);

    sc1.addCodeLine(
        "The nearest neighbour algorithm was one of the first algorithms used to determine",
        null, 0, null);
    sc1.addCodeLine("a solution to the travelling salesman problem.", null, 0,
        null);
    sc1.addCodeLine("", null, 0, null);
    sc1.addCodeLine(
        "the salesman starts at a random city and repeatedly visits the nearest city ",
        null, 0, null);
    sc1.addCodeLine("until all have been visited. ", null, 0, null); // 0
    sc1.addCodeLine("", null, 0, null);

    sc1.addCodeLine(
        "When the algorithm ends sucessfully it finds a short tour, but usually not the optimal one. ",
        null, 0, null); // 0

    sc1.addCodeLine("The algorithm fails in some cases.", null, 0, null); // 0

    lang.nextStep("1.Introduction");

    sc1.hide();
    // lang.nextStep();
    // ///////////////////////////////////////////////////////////////////////

    SourceCode sc2 = lang.newSourceCode(new Coordinates(90, 120), "sourceCode",
        null, scPresen1);

    sc2.addCodeLine("Pseudo Code: 		", null, 0, null);

    sc2.addCodeLine("These are the steps of the algorithm:", null, 0, null);
    sc2.addCodeLine(" ", null, 0, null);
    sc2.addCodeLine(
        "1.From the starting vertex , choose the edge with the smallest cost and use ",
        null, 0, null);

    sc2.addCodeLine("that as the first edge in your circuit.", null, 2, null);
    sc2.addCodeLine(
        "2.Continue in this manner , choosing among the edges that coonect from the current ",
        null, 0, null); // 0
    sc2.addCodeLine("vertex to vertices you have not yet visited", null, 2,
        null); // 0
    sc2.addCodeLine(
        "3.When you have visited evry vertex , return to the starting vertex. ",
        null, 0, null); //

    lang.nextStep("2.PseudoCode");

    sc2.hide();

  }

  public String getName() {
    return "Nearest Neighbor Algorithm";
  }

  public String getAlgorithmName() {
    return "Nearest Neighbor Algorithm";
  }

  public String getAnimationAuthor() {
    return "Ahmed Charfi, Jihed Ouni";
  }

  public String getDescription() {
    return "The nearest neighbour algorithm was one of the first algorithms used to determine a solution "
        + "\n"
        + "to the travelling salesman problem. "
        + "\n"
        + "\n"
        + "In it, the salesman starts at a random city and repeatedly visits the nearest city"
        + "\n"
        + " until all have been visited. The algorithm may fail in some cases. When it ends sucessfully it finds a short tour, but usually not the optimal one."
        + "\n";
  }

  public String getCodeExample() {
    return "These are the steps of the algorithm:"
        + "\n"
        + "\n"
        + "1. stand on an arbitrary vertex as current vertex."
        + "\n"
        + "2. find out the shortest edge connecting current vertex and an unvisited vertex V."
        + "\n" + "3. set current vertex to V." + "\n" + "4. mark V as visited."
        + "\n" + "5. if all the vertices in dof are visited, then terminate."
        + "\n" + "6. Go to step 2." + "\n";
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

  public static GraphProperties getDefaultGraphProperties() {

    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.black);
    graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    graphProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.red);

    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

    return graphProps;

  }

  public static TextProperties getDefaultTextProperties() {

    TextProperties tprops = new TextProperties();
    tprops.set("font", new Font("SansSerif", 1, 16));
    tprops.set("color", Color.black);
    return tprops;

  }

  /**
   * 
   * @return the defaultMatrix
   */
  public static int[][] getDefaultAdjMatrix() {
    return getDefaultAdjMatrix(3);
  }

  /**
   * some tests
   * 
   * @param index
   * @return the default matrix
   */
  public static int[][] getDefaultAdjMatrix(int index) {
    int[][] AdjMatrix;
    if (index == 0) {
      AdjMatrix = new int[5][5];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 35;
      AdjMatrix[1][0] = 35;
      AdjMatrix[0][3] = 40;
      AdjMatrix[3][0] = 40;
      AdjMatrix[1][3] = 25;
      AdjMatrix[3][1] = 25;
      AdjMatrix[1][2] = 10;
      AdjMatrix[2][1] = 10;
      AdjMatrix[2][3] = 20;
      AdjMatrix[3][2] = 20;
      AdjMatrix[2][4] = 30;
      AdjMatrix[4][2] = 30;
      AdjMatrix[3][4] = 15;
      AdjMatrix[4][3] = 15;
      //
    } else if (index == 1) {
      AdjMatrix = new int[6][6];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 5;
      AdjMatrix[1][0] = 5;
      AdjMatrix[1][2] = 7;
      AdjMatrix[2][1] = 7;
      AdjMatrix[2][3] = 6;
      AdjMatrix[3][2] = 6;
      AdjMatrix[3][4] = 8;
      AdjMatrix[4][3] = 8;
      AdjMatrix[4][5] = 4;
      AdjMatrix[5][4] = 4;
      AdjMatrix[5][0] = 9;
      AdjMatrix[0][5] = 9;
      //

    }

    else if (index == 2) {
      AdjMatrix = new int[4][4];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 1;
      AdjMatrix[1][0] = 1;
      AdjMatrix[1][2] = 4;
      AdjMatrix[2][1] = 4;
      AdjMatrix[1][3] = 3;
      AdjMatrix[3][1] = 3;
      AdjMatrix[2][3] = 2;
      AdjMatrix[3][2] = 2;
      //
    } else if (index == 3) {
      AdjMatrix = new int[8][8];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 4;
      AdjMatrix[1][0] = 4;
      AdjMatrix[1][2] = 11;
      AdjMatrix[2][1] = 11;
      AdjMatrix[2][3] = 3;
      AdjMatrix[3][2] = 3;
      AdjMatrix[3][4] = 12;
      AdjMatrix[4][3] = 12;
      AdjMatrix[4][5] = 2;
      AdjMatrix[5][4] = 2;
      AdjMatrix[5][6] = 10;
      AdjMatrix[6][5] = 10;
      AdjMatrix[6][7] = 1;
      AdjMatrix[7][6] = 1;
      AdjMatrix[7][0] = 9;
      AdjMatrix[0][7] = 9;

      AdjMatrix[2][6] = 7;
      AdjMatrix[6][2] = 7;
      //
    }

    else {
      AdjMatrix = new int[3][3];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 24;
      AdjMatrix[1][0] = 24;
      AdjMatrix[0][2] = 3;
      AdjMatrix[2][0] = 3;
      AdjMatrix[1][2] = 14;
      AdjMatrix[2][1] = 14;
      //
    }

    return AdjMatrix;
  }

  /**
   * main method
   * 
   * @param args
   */
  public static void main(String[] args) {

    NearestNeighborAlgorithm b = new NearestNeighborAlgorithm();
    if (b.text == null)
      b.text = getDefaultTextProperties();
    if (b.gprops == null)
      b.gprops = getDefaultGraphProperties();

    b.start();

    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
    primitives.put("adjMatrix", getDefaultAdjMatrix());

    AnimationPropertiesContainer props = new AnimationPropertiesContainer();
    b.findMinTour(props, primitives);

    System.out.println(b.lang);
  }

  private void findMinTour(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    lang.setStepMode(true);

    SourceCodeProperties scPresena = new SourceCodeProperties();
    scPresena.set(AnimationPropertiesKeys.FONT_PROPERTY, text.get("font"));

    scPresena.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scPresena.set(AnimationPropertiesKeys.COLOR_PROPERTY, text.get("color"));

    adjMatrix = (int[][]) primitives.get("adjMatrix");

    if (adjMatrix == null)
      adjMatrix = getDefaultAdjMatrix();

    int size = adjMatrix.length;

    Node[] graphNodes = new Node[size];
    String[] nodeLabels = new String[size];

    int startX = 100;
    int startY = 200;
    int x = startX;
    int y = startY;
    int xmax = startX;
    int delta = 80;
    if (size > 10)
      delta = 50;

    for (int i = 0; i < size; i++) {
      graphNodes[i] = new Coordinates(x, y);

      if (i < Math.round(size / 2) - 1) {
        x = x + delta;
        xmax = x;

      }

      if (i == Math.round(size / 2) - 1) {

        y = y + 200;

      }

      if (i > Math.round(size / 2) - 1) {
        x = x - delta;
      }

    }
    char startChar = 65;

    for (int i = 0; i < size; i++) {
      nodeLabels[i] = startChar + "";
      startChar++;
    }

    // new code
    SourceCode scc = lang.newSourceCode(new Coordinates(xmax + 450, 120),
        "sourceCode", null, scPresena);

    scc.addCodeLine(
        "1.From the starting vertex , choose the edge with the smallest cost and use ",
        "line1", 0, null);

    scc.addCodeLine("that as the first edge in your circuit.", "line11", 2,
        null);
    scc.addCodeLine(
        "2.Continue in this manner , choosing among the edges that coonect from the current ",
        "line2", 0, null); // 0
    scc.addCodeLine("vertex to vertices you have not yet visited", "line21", 2,
        null); // 0
    scc.addCodeLine(
        "3.When you have visited evry vertex , return to the starting vertex. ",
        "line3", 0, null); //

    gprops.setName("Nearest Neighbor Algorithm");

    Graph g = lang.newGraph(this.getName(), adjMatrix, graphNodes, nodeLabels,
        null, gprops);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if ((i != j) && (adjMatrix[i][j] != 0)
            && (adjMatrix[i][j] != Integer.MAX_VALUE))
          g.setEdgeWeight(i, j, adjMatrix[i][j], null, null);
      }
    }

    int mycounter = 1;

    SourceCodeProperties scGraph1 = new SourceCodeProperties();
    scGraph1.set(AnimationPropertiesKeys.FONT_PROPERTY, text.get("font"));

    scGraph1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scGraph1.set(AnimationPropertiesKeys.COLOR_PROPERTY, text.get("color"));

    SourceCode sc2 = lang.newSourceCode(new Coordinates(xmax + 35, 50),
        "sourceCode", null, scGraph1);

    ArrayList<Integer> visitedNodes = new ArrayList<Integer>();
    ArrayList<Integer> path = new ArrayList<Integer>();
    ArrayList<Integer> weights = new ArrayList<Integer>();

    // select one node randomly
    Random rand = new Random();
    int randIndex = rand.nextInt(adjMatrix.length);

    sc2.addCodeLine("Step" + mycounter + ":", null, 0, null);
    sc2.addCodeLine("NNA takes in step 1 a random node for example "
        + getAsChar(randIndex), "z1", 0, null);

    g.highlightNode(randIndex, null, null);
    scc.highlight("line1");
    scc.highlight("line11");
    lang.nextStep("3.Graph");
    scc.unhighlight("line1");
    scc.unhighlight("line11");

    int startNode = randIndex;

    int currNode = randIndex;

    while (mycounter - 1 < adjMatrix.length) {

      if (mycounter != 1) {
        sc2.addCodeLine("Step" + mycounter + ":", null, 0, null);
        sc2.addCodeLine(" The algorithm takes in step " + mycounter + " node "
            + getAsChar(currNode), "z1", 0, null);
        g.highlightNode(currNode, null, null);
        scc.highlight("line2");
        scc.highlight("line21");
        lang.nextStep();
        scc.unhighlight("line2");
        scc.unhighlight("line21");

      }

      path.add(currNode);

      if (mycounter == adjMatrix.length) {

        if (adjMatrix[currNode][startNode] != Integer.MAX_VALUE
            && adjMatrix[currNode][startNode] != 0) {
          sc2.addCodeLine(" The algorithm now goes back to the start node "
              + getAsChar(startNode) + ".", null, 0, null);
          sc2.addCodeLine(" The weight of edge " + getAsChar(currNode) + ","
              + getAsChar(startNode) + " is " + adjMatrix[currNode][startNode]
              + ".", null, 0, null);

          path.add(startNode);
          weights.add(adjMatrix[currNode][startNode]);
          g.highlightEdge(currNode, startNode, null, null);
          sc2.addCodeLine(" The algorithm ends successfully.", "wz", 0, null);
          sc2.highlight("wz");
          scc.highlight("line3");
          lang.nextStep();
          // scc.unhighlight("line3");
          scc.hide();

          sc2.hide();

          SourceCode cy = lang.newSourceCode(new Coordinates(90, 120),
              "Presnetationsum", null, scGraph1);
          cy.addCodeLine("                        ", null, 0, null);

          String spath = "";
          String sweights = "";
          int sum = 0;
          for (int i = 0; i < path.size(); i++)
            if (i != path.size() - 1)
              spath = spath + getAsChar(path.get(i)) + " -> ";
            else
              spath = spath + getAsChar(path.get(i));

          for (int j = 0; j < weights.size(); j++) {
            if (j != weights.size() - 1) {
              sweights = sweights + weights.get(j) + " + ";
            } else {
              sweights = sweights + weights.get(j);
            }

            sum = sum + weights.get(j);
          }

          cy.addCodeLine(" The found route is the following: " + spath, null,
              0, null);
          cy.addCodeLine(" The total weight of that route is " + sweights
              + " = " + sum, null, 0, null);
          cy.addCodeLine(" ", null, 0, null);

          break;
        } else {
          sc2.addCodeLine(" There is no edge back to the start node "
              + getAsChar(startNode) + ".", null, 0, null);
          sc2.addCodeLine(" The algorithm ends with failure.", "zz", 0, null);
          sc2.highlight("zz");
          lang.nextStep();
          scc.hide();
          break;
        }
      } else {

        visitedNodes.add(currNode);

        ArrayList<Index> connections = new ArrayList<Index>();
        Index ind = new Index(0, 0, 0);
        ;

        // find cheapest edge
        for (int j = 0; j < adjMatrix.length; j++) {

          if (adjMatrix[currNode][j] == 0) {
            adjMatrix[currNode][j] = Integer.MAX_VALUE;
          }

          if (currNode != j) {
            ind = new Index(adjMatrix[currNode][j], currNode, j);
            connections.add(ind);
          }

        }
        // sortieren
        Collections.sort(connections, ind);

        Index mindex = connections.get(0);

        int nextNode = mindex.y;
        int c = 1;

        while (visitedNodes.contains(nextNode) && c < connections.size()) {
          nextNode = connections.get(c).y;
          c++;
        }

        if (adjMatrix[currNode][nextNode] != Integer.MAX_VALUE) {
          sc2.addCodeLine(" The cheapest edge is " + getAsChar(currNode) + ","
              + getAsChar(nextNode) + " with the weight "
              + adjMatrix[currNode][nextNode], "z2", 0, null);
          weights.add(adjMatrix[currNode][nextNode]);
          g.highlightEdge(currNode, nextNode, null, null);
          g.highlightNode(nextNode, null, null);
          sc2.highlight("z2");
        } else {
          sc2.addCodeLine(" All neighbors of " + getAsChar(currNode)
              + " were visited.", null, 0, null);
          sc2.addCodeLine(" The algorithm ends with failure.", null, 0, null);

          lang.nextStep();
          scc.hide();
          break;
        }

        lang.nextStep();
        sc2.unhighlight("z2");

        currNode = nextNode;

        mycounter++;
      }
    }
    // lang.nextStep();
    g.hide();
    sc2.hide();
    SourceCode sc4 = lang.newSourceCode(new Coordinates(90, 200),
        "Presnetation1", null, scGraph1);

    // sc4.addCodeLine("", null, 0, null);
    // sc4.addCodeLine("", null, 0, null);

    sc4.addCodeLine("the complexity of Nearest neighbor Algorithm is O(n*n)",
        null, 0, null);
    sc4.addCodeLine("This algorithm provides not awlays an optimal solution. ",
        null, 0, null);
    sc4.addCodeLine(
        "Sometimes, we found  a way to go from the start node , vistiging all nodes in graph , ",
        null, 0, null);
    sc4.addCodeLine(
        "and come back to it. we have a succesful case only if there is an edge from the last visited ",
        null, 0, null); // 0

    sc4.addCodeLine("to the start node", null, 0, null); // 0
    lang.nextStep("4.Complexity");

  }

  /**
   * 
   * @param ind
   * @return the char value
   */
  public char getAsChar(int ind) {
    char retChar = (char) (65 + ind);
    return retChar;
  }
}

class Index implements Comparator<Index> {
  public int val;
  public int x;
  public int y;

  /**
   * 
   * @param a
   * @param b
   */
  public Index(int a, int b) {
    this.x = a;
    this.y = b;
    this.val = 0;
  }

  /**
   * 
   * @param val
   * @param a
   * @param b
   */
  public Index(int val, int a, int b) {
    this.val = val;
    this.x = a;
    this.y = b;
  }

  @Override
  /**
   * 
   */
  public int compare(Index o1, Index o2) {

    if (o1.val < o2.val)
      return -1;
    else if (o1.val == o2.val)
      return 0;
    else
      return 1;
  }
}