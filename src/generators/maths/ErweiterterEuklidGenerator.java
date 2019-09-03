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
 * @author Julien Alexander Gedeon <j_gedeon@rbg.informatik.tu-darmstadt.de>,
 *         1412590
 */

public class ErweiterterEuklidGenerator implements Generator {
  private Language             lang;
  private SourceCodeProperties highlightColor;

  private int                  b;
  private int                  a;

  private int                  stepCount = 0;
  private int                  ggT;
  private int                  x;
  private int                  y;

  private Variables            vars;

  public ErweiterterEuklidGenerator() {
    init();
  }

  public ErweiterterEuklidGenerator(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();
  }

  public void init() {
    lang = new AnimalScript("Erweiterter Euklid [DE]",
        "Julien Alexander Gedeon", 800, 600);
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
    vars.declare("int", "q", "");
    vars.declare("int", "u", String.valueOf(1));
    vars.declare("int", "s", String.valueOf(0));
    vars.declare("int", "v", String.valueOf(0));
    vars.declare("int", "t", String.valueOf(1));
    doEuklid(a, b);
    return lang.toString();
  }

  // Hinweis: Wird benötigt, um vorab die Anzahl der Iterationen zu berechnen,
  // damit die Tabelle entsprechend angelegt werden kann
  private int computeStepCount(int a, int b) {
    int i = 0;
    int q;
    int r;
    int b2 = b;
    while (b2 > 0) {
      int a2 = a;
      q = a2 / b2;
      r = a2 - q * b2;
      a2 = b2;
      b2 = r;
      i++;
    }
    return i;
  }

