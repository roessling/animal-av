package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;

/**
 * 
 * Base64 Encoding Algorithm
 * 
 * @author Oliver Guenther, Sebastian Engel
 * 
 */
public class Base64 implements Generator {

  public static void main(String[] args) {
    Base64 base = new Base64();
    base.init();
    Animal.startGeneratorWindow(base);
  }
  
  private Language                          lang;

  // private static final String algo_name = "Base64 Kodierung";
  // private static final String authors = "Sebastian Engel, Oliver Günther";

  private static final String               description           = "Base64-Encoding ist ein populäres Format, um Binärdaten in eine ASCII-Repräsentation zu bringen\n"
                                                                      + "Dazu werden jeweils drei Byte Binärdaten (= 24bit) in vier 6-bit große Blöcke gestückelt. Jeder dieser Block entspricht einem Wert im Intervall [0,63].\n\n"
                                                                      + "Anhand einer ASCII-Tabelle werden diese Werte dann einem ASCII-Zeichen zugewiesen.\n"
                                                                      + "Aus den 64 Symbolen in der Tabelle ergibt sich der Name Base64 - jedes kodiertes Zeichen wird genau einem Wert aus dieser Tabelle zugewiesen.\n"
                                                                      + "Die Tabelle setzt sich dabei wie folgt zusammen: A-Z (Wert 0-25), a-z (Wert 26-51), 0-9 (52-61), + (62), / (63)\n\n"
                                                                      + "Sollten sich die Binärdaten nicht in 24bit Blöcke unterteilen lassen (da sie nicht durch drei teilbar sind),\n"
                                                                      + "werden soviele 6bit Null-Blöcke angehängt, bis die Daten durch drei teilbar sind.\n"
                                                                      + "Diese Null-Bytes werden zudem mit dem Zeichen '=' maskiert, um das Padding zu signalisieren.\n"
                                                                      + "Das Zeichen wird beim Decoden wieder auf ein 'A' (dem Symbol für den Wert 0) zurückgewandelt.\n\n"
                                                                      + "Der entgegengesetzte Prozess arbeit analog dazu. Dazu bildet man die Symbole auf ihre Werte ab und teilt vier 6-bit Blöcke in drei Bytes auf.\n"
                                                                      + "Zum Schluss wird noch das Padding entfernt, indem soviele 0-Bytes entfernt werden, wie '='-Symbole im Base64-Text waren.";

  private static final String               code_example          = "Input: Binär-Daten b, hier die ersten vier Bytes eines BMP Header\n"
                                                                      + "String b = \"\\42\\4D\\12\\09\";\n"
                                                                      + "String s = \"\", p = \"\"; // Initialisiere Ausgabe und Padding\n\n"
                                                                      + "1. Schritt: Füge (3 - (Größe mod 3)) '=' Zeichen dem Padding-String p hinzu. (3- (4 % 3)) => 2 Zeichen\n"
                                                                      + "// p enthält nun also \"==\"\n"
                                                                      + "Die gleiche Anzahl 0-Bytes an b anhängen:\n"
                                                                      + "b += \"\0\0\" // b enthält nun \"\\42\\4D\\12\\09\0\0\"\n\n"
                                                                      + "2. Schritt: Jeweils Drei Bytes Konvertieren in vier 6-bit Werte:\n"
                                                                      + "\\42\\4D\\12 = 01000010  01001101 00010010 =>  010000 100100 110100 010010 => 16 36 52 18 => Q k 0 S\n"
                                                                      + "\\09\\0\\0 = 00001001 00000000 00000000 => 000010 010000 000000 000000 => 2 16 0 0 => C Q A A\n\n"
                                                                      + "3. Schritt: Ersetze die letzten zwei Zeichen durch das Padding.\n"
                                                                      + "Ausgabe Base64-encoded: \"Qk0SCQ==\"";

  /**
   * The base64 encoding table used throughout this class
   */
  private static final String               base64_alphabet       = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

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
   * Default TextProperties for displaying extra information
   */
  private static final TextProperties       tp_bold_default       = new TextProperties();
  static {
    tp_bold_default.set(AnimationPropertiesKeys.FONT_PROPERTY, bold12);
  }

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

  /*
   * Private Properties, set by generator or defaults to *_default
   */

