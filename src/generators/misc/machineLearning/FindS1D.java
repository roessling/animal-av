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
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class FindS1D extends AbstractFindSGenerator {

  private SourceCode         sc;
  private CoordinateSystem1D cs1D;
  private StringMatrix       trainingDataMatrix;
  private String lastHypo;

  public FindS1D(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  public String[] generalize(String[] example, String[] hypo, int... hypoNum) {

    String[] hypoNew = new String[2];
    for (int i = 0; i < hypoNew.length; i++) {
      hypoNew[i] = hypo[i];
    }

    for (int i = 0; i < hypoNew.length; i++) {
      if ((i == 0 || i == 1) && hypo[i].equalsIgnoreCase("false")) {
        hypoNew[0] = example[0];
        hypoNew[1] = example[0];
      } else if ((i == 2 || i == 3) && hypo[i].equalsIgnoreCase("false")) {
        hypoNew[2] = example[1];
        hypoNew[3] = example[1];
      } else if (i == 0
          && (Double.parseDouble(hypo[i]) > Double.parseDouble(example[0]))) {
        hypoNew[0] = example[0];
      } else if (i == 1
          && Double.parseDouble(hypo[i]) < Double.parseDouble(example[0])) {
        hypoNew[1] = example[0];
      }
    }

    return hypoNew;

  }

  public boolean isExampleCovered(String example[], String[] hypo) {
    if (hypo[0].equalsIgnoreCase("false")) {
      return false;
    } else if ((Double.parseDouble(example[0]) >= Double.parseDouble(hypo[0])
        && Double.parseDouble(example[0]) <= Double.parseDouble(hypo[1]))) {
      return true;
    } else
      return false;
  }

  public String createNewHypothesis(String[] example, int hypoNum) {
    StringBuilder sb = new StringBuilder();

    sb.append("H").append(hypoNum).append(" : < ");
    sb.append("[ ").append(example[0]).append(", ").append(example[1])
        .append(" ] ").append(" >");

    lang.newText(new Offset(0, (hypoNum * 20) + 10, "titleHypothesis",
        AnimalScript.DIRECTION_SW), sb.toString(), "h" + hypoNum, null);

    return sb.toString();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    initProps(props);

    createIntro(translator.translateMessage("algorithmName"),
        translator.translateMessage("description"), new Coordinates(10, 10),
        new Coordinates(400, 60), new Coordinates(60, 15),
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
    outroQuestion.addAnswer("3",
        translator.translateMessage("outroQuestionAnswer1"), 1,
        translator.translateMessage("outroQuestionFeedback1"));
    outroQuestion.addAnswer("4",
        translator.translateMessage("outroQuestionAnswer2"), 0,
        translator.translateMessage("outroQuestionFeedback2"));
    outroQuestion.addAnswer("5",
        translator.translateMessage("outroQuestionAnswer3"), 0,
        translator.translateMessage("outroQuestionFeedback3"));
    outroQuestion.setNumberOfTries(3);

    String[][] trainingdata = (String[][]) primitives.get("stringMatrix");

    LinkedList<Double> xValues = new LinkedList<Double>();

    for (int i = 1; i < trainingdata.length; i++) {
      xValues.add(Double.parseDouble(trainingdata[i][0]));
    }

    lang.newText(new Coordinates(600, 10), "Pseudo Code:", "titleSourceCode",
        null, titleProps);

    sc = lang.newSourceCode(
        new Offset(0, 0, "titleSourceCode", AnimalScript.DIRECTION_SW), "sc",
        null, scProps);

    sc.addMultilineCode(
        "Define a hypothesis h and initialise it to h = [false,false]\n", null,
        null); // 1
    sc.addCodeLine("for each training example e", null, 0, null); // 2
    sc.addCodeLine("a) if e is negative", null, 1, null); // 3
    sc.addCodeLine("   do nothing", null, 1, null); // 4
    sc.addCodeLine("b) if e is positive", null, 1, null); // 5
    sc.addCodeLine("   if e is not within h", null, 1, null); // 6
    sc.addCodeLine(
        "   enlarge h to be just big enough to cover e (and all previous e's)",
        null, 2, null); // 7

    lang.newText(new Coordinates(10, 10),
        translator.translateMessage("dataset"), "titleDataSet", null,
        titleProps);

    trainingDataMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleDataSet", AnimalScript.DIRECTION_SW),
        trainingdata, "dataset", null, matrixProps);

    lang.newText(new Offset(250, 0, "titleDataSet", AnimalScript.DIRECTION_NW),
        translator.translateMessage("cs"), "titleCS", null, titleProps);

    lang.newText(new Offset(0, 200, "titleCS", AnimalScript.DIRECTION_SW),
        translator.translateMessage("hypo"), "titleHypothesis", null,
        titleProps);

    lang.newText(
        new Offset(0, 200, "titleSourceCode", AnimalScript.DIRECTION_SW),
        translator.translateMessage("explanation"), "titleExplanation", null,
        titleProps);

    cs1D = new CoordinateSystem1D(lang, new Coordinates(260, 100), 250,
        Collections.min(xValues), Collections.max(xValues));
    cs1D.createCoordinateSystem();
    cs1D.labelAxis();

    sc.highlight(0);
    setExplanationText(translator.translateMessage("start"));

    String[] h0 = new String[] { "false", "false" };
    String[] previousHypo = h0;
    lastHypo = createNewHypothesis(h0, 0);
    int hypoNum = 1;
    lang.nextStep();
    sc.toggleHighlight(0, 1);
    setExplanationText(translator.translateMessage("foreach"));
    lang.nextStep();
    sc.unhighlight(1);
    sc.highlight(2);

    for (int i = 1; i < trainingdata.length; i++) {
      trainingDataMatrix.highlightCellColumnRange(i, 0,
          trainingdata[0].length - 2, null, null);
      trainingDataMatrix.highlightCell(i,
          trainingdata[0].length - 1, null, null);

      unhighlightAllSourceCodeLines(sc, 7);

      // if example is positive
      if (getClassOfExample(trainingdata[i]).equalsIgnoreCase("yes")) {
        sc.highlight(4);

        cs1D.setPoint(Double.parseDouble(trainingdata[i][0]),
            trainingdata[i][1]);

        setExplanationText(translator.translateMessage("notGeneralize"));

        if (!isExampleCovered(trainingdata[i], previousHypo)) {
          sc.highlight(5);
          setExplanationText(translator.translateMessage("generalize"));
          cs1D.hideBoundary();
          lang.nextStep();
          sc.highlight(6);
          String[] currentHypo = generalize(trainingdata[i], previousHypo,
              null);

          previousHypo = currentHypo;

          cs1D.drawSetBoundary(Double.parseDouble(currentHypo[0]),
              Double.parseDouble(currentHypo[1]));

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
        cs1D.setPoint(Double.parseDouble(trainingdata[i][0]),
            trainingdata[i][1]);
        if (isExampleCovered(trainingdata[i], previousHypo)) {

          setExplanationText(translator.translateMessage("noConcept"));
          lang.nextStep();
          lang.addMCQuestion(outroQuestion);
          lang.finalizeGeneration();
          return lang.toString();
        } else {
          sc.highlight(2);
          sc.highlight(3);
          setExplanationText(translator.translateMessage("negative"));

        }
      }

      lang.nextStep();
      trainingDataMatrix.unhighlightCellColumnRange(i, 0,
          trainingdata[0].length - 2, null, null);
      trainingDataMatrix.unhighlightCell(i,
          trainingdata[0].length - 1, null, null);
    }

    unhighlightAllSourceCodeLines(sc, 7);
    setExplanationText(translator.translateMessage("terminates", lastHypo.substring(5)));
    lang.nextStep();
    lang.addMCQuestion(outroQuestion);
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getCodeExample() {
    return "Define a hypothesis rectangle h[a,b], and initialise it to h = [false,false]"
        + "\n" + "a and b defines the interval on the x-axis" + "\n"
        + "for each training example e" + "\n" + "   a) if e is negative" + "\n"
        + "      do nothing" + "\n" + "   b) if e is positive" + "\n"
        + "      if e is not within h" + "\n"
        + "         enlarge h to be just big enough to cover e (and all previous e's)";
  }

  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {

    String[][] trainingDataValidate = (String[][]) primitives
        .get("stringMatrix");
    int classCol = trainingDataValidate[0].length - 1;

    if (trainingDataValidate == null || trainingDataValidate.length < 2
        || trainingDataValidate[0].length < 2) {
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
