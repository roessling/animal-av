package generators.misc.nonuniformTM;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.Ellipse;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.variables.Variable;
import animal.variables.VariableRoles;

/*
 * SpaceComplexity.java Giang Nam Nguyen, 2015 for the Animal project at TU Darmstadt.
 */
public class SpaceComplexity implements Generator {
  // example 1
  // public static final int n = 3;
  // public static final int s_n = 3;

  // example 2
  /**
   * Length of input bit sequence.
   */
  public static final int n = 4;
  /**
   * Number of gates in circuit.
   */
  public static final int s_n = 4;

  // Ex.3
  // public static final int n = 4;
  // public static final int s_n = 3;

  /**
   * Number of bits required to enumerate bits and gates.
   */
  public static final int numberOfBits = ((int) (Math.log(Math.max(n, s_n)) / Math.log(2))) + 1;

  public int longestPath;
  /**
   * n bits input.
   */
  private int[] input;

  // private static int[] input = {1, 0, 1};

  // private static int[] input = {1, 0, 1, 0};
  /**
   * Output gates. (root node in circuit tree)
   */
  private Node root;
  private Node[] circuit;
  private HashMap<Integer, ArrayList<Integer>> levelGate;

  // Visualization part.
  private Language lang;

  private IntArray inputTape;
  private StringArray oracleTape;
  private StringArray outputTape;
  private StringArray counterTape;
  private Timing defaultTiming;
  // input bits
  private Circle[] x;
  // gates
  private Ellipse[] e;

  private ArrayList<String> oracle;

  private ArrayList<String> outputArrayList;

  private ArrayList<String> counterTapeString;

  private SourceCode sc;
  private SourceCode hintTMstate;

  // Properties.
  private ArrayProperties array;
  private EllipseProperties gate;
  private CircleProperties inputBits;
  private ArrayMarkerProperties arrayMarker;
  private TextProperties title;
  private TextProperties header1;
  private TextProperties header2;
  private TextProperties text;
  private TextProperties hint;
  private SourceCodeProperties sourceCode;

  // Markers.
  private ArrayMarker inputTapeMarker;
  private ArrayMarker oracleTapeMarker;
  private ArrayMarker outputTapeMarker;
  private ArrayMarker counterTapeMarker;

  // TM's state
  private Text stateText;
  private String stateString;

  /**
   * Can hold values -1, 0 or 1. -1 means the state memory is free.
   */
  private int stateMemory = -1; // firstly the memory is free

  private int[] currentInputs = {-1, -1};

  // hint TM tapes
  private Text hintInput;
  private Text hintOutput;
  private Text hintCounter;
  private Text hintOracle;
  private Text hintTM;
  // to overcome a bug with Const.topBorderHorizontal
  public static final int topBorderHorizontal = 300;

  // closing words
  public static final String conclusion1 =
      "Die Animation zeigt, dass ein Schaltkreis durch eine nichtuniforme Turingmaschine simuliert werden kann.";
  public static final String conclusion2 =
      "Der Platzbedarf der Simulation hängt linear von der Schaltkreistiefe ab.";

  /**
   * Constructor.
   */
  public SpaceComplexity() {

  }

