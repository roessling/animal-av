package generators.sorting;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AnnotatedRadixSort extends AnnotatedAlgorithm implements Generator {

  private Language             l;
  private StringMatrix         matrix;
  private final static String  ST             = ". Stelle wird sortiert";
  private Text                 text;
  private Text                 rectText;
  private Text                 partCollText;
  private final static Timing  StandardTiming = new TicksTiming(40);
  private int                  elementLength;

  private int[]                keysData;
  private SourceCodeProperties sourceProps;
  private MatrixProperties     matrixProps;

  public AnnotatedRadixSort() {
    l = new AnimalScript("RadixSort Animation",
        "Ioannis Tsigaridas & Manuel Wick", 800, 600);
    l.setStepMode(true);
  }

  @Override
  public String getAnnotatedSrc() {

    return "Radixsort(keys) {					    @label(\"header\")\n"
        + "   int s, int k;						@label(\"indizes\") @declare(\"int\", \"s\", \""
        + elementLength
        + "\") @declare(\"int\", \"k\")  \n"
        + "   for(s=1; s<=numberOfDigits; s++){	@label(\"for1\")  @dec(\"s\") @set(\"k\", \"0\") \n"
        + "      for(k=1; k<=keys.length; k++){ 	@label(\"for2\")  @inc(\"k\") \n"
        + "         partition(k, k.digit(s));		@label(\"partitioniere\") \n"
        + "      }								@label(\"klammer\") \n"
        + "      for(k=1;k<=keys.length;k++){		@label(\"for3\") \n"
        + "         collect(k);                   @label(\"sammel\")\n"
        + "      }								@label(\"end1\") \n"
        + "   }									@label(\"end2\") \n" + "}										@label(\"end3\") \n";
  }

  private void radixSort() {

    exec("indizes");
    l.nextStep();

    while (Integer.parseInt(vars.get("s")) > 0) {
      exec("for1");

      System.out.println(Integer.parseInt(vars.get("k")));

      text.setText((elementLength - Integer.parseInt(vars.get("s"))) + ST,
          null, null);

      l.nextStep();

      partition(Integer.parseInt(vars.get("s")));
      collect();

    }

    partCollText.setText("Schlüssel fertig sortiert", null, StandardTiming);
  }

  private void partition(int literalPos) {
    partCollText.setText("Partitionierungsphase", null, null);

    System.out.println(Integer.parseInt(vars.get("k")));

    while (Integer.parseInt(vars.get("k")) < matrix.getNrCols() - 1) {
      exec("for2");
      System.out.println(Integer.parseInt(vars.get("k")));

      matrix.highlightElem(0, Integer.parseInt(vars.get("k")), null, null);

      setKey(Integer.parseInt(vars.get("k")), literalPos);

      matrix.unhighlightElem(0, Integer.parseInt(vars.get("k")), null, null);
      exec("partitioniere");

      l.nextStep();

    }
  }

  private void collect() {
    int keyPos = 1;

    partCollText.setText("Sammelphase", null, null);

    l.nextStep();

    rectText.setText("", null, null);
    for (int j = 2; j < matrix.getNrRows(); j++) {

      for (int i = 1; i < matrix.getNrCols() - 1; i++) {
        if (matrix.getElement(j, i).compareTo("") != 0) {

          matrix.highlightElem(j, i, null, null);

          exec("for3");

          l.nextStep();

          matrix.unhighlightElem(j, i, null, null);
          exec("sammel");

          matrix.swap(j, i, 0, keyPos, null, new TicksTiming(40));
          keyPos++;

          l.nextStep();
          exec("end3");
        }
      }
    }
  }

  private void setKey(int keyPos, int literalPos) {
    String str = matrix.getElement(0, keyPos);
    String chr = String.valueOf(str.charAt(literalPos));
    rectText.setText(chr, null, null);

    l.nextStep();

    int i = Integer.valueOf(chr);
    set(keyPos, i);
  }

  private void set(int keyPos, int targetPos) {
    int myTargetPos = targetPos + 2;

    int i;
    for (i = 1; i < matrix.getNrCols() - 1; i++) {
      if (matrix.getElement(myTargetPos, i).compareTo("") == 0) {
        break;
      }
    }
    matrix.swap(0, keyPos, myTargetPos, i, null, new TicksTiming(40));
  }

  public static int generateRandomNumber(int numOfDigits) {
    int key;
    do {
      key = (int) Math.floor(Math.random() * Math.pow(10, numOfDigits));
    } while (String.valueOf(key).length() < numOfDigits);
    return key;
  }

  private int getLongestKey(String[] keysData) {
    int maxLength = 0;
    for (String key : keysData) {
      maxLength = key.length() > maxLength ? key.length() : maxLength;
    }
    return maxLength;
  }

  public boolean fillKeys(String[] keys) {
    int maxLength = getLongestKey(keys);
    boolean notSameLength = false;

    for (int j = 0; j < keys.length; j++) {
      int k = maxLength - keys[j].length();
      for (int i = 0; i < k; i++) {
        keys[j] = "0" + keys[j];
        notSameLength = true;
      }
    }

    return notSameLength;
  }

  @Override
  public String generate(AnimationPropertiesContainer properties,
      Hashtable<String, Object> primitives) {

    keysData = (int[]) primitives.get("keys");

    matrixProps = (MatrixProperties) properties.getPropertiesByName("matrix");
    sourceProps = (SourceCodeProperties) properties
        .getPropertiesByName("sourceCode");

    this.initLocal();
    this.radixSort();

    return l.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Radixsort";
  }

  private final static String SOURCE_CODE = "Radixsort(keys) {\n"
                                              + "   int s, int k;\n"
                                              + "   for(s=1; s<=numberOfDigits; s++){\n"
                                              + "      for(k=1; k<=keys.length; k++){\n"
                                              + "         partition(k, k.digit(s));\n"
                                              + "      }\n"
                                              + "      for(k=1;k<=keys.length;k++){\n"
                                              + "         collect(k);\n"
                                              + "      }\n" + "   }\n" + "}";

  private void showDescription() {
    SourceCode description = l.newSourceCode(new Coordinates(45, 260),
        "description", null);
    description.addCodeLine(
        "Radixsort ist ein stabiles Sortierverfahren. Die Idee dabei ist nach",
        null, 0, null);
    description
        .addCodeLine(
            "jedem Zeichen des Keys zu sortiern. Zuerst wird nach der letzten Stelle sortiert",
            null, 0, null);
    description.addCodeLine("dann nach der vorletzten Stelle usw...", null, 0,
        null);
    description
        .addCodeLine(
            "Dieser Sortiervorgang besteht aus zwei Phasen, die sich immer abwechseln.",
            null, 0, null);
    description
        .addCodeLine("Das ist die Partitionierungsphase und die Sammelphase.",
            null, 0, null);
    description
        .addCodeLine(
            "In der Partitionierungsphase werden die Keys auf Fächer(Zahlen von 0 bis 9)aufgeteilt.",
            null, 0, null);
    description
        .addCodeLine(
            "In der Sammelphase werden die Keys von den Fächern(Zahlen von 0 bis 9) in Reihenfolge",
            null, 0, null);
    description
        .addCodeLine(
            "vom niederwertigsten aufgesammelt. Diese zwei Phasen werden solange durchgeführt bis alle Stellen(Ziffern)",
            null, 0, null);
    description
        .addCodeLine(
            "untersucht worden sind. In der letzten Iteration nach dem Aufsammeln der Keys sind",
            null, 0, null);
    description.addCodeLine("die Keys aufsteigend sortiert.", null, 0, null);

    l.nextStep();
    description.hide();
  }

  private final static String DESCRIPTION = " Radixsort\n"
                                              + "\n"
                                              + "Radixsort ist ein stabiles Sortierverfahren. Die Idee dabei ist nach\n"
                                              + "jedem Zeichen des Keys zu sortiern. Zuerst wird nach der letzten Stelle sortiert,\n"
                                              + "dann nach der vorletzten Stelle usw...\n"
                                              + "Dieser Sortiervorgang besteht aus zwei Phasen, die sich immer abwechseln.\n"
                                              + "Das ist die Partitionierungsphase und die Sammelphase.\n"
                                              + "In der Partitionierungsphase werden die Keys auf Fächer(Zahlen von 0 bis 9)aufgeteilt.\n"
                                              + "In der Sammelphase werden die Keys von den Fächern(Zahlen von 0 bis 9) in Reihenfolge\n"
                                              + "vom niederwertigsten aufgesammelt. Diese zwei Phasen werden solange durchgeführt bis alle Stellen(Ziffern)\n"
                                              + "untersucht worden sind. In der letzten Iteration nach dem Aufsammeln der Keys sind\n"
                                              + "die Keys aufsteigend sortiert."
                                              + "\n"
                                              + "\n"
                                              + "\n"
                                              + "Wichtig: Die Keys, die Sie eingeben, muessen die gleiche Anzahl an Stellen besitzen."
                                              + "";

  @Override
  public String getCodeExample() {
    return SOURCE_CODE;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
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
    return "RadixSort";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public String getAnimationAuthor() {
    return "Manuel Wick, Ioannis Tsigaridas";
  }

  public void initLocal() {
    super.init();

    String[] keys = new String[keysData.length];

    for (int i = 0; i < keysData.length; i++) {
      keys[i] = String.valueOf(keysData[i]);
    }

    if (fillKeys(keys)) {
      System.out
          .println("Schlüssel haben ungleiche Länge. Zu kurze Schlüssel wurden aufgefüllt.");
    }

    String[][] matrixKeys = new String[13][keys.length + 1];

    for (int i = 0; i < matrixKeys.length; i++) {
      for (int j = 0; j < matrixKeys[0].length; j++) {
        matrixKeys[i][j] = "";
      }
    }

    for (int i = 0; i < keys.length; i++) {
      matrixKeys[0][1 + i] = keys[i];
    }

    int n = 0;
    for (int i = 2; i < 13; i++) {
      matrixKeys[i][0] = String.valueOf(n);
      n++;
    }

    Node upperLeft = new Coordinates(60, 40);
    Text titleText = l.newText(upperLeft, "RadixSort", "titleText", null);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    l.newRect(new Offset(-15, -5, titleText, AnimalScript.DIRECTION_NW),
        new Offset(15, 5, titleText, AnimalScript.DIRECTION_SE), "titleBox",
        null, rectProps);

    partCollText = l.newText(new Offset(20, 80, "titleText",
        AnimalScript.DIRECTION_NW), "", "partcoll", null);

    sourceCode = l.newSourceCode(new Offset(260, -90, "partcoll",
        AnimalScript.DIRECTION_NW), "sourceCode", null, sourceProps);

    showDescription();

    text = l.newText(new Offset(0, 20, "partcoll", AnimalScript.DIRECTION_NW),
        "1" + ST, "st", null);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));
    rectText = l.newText(new Offset(30, 0, "st", AnimalScript.DIRECTION_NE),
        "0", "rectText", null, textProps);

    l.newRect(new Offset(-5, -5, "rectText", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "rectText", AnimalScript.DIRECTION_SE), "rect", null);
    rectText.setText("", null, null);

    matrix = l.newStringMatrix(new Offset(-20, 120, "st",
        AnimalScript.DIRECTION_SW), matrixKeys, "matrix", null, matrixProps);

    elementLength = matrix.getElement(0, 1).length();

    parse();

    exec("header");
    l.nextStep();
  }

}
