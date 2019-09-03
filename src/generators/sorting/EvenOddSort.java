package generators.sorting;

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

public class EvenOddSort implements Generator {

  private Language        lang;

  private int[]           arrayData;
  private IntArray        arr;

  private ArrayProperties arrayProperties;
  private ArrayMarkerProperties oddProperties, evenProperties, plus1Properties;
  private SourceCodeProperties  codeProperties;

  private ArrayMarker           oddMarker, evenMarker, plus1Marker;
  private SourceCode            code;

  public EvenOddSort() {
    // Parameterloser Konstruktor
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    // ArrayData uebernehmen
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
    // IntArray anlegen
    arr = lang.newIntArray(new Coordinates(20, 140), arrayData, "arr", null,
        arrayProperties);

    // oddMarkerProperties uebernehmen
    oddProperties = new ArrayMarkerProperties();
    oddProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("odd", AnimationPropertiesKeys.COLOR_PROPERTY));
    oddProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        props.get("odd", AnimationPropertiesKeys.LABEL_PROPERTY));
    oddProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    oddMarker = lang.newArrayMarker(arr, 0, "odd", null, oddProperties);
    oddMarker.hide();

    // evenMarkerProperties uebernehmen
    evenProperties = new ArrayMarkerProperties();
    evenProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("even", AnimationPropertiesKeys.COLOR_PROPERTY));
    evenProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        props.get("even", AnimationPropertiesKeys.LABEL_PROPERTY));
    evenProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true); // diese
                                                                       // Eigentschaft
                                                                       // ist
                                                                       // fest
                                                                       // kodiert
    evenMarker = lang.newArrayMarker(arr, 0, "even", null, evenProperties);
    evenMarker.hide();

    // evenMarkerProperties uebernehmen
    plus1Properties = new ArrayMarkerProperties();
    plus1Properties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("plus1", AnimationPropertiesKeys.COLOR_PROPERTY));
    plus1Properties.set(AnimationPropertiesKeys.LABEL_PROPERTY, ""); // diese
                                                                     // Eigentschaft
                                                                     // ist fest
                                                                     // kodiert
    plus1Properties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true); // diese
                                                                        // Eigentschaft
                                                                        // ist
                                                                        // fest
                                                                        // kodiert
    plus1Marker = lang.newArrayMarker(arr, 0, "plus1", null, plus1Properties);
    plus1Marker.hide();

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

    sort();
    return lang.toString();
  }

  public void makeCode() {
    code.addCodeLine("public static void sortiere(int[] list) {", null, 0, null);
    code.addCodeLine("boolean sorted = false;", null, 1, null);
    code.addCodeLine("while (!sorted) {", null, 1, null);
    code.addCodeLine("sorted = true;", null, 2, null);
    code.addCodeLine("//odd-even", null, 2, null);
    code.addCodeLine("for (int odd = 1; odd < list.length-1; odd += 2) {",
        null, 2, null);
    code.addCodeLine("if (list[odd] > list[odd+1]) {", null, 3, null);
    code.addCodeLine("int temp = list[odd];", null, 4, null);
    code.addCodeLine("list[odd] = list[odd+1];", null, 4, null);
    code.addCodeLine("list[odd+1] = temp;", null, 4, null);
    code.addCodeLine("sorted = false;", null, 4, null);
    code.addCodeLine("}", null, 3, null);
    code.addCodeLine("}", null, 2, null);
    code.addCodeLine("//even odd", null, 2, null);
    code.addCodeLine("for (int even = 0; even < list.length-1; even += 2) {",
        null, 2, null);
    code.addCodeLine("if (list[even] > list[even+1]) {", null, 3, null);
    code.addCodeLine("int temp = list[even]; ", null, 4, null);
    code.addCodeLine("list[even] = list[even+1];", null, 4, null);
    code.addCodeLine("list[even+1] = temp;", null, 4, null);
    code.addCodeLine("sorted = false;", null, 4, null);
    code.addCodeLine("}", null, 3, null);
    code.addCodeLine("}", null, 2, null);
    code.addCodeLine("}", null, 1, null);
    code.addCodeLine("}", null, 0, null);
  }

  public void goToWhile() {
    code.unhighlight(3);
    code.highlight(3, 0, true);
    code.highlight(21, 0, true);
  }

  public void goFromWhile() {
    code.unhighlight(3);
    code.unhighlight(21);
  }

  public void goToFor1() {
    code.highlight(5, 0, true);
    code.highlight(12, 0, true);
  }

  public void goFromFor1() {
    code.unhighlight(5);
    code.unhighlight(12);
  }

  public void goToFor2() {
    code.highlight(13, 0, true);
    code.highlight(20, 0, true);
  }

  public void goFromFor2() {
    code.unhighlight(13);
    code.unhighlight(20);
  }

  @Override
  public void init() {
    lang = new AnimalScript(getName(), getAnimationAuthor(), 640, 480);
    lang.setStepMode(true);

    // Header anlegen (dieser Block ist fest kodiert)
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    TextProperties headerProperties = new TextProperties();
    headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30), "Even-Odd-Sort",
        "header", null, headerProperties);
    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "hRect", null,
        rectProperties);
    lang.nextStep();
  }

  public void sort() {

    code.highlight(0);
    lang.nextStep();

    boolean sorted = false;
    code.unhighlight(0);
    code.highlight(1);
    lang.nextStep();

    code.unhighlight(1);
    code.highlight(2);
    lang.nextStep();

    code.unhighlight(2);
    code.highlight(3);
    lang.nextStep();

    while (!sorted) {
      goToWhile();
      sorted = true;
      code.highlight(4);
      lang.nextStep();

      code.unhighlight(4);
      code.highlight(5);
      lang.nextStep();

      oddMarker.show();
      for (int odd = 1; odd < arrayData.length - 1; odd += 2) {
        oddMarker.move(odd, null, new MsTiming(100));
        lang.nextStep();

        goToFor1();
        code.highlight(6);
        lang.nextStep();

        arr.highlightElem(odd, null, null);
        lang.nextStep();

        plus1Marker.show();
        plus1Marker.move(odd + 1, null, new MsTiming(100));
        lang.nextStep();

        arr.highlightElem(odd + 1, null, null);
        lang.nextStep();

        if (arr.getData(odd) > arr.getData(odd + 1)) {
          code.unhighlight(6);
          code.highlight(6, 0, true);
          code.highlight(7);
          code.highlight(8);
          code.highlight(9);
          code.highlight(11, 0, true);
          lang.nextStep();

          CheckpointUtils.checkpointEvent(this, "oddSwap", new Variable("ele1",
              arr.getData(odd)), new Variable("ele2", arr.getData(odd + 1)));
          arr.swap(odd, odd + 1, null, new MsTiming(1000));
          arr.unhighlightElem(odd, null, null);
          arr.unhighlightElem(odd + 1, null, null);
          lang.nextStep();

          code.unhighlight(7);
          code.unhighlight(8);
          code.unhighlight(9);
          code.highlight(10);
          lang.nextStep();
        }

        plus1Marker.hide();
        code.unhighlight(10);
        code.unhighlight(6);
        code.unhighlight(11);
        arr.unhighlightElem(odd, null, null);
        arr.unhighlightElem(odd + 1, null, null);
        goFromFor1();
        code.highlight(5);
        lang.nextStep();
      }
      oddMarker.move(0, null, new MsTiming(1000));
      plus1Marker.move(0, null, new MsTiming(1000));
      oddMarker.hide();
      goFromFor1();

      // zweite for-schleife
      code.highlight(13);
      lang.nextStep();

      evenMarker.show();
      for (int even = 0; even < arrayData.length - 1; even += 2) {
        evenMarker.move(even, null, new MsTiming(100));
        lang.nextStep();

        goToFor2();
        code.highlight(14);
        lang.nextStep();

        arr.highlightElem(even, null, null);
        lang.nextStep();

        plus1Marker.show();
        plus1Marker.move(even + 1, null, new MsTiming(100));
        lang.nextStep();

        arr.highlightElem(even + 1, null, null);
        lang.nextStep();

        if (arr.getData(even) > arr.getData(even + 1)) {
          code.unhighlight(14);
          code.highlight(14, 0, true);
          code.highlight(15);
          code.highlight(16);
          code.highlight(17);
          code.highlight(19, 0, true);
          lang.nextStep();

          CheckpointUtils.checkpointEvent(this, "evenSwap", new Variable(
              "ele1", arr.getData(even)),
              new Variable("ele2", arr.getData(even + 1)));
          arr.swap(even, even + 1, null, new MsTiming(1000));
          arr.unhighlightElem(even, null, null);
          arr.unhighlightElem(even + 1, null, null);
          lang.nextStep();

          sorted = false;
          code.unhighlight(15);
          code.unhighlight(16);
          code.unhighlight(17);
          code.highlight(18);
          lang.nextStep();
        }
        plus1Marker.hide();
        code.unhighlight(18);
        code.unhighlight(14);
        code.unhighlight(19);
        arr.unhighlightElem(even, null, null);
        arr.unhighlightElem(even + 1, null, null);
        goFromFor2();
        code.highlight(13);
        lang.nextStep();
      }
      code.unhighlight(13);
      evenMarker.hide();
      evenMarker.move(0, null, null);
      plus1Marker.hide();
      plus1Marker.move(0, null, null);
    }
    goFromWhile();
    lang.nextStep();
    // ARRAY IS SORTED
    arr.highlightCell(0, arr.getLength() - 1, null, null);
    lang.nextStep();
  }

  @Override
  public String getAlgorithmName() {
    return "Odd-Even-Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Hlib Levitin";
  }

  @Override
  public String getCodeExample() {
    String code = "public static void sortiere(int[] list) {\n"
        + "  boolean sorted = false;\n" + "  while (!sorted){\n"
        + "    sorted = true;\n" + "    //odd-even\n"
        + "    for (int odd = 1; odd &lt; list.length-1; odd += 2) {\n"
        + "      if (list[odd] &gt; list[odd+1]) {\n"
        + "        int temp = list[odd];\n"
        + "        list[odd] = list[odd+1];\n"
        + "        list[odd+1] = temp;\n" + "        sorted = false;\n"
        + "      }\n" + "    }\n" + "    //even odd\n"
        + "    for (int even = 0; even &lt; list.length-1; even += 2) {\n"
        + "      if (list[even] &gt; list[even+1]) {\n"
        + "        int temp = list[even];\n"
        + "        list[even] = list[even+1];\n"
        + "        list[even+1] = temp;\n" + "        sorted = false;\n"
        + "      }\n" + "    }\n" + "  }\n" + "}\n";
    return code;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Die Effizienz von Even-Odd-Sort wird auf den Mehrkernprozessoren gesteigert,\n"
        + "indem jeder Prozessor einen normalen BubbleSort ausf&uuml;hrt, startet jedoch an\n"
        + "unteschiedlichen Stellen in der Liste. Durch diese Strategie nutzt man einen\n"
        + "superschnellen Cache jedes Prozessors x-mal effektiver, was besonders sinnvoll ist,\n"
        + "wenn man mit langen Listen und komplexen Objekten arbeitet.";
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
    return "Even-Odd-Sort(DE)";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}