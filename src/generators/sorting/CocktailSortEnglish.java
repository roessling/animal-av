package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
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
import algoanim.util.TicksTiming;

/**
 * @author ZHANG Qi
 * 
 */
public class CocktailSortEnglish implements Generator {
  /**
   * The concrete language object used for creating output
   */
  private Language              lang;
  private static final String   DESCRIPTION = "Cocktail sort is a variation of bubble sort that is both a stable sorting algorithm and a comparison sort.\n"
                                                + "The algorithm differs from a bubble sort in that it sorts in both directions on each pass through the list.\n\n\n\n\n\n"
                                                + "The algorithm:\n"
                                                + "1. The first rightward pass will shift the largest element to its correct place at the end.\n\n"
                                                + "2. The following leftward pass will shift the smallest element to its correct place at the beginning.\n\n"
                                                + "3. The second complete pass will shift the second largest and second smallest elements to their correct places.\n\n"
                                                + "4. After i passes, the first i and the last i elements in the list are in their correct positions, and do not need to be checked.";

  private static final String   SOURCE_CODE = "public void sort (int[] array) {" // 0
                                                + "\n	boolean moved=false;" // 1
                                                + "\n 	int curmax=array.length, curmin=1;" // 2
                                                + "\n	do {" // 3
                                                + "\n		moved=false;" // 4
                                                + "\n		for (int i=curmin; i < curmax; i++)" // 5
                                                + "\n			if (compare(array[i], array[i-1])<0) {" // 6
                                                + "\n				exchange(array[i], array[i-1]);" // 7
                                                + "\n				moved=true;" // 8
                                                + "\n			}" // 9
                                                + "\n		curmax--;" // 10
                                                + "\n		if (!moved) break;" // 11
                                                + "\n		for (int i=curmax-1; i >= curmin; i--)" // 12
                                                + "\n			if (compare(array[i], array[i-1])<0) {" // 13
                                                + "\n				exchange(array[i], array[i-1]);" // 14
                                                + "\n				moved=true;" // 15
                                                + "\n			}" // 16
                                                + "\n		curmin++;" // 17
                                                + "\n	} while (moved);" // 18
                                                + "\n}";                                                                                                                              // 19

