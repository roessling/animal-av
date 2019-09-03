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

public class FindGSymbolic extends AbstractFindGGenerator {

  private SourceCode   sc;
  private StringMatrix trainingDataMatrix;
  private String[][]   trainingdata;
  private String lastHypo;

  public FindGSymbolic(String resourceName, Locale locale) {
    super(resourceName, locale);
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

    createIntro(translator.translateMessage("algorithmName"),
        translator.translateMessage("description"), new Coordinates(10, 10),
        new Coordinates(450, 60), new Coordinates(60, 15),
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

    // title for the training data
    lang.newText(new Coordinates(10, 10), "Dataset:", "titleDataSet", null,
        titleProps);

    // training data
    trainingDataMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleDataSet", AnimalScript.DIRECTION_SW),
        trainingdata, "dataset", null, matrixProps);

    lang.newText(new Coordinates(500, 10), "Pseudo Code:", "titleSourceCode",
        null, titleProps);

    sc = lang.newSourceCode(
        new Offset(0, 0, "titleSourceCode", AnimalScript.DIRECTION_SW), "sc",
        null, scProps);
    sc.addCodeLine(
        "1. h = most general hypothesis in H (covering all examples)", null, 0,
        null); // 0
    sc.addCodeLine("2. for each training example e", null, 0, null); // 1
    sc.addCodeLine("a) if e is positive", null, 1, null); // 2
    sc.addCodeLine("do nothing", null, 2, null); // 3
    sc.addCodeLine("b) if e is negative", null, 1, null); // 4
    sc.addCodeLine("for some condition c in e", null, 2, null); // 5
    sc.addCodeLine("if c is not part of h", null, 3, null); // 6
    sc.addCodeLine(
        "add a condition that negates c and covers all previous examples to h",
        null, 4, null); // 7

    lang.newText(new Offset(0, 30, "sc", AnimalScript.DIRECTION_SW),
        translator.translateMessage("explanation"), "titleExplanation", null,
        titleProps);
    lang.newText(new Offset(0, 30, "dataset", AnimalScript.DIRECTION_SW),
        translator.translateMessage("hypo"), "titleHypothesis", null,
        titleProps);

    // create h0
    String[] h0 = new String[trainingdata[0].length - 1];
    Arrays.fill(h0, "?");

    lang.nextStep();
    String[] previousHypo = h0;
    int hypoNum = 1;

    lastHypo = createNewHypothesis(h0, 0);

    sc.highlight(0);
    createMostGeneralHypo(trainingdata);
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

      if (getClassOfExample(trainingdata[i]).equalsIgnoreCase("no")) {
        
        if (!isExampleCovered(trainingdata[i], previousHypo)) {
          sc.highlight(4);
          setExplanationText(translator.translateMessage("notCovered"));
        }else {
        
        
        sc.highlight(4);
        sc.highlight(5);
        setExplanationText(translator.translateMessage("negative"));
        lang.nextStep();

        String[] currentHypo = specify(trainingdata[i], previousHypo, hypoNum,
            i);

        if(Arrays.equals(previousHypo,currentHypo)) {
          setExplanationText(translator.translateMessage("noNegationPossible2"));
          lang.nextStep();
          lang.addMCQuestion(outroQuestion);
          lang.finalizeGeneration();
          return lang.toString();
        }

        
        previousHypo = currentHypo;
        lastHypo = createNewHypothesis(currentHypo, hypoNum);
        
        
        setExplanationText(translator.translateMessage("newHypo") + " " + lastHypo);
        hypoNum++;
        }
      } else {

        sc.highlight(2);
        sc.highlight(3);
        setExplanationText(translator.translateMessage("positive"));
        if (!isExampleCovered(trainingdata[i], previousHypo)) {
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
    setExplanationText(translator.translateMessage("terminates", lastHypo.substring(4)));
    unhighlightAllSourceCodeLines(sc, 8);
    lang.nextStep();
    lang.addMCQuestion(outroQuestion);
    lang.finalizeGeneration();
    return lang.toString();
  }

  @Override
  public String getCodeExample() {
    return "1. h = most general hypothesis in H (covering all examples)" + "\n"
        + "2. for each training example e" + "\n" + "   a) if e is positive"
        + "\n" + "      do nothing" + "\n" + "   b) if e is negative" + "\n"
        + "      for some condition c in e" + "\n"
        + "         if c is not part of h" + "\n"
        + "            add a condition that negates c and covers all previous positive examples to h";
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
  public String[] specify(String[] example, String[] hypo, int hypoNum,
      int row) {

    String[] hypoNew = new String[hypo.length];
    System.arraycopy( hypo, 0, hypoNew, 0, hypo.length );
    String[] attributes;
    boolean coverPrevious = true;

    int numOfAttributes = trainingdata[0].length - 1;

    for (int i = 0; i < numOfAttributes; i++) {
      trainingDataMatrix.highlightElem(row, i, null, null);

      coverPrevious = true;
      if (hypo[i].equalsIgnoreCase("?")
          && !example[i].equalsIgnoreCase(hypo[i])) {

        sc.highlight(6);

        attributes = getAttributeValues(trainingdata, i);

        for (int j = 0; j < attributes.length; j++) {

          if (!attributes[j].equalsIgnoreCase(example[i])) {

            for (int k = 1; k < row; k++) {
              if (trainingdata[k][i].equalsIgnoreCase(attributes[j])
                  && trainingdata[k][trainingdata[0].length - 1]
                      .equalsIgnoreCase("yes")) {

                coverPrevious = coverPrevious & true;
              } else if(!trainingdata[k][i].equalsIgnoreCase(attributes[j])
                  && trainingdata[k][trainingdata[0].length - 1]
                      .equalsIgnoreCase("yes")) {
                coverPrevious = false;
                setExplanationText(translator.translateMessage("noNegationPossible1"));
              }
            }

            if (coverPrevious) {

              sc.highlight(7);
              setExplanationText(
                  translator.translateMessage("negate1") + " " + attributes[j]
                      + " " + translator.translateMessage("negate2"));
              hypoNew[i] = attributes[j];
              lang.nextStep();

              trainingDataMatrix.unhighlightElem(row, i, null, null);
              return hypoNew;
            } else {
              hypoNew[i] = hypo[i];
            }
          }
        }


      }
      lang.nextStep();
      trainingDataMatrix.unhighlightElem(row, i, null, null);
    }

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

  public void createMostGeneralHypo(String[][] dataset) {

    String[] column;
    Object[] currentAttributes;
    StringBuilder sb = new StringBuilder();

    sb.append(translator.translateMessage("start"));

    for (int i = 0; i < dataset[0].length - 1; i++) {
      column = getCol(dataset, i);
      currentAttributes = getAttributeValues(column);

      for (int j = 0; j < currentAttributes.length; j++) {
        if (j == 0) {
          sb.append(" \n<");
        }
        if ((j == currentAttributes.length - 1)
            && (i == dataset[0].length - 2)) {
          sb.append(dataset[0][i]).append(" == ").append(currentAttributes[j])
              .append(">");
        } else if (j == currentAttributes.length - 1) {
          sb.append(dataset[0][i]).append(" == ").append(currentAttributes[j])
              .append(",");
        } else {
          sb.append(dataset[0][i]).append(" == ").append(currentAttributes[j])
              .append(" || ");
        }
      }
    }
    setExplanationText(sb.toString());
  }

}
