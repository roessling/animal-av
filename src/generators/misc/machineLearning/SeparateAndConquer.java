package generators.misc.machineLearning;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class SeparateAndConquer extends AbstractMachineLearning {

  private SourceCode   sc;
  private StringMatrix trainingSet;
  private Heuristic    heuristic;
  private String       rule;
  private String[][]   trainingData;
  private int          ruleCounter;
  private int          coveredPositivesWithRules;
  private int          coveredNegativesWithRules;

  public SeparateAndConquer(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  /**
   * 
   * @param examples
   * @param attribute
   * @param attributeValue
   * @param previousHeuristic
   * @return
   */
  public String[][] refine(String[][] examples, String attribute,
      String attributeValue) {

    setExplanationText(" ");

    String[][] tmp = filter(examples, attribute, attributeValue);

    trainingSet.hide();
    trainingSet = lang.newStringMatrix(
        new Offset(0, 30, "titleTrainingData", AnimalScript.DIRECTION_NW), tmp,
        "trainingSet", null, matrixProps);

    String[][] c = createConqueredMatrix(tmp, false);
    StringMatrix cM = lang.newStringMatrix(
        new Offset(0, 30, "titleConqueredData", AnimalScript.DIRECTION_NW), c,
        "conqueredData", null, matrixProps);

    lang.nextStep();
    calcHeuristic(cM);

    String[] highestHeuristic = heuristic.findHighestHeuristic(cM);

    rule = createRule(rule, highestHeuristic[0], highestHeuristic[1]);

    if (Double.parseDouble(highestHeuristic[3]) > 0) {

      setExplanationText(translator.translateMessage("filterAndRefine2", rule));
      lang.nextStep();
      cM.hide();
      refine(tmp, highestHeuristic[0], highestHeuristic[1]);

    } else {

      setExplanationText(
          translator.translateMessage("noNegativeCovered", rule));

      rule = rule + " -> yes ( p = " + highestHeuristic[2] + ", n = "
          + highestHeuristic[3] + " )";
      coveredPositivesWithRules += Integer.valueOf(highestHeuristic[2]);
      coveredNegativesWithRules += Integer.valueOf(highestHeuristic[3]);
      ruleCounter++;

      lang.nextStep();
      setExplanationText(translator.translateMessage("addRule"));
      lang.newText(
          new Offset(0, ruleCounter * 17, "titleRuleset",
              AnimalScript.DIRECTION_SW),
          ruleCounter + ". " + rule, "rule", null, textProps);

      sc.highlight(4);
      sc.highlight(5);
      sc.toggleHighlight(1, 6);
      lang.nextStep();
      cM.hide();
      sc.unhighlight(6);
      sc.unhighlight(5);
      sc.unhighlight(4);
      sc.highlight(1);

    }
    return filter(tmp, highestHeuristic[0], highestHeuristic[1]);
  }

  /**
   * create new rule and concatenate it to the previous ruleset
   * 
   * @param currentRule
   *          current ruleset
   * @param attribute
   *          attribute of new rule
   * @param attributeValue
   *          attribute value of new rule
   * @return new ruleset
   */
  public String createRule(String currentRule, String attribute,
      String attributeValue) {
    StringBuilder sb = new StringBuilder();

    if (currentRule == "" || currentRule == null) {
      sb.append(attribute).append(" = ").append(attributeValue);
    } else {
      sb.append(currentRule).append(" AND ").append(attribute).append(" = ")
          .append(attributeValue);
    }

    return sb.toString();
  }

  /**
   * filter examples with a specific attribute value
   * 
   * @param examples
   *          training data
   * @param attribute
   *          attribute that will be filtered
   * @param attributeValue
   *          value of the attribute that will be filtered
   * @return examples with attribute = attributeValue
   */
  public String[][] filter(String[][] examples, String attribute,
      String attributeValue) {

    int rows = countClassAttribute(examples, attribute, attributeValue, "yes")
        + countClassAttribute(examples, attribute, attributeValue, "no");

    String[][] filteredMatrix = new String[rows + 1][examples[0].length];
    int attIndex = 0;

    for (int i = 0; i < examples[0].length; i++) {
      if (attribute.equalsIgnoreCase(examples[0][i])) {
        attIndex = i;
      }
    }

    int k = 0;
    for (int i = 0; i < examples.length; i++) {
      if (i == 0 || examples[i][attIndex].equalsIgnoreCase(attributeValue)) {

        for (int j = 0; j < examples[0].length; j++) {
          filteredMatrix[k][j] = examples[i][j];
        }
        k++;
      }
    }

    return filteredMatrix;
  }

  /**
   * remove all examples with attribute = attributeValue
   * 
   * @param examples
   *          training data
   * @param attribute
   *          attribute that need to be removed
   * @param attributeValue
   *          value of the attribute that need to be removed
   * @return examples without attribute = attributeValue
   */
  public String[][] remove(String[][] examples, String attribute,
      String attributeValue) {

    int amount = countClassAttribute(examples, attribute, attributeValue, "yes")
        + countClassAttribute(examples, attribute, attributeValue, "no");

    String[][] removedMatrix = new String[examples.length
        - amount][examples[0].length];

    int attIndex = 0;

    attIndex = getIndex(examples, attribute);

    int k = 0;
    for (int i = 0; i < examples.length; i++) {
      if (!examples[i][attIndex].equalsIgnoreCase(attributeValue)) {
        removedMatrix[k] = examples[i];
        k++;
      }
    }

    return removedMatrix;
  }

  /**
   * remove a set of examples out of
   * 
   * @param examples
   *          training data
   * @param removeDataSet
   *          examples that need to be removed
   * @return data set without the examples in removeDataSet
   */
  public String[][] remove(String[][] examples, String[][] removeDataSet) {

    String[][] newDataSet = new String[examples.length - removeDataSet.length
        + 1][examples[0].length];

    int r = 0;
    for (int i = 0; i < examples.length; i++) {
      if (!matrixContainsRow(removeDataSet, examples[i]) || i == 0) {
        newDataSet[r] = examples[i];
        r++;
      }
    }

    return newDataSet;

  }

  /**
   * check if a data set contains a specific example
   * 
   * @param examples
   *          training data
   * @param row
   *          example
   * @return true if the matrix contains the example
   */
  public boolean matrixContainsRow(String[][] examples, String[] row) {

    boolean val = false;
    for (int j = 0; j < examples.length; j++) {
      if (Arrays.equals(examples[j], row)) {
        return true;
      }
    }

    return val;
  }

  /**
   * calculate the heuristic value for each example in the matrix
   * 
   * @param m
   *          conquered matrix
   */
  public void calcHeuristic(StringMatrix m) {

    sc.highlight(1);
    sc.unhighlight(5);
    sc.unhighlight(6);
    heuristic = new PrecisionHeuristic();
    for (int i = 1; i < m.getNrRows(); i++) {

      double positiveValue = Double.valueOf(m.getElement(i, 2));
      double negativeValue = Double.valueOf(m.getElement(i, 3));

      setExplanationText(translator.translateMessage("calc")
          + translator.translateMessage(heuristic.getDescription()) + "\n"
          + heuristic.getFormula(positiveValue, negativeValue));

      m.put(i, m.getNrCols() - 1,
          String.valueOf(
              heuristic.round(heuristic.calc(positiveValue, negativeValue), 2)),
          null, null);
      lang.nextStep();
    }
  }

  /**
   * create a table with all attribute values and the amount of positive and
   * negative examples with this value. add an additional column for the
   * heuristic value and prepare it with underlines
   * 
   * @param examples
   *          training data
   * @param initValue
   *          true = prepare the heuristic value with underlines, false =
   *          prepare the heuristic value with 0.0
   * @return
   */
  public String[][] createConqueredMatrix(String[][] examples,
      boolean initValue) {

    Map<String, String[]> attWithVal = getAttributesWithValues(examples);
    String[][] matrix = hashSetToMatrix(attWithVal);

    String[][] conMatrix = new String[matrix.length + 1][5];

    for (int i = 0; i < conMatrix.length; i++) {
      if (i == 0) {
        conMatrix[0][0] = "Attribute";
        conMatrix[0][1] = "Value";
        conMatrix[0][2] = "Positives (yes)";
        conMatrix[0][3] = "Negatives (no)";
        conMatrix[0][4] = "Heuristic";
        continue;
      }

      for (int j = 0; j < conMatrix[0].length; j++) {

        if (j == 0 || j == 1) {
          conMatrix[i][j] = matrix[i - 1][j];
        } else if (j == 2) {
          conMatrix[i][j] = Integer.toString(countClassAttribute(examples,
              conMatrix[i][0], conMatrix[i][1], "yes"));
        } else if (j == 3) {
          conMatrix[i][j] = Integer.toString(countClassAttribute(examples,
              conMatrix[i][0], conMatrix[i][1], "no"));
        } else if (j == 4 && initValue) {
          conMatrix[i][j] = "0.0";
        } else
          conMatrix[i][j] = "_______";
      }
    }
    return conMatrix;
  }

  /**
   * count the number of example with a specific attribute value and
   * classification
   * 
   * @param examples
   *          training data
   * @param attribute
   * @param attributeValue
   * @param classification
   * @return
   */
  public int countClassAttribute(String[][] examples, String attribute,
      String attributeValue, String classification) {

    int num = 0;

    for (int i = 0; i < examples.length; i++) {
      for (int j = 0; j < examples[0].length; j++) {

        if (examples[0][j].equalsIgnoreCase(attribute)
            && examples[i][j].equalsIgnoreCase(attributeValue)
            && examples[i][examples[0].length - 1]
                .equalsIgnoreCase(classification)) {
          num++;
        }

      }
    }

    return num;
  }

  /**
   * transform hashset to 2D array
   * 
   * @param hashset
   *          hashset that neeed to be transformed
   * @return 2d array with each attribute value in the second columm and the
   *         attribute in the first column
   */
  public String[][] hashSetToMatrix(Map<String, String[]> hashset) {

    int valueLen = 0;

    for (String att : hashset.keySet()) {
      String key = att;
      valueLen += hashset.get(key).length;
    }

    String[][] matrix = new String[valueLen][2];

    int i = 0, j = 0;
    for (String att : hashset.keySet()) {
      j = 0;
      for (int k = i; k < i + hashset.get(att).length; k++) {
        matrix[k][0] = att;
        matrix[k][1] = hashset.get(att)[j];
        j++;
      }
      i = i + hashset.get(att).length;

    }

    return matrix;
  }

  /**
   * get for each attribute all attribute values
   * 
   * @param examples
   *          training data
   * @return hashmap with the attribute and all attribute values in a string
   *         array
   */
  public LinkedHashMap<String, String[]> getAttributesWithValues(
      String[][] examples) {

    LinkedHashMap<String, String[]> attWithVal = new LinkedHashMap<String, String[]>();

    // ignore last column (class)
    for (int j = 0; j < examples[0].length; j++) {
      if (!examples[0][j].equalsIgnoreCase("class")) {
        attWithVal.put(examples[0][j], getValuesForAttribute(examples, j));
      }
    }

    return attWithVal;
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

      for (int j = 0; j < trainingDataValidate[0].length; j++) {

        if (isNumericAttribute(trainingDataValidate[i][j])) {
          throw new IllegalArgumentException(
              "Please use only symbolic attributes");
        }

        if (trainingDataValidate[i][j].equals("")) {
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
        new Coordinates(550, 60), new Coordinates(60, 15),
        new Coordinates(10, 100));

    lang.nextStep();
    lang.hideAllPrimitives();

    MultipleChoiceQuestionModel introQuestion = new MultipleChoiceQuestionModel(
        "introQuestion");
    introQuestion.setPrompt(translator.translateMessage("introQuestion"));
    introQuestion.addAnswer("0",
        translator.translateMessage("introQuestionAnswer1"), 1,
        translator.translateMessage("introQuestionFeedback1"));
    introQuestion.addAnswer("1",
        translator.translateMessage("introQuestionAnswer2"), 0,
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

    String[][] examples = (String[][]) primitives.get("stringMatrix");

    coveredPositivesWithRules = 0;
    coveredNegativesWithRules = 0;

    trainingData = examples;

    // number of rules
    ruleCounter = 0;

    // title for training data
    lang.newText(new Coordinates(10, 10),
        translator.translateMessage("dataset"), "titleTrainingData", null,
        titleProps);

    trainingSet = lang.newStringMatrix(
        new Offset(0, 30, "titleTrainingData", AnimalScript.DIRECTION_NW),
        examples, "trainingSet", null, matrixProps);

    // title for conquered data
    lang.newText(new Offset(50, -30, "trainingSet", AnimalScript.DIRECTION_NE),
        translator.translateMessage("conqueredData"), "titleConqueredData",
        null, titleProps);

    // conquered matrix
    String[][] c = createConqueredMatrix(trainingData, false);
    StringMatrix cM = lang.newStringMatrix(
        new Offset(0, 30, "titleConqueredData", AnimalScript.DIRECTION_NW), c,
        "conqueredData", null, matrixProps);

    // title for pseudo code
    lang.newText(
        new Offset(50, -30, "conqueredData", AnimalScript.DIRECTION_NE),
        "Pseudo Code:", "titlePseudoCode", null, titleProps);

    sc = lang.newSourceCode(
        new Offset(0, 30, "titlePseudoCode", AnimalScript.DIRECTION_NW), "sc",
        null, scProps);
    sc.addCodeLine("1.) Start with an empty theory T and training set E", null,
        0, null);
    sc.addCodeLine(
        "2.) Learn a single (consistent) rule R from E and add it to T", null,
        0, null);
    sc.addCodeLine("3.) If T is satisfactory (complete)", null, 0, null);
    sc.addCodeLine("       return T", null, 0, null);
    sc.addCodeLine("    else:", null, 0, null);
    sc.addCodeLine("       a) Separate: Remove examples explained by R from E",
        null, 0, null);
    sc.addCodeLine("       b) Conquer: goto 2", null, 0, null);

    // title for explanation
    lang.newText(new Offset(0, 30, "sc", AnimalScript.DIRECTION_SW),
        translator.translateMessage("explanation"), "titleExplanation", null,
        titleProps);

    // title for ruleset
    lang.newText(
        new Offset(0, 130, "titleExplanation", AnimalScript.DIRECTION_NW),
        translator.translateMessage("ruleset"), "titleRuleset", null,
        titleProps);

    sc.highlight(0);

    // while not all positive examples are covered
    while (countClassValue(trainingData, "yes") > 0) {

      setExplanationText(" ");
      trainingSet.hide();
      cM.hide();

      trainingSet = lang.newStringMatrix(
          new Offset(0, 30, "titleTrainingData", AnimalScript.DIRECTION_NW),
          trainingData, "trainingSet", null, matrixProps);

      c = createConqueredMatrix(trainingData, false);
      cM = lang.newStringMatrix(
          new Offset(0, 30, "titleConqueredData", AnimalScript.DIRECTION_NW), c,
          "conqueredData", null, matrixProps);

      lang.nextStep();
      sc.unhighlight(0);
      sc.unhighlight(4);

      calcHeuristic(cM);

      String[] highestHeuristic = heuristic.findHighestHeuristic(cM);
      setExplanationText(translator.translateMessage("selectHighestHeuristic"));

      lang.nextStep();

      String[][] tmp = trainingData;

      // check if some negatives examples are covered with the current rule
      if (Double.parseDouble(highestHeuristic[3]) > 0.0) {
        rule = createRule(rule, highestHeuristic[0], highestHeuristic[1]);

        setExplanationText(
            translator.translateMessage("filterAndRefine", rule));

        lang.nextStep();
        cM.hide();

        // some negative examples are still covered -> refine
        String[][] removableExamples = refine(trainingData, highestHeuristic[0],
            highestHeuristic[1]);

        // remove all examples that are covered by the rule
        trainingData = remove(tmp, removableExamples);

        if (countClassValue(getData(trainingSet), "yes") == 0) {
          unhighlightAllSourceCodeLines(sc, 7);
          sc.highlight(2);
          sc.highlight(3);
        }
        rule = null;

      } else {

        // create and print new learned rule
        rule = createRule(rule, highestHeuristic[0], highestHeuristic[1]);
        String pnrule = rule + " -> yes ( p = " + highestHeuristic[2] + ", n = "
            + highestHeuristic[3] + " )";
        ruleCounter++;
        coveredPositivesWithRules += Integer.valueOf(highestHeuristic[2]);
        coveredNegativesWithRules += Integer.valueOf(highestHeuristic[3]);

        setExplanationText(
            translator.translateMessage("noNegativeCovered", rule));
        lang.newText(
            new Offset(0, ruleCounter * 17, "titleRuleset",
                AnimalScript.DIRECTION_SW),
            ruleCounter + ". " + pnrule, "rule", null, textProps);

        lang.nextStep();
        sc.highlight(4);
        sc.highlight(5);
        sc.toggleHighlight(1, 6);
        setExplanationText(
            translator.translateMessage("notAllPositiveCovered", rule));

        rule = null;

        trainingData = remove(trainingData, highestHeuristic[0],
            highestHeuristic[1]);

        if (countClassValue(trainingData, "yes") == 0) {
          unhighlightAllSourceCodeLines(sc, 7);
          sc.highlight(2);
          sc.highlight(3);
        }

        lang.nextStep();
        trainingSet.hide();
        trainingSet = lang.newStringMatrix(
            new Offset(0, 30, "titleTrainingData", AnimalScript.DIRECTION_NW),
            trainingData, "trainingSet", null, matrixProps);

        unhighlightAllSourceCodeLines(sc, 7);
        sc.highlight(1);

        cM.hide();
      }
    }

    // final training data with no positive examples
    trainingSet.hide();
    trainingSet = lang.newStringMatrix(
        new Offset(0, 30, "titleTrainingData", AnimalScript.DIRECTION_NW),
        trainingData, "trainingSet", null, matrixProps);

    c = createConqueredMatrix(trainingData, true);

    cM = lang.newStringMatrix(
        new Offset(0, 30, "titleConqueredData", AnimalScript.DIRECTION_NW), c,
        "conqueredData", null, matrixProps);

    // summary
    String summary = translator.translateMessage("summary",
        String.valueOf(coveredPositivesWithRules),
        String.valueOf(coveredNegativesWithRules));
    lang.newText(new Offset(0, (ruleCounter + 1) * 17, "titleRuleset",
        AnimalScript.DIRECTION_SW), summary, "rule", null, textProps);

    unhighlightAllSourceCodeLines(sc, 7);
    setExplanationText(translator.translateMessage("terminates"));
    lang.nextStep();
    lang.addMCQuestion(outroQuestion);
    lang.finalizeGeneration();
    return lang.toString();
  }

  @Override
  public String getCodeExample() {
    return "1.) Start with an empty theory T and training set E" + "\n"
        + "2.) Learn a single (consistent) rule R from E and add it to T" + "\n"
        + "3.) If T is satisfactory (complete)" + "\n" + "       return T"
        + "\n" + "    else:" + "\n"
        + "       a) Separate: Remove examples explained by R from E" + "\n"
        + "       b) Conquer: goto 2";
  }

}
