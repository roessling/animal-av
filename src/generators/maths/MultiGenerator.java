package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;

import java.util.Hashtable;

import de.ahrgr.animal.kohnert.asugen.EKFont;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class MultiGenerator implements Generator {

  /**
   * static variables -> simple testing
   */
  static int                  A;
  static int                  B;
  static SourceCodeProperties SC_PROPS;
  static SourceCodeProperties TEXT_PROPS;
  static int                  HIGHLIGHTING_TIME   = 100;         // in ticks
  static int                  UNHIGHLIGHTING_TIME = 0;           // in ticks

  // =========================================================================================================

  /**
   * The concrete language object used for creating output
   */
  private Language            lang;

  /**
   * random number generator for user interaction
   */
  private Random              rand                = new Random();

  /**
   * number of questions
   */
  private int                 questionCount       = 0;

  /**
   * number of correct answers
   */
  // private int correctAnswers = 0;

  /**
   * The "nicht addieren!" text
   */
  private SourceCode          nichtAddieren       = null;

  /**
   * The info text as a SourceCode object (first slide)
   */
  private SourceCode          info                = null;

  /**
   * The statement text as a SourceCode object (last several slides)
   */
  private SourceCode          statement           = null;

  /**
   * The sourceCode
   */
  private SourceCode          sc                  = null;

  /**
   * The tables
   */
  private StringMatrix        tb1                 = null;
  private StringMatrix        tb2                 = null;

  /**
   * int-array with the values of b (only for animation)
   */
  private int[]               bArray              = null;

  /**
   * recursionArray
   */
  private int[]               recursionArray      = null;

  /**
   * unhighlightingTime
   */
  private Timing              unhighTime          = new Timing(
                                                      UNHIGHLIGHTING_TIME) {

                                                    @Override
                                                    public String getUnit() {
                                                      // TODO Auto-generated
                                                      // method stub
                                                      return "ticks";
                                                    }
                                                  };

  /**
   * highlightTime
   */
  private Timing              highTime            = new Timing(
                                                      HIGHLIGHTING_TIME) {

                                                    @Override
                                                    public String getUnit() {
                                                      // TODO Auto-generated
                                                      // method stub
                                                      return "ticks";
                                                    }
                                                  };

  /**
   * number of lines for tb1
   */
  private int                 linesOfTb1          = 0;

  // =========================================================================================================

  /**
   * Default constructor
   */
  public MultiGenerator() {
    this.lang = new AnimalScript("Russische Bauernmultiplikation [DE]",
        "Ulf Gebhardt, Michael Scholz", 800, 600);

    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  /**
   * 
   * Builds the user interaction
   * 
   * @param question
   * @param answer
   * @param feedback
   */
  public void showFillInQuestion(String question, String answer, String feedback) {

    this.questionCount++;

    FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
        String.valueOf(this.questionCount));
    fibq.setPrompt(question);
    fibq.addAnswer(answer, 1, feedback);
    fibq.setGroupID("First question group");
    lang.addFIBQuestion(fibq);

    // this.correctAnswers = fibq.getPointsAchieved();
    // method getPointsAchieved() always returns 0 as result....
  }

  /**
   * Builds the information text for the first page in the animation
   */
  public void generateInfoText() {

    // create first page of animation (info text) as a code group
    this.info = lang.newSourceCode(new Coordinates(10, 75), "info", null,
        TEXT_PROPS);
    this.info
        .addCodeLine(
            "Das hier vorgestellte Verfahren eignet sich für die Multiplikation zweier ganzer Zahlen.",
            null, 0, null);
    this.info
        .addCodeLine(
            "Die Funktionsweise lässt sich in die folgenden fünf Schritte gliedern:",
            null, 0, null);
    this.info.addCodeLine(
        "1. Schreibe die beiden zu multiplizierenden Zahlen nebeneinander.",
        null, 1, null);
    this.info
        .addCodeLine(
            "2. Die linke Zahl wird halbiert (Reste werden abgerundet), die rechte Zahl wird verdoppelt.",
            null, 1, null);
    this.info
        .addCodeLine(
            " Die beiden berechneten Zahlen werden in die darauffolgende Zeile geschrieben.",
            null, 2, null);
    this.info
        .addCodeLine(
            "3. Schritt 2 wird solange wiederholt, bis in der linken Spalte eine 1 steht.",
            null, 1, null);
    this.info.addCodeLine(
        "4. Nun streicht man alle Zeilen, in denen die linke Zahl gerade ist.",
        null, 1, null);
    this.info
        .addCodeLine(
            "5. Schlussendlich werden alle uebrigen Zahlen der rechten Spalte addiert.",
            null, 1, null);

  }

  /**
   * Builds the header which is shown during the whole animation
   */
  public void generateHeader() {

    // head
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new java.awt.Font("Serif", EKFont.SANSSERIF, 24));
    lang.newText(new Coordinates(11, 15), "Russische Bauernmultiplikation",
        "header", null, textProperties);

    // header background
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", AnimalScript.DIRECTION_SE),
        "headerBackground", null, rectProperties);
  }

  /**
   * Builds the SourceCode
   */
  public void generateSourceCode() {

    // initialize source code object and add code lines
    this.sc = lang.newSourceCode(new Coordinates(10, 147), "sourceCode", null,
        SC_PROPS);
    this.sc.addCodeLine("public int russe(int a, int b){", null, 0, null); // 0
    this.sc.addCodeLine("if(a == 1){", null, 1, null); // 1
    this.sc.addCodeLine("return b;", null, 2, null); // 2
    this.sc.addCodeLine("}", null, 1, null); // 3
    this.sc.addCodeLine("if(a % 2 == 1){", null, 1, null); // 4
    this.sc.addCodeLine("return b + russe(a/2, b*2);", null, 2, null); // 5
    this.sc.addCodeLine("}else{", null, 1, null); // 6
    this.sc.addCodeLine("return russe(a/2, b*2);", null, 2, null); // 7
    this.sc.addCodeLine("}", null, 1, null); // 8
    this.sc.addCodeLine("}", null, 0, null); // 9

  }

  /**
   * calculate needed lines -> animation is more dynamic
   * 
   * @param a
   * @param b
   */
  public void calculateLines(int a, int b) {

    // calculate needed lines -> animation is more dynamic
    int aTemp = a;
    int lines = 3;
    while (aTemp != 1) {
      aTemp = aTemp / 2;
      lines++;
    }
    // initialize the bArray und the recursionArray
    this.bArray = new int[lines - 1];
    this.recursionArray = new int[lines - 1];
    this.linesOfTb1 = lines;
  }

  /**
   * Builds the table
   * 
   * @param a
   * @param b
   * @param lines
   */
  public void generateTableTb1(int a, int b, int lines) {

    // create String[][] data array for tb1
    String[][] tb1Data = new String[lines][4];

    // fill all fields with ""
    for (int i = 0; i < tb1Data.length; i++) {
      for (int j = 0; j < tb1Data[i].length; j++) {
        tb1Data[i][j] = "";
      }
    }

    // fill the known fields
    tb1Data[0][0] = "A-Seite";
    tb1Data[0][1] = "B-Seite";
    tb1Data[0][2] = "addieren";
    tb1Data[0][3] = "Summe";
    tb1Data[1][0] = a + "";
    tb1Data[1][1] = b + "";

    // create table tb1
    this.tb1 = lang.newStringMatrix(new Coordinates(270, 100), tb1Data, "tb1",
        null);

  }

  /**
   * builds the statement for the last several slides
   */
  public void generateStatement() {
    // final statement
    // SourceCodeProperties statementProps = new SourceCodeProperties();
    // statementProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // java.awt.Font("SansSerif", Font.SANSSERIF, 16));

    this.statement = lang.newSourceCode(new Coordinates(10, 75), "statement",
        null, TEXT_PROPS);
    this.statement.addCodeLine("Erklaerung:", null, 0, null);
    this.statement
        .addCodeLine(
            "Die Idee des Verfahrens kann man mit Hilfe des Dualsystems verdeutlichen.",
            null, 0, null);
    this.statement
        .addCodeLine("Hierbei wird eine Zahl in ihre Zweierpotenzen zerlegt.",
            null, 0, null);
    this.statement.addCodeLine("", null, 0, null); // empty line for vertical
                                                   // space
    // STEP
    lang.nextStep();
    this.statement.addCodeLine(
        "82 * 27 = 82 * (2^0 + 2^1 + 0 * 2^2 + 2^3 + 2^4 )", null, 0, null);
    // STEP
    lang.nextStep();
    this.statement.addCodeLine(
        "= 82 * 2^0 + 82 * 2^1 + 82 * 0 + 82 * 2^3 + 82 * 2^4", null, 4, null);
    // STEP
    lang.nextStep();
    this.statement.addCodeLine("= 82 + 164 + 0 + 656 + 1312", null, 4, null);
    // STEP
    lang.nextStep();
    this.statement.addCodeLine("= 2214", null, 4, null);

    // STEP
    lang.nextStep();
    this.statement.hide();
    SourceCode komp = lang.newSourceCode(new Coordinates(10, 75), "komp", null,
        TEXT_PROPS);
    komp.addCodeLine("Komplexität:", null, 0, null);
    komp.addCodeLine(
        "Abschließend wollen wir noch einen kurzen Blick auf die Komplexität des Verfahrens werfen.",
        null, 0, null);
    komp.addCodeLine(
        "In jedem Schritt wird die erste Zahl halbiert. Nennen wir diese Zahl n und betrachten die Zeit des aufaddierens als eine Konstante c.",
        null, 0, null);
    komp.addCodeLine(
        "Somit erhalten wir als obere Schranke O(n). Die Konstante c kann vernachlässigt werden.",
        null, 0, null);
    komp.addCodeLine("", null, 0, null); // empty line -> vertical space
    komp.addCodeLine("", null, 0, null);

    // -> Can't
    /*
     * komp.addCodeLine("Sie haben während der Animation "+ this.correctAnswers
     * + " von " + this.questionCount + " Fragen richtig beantwortet.", null, 0,
     * null); if(this.correctAnswers * 2 < this.questionCount){
     * komp.addCodeLine(
     * "Das ist weniger als die Hilfte! Bitte beschäftigen Sie sich weiter mit dem Verfahren."
     * , null, 0, null); }else if(this.correctAnswers == this.questionCount){
     * komp.addCodeLine(
     * "Super, Sie haben 100 Prozent der gestellten Fragen richtig beantwortet :)."
     * , null, 0, null); }else{ komp.addCodeLine("Gar nicht schlecht!", null, 0,
     * null); }
     */

  }

  /**
   * write "nicht addieren"
   */
  public void writeNichtAddieren() {
    SourceCodeProperties scProps = new SourceCodeProperties();
    this.nichtAddieren = lang.newSourceCode(new Offset(2, 8, this.tb2,
        AnimalScript.DIRECTION_SW), "sourceCode", null, scProps);
    nichtAddieren.addCodeLine("Das Zwischenergebnis bleibt unverändert!", null,
        0, null);
    this.nichtAddieren.hide();
  }

  /**
   * Draw the table tb2
   * 
   * @param bArrayLength
   */
  public void drawTb2(int bArrayLength) {

    // create String[][] data array for tb2
    String[][] tb2Data = new String[2][1];
    tb2Data[0][0] = "Rekursion aufloesen:";

    String tmp = "";
    for (int i = 1; i < bArrayLength; i++) {
      if (this.bArray[i] != 0) {
        tmp = tmp + this.bArray[i];
        if (i + 1 != bArrayLength) {
          tmp = tmp + "+";
        }
      }
    }
    tb2Data[1][0] = tmp;

    // create table tb2
    if (this.tb2 != null) {
      this.tb2.put(1, 0, tmp, this.highTime, this.highTime);
    } else {
      this.tb2 = lang.newStringMatrix(new Offset(0, 10, this.tb1,
          AnimalScript.DIRECTION_SW), tb2Data, "tb2", null);
      this.tb2.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
          null, null);
    }
  }

  /**
   * Generates the animation of the recursion flow
   */
  public void generateRecursion() {

    // draw tb2
    drawTb2(this.bArray.length);

    // write "nicht addieren"
    writeNichtAddieren();

    // STEP
    lang.nextStep();

    int newLineNumber = 0;
    int oldLineNumber = 0;
    this.sc.unhighlight(2); // "return b"
    for (int i = this.bArray.length - 1; i > 1; i--) {
      oldLineNumber = newLineNumber;
      if (this.recursionArray[i - 1] == 0) {
        newLineNumber = 5;
      } else {
        newLineNumber = 7;
      }
      if (oldLineNumber == 5) {
        this.tb2.unhighlightCell(1, 0, this.unhighTime, this.unhighTime);
      }
      this.nichtAddieren.hide();
      this.sc.unhighlight(oldLineNumber);
      // STEP
      lang.nextStep();
      this.sc.highlight(newLineNumber);
      if (newLineNumber == 5) {
        this.tb2.highlightCell(1, 0, this.highTime, this.highTime);
      } else {
        this.nichtAddieren.show();
      }
      // update tb2
      this.bArray[i - 1] = this.bArray[i - 1] + this.bArray[i];
      drawTb2(i);
      // STEP
      lang.nextStep();
    }
    this.sc.unhighlight(oldLineNumber);
  }

  /**
   * logical method: calls the needed methods for generating the animation
   * 
   * @param a
   * @param b
   */
  public void multiply(int a, int b) {

    // set some options for user interaction
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "First question group", 1);
    lang.addQuestionGroup(groupInfo);

    // generate information text and the header
    generateInfoText();
    generateHeader();

    // STEP
    lang.nextStep("Beispiel");
    this.info.hide();

    // generate SourceCode
    generateSourceCode();

    // calculate needed lines and generate table
    calculateLines(a, b);
    generateTableTb1(a, b, this.linesOfTb1);

    // STEP
    lang.nextStep();

    // higlight line 0
    this.sc.highlight(0);

    russe(a, b, sc, tb1, 1, "");

    // unhighlight last line of tb1
    this.tb1.unhighlightCellColumnRange(this.linesOfTb1 - 2, 2, 3,
        this.unhighTime, this.unhighTime);

    // generate recursion animation
    generateRecursion();

    // STEP
    lang.nextStep("Fazit");
    this.tb1.hide();
    this.tb2.hide();
    this.sc.hide();

    // generate statement
    generateStatement();

  }

  /**
   * the algorithm which is used in the animation
   * 
   * @param a
   * @param b
   * @param code
   * @param tb1
   * @param line
   * @param sum
   * @return the product of a and b (a*b)
   */
  public int russe(int a, int b, SourceCode code, StringMatrix tb1, int line,
      String sum) {

    int aTb = a;
    int bTb = b;

    this.tb1.unhighlightCellColumnRange(line - 1, 2, 3, this.unhighTime,
        this.unhighTime);
    this.tb1.highlightCellColumnRange(line, 0, 1, this.highTime, this.highTime);

    // user interaction
    if (rand.nextBoolean()) { // ask question?
      if (rand.nextBoolean()) { // ask for a or b?
        showFillInQuestion("Welche Zahl steht in der nächsten Zeile von a?",
            String.valueOf(a / 2), "Richtig!");
      } else {
        showFillInQuestion("Welche Zahl steht in der nächsten Zeile von b?",
            String.valueOf(b * 2), "Richtig!");
      }

    }

    this.tb1.put(line, 0, a + "", null, null); // update column 0 (a)
    this.tb1.put(line, 1, b + "", null, null); // update column 1 (b)

    // STEP
    lang.nextStep();
    this.tb1.unhighlightCellColumnRange(line, 0, 1, this.unhighTime,
        this.unhighTime);
    code.unhighlight(0);
    code.highlight(1);

    // STEP
    lang.nextStep();

    String sum2 = sum;
    if (a == 1) {
      code.toggleHighlight(1, 2);

      // write last line
      sum2 = sum2 + "+" + b;
      // update table
      this.tb1.highlightCellColumnRange(line, 2, 3, this.highTime,
          this.highTime);
      this.tb1.put(line, 2, "Ja", null, null); // update column 2 (addieren)
      this.tb1.put(line, 3, sum2, null, null); // update column 3 (Summe)
      this.bArray[line] = b;
      return b;
    }
    if (line != 1) { // first line
      aTb = aTb / 2;
      bTb = bTb * 2;
    }
    code.toggleHighlight(1, 4);

    // STEP
    lang.nextStep();
    code.unhighlight(1);

    if (a % 2 == 1) {
      code.toggleHighlight(4, 5);

      if (line == 1) {
        sum2 = b + "";
      } else {
        if (sum2.equals("")) {
          sum2 = b + "";
        } else {
          sum2 = sum2 + "+" + b;
        }
      }
      // update table
      this.tb1.highlightCellColumnRange(line, 2, 3, this.highTime,
          this.highTime);
      this.tb1.put(line, 2, "Ja", null, null); // update column 2 (addieren)
      this.tb1.put(line, 3, sum2, null, null); // update column 3 (Summe)
      // inser
      // STEP
      lang.nextStep();
      code.unhighlight(5);
      code.highlight(0);
      // safe actual value of b in bArray
      this.bArray[line] = b;
      this.recursionArray[line] = 0;
      return b + russe(a / 2, b * 2, code, tb1, line + 1, sum2);
    } else {
      code.toggleHighlight(4, 7);
      // update table
      this.tb1.highlightCellColumnRange(line, 2, 3, this.highTime,
          this.highTime);
      this.tb1.put(line, 2, "Nein", null, null); // update column 2 (addieren)
      this.tb1.put(line, 3, sum2, null, null); // update column 3 (Summe)

      // STEP
      lang.nextStep();
      code.highlight(0);
      code.unhighlight(7);
      // safe 0 in bArray
      this.bArray[line] = 0;
      this.recursionArray[line] = 1;
      return russe(a / 2, b * 2, code, tb1, line + 1, sum2);
    }
  }

  @Override
  public void init() {
    lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);
    lang.setStepMode(true);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    A = (Integer) primitives.get("a");
    B = (Integer) primitives.get("b");
    SC_PROPS = (SourceCodeProperties) props.getPropertiesByName("Quelltext");
    TEXT_PROPS = (SourceCodeProperties) props.getPropertiesByName("Text");

    multiply(A, B);
    lang.finalizeGeneration();
    return lang.toString();
  }

  @Override
  public String getName() {
    return "Russische Bauernmultiplikation [DE]";
  }

  @Override
  public String getAlgorithmName() {
    return "Russische Bauernmultiplikation";
  }

  @Override
  public String getAnimationAuthor() {
    return "Ulf Gebhardt, Michael Scholz";
  }

  @Override
  public String getDescription() {
    return "Das hier vorgestellte Verfahren eignet sich f&uuml;r die Multiplikation zweier ganzer Zahlen. Es ist auch unter den"
        + "\n"
        + "Namen &Auml;gyptischen Multiplizieren, Abessinische Bauernregel oder Verdopplungs-Halbierungs-Methode"
        + "\n"
        + "bekannt. Die Geschichte des vorgestellten Rechenverfahrens f&uuml;hrt bis auf die &Auml;gypter zur&uuml;ck. Sie"
        + "\n"
        + "nutzten diese Methode nachweislich zur Multiplikation zweier ganzer Zahlen. Das Verfahren baut auf dem Teile und Herrsche Prinzip"
        + "\n"
        + "(Divide et impera ) auf und l&auml;sst sich somit leicht mittels Rekursion implementieren.\n"
        + "\n"
        + "<br>Die Funktionsweise l&auml;sst sich in die folgenden f&uuml;nf Schritte gliedern:\n"
        + "\n"
        + "<br>1. Schreibe die beiden zu multiplizierenden Zahlen nebeneinander.\n"
        + "\n"
        + "<br>2. Die linke Zahl wird halbiert (Reste werden abgerundet), die rechte Zahl wird verdoppelt.\n"
        + "\n"
        + "\t Die beiden berechneten Zahlen werden in die darauffolgende Zeile geschrieben.\n"
        + "\n"
        + "<br>3. Schritt 2 wird solange wiederholt, bis in der linken Spalte eine 1 steht.\n"
        + "\n"
        + "<br>4. Nun streicht man alle Zeilen, in denen die linke Zahl gerade ist.\n"
        + "\n"
        + "<br>5. Schlussendlich werden alle &uuml;brigen Zahlen der rechten Spalte addiert."
        + "\n" + "	";
  }

  @Override
  public String getCodeExample() {
    return "public int russe(int a, int b){" + "\n    if(a == 1){"
        + "\n      return b;" + "\n    }" + "\n    if(a % 2 == 1){"
        + "\n      return b + russe(a/2, b*2);" + "\n    }else{"
        + "\n      return russe(a/2, b*2);" + "\n    }" + "\n  }";
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
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}