package generators.sorting;

import static algoanim.animalscript.AnimalScript.DIRECTION_NE;
import static algoanim.animalscript.AnimalScript.DIRECTION_SW;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.sorting.helpers.AbstractGenerator;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

/**
 * 
 * @author Jan Kassens
 * @author Zoran Zaric
 * @author Jürgen Benjamin Ronshausen
 * 
 */
public class KRZCountingSort extends AbstractGenerator {

  private int  uid;
  private Text headline;
  // private SourceCode desc;
  private Text helpText;
  private Text helpText2;

  public KRZCountingSort() {
  }

  public void sort(int[] array) {
    for (int i = 0; i < array.length; i++) {
      array[i] = Math.abs(array[i]);
    }

    headline = lang.newText(new Coordinates(30, 30), "Countingsort",
        "headline", null, HEADLINE_PROPERTIES);
    showDescription();

    lang.nextStep();

    sourceCode = lang.newSourceCode(new Offset(20, 10, headline, DIRECTION_SW),
        "sourceCode", null, SOURCECODE_PROPERTIES);
    parse();

    // Intervalgröße ermitteln
    int k = array[0];
    for (int i = 1; i < array.length; i++) {
      if (array[i] > k)
        k = array[i];
    }

    int length = array.length;

    // Intervalgröße
    Text kLabel = lang.newText(new Offset(0, 20, sourceCode, DIRECTION_SW),
        "k = ", "length_label", null, LABEL_PROPERTIES);
    lang.newText(new Offset(0, 0, kLabel, DIRECTION_NE), String.valueOf(k),
        getUID(), null, LABEL_PROPERTIES);

    Text labelA = lang.newText(new Offset(0, 60, kLabel, DIRECTION_SW),
        "Array A [1..length(A)]:", "label_a", null, LABEL_PROPERTIES);
    Text labelC = lang.newText(new Offset(0, 45, labelA, DIRECTION_SW),
        "Array C [0..k]:", "label_c", null, LABEL_PROPERTIES);
    lang.newText(new Offset(0, 45, labelC, DIRECTION_SW),
        "Array B [1..length(A)]:", "label_b", null, LABEL_PROPERTIES);

    IntArray a = createIntArray(new Offset(10, 0, labelA, DIRECTION_NE), array);
    IntArray c = createEmptyIntArray(new Offset(0, 40, a, DIRECTION_SW), k + 1);
    IntArray b = createEmptyIntArray(new Offset(0, 40, c, DIRECTION_SW), length);

    // length(A)
    Text lengthLabel = lang.newText(new Offset(0, 0, kLabel, DIRECTION_SW),
        "length(A): ", getUID(), null, LABEL_PROPERTIES);
    lang.newText(new Offset(0, 0, lengthLabel, DIRECTION_NE),
        String.valueOf(length), getUID(), null, LABEL_PROPERTIES);

    execute("initC");

    Text helpTextHeader = lang.newText(new Offset(100, -10, kLabel,
        DIRECTION_NE), "Beschreibung des aktuellen Schrittes", getUID(), null,
        HEADLINE_PROPERTIES);
    helpText = lang.newText(new Offset(0, 0, helpTextHeader, DIRECTION_SW),
        "Das Hilfsarray C wird mit Nullen initialisiert.", getUID(), null,
        DESC_PROPERTIES);
    helpText2 = lang.newText(new Offset(0, 0, helpText, DIRECTION_SW), "",
        getUID(), null, DESC_PROPERTIES);

    ArrayMarker cMarker = createArrayMarker(c, 0);
    for (int i = 0; i <= k; i++) {
      execute("initCStep");
      c.put(i, 0, null, null);
      execute("initC");
      if (i < k)
        cMarker.move(i + 1, null, new MsTiming(400));
      else
        cMarker.moveOutside(null, new MsTiming(400));
    }

    execute("count");
    showHelpText(
        "F\u00FCr jede Zahl wird deren Stelle im Hilfsarray C inkrementiert.",
        "");
    cMarker.hide();
    ArrayMarker aMarker = createArrayMarker(a, 0);
    for (int i = 0; i < a.getLength(); i++) {
      execute("countStep");
      int a_j = a.getData(i);
      c.highlightCell(a_j, null, null);
      c.put(a_j, c.getData(a_j) + 1, new MsTiming(400), null);
      execute("count");
      c.unhighlightCell(a_j, null, null);
      if (i + 1 < a.getLength())
        aMarker.move(i + 1, null, new MsTiming(400));
      else
        aMarker.moveOutside(null, new MsTiming(400));
    }

    showHelpText(
        "Das Hilfsarray enth\u00E4lt nun die Anzahl der Vorkommen jeder Zahl.",
        "");
    lang.nextStep();

    execute("sum");
    showHelpText("Das Hilfsarray C wird nun aufsummiert, sodass in jedem Feld",
        "die Summe bis zu diesem Feld steht.");
    aMarker.hide();
    cMarker.move(1, null, null);
    cMarker.show();
    for (int i = 1; i <= k; i++) {
      execute("sumStep");
      c.put(i, c.getData(i) + c.getData(i - 1), null, null);
      execute("sum");
      if (i < k)
        cMarker.move(i + 1, null, new MsTiming(400));
      else
        cMarker.moveOutside(null, new MsTiming(400));
    }

    showHelpText("Das Hilfsarray enth\u00E4lt nun die Position bis zu welcher",
        "ein Wert in der sortierten Liste auftaucht.");
    lang.nextStep();

    execute("collect");
    showHelpText(
        "Elemente aus A werden in B kopiert. C bestimmt an welche Stelle kopiert wird.",
        "C[A[j]] wird um eins dekrementiert, sodass das Element mit gleichem Wert davor kopiert wird.");

    cMarker.hide();
    boolean first = true;
    aMarker.show();
    for (int j = a.getLength() - 1; j >= 0; j--) {
      int a_j = a.getData(j);
      int c_a_j = c.getData(a_j);
      aMarker.move(j, null, first ? null : new MsTiming(400));
      execute("collectStep1");
      c.highlightCell(a_j, null, null);
      b.highlightCell(c_a_j - 1, new MsTiming(400), null);
      b.put(c_a_j - 1, a_j, new MsTiming(800), null);
      execute("collectStep2");
      b.unhighlightCell(c_a_j - 1, null, null);
      c.put(a_j, c_a_j - 1, null, null);
      execute("collect");
      first = false;
      c.unhighlightCell(a_j, null, null);
    }
    aMarker.hide();
    cMarker.hide();
    showHelpText("B enth\u00E4lt nun eine sortierte Kopie von A", "");
    execute("return");
  }

  private void showHelpText(String line1, String line2) {
    helpText.setText(line1, null, null);
    helpText2.setText(line2, null, null);
  }

  private IntArray createIntArray(Offset b, int[] a) {
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set("color", Color.BLACK);
    arrayProps.set("fillColor", Color.WHITE);
    arrayProps.set("filled", Boolean.TRUE);
    arrayProps.set("elementColor", Color.BLACK);
    arrayProps.set("elemHighlight", Color.RED);
    arrayProps.set("cellHighlight", Color.YELLOW);
    return this.lang.newIntArray(b, a, getUID(), null, arrayProps);
  }

  private void showDescription() {
    String[] lines = getDescription().split("\n");
    Text[] labels = new Text[lines.length];
    for (int i = 0; i < lines.length; i++) {
      Offset position = i == 0 ? new Offset(0, 20, headline, DIRECTION_SW)
          : new Offset(0, 0, labels[i - 1], DIRECTION_SW);
      labels[i] = lang.newText(position, lines[i], getUID(), null,
          LABEL_PROPERTIES);
    }
    lang.nextStep();
    for (Text label : labels) {
      label.hide();
    }
  }

  private ArrayMarker createArrayMarker(ArrayPrimitive array, int index) {
    return lang.newArrayMarker(array, index, getUID(), null);
  }

  private String getUID() {
    return "uid_" + this.uid++;
  }

  private IntArray createEmptyIntArray(Offset b, int length) {
    ArrayProperties props = new ArrayProperties();
    props.set("color", Color.BLACK); // Cell outlines
    props.set("fillColor", Color.WHITE); // background of cells
    props.set("elementColor", Color.BLACK);
    props.set("elemHighlight", Color.WHITE);
    props.set("cellHighlight", Color.YELLOW);
    IntArray array = this.lang.newIntArray(b, new int[length], getUID(), null,
        props);
    for (int i = 0; i < array.getLength(); i++) {
      array.highlightElem(i, null, null);
    }
    return array;
  }

  public static void main(String[] args) {
    KRZCountingSort s = new KRZCountingSort();
    s.init();
    System.out.println(s.generate());
  }

  public String generate() {
    int[] array = { 2, 5, 3, 6, 2, 3, 10, 3 };
    this.sort(array);
    return this.lang.toString();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int[] array = (int[]) primitives.get("array");
    this.sort(array);
    return this.lang.toString();
  }

  public String getAlgorithmName() {
    return "Counting Sort";
  }

  public String getCodeExample() {
    return "for i = 0 to max(A)         \n" + "  do C[i] = 0               \n"
        + "for j = 1 to length(A)      \n" + "  do C[A[j]] = C[A[j]] + 1  \n"
        + "for i = 1 to max(A)         \n" + "  do C[i] = C[i] + C[i - 1] \n"
        + "for j = length(A) downto 1  \n" + "  do B[C[A[j]]] = A[j]      \n"
        + "     C[A[j]] = C[A[j]] - 1  \n" + "return B                      ";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Countingsort ist ein stabiler Sortieralgorithmus, der out-of-place arbeitet. \n"
        + "Er sortiert eine Zahlenfolge nicht durch Vergleiche, sondern setzt das Wissen voraus, \n"
        + "aus welchem Intervall die Zahlen des Schl\u00FCssels stammen. Laufzeit: O(N + M) \n"
        + "wobei N = Anzahl Elemente der Eingabe, M = Gr\u00F6\u00DFe"
        + " des Intervalls der Eingabeelemente.";
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return "Counting Sort";
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("Countingsort Animation",
        "Jan Kassens, Zoran Zaric, J\u00FCrgen Benjamin Ronshausen", 640, 480);
    lang.setStepMode(true);
  }

  @Override
  protected String[] getAnnotatedSrc() {
    return new String[] { "for i = 0 to max(A):        @label(\"initC\")\n",
        "  C[i] = 0                  @label(\"initCStep\")\n",
        "for j = 1 to length(A):     @label(\"count\")\n",
        "  C[A[j]] = C[A[j]] + 1     @label(\"countStep\")\n",
        "for i = 1 to max(A):        @label(\"sum\")\n",
        "  C[i] = C[i] + C[i - 1]    @label(\"sumStep\")\n",
        "for j = length(A) downto 1: @label(\"collect\")\n",
        "  B[C[A[j]]] = A[j]         @label(\"collectStep1\")\n",
        "  C[A[j]] = C[A[j]] - 1     @label(\"collectStep2\")\n",
        "return B                    @label(\"return\")\n" };
  }
}
