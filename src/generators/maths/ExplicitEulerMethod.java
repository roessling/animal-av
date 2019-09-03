package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import net.astesana.javaluator.DoubleEvaluator;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class ExplicitEulerMethod implements Generator {
  private Language             lang;
  private MatrixProperties     matrixProps;
  private double               t0;
  private TextProperties       textPropsTitle;
  private SourceCodeProperties finalRemarkProps;
  private TextProperties       textPropsHeadlines;
  private double               stepSize;
  private SourceCodeProperties valueCalculationFieldProps;
  private int                  numberOfSteps;
  private double               x0;
  private String               function;

  public void init() {
    lang = new AnimalScript("Explicit Euler method",
        "Benjamin Schiller,Philipp Duennebeil", 800, 600);
    lang.setStepMode(true);
  }

  public ExplicitEulerMethod() {
  };

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
    t0 = (Double) primitives.get("t0");
    textPropsTitle = (TextProperties) props
        .getPropertiesByName("textPropsTitle");
    finalRemarkProps = (SourceCodeProperties) props
        .getPropertiesByName("finalRemarkProps");
    textPropsHeadlines = (TextProperties) props
        .getPropertiesByName("textPropsHeadlines");
    stepSize = (Double) primitives.get("stepSize");
    valueCalculationFieldProps = (SourceCodeProperties) props
        .getPropertiesByName("valueCalculationFieldProps");
    numberOfSteps = (Integer) primitives.get("numberOfSteps");
    x0 = (Double) primitives.get("x0");
    function = (String) primitives.get("function");

    euler(function, stepSize, x0, t0, numberOfSteps);

    return lang.toString();
  }

  public void euler(String fct, double h, double x0, double t0, int steps) {

    // set the visual properties for all fields
    // valueCalculationFieldProps = new SourceCodeProperties();
    // valueCalculationFieldProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
    // Color.BLACK);
    valueCalculationFieldProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 14));
    //
    // valueCalculationFieldProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.GREEN);
    // valueCalculationFieldProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
    // Color.BLACK);
    //
    // finalRemarkProps = new SourceCodeProperties();
    // finalRemarkProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
    // Color.BLACK);
    finalRemarkProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    //
    // finalRemarkProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.GREEN);
    // finalRemarkProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new
    // Color(66, 159, 78));
    //
    // matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    // matrixProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
    // matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
    // Color.GRAY);
    // matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
    // Color.GRAY);
    //
    //
    // textPropsHeadlines = new TextProperties("textProps");
    textPropsHeadlines.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 14));
    //
    // textPropsTitle = new TextProperties("textPropsHeader");
    textPropsTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 18));

    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    cp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));

    // Define field with header of program
    lang.newText(new Coordinates(20, 30), "Explicit Euler method", "header",
        null, textPropsTitle);

    // Define and initialize description field
    SourceCode description = lang.newSourceCode(new Offset(0, 60, "header",
        AnimalScript.DIRECTION_NW), "description", null,
        valueCalculationFieldProps);
    description.addCodeLine(
        "In mathematics and computational science, the Euler method", null, 0,
        null);
    description
        .addCodeLine(
            "is a first-order numerical procedure for solving ordinary differential",
            null, 0, null);
    description.addCodeLine(
        "equations (ODEs) with a given initial value. It is the most basic",
        null, 0, null);
    description.addCodeLine(
        "explicit method for numerical integration of ordinary differential",
        null, 0, null);
    description
        .addCodeLine(
            "equations and is the simplest Runge-Kutta method. The Euler method is",
            null, 0, null);
    description
        .addCodeLine(
            "named after Leonhard Euler, who treated it in his book 'Institutionum",
            null, 0, null);
    description
        .addCodeLine(
            "calculi integralis' (published 1768-70). The Euler method is a first-order",
            null, 0, null);
    description
        .addCodeLine(
            "method, which means that the local error (error per step) is proportional",
            null, 0, null);
    description
        .addCodeLine(
            "to the square of the step size, and the global error (error at a given time)",
            null, 0, null);
    description
        .addCodeLine(
            "is proportional to the step size. It also suffers from stability problems.",
            null, 0, null);
    description.addCodeLine(
        "For these reasons, the Euler method is not often used in practice.",
        null, 0, null);
    description.addCodeLine(
        "It serves as the basis to construct more complicated methods.", null,
        0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine(
        "Source: http://en.wikipedia.org/wiki/Euler_method", null, 0, null);

    lang.nextStep();

    // hide description
    description.hide();

    // Define field with given Parameters and header of the field
    SourceCode parameter = lang.newSourceCode(new Offset(0, 60, "header",
        AnimalScript.DIRECTION_NW), "parameter", null,
        valueCalculationFieldProps);
    parameter.addCodeLine(
        "Explicit Euler Formula: xN = x(N-1) + h * f(x(N-1))", null, 0, null);
    parameter.addCodeLine("Function: x'(t) = f(x) = " + fct, null, 0, null);
    parameter.addCodeLine("Step size: h = " + h, null, 0, null);
    parameter.addCodeLine("Initial Value: x0 = " + x0, null, 0, null);
    parameter.addCodeLine("Start Value: t0 = " + t0, null, 0, null);
    parameter.addCodeLine("Number of steps: " + steps, null, 0, null);
    lang.newText(new Offset(0, -25, "parameter", AnimalScript.DIRECTION_NW),
        "Parameters", "headerParam", null, textPropsHeadlines);

    // create StringMatrix and initialize it
    String[][] content = new String[4][steps + 2];
    content[0][0] = "N";
    content[1][0] = "tN";
    content[2][0] = "xN";
    content[3][0] = "";
    content[0][1] = "0";
    content[1][1] = "" + t0;
    content[2][1] = "" + x0;
    content[3][1] = "";

    for (int i = 1; i < steps + 2; i++) {
      content[0][i] = "" + (i - 1);
      content[1][i] = "?";
      content[2][i] = "?";
      content[3][i] = "";
    }
    StringMatrix sm = lang.newStringMatrix(new Offset(0, 45, "parameter",
        AnimalScript.DIRECTION_SW), content, "matrix", null, matrixProps);
    lang.newText(new Offset(0, -20, "matrix", AnimalScript.DIRECTION_NW),
        "Result", "headerSM", null, textPropsHeadlines);

    // define counter for matrix
    TwoValueCounter counter = lang.newCounter(sm);

    // create view for counter
    lang.newCounterView(counter, new Offset(400, 0, "parameter",
        AnimalScript.DIRECTION_NW), cp, true, true);
    lang.newText(new Offset(400, 0, "headerParam",
        AnimalScript.DIRECTION_NW), "Counter", "headerCounter", null,
        textPropsHeadlines);

    lang.nextStep();

    // initial source Code
    SourceCode sc = lang.newSourceCode(new Offset(0, 160, "parameter",
        AnimalScript.DIRECTION_SW), "sourceCode", null,
        valueCalculationFieldProps);
    sc.addCodeLine("Inserting the initial values x0 and t0"
        + " into the result-array", null, 0, null);
    lang.newText(new Offset(0, -30, "sourceCode",
        AnimalScript.DIRECTION_NW), "Current value calculation", "headerSC",
        null, textPropsHeadlines);

    // commentfield for Sourcecode
    SourceCode scComment = lang.newSourceCode(new Offset(0, 160, "parameter",
        AnimalScript.DIRECTION_SW), "sourceCodeComment", null,
        valueCalculationFieldProps);

    // put init-values for counter into matrix
    sm.put(1, 1, "" + t0, null, null);
    sm.put(2, 1, "" + x0, null, null);

    // highlight initial matrix-entries
    sm.highlightCell(1, 1, null, null);
    sm.highlightCell(2, 1, null, null);

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    lang.nextStep();

    // start calculating
    // f(x) = x'(t)
    DoubleEvaluator eval = new DoubleEvaluator();
//    int pointsAchieved = 0;
    int amountAsked = 0; // stops from asking too many questions
    double newX = 0;
    double oldX = x0;
    double fx = 0;
    double oldT = t0;
    for (int i = 2; i < steps + 2; i++) {

      // hide old calculation and unhighlight the matrix
      sc.hide();
      sm.unhighlightCell(1, (i - 1), null, null);
      sm.unhighlightCell(2, (i - 1), null, null);

      sc = lang.newSourceCode(new Offset(0, 160, "parameter",
          AnimalScript.DIRECTION_SW), "sourceCode", null,
          valueCalculationFieldProps);

      scComment = lang.newSourceCode(new Offset(50, -10, "sourceCode",
          AnimalScript.DIRECTION_NE), "sourceCodeComment", null,
          valueCalculationFieldProps);

      // create Function with replaced values for t and x
      String repFct = fct.replaceAll("x", "" + oldX);
      repFct = repFct.replace("t", "" + oldT);

      // Round fx to 4 values after ","
      fx = eval.evaluate(repFct);

      // create Function with current variable names for x and t
      String modFct = fct.replaceAll("x", "x" + (i - 2));
      modFct = modFct.replaceAll("t", "t" + (i - 2));

      // calculate new x, Round new x to 3 values after ","
      newX = Math.round((oldX + h * fx) * 1000.0) / 1000.0;

      if (amountAsked < 4 && i % 2 != 0) {
        FillInBlanksQuestionModel questionTModel = new FillInBlanksQuestionModel(
            "questionTModel" + (i - 1));
        questionTModel.setPrompt("What is the value of x" + (i - 1));
        questionTModel
            .addAnswer("" + newX, 1, "Your answer was correct. Good.");
        lang.addFIBQuestion(questionTModel);
        lang.nextStep();
        amountAsked++;
      }
      // round new value for T
      double newT = Math.round((oldT + h) * 1000.0) / 1000.0;

      // add calculation in current value text
      sc.addCodeLine("t" + (i - 1) + "     = " + "t" + (i - 2) + " + h" + " = "
          + sm.getElement(1, i - 1) + " + " + h + " = " + newT, null, 0, null); // tN
      scComment.addCodeLine("// Calculate t" + (i - 1) + " for next Step",
          null, 0, null); // comment
      sm.highlightCell(1, i, null, null);
      sm.put(1, i, "" + newT, null, null); // write tN into matrix
      lang.nextStep();
      sc.addCodeLine("f(" + "x" + (i - 2) + ")" + " = " + modFct, null, 0, null); // fx
      scComment.addCodeLine("// Set up the formula", null, 0, null); // comment
      lang.nextStep();
      sc.addCodeLine("        = " + repFct, null, 0, null);
      scComment.addCodeLine("// Insert values from the table into the formula",
          null, 0, null); // comment
      lang.nextStep();
      sc.addCodeLine("        = " + fx, null, 0, null);
      scComment.addCodeLine("// Calculate the result for f(x)", null, 0, null); // comment
      lang.nextStep();
      sc.addCodeLine("x" + (i - 1) + "    = " + "x" + (i - 2) + " + h" + " * "
          + "f(" + "x" + (i - 2) + ")", null, 0, null); // newX
      scComment.addCodeLine("// Set up the formula", null, 0, null); // comment
      lang.nextStep();
      sc.addCodeLine("       = " + oldX + " + " + h + " * " + fx, null, 0, null);
      scComment.addCodeLine(
          "// Insert values from the table and the just calculated f(x)", null,
          0, null); // comment
      lang.nextStep();
      sc.addCodeLine("       = " + newX, null, 0, null);
      scComment.addCodeLine("// Calculate the result", null, 0, null); // comment

      // new entry with results in matrix + highlight
      sm.put(2, i, "" + newX, null, null);
      sm.unhighlightCell(1, (i), null, null);
      sm.highlightCell(2, i, null, null);

      // initiate for next loop
      oldT = Double.valueOf(sm.getElement(1, i));
      oldX = Double.valueOf(sm.getElement(2, i));

      // do next step
      lang.nextStep();
      // pointsAchieved = pointsAchieved +
      // (questionTModel.getAnswers().iterator().next().getText() == ""+newX? 1
      // : 0);
      scComment.hide();
    }

     lang.newText(new Offset(300, 0, "headerSC",
        AnimalScript.DIRECTION_NE), "Final remark", "headerRemark", null,
        textPropsHeadlines);
    SourceCode remark = lang.newSourceCode(new Offset(0, 0, "headerRemark",
        AnimalScript.DIRECTION_SW), "remark", null, finalRemarkProps);
    // remark.addCodeLine("You reached "+pointsAchieved+"/"+steps+" points",
    // null, 0, null);
    // remark.addCodeLine("", null, 0, null);
    remark.addCodeLine("Last step t" + steps
        + " is reached. The array Result contains", null, 0, null);
    remark.addCodeLine(
        "the approximated values at their specific positions (tN).", null, 0,
        null);
    remark.addCodeLine(
        "If the function x(t) is given, the difference between the", null, 0,
        null);
    remark.addCodeLine("approximated values and the exact values can be", null,
        0, null);
    remark.addCodeLine("calculated by inserting the t-values into the", null,
        0, null);
    remark.addCodeLine(
        "function x(t) and comparing it to the approximated values.", null, 0,
        null);

    lang.finalizeGeneration();
  }

  public String getName() {
    return "Explicit Euler method";
  }

  public String getAlgorithmName() {
    return "Explicit Euler method";
  }

  public String getAnimationAuthor() {
    return "Benjamin Schiller, Philipp Dünnebeil";
  }

  public String getDescription() {
    return "In mathematics and computational science, the Euler method is a first-order "
        + "\n"
        + "numerical procedure for solving ordinary differential equations (ODEs) with"
        + "\n"
        + "a given initial value. It is the most basic explicit method for numerical integration "
        + "\n"
        + "of ordinary differential equations and is the simplest Runge-Kutta method. "
        + "\n"
        + "The Euler method is named after Leonhard Euler, who treated it in his book"
        + "\n"
        + "\"Institutionum calculi integralis\" (published 1768-70)."
        + "\n"
        + "The Euler method is a first-order method, which means that the local error"
        + "\n"
        + "(error per step) is proportional to the square of the step size, and the global "
        + "\n"
        + "error (error at a given time) is proportional to the step size. It also suffers from"
        + "\n"
        + " stability problems. For these reasons, the Euler method is not often used in"
        + "\n"
        + " practice. It serves as the basis to construct more complicated methods."
        + "\n" + "\n" + "Source: http://en.wikipedia.org/wiki/Euler_method";
  }

  public String getCodeExample() {
    return "Given Parameters:" + "\n" + "\n" + "Function: 	x'(t) = f(x, t)"
        + "\n" + "Step size: 	h " + "\n" + "Initial value: 	x0" + "\n"
        + "Start value:	t0" + "\n" + "\n" + "Calculation of next value:" + "\n"
        + "\n" + "x1 = x0 + h*f(x0, t0) " + "\n" + "x2 = x1 + h*f(x1, t1)"
        + "\n" + "... " + "\n" + "\n";
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