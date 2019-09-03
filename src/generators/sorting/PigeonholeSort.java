/*
 * PigeonholeSort.java
 * Tomasz Gasiorowski, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.grid.Grid;
import generators.maths.grid.GridProperty;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;

public class PigeonholeSort implements Generator {
  private Language             lang;
  private TextProperties       textProps;
  private ArrayProperties      arrayProps;
  private SourceCodeProperties sourceCodeProps;
  private int[]                intArray;
  private int                  inputCount;
  private int                  matrixCount;
  private Text                 matrixText;
  private Text                 countText;

  private void updateCount() {
    countText.setText("Input array read/write: " + inputCount, null, null);
  }

  private void updateMatrix() {
    matrixText.setText("Matrix read/write: " + matrixCount, null, null);
  }

  public int[] pigeonsort(ArrayProperties ap, SourceCodeProperties scp,
      TextProperties textp, int[] inputArray) {
    int[] input = inputArray;
    TextProperties tp = textp;
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 20));
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.green);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    Text pigeonholesorttext = lang.newText(new Coordinates(10, 30),
        "Pigeonhole sort", "PIGEONHOLESORT", null, tp);
    pigeonholesorttext.hide();
    lang.newRect(new Offset(-5, -5, pigeonholesorttext, "NW"),
        new Offset(5, 5, pigeonholesorttext, "SE"), "RECT", null, rp);
    pigeonholesorttext.show();
    Text desc = lang.newText(new Coordinates(10, 120), "Description: ", "desc",
        null, tp);
    Text desc1 = lang.newText(new Coordinates(10, 160),
        "Pigeonhole sort is a sorting algorithm which sorts elements through pigeonholing, ie. placing elements into specific categories.",
        "desc1", null, tp);
    Text desc2 = lang.newText(new Coordinates(10, 180),
        "The categories in Pigeonhole sort are the integer values of the input array.",
        "desc2", null, tp);
    Text desc3 = lang.newText(new Coordinates(10, 200),
        "The algorithm works as follows:", "desc3", null, tp);
    Text desc4 = lang.newText(new Coordinates(10, 240),
        "1. Find the smallest and biggest element of the input list.", "desc4",
        null, tp);
    Text desc5 = lang.newText(new Coordinates(10, 260),
        "    Max - min + 1 is the range of the pigeonhole values.", "desc5",
        null, tp);
    Text desc6 = lang.newText(new Coordinates(10, 280),
        "    Set up a matrix with a height equal to the range, where each row is for storing elements of the same value.",
        "desc6", null, tp);
    Text desc7 = lang.newText(new Coordinates(10, 300),
        "2. Iterate over the input list, putting each element into the corresponding matrix row.",
        "desc7", null, tp);
    Text desc8 = lang.newText(new Coordinates(10, 320),
        "3. Iterate over the matrix in order and place each element back into the input array.",
        "desc8", null, tp);
    Text desc9 = lang.newText(new Coordinates(10, 360),
        "The complexity of pigeonhole sort is O(N + n), where N is the range of values and n is the input size.",
        "desc9", null, tp);
    lang.nextStep("Description");

    ArrayProperties arrayProps = ap;

    desc.hide();
    desc1.hide();
    desc2.hide();
    desc3.hide();
    desc4.hide();
    desc5.hide();
    desc6.hide();
    desc7.hide();
    desc8.hide();
    desc9.hide();
    Text inputText = lang.newText(new Coordinates(650, 50), "Input array",
        "inputText", null, tp);
    IntArray INPUT = lang.newIntArray(new Coordinates(600, 100), input,
        "intArrayInput", null, arrayProps);
    lang.nextStep();

    SourceCodeProperties scProps = sourceCodeProps;
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("Monospaced", Font.PLAIN, 16));
    SourceCode sc = lang.newSourceCode(new Coordinates(10, 120), "sourceCode",
        null, scProps);
    sc.addCodeLine("public int[] pigeonsort(int[] input) {", "l1", 0, null);
    sc.addCodeLine("int min = searchForSmallestElement();", "l2", 2, null);
    sc.addCodeLine("int max = searchForSBiggestElement();", "l3", 2, null);
    sc.addCodeLine("int pigeonholes[][] = setupPigeonholes();", "l4", 2, null);
    sc.addCodeLine(
        "List<Integer> indexes = new ArrayList<Integer>(max - min + 1);", "l5",
        2, null);
    sc.addCodeLine("for (int i = 0; i < input.length; i++) {", "l6", 2, null);
    sc.addCodeLine("// place the value into the matching pigeonhole", "l7", 4,
        null);
    sc.addCodeLine(
        "// After that, update the next empty array spot for this pigeonhole in the index list",
        "l8", 4, null);
    sc.addCodeLine(
        "pigeonholes[input[i] - min][indexes.get(input[i] - min)] = input[i];",
        "l9", 4, null);
    sc.addCodeLine(
        "indexes.set(input[i] - min, indexes.get(input[i] - min) + 1);", "l10",
        4, null);
    sc.addCodeLine("}", "l11", 2, null);
    sc.addCodeLine(
        "input = aggregatePigeonholes(); // iterate over the matrix, put integers back into array",
        "l12", 2, null);
    sc.addCodeLine("return input;", "l13", 2, null);
    sc.addCodeLine("}", "l14", 0, null);
    countText = lang.newText(new Coordinates(10, 450),
        "Input array read/write: 0", "ARRAYOP", null, tp);
    lang.nextStep();

    inputCount = 0;
    matrixCount = 0;
    int maxCount = 0;
    int minCount = 0;
    int min = input[0];
    int max = input[0];
    for (int i = 0; i < input.length; i++) {
      if (input[i] < min) {
        min = input[i];
      }
      inputCount++;
      minCount++;
      if (input[i] > max) {
        max = input[i];
      }
      maxCount++;
    }
    // inputCount = inputCount + input.length;
    int smallestBiggestCount = inputCount;
    int highlighttemp = -1;
    for (int i = 0; i < input.length; i++) {
      if (input[i] == min) {
        INPUT.highlightCell(i, null, null);
        highlighttemp = i;
        break;
      }
    }
    Text minText = lang.newText(new Offset(300, 0, inputText, "NE"),
        "Smallest element: " + Integer.toString(min), "minElement", null, tp);
    sc.highlight(1);
    updateCount();
    lang.nextStep();
    sc.unhighlight(1);
    INPUT.unhighlightCell(highlighttemp, null, null);
    Text maxText = lang.newText(new Offset(50, 0, minText, "NE"),
        "Biggest element: " + Integer.toString(max), "maxElement", null, tp);
    for (int i = 0; i < input.length; i++) {
      if (input[i] == max) {
        INPUT.highlightCell(i, null, null);
        highlighttemp = i;
        break;
      }
    }
    sc.highlight(2);
    inputCount = inputCount + maxCount;
    updateCount();
    FillInBlanksQuestionModel fib = new FillInBlanksQuestionModel(
        "Pigeonholes");
    fib.setPrompt("How many rows will the pigeonhole matrix have?");
    fib.addAnswer(Integer.toString(max - min + 1), 1,
        "max - min + 1 = " + (max - min + 1));
    lang.addFIBQuestion(fib);
    lang.nextStep();
    sc.unhighlight(2);
    INPUT.unhighlightCell(highlighttemp, null, null);
    Text setupHoles = lang.newText(new Offset(0, 150, minText, "SW"),
        "Set up the categories", "SETUP", null, tp);
    int[][] temp = new int[max - min + 1][input.length];
    for (int i = 0; i < max - min + 1; i++) {
      for (int j = 0; j < input.length; j++) {
        temp[i][j] = Integer.MIN_VALUE;
      }
    }
    inputCount++;
    GridProperty gp = new GridProperty();
    gp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    gp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    gp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    gp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    gp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    gp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    gp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "TABLE");
    Grid g = new Grid(new Coordinates(950, 300), input.length + 1,
        max - min + 1, 25, lang, gp);
    matrixCount++;
    for (int i = 0, x = min; i < temp.length; i++) {
      g.setLabel(0, i, Integer.toString(x));
      g.highlightCell(0, i, Color.RED, 0);
      x++;
    }
    matrixText = lang.newText(new Coordinates(10, 500), "Matrix read/write: 0",
        "MATRIXOP", null, tp);
    sc.highlight(3);
    updateMatrix();
    updateCount();
    int inputCountb4Loop = inputCount;
    lang.nextStep();
    sc.unhighlight(3);
    sc.highlight(4);
    lang.nextStep();
    sc.unhighlight(4);
    ArrayList<Integer> indexes = new ArrayList<Integer>();
    for (int i = 0; i < max - min + 1; i++) {
      indexes.add(0);
    }
    sc.highlight(5);
    INPUT.highlightCell(0, null, null);
    setupHoles.setText("Insert elements", null, null);
    updateCount();
    lang.nextStep("1. Iteration");
    for (int i = 0; i < input.length; i++) {
      sc.unhighlight(5);
      sc.highlight(8);
      g.setLabel(indexes.get(input[i] - min) + 1, input[i] - min,
          Integer.toString(input[i]));
      g.highlightCell(indexes.get(input[i] - min) + 1, input[i] - min,
          (Color) arrayProps
              .get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY),
          0);
      inputCount = inputCount + 3;
      matrixCount++;
      updateCount();
      updateMatrix();
      lang.nextStep();
      temp[input[i] - min][indexes.get(input[i] - min)] = input[i];
      sc.unhighlight(8);
      sc.highlight(9);
      inputCount = inputCount + 2;
      updateCount();
      lang.nextStep();
      indexes.set(input[i] - min, indexes.get(input[i] - min) + 1);
      sc.unhighlight(9);
      sc.highlight(5);
      INPUT.unhighlightCell(0, null, null);
      INPUT.unhighlightCell(i, null, null);
      INPUT.highlightCell(i + 1, null, null);
      updateCount();
      lang.nextStep(i + 2 + "." + "Iteration");
    }
    int inputCountLoop = inputCount - inputCountb4Loop;
    sc.unhighlight(5);
    List<Integer> l = new ArrayList<Integer>(5);
    int[] result = new int[input.length];
    Text resultText = lang.newText(
        new Coordinates(640, 350 + 25 * (max - min + 1)), "Sorted array",
        "resultText", null, tp);
    IntArray resultArray = lang.newIntArray(
        new Offset(-40, 50, resultText, "SW"), result, "RESULTARRAY", null,
        arrayProps);
    sc.highlight(11);
    for (int i = 1; i < temp[0].length; i++) {
      g.unhighlightColumn(i, 0);
    }
    int k = 0;
    for (int i = 0; i < temp.length; i++) {
      matrixCount++;
      for (int j = 0; j < temp[0].length; j++) {
        if (temp[i][j] != -2147483648) {
          result[k] = temp[i][j];
          if (j != 0) {
            matrixCount++;
          }

          resultArray.put(k, result[k], null, null);
          resultArray.highlightCell(k, null, null);
          k++;
        }
      }
    }
    inputCount = inputCount + inputArray.length;
    updateCount();
    updateMatrix();
    MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel(
        "complexity");
    mc.setPrompt(
        "What is the time complexity of pigeonhole sort? N is the range and n is the input size.");
    mc.addAnswer("O(n)", 0, "wrong");
    mc.addAnswer("O(n*N)", 0, "wrong");
    mc.addAnswer("O(N + n)", 1, "correct!");
    mc.addAnswer("O(log(n))", 0, "wrong");
    lang.addMCQuestion(mc);
    lang.nextStep("Result");
    sc.unhighlight(11);
    sc.highlight(12);
    double c0 = input.length;
    double c1 = inputCountb4Loop;
    int c2 = inputCountLoop;
    double c3 = matrixCount - input.length - 1;
    double c4 = max - min + 1;
    double count1 = c1 / c0;
    double count2 = c2 / c0;
    double count3 = c3 / c4;
    countText
        .setText("Input array operations: Find smallest + biggest elements = "
            + (inputCountb4Loop - 1) + " => O(2n)", null, null);
    matrixText.setText("                                  Loop operations: "
        + inputCountLoop + " => O(" + count2 + "n)", null, null);
    Text thirdText = lang.newText(new Coordinates(10, 550),
        "                                  Write result: " + input.length
            + " => O(n)",
        "THIRDTEXT", null, tp);

    lang.nextStep();
    thirdText.hide();
    countText.setText("Matrix operations:      Create + write into matrix: "
        + (input.length + 1) + " => O(n) + O(1)", null, null);
    matrixText.setText(
        "                                Write back to input array: "
            + (matrixCount - input.length - 1) + " => O(" + count3 + "N)",
        null, null);
    lang.nextStep();
    countText.setText(
        "The complexity of pigeonhole sort is O(N + n), where N is the range of values and n is the input size.",
        null, null);
    matrixText.setText(
        "This is because we first have to iterate over the whole input array to find out the range, to copy",
        null, null);
    thirdText.setText(
        "the integers to the pigeonhole matrix and vice versa. Copying the integers back into the array requires",
        null, null);
    thirdText.show();
    Text fourthText = lang.newText(new Coordinates(10, 600),
        "iterating over all pigeonholes, which is a bottleneck for runtime if we have a large range but many empty pigeonholes. ",
        "FOURTH", null, tp);
    Text fifthText = lang.newText(new Coordinates(10, 650),
        "Pigeonhole sort achieves its best performance when n and N are roughly the same.",
        "FIFTH", null, tp);
    resultText.hide();
    resultArray.hide();
    return result;

  }

  public void init() {
    lang = new AnimalScript("Pigeonhole sort", "Tomasz Gasiorowski", 800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    textProps = (TextProperties) props.getPropertiesByName("textProps");
    arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProps");
    intArray = (int[]) primitives.get("intArray");
    pigeonsort(arrayProps, sourceCodeProps, textProps, intArray);
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Pigeonhole sort";
  }

  public String getAlgorithmName() {
    return "Pigeonhole sort";
  }

  public String getAnimationAuthor() {
    return "Tomasz Gasiorowski";
  }

  public String getDescription() {
    return "Pigeonhole sort is a sorting algorithm which sorts elements through pigeonholing, ie. placing elements into specific categories. "
        + "\n"
        + "The algorithm first calculates the range of values of the input elements. Then it builds a matrix with a height equal to the range,"
        + "\n" + "where each row is for storing elements of the same value. "
        + "\n"
        + "After that, it iterates over the input array and places each element into the corresponding matrix row. "
        + "\n"
        + "Finally, it iterates over the matrix in order and places each element back into the input array."
        + "\n"
        + "The complexity of pigeonhole sort is O(N + n), where N is the range of values and n is the input size.";
  }

  public String getCodeExample() {
    return "public int[] pigeonsort(int[] input) {" + "\n"
        + "    int min = searchForSmallestElement();" + "\n"
        + "    int max = searchForSBiggestElement();" + "\n"
        + "    int pigeonholes[][] = setupPigeonholes();" + "\n"
        + "    List<Integer> indexes = new ArrayList<Integer>(max - min + 1);"
        + "\n" + "    for (int i = 0; i < input.length; i++) {" + "\n"
        + "        // place the value into the matching pigeonhole" + "\n"
        + "        // After that, update the next empty array spot for this pigeonhole in the index list"
        + "\n"
        + "        pigeonholes[input[i] - min][indexes.get(input[i] - min)] = input[i];"
        + "\n"
        + "        indexes.set(input[i] - min, indexes.get(input[i] - min) + 1);"
        + "\n" + "    }" + "\n"
        // +" int[] result = new int[input.length];"
        // +"\n"
        + "    input = aggregatePigeonholes();" + "\n" + "    return input;"
        + "\n" + "}";
  }

  public String getFileExtension() {
    return "asu";
  }

  public Locale getContentLocale() {
    return Locale.ENGLISH;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}