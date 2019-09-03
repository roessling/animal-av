package generators.cryptography.helpers;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * 
 * @author Michelle Walther & Steffen Heger
 * 
 */
public class CFB {

  /**
   * The concrete language object used for creating output
   */
  private Language lang;

  /**
   * Default constructor
   * 
   * @param l
   *          the concrete language object used for creating output
   */
  public CFB(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  private static final String DESCRIPTION          = "Eine Blockchiffre ist ein Chiffriermodus zum verschluesseln von Bloecken einer konstanten Laenge, zu Bloecken gleicher Laenge.\n"
                                                       + "Betriebsmodi ermoeglichen den wiederholten und sicheren Gebrauch von Blockchiffren unter einem einzelnen Schluessel.\n"
                                                       + "Einer von diesen Betriebsmodi ist der Cipher Feedback Mode kurz CFB Mode.\n"
                                                       + "Der CFB ist ein Betriebsmodus in dem Klartexte verschluesselt (und wieder dechiffriert) werden koennen,\n"
                                                       + "die laenger als die Blocklaenge des Chiffrierverfahrens sind.\n"
                                                       + "Im CFB werden Bloecke, die kuerzere Laenge als n haben koennen, durch Addition mod 2 entsprechender Schluesselbloecke verschluesselt.\n";

  private static final String ALGORITHMDESCRIPTION = "init:\n"
                                                       + "          - nehme eine Nachricht m zum kodieren\n"
                                                       + "          - nehme eine Verschluesselungsfunktion E. Im Allgemeinfall ist E eine Permutation\n"
                                                       + "          - nehme eine natuerliche Zahl n als Initialisierungsvektor IV Element {0,1}^n.\n"
                                                       + "          - nehme eine natuerliche Zahl r, 1 <= r <= n\n"
                                                       + "          - der Text wird in Bloecke der Laenge r aufgeteilt. Wir bekommen m = m_1 m_2 m_3 ... m_j raus.\n"
                                                       + "	       - Angenommen Block m_j hat nicht die Laenge r. Falls ja, fuege Nullen am Ende von m_j an\n"
                                                       + "            bis m_j die Laenge r hat.\n"
                                                       + "          - wenn man die Blockfolge m_1,...,m_u verschluesseln will, setzt man I_1= I_V\n"
                                                       + "Schritt 1:\n"
                                                       + "          - berechne O_i = E_k (I_i)\n"
                                                       + "Schritt 2:\n"
                                                       + "          - berechne t_i auf den String, der aus den ersten r Bits von O_i gebildet wird,\n"
                                                       + "Schritt 3:\n"
                                                       + "          - berechne c_i = m_i XOR t_i\n"
                                                       + "\n"
                                                       + "Schritt 4:\n"
                                                       + "		   - setze I_i+1 = 2^r I_i +c_i mod 2^n. I_i+1 entsteht also, indem in I_i die ersten r Bits\n"
                                                       + "		     entfernt werden und c_i hinten angefuegt wird.\n"
                                                       + "		     Der Schluesseltext ist die Folge c1, c2, . . . , cu.";

  public void cfb(String msg, int nNum, int rNum, String initVector, E eFunc) {
    // make header with Rectangle
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(138, 43,
        226));
    textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 35));
    Text header = lang.newText(new Coordinates(600, 50), "CFB Mode", "header",
        null, textProps);

    RectProperties rectProps = new RectProperties();
    rectProps
        .set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(0, 250, 154));
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-6, -6, header, AnimalScript.DIRECTION_NW),
        new Offset(6, 6, header, AnimalScript.DIRECTION_SE), "headerRect",
        null, rectProps);

    // start a new step after header was printed
    lang.nextStep();

    // create Descrption with 2 rects
    SourceCode descr = lang.newSourceCode(new Coordinates(50, 100), "descr",
        null);
    descr
        .addCodeLine(
            "Eine Blockchiffre ist ein Chiffriermodus zum verschluesseln von Bloecken einer konstanten Laenge, zu Bloecken gleicher Laenge.",
            null, 0, new TicksTiming(25));
    descr
        .addCodeLine(
            "Betriebsmodi ermoeglichen den wiederholten und sicheren Gebrauch von Blockchiffren unter einem einzelnen Schluessel.",
            null, 0, new TicksTiming(10));
    descr
        .addCodeLine(
            "Einer von diesen Betriebsmodi ist der Cipher Feedback Mode kurz CFB Mode.",
            null, 0, new TicksTiming(25));
    descr
        .addCodeLine(
            "Der CFB ist ein Betriebsmodus in dem Klartexte verschluesselt (und wieder dechiffriert) werden koennen,",
            null, 0, new TicksTiming(10));
    descr.addCodeLine(
        "die laenger als die Blocklaenge des Chiffrierverfahrens sind.", null,
        0, new TicksTiming(25));
    descr
        .addCodeLine(
            "Im CFB werden Bloecke, die kuerzere Laenge als n haben koennen, durch Addition mod 2 entsprechender Schluesselbloecke verschluesselt.",
            null, 0, new TicksTiming(50));

    // inner Rect
    Rect inRect = lang.newRect(new Offset(-10, -2, "descr",
        AnimalScript.DIRECTION_NW), new Offset(10, 2, "descr",
        AnimalScript.DIRECTION_SE), "inRect", null);

    // outer Rect
    rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 255,
        255));
    Rect outRect = lang.newRect(new Offset(-10, -2, "descr",
        AnimalScript.DIRECTION_NW), new Offset(10, 2, "descr",
        AnimalScript.DIRECTION_SE), "outRect", null, rectProps);
    // start a new step description was printed
    lang.nextStep();

    // make Calc Text
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.BOLD, 25));
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(138, 43,
        226));
    Text calc = lang.newText(new Offset(100, 30, "inRect",
        AnimalScript.DIRECTION_SW), "Beschreibung", "calc", null, textProps);

    // make step by step block
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.PLAIN, 20));
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(138, 43,
        226));
    Text stepDescr = lang.newText(new Offset(10, 10, "calc",
        AnimalScript.DIRECTION_SW), "step by step", "stepDescr", null,
        textProps);

    SourceCodeProperties srcProps = new SourceCodeProperties();
    srcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));
    SourceCode steps = lang.newSourceCode(new Offset(0, 10, "stepDescr",
        AnimalScript.DIRECTION_SW), "steps", null, srcProps);
    steps.addCodeLine("init", null, 0, null);
    steps.addCodeLine("          - nehme eine Nachricht m zum kodieren", null,
        0, null);
    steps
        .addCodeLine(
            "          - nehme eine Verschluesselungsfunktion E. Im Allgemeinfall ist E eine Permutation",
            null, 0, null);
    steps
        .addCodeLine(
            "          - nehme eine natuerliche Zahl n als Initialisierungsvektor IV Element {0,1}^n.",
            null, 0, null);
    steps.addCodeLine("          - nehme eine natuerliche Zahl r, 1 <= r <= n",
        null, 0, null);
    steps
        .addCodeLine(
            "          - der Text wird in Bloecke der Laenge r aufgeteilt. Wir bekommen m = m_1 m_2 m_3 ... m_j raus.",
            null, 0, null);
    steps
        .addCodeLine(
            "	       	 - Angenommen Block m_j hat nicht die Laenge r. Falls ja, fuege Nullen am Ende von m_j an",
            null, 0, null);
    steps.addCodeLine("            bis m_j die Laenge r hat.", null, 0, null);
    steps
        .addCodeLine(
            "          - wenn man die Blockfolge m_1,...,m_u verschluesseln will, setzt man I_1= I_V",
            null, 0, null);
    steps.addCodeLine("", null, 0, null);
    steps.addCodeLine("Schritt 1:", null, 0, null);
    steps.addCodeLine("          - berechne O_i = E_k (I_i)", null, 0, null);
    steps.addCodeLine("", null, 0, null);
    steps.addCodeLine("Schritt 2:", null, 0, null);
    steps
        .addCodeLine(
            "          - berechne t_i auf den String, der aus den ersten r Bits von O_i gebildet wird,",
            null, 0, null);
    steps.addCodeLine("", null, 0, null);
    steps.addCodeLine("Schritt 3:", null, 0, null);
    steps.addCodeLine("          - berechne c_i = m_i XOR t_i", null, 0, null);
    steps.addCodeLine("", null, 0, null);
    steps.addCodeLine("Schritt 4:", null, 0, null);
    steps
        .addCodeLine(
            "		      - setze I_i+1 = 2^r I_i +c_i mod 2^n. I_i+1 entsteht also, indem in I_i die ersten r Bits",
            null, 0, null);
    steps.addCodeLine("		     entfernt werden und c_i hinten angefuegt wird.",
        null, 0, null);
    steps.addCodeLine(
        "		     Der Schluesseltext ist die Folge c1, c2, . . . , cu.", null, 0,
        null);

    Rect stepRect = lang.newRect(new Offset(-8, -6, "stepDescr",
        AnimalScript.DIRECTION_NW), new Offset(8, 6, "steps",
        AnimalScript.DIRECTION_SE), "stepRect", null);

    // next step after printing step by step and pseudocode
    lang.nextStep();

    // hide description
    descr.hide(new TicksTiming(500));
    inRect.hide(new TicksTiming(500));
    outRect.hide(new TicksTiming(500));

    // next step after hiding description
    lang.nextStep();

    // hide step by step
    calc.hide(new TicksTiming(150));
    stepRect.hide(new TicksTiming(150));
    stepDescr.hide(new TicksTiming(150));
    steps.hide(new TicksTiming(150));

    // next step after hiding step by step
    lang.nextStep();

    // make m text
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.PLAIN, 16));
    Text m = lang.newText(new Coordinates(40, 150), "m = " + msg, "m",
        new TicksTiming(50), textProps);

    // make n text
    Text n = lang.newText(new Offset(0, 20, "m", AnimalScript.DIRECTION_SW),
        "n = " + nNum, "n", new TicksTiming(100), textProps);

    // make r text
    Text r = lang.newText(new Offset(0, 20, "n", AnimalScript.DIRECTION_SW),
        "r = " + rNum, "r", new TicksTiming(150), textProps);

    // make IV text
    Text iv = lang.newText(new Offset(0, 20, "r", AnimalScript.DIRECTION_SW),
        "iv = " + initVector, "iv", new TicksTiming(200), textProps);

    // List to save all eFunc elements
    LinkedList<Text> eList = new LinkedList<Text>();
    LinkedList<Polyline> lineList = new LinkedList<Polyline>();

    String[] givenPermutation = (String[]) eFunc.stringRepresentation();
    String[][] permutation = new String[2][givenPermutation.length];
    // fill array
    for (int i = 0; i < givenPermutation.length; i++) {
      permutation[0][i] = new Integer(i + 1).toString();
      permutation[1][i] = givenPermutation[i];
    }

    // create Grid

    // make [0][0] and e[1][0]
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(138, 43,
        226));
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.PLAIN, 16));
    Text curr0 = lang.newText(
        new Offset(50, 18, "m", AnimalScript.DIRECTION_NE), permutation[0][0],
        "e[0][0]", new TicksTiming(250), textProps);
    Text curr1 = lang.newText(new Offset(0, 20, "e[0][0]",
        AnimalScript.DIRECTION_SW), permutation[1][0], "e[1][0]",
        new TicksTiming(250), textProps);
    // add all to the eList
    eList.add(curr0);
    eList.add(curr1);

    // generate the rest of e[0][i] and e[1][i]
    for (int i = 1; i < permutation[0].length; i++) {
      curr0 = lang.newText(new Offset(20, 0, "e[0][" + (i - 1) + "]",
          AnimalScript.DIRECTION_NE), permutation[0][i], "e[0][" + i + "]",
          new TicksTiming(250), textProps);
      curr1 = lang.newText(new Offset(20, 0, "e[1][" + (i - 1) + "]",
          AnimalScript.DIRECTION_NE), permutation[1][i], "e[1][" + i + "]",
          new TicksTiming(250), textProps);
      eList.add(curr0);
      eList.add(curr1);
    }

    // make E text
    Text e = lang.newText(new Offset(-40, 25, "e[0][0]",
        AnimalScript.DIRECTION_NW), "E = ", "e", new TicksTiming(250),
        textProps);
    eList.add(e);

    // make brackets
    Polyline pl1 = lang.newPolyline(new Offset[] {
        new Offset(-10, -5, "e[0][0]", AnimalScript.DIRECTION_NW),
        new Offset(-10, 5, "e[1][0]", AnimalScript.DIRECTION_SW) }, "pl1",
        new TicksTiming(250));
    Polyline pl2 = lang.newPolyline(new Offset[] {
        new Offset(0, 0, "pl1", AnimalScript.DIRECTION_NW),
        new Offset(10, 0, "pl1", AnimalScript.DIRECTION_NW) }, "pl2",
        new TicksTiming(250));
    Polyline pl3 = lang.newPolyline(new Offset[] {
        new Offset(0, 0, "pl1", AnimalScript.DIRECTION_SW),
        new Offset(10, 0, "pl1", AnimalScript.DIRECTION_SW) }, "pl3",
        new TicksTiming(250));
    lineList.add(pl1);
    lineList.add(pl2);
    lineList.add(pl3);

    Polyline pr1 = lang.newPolyline(new Offset[] {
        new Offset(10, -5, "e[0][" + (permutation[0].length - 1) + "]",
            AnimalScript.DIRECTION_NE),
        new Offset(10, 5, "e[1][" + (permutation[0].length - 1) + "]",
            AnimalScript.DIRECTION_SE) }, "pr1", new TicksTiming(250));
    Polyline pr2 = lang.newPolyline(new Offset[] {
        new Offset(0, 0, "pr1", AnimalScript.DIRECTION_NE),
        new Offset(-10, 0, "pr1", AnimalScript.DIRECTION_NE) }, "pr2",
        new TicksTiming(250));
    Polyline pr3 = lang.newPolyline(new Offset[] {
        new Offset(0, 0, "pr1", AnimalScript.DIRECTION_SE),
        new Offset(-10, 0, "pr1", AnimalScript.DIRECTION_SE) }, "pr3",
        new TicksTiming(250));
    lineList.add(pr1);
    lineList.add(pr2);
    lineList.add(pr3);

    // next step after printing the preconditions
    lang.nextStep();

    // print Pseudocode
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.BOLD, 20));
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(138, 43,
        226));

    srcProps = new SourceCodeProperties();
    srcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.BOLD, 20));
    srcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(
        138, 43, 226));
    SourceCode src = lang.newSourceCode(new Coordinates(40, 400), "src", null,
        srcProps);

    src.addCodeLine("def CFB(m, r, IV):", "src", 0, null);
    src.addCodeLine("zerlege den Text in Bloecke der Laenge r", "src", 1, null);
    src.addCodeLine("wende die Permutation E auf den Block I_i an", "src", 1,
        null);
    src.addCodeLine("(erste Iteration = Anwendung von E auf IV)", "src", 1,
        null);
    src.addCodeLine("# setze Initialisierungsvektor als I_i", "src", 1, null);
    src.addCodeLine("I_i = IV", "src", 1, null);
    src.addCodeLine("1.) O_i = E(I_i)", "src", 1, null);
    src.addCodeLine("2.) t_i = ersten r Bits von O_i", "src", 1, null);
    src.addCodeLine("3.) m_i = zu betrachtender Teilblock", "src", 1, null);
    src.addCodeLine("4.) c[i] = t_i XOR m_i", "src", 1, null);
    src.addCodeLine(
        "5.) I_i = entferne die ersten r Bit von I_i und haenge c_i hinten an",
        "src", 1, null);
    src.addCodeLine(
        "gehe zurueck zu Schritt 1.) solang noch Bloecke vorhanden sind",
        "src", 1, null);
    src.addCodeLine("return c", "src", 1, null);

    // highlight line 1 in code
    src.highlight(0);

    // print table
    // between line and table element are (n+1)/2 + 10 pixel.
    int between = (nNum + 1) / 2 * 10;

    // calc number of m blocks
    int blockCount = (int) Math.ceil(new Double(msg.length()) / rNum);

    // make i entry
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));
    Text i = lang.newText(new Offset(120, 0, "src", AnimalScript.DIRECTION_NE),
        "i", "i", null, textProps);

    int lineLength = (blockCount + 1) * 33;

    // paint line between i and I_i
    Polyline lineiI_i = lang.newPolyline(new Offset[] {
        new Offset(between, -5, "i", AnimalScript.DIRECTION_NE),
        new Offset(between, lineLength, "i", AnimalScript.DIRECTION_NE) },
        "lineiI_i", null);

    // make I_i entry
    Text i_i = lang.newText(new Offset(30, 5, "lineiI_i",
        AnimalScript.DIRECTION_NE), "i_i", "i_i", null, textProps);

    // paint line between I_i and O_i
    Polyline lineI_iO_i = lang.newPolyline(new Offset[] {
        new Offset(between, -5, "i_i", AnimalScript.DIRECTION_NE),
        new Offset(between, lineLength, "i_i", AnimalScript.DIRECTION_NE) },
        "lineI_iO_i", null);

    // make O_i entry
    Text o_i = lang.newText(new Offset(30, 5, "lineI_iO_i",
        AnimalScript.DIRECTION_NE), "o_i", "o_i", null, textProps);

    // paint line between O_i and t_i
    Polyline lineO_it_i = lang.newPolyline(new Offset[] {
        new Offset(between, -5, "o_i", AnimalScript.DIRECTION_NE),
        new Offset(between, lineLength, "o_i", AnimalScript.DIRECTION_NE) },
        "lineO_it_i", null);

    // make t_i entry
    Text t_i = lang.newText(new Offset(30, 5, "lineO_it_i",
        AnimalScript.DIRECTION_NE), "t_i", "t_i", null, textProps);

    // paint line between t_i and m_i
    Polyline linet_im_i = lang.newPolyline(new Offset[] {
        new Offset(between, -5, "t_i", AnimalScript.DIRECTION_NE),
        new Offset(between, lineLength, "t_i", AnimalScript.DIRECTION_NE) },
        "linet_im_i", null);

    // make m_i entry
    Text m_i = lang.newText(new Offset(30, 5, "linet_im_i",
        AnimalScript.DIRECTION_NE), "m_i", "m_i", null, textProps);

    // paint line between m_i and c_i
    Polyline linem_ic_i = lang.newPolyline(new Offset[] {
        new Offset(between, -5, "m_i", AnimalScript.DIRECTION_NE),
        new Offset(between, lineLength, "m_i", AnimalScript.DIRECTION_NE) },
        "linem_ic_i", null);

    // make t_i entry
    Text c_i = lang.newText(new Offset(30, 5, "linem_ic_i",
        AnimalScript.DIRECTION_NE), "c_i", "c_i", null, textProps);

    // horizontal line
    Polyline hline = lang.newPolyline(new Offset[] {
        new Offset(-35, 5, "i", AnimalScript.DIRECTION_SW),
        new Offset(30, 5, "c_i", AnimalScript.DIRECTION_SE) }, "hline", null);

    // double line between table and Code
    Polyline vline1 = lang.newPolyline(new Offset[] {
        new Offset(-120, 0, "lineiI_i", AnimalScript.DIRECTION_NW),
        new Offset(-120, 0, "lineiI_i", AnimalScript.DIRECTION_SW) }, "vline1",
        null);
    Polyline vline2 = lang.newPolyline(new Offset[] {
        new Offset(-2, 0, "vline1", AnimalScript.DIRECTION_NW),
        new Offset(-2, 0, "vline1", AnimalScript.DIRECTION_SW) }, "vline2",
        null);

    // next step after printing code and table
    lang.nextStep();

    // split text
    Text split = lang.newText(new Offset(200, 10, "e",
        AnimalScript.DIRECTION_NE), "teile m in Bloecke der Laenge:" + rNum
        + ":", "split", null, textProps);

    // split Array
    Text blocks = lang.newText(new Offset(200, 60, "e",
        AnimalScript.DIRECTION_SE), "Bloecke = ", "blocks", null, textProps);

    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(192, 192,
        192));
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(
        255, 20, 147));
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(0,
        250, 154));

    // get filler
    String fill = "";
    for (int j = 0; j < rNum; j++)
      fill += "_";

    String[] input = new String[blockCount];
    for (int j = 0; j < blockCount; j++)
      input[j] = fill;

    StringArray arr = lang.newStringArray(new Offset(5, 0, "blocks",
        AnimalScript.DIRECTION_SE), input, "arr", null, arrayProps);

    src.unhighlight(0, 0, false, new TicksTiming(200), null);
    src.highlight(1, 0, false, new TicksTiming(200), null);

    // next step after creating the array
    lang.nextStep();

    // get split
    SplitReturn splitResult = splitTextIntoBlocks(msg, rNum, blockCount);
    String[] blocks_arr = splitResult.split;
    // put it in
    for (int j = 0; j < blocks_arr.length - 1; j++) {
      arr.put(j, blocks_arr[j], null, null);
      lang.nextStep();
    }

    // add last block
    arr.put(blocks_arr.length - 1, blocks_arr[blocks_arr.length - 1], null,
        null);

    // check if last element has length of r
    int missing = splitResult.missing;
    if (missing > 0) {
      // add Zeros Text
      Text addZeros1 = lang.newText(new Offset(15, 10, "arr",
          AnimalScript.DIRECTION_NE), "der letzte Block hat nicht Laenge "
          + rNum, "addZeros1", null, textProps);
      Text addZeros2 = lang.newText(new Offset(0, 1, "addZeros1",
          AnimalScript.DIRECTION_SW),
          "mit 0 auffuellen bis zu gewuenschter Laenge " + rNum, "addZeros2",
          null, textProps);

      // next step after printing addZero messages

      lang.nextStep();

      // hide messages
      addZeros1.hide(new TicksTiming(700));
      addZeros2.hide(new TicksTiming(700));

      // fill missing _ with 0
      String tmp = blocks_arr[blocks_arr.length - 1];
      int ticks = 900;
      for (int j = 0; j <= missing; j++) {
        tmp = tmp.replaceFirst("_", "0");
        arr.put(blocks_arr.length - 1, tmp, new TicksTiming(ticks), null);
        ticks = ticks + 200;
      }
      blocks_arr[blocks_arr.length - 1] = tmp;

      // next step after splitting the array
      lang.nextStep();

      src.unhighlight(1);
      src.highlight(5);
    }

    // add 0 label and init Vektor
    // make step 0 manuell because of the positioning
    int pufferN = 5 * between / 10;
    int pufferR = 2 * between / 10;

    LinkedList<String> cList = new LinkedList<String>();
    LinkedList<Text> tableList = new LinkedList<Text>();
    Text zero = lang.newText(new Offset(0, 10, "i", AnimalScript.DIRECTION_SW),
        "0", "0", null, textProps);
    String i_ = initVector;
    Text i_0 = lang.newText(new Offset(-1 * pufferN, 6, "i_i",
        AnimalScript.DIRECTION_SW), i_, "i_0", null, textProps);
    tableList.add(zero);
    tableList.add(i_0);

    // next step after putting zero and I_0
    lang.nextStep();

    src.unhighlight(5);
    src.highlight(6);
    String o_ = eFunc.encrypt(initVector);
    Text o_0 = lang.newText(new Offset(-1 * pufferN, 6, "o_i",
        AnimalScript.DIRECTION_SW), o_, "o_0", null, textProps);
    tableList.add(o_0);

    lang.nextStep();

    src.unhighlight(6);
    src.highlight(7);
    String t_ = o_.substring(0, rNum);
    Text t_0 = lang.newText(new Offset(-1 * pufferR, 6, "t_i",
        AnimalScript.DIRECTION_SW), t_, "t_0", null, textProps);
    tableList.add(t_0);

    lang.nextStep();

    src.unhighlight(7);
    src.highlight(8);

    lang.nextStep();

    arr.highlightElem(0, null, null);
    ArrayMarker marker = lang.newArrayMarker(arr, 0, "marker", null);
    String m_ = blocks_arr[0];
    Text m_0 = lang.newText(new Offset(-1 * pufferR, 6, "m_i",
        AnimalScript.DIRECTION_SW), m_, "m_0", null, textProps);
    tableList.add(m_0);

    lang.nextStep();

    src.unhighlight(8);
    src.highlight(9);
    String c_ = xor(t_, m_);
    cList.add(c_);
    Text c_0 = lang.newText(new Offset(-1 * pufferR, 6, "c_i",
        AnimalScript.DIRECTION_SW), c_, "c_0", null, textProps);
    tableList.add(c_0);

    lang.nextStep();

    src.unhighlight(9);
    src.highlight(10);
    zero = lang.newText(new Offset(0, 6, "0", AnimalScript.DIRECTION_SW), "1",
        "1", null, textProps);
    i_ = i_.substring(rNum) + c_;
    i_0 = lang.newText(new Offset(0, 6, "i_0", AnimalScript.DIRECTION_SW), i_,
        "i_1", null, textProps);
    tableList.add(zero);
    tableList.add(i_0);
    // first row was created, now the rest
    lang.nextStep();

    for (int j = 1; j < blocks_arr.length; j++) {
      src.unhighlight(10);
      src.highlight(11);
      arr.highlightCell(j - 1, null, null);
      lang.nextStep();

      src.unhighlight(11);
      src.highlight(6);
      o_ = eFunc.encrypt(i_);
      o_0 = lang.newText(new Offset(0, 6, "o_" + (j - 1),
          AnimalScript.DIRECTION_SW), o_, "o_" + j, null, textProps);
      tableList.add(o_0);
      lang.nextStep();

      src.unhighlight(6);
      src.highlight(7);
      t_ = o_.substring(0, rNum);
      t_0 = lang.newText(new Offset(0, 6, "t_" + (j - 1),
          AnimalScript.DIRECTION_SW), t_, "t_" + j, null, textProps);
      tableList.add(t_0);
      lang.nextStep();

      src.unhighlight(7);
      src.highlight(8);
      arr.highlightElem(j, null, null);
      marker.increment(null, null);
      m_ = blocks_arr[j];
      m_0 = lang.newText(new Offset(0, 6, "m_" + (j - 1),
          AnimalScript.DIRECTION_SW), m_, "m_" + j, null, textProps);
      tableList.add(m_0);
      lang.nextStep();

      src.unhighlight(8);
      src.highlight(9);
      c_ = xor(t_, m_);
      cList.add(c_);
      c_0 = lang.newText(new Offset(0, 6, "c_" + (j - 1),
          AnimalScript.DIRECTION_SW), c_, "c_" + j, null, textProps);
      tableList.add(c_0);
      lang.nextStep();

      src.unhighlight(9);
      src.highlight(10);
      zero = lang.newText(new Offset(0, 6, new Integer(j).toString(),
          AnimalScript.DIRECTION_SW), new Integer(j + 1).toString(),
          new Integer(j + 1).toString(), null, textProps);

      i_ = i_.substring(rNum) + c_;
      i_0 = lang.newText(new Offset(0, 6, "i_" + j, AnimalScript.DIRECTION_SW),
          i_, "i_" + (j + 1), null, textProps);
      tableList.add(zero);
      tableList.add(i_0);
      lang.nextStep();
    }

    // mark last array cell and highlight for loop
    src.unhighlight(10);
    src.highlight(11);
    arr.highlightCell(blocks_arr.length - 1, null, null);

    lang.nextStep();

    // jump to return
    src.unhighlight(11);
    src.highlight(12);
    lang.nextStep();

    // hide preconditions
    m.hide(new TicksTiming(200));
    n.hide(new TicksTiming(200));
    r.hide(new TicksTiming(200));
    iv.hide(new TicksTiming(200));
    // hide E
    for (int j = 0; j < eList.size(); j++)
      eList.get(j).hide(new TicksTiming(200));
    for (int j = 0; j < lineList.size(); j++)
      lineList.get(j).hide(new TicksTiming(200));

    // hide texts f or split block
    split.hide(new TicksTiming(215));
    blocks.hide(new TicksTiming(215));
    arr.hide(new TicksTiming(215));
    // hide source
    src.hide(new TicksTiming(250));

    // hide all elemets except the header
    // all table elements
    for (int j = 0; j < tableList.size(); j++)
      tableList.get(j).hide(new TicksTiming(275));

    // hide Table
    vline1.hide(new TicksTiming(275));
    vline2.hide(new TicksTiming(275));
    lineiI_i.hide(new TicksTiming(275));
    lineI_iO_i.hide(new TicksTiming(275));
    lineO_it_i.hide(new TicksTiming(275));
    linet_im_i.hide(new TicksTiming(275));
    linem_ic_i.hide(new TicksTiming(275));
    hline.hide(new TicksTiming(275));
    i.hide(new TicksTiming(275));
    i_i.hide(new TicksTiming(275));
    o_i.hide(new TicksTiming(275));
    t_i.hide(new TicksTiming(275));
    m_i.hide(new TicksTiming(275));
    c_i.hide(new TicksTiming(275));
    // TO DO wo passiert, dass die werte von m gedruckt werden anstelle von c
    // vermutung blocks_arr hat was damit zu tun weil in zeile:704 bedeutung für
    // das arr von m!
    // finde die variable die c wiedergeben soll nicht.
    lang.nextStep();
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));
    Text cBlocks = lang.newText(new Coordinates(250, 150),
        "Das Ergebnis ist folgendes Array von chiffrierten Teilbloecken:",
        "cBlocks", null, textProps);

    // make ciphertext
    String cipher = "";
    for (int j = 0; j < blocks_arr.length; j++)
      cipher += cList.get(j);

    splitResult = splitTextIntoBlocks(cipher, rNum, blockCount);

    Text c = lang.newText(new Offset(0, 20, "cBlocks",
        AnimalScript.DIRECTION_SW), "c = ", "c", null, textProps);

    arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(192, 192,
        192));
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(
        255, 20, 147));
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(0,
        250, 154));
    StringArray cArr = lang
        .newStringArray(new Offset(8, 0, "c", AnimalScript.DIRECTION_NE),
            splitResult.split, "cArr", null, arrayProps);

    Text text1 = lang.newText(
        new Offset(0, 40, "c", AnimalScript.DIRECTION_SW),
        "Zusammengesetzt ergibt sich daraus unser Chiffrat c = " + cipher,
        "text1", null, textProps);
    Text text2 = lang.newText(new Offset(0, 5, "text1",
        AnimalScript.DIRECTION_SW), "aus dem Klartext m = " + msg, "text2",
        null, textProps);

    lang.nextStep();

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(138, 43,
        226));
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.BOLD, 18));
    Text text3 = lang.newText(new Offset(0, 20, "text2",
        AnimalScript.DIRECTION_SW),
        "Jetzt sind wir mit der Verschluesselung fertig.", "text3",
        new TicksTiming(350), textProps);
    Text text4 = lang.newText(new Offset(0, 10, "text3",
        AnimalScript.DIRECTION_SW),
        "Um die Korrektheit des Vefahrens zu demonstrieren,", "text4",
        new TicksTiming(400), textProps);
    Text text5 = lang.newText(new Offset(0, 10, "text4",
        AnimalScript.DIRECTION_SW),
        "dechiffrieren wir im Folgenden das zuvor berechnete Chiffrat.",
        "text3", new TicksTiming(400), textProps);

    cBlocks.hide(new TicksTiming(350));
    c.hide();
    cArr.hide();
    text1.hide();
    text2.hide();
    text3.hide();
    text4.hide();
    text5.hide();

    CFB2 decrypt = new CFB2(this.lang);
    decrypt.cfb(cipher, nNum, rNum, initVector, eFunc);
  }

  /**
   * splits the given String m in blocks of length r
   * 
   * @param m
   *          given String to split
   * @param r
   *          to split in blocks of length r
   * @param blockCount
   *          number of blocks
   * 
   * @return object containing the array and the number of missing zeros in the
   *         last block
   */
  private SplitReturn splitTextIntoBlocks(String m, int r, int blockCount) {
    String[] blocks = new String[blockCount];
    for (int i = 0; i < blocks.length; i++)
      blocks[i] = "";

    int block = 0;
    int count = 0;

    for (int i = 0; i < m.length(); i++) {
      blocks[block] += m.charAt(i);
      count++;
      if (count == r) {
        count = 0;
        block++;
      }
    }

    SplitReturn result = new SplitReturn(null,
        blocks[blocks.length - 1].length());

    // check if last block has r elements
    if (blocks[blocks.length - 1].length() < r) {
      int miss = r - (blocks[blocks.length - 1].length());
      for (int i = 0; i < miss; i++) {
        blocks[blocks.length - 1] += "_";
        System.out.println(blocks[blocks.length - 1]);
      }

    }

    result.split = blocks;
    return result;
  }

  /**
   * XORs 2 strings
   * 
   * @param m
   *          first String
   * @param t
   *          second String
   * 
   * @return both strings XORed
   */
  private String xor(String m, String t) {
    String result = "";
    for (int i = 0; i < m.length(); i++) {
      switch (m.charAt(i)) {
        case ('0'):
          if (t.charAt(i) == '0')
            result += "0";
          else
            result += "1";
          break;
        case ('1'):
          if (t.charAt(i) == '0')
            result += "1";
          else
            result += "0";
          break;
      }
    }

    return result;
  }

  protected String getAlgorithmDescription() {
    return ALGORITHMDESCRIPTION;
  }

  public String getName() {
    return "CFB Mode";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  class SplitReturn {

    String[] split;
    int      missing;

    public SplitReturn(String[] split, int missing) {
      this.split = split;
      this.missing = missing;
    }
  }

  public static void main(String[] args) {
    // Create a new animation
    // name, author, screen width, screen height

    Language l = new AnimalScript("CFB Mode",
        "Michelle Walther, Steffen Heger", 1000, 1000);
    CFB o = new CFB(l);

    // Example where E is a Permutation
    // for more Details about E see E.java
    E e1 = new E() {

      @Override
      public String encrypt(String i_i) {
        // (1 2 3 4)
        // (2 3 4 1)
        StringBuffer sb = new StringBuffer();
        sb.append(i_i.charAt(1)).append(i_i.charAt(2)).append(i_i.charAt(3))
            .append(i_i.charAt(0));

        return sb.toString();
      }

      @Override
      public Object stringRepresentation() {
        return new String[] { "2", "3", "4", "1" };
      }

      @Override
      public boolean isPermutation() {
        return true;
      }
    };

    // // Example E where E is no Permutation
    // // for more Details about E see E.java
    // E e2 = new E() {
    //
    // @Override
    // public Object stringRepresentation() {
    // return "m mod 128";
    // }
    //
    // @Override
    // public boolean isPermutation() {
    // return false;
    // }
    //
    // @Override
    // public String encrypt(String i_i) {
    // return i_i;
    // }
    // };
    // start
    o.cfb("10110001010010", 4, 3, "1010", e1);
    System.out.println(l);
  }

}
