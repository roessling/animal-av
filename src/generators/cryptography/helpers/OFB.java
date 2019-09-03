package generators.cryptography.helpers;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.cryptography.OFBGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * 
 * @author Christian Feier & Yannick Drost
 * 
 */
public class OFB {

  /**
   * The concrete language object used for creating output
   */
  private Language     lang;

  private OFBGenerator ofbgenerator;

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   * @param ofbGenerator
   */
  public OFB(Language l, OFBGenerator ofbGenerator) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
    this.ofbgenerator = ofbGenerator;
  }

  private static final String DESCRIPTION          = "A Blockcipher is a ciphering mode to map blocks of a constant length to blocks of the same length. Modes of operation enable the repeated and secure use of\n"
                                                       + "block ciphers under a single key. One of those modes of operation is the Output Feedback Mode aka OFB Mode. The OFB makes block ciphers into a synchronous\n"
                                                       + "stream Cipher by generating keystream blocks which are then XORed with the plaintext blocks to geht the ciphertext blocks.\n"
                                                       + "OFB Mode is pretty useful to encode long texts because same blocks will be mapped on different blocks. So same blocks will never be encoded as the block before.";

  private static final String ALGORITHMDESCRIPTION = "init:\n"
                                                       + "        - choose a message m to encode\n"
                                                       + "        - choose an encryption function E. Usually E is a permutation key\n"
                                                       + "        - choose an initialvector IV in {0,1}^n. n is a natural number.\n"
                                                       + "        - choose an r with 1 <= r <= n\n"
                                                       + "        - split the given text into blocks of length r. So we get m = m_1 m_2 m_3 ... m_j.\n"
                                                       + "          Assuming block m_j has not length of r. If so, we add zeros to the end of m_j\n"
                                                       + "          until it has length of r.\n"
                                                       + "        - set I_0 = IV\n\n"
                                                       + "step 1:\n"
                                                       + "        - calculate O_i = E_k(I_i)\n\n"
                                                       + "step 2:\n"
                                                       + "        - calculate t_i. t_i are the first r bits of O_i\n\n"
                                                       + "step 3:\n"
                                                       + "        - calculate c_i = t_i xor m_i\n\n"
                                                       + "step 4:\n"
                                                       + "        - set I_i+1 = O_i and start at step 1 again until m_j is encrypted";

  private static final String SOURCECODE           = "def OFB(m, r, IV):\n"
                                                       + "    # split text into blocks of length of r\n"
                                                       + "    blocks = splitTextIntoBlocks(m, r)\n"
                                                       + "    # set init vector as I_i\n"
                                                       + "    I_i = IV\n"
                                                       + "    c = Array(blocks.length())\n\n"
                                                       + "    for block in blocks:\n"
                                                       + "        O_i = E(I_i)\n"
                                                       + "        t_i = O_i[0:r]\n"
                                                       + "        c[i] = t_i xor m_i\n\n"
                                                       + "        I_i = O_i\n\n"
                                                       + "    return c";

  public void ofb(String message, int nNum, int rNum, String initVector,
      E eFunc, RectProperties headerRectProps, SourceCodeProperties codeProp,
      ArrayProperties arrayProps, TextProperties attentionProps,
      ArrayMarkerProperties arrMarkerProps) {

    // Properties
    // header property
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    headerProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 32));

    // Calculation Label
    TextProperties calcProps = new TextProperties();
    calcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    calcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    // step by step / Pseudocode
    TextProperties stepPseudoProps = new TextProperties();
    stepPseudoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 20));
    stepPseudoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    // make Header with Rect
    Text header = lang.newText(new Coordinates(440, 50), "OFB Mode", "header",
        null, headerProps);
    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "headerRect",
        null, headerRectProps);

    // start a new step after header was printed
    lang.nextStep();

    // create Descrption with 2 rects
    SourceCode descr = lang.newSourceCode(new Coordinates(50, 100), "descr",
        null);
    descr
        .addCodeLine(
            "A Blockcipher is a ciphering mode to map blocks of a constant length to blocks of the same length. Modes of operation enable the repeated and secure use of",
            null, 0, new TicksTiming(25));
    descr
        .addCodeLine(
            "block ciphers under a single key. One of those modes of operation is the Output Feedback Mode aka OFB Mode. The OFB makes block ciphers into a synchronous",
            null, 0, new TicksTiming(10));
    descr
        .addCodeLine(
            "stream Cipher by generating keystream blocks which are then XORed with the plaintext blocks to geht the ciphertext blocks.",
            null, 0, new TicksTiming(25));
    descr
        .addCodeLine(
            "OFB Mode is pretty useful to encode long texts because same blocks will be mapped on different blocks. So same blocks will never be encoded as the block before.",
            null, 0, new TicksTiming(50));

    // inner Rect
    Rect inRect = lang.newRect(new Offset(-10, -2, "descr",
        AnimalScript.DIRECTION_NW), new Offset(10, 2, "descr",
        AnimalScript.DIRECTION_SE), "inRect", null);

    // outer Rect
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        new Color(0, 255, 255));
    Rect outRect = lang.newRect(new Offset(-11, -3, "descr",
        AnimalScript.DIRECTION_NW), new Offset(11, 3, "descr",
        AnimalScript.DIRECTION_SE), "outRect", null, rectProps);

    // start a new step description was printed
    lang.nextStep("Description");

    // make Calc Text
    Text calc = lang.newText(new Offset(0, 30, "inRect",
        AnimalScript.DIRECTION_SW), "Calculation", "calc", null, calcProps);

    // make step by step block

    Text stepDescr = lang.newText(new Offset(10, 10, "calc",
        AnimalScript.DIRECTION_SW), "step by step", "stepDescr", null,
        stepPseudoProps);

    SourceCodeProperties srcProps = new SourceCodeProperties();
    srcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));
    SourceCode steps = lang.newSourceCode(new Offset(0, 10, "stepDescr",
        AnimalScript.DIRECTION_SW), "steps", null, srcProps);
    steps.addCodeLine("init", null, 0, null);
    steps.addCodeLine("        - choose a message m to encode", null, 0, null);
    steps
        .addCodeLine(
            "        - choose an encryption function E. Usually E is a permutation key",
            null, 0, null);
    steps
        .addCodeLine(
            "        - choose an initialvector IV in {0,1}^n. n is a natural number.",
            null, 0, null);
    steps.addCodeLine("        - choose an r with 1 <= r <= n", null, 0, null);
    steps
        .addCodeLine(
            "        - split the given text into blocks of length r. So we get m = m_1 m_2 m_3 ... m_j.",
            null, 0, null);
    steps
        .addCodeLine(
            "          Assuming block m_j has not length of r. If so, we add zeros to the end of m_j",
            null, 0, null);
    steps.addCodeLine("          until it has length of r.", null, 0, null);
    steps.addCodeLine("        - set I_0 = IV", null, 0, null);
    steps.addCodeLine("", null, 0, null);
    steps.addCodeLine("step 1:", null, 0, null);
    steps.addCodeLine("        - calculate O_i = E_k(I_i)", null, 0, null);
    steps.addCodeLine("", null, 0, null);
    steps.addCodeLine("step 2:", null, 0, null);
    steps.addCodeLine(
        "        - calculate t_i. t_i are the first r bits of O_i", null, 0,
        null);
    steps.addCodeLine("", null, 0, null);
    steps.addCodeLine("step 3:", null, 0, null);
    steps.addCodeLine("        - calculate c_i = t_i xor m_i", null, 0, null);
    steps.addCodeLine("", null, 0, null);
    steps.addCodeLine("step 4:", null, 0, null);
    steps
        .addCodeLine(
            "        - set I_i+1 = O_i and start at step 1 again until m_j is encrypted",
            null, 0, null);

    Rect stepRect = lang.newRect(new Offset(-10, -5, "stepDescr",
        AnimalScript.DIRECTION_NW), new Offset(10, 5, "steps",
        AnimalScript.DIRECTION_SE), "stepRect", null);

    // make pseudocode block
    Text codeDescr = lang.newText(new Offset(600, 4, "stepDescr",
        AnimalScript.DIRECTION_NW), "in Pseudocode", "codeDescr", null,
        stepPseudoProps);

    codeProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 16));
    SourceCode code = lang.newSourceCode(new Offset(0, 0, "codeDescr",
        AnimalScript.DIRECTION_SW), "code", null, codeProp);
    code.addCodeLine("def OFB(m, r, IV):", null, 0, null);
    code.addCodeLine("# split text into blocks of length of r", null, 1, null);
    code.addCodeLine("blocks = splitTextIntoBlocks(m, r)", null, 1, null);
    code.addCodeLine("# set init vector as I_i", null, 1, null);
    code.addCodeLine("I_i = IV", null, 1, null);
    code.addCodeLine("c = Array(blocks.length())", null, 1, null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("for block in blocks:", null, 1, null);
    code.addCodeLine("O_i = E(I_i)", null, 2, null);
    code.addCodeLine("t_i = O_i[0:r]", null, 2, null);
    code.addCodeLine("c[i] = t_i xor m_i", null, 2, null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("I_i = O_i", null, 2, null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("return c", null, 0, null);

    Rect codeRect = lang.newRect(new Offset(-10, -5, "codeDescr",
        AnimalScript.DIRECTION_NW), new Offset(10, 5, "code",
        AnimalScript.DIRECTION_SE), "codeRect", null);

    // next step after printing step by step and pseudocode
    lang.nextStep();

    // hide description
    descr.hide(new TicksTiming(1000));
    inRect.hide(new TicksTiming(1000));
    outRect.hide(new TicksTiming(1000));

    stepRect.hide(new TicksTiming(1150));
    stepDescr.hide(new TicksTiming(1150));
    steps.hide(new TicksTiming(1150));

    calc.hide(new TicksTiming(1300));
    codeRect.hide(new TicksTiming(1300));
    codeDescr.hide(new TicksTiming(1300));
    code.hide(new TicksTiming(1300));

    // next step after hiding pseudocode
    lang.nextStep();

    // make m text
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));
    Text m = lang.newText(new Coordinates(40, 150), "m = " + message, "m",
        new TicksTiming(50), textProps);

    // make n text
    Text n = lang.newText(new Offset(0, 20, "m", AnimalScript.DIRECTION_SW),
        "n = " + nNum, "n", new TicksTiming(100), textProps);

    // make r text
    Text r = lang.newText(new Offset(40, 0, "n", AnimalScript.DIRECTION_NE),
        "r = " + rNum, "r", new TicksTiming(150), textProps);

    // make IV text
    Text iv = lang.newText(new Offset(40, 0, "r", AnimalScript.DIRECTION_NE),
        "iv = " + initVector, "iv", new TicksTiming(200), textProps);

    // List to save all eFunc elements
    LinkedList<Text> eList = new LinkedList<Text>();
    LinkedList<Polyline> lineList = new LinkedList<Polyline>();
    // check if E is a Permutation or a usual Function
    if (eFunc.isPermutation()) {
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
      textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.PLAIN, 16));
      Text curr0 = lang.newText(new Offset(200, 0, "m",
          AnimalScript.DIRECTION_NE), permutation[0][0], "e[0][0]",
          new TicksTiming(250), textProps);
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
      Text e = lang.newText(new Offset(-80, 0, "e[0][0]",
          AnimalScript.DIRECTION_SW), "E(x) = ", "e", new TicksTiming(250),
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
    }
    // else it is a usual function
    else {
      textProps = new TextProperties();
      textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.PLAIN, 16));
      Text func = lang.newText(new Offset(200, 0, "m",
          AnimalScript.DIRECTION_NE), (String) eFunc.stringRepresentation(),
          "func", new TicksTiming(250), textProps);
      Text e = lang.newText(new Offset(-75, 0, "func",
          AnimalScript.DIRECTION_NW), "E(m) = ", "e", new TicksTiming(250),
          textProps);
      eList.add(func);
      eList.add(e);
    }

    // next step after printing the preconditions
    lang.nextStep("init");

    // print Pseudocode
    SourceCode src = lang.newSourceCode(new Offset(0, 130, "n",
        AnimalScript.DIRECTION_SW), "src", null, codeProp);
    src.addCodeLine("def OFB(m, r, IV):", null, 0, null);
    src.addCodeLine("# split text into blocks of length of r", null, 1, null);
    src.addCodeLine("blocks = splitTextIntoBlocks(m, r)", null, 1, null);
    src.addCodeLine("# set init vector as I_i", null, 1, null);
    src.addCodeLine("I_i = IV", null, 1, null);
    src.addCodeLine("c = Array(blocks.length())", null, 1, null);
    src.addCodeLine("", null, 0, null);
    src.addCodeLine("for block in blocks:", null, 1, null);
    src.addCodeLine("O_i = E(I_i)", null, 2, null);
    src.addCodeLine("t_i = O_i[0:r]", null, 2, null);
    src.addCodeLine("c[i] = t_i xor m_i", null, 2, null);
    src.addCodeLine("", null, 0, null);
    src.addCodeLine("I_i = O_i", null, 2, null);
    src.addCodeLine("", null, 0, null);
    src.addCodeLine("return c", null, 0, null);

    // highlight line 1 in code
    src.highlight(0);

    // print table
    // between line and table element are (n+1)/2 + 10 pixel.
    int between = (nNum + 1) / 2 * 10;

    // calc number of m blocks
    int blockCount = (int) Math.ceil(new Double(message.length()) / rNum);

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
        AnimalScript.DIRECTION_NE), "O_i", "o_i", null, textProps);

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
        AnimalScript.DIRECTION_NE), "c[i]", "c_i", null, textProps);

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
    Text split = lang.newText(
        new Offset(0, 40, "n", AnimalScript.DIRECTION_SW),
        "split m in blocks of length " + rNum + ":", "split", null, textProps);

    // split Array
    Text blocks = lang.newText(new Offset(0, 40, "split",
        AnimalScript.DIRECTION_SW), "blocks = ", "blocks", null, textProps);

    // get filler
    String fill = "";
    for (int j = 0; j < rNum; j++)
      fill += "_";

    String[] input = new String[blockCount];
    for (int j = 0; j < blockCount; j++)
      input[j] = fill;

    StringArray arr = lang.newStringArray(new Offset(10, 0, "blocks",
        AnimalScript.DIRECTION_NE), input, "arr", null, arrayProps);

    src.unhighlight(0, 0, false, new TicksTiming(200), null);
    src.highlight(2, 0, false, new TicksTiming(200), null);

    // next step after creating the array
    lang.nextStep();

    // get split
    SplitReturn splitResult = splitTextIntoBlocks(message, rNum, blockCount);
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
      Text addZeros1 = lang.newText(new Offset(15, -25, "arr",
          AnimalScript.DIRECTION_NE),
          "the last block hasn't length of " + rNum, "addZeros1", null,
          attentionProps);
      Text addZeros2 = lang.newText(new Offset(0, 1, "addZeros1",
          AnimalScript.DIRECTION_SW),
          "so we add zeros until it has length of  " + rNum, "addZeros2", null,
          attentionProps);

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
    }

    // next step after splitting the array
    lang.nextStep();

    src.unhighlight(2);
    src.highlight(4);

    LinkedList<String> cList = new LinkedList<String>();

    // add 0 label and init Vektor
    // make step 0 manuell because of the positioning
    int pufferN = 5 * between / 10;
    int pufferR = 2 * between / 10;

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

    src.unhighlight(4);
    src.highlight(5);

    lang.nextStep();

    src.unhighlight(5);
    src.highlight(7);

    lang.nextStep();

    src.unhighlight(7);
    src.highlight(8);
    String o_ = eFunc.encrypt(initVector);

    CheckpointUtils.checkpointEvent(ofbgenerator, "O0Event", new Variable("O0",
        o_));

    Text o_0 = lang.newText(new Offset(-1 * pufferN, 6, "o_i",
        AnimalScript.DIRECTION_SW), o_, "o_0", null, textProps);
    tableList.add(o_0);

    lang.nextStep("step 1");

    src.unhighlight(8);
    src.highlight(9);
    String t_ = o_.substring(0, rNum);

    CheckpointUtils.checkpointEvent(ofbgenerator, "t0Event", new Variable("t0",
        t_));

    Text t_0 = lang.newText(new Offset(-1 * pufferR, 6, "t_i",
        AnimalScript.DIRECTION_SW), t_, "t_0", null, textProps);
    tableList.add(t_0);

    lang.nextStep("step 2");

    arr.highlightElem(0, null, null);
    ArrayMarker marker = lang.newArrayMarker(arr, 0, "marker", null,
        arrMarkerProps);
    String m_ = blocks_arr[0];
    Text m_0 = lang.newText(new Offset(-1 * pufferR, 6, "m_i",
        AnimalScript.DIRECTION_SW), m_, "m_0", null, textProps);
    tableList.add(m_0);

    CheckpointUtils.checkpointEvent(ofbgenerator, "m0Event", new Variable("m0",
        m_));

    lang.nextStep();

    src.unhighlight(9);
    src.highlight(10);
    String c_ = xor(t_, m_);
    cList.add(c_);
    Text c_0 = lang.newText(new Offset(-1 * pufferR, 6, "c_i",
        AnimalScript.DIRECTION_SW), c_, "c_0", null, textProps);
    tableList.add(c_0);

    CheckpointUtils.checkpointEvent(ofbgenerator, "c0Event", new Variable("c0",
        c_));

    lang.nextStep("step 3");

    src.unhighlight(10);
    src.highlight(12);
    zero = lang.newText(new Offset(0, 6, "0", AnimalScript.DIRECTION_SW), "1",
        "1", null, textProps);
    i_ = o_;
    i_0 = lang.newText(new Offset(0, 6, "i_0", AnimalScript.DIRECTION_SW), i_,
        "i_1", null, textProps);
    tableList.add(zero);
    tableList.add(i_0);
    // first row was created, now the rest
    lang.nextStep("step 4");

    for (int j = 1; j < blocks_arr.length; j++) {
      src.unhighlight(12);
      src.highlight(7);
      arr.highlightCell(j - 1, null, null);
      lang.nextStep();

      src.unhighlight(7);
      src.highlight(8);
      o_ = eFunc.encrypt(i_);
      o_0 = lang.newText(new Offset(0, 6, "o_" + (j - 1),
          AnimalScript.DIRECTION_SW), o_, "o_" + j, null, textProps);
      tableList.add(o_0);
      lang.nextStep("step 1");

      CheckpointUtils.checkpointEvent(ofbgenerator, "OiEvent", new Variable(
          "Oi", o_));

      src.unhighlight(8);
      src.highlight(9);
      t_ = o_.substring(0, rNum);
      t_0 = lang.newText(new Offset(0, 6, "t_" + (j - 1),
          AnimalScript.DIRECTION_SW), t_, "t_" + j, null, textProps);
      tableList.add(t_0);
      lang.nextStep("step 2");

      CheckpointUtils.checkpointEvent(ofbgenerator, "tiEvent", new Variable(
          "ti", t_));

      arr.highlightElem(j, null, null);
      marker.increment(null, null);
      m_ = blocks_arr[j];
      m_0 = lang.newText(new Offset(0, 6, "m_" + (j - 1),
          AnimalScript.DIRECTION_SW), m_, "m_" + j, null, textProps);
      tableList.add(m_0);
      lang.nextStep();

      CheckpointUtils.checkpointEvent(ofbgenerator, "miEvent", new Variable(
          "mi", m_));

      src.unhighlight(9);
      src.highlight(10);
      c_ = xor(t_, m_);
      cList.add(c_);
      c_0 = lang.newText(new Offset(0, 6, "c_" + (j - 1),
          AnimalScript.DIRECTION_SW), c_, "c_" + j, null, textProps);
      tableList.add(c_0);
      lang.nextStep("step 3");

      CheckpointUtils.checkpointEvent(ofbgenerator, "ciEvent", new Variable(
          "ci", c_));

      src.unhighlight(10);
      src.highlight(12);
      zero = lang.newText(new Offset(0, 6, new Integer(j).toString(),
          AnimalScript.DIRECTION_SW), new Integer(j + 1).toString(),
          new Integer(j + 1).toString(), null, textProps);
      i_ = o_;
      i_0 = lang.newText(new Offset(0, 6, "i_" + j, AnimalScript.DIRECTION_SW),
          i_, "i_" + (j + 1), null, textProps);
      tableList.add(zero);
      tableList.add(i_0);
      lang.nextStep("step 4");

      CheckpointUtils.checkpointEvent(ofbgenerator, "nextiEvent", new Variable(
          "nexti", i_));
    }

    // mark last array cell and highlight for loop
    src.unhighlight(12);
    src.highlight(7);
    arr.highlightCell(blocks_arr.length - 1, null, null);

    lang.nextStep();

    // jump to return
    src.unhighlight(7);
    src.highlight(14);
    lang.nextStep("calculation is done");

    // hide all elemets except the header
    // all table elements
    for (int j = 0; j < tableList.size(); j++)
      tableList.get(j).hide(new TicksTiming(500));
    // hide preconditions
    m.hide(new TicksTiming(500));
    n.hide(new TicksTiming(500));
    r.hide(new TicksTiming(500));
    iv.hide(new TicksTiming(500));
    // hide E
    for (int j = 0; j < eList.size(); j++)
      eList.get(j).hide(new TicksTiming(500));
    for (int j = 0; j < lineList.size(); j++)
      lineList.get(j).hide(new TicksTiming(500));
    // hide texts f or split block
    blocks.hide(new TicksTiming(500));
    split.hide(new TicksTiming(500));
    arr.hide(new TicksTiming(500));
    // hide Table and Source
    src.hide(new TicksTiming(500));
    vline1.hide(new TicksTiming(500));
    vline2.hide(new TicksTiming(500));
    lineiI_i.hide(new TicksTiming(500));
    lineI_iO_i.hide(new TicksTiming(500));
    lineO_it_i.hide(new TicksTiming(500));
    linet_im_i.hide(new TicksTiming(500));
    linem_ic_i.hide(new TicksTiming(500));
    hline.hide(new TicksTiming(500));
    i.hide(new TicksTiming(500));
    i_i.hide(new TicksTiming(500));
    o_i.hide(new TicksTiming(500));
    t_i.hide(new TicksTiming(500));
    m_i.hide(new TicksTiming(500));
    c_i.hide(new TicksTiming(500));

    lang.nextStep();

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));
    // Text cBlocks =
    lang.newText(new Coordinates(40, 150),
        "The return value is the following array:", "cBlocks", null, textProps);
    // Text c =
    lang.newText(new Offset(0, 20, "cBlocks", AnimalScript.DIRECTION_SW),
        "c = ", "c", null, textProps);

    // make ciphertext
    String cipher = "";
    String[] cListstr = new String[blocks_arr.length];
    for (int j = 0; j < blocks_arr.length; j++) {
      cipher += cList.get(j);
      cListstr[j] = cList.get(j);
    }

    CheckpointUtils.checkpointEvent(ofbgenerator, "cipherEvent", new Variable(
        "cipher", cipher));

    // arrayProps = new ArrayProperties();
    // arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new
    // Color(192,192,192));
    // arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    // arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
    // Color.GREEN);
    // arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new
    // Color(255,200,0));
    lang.newStringArray(new Offset(8, 0, "c", AnimalScript.DIRECTION_NE),
        cListstr, "cArr", null, arrayProps);

    // make ciphertext
    /*
     * String cipher = ""; for (int j = 0; j < blocks_arr.length; j++) cipher +=
     * cList.get(j);
     */

    lang.newText(new Offset(0, 40, "c", AnimalScript.DIRECTION_SW),
        "so we Get the ciphertext c = " + cipher, "text1", null, textProps);
    lang.newText(new Offset(0, 5, "text1", AnimalScript.DIRECTION_SW),
        "of the given message m = " + message, "text2", null, textProps);

    lang.nextStep("conclusion");

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 28));
    lang.newText(new Offset(0, 70, "text2", AnimalScript.DIRECTION_SW),
        "We are done! Hope you enjoyed it!", "done", new TicksTiming(400),
        textProps);

    lang.nextStep("Presentation is done");
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

    SplitReturn result = new SplitReturn(null, r
        - blocks[blocks.length - 1].length());

    // check if last block has r elements
    if (blocks[blocks.length - 1].length() < r) {
      int miss = r - (blocks[blocks.length - 1].length());
      for (int i = 0; i < miss; i++) {
        blocks[blocks.length - 1] += "_";
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

  protected String getAlgorithmCode() {
    return SOURCECODE;
  }

  public String getName() {
    return "OFB Mode";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getCodeExample() {
    return SOURCECODE;
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

    Language l = new AnimalScript(
        "OFB Mode",
        "Christian Feier <feier@rbg.informatik.tu-darmstadt.de>, Yannick Drost <drost.yannick@googlemail.com>",
        1000, 700);
    OFB o = new OFB(l, null);

    // Example where E is a Permutation
    // for more Details about E see E.java
    E e1 = new E() {

      @Override
      public String encrypt(String i_i) {
        // (1 2 3 4 5 6)
        // (4 6 1 3 2 5)

        StringBuffer sb = new StringBuffer();
        sb.append(i_i.charAt(3)).append(i_i.charAt(5)).append(i_i.charAt(0))
            .append(i_i.charAt(2)).append(i_i.charAt(1)).append(i_i.charAt(4))
            .append(i_i.charAt(7)).append(i_i.charAt(6));

        return sb.toString();
      }

      @Override
      public Object stringRepresentation() {
        return new String[] { "4", "6", "1", "3", "2", "5", "8", "7" };
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

    // header rect property
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    // code property
    SourceCodeProperties codeProps = new SourceCodeProperties();
    codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.MAGENTA);
    codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 16));

    // m and c block array property
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(192, 192,
        192));
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(
        255, 200, 0));

    // Attention lablels, e.g. addZeros
    TextProperties attentionProps = new TextProperties();
    attentionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    attentionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 16));

    // ArrayMarker property
    ArrayMarkerProperties arrMarkerProps = new ArrayMarkerProperties();
    arrMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // start
    String m = "110001011001101111000111101010101010101100101";
    o.ofb(m, 8, 5, "10101011", e1, rectProps, codeProps, arrayProps,
        attentionProps, arrMarkerProps);
    System.out.println(l);

    // boolean write = true;

    try {
      File file = new File("OFB.asu");
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));
      bw.write(l.toString());
      bw.close();

    } catch (Exception e) {

    }
  }
}