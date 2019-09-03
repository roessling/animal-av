package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Julien Alexander Gedeon <j_gedeon@rbg.informatik.tu-darmstadt.de>
 * 
 *         Verbesserungen: - Titel farbig hinterlegt - Abschnitte für die
 *         Animationsschritte im Inhaltsverzeichnis - Nutzung des
 *         Variablenfensters
 * 
 *         Folgenden Parameter kann der Benutzer Einstellen: - Die Werte a,b für
 *         die der Algorithmus durchgeführt wird - Die Farbe für das
 *         Code-Highlighting
 */

public class EuklidGenerator implements Generator {
  private Language             lang;
  private int                  b;
  private SourceCodeProperties highlightColor;
  private int                  a;

  private int                  stepCount = 0;
  private int                  result    = 0;

  private Variables            vars;

  public EuklidGenerator() {
    init();
  }

  public EuklidGenerator(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();
  }

  public void init() {
    lang = new AnimalScript("Euklid [DE]", "Julien Alexander Gedeon", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    b = (Integer) primitives.get("b");
    highlightColor = (SourceCodeProperties) props
        .getPropertiesByName("highlightColor");
    a = (Integer) primitives.get("a");
    stepCount = computeStepCount(a, b);
    vars = lang.newVariables();
    vars.declare("int", "a", String.valueOf(a));
    vars.declare("int", "b", String.valueOf(b));
    doEuklid(a, b);
    return lang.toString();
  }

  // Hinweis: Wird benötigt, um vorab die Anzahl der Iterationen zu berechnen,
  // damit die Tabelle entsprechend angelegt werden kann
  private int computeStepCount(int a, int b) {
    int i = 0;
    int a2 = a;
    int b2 = b;
    while (b2 != 0) {
      if (a2 > b2) {
        a2 = a2 - b2;
      } else {
        b2 = b2 - a2;
      }
      i++;
    }
    return i;
  }

  public void doEuklid(int a, int b) {

    // Einleitung
    TextProperties headerProp = new TextProperties();
    headerProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30), "Euklidischer Algorithmus", "header",
        null, headerProp);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);

    SourceCodeProperties textProp = new SourceCodeProperties();
    textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));
    textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    SourceCode description = lang.newSourceCode(new Coordinates(20, 50),
        "description", null, textProp);
    description.addCodeLine(
        "Mit Hilfe des euklidischen Algorithmus lässt sich", null, 0, null);
    description.addCodeLine(
        "der größte gemeinsame Teiler (ggT) zweier natürlicher", null, 0, null);
    description.addCodeLine(
        "Zahlen a,b ermitteln. Benannt ist das Verfahren nach", null, 0, null);
    description.addCodeLine("dem griechischen Mathematiker Euklid.", null, 0,
        null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("Es funktioniert folgendermaßen:", null, 0, null);
    description
        .addCodeLine("Es werden wiederholt Subtraktionen durchgeführt. Ist a",
            null, 0, null);
    description.addCodeLine(
        "im aktuellen Schritt größer als b, so erhält a den neuen", null, 0,
        null);
    description.addCodeLine(
        "Wert a-b. Ist das nicht der Fall, so bleibt a unverändert", null, 0,
        null);
    description.addCodeLine(
        "und b bekommt den neuen Wert b-a. Sobald b den Wert 0", null, 0, null);
    description
        .addCodeLine("annimmt, terminiert der Algorithmus und der Wert von a",
            null, 0, null);
    description.addCodeLine("ist dann der größte gemeinsame Teiler.", null, 0,
        null);
    description.addCodeLine("", null, 0, null);
    description
        .addCodeLine(
            "Hinweis: Im Pseudocode nehmen wir an, dass die Startwerte a,b > 0 sind.",
            null, 0, null);

    lang.nextStep("Einleitung");
    description.hide();

    // Algorithmus
    String inputArray[][] = new String[stepCount + 2][2];
    for (int i = 0; i < (stepCount + 2); i++) {
      inputArray[i][0] = "";
      inputArray[i][1] = "";
    }
    inputArray[0][0] = "a";
    inputArray[0][1] = "b";
    inputArray[1][0] = Integer.toString(a);
    inputArray[1][1] = Integer.toString(b);
    MatrixProperties matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    StringMatrix matrix = lang.newStringMatrix(new Coordinates(20, 80),
        inputArray, "matrix", null, matrixProps);

    SourceCodeProperties codeProps = new SourceCodeProperties();
    codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 16));
    codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        highlightColor.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));

    codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    SourceCode pseudocode = lang.newSourceCode(new Coordinates(20,
        ((30 * stepCount)) + 150), "sourceCode", null, codeProps);
    pseudocode.addCodeLine("EUCLID (a,b)", null, 0, null); // 0
    pseudocode.addCodeLine("while b != 0", null, 0, null); // 1
    pseudocode.addCodeLine("if a > b", null, 1, null); // 2
    pseudocode.addCodeLine("then a <- a-b", null, 2, null); // 3
    pseudocode.addCodeLine("else b <- b-a", null, 2, null); // 4
    pseudocode.addCodeLine("return a", null, 0, null); // 5

    pseudocode.highlight(0);
    lang.nextStep("Start Algorithmus");
    pseudocode.unhighlight(0);

    int i = 1;
    int changedRow;
    int changedCol;

    while ((Integer.valueOf(matrix.getElement(i, 1))) != 0) {
      pseudocode.highlight(1);
      lang.nextStep();
      pseudocode.toggleHighlight(1, 2);

      if (Integer.valueOf(matrix.getElement(i, 0)) > Integer.valueOf(matrix
          .getElement(i, 1))) {
        lang.nextStep();
        pseudocode.toggleHighlight(2, 3);
        int newValue = Integer.valueOf(matrix.getElement(i, 0))
            - Integer.valueOf(matrix.getElement(i, 1));
        matrix.put(i + 1, 0, Integer.toString(newValue), null, null);
        matrix.highlightElem(i + 1, 0, null, null);
        changedRow = i + 1;
        changedCol = 0;
        matrix.put(i + 1, 1, matrix.getElement(i, 1), null, null);
        vars.set("a", String.valueOf(newValue));
      } else {
        lang.nextStep();
        pseudocode.toggleHighlight(2, 4);
        int newValue = Integer.valueOf(matrix.getElement(i, 1))
            - Integer.valueOf(matrix.getElement(i, 0));
        matrix.put(i + 1, 1, Integer.toString(newValue), null, null);
        matrix.highlightElem(i + 1, 1, null, null);
        changedRow = i + 1;
        changedCol = 1;
        matrix.put(i + 1, 0, matrix.getElement(i, 0), null, null);
        vars.set("b", String.valueOf(newValue));
      }
      lang.nextStep();
      pseudocode.unhighlight(3);
      pseudocode.unhighlight(4);
      pseudocode.highlight(1);
      matrix.unhighlightCell(changedRow, changedCol, null, null);
      i++;
    }

    lang.nextStep();
    pseudocode.toggleHighlight(1, 5);
    matrix.highlightElem(stepCount + 1, 0, null, null);
    result = Integer.valueOf(matrix.getElement(stepCount + 1, 0));
    lang.nextStep("Terminierung Algorithmus");
    matrix.hide();
    pseudocode.hide();

    // Abschluss-"Folie"
    SourceCode endText = lang.newSourceCode(new Coordinates(20, 50),
        "description", null, textProp);
    endText.addCodeLine("Anzahl der Iterationen: " + stepCount, null, 0, null);
    endText.addCodeLine(
        "Nach der Terminierung steht das Ergebnis (der ggT) in der a-Spalte",
        null, 0, null);
    endText.addCodeLine("Also hier: ggT(" + a + "," + b + ") = " + result,
        null, 0, null);
    lang.nextStep("Fazit");
  }

  public String getName() {
    return "Euklid [DE]";
  }

  public String getAlgorithmName() {
    return "Euklidischer Algorithmus";
  }

  public String getAnimationAuthor() {
    return "Julien Alexander Gedeon";
  }

  public String getDescription() {
    return "Mit Hilfe des euklidschen Algorithmus l&auml;sst sich der gr&ouml;&szlig;te gemeinsame Teiler (ggT) zweier nat&uuml;rlicher Zahlen ermitteln."
        + "\n"
        + "Benannt ist das Verfahren nach dem griechischen Mathematiker Euklid."
        + "\n"
        + "Es funktioniert folgenderma&szlig;en:"
        + "\n"
        + "Es werden wiederholt Subtraktionen durchgef&uuml;hrt. Ist a im aktuellen Schritt gr&ouml;&szlig;er als b, so erh&auml;lt a den neuen Wert a-b."
        + "\n"
        + "Ist das nicht der Fall, so bleibt a unver&auml;ndert und b bekommt den neuen Wert b-a. Sobald b den Wert 0 annimt, terminiert der Algorithmus"
        + "\n"
        + "und der Wert von a ist dann der gr&ouml;&szlig;te gemeinsame Teiler."
        + "\n"
        + "\n"
        + "Hinweis: Im Pseudocode nehmen wir an, dass die Startwerte a,b > 0 sind.";
  }

  public String getCodeExample() {
    return "EUKLID(a,b)" + "\n" + "while b != 0" + "\n" + "  if a > b" + "\n"
        + "    then a <- a - b" + "\n" + "    else b <- b - a" + "\n"
        + "return a";
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
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}