package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

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
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

public class StoogeSortEN implements Generator {
  private Language             lang;
  private SourceCodeProperties sourceCode;
  private int[]                intArray;

  private int                  comparison  = 0;
  private int                  swaps       = 0;
  private int                  recDepth    = 0;
  private int                  currentLine = -1;
  private int                  selectedI   = -1;
  private int                  selectedJ   = -1;
  private ArrayMarker          iMarker;
  private ArrayMarker          jMarker;
  Text                         infoComparisons;
  Text                         infoSwaps;
  Text                         infoDepth;

  public void init() {
    lang = new AnimalScript("Stooge Sort", "Dimitar Goshev", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    intArray = (int[]) primitives.get("intArray");

    this.sort(intArray);
    return lang.toString();
  }

  public void sort(int[] arr) {
    // TITLE
    TextProperties titleProps = new TextProperties();
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 18));
    lang.newText(new Coordinates(10, 10), "Stooge Sort", "title", null,
        titleProps);

    lang.nextStep("Introduction");

    // Introduction
    SourceCodeProperties introProps = new SourceCodeProperties();
    introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    SourceCode intro = lang.newSourceCode(new Coordinates(10, 50),
        "sourceCode", null, introProps);
    intro.addCodeLine(
        "Stooge sort is a recursive sorting algorithm with a time complexity",
        null, 0, null);
    intro
        .addCodeLine("of O(nlog 3 / log 1.5 ) = O(n2.7095...).", null, 0, null);
    intro.addCodeLine("The algorithm is defined as follows:\n", null, 0, null);
    intro
        .addCodeLine(
            "* If the value at the end is smaller than the value at the start, swap them.",
            null, 0, null);
    intro
        .addCodeLine(
            "* If there are three or more elements in the current list subset, then:",
            null, 0, null);
    intro.addCodeLine("  - Stooge sort the initial 2/3 of the list", null, 0,
        null);
    intro.addCodeLine("  - Stooge sort the final 2/3 of the list", null, 0,
        null);
    intro.addCodeLine("  - Stooge sort the initial 2/3 of the list again",
        null, 0, null);

    lang.nextStep("Algorithm");
    intro.hide();

    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    IntArray intArr = lang.newIntArray(new Coordinates(20, 100), arr,
        "intArray", null, arrayProps);

    lang.nextStep();
    // STEP: Create the source code

