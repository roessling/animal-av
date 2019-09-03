package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Thomas Hartmann, Wail Shakir
 * @version 1.0 2013-06-07
 * 
 */
public class WelshPowell implements Generator {
  private Language                                       lang;
  private GraphProperties                                graphProp;
  private Graph                                          graph;
  private SourceCodeProperties                           sourceCode;
  private HashMap<String, algoanim.primitives.Primitive> allPrimitives;
  private ArrayList<Integer>                             sortList;
  private ArrayList<Color>                               colors;
  private ArrayList<ArrayList<Integer>>                  cSets;

  private Variables                                      variables;

  private static final String                            Description = "Der Welsh-Powell Algorithmus ist ein Verfahren, welches jedem Knoten eines \n"
                                                                         + "ungerichteten Graphen eine Farbe zuordnet ohne, dass benachbarte Knoten \n"
                                                                         + "die gleiche Farbe erhalten. Dabei verwendet es möglichst wenige Farben.";

  private static final String                            PSEUDOTEXT  = "Schritt 1: Man numeriere die Knoten von G als x1 , . . . , xn , so dass d(x1) ≥ d(x2) ≥ . . . ≥ d(xn). \n"
                                                                         + "#(Wobei d(xi) ∼ Knotengrad des i-ten Knoten). \n"
                                                                         + "#Man numeriere die zur Verfügung stehenden Farben mit 1, 2, . . . , n. \n"
                                                                         + "Schritt 2: Für jedes i = 1, . . . , n sei Ci = {1, 2, . . . , i} die Menge der Farben, die den Knoten xi färben könnten. \n"
                                                                         + "Schritt 3: Man setze i = 1. \n"
                                                                         + "Schritt 4: Es sei ci die erste Farbe in Ci . man ordne diese Farbe dem Knoten xi zu. \n"
                                                                         + "Schritt 5: Für jedes j, für das i < j gilt und xi zu xj adjazent (benachbart) in G ist, setze man Cj = Cj − {ci}. \n"
                                                                         + "#Man erhöhe i um 1 und kehre zu Schritt 4 zurück, wenn i + 1 ≤ n gilt. \n"
                                                                         + "Schritt 6: Man notiere die Farbe jedes Knotens \n";

  private static final String                            CONCLUSION  = "Der Welsh-Powell Algorithmus findet nicht immer die optimale Lösung des Färbungsproblems.\n"
                                                                         + "Er verwendet höchstes d(G)+1 Farben, wobei d(G) der höchste Knotengrad im Graphen G ist.\n"
                                                                         + "Andere Färbungsverfahren sind: \n"
                                                                         + "\t \t Greedy-Algorithmus zur Graphenfärbung\n"
                                                                         + "\t \t Randomisierte Farbreduzierung";

  private static final Font                              NORMALFont  = new Font(
                                                                         "SansSerif",
                                                                         Font.PLAIN,
                                                                         12);
  private static final Font                              HEADERFont  = new Font(
                                                                         "SansSerif",
                                                                         Font.BOLD,
                                                                         16);

  /**
   * Default constructor
   */
  public WelshPowell() {
  }

  public void init() {
    lang = new AnimalScript("Welsh-Powell Algorithmus[DE]",
        "Thomas Hartmann,Wail Shakir", 1200, 960);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    allPrimitives = new HashMap<String, algoanim.primitives.Primitive>();
    sourceCode = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    graphProp = (GraphProperties) props.getPropertiesByName("graphProperties");
    graph = (Graph) primitives.get("graph");
    colors = new ArrayList<Color>();
    sortList = new ArrayList<Integer>();
    init();
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    lang.setStepMode(true);
    create();

    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Welsh-Powell Algorithmus[DE]";
  }

  public String getAlgorithmName() {
    return "Welsh-Powell Algorithm";
  }

  public String getAnimationAuthor() {
    return "Thomas Hartmann, Wail Shakir";
  }

  public String getDescription() {
    return "Der Welsh-Powell Algorithmus ist ein Verfahren, welches jedem Knoten eines ungerichteten Graphen eine Farbe zuordnet ohne, dass benachbarte Knoten die gleiche Farbe erhalten. Dabei verwendet es m&ouml;glichst wenige Farben.";
  }

