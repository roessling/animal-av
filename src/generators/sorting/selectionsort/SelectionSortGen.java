/**
 * @author Iliya Gurov and Vladislava Arabadzhieva
 */
package generators.sorting.selectionsort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class SelectionSortGen implements Generator {
  private Language                     lang;
  private int[]                        unsortedArray;
  private ArrayMarker                  jMarker;
  private ArrayMarker                  arrayMarker;
  private ArrayMarker                  minMarker;

  private SourceCodeProperties         selectionSortProps;
  private ArrayMarkerProperties        arrayMarkerProps;
  private ArrayMarkerProperties        minMarkerProps;
  private ArrayProperties              arrayProps;
  private TextProperties               textProps, minTextProps, headerProps;

  private IntArray                     arrayAnim;
  private Text                         endText2, endText1, minIndexText,
      currentMinText, endText;
  private Text                         comparisonsText, assignmentsText;
  private TextProperties               textStaticProps, textDynamicProps;

  private SourceCode                   selectionSortSourceCode,
      findMinSourceCode, swapSourceCode;

  private AnimationPropertiesContainer container;
  // private MultipleChoiceQuestionModel mcQuestionModel;
  private int                          comparisons = 0;
  private int                          assignments = 0;
  private boolean                      mcAsked     = false;

  public SelectionSortGen() {

  }

  private void initProps() {

    selectionSortProps = new SourceCodeProperties();
    arrayMarkerProps = new ArrayMarkerProperties();
    minMarkerProps = new ArrayMarkerProperties();
    arrayProps = new ArrayProperties();
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    minTextProps = new TextProperties();
    minTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.BOLD, 30));
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.DARK_GRAY);

    textStaticProps = new TextProperties();
    textDynamicProps = new TextProperties();
    textDynamicProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
  }

  public void init() {
    lang = new AnimalScript("Selection Sort [EN]",
        "Iliya Gurov, Vladislava Arabadzhieva", 800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

  }

  // -----------------------------------------------------SET PROPS ...from
  // container
  private void setSourceCodeProps() {
    this.selectionSortProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        container.get("source", AnimationPropertiesKeys.COLOR_PROPERTY));
    this.selectionSortProps.set(
        AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, container.get(
            "source", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
  }

  private void setArrayMarkerProps() {
    minMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        container.get("minMarker", AnimationPropertiesKeys.LABEL_PROPERTY));
    minMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        container.get("minMarker", AnimationPropertiesKeys.COLOR_PROPERTY));

  }

  private void setMinMarkerProps() {// setMinMarkerProps
    arrayMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        container.get("arrayMarker", AnimationPropertiesKeys.LABEL_PROPERTY));
    arrayMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        container.get("arrayMarker", AnimationPropertiesKeys.COLOR_PROPERTY));
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

  private void setDefaultSourceCodeProperties() {
    selectionSortProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    selectionSortProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.red);
  }

  private void setDefaultArrayMarkerProps() {
    minMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
    minMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
  }

  private void setDefaultMinMarkerProps() {
    arrayMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    arrayMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
  }

  private void setDefaultArrayProps() {
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.blue);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.yellow);
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
      setDefaultMinMarkerProps();
      setDefaultArrayProps();
    } else {
      if (container.getPropertiesByName("source") != null)
        setSourceCodeProps();
      else
        setDefaultSourceCodeProperties();
      if (container.getPropertiesByName("array") != null)
        setArrayProps();
      else
        setDefaultArrayProps();
      if (container.getPropertiesByName("minMarker") != null)
        setArrayMarkerProps();
      else
        setDefaultArrayMarkerProps();
      if (container.getPropertiesByName("arrayMarker") != null)
        setMinMarkerProps();
      else
        setDefaultMinMarkerProps();
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
    lang.nextStep("Introduction - Idea");
    lang.newText(new Coordinates(10, 2), "SELECTION SORT", "header", null);
    lang.newText(
        new Coordinates(2, 30),
        "Selection sort is an in place comparison sort.  It is noted for its simplicity.  ",
        "initialText", null);
    lang.newText(
        new Offset(0, 5, "initialText", "SW"),
        " An analogy to the famous 'Hand of cards' can be drawn, where we look over all the cards,  ",
        "initialText1", null);
    lang.newText(
        new Offset(0, 5, "initialText1", "SW"),
        "select the lowest one and place it where it belongs. Then we select the second lowest, etc.",
        "initialText2", null);

    lang.newText(new Offset(0, 5, "initialText2", "SW"),
        "The idea of the algorithm is given below:", "idea", null);
    lang.newText(new Offset(5, 5, "idea", "SW"),
        "1. Find the minimum value in the array:", "row1", null);
    lang.newText(new Offset(0, 5, "row1", "SW"),
        "2. Swap it with the value in the first position", "row2", null);
    lang.newText(new Offset(0, 5, "row2", "SW"),
        "3. Repeat the steps above for the remainder of the array", "row3",
        null);
    // next step - add source code
    lang.nextStep("Introduction - Source Code");

    // selection sort - animate source code
    selectionSortSourceCode = lang.newSourceCode(
        new Offset(0, 30, "row3", "SW"), "code", null, selectionSortProps);
    selectionSortSourceCode.addCodeLine("int[] selectionSort()", null, 0, null); // 0
    selectionSortSourceCode.addCodeLine(
        "for (int i = 0; i < array.length; i ++) {", null, 2, null); // 1
    selectionSortSourceCode
        .addCodeLine("int min = findMin (i);", null, 3, null); // 2
    selectionSortSourceCode.addCodeLine("swap (min, i);", null, 3, null); // 3
    selectionSortSourceCode.addCodeLine("}", null, 2, null); // 4
    selectionSortSourceCode.addCodeLine("}", null, 0, null); // 5

    // findMind - animate source code
    findMinSourceCode = lang.newSourceCode(new Offset(0, 15, "code", "SW"),
        "codeFindMin", null, selectionSortProps);
    findMinSourceCode.addCodeLine("int findMin (int i) {", null, 0, null); // 0
    findMinSourceCode.addCodeLine("int minIndex = i;", null, 1, null); // 1
    findMinSourceCode.addCodeLine("for(int j = i+1; j < array.length; j ++) {",
        null, 3, null); // 2
    findMinSourceCode.addCodeLine("if (array[j] < array[minIndex]) {", null, 4,
        null); // 3
    findMinSourceCode.addCodeLine("minIndex = j;", null, 5, null); // 4
    findMinSourceCode.addCodeLine("}", null, 4, null); // 5
    findMinSourceCode.addCodeLine("}", null, 3, null); // 6
    findMinSourceCode.addCodeLine("return minIndex;", null, 1, null); // 7
    findMinSourceCode.addCodeLine("}", null, 0, null); // 8

    // next step - add the array
    lang.nextStep();
    arrayAnim = lang.newIntArray(new Offset(70, 50, "code", "NE"),
        unsortedArray, "unsortedArray", null, arrayProps);
    lang.newText(new Offset(0, 50, "unsortedArray", "SW"), "min index:",
        "minIndexText", null, new TextProperties());
    minIndexText = lang.newText(new Offset(5, 0, "minIndexText", "NE"), "",
        "minText", null, minTextProps);
    minIndexText.hide();
    lang.newText(new Offset(0, 10, "minIndexText", "SW"), "current min:",
        "currentMinText", null, new TextProperties());
    currentMinText = lang.newText(new Offset(5, 0, "currentMinText", "NE"), "",
        "currentMin", null, textProps);
    currentMinText.hide();

    lang.newText(new Offset(0, 25, "currentMinText", "SW"),
        "Number of comparisons in findMin: n*(n-1)", "staticT", null,
        textStaticProps);
    lang.newText(new Offset(0, 25, "staticT", "SW"),
        "Number of comparisons in findMin:", "comparisonsStatic", null,
        textStaticProps);
    comparisonsText = lang.newText(new Offset(5, 0, "comparisonsStatic", "NE"),
        "", "comparisonsDynamic", null, textDynamicProps);
    comparisonsText.hide();
    lang.newText(new Offset(0, 25, "comparisonsStatic", "SW"),
        "Number of assignments in findMin:", "assignmentsStatic", null,
        textStaticProps);
    assignmentsText = lang.newText(new Offset(5, 0, "assignmentsStatic", "NE"),
        "", "assignmentsDynamic", null, textDynamicProps);
    assignmentsText.hide();

    endText = lang
        .newText(
            new Offset(0, 40, "codeFindMin", "SW"),
            "Selection sort has O(n^2) time complexity which makes it inefficient on large lists. ",
            "endText", null, textStaticProps);
    endText.hide();
    endText1 = lang
        .newText(
            new Offset(0, 5, "endText", "SW"),
            "In general it performs worse than the similar Insertion sort. However, it also has performance advantages",
            "endText1", null, textStaticProps);
    endText1.hide();
    endText2 = lang
        .newText(
            new Offset(0, 5, "endText1", "SW"),
            "over more complicated algorithms in certain situations, particularly when auxiliary memory is limited.",
            "endText2", null, textStaticProps);
    endText2.hide();
    selectionSort(selectionSortSourceCode, findMinSourceCode, swapSourceCode,
        arrayAnim);

  }

  /**
   * Visualization and actual sorting algorithm
   * 
   * @param selectionSortSourceCode
   *          - animated source code for selection sort
   * @param findMinSourceCode
   *          - animated source code for finding the index of the min element
   * @param arrayAnim
   *          - the unsorted animated array
   */
  private void selectionSort(SourceCode selectionSortSourceCode,
      SourceCode findMinSourceCode, SourceCode swapSourceCode,
      IntArray arrayAnim) {
    int min = 0;

    // next step -highlight the name of the function
    lang.nextStep();
    selectionSortSourceCode.highlight(0);

    // next step - highlight code and add a pointer
    lang.nextStep();
    selectionSortSourceCode.toggleHighlight(0, 1);
    arrayMarker = lang
        .newArrayMarker(arrayAnim, 0, "i", null, arrayMarkerProps);
    jMarker = lang.newArrayMarker(arrayAnim, 0, "j", null, minMarkerProps);
    minMarker = lang.newArrayMarker(arrayAnim, 0, "minIndex", null,
        minMarkerProps);
    jMarker.hide();
    minMarker.hide();

    TrueFalseQuestionModel tfquestionModel = new TrueFalseQuestionModel("TF",
        false, 1);
    tfquestionModel
        .setPrompt("At each iteration we substitute the first element of the subarray with the subsequent element of the subarray. Is it true or false?");
    tfquestionModel.setFeedbackForAnswer(true, "incorrect");
    tfquestionModel.setFeedbackForAnswer(false, "correct");
    tfquestionModel.setCorrectAnswer(false);

    lang.addTFQuestion(tfquestionModel);

    for (int i = 0; i < arrayAnim.getLength(); i++) {
      arrayMarker.move(i, null, null);
      arrayAnim.highlightElem(i, null, null);
      arrayAnim.highlightCell(i, null, null);

      // next step - highlight code and use findMin
      lang.nextStep();
      selectionSortSourceCode.toggleHighlight(1, 2);
      findMinSourceCode.highlight(0);

      min = findMin(i, findMinSourceCode);
      arrayAnim.highlightCell(min, null, null);

      if (!mcAsked) {
        MultipleChoiceQuestionModel mcquestionModel = new MultipleChoiceQuestionModel(
            "MC");
        mcquestionModel
            .setPrompt("The elements at which positions do we substitute now?");
        mcquestionModel
            .addAnswer(
                "The element at position " + ((Integer) i).toString()
                    + " with the element at position "
                    + ((Integer) min).toString(), 1, "correct");
        mcquestionModel.addAnswer(
            "The element at position " + ((Integer) i).toString()
                + " with the element at position "
                + ((Integer) (min + 1)).toString(), 0, "incorrect");
        mcquestionModel
            .addAnswer(
                "The element at position " + ((Integer) i).toString()
                    + " with the element at position "
                    + ((Integer) (i)).toString(), 0, "incorrect");
        mcquestionModel.setNumberOfTries(2);
        lang.addMCQuestion(mcquestionModel);
        mcAsked = true;
      }

      // next step - swap
      lang.nextStep("Swap elements");
      findMinSourceCode.unhighlight(8);
      selectionSortSourceCode.toggleHighlight(2, 3);
      arrayAnim.swap(min, i, null, null);

      // next step - highlight }
      lang.nextStep();
      selectionSortSourceCode.toggleHighlight(3, 4);

      // next step - mark the i-th element as sorted
      lang.nextStep();
      selectionSortSourceCode.toggleHighlight(4, 1);
      arrayAnim.unhighlightCell(i, null, null);
      arrayAnim.unhighlightCell(min, null, null);
      arrayAnim.highlightElem(i, null, null);

    }
    lang.nextStep("The array is sorted");
    selectionSortSourceCode.toggleHighlight(1, 5);
    arrayMarker.hide();
    lang.nextStep();
    selectionSortSourceCode.unhighlight(5);
    endText.show();
    endText1.show();
    endText2.show();
    lang.finalizeGeneration();

  }

  /**
   * Finds the index of the min element in the array from a given position on.
   * 
   * @param index
   *          - shows from which index on do we search
   * @param findMinSourceCode
   *          - the visualized source code for finding min index
   * @return
   */
  private int findMin(int index, SourceCode findMinSourceCode) {
    int minIndex = index;

    // next step - add text fields for the values of minIndex and min
    lang.nextStep();
    findMinSourceCode.toggleHighlight(0, 1);

    minIndexText.setText(((Integer) minIndex).toString(), null, null);
    minIndexText.show();
    currentMinText.setText(((Integer) arrayAnim.getData(minIndex)).toString(),
        null, null);
    currentMinText.show();
    assignments++;
    assignmentsText.setText(((Integer) assignments).toString(), null, null);
    assignmentsText.show();

    // next step
    lang.nextStep();
    findMinSourceCode.toggleHighlight(1, 2);

    for (int j = index + 1; j < arrayAnim.getLength(); j++) {
      comparisons++;
      assignments++;
      comparisonsText.setText(((Integer) comparisons).toString(), null, null);
      comparisonsText.show();
      assignmentsText.setText(((Integer) assignments).toString(), null, null);
      assignmentsText.show();

      minMarker.show();
      minMarker.move(j, null, null);
      jMarker.show();
      jMarker.changeColor("color", Color.blue, null, null);
      jMarker.move(j, null, null);

      // next step
      lang.nextStep();
      findMinSourceCode.toggleHighlight(2, 3);
      arrayAnim.highlightElem(j, null, null);
      comparisons++;
      comparisonsText.setText(((Integer) comparisons).toString(), null, null);
      comparisonsText.show();
      if (arrayAnim.getData(j) < arrayAnim.getData(minIndex)) {

        // next step
        lang.nextStep();
        findMinSourceCode.toggleHighlight(3, 4);
        arrayAnim.unhighlightElem(minIndex, null, null);
        minIndex = j;
        assignments++;
        assignmentsText.setText(((Integer) assignments).toString(), null, null);
        assignmentsText.show();

        minIndexText.setText(((Integer) minIndex).toString(), null, null);
        currentMinText.setText(
            ((Integer) arrayAnim.getData(minIndex)).toString(), null, null);

        // next steps - highlight code
        lang.nextStep();
        findMinSourceCode.toggleHighlight(4, 5);
        lang.nextStep();
        findMinSourceCode.toggleHighlight(5, 6);
        lang.nextStep();
        findMinSourceCode.toggleHighlight(6, 2);
      } else {
        lang.nextStep();
        findMinSourceCode.toggleHighlight(3, 5);
        lang.nextStep();
        findMinSourceCode.toggleHighlight(5, 6);
        lang.nextStep();
        findMinSourceCode.toggleHighlight(6, 2);
        arrayAnim.unhighlightElem(j, null, null);
      }

    }
    // next step
    lang.nextStep();
    findMinSourceCode.toggleHighlight(2, 7);
    minIndexText.changeColor("color", Color.red, null, null);
    jMarker.moveOutside(null, null);
    jMarker.changeColor("color", Color.white, null, null);

    // next step
    lang.nextStep();
    findMinSourceCode.toggleHighlight(7, 8);

    // next step
    lang.nextStep();
    findMinSourceCode.unhighlight(8);
    minIndexText.changeColor("color", Color.blue, null, null);

    return minIndex;

  }

  public String getName() {
    return "Selection Sort [EN]";
  }

  public String getAlgorithmName() {
    return "Selection Sort";
  }

  public String getAnimationAuthor() {
    return "Iliya Gurov, Vladislava Arabadzhieva";
  }

  public String getDescription() {
    return "Selection sort is a sorting algorithm, specifically an in-place comparison sort. <br/>"
        + "\n"
        + "The algorithm works as follows: <br/>"
        + "\n"
        + "\n"
        + "1.Find the minimum value in the array <br/>"
        + "\n"
        + "2.Swap it with the value in the first position <br/>"
        + "\n"
        + "3.Repeat the steps above for the remainder of the array (starting at the second position"
        + "\n" + " and advancing each time) <br/>";
  }

  public String getCodeExample() {
    return "int[] selectionSort(){" + "\n"
        + "	for(int i = 0; i < array.length; i ++){" + "\n"
        + "		int min = findMin(i);" + "\n" + "		swap(min, i);" + "\n" + "	 }"
        + "\n" + "}" + "\n" + "\n" + "int findMin(int i){" + "\n"
        + "	int minIndex = i;" + "\n"
        + "	for(int j = i+1; j < array.length; j ++){" + "\n"
        + "		if (array[j] < array[minIndex]){" + "\n" + "			minIndex = j;"
        + "\n" + "		}" + "\n" + "	}" + "\n" + "	return minIndex;" + "\n" + "}";
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

}
