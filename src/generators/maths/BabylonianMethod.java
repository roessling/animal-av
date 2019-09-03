package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

public class BabylonianMethod implements Generator {
  private Language             lang;
  private MatrixProperties     tableProps;
  private TextProperties       textPropsTitle;
  private TextProperties       textPropsHeadlines;
  private RectProperties       rectangleProps;
  private SourceCodeProperties finalRemarkProps;
  private double               radicant;
  private int                  steps;
  private SourceCodeProperties codeProperties;

  public void init() {
    lang = new AnimalScript("Babylonian Root",
        "Benjamin Schiller,Philipp Duennebeil", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    tableProps = (MatrixProperties) props.getPropertiesByName("tableProps");
    textPropsTitle = (TextProperties) props
        .getPropertiesByName("textPropsTitle");
    textPropsHeadlines = (TextProperties) props
        .getPropertiesByName("textPropsHeadlines");
    rectangleProps = (RectProperties) props
        .getPropertiesByName("rectangleProps");
    radicant = (Double) primitives.get("radicant");
    steps = (Integer) primitives.get("steps");
    codeProperties = (SourceCodeProperties) props
        .getPropertiesByName("codeProperties");
    finalRemarkProps = new SourceCodeProperties("finalRemarkProps");

    babylonian();

    return lang.toString();
  }

  private void babylonian() {

    codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    finalRemarkProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    finalRemarkProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);
    tableProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));
    // tableProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
    // Color.green);
    textPropsHeadlines.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 14));
    textPropsTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 18));
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    cp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));

    // Define field with header of program
    lang.newText(new Coordinates(20, 30), "Babylonian Method", "header", null,
        textPropsTitle);

    // Define and initialize description field
    SourceCode description = lang.newSourceCode(new Offset(0, 60, "header",
        AnimalScript.DIRECTION_NW), "description", null, codeProperties);
    description
        .addCodeLine(
            "Perhaps the first algorithm used for approximating square root of S is known as the",
            null, 0, null);
    description
        .addCodeLine(
            "''Babylonian method'', named after the Babylonians, or ''Hero's method'', named after",
            null, 0, null);
    description
        .addCodeLine(
            "the first-century Greek mathematician Hero of Alexandria who gave the first explicit ",
            null, 0, null);
    description
        .addCodeLine(
            "description of the method.[2] It can be derived from (but predates by 16 centuries)",
            null, 0, null);
    description
        .addCodeLine(
            "Newton's method (see below). The basic idea is that if x is an overestimate to the",
            null, 0, null);
    description
        .addCodeLine(
            "square root of a non-negative real number S then (S / x), will be an ",
            null, 0, null);
    description
        .addCodeLine(
            "underestimate and so the average of these two numbers may reasonably be ",
            null, 0, null);
    description
        .addCodeLine(
            "expected to provide a better approximation (though the formal proof of that",
            null, 0, null);
    description
        .addCodeLine(
            "assertion depends on the inequality of arithmetic and geometric means that ",
            null, 0, null);
    description
        .addCodeLine(
            "shows this average is always an overestimate of the square root, as noted in the ",
            null, 0, null);
    description.addCodeLine(
        "article on square roots, thus assuring convergence).", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description
        .addCodeLine(
            "Source: http://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Babylonian_method",
            null, 0, null);

    lang.nextStep();

    // hide description
    description.hide();

    // Define field with given Parameters and header of the field
    lang.newText(new Offset(0, 60, "header", AnimalScript.DIRECTION_NW),
        "General remarks:", "parameterHeader", null, textPropsHeadlines);
    SourceCode parameter = lang.newSourceCode(new Offset(0, 5,
        "parameterHeader", AnimalScript.DIRECTION_SW), "parameter", null,
        codeProperties);
    parameter.addCodeLine("Formula  : x(N+1) = ( x(N) + y(N) ) / 2", null, 0,
        null);
    parameter.addCodeLine("                       y(N) = a / x(N)", null, 0,
        null);
    parameter.addCodeLine("                       x(0) = a", null, 0, null);
    parameter.addCodeLine("                       y(0) = 1", null, 0, null);
    parameter.addCodeLine("Parameter: a = " + radicant, null, 0, null);

    // Define field for calculation
    lang.newText(new Offset(0, 10, "parameter", AnimalScript.DIRECTION_SW),
        "Calculation:", "calculationHeader", null, textPropsHeadlines);
    SourceCode calculation = lang.newSourceCode(new Offset(0, 5,
        "calculationHeader", AnimalScript.DIRECTION_SW), "calculation", null,
        codeProperties);
    calculation.addCodeLine("", null, 0, null);

    // create StringMatrix and initialize it
    String[][] content = new String[steps + 2][6];
    content[0][0] = "N";
    content[0][1] = "x";
    content[0][2] = "sqrt(x)";
    content[0][3] = "abs. error";
    content[0][4] = "rel. error";
    content[0][5] = "y";

    for (int i = 0; i < 6; i++) {
      content[steps + 1][i] = "";
    }
    for (int i = 1; i < steps + 1; i++) {
      content[i][0] = "?";
      content[i][1] = "?";
      content[i][2] = "?";
      content[i][3] = "?";
      content[i][4] = "?";
      content[i][5] = "?";
    }

    lang.newText(new Offset(0, 100, "parameter", AnimalScript.DIRECTION_SW),
        "Table with results", "tableHeader", null, textPropsHeadlines);
    StringMatrix sm = lang.newStringMatrix(new Offset(0, 5, "tableHeader",
        AnimalScript.DIRECTION_SW), content, "sm", null, tableProps);

    // define counter for matrix
    TwoValueCounter counter = lang.newCounter(sm);

    // create view for counter
    lang.newText(new Offset(250, 0, "parameterHeader",
        AnimalScript.DIRECTION_NE), "Counter", "headerCounter", null,
        textPropsHeadlines);
    lang.newCounterView(counter, new Offset(0, 5, "headerCounter",
        AnimalScript.DIRECTION_SW), cp, true, true);

    double scale = 500.0 / radicant;

    int xValue = (int) Math.round(radicant * scale);
    int yValue = (int) Math.round(1 * scale);

    lang.newText(
        new Offset(0, 100, "headerCounter", AnimalScript.DIRECTION_SW),
        "Draft", "rectHeader", null, textPropsHeadlines);
    Rect rect = lang.newRect(new Offset(0, 5, "rectHeader",
        AnimalScript.DIRECTION_SW), new Offset(xValue, 5 + yValue,
        "rectHeader", AnimalScript.DIRECTION_SW), "rect", null, rectangleProps);

    TriangleProperties triangleProps = new TriangleProperties("triangleProps");
    triangleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    triangleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.black);

    Node[] vertices = new Node[] {
        new Offset(20, 0, "rect", AnimalScript.DIRECTION_NE),
        new Offset(20, 0, "rect", AnimalScript.DIRECTION_SE) };
    Polyline vert = lang.newPolyline(vertices, "vert", null);
    Node[] vertices2 = new Node[] {
        new Offset(0, 20, "rect", AnimalScript.DIRECTION_SW),
        new Offset(0, 20, "rect", AnimalScript.DIRECTION_SE) };
    Polyline hori = lang.newPolyline(vertices2, "hori", null);
    Triangle vertTop = lang.newTriangle(new Offset(20, 0, "rect",
        AnimalScript.DIRECTION_NE), new Offset(10, 10, "rect",
        AnimalScript.DIRECTION_NE), new Offset(30, 10, "rect",
        AnimalScript.DIRECTION_NE), "vertTop", null, triangleProps);
    Triangle vertBot = lang.newTriangle(new Offset(20, 0, "rect",
        AnimalScript.DIRECTION_SE), new Offset(10, -10, "rect",
        AnimalScript.DIRECTION_SE), new Offset(30, -10, "rect",
        AnimalScript.DIRECTION_SE), "vertBot", null, triangleProps);
    Triangle horiLeft = lang.newTriangle(new Offset(0, 20, "rect",
        AnimalScript.DIRECTION_SW), new Offset(10, 10, "rect",
        AnimalScript.DIRECTION_SW), new Offset(10, 30, "rect",
        AnimalScript.DIRECTION_SW), "horiLeft", null, triangleProps);
    Triangle horiRight = lang.newTriangle(new Offset(0, 20, "rect",
        AnimalScript.DIRECTION_SE), new Offset(-10, 10, "rect",
        AnimalScript.DIRECTION_SE), new Offset(-10, 30, "rect",
        AnimalScript.DIRECTION_SE), "horiRight", null, triangleProps);
    Text horiText = lang.newText(
        new Offset((int) (Math.round(radicant * scale / 2.0) - 10), 5, "hori",
            AnimalScript.DIRECTION_NW), "" + radicant, "horiText", null,
        textPropsHeadlines);
    Text vertText = lang.newText(
        new Offset(10, (int) (Math.round(scale / 2.0) - 10), "vert",
            AnimalScript.DIRECTION_NW), "" + 1.0, "vertText", null,
        textPropsHeadlines);

    lang.nextStep();

    for (int i = 1; i < steps + 1; i++) {
      double xN = 0, oldXN, oldYN;
      ;
      calculation.hide();
      calculation = lang.newSourceCode(new Offset(0, 10, "parameter",
          AnimalScript.DIRECTION_SW), "calculation", null, codeProperties);

      if (i == 1) {
        oldXN = radicant;
        oldYN = 1.0;
        xN = round3(radicant);
        calculation.addCodeLine("x(" + (i - 1) + ") = " + radicant, null, 0,
            null);
      } else {
        oldXN = Double.valueOf(sm.getElement(i - 1, 1));
        oldYN = Double.valueOf(sm.getElement(i - 1, 5));
        xN = round3((oldXN + (radicant / oldXN)) / 2.0);

        calculation.addCodeLine("x(" + (i - 1) + ") = ( x(" + (i - 2)
            + ") + y(" + (i - 2) + ") ) / 2", null, 0, null);
        calculation.addCodeLine("        = ( " + oldXN + " + " + oldYN
            + " ) / 2", null, 0, null);
        calculation.addCodeLine("        = " + xN, null, 0, null);
        sm.unhighlightCellColumnRange(i - 1, 0, 5, null, null);
      }

      sm.highlightCellColumnRange(i, 0, 5, null, null);

      double yN = round3(radicant / xN);

      int dx = (int) Math.round(-(oldXN - xN) * scale);
      int dy = (int) Math.round((yN - oldYN) * scale);

      rect.moveBy("translate #2", dx, dy, new MsTiming(10), new MsTiming(1000));
      vert.moveBy("translate #1", dx, 0, new MsTiming(10), new MsTiming(1000));
      vert.moveBy("translate #2", dx, dy, new MsTiming(10), new MsTiming(1000));
      hori.moveBy("translate #1", 0, dy, new MsTiming(10), new MsTiming(1000));
      hori.moveBy("translate #2", dx, dy, new MsTiming(10), new MsTiming(1000));
      vertTop.moveBy("translate", dx, 0, new MsTiming(10), new MsTiming(1000));
      vertBot.moveBy("translate", dx, dy, new MsTiming(10), new MsTiming(1000));
      horiLeft.moveBy("translate", 0, dy, new MsTiming(10), new MsTiming(1000));
      horiRight.moveBy("translate", dx, dy, new MsTiming(10),
          new MsTiming(1000));
      vertText.moveBy("translate", dx, (int) Math.round(dy / 2.0),
          new MsTiming(10), new MsTiming(1000));
      vertText.setText("" + yN, new MsTiming(10), new MsTiming(1000));
      horiText.moveBy("translate", (int) Math.round(dx / 2.0), dy,
          new MsTiming(10), new MsTiming(1000));
      horiText.setText("" + xN, new MsTiming(10), new MsTiming(1000));

      double absErr = round3(Math.abs(xN - Math.sqrt(radicant)));

      sm.put(i, 0, "" + (i - 1), null, null);
      sm.put(i, 1, "" + xN, null, null);
      sm.put(i, 2, "" + round3(Math.sqrt(radicant)), null, null);
      sm.put(i, 3, "" + absErr, null, null);
      sm.put(i, 4, "" + round3(absErr / Math.sqrt(radicant)), null, null);
      sm.put(i, 5, "" + yN, null, null);

      lang.nextStep();
    }

    lang.newText(new Offset(0, 80, "rect", AnimalScript.DIRECTION_SW),
        "Final Remark", "finalRemarkHeader", null, textPropsHeadlines);
    SourceCode finalRemark = lang.newSourceCode(new Offset(0, 0,
        "finalRemarkHeader", AnimalScript.DIRECTION_SW), "finalRemark", null,
        finalRemarkProps);
    finalRemark.addCodeLine(
        "The rectangle with lengths x and y is almost a square now.", null, 0,
        null);
    finalRemark.addCodeLine(
        "Thus x and y respectively are almost the square roots of " + radicant
            + ",", null, 0, null);
    finalRemark.addCodeLine("because the covered area remained the same.",
        null, 0, null);
  }

  public String getName() {
    return "Babylonian Method";
  }

  public String getAlgorithmName() {
    return "Babylonian Method";
  }

  public String getAnimationAuthor() {
    return "Benjamin Schiller, Philipp Dünnebeil";
  }

  public String getDescription() {
    return "Perhaps the first algorithm used for approximating square root of S is known as the "
        + "\n"
        + "\"Babylonian method\", named after the Babylonians, or \"Hero's method\", named after "
        + "\n"
        + "the first-century Greek mathematician Hero of Alexandria who gave the first explicit "
        + "\n"
        + "description of the method.[2] It can be derived from (but predates by 16 centuries) "
        + "\n"
        + "Newton's method (see below). The basic idea is that if x is an overestimate to the "
        + "\n"
        + "square root of a non-negative real number S then (S / x), will be an "
        + "\n"
        + "underestimate and so the average of these two numbers may reasonably be "
        + "\n"
        + "expected to provide a better approximation (though the formal proof of that "
        + "\n"
        + "assertion depends on the inequality of arithmetic and geometric means that "
        + "\n"
        + "shows this average is always an overestimate of the square root, as noted in the "
        + "\n"
        + "article on square roots, thus assuring convergence)."
        + "\n"
        + "Source: http://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Babylonian_method";
  }

  private double round3(double value) {
    return (Math.round(value * 1000.0) / 1000.0);
  }

  public String getCodeExample() {
    return "";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}