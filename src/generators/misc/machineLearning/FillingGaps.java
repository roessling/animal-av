package generators.misc.machineLearning;

import java.util.ArrayList;
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

public class FillingGaps extends AbstractInstanceBasedGenerator {

  public FillingGaps(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  private SourceCode            sc;
  private StringMatrix          dataSetMatrix;
  private ArrayList<Integer>    numericalIndices;
  private ArrayList<Integer>    nominalIndices;
  private ZeroOneDistance       nominalDistance;
  private ManhattanDistance     exampleDistance;
  private NormalizeDistance     numericalDistance;
  private MajorityAverageVote   majorityAverageVote;
  private QuickSortStringMatrix qSort;
  private int                   kParam;
  private LinkedList<int[]> missingValues;

  /**
   * fill all gaps in matrix m
   * 
   * @param m
   *          matrix
   */
  public void fillGaps(StringMatrix m) {

    int previousLine = 0;

    for (int i = 1; i < m.getNrRows(); i++) {
      // check if row contains gap
      if (rowContainsGap(m, i)) {
        
        
        
        
        m.highlightCellColumnRange(i, 0, m.getNrCols() - 2, null, null);
        m.highlightCell(i, m.getNrCols() - 1, null, null);
        

        m.unhighlightCellColumnRange(previousLine, 0, m.getNrCols() - 2, null,
            null);
        m.unhighlightCell(previousLine, m.getNrCols() - 1, null,
            null);
        calcDistanceForExample(m, i);
        previousLine = i;
        sc.unhighlight(4);
      }
    }
  }

  public void calcDistanceForExample(StringMatrix m, int row) {

    String[][] examples = getData(m);

    String classValue = m.getElement(row, m.getNrCols() - 1);

    String[][] filteredExamples = addDistanceColumn(
        filterClass(examples, classValue, row));

    // safe all results
    double[] results = new double[m.getNrCols() - 1];
    SourceCode calc;
    double result = 0.0;

    // string operands
    String stringA = " ";
    String stringB = " ";

    // double operands
    double doubleA = 0.0;
    double doubleB = 0.0;

    // show matrix with the same class as the example with the missing value
    StringMatrix filteredM = lang.newStringMatrix(
        new Offset(0, 30, "titleFilteredExamples", AnimalScript.DIRECTION_NW),
        filteredExamples, "fil", null, matrixProps);

    // calculate for each filtered example the distance
    for (int i = 1; i < filteredM.getNrRows(); i++) {
      calc = lang.newSourceCode(
          new Offset(0, 10, "explanation", AnimalScript.DIRECTION_SW), "calc",
          null, expProps);

      setExplanationText(translator.translateMessage("filter",
          m.getElement(row, m.getNrCols() - 1)));

      sc.highlight(2);
      lang.nextStep();

      for (int j = 0; j < filteredM.getNrCols() - 1; j++) {

        // nominal attributes
        if (nominalIndices.contains(j)) {
          stringA = filteredM.getElement(i, j);
          if (stringA == null)

            if (m.getElement(row, j).equalsIgnoreCase("X")) {
              stringB = stringA;
            } else {
              stringB = m.getElement(row, j);
            }

          results[j] = nominalDistance.calc(stringA, stringB);

          // numerical attributes
        } else if (numericalIndices.contains(j)) {

          if (filteredM.getElement(i, j).equalsIgnoreCase("X")) {
            doubleA = 0.0;
            doubleB = getMaxDistance(m, j);

          } else {
            doubleA = Double.parseDouble(filteredM.getElement(i, j));

            if (m.getElement(row, j).equalsIgnoreCase("X")) {
              doubleB = doubleA;
            } else {
              doubleB = Double.parseDouble(m.getElement(row, j));
            }
          }

          numericalDistance.setCurrentIndex(j);
          results[j] = round(numericalDistance.calc(doubleA, doubleB), 2);
        }
        result = round(exampleDistance.calc(results), 2);
      }

      // print calculation for the distance between the two examples
      calc.addCodeLine(
          exampleDistance.createCalculation(results) + " = " + result, null, 0,
          null);
      filteredM.put(i, filteredM.getNrCols() - 1, String.valueOf(result), null,
          null);
      result = 0.0;
      calc.hide();
    }

    lang.nextStep();
    classify(m, filteredM, row, kParam);

  }

  /**
   * classify example
   * 
   * @param m
   *          training data
   * @param filteredExamples
   *          example with the same class as the example with the missing value
   * @param row
   * @param k
   *          check first k examples
   */
  public void classify(StringMatrix m, StringMatrix filteredExamples, int row,
      int k) {

    sc.toggleHighlight(2, 3);
    setExplanationText(translator.translateMessage("sort", String.valueOf(k)));

    qSort = new QuickSortStringMatrix(filteredExamples);
    qSort.sort(filteredExamples);

    // get column index of the missing value
    int indexToClassify = 0;
    for (int i = 0; i < m.getNrCols(); i++) {
      if (m.getElement(row, i).equalsIgnoreCase("X")) {
        indexToClassify = i;
      }
    }

    lang.nextStep();
    String message = "";
    String type = "";

    // nominal attribute
    if (nominalIndices.contains(indexToClassify)) {
      String[] values = getValuesForAttribute(filteredExamples,
          indexToClassify);
      message = majorityAverageVote.classifySymbolic(m, filteredExamples,
          values, indexToClassify, row, k);
      type = "classifySymbolic";
      // numerical attribute
    } else if (numericalIndices.contains(indexToClassify)) {
      message = majorityAverageVote.classifyNumeric(m, filteredExamples,
          indexToClassify, row, k);
      type = "classifyNumeric";
    }

    setExplanationText(translator.translateMessage(type, message));
    lang.nextStep();
    filteredExamples.hide();
  }

  /**
   * filter examples of a special class
   * 
   * @param examples
   *          training data
   * @param classValue
   *          classification
   * @param row
   *          row of the example that don't need to be filtered
   * @return examples with the classification classValue
   */
  public String[][] filterClass(String[][] examples, String classValue,
      int row) {

    int lastColumn = examples[0].length - 1;

    int num = countClassValue(examples, classValue);

    String[][] filtered = new String[num][examples[0].length];

    int k = 1;
    for (int i = 0; i < examples.length; i++) {

      if (i == 0) {
        filtered[0] = examples[0];
        //filtered[0][filtered[0].length] = translator.translateMessage("distance");
      } else if (examples[i][lastColumn].equalsIgnoreCase(classValue)
          && i != row) {
        filtered[k] = examples[i];
        k++;
      }
    }
    return filtered;
  }

  /**
   * get the column index of the missing value
   * 
   * @param m
   *          training data
   * @param row
   *          row of the example
   * @return column index
   */
  public int getEmptyIndex(StringMatrix m, int row) {

    for (int i = 1; i < m.getNrCols(); i++) {
      if (m.getElement(row, i).equalsIgnoreCase("X")) {
        return i;
      }
    }
    return 0;
  }

  /**
   * check if the example contains a missing value
   * 
   * @param m
   *          training data
   * @param row
   *          row index of the example
   * @return true if the row contains a missing value
   */
  public boolean rowContainsGap(StringMatrix m, int row) {

    for (int i = 0; i < m.getNrCols(); i++) {
      if (m.getElement(row, i).equalsIgnoreCase("X")) {
        missingValues.add(new int[] {row, i});
        return true;
      }

    }
    return false;

  }

  /**
   * remove all examples with a missing classification
   * 
   * @param m
   *          training data
   * @return filtered training data
   */
  public StringMatrix removeClasslessExamples(StringMatrix m) {

    String[][] s = getData(m);

    int counter = 0;
    for (int i = 0; i < s.length; i++) {
      if (s[i][s[0].length - 1].equalsIgnoreCase("X")) {
        counter++;
        m.highlightCellColumnRange(i, 0, m.getNrCols() - 2, null, null);
        m.highlightCell(i, m.getNrCols() - 1, null, null);
      }

    }

    String[][] removed = new String[s.length - counter][s[0].length];
    int k = 0;
    for (int i = 0; i < s.length; i++) {

      if (!s[i][s[0].length - 1].equalsIgnoreCase("X")) {
        removed[k] = s[i];
        k++;
      }

    }
    lang.nextStep();
    m.hide();
    StringMatrix sm = lang.newStringMatrix(
        new Offset(0, 10, "titleTrainingData", AnimalScript.DIRECTION_SW),
        removed, "data", null, matrixProps);

    return sm;
  }

  /**
   * get the biggest difference between two numerical values
   * 
   * @param examples
   *          training data
   * @param index
   *          column index of numerical attribute
   * @return biggest difference
   */
  public double getMaxDistance(StringMatrix examples, int index) {

    double smallest = Integer.MAX_VALUE;
    double biggest = Integer.MIN_VALUE;

    for (int i = 1; i < examples.getNrRows(); i++) {

      if (!examples.getElement(i, index).equalsIgnoreCase("X")) {

        if (Double.parseDouble(examples.getElement(i, index)) > biggest) {
          biggest = Double.parseDouble(examples.getElement(i, index));
        }

        if (Double.parseDouble(examples.getElement(i, index)) < smallest) {
          smallest = Double.parseDouble(examples.getElement(i, index));
        }
      }
    }

    return Math.abs(biggest - smallest);

  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {

    String[][] trainingDataValidate = (String[][]) primitives
        .get("stringMatrix");
    int k = (int) primitives.get("k");

    int positives = countClassValue(trainingDataValidate, "yes");
    int negatives = countClassValue(trainingDataValidate, "no");

    int classCol = trainingDataValidate[0].length - 1;

    ArrayList<Integer> numericalIndices = getNumericalIndices(
        trainingDataValidate);
    ArrayList<Integer> nominalIndices = getNominalIndices(trainingDataValidate);

    for (int i = 1; i < trainingDataValidate.length; i++) {

      if (!trainingDataValidate[i][classCol].equalsIgnoreCase("yes")
          && !trainingDataValidate[i][classCol].equalsIgnoreCase("no")
          && !trainingDataValidate[i][classCol].equalsIgnoreCase("X")) {
        throw new IllegalArgumentException(
            "The last attribute classifies the training example. Please specifiy the class of each example by adding 'yes' or 'no' to the class attribute (the last element of a training example).");
      }

      for (int j = 0; j < trainingDataValidate[0].length; j++) {
        if ((nominalIndices.contains(j)
            && (isNumericAttribute(trainingDataValidate[i][j])
                && !trainingDataValidate[i][j].equalsIgnoreCase("X"))
            || (numericalIndices.contains(j)
                && (!isNumericAttribute(trainingDataValidate[i][j])
                    && !trainingDataValidate[i][j].equalsIgnoreCase("X"))))) {
          throw new IllegalArgumentException(
              "Do not mix nominal and numerical attributes!");
        }

        if (trainingDataValidate[i][j] == null
            || trainingDataValidate[i][j].equals("")) {
          throw new IllegalArgumentException(
              "Mark the missing values with 'X'");
        }
      }

    }

    if (k >= Math.min(positives, negatives)) {
      throw new IllegalArgumentException(
          "Choose a smaller k. k cannot be greater than the number of classes with the fewest examples minus one");
    }

    return true;
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    initProps(props);

    createIntro(translator.translateMessage("algorithmName"),
        translator.translateMessage("description"), new Coordinates(10, 10),
        new Coordinates(400, 60), new Coordinates(60, 15),
        new Coordinates(10, 100));

    lang.nextStep();
    lang.hideAllPrimitives();
    
    missingValues = new LinkedList<int[]>();

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

    kParam = (int) primitives.get("k");

    numericalIndices = getNumericalIndices(examples);
    nominalIndices = getNominalIndices(examples);

    // title for training data
    lang.newText(new Coordinates(10, 10),
        translator.translateMessage("dataset"), "titleTrainingData", null,
        titleProps);

    dataSetMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleTrainingData", AnimalScript.DIRECTION_SW),
        examples, "dataset", null, matrixProps);

    // title for calculation
    lang.newText(new Offset(50, -30, "dataset", AnimalScript.DIRECTION_NE),
        translator.translateMessage("filteredExamples"),
        "titleFilteredExamples", null, titleProps);

    // title for pseudo Code
    lang.newText(
        new Offset(300, 0, "titleFilteredExamples", AnimalScript.DIRECTION_NE),
        "Pseuco Code:", "titlePseudoCode", null, titleProps);

    sc = lang.newSourceCode(
        new Offset(0, 30, "titlePseudoCode", AnimalScript.DIRECTION_NW), "sc",
        null, scProps);

    sc.addCodeLine("1.) Remove all classless examples", null, 0, null);
    sc.addCodeLine("2.) for each example with missing values", null, 0, null);
    sc.addCodeLine("a) calculate distance to all examples with the same class", null,
        1, null);
    sc.addCodeLine("b) select k nearest examples", null, 1, null);
    sc.addCodeLine(
        "c) fill the missing value according to the k nearest neighbors", null,
        1, null);

    majorityAverageVote = new MajorityAverageVote(lang, sc);
    nominalDistance = new ZeroOneDistance();
    exampleDistance = new ManhattanDistance();
    numericalDistance = new NormalizeDistance(dataSetMatrix);

    // title for explanation
    lang.newText(new Offset(0, 50, "sc", AnimalScript.DIRECTION_SW),
        translator.translateMessage("explanation"), "titleExplanation", null,
        titleProps);
    lang.nextStep();
    setExplanationText(translator.translateMessage("classless"));
    sc.highlight(0);
    StringMatrix removed = removeClasslessExamples(dataSetMatrix);
    sc.toggleHighlight(0, 1);

    String nominal = translator.translateMessage("calcNom")
        + nominalDistance.getName() + "\n"
        + translator.translateMessage("zeroOneDistanceDescription") + "\n \n";
    String numerical = translator.translateMessage("calcNum")
        + numericalDistance.getName() + "\n"
        + translator.translateMessage("normalizeDistanceDescription") + "\n \n";
    String distance = translator.translateMessage("calcEx")
        + exampleDistance.getName() + "\n"
        + translator.translateMessage("manhattanDistanceDescription") + "\n ";
    setExplanationText(nominal + numerical + distance);

    lang.nextStep();
    fillGaps(removed);

    lang.nextStep();
    unhighlightAll(removed);
    unhighlightAllSourceCodeLines(sc, 5);
    setExplanationText(translator.translateMessage("terminates"));
    
    for(int i = 0; i< missingValues.size(); i++) {
      removed.highlightCell(missingValues.get(i)[0], missingValues.get(i)[1], null, null);
    }
    
    lang.nextStep();
    lang.addMCQuestion(outroQuestion);
    lang.finalizeGeneration();
    return lang.toString();
  }

  @Override
  public String getCodeExample() {
    return "1.) Remove all classless examples" + "\n"
        + "2.) for each example with missing values" + "\n"
        + "       a) calculate distance to all examples with the same class" + "\n"
        + "       b) select k nearest examples" + "\n"
        + "       c) fill the missing value according to the k nearest neighbors"
        + "\n";
  }
}
