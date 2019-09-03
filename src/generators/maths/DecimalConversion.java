package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class DecimalConversion implements Generator, ValidatingGenerator {
  private Language        lang;
  private GeneratorValues input;

  /**
   * Holds all input given by the user
   */
  class GeneratorValues {
    int                   baseValue     = 2;
    int                   decimalValue  = 42;
    boolean               showQuestions = false;

    SourceCodeProperties  sourceCodeLayout;
    ArrayMarkerProperties positionMarkerLayout;
    TextProperties        calculationLayout;
    ArrayProperties       resultLayout;
    RectProperties        rectLayout;
  }

  /**
   * Initializing Constants
   */
  private final String LANG_TITLE  = "Decimal Conversion";
  private final String LANG_AUTHOR = "Max Kolhagen, Patrick Lowin";
  private final int    LANG_WIDTH  = 640;
  private final int    LANG_HEIGHT = 480;

  public void init() {
    // initialize random
    this.random = new Random();

    // create animal script
    this.lang = new AnimalScript(LANG_TITLE, LANG_AUTHOR, LANG_WIDTH,
        LANG_HEIGHT);

    // enable questions
    this.lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    // set step mode to manual
    this.lang.setStepMode(true);
  }

  /**
   * validates the user input
   */
  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    // get primitives
    int base = (Integer) primitives.get("base");
    int decimal = (Integer) primitives.get("decimal value");

    // check if base is between 2 and 16 (inclusive)
    if (base <= 1 || base > 16)
      throw new IllegalArgumentException(
          "base must be greater 1 and less than 16!");

    // check if decimal is positive and not zero
    if (decimal < 1)
      throw new IllegalArgumentException("decimal must be greater 0!");

    // check if decimal is ridiculous
    if (decimal > 8192)
      throw new IllegalArgumentException("don't get ridiculous :)");

    // everything's fine
    return true;
  }

  /**
   * get user input
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    this.input = new GeneratorValues();

    // get primitives
    this.input.baseValue = (Integer) primitives.get("base");
    this.input.decimalValue = (Integer) primitives.get("decimal value");
    this.input.showQuestions = (Boolean) primitives.get("show questions");

    // get properties
    this.input.sourceCodeLayout = (SourceCodeProperties) props
        .getPropertiesByName("source code layout");
    this.input.positionMarkerLayout = (ArrayMarkerProperties) props
        .getPropertiesByName("position marker layout");
    this.input.calculationLayout = (TextProperties) props
        .getPropertiesByName("calculation layout");
    this.input.resultLayout = (ArrayProperties) props
        .getPropertiesByName("result layout");
    this.input.rectLayout = (RectProperties) props
        .getPropertiesByName("header rect layout");

    // start animation by preparation
    this.prepare(this.input.baseValue, this.input.decimalValue);

    // finalize (neccessary for questions)
    this.lang.finalizeGeneration();

    // return animal script code
    return lang.toString();
  }

  /**
   * get heading of algorithm
   */
  public String getName() {
    return "Decimal To Any Base";
  }

  /**
   * get name of algorithm
   */
  public String getAlgorithmName() {
    return "Decimal To Any Base Conversion";
  }

  /**
   * get names of authors
   */
  public String getAnimationAuthor() {
    return "Max Kolhagen, Patrick Lowin";
  }

  /**
   * get algorithm description
   */
  public String getDescription() {
    return "This algorithm converts any <b>positive decimal</b> to <b>any other base</b> (<i>from 1 to 16</i>).<br/>"
        + "For example converts 42<sub><small>10</small></sub> to 101010<sub><small>2</small></sub>:<br/><br/>"
        + "<table><tr><th>Numerator</th><th></th><th>Base</th><th></th><th>Result</th><th>Remainder<br/><small>(LSB-MSB)</small></th></tr>"
        + "<tr align='center'><td>42</td><td>/</td><td>2</td><td>=</td><td>21</td><td>0</td></tr>"
        + "<tr align='center'><td>21</td><td>/</td><td>2</td><td>=</td><td>10</td><td>1</td></tr>"
        + "<tr align='center'><td>10</td><td>/</td><td>2</td><td>=</td><td>5</td><td>0</td></tr>"
        + "<tr align='center'><td>5</td><td>/</td><td>2</td><td>=</td><td>2</td><td>1</td></tr>"
        + "<tr align='center'><td>2</td><td>/</td><td>2</td><td>=</td><td>1</td><td>0</td></tr>"
        + "<tr align='center'><td>1</td><td>/</td><td>2</td><td>=</td><td>0</td><td>1</td></tr></table><br/><br/>";
  }

  /**
   * get code example for algorithm
   */
  public String getCodeExample() {
    return "<b>public String</b> toBase( int base, int decimal )\n" + "{\n"
        + "    char[] characters = { '0', '1', '2', '3', \n"
        + "                          '4', '5', '6', '7', \n"
        + "                          '8', '9','A', 'B', \n"
        + "                          'C', 'D', 'E', 'F' };\n" + "\n"
        + "    String result = '';\n" + "\n" + "    int numerator = decimal;\n"
        + "\n" + "    while ( numerator != 0 )\n" + "    {\n"
        + "        int index = numerator % base;\n"
        + "        result = characters[index] + result;\n"
        + "        numerator = (numerator / base);\n" + "    }\n" + "\n"
        + "    return result;\n" + "}\n";
  }

  /**
   * get file extension
   */
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /**
   * get language of algorithm
   */
  public Locale getContentLocale() {
    return Locale.US;
  }

  /**
   * get categorie of algorithm
   */
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  /**
   * get coding language
   */
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  /**
   * animation elements
   */
  private SourceCode      sourceCode              = null;
  private StringArray     stringArray             = null;
  private ArrayMarker     arrayMarker             = null;

  private Text            lastCalculationText     = null;
  private int             currentCalculationIndex = 1;
  private ArrayList<Text> calculationGroup        = new ArrayList<Text>();

  /**
   * this method prepares the animation by creating the needed labels and
   * structures
   */
  private void prepare(int base, int decimal) {
    // define h1 text layout
    TextProperties h1 = new TextProperties();
    h1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));

    // define h2 text layout
    TextProperties h2 = new TextProperties();
    h2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));

    // show animation title in h1
    Text txTitle = this.lang.newText(new Coordinates(40, 40),
        "Decimal Conversion", "txTitle", null, h1);

    // surrounding for title
    this.lang.newRect(new Offset(-5, -5, txTitle, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, txTitle, AnimalScript.DIRECTION_SE), "txTitleRect",
        null, this.input.rectLayout);

    // show small introduction
    ArrayList<Text> introduction = new ArrayList<Text>();

    introduction
        .add(this.lang
            .newText(
                new Offset(0, 100, txTitle, AnimalScript.DIRECTION_NW),
                "This algorithm converts any positive decimal to any other base (from 1 to 16).",
                "txIntro01", null, h2));
    introduction.add(this.lang.newText(new Offset(0, 150, txTitle,
        AnimalScript.DIRECTION_NW),
        "For example converts 42 (Decimal-System) to 101010 (Binary-System).",
        "txIntro02", null, h2));
    introduction.add(this.lang.newText(new Offset(0, 190, txTitle,
        AnimalScript.DIRECTION_NW), "click next to get started...",
        "txIntro03", null));

    this.lang.nextStep("Introduction");

    // hide introduction
    for (Text introText : introduction)
      introText.hide();

    // prepare question
    int answer = (int) Math.ceil(Math.log(decimal) / Math.log(2));

    MultipleChoiceQuestionModel quest01 = new MultipleChoiceQuestionModel(
        "mcQuest01");
    quest01.setPrompt("How many bits are neccessary to express " + decimal
        + " in binary?");
    quest01.addAnswer("" + (answer - 1 - this.random.nextInt(2)), 0, "Nup! :(");
    quest01.addAnswer("" + (answer + 1 + this.random.nextInt(2)), 0,
        "No, not really.");
    quest01.addAnswer("" + answer, 10, "Yep that's right :)");
    quest01.setGroupID("Initial Questions");

    // check if question is shown
    if (this.askQuestion())
      this.lang.addMCQuestion(quest01);

    // draw headings (Input, Result, Source, Calculation)
    this.lang.newText(new Coordinates(40, 90), "Input:", "txInput", null, h2);
    this.lang
        .newText(new Coordinates(400, 90), "Result:", "txResult", null, h2);
    this.lang
        .newText(new Coordinates(40, 140), "Source:", "txSource", null, h2);

    // draw calculation header (and add to calculation group)
    Text calculationHeader = this.lang.newText(new Coordinates(400, 140),
        "Calculation:", "txCalculation", null, h2);
    this.calculationGroup.add(calculationHeader);

    // show input
    this.lang.newText(new Coordinates(40, 110), "base = " + base
        + ", decimal = " + decimal, "txInputValues", null);

    // create empty (pseudo) calculation text as an anchor
    this.lastCalculationText = this.lang.newText(new Coordinates(400, 145), "",
        "calc0", null, this.input.calculationLayout);
    this.calculationGroup.add(this.lastCalculationText);

    // create source code
    this.sourceCode = this.lang.newSourceCode(new Coordinates(40, 160),
        "sourceCode", null, this.input.sourceCodeLayout);

    this.sourceCode.addCodeLine(
        "public String toBase( int base, int decimal )", null, 0, null);
    this.sourceCode.addCodeLine("{", null, 0, null);
    this.sourceCode.addCodeLine("char[] characters = { '0', '1', '2', '3', ",
        null, 2, null);
    this.sourceCode.addCodeLine("'4', '5', '6', '7', ", null, 15, null);
    this.sourceCode.addCodeLine("'8', '9','A', 'B', ", null, 15, null);
    this.sourceCode.addCodeLine("'C', 'D', 'E', 'F' };", null, 15, null);
    this.sourceCode.addCodeLine("", null, 0, null);
    this.sourceCode.addCodeLine("String result = '';", null, 2, null);
    this.sourceCode.addCodeLine("", null, 0, null);
    this.sourceCode.addCodeLine("int numerator = decimal;", null, 2, null);
    this.sourceCode.addCodeLine("", null, 0, null);
    this.sourceCode.addCodeLine("while ( numerator != 0 )", null, 2, null);
    this.sourceCode.addCodeLine("{", null, 2, null);
    this.sourceCode.addCodeLine("int index = numerator % base;", null, 4, null);
    this.sourceCode.addCodeLine("result = characters[index] + result;", null,
        4, null);
    this.sourceCode.addCodeLine("numerator = (numerator / base);", null, 4,
        null);
    this.sourceCode.addCodeLine("}", null, 2, null);
    this.sourceCode.addCodeLine("", null, 0, null);
    this.sourceCode.addCodeLine("return result;", null, 2, null);
    this.sourceCode.addCodeLine("}", null, 0, null);

    // create result array
    int length = (int) Math.ceil(Math.log(decimal) / Math.log(base));

    String[] s = new String[length];
    for (int i = 0; i < length; i++)
      s[i] = "X";

    this.stringArray = this.lang.newStringArray(new Coordinates(500, 110), s,
        "stringArray", null, this.input.resultLayout);

    // create array marker
    this.arrayMarker = this.lang.newArrayMarker(this.stringArray, length - 1,
        "arrayMarker", null, this.input.positionMarkerLayout);

    this.lang.nextStep("Initialization");

    // start algorithm
    String result = this.toBase(base, decimal, length - 1);

    // show summary
    this.showSummary(base, decimal, result);
  }

  /**
   * this method performs the algorithm itself adding all extra animal script
   * commands
   */
  private String toBase(int base, int decimal, int current) {
    // show variables window
    Variables variables = this.lang.newVariables();

    // ALGORITHM: define char array for result
    char[] characters = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F' };

    // ALGORITHM: define result as empty string
    String result = "";

    this.sourceCode.highlight(7);

    this.lang.nextStep();

    // declare global variable result (is not hidden on openContext)
    variables.declare("String", "result", result);
    variables.setGlobal("result");

    this.lang.nextStep();

    // ALGORITHM: set numerator to decimal
    int numerator = decimal;

    this.sourceCode.unhighlight(7);
    this.sourceCode.highlight(9);

    this.lang.nextStep();

    // declare global variable numerator
    variables.declare("int", "numerator", "" + numerator);
    variables.setGlobal("numerator");

    // currentCalculation holds the current string printed in the
    // calculation area
    String currentCalculation = "Numerator: " + decimal;

    // add line to calculation area (+ increase currentCalculationIndex)
    this.lastCalculationText = this.lang.newText(new Offset(0, 15,
        this.lastCalculationText, AnimalScript.DIRECTION_NW),
        currentCalculation, "calc" + this.currentCalculationIndex++, null);

    // add to calculation group
    this.calculationGroup.add(this.lastCalculationText);

    this.lang.nextStep();

    this.sourceCode.unhighlight(9);
    this.sourceCode.highlight(11);

    this.lang.nextStep("Calculation #" + (this.currentCalculationIndex - 1));

    // ALGORITHM: check if numerator is greater than 0
    while (numerator != 0) {
      // ALGORITHM: get remainder of numerator / base
      int index = numerator % base;

      // ALGORITHM: get result character for the current position
      result = characters[index] + result;

      // cache old numerator
      int previousNumerator = numerator;

      // ALGORITHM: determine the new numerator
      numerator = (numerator / base);

      // open context (for variable index only being shown in while)
      variables.openContext();

      // prepare question
      FillInBlanksQuestionModel quest02 = new FillInBlanksQuestionModel(
          "fibQuest" + UUID.randomUUID());
      quest02.setPrompt("What is going to be the next entry?");
      quest02.addAnswer("" + characters[index], 5, "Yeah!");
      quest02.addAnswer(("" + characters[index]).toLowerCase(), 5, "Yeah!");
      quest02.setGroupID("Calculation Questions");

      // check if question is shown (not shown in first iteration)
      if (numerator != decimal && this.askQuestion())
        this.lang.addFIBQuestion(quest02);

      // add sth to current calculation string
      currentCalculation += " is not 0!";
      this.lastCalculationText.setText(currentCalculation, null, null);

      // if not first iteration, decrement array marker
      int current2 = current;
      if (this.arrayMarker.getPosition() != current2)
        this.arrayMarker.decrement(null, null);

      this.lang.nextStep();

      this.sourceCode.unhighlight(11);
      this.sourceCode.highlight(13);

      this.lang.nextStep();

      // add a new line to the calculation (+ increase
      // currentCalculationIndex)
      currentCalculation = "";
      this.lastCalculationText = this.lang.newText(new Offset(0, 15,
          this.lastCalculationText, AnimalScript.DIRECTION_NW),
          currentCalculation, "calc" + this.currentCalculationIndex++, null);

      // add to calculation group
      this.calculationGroup.add(this.lastCalculationText);

      // declare variable index (in current context)
      variables.declare("int", "index", "" + index);

      // add sth to current calculation string
      currentCalculation += "Index: " + numerator + " % " + base + " = "
          + index;
      this.lastCalculationText.setText(currentCalculation, null, null);

      this.lang.nextStep();

      this.sourceCode.unhighlight(13);
      this.sourceCode.highlight(14);

      // highlight the current position in the result array
      this.stringArray.highlightCell(current2, null, null);

      this.sourceCode.highlight(2);
      this.sourceCode.highlight(3);
      this.sourceCode.highlight(4);
      this.sourceCode.highlight(5);

      this.lang.nextStep();

      // set new value to variable result
      variables.set("result", result);

      // set new character to the current position in the result array
      this.stringArray.put(current2, "" + characters[index], null, null);

      this.lang.nextStep();

      // unhighlight the current cell
      this.stringArray.unhighlightCell(current2, null, null);

      this.sourceCode.unhighlight(2);
      this.sourceCode.unhighlight(3);
      this.sourceCode.unhighlight(4);
      this.sourceCode.unhighlight(5);

      this.sourceCode.unhighlight(14);
      this.sourceCode.highlight(15);

      // decrease current position in result array
      current2--;

      this.lang.nextStep();

      // set new value to variable numerator
      variables.set("numerator", "" + numerator);

      // add sth to current calculation string
      currentCalculation += ", Numerator: " + previousNumerator + " / " + base
          + " = " + numerator;
      this.lastCalculationText.setText(currentCalculation, null, null);

      this.lang.nextStep();

      this.sourceCode.unhighlight(15);
      this.sourceCode.highlight(11);

      // close the current context
      variables.closeContext();

      this.lang.nextStep("Calculation #" + (this.currentCalculationIndex - 1));
    }

    // add sth to current calculation string
    currentCalculation += " exit!";
    this.lastCalculationText.setText(currentCalculation, null, null);

    this.lang.nextStep();

    this.sourceCode.unhighlight(11);
    this.sourceCode.highlight(18);

    this.lang.nextStep("Result");

    return result;
  }

  private void showSummary(int base, int decimal, String converted) {
    // hide calculation group
    for (Text text : this.calculationGroup)
      text.hide();

    // define header text layout
    TextProperties summaryHeadLayout = new TextProperties();
    summaryHeadLayout.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 16));
    summaryHeadLayout.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(
        255, 153, 0));

    // define text layout
    TextProperties summaryLayout = new TextProperties();
    summaryLayout.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    summaryLayout.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255,
        153, 0));

    // create summary group and text helper
    ArrayList<Text> summaryGroup = new ArrayList<Text>();
    Text lastSummaryText = null;
    int currentSummaryIndex = 1;

    // create title and add to group
    Text txSummary = this.lang.newText(new Coordinates(400, 180), "Summary:",
        "txSummary", null, summaryHeadLayout);
    summaryGroup.add(txSummary);

    // create empty (pseudo) calculation text as an anchor
    lastSummaryText = this.lang.newText(new Coordinates(400, 190), "", "sum0",
        null, summaryLayout);
    summaryGroup.add(lastSummaryText);

    // create intro text for summary + space to next
    lastSummaryText = this.lang.newText(new Offset(0, 15, lastSummaryText,
        AnimalScript.DIRECTION_NW), "let's check our result:", "sum"
        + currentSummaryIndex++, null, summaryLayout);
    summaryGroup.add(lastSummaryText);

    lastSummaryText = this.lang.newText(new Offset(0, 10, lastSummaryText,
        AnimalScript.DIRECTION_NW), "", "sum" + currentSummaryIndex++, null,
        summaryLayout);
    summaryGroup.add(lastSummaryText);

    // create character resolution hashtable
    HashMap<Character, Integer> resolution = new HashMap<Character, Integer>();
    resolution.put('0', 0);
    resolution.put('1', 1);
    resolution.put('2', 2);
    resolution.put('3', 3);
    resolution.put('4', 4);
    resolution.put('5', 5);
    resolution.put('6', 6);
    resolution.put('7', 7);
    resolution.put('8', 8);
    resolution.put('9', 9);
    resolution.put('A', 10);
    resolution.put('B', 11);
    resolution.put('C', 12);
    resolution.put('D', 13);
    resolution.put('E', 14);
    resolution.put('F', 15);

    // declare variables
    int position = converted.length() - 1;
    int result = 0;

    // go through result string (char by char)
    while (position >= 0) {
      // determine current exponent, factor and result
      int exponent = converted.length() - 1 - position;
      int factor = resolution.get(converted.charAt(position));
      int temp = factor * (int) Math.pow(base, exponent);

      result += temp;

      // decrease position
      position--;

      // print current line
      String line = "" + factor + " * " + base + "^" + exponent + " (= " + temp
          + ") +";

      lastSummaryText = this.lang.newText(new Offset(0, 15, lastSummaryText,
          AnimalScript.DIRECTION_NW), line, "sum" + currentSummaryIndex++,
          null, summaryLayout);
      summaryGroup.add(lastSummaryText);
    }

    // print summarizing line
    lastSummaryText = this.lang.newText(new Offset(0, 25, lastSummaryText,
        AnimalScript.DIRECTION_NW), "= " + result + " !!!", "sum"
        + currentSummaryIndex++, null, summaryHeadLayout);
    summaryGroup.add(lastSummaryText);

    // determine highest x-value for bounding box
    int highestX = 0;
    for (Text text : summaryGroup) {
      int current = text.getText().length() * 12;

      if (current > highestX)
        highestX = current;
    }

    // create rect properties
    RectProperties rectLayout = new RectProperties();
    rectLayout.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 153,
        0));
    rectLayout.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(234, 234,
        234));
    rectLayout.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectLayout.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    // create surrounding for summary
    this.lang
        .newRect(new Offset(-5, -5, txSummary, AnimalScript.DIRECTION_NW),
            new Offset(highestX + 50, 5, lastSummaryText,
                AnimalScript.DIRECTION_SW), "summaryRect", null, rectLayout);
  }

  /**
   * all needed data for determine random values
   */
  private Random random              = null;
  private int    questionProbability = 20;

  /**
   * determines whether a certain question should be shown or not (determined
   * randomly)
   */
  private boolean askQuestion() {
    // check if asking questions is enabled
    if (!this.input.showQuestions)
      return false;

    // determine random value from 0 to 99
    int n = this.random.nextInt(100);

    // check probability
    return (n < questionProbability);
  }
}