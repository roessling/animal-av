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
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class CountingSort3 implements Generator {
  private Language        lang;
  private int[]           intArray;

  // my stuff
  private ArrayProperties arrayProps;
  private TextProperties  textProps, headlineProps, letterProps;
  private ArrayMarkerProperties am_i, am_j, am_a_i, am_c_a_i;
  private SourceCode            sc;
  private SourceCodeProperties  scProps;

  public void init() {
    lang = new AnimalScript("Counting Sort [DE]",
        "Marius Hornung, Jan Klostermann", 500, 500);

    // my stuff
    // Activate step control
    lang.setStepMode(true);

    // create properties with default values
    arrayProps = new ArrayProperties();
    textProps = new TextProperties();
    headlineProps = new TextProperties();
    letterProps = new TextProperties();
    scProps = new SourceCodeProperties();

    // Redefine properties: border red, filled with gray
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK); // color
                                                                                // black
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY); // fill
                                                                       // color
                                                                       // gray

    // first, set the visual properties for the source code

    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // Redefine text properties
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));

    letterProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));

    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 24));

    am_i = new ArrayMarkerProperties();
    am_i.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    am_i.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

    am_j = new ArrayMarkerProperties();
    am_j.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    am_j.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");

    am_a_i = new ArrayMarkerProperties();
    am_a_i.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    am_a_i.set(AnimationPropertiesKeys.LABEL_PROPERTY, "a[i]");

    am_c_a_i = new ArrayMarkerProperties();
    am_c_a_i.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    am_c_a_i.set(AnimationPropertiesKeys.LABEL_PROPERTY, "c[a[i]] - 1");

    // my stuff end
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    intArray = (int[]) primitives.get("intArray");

    arrayProps = (ArrayProperties) props.getPropertiesByName("userArrayProps");

    am_i = (ArrayMarkerProperties) props.getPropertiesByName("arrayMarker_i");
    am_j = (ArrayMarkerProperties) props.getPropertiesByName("arrayMarker_j");
    am_a_i = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker_a_i");
    am_c_a_i = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker_c_a_i");

    scProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");

    // my stuff
    countingSort(intArray);
    // my stuff end
    return lang.toString();
  }

  // my stuff
  public void countingSort(int[] a) {

    Timing defaultTiming = new TicksTiming(15);

    // Show description

    showSourceCode();

    lang.newText(new Coordinates(300, 30), "Counting Sort", "headline", null,
        headlineProps);

    // Show description
    Text description_1 = lang.newText(new Coordinates(20, 95),
        "Counting Sort ist ein einfaches Verfahren zur Sortierung", "line_1",
        null, textProps);
    Text description_2 = lang.newText(new Coordinates(20, 130),
        "von Integerzahlen. Es zaehlt zunaechst die Anzahl der", "line_2",
        null, textProps);
    Text description_3 = lang.newText(new Coordinates(20, 160),
        "unterschiedlichen Zahlen der Eingabe und berechnet daraus", "line_3",
        null, textProps);
    Text description_4 = lang.newText(new Coordinates(20, 190),
        "die entsprechende Position in der sortierten Ausgabe.", "line_4",
        null, textProps);
    Text description_5 = lang.newText(new Coordinates(20, 220),
        "Die Laufzeit von Counting Sort betraegt O(N + M),", "line_5", null,
        textProps);
    Text description_6 = lang.newText(new Coordinates(20, 250),
        "wobei N die Groesse des Eingabearrays und M die", "line_6", null,
        textProps);
    Text description_7 = lang.newText(new Coordinates(20, 280),
        "Anzahl jeder Zahl darstellt.", "line_7", null, textProps);

    lang.nextStep();
    description_1.hide();
    description_2.hide();
    description_3.hide();
    description_4.hide();
    description_5.hide();
    description_6.hide();
    description_7.hide();

    // description 1
    Text desc_1 = lang
        .newText(
            new Coordinates(50, 400),
            "Such den groessten Wert in A (max) und initialisiere ein Array C von [0] .. [max].",
            "desc_1", null, textProps);

    sc.highlight(0);

    lang.newText(new Coordinates(40, 95), "A", "caption_A", null, letterProps);

    IntArray array_a = lang.newIntArray(new Coordinates(60, 90), a, "A", null,
        arrayProps);
    lang.nextStep();

    lang.newText(new Coordinates(40, 215), "B", "caption_B", null, letterProps);
    // sorted array
    IntArray array_b = lang.newIntArray(new Coordinates(60, 210),
        new int[array_a.getLength()], "B", null, arrayProps);
    sc.toggleHighlight(0, 1);
    lang.nextStep();

    // get maximum value k of array a
    int max = getMax(a);
    sc.toggleHighlight(1, 2);

    lang.newText(new Coordinates(250, 130), "max = " + max, "max", null,
        textProps);

    lang.nextStep();

    lang.newText(new Coordinates(40, 335), "C", "caption_C", null, letterProps);
    // init new array of size k
    IntArray array_c = lang.newIntArray(new Coordinates(60, 330),
        new int[max + 1], "C", null, arrayProps);
    sc.toggleHighlight(2, 3);
    lang.nextStep();
    sc.unhighlight(3);
    /*
     * // init entries with 0 for (int i = 0; i < c.length; i++) { c[i] = 0; }
     */
    desc_1.hide();
    Text desc_2 = lang.newText(new Coordinates(50, 400),
        "Zaehle alle Elemente die gleich i sind.", "desc_2", null, textProps);

    ArrayMarker i = lang.newArrayMarker(array_a, 0, "i", null, am_i);
    ArrayMarker a_i = lang.newArrayMarker(array_c, 0, "a_i", null, am_a_i);

    // count each element in a
    for (; i.getPosition() < array_a.getLength(); i.increment(null,
        defaultTiming)) {
      sc.highlight(4);
      a_i.move(array_a.getData(i.getPosition()), null, defaultTiming);
      lang.nextStep();
      array_c.put(array_a.getData(i.getPosition()),
          array_c.getData(array_a.getData(i.getPosition())) + 1, null,
          defaultTiming);
      // ArrayMarker
      sc.toggleHighlight(4, 5);
      lang.nextStep();
      sc.unhighlight(5);
    }

    sc.unhighlight(4);

    i.hide();
    a_i.hide();

    ArrayMarker j = lang.newArrayMarker(array_c, 1, "j", null, am_j);

    desc_2.hide();
    Text desc_3 = lang
        .newText(
            new Coordinates(50, 400),
            "Add. die Anzahl der Vorgaenger zur eigenen Anzahl, um die Arrayposition im sortierten Array herauszufinden.",
            "desc_3", null, textProps);

    // add to each predecessor
    for (; j.getPosition() < array_c.getLength(); j.increment(null,
        defaultTiming)) {
      sc.highlight(7);
      lang.nextStep();
      array_c.put(
          j.getPosition(),
          array_c.getData(j.getPosition())
              + array_c.getData(j.getPosition() - 1), null, defaultTiming);
      sc.toggleHighlight(7, 8);
      lang.nextStep();
      sc.unhighlight(8);
    }
    sc.unhighlight(7);
    j.hide();

    i = lang.newArrayMarker(array_a, array_a.getLength() - 1, "i", null, am_i);
    ArrayMarker c_a_i = lang
        .newArrayMarker(array_b, 0, "c_a_i", null, am_c_a_i);

    a_i = lang.newArrayMarker(array_c, 0, "a_i", null, am_a_i);

    desc_3.hide();
    Text desc_4 = lang.newText(new Coordinates(50, 400),
        "Fuege die Zahlen an ihrer sortierten Array-Position ein.", "desc_4",
        null, textProps);

    // sort
    for (; i.getPosition() >= 0; i.decrement(null, defaultTiming)) {
      sc.highlight(10);
      c_a_i.move(array_c.getData(array_a.getData(i.getPosition())) - 1, null,
          defaultTiming);
      a_i.move(array_a.getData(i.getPosition()), null, defaultTiming);
      lang.nextStep();

      array_b.put(array_c.getData(array_a.getData(i.getPosition())) - 1,
          array_a.getData(i.getPosition()), null, defaultTiming);
      sc.toggleHighlight(10, 11);
      lang.nextStep();

      sc.toggleHighlight(11, 12);
      array_c.put(array_a.getData(i.getPosition()),
          array_c.getData(array_a.getData(i.getPosition())) - 1, null,
          defaultTiming);
      lang.nextStep();
      sc.unhighlight(12);
    }
    i.hide();
    a_i.hide();
    c_a_i.hide();

    desc_4.hide();
    lang.newText(new Coordinates(50, 400),
        "Array B ist nun eine sortierte Permutation von Array A.", "desc_5",
        null, textProps);

  }

  public int getMax(int[] input) {

    int max = input[0];

    for (int i = 1; i < input.length; i++) {

      if (input[i] > max)
        max = input[i];

    }

    return max;
  }

  public void showSourceCode() {

    // now, create the source code entity
    sc = lang.newSourceCode(new Coordinates(430, 90), "sourceCode", null,
        scProps);
    // add a code line
    // parameters: code itself; name (can be null); indentation level; display
    // options
    sc.addCodeLine("public void countingSort(int[] a) {", null, 0, null);
    sc.addCodeLine("int[] b = new int[a.length];", null, 1, null);
    sc.addCodeLine("int max = getMax(a);", null, 1, null);
    sc.addCodeLine("int[] c = new int[max + 1];", null, 1, null);
    sc.addCodeLine("for (int i = 0; i < a.length; i++) {", null, 1, null);
    sc.addCodeLine("c[a[i]] = c[a[i]] + 1;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("for (int j = 1; j < c.length; j++) {", null, 1, null);
    sc.addCodeLine("c[j] = c[j] + c[j - 1];", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("for (int i = a.length - 1; i >= 0; i--) {", null, 1, null);
    sc.addCodeLine("b[c[a[i]] - 1] = a[i];", null, 2, null);
    sc.addCodeLine("c[a[i]] = c[a[i]] - 1;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

  }

  // my stuff end

  public String getName() {
    return "Counting Sort [DE]";
  }

  public String getAlgorithmName() {
    return "Counting Sort";
  }

  public String getAnimationAuthor() {
    return "Marius Hornung, Jan Klostermann";
  }

  public String getDescription() {
    return "Counting Sort ist ein einfaches Verfahren zur Sortierung von Integerzahlen. Es z&auml;hlt zun&auml; "
        + "\n"
        + "die Anzahl der unterschiedlichen Zahlen der Eingabe und berechnet daraus die entsprechende "
        + "\n"
        + "Position in der sortierten Ausgabe. Der Algorithmus ben&ouml;tigt drei Schleifen, um zum "
        + "\n"
        + "sortierten Ergebnis zu gelangen:<br>"
        + "\n"
        + "<br>"
        + "\n"
        + "<ul>"
        + "\n"
        + "<li><b>Schleife 1:</b> Z&auml;hle alle Vorkommen der Elemente aus der Eingabe und trage "
        + "\n"
        + "diese in ein neues Array mit L&auml;nge gleich des Maximums der Eingabe.</li>"
        + "\n"
        + "<li><b>Schleife 2:</b> Addiere im neu erstellten Array die eingetragenen H&auml;ufigkeiten auf.</li>"
        + "\n"
        + "<li><b>Schleife 3:</b> Berechne aus diesem Array die endg&uuml;ltige Position der Zahlen aus der "
        + "\n"
        + "Eingabe im sortierten Ausgabearray.<br>"
        + "\n"
        + "<br>"
        + "\n"
        + "Im Gegensatz zu anderen bekannteren Sortieralgorithmen "
        + "\n"
        + "arbeitet Counting Sort demnach nicht mit Hilfe von Vergleichen.";
  }

  public String getCodeExample() {
    return "public void countingSort(int[] a) {" + "\n"
        + "    int[] b = new int[a.length];" + "\n"
        + "    int max = getMax(a);" + "\n" + "    int[] c = new int[max + 1];"
        + "\n" + "    for (int i = 0; i < a.length; i++) {" + "\n"
        + "        c[a[i]] = c[a[i]] + 1;" + "\n" + "    }" + "\n"
        + "    for (int j = 1; j < c.length; j++) {" + "\n"
        + "        c[j] = c[j] + c[j - 1];" + "\n" + "    }" + "\n"
        + "    for (int i = a.length - 1; i >= 0; i--) {" + "\n"
        + "        b[c[a[i]] - 1] = a[i];" + "\n"
        + "        c[a[i]] = c[a[i]] - 1;" + "\n" + "    }" + "\n" + "}";
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