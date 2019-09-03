package generators.misc.machineLearning;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class FindSSymbolic extends AbstractFindSGenerator {

  private SourceCode   sc;
  private StringMatrix trainingDataMatrix;
  private int          numOfAttributes;
  private String[][]   trainingdata;
  private String lastHypo;

  public FindSSymbolic(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  public String[] generalize(String[] example, String[] hypo, int... v) {

    int row = v[0];

    String[] hypoNew = new String[numOfAttributes];

    for (int i = 0; i < numOfAttributes; i++) {

      if (!hypo[i].equalsIgnoreCase("?")) {

        trainingDataMatrix.highlightElem(row, i, null, null);
        if (hypo[i].equalsIgnoreCase("false")
            && !example[i].equalsIgnoreCase(hypo[i])) {
          hypoNew[i] = example[i];
          setExplanationText(translator.translateMessage("removeCon1") + " "
              + example[i] + translator.translateMessage("removeCon2"));
          sc.highlight(7);
        } else if (!example[i].equalsIgnoreCase(hypo[i])) {
          hypoNew[i] = "?";
          sc.highlight(7);
          setExplanationText(translator.translateMessage("removeCon3") + " "
              + hypo[i] + translator.translateMessage("removeCon4"));
        } else {
          setExplanationText(translator.translateMessage("attVal") + " "
              + hypo[i] + " " + translator.translateMessage("covered"));
          hypoNew[i] = hypo[i];
        }
        lang.nextStep();
        trainingDataMatrix.unhighlightElem(row, i, null, null);
      } else {
        hypoNew[i] = "?";
      }
    }
    unhighlightAllSourceCodeLines(sc, 8);
    return hypoNew;
  }

  public String createNewHypothesis(String[] example, int hypoNum) {

    StringBuilder sb = new StringBuilder();

    sb.append("H" + hypoNum + ": < ");
    for (int i = 0; i < example.length; i++) {

      if (i == example.length - 1) {
        sb.append(example[i]);
      } else {
        sb.append(example[i] + ", ");
      }
    }
    sb.append(" >");

    lang.newText(new Offset(0, (hypoNum * 20) + 10, "titleHypothesis",
        AnimalScript.DIRECTION_SW), sb.toString(), "h" + hypoNum, null);
    return sb.toString();
  }

  /**
   * 
   * @param trainingdata
   */
  public void createMostSpecificHypo(String[][] trainingdata) {

    String[] column;
    Object[] currentAttributes;
    StringBuilder sb = new StringBuilder();

    sb.append(translator.translateMessage("start"));

    for (int i = 0; i < trainingdata[0].length - 1; i++) {
      column = getCol(trainingdata, i);
      currentAttributes = getAttributeValues(column);

      for (int j = 0; j < currentAttributes.length; j++) {
        if (j == 0) {
          sb.append(" \n<");
        }
        if ((j == currentAttributes.length - 1)
            && (i == trainingdata[0].length - 2)) {
          sb.append(trainingdata[0][i]).append(" == ")
              .append(currentAttributes[j]).append(">");
        } else if (j == currentAttributes.length - 1) {
          sb.append(trainingdata[0][i]).append(" == ")
              .append(currentAttributes[j]).append(",");
        } else {
          sb.append(trainingdata[0][i]).append(" == ")
              .append(currentAttributes[j]).append(" && ");
        }
      }
    }
    setExplanationText(sb.toString());
  }

  @Override
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
        if (isNumericAttribute(trainingDataValidate[i][j])) {
          throw new IllegalArgumentException(
              "Only nominal values are allowed!");
        }

        if (trainingDataValidate[i][j] == null
            || trainingDataValidate[i][j].equals("")) {
          throw new IllegalArgumentException("Missing values are not allowed!");
        }
      }
    }

    return true;
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    initProps(props);

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

    createIntro(translator.translateMessage("algorithmName"),
        translator.translateMessage("description"), new Coordinates(10, 10),
        new Coordinates(450, 60), new Coordinates(60, 15),
        new Coordinates(10, 100));

    lang.nextStep();
    lang.hideAllPrimitives();

    trainingdata = (String[][]) primitives.get("stringMatrix");

    // title for trainingdata
    lang.newText(new Coordinates(10, 10),
        translator.translateMessage("dataset"), "titleTrainingdata", null,
        titleProps);

    trainingDataMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleTrainingdata", AnimalScript.DIRECTION_SW),
        trainingdata, "trainingdata", null, matrixProps);

    // title for source code
    lang.newText(
        new Offset(100, -25, "trainingdata", AnimalScript.DIRECTION_NE),
        "Pseudo Code:", "titleSourceCode", null, titleProps);

    // title for hypothesis
    lang.newText(new Offset(0, 30, "trainingdata", AnimalScript.DIRECTION_SW),
        translator.translateMessage("hypo"), "titleHypothesis", null,
        titleProps);

    sc = lang.newSourceCode(
        new Offset(0, 0, "titleSourceCode", AnimalScript.DIRECTION_SW), "sc",
        null, scProps);

    sc.addCodeLine(
        "1. h = most specific hypothesis in H (covering no examples)", null, 0,
        null); // 0
    sc.addCodeLine("2. for each training example e", null, 0, null); // 1
    sc.addCodeLine("a) if e is negative", null, 1, null); // 2
    sc.addCodeLine("   do nothing", null, 1, null); // 3
    sc.addCodeLine("b) if e is positive", null, 1, null); // 4
    sc.addCodeLine("   for each condition c in h", null, 1, null); // 5
    sc.addCodeLine("if c does not cover e", null, 3, null); // 6
    sc.addCodeLine("delete c from h", null, 4, null); // 7

    // title for explanation
    lang.newText(new Offset(0, 30, "sc", AnimalScript.DIRECTION_SW),
        translator.translateMessage("explanation"), "titleExplanation", null,
        titleProps);

    // ignore the class column
    numOfAttributes = trainingdata[0].length - 1;

    // create h0
    String[] h0 = new String[numOfAttributes];
    Arrays.fill(h0, "false");

    lang.nextStep();
    String[] previousHypo = h0;
    int hypoNum = 1;

    lastHypo = createNewHypothesis(h0, 0);

    sc.highlight(0);
    createMostSpecificHypo(trainingdata);
    lang.nextStep();
    sc.toggleHighlight(0, 1);
    setExplanationText(translator.translateMessage("foreach"));
    lang.nextStep();

    for (int i = 1; i < trainingdata.length; i++) {

      trainingDataMatrix.highlightCellColumnRange(i, 0,
          trainingdata[0].length - 2, null, null);
      trainingDataMatrix.highlightCell(i,
          trainingdata[0].length - 1, null, null);
      unhighlightAllSourceCodeLines(sc, 8);

      if (getClassOfExample(trainingdata[i]).equalsIgnoreCase("yes")) {
        
        if (isExampleCovered(trainingdata[i], previousHypo)) {
          sc.highlight(4);
          setExplanationText(translator.translateMessage("coveredEx"));
          
        } else {
        
        sc.highlight(4);
        sc.highlight(5);
        setExplanationText(translator.translateMessage("positive"));
        lang.nextStep();

        sc.highlight(6);
        String[] currentHypo = generalize(trainingdata[i], previousHypo, i);

        previousHypo = currentHypo;
        lastHypo = createNewHypothesis(currentHypo, hypoNum);

        setExplanationText(translator.translateMessage("newHypo") + " " + lastHypo);
        hypoNum++;

        if (arePreviousNegativeCovered(trainingdata, previousHypo, hypoNum)) {
          setExplanationText(translator.translateMessage("noConcept"));
          lang.nextStep();
          lang.addMCQuestion(outroQuestion);
          lang.finalizeGeneration();
          return lang.toString();
        }
        }

      } else {

        setExplanationText(translator.translateMessage("negative"));

        if (isExampleCovered(trainingdata[i], previousHypo)) {

          setExplanationText(translator.translateMessage("noConcept"));
          lang.nextStep();
          lang.addMCQuestion(outroQuestion);
          lang.finalizeGeneration();
          return lang.toString();
        }
        sc.highlight(2);
        sc.highlight(3);
      }
      lang.nextStep();
      trainingDataMatrix.unhighlightCellColumnRange(i, 0,
          trainingdata[0].length - 2, null, null);
      trainingDataMatrix.unhighlightCell(i,
          trainingdata[0].length - 1, null, null);
    }
    setExplanationText(translator.translateMessage("terminates", lastHypo.substring(4)));
    unhighlightAllSourceCodeLines(sc, 8);
    lang.nextStep();
    lang.addMCQuestion(outroQuestion);
    lang.finalizeGeneration();
    return lang.toString();
  }

  public boolean isExampleCovered(String[] example, String[] hypo) {

    for (int i = 0; i < hypo.length; i++) {
      if (!example[i].equalsIgnoreCase(hypo[i])
          && !hypo[i].equalsIgnoreCase("?")) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String getCodeExample() {
    return "1. h = most specific hypothesis in H (covering no examples)" + "\n"
        + "2. for each training example e" + "\n" + "   a) if e is negative"
        + "\n" + "      do nothing" + "\n" + "   b) if e is positive" + "\n"
        + "      for each condition c in h" + "\n"
        + "         if c does not cover e" + "\n"
        + "            delete c from h";
  }

}
