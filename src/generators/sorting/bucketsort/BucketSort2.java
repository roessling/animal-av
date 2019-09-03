package generators.sorting.bucketsort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class BucketSort2 extends AnnotatedAlgorithm implements Generator {

  private ArrayProperties       arrayProps;
  private ArrayMarker           markerA, markerH;
  private ArrayMarkerProperties markerProps;

  private SourceCodeProperties  sourceCodeProps;

  private Text                  aToSort, hist, elementeToSort, anzBuckets;

  private IntArray              arrayToSort, histoGramm;
  private int[]                 aTosort, hGramm;

  private int                   anzahlToSort, anzahlBuckets, x, i, j;

  private String                ver         = "Vergleiche";
  private String                zuw         = "Zuweisungen";

  private static final String   DESCRIPTION = "Bucketsort (von engl. bucket 'Eimer') ist ein stabiles Sortierverfahren, "
                                                + "das eine Liste in linearer Laufzeit sortieren kann, "
                                                + "da es nicht auf Schlüsselvergleichen basiert (siehe Sortierverfahren). Es arbeitet jedoch out-of-place."
                                                + "Bucketsort zählt die Häufigkeit jedes Schlüsselwertes in einer Liste. "
                                                + "Daraus errechnet es die korrekte Position jedes Elements und fügt es in einer zweiten Liste dort ein."
                                                + "Die Häufigkeit der Schlüsselwerte wird in einem so genannten Histogramm gespeichert. "
                                                + "Dies wird meist als Array implementiert, das so lang ist, wie es mögliche Schlüsselwerte gibt; "
                                                + "als Indizes werden dabei die Schlüsselwerte bzw. die ihnen zugeordneten ganzen Zahlen gewählt. "
                                                + "Elemente mit gleichem Sortierschlüssel werden dabei in Gruppen, so genannten Buckets, zusammengefasst."
                                                + "Das Histogramm wird zunächst mit Nullen initialisiert. "
                                                + "Dann wird die zu sortierende Liste durchlaufen "
                                                + "und bei jedem Listenelement der entsprechende Histogrammeintrag um eins erhöht."
                                                + "In einem zweiten Array, das ebenso lang ist wie das Histogramm-Array "
                                                + "und ebenfalls mit Nullen initialisiert wird, werden nun die aus dem Histogramm errechneten Einfügepositionen gespeichert."
                                                + "Schließlich werden in eine Liste, die ebenso lang ist wie die zu sortierende, "
                                                + "die Elemente der zu sortierenden Liste nacheinander an den berechneten Positionen eingefügt.";

  private static final String   SOURCE_CODE = "public void bucketsort(int n, int anzahlBuckets, int z[]){" // 0
                                                + "\n// histogramm erstellen" // 1
                                                + "\n  int buckets[] = new int[anzahlBuckets];" // 2
                                                + "\n  for (int i=0; i<n; i++){" // 3
                                                + "\n    buckets[z[i]]++;" // 4
                                                + "\n  }" // 5
                                                + "\n// sortieren" // 6
                                                + "\n  int x=0;" // 7
                                                + "\n  for (int j=0; j<anzahlBuckets; j++){" // 8
                                                + "\n       while (buckets[j] > 0) {" // 9
                                                + "\n          z[x++] = j;" // 10
                                                + "\n          buckets[j]--;" // 11
                                                + "\n       }" // 12
                                                + "\n  }" // 13
                                                + "\n  }";                                                                                       // 14

  public void init() {
    super.init();
    setProps();

    hGramm = new int[anzahlToSort];

    sourceCode = lang.newSourceCode(new Coordinates(10, 10), "sourceCode",
        null, sourceCodeProps);
    // setSourceCode();

    aToSort = lang.newText(new Coordinates(20, 350), "ArrayToSort", "text0",
        null);
    aToSort.setFont(new Font("Serif", Font.BOLD, 20), null, null);

    arrayToSort = lang.newIntArray(new Offset(200, 0, aToSort, "west"),
        aTosort, "arrayToSort", null, arrayProps);

    elementeToSort = lang.newText(new Coordinates(20, 500), "elementeToSort = "
        + anzahlToSort, "text1", null);
    elementeToSort.setFont(new Font("Serif", Font.BOLD, 16), null, null);

    anzBuckets = lang.newText(new Coordinates(20, 550), "anzahlBuckets = "
        + anzahlBuckets, "text2", null);
    anzBuckets.setFont(new Font("Serif", Font.BOLD, 16), null, null);

    // setup complexity
    vars.declare("int", ver);
    vars.setGlobal(ver);
    vars.declare("int", zuw);
    vars.setGlobal(zuw);

    Text text = lang.newText(new Coordinates(20, 600), "...", "complexity",
        null);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Vergleiche: ");
    tu.addToken(vars.getVariable(ver));
    tu.addToken(" - Zuweisungen: ");
    tu.addToken(vars.getVariable(zuw));
    tu.update();

    parse();
  }

  @Override
  public String getAnnotatedSrc() {
    return "public void bucketsort(int elementeToSort, int anzahlBuckets, int ArrayToSort[]){  @label(\"header\")\n"
        + "	int HistoGramm[] = new int[anzahlBuckets];	 @label(\"histogramm\")  @inc(\""
        + zuw
        + "\")\n"
        + "	for (int i=0;				                 @label(\"ForInit1\") @declare(\"int\", \"i\") @inc(\""
        + zuw
        + "\")\n"
        + "i<elementeToSort;				             @label(\"ForComp1\") @continue @inc(\""
        + ver
        + "\")\n"
        + "                    i++) {		@label(\"ForInc1\")  @continue @inc(\"i\") @inc(\""
        + zuw
        + "\")\n"
        + "	 HistoGramm[ArrayToSort[i]]++;	@label(\"setHisto\") @inc(\""
        + zuw
        + "\")\n"
        + "	}				                @label(\"endFor1\")\n"
        + " int x=0;					    @label(\"varX\") @declare(\"int\", \"x\") @set(\"varX\", \"0\") @inc(\""
        + zuw
        + "\")\n"
        + "	for (int j=0;	                @label(\"ForInit2\") @declare(\"int\", \"j\") @inc(\""
        + zuw
        + "\")\n"
        + "		j<anzahlBuckets;        	@label(\"ForComp2\") @continue @inc(\""
        + ver
        + "\") \n"
        + "				j++){		        @label(\"ForInc2\") @continue @inc(\"j\") @inc(\""
        + zuw
        + "\") \n"
        + "	 while (HistoGramm[j] > 0) {	@label(\"while\")  @inc(\""
        + ver
        + "\")\n"
        + "	 	ArrayToSort[x++] = j;	    @label(\"sortA\") @inc(\"x\") @inc(\""
        + zuw
        + "\") @inc(\""
        + zuw
        + "\")\n"
        + "	  HistoGramm[j]--;			@label(\"getHisto\") @inc(\""
        + zuw
        + "\")\n"
        + "	 }						        @label(\"endwhile\")\n"
        + " }								@label(\"endFor2\")\n"
        + "}								    @label(\"endmethod\")\n"
        + "								    @label(\"end\")\n";
  }

  public void sort() {
    init();
    try {

      bucketSort(anzahlToSort, anzahlBuckets, aTosort);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  void bucketSort(int n, int anzahlBuckets, int z[]) {
    lang.nextStep();
    exec("header");

    lang.nextStep();
    // histogramm erstellen
    exec("histogramm");
    hist = lang.newText(new Coordinates(20, 450), "HistoGramm", "text1", null);
    hist.setFont(new Font("Serif", Font.BOLD, 20), null, null);
    histoGramm = lang.newIntArray(new Offset(200, 0, hist, "west"), hGramm,
        "histogramm", null, arrayProps);

    lang.nextStep();
    exec("ForInit1");
    for (i = 0; i < n; i++) {

      lang.nextStep();
      exec("ForComp1");

      lang.nextStep();
      exec("setHisto");
      arrayToSort.highlightElem(i, null, null);
      markerA = lang
          .newArrayMarker(arrayToSort, i, "marker", null, markerProps);
      int akt = histoGramm.getData(z[i]) + 1;
      histoGramm.put(z[i], akt, null, null);
      histoGramm.highlightElem(z[i], null, null);
      markerH = lang.newArrayMarker(histoGramm, z[i], "marker", null,
          markerProps);

      lang.nextStep();
      exec("ForInc1");
      arrayToSort.unhighlightElem(i, null, null);
      histoGramm.unhighlightElem(z[i], null, null);
      markerA.hide();
      markerH.hide();
    }

    lang.nextStep();
    exec("varX");
    // sortieren
    x = 0;

    lang.nextStep();
    exec("ForInit2");
    for (j = 0; j < anzahlBuckets; j++) {

      lang.nextStep();
      exec("ForComp2");

      lang.nextStep();
      exec("while");
      histoGramm.highlightElem(j, null, null);
      markerH = lang.newArrayMarker(histoGramm, j, "marker", null, markerProps);

      while (histoGramm.getData(j) > 0) {

        lang.nextStep();
        exec("sortA");
        markerA = lang.newArrayMarker(arrayToSort, x, "marker", null,
            markerProps);
        arrayToSort.put(x, j, null, null);
        arrayToSort.highlightElem(x, null, null);
        x++;

        lang.nextStep();
        exec("getHisto");
        int akt = histoGramm.getData(j) - 1;
        histoGramm.put(j, akt, null, null);
        histoGramm.highlightElem(j, null, null);

        lang.nextStep();
        exec("while");
        arrayToSort.unhighlightElem(x - 1, null, null);
        markerA.hide();

      }
      lang.nextStep();
      exec("ForInc2");
      histoGramm.unhighlightElem(j, null, null);
      arrayToSort.unhighlightElem(x, null, null);
      markerA.hide();
      markerH.hide();
    }
    lang.nextStep();
    exec("end");
  }

  private void setProps() {
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 16));

  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
//    BucketSort2 b = new BucketSort2();
    aTosort = (int[]) arg1.get("arrayToSort");
    anzahlBuckets = (Integer) arg1.get("buckets");
    anzahlToSort = (Integer) arg1.get("anzahlToSort");

    arrayProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");
    markerProps = (ArrayMarkerProperties) arg0
        .getPropertiesByName("markerProps");
    sort();
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Bucket Sort";
  }

  public String getAnimationAuthor() {
    return "Jurlind Budurushi, Genc Shala";
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

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return "Bucket Sort";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }
}
