package generators.graph.betweenness;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpers.Pathfinder;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import algoanim.primitives.Graph;
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

import java.util.Hashtable;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.StringMatrix;

public class Betweenness implements Generator {
  private Language     lang;

  // Properties
  GraphProperties      graph_properties;
  TextProperties       textProperties;
  SourceCodeProperties sourceCodeProperties;
  // Color Settings
  Color                matrix_highlight_color;
  Color                result_highlight_color;
  // Input Graph
  Graph                graph;
  // Data structure
  StringMatrix         unvisitedNodeMatrix;
  Text[][]             text_link;
  Text[]               text_node;
  Float[]              count_node;
  Float[][]            count_link;

  Text                 path_counter;
  String               path_counter_text = "Sum: ";
  DecimalFormat        df                = new DecimalFormat();
  BETWEENNESS_TYPE     type;
  String               typeName;

  public enum BETWEENNESS_TYPE {
    NODE_BETWEENNESS, LINK_BETWEENNESS
  }

  private TextProperties title_properties;

  public Betweenness(BETWEENNESS_TYPE type) {
    // true:node Betweenness false:link Betweenness
    this.type = type;
    if (type == BETWEENNESS_TYPE.LINK_BETWEENNESS) {
      typeName = "link";
    } else if (type == BETWEENNESS_TYPE.NODE_BETWEENNESS) {
      typeName = "node";
    }
  }

  public void init() {
    lang = new AnimalScript("Betweenness Centrality (" + typeName + ")",
        "Baran D. Ö.,Patrick F.", 800, 600);
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
    // Matrix Highlight Color
    matrix_highlight_color = (Color) primitives.get("matrix_highlight_color");
    result_highlight_color = (Color) primitives.get("result_highlight_color");
    run();
    return lang.toString();
  }

  public String getName() {
    return "Betweenness Centrality (" + typeName + ")";

  }

  public String getAlgorithmName() {
    return "Betweenness Centrality";
  }

  public String getAnimationAuthor() {
    return "Patrick Felka, Baran Denis Özdemir";
  }

  public String getDescription() {
    return "<p><span>"
        + "\n"
        + "One oft the primary uses of graph theory in social network analysis is the identification&nbsp;<br>"
        + "\n"
        + "of the &bdquo;most important&ldquo; actors in a social network.&nbsp;Actors who are the most important&nbsp;<br>"
        + "\n"
        + "or the most prominent are usually located in strategic locations within the network. <br></span></p>"
        + "\n"
        + "\n"
        + "<p><span>"
        + "\n"
        + "Interactions betweentwo nonadjacent actors might depend on the other actors in the&nbsp;<br>"
        + "\n"
        + "set of actors. Especially the actors who lie on the path between the two. These<br>"
        + "\n"
        + "&ldquo;other actors &ldquo; potentially might have some control over the interactions between&nbsp;<br>"
        + "\n"
        + "the two non adjacent actors. 	<br>"
        + "\n"
        + "The important idea here is that an actor is central if it lies&nbsp;<br>"
        + "\n"
        + "between other actors on their geodesics, implying that to have a large &ldquo;betweenness&rdquo;<br>"
        + "\n"
        + "centrality, the actor must be between many of the actor via their geodesics.<br><br>"
        + "\n"
        + "</span>"
        + "\n"
        + "<span style=\"\">\"Quote:"
        + "\n"
        + "Wassermann, Faust 1999; Social Network Analysis: Methods and Applications\"</span>"
        + "<span>\n <br><br>Note: The algorithm requires an unweighted and undirected graph</p>"
        + "\n";
  }

