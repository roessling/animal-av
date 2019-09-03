package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;
import de.ahrgr.animal.kohnert.asugen.EKFont;

public class MatrixGenerator implements Generator {

  /**
   * static variables -> simple testing
   */
  static int[][]              matrixA             = { { 1, 2 }, { 3, 4 } };
  static int[][]              matrixB             = { { 5, 6 }, { 7, 8 } };
  static SourceCodeProperties SC_PROPS;
  static SourceCodeProperties TEXT_PROPS;
  static int                  HIGHLIGHTING_TIME   = 100;                   // in
                                                                            // ticks
  static int                  UNHIGHLIGHTING_TIME = 0;                     // in
                                                                            // ticks
  // =========================================================================================================

  /**
   * The concrete language object used for creating output
   */
  private Language            lang;

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
   * The matrices
   */
  private StringMatrix        summe               = null;
  private IntMatrix           intMatrixA          = null;
  private IntMatrix           intMatrixB          = null;
  private IntMatrix           intMatrixResult     = null;

  /**
   * The lines using rectanlges
   */
  private Rect                line1               = null;
  private Rect                line2               = null;

  /**
   * unhighlightingTime
   */
  private Timing              unhighTime          = new Timing(
                                                      UNHIGHLIGHTING_TIME) {

                                                    @Override
                                                    public String getUnit() {
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
                                                      return "ticks";
                                                    }
                                                  };

  // =========================================================================================================

  /**
   * Default constructor
   */
  public MatrixGenerator() {
    this.lang = new AnimalScript("Falksches Schema [DE]",
        "Ulf Gebhardt, Michael Scholz", 800, 600);

    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
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
            "Das hier vorgestellte Verfahren eignet sich zur Multiplikation zweier Matrizen.",
            null, 0, null);
    this.info
        .addCodeLine(
            "Zwei Matrizen können genau dann multipliziert werden, wenn die Spaltenanzahl der",
            null, 0, null);
    this.info
        .addCodeLine(
            "linken Matrix mit der Zeilenanzahl der rechten Matrix übereinstimmt. ",
            null, 0, null);
    this.info.addCodeLine(
        "Das Verfahren lässt sich leicht iterativ implementieren.", null, 0,
        null);
    this.info.addCodeLine("Die Funktioneweise ist hierbei wie folgt:", null, 0,
        null);
    this.info
        .addCodeLine(
            "- Betrachte die i-te Zeile der linken Matrix und die i-te Spalte der rechten Matrix.",
            null, 1, null);
    this.info
        .addCodeLine(
            "- Nun wird des j-te Element der i-ten Zeilen mit dem j-ten Element der i-ten Spalte multipliziert.",
            null, 1, null);
    this.info
        .addCodeLine(
            "- Die einzelnen Ergebnisse der Multiplikationen werden addiert und man erhält das Element e(ij) der Ergebnismatrix E.",
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
    lang.newText(new Coordinates(11, 15), "Falksches Schema", "header", null,
        textProperties);

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
    this.sc = lang.newSourceCode(new Coordinates(150, 45), "sourceCode", null,
        SC_PROPS);
    this.sc.addCodeLine(
        "public void falkschesSchema(int[][] matrixA, int[][] matrixB){", null,
        0, null); // 0
    this.sc.addCodeLine(
        "int[][] result = new int[matrixA.length][matrixB[0].length];", null,
        1, null); // 1
    this.sc.addCodeLine("int summe = 0;", null, 1, null); // 1
    this.sc.addCodeLine("for(int i=0; i<matrixA.length; i++){", null, 1, null); // 2
    this.sc.addCodeLine("for( int j=0; j<matrixB[0].length; j++){", null, 2,
        null); // 3
    this.sc.addCodeLine("for(int k=0; k<matrixA[0].length; k++){", null, 3,
        null); // 3
    this.sc.addCodeLine("summe = summe + matrixA[i][k]*matrixB[k][j];", null,
        4, null); // 3
    this.sc.addCodeLine("result[i][j]=summe;", null, 4, null); // 3
    this.sc.addCodeLine("}", null, 3, null); // 3
    this.sc.addCodeLine("summe = 0;", null, 3, null); // 3
    this.sc.addCodeLine("}", null, 2, null); // 3
    this.sc.addCodeLine("}", null, 1, null); // 3

  }

