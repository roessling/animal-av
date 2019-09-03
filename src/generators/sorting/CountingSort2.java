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
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class CountingSort2 extends AnnotatedAlgorithm implements Generator {

  private static Language lang;
  private static String   algoName   = "Counting Sort";
  private static String   author     = "Can Güler, Tayfun Atik";
  private static int[]    resolution = new int[] { 640, 480 };

  private IntArray        in;
  private IntArray        out;
  private int[]           output;

  private String          comp       = "Vergleiche";
  private String          assi       = "Zuweisungen";

  // Font Style and Color
  private Font            font       = null;
  private Color           fontColor  = null;

  // Element Highlight Color
  private Color           inEH       = null;
  private Color           outEH      = null;

  // Element Color
  private Color           inEC       = null;
  private Color           outEC      = null;

  // Color Highlight
  private Color           inCH       = null;
  private Color           outCH      = null;

  public CountingSort2() {
  }

  /**
   * Initialisiere
   * 
   * @param input
   *          the array to be sorted
   */
  public void beginAlgo(int[] input) {

    int length = input.length;

    // Array mit Klartext-Buchstaben
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, inEC);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, inEH);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, inCH);

    // Array mit kodierten Werten, am Anfang unsichtbar
    ArrayProperties arrayProps2 = new ArrayProperties();
    arrayProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.CYAN);
    arrayProps2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
    arrayProps2.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    arrayProps2.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, outEC);
    arrayProps2.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, outEH);
    arrayProps2.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, outCH);

    // Array Beschriftung
    SourceCodeProperties arrayTitleProps = new SourceCodeProperties();
    arrayTitleProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    arrayTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(font.getFontName(), Font.PLAIN, 11));
    arrayTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);

    SourceCode numberIn = lang.newSourceCode(new Coordinates(460, 250),
        "numberIn", null, arrayTitleProps);
    numberIn.addCodeLine("numbers[] unsortiert", null, 0, null);
    numberIn.show();

    SourceCode numberOut = lang.newSourceCode(new Coordinates(460, 300),
        "numberIn", null, arrayTitleProps);
    numberOut.addCodeLine("numbers[] sortiert", null, 0, null);
    numberOut.show();

    output = new int[length];

    for (int i = 0; i < length; i++) {
      output[i] = 0;
    }

    // StringArray mit den entsprechenden Properties
    in = lang.newIntArray(new Coordinates(570, 250), input, "inArray", null,
        arrayProps);
    out = lang.newIntArray(new Coordinates(570, 300), output, "outArray", null,
        arrayProps2);

    countingSort(input);

  }

  private void countingSort(int[] numbers) {
    exec("header");
    int max = numbers[0];
    exec("var_marker1");
    exec("var_marker2");

    exec("iForInit");
    for (int i = 0; i < numbers.length; i++) {
      exec("iForComp1");
      exec("if");
      if (numbers[i] > max) {
        exec("setMax");
        max = numbers[i];
      }
      exec("iForInc");
    }
    exec("iForEnd");

    exec("var_sortnumber");
    int[] sortedNumbers = new int[max + 1];

    exec("jForInit");
    for (int j = 0; j < numbers.length; j++) {
      exec("jForComp1");
      sortedNumbers[numbers[j]] = sortedNumbers[numbers[j]] + 1;
      exec("setsortNumber");
      exec("jForInc");
    }
    exec("jForEnd");

    exec("var_fillheight");
    int fillheight = 0;

    exec("kForInit");
    for (int k = 0; k <= max; k++) {
      exec("kForComp1");
      exec("hForInit");
      for (int h = 0; h < sortedNumbers[k]; h++) {
        // Setze k Position vom Output und Input
        exec("hForComp1");
        in.highlightCell(fillheight, null, null);
        out.highlightCell(fillheight, null, null);
        out.put(fillheight, k, null, null);

        exec("numberFor");
        numbers[fillheight] = k;

        lang.nextStep();

        // Fuelle Array an k'ter Position
        in.unhighlightCell(fillheight, null, null);
        out.unhighlightCell(fillheight, null, null);
        exec("fillForInc");
        fillheight++;
        exec("hForInc");
      }
      exec("hForEnd");
      exec("kForInc");
    }
    exec("kForEnd");

    lang.nextStep();
    in.unhighlightCell(fillheight, null, null);
    out.unhighlightCell(fillheight, null, null);
    exec("return");
    exec("end");
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    System.err.println("arg1 " + (arg1 != null) + ", " + (arg0 != null));
    if (arg1 != null & arg0 != null) {

      font = (Font) arg1.get("Schriftart");
      fontColor = (Color) arg1.get("Schriftfarbe");
      inEH = (Color) arg1.get("EingabeArrayHC");
      outEH = (Color) arg1.get("AusgabeArrayHC");
      inEC = (Color) arg1.get("EingabeFarbe");
      outEC = (Color) arg1.get("AusgabeFarbe");
      inCH = (Color) arg1.get("EingabeArrayCH");
      outCH = (Color) arg1.get("AusgabeArrayCH");

      initLocal();

      this.beginAlgo((int[]) arg1.get("Eingabe"));
      System.err.println(lang.toString());
      return lang.toString();
    } else
      return "";
  }

  @Override
  public String getAlgorithmName() {
    return algoName;
  }

  public String getAnimationAuthor() {
    return author;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Countingsort ist ein stabiles Sortierverfahren."
        + "Es sortiert eine gegebene Zahlenfolge die in im "
        + "Intervall von [0,...,k] für ein festes, im Voraus bekanntes k liegen. "
        + "Für jedes zu sortierende Element x wird die Anzahl der Elemente bestimmt, "
        + "welche in der sortierten Reihenfolge vor x liegen, und platziere damit "
        + "x an die korrekte Stelle.";
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
    return algoName;
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void initLocal() {
    lang = new AnimalScript(algoName, author, resolution[0], resolution[1]);
    super.init();

    // Die Ueberschrift
    SourceCodeProperties titleProps = new SourceCodeProperties();
    titleProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(font.getFontName(), Font.PLAIN, 18));
    titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);

    SourceCode title = lang.newSourceCode(new Coordinates(60, 30), "title",
        null, titleProps);
    title.addCodeLine("Counting Sort", null, 0, null);
    title.show();

    SourceCodeProperties props = new SourceCodeProperties();
    props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.blue);
    props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);

    // instantiate source code primitive to work with
    sourceCode = lang.newSourceCode(new Coordinates(20, 100), "sourcecode",
        null, props);

    // setup complexity
    vars.declare("int", comp);
    vars.setGlobal(comp);
    vars.declare("int", assi);
    vars.setGlobal(assi);

    Text text = lang.newText(new Coordinates(500, 200), "Komplexitaet",
        "Counting Sort Komplexitaet", null);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken(comp + ": ");
    tu.addToken(vars.getVariable(comp));
    tu.addToken(" - " + assi + ": ");
    tu.addToken(vars.getVariable(assi));
    tu.update();

    parse();
  }

  @Override
  public String getAnnotatedSrc() {
    return "int[] countingSort(int[] numbers) {" + "@label(\"header\")\n"
        + " int max = numbers[0];" + "@label(\"var_marker1\")\n"
        + " int i,j,k,h;" + "@label(\"var_marker2\")\n" + " for (i = 0;"
        + "@label(\"iForInit\") @inc(\""
        + assi
        + "\")\n"
        + " i < numbers.length;"
        + "@label(\"iForComp1\") @continue @inc(\""
        + comp
        + "\")\n"
        + " i++) {"
        + "@label(\"iForInc\") @continue @inc(\""
        + assi
        + "\")\n"
        + " if (numbers[i] > max)"
        + "@label(\"if\") @inc(\""
        + comp
        + "\")\n"
        + "   max = numbers[i];"
        + "@label(\"setMax\") @inc(\""
        + assi
        + "\")\n"
        + " }"
        + "@label(\"iForEnd\")\n"
        + "\n"
        + " int[] sortedNumbers = new int[max+1];"
        + "@label(\"var_sortnumber\") @inc(\""
        + assi
        + "\")\n"
        + " for (j = 0;"
        + "@label(\"jForInit\") @inc(\""
        + assi
        + "\")\n"
        + " j < numbers.length;"
        + "@label(\"jForComp1\") @continue @inc(\""
        + comp
        + "\")\n"
        + " j++) {"
        + "@label(\"jForInc\") @continue @inc(\""
        + assi
        + "\")\n"
        + " sortedNumbers[numbers[j]] = sortedNumbers[numbers[j]] + 1;"
        + "@label(\"setsortNumber\") @inc(\""
        + assi
        + "\")\n"
        + " }"
        + "@label(\"jForEnd\")\n"
        + " int fillheight = 0;"
        + "@label(\"var_fillheight\") @inc(\""
        + assi
        + "\")\n"
        + " for (k = 0;"
        + "@label(\"kForInit\") @inc(\""
        + assi
        + "\")\n"
        + " k <= max;"
        + "@label(\"kForComp1\") @continue @inc(\""
        + comp
        + "\")\n"
        + " k++) {"
        + "@label(\"kForInc\") @continue @inc(\""
        + assi
        + "\")\n"
        + " for (h = 0;"
        + "@label(\"hForInit\") @inc(\""
        + assi
        + "\")\n"
        + " h < sortedNumbers[k];"
        + "@label(\"hForComp1\") @continue @inc(\""
        + comp
        + "\")\n"
        + " h++) {"
        + "@label(\"hForInc\") @continue @inc(\""
        + assi
        + "\")\n"
        + " numbers[fillheight] = k;"
        + "@label(\"numberFor\") @inc(\""
        + assi
        + "\")\n"
        + " fillheight++;"
        + "@label(\"fillForInc\") @inc(\""
        + assi
        + "\")\n"
        + " }"
        + "@label(\"hForEnd\")\n"
        + " }"
        + "@label(\"kForEnd\")\n"
        + " return numbers;"
        + "@label(\"return\")\n"
        + "}" + "@label(\"end\")\n";
  }

}