    // Source code properties
    SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
        null, sourceCode);

    sc.addCodeLine("public static void stoogeSort( int[] arr, int i, int j )",
        null, 0, null);
    sc.addCodeLine("{", null, 0, null);
    sc.addCodeLine("if( arr[i] > arr[j] ){", null, 1, null);
    sc.addCodeLine("swap(arr, i, j);", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("if( j-i > 1 ){", null, 1, null);
    sc.addCodeLine("int t = ( j-i+1 )/3;", null, 2, null);
    sc.addCodeLine("stoogeSort( arr, i, j-t );", null, 2, null);
    sc.addCodeLine("stoogeSort( arr, i+t, j );", null, 2, null);
    sc.addCodeLine("stoogeSort( arr, i, j-t );", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    // SWAPS, COMPARISIONS
    TextProperties infoProps = new TextProperties();
    infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 12));
    infoComparisons = lang.newText(new Coordinates(300, 10), "Comparision: 0",
        "comparisons", null, infoProps);
    infoSwaps = lang.newText(new Coordinates(300, 30), "Swaps: 0", "swaps",
        null, infoProps);
    infoDepth = lang.newText(new Coordinates(300, 50), "Recursion Depth: 0",
        "depth", null, infoProps);

    lang.nextStep();

    stoogeSortReq(intArr, 0, intArr.getLength() - 1, sc);

    lang.nextStep("Conclusion");
    SourceCode conclusion = lang.newSourceCode(new Coordinates(10, 50),
        "sourceCode", null, introProps);
    conclusion.addCodeLine(
        "Stooge sort is much slower compared to efficient sorting", null, 0,
        null);
    conclusion.addCodeLine("algorithms such as Merge Sort and Quick Sort.",
        null, 0, null);
    iMarker.hide();
    jMarker.hide();
    intArr.unhighlightCell(selectedI, null, null);
    intArr.unhighlightCell(selectedJ, null, null);
    sc.hide();
  }

  private void stoogeSortReq(IntArray arr, int i, int j, SourceCode sc)
      throws LineNotExistsException {
    int iData = arr.getData(i);
    int jData = arr.getData(j);
    int t;

    if (selectedI != -1) {
      arr.unhighlightCell(selectedI, null, null);
      arr.unhighlightCell(selectedJ, null, null);
    } else {
      ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
      arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
      arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      iMarker = lang.newArrayMarker(arr, i, "i", null, arrayIMProps);

      ArrayMarkerProperties arrayJMProps = new ArrayMarkerProperties();
      arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
      arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      jMarker = lang.newArrayMarker(arr, j, "j", null, arrayJMProps);
    }

    // STEP: Highlight the 1st line
    sc.toggleHighlight(currentLine, 0, false, 0, 0);
    lang.nextStep();

    // STEP: if( arr[i] > arr[j] ){
    comparison++;
    infoComparisons.setText("Comparisions: " + comparison, new MsTiming(0),
        new MsTiming(200));

    sc.toggleHighlight(currentLine, 0, false, 2, 0);
    currentLine = 2;

    arr.highlightCell(i, new MsTiming(0), new MsTiming(200));
    iMarker.move(i, new MsTiming(0), new MsTiming(200));
    arr.highlightCell(j, new MsTiming(0), new MsTiming(200));
    jMarker.move(j, new MsTiming(0), new MsTiming(200));
    selectedI = i;
    selectedJ = j;
    lang.nextStep();

    // STEP: swap(arr, i, j);
    if (iData > jData) {
      swaps++;
      infoSwaps.setText("Swaps: " + swaps, new MsTiming(0), new MsTiming(200));

      sc.toggleHighlight(currentLine, 0, false, 3, 0);
      currentLine = 3;
      arr.swap(i, j, new MsTiming(0), new MsTiming(200));
      lang.nextStep();
    }

    // STEP: if( j-i > 1 ){
    sc.toggleHighlight(currentLine, 0, false, 5, 0);
    currentLine = 5;
    lang.nextStep();

    if (j - i > 1) {
      // STEP: int t = ( j-i+1 )/3;
      t = (j - i + 1) / 3;
      sc.toggleHighlight(currentLine, 0, false, 6, 0);
      currentLine = 6;
      lang.nextStep();

      // STEP: stoogeSort( arr, i, j-t );
      sc.toggleHighlight(currentLine, 0, false, 7, 0);
      currentLine = 7;
      lang.nextStep();
      recDepth++;
      infoDepth.setText("Recusrsion Depth: " + recDepth, new MsTiming(0),
          new MsTiming(200));
      stoogeSortReq(arr, i, j - t, sc);
      recDepth--;
      infoDepth.setText("Recusrsion Depth: " + recDepth, new MsTiming(0),
          new MsTiming(200));

      // STEP: stoogeSort( arr, i+t, j );
      sc.toggleHighlight(currentLine, 0, false, 8, 0);
      currentLine = 8;
      lang.nextStep();
      recDepth++;
      infoDepth.setText("Recusrsion Depth: " + recDepth, new MsTiming(0),
          new MsTiming(200));
      stoogeSortReq(arr, i + t, j, sc);
      recDepth--;
      infoDepth.setText("Recusrsion Depth: " + recDepth, new MsTiming(0),
          new MsTiming(200));

      // STEP: stoogeSort( arr, i, j-t );
      sc.toggleHighlight(currentLine, 0, false, 9, 0);
      currentLine = 9;
      lang.nextStep();
      recDepth++;
      infoDepth.setText("Recusrsion Depth: " + recDepth, new MsTiming(0),
          new MsTiming(200));
      stoogeSortReq(arr, i, j - t, sc);
      recDepth--;
      infoDepth.setText("Recusrsion Depth: " + recDepth, new MsTiming(0),
          new MsTiming(200));
    }
  }

  public String getName() {
    return "Stooge Sort";
  }

  public String getAlgorithmName() {
    return "Stooge Sort";
  }

  public String getAnimationAuthor() {
    return "Dimitar Goshev";
  }

  public String getDescription() {
    return "Stooge sort is a recursive sorting algorithm with a time complexity"
        + "\n"
        + "of O(nlog 3 / log 1.5 ) = O(n2.7095...)."
        + "\n"
        + "The algorithm is defined as follows:"
        + "\n"
        + "  * If the value at the end is smaller than the value at the start, swap them."
        + "\n"
        + "  * If there are three or more elements in the current list subset, then:"
        + "\n"
        + "    - Stooge sort the initial 2/3 of the list"
        + "\n"
        + "    - Stooge sort the final 2/3 of the list"
        + "\n"
        + "    - Stooge sort the initial 2/3 of the list again"
        + "\n"
        + "  "
        + "\n" + "http://en.wikipedia.org/wiki/Stooge_sort";
  }

  public String getCodeExample() {
    return "public static void stoogeSort( int[] arr, int i, int j )" + "\n"
        + "{" + "\n" + "  if( arr[i] > arr[j] ){\", null, 1, null);" + "\n"
        + "    swap(arr, i, j);\", null, 2, null);" + "\n" + "  }" + "\n"
        + "  if( j-i > 1 ){" + "\n" + "    int t = ( j-i+1 )/3;" + "\n"
        + "    stoogeSort( arr, i, j-t );" + "\n"
        + "    stoogeSort( arr, i+t, j );" + "\n"
        + "    stoogeSort( arr, i, j-t );" + "\n" + "  }" + "\n" + "}";
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