package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Histogramm implements Generator {
  private Language             lang;
  private SourceCodeProperties description;
  private ArrayProperties      arrayProps;
  private int[]                original;
  private RectProperties       rect;
  private SourceCodeProperties scProps;
  private TextProperties       textProps;
  // private IntArray arr ;
  private IntArray             init;
  private IntArray             sumArr;
  private IntArray             arrTrans;
  private IntArray             ausgeglichen;
  private SourceCode           sc;

  public void init() {
    lang = new AnimalScript("Histogramm-Ausgleich",
        "Tabea Born, Yasmin Krahofer", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    description = (SourceCodeProperties) props
        .getPropertiesByName("description");
    arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
    original = (int[]) primitives.get("original");
    rect = (RectProperties) props.getPropertiesByName("rect");
    scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
    textProps = (TextProperties) props.getPropertiesByName("textProps");

    hist(original);

    return lang.toString();
  }

  public String getName() {
    return "Histogramm-Ausgleich";
  }

  public String getAlgorithmName() {
    return "Histogramm-Ausgleich";
  }

  public String getAnimationAuthor() {
    return "Tabea Born, Yasmin Krahofer";
  }

  public String getDescription() {
    return "&Uuml;ber den Algorithmus:"
        + "\n"
        + "\n"
        + "Dieser Algorithmus wird in der Bildverarbeitung verwendet. Man kann mit ihm"
        + "\n"
        + "zu helle oder zu dunkle Bilder ausgleichen, so dass sich die Grauwerte,"
        + "\n"
        + "gleichm&auml;&szlig;ig verteilen."
        + "\n"
        + "\n"
        + "Hierf&uuml;r wird eine Wahrscheinlichkeitsformel T(p) auf das"
        + "\n"
        + "unausgeglichene Histogramm angewendet."
        + "\n"
        + "\n"
        + "T(p) = ((maximalerGrauwert)/(Bildgr&ouml;&szlig;e)) *"
        + "\n"
        + "(AufsummiertesHistogramm (i))"
        + "\n"
        + "\n"
        + "Somit wird jedem Grauwert p ein neuer Grauwert T(p) zugeordnet, indem"
        + "\n"
        + "die aufsummierten relativen H&auml;ufigkeiten bis zum Wert p mit dem"
        + "\n"
        + "maximalen Grauwert gewichtet werden. Die neuen Werte werden gerundet und"
        + "\n"
        + "die Grauwert-Verteilung des Originalbildes wird auf das neue ausgeglichene"
        + "\n" + "Histogramm abgebildet." + "\n" + "\n"
        + "Quelle: Bildverarbeitung 2011 L&ouml;sung: &Uuml;bungsblatt 3";
  }

  public String getCodeExample() {
    return "Schritt 1:"
        + "\n"
        + "Aufsummierung der Grauwerte."
        + "\n"
        + "\n"
        + "Schritt 2:"
        + "\n"
        + "Wahrscheinlichkeitsformel T(p) auf jedes Feld des aufsummierten Histogramms"
        + "\n"
        + "anwenden. Das Ergebnis wird gerundet."
        + "\n"
        + "\n"
        + "Schritt 3:"
        + "\n"
        + "Das transformierte Histogramm als Look-Up Table benutzen, um damit das"
        + "\n" + "ausgeglichene Histogramm zu erstellen.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void hist(int[] inputArray) {
    showHeader();
    showSourceCode();
    showDescription();

    int[] sumA = new int[inputArray.length];
    int[] transi = new int[inputArray.length];
    int[] gleich = new int[inputArray.length];
    int[] ini = new int[inputArray.length];

    init = lang.newIntArray(new Coordinates(235, 80), ini, "init", null,
        arrayProps);
    lang.newIntArray(new Coordinates(235, 100), inputArray, "array", null,
        arrayProps);
    sumArr = lang.newIntArray(new Coordinates(235, 130), sumA, "summy", null,
        arrayProps);
    arrTrans = lang.newIntArray(new Coordinates(235, 160), transi, "trans",
        null, arrayProps);
    ausgeglichen = lang.newIntArray(new Coordinates(235, 190), gleich,
        "gleich", null, arrayProps);

    int sum = 0;
    double max = 0;
    double grayVal = 0;
    double freq = 0;

    setNames();

    for (int i = 0; i < init.getLength(); i++) {
      init.put(i, i, null, null);
    }

    lang.nextStep();
    sc.highlight(0);
    sc.highlight(1);
    lang.nextStep();
    for (int i = 0; i < inputArray.length; i++) {
      sum += inputArray[i];
      sumArr.put(i, sum, null, null);
      sumArr.highlightCell(i, null, null);
      if (i != 0) {
        sumArr.unhighlightCell(i - 1, null, null);
      }
      lang.nextStep();
    }
    sumArr.unhighlightCell(inputArray.length - 1, null, null);
    lang.nextStep();

    max = sumArr.getLength() - 1; // passt
    freq = sumArr.getData((int) max);

    sc.toggleHighlight(0, 3);
    sc.toggleHighlight(1, 4);
    sc.highlight(5);
    sc.highlight(6);
    lang.nextStep();
    for (int i = 0; i < sumArr.getLength(); i++) {
      double maxDurchGr = (max / freq);
      arrTrans.put(i,
          (int) Math.round((double) maxDurchGr * (double) sumArr.getData(i)),
          null, null);
      arrTrans.highlightCell(i, null, null);
      if (i != 0) {
        arrTrans.unhighlightCell(i - 1, null, null);
      }
      lang.nextStep();
    }
    arrTrans.unhighlightCell(inputArray.length - 1, null, null);
    lang.nextStep();

    sc.toggleHighlight(3, 8);
    sc.toggleHighlight(4, 9);
    sc.toggleHighlight(5, 10);
    sc.unhighlight(6);
    lang.nextStep();
    for (int i = 0; i < arrTrans.getLength(); i++) {
      grayVal = arrTrans.getData(i);
      freq = (int) inputArray[i];
      ausgeglichen.put((int) grayVal,
          (int) (freq + ausgeglichen.getData((int) grayVal)), null, null);
      if (i != 0) {
        ausgeglichen.unhighlightCell((int) arrTrans.getData(i - 1), null, null);
      }
      ausgeglichen.highlightCell((int) grayVal, null, null);
      lang.nextStep();
    }
    ausgeglichen.unhighlightCell(arrTrans.getData(arrTrans.getLength() - 1),
        null, null);
    sc.unhighlight(8);
    sc.unhighlight(9);
    sc.unhighlight(10);
  }

  public void showDescription() {

    description.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 15));
    SourceCode desc = lang.newSourceCode(new Coordinates(700, 70),
        "description", null, description);

    desc.addCodeLine("Ueber den Algorithmus:", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Dieser Algorithmus wird in der Bildverarbeitung verwendet. Man kann mit ihm zu helle",
        null, 1, null);
    desc.addCodeLine(
        "oder zu dunkle Bilder ausgleichen, so dass sich die Grauwerte, gleichmaessig verteilen.",
        null, 1, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Hierfuer wird eine Wahrscheinlichkeitsformel T(p) auf das unausgeglichene",
        null, 1, null);
    desc.addCodeLine("Histogramm angewendet.", null, 1, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "T(p) = ((maximalerGrauwert)/(Bildgroesse)) * (AufsummiertesHistogramm (i))",
        null, 1, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Somit wird jedem Grauwert p ein neuer Grauwert T(p) zugeordnet,",
        null, 1, null);
    desc.addCodeLine(
        "indem die aufsummierten relativen Haeufigkeiten bis zum Wert p mit maximalen",
        null, 1, null);
    desc.addCodeLine(
        "Grauwert gewichtet werden. Die neuen Werte werden gerundet und", null,
        1, null);
    desc.addCodeLine(
        "die Grauwert-Verteilung des Originalbildes wird auf das neue,", null,
        1, null);
    desc.addCodeLine("ausgeglichene Histogramm abgebildet.", null, 1, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("Quelle: Bildverarbeitung 2011 Loesung: Uebungsblatt 3",
        null, 1, null);

  }

  public void showHeader() {
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30), "Histogramm-Ausgleich",
        "header", null, textProps);

    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "HeaderBack",
        null, rect);
  }

  public void showSourceCode() {
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 15));

    sc = lang.newSourceCode(new Coordinates(40, 230), "sourceCode", null,
        scProps);
    // 0-1
    sc.addCodeLine("Schritt 1:", null, 0, null);
    sc.addCodeLine("Aufsummierung der Grauwerte.", null, 0, null);
    sc.addCodeLine("", null, 0, null);

    // 3-6
    sc.addCodeLine("Schritt 2:", null, 0, null);
    sc.addCodeLine("Wahrscheinlichkeitsformel T(p) auf jedes Feld", null, 0,
        null);
    sc.addCodeLine("des aufsummierten Histogramms anwenden.", null, 0, null);
    sc.addCodeLine("Das Ergebnis wird gerundet", null, 0, null);
    sc.addCodeLine("", null, 0, null);

    // 8-10
    sc.addCodeLine("Schritt 3:", null, 0, null);
    sc.addCodeLine("Das transformierte Histogramm als Look-Up Table benutzen,",
        null, 0, null);
    sc.addCodeLine("um damit das ausgeglichene Histogramm zu erstellen.", null,
        0, null);
    sc.addCodeLine("", null, 0, null);

  }

  public void setNames() {
    TextProperties nameProps = new TextProperties();
    nameProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    nameProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 15));

    lang.newText(new Coordinates(30, 80), "Grauwerte:", "grauwerte", null,
        nameProps);
    lang.newText(new Coordinates(30, 100), "Haeufigkeit:", "haeufigkeit", null,
        nameProps);
    lang.newText(new Coordinates(30, 130), "Aufsummiertes Histogramm:",
        "summiert", null, nameProps);
    lang.newText(new Coordinates(30, 160), "Histogramm mit Formel:",
        "histogramm", null, nameProps);
    lang.newText(new Coordinates(30, 190), "Ausgeglichenes Histogramm:",
        "ausgeglichen", null, nameProps);
  }

}