  public String getCodeExample() {
    return "Schritt 1: Man numeriere die Knoten von G als x1 , . . . , xn , so dass d(x1) ≥ d(x2) ≥ . . . ≥ d(xn)."
        + "\n"
        + " Wobei d(xi) ∼ Knotengrad des i-ten Knoten)."
        + "\n"
        + " Man numeriere die zur Verfügung stehenden Farben mit 1, 2, . . . , n."
        + "\n"
        + "Schritt 2: Für jedes i = 1, . . . , n sei Ci = {1, 2, . . . , i} die Menge der Farben, die den Knoten xi färben könnten."
        + "\n"
        + "Schritt 3: Man setze i = 1."
        + "\n"
        + "Schritt 4: Es sei ci die erste Farbe in Ci . man ordne diese Farbe dem Knoten xi zu."
        + "\n"
        + "Schritt 5: Für jedes j, für das i < j gilt und xi zu xj adjazent (benachbart) in G ist, setze man Cj = Cj − {ci}."
        + "\n"
        + " Man erhöhe i um 1 und kehre zu Schritt 4 zurück, wenn i + 1 ≤ n gilt."
        + "\n" + "Schritt 6: Man notiere die Farbe jedes Knotens";
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
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  private String[] createNodeName(boolean leer) {
    String[] nodeName = new String[this.sortList.size()];

    for (int i = 0; i < this.sortList.size(); i++) {

      nodeName[this.sortList.get(i)] = leer ? "  " : ("X" + (i + 1));
    }
    return nodeName;
  }

  private int[][] createAdjMatrix() {
    int[][] adjMatrix = this.graph.getAdjacencyMatrix();

    for (int i = 0; i < adjMatrix.length; i++) {
      for (int j = 0; j <= i; j++) {
        if (adjMatrix[i][j] != 1)
          adjMatrix[i][j] = adjMatrix[j][i];
        else if (adjMatrix[j][i] != 1)
          adjMatrix[j][i] = adjMatrix[i][j];

      }
    }
    return adjMatrix;

  }

  public void create() {
    variables = lang.newVariables();

    createHeader();

    createIntroduction();
    lang.nextStep("Einleitung");

    hideIntroduction();

    createPseudoCode();
    lang.nextStep("Pseudocode");

    int[][] adjMatrix = createAdjMatrix();

    sortEgdesCounter(adjMatrix);
    int graphX = 70;
    int graphY = 260;
    Coordinates[] coordinates = new Coordinates[adjMatrix.length];

    for (int i = 0; i < adjMatrix.length; i++) {
      Coordinates coordinates2 = (Coordinates) this.graph.getNode(i);
      coordinates[i] = new Coordinates(coordinates2.getX() + graphX,
          coordinates2.getY() + graphY);
    }
    createGraph(adjMatrix, coordinates, createNodeName(true), true);
    lang.nextStep();
    highlightPseudoCode(0);
    highlightPseudoCode(1);

    ((Graph) this.allPrimitives.get("welshPowell0")).hide();
    createGraph(adjMatrix, coordinates, createNodeName(false), false);

    lang.nextStep();
    highlightPseudoCode(2);
    initColorList();
    createColorTable(800, 50);
    lang.nextStep();
    unhighlightPseudoCode(0);
    unhighlightPseudoCode(1);
    unhighlightPseudoCode(2);
    highlightPseudoCode(3);
    initcSets();
    for (int i = 0; i < this.cSets.size(); i++) {
      variables.declare("String", "C" + (i + 1), "{" + cSetString(cSets.get(i))
          + "}");
    }

    createColorSetArrays(800, 50 + 25 * colors.size() + 100);
    lang.nextStep();
    unhighlightPseudoCode(3);
    highlightPseudoCode(4);
    createCounterI(800, 50 + 25 * colors.size() + 50);
    variables.declare("int", "i", "1");

    MultipleChoiceQuestionModel mcQuestion = new MultipleChoiceQuestionModel(
        "farbeAbfrage");
    mcQuestion.setPrompt("Welche Farbe wird X1 zugeordnet?");
    for (int i = 0; i < Math.min(this.sortList.size(), 5); i++) {
      mcQuestion
          .addAnswer(
              "Nr. " + (i + 1),
              (i == 0) ? 1 : 0,
              (i == 0) ? "Richtig!"
                  : "Leider falsch! Dem Knoten mit den meisten Kanten(X1) wird immer die erste Farbe zugeordnet.");
    }
    lang.addMCQuestion(mcQuestion);

    lang.nextStep();
    unhighlightPseudoCode(4);
    for (int i = 0; i < this.sortList.size(); i++) {
      highlightPseudoCode(5);
      createCircle(this.sortList.get(i),
          this.colors.get(this.cSets.get(i).get(0)));
      lang.nextStep("Iteration " + (i + 1));
      unhighlightPseudoCode(5);
      highlightPseudoCode(6);

      removeColorWithAdjacencyMatrix(this.sortList.get(i), this.cSets.get(i)
          .get(0));

      lang.nextStep();

      highlightPseudoCode(7);
      indentColorSetArray();

      lang.nextStep();

      if (i < this.sortList.size() - 1) {
        variables.set("i", (i + 1) + "");
        updateCounter(i + 2);

      }
      lang.nextStep();
      unhighlightPseudoCode(6);
      unhighlightPseudoCode(7);
    }
    highlightPseudoCode(8);

    TrueFalseQuestionModel tfQuestion = new TrueFalseQuestionModel(
        "immerOptimaleLoesung");
    tfQuestion
        .setPrompt("Findet der Welsh-Powell Algorithmus immer die optimale Lösung des Färbungsproblems?");
    tfQuestion.setCorrectAnswer(false);
    tfQuestion.setPointsPossible(1);
    lang.addTFQuestion(tfQuestion);

    lang.nextStep();
    hideAll();

    createConclusion();
    lang.nextStep("Conclusion");

  }

  private void sortEgdesCounter(int[][] adjacencyMatrix) {
    int edgeCounter;
    ArrayList<Integer[]> sortList = new ArrayList<Integer[]>();
    for (int i = 0; i < adjacencyMatrix.length; i++) {
      edgeCounter = 0;
      for (int j = 0; j < adjacencyMatrix[i].length; j++) {
        edgeCounter += adjacencyMatrix[i][j];
      }

      sortList.add(new Integer[] { new Integer(i), new Integer(edgeCounter) });
    }

    Collections.sort(sortList, new Comparator<Integer[]>() {

      @Override
      public int compare(Integer[] o1, Integer[] o2) {
        return o1[1].compareTo(o2[1]) * -1;
      }
    });

    for (Integer[] integers : sortList) {
      this.sortList.add(integers[0]);
    }

  }

  /**
   * graph ”graph ” s i z e 5 d i r e c t e d w e i g h t e d nodes { ”A” ( 4 0
   * , 1 0 0 ) , ”B” ( 1 6 0 , 1 0 0 ) , ”C” ( 4 0 , 2 2 0 ) , ”D” ( 1 6 0 , 2 2
   * 0 ) , ”E” ( 1 0 0 , 1 6 0 ) } edges { (0 , 1 , ”15”) , (0 , 2 , ” 3 ” ) , (
   * 1 , 3 , ”7”) ( 4 , 2 , ” 8 ” ) , ( 2 , 0 , ” 1 1 ” ) } showIndices
   */
  private void createGraph(int[][] graphAdjacencyMatrix,
      Coordinates[] graphNodes, String[] labels, boolean leer) {

    Graph graph = lang.newGraph("welshPowell" + (leer ? "0" : "1"),
        graphAdjacencyMatrix, graphNodes, labels, null, this.graphProp);

    // this.graphProp
    this.allPrimitives.put(graph.getName(), graph);
  }

  private void initColorList() {
    for (int i = 0; i < this.sortList.size(); i++) {
      colors.add(new Color((2000 + i * 5555000) % 16777215));
    }
  }

  private void initcSets() {
    cSets = new ArrayList<ArrayList<Integer>>();
    for (int i = 0; i < colors.size(); i++) {
      ArrayList<Integer> intList = new ArrayList<Integer>();
      for (int j = 0; j <= i; j++) {
        intList.add(new Integer(j));
      }

      cSets.add(intList);
    }

  }

  private void createColorSetArrays(int xPos, int yPos) {
    for (int i = 0; i < colors.size(); i++) {

      Text colorSet = lang.newText(new Coordinates(xPos, yPos + i * 20), "X"
          + (i + 1) + ": \t C" + (i + 1) + " = {" + cSetString(cSets.get(i))
          + "}", "colorSet" + (i + 1), null, new TextProperties());
      allPrimitives.put(colorSet.getName(), colorSet);
    }
  }

  private String cSetString(ArrayList<Integer> set) {
    String numberString = "";
    for (int j = 0; j < set.size(); j++) {
      if (j > 0)
        numberString += ", ";
      numberString += (set.get(j) + 1);
    }

    return numberString;
  }

  private void createCircle(int nodeNumber, Color color) {
    int indexOfNode = this.sortList.indexOf(new Integer(nodeNumber));
    Graph graph = (Graph) this.allPrimitives.get("welshPowell1");
    CircleProperties circleProperties = new CircleProperties();
    circleProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
    circleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Coordinates coordinates = (Coordinates) graph.getNode(nodeNumber);
    Circle circle = lang.newCircle(new Coordinates(coordinates.getX()
        + (indexOfNode < 9 ? 8 : 10), coordinates.getY() + 8),
        indexOfNode < 9 ? 18 : 25, graph.getNodeLabel(nodeNumber) + "Circle",
        null, circleProperties);

    TextProperties datatextproperties = new TextProperties();
    datatextproperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.white);
    datatextproperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    Text text = lang.newText(coordinates, graph.getNodeLabel(nodeNumber),
        graph.getNodeLabel(nodeNumber) + "Text", null, datatextproperties);
    this.allPrimitives.put(circle.getName(), circle);
    this.allPrimitives.put(text.getName(), text);
  }

