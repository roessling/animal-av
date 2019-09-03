package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Stack;

import algoanim.primitives.Graph;
import algoanim.primitives.Point;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpers.MyEdge;
import generators.graph.helpers.MyNode;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.exceptions.IllegalDirectionException;

public class GeneratorKruskalAlgo implements Generator {

  private Language                lang;
  private TextProperties          edgeAcceptProp;
  private TextProperties          edgeRejectProp;
  private TextProperties          edgeWeightColorProp;
  private SourceCodeProperties    highlightAlgoProp;
  private ArrayProperties         tableProp;

  private String[]                nodesInput;
  private String[]                edgesInput;

  private int                     greenCounter    = 0;

  private ArrayList<MyNode>       path            = new ArrayList<MyNode>();
  private ArrayList<MyNode>       newPath         = new ArrayList<MyNode>();

  private Stack<MyNode>           STACK           = new Stack<MyNode>();

  public HashMap<String, Integer> nodeNumber      = new HashMap<String, Integer>();
  public HashMap<String, Integer> nodeNumberGreen = new HashMap<String, Integer>();
  public HashMap<String, Integer> nodeNumberRed   = new HashMap<String, Integer>();

  public ArrayList<MyNode>        nodeList        = new ArrayList<MyNode>();
  public ArrayList<MyNode>        nodeListRed     = new ArrayList<MyNode>();
  public ArrayList<MyNode>        nodeListAcc     = new ArrayList<MyNode>();
  public ArrayList<MyNode>        nodeListAccRed  = new ArrayList<MyNode>();
  public ArrayList<MyEdge>        edgeListL       = new ArrayList<MyEdge>();
  public ArrayList<MyEdge>        edgeListAcc     = new ArrayList<MyEdge>();
  public ArrayList<MyEdge>        newEdgeListAcc  = new ArrayList<MyEdge>();
  public ArrayList<MyEdge>        tmpRedEdges     = new ArrayList<MyEdge>();

  @Override
  public void init() {
    lang = new AnimalScript(
        "Kruskal's Algorithm [DE]",
        "Wladimir Schmidt <wschmidt@rbg.informatik.tu-darmstadt.de>, Peter Jelesnak <jelesnak@rbg.informatik.tu-darmstadt.de>",
        800, 600);

    lang.addLine("");
    lang.setStepMode(true);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    nodesInput = (String[]) primitives.get("nodesInput");
    edgesInput = (String[]) primitives.get("edgesInput");

    edgeAcceptProp = (TextProperties) props
        .getPropertiesByName("edgeAcceptProp");
    edgeRejectProp = (TextProperties) props
        .getPropertiesByName("edgeRejectProp");
    edgeWeightColorProp = (TextProperties) props
        .getPropertiesByName("edgeWeightColorProp");
    highlightAlgoProp = (SourceCodeProperties) props
        .getPropertiesByName("highlightAlgoProp");
    tableProp = (ArrayProperties) props.getPropertiesByName("tableProp");

    ArrayList<String> nodesInputNew = new ArrayList<String>();
    ArrayList<String> edgesInputNew = new ArrayList<String>();

    for (int i = 0; i < nodesInput.length; i++)
      nodesInputNew.add(nodesInput[i]);

    for (int i = 0; i < edgesInput.length; i++)
      edgesInputNew.add(edgesInput[i]);

    generateAnimation(nodesInputNew, edgesInputNew);

    return lang.toString();
  }

