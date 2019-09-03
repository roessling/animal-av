package generators.sorting.bucketsort;

import generators.AnnotatedAlgorithm;
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
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class BucketSortAnnon extends AnnotatedAlgorithm implements
    generators.framework.Generator {

  private String                comp = "Compares";
  private String                assi = "Assignments";
  private ArrayProperties       arrayProps;
  private ArrayMarkerProperties ami;
  private ArrayMarkerProperties amj;

  private ArrayMarkerUpdater    amuI, amuJ;
  private TextProperties        txtProb, txt1Prob;

  public void init() {
    super.init();

    SourceCodeProperties scProb = new SourceCodeProperties();
    scProb.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.blue);
    scProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 16));
    scProb.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    scProb.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);

    sourceCode = lang.newSourceCode(new Coordinates(40, 220), "sourceCode",
        null, scProb);

    txtProb = new TextProperties();
    txtProb.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    txt1Prob = new TextProperties();
    txt1Prob.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    txt1Prob.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));

    arrayProps = new ArrayProperties();

    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.red);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.gray);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.yellow);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.black);

    ami = new ArrayMarkerProperties();
    ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    amj = new ArrayMarkerProperties();
    amj.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    amj.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");

    vars.declare("int", comp);
    vars.setGlobal(comp);
    vars.declare("int", assi);
    vars.setGlobal(assi);

    Text text = lang.newText(new Coordinates(300, 20), "...", "complexity",
        null);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Compares: ");
    tu.addToken(vars.getVariable(comp));
    tu.addToken(" - Assignments: ");
    tu.addToken(vars.getVariable(assi));
    tu.update();

    parse();
  }

  @Override
  public String getAnnotatedSrc() {
    // Coordinates(40,220),"sourceCode",null,scProb);
    return "public  void bucketsort(int anzahlBuckets, int z[]) {     	@label(\"header\")\n"
        + "  int buckets[] = new int[anzahlBuckets];					@label(\"neuesbucket\")@inc(\""
        + assi
        + "\")\n"
        + "  for (int i=0;												@label(\"oForInit\") @declare(\"int\", \"i\", \"0\", \"Stepper\")@inc(\""
        + assi
        + "\")\n"
        + " i<z.length; 											@label(\"oForComp1\")@continue @inc(\""
        + comp
        + "\")\n"
        + "i++) {													@label(\"oForInc\") @continue @inc(\"i\") @inc(\""
        + assi
        + "\")\n"
        + "    buckets[z[i]]++;											@label(\"bucketInc\")@inc(\""
        + assi
        + "\")\n"
        + "  }															@label(\"1.klammer\")\n"
        + "  int x=0;													@label(\"xdef\") @declare(\"int\", \"x\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "  for (int j=0; 											@label(\"jForInit\") @declare(\"int\", \"j\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "j<anzahlBuckets; 											@label(\"jForComp1\") @continue @inc(\""
        + comp
        + "\") \n"
        + "j++) {													@label(\"jForInc\") @continue @inc(\"j\") @inc(\""
        + assi
        + "\")\n"
        + "    while (buckets[j] > 0) {									@label(\"while\") @inc(\""
        + comp
        + "\")\n"
        + "      z[x] = j;													@label(\"setzen\") @inc(\""
        + assi
        + "\")\n"
        + "      x++;														@label(\"xInc\")@inc(\"x\") @inc(\""
        + assi
        + "\")\n"
        + "      buckets[j]--;												@label(\"niedriger\") @inc(\""
        + assi
        + "\")\n"
        + "    }															@label(\"2.klammer\")\n"
        + "  }															@label(\"3.klammer\")\n"
        + "}															@label(\"4.klammer\")\n";

  }

  public void setTitle() {
    Text title = lang.newText(new Coordinates(20, 30), "BucketSortDemo",
        "title", null, txtProb);
 lang.newRect(new Offset(-5, -5, title,
        AnimalScript.DIRECTION_NW), new Offset(5, 5, title,
        AnimalScript.DIRECTION_SE), "recht", null);
  }

  public void bucketsort(int anzahlBuckets, int[] z) {
    exec("header");
    IntArray eingabe = lang.newIntArray(new Coordinates(50, 140), z, "array",
        null, arrayProps);
    lang.newText(new Offset(0, 0, eingabe,
        AnimalScript.DIRECTION_SW), "array", "arr", null, txt1Prob);
    setTitle();
    lang.nextStep();

    exec("neuesbucket");

    Timing time = new TicksTiming(10);
    int buckets[] = new int[anzahlBuckets];
    IntArray bucket = lang.newIntArray(new Coordinates(250, 140), buckets,
        "buckets", null, arrayProps);
     lang.newText(
        new Offset(0, 0, bucket, AnimalScript.DIRECTION_SW), "buckets", "ar",
        null, txt1Prob);
    lang.nextStep();

    exec("oForInit");
    ArrayMarker i = lang.newArrayMarker(eingabe, 0, "i", null, ami);
    amuI = new ArrayMarkerUpdater(i, null, time, eingabe.getLength() - 1);
    amuI.setVariable(vars.getVariable("i"));
    lang.nextStep();

    while (Integer.parseInt(vars.get("i")) < eingabe.getLength()) {
      exec("oForComp1");
      lang.nextStep();
      exec("bucketInc");
      bucket.highlightCell(eingabe.getData(i.getPosition()), null, time);
      bucket.put(eingabe.getData(i.getPosition()), bucket.getData(eingabe
          .getData(i.getPosition())) + 1, null, null);
      lang.nextStep();

      bucket.unhighlightCell(eingabe.getData(i.getPosition()), null, time);
      exec("oForInc");
      lang.nextStep();

      // buckets[z[i]]++;
    }
    i.hide();
    lang.nextStep();
    exec("xdef");
    lang.nextStep();
    exec("jForInit");
    ArrayMarker j = lang.newArrayMarker(bucket, 0, "j", null, amj);
    amuJ = new ArrayMarkerUpdater(j, null, time, bucket.getLength() - 1);
    amuJ.setVariable(vars.getVariable("j"));

    while (Integer.parseInt(vars.get("j")) < anzahlBuckets) {
      exec("jForComp1");
      lang.nextStep();

      while (bucket.getData(Integer.parseInt(vars.get("j"))) > 0) {
        exec("while");

        lang.nextStep();

        eingabe.highlightCell(Integer.parseInt(vars.get("x")), null, null);
        exec("setzen");
        eingabe.put(Integer.parseInt(vars.get("x")), Integer.parseInt(vars
            .get("j")), null, null);
        lang.nextStep();
        exec("xInc");
        lang.nextStep();
        eingabe
            .unhighlightCell(Integer.parseInt(vars.get("x")) - 1, null, null);
        exec("niedriger");
        // z[x++] = j;
        bucket.put(j.getPosition(), bucket.getData(Integer.parseInt(vars
            .get("j"))) - 1, null, null);
        lang.nextStep();

        // buckets[i]--;
      }
      exec("while");
      lang.nextStep();
      exec("jForInc");
      lang.nextStep();
    }
    j.hide();
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    BucketSortAnnon probe = new BucketSortAnnon();
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

  /*
   * @Override public String getCodeExample() {
   * 
   * return "void bucketsort(int n, int anzahlBuckets, int z[]) { \n"+
   * "	int buckets[] = new int[anzahlBuckets]; \n" +
   * "	for (int i=0; i<n; i++) { \n"+ "		buckets[z[i]]++; \n" + "	}\n" +
   * "	int x=0; \n"+ "	for (int i=0; i<anzahlBuckets; i++) { \n" +
   * "		while (buckets[i] > 0) { \n" + "			z[x++] = i; \n" +
   * "			buckets[i]--; \n" + "		}\n" + "	} \n" + "}"; }
   */

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

    return "Bucket Sort using Annotations";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}