  private void removeColorWithAdjacencyMatrix(int nodeNumber, int colorNumber) {
    int[] adjacencyMatrix = ((Graph) this.allPrimitives.get("welshPowell1"))
        .getAdjacencyMatrix()[nodeNumber];

    for (int i = 0; i < adjacencyMatrix.length; i++) {
      if (adjacencyMatrix[i] == 1) {
        int setNumber = this.sortList.indexOf(new Integer(i));

        if (setNumber > this.sortList.indexOf(new Integer(nodeNumber))) {
          updateColorSetArray(setNumber, colorNumber, i);
        }

      }
    }

  }

  private void highlightPseudoCode(int lineNumber) {
    SourceCode code = (SourceCode) this.allPrimitives.get("pseudoCode");
    code.highlight(lineNumber);
  }

  private void unhighlightPseudoCode(int lineNumber) {
    SourceCode code = (SourceCode) this.allPrimitives.get("pseudoCode");
    code.unhighlight(lineNumber);
  }

  private boolean updateColorSetArray(int setNumber, int removeElement,
      int index) {
    Text colorSet = (Text) allPrimitives.get("colorSet" + (setNumber + 1));
    Graph graph = (Graph) this.allPrimitives.get("welshPowell1");
    ArrayList<Integer> newList = new ArrayList<Integer>(cSets.get(setNumber));
    boolean result = newList.remove(new Integer(removeElement));
    if (!result) {
      // colorSet.setText("unnoetiger aufruf", new TicksTiming(0),
      // new TicksTiming(10));
      return result;
    }

    colorSet.setText(graph.getNodeLabel(index) + ": \t C" + (setNumber + 1)
        + " = {" + cSetString(cSets.get(setNumber)) + "} - {"
        + (removeElement + 1) + "} = {" + cSetString(newList) + "}",
        new TicksTiming(0), new TicksTiming(10));
    cSets.set(setNumber, newList);
    variables.set("C" + (setNumber + 1), "{" + cSetString(cSets.get(setNumber))
        + "}");
    return result;
  }

