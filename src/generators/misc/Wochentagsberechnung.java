package generators.misc;

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
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class Wochentagsberechnung implements ValidatingGenerator {
  private Language        lang;
  private ArrayProperties array_style;
  private TextProperties  title_text, textProps;
  private RectProperties  title_background;
  private SourceCodeProperties sourcecode_style, text_style;
  private SourceCode           desc, sc, end;
  private Text                 title, datum, mZiffern, wTage;
  // private Rect title_bg;
  private int                  tag, monat, jahr;
  private int[]                monatsziffern = { 0, 3, 3, 6, 1, 4, 6, 2, 5, 0,
      3, 5                                  };
  private String[]             wochentage    = { "So", "Mo", "Di", "Mi", "Do",
      "Fr", "Sa"                            };
  private IntArray             datum_array, mZiffern_array;
  private StringArray          wTage_array;
  private TwoValueCounter      counter;

  public void init() {
    lang = new AnimalScript("Wochentagsberechnung",
        "Marcel Dostal, Ilja Schwarz", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    text_style = (SourceCodeProperties) props.getPropertiesByName("text_style");
    array_style = (ArrayProperties) props.getPropertiesByName("array_style");
    title_text = (TextProperties) props.getPropertiesByName("title_text");
    title_background = (RectProperties) props
        .getPropertiesByName("title_background");
    sourcecode_style = (SourceCodeProperties) props
        .getPropertiesByName("sourcecode_style");
    jahr = (Integer) primitives.get("jahr");
    monat = (Integer) primitives.get("monat");
    tag = (Integer) primitives.get("tag");

    // set the visual properties
    Font font = (Font) title_text.get(AnimationPropertiesKeys.FONT_PROPERTY);
    title_text.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(font.getFontName(), Font.BOLD, 18));

    Color ccolor = (Color) text_style
        .get(AnimationPropertiesKeys.COLOR_PROPERTY);
    Font cfont = (Font) text_style.get(AnimationPropertiesKeys.FONT_PROPERTY);
    int csize = (Integer) text_style.get(AnimationPropertiesKeys.SIZE_PROPERTY);
    boolean cbold = (Boolean) text_style
        .get(AnimationPropertiesKeys.BOLD_PROPERTY);
    cfont = new Font(cfont.getName(), cbold ? Font.BOLD : Font.PLAIN, csize);

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, cfont);
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, ccolor);

    // show title
    title = lang.newText(new Coordinates(20, 30), getAlgorithmName(), "title",
        null, title_text);

    new Rect(new AnimalRectGenerator(lang), new Offset(-5, -5, title,
        AnimalScript.DIRECTION_NW), new Offset(5, 5, title,
        AnimalScript.DIRECTION_SE), "title_bg", null, title_background);

    showDesc();
    lang.nextStep("Beschreibung");
    desc.hide();

    int[] d = { tag, monat, jahr };
    datum = lang.newText(new Offset(20, 15, title, AnimalScript.DIRECTION_SW),
        "Datum: ", "datum", null, textProps);
    datum_array = lang.newIntArray(new Offset(0, 5, datum,
        AnimalScript.DIRECTION_SW), d, "datum_array", null, array_style);
    mZiffern = lang.newText(new Offset(0, 15, datum_array,
        AnimalScript.DIRECTION_SW), "Monatsziffern: ", "mZiffern", null,
        textProps);
    mZiffern_array = lang.newIntArray(new Offset(0, 5, mZiffern,
        AnimalScript.DIRECTION_SW), monatsziffern, "mZiffern_array", null,
        array_style);
    wTage = lang.newText(new Offset(0, 15, mZiffern_array,
        AnimalScript.DIRECTION_SW), "Wochentage: ", "wTage", null, textProps);
    wTage_array = lang.newStringArray(new Offset(0, 5, wTage,
        AnimalScript.DIRECTION_SW), wochentage, "wTage_array", null,
        array_style);

    // counter
    counter = new TwoValueCounter();
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);

    String[] names = { "Rechenoperationen", "Vergleiche" };
    lang.newCounterView(counter, new Offset(0, 15, wTage_array,
        AnimalScript.DIRECTION_SW), cp, true, true, names);

    lang.nextStep("Initialisierung");
    int w = wochentagsberechnung(tag, monat, jahr);

    String[] wochentage = { "Sonntag", "Montag", "Dienstag", "Mittwoch",
        "Donnerstag", "Freitag", "Samstag" };
    end = lang.newSourceCode(new Offset(0, 15, sc, AnimalScript.DIRECTION_NW),
        "end", null, text_style);
    end.addCodeLine("Der " + tag + "." + monat + "." + jahr + " ist ein "
        + wochentage[w] + ".", null, 0, null);
    end.addCodeLine(
        "Dieser Alghorithmus sieht zwar kompliziert aus, ist aber sehr schnell (Komplexität O(1)) und wird deshalb auch gerne bei Kopfrechen Wettbewerben eingesetzt,",
        null, 0, null);
    end.addCodeLine(
        "um festzustellen, wer die meisten Daten in gegebener Zeit berechnen kann.",
        null, 0, null);

    return lang.toString();
  }

  public String getName() {
    return "Wochentagsberechnung";
  }

  public String getAlgorithmName() {
    return "Wochentagsberechnung";
  }

  public String getAnimationAuthor() {
    return "Marcel Dostal, Ilja Schwarz";
  }

  public String getDescription() {
    return "Der Algorithmus (Wochentagsberechnung oder auch Kalenderrechnen) berechnet aus einem gegebenen Datum den jeweiligen Wochentag des Gregorianischen Kalenders. <br>"
        + "\n"
        + "\n"
        + "Die Wochentagsberechnung ist eine Disziplin bei Meisterschaften im Kopfrechnen.";
  }

  public String getCodeExample() {
    return "public String wochentagsberechnung (int tag, int monat, int jahr) {"
        + "\n"
        + "    int[] monatsziffern = {0, 3, 3, 6, 1, 4, 6, 2, 5, 0, 3, 5};"
        + "\n"
        + "    String[] wochentage = {\"So\", \"Mo\", \"Di\", \"Mi\", \"Do\", \"Fr\", \"Sa\"};"
        + "\n"
        + "    int jahrImJahrhundert = jahr%100;"
        + "\n"
        + "    int jahrhundert = jahr/100;"
        + "\n"
        + "\n"
        + "    int tagesziffer = tag % 7;"
        + "\n"
        + "    int monatsziffer = monatsziffern[monat-1];"
        + "\n"
        + "    int jahresziffer = (jahrImJahrhundert+(jahrImJahrhundert/4)) % 7;"
        + "\n"
        + "    int jahrhundertsziffer = (3 - (jahrhundert % 4)) * 2;"
        + "\n"
        + "    int schaltjahreskorrektur = ((jahr % 4 == 0 && jahr % 100 != 0) || jahr % 400 == 0)? 6:0;"
        + "\n"
        + "\n"
        + "    int wochentag = (tagesziffer + monatsziffer + jahresziffer"
        + "\n"
        + "                   + jahrhundertsziffer + schaltjahreskorrektur) % 7;"
        + "\n" + "\n" + "    return wochentage[wochentag];" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public int wochentagsberechnung(int tag, int monat, int jahr) {
    int space = 8;
    showSourceCode();
    sc.highlight(0);
    lang.nextStep("Berechnung");

    sc.toggleHighlight(0, 1);
    SourceCode cMZiffern = lang.newSourceCode(new Offset(20, space, sc,
        AnimalScript.DIRECTION_NE), "cMZiffern", null, text_style);
    cMZiffern.addCodeLine(
        "Die Zahl für Januar ist 0, die nächsten werden wie folgt berechnet:",
        null, 0, null);
    cMZiffern
        .addCodeLine(
            "((Tage des vorherigen Monats mod 7) + Monatsziffer der Vormonats) mod 7",
            null, 0, null);
    cMZiffern
        .addCodeLine("Februar = ((31 mod 7) + 0) mod 7 = 3", null, 0, null);
    cMZiffern.addCodeLine("März = ((28 mod 7) + 3) mod 7 = 3 usw", null, 0,
        null);
    lang.nextStep("Monatsziffern");

    cMZiffern.hide();
    sc.toggleHighlight(1, 2);
    counter.assignmentsInc(1);
    int jahrImJahrhundert = jahr % 100;
    datum_array.highlightCell(2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    Text cJIJahrhundert = lang.newText(new Offset(0, space, cMZiffern,
        AnimalScript.DIRECTION_NW), "Jahr im Jahrhundert = "
        + jahrImJahrhundert, "cJIJahrhundert", null, textProps);
    highlightText(cJIJahrhundert);
    lang.nextStep("Jahr im Jahrhundert");

    sc.toggleHighlight(2, 3);
    counter.assignmentsInc(1);
    int jahrhundert = jahr / 100;
    Text cJahrhundert = lang.newText(new Offset(0, 0, cJIJahrhundert,
        AnimalScript.DIRECTION_SW), "Jahrhundert = " + jahrhundert,
        "cJahrhundert", null, textProps);
    unhighlightText(cJIJahrhundert);
    highlightText(cJahrhundert);
    lang.nextStep("Jahrhundert");

    sc.toggleHighlight(3, 4);
    counter.assignmentsInc(1);
    int tagesziffer = tag % 7;
    datum_array.unhighlightCell(2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    datum_array.highlightCell(0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    Text cTZiffer = lang.newText(new Offset(0, 0, cJahrhundert,
        AnimalScript.DIRECTION_SW), "Tagesziffer = " + tagesziffer, "cTZiffer",
        null, textProps);
    unhighlightText(cJahrhundert);
    highlightText(cTZiffer);
    lang.nextStep("Tagesziffer");

    sc.toggleHighlight(4, 5);
    int monatsziffer = monatsziffern[monat - 1];
    datum_array.unhighlightCell(0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    datum_array.highlightCell(1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    mZiffern_array.highlightCell(monat - 1, Timing.INSTANTEOUS,
        Timing.INSTANTEOUS);
    Text cMZiffer = lang.newText(new Offset(0, 0, cTZiffer,
        AnimalScript.DIRECTION_SW), "Monatsziffer = " + monatsziffer,
        "cMZiffer", null, textProps);
    unhighlightText(cTZiffer);
    highlightText(cMZiffer);
    lang.nextStep("Monatsziffer");

    sc.toggleHighlight(5, 6);
    counter.assignmentsInc(3);
    int jahresziffer = (jahrImJahrhundert + (jahrImJahrhundert / 4)) % 7;
    datum_array.unhighlightCell(1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    mZiffern_array.unhighlightCell(monat - 1, Timing.INSTANTEOUS,
        Timing.INSTANTEOUS);
    Text cJZiffer = lang.newText(new Offset(0, 0, cMZiffer,
        AnimalScript.DIRECTION_SW), "Jahresziffer = " + jahresziffer,
        "cJZiffer", null, textProps);
    unhighlightText(cMZiffer);
    highlightText(cJZiffer);
    SourceCode cJZifferR = lang.newSourceCode(new Offset(0, 0, cJZiffer,
        AnimalScript.DIRECTION_SW), "cJZifferR", null, text_style);
    cJZifferR.addCodeLine("(" + jahrImJahrhundert + "+(" + jahrImJahrhundert
        + "/4)) mod 7", null, 0, null);
    cJZifferR.addCodeLine("(" + jahrImJahrhundert + "+" + jahrImJahrhundert / 4
        + ") mod 7", null, 0, null);
    cJZifferR.addCodeLine("(" + (jahrImJahrhundert + (jahrImJahrhundert / 4))
        + ") mod 7 =" + jahresziffer, null, 0, null);
    cJZifferR.highlight(0);
    lang.nextStep("Jahresziffer");

    cJZifferR.toggleHighlight(0, 1);
    lang.nextStep();
    cJZifferR.toggleHighlight(1, 2);
    lang.nextStep();

    sc.toggleHighlight(6, 7);
    cJZifferR.hide();
    counter.assignmentsInc(3);
    int jahrhundertsziffer = (3 - (jahrhundert % 4)) * 2;
    Text cJhZiffer = lang.newText(new Offset(0, 0, cJZiffer,
        AnimalScript.DIRECTION_SW), "Jahrhundertsziffer = "
        + jahrhundertsziffer, "cJhZiffer", null, textProps);
    unhighlightText(cJZiffer);
    highlightText(cJhZiffer);
    SourceCode cJhZifferR = lang.newSourceCode(new Offset(0, 0, cJhZiffer,
        AnimalScript.DIRECTION_SW), "cJhZifferR", null, text_style);
    cJhZifferR
        .addCodeLine("(3 - (" + jahrhundert + " % 4)) * 2", null, 0, null);
    cJhZifferR.addCodeLine("(3 - " + jahrhundert % 4 + ") * 2", null, 0, null);
    cJhZifferR.addCodeLine((3 - (jahrhundert % 4)) + " * 2 = "
        + jahrhundertsziffer, null, 0, null);
    cJhZifferR.highlight(0);
    lang.nextStep("Jahrhundertsziffer");

    cJhZifferR.toggleHighlight(0, 1);
    lang.nextStep();
    cJhZifferR.toggleHighlight(1, 2);
    lang.nextStep();

    sc.toggleHighlight(7, 8);
    cJhZifferR.hide();
    counter.assignmentsInc(3);
    counter.accessInc(2);
    boolean schaltjahr = ((jahr % 4 == 0 && jahr % 100 != 0) || jahr % 400 == 0);
    int schaltjahreskorrektur = schaltjahr ? 6 : 0;
    Text cSjahr = lang.newText(new Offset(0, 0, cJhZiffer,
        AnimalScript.DIRECTION_SW), "Schaltjahreskorrektur = "
        + schaltjahreskorrektur, "cSjahr", null, textProps);
    unhighlightText(cJhZiffer);
    highlightText(cSjahr);
    datum_array.highlightCell(2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    SourceCode cSjahrR = lang.newSourceCode(new Offset(0, 0, cSjahr,
        AnimalScript.DIRECTION_SW), "cSjahrR", null, text_style);
    cSjahrR
        .addCodeLine(
            "Die Schaltjahreskorrektur beträgt 6, falls das Jahr ein Schaltjahr ist, ansonsten 0.",
            null, 0, null);
    cSjahrR.addCodeLine("Ein Schaltjahr ist, wenn", null, 0, null);
    cSjahrR.addCodeLine("Jahr durch 4 teilbar ist (" + jahr
        + " mod 4 == 0), nicht aber durch 100 (" + jahr + " mod 100 != 0)",
        null, 1, null);
    cSjahrR.addCodeLine(jahr % 4 + "==0 && " + jahr % 100 + "!=0 "
        + ((jahr % 4 == 0 && jahr % 100 != 0) ? "Wahr" : "Falsch"), null, 2,
        null);
    cSjahrR.addCodeLine("oder Jahreszahl durch 400 teilbar ist (" + jahr
        + " mod 400 == 0)", null, 1, null);
    cSjahrR.addCodeLine(jahr % 400 + "==0 "
        + ((jahr % 400 == 0) ? "Wahr" : "Falsch"), null, 2, null);
    cSjahrR.addCodeLine("=> " + (schaltjahr ? "" : "kein ") + "Schaltjahr",
        null, 1, null);
    lang.nextStep("Schaltjahreskorrektur");

    sc.toggleHighlight(8, 10);
    sc.highlight(11);
    cSjahrR.hide();
    counter.assignmentsInc(5);
    int wochentag = (tagesziffer + monatsziffer + jahresziffer
        + jahrhundertsziffer + schaltjahreskorrektur) % 7;
    Text cWTag = lang.newText(new Offset(0, space, cSjahr,
        AnimalScript.DIRECTION_SW), "(" + tagesziffer + " + " + monatsziffer
        + " + " + jahresziffer + " + " + jahrhundertsziffer + " + "
        + schaltjahreskorrektur + ") % 7 = " + wochentag, "cWTag", null,
        textProps);
    unhighlightText(cSjahr);
    highlightText(cWTag);
    datum_array.unhighlightCell(2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    lang.nextStep("Wochentag");

    sc.toggleHighlight(10, 14);
    sc.unhighlight(11);
    Text cRet = lang.newText(new Offset(0, 5 * space, cWTag,
        AnimalScript.DIRECTION_SW), "wochentage[" + wochentag + "] = "
        + wochentage[wochentag], "cRet", null, textProps);
    unhighlightText(cWTag);
    highlightText(cRet);
    wTage_array
        .highlightCell(wochentag, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    lang.nextStep("Return");

    cJIJahrhundert.hide();
    cJZiffer.hide();
    cJahrhundert.hide();
    cJhZiffer.hide();
    cMZiffer.hide();
    cRet.hide();
    cSjahr.hide();
    cTZiffer.hide();
    cWTag.hide();
    sc.hide();
    lang.nextStep();
    return wochentag;
  }

  private void showDesc() {
    desc = lang.newSourceCode(new Offset(20, 80, title,
        AnimalScript.DIRECTION_SW), "desc", null, text_style);

    desc.addCodeLine(
        "Der Algorithmus (Wochentagsberechnung oder auch Kalenderrechnen) berechnet aus einem gegebenen Datum den jeweiligen Wochentag des Gregorianischen Kalenders.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Die Wochentagsberechnung ist eine Disziplin bei Meisterschaften im Kopfrechnen.",
        null, 0, null);
  }

  private void showSourceCode() {
    sc = lang.newSourceCode(new Offset(0, 55, wTage_array,
        AnimalScript.DIRECTION_SW), "code", null, sourcecode_style);

    sc.addCodeLine(
        "public String wochentagsberechnung (int tag, int monat, int jahr) {",
        null, 0, null);
    sc.addCodeLine(
        "int[] monatsziffern = {0, 3, 3, 6, 1, 4, 6, 2, 5, 0, 3, 5};", null, 1,
        null);
    sc.addCodeLine("int jahrImJahrhundert = jahr%100;", null, 1, null);
    sc.addCodeLine("int jahrhundert = jahr/100;", null, 1, null);
    sc.addCodeLine("int tagesziffer = tag % 7;", null, 1, null);
    sc.addCodeLine("int monatsziffer = monatsziffern[monat-1];", null, 1, null);
    sc.addCodeLine(
        "int jahresziffer = (jahrImJahrhundert+(jahrImJahrhundert/4)) % 7;",
        null, 1, null);
    sc.addCodeLine("int jahrhundertsziffer = (3 - (jahrhundert % 4)) * 2;",
        null, 1, null);
    sc.addCodeLine(
        "int schaltjahreskorrektur = ((jahr % 4 == 0 && jahr % 100 != 0) || jahr % 400 == 0)? 6:0;",
        null, 1, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine(
        "int wochentag = (tagesziffer + monatsziffer + jahresziffer", null, 1,
        null);
    sc.addCodeLine("+ jahrhundertsziffer + schaltjahreskorrektur) % 7;", null,
        2, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine(
        "String[] wochentage = {\\\"So\\\", \\\"Mo\\\", \\\"Di\\\", \\\"Mi\\\", \\\"Do\\\", \\\"Fr\\\", \\\"Sa\\\"};",
        null, 1, null);
    sc.addCodeLine("return wochentage[wochentag];", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
  }

  private void highlightText(Text t) {

    Color ccolor = (Color) text_style.getItem(
        AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY).get();
    t.changeColor("", ccolor, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

  }

  private void unhighlightText(Text t) {

    Color ccolor = (Color) text_style.getItem(
        AnimationPropertiesKeys.COLOR_PROPERTY).get();
    t.changeColor("", ccolor, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer apc,
      Hashtable<String, Object> hshtbl) throws IllegalArgumentException {
    int[] tage = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    int t = (Integer) hshtbl.get("tag");
    int m = (Integer) hshtbl.get("monat");
    int j = (Integer) hshtbl.get("jahr");

    boolean error = false;

    if (j < 0)
      error = true;

    if ((j % 4 == 0 && j % 100 != 0) || j % 400 == 0)
      tage[1] = 29;

    if (m > 12 || m < 1)
      error = true;
    if (t > tage[monat] || t < 1)
      error = true;

    if (error)
      throw new IllegalArgumentException("Kein gültiges Datum angegeben.");
    return true;
  }
}