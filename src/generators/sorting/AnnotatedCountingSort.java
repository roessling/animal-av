package generators.sorting;

import generators.AnnotatedAlgorithm;
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
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AnnotatedCountingSort extends AnnotatedAlgorithm implements
    Generator {

  private int                         textCounter   = 0;
  private String                      vergleich     = "VERGLEICHE";
  private String                      zuweisung     = "ZUWEISUNGEN";

  private int[]                       arrayData;
  private IntArray                    array         = null;
  private IntArray                    array1        = null;
  private IntArray                    array2        = null;
  private ArrayMarker                 iMarker, mMarker;
  private Timing                      defaultTiming = new TicksTiming(100);

  private ArrayMarkerUpdater          amuI;

  private static ArrayProperties      arrProp;
  private static SourceCodeProperties sc_prop;
  private static ArrayMarkerProperties i_zeiger, max_zeiger;
  private static TextProperties        tp1, tp2, tp3, tp4, tp5;

  @Override
  public String getAnnotatedSrc() {
    return "int [] countingSort (int [] array) { 				@label(\"header\")\n"
        + " int i;													@label(\"vars_marker\") @declare(\"int\", \"i\")\n"
        + " int l;													@label(\"laenge\") @declare(\"int\", \"l\")\n"
        + " int maximum = array[0];									@label(\"maximum1\") @declare(\"int\", \"max\") @inc(\""
        + zuweisung
        + "\")\n"
        + " for(i = 1; 												@label(\"1_ForInit\") @set(\"i\", \"1\") @inc(\""
        + zuweisung
        + "\")\n"
        + "	i < array.length;										@label(\"1_ForComp\") @continue @inc(\""
        + vergleich
        + "\")\n"
        + "		i++) { 												@label(\"1_ForInc\") @continue @inc(\"i\") @inc(\""
        + zuweisung
        + "\")\n"
        + "			if(array[i] > maximum) {						@label(\"1_if\") @inc(\""
        + vergleich
        + "\")\n"
        + "				maximum = array[i]; 						@label(\"maximum2\") @inc(\""
        + zuweisung
        + "\")\n"
        + "			}												@label(\"1_ifEnd\")\n"
        + " }														@label(\"1_ForEnd\")\n"
        + " int[] count = new int[maximum+1];						@label(\"count1\") @inc(\""
        + zuweisung
        + "\")\n"
        + " for(i=0; 												@label(\"2_ForInit\") @set(\"i\", \"0\") @inc(\""
        + zuweisung
        + "\")\n"
        + "	i < array.length; 										@label(\"2_ForComp\") @continue @inc(\""
        + vergleich
        + "\")\n"
        + "i++){ 												@label(\"2_ForInc\") @continue @inc(\"i\") @inc(\""
        + zuweisung
        + "\")\n"
        + "			count[ array[i] ] = count[ array[i] ]+1;		@label(\"count2\") @inc(\""
        + zuweisung
        + "\")\n"
        + " }														@label(\"2_ForEnd\")\n"
        + " for(i=1; 												@label(\"3_ForInit\") @set(\"i\", \"1\") @inc(\""
        + zuweisung
        + "\")\n"
        + " 	i <= maximum;											@label(\"3_ForComp\") @continue @inc(\""
        + vergleich
        + "\")\n"
        + " 		i++){ 												@label(\"3_ForInc\") @continue @inc(\"i\") @inc(\""
        + zuweisung
        + "\")\n"
        + "			count[ i ] = count[ i ] + count[ i-1 ];			@label(\"count3\") @inc(\""
        + zuweisung
        + "\")\n"
        + " }														@label(\"3_ForEnd\")\n"
        + " int[] result= new int[array.length]; 					@label(\"result1\") @inc(\""
        + zuweisung
        + "\")\n"
        + " for(i = array.length-1;									@label(\"4_ForInit\") @inc(\""
        + zuweisung
        + "\")\n"
        + " 	i >= 0;													@label(\"4_ForComp\") @continue @inc(\""
        + vergleich
        + "\")\n"
        + " 		i--) { 												@label(\"4_ForInc\") @continue @dec(\"i\") @inc(\""
        + zuweisung
        + "\")\n"
        + "			result[ count[ array[i] ] -1 ] = array[i]; 		@label(\"result4\") @inc(\""
        + zuweisung
        + "\")\n"
        + "			count[ array[i] ] = count[ array[i] ] -1;		@label(\"count4\") @inc(\""
        + zuweisung
        + "\")\n"
        + " }														@label(\"4_ForEnd\")\n"
        + " return result;											@label(\"return\")\n"
        + "}															@label(\"end\")\n";
  }

  public void algoDescription() {

    // *************** Beschreibung des Algorithmus im Schrift
    // *************************

    Text t0 = lang.newText(new Coordinates(150, 20), "COUNTINGSORT", "f1"
        + textCounter, null, tp1);
    textCounter++;
    lang.nextStep();

    Text t1 = lang.newText(new Coordinates(150, 60),
        "Beschreibung des Algorithmus", "f1" + textCounter, null, tp2);
    textCounter++;
    lang.nextStep();

    Text t2 = lang
        .newText(
            new Coordinates(20, 120),
            "1. Bestimme für jeden Wert x aus A die Anzahl p von Werten aus A, die kleiner oder gleich x sind.",
            "f1" + textCounter, null, tp3);
    textCounter++;
    lang.nextStep();

    Text t3 = lang
        .newText(
            new Coordinates(20, 160),
            "2. Speichere x (beginnend mit dem letzten Element aus A) im Ergebnisarray an Position p+l",
            "f1" + textCounter, null, tp3);
    textCounter++;
    lang.nextStep();

    Text t4 = lang.newText(new Coordinates(20, 200), "3. Dekrementiere p.",
        "f1" + textCounter, null, tp3);
    textCounter++;
    lang.nextStep();

    t0.hide();
    t1.hide();
    t2.hide();
    t3.hide();
    t4.hide();
  }

  public void sort() {
    lang.newText(new Coordinates(470, 110),
        "Gesucht wird das größte Element des Array", "f1" + textCounter, null,
        tp5);
    exec("header");
    lang.nextStep();

    exec("vars_marker");
    amuI.setVariable(vars.getVariable("i"));
    lang.nextStep();

    exec("laenge");
    vars.set("l", String.valueOf(array.getLength()));
    lang.nextStep();

    exec("maximum1");
    mMarker = lang.newArrayMarker(array, 0, "mMarker", null, max_zeiger);
    vars.set("max", String.valueOf(array.getData(0)));
    // int max_index = 0;
    // mMarker.move(0, null, null);
    array.highlightElem(0, null, null);
    array.highlightCell(0, null, null);
    lang.nextStep();

    exec("1_ForInit");
    array.highlightElem(Integer.parseInt(vars.get("i")), null, null);
    lang.nextStep();

    exec("1_ForComp");
    lang.nextStep();

    while (Integer.parseInt(vars.get("i")) < Integer.parseInt(vars.get("l"))) {

      int i = Integer.parseInt(vars.get("i"));

      exec("1_if");
      lang.nextStep();

      int max = Integer.parseInt(vars.get("max"));

      if (array.getData(i) > max) {
        // max_index = i;
        exec("maximum2");
        vars.set("max", String.valueOf(array.getData(i)));
        array.unhighlightElem(0, i - 1, null, null);
        mMarker.move(i, null, null);
        array.highlightElem(i, null, null);
        array.unhighlightCell(0, array.getLength() - 1, null, null);
        array.highlightCell(i, null, null);
        lang.nextStep();
      } else {
        if (i == 1) {
          array.unhighlightElem(0, null, null);
        } else {
          array.unhighlightElem(i, null, null);
        }
      }

      exec("1_ForInc");
      array.highlightElem(Integer.parseInt(vars.get("i")), null, null);
      lang.nextStep();

      exec("1_ForComp");
      lang.nextStep();
    }
    // t5.hide();
    array.unhighlightElem(0, array.getLength() - 1, null, null);
    array.unhighlightCell(0, array.getLength() - 1, null, null);
    iMarker.hide();
    // array.highlightCell(Integer.parseInt(vars.get("max")), null, null);
    exec("count1");
    int[] count = new int[Integer.parseInt(vars.get("max")) + 1];
    Text t6 = lang.newText(new Coordinates(470, 280),
        "Die Häufigkeit jeder Zahl im Input-Array wird gezählt", "f1"
            + textCounter, null, tp5);
    lang.newText(new Coordinates(470, 340), "Count", "f1" + textCounter, null,
        tp3);
    array1 = lang.newIntArray(new Coordinates(560, 340), count, "array", null,
        arrProp);
    lang.nextStep();

    exec("2_ForInit");
    iMarker.show();
    lang.nextStep();

    exec("2_ForComp");
    lang.nextStep();

    while (Integer.parseInt(vars.get("i")) < Integer.parseInt(vars.get("l"))) {
      exec("count2");
      int index = array.getData(Integer.parseInt(vars.get("i")));
      array.highlightElem(Integer.parseInt(vars.get("i")), null, null);
      int value = count[index] + 1;
      count[index] = value;
      array1.put(index, value, null, null);
      array1.highlightElem(index, null, null);
      lang.nextStep();

      exec("2_ForInc");
      array.unhighlightElem(0, array.getLength() - 1, null, null);
      lang.nextStep();

      exec("2_ForComp");
      lang.nextStep();
    }
    t6.hide();
    array1.unhighlightElem(0, array1.getLength() - 1, null, null);
    iMarker.hide();
    lang.newText(new Coordinates(470, 280),
        "Ändern des Count-Arrays, um die Anzahl der Werte ",
        "f1" + textCounter, null, tp5);
    lang.newText(new Coordinates(470, 300),
        "kleiner gleich aktueller Index aus Input-Array.", "f1" + textCounter,
        null, tp5);

    exec("3_ForInit");
    iMarker.show();
    array1.highlightCell(0, null, null);
    lang.nextStep();

    exec("3_ForComp");
    lang.nextStep();

    while (Integer.parseInt(vars.get("i")) <= Integer.parseInt(vars.get("max"))) {
      exec("count3");
      int i = Integer.parseInt(vars.get("i"));
      int value = count[i] + count[i - 1];
      count[i] = value;
      array1.put(i, value, null, null);
      array1.highlightCell(i, null, null);
      lang.nextStep();

      exec("3_ForInc");
      lang.nextStep();

      exec("3_ForComp");
      lang.nextStep();
    }
    array1.unhighlightCell(0, array1.getLength() - 1, null, null);

    exec("result1");
    int[] result = new int[Integer.parseInt(vars.get("l"))];
    lang.newText(new Coordinates(470, 400),
        "Jede Zahl des Input-Array wird ins Output-Array geschrieben.", "f1"
            + textCounter, null, tp5);
    lang.newText(new Coordinates(470, 420),
        "Die Position wird dem Count-Array entnommen.", "f1" + textCounter,
        null, tp5);
    lang.newText(new Coordinates(470, 460), "Output", "f1" + textCounter, null,
        tp3);
    array2 = lang.newIntArray(new Coordinates(560, 460), result, "array", null,
        arrProp);
    lang.nextStep();

    exec("4_ForInit");
    vars.set("i", String.valueOf(array.getLength() - 1));
    lang.nextStep();

    exec("4_ForComp");
    lang.nextStep();

    while (Integer.parseInt(vars.get("i")) >= 0) {
      exec("result4");
      int index1 = array.getData(Integer.parseInt(vars.get("i")));
      array.highlightCell(Integer.parseInt(vars.get("i")), null, null);
      int index2 = count[index1];
      array1.highlightCell(index1, null, null);
      int index3 = index2 - 1;

      int value = array.getData(Integer.parseInt(vars.get("i")));
      result[index3] = value;
      array2.put(index3, value, null, null);
      array2.highlightCell(index3, null, null);

      lang.nextStep();
      array.unhighlightCell(0, array.getLength() - 1, null, null);
      array1.unhighlightCell(0, array1.getLength() - 1, null, null);
      // array1.unhighlightCell(index2, null, null);
      exec("count4");
      int value1 = count[index1] - 1;
      count[index1] = value1;
      array1.put(index1, value1, null, null);
      array1.highlightCell(index1, null, null);
      lang.nextStep();

      exec("4_ForInc");
      array1.unhighlightCell(0, array1.getLength() - 1, null, null);
      lang.nextStep();

      exec("4_ForComp");
      lang.nextStep();
    }

    array1.unhighlightCell(0, array1.getLength() - 1, null, null);
    iMarker.hide();

    exec("return");
    // highlight all cells on end if not
    for (int i = 0; i < array.getLength(); i++)
      array.highlightCell(i, null, null);
  }

  public static void main(String args[]) {

    AnnotatedCountingSort countingSort = new AnnotatedCountingSort();

    countingSort.arrayData = new int[] { 2, 5, 3, 0, 2, 6, 0, 3 };

    countingSort.init();

    countingSort.sort();

    System.out.println(countingSort.lang.toString());
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    arrayData = (int[]) primitives.get("array");

    init();

    sort();

    return lang.toString();
  }

  public void init() {

    super.init();

    lang = new AnimalScript("CountingSort", "Paulin Nguimdoh", 640, 480);

    lang.setStepMode(true);

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

    tp5 = new TextProperties();
    tp5.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    tp5.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.PLAIN, 18));

    algoDescription();

    sc_prop = new SourceCodeProperties();
    sc_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    sc_prop.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    sc_prop.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.cyan);
    sc_prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));
    sourceCode = lang.newSourceCode(new Coordinates(20, 100), "sumupCode",
        null, sc_prop);

    arrProp = new ArrayProperties();
    arrProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    arrProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
    arrProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.black);
    arrProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.white);
    arrProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.green);
    lang.newText(new Coordinates(470, 200), "Input", "f1" + textCounter, null,
        tp3);
    array = lang.newIntArray(new Coordinates(560, 200), arrayData, "array",
        null, arrProp);

    i_zeiger = new ArrayMarkerProperties();
    i_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    i_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    iMarker = lang.newArrayMarker(array, 0, "iMarker", null, i_zeiger);
    amuI = new ArrayMarkerUpdater(iMarker, null, defaultTiming,
        array.getLength() - 1);

    max_zeiger = new ArrayMarkerProperties();
    max_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "max");
    max_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.magenta);

    tp4 = new TextProperties();
    tp4.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    tp4.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD,
        24));

    // setup complexity
    vars.declare("int", vergleich);
    vars.setGlobal(vergleich);
    vars.declare("int", zuweisung);
    vars.setGlobal(zuweisung);

    Text text = lang.newText(new Coordinates(300, 20), "...", "complexity",
        null, tp4);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Vergleiche: ");
    tu.addToken(vars.getVariable(vergleich));
    tu.addToken(" - Zuweisungen: ");
    tu.addToken(vars.getVariable(zuweisung));
    tu.update();

    // parsing anwerfen
    parse();

  }

  public String getAnimationAuthor() {

    return "Paulin Nguimdoh";
  }

  @Override
  public String getAlgorithmName() {

    return "Counting Sort";
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
  public String getName() {

    return "Counting Sort Animation";
  }

  @Override
  public String getOutputLanguage() {

    return Generator.JAVA_OUTPUT;
  }

  @Override
  public String getDescription() {
    return "Hier handelt es sich um die Beschreibung des Counting Sort Algorithmus.";
  }
}