  public String getCodeExample() {
    String codeE;
    if (type == BETWEENNESS_TYPE.NODE_BETWEENNESS) {

      codeE = "Graph g = /* read input graph */;\n"
          + "Nodeset ns = new Workset(g.getNodes()) \n"
          + "Int[] nodeCounter; \n" + "foreach (Node i : ns) { \n"
          + "   foreach (Node j: ns) { \n"
          + "		path = findShortestPath(i,j); \n" + "		foreach(Node k:path){ \n"
          + "			if(k != i && k != j){ \n" + "				nodeCounter[k]++;\n"
          + "			}\n" + "		}\n" + "		/*if there are multiple shortest paths \n"
          + "		increase the node counter proportionally */\n" + "   }\n"
          + "}\n" + "Int sum = sum(nodeCounter[]);\n" +

          "Int[] nodeBetweenness;\n" + "foreach(Node k:nodeCounter){\n"
          + "	nodeBetweenness[k] = nodeCounter[k] / sum;\n" + "}\n"
          + "return nodeBetweenness;\n";
    } else {
      codeE = "Graph g = /* read input graph */;\n"
          + "Nodeset ns = new Nodeset(g.getNodes()) \n"
          + "Edgeset es = new Edgeset(g.getEdges());\n"
          + "Int[] edgeCounter=0; \n" +

          "foreach (Node i : ns) { \n" + "   foreach (Node j: ns) { \n"
          + "	path = findShortestPath(i,j); \n" + "		foreach(Edge e:path){ \n"
          + "			if(e!=Edge(i,j)){ \n" + "				edgeCounter[e]++;\n" + "			}\n"
          + "		}\n" + "		/*if there are multiple shortest paths \n"
          + "		increase the edge counter proportionally */\n" + "   }\n"
          + "}\n" + "Int sum = sum(edgeCounter[]);\n" +

          "Int[] linkBetweenness;\n" + "foreach(Edge e:edgeCounter){\n"
          + "	linkBetweenness[e] = edegeCounter[e] / sum;\n" + "}\n"
          + "return linkBetweenness;\n";
    }
    return codeE;
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

  @SuppressWarnings("unused")
  public void run() {
    // Title
    lang.newText(new Coordinates(20, 30), "Betweenness centrality (" + typeName
        + ")", "title", null, title_properties);
    RectProperties rprop = new RectProperties();
    rprop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "title", "NW"), new Offset(5, 5, "title",
        "SE"), "titleR", null, rprop);
    lang.nextStep();

    // Introduction
    SourceCode sc = lang.newSourceCode(new Offset(0, 3, "title", "SW"),
        "definition", null, sourceCodeProperties);
    sc.addCodeLine("Introduction:", null, 0, null);
    sc.addCodeLine(
        "One oft he primary uses of graph theory in social network analysis is the identification ",
        null, 0, null);
    sc.addCodeLine(
        "of  the ''most important'' actors in a social network. Actors who are the most important ",
        null, 0, null);
    sc.addCodeLine(
        "or the most prominent are usually located in strategic locations within the network.",
        null, 0, null);
    sc.addCodeLine(" ", null, 0, null);
    sc.addCodeLine(
        "Interactions between two nonadjacent actors might depend on the other actors in the ",
        null, 0, null);
    sc.addCodeLine(
        "set of actors. Especially the actors who lie on the path between the two. Theses",
        null, 0, null);
    sc.addCodeLine(
        "''other actors'' potentially might have some control over the interactions between the ",
        null, 0, null);
    sc.addCodeLine(
        "two non adjacent actors. The important idea here is that an actor is central if it ",
        null, 0, null);
    sc.addCodeLine(
        "lies between other actors on their geodesics, implying that to have a large betweenness",
        null, 0, null);
    sc.addCodeLine(
        "centrality, the actor must be between many of the actor via their geodesics. ",
        null, 0, null);
    sc.addCodeLine(" ", null, 0, null);
    sc.addCodeLine(
        "Quote: Wassermann, Faust 1999; Social Network Analysis: Methods and Applications",
        null, 0, null);
    sc.highlight(0);
    Rect codeRec1 = lang.newRect(new Offset(-5, -5, "definition", "NW"),
        new Offset(5, 5, "definition", "SE"), "codRec1", null, rprop);
    rprop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    Rect codeRec2 = lang.newRect(new Offset(-3, -3, "definition", "NW"),
        new Offset(3, 3, "definition", "SE"), "codRec2", null, rprop);

