package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import algoanim.animalscript.AnimalGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringArrayGenerator;
import algoanim.animalscript.AnimalStringMatrixGenerator;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.StringArrayGenerator;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.items.EnumerationPropertyItem;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

/**
 * Generator for the Longest Common Substring algorithm
 * 
 * @author Pablo Hoch
 */
public class LongestCommonSubstringGenerator implements Generator {

  // Set to true to use my fixed generator or to false to use the default
  // AnimalStringMatrixGenerator
  // see the FixedAnimalStringMatrixGenerator class below
  private static final boolean  USE_FIXED_STRING_MATRIX_GENERATOR = true;
  // Same for AnimalStringArrayGenerator (the bug is actually in
  // AnimalArrayGenerator)
  private static final boolean  USE_FIXED_ANIMAL_ARRAY_GENERATOR  = true;
  // Set this to true to suppress the "refresh" command for the matrix.
  // This fixes the bug where the cell backgrounds have completely wrong
  // positions, but
  // the cells don't resize to fit the content.
  private static final boolean  SUPPRESS_GRID_REFRESH             = true;

  AnimalScript                  lang;

  // Properties
  private String                S;
  private String                T;
  private boolean               includeQuestions;
  private TextProperties        titleTP;
  private SourceCodeProperties  sourceCodeProperties;
  private TextProperties        descriptionTP;
  private MatrixProperties      matrixProperties;
  private RectProperties        titleRP;
  private ArrayMarkerProperties arrayMarkerProperties;
  private ArrayProperties       arrayProperties;
  private TextProperties        variablesTP;

  // Spacing between two lines of text
  private static final int      LINE_SPACING                      = 5;

  private final Timing          updateDelay                       = new TicksTiming(
                                                                      10);
  private final Timing          matrixT                           = new TicksTiming(
                                                                      10);
  private final Timing          matrixD                           = new TicksTiming(
                                                                      30);
  private final TicksTiming     markerMoveTiming                  = new TicksTiming(
                                                                      10);

  // Questions
  private static final String   Q_CORRECT                         = "Richtig. ";
  private static final String   Q_WRONG                           = "Falsch. ";
  private static final String   Q_MATRIX_ENTRY_EXPLANATION        = "Der Wert gibt die Länge des Substrings an und "
                                                                      + "die Position in der Matrix gibt an, an welcher Position in den Eingabestrings der Substring endet. "
                                                                      + "Die Zeile (i) bezieht sich auf den ersten String (S), die Spalte (j) auf den zweiten String (T).";

  // AnimalScript variables
  private Text                  title;
  private Rect                  titleRect;
  private SourceCode            sc;
  private StringMatrix          matrix;
  private Variables             vars;
  private Text                  maxLenLabel, iLabel, jLabel;
  private Text                  maxLenValue, iValue, jValue;
  private StringListDisplay     retList;
  private Text                  sLabel, tLabel;
  private StringArray           sArray, tArray;
  private ArrayMarker           iMarker, jMarker;
  private Polyline              moveRetListLine;
  private final Hidden          hidden                            = new Hidden();
  private TwoValueCounter       matrixCounter;
  private TwoValueView          matrixCounterView;
  private Text                  matrixCounterTitle;

  private QuestionGroupModel    qGroupEnterSubstring;
  private QuestionGroupModel    qGroupMC;

  private int                   maxLen;
  private int                   enterSubstringId;
  private int                   mcId;
  private Random                rand;                                                                                                                                      // used
                                                                                                                                                                            // for
                                                                                                                                                                            // questions

  public void init() {
    // moved to generate(), because the size depends on the input variables
    // lang = new
    // AnimalScript("Longest Common Substring (Dynamische Programmierung) [DE]",
    // "Pablo Hoch", 820, 1000);
    rand = new Random();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // Get input
    S = (String) primitives.get("S");
    T = (String) primitives.get("T");
    includeQuestions = (Boolean) primitives.get("includeQuestions");

    // Estimate the required size of the animation
    // The height is not the height of the canvas however... not even the height
    // of the window...
    // Anyway, a part of the source code will be hidden if the matrix is too
    // high, because no matter how large
    // the height is, the canvas seems to be limited by the screen resolution :(
    int height = 850 + 30 * S.length();
    int width = Math.max(820, 500 + 45 * T.length());
    lang = new AnimalScript(
        "Longest Common Substring (Dynamische Programmierung) [DE]",
        "Pablo Hoch", width, height);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    // Get properties
    titleTP = (TextProperties) props.getPropertiesByName("title");
    descriptionTP = (TextProperties) props.getPropertiesByName("description");
    variablesTP = (TextProperties) props.getPropertiesByName("variables");
    titleRP = (RectProperties) props.getPropertiesByName("titleRect");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    matrixProperties = (MatrixProperties) props.getPropertiesByName("matrix");
    arrayMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker");
    arrayProperties = (ArrayProperties) props.getPropertiesByName("array");

    // Fix font sizes (it's only possible to set the font name in the generator
    // gui...)
    patchFont(titleTP, Font.BOLD, 24);
    // patchFont(currentStepTP, Font.PLAIN, 14);
    patchFont(descriptionTP, Font.PLAIN, 14);
    patchFont(variablesTP, Font.PLAIN, 14);
    patchFont(sourceCodeProperties, Font.PLAIN, 12);

    lang.setStepMode(true);

    generateIntro();
    generateSteps();

    lang.finalizeGeneration();

    return lang.toString();
  }

