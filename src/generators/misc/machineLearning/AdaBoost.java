package generators.misc.machineLearning;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class AdaBoost extends AbstractMachineLearning {

  private CoordinateSystem2D             cs2D;
  private SourceCode                     sc;
  private StringMatrix                   dataSetMatrix;
  private StringMatrix                   decisionStumpX;
  private StringMatrix                   decisionStumpY;
  private LinkedList<Double>             bestError;
  private LinkedList<LinkedList<Double>> classifiers;
  private int                            absoluteMin;
  private int                            absoluteMax;
  private SourceCode                     explanation;

  public AdaBoost(String resourceName, Locale locale) {
    super(resourceName, locale);

  }

  /**
   * combine all classifiers and predict the class
   * 
   * @param examples
   *          training data
   */
  public void test(String[][] examples) {
    String[][] table = new String[examples.length][5 + classifiers.size()];

    for (int i = 0; i < table.length; i++) {
      for (int j = 0; j < table[0].length; j++) {

        // fill table with headline for each column
        if (i == 0) {

          if (j == 0)
            table[i][j] = translator.translateMessage("example");
          else if (j == 1)
            table[i][j] = translator.translateMessage("class");
          else if (j == table[0].length - 3)
            table[i][j] = translator.translateMessage("sumYes");
          else if (j == table[0].length - 2)
            table[i][j] = translator.translateMessage("sumNo");
          else if (j == table[0].length - 1)
            table[i][j] = translator.translateMessage("prediction");
          else
            table[i][j] = "C_" + (j - 1) + " ("
                + +classifiers.get(j - 2).getLast() + ")";

        } else {

          // fill table with classifications
          if (j == 0)
            table[i][j] = "(" + examples[i][j] + "," + examples[i][j + 1] + ")";
          else if (j == 1)
            table[i][j] = examples[i][j + 2];
          else if (j < table[0].length - 3) {
            String s = getClassification(classifiers.get(j - 2), examples);
            if (s.contains(table[i][0])) {
              if (table[i][1].equalsIgnoreCase("yes"))
                table[i][j] = "no";
              else if (table[i][1].equalsIgnoreCase("no"))
                table[i][j] = "yes";
            } else
              table[i][j] = table[i][1];
          } else if (j == table[0].length - 3)
            table[i][j] = String.valueOf(sumOfWeights(table[i], "yes"));
          else if (j == table[0].length - 2)
            table[i][j] = String.valueOf(sumOfWeights(table[i], "no"));
          else if (j == table[0].length - 1)
            table[i][j] = (Double.parseDouble(table[i][j - 2]) >= Double
                .parseDouble(table[i][j - 1])) ? "yes" : "no";
        }
      }
    }

    lang.newStringMatrix(
        new Offset(0, 10, "titleTest", AnimalScript.DIRECTION_SW), table,
        "testTable", null, matrixProps);

  }

  /**
   * calculate the sum of classifier weights for a specific classification
   * 
   * @param example
   *          calculate sum of this example
   * @param classification
   *          which classification
   * @return sum of all classifier weights that predicts the classification
   */
  public double sumOfWeights(String[] example, String classification) {

    double sum = 0.0;

    for (int i = 2; i < example.length - 3; i++) {

      if (example[i].equalsIgnoreCase(classification))
        sum += classifiers.get(i - 2).getLast();
    }

    return round(sum, 3);
  }

  /**
   * get the classification for a specific classifier
   * 
   * @param classifier
   *          classifier
   * @param examples
   *          training data
   * @return classification
   */
  public String getClassification(LinkedList<Double> classifier,
      String[][] examples) {

    String results = getWrongClassifiedExamples(examples,
        classifier.get(1).intValue(), classifier.get(4).intValue(),
        classifier.get(2).intValue());

    return results;
  }

  /**
   * draw all examples
   * 
   * @param dataset
   *          training data
   */
  public void drawPoints(StringMatrix dataset) {
    for (int i = 1; i < dataset.getNrRows(); i++) {

      cs2D.setPoint(Double.parseDouble(dataset.getElement(i, 0)),
          Double.parseDouble(dataset.getElement(i, 1)),
          dataset.getElement(i, 3));

    }
  }

  /**
   * get all examples that are wrong classified for
   * 
   * @param dataset
   * @param value
   *          value for the axis
   * @param axis
   *          0 = x-axis, 1 = y-axis
   * @param cond
   *          0 = less equal (<=), 1: greater (>)
   * @return set as a string with all wrong classified examples
   */
  public String getWrongClassifiedExamples(String[][] dataset, int value,
      int axis, int cond) {

    StringBuilder sb = new StringBuilder();
    int axis2 = (axis == 0) ? 1 : 0;

    for (int i = 1; i < dataset.length; i++) {

      // condition = less equal
      if (cond == 0) {
        if ((Integer.parseInt(dataset[i][axis]) <= value
            && dataset[i][3].equalsIgnoreCase("no"))
            || (Integer.parseInt(dataset[i][axis]) > value
                && dataset[i][3].equalsIgnoreCase("yes"))) {
          sb.append("(" + dataset[i][0] + "," + dataset[i][1] + "), ");
        }

        // condition = greater
      } else if (cond == 1) {
        if (Integer.parseInt(dataset[i][axis]) > value
            && dataset[i][3].equalsIgnoreCase("no")
            || Integer.parseInt(dataset[i][axis]) <= value
                && dataset[i][3].equalsIgnoreCase("yes")) {
          sb.append("(" + dataset[i][0] + "," + dataset[i][1] + "), ");
        }
      }
    }

    String s = sb.toString();
    if (s != null && s.length() != 0) {
      s = s.substring(0, s.length() - 2);
    }

    return s;

  }

  /**
   * start learning
   * 
   * @param dataset
   *          training data
   * @param t
   *          number of iterations
   */
  public void boost(String[][] dataset, int t) {

    // step 1
    sc.toggleHighlight(0, 1);
    setExplanationText(translator.translateMessage("colors"));
    lang.nextStep();
    for (int i = 0; i < t; i++) {

      // step 2
      sc.highlight(2);
      learnClassifier(dataset);

      lang.nextStep();
      sc.toggleHighlight(2, 3);
      sc.highlight(4);

      setExplanationText(translator.translateMessage("bestValue")
          + "Classifier_" + (i + 1) + ": " + formatError(bestError));
      classifiers.add(bestError);

      // step 3
      double weightedErr = computeWeightedError(i);
      drawError(bestError);

      lang.nextStep();
      sc.toggleHighlight(3, 5);
      sc.toggleHighlight(4, 5);

      // step 4
      double classifierWeight = computeClassifierWeight(weightedErr);
      classifiers.get(i).add(round(classifierWeight, 3));
      setExplanationText(translator.translateMessage("calcClassifierWeights")
          + round(classifierWeight, 3));

      lang.nextStep();
      sc.toggleHighlight(5, 6);
      sc.toggleHighlight(5, 7);
      setExplanationText(translator.translateMessage("updateAllWeights"));

      // step 5 & 6
      updateWeights(dataset, i, classifierWeight);
      lang.nextStep();
      sc.toggleHighlight(6, 8);
      sc.toggleHighlight(7, 8);

      // step 7
      normalize(dataset);
      lang.nextStep();
      unhighlightAllSourceCodeLines(sc, 12);
    }

  }

  /**
   * draw error
   * 
   * @param err
   *          error
   */
  public void drawError(LinkedList<Double> err) {

    if (err.get(2) == 0.0) {
      if (err.get(4) == 0.0) {
        cs2D.drawPositiveDecisionStump(absoluteMin, err.get(1).intValue(),
            absoluteMin, absoluteMax);
        cs2D.drawNegativeDecisionStump(err.get(1).intValue(), absoluteMax,
            absoluteMin, absoluteMax);
      } else {
        cs2D.drawPositiveDecisionStump(absoluteMin, absoluteMax, absoluteMin,
            err.get(1).intValue());
        cs2D.drawNegativeDecisionStump(absoluteMin, absoluteMax,
            err.get(1).intValue(), absoluteMax);
      }
    } else {
      if (err.get(4) == 0.0) {
        cs2D.drawNegativeDecisionStump(absoluteMin, err.get(1).intValue(),
            absoluteMin, absoluteMax);
        cs2D.drawPositiveDecisionStump(err.get(1).intValue(), absoluteMax,
            absoluteMin, absoluteMax);
      } else {
        cs2D.drawNegativeDecisionStump(absoluteMin, absoluteMax, absoluteMin,
            err.get(1).intValue());
        cs2D.drawPositiveDecisionStump(absoluteMin, absoluteMax,
            err.get(1).intValue(), absoluteMax);
      }

    }
  }

  /**
   * formate linkedlist to string
   * 
   * @param err
   *          error
   * @return error as string with axis, condition and value
   */
  public String formatError(LinkedList<Double> err) {

    String cond;
    String axis;

    if (err.get(2) == 0.0) {
      cond = " <= ";
    } else
      cond = " > ";

    if (err.get(4) == 0.0) {
      axis = "x";
    } else
      axis = "y";

    return axis + cond + err.get(1);
  }

  /**
   * learn classifier for for each possible split
   * 
   * @param dataset
   *          training data
   */
  public void learnClassifier(String[][] dataset) {

    if (decisionStumpX != null && decisionStumpY != null) {
      decisionStumpX.hide();
      decisionStumpY.hide();
    }

    setExplanationText(translator.translateMessage("createDecStumX"));
    createDecisionStumpsX(dataset);

    setExplanationText(translator.translateMessage("createDecStumY"));
    createDecisionStumpsY(dataset);
  }

  /**
   * get weighted error
   * 
   * @param iteration
   *          index of classifier
   * @return weighted error
   */
  public double computeWeightedError(int iteration) {
    return round(classifiers.get(iteration).get(3), 3);
  }

  /**
   * compute classifier weight for error
   * 
   * @param err
   *          error
   * @return classifier weight
   */
  public double computeClassifierWeight(double err) {
    return 0.5 * Math.log((1 - err) / err);
  }

  /**
   * update weights of all examples
   * 
   * @param dataset
   *          training data
   * @param iteration
   *          iteration number
   * @param classifierWeight
   *          weight of classifier
   */
  public void updateWeights(String[][] dataset, int iteration,
      double classifierWeight) {

    LinkedList<Double> classifier = classifiers.get(iteration);
    int axis = classifier.get(4).intValue();
    int condition = classifier.get(2).intValue();
    int value = classifier.get(1).intValue();

    for (int i = 1; i < dataset.length; i++) {

      if (condition == 0) {

        // positive and classified as positive
        if (Integer.valueOf(dataset[i][axis]) <= value
            && dataset[i][3].equalsIgnoreCase("yes")
            || (Integer.valueOf(dataset[i][axis]) > value
                && dataset[i][3].equalsIgnoreCase("no"))) {
          dataset[i][2] = String.valueOf(round(
              Double.parseDouble(dataset[i][2]) * Math.exp(-classifierWeight),
              4));

        }

        // negative and classified as positive
        else if (Integer.valueOf(dataset[i][axis]) <= value
            && dataset[i][3].equalsIgnoreCase("no")
            || (Integer.valueOf(dataset[i][axis]) > value
                && dataset[i][3].equalsIgnoreCase("yes"))) {
          dataset[i][2] = String.valueOf(round(
              Double.parseDouble(dataset[i][2]) * Math.exp(classifierWeight),
              4));
        }
      }

      if (condition == 1) {

        // positive and classified as positive
        if (Integer.valueOf(dataset[i][axis]) > value
            && dataset[i][3].equalsIgnoreCase("yes")
            || (Integer.valueOf(dataset[i][axis]) <= value
                && dataset[i][3].equalsIgnoreCase("no"))) {
          dataset[i][2] = String
              .valueOf(round(Double.parseDouble(dataset[i][axis])
                  * Math.exp(-classifierWeight), 4));
        }

        // negative and classified as positive
        if (Integer.valueOf(dataset[i][axis]) > value
            && dataset[i][3].equalsIgnoreCase("no")
            || (Integer.valueOf(dataset[i][axis]) <= value
                && dataset[i][3].equalsIgnoreCase("yes"))) {
          dataset[i][2] = String.valueOf(round(
              Double.parseDouble(dataset[i][axis]) * Math.exp(classifierWeight),
              4));
        }

      }
    }

    dataSetMatrix.hide();
    dataSetMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleTrainingData", AnimalScript.DIRECTION_SW),
        dataset, "dataset", null, matrixProps);

  }

  /**
   * normalize the weights
   * 
   * @param dataset
   *          training data
   */
  public void normalize(String[][] dataset) {

    double sum = 0.0;

    for (int i = 1; i < dataset.length; i++) {
      sum += Double.parseDouble(dataset[i][2]);
    }

    for (int i = 1; i < dataset.length; i++) {
      dataset[i][2] = String
          .valueOf(round((Double.parseDouble(dataset[i][2]) / sum), 4));
    }

    dataSetMatrix.hide();
    dataSetMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleTrainingData", AnimalScript.DIRECTION_SW),
        dataset, "dataset", null, matrixProps);

  }

  /**
   * create decision stumps on the x-axis
   * 
   * @param dataset
   *          training data
   */
  public void createDecisionStumpsX(String[][] dataset) {

    int max = getHighestVal(dataset, 0);
    int min = getLowestVal(dataset, 0);

    String[][] decisionX = createEmptyMatrix((max - min) + 2, 3);
    decisionX[0][0] = translator.translateMessage("value");
    decisionX[0][1] = "x <= " + translator.translateMessage("value");
    decisionX[0][2] = "x > " + translator.translateMessage("value");

    decisionStumpX = lang.newStringMatrix(
        new Offset(0, 20, "titleCalc", AnimalScript.DIRECTION_SW), decisionX,
        "decisionX", null, matrixProps);

    for (int i = min; i <= max; i++) {
      lang.nextStep();
      cs2D.hideDecisionStumps();

      decisionStumpX.put(i - min + 1, 0, Integer.toString(i), null, null);

      String s = getWrongClassifiedExamples(dataset, i, 0, 0);
      setExplanationText(translator.translateMessage("sumWrongClassified") + s);

      decisionStumpX.put(i - min + 1, 1,
          String.valueOf(round((getError(dataset, 0, i)[0]), 4)), null, null);
      cs2D.drawPositiveDecisionStump(absoluteMin, i, absoluteMin, absoluteMax);
      cs2D.drawNegativeDecisionStump(i, absoluteMax, absoluteMin, absoluteMax);

      lang.nextStep();

      s = getWrongClassifiedExamples(dataset, i, 0, 1);
      setExplanationText(translator.translateMessage("sumWrongClassified") + s);

      cs2D.hideDecisionStumps();
      cs2D.drawNegativeDecisionStump(absoluteMin, i, absoluteMin, absoluteMax);
      cs2D.drawPositiveDecisionStump(i, absoluteMax, absoluteMin, absoluteMax);
      decisionStumpX.put(i - min + 1, 2,
          String.valueOf(round((getError(dataset, 0, i)[1]), 4)), null, null);

    }

    LinkedList<Double> bestErrX = findBestError(decisionStumpX, 0);
    bestError = bestErrX;

    lang.nextStep();
    setExplanationText(translator.translateMessage("findBestSplitX"));
    decisionStumpX.highlightCell(bestErrX.get(0).intValue(),
        bestErrX.get(2).intValue() + 1, null, null);

    lang.nextStep();
  }

  /**
   * create decision stumps on y-axis
   * 
   * @param dataset
   *          training data
   */
  public void createDecisionStumpsY(String[][] dataset) {

    int max = getHighestVal(dataset, 1);
    int min = getLowestVal(dataset, 1);

    String[][] decisionY = createEmptyMatrix((max - min) + 2, 3);
    decisionY[0][0] = translator.translateMessage("value");
    decisionY[0][1] = "y <= " + translator.translateMessage("value");
    decisionY[0][2] = "y > " + translator.translateMessage("value");

    decisionStumpY = lang.newStringMatrix(
        new Offset(30, 0, "decisionX", AnimalScript.DIRECTION_NE), decisionY,
        "decisionY", null, matrixProps);

    for (int i = min; i <= max; i++) {
      lang.nextStep();
      cs2D.hideDecisionStumps();

      String s = getWrongClassifiedExamples(dataset, i, 1, 0);
      setExplanationText(translator.translateMessage("sumWrongClassified") + s);

      decisionStumpY.put(i - min + 1, 0, Integer.toString(i), null, null);
      decisionStumpY.put(i - min + 1, 1,
          String.valueOf(round((getError(dataset, 1, i)[0]), 4)), null, null);
      cs2D.drawPositiveDecisionStump(absoluteMin, absoluteMax, absoluteMin, i);
      cs2D.drawNegativeDecisionStump(absoluteMin, absoluteMax, i, absoluteMax);

      lang.nextStep();

      cs2D.hideDecisionStumps();
      s = getWrongClassifiedExamples(dataset, i, 1, 1);
      setExplanationText(translator.translateMessage("sumWrongClassified") + s);

      decisionStumpY.put(i - min + 1, 2,
          String.valueOf(round((getError(dataset, 1, i)[1]), 4)), null, null);
      cs2D.drawNegativeDecisionStump(absoluteMin, absoluteMax, absoluteMin, i);
      cs2D.drawPositiveDecisionStump(absoluteMin, absoluteMax, i, absoluteMax);

    }

    LinkedList<Double> bestErrY = findBestError(decisionStumpY, 1);

    if (bestError.get(3) > bestErrY.get(3)) {
      bestError = bestErrY;
    }

    lang.nextStep();
    setExplanationText(translator.translateMessage("findBestSplitY"));
    decisionStumpY.highlightCell(bestErrY.get(0).intValue(),
        bestErrY.get(2).intValue() + 1, null, null);
  }

  /**
   * get the best error for a specific axis
   * 
   * @param m
   *          decision stumps
   * @param axis
   *          0 = x-axis; 1 = y-axis;
   * @return
   */
  public LinkedList<Double> findBestError(StringMatrix m, int axis) {
    int rowID = 0;
    int bestCondition = 0;
    double val = 0;
    double e = Double.MAX_VALUE;

    LinkedList<Double> err = new LinkedList<Double>();

    for (int i = 1; i < m.getNrRows(); i++) {

      if (Double.parseDouble(m.getElement(i, 1)) < e) {
        val = Double.parseDouble(m.getElement(i, 0));
        e = Double.parseDouble(m.getElement(i, 1));
        rowID = i;
        bestCondition = 0;
      } else if (Double.parseDouble(m.getElement(i, 2)) < e) {
        val = Double.parseDouble(m.getElement(i, 0));
        e = Double.parseDouble(m.getElement(i, 2));
        rowID = i;
        bestCondition = 1;
      }
    }

    // fill error: < row number of decision stumps, axis value, condition (<= /
    // >), error, axis
    err.add((double) rowID);
    err.add(val);
    err.add((double) bestCondition);
    err.add(e);
    err.add((double) axis);

    return err;
  }

  /**
   * get error for value and axis split
   * 
   * @param dataset
   *          training data
   * @param axis
   *          0 = x-axis, 1 = y-axis
   * @param value
   *          split position
   * @return error for axis <= value and axis > value
   */
  public double[] getError(String[][] dataset, int axis, int value) {

    double[] error = new double[2];

    for (int i = 1; i < dataset.length; i++) {

      if (Integer.valueOf(dataset[i][axis]) <= value
          && dataset[i][3].equalsIgnoreCase("no")
          || (Integer.valueOf(dataset[i][axis]) > value
              && dataset[i][3].equalsIgnoreCase("yes"))) {
        error[0] = error[0] + Double.parseDouble(dataset[i][2]);

      } else if ((Integer.valueOf(dataset[i][axis]) > value
          && dataset[i][3].equalsIgnoreCase("no"))
          || (Integer.valueOf(dataset[i][axis]) <= value
              && dataset[i][3].equalsIgnoreCase("yes"))) {
        error[1] = error[1] + Double.parseDouble(dataset[i][2]);
      }
    }

    return error;

  }

  /**
   * get highest value for axis
   * 
   * @param dataset
   *          training data
   * @param index
   *          0 = x-axis, 1 = y-axis
   * @return highest value
   */
  public int getHighestVal(String[][] dataset, int index) {
    int max = Integer.MIN_VALUE;

    for (int i = 1; i < dataset.length; i++) {
      if (Integer.valueOf(dataset[i][index]) > max) {
        max = Integer.valueOf(dataset[i][index]);
      }
    }
    return max;
  }

  /**
   * get lowest value for axis
   * 
   * @param dataset
   *          training data
   * @param index
   *          0 = x-axis, 1 = y-axis
   * @return lowest value
   */
  public int getLowestVal(String[][] dataset, int index) {
    int min = Integer.MAX_VALUE;

    for (int i = 1; i < dataset.length; i++) {
      if (Integer.valueOf(dataset[i][index]) < min) {
        min = Integer.valueOf(dataset[i][index]);
      }
    }
    return min;
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
              "Only numeric attributes are allowed.");
        }

        if (trainingDataValidate[i][j] == null
            || trainingDataValidate[i][j].equals("")) {
          throw new IllegalArgumentException("Missing values are not allowed!");
        }

        try {

          int num = Integer.parseInt(trainingDataValidate[i][j]);

          if (num < 0 || num > 10) {
            throw new IllegalArgumentException(
                "Please use only values between 0 and 10");
          }
        }

        catch (NumberFormatException e) {
          throw new IllegalArgumentException(
              "Only integer values are allowed.");
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
        new Coordinates(350, 60), new Coordinates(60, 15),
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

    int t = (int) primitives.get("iteration");

    LinkedList<Double> xValues = new LinkedList<Double>();
    LinkedList<Double> yValues = new LinkedList<Double>();

    for (int i = 1; i < examples.length; i++) {
      xValues.add(Double.parseDouble(examples[i][0]));
      yValues.add(Double.parseDouble(examples[i][1]));
    }

    absoluteMin = 0;
    absoluteMax = 10;

    String[][] data = new String[examples.length][examples[0].length + 1];
    for (int i = 0; i < examples.length; i++) {

      data[i][0] = examples[i][0];
      data[i][1] = examples[i][1];

      if (i == 0) {
        data[i][2] = translator.translateMessage("weight");
      } else
        data[i][2] = String.valueOf(round(1.0 / (examples.length - 1), 2));

      data[i][3] = examples[i][2];
    }

    // title for training data
    Text titleTrainingData = lang.newText(new Coordinates(30, 30),
        translator.translateMessage("dataset"), "titleTrainingData", null,
        titleProps);

    dataSetMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleTrainingData", AnimalScript.DIRECTION_SW), data,
        "dataset", null, matrixProps);

    // title for training data
    Text titleCS = lang.newText(
        new Offset(60, 0, "titleTrainingData", AnimalScript.DIRECTION_NE),
        translator.translateMessage("cs"), "cs", null, titleProps);

    // set coordinate system
    cs2D = new CoordinateSystem2D(lang, new Coordinates(250, 320), 250, 250,
        0.0, 10.0);
    cs2D.createCoordinateSystem();
    cs2D.labelAxis();

    // title for calculation
    Text titleCalc = lang.newText(
        new Offset(400, 0, "titleTrainingData", AnimalScript.DIRECTION_NE),
        translator.translateMessage("calculation"), "titleCalc", null,
        titleProps);

    // title for pseudo Code
    lang.newText(new Offset(330, 0, "titleCalc", AnimalScript.DIRECTION_NE),
        "Pseuco Code:", "titlePseudoCode", null, titleProps);

    sc = lang.newSourceCode(
        new Offset(0, 30, "titlePseudoCode", AnimalScript.DIRECTION_NW), "sc",
        null, scProps);
    sc.addCodeLine("1.) Initialize example weights w_i = 1/N", null, 0, null);
    sc.addCodeLine("2.) for m = 1 to t", null, 0, null);
    sc.addCodeLine(
        "a) learn a classifier C_m using the current example weights", null, 1,
        null);
    sc.addCodeLine("b) compute a weighted error estimate:", null, 1, null);
    sc.addCodeLine("   err = sum of(w_i of all incorrect classified e_i)", null,
        1, null);
    sc.addCodeLine(
        "c) compute a classifier weight: a_m = 1/2*ln( (1- err_m)/ err_m )",
        null, 1, null);
    sc.addCodeLine(
        "d) for all correctly classified examples e_i: w_i = w*exp(-a_m) ", null,
        1, null);
    sc.addCodeLine(
        "e) for all correctly classified examples e_i: w_i = w*exp(a_m) ", null,
        1, null);
    sc.addCodeLine("f) normalize the weights w_i so that they sum to 1", null,
        1, null);
    sc.addCodeLine("3.) for each test example", null, 0, null);
    sc.addCodeLine("a) try all classifiers C_m", null, 1, null);
    sc.addCodeLine(
        "b) predict the class that receives the highest sum of weights a_m",
        null, 1, null);

    // title for explanation
    lang.newText(new Offset(0, 50, "sc", AnimalScript.DIRECTION_SW),
        translator.translateMessage("explanation"), "titleExplanation", null,
        titleProps);

    classifiers = new LinkedList<LinkedList<Double>>();

    drawPoints(dataSetMatrix);

    explanation = lang.newSourceCode(
        new Offset(0, 0, "titleExplanation", AnimalScript.DIRECTION_SW),
        "explanation", null, scProps);
    explanation.addMultilineCode(" ", null, null);

    setExplanationText(translator.translateMessage("initWeights",
        String.valueOf(examples.length - 1),
        String.valueOf(round(1.0 / (examples.length - 1), 2))));
    sc.highlight(0);

    lang.nextStep();

    boost(data, t);

    dataSetMatrix.hide();
    cs2D.hide();
    decisionStumpX.hide();
    decisionStumpY.hide();
    titleCalc.hide();
    titleTrainingData.hide();
    titleCS.hide();

    sc.highlight(9);
    sc.highlight(10);
    sc.highlight(11);
    // title for training data

    setExplanationText(translator.translateMessage("combine"));
    lang.newText(new Coordinates(30, 30), "Test:", "titleTest", null,
        titleProps);
    test(data);

    lang.addMCQuestion(outroQuestion);
    lang.finalizeGeneration();
    return lang.toString();

  }

  @Override
  public String getCodeExample() {
    return "1.) Initialize example weights w_i = 1/N" + "\n"
        + "2.) for m = 1 to t" + "\n"
        + "       a) learn a classifier C_m using the current example weights"
        + "\n" + "       b) compute a weighted error estimate:" + "\n"
        + "       err = sum of(w_i of all incorrect classified e_i)" + "\n"
        + "       c) compute a classifier weight: a_m = 1/2*ln( (1- err_m)/ err_m )"
        + "\n"
        + "       d) for all correctly classified examples e_i: w_i = w*exp(-a_m) "
        + "\n"
        + "       e) for all correctly classified examples e_i: w_i = w*exp(a_m) "
        + "\n" + "       f) normalize the weights w_i so that they sum to 1"
        + "\n" + "3.) for each test example" + "\n"
        + "       a) try all classifiers C_m" + "\n"
        + "       b) predict the class that receives the highest sum of weights a_m";
  }
}
