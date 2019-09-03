package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * Klasse zur Erstellung einer Animation des Choleskyalgorithmus.
 * 
 * @author Patrick Hörmann, Jonas Kellert
 * 
 */
public class Cholgen implements Generator {
  private Language             lang;
  private int[][]              MatrixA;
  private Color                Lieblingsfarbe;
  private int                  col;
  private MatrixProperties     matrixProps;
  private SourceCodeProperties sourceProp;
  private SourceCodeProperties introProp;
  private TextProperties       headerProp;
  private Text                 header;
  private TextProperties       congratProp;
  // private Text congrat;
  private StringMatrix         valTableObj;

  private int                  dist1  = 0;
  private int                  dist2  = 0;
  private int                  xdistS = 0;

  public void init() {
    // Erstellt eine neue "Language"-instanz zur Contentproduktion
    lang = new AnimalScript("Cholesky Animation",
        "Patrick Hörmann, Jonas Kellert", 640, 480);
    // aktiviert die Schrittkontrolle
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    // Rechteckeigenschaften um den Titel zu hinterlegen
    RectProperties backRectProps = new RectProperties();
    backRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    backRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    ;
    backRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    backRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    headerProp = new TextProperties();
    headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(null,
        Font.BOLD, 24));
    headerProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    header = lang.newText(new Coordinates(20, 30), "Choleskyzerlegung",
        "header", null, headerProp);

    // Rechteck um Titel zu hinterlegen
    Rect backRect = lang.newRect(new Offset(-5, -5, "header", "NW"),
        new Offset(5, 5, "header", "SE"), "orange", null, backRectProps);
    backRect.show();

