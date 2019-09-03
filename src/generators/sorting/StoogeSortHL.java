package generators.sorting;

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
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class StoogeSortHL implements Generator {

  private Language        lang;
  private int[]           arrayData;
  private IntArray        arr;

  private ArrayProperties arrayProperties;
  private ArrayMarkerProperties leftProperties, rightProperties;

  private ArrayMarker           leftMarker, rightMarker;

  private SourceCodeProperties  codeProperties;
  private SourceCode            code;

  public StoogeSortHL() {
    // Parameterloser Konstruktor
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    arrayData = (int[]) primitives.get("array");

    init();

    // ArrayProperties uebernehmen
    arrayProperties = new ArrayProperties();
    arrayProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("array", AnimationPropertiesKeys.COLOR_PROPERTY));
    arrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
        props.get("array", AnimationPropertiesKeys.FILL_PROPERTY));
    arrayProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY,
        props.get("array", AnimationPropertiesKeys.FILLED_PROPERTY));
    arrayProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        props.get("array", AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
    arrayProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        props.get("array", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
    arrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        props.get("array", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
    arr = lang.newIntArray(new Coordinates(20, 140), arrayData, "arr", null,
        arrayProperties);
    lang.nextStep();

    // leftMarkerProperties uebernehmen
    leftProperties = new ArrayMarkerProperties();
    leftProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("left", AnimationPropertiesKeys.COLOR_PROPERTY));
    leftProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        props.get("left", AnimationPropertiesKeys.LABEL_PROPERTY));
    leftProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    leftMarker = lang.newArrayMarker(arr, 0, "left", null, leftProperties);
    leftMarker.hide();

    // rightMarkerProperties uebernehmen
    rightProperties = new ArrayMarkerProperties();
    rightProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("right", AnimationPropertiesKeys.COLOR_PROPERTY));
    rightProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        props.get("right", AnimationPropertiesKeys.LABEL_PROPERTY));
    rightProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true); // diese
                                                                        // Eigentschaft
                                                                        // ist
                                                                        // fest
                                                                        // kodiert
    rightMarker = lang.newArrayMarker(arr, 0, "right", null, rightProperties);
    rightMarker.hide();

    // codeProperties uebernehmen
    codeProperties = new SourceCodeProperties();
    codeProperties.set(AnimationPropertiesKeys.BOLD_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.BOLD_PROPERTY));
    codeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
    codeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, props
        .get("sourceCode", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
    codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.FONT_PROPERTY));
    codeProperties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY));
    codeProperties.set(AnimationPropertiesKeys.ITALIC_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.ITALIC_PROPERTY));
    codeProperties.set(AnimationPropertiesKeys.SIZE_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.SIZE_PROPERTY));
    code = lang.newSourceCode(new Coordinates(20, 190), "sourceCode", null,
        codeProperties);
    makeCode();
    lang.nextStep();

    arr.highlightCell(0, arrayData.length - 1, null, null);
    lang.nextStep();
    stoogeSort(arr, 0, arr.getLength());

    return lang.toString();
  }

  @Override
  public void init() {
    lang = new AnimalScript(getName(), getAnimationAuthor(), 640, 480);
    lang.setStepMode(true);

    // Header
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    TextProperties headerProperties = new TextProperties();
    headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30), "StoogeSort", "header",
        null, headerProperties);
    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "hRect", null,
        rectProperties);
    lang.nextStep();

    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 14));

    Text text1 = lang
        .newText(
            new Coordinates(20, 90),
            "1. Sind das erste und das letzte Element nicht in der richtigen Reihenfolge, so werden sie vertauscht.",
            "descr", null, textProperties);
    Text text2 = lang
        .newText(
            new Coordinates(20, 130),
            "2. Sind mehr als zwei Elemente in der Liste - fortsetzen, sonst abbrechen.",
            "descr1", null, textProperties);
    Text text3 = lang.newText(new Coordinates(20, 160),
        "3. Sortiere die ersten zwei Drittel der Liste.", "descr3", null,
        textProperties);
    Text text4 = lang.newText(new Coordinates(20, 190),
        "4. Sortiere die letzten zwei Drittel der Liste.", "descr4", null,
        textProperties);
    Text text5 = lang.newText(new Coordinates(20, 220),
        "5. Sortiere die ersten zwei Drittel der Liste.", "descr5", null,
        textProperties);
    Text text6 = lang
        .newText(
            new Coordinates(20, 280),
            "Komplexitaet: O(n^2.71) [Bemerkung: StoogeSort zaehlt eher zu unpraktischen Algorithmen]",
            "descr6", null, textProperties);
    lang.nextStep();
    text1.hide();
    text2.hide();
    text3.hide();
    text4.hide();
    text5.hide();
    text6.hide();
  }

  public void makeCode() {
    code.addCodeLine("public static void sort(int[] a, int left, int right) {",
        null, 0, null);
    code.addCodeLine("if (a[right-1] < a[left]){", null, 1, null);
    code.addCodeLine("int temp = a[left];", null, 2, null);
    code.addCodeLine("a[left] = a[right-1];", null, 2, null);
    code.addCodeLine("a[right-1] = temp;", null, 2, null);
    code.addCodeLine("}", null, 1, null);
    code.addCodeLine("int len = right-left;", null, 1, null);
    code.addCodeLine("if (len > 2) {", null, 1, null);
    code.addCodeLine("int third=len/3;", null, 2, null);
    code.addCodeLine(
        "stoogeSort(a, left, right-third); // sortiere die ersten 2/3", null,
        2, null);
    code.addCodeLine(
        "stoogeSort(a, left+third, right); // sortiere die letzten 2/3", null,
        2, null);
    code.addCodeLine(
        "stoogeSort(a, left, right-third); // sortiere die ersten 2/3", null,
        2, null);
    code.addCodeLine("}", null, 1, null);
    code.addCodeLine("}", null, 0, null);
  }

  public void goToFunk() {
    code.highlight(0, 0, true);
    code.highlight(13, 0, true);
  }

  public void goFromFunk() {
    code.unhighlight(0);
    code.unhighlight(13);
  }

  public void goToIf1() {
    code.highlight(1, 0, true);
    code.highlight(5, 0, true);
  }

  public void goFromIf1() {
    code.unhighlight(1);
    code.unhighlight(5);
  }

  public void goToIf2() {
    code.highlight(7, 0, true);
    code.highlight(12, 0, true);
  }

  public void goFromIf2() {
    code.unhighlight(7);
    code.unhighlight(12);
  }

  public void unhightLightAll() {
    for (int i = 0; i < 14; i++)
      code.unhighlight(i);
  }

  public void stoogeSort(IntArray a, int left, int right) {
    unhightLightAll();
    code.highlight(0);
    lang.nextStep();

    code.unhighlight(0);
    code.highlight(1);
    lang.nextStep();

    leftMarker.move(left, null, null);
    leftMarker.show();
    rightMarker.move(right - 1, null, null);
    rightMarker.show();
    lang.nextStep();

    a.highlightElem(left, null, null);
    a.highlightElem(right - 1, null, null);
    lang.nextStep();

    if (a.getData(right - 1) < a.getData(left)) {
      code.unhighlight(1);
      goToIf1();
      code.highlight(2);
      code.highlight(3);
      code.highlight(4);
      lang.nextStep();

      arr.swap(left, right - 1, null, new MsTiming(1000));
      lang.nextStep();
      code.unhighlight(2);
      code.unhighlight(3);
      code.unhighlight(4);
    }
    goFromIf1();

    a.unhighlightElem(left, null, null);
    a.unhighlightElem(right - 1, null, null);

    leftMarker.hide();
    rightMarker.hide();

    int len = right - left;
    code.highlight(6);
    lang.nextStep();

    code.unhighlight(6);
    code.highlight(7);
    lang.nextStep();
    if (len > 2) {
      goToIf2();
      code.highlight(8);
      int third = len / 3; // abgerundene Integer-Division
      lang.nextStep();

      // erster rekursiver Aufrunf
      code.unhighlight(8);
      code.highlight(9);
      lang.nextStep();
      arr.unhighlightCell(0, a.getLength() - 1, null, null);
      arr.highlightCell(left, right - third - 1, null, null);
      lang.nextStep();
      stoogeSort(a, left, right - third); // sortiere die ersten 2/3

      // zweiter rekursiver Aufrunf
      unhightLightAll();
      code.highlight(10);
      lang.nextStep();
      arr.unhighlightCell(0, a.getLength() - 1, null, null);
      arr.highlightCell(left + third, right - 1, null, null);
      lang.nextStep();
      stoogeSort(a, left + third, right); // sortiere die letzten 2/3

      // dritter rekursiver Aufrunf
      unhightLightAll();
      code.highlight(11);
      lang.nextStep();
      arr.unhighlightCell(0, a.getLength() - 1, null, null);
      arr.highlightCell(left, right - third - 1, null, null);
      lang.nextStep();
      stoogeSort(a, left, right - third); // sortiere die ersten 2/3

    }
    goFromIf2();
    arr.unhighlightCell(0, arr.getLength() - 1, null, null);
  }

  @Override
  public String getAlgorithmName() {
    return "Stooge Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Hlib Levitin";
  }

  @Override
  public String getCodeExample() {
    String code = "public void stoogeSort(int[] a, int left, int right) {\n"
        + "  if (a[right-1] &lt; a[left]) {\n" + "    int temp = a[left];\n"
        + "    a[left] = a[right-1];\n" + "    a[right-1] = temp;\n" + "  }\n"
        + "  int len = right-left;\n" + "  if (len &gt; 2) {\n"
        + "    int third = len/3;\n"
        + "    stoogeSort(a, left, right-third);\n"
        + "    stoogeSort(a, left+third, right);\n"
        + "    stoogeSort(a, left, right-third);\n" + "  }\n" + "}\n";
    return code;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Stooge sort (auch Trippelsort) ist ein rekursiver Sortieralgorithmus nach dem Prinzip Teile und herrsche (divide and conquer).";
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
  public String getName() {
    return "StoogeSort (DE)";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}