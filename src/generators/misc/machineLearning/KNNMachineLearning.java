package generators.misc.machineLearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class KNNMachineLearning extends AbstractInstanceBasedGenerator {

  private StringMatrix          trainingDataMatrix;
  private int                   k;
  private QuickSortStringMatrix qSort;
  private SourceCode            sc;
  private ArrayList<Integer>    numericalIndices;
  private ArrayList<Integer>    nominalIndices;
  private ZeroOneDistance       nominalDistance;
  private NormalizeDistance     numericalDistance;
  private ManhattanDistance     exampleDistance;

  public KNNMachineLearning(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    initProps(props);

    createIntro(translator.translateMessage("algorithmName"),
        translator.translateMessage("description"), new Coordinates(10, 10),
        new Coordinates(500, 60), new Coordinates(60, 15),
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

    String[][] dataSet = (String[][]) primitives.get("stringMatrix");

    String[] example = (String[]) primitives.get("example");

    k = (int) primitives.get("k");

    // get indices
    numericalIndices = getNumericalIndices(dataSet);
    nominalIndices = getNominalIndices(dataSet);

    // title for the training data
    lang.newText(new Coordinates(10, 10),
        translator.translateMessage("dataset"), "titleDataset", null,
        titleProps);

    // training data
    trainingDataMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleDataset", AnimalScript.DIRECTION_SW), dataSet,
        "dataset", null, matrixProps);

    // title for the example
    lang.newText(new Offset(100, -30, "dataset", AnimalScript.DIRECTION_NE),
        translator.translateMessage("example") + "(k = " + k + "):",
        "titleExample", null, titleProps);

    // prepare the example in a 2D array
    String[][] example2D = new String[2][dataSet[0].length];
    for (int i = 0; i < example.length; i++) {
      example2D[0][i] = dataSet[0][i];
      example2D[1][i] = example[i];
    }
    example2D[0][example.length] = "Class";
    example2D[1][example.length] = "?";

    // create distance functions
    nominalDistance = new ZeroOneDistance();
    numericalDistance = new NormalizeDistance(trainingDataMatrix);
    exampleDistance = new ManhattanDistance();

    // create stringmatrix for example
    StringMatrix exampleMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleExample", AnimalScript.DIRECTION_SW), example2D,
        "exampleMatrix", null, matrixProps);

    // title for pseudo code
    lang.newText(
        new Offset(100, -30, "exampleMatrix", AnimalScript.DIRECTION_NE),
        "Pseudo Code:", "titleSourceCode", null, titleProps);

    // create source code
    sc = lang.newSourceCode(
        new Offset(0, 0, "titleSourceCode", AnimalScript.DIRECTION_SW), "sc",
        null, scProps);
    sc.addCodeLine("Compute distance to other training examples", null, 0,
        null);
    sc.addCodeLine("Identify k nearest neighobrs", null, 0, null);
    sc.addCodeLine(
        "Use class labels of nearest neighbors to determine the class label of unknown example",
        null, 0, null);

    // title for the explanation
    lang.newText(new Offset(0, 30, "sc", AnimalScript.DIRECTION_SW),
        translator.translateMessage("explanation"), "titleExplanation", null,
        titleProps);

    // title for the calculation
    lang.newText(new Offset(0, 30, "exampleMatrix", AnimalScript.DIRECTION_SW),
        translator.translateMessage("calculation"), "titleCalculation", null,
        titleProps);

    lang.nextStep();
    sc.highlight(0);

    // add new column for the distance calculation
    dataSet = addDistanceColumn(dataSet);

    trainingDataMatrix.hide();
    trainingDataMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleDataset", AnimalScript.DIRECTION_SW), dataSet,
        "dataset", null, matrixProps);

    // prepare string with the biggest difference for each numerical attribute
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < numericalIndices.size(); i++) {
      sb.append(dataSet[0][numericalIndices.get(i)]).append(" ")
          .append(translator.translateMessage("with")).append(" ")
          .append(getBiggestDifference(trainingDataMatrix,
              numericalIndices.get(i)));
      if (i < numericalIndices.size() - 1) {
        sb.append(" " + translator.translateMessage("and"));
      }
    }

    setExplanationText(
        translator.translateMessage("biggestDiff") + sb.toString());

    lang.nextStep();

    setExplanationText(translator.translateMessage("calcNum") + " "
        + translator.translateMessage(numericalDistance.getDescription())
        + translator.translateMessage("calcNom") + " "
        + translator.translateMessage(nominalDistance.getDescription())
        + translator.translateMessage("calcEx") + " "
        + translator.translateMessage(exampleDistance.getDescription()));

    lang.nextStep();

    // calculate the distances to all examples in trainingDataMatrix
    calcDistance(trainingDataMatrix, example);

    sc.unhighlight(0);

    // STEP 2
    sc.highlight(1);
    setExplanationText(translator.translateMessage("sort"));
    lang.nextStep();
    sortDataSet(trainingDataMatrix);
    lang.nextStep();

    // STEP 3:
    selectFirstKExamples(trainingDataMatrix, k);
    setExplanationText(
        translator.translateMessage("select", String.valueOf(k)));

    lang.nextStep();
    sc.unhighlight(1);
    sc.highlight(2);

    // get the class distribution of the k nearest examples
    HashMap<String, Integer> classes = getKClasses(trainingDataMatrix, k);

    setExplanationText(translator.translateMessage("classDistribution",
        String.valueOf(k), String.valueOf(classes.get("yes")),
        String.valueOf(classes.get("no"))));

    lang.nextStep();

    // majority vote
    if (classes.get("no") > classes.get("yes")) {
      setExplanationText(translator.translateMessage("moreNegative"));
      classify(exampleMatrix, "no");
    } else if (classes.get("no") < classes.get("yes")) {
      setExplanationText(translator.translateMessage("morePositive"));
      classify(exampleMatrix, "yes");
    } else if (classes.get("no") == classes.get("yes")) {
      setExplanationText(translator.translateMessage("equal"));
    }
    lang.nextStep();

    lang.addMCQuestion(outroQuestion);
    lang.finalizeGeneration();
    return lang.toString();
  }

  /**
   * get the class distribution of the first k examples
   * 
   * @param dataSet
   *          examples
   * @param k
   *          first k examples
   * @return String = class, Integer = amount of examples with this class
   */
  public HashMap<String, Integer> getKClasses(StringMatrix dataSet, int k) {

    HashMap<String, Integer> classes = new HashMap<String, Integer>();
    int yes = 0;
    int no = 0;

    for (int i = 1; i <= Math.min(k, dataSet.getNrRows() - 1); i++) {

      if (dataSet.getElement(i, dataSet.getNrCols() - 2)
          .equalsIgnoreCase("no")) {
        no++;
      }
      if (dataSet.getElement(i, dataSet.getNrCols() - 2)
          .equalsIgnoreCase("yes")) {
        yes++;
      }

    }

    classes.put("no", no);
    classes.put("yes", yes);

    return classes;
  }

  /**
   * highlight first k examples
   * 
   * @param dataSet
   *          all examples
   * @param k
   *          first k examples
   */
  public void selectFirstKExamples(StringMatrix dataSet, int k) {
    for (int i = 1; i <= Math.min(k, (dataSet.getNrRows() - 1)); i++) {
      dataSet.highlightCellColumnRange(i, 0, dataSet.getNrCols() - 2, null,
          null);
      dataSet.highlightCell(i, dataSet.getNrCols() - 1, null, null);
    }
  }

  /**
   * sort by the distance
   * 
   * @param dataSet
   *          all examples
   */
  public void sortDataSet(StringMatrix dataSet) {
    qSort = new QuickSortStringMatrix(dataSet);
    qSort.sort(dataSet);
  }

  /**
   * get (max(a) - min(a))
   * 
   * @param examples
   *          all examples
   * @param index
   *          index of numerical attribute
   * @return biggest difference between two values of attribute i
   */
  public double getBiggestDifference(StringMatrix examples, int index) {

    double smallest = Integer.MAX_VALUE;
    double biggest = Integer.MIN_VALUE;

    for (int i = 1; i < examples.getNrRows(); i++) {

      if (Double.parseDouble(examples.getElement(i, index)) > biggest) {
        biggest = Double.parseDouble(examples.getElement(i, index));
      }

      if (Double.parseDouble(examples.getElement(i, index)) < smallest) {
        smallest = Double.parseDouble(examples.getElement(i, index));
      }
    }

    return Math.abs(biggest - smallest);

  }

  /**
   * calculate the distance for all examples in dataset
   * 
   * @param dataSet
   *          all examples
   * @param example
   *          calculate the distance to this example
   */
  public void calcDistance(StringMatrix dataSet, String[] example) {

    double tmpResult = 0.0;

    String stringA;
    String stringB;
    double doubleA;
    double doubleB;

    double results[] = new double[dataSet.getNrCols() - 2];

    for (int i = 1; i < dataSet.getNrRows(); i++) {

      dataSet.highlightCellColumnRange(i, 0, dataSet.getNrCols() - 2, null,
          null);
      dataSet.highlightCell(i, dataSet.getNrCols() - 1, null,
          null);

      SourceCode calc = lang.newSourceCode(
          new Offset(0, 0, "titleCalculation", AnimalScript.DIRECTION_SW),
          "calculation", null, scProps);

      for (int j = 0; j < dataSet.getNrCols() - 2; j++) {

        // numerical attributes
        if (numericalIndices.contains(j)) {

          doubleA = Double.parseDouble(dataSet.getElement(i, j));
          doubleB = Double.parseDouble(example[j]);

          numericalDistance.setCurrentIndex(j);
          tmpResult = round(numericalDistance.calc(doubleA, doubleB),2);
        }

        // nominal attributes
        else if (nominalIndices.contains(j)) {

          stringA = dataSet.getElement(i, j);
          stringB = example[j];

          tmpResult = nominalDistance.calc(stringA, stringB);

        }

        results[j] = tmpResult;

      }

      double res = exampleDistance.calc(results);

      // print calculation for the distance between the two examples
      calc.addCodeLine(exampleDistance.createCalculation(results) + " = " + res,
          null, 0, null);
      dataSet.put(i, dataSet.getNrCols() - 1, Double.toString(res), null, null);
      lang.nextStep();

      dataSet.unhighlightCellColumnRange(i, 0, dataSet.getNrCols() - 2, null,
          null);
      dataSet.unhighlightCell(i, dataSet.getNrCols() - 1, null,
          null);
      calc.hide();
    }
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {

    String[][] trainingDataValidate = (String[][]) primitives
        .get("stringMatrix");
    int classCol = trainingDataValidate[0].length - 1;

    String[] example = (String[]) primitives.get("example");
    int kP = (int) primitives.get("k");

    ArrayList<Integer> numericalIndices = getNumericalIndices(
        trainingDataValidate);
    ArrayList<Integer> nominalIndices = getNominalIndices(trainingDataValidate);

    int positives = countClassValue(trainingDataValidate, "yes");
    int negatives = countClassValue(trainingDataValidate, "no");

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
        if ((nominalIndices.contains(j)
            && isNumericAttribute(trainingDataValidate[i][j])
            || (numericalIndices.contains(j)
                && !isNumericAttribute(trainingDataValidate[i][j])))) {
          throw new IllegalArgumentException(
              "Do not mix nominal and numerical attributes!");
        }

        if (trainingDataValidate[i][j] == null
            || trainingDataValidate[i][j].equals("")) {
          throw new IllegalArgumentException("Empty values are not allowed!");
        }
      }

    }

    if (kP > trainingDataValidate.length-1) {
      throw new IllegalArgumentException(
          "Choose a smaller k. k cannot be greater than the number of examples.");
    }

    for (int i = 0; i < example.length; i++) {

      if ((nominalIndices.contains(i) && isNumericAttribute(example[i])
          || (numericalIndices.contains(i)
              && !isNumericAttribute(example[i])))) {
        throw new IllegalArgumentException(
            "Do not mix nominal and numerical attributes!");
      }

      if (example[i].equals("")) {
        throw new IllegalArgumentException("Empty values are not allowed!");
      }
    }

    if (example.length != trainingDataValidate[0].length - 1) {
      throw new IllegalArgumentException(
          "The example must have the same length!");
    }

    return true;
  }

  @Override
  public String getCodeExample() {
    return "Compute distance to other training examples" + "\n"
        + "Identify k nearest neighobrs" + "\n"
        + "Use class labels of nearest neighbors to determine the class label of unknown example";
  }

}
