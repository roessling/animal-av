package generators.misc.machineLearning;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class FindS2D extends AbstractFindSGenerator {

  private SourceCode         sc;
  private CoordinateSystem2D cs2D;
  private String[][]         trainingdata;
  private StringMatrix       trainingDataMatrix;
  private String lastHypo;
  public FindS2D(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  public boolean isExampleCovered(String example[], String[] hypo) {
    if (hypo[0].equalsIgnoreCase("false")) {
      return false;
    } else if ((Double.parseDouble(example[0]) >= Double.parseDouble(hypo[0])
        && Double.parseDouble(example[0]) <= Double.parseDouble(hypo[1]))
        && (Double.parseDouble(example[1]) >= Double.parseDouble(hypo[2])
            && Double.parseDouble(example[1]) <= Double.parseDouble(hypo[3]))) {
      return true;
    } else
      return false;
  }

  public String createNewHypothesis(String[] example, int hypoNum) {

    StringBuilder sb = new StringBuilder();

    sb.append("H").append(hypoNum).append(" : < ");
    sb.append("[ ").append(example[0]).append(", ").append(example[1])
        .append(" ], ");
    sb.append("[ ").append(example[2]).append(", ").append(example[3])
        .append(" ] >");

    lang.newText(new Offset(0, (hypoNum * 20) + 10, "titleHypothesis",
        AnimalScript.DIRECTION_SW), sb.toString(), "h" + hypoNum, null);

    return sb.toString();
  }

  public String[] generalize(String[] example, String[] hypo, int... hypoNum) {

    String[] hypoNew = new String[4];
    hypoNew = hypo;

    if (hypo[0].equalsIgnoreCase("false")) {
      hypoNew[0] = example[0];
      hypoNew[1] = example[0];
      hypoNew[2] = example[1];
      hypoNew[3] = example[1];
      return hypoNew;
    }
    if (Double.parseDouble(hypo[0]) > Double.parseDouble(example[0])) {
      hypoNew[0] = example[0];
    }
    if (Double.parseDouble(hypo[1]) < Double.parseDouble(example[0])) {
      hypoNew[1] = example[0];
    }
    if (Double.parseDouble(hypo[2]) > Double.parseDouble(example[1])) {
      hypoNew[2] = example[1];
    }
    if (Double.parseDouble(hypo[3]) < Double.parseDouble(example[1])) {
      hypoNew[3] = example[1];
    }

    return hypoNew;

  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    initProps(props);
    
    createIntro(translator.translateMessage("algorithmName"),
        translator.translateMessage("description"), new Coordinates(10, 10),
        new Coordinates(350, 60), new Coordinates(60, 15),
        new Coordinates(10, 100));

    lang.nextStep();
    lang.hideAllPrimitives();

    MultipleChoiceQuestionModel introQuestion = new MultipleChoiceQuestionModel(
        "introQuestion");
    introQuestion.setPrompt(translator.translateMessage("introQuestion"));
    introQuestion.addAnswer("0",
        translator.translateMessage("introQuestionAnswer1"), 0,
        translator.translateMessage("introQuestionFeedback1"));
    introQuestion.addAnswer("1",
        translator.translateMessage("introQuestionAnswer2"), 1,
        translator.translateMessage("introQuestionFeedback2"));
    introQuestion.addAnswer("2",
        translator.translateMessage("introQuestionAnswer3"), 0,
        translator.translateMessage("introQuestionFeedback3"));
    introQuestion.setNumberOfTries(3);
    lang.addMCQuestion(introQuestion);

    MultipleChoiceQuestionModel outroQuestion = new MultipleChoiceQuestionModel(
        "outroQuestion");
    outroQuestion.setPrompt(translator.translateMessage("outroQuestion"));
    outroQuestion.addAnswer("0",
        translator.translateMessage("outroQuestionAnswer1"), 1,
        translator.translateMessage("outroQuestionFeedback1"));
    outroQuestion.addAnswer("1",
        translator.translateMessage("outroQuestionAnswer2"), 0,
        translator.translateMessage("outroQuestionFeedback2"));
    outroQuestion.addAnswer("2",
        translator.translateMessage("outroQuestionAnswer3"), 0,
        translator.translateMessage("outroQuestionFeedback3"));
    outroQuestion.setNumberOfTries(3);

    trainingdata = (String[][]) primitives.get("stringMatrix");

    LinkedList<Double> xValues = new LinkedList<Double>();
    LinkedList<Double> yValues = new LinkedList<Double>();

    for (int i = 1; i < trainingdata.length; i++) {
      xValues.add(Double.parseDouble(trainingdata[i][0]));
      yValues.add(Double.parseDouble(trainingdata[i][1]));
    }

    lang.newText(new Coordinates(600, 10), "Pseudo Code:", "titleSourceCode",
        null, titleProps);
    sc = lang.newSourceCode(
        new Offset(0, 0, "titleSourceCode", AnimalScript.DIRECTION_SW), "sc",
        null, scProps);
    sc.addMultilineCode(
        "Define a hypothesis rectangle h[a,b,c,d], and initialise it to h = [false,false,false,false]",
        null, null); // 1
    sc.addCodeLine("for each training example e", null, 0, null); // 2
    sc.addCodeLine("a) if e is negative", null, 1, null); // 3
    sc.addCodeLine("do nothing", null, 2, null); // 4
    sc.addCodeLine("b) if e is positive", null, 1, null); // 5
    sc.addCodeLine("if e is not within h", null, 2, null); // 6
    sc.addCodeLine(
        "enlarge h to be just big enough to cover e (and all previous e's)",
        null, 3, null); // 7
    sc.show();

    lang.newText(new Coordinates(10, 10),
        translator.translateMessage("dataset"), "titleDataSet", null,
        titleProps);
    trainingDataMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleDataSet", AnimalScript.DIRECTION_SW),
        trainingdata, "dataset", null, matrixProps);

    lang.newText(new Offset(200, 0, "titleDataSet", AnimalScript.DIRECTION_NW),
        translator.translateMessage("cs"), "titleCS", null, titleProps);

    cs2D = new CoordinateSystem2D(lang, new Coordinates(230, 300), 250, 250,
        Math.min(Collections.min(xValues), Collections.min(yValues)),
        Math.max(Collections.max(xValues), Collections.max(yValues)));

    cs2D.createCoordinateSystem();
    cs2D.labelAxis();

    lang.newText(new Offset(0, 30, "sc", AnimalScript.DIRECTION_SW),
        translator.translateMessage("hypo"), "titleHypothesis", null,
        titleProps);

    lang.newText(
        new Offset(200, 0, "titleHypothesis", AnimalScript.DIRECTION_NE),
        translator.translateMessage("explanation"), "titleExplanation", null,
        titleProps);

    sc.highlight(0);

    String[] h0 = new String[] { "false", "false", "false", "false" };
    String[] previousHypo = h0;
    lastHypo = createNewHypothesis(previousHypo, 0);
    int hypoNum = 1;
    setExplanationText(translator.translateMessage("start"));

    lang.nextStep();
    sc.unhighlight(0);
    sc.highlight(1);
    setExplanationText(translator.translateMessage("foreach"));
    lang.nextStep();
    String[] currentHypo;
    for (int i = 1; i < trainingdata.length; i++) {

      trainingDataMatrix.highlightCellColumnRange(i, 0,
          trainingdata[0].length - 2, null, null);
      trainingDataMatrix.highlightCell(i,
          trainingdata[0].length - 1, null, null);
      unhighlightAllSourceCodeLines(sc, 7);

      // if example is positive
      if (getClassOfExample(trainingdata[i]).equalsIgnoreCase("yes")) {

        sc.highlight(4);
        cs2D.setPoint(Double.parseDouble(trainingdata[i][0]),
            Double.parseDouble(trainingdata[i][1]), trainingdata[i][2]);
        setExplanationText(translator.translateMessage("notGeneralize"));

        if (!isExampleCovered(trainingdata[i], previousHypo)) {

          sc.highlight(4);
          sc.highlight(5);
          setExplanationText(translator.translateMessage("generalize"));
          lang.nextStep();
          cs2D.hideBoundary();
          sc.highlight(6);
          currentHypo = generalize(trainingdata[i], previousHypo, null);
          previousHypo = currentHypo;

          cs2D.drawSetBoundary(Double.parseDouble(previousHypo[0]),
              Double.parseDouble(previousHypo[2]),
              Double.parseDouble(previousHypo[1]),
              Double.parseDouble(previousHypo[3]));

          if (arePreviousNegativeCovered(trainingdata, previousHypo, i)) {

            setExplanationText(translator.translateMessage("noConcept"));
            lang.nextStep();
            lang.addMCQuestion(outroQuestion);
            lang.finalizeGeneration();
            return lang.toString();
          }
        }

        lastHypo = createNewHypothesis(previousHypo, hypoNum);
        hypoNum++;
      } else {
        sc.highlight(2);
        sc.highlight(3);
        setExplanationText(translator.translateMessage("negative"));
        cs2D.setPoint(Double.parseDouble(trainingdata[i][0]),
            Double.parseDouble(trainingdata[i][1]), trainingdata[i][2]);

        if (isExampleCovered(trainingdata[i], previousHypo)) {

          setExplanationText(translator.translateMessage("noConcept"));
          lang.nextStep();
          lang.addMCQuestion(outroQuestion);
          lang.finalizeGeneration();
          return lang.toString();
        }
      }
      lang.nextStep();
      trainingDataMatrix.unhighlightCellColumnRange(i, 0,
          trainingdata[0].length - 2, null, null);
      trainingDataMatrix.unhighlightCell(i,
          trainingdata[0].length - 1, null, null);

    }
    setExplanationText(translator.translateMessage("terminates", lastHypo.substring(5)));
    lang.nextStep();
    lang.addMCQuestion(outroQuestion);
    lang.finalizeGeneration();
    return lang.toString();
  }

  @Override
  public String getCodeExample() {
    return "Define a hypothesis rectangle h[a,b,c,d], and initialise it to h = [false,false,false,false]"
        + "\n"
        + "a and b defines the interval on the x-axis, c and d the y-axis"
        + "\n" + "for each training example e" + "\n" + "   a) if e is negative"
        + "\n" + "      do nothing" + "\n" + "   b) if e is positive" + "\n"
        + "      if e is not within h" + "\n"
        + "         enlarge h to be just big enough to cover e (and all previous e's)";
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {

    String[][] trainingDataValidate = (String[][]) primitives
        .get("stringMatrix");
    int classCol = trainingDataValidate[0].length - 1;

    if (trainingDataValidate == null || trainingDataValidate.length < 2
        || trainingDataValidate[0].length < 3) {
      throw new IllegalArgumentException("Please insert valid training data!");
    }

    for (int i = 1; i < trainingDataValidate.length; i++) {
      if (!trainingDataValidate[i][classCol].equalsIgnoreCase("yes")
          && !trainingDataValidate[i][classCol].equalsIgnoreCase("no")) {
        throw new IllegalArgumentException(
            "The last attribute classifies the training example. Please specifiy the class of each example by adding 'yes' or 'no' to the class attribute (the last element of a training example).");
      }

      for (int j = 0; j < trainingDataValidate[0].length - 1; j++) {
        if (!isNumericAttribute(trainingDataValidate[i][j])) {
          throw new IllegalArgumentException(
              "Only numeric values are allowed!");
        }

        if (trainingDataValidate[i][j] == null
            || trainingDataValidate[i][j].equals("")) {
          throw new IllegalArgumentException("Missing values are not allowed!");
        }
      }
    }

    return true;
  }

}
