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
 * TimeComplexity.java Giang Nam Nguyen, 2015 for the Animal project at TU Darmstadt.
 */
public class TimeComplexity implements Generator {
  // example 1
  // public static final int n = 3;
  // public static final int s_n = 3;

  // example 2
  /**
   * Length of input bit sequence.
   */
  // public static final int n = 3;
  /**
   * Number of gates in circuit.
   */
  // public static final int s_n = 2;

  // Ex.3
  public static final int n = 4;
  public static final int s_n = 3;

  // Ex.4
  // public static final int n = 4;
  // public static final int s_n = 4;

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

  // private static int[] input = {1, 0, 1, 1};
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
  private SourceCodeProperties pseudoCode;

  // Markers.
  private ArrayMarker inputTapeMarker;
  private ArrayMarker oracleTapeMarker;
  private ArrayMarker outputTapeMarker;
  private ArrayMarker counterTapeMarker;

  // TM's state
  private Text stateText;
  private String stateString;
  // hint TM tapes
  private Text hintInput;
  private Text hintOracle;
  private Text hintOutput;
  private Text hintCounter;
  // to overcome a bug with Const.topBorderHorizontal
  public static final int topBorderHorizontal = 300;
  // closing words
  public static final String conclusion1 =
      "Die Animation zeigt, dass ein Schaltkreis durch eine nichtuniforme Turingmaschine simuliert werden kann.";
  public static final String conclusion2 =
      "Der Zeitbedarf der Simulation hängt quadratisch von der Schaltkreisgröße ab.";

  /**
   * Constructor.
   */
  public TimeComplexity() {

  }

  /**
   * Initialize properties.
   */
  public void init() {
    lang =
        new AnimalScript(
            "Zeitbedarf der Simulation eines Schaltkreises durch eine nichtuniforme Turingmaschine",
            "Giang Nam Nguyen", 1500, 800);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    // text properties
    // title = new TextProperties();
    // title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
    //
    // header1 = new TextProperties();
    // header1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));
    //
    // header2 = new TextProperties();
    // header2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 15));
    //
    // hint = new TextProperties();
    // hint.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    // hint.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    //
    // // array properties
    // array = new ArrayProperties();
    // array.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    // array.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // array.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // array.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    //
    // gate = new EllipseProperties();
    // gate.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // gate.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    //
    // inputBits = new CircleProperties();
    // inputBits.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // inputBits.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    //
    // arrayMarker = new ArrayMarkerProperties();
    // arrayMarker.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // default timing
    defaultTiming = new TicksTiming(15);

    // pseudoCode properties
    // pseudoCode = new SourceCodeProperties();
    // pseudoCode.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // pseudoCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN,
    // 12));
    // pseudoCode.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // pseudoCode.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

  }

  /**
   * Show theorem and proof.
   */
  private void showProof() {
    lang.nextStep("1. Theorem und Beweisskizze");
    lang.newText(new Offset(-3, 40, "title", "NW"), "Theorem:", "theorem", null, header1);
    lang.newText(
        new Offset(17, -22, "theorem", "SE"),
        "Das zur Schaltkreisfamilie S = (S_n) gehörende Entscheidungsproblem A_f kann von einer nichtuniformen Turingmaschine",
        "", null, text);
    lang.newText(new Offset(10, 5, "theorem", "SE"),
        "mit zwei Arbeitsbändern in Zeit O(s*(n)^2) auf Platz O(s*(n)) gelöst werden.", "", null,
        text);
    lang.nextStep("Theorem");
    lang.newText(new Offset(0, 45, "theorem", "NW"), "Beweisskizze:", "proof", null, header1);
    lang.nextStep();
    lang.newText(
        new Offset(0, 10, "proof", "SW"),
        "- Für einen Schaltkreis der Größe s(n) braucht die TM s(n) Plätze auf dem 1. Arbeitsband.",
        "proofLine1", null, text);
    lang.nextStep();
    lang.newText(
        new Offset(0, 10, "proofLine1", "SW"),
        "- Um den Wert eines Eingang zu finden, braucht die TM O(n) bzw. O(s(n)) Schritte, wenn der Wert auf dem Eingabeband",
        "proofLine2", null, text);
    lang.newText(new Offset(0, 10, "proofLine2", "SW"), "bzw. auf dem 1. Arbeitsband liegt.",
        "proofLine3", null, text);
    lang.nextStep();
    lang.newText(new Offset(0, 10, "proofLine3", "SW"),
        "- D.h., um s(n) Bausteine auszuwerten, bedarf die TM O(s*(n)^2) Schritte.", "", null, text);
    lang.nextStep("Beweisskizze");
  }

