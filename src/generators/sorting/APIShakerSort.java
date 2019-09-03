package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
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

/**

 *
 */
public class APIShakerSort implements generators.framework.Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language lang;

  public APIShakerSort() {
    this(new AnimalScript("Shakersort Animation",
        "Florian Kryst und David Kuhn", 640, 480));
  }

  /**
   * Default constructor
   * 
   * @param l
   *          the concrete language object used for creating output
   */
  public APIShakerSort(Language l) {
    lang = l;
    lang.setStepMode(true);
  }

  private static final String DESCRIPTION = "Das zu sortierende Feld wird abwechselnd nach oben und nach unten durchlaufen."
                                              + " Dabei werden jeweils zwei benachbarte Elemente verglichen und gegebenenfalls vertauscht."
                                              + "Durch diese Bidirektionalität kommt es zu einem schnellerem Absetzen von großen bzw. kleinen Elementen."
                                              + " Anhand des Sortierverfahrens lässt sich auch der Name erklären, denn der Sortiervorgang erinnert an das Schütteln"
                                              + " des Arrays oder eines Barmixers.";

  private static final String SOURCE_CODE = "public void shakerSort(int[] array)" // 0
                                              + "\n{" // 1
                                              + "\n  boolean austausch;" // 2
                                              + "\n  int links = 1; //beginnt beim zweiten Element" // 3
                                              + "\n  int rechts = (array.length)-1;" // 4
                                              + "\n  int fertig = rechts;" // 5
                                              + "\n  do{" // 6
                                              + "\n     austausch = false;" // 7
                                              + "\n     for (int i = rechts; i >= links; i--) //Laufe rückwärts - beginne beim Zeiger rechts" // 8
                                              + "\n        if(array[i].compareTo(array[i-1]) < 0){ //wenn aktuelles Element kleiner als Vorgänger" // 9
                                              + "\n           austausch = true; fertig = i; //Tausche die Elemente" // 10
                                              + "\n           int temp = array[i-1]; //durch einen Dreickstausch" // 11
                                              + "\n           array[i-1]=array[i]; array[i]=temp;" // 12
                                              + "\n        }" // 13
                                              + "\n     links = fertig + 1; //fertig ist das zuletzt getauschte Element" // 14
                                              + "\n     for (int i = links; i <= rechts; i++) //gehe zum nächsten Element" // 15
                                              + "\n        if (array[i].compareTo(array[i-1]) < 0){ //wenn aktuelles Element kleiner als Vorgänger" // 16
                                              + "\n          austausch = true; fertig = i;" // 17
                                              + "\n          int temp = array[i-1]; //durch einen Dreickstausch" // 18
                                              + "\n          array[i-1]=array[i]; array[i]=temp;" // 19
                                              + "\n        }" // 10
                                              + "\n     rechts = fertig -1;" // 21
                                              + "\n     }while (austausch); //wiederhole solange ausgetsucht wird" // 22
                                              + "\n};";                             // 23

  /**
   * Sort the int array passed in
   * 
   * @param props
   *          the animation properties
   * @param primitives
   *          the primitives
   */
  public void sort(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    TicksTiming standardDuration = new TicksTiming(100);
    TicksTiming standardDelay = new TicksTiming(20);
    int[] a = (int[]) primitives.get("intArray");
    ArrayMarkerProperties arrayLinksProps = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker_left");
    ArrayMarkerProperties arrayRechtsProps = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker_right");
    ArrayMarkerProperties arrayFertigProps = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker_ready");
    ArrayMarkerProperties arrayCurrentProps = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker_current");
    TextProperties variablenProps = (TextProperties) props
        .getPropertiesByName("text");
    ArrayProperties arrayProps = (ArrayProperties) props
        .getPropertiesByName("array");
    SourceCodeProperties scProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    Font titleFont = new Font("SansSerif", Font.BOLD, 30);
    Font standardFont = new Font("SansSerif", Font.PLAIN, 15);
    variablenProps.set(AnimationPropertiesKeys.FONT_PROPERTY, titleFont);
    lang.newText(new Coordinates(0, 20), "ShakerSort", "sort_title", null,
        variablenProps);
    variablenProps.set(AnimationPropertiesKeys.FONT_PROPERTY, standardFont);

    IntArray ia = lang.newIntArray(new Coordinates(20, 100), a, "intArray",
        null, arrayProps);
    lang.nextStep();

    SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
        null, scProps);
    sc.addCodeLine("public void shakerSort(int[] array)", null, 0, null); // 0
    sc.addCodeLine("{", null, 0, null);
    sc.addCodeLine("boolean austausch;", null, 1, null);
    sc.addCodeLine("int links = 1;", null, 1, null); // 3
    sc.addCodeLine("int rechts = (array.length)-1;", null, 1, null); // 4
    sc.addCodeLine("int fertig = rechts;", null, 1, null); // 5
    sc.addCodeLine("do {", null, 2, null); // 6
    sc.addCodeLine("austausch = false;", null, 3, null); // 7
    sc.addCodeLine("for (int i = rechts; i >= links; i--)", null, 3, null); // 8
    sc.addCodeLine("if(array[i].compareTo(array[i-1]) < 0){", null, 4, null); // 9
    sc.addCodeLine("austausch = true; fertig = i;", null, 5, null); // 10
    sc.addCodeLine("int temp = array[i-1];", null, 5, null); // 11
    sc.addCodeLine("array[i-1]=array[i]; array[i]=temp;", null, 5, null); // 12
    sc.addCodeLine("}", null, 4, null); // 13
    sc.addCodeLine("links = fertig + 1;", null, 3, null); // 14
    sc.addCodeLine("for (int i = links; i <= rechts; i++)", null, 3, null); // 15
    sc.addCodeLine("if (array[i].compareTo(array[i-1]) < 0){", null, 4, null); // 16
    sc.addCodeLine("austausch = true; fertig = i;", null, 5, null); // 17
    sc.addCodeLine("int temp = array[i-1];", null, 5, null); // 18
    sc.addCodeLine("array[i-1]=array[i]; array[i]=temp;", null, 5, null); // 19
    sc.addCodeLine("}", null, 4, null); // 20
    sc.addCodeLine("rechts = fertig -1;", null, 3, null); // 21
    sc.addCodeLine("}while (austausch);", null, 2, null); // 22
    sc.addCodeLine("}", null, 0, null); // 23

    lang.nextStep();

    IntArray array = ia;
    SourceCode codeSupport = sc;

    ArrayMarker linksMarker = lang.newArrayMarker(array, 0, "links", null,
        arrayLinksProps);
    ArrayMarker rechtMarker = lang.newArrayMarker(array, 0, "rechts", null,
        arrayRechtsProps);
    ArrayMarker fertigMarker = lang.newArrayMarker(array, 0, "fertig", null,
        arrayFertigProps);
    ArrayMarker currentMarker = lang.newArrayMarker(array, 0, "i", null,
        arrayCurrentProps);

    /**
     * TextProperties variablenProps = new TextProperties();
     * variablenProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
     **/

    lang.newText(new Coordinates(380, 50), "Informationen:", "info_title",
        null, variablenProps);
    Text InformationenText = lang.newText(new Coordinates(380, 75), "",
        "info_text", null, variablenProps);

    lang.nextStep();

    codeSupport.toggleHighlight(0, 0, false, 2, 0);
    lang.newText(new Coordinates(380, 120), "Variablen:", "variablen_title",
        null, variablenProps);
    Text AustauschText = lang.newText(new Coordinates(380, 145), "austausch",
        "austausch", null, variablenProps);
    Text TempText = lang.newText(new Coordinates(380, 170), "", "temp", null,
        variablenProps);
    boolean austausch; // CODE
    lang.nextStep();

    codeSupport.toggleHighlight(2, 0, false, 3, 0);
    int links = 1; // CODE
    linksMarker.show();
    linksMarker.move(links, standardDelay, standardDuration);
    lang.nextStep();

    codeSupport.toggleHighlight(3, 0, false, 4, 0);
    int rechts = array.getLength() - 1;
    rechtMarker.show();
    rechtMarker.move(rechts, standardDelay, standardDuration);
    lang.nextStep();

    codeSupport.toggleHighlight(4, 0, false, 5, 0);
    int fertig = rechts; // CODE
    fertigMarker.show();
    fertigMarker.move(fertig, standardDelay, standardDuration);
    lang.nextStep();

    codeSupport.toggleHighlight(5, 0, false, 6, 0);
    do { // CODE
      lang.nextStep();

      codeSupport.unhighlight(22);
      codeSupport.toggleHighlight(6, 0, false, 7, 0);
      austausch = false; // CODE
      AustauschText.setText("austausch = false", null, null);
      lang.nextStep();
      codeSupport.unhighlight(7);

      for (int i = rechts; i >= links; i--) { // CODE
        InformationenText.setText("Gehe von rechts nach links durch das Array",
            null, null);
        currentMarker.show();
        currentMarker.move(i, standardDelay, standardDuration);
        codeSupport.highlight(8);
        array.unhighlightElem(0, array.getLength() - 1, null, null);
        lang.nextStep();

        array.highlightElem(i - 1, i, null, null);
        codeSupport.toggleHighlight(8, 0, false, 9, 0);
        lang.nextStep();
        if (array.getData(i) < array.getData(i - 1)) { // CODE
          InformationenText.setText("Element " + i
              + " ist kleiner als Element " + (i - 1) + ": " + array.getData(i)
              + "<" + array.getData(i - 1), null, null);
          lang.nextStep();

          codeSupport.toggleHighlight(9, 0, false, 10, 0);
          austausch = true;
          fertig = i; // CODE
          AustauschText.setText("austausch = true", null, null);
          fertigMarker.move(fertig, standardDelay, standardDuration);
          lang.nextStep();

          codeSupport.toggleHighlight(10, 0, false, 11, 0);
          int temp = array.getData(i - 1);// CODE
          InformationenText.setText("Element " + (i - 1)
              + " in temp zwischengespeichert.", null, null);
          TempText.setText("temp=" + temp, null, null);
          lang.nextStep();

          codeSupport.toggleHighlight(11, 0, false, 12, 0);
          InformationenText.setText("Element " + (i - 1)
              + " überschrieben und temp in Element " + i + " gespeichert.",
              null, null);
          array.swap(i - 1, i, standardDelay, standardDuration);
          lang.nextStep();

          codeSupport.unhighlight(12);
        } else {
          InformationenText.setText("Element " + (i)
              + " nicht kleiner als  Element " + (i - 1), null, null);
        }
        codeSupport.unhighlight(9);
        codeSupport.unhighlight(8);
      } // (for)

      codeSupport.highlight(14);
      links = fertig + 1; // CODE
      linksMarker.move(links, standardDelay, standardDuration);
      array.highlightCell(0, fertig - 1, null, null);
      array.unhighlightElem(0, array.getLength() - 1, null, null);
      lang.nextStep();
      codeSupport.unhighlight(14);

      for (int i = links; i <= rechts; i++) { // CODE
        InformationenText.setText("Gehe von links nach rechts durch das Array",
            null, null);
        codeSupport.unhighlight(19);
        codeSupport.highlight(15);
        currentMarker.move(i, standardDelay, standardDuration);
        array.unhighlightElem(0, array.getLength() - 1, null, null);
        lang.nextStep();

        array.highlightElem(i - 1, i, null, null);
        codeSupport.toggleHighlight(15, 0, false, 16, 0);
        lang.nextStep();
        if (array.getData(i) < array.getData(i - 1)) { // CODE
          InformationenText.setText("Element " + i
              + " ist kleiner als Element " + (i - 1) + ": " + array.getData(i)
              + "<" + array.getData(i - 1), null, null);

          codeSupport.toggleHighlight(16, 0, false, 17, 0);
          austausch = true;
          fertig = i; // CODE
          AustauschText.setText("austausch = true", null, null);
          fertigMarker.move(fertig, standardDelay, standardDuration);
          lang.nextStep();

          codeSupport.toggleHighlight(17, 0, false, 18, 0);
          @SuppressWarnings("unused")
          int temp = array.getData(i - 1);// CODE
          InformationenText.setText("Element " + (i - 1)
              + " in temp zwischengespeichert.", null, null);
          lang.nextStep();

          codeSupport.toggleHighlight(18, 0, false, 19, 0);
          InformationenText.setText("Element " + (i - 1)
              + " überschrieben und temp in Element " + i + " gespeichert.",
              null, null);
          array.swap(i - 1, i, standardDelay, standardDuration);
          lang.nextStep();

        }// CODE (if)
        codeSupport.unhighlight(15);
        codeSupport.unhighlight(16);
      }// (for)

      codeSupport.toggleHighlight(19, 0, false, 21, 0);
      array.unhighlightElem(0, array.getLength() - 1, null, null);
      rechts = fertig - 1; // CODE
      rechtMarker.move(rechts, standardDelay, standardDuration); //
      array.highlightCell(fertig, array.getLength() - 1, null, null);
      lang.nextStep();
      // rechtMarker.move(rechts, standardDelay, standardDuration); //
      codeSupport.toggleHighlight(21, 0, false, 22, 0);
    } while (austausch);
    lang.nextStep();
    codeSupport.unhighlight(22);
    lang.nextStep();
  }

  /**
   * counter for the number of pointers
   * 
   */
  @SuppressWarnings("unused")
  private int pointerCounter = 0;

  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  public String getName() {
    return "Shaker Sort";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    Language l = new AnimalScript("Shakersort Animation",
        "Florian Kryst und David Kuhn", 640, 480);
    APIShakerSort s = new APIShakerSort(l);
    s.sort(arg0, arg1);
    return l.toString();
  }

  public String getAlgorithmName() {
    return "Shaker Sort";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public String getAnimationAuthor() {
    return "David Kuhn, Florian Kryst";
  }

  @Override
  public void init() {
    // nothing to be done here

  }

}
