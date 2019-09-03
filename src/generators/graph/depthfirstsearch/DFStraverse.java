package generators.graph.depthfirstsearch;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
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

public class DFStraverse implements Generator, ValidatingGenerator {
  private Language            lang;

  private String[]            nodeNames;
  private int[][]             adjazenzMatrix;
  private int[][]             coordinates;

  private Node[]              graphNodes;

  private int                 defaultXOffset      = 500;
  private int                 defaultYOffset      = 70;

  private LinkedList<String>  nichtBesuchteKnoten = new LinkedList<String>();
  private LinkedList<Integer> besuchteInts        = new LinkedList<Integer>();
  private int                 anzahlZugriffe      = 0;

  public void init() {
    lang = new AnimalScript("DFS - Tiefentraversierung",
        "Max Kolhagen,Patrick Lowin", 800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    this.random = new Random();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    nodeNames = (String[]) primitives.get("nodeNames");
    adjazenzMatrix = (int[][]) primitives.get("adjazenzMatrix");
    coordinates = (int[][]) primitives.get("coordinates");

    graphNodes = new Node[nodeNames.length];
    for (int i = 0; i < nodeNames.length; i++) {
      graphNodes[i] = new Coordinates((coordinates[i][0] + defaultXOffset),
          (coordinates[i][1] + defaultYOffset));
    }
    lang.setStepMode(true);
    dfsInit(adjazenzMatrix, graphNodes, nodeNames);
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "DFS - Tiefentraversierung";
  }

  public String getAlgorithmName() {
    return "DFS";
  }

  public String getAnimationAuthor() {
    return "Max Kolhagen, Patrick Lowin";
  }

  public String getDescription() {
    return "Tiefensuche (DFS = Depth first search) ist ein Verfahren, um uninformiert festzustellen, ob"
        + "<br/>"
        + "ein Knoten in einem Graphen enthalten ist, indem seine Wegmarkierung nach Durchfuehrung true ist."
        + "<br/>"
        + "<br/>"
        + "Der Algorithmus arbeitet wie folgt:"
        + "<br/>"
        + "1. Starte an die Suche an einem festgelegten Knoten"
        + "<br/>"
        + "2. Speichere alle unbesuchten Nachbarknoten in einem Stack (LIFO)"
        + "<br/>"
        + "3. Solange der Stack nicht leer ist"
        + "<br/>"
        + "	- entnehme den zuletzten gespeicherten Knoten"
        + "<br/>"
        + "	- setze seinen Status auf  -posbesucht-"
        + "<br/>"
        + "	- fuehre fuer diesen Knoten selbiges Verfahren durch..."
        + "<br/>"
        + "<br/>"
        + "BITTE BEACHTEN, wenn L = Laenge nodeNames ist, muss adjazenzMatrix[L][L] sein "
        + "und nur 0en oder 1en enthalten. Ausserdem muss coordinates[L][L] sein (Bug). X und Y Koordinate stehen dann in Spalte 1 und 2";
  }

  public String getCodeExample() {
    return "DFS(Knoten Vs)" + "\n" + "     lege an Stack S = leerer Stack"
        + "\n" + "     fuege ein Vs in S" + "\n"
        + "     solange gilt S ist nicht leer" + "\n"
        + "          entnehme Knoten Va aus S" + "\n"
        + "          falls Va.besucht = falsch" + "\n"
        + "               setze Va.besucht = wahr" + "\n"
        + "               fuer alle Nachbarknoten Vn = N(Va)" + "\n"
        + "                    falls Vn.besucht = falsch" + "\n"
        + "                         fuege ein Vn in S";
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

  // ////////////////////

  @SuppressWarnings("unused")
  public void dfsInit(int[][] graphAdjacencyMatrix, Node[] graphNodes,
      String[] labels) {

    // Header
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 25));
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    Text header = lang.newText(new Coordinates(300, 30),
        "DFS - Tiefentraversierung", "header", null, headerProps);

    RectProperties recProps = new RectProperties();
    recProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    recProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    recProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    recProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    Rect hRect = lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5,
        5, "header", "SE"), "hRect", null, recProps);

    // Intro
    TextProperties header2Props = new TextProperties();
    header2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    Text header2 = lang.newText(new Coordinates(50, 80), "Einleitung:",
        "header2", null, header2Props);

    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    SourceCode intro = lang.newSourceCode(new Offset(0, 3, "header2", "SW"),
        "intro", null, scProps);

    // set code lines for introText: line, name, indentation, display, delay
    intro
        .addCodeLine(
            "Tiefensuche (DFS = Depth first search) ist ein Verfahren, um uninformiert festzustellen, ob",
            null, 0, null);
    intro
        .addCodeLine(
            "ein Knoten in einem Graphen enthalten ist, indem seine Wegmarkierung nach Durchfuehrung true ist.",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine("Der Algorithmus arbeitet wie folgt:", null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine("1. Starte an die Suche an einem festgelegten Knoten",
        null, 0, null);
    intro.addCodeLine(
        "2. Speichere alle unbesuchten Nachbarknoten in einem Stack (LIFO)",
        null, 0, null);
    intro.addCodeLine("3. Solange der Stack nicht leer ist", null, 0, null);
    intro.addCodeLine("- entnehme den zuletzten gespeicherten Knoten", null, 1,
        null);
    intro.addCodeLine("- setze seinen Status auf 'besucht'", null, 1, null);
    intro.addCodeLine("- fuehre fuer diesen Knoten selbiges Verfahren durch",
        null, 1, null);

    // next step
    lang.nextStep("Introduction");
    intro.hide();
    header2.setText("Pseudo Code:", null, null);

    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Offset(0, 3, "header2", "SW"),
        "sourceCode", null, scProps);
    sc.addCodeLine("DFS(Knoten Vs)", null, 0, null);
    sc.addCodeLine("lege an Stack S = leerer Stack", null, 1, null);
    sc.addCodeLine("fuege ein Vs in S", null, 1, null);
    sc.addCodeLine("solange gilt <S ist nicht leer>", null, 1, null);
    sc.addCodeLine("entnehme Knoten Va aus S", null, 2, null);
    sc.addCodeLine("falls <Va.besucht = falsch>", null, 2, null);
    sc.addCodeLine("setze Va.besucht = wahr", null, 3, null);
    sc.addCodeLine("fuer <alle Nachbarknoten Vn = N(Va)>", null, 3, null);
    sc.addCodeLine("falls <Vn.besucht = falsch>", null, 4, null);
    sc.addCodeLine("fuege ein Vn in S", null, 5, null);

    lang.nextStep();

    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    graphProps
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
    graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    // graphProps.set(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY,
    // Color.GREEN);

    Graph graph = lang.newGraph("graph", graphAdjacencyMatrix, graphNodes,
        labels, null, graphProps);

    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLUE);
    String[] nodeNamenArray = new String[graph.getSize()];
    for (int i = 0; i < nodeNamenArray.length; i++) {
      nodeNamenArray[i] = graph.getNodeLabel(i);
    }

    StringArray isBesucht = lang.newStringArray(new Coordinates(400, 100),
        nodeNamenArray, "BesuchtArray", null, arrayProps);
    Text isBesuchtHeader = lang.newText(new Offset(-100, 5, "BesuchtArray",
        AnimalScript.DIRECTION_NW), "Besucht?: (gruen)", "isBesuchtHeader",
        null, new TextProperties());
    sc.highlight(0);
    lang.nextStep("Initialization");

    // Stack anlegen (1)
    sc.toggleHighlight(0, 1);
    ArrayProperties stackProps = new ArrayProperties();
    stackProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    stackProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    stackProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    stackProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    stackProps.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);

    String[] n = new String[graph.getSize()];
    for (int i = 0; i < n.length; i++)
      n[i] = "";
    StringArray stack = lang.newStringArray(new Coordinates(400, 160), n,
        "stack", null, stackProps);

    Text stackHeader = lang.newText(new Offset(-10, -16, "stack",
        AnimalScript.DIRECTION_NW), "Stack:", "stackHeader", null,
        new TextProperties());
    lang.nextStep();

    TrueFalseQuestionModel tfq = new TrueFalseQuestionModel(
        "trueFalseQuestion", false, 5);
    tfq.setPrompt("Ist Hoehensuche eine Alternative zur Tiefensuche?");
    tfq.setFeedbackForAnswer(false,
        "Richtig, Breitensuche waere eine Alternative.");
    tfq.setFeedbackForAnswer(true,
        "Leider falsch, Hoehensuche existiert nicht.");
    tfq.setGroupID("Second question group");
    lang.addTFQuestion(tfq);

    lang.nextStep();
    // wie Example
    try {
      traverse(isBesucht, stack, sc, graph);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
    sc.hide();
    stack.hide();
    stackHeader.hide();

    header2.setText("Resumee:", null, null);
    SourceCode outtro = lang.newSourceCode(new Offset(0, 3, "header2", "SW"),
        "outtro", null, scProps);
    outtro.addCodeLine("Man sieht nun sowohl in dem ", null, 0, null);
    outtro.addCodeLine("Array, als auch in dem", null, 0, null);
    outtro.addCodeLine("Graphen, welche Knoten durch", null, 0, null);
    outtro.addCodeLine("DFS traversiert wurden, und", null, 0, null);
    outtro.addCodeLine("", null, 0, null);
    outtro.addCodeLine("Die Komplexitaet von DFS betraegt O(n)", null, 0, null);
    outtro.addCodeLine("", null, 0, null);
    outtro.addCodeLine("Folgene Knoten wurden nicht traversiert:", null, 0,
        null);
    outtro.addCodeLine(nichtBesuchteKnoten.toString(), null, 0, null);
    outtro.addCodeLine("", null, 0, null);
    outtro.addCodeLine("Es wurde auf " + anzahlZugriffe
        + " Knoten zugegriffen.", null, 0, null);
    // traversierte Knoten markieren
    for (int i = 0; i < besuchteInts.size(); i++) {
      graph.highlightNode(i, null, null);
    }
  }

  public void traverse(StringArray isBesucht, StringArray theStack,
      SourceCode sc, Graph graph) {
    StringArray stack = theStack;
    // array Pos fuers highlightening
    int currentNode;
    int[][] matrix = graph.getAdjacencyMatrix();

    Boolean[] internBesucht = new Boolean[isBesucht.getLength()];
    for (int i = 0; i < internBesucht.length; i++) {
      internBesucht[i] = false;
    }

    ArrayMarkerProperties arrayMarkerProps = new ArrayMarkerProperties();
    arrayMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ArrayMarker marker = lang.newArrayMarker(isBesucht, 0, "arrayMarker", null,
        arrayMarkerProps);
    marker.hide();
    ArrayMarker marker2 = lang.newArrayMarker(stack, 0, "stackMarker", null,
        arrayMarkerProps);
    marker2.hide();

    // Vs in Stack (2)
    sc.toggleHighlight(1, 2);
    graph.highlightNode(0, null, null);
    stack.put(0, graph.getNodeLabel(0), null, null);
    lang.nextStep();

    // while S != empty (3)
    graph.unhighlightNode(0, null, null);
    while (!stack.getData(0).equals("")) {
      sc.unhighlight(2);
      sc.unhighlight(7);
      sc.unhighlight(8);
      sc.unhighlight(9);
      sc.highlight(3);
      marker2.show();
      lang.nextStep();

      // nimm Va aus Stack (4)

      FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
          "fillInBlanksQuestion" + UUID.randomUUID());
      fibq.setPrompt("Welcher Knoten wird aus dem Stack genommen?");
      fibq.addAnswer(("" + stack.getData(0)).toLowerCase(), 10,
          "Das war zu einfach... ;)");
      fibq.addAnswer(("" + stack.getData(0)).toUpperCase(), 10,
          "Das war zu einfach... ;)");

      fibq.setGroupID("First question group");

      if (askQuestion()) {
        lang.addFIBQuestion(fibq);
        lang.nextStep();
      }

      anzahlZugriffe++;
      sc.toggleHighlight(3, 4);
      marker2.hide();
      currentNode = findArrayPos(isBesucht, stack.getData(0));
      stack = pop2(stack);
      graph.highlightNode(currentNode, null, null);
      marker.move(currentNode, null, null);
      lang.nextStep();

      // if(Va.visit == false) (5)
      sc.toggleHighlight(4, 5);
      marker.show();
      lang.nextStep();

      // Va.visit = true (6)
      if (!internBesucht[currentNode]) {
        sc.toggleHighlight(5, 6);
        isBesucht.highlightCell(currentNode, null, null);
        internBesucht[currentNode] = true;
        lang.nextStep();

        // for all Neighbors (7) problem: reihenfolge z.B. bei kante a -
        // e --> visuell "komische" Reihenfolge
        marker.hide();
        for (int i = 0; i < isBesucht.getLength(); i++) {
          sc.unhighlight(6);
          sc.unhighlight(8);
          sc.unhighlight(9);
          sc.highlight(7);
          if (matrix[currentNode][i] == 1) {
            graph.highlightEdge(currentNode, i, null, null);
            graph.highlightEdge(i, currentNode, null, null);
            lang.nextStep();

            // falls Vn.visit == false (8)
            sc.toggleHighlight(7, 8);
            marker.move(i, null, null);
            marker.show();
            if (internBesucht[i] == false) {
              lang.nextStep();

              // Stack.push Vn (9)
              sc.toggleHighlight(8, 9);
              stack = push2(stack, graph.getNodeLabel(i));
            }
            lang.nextStep();
            marker.hide();
            graph.unhighlightEdge(currentNode, i, null, null);
            graph.unhighlightEdge(i, currentNode, null, null);
          }
        }
      } else {
        sc.unhighlight(5);
        marker.hide();
        lang.nextStep();
      }
      graph.unhighlightNode(currentNode, null, null);
    }

    for (int i = 0; i < internBesucht.length; i++) {
      if (internBesucht[i] == false)
        nichtBesuchteKnoten.add(graph.getNodeLabel(i));
      else
        besuchteInts.add(i);
    }
    lang.nextStep();

    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion");
    mcq.setPrompt("Wie viele Knoten wurden traversiert?");

    mcq.addAnswer("" + (anzahlZugriffe - 1), 0, "Falsch!");
    mcq.addAnswer("" + anzahlZugriffe, 10, "gut gemacht!");
    mcq.addAnswer("" + (anzahlZugriffe / 2), 0, "Falsch!");
    mcq.addAnswer("" + (anzahlZugriffe * 3), 0, "Falsch!");
    mcq.setGroupID("First question group");
    lang.addMCQuestion(mcq);

    lang.nextStep("Resumee");
  }

  // Buchstabe zu zahl
  public int findArrayPos(StringArray isBesucht, String toFind) {
    for (int i = 0; i < isBesucht.getLength(); i++) {
      if (isBesucht.getData(i).equals(toFind))
        return i;
    }

    return -1;
  }

  // stackarray nach dem pop shiften , DAVOR den Wert entnehmen
  public StringArray pop(StringArray pushStack) {
    pushStack.put(0, null, null, null);
    for (int i = 0; i < pushStack.getLength() - 1; i++) {
      pushStack.swap(i, i + 1, null, null);
    }
    return pushStack;
  }

  // neuer Versuch
  public StringArray pop2(StringArray pushStack) {
    for (int i = 0; i < pushStack.getLength() - 1; i++) {
      pushStack.put(i, pushStack.getData(i + 1), null, null);
    }
    return pushStack;
  }

  // etwas pushen und stackarray shiften
  public StringArray push(StringArray pushStack, String toPush) {
    for (int i = pushStack.getLength() - 1; i > 0; i--) {
      pushStack.swap(i, i - 1, null, null);
    }
    pushStack.put(0, toPush, null, null);
    return pushStack;
  }

  // put besser als swap
  public StringArray push2(StringArray pushStack, String toPush) {
    for (int i = pushStack.getLength() - 1; i > 0; i--) {
      pushStack.put(i, pushStack.getData(i - 1), null, null);
    }
    pushStack.put(0, toPush, null, null);
    return pushStack;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    String[] nodeNames2 = (String[]) arg1.get("nodeNames");
    int[][] adjazenzMatrix2 = (int[][]) arg1.get("adjazenzMatrix");
    int[][] coordinates2 = (int[][]) arg1.get("coordinates");
    int length = nodeNames2.length;

    if (coordinates2.length != length || adjazenzMatrix2.length != length) {
      throw new IllegalArgumentException(
          "Falsche Variableneingabe. Die Anzahl der Eintraege in nodeNames muss jeweils"
              + " der Breite und Hoehe der Adjazenzmatrix entsprechen, sowie der Anzahl (Hoehe)"
              + " der Koordinaten. ");
    }
    for (int j = 0; j < length; j++) {
      if (adjazenzMatrix2[j].length != length
      /* || coordinates2[j].length != 2 geht leider nicht */)
        throw new IllegalArgumentException(
            "Falsche Variableneingabe. Die Breite der Koordinaten muss 2 betragen.");
      for (int i = 0; i < length; i++) {
        if (!(adjazenzMatrix2[j][i] == 0 || adjazenzMatrix2[j][i] == 1))
          throw new IllegalArgumentException(
              "Falsche Variableneingabe. In der Adjazenzmatrix duerfen nur 0en (kein Pfad)"
                  + " und 1en (Pfad vorhanden) stehen.");
      }
    }
    return true;
  }

  /**
   * all needed data for determine random values
   */
  private Random  random              = null;
  private int     questionProbability = 30;

  private boolean showQuestions       = true;

  /**
   * determines whether a certain question should be shown or not (determined
   * randomly)
   */
  private boolean askQuestion() {
    // check if asking questions is enabled
    if (!this.showQuestions)
      return false;

    // determine random value from 0 to 99
    int n = this.random.nextInt(100);

    // check probability
    return (n < questionProbability);
  }
}