package generators.sorting.bucketsort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class BucketSortMB extends AnnotatedAlgorithm implements Generator {

  /**
   * The concrete language object used for creating output
   */
  public int                   anzahlBuckets;
  private TextProperties       T;
  private String               Author      = "Malek Boulakbech";
  public final String          DESCRIPTION = "BucketSort  (von engl. bucket  - Eimer) ist ein schnelles stabiles  Sortierverfahren  das eine Liste in linearer Laufzeit  sortieren kann jedoch out-of-place  arbeitet.";
  private ArrayProperties      arrayProps;
  private SourceCodeProperties scProps;

  public BucketSortMB() {

  }

  public String getAnnotatedSrc() {
    return " void bucketsort(int n, int anzahlBuckets, int z[])   @label(\"header\")\n\n"

        + " int buckets[] = new int[anzahlBuckets];    @label(\"bucketsInit\")\n"
        + " for (int i=0;                              @label(\"iForInit\") @declare(\"int\", \"i\")\n"
        + "i<n;         						  @label(\"condFor\")  @continue \n"
        + "i++) {     					      @label(\"iForInc\")  @continue @inc(\"i\") \n"
        + "    buckets[z[i]]++; 						  @label(\"bucketsInc\")\n"
        + "   }									      @label(\"forEnd\")\n"

        + " int x=0; 								  @label(\"var_X\")    @declare(\"int\", \"x\", \"0\")\n"
        + " for (int j=0; 							  @label(\"jForInit\") @declare(\"int\", \"j\")\n"
        + "j<anzahlBuckets;   				  @label(\"condFor2\") @continue \n"
        + "j++) {							      @label(\"jForInc\")  @continue @inc(\"j\")\n"
        + "    while (buckets[j] > 0) {				  @label(\"whileB>0\")             \n"
        + "        z[x++] = j;						  @label(\"setZ\")                 \n"
        + "        buckets[j]--;                       @label(\"bucketsIdec\")          \n"
        + "    }									      @label(\"whileEnd\")             \n"
        + " }                                          @label(\"forEnd2\")              \n"
        + "} 										  @label(\"end\")";
  }

  private Text         myT;
  private Text         myT1;
  private Vector<Text> V  = new Vector<Text>();
  private Vector<Text> V1 = new Vector<Text>();

  public void sort(int[] a) {

    IntArray ia = lang.newIntArray(new Coordinates(20, 100), a, "intArray_",
        null, arrayProps);
    lang.nextStep();

    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 15));
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        new Color(130, 130, 130));
    sourceCode = lang.newSourceCode(new Coordinates(400, 140), "sourceCode",
        null, scProps);

    lang.nextStep();
    try {

      bucketsort(ia.getLength(), anzahlBuckets, ia);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  public void init(int a[]) {
    localInit();
    int maximum = a[0];
    for (int i = 0; i < a.length; i++) {
      if (a[i] > maximum) {
        maximum = a[i];
      }
    }
    anzahlBuckets = maximum + 1;
    for (int j = 0; j < anzahlBuckets; j++) {
      myT1 = lang.newText(new Coordinates(2, 150 + j * 30), "" + j, "a" + j,
          null, T);
      V1.add(myT1);
    }
  }

  public void bucketsort(int n, int anzahlBucketsl, IntArray z) {

    exec("header");
    lang.nextStep();
    exec("bucketsInit");
    int buckets[] = new int[anzahlBucketsl];
    lang.nextStep();
    exec("iForInit");
    lang.nextStep();
    exec("condFor");
    lang.nextStep();
    for (int i = 0; i < n; i = Integer.parseInt(vars.get("i"))) {
      exec("bucketsInc");
      T.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      z.highlightElem(i, null, null);
      myT = lang.newText(new Coordinates(32 + buckets[z.getData(i)] * 30,
          150 + z.getData(i) * 30), "" + z.getData(i), "b" + i, null, T);
      V.add(myT);
      buckets[z.getData(i)]++;
      lang.nextStep();
      exec("iForInc");
      lang.nextStep();
      exec("condFor");
      lang.nextStep();
    }

    exec("Var_X");
    int x = Integer.parseInt(vars.get("Vars_X"));
    lang.nextStep();
    exec("jForInit");
    lang.nextStep();
    exec("condFor2");
    lang.nextStep();
    for (int j = 0; j < anzahlBuckets; j = Integer.parseInt(vars.get("j"))) {
      V1.get(j).changeColor(null, Color.RED, null, null);
      exec("whileB>0");
      lang.nextStep();
      while (buckets[j] > 0) {
        exec("setZ");
        lang.nextStep();
        z.put(x, j, null, null);
        int count = 0;
        while (count < V.size()) {
          if (V.get(count).getText().compareTo("" + j) == 0) {
            V.get(count).hide();
            V.remove(count);
            break;
          }
          count++;
        }
        lang.nextStep();
        exec("bucketsIdec");
        buckets[j]--;
        lang.nextStep();
        exec("whileB>0");
        lang.nextStep();
      }
      exec("jForInit");
      lang.nextStep();
      exec("condFor2");
      lang.nextStep();
    }
  }

  public String generate(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) {

    lang = new AnimalScript("Bucketsort Animation", Author, 640, 480);
    lang.setStepMode(true);
    int arrayData[] = (int[]) arg1.get("intArray");
    init(arrayData);
    scProps.set("highlightColor", arg0.get("Code", "highlightColor"));
    arrayProps.set("color", arg0.get("array", "color"));
    sort(arrayData);
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  public String getName() {
    return "Bucketsort [annotation based]";
  }

  public String getAlgorithmName() {
    return "Bucket Sort";
  }

  public String getAnimationAuthor() {
    return Author;
  }

  public String getCodeExample() {
    return super.getCodeExample();
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return DESCRIPTION;
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

  public void init() {
    // nothing to be done here
  }

  public void localInit() {

    super.init();
    scProps = new SourceCodeProperties();
    T = new TextProperties();
    T.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 15));
    T.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(170, 170,
        170));
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(0,
        44, 255));
    parse();

  }
}
