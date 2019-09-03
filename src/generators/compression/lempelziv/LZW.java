package generators.compression.lempelziv;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class LZW extends CompressionAlgorithm implements
    generators.framework.Generator {

  private SourceCode          sc;

  private static final int    inputLimit  = 24;

  private static final String DESCRIPTION = "Der Lempel-Ziv-Welch-Algorithmus ist ein verlustfreies Kompressionsverfahren f&uuml;r Texte. Er"
                                              + " verwendet ein W&ouml;rterbuch, um h&auml;ufig vorkommende Zeichenketten durch einen Schl&uuml;ssel zu ersetzen."
                                              + " Einzelne Zeichen werden durch ihren jeweiligen ASCII-Wert kodiert. Es wird kein W&ouml;rterbuch"
                                              + " aus einer Datenbank ben&ouml;tigt. Die Erstellung des W&ouml;rterbuchs erfolgt zur Laufzeit. So werden"
                                              + " aufeinanderfolgende Zeichen sukzessive dem W&ouml;rterbuch hinzugef&uuml;gt, sofern ihr Pr&auml;fix bereits"
                                              + " im W&ouml;rterbuch vorhanden ist.";

  private static final String SOURCE_CODE = "Der Algorithmus wird in einer Animation demonstriert. Um die grafische Animation in voller Größe darstellen"
                                              + " zu können, wird die Eingabe auf 24 Buchstaben begrenzt.\n\n"
                                              + "public static void lzw(String[] text) {"
                                              + "\nString w, k, result;"
                                              + "\n int cnt = 256;"
                                              + "\n Hashtable<String, Integer> dict = new Hashtable<String, Integer>();"
                                              + "\n  for (int i = 0; i < 256;i++) {"
                                              + "\n   dict.put(\"\" + ((char)i), i);"
                                              + "\n  }"
                                              + "\n  for (int i=0; i < text.length; i++) {"
                                              + "\n   k = text[i];"
                                              + "\n   if (dict.containsKey(w + k)) w = w + k;"
                                              + "\n   else {"
                                              + "\n    result += dict.get(w) + \" \";"
                                              + "\n    dict.put(w + k, cnt);"
                                              + "\n    cnt++;"
                                              + "\n    w = k;"
                                              + "\n   }" + "\n  }" + "\n }";

  public LZW() {
    // nothing to be done
  }

  public void compress(String[] text) throws LineNotExistsException {
    // trim input to maximum length
    // String ein = "";
    String[] t = new String[Math.min(text.length, inputLimit)];
    for (int i = 0; i < t.length; i++) {
      t[i] = text[i];
      // ein += text[i];
    }
    // text = t;

    // topic
    Text topic = lang.newText(new Coordinates(20, 50), "LZW", "Topic", null,
        tptopic);

    lang.newRect(new Offset(-5, -5, topic, "NW"),
        new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

    // Algo in words
    lang.nextStep();
    Text algoinWords = lang.newText(new Coordinates(20, 100),
        "Der Algorithmus in Worten", "inWords", null, tpwords);

    lang.nextStep();
    Text step0 = lang
        .newText(
            new Offset(0, 100, topic, "SW"),
            "0) Der Algorithmus erzeugt während der Laufzeit eigenständig ein Wörterbuch. Einzelne Zeichen",
            "line0", null, tpsteps);
    Text step01 = lang
        .newText(
            new Offset(0, 20, step0, "SW"),
            "    werden gemäß ASCII-Standard kodiert. Für diese Zeichen sind also im Wörterbuch die Einträge",
            "line0", null, tpsteps);
    Text step02 = lang
        .newText(
            new Offset(0, 20, step01, "SW"),
            "    0 bis 255 reserviert. Für jede neue Kodierung wird sukzessive ab 256 ein neuer Eintrag angelegt.",
            "line0", null, tpsteps);
    lang.nextStep();
    Text step1 = lang
        .newText(new Offset(0, 40, step02, "SW"),
            "1) Iteriere buchstabenweise über die Eingabe:", "line1", null,
            tpsteps);
    lang.nextStep();
    Text step2 = lang
        .newText(
            new Offset(0, 40, step1, "SW"),
            "2) Betrachte an jeder Stelle den vorhergegangen Buchstaben bzw. ein Präfix, welches in 3) entstehen kann.",
            "line2", null, tpsteps);
    lang.nextStep();
    Text step3 = lang
        .newText(
            new Offset(0, 40, step2, "SW"),
            "3) Ist die Kombination des Präfixes im Wörterbuch abgespeichert, so vergrößere das aktuelle Präfix",
            "line3", null, tpsteps);
    Text step31 = lang
        .newText(
            new Offset(0, 20, step3, "SW"),
            "    um den eingelesenen Buchstaben und fahre beim nächsten Buchstaben in 2) fort.",
            "line31", null, tpsteps);
    lang.nextStep();
    Text step4 = lang
        .newText(
            new Offset(0, 40, step31, "SW"),
            "4) Ansonsten kodiere das Präfix des aktuellen Buchstabens durch das Wörterbuch, füge das Präfix konkateniert",
            "line4", null, tpsteps);
    Text step41 = lang
        .newText(
            new Offset(0, 20, step4, "SW"),
            "    mit dem aktuellen Buchstaben dem Wörterbuch hinzu und fahre beim nächsten Buchstaben fort.",
            "line41", null, tpsteps);
    lang.nextStep();
    algoinWords.hide();
    step0.hide();
    step01.hide();
    step02.hide();
    step1.hide();
    step2.hide();
    step3.hide();
    step31.hide();
    step4.hide();
    step41.hide();

    StringArray strArray = lang.newStringArray(new Offset(0, 100, topic, "SW"),
        t, "stringArray", null, ap);

    SourceCodeProperties scp = new SourceCodeProperties();
    scp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.RED);
    scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sc = lang.newSourceCode(new Offset(0, 50, strArray, "SW"), "codeName",
        null, scp);
    sc.addCodeLine("public void lzw(String[] array) {", null, 0, null);
    sc.addCodeLine("String w, k, result;", null, 1, null);
    sc.addCodeLine("int cnt = 256;", null, 1, null);
    sc.addCodeLine("Hashtable dict = internSetupMethod();", null, 1, null);
    sc.addCodeLine("for (int i=0; i < t.length; i++) {", null, 1, null);
    sc.addCodeLine("k = text[i];", null, 2, null);
    sc.addCodeLine("if (dict.containsKey(w + k)) w = w + k;", null, 2, null);
    sc.addCodeLine("else { ", null, 2, null);
    sc.addCodeLine("result += dict.get(w);", null, 3, null);
    sc.addCodeLine("dict.put(w + k, cnt);", null, 3, null);
    sc.addCodeLine("cnt++;", null, 3, null);
    sc.addCodeLine("w = k;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    lang.nextStep();

    // start the algorithm
    ArrayMarkerProperties amp = new ArrayMarkerProperties();
    amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    sc.highlight(0, 0, false);

    lang.nextStep();
    sc.toggleHighlight(0, 0, false, 1, 0);

    lang.nextStep();
    sc.toggleHighlight(1, 0, false, 2, 0);

    lang.nextStep();
    sc.toggleHighlight(2, 0, false, 3, 0);

    lang.nextStep();
    sc.unhighlight(3, 0, false);
    sc.highlight(4);
    String w = "";
    String k = "";
    String result = "";
    int cnt = 256;
    // setup dicionary
    Hashtable<String, Integer> dict = new Hashtable<String, Integer>();
    for (int i = 0; i < 256; i++) {
      dict.put("" + ((char) i), i);
    }

    // setup the dictonary matrix

    String[][] dictData = new String[inputLimit][2];
    for (int i = 0; i < inputLimit; i++) {
      dictData[i][0] = "";
      dictData[i][1] = "";
    }
    StringMatrix dic = lang.newStringMatrix(
        new Offset(200, -125, strArray, "E"), dictData, "dict", null, mp);
    int matrixCount = 0;

    ArrayMarker am = lang.newArrayMarker(strArray, 0, "arrayMarker", null, amp);

    Text wLabel = lang.newText(new Offset(0, 50, sc, "SW"), "w: ", "w", null,
        tpsteps);
    Text kLabel = lang.newText(new Offset(0, 20, wLabel, "SW"), "k: ", "k",
        null, tpsteps);
    Text ausgabeLabel = lang.newText(new Offset(0, 30, kLabel, "SW"),
        "Ausgabe:", "ausgabe", null, tpsteps);
    Text ausgabe = lang.newText(new Offset(15, -5, ausgabeLabel, "SE"), "",
        "ausgabe", null, tpsteps);
    int highlightCounter = 0;
    ausgabe.changeColor(null, Color.BLUE, null, null);

    for (int i = 0; i < t.length; i++) {
      am.move(i, null, null);
      sc.highlight(4, 0, false); // highlight loop
      lang.nextStep();
      sc.toggleHighlight(4, 0, false, 5, 0);
      k = t[i];
      kLabel.setText("k:  " + k, null, null);
      lang.nextStep();
      sc.toggleHighlight(5, 0, false, 6, 0);
      if (dict.containsKey(w + k)) {
        w = w + k;
        wLabel.setText("w: " + w, null, null);
        lang.nextStep();
        sc.unhighlight(6, 0, false);
      } else {
        sc.toggleHighlight(6, 0, false, 7, 0); // highlight else {
        lang.nextStep();
        sc.toggleHighlight(7, 0, false, 8, 0);
        result += dict.get(w) + " ";
        ausgabe.setText(result, null, null);
        lang.nextStep();
        sc.toggleHighlight(8, 0, false, 9, 0);
        dict.put(w + k, cnt);

        dic.put(matrixCount, 1, "" + new Integer(cnt), null, null);
        dic.put(matrixCount, 0, w + k, null, null);
        dic.highlightCell(highlightCounter, 0, null, null);
        dic.highlightCell(highlightCounter, 1, null, null);
        if (highlightCounter > 0) {
          dic.unhighlightCell(highlightCounter - 1, 1, null, null);
          dic.unhighlightCell(highlightCounter - 1, 0, null, null);
        }
        highlightCounter++;
        matrixCount++;

        lang.nextStep();
        sc.toggleHighlight(9, 0, false, 10, 0);
        cnt++;
        lang.nextStep();
        sc.toggleHighlight(10, 0, false, 11, 0);
        w = k;
        wLabel.setText("w: " + w, null, null);
        lang.nextStep();
        sc.unhighlight(11, 0, false);
      }
    }

    Text fazit1 = lang
        .newText(
            new Offset(0, 90, ausgabeLabel, "SW"),
            "Das Wörterbuch wird dabei nicht mit ausgegeben, da es in der Dekompression",
            "name", null, tpsteps);

    lang.newText(new Offset(0, 20, fazit1, "SW"), "neu generiert werden kann.",
        "fazit", null, tpsteps);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    String[] strArray = (String[]) primitives.get("stringArray");
    try {
      compress(strArray);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
  }

  public String getName() {
    return "LZW-Komprimierung";
  }

  @Override
  public String getAlgorithmName() {
    return "LZW (Lempel, Ziv, Welch 1984)";
  }

  @Override
  public void init() {
    lang = new AnimalScript("LZW Compression", "Florian Lindner", 800, 600);
    lang.setStepMode(true);
  }
}
