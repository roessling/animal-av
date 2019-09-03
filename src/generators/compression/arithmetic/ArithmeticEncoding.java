package generators.compression.arithmetic;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class ArithmeticEncoding extends CompressionAlgorithm implements
    Generator {

  /**
   * Represents one range of the interval.
   * 
   * @author Florian Lindner
   * 
   */
  public static class Range {

    protected char  letter;

    protected float start;

    protected float end;

    protected float frequency;

    public Range(char letterIn, float startIn, float endIn, float frequencyIn) {

      this.letter = letterIn;

      this.start = startIn;

      this.end = endIn;

      this.frequency = frequencyIn;
    }

    public float getEnd() {
      return end;
    }

    public char getLetter() {
      return letter;
    }

    public float getStart() {
      return start;
    }

    public float getFrequency() {
      return frequency;
    }
  }

  private float               startSubInterval;

  private float               endSubInterval;

  /**
   * Saves the Rects seauentially to be ablbe to mofify them lateron.
   */
  private static Vector<Rect> rects;

  private static final int    inputLimit  = 6;

  private static final String DESCRIPTION = 
    "Die arithmetische Kodierung ist ein verlustfreies Kompressionsverfahren."
    + " Der Eingabestring wird in eine Gleitkommazahl umgewandelt, wobei"
    + " die Einteilung der Gleitkommazahlenintervalle durch eine H&auml;ufigkeitsverteilung"
    + " erfolgt.";

  private static final String SOURCE_CODE = 
    "Der Algorithmus wird in einer Animation demonstriert.\nUm die grafische Animation in voller Gr&ouml;&szlig;e darstellen"
    + " zu k&ouml;nnen, wird die Eingabe auf 6 Buchstaben begrenzt.";

  public ArithmeticEncoding() {
    // Nothing to be done here
  }

  public ArithmeticEncoding(Language language) {
  }

  /**
   * encode the input
   * 
   * @param text
   */
  public void compress(String[] text) {

    // trim input to maximum length
//    String ein = "";
    String[] t = new String[Math.min(text.length, inputLimit)];
    for (int i = 0; i < t.length; i++) {
      t[i] = text[i];
//      ein += text[i];
    }
    // text = t;

    // Extract the input to a string
    String input = "";
    for (int i = 0; i < Math.min(inputLimit, t.length); i++) {
      input += t[i];
    }

    // topic
    Text topic = lang.newText(new Coordinates(20, 50), "Arithmetic Encoding",
        "Topic", null, tptopic);
    lang.newRect(new Offset(-5, -5, topic, "NW"),
        new Offset(10, 0, topic, "SE"), "topicRect", null, rctp);

    // Algo in words
    lang.nextStep();
    Text algoinWords = lang.newText(new Offset(0, 35, topic, "SW"),
        "Der Algorithmus in Worten", "inWords", null, tpwords);

    // Algo steps descrption
    lang.nextStep();
    Text step1 = lang.newText(new Offset(0, 100, topic, "SW"),
        "1) Berechne die absoluten Häufigkeiten jedes Buchstabens.", "line1",
        null, tpsteps);
    lang.nextStep();
    Text step2 = lang
        .newText(
            new Offset(0, 30, step1, "SW"),
            "2) Ordne jedem Buchstaben ein Teilintervall zwischen 0 und 1 zu, abhängig von seiner Häufigkeit.",
            "line2", null, tpsteps);
    lang.nextStep();
    Text step3 = lang
        .newText(
            new Offset(0, 30, step2, "SW"),
            "3)  Um einen String zu kodieren, iteriere buchstabenweise über den String und bestimme sukzessive ein",
            "line3", null, tpsteps);
    Text step31 = lang
        .newText(
            new Offset(0, 30, step3, "SW"),
            "      neues Intervall. Das Subintervall, das dem nächsten Zeichen der Eingabe entspricht, wird zum",
            "line31", null, tpsteps);
    Text step32 = lang
        .newText(
            new Offset(0, 30, step31, "SW"),
            "      aktuellen Intervall, welches im nächsten Schritt unterteilt wird.",
            "line32", null, tpsteps);
    Text step4 = lang
        .newText(
            new Offset(0, 30, step32, "SW"),
            "4) Sind noch weitere Zeichen zu kodieren, dann fahr fort bei Schritt 2.",
            "line4", null, tpsteps);
    lang.nextStep();
    Text step5 = lang
        .newText(
            new Offset(0, 30, step4, "SW"),
            "5) Eine beliebige Zahl innerhalb des letzten Intervalls stellt das Ergebnis dar.",
            "line4", null, tpsteps);
    lang.nextStep();

    algoinWords.setText("Eingabe:", null, null);
    Text eingabe = lang.newText(new Offset(-120, -5, algoinWords, "SE"), input,
        "eingabe", null, tpsteps);
    eingabe.changeColor(null, Color.RED, null, null);

    // highlight step1
    step1.changeColor(null, Color.RED, null, null);
    step2.hide();
    step3.hide();
    step3.hide();
    step31.hide();
    step32.hide();
    step4.hide();
    step5.hide();

    // algorithm
    rects = new Vector<Rect>();

    // initialize interval
    startSubInterval = 0;
    endSubInterval = 1;

    // set up frequencys
    float[] letters = new float[256];
    for (int i = 0; i < t.length; i++) {
      letters[new Integer(t[i].charAt(0))]++;
    }
    float[] frequency = new float[256];
    for (int i = 0; i < letters.length; i++) {
      frequency[i] = letters[i] / t.length;
    }

    // put them sequentially into a vector cotaining their ranges and the
    // frequencies
    float big = 0;
    int index = 0;
    Vector<Range> ranges = new Vector<Range>(0, 1);

    for (int i = 0; i < t.length; i++) {
      for (int j = 0; j < frequency.length; j++) {
        if (frequency[j] > big) {
          big = frequency[j];
          index = j;
        }
      }
      if (!ranges.isEmpty() && big > 0)
        ranges.add(new Range((char) index, ranges.lastElement().getEnd(),
            ranges.lastElement().getEnd() + big, letters[index]));
      else if (big > 0)
        ranges.add(new Range((char) index, 0, big, letters[index]));
      frequency[index] = -1;
      big = 0;
      index = 0;
    }

    // step1: print the matrix of the frequencys on the left side
    String[][] freqPrint = new String[ranges.size()][2];
    for (int i = 0; i < ranges.size(); i++) {
      freqPrint[i][0] = ranges.elementAt(i).getLetter() + ": ";
      freqPrint[i][1] = new Integer(new Float(ranges.elementAt(i)
          .getFrequency()).intValue()).toString();

    }
    StringMatrix strMatrix = lang.newStringMatrix(
        new Offset(0, 30, step1, "SW"), freqPrint, "matrix", null, mp);
    lang.nextStep();

    // step2,3: show description and the intervals
    strMatrix.hide();
    step1.setText(step2.getText(), null, null);
    step2.setText(step3.getText(), null, null);
    step3.setText(step31.getText(), null, null);
    step31.setText(step32.getText(), null, null);
    step32.setText("Aktueller Buchstabe:  ", null, null);
    step32.changeColor(null, Color.BLUE, null, null);
    step2.changeColor(null, Color.RED, null, null);
    step3.changeColor(null, Color.RED, null, null);
    step31.changeColor(null, Color.RED, null, null);
    step2.show();
    step3.show();
    step31.show();

    step32.show();

    lang.nextStep();

    /*
     * print initial interval, actualRect represents the last printed left sided
     * Rect of the interval to get an Offset relation
     */

    TextProperties tpend = new TextProperties();
    tpend.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    tpend.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 10));

    Rect actualRect = null;

    // print the initial interval (0 <-----> 1)
    for (int i = 0; i < ranges.size(); i++) {
      if (i == 0)
        actualRect = this.printRange(ranges.elementAt(i), step31);
      else
        this.printRange(ranges.elementAt(i), step31);
      // special case for the last rectangle because the of the last index print
      if (i == ranges.size() - 1) {
        lang.newText(new Offset(685, 10, actualRect, "SW"), new Float(
            this.endSubInterval).toString(), "text", null, tpend);
      }
    }
    lang.nextStep();

    // encode
    float foo;
    char tmp;
    for (int i = 0; i < t.length; i++) {
      tmp = t[i].charAt(0);
      step32.setText("Aktueller Buchstabe:  " + tmp + " aus " + input
          + " an Stelle " + (i + 1), null, null);

      // highlight the cell in the LAST row
      RectProperties rctpHighlight = new RectProperties();
      rctpHighlight.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      rctpHighlight.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
      rctpHighlight.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
      rctpHighlight.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

      for (int j = 0; j < ranges.size(); j++) {
        if ((ranges.elementAt(j).getLetter()) == tmp) {
          lang.newRect(rects.elementAt(j).getUpperLeft(), rects.elementAt(j)
              .getLowerRight(), "rect", null, rctpHighlight);
          rects.elementAt(j).hide();
          rects = new Vector<Rect>();
          break;
        }
      }

      lang.nextStep();

      for (int j = 0; j < ranges.size(); j++) {
        if (tmp == ranges.elementAt(j).getLetter()) {
          foo = startSubInterval + (endSubInterval - startSubInterval)
              * ranges.elementAt(j).getStart();
          endSubInterval = foo + (endSubInterval - startSubInterval)
              * (ranges.elementAt(j).getEnd() - ranges.elementAt(j).getStart());
          startSubInterval = foo;
        }
      }
      // print the actual subinterval
      Rect tmpRect = actualRect;
      for (int k = 0; k < ranges.size(); k++) {
        if (k == 0)
          actualRect = this.printRange(ranges.elementAt(k), actualRect);
        else
          this.printRange(ranges.elementAt(k), tmpRect);
        // print the last Nodevalue on the right side
        if (k == ranges.size() - 1) {
          lang.newText(new Offset(685, 10, actualRect, "SW"), new Float(
              this.endSubInterval).toString(), "text", null, tpend);
        }

      }
      lang.nextStep();
    }

    tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    tpsteps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));
    Text fazit = lang
        .newText(
            new Offset(0, 50, actualRect, "SW"),
            "Das letzte Subintervall stellt den Ergebnis-Bereich dar. Um nun die Eingabe zu",
            "text", null, tpsteps);
    Text fazit2 = lang
        .newText(
            new Offset(0, 20, fazit, "SW"),
            "kodieren, wird eine beliebige Zahl in diesem Intervall gewühlt, die sich mit",
            "text2", null, tpsteps);
    Text fazit3 = lang.newText(new Offset(0, 20, fazit2, "SW"),
        "möglichst wenigen Bits darstellen lässt. Beispielsweise: ", "text3",
        null, tpsteps);
    Text fazit4 = lang.newText(new Offset(20, -4, fazit3, "SE"), new Float(
        startSubInterval).toString(), "text3", null, tpsteps);
    fazit4.changeColor(null, Color.BLUE, null, null);
  }

  /**
   * 
   * Prints one interval of the rectangle (200 pixel width) that represents the
   * interval of the letters
   * 
   * @param start
   *          the start of the interval
   * @param end
   *          the end of the interval
   * @param startAbsolute
   *          the startpoint of the abolute subinterval
   * @param endAbsolute
   *          the startpoint of the abolute subinterval
   * @param letter
   *          the letter that is shown in the rectangle
   * @param prim
   *          the primitive under whck the interval is shown
   * @return the rectangle representating the interval
   */
  private Rect printRange(Range r, Primitive prim) {
    /*
     * create the rectange The recangle are seperately saved in a static vector
     * of the class. So each rectangle can always be accesed for example to
     * highlight a cell afterwards
     */
    int s = new Float(700 * r.getStart()).intValue();
    int e = new Float(700 * r.getEnd()).intValue();
    RectProperties recIntervalProp = new RectProperties();
    recIntervalProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    Rect rect = lang.newRect(new Offset(s, 40, prim, "SW"), new Offset(e, 65,
        prim, "SW"), "rectangle", null, recIntervalProp);
    // add it to the static vector
    rects.add(rect);
    tpsteps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 10));
    lang.newText(new Offset((e - s) / 2, 20, rect, "M"), "" + r.getLetter(),
        "text", null, tpsteps);
    // round on 3 digits
    lang.newText(new Offset(2, 9, rect, "SW"),
        new Float(startSubInterval + r.getStart()
            * (endSubInterval - startSubInterval)).toString(), "startValue",
        null, tpsteps);
    return rect;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    String[] strArray = (String[]) primitives.get("stringArray");
    compress(strArray);
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
    return "Arithmetische Komprimierung";
  }

  public String getAlgorithmName() {
    return "Arithmetische Komprimierung";
  }

  public void init() {
    lang = new AnimalScript("Arithmetic Encoding", "Florian Lindner", 1000, 1000);
    lang.setStepMode(true);
  }
}
