package generators.sorting.bucketsort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
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
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class BucketSort implements generators.framework.Generator {

  protected Language            lang;
  private ArrayProperties       arrayProps;
  private ArrayMarkerProperties ami;
  private ArrayMarkerProperties amj;
  SourceCodeProperties          scProb;

  private TextProperties        txtProb, txt1Prob;
  private SourceCode            sc;

  public void init() {
    lang = new AnimalScript("BucketSortDemo",
        "iaad zabar und abdulghani alshadadi", 640, 480);
    lang.setStepMode(true);

    txtProb = new TextProperties();
    txtProb.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    txt1Prob = new TextProperties();
    txt1Prob.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    txt1Prob.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));

    arrayProps = new ArrayProperties();

    /*
     * arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,Color.red);
     * arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
     * arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.gray);
     * arrayProps
     * .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,Color.yellow);
     * arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
     * Color.black);
     */
    ami = new ArrayMarkerProperties();
    ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    amj = new ArrayMarkerProperties();
    amj.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    amj.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
  }

  public void showSourceCode() {

    scProb = new SourceCodeProperties();
    scProb.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.blue);
    scProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 16));
    scProb.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    scProb.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);

    scProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 16));
    sc = lang.newSourceCode(new Coordinates(40, 220), "sourceCode", null,
        scProb);

    sc.addCodeLine("public  void bucketsort(int anzahlBuckets, int z[]) {",
        null, 0, null);
    sc.addCodeLine("int buckets[] = new int[anzahlBuckets];", null, 1, null);
    sc.addCodeLine("for (int i=0; i<z.length; i++) {", null, 1, null);
    sc.addCodeLine("buckets[z[i]]++;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("int x=0;", null, 1, null);
    sc.addCodeLine("for (int j=0; j<anzahlBuckets; j++) {", null, 1, null);
    sc.addCodeLine("while (buckets[j] > 0) {", null, 2, null);
    sc.addCodeLine("z[x++] = j;", null, 3, null);
    sc.addCodeLine("buckets[j]--;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
  }

  public void setTitle() {
    Text title = lang.newText(new Coordinates(20, 30), "BucketSortDemo",
        "title", null, txtProb);
    lang.newRect(new Offset(-5, -5, title, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, title, AnimalScript.DIRECTION_SE), "recht", null);
  }

  public void bucketsort(int anzahlBuckets, int[] z) {
    IntArray eingabe = lang.newIntArray(new Coordinates(50, 140), z, "array",
        null, arrayProps);
    lang.newText(new Offset(0, 0, eingabe, AnimalScript.DIRECTION_SW), "array",
        "arr", null, txt1Prob);
    showSourceCode();
    sc.highlight(0);
    setTitle();
    lang.nextStep();
    sc.toggleHighlight(0, 1);
    Timing time = new TicksTiming(10);
    int buckets[] = new int[anzahlBuckets];
    IntArray bucket = lang.newIntArray(new Coordinates(250, 140), buckets,
        "buckets", null, arrayProps);
    lang.newText(new Offset(0, 0, bucket, AnimalScript.DIRECTION_SW),
        "buckets", "ar", null, txt1Prob);
    lang.nextStep();
    sc.unhighlight(1);
    ArrayMarker i = lang.newArrayMarker(eingabe, 0, "i", null, ami);
    for (; i.getPosition() < eingabe.getLength(); i.increment(null, time)) {
      sc.highlight(2);
      lang.nextStep();
      sc.toggleHighlight(2, 3);
      bucket.highlightCell(eingabe.getData(i.getPosition()), null, null);
      bucket.put(eingabe.getData(i.getPosition()), bucket.getData(eingabe
          .getData(i.getPosition())) + 1, null, null);
      lang.nextStep();
      sc.unhighlight(3);
      bucket.unhighlightCell(eingabe.getData(i.getPosition()), null, null);
      lang.nextStep();
      // buckets[z[i]]++;
    }
    i.hide();
    lang.nextStep();
    sc.highlight(5);
    int x = 0;
    lang.nextStep();
    sc.unhighlight(5);
    ArrayMarker j = lang.newArrayMarker(bucket, 0, "j", null, amj);
    for (; j.getPosition() < anzahlBuckets; j.increment(null, time)) {
      sc.highlight(6);
      lang.nextStep();
      sc.toggleHighlight(6, 7);
      lang.nextStep();
      sc.unhighlight(7);
      while (bucket.getData(j.getPosition()) > 0) {
        sc.highlight(7);
        lang.nextStep();
        sc.toggleHighlight(7, 8);
        eingabe.highlightCell(x, null, null);
        eingabe.put(x++, j.getPosition(), null, null);
        lang.nextStep();
        eingabe.unhighlightCell(x - 1, null, null);
        sc.toggleHighlight(8, 9);
        // z[x++] = j;
        bucket.put(j.getPosition(), bucket.getData(j.getPosition()) - 1, null,
            null);
        lang.nextStep();

        sc.unhighlight(9);
        // buckets[i]--;
      }
    }
    j.hide();
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    BucketSort probe = new BucketSort();
    probe.init();
    probe.arrayProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");

    int anzahlBuckets = ((Integer) arg1.get("AnzahlBucket"));
    int[] array = (int[]) arg1.get("array");

    probe.bucketsort(anzahlBuckets, array);
    return probe.lang.toString();
  }

  @Override
  public String getAlgorithmName() {

    return "Bucket Sort";
  }

  @Override
  public String getAnimationAuthor() {

    return "Iaad Zabar, Abdulghani Alshadadi";
  }

  @Override
  public String getCodeExample() {

    return "void bucketsort(int n, int anzahlBuckets, int z[]) { \n"
        + "	int buckets[] = new int[anzahlBuckets]; \n"
        + "	for (int i=0; i<n; i++) { \n" + "		buckets[z[i]]++; \n" + "	}\n"
        + "	int x=0; \n" + "	for (int i=0; i<anzahlBuckets; i++) { \n"
        + "		while (buckets[i] > 0) { \n" + "			z[x++] = i; \n"
        + "			buckets[i]--; \n" + "		}\n" + "	} \n" + "}";
  }

  @Override
  public Locale getContentLocale() {

    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {

    return "Bucketsort zählt die Häufigkeit jedes Schlüsselwertes in einer Liste. Daraus errechnet es die korrekte Position \n"
        + "jedes Elements und fügt es in einer zweiten Liste dort ein.\n"
        + "Die Häufigkeit der Schlüsselwerte wird in einem so genannten Histogramm gespeichert. Dies wird meist \n"
        + "als Array implementiert, das so lang ist, wie es mögliche Schlüsselwerte gibt; als Indizes werden dabei die \n"
        + "Schlüsselwerte bzw. die ihnen zugeordneten ganzen Zahlen gewählt. Elemente mit gleichem Sortierschlüssel werden dabei \n"
        + "in Gruppen, so genannten Buckets, zusammengefasst.";
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

    return "Bucket Sort";
  }

  @Override
  public String getOutputLanguage() {

    return Generator.JAVA_OUTPUT;
  }

}
