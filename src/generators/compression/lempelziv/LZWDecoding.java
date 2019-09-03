package generators.compression.lempelziv;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.StringTokenizer;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class LZWDecoding extends CompressionAlgorithm implements Generator {

  private SourceCode          sc;

  private static final int    inputLimit  = 24;

  private static final String DESCRIPTION = "Die Lempel Ziv Welch Dekodierung dient dazu, die durch den gleichnamigen Kodierungsalgorithmus"
                                              + " komprimierten Daten wiederherzustellen. Dabei ist es nicht n&ouml;tig, dass das automatisch generierte"
                                              + " W&ouml;rterbuch der Kodierung verwendet wird. Auch bei der Dekodierung kann dieses W&ouml;rterbuch wieder automatisch"
                                              + " erstellt werden.";

  private static final String SOURCE_CODE = "Der Algorithmus wird in einer Animation demonstriert.\nUm die grafische Animation in voller Größe darstellen"
                                              + " zu k&ouml;nnen, wird die Eingabe auf 24 Buchstaben begrenzt.\nEs handelt sich hier um einen Dekodierungsalgorithmus. Ihre Eingabe wird zunächst durch die"
                                              + " entsprechende Kodierung kodiert. Erst diese Daten werden dekodiert.\n\n"
                                              + "public static void decode(String input) {" // 0
                                              + "\n  StringTokenizer t = new StringTokenizer(input);" // 1
                                              + "\n  int cnt = t.countTokens();" // 2
                                              + "\n  String w, k, result = \"\";" // 3
                                              + "\n  int dicCnt = 256;" // 4
                                              + "\n  for (int i = 0; i < cnt; i++) {" // 5
                                              + "\n    int tmp = Integer.parseInt(t.nextToken());" // 6
                                              + "\n    if (tmp < 256) {" // 7
                                              + "\n	   k = \"\" + (char)tmp;" // 8
                                              + "\n	   if (w.equals(\"\")) {" // 9
                                              + "\n	     w = k;" // 10
                                              + "\n	     continue;" // 11
                                              + "\n	   }" // 12
                                              + "\n	   else if (!dict.contains(w + k)) {" // 13
                                              + "\n	     result += w;" // 14
                                              + "\n	     dict.put(dicCnt, w + k);" // 15
                                              + "\n        dicCnt++;" // 16
                                              + "\n        w = k;" // 17
                                              + "\n      }" // 18
                                              + "\n    }" // 19
                                              + "\n    else {  // dict entry" // 20
                                              + "\n       result += w;" // 21
                                              + "\n       String add = dict.get(tmp);" // 22
                                              + "\n       dict.put(dicCnt, w + add.charAt(0));" // 23
                                              + "\n       dicCnt++;" // 24
                                              + "\n       w = add;" // 25
                                              + "\n    }" // 26
                                              + "\n  }" // 27
                                              + "\n  result += k;" // 28
                                              + "\n }";             // 29

  public LZWDecoding() {
    // nothing to be done
  }

  /**
   * Decode the LZW encoded input.
   * 
   * @param text
   * @throws LineNotExistsException
   */
  public void decode(String[] text) throws LineNotExistsException {
    // trim input to maximum length
    // String ein = "";
    String[] t = new String[Math.min(text.length, inputLimit)];
    for (int i = 0; i < t.length; i++) {
      t[i] = text[i];
      // ein += text[i];
    }
    // text = t;

    // Extract the input
    String input = "";
    for (int i = 0; i < t.length; i++) {
      input += t[i];
    }
    String chiffre = code(input);
    StringTokenizer initialTokenizer = new StringTokenizer(chiffre);
    int tokens = initialTokenizer.countTokens();
    int[] chiffreArray = new int[tokens];
    for (int i = 0; i < tokens; i++) {
      chiffreArray[i] = Integer.parseInt(initialTokenizer.nextToken());
    }

    // topic
    Text topic = lang.newText(new Coordinates(20, 50), "LZW Decoding", "Topic",
        null, tptopic);

    lang.newRect(new Offset(-5, -5, topic, "NW"),
        new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

    lang.nextStep();
    Text in1 = lang.newText(new Offset(0, 50, topic, "SW"),
        "Die LZW-Kodierung von  " + input + " ergibt: " + code(input), "in",
        null, tpsteps);

    lang.nextStep();
    Text algoinWords = lang.newText(new Offset(0, 50, in1, "SW"),
        "Der Algorithmus in Worten", "inWords", null, tpwords);

    lang.nextStep();

    Text step0 = lang
        .newText(
            new Offset(0, 30, algoinWords, "SW"),
            "0) Der Algorithmus bildet sich während der Laufzeit sein eigenes Wörterbuch. Einzelne Zeichen",
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
        .newText(
            new Offset(0, 40, step02, "SW"),
            "1) Lese den Eingabestring zahlenweise beginnend an der zweiten Zahl ein. Sei z die eingelesene Zahl.",
            "line1", null, tpsteps);
    lang.nextStep();
    Text step2 = lang
        .newText(
            new Offset(0, 40, step1, "SW"),
            "2) Ist z im Wörterbuch vorhanden, füge die Kodierung des Präfixes von z konkateniert mit dem",
            "line2", null, tpsteps);
    Text step21 = lang
        .newText(
            new Offset(0, 20, step2, "SW"),
            "   dem ersten Buchstaben der Kodierung von z dem Wörterbuch an der nächsten freien Stelle hinzu.",
            "line2", null, tpsteps);
    lang.nextStep();
    Text step3 = lang
        .newText(
            new Offset(0, 40, step21, "SW"),
            "3) Ansonsten erweitere die Ausgabe um die Kodierung des Präfixes von z, setzte das Präfix auf z, und fahre bei der",
            "line3", null, tpsteps);
    Text step31 = lang.newText(new Offset(0, 20, step3, "SW"),
        "   nächsten Zahl fort.", "line31", null, tpsteps);
    lang.nextStep();

    algoinWords.hide();
    step0.hide();
    step01.hide();
    step02.hide();
    step1.hide();
    step2.hide();
    step21.hide();
    step3.hide();
    step31.hide();

    lang.nextStep();

    in1.setText("Wir wollen " + chiffre + " nun dekodieren.", null, null);

    lang.nextStep();

    in1.hide();

    IntArray intArray = lang.newIntArray(new Offset(0, 25, in1, "SW"),
        chiffreArray, "stringArray", null, ap);

    ArrayMarker am = lang.newArrayMarker(intArray, 0, "i", null, amp);

    lang.nextStep();

    sc = lang.newSourceCode(new Offset(0, 25, intArray, "SW"), "codeName",
        null, scp);
    sc.addCodeLine("public static void decode(String input) {", null, 0, null);
    sc.addCodeLine("StringTokenizer t = new StringTokenizer(input);", null, 1,
        null);
    sc.addCodeLine("int cnt = t.countTokens();", null, 1, null);
    sc.addCodeLine("String w, k, result;", null, 1, null);
    sc.addCodeLine("int dicCnt = 256;", null, 1, null);
    sc.addCodeLine("for (int i = 0; i < cnt; i++) {;", null, 1, null);
    sc.addCodeLine("int tmp = Integer.parseInt(t.nextToken());", null, 2, null);
    sc.addCodeLine("if (tmp < 256) {", null, 2, null);
    sc.addCodeLine("k = (char)tmp;", null, 3, null);
    sc.addCodeLine("if (w.length() == 0) {", null, 3, null);
    sc.addCodeLine("w = k;", null, 4, null);
    sc.addCodeLine("continue;", null, 4, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("else if (!dict.contains(w + k)) {", null, 3, null);
    sc.addCodeLine("result += w;", null, 4, null);
    sc.addCodeLine("dict.put(dicCnt, w + k);", null, 4, null);
    sc.addCodeLine("dicCnt++;", null, 4, null);
    sc.addCodeLine("w = k;", null, 4, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("else {  // dict entry -> greater than 255", null, 2, null);
    sc.addCodeLine("result += w;", null, 3, null);
    sc.addCodeLine("String add = dict.get(tmp);", null, 3, null);
    sc.addCodeLine("dict.put(dicCnt, w + add.charAt(0));", null, 3, null);
    sc.addCodeLine("dicCnt++;", null, 3, null);
    sc.addCodeLine("w = add;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("result += k;", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    lang.nextStep();

    Text wLabel = lang.newText(new Offset(0, 25, sc, "SW"), "w: ", "w", null,
        tpsteps);
    Text kLabel = lang.newText(new Offset(0, 20, wLabel, "SW"), "k:  ", "k",
        null, tpsteps);
    Text dicLabel = lang.newText(new Offset(0, 25, kLabel, "SW"), "dicCnt: ",
        "dicCnt", null, tpsteps);
    Text ausgabeLabel = lang.newText(new Offset(0, 25, dicLabel, "SW"),
        "Ausgabe: ", "ausgabeLabel", null, tpsteps);
    ausgabeLabel.changeColor(null, Color.RED, null, null);
    Text ausgabe = lang.newText(new Offset(15, -4, ausgabeLabel, "SE"), "",
        "ausgabe", null, tpsteps);
    ausgabe.changeColor(null, Color.BLUE, null, null);

    // algorithm
    sc.highlight(0, 0, false);

    lang.nextStep();
    sc.toggleHighlight(0, 0, false, 1, 0);
    StringTokenizer to = new StringTokenizer(chiffre); // 1

    lang.nextStep();
    sc.toggleHighlight(1, 0, false, 2, 0);
    int cnt = to.countTokens(); // 2

    lang.nextStep();
    sc.toggleHighlight(2, 0, false, 3, 0);
    String w = ""; // 3
    String k = "";// 3
    String result = ""; // 3

    lang.nextStep();
    sc.toggleHighlight(3, 0, false, 4, 0);
    int dicCnt = 256; // 4
    dicLabel.setText("dicCnt: " + dicCnt, null, null);

    // setup dicionary, this time the number is the key and the letters are the
    // value

    // setup the dictonary matrix
    String[][] dictData = new String[inputLimit][2];
    for (int i = 0; i < inputLimit; i++) {
      dictData[i][0] = "";
      dictData[i][1] = "";
    }
    StringMatrix dic = lang.newStringMatrix(
        new Offset(200, -50, intArray, "E"), dictData, "dict", null, mp);
    int matrixCount = 0;

    Hashtable<Integer, String> dict = new Hashtable<Integer, String>();
    for (int i = 0; i < 256; i++) {
      dict.put(i, "" + ((char) i));
    }

    lang.nextStep();
    sc.toggleHighlight(4, 0, false, 5, 0);
    for (int i = 0; i < cnt - 1; i++, sc.highlight(5, 0, false), am.move(i,
        null, null), intArray.highlightCell(i, null, null)) { // 5

      if (i > 0)
        intArray.unhighlightCell(i - 1, null, null);
      lang.nextStep();
      sc.unhighlight(5, 0, false);
      sc.highlight(6, 0, false);
      int tmp = Integer.parseInt(to.nextToken()); // 6

      lang.nextStep();
      sc.toggleHighlight(6, 0, false, 7, 0);
      if (tmp < 256) { // 7

        lang.nextStep();
        sc.unhighlight(7, 0, false);
        sc.highlight(8, 0, false);
        k = "" + (char) tmp; // 8
        kLabel.setText("k: " + k, null, null);

        lang.nextStep();
        sc.toggleHighlight(8, 0, false, 9, 0);
        if (w.equals("")) { // 9

          lang.nextStep();
          sc.toggleHighlight(9, 0, false, 10, 0);
          w = k; // 10
          wLabel.setText("w: " + w, null, null);

          lang.nextStep();
          sc.toggleHighlight(10, 0, false, 11, 0);
          lang.nextStep();
          sc.toggleHighlight(11, 0, false, 5, 0);

          continue; // 11
        } // 12
        else if (!dict.contains(w + k)) { // 13
          lang.nextStep();
          sc.toggleHighlight(9, 0, false, 13, 0);

          lang.nextStep();
          sc.toggleHighlight(13, 0, false, 14, 0);
          result += w; // 14
          ausgabe.setText(result, null, null);

          lang.nextStep();
          sc.toggleHighlight(14, 0, false, 15, 0);
          dict.put(dicCnt, w + k); // 15
          dic.put(matrixCount, 0, "" + (matrixCount + 256), null, null);
          dic.put(matrixCount, 1, w + k, null, null);
          matrixCount++;

          lang.nextStep();
          sc.toggleHighlight(15, 0, false, 16, 0);
          dicCnt++; // 16
          dicLabel.setText("dicCnt: " + dicCnt, null, null);

          lang.nextStep();
          sc.toggleHighlight(16, 0, false, 17, 0);
          w = k; // 17
          wLabel.setText("w: " + w, null, null);

          lang.nextStep();
          sc.unhighlight(17, 0, false);
        } // 18
      } // 19
      else { // dict entry // 20
        lang.nextStep();
        sc.toggleHighlight(7, 0, false, 20, 0);

        lang.nextStep();
        sc.toggleHighlight(20, 0, false, 21, 0);
        result += w; // 21
        ausgabe.setText(result, null, null);

        lang.nextStep();
        sc.toggleHighlight(21, 0, false, 22, 0);
        String add = dict.get(tmp); // 22

        lang.nextStep();
        sc.toggleHighlight(22, 0, false, 23, 0);
        dict.put(dicCnt, w + add.charAt(0)); // 23
        dic.put(matrixCount, 0, "" + (matrixCount + 256), null, null);
        dic.put(matrixCount, 1, w + add.charAt(0), null, null);
        matrixCount++;

        lang.nextStep();
        sc.toggleHighlight(23, 0, false, 24, 0);
        dicCnt++; // 24
        dicLabel.setText("dicCnt: " + dicCnt, null, null);

        lang.nextStep();
        sc.toggleHighlight(24, 0, false, 25, 0);
        w = add; // 25
        wLabel.setText("w: " + w, null, null);

        lang.nextStep();
        sc.unhighlight(25, 0, false);
      } // 26
    } // 27

    lang.nextStep();
    sc.unhighlight(25, 0, false);
    sc.unhighlight(5, 0, false);
    sc.highlight(28, 0, false);
    result += k; // 28
    ausgabe.setText(result, null, null);

    Text fazit1 = lang.newText(new Offset(0, 90, ausgabeLabel, "SW"),
        "Die Ausgabe entspricht genau der anfänglichen Eingabe. Das", "name",
        null, tpsteps);

    lang.newText(new Offset(0, 20, fazit1, "SW"),
        "Wörterbuch konnte neu generiert werden.", "fazit", null, tpsteps);

  } // 29

  /**
   * Endode the input by LZW.
   * 
   * @param input
   * @return the String
   */
  public static String code(String input) {
    String w = "";
    String k = "";
    String result = "";
    int cnt = 256;
    // setup dicionary
    Hashtable<String, Integer> dict = new Hashtable<String, Integer>();
    for (int i = 0; i < 256; i++) {
      dict.put("" + ((char) i), i);
    }

    for (int i = 0; i < input.length(); i++) {
      k = "" + input.charAt(i);
      if (dict.containsKey(w + k))
        w = w + k;
      else {
        result += dict.get(w) + " ";
        dict.put(w + k, cnt);
        cnt++;
        w = k;
      }
    }
    result += dict.get(k);

    return result;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    String[] strArray = (String[]) primitives.get("stringArray");
    try {
      decode(strArray);
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
    return "LZW Dekomprimierung";
  }

  @Override
  public String getAlgorithmName() {
    return "LZW (Lempel, Ziv, Welch 1984)";
  }

  @Override
  public void init() {
    lang = new AnimalScript("LZW Decoding", "Florian Lindner", 1152, 768);
    lang.setStepMode(true);
  }
}
