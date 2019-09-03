package generators.sorting.shakersort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class ShakerSort1 implements generators.framework.Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language        lang;
  private IntArray        ia;
  private SourceCode      sc, sc1;
  private ArrayMarker     lMarker, rMarker;
  protected int           letzteHighlightedZeile = -1;
  private ArrayProperties arrayProps;
  private int[]           a;
  ShakerSort1             ss;

  public ShakerSort1() {
    this(new AnimalScript("Shaker Sort", "Emal Rahman", 640, 480));
  }

  /**
   * Default Constructor
   * 
   * @param l
   *          the concrete language object used for creating output
   */
  public ShakerSort1(Language l) {

    lang = l;
    lang.setStepMode(true);
  }

  /*
   * Receives the settings effected by the user and delivers the animation code
   * as a string back.
   * 
   * @param 1 props
   * 
   * @param 2 string
   * 
   * @param 3 primitives
   */
  public String generate(AnimationPropertiesContainer props,
      java.util.Hashtable<String, Object> primitive) {

    lang = new AnimalScript("Shaker Sort", "Emal Rahman", 640, 480);
    ss = new ShakerSort1(lang);
    a = (int[]) primitive.get("ia");
    arrayProps = (ArrayProperties) props.getPropertiesByName("arrayP");
    ss.localInit(a, arrayProps);
    ss.shakerSort();
    return lang.toString();

  }

  /*
   * Returns the name of the algorithm
   */
  public String getAlgorithmName() {
    return "Shaker Sort";
  }

  /*
   * Delivers the name of the author
   */
  public String getAnimationAuthor() {
    return "Emal Rahman";
  }

  /*
   * Delivers the used Source or Pseudo-Code. With it be sure the reader before
   * choice of the generator is able to have taken the "right one".
   */
  public String getCodeExample() {

    return "public void sort(E[] sammlung) { 									\n "
        + "	boolean austausch; 												\n"
        + "	int links = 1;													\n"
        + "	int rechts = sammlung.length-1;									\n"
        + "	int fertig = rechts;											\n" + "																	\n"
        + "	do {															\n" + "		austausch = false;											\n"
        + "		for (int ab = rechts; ab >= links; ab--)					\n"
        + "			if (sammlung[ab].compareTo(sammlung[ab-1]) < 0){		\n"
        + "				austausch = true; fertig = ab;						\n"
        + "				final E temp = sammlung[ab-1];						\n"
        + "				sammlung[ab-1]=sammlung[ab]; sammlung[ab]=temp;		\n"
        + "   			}														\n" + "  		links = fertig + 1;											\n"
        + "  		for (int auf = links; auf <= rechts; auf++);				\n"
        + "   			if (sammlung[auf].compareTo(sammlung[auf-1]) < 0){		\n"
        + "    			austausch = true; fertig = auf;						\n"
        + "    			final E temp = sammlung[auf-1];						\n"
        + "    			sammlung[auf-1] = sammlung[auf]; sammlung[auf] = temp;\n"
        + "			}														\n" + "		rechts = fertig - 1;										\n"
        + "	}																\n" + "	while (austausch);												\n" + "}";
  }

  /*
   * Delivers the "Locale" and with it the output-Language for the generator -
   * mostly java.util. Locale. GERMANY or another constant of the class
   * java.util. Locale.
   */
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  /*
   * Delivers a short textuelle description of the visualised ones Contents.
   */

  public String getDescription() {
    return "Shakersort bezeichnet einen stabilen Sortieralgorithmus, der eine Menge von linear angeordneten\n"
        + "Elementen (z. B. Zahlen) der Größe nach sortiert. Das zu sortierende Feld wird abwechselnd nach\n"
        + "oben und nach unten durchlaufen. Dabei werden jeweils zwei benachbarte Elemente verglichen und \n"
        + "gegebenenfalls vertauscht. Durch diese Bidirektionalität kommt es zu einem schnellerem Absetzen\n"
        + "von großen bzw. kleinen Elementen. Anhand des Sortierverfahrens lässt sich auch der Name erklären,\n"
        + "denn der Sortiervorgang erinnert an das Schütteln des Arrays oder eines Barmixers.\n";
  }

  /*
   * Delivers the file ending; had to go "asu" for the uncompressed ANIMALSCRIPT
   * as a result have.
   */
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /*
   * Delivers the type of the algorithm back. Please, are of use the predefined
   * constants in class GeneratorType - GeneratorType. GENERATOR_TYPE_X - which
   * you directly in the constructor of GeneratorType - if necessary with "+"
   * ties together - can hand over.
   */
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  /*
   * Gives the name elective by you of the algorithm for the announcement in the
   * list back, so possibly "Bubble Sort with untimely demolition" or
   * "Shaker Sort".
   */
  public String getName() {
    return "Shaker Sort";
  }

  /*
   * As a rule generator. PSEUDO_CODE_OUTPUT, generator. JAVA_CODE_OUTPUT or
   * another string, possibly "DELPHI".
   */
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /*
   * Initializes the generator in such a way, that he also with multiple ones To
   * calls from generate (...) on the same object always with the right "state"
   * starts.
   */
  public void localInit(int[] aa, ArrayProperties aP) {
    sc = lang
        .newSourceCode(new Coordinates(4, 4), "sourceCode", null, getSCP());
    sc1 = lang.newSourceCode(new Coordinates(500, 4), "SourceCode1", null,
        getSCP());
    ia = lang.newIntArray(new Coordinates(600, 400), aa, "intArray", null, aP);
    ia.hide();
    lMarker = lang.newArrayMarker(ia, 1, "Rechts - 1", null, getAMPR());
    lMarker.hide();
    rMarker = lang.newArrayMarker(ia, ia.getLength() - 1, "Rechts", null,
        getAMPB());
    rMarker.hide();

  }

  public void init() {
  }

  /**
   * The method zeigeLinie provides for the fact that only one line is
   * emphasized.
   * 
   * @param x
   *          is the line number
   * @param soco
   *          is the SourceCode which must be emphasized
   */
  public void zeigeLinie(int x, SourceCode soco) {
    if (letzteHighlightedZeile != -1) {
      soco.unhighlight(letzteHighlightedZeile, 0, false);
    }

    letzteHighlightedZeile = x;
    soco.highlight(x, 0, false);
  }

  /**
   * The method getSCP delivers the Sourcecode Porperties
   */
  public static SourceCodeProperties getSCP() {
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    return scProps;
  }

  /**
   * The Method getAMP delivers the Arraymarker Porperties with black Color
   */
  public static ArrayMarkerProperties getAMPB() {
    ArrayMarkerProperties arrayMProps = new ArrayMarkerProperties();
    arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Rechts ");
    arrayMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    return arrayMProps;
  }

  /**
   * The Method getAMP delivers the Arraymarker Porperties with black Color
   */
  public static ArrayMarkerProperties getAMPR() {
    ArrayMarkerProperties arrayMProps = new ArrayMarkerProperties();
    arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Links");
    arrayMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    return arrayMProps;
  }

  /**
   * The method leseArray converts an Integer-Array into string with comma and
   * curly braces
   * 
   * @param x
   *          is for the handing over of Integer-Array
   */
  public static String leseArray(IntArray x) {
    String Array = new String();
    Array = "{";
    int c;
    for (int i = 0; i < x.getLength() - 1; i++) {
      c = x.getData(i);
      Array = Array + Integer.toString(c) + "," + " ";
    }
    c = x.getLength();
    c = x.getData(c - 1);
    Array = Array + Integer.toString(c) + "}";
    return Array;
  }

  /**
   * Dump Java-Code
   * 
   * @param sammlung
   *          Integer-Array for dump on Screen
   */
  public void sort(int[] sammlung) {

  }

  /**
   * Sorts array by exchanging neighbouring elements
   */
  public void shakerSort() { //
    sc.addCodeLine("public void sort(E[] sammlung) {", null, 0, null); // 0
    sc.addCodeLine("boolean austausch;", null, 1, null); // 1
    sc.addCodeLine("int links = 1;", null, 1, null); // 2
    sc.addCodeLine("int rechts = sammlung.length-1;", null, 1, null); // 3
    sc.addCodeLine("int fertig = rechts;", null, 1, null); // 4
    sc.addCodeLine("do {", null, 1, null); // 5
    sc.addCodeLine("austausch = false;", null, 2, null); // 6
    sc.addCodeLine("for (int ab = rechts; ab >= links; ab--)", null, 2, null); // 7
    sc.addCodeLine("if (sammlung[ab].compareTo(sammlung[ab-1]) < 0)", null, 3,
        null); // 8
    sc.addCodeLine("austausch = true; fertig = ab;", null, 4, null); // 9
    sc.addCodeLine("final E temp = sammlung[ab-1];", null, 4, null); // 10
    sc.addCodeLine("sammlung[ab-1]=sammlung[ab]; sammlung[ab]=temp;", null, 4,
        null); // 11
    sc.addCodeLine("}", null, 3, null); // 12
    sc.addCodeLine("links = fertig + 1;", null, 2, null); // 13
    sc.addCodeLine("for (int auf = links; auf <= rechts; auf++);", null, 2,
        null); // 14
    sc.addCodeLine("if (sammlung[auf].compareTo(sammlung[auf-1]) < 0)", null,
        3, null); // 15
    sc.addCodeLine("austausch = true; fertig = auf;", null, 3, null); // 16
    sc.addCodeLine("final E temp = sammlung[auf-1];", null, 3, null); // 17
    sc.addCodeLine("sammlung[auf-1] = sammlung[auf]; sammlung[auf] = temp;",
        null, 2, null); // 18
    sc.addCodeLine("}", null, 1, null); // 19
    sc.addCodeLine("rechts = fertig - 1;", null, 2, null); // 20
    sc.addCodeLine("} while (austausch);", null, 1, null); // 21
    sc.addCodeLine("}", null, 0, null); // 22
    lang.nextStep();

    sc1.addCodeLine("Folgende Array ist gegeben: " + leseArray(ia), null, 0,
        null);
    zeigeLinie(0, sc);
    lang.nextStep();

    boolean austausch;// 1
    zeigeLinie(1, sc);
    lang.nextStep();

    int links = 1; // 2 anfangen beim zweiten Element
    lMarker.show();
    ia.show();
    zeigeLinie(2, sc);
    lang.nextStep();

    int rechts = ia.getLength() - 1; // 3
    rMarker.show();
    zeigeLinie(3, sc);
    lang.nextStep();

    int fertig = rechts; // 4
    zeigeLinie(4, sc);
    lang.nextStep();

    do { // 5
      zeigeLinie(5, sc);
      lang.nextStep();

      austausch = false; // 6
      zeigeLinie(6, sc);
      lang.nextStep();

      zeigeLinie(7, sc);
      lang.nextStep();
      for (int ab = rechts; ab >= links; ab--)
        // 7 abwaerts
        if (ia.getData(ab) < ia.getData(ab - 1)) { // 8
          rMarker.move(ab, null, null);
          zeigeLinie(8, sc);
          lang.nextStep();

          austausch = true;
          fertig = ab; // 9
          zeigeLinie(9, sc);
          lang.nextStep();

          // 10
          zeigeLinie(10, sc);
          lang.nextStep();

          // 11
          zeigeLinie(11, sc);
          ia.swap(ab, ab - 1, null, null);
          lang.nextStep();
        } // 12

      zeigeLinie(12, sc);
      lang.nextStep();

      links = fertig + 1; // 13
      zeigeLinie(13, sc);
      lang.nextStep();

      zeigeLinie(14, sc);
      lang.nextStep();
      for (int auf = links; auf <= rechts; auf++)
        // 14 aufwaerts

        if (ia.getData(auf) < ia.getData(auf - 1)) { // 15
          zeigeLinie(15, sc);
          lMarker.move(auf, null, null);
          lang.nextStep();

          austausch = true;
          fertig = auf; // 16
          zeigeLinie(16, sc);
          lang.nextStep();

          // 17
          zeigeLinie(17, sc);
          lang.nextStep();

          // 18
          ia.swap(auf - 1, auf, null, null);
          zeigeLinie(18, sc);
          lang.nextStep();
        }// 19
      zeigeLinie(19, sc);
      lang.nextStep();

      rechts = fertig - 1; // 20
      zeigeLinie(20, sc);
      lang.nextStep();

    } while (austausch); // 21
    zeigeLinie(21, sc);
    lang.nextStep();

    zeigeLinie(22, sc);
    lang.nextStep();
  } // 22

}