  /**
   * Initialize properties.
   */
  public void init() {
    lang =
        new AnimalScript(
            "Zeitbedarf der Simulation von Schaltkreisen durch nichtuniforme Turingmaschinen",
            "Giang Nam Nguyen", 1500, 800);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    // text properties
    // title = new TextProperties();
    // title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
    //
    // header1 = new TextProperties();
    // header1.set(AnimationPropertiesKeys.FONT_PROPERTY,
    // new Font(Font.SANS_SERIF, Font.BOLD, 18));
    //
    // header2 = new TextProperties();
    // header2.set(AnimationPropertiesKeys.FONT_PROPERTY,
    // new Font(Font.SANS_SERIF, Font.BOLD, 15));
    //
    // hint = new TextProperties();
    // hint.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    // hint.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    // array properties
    // arrayProps = new ArrayProperties();
    // arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    // arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    //
    // ellipseProps = new EllipseProperties();
    // ellipseProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // ellipseProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    //
    // circleProps = new CircleProperties();
    // circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    //
    // markerProps = new ArrayMarkerProperties();
    // markerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // default timing
    defaultTiming = new TicksTiming(15);

    // first, set the visual properties for the source code
    // scProps = new SourceCodeProperties();
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  /**
   * Show theorem and proof.
   */
  private void showProof() {
    lang.nextStep("1. Theorem und Beweisskizze");
    lang.newText(new Offset(-3, 40, "title", "NW"), "Theorem:", "theorem", null, header1);
    lang.newText(
        new Offset(17, -22, "theorem", "SE"),
        "Das zur Schaltkreisfamilie S = (S_n) gehörende Entscheidungsproblem A_f kann von einer nichtuniformen",
        "", null, text);
    lang.newText(new Offset(10, 5, "theorem", "SE"),
        "Turingmaschine auf Platz O(d*(n)) gelöst werden.", "", null, text);
    lang.nextStep("Theorem");
    lang.newText(new Offset(0, 45, "theorem", "NW"), "Beweisskizze:", "proof", null, header1);
    lang.nextStep("Beweisskizze");
    lang.newText(
        new Offset(0, 10, "proof", "SW"),
        "- Für einen Schaltkreis der Tiefe d(n) braucht die TM d(n) Plätze auf dem 1. Arbeitsband.",
        "proofLine1", null, text);
    lang.nextStep();
    sc = lang.newSourceCode(new Offset(0, 5, "proofLine1", "SW"), "sourceCode", null, sourceCode);
    sc.addCodeLine("Diese Behauptung beweisen wir mit Induktion.", null, 0, null);
    lang.nextStep();
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine(
        "IA: zum Auswerten eines Schaltkreises der Tiefe 1 braucht die Turingmaschine nur einen Speicherplatz.",
        null, 0, null);
    sc.addCodeLine("Beispiel: G = x1 AND x2.", null, 1, null);
    sc.addCodeLine("- Die TM liest den Schaltkreis in Post-order als x1|x2|AND.", null, 1, null);
    sc.addCodeLine("- Die TM merkt einen Eingabewert in ihrem Zustand.", null, 1, null);
    sc.addCodeLine("- Den anderen Eingabewert liest die TM aus dem Arbeitsband.", null, 1, null);
    sc.addCodeLine(
        "- Nach der Auswertung überschreibt die TM den Eingabewert auf dem Arbeitsband mit dem Wert des Bausteins.",
        null, 1, null);
    sc.addCodeLine("- Dafür nutzt die TM nur einen Speicherplatz.", null, 1, null);
    sc.addCodeLine("", null, 0, null);
    lang.nextStep();
    sc.addCodeLine(
        "IV: wir nehmen an, dass die TM für einen Schaltkreis der Tiefe d(n)-1 d(n)-1 Speicherplätze braucht.",
        null, 0, null);
    sc.addCodeLine("", null, 0, null);
    lang.nextStep();
    sc.addCodeLine(
        "IS: wir beweisen, dass d(n) Plätze für einen Schaltkreis der Tiefe d(n) auf dem Arbeitsband benötigt werden.",
        null, 0, null);
    sc.addCodeLine(
        "- Als eine boolesche Formel hat ein Schaltkreis der Tiefe d(n) zwei Teilformeln der Tiefe maximal d(n)-1.",
        null, 1, null);
    sc.addCodeLine(
        "- Für die linke Teilformel braucht die Turingmaschine nach Induktionsvoraussetzung maximal d(n) − 1 Speicherplätze.",
        null, 1, null);
    sc.addCodeLine("- Nach der Auswertung der linken Teilformel ist ein Platz besetzt.", null, 1,
        null);
    sc.addCodeLine(
        "- Zur Auswertung der rechten Teilformel braucht die Turingmaschine wieder höchstens d(n) − 1 Plätze. In diesem Moment sind alle d(n) Speicherplätze benötigt.",
        null, 1, null);
    sc.addCodeLine("- Nach der Auswertung der rechten Teilformel sind zwei Plätze besetzt.", null,
        1, null);
    sc.addCodeLine(
        "- Nach der Auswertung der Wurzel bleibt nur noch ein Speicherplatz belegt. Also sind d(n) Speicherplätze genug zum Auswerten einer Formel der Tiefe d(n).",
        null, 1, null);
    lang.nextStep();
    sc.hide();
    lang.nextStep();
    lang.newText(new Offset(0, 10, "proofLine1", "SW"),
        "- Das 2. Arbeitsband dient als ein Zähler, dafür werden O(log n) Plätze benötigt.",
        "proofLine2", null, text);
    lang.nextStep();
    lang.newText(
        new Offset(0, 10, "proofLine2", "SW"),
        "- D.h., insgesamt braucht die TM O(log n + d(n)) = O(d*(n)) Plätze (für d*(n) = max {log n, d(n)}).",
        "", null, text);
    lang.nextStep();
  }

  /**
   * Draw the given circuit.
   */
  private void showCircuit(int[] inputArray) {
    // init given circuit
    circuit = new Node[n + s_n];

    // assign input bits
    for (int i = 0; i < n; i++) {
      circuit[i] = new Input(i, null, null, inputArray[i]);
    }
    // init logic gates
    // example 1
    // circuit[3] = new Gate(0, circuit[1], circuit[0], "AND");
    // circuit[4] = new Gate(1, circuit[2], circuit[1], "AND");
    // circuit[5] = new Gate(2, circuit[4], circuit[3], "OR");
    // root = circuit[5];

    // example 2
    circuit[4] = new Gate(0, circuit[1], circuit[0], "OR");
    circuit[5] = new Gate(1, null, circuit[2], "NOT");
    circuit[6] = new Gate(2, circuit[3], circuit[5], "AND");
    circuit[7] = new Gate(3, circuit[6], circuit[4], "AND");
    root = circuit[7];

    // ex.3
    // circuit[4] = new Gate(0, circuit[1], circuit[0], "AND");
    // circuit[5] = new Gate(1, circuit[2], circuit[4], "OR");
    // circuit[6] = new Gate(2, circuit[3], circuit[5], "OR");
    // root = circuit[6];

    // find level (depth) of gates
    levelGate = new HashMap<Integer, ArrayList<Integer>>();

    longestPath = getDepth(root);

    // each input bit is rendered as a circle with a label x_i
    x = new Circle[n];
    String inputBitName;
    String inputBitLabel;

    // draw input bits
    for (int i = 0; i < n; i++) {
      inputBitName = "x" + Integer.toString(i + 1);
      x[i] =
          lang.newCircle(new Coordinates(Const.leftBorderVertical + i * Const.drawShiftHorizontal,
              topBorderHorizontal), 5, inputBitName, null);
      inputBitLabel = "bit" + Integer.toString(i + 1);
      // text offset (10, -5, "E")
      lang.newText(new Offset(10, -5, inputBitName, "E"), inputBitName, inputBitLabel, null);
    }

    // title
    lang.newText(new Offset(-25, -50, "bit1", "NW"), "Schaltkreis:", "", null, header1);

    // draw gates
    e = new Ellipse[s_n];
    String gateName;
    String operatorLabel;
    Coordinates[] vertices;
    Coordinates leftChild, rightChild;
    PolylineProperties polylineProps = new PolylineProperties();
    polylineProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);

    for (int i = 1; i <= longestPath; i++) {
      ArrayList<Integer> gateNumbers = levelGate.get(i);
      Collections.sort(gateNumbers);
      // draw gates on the same level
      for (Iterator iterator = gateNumbers.iterator(); iterator.hasNext();) {
        Integer number = (Integer) iterator.next();

        Node currentNode = circuit[number + n];
        rightChild = getChildCoordinates(currentNode.getRightChild());

        gateName = "G" + Integer.toString(number + 1);
        e[number] =
            lang.newEllipse(new Coordinates(rightChild.getX(), topBorderHorizontal + i
                * Const.drawShiftVertical), Const.ellipseSize, gateName, null);

        lang.newText(new Coordinates(rightChild.getX() + 35, topBorderHorizontal + i
            * Const.drawShiftVertical), gateName, "", null);

        operatorLabel = ((Gate) currentNode).getValue();
        lang.newText(new Coordinates(rightChild.getX() - 13, topBorderHorizontal + i
            * Const.drawShiftVertical - 5), operatorLabel, operatorLabel, null);

        vertices = new Coordinates[2];

        vertices[0] =
            new Coordinates(rightChild.getX(), topBorderHorizontal + i * Const.drawShiftVertical
                - 13);

        // NOT gates have only one input
        if (!((Gate) currentNode).getValue().equals("NOT")) {
          leftChild = getChildCoordinates(currentNode.getLeftChild());
          // draw line from node to its left child
          vertices[1] = leftChild;
          lang.newPolyline(vertices, "", null, polylineProps);
        }
        // draw line from node to its right child
        vertices[1] = rightChild;
        lang.newPolyline(vertices, "", null, polylineProps);
      }
    }

    // generate some questions
    lang.nextStep();

    // show circuit info.
    // lang.newText(new Coordinates(Const.leftBorderVertical - 15, topBorderHorizontal + longestPath
    // * Const.drawShiftVertical + 40), "n = " + Integer.toString(n), "circuitInfo1", null);
    // lang.newText(new Offset(0, 15, "circuitInfo1", "SW"),
    // "Schaltkreisgröße s(n) = " + Integer.toString(s_n), "circuitInfo2", null);
    // lang.newText(new Offset(0, 15, "circuitInfo2", "SW"),
    // "Schaltkreistiefe d(n) = " + Integer.toString(longestPath), "circuitInfo3", null);
    // lang.newText(
    // new Offset(0, 15, "circuitInfo3", "SW"),
    // "Anzahl der Bits auf dem 2. Arbeitsband = max{log s(n), log n} + 1 = "
    // + Integer.toString(numberOfBits), "circuitInfo4", null);
    // lang.newText(
    // new Offset(0, 15, "circuitInfo4", "SW"),
    // "d*(n) = max {d(n), log n} = "
    // + Integer.toString(Utils.max(longestPath, (int) Math.ceil(Math.log(n)))),
    // "circuitInfo3", null);

    Random ran = new Random();
    int x = ran.nextInt(2);
    MultipleChoiceQuestionModel q;

    // either show question or not
    if (x == 1) {
      q = new MultipleChoiceQuestionModel("q1");
      q.setPrompt("Wie hoch ist die Schaltkreisgröße s(n) in dem Beispiel?");
      q.addAnswer("2", 0,
          "Falsch. Es gibt 4 Bausteine in diesem Beispiel. Die richtige Antwort ist s(n) = 4.");
      q.addAnswer("3", 0,
          "Falsch. Es gibt 4 Bausteine in diesem Beispiel. Die richtige Antwort ist s(n) = 4.");
      q.addAnswer("4", 1, "Richtig!");
      lang.addMCQuestion(q);
      lang.nextStep();
    }

    // either show question or not
    x = ran.nextInt(2);
    if (x == 1) {
      q = new MultipleChoiceQuestionModel("q2");
      q.setPrompt("Wie hoch ist die Schaltkreistiefe d(n) in dem Beispiel?");
      q.addAnswer("2", 0,
          "Falsch. Der längste Weg ist (x3, G2, G3, G4). Die richtige Antwort ist d(n) = 3.");
      q.addAnswer("3", 1, "Richtig!");
      q.addAnswer("4", 0,
          "Falsch. Der längste Weg ist (x3, G2, G3, G4). Die richtige Antwort ist d(n) = 3.");
      lang.addMCQuestion(q);
      lang.nextStep();
    }

    // either show question or not
    x = ran.nextInt(2);
    if (x == 1) {
      q = new MultipleChoiceQuestionModel("q3");
      q.setPrompt("Wie viel Bits werden auf dem 2. Arbeitsband benötigt? Hinweis: Betrachten Sie die Eingabelänge bzw. die Anzahl der Bausteine (Schaltkreisgröße).");
      q.addAnswer(
          "2",
          0,
          "Falsch. Die Eingabelänge ist 4. Die Eingaben werden beginnend bei 1 aufsteigend durchnummeriert. Deshalb werden 3 Bits auf dem 2. Arbeitsband benötigt.");
      q.addAnswer("3", 1, "Richtig!");
      q.addAnswer(
          "4",
          0,
          "Falsch. Die Eingabelänge ist 4. Die Eingaben werden beginnend bei 1 aufsteigend durchnummeriert. Deshalb werden 3 Bits auf dem 2. Arbeitsband benötigt.");
      lang.addMCQuestion(q);
    }
  }

