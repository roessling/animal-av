package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Adler32 implements Generator {
  private Language                          lang;
  private String                            input;

  /**
   * Default RectProperties for Bordered Rectangles (header)
   */
  static final RectProperties               rp_default            = new RectProperties();
  static {
    rp_default.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rp_default.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    rp_default.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  /**
   * Default ArrayProperties for Input Arrays
   */
  private static final ArrayProperties      arrayProps_default    = new ArrayProperties();
  static {
    arrayProps_default.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps_default.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps_default.set(AnimationPropertiesKeys.FILLED_PROPERTY,
        Boolean.TRUE);
    arrayProps_default.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        Color.BLACK);
    arrayProps_default.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.WHITE);
    arrayProps_default.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.gray);
  }

  /**
   * Default SourceCodeProperties for Description texts
   */
  private static final SourceCodeProperties descp_default         = new SourceCodeProperties();
  static {
    descp_default.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLACK);
    descp_default.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
  }

  /**
   * Default SourceCodeProperties for displaying Source code
   */
  private static final SourceCodeProperties scp_default           = new SourceCodeProperties();
  static {
    scp_default.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
    scp_default.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));

    scp_default.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scp_default.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  /**
   * Default TextProperties for Header fonts
   */
  private static final TextProperties       tp_header_default     = new TextProperties();
  static {
    tp_header_default.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 18));
  }

  /**
   * Bold font, 12pt
   */
  private static final Font                 bold12                = new Font(
                                                                      "SansSerif",
                                                                      Font.BOLD,
                                                                      12);

  /**
   * Default highlight TextProperties font, 12 bold, blue Color
   */
  private static final TextProperties       tp_highlightb_default = new TextProperties();
  static {
    tp_highlightb_default.set(AnimationPropertiesKeys.FONT_PROPERTY, bold12);
    tp_highlightb_default.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLUE);
  }

  /**
   * Highlight TextProperties for active elements in the current slide, Font 12
   * bold, red
   */
  private static final TextProperties       tp_highlight_default  = new TextProperties();
  static {
    tp_highlight_default.set(AnimationPropertiesKeys.FONT_PROPERTY, bold12);
    tp_highlight_default.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
  }

  private static final String               description           = "Adler-32 ist ein 1995 entwickelter Checksummen-Algorithmus, welcher unter anderem in zlib benutzt wird.\n\n"
                                                                      + "Dazu werden zwei 16-Bit Prüfsummen berechnet und dann zu einer 32-Bit Prüfsumme konkateniert.\n"
                                                                      + "Als erstes werden die Bytes der Daten in ASCII-Werten umgewandelt, beispielsweise hätte der String 'Algorithmus' \n "
                                                                      + "als dezimale ASCII-Werte für seine Buchstaben: 65 108 103 111 114 105 116 104 109 117 115.\n\n"
                                                                      + "Anschliessend werden zwei Prüfsummen A und B berechnet.\n"
                                                                      + "Prüfsumme A wird mit dem Wert 1 initialisiert und es werden in jeder Iteration des Algorithmus jeweils\n "
                                                                      + "nur die ASCII-Werte der Bytes aufaddiert.\n\n"
                                                                      + "Für Prüfsumme B, welche den Startwert 0 hat, werden jeweils die Werte von A in der gleichen Iteration \n"
                                                                      + "und die Werte von B aus der letzen Iteration aufaddiert."
                                                                      + "Um zu gewährleisten, dass auch bei langen Nachrichten die beiden Summen keine Werte annehmen, \n"
                                                                      + "die durch 16-Bit nicht mehr darstellbar sind, werden in jeder Iteration die Summen modulo 65521 gerechnet.\n\n"
                                                                      + "Dies kommt daher, dass (2 ^ 16) - 1 = 65521 die größte, mit 16-Bit darstellbare Zahl ist.\n"
                                                                      + "Nun, wenn beide Prüfsummen berechnet worden sind, wird A hinter B gehängt und bilden so die Adler-32 Prüfsumme.";

  private static final String               codeExample           = "Input: der String \"Algo\"\n"
                                                                      + "1. Schritt: Umwandlung der Nachricht in ASCII-Werte: 65 108 103 111\n"
                                                                      + "2. Schritt: Berechnung der Prüfsummen A und B:\n"
                                                                      + "		Initialisierung	:	A = 1 							B = 0\n"
                                                                      + "		1. Iteration	:	A = 1 + 65 = 66	(mod 65521)		B = 0 + 66 = 66 (mod 65521)\n"
                                                                      + "		2. Iteration	: 	A = 66 + 108 = 174 (mod 65521)	B = 66 + 174 = 240 (mod 65521)\n"
                                                                      + "		3. Iteration	: 	A = 174 + 103 = 277 (mod 65521)	B = 240 + 277 = 517 (mod 65521)\n"
                                                                      + "		4. Iteration	:	A = 277 + 111 = 388 (mod 65521) B = 517 + 388 = 905 (mod 65521)\n\n"
                                                                      + "A = 388, B = 905\n"
                                                                      + "3. Schritt: Konkatenation der Summen: B|A = 905388";

  // private static final String algo_name =
  // "Adler-32 Checksumme / Hashverfahren";
  // private static final String authors = "Sebastian Engel, Oliver Günther";

  /*
   * Private Properties, set by generator or defaults to *_default
   */

  private RectProperties                    rp;
  private SourceCodeProperties              descp;
  private SourceCodeProperties              scp;
  private ArrayProperties                   arrayProps;
  private TextProperties                    tp_header;
  private TextProperties                    tp_highlightb;
  private TextProperties                    tp_highlight;

  public Adler32() {
    // Set Properties defaults
    this.rp = rp_default;
    this.descp = descp_default;
    this.scp = scp_default;
    this.arrayProps = arrayProps_default;
    this.tp_header = tp_header_default;
    this.tp_highlight = tp_highlight_default;
    this.tp_highlightb = tp_highlightb_default;

  }

  public void animate(String input) {

    // Setup generators
    TextGenerator tg = new AnimalTextGenerator(lang);
    this.lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    // Show algo description
    Text header = this.lang.newText(new Coordinates(20, 30),
        "Adler-32 Prüfsummenberechnung", "header", null, tp_header);

    Rect header_bg = this.lang.newRect(new Offset(-5, -5, header, "NW"),
        new Offset(5, 5, header, "SE"), "rectheader", null, rp);

    SourceCode desc = lang.newSourceCode(new Offset(0, 30, header, "SW"),
        "desc", null, descp);
    for (String s : description.split("\n")) {
      desc.addCodeLine(s, null, 0, null);
    }

    lang.nextStep("Start der Animation");
    desc.hide();

    Text eingabe = new Text(tg, new Offset(0, 30, header, "SW"),
        "EingabeString: ", "title", null, tp_highlightb);
    // Create string array from String
    String[] input_arr = new String[input.length()];

    for (int i = 0; i < input.length(); i++) {
      input_arr[i] = String.valueOf(input.charAt(i));
    }
    StringArray rawvals = lang.newStringArray(
        new Offset(30, -18, eingabe, "SE"), input_arr, "values", null,
        arrayProps);

    lang.nextStep();

    eingabe.changeColor("color", Color.DARK_GRAY, null, null);

    Variables v = lang.newVariables();

    // Create string array from String
    int[] ord_values = new int[input.length()];

    for (int i = 0; i < input.length(); i++) {
      ord_values[i] = (int) input.charAt(i);
    }

    Text ascii_eingabe = new Text(tg, new Offset(0, 10, eingabe, "SW"),
        "ASCII-Werte der Eingabe", "title", null, tp_highlightb);
    IntArray vals = lang.newIntArray(new Offset(30, -18, ascii_eingabe, "SE"),
        ord_values, "values", null, arrayProps);

    lang.nextStep();

    ascii_eingabe.changeColor("color", Color.DARK_GRAY, null, null);

    SourceCode sc = lang.newSourceCode(new Coordinates(20, 120), "sourceCode",
        null, scp);

    sc.addCodeLine("public String computeAdler32str(String input) {", null, 0,
        null);
    sc.addCodeLine("int modAdler = 65521;", null, 1, null); // 1
    sc.addCodeLine(" ", null, 1, null);
    sc.addCodeLine("long a = 1;", null, 1, null); // 3
    sc.addCodeLine("long b = 0;", null, 1, null); // 4
    sc.addCodeLine(" ", null, 1, null);
    sc.addCodeLine("for (int i = 0; i < input.length(); i++ {", null, 1, null); // 6
    sc.addCodeLine("int val = (int) input.charAt(i);", null, 2, null); // 7
    sc.addCodeLine(" ", null, 2, null); // 8
    sc.addCodeLine("a = a + (val % modAdler);", null, 2, null); // 9
    sc.addCodeLine("b = (a + b) % modAdler;", null, 2, null); // 10
    sc.addCodeLine(" ", null, 2, null);
    sc.addCodeLine("}", null, 1, null); // 12
    sc.addCodeLine(" ", null, 1, null);
    sc.addCodeLine("long checksum ((b << 16) | a);", null, 1, null); // 14
    sc.addCodeLine("return checksum;", null, 1, null); // 15
    sc.addCodeLine("}", null, 0, null);

    sc.highlight(0);

    lang.nextStep();

    sc.toggleHighlight(0, 1);

    // Hinweis bzgl modAdler

    SourceCode modAdlerHinweis = lang.newSourceCode(new Offset(20, 10,
        "sourceCode", AnimalScript.DIRECTION_NE), "infotext", null, scp);

    modAdlerHinweis.addCodeLine("Erklärung für modAdler = 65521:", null, 0,
        null);
    modAdlerHinweis.addCodeLine(
        "modAdler stellt sicher, dass keine der beiden ", null, 0, null);
    modAdlerHinweis.addCodeLine(
        "Zwischenergebnisse eine Zahl größer 2^16 annehmen.", null, 0, null);
    modAdlerHinweis.addCodeLine("[65521 ist die größte Primzahl < 2^16]", null,
        0, null);
    modAdlerHinweis.highlight(0);

    lang.nextStep("Initialisierung");

    modAdlerHinweis.hide();

    sc.toggleHighlight(1, 3);

    // Hinweis zu Initialisierung

    Text aHinweis = this.lang.newText(new Offset(0, 25, "infotext",
        AnimalScript.DIRECTION_NW), "Initialisere Zwischenwert A als 1",
        "a_hinweis", null, tp_highlight);

    lang.nextStep();

    aHinweis.changeColor("color", Color.DARK_GRAY, null, null);

    sc.toggleHighlight(3, 4);

    Text bHinweis = this.lang.newText(new Offset(0, 18, "a_hinweis",
        AnimalScript.DIRECTION_NW), "Initialisere Zwischenwert B als 0",
        "b_hinweis", null, tp_highlight);

    bHinweis.changeColor("color", Color.DARK_GRAY, null, null);

    sc.toggleHighlight(4, 6);

    int modAdler = 65521;
    long a = 1;
    long b = 0;

    v.declare("String", "curchar", "");
    v.declare("String", "a", "1");
    v.declare("String", "b", "0");
    v.declare("String", "Checksum", "null");

    Text rundennr = new Text(tg, new Offset(0, 13, "b_hinweis", "SW"), "",
        "rundennr", null, tp_highlightb);
    Text curchar = new Text(tg, new Offset(0, 20, "rundennr", "NE"), "",
        "curchar", null, tp_highlight);
    Text berechnet_a = new Text(tg, new Offset(0, 25, "curchar", "NE"), "",
        "ba", null, tp_highlight);
    Text berechnet_b = new Text(tg, new Offset(0, 20, "ba", "NE"), "", "bb",
        null, tp_highlight);
    Text ergebnis = new Text(tg, new Offset(0, 45, "bb", "SW"), "", "ergebnis",
        null, tp_highlight);

    for (int i = 0; i < input.length(); i++) {

      berechnet_a.hide();
      berechnet_b.hide();
      curchar.hide();

      sc.toggleHighlight(10, 6);
      rundennr.setText("Runde " + (i + 1) + " von " + input.length(), null,
          null);

      if (i == 0)
        lang.nextStep("Berechnung der Summen");
      else
        lang.nextStep();

      sc.toggleHighlight(6, 7);

      char c = input.charAt(i);
      v.set("curchar", String.valueOf(c));
      int x = (int) c;
      curchar.setText("ASCII-Wert von  '" + c + "' ist " + x, null, null);
      curchar.show();
      if (i > 0) {
        vals.unhighlightCell(i - 1, null, null);
        vals.unhighlightElem(i - 1, null, null);
        rawvals.unhighlightCell(i - 1, null, null);
        rawvals.unhighlightElem(i - 1, null, null);
      }
      vals.highlightCell(i, null, null);
      vals.highlightElem(i, null, null);
      rawvals.highlightCell(i, null, null);
      rawvals.highlightElem(i, null, null);

      lang.nextStep();
      sc.toggleHighlight(7, 9);
      curchar.changeColor("color", Color.DARK_GRAY, null, null);
      long a_neu = a + (x % modAdler);
      berechnet_a.setText("a = ( " + a + " + " + x + " ) = " + a_neu
          + " (mod 65521)", null, null);
      a = a_neu;
      berechnet_a.show();

      v.set("a", String.valueOf(a));

      lang.nextStep();
      sc.toggleHighlight(9, 10);
      berechnet_a.changeColor("color", Color.DARK_GRAY, null, null);
      long b_neu = (a + b) % modAdler;
      berechnet_b.setText("b = ( " + a + " + " + b + " ) = " + b_neu
          + " (mod 65521)", null, null);
      b = b_neu;
      berechnet_b.show();

      v.set("b", String.valueOf(b));

      lang.nextStep();

      // Texte zurücksetzen
      curchar.changeColor("color", Color.RED, null, null);
      berechnet_a.changeColor("color", Color.RED, null, null);
      berechnet_b.changeColor("color", Color.RED, null, null);

      berechnet_b.hide();
      berechnet_a.hide();
      curchar.hide();

    }

    sc.toggleHighlight(10, 12);
    rundennr.hide();

    lang.nextStep();

    sc.toggleHighlight(12, 14);
    long checksum = ((b << 16) | a);

    v.set("Checksum", String.valueOf(checksum));

    ergebnis.setText("checksum = ( ( " + b + " << 16 ) | " + a + " ) = "
        + checksum, null, null);

    lang.nextStep("Konkatenation der Summen");

    sc.toggleHighlight(14, 15);

    ergebnis.changeColor("color", Color.DARK_GRAY, null, null);
    new Text(tg, new Offset(0, 30, "sourceCode", "SW"),
        "Die Prüfsumme des Eingabestrings '" + input + "' ist: " + checksum,
        "ergebnisstr", null, tp_highlightb);

    lang.nextStep();
    MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel(
        "Länge der Checksumme");
    mc.setPrompt("Das Ergebnis dieses Algorithmus liefert immer einen 32-bit Integer");
    mc.addAnswer(
        "Ja, weil a und b jeweils 16-Bit Checksummen sind, die konkateniert werden",
        10, "Korrekt!");
    mc.addAnswer(
        "Nein, weil das Ergebnis immer modulo 65521 (modAdler-Wert) zu betrachten ist",
        0, "Das ist falsch, "
            + "denn nur die laufenden Summen (a,b) sind jeweils mod 65521.");
    mc.addAnswer("Nein, weil das Ergebnis auch eine Gleitkommazahl sein kann.",
        0, "Leider Falsch, die Modulo Operation liefert immer Ganzzahlen, "
            + "und auch die Konkatenation liefert Integers.");
    mc.setGroupID("Erste Frage");
    this.lang.addMCQuestion(mc);

    lang.nextStep("Zusammenfassung");
    lang.addLine("hideAll");

    header.show();
    header_bg.show();

    desc = lang.newSourceCode(new Coordinates(30, 80), "desc", null, descp);

    desc.addCodeLine("Die Prüfsumme des Eingabestrings: '" + input + "' ist "
        + checksum, null, 0, null);
    desc.addCodeLine(" ", null, 0, null);
    desc.addCodeLine(
        "Die hier gezeigte Algorithmus Adler-32 (RFC 1950) ist eine vereinfachte Version der Fletcher-Checksumme.",
        null, 0, null);
    desc.addCodeLine("Adler-32 wurde von Mark Adler im Jahr 1995 entwickelt.",
        null, 0, null);
    desc.addCodeLine(" ", null, 0, null);
    desc.addCodeLine(
        "Die Komplexität ergibt sich unmittelbar aus der Länge des Eingabestrings, und ist daher in O(n).",
        null, 0, null);
    desc.addCodeLine(" ", null, 0, null);
    desc.addCodeLine(
        "Hierbei ist zu beachten, dass Adler-32 im Vergleich zum weiter verbreiteten CRC-32 mehr Wert auf Geschwindigkeit",
        null, 0, null);
    desc.addCodeLine(
        "als auf Sicherheit im Bezug auf Fehlerfindung und Integrität legt.",
        null, 0, null);
    desc.addCodeLine(
        "Dies bedeutet, dass bei nur sehr kurzen Nachrichten sich bei kleinen Änderungen oftmals auch nur die Prüfsumme",
        null, 0, null);
    desc.addCodeLine(
        "kleine Änderungen aufweist und ist daher viel leichter fälschbar als beispielsweise bei CRC-32.",
        null, 0, null);

  }

  public void init() {
    this.lang = new AnimalScript("Adler-32 Checksumme / Hashverfahren",
        "Sebastian Engel, Oliver Günther", 1000, 600);
    this.lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // get header background setting
    this.rp = ((RectProperties) props.getPropertiesByName("title-background"));
    // get header text setting
    this.tp_header = ((TextProperties) props.getPropertiesByName("title-text"));

    // get input styling
    this.arrayProps = ((ArrayProperties) props
        .getPropertiesByName("values-style"));

    // get sourcecode styling
    this.scp = ((SourceCodeProperties) props
        .getPropertiesByName("sourcecode-style"));

    // get content styling
    this.tp_highlight = ((TextProperties) props
        .getPropertiesByName("default-highlight-style"));
    this.tp_highlightb = ((TextProperties) props
        .getPropertiesByName("important-highlight-style"));
    this.descp = ((SourceCodeProperties) props
        .getPropertiesByName("enumeration-style"));

    // override bold font in highlight, tp_bold (there's no option in XML!?)
    this.tp_header.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 18));
    this.tp_highlight.set(AnimationPropertiesKeys.FONT_PROPERTY, bold12);
    this.tp_highlightb.set(AnimationPropertiesKeys.FONT_PROPERTY, bold12);

    // Input string
    input = (String) primitives.get("input");
    animate(input);
    this.lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Adler-32";
  }

  public String getAlgorithmName() {
    return "Adler-32 Checksumme / Hashverfahren";
  }

  public String getAnimationAuthor() {
    return "Sebastian Engel, Oliver Günther";
  }

  public String getDescription() {
    return description;
  }

  public String getCodeExample() {
    return codeExample;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}
