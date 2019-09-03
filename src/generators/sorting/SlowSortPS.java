package generators.sorting;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.annotations.Annotation;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class SlowSortPS extends AnnotatedAlgorithm implements
    generators.framework.Generator {

  private Language              lang;
  int                           counter;
  private int[]                 array      = { 2, 3, 1, 5, 9 };
  private IntArray              intArray;

  private SourceCode            sc;                            // Sourcode
                                                                // gespeichert

  private Text                  m_var;

  private int                   tausch     = 0;
  private int                   rekursion  = 0;

  private boolean               initSort   = false;

  private int                   vergleiche = 0;

  private ArrayMarker           iPointer;

  private ArrayMarkerProperties iPointerProp;

  private ArrayMarkerProperties jPointerProp;

  private ArrayMarker           jPointer;

  private Text                  i_var;

  private Text                  j_var;

  private ArrayProperties       arrayProps;

  private SourceCodeProperties  scProps;

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    this.init();
    counter = 1;
    if (arg1.get("array") != null) {
      this.iPointerProp = (ArrayMarkerProperties) arg0.getPropertiesByName("i");
      this.jPointerProp = (ArrayMarkerProperties) arg0.getPropertiesByName("j");
      this.arrayProps = (ArrayProperties) arg0.getPropertiesByName("arr");
      this.scProps = (SourceCodeProperties) arg0
          .getPropertiesByName("sourceCode");
      this.array = (int[]) arg1.get("array");
    } else {
      // Quellcode einstellungen
      this.scProps = new SourceCodeProperties();
      scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
          Font.PLAIN, 12));
      scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

      this.arrayProps = new ArrayProperties();
      arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
      arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
      arrayProps
          .set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
      arrayProps
          .set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLUE);
      arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
          Color.YELLOW);

      // Pointer i erstellen
      this.iPointerProp = new ArrayMarkerProperties();
      iPointerProp.setName("linksP");
      iPointerProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
      iPointerProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

      // Pointer j erstellen

      this.jPointerProp = new ArrayMarkerProperties();
      jPointerProp.setName("linksP");
      jPointerProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
      jPointerProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    }
    // Ueberschrift
    algoanim.primitives.Text header = lang.newText(new Coordinates(20, 30),
        "SlowSort", "SlowSort", null);
    header.setFont(new Font("SansSerif", 1, 22), null, null);

    this.sc = lang.newSourceCode(new Coordinates(30, 40), "sourceCode", null,
        scProps);
    sc.addCodeLine("procedure slowsort(A,i,j)", null, 0, null);
    sc.addCodeLine("    if i >= j then return", null, 0, null);
    sc.addCodeLine("    m := (i+j)/2", null, 0, null);
    sc.addCodeLine("    slowsort(A,i,m)", null, 0, null);
    sc.addCodeLine("    slowsort(A,m+1,j)", null, 0, null);
    sc.addCodeLine("    if A[j] < A[m] then vertausche A[j] und A[m]", null, 0,
        null);
    sc.addCodeLine("    slowsort(A,i,j-1)", null, 0, null);

    // Properties fuer das Array

    this.intArray = lang.newIntArray(new Coordinates(450, 220), this.array,
        "IntArray", null, arrayProps);

    // Properties fuer das "m"
    this.m_var = lang.newText(new Coordinates(450, 250), "m = ", "m = ", null);
    m_var.setFont(new Font("SansSerif", 1, 16), null, null);

    // Properties fuer das "i"
    this.i_var = lang.newText(new Coordinates(450, 270), "i = ", "i = ", null);
    i_var.setFont(new Font("SansSerif", 1, 16), null, null);

    // Properties fuer das "j"
    this.j_var = lang.newText(new Coordinates(450, 290), "j = ", "j = ", null);
    j_var.setFont(new Font("SansSerif", 1, 16), null, null);

    this.iPointer = lang.newArrayMarker(intArray, 0, "i", null, iPointerProp);
    this.iPointer.hide();

    this.jPointer = lang.newArrayMarker(intArray, 0, "j", null, jPointerProp);
    this.jPointer.hide();

    this.slowSort(this.array, 0, this.array.length - 1);

    return lang.toString();
  }

  private String slowSort(int[] arr, int i, int j) {
    if (!this.initSort) {
      this.initSort = true;
      vars.declare("int", "i", String.valueOf(i));
      vars.declare("int", "j", String.valueOf(j));
      vars.declare("int", "m", String.valueOf(0));
      vars.declare("int", "Rekursion", "0");
      vars.declare("int", "Vergleiche", String.valueOf(0));
      vars.declare("int", "TauschOperationen", String.valueOf(0));
    }
    rekursion++;
    vars.set("Rekursion", Integer.toString(rekursion));

    int backupi = i;
    int backupj = j;
    vars.set("i", String.valueOf(i));
    vars.set("j", String.valueOf(j));

    lang.nextStep("Step " + this.counter);
    counter++;
    this.i_var.setText("i = " + i, null, null);
    this.j_var.setText("j = " + j, null, null);
    iPointer.move(i, null, new TicksTiming(35));
    iPointer.show();
    jPointer.move(j, null, new TicksTiming(35));
    jPointer.show();
    intArray.unhighlightCell(0, intArray.getLength() - 1, null, null);
    intArray.highlightCell(i, j, null, null);
    lang.nextStep();
    vergleiche++;
    vars.set("Vergleiche", Integer.toString(vergleiche));
    if (i >= j)
      return ""; // Zeile 1
    sc.highlight(1);
    lang.nextStep();
    sc.unhighlight(1);
    int m = (i + j) / 2; // Zeile 2
    vars.set("m", String.valueOf(m));

    lang.nextStep();
    sc.highlight(2);
    this.m_var.setText("m = " + m, null, null);
    this.lang.nextStep();
    sc.unhighlight(2);
    sc.highlight(3);
    lang.nextStep();
    sc.unhighlight(3);
    slowSort(arr, i, m); // Zeile 3
    sc.highlight(4);
    lang.nextStep();
    sc.unhighlight(4);
    slowSort(arr, m + 1, j);
    intArray.highlightCell(backupi, backupj, null, null);
    sc.highlight(5);
    vergleiche++;
    vars.set("Vergleiche", Integer.toString(vergleiche));
    if (arr[j] < arr[m]) { // Zeile 4
      this.lang.nextStep();
      tausch++;
      this.intArray.swap(j, m, null, new TicksTiming(200));
      vars.set("TauschOperationen", Integer.toString(tausch));
      this.lang.nextStep();
    }
    sc.unhighlight(5);
    sc.highlight(6);
    lang.nextStep();
    sc.unhighlight(6);
    slowSort(arr, i, j - 1);
    return null;
  }

  public SlowSortPS() {
    this.init();
  }

  @Override
  public String getAlgorithmName() {
    return "Slowsort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Philipp Sowinski, Tarik Tahiri";
  }

  /*
   * @Override public String getCodeExample() { return
   * "procedure slowsort(A,i,j)\n" + "	if i &gt;= j then return\n" +
   * "	m := (i+j)/2\n" + "	slowsort(A,i,m)\n" + "	slowsort(A,m+1,j)\n" +
   * "	if A[j] &lt; A[m] then vertausche A[j] und A[m]\n" +
   * "	slowsort(A,i,j-1)\n"; }
   */
  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Slowsort animation";
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
    return "Slowsort";
  }

  @Override
  public String getOutputLanguage() {
    return PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    super.init();
    lang = new AnimalScript("Slowsort", "Tarik Tahiri und Philipp Sowinski",
        800, 600);
    lang.setStepMode(true);
    vars = lang.newVariables();

    annotations = new HashMap<String, Vector<Annotation>>();
    // parse();
  }

  @Override
  public String getAnnotatedSrc() {
    return "procedure slowsort(A,i,j)							@label(\"header\") @declare(\"int\",\"i\", \"1\") @declare(\"int\",\"j\", \"1\") \n"
        + "	if i &gt;= j then return\n								@label(\"if\") @inc(\"Rekursion\") \n"
        + "	m := (i+j)/2 											@label(\"m\") @declare(\"int\",\"m\", \"1\")\n"
        + "	slowsort(A,i,m)\n										@label(\"t3\")\n"
        + "	slowsort(A,m+1,j)\n										@label(\"t4\")\n"
        + "	if A[j] &lt; A[m] then vertausche A[j] und A[m]			@label(\"if2\")\n"
        + "	slowsort(A,i,j-1)										@label(\"t6\")\n";
  }

}
