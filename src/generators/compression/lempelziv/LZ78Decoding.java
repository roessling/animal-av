package generators.compression.lempelziv;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class LZ78Decoding extends CompressionAlgorithm implements Generator {

  private static final int    inputLimit  = 26;

  private static final String DESCRIPTION = "Die LZ78 Dekodierung ist die Umkehrung der gleichnamigen Kodierung. Das Wörterbuch, welches"
                                              + " in der Kodierung entstanden ist, kann dabei wieder generiert werden.";

  private static final String SOURCE_CODE = "Der Algorithmus wird in einer Animation demonstriert. Um die grafische Animation in voller Größe darstellen"
                                              + " zu können, wird die Eingabe auf 26 Buchstaben begrenzt. Es handelt sich hier um einen Dekodierungsalgorithmus. Ihre Eingabe wird zunächst durch die"
                                              + " entsprechende Kodierung kodiert. Erst diese Daten werden dekodiert.";

  public LZ78Decoding() {
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
    Text topic = lang.newText(new Coordinates(20, 50), "LZ78 Decoding",
        "Topic", null, tptopic);

    lang.newRect(new Offset(-5, -5, topic, "NW"),
        new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

    // extract the input
    String input = "";
    for (int i = 0; i < t.length; i++) {
      input += t[i];
    }

    // Algo steps
    lang.nextStep();
    Text step1 = lang.newText(new Offset(0, 40, topic, "SW"),
        "Die Eingabe ist:  ", "line1", null, tpsteps);
    Text step12 = lang.newText(new Offset(10, -5, step1, "SE"), input, "line1",
        null, tpsteps);
    step12.changeColor(null, Color.RED, null, null);
    lang.nextStep();
    Text step2 = lang.newText(new Offset(0, 30, step1, "SW"),
        "Durch die LZ78 Kodierung erhalten wir:  ", "line1", null, tpsteps);
    String in = LZ78code(t);
    Text step22 = lang.newText(new Offset(10, -5, step2, "SE"), in, "line1",
        null, tpsteps);
    step12.changeColor(null, Color.BLACK, null, null);
    step22.changeColor(null, Color.RED, null, null);
    Text step3 = lang
        .newText(new Offset(0, 30, step2, "SW"),
            "Wir wollen die Ausgabe nun wieder dekodieren.", "line1", null,
            tpsteps);
    lang.nextStep();

    Text step4 = lang.newText(new Offset(10, 40, step3, "SW"),
        "- Lese die Eingabezahlen einzeln ein. ", "line2", null, tpsteps);
    Text step41 = lang
        .newText(
            new Offset(0, 20, step4, "SW"),
            "- Erweitere die Ausgabe um die Buchstaben aus dem Wörterbuch, die dieser Zahl entsprechen.",
            "line2", null, tpsteps);
    Text step42 = lang
        .newText(
            new Offset(0, 20, step41, "SW"),
            "- Füge dem Wörterbuch an der nächsten freien Stelle einen neuen Eintrag hinzu, welcher aus",
            "line2", null, tpsteps);
    Text step43 = lang
        .newText(
            new Offset(0, 20, step42, "SW"),
            "  dem zuvor gelesenen Eintrag erweitert um den ersten Buchstaben des eben gelesenen",
            "line2", null, tpsteps);
    Text step44 = lang.newText(new Offset(0, 20, step43, "SW"),
        "  Teils besteht.", "line2", null, tpsteps);
    lang.nextStep();

    step4.hide();
    step41.hide();
    step42.hide();
    step43.hide();
    step44.hide();

    String[][] dictData = new String[inputLimit + 4][2];

    for (int i = 0; i < dictData.length; i++) {
      dictData[i][0] = "  ";
      dictData[i][1] = "  ";
    }
    dictData[0][0] = "0";
    dictData[0][1] = "EOF";
    dictData[1][0] = "1";
    dictData[1][1] = "A";
    dictData[2][0] = "...";
    dictData[2][1] = "...";
    dictData[3][0] = "26";
    dictData[3][1] = "Z";
    int matrixCounter = 4;

    StringTokenizer to = new StringTokenizer(in);
    int cnt = to.countTokens();
    String eingabeData[] = new String[cnt];

    for (int i = 0; i < cnt; i++) {
      eingabeData[i] = to.nextToken();
    }
    StringArray eingabe = lang.newStringArray(new Offset(0, 70, step3, "SW"),
        eingabeData, "eingabe", null, ap);
    ArrayMarker am = lang.newArrayMarker(eingabe, 0, "am", null, amp);
    eingabe.highlightCell(0, null, null);
    StringMatrix dictMatrix = lang.newStringMatrix(new Offset(175, 25, step3,
        "NE"), dictData, "dict", null, mp);
    String result = "";
    Text ausgabeLabel = lang.newText(new Offset(0, 70, eingabe, "SW"),
        "Ausgabe:", "ausgabe", null, tpsteps);
    Text ausgabe = lang.newText(new Offset(15, -5, ausgabeLabel, "SE"), result,
        "ausgabe", null, tpsteps);
    ausgabe.changeColor(null, Color.BLUE, null, null);

    // algo

    // fill the initial dictionary
    Vector<String> dict = new Vector<String>(0, 1);
    dict.add("EOF"); // 0
    for (int i = 65; i < 91; i++) {
      dict.add("" + (char) i);
    }

    StringTokenizer t2 = new StringTokenizer(in);
    String last = "";

    for (int i = 0; i < cnt; i++) {
      if (i > 0) {
        eingabe.unhighlightCell(i - 1, null, null);
        am.move(i, null, null);
        eingabe.highlightCell(i, null, null);
      }
      String tmp = t2.nextToken();
      result += dict.elementAt(Integer.parseInt(tmp));
      lang.nextStep();
      ausgabe.setText(result, null, null);

      if (i > 0) {
        dict.add(dict.elementAt(Integer.parseInt(last))
            + dict.elementAt(Integer.parseInt(tmp)).charAt(0));
        dictMatrix.put(matrixCounter, 0, "" + (matrixCounter + 23), null, null);
        dictMatrix.put(matrixCounter, 1, dict.elementAt(Integer.parseInt(last))
            + dict.elementAt(Integer.parseInt(tmp)).charAt(0), null, null);
        matrixCounter++;
        lang.nextStep();
      }

      last = tmp;
    }

    lang.newText(new Offset(0, 60, ausgabeLabel, "SW"),
        "Die Ausgabe entspricht genau der Eingabe.", "Ausgabe", null, tpsteps);
  }

  public static String LZ78code(String[] text) {
    // extract the input
    String input = "";
    for (int i = 0; i < text.length; i++) {
      input += text[i];
    }
    input = input.toUpperCase();

    // fill the initial dictionary
    Vector<String> dict = new Vector<String>(0, 1);
    dict.add("EOF"); // 0
    for (int i = 65; i < 91; i++) {
      dict.add("" + (char) i);
    }

    String result = "";
    for (int i = 0; i < input.length(); i++) {
      String tmp = "" + input.charAt(i);
      while (dict.contains(tmp) && i + 1 < input.length()) {
        if (dict.contains(tmp + input.charAt(i + 1))) {
          tmp += input.charAt(i + 1);
          i++;
        } else {
          dict.add(tmp + input.charAt(i + 1));
          break;
        }
      }
      result += dict.indexOf(tmp) + " ";
    }
    return result;
  }

  public static String getSOURCE_CODE() {
    return SOURCE_CODE;
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getName() {
    return "LZ78 Dekomprimierung";
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

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
  }

  @Override
  public String getAlgorithmName() {
    return "LZ78 (Lempel, Ziv 1978)";
  }

  @Override
  public void init() {
    lang = new AnimalScript("LZ78 Decoding", "Florian Lindner", 800, 600);
    lang.setStepMode(true);
  }
}
