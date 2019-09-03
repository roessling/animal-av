package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class DecentralStandardization implements Generator {

  // properties of the algorithm.
  private Language             lang;
  private RectProperties       rectProperties;
  private SourceCodeProperties sourceCodeProperties;
  private String[]             nodes;
  private int[][]              nodeCoordinates;
  private int[]                standardizationCosts;
  private TextProperties       textProperties;
  private MatrixProperties     matrixProperties;
  private int[][]              adjacencyMatrix;
  private Graph                graph;
  private GraphProperties      graphProperties;
  private boolean              showComprehensionQuestions;

  // View elements
  private Text                 infobox1;
  private Text                 infobox2;
  private Text                 infobox3;
  private Text                 infobox4;
  private Text                 infobox5;
  private Text                 infobox6;
  private Text                 infobox7;
  private Text                 infobox8;
  private Text                 infobox9;
  private Text                 infobox10;
  private Rect                 infoboxRect;
  private SourceCode           pseudoCode;
  private Text                 explanation1;
  private Text                 explanation2;
  private Text                 explanation3;
  private Text                 explanation4;
  private Text                 explanation5;
  private Text                 explanation6;
  private Text                 explanation7;
  private StringMatrix         standardizationCostsMatrix;
  private StringMatrix         expectedBenefitMatrix;
  private StringMatrix         standardizationProbabilitiesMatrix;
  private Text                 braceDown;
  private Text                 braceMiddle;
  private Text                 braceUp;
  private Text                 pij;
  private Rect                 explanationRect;
  private StringMatrix         communicationCostMatrix;
  private StringMatrix         numberOfNeighborMatrix;
  private TwoValueView         view;
  private Text                 standardizedInThisIteration;
  private Rect                 pseudoCodeRect;

  public static void main(String[] args) {
    DecentralStandardization dS = new DecentralStandardization();

    dS.init();
    dS.mainInit();
    dS.initAlgorithm();

    System.out.println(dS.lang.toString().replaceAll("refresh", "")
        .replaceAll("style table", "style table cellWidth 60 cellHeight 25"));
  }

  private void mainInit() {

    textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));

    sourceCodeProperties = new SourceCodeProperties();
    sourceCodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    sourceCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);

    rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

    matrixProperties = new MatrixProperties();
    matrixProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    matrixProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");

    showComprehensionQuestions = true;

    if (showComprehensionQuestions) {
      lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    // ###############################
    // # Example 1 #
    // ###############################

    // only if nodes have not been set yet.
    if (nodes == null) {
      // the name of the nodes.
      nodes = new String[4];
      nodes[0] = "A";
      nodes[1] = "B";
      nodes[2] = "C";
      nodes[3] = "D";
    }

    // only if the adjacency matrix has not been set yet.
    if (adjacencyMatrix == null) {
      // the adjacency matrix
      adjacencyMatrix = new int[4][4];
      adjacencyMatrix[0][0] = 0;
      adjacencyMatrix[0][1] = 31;
      adjacencyMatrix[0][2] = 22;
      adjacencyMatrix[0][3] = 0;
      adjacencyMatrix[1][0] = 27;
      adjacencyMatrix[1][1] = 0;
      adjacencyMatrix[1][2] = 41;
      adjacencyMatrix[1][3] = 26;
      adjacencyMatrix[2][0] = 28;
      adjacencyMatrix[2][1] = 19;
      adjacencyMatrix[2][2] = 0;
      adjacencyMatrix[2][3] = 32;
      adjacencyMatrix[3][0] = 0;
      adjacencyMatrix[3][1] = 36;
      adjacencyMatrix[3][2] = 25;
      adjacencyMatrix[3][3] = 0;
    }

    // the coordinates of the graph
    nodeCoordinates = new int[4][2];
    nodeCoordinates[0][0] = 840;
    nodeCoordinates[0][1] = 100;
    nodeCoordinates[1][0] = 1120;
    nodeCoordinates[1][1] = 100;
    nodeCoordinates[2][0] = 840;
    nodeCoordinates[2][1] = 380;
    nodeCoordinates[3][0] = 1120;
    nodeCoordinates[3][1] = 380;

    // only if the costs of standardization have not been set yet.
    if (standardizationCosts == null) {
      // the costs of standardization
      standardizationCosts = new int[4];
      standardizationCosts[0] = 12;
      standardizationCosts[1] = 97;
      standardizationCosts[2] = 14;
      standardizationCosts[3] = 22;
    }

    // ###############################
    // # Example 2 #
    // ###############################

    // only if nodes have not been set yet.
    /*
     * if(nodes == null){ //the name of the nodes. nodes = new String[5];
     * nodes[0] = "A"; nodes[1] = "B"; nodes[2] = "C"; nodes[3] = "D"; nodes[4]
     * = "E"; }
     * 
     * //only if the adjacency matrix has not been set yet. if(adjacencyMatrix
     * == null){ //the adjacency matrix adjacencyMatrix = new int[5][5];
     * adjacencyMatrix[0][0] = 0; adjacencyMatrix[0][1] = 16;
     * adjacencyMatrix[0][2] = 0; adjacencyMatrix[0][3] = 31;
     * adjacencyMatrix[0][4] = 0; adjacencyMatrix[1][0] = 23;
     * adjacencyMatrix[1][1] = 0; adjacencyMatrix[1][2] = 32;
     * adjacencyMatrix[1][3] = 0; adjacencyMatrix[1][4] = 45;
     * adjacencyMatrix[2][0] = 0; adjacencyMatrix[2][1] = 2;
     * adjacencyMatrix[2][2] = 0; adjacencyMatrix[2][3] = 29;
     * adjacencyMatrix[2][4] = 15; adjacencyMatrix[3][0] = 11;
     * adjacencyMatrix[3][1] = 0; adjacencyMatrix[3][2] = 17;
     * adjacencyMatrix[3][3] = 0; adjacencyMatrix[3][4] = 0;
     * adjacencyMatrix[4][0] = 0; adjacencyMatrix[4][1] = 25;
     * adjacencyMatrix[4][2] = 56; adjacencyMatrix[4][3] = 0;
     * adjacencyMatrix[4][4] = 0; }
     * 
     * //only if the node coordinates have not been set yet. if(nodeCoordinates
     * == null){ //the coordinates of the graph nodeCoordinates = new int[5][2];
     * nodeCoordinates[0][0] = 840; nodeCoordinates[0][1] = 100;
     * nodeCoordinates[1][0] = 1120; nodeCoordinates[1][1] = 100;
     * nodeCoordinates[2][0] = 980; nodeCoordinates[2][1] = 240;
     * nodeCoordinates[3][0] = 840; nodeCoordinates[3][1] = 380;
     * nodeCoordinates[4][0] = 1120; nodeCoordinates[4][1] = 380;
     * 
     * }
     * 
     * //only if the costs of standardization have not been set yet.
     * if(standardizationCosts == null){ //the costs of standardization
     * standardizationCosts = new int[5]; standardizationCosts[0] = 12;
     * standardizationCosts[1] = 86; standardizationCosts[2] = 37;
     * standardizationCosts[3] = 45; standardizationCosts[4] = 65;
     * 
     * }
     */

    // ###############################
    // # Example 3 #
    // ###############################

    // only if nodes have not been set yet.
    /*
     * if(nodes == null){ //the name of the nodes. nodes = new String[5];
     * nodes[0] = "A"; nodes[1] = "B"; nodes[2] = "C"; nodes[3] = "D"; nodes[4]
     * = "E"; }
     * 
     * //only if the adjacency matrix has not been set yet. if(adjacencyMatrix
     * == null){ //the adjacency matrix adjacencyMatrix = new int[5][5];
     * adjacencyMatrix[0][0] = 0; adjacencyMatrix[0][1] = 0;
     * adjacencyMatrix[0][2] = 0; adjacencyMatrix[0][3] = 25;
     * adjacencyMatrix[0][4] = 0; adjacencyMatrix[1][0] = 0;
     * adjacencyMatrix[1][1] = 0; adjacencyMatrix[1][2] = 27;
     * adjacencyMatrix[1][3] = 0; adjacencyMatrix[1][4] = 37;
     * adjacencyMatrix[2][0] = 0; adjacencyMatrix[2][1] = 19;
     * adjacencyMatrix[2][2] = 0; adjacencyMatrix[2][3] = 24;
     * adjacencyMatrix[2][4] = 31; adjacencyMatrix[3][0] = 20;
     * adjacencyMatrix[3][1] = 0; adjacencyMatrix[3][2] = 29;
     * adjacencyMatrix[3][3] = 0; adjacencyMatrix[3][4] = 36;
     * adjacencyMatrix[4][0] = 0; adjacencyMatrix[4][1] = 41;
     * adjacencyMatrix[4][2] = 26; adjacencyMatrix[4][3] = 17;
     * adjacencyMatrix[4][4] = 0; }
     * 
     * //only if the node coordinates have not been set yet. if(nodeCoordinates
     * == null){ //the coordinates of the graph nodeCoordinates = new int[5][2];
     * nodeCoordinates[0][0] = 840; nodeCoordinates[0][1] = 130;
     * nodeCoordinates[1][0] = 1120; nodeCoordinates[1][1] = 130;
     * nodeCoordinates[2][0] = 980; nodeCoordinates[2][1] = 270;
     * nodeCoordinates[3][0] = 840; nodeCoordinates[3][1] = 410;
     * nodeCoordinates[4][0] = 1120; nodeCoordinates[4][1] = 410;
     * 
     * }
     * 
     * //only if the costs of standardization have not been set yet.
     * if(standardizationCosts == null){ //the costs of standardization
     * standardizationCosts = new int[5]; standardizationCosts[0] = 12;
     * standardizationCosts[1] = 26; standardizationCosts[2] = 85;
     * standardizationCosts[3] = 17; standardizationCosts[4] = 23;
     * 
     * }
     */
  }

  public void init() {
    lang = new AnimalScript(
        "The Decentral Standardization Problem",
        "Daniel Burgmann <daniel.schlossfrau@gmail.com>, Matthias Horn <horn@stud.tu-darmstadt.de>, Torben Stoffer <torben.stoffer@stud.tu-darmstadt.de>",
        800, 600);
    lang.setStepMode(true);

    // necessary for generating the algorithm twice without closing animal.
    infobox1 = null;
    infobox2 = null;
    infobox3 = null;
    infobox4 = null;
    infobox5 = null;
    infobox6 = null;
    infobox7 = null;
    infobox8 = null;
    infobox9 = null;
    infobox10 = null;
    infoboxRect = null;
    pseudoCode = null;
    graph = null;
    explanation1 = null;
    explanation2 = null;
    explanation3 = null;
    explanation4 = null;
    explanation5 = null;
    explanation6 = null;
    explanation7 = null;
    standardizationCostsMatrix = null;
    expectedBenefitMatrix = null;
    standardizationProbabilitiesMatrix = null;
    braceDown = null;
    braceMiddle = null;
    braceUp = null;
    pij = null;
    explanationRect = null;
    communicationCostMatrix = null;
    numberOfNeighborMatrix = null;
    view = null;
    standardizedInThisIteration = null;
    pseudoCodeRect = null;

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    rectProperties = (RectProperties) props
        .getPropertiesByName("rectProperties");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    nodes = (String[]) primitives.get("nodes");
    nodeCoordinates = (int[][]) primitives.get("nodeCoordinates");
    standardizationCosts = (int[]) primitives.get("standardizationCosts");
    textProperties = (TextProperties) props
        .getPropertiesByName("textProperties");
    matrixProperties = (MatrixProperties) props
        .getPropertiesByName("matrixProperties");
    adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
    showComprehensionQuestions = (Boolean) primitives
        .get("showComprehensionQuestions");

    // some modifications.
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));

    // if the user wants to answer the comprehension questions.
    if (showComprehensionQuestions) {
      lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    // checking the validity of the user input
    // nodes: String[X]
    // nodeCoordinates: int[X][2]
    // adjacencyMatrix: int[X][X]
    // standardizationCosts: int[X]
    // X is the number of nodes in the graph.

    // The X-Coordinate of each node has to be higher than 840.
    boolean invalidCoordinates = false;

    for (int i = 0; i < nodeCoordinates.length; i++) {
      if (nodeCoordinates[i][0] < 840)
        invalidCoordinates = true;
    }

    int numbOfNodes = nodes.length;

    // adding the failure message when the input data is not valid. Else perform
    // algorithm.
    if (nodeCoordinates.length != numbOfNodes || nodeCoordinates[0].length != 2
        || adjacencyMatrix.length != numbOfNodes
        || adjacencyMatrix[0].length != numbOfNodes
        || standardizationCosts.length != numbOfNodes) {

      // adding the failure message
      TextProperties failureMessageProperties = new TextProperties();
      failureMessageProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
          new Font(Font.SANS_SERIF, Font.PLAIN, 16));
      lang.newText(new Coordinates(0, 0),
          "An error occured. Please check the validity of your input data:",
          "failureMessage1", null, failureMessageProperties);
      lang.newText(new Offset(0, 20, "failureMessage1",
          AnimalScript.DIRECTION_NW), "nodes: String[X]", "failureMessage2",
          null, failureMessageProperties);
      lang.newText(new Offset(0, 20, "failureMessage2",
          AnimalScript.DIRECTION_NW), "nodeCoordinates: int[X][2]",
          "failureMessage3", null, failureMessageProperties);
      lang.newText(new Offset(0, 20, "failureMessage3",
          AnimalScript.DIRECTION_NW), "adjacencyMatrix: int[X][X]",
          "failureMessage4", null, failureMessageProperties);
      lang.newText(new Offset(0, 20, "failureMessage4",
          AnimalScript.DIRECTION_NW), "standardizationCosts: int[X]",
          "failureMessage5", null, failureMessageProperties);
      lang.newText(new Offset(0, 20, "failureMessage5",
          AnimalScript.DIRECTION_NW), "X is the number of nodes in the graph.",
          "failureMessage6", null, failureMessageProperties);

    } else {
      if (invalidCoordinates) {
        TextProperties failureMessageProperties = new TextProperties();
        failureMessageProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
            new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        lang.newText(
            new Coordinates(0, 0),
            "An error occured. The x-coordinate of each node has to be higher or equal than 840.",
            "failureMessage1", null, failureMessageProperties);
      } else
        initAlgorithm();
    }

    // if the user wants to answer the comprehension questions.
    if (showComprehensionQuestions) {
      lang.finalizeGeneration();
    }

    return lang.toString().replaceAll("refresh", "")
        .replaceAll("style table", "style table cellWidth 60 cellHeight 25");
  }

  public String getName() {
    return "The Decentral Standardization Problem";
  }

  public String getAlgorithmName() {
    return "The Decentral Standardization Problem";
  }

  public String getAnimationAuthor() {
    return "Daniel Burgmann, Matthias Horn, Torben Stoffer";
    // return
    // "Daniel Burgmann <daniel.schlossfrau@gmail.com>, Matthias Horn <horn@stud.tu-darmstadt.de>, Torben Stoffer <torben.stoffer@stud.tu-darmstadt.de>";
  }

  public String getDescription() {
    return "The decentral problem of standardization is based on a network in"
        + "\n"
        + "which the communication between two nodes results in communication"
        + "\n"
        + "costs if no or only one node has decided to implement a given standard."
        + "\n"
        + "Consequently there are no communication costs if both nodes has decided to"
        + "\n"
        + "implement the standard. If a node decides to implement the standard it has"
        + "\n"
        + "to defray standardization costs. Because every node has to decide whether it"
        + "\n"
        + "is useful to implement the standard or not on its own the problem is"
        + "\n" + "decentral and is solved by use of an iterative algorithm.";
  }

  public String getCodeExample() {
    return "While algorithm reached no convergency (at least one standardization in the last iteration)"
        + "\n"
        + "   Calculate the estimated probabilities that the neighbours of the selected node standardize (Pij)"
        + "\n"
        + "		"
        + "\n"
        + "                   0                             if Cji⋅∅(j) < Aj  AND  j has not standardized yet"
        + "\n"
        + "                   (Cji⋅∅(j)-Aj)/(Cji⋅∅(j))     	if 0 ≤ Aj ≤ Cji⋅∅(j)  AND  j has not standardized yet"
        + "\n"
        + "                   1                             if Aj < 0  OR  j has already standardized"
        + "\n"
        + "\n"
        + "   Calculate the expected benefit of standardization for the selected node"
        + "\n"
        + "		"
        + "\n"
        + "      EXPECT[Ei] = ∑(Pij⋅Cij) - Ai"
        + "\n"
        + "                 j∊N(i)"
        + "\n"
        + "		"
        + "\n"
        + "   Decide whether the selected node will standardize or not (standardize if 0 < EXPECT[Ei])";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * initializes the algorithm.
   */
  private void initAlgorithm() {

    // adding the heading
    TextProperties headerProperties = new TextProperties();
    headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30),
        "The Decentral Problem of Standardization    ", "header", null,
        headerProperties);

    // adding the rectangle around the heading
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "headerRect", null, rectProperties);

    // adding the information about the algorithm
    infobox1 = lang.newText(new Coordinates(20, 80),
        "Information about the decentral problem of standardization",
        "infobox1", null, textProperties);
    infobox2 = lang.newText(new Offset(0, 5, "infobox1",
        AnimalScript.DIRECTION_SW), "", "infobox2", null, textProperties);
    infobox3 = lang.newText(new Offset(0, 5, "infobox2",
        AnimalScript.DIRECTION_SW),
        "The decentral problem of standardization is based on a network in",
        "infobox3", null, textProperties);
    infobox4 = lang.newText(new Offset(0, 5, "infobox3",
        AnimalScript.DIRECTION_SW),
        "which the communication between two nodes results in communication",
        "infobox4", null, textProperties);
    infobox5 = lang
        .newText(
            new Offset(0, 5, "infobox4", AnimalScript.DIRECTION_SW),
            "costs if no or only one node has decided to implement a given standard.",
            "infobox5", null, textProperties);
    infobox6 = lang
        .newText(
            new Offset(0, 5, "infobox5", AnimalScript.DIRECTION_SW),
            "Consequently there are no communication costs if both nodes has decided to",
            "infobox6", null, textProperties);
    infobox7 = lang
        .newText(
            new Offset(0, 5, "infobox6", AnimalScript.DIRECTION_SW),
            "implement the standard. If a node decides to implement the standard it has",
            "infobox7", null, textProperties);
    infobox8 = lang
        .newText(
            new Offset(0, 5, "infobox7", AnimalScript.DIRECTION_SW),
            "to defray standardization costs. Because every node has to decide whether it",
            "infobox8", null, textProperties);
    infobox9 = lang.newText(new Offset(0, 5, "infobox8",
        AnimalScript.DIRECTION_SW),
        "is useful to implement the standard or not on its own the problem is",
        "infobox9", null, textProperties);
    infobox10 = lang
        .newText(
            new Offset(0, 5, "infobox9", AnimalScript.DIRECTION_SW),
            "decentral and is solved by use of an iterative algorithm.                                ",
            "infobox10", null, textProperties);

    // adding the rectangle around the infobox
    infoboxRect = lang.newRect(new Offset(-5, -5, "infobox1",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "infobox10",
        AnimalScript.DIRECTION_SE), "infoboxRect", null, rectProperties);

    // the next animation step
    lang.nextStep();

    // hiding the infobox and its rectangle
    infobox1.hide();
    infobox2.hide();
    infobox3.hide();
    infobox4.hide();
    infobox5.hide();
    infobox6.hide();
    infobox7.hide();
    infobox8.hide();
    infobox9.hide();
    infobox10.hide();
    infoboxRect.hide();

    // adding the pseudo code of the algorithm
    pseudoCode = lang.newSourceCode(new Offset(0, 25, "headerRect",
        AnimalScript.DIRECTION_SW), "pseudoCode", null, sourceCodeProperties);
    pseudoCode
        .addCodeLine(
            "While algorithm reached no convergency (at least one standardization in the last iteration)",
            null, 0, null);
    pseudoCode
        .addCodeLine(
            "   Calculate the estimated probabilities that the neighbours of the selected node standardize (Pij)",
            null, 0, null);
    pseudoCode.addCodeLine("", null, 0, null);
    pseudoCode
        .addCodeLine(
            "                   0                                     if Cji⋅∅(j) < Aj  AND  j has not standardized yet",
            null, 0, null);
    pseudoCode
        .addCodeLine(
            "                   (Cji⋅∅(j)-Aj)/(Cji⋅∅(j))     if 0 ≤ Aj ≤ Cji⋅∅(j)  AND  j has not standardized yet",
            null, 0, null);
    pseudoCode
        .addCodeLine(
            "                   1                                     if Aj < 0  OR  j has already standardized",
            null, 0, null);
    pseudoCode.addCodeLine("", null, 0, null);
    pseudoCode
        .addCodeLine(
            "   Calculate the expected benefit of standardization for the selected node",
            null, 0, null);
    pseudoCode.addCodeLine("", null, 0, null);
    pseudoCode.addCodeLine("      EXPECT[Ei] = ∑(Pij⋅Cij) - Ai", null, 0, null);
    pseudoCode.addCodeLine("                         j∊N(i)", null, 0, null);
    pseudoCode.addCodeLine("", null, 0, null);
    pseudoCode
        .addCodeLine(
            "   Decide whether the selected node will standardize or not (standardize if 0 < EXPECT[Ei])                  ",
            null, 0, null);

    // adding the rectangle around the heading
    pseudoCodeRect = lang.newRect(new Offset(-5, -5, "pseudoCode",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "pseudoCode", "SE"),
        "pseudoCodeRect", null, rectProperties);

    // adding some pseudoCodeElements which will not be highlighted later.
    TextProperties pijProperties = new TextProperties();
    pijProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    pijProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        sourceCodeProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    pij = lang.newText(new Offset(25, 80, "pseudoCode",
        AnimalScript.DIRECTION_NW), "Pij = ", "pij", null, pijProperties);

    // the middle part of the curly bracket.
    TextProperties braceMiddleProperties = new TextProperties();
    braceMiddleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    braceMiddleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        sourceCodeProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    braceMiddle = lang.newText(new Offset(67, 80, "pseudoCode",
        AnimalScript.DIRECTION_NW), "⎨", "braceMiddle", null,
        braceMiddleProperties);

    // the upper part of the curly bracket.
    TextProperties braceUpProperties = new TextProperties();
    braceUpProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    braceUpProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        sourceCodeProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    braceUp = lang.newText(new Offset(67, 63, "pseudoCode",
        AnimalScript.DIRECTION_NW), "⎧", "braceUp", null, braceUpProperties);

    // the lower part of the curly bracket.
    TextProperties braceDownProperties = new TextProperties();
    braceDownProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    braceDownProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        sourceCodeProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    braceDown = lang
        .newText(new Offset(67, 97, "pseudoCode", AnimalScript.DIRECTION_NW),
            "⎩", "braceDown", null, braceDownProperties);

    // building the NodeCoordinates.
    Node[] coordinatesOfNodes = new Node[nodeCoordinates.length];

    for (int i = 0; i < nodeCoordinates.length; i++) {
      coordinatesOfNodes[i] = new Coordinates(nodeCoordinates[i][0],
          nodeCoordinates[i][1]);
    }

    // the graphProperties
    graphProperties = new GraphProperties();
    graphProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    graphProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.RED);
    graphProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProperties.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    graphProperties.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

    // adding the graph
    graph = lang.newGraph("graph", adjacencyMatrix, coordinatesOfNodes, nodes,
        null, graphProperties);

    // filling the matrix of the standardization costs.
    String[][] communicationCostMatrixContent = new String[nodes.length + 1][nodes.length + 1];
    communicationCostMatrixContent[0][0] = "Cij";

    // filling the rest of the matrix
    for (int i = 1; i < communicationCostMatrixContent.length; i++) {
      communicationCostMatrixContent[0][i] = nodes[i - 1];
      communicationCostMatrixContent[i][0] = nodes[i - 1];
    }

    // filling the rest of the matrix
    for (int i = 1; i < communicationCostMatrixContent.length; i++) {
      for (int j = 1; j < communicationCostMatrixContent.length; j++) {
        if (i == j || adjacencyMatrix[i - 1][j - 1] == 0)
          communicationCostMatrixContent[i][j] = "---";
        else
          communicationCostMatrixContent[i][j] = ""
              + adjacencyMatrix[i - 1][j - 1];
      }
    }

    // adding the matrix of standardization costs.
    communicationCostMatrix = lang.newStringMatrix(new Offset(0, 50, "graph",
        AnimalScript.DIRECTION_SW), communicationCostMatrixContent,
        "communicationCost", null, matrixProperties);

    // filling the matrix of the standardization costs.
    String[][] standardizationCostsMatrixContent = new String[2][standardizationCosts.length + 1];
    standardizationCostsMatrixContent[0][0] = "i";
    standardizationCostsMatrixContent[1][0] = "Ai";

    // filling the rest of the matrix.
    for (int i = 1; i < standardizationCostsMatrixContent[0].length; i++) {
      standardizationCostsMatrixContent[0][i] = nodes[i - 1];
      int sc = standardizationCosts[i - 1];
      standardizationCostsMatrixContent[1][i] = "" + sc + "";
    }
    // adding the matrix of standardization costs.
    standardizationCostsMatrix = lang.newStringMatrix(new Offset(0, 25,
        "communicationCost", AnimalScript.DIRECTION_SW),
        standardizationCostsMatrixContent, "standardizationCosts", null,
        matrixProperties);

    // filling the matrix with the number of neighbors.
    String[][] numberOfNeighborMatrixContent = new String[2][nodes.length + 1];
    numberOfNeighborMatrixContent[0][0] = "i";
    numberOfNeighborMatrixContent[1][0] = "∅(i)";

    // filling the rest of the matrix.
    for (int i = 1; i < numberOfNeighborMatrixContent[0].length; i++) {
      numberOfNeighborMatrixContent[0][i] = nodes[i - 1];
      int numbOfNeighbors = 0;

      // counting the neighbors
      for (int j = 0; j < adjacencyMatrix[i - 1].length; j++) {
        if (adjacencyMatrix[i - 1][j] != 0)
          numbOfNeighbors++;
      }

      numberOfNeighborMatrixContent[1][i] = "" + numbOfNeighbors + "";
    }

    // adding the matrix of standardization costs.
    numberOfNeighborMatrix = lang.newStringMatrix(new Offset(0, 25,
        "standardizationCosts", AnimalScript.DIRECTION_SW),
        numberOfNeighborMatrixContent, "numberOfNeighbors", null,
        matrixProperties);

    // filling the matrix of the standardization costs.
    String[][] standardizationProbabilitiesMatrixContent = new String[nodes.length + 1][nodes.length + 1];
    standardizationProbabilitiesMatrixContent[0][0] = "Pij";

    // filling the rest of the matrix
    for (int i = 1; i < standardizationProbabilitiesMatrixContent.length; i++) {
      standardizationProbabilitiesMatrixContent[0][i] = nodes[i - 1];
      standardizationProbabilitiesMatrixContent[i][0] = nodes[i - 1];
    }

    // filling the rest of the matrix
    for (int i = 1; i < standardizationProbabilitiesMatrixContent.length; i++) {
      for (int j = 1; j < standardizationProbabilitiesMatrixContent.length; j++) {
        if (i == j)
          standardizationProbabilitiesMatrixContent[i][j] = "---";
        else
          standardizationProbabilitiesMatrixContent[i][j] = "";
      }
    }

    // adding the matrix of standardization costs.
    standardizationProbabilitiesMatrix = lang.newStringMatrix(new Offset(0,
        140, "pseudoCode", AnimalScript.DIRECTION_SW),
        standardizationProbabilitiesMatrixContent,
        "standardizationProbabilities", null, matrixProperties);

    // adding a text for signaling that there was at least one standardization.
    TextProperties standardizedInThisIterationProperties = new TextProperties();
    standardizedInThisIterationProperties.set(
        AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
            Font.PLAIN, 16));
    standardizedInThisIterationProperties.set(
        AnimationPropertiesKeys.COLOR_PROPERTY,
        sourceCodeProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    standardizedInThisIteration = lang.newText(new Offset(0, 40, "pseudoCode",
        AnimalScript.DIRECTION_SW),
        "At least one standardization in the current iteration: F",
        "standardizedInThisIteration", null,
        standardizedInThisIterationProperties);

    // adding a counter for the standardization probability matrix
    TwoValueCounter counter = lang
        .newCounter(standardizationProbabilitiesMatrix);
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);

    view = lang.newCounterView(counter, new Offset(0, 25,
        "standardizedInThisIteration", AnimalScript.DIRECTION_SW), cp, true,
        true);

    // filling the matrix of the standardization costs.
    String[][] expectedBenefitMatrixContent = new String[3][nodes.length + 1];
    expectedBenefitMatrixContent[0][0] = "i";
    expectedBenefitMatrixContent[1][0] = "EXP[Ei]";
    expectedBenefitMatrixContent[2][0] = "Std.?";

    // filling the rest of the matrix
    for (int i = 0; i < expectedBenefitMatrixContent.length; i++) {
      for (int j = 1; j < expectedBenefitMatrixContent[i].length; j++) {
        // only in the upper row.
        if (i == 0)
          expectedBenefitMatrixContent[i][j] = nodes[j - 1];
        else
          expectedBenefitMatrixContent[i][j] = "";
      }
    }

    // adding the expected benefit matrix.
    expectedBenefitMatrix = lang
        .newStringMatrix(new Offset(0, 25, "standardizationProbabilities",
            AnimalScript.DIRECTION_SW), expectedBenefitMatrixContent,
            "expectedBenefit", null, matrixProperties);

    // adding the explanation about the algorithm
    explanation1 = lang.newText(new Offset(0, 25, "expectedBenefit",
        AnimalScript.DIRECTION_SW),
        "weights of the edges = communication costs of the edge",
        "explanation1", null, textProperties);
    explanation2 = lang.newText(new Offset(0, 5, "explanation1",
        AnimalScript.DIRECTION_SW),
        "Pij = estimated probability (by i) that j will standardize",
        "explanation2", null, textProperties);
    explanation3 = lang.newText(new Offset(0, 5, "explanation2",
        AnimalScript.DIRECTION_SW),
        "Cij = communication costs for i to communicate with j",
        "explanation3", null, textProperties);
    explanation4 = lang.newText(new Offset(0, 5, "explanation3",
        AnimalScript.DIRECTION_SW), "∅(j) = number of j's neighbours",
        "explanation4", null, textProperties);
    explanation5 = lang.newText(new Offset(0, 5, "explanation4",
        AnimalScript.DIRECTION_SW), "Aj = standardization costs of j",
        "explanation5", null, textProperties);
    explanation6 = lang.newText(new Offset(0, 5, "explanation5",
        AnimalScript.DIRECTION_SW), "N(i) = neighbours of i", "explanation6",
        null, textProperties);
    explanation7 = lang.newText(new Offset(0, 5, "explanation6",
        AnimalScript.DIRECTION_SW),
        "EXPECT[Ei] = estimated benefit through standardization for i",
        "explanation7", null, textProperties);

    // adding the rectangle around the explaination
    explanationRect = lang.newRect(new Offset(-5, -5, "explanation1",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "explanation7", "SE"),
        "explanationRect", null, rectProperties);

    // highlighting line #0.
    lang.nextStep();
    pseudoCode.highlight(0);
    lang.nextStep("Iteration 1");

    // performs the algorithm.
    performAlgorithm(pseudoCode, standardizationProbabilitiesMatrix,
        expectedBenefitMatrix);

    // hide all elements which are not needed any more.
    pseudoCode.hide();
    graph.hide();
    explanation1.hide();
    explanation2.hide();
    explanation3.hide();
    explanation4.hide();
    explanation5.hide();
    explanation6.hide();
    explanation7.hide();
    standardizationCostsMatrix.hide();
    expectedBenefitMatrix.hide();
    standardizationProbabilitiesMatrix.hide();
    braceDown.hide();
    braceMiddle.hide();
    braceUp.hide();
    pij.hide();
    explanationRect.hide();
    communicationCostMatrix.hide();
    numberOfNeighborMatrix.hide();
    view.hide();
    standardizedInThisIteration.hide();
    pseudoCodeRect.hide();

    // adding the information about the algorithm
    lang.newText(new Coordinates(20, 80),
        "Closing Information about the decentral problem of standardization",
        "closingInformation1", null, textProperties);
    lang.newText(new Offset(0, 5, "closingInformation1",
        AnimalScript.DIRECTION_SW), "", "closingInformation2", null,
        textProperties);
    lang.newText(
        new Offset(0, 5, "closingInformation2", AnimalScript.DIRECTION_SW),
        "Please note that this implementation is based on almost complete information",
        "closingInformation3", null, textProperties);
    lang.newText(new Offset(0, 5, "closingInformation3",
        AnimalScript.DIRECTION_SW), "for every node about its neighbourhood.",
        "closingInformation4", null, textProperties);
    lang.newText(
        new Offset(0, 5, "closingInformation4", AnimalScript.DIRECTION_SW),
        "Therefore every node has knowledge about the number of its neighbours,",
        "closingInformation5", null, textProperties);
    lang.newText(
        new Offset(0, 5, "closingInformation5", AnimalScript.DIRECTION_SW),
        "their costs of standardization and the communication costs of the links",
        "closingInformation6", null, textProperties);
    lang.newText(new Offset(0, 5, "closingInformation6",
        AnimalScript.DIRECTION_SW),
        "between it and its neighbours and vice versa.", "closingInformation7",
        null, textProperties);
    lang.newText(
        new Offset(0, 5, "closingInformation7", AnimalScript.DIRECTION_SW),
        "In other possible implementations it is possible to limit the knowledge",
        "closingInformation8", null, textProperties);
    lang.newText(
        new Offset(0, 5, "closingInformation8", AnimalScript.DIRECTION_SW),
        "of every node which may lead to completely different results.                            ",
        "closingInformation9", null, textProperties);

    // adding the rectangle around the infobox
    lang.newRect(new Offset(-5, -5, "closingInformation1",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "closingInformation9",
        AnimalScript.DIRECTION_SE), "closingInfoboxRect", null, rectProperties);
  }

  /**
   * performs the algorithm.
   */
  private void performAlgorithm(SourceCode pseudoCode,
      StringMatrix standardizationProbabilitiesMatrix,
      StringMatrix expectedBenefitMatrix) {

    // a boolean array for saving whether the node has already standardized.
    boolean[] hasStandardized = new boolean[nodes.length];

    // initializing the boolean array.
    for (int i = 0; i < hasStandardized.length; i++) {
      hasStandardized[i] = false;
    }

    // a boolean for checking whether convergency is reached.
    boolean convergencyReached = false;

    // a 2D-double array for all standardization possibilities.
    double[][] standardizationProbabilities = new double[nodes.length][nodes.length];

    // a boolean for the questions.
    boolean firstIteration = true;

    // a counter for the iterations
    int howManyIterations = 0;

    // while convergency is not reached.
    while (!convergencyReached) {

      // initializing the standardization text.
      standardizedInThisIteration.setText(
          "At least one standardization in the current iteration: F", null,
          null);
      standardizedInThisIteration.changeColor(null,
          (Color) sourceCodeProperties
              .get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);

      // unhighlighting line #0;
      pseudoCode.unhighlight(0);

      // a help array.
      boolean[] hasStandardizedThisRound = new boolean[hasStandardized.length];

      // copiing the boolean array with the standardization information.
      for (int i = 0; i < hasStandardized.length; i++) {
        hasStandardizedThisRound[i] = hasStandardized[i];
      }

      // looping over all nodes.
      for (int i = 0; i < nodes.length; i++) {

        // only if the node has not standardized yet.
        if (!hasStandardized[i]) {

          // the edges for node i.
          int[][] allEdges = graph.getAdjacencyMatrix();
          // highlighting all corresponding edges to i.
          for (int k = 0; k < allEdges.length; k++) {
            // if there is an edge between i and k.
            if (allEdges[i][k] != 0) {
              graph.highlightEdge(i, k, null, null);
              graph.highlightEdge(k, i, null, null);
            }
          }

          // highlighting the node.
          graph.highlightNode(i, null, null);

          // highlighting line 1.
          pseudoCode.highlight(1);

          // a boolean for the labels
          boolean firstNode = true;

          // looping over all nodes for calculating the standardization
          // probability.
          for (int j = 0; j < nodes.length; j++) {

            // only if there is an edge between i and j.
            if (graph.getAdjacencyMatrix()[i][j] != 0) {

              // the amount of neighbors of the current node.
              int howManyNeighbors = 0;

              // looping over all nodes for counting the neighbors.
              for (int k = 0; k < nodes.length; k++) {
                // only if k != j and there is an edge between j and k.
                if (k != j && adjacencyMatrix[j][k] != 0)
                  howManyNeighbors++;
              }

              // the benefit in form of saved communication costs.
              double communicationCostsBenefit = graph.getEdgeWeight(j, i)
                  * howManyNeighbors;

              // the old standardization probability
              double oldStandardizationProbability = standardizationProbabilities[i][j];

              // saves which case will be visited.
              int whichCase = -1;

              // only if j has not already standardized and the communication
              // costs benefit is lower than the standardization costs.
              if (hasStandardized[j] == false
                  && communicationCostsBenefit < standardizationCosts[j]) {
                standardizationProbabilities[i][j] = 0;
                whichCase = 3;
              } else {
                // only if j has already standardized or the standardization
                // costs are negative.
                if (hasStandardized[j] == true || 0 > standardizationCosts[j]) {
                  standardizationProbabilities[i][j] = 1;
                  whichCase = 5;
                }

                else {
                  standardizationProbabilities[i][j] = (communicationCostsBenefit - standardizationCosts[j])
                      / communicationCostsBenefit;
                  whichCase = 4;
                }
              }

              // if the probability changed.
              if (oldStandardizationProbability != standardizationProbabilities[i][j]
                  || standardizationProbabilitiesMatrix
                      .getElement(i + 1, j + 1).equals("")) {
                standardizationProbabilitiesMatrix.highlightCell(i + 1, j + 1,
                    null, null);
                standardizationProbabilitiesMatrix.put(i + 1, j + 1,
                    "" + Math.round(standardizationProbabilities[i][j] * 100)
                        / 100.0, null, null);
                pseudoCode.highlight(whichCase);

                // ask a question if the first iteration is not over.
                if (firstIteration && showComprehensionQuestions) {
                  FillInBlanksQuestionModel pijQuestion = new FillInBlanksQuestionModel(
                      "p" + i + j + "question");
                  pijQuestion
                      .setPrompt("Which value was computed for P"
                          + nodes[i]
                          + nodes[j]
                          + "? (Please use a comma for decimal numbers instead of a dot.)");

                  String answer = standardizationProbabilitiesMatrix
                      .getElement(i + 1, j + 1);
                  pijQuestion.addAnswer(answer.replace(".", ","), 1,
                      "Correct! " + answer.replace(".", ",")
                          + " was computed for P" + nodes[i] + nodes[j] + "!");
                  lang.addFIBQuestion(pijQuestion);
                }

                if (whichCase == 4) {
                  standardizationCostsMatrix
                      .highlightCell(1, j + 1, null, null);
                  communicationCostMatrix.highlightCell(j + 1, i + 1, null,
                      null);
                  numberOfNeighborMatrix.highlightCell(1, j + 1, null, null);
                }

                // only for the first node.
                if (firstNode) {
                  if (howManyIterations == 0)
                    lang.nextStep("   Calculation of standardization propabilities for node "
                        + nodes[i]);
                  else
                    lang.nextStep("   Recalculation of standardization propabilities for node "
                        + nodes[i]);
                } else
                  lang.nextStep();

                standardizationProbabilitiesMatrix.unhighlightCell(i + 1,
                    j + 1, null, null);
                pseudoCode.unhighlight(whichCase);

                if (whichCase == 4) {
                  standardizationCostsMatrix.unhighlightCell(1, j + 1, null,
                      null);
                  communicationCostMatrix.unhighlightCell(j + 1, i + 1, null,
                      null);
                  numberOfNeighborMatrix.unhighlightCell(1, j + 1, null, null);
                }

                // no more labels
                firstNode = false;
              }
            }
            // if there is no edge between i and j
            else {
              if (i != j
                  && !standardizationProbabilitiesMatrix.getElement(i + 1,
                      j + 1).equals("---")) {
                standardizationProbabilitiesMatrix.highlightCell(i + 1, j + 1,
                    null, null);
                standardizationProbabilitiesMatrix.put(i + 1, j + 1, "---",
                    null, null);
                lang.nextStep();
                standardizationProbabilitiesMatrix.unhighlightCell(i + 1,
                    j + 1, null, null);
              }
            }
          }

          // unhighlighting line #1
          pseudoCode.unhighlight(1);

          // highlight lines#7,9,10
          pseudoCode.highlight(7);
          pseudoCode.highlight(9);
          pseudoCode.highlight(10);

          // calculated the expected benefit of standardization.
          double expectedBenefit = 0;

          // looping over all nodes for calculating the expected benefit of
          // standardization.
          for (int k = 0; k < nodes.length; k++) {
            // only if k != i
            if (k != i) {
              expectedBenefit += standardizationProbabilities[i][k]
                  * graph.getEdgeWeight(i, k);
            }
          }
          // substract the costs of standardization
          expectedBenefit -= standardizationCosts[i];
          // updating the expected benefit matrix.
          expectedBenefitMatrix.highlightCell(1, i + 1, null, null);
          expectedBenefitMatrix.put(1, i + 1,
              "" + Math.round(expectedBenefit * 100) / 100.0, null, null);

          // ask a question if the first iteration is not over.
          if (firstIteration && showComprehensionQuestions) {
            FillInBlanksQuestionModel expectedBenefitQuestion = new FillInBlanksQuestionModel(
                "expectedBenefitQuestion");
            expectedBenefitQuestion
                .setPrompt("Which value was computed for the expected benefit for "
                    + nodes[i]
                    + "? (Please use a comma for decimal numbers instead of a dot.)");

            String answer = expectedBenefitMatrix.getElement(1, i + 1);
            expectedBenefitQuestion.addAnswer(answer.replace(".", ","), 1,
                "Correct! " + answer.replace(".", ",")
                    + " was computed for the expected benefit for " + nodes[i]
                    + "!");
            lang.addFIBQuestion(expectedBenefitQuestion);
          }

          lang.nextStep();
          // unhighlight lines#7,9,10
          pseudoCode.unhighlight(7);
          pseudoCode.unhighlight(9);
          pseudoCode.unhighlight(10);
          // unhighlighting the expectedBenefitMatrix.
          expectedBenefitMatrix.unhighlightCell(1, i + 1, null, null);

          // highlight line#12
          pseudoCode.highlight(12);

          // checking whether there is benefit of standardization.
          if (expectedBenefit > 0) {
            hasStandardizedThisRound[i] = true;
            // updating the expected benefit matrix.
            expectedBenefitMatrix.highlightCell(2, i + 1, null, null);
            expectedBenefitMatrix.put(2, i + 1, "T", null, null);
            // updating the standardization text.
            standardizedInThisIteration.setText(
                "At least one standardization in the current iteration: T",
                null, null);
            standardizedInThisIteration.changeColor(null,
                (Color) sourceCodeProperties
                    .get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY),
                null, null);

            // ask a question if the user wants to answer questions.
            if (showComprehensionQuestions && firstIteration) {
              MultipleChoiceQuestionModel standardizationQuestion = new MultipleChoiceQuestionModel(
                  "standardizationQuestion");
              standardizationQuestion.setPrompt("Did node " + nodes[i]
                  + " standardize or not?");
              standardizationQuestion
                  .addAnswer(
                      "Yes",
                      1,
                      "Correct! Node "
                          + nodes[i]
                          + " standardized because the expected benefit was greater than zero");
              standardizationQuestion
                  .addAnswer(
                      "No",
                      0,
                      "That is not correct! "
                          + nodes[i]
                          + " standardized because the expected benefit was greater than zero");
              lang.addMCQuestion(standardizationQuestion);
            }
          } else {
            // updating the expected benefit matrix.
            expectedBenefitMatrix.highlightCell(2, i + 1, null, null);
            expectedBenefitMatrix.put(2, i + 1, "F", null, null);

            // ask a question if the user wants to answer questions.
            if (showComprehensionQuestions && firstIteration) {
              MultipleChoiceQuestionModel standardizationQuestion = new MultipleChoiceQuestionModel(
                  "standardizationQuestion");
              standardizationQuestion.setPrompt("Did node " + nodes[i]
                  + " standardize or not?");
              standardizationQuestion
                  .addAnswer(
                      "Yes",
                      0,
                      "That is not correct! "
                          + nodes[i]
                          + " did not standardize because the expected benefit was lower than zero");
              standardizationQuestion
                  .addAnswer(
                      "No",
                      1,
                      "Correct! Node "
                          + nodes[i]
                          + " did not standardize because the expected benefit was lower than zero");
              lang.addMCQuestion(standardizationQuestion);
            }

          }

          lang.nextStep();

          // unhighlight line#12
          pseudoCode.unhighlight(12);

          // unhighlighting the expectedBenefitMatrix.
          expectedBenefitMatrix.unhighlightCell(2, i + 1, null, null);

          // unhighlighting all corresponding edges to i.
          for (int k = 0; k < allEdges.length; k++) {
            // if there is an edge between i and k.
            if (allEdges[i][k] != 0) {
              graph.unhighlightEdge(i, k, null, null);
              graph.unhighlightEdge(k, i, null, null);
            }
          }

          // highlighting the node.
          graph.unhighlightNode(i, null, null);
        }

        // when first iteration is finished.
        firstIteration = false;
      }

      // checking if convergency is reached.
      convergencyReached = true;

      for (int k = 0; k < hasStandardized.length; k++) {
        // if node k decided to standardized this round.
        if (hasStandardized[k] != hasStandardizedThisRound[k]) {
          convergencyReached = false;
          hasStandardized[k] = hasStandardizedThisRound[k];
        }
      }

      howManyIterations++;

      // if convergency is reached.
      if (convergencyReached) {
        // ask a question if the user wants to answer questions.
        if (showComprehensionQuestions) {
          MultipleChoiceQuestionModel contingencyQuestion = new MultipleChoiceQuestionModel(
              "contingencyQuestion" + howManyIterations);
          contingencyQuestion.setPrompt("Is convergency reached?");
          contingencyQuestion
              .addAnswer(
                  "Yes",
                  1,
                  "Correct! Convergency is reached because there wasn't a node which standardized this iteration");
          contingencyQuestion
              .addAnswer(
                  "No",
                  0,
                  "That is not correct! Convergency is reached because there wasn't a node which standardized this iteration");
          lang.addMCQuestion(contingencyQuestion);
        }
      } else {
        // ask a question if the user wants to answer questions.
        if (showComprehensionQuestions) {
          MultipleChoiceQuestionModel contingencyQuestion = new MultipleChoiceQuestionModel(
              "contingencyQuestion" + howManyIterations);
          contingencyQuestion.setPrompt("Is convergency reached?");
          contingencyQuestion
              .addAnswer(
                  "Yes",
                  0,
                  "That is not correct! Convergency is not reached because there was at least one node which standardized this iteration");
          contingencyQuestion
              .addAnswer(
                  "No",
                  1,
                  "Correct! Convergency is not reached because there was at least one node which standardized this iteration");
          lang.addMCQuestion(contingencyQuestion);
        }
      }

      // highlighting the line #0.
      pseudoCode.highlight(0);
      lang.nextStep("Iteration " + (howManyIterations + 1));

    }
  }
}