  public void generateAnimation(ArrayList<String> pNodesInput,
      ArrayList<String> pEdgesInput) {

    parseInput(pNodesInput, pEdgesInput);
    // paintGraph(nodeList, edgeList, "graph", graphProp);

    // STEP 1
    // lang.addLine("location \"mitte\" at  ( 628 , 250 )");
    PointProperties mitteProps = new PointProperties();
    mitteProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    Point mitte = this.lang.newPoint(new Coordinates(628, 250), "mitte", null,
        mitteProps);

    // location \"gS\" offset ( 374 , -150 ) from \"mitte\"
    PointProperties gSProps = new PointProperties();
    gSProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    Point gS = this.lang.newPoint(new Offset(200, -200, mitte,
        AnimalScript.DIRECTION_S), "gS", null, gSProps);

    // location \"gS1\" offset ( -374 , -150 ) from \"mitte\"
    PointProperties gS1Props = new PointProperties();
    gS1Props.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    Point gS1 = this.lang.newPoint(new Offset(-200, -200, mitte,
        AnimalScript.DIRECTION_S), "gS1", null, gS1Props);

    // line "mline" ( 628 , 225 ) ( 628 , 50 ) color black hidden
    PolylineProperties lineProps = new PolylineProperties();
    lineProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    Node[] nodes = new Node[2];
    Node node0 = new Coordinates(628, 225);
    Node node1 = new Coordinates(628, 50);
    nodes[0] = node0;
    nodes[1] = node1;

    Polyline hiddenLine1 = lang.newPolyline(nodes, "mline", null, lineProps);

    // text "header" "Kruskal's Algorithmus" offset ( -150 , -25 ) from "mitte"
    // color green font SansSerif size 30
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 30));
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);

    Text headerTxt = new Text(new AnimalTextGenerator(lang), new Offset(-150,
        -35, hiddenLine1, AnimalScript.DIRECTION_S), "Kruskal Algorithmus",
        "header", null, headerProps);

    lang.addItem(headerTxt);

    // lang.addLine("text \"footer\" \"(c) TU Darmstadt 2011\" offset ( 480 , 220 ) from \"mitte\" color black font SansSerif size 12");
    TextProperties footerProps = new TextProperties();
    footerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
    footerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    Text footerTxt = new Text(new AnimalTextGenerator(lang), new Offset(480,
        250, hiddenLine1, AnimalScript.DIRECTION_S), "(c) TU Darmstadt 2011",
        "footer", null, footerProps);

    lang.addItem(footerTxt);

    int boldOrPlain = ((Boolean) (highlightAlgoProp.get("bold")) ? Font.BOLD
        : Font.PLAIN);

    lang.nextStep();

    // move \"header\" type \"translate\" via \"mline\" after 20 ticks within 80
    // ticks
    Timing headerDelay = new Timing(20) {

      @Override
      public String getUnit() {
        return "ticks";
      }
    };

    Timing headerAnimationTime = new Timing(80) {

      @Override
      public String getUnit() {

        return "ticks";
      }
    };

    try {
      headerTxt.moveVia(AnimalScript.DIRECTION_N, "translate", hiddenLine1,
          headerDelay, headerAnimationTime);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    // text \"desc1\" \"Algorithmus von Kruskal ist ein Algorithmus der
    // Graphentheorie\" offset (-250, -20) from \"mitte\" color black size 20
    // after 150 ticks
    TextProperties descProps = new TextProperties();
    descProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 20));
    descProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    Text descTxt1 = new Text(new AnimalTextGenerator(lang), new Offset(-320,
        -5, hiddenLine1, AnimalScript.DIRECTION_S),
        "Algorithmus von Kruskal ist ein Algorithmus der Graphentheorie",
        "desc1", null, descProps);

    // text \"desc2\" \"zur Berechnung minimaler Spannbaeume von ungerichteten
    // Graphen.\" offset (-250, -5) from \"mitte\" color black size 20 after 150
    // ticks
    Text descTxt2 = new Text(new AnimalTextGenerator(lang), new Offset(-320,
        10, hiddenLine1, AnimalScript.DIRECTION_S),
        "zur Berechnung minimaler Spannbaeume von ungerichteten Graphen.",
        "desc2", null, descProps);

    lang.addItem(descTxt1);
    lang.addItem(descTxt2);

    lang.nextStep();

    // STEP 3
    descTxt1.hide();
    descTxt2.hide();

    // text \"anfang\" \"Anfangsgraph\" offset ( -70 , -150) from \"mitte\"
    // color black size 18
    TextProperties anfangProps = new TextProperties();
    anfangProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));
    anfangProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    Text anfangTxt = new Text(new AnimalTextGenerator(lang), new Offset(-70,
        -150, hiddenLine1, AnimalScript.DIRECTION_S), "Anfangsgraph", "anfang",
        null, anfangProps);

    lang.addItem(anfangTxt);

    // graph "graph" size 9 bgColor gray outlineColor black highlightColor red
    // nodeFontColor white weighted nodes { "A" offset (0,0) from "gS" , "B"
    // offset (-125,25) from "gS", "C" offset (125,25) from "gS", "D" offset
    // (-90,150) from "gS", "E" offset (0,150) from "gS", "F" offset (90,150)
    // from "gS", "G" offset (-125,275) from "gS", "H" offset (125,275) from
    // "gS", "J" offset (0,300) from "gS"} edges {(0,1,"9"), (2,0,"3"),
    // (0,4,"6"), (1,3,"16"), (4,1,"3"), (2,4,"13"), (2,5,"1"), (4,3,"7"),
    // (3,6,"1"), (5,4,"1"), (4,6,"14"), (7,4,"18"), (4,8,"8"), (7,5,"5"),
    // (8,6,"9"), (7,8,"11")}
    GraphProperties graphProp = new GraphProperties();
    graphProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    graphProp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
    graphProp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
    graphProp.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY,
        (Color) edgeWeightColorProp.get("color"));

    Graph graph = constructGraph(nodeList, edgeListL, "initGraph", graphProp,
        "gray", "gS");
    Graph firstGraph = constructGraph(nodeList, edgeListL, "firstGraph",
        graphProp, "gray", "gS1");
    firstGraph.hide();

    lang.nextStep();

    // STEP 4
    // hide "anfang"
    anfangTxt.hide();

    // grid "tabelle" offset (-548, -150) from "mitte" lines 17 columns 3 style
    // table cellWidth 100 maxCellWidth 150 cellHeight 15 maxCellHeight 15
    // fillColor white font Serif size 16 bold
    // eine Grid-Tabelle existiert nicht in API von Animal

    String tableFillColor = "(" + ((Color) tableProp.get("fillColor")).getRed()
        + ", " + ((Color) tableProp.get("fillColor")).getGreen() + ", "
        + ((Color) tableProp.get("fillColor")).getBlue() + ")";

    String tableTextColor = "(" + ((Color) tableProp.get("color")).getRed()
        + ", " + ((Color) tableProp.get("color")).getGreen() + ", "
        + ((Color) tableProp.get("color")).getBlue() + ")";

    String tableFont = ((Font) tableProp.get("font")).getFamily();

    int tableSize = edgeListL.size() + 1;
    lang.addLine("grid \"tabelle\" (80,100) lines "
        + tableSize
        + " columns 3 style table cellWidth 100 maxCellWidth 150 cellHeight 15 maxCellHeight 15 textColor "
        + tableTextColor + " fillColor " + tableFillColor + " font "
        + tableFont + " size 16 bold");
    lang.addLine("setGridValue \"tabelle [0][0]\" \"Kante\"");
    lang.addLine("setGridValue \"tabelle [0][1]\" \"Kantegewicht\"");
    lang.addLine("setGridValue \"tabelle [0][2]\" \"Status\"");

    SourceCodeProperties someTxtProps = new SourceCodeProperties();
    someTxtProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    someTxtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.PLAIN, 18));
    someTxtProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.BLACK);

    SourceCode someTxt = lang.newSourceCode(new Offset(-150, -80, hiddenLine1,
        AnimalScript.DIRECTION_S), "Schritt1", null, someTxtProps);

    someTxt.addCodeLine("In erstem Schritt werden alle Kanten", null, 0, null);
    someTxt.addCodeLine("aus dem Graph entfernt und aufsteigend ", null, 0,
        null);
    someTxt.addCodeLine("sortiert in eine Tabelle eingefuegt.", null, 0, null);
    someTxt.addCodeLine("", null, 0, null);
    someTxt.addCodeLine("Dann werden die Kanten nacheinander ", null, 0, null);
    someTxt.addCodeLine("in den Graph eingefuegt, beginnend ", null, 0, null);
    someTxt.addCodeLine("mit den kuerzesten, solange es keinen", null, 0, null);
    someTxt.addCodeLine("Zyklus innerhalb des Graphs ergibt.", null, 0, null);
    someTxt.addCodeLine("", null, 0, null);
    someTxt.addCodeLine("Das Algorithmus ist beendet, wenn alle ", null, 0,
        null);
    someTxt.addCodeLine("Kanten durchgegangen wurden.", null, 0, null);

    lang.nextStep();

    // STEP 5

    someTxt.hide();

    // codeGroup "pseudocode" offset (-150 , -120) from "mitte" color black
    // highlightColor magenta contextColor red font Serif size 15 bold
    SourceCodeProperties pseudocodeProps = new SourceCodeProperties();
    pseudocodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.RED);
    pseudocodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        ((Font) highlightAlgoProp.get("font")).getFamily(), boldOrPlain,
        (Integer) highlightAlgoProp.get("size")));
    pseudocodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        (Color) highlightAlgoProp.get("highlightColor"));
    pseudocodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        (Color) highlightAlgoProp.get("color"));

    SourceCode pseudoCode = lang.newSourceCode(new Offset(-170, -80,
        hiddenLine1, AnimalScript.DIRECTION_S), "pseudocode", null,
        pseudocodeProps);

    pseudoCode.addCodeLine("G = (V,E,w) - ein ungerichteter,", null, 0, null);
    pseudoCode.addCodeLine("gewichteter Graph mit E':= {}, L:= E", null, 0,
        null);
    pseudoCode.addCodeLine(
        "Sortiere alle Kanten in L nach aufsteigendem Gewicht", null, 0, null);
    pseudoCode.addCodeLine("", null, 0, null);
    pseudoCode.addCodeLine("while L != {}", null, 0, null);
    pseudoCode.addCodeLine("waehle eine Kante e von L mit kleinstem Gewicht",
        null, 1, null);
    pseudoCode.addCodeLine("loesche die Kante e aus L", null, 1, null);
    pseudoCode.addCodeLine("wenn der neuen Graph (V,E' + neue Kante {e})",
        null, 1, null);
    pseudoCode.addCodeLine("keinen Kreis enthaelt,", null, 1, null);
    pseudoCode.addCodeLine("dann E':= E' + {e}", null, 2, null);
    pseudoCode.addCodeLine("M = (V,E') - ein minimaler Spannbaum von G", null,
        0, null);

    SourceCodeProperties leTextProps = new SourceCodeProperties();
    leTextProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        (Color) highlightAlgoProp.get("highlightColor"));
    leTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        ((Font) highlightAlgoProp.get("font")).getFamily(), boldOrPlain,
        (Integer) highlightAlgoProp.get("size")));
    leTextProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        (Color) highlightAlgoProp.get("highlightColor"));
    leTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        (Color) highlightAlgoProp.get("color"));

    SourceCode leText = lang.newSourceCode(new Offset(-170, 160, hiddenLine1,
        AnimalScript.DIRECTION_S), "leText", null, leTextProps);

    String[] edgesForL = new String[edgeListL.size()];

    for (int i = 0; i < edgeListL.size(); i++) {
      edgesForL[i] = "(" + edgeListL.get(i).getNode1().getName() + ","
          + edgeListL.get(i).getNode2().getName() + ")";
    }

    leText = printEdgesL(edgesForL, leText, leTextProps, hiddenLine1, false);

    String[] edgesForE = null;

    printEdgesE(edgesForE, leText);

    highlightPseudocode(new int[] { 0, 1 }, null, pseudoCode);

    lang.nextStep();

    // STEP 6

    graphProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    graphProp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
    graphProp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
    graphProp.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY,
        (Color) edgeWeightColorProp.get("color"));

    graph.hide();

    graph = constructGraph(nodeList, edgeListAcc, "graphGrey", graphProp,
        "gray", "gS");

    for (int i = 0; i < edgeListL.size(); i++)
      for (int j = 0; j < edgeListL.size(); j++)
        if (edgeListL.get(i).getWeight() < edgeListL.get(j).getWeight()) {
          MyEdge tmp = edgeListL.get(i);
          edgeListL.set(i, edgeListL.get(j));
          edgeListL.set(j, tmp);
        }

    // for(int i=0; i<edgeListL.size(); i++){
    // System.out.println(edgeListL.get(i).getNode1().getName() +" "+
    // edgeListL.get(i).getNode2().getName()
    // +" "+ edgeListL.get(i).getWeight());
    // }

    for (int i = 0; i < edgeListL.size(); i++) {
      edgesForL[i] = "(" + edgeListL.get(i).getNode1().getName() + ","
          + edgeListL.get(i).getNode2().getName() + ")";

      int j = i + 1;
      lang.addLine("setGridValue \"tabelle [" + j + "][0]\" \"" + edgesForL[i]
          + "\"");
      lang.addLine("setGridValue \"tabelle [" + j + "][1]\" \""
          + edgeListL.get(i).getWeight() + "\"");
    }

    leText = printEdgesL(edgesForL, leText, leTextProps, hiddenLine1, false);

    printEdgesE(edgesForE, leText);

    highlightPseudocode(new int[] { 2 }, new int[] { 0, 1 }, pseudoCode);
    lang.nextStep();

    // STEP 7

    int tableCounter = 1;
    // int arrowStep = 22;

    while (edgeListL.size() != 0) {

      highlightPseudocode(new int[] { 4 }, new int[] { 2 }, pseudoCode);
      lang.nextStep();

      // polyline "arrow" offset (-220, -112) from "mitte" move (35, 0) color
      // black bwArrow
      // PolylineProperties arrowLineProps = new PolylineProperties();
      // arrowLineProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
      // arrowLineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
      // Color.BLACK);
      // arrowLineProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
      //
      // nodes = new Node[2];
      // node0 = new Offset(-220, (tableCounter-1)*arrowStep, hiddenLine1,
      // AnimalScript.DIRECTION_W);
      // node1 = new Offset(-185, (tableCounter-1)*arrowStep, hiddenLine1,
      // AnimalScript.DIRECTION_W);
      // nodes[0] = node0;
      // nodes[1] = node1;
      //
      // Polyline arrowLine = new Polyline(new AnimalPolylineGenerator(lang),
      // nodes, "arrow", null, arrowLineProps);

      leText = printEdgesL(edgesForL, leText, leTextProps, hiddenLine1, true);
      printEdgesE(edgesForE, leText);

      // CYCLE DETECTION

      highlightPseudocode(new int[] { 5 }, new int[] { 4 }, pseudoCode);

      lang.nextStep();

      // STEP 8

      dfs(edgeListL.get(0).getNode1(), edgeListL.get(0).getNode2());

      for (int i = 0; i < edgeListAcc.size(); i++) {
        edgeListAcc.get(i).getNode1().setVisited(false);
        edgeListAcc.get(i).getNode2().setVisited(false);
      }

      // GREEN

      graphProp = new GraphProperties();
      graphProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
          (Color) edgeAcceptProp.get("color"));
      graphProp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
      graphProp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
      graphProp.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY,
          (Color) edgeWeightColorProp.get("color"));

      if (!nodeNumberGreen.containsKey(nodeList.get(nodeNumber.get(edgeListL
          .get(0).getNode1().getName())))) {
        nodeListAcc.add(nodeList.get(nodeNumber.get(edgeListL.get(0).getNode1()
            .getName())));
        nodeNumberGreen.put(
            nodeList.get(nodeNumber.get(edgeListL.get(0).getNode1().getName()))
                .getName(), greenCounter);
        nodeList.remove(nodeNumber.get(edgeListL.get(0).getNode1().getName()));

        greenCounter++;
      }

      if (!nodeNumberGreen.containsKey(nodeList.get(nodeNumber.get(edgeListL
          .get(0).getNode2().getName())))) {
        nodeListAcc.add(nodeList.get(nodeNumber.get(edgeListL.get(0).getNode2()
            .getName())));
        nodeNumberGreen.put(
            nodeList.get(nodeNumber.get(edgeListL.get(0).getNode2().getName()))
                .getName(), greenCounter);
        nodeList.remove(nodeNumber.get(edgeListL.get(0).getNode2().getName()));

        greenCounter++;
      }

      // IF CYCLE, dann hier weiter, sonst alles ok.
      if (!newPath.isEmpty()) {

        for (int i = 0; i < edgeListAcc.size(); i++)
          newEdgeListAcc.add(edgeListAcc.get(i));

        newEdgeListAcc.add(edgeListL.get(0));

        ArrayList<MyNode> tmpN = new ArrayList<MyNode>();
        for (int i = 0; i < newPath.size(); i++)
          tmpN.add(newPath.get(i));

        while (!tmpN.isEmpty()) {
          MyNode gNode = tmpN.get(0);
          tmpN.remove(0);
          for (int i = 0; i < tmpN.size(); i++) {
            getEdge(gNode, tmpN.get(i));
          }
        }

        for (int i = 0; i < newPath.size(); i++) {
          nodeNumberRed.put(newPath.get(i).getName(), i);
        }

      } else {
        edgeListAcc.add(edgeListL.get(0));
      }

      edgeListL.remove(0);

      highlightPseudocode(new int[] { 6 }, new int[] { 5 }, pseudoCode);

      edgesForL = new String[edgeListL.size()];

      for (int i = 0; i < edgeListL.size(); i++) {
        edgesForL[i] = "(" + edgeListL.get(i).getNode1().getName() + ","
            + edgeListL.get(i).getNode2().getName() + ")";
      }

      leText = printEdgesL(edgesForL, leText, leTextProps, hiddenLine1, false);

      printEdgesE(edgesForE, leText);

      lang.nextStep();

      // NEXT STEP

      graph.hide();

      highlightPseudocode(new int[] { 7, 8 }, new int[] { 6 }, pseudoCode);

      // if(edge)

      constructGraph(nodeListAcc, edgeListAcc, "graphGreen", graphProp,
          "green", "gS");

      graphProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
      graphProp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
      graphProp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
      graphProp.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY,
          (Color) edgeWeightColorProp.get("color"));

      graph = constructGraph(nodeList, new ArrayList<MyEdge>(), "graphGrey",
          graphProp, "gray", "gS");

      graphProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
          (Color) edgeRejectProp.get("color"));
      graphProp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
      graphProp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
      graphProp.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY,
          (Color) edgeWeightColorProp.get("color"));

      // RED GRAPH, same variable name as green graph(?)
      Graph graph3 = constructGraph(newPath, tmpRedEdges, "graphRed",
          graphProp, "red", "gS");

      if (newPath.isEmpty()) {

        String tableAcceptTextColor = "("
            + ((Color) edgeAcceptProp.get("color")).getRed() + ", "
            + ((Color) edgeAcceptProp.get("color")).getGreen() + ", "
            + ((Color) edgeAcceptProp.get("color")).getBlue() + ")";

        lang.addLine("setGridColor \"tabelle [" + tableCounter
            + "][2]\" color " + tableAcceptTextColor + "");
        lang.addLine("setGridValue \"tabelle [" + tableCounter
            + "][2]\" \"accept\"");

        // polyline "arrow" offset (-220, -112) from "mitte" move (35, 0) color
        // green bwArrow
        // arrowLineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        // Color.GREEN);
        //
        // arrowLine = new Polyline(new AnimalPolylineGenerator(lang), nodes,
        // "arrow", null, arrowLineProps);
      } else {

        String tableRejectTextColor = "("
            + ((Color) edgeRejectProp.get("color")).getRed() + ", "
            + ((Color) edgeRejectProp.get("color")).getGreen() + ", "
            + ((Color) edgeRejectProp.get("color")).getBlue() + ")";

        lang.addLine("setGridColor \"tabelle [" + tableCounter
            + "][2]\" color " + tableRejectTextColor + "");
        lang.addLine("setGridValue \"tabelle [" + tableCounter
            + "][2]\" \"reject\"");

        // polyline "arrow" offset (-220, -112) from "mitte" move (35, 0) color
        // green bwArrow
        // arrowLineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        // Color.RED);
        //
        // arrowLine = new Polyline(new AnimalPolylineGenerator(lang), nodes,
        // "arrow", null, arrowLineProps);
      }

      lang.nextStep();

      // hide red graph

      graph3.hide();

      leText = printEdgesL(edgesForL, leText, leTextProps, hiddenLine1, false);

      edgesForE = new String[edgeListAcc.size()];

      for (int i = 0; i < edgeListAcc.size(); i++) {
        edgesForE[i] = "(" + edgeListAcc.get(i).getNode1().getName() + ","
            + edgeListAcc.get(i).getNode2().getName() + ")";
      }

      printEdgesE(edgesForE, leText);

      if (newPath.isEmpty()) {
        highlightPseudocode(new int[] { 9 }, new int[] { 7, 8 }, pseudoCode);

        lang.nextStep();

        highlightPseudocode(new int[] {}, new int[] { 9 }, pseudoCode);
      } else {
        highlightPseudocode(null, new int[] { 7, 8 }, pseudoCode);
      }

      newEdgeListAcc.clear();
      tmpRedEdges.clear();
      path.clear();
      newPath.clear();

      // arrowLine.hide();

      tableCounter++;
    }

    highlightPseudocode(new int[] { 10 }, new int[] { 7, 8, 9 }, pseudoCode);

    lang.nextStep();

    lang.addLine("hide \"tabelle\"");
    pseudoCode.hide();
    graph.hide();
    leText.hide();

    // text "id" "Minimaler Spannbaum von G" offset (-110 , 350 ) from "gS"
    // color black size 18
    TextProperties conTxtProps = new TextProperties();
    conTxtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));
    conTxtProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    Text conTxt = new Text(new AnimalTextGenerator(lang), new Offset(-110, 450,
        gS, AnimalScript.DIRECTION_S), "Minimaler Spannbaum von G", "id", null,
        conTxtProps);

    lang.addItem(conTxt);

    // text "id1" "Graph G" offset (-25 , 350 ) from "gS1" color black size 18
    conTxt = new Text(new AnimalTextGenerator(lang), new Offset(-70, 450, gS1,
        AnimalScript.DIRECTION_S), "Graph G", "id1", null, conTxtProps);

    lang.addItem(conTxt);

    // GraphProperties graphProp = new GraphProperties();
    graphProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    graphProp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
    graphProp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
    graphProp.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY,
        (Color) edgeWeightColorProp.get("color"));

    firstGraph.show();

  }

  private SourceCode printEdgesL(String[] array, SourceCode leText,
      SourceCodeProperties scProp, Polyline hiddenLine1,
      boolean notFirstTimeShown) {

    leText.hide();

    SourceCode leTextRet = lang.newSourceCode(new Offset(-170, 160,
        hiddenLine1, AnimalScript.DIRECTION_S), "leText", null, scProp);

    boolean wasUp = false;

    leTextRet.addCodeLine("L = {", null, 0, null);

    if (array != null) {

      for (int i = 0; i < array.length; i++) {

        if (i % 8 == 7) {
          wasUp = true;

          leTextRet.addCodeElement(array[i], "leText", 0, null);

        } else {

          if (wasUp) {
            // zeilenumbruch, neue zeile erstellt
            leTextRet.addCodeLine(array[i], null, 3, null);
            // leText.addCodeElement(", ", "leText", true, 0, null);
            wasUp = false;
          } else {
            leTextRet.addCodeElement(array[i], "leText", 0, null);
            // leText.addCodeElement(", ", "leText", true, 0, null);
          }

        }
      }
    }
    leTextRet.addCodeElement("}", "leText", 0, null);

    if (notFirstTimeShown) {
      leTextRet.highlight(0, 1, true);
    }

    return leTextRet;
  }

  private void printEdgesE(String[] array, SourceCode leText) {

    boolean wasUp = false;

    leText.addCodeLine("E' = {", null, 0, null);

    if (array != null) {

      for (int i = 0; i < array.length; i++) {

        if (i % 8 == 7) {
          wasUp = true;

          leText.addCodeElement(array[i], "leText", 0, null);

        } else {

          if (wasUp) {
            leText.addCodeLine(array[i], null, 3, null);
            wasUp = false;
          } else {
            leText.addCodeElement(array[i], "leText", 0, null);
          }

        }

      }
    }

    leText.addCodeElement("}", "leText", 0, null);

  }

  private void dfs(MyNode from, MyNode to) {

    if (from.getName().equals(to.getName())) {

      path.add(from);
      // System.out.println("CYCLE");

      if (!newPath.containsAll(path))
        newPath.addAll(path);
    }

    from.setVisited(true);

    path.add(from);

    ArrayList<MyNode> checkNeighbours = findNeighbours(from);

    if (checkNeighbours.size() != 0) {

      STACK.add(from);
      STACK.addAll(checkNeighbours);

      // String tt = "stack: ";
      // for(int i=0; i<STACK.size(); i++)
      // tt += STACK.get(i).getName()+ " ";
      // System.out.println(tt);

      while (!STACK.isEmpty()) {
        MyNode tmpNode = STACK.pop();
        if (tmpNode.getName().equals(from.getName())) {
          path.remove(from);
        } else {
          tmpNode.setVisited(true);
          dfs(tmpNode, to);
        }
      }
    }

    else
      path.remove(from);

  }

  private void getEdge(MyNode n1, MyNode n2) {

    for (int i = 0; i < newEdgeListAcc.size(); i++) {

      if ((n1.getName().equals(newEdgeListAcc.get(i).getNode1().getName()) && n2
          .getName().equals(newEdgeListAcc.get(i).getNode2().getName()))
          || (n1.getName().equals(newEdgeListAcc.get(i).getNode2().getName()) && n2
              .getName().equals(newEdgeListAcc.get(i).getNode1().getName()))) {
        tmpRedEdges.add(newEdgeListAcc.get(i));
      }
    }
  }

  private ArrayList<MyNode> findNeighbours(MyNode start) {

    ArrayList<MyNode> result = new ArrayList<MyNode>();

    for (int i = 0; i < edgeListAcc.size(); i++) {

      if (start.getName().equals(edgeListAcc.get(i).getNode1().getName())
          && !result.contains(edgeListAcc.get(i).getNode2())
          && !edgeListAcc.get(i).getNode2().isVisited()) {

        result.add(edgeListAcc.get(i).getNode2());
      }

      if (start.getName().equals(edgeListAcc.get(i).getNode2().getName())
          && !result.contains(edgeListAcc.get(i).getNode1())
          && !edgeListAcc.get(i).getNode1().isVisited()) {

        result.add(edgeListAcc.get(i).getNode1());
      }
    }

    return result;
  }

  private Graph constructGraph(ArrayList<MyNode> nodeList,
      ArrayList<MyEdge> edgeList, String graphName, GraphProperties graphProp,
      String color, String offset) {

    int nodeListLen = nodeList.size();
    int[][] adjacencyMatrix = new int[nodeListLen][nodeListLen];

    // String tt = "edgeList: ";
    // for(int i=0; i<edgeList.size(); i++){
    // tt += " "+edgeList.get(i).getNode1().getName() +
    // " "+edgeList.get(i).getNode2().getName();
    // }
    //
    // System.out.println(tt);

    if (edgeList.size() > 0) {
      for (int i = 0; i < edgeList.size(); i++) {
        if (color.equals("gray"))
          adjacencyMatrix[nodeNumber.get(edgeList.get(i).getNode1().getName())][nodeNumber
              .get(edgeList.get(i).getNode2().getName())] = 1;
        else if (color.equals("green"))
          adjacencyMatrix[nodeNumberGreen.get(edgeList.get(i).getNode1()
              .getName())][nodeNumberGreen.get(edgeList.get(i).getNode2()
              .getName())] = 1;
        else if (color.equals("red"))
          adjacencyMatrix[nodeNumberRed.get(edgeList.get(i).getNode1()
              .getName())][nodeNumberRed.get(edgeList.get(i).getNode2()
              .getName())] = 1;

      }
    }

    Node[] graphNodes = new Node[nodeListLen];
    String[] labels = new String[nodeListLen];

    for (int i = 0; i < nodeListLen; i++) {
      if (offset.equals("gS"))
        graphNodes[i] = new Offset(nodeList.get(i).getCoordinates().getX(),
            nodeList.get(i).getCoordinates().getY(), offset,
            AnimalScript.DIRECTION_C);
      else if (offset.equals("gS1"))
        graphNodes[i] = new Offset(
            nodeList.get(i).getCoordinates().getX() - 400, nodeList.get(i)
                .getCoordinates().getY(), offset, AnimalScript.DIRECTION_C);

      labels[i] = nodeList.get(i).getName();
    }

    Graph graph = this.lang.newGraph(graphName, adjacencyMatrix, graphNodes,
        labels, null, graphProp);

    if (edgeList.size() > 0) {
      for (int i = 0; i < edgeList.size(); i++) {
        if (color.equals("gray"))
          graph.setEdgeWeight(nodeNumber.get(edgeList.get(i).getNode1()
              .getName()),
              nodeNumber.get(edgeList.get(i).getNode2().getName()), edgeList
                  .get(i).getWeight(), null, null);

        else if (color.equals("green"))
          graph.setEdgeWeight(
              nodeNumberGreen.get(edgeList.get(i).getNode1().getName()),
              nodeNumberGreen.get(edgeList.get(i).getNode2().getName()),
              edgeList.get(i).getWeight(), null, null);
        else if (color.equals("red"))
          graph.setEdgeWeight(nodeNumberRed.get(edgeList.get(i).getNode1()
              .getName()), nodeNumberRed.get(edgeList.get(i).getNode2()
              .getName()), edgeList.get(i).getWeight(), null, null);
      }
    }

    return graph;

  }

  private void parseInput(ArrayList<String> pNodeInput,
      ArrayList<String> pEdgeInput) {

    for (int i = 0; i < pNodeInput.size(); i++) {

      MyNode node1 = new MyNode();

      node1.setName(pNodeInput.get(i).substring(0,
          pNodeInput.get(i).trim().indexOf("#")));
      node1.setCoordinates(new Coordinates(Integer.parseInt(pNodeInput.get(i)
          .substring(pNodeInput.get(i).trim().indexOf("#") + 1,
              pNodeInput.get(i).trim().lastIndexOf("#"))), (Integer
          .parseInt(pNodeInput.get(i).substring(
              pNodeInput.get(i).trim().lastIndexOf("#") + 1,
              pNodeInput.get(i).trim().length())))));

      nodeList.add(node1);
      nodeNumber.put(node1.getName(), i);
    }

    for (int i = 0; i < nodeList.size(); i++)
      nodeListRed.add(nodeList.get(i));

    for (int i = 0; i < pEdgeInput.size(); i++) {

      MyNode node1 = new MyNode();
      MyNode node2 = new MyNode();

      MyEdge edge = new MyEdge();

      String nodeName = pEdgeInput.get(i).substring(0,
          pEdgeInput.get(i).trim().indexOf("#"));

      int nodeIndex = nodeNumber.get(nodeName);
      node1 = nodeList.get(nodeIndex);

      nodeName = pEdgeInput.get(i).substring(
          pEdgeInput.get(i).trim().indexOf("#") + 1,
          pEdgeInput.get(i).trim().lastIndexOf("#"));
      nodeIndex = nodeNumber.get(nodeName);
      node2 = nodeList.get(nodeIndex);

      int edgeWeight = Integer.parseInt(pEdgeInput.get(i).substring(
          pEdgeInput.get(i).trim().lastIndexOf("#") + 1,
          pEdgeInput.get(i).trim().length()));

      edge.setNode1(node1);
      edge.setNode2(node2);
      edge.setWeight(edgeWeight);

      edgeListL.add(edge);

    }
  }

  private void highlightPseudocode(int[] highlight, int[] unhighlight,
      SourceCode pseudoCode) {

    if (highlight != null)
      for (int i = 0; i < highlight.length; i++)
        pseudoCode.highlight(highlight[i]);

    if (unhighlight != null)
      for (int i = 0; i < unhighlight.length; i++)
        pseudoCode.unhighlight(unhighlight[i]);
  }

  public Language getLang() {
    return lang;
  }

  public void setLang(Language lang) {
    this.lang = lang;
  }

  @Override
  public String getName() {
    return "Kruskal's Algorithm [DE]";
  }

  @Override
  public String getAlgorithmName() {
    return "Kruskal's Algorithm";
  }

  @Override
  public String getAnimationAuthor() {
    // "Wladimir Schmidt <wschmidt@rbg.informatik.tu-darmstadt.de>, Peter Jelesnak <jelesnak@rbg.informatik.tu-darmstadt.de>";
    return "Wladimir Schmidt, Peter Jelesnak";
  }

  @Override
  public String getDescription() {
    return "Algorithmus von Kruskal ist ein Algorithmus der Graphentheorie<br>"
        + "\n"
        + "zur Berechnung minimaler Spannb&auml;ume von ungerichteten Graphen.";
  }

  @Override
  public String getCodeExample() {
    return "G = (V,E,w) - ein ungerichteter,<br>" + "\n"
        + "gewichteter Graph mit E':= {}, L:= E<br>" + "\n"
        + "Sortiere alle Kanten in L nach aufsteigendem Gewicht<br>" + "\n"
        + "<br>" + "\n" + "while L != {}<br>" + "\n"
        + "     w&auml;hle eine Kante e von L mit kleinstem Gewicht<br>" + "\n"
        + "     l&öuml;sche die Kante e aus L<br>" + "\n"
        + "     wenn der neuen Graph (V,E' + neue Kante {e})<br>" + "\n"
        + "     keinen Kreis enth&auml;lt,<br>" + "\n"
        + "          dann E':= E' + {e}<br>" + "\n" + "<br>" + "\n"
        + "M = (V,E') - ein minimaler Spannbaum von G";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}