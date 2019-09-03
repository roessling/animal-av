package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedIntArrayAlgorithm;

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
import algoanim.util.TicksTiming;

public class CountingSortAnimation extends AnimatedIntArrayAlgorithm implements
    Generator {

  private int                          textCounter       = 0;

  private Text                         t5;

  private Language                     language;
  private static ArrayProperties       arrProp;
  private static SourceCodeProperties  sc_prop;
  private static ArrayMarkerProperties i_zeiger;
  private static TextProperties        tp1, tp2, tp3, tp4;
  private SourceCode                   sc;

  static int[]                         einArray;

  private static final String          CODE_BESCHREIBUNG = "public static void countingSort (int [] array)\n"
                                                             + "{"
                                                             + "	int maximum = array[0];"

                                                             + "	for(int i = 1; i < array.length; i++)"
                                                             + "	{"
                                                             + "		if(array[i] > maximum)"
                                                             + "	    {"
                                                             + "			maximum = array[i];"
                                                             + "		}"
                                                             + "	}"

                                                             + "	int[] count= new int[maximum+1];"

                                                             + "	int[] result= new int[array.length];"

                                                             + "	for(int i=0; i < array.length; i++)"
                                                             + "	{"
                                                             + "		count[ array[i] ] = count[ array[i] ]+1;"
                                                             + "	}"

                                                             + "	for(int j=1; j <= maximum; j++)"
                                                             + "	{"
                                                             + "		count[ j ] = count[ j ] + count[ j-1 ];"
                                                             + "	}"

                                                             + "	for(int i = array.length-1; i >= 0; i--)"
                                                             + "	{"
                                                             + "		result[ count[ array[i] ] -1 ] = array[i];"
                                                             + "		count[ array[i] ] = count[ array[i] ] -1;"
                                                             + "	}" + "}";

  public static final String           ALGO_BESCHREIBUNG = "COUNTINGSORT\n"
                                                             + "Beschreibung des Algorithmus\n"
                                                             + "1. Bestimme für jeden Wert x aus A die Anzahl p von Werten aus A, die kleiner oder gleich x sind.\n"
                                                             + "2. Speichere x (beginnend mit dem letzten Element aus A) im Ergebnisarray an Position p+l und dekrementiere p.";

  public void sort(int[] liste) {

    // *************** Beschreibung des Algorithmus im Schrift
    // *************************

    Text t0 = language.newText(new Coordinates(150, 20), "COUNTINGSORT", "f1"
        + textCounter, null, tp1);
    textCounter++;
    language.nextStep();

    Text t1 = language.newText(new Coordinates(150, 60),
        "Beschreibung des Algorithmus", "f1" + textCounter, null, tp2);
    textCounter++;
    language.nextStep();

    Text t2 = language
        .newText(
            new Coordinates(20, 120),
            "1. Bestimme für jeden Wert x aus A die Anzahl p von Werten aus A, die kleiner oder gleich x sind.",
            "f1" + textCounter, null, tp3);
    textCounter++;
    language.nextStep();

    Text t3 = language
        .newText(
            new Coordinates(20, 160),
            "2. Speichere x (beginnend mit dem letzten Element aus A) im Ergebnisarray an Position p+l",
            "f1" + textCounter, null, tp3);
    textCounter++;
    language.nextStep();

    Text t4 = language.newText(new Coordinates(20, 200), "3. Dekrementiere p.",
        "f1" + textCounter, null, tp3);
    textCounter++;
    language.nextStep();

    t0.hide();
    t1.hide();
    t2.hide();
    t3.hide();
    t4.hide();

    language.nextStep();

    // *********************************************************************************

    language.newText(new Coordinates(150, 20),
        "CountingSort Animationsbeispiel", "f1" + textCounter, null, tp4);
    t5 = language.newText(new Coordinates(20, 100), "Array",
        "f1" + textCounter, null, tp3);

    IntArray array = language.newIntArray(new Coordinates(80, 120), liste,
        "array", null, arrProp);

    sc = language
        .newSourceCode(new Coordinates(20, 150), "code", null, sc_prop);
    // Zeilen im SourceCode hinzufügen
    sc.addCodeLine("public static void countingSort (int [] array)", null, 0,
        null);
    sc.addCodeLine("{", null, 0, null);
    sc.addCodeLine("int maximum = array[0];", null, 1, null);
    sc.addCodeLine("for(int i = 1; i < array.length; i++)", null, 1, null);
    sc.addCodeLine("{", null, 1, null);
    sc.addCodeLine("if(array[i] > maximum)", null, 2, null);
    sc.addCodeLine("{", null, 2, null);
    sc.addCodeLine("maximum = array[i];", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("int [] count = new int [maximum + 1];", null, 1, null);
    sc.addCodeLine("for(int i = 0; i < array.length; i++)", null, 1, null);
    sc.addCodeLine("{", null, 1, null);
    sc.addCodeLine("count[array[i]]++;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("for(int i = 1; i <= maximum; i++)", null, 1, null);
    sc.addCodeLine("{", null, 1, null);
    sc.addCodeLine("count[i] = count[i] + count[i-1];", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("int [] result = new int [array.length];", null, 1, null);
    sc.addCodeLine("for(int i = array.length-1; i >= 0; i--)", null, 1, null);
    sc.addCodeLine("{", null, 1, null);
    sc.addCodeLine("result[count[array[i]]-1] = array[i];", null, 2, null);
    sc.addCodeLine("count[array[i]] = count[array[i]] - 1;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    language.nextStep();

    countingSort(array, sc);
  }

  public void countingSort(IntArray array, SourceCode sourceCode) {

    int maxPosi = 0;
    sourceCode.highlight(0);

    language.nextStep();
    sourceCode.toggleHighlight(0, 0, false, 2, 0);
    array.highlightCell(0, null, null);

    int maximum = array.getData(0);

    language.nextStep();
    sourceCode.toggleHighlight(2, 0, false, 3, 0);

    ArrayMarker i_Pointer = language.newArrayMarker(array, 1, "i", null,
        i_zeiger);
    for (int i = 1; i < array.getLength(); i++) {

      i_Pointer.move(i, new TicksTiming(0), new TicksTiming(50));

      language.nextStep();
      sourceCode.toggleHighlight(3, 0, false, 5, 0);

      if (array.getData(i) > maximum) {

        language.nextStep();
        sourceCode.toggleHighlight(5, 0, false, 7, 0);

        maximum = array.getData(i);
        maxPosi = i;
        array.unhighlightCell(0, i, null, null);
        array.highlightCell(i, new TicksTiming(0), new TicksTiming(50));

        language.nextStep();
        sourceCode.toggleHighlight(7, 0, false, 3, 0);

      } else {
        language.nextStep();
        sourceCode.toggleHighlight(5, 0, false, 3, 0);
      }
    }

    language.nextStep();
    i_Pointer.hide();

    sourceCode.toggleHighlight(3, 0, false, 10, 0);

    int[] count = new int[maximum + 1];

    language.nextStep();
    Text t6 = language.newText(new Coordinates(250, 120), "Count", "f1"
        + textCounter, null, tp3);
    IntArray array1 = language.newIntArray(new Coordinates(310, 120), count,
        "array", null, arrProp);
    array.unhighlightCell(maxPosi, null, null);

    language.nextStep();
    sourceCode.toggleHighlight(10, 0, false, 11, 0);
    i_Pointer = language.newArrayMarker(array, 0, "i", null, i_zeiger);

    for (int i = 0; i < array.getLength(); i++) {
      i_Pointer.move(i, new TicksTiming(0), new TicksTiming(50));
      int index = array.getData(i);
      int value = count[index] + 1;
      language.nextStep();
      sourceCode.toggleHighlight(11, 0, false, 13, 0);
      count[index] = value;
      array1.put(index, value, new TicksTiming(0), new TicksTiming(50));
      array1.highlightCell(index, null, null);
      language.nextStep();
      sourceCode.toggleHighlight(13, 0, false, 11, 0);
      array1.unhighlightCell(index, null, null);
    }
    language.nextStep();
    i_Pointer.hide();
    sourceCode.toggleHighlight(11, 0, false, 15, 0);
    i_Pointer = language.newArrayMarker(array1, 1, "i", null, i_zeiger);

    for (int i = 1; i <= maximum; i++) {
      int value = count[i] + count[i - 1];
      i_Pointer.move(i, new TicksTiming(0), new TicksTiming(50));
      language.nextStep();
      sourceCode.toggleHighlight(15, 0, false, 17, 0);
      count[i] = value;
      array1.put(i, value, new TicksTiming(0), new TicksTiming(50));
      array1.highlightCell(i, null, null);

      language.nextStep();
      sourceCode.toggleHighlight(17, 0, false, 15, 0);
      array1.unhighlightCell(i, null, null);
    }

    language.nextStep();
    i_Pointer.hide();
    sourceCode.toggleHighlight(15, 0, false, 19, 0);

    int[] result = new int[array.getLength()];

    language.nextStep();
    language.newText(new Coordinates(450, 120), "Result", "f1" + textCounter,
        null, tp3);
    IntArray array2 = language.newIntArray(new Coordinates(510, 120), result,
        "array", null, arrProp);

    language.nextStep();
    sourceCode.toggleHighlight(19, 0, false, 20, 0);
    i_Pointer = language.newArrayMarker(array2, array2.getLength(), "i", null,
        i_zeiger);
    for (int i = array.getLength() - 1; i >= 0; i--) {
      i_Pointer.move(i, new TicksTiming(0), new TicksTiming(50));
      language.nextStep();
      sourceCode.toggleHighlight(20, 0, false, 22, 0);
      int index1 = array.getData(i);
      int index2 = count[index1];
      int index3 = index2 - 1;
      int value = array.getData(i);

      result[index3] = value;
      array2.put(index3, value, new TicksTiming(0), new TicksTiming(50));
      array2.highlightCell(index3, null, null);
      language.nextStep();
      sourceCode.toggleHighlight(22, 0, false, 23, 0);

      int value1 = count[index1] - 1;
      count[index1] = value1;
      array1.put(index1, value1, new TicksTiming(0), new TicksTiming(50));
      array1.highlightCell(index1, null, null);
      language.nextStep();
      sourceCode.toggleHighlight(23, 0, false, 20, 0);
    }

    language.nextStep();
    i_Pointer.hide();
    sourceCode.unhighlight(20);

    language.nextStep();
    sourceCode.hide();
    t5.hide();
    t6.hide();
    array.hide();
    array1.hide();
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD,
        40));
    language.newText(new Coordinates(150, 300), "FERTIG !", "f2", null, tp);
  }

  public static void main(String args[]) {

    CountingSortAnimation countingSort = new CountingSortAnimation();

    countingSort.init();

    int[] myArray = new int[] { 2, 5, 3, 0, 2, 3, 0, 3 };

    countingSort.sort(myArray);

    System.out.println(countingSort.language.toString());
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    init();

    int[] arrayData = (int[]) primitives.get("array");

    arrProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("array", AnimationPropertiesKeys.COLOR_PROPERTY));
    arrProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        props.get("array", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
    arrProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        props.get("array", AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
    arrProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        props.get("array", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
    arrProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
        props.get("array", AnimationPropertiesKeys.FILL_PROPERTY));

    i_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("i_Pointer", AnimationPropertiesKeys.COLOR_PROPERTY));
    i_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        props.get("i_Pointer", AnimationPropertiesKeys.LABEL_PROPERTY));

    sc_prop.set(AnimationPropertiesKeys.BOLD_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.BOLD_PROPERTY));
    sc_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
    sc_prop.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY));
    sc_prop.set(AnimationPropertiesKeys.FONT_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.FONT_PROPERTY));
    sc_prop.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, props.get(
        "sourceCode", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
    sc_prop.set(AnimationPropertiesKeys.SIZE_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.SIZE_PROPERTY));

    sort(arrayData);

    return language.toString();
  }

  public void init() {

    language = new AnimalScript("CountingSort", "Paulin Nguimdoh", 640, 480);

    language.setStepMode(true);

    sc_prop = new SourceCodeProperties();
    sc_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    sc_prop.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    sc_prop.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.cyan);
    sc_prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));

    arrProp = new ArrayProperties();
    arrProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    arrProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.gray);
    arrProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.black);
    arrProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.white);
    arrProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.green);

    i_zeiger = new ArrayMarkerProperties();
    i_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    i_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);

    tp1 = new TextProperties();
    tp1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    tp1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD,
        35));

    tp2 = new TextProperties();
    tp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    tp2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD,
        24));

    tp3 = new TextProperties();
    tp3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    tp3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.PLAIN, 20));

    tp4 = new TextProperties();
    tp4.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    tp4.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD,
        35));

  }

  public String getAnimationAuthor() {

    return "Paulin Nguimdoh";
  }

  @Override
  public String getAlgorithmName() {

    return "Counting Sort";
  }

  @Override
  public String getCodeExample() {

    return CODE_BESCHREIBUNG;
  }

  @Override
  public Locale getContentLocale() {

    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {

    return ALGO_BESCHREIBUNG;
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

    return "Counting Sort Animation";
  }

  @Override
  public String getOutputLanguage() {

    return Generator.JAVA_OUTPUT;
  }
}
