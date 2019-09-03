package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

public class SiebDesAtkin implements ValidatingGenerator {
  private TextProperties       textProps;
  private TextProperties       HeadTextProps;
  private Language             lang;
  private SourceCodeProperties scProps;
  private ArrayProperties      arrayProps;
  private ArrayProperties      arrayPropsPrim;
  private ArrayProperties      arrayPropsNichtPrim;

  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {

    int N = (Integer) primitives.get("AnzZahlen");
    if (N <= 0) {
      throw new IllegalArgumentException(
          "ERROR: The given variable must be a positive number and unequal zero!");
    }
    if (N > 200) {
      throw new IllegalArgumentException(
          "ERROR: The given variable must not exceed 200!");
    }
    return true;
  }

  public void init() {
    lang = new AnimalScript("Sieb des Atkin",
        "Stephan Wezorke und Karsten Will", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    int N = (Integer) primitives.get("AnzZahlen");
    scProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    arrayPropsNichtPrim = (ArrayProperties) props
        .getPropertiesByName("arrayPrim");
    arrayProps = (ArrayProperties) props.getPropertiesByName("array");
    arrayPropsPrim = (ArrayProperties) props
        .getPropertiesByName("arrayNotPrim");
    textProps = (TextProperties) props.getPropertiesByName("text");
    HeadTextProps = (TextProperties) props.getPropertiesByName("head");
    Sieb(N);
    return lang.toString();
  }

  public String getName() {
    return "Sieb des Atkin";
  }

  public String getAlgorithmName() {
    return "Sieb des Atkin";
  }

  public String getAnimationAuthor() {
    return "Stephan Wezorke, Karsten Will";
  }

  public String getDescription() {
    return "Das Sieb von Atkin ist ein schneller, moderner Algorithmus zur Bestimmung aller Primzahlen"
        + "\n"
        + "bis zu einer vorgegebenen Grenze. Es ist eine optimierte Version des antiken Sieb des Eratosthenes: "
        + "\n"
        + "Das Atkinsieb leistet einige Vorarbeit und streicht dann alle Vielfachen von Primzahlquadraten. "
        + "\n"
        + "Es wurde von A. O. L. Atkin und Daniel J. Bernstein entwickelt."
        + "\n"
        + "\n"
        + "Im Folgenden bedeutet Invertieren eines Eintrags der Siebliste, dass dessen Markierung (prim oder nicht-prim) zum Gegenteil gewechselt wird.";
  }

  public String getCodeExample() {
    return "1) Erstelle eine mit 2, 3 und 5 gef&uuml;llte Ergebnisliste."
        + "\n"
        + "2) Erstelle eine Siebliste mit einem Eintrag f&uuml;r jede positive ganze Zahl; alle Eintr&auml;ge dieser Liste werden am Anfang als nicht-prim markiert."
        + "\n"
        + "3) Für jeden Eintrag n in der Siebliste f&uuml;hre folgendes aus:"
        + "\n"
        + "	a) Falls der Eintrag eine Zahl mit Rest 1, 13, 17, 29, 37, 41, 49, oder 53 enth&auml;lt, invertiere ihn f&uuml;r jede m&ouml;gliche L&ouml;sung der Gleichung:   4x^2 + y^2 = n."
        + "\n"
        + "	b) Falls der Eintrag eine Zahl mit Rest 7, 19, 31, oder 43 enth&auml;lt, invertiere ihn f&uuml;r jede m&ouml;gliche L&ouml;sung der Gleichung:   3x^2 + y^2 = n."
        + "\n"
        + "	c) Falls der Eintrag eine Zahl mit Rest 11, 23, 47, oder 59 enth&auml;lt, invertiere ihn f&uuml;r jede m&ouml;gliche L&ouml;sung der Gleichung:   3x^2 - y^2 = n, wobei   x > y."
        + "\n"
        + "4) Beginne mit der niedrigsten Zahl in der Siebliste."
        + "\n"
        + "5) Nimm die n&auml;chste Zahl in der Siebliste, die immer noch als prim markiert ist."
        + "\n"
        + "6) F&uuml;ge die Zahl in die Ergebnisliste ein."
        + "\n"
        + "7) Quadriere die Zahl und markiere alle Vielfachen von diesem Quadrat als nicht-prim."
        + "\n" + "8) Wiederhole die Schritte 5 bis 8.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  // Berechnet #L&ouml;sungen von 4x^2 + y^2 = n f&uuml;r gegebenes n und int
  // x,y
  private static int helper1(int n) {
    int i = 0; // Z&auml;hler
    for (int x = 1; x <= Math.sqrt(n) / 2; x++) {
      for (int y = 1; y <= Math.sqrt(n); y++) {
        if (4 * x * x + y * y == n)
          i++;
      }
    }
    return i;
  }

  // Berechnet #L&ouml;sungen von 3x^2 + y^2 = n f&uuml;r gegebenes n und int
  // x,y
  private static int helper2(int n) {
    int i = 0; // Z&auml;hler
    for (int x = 1; x <= Math.sqrt(n / 3.0); x++) {
      for (int y = 1; y <= Math.sqrt(n); y++) {
        if (3 * x * x + y * y == n)
          i++;
      }
    }
    return i;
  }

  // Berechnet #L&ouml;sungen von 3x^2 - y^2 = n f&uuml;r gegebenes n und int
  // x,y
  private static int helper3(int n) {
    int i = 0; // Z&auml;hler
    for (int x = 1; x <= 2 * n; x++) {
      for (int y = 1; y < x; y++) {
        if (3 * x * x - y * y == n)
          i++;
      }
    }
    return i;
  }

  public void Sieb(int n) {

    lang.nextStep("Einleitung");
    // Startfolie:
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    Rect rec = lang.newRect(new Coordinates(250, 0), new Coordinates(500, 40),
        "back", null);
    TextProperties tp = HeadTextProps;
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 22));
    Text heading = lang.newText(new Coordinates(270, 20), "Sieb des Atkin",
        "head", new MsTiming(0), tp);

    lang.newText(
        new Coordinates(120, 50),
        "Das Sieb von Atkin ist ein schneller, moderner Algorithmus zur Bestimmung aller Primzahlen bis zu einer vorgegebenen Grenze.",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 75),
        "Es ist eine optimierte Version des antiken Sieb des Eratosthenes: Das Atkinsieb leistet einige Vorarbeit und streicht dann alle Viel-",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 90),
        "fachen von Primzahlquadraten. Es wurde von A. O. L. Atkin und Daniel J. Bernstein entwickelt.",
        "intro", null, textProps);
    lang.newText(new Coordinates(120, 120), "Erklärung:", "intro", null,
        textProps);

    lang.newText(
        new Coordinates(120, 135),
        "Der Algorithmus ignoriert alle Zahlen, die durch zwei, drei oder fünf teilbar sind.",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 150),
        "- Alle Zahlen mit Modulo 60 Rest 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32,",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 165),
        "34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, oder 58 sind teilbar durch zwei und nicht prim.",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 180),
        "- Alle Zahlen mit Modulo 60 Rest 3, 9, 15, 21, 27, 33, 39, 45, 51, oder 57 sind teilbar durch drei und nicht prim.",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 195),
        "- Alle Zahlen mit Modulo 60 Rest 5, 25, 35, oder 55 sind teilbar durch 5 und nicht prim.",
        "intro", null, textProps);
    lang.newText(new Coordinates(120, 210),
        "Diese Reste werden alle ignoriert.", "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 225),
        "- Alle Zahlen mit Modulo 60 Rest 1, 13, 17, 29, 37, 41, 49, oder 53 haben einen Modulo 4 Rest von 1.",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 240),
        "Diese Zahlen sind genau dann prim, wenn die Anzahl an Lösungen für   4x^2 + y^2 = n   ungerade ist",
        "intro", null, textProps);
    lang.newText(new Coordinates(120, 255), "und die Zahl quadratfrei ist.",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 270),
        "- Alle Zahlen mit Modulo 60 Rest 7, 19, 31, oder 43 haben einen Modulo 6 Rest von 1. Diese Zahlen sind ",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 285),
        "genau dann prim, wenn die Anzahl an Lösungen für 3x^2 + y^2 = n   ungerade ist und die Zahl quadratfrei ist.",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 300),
        "- Alle Zahlen mit Modulo 60 Rest 11, 23, 47, oder 59 haben einen Modulo 12 Rest von 11. Diese Zahlen sind",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 315),
        "genau dann prim, wenn die Anzahl an Lösungen für   3x^2 - y^2 = n   ungerade ist und die Zahl quadratfrei ist.",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 330),
        "Keine der potentiellen Primzahlen sind teilbar durch 2, 3, oder 5, also können sie nicht durch ihre Quadrate teilbar sein.",
        "intro", null, textProps);
    lang.newText(
        new Coordinates(120, 345),
        "Deshalb wird die Quadratfreiheit nicht bei 2^2 3^2, und 5^2 überprüft.",
        "intro", null, textProps);
    lang.newText(new Coordinates(120, 360), "Quelle: wikipedia.de", "intro",
        null, textProps);

    lang.nextStep("Initialisierung");
    lang.hideAllPrimitives();
    heading.show();
    rec.show();

    int number = (int) Math.ceil(n / 30.0); // Anzahl der Zeilen

    // Quelltextanzeige:
    // SourceCodeProperties scProps = new SourceCodeProperties();
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
    // new Font("Monospaced", Font.PLAIN, 12));
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Coordinates(40, 80 + 35 * number),
        "sourceCode", new MsTiming(0), scProps);
    // add a code line
    // parameters: code itself; name (can be null); indentation level; display
    // options
    sc.addCodeLine("1) Erstelle eine mit 2, 3 und 5 gefüllte Ergebnisliste.",
        null, 0, null);
    sc.addCodeLine(
        "2) Erstelle eine Siebliste mit einem Eintrag für jede positive ganze Zahl; alle Einträge dieser Liste werden am Anfang als nicht-prim markiert.",
        null, 0, null);
    sc.addCodeLine(
        "3) Für jeden Eintrag n in der Siebliste führe folgendes aus:", null,
        0, null);
    sc.addCodeLine("Berechne Rest einer Division durch 60", null, 1, null);

    sc.addCodeLine(
        "a) Falls der Eintrag eine Zahl mit Rest 1, 13, 17, 29, 37, 41, 49, oder 53 enthält, invertiere ihn für jede mögliche Lösung der Gleichung:   4x^2 + y^2 = n.",
        null, 2, null);
    sc.addCodeLine(
        "b) Falls der Eintrag eine Zahl mit Rest 7, 19, 31, oder 43 enthält, invertiere ihn für jede mögliche Lösung der Gleichung:   3x^2 + y^2 = n.",
        null, 2, null);
    sc.addCodeLine(
        "c) Falls der Eintrag eine Zahl mit Rest 11, 23, 47, oder 59 enthält, invertiere ihn für jede mögliche Lösung der Gleichung:   3x^2 - y^2 = n, wobei   x > y.",
        null, 2, null);
    sc.addCodeLine("4) Beginne mit der niedrigsten Zahl in der Siebliste.",
        null, 0, null);
    sc.addCodeLine(
        "5) Nimm die nächste Zahl in der Siebliste, die immer noch als prim markiert ist.",
        null, 2, null);
    sc.addCodeLine("6) Füge die Zahl in die Ergebnisliste ein.", null, 2, null);
    sc.addCodeLine(
        "7) Quadriere die Zahl und markiere alle Vielfachen von diesem Quadrat als nicht-prim.",
        null, 2, null);
    sc.addCodeLine("8) Wiederhole die Schritte 5 bis 8.", null, 2, null);

    // Anzeige Ende

    TextProperties tp2 = new TextProperties();
    tp2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 22));

    Text ausgabe = lang.newText(new Coordinates(50, 60 + 35 * number), "",
        "ausgabe", null, textProps);
    Text loesungen = lang.newText(new Coordinates(50, 70 + 35 * number),
        "Anzahl der Lösungen: NaN", "loesungen", null, textProps);
    int counter = 0;

    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    IntArray result = lang.newIntArray(new Coordinates(10, 50), new int[46],
        "result", new ArrayDisplayOptions(new MsTiming(0), new MsTiming(0),
            true), arrayProps);
    sc.highlight(0);
    result.put(counter, 2, null, null);
    counter++;
    result.put(counter, 3, null, null);
    counter++;
    result.put(counter, 5, null, null);
    counter++;

    lang.nextStep();

    sc.toggleHighlight(0, 1);

    int spalten; // Anzahl der Spalten
    if (number == 1)
      spalten = n;
    else
      spalten = 30;
    // StringMatrix sieb = lang.newStringMatrix(new Coordinates(10,80), new
    // String[2*number][spalten], "sieb", null, matrixProps);
    IntArray[][] sieb = new IntArray[number][spalten];
    IntArray helperSieb = lang.newIntArray(new Coordinates(1000, 1000),
        new int[1], "helper", null, arrayPropsNichtPrim);
    TwoValueCounter counter_2 = lang.newCounter(helperSieb); // Zaehler anlegen
    CounterProperties cp = new CounterProperties(); // Zaehler-Properties
                                                    // anlegen
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit Blau

    Text message = lang.newText(new Coordinates(100, 300 + 35 * number),
        "Lese- und Schreibzugriffe auf das Sieb:", "ausgabe", null);
    TwoValueView view = lang.newCounterView(counter_2, new Coordinates(100,
        320 + 35 * number), cp, true, true);

    lang.newText(new Coordinates(850, 250 + 35 * number), "Legende:",
        "legende", null);
    lang.newText(new Coordinates(850, 270 + 35 * number), "Prim markiert:",
        "primZ", null);
    lang.newText(new Coordinates(850, 290 + 35 * number), "Sonst:",
        "KeinePrimZ", null);
    int[] test = new int[1];
    test[0] = 0;
    lang.newIntArray(new Coordinates(950, 270 + 35 * number), test, "sieb3",
        null, arrayPropsPrim);
    lang.newIntArray(new Coordinates(950, 290 + 35 * number), test, "sieb4",
        null, arrayPropsNichtPrim);
    // Was sind Primzahlen?
    boolean[] prim = new boolean[n];
    for (int i = 0; i < n; i++) {
      prim[i] = false; // alle Zahlen als NP makieren
    }

    // Wo liegt der Eintrag im Sieb?
    int zeile = 0;
    int spalte = 0;
    int[] value = new int[1];
    for (int i = 0; i < n; i++) {
      zeile = (i / 30);
      spalte = i % 30;
      // sieb.put(zeile, spalte, String.valueOf(i+1), null, null);
      // sieb.put(zeile+1, spalte, "np", null, null);
      value[0] = i + 1;
      sieb[zeile][spalte] = lang.newIntArray(new Coordinates(10 + spalte * 35,
          80 + zeile * 30), value, "sieb", null, arrayPropsNichtPrim);
    }
    if (number > 1) {
      for (int i = (spalte + 1); i < 30; i++) {
        // sieb.put(zeile, i, " ", null, null);
        // sieb.put(zeile+1, i, " ", null, null);
      }
    }
    lang.nextStep("Iteration");

    sc.toggleHighlight(1, 2);
    for (int i = 0; i < n; i++) {
      zeile = (i / 30);
      spalte = i % 30;
      lang.nextStep();
      ausgabe.changeColor("color", Color.BLACK, null, null);
      loesungen.setText("Anzahl der Lösungen: NaN", null, null);
      try {
        sieb[zeile][spalte - 1].unhighlightCell(0, null, null);
      } catch (Exception e) {
      }
      try {
        sieb[zeile - 1][29].unhighlightCell(0, null, null);
      } catch (Exception e) {
      }
      sc.unhighlight(4);
      sc.unhighlight(5);
      sc.unhighlight(6);
      sieb[zeile][spalte].highlightCell(0, null, null);
      sc.toggleHighlight(2, 3);
      ausgabe.setText(
          Integer.toString(i + 1) + " % 60 =" + Integer.toString((i + 1) % 60),
          null, null);
      switch ((i + 1) % 60) {
        case 1:
        case 13:
        case 17:
        case 29:
        case 37:
        case 41:
        case 49:
        case 53:
          ausgabe.setText(
              Integer.toString(i + 1) + " % 60 ="
                  + Integer.toString((i + 1) % 60) + " => Fall a)",
              new MsTiming(2000), null);
          ausgabe.changeColor("color", Color.RED, new MsTiming(800), null);
          lang.nextStep();
          int Result = helper1(i + 1);
          loesungen.setText("Anzahl der Lösungen: " + Result,
              new MsTiming(800), null);
          sc.toggleHighlight(3, 4);
          helperSieb.getData(0);
          if (helper1(i + 1) % 2 == 1) {
            loesungen.setText("Anzahl der Lösungen: " + Result
                + " => Eintrag Invertieren", new MsTiming(2000), null);
            if (prim[i]) {
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.COLOR_PROPERTY,
                  (Color) arrayPropsNichtPrim
                      .get(AnimationPropertiesKeys.COLOR_PROPERTY),
                  new MsTiming(3000), null);
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
                  (Color) arrayPropsNichtPrim
                      .get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY),
                  new MsTiming(3000), null);
            } else {
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.COLOR_PROPERTY,
                  (Color) arrayPropsPrim
                      .get(AnimationPropertiesKeys.COLOR_PROPERTY),
                  new MsTiming(3000), null);
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
                  (Color) arrayPropsPrim
                      .get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY),
                  new MsTiming(3000), null);
            }
            prim[i] = !prim[i];
            helperSieb.put(0, 0, null, null);
          } else
            loesungen.setText("Anzahl der Lösungen: " + Result
                + " => Eintrag nicht Invertieren", new MsTiming(2000), null);
          break;
        case 7:
        case 19:
        case 31:
        case 43:
          ausgabe.setText(
              Integer.toString(i + 1) + " % 60 ="
                  + Integer.toString((i + 1) % 60) + " => Fall b)",
              new MsTiming(2000), null);
          ausgabe.changeColor("color", Color.RED, new MsTiming(800), null);
          lang.nextStep();
          Result = helper2(i + 1);
          loesungen.setText("Anzahl der Lösungen: " + Result,
              new MsTiming(800), null);
          sc.toggleHighlight(3, 5);
          helperSieb.getData(0);
          if (helper2(i + 1) % 2 == 1) {
            loesungen.setText("Anzahl der Lösungen: " + Result
                + " => Eintrag Invertieren", new MsTiming(2000), null);
            if (prim[i]) {
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.COLOR_PROPERTY,
                  (Color) arrayPropsNichtPrim
                      .get(AnimationPropertiesKeys.COLOR_PROPERTY),
                  new MsTiming(3000), null);
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
                  (Color) arrayPropsNichtPrim
                      .get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY),
                  new MsTiming(3000), null);
            } else {
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.COLOR_PROPERTY,
                  (Color) arrayPropsPrim
                      .get(AnimationPropertiesKeys.COLOR_PROPERTY),
                  new MsTiming(3000), null);
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
                  (Color) arrayPropsPrim
                      .get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY),
                  new MsTiming(3000), null);
            }
            prim[i] = !prim[i];
            helperSieb.put(0, 0, null, null);
          } else
            loesungen.setText("Anzahl der Lösungen: " + Result
                + " => Eintrag nicht Invertieren", new MsTiming(2000), null);
          break;
        case 11:
        case 23:
        case 47:
        case 59:
          ausgabe.setText(
              Integer.toString(i + 1) + " % 60 ="
                  + Integer.toString((i + 1) % 60) + " => Fall c)",
              new MsTiming(2000), null);
          ausgabe.changeColor("color", Color.RED, new MsTiming(800), null);
          lang.nextStep();
          Result = helper3(i + 1);
          loesungen.setText("Anzahl der Lösungen: " + Result,
              new MsTiming(800), null);
          sc.toggleHighlight(3, 6);
          helperSieb.getData(0);
          if (helper3(i + 1) % 2 == 1) {
            loesungen.setText("Anzahl der Lösungen: " + Result
                + " => Eintrag Invertieren", new MsTiming(2000), null);
            if (prim[i]) {
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.COLOR_PROPERTY,
                  (Color) arrayPropsNichtPrim
                      .get(AnimationPropertiesKeys.COLOR_PROPERTY),
                  new MsTiming(3000), null);
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
                  (Color) arrayPropsNichtPrim
                      .get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY),
                  new MsTiming(3000), null);
            } else {
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.COLOR_PROPERTY,
                  (Color) arrayPropsPrim
                      .get(AnimationPropertiesKeys.COLOR_PROPERTY),
                  new MsTiming(3000), null);
              sieb[zeile][spalte].changeColor(
                  AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
                  (Color) arrayPropsPrim
                      .get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY),
                  new MsTiming(3000), null);
            }
            prim[i] = !prim[i];
            helperSieb.put(0, 0, null, null);
          } else
            loesungen.setText("Anzahl der Lösungen: " + Result
                + " => Eintrag nicht Invertieren", new MsTiming(2000), null);

          break;
      }
    }
    lang.nextStep();
    sc.highlight(7);
    sc.unhighlight(4);
    sc.unhighlight(5);
    sc.unhighlight(6);
    sc.unhighlight(3);
    sieb[zeile][spalte].unhighlightCell(0, null, null);

    for (int i = 0; i < n; i++) {
      zeile = (i / 30);
      spalte = i % 30;
      helperSieb.getData(0);
      if (prim[i]) {
        lang.nextStep();
        sc.toggleHighlight(7, 8);
        sieb[zeile][spalte].highlightCell(0, new MsTiming(2000), null);
        lang.nextStep();
        sc.toggleHighlight(8, 9);
        result.put(counter, i + 1, new MsTiming(2000), null);
        counter++;
        lang.nextStep();
        sc.toggleHighlight(9, 10);
        sieb[zeile][spalte].unhighlightCell(0, null, null);
        for (int j = (i + 1) * (i + 1); j < n; j += (i + 1) * (i + 1)) {
          spalte = (j - 1) % 30;
          prim[j] = false;
          helperSieb.put(0, 0, null, null);
        }

        lang.nextStep();
        sc.toggleHighlight(10, 11);

        sc.toggleHighlight(11, 8);
      }

    }
    lang.nextStep("Fazit");
    lang.hideAllPrimitives();
    heading.show();
    result.show();
    rec.show();
    message.show();
    view.show();
    lang.newText(new Coordinates(140, 100 + 35 * number), "Unter den ersten "
        + n + " Zahlen gibt es " + counter + " Primzahlen. ", "outro", null,
        textProps);
    lang.newText(
        new Coordinates(140, 115 + 35 * number),
        "Das Sieb Atkin benötigt O(n / log log n) Operationen, das Sieb des Erathosthenes O(n).",
        "outro", null, textProps);
  }

}