  /**
   * Generates the one single matrix including matrixA, matrixB und the
   * calculated result matrix
   */
  public void generateMatrices() {

    MatrixProperties mp = new MatrixProperties();
    mp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

    // generate matrixB
    this.intMatrixB = lang.newIntMatrix(new Offset(20, 40, sc,
        AnimalScript.DIRECTION_S), matrixB, "matrixB", null, mp);

    // generate matrixA with an offset to matrixB
    this.intMatrixA = lang.newIntMatrix(new Offset((-30 * matrixB.length), 10,
        intMatrixB, AnimalScript.DIRECTION_SW), matrixA, "matrixA", null, mp);

    // generate result matrix
    int[][] result = new int[matrixB[0].length][matrixA.length];
    // fill result[][] with zeros
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = 0;
      }
    }

    this.intMatrixResult = lang.newIntMatrix(new Offset(0, 10, intMatrixB,
        AnimalScript.DIRECTION_SW), result, "table", null, mp);

    String[][] data = new String[1][2];
    data[0][0] = "Summe = ";
    data[0][1] = "";

    this.summe = lang.newStringMatrix(new Offset(15, 0, intMatrixB,
        AnimalScript.DIRECTION_E), data, "stumme", null);
    this.summe.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
        null, null);

    // generate lines using rectangles
    line1 = lang.newRect(new Offset(-4, 0, intMatrixB,
        AnimalScript.DIRECTION_NW), new Offset(-4, 0, intMatrixResult,
        AnimalScript.DIRECTION_SW), "line1", null);

    line2 = lang.newRect(new Offset(0, -2, intMatrixA,
        AnimalScript.DIRECTION_NW), new Offset(0, -2, intMatrixResult,
        AnimalScript.DIRECTION_NE), "line2", null);

  }

  /**
   * Falksches Schema
   * 
   * @param matrixA
   * @param matrixB
   */
  private void falk() {

    // STEP
    lang.nextStep("Einleitung");
    info.hide();

    // generate the source code
    generateSourceCode();

    // generate matrices
    generateMatrices();

    // STEP
    lang.nextStep("Der Algorithmus");
    sc.highlight(0);

    // STEP
    lang.nextStep();
    sc.unhighlight(0);
    sc.highlight(1);

    // STEP
    lang.nextStep();
    sc.unhighlight(1);
    sc.highlight(2);
    this.summe.put(0, 1, "0", null, null);
    int[][] result = new int[matrixA.length][matrixB[0].length]; // 1
    int summe = 0; // 2

    int summetmp = 0;
    for (int i = 0; i < matrixA.length; i++) { // 3
      // STEP
      lang.nextStep();
      sc.unhighlight(2);
      sc.unhighlight(9);
      sc.highlight(3);
      for (int j = 0; j < matrixB[0].length; j++) { // 4
        // STEP
        lang.nextStep();
        sc.unhighlight(9);
        sc.unhighlight(3);
        sc.highlight(4);
        for (int k = 0; k < matrixA[0].length; k++) { // 5
          // STEP
          lang.nextStep();
          sc.unhighlight(7);
          intMatrixResult.unhighlightCell(i, j, highTime, unhighTime);
          sc.unhighlight(4);
          sc.highlight(5);
          summetmp = summe;
          summe = summe + matrixA[i][k] * matrixB[k][j]; // 6
          // STEP
          lang.nextStep();
          sc.unhighlight(5);
          sc.highlight(6);
          intMatrixA.highlightCell(i, k, highTime, unhighTime);
          intMatrixB.highlightCell(k, j, highTime, unhighTime);
          this.summe.put(0, 1, summetmp + "+(" + matrixA[i][k] + "*"
              + matrixB[k][j] + ")", null, null);
          this.summe.highlightCell(0, 1, highTime, unhighTime);
          result[i][j] = summe; // 7
          // STEP
          lang.nextStep();
          sc.unhighlight(6);
          sc.highlight(7);
          intMatrixA.unhighlightCell(i, k, highTime, unhighTime);
          intMatrixB.unhighlightCell(k, j, highTime, unhighTime);
          this.summe.unhighlightCell(0, 1, highTime, unhighTime);
          intMatrixResult.highlightCell(i, j, highTime, unhighTime);
          intMatrixResult.put(i, j, summe, null, null);
        } // 8
        // STEP
        lang.nextStep();
        sc.unhighlight(7);
        sc.highlight(9);
        intMatrixResult.unhighlightCell(i, j, highTime, unhighTime);
        summetmp = summe = 0; // 9
        this.summe.put(0, 1, summetmp + "", null, null);
      }
    }
    // STEP
    lang.nextStep();
    sc.unhighlight(9);

    // STEP
    lang.nextStep();

    // hide all and show the result
    intMatrixA.hide();
    intMatrixB.hide();
    this.summe.hide();
    line1.hide();
    line2.hide();

    algoanim.primitives.Text ergebnis = lang
        .newText(
            new Offset(-60, 0, intMatrixResult, AnimalScript.DIRECTION_NW),
            "Ergebnis: ", "name", null);

    // STEP
    lang.nextStep();
    ergebnis.hide();
    sc.hide();
    intMatrixResult.hide();
    generateHeader();

  }

  /**
   * Generates the statement for the last slides
   */
  private void generateStatement() {

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    statement = lang.newSourceCode(new Coordinates(10, 75), "statement", null,
        TEXT_PROPS);
    statement.addCodeLine("Komplexität:", null, 0, null);

    MultipleSelectionQuestionModel msq = new MultipleSelectionQuestionModel(
        "multipleSelectionQuestion");
    msq.setPrompt("Welche Komplexität besitzt die behandelte Methode?");
    msq.addAnswer("O(n^2)", -1, "Nein, es handelt sich um O(n^3)!");
    msq.addAnswer("O(n^3)", 1, "Richtig!");
    msq.addAnswer("Omega(n^3)", -1, "Nein, es handelt sich um O(n^3)!");
    msq.setGroupID("Second question group");
    lang.addMSQuestion(msq);

    // STEP
    lang.nextStep("Komplexität");

    statement
        .addCodeLine(
            "Abschließend wollen wir noch einen kurzen Blick auf die Komplexität des Verfahrens werfen.",
            null, 0, null);
    statement
        .addCodeLine(
            "Die kritischen Elemente des Algorithmus sind die drei verschachtelten Schleifen.",
            null, 0, null);
    statement.addCodeLine(
        "Die äußere Schleife läuft über die Anzahl n der Zeilen der Matrix A.",
        null, 0, null);
    statement
        .addCodeLine(
            "Die mittlere Schleife läuft über die Anzahl m der Spalten der Matrix B.",
            null, 0, null);
    statement.addCodeLine(
        "Die innere Schleife läuft über die Anzahl p der Spalten von A.", null,
        0, null);
    statement
        .addCodeLine(
            "Durch Anwendung der Produktregel erhalten wir somit eine kubische Komplexität von O(n^3).",
            null, 0, null);

  }

  @Override
  public void init() {
    lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);
    lang.setStepMode(true);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    matrixA = (int[][]) primitives.get("matrixA");
    matrixB = (int[][]) primitives.get("matrixB");
    SC_PROPS = (SourceCodeProperties) props.getPropertiesByName("Quelltext");
    TEXT_PROPS = (SourceCodeProperties) props.getPropertiesByName("Text");

    generateHeader();
    generateInfoText();
    falk();
    generateStatement();

    lang.finalizeGeneration();
    return lang.toString();
  }

  @Override
  public String getName() {
    return "Falksches Schema [DE]";
  }

  @Override
  public String getAlgorithmName() {
    return "Falksches Schema";
  }

  @Override
  public String getAnimationAuthor() {
    return "Ulf Gebhardt, Michael Scholz";
  }

  @Override
  public String getDescription() {
    return "Das hier vorgestellte Verfahren eignet sich zur Multiplikation zweier Matrizen. Zwei Matrizen k&ouml;nnen genau dann "
        + "multipliziert werden, wenn die Spaltenanzahl der linken Matrix mit der Zeilenanzahl der rechten Matrix "
        + "&uuml;bereinstimmt. Das Verfahren l&auml;sst sich leicht iterativ implementieren."
        + "<br>Die Funktioneweise ist hierbei wie folgt:"
        + "<br>- Betrachte die i-te Zeile der linken Matrix und die i-te Spalte der rechten Matrix."
        + "<br>- Nun wird des j-te Element der i-ten Zeilen mit dem j-ten Element der i-ten Spalte multipliziert."
        + "<br>- Die einzelnen Ergebnisse der Multiplikationen werden addiert und man erh&auml;lt das Element e(ij) der Ergebnismatrix E.";

  }

  @Override
  public String getCodeExample() {
    return "public1 int[][] falkschesSchema(int[][] matrixA, int[][] matrixB){"
        + "\n  int[][] result = new int[matrixA.length][matrixB[0].length];"
        + "\n  int summe=0;" + "\n  for(int i=0; i < matrixA.length; i++){"
        + "\n    for( int j=0; j < matrixB[0].length; j++){"
        + "\n      for(int k=0; k < matrixA[0].length; k++){"
        + "\n        summe = summe + matrixA[i][k]*matrixB[k][j];"
        + "\n        result[i][j]=summe;" + "\n      }" + "\n      summe=0;"
        + "\n    }" + "\n  }" + "\n  return result;" + "\n}";
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