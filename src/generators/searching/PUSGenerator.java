package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
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
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class PUSGenerator implements Generator {
  private Language                  lang;
  private ArrayMarkerProperties     markerProps;
  private ArrayMarkerProperties     indexProps;
  private ArrayMarkerProperties     boundProps;
  private int                       element;
  private TextProperties            textProps;
  private TextProperties            helperProps;
  private int[]                     array;
  private ArrayProperties           arrayProps;
  private SourceCodeProperties      codeProps;
  private int                       boundaryStore = 0;

  // private Text header;
  // private Text description;
  // private Rect hRect;
  private IntArray                  intArray;
  private SourceCode                sc;
  private ArrayMarker               aMarker;
  private ArrayMarker               boundMarker;
  private ArrayMarker               indexMarker;
  private Text                      counter;
  private int                       count;
  private Text                      helper;
  private FillInBlanksQuestionModel algoQuest1;
  private FillInBlanksQuestionModel algoQuest2;
  private Boolean                   highlightQSonFirstIteration;

  public void init() {
    lang = new AnimalScript("Finding the n-th element with prune and search",
        "Florian Spitz, Toni Plöchl", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    markerProps = (ArrayMarkerProperties) props
        .getPropertiesByName("markerProps");
    indexProps = (ArrayMarkerProperties) props
        .getPropertiesByName("indexProps");
    boundProps = (ArrayMarkerProperties) props
        .getPropertiesByName("boundProps");
    element = (Integer) primitives.get("element");
    textProps = (TextProperties) props.getPropertiesByName("textProps");
    helperProps = (TextProperties) props.getPropertiesByName("helperProps");
    array = (int[]) primitives.get("array");
    arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
    codeProps = (SourceCodeProperties) props.getPropertiesByName("codeProps");
    highlightQSonFirstIteration = (Boolean) primitives
        .get("highlightQSonFirstIteration");

    lang.setStepMode(true);

    // Animal bugs: --> Abschlussbericht!
    // int[] intArray = { 8, 9, 7, 4, 3, 18, 20, 2 };
    // broken array bug: int[] a = { 3, 4, 7, 9, 6 };

    // int[] intArray = { 8, 9, 7, 4, 3, 1, 5, 2 };

    // First page... Header + description
    header();
    description();
    lang.nextStep("Introduction");

    // Second page... Header + subHeadline
    lang.hideAllPrimitives();
    header();

    lang.newText(new Coordinates(20, 60), "find the " + ordinal(element)
        + " highest number within the following array:", "headline", null,
        textProps);

    // create the helper
    helper = lang.newText(new Coordinates(40, 240), "", "helper", null,
        helperProps);

    // now, create the source code entity
    sc = lang.newSourceCode(new Coordinates(40, 260), "sourceCode", null,
        codeProps);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display delay
    sc.addCodeLine(
        "public static int pruneAndSearch(int[] array, int index, int left, int right) {",
        null, 0, null); // 0
    sc.addCodeLine("int boundary = left;", null, 3, null); // 1
    sc.addCodeLine("//quicksort", null, 3, null); // 2
    sc.addCodeLine("for (int i = left + 1; i < right; i++) {", null, 3, null); // 3
    sc.addCodeLine("if (array[i] > array[left]) {", null, 5, null); // 4
    sc.addCodeLine("swap(array, i, ++boundary);", null, 7, null); // 5
    sc.addCodeLine("}", null, 5, null); // 6
    sc.addCodeLine("}", null, 3, null); // 7
    sc.addCodeLine("swap(array, left, boundary);", null, 3, null); // 8
    sc.addCodeLine("//quicksort end", null, 3, null); // 9
    sc.addCodeLine(" ", null, 3, null); // 10
    sc.addCodeLine("if (boundary == index) {", null, 3, null); // 11
    sc.addCodeLine("return array[boundary];", null, 5, null); // 12
    sc.addCodeLine("} else if (boundary > index) {", null, 3, null); // 13
    sc.addCodeLine("return pruneAndSearch(array, index, left, boundary);",
        null, 5, null); // 14
    sc.addCodeLine("} else {", null, 3, null); // 15
    sc.addCodeLine("return pruneAndSearch(array, index, boundary + 1, right);",
        null, 5, null); // 16
    sc.addCodeLine("}", null, 3, null); // 17
    sc.addCodeLine("}", null, 0, null); // 18

    highlightSC(0, false);

    // create the counter
    counter = lang.newText(new Coordinates(40, 210), "Iteration: " + count,
        "counter", null, textProps);
    // reset the counter for executing multiple animations ins Animal
    count = 0;
    countTheCounter();

    // create the intArray
    intArray = lang.newIntArray(new Coordinates(20, 160), array, "intArray",
        null, arrayProps);

    // create the marker
    aMarker = lang.newArrayMarker(intArray, 0, "aMarker", null, markerProps);
    aMarker.hide();

    // create the pivot Marker
    boundMarker = lang.newArrayMarker(intArray, 0, "b", null, boundProps);
    boundMarker.hide();

    // create the index marker
    indexMarker = lang.newArrayMarker(intArray, 0, "i", null, indexProps);
    indexMarker.hide();

    lang.nextStep();

    // QUestions....
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    algoQuest1 = new FillInBlanksQuestionModel("algoQuest1");
    algoQuest2 = new FillInBlanksQuestionModel("algoQuest2");

    pruneAndSearch(intArray);

    // Result
    lang.nextStep("Result");

    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Finding the n-th element with prune and search";
  }

  public String getAlgorithmName() {
    return "Prune and Search";
  }

  public String getAnimationAuthor() {
    return "Florian Spitz, Toni Plöchl";
  }

  public String getDescription() {
    return "Prune and search is a method for finding an optimal value by iteratively dividing a search space into two parts - the promising one, which contains the optimal value and is recursively searched and the second part without optimal value, which is pruned (thrown away). This paradigm is very similar to well know divide and conquer algorithms. Sorting the whole array and returning the n-th value would be very ineffective. A better solution is to employ a modified prune-and-search version of quicksort. The modified prune-and-search algorithm advances similarly as quicksort, in the first phase the array is divided, in phase 2 only the part, which contains the n-th index, gets sorted. The two parts remaining are pruned, because they cannot contain a solution."
        + "\n";
  }

  public String getCodeExample() {
    return "public static int pruneAndSearch(int[] array, int index, int left, int right) {"
        + "\n"
        + "	int boundary = left;"
        + "\n"
        + "	// quicksort"
        + "\n"
        + "	for (int i = left + 1; i < right; i++) {"
        + "\n"
        + "		if (array[i] > array[left]) {"
        + "\n"
        + "			swap(array, i, ++boundary);"
        + "\n"
        + "		}"
        + "\n"
        + "	}"
        + "\n"
        + "	swap(array, left, boundary);"
        + "\n"
        + "	// quicksort end"
        + "\n"
        + "		if (boundary == index) {"
        + "\n"
        + "		return array[boundary];"
        + "\n"
        + "	} else if (boundary > index) {"
        + "\n"
        + "		return pruneAndSearch(array, index, left, boundary);"
        + "\n"
        + "	} else {"
        + "\n"
        + "		return pruneAndSearch(array, index, boundary + 1, right);"
        + "\n"
        + "	}" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void header() {
    // show the header with a heading surrounded by a rectangle
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30),
        "Finding the n-th largest number with prune and search", "header",
        null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);
  }

  public void description() {
    // setup the description
    lang.newText(
        new Coordinates(20, 100),
        "Prune and search is a method for finding an optimal value by iteratively dividing a",
        "description1", null, textProps);
    lang.newText(
        new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
        "search space into two parts - the promising one, which contains the optimal value and",
        "description2", null, textProps);
    lang.newText(
        new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
        "is recursively searched and the second part without optimal value, which is pruned",
        "description3", null, textProps);
    lang.newText(
        new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
        "(thrown away). This paradigm is very similar to well know divide and conquer algorithms",
        "description4", null, textProps);
    lang.newText(
        new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
        "Sorting the whole array and returning the n-th value would be very ineffective.",
        "description5", null, textProps);
    lang.newText(
        new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
        "A better solution is to employ a modified prune-and-search version of quicksort",
        "description6", null, textProps);
    lang.newText(
        new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
        "The modified prune-and-search algorithm advances similarly as quicksort, in the first",
        "description7", null, textProps);
    lang.newText(
        new Offset(0, 25, "description7", AnimalScript.DIRECTION_NW),
        "gets sorted. The two parts remaining are pruned, because they cannot contain a solution.",
        "description8", null, textProps);
  }

  public void pruneAndSearch(IntArray array) throws LineNotExistsException {
    try {
      // Start prune
      pruneAndSearch(array, element - 1, 0, array.getLength());
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prune and search
   * 
   * @param a
   *          the array to be searched in
   * @param index
   *          order of the searched value (indexed starting at 0)
   * @param left
   *          first element, which can be touched
   * @param right
   *          first element, which cannot be touched
   * @return n-th largest value
   */
  public int pruneAndSearch(IntArray a, int index, int left, int right) {
    // step 1 select pivot

    int boundary = left;

    if (highlightQSonFirstIteration) {
      indexMarker.show();
      boundMarker.show();
    }

    highlightArrayCell(boundaryStore, true);
    boundMarker.move(boundaryStore, null, null);
    indexMarker.move(boundaryStore, null, null);
    highlightSC(1, true);
    helper.setText("set left element as pivot (highlighted above)", null, null);
    lang.nextStep(ordinal(count) + " iteration");

    if (!highlightQSonFirstIteration) {
      helper.setText("quicksort", null, null);
      highlightSC(2, 9, true);
      for (int i = left + 1; i < right; i++) {
        if (a.getData(i) > a.getData(left)) {
          intArray.swap(i, ++boundary, null, null);
        }
      }
      lang.nextStep();
    } else {
      // step 2 quicksort
      highlightSC(3, true);
      for (int i = left + 1; i < right; i++) {
        indexMarker.move(i, null, null);

        helper.setText(i + " < " + right + " = " + (i < right), null, null);
        lang.nextStep();
        highlightSC(4, true);
        helper.setText(
            a.getData(i) + " > " + a.getData(left) + " = "
                + (a.getData(i) > a.getData(left)), null, null);
        lang.nextStep();
        if (a.getData(i) > a.getData(left)) {
          // place all values > pivot after the pivot
          highlightSC(5, true);
          helper.setText("inc pivot and swap element " + a.getData(i)
              + " with " + a.getData(boundary + 1), null, null);
          intArray.swap(i, ++boundary, null, null);
          boundMarker.move(boundary, null, null);
          lang.nextStep();

        }
        highlightSC(3, true);
        helper.setText((i + 1) + " < " + right + " = " + ((i + 1) < right),
            null, null);
      }

      // place pivot in the middle
      lang.nextStep();
      highlightSC(8, true);

    }

    helper
        .setText("move pivot element " + a.getData(left)
            + " to the middle. (Swap with " + a.getData(boundary) + ")", null,
            null);
    if (highlightQSonFirstIteration) {
      boundMarker.move(boundary, null, null);
      indexMarker.move(left, null, null);
    }

    intArray.swap(left, boundary, new MsTiming(100), new MsTiming(100));
    aMarker.move(left, null, null);

    highlightArrayCell(boundary, true);
    boundaryStore = boundary;

    lang.nextStep();
    if (highlightQSonFirstIteration) {
      indexMarker.hide();
      boundMarker.hide();
      highlightQSonFirstIteration = false;
    }

    helper.hide();
    // step 3 select
    highlightSC(11, true);
    algoQuest1.setPrompt("Have we already found the answer? (yes/no)");
    algoQuest1.addAnswer((boundary == index ? "yes" : "no"), 1, "");
    lang.addFIBQuestion(algoQuest1);
    lang.nextStep();
    if (boundary == index) {
      // found

      highlightSC(12, true);
      helper.show();
      helper.setText("result found", null, null);
      if (highlightQSonFirstIteration) {
        indexMarker.hide();
        boundMarker.hide();
      }
      aMarker.show();
      lang.nextStep();
      helper.hide();

      // show result step
      sc.hide();
      counter.hide();

      lang.newText(new Coordinates(20, 200), "The " + ordinal(element)
          + " largest number is " + a.getData(boundary) + ".", "result", null,
          textProps);
      lang.newText(new Offset(0, 25, "result", AnimalScript.DIRECTION_NW),
          "The algorithm needed " + count + " iterations to find it.",
          "result2", null, textProps);
      lang.newText(
          new Offset(0, 25, "result2", AnimalScript.DIRECTION_NW),
          "The algorithm finds the n-th largest number in a O(c*n) expected complexity, where c is a small constant (approx 4).",
          "result3", null, textProps);

      return a.getData(boundary);

    }
    highlightSC(13, true);
    algoQuest2.setPrompt("Which branch will get pruned? (left/right)");
    algoQuest2.addAnswer((boundary > index ? "right" : "left"), 1, "");
    lang.addFIBQuestion(algoQuest2);
    lang.nextStep();
    if (boundary > index) {
      // prune the right branch
      highlightSC(14, true);
      helper.show();
      helper.setText("prune the right branch", null, null);
      intArray.highlightElem(boundary + 1, a.getLength() - 1, null, null);
      lang.nextStep();
      countTheCounter();

      return pruneAndSearch(a, index, left, boundary);

    }
    highlightSC(15, true);
    lang.nextStep();
    if (boundary < index) { // else
      // prune the left branch
      highlightSC(16, true);
      helper.show();
      helper.setText("prune the left branch", null, null);
      intArray.highlightElem(0, boundary - 1, null, null);
      lang.nextStep();
      countTheCounter();
      return pruneAndSearch(a, index, boundary + 1, right);
    }
    // will never happen
    return 42;
  }

  /*
   * Helper methods
   */

  public void highlightSC(int i, boolean unhighlight) {
    highlightSC(i, i, unhighlight);
  }

  public void highlightSC(int from, int to, boolean unhighlight) {
    if (unhighlight) {
      unhighlightSC();
    }
    for (int i = from; i <= to; i++) {
      sc.highlight(i);
    }
  }

  public void unhighlightSC() {
    for (int i = 0; i <= 18; i++) {
      sc.unhighlight(i);
    }
  }

  public void highlightArrayCell(int i, boolean unhighlight) {
    if (unhighlight) {
      unhighlightAllCells();
    }
    intArray.highlightCell(i, null, null);
  }

  public void unhighlightAllCells() {
    for (int i = 0; i < intArray.getLength(); i++) {
      intArray.unhighlightCell(i, null, null);
    }
  }

  public void countTheCounter() {
    count++;
    counter.setText("Iteration: " + count, null, null);
  }

  // returns 1st, 2nd, 3rd, 4th etc.
  // why oh why did we choose English? :D
  public String ordinal(int i) {
    String[] suffix = new String[] { "th", "st", "nd", "rd" };
    if ((i % 10) > 3)
      return i + "th";
    else
      return i + suffix[i % 10];

    // switch (i % 10) {
    // case 1:
    // return i + "st";
    // case 2:
    // return i + "nd";
    // case 3:
    // return i + "rd";
    // default:
    // return i + "th";
    //
    // int lastDigit = i % 10;
    // if (lastDigit == 1)
    // return i + "st";
    // if (lastDigit == 2)
    // return i + "nd";
    // if (lastDigit == 3)
    // return i + "rd";
    // else
    // return i + "th";
  }

}
