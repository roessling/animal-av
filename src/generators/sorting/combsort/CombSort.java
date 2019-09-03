package generators.sorting.combsort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

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
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class CombSort implements generators.framework.Generator {
  private Language lang;

  public CombSort() {
    this(new AnimalScript("CombSort Animation", "Florian Kryst und David Kuhn",
        640, 480));
  }

  public CombSort(Language l) {
    this.lang = l;
    lang.setStepMode(true);
  }

  private static final String DESCRIPTION = "Das Verfahren Combsort ist eine optimierte Version des bekannten Sortierverfahrens Bubblesort."
                                              + "Es benutzt einen kleinerwerdenden 'gap'-(Lücken)-Operator um Elemente zu Vergleichen die weitere als ein Element voneinander entfernt sind."
                                              + "Während diese Lücke zwischen den verglichenen Elementen kleiner wird, wird das Array in die richtige Reihenfolge gebracht."
                                              + "Zunächst werden hierzu Elemente vertauscht, die weiter auseinander liegen, dann solche die näher aneinander liegen."
                                              + "Durch Versuche des BYTE Magazins wurde herausgefunden, dass der optimale Verkleinerungsfaktor für die Lücke 1,3 ist."
                                              + "Die Komplexität liegt zwischen O(n^2) im schlechtesten und O(n log n) im besten Fall.";

  private static final String SOURCE_CODE = "public void combSort(int[] array){"
                                              + "\n    int gap, i, j;"
                                              + "\n    boolean fertig;"
                                              + "\n    float shrink = 1.3F;"
                                              + "\n    int elements = array.getLength();"
                                              + "\n    gap = elements;"
                                              + "\n    do{"
                                              + "\n       fertig = true;"
                                              + "\n       gap = (int)((float)gap/shrink);"
                                              + "\n       if (gap<1)	gap = 1;"
                                              + "\n       for (i=0; i<elements-gap; i++){"
                                              + "\n          j = i+gap;"
                                              + "\n          if (array.getData(i)>array.getData(j)){"
                                              + "\n             array.swap(i, j, null, null);"
                                              + "\n             fertig = false;"
                                              + "\n          }"
                                              + "\n       }"
                                              + "\n  }"
                                              + "\n  while (gap>1 || !fertig);"
                                              + "\n  }";

  // private int pointerCounter = 0;

  protected void sort(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    int[] a = (int[]) arg1.get("intArray");
    ArrayMarkerProperties arrayiProps = (ArrayMarkerProperties) arg0
        .getPropertiesByName("arrayMarker_i");
    ArrayMarkerProperties arrayjProps = (ArrayMarkerProperties) arg0
        .getPropertiesByName("arrayMarker_j");
    TextProperties variablenProps = (TextProperties) arg0
        .getPropertiesByName("text");
    ArrayProperties arrayProps = (ArrayProperties) arg0
        .getPropertiesByName("array");
    SourceCodeProperties scProps = (SourceCodeProperties) arg0
        .getPropertiesByName("sourceCode");
    Font titleFont = new Font("SansSerif", Font.BOLD, 30);
    Font standardFont = new Font("SansSerif", Font.PLAIN, 15);

    variablenProps.set(AnimationPropertiesKeys.FONT_PROPERTY, titleFont);
    // Überschrift wird angelegt
    lang.newText(new Coordinates(0, 20), "CombSort", "sort_title", null,
        variablenProps);
    variablenProps.set(AnimationPropertiesKeys.FONT_PROPERTY, standardFont);
    // Ausgaben für Variablen fertig(boolean) und gap(int) werden erzeugt
    Text TextvarGap = lang.newText(new Coordinates(300, 60), "", "varGap",
        null, variablenProps);
    Text TextvarFertig = lang.newText(new Coordinates(300, 100), "",
        "varFertig", null, variablenProps);
    IntArray intArray = lang.newIntArray(new Coordinates(20, 100), a,
        "intArray", null, arrayProps);

    // Zwischenschritt wird angelegt
    lang.nextStep();

    // SourceCode wird erstellt
    SourceCode sourceCode = lang.newSourceCode(new Coordinates(40, 140),
        "sourceCode", null, scProps);
    sourceCode.addCodeLine("public void combSort(int[] array){", null, 0, null); // 0
    sourceCode.addCodeLine("int gap, i, j;", null, 1, null); // 1
    sourceCode.addCodeLine("boolean fertig;", null, 1, null); // 2
    sourceCode.addCodeLine("float shrink = 1.3F;", null, 1, null); // 3
    sourceCode.addCodeLine("int elements = array.getLength();", null, 1, null); // 4
    sourceCode.addCodeLine("gap = elements;", null, 1, null); // 5
    sourceCode.addCodeLine("do{", null, 1, null); // 6
    sourceCode.addCodeLine("fertig = true;", null, 2, null); // 7
    sourceCode.addCodeLine("gap = (int)((float)gap/shrink);", null, 2, null); // 8
    sourceCode.addCodeLine("if (gap<1)	gap = 1;", null, 2, null); // 9
    sourceCode.addCodeLine("for (i=0; i<elements-gap; i++){", null, 2, null); // 10
    sourceCode.addCodeLine("j = i+gap;", null, 3, null); // 11
    sourceCode.addCodeLine("if (array.getData(i)>array.getData(j)){", null, 3,
        null); // 12
    sourceCode.addCodeLine("array.swap(i, j, null, null);", null, 4, null); // 13
    sourceCode.addCodeLine("fertig = false;", null, 4, null); // 14
    sourceCode.addCodeLine("}", null, 3, null); // 15
    sourceCode.addCodeLine("}", null, 2, null); // 16
    sourceCode.addCodeLine("}", null, 1, null); // 17
    sourceCode.addCodeLine("while (gap>1 || !fertig);", null, 1, null); // 18
    sourceCode.addCodeLine("}", null, 0, null); // 19

    // Zwischenschritt wird angelegt
    lang.nextStep();

    // Pointer werden auf das Array gesetzt
    // pointerCounter++;
    ArrayMarker iMarker = lang.newArrayMarker(intArray, 0, "i", null,
        arrayiProps);

    // pointerCounter++;
    ArrayMarker jMarker = lang.newArrayMarker(intArray, 0, "j", null,
        arrayjProps);

    lang.nextStep();

    // Sortieralgorithmus wird gestartet
    try {
      MyCombSort(intArray, sourceCode, iMarker, jMarker, TextvarGap,
          TextvarFertig);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  private void MyCombSort(IntArray array, SourceCode codeSupport,
      ArrayMarker markerI, ArrayMarker markerJ, Text varGap, Text varFertig)
      throws LineNotExistsException {
    TicksTiming standardDuration = new TicksTiming(100);
    TicksTiming standardDelay = new TicksTiming(20);

    codeSupport.highlight(0);
    codeSupport.highlight(19);
    lang.nextStep();

    codeSupport.unhighlight(0);
    codeSupport.unhighlight(19);
    codeSupport.highlight(1);
    int gap, i, j;
    varGap.setText("int gap;", null, null);
    lang.nextStep();

    codeSupport.toggleHighlight(1, 0, false, 2, 0);
    boolean fertig;
    varFertig.setText("boolean fertig;", null, null);
    lang.nextStep();

    codeSupport.toggleHighlight(2, 0, false, 3, 0);
    float shrink = 1.3F;
    lang.nextStep();

    codeSupport.toggleHighlight(3, 0, false, 4, 0);
    int elements = array.getLength();
    lang.nextStep();

    codeSupport.toggleHighlight(4, 0, false, 5, 0);
    gap = elements;
    varGap.setText("int gap = " + gap + ";", null, null);
    lang.nextStep();
    codeSupport.unhighlight(5);

    do {
      codeSupport.unhighlight(18);
      codeSupport.highlight(6);
      codeSupport.highlight(17);
      lang.nextStep();

      codeSupport.unhighlight(6);
      codeSupport.unhighlight(17);
      codeSupport.highlight(7);
      fertig = true;
      varFertig.setText("boolean fertig = " + fertig + ";", null, null);
      lang.nextStep();

      codeSupport.toggleHighlight(7, 0, false, 8, 0);
      gap = (int) ((float) gap / shrink);
      varGap.setText("int gap = " + gap + ";", null, null);
      lang.nextStep();

      codeSupport.toggleHighlight(8, 0, false, 9, 0);
      if (gap < 1)
        gap = 1;
      varGap.setText("int gap = " + gap + ";", null, null);
      lang.nextStep();
      codeSupport.unhighlight(9);

      for (i = 0; i < elements - gap; i++) {
        markerI.move(i, standardDelay, standardDuration);
        codeSupport.highlight(10);
        codeSupport.highlight(16);
        lang.nextStep();

        codeSupport.unhighlight(10);
        codeSupport.unhighlight(16);
        codeSupport.highlight(11);
        j = i + gap;
        markerJ.move(j, standardDelay, standardDuration);
        lang.nextStep();
        codeSupport.unhighlight(11);

        codeSupport.highlight(12);
        codeSupport.highlight(15);
        array.highlightElem(i, i, null, null);
        array.highlightElem(j, j, null, null);
        lang.nextStep();
        codeSupport.unhighlight(12);
        codeSupport.unhighlight(15);

        if (array.getData(i) > array.getData(j)) {
          codeSupport.highlight(13);
          array.swap(i, j, standardDelay, standardDuration);
          lang.nextStep();

          codeSupport.toggleHighlight(13, 0, false, 14, 0);
          fertig = false;
          varFertig.setText("boolean fertig = " + fertig + ";", null, null);
          lang.nextStep();
          codeSupport.unhighlight(14);
        }
        array.unhighlightElem(0, array.getLength() - 1, null, null);
      }

      codeSupport.highlight(18);
    } while (gap > 1 || !fertig);

    codeSupport.unhighlight(18);
    codeSupport.highlight(0);
    codeSupport.highlight(19);
    lang.nextStep();

    codeSupport.unhighlight(0);
    codeSupport.unhighlight(19);
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    Language l = new AnimalScript("CombSort Animation",
        "Florian Kryst und David Kuhn", 640, 480);
    CombSort c = new CombSort(l);
    c.sort(arg0, arg1);
    return l.toString();
  }

  public String getAlgorithmName() {
    return "Comb Sort";
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

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getName() {
    return "Comb Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "David Kuhn, Florian Kryst";
  }

  @Override
  public void init() {
    // nothing to be done here...
  }
}