  public void doEuklid(int a, int b) {
    int q, r, u, s, v, t;
    u = 1;
    s = 0;
    v = 0;
    t = 1;

    int a2 = a;
    int initialA = a2;
    int b2 = b;
    int initialB = b2;

    // Einleitung
    TextProperties headerProp = new TextProperties();
    headerProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30),
        "Erweiterter Euklidischer Algorithmus", "header", null, headerProp);

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
    description.addCodeLine(
        "Zusätzlich berechnet der erweiterte Euklidische Algorithmus", null, 0,
        null);
    description.addCodeLine("zwei ganze Zahlen x,y, so dass gilt:", null, 0,
        null);
    description.addCodeLine("ggT(a,b) = x * a + y * b", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description
        .addCodeLine(
            "Hinweis: Im Pseudocode nehmen wir an, dass für die Startwerte gilt: a > b > 0.",
            null, 0, null);

    lang.nextStep("Einleitung");
    description.hide();

    // Algorithmus
    String inputArray[][] = new String[stepCount + 2][7];
    for (int i = 0; i < (stepCount + 2); i++) {
      inputArray[i][0] = "";
      inputArray[i][1] = "";
      inputArray[i][2] = "";
      inputArray[i][3] = "";
      inputArray[i][4] = "";
      inputArray[i][5] = "";
      inputArray[i][6] = "";
    }
    inputArray[0][0] = "a";
    inputArray[0][1] = "b";
    inputArray[0][2] = "q";
    inputArray[0][3] = "u";
    inputArray[0][4] = "s";
    inputArray[0][5] = "v";
    inputArray[0][6] = "t";

    inputArray[1][0] = Integer.toString(a2);
    inputArray[1][1] = Integer.toString(b2);

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
    pseudocode.addCodeLine("ERWEITERTER EUKLID (a,b)", null, 0, null); // 0
    pseudocode.addCodeLine("u <- 1", null, 0, null); // 1
    pseudocode.addCodeLine("s <- 0", null, 0, null); // 2
    pseudocode.addCodeLine("v <- 0", null, 0, null); // 3
    pseudocode.addCodeLine("t <- 1", null, 0, null); // 4
    pseudocode.addCodeLine("while b > 0", null, 0, null); // 5
    pseudocode.addCodeLine("q <- a / b", null, 1, null); // 6
    pseudocode.addCodeLine("a <- b", null, 1, null); // 7
    pseudocode.addCodeLine("b <- a - q*b", null, 1, null); // 8
    pseudocode.addCodeLine("u <- s", null, 1, null); // 9
    pseudocode.addCodeLine("s <- u - q*s", null, 1, null); // 10
    pseudocode.addCodeLine("v <- t", null, 1, null); // 11
    pseudocode.addCodeLine("t <- v - q*t", null, 1, null); // 12
    pseudocode.addCodeLine("ggT(a,b) = a", null, 0, null); // 13
    pseudocode.addCodeLine("x = u", null, 0, null); // 14
    pseudocode.addCodeLine("y = v", null, 0, null); // 15

    lang.nextStep("Start Algorithmus");
    pseudocode.highlight(0);

    lang.nextStep();
    pseudocode.toggleHighlight(0, 1);
    matrix.put(1, 3, "1", null, null);
    matrix.highlightElem(1, 3, null, null);
    lang.nextStep("Initialisierung");

    pseudocode.toggleHighlight(1, 2);
    matrix.put(1, 4, "0", null, null);
    matrix.unhighlightElem(1, 3, null, null);
    matrix.highlightElem(1, 4, null, null);
    lang.nextStep();

    pseudocode.toggleHighlight(2, 3);
    matrix.put(1, 5, "0", null, null);
    matrix.unhighlightElem(1, 4, null, null);
    matrix.highlightElem(1, 5, null, null);
    lang.nextStep();

    pseudocode.toggleHighlight(3, 4);
    matrix.put(1, 6, "1", null, null);
    matrix.unhighlightElem(1, 5, null, null);
    matrix.highlightElem(1, 6, null, null);
    lang.nextStep();
    matrix.unhighlightElem(1, 6, null, null);

    int i = 1;

    while ((Integer.valueOf(matrix.getElement(i, 1))) > 0) {
      pseudocode.unhighlight(4);
      pseudocode.highlight(5);
      lang.nextStep("Iterationen");
      pseudocode.toggleHighlight(5, 6);

      q = a2 / b2;
      vars.set("q", String.valueOf(q));
      matrix.put(i + 1, 2, Integer.toString(q), null, null);
      matrix.highlightElem(i + 1, 2, null, null);
      lang.nextStep();

      pseudocode.toggleHighlight(6, 7);
      r = a2 - q * b2;
      a2 = b2;
      vars.set("a", String.valueOf(a2));
      b2 = r;
      vars.set("b", String.valueOf(b2));
      matrix.put(i + 1, 0, Integer.toString(a2), null, null);
      matrix.unhighlightElem(i + 1, 2, null, null);
      matrix.highlightElem(i + 1, 0, null, null);
      lang.nextStep();
      pseudocode.toggleHighlight(7, 8);
      matrix.put(i + 1, 1, Integer.toString(b2), null, null);
      matrix.unhighlightElem(i + 1, 0, null, null);
      matrix.highlightElem(i + 1, 1, null, null);
      lang.nextStep();

      r = u - q * s;
      u = s;
      vars.set("u", String.valueOf(u));
      s = r;
      vars.set("s", String.valueOf(s));
      pseudocode.toggleHighlight(8, 9);
      matrix.put(i + 1, 3, Integer.toString(u), null, null);
      matrix.unhighlightElem(i + 1, 1, null, null);
      matrix.highlightElem(i + 1, 3, null, null);
      lang.nextStep();
      pseudocode.toggleHighlight(9, 10);
      matrix.put(i + 1, 4, Integer.toString(s), null, null);
      matrix.unhighlightElem(i + 1, 3, null, null);
      matrix.highlightElem(i + 1, 4, null, null);
      lang.nextStep();

      r = v - q * t;
      v = t;
      vars.set("v", String.valueOf(v));
      t = r;
      vars.set("t", String.valueOf(t));
      pseudocode.toggleHighlight(10, 11);
      matrix.put(i + 1, 5, Integer.toString(v), null, null);
      matrix.unhighlightElem(i + 1, 4, null, null);
      matrix.highlightElem(i + 1, 5, null, null);
      lang.nextStep();
      pseudocode.toggleHighlight(11, 12);
      matrix.put(i + 1, 6, Integer.toString(t), null, null);
      matrix.unhighlightElem(i + 1, 5, null, null);
      matrix.highlightElem(i + 1, 6, null, null);

      lang.nextStep();
      pseudocode.unhighlight(12);
      pseudocode.highlight(5);
      i++;
    }

    lang.nextStep();
    matrix.unhighlightElem(i, 6, null, null);
    pseudocode.toggleHighlight(5, 13);
    pseudocode.highlight(14);
    pseudocode.highlight(15);
    matrix.highlightElem(stepCount + 1, 0, null, null);
    matrix.highlightElem(stepCount + 1, 3, null, null);
    matrix.highlightElem(stepCount + 1, 5, null, null);
    ggT = Integer.valueOf(matrix.getElement(stepCount + 1, 0));
    x = Integer.valueOf(matrix.getElement(stepCount + 1, 3));
    y = Integer.valueOf(matrix.getElement(stepCount + 1, 5));
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
    endText.addCodeLine("Also hier: ggT(" + initialA + "," + initialB + ") = "
        + ggT, null, 0, null);
    endText.addCodeLine("Ferner ist x = " + x + " und y = " + y + ", also",
        null, 0, null);
    endText.addCodeLine("ggT(" + initialA + "," + initialB + ") = " + ggT
        + " = " + x + " * " + initialA + " + " + y + " * " + initialB, null, 0,
        null);
    lang.nextStep("Fazit");
  }

  public String getName() {
    return "Erweiterter Euklid [DE]";
  }

  public String getAlgorithmName() {
    return "Erweiterter Euklidischer Algorithmus";
  }

  public String getAnimationAuthor() {
    return "Julien Alexander Gedeon";
  }

  public String getDescription() {
    return "Mit Hilfe des euklidschen Algorithmus l&auml;sst sich der gr&ouml;&szlig;te gemeinsame Teiler (ggT) zweier nat&uuml;rlicher Zahlen ermitteln."
        + "\n"
        + "Benannt ist das Verfahren nach dem griechischen Mathematiker Euklid."
        + "\n"
        + "Zus&auml;tzlich berechnet der erweiterte Euklidische Algorithmus zwei ganze Zahlen x,y, so dass gilt:"
        + "\n"
        + "ggT(a,b) = x * a + y * b"
        + "\n"
        + "\n"
        + "Hinweis: Im Pseudocode nehmen wir an, dass für die Startwerte gilt: a > b > 0.";
  }

  public String getCodeExample() {
    return "ERWEITERTER EUKLID (a,b)" + "\n" + "u <- 1" + "\n" + "s <- 0"
        + "\n" + "v <- 0" + "\n" + "t <- 1" + "\n" + "while b > 0" + "\n"
        + "  q <- a / b" + "\n" + "  a <- b" + "\n" + "  b <- a - q*b" + "\n"
        + " u <- s" + "\n" + "  s <- u + q*s" + "\n" + "  v <- t" + "\n"
        + "  t <- v - q*t" + "\n" + "ggT(a,b) = a" + "\n" + "x = u" + "\n"
        + "y = v";
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