package generators.compression;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class BurrowsWheelerTransform extends CompressionAlgorithm implements
    Generator {

  public static int        IndexOut;

  private static final int inputLimit = 12;

  public BurrowsWheelerTransform() {
    // nothing to be done here
  }

  private static final String DESCRIPTION = 
    "Die Burrows Wheeler Transformation dient der Vorbereitung eines Kompressionsverfahrens."
    + " Der Eingabetext wird bei der Transformation nicht komprimiert, da die"
    + " Buchstaben und Zeichen alle erhalten bleiben. Hingegen wird die Reihenfolge so ver&auml;ndert,"
    + " dass ein Ausgabestring erzeugt wird, bei dem Buchstaben h&auml;ufig aufeinanderfolgen. So"
    + " dient die Transformation als eine gute Vorbereitung z.B. f&uuml;r eine Laufl&auml;ngenkodierung.";

  private static final String SOURCE_CODE = "Der Algorithmus wird in einer Animation demonstriert.\nUm die grafische Animation in voller Größe darstellen"
    + " zu k&ouml;nnen, wird die Eingabe auf 12 Buchstaben begrenzt.";

  /**
   * Transforms the input.
   * 
   * @param text
   */
  public void compress(String[] text) { // throws NotEnoughNodesException {

    // trim input to maximum length
//    String ein = "";
    String[] t = new String[Math.min(text.length, inputLimit)];
    for (int i = 0; i < t.length; i++) {
      t[i] = text[i];
//      ein += text[i];
    }
    // text = t;

    // topic
    Text topic = lang.newText(new Coordinates(20, 50),
        "Burrows Wheeler Transformation", "Topic", null, tptopic);
    lang.newRect(new Offset(-5, -5, topic, "NW"),
        new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

    // Algo steps
    lang.nextStep();
    Text step1 = lang.newText(new Coordinates(20, 100),
        "1) Liste alle Rotationsmöglichkeiten des Eingabe-Strings auf.",
        "line1", null, tpsteps);
    lang.nextStep();
    Text step2 = lang.newText(new Offset(0, 30, step1, "SW"),
        "2) Sortiere die entstandene Liste alphabetisch.", "line2", null,
        tpsteps);
    lang.nextStep();
    Text step3 = lang
        .newText(
            new Offset(0, 30, step2, "SW"),
            "3)  Der letzte Buchstabe jedes Rotations-Strings wird dem Ergebnis hinzugefügt.",
            "line3", null, tpsteps);

    lang.nextStep();

    // highlight step1
    step1.changeColor(null, Color.RED, null, null);

    // show the rotated string arrays

    // input array
    StringArray strArray = lang.newStringArray(new Offset(0, 40, step3, "SW"),
        t, "stringArray", null, ap);
    StringArray first = strArray;
    // fill a vector with all rotations
    Vector<String[]> rotations = new Vector<String[]>(0, 1);
    String[] tmp = t;
    for (int i = 0; i < t.length; i++) {
      lang.nextStep();
      rotations.add(rotateLeft(tmp));
      tmp = rotateLeft(tmp);
      if (i != t.length - 1) {
        strArray = lang.newStringArray(new Offset(0, 10, strArray, "SW"), tmp,
            "stringArray", null, ap);
      }
    }

    StringArray last = strArray;

    // sort them alphaetically
    step1.changeColor(null, Color.BLACK, null, null);
    step2.changeColor(null, Color.RED, null, null);

    Vector<String[]> sorted = new Vector<String[]>(0, 1);
    Vector<StringArray> strArrays = new Vector<StringArray>();
    String[] early = rotations.elementAt(0);
    // used to get an offset lateron
    strArray = lang.newStringArray(new Offset(50, -36, first, "NE"), tmp,
        "stringArray", null, ap);
    strArray.hide();
    while (!rotations.isEmpty()) {
      for (int i = 0; i < rotations.size(); i++) {
        if (isEarlier(rotations.elementAt(i), early)) {
          early = rotations.elementAt(i);
        }
      }
      sorted.add(early);
      strArray = lang.newStringArray(new Offset(0, 10, strArray, "SW"), early,
          "stringArray", null, ap);
      strArrays.add(strArray);
      lang.nextStep();
      rotations.removeElement(early);
      if (!rotations.isEmpty())
        early = rotations.elementAt(0);
    }
    step2.changeColor(null, Color.BLACK, null, null);
    step3.changeColor(null, Color.RED, null, null);

    // highlight last cells
    for (int i = 0; i < strArrays.size(); i++) {
      strArrays.elementAt(i).highlightCell(
          strArrays.elementAt(i).getLength() - 1, null, null);
    }

    // get the index for the output row
    for (int i = 0; i < sorted.size(); i++) {
      boolean equal = true;
      for (int j = 0; j < t.length; j++) {
        if (t[j] != sorted.elementAt(i)[j]) {
          equal = false;
          break;
        }
      }
      if (equal) {
        IndexOut = i;
        break;
      }
    }

    // take the last letters in a row
    String result = "";
    for (int i = 0; i < sorted.size(); i++) {
      result += sorted.elementAt(i)[sorted.elementAt(i).length - 1];
    }

    // show the result and explain it
    Text fazit1 = lang.newText(new Offset(0, 50, last, "SW"),
        "Daraus ergibt sich die Ausgabe:  ", "fazit", null, tpsteps);
    tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    lang.newText(new Offset(15, 8, fazit1, "E"), result, "fazit1", null,
        tpsteps);
    tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    Text fazit2 = lang
        .newText(
            new Offset(0, 15, fazit1, "SW"),
            "Durch die erfolgte Transformation folgen mehrere Buchstaben aufeinander.",
            "fazit2", null, tpsteps);
    Text fazit3 = lang
        .newText(
            new Offset(0, 15, fazit2, "SW"),
            "Darauf können nun andere Kompressionsverfahren, wie die Lauflängenkodierung,",
            "fazit3", null, tpsteps);
    Text fazit4 = lang.newText(new Offset(0, 15, fazit3, "SW"),
        "vorgenommen werden, wodurch eine bessere Kompression erreicht wird.",
        "fazit4", null, tpsteps);
    Text fazit5 = lang.newText(new Offset(0, 15, fazit4, "SW"),
        "Für eine Dekodierung wird desweiteren die Zeile der Rotationsmatrix",
        "fazit5", null, tpsteps);
    Text fazit6 = lang.newText(new Offset(0, 15, fazit5, "SW"),
        "ausgeben, in der sich die Eingabe befindet:", "fazit5", null, tpsteps);
    tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    lang.newText(new Offset(15, 8, fazit6, "E"), "" + IndexOut, "fazit6", null,
        tpsteps);
    tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  /**
   * Rotates a string array one position to the left side
   * 
   * @param text
   *          The text string array
   */
  public static String[] rotateLeft(String[] text) {
    String[] tmp = new String[text.length];
    for (int i = 0; i < text.length - 1; i++) {
      tmp[i] = text[i + 1];
    }
    tmp[text.length - 1] = text[0];
    return tmp;
  }

  /**
   * Tests whether text1 is alphabetically smaller then text2
   * 
   * @param text1
   * @param text2
   */
  public static boolean isEarlier(String[] text1, String[] text2) {
    int first;
    int second;
    for (int i = 0; i < text1.length; i++) {
      first = (new Integer(text1[i].charAt(0))).intValue();
      second = (new Integer(text2[i].charAt(0))).intValue();
      if (text1[i].equals("."))
        first = Integer.MAX_VALUE;
      if (text2[i].equals("."))
        second = Integer.MAX_VALUE;

      if (first < second)
        return true;
      if (first > second)
        return false;
    }
    return false;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    String[] strArrayIn = (String[]) primitives.get("stringArray");
    // try {
    compress(strArrayIn);
    // } catch (NotEnoughNodesException e) {
    // e.printStackTrace();
    // }
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
    return "Burrows-Wheeler Transformation";
  }

  public String getAlgorithmName() {
    return "Burrows-Wheeler Transformation";
  }

  public void init() {
    lang = new AnimalScript("Burrows Wheeler Transformation",
        "Florian Lindner", 800, 800);
    lang.setStepMode(true);
  }

}
