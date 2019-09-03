package generators.sorting;

import static algoanim.animalscript.AnimalScript.DIRECTION_NE;
import static algoanim.animalscript.AnimalScript.DIRECTION_SW;
import static algoanim.properties.AnimationPropertiesKeys.COLOR_PROPERTY;
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
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Jan Kassens
 * @author Zoran Zaric
 * @author Jürgen Benjamin Ronshausen
 */
public class KRZRadixSort extends AbstractGenerator {
  private Text       headline;
  private Text       lengthValue;
  private Text       stelleValue;
  private int        stelle;
  private Bucket     array;
  private SourceCode partitionSourceCode;
  private SourceCode collectSourceCode;
  private int        uid = 0;

  public KRZRadixSort() {
  }

  public void sort(int[] inputArray) {
    int n = 0;
    for (int i : inputArray) {
      n = (int) Math.max(n, Math.floor(Math.log10(i)) + 1);
    }

    headline = lang.newText(new Coordinates(30, 30), "Radixsort", "headline",
        null, HEADLINE_PROPERTIES);
    showDescription();
    showVariables();

    String partitionierenDescription = "Partitionieren";
    Text partitionCode = lang.newText(new Offset(20, 0, sourceCode,
        DIRECTION_NE), partitionierenDescription, getUID(), null,
        LABEL_PROPERTIES);
    partitionSourceCode = lang.newSourceCode(new Offset(0, 0, partitionCode,
        DIRECTION_SW), getUID(), null, SOURCECODE_PROPERTIES);
    partitionSourceCode.addCodeLine("while Stapel not empty do", getUID(), 0,
        null);
    partitionSourceCode.addCodeLine("zahl = Stapel.dequeue()", getUID(), 1,
        null);
    partitionSourceCode.addCodeLine("ziffer = zahl.getStelle(position)",
        getUID(), 1, null);
    partitionSourceCode.addCodeLine("F\u00E4cher[ziffer].enqueue(zahl)",
        getUID(), 1, null);

    Text collectCode = lang.newText(new Offset(0, 20, partitionSourceCode,
        DIRECTION_SW), "Einsammeln", getUID(), null, LABEL_PROPERTIES);
    collectSourceCode = lang.newSourceCode(new Offset(0, 0, collectCode,
        DIRECTION_SW), getUID(), null, SOURCECODE_PROPERTIES);
    collectSourceCode.addCodeLine("for Fach in F\u00E4cher do", getUID(), 0,
        null);
    collectSourceCode.addCodeLine("while Fach not empty do", getUID(), 1, null);
    collectSourceCode.addCodeLine("Stapel.enqueue(Fach.dequeue())", getUID(),
        2, null);

    array = new Bucket("Stapel", new Offset(-20, 95, sourceCode, DIRECTION_SW),
        lang);
    for (int i = 0; i < inputArray.length; i++) {
      array.add(new Zahl(inputArray[i], n, lang));
    }

    execute("findLength");

    // Länge der längsten Zahl
    lengthValue.setText(String.valueOf(n), null, null);

    // Initialisiere die Stelle
    execute("initStelle");
    setStelle(0);

    // Initialisiere die Buckets
    execute("initBuckets");

    new Bucket("F\u00E4cher", new Offset(350, 70, sourceCode, DIRECTION_SW),
        lang);

    Bucket[] buckets = new Bucket[10];
    for (int i = 0; i <= 9; i++) {
      buckets[i] = new Bucket(Integer.toString(i), new Offset(110 + 60 * i, 90,
          sourceCode, DIRECTION_SW), this.lang);
    }

    while (getStelle() < n) {
      execute("while");

      // Buckets leeren
      for (int i = 0; i <= 9; i++) {
        buckets[i].empty();
      }

      // partitionieren
      execute("partition");
      partitionCode.changeColor(COLOR_PROPERTY, Color.RED, null, null);
      for (int i = 0; i < 4; i++)
        partitionSourceCode.highlight(i);
      for (Zahl z : array.getZahlen()) {
        buckets[z.getStelle(getStelle())].add(z);
        lang.nextStep();
      }

      // sammle ein
      execute("collect");
      partitionCode.changeColor(COLOR_PROPERTY, Color.BLACK, null, null);
      collectCode.changeColor(COLOR_PROPERTY, Color.RED, null, null);
      for (int i = 0; i < 4; i++)
        partitionSourceCode.unhighlight(i);
      for (int i = 0; i < 3; i++)
        collectSourceCode.highlight(i);

      array.empty();
      for (Bucket bucket : buckets) {
        for (Zahl z : bucket.getZahlen()) {
          array.add(z);
          lang.nextStep();
        }
      }

      // verschiebe den Zeiger
      execute("next");
      collectCode.changeColor(COLOR_PROPERTY, Color.BLACK, null, null);
      for (int i = 0; i < 3; i++)
        collectSourceCode.unhighlight(i);
      setStelle(getStelle() + 1);
    }
    execute("while");
    execute("return");
  }

  private void setStelle(int stelle) {
    this.stelle = stelle;
    stelleValue.setText(String.valueOf(stelle + 1), null, null);
    for (Zahl z : array.getZahlen()) {
      z.setHighlight(stelle);
    }
  }

  private int getStelle() {
    return this.stelle;
  }

  private void showVariables() {
    sourceCode = lang.newSourceCode(new Offset(0, 20, headline, DIRECTION_SW),
        "sourceCode", null, SOURCECODE_PROPERTIES);
    parse();

    // Maximale Anzahl der Stellen
    Text lengthLabel = lang
        .newText(new Offset(0, 20, sourceCode, DIRECTION_SW),
            "Maximale Anzahl der Stellen: ", "length_label", null,
            LABEL_PROPERTIES);
    lengthValue = lang.newText(new Offset(0, 0, lengthLabel, DIRECTION_NE),
        "null", "length_value", null, LABEL_PROPERTIES);

    // Aktuelle Stelle
    Text stelleLabel = lang.newText(
        new Offset(0, 0, lengthLabel, DIRECTION_SW), "Aktuelle Stelle: ",
        "pointer_label", null, LABEL_PROPERTIES);
    stelleValue = lang.newText(new Offset(0, 0, stelleLabel, DIRECTION_NE),
        "null", "pointer_value", null, VARIABLE_PROPERTIES);
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

  public static void main(String[] args) {
    KRZRadixSort s = new KRZRadixSort();
    s.init();
    System.out.println(s.generate());
  }

  public String generate() {
    int[] array = { 124, 523, 9, 128, 923, 584 };
    this.sort(array);
    return this.lang.toString();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int[] array = (int[]) primitives.get("Input Array");
    this.sort(array);
    return this.lang.toString();
  }

  public String getAlgorithmName() {
    return "Radixsort";
  }

  public String getCodeExample() {
    return "Ermitteln der Anzahl Stellen n der gr\u00F6\u00DFten Zahl \n"
        + "F\u00FCr alle i = 0; i <= n, i = i + 1 \n"
        + "    F\u00FCr alle Elemente im Stapel \n"
        + "        Nehme oberstes Element vom Stapel \n"
        + "        Finde Fachnummer = i Stelle von rechts \n"
        + "        Lege aktuelles Element in dieses Fach ab \n" + "    }\n"
        + "    F\u00FCr alle F\u00E4cher von links nach rechts \n"
        + "        Nehme alle Elemente des Fachs \n"
        + "        Packe die Elemente unter das unterste Element im Stapel \n"
        + "    } \n" + "} \n" + "Gebe sortierten Stapel aus";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Radixsort ist ein Algorithmus, der von Lochkartensortiermaschinen verwendet\n"
        + "wurde. Lochkarten sind in 80 Spalten organisiert und in jeder Spalte kann an\n"
        + "einem von 12 Pl\u00E4tzen ein Loch gestanzt werden. Die Sortiermaschine untersucht\n"
        + "eine eingestellte Spalte einer jeden Lochkarte eines Stapels und sortiert die\n"
        + "Lochkarte dann in das entsprechende Fach von 12 F\u00E4chern. Fasst man die Karten\n"
        + "vom kleinsten zum gr\u00F6\u00DFten Wert aufsteigend zusammen und wiederholt den Vorgang\n"
        + "mehrfach von niedrigwertiger zum h\u00F6chstwertiger Stelle, so erh\u00E4lt man ein\n"
        + "stabiles Sortierverfahren mit einer Laufzeit O(Elemente * Stellen).";
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return "Radixsort";
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("Radixsort Animation",
        "Jan Kassens, Zoran Zaric, J\u00FCrgen Benjamin Ronshausen", 640, 480);
    lang.setStepMode(true);
  }

  @Override
  protected String[] getAnnotatedSrc() {
    return new String[] {
        "Stellen = Anzahl Stellen der gr\u00F6\u00DFten Zahl   @label(\"findLength\")\n",
        "Position = 1                                @label(\"initStelle\")\n",
        "Initialisiere F\u00E4cher                        @label(\"initBuckets\")\n",
        "while Position <= Stellen {                 @label(\"while\")\n",
        "   Partitionieren()                         @label(\"partition\")\n",
        "   Einsammeln()                             @label(\"collect\")\n",
        "   Position = Position + 1                  @label(\"next\")\n",
        "}                                           @label(\"endWhile\")\n",
        "Gebe sortierten Stapel aus                  @label(\"return\")\n" };
  }

  private String getUID() {
    return "uid_" + this.uid++;
  }
}