  /**
   * Show the non-uniform Turing machine.
   */
  private void showTM() {
    // title
    lang.newText(new Coordinates(Const.leftBorderVerticalTM, topBorderHorizontal - 50),
        "Nichtuniforme Turingmaschine:", "", null, header1);

    // arithmetic unit of TM
    lang.newText(new Coordinates(Const.leftBorderVerticalTM, topBorderHorizontal), "Zustand: ",
        "t5", null, header2);

    stateString = "[q0, -]";
    stateText = lang.newText(new Offset(80, -15, "t5", "E"), stateString, "", null);

    hintTMstate = lang.newSourceCode(new Offset(0, 5, "t5", "SW"), "sourceCode", null, sourceCode);

    hintTMstate.addCodeLine("Hinweis:", null, 0, null);
    hintTMstate.addCodeLine("[q0, x]: Anfangszustand und Endzustand,", null, 0, null);
    hintTMstate.addCodeLine("[q1, x]: Zustand, indem AND-Operator gemerkt wird,", null, 0, null);
    hintTMstate.addCodeLine("[q2, x]: Zustand, indem OR-Operator gemerkt wird,", null, 0, null);
    hintTMstate.addCodeLine("[q3, x]: Zustand, indem XOR-Operator gemerkt wird,", null, 0, null);
    hintTMstate.addCodeLine("[q4, x]: Zustand, indem NOT-Operator gemerkt wird,", null, 0, null);
    hintTMstate.addCodeLine("für x in {1, 0, -}.", null, 0, null);

    // borders
    lang.newRect(new Offset(-10, -10, "t5", "NW"), new Offset(275, 140, "t5", "SE"), "", null);

    // hint for TM
    hintTM = lang.newText(new Offset(285, -5, "t5", "NE"), "", "hintTM", null, hint);

    // input band
    lang.newText(new Offset(0, 200, "t5", "SW"), "Eingabeband", "t1", null, header2);
    inputTape = lang.newIntArray(new Offset(50, -8, "t1", "E"), input, "inputBand", null, array);
    hintInput =
        lang.newText(new Offset(n * Const.shiftLeftChar, -7, "inputBand", "E"), "", "hintInput",
            null, hint);

    // oracle band
    lang.newText(new Offset(0, 53, "t1", "SW"), "Orakelband", "t2", null, header2);

    // working band 1
    lang.newText(new Offset(0, 53, "t2", "SW"), "1. Arbeitsband", "t3", null, header2);

    // display working band 1
    outputArrayList = new ArrayList<String>();
    outputArrayList.add("  ");
    outputTape =
        lang.newStringArray(new Offset(0, 115, "inputBand", "SW"),
            outputArrayList.toArray(new String[outputArrayList.size()]), "workingBand1", null,
            array);

    hintOutput =
        lang.newText(new Offset(s_n * (Const.shiftLeftChar + 5), -5, "workingBand1", "E"), "",
            "hintOuput", null, hint);

    // working band 2
    lang.newText(new Offset(0, 53, "t3", "SW"), "2. Arbeitsband", "t4", null, header2);
    lang.nextStep("3. Nichtuniforme Turingmaschine");
    // create OracleTape
    hintOracle =
        lang.newText(new Offset(0, 5, "t2", "SW"), "Der Schaltkreis wird in Postorder gelesen.",
            "hintOracle", null, hint);
    lang.nextStep("Orakel = Durchlauf des Schaltkreises in Post-Order");
    oracle = new ArrayList<String>();
    postorder(root);

    lang.nextStep();
    hintOracle.hide();

    // init a blank counter tape
    counterTapeString = new ArrayList<String>();
    for (int i = 0; i < numberOfBits; i++)
      counterTapeString.add("  ");

    counterTape =
        lang.newStringArray(new Offset(0, 120, "oracleBand", "SW"),
            counterTapeString.toArray(new String[counterTapeString.size()]), "workingBand2", null,
            array);
    hintCounter =
        lang.newText(new Offset(counterTapeString.size() * Const.shiftLeftChar, -5, "workingBand2",
            "E"), "", "hintCounter", null, hint);
  }

