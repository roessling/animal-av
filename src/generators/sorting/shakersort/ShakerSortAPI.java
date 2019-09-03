package generators.sorting.shakersort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

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
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class ShakerSortAPI implements Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language              lang;

  // Source Code
  private SourceCode            sc;

  private SourceCodeProperties  scProps;
  // Array markers
  private ArrayMarkerProperties lMarkerProps, rMarkerProps, uMarkerProps,
      dMarkerProps;

  ArrayProperties               arrayProps;

  private IntArray              intArray;

  // default timing
  private Timing                defaultTiming;

  // Text objects as counter for Iterations, Comparisons and swaps
  private Text                  nrIt, nrCo, nrSw;

  private Text                  swappedTF;

  public ShakerSortAPI() {

  }

  private void internInit(int[] array) {
    defaultTiming = new TicksTiming(15);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    // Rect box =
    lang.newRect(new Coordinates(10, 10), new Coordinates(200, 60), "box",
        null, rectProps);

    TextProperties textProps1 = new TextProperties();
    textProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 30));
    // Text header =
    lang.newText(new Coordinates(20, 30), "Shaker Sort", "header", null,
        textProps1);

    intArray = lang.newIntArray(new Coordinates(20, 130), array, "intArray",
        null, arrayProps);

    Text infoIt = lang.newText(new Offset(100, -60, intArray,
        AnimalScript.DIRECTION_SE), "Iterations: ", "Iteration", null,
        textProps1);
    Text infoCo = lang.newText(new Offset(0, 5, infoIt,
        AnimalScript.DIRECTION_SW), "Array Comparisons: ", "Comparison", null,
        textProps1);
    Text infoSw = lang.newText(new Offset(0, 5, infoCo,
        AnimalScript.DIRECTION_SW), "Swaps: ", "Swaps", null, textProps1);

    TextProperties infoProps = new TextProperties();
    infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 30));
    infoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    nrIt = lang.newText(new Offset(10, 0, infoIt, AnimalScript.DIRECTION_NE),
        "0", "#Iterations", null, infoProps);
    nrCo = lang.newText(new Offset(10, 0, infoCo, AnimalScript.DIRECTION_NE),
        "0", "#Comparisons", null, infoProps);
    nrSw = lang.newText(new Offset(10, 0, infoSw, AnimalScript.DIRECTION_NE),
        "0", "#swaps", null, infoProps);

    TextProperties textProps2 = new TextProperties();
    textProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));
    textProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    swappedTF = lang.newText(new Coordinates(20, 160), " ", "lblTF", null,
        textProps2);

    lMarkerProps = new ArrayMarkerProperties();
    lMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "L");
    lMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);

    rMarkerProps = new ArrayMarkerProperties();
    rMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "R");
    rMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);

    uMarkerProps = new ArrayMarkerProperties();
    uMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "UP");
    uMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    dMarkerProps = new ArrayMarkerProperties();
    dMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "DOWN");
    dMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
  }

  private void showSourceCode() {
    sc = lang.newSourceCode(new Coordinates(20, 200), "sourceCode", null,
        scProps);

    sc.addCodeLine("public void shakerSort(int[] intArray) {", null, 0, null); // 0
    sc.addCodeLine("boolean swapped;", null, 2, null); // 1
    sc.addCodeLine("int left = 0;", null, 2, null); // 2
    sc.addCodeLine("int right = intArray.length - 1;", null, 2, null); // 3
    sc.addCodeLine("int complete = right;", null, 2, null); // 4
    sc.addCodeLine("do {", null, 2, null); // 5
    sc.addCodeLine("swapped = false;", null, 4, null); // 6
    sc.addCodeLine("for (int down = right; down > left; down--)", null, 4, null); // 7
    sc.addCodeLine("if (intArray[down] < intArray[down - 1]) {", null, 6, null); // 8
    sc.addCodeLine("swap(intArray, down, down-1);", null, 8, null); // 9
    sc.addCodeLine("swapped = true;", null, 8, null); // 10
    sc.addCodeLine("complete = down;", null, 8, null); // 11
    sc.addCodeLine("}", null, 6, null); // 12
    sc.addCodeLine("left = complete;", null, 4, null); // 13
    sc.addCodeLine("for (int up = left; up < right; up++)", null, 4, null); // 14
    sc.addCodeLine("if (intArray[up + 1] < intArray[up]) {", null, 6, null); // 15
    sc.addCodeLine("swap(intArray[up + 1], up, up+1);", null, 8, null); // 16
    sc.addCodeLine("swapped = true;", null, 8, null); // 17
    sc.addCodeLine("complete = up;", null, 8, null); // 18
    sc.addCodeLine("}", null, 6, null); // 19
    sc.addCodeLine("right = complete;", null, 4, null); // 20
    sc.addCodeLine("} while (swapped);", null, 2, null); // 21
    sc.addCodeLine("}", null, 0, null); // 22

  }

  public void shakerSort(int[] array) {
    Timing swapTiming = new TicksTiming(70);
    int nrIterations = 0, nrComparisons = 0, nrSwaps = 0;

    ArrayMarker udMarker;

    showSourceCode();
    lang.nextStep();

    sc.highlight(0);
    lang.nextStep();

    boolean swapped;
    swappedTF.setText("swapped: ", null, null);
    sc.toggleHighlight(0, 1);
    lang.nextStep();

    ArrayMarker lMarker = lang.newArrayMarker(intArray, 0, "L", null,
        lMarkerProps);
    sc.toggleHighlight(1, 2);
    lang.nextStep();

    ArrayMarker rMarker = lang.newArrayMarker(intArray,
        intArray.getLength() - 1, "R", null, rMarkerProps);
    sc.toggleHighlight(2, 3);
    lang.nextStep();

    int complete = rMarker.getPosition();
    sc.toggleHighlight(3, 4);
    intArray.highlightCell(complete, null, null);
    lang.nextStep();
    sc.unhighlight(4);

    do {
      sc.highlight(5);
      lang.nextStep();

      nrIt.setText(String.valueOf(++nrIterations), null, null);
      swapped = false;
      swappedTF.setText("swapped: false", null, null);
      swappedTF.changeColor(null, Color.RED, null, null);
      sc.toggleHighlight(5, 6);
      lang.nextStep();

      swappedTF.changeColor(null, Color.BLACK, null, null);
      sc.toggleHighlight(6, 7);
      udMarker = lang.newArrayMarker(intArray, rMarker.getPosition(), "DOWN",
          null, dMarkerProps);

      for (; udMarker.getPosition() > lMarker.getPosition(); udMarker
          .decrement(null, defaultTiming)) {
        lang.nextStep();

        nrIt.setText(String.valueOf(++nrIterations), null, null);
        sc.toggleHighlight(7, 8);
        intArray.highlightElem(udMarker.getPosition() - 1,
            udMarker.getPosition(), null, null);
        nrCo.setText(String.valueOf(++nrComparisons), null, null);
        lang.nextStep();

        sc.unhighlight(8);
        if (intArray.getData(udMarker.getPosition()) < intArray
            .getData(udMarker.getPosition() - 1)) {
          sc.highlight(9);
          intArray.unhighlightCell(complete, null, null);
          intArray.swap(udMarker.getPosition(), udMarker.getPosition() - 1,
              null, swapTiming);
          intArray.highlightCell(complete, null, null);
          nrSw.setText(String.valueOf(++nrSwaps), null, null);
          lang.nextStep();
          intArray.unhighlightElem(udMarker.getPosition() - 1,
              udMarker.getPosition(), null, null);

          swapped = true;
          swappedTF.setText("swapped: true", null, null);
          swappedTF.changeColor(null, Color.RED, null, null);
          sc.toggleHighlight(9, 10);
          lang.nextStep();
          swappedTF.changeColor(null, Color.BLACK, null, null);

          intArray.unhighlightCell(complete, null, null);
          complete = udMarker.getPosition();
          intArray.highlightCell(complete, null, null);
          sc.toggleHighlight(10, 11);
          lang.nextStep();
          sc.unhighlight(11);
        }
        intArray.unhighlightElem(udMarker.getPosition() - 1,
            udMarker.getPosition(), null, null);
        sc.highlight(12);
        lang.nextStep();
        sc.toggleHighlight(12, 7);

      }

      lang.nextStep();
      udMarker.hide();
      sc.toggleHighlight(7, 13);
      intArray.highlightCell(lMarker.getPosition(), null, null);
      lMarker.move(complete, null, defaultTiming);

      lang.nextStep();

      sc.toggleHighlight(13, 14);

      udMarker = lang.newArrayMarker(intArray, lMarker.getPosition(), "UP",
          null, uMarkerProps);

      for (; udMarker.getPosition() < rMarker.getPosition(); udMarker
          .increment(null, defaultTiming)) {
        lang.nextStep();

        nrIt.setText(String.valueOf(++nrIterations), null, null);
        sc.toggleHighlight(14, 15);
        intArray.highlightElem(udMarker.getPosition(),
            udMarker.getPosition() + 1, null, null);
        nrCo.setText(String.valueOf(++nrComparisons), null, null);
        lang.nextStep();

        sc.unhighlight(15);

        if (intArray.getData(udMarker.getPosition() + 1) < intArray
            .getData(udMarker.getPosition())) {
          sc.highlight(16);
          intArray.unhighlightCell(complete, null, null);
          intArray.swap(udMarker.getPosition(), udMarker.getPosition() + 1,
              null, swapTiming);
          intArray.highlightCell(complete, null, null);
          nrSw.setText(String.valueOf(++nrSwaps), null, null);
          lang.nextStep();

          intArray.unhighlightElem(udMarker.getPosition(),
              udMarker.getPosition() + 1, null, null);

          swapped = true;
          swappedTF.setText("swapped: true", null, null);
          swappedTF.changeColor(null, Color.RED, null, null);
          sc.toggleHighlight(16, 17);
          lang.nextStep();
          swappedTF.changeColor(null, Color.BLACK, null, null);
          intArray.unhighlightCell(complete, null, null);
          complete = udMarker.getPosition();
          intArray.highlightCell(complete, null, null);
          sc.toggleHighlight(17, 18);
          lang.nextStep();

          sc.unhighlight(18);
        }

        intArray.unhighlightElem(udMarker.getPosition(),
            udMarker.getPosition() + 1, null, null);
        sc.highlight(19);
        lang.nextStep();
        sc.toggleHighlight(19, 14);
      }

      lang.nextStep();
      udMarker.hide();
      sc.toggleHighlight(14, 20);
      intArray.highlightCell(rMarker.getPosition(), null, null);
      rMarker.move(complete, null, defaultTiming);
      lang.nextStep();

      sc.toggleHighlight(20, 21);
      swappedTF.changeColor(null, Color.RED, null, null);
      lang.nextStep();
      sc.unhighlight(21);
      swappedTF.changeColor(null, Color.BLACK, null, null);
    } while (swapped);
    sc.highlight(22);
    intArray.highlightCell(0, intArray.getLength() - 1, null, null);
    lang.nextStep();
    sc.unhighlight(22);
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    init();

    int[] arrayData = (int[]) arg1.get("Input Data");

    arrayProps = (ArrayProperties) arg0.getPropertiesByName("Array");
    scProps = (SourceCodeProperties) arg0.getPropertiesByName("Source Code");

    internInit(arrayData);

    shakerSort(arrayData);
    return lang.toString();
  }

  public String getAlgorithmName() {

    return "Shaker Sort";
  }

  public String getAnimationAuthor() {

    return "Amir Naseri, Morteza Emamgholi";
  }

  public String getCodeExample() {
    return "public void shakerSort(int[] intArray) {\n" + "	boolean swapped;\n"
        + "	int left = 0;\n" + "	int right = intArray.length - 1;\n"
        + "	int complete = right;\n" + "	do {\n" + "		swapped = false;\n"
        + "		for (int down = right; down > left; down--)\n"
        + "			if (intArray[down] < intArray[down - 1]) {\n"
        + "				swap(intArray, down, down-1);\n" + "				swapped = true;\n"
        + "				complete = down;\n" + "			}\n" + "		left = complete;\n"
        + "		for (int up = left; up < right; up++)\n"
        + "			if (intArray[up + 1] < intArray[up]) {\n"
        + "				swap(intArray[up + 1], up, up+1);\n" + "				swapped = true;\n"
        + "				complete = up;\n" + "			}\n" + "		right = complete;\n"
        + "	} while (swapped);\n" + "}";
  }

  public Locale getContentLocale() {

    return Locale.US;
  }

  public String getDescription() {

    return "Animates ShakerSort with Source Code and Highlighting";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {

    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {

    return "ShakerSort";
  }

  public String getOutputLanguage() {
    // TODO Auto-generated method stub
    return Generator.JAVA_OUTPUT;
  }

  public void init() {

    lang = new AnimalScript("ShakerSort", "Amir Naseri, Morteza Emamgholi",
        640, 480);
    lang.setStepMode(true);

  }

}