    // setzen von Anzeigeeigenschaften fuer die Zahlenmatrizen
    matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.YELLOW);
    matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);
    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

    // setzen der Quellcodeanzeigeeigenschaften
    sourceProp = new SourceCodeProperties();
    sourceProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    sourceProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sourceProp.set(AnimationPropertiesKeys.SIZE_PROPERTY, 12);

    introProp = new SourceCodeProperties();
    introProp.set(AnimationPropertiesKeys.SIZE_PROPERTY, 14);
    introProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.WHITE);

    SourceCode intro = lang.newSourceCode(new Coordinates(50, 100), "intro",
        null, introProp);
    intro
        .addCodeLine(
            "In der Vorlesung 'Mathe 3 für Informatik' wird das Cholesky-Verfahren zur Zerlegung",
            null, 0, null);
    intro
        .addCodeLine(
            "einer positiv-definiten Matrix A in eine obere und eine untere Dreiecksmatrix L behandelt.",
            null, 0, null);
    intro.addCodeLine("Dabei gilt: L * L^T = A", null, 0, null);
    intro
        .addCodeLine(
            "Diese Animation fährt eine Choleskyzerlegung Schritt für Schritt vor.",
            null, 0, null);
    intro.addCodeLine("Viel Spass beim Betrachten!", null, 0, null);

    lang.nextStep("Initialisierung");

    intro.hide();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    MatrixA = (int[][]) primitives.get("MatrixA");
    Lieblingsfarbe = (Color) primitives.get("Lieblingsfarbe :-)");
    zerlegung(MatrixA);
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Super-Cholesky";
  }

  public String getAlgorithmName() {
    return "Cholesky Verfahren";
  }

  public String getAnimationAuthor() {
    return "Patrick Hörmann, Jonas Kellert";
  }

  public String getDescription() {
    return "Das Cholesky-Verfahren ist ein Verfahren, um eine positiv-definite Matrix A"
        + "\n" + "in eine Matrix L zu zerlegen, für die gilt: L * L^T = A.";
  }

  public String getCodeExample() {
    return "for(int i = 0; i < col; i++){" + "\n" + "	double a = 0;" + "\n"
        + "	for(int k = 0; k < i; k++){" + "\n"
        + "		a = a + matrixL[i][k]*matrixL[i][k];" + "\n" + "	}" + "\n"
        + "	a = matrixA[i][i] - a;" + "\n" + "	if(a < 0){" + "\n"
        + "		System.out.println(\"Diese Matrix ist nicht positiv definit\");"
        + "\n" + "	}" + "\n" + "	matrixL[i][i] = Math.sqrt(a);" + "\n"
        + "	for(int k = i+1; k < col; k++){" + "\n"
        + "		double zwischensumme = 0;" + "\n"
        + "		for(int j = 0; j < i; j++){" + "\n"
        + "			zwischensumme = zwischensumme + matrixL[k][j]*matrixL[i][j];"
        + "\n" + "		}" + "\n"
        + "		matrixL[k][i] = matrixA[k][i] - zwischensumme;" + "\n"
        + "		matrixL[k][i] = matrixL[k][i]/matrixL[i][i];" + "\n" + "	}" + "\n"
        + "}" + "\n" + "return matrixL;";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  /**
   * Zerlegt die gegebene Matrix in untere und eine obere L Matrix.
   * 
   * @param matrixA2
   */
  public void zerlegung(int[][] matrixA2) {
    col = matrixA2.length;

    // defines the distances of the Rects to the upper left of the window, in
    // order of the size of matrix.
    int distL = 100 + col * 30 + 30;

    // defines the distances of the Rects to the upper left of the window, in
    // order of the size of matrix.
    dist1 = 100 + col * 30;
    dist2 = 100 + 2 * col * 30 + 30;
    xdistS = 10 + col * 40 + 10;

    // setzen von Anzeigeeigenschaften fuer die Zahlenmatrizen
    MatrixProperties matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.YELLOW);
    matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);
    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    matrixProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

    // wandelt die Integermatrix in eine Doublematrix um.
    double[][] doubleA = new double[col][col];

    for (int i = 0; i < col; i++) {
      for (int j = 0; j < col; j++) {
        doubleA[i][j] = matrixA2[i][j];
      }
    }

    // erstellt das DoubleMatrixObjekt
    DoubleMatrix dMatrixA = lang.newDoubleMatrix(new Coordinates(10, 100),
        doubleA, "A", null, matrixProps);

    lang.nextStep();

    // initialisieren der Matrix L als Einheitsmatrix
    double[][] matrixL = new double[col][col];
    for (int i = 0; i < col; i++) {
      for (int j = 0; j < col; j++) {
        if (i == j)
          matrixL[i][j] = 1;
        else
          matrixL[i][j] = 0;
      }
    }

    DoubleMatrix dMatrixL = lang.newDoubleMatrix(new Coordinates(10, distL),
        matrixL, "L", null, matrixProps);

    lang.nextStep();

    // setzen der Quellcodeanzeigeeigenschaften
    SourceCodeProperties sourceProp = new SourceCodeProperties();
    sourceProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    sourceProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sourceProp.set(AnimationPropertiesKeys.SIZE_PROPERTY, 12);

    SourceCode source = lang.newSourceCode(new Offset(30, -14, "A", "NE"),
        "SourceCode", null, sourceProp);
    source.addCodeLine(
        "public static double[][] cholesky(double[][] matrixA, int col){",
        null, 0, null);
    source.addCodeLine("for(int i = 0; i < col; i++){", null, 1, null);
    source.addCodeLine("double a = 0;", null, 2, null);
    source.addCodeLine("for(int k = 0; k < i; k++){", null, 2, null);
    source.addCodeLine("a = a + matrixL[i][k]*matrixL[i][k];", null, 3, null);
    source.addCodeLine("}", null, 2, null);
    source.addCodeLine("a = matrixA[i][i] - a;", null, 2, null);
    source.addCodeLine("if(a < 0){", null, 2, null);
    source.addCodeLine(
        "System.out.println('Diese Matrix ist nicht positiv definit');", null,
        3, null);
    source.addCodeLine("}", null, 2, null);
    source.addCodeLine("matrixL[i][i] = Math.sqrt(a);", null, 2, null);
    source.addCodeLine("for(int k = i+1; k < col; k++){", null, 2, null);
    source.addCodeLine("double ð = 0;", null, 3, null);
    source.addCodeLine("for(int j = 0; j < i; j++){", null, 3, null);
    source.addCodeLine("ð = ð + matrixL[k][j]*matrixL[i][j];", null, 4, null);
    source.addCodeLine("}", null, 3, null);
    source.addCodeLine("matrixL[k][i] = matrixA[k][i] - ð;", null, 3, null);
    source.addCodeLine("matrixL[k][i] = matrixL[k][i]/matrixL[i][i];", null, 3,
        null);
    source.addCodeLine("}", null, 2, null);
    source.addCodeLine("}", null, 1, null);
    source.addCodeLine("return matrixL;", null, 1, null);
    source.addCodeLine("}", null, 0, null);

    try {
      // Start cholesky
      dMatrixL = cholesky(dMatrixA, dMatrixL, source);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
    source.addCodeLine("", null, 0, null);
    source.addCodeLine("Die Matrix L ist nun eine linke untere Dreiecksmatrix",
        null, 0, null);
    source.addCodeLine("und wird zurückgegeben.", null, 0, null);
    source.highlight(23);
    source.highlight(24);
    header.setText("Choleskyzerlegung ✔", null, null);
    lang.nextStep();
    source.hide();
    dMatrixA.hide();
    dMatrixL.hide();
    valTableObj.hide();
    congratProp = new TextProperties();
    congratProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(null,
        Font.CENTER_BASELINE, 120));
    if (Lieblingsfarbe != null)
      congratProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Lieblingsfarbe);
    lang.newText(new Coordinates(150, 150), "☻", "congrat", null, congratProp);
    lang.newText(new Coordinates(20, 90), "Herzlichen Glückwunsch!",
        "congrat2", null, headerProp);
  }

  /**
   * Diese Funktion berechnet die Choleksyzerlegung der Matrix A mit
   * Erweiterungen für Animal
   * 
   * @param matrixA
   *          die zu "zerteilende Matrix A"
   * @param matrixL
   *          die noch nicht berechnete Matrix L (Einheitsmatrix)
   * @param source
   *          übergabe des Sourceqodes in form des Sourceobjekts
   * @return Rückgabe der Unterendreiecksmatrix L
   */
  private DoubleMatrix cholesky(DoubleMatrix matrixA, DoubleMatrix matrixL,
      SourceCode source) {

    // TrueFalseQuestionModel tfQuestion = new
    // TrueFalseQuestionModel("tfQuestion");
    MultipleChoiceQuestionModel mcQuestion = new MultipleChoiceQuestionModel(
        "mcQuestion");

    // Infotext und Eigenschaften um zuätzliche Informationen anzuzeigen.
    TextProperties infoTextProp = new TextProperties();
    infoTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(null,
        Font.PLAIN, 14));
    infoTextProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);

    Text infoText = lang.newText(new Coordinates(0, 0), " ", "info", null,
        infoTextProp);
    infoText.hide();

    // Properties of Rects to hide some not nice display errors of animal
    RectProperties hideingRectProps = new RectProperties();
    hideingRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.white);
    hideingRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);

    hideingRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    hideingRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    // Some Rects to hide some not nice display errors of animal
    lang.newRect(new Coordinates(0, dist1),
        new Coordinates(xdistS, dist1 + 30), "one", null, hideingRectProps);
    lang.newRect(new Coordinates(0, dist2),
        new Coordinates(xdistS, dist2 + 30), "two", null, hideingRectProps);
    lang.newRect(new Coordinates(0, dist2 + 30), new Coordinates(10, 1000),
        "three", null, hideingRectProps);
    lang.newRect(new Coordinates(10 + 3 * 29, dist2 + 30), new Coordinates(
        xdistS, 1000), "four", null, hideingRectProps);

    // setzen von Anzeigeeigenschaften für die Tabelle der Variablen
    MatrixProperties valTableProps = new MatrixProperties();
    valTableProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    valTableProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.green);
    valTableProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    // erstellen der Tablle der Variablen.
    String[][] valTable = { { "a", "", "" }, { "i", "", "" }, { "j", "", "" },
        { "k", "", "" }, { "ð", "", "" }, { "", "", "" } };
    valTableObj = lang.newStringMatrix(new Coordinates(10, dist2 + 30),
        valTable, "TableOfVar", null, valTableProps);
    Integer i, j, k, x = 1;
    // double a;
    Double a, zwischensumme;

    // begin der Animation der Zerlegung
    lang.nextStep();

    TrueFalseQuestionModel tfQuestion1 = new TrueFalseQuestionModel(
        "tfQuestion" + x++);
    changeTFQuestion(tfQuestion1,
        "Springt der Algorithmus nun in die Schleife?", "War das abzusehen?",
        "Du solltest nochmal nachschauen, welchen Werte i hat!", 0 < col);
    lang.addTFQuestion(tfQuestion1);
    for (i = 0; i < col; i++) {
      // hervorheben der Zeile "       for(int i = 0; i < col; i++){" und i mit
      // 0 initialisieren
      source.unhighlight(17);
      source.highlight(1);
      showInfoText(" col = " + matrixA.getNrCols(), infoText, 29, 1);

      lang.nextStep("" + i + ". Durchlauf");

      infoText.hide();
      valTableObj.highlightCell(1, 1, null, null); // i
      valTableObj.put(1, 1, i.toString(), null, null);

      lang.nextStep();

      valTableObj.unhighlightCell(1, 1, null, null); // i
      valTableObj.highlightCell(0, 1, null, null); // a
      a = 0.;
      source.toggleHighlight(1, 2);

      lang.nextStep();

      valTableObj.put(0, 1, a.toString(), null, null);

      lang.nextStep();

      // nächste Zeile hervorheben und k in Variablentabelle übernehmen
      valTableObj.highlightCell(1, 1, null, null);
      source.unhighlight(2);
      source.unhighlight(4);
      source.highlight(3);

      TrueFalseQuestionModel tfQuestion2 = new TrueFalseQuestionModel(
          "tfQuestion" + x++);
      changeTFQuestion(tfQuestion2,
          "Springt der Algorithmus nun in die Schleife?", "War das abzusehen?",
          "Du solltest nochmal nachschauen, welche Werte i und k haben!", 0 < i);
      lang.addTFQuestion(tfQuestion2);
      for (k = 0; k < i; k++) {
        valTableObj.put(3, 1, k.toString(), null, null);

        lang.nextStep();

        showInfoText(" " + a + " + " + matrixL.getElement(i, k) + " * "
            + matrixL.getElement(i, k) + " = "
            + (a + matrixL.getElement(i, k) * matrixL.getElement(i, k)),
            infoText, 35, 4);
        a = a + matrixL.getElement(i, k) * matrixL.getElement(i, k); // der
                                                                     // eigentliche
                                                                     // Code
        a = Math.rint(a * 100) / 100;
        source.toggleHighlight(3, 4); // Codehervorhebung

        lang.nextStep();

        // infoText.hide();
        valTableObj.highlightCell(1, 1, null, null); // Hervorhebung in der
                                                     // Tabelle der Variablen
        valTableObj.highlightCell(3, 1, null, null); // Hervorhebung in der
                                                     // Tabelle der Variablen
        valTableObj.put(0, 1, a.toString(), null, null);
        matrixL.highlightCell(i, k, null, null); // Hervorhebung in der Matrix
        infoText.hide();

        lang.nextStep();

        matrixL.unhighlightCell(i, k, null, null);

      }

      lang.nextStep();

      // Hervorhebung von i, k zuruecksetzten
      valTableObj.unhighlightCell(1, 1, null, null);
      valTableObj.unhighlightCell(3, 1, null, null);
      // Codezeilenhervorhebung aendern
      source.unhighlight(4);
      source.unhighlight(3);
      source.highlight(6);

      lang.nextStep();

      // eigentlicher Code
      showInfoText(
          " " + matrixA.getElement(i, i) + " - " + a + " = "
              + Math.rint(100 * (matrixA.getElement(i, i) - a) / 100),
          infoText, 22, 6);
      a = matrixA.getElement(i, i) - a;
      a = Math.rint(a * 100) / 100;
      // a in Tabelle hervorheben
      valTableObj.highlightCell(0, 1, null, null);
      // a in Tabelle uebernehmen
      valTableObj.put(0, 1, a.toString(), null, null);
      // Änderung von Hervorhebungen in den Matrizen
      matrixA.highlightCell(i, i, null, null);

      lang.nextStep();

      // Aenderungen bei Hervorhebungen von a, MatrixA und Sourcecode
      valTableObj.unhighlightCell(0, 1, null, null);
      matrixA.unhighlightCell(i, i, null, null);
      source.toggleHighlight(6, 7);
      infoText.hide();

      lang.nextStep();

      valTableObj.highlightCell(0, 1, null, null); // der Wert von a wird
                                                   // hervorgehoben
      createMCQuestion(mcQuestion);
      lang.addMCQuestion(mcQuestion);
      // eigentlicher Code
      if (a < 0) {

        lang.nextStep();

        // Aenderungen bei Hervorhebungen von a, MatrixA und Source
        valTableObj.unhighlightCell(0, 1, null, null);
        matrixA.unhighlightCell(i, i, null, null);
        source.toggleHighlight(7, 8);
      }

      lang.nextStep();

      // Aenderungen bei Hervorhebungen von MatrixA und Sourcecode
      matrixA.unhighlightCell(i, i, null, null);
      source.unhighlight(8);
      source.unhighlight(7);
      source.highlight(10);

      lang.nextStep();

      // Aenderungen bei Hervorhebungen von MatrixL und Wertetabelle
      valTableObj.highlightCell(0, 1, null, null);
      matrixL.highlightCell(i, i, null, null);

      lang.nextStep();

      // eigentlicher Code
      matrixL.put(i, i, Math.rint(100 * Math.sqrt(a)) / 100, null, null);

      lang.nextStep();

      // Aenderungen bei Hervorhebung von a, k, i, MatrixL und Source
      valTableObj.unhighlightCell(0, 1, null, null); // a
      matrixL.unhighlightCell(i, i, null, null);
      source.unhighlight(10);
      source.highlight(11);

      // Infotexteinblendung
      if (i + 1 == matrixA.getNrCols()) {
        showInfoText(
            "Da i + 1 = "
                + col
                + " wird die Schleife übersprungen und springt dann wieder an den anfange der Hautpschleife und dann aus der Haupschleife ans Ende.",
            infoText, 31, 11);

        lang.nextStep();

        infoText.hide();
      } else
        showInfoText(" col = " + matrixA.getNrCols(), infoText, 31, 11);
      TrueFalseQuestionModel tfQuestion3 = new TrueFalseQuestionModel(
          "tfQuestion" + x++);
      changeTFQuestion(tfQuestion3,
          "Springt der Algorithmus nun in die Schleife?", "War das abzusehen?",
          "Du solltest nochmal nachschauen, welche Werte i und k haben!",
          i + 1 < col);
      lang.addTFQuestion(tfQuestion3);
      for (k = i + 1; k < matrixA.getNrCols(); k++) {
        source.highlight(11);
        source.unhighlight(17);

        lang.nextStep();

        infoText.hide();
        valTableObj.highlightCell(1, 1, null, null); // i
        valTableObj.highlightCell(3, 1, null, null); // k
        valTableObj.put(3, 1, k.toString(), null, null); // k in Tabelle
                                                         // aktualisieren

        lang.nextStep();

        valTableObj.unhighlightCell(1, 1, null, null); // i
        valTableObj.unhighlightCell(3, 1, null, null); // k
        valTableObj.highlightCell(4, 1, null, null); // ð
        zwischensumme = 0.;
        // naechste Zeile hervorheben
        source.toggleHighlight(11, 12);

        lang.nextStep();

        valTableObj.put(4, 1, zwischensumme.toString(), null, null); // ð in
                                                                     // Tabelle
                                                                     // eintragen

        lang.nextStep();

        // Aenderungen bei Hervorhebung von j, zwischensumme und Source
        valTableObj.unhighlightCell(3, 1, null, null); // k
        valTableObj.unhighlightCell(4, 1, null, null); // ð
        valTableObj.highlightCell(2, 1, null, null); // j
        source.unhighlight(12);
        source.highlight(13);

        // source code
        TrueFalseQuestionModel tfQuestion4 = new TrueFalseQuestionModel(
            "tfQuestion" + x++);
        changeTFQuestion(tfQuestion4,
            "Springt der Algorithmus nun in die Schleife?",
            "War das abzusehen?",
            "Du solltest nochmal nachschauen, welche Werte i und j haben!",
            0 < i);
        lang.addTFQuestion(tfQuestion4);
        for (j = 0; j < i; j++) {
          valTableObj.highlightCell(2, 1, null, null); // j
          source.unhighlight(14);
          source.highlight(13);

          lang.nextStep();

          valTableObj.put(2, 1, j.toString(), null, null);

          lang.nextStep();

          // Aenderungen bei Hervorhebungen von k, i, Matrix L und source
          source.toggleHighlight(13, 14);

          lang.nextStep();

          valTableObj.highlightCell(3, 1, null, null); // k
          valTableObj.highlightCell(1, 1, null, null); // i
          valTableObj.highlightCell(4, 1, null, null); // zwischensumme
          matrixL.highlightCell(k, j, null, null);
          matrixL.highlightCell(i, j, null, null);
          double hilfsvar = zwischensumme;
          zwischensumme = zwischensumme + matrixL.getElement(k, j)
              * matrixL.getElement(i, j);
          zwischensumme = Math.rint(zwischensumme * 100) / 100;
          showInfoText(" " + hilfsvar + " + " + matrixL.getElement(k, j)
              + " * " + matrixL.getElement(i, j) + " = " + zwischensumme,
              infoText, 36, 14);

          lang.nextStep();

          valTableObj.unhighlightCell(3, 1, null, null); // k
          valTableObj.unhighlightCell(1, 1, null, null); // i
          valTableObj.unhighlightCell(4, 1, null, null); // zwischensumme
          matrixL.unhighlightCell(k, j, null, null);
          matrixL.unhighlightCell(i, j, null, null);
          infoText.hide();

        }

        lang.nextStep();

        valTableObj.unhighlightCell(2, 1, null, null); // j
        source.unhighlight(14);
        source.unhighlight(13);
        source.highlight(16);

        lang.nextStep();

        // Aenderung bei Hervorhebung von A, L, j, zwischensumme und source
        valTableObj.unhighlightCell(2, 1, null, null); // j
        valTableObj.highlightCell(3, 1, null, null); // k
        valTableObj.highlightCell(1, 1, null, null); // i
        valTableObj.highlightCell(4, 1, null, null); // zwischensumme
        matrixL.highlightCell(k, i, null, null);
        matrixA.highlightCell(k, i, null, null);

        lang.nextStep();

        // code
        matrixL.put(k, i,
            Math.rint(100 * (matrixA.getElement(k, i) - zwischensumme)) / 100,
            null, null);
        showInfoText(" " + matrixA.getElement(k, i) + " - " + zwischensumme
            + " = " + matrixL.getElement(k, i), infoText, 34, 17);

        lang.nextStep();

        // Aenderung bei Hervorhebung von A, L und source
        valTableObj.unhighlightCell(4, 1, null, null); // zwischensumme
        matrixA.unhighlightCell(k, i, null, null);
        matrixL.highlightCell(i, i, null, null);
        source.toggleHighlight(16, 17);

        lang.nextStep();

        double matrixLki = matrixL.getElement(k, i);
        // code und änderung in MatrixL
        matrixL.put(k, i, Math.rint(matrixL.getElement(k, i)
            / matrixL.getElement(i, i) * 100) / 100, null, null);
        showInfoText(" " + matrixLki + " / " + matrixL.getElement(i, i) + " = "
            + matrixL.getElement(k, i), infoText, 44, 18);

        lang.nextStep();

        infoText.hide();
        matrixL.unhighlightCell(i, i, null, null);
        matrixL.unhighlightCell(k, i, null, null);
      }
    }

    lang.nextStep();

    infoText.hide();
    source.highlight(20);

    return matrixL;
  }

  /**
   * Diese Methode positioniert einen Infotext
   * 
   * @param text
   *          den Einzugebenden Text
   * @param infoText
   *          das Objekt von Text das die Infos anzeigen soll
   * @param x
   *          angabe wieviele "ASCI"-Zeichen in x Richtung
   * @param y
   *          Angabe welche Sorcezeile
   */
  private void showInfoText(String text, Text infoText, int x, int y) {
    infoText.show();
    infoText.setText(text, null, null);
    try {
      infoText.moveTo("NW", "translate", new Offset(x * 6, y * 15,
          "SourceCode", "NW"), null, null);// new Coordinates(xdistS + x*5 ,100
                                           // + y*15), null, null);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }
  }

  /**
   * Konfigurtiert eine Wahr/Falsch-Frage
   * 
   * @param tfQuestion
   *          das FragenObjekt
   * @param frage
   *          die Frage
   * @param lob
   *          positives Feedback
   * @param kritik
   *          Feedback auf eine falsche Antwort
   * @param richtig
   *          die Antwort auf die Frage
   */
  private void changeTFQuestion(TrueFalseQuestionModel tfQuestion,
      String frage, String lob, String kritik, boolean richtig) {

    tfQuestion.setCorrectAnswer(richtig);
    tfQuestion.setPrompt(frage);
    tfQuestion.setFeedbackForAnswer(!richtig, kritik);
    tfQuestion.setFeedbackForAnswer(richtig, lob);
    tfQuestion.setGroupID("Forschleifen");
    tfQuestion.setNumberOfTries(1);
    tfQuestion.setPointsPossible(1);
  }

  /**
   * Konfigurtiert eine Multiplechoice-Frage
   * 
   * @param tfQuestion
   *          das FragenObjekt
   */
  private void createMCQuestion(MultipleChoiceQuestionModel mcQuestion) {
    mcQuestion.setPrompt("Wozu dient diese if-Abfrage?");
    mcQuestion
        .addAnswer(
            "richtig",
            "Um einen negativen Radikanten zu verhindern.",
            2,
            "Das ist correct. Ein angenemer Nebeneffekt ist das Erkennen fehlender positiver definitheit.");
    mcQuestion
        .addAnswer("falsch2", "Um ein durch Null teilen zu verhindern.", 0,
            "Leider falsch. Es dient dazu einen negativen Radikanten zu verhindern.");
    mcQuestion.addAnswer("falsch3", "Um negative Werte zu verhindern.", 0,
        "Im Prinzip richtig, nur warum will man das verhindern?");
    mcQuestion
        .addAnswer("falsch1",
            "Um zu zeigen, dass die Matrix A positiv definit ist.", 0,
            "Leider falsch. Es dient dazu einen negativen Radikanten zu verhindern.");
    mcQuestion.setGroupID("ifClauses");
    mcQuestion.setNumberOfTries(1);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
    Cholgen saa = new Cholgen();
    saa.init();
    // int col = 5;
    // int[][] matrix = {{4, 2, 0, 0, 0},{2, 4, 1, 0, 0},{0, 1, 6, 0, 0},{0, 1,
    // 6, 7, 0},{0, 1, 6, 0, 8}};
    int[][] matrix = { { 5, 3, 1 }, { 2, 5, 2 }, { 1, 3, 5 } };
    // int[][] matrix = {{4, 2},{2, 4}};
    primitives.put("MatrixA", matrix);
    saa.generate(null, primitives);
    System.out.println(saa.lang.toString());
  }

}