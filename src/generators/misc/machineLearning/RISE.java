package generators.misc.machineLearning;

import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

public class RISE implements ValidatingGenerator {

  private Language             lang;
  private SourceCode           explanation;
  private SourceCode           sc;
  private Translator           translator;
  private StringMatrix         distanceMatrix;
  private SourceCodeProperties expProps;
  private SourceCodeProperties scProps;
  private TextProperties       titleProps;
  private StringMatrix         datasetMatrix;
  private MatrixProperties     matrixProps;
  private LinkedList<String>   distanceList;
  private Variables vars;
  
  /*
   * new RISE("generators/misc/machineLearning/helperRISE/PseudoRISE", Locale.US)
   * new RISE("generators/misc/machineLearning/helperRISE/PseudoRISE", Locale.GERMANY)
   */
  
  public RISE(String resourceName, Locale locale) {
    lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
        getAlgorithmName(), getAnimationAuthor(), 1000, 1000);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    translator = new Translator(resourceName, locale);
  }

  public String[][] initialAccuracy(String[][] dataset) {

    String[][] table = createEmptyMatrix(dataset.length, dataset.length + 2);
    table[0][table[0].length - 2] = "NN";
    table[0][table[0].length - 1] = translator.translateMessage("distance");

    for (int i = 0; i < table.length - 1; i++) {
      table[0][i + 1] = dataset[i + 1][0];
      table[i + 1][0] = dataset[i + 1][0];
    }

    for (int i = 1; i < table.length; i++) {

      for (int j = 1; j < table.length; j++) {

        if (i != j) {
          table[i][j] = String.valueOf(
              round(calcVDMForExamples(dataset, dataset[i], dataset[j]), 2));
        }
      }
    }
    return table;

  }

  public void showVDMCalc(StringMatrix m, StringMatrix d, int aIndex,
      int bIndex) {

    String s = "";

    for (int i = 0; i < distanceList.size(); i++) {
      s = s + distanceList.get(i);
    }

    s = s + " \n" + translator.translateMessage("exampleCalc") + "\n";
    setExplanationText(s);
    lang.nextStep();

    String[] a = getData(m)[aIndex];
    String[] b = getData(m)[bIndex];

    m.highlightCellColumnRange(aIndex, 0, m.getNrCols() - 2, null, null);
    m.highlightCell(aIndex, m.getNrCols() - 1, null, null);

    m.highlightCellColumnRange(bIndex, 0, m.getNrCols() - 2, null, null);
    m.highlightCell(bIndex, m.getNrCols() - 1, null, null);

    d.highlightCell(aIndex, bIndex, null, null);
    d.highlightElem(aIndex, bIndex, null, null);
    
    s = s + "d(" + m.getElement(aIndex, 0) + ", " + m.getElement(bIndex, 0) + ")\n=    ";
    setExplanationText(s);
    lang.nextStep();
    for (int i = 1; i < a.length - 1; i++) {

      m.highlightElem(aIndex, i, null, null);
      m.highlightElem(bIndex, i, null, null);

      s = s +  "    d(" + m.getElement(aIndex, i) + ", " + m.getElement(bIndex, i)
          + ") \n";

      if (i < (a.length - 2)) {
        s = s + "+    ";
      }

      setExplanationText(s);
      lang.nextStep();

    }

    s = s + " \n=    ";

    for (int i = 1; i < a.length - 1; i++) {

      m.highlightElem(aIndex, i, null, null);
      m.highlightElem(bIndex, i, null, null);

      s = s + "    " + round(Math
          .pow(calcVDMForAttribute(getClassDistribution(getData(m), a[i], i),
              getClassDistribution(getData(m), b[i], i)), 2),
          2) + "\n";

      if (i < (a.length - 2)) {
        s = s + "+    ";
      }

      setExplanationText(s);
      lang.nextStep();
    }

    s = s + "=        " + round(calcVDMForExamples(getData(m), a, b), 2);
    setExplanationText(s);
    lang.nextStep();

    unhighlightAll(m);
    unhighlightAll(d);

  }

  public double calcVDMForExamples(String[][] dataset, String[] exampleA,
      String[] exampleB) {

    double sum = 0.0;
    for (int i = 1; i < exampleA.length - 1; i++) {

      if (!exampleB[i].equals("?")) {
        double res = Math.pow(
            calcVDMForAttribute(getClassDistribution(dataset, exampleA[i], i),
                getClassDistribution(dataset, exampleB[i], i)),
            2);
        sum = sum + res;
      }
    }

    return sum;
  }

  public double calcVDMForAttribute(int[] distributionA, int[] distributionB) {

    double result = 0.0;

    for (int i = 0; i < distributionA.length; i++) {
      result = result + Math.abs(
          ((double) distributionA[i] / (distributionA[0] + distributionA[1]))
              - ((double) distributionB[i]
                  / (distributionB[0] + distributionB[1])));
    }

    return round(result, 2);
  }

  public int[] getClassDistribution(String[][] dataset, String val,
      int colIndex) {

    int positives = 0;
    int negatives = 0;

    int numOfCol = dataset[0].length;

    for (int i = 1; i < dataset.length; i++) {

      if (dataset[i][colIndex].equalsIgnoreCase(val)) {

        if (dataset[i][numOfCol - 1].equalsIgnoreCase("yes")) {
          positives++;
        } else {
          negatives++;
        }
      }

    }

    return new int[] { positives, negatives };
  }

  public void createIntro(String headline, String description, Coordinates uL,
      Coordinates lR, Coordinates tUL, Coordinates dC) {

    RectProperties headlinebgProps = new RectProperties();
    RectProperties shadowProps = new RectProperties();

    // shadow properties (back rectangle in the background)
    shadowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    shadowProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    shadowProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    shadowProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    // headline rectangle properties
    headlinebgProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headlinebgProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    headlinebgProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    headlinebgProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    // shadow rectangle
    lang.newRect(uL, lR, "headlinebg", null, headlinebgProps);
    Coordinates shadowUpperLeft = new Coordinates(uL.getX() + 10,
        uL.getY() + 10);
    Coordinates shadowLowerRight = new Coordinates(lR.getX() + 10,
        lR.getY() + 10);

    // headline rectangle
    lang.newRect(shadowUpperLeft, shadowLowerRight, "shadow", null,
        shadowProps);
    TextProperties headlineProps = new TextProperties();
    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.BOLD, 30));

    // headline text
    lang.newText(tUL, headline, "headline", null, headlineProps);

    SourceCodeProperties descProps = new SourceCodeProperties();
    descProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 12));

    SourceCode desc = lang.newSourceCode(dC, "desc", null, descProps);
    desc.addMultilineCode(description, "desc", null);
  }

  public void setExplanationText(String s) {
    if (explanation != null) {
      explanation.hide();
    }
    explanation = lang.newSourceCode(
        new Offset(0, 0, "titleExplanation", AnimalScript.DIRECTION_SW),
        "explanation", null, expProps);
    explanation.addMultilineCode(s, null, null);
  }

  public double round(double v, int d) {
    return Math.round(v * Math.pow(10.0, d)) / Math.pow(10.0, d);
  }

  public String[][] createEmptyMatrix(int row, int col) {

    String[][] m = new String[row][col];

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        m[i][j] = " ";
      }
    }

    return m;
  }

  public void createIntroWithoutDesc(String headline, String description,
      Coordinates uL, Coordinates lR, Coordinates tUL) {

    RectProperties headlinebgProps = new RectProperties();
    RectProperties shadowProps = new RectProperties();

    // shadow properties (back rectangle in the background)
    shadowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    shadowProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    shadowProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    shadowProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    // headline rectangle properties
    headlinebgProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headlinebgProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    headlinebgProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    headlinebgProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    // shadow rectangle
    lang.newRect(uL, lR, "headlinebg", null, headlinebgProps);
    Coordinates shadowUpperLeft = new Coordinates(uL.getX() + 10,
        uL.getY() + 10);
    Coordinates shadowLowerRight = new Coordinates(lR.getX() + 10,
        lR.getY() + 10);

    // headline rectangle
    lang.newRect(shadowUpperLeft, shadowLowerRight, "shadow", null,
        shadowProps);
    TextProperties headlineProps = new TextProperties();
    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.BOLD, 30));

    // headline text
    lang.newText(tUL, headline, "headline", null, headlineProps);

    SourceCodeProperties descProps = new SourceCodeProperties();
    descProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 12));
  }

  public void findNN(StringMatrix m) {

    int minIndex = 0;
    double minVal = Double.MAX_VALUE;

    for (int i = 1; i < m.getNrRows(); i++) {

      String example = m.getElement(i, 0).substring(0, 2);
      for (int j = 1; j < m.getNrCols() - 2; j++) {

        String rule = m.getElement(0, j).substring(0, 2);

        if (!example.equalsIgnoreCase(rule)) {
          if (Double.parseDouble(m.getElement(i, j)) < minVal) {
            minVal = Double.parseDouble(m.getElement(i, j));
            minIndex = j;
          }
        }
      }
      m.highlightCell(i, minIndex, null, null);
      m.put(i, m.getNrCols() - 2, m.getElement(0, minIndex), null, null);
      m.put(i, m.getNrCols() - 1, m.getElement(i, minIndex), null, null);
      m.setGridTextColor(i, m.getNrCols() - 2, Color.RED, null, null);
      m.setGridTextColor(i, m.getNrCols() - 1, Color.RED, null, null);

      minVal = Double.MAX_VALUE;
    }
  }

  public StringMatrix createTableNN(StringMatrix m) {

    String[][] table = new String[m.getNrRows()][4];
    table[0][0] = translator.translateMessage("toClassifyingExample");
    table[0][1] = translator.translateMessage("rule");
    table[0][2] = translator.translateMessage("distance");
    table[0][3] = translator.translateMessage("prediction");

    for (int i = 1; i < table.length; i++) {

      table[i][0] = m.getElement(i, 0);
      table[i][1] = m.getElement(i, m.getNrCols() - 2);
      table[i][2] = m.getElement(i, m.getNrCols() - 1);
      table[i][3] = (m.getElement(i, m.getNrCols() - 2).contains("Y")
          || m.getElement(i, m.getNrCols() - 2).contains("y")) ? "yes" : "no";
    }
    StringMatrix tableNN = lang.newStringMatrix(m.getUpperLeft(), table, "nn",
        null, matrixProps);
    setHeadLine(tableNN);
    lang.nextStep();

    for (int i = 1; i < table.length; i++) {
      if ((isPositive(table[i][0])
          && table[i][table[0].length - 1].equalsIgnoreCase("no"))
          || (!isPositive(table[i][0])
              && table[i][table[0].length - 1].equalsIgnoreCase("yes"))) {
        tableNN.highlightCellColumnRange(i, 0, table[0].length - 2, null, null);
        tableNN.highlightCell(i, table[0].length - 1, null, null);
      }
    }

    return tableNN;

  }

  public StringMatrix chooseUncoveredExample(StringMatrix m, String rule,
      int colIndex) {

    LinkedList<String[]> tableList = new LinkedList<String[]>();

    String[] row = new String[3];
    row[0] = "Rule";
    row[1] = "Example";
    row[2] = "Distance";

    tableList.add(row);

    String ruleSub = rule.substring(0, 2);

    for (int i = 1; i < m.getNrRows(); i++) {

      String mSub = m.getElement(i, 0).substring(0, 2);

      if (!ruleSub.equals(mSub)
          && ((isPositive(rule) && isPositive(m.getElement(i, 0)))
              || (!isPositive(rule) && !isPositive(m.getElement(i, 0))))) {

        row = new String[3];
        row[0] = rule;
        row[1] = m.getElement(i, 0);
        row[2] = m.getElement(i, colIndex);
        tableList.add(row);
      }
    }

    String[][] table = new String[tableList.size()][3];

    for (int i = 0; i < table.length; i++) {
      table[i] = tableList.get(i);
    }

    return lang.newStringMatrix(m.getUpperLeft(), table, "cue", null,
        matrixProps);

  }

  public boolean isPositive(String s) {
    if (s.contains("y") || s.contains("Y"))
      return true;
    else
      return false;
  }

  public int getNumOfNegatives(StringMatrix m) {
    int count = 0;

    for (int i = 1; i < m.getNrRows(); i++) {
      if (!isPositive(m.getElement(i, 0)))
        count++;
    }

    return count;
  }

  public int getNumOfPositives(StringMatrix m) {
    int count = 0;

    for (int i = 1; i < m.getNrRows(); i++) {
      if (isPositive(m.getElement(i, 0)))
        count++;
    }

    return count;
  }

  public StringMatrix generalizeTable(String[][] data, String[] a, String[] b) {

    String[][] table = new String[4][a[0].length()];

    table[0] = data[0];
    table[1] = a;
    table[2] = b;

    table[3] = generalize(a, b);

    return lang.newStringMatrix(
        new Offset(0, 30, "titleInterSteps", AnimalScript.DIRECTION_NW), table,
        "gT", null, matrixProps);

  }

  public String[] generalize(String[] a, String[] b) {

    String[] g = new String[a.length];

    String[] splitA = isPositive(a[0]) ? a[0].split("[Yy]")
        : a[0].split("[Nn]");
    String[] splitB = isPositive(b[0]) ? b[0].split("[Yy]")
        : b[0].split("[Nn]");

    g[0] = isPositive(a[0]) ? "Y" : "N";
    g[0] = g[0] + splitA[1] + splitB[1];
    for (int i = 1; i < a.length; i++) {

      if (a[i].equalsIgnoreCase(b[i]))
        g[i] = a[i];
      else
        g[i] = "?";
    }
    return g;
  }

  public String findNNSameClass(StringMatrix table, String rule) {

    double nn = Double.MAX_VALUE;
    String nnExample = "";
    int nnIndex = 0;

    for (int i = 1; i < table.getNrRows(); i++) {
      if (Double.parseDouble(table.getElement(i, 2)) < nn
          && Double.parseDouble(table.getElement(i, 2)) > 0.0) {
        nnExample = table.getElement(i, 1);
        nn = Double.parseDouble(table.getElement(i, 2));
        nnIndex = i;
      }
    }

    table.highlightCellColumnRange(nnIndex, 0, 1, null, null);
    table.highlightCell(nnIndex, 2, null, null);

    return nnExample;
  }

  public String[] getExampleByName(StringMatrix m, String name) {

    String[][] data = getData(m);
    String[] example = new String[m.getNrCols()];

    for (int i = 1; i < data.length; i++) {

      if (data[i][0].equalsIgnoreCase(name)) {
        example = data[i];
      }
    }

    return example;
  }

  public String[][] getData(StringMatrix s) {
    String[][] m = new String[s.getNrRows()][s.getNrCols()];

    for (int i = 0; i < s.getNrRows(); i++) {
      for (int j = 0; j < s.getNrCols(); j++) {

        m[i][j] = s.getElement(i, j);

      }
    }

    return m;
  }

  public StringMatrix calcDistanceToNewRule(StringMatrix dataset,
      StringMatrix prevTable, String[] newRule) {

    String[][] data = getData(dataset);
    String[] example;

    StringMatrix newDistanceTable = lang.newStringMatrix(
        prevTable.getUpperLeft(), getData(prevTable), "newDist", null,
        matrixProps);

    for (int i = 1; i < data.length; i++) {

      example = getExampleByName(dataset, data[i][0]);
      double res = round(calcVDMForExamples(data, example, newRule), 2);

      if (res < Double.parseDouble(prevTable.getElement(i, 2))) {
        newDistanceTable.put(i, 2, String.valueOf(res), null, null);
        newDistanceTable.put(i, 1, newRule[0], null, null);

        String c = isPositive(newRule[0]) ? "yes" : "no";
        newDistanceTable.put(i, 3, c, null, null);
        newDistanceTable.highlightCellColumnRange(i, 0,
            newDistanceTable.getNrCols() - 2, null, null);
        newDistanceTable.highlightCell(i, newDistanceTable.getNrCols() - 1,
            null, null);
      }
    }
    return newDistanceTable;
  }

  public int countCorrectClassified(StringMatrix table) {

    int correct = 0;

    for (int i = 1; i < table.getNrRows(); i++) {

      if ((isPositive(table.getElement(i, 0))
          && isPositive(table.getElement(i, 1)))
          || (!isPositive(table.getElement(i, 0))
              && !isPositive(table.getElement(i, 1)))) {
        correct++;
      }
    }

    return correct;
    // return round((double) correct / (table.getNrRows() - 1), 2);

  }

  public String[][] addNewRule(String[][] data, String oldRule,
      String[] newRule) {

    String[][] dataset = data;

    for (int i = 1; i < dataset.length; i++) {

      if (dataset[i][0].equalsIgnoreCase(oldRule)) {
        dataset[i] = newRule;
        break;
      }
    }

    return dataset;
  }

  public boolean ruleExists(String[][] data, String rule) {

    LinkedList<HashSet<String>> ruleSet = new LinkedList<HashSet<String>>();
    HashSet<String> currentSet = new HashSet<String>();

    for (int i = 1; i < data.length; i++) {

      String[] currentRule = data[i][0].split("");
      currentSet = new HashSet<String>();

      for (int j = 0; j < currentRule.length; j++) {

        currentSet.add(currentRule[0] + currentRule[j]);
      }

      ruleSet.add(currentSet);
    }

    HashSet<String> newRule = new HashSet<String>();
    String[] newRuleArr = rule.split("");
    for (int i = 0; i < newRuleArr.length; i++) {
      newRule.add(newRuleArr[0] + newRuleArr[i]);
    }

    return ruleSet.contains(newRule);

  }

  public String[][] removeExample(String[][] data, String example) {

    String[][] newDataset = new String[data.length - 1][data[0].length];
    int k = 0;

    for (int i = 0; i < data.length; i++) {
      if (!data[i][0].equalsIgnoreCase(example)) {
        newDataset[k] = data[i];
        k++;
      }
    }

    return newDataset;
  }

  public void showVDMDistances(StringMatrix dataset) {

    setExplanationText(translator.translateMessage("calcVDM2"));

    String[][] data = getData(dataset);

    String[] values;
    int[] distA;
    int[] distB;

    for (int i = 1; i < data[0].length - 1; i++) {

      values = getValuesForAttribute(data, i);

      for (int j = 0; j < values.length; j++) {
        distA = getClassDistribution(data, values[j], i);

        for (int k = j; k < values.length; k++) {
          distB = getClassDistribution(data, values[k], i);
          if (k != j) {

            String a = translator.translateMessage("calcDist") + "\n" + "      "
                + translator.translateMessage("attribute") + " = " + data[0][i]
                + " (" + values[j] + ", " + values[k] + ")" + "\n";
            setExplanationText(a);
            lang.nextStep();
            highlightWithValue(dataset, values[j], i);

            a = a + "          " + values[j] + ":\n";
            setExplanationText(a);
            lang.nextStep();

            highlightWithValue(dataset, values[j], i, "yes");
            a = a + "             " + distA[0] + "/" + (distA[0] + distA[1]) + " "
                + translator.translateMessage("examplesArePositive");
            setExplanationText(a);

            lang.nextStep();
            // unhighlightAll(dataset);
            highlightWithValue(dataset, values[j], i, "no");

            a = a + "             " + distA[1] + "/" + (distA[0] + distA[1]) + " "
                + translator.translateMessage("examplesAreNegative");
            setExplanationText(a);

            lang.nextStep();
            unhighlightAll(dataset);

            highlightWithValue(dataset, values[k], i);

            a = a + "          " + values[k] + ":\n";
            setExplanationText(a);
            lang.nextStep();
            highlightWithValue(dataset, values[k], i, "yes");
            a = a + "             " + distB[0] + "/" + (distB[0] + distB[1]) + " "
                + translator.translateMessage("examplesArePositive");
            setExplanationText(a);

            lang.nextStep();
            // unhighlightAll(dataset);

            highlightWithValue(dataset, values[k], i, "no");

            a = a + "             " + distB[1] + "/" + (distB[0] + distB[1]) + " "
                + translator.translateMessage("examplesAreNegative");
            setExplanationText(a);

            lang.nextStep();
            unhighlightAll(dataset);

            a = a + translator.translateMessage("calcDifference");
            a = a + "d(" + values[j] + ", " + values[k] + ") = ( |" + distA[0]
                + "/" + (distA[0] + distA[1]) + " - " + distB[0] + "/"
                + (distB[0] + distB[1]) + "| + |" + distA[1] + "/"
                + (distA[0] + distA[1]) + " - " + distB[1] + "/"
                + (distB[0] + distB[1]) + "| )^2 = "
                + round(Math.pow(calcVDMForAttribute(distA, distB), 2), 2);
            changeRowColorDefault(dataset);
            setExplanationText(a);

            distanceList.add("d(" + values[j] + ", " + values[k] + ") = "
                + round(Math.pow(calcVDMForAttribute(distA, distB), 2), 2)
                + "\n");
           
            vars.declare("string", "\"d("+ values[j] + ", " + values[k]+")\"", String.valueOf(round(Math.pow(calcVDMForAttribute(distA, distB), 2), 2)));
            lang.nextStep();
          }
        }
      }
    }

  }

  public void highlightWithValue(StringMatrix m, String val, int colIndex,
      String classification) {

    for (int i = 1; i < m.getNrRows(); i++) {

      if (m.getElement(i, colIndex).equalsIgnoreCase(val) && m
          .getElement(i, m.getNrCols() - 1).equalsIgnoreCase(classification)) {
        m.highlightCellColumnRange(i, 0, m.getNrCols() - 2, null, null);
        m.highlightCell(i, m.getNrCols() - 1, null, null);
        changeRowColor(m, i, classification);
      }
    }
  }

  public void highlightWithValue(StringMatrix m, String val, int colIndex) {

    for (int i = 1; i < m.getNrRows(); i++) {

      if (m.getElement(i, colIndex).equalsIgnoreCase(val)) {
        m.highlightCellColumnRange(i, 0, m.getNrCols() - 2, null, null);
        m.highlightCell(i, m.getNrCols() - 1, null, null);
      }
    }
  }

  public void changeRowColor(StringMatrix m, int row, String classification) {

    Color c = classification.equalsIgnoreCase("yes") ? Color.GREEN : Color.RED;

    for (int i = 0; i < m.getNrCols(); i++) {
      m.setGridHighlightFillColor(row, i, c, null, null);
    }
  }

  public void changeRowColorDefault(StringMatrix m) {

    for (int i = 0; i < m.getNrRows(); i++) {
      for (int j = 0; j < m.getNrCols(); j++) {
        m.setGridHighlightFillColor(i, j, Color.LIGHT_GRAY, null, null);
      }
    }
  }

  public String[] getValuesForAttribute(String[][] examples, int index) {

    HashSet<String> values = new HashSet<String>();

    for (int i = 1; i < examples.length; i++) {
      for (int j = 0; j < examples[0].length; j++) {
        if (j == index) {
          values.add(examples[i][j]);
        }
      }
    }

    String[] stringValues = new String[values.size()];
    stringValues = values.toArray(stringValues);

    return stringValues;
  }

  public void unhighlightAll(StringMatrix m) {

    for (int i = 0; i < m.getNrRows(); i++) {
      m.unhighlightCellColumnRange(i, 0, m.getNrCols() - 2, null, null);
      m.unhighlightCell(i, m.getNrCols() - 1, null, null);

      m.unhighlightElemColumnRange(i, 0, m.getNrCols() - 1, null, null);
      // m.unhighlightElem(i, m.getNrCols() - 1, null, null);
    }
  }

  public StringMatrix calcAccuracyWithRuleset(StringMatrix dataset,
      String[][] ruleset) {

    String[][] data = getData(dataset);

    String[][] distMatrix = createEmptyMatrix(dataset.getNrRows(),
        ruleset.length + 2);

    for (int i = 1; i < ruleset.length; i++) {
      distMatrix[0][i] = ruleset[i][0];
    }
    distMatrix[0][distMatrix[0].length - 2] = "NN";
    distMatrix[0][distMatrix[0].length - 1] = translator
        .translateMessage("distance");

    for (int i = 1; i < data.length; i++) {
      distMatrix[i][0] = data[i][0];
    }

    for (int i = 1; i < distMatrix.length; i++) {

      String example = data[i][0].substring(0, 2);

      for (int j = 1; j < distMatrix[0].length - 2; j++) {

        String rule = ruleset[j][0].substring(0, 2);

        if (!example.equalsIgnoreCase(rule)) {
          distMatrix[i][j] = String
              .valueOf(round(calcVDMForExamples(data, data[i], ruleset[j]), 2));
        } else {
          distMatrix[i][j] = " ";
        }
      }
    }

    StringMatrix dM = lang.newStringMatrix(
        new Offset(0, 30, "titleInterSteps", AnimalScript.DIRECTION_NW),
        distMatrix, "newruleset", null, matrixProps);

    dM.setGridTextColor(0, dM.getNrCols() - 2, Color.RED, null, null);
    dM.setGridTextColor(0, dM.getNrCols() - 1, Color.RED, null, null);

    return dM;

  }

  public void unhighlightAllSourceCodeLines(SourceCode s, int numOfLines) {
    for (int i = 0; i < numOfLines; i++) {
      s.unhighlight(i);
    }
  }

  public void setHeadLine(StringMatrix m) {
    for (int i = 0; i < m.getNrCols(); i++) {
      m.setGridFont(0, i, new Font("SansSerif", Font.BOLD, 12), null, null);
    }
  }

  public void setFirstColumn(StringMatrix m) {
    for (int i = 0; i < m.getNrRows(); i++) {
      m.setGridFont(i, 0, new Font("SansSerif", Font.BOLD, 12), null, null);
    }
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    titleProps = (TextProperties) props.getPropertiesByName("titleProps");
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.BOLD, 18));
    
    matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
    scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
    expProps = (SourceCodeProperties) props.getPropertiesByName("expProps");
    
    String[][] dataset = (String[][]) primitives.get("dataset");

    distanceList = new LinkedList<String>();

    createIntro("RISE - Rule Induction from a Set of Exemplars",
        translator.translateMessage("description"), new Coordinates(10, 10),
        new Coordinates(825, 60), new Coordinates(60, 15),
        new Coordinates(10, 100));

    lang.nextStep("intro"); 
    
    lang.hideAllPrimitives();
    // init properties for titles

    createIntroWithoutDesc("RISE - Rule Induction from a Set of Exemplars",
        translator.translateMessage("description"), new Coordinates(10, 10),
        new Coordinates(825, 60), new Coordinates(60, 15));

    MultipleChoiceQuestionModel quest1 = new MultipleChoiceQuestionModel(
        "q1");
    quest1.setPrompt(translator.translateMessage("q1"));
    quest1.addAnswer("0",
        translator.translateMessage("q1a1"), 0,
        translator.translateMessage("qFeedbackW"));
    quest1.addAnswer("1",
        translator.translateMessage("q1a2"), 0,
        translator.translateMessage("qFeedbackC"));
    quest1.addAnswer("2",
        translator.translateMessage("q1a3"), 0,
        translator.translateMessage("qFeedbackW"));
    lang.addMCQuestion(quest1);

    
    MultipleChoiceQuestionModel quest2 = new MultipleChoiceQuestionModel(
        "q2");
    quest2.setPrompt(translator.translateMessage("q2"));
    quest2.addAnswer("3",
        translator.translateMessage("q2a1"), 0,
        translator.translateMessage("qFeedbackW"));
    quest2.addAnswer("4",
        translator.translateMessage("q2a2"), 0,
        translator.translateMessage("qFeedbackC"));
    quest2.addAnswer("5",
        translator.translateMessage("q2a3"), 0,
        translator.translateMessage("qFeedbackW"));
    
    MultipleChoiceQuestionModel quest3 = new MultipleChoiceQuestionModel(
        "q3");
    quest3.setPrompt(translator.translateMessage("q2"));
    quest3.addAnswer("6",
        translator.translateMessage("q2a1"), 0,
        translator.translateMessage("qFeedbackW"));
    quest3.addAnswer("7",
        translator.translateMessage("q2a2"), 0,
        translator.translateMessage("qFeedbackW"));
    quest3.addAnswer("8",
        translator.translateMessage("q2a3"), 0,
        translator.translateMessage("qFeedbackC"));
    
    boolean q2 = true;
    boolean q3 = true;
    
    

    lang.newText(new Coordinates(30, 130),
        translator.translateMessage("titleRuleset"), "titleTrainingData", null,
        titleProps);

    datasetMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleTrainingData", AnimalScript.DIRECTION_SW),
        dataset, "dataset", null, matrixProps);

    setHeadLine(datasetMatrix);

    lang.newText(new Offset(100, -30, "dataset", AnimalScript.DIRECTION_NE),
        "Source Code", "scTitle", null, titleProps);

    sc = lang.newSourceCode(
        new Offset(0, 0, "scTitle", AnimalScript.DIRECTION_SW), "sc", null,
        scProps);
    sc.addCodeLine("1.) turn each example into a rule resulting in a theory T",
        null, 0, null);
    sc.addCodeLine("2.) Initiale Accuracy", null, 0, null);
    sc.addCodeLine("3.) repeat", null, 0, null);
    sc.addCodeLine("    for each rule R in T", null, 1, null);
    sc.addCodeLine("    i.) choose uncovered example x_min = arg min d(x,R)",
        null, 2, null);
    sc.addCodeLine("    ii.) R' = minimalGeneralisation(R, x_min)", null, 2,
        null);
    sc.addCodeLine(
        "    iii.) replace R with R' if this does not decrease the accuracy of T",
        null, 2, null);
    sc.addCodeLine(
        "    iv.) delete R' it is already part of T (duplicate rule)", null, 2,
        null);
    sc.addCodeLine("    update ruleset", null, 1, null);
    sc.addCodeLine("5.) until no further increase in accuracy", null, 0, null);

    lang.newText(new Offset(100, -30, "sc", AnimalScript.DIRECTION_NE),
        translator.translateMessage("titleInterSteps"), "titleInterSteps", null,
        titleProps);

    lang.newText(new Offset(0, 30, "sc", AnimalScript.DIRECTION_SW),
        translator.translateMessage("titleExp"), "titleExplanation", null,
        titleProps);

    setExplanationText(translator.translateMessage("task"));
    lang.nextStep();
    sc.highlight(0);
    setExplanationText(translator.translateMessage("convertAllRules"));

    lang.nextStep();

    sc.toggleHighlight(0, 1);
    setExplanationText(translator.translateMessage("calcVDM"));

    lang.nextStep("calculate VDM");
    String[][] ruleset = getData(datasetMatrix);
    vars = lang.newVariables();

    showVDMDistances(datasetMatrix);
    
    double previousAccuracy = 0.0;
    double currentAccuracy = 0.0;
    vars.declare("string", "Accuracy", String.valueOf(currentAccuracy));

    StringMatrix rulesetMatrix;

    String[][] previousRulesetMatrix;

    StringMatrix tableNN;

    datasetMatrix.hide();

    boolean firstRound = true;
        
    do {

      rulesetMatrix = lang.newStringMatrix(datasetMatrix.getUpperLeft(),
          ruleset, "ruleset", null, matrixProps);

      setHeadLine(rulesetMatrix);

      if(firstRound) {
        lang.nextStep();
      } else {
        lang.nextStep("update rule set");
      }
      
      sc.unhighlight(8);
      previousRulesetMatrix = getData(rulesetMatrix);

      distanceMatrix = calcAccuracyWithRuleset(datasetMatrix, ruleset);
      setHeadLine(distanceMatrix);
      setFirstColumn(distanceMatrix);

      setExplanationText(
          translator.translateMessage("calcDistanceForEachExample1"));

      lang.nextStep("calculate distance");

      if (firstRound) {
        showVDMCalc(rulesetMatrix, distanceMatrix, 1, 2);
        showVDMCalc(rulesetMatrix, distanceMatrix, 1, 3);
        firstRound = false;
      }

      setExplanationText(
          translator.translateMessage("calcDistanceForEachExample2"));

      findNN(distanceMatrix);
      lang.nextStep("calculate Accuracy");
      distanceMatrix.hide();

      setExplanationText(translator.translateMessage("calcAcc"));
      tableNN = createTableNN(distanceMatrix);
      setHeadLine(tableNN);
      // calc initial accuracy
      int correct = countCorrectClassified(tableNN);

      previousAccuracy = currentAccuracy;

      currentAccuracy = round((double) correct / (tableNN.getNrRows() - 1), 2);
      vars.set("Accuracy", String.valueOf(currentAccuracy));

      setExplanationText(translator.translateMessage("wrongClassifiedEx",
          String.valueOf((tableNN.getNrRows() - 1 - correct)),
          String.valueOf(correct), String.valueOf((tableNN.getNrRows() - 1)),
          String.valueOf(currentAccuracy), String.valueOf(previousAccuracy)));

      lang.nextStep();
      String[][] newRuleset = ruleset;

      /////////////////////////////////////////////////////////////////////////////////////////////////////

      for (int i = 1; i < ruleset.length; i++) {

        unhighlightAllSourceCodeLines(sc, 10);

        tableNN.hide();
        String currentRule = ruleset[i][0];

        unhighlightAll(rulesetMatrix);

        sc.toggleHighlight(1, 2);
        sc.highlight(3);
        sc.highlight(4);
        setExplanationText(translator.translateMessage("iterate"));

        rulesetMatrix.highlightCellColumnRange(i, 0,
            rulesetMatrix.getNrCols() - 2, null, null);
        rulesetMatrix.highlightCell(i, rulesetMatrix.getNrCols() - 1, null,
            null);

        StringMatrix uncoveredExamples = chooseUncoveredExample(distanceMatrix,
            currentRule, i);
        setHeadLine(uncoveredExamples);

        String nn = findNNSameClass(uncoveredExamples, currentRule);

        lang.nextStep();
        uncoveredExamples.hide();

        sc.toggleHighlight(4, 5);
        StringMatrix generalizedTable = generalizeTable(dataset,
            getExampleByName(rulesetMatrix, currentRule),
            getExampleByName(datasetMatrix, nn));

        setHeadLine(generalizedTable);

        setExplanationText(translator.translateMessage("createNewRule"));

        String[] newRule = getData(generalizedTable)[3];

        if (!ruleExists(newRuleset, newRule[0])) {

          lang.nextStep();
          generalizedTable.hide();
          StringMatrix newDistances = calcDistanceToNewRule(datasetMatrix,
              tableNN, newRule);
          setHeadLine(newDistances);
 
          setExplanationText(
              translator.translateMessage("newMinimalDistance", newRule[0]));

          double newCorrect = countCorrectClassified(newDistances);
          double newAccuracy = round(
              (double) newCorrect / (datasetMatrix.getNrRows() - 1), 2);

          if (newAccuracy >= currentAccuracy) {

            if(q2) {
              lang.addMCQuestion(quest2);
              q2 = false;
            }
            
            lang.nextStep();
            sc.toggleHighlight(5, 6);
            setExplanationText(translator.translateMessage("addNewRule",
                newRule[0],

                String.valueOf((int) newCorrect),
                String.valueOf((int) newCorrect),
                String.valueOf((tableNN.getNrRows() - 1)),
                String.valueOf(newAccuracy), String.valueOf(currentAccuracy)));
            newRuleset = addNewRule(newRuleset, currentRule, newRule);

          } else {
            lang.nextStep();

            setExplanationText(translator.translateMessage("dontAddNewRule",
                newRule[0], String.valueOf((int) newCorrect),
                String.valueOf(newAccuracy), String.valueOf(currentAccuracy)));

          } 
          lang.nextStep();
          newDistances.hide();
        } else {
          
          if(q3) {
            lang.addMCQuestion(quest3);
            q3 = false;
          }
          
          lang.nextStep();

          setExplanationText(
              translator.translateMessage("removeRule", currentRule));
          sc.toggleHighlight(5, 7);
          lang.nextStep();
          generalizedTable.hide();

          newRuleset = removeExample(newRuleset, currentRule);
        }
      }

      setExplanationText(translator.translateMessage("updateRuleset"));
      sc.unhighlight(3);
      sc.unhighlight(4);
      sc.unhighlight(5);
      sc.unhighlight(6);
      sc.unhighlight(7);
      sc.highlight(8);
      ruleset = newRuleset;
      rulesetMatrix.hide();

    } while (previousAccuracy < currentAccuracy);

    rulesetMatrix = lang.newStringMatrix(rulesetMatrix.getUpperLeft(), ruleset,
        "ruleset", null, matrixProps);
    setHeadLine(rulesetMatrix);
    lang.nextStep("update ruleset");

    unhighlightAllSourceCodeLines(sc, 10);
    sc.highlight(2);
    sc.highlight(3);

    distanceMatrix = calcAccuracyWithRuleset(datasetMatrix, ruleset);
    setHeadLine(distanceMatrix);
    setFirstColumn(distanceMatrix);
    findNN(distanceMatrix);

    setExplanationText(translator.translateMessage("calcAcc"));

    lang.nextStep(); 

    distanceMatrix.hide();
    tableNN = createTableNN(distanceMatrix);
    setHeadLine(tableNN);

    // calc initial accuracy
    int correct = countCorrectClassified(tableNN);

    currentAccuracy = round((double) correct / (tableNN.getNrRows() - 1), 2);

    sc.unhighlight(2);
    sc.unhighlight(3);
    sc.highlight(9);
    setExplanationText(translator.translateMessage("terminate",
        String.valueOf((tableNN.getNrRows() - 1 - correct)),
        String.valueOf(correct), String.valueOf((tableNN.getNrRows() - 1)),
        String.valueOf(currentAccuracy), String.valueOf(previousAccuracy)));
    lang.nextStep("Result");

    rulesetMatrix.hide();
    tableNN.hide();

    lang.hideAllPrimitives();

    createIntroWithoutDesc("RISE - Rule Induction from a Set of Exemplars",
        translator.translateMessage("description"), new Coordinates(10, 10),
        new Coordinates(825, 60), new Coordinates(60, 15));

    lang.newText(new Coordinates(30, 130),
        translator.translateMessage("titleExamples"), "finalExamples", null,
        titleProps);

    StringMatrix examples = lang.newStringMatrix(
        new Offset(0, 10, "finalExamples", AnimalScript.DIRECTION_SW), dataset,
        "examples", null, matrixProps);

    setHeadLine(examples);

    lang.newText(new Offset(50, -30, "examples", AnimalScript.DIRECTION_NE),
        translator.translateMessage("titleRuleset"), "finalRuleset", null,
        titleProps);

    StringMatrix rules = lang.newStringMatrix(
        new Offset(0, 10, "finalRuleset", AnimalScript.DIRECTION_SW),
        previousRulesetMatrix, "fruleset", null, matrixProps);

    setHeadLine(rules);

    lang.newText(new Offset(50, -30, "fruleset", AnimalScript.DIRECTION_NE),
        translator.translateMessage("result"), "fexp", null, titleProps);

    SourceCode finalText = lang.newSourceCode(
        new Offset(0, 10, "fexp", AnimalScript.DIRECTION_SW), "finalexp", null,
        expProps);
    finalText.addMultilineCode(translator.translateMessage("finalRuleset",
        String.valueOf(previousAccuracy)), null, null);

    lang.finalizeGeneration();
    return lang.toString();

  }

  @Override
  public String getAlgorithmName() {
    return "RISE - Rule Indiuction from a Set of Exemplars";
  }

  @Override
  public String getAnimationAuthor() {
    return "Dominik Rödszus, Stanislaw Kin";
  }

  @Override
  public String getCodeExample() {

    return "1.) turn each example into a rule resulting in a theory T" + "\n"
        + "2.) Initiale Accuracy" + "\n" + "3.) repeat" + "\n"
        + "       for each rule R in T" + "\n"
        + "          i.) choose uncovered example x_min = arg min d(x,R)" + "\n"
        + "          ii.) R' = minimalGeneralisation(R, x_min)" + "\n"
        + "          iii.) replace R with R' if this does not decrease the accuracy of T"
        + "\n"
        + "          iv.) delete R' it is already part of T (duplicate rule)"
        + "\n" + "    update ruleset" + "\n"
        + "5.) until no further increase in accuracy" + "\n";
  }

  @Override
  public Locale getContentLocale() {
    return translator.getCurrentLocale();
  }

  @Override
  public String getDescription() {
    return translator.translateMessage("description");
  }

  @Override
  public String getFileExtension() {
    return "asu";
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public String getName() {
    return "RISE - Rule Indiuction from a Set of Exemplars";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
        getAlgorithmName(), getAnimationAuthor(), 1000, 1000);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {

    String[][] trainingDataValidate = (String[][]) primitives.get("dataset");

    for (int i = 1; i < trainingDataValidate.length; i++) {

      if (!trainingDataValidate[i][trainingDataValidate[0].length - 1]
          .equalsIgnoreCase("no")
          && (!trainingDataValidate[i][trainingDataValidate[0].length - 1]
              .equalsIgnoreCase("yes"))) {
        throw new IllegalArgumentException(
            "The last attribute classifies the training example. Please specifiy the class of each example by adding 'yes' or 'no' to the class attribute (the last element of a training example).");
      }

      for (int j = 0; j < trainingDataValidate[0].length; j++) {
        if (isNumericAttribute(trainingDataValidate[i][j])) {
          throw new IllegalArgumentException(
              "Only nominal attributes are allowed.");
        }
      }
    }

    return true;
  }

  public boolean isNumericAttribute(String s) {
    return s != null && s.matches("[-+]?\\d*\\.?\\d+");
  }

}
