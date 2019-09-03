package generators.sorting.bucketsort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class BucketSortER extends AnnotatedAlgorithm implements Generator {

  protected static Language lang;
  protected int             letzteHighlightedZeile = -1;
  protected int             letzteHighlightedZelle = -1;
  protected int             incZuweisung           = 120;
  protected int             incVergleich           = 120;
  protected int             maxValue;
  private String            comp                   = "Compares";
  private String            assi                   = "Assignments";
  protected SourceCode      sc, sc1, sc2, sc3, sc4;
  protected Text            text, Zuweisung, Vergleich, zuweisungsAnzahl,
      vergleichsAnzahl;
  protected IntArray        ia, intArrayBucket, sortierteArray;
  protected ArrayMarker     bucketMarker, intMarker, sortierteArrayMarker,
      intMarker1;
  protected Rect            vergleichRechtsEck, zuweisungRechtsEck;

  public BucketSortER() {
    // nothing to be done here...
  }

  public String getAnnotatedSrc() {
    return "public class BucketSort {					@label(\"header\")\n"
        + "public void bucketSort(int[] a){			@label(\"bucketSort\")"
        + "											\n"
        + "	int [] bucket=new int[maxValue(a)+1];	@label(\"obj_bucket\") @declare(\"int[]\", \"bucket\", \"new int[maxValue(a)+1]\") @inc(\""
        + assi
        + "\")\n"
        + "	for(int j=0;				 			@label(\"ForInitj\") @declare(\"int\", \"j\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "	j<bucket.length;						@label(\"oForCompj\") @continue @inc(\""
        + comp
        + "\")\n"
        + "	j++)									@label(\"oForDecj\") @continue @dec(\"j\") @inc(\""
        + assi
        + "\")\n"
        + "		bucket[j]=0;						@label(\"reset_bucket\") @set(\"bucket_j for all j\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "											\n"
        + "	for (int i=0; 							@label(\"ForIniti\") @declare(\"int\", \"i\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "	i<a.length; 							@label(\"oForCompi\") @continue @inc(\""
        + comp
        + "\")\n"
        + "	i++)									@label(\"oForDeci\") @continue @dec(\"i\") @inc(\""
        + assi
        + "\")\n"
        + "		bucket[a[i]]++;						@label(\"inc_bucket\") @set(\"bucket\", \"bucket\") @inc(\""
        + assi
        + "\")\n"
        + "											\n"
        + "	int outPos=0;							@label(\"Init_pos\") @declare(\"int\", \"outPos\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "	for(int i=0;							@label(\"ForInitii\") @declare(\"int\", \"ii\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "	i<bucket.length;						@label(\"oForCompii\") @continue @inc(\""
        + comp
        + "\")\n"
        + " 	i++)									@label(\"oForDecii\") @continue @dec(\"ii\") @inc(\""
        + assi
        + "\")\n"
        + "		for(int j=0;						@label(\"ForInitjj\") @declare(\"int\", \"jj\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + " 		j<bucket[i];						@label(\"oForCompjj\") @continue @inc(\""
        + comp
        + "\")\n"
        + "		j++)								@label(\"oForDecjj\") @continue @dec(\"j\") @inc(\""
        + assi
        + "\")\n"
        + "			a[outPos++]= i;					@label(\"inc_bucket_a_i\") @set(\"a_outPos++\", \"ii\") @inc(\""
        + assi
        + "\")\n"
        + "}											@label(\"end_of_bucketSort\")\n"
        + "public static int maxValue(int[] data){	@label(\"maxValue\")\n"
        + "	int maxValue=data[0];					@label(\"Init_maxValue\") @declare(\"int\", \"maxValue\", \"data[0]\") @inc(\""
        + assi
        + "\")\n"
        + "	for (int i=0; 							@label(\"ForInitiii\") @declare(\"int\", \"iii\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "	i < data.length;						@label(\"oForCompiii\") @continue @inc(\""
        + comp
        + "\")\n"
        + "	i++){									@label(\"oForDeciii\") @continue @dec(\"iii\") @inc(\""
        + assi
        + "\")\n"
        + "		if (data[i]>maxValue)				@label(\"if\") @inc(\""
        + comp
        + "\")\n"
        + "			maxValue = data[i];				@label(\"setSwapped\") @set(\"maxValue\", \"data_i\") @inc(\""
        + assi
        + "\")\n"
        + "		return maxValue;					@label(\"return\")\n"
        + "		}									@label(\"iForEnd\")\n"
        + "}											@label(\"end_of_maxValue\")\n"
        + "public static void main(String[] args){	@label(\"main\")\n"
        + "	int[] array = {Eingabe_Liste};			@label(\"int_array array\") @declare(\"int[]\", \"array\", \"Eingabe_Array\") \n"
        + "	BucketSort obj=new BucketSort();		@label(\"BucketSortTest obj\") @declare(\"BucketSortTest\", \"obj\", \"new BucketSortTest();\") \n"
        + "	obj.bucketSort(array);					@label(\"obj.bucketSort array\")\n"
        + "}											@label(\"end_of_main\")\n"
        + "}											@label(\"end_of_BucketSort\")\n";

  }

  public String generate(AnimationPropertiesContainer props,
      java.util.Hashtable<String, Object> primitive) {
    ArrayProperties myArrayProps = (ArrayProperties) props.get(0);
    SourceCodeProperties mySourceCodeProperties = (SourceCodeProperties) props
        .get(1);
    RectProperties myRectProperties_1 = (RectProperties) props.get(2);
    RectProperties myRectProperties_2 = (RectProperties) props.get(3);
    ArrayMarkerProperties myMarkerProperty_1 = (ArrayMarkerProperties) props
        .get(4);
    ArrayMarkerProperties myMarkerProperty_2 = (ArrayMarkerProperties) props
        .get(5);
    int[] values = (int[]) primitive.get("values");
    localInit();
    this.bucketSort(myArrayProps, mySourceCodeProperties, myRectProperties_1,
        myRectProperties_2, myMarkerProperty_1, myMarkerProperty_2, values);
    return lang.toString();
  }

  /*
   * Returns the name of the algorithm
   */
  public String getAlgorithmName() {
    return "Bucket Sort";
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
    return "public void bucketSort(int[] a){\n"
        + "	int [] bucket = new int[maxValue(a)+1];\n"
        + "	for(int j=0; j<bucket.length; j++)\n" + "		bucket[j]=0;\n" + "\n"
        + "	for (int i=0; i<a.length; i++)\n" + "		bucket[a[i]]++;\n" + "\n"
        + "	int outPos=0;\n" + "	for(int i=0; i<bucket.length; i++)\n"
        + "		for(int j=0; j<bucket[i]; j++)\n" + "			a[outPos++]= i;\n" + "}\n"
        + "\n" + "public static int maxValue(int[] data){\n"
        + "	int maxValue=data[0];\n"
        + "	for (int i=0; i < data.length; i++){\n"
        + "		if (data[i]>maxValue)\n" + "     		maxValue = data[i];\n" + "	}\n"
        + "  return maxValue;\n" + "}\n";
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
    return "Bucketsort ist ein stabiles Sortierverfahren, das eine Liste in\n"
        + "linearer Laufzeit sortieren kann, da es nicht auf Schlüsselvergleichen\n"
        + "basiert (siehe Sortierverfahren). Es arbeitet jedoch out-of-place.\n"
        + "Damit eine Liste mit Bucketsort sortiert werden kann, muss die Anzahl \n"
        + "der von den Sortierschlüsseln annehmbaren Werte endlich sein. Bucketsort\n"
        + "ist also etwa gut geeignet, um eine lange Adressliste nach Postleitzahlen\n"
        + "zu ordnen, aber nicht ohne Weiteres, um ein Personenverzeichnis nach Namen\n"
        + "zu sortieren.\n"
        + "Vereinfacht wird die Implementierung, wenn die Sortierschlüssel ganze Zahlen\n"
        + "sind, da sie dann als Indizes eines Feldes (Arrays) verwendet werden können.\n"
        + "Ist dies nicht der Fall, so ist eine zusätzliche bijektive Funktion nötig, die\n"
        + "jedem möglichen Schlüsselwert eine Zahl zuordnet, sowie die dazugehörige \n"
        + "Umkehrfunktion.\n";
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
    return "Bucket Sort";
  }

  /*
   * As a rule generator. PSEUDO_CODE_OUTPUT, generator. JAVA_CODE_OUTPUT or
   * another string, possibly "DELPHI".
   */
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public void init() {
    // nothing to be done here
  }

  /*
   * Initializes the variables and calls the Super constructor
   */
  public void localInit() {
    super.init();

    lang = new AnimalScript("Bucket Sort", "Emal Rahman", 640, 480);
    lang.setStepMode(true);

    SourceCodeProperties props = new SourceCodeProperties();
    props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
    props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF,
        Font.BOLD, 14));
    sourceCode = lang.newSourceCode(new Coordinates(20, 4), "sumupCode", null,
        props);

    vars.declare("int", comp);
    vars.setGlobal(comp);
    vars.declare("int", assi);
    vars.setGlobal(assi);
    Text vergleich = lang.newText(new Coordinates(4, 530), "...", "complexity",
        null);
    TextUpdater vgl = new TextUpdater(vergleich);
    vgl.addToken("Compares: ");
    vgl.addToken(vars.getVariable(comp));
    vgl.update();
    Text zuweisung = lang.newText(new Coordinates(4, 554), "...", "complexity",
        null);
    TextUpdater zw = new TextUpdater(zuweisung);
    zw.addToken("Assignments: ");
    zw.addToken(vars.getVariable(assi));
    zw.update();

    parse();
  }

  /*
   * The method incVergleich draws a 4-corner for the number of the comparisons
   * in the algorithm
   */
  public void incVergleich(RectProperties myRectProperties_1) {
    incVergleich = incVergleich + 5;
    vergleichRechtsEck = lang.newRect(new Coordinates(incVergleich, 548),
        new Coordinates(120, 535), "McCain", null, myRectProperties_1);
  }

  /*
   * The method incZuweisung draws a 4-corner for the number of the assignment
   * in the algorithm
   */
  public void incZuweisung(RectProperties myRectProperties_2) {
    incZuweisung = incZuweisung + 5;
    zuweisungRechtsEck = lang.newRect(new Coordinates(incZuweisung, 568),
        new Coordinates(120, 555), "McCain", null, myRectProperties_2);
  }

  /*
   * The method zeigeZelle highlighting the Cell from IntArray
   */

  public void zeigeZelle(int x, IntArray intarray) {
    if (letzteHighlightedZelle != -1) {
      intarray.unhighlightCell(letzteHighlightedZelle, null, null);
    }
    letzteHighlightedZelle = x;
    intarray.highlightCell(x, null, null);
  }

  /**
   * Bucketsort is a stable sorting method, which can sort a list in linear
   * term, because it is not based on key comparisons. Nevertheless, it works
   * out of place.
   * 
   * @param myArrayProps
   * @param mySourceCodeProperties
   * @param myRectProperties_1
   * @param myRectProperties_2
   * @param myMarkerProperty_1
   * @param myMarkerProperty_2
   * @param array
   */

  public void bucketSort(ArrayProperties myArrayProps,
      SourceCodeProperties mySourceCodeProperties,
      RectProperties myRectProperties_1, RectProperties myRectProperties_2,
      ArrayMarkerProperties myMarkerProperty_1,
      ArrayMarkerProperties myMarkerProperty_2, int[] array) {

    exec("header");
    lang.nextStep();

    exec("main");
    lang.nextStep();

    exec("int_array array");
    ia = lang.newIntArray(new Coordinates(700, 150), array, "intArray", null,
        myArrayProps);
    sc2 = lang.newSourceCode(new Coordinates(500, 145), "SourceCode1", null,
        mySourceCodeProperties);
    sc2.addCodeLine("Eingabe-Liste: ", null, 0, null);
    lang.nextStep();

    exec("BucketSortTest obj");
    lang.nextStep();

    exec("obj.bucketSort array");
    lang.nextStep();

    exec("bucketSort");
    lang.nextStep();

    exec("obj_bucket");
    incZuweisung(myRectProperties_2);
    lang.nextStep();
    int[] bucket = new int[maxValue(array, myMarkerProperty_1,
        myRectProperties_1, myRectProperties_2) + 1];

    intMarker.hide();
    text.setText("maxValue = " + maxValue + " = Laenge von Bucket-Liste", null,
        null);
    ia.unhighlightCell(letzteHighlightedZelle, null, null);
    exec("obj_bucket");
    lang.nextStep();

    exec("ForInitj");
    text.hide();
    incZuweisung(myRectProperties_2);
    intArrayBucket = lang.newIntArray(new Coordinates(700, 250), bucket,
        "intArray", null, myArrayProps);
    sc4 = lang.newSourceCode(new Coordinates(500, 245), "SourceCode1", null,
        mySourceCodeProperties);
    sc4.addCodeLine("Bucket-Liste: ", null, 0, null);
    bucketMarker = lang.newArrayMarker(intArrayBucket, 0, "a[i]", null,
        myMarkerProperty_1);
    lang.nextStep();
    for (int j = 0; j < intArrayBucket.getLength(); j++) {

      exec("oForCompj");
      incVergleich(myRectProperties_1);
      lang.nextStep();

      exec("reset_bucket");
      incZuweisung(myRectProperties_2);
      intArrayBucket.put(j, 0, null, null);
      bucketMarker.move(j, null, null);
      lang.nextStep();

      exec("oForDecj");
      lang.nextStep();

    }
    exec("oForCompj");
    incVergleich(myRectProperties_1);
    lang.nextStep();

    exec("ForIniti");
    incZuweisung(myRectProperties_2);
    intMarker1 = lang.newArrayMarker(ia, 0, "i", null, myMarkerProperty_2);
    lang.nextStep();
    for (int i = 0; i < ia.getLength(); i++) {

      exec("oForCompi");
      incVergleich(myRectProperties_1);
      lang.nextStep();

      exec("inc_bucket");
      incZuweisung(myRectProperties_2);
      intArrayBucket.put(ia.getData(i),
          (intArrayBucket.getData(ia.getData(i)) + 1), null, null);
      bucketMarker.move(ia.getData(i), null, null);
      intMarker1.move(i, null, null);
      lang.nextStep();

      exec("oForDeci");
      incZuweisung(myRectProperties_2);
      lang.nextStep();
    }
    exec("oForCompi");
    incVergleich(myRectProperties_1);
    lang.nextStep();

    exec("Init_pos");
    incZuweisung(myRectProperties_2);
    int pos = 0;
    intMarker1.hide();
    lang.nextStep();

    exec("ForInitii");
    incZuweisung(myRectProperties_2);
    int[] leerArray = new int[ia.getLength()];
    sortierteArray = lang.newIntArray(new Coordinates(700, 350), leerArray,
        "intArray", null, myArrayProps);
    sc3 = lang.newSourceCode(new Coordinates(500, 345), "SourceCode1", null,
        mySourceCodeProperties);
    sc3.addCodeLine("Ausgabe-Liste: ", null, 0, null);
    sortierteArrayMarker = lang.newArrayMarker(sortierteArray, 0, "i", null,
        myMarkerProperty_1);
    lang.nextStep();
    for (int j = 0; j < intArrayBucket.getLength(); ++j) {

      exec("oForCompii");
      incVergleich(myRectProperties_1);
      lang.nextStep();

      exec("ForInitjj");
      incZuweisung(myRectProperties_2);
      lang.nextStep();
      for (int k = intArrayBucket.getData(j); k > 0; k--) {

        exec("oForCompjj");
        incVergleich(myRectProperties_1);
        lang.nextStep();

        exec("inc_bucket_a_i");
        incZuweisung(myRectProperties_2);
        sortierteArray.put(pos++, j, null, null);
        sortierteArrayMarker.move(pos, null, null);
        bucketMarker.move(j, null, null);
        lang.nextStep();

        if (k > 0) {
          exec("oForDecjj");
          incZuweisung(myRectProperties_2);
          lang.nextStep();
        }
      }
      exec("oForCompjj");
      incVergleich(myRectProperties_1);
      lang.nextStep();

      exec("oForDecii");
      incZuweisung(myRectProperties_2);
      lang.nextStep();
    }
    exec("oForCompii");
    incVergleich(myRectProperties_1);
    lang.nextStep();
    exec("end_of_BucketSort");
    lang.nextStep();
  }

  public int maxValue(int[] data, ArrayMarkerProperties myMarkerProperty_1,
      RectProperties myRectProperties_1, RectProperties myRectProperties_2) {
    exec("maxValue");
    lang.nextStep();

    exec("Init_maxValue");
    incZuweisung(myRectProperties_2);
    zeigeZelle(0, ia);
    text = lang.newText(new Coordinates(700, 80), "", "rekText", null);
    maxValue = data[0];
    text.setText("maxValue = " + maxValue, null, null);
    intMarker = lang.newArrayMarker(ia, 0, "i", null, myMarkerProperty_1);
    lang.nextStep();

    exec("ForInitiii");
    incZuweisung(myRectProperties_2);
    lang.nextStep();
    for (int i = 0; i < data.length; i++) {

      exec("oForCompiii");
      incVergleich(myRectProperties_1);
      lang.nextStep();

      exec("if");
      incVergleich(myRectProperties_1);
      intMarker.move(i, null, null);
      lang.nextStep();
      if (data[i] > maxValue) {

        exec("setSwapped");
        incZuweisung(myRectProperties_2);
        maxValue = data[i];
        text.setText("maxValue = " + maxValue, null, null);
        zeigeZelle(i, ia);
        lang.nextStep();

        exec("oForDeciii");
        incZuweisung(myRectProperties_2);
        lang.nextStep();
      } else {
        exec("oForDeciii");
        incZuweisung(myRectProperties_2);
        lang.nextStep();
      }
    }
    exec("oForCompiii");
    incVergleich(myRectProperties_1);
    lang.nextStep();

    exec("return");
    lang.nextStep();

    return maxValue;
  }

}
