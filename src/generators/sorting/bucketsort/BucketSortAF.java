package generators.sorting.bucketsort;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;

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
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class BucketSortAF implements Generator {

  private Language            lang;
  private final int           bucketGap      = 30;

  private static final String ALGORITHM_NAME = "Bucket Sort";

  private static final String AUTHOR         = "Andreas Franek"; // <andy-held@web.de>";

  private static final String DESCRIPTION    = "(von Wikipedia)BucketSort ist ein stabiles Sortierverfahren, das eine Liste in linearer Laufzeit sortieren kann, da es nicht auf Schlüsselvergleichen basiert. Es arbeitet jedoch out-of-place.";

  private static final String SOURCE_CODE    = "void bucketsort(int[] arr, int n)"
                                                 + "\n{"
                                                 + "\n	Vector<LinkedList<Integer>> buckets = new Vector<LinkedList<Integer>>(n);"
                                                 + "\n	int maxVal = arr[0];"
                                                 + "\n	int minVal = arr[0];"
                                                 + "\n	for(int i = 0; i < arr.length; i++)"
                                                 + "\n	{"
                                                 + "\n		if(minVal > arr[i]) minVal = i;"
                                                 + "\n		if(maxVal < arr[i]) maxVal = i;"
                                                 + "\n	}"
                                                 + "\n	int range = maxVal - minVal + 1;"
                                                 + "\n	int buckSize = (range % n == 0)? range/n: range/n+1;"
                                                 + "\n	for(int i: arr)"
                                                 + "\n	{"
                                                 + "\n		buckets.elementAt((i-minVal)/buckSize).add(i);"
                                                 + "\n	}" + "\n}";

  public BucketSortAF(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  public BucketSortAF() {
  }

  /**
   * Sort the int array passed in
   * 
   * @param arr
   *          the array to be sorted
   * @param n
   *          number of buckets
   */
  public void sort(int[] arr, int n) {
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 32));
    @SuppressWarnings("unused")
    Text header = lang.newText(new Coordinates(20, 30), "BucketSort", "header",
        null, headerProps);
    @SuppressWarnings("unused")
    Rect rect = lang.newRect(new Coordinates(12, 15), new Coordinates(215, 52),
        "hdrect", null);
    // Create Array: coordinates, data, name, display options,
    // default properties

    // Create SourceCode: coordinates, name, display options,
    // default properties

    // first, set the visual properties for the source code
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 16));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Coordinates(20, 50), "sourceCode",
        null, scProps);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display delay
    sc.addCodeLine("Pseudcode Beschreibung:", null, 0, null); // 0
    sc.addCodeLine("", null, 2, null);
    sc.addCodeLine("Eingaben: Array mit Integern arr, positiver Integer n",
        null, 0, null);
    sc.addCodeLine(
        "Das Array mit Integern ist zu sortieren, dazu werden n 'Buckets' erstellt",
        null, 0, null); // 3
    sc.addCodeLine(
        "1. Gehe einmal durch arr und notiere den kleinsten (minVal) und größten (maxVal) Wert",
        null, 0, null); // 4
    sc.addCodeLine("2. Berechne:", null, 0, null); // 5
    sc.addCodeLine("range = maxVal - minVal + 1", null, 2, null); // 6
    sc.addCodeLine("buckSize = (range%n == 0)? range/n: range/n+1", null, 2,
        null); // 7
    sc.addCodeLine("3. Für jedes Element i in arr", null, 0, null); // 8
    sc.addCodeLine(
        "Füge das Element in den Bucket mit der Nummer: (i-minVal)/buckSize",
        null, 2, null); // 9

    lang.nextStep();

    sc.highlight(2);

    TextProperties inputProps = new TextProperties();
    inputProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
    inputProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));
    @SuppressWarnings("unused")
    Text nText = lang.newText(new Coordinates(20, 275), "n = " + n, "n", null,
        inputProps);
    Text arrText = lang.newText(new Coordinates(20, 335), "arr:", "arrT", null,
        inputProps);
    // first, set the visual properties (somewhat similar to CSS)
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.ORANGE);

    // now, create the IntArray object, linked to the properties
    IntArray ia = lang.newIntArray(new Coordinates(20, 350), arr, "intArray",
        null, arrayProps);
    // start a new step after the array was created
    lang.nextStep();

    Text buckText = lang.newText(new Coordinates(20, 380), "buckets:",
        "buckText", null, inputProps);
    StringArray buckets[] = new StringArray[n];
    String s[] = { "  " };
    for (int i = 0; i < n; i++) {
      buckets[i] = lang.newStringArray(
          new Coordinates(20 + i * bucketGap, 400), s, "b" + i, null,
          arrayProps);
    }
    sc.unhighlight(2);
    sc.highlight(3);
    lang.nextStep();

    sc.unhighlight(3);
    sc.highlight(4);
    arrText.hide();
    buckText.hide();
    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    ArrayMarker am = lang.newArrayMarker(ia, 0, "am", null, arrayIMProps);
    ia.highlightCell(0, null, null);
    ia.highlightElem(0, null, null);
    int maxVal, minVal;
    maxVal = arr[0];
    minVal = arr[0];
    Text maxV = lang.newText(new Coordinates(100, 275), "maxVal = " + maxVal,
        "maxV", null);
    Text minV = lang.newText(new Coordinates(200, 275), "minVal = " + minVal,
        "minV", null);
    lang.nextStep();

    for (int i = 1; i < arr.length; i++) {
      lang.nextStep();
      if (arr[i] > maxVal) {
        maxVal = arr[i];
        maxV.setText("maxVal = " + maxVal, null, null);
      } else if (arr[i] < minVal) {
        minVal = arr[i];
        minV.setText("minVal = " + minVal, null, null);
      }

      am.move(i, null, null);
      ia.unhighlightCell(i - 1, null, null);
      ia.unhighlightElem(i - 1, null, null);
      ia.highlightCell(i, null, null);
      ia.highlightElem(i, null, null);
      lang.nextStep();
    }

    am.hide();
    ia.unhighlightCell(arr.length - 1, null, null);
    ia.unhighlightElem(arr.length - 1, null, null);
    sc.unhighlight(4);
    sc.highlight(5);
    sc.highlight(6);
    int range = maxVal - minVal + 1;
    @SuppressWarnings("unused")
    Text rangeT = lang.newText(new Coordinates(100, 300), "range = " + range,
        "range", null);
    lang.nextStep();

    sc.unhighlight(6);
    sc.highlight(7);
    int buckSize = (range % n == 0) ? range / n : range / n + 1;
    @SuppressWarnings("unused")
    Text buckSizeT = lang.newText(new Coordinates(200, 300), "buckSize = "
        + buckSize, "buckSize", null);
    lang.nextStep();

    CheckpointUtils.checkpointEvent(this, "MinMaxSet", new Variable("Range",
        range), new Variable("buckSize", buckSize),
        new Variable("Min", minVal), new Variable("Max", maxVal));

    int bucketAt[] = new int[arr.length];
    sc.unhighlight(5);
    sc.unhighlight(7);
    sc.highlight(8);
    sc.highlight(9);
    am.move(0, null, null);
    am.show();
    ia.highlightCell(0, null, null);
    ia.highlightElem(0, null, null);
    int buckNum = (arr[0] - minVal) / buckSize;
    buckets[buckNum].put(0, String.valueOf(arr[0]), null, null);
    CheckpointUtils.checkpointEvent(this, "bucketInhalt", new Variable(
        "position0", buckNum), new Variable("index0", String.valueOf(arr[0])));
    bucketAt[buckNum]++;
    lang.nextStep();

    for (int i = 1; i < arr.length; i++) {
      lang.nextStep();
      am.move(i, null, null);
      buckNum = (arr[i] - minVal) / buckSize;
      if (bucketAt[buckNum] == 0) {
        buckets[buckNum].put(0, String.valueOf(arr[i]), null, null);
        CheckpointUtils.checkpointEvent(this, "bucketInhalt", new Variable(
            "position1", buckNum),
            new Variable("value1", String.valueOf(arr[i])));
      } else {
        lang.newStringArray(new Coordinates(20 + buckNum * bucketGap,
            400 + bucketAt[buckNum] * 25), new String[] { String
            .valueOf(arr[i]) }, "b" + i, null, arrayProps);
        CheckpointUtils.checkpointEvent(this, "bucketInhalt", new Variable(
            "position2", buckNum),
            new Variable("value2", String.valueOf(arr[i])));
      }
      bucketAt[buckNum]++;

      CheckpointUtils.checkpointEvent(this, "BucketAdd", new Variable(
          "buckNum", buckNum), new Variable("BucketAt", bucketAt[buckNum]));

      ia.unhighlightCell(i - 1, null, null);
      ia.unhighlightElem(i - 1, null, null);
      ia.highlightCell(i, null, null);
      ia.highlightElem(i, null, null);
      lang.nextStep();
    }
    am.hide();
    ia.unhighlightCell(arr.length - 1, null, null);
    ia.unhighlightElem(arr.length - 1, null, null);
    sc.unhighlight(8);
    sc.unhighlight(9);

    sc.hide();
    SourceCode conclusion = lang.newSourceCode(new Coordinates(20, 50),
        "conclusion", null, scProps);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display delay
    conclusion.addCodeLine("Fazit:", null, 0, null); // 0
    conclusion.addCodeLine("", null, 2, null);
    conclusion.addCodeLine("Die Buckets müssen intern noch sortiert werden.",
        null, 0, null);
    conclusion.addCodeLine(
        "Das kann in O(nlogn) gemacht werden (n ist nun kleiner!).", null, 0,
        null); // 3
    conclusion.addCodeLine("Das sortieren in die Buckets ist in O(n).", null,
        0, null); // 4
    conclusion
        .addCodeLine(
            "Die Buckets können am Ende leer sein, oder aber sehr viele Werte beinhalten.",
            null, 0, null); // 5
    conclusion.addCodeLine("Das verschlechtert die Laufzeit deutlich.", null,
        0, null); // 6
  }

  @Override
  public String getName() {
    return "Bucket Sort";
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public String getCodeExample() {
    return SOURCE_CODE;
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    sort((int[]) arg1.get("array"), (Integer) arg1.get("n"));
    return lang.getAnimationCode();
  }

  @Override
  public String getAlgorithmName() {
    return ALGORITHM_NAME;
  }

  @Override
  public String getAnimationAuthor() {
    return AUTHOR;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript(ALGORITHM_NAME, AUTHOR, 640, 480);
  }

}