  private void indentColorSetArray() {
    for (int i = 0; i < this.sortList.size(); i++) {
      Text colorSet = (Text) allPrimitives.get("colorSet" + (i + 1));
      colorSet.setText("X" + (i + 1) + ": \t C" + (i + 1) + " = {"
          + cSetString(cSets.get(i)) + "}", new TicksTiming(0),
          new TicksTiming(10));
    }

  }

  private void createCounterI(int xPos, int yPos) {
    Text counter = lang.newText(new Coordinates(xPos, yPos), "i = 1",
        "counter", null, new TextProperties());
    allPrimitives.put(counter.getName(), counter);
  }

  private void updateCounter(int iNew) {
    Text counter = (Text) allPrimitives.get("counter");
    counter.setText("i = " + iNew, new TicksTiming(0), new TicksTiming(10));
  }

  private SourceCode createSourceCode(Coordinates coordinates, String name,
      String text, SourceCodeProperties scProperties) {
    SourceCodeProperties properties = scProperties;
    if (properties == null) {
      properties = new SourceCodeProperties();
      properties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      properties.set(AnimationPropertiesKeys.FONT_PROPERTY, NORMALFont);

      properties
          .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    } else
      properties = sourceCode;
    SourceCode code = lang.newSourceCode(coordinates, name, null, properties);
    String[] splitText = text.split("\n");

    for (int i = 0; i < splitText.length; i++) {
      String str = splitText[i];
      int index = str.indexOf("#");
      String[] splitText2 = str.split("#");
      code.addCodeLine(index == -1 ? splitText2[0] : splitText2[1], null,
          index == -1 ? 2 : 4, null);
    }

    return code;
  }

  private void createConclusion() {
    SourceCode code = createSourceCode(new Coordinates(5, 50),
        "ConclusionText", CONCLUSION, null);
    this.allPrimitives.put(code.getName(), code);
  }

