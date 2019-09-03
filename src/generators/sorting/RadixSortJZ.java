package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.Bucket;
import generators.helpers.Zahl;
import generators.sorting.helpers.AbstractGenerator;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * 
 * @author Jan Kassens
 * @author Zoran Zaric
 * 
 */
public class RadixSortJZ extends AbstractGenerator {

  public RadixSortJZ() {
    lang = new AnimalScript("RadixSort Animation",
        "Jan Kassens und Zoran Zaric", 640, 480);
    ;
    lang.setStepMode(true);
  }

  public void sort(int[] array) {

    Text headline = lang.newText(new Coordinates(MARGIN, MARGIN), "RadixSort",
        "headline", null, HEADLINE_PROPERTIES);

    // Ausführliche description
    Text description = lang.newText(new Offset(0, PADDING, headline,
        AnimalScript.DIRECTION_SW), "RadixSort sortiert zahlen", "description",
        null, LABEL_PROPERTIES);

    lang.nextStep();
    description.hide();

    Text lengthLabel = lang.newText(new Offset(0, PADDING, headline,
        AnimalScript.DIRECTION_SW), "Länge der längsten Zahl: ",
        "length_label", null, LABEL_PROPERTIES);

    Text lengthValue = lang.newText(new Offset(0, 0, lengthLabel,
        AnimalScript.DIRECTION_NE), "nil", "length_value", null,
        LABEL_PROPERTIES);

    Text stelleLabel = lang.newText(new Offset(0, 0, lengthLabel,
        AnimalScript.DIRECTION_SW), "Stelle: ", "pointer_label", null,
        LABEL_PROPERTIES);

    Text stelleValue = lang.newText(new Offset(0, 0, stelleLabel,
        AnimalScript.DIRECTION_NE), "nil", "pointer_value", null,
        LABEL_PROPERTIES);

    sourceCode = lang.newSourceCode(new Offset(PADDING, PADDING - 10 /*
                                                                      * wird von
                                                                      * animal
                                                                      * geschluckt
                                                                      */,
        stelleLabel, AnimalScript.DIRECTION_SW), "sourceCode", null,
        SOURCECODE_PROPERTIES);
    parse();

    Text partitionCode = lang.newText(new Offset(PADDING, 0, sourceCode,
        AnimalScript.DIRECTION_NE), "Partitionieren", "partitionCode", null,
        LABEL_PROPERTIES);
    Text collectCode = lang.newText(new Offset(0, PADDING, partitionCode,
        AnimalScript.DIRECTION_SW), "Einsammeln der Werte", "collectCode",
        null, LABEL_PROPERTIES);
    // richtiger untercode hier

    /* Mache ein Zahlen-Array aus dem Integer-Array */
    Bucket newArray = new Bucket("Array", new Offset(-PADDING, PADDING,
        sourceCode, AnimalScript.DIRECTION_SW), lang);

    for (int i = 0; i < array.length; i++) {
      newArray.add(new Zahl(array[i], 5, lang));
    }

    execute("findLength");

    // Finde die Länge der längsten Zahl
    int n = 0;
    for (int i : array) {
      n = (int) Math.max(n, Math.floor(Math.log10((double) i)) + 1);
    }
    lengthValue.setText(String.valueOf(n), null, null);

    // Initialisiere die Stelle
    execute("initStelle");
    int stelle = 0;
    stelleValue.setText(String.valueOf(stelle + 1), null, null);

    /* Initialisiere die Buckets */
    execute("initBuckets");

    Bucket[] newBuckets = new Bucket[10];
    for (int i = 0; i <= 9; i++) {
      newBuckets[i] = new Bucket(Integer.toString(i), new Offset(60 + 60 * i,
          PADDING, sourceCode, AnimalScript.DIRECTION_SW), this.lang);
    }

    while (stelle < n) {
      execute("while");

      // Buckets leeren
      for (int i = 0; i <= 9; i++) {
        newBuckets[i].empty();
      }

      // partitionieren
      execute("partition");
      partitionCode.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.RED, null, null);
      for (Zahl z : newArray.getZahlen()) {
        newBuckets[z.getStelle(stelle)].add(z);
        lang.nextStep();
        lang.nextStep();
      }

      // sammle ein
      execute("collect");
      partitionCode.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.BLACK, null, null);
      collectCode.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.RED, null, null);

      newArray.empty();
      for (Bucket bucket : newBuckets) {
        for (Zahl z : bucket.getZahlen()) {
          newArray.add(z);
          lang.nextStep();
          lang.nextStep();
        }
      }

      // verschiebe den Zeiger
      execute("next");
      collectCode.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.BLACK, null, null);
      stelle++;
      stelleValue.setText(String.valueOf(stelle + 1), null, null);
    }
    execute("while");
    execute("return");
  }

  public String generate() {
    int[] array = { 124, 523, 483, 128, 923, 584 };
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
    return "Radixsort";
  }

  public String getCodeExample() {
    return "Finde die Länge der längsten Zahl heraus;\n"
        + "Setze die Stele auf 1\n" + "Initialisiere Buckets\n"
        + "Solange Stelle kleiner gleich Länge der längsten Zahl {\n"
        + "  partitioniere\n" + "  sammle ein\n"
        + "  verschiebe den Zeiger um eine Stelle nach links";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Description";
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return "RadixSort";
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    // nothing to be done here
  }

  @Override
  protected String[] getAnnotatedSrc() {
    return new String[] {
        //
        "Finde die Länge der längsten Zahl heraus                @label(\"findLength\")\n",
        "Setze die Stelle auf 1                                  @label(\"initStelle\")\n",
        "Initialisiere Buckets                                   @label(\"initBuckets\")\n",
        "Solange Stelle kleiner gleich Länge der längsten Zahl { @label(\"while\")\n",
        "	partitioniere                                        @label(\"partition\")\n",
        "	sammle ein                                           @label(\"collect\")\n",
        "	verschiebe den Zeiger um eine Stelle nach links      @label(\"next\")\n",
        "}                                                       @label(\"endWhile\")\n",
        "Gebe sortierte Liste zurück                             @label(\"return\")\n" };
  }
}
