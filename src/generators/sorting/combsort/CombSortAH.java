package generators.sorting.combsort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Alexander Heinz
 * @version 1.0 2010-23-07
 * 
 */
public class CombSortAH extends AnnotatedAlgorithm implements
    generators.framework.Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language lang;

  /**
   * Default constructor
   */
  public CombSortAH() {
  }

  private static final String DESCRIPTION = "Beim CombSort Sortieralgorithmus werden, &auml;hnlich wie beim BubbleSort Algorithmus, "
                                              + "jeweils 2 Elemente verglichen und gegebenenfalls vertauscht. Dabei wird jedoch "
                                              + "zun&auml;chst eine gro&szlig;e L&uuml;cke zwischen den zu vergleichenden Elementen gew&auml;hlt, so "
                                              + "dass grob falsch sortierte Elemente schneller ihre Zielposition finden. Diese "
                                              + "L&uuml;cke wird nach jedem Durchlauf verkleinert, bis schließlich benachbarte Elemente "
                                              + "verglichen werden. Findet dabei kein Austausch mehr statt, sind die Elemente sortiert.";

  private static final String SOURCE_CODE = "0. L&uuml;cke = L&auml;nge des Arrays"
                                              + "\n1. Tausch = 0"
                                              + "\n2. L&uuml;cke verkleinern (L&uuml;cke/1.3)"
                                              + "\n3. F&uuml;r jeden mit dieser L&uuml;cke m&ouml;glichen Vergleich:"
                                              + "\n  4. Zeiger auf aktuelle Elemente setzen"
                                              + "\n  5. Elemente vergleichen, gegebenenfalls tauschen und Tausch = 1 setzen"
                                              + "\n  6. Weiter mit n&auml;chstem Elementepaar"
                                              + "\n7. Falls L&uuml;cke > 1 oder Tausch = 1: Springe zu 1."
                                              + "\nNun ist das Feld sortiert";

  /**
   * initialize primitives
   * 
   * @param arrayProps
   *          properties of the array
   * @param scProps
   *          properties of the sourcecode
   * @param a
   *          array to sort
   * @param markerColor
   *          color of the arraypointers
   */
  public void init(ArrayProperties arrayProps, SourceCodeProperties scProps,
      int[] a, Color markerColor) {
    super.init();

    // header text
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    // Text header =
    lang.newText(new Coordinates(70, 30), "CombSort", "header", null, textProps);

    // rectangle around header
    // Rect hrect =
    lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5, "header",
        "SE"), "hRect", null);

    // create array to sort
    IntArray ia = lang.newIntArray(new Coordinates(100, 100), a, "arr", null,
        arrayProps);

    // create the sourcecode
    sourceCode = lang.newSourceCode(new Coordinates(10, 200), "listSource",
        null, scProps);

    lang.nextStep("Übersicht");

    // parse the annotated sourcecode
    parse();

    // start combsort
    sort(ia, sourceCode, markerColor);
  }

  /**
   * sort: sorts the given array using combsort
   * 
   * @param array
   *          the IntArray to be sorted
   * @param codeSupport
   *          the underlying code instance
   * @param markerColor
   *          color of the marker which point to the array positions
   */
  private void sort(IntArray array, SourceCode codeSupport, Color markerColor)
      throws LineNotExistsException {
    // create ArrayMarker
    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, markerColor);
    ArrayMarker Marker1 = lang.newArrayMarker(array, 0, "pointer1", null,
        arrayIMProps);
    ArrayMarker Marker2 = lang.newArrayMarker(array, 0, "pointer2", null,
        arrayIMProps);

    // create texts and textupdater for luecke and tausch
    Text TauschText = lang.newText(new Offset(0, 5, "arr", "SW"), "Tausch:",
        "tauschText", null);
    TextUpdater tauschTU = new TextUpdater(TauschText);
    Text LueckeText = lang.newText(new Offset(0, 5, "tauschText", "SW"),
        "Lücke:", "lueckeText", null);
    TextUpdater lueckeTU = new TextUpdater(LueckeText);

    exec("iniluecke");
    vars.set("luecke", String.valueOf(array.getLength()));

    lueckeTU.addToken("Lücke: ");
    lueckeTU.addToken(vars.getVariable("luecke"));
    lueckeTU.update();
    lang.nextStep();

    tauschTU.addToken("Tausch: ");
    tauschTU.addToken(vars.getVariable("tausch"));
    tauschTU.update();
    do {
      exec("initausch");
      lang.nextStep();

      exec("decluecke");
      if (Integer.parseInt(vars.get("luecke")) < 1)
        vars.set("luecke", String.valueOf("1"));
      lang.nextStep();

      for (int i = 0; i < array.getLength()
          - Integer.parseInt(vars.get("luecke")); i++) {
        exec("setmarker");
        Marker1.move(i, null, null);
        Marker2.move(i + Integer.parseInt(vars.get("luecke")), null, null);
        lang.nextStep();

        exec("switch");
        if (array.getData(i) > array.getData(i
            + Integer.parseInt(vars.get("luecke")))) {
          array.swap(i, i + Integer.parseInt(vars.get("luecke")),
              new TicksTiming(50), new TicksTiming(100));
          vars.set("tausch", "1");
        }
        lang.nextStep();

        exec("inci");
        lang.nextStep();

        exec("jumpcond");
      }
      lang.nextStep();

    } while (Integer.valueOf(vars.get("luecke")).intValue() > 1
        | vars.get("tausch").equals("1"));
    exec("done");
  }

  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  public String getName() {
    return "Comb Sort";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    lang = new AnimalScript("CombSort", "Alexander Heinz", 640, 480);
    lang.setStepMode(true);
    init((ArrayProperties) arg0.get(0), (SourceCodeProperties) arg0.get(1),
        (int[]) arg1.get("array"), (Color) arg1.get("markerColor"));
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Comb Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Alexander Heinz";
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
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {

  }

  @Override
  public String getAnnotatedSrc() {
    return "0. Lücke = Länge des Arrays                @label(\"iniluecke\") @declare(\"int\", \"luecke\") @declare(\"int\", \"tausch\")\n"
        + "1. Tausch = 0                             @label(\"initausch\") @set(\"tausch\", \"0\")\n"
        + "2. Lücke verkleinern (Lücke/1.3)          @label(\"decluecke\") @eval(\"luecke\", \"luecke / 1.3\")\n"
        + "3. Für jeden mit dieser Lücke möglichen Vergleich: @label(\"loopstart\")\n"
        + "  4. Zeiger auf aktuelle Elemente setzen  @label(\"setmarker\")\n"
        + "  5. Elemente vergleichen, gegebenenfalls tauschen und Tausch = 1 setzen @label(\"switch\")\n"
        + "  6. Weiter mit nächstem Elementepaar     @label(\"inci\")\n"
        + "7. Falls Lücke > 1 oder Tausch = 1: Springe zu 1. @label(\"jumpcond\")\n"
        + "Nun ist das Feld sortiert                 @label(\"done\")\n";
  }
}