  private void generateIntro() {
    title = lang.newText(new Coordinates(20, 20), "Longest Common Substring",
        "title", null, titleTP);

    titleRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    titleRect = lang.newRect(new Offset(-5, -5, "title", "NW"), new Offset(5,
        5, "title", "SE"), "titleRect", null, titleRP);

    generateMultiLineText(
        new Coordinates(20, 50),
        "pt",
        new String[] {
            "Der Longest Common Substring Algorithmus erhält als Eingabe zwei Strings und liefert alle",
            "Substrings maximaler Länge zurück, die in beiden Eingabestrings vorkommen.",
            "In dieser Animation ist der Algorithmus mittels dynamischer Programmierung implementiert.",
            "Der Algorithmus hat Komplexität O(mn), wobei m und n die Längen der Eingabestrings sind.",
            "",
            "Der Algorithmus verwendet eine mxn Matrix. Zur Veranschaulichung ist in dieser Animation",
            "links der Matrix der erste String (S) und oberhalb der Matrix der zweite String (T) abgetragen.",
            "In der ausgefüllten Matrix besagt eine Zahl x an Position (i, j), dass die beiden Strings",
            "einen gemeinsamen Substring S[i-x..i] == T[j-x..j] besitzen. Der Eintrag in der Matrix gibt",
            "also die Länge des Substrings an, der in S an der Position i (Zeile) endet und in T an der",
            "Position j (Spalte) endet.",
            "Die Matrix wird von oben nach unten und links nach rechts ausgefüllt.",
            "Noch nicht ausgefüllte Einträge der Matrix werden in dieser Animation als - dargestellt.",
            // "Zu Beginn sind alle Einträge der Matrix 0, zur besseren Veranschaulichung, welche Einträge",
            // "der Matrix bereits gefüllt wurden, werden in dieser Animation noch nicht bearbeitete",
            // "Einträge jedoch als - dargestellt.",
            "",
            "Am Ende des Algorithmus gibt der größte Eintrag in der Matrix die Länge des längsten gemeinsamen",
            "Substrings an. Der Algorithmus sammelt außerdem während der Ausführung alle Substrings mit bis dahin",
            "maximaler Länge, so dass diese zurückgegeben werden können." });

    lang.nextStep("Einleitung");
  }

  private void generateSteps() {
    // Hide intro text
    lang.hideAllPrimitives();
    title.show();
    titleRect.show();

    // Generate primitives
    vars = lang.newVariables();
    generateMatrix();
    generateCode();

    lang.nextStep("Initialisierung");

    if (includeQuestions) {
      generateQuestionGroups();
    }

    // Generate slides for each step
    generateLCS();

  }

  private void generateMatrix() {
    String[][] matrixData = new String[S.length() + 1][T.length() + 1];

    matrixData[0][0] = "";
    for (int i = 0; i < S.length(); i++) {
      matrixData[i + 1][0] = Character.toString(S.charAt(i));
    }
    for (int i = 0; i < T.length(); i++) {
      matrixData[0][i + 1] = Character.toString(T.charAt(i));
    }
    for (int i = 1; i < matrixData.length; i++) {
      for (int j = 1; j < matrixData[0].length; j++) {
        matrixData[i][j] = "-";
      }
    }

    // because of rendering bugs...
    matrixProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");

    if (USE_FIXED_STRING_MATRIX_GENERATOR)
      matrix = new StringMatrix(new FixedAnimalStringMatrixGenerator(lang),
          new Offset(0, 40, titleRect, "SW"), matrixData, "L", null,
          matrixProperties);
    else
      matrix = lang.newStringMatrix(new Offset(0, 40, titleRect, "SW"),
          matrixData, "L", null, matrixProperties);

    // using display properties Hidden() doesn't work
    matrix.hide();

  }

  private void generateCode() {
    sc = lang.newSourceCode(new Offset(0, 40, matrix, "SW"), "code", null,
        sourceCodeProperties);

    sc.addCodeLine("Set<String> lcs(String S, String T) {", null, 0, null); // 0
    sc.addCodeLine("int maxLen = 0;", null, 1, null); // 1
    sc.addCodeLine("Set<String> ret = new HashSet<String>();", null, 1, null); // 2
    sc.addCodeLine("if (S.isEmpty() || T.isEmpty()) return ret;", null, 1, null); // 3
    sc.addCodeLine("int[][] L = new int[S.length()][T.length()];", null, 1,
        null); // 4
    sc.addCodeLine("", null, 1, null);

    sc.addCodeLine("for (int i = 0; i < S.length(); i++) {", null, 1, null); // 6
    sc.addCodeLine("for (int j = 0; j < T.length(); j++) {", null, 2, null); // 7
    sc.addCodeLine("", null, 3, null);

    sc.addCodeLine("if (S.charAt(i) == T.charAt(j)) {", null, 3, null); // 9

    sc.addCodeLine("if (i == 0 || j == 0)", null, 4, null); // 10
    sc.addCodeLine("L[i][j] = 1;", null, 5, null); // 11
    sc.addCodeLine("else", null, 4, null); // 12
    sc.addCodeLine("L[i][j] = L[i-1][j-1] + 1;", null, 5, null); // 13
    sc.addCodeLine("", null, 4, null);
    sc.addCodeLine("if (L[i][j] > maxLen) {", null, 4, null); // 15
    sc.addCodeLine("maxLen = L[i][j];", null, 5, null); // 16
    sc.addCodeLine("ret.clear();", null, 5, null); // 17
    sc.addCodeLine("ret.add(S.substring(i-maxLen+1, i+1));", null, 5, null); // 18
    sc.addCodeLine("} else if (L[i][j] == maxLen) {", null, 4, null); // 19
    sc.addCodeLine("ret.add(S.substring(i-maxLen+1, i+1));", null, 5, null); // 20
    sc.addCodeLine("}", null, 4, null); // 21

    sc.addCodeLine("} else {", null, 3, null); // 22
    sc.addCodeLine("L[i][j] = 0;", null, 4, null); // 23
    sc.addCodeLine("}", null, 3, null); // 24
    sc.addCodeLine("", null, 3, null);

    sc.addCodeLine("}", null, 2, null); // 26
    sc.addCodeLine("}", null, 1, null); // 27
    sc.addCodeLine("", null, 1, null);

    sc.addCodeLine("return ret;", null, 1, null); // 29
    sc.addCodeLine("}", null, 0, null); // 30

  }

  private void generateVariableDisplay() {
    TextProperties plainTP = variablesTP;

    TextProperties boldTP = new TextProperties();
    boldTP.set(AnimationPropertiesKeys.FONT_PROPERTY,
        plainTP.get(AnimationPropertiesKeys.FONT_PROPERTY));
    boldTP.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        plainTP.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    patchFont(boldTP, Font.BOLD, 14);

    maxLenLabel = lang.newText(new Offset(40, 0, matrix, "NE"), "maxLen = ",
        "maxLenLabel", null, plainTP);
    maxLenValue = lang.newText(new Offset(0, 0, maxLenLabel, "NE"), "0",
        "maxLenValue", null, boldTP);

    iLabel = lang.newText(new Offset(0, LINE_SPACING, maxLenLabel, "SW"),
        "i = ", "iLabel", hidden, plainTP);
    iValue = lang.newText(new Offset(0, 0, iLabel, "NE"), "0", "iValue",
        hidden, boldTP);

    jLabel = lang.newText(new Offset(0, LINE_SPACING, iLabel, "SW"), "j = ",
        "jLabel", hidden, plainTP);
    jValue = lang.newText(new Offset(0, 0, jLabel, "NE"), "0", "jValue",
        hidden, boldTP);

    retList = new StringListDisplay(new Offset(60, 80, sc, "NE"), "retList",
        "Substrings maximaler Länge (ret):", boldTP, plainTP);

    moveRetListLine = lang.newPolyline(new Node[] {
        new Offset(60, 80, sc, "NE"), new Offset(0, 80, sc, "NW") },
        "moveRetListLine", hidden);
  }

  private void generateArrays() {
    Offset upperLeft = new Offset(80, 40, maxLenValue, "NE");
    if (S.length() < 4) {
      upperLeft = new Offset(80, -80, sc, "NE");
    }

    sLabel = lang.newText(upperLeft, "S = ", "sLabel", null, variablesTP);
    tLabel = lang.newText(new Offset(0, 80, sLabel, "NW"), "T = ", "tLabel",
        null, variablesTP);

    if (USE_FIXED_ANIMAL_ARRAY_GENERATOR) {
      StringArrayGenerator sag = new FixedAnimalStringArrayGenerator(lang);
      sArray = new StringArray(sag, new Offset(0, 0, sLabel, "NE"),
          stringToArray(S), "sArray", null, arrayProperties);
      tArray = new StringArray(sag, new Offset(0, 0, tLabel, "NE"),
          stringToArray(T), "tArray", null, arrayProperties);
    } else {
      sArray = lang.newStringArray(new Offset(0, 0, sLabel, "NE"),
          stringToArray(S), "sArray", null, arrayProperties);
      tArray = lang.newStringArray(new Offset(0, 0, tLabel, "NE"),
          stringToArray(T), "tArray", null, arrayProperties);
    }

    arrayMarkerProperties.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY,
        true);
    arrayMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    iMarker = lang.newArrayMarker(sArray, 0, "iMarker", hidden,
        arrayMarkerProperties);

    arrayMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
    jMarker = lang.newArrayMarker(tArray, 0, "jMarker", hidden,
        arrayMarkerProperties);
  }

  private String[] stringToArray(String s) {
    String[] arr = new String[s.length()];
    for (int i = 0; i < s.length(); i++)
      arr[i] = Character.toString(s.charAt(i));
    return arr;
  }

  private void generateCounter() {
    matrixCounter = lang.newCounter(matrix);

    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.blue);
    cp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));

    TextProperties boldTP = new TextProperties();
    boldTP.set(AnimationPropertiesKeys.FONT_PROPERTY,
        variablesTP.get(AnimationPropertiesKeys.FONT_PROPERTY));
    boldTP.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        variablesTP.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    patchFont(boldTP, Font.BOLD, 14);

    matrixCounterTitle = lang.newText(new Offset(60, -85, sc, "SE"),
        "Zugriffe auf Matrix:", "matrixCounterTitle", null, boldTP);

    matrixCounterView = lang.newCounterView(matrixCounter, new Offset(60, -60,
        sc, "SE"), cp, true, false, new String[] { "Schreibzugriffe:",
        "Lesezugriffe:" });
  }

  private void generateQuestionGroups() {
    // according to the documentation the numberOfRepeats parameter should
    // limit the number of times a question of this group is displayed
    // (should only be displayed until the user has answered it correctly
    // numberOfRepeat times)
    // but it seems that the questions are always displayed, regardless of this
    // number :(
    qGroupEnterSubstring = new QuestionGroupModel("qGroupEnterSubstring", 2);
    lang.addQuestionGroup(qGroupEnterSubstring);

    qGroupMC = new QuestionGroupModel("qGroupMC", 2);
    lang.addQuestionGroup(qGroupMC);

    enterSubstringId = 0;
    mcId = 0;
  }

  private void addEnterSubstringQuestion(int matrixValue, String answer) {
    FillInBlanksQuestionModel q = new FillInBlanksQuestionModel(
        "qEnterSubstring" + (enterSubstringId++));
    q.setPrompt("Auf welchen gemeinsamen Substring bezieht sich der markierte Eintrag der Matrix mit dem Wert "
        + matrixValue + "?");
    q.addAnswer(answer, 1, Q_CORRECT + Q_MATRIX_ENTRY_EXPLANATION);
    // apparently it's not possible to set a feedback text for wrong answers
    // (the user can enter ANY string...)
    q.setGroupID(qGroupEnterSubstring.getID());

    lang.addFIBQuestion(q);
  }

  private void addMCQuestion(int matrixValue, String substring) {
    MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel("qMC"
        + (mcId++));
    q.setPrompt("Welche Bedeutung hat der markierte Eintrag der Matrix mit dem Wert "
        + matrixValue + "?");
    q.setGroupID(qGroupMC.getID());

    String explanation = Q_MATRIX_ENTRY_EXPLANATION
        + " Der Substring lautet in diesem Fall '" + substring + "'.";

    q.addAnswer("Er gibt die Länge eines gemeinsamen Substrings an.", 1,
        Q_CORRECT + explanation);

    String[] wrongAnswers = new String[] {
        "Er gibt an, wieviele gemeinsame Substrings bisher gefunden wurden.",
        "Er gibt an, wieviele gemeinsame Substrings maximaler Länge bisher gefunden wurden.",
        "Er gibt die Position im ersten String (S) an, an der ein gemeinsamer Substring endet.",
        "Er gibt die Position im zweiten String (T) an, an der ein gemeinsamer Substring endet.", };

    // pick two wrong answers at random
    int i = rand.nextInt(wrongAnswers.length);
    int j = (i + 1 + rand.nextInt(wrongAnswers.length - 1))
        % wrongAnswers.length;

    q.addAnswer(wrongAnswers[i], 0, Q_WRONG + explanation);
    q.addAnswer(wrongAnswers[j], 0, Q_WRONG + explanation);

    lang.addMCQuestion(q);
  }

  private void generateLCS() {

    // Initialization

    sc.highlight(1);
    sc.highlight(2);

    vars.declare("int", "maxLen", "0");
    generateVariableDisplay();
    generateArrays();

    int maxLen = 0; // 1
    Set<String> ret = new HashSet<String>(); // 2

    lang.nextStep();

    sc.unhighlight(1);
    sc.unhighlight(2);
    sc.highlight(3);
    lang.nextStep();

    if (S.isEmpty() || T.isEmpty()) { // 3
      // one of the strings is empty, exit
      return;
    }

    sc.toggleHighlight(3, 4);
    matrix.show();

    generateCounter();

    int[][] L = new int[S.length()][T.length()]; // 4

    lang.nextStep();

    sc.unhighlight(4);

    // Loop

    vars.declare("int", "i", "0");
    iLabel.show();
    iValue.show();
    iMarker.show();

    for (int i = 0; i < S.length(); i++) { // 6
      sc.highlight(6);
      vars.set("i", Integer.toString(i));
      iValue.setText(Integer.toString(i), null, null);
      if (i != 0)
        iMarker.move(i, null, markerMoveTiming);
      matrix.highlightElem(i + 1, 0, null, null); // S
      lang.nextStep((i + 1) + ". Iteration der äußeren Schleife (S[" + i
          + "] = '" + Character.toString(S.charAt(i)) + "')");
      sc.unhighlight(6);
      sc.highlight(6, 0, true);

      if (i == 0)
        vars.declare("int", "j", "0");
      jLabel.show();
      jValue.show();
      jMarker.move(0, null, null);
      jMarker.show();

      for (int j = 0; j < T.length(); j++) { // 7
        sc.highlight(7);
        vars.set("j", Integer.toString(j));
        jValue.setText(Integer.toString(j), null, null);
        if (j != 0)
          jMarker.move(j, null, markerMoveTiming);
        matrix.highlightElem(0, j + 1, null, null); // T
        lang.nextStep("   " + (j + 1) + ". Iteration der inneren Schleife (T["
            + j + "] = '" + Character.toString(T.charAt(j)) + "')");
        sc.unhighlight(7);
        sc.highlight(7, 0, true);

        sc.highlight(9);

        sArray.highlightElem(i, null, null);
        tArray.highlightElem(j, null, null);

        lang.nextStep();

        sArray.unhighlightElem(i, null, null);
        tArray.unhighlightElem(j, null, null);

        if (S.charAt(i) == T.charAt(j)) { // 9
          sc.toggleHighlight(9, 10);
          sc.highlight(9, 0, true);
          lang.nextStep();

          matrix.highlightCell(i + 1, j + 1, null, null);
          if (i == 0 || j == 0) { // 10
            sc.toggleHighlight(10, 11);
            sc.highlight(10, 0, true);

            matrix.put(i + 1, j + 1, "1", matrixT, matrixD);

            lang.nextStep();

            L[i][j] = 1; // 11

            sc.unhighlight(11);
          } else { // 12
            sc.toggleHighlight(10, 13);
            sc.highlight(10, 0, true);
            sc.highlight(12, 0, true);

            matrix.getElement(i - 1, j - 1); // for the counter

            matrix.highlightElem(i, j, null, null);
            matrix.put(i + 1, j + 1, Integer.toString(L[i - 1][j - 1] + 1),
                matrixT, matrixD);

            lang.nextStep();

            L[i][j] = L[i - 1][j - 1] + 1; // 13

            matrix.unhighlightElem(i, j, null, null);
            sc.unhighlight(12);
            sc.unhighlight(13);
          }

          sc.unhighlight(10);
          sc.highlight(15);
          matrix.getElement(i, j); // for the counter

          if (includeQuestions && L[i][j] >= maxLen) {
            // insert a question
            // randomly choose between FIB or MC question
            String substring = S.substring(i - L[i][j] + 1, i + 1);
            if (rand.nextInt(2) == 0) {
              addEnterSubstringQuestion(L[i][j], substring);
            } else {
              addMCQuestion(L[i][j], substring);
            }
          }

          lang.nextStep();

          if (L[i][j] > maxLen) { // 15
            sc.toggleHighlight(15, 16);
            sc.highlight(15, 0, true);
            matrix.getElement(i, j); // for the counter
            maxLen = L[i][j]; // 16
            vars.set("maxLen", Integer.toString(maxLen));
            maxLenValue.setText(Integer.toString(maxLen), updateDelay, null);
            lang.nextStep();

            sc.toggleHighlight(16, 17);
            sc.highlight(18);
            retList.clear();
            retList.append(S.substring(i - maxLen + 1, i + 1), updateDelay);

            sArray.highlightCell(i - maxLen + 1, i, null, null);
            tArray.highlightCell(j - maxLen + 1, j, null, null);

            lang.nextStep();

            ret.clear(); // 17
            ret.add(S.substring(i - maxLen + 1, i + 1)); // 18

            sArray.unhighlightCell(i - maxLen + 1, i, null, null);
            tArray.unhighlightCell(j - maxLen + 1, j, null, null);

            sc.unhighlight(15);
            sc.unhighlight(17);
            sc.unhighlight(18);
          } else { // -- 19--

            sc.toggleHighlight(15, 19);
            matrix.getElement(i, j); // for the counter
            lang.nextStep();

            if (L[i][j] == maxLen) { // --19--
              String substring = S.substring(i - maxLen + 1, i + 1);

              sc.toggleHighlight(19, 20);
              sc.highlight(19, 0, true);
              if (!ret.contains(substring))
                retList.append(S.substring(i - maxLen + 1, i + 1), updateDelay);

              sArray.highlightCell(i - maxLen + 1, i, null, null);
              tArray.highlightCell(j - maxLen + 1, j, null, null);

              lang.nextStep();

              ret.add(substring); // 20

              sArray.unhighlightCell(i - maxLen + 1, i, null, null);
              tArray.unhighlightCell(j - maxLen + 1, j, null, null);

              sc.unhighlight(20);
            }

            sc.unhighlight(19);
          } // 21

          matrix.unhighlightCell(i + 1, j + 1, null, null);

        } else { // 22
          sc.toggleHighlight(9, 23);
          sc.highlight(9, 0, true);
          sc.highlight(22, 0, true);

          matrix.highlightCell(i + 1, j + 1, null, null);
          matrix.put(i + 1, j + 1, "0", matrixT, matrixD);

          lang.nextStep();

          L[i][j] = 0; // 23

          matrix.unhighlightCell(i + 1, j + 1, null, null);

          sc.unhighlight(22);
          sc.unhighlight(23);
        } // 24

        matrix.unhighlightElem(0, j + 1, null, null); // T

        sc.unhighlight(9);
        sc.unhighlight(7);
      } // 26

      matrix.unhighlightElem(i + 1, 0, null, null); // S

      jLabel.hide();
      jValue.hide();
      jMarker.hide();

    } // 27

    sc.highlight(6, 0, true);
    sc.highlight(27);
    lang.nextStep();
    sc.unhighlight(6);
    sc.unhighlight(27);

    iLabel.hide();
    iValue.hide();
    iMarker.hide();

    sc.highlight(29);
    lang.nextStep();

    sc.unhighlight(29);
    lang.nextStep();

    // return ret; // 29

    this.maxLen = maxLen;

    generateFinalSlide(L);
  } // 30

  // private Set<String> lcs(String S, String T) {
  // int maxLen = 0;
  // Set<String> ret = new HashSet<String>();
  // if (S.isEmpty() || T.isEmpty()) return ret;
  // int[][] L = new int[S.length()][T.length()];
  //
  // for (int i = 0; i < S.length(); i++) {
  // for (int j = 0; j < T.length(); j++) {
  //
  // if (S.charAt(i) == T.charAt(j)) {
  // if (i == 0 || j == 0)
  // L[i][j] = 1;
  // else
  // L[i][j] = L[i-1][j-1] + 1;
  //
  // if (L[i][j] > maxLen) {
  // maxLen = L[i][j];
  // ret.clear();
  // ret.add(S.substring(i-maxLen+1, i+1));
  // } else if (L[i][j] == maxLen) {
  // ret.add(S.substring(i-maxLen+1, i+1));
  // }
  // } else {
  // L[i][j] = 0;
  // }
  //
  // }
  // }
  //
  // System.err.println("LCS found:");
  // for (String s : ret) {
  // System.err.println(s);
  // }
  //
  // return ret;
  // }

  private void generateFinalSlide(int[][] L) {

    sc.hide();

    TicksTiming animationTiming = new TicksTiming(30);

    Polyline moveSArrayLine = lang.newPolyline(new Node[] {
        new Offset(0, 0, sLabel, "NW"), new Offset(80, 0, maxLenValue, "NE") },
        "moveSArrayLine", hidden);

    Polyline moveTArrayLine = lang.newPolyline(
        new Node[] { new Offset(0, 0, tLabel, "NW"),
            new Offset(80, 40, maxLenValue, "NE") }, "moveTArrayLine", hidden);

    sLabel.moveVia("NW", "translate", moveSArrayLine, animationTiming,
        animationTiming);
    sArray.moveVia("NW", "translate", moveSArrayLine, animationTiming,
        animationTiming);

    tLabel.moveVia("NW", "translate", moveTArrayLine, animationTiming,
        animationTiming);
    tArray.moveVia("NW", "translate", moveTArrayLine, animationTiming,
        animationTiming);

    String[] lines;

    if (maxLen > 0)
      lines = new String[] {
          "Der längste gemeinsame Substring hat die Länge " + maxLen + ".",
          "Es gibt "
              + (retList.size() == 1 ? "genau einen" : retList.size()
                  + " solche") + " Substrings (siehe unten).", };
    else
      lines = new String[] { "Die beiden Strings haben keine gemeinsamen Substrings.", };

    generateMultiLineText(new Offset(0, 0, sc, "NW"), "ft", lines);

    if (retList.size() == 0) {
      retList.hide();
    } else {
      retList.moveVia("NW", moveRetListLine, animationTiming, animationTiming);
    }

    // Highlight maxLen values in matrix
    if (maxLen > 0) {
      for (int i = 0; i < L.length; i++) {
        for (int j = 0; j < L[i].length; j++) {
          if (L[i][j] == maxLen) {
            matrix
                .highlightCell(i + 1, j + 1, animationTiming, animationTiming);
          }
        }
      }
    }

    if (matrixCounterTitle != null && matrixCounterView != null) {
      Polyline moveMatrixCounterLine = lang.newPolyline(new Node[] {
          new Offset(0, 0, matrixCounterTitle, "NW"),
          new Offset(0, 120 + (retList.size() * 25), sc, "NW") },
          "moveMatrixCounterLine", hidden);

      matrixCounterTitle.moveVia("NW", "translate", moveMatrixCounterLine,
          animationTiming, animationTiming);
      matrixCounterView.moveVia("NW", moveMatrixCounterLine, animationTiming,
          animationTiming);
    }

    lang.nextStep("Ende");
  }

  private void patchFont(AnimationProperties tp, int style, int size) {
    Font baseFont = (Font) tp.get(AnimationPropertiesKeys.FONT_PROPERTY);
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(baseFont.getName(),
        style, size));
  }

  /**
   * Generate multiple lines of text
   * 
   * @param upperLeft
   *          Location
   * @param namePrefix
   *          Prefix for generated ids
   * @param lines
   *          Text lines
   */
  private void generateMultiLineText(Node upperLeft, String namePrefix,
      String[] lines) {
    if (lines.length == 0)
      return;

    Text prevLine = lang.newText(upperLeft, lines[0], namePrefix + "1", null,
        descriptionTP);
    int offset = LINE_SPACING;
    int counter = 2;

    for (int i = 1; i < lines.length; i++) {
      String line = lines[i];
      if (line != null && line.length() > 0) {
        prevLine = lang.newText(new Offset(0, offset, prevLine, "SW"), line,
            namePrefix + counter, null, descriptionTP);
        counter++;
        offset = LINE_SPACING;
      } else {
        // empty line -> increase offset for next line
        offset = LINE_SPACING + 20;
      }
    }
  }

  public String getName() {
    return "Longest Common Substring (Dynamische Programmierung) [DE]";
  }

  public String getAlgorithmName() {
    return "Longest Common Substring";
  }

  public String getAnimationAuthor() {
    return "Pablo Hoch";
  }

  public String getDescription() {
    return "Der Longest Common Substring Algorithmus erh&auml;lt als Eingabe zwei Strings und liefert alle\n"
        + "Substrings maximaler L&auml;nge zurück, die in beiden Eingabestrings vorkommen.\n"
        + "In dieser Animation ist der Algorithmus mittels dynamischer Programmierung implementiert.\n"
        + "Der Algorithmus hat Komplexit&auml;t O(mn), wobei m und n die L&auml;ngen der Eingabestrings sind.\n"
        + "\n"
        + "<p>Der Algorithmus verwendet eine mxn Matrix. Zur Veranschaulichung ist in dieser Animation\n"
        + "links der Matrix der erste String (S) und oberhalb der Matrix der zweite String (T) abgetragen.\n"
        + "In der ausgef&uuml;llten Matrix besagt eine Zahl x an Position (i, j), dass die beiden Strings\n"
        + "einen gemeinsamen Substring S[i-x..i] == T[j-x..j] besitzen. Der Eintrag in der Matrix gibt\n"
        + "also die L&auml;nge des Substrings an, der in S an der Position i (Zeile) endet und in T an der\n"
        + "Position j (Spalte) endet.\n"
        + "<br>Die Matrix wird von oben nach unten und links nach rechts ausgef&uuml;llt.\n"
        + "Noch nicht ausgef&uuml;llte Einträge der Matrix werden in dieser Animation als - dargestellt.\n"
        +
        // "Zu Beginn sind alle Einträge der Matrix 0, zur besseren Veranschaulichung, welche Einträge\n"+
        // "der Matrix bereits gefüllt wurden, werden in dieser Animation noch nicht bearbeitete\n"+
        // "Einträge jedoch als - dargestellt.\n"+
        "\n"
        + "<p>Am Ende des Algorithmus gibt der gr&ouml;&szlig;te Eintrag in der Matrix die Länge des l&auml;ngsten gemeinsamen\n"
        + "Substrings an. Der Algorithmus sammelt au&szlig;erdem w&auml;hrend der Ausf&uuml;hrung alle Substrings mit bis dahin\n"
        + "maximaler L&auml;nge, so dass diese zur&uuml;ckgegeben werden k&ouml;nnen.";
  }

  public String getCodeExample() {
    return "Set&lt;String&gt; lcs(String S, String T) {\n"
        + "    int maxLen = 0;\n"
        + "    Set&lt;String&gt; ret = new HashSet&lt;String&gt;();\n"
        + "    if (S.isEmpty() || T.isEmpty()) return ret;\n"
        + "    int[][] L = new int[S.length()][T.length()];\n" + "\n"
        + "    for (int i = 0; i &lt; S.length(); i++) {\n"
        + "        for (int j = 0; j &lt; T.length(); j++) {\n" + "\n"
        + "            if (S.charAt(i) == T.charAt(j)) {\n"
        + "                if (i == 0 || j == 0)\n"
        + "                    L[i][j] = 1;\n" + "                else\n"
        + "                    L[i][j] = L[i-1][j-1] + 1;\n" + "\n"
        + "                if (L[i][j] &gt; maxLen) {\n"
        + "                    maxLen = L[i][j];\n"
        + "                    ret.clear();\n"
        + "                    ret.add(S.substring(i-maxLen+1, i+1));\n"
        + "                } else if (L[i][j] == maxLen) {\n"
        + "                    ret.add(S.substring(i-maxLen+1, i+1));\n"
        + "                }\n" + "            } else {\n"
        + "                L[i][j] = 0;\n" + "            }\n" + "\n"
        + "        }\n" + "    }\n" + "\n" + "    return ret;\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  private class StringListDisplay {
    private String         baseId;
    private Text           header;
    private List<Text>     texts;
    private TextProperties entryTP;
    private int            inUse;

    StringListDisplay(Node upperLeft, String baseId, String title,
        TextProperties titleTP, TextProperties entryTP) {
      this.baseId = baseId;
      this.entryTP = entryTP;
      this.inUse = 0;
      this.texts = new ArrayList<Text>();

      header = lang.newText(upperLeft, title, baseId + "Title", null, titleTP);
    }

    public void clear() {
      for (int i = 0; i < inUse; i++) {
        texts.get(i).hide();
      }
      inUse = 0;
    }

    public void append(String str, Timing delay) {
      if (inUse < texts.size()) {
        // reuse existing line
        Text e = texts.get(inUse);
        e.setText(str, delay, null);
        e.show(delay);
      } else {
        // create new line
        int i = texts.size();
        Text previous = i == 0 ? header : texts.get(i - 1);
        Text e = lang.newText(new Offset(0, LINE_SPACING, previous, "SW"), str,
            baseId + i, delay, entryTP);
        texts.add(e);
      }
      inUse++;
    }

    // public void show(Timing t) {
    // header.show(t);
    // for (Text e : texts)
    // e.show(t);
    // }

    // public void show() {
    // show(null);
    // }

    public void hide(Timing t) {
      header.hide(t);
      for (Text e : texts)
        e.hide(t);
    }

    public void hide() {
      hide(null);
    }

    public int size() {
      return inUse;
    }

    public void moveVia(String direction, Primitive via, Timing delay,
        Timing duration) {
      header.moveVia(direction, "translate", via, delay, duration);
      for (int i = 0; i < inUse; i++) {
        texts.get(i).moveVia(direction, "translate", via, delay, duration);
      }
    }
  }

  private static class FixedAnimalStringMatrixGenerator extends
      AnimalStringMatrixGenerator {
    FixedAnimalStringMatrixGenerator(AnimalScript as) {
      super(as);
    }

    @SuppressWarnings("unused")
    @Override
    public boolean create(StringMatrix aMatrix) {
      // skipped name generation, because i don't need it
      lang.addItem(aMatrix);

      StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
      def.append("grid \"").append(aMatrix.getName()).append("\" ");
      def.append(AnimalGenerator.makeNodeDef(aMatrix.getUpperLeft()));
      int nrRows = aMatrix.getNrRows(), nrCols = aMatrix.getNrCols();
      def.append(" lines ").append(nrRows).append(" columns ");
      def.append(nrCols).append(' ');

      /* Properties */
      MatrixProperties matrixProps = aMatrix.getProperties();
      // fix #1 from AnimalIntMatrixGenetator: use GRID_STYLE_PROPERTY
      EnumerationPropertyItem styleItem = (EnumerationPropertyItem) matrixProps
          .getItem(AnimationPropertiesKeys.GRID_STYLE_PROPERTY);
      if (styleItem != null)
        def.append("style ").append(styleItem.getChoice()).append(' ');
      addColorOption(matrixProps, def);
      // fix #2 from AnimalIntMatrixGenerator: must be "textColor", not
      // "elementColor"
      addColorOption(matrixProps,
          AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, " textColor ", def);
      addColorOption(matrixProps, AnimationPropertiesKeys.FILL_PROPERTY,
          " fillColor ", def);
      addColorOption(matrixProps,
          AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
          " highlightTextColor ", def);
      addColorOption(matrixProps,
          AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
          " highlightBackColor ", def);
      addIntOption(matrixProps, AnimationPropertiesKeys.DEPTH_PROPERTY,
          " depth ", def);

      lang.addLine(def);
      // generate the elements...
      for (int row = 0; row < nrRows; row++) {
        for (int col = 0; col < nrCols; col++) {
          StringBuilder sb = new StringBuilder(128);
          sb.append("setGridValue \"").append(aMatrix.getName());
          sb.append("[").append(row).append("][").append(col);
          sb.append("]\" \"").append(aMatrix.getElement(row, col));
          sb.append("\"");
          // according to the forums this causes the layout bug...
          if (row == nrRows - 1 && col == nrCols - 1 && !SUPPRESS_GRID_REFRESH)
            sb.append(" refresh");
          lang.addLine(sb.toString());
        }
      }
      return true;
    }

    // fix #3: actually generate highlight code for gridELEM instead of
    // gridCELL.
    // these don't do anything though.

    // @Override
    // public void highlightElem(StringMatrix intMatrix, int row, int col,
    // Timing offset, Timing duration) {
    // StringBuilder sb = new StringBuilder(128);
    // sb.append("highlightGridElem \"").append(intMatrix.getName());
    // sb.append("[").append(row).append("][").append(col).append("]\" ");
    // addWithTiming(sb, offset, duration);
    // }
    //
    // @Override
    // public void unhighlightElem(StringMatrix intMatrix, int row, int col,
    // Timing offset, Timing duration) {
    // StringBuilder sb = new StringBuilder(128);
    // sb.append("unhighlightGridCell \"").append(intMatrix.getName());
    // sb.append("[").append(row).append("][").append(col).append("]\" ");
    // addWithTiming(sb, offset, duration);
    // }

    @Override
    public void put(StringMatrix intMatrix, int row, int col, String what,
        Timing delay, Timing duration) {
      if (SUPPRESS_GRID_REFRESH) {
        StringBuilder sb = new StringBuilder(128);
        sb.append("setGridValue \"").append(intMatrix.getName()).append("[");
        sb.append(row).append("][").append(col).append("]\" \"");
        sb.append(what).append("\" ");
        addWithTiming(sb, delay, duration);
      } else {
        super.put(intMatrix, row, col, what, delay, duration);
      }
    }
  }

  private static class FixedAnimalStringArrayGenerator extends
      AnimalStringArrayGenerator {
    FixedAnimalStringArrayGenerator(Language aLang) {
      super(aLang);
    }

    @Override
    public void unhighlightCell(ArrayPrimitive ia, int from, int to,
        Timing offset, Timing duration) {
      createEntry(ia, "unhighlightArrayCell", from, to, offset, duration);
    }
  }

  public static void main(String[] args) {
    LongestCommonSubstringGenerator generator = new LongestCommonSubstringGenerator();

    AnimationPropertiesContainer props = new AnimationPropertiesContainer();

    TextProperties titleTP = new TextProperties();
    titleTP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    titleTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));
    titleTP.set(AnimationPropertiesKeys.NAME, "title");
    props.add(titleTP);

    RectProperties titleRP = new RectProperties();
    titleRP.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
    titleRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    titleRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    titleRP.set(AnimationPropertiesKeys.NAME, "titleRect");
    props.add(titleRP);

    TextProperties descriptionTP = new TextProperties();
    descriptionTP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    descriptionTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    descriptionTP.set(AnimationPropertiesKeys.NAME, "description");
    props.add(descriptionTP);

    TextProperties variablesTP = new TextProperties();
    variablesTP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    variablesTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    variablesTP.set(AnimationPropertiesKeys.NAME, "variables");
    props.add(variablesTP);

    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.blue);
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    scProps.set(AnimationPropertiesKeys.NAME, "sourceCode");
    props.add(scProps);

    ArrayProperties ap = new ArrayProperties();
    ap.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    ap.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    ap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.black);
    ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
    ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.yellow);
    ap.set(AnimationPropertiesKeys.NAME, "array");
    props.add(ap);

    ArrayMarkerProperties amp = new ArrayMarkerProperties();
    amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    amp.set(AnimationPropertiesKeys.NAME, "arrayMarker");
    props.add(amp);

    MatrixProperties mp = new MatrixProperties();
    mp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.black);
    mp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
    mp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.yellow);
    mp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    mp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    mp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
    mp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 12));
    mp.set(AnimationPropertiesKeys.NAME, "matrix");
    props.add(mp);

    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
    primitives.put("S", "ABABC");
    primitives.put("T", "BABA");
    // primitives.put("S", "AABBCCDDEEFF");
    // primitives.put("T", "BBAADDCCFFEE");
    primitives.put("includeQuestions", false);

    generator.init();
    String animalScript = generator.generate(props, primitives);

    System.out.println(animalScript);

    try {
      PrintWriter pw = new PrintWriter("lcs.asu");
      pw.println(animalScript);
      pw.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

}