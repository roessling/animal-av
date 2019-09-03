package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class Spaghettisortgenerator implements Generator {
  private Language             lang;
  // private int[] Zahlenliste;
  // private TextProperties Highlighfarbe;

  // private int nachrichten;
  // private Text currentEvent1;
  // private Text text2;
  // private Text text3;

  // private Text headerText;
  // private Rect headerBorder;
  // private TextProperties textProps;
  private SourceCode           src;
  private SourceCodeProperties sourceCodeProps;
  private SourceCode           desc;
  private SourceCodeProperties descCodeProps;
  private TextProperties       Kopfzeile;
  private RectProperties       Headerbox;
  private RectProperties       Handfarbe;

  public void init() {
    lang = new AnimalScript("Spaghetti Sort [DE]", "Pascal Schardt", 800, 600);
  }

  public static int[] removeElement(int[] original, int element) {
    int[] n = new int[original.length - 1];
    System.arraycopy(original, 0, n, 0, element);
    System.arraycopy(original, element + 1, n, element, original.length
        - element - 1);
    return n;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int[] Zahlenliste = (int[]) primitives.get("Zahlenliste");
    RectProperties Spaghettifarbe = (RectProperties) props
        .getPropertiesByName("Spaghettifarbe");
    TextProperties Beschreibungstext = (TextProperties) props
        .getPropertiesByName("Beschreibungstext");
    SourceCodeProperties Sourcecode = (SourceCodeProperties) props
        .getPropertiesByName("Sourcecode");
    RectProperties SpaghettiHighlightfarbe = (RectProperties) props
        .getPropertiesByName("Spaghetti-Highlightfarbe");
    ArrayProperties arrayProperties = new ArrayProperties();
    Kopfzeile = (TextProperties) props.getPropertiesByName("Kopfzeile");
    Headerbox = (RectProperties) props.getPropertiesByName("Headerbox");
    Handfarbe = (RectProperties) props.getPropertiesByName("Handfarbe");

    // Beginn eigener Implementierung

    // INITIALISIERUNG ANIMATION
    // lang.setInteractionType(1024);
    // lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    lang.setStepMode(true);

    int[] result = new int[Zahlenliste.length];
    System.arraycopy(Zahlenliste, 0, result, 0, Zahlenliste.length);

    arrayProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    arrayProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
    arrayProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));
    arrayProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    arrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.RED);
    arrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.GREEN);

    IntArray resultarray = lang.newIntArray(new Coordinates(30, 300), result,
        "resultarray", null, arrayProperties);

    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        Kopfzeile.get(AnimationPropertiesKeys.FONT_PROPERTY));
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Kopfzeile.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    lang.newText(new Coordinates(20, 30), "Spaghetti Sort Animation",
        "headerText", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Headerbox.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    lang.newRect(new Coordinates(10, 20), new Coordinates(215, 50),
        "headerBorder", null, rectProps);

    lang.newText(new Coordinates(30, 280), "Ergebnis:", "resulttext", null,
        headerProps);

    descCodeProps = new SourceCodeProperties();
    descCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        Beschreibungstext.get(AnimationPropertiesKeys.FONT_PROPERTY));
    descCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Beschreibungstext.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    descCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLACK);
    descCodeProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    descCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        new Color(255, 0, 0));
    desc = lang.newSourceCode(new Coordinates(20, 65), "description", null,
        descCodeProps);

    desc.addCodeLine(
        "Spaghetti Sort sortiert eine Liste von Zahlen, indem er jeder Zahl eine ungekochte Spaghetti in ",
        null, 0, null);
    desc.addCodeLine("entsprecher Größe zuordnet.", null, 0, null);
    desc.addCodeLine("", null, 0, null);

    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        Sourcecode.get(AnimationPropertiesKeys.FONT_PROPERTY));
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Sourcecode.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLACK);
    sourceCodeProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Sourcecode.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
    src = lang.newSourceCode(new Coordinates(500, 250), "sourceCode", null,
        sourceCodeProps);

    src.addCodeLine(
        "Weise jeder Zahl eine ungekochte Spaghetti entsprechender Länge zu",
        null, 0, null);
    src.addCodeLine("", null, 0, null);
    src.addCodeLine(
        "Nimm alle Spaghetti in die Hand und stelle sie auf eine ebene Oberfläche",
        null, 0, null);
    src.addCodeLine(
        "	Nähere dich mit deiner anderen Hand von oben und nimm jeweils die Spaghetti, die deine Hand berührt",
        null, 1, null);
    src.addCodeLine(
        "	Entferne diese Spaghetti und setze die entsprechende Zahl an die letzte freie Positition in deiner Liste",
        null, 1, null);

    lang.nextStep("Spaghettis initialisieren");

    int[] counterarr = new int[1];
    counterarr[0] = 0;

    IntArray intArray = lang.newIntArray(new Coordinates(190, 350), counterarr,
        "counterarray", null, arrayProperties);

    lang.newText(new Coordinates(215, 350), "Zeiteinheiten", "zeiteinheiten",
        null, headerProps);
    lang.newText(
        new Coordinates(190, 370),
        "Hier werden die benötigten Zeiteinheiten zur Analyse der Komplextität des Algorithmus gezählt",
        "zeiterkl", null, headerProps);
    /*
     * TwoValueCounter counter = lang.newCounter(intArray); // Zaehler anlegen
     * CounterProperties cp = new CounterProperties(); // Zaehler-Properties
     * anlegen cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); //
     * gefuellt... cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); //
     * ...mit Blau // view anlegen, Parameter: // 1. Counter // 2. linke obere
     * Ecke; // 3. CounterProperties; // 4. Anzeige Zaehlerwert als Zahl? // 5.
     * Anzeige Zaehlerwert als Balken? // Alternativ: nur Angabe Counter,
     * Koordinate und Properties TwoValueView view =
     * lang.newCounterView(counter, new Coordinates(190, 370), cp, true, true);
     */

    int durchlauf = 0;
    RectProperties spaghettiProps = new RectProperties();
    spaghettiProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    spaghettiProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Spaghettifarbe.get(AnimationPropertiesKeys.FILL_PROPERTY));
    spaghettiProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    Rect[] spaghetti = new Rect[Zahlenliste.length];
    Text[] spaghettitext = new Text[Zahlenliste.length];

    Text fazittext = lang.newText(new Coordinates(30, 420),
        "Der Algorithmus hat eine Komplexität von O(n) = n + n + n + 1,",
        "fazittext", null, headerProps);

    for (int zahl : Zahlenliste) {
      // make Spaghetti
      // high = zahl*10px?
      // Rectangle
      src.highlight(0);
      spaghetti[durchlauf] = lang.newRect(new Coordinates(210 + durchlauf * 30,
          300 - zahl * 5), new Coordinates(220 + durchlauf * 30,
          300 - zahl * 15), "spaghetti" + durchlauf + "", null, spaghettiProps);
      spaghettitext[durchlauf] = lang.newText(new Coordinates(
          210 + durchlauf * 30, 320), "" + zahl, "spaghettitext" + durchlauf,
          null, headerProps);
      durchlauf = durchlauf + 1;
      Text[] einezeiteinheit = new Text[Zahlenliste.length];

      einezeiteinheit[durchlauf - 1] = lang.newText(new Coordinates(170, 150),
          "+1", "einezeiteinheit", null, headerProps);
      einezeiteinheit[durchlauf - 1].show();
      einezeiteinheit[durchlauf - 1].moveBy("translate", 0, 200, null,
          new TicksTiming(300));
      einezeiteinheit[durchlauf - 1].hide();

      counterarr[0] = counterarr[0] + 1;

      intArray.put(0, counterarr[0], null, new TicksTiming(300));
      fazittext.setText(
          "Spaghetti auf eine Größe brechen benötigt 1 Zeiteinheit.", null,
          null);

      lang.nextStep();
      einezeiteinheit[durchlauf - 1].setText("", null, null);
      lang.nextStep();
    }

    src.unhighlight(0);
    src.highlight(2);

    Text einezeiteinheittext = lang.newText(new Coordinates(170, 150), "+1",
        "einezeiteinheittext", null, headerProps);
    einezeiteinheittext.show();
    einezeiteinheittext.moveBy("translate", 0, 200, null, new TicksTiming(300));

    counterarr[0] = counterarr[0] + 1;

    intArray.put(0, counterarr[0], null, new TicksTiming(300));
    fazittext
        .setText(
            "Die Spaghetti auf den Tisch in der Hand fallen lassen benötigt 1 Zeiteinheit.",
            null, null);

    durchlauf = 0;
    for (int zahl : Zahlenliste) {
      // drop Spaghetti
      // high = zahl*10px?
      // Rectangle
      src.highlight(0);
      spaghetti[durchlauf].moveBy("translate", 0, zahl * 5, null,
          new TicksTiming(300));
      durchlauf = durchlauf + 1;
    }

    lang.nextStep();

    einezeiteinheittext.setText("", null, null);

    src.unhighlight(2);

    int maxvalue = 0;
    RectProperties handProps = new RectProperties();
    handProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    handProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Handfarbe.get(AnimationPropertiesKeys.FILL_PROPERTY));
    handProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    Rect hand = lang.newRect(new Coordinates(210, 110), new Coordinates(
        220 + durchlauf * 30, 100), "hand", null, handProps);
    hand.hide();
    int oldmax = 19;
    for (int i = 0; i < Zahlenliste.length; i++) {
      // Lower the bar
      // Rectangle
      int maxindex = 0;
      for (int k = 0; k < Zahlenliste.length; k++) {
        if (Zahlenliste[k] > Zahlenliste[maxindex]) {
          maxindex = k;
          maxvalue = Zahlenliste[k];
        }
      }
      if (maxindex == 0) {
        maxvalue = Zahlenliste[0];
      }
      hand.moveBy("translate", 0, (oldmax - maxvalue) * 10, null,
          new TicksTiming(300));
      hand.show();

      Text[] einezeiteinheit = new Text[Zahlenliste.length];
      Text[] nocheinezeiteinheit = new Text[Zahlenliste.length];

      einezeiteinheit[i] = lang.newText(new Coordinates(170,
          (300 - oldmax * 10)), "+1", "einezeiteinheit", null, headerProps);
      einezeiteinheit[i].show();
      einezeiteinheit[i].moveBy("translate", 0, oldmax * 10 + 50, null,
          new TicksTiming(300));
      einezeiteinheit[i].hide();

      counterarr[0] = counterarr[0] + 1;

      intArray.put(0, counterarr[0], null, new TicksTiming(300));
      fazittext.setText("Handsenken benötigt eine Zeiteinheit.", null, null);

      spaghetti[maxindex].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
          (Color) SpaghettiHighlightfarbe
              .get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
      src.unhighlight(4);
      src.highlight(3);
      lang.nextStep("Hand senken");
      einezeiteinheit[i].setText("", null, null);
      // Remove bar

      nocheinezeiteinheit[i] = lang.newText(new Coordinates(170,
          (300 - oldmax * 10)), "+1", "einezeiteinheit", null, headerProps);
      nocheinezeiteinheit[i].show();
      nocheinezeiteinheit[i].moveBy("translate", 0, oldmax * 10 + 50, null,
          new TicksTiming(300));
      nocheinezeiteinheit[i].hide();

      counterarr[0] = counterarr[0] + 1;

      intArray.put(0, counterarr[0], null, new TicksTiming(300));

      fazittext.setText(
          "Das Entfernen einer Spaghetti benötigt eine Zeiteinheit.", null,
          null);

      src.unhighlight(3);
      src.highlight(4);
      Zahlenliste[maxindex] = 0;
      spaghetti[maxindex].hide();
      spaghettitext[maxindex].hide();

      // OUTPUT ARRAY
      resultarray.put(Zahlenliste.length - 1 - i, maxvalue, null,
          new TicksTiming(15));
      resultarray.highlightCell(Zahlenliste.length - 1 - i, null, null);
      lang.nextStep("Spaghetti wegnehmen");
      nocheinezeiteinheit[i].setText("", null, null);
      resultarray.unhighlightCell(Zahlenliste.length - 1 - i, null, null);
      oldmax = maxvalue;
    }

    lang.nextStep();

    // FAZIT, Zugriffe, Komplexität usw
    fazittext.setText("", null, null);
    fazittext = lang.newText(new Coordinates(30, 420),
        "Der Algorithmus hat eine Komplexität von O(n) = n + n + n + 1,",
        "fazittext", null, headerProps);
    lang.newText(
        new Coordinates(30, 435),
        "da sowohl die Spaghettizuordnung, das Abstellen der Spaghetti auf den Tisch,",
        "fazittext2", null, headerProps);
    lang.newText(
        new Coordinates(30, 450),
        "das Handsenken als auch das wegnehmen einer Spaghetti in konstanter Zeit,",
        "fazittext2", null, headerProps);
    lang.newText(
        new Coordinates(30, 465),
        "also O(1) geschieht. Lediglich das Iterieren über alle Spaghetti benötigt O(n).",
        "fazittext3", null, headerProps);
    lang.newText(
        new Coordinates(30, 480),
        "Es handelt sich hierbei um einen massiv parallelen Algorithmus, da Hand, Obefläche und Spaghettis komplett parallel zueinander arbeiten.",
        "fazittext4", null, headerProps);
    // ,\nda sowohl die Spaghettizuordnung, das Handsenken als auch das
    // wegnehmen einer Spaghetti in konstanter Zeit,\nalso O(1) geschieht.
    lang.nextStep("Ende der Animation");
    lang.finalizeGeneration();

    return lang.toString();
  }

  public String getName() {
    return "Spaghetti Sort [DE]";
  }

  public String getAlgorithmName() {
    return "Spaghetti Sort [DE]";
  }

  public String getAnimationAuthor() {
    return "Pascal Schardt";
  }

  public String getDescription() {
    return "Spaghetti Sort sortiert eine Liste von Zahlen, indem er jeder Zahl eine ungekochte Spaghetti in "
        + "\n" + "entsprecher Größe zuordnet.";
  }

  public String getCodeExample() {
    return "Weise jeder Zahl eine ungekochte Spaghetti entsprechender Länge zu"
        + "\n"
        + "\n"
        + "Nimm alle Spaghetti in die Hand und stelle sie auf eine ebene Oberfläche"
        + "\n"
        + "  Nähere dich mit deiner anderen Hand von oben und nimm jeweils die Spaghetti, die deine Hand berührt"
        + "\n"
        + "  Lege diese Spaghetti an die letzte freie Positition in deiner Liste";
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
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}