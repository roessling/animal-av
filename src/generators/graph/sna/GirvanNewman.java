package generators.graph.sna;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpers.Pathfinder;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class GirvanNewman implements Generator {
  Language             lang;
  Integer[][]          betweenness;
  boolean              defaultGraph   = true;
  Text[][]             text_link;
  Graph                graph;
  int                  size;
  DecimalFormat        df             = new DecimalFormat();
  Pathfinder           findingWalter;
  int                  graph_max_x;
  int                  graph_min_x;
  int                  graph_max_y;
  int                  graph_min_y;
  Cluster              dendroTreeRoot = new Cluster();
  int[][]              graphAdjacencyMatrix;
  float[][]            betweeness;

  // Properties
  GraphProperties      graph_properties;
  TextProperties       textProperties;
  SourceCodeProperties sourceCodeProperties;
  TextProperties       title_properties;
  // Color Settings
  Color                highlight_color;

  public GirvanNewman() {

  }

  public void init() {
    lang = new AnimalScript("Girvan-Newman", "Baran D. Ö.,Patrick F.", 640, 480);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // Title Properties
    title_properties = (TextProperties) props.getPropertiesByName("titleText");
    Font font = (Font) title_properties
        .get(AnimationPropertiesKeys.FONT_PROPERTY);
    Font new_font = new Font(font.getName(), font.getStyle(), 24);
    title_properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new_font);

    // Text & SourceCode Properties
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    props.remove(sourceCodeProperties);

    textProperties = (TextProperties) props.getPropertiesByName("standardText");
    // Graph & GraphProperties
    graph = (Graph) primitives.get("graph");
    graph_properties = (GraphProperties) props
        .getPropertiesByName("graphProps");
    // Highlight Color
    highlight_color = (Color) primitives.get("highlight_color");
    run();
    return lang.toString();
  }

  public String getName() {
    return "Girvan-Newman";
  }

  public String getAlgorithmName() {
    return "Girvan-Newman";
  }

  public String getAnimationAuthor() {
    return "Baran Denis Özdemir, Patrick Felka";
  }

  public String getDescription() {
    return "<p><span><span style=\"font-weight: bold;\">Introduction:</span></span></p>"
        + "\n"
        + "<p><span>"
        + "\n"
        + "The Girvan-Newman Algorithm is used to detect community structures in complex systems. A community<br>"
        + "\n"
        + "in a network is a group of nodes whose connections are dense but connections to other communities<br>"
        + "\n"
        + "and nodes are sparse. To detect communities the algorithm focuses on least central edges, <br>"
        + "\n"
        + "edges which lie most likely in between communities. <br>"
        + "\n"
        + "By progressively removing central edges community structures are revealed. The Algorithm produces<br>"
        + "\n"
        + "a dendogram as result. The dendogram visualizes the hierarchic structure of the network. <br>"
        + "\n"
        + "The leaves are individual nodes.<br><br>"
        + "\n"
        + "</span></p>" + "\n";
  }

  public String getCodeExample() {
    return "GrivanNewman(Graph g){"
        + "\n		create a new tree t an add all Nodes of g as a child node of t"
        + "\n		while graph g contains edges {"
        + "\n			calculate BetweennessCentrality of g"
        + "\n			remove the edge with the highest centrality"
        + "\n			if the graph is divided into two unconnected parts by removing the edge{"
        + "\n				adjust the tree by moving the child’s of t into two new sub-trees "
        + "\n				of t which contains the same nodes as the respective "
        + "\n				unconnected part of g" + "\n			}" + "\n		}" + "\n	return t \n"
        + "}";
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

  public void run() {
    // Title
    lang.newText(new Coordinates(20, 30), "Girvan-Newman", "title", null,
        title_properties);
    RectProperties rprop = new RectProperties();
    rprop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "title", "NW"), new Offset(5, 5, "title",
        "SE"), "titleR", null, rprop);

    // Introduction
    SourceCode scDef = lang.newSourceCode(new Offset(0, 3, "title", "SW"),
        "definition", null, sourceCodeProperties);
    scDef.addCodeLine("Introduction:", null, 0, null);
    scDef
        .addCodeLine(
            "The Girvan-Newman Algorithm is used to detect community structures in complex systems. A community",
            null, 0, null);
    scDef
        .addCodeLine(
            "in a network is a group of nodes whose connections are dense but connections to other communities",
            null, 0, null);
    scDef
        .addCodeLine(
            "and nodes are sparse. To detect communities the algorithm focuses on least central edges,",
            null, 0, null);
    scDef
        .addCodeLine(
            "edges which lie most likely in between communities. To determine a central edge, the algorithm ",
            null, 0, null);
    scDef
        .addCodeLine(
            "uses the betweenness centrality (link). A visualization of this algorithm is also available",
            null, 0, null);
    scDef.addCodeLine(
        "in animal: Generators -> Graphs -> Betweenness Centrality (link)",
        null, 0, null);
    scDef.addCodeLine("", null, 0, null);
    scDef
        .addCodeLine(
            "By progressively removing central edges community structures are revealed. The Algorithm produces",
            null, 0, null);
    scDef
        .addCodeLine(
            "a dendogram as result. The dendogram visualizes the hierarchic structure of the network.",
            null, 0, null);
    scDef.addCodeLine("The leaves are individual nodes.", null, 0, null);
    scDef.highlight(0);
    Rect codeRec1Def = lang.newRect(new Offset(-5, -5, "definition", "NW"),
        new Offset(5, 5, "definition", "SE"), "codRec1", null, rprop);
    rprop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    Rect codeRec2Def = lang.newRect(new Offset(-3, -3, "definition", "NW"),
        new Offset(3, 3, "definition", "SE"), "codRec2", null, rprop);

    lang.nextStep("Introduction");
    scDef.hide();
    codeRec1Def.hide();
    codeRec2Def.hide();

    // Paint Graph
    Node[] nodes = new Node[graph.getSize()];
    String[] labels = new String[graph.getSize()];
    for (int i = 0; i < graph.getSize(); i++) {
      nodes[i] = graph.getNode(i);
      labels[i] = graph.getNodeLabel(i);
      // intialize dendroRoot
      dendroTreeRoot.addClusterNode(new ClusterNode(i));
    }
    // adjust graph position
    graph_max_x = ((Coordinates) nodes[0]).getX();
    graph_min_x = ((Coordinates) nodes[0]).getX();
    graph_max_y = ((Coordinates) nodes[0]).getY();
    graph_min_y = ((Coordinates) nodes[0]).getY();
    for (Node node : nodes) {
      graph_max_x = Math.max(((Coordinates) node).getX(), graph_max_x);
      graph_max_y = Math.max(((Coordinates) node).getY(), graph_max_y);
      graph_min_x = Math.min(((Coordinates) node).getX(), graph_min_x);
      graph_min_y = Math.min(((Coordinates) node).getY(), graph_min_y);
    }
    // move graph to
    // startpoint x=20,y=300
    int move_x = 0;
    int move_y = 0;
    move_x = 20 - graph_min_x;
    move_y = 80 - graph_min_y;
    for (int i = 0; i < nodes.length; i++) {
      Coordinates tempNode = (Coordinates) nodes[i];
      nodes[i] = new Coordinates(tempNode.getX() + move_x, tempNode.getY()
          + move_y);
    }
    // Set GraphProperties
    GraphProperties graphProp = new GraphProperties();
    graphProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    graphProp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
    graphProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.WHITE);
    graphProp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);
    graphAdjacencyMatrix = makeMatrixSymmetric(graph.getAdjacencyMatrix());
    graph = lang.newGraph("ExampleGraph", graph.getAdjacencyMatrix(), nodes,
        labels, null, graphProp);
    graph.setStartNode(graph.getNode(0));
    graphAdjacencyMatrix = graph.getAdjacencyMatrix();

    size = graph.getSize();
    if (graph.getSize() == 0 || !existingEdges()) {
      lang.nextStep();
      SourceCodeProperties exitProbs = new SourceCodeProperties();
      exitProbs.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      exitProbs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "SansSerif", Font.PLAIN, 50));
      exitProbs.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      exitProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      SourceCode exitText = lang.newSourceCode(new Coordinates(50, 300),
          "exitText", null, exitProbs);
      exitText.addCodeLine("Congratulation to this Masterpiece of a Graph",
          null, 0, null);
      return;
    }

    text_link = new Text[size][size];
    for (int i = 0; i < graph.getSize(); i++) {
      for (int j = i; j < graph.getSize(); j++) {
        if (graph.getAdjacencyMatrix()[i][j] > 0) {

          // calculate text position between 2 nodes
          Coordinates n1 = (Coordinates) graph.getNode(i);
          Coordinates n2 = (Coordinates) graph.getNode(j);
          int x = ((Math.max(n1.getX(), n2.getX()) - Math.min(n1.getX(),
              n2.getX())) / 2)
              + Math.min(n1.getX(), n2.getX());
          int y = ((Math.max(n1.getY(), n2.getY()) - Math.min(n1.getY(),
              n2.getY())) / 2)
              + Math.min(n1.getY(), n2.getY());
          Coordinates position_text = new Coordinates(x, y);

          lang.newPoint(position_text, "pos_link_text_" + i + "_" + j, null,
              new PointProperties()).hide();
          text_link[i][j] = lang.newText(new Offset(0, -10, "pos_link_text_"
              + i + "_" + j, "C"), "0", "link_text_" + i + "_" + j, null,
              new TextProperties());
          text_link[j][i] = lang.newText(new Offset(0, -10, "pos_link_text_"
              + i + "_" + j, "C"), "0", "link_text_" + i + "_" + j, null,
              new TextProperties());
          text_link[j][i].hide();
        }
      }
    }

    // Explanation
    SourceCode sc3 = lang.newSourceCode(new Coordinates(430, 100),
        "definition3", null, sourceCodeProperties);
    sc3.addCodeLine(
        "1. The betweenness of all existing edges in the network is calculated first.",
        null, 0, null);
    sc3.addCodeLine("2. The edge(s) with the highest betweenness is removed.",
        null, 0, null);
    sc3.addCodeLine(
        "3. The betweenness of all edges affected by the removal is recalculated.",
        null, 0, null);
    sc3.addCodeLine("4. Steps 2 and 3 are repeated until no edges remain.",
        null, 0, null);

    Rect codeRec5 = lang.newRect(new Offset(-5, -5, "definition3", "NW"),
        new Offset(5, 5, "definition3", "SE"), "codeRec5", null);
    Rect codeRec6 = lang.newRect(new Offset(-3, -3, "definition3", "NW"),
        new Offset(3, 3, "definition3", "SE"), "codeRec6", null);
    sc3.highlight(0);

    SourceCode sc4 = lang.newSourceCode(new Coordinates(430, 100), "result",
        null, sourceCodeProperties);
    sc4.addCodeLine("Result:", null, 0, null);
    sc4.addCodeLine(
        "The algorithm of Girvan-Newman produces a dendogram as result whose leaves correspond",
        null, 0, null);
    sc4.addCodeLine(
        "to the vertices of the graph. In the composition of the dendogram the communities found",
        null, 0, null);
    sc4.addCodeLine(
        "can be read. The relationships determined here can be used for further investigations.",
        null, 0, null);
    sc4.addCodeLine(
        "For example by determing and comparing certain charasteristics for each group.",
        null, 0, null);
    sc4.addCodeLine("", null, 0, null);
    sc4.addCodeLine("Complexity:", null, 0, null);
    sc4.addCodeLine(
        "The betweeness calculation takes O(v^3) time. In the worst case szenario only one",
        null, 0, null);
    sc4.addCodeLine(
        "edge is deleted in each step. This causes the total consumption of O(e*v^3) time.",
        null, 0, null);
    sc4.addCodeLine("e=edges, v=vertices", null, 0, null);
    sc4.hide();

    lang.nextStep("Run Algorithm");
    calcBetweeness();
    showBetweeness();
    lang.nextStep();
    sc3.unhighlight(0);
    sc3.highlight(1);

    proceedBetweenessResults();

    while (existingEdges()) {
      lang.nextStep();
      sc3.unhighlight(1);
      sc3.highlight(2);

      calcBetweeness();
      showBetweeness();

      lang.nextStep();
      sc3.unhighlight(2);
      sc3.highlight(1);
      proceedBetweenessResults();
    }

    lang.nextStep();
    sc3.unhighlight(1);
    sc3.addCodeLine("As Result a Dendogramm is produced", null, 0, null);
    sc3.highlight(4);

    codeRec5.hide();
    codeRec6.hide();
    codeRec5 = lang.newRect(new Offset(-5, -5, "definition3", "NW"),
        new Offset(5, 5, "definition3", "SE"), "codeRec5", null);
    codeRec6 = lang.newRect(new Offset(-3, -3, "definition3", "NW"),
        new Offset(3, 3, "definition3", "SE"), "codeRec6", null);
    codeRec5.show();
    codeRec6.show();

    // Get node order
    LinkedList<Integer> order = dendroTreeRoot.getDendroLableOrder();
    // Convert Linked-List to Array
    int[] dendroLableOrder = new int[order.size()];
    for (int i = 0; i < order.size(); i++) {
      dendroLableOrder[i] = order.get(i);
    }
    // Initialize Labels
    HashMap<Integer, Text> dendo_labels = drawLabels(dendroLableOrder,
        sc3.getUpperLeft());
    // Initialize node positions
    // node position = label position "north"
    for (Integer node_index : dendo_labels.keySet()) {
      dendroTreeRoot.setNodePosition(node_index, new Offset(0, 0, dendo_labels
          .get(node_index).getName(), "north"));
    }
    // Paint Dendrogram
    dendroTreeRoot.paintCluster();

    lang.nextStep("Results");
    sc3.hide();
    codeRec5.hide();
    codeRec6.hide();
    sc4.show();
    lang.nextStep("Interpretation");
  }

  private HashMap<Integer, Text> drawLabels(int[] arr, Node pos) {
    HashMap<Integer, Text> dendo_labels = new HashMap<Integer, Text>();
    for (int i = 0; i < arr.length; i++) {
      dendo_labels.put(arr[i], lang.newText(new Offset(((i * 30)), 200,
          "definition3", "SW"), graph.getNodeLabel(arr[i]), "dendo_label_"
          + graph.getNodeLabel(arr[i]), null, new TextProperties()));
    }
    return dendo_labels;
  }

  private void calcBetweeness() {
    findingWalter = new Pathfinder(graphAdjacencyMatrix);
    betweeness = new float[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = i + 1; j < size; j++) {
        List<List<Integer>> directions = findingWalter.getDirections(i, j);
        HashMap<Integer, List<String>> queue = new HashMap<Integer, List<String>>();
        Float[][] inc = new Float[size][size];
        for (int ii = 0; ii < inc.length; ii++) {
          for (int jj = 0; jj < inc[0].length; jj++) {
            inc[ii][jj] = 0f;
          }
        }
        for (int h = 0; h < directions.size(); h++) {
          for (int m = 0; m < (directions.get(h).size() - 1); m++) {
            if (!queue.containsKey(m)) {
              queue.put(m, new ArrayList<String>());
            }
            if (!queue.get(m).contains(
                directions.get(h).get(m) + ";" + directions.get(h).get(m + 1))) {
              queue.get(m)
                  .add(
                      directions.get(h).get(m) + ";"
                          + directions.get(h).get(m + 1));
            }
            if (m != directions.get(h).size() - 1) {
              inc[directions.get(h).get(m)][directions.get(h).get(m + 1)] += 1f;
              inc[directions.get(h).get(m + 1)][directions.get(h).get(m)] += 1f;
            }
          }
        }
        if (directions.size() > 1) {
          for (int h = 0; h < inc.length; h++) {
            for (int m = 0; m < inc[0].length; m++) {
              inc[h][m] = inc[h][m] / directions.size();
            }
          }
        }
        List<String> step_i;
        for (int m = 0; m < queue.size(); m++) {
          step_i = queue.get(m);
          for (int o = 0; o < step_i.size(); o++) {

            int from = Integer.parseInt(step_i.get(o).split(";")[0]);
            int to = Integer.parseInt(step_i.get(o).split(";")[1]);

            // update counters, visual and intern
            float oldCounter = betweeness[from][to];
            float add = inc[from][to];
            float newCounter = oldCounter + add;

            betweeness[from][to] = newCounter;
            betweeness[to][from] = newCounter;
          }
        }
      }
    }
  }

  private void showBetweeness() {
    int step = 200;
    int delay = 100;
    for (int i = 0; i < size; i++) {
      for (int j = i; j < size; j++) {
        if (graphAdjacencyMatrix[i][j] > 0)
          text_link[i][j].setText(df.format(betweeness[i][j]),
              new AnimalTiming2(delay), new AnimalTiming2(step));
      }
      delay += step;
    }
  }

  // Highlight Results
  private void proceedBetweenessResults() {
    List<int[]> affectedEdges;
    affectedEdges = getEdgeWithHighestBetweeness();

    for (int[] s : affectedEdges) {
      // highlight
      text_link[s[0]][s[1]].changeColor("color", highlight_color, null, null);
      text_link[s[1]][s[0]].changeColor("color", highlight_color, null, null);
    }
    lang.nextStep();

    for (int[] s : affectedEdges) {
      // remove
      graph.highlightEdge(s[0], s[1], null, null);
      text_link[s[0]][s[1]].changeColor("color", Color.white, null, null);
      text_link[s[1]][s[0]].changeColor("color", Color.white, null, null);
      text_link[s[0]][s[1]].hide();
      text_link[s[1]][s[0]].hide();
      // graph.showEdge(s[0], s[1], new AnimalTiming2(100), new
      // AnimalTiming2(100));
      // graph.hideEdge(0, 1, null, null);
      graphAdjacencyMatrix[s[0]][s[1]] = 0;
      graphAdjacencyMatrix[s[1]][s[0]] = 0;
    }
    // ======================================
    // Check whether the graph by removing the edge divided into two
    // clusters
    HashMap<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
    // Die HashMap enhält zu allen Knoten infomationen ob ein Knoten bereits
    // gesehen wurde
    for (int[] s : affectedEdges) {
      for (int i = 0; i < s.length; i++) {
        if (visited.containsKey(s[i])) {
          visited.put(s[i], true);
        } else {
          visited.put(s[i], false);
        }
      }
    }
    // Suche nach doppelten Knoten bzw. Kanten
    for (Integer key : visited.keySet()) {
      Pathfinder pf = new Pathfinder(graphAdjacencyMatrix);
      if (visited.get(key)) {
        List<Integer> affectedMultiClusterEdges = new ArrayList<Integer>();
        for (int[] e : affectedEdges) {
          if (e[0] == key) {
            affectedMultiClusterEdges.add(e[1]);
          }
          if (e[1] == key) {
            affectedMultiClusterEdges.add(e[0]);
          }
        }
        List<List<Integer>> group2 = new ArrayList<List<Integer>>();
        for (Integer node : affectedMultiClusterEdges) {
          List<Integer> nodes = pf.getReachableNodes(node);
          if (nodes.size() > 1) {
            group2.add(pf.getReachableNodes(node));
          }
        }
        if (group2.size() > 1) {
          dendroTreeRoot.splitMultiCluster(key, group2);
        }
      }
    }
    // REMOVE MULTI EDGES
    for (Integer key : visited.keySet()) {
      if (visited.get(key)) {
        for (int i = 0; i < affectedEdges.size(); i++) {
          if (affectedEdges.get(i)[0] == key || affectedEdges.get(i)[1] == key) {
            affectedEdges.remove(i);
            i--;
          }
        }
      }
    }
    //
    for (int[] s : affectedEdges) {
      Pathfinder pf = new Pathfinder(graphAdjacencyMatrix);
      // Nodes of cluster 1
      List<Integer> group1 = pf.getReachableNodes(s[0]);
      // Nodes of cluster 2
      List<Integer> group2 = pf.getReachableNodes(s[1]);
      // if the graph does not decompose into two clusters are both
      // clusters are equal
      if (!(group1.containsAll(group2) && group2.containsAll(group1))) {
        if (group1.size() > 1 || group2.size() > 1) {
          // if the graph is divided into two clusters .. adjust the
          // tree
          dendroTreeRoot.splitCluster(group1, group2);
        }
      }
    }
  }

  private List<int[]> getEdgeWithHighestBetweeness() {
    float count = 0;
    List<int[]> highlightMe = new Vector<int[]>();

    for (int i = 0; i < size; i++) {
      for (int j = i + 1; j < size; j++) {
        if (betweeness[i][j] > count) {
          count = betweeness[i][j];
          highlightMe.clear();
          highlightMe.add(new int[] { i, j });
        } else {
          if (betweeness[i][j] == count) {
            highlightMe.add(new int[] { i, j });
          }
        }
      }
    }
    return highlightMe;
  }

  private boolean existingEdges() {
    for (int i = 0; i < size; i++) {
      for (int j = i; j < size; j++) {
        if (graphAdjacencyMatrix[i][j] != 0)
          return true;
        else
          ;
      }
    }
    return false;
  }

  private int[][] makeMatrixSymmetric(int[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = i; j < matrix[i].length; j++) {
        if (matrix[i][j] == 1) {
          matrix[j][i] = 1;
        }
      }
    }
    return matrix;
  }

  class Cluster {
    List<Cluster>                 clusters = new ArrayList<Cluster>();
    HashMap<Integer, ClusterNode> nodes    = new HashMap<Integer, ClusterNode>();
    boolean                       painted  = false;
    private Offset                position;
    private int                   level;

    public boolean isPainted() {
      return painted;
    }

    public void setNodePosition(int node_index, Offset offset) {
      if (nodes.containsKey(node_index)) {
        nodes.get(node_index).setPosition(offset);
      } else {
        for (Cluster c : clusters) {
          c.setNodePosition(node_index, offset);
        }
      }
    }

    public Offset getPosition() {
      return position;
    }

    public void addClusterNode(ClusterNode cn) {
      nodes.put(cn.getNode(), cn);
    }

    public void splitCluster(List<Integer> group1, List<Integer> group2) {
      if (nodes.keySet().containsAll(group1)
          && nodes.keySet().containsAll(group2)) {
        Cluster cGroup1 = new Cluster();
        for (Integer node_group1 : group1) {
          cGroup1.addClusterNode(nodes.remove(node_group1));
        }
        Cluster cGroup2 = new Cluster();
        for (Integer node_group2 : group2) {
          cGroup2.addClusterNode(nodes.remove(node_group2));
        }

        if (cGroup1.nodes.size() > 0 || cGroup1.clusters.size() > 0) {
          clusters.add(cGroup1);
        }
        if (cGroup2.nodes.size() > 0 || cGroup2.clusters.size() > 0) {
          clusters.add(cGroup2);
        }
      } else {
        for (Cluster c : clusters) {
          c.splitCluster(group1, group2);
        }
      }
    }

    public void splitMultiCluster(Integer group1, List<List<Integer>> group2) {
      List<Integer> temp_group = new ArrayList<Integer>();
      temp_group.add(group1);
      for (List<Integer> g : group2) {
        temp_group.addAll(g);
      }
      if (nodes.keySet().containsAll(temp_group)) {
        Cluster newTopCluster = new Cluster();
        Cluster newSubCluster = new Cluster();
        List<Cluster> subClusterList = new ArrayList<Cluster>();

        for (List<Integer> node_group2 : group2) {
          Cluster cGroup2 = new Cluster();
          for (Integer groups : node_group2) {
            cGroup2.addClusterNode(nodes.remove(groups));
          }
          if (cGroup2.nodes.size() > 0 || cGroup2.clusters.size() > 0) {
            subClusterList.add(cGroup2);
          }
        }
        clusters.addAll(subClusterList);

        if (newTopCluster.nodes.size() > 0 || newTopCluster.clusters.size() > 0) {
          clusters.add(newTopCluster);
        }
        if (newSubCluster.nodes.size() > 0 || newSubCluster.clusters.size() > 0) {
          clusters.add(newSubCluster);
        }
      } else {
        for (Cluster c : clusters) {
          c.splitMultiCluster(group1, group2);
        }
      }
    }

    public int getLevel() {
      return level;
    }

    public void paintCluster() {
      // First, paint all sub-clusters
      for (Cluster c : clusters) {
        if (!c.isPainted()) {
          c.paintCluster();
        }
      }
      lang.nextStep();

      // Update Cluster Level
      level = 2;
      for (Cluster c : clusters) {
        level = Math.max(level, (c.getLevel() + 1));
      }
      // Paint Lines of current Cluster
      List<Polyline> line_cache = new ArrayList<Polyline>();

      // Vertical Lines (Cluster)
      for (Cluster c : clusters) {
        Node[] line_ver = {
            new Offset(0, -10 * (level - c.getLevel()), c.getPosition()
                .getBaseID(), "north"),
            new Offset(0, 0, c.getPosition().getBaseID(), "north") };
        line_cache.add(lang.newPolyline(line_ver, "line_vertical_cluster_"
            + level, null));
      }

      // Vertical Lines (Nodes)
      Set<Integer> nodes_index = nodes.keySet();
      for (Integer cn_index : nodes_index) {
        ClusterNode cn = nodes.get(cn_index);
        Node[] line_ver = {
            new Offset(0, -10 * (level - 1), cn.getPosition().getBaseID(),
                "north"),
            new Offset(0, 0, cn.getPosition().getBaseID(), "north") };
        line_cache.add(lang.newPolyline(line_ver,
            "line_vertical_node_" + cn.getNode(), null));
      }

      // Horizonal Lines
      for (int i = 0; i < line_cache.size() - 1; i++) {
        Node[] line_hor = { new Offset(0, 0, line_cache.get(i), "north"),
            new Offset(0, 0, line_cache.get(i + 1), "north") };
        Polyline pl = lang.newPolyline(line_hor, "line_horizontal_"
            + line_cache.get(i).getName() + "_"
            + line_cache.get(i + 1).getName(), null);
        // Update Cluster Position
        position = new Offset(0, -10, pl.getName(), "north");
      }
      if (line_cache.size() == 1) {
        position = new Offset(0, 0, line_cache.get(0).getName(), "north");
      }
      painted = true;

      // UNHIGHLIGHT EDGES
      for (Integer nodeID1 : nodes.keySet()) {
        for (Cluster cluster : clusters) {
          for (Integer nodeID2 : cluster.nodes.keySet()) {
            graph.unhighlightEdge(nodeID1, nodeID2, null, null);
          }
        }
        for (Integer nodeID2 : nodes.keySet()) {
          graph.unhighlightEdge(nodeID1, nodeID2, null, null);
        }
      }
      for (Cluster cluster1 : clusters) {
        for (Cluster cluster2 : clusters) {
          for (Integer nodeID1 : cluster1.nodes.keySet()) {
            for (Integer nodeID2 : cluster2.nodes.keySet()) {
              graph.unhighlightEdge(nodeID1, nodeID2, null, null);
            }
          }
        }
      }
    }

    public LinkedList<Integer> getDendroLableOrder() {
      LinkedList<Integer> dendroOrder = new LinkedList<Integer>();
      getDendroLableSuborder(dendroOrder);
      return dendroOrder;
    }

    private void getDendroLableSuborder(LinkedList<Integer> dendroOrder) {
      dendroOrder.addAll(nodes.keySet());
      for (Cluster c : clusters) {
        c.getDendroLableSuborder(dendroOrder);
      }
    }

    // public String toString() {
    // String string;
    // string = "\t[[Nodes:" + nodes.keySet() + "][Cluster:";
    // for (Cluster c : clusters) {
    // string = string + c.toString();
    // }
    // string = string + "]]";
    // string = string.replace("\t", "\t\t");
    // return string;
    // }
  }

  private class ClusterNode {

    private Offset position;
    int            node;

    public ClusterNode(int node_index) {
      this.node = node_index;
    }

    public Offset getPosition() {
      return position;
    }

    public void setPosition(Offset position) {
      this.position = position;
    }

    public int getNode() {
      return node;
    }

  }

  class AnimalTiming2 extends Timing {

    public AnimalTiming2(int theDelay) {
      super(theDelay);
    }

    @Override
    public String getUnit() {
      return "ms";
    }

  }
}