  /**
   * Show pseudo code.
   */
  private void showPseudoCode() {
    // title
    lang.newText(new Coordinates(Const.leftBorderVerticalPseudoCode, topBorderHorizontal - 50),
        "Pseudocode:", "titlePseudoCode", null, header1);

    // now, create the source code entity
    sc =
        lang.newSourceCode(new Offset(0, -5, "titlePseudoCode", "SW"), "sourceCode", null,
            pseudoCode);

    // add a code line
    // parameters: code itself; name (can be null); indentation level; display options
    sc.addCodeLine("oracle = traverse the circuit in level-order", null, 0, null);
    sc.addCodeLine("while (!endOf(oracle)) {", null, 0, null);
    sc.addCodeLine("switch (current char) {", null, 1, null);

    sc.addCodeLine("case 'AND' (or 'OR', 'XOR', 'NOT'): ", null, 2, null);
    sc.addCodeLine("change state respectively", null, 3, null);
    sc.addCodeLine("break", null, 3, null);

    sc.addCodeLine("case 'x' (or 'G'):", null, 2, null);
    sc.addCodeLine("search for input value later on 1st tape (for 'x') or 3rd tape (for 'G')",
        null, 3, null);
    sc.addCodeLine("break", null, 3, null);
    sc.addCodeLine("case '0' (or '1'):", null, 2, null);
    sc.addCodeLine("copy this character onto 4th tape", null, 3, null);
    sc.addCodeLine("if (all bits which represent this input number are read) {", null, 3, null);
    sc.addCodeLine("move the read/write head of the 1st (or 3rd) tape to leftmost position", null,
        4, null);
    sc.addCodeLine("while (4th tape's value != 1) {", null, 4, null);
    sc.addCodeLine("value--", null, 5, null);
    sc.addCodeLine("move the read/write head of the 1st (or 3rd) tape one position to the right",
        null, 5, null);
    sc.addCodeLine("}", null, 4, null);
    sc.addCodeLine("read and remember the found value in the state", null, 4, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("break", null, 3, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("if (all input values needed for the current operation are found) {", null, 1,
        null);
    sc.addCodeLine("evaluate the current gate", null, 2, null);
    sc.addCodeLine("write down the result on 3rd tape", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
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
    // circuit[3] = new Gate(0, circuit[2], circuit[1], "AND");
    // circuit[4] = new Gate(1, circuit[3], circuit[0], "OR");
    // root = circuit[4];

    // ex.3
    circuit[4] = new Gate(0, circuit[1], circuit[0], "AND");
    circuit[5] = new Gate(1, circuit[2], circuit[4], "OR");
    circuit[6] = new Gate(2, circuit[3], circuit[5], "OR");
    root = circuit[6];

    // ex.4
    // circuit[4] = new Gate(0, circuit[1], circuit[0], "OR");
    // circuit[5] = new Gate(1, null, circuit[2], "NOT");
    // circuit[6] = new Gate(2, circuit[3], circuit[5], "AND");
    // circuit[7] = new Gate(3, circuit[6], circuit[4], "AND");
    // root = circuit[7];

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

    Random ran = new Random();
    int x = ran.nextInt(2);
    MultipleChoiceQuestionModel q;

    // either show question or not
    if (x == 1) {
      lang.nextStep();
      q = new MultipleChoiceQuestionModel("q1");
      q.setPrompt("Wie hoch ist die Schaltkreisgröße s(n) in dem Beispiel?");
      q.addAnswer("2", 0,
          "Falsch. Es gibt 3 Bausteine in diesem Beispiel. Die richtige Antwort ist s(n) = 3.");
      q.addAnswer("3", 1, "Richtig!");
      q.addAnswer("4", 0,
          "Falsch. Es gibt 3 Bausteine in diesem Beispiel. Die richtige Antwort ist s(n) = 3.");
      lang.addMCQuestion(q);
      lang.nextStep();
    }

    // either show question or not
    x = ran.nextInt(2);
    if (x == 1) {
      q = new MultipleChoiceQuestionModel("q2");
      q.setPrompt("Wie hoch ist die Schaltkreistiefe d(n) in dem Beispiel?");
      q.addAnswer("2", 0,
          "Falsch. Der längste Weg ist (x1, G1, G2, G3). Die richtige Antwort ist d(n) = 3.");
      q.addAnswer("3", 1, "Richtig!");
      q.addAnswer("4", 0,
          "Falsch. Der längste Weg ist (x1, G1, G2, G3). Die richtige Antwort ist d(n) = 3.");
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
          "Falsch. Die Eingabelänge ist 4, und die Schaltkreisgröße ist 3. Die Bausteine bzw. die Eingaben werden beginnend bei 1 aufsteigend durchnummeriert. Deshalb wird 3 Bits auf dem 2. Arbeitsband benötigt.");
      q.addAnswer("3", 1, "Richtig!");
      q.addAnswer(
          "4",
          0,
          "Falsch. Die Eingabelänge ist 4, und die Schaltkreisgröße ist 3. Die Bausteine bzw. die Eingaben werden beginnend bei 1 aufsteigend durchnummeriert. Deshalb wird 3 Bits auf dem 2. Arbeitsband benötigt.");
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


    stateString = "[q0, -, -]";
    stateText = lang.newText(new Offset(80, -15, "t5", "E"), stateString, "", null);

    hintTMstate = lang.newSourceCode(new Offset(0, 5, "t5", "SW"), "sourceCode", null, pseudoCode);

    hintTMstate.addCodeLine("Hinweis:", null, 0, null);
    hintTMstate.addCodeLine("[q0, -, -]: Anfangszustand und Endzustand,", null, 0, null);
    hintTMstate.addCodeLine("[q1, x, y]: Zustand, indem AND-Operator gemerkt,", null, 0, null);
    hintTMstate.addCodeLine("[q2, x, y]: Zustand, indem OR-Operator gemerkt,", null, 0, null);
    hintTMstate.addCodeLine("[q3, x, y]: Zustand, indem XOR-Operator gemerkt,", null, 0, null);
    hintTMstate.addCodeLine("[q4, x, y]: Zustand, indem NOT-Operator gemerkt,", null, 0, null);
    hintTMstate.addCodeLine("wobei x,y in {1, 0, -}.", null, 0, null);

    // borders
    lang.newRect(new Offset(-10, -10, "t5", "NW"), new Offset(275, 140, "t5", "SE"), "", null);

    // input band
    lang.newText(new Offset(0, 200, "t5", "SW"), "Eingabeband", "t1", null, header2);
    inputTape = lang.newIntArray(new Offset(50, -8, "t1", "E"), input, "inputBand", null, array);
    hintInput =
        lang.newText(new Offset(n * Const.shiftLeftChar, -7, "inputBand", "E"), "", "hintInput",
            null, hint);

    // oracle band
    lang.newText(new Offset(0, 53, "t1", "SW"), "Orakelband", "t2", null, header2);

    int numberOfNotGates = 0;
    for (int i = n; i < circuit.length; i++)
      if (((Gate) circuit[i]).getValue().equals("NOT")) {
        numberOfNotGates++;
      }

    int oracleLength =
        (3 + 2 * numberOfBits) * (s_n - numberOfNotGates) + (2 + numberOfBits) * numberOfNotGates;
    String[] stringArray = new String[oracleLength];
    for (int i = 0; i < oracleLength; i++)
      stringArray[i] = "  ";

    oracleTape =
        lang.newStringArray(new Offset(0, 48, "inputBand", "SW"), stringArray, "oracleBand", null,
            array);
    hintOracle = lang.newText(new Offset(0, 5, "t2", "SW"), "", "hintOracle", null, hint);

    // working band 1
    lang.newText(new Offset(0, 53, "t2", "SW"), "1. Arbeitsband", "t3", null, header2);


    // display working band 1
    ArrayList<String> tmp4 = new ArrayList<String>();
    // number of boxes on working band 1 = s_n
    for (int i = 0; i < s_n; i++) {
      tmp4.add("  ");
    }
    outputTape =
        lang.newStringArray(new Offset(0, 48, "oracleBand", "SW"),
            tmp4.toArray(new String[tmp4.size()]), "workingBand1", null, array);
    hintOutput =
        lang.newText(new Offset(s_n * Const.shiftLeftChar, -5, "workingBand1", "E"), "",
            "hintOuput", null, hint);

    // working band 2
    lang.newText(new Offset(0, 53, "t3", "SW"), "2. Arbeitsband", "t4", null, header2);

    // display working band 2
    // number of boxes on working band 2 is = log(max{n,s_n})

    // init a blank counter tape
    counterTapeString = new ArrayList<String>();
    for (int i = 0; i < numberOfBits; i++)
      counterTapeString.add("  ");

    counterTape =
        lang.newStringArray(new Offset(0, 48, "workingBand1", "SW"),
            counterTapeString.toArray(new String[counterTapeString.size()]), "workingBand2", null,
            array);
    hintCounter =
        lang.newText(new Offset(counterTapeString.size() * Const.shiftLeftChar, -5, "workingBand2",
            "E"), "", "hintCounter", null, hint);
    lang.nextStep("4. Nichtuniforme Turingmaschine");
    // create OracleTape
    Node leftChild, rightChild;
    int count = 0;
    
    sc.highlight(0);
    hintOracle
        .setText(
            "Kodierung jedes Gatters = Tripel aus (Operator, 1. Eingabe, 2. Eingabe), Nummern in Binärdarstellung",
            null, defaultTiming);
    lang.nextStep("Orakel = Durchlauf des Schaltkreises in Level-Order");
    
    for (int i = 1; i <= longestPath; i++) {
      ArrayList<Integer> gateNumbers = levelGate.get(i);
      Collections.sort(gateNumbers);
      // traverse circuit graph in level order
      for (Iterator iterator = gateNumbers.iterator(); iterator.hasNext();) {
        Integer number = (Integer) iterator.next();

        Node currentNode = circuit[number + n];
        lang.nextStep();
        // highlight the current node
        e[number] =
            lang.newEllipse(e[number].getCenter(), e[number].getRadius(), e[number].getName(),
                null, gate);
        lang.newText(new Coordinates(((Coordinates) e[number].getCenter()).getX() - 13,
            ((Coordinates) e[number].getCenter()).getY() - 5), ((Gate) currentNode).getValue(), "",
            null);

        oracleTape.put(count++, ((Gate) currentNode).getValue(), null, defaultTiming);

        lang.nextStep();
        // unhighlight current node
        e[number].hide();

        rightChild = currentNode.getRightChild();
        // highlight right child
        showOrHideAChildNode(rightChild, true, count);
        count++;
        String tmp = Utils.decToBin(rightChild.getNumber() + 1, numberOfBits);
        for (int j = 0; j < tmp.length(); j++) {
          oracleTape.put(count++, Character.toString(tmp.charAt(j)), null, defaultTiming);
          lang.nextStep();
        }
        // unhighlight left child
        showOrHideAChildNode(rightChild, false, count);

        if (!((Gate) currentNode).getValue().equals("NOT")) {
          leftChild = currentNode.getLeftChild();
          // highlight left child
          showOrHideAChildNode(leftChild, true, count);
          count++;
          tmp = Utils.decToBin(leftChild.getNumber() + 1, numberOfBits);
          for (int j = 0; j < tmp.length(); j++) {
            oracleTape.put(count++, Character.toString(tmp.charAt(j)), null, defaultTiming);
            lang.nextStep();
          }
          // unhighlight left child
          showOrHideAChildNode(leftChild, false, count);
        }
      }
    }


  }

  /**
   * Highlight or unhighlight a node on circuit graph.
   * 
   * @param child a Node
   * @param showHide true when highlighting, false when unhighlighting
   */
  private void showOrHideAChildNode(Node child, boolean showHide, int count) {
    if (showHide) {
      if (child instanceof Input) {
        x[child.getNumber()] =
            lang.newCircle(x[child.getNumber()].getCenter(), x[child.getNumber()].getRadius(),
                x[child.getNumber()].getName(), null, inputBits);
        lang.nextStep();
        oracleTape.put(count, "x", null, defaultTiming);
      } else {
        e[child.getNumber()] =
            lang.newEllipse(e[child.getNumber()].getCenter(), e[child.getNumber()].getRadius(),
                e[child.getNumber()].getName(), null, gate);

        lang.newText(new Coordinates(((Coordinates) e[child.getNumber()].getCenter()).getX() - 13,
            ((Coordinates) e[child.getNumber()].getCenter()).getY() - 5),
            ((Gate) child).getValue(), "", null);
        lang.nextStep();
        oracleTape.put(count, "G", null, defaultTiming);
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
   * Add a gate number to corressponding level in levelGate.
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
    // show title of algo
    lang.newRect(new Coordinates(5, 5), new Coordinates(985, 35), "", null);
    lang.newText(new Coordinates(10, 15),
        "Zeitbedarf der Simulationen von Schaltkreisen durch nichtuniforme Turingmaschinen",
        "title", null, title);

    showProof();
    showCircuit(array);
    lang.nextStep("2. Boolescher Schaltkreis");
    showPseudoCode();
    lang.nextStep("3. Pseudo-Code");
    showTM();
    
    // init array marker
    inputTapeMarker = lang.newArrayMarker(inputTape, 0, "inputTapeMarker", null, arrayMarker);

    oracleTapeMarker = lang.newArrayMarker(oracleTape, 0, "oracleMarker", null, arrayMarker);

    outputTapeMarker = lang.newArrayMarker(outputTape, 0, "outputTapeMarker", null, arrayMarker);

    counterTapeMarker = lang.newArrayMarker(counterTape, 0, "counterTapeMarker", null, arrayMarker);


    boolean countDown = false;
    int bitCount = 0;
    // inputSource = 1 when TM searchs a value on inputband
    // = 2 when the search value is on first working band
    int inputSource = 0;
    ArrayMarker countDownMarker = null;
    String operator = null;
    // count how many inputs are known
    int numberOfInputs = 0;
    int[] currentInputs = new int[2];
    ArrayList<Integer> outputValues = new ArrayList<Integer>();


    // begin simulation
    sc.toggleHighlight(0, 1);
    hintTMstate.highlight(1);
    hintOracle.hide();
    lang.nextStep("5. Simulation");

    while (oracleTapeMarker.getPosition() < oracleTape.getLength()) {
      if (!countDown) {
        lang.nextStep();
        sc.toggleHighlight(1, 2);

        // when TM reads a cell, highlight this cell
        oracleTape.highlightCell(oracleTapeMarker.getPosition(), null, defaultTiming);

        switch (oracleTape.getData(oracleTapeMarker.getPosition())) {

          case "AND":
            lang.nextStep();
            sc.toggleHighlight(2, 3);
            operator = "AND";
            numberOfInputs = 0;
            lang.nextStep();
            sc.toggleHighlight(3, 4);
            stateString = stateString.replaceAll("q\\d", "q1");
            hintTMstate.toggleHighlight(1, 2);
            stateText.setText(stateString, null, null);
            lang.nextStep();
            sc.toggleHighlight(4, 5);
            lang.nextStep();
            sc.toggleHighlight(5, 20);
            lang.nextStep();
            sc.toggleHighlight(20, 21);
            lang.nextStep();
            sc.toggleHighlight(21, 24);
            lang.nextStep();
            sc.toggleHighlight(24, 25);
            break;

          case "OR":
            lang.nextStep();
            sc.toggleHighlight(2, 3);
            operator = "OR";
            numberOfInputs = 0;
            lang.nextStep();
            sc.toggleHighlight(3, 4);
            stateString = stateString.replaceAll("q\\d", "q2");
            hintTMstate.toggleHighlight(1, 3);
            stateText.setText(stateString, null, null);
            lang.nextStep();
            sc.toggleHighlight(4, 5);
            lang.nextStep();
            lang.nextStep();
            sc.toggleHighlight(5, 20);
            lang.nextStep();
            sc.toggleHighlight(20, 21);
            lang.nextStep();
            sc.toggleHighlight(21, 24);
            lang.nextStep();
            sc.toggleHighlight(24, 25);
            break;

          case "XOR":
            lang.nextStep();
            sc.toggleHighlight(2, 3);
            operator = "XOR";
            numberOfInputs = 0;
            lang.nextStep();
            sc.toggleHighlight(3, 4);
            stateString = stateString.replaceAll("q\\d", "q3");
            hintTMstate.toggleHighlight(1, 4);
            stateText.setText(stateString, null, null);
            lang.nextStep();
            sc.toggleHighlight(4, 5);
            lang.nextStep();
            lang.nextStep();
            sc.toggleHighlight(5, 20);
            lang.nextStep();
            sc.toggleHighlight(20, 21);
            lang.nextStep();
            sc.toggleHighlight(21, 24);
            lang.nextStep();
            sc.toggleHighlight(24, 25);
            break;

          case "NOT":
            lang.nextStep();
            sc.toggleHighlight(2, 3);
            operator = "NOT";
            numberOfInputs = 0;
            lang.nextStep();
            sc.toggleHighlight(3, 4);
            stateString = stateString.replaceAll("q\\d", "q4");
            hintTMstate.toggleHighlight(1, 5);
            stateText.setText(stateString, null, null);
            lang.nextStep();
            sc.toggleHighlight(4, 5);
            lang.nextStep();
            sc.toggleHighlight(5, 20);
            lang.nextStep();
            sc.toggleHighlight(20, 21);
            lang.nextStep();
            sc.toggleHighlight(21, 24);
            lang.nextStep();
            sc.toggleHighlight(24, 25);
            break;

          case "x":
            lang.nextStep();
            sc.toggleHighlight(2, 6);
            inputSource = 1;
            lang.nextStep();
            hintInput.setText("Der aktuelle Eingabewert wird auf dem Band gesucht.", null,
                defaultTiming);
            sc.toggleHighlight(6, 7);
            bitCount = 0;
            lang.nextStep();
            sc.toggleHighlight(7, 8);
            lang.nextStep();
            sc.toggleHighlight(8, 20);
            lang.nextStep();
            sc.toggleHighlight(20, 21);
            lang.nextStep();
            sc.toggleHighlight(21, 24);
            lang.nextStep();
            sc.toggleHighlight(24, 25);
            break;

          case "G":
            lang.nextStep();
            sc.toggleHighlight(2, 6);
            inputSource = 2;
            lang.nextStep();
            hintOutput.setText("Der aktuelle Eingabewert wird auf dem Band gesucht.", null,
                defaultTiming);
            sc.toggleHighlight(6, 7);
            bitCount = 0;
            lang.nextStep();
            sc.toggleHighlight(7, 8);
            lang.nextStep();
            sc.toggleHighlight(8, 20);
            lang.nextStep();
            sc.toggleHighlight(20, 21);
            lang.nextStep();
            sc.toggleHighlight(21, 24);
            lang.nextStep();
            sc.toggleHighlight(24, 25);
            break;

          case "0":
          case "1":
            lang.nextStep();
            sc.toggleHighlight(2, 9);
            hintCounter.setText("Die Nummer der aktuellen Eingabe wird auf das Band geschrieben.",
                null, defaultTiming);
            lang.nextStep();
            sc.toggleHighlight(9, 10);
            lang.nextStep();
            counterTapeString.set(bitCount, oracleTape.getData(oracleTapeMarker.getPosition()));

            counterTapeMarker.hide();
            counterTapeMarker =
                lang.newArrayMarker(counterTape, bitCount, "counterTapeMarker", null, arrayMarker);

            counterTape.put(bitCount, oracleTape.getData(oracleTapeMarker.getPosition()), null,
                defaultTiming);

            bitCount++;
            lang.nextStep();
            sc.toggleHighlight(10, 11);
            if (bitCount == numberOfBits) {
              countDown = true;
            } else {
              lang.nextStep();
              sc.toggleHighlight(11, 18);
              lang.nextStep();
              sc.toggleHighlight(18, 19);
              lang.nextStep();
              sc.toggleHighlight(19, 20);
              lang.nextStep();
              sc.toggleHighlight(20, 21);
              lang.nextStep();
              sc.toggleHighlight(21, 24);
              lang.nextStep();
              sc.toggleHighlight(24, 25);
            }
        }

      } else {

        lang.nextStep();
        sc.toggleHighlight(11, 12);

        // move array marker back to pos 1
        if (inputSource == 1) {
          while (inputTapeMarker.getPosition() > 0) {
            lang.nextStep();
            inputTapeMarker.hide();
            inputTapeMarker =
                lang.newArrayMarker(inputTape, inputTapeMarker.getPosition() - 1,
                    "inputTapeMarker", null, arrayMarker);
            hintInput.setText("Der Lesekopf wird ganz links gebracht.", null, defaultTiming);
          }

        } else if (inputSource == 2) {
          while (outputTapeMarker.getPosition() > 0) {
            lang.nextStep();
            outputTapeMarker.hide();
            outputTapeMarker =
                lang.newArrayMarker(outputTape, outputTapeMarker.getPosition() - 1,
                    "outputTapeMarker", null, arrayMarker);
            hintOutput.setText("Der Lesekopf wird ganz links gebracht.", null, defaultTiming);
          }
        }

        lang.nextStep();
        sc.toggleHighlight(12, 13);
        hintInput.setText("", null, defaultTiming);
        hintOutput.setText("", null, defaultTiming);

        while (!Utils.isOne(counterTapeString)) {
          lang.nextStep();
          sc.toggleHighlight(13, 14);

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

              // all the bits which lie behind this position are changed to 1
              for (int j = i + 1; j < counterTapeString.size(); j++) {

                lang.nextStep();
                counterTapeMarker.hide();
                counterTapeMarker =
                    lang.newArrayMarker(counterTape, j, "counterTapeMarker", null, arrayMarker);

                counterTapeString.set(j, "1");
                counterTape.put(j, "1", null, defaultTiming);

              }
              lang.nextStep();
              sc.toggleHighlight(14, 15);
              break;
            }
          }

          if (inputSource == 1) {
            hintCounter
                .setText(
                    "Der Lesekopf auf dem Eingabeband wird nach rechts auf die nächste Zelle verschoben.",
                    null, defaultTiming);
            inputTapeMarker.increment(null, defaultTiming);
          } else if (inputSource == 2) {
            hintCounter
                .setText(
                    "Der Lesekopf auf dem 1. Arbeitsband wird nach rechts auf die nächste Zelle verschoben.",
                    null, defaultTiming);
            outputTapeMarker.increment(null, defaultTiming);
          }

          lang.nextStep();
          sc.unhighlight(15);
          sc.highlight(13);
        }

        // input value is found
        lang.nextStep();
        sc.unhighlight(13);
        sc.toggleHighlight(15, 16);
        lang.nextStep();
        sc.toggleHighlight(16, 17);
        hintCounter.setText("", null, defaultTiming);
        if (inputSource == 1) { // on input tape
          inputTape.highlightCell(inputTapeMarker.getPosition(), null, defaultTiming);
          hintInput.setText("Eingabewert gefunden!", null, defaultTiming);
          stateString =
              stateString.replaceFirst("-",
                  Integer.toString(inputTape.getData(inputTapeMarker.getPosition())));

          stateText.setText(stateString, null, defaultTiming);
          // set t7
          currentInputs[numberOfInputs] = inputTape.getData(inputTapeMarker.getPosition());
          numberOfInputs++;

        } else { // on output tape

          outputTape.highlightCell(outputTapeMarker.getPosition(), null, defaultTiming);
          hintOutput.setText("Eingabewert gefunden!", null, defaultTiming);
          stateString =
              stateString.replaceFirst("-", outputTape.getData(outputTapeMarker.getPosition()));
          stateText.setText(stateString, null, defaultTiming);

          // set t7;
          currentInputs[numberOfInputs] =
              Integer.valueOf(outputTape.getData(outputTapeMarker.getPosition()));
          numberOfInputs++;
        }

        lang.nextStep();
        // search value is found
        if (inputSource == 1) {
          inputTape.unhighlightCell(inputTapeMarker.getPosition(), null, defaultTiming);
          hintInput.setText("", null, defaultTiming);
        } else {
          outputTape.unhighlightCell(outputTapeMarker.getPosition(), null, defaultTiming);
          hintOutput.setText("", null, defaultTiming);
        }

        // reset
        countDown = false;

        lang.nextStep();
        sc.toggleHighlight(17, 18);
        lang.nextStep();
        sc.toggleHighlight(18, 19);
        lang.nextStep();
        sc.toggleHighlight(19, 20);
        lang.nextStep();
        sc.toggleHighlight(20, 21);

        // calculate output value of current gate
        if (numberOfInputs == Utils.numberOfInputsRequired(operator)) {
          lang.nextStep();
          sc.toggleHighlight(21, 22);
          outputValues.add(Utils.calculate(operator, currentInputs));
          // show calculation
          String tmp = "";
          switch (numberOfInputs) {
            case 1: // unary operation
              tmp =
                  operator + " " + Integer.toString(currentInputs[0]) + " = "
                      + Integer.toString(Utils.calculate(operator, currentInputs));
              break;
            case 2: // binary operation
              tmp =
                  Integer.toString(currentInputs[0]) + " " + operator + " "
                      + Integer.toString(currentInputs[1]) + " = "
                      + Integer.toString(Utils.calculate(operator, currentInputs));
              break;
          }
          hintOutput.setText(tmp, null, defaultTiming);
          lang.nextStep();
          sc.toggleHighlight(22, 23);
          outputTapeMarker.move(outputValues.size() - 1, null, defaultTiming);
          lang.nextStep();
          hintOutput.setText("Der Wert des aktuellen Bausteins wird auf das Band geschrieben.",
              null, defaultTiming);
          // write result on output tape
          outputTape.put(outputValues.size() - 1,
              Integer.toString(outputValues.get(outputValues.size() - 1)), null, defaultTiming);
          // reset text
          lang.nextStep();
          sc.toggleHighlight(23, 24);
          // unhighlight texts in state box
          for (int i = 1; i < 6; i++)
            hintTMstate.unhighlight(i);
          stateString = "[q0, -, -]";
          hintTMstate.highlight(1);
          hintOutput.setText("", null, defaultTiming);
          stateText.setText(stateString, null, defaultTiming);
        } else {
          lang.nextStep();
          sc.toggleHighlight(21, 24);
        }
        lang.nextStep();
        sc.toggleHighlight(24, 25);
      }

      // move the read/write head one position to the right
      if (!countDown) {
        oracleTape.unhighlightCell(oracleTapeMarker.getPosition(), null, defaultTiming);
        lang.nextStep();
        sc.toggleHighlight(25, 1);
        if (oracleTapeMarker.getPosition() + 1 <= oracleTape.getLength() - 1) {
          oracleTapeMarker.hide();
          oracleTapeMarker =
              lang.newArrayMarker(oracleTape, oracleTapeMarker.getPosition() + 1, "oracleMarker",
                  null, arrayMarker);
        } else {
          lang.nextStep();
          sc.toggleHighlight(1, 25);
          break;
        }
      }
    }

    // end simulation
    lang.nextStep();
    sc.unhighlight(25);

    // closing words
    lang.nextStep();
    TextProperties conclusion = new TextProperties();
    conclusion.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 15));
    conclusion.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    lang.newText(new Offset(0, 730, "theorem", "NW"), conclusion1, "conclusion1", null, conclusion);
    lang.newText(new Offset(0, 5, "conclusion1", "SW"), conclusion2, "conclusion2", null,
        conclusion);
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
    pseudoCode = (SourceCodeProperties) props.getPropertiesByName("pseudoCode");
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
    return "Zeitbedarf der Simulationen von Schaltkreisen durch nichtuniforme Turingmaschinen";
  }

  public String getAlgorithmName() {
    return "Simulationen von Schaltkreisen durch nichtuniforme Turingmaschinen";
  }

  public String getAnimationAuthor() {
    return "Giang Nam Nguyen";
  }

  public String getDescription() {
    return "Schaltkreisfamilien S  = (S_n) bilden ein nichtuniformes Berechnungsmodell, weil wir uns nicht darum kümmern, wie wir bei Eingabelänge n den Schaltkreis S_n erhalten. Damit eine Turingmaschine eine Schaltkreisfamilie simulieren kann, muss sie ebenfalls eine von n = |x|, aber nicht vom Inhalt der aktuellen Eingabe x abhängige Information kostenlos erhalten. Eine nichtuniforme Turingmaschine ist eine Turingmaschine mit zwei Eingabebändern, auf denen nur gelesen werden darf. Das erste Eingabeband enthält die Eingabe x und das zweite Eingabeband die Hilfsinformation h(|x|), die für alle Eingaben gleicher Länge identisch ist. Häufig wird das zweite Eingabeband als Orakelband und die Hilfsinformation als Orakel bezeichnet. ([1], Kapitel 14, 14.3)"
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
        + "Theorem 1 ([1], Theorem 14.3.1):"
        + "\n"
        + "Das zur Schaltkreisfamilie S = (S_n) gehörende Entscheidungsproblem A_f kann von einer nichtuniformen Turingmaschine mit zwei Arbeitsbändern in Zeit O(s*(n)^2) auf Platz O(s*(n)) gelöst werden."
        + "\n"
        + "\n"
        + "Beweis: "
        + "\n"
        + "- Das Orakel ist die Traverse des Schaltkreises in Level-Order."
        + "\n"
        + "- Das erste Arbeitsband ist benutzt, um die Werte der bereits berechneten Gatter zu speichern."
        + "\n"
        + "- Das zweite Arbeitsband dient als ein Zähler. Die Nummer des gesuchten Werts (von einem Eingabebit bzw. einem Gatter) in Binärdarstellung wird auf dem Band geschrieben und bis zum 1 abgezählt. Während von der Zahl auf dem zweiten Arbeitsband 1 subtrahiert wird, bis der Wert 1 erreicht wird, wandert der Kopf auf dem ersten Eingabeband bzw. dem ersten Arbeitsband jeweils eine Position nach rechts. Am Ende dieses Vorgangs liest der Kopf auf dem ersten Eingabeband bzw. dem Arbeitsband die gesuchte Information."
        + "\n"
        + "- Um einen Eingabewert zu finden, braucht die nichtuniforme TM entweder auf dem ersten Eingabeband O(n) oder auf dem ersten Arbeitsband O(s(n)) Zeitschritte."
        + "\n"
        + "- Ein Gatter hat maximal zwei Eingänge. Deshalb für einen Gatter wird O(s*(n)) Zeitschritte benötigt."
        + "\n"
        + "- Die Schaltkreisgröße ist s(n). Deswegen ist der Zeitbedarf der Simulation O(s(n)s*(n)) = O(s*(n)^2). "
        + "\n"
        + "\n"
        + "Literatur:"
        + "\n"
        + "[1] Ingo Wegener. Komplexitätstheorie: Grenzen der Effizienz von Algorithmen. Springer, 2003."
        + "\n";
  }

  public String getCodeExample() {
    return "oracle = traverse the circuit in level-order" + "\n" + "while (!endOf(oracle)) {"
        + "\n" + " switch (current char) {" + "\n" + "   case 'AND' (or 'OR', 'XOR', 'NOT'): "
        + "\n" + "     change state respectively   " + "\n" + "     break" + "\n"
        + "   case 'x' (or 'G'):" + "\n"
        + "     search for input value in next steps on 1st tape (for 'x') or 3rd tape (for 'G')"
        + "\n" + "     break" + "\n" + "   case '0' (or '1'):" + "\n"
        + "     copy this character onto 4th tape" + "\n"
        + "     if (all bits which represent this input number are read) {" + "\n"
        + "       move the read/write head of the 1st (or 3rd) tape to the leftmost position"
        + "\n" + "       while (4th tape's value != 1) {" + "\n" + "         value--" + "\n"
        + "         move the read/write head of the 1st (or 3rd) tape one position to the right"
        + "\n" + "       }" + "\n" + "       read and remember the found value in state" + "\n"
        + "     }" + "\n" + "     break" + "\n" + " }" + "\n"
        + " if (all input values needed for the current operation are found) {" + "\n"
        + "   evaluate the current gate" + "\n" + "   write down the result on 3rd tape " + "\n"
        + " }" + "\n" + "}";
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