  // W
  private void createIntroduction() {
    SourceCode code = createSourceCode(new Coordinates(5, 50),
        "introductionText", Description, null);
    this.allPrimitives.put(code.getName(), code);
  }

  // W
  private void hideIntroduction() {
    this.allPrimitives.get("introductionText").hide();
  }

  private void createPseudoCode() {
    createTextWithBox("pseudoCodeheader", "Pseudo-Code",
        new Coordinates(20, 70), true, Color.WHITE);
    SourceCode code = createSourceCode(new Coordinates(20, 90), "pseudoCode",
        PSEUDOTEXT, sourceCode);
    this.allPrimitives.put(code.getName(), code);
  }

  private void createTextWithBox(String name, String title,
      Coordinates coordinates, boolean putPrimitive, Color bgColor) {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, HEADERFont);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    Text text = lang.newText(coordinates, title, name, null, textProperties);

    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, bgColor);
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect rect = lang.newRect(
        new Offset(-5, -5, text, AnimalScript.DIRECTION_NW), new Offset(5, 5,
            text, AnimalScript.DIRECTION_SE), "hRect", null, rectProperties);

    if (putPrimitive) {
      this.allPrimitives.put(text.getName(), text);
      this.allPrimitives.put(rect.getName(), rect);
    }
  }

  private void createHeader() {
    TextProperties headerProperties = new TextProperties();
    headerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    Text header = lang.newText(new Coordinates(20, 30),
        "Welsh-Powell Algorithmus", "header", null, headerProperties);

    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "hRect", null);
  }

  private void hideAll() {
    Iterator<Entry<String, Primitive>> it = allPrimitives.entrySet().iterator();
    while (it.hasNext()) {
      Entry<String, Primitive> pairs = it.next();
      Primitive element = (Primitive) pairs.getValue();
      element.hide();
      it.remove();
    }
  }

  private void createColorTable(int xPos, int yPos) {

    Text colorTableText = lang.newText(new Coordinates(xPos, yPos), "Nr.",
        "color_TableText", null, new TextProperties());
    PolylineProperties plineProperties = new PolylineProperties();
    Polyline polyline0 = lang.newPolyline(new Offset[] {
        new Offset(0, 5, colorTableText, AnimalScript.DIRECTION_SW),
        new Offset(80, 5, colorTableText, AnimalScript.DIRECTION_SW) },
        "color_TableHeadLine", null, plineProperties);
    Polyline polyline1 = lang.newPolyline(new Offset[] {
        new Offset(0, 10 + 20 * colors.size(), colorTableText,
            AnimalScript.DIRECTION_SW),
        new Offset(80, 10 + 20 * colors.size(), colorTableText,
            AnimalScript.DIRECTION_SW) }, "color_TableFootLine", null,
        plineProperties);
    Text text0 = lang.newText(new Offset(25, 0, colorTableText,
        AnimalScript.DIRECTION_NE), "Farbe", "color_TableTextFarbe", null,
        new TextProperties());
    Polyline polyline2 = lang.newPolyline(new Offset[] {
        new Offset(5, 0, colorTableText, AnimalScript.DIRECTION_NE),
        new Offset(5, 35 + colors.size() * 20, colorTableText,
            AnimalScript.DIRECTION_NE) }, "color_TableLeftLine", null,
        plineProperties);
    this.allPrimitives.put(polyline0.getName(), polyline0);
    this.allPrimitives.put(polyline1.getName(), polyline1);
    this.allPrimitives.put(polyline2.getName(), polyline2);
    this.allPrimitives.put(text0.getName(), text0);
    this.allPrimitives.put(colorTableText.getName(), colorTableText);
    RectProperties rectColorProperties = new RectProperties();
    rectColorProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
    rectColorProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    for (int i = 0; i < colors.size(); i++) {
      Text text = lang.newText(new Offset(-15, 10 + i * 20, colorTableText,
          AnimalScript.DIRECTION_SE), (i + 1) + "", "FarbeNr" + (i + 1), null,
          new TextProperties());
      rectColorProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
          colors.get(i));
      Rect rect = lang.newRect(new Offset(35, 12 + i * 20, colorTableText,
          AnimalScript.DIRECTION_SE), new Offset(45, 22 + i * 20,
          colorTableText, AnimalScript.DIRECTION_SE), "hRectFarbe" + (i + 1),
          null, rectColorProperties);
      this.allPrimitives.put(rect.getName(), rect);
      this.allPrimitives.put(text.getName(), text);
    }
  }

}
