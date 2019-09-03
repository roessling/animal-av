package generators.sorting.bubblesort;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import animal.variables.Variable;
import animal.variables.VariableRoles;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.TrueFalseQuestionModel;

public class BubbleSort implements Generator {
  private Language              lang;
  private SourceCodeProperties  sourceCodeProperties;
  private SourceCodeProperties  descProperties;
  private TextProperties        textPropertiesTitle;
  private TextProperties        steps;
  private ArrayProperties       arrayProperties;
  private ArrayMarkerProperties jArrayMarker;
  private ArrayMarkerProperties iArrayMarker;
  private RectProperties        stepsFrames;
  private RectProperties        titleFrame;
  private RectProperties        informationFrame;
  private int[]                 array;
  SourceCode                    sourceCode;
  SourceCode                    description;
  Variables                     v;

  public void init() {
    lang = new AnimalScript("Bubble Sort 2.0[EN]",
        "Tanya Harizanova,Dimitar Dimitrov", 800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    descProperties = (SourceCodeProperties) props
        .getPropertiesByName("description");
    textPropertiesTitle = (TextProperties) props
        .getPropertiesByName("textPropertiesTitle");
    arrayProperties = (ArrayProperties) props
        .getPropertiesByName("_arrayProperties");
    jArrayMarker = (ArrayMarkerProperties) props
        .getPropertiesByName("_arrayMarker_j");
    iArrayMarker = (ArrayMarkerProperties) props
        .getPropertiesByName("_arrayMarker_i");
    steps = (TextProperties) props.getPropertiesByName("result");
    titleFrame = (RectProperties) props.getPropertiesByName("titleFrame");
    informationFrame = (RectProperties) props
        .getPropertiesByName("informationFrame");
    stepsFrames = (RectProperties) props.getPropertiesByName("stepsFrames");
    array = (int[]) primitives.get("array");
    displayAlgorithmDescription();
    lang.nextStep("Introduction");
    bubbleSorting(array);

    displaylastPage();
    lang.nextStep("Conclusion");
    return lang.toString();
  }

  public Text displayTitle() {
    TextProperties titleP = textPropertiesTitle;
    Text title = lang.newText(new Coordinates(150, 30), "BubbleSort", "title",
        null, titleP);
    lang.newRect(new Offset(-5, -5, title, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, title, AnimalScript.DIRECTION_SE), "frame1", null,
        titleFrame);
    return title;
  }

  /**
   * this method creates a source code and displays it;
   */
  public SourceCode displaySourceCode() {
    SourceCodeProperties scp = sourceCodeProperties;
    sourceCode = lang.newSourceCode(new Coordinates(10, 200), "sourceCode",
        null, scp);

    sourceCode.addCodeLine("If isSorted = true", null, 0, null);
    sourceCode.addCodeLine(" terminate the algorithm", null, 3, null);
    sourceCode.addCodeLine(" otherwise set isSorted = true ", null, 3, null);
    sourceCode.addCodeLine(
        "Compare the element at position i with element at position j,", null,
        0, null);
    sourceCode.addCodeLine(
        "element at position i is greater then element at position j,", null,
        0, null);
    sourceCode.addCodeLine(" then switch elements,and set isSorted=false",
        null, 3, null);
    sourceCode.addCodeLine(" otherwise do nothing.", null, 3, null);
    sourceCode.addCodeLine("if i position is last position of the list", null,
        0, null);
    sourceCode.addCodeLine(
        " then start from the beginn of the list ,the same all over again",
        null, 3, null);
    sourceCode.addCodeLine(
        " otherwise go to position i + 1 and j + 1 and compare it again..",
        null, 3, null);

    return sourceCode;
  }

  /**
   * this method creates an algorithm description and displays it;
   */
  public SourceCode displayAlgorithmDescription() {
    displayTitle();
    SourceCodeProperties scp = descProperties;

    description = lang.newSourceCode(new Coordinates(10, 110), "description",
        null, scp);

    description.addCodeLine("Information about this algorithm:", null, 0, null);
    description
        .addCodeLine(
            "Bubble sort, sometimes incorrectly referred to as sinking sort, is a simple sorting algorithm  ",
            null, 0, null);
    description.addCodeLine(
        "that works by repeatedly stepping through the list to be sorted,",
        null, 0, null);
    description.addCodeLine(
        "comparing each pair of adjacent items and swapping them  ", null, 0,
        null);
    description
        .addCodeLine(
            "if they are in the wrong order. The pass through the list is repeated",
            null, 0, null);
    description.addCodeLine(
        "until no swaps are needed, which indicates that the list is sorted.",
        null, 0, null);
    description
        .addCodeLine(
            "The algorithm gets its name from the way smaller elements bubble to the top of the list.",
            null, 0, null);
    description.addCodeLine(
        "Because it only uses comparisons to operate on elements, ", null, 0,
        null);
    description.addCodeLine(
        "it is a comparison sort. Although the algorithm is simple, ", null, 0,
        null);
    description.addCodeLine(
        "some other algorithms are more efficient for sorting large lists.",
        null, 0, null);

    lang.newRect(new Offset(-5, -5, description, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, description, AnimalScript.DIRECTION_SE), "frame1",
        null, informationFrame);
    return description;
  }

  /**
   * @param toBeSorted
   *          this method sorts an array - toBeSort
   */
  public void bubbleSorting(int[] toBeSorted) {
    int repeats = 0;
    int comparisons = 0;
    int swaps = 0;
    lang.hideAllPrimitives();
    displayTitle();
    displaySourceCode();
    IntArray intArray = lang.newIntArray(new Coordinates(30, 160), toBeSorted,
        "array", null, arrayProperties);

    lang.nextStep();

    ArrayMarker i_Marker = lang.newArrayMarker(intArray, 0, "i", null,
        iArrayMarker);
    ArrayMarker j_Marker = lang.newArrayMarker(intArray,
        i_Marker.getPosition() + 1, "j", null, jArrayMarker);
    boolean isSorted = false;
    v = lang.newVariables();
    showVariablen("isSorted", "" + isSorted + "  in step (" + lang.getStep()
        + ")");
    lang.nextStep();

    while (true) {

      int iteraction = repeats + 1;
      lang.nextStep(iteraction + ". Interaction ");
      if (isSorted) {
        sourceCode.unhighlight(4);
        sourceCode.highlight(0);
        lang.nextStep();
        sourceCode.unhighlight(0);
        sourceCode.highlight(1);
        displaySteps(comparisons, swaps, repeats);
        lang.nextStep("Show Result");
        return;
      } else {
        sourceCode.highlight(0);
        lang.nextStep();
        sourceCode.unhighlight(0);
        sourceCode.highlight(2);
        isSorted = true;
        showVariablen("isSorted",
            "" + isSorted + "  in step (" + lang.getStep() + ")");
        lang.nextStep();
        sourceCode.unhighlight(2);
        sourceCode.highlight(3);
        lang.nextStep();
      }

      repeats++;
      int i = 0;
      showVariablen("repeats", "" + iteraction + "  in step (" + lang.getStep()
          + ")");
      for (i_Marker.move(0, null, new TicksTiming(40)); i < toBeSorted.length - 1; i_Marker
          .move(i, null, new TicksTiming(40))) {
        int newJPosition = i_Marker.getPosition() + 1;
        j_Marker.move(newJPosition, null, new TicksTiming(40));

        sourceCode.unhighlight(3);
        sourceCode.unhighlight(9);
        sourceCode.highlight(4);
        lang.nextStep(iteraction + "." + i + " Comparisson");

        showVariablen("comparisson",
            "" + comparisons + "  in step (" + lang.getStep() + ")");

        if (toBeSorted[j_Marker.getPosition()] < toBeSorted[i_Marker
            .getPosition()]) {

          sourceCode.unhighlight(4);
          sourceCode.highlight(5);
          intArray.swap(i_Marker.getPosition(), j_Marker.getPosition(), null,
              new TicksTiming(100));
          lang.nextStep(iteraction + "." + i + "." + swaps + " Swap");
          isSorted = false;
          showVariablen("isSorted",
              "" + isSorted + "  in step (" + lang.getStep() + ")");
          showVariablen("Swaps", "" + swaps + "  in step (" + lang.getStep()
              + ")");
          swaps++;
        } else {

          sourceCode.unhighlight(4);
          sourceCode.highlight(6);
          lang.nextStep();
        }
        comparisons++;
        sourceCode.unhighlight(5);
        sourceCode.unhighlight(6);
        sourceCode.highlight(7);
        lang.nextStep();
        sourceCode.unhighlight(7);
        sourceCode.highlight(9);
        lang.nextStep();
        i++;
      }

      sourceCode.unhighlight(9);
      sourceCode.highlight(8);
      lang.nextStep();
      sourceCode.unhighlight(8);
    }

  }

  /**
   * @param comparisons
   * @param swaps
   * @param repeats
   *          this method resolves the result of sorting and displays it;
   */
  private void displaySteps(int comparisons, int swaps, int repeats) {

    TextProperties tp = steps;
    Text repeat = lang.newText(new Coordinates(15, 460),
        "Repeats : " + repeats, "repeats", null, tp);
    Text comparissons = lang.newText(new Coordinates(145, 464),
        "Comparisons : " + comparisons, "comparisons", null, tp);
    Text swap = lang.newText(new Coordinates(315, 464), "Swaps : " + swaps,
        "swaps", null, tp);

    lang.newRect(new Offset(-5, -5, repeat, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, repeat, AnimalScript.DIRECTION_SE), "frame1", null,
        stepsFrames);
    lang.newRect(new Offset(-5, -5, comparissons, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, comparissons, AnimalScript.DIRECTION_SE), "frame2",
        null, stepsFrames);
    lang.newRect(new Offset(-5, -5, swap, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, swap, AnimalScript.DIRECTION_SE), "frame3", null,
        stepsFrames);
  }

  public void displaylastPage() {

    lang.hideAllPrimitives();
    SourceCodeProperties scp = descProperties;

    displayTitle();
    description = lang.newSourceCode(new Coordinates(10, 50), "description",
        null, scp);

    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("Run time :", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine(
        "Bubble sort has worst-case and average complexity both O(n^2), ",
        null, 0, null);
    description.addCodeLine("where n is the number of items being sorted.",
        null, 0, null);
    description.addCodeLine(
        "Therefore,it is recomended to use another faster algorithm to ", null,
        0, null);
    description.addCodeLine("sort a list efficiently.", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("Sources:", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("http://de.wikipedia.org/wiki/Bubblesort", null, 0,
        null);

  }

  public void showQuestion(String question, boolean answer, String correct,
      String incorrect) {

    TrueFalseQuestionModel q = new TrueFalseQuestionModel("ID");
    q.setPrompt(question);
    q.setFeedbackForAnswer(true, correct);
    q.setFeedbackForAnswer(false, incorrect);
    q.setCorrectAnswer(answer);
    lang.addTFQuestion(q);

  }

  public void showVariablen(String key, String value) {
    v.declare("STRING", key, value, Variable.getRoleString(VariableRoles.STEPPER));
//    v.setRole(key, "" + VariableRoles.STEPPER);
  }

  public String getName() {
    return "Bubble Sort 2.0[EN]";
  }

  public String getAlgorithmName() {
    return "Bubble Sort";
  }

  public String getAnimationAuthor() {
    return "Tanya Harizanova, Dimitar Dimitrov";
  }

  public String getDescription() {
    return " Bubble sort, sometimes incorrectly referred to as sinking sort, is a simple sorting algorithm "
        + "\n"
        + " that works by repeatedly stepping through the list to be sorted,"
        + "\n"
        + " comparing each pair of adjacent items and swapping them "
        + "\n"
        + " if they are in the wrong order. The pass through the list is repeated"
        + "\n"
        + " until no swaps are needed, which indicates that the list is sorted."
        + "\n"
        + " The algorithm gets its name from the way smaller elements bubble to the top of the list."
        + "\n"
        + " Because it only uses comparisons to operate on elements,"
        + "\n"
        + " it is a comparison sort. Although the algorithm is simple, "
        + "\n"
        + " some other algorithms are more efficient for sorting large lists."
        + "\n"
        + " Run time :\""
        + "\n"
        + " Bubble sort has worst-case and average complexity both O(n^2),"
        + "\n" + " where n is the number of items being sorted.";
  }

  public String getCodeExample() {
    return "Compare the element at position i with element at position j,"
        + "\n"
        + " if element at position i is greater then element at position j,"
        + "\n"
        + "     then switch elements,"
        + "\n"
        + "		otherwise do nothing."
        + "\n"
        + "if i position is last position of the list"
        + "      then start from the beginn of the list ,the same all over again"
        + "\n"
        + "		otherwise go to position i + 1 and j + 1 and compare it again..";

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
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}