package generators.searching.helpers;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class KadaneScriptWriter {
  private Language             lang;
  private SourceCodeProperties sourceCode;
  private ArrayProperties      arrayProps;
  private TextProperties       comments;
  private ArrayMarker          iMarker;
  private Text                 statusMessage;
  private Text                 currentGlobalSum;
  private Text                 currentMaxSumOfCurrentSubstring;
  private Text                 currentPositiveMaxSumUntilPos;
  private Text                 tittle;
  private int[]                arrayData;
  private IntArray             array;
  private SourceCode           sc;
  private List<Integer>        highlightedCells;
  private int                  startIndex;
  private int                  endIndex;
  private int                  sum;

  public KadaneScriptWriter(Language lang, AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    this.lang = lang;
    this.lang.setStepMode(true);
    arrayData = (int[]) primitives.get("array");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
    comments = (TextProperties) props.getPropertiesByName("comments");
    highlightedCells = new ArrayList<Integer>();
    placeTittle();
  }

  public void placeTittle() {
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.BOLD, 24));
    tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    tittle = lang.newText(new Coordinates(20, 30), "Kadane's Algorithm",
        "header", null, tp);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps
        .set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(204, 255, 0));
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    lang.newRect(new Offset(-5, -5, tittle, "NW"), new Offset(5, 5, tittle,
        "SE"), "tittleRect", null, rectProps);
  }

  public void writeInitialization() {
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    SourceCode description = lang.newSourceCode(
        new Offset(0, 100, tittle, "SW"), "description", null, scProps);
    description
        .addCodeLine(
            "Kadane's Algorithm calculates the biggest sum of a subarray of the given array.",
            null, 1, null); // 0
    description.addCodeLine("For more information visit:", null, 0, null); // 5
    description.addCodeLine("http://en.wikipedia.org/wiki/Kadane's_algorithm",
        null, 0, null); // 5

    lang.nextStep("Initialization");
    array = lang.newIntArray(new Offset(0, 70, tittle, "SW"), arrayData,
        "array", null, arrayProps);
    description.hide();

    lang.nextStep();
    sc = lang.newSourceCode(new Offset(0, 70, array, "SW"), "source", null,
        sourceCode);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    sc.addCodeLine("public int getMaxSum(int[] array) {", null, 0, null); // 0
    sc.addCodeLine("int current_max_sum = 0;", null, 1, null); // 1
    sc.addCodeLine("int max_positive_sum_until_position = 0;", null, 1, null); // 2
    sc.addCodeLine("if (max(array) <= 0)", null, 1, null); // 3
    sc.addCodeLine("current_max_sum=max(array);", null, 2, null); // 4
    sc.addCodeLine("else{", null, 1, null); // 5
    sc.addCodeLine("for (int i : array) {", null, 2, null); // 6
    sc.addCodeLine("if (max_positive_sum_until_position + i > 0) ", null, 3,
        null); // 7
    sc.addCodeLine(
        "max_positive_sum_until_position = max_positive_sum_until_position + i;",
        null, 4, null); // 8
    sc.addCodeLine("else", null, 3, null); // 9
    sc.addCodeLine("max_positive_sum_until_position = 0;", null, 4, null); // 10
    sc.addCodeLine("if (current_max_sum < max_positive_sum_until_position)",
        null, 3, null); // 11
    sc.addCodeLine("current_max_sum = max_positive_sum_until_position;", null,
        4, null); // 12
    sc.addCodeLine("}", null, 2, null); // 13
    sc.addCodeLine("}", null, 1, null); // 14
    sc.addCodeLine("return current_max_sum;", null, 1, null); // 15
    sc.addCodeLine("}", null, 0, null); // 16

    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.ITALIC, 15));
    Text statusMessages = lang.newText(new Offset(20, 20, sc, "NE"),
        "Status updates:", "statusMessages", null, tp);
    statusMessage = lang.newText(new Offset(0, 20, statusMessages, "SW"), "",
        "update", null, comments);

    tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(120, 120, 120));
    currentGlobalSum = lang.newText(new Offset(0, 20, array, "S"),
        "Global Sum: 0", "currentGlobalSum", null, tp);
    currentMaxSumOfCurrentSubstring = lang.newText(
        new Offset(0, 35, array, "S"), "Current max sum of subarray: 0",
        "currentMaxSum", null, tp);
    currentPositiveMaxSumUntilPos = lang.newText(new Offset(0, 50, array, "S"),
        "Current positive sum until the current position: 0",
        "currentPositiveMaxSumUntilPos", null, tp);
    lang.nextStep();

  }

  public void writeInitialSteps() {

    sc.highlight(1);
    statusMessage.setText("Initialize required variables.", null, null);
    lang.nextStep();

    sc.toggleHighlight(1, 2);
    lang.nextStep();

    sc.toggleHighlight(2, 3);
    statusMessage.setText("", null, null);
    lang.nextStep();
  }

  public void writeAllNegative(int position) {
    startIndex = position;
    endIndex = position;
    sum = arrayData[position];
    sc.toggleHighlight(3, 4);
    array.highlightCell(position, null, null);
    statusMessage
        .setText(
            "All elements are equal to or less than 0, therefore the sum is equal to the biggest element in the array.",
            null, null);
    lang.nextStep();
    currentGlobalSum.setText(
        "Global Sum: " + getTotalSum(arrayData.length - 1), null, null);
    sc.toggleHighlight(4, 15);
    addFinalComment();
    lang.nextStep();

  }

  private int getTotalSum(int index) {
    int result = 0;
    for (int i = 0; i <= index; i++) {
      result += arrayData[i];
    }
    return result;
  }

  public void writeChangeMaxPositiveSumUntilPosition(int i,
      int current_max_sum, int max_positive_sum_until_position) {
    if (i == 0) {
      currentGlobalSum.setText("Global Sum: " + arrayData[i], null, null);
      ArrayMarkerProperties amp = new ArrayMarkerProperties();
      amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
      iMarker = lang.newArrayMarker(array, 0, "i", null, amp);
      sc.toggleHighlight(3, 6);
    } else {
      iMarker.increment(null, null);
      currentGlobalSum.setText("Global Sum: " + getTotalSum(i), null, null);
      sc.unhighlight(7);
      sc.unhighlight(10);
      sc.unhighlight(12);
      sc.unhighlight(11);
      sc.highlight(6);
    }

    lang.nextStep();
    sc.toggleHighlight(6, 7);
    lang.nextStep();
    sc.toggleHighlight(7, 8);
    currentPositiveMaxSumUntilPos.setText(
        "Current positive sum until the current position: "
            + max_positive_sum_until_position, null, null);
    statusMessage.setText("Found another potential element.", null, null);
    sum = current_max_sum;
    lang.nextStep();
    statusMessage.setText("", null, null);
    sc.unhighlight(8);
    sc.highlight(11);
    lang.nextStep();
  }

  public void writeResetMaxPositiveSumUntilPosition(int i, int current_max_sum,
      int max_positive_sum_until_position) {
    if (i == 0) {
      currentGlobalSum.setText("Global Sum: " + arrayData[i], null, null);
      sc.toggleHighlight(3, 6);
      ArrayMarkerProperties amp = new ArrayMarkerProperties();
      amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
      iMarker = lang.newArrayMarker(array, 0, "i", null, amp);
    } else {
      iMarker.increment(null, null);
      currentGlobalSum.setText("Global Sum: " + getTotalSum(i), null, null);
      sc.unhighlight(7);
      sc.unhighlight(10);
      sc.unhighlight(12);
      sc.unhighlight(11);
      sc.highlight(6);
    }

    lang.nextStep();
    sc.toggleHighlight(6, 7);
    lang.nextStep();
    sc.toggleHighlight(7, 10);
    statusMessage.setText("Current positive sum until position is below 0.",
        null, null);
    currentPositiveMaxSumUntilPos.setText(
        "Current positive sum until the current position: "
            + max_positive_sum_until_position, null, null);
    sum = current_max_sum;
    lang.nextStep();
    statusMessage.setText("", null, null);
    sc.unhighlight(10);
    sc.highlight(11);
    lang.nextStep();
  }

  public void writeStartNewSubArray(int startIndex, int current_max_sum,
      int max_positive_sum_until_position) {
    for (int i : highlightedCells) {
      array.unhighlightCell(i, null, null);
    }
    currentPositiveMaxSumUntilPos.setText(
        "Current positive sum until the current position: "
            + max_positive_sum_until_position, null, null);
    currentMaxSumOfCurrentSubstring.setText("Current max sum of subarray: "
        + current_max_sum, null, null);

  }

  public void writeIncrementMaxSum(int start, int end, int current_max_sum,
      int max_positive_sum_until_position) {
    startIndex = start;
    endIndex = end;
    statusMessage.setText("Add the element to the current subarray sum.", null,
        null);
    currentPositiveMaxSumUntilPos.setText(
        "Current positive sum until the current position: "
            + max_positive_sum_until_position, null, null);
    currentMaxSumOfCurrentSubstring.setText("Current max sum of subarray: "
        + current_max_sum, null, null);
    sc.toggleHighlight(11, 12);
    array.highlightCell(start, end, null, null);
    for (int i = 0; i < arrayData.length; i++) {
      if (i <= end && i >= start)
        highlightedCells.add(i);
    }

    lang.nextStep();
    statusMessage.setText("", null, null);
  }

  public void addFinalComment() {
    TextProperties finalCommentProps = new TextProperties();
    finalCommentProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    lang.newText(new Offset(0, 20, sc, "SW"),
        "Done - the maximum subarray sum of the array is " + sum
            + " starting at index " + startIndex + " and ending at index "
            + endIndex + ".", "comment6", null, finalCommentProps);

  }

  public void writeElseReturn() {
    sc.unhighlight(8);
    sc.unhighlight(10);
    sc.unhighlight(11);
    sc.unhighlight(12);
    sc.highlight(6);
    lang.nextStep();
    sc.toggleHighlight(6, 13);
    lang.nextStep();
    sc.toggleHighlight(13, 15);
    statusMessage.setText("Positive subarray sum is present.", null, null);
    addFinalComment();
  }
}