  /**
   * Post-order traverse on a node x.
   * 
   * @param x a node
   */
  private void postorder(Node x) {
    if (x instanceof Input) {
      showOrHideAChildNode(x, true);
      addToOracleTape("x");
      String decToBin = Utils.decToBin(x.getNumber() + 1, numberOfBits);
      for (int i = 0; i < decToBin.length(); i++) {
        addToOracleTape(Character.toString(decToBin.charAt(i)));
      }
      lang.nextStep();
      showOrHideAChildNode(x, false);
    } else {
      postorder(x.getRightChild());
      if (!((Gate) x).getValue().equals("NOT")) {
        postorder(x.getLeftChild());
      }
      showOrHideAChildNode(x, true);
      addToOracleTape(((Gate) x).getValue());
      lang.nextStep();
      showOrHideAChildNode(x, false);
    }
  }

  /**
   * Add a string to oracle tape.
   * 
   * @param s a string
   */
  private void addToOracleTape(String s) {
    lang.nextStep();
    oracle.add(" ");
    oracleTape =
        lang.newStringArray(new Offset(0, 48, "inputBand", "SW"),
            oracle.toArray(new String[oracle.size()]), "oracleBand", null, array);
    lang.nextStep();
    oracleTape.put(oracle.size() - 1, s, null, defaultTiming);
    oracle.set(oracle.size() - 1, s);
  }