    lang.nextStep("Introduction");
    sc.hide();
    codeRec1.hide();
    codeRec2.hide();

    // Definition of calculation
    SourceCode sc2 = lang.newSourceCode(new Offset(0, 3, "title", "SW"),
        "definition2", null, sourceCodeProperties);
    sc2.addCodeLine("Definition:", null, 0, null);
    sc2.addCodeLine("The betweenness centrality of " + typeName + " "
        + typeName.charAt(0)
        + " is defined as the number of shortest paths that", null, 0, null);
    sc2.addCodeLine("pass through " + typeName.charAt(0)
        + " divided by the total number of shortest paths from all " + typeName
        + "s.", null, 0, null);
    sc2.addCodeLine("", null, 0, null);
    sc2.addCodeLine("Simplification:", null, 0, null);
    sc2.addCodeLine(
        "The calculation of the centrality requires the shortest paths in the graph which are determined by a",
        null, 0, null);
    sc2.addCodeLine(
        "further algorithm. Shortest path algorithms such as Floyd-Warshall-Algorithm or Dijkstra-Algorithm",
        null, 0, null);
    sc2.addCodeLine(
        "are already available in Animal. Therefore, the shortest path will be displayed but not calculated",
        null, 0, null);
    sc2.addCodeLine("in the following example.", null, 0, null);
    sc2.highlight(0);
    sc2.highlight(4);
    Rect codeRec3 = lang.newRect(new Offset(-5, -5, "definition2", "NW"),
        new Offset(5, 5, "definition2", "SE"), "codRec3", null);
    rprop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    Rect codeRec4 = lang.newRect(new Offset(-3, -3, "definition2", "NW"),
        new Offset(3, 3, "definition2", "SE"), "codRec4", null, rprop);
    lang.nextStep("Definitions");

    sc2.hide();
    codeRec3.hide();
    codeRec4.hide();

    SourceCode sc3 = lang.newSourceCode(new Offset(0, 3, "title", "SW"),
        "definition3", null, sourceCodeProperties);
    sc3.addCodeLine("The betweenness centrality of a " + typeName + " "
        + typeName.charAt(0)
        + " is defined as the number of shortest paths that", null, 0, null);
    sc3.addCodeLine("pass through " + typeName.charAt(0)
        + " divided by the total number of shortest paths from all " + typeName
        + "s.", null, 0, null);
    sc3.addCodeLine("1)	Initialise all links to zero", null, 0, null);
    sc3.addCodeLine("2)	For each node(red):", null, 0, null);
    sc3.addCodeLine(
        "		a.	Finds shortest paths between all pairs of nodes(green)", null, 0,
        null);
    sc3.addCodeLine("	    If a " + typeName
        + " lies on the shortest path increase the counter of this " + typeName
        + " by one.", null, 0, null);
    sc3.addCodeLine("	    If there are multiple shortest paths increase the "
        + typeName + " counter of these " + typeName + "s proportionally.",
        null, 0, null);
    sc3.addCodeLine("3)   Calculating the betweenness centrality of "
        + typeName + " " + typeName.charAt(0) + " by dividing the number of",
        null, 0, null);
    sc3.addCodeLine(
        "     shortest paths that pass through " + typeName.charAt(0)
            + " by the total number of shortest paths in graph g.", null, 0,
        null);
    Rect codeRec5 = lang.newRect(new Offset(-5, -5, "definition3", "NW"),
        new Offset(5, 5, "definition3", "SE"), "codeRec5", null);
    Rect codeRec6 = lang.newRect(new Offset(-3, -3, "definition3", "NW"),
        new Offset(3, 3, "definition3", "SE"), "codeRec6", null);
    sc3.highlight(2);
    TextProperties graphCounterProperties = new TextProperties();
    graphCounterProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
    path_counter = lang.newText(new Offset(0, 10, "definition3", "SW"),
        path_counter_text + 0, "counter", null, graphCounterProperties);