  private RectProperties                    rp;
  private SourceCodeProperties              descp;
  private SourceCodeProperties              scp;
  private ArrayProperties                   arrayProps;
  private TextProperties                    tp_header;
  private TextProperties                    tp_bold;
  private TextProperties                    tp_highlightb;
  private TextProperties                    tp_highlight;

  public Base64() {
    // Set Properties defaults
    this.rp = rp_default;
    this.descp = descp_default;
    this.scp = scp_default;
    this.arrayProps = arrayProps_default;
    this.tp_bold = tp_bold_default;
    this.tp_header = tp_header_default;
    this.tp_highlight = tp_highlight_default;
    this.tp_highlightb = tp_highlightb_default;

  }

  /**
   * @param bindata
   *          an int array with unsigned byte values
   */
  public void encode(byte[] bindata) {

    this.lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    // Show algo description
    Text header = this.lang.newText(new Coordinates(20, 30),
        "Base-64 Encoding Algorithmus", "header", null, tp_header);

    Rect header_bg = this.lang.newRect(new Offset(-5, -5, header, "NW"),
        new Offset(5, 5, header, "SE"), "rectheader", null, rp);

    SourceCode desc = lang.newSourceCode(new Offset(0, 30, header, "SW"),
        "desc", null, descp);
    for (String s : description.split("\n")) {
      desc.addCodeLine(s, null, 0, null);
    }

    lang.nextStep("Start der Animation");

    header_bg.hide();
    header.hide();
    desc.hide();

    // Create the int array for representation (is there a way to use byte[] ?)
    int[] bindata_conv = new int[bindata.length];
    for (int i = 0; i < bindata.length; i++) {
      bindata_conv[i] = bindata[i] & 0xff;
    }

    Font bold12 = new Font("SansSerif", Font.BOLD, 12);
    TextProperties tp_bold = new TextProperties();
    tp_bold.set(AnimationPropertiesKeys.FONT_PROPERTY, bold12);
    lang.newText(new Coordinates(40, 30), "Input: Byte-Array", "title", null,
        tp_bold);
    IntArray bin = lang.newIntArray(new Coordinates(40, 50), bindata_conv,
        "bindata", null, arrayProps);

    lang.nextStep();

    // Add source code object itself
    SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
        null, scp);

    sc.addCodeLine("public String base64_encode(byte[] bin) {", null, 0, null);
    sc.addCodeLine("String result = \\\"\\\", padding = \\\"\\\";", null, 1,
        null); // 1
    sc.addCodeLine("int pad_length = 3 - (bin.length % 3);", "pad_length", 1,
        null);
    sc.addCodeLine("bin = Arrays.copyOf(bin, bin.length + pad_length);", null,
        1, null); // 3
    sc.addCodeLine("for (; pad_length < 3; pad_length++) {", null, 1, null);
    sc.addCodeLine("padding += '=';", null, 2, null); // 5
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("for (int i = 0; i < bin.length; i += 3) {", null, 1, null); // 7
    sc.addCodeLine("int concat_bytes = ((bin[i] & 0xFF) << 16) + ", null, 2,
        null);
    sc.addCodeLine("((bin[i+1] & 0xFF) << 8) + (bin[i+2] & 0xFF);", null, 3,
        null); // 9
    sc.addCodeLine("int b0 = (concat_bytes >> 18) & 63, ", null, 2, null);
    sc.addCodeLine("b1 = (concat_bytes >> 12) & 63,", null, 3, null); // 11
    sc.addCodeLine("b2 = (concat_bytes >> 6) & 63,", null, 3, null);
    sc.addCodeLine("b3 = concat_bytes & 63;", null, 3, null); // 13
    sc.addCodeLine("result += \\\"\\\" + base64_alphabet.charAt(b0) + ", null,
        2, null);
    sc.addCodeLine("base64_alphabet.charAt(b1) + ", null, 3, null); // 15
    sc.addCodeLine("base64_alphabet.charAt(b2) + ", null, 3, null);
    sc.addCodeLine("base64_alphabet.charAt(b3);", null, 3, null); // 17
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine(
        "result = r.substring(0, result.length() - pad_length) + padding;",
        null, 1, null); // 19
    sc.addCodeLine("return result;", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    lang.nextStep();

    base64_encode(bin, sc);
  }

  private void base64_encode(IntArray ia, SourceCode sc) {

    // Setup generators
    TextGenerator tg = new AnimalTextGenerator(lang);

    sc.highlight(0, 0, false);
    int nrElems = ia.getLength();
    int[] bin = new int[nrElems];
    for (int pos = 0; pos < nrElems; pos++)
      bin[pos] = ia.getData(pos);
    // int[] bin = ia.getData();
    String result = "", padding = "";

    // Display lookup table
    SourceCode sm = lang.newSourceCode(new Offset(200, 0, "bindata",
        AnimalScript.DIRECTION_NE), "lookup_table", null, scp);
    sm.addCodeLine("", "", 0, null);
    for (int i = 0; i < 64; i++) {
      if (i < 10)
        sm.addCodeElement(" " + String.valueOf(i), null, 0, null);
      else {
        sm.addCodeElement(String.valueOf(i), null, 0, null);
      }

      if (i == 31) {
        sm.addCodeLine("", "", 0, null);
        for (int j = 0; j < 32; j++) {
          sm.addCodeElement(" " + String.valueOf(base64_alphabet.charAt(j)),
              "", 0, null);
        }
        sm.addCodeLine("", "", 0, null);
        sm.addCodeLine("", "", 0, null);
      }

    }
    sm.addCodeLine("", "", 0, null);
    for (int j = 32; j < 64; j++) {
      sm.addCodeElement(" " + String.valueOf(base64_alphabet.charAt(j)), "", 0,
          null);
    }

    // Next step: pad_length computation
    lang.nextStep("Padding berechnen");

    // Highlight line 2
    sc.toggleHighlight(0, 2);

    // compute length
    int pad_length = 3 - (bin.length % 3);

    FillInBlanksQuestionModel fib_pad = new FillInBlanksQuestionModel(
        "FIB_padding");
    fib_pad
        .setPrompt("Wieviele Elemente werden für das Padding angefügt? (Wert 1,2, oder 3 eingeben)");
    fib_pad.addAnswer(String.valueOf(pad_length), 10, "Korrekt!");
    fib_pad.setGroupID("Erste Frage");
    this.lang.addFIBQuestion(fib_pad);

    // Next step, show result
    lang.nextStep();

    lang.newText(new Offset(0, 30, "bindata", "SW"), "Padding: " + pad_length
        + " Null-Bytes werden angehängt", "padresult", null, tp_bold);

    // Next step: Resize array with padding
    lang.nextStep();

    sc.toggleHighlight(2, 3);
    bin = Arrays.copyOf(bin, bin.length + pad_length);

    // Next step: show info box with method doc
    lang.nextStep();
    SourceCode infotext = lang.newSourceCode(new Offset(-30, 20, "sourceCode",
        AnimalScript.DIRECTION_NE), "infotext", null, scp);

    infotext.addCodeLine("Info:", null, 0, null);
    infotext.addCodeLine("Arrays.copyOf(array, n) gibt ein Array zurück,",
        null, 0, null);
    infotext.addCodeLine("in dem n Elemente aus array kopiert werden.", null,
        0, null);
    infotext.addCodeLine(
        "Falls n > array.length, wird der Rest mit 0 gefüllt.", null, 0, null);
    infotext.highlight(0);

    lang.nextStep();

    infotext.hide();

    // Replace ia with new, padded IntArray
    ia.hide();
    IntArray ia_padded = lang.newIntArray(new Coordinates(40, 50), bin,
        "bindata", null, arrayProps);

    // Highlight padding
    if (pad_length > 0) {
      ia_padded.highlightCell(bin.length - pad_length, bin.length - 1, null,
          null);
      ia_padded.highlightElem(bin.length - pad_length, bin.length - 1, null,
          null);
    }

    // Next Step: Highlight padding string
    lang.nextStep();

    ia_padded.unhighlightCell(bin.length - pad_length, bin.length - 1, null,
        null);
    ia_padded.unhighlightElem(bin.length - pad_length, bin.length - 1, null,
        null);

    sc.toggleHighlight(3, 4);

    Text padstring = new Text(tg, new Offset(0, 8, "padresult", "SW"),
        "Padding-String: leer", "padstring", null, tp_highlight);
    for (int i = 0; i < pad_length; i++) {
      // next step, highlight padding append
      lang.nextStep();
      sc.toggleHighlight(4, 5);
      padding += "=";
      padstring.setText("Padding-String: " + padding, null, null);
      lang.nextStep();
      // re-highlight loop header
      sc.toggleHighlight(5, 4);
    }

    // Next-Step: end for-loop, start processing
    lang.nextStep("Beginn der Kodierung");
    padstring.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.DARK_GRAY,
        null, null);
    sc.toggleHighlight(4, 7);

    // Show result string
    Text resultstring = new Text(tg, new Offset(0, 20, "sourceCode", "SW"),
        "Base64-String: leer", "resultstring", null, tp_highlightb);

    // Initialize Loop texts
    Text rundennr = new Text(tg, new Offset(40, 110, "sourceCode", "NE"), "",
        "rundennr", null, tp_highlightb);
    Text loop_concat_bytes = new Text(tg, new Offset(0, 30, "rundennr", "NE"),
        "", "concat_bytes", null, tp_highlight);
    Text b_0 = new Text(tg, new Offset(0, 15, "concat_bytes", "SW"), "", "b_0",
        null, tp_highlight);
    Text b_1 = new Text(tg, new Offset(0, 15, "b_0", "SW"), "", "b_1", null,
        tp_highlight);
    Text b_2 = new Text(tg, new Offset(0, 15, "b_1", "SW"), "", "b_2", null,
        tp_highlight);
    Text b_3 = new Text(tg, new Offset(0, 15, "b_2", "SW"), "", "b_3", null,
        tp_highlight);

    Text[] b = { b_0, b_1, b_2, b_3 };

    // start computing loop
    int runde = 1, runden_max = (bin.length / 3);
    for (int i = 0; i < bin.length; i += 3) {
      // Reset texts
      b_0.changeColor("color", Color.RED, null, null);
      b_1.changeColor("color", Color.RED, null, null);
      b_2.changeColor("color", Color.RED, null, null);
      b_3.changeColor("color", Color.RED, null, null);
      loop_concat_bytes.changeColor("color", Color.RED, null, null);
      loop_concat_bytes.hide();
      b_0.hide();
      b_1.hide();
      b_2.hide();
      b_3.hide();

      rundennr.setText("Runde: " + runde + " / " + runden_max + ". i = " + i,
          null, null);

      // next step: compute concat bytes
      lang.nextStep();
      sc.unhighlight(7);
      sc.highlight(8);
      sc.highlight(9);

      // Compute bin_i
      lang.nextStep("Runde " + runde + " / " + runden_max);

      ia_padded.highlightCell(i, null, null);
      ia_padded.highlightElem(i, null, null);

      // compute steps of concat_bytes
      int bin_i = ((bin[i] & 0xFF) << 16);
      b_0.setText("(bin[i] & 0xFF) << 16 => " + bin_i, null, null);
      b_0.show();

      lang.nextStep();
      b_0.changeColor("color", Color.DARK_GRAY, null, null);
      ia_padded.unhighlightCell(i, null, null);
      ia_padded.unhighlightElem(i, null, null);
      ia_padded.highlightCell(i + 1, null, null);
      ia_padded.highlightElem(i + 1, null, null);
      int bin_i1 = ((bin[i + 1] & 0xFF) << 8);
      b_1.setText("(bin[i+1] & 0xFF) << 8 => " + bin_i1, null, null);
      b_1.show();

      lang.nextStep();
      b_1.changeColor("color", Color.DARK_GRAY, null, null);
      ia_padded.unhighlightCell(i + 1, null, null);
      ia_padded.unhighlightElem(i + 1, null, null);
      ia_padded.highlightCell(i + 2, null, null);
      ia_padded.highlightElem(i + 2, null, null);
      int bin_i2 = ((bin[i + 2] & 0xFF));
      b_2.setText("(bin[i+2] & 0xFF) => " + bin_i2, null, null);
      b_2.show();

      // compute concat_bytes
      int concat_bytes = ((bin[i] & 0xFF) << 16) + ((bin[i + 1] & 0xFF) << 8)
          + (bin[i + 2] & 0xFF);

      // Frage nur in zweiter Rudne stellen
      if (runde == 2 || (runde == 1 && runden_max == 1)) {
        // Fragerunde 2
        lang.nextStep();
        MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel(
            "Summe");
        mc.setPrompt("Das Ergebnis für concat_bytes lautet?");
        mc.addAnswer(Integer.toString(concat_bytes), 10, "Korrekt!");
        mc.addAnswer(Integer.toString((int) (concat_bytes / 2)), 0,
            "Leider Falsch!");
        mc.addAnswer("0", 0, "Leider Falsch!");
        mc.setGroupID("Erste Frage");
        this.lang.addMCQuestion(mc);
      }

      // next step: show concat bytes
      lang.nextStep();
      b_2.changeColor("color", Color.DARK_GRAY, null, null);
      ia_padded.unhighlightCell(i + 2, null, null);
      ia_padded.unhighlightElem(i + 2, null, null);
      loop_concat_bytes.setText("Kombinierter 3-Byte Wert: " + concat_bytes,
          null, null);
      // remove highlight after one tick
      // not working, color changed immediately -
      // loop_concat_bytes.changeColor("color", Color.black, new TicksTiming(1),
      // null);
      loop_concat_bytes.show();

      // next step: compute b_i
      lang.nextStep();
      b_0.hide();
      b_1.hide();
      b_2.hide();
      b_0.changeColor("color", Color.RED, null, null);
      b_1.changeColor("color", Color.RED, null, null);
      b_2.changeColor("color", Color.RED, null, null);
      b_3.changeColor("color", Color.RED, null, null);

      // Unhighlight
      loop_concat_bytes.changeColor("color", Color.DARK_GRAY, null, null);
      sc.unhighlight(8);

      int b0 = (concat_bytes >> 18) & 63, b1 = (concat_bytes >> 12) & 63, b2 = (concat_bytes >> 6) & 63, b3 = concat_bytes & 63;

      int[] bval = { b0, b1, b2, b3 };

      int highlightline = 9;
      for (int j = 0; j < 4; j++) {

        // Next step: show b_i
        sc.toggleHighlight(highlightline, ++highlightline);
        lang.nextStep();
        Text b_i = b[j];
        b_i.setText(
            "b" + j + ": " + bval[j] + " => '"
                + base64_alphabet.charAt(bval[j]) + "'", null, null);
        b_i.show();

        // Highlight element in source code
        if (bval[j] < 32) {
          sm.highlight(0, bval[j] + 1, false);
          sm.highlight(1, bval[j] + 1, false);
        } else {
          sm.highlight(3, (bval[j] - 31), false);
          sm.highlight(4, (bval[j] - 31), false);
        }

        // next step: next iteration
        lang.nextStep();

        if (bval[j] < 32) {
          sm.unhighlight(0, bval[j] + 1, false);
          sm.unhighlight(1, bval[j] + 1, false);
        } else {
          sm.unhighlight(3, (bval[j] - 31), false);
          sm.unhighlight(4, (bval[j] - 31), false);
        }
        b_i.changeColor("color", Color.DARK_GRAY, null, null);

      }

      sc.unhighlight(highlightline);

      sc.unhighlight(8);
      sc.unhighlight(9);

      sc.highlight(14);
      sc.highlight(15);
      sc.highlight(16);
      sc.highlight(17);

      // Next step: Display new chars
      lang.nextStep();
      result += "" + base64_alphabet.charAt(b0)
          + (char) base64_alphabet.charAt(b1)
          + (char) base64_alphabet.charAt(b2)
          + (char) base64_alphabet.charAt(b3);
      resultstring.setText("Base64-String: " + result, null, null);

      lang.nextStep();

      // Rehighlight loop
      sc.highlight(7);

      // Unhighlight rest
      sc.unhighlight(14);
      sc.unhighlight(15);
      sc.unhighlight(16);
      sc.unhighlight(17);

      runde++;
    }

    // Next Step: exit loop
    lang.nextStep();
    sc.toggleHighlight(7, 19);

    result = result.substring(0, result.length() - pad_length) + padding;

    // Next Step: display result
    lang.nextStep();
    resultstring.setText("Base64-String: " + result, null, null);

    // Next Step: return
    lang.nextStep();
    sc.toggleHighlight(19, 20);

    lang.nextStep("Berechnung abgeschlossen");
    lang.addLine("hideAll");

    sc.unhighlight(20);

    // Next step: Result, Overhead
    Text header = this.lang.newText(new Coordinates(20, 30), "Auswertung",
        "last_header", null, tp_header);

    @SuppressWarnings("unused")
    Rect header_bg = this.lang.newRect(new Offset(-5, -5, header, "NW"),
        new Offset(5, 5, header, "SE"), "last_rectheader", null, rp);

    SourceCode desc = lang.newSourceCode(new Offset(0, 30, header, "SW"),
        "desc", null, descp);
    desc.addCodeLine("Der fertig kodierte Base64-String ist: '" + result + "'",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Die hier gezeigte Algorithmus basiert auf dem RFC 4648. Es gibt aber auch andere Varianten des Base64-Encoding.",
        null, 0, null);
    desc.addCodeLine(
        "Manche Varianten verwenden andere Alphabete, um beispielsweise Base64 in URLs zu ermöglichen.",
        null, 0, null);
    desc.addCodeLine(
        "Oftmals wird zudem nach 64 oder 76 Zeichen ein Zeilenumbruch hinzugefügt.",
        null, 0, null);
    desc.addCodeLine(
        "Für die zentrale Funktionalität des Algorithmus ist das allerdings irrelevant, und daher hier auch nicht ausgeführt.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Die Komplexität ergibt sich unmittelbar aus der Länge des Eingabestrings, und ist daher in O(n).",
        null, 0, null);
    desc.addCodeLine(
        "Durch das Encoden von drei Byte in vier Zeichen ergibt sich zudem ein Overhead von ungefähr 4/3 (33% größere Ausgabe als Eingabe)",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Eine weit verbreitete Anwendung für den Base64-Encoding sind Data-URLs (RFC 2397), um binäre Medien (JPGs, PNGs, usw) als URL darzustellen.",
        null, 0, null);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    // get header background setting
    this.rp = ((RectProperties) props.getPropertiesByName("title-background"));
    // get header text setting
    this.tp_header = ((TextProperties) props.getPropertiesByName("title-text"));

    // get input styling
    this.arrayProps = ((ArrayProperties) props
        .getPropertiesByName("input-style"));

    // get sourcecode styling
    this.scp = ((SourceCodeProperties) props
        .getPropertiesByName("sourcecode-style"));

    // get content styling
    this.tp_highlight = ((TextProperties) props
        .getPropertiesByName("default-highlight-style"));
    this.tp_highlightb = ((TextProperties) props
        .getPropertiesByName("important-highlight-style"));
    this.tp_bold = ((TextProperties) props
        .getPropertiesByName("default-font-style"));
    this.descp = ((SourceCodeProperties) props
        .getPropertiesByName("enumeration-style"));

    // override bold font in highlight, tp_bold (there's no option in XML!?)
    this.tp_header.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 18));
    this.tp_highlight.set(AnimationPropertiesKeys.FONT_PROPERTY, bold12);
    this.tp_highlightb.set(AnimationPropertiesKeys.FONT_PROPERTY, bold12);
    this.tp_bold.set(AnimationPropertiesKeys.FONT_PROPERTY, bold12);

    // Casting directly to byte[] fails with an exception, so let's
    // hack our values together

    int[] temp = (int[]) primitives.get("values");
    byte[] values = new byte[temp.length];

    for (int i = 0; i < temp.length; ++i) {
      if (temp[i] < 0) {
        values[i] = (byte) 0; // Mind the signed-ness
      } else if (temp[i] > 255) {
        values[i] = (byte) 255;
      } else {
        values[i] = (byte) temp[i];
      }

    }

    encode(values);
    this.lang.finalizeGeneration();
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Base64";
  }

  @Override
  public String getAnimationAuthor() {
    return "Sebastian Engel, Oliver Günther";
  }

  @Override
  public String getCodeExample() {
    return code_example;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public String getName() {
    return "Base64 Kodierung";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    this.lang = new AnimalScript("Base64 Kodierung",
        "Sebastian Engel, Oliver Günther", 1000, 600);
    this.lang.setStepMode(true);

  }
}
