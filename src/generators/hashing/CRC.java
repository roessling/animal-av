package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class CRC implements ValidatingGenerator {
  private Language        lang;
  private RectProperties  title_background;
  private ArrayProperties array_style;
  private TextProperties  title_text, textProps;
  private SourceCodeProperties sourcecode_style, comments_style;

  // private Rect hRect;
  private Text                 header;
  private Text                 titleCrc;
  private Text                 titleData;
  private Text                 textCrc32;
  private Text                 mask_hinweis, crc_hinweis, i_hinweis,
      if_hinweis, else_hinweis;
  private Text                 showCrc32, showCrcMask, showLine, showResult;
  private SourceCode           xor_hinweis;
  private SourceCode           desc;
  private SourceCode           sc;
  private SourceCode           end;

  private int[]                data;

  public void init() {
    lang = new AnimalScript("CRC - cyclic redundancy check",
        "Marcel Dostal, Ilja Schwarz", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    title_background = (RectProperties) props
        .getPropertiesByName("title_background");
    array_style = (ArrayProperties) props.getPropertiesByName("array_style");
    title_text = (TextProperties) props.getPropertiesByName("title_text");
    sourcecode_style = (SourceCodeProperties) props
        .getPropertiesByName("sourcecode_style");
    comments_style = (SourceCodeProperties) props
        .getPropertiesByName("comments_style");
    data = (int[]) primitives.get("data");

    hash(data);

    return lang.toString();
  }

  public String getName() {
    return "CRC - cyclic redundancy check";
  }

  public String getAlgorithmName() {
    return "CRC32";
  }

  public String getAnimationAuthor() {
    return "Marcel Dostal, Ilja Schwarz";
  }

  public String getDescription() {
    return "Beim CRC-Verfahren wird bei der &Uuml;bertragung jedes Datenblocks ein zus&auml;tzlicher redundanter Datensatz angeh&auml;ngt, der CRC-Wert."
        + "<br>"
        + "CRC eignet sich um zuf&auml;llige Fehler mit hoher Wahrscheinlichkeit zu entdecken, wie z.B. Rauschen auf der Leitung."
        + "<br>"
        + "<br>"
        + "Mit dieser Methode l&auml;sst sich schnell und ohne viel Rechenleistung ein Pr&uuml;fwert erstellen,"
        + "<br>"
        + "wobei die Berechnung lineare Zeit benötigt."
        + "<br>"
        + "Da dieser Algorithmus so effizient ist, eignet er sich gut um ihn in Hardware zu implementieren.";
  }

  public String getCodeExample() {
    return "    public int crc(int[] data){"
        + "\n"
        + "        int crc32mask = 0x04C11DB7; /* CRC-32 Bitmaske */"
        + "\n"
        + "        int crc32 = 0;"
        + "\n"
        + "        for (int i=0;i<data.length;i++){          "
        + "\n"
        + "            if((((crc32 & 0x80000000) == 0x80000000)? 1:0) != data[i])"
        + "\n" + "                crc32 = (crc32 << 1)^ crc32mask;" + "\n"
        + "            else" + "\n" + "                crc32 <<= 1;" + "\n"
        + "        }   " + "\n" + "        return crc32;" + "\n" + "    }";
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

  private void hash(int[] d) {
    // set the visual properties

    Font font = (Font) title_text.get(AnimationPropertiesKeys.FONT_PROPERTY);
    title_text.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(font.getFontName(), Font.BOLD, 18));

    Color ccolor = (Color) comments_style
        .get(AnimationPropertiesKeys.COLOR_PROPERTY);
    Font cfont = (Font) comments_style
        .get(AnimationPropertiesKeys.FONT_PROPERTY);
    int csize = (Integer) comments_style
        .get(AnimationPropertiesKeys.SIZE_PROPERTY);
    boolean cbold = (Boolean) comments_style
        .get(AnimationPropertiesKeys.BOLD_PROPERTY);
    cfont = new Font(cfont.getName(), cbold ? Font.BOLD : Font.PLAIN, csize);

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, cfont);
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, ccolor);

    header = lang.newText(new Coordinates(20, 30),
        "CRC32 - cyclic redundancy check", "header", null, title_text);

    new Rect(new AnimalRectGenerator(lang), new Offset(-5, -5, header,
        AnimalScript.DIRECTION_NW), new Offset(5, 5, header,
        AnimalScript.DIRECTION_SE), "hRect", null, title_background);
    showDesc();
    lang.nextStep("Beschreibung");

    desc.hide();

    int c = crc32(d);

    sc.hide();
    i_hinweis.setText("", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    i_hinweis.hide();
    crc_hinweis.hide();
    mask_hinweis.hide();

    end = lang.newSourceCode(new Offset(0, 50, titleCrc,
        AnimalScript.DIRECTION_SE), "end", null, comments_style);

    end.addCodeLine("Der Berechnete CRC32 Prüfwert lautet " + getBinary32(c)
        + ",", null, 0, null);
    end.addCodeLine(
        "mit ihm kann man überprüfen ob der Datensatz richtig übertragen wurde.",
        null, 0, null);
    end.addCodeLine("", null, 0, null);
    end.addCodeLine("Die Komplexitätsklasse von Crc32 ist n,", null, 0, null);
    end.addCodeLine(
        "da die Komplexität von der Größe n des eingegebenen Daten-Arrays abhängt.",
        null, 0, null);

    cleanup();
  }

  private int crc32(int[] d) {
    int crc32mask = 0x04C11DB7; /* CRC-32 Bitmaske */
    int crc32 = 0;

    titleData = lang.newText(new Offset(0, 40, header,
        AnimalScript.DIRECTION_SW), "data:", "titleData", null, textProps);
    // wrap int[] to IntArray instance
    IntArray data_array = lang.newIntArray(new Offset(30, -18, titleData,
        AnimalScript.DIRECTION_SE), d, "data", null, array_style);
    titleCrc = lang.newText(new Offset(-10, 20, titleData,
        AnimalScript.DIRECTION_SW), "crc32:", "titleCrc", null, textProps);
    textCrc32 = lang.newText(new Offset(30, 0, titleCrc,
        AnimalScript.DIRECTION_NE), getBinary32(crc32), "crc32", null,
        textProps);
    Timing defaultTiming = new TicksTiming(0);

    // counter
    TwoValueCounter counter = new TwoValueCounter();
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);

    String[] names = { "Rechenoperationen", "Vergleiche" };
    lang.newCounterView(counter, new Offset(60, -8, data_array,
        AnimalScript.DIRECTION_NE), cp, true, true, names);

    lang.nextStep("Initialisierung");
    showSourceCode();
    sc.highlight(0);
    lang.nextStep();
    sc.toggleHighlight(0, 1); // int crc32mask
    mask_hinweis = showHinweis(mask_hinweis, new Offset(20, 15, sc,
        AnimalScript.DIRECTION_NE),
        "CRC32 Bitmaske, verwendet bei Ethernet nach IEEE 802.3",
        "mask_hinweis");
    lang.nextStep();
    unhighlightText(mask_hinweis);
    crc_hinweis = showHinweis(crc_hinweis, new Offset(0, 17, mask_hinweis,
        AnimalScript.DIRECTION_NW), "32 Bit Schieberegister", "crc_hinweis");
    sc.toggleHighlight(1, 2); // int crc32
    lang.nextStep();
    sc.unhighlight(2);
    unhighlightText(crc_hinweis);

    ArrayMarker i = lang.newArrayMarker(data_array, 0, "i", null);
    ArrayMarkerProperties ami = new ArrayMarkerProperties();
    ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    ami.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

    for (; i.getPosition() < data_array.getLength(); i.increment(null,
        defaultTiming)) {
      i_hinweis = showHinweis(i_hinweis, new Offset(0, 34, crc_hinweis,
          AnimalScript.DIRECTION_NW), "i=" + i.getPosition(), "i_hinweis");
      sc.highlight(4); // for
      lang.nextStep("Schleifendurchgang Nr. " + i.getPosition() + 1);
      unhighlightText(i_hinweis);
      int firstbit = ((crc32 & 0x80000000) == 0x80000000) ? 1 : 0;
      int datai = data_array.getData(i.getPosition());
      if_hinweis = showHinweis(if_hinweis, new Offset(0, 17, i_hinweis,
          AnimalScript.DIRECTION_NW), "Ist erstes Bit von crc32 != data[i]? "
          + firstbit + "!=" + datai + " "
          + (firstbit != datai ? "Wahr" : "Falsch"), "if_hinweis");
      sc.toggleHighlight(4, 5); // if

      counter.assignmentsInc(1);
      counter.accessInc(2);

      lang.nextStep();
      unhighlightText(if_hinweis);
      if (firstbit != datai) {
        counter.assignmentsInc(2);
        sc.toggleHighlight(5, 6);
        if (xor_hinweis == null) {
          xor_hinweis = lang.newSourceCode(new Offset(0, 5, if_hinweis,
              AnimalScript.DIRECTION_NW), "xor_hinweis", null, comments_style);
          xor_hinweis.addCodeLine("Schiebe crc32 um 1 nach links.", null, 0,
              null);
          xor_hinweis.addCodeLine("Berechne crc32 xor crc32mask.", null, 0,
              null);
        } else {
          xor_hinweis.show();
        }
        xor_hinweis.toggleHighlight(1, 0);
        crc32 = crc32 << 1;
        textCrc32.setText(getBinary32(crc32), defaultTiming, defaultTiming);
        lang.nextStep();
        xor_hinweis.toggleHighlight(0, 1);

        showCrc32 = showHinweis(showCrc32, new Offset(0, 0, xor_hinweis,
            AnimalScript.DIRECTION_SW), " " + getBinary32(crc32), "showCrc32");
        showCrcMask = showHinweis(showCrcMask, new Offset(0, 0, showCrc32,
            AnimalScript.DIRECTION_SW), "^" + getBinary32(crc32mask),
            "showCrcMask");
        showLine = showHinweis(showLine, new Offset(0, 0, showCrcMask,
            AnimalScript.DIRECTION_SW), " ________________________________",
            "showLine");
        showResult = showHinweis(showResult, new Offset(0, 0, showLine,
            AnimalScript.DIRECTION_SW), " " + getBinary32(crc32 ^ crc32mask),
            "showResult");

        crc32 = crc32 ^ crc32mask;
        textCrc32.setText(getBinary32(crc32), defaultTiming, defaultTiming);

        lang.nextStep();
        xor_hinweis.hide();
        showCrc32.hide();
        showCrcMask.hide();
        showLine.hide();
        showResult.hide();
        sc.unhighlight(6);
      } else {
        counter.assignmentsInc(1);
        sc.toggleHighlight(5, 8);
        crc32 <<= 1;
        textCrc32.setText(getBinary32(crc32), defaultTiming, defaultTiming);
        else_hinweis = showHinweis(else_hinweis, new Offset(0, 45, if_hinweis,
            AnimalScript.DIRECTION_NW), "Schiebe crc32 um 1 nach links.",
            "else_hinweis");
        lang.nextStep();
        else_hinweis.hide();
        sc.unhighlight(8);
      }
      if_hinweis.hide();
    }
    sc.highlight(10); // return
    i.hide();
    lang.nextStep("CRC32 Ergebnis");
    return crc32;
  }

  private String getBinary32(int i) {
    String format = "00000000000000000000000000000000";
    String crc32bin = Integer.toBinaryString(i);
    return format.substring(crc32bin.length()) + crc32bin;
  }

  // Erstelle neuen Text, wenn schon vorhanden zeige Text an.
  private Text showHinweis(Text t, Offset o, String text, String name) {
    Text t2 = t;
    if (t2 == null) {
      t2 = lang.newText(o, text, name, null, textProps);
    } else {
      t2.show();
      t2.setText(text, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    }
    Color ccolor = (Color) comments_style.getItem(
        AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY).get();
    t2.changeColor("", ccolor, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    return t2;
  }

  private void unhighlightText(Text t) {
    Color ccolor = (Color) comments_style.getItem(
        AnimationPropertiesKeys.COLOR_PROPERTY).get();
    t.changeColor("", ccolor, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
  }

  private void showDesc() {
    desc = lang.newSourceCode(new Offset(20, 80, header,
        AnimalScript.DIRECTION_SW), "desc", null, comments_style);

    desc.addCodeLine(
        "Beim CRC-Verfahren wird bei der Übertragung jedes Datenblocks ein zusätzlicher redundanter Datensatz angehängt, der CRC-Wert.",
        null, 0, null);
    desc.addCodeLine(
        "CRC eignet sich um zufällige Fehler mit hoher Wahrscheinlichkeit zu entdecken, wie z.B. Rauschen auf der Leitung.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Mit dieser Methode lässt sich schnell und ohne viel Rechenleistung ein Prüfwert erstellen,",
        null, 0, null);
    desc.addCodeLine("wobei die Berechnung lineare Zeit benötigt. ", null, 0,
        null);
    desc.addCodeLine(
        "Da dieser Algorithmus so effizient ist, eignet er sich gut um ihn in Hardware zu implementieren.",
        null, 0, null);
  }

  private void showSourceCode() {
    sc = lang.newSourceCode(new Offset(0, 50, titleCrc,
        AnimalScript.DIRECTION_SE), "code", null, sourcecode_style);

    sc.addCodeLine("public int crc32(int[] data){", null, 0, null);
    sc.addCodeLine("int crc32mask = 0x04C11DB7;", null, 1, null);
    sc.addCodeLine("int crc32 = 0;", null, 1, null);
    sc.addCodeLine("", null, 1, null);
    sc.addCodeLine("for (int i=0;i<data.length;i++){  ", null, 1, null);
    sc.addCodeLine(
        "if((((crc32 & 0x80000000) == 0x80000000)? 1:0) != data[i])", null, 2,
        null);
    sc.addCodeLine("crc32 = (crc32 << 1)^ crc32mask;", null, 3, null);
    sc.addCodeLine("else", null, 2, null);
    sc.addCodeLine("crc32 <<= 1;", null, 3, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("return crc32;", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer apc,
      Hashtable<String, Object> hshtbl) throws IllegalArgumentException {
    int[] d = (int[]) hshtbl.get("data");
    for (int i = 0; i < d.length; i++) {
      if (d[i] != 0 && d[i] != 1) {
        throw new IllegalArgumentException(
            "Die Array Felder dürfen nur 1 oder 0 enthalten.");
      }
    }
    return true;
  }

  private void cleanup() {
    i_hinweis = null;
    crc_hinweis = null;
    mask_hinweis = null;
    if_hinweis = null;
    xor_hinweis = null;
    showCrc32 = null;
    showCrcMask = null;
    showLine = null;
    showResult = null;
    else_hinweis = null;
  }

}