    // Paint Graph
    Node[] nodes = new Node[graph.getSize()];
    String[] labels = new String[graph.getSize()];
    for (int i = 0; i < graph.getSize(); i++) {
      nodes[i] = graph.getNode(i);
      labels[i] = graph.getNodeLabel(i);
    }

    // adjust graph position
    int graph_max_x = ((Coordinates) nodes[0]).getX();
    int graph_min_x = ((Coordinates) nodes[0]).getX();
    int graph_max_y = ((Coordinates) nodes[0]).getY();
    int graph_min_y = ((Coordinates) nodes[0]).getY();
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
    move_y = 280 - graph_min_y;
    for (int i = 0; i < nodes.length; i++) {
      Coordinates tempNode = (Coordinates) nodes[i];
      nodes[i] = new Coordinates(tempNode.getX() + move_x, tempNode.getY()
          + move_y);
    }
    // Set GraphProperties
    graph = lang.newGraph("graph", graph.getAdjacencyMatrix(), nodes, labels,
        null, graph_properties);
    graph.setStartNode(graph.getNode(0));

    df.setMaximumFractionDigits(2);

    if (type == BETWEENNESS_TYPE.NODE_BETWEENNESS) {
      // true:node: hide edgeweight, setup nodecounter
      // initiate textfields(text_node) and values(count_node) for
      // nodecounters
      text_node = new Text[graph.getSize()];
      count_node = new Float[graph.getSize()];
      for (int i = 0; i < graph.getSize(); i++) {
        lang.newPoint(graph.getNode(i), "point_" + i, null,
            new PointProperties()).hide();
        text_node[i] = lang.newText(new Offset(0, -25, "point_" + i, "N"), "0",
            "text_node_" + i, null, new TextProperties());
        count_node[i] = 0f;
      }

    } else {
      // false:link: set edgeweight 0, nodecounter not needed
      // initiate textfields(text_link) and values(count_link) for
      // linkcounters
      text_link = new Text[graph.getSize()][graph.getSize()];
      count_link = new Float[graph.getSize()][graph.getSize()];

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
          count_link[i][j] = 0f;
          count_link[j][i] = 0f;
        }
      }
    }

    // unvisitedNodeMatrix
    lang.nextStep("Pseudocode");
    unvisitedNodeMatrix = lang.newStringMatrix(
        new Offset(30, 0, graph.getName(), "NE"), createUnvisitedMatrix(graph),
        "unvisitedNodeMatrix", null);
    Text unvisitedNodeMatrixText = lang.newText(new Offset(0, -30,
        "unvisitedNodeMatrix", "NW"), "Discovered paths", "unvisitedNodeTitle",
        null);
    sc3.unhighlight(2);
    sc3.highlight(3);
    sc3.highlight(4);
    sc3.highlight(5);
    sc3.highlight(6);
    graph.highlightNode(0, null, null); // highlighting initial startnode

    runGraphAlgo();

    lang.nextStep("Results");
    // Calculate results
    sc3.unhighlight(3);
    sc3.unhighlight(4);
    sc3.unhighlight(5);
    sc3.unhighlight(6);
    sc3.highlight(7);
    sc3.highlight(8);

    //
    // Result highlighting with different intensities
    int delay = 600;
    int step = 600;
    float sum_float = 0;
    float max_value_float = 0;
    float min_value_float = Float.MAX_VALUE;
    float delta_value_float;
    if (type == BETWEENNESS_TYPE.NODE_BETWEENNESS) {
      for (int i = 0; i < count_node.length; i++) {
        sum_float += count_node[i];
        max_value_float = Math.max(max_value_float, count_node[i]);
        min_value_float = Math.min(min_value_float, count_node[i]);
      }
      delta_value_float = (max_value_float / sum_float)
          - (min_value_float / sum_float);
      min_value_float = min_value_float / sum_float;
      max_value_float = max_value_float / sum_float;
      for (int i = 0; i < graph.getSize(); i++) {
        float node_value = (count_node[i] / sum_float) * 100;
        float factor = ((node_value - min_value_float) / (delta_value_float * 100));

        text_node[i].setText(df.format(node_value) + " %", new AnimalTiming(
            delay), new AnimalTiming(step));
        int red_highlight_intensity = (int) (factor * result_highlight_color
            .getRed());
        int green_highlight_intensity = (int) (factor * result_highlight_color
            .getGreen());
        int blue_highlight_intensity = (int) (factor * result_highlight_color
            .getBlue());
        text_node[i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            new Color(red_highlight_intensity, green_highlight_intensity,
                blue_highlight_intensity), new AnimalTiming(delay),
            new AnimalTiming(0));
        delay = delay + step;
      }
    } else {
      for (int i = 0; i < count_link.length; i++) {
        for (int j = i + 1; j < count_link[0].length; j++) {
          sum_float += count_link[i][j];
          max_value_float = Math.max(max_value_float, count_link[i][j]);
          min_value_float = Math.min(min_value_float, count_link[i][j]);
        }
      }
      delta_value_float = (max_value_float / sum_float)
          - (min_value_float / sum_float);
      min_value_float = min_value_float / sum_float;
      max_value_float = max_value_float / sum_float;
      for (int i = 0; i < count_link.length; i++) {
        for (int j = i + 1; j < count_link[0].length; j++) {
          if (graph.getAdjacencyMatrix()[i][j] > 0) {
            float node_value = (count_link[i][j] / sum_float) * 100;
            float factor = ((node_value - min_value_float) / (delta_value_float * 100));
            int red_highlight_intensity = (int) (factor * result_highlight_color
                .getRed());
            int green_highlight_intensity = (int) (factor * result_highlight_color
                .getGreen());
            int blue_highlight_intensity = (int) (factor * result_highlight_color
                .getBlue());
            text_link[i][j].setText(df.format(node_value) + " %",
                new AnimalTiming(delay), new AnimalTiming(step));
            text_link[i][j].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
                new Color(red_highlight_intensity, green_highlight_intensity,
                    blue_highlight_intensity), new AnimalTiming(delay),
                new AnimalTiming(0));
            delay = delay + step;
          }
        }
      }
    }
    // Interpretation
    lang.nextStep();
    sc3.hide();
    codeRec5.hide();
    codeRec6.hide();
    SourceCode sc4 = lang.newSourceCode(new Offset(0, 3, "title", "SW"),
        "interpretation", null, sourceCodeProperties);
    sc4.addCodeLine("Interpretation:", null, 0, null);
    sc4.addCodeLine(
        "Individuals or nodes with a high betweenness centrality have a more intense coloration. High",
        null, 0, null);
    sc4.addCodeLine(
        "betweenness individuals often do not have the shortest average path to everyone else, but they",
        null, 0, null);
    sc4.addCodeLine(
        "have the greatest number of shortest paths that necessarily have to go through them. They are well ",
        null, 0, null);
    sc4.addCodeLine(
        "positioned to perform brokering roles across these clusters in the sense that brokers connect ",
        null, 0, null);
    sc4.addCodeLine(
        "otherwise disconnected people who yet may benefit from an exchange of information.",
        null, 0, null);
    sc4.highlight(0);
    Rect codeRec7 = lang.newRect(new Offset(-5, -5, "interpretation", "NW"),
        new Offset(5, 5, "interpretation", "SE"), "codeRec7", null);
    Rect codeRec8 = lang.newRect(new Offset(-3, -3, "interpretation", "NW"),
        new Offset(3, 3, "interpretation", "SE"), "codeRec8", null);

    lang.nextStep("Interpretation");
    sc4.hide();
    codeRec7.hide();
    codeRec8.hide();
    SourceCode sc5 = lang.newSourceCode(new Offset(0, 3, "title", "SW"),
        "interpretation", null, sourceCodeProperties);
    sc5.addCodeLine("Time complexity:", null, 0, null);
    sc5.addCodeLine(
        "Because of the simplification, the shortest paths were only be shown and not calculated. ",
        null, 0, null);
    sc5.addCodeLine(
        "However, this operation is just very time consuming. Often, the Floyd-Warshall algorithm ",
        null, 0, null);
    sc5.addCodeLine(
        "is used to calculate the shortest paths, which is responsible for the high time ",
        null, 0, null);
    sc5.addCodeLine("complexity of O(N ^ 3). (N = nodes) ", null, 0, null);
    sc5.highlight(0);
    Rect codeRec9 = lang.newRect(new Offset(-5, -5, "interpretation", "NW"),
        new Offset(5, 5, "interpretation", "SE"), "codeRec7", null);
    Rect codeRec10 = lang.newRect(new Offset(-3, -3, "interpretation", "NW"),
        new Offset(3, 3, "interpretation", "SE"), "codeRec8", null);
    lang.nextStep("Time complexity");
  }

  private void runGraphAlgo() {
    df.setMaximumFractionDigits(2);

    lang.nextStep("Run Algorithm");
    for (int k = 0; k < graph.getSize(); k++) { // startnode
      // highlight start node
      lang.addLine("setGridColor \"unvisitedNodeMatrix[" + (k + 1)
          + "][0]\" color (" + matrix_highlight_color.getRed() + ","
          + matrix_highlight_color.getGreen() + ","
          + matrix_highlight_color.getBlue() + ")");
      for (int l = k + 1; l < graph.getSize(); l++) { // targetnode
        Pathfinder eGraph = new Pathfinder(
            makeMatrixSymmetric(graph.getAdjacencyMatrix()));

        // highlight/unhighlight target node
        if (l != k + 1)
          graph.unhighlightNode(l - 1, null, null);
        else
          ; // needed to prevent unhighlighting startnode
        graph.highlightNode(l, null, null);
        lang.addLine("setGridColor \"unvisitedNodeMatrix[0][" + (l + 1)
            + "]\" color (" + matrix_highlight_color.getRed() + ","
            + matrix_highlight_color.getGreen() + ","
            + matrix_highlight_color.getBlue() + ")");

        // get shortest paths between k,l
        List<List<Integer>> directions = eGraph.getDirections(k, l);

        // update found path matrix
        unvisitedNodeMatrix.put(k + 1, l + 1, "" + "☑", null, null);

        Map<Integer, List<String>> queue;
        Float[][] inc;

        // initialise inc array
        if (type == BETWEENNESS_TYPE.NODE_BETWEENNESS)
          inc = new Float[1][graph.getSize()];
        else
          inc = new Float[graph.getSize()][graph.getSize()];
        for (int ii = 0; ii < inc.length; ii++) {
          for (int jj = 0; jj < inc[0].length; jj++) {
            inc[ii][jj] = 0f;
          }
        }

        // generate animation queue for one or *more* shortest paths
        // between two nodes and calculate increase per step
        queue = new HashMap<Integer, List<String>>();

        for (int i = 0; i < directions.size(); i++) {
          for (int j = 0; j < (directions.get(i).size() - 1); j++) {
            if (!queue.containsKey(j)) {
              queue.put(j, new ArrayList<String>());
            }
            if (!queue.get(j).contains(
                directions.get(i).get(j) + ";" + directions.get(i).get(j + 1))) {
              queue.get(j)
                  .add(
                      directions.get(i).get(j) + ";"
                          + directions.get(i).get(j + 1));
            }

            // save values to increase counters
            if (type == BETWEENNESS_TYPE.NODE_BETWEENNESS) {

              inc[0][directions.get(i).get(j)] += 1f;

            } else {
              if (j != directions.get(i).size() - 1) {
                inc[directions.get(i).get(j)][directions.get(i).get(j + 1)] += 1f;
                inc[directions.get(i).get(j + 1)][directions.get(i).get(j)] += 1f;
              }
            }
          }
        }

        if (directions.size() > 1) {
          for (int i = 0; i < inc.length; i++) {
            for (int j = 0; j < inc[0].length; j++) {
              inc[i][j] = inc[i][j] / directions.size();
            }
          }
        }

        // work on animation queue
        List<String> step_i;
        int delay = 600;
        int step = 300;
        DecimalFormat df = new DecimalFormat();
        for (int i = 0; i < queue.size(); i++) {
          step_i = queue.get(i);
          for (int j = 0; j < step_i.size(); j++) {
            graph.highlightEdge(Integer.parseInt(step_i.get(j).split(";")[0]),
                Integer.parseInt(step_i.get(j).split(";")[1]),
                new AnimalTiming(delay), new AnimalTiming(step));

            if (type == BETWEENNESS_TYPE.NODE_BETWEENNESS) { // true:node:
                                                             // increase
                                                             // nodecounter
              int node = Integer.parseInt(step_i.get(j).split(";")[0]);
              // dont increase start/end node counters
              if (node == k || node == l)
                ;
              else {
                float oldCounter = count_node[node];
                float add = inc[0][node];
                float count = oldCounter + add;

                // update counters
                text_node[node].setText(df.format(count), new AnimalTiming(
                    delay), new AnimalTiming(step));
                count_node[node] = count;
              }
            } else { // false:link: increase edgeweight
              int from = Integer.parseInt(step_i.get(j).split(";")[0]);
              int to = Integer.parseInt(step_i.get(j).split(";")[1]);

              // update counters, visual and intern
              float oldCounter = count_link[from][to];
              float add = inc[from][to];
              float newCounter = oldCounter + add;

              text_link[from][to].setText("" + df.format(newCounter),
                  new AnimalTiming(delay), new AnimalTiming(step));
              text_link[to][from].setText("" + df.format(newCounter),
                  new AnimalTiming(delay), new AnimalTiming(step));
              count_link[from][to] = newCounter;
              count_link[to][from] = newCounter;

            }// end of type
          }
          delay = delay + step;
        }
        float path_counter_float = 0;
        if (type == BETWEENNESS_TYPE.NODE_BETWEENNESS) {
          for (int i = 0; i < count_node.length; i++)
            path_counter_float = path_counter_float + count_node[i];
        } else {
          for (int i = 0; i < count_link.length; i++)
            for (int j = i + 1; j < count_link[i].length; j++)
              path_counter_float = path_counter_float + count_link[i][j];
        }
        path_counter.setText(path_counter_text + df.format(path_counter_float),
            null, null);

        // unhighlight all edges
        for (int x = 0; x < graph.getAdjacencyMatrix().length; x++)
          for (int y = x + 1; y < graph.getAdjacencyMatrix()[0].length; y++) {
            if (graph.getAdjacencyMatrix()[x][y] != 0)
              graph.unhighlightEdge(x, y, null, null);
          }
        lang.nextStep();

        lang.addLine("setGridColor \"unvisitedNodeMatrix[0][" + (l + 1)
            + "]\" color black");
      }

      // highlighting/unhighlighting startnode
      lang.nextStep();
      lang.addLine("setGridColor \"unvisitedNodeMatrix[" + (k + 1)
          + "][0]\" color black");
      graph.unhighlightNode(k, null, null);
      if (k < graph.getSize() - 1)
        graph.highlightNode(k + 1, null, null);
      else
        ;
      graph.unhighlightNode(graph.getSize() - 1, null, null);

    }

  }

  private String[][] createUnvisitedMatrix(Graph graph) {
    int numOfNodes = graph.getSize();
    String[][] unviMatrix = new String[numOfNodes + 1][numOfNodes + 1];
    for (int i = 0; i <= numOfNodes; i++) {
      for (int j = 0; j <= numOfNodes; j++) {
        if (i == 0 && j != 0) {
          unviMatrix[0][j] = graph.getNodeLabel(j - 1);
        } else if (j == 0 && i != 0) {
          unviMatrix[i][0] = graph.getNodeLabel(i - 1);
        } else if (i < j) {
          unviMatrix[i][j] = "0";
        } else {
          unviMatrix[i][j] = " ";
        }
      }

    }
    return unviMatrix;
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
}

class AnimalTiming extends Timing {

  public AnimalTiming(int theDelay) {
    super(theDelay);
  }

  @Override
  public String getUnit() {
    return "ms";
  }

}