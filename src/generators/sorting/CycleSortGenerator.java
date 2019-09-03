package generators.sorting;

import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.*;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.QuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

/**
 * Cycle Sort Generator
 * 
 * @author Pablo Hoch
 */
public class CycleSortGenerator implements Generator {

  private Language              lang;

  // Properties
  private int[]                 input;
  private TextProperties        titleTP;
  private ArrayMarkerProperties posMarkerProperties;
  private TextProperties        currentStepTP;
  private SourceCodeProperties  sourceCodeProperties;
  private TextProperties        descriptionTP;
  private ArrayMarkerProperties cycleStartMarkerProperties;
  private RectProperties        titleRP;
  private ArrayMarkerProperties iMarkerProperties;
  private TextProperties        variablesTP;
  private ArrayProperties       arrayProperties;

  // Current step descriptions
  private static final String   STEP_FIND_CYCLE                    = "Zykel suchen";
  private static final String   STEP_FIND_POSITION_FOR_ITEM        = "Korrekte Position (pos) für item suchen";
  private static final String   STEP_NO_CYCLE                      = "item bereits an korrekter Position (kein Zykel)";
  private static final String   STEP_MOVE_ITEM_TO_CORRECT_POSITION = "item an korrekte Position (pos) schreiben";
  private static final String   STEP_ROTATE_REST_OF_CYCLE          = "Rest des Zykels rotieren";

  // Questions
  private static final String   Q_CORRECT                          = "Richtig. ";
  private static final String   Q_WRONG                            = "Falsch. ";
  public static final String    Q_EX_MOVE_REQUIRED                 = "Das Element befindet sich nicht an der korrekten Position und muss daher verschoben werden. "
                                                                       + "Das sieht man daran, dass sich im Array rechts von dem Element noch kleinere Zahlen befinden. "
                                                                       + "Im Algorithmus gibt der pos-Zeiger die Position an, an die das Element verschoben werden muss.";
  public static final String    Q_EX_NO_MOVE_REQUIRED              = "Das Element befindet sich bereits an der korrekten Position, "
                                                                       + "da das Array links von dem Element schon sortiert ist und sich rechts des Elements keine kleineren "
                                                                       + "Zahlen befinden.";

  // Set to true to use the TrueFalseQuestionModel for true/false questions,
  // or to false to use the MultipleChoiceQuestionModel.
  // Currently, the feedback text isn't shown when using the
  // TrueFalseQuestionModel,
  // therefore I am using MultipleChoiceQuestionModel even for true/false
  // questions.
  private static final boolean  USE_TF_QUESTION_MODEL              = false;

  // Spacing between two lines of text
  public static final int       LINE_SPACING                       = 5;

  private final TicksTiming     markerMoveTiming                   = new TicksTiming(
                                                                       10);

  // AnimalScript variables
  private IntArray              intArray;
  private boolean               includeQuestions                   = true;
  private Text                  title;
  private Rect                  titleRect;
  private ArrayMarker           cycleStartMarker;
  private ArrayMarker           posMarker;
  private ArrayMarker           iMarker;
  private SourceCode            sc;
  private Variables             vars;
  private Text                  itemValue;
  private Text                  tempValue;
  private Text                  itemLabel;
  private Text                  tempLabel;
  private Group                 varGroup;
  private Text                  stepLabel;
  private Text                  currentStep;
  private TwoValueCounter       counter;
  private TwoValueView          counterView;
  private Polyline              moveCounterLine;

  private QuestionGroupModel    qGroupIsCycle;

  public void init() {
    lang = new AnimalScript("Cycle Sort [DE]", "Pablo Hoch", 820, 1000);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // Get input
    input = (int[]) primitives.get("intArray");
    includeQuestions = (Boolean) primitives.get("includeQuestions");

    // Get properties
    titleTP = (TextProperties) props.getPropertiesByName("title");
    currentStepTP = (TextProperties) props.getPropertiesByName("currentStep");
    descriptionTP = (TextProperties) props.getPropertiesByName("description");
    variablesTP = (TextProperties) props.getPropertiesByName("variables");
    cycleStartMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("cycleStartMarker");
    posMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("posMarker");
    iMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("iMarker");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    titleRP = (RectProperties) props.getPropertiesByName("titleRect");
    arrayProperties = (ArrayProperties) props.getPropertiesByName("array");

    // Fix font sizes (it's only possible to set the font name in the generator
    // gui...)
    patchFont(titleTP, Font.BOLD, 24);
    patchFont(currentStepTP, Font.PLAIN, 14);
    patchFont(descriptionTP, Font.PLAIN, 14);
    patchFont(variablesTP, Font.PLAIN, 14);
    patchFont(sourceCodeProperties, Font.PLAIN, 12);

    lang.setStepMode(true);

    generateIntro();
    generateSort();

    lang.finalizeGeneration();

    return lang.toString();
  }

  private void patchFont(AnimationProperties tp, int style, int size) {
    Font baseFont = (Font) tp.get(AnimationPropertiesKeys.FONT_PROPERTY);
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(baseFont.getName(),
        style, size));
  }

  /**
   * Generate the intro slide
   */
  private void generateIntro() {
    title = lang.newText(new Coordinates(20, 20), "Cycle Sort", "title", null,
        titleTP);

    titleRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    titleRect = lang.newRect(new Offset(-5, -5, "title", "NW"), new Offset(5,
        5, "title", "SE"), "titleRect", null, titleRP);

    generateMultiLineText(
        new Coordinates(20, 50),
        "pt",
        new String[] {
            "Cycle Sort ist ein in-place Sortieralgorithmus.",
            "Im Gegensatz zu den meisten Sortieralgorithmen kommt Cycle Sort mit einer minimalen Anzahl",
            "an Schreibzugriffen auf das zu sortierende Array aus. Jedes Element wird höchstens ein mal",
            "in das Array geschrieben, nämlich dann, wenn es sich nicht an der korrekten Position befindet.",
            "Der Algorithmus eignet sich daher für Anwendungsfälle, wo Schreibzugriffe auf das zu sortierende",
            "Array sehr teuer sind. Die Komplexität des Algorithmus beträgt O(n^2).",
            "",
            "Die Grundidee des Algorithmus ist, dass sich eine Permutation des Arrays in Zykel zerlegen lässt.",
            "Ein Zykel besteht aus einer Menge von Elementen, deren Position rotiert wurde.",
            "Beispielsweise lässt sich die Permutation des Arrays [1,4,2,3] durch den Zykel (2,3,4) beschreiben,",
            "d.h. das 2. Element des sortierten Arrays [1,2,3,4] wurde an Position 3 verschoben, Element 3 an",
            "Position 4 und Element 4 an Position 2. Der Algorithmus findet diese Zyklen und rotiert die Elemente",
            "so, dass am Ende ein sortiertes Array entsteht.",
            "",
            "Der Algorithmus läuft wie folgt ab:",
            "Es wird über das zu sortierende Array iteriert und zunächst für das aktuelle Element (item)",
            "die Position im sortierten Array bestimmt (pos). Befindet sich das Element bereits an der",
            "korrekten Position, wird direkt zum nächsten Element gesprungen. Andernfalls wird das aktuelle",
            "Element an die korrekte Position im Array geschrieben. Anschließend muss noch das nun überschriebene",
            "Element an seine korrekte Position im Array geschrieben werden. Dies wird solange fortgesetzt,",
            "bis alle Elemente dieses Zykels an ihrer korrekten Position sind. Dies ist der Fall, wenn das",
            "letzte Element des Zykels an die Position im Array geschrieben wurde, wo sich das erste Element",
            "des Zykels befand." });

    lang.nextStep("Einleitung");
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

  /**
   * Generate the slides for the algorithm
   */
  private void generateSort() {

    // Hide intro text
    lang.hideAllPrimitives();
    title.show();
    titleRect.show();

    // Generate primitives
    generateArray();
    generateCode();
    generateVariableDisplay();
    vars = lang.newVariables();
    generateCounter();

    if (includeQuestions) {
      generateQuestionGroups();
    }

    lang.nextStep("Initialisierung");

    // Generate slides for each step
    cycleSort();

    generateFinalSlide();

  }

  private void generateArray() {
    intArray = lang.newIntArray(new Coordinates(50, 170), input, "array", null,
        arrayProperties);
  }

  private void generateCode() {
    sc = lang.newSourceCode(new Coordinates(20, 240), "code", null,
        sourceCodeProperties);

    sc.addCodeLine("void cycleSort(int[] array) {", null, 0, null); // 0
    sc.addCodeLine(
        "for (int cycleStart = 0; cycleStart < array.length-1; cycleStart++) {",
        null, 1, null); // 1
    sc.addCodeLine("int item = array[cycleStart];", null, 2, null); // 2
    sc.addCodeLine("int pos = cycleStart;", null, 2, null); // 3
    sc.addCodeLine("for (int i = cycleStart + 1; i < array.length; i++) {",
        null, 2, null); // 4
    sc.addCodeLine("if (array[i] < item)", null, 3, null); // 5
    sc.addCodeLine("pos++;", null, 4, null); // 6
    sc.addCodeLine("}", null, 2, null); // 7
    sc.addCodeLine("if (pos == cycleStart)", null, 2, null); // 8
    sc.addCodeLine("continue;", null, 3, null); // 9
    sc.addCodeLine("while (array[pos] == item)", null, 2, null); // 10
    sc.addCodeLine("pos++;", null, 3, null); // 11
    sc.addCodeLine("int temp = array[pos];", null, 2, null); // 12
    sc.addCodeLine("array[pos] = item;", null, 2, null); // 13
    sc.addCodeLine("item = temp;", null, 2, null); // 14

    sc.addCodeLine("while (pos != cycleStart) {", null, 2, null); // 15
    sc.addCodeLine("pos = cycleStart;", null, 3, null); // 16
    sc.addCodeLine("for (int i = cycleStart + 1; i < array.length; i++) {",
        null, 3, null); // 17
    sc.addCodeLine("if (array[i] < item)", null, 4, null); // 18
    sc.addCodeLine("pos++;", null, 5, null); // 19
    sc.addCodeLine("}", null, 3, null); // 20
    sc.addCodeLine("while (array[pos] == item)", null, 3, null); // 21
    sc.addCodeLine("pos++;", null, 4, null); // 22
    sc.addCodeLine("temp = array[pos];", null, 3, null); // 23
    sc.addCodeLine("array[pos] = item;", null, 3, null); // 24
    sc.addCodeLine("item = temp;", null, 3, null); // 25
    sc.addCodeLine("}", null, 2, null); // 26
    sc.addCodeLine("}", null, 1, null); // 27
    sc.addCodeLine("}", null, 0, null); // 28

  }

  private void generateArrayMarkers() {
    cycleStartMarker = generateArrayMarker("cycleStart", 0,
        AnimationPropertiesKeys.LONG_MARKER_PROPERTY, true, 3,
        cycleStartMarkerProperties);
    posMarker = generateArrayMarker("pos", 0, null, false, 2,
        posMarkerProperties);
    iMarker = generateArrayMarker("i", 0,
        AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, false, 1,
        iMarkerProperties);
  }

  private ArrayMarker generateArrayMarker(String label, int index,
      String length, boolean visible, int depth, ArrayMarkerProperties amp) {
    amp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, depth);
    if (length != null)
      amp.set(length, true);
    if (!visible)
      amp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    String name = label + "Marker";
    ArrayMarker marker = lang.newArrayMarker(intArray, index, name, null, amp);
    return marker;
  }

  private void generateVariableDisplay() {
    TextProperties plainTP = variablesTP;

    TextProperties boldTP = new TextProperties();
    boldTP.set(AnimationPropertiesKeys.FONT_PROPERTY,
        plainTP.get(AnimationPropertiesKeys.FONT_PROPERTY));
    boldTP.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        plainTP.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    patchFont(boldTP, Font.BOLD, 14);

    Hidden hidden = new Hidden();

    itemLabel = lang.newText(new Offset(40, -40, intArray, "NE"), "item = ",
        "itemLabel", hidden, plainTP);
    itemValue = lang.newText(new Offset(0, 0, itemLabel, "NE"), "undefined",
        "itemValue", hidden, boldTP);

    tempLabel = lang.newText(new Offset(0, LINE_SPACING, itemLabel, "SW"),
        "temp = ", "tempLabel", hidden, plainTP);
    tempValue = lang.newText(new Offset(0, 0, tempLabel, "NE"), "undefined",
        "tempValue", hidden, boldTP);

    varGroup = lang.newGroup(
        new LinkedList<Primitive>(Arrays.asList(itemLabel, itemValue,
            tempLabel, tempValue)), "varGroup");
  }

  private void generateCounter() {
    counter = lang.newCounter(intArray);

    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.blue);
    cp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));

    counterView = lang.newCounterView(counter, new Offset(170, -42, intArray,
        "NE"), cp, true, false, new String[] { "Schreibzugriffe:",
        "Lesezugriffe:" });

    // must create the line here, because on the final slide the offset from the
    // intArray
    // results in other coordinates...
    // this is used on the final slide.
    moveCounterLine = lang.newPolyline(new Node[] {
        // cant use Offset to counterView, because it's not a primitive
        new Offset(170, -42, intArray, "NE"),
        // new Offset(0, 50, "ft1", "SW")
        new Coordinates(20, 300) }, "moveCounterLine", new Hidden());
  }

  private void generateStepDescription() {
    stepLabel = lang.newText(new Coordinates(20, 210), "Aktueller Schritt: ",
        "stepLabel", null, currentStepTP);
    currentStep = lang.newText(new Offset(0, 0, stepLabel, "NE"),
        STEP_FIND_CYCLE, "currentStep", null, currentStepTP);
  }

  private void generateFinalSlide() {

    sc.hide();
    varGroup.hide();
    stepLabel.hide();
    currentStep.hide();

    generateMultiLineText(new Coordinates(20, 250), "ft",
        new String[] { "Das Array wurde sortiert.", });

    counterView.moveVia("NW", moveCounterLine, new TicksTiming(30),
        new TicksTiming(30));

    lang.nextStep("Ende");
  }

  private void generateQuestionGroups() {
    // according to the documentation the numberOfRepeats parameter should
    // limit the number of times a question of this group is displayed
    // (should only be displayed until the user has answered it correctly
    // numberOfRepeat times)
    // but it seems that the questions are always displayed, regardless of this
    // number :(
    qGroupIsCycle = new QuestionGroupModel("qGroupIsCycle", 3);
    lang.addQuestionGroup(qGroupIsCycle);
  }

  private QuestionModel addTFQuestion(String id, String prompt,
      boolean correctAnswer, String trueAnswer, String falseAnswer,
      String explanation, QuestionGroupModel group) {

    // see comment at the beginning of the file (USE_TF_QUESTION_MODEL
    // definition)
    // for why i am using the MC model

    if (USE_TF_QUESTION_MODEL) {
      TrueFalseQuestionModel question = new TrueFalseQuestionModel(id,
          correctAnswer, 1);
      question.setPrompt(prompt);
      if (correctAnswer) {
        // this (currently) doesn't work. the feedback string is written to the
        // intDef... file, but it's not displayed in the question popup window.
        question.setFeedbackForAnswer(true, Q_CORRECT + explanation);
        question.setFeedbackForAnswer(false, Q_WRONG + explanation);
      } else {
        question.setFeedbackForAnswer(true, Q_WRONG + explanation);
        question.setFeedbackForAnswer(false, Q_CORRECT + explanation);
      }
      question.setGroupID(group.getID());
      lang.addTFQuestion(question);
      return question;
    } else {
      MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel(id);
      question.setPrompt(prompt);
      if (correctAnswer) {
        question.addAnswer("aTrue", trueAnswer, 1, Q_CORRECT + explanation);
        question.addAnswer("bFalse", falseAnswer, 0, Q_WRONG + explanation);
      } else {
        question.addAnswer("aTrue", trueAnswer, 0, Q_WRONG + explanation);
        question.addAnswer("bFalse", falseAnswer, 1, Q_CORRECT + explanation);
      }
      question.setGroupID(group.getID());
      lang.addMCQuestion(question);
      return question;
    }

  }

  /**
   * Actual cycle sort algorithm, includes generation of AnimalScript
   * 
   * @return
   */
  private int cycleSort() {
    int writes = 0;

    generateArrayMarkers();
    generateStepDescription();

    vars.declare("int", "item");
    vars.declare("int", "temp");

    // Find cycles to rotate
    for (int cycleStart = 0; cycleStart < intArray.getLength() - 1; cycleStart++) {
      sc.highlight(1);
      posMarker.hide();
      currentStep.setText(STEP_FIND_CYCLE, null, null);
      cycleStartMarker.move(cycleStart, null, markerMoveTiming);
      lang.nextStep((cycleStart + 1)
          + ". Iteration - Auflösen des Zykels beginnend bei Position "
          + cycleStart);

      int item = intArray.getData(cycleStart); // array[cycleStart];
      sc.toggleHighlight(1, 2);
      sc.highlight(1, 0, true);
      vars.set("item", Integer.toString(item));
      itemValue.setText(Integer.toString(item), null, null);

      if (cycleStart == 0) {
        itemLabel.show();
        itemValue.show();
      }

      lang.nextStep();

      int pos = cycleStart;
      posMarker.move(pos, null, null);
      posMarker.show();
      sc.toggleHighlight(2, 3);
      currentStep.setText(STEP_FIND_POSITION_FOR_ITEM, null, null);
      lang.nextStep();

      iMarker.show();
      sc.unhighlight(3);

      // Find new position for the item
      for (int i = cycleStart + 1; i < intArray.getLength(); i++) { // 4
        iMarker.move(i, null, (i == cycleStart + 1) ? null : markerMoveTiming);
        sc.highlight(4);
        lang.nextStep();

        sc.toggleHighlight(4, 5);
        sc.highlight(4, 0, true);
        // intArray.highlightElem(cycleStart, null, null);
        itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            Color.red, null, null);
        intArray.highlightElem(i, null, null);
        lang.nextStep();
        sc.unhighlight(5);
        // intArray.unhighlightElem(cycleStart, null, null);
        itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            Color.black, null, null);
        intArray.unhighlightElem(i, null, null);

        if (intArray.getData(i) < item) {
          pos++;

          sc.highlight(6);
          posMarker.move(pos, null, markerMoveTiming);
          lang.nextStep();
          sc.unhighlight(6);
        }

      }

      sc.highlight(7);
      lang.nextStep();

      sc.toggleHighlight(7, 8);
      sc.unhighlight(4);
      iMarker.hide();
      if (includeQuestions) {
        boolean mustMoveElement = pos != cycleStart;

        addTFQuestion("qIsCycle" + cycleStart, "Muss das Element item = "
            + item + " (Position cycleStart = " + cycleStart
            + " im Array) an eine andere Position im Array verschoben werden?",
            mustMoveElement, "Ja, das Element muss verschoben werden.",
            "Nein, das Element befindet sich schon an der korrekten Position.",
            mustMoveElement ? Q_EX_MOVE_REQUIRED : Q_EX_NO_MOVE_REQUIRED,
            qGroupIsCycle);

      }
      lang.nextStep();

      // If item already is there, it's not a cycle
      if (pos == cycleStart) {
        sc.toggleHighlight(8, 9);
        currentStep.setText(STEP_NO_CYCLE, null, null);
        lang.nextStep();
        sc.unhighlight(9);
        continue;
      }

      sc.toggleHighlight(8, 10);
      currentStep.setText(STEP_MOVE_ITEM_TO_CORRECT_POSITION, null, null);
      // intArray.highlightElem(cycleStart, null, null);
      itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red,
          null, null);
      intArray.highlightElem(pos, null, null);
      lang.nextStep();
      intArray.unhighlightElem(pos, null, null);

      // Move item (skip duplicates)
      while (intArray.getData(pos) == item) {
        // intArray.unhighlightElem(cycleStart, null, null);
        itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            Color.black, null, null);

        pos++;

        sc.toggleHighlight(10, 11);
        posMarker.move(pos, null, markerMoveTiming);
        lang.nextStep();

        sc.toggleHighlight(11, 10);
        // intArray.highlightElem(cycleStart, null, null);
        itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            Color.red, null, null);
        intArray.highlightElem(pos, null, null);
        lang.nextStep();
        intArray.unhighlightElem(pos, null, null);
      }

      // intArray.unhighlightElem(cycleStart, null, null);
      itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.black, null, null);

      // +++ WRITE +++

      int temp = intArray.getData(pos);
      vars.set("temp", Integer.toString(temp));
      tempValue.setText(Integer.toString(temp), null, null);
      tempLabel.show();
      tempValue.show();
      sc.toggleHighlight(10, 12);
      intArray.highlightCell(pos, null, null);
      lang.nextStep();

      // array[pos] = item;
      sc.toggleHighlight(12, 13);
      intArray.put(pos, item, new TicksTiming(20), new TicksTiming(20));
      lang.nextStep();
      intArray.unhighlightCell(pos, null, null);

      item = temp;
      vars.set("item", Integer.toString(item));
      itemValue.setText(Integer.toString(item), null, null);
      sc.toggleHighlight(13, 14);
      lang.nextStep();

      writes++;

      sc.toggleHighlight(14, 15);
      tempLabel.hide();
      tempValue.hide();
      currentStep.setText(STEP_ROTATE_REST_OF_CYCLE, null, null);
      lang.nextStep();

      // Rotate rest of the cycle
      while (pos != cycleStart) {
        // Find new position for the item
        pos = cycleStart;

        sc.toggleHighlight(15, 16);
        sc.highlight(15, 0, true);
        currentStep.setText(STEP_FIND_POSITION_FOR_ITEM, null, null);
        posMarker.move(pos, null, markerMoveTiming);
        lang.nextStep();

        // sc.toggleHighlight(16, 17);
        // iMarker.move(cycleStart + 1, null, markerMoveTiming);
        iMarker.show();
        // lang.nextStep();
        sc.unhighlight(16);

        for (int i = cycleStart + 1; i < intArray.getLength(); i++) { // 17
          iMarker
              .move(i, null, (i == cycleStart + 1) ? null : markerMoveTiming);
          sc.highlight(17);
          lang.nextStep();

          sc.toggleHighlight(17, 18);
          sc.highlight(17, 0, true);

          itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
              Color.red, null, null);
          intArray.highlightElem(i, null, null);
          lang.nextStep();
          sc.unhighlight(18);
          itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
              Color.black, null, null);
          intArray.unhighlightElem(i, null, null);

          if (intArray.getData(i) < item) {
            pos++;

            sc.highlight(19);
            posMarker.move(pos, new TicksTiming(10), markerMoveTiming);
            lang.nextStep();
            sc.unhighlight(19);
          }

          // sc.highlight(17);
          // if (i + 1 < array.length)
          // iMarker.move(i + 1, null, markerMoveTiming);
          // else
          // iMarker.moveOutside(null, markerMoveTiming);
          // lang.nextStep();
        }

        // sc.toggleHighlight(17, 20);
        sc.highlight(20);
        sc.highlight(17, 0, true);
        lang.nextStep();

        sc.toggleHighlight(20, 21);
        sc.unhighlight(17);
        currentStep.setText(STEP_MOVE_ITEM_TO_CORRECT_POSITION, null, null);
        // intArray.highlightElem(cycleStart, null, null);
        itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            Color.red, null, null);
        intArray.highlightElem(pos, null, null);
        iMarker.hide();
        lang.nextStep();
        intArray.unhighlightElem(pos, null, null);

        // Move item (skip duplicates)
        while (intArray.getData(pos) == item) {
          // intArray.unhighlightElem(cycleStart, null, null);
          itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
              Color.black, null, null);
          pos++;

          sc.toggleHighlight(21, 22);
          posMarker.move(pos, new TicksTiming(10), markerMoveTiming);
          lang.nextStep();

          sc.toggleHighlight(22, 21);
          // intArray.highlightElem(cycleStart, null, null);
          itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
              Color.red, null, null);
          intArray.highlightElem(pos, null, null);
          lang.nextStep();
          intArray.unhighlightElem(pos, null, null);
        }

        // intArray.unhighlightElem(cycleStart, null, null);
        itemValue.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            Color.black, null, null);

        // +++ WRITE +++

        temp = intArray.getData(pos);
        vars.set("temp", Integer.toString(temp));
        tempValue.setText(Integer.toString(temp), null, null);
        tempLabel.show();
        tempValue.show();
        sc.toggleHighlight(21, 23);
        intArray.highlightCell(pos, null, null);
        lang.nextStep();

        // array[pos] = item;
        sc.toggleHighlight(23, 24);
        intArray.put(pos, item, new TicksTiming(20), new TicksTiming(20));
        lang.nextStep();
        intArray.unhighlightCell(pos, null, null);

        item = temp;
        vars.set("item", Integer.toString(item));
        itemValue.setText(Integer.toString(item), null, null);
        sc.toggleHighlight(24, 25);
        lang.nextStep();

        writes++;

        sc.toggleHighlight(25, 15);
        tempLabel.hide();
        tempValue.hide();
        currentStep.setText(STEP_ROTATE_REST_OF_CYCLE, null, null);
        lang.nextStep();
      }

      sc.toggleHighlight(15, 26);
      sc.highlight(15, 0, true);
      lang.nextStep();
      sc.unhighlight(15);
      sc.unhighlight(26);

    }

    sc.highlight(27);
    posMarker.hide();
    lang.nextStep();

    cycleStartMarker.hide();
    sc.unhighlight(1);
    sc.toggleHighlight(27, 28);
    lang.nextStep();

    return writes;
  }

  public String getName() {
    return "Cycle Sort [DE]";
  }

  public String getAlgorithmName() {
    return "Cycle Sort";
  }

  public String getAnimationAuthor() {
    return "Pablo Hoch";
  }

  public String getDescription() {
    return "Cycle Sort ist ein in-place Sortieralgorithmus."
        + "\n"
        + "Im Gegensatz zu den meisten Sortieralgorithmen kommt Cycle Sort mit einer minimalen Anzahl"
        + "\n"
        + "an Schreibzugriffen auf das zu sortierende Array aus. Jedes Element wird h&ouml;chstens ein mal"
        + "\n"
        + "in das Array geschrieben, n&auml;mlich dann, wenn es sich nicht an der korrekten Position befindet."
        + "\n"
        + "Der Algorithmus eignet sich daher für Anwendungsf&auml;lle, wo Schreibzugriffe auf das zu sortierende"
        + "\n"
        + "Array sehr teuer sind.  Die Komplexität des Algorithmus beträgt O(n<sup>2</sup>)."
        + "\n"
        + "<p>\n"
        + "Die Grundidee des Algorithmus ist, dass sich eine Permutation des Arrays in Zykel zerlegen lässt."
        + "\n"
        + "Ein Zykel besteht aus einer Menge von Elementen, deren Position rotiert wurde."
        + "\n"
        + "Beispielsweise lässt sich die Permutation des Arrays [1,4,2,3] durch den Zykel (2,3,4) beschreiben,"
        + "\n"
        + "d.h. das 2. Element des sortierten Arrays [1,2,3,4] wurde an Position 3 verschoben, Element 3 an"
        + "\n"
        + "Position 4 und Element 4 an Position 2. Der Algorithmus findet diese Zyklen und rotiert die Elemente"
        + "\n" + "so, dass am Ende ein sortiertes Array entsteht." + "\n";
  }

  public String getCodeExample() {
    return "void cycleSort(int[] array) {"
        + "\n"
        + "    for (int cycleStart = 0; cycleStart &lt; array.length-1; cycleStart++) {"
        + "\n"
        + "        int item = array[cycleStart];"
        + "\n"
        + "        int pos = cycleStart;"
        + "\n"
        + "        for (int i = cycleStart + 1; i &lt; array.length; i++) {"
        + "\n"
        + "            if (array[i] &lt; item)"
        + "\n"
        + "                pos++;"
        + "\n"
        + "        }"
        + "\n"
        + "        if (pos == cycleStart)"
        + "\n"
        + "            continue;"
        + "\n"
        + "        while (array[pos] == item)"
        + "\n"
        + "            pos++;"
        + "\n"
        + "        int temp = array[pos];"
        + "\n"
        + "        array[pos] = item;"
        + "\n"
        + "        item = temp;"
        + "\n"
        + "        while (pos != cycleStart) {"
        + "\n"
        + "            pos = cycleStart;"
        + "\n"
        + "            for (int i = cycleStart + 1; i &lt; array.length; i++) {"
        + "\n" + "                if (array[i] &lt; item)" + "\n"
        + "                    pos++;" + "\n" + "            }" + "\n"
        + "            while (array[pos] == item)" + "\n"
        + "                pos++;" + "\n" + "            temp = array[pos];"
        + "\n" + "            array[pos] = item;" + "\n"
        + "            item = temp;" + "\n" + "        }" + "\n" + "    }"
        + "\n" + "}" + "\n";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}