  /**
   * Highlight or unhighlight a node on circuit graph.
   * 
   * @param child a Node
   * @param showHide true when highlighting, false when unhighlighting
   */
  private void showOrHideAChildNode(Node child, boolean showHide) {
    if (showHide) {
      if (child instanceof Input) {
        x[child.getNumber()] =
            lang.newCircle(x[child.getNumber()].getCenter(), x[child.getNumber()].getRadius(),
                x[child.getNumber()].getName(), null, inputBits);
      } else {
        e[child.getNumber()] =
            lang.newEllipse(e[child.getNumber()].getCenter(), e[child.getNumber()].getRadius(),
                e[child.getNumber()].getName(), null, gate);

        lang.newText(new Coordinates(((Coordinates) e[child.getNumber()].getCenter()).getX() - 13,
            ((Coordinates) e[child.getNumber()].getCenter()).getY() - 5),
            ((Gate) child).getValue(), "", null);
      }
      lang.nextStep();
    } else {
      if (child instanceof Input) {
        x[child.getNumber()].hide();
      } else {
        e[child.getNumber()].hide();
      }
      lang.nextStep();
    }
  }

  /**
   * Get coordinates of a given child.
   * 
   * @param child
   * @return
   */
  private Coordinates getChildCoordinates(Node child) {
    int posX = 0;
    int posY = 0;
    Coordinates tmp;
    if (child instanceof Input) {
      tmp = (Coordinates) x[child.getNumber()].getCenter();
      posX = tmp.getX();
      posY = tmp.getY() + 4;
    } else {
      tmp = (Coordinates) e[child.getNumber()].getCenter();
      posX = tmp.getX();
      posY = tmp.getY() + 13;
    }
    return new Coordinates(posX, posY);
  }

  /**
   * Get depth of a node.
   * 
   * @param n a given node.
   * @return depth
   */
  private int getDepth(Node n) {
    if (n instanceof Input) {
      addGateToLevel(n.getNumber(), 0);
      return 0;
    } else {
      int max = 0;
      if ((n.getLeftChild() != null) && (n.getRightChild() != null)) {
        max = Utils.max(getDepth(n.getLeftChild()), getDepth(n.getRightChild()));
      } else if (n.getLeftChild() == null) {
        max = getDepth(n.getRightChild());
      } else if (n.getRightChild() == null) {
        max = getDepth(n.getLeftChild());
      }
      int level = 1 + max;
      addGateToLevel(n.getNumber(), level);
      return level;
    }
  }

  /**
   * Add a gate number to corresponding level in levelGate.
   * 
   * @param number gate number
   * @param level corresponding level
   */
  private void addGateToLevel(int number, int level) {
    if (levelGate.containsKey(level)) {
      levelGate.get(level).add(number);
    } else {
      levelGate.put(level, new ArrayList<Integer>() {
        {
          add(number);
        }
      });
    }
  }

  /**
   * Run simulation with user's input.
   * 
   * @param array user's input array.
   */
  public void runSimulation(int[] array) {
    // show title of the algorithm
    lang.newRect(new Coordinates(5, 5), new Coordinates(1000, 35), "", null);
    lang.newText(new Coordinates(10, 15),
        "Platzbedarf der Simulationen von Schaltkreisen durch nichtuniforme Turingmaschinen",
        "title", null, title);

    showProof();
    showCircuit(array);
    lang.nextStep("2. Boolescher Schaltkreis");
    showTM();

    lang.nextStep("4. Simulation");
    // init array marker
    inputTapeMarker = lang.newArrayMarker(inputTape, 0, "inputTapeMarker", null, arrayMarker);
    oracleTapeMarker = lang.newArrayMarker(oracleTape, 0, "oracleMarker", null, arrayMarker);
    outputTapeMarker = lang.newArrayMarker(outputTape, 0, "outputTapeMarker", null, arrayMarker);
    counterTapeMarker = lang.newArrayMarker(counterTape, 0, "counterTapeMarker", null, arrayMarker);

    boolean countDown = false;
    int bitCount = 0;
    String operator = null;

    // begin simulation
    hintTMstate.highlight(1);
    while (oracleTapeMarker.getPosition() < oracleTape.getLength()) {

      if (!countDown) {
        lang.nextStep();

        // when TM reads a cell, highlight this cell
        oracleTape.highlightCell(oracleTapeMarker.getPosition(), null, defaultTiming);

        switch (oracleTape.getData(oracleTapeMarker.getPosition())) {

          case "AND":
            operator = "AND";
            calculateValueOfAGate(operator);
            break;

          case "OR":
            operator = "OR";
            calculateValueOfAGate(operator);
            break;

          case "XOR":
            operator = "XOR";
            calculateValueOfAGate(operator);
            break;

          case "NOT":
            operator = "NOT";
            calculateValueOfAGate(operator);
            break;

          case "x":
            lang.nextStep();
            hintInput.setText("Der aktuelle Eingabewert wird auf dem Eingabeband gesucht.", null,
                defaultTiming);
            bitCount = 0;
            break;

          case "0":
          case "1":
            lang.nextStep();
            hintCounter.setText("Die Nummer der aktuellen Eingabe wird auf das Band geschrieben.",
                null, defaultTiming);
            lang.nextStep();
            counterTapeString.set(bitCount, oracleTape.getData(oracleTapeMarker.getPosition()));

            counterTapeMarker.hide();
            counterTapeMarker =
                lang.newArrayMarker(counterTape, bitCount, "counterTapeMarker", null, arrayMarker);

            counterTape.put(bitCount, oracleTape.getData(oracleTapeMarker.getPosition()), null,
                defaultTiming);

            bitCount++;

            if (bitCount == numberOfBits) {
              countDown = true;
            }

        }

      } else {

        hintCounter.setText("", null, defaultTiming);
        // move array marker back to pos 1
        while (inputTapeMarker.getPosition() > 0) {
          lang.nextStep();
          inputTapeMarker.hide();
          inputTapeMarker =
              lang.newArrayMarker(inputTape, inputTapeMarker.getPosition() - 1, "inputTapeMarker",
                  null, arrayMarker);
          hintInput.setText("Der Lesekopf wird ganz links gebracht.", null, defaultTiming);
        }

        hintInput.setText("", null, defaultTiming);
        hintOutput.setText("", null, defaultTiming);

        while (!Utils.isOne(counterTapeString)) {
          lang.nextStep();
          hintInput.setText("", null, defaultTiming);
          hintCounter.setText(
              "Die Nummer der gesuchten Eingabe wird in Binärdarstellung 1 abgezählt.", null,
              defaultTiming);
          // count down to 01 (binary)
          for (int i = counterTapeString.size() - 1; i >= 0; i--) {
            // find the first 1 from right and change it to 0
            if (counterTapeString.get(i).equals("1")) {
              lang.nextStep();
              counterTapeMarker.hide();
              counterTapeMarker =
                  lang.newArrayMarker(counterTape, i, "counterTapeMarker", null, arrayMarker);

              counterTapeString.set(i, "0");
              counterTape.put(i, "0", null, defaultTiming);

              // all the bits after that are changed to 1
              for (int j = i + 1; j < counterTapeString.size(); j++) {

                lang.nextStep();
                counterTapeMarker.hide();
                counterTapeMarker =
                    lang.newArrayMarker(counterTape, j, "counterTapeMarker", null, arrayMarker);

                counterTapeString.set(j, "1");
                counterTape.put(j, "1", null, defaultTiming);

              }
              break;
            }
          }
          lang.nextStep();
          hintCounter.setText("", null, defaultTiming);
          hintOutput.setText("", null, defaultTiming);
          hintInput
              .setText(
                  "Der Lesekopf auf dem Eingabeband wird nach rechts auf die nächste Zelle verschoben.",
                  null, defaultTiming);
          inputTapeMarker.increment(null, defaultTiming);
        }

        // input value is found
        lang.nextStep();
        hintInput.setText("Eingabewert gefunden!", null, defaultTiming);
        inputTape.highlightCell(inputTapeMarker.getPosition(), null, defaultTiming);
        lang.nextStep();
        hintInput.setText("", null, defaultTiming);
        // search value is found
        if (stateMemory != -1) { // the state memory is full
          // write the value in state memory to output tape
          hintTM.setText("Der Zustandsspeicher ist besetzt.", null, defaultTiming);
          lang.nextStep();
          hintTM.setText("Der aktuelle Wert im Zustand wird auf das 1. Arbeitsband gelegt.", null,
              defaultTiming);
          writeOnOutputTape(Integer.toString(stateMemory), false);
          hintTM.setText("", null, defaultTiming);
        }
        inputTape.unhighlightCell(inputTapeMarker.getPosition(), null, defaultTiming);
        hintInput.setText("", null, defaultTiming);
        // overwrite state memory with the new found value
        stateMemory = inputTape.getData(inputTapeMarker.getPosition());

        currentInputs[0] = stateMemory;

        changeStateMemory();
        // reset
        countDown = false;
      }

      if (!countDown) {
        oracleTape.unhighlightCell(oracleTapeMarker.getPosition(), null, defaultTiming);
        lang.nextStep();
        if (oracleTapeMarker.getPosition() + 1 <= oracle.size() - 1) {
          oracleTapeMarker.move(oracleTapeMarker.getPosition() + 1, null, defaultTiming);
        } else {
          break;
        }
      }
    }

    // closing words
    lang.nextStep("5. Schlusswort");
    TextProperties conclusion = new TextProperties();
    conclusion.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 15));
    conclusion.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    lang.newText(new Offset(0, 730, "theorem", "NW"), conclusion1, "conclusion1", null, conclusion);
    lang.newText(new Offset(0, 5, "conclusion1", "SW"), conclusion2, "conclusion2", null,
        conclusion);
  }

  /**
   * Calculate value of a gate given an operator.
   * 
   * @param operator given operator.
   */
  private void calculateValueOfAGate(String operator) {
    int output;
    int toogleHighlightTargetLine = 1;
    // determine line for highlighting corresponding to the given operator.
    switch (operator) {
      case "AND":
        toogleHighlightTargetLine = 2;
        break;
      case "OR":
        toogleHighlightTargetLine = 3;
        break;
      case "XOR":
        toogleHighlightTargetLine = 4;
        break;
      case "NOT":
        toogleHighlightTargetLine = 5;
        break;
    }

    // state memory is blank and input value must be read from output tape
    if (stateMemory == -1) {
      stateMemory = pop();
      hintOutput.setText("Der 1. Eingabewert wird aus dem 1. Arbeitsband gelesen.", null,
          defaultTiming);
      changeStateMemory();
      currentInputs[0] = stateMemory;
      lang.nextStep();
      cleanOutputTapeEntry();
      lang.nextStep();
      outputTape.unhighlightCell(outputTapeMarker.getPosition(), null, defaultTiming);
      hintOutput.setText("", null, defaultTiming);
      lang.nextStep();
      outputTapeMarker.hide();
      outputTapeMarker =
          lang.newArrayMarker(outputTape, outputTapeMarker.getPosition() - 1, "outputTapeMarker",
              null, arrayMarker);
      lang.nextStep();
    }

    // operators except NOT need 2 parameter
    if (!operator.equals("NOT")) {
      hintOutput
          .setText(
              "Der Wert, der unter dem Lesekopf auf dem 1. Arbeitsband liegt, wird mit dem Wert im Zustandsspeicher "
                  + operator + "-verknüpft.", null, defaultTiming);
      currentInputs[1] = pop();
      lang.nextStep();
      hintOutput.setText("", null, defaultTiming);
    } else { // NOT operator needs 1 parameter
      hintTM.setText("Der Wert, der im Zustandsspeicher liegt, wird negiert.", null, defaultTiming);
      lang.nextStep();
      hintTM.setText("", null, defaultTiming);
    }
    outputTape.unhighlightCell(outputTapeMarker.getPosition(), null, defaultTiming);
    lang.nextStep();
    showNewState(1);
    hintTMstate.toggleHighlight(1, toogleHighlightTargetLine);
    // evaluate current gate
    output = Utils.calculate(operator, currentInputs);
    // show the calculation
    String tmp = "";
    if (operator.equals("NOT")) {
      tmp = "NOT " + Integer.toString(currentInputs[0]) + " = " + Integer.toString(output);
    } else {
      tmp =
          Integer.toString(currentInputs[0]) + " " + operator + " "
              + Integer.toString(currentInputs[1]) + " = " + Integer.toString(output);
    }
    hintOutput.setText(tmp, null, defaultTiming);

    // for all operators except NOT
    // clean the entry, which lies under the read-write head on the 3rd tape
    if (!operator.equals("NOT")) {
      lang.nextStep();
      hintOutput.setText(
          "Der Wert unter dem Lesekopf wird nicht mehr benötigt und wird deswegen gelöscht.", null,
          defaultTiming);
      cleanOutputTapeEntry();
    }

    lang.nextStep();
    writeOnOutputTape(Integer.toString(output), true);

    // end calculation

    // Reset input array after calculating a gate.
    currentInputs[0] = -1;
    currentInputs[1] = -1;

    lang.nextStep();
    showNewState(0);
    hintTMstate.toggleHighlight(toogleHighlightTargetLine, 1);
    hintOutput.setText("", null, defaultTiming);
  }

  /**
   * Write a string on output tape.
   * 
   * @param s given string
   * @param gateValue true when the given string is a gate value, false when the given string is
   *        only an input value.
   */
  private void writeOnOutputTape(String s, boolean gateValue) {
    // when the current entry on output tape is not blank
    if (!outputArrayList.get(outputTapeMarker.getPosition()).equals("  ")) {
      lang.nextStep();
      // create a new blank entry on output tape
      outputArrayList.add(" ");
      outputTape =
          lang.newStringArray(new Offset(0, 115, "inputBand", "SW"),
              outputArrayList.toArray(new String[outputArrayList.size()]), "workingBand1", null,
              array);
      // move the marker onto the new created entry
      outputTapeMarker.hide();
      outputTapeMarker =
          lang.newArrayMarker(outputTape, outputArrayList.size() - 1, "outputTapeMarker", null,
              arrayMarker);
      if (gateValue) { // show text only when a gate value will be written on the current entry
        hintOutput.setText(
            "Der Wert des ausgewerteten Bausteins wird auf einer neuen Zelle geschrieben.", null,
            defaultTiming);
      }
    } else {
      lang.nextStep();
      if (gateValue) { // show text only when a gate value will be written on the current entry
        hintOutput.setText("Der Wert des ausgewerteten Bausteins wird auf der Zelle geschrieben.",
            null, defaultTiming);
      }
    }
    lang.nextStep();
    outputTape.put(outputTapeMarker.getPosition(), s, null, defaultTiming);
    outputArrayList.set(outputTapeMarker.getPosition(), s);
  }

  /**
   * Show new state.
   * 
   * @param q number of new state.
   */
  private void showNewState(int q) {
    char[] tmp = stateString.toCharArray();
    tmp[2] = Character.forDigit(q, 10);
    if (q == 0) {
      tmp[5] = '-';
      stateMemory = -1;
    }
    stateString = String.valueOf(tmp);
    // show new state
    lang.nextStep();
    stateText.setText(stateString, null, null);
  }

  /**
   * Get value on the rightmost entry of output tape.
   * 
   * @return found value
   */
  private int pop() {
    // move the marker to the first entry with a value
    while (outputTape.getData(outputTapeMarker.getPosition()).equals("  ")
        && outputTapeMarker.getPosition() > 0) {
      outputTapeMarker.decrement(null, defaultTiming);
    }
    outputTape.highlightCell(outputTapeMarker.getPosition(), null, defaultTiming);
    return Integer.valueOf(outputTape.getData(outputTapeMarker.getPosition()));
  }

  /**
   * Update and show new state.
   */
  private void changeStateMemory() {
    lang.nextStep();
    hintOutput.setText("", null, defaultTiming);
    hintTM.setText("Der auf dem Eingabeband gefundene Eingabewert wird im Zustand gespeichert.",
        null, defaultTiming);
    char[] tmp = stateString.toCharArray();
    tmp[5] = Character.forDigit(stateMemory, 10);
    stateString = String.valueOf(tmp);

    // show new state
    lang.nextStep();
    stateText.setText(stateString, null, null);
    hintTM.setText("", null, defaultTiming);
  }

  /**
   * Clear the value on current entry of output tape.
   */
  private void cleanOutputTapeEntry() {
    lang.nextStep();
    outputArrayList.set(outputTapeMarker.getPosition(), "  ");
    // if (outputTapeMarker.getPosition() != 0) {
    outputTape.put(outputTapeMarker.getPosition(), "  ", null, defaultTiming);
    // } else {
    // outputTape.hide();
    // outputTapeMarker.hide();
    // lang.nextStep();
    // outputTape =
    // lang.newStringArray(new Offset(0, 115, "inputBand", "SW"),
    // outputArrayList.toArray(new String[outputArrayList.size()]), "workingBand1", null,
    // arrayProps);
    // outputTapeMarker.show();
    // }
  }

  public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
    input = (int[]) primitives.get("input");
    // variables window
    Variables var = lang.newVariables();
    String key;
    for (int i = 0; i < input.length; i++) {
      key = "bit" + Integer.toString(i + 1);
      var.declare("int", key, Integer.toString(input[i]),
          Variable.getRoleString(VariableRoles.FIXED_VALUE));
    }

    array = (ArrayProperties) props.getPropertiesByName("array");
    arrayMarker = (ArrayMarkerProperties) props.getPropertiesByName("arrayMarker");
    inputBits = (CircleProperties) props.getPropertiesByName("inputBits");
    gate = (EllipseProperties) props.getPropertiesByName("gate");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    title = (TextProperties) props.getPropertiesByName("title");
    title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
    header1 = (TextProperties) props.getPropertiesByName("header1");
    header1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));
    header2 = (TextProperties) props.getPropertiesByName("header2");
    header2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 15));
    text = (TextProperties) props.getPropertiesByName("text");
    text.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
    hint = (TextProperties) props.getPropertiesByName("hint");
    hint.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));

    this.runSimulation(input);
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Platzbedarf der Simulationen von Schaltkreisen durch nichtuniforme Turingmaschinen";
  }

  public String getAlgorithmName() {
    return "Simulationen von Schaltkreisen durch nichtuniforme Turingmaschinen";
  }

  public String getAnimationAuthor() {
    return "Giang Nam Nguyen";
  }

  public String getDescription() {
    return "Schaltkreisfamilien S  = (S_n) bilden ein nichtuniformes Berechnungsmodell, weil wir uns nicht darum kümmern, wie wir bei Eingabelänge n den Schaltkreis S_n erhalten. Damit eine Turingmaschine eine Schaltkreisfamilie simulieren kann, muss sie ebenfalls eine von n = |x|, aber nicht vom Inhalt der aktuellen Eingabe x abhängige Information kostenlos erhalten. Eine nichtuniforme Turingmaschine ist eine Turingmaschine mit zwei Eingabebändern, auf denen nur gelesen werden darf. Das erste Eingabeband enthält die Eingabe x und das zweite Eingabeband die Hilfsinformation h(|x|), die für alle Eingaben gleicher Länge identisch ist. Häufig wird das zweite Eingabeband als Orakelband und die Hilfsinformation als Orakel bezeichnet. ([1], 14.3)"
        + "\n"
        + "\n"
        + "Wir benutzen für Schaltkreisfamilien S = (S_n) folgende Bezeichnungen:"
        + "\n"
        + "- s(n) für die Größe von S_n und s*(n) für max{s(n), n},"
        + "\n"
        + "- d(n) für die Tiefe von S_n und d*(n) für max{d(n), log n},"
        + "\n"
        + "- f_n für die von S_n berechnete Funktion,"
        + "\n"
        + "- A_f für das zu f = (f_n) gehörende Entscheidungsproblem."
        + "\n"
        + "\n"
        + "Bei dieser Simulation erhält die Turingmaschine eine Postorderdarstellung des Schaltkreises als Orakel. D. h., die Bausteine werden in einem ’postorder depth-first search’ (DFS)-Durchlauf ausgewertet. Der DFS-Durchlauf durch den Schaltkreisgraphen funktioniert nur, wenn jeder Knoten höchstens einen Nachfolger hat. Solche Schaltkreise heißen auch boolesche Formeln. Ein boolescher Schaltkreis kann in eine boolesche Formel der gleichen Tiefe tranformiert werden, indem jeder Knoten mit r Nachfolgern mit allen ihren Vorgängern durch r Kopien ersetzt werden."
        + "\n"
        + "In der Postorderdarstellung wird für jeden inneren Knoten zuerst der linke Vorgänger, dann der rechte Vorgänger durchlaufen. Anschließend wird die Operation des Knoten betrachtet. Zum Auswerten des Schaltkreises kommt jeder Bausteinwert nur ein Mal vor. Aus diesem Grund brauchen wir für Bausteine deren Nummer nicht."
        + "\n"
        + "\n"
        + "\n"
        + "Theorem 1 ([1], Theorem 14.3.2):"
        + "\n"
        + "Das zur Schaltkreisfamilie S = (S_n) gehörende Entscheidungsproblem A_f kann von einer nichtuniformen Turingmaschine auf Platz O(d*(n)) gelöst werden."
        + "\n"
        + "\n"
        + "Lemma 2 ([1], Beweis vom Theorem 14.3.2, Seite 220):"
        + "\n"
        + "Um eine Formel der Tiefe d(n) auszuwerten, braucht die nichtuniforme Turingmaschine d(n) Plätze auf dem 1. Arbeitsband."
        + "\n"
        + "\n"
        + "Beweis:"
        + "\n"
        + "- Lemma 2 zufolge braucht die Turingmaschine d(n) Plätze auf dem 1. Arbeitsband."
        + "\n"
        + "- Auf dem 2. Arbeitsband braucht die Turingmaschine O(log n) Plätze."
        + "\n"
        + "- Insgesamt werden O(d(n) + log n) = O(d*(n)) Plätze benötigt."
        + "\n"
        + "\n"
        + "Literatur:"
        + "\n"
        + "[1] Ingo Wegener. Komplexitätstheorie: Grenzen der Effizienz von Algorithmen. Springer, 2003.";
  }

  public String getCodeExample() {
    return "";
  }

  public String getFileExtension() {
    return "asu";
  }

  public Locale getContentLocale() {
    return Locale.GERMAN;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}
