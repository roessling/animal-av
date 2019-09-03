package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class StrandSortGen implements Generator {

  static int                           resultsLastIndex = 0;
  private Language                     lang;
  private int[]                        unsortedArray;           // stores the
                                                                 // array which
                                                                 // has to be
                                                                 // sorted
  private ArrayMarkerProperties        arrayMarkerPropsI;
  private ArrayMarkerProperties        subarrayMarkerProps;
  private ArrayProperties              arrayProps;
  private ArrayProperties              resultsArrayProps;
  private SourceCode                   strandSourceCode;
  private IntArray                     arrAnim;
  private TextProperties               headerProps;
  private SourceCodeProperties         strandSortProps;

  private AnimationPropertiesContainer container;
//  private boolean                      asked            = false;

  /**
   * Constructor. Used for creating objects and initializing different
   * properties.
   * 
   * @param array
   *          - the unsorted array which we need to sort
   */
  StrandSortGen() {
  }

  public void init() {
    lang = new AnimalScript("Strand Sort [EN]",
        "Iliya Gurov, Vladislava Arabadzhieva", 800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  private void initProps() {
    arrayMarkerPropsI = new ArrayMarkerProperties();
    subarrayMarkerProps = new ArrayMarkerProperties();
    arrayProps = new ArrayProperties();
    resultsArrayProps = new ArrayProperties();

    headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.BOLD, 20));
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.gray);
    strandSortProps = new SourceCodeProperties();

  }

  // -----------------------------------------------------SET PROPS ...from
  // container
  private void setSourceCodeProps() {
    this.strandSortProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        container.get("source", AnimationPropertiesKeys.COLOR_PROPERTY));
    this.strandSortProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        container
            .get("source", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
  }

  private void setArrayMarkerProps() {
    this.arrayMarkerPropsI.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        container.get("arrayMarker", AnimationPropertiesKeys.LABEL_PROPERTY));
    this.arrayMarkerPropsI.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        container.get("arrayMarker", AnimationPropertiesKeys.COLOR_PROPERTY));

  }

  private void setSubarrayMarkerProps() {// setMinMarkerProps
    this.subarrayMarkerProps
        .set(AnimationPropertiesKeys.LABEL_PROPERTY, container.get(
            "subarrayMarker", AnimationPropertiesKeys.LABEL_PROPERTY));
    this.subarrayMarkerProps
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, container.get(
            "subarrayMarker", AnimationPropertiesKeys.COLOR_PROPERTY));
  }

  private void setArrayProps() {
    // Array Properties
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        container.get("array", AnimationPropertiesKeys.COLOR_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
        container.get("array", AnimationPropertiesKeys.FILLED_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        container.get("array", AnimationPropertiesKeys.FILL_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        container.get("array", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        container.get("array", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
  }

  private void setResultProps() {

    resultsArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        container.get("results", AnimationPropertiesKeys.COLOR_PROPERTY));
    resultsArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
        container.get("results", AnimationPropertiesKeys.FILLED_PROPERTY));
    resultsArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        container.get("results", AnimationPropertiesKeys.FILL_PROPERTY));
    resultsArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        container
            .get("results", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
    resultsArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        container
            .get("results", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
  }

  private void setDefaultSourceCodeProperties() {
    strandSortProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    strandSortProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.red);
  }

  private void setDefaultArrayMarkerProps() {
    arrayMarkerPropsI.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayMarkerPropsI.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
  }

  private void setDefaultSubarrayMarkerProps() {
    subarrayMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    subarrayMarkerProps
        .set(AnimationPropertiesKeys.LABEL_PROPERTY, "lastIndex");
  }

  private void setDefaultArrayProps() {
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.blue);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.yellow);
  }

  private void setDefaultResultProps() {
    resultsArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    resultsArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    resultsArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    resultsArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.blue);
    resultsArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.cyan);
  }

  // -----------------------------------------------------------------------------------------------
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();
    lang.setStepMode(true);
    initProps();
    this.container = props;
    if (!(primitives == null) && !(primitives.get("arrayToSort") == null))
      unsortedArray = (int[]) primitives.get("arrayToSort");

    if (container == null || container.isEmpty()) {
      setDefaultSourceCodeProperties();
      setDefaultArrayMarkerProps();
      setDefaultSubarrayMarkerProps();
      setDefaultArrayProps();
      setDefaultResultProps();
    } else {
      if (container.getPropertiesByName("source") != null)
        setSourceCodeProps();
      else
        setDefaultSourceCodeProperties();
      if (container.getPropertiesByName("array") != null)
        setArrayProps();
      else
        setDefaultArrayProps();
      if (container.getPropertiesByName("results") != null)
        setResultProps();
      else
        setDefaultResultProps();
      if (container.getPropertiesByName("subarrayMarker") != null)
        setSubarrayMarkerProps();
      else
        setDefaultSubarrayMarkerProps();
      if (container.getPropertiesByName("arrayMarker") != null)
        setArrayMarkerProps();
      else
        setDefaultArrayMarkerProps();
    }

    animateSort();
    return lang.toString();
  }

  /**
   * Visualizing the code and the unsorted array. Calling the actual
   * sort-function
   */
  public void animateSort() {

    // next step - add header
    lang.nextStep("Introduction - idea");
    lang.newText(new Coordinates(10, 20), "STRAND SORT", "header", null,
        headerProps);
    lang.newText(new Offset(0, 5, "header", "SW"), "Idea:", "idea", null);
    lang.newText(
        new Offset(5, 5, "idea", "SW"),
        "The algorithm works by repeatedly pulling sorted subarrays out of the array to be sorted and merging them with a result array.",
        "row1", null);
    lang.newText(
        new Offset(0, 5, "row1", "SW"),
        "Each iteration through the unsorted array pulls out a series of elements which were already sorted, and merges those series together.",
        "row2", null);

    // next step - add source code
    lang.nextStep("Introduction - source code");
    SourceCodeProperties strandSortProperties = new SourceCodeProperties();
    strandSortProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.black);
    strandSortProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.red);

    strandSourceCode = lang.newSourceCode(new Offset(0, 45, "row2", "SW"),
        "code", null, strandSortProperties);
    strandSourceCode.addCodeLine("int[] strandSort(int[] array){", null, 0,
        null); // 0
    strandSourceCode.addCodeLine("int len = array.length;", null, 1, null); // 1
    strandSourceCode.addCodeLine("int counterSorted = 0, lastIndex = 0;", null,
        1, null); // 2
    strandSourceCode.addCodeLine("int[] subarray = new int[len];", null, 1,
        null); // 3
    strandSourceCode.addCodeLine("boolean[] isEltSorted = new boolean[len];",
        null, 1, null); // 4
    strandSourceCode.addCodeLine("int[] sortedArray = new int[len];", null, 1,
        null); // 6
    strandSourceCode.addCodeLine("for (int i = 0; i < len; i++){", null, 2,
        null); // 7
    strandSourceCode.addCodeLine("subarray[i] = Integer.MAX_VALUE;", null, 4,
        null); // 8
    strandSourceCode.addCodeLine("isEltSorted[i] = false;", null, 4, null); // 9
    strandSourceCode.addCodeLine("}", null, 2, null); // 10
    strandSourceCode.addCodeLine("while(counterSorted < len){", null, 2, null); // 11
    strandSourceCode.addCodeLine("for (int i = 0; i < len; i++){", null, 4,
        null); // 12
    strandSourceCode.addCodeLine(
        "if (( ( !isEltSorted[i] ) && (lastIndex == 0) ) || ", null, 5, null); // 13
    strandSourceCode.addCodeLine(
        "( (!isEltSorted[i]) && (array[i] > subarray[lastIndex-1]) )){", null,
        5, null); // 14
    strandSourceCode.addCodeLine("subarray[lastIndex] = array[i];", null, 6,
        null); // 15
    strandSourceCode.addCodeLine("isEltSorted[i] = true;", null, 6, null); // 16
    strandSourceCode.addCodeLine("counterSorted ++;", null, 6, null); // 17
    strandSourceCode.addCodeLine("lastIndex ++;", null, 6, null); // 18
    strandSourceCode.addCodeLine("}", null, 5, null); // 19
    strandSourceCode.addCodeLine("}", null, 4, null); // 20
    strandSourceCode.addCodeLine(
        "sortedArray = mergeArray (sortedArray, subarray, lastIndex);", null,
        4, null); // 21
    strandSourceCode.addCodeLine("lastIndex = 0;", null, 4, null); // 22
    strandSourceCode.addCodeLine("}", null, 2, null); // 23
    strandSourceCode.addCodeLine("return sortedArray;", null, 1, null); // 24
    strandSourceCode.addCodeLine("}", null, 0, null); // 25

    // next step - add array
    lang.nextStep();
    lang.newText(new Offset(20, 75, "code", "NE"), "unsorted array:",
        "arrayText", null, new TextProperties());
    arrAnim = lang.newIntArray(new Offset(40, 0, "arrayText", "NE"),
        unsortedArray, "arrayToSort", null, arrayProps);

    // next step - visualize sort
    lang.nextStep();
    strandSort(strandSourceCode, arrAnim);

  }

  private void strandSort(SourceCode strandSourceCode, IntArray unsortedArray) {

    strandSourceCode.highlight(0);
    int counterSorted = 0;

    // next step
    lang.nextStep("Initialization");
    strandSourceCode.toggleHighlight(0, 1);

    // next step
    lang.nextStep();
    strandSourceCode.toggleHighlight(1, 2);

    // next step
    lang.nextStep();
    int[] subarray = new int[unsortedArray.getLength()];
    strandSourceCode.toggleHighlight(2, 3);
    lang.newText(new Offset(0, 75, "arrayText", "SW"), "subarray:",
        "sublistText", null, new TextProperties());
    IntArray subarrayAnim = lang.newIntArray(new Offset(40, 0, "sublistText",
        "NE"), subarray, "sublistAnim", null, arrayProps);

    // next step
    lang.nextStep();
    boolean[] isEltSorted = new boolean[unsortedArray.getLength()];
    String[] eltSortedString = new String[unsortedArray.getLength()];
    for (int i = 0; i < eltSortedString.length; i++)
      eltSortedString[i] = "";
    strandSourceCode.toggleHighlight(3, 4);
    lang.newText(new Offset(0, 75, "sublistText", "SW"), "isEltSorted:",
        "isEltSortedText", null, new TextProperties());
    StringArray isEltSortedAnim = lang.newStringArray(new Offset(40, 0,
        "isEltSortedText", "NE"), eltSortedString, "isArrayEltSortedAnim",
        null, arrayProps);

    // next step
    int[] results = new int[unsortedArray.getLength()];
    lang.nextStep();
    strandSourceCode.toggleHighlight(4, 5);
    lang.newText(new Offset(0, 75, "isEltSortedText", "SW"), "sorted array:",
        "resultsText", null, new TextProperties());
    IntArray resultsAnim = lang.newIntArray(new Offset(40, 0, "resultsText",
        "NE"), results, "resultsAnim", null, resultsArrayProps);

    int lastIndex;
    lang.nextStep();
    strandSourceCode.toggleHighlight(5, 6);
    ArrayMarker sublistMarker = lang.newArrayMarker(subarrayAnim, 0,
        "lastIndex", null, subarrayMarkerProps);
    ArrayMarker isEltSortedMarker = lang.newArrayMarker(isEltSortedAnim, 0,
        "i", null, arrayMarkerPropsI);
    ArrayMarker iMarker = lang.newArrayMarker(unsortedArray, 0, "i", null,
        arrayMarkerPropsI);
    iMarker.hide();

    for (lastIndex = 0; lastIndex < unsortedArray.getLength(); lastIndex++) {
      sublistMarker.move(lastIndex, null, null);
      isEltSortedMarker.move(lastIndex, null, null);
      lang.nextStep();
      strandSourceCode.toggleHighlight(6, 7);
      subarray[lastIndex] = Integer.MAX_VALUE;
      subarrayAnim.put(lastIndex, Integer.MAX_VALUE, null, null);
      lang.nextStep();
      strandSourceCode.toggleHighlight(7, 8);

      isEltSortedAnim.put(lastIndex, "false", null, null);
      isEltSorted[lastIndex] = false;
      lang.nextStep();

      strandSourceCode.toggleHighlight(8, 9);
      lang.nextStep();
      strandSourceCode.toggleHighlight(9, 6);
    }

    lastIndex = 0;
    lang.nextStep();
    sublistMarker.hide();
    isEltSortedMarker.hide();
    strandSourceCode.toggleHighlight(6, 10);

    int counterQuestion = 1;
    for (int i = 1; i < unsortedArray.getLength(); i++)
      if (unsortedArray.getData(i) > unsortedArray.getData(0))
        counterQuestion++;

    TrueFalseQuestionModel tfquestionModel = new TrueFalseQuestionModel("TF",
        false, 1);
    tfquestionModel
        .setPrompt(((Integer) (counterQuestion - 1)).toString()
            + " elements will be included to the subarray during the first iteration. The statement is");
    tfquestionModel.setFeedbackForAnswer(true, "incorrect");
    tfquestionModel.setFeedbackForAnswer(false, "correct");
    lang.addTFQuestion(tfquestionModel);

    while (counterSorted < unsortedArray.getLength()) { // 10
      lang.nextStep();
      for (int i = 0; i < unsortedArray.getLength(); i++) { // 11

        strandSourceCode.toggleHighlight(10, 11);
        sublistMarker.move(lastIndex, null, null);
        isEltSortedMarker.move(i, null, null);
        iMarker.show();
        iMarker.move(i, null, null);
        isEltSortedMarker.show();
        sublistMarker.show();
        lang.nextStep();
        unsortedArray.highlightCell(i, null, null);
        subarrayAnim.highlightCell((lastIndex - 1), null, null);
        isEltSortedAnim.highlightCell(i, null, null);
        strandSourceCode.toggleHighlight(11, 12);
        strandSourceCode.highlight(13);

        if (((!isEltSorted[i]) && (lastIndex == 0))
            || ((!isEltSorted[i]) && (unsortedArray.getData(i) > subarrayAnim
                .getData(lastIndex - 1)))) {

          lang.nextStep();
          strandSourceCode.unhighlight(13);
          strandSourceCode.toggleHighlight(12, 14);
          subarray[lastIndex] = unsortedArray.getData(i);
          subarrayAnim.put(lastIndex, unsortedArray.getData(i), null, null);
          subarrayAnim.highlightElem(lastIndex, null, null);
          lang.nextStep();
          strandSourceCode.toggleHighlight(14, 15);
          isEltSortedAnim.put(i, "true", null, null);
          isEltSorted[i] = true;
          lang.nextStep();
          unsortedArray.unhighlightCell(i, null, null);
          isEltSortedAnim.unhighlightCell(i, null, null);
          subarrayAnim.unhighlightCell((lastIndex - 1), null, null);
          strandSourceCode.toggleHighlight(15, 16);
          counterSorted++;
          lang.nextStep();
          strandSourceCode.toggleHighlight(16, 17);
          lastIndex++;
          sublistMarker.move(lastIndex, null, null);
          lang.nextStep();
          strandSourceCode.toggleHighlight(17, 18);
          lang.nextStep();
          strandSourceCode.toggleHighlight(18, 19);
          lang.nextStep();
          strandSourceCode.toggleHighlight(19, 11);
        } else {
          lang.nextStep();
          unsortedArray.unhighlightCell(i, null, null);
          isEltSortedAnim.unhighlightCell(i, null, null);
          subarrayAnim.unhighlightCell((lastIndex - 1), null, null);
          strandSourceCode.unhighlight(13);
          strandSourceCode.toggleHighlight(12, 11);
        }

      }
      lang.nextStep("Merging the subarray with the results array");
      strandSourceCode.toggleHighlight(11, 20);
      // merge the sorted sublist-array with the sorted results-array
      results = mergeArray(results, subarray, lastIndex);
      for (int k = 0; k < lastIndex; k++)
        subarrayAnim.unhighlightElem(k, null, null);
      for (int p = 0; p < resultsLastIndex; p++) {
        resultsAnim.put(p, results[p], null, null);
        resultsAnim.highlightCell(p, null, null);
      }
      lang.nextStep();
      strandSourceCode.toggleHighlight(20, 21);
      lastIndex = 0;
      sublistMarker.move(lastIndex, null, null);
      lang.nextStep();
      strandSourceCode.toggleHighlight(21, 22);
      lang.nextStep();
      strandSourceCode.toggleHighlight(22, 10);
    }
    lang.nextStep();
    strandSourceCode.toggleHighlight(10, 23);
    lang.nextStep("Sorted Array");
    strandSourceCode.toggleHighlight(23, 24);
    lang.nextStep();
    strandSourceCode.unhighlight(24);
    iMarker.hide();
    sublistMarker.hide();
    isEltSortedMarker.hide();
    lang.finalizeGeneration();

  }

  static int[] mergeArray(int[] results, int[] sublist, int sublistLastIndex) {

    int[] res = new int[results.length];
    int k = 0, i = 0, j = 0;
    if (resultsLastIndex == 0) {
      int t = 0;
      for (t = 0; t < sublistLastIndex; t++)
        res[t] = sublist[t];
      resultsLastIndex = t;
    } else {
      while (k < resultsLastIndex + sublistLastIndex) {

        if (sublist[j] < results[i] && j < sublistLastIndex) {
          res[k] = sublist[j];
          k++;
          j++;
        } else {
          res[k] = results[i];
          k++;
          i++;
        }

      }
      resultsLastIndex = k;
    }
    return res;
  }

  public String getName() {
    return "Strand Sort [EN]";
  }

  public String getAlgorithmName() {
    return "Strand Sort";
  }

  public String getAnimationAuthor() {
    return "Iliya Gurov, Vladislava Arabadzhieva";
  }

  public String getDescription() {
    return "Strand sort is a sorting algorithm. <br/>"
        + "\n"
        + "The algorithm works by repeatedly pulling sorted sublists out of the list to be sorted and merging them with a result array. <br/>"
        + "\n"
        + "\n"
        + "Each iteration through the unsorted list pulls out a series of elements which were already sorted, and merges those series together. <br/>";
  }

  public String getCodeExample() {
    return "int[] sort(int[] array){"
        + "\n"
        + "	int len = array.length;"
        + "\n"
        + "	int counter sorted = 0, lastIndex = 0;"
        + "\n"
        + "	int[] subarray = new int[len];"
        + "\n"
        + "	boolean[] isEltSorted = new boolean[len];"
        + "\n"
        + " int[] sortedArray = new int[len];"
        + "\n"
        + "		for(int i = 0; i < len; i ++){"
        + "\n"
        + "			subarray [i] = Integer.MAX_VALUE;"
        + "\n"
        + "			isEltSorted [i] = false;"
        + "\n"
        + "		}"
        + "\n"
        + "		while (counterSorted < len){"
        + "\n"
        + "			for(int i = 0; i < len; i ++){"
        + "\n"
        + "				if( ( (!isEltSorted [i]) && (lastIndex == 0) ) || ( (!isEltSorted [i]) && (array [i] > subarray [lastIndex -1]))){"
        + "\n" + "					subarray[lastIndex] = array[i];" + "\n"
        + "					isEltSorted[i] = true;" + "\n" + "					counterSorted ++;"
        + "\n" + "					lastIndex ++;" + "\n" + "				}" + "\n" + "			}" + "\n"
        + "			sortedArray = mergeArray (sortedArray, subarray, lastIndex);"
        + "\n" + "			lastIndex = 0;" + "\n" + "		}" + "\n"
        + "	return sortedArray;" + "\n" + "}";
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

  /**
   * @param args
   */
  public static void main(String[] args) {
    int[] a = { 7, 4, 3, 9, 0, 1, 2, -5 };
    Hashtable<String, Object> ht = new Hashtable<String, Object>();
    ht.put("arrayToSort", a);
    // creating the object
    StrandSortGen sort = new StrandSortGen();
    System.out.println(sort.generate(null, ht));

  }
}
