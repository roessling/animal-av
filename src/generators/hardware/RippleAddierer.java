package generators.hardware;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class RippleAddierer implements ValidatingGenerator {
  private Language                         lang;
//  private SourceCode                       src;
  private Vector<Primitive>                allObjects;
  private Vector<algoanim.primitives.Text> allSums;
  private Vector<algoanim.primitives.Text> allCout;
  private algoanim.primitives.Text         header;
//  private Rect                             vLine;
  private int                              steps;
  private Vector<Integer>                  b1;
  private Vector<Integer>                  b2;
  private Vector<Integer>                  c;
  private Vector<Integer>                  r;
  private int                              numAnds;
  private int                              numOrs;
  private SourceCode                       verilog;
  private SourceCode                       description;
  private Color                            textColor;
  private Color                            hightlightColor;
  private Color                            adderBorder;
  public int                               delayAndGate;
  public int                               delayOrGate;

  public void init() {
    lang = new AnimalScript("N-Bit Rippel Volladdierer",
        "Marcel Gazsi, Mark Schneemann ", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int[] A = (int[]) primitives.get("A");
    int[] B = (int[]) primitives.get("B");

    if (A.length != B.length) {
      if (A.length > B.length) {
        int difference = A.length - B.length;
        int[] temp = B;

        B = new int[temp.length + difference];
        for (int i = 0; i < difference; i++) {
          B[i] = 0;
        }

        for (int i = 0; i < temp.length; i++) {
          B[difference + i] = temp[i];
        }
      } else {
        int[] temp = A;
        int difference = B.length - A.length;

        A = new int[temp.length + difference];

        for (int i = 0; i < difference; i++) {
          A[i] = 0;
        }

        for (int i = 0; i < temp.length; i++) {
          A[difference + i] = temp[i];
        }
      }
    }

    this.delayAndGate = (Integer) primitives.get("Verzögerung And Gatter");
    this.delayOrGate = (Integer) primitives.get("Verzögerung Or Gatter");
    this.textColor = (Color) primitives.get("Textfarbe");
    this.hightlightColor = (Color) primitives.get("Highlight Farbe");
    this.adderBorder = (Color) primitives.get("Farbe der Addierer");

    this.allObjects = new Vector<Primitive>();
    this.allSums = new Vector<algoanim.primitives.Text>();
    this.allCout = new Vector<algoanim.primitives.Text>();

    lang.setStepMode(true);

    this.steps = Math.max(A.length, B.length);

    this.b1 = new Vector<Integer>();
    this.b2 = new Vector<Integer>();
    this.b1 = this.intArrayToVector(A, this.b1);
    this.b2 = this.intArrayToVector(B, this.b2);

    this.runAnim();

    return lang.toString();
  }

  public String getName() {
    return "Rippel Volladdierer";
  }

  public String getAlgorithmName() {
    return "Rippel Volladdierer";
  }

  public String getAnimationAuthor() {
    return "Marcel Gazsi, Mark Schneemann";
  }

  public String getDescription() {
    return "Der Rippel-Carry Adder ist die einfachste Methode um einen 'N-Bit Carry Propagate Adder' zu bauen, "
        + "\n"
        + "in dem man N Volladdierer hinter einander schaltet und die C_out des n-ten Addierers mit dem C_in des n+1-ten Addierers verbindet. "
        + "\n"
        + "\n"
        + "Die Vorteile des Ripple-Carry Addierers sind, dass er einerseits, im Vergleich zu anderen Carry Propagate Addierern, "
        + "\n"
        + "nicht so viel Hardware ben&ouml;tigt und dass er sehr einfach zu bauen ist. F&uuml;r kleine Bitbreiten gen&uuml;gt er aus Sicht der Performance. "
        + "\n"
        + "\n"
        + "Die Nachteile sind, dass bei gr&ouml;&szlig;eren Bitbreiten sehr langsam ist, weil jede Stelle von all den vorherigen Stellen abh&auml;ngig ist.";
  }

  public String getCodeExample() {
    return "module adder ( \n" + "input  [n-1 : 0] a,b; \n"
        + "output [n-1 : 0] sum; \n" + "output c_out);\n"
        + "for (i = 0; i < 5; i = i + 1) begin \n"
        + "    sum[i] = a[i] + b[i] \n" + "end" + "endmodule";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
  }

  // Wie da verilog?
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public RippleAddierer(Language aLang) {
    this.lang = aLang;
    this.allObjects = new Vector<Primitive>();
    this.allSums = new Vector<algoanim.primitives.Text>();
    this.allCout = new Vector<algoanim.primitives.Text>();
    this.lang.setStepMode(true);
  }

  public RippleAddierer() {
    this.lang = new AnimalScript("Ripple Addierer [DE]",
        "Marcel Andreas Gazsi, Mark Schneemann", 800, 600);
    this.allObjects = new Vector<Primitive>();
    this.allSums = new Vector<algoanim.primitives.Text>();
    this.allCout = new Vector<algoanim.primitives.Text>();
    this.lang.setStepMode(true);
  }

  /**
   * Runs the whole animation
   * 
   * @author Marcel Andreas Gazsi
   */
  public void runAnim() {

    // First of all, we display a description of the algorithm
    this.showHeadline();
    this.drawVerticalLine();

    this.c = new Vector<Integer>();
    this.r = new Vector<Integer>();

    // Init the vector with zeros
    for (int i = 0; i < b1.size(); i++) {
      c.add(0);
      r.add(0);
    }

    this.calcVaules();
    lang.nextStep("Beschreibung");
    this.drawDescription();
    lang.nextStep("Verilog Code");
    this.printVerilogCode();
    lang.nextStep("Aufbau des Addierers");
    this.hideDescription();
    for (int i = 0; i < this.steps; i++) {
      this.drawPart(20 + (i * 80), 230, i, b1.elementAt(i).toString(), b2
          .elementAt(i).toString());
      lang.nextStep();
    }
    this.verilog.unhighlight(2);
    lang.nextStep("Addition");

    for (int i = 1; i <= this.steps; i++) {
      this.doSumStep(i);
    }
    this.flushDrawing();
    lang.nextStep("Zusammenfassung");
    this.calcNeededHardware();
    this.drawSummary();
    lang.nextStep();
  }

  /**
   * Draws adder, the wires and the textfields
   * 
   * @author Marcel Andreas Gazsi
   * @param x
   *          x coordinate of the next part of the adder
   * @param y
   *          y coordinate of the next part of the adder
   * @param iteratino
   *          the iteration count
   * @param input1
   *          the first input
   * @param input2
   *          the second input
   */
  private void drawPart(int x, int y, int iteration, String input1,
      String input2) {
    Rect Rect1;
    Rect Rect2;
    Rect Rect3;
    Rect Rect4;
    Rect Rect5;

    algoanim.primitives.Text name;

    // Now highlight the verilog code
    this.verilog.highlight(2);

    TextProperties textProps = new TextProperties();
    algoanim.primitives.Text i1 = lang.newText(new Coordinates(x + 27, y - 54),
        input1, "input1." + iteration, null, textProps);
    i1.changeColor(null, this.textColor, null, null);
    algoanim.primitives.Text i2 = lang.newText(new Coordinates(x + 67, y - 54),
        input2, "input2." + iteration, null, textProps);
    i2.changeColor(null, this.textColor, null, null);
    algoanim.primitives.Text cout = lang.newText(
        new Coordinates(x + 10, y - 3), " ", "cout" + iteration, null,
        textProps);
    cout.changeColor(null, this.textColor, null, null);
    algoanim.primitives.Text i3 = lang.newText(new Coordinates(x + 53, y + 44),
        " ", "output", null, textProps);
    i3.changeColor(null, this.textColor, null, null);
    this.allObjects.add(i1);
    this.allObjects.add(i2);
    // Define properties
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.adderBorder);

    Rect1 = lang.newRect(new Coordinates(x, y), new Coordinates(x + 20, y),
        "wire" + iteration, null, rectProps);
    this.allObjects.add(Rect1);

    // Here we have the adder as symbol
    // add = language.newRect(new Coordinates(40,100), new
    // Coordinates(100,140), "adder", null, rectProps);
    Rect2 = lang.newRect(new Coordinates(x + 20, y - 20), new Coordinates(
        x + 80, y + 20), "adder" + iteration, null, rectProps);
    this.allObjects.add(Rect2);

    // a Name within the adder
    // name = language.newText(new Coordinates(50,110), "Adder", "name1",
    // null, textProps);
    name = lang.newText(new Coordinates(x + 30, y - 10), "Adder", "name"
        + iteration, null, textProps);
    name.changeColor(null, this.textColor, null, null);
    this.allObjects.add(name);

    Rect3 = lang.newRect(new Coordinates(x + 30, y - 20), new Coordinates(
        x + 30, y - 40), "input1." + iteration, null, rectProps);
    this.allObjects.add(Rect3);

    Rect4 = lang.newRect(new Coordinates(x + 70, y - 20), new Coordinates(
        x + 70, y - 40), "input2." + iteration, null, rectProps);
    this.allObjects.add(Rect4);
    Rect5 = lang.newRect(new Coordinates(x + 50, y + 20), new Coordinates(
        x + 50, y + 40), "output" + iteration, null, rectProps);
    this.allObjects.add(Rect5);
    this.allSums.add(i3);
    this.allCout.add(cout);
  }

  /**
   * A function which displays a description of our algorithm
   * 
   * @author Marcel Andreas Gazsi
   * @since 17.05.2013
   */
  private void drawDescription() {
    SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.textColor);

    this.description = lang.newSourceCode(new Coordinates(20, 80),
        "description", null, sourceCodeProps);
    this.description.addCodeLine(
        "Der Rippel-Carry Adder ist die einfachste Methode um einen", null, 0,
        null);
    this.description.addCodeLine(
        "'N-Bit Carry propagate adder' zu in dem mann N Volladdierer", null, 0,
        null);
    this.description.addCodeLine(
        "hinter einander schaltet und die C_out des n-ten Addierers ", null, 0,
        null);
    this.description.addCodeLine(
        "bauen, mit dem C_in des n+1-ten Addierers verbindet.", null, 0, null);
    this.description.addCodeLine(" ", null, 0, null);
    this.description.addCodeLine(
        "Die Vorteile des Ripple-Carry Addierers sind, dass er einerseits, ",
        null, 0, null);
    this.description.addCodeLine(
        "im Vergleich zu anderen Carry Propagate Addierern, nicht so viel",
        null, 0, null);
    this.description.addCodeLine(
        "Hardware benötigt und dass er sehr einfach zu bauen ist.", null, 0,
        null);
    this.description.addCodeLine(
        "Für kleine Bitbreiten genügt er aus Sicht der Performance.", null, 0,
        null);
    this.description.addCodeLine(" ", null, 0, null);
    this.description.addCodeLine(
        "Die Nachteile sind, dass bei größeren Bitbreiten sehr langsam", null,
        0, null);
    this.description.addCodeLine(
        "ist, weil jede Stelle von all den vorherigen Stellen abhängig ist.",
        null, 0, null);
  }

  /**
   * This function displays the iteration through the adder chain
   * 
   * @author Marcel Gazsi
   * @param i
   *          Integer
   */
  private void doSumStep(int i) {
    int elementPos = (this.steps - i) * 8 + 2;

    // Get the labels we'll need them soon!
    algoanim.primitives.Text result = (algoanim.primitives.Text) allSums
        .get(this.steps - i);
    algoanim.primitives.Text cout = this.allCout.get(this.steps - i);

    // Set the text for the actual c_out and result
    String c_text = c.get(c.size() - i).toString();
    String r_text = r.get(r.size() - i).toString();
    cout.setText(c_text, null, null);
    result.setText(r_text, null, null);

    // Unhighlight the last wire
    int posOfOldWire = (this.steps - i + 1) * 8 + 2;
    if (i != 1) {
      Rect oldWire = (Rect) allObjects.get(posOfOldWire);
      oldWire.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          this.adderBorder, null, null);
    }
    // Highlight the actual wire
    Rect wire = (Rect) allObjects.get(elementPos);
    wire.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        this.hightlightColor, null, null);

    this.lang.nextStep();
  }

  /**
   * Mindless function to draw the headline
   * 
   * @author Marcel Gazsi
   */
  private void showHeadline() {
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    header = lang.newText(new Coordinates(20, 30), "Ripple Addierer", "header",
        null, headerProps);
    header.changeColor(null, this.textColor, null, null);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.adderBorder);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);
  }

  /**
   * Mindless function to draw a line
   * 
   * @author Marcel Gazsi
   */
  private void drawVerticalLine() {
    RectProperties vLineProbs = new RectProperties();
    lang.newRect(new Coordinates(460, 80), new Coordinates(460, 180),
        "vLine", null, vLineProbs);
  }

  /**
   * This function calculates values like c_ins and c_outs four our display task
   * 
   * @author Marcel Gazsi
   */
  private void calcVaules() {

    // Now we calculate the values for all couts and the result
    for (int i = b1.size() - 1; i >= 0; i--) {
      int sum = b1.get(i) + b2.get(i);

      // Check if we need to add the c_in
      if (i + 1 < b1.size()) {
        if (c.get(i + 1) == 1) {
          sum = sum + 1;
        }
      }

      // If we need to, set the c_out
      if (sum > 1) {
        c.setElementAt(1, i);
      }

      // Now write the value, which should be displayed into the vector
      r.setElementAt(sum % 2, i);
    }
  }

  /**
   * This function calculates from the bandwith the number of and and or gates
   * needed to build the adder.
   * 
   * @author Marcel Gazsi
   */
  private void calcNeededHardware() {
    // We know an adder is made out of 2 xor and 3 ands
    // an xor is made out of 2 and and 1 or

    // Num Ands in adder
    this.numAnds = this.steps * 7;
    // Num Ors in adder
    this.numOrs = this.steps;

  }

  /**
   * This draws the last page, with all needed informations
   * 
   * @author Marcel Gazsi
   */
  private void drawSummary() {
    SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.textColor);

    int delayXor = this.delayAndGate + this.delayOrGate;
    int overalltime = this.steps * delayXor;
    int result = 0;

    for (int i = 0; i < this.r.size(); i++) {
      if (this.r.get(i) == 1) {
        result = result + (int) (Math.pow(2, this.r.size() - 1 - i));
      }
    }

    this.description = lang.newSourceCode(new Coordinates(20, 100),
        "description", null, sourceCodeProps);
    // ToDo: Hier Aufzählung
    this.description.addCodeLine("S hat folgende Formel: A xor B xor C_in.",
        null, 0, null);
    this.description.addCodeLine(
        "Cout hat folgende Formel: (A and B) or (A and C_in) or (B and C_in).",
        null, 0, null);
    this.description.addCodeLine("Daher haben wir " + this.numAnds
        + " And-Gatter verbraucht.", null, 0, null);
    this.description.addCodeLine("Außerdem wurden " + this.numOrs
        + " Or-Gatter verbaut.", null, 0, null);
    this.description.addCodeLine(
        "Die einzelnen Additionen können nicht gleichzeitig", null, 0, null);
    this.description.addCodeLine(
        "ausgeführt werden,  da jede Stelle auf seine vorherrige warten muss",
        null, 0, null);
    this.description.addCodeLine("", null, 0, null);
    this.description.addCodeLine("Unterbedingung,  dass And Gatter "
        + this.delayAndGate + " ms brauchen und Or-Gatter " + this.delayOrGate
        + " ms, benötigen wir so " + overalltime
        + "ms für die komplette Addition.", null, 0, null);
    this.description.addCodeLine("Das Ergebniss ist: " + result, null, 0, null);
    this.description.addCodeLine(
        "Außerdem hat C_out den Wert: " + this.c.get(0), null, 0, null);
  }

  /**
   * This writes as new codelines the verilog code to create a ripple adder It
   * displays how many and and or gates we used and what the result is in
   * decimal system and binary
   * 
   * @author Marcel Gazsi
   */
  private void printVerilogCode() {
    SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        this.hightlightColor);
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.textColor);
    this.verilog = lang.newSourceCode(new Coordinates(480, 80), "Veri Code",
        null, sourceCodeProps);
    this.verilog.addCodeLine("input [n-1:0]a, b", null, 0, null);
    this.verilog.addCodeLine("for (i = 0; i < " + this.steps
        + "; i = i + 1) begin", null, 0, null);
    this.verilog.addCodeLine("    result[i] = a[i] + b[i]", null, 0, null);
    this.verilog.addCodeLine("end", null, 0, null);

  }

  /**
   * This functions hides the text and drawing of the former presentation
   * 
   * @author Marcel Gazsi
   */
  private void flushDrawing() {
    for (int i = 0; i < this.allObjects.size(); i++) {
      Primitive apart = this.allObjects.get(i);
      apart.hide();
    }

    for (int i = 0; i < this.allSums.size(); i++) {
      Primitive apart = this.allSums.get(i);
      apart.hide();
    }

    for (int i = 0; i < this.allCout.size(); i++) {
      Primitive apart = this.allCout.get(i);
      apart.hide();
    }
  }

  private void hideDescription() {
    this.description.hide();
  }

  private Vector<Integer> intArrayToVector(int[] anArray, Vector<Integer> vector) {
    for (int i = 0; i < anArray.length; i++) {
      Integer tmp = new Integer(anArray[i]);
      vector.add(tmp);
    }
    return vector;
  }

  public void setSteps(int n) {
    this.steps = n;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    int[] A = (int[]) primitives.get("A");
    int[] B = (int[]) primitives.get("B");

    for (int i = 0; i < A.length; i++) {
      if (A[i] > 1 || A[i] < 0) {
        throw new IllegalArgumentException(
            "Das Array A enthält eine nicht binäre Zahl");
      }
    }

    for (int i = 0; i < B.length; i++) {
      if (B[i] > 1 || B[i] < 0) {
        throw new IllegalArgumentException(
            "Das Array B enthält eine nicht binäre Zahl");
      }
    }

    if (A.length != B.length)
      JOptionPane
          .showMessageDialog(
              null,
              "Eine Binärzahl ist kürzer als die andere, fülle die Kleinere mit Nullen auf",
              "Bitlänge", JOptionPane.WARNING_MESSAGE);
    return true;
  }

}