  private ArrayProperties       arrayProps;
  private ArrayMarkerProperties arrayIMProps;
  private int                   assignCounts;
  private int                   compareCounts;

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   */
  public CocktailSortEnglish(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  public CocktailSortEnglish() {
    lang = new AnimalScript("Cocktail Sort [EN]", "Qi ZHANG", 800, 600);
    lang.setStepMode(true);
  }

  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public String getName() {
    return "Cocktail Sort [EN]";
  }

  public String getAlgorithmName() {
    return "Shaker Sort";
  }

  public String getAnimationAuthor() {
    return "Qi ZHANG";
  }

  public void sort(int[] a) {
    // 1. page
    // background
    assignCounts = 0;
    compareCounts = 0;
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(45, 166,
        218));
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        new Color(45, 166, 218));
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    Rect titleRect = lang.newRect(new Coordinates(10, 20), new Coordinates(160,
        55), "background", null, rectProps);
    // title
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    lang.newText(new Offset(10, 10, titleRect, AnimalScript.DIRECTION_NW),
        "Cocktail Sort", "title", null, textProps);
    // description
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 20));
    SourceCode desc = lang.newSourceCode(new Offset(0, 50, titleRect,
        AnimalScript.DIRECTION_SW), "desc1", null, scProps);
    desc.addCodeLine(
        "Cocktail sort is a variation of bubble sort that is both a stable sorting algorithm and a comparison sort.",
        null, 0, null);
    desc.addCodeLine(
        "The algorithm differs from a bubble sort in that it sorts in both directions on each pass through the list.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("The algorithm:", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "1. The first rightward pass will shift the largest element to its correct place at the end.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "2. The following leftward pass will shift the smallest element to its correct place at the beginning.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "3. The second complete pass will shift the second largest and second smallest elements to their correct places.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "4. After i passes, the first i and the last i elements in the list are in their correct positions, and do not need to be checked.",
        null, 0, null);
    // 2.page
    // Create SourceCode: coordinates, name, display options
    // first, set the visual properties for the source code

    // now, create the source code entity
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    SourceCode sc = lang.newSourceCode(new Offset(0, 40, titleRect,
        AnimalScript.DIRECTION_SW), "sourceCode", null, scProps);
    lang.nextStep();
    desc.hide();

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "First question group", 1);
    lang.addQuestionGroup(groupInfo);
    TrueFalseQuestionModel tfq = new TrueFalseQuestionModel(
        "trueFalseQuestion", true, 1);
    tfq.setPrompt("Is Cocktail sort a variation of bubble Sort?");
    tfq.setGroupID("First question group");
    lang.addTFQuestion(tfq);
    lang.nextStep();

    // Create Array: coordinates, data, name, display options,
    // default properties

    // first, set the visual properties (somewhat similar to CSS)

    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(45, 166,
        218));
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(
        255, 200, 0));
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 18));
    // now, create the IntArray object, linked to the properties

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy

    sc.addCodeLine("public void sort (int[] array) {", null, 0, null); // 0
    sc.addCodeLine("boolean moved = false;", null, 1, null); // 1
    sc.addCodeLine("int curmax = array.length, curmin=1;", null, 1, null); // 2
    sc.addCodeLine("do {", null, 1, null); // 3
    sc.addCodeLine("moved = false;", null, 2, null); // 4
    sc.addCodeLine("for (int i = curmin; i < curmax; i++)", null, 2, null); // 5
    sc.addCodeLine("if (compare(array[i], array[i-1]) < 0) {", null, 3, null); // 6
    sc.addCodeLine("exchange(array[i], array[i-1]);", null, 4, null); // 7
    sc.addCodeLine("moved = true;", null, 4, null); // 8
    sc.addCodeLine("}", null, 3, null); // 9
    sc.addCodeLine("curmax--;", null, 2, null); // 10
    sc.addCodeLine("if (!moved) break;", null, 2, null); // 11
    sc.addCodeLine("for (int i = curmax-1; i >= curmin; i--)", null, 2, null); // 12
    sc.addCodeLine("if (compare(array[i], array[i-1]) < 0) {", null, 3, null); // 13
    sc.addCodeLine("exchange(array[i], array[i-1]);", null, 4, null); // 14
    sc.addCodeLine("moved = true;", null, 4, null); // 15
    sc.addCodeLine("}", null, 3, null); // 16
    sc.addCodeLine("curmin++;", null, 2, null); // 17
    sc.addCodeLine("} while (moved);", null, 1, null); // 18
    sc.addCodeLine("}", null, 0, null); // 19

    IntArray ia = lang.newIntArray(new Offset(200, 0, sc,
        AnimalScript.DIRECTION_NE), a, "intArray", null, arrayProps);

    // parameter

    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 18));
    lang.newText(new Coordinates(485, 150), "Assignments: ", "Assignments",
        null, textProps);
    Text txtComparisons = lang.newText(new Coordinates(485, 190),
        "Comparisons: ", "Comparisons", null, textProps);
    Text txtMin = lang.newText(new Offset(0, 20, txtComparisons,
        AnimalScript.DIRECTION_SW), "curmin = 1", "min", null, textProps);
    Text txtMax = lang.newText(new Offset(0, 20, txtMin,
        AnimalScript.DIRECTION_SW), "curmax = " + a.length, "max", null,
        textProps);
    Text txtLoop = lang.newText(new Offset(0, 20, txtMax,
        AnimalScript.DIRECTION_SW), "Loop = 0", "loop", null, textProps);
    Text txtI = lang.newText(new Offset(0, 20, txtLoop,
        AnimalScript.DIRECTION_SW), "i = 0", "i", null, textProps);
    Text txtSorted = lang
        .newText(new Offset(0, 20, txtI, AnimalScript.DIRECTION_SW),
            "sorted = false", "sorted", null, textProps);
    lang.nextStep();
    // Highlight all cells
    try {
      // Start cocktailSort
      cocktailSort(ia, sc, txtMin, txtMax, txtLoop, txtI, textProps, txtSorted);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }

    // lang.nextStep();

    lang.newText(new Offset(0, 20, txtSorted, AnimalScript.DIRECTION_SW),
        "Finish, Cocktail Sort performed a total of " + assignCounts
            + " assignments and " + compareCounts + " comparisons", "finish",
        null, textProps);
    lang.nextStep();
    lang.hideAllPrimitives();
    lang.newRect(new Coordinates(10, 20), new Coordinates(160, 55),
        "background", null, rectProps);
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    lang.newText(new Offset(10, 10, titleRect, AnimalScript.DIRECTION_NW),
        "Cocktail Sort", "title", null, textProps);
    // description
    SourceCodeProperties scProps1 = new SourceCodeProperties();
    scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    SourceCode summary = lang.newSourceCode(new Coordinates(20, 80), "summary",
        null, scProps1);
    summary.addCodeLine("Summary", null, 0, null);
    summary.addCodeLine("", null, 0, null);
    summary.addCodeLine("", null, 0, null);
    scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 18));
    SourceCode detail = lang.newSourceCode(new Offset(0, 30, summary,
        AnimalScript.DIRECTION_SW), "detail", null, scProps1);
    detail
        .addCodeLine(
            "Cocktail sort is a slight variation of bubble sort. It differs in that instead of repeatedly passing through",
            null, 0, null);
    detail
        .addCodeLine(
            "the list from bottom to top, it passes alternately from bottom to top and then from top to bottom. It can",
            null, 0, null);
    detail
        .addCodeLine(
            "achieve slightly better performance than a standard bubble sort. The reason for this is that bubble sort",
            null, 0, null);
    detail
        .addCodeLine(
            "only passes through the list in one direction and therefore can only move items backward one step",
            null, 0, null);
    detail.addCodeLine("each iteration.", null, 0, null);
    detail.addCodeLine("", null, 0, null);
    detail
        .addCodeLine(
            "The complexity of cocktail sort in big O notation is O(n*n) or both the worst case and the average case,",
            null, 0, null);
    detail
        .addCodeLine(
            "but it becomes closer to O(n) if the list is mostly ordered before applying the sorting algorithm.",
            null, 0, null);
    detail.addCodeLine("", null, 0, null);
    detail
        .addCodeLine(
            "For example, if every element is at a position that differs at most k (k ≥ 1) from the position it is going to",
            null, 0, null);
    detail.addCodeLine(
        "end up in, the complexity of cocktail sort becomes O(n*k).", null, 0,
        null);
  }

  /**
   * @param txtI
   * @param txtLoop
   * @param txtMax
   * @param txtMin
   * @param textProps
   * @param txtSorted
   * 
   */
  private void cocktailSort(IntArray array, SourceCode codeSupport,
      Text txtMin, Text txtMax, Text txtLoop, Text txtI,
      TextProperties textProps, Text txtSorted) {
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(45, 166,
        218));
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        new Color(45, 166, 218));
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    ArrayMarker iMarker = lang
        .newArrayMarker(array, 0, "i", null, arrayIMProps);

    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i-1");
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    // ArrayMarker jMarker = lang.newArrayMarker(array, 0, "i-1",
    // null, arrayIMProps);
    // TextProperties variableProps=new TextProperties();
    // variableProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    // Text v1=lang.newText(new Coordinates(680,290), "", "v1",
    // null,variableProps);
    boolean moved = false;
    codeSupport.highlight(1);
    codeSupport.highlight(2);
    assignCounts = assignCounts + 3;
    lang.newRect(new Coordinates(650, 150), new Coordinates(650 + assignCounts,
        170), "rectAssignments" + assignCounts, null, rectProps);
    lang.nextStep();
    codeSupport.unhighlight(1);
    codeSupport.unhighlight(2);
    array.highlightCell(0, null, null);
    array.highlightCell(1, null, null);
    codeSupport.highlight(5);
    lang.nextStep();
    int curmax = array.getLength(), curmin = 1;
    int loopCount = 0;
    do {
      loopCount++;
      txtLoop.setText("Loop = " + loopCount, null, null);
      codeSupport.unhighlight(18);
      moved = false;
      assignCounts++;
      lang.newRect(new Coordinates(650, 150), new Coordinates(
          650 + assignCounts, 170), "rectAssignments" + assignCounts, null,
          rectProps);
      lang.nextStep("Loop " + loopCount);
      for (int i = curmin; i < curmax; i++) {
        assignCounts++;
        compareCounts++;
        lang.newRect(new Coordinates(650, 150), new Coordinates(
            650 + assignCounts, 170), "rectAssignments" + assignCounts, null,
            rectProps);
        lang.newRect(new Coordinates(650, 190), new Coordinates(
            650 + compareCounts, 210), "rectCompareCounts" + compareCounts,
            null, rectProps);
        iMarker.move(i, null, null);
        // jMarker.move(i-1, null, null);
        txtI.setText("i = " + i, null, null);
        // v1.setText(i+"", null, null);
        // v1.changeColor("Color", Color.RED, new TicksTiming(100), new
        // TicksTiming(100));
        // v1.changeColor("Color", Color.BLACK, new TicksTiming(100), new
        // TicksTiming(100));
        // array.highlightCell(i, null, null);
        array.highlightCell(i - 1, null, null);
        codeSupport.unhighlight(7);
        codeSupport.unhighlight(8);
        codeSupport.toggleHighlight(5, 6);
        lang.nextStep("in Loop " + loopCount + " i = " + i);
        if (array.getData(i) < array.getData(i - 1)) {
          txtSorted.setText("sorted = false", null, null);
          array.swap(i, i - 1, null, new TicksTiming(50));
          moved = true;
          codeSupport.toggleHighlight(6, 7);
          codeSupport.highlight(8);
          compareCounts++;
          assignCounts = assignCounts + 2;
          lang.newRect(new Coordinates(650, 150), new Coordinates(
              650 + assignCounts, 170), "rectAssignments" + assignCounts, null,
              rectProps);
          lang.newRect(new Coordinates(650, 190), new Coordinates(
              650 + compareCounts, 210), "rectCompareCounts" + compareCounts,
              null, rectProps);
        } else {
          codeSupport.unhighlight(6);
          txtSorted.setText("sorted = true", null, null);
        }
        array.unhighlightCell(i, null, null);
        array.unhighlightCell(i - 1, null, null);

        lang.nextStep();
      }
      curmax--;
      assignCounts++;
      lang.newRect(new Coordinates(650, 150), new Coordinates(
          650 + assignCounts, 170), "rectAssignments" + assignCounts, null,
          rectProps);
      txtMax.setText("curmax = " + curmax, null, null);
      codeSupport.unhighlight(7);
      codeSupport.toggleHighlight(8, 10);
      lang.nextStep();
      codeSupport.toggleHighlight(10, 11);
      lang.nextStep();
      if (!moved) {
        compareCounts++;
        lang.newRect(new Coordinates(650, 150), new Coordinates(
            650 + assignCounts, 170), "rectAssignments" + assignCounts, null,
            rectProps);
        break;
      }
      codeSupport.toggleHighlight(11, 12);
      lang.nextStep();
      for (int i = curmax - 1; i >= curmin; i--) {
        assignCounts++;
        compareCounts++;
        lang.newRect(new Coordinates(650, 150), new Coordinates(
            650 + assignCounts, 170), "rectAssignments" + assignCounts, null,
            rectProps);
        lang.newRect(new Coordinates(650, 190), new Coordinates(
            650 + compareCounts, 210), "rectCompareCounts" + compareCounts,
            null, rectProps);
        iMarker.move(i, null, null);
        // jMarker.move(i-1, null, null);
        txtI.setText("i = " + i, null, null);
        array.highlightCell(i, null, null);
        array.highlightCell(i - 1, null, null);
        codeSupport.unhighlight(14);
        codeSupport.unhighlight(15);
        codeSupport.toggleHighlight(12, 13);
        lang.nextStep();
        if (array.getData(i) < array.getData(i - 1)) {
          assignCounts = assignCounts + 2;
          compareCounts++;
          lang.newRect(new Coordinates(650, 150), new Coordinates(
              650 + assignCounts, 170), "rectAssignments" + assignCounts, null,
              rectProps);
          lang.newRect(new Coordinates(650, 190), new Coordinates(
              650 + compareCounts, 210), "rectCompareCounts" + compareCounts,
              null, rectProps);

          array.swap(i, i - 1, null, new TicksTiming(50));
          codeSupport.toggleHighlight(13, 14);
          codeSupport.highlight(15);
          moved = true;
          txtSorted.setText("sorted = false", null, null);

        } else {
          codeSupport.unhighlight(13);
          txtSorted.setText("sorted = true", null, null);
        }
        array.unhighlightCell(i, null, null);
        array.unhighlightCell(i - 1, null, null);
        lang.nextStep();
      }
      curmin++;
      assignCounts = assignCounts + 2;
      lang.newRect(new Coordinates(650, 150), new Coordinates(
          650 + assignCounts, 170), "rectAssignments" + assignCounts, null,
          rectProps);
      txtMin.setText("curmin = " + curmin, null, null);
      codeSupport.unhighlight(14);
      codeSupport.toggleHighlight(15, 17);
      lang.nextStep();
      codeSupport.toggleHighlight(17, 18);
      compareCounts++;
      lang.newRect(new Coordinates(650, 190), new Coordinates(
          650 + compareCounts, 210), "rectCompareCounts" + compareCounts, null,
          rectProps);

      lang.nextStep();
    } while (moved);
    codeSupport.unhighlight(11);
  }

  public static void main(String[] args) {
    // Create a new animation
    // name, author, screen width, screen height
    // Language l = new AnimalScript("Cocktail Sort Animation", "Qi ZHANG", 640,
    // 480);
    CocktailSortEnglish s = new CocktailSortEnglish();
    s.init();
    int[] a = { 55, 7, 78, 12, 42 };
    s.sort(a);
    System.out.println(s.lang);
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void init() {
    arrayProps = new ArrayProperties();
    arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init(); // ensure all properties are set up

    // for (Iterator it = primitives.keySet().iterator(); it.hasNext(); ) {
    // String key = (String) it.next();
    // Object value = primitives.get(key);
    // System.out.println("key= "+key+"    value= "+value);
    // }

    int[] arrayData = (int[]) primitives.get("intArray");
    // arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
    // props.get("intArray",
    // AnimationPropertiesKeys.COLOR_PROPERTY));
    sort(arrayData);
    lang.finalizeGeneration();
    return lang.toString();
  }
}
