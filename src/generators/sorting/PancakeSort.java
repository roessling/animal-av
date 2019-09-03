package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class PancakeSort implements Generator {

  private Language        lang;
  private int[]           numbers;
  private ArrayProperties arrayProps;
  private TextProperties  textProps, headlineProps;
  private RectProperties  headlineRectProps, sourceCodeRectProps,
      flipRectProps;
  private ArrayMarkerProperties am_i, am_n_minus_i, am_curMax;
  private SourceCode            sc, fc;
  private SourceCodeProperties  scProps, fcProps;
  private Timing                defaultTiming;
  private IntArray              numbers_array;
  private Text                  lable_n, lable_flips, lable_curMax, currStep;
  private int                   flips;

  public void init() {
    lang = new AnimalScript("Pancake Sort [DE]",
        "Marius Hornung, Jan Klostermann", 730, 480);

    lang.setStepMode(true);

    // create properties with default values
    arrayProps = new ArrayProperties();
    textProps = new TextProperties();
    headlineProps = new TextProperties();
    headlineRectProps = new RectProperties();
    sourceCodeRectProps = new RectProperties();
    flipRectProps = new RectProperties();
    scProps = new SourceCodeProperties();
    fcProps = new SourceCodeProperties();
    am_i = new ArrayMarkerProperties();
    am_n_minus_i = new ArrayMarkerProperties();
    am_curMax = new ArrayMarkerProperties();
    defaultTiming = new TicksTiming(15);
    ;

    // Redefine properties: border black, filled with gray
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE); // color
                                                                                // white
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        new Color(70, 70, 70)); // color dark gray
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    arrayProps.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true); // vertical
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(100, 100,
        100)); // fill color gray
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.WHITE);

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));

    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));

    am_i.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    am_i.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

    am_n_minus_i.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(214,
        143, 31));
    am_n_minus_i.set(AnimationPropertiesKeys.LABEL_PROPERTY, "n - i");

    am_curMax.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        new Color(20, 108, 20));
    am_curMax.set(AnimationPropertiesKeys.LABEL_PROPERTY, "curMax");

    headlineRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headlineRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    headlineRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255,
        241, 189));
    headlineRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    sourceCodeRectProps
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    sourceCodeRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    sourceCodeRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(
        235, 235, 235));
    sourceCodeRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    flipRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    flipRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    flipRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255,
        241, 189));
    flipRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    fcProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    fcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    fcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    fcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  // algorithm

  public void pancakeSort(int n) {

    showSourceCode();
    showFlipCode();

    lang.newText(new Coordinates(300, 30), "Pancake Sort", "headline", null,
        headlineProps);
    lang.newRect(new Coordinates(280, 20), new Coordinates(430, 50),
        "headlineRect", null, headlineRectProps);

    lang.newRect(new Coordinates(380, 90), new Coordinates(710, 310),
        "sourceCodeRect", null, sourceCodeRectProps);
    lang.newRect(new Coordinates(380, 320), new Coordinates(710, 460),
        "flipRect", null, flipRectProps);

    // show intro text

    Text desc_1 = lang.newText(new Coordinates(20, 85),
        "Pancake Sort ist ein Sortieralgorithmus zum", "desc_1", null,
        textProps);
    Text desc_2 = lang.newText(new Coordinates(20, 110),
        "Sortieren von Integerzahlen. Die einzige erlaubte", "desc_2", null,
        textProps);
    Text desc_3 = lang.newText(new Coordinates(20, 130),
        "Operation ist die Umkehrung einer Teilsequenz der", "desc_3", null,
        textProps);
    Text desc_4 = lang.newText(new Coordinates(20, 150),
        "zu sortierenden Zahlen. Diese Operation kann man", "desc_4", null,
        textProps);
    Text desc_5 = lang
        .newText(new Coordinates(20, 170),
            "sich in etwa wie das Umdrehen der obersten", "desc_5", null,
            textProps);
    Text desc_6 = lang.newText(new Coordinates(20, 190),
        "k Pfannkuchen (engl. pancakes) von einem Stapel", "desc_6", null,
        textProps);
    Text desc_7 = lang.newText(new Coordinates(20, 210),
        "Pfannkuchen vorstellen.", "desc_7", null, textProps);

    Text desc_8 = lang.newText(new Coordinates(20, 250),
        "Das Pancake Sort Problem beschaeftigt sich mit", "desc_8", null,
        textProps);
    Text desc_9 = lang.newText(new Coordinates(20, 270),
        "der Frage, wieviel 'Flips' fuer die Sortierung", "desc_9", null,
        textProps);
    Text desc_10 = lang.newText(new Coordinates(20, 290),
        "des Stapels benoetigt werden. Die Laufzeit", "desc_10", null,
        textProps);
    Text desc_11 = lang.newText(new Coordinates(20, 310),
        "des Algorithmus ansich ist irrelevant.", "desc_11", null, textProps);
    Text desc_12 = lang.newText(new Coordinates(20, 330),
        "Die genaue Anzahl der benoetigten Flips ist", "desc_12", null,
        textProps);
    Text desc_13 = lang.newText(new Coordinates(20, 350),
        "unbekannt. Tests haben jedoch ergeben, dass der", "desc_13", null,
        textProps);
    Text desc_14 = lang.newText(new Coordinates(20, 370),
        "Wert zwischen (15/14) n und (18/11) n liegt.", "desc_14", null,
        textProps);

    lang.nextStep();

    desc_1.hide();
    desc_2.hide();
    desc_3.hide();
    desc_4.hide();
    desc_5.hide();
    desc_6.hide();
    desc_7.hide();
    desc_8.hide();
    desc_9.hide();
    desc_10.hide();
    desc_11.hide();
    desc_12.hide();
    desc_13.hide();
    desc_14.hide();

    numbers_array = lang.newIntArray(new Coordinates(100, 160), numbers, "A",
        null, arrayProps);

    lable_curMax = lang.newText(new Coordinates(20, 405),
        "Index max. Wert (curMax): -", "var_curMax", null, headlineProps);
    lable_n = lang.newText(new Coordinates(20, 420), "Variable n: " + n,
        "var_n", null, headlineProps);
    lable_flips = lang.newText(new Coordinates(20, 440), "Anzahl 'Flips': 0",
        "count_flips", null, headlineProps);

    currStep = lang.newText(new Coordinates(20, 100),
        "Initialisierung des Algorithmus.", "currentStep", null, textProps);

    sort(n);

  }

  public void sort(int n) {

    sc.highlight(0);
    lang.nextStep();

    currStep
        .setText("Abbruch des Algorithmus bei n == 1.", null, defaultTiming);

    sc.toggleHighlight(0, 1);
    lang.nextStep();

    if (n == 1) {
      currStep.setText("Das Array (der Pfannkuchenstapel) wurde sortiert.",
          null, defaultTiming);
    }

    if (n == 1)
      return;

    int curMax = getMax(n);

    currStep.setText("Bestimmung des Maximum im Teilintervall [0.." + n + "].",
        null, defaultTiming);

    // show curMax in array
    numbers_array.highlightCell(curMax, null, defaultTiming);

    // set curMax lable
    lable_curMax.setText("Index max. Wert (curMax): " + curMax, defaultTiming,
        defaultTiming);

    sc.toggleHighlight(1, 2);
    lang.nextStep();

    currStep.setText("Befindet sich das Maximum bereits an Stelle 0?", null,
        defaultTiming);

    sc.toggleHighlight(2, 3);
    lang.nextStep();

    if (curMax == 0) {

      currStep.setText("Ja! Es muss nicht mehr an Stelle 0 geflipt werden.",
          null, defaultTiming);

      sc.toggleHighlight(3, 4);
      lang.nextStep();

      currStep.setText("Flipoperation auf komplettes Teilintervall.", null,
          defaultTiming);
      lang.nextStep();

      // increase label flips
      flips++;
      lable_flips.setText("Anzahl 'Flips': " + flips, defaultTiming,
          defaultTiming);

      numbers_array.unhighlightCell(curMax, null, defaultTiming);
      flip(n - 1);

      sc.toggleHighlight(4, 5);
      lang.nextStep();

      sc.unhighlight(5);

      // set label n--
      lable_n.setText("Variable n: " + (n - 1), defaultTiming, defaultTiming);

      sort(n - 1);

    } else {

      currStep.setText("Nein! Es muss an Stelle 0 geflipt werden.", null,
          defaultTiming);

      sc.toggleHighlight(3, 7);
      lang.nextStep();

      // increase label flips
      flips++;
      lable_flips.setText("Anzahl 'Flips': " + flips, defaultTiming,
          defaultTiming);

      numbers_array.unhighlightCell(curMax, null, defaultTiming);
      flip(curMax);

      sc.toggleHighlight(7, 8);
      lang.nextStep();

      currStep.setText("Flipoperation auf komplettes Teilintervall.", null,
          defaultTiming);
      lang.nextStep();
      // increase label flips
      flips++;
      lable_flips.setText("Anzahl 'Flips': " + flips, defaultTiming,
          defaultTiming);

      flip(n - 1);

      sc.toggleHighlight(8, 9);
      lang.nextStep();

      sc.unhighlight(9);

      // set label n--
      lable_n.setText("Variable n: " + (n - 1), defaultTiming, defaultTiming);

      sort(n - 1);

    }

  }

  public void flip(int n) {

    currStep.setText("Flipoperation auf den Teilstapel [0.." + n + "].", null,
        defaultTiming);

    fc.highlight(0);
    lang.nextStep();

    ArrayMarker marker_i = lang.newArrayMarker(numbers_array, 0, "i", null,
        am_i);
    ArrayMarker marker_n_minus_i = lang.newArrayMarker(numbers_array, n
        - marker_i.getPosition(), "n - i", null, am_n_minus_i);

    for (; marker_i.getPosition() < (n + 1) / 2; marker_i.increment(null,
        defaultTiming)) {

      fc.toggleHighlight(0, 1);

      marker_n_minus_i.move(n - marker_i.getPosition(), null, defaultTiming);

      lang.nextStep();

      int tmp = numbers_array.getData(marker_i.getPosition());

      fc.toggleHighlight(1, 2);
      lang.nextStep();

      numbers_array.put(marker_i.getPosition(),
          numbers_array.getData(n - marker_i.getPosition()), null,
          defaultTiming);

      fc.toggleHighlight(2, 3);
      lang.nextStep();

      numbers_array.put(n - marker_i.getPosition(), tmp, null, defaultTiming);

      fc.toggleHighlight(3, 4);
      lang.nextStep();

      fc.unhighlight(4);

    }

    marker_i.hide();
    marker_n_minus_i.hide();

  }

  public int getMax(int n) {

    int posMax = 0;

    for (int i = 1; i < n; i++) {
      if (numbers[i] > numbers[posMax]) {
        posMax = i;
      }
    }

    return posMax;

  }

  // end algorithm

  public void showSourceCode() {

    // now, create the source code entity
    sc = lang.newSourceCode(new Coordinates(400, 90), "sourceCode", null,
        scProps);
    // add a code line
    // parameters: code itself; name (can be null); indentation level; display
    // options
    sc.addCodeLine("public void pancakeSort(int n) {", null, 0, null);
    sc.addCodeLine("if (n == 1) return;", null, 1, null);
    sc.addCodeLine("int curMax = getMax(n);", null, 1, null);
    sc.addCodeLine("if (curMax == 0) {", null, 1, null);
    sc.addCodeLine("flip(n - 1);", null, 2, null);
    sc.addCodeLine("pancakeSort(n - 1);", null, 2, null);
    sc.addCodeLine("} else {", null, 1, null);
    sc.addCodeLine("flip(curMax);", null, 3, null);
    sc.addCodeLine("flip(n - 1);", null, 3, null);
    sc.addCodeLine("pancakeSort(n - 1);", null, 3, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

  }

  public void showFlipCode() {

    // now, create the source code entity
    fc = lang.newSourceCode(new Coordinates(400, 320), "flipCode", null,
        fcProps);
    // add a code line
    // parameters: code itself; name (can be null); indentation level; display
    // options
    fc.addCodeLine("public void flip(int k) {", null, 0, null);
    fc.addCodeLine("for (int i = 0; i < (k + 1) / 2; i++) {", null, 1, null);
    fc.addCodeLine("int tmp = numbers[i];", null, 2, null);
    fc.addCodeLine("numbers[i] = numbers[k - i];", null, 2, null);
    fc.addCodeLine("numbers[k - i] = tmp;", null, 2, null);
    fc.addCodeLine("}", null, 1, null);
    fc.addCodeLine("}", null, 0, null);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    am_n_minus_i = (ArrayMarkerProperties) props
        .getPropertiesByName("ArrayMarker_n_minus_i");
    numbers = (int[]) primitives.get("numbers");
    arrayProps = (ArrayProperties) props.getPropertiesByName("Array_input");
    arrayProps.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true); // vertical
    scProps = (SourceCodeProperties) props
        .getPropertiesByName("pancakeSort_sourceCode");
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    am_i = (ArrayMarkerProperties) props.getPropertiesByName("ArrayMarker_i");
    am_curMax = (ArrayMarkerProperties) props
        .getPropertiesByName("ArrayMarker_curMax");
    fcProps = (SourceCodeProperties) props
        .getPropertiesByName("flip_sourceCode");
    fcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    pancakeSort(numbers.length);

    return lang.toString();
  }

  public String getName() {
    return "Pancake Sort [DE]";
  }

  public String getAlgorithmName() {
    return "Pancake Sort";
  }

  public String getAnimationAuthor() {
    return "Marius Hornung, Jan Klostermann";
  }

  public String getDescription() {
    return "Pancake Sort ist ein Sortieralgorithmus zum Sortieren von Integerzahlen. Die einzige erlaubte "
        + "\n"
        + "Operation ist die Umkehrung einer Teilsequenz der "
        + "\n"
        + "zu sortierenden Zahlen. Diese Operation kann man "
        + "\n"
        + "sich in etwa wie das Umdrehen der obersten "
        + "\n"
        + "k Pfannkuchen (engl. pancakes) von einem Stapel "
        + "\n"
        + "Pfannkuchen vorstellen.<br>"
        + "\n"
        + "<br>"
        + "\n"
        + "Das Pancake Sort Problem beschaeftigt sich mit "
        + "\n"
        + "der Frage, wieviel 'Flips' fuer die Sortierung "
        + "\n"
        + "des Stapels benoetigt werden. Die Laufzeit "
        + "\n"
        + "des Algorithmus ansich ist irrelevant. "
        + "\n"
        + "Die genaue Anzahl der benoetigten Flips ist "
        + "\n"
        + "unbekannt. Tests haben jedoch ergeben, dass der "
        + "\n"
        + "Wert zwischen (15/14) n und (18/11) n liegt.";
  }

  public String getCodeExample() {
    return "public void pancakeSort(int n) {" + "\n"
        + "    if (n == 1) return;" + "\n" + "    int curMax = getMax(n);"
        + "\n" + "    if (curMax == 0) {" + "\n" + "        flip(n - 1);"
        + "\n" + "        pancakeSort(n - 1);" + "\n" + "    } else {" + "\n"
        + "        flip(curMax);" + "\n" + "        flip(n - 1);" + "\n"
        + "        pancakeSort(n - 1);" + "\n" + "    }" + "\n" + "}" + "\n"
        + "	" + "\n" + "public void flip(int k) {" + "\n"
        + "    for (int i = 0; i < (k + 1) / 2; i++) {" + "\n"
        + "        int tmp = numbers[i];" + "\n"
        + "        numbers[i] = numbers[k - i];" + "\n"
        + "        numbers[k - i] = tmp;" + "\n" + "    }" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}