package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * Lösung für die 6. Aufgabe
 * 
 * @author Clemens Bergmann
 * 
 */
public class StoogeSort2 implements Generator {

  /*
   * Alle Generatoren dieser Klasse haben das selbe Timing
   */
  /**
   * Wie lange soll vor einer Aktion gewartet werden
   */
  private Timing                wait     = null;
  /**
   * Wie lange soll eine Aktion dauern
   */
  private Timing                duration = new TicksTiming(20);

  /**
   * Die generellen eigenschaften von allen Arrays eines generators diesen Types
   */
  private ArrayProperties       ap;

  /**
   * Die generellen Eigenschaften aller ArrayMarker eines Generators diesen
   * Types
   */
  private ArrayMarkerProperties amp;

  /**
   * Dieses SourceCode Object beinhaltet den Sourcecode eines Genrators
   */
  private SourceCode            code;

  /**
   * Das Sprachobjekt eines Generators
   */
  private Language              lang;
  /**
   * Der zu sortierende Text
   */
  private String                tosort;

  private ArrayMarker           mark_i;
  private ArrayMarker           mark_j;

  /**
   * Dies ist der Standard Konstruktor Er erstellt einen Generator mit leerem
   * Geheimtext und dem Schlüsselbuchstaben 'a'
   */
  public StoogeSort2() {
  }

  private StringArray arr;
  private Text        i_text;
  private Text        j_text;
  private Text        comp_text;

  /**
   * Diese Funktion liefert ein String array mit dem Sourcecode.
   * 
   * @return Der Sourcecode. Jede Zeile ist ein Eintrag im array.
   */
  private String[] getCode() {
    ArrayList<String> ar = new ArrayList<String>();
    ar.add("1. Gegeben sei ein Array A. Diese enthält ein zu sortierende Zeichenfolge.");
    ar.add("2. Gegeben sei die linke Grenze i des zu sortierenden Bereichs.");
    ar.add("3. Gegeben sei die rechte Grenze j des zu sortierenden Bereichs.");
    ar.add("4. Vergleiche A[i] und A[j].");
    ar.add("5. Sollte die Reihenfolge nicht stimmen vertausche A[i] und A[j].");
    ar.add("6. Sollte i+1 größer J sein, breche ab.");
    ar.add("7. Berechne k als Grenze der Drittel des Arrays. k = [(j-i+1)/3]");
    ar.add("8. Berechne Stoogesort für die ersten beiden Drittel.");
    ar.add("9. Berechne Stoogesort für die letzten beiden Drittel.");
    ar.add("10. Berechne Stoogesort für die ersten beiden Drittel.");

    return ar.toArray(new String[0]);
  }

  /**
   * Diese Funktion erstellt überschift und Sourcecode in der Animation
   */
  private void stoogePrepare() {
    DisplayOptions header_ops = null;
    Text header = lang.newText(new Coordinates(20, 30),
        this.getAlgorithmName(), "header", header_ops);

    lang.nextStep();

    SourceCodeProperties scp = new SourceCodeProperties();

    scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);

    code = lang.newSourceCode(new Offset(0, 100, header,
        AnimalScript.DIRECTION_SW), "source", null, scp);

    String[] sourcecodesource = this.getCode();
    for (String a : sourcecodesource) {
      code.addCodeLine(a, null, 0, null);
    }

    lang.nextStep();

  }

  /**
   * 
   */
  private void stoogeIntro() {
    code.highlight(0);

    ArrayList<String> al = new ArrayList<String>();
    for (char c : this.tosort.toCharArray()) {
      al.add("" + c);
    }

    this.arr = lang.newStringArray(new Offset(0, 40, code,
        AnimalScript.DIRECTION_SW), al.toArray(new String[0]), "tosort", null,
        ap);

    lang.nextStep();

    this.i_text = lang.newText(new Offset(0, 20, this.arr,
        AnimalScript.DIRECTION_SW), "", "i", null);

    this.j_text = lang.newText(new Offset(0, 20, this.i_text,
        AnimalScript.DIRECTION_SW), "", "j", null);

    this.comp_text = lang.newText(new Offset(0, 20, this.j_text,
        AnimalScript.DIRECTION_SW), "", "comp", null);

    this.mark_i = lang.newArrayMarker(arr, 0, "mark_i", null, amp);
    this.mark_j = lang.newArrayMarker(arr, this.arr.getLength() - 1, "mark_j",
        null, amp);

    this.code.unhighlight(0);
  }

  private int count = 0;

  /**
   * Sort the given range of the array
   * 
   * @param i
   * @param j
   */
  private void stoogeSort(int i, int j) {
    count++;

    code.highlight(1);

    this.i_text.setText("i = " + i, null, null);

    if (count == 1) {
      this.mark_i.move(i, this.wait, this.duration);
      lang.nextStep();
    } else {
      this.mark_i.move(i, null, null);
    }

    code.toggleHighlight(1, 2);

    this.j_text.setText("j = " + j, null, null);

    if (count == 1) {
      this.mark_j.move(j, this.wait, this.duration);
      lang.nextStep();
    } else {
      this.mark_j.move(j, null, null);
    }

    code.toggleHighlight(2, 3);

    String ai = arr.getData(i);
    String aj = arr.getData(j);
    boolean comp = (ai.compareTo(aj) > 0);

    this.comp_text.setText(ai + " < " + aj + " ? =>" + (comp ? "ja" : "nein"),
        null, null);

    lang.nextStep();

    this.comp_text.setText("", null, null);

    code.unhighlight(3);

    if (comp) {
      code.highlight(4);

      arr.swap(i, j, this.wait, this.duration);

      lang.nextStep();

      code.unhighlight(4);
    }

    if (i + 1 >= j) {
      code.highlight(5);
      lang.nextStep();
      code.unhighlight(5);
      return;
    }

    code.highlight(6);

    int k = (j - i + 1) / 3;

    this.comp_text.setText("k = (j-i+1)/3 =" + k, null, null);

    lang.nextStep();

    code.toggleHighlight(6, 7);

    lang.nextStep();

    code.unhighlight(7);

    stoogeSort(i, j - k);

    code.highlight(8);

    lang.nextStep();

    code.unhighlight(8);

    stoogeSort(i + k, j);

    code.highlight(9);

    lang.nextStep();

    code.unhighlight(9);

    stoogeSort(i, j - k);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    this.init();

    this.tosort = (String) primitives.get("tosort");
    this.wait = new TicksTiming((Integer) primitives.get("wait"));
    this.duration = new TicksTiming((Integer) primitives.get("duration"));
    this.amp = (ArrayMarkerProperties) props.getPropertiesByName("amp");
    this.ap = (ArrayProperties) props.getPropertiesByName("ap");

    stoogePrepare();

    stoogeIntro();

    stoogeSort(0, this.tosort.length() - 1);

    return this.lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Stooge Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Clemens Bergmann"; // <cbergmann@schuhklassert.de>";
  }

  @Override
  public String getCodeExample() {
    StringBuffer sb = new StringBuffer();
    for (String s : this.getCode()) {
      sb.append(s + "\n");
    }
    return sb.toString();
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "This Generator generates a Animation for the Caesar-Chiffre.";
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
    return "Stoogesort übergebbarem sortierarray.";
  }

  @Override
  public String getOutputLanguage() {
    return PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    this.lang = new AnimalScript(this.getAlgorithmName(),
        this.getAnimationAuthor(), 640, 480);
    lang.setStepMode(true);
  }
}
