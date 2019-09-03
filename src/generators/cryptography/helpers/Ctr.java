package generators.cryptography.helpers;

import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class Ctr {

  /**
   * The concrete language object used for creating output
   */
  private Language   lang;

  private String     message_m;

  private String     blocksize_n;

  private String     counter;

  private int        ctr_inc, paddedZeros;

  private Text       init_counter, function;

  private String[]   array;                 // , cipher_array;
  private int[]      postPerm;
  private SourceCode pseudoCode;
  private Square     square;
  private Circle     xor_circle;
  private ArrayMarker messageMarker, cipherMarker; // , minMarker;

  // properties
  private TextProperties headerProps, normalTextProps, boldTextProps,
      monoTextProps, monoTextGreenProps, italicRedProps, monoBoldTextProps,
      monoTextBigProps, italicBlackProps;
  private RectProperties hRectProps, auxRectProps;
  private SourceCodeProperties descriptionProps, sourceCodeProps;
  private ArrayProperties      arrayProps;
  private PolylineProperties   polyProps, polyProps1;

  public Ctr(Language l, String message_m, String blocksize_n, String counter,
      int[] postPerm, int ctr_inc) {
    // Store the language object
    lang = l;
    this.message_m = message_m;
    this.blocksize_n = blocksize_n;
    this.counter = counter;
    this.postPerm = postPerm;
    this.ctr_inc = ctr_inc;

    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  public void setProperties(TextProperties headerProps,
      RectProperties hRectProps, TextProperties normalTextProps,
      RectProperties auxRectProps, TextProperties boldTextProps,
      SourceCodeProperties descriptionProps, TextProperties monoTextProps,
      TextProperties monoTextGreenProps, ArrayProperties arrayProps,
      TextProperties italicProps, TextProperties monoBoldTextProps,
      TextProperties monoTextBigProps, TextProperties italicBlackProps,
      PolylineProperties polyProps, PolylineProperties polyProps1,
      SourceCodeProperties sourceCodeProps) {
    this.headerProps = headerProps;
    this.hRectProps = hRectProps;
    this.normalTextProps = normalTextProps;
    this.auxRectProps = auxRectProps;
    this.boldTextProps = boldTextProps;
    this.descriptionProps = descriptionProps;
    this.monoTextProps = monoTextProps;
    this.monoTextGreenProps = monoTextGreenProps;
    this.arrayProps = arrayProps;
    this.italicRedProps = italicProps;
    this.monoBoldTextProps = monoBoldTextProps;
    this.monoTextBigProps = monoTextBigProps;
    this.italicBlackProps = italicBlackProps;
    this.polyProps = polyProps;
    this.polyProps1 = polyProps1;
    this.sourceCodeProps = sourceCodeProps;

  }

  public void drawDescription() throws IllegalDirectionException {
    // header
    Text header = lang.newText(Node.convertToNode(new Point(400, 30)),
        "Counter Mode (CTR)", "header", null, headerProps);

    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null,
        hRectProps);

    lang.nextStep("Introduction");

    // Introduction

    Text auxKnowledge1 = lang
        .newText(
            Node.convertToNode(new Point(50, 130)),
            "A block cipher is an efficient algorithm that often uses a (strong,) keyed pseudorandom permutation to match blocks of a specific length on blocks of the same fixed length. A mode",
            "", null, normalTextProps);
    Text auxKnowledge2 = lang
        .newText(
            Node.convertToNode(new Point(50, 155)),
            "of operation is essentially a way of encrypting arbitrary-length messages using a block cipher. Note that arbitrary-length messages can be unambiguously padded to a total",
            "", null, normalTextProps);
    Text auxKnowledge3 = lang
        .newText(
            Node.convertToNode(new Point(50, 180)),
            "length that is a multiple of any desired block size by using specific padding techniques. Like OFB, counter mode can be viewed as a way of generating a pseudorandom stream",
            "", null, normalTextProps);
    Text auxKnowledge4 = lang.newText(Node.convertToNode(new Point(50, 205)),
        "from a block cipher.", "", null, normalTextProps);
    Text auxKnowledge5 = lang
        .newText(
            Node.convertToNode(new Point(50, 248)),
            "Two of the most renowned representatives as block ciphers are Data Encryption Standard (DES) and Advanced Encryption Standard (AES), whereat the former algorithm deemed",
            "", null, normalTextProps);
    Text auxKnowledge6 = lang.newText(Node.convertToNode(new Point(50, 273)),
        "to be broken, however the latter algorithm is in common use.", "",
        null, normalTextProps);

    Rect auxRect = lang.newRect(new Offset(-5, -5, auxKnowledge1,
        AnimalScript.DIRECTION_NW), new Offset(15, 150, auxKnowledge1,
        AnimalScript.DIRECTION_SE), "", null, auxRectProps);

    lang.nextStep("Step by Step Description");

    auxKnowledge1.hide();
    auxKnowledge2.hide();
    auxKnowledge3.hide();
    auxKnowledge4.hide();
    auxKnowledge5.hide();
    auxKnowledge6.hide();
    auxRect.hide();

    Text stepHeader = lang.newText(Node.convertToNode(new Point(10, 130)),
        "Description step by step:", "", null, boldTextProps);
    SourceCode description = lang.newSourceCode(
        Node.convertToNode(new Point(10, 150)), "description", null,
        descriptionProps);

    description.addCodeLine("Initialization:", null, 0, null);
    description.addCodeLine(" ", null, 0, null);
    description.addCodeLine("1. Choose a message m to encrypt.", null, 0, null);
    description.addCodeLine(" ", null, 0, null);
    description.addCodeLine(
        "2. Choose a function F_k (often pseudorandom permutation)", null, 0,
        null);
    description.addCodeLine(" ", null, 0, null);
    description.addCodeLine(
        "3. Choose a number ctr := {1,0}^n (n is a natural number)", null, 0,
        null);
    description.addCodeLine(" ", null, 0, null);
    description.addCodeLine("4. Split the message into blocks of size n.",
        null, 0, null);
    description.addCodeLine(" ", null, 0, null);
    description.addCodeLine("Calculation:", null, 0, null);
    description.addCodeLine(" ", null, 0, null);
    description.addCodeLine("for all blocks m_i:", null, 0, null);
    description.addCodeLine(" ", null, 0, null);
    description.addCodeLine(
        "calculate stream r_i := F((ctr + i) mod 2^n) XOR m_i", null, 0, null);
    description.addCodeLine(" ", null, 0, null);

    Rect descRect = lang.newRect(new Offset(-5, -5, description,
        AnimalScript.DIRECTION_NW), new Offset(15, 15, description,
        AnimalScript.DIRECTION_SE), "pseudoRect", null, auxRectProps);

    lang.nextStep("pseudo code");

    Text pseudoCodeHeader = lang.newText(
        Node.convertToNode(new Point(400, 130)), "Pseudocode:", "", null,
        boldTextProps);
    pseudoCode = lang.newSourceCode(Node.convertToNode(new Point(400, 150)),
        "pseudoCode", null, sourceCodeProps);

    pseudoCode.addCodeLine(" ", null, 0, null);
    pseudoCode.addCodeLine("counterMode(m, n){", null, 0, null);
    pseudoCode.addCodeLine(" ", null, 0, null);
    pseudoCode.addCodeLine("ctr = chooseRandomNumber(n)", null, 1, null);
    pseudoCode.addCodeLine(" ", null, 0, null);
    pseudoCode.addCodeLine("blocks = splitMessage(m, n)", null, 1, null);
    pseudoCode.addCodeLine(" ", null, 0, null);
    pseudoCode.addCodeLine("for(i = 0; i < blocks.length; i++) {", null, 1,
        null);
    pseudoCode.addCodeLine(" ", null, 0, null);
    pseudoCode.addCodeLine("  tmp = F(ctr)", null, 3, null);
    pseudoCode.addCodeLine(" ", null, 0, null);
    pseudoCode.addCodeLine("  cipher[i] =  blocks[i] XOR tmp[0:(n-1)]", null,
        3, null);
    pseudoCode.addCodeLine(" ", null, 0, null);
    pseudoCode.addCodeLine(
        "// the amount to increase ctr is arbitrary but constant", null, 1,
        null);
    pseudoCode.addCodeLine(" ", null, 0, null);
    pseudoCode.addCodeLine("  ctr = ctr + 1", null, 1, null);
    pseudoCode.addCodeLine(" ", null, 0, null);
    pseudoCode.addCodeLine("}", null, 1, null);

    Rect pseudoRect = lang.newRect(new Offset(-5, -5, pseudoCode,
        AnimalScript.DIRECTION_NW), new Offset(15, 15, pseudoCode,
        AnimalScript.DIRECTION_SE), "pseudoRect", null, auxRectProps);

    lang.nextStep("Initialization");

    stepHeader.hide();
    description.hide();
    descRect.hide();

    pseudoCode.moveBy(null, -390, -20, null, null);
    pseudoRect.moveBy(null, -390, -10, null, null);
    pseudoCodeHeader.moveBy(null, -395, 0, null, null);

    lang.nextStep("Initialisation1");

    pseudoCode.highlight(1);
    Timing defaultTiming = new TicksTiming(150);

    Text m_label = lang.newText(Node.convertToNode(new Point(10, 470)), "m = ",
        "", null, monoTextGreenProps);
    m_label.hide();
    m_label.show(defaultTiming);
    Text init_m = lang.newText(Node.convertToNode(new Point(40, 470)),
        message_m, "", null, monoTextGreenProps);
    init_m.hide();
    init_m.show(defaultTiming);
    Text n_label = lang.newText(Node.convertToNode(new Point(10, 490)), "n = ",
        "", null, monoTextGreenProps);
    n_label.hide();
    n_label.show(defaultTiming);
    Text init_n = lang.newText(Node.convertToNode(new Point(40, 490)),
        blocksize_n, "", null, monoTextGreenProps);
    init_n.hide();
    init_n.show(defaultTiming);

    lang.nextStep("Initialisation2");

    pseudoCode.unhighlight(1);
    m_label.hide();
    init_m.hide();
    n_label.hide();
    init_n.hide();

    m_label = lang.newText(Node.convertToNode(new Point(10, 470)), "m = ", "",
        null, monoTextProps);
    init_m = lang.newText(Node.convertToNode(new Point(40, 470)), message_m,
        "", null, monoTextProps);
    n_label = lang.newText(Node.convertToNode(new Point(10, 490)), "n = ", "",
        null, monoTextProps);
    init_n = lang.newText(Node.convertToNode(new Point(40, 490)), blocksize_n,
        "", null, monoTextProps);

    pseudoCode.highlight(3);
    Text counter_label = lang.newText(Node.convertToNode(new Point(10, 520)),
        "ctr = ", "", null, monoTextGreenProps);
    counter_label.hide();
    counter_label.show(defaultTiming);
    init_counter = lang.newText(Node.convertToNode(new Point(50, 520)),
        counter, "", null, monoTextGreenProps);
    init_counter.hide();
    init_counter.show(defaultTiming);

    lang.nextStep("Initialisation3");

    counter_label.hide();
    init_counter.hide();
    pseudoCode.unhighlight(3);
    pseudoCode.highlight(5);
    counter_label = lang.newText(Node.convertToNode(new Point(10, 520)),
        "ctr = ", "", null, monoTextProps);
    init_counter = lang.newText(Node.convertToNode(new Point(50, 520)),
        counter, "", null, monoTextProps);
    Text array_label = lang.newText(Node.convertToNode(new Point(10, 550)),
        "blocks: ", "", null, monoTextGreenProps);
    StringArray array = lang.newStringArray(
        Node.convertToNode(new Point(70, 550)),
        drawArray(message_m, blocksize_n), "array", null, arrayProps);

    lang.nextStep("Fill Array");

    fillArray(message_m, blocksize_n, array);

    lang.nextStep("padding digression");

    array_label.hide();
    array_label = lang.newText(Node.convertToNode(new Point(10, 550)),
        "blocks: ", "", null, monoTextProps);

    Text paddingDigression = lang.newText(
        Node.convertToNode(new Point(450, 130)), "Digression: Padding", "",
        null, boldTextProps);
    Text paddingDigression1 = lang.newText(
        Node.convertToNode(new Point(450, 155)),
        "The modes of operation require messages whose length", "", null,
        monoBoldTextProps);
    Text paddingDigression2 = lang.newText(
        Node.convertToNode(new Point(450, 180)),
        "is a multiple of the block size, so messages have to", "", null,
        monoBoldTextProps);
    Text paddingDigression3 = lang.newText(
        Node.convertToNode(new Point(450, 205)),
        "be padded to bring them to this length. One popular", "", null,
        monoBoldTextProps);
    Text paddingDigression4 = lang.newText(
        Node.convertToNode(new Point(450, 230)),
        "method ist to fill the last block with a single 1-bit", "", null,
        monoBoldTextProps);
    Text paddingDigression5 = lang.newText(
        Node.convertToNode(new Point(450, 255)), "followed by zero bits.", "",
        null, monoBoldTextProps);

    Rect digressionRect = lang.newRect(new Offset(-5, -5, paddingDigression1,
        AnimalScript.DIRECTION_NW), new Offset(315, 15, paddingDigression5,
        AnimalScript.DIRECTION_SE), "digressionRect", null, auxRectProps);

    lang.nextStep("function init");

    paddingDigression.hide();
    paddingDigression1.hide();
    paddingDigression2.hide();
    paddingDigression3.hide();
    paddingDigression4.hide();
    paddingDigression5.hide();
    digressionRect.hide();

    drawPermutation();

    Text funcInfo1 = lang
        .newText(
            new Offset(0, 80, counter_label, "SW"),
            "The numbers in the first row are the bit-positions of the input block before applying the function (in this case a simple permutation) to the input block,",
            "", null, italicBlackProps);
    Text funcInfo2 = lang.newText(new Offset(0, 100, counter_label, "SW"),
        "the second row represents the new bit order.", "", null,
        italicBlackProps);

    lang.nextStep("table and graphic");

    funcInfo1.hide();
    funcInfo2.hide();
    pseudoCode.unhighlight(5);

    Text calculation = lang.newText(Node.convertToNode(new Point(400, 580)),
        "Calculation:", "", null, monoBoldTextProps);
    drawTable();

    init_counter.hide();
    counter_label.hide();
    init_counter = lang.newText(Node.convertToNode(new Point(420, 250)),
        counter, "", null, monoTextProps);
    counter_label = lang.newText(Node.convertToNode(new Point(380, 253)),
        "ctr = ", "", null, monoTextProps);
    array_label.moveBy("translate", 600, -420, null, null);
    array.moveBy("translate", 600, -420, null, null);

    lang.newPolyline(new Node[] { new Offset(150, 12, init_counter, "NW"),
        new Offset(240, 12, init_counter, "NW") }, "graphic1", null, polyProps);
    square = lang.newSquare(new Offset(5, -30, "graphic1", "NE"), 60, "", null);
    lang.newText(Node.convertToNode(new Point(675, 255)), "F(x)", "func1",
        null, monoBoldTextProps);

    lang.newPolyline(new Node[] { new Offset(240, -100, "graphic1", "NW"),
        new Offset(240, -20, "graphic1", "NW") }, "graphic2", null, polyProps);
    lang.newPolyline(new Node[] { new Offset(5, 30, square, "NE"),
        new Offset(65, 30, square, "NE") }, "graphic3", null, polyProps);

    xor_circle = lang.newCircle(Node.convertToNode(new Point(810, 265)), 15,
        "", null);
    lang.newPolyline(new Node[] { new Offset(0, 0, xor_circle, "NE"),
        new Offset(0, 0, xor_circle, "SW") }, "circle-line1", null, polyProps1);
    lang.newPolyline(new Node[] { new Offset(0, 0, xor_circle, "NW"),
        new Offset(0, 0, xor_circle, "SE") }, "circle-line2", null, polyProps1);

    lang.newPolyline(new Node[] { new Offset(5, 15, xor_circle, "NE"),
        new Offset(65, 15, xor_circle, "NE") }, "graphic4", null, polyProps);

    Text cipher_array_label = lang.newText(
        Node.convertToNode(new Point(900, 250)), "cipher:", "", null,
        monoTextProps);

    StringArray cipher_array = lang.newStringArray(
        Node.convertToNode(new Point(950, 250)),
        drawArray(message_m, blocksize_n), "cipher_array", null, arrayProps);

    lang.nextStep("Calculation");

    messageMarker = lang.newArrayMarker(array, 0, "messageMarker", null);
    messageMarker.moveBy("translate", 600, -420, null, null);
    cipherMarker = lang.newArrayMarker(cipher_array, 0, "cipherMarker", null);
    calculate(cipher_array, array);

    lang.addLine("hide \"table\" 1");
    calculation.hide();
    init_m.hide();
    init_n.hide();
    hidePermutation();
    m_label.hide();
    n_label.hide();
    pseudoCodeHeader.hide();
    pseudoRect.hide();
    pseudoCode.hide();
    array.hide();
    array_label.hide();
    xor_circle.hide();
    square.hide();
    lang.addLine("hide \"graphic1\" 1");
    lang.addLine("hide \"graphic2\" 1");
    lang.addLine("hide \"graphic3\" 1");
    lang.addLine("hide \"graphic4\" 1");
    lang.addLine("hide \"circle-line1\" 1");
    lang.addLine("hide \"circle-line2\" 1");
    lang.addLine("hide \"func1\" 1");
    counter_label.hide();
    init_counter.hide();

    cipher_array.moveBy(null, -550, -100, null, null);
    cipher_array_label.moveBy(null, -550, -100, null, null);
    cipherMarker.hide();

    lang.newText(
        Node.convertToNode(new Point(250, 70)),
        "Finally we are done with encrypting our message with the following result:",
        "", null, normalTextProps);
    lang.newText(Node.convertToNode(new Point(10, 200)),
        "Additional information:", "", null, boldTextProps);
    Text finText3 = lang.newText(Node.convertToNode(new Point(10, 220)),
        "The decryption of the cipher works analogous to the encryption.", "",
        null, normalTextProps);
    lang.newText(Node.convertToNode(new Point(10, 240)),
        "You need the same counter values which were used for the encryption",
        "", null, normalTextProps);
    lang.newText(Node.convertToNode(new Point(10, 260)),
        "and apply the same function to the counter value to get a temporary",
        "", null, normalTextProps);
    lang.newText(Node.convertToNode(new Point(10, 280)),
        "key. Finally use the XOR-function to combine each cipher block with",
        "", null, normalTextProps);
    lang.newText(Node.convertToNode(new Point(10, 300)),
        "its corresponding temporary key to get the original message blocks.",
        "", null, normalTextProps);

    lang.newRect(new Offset(-5, -0, finText3, AnimalScript.DIRECTION_NW),
        new Offset(50, 95, finText3, AnimalScript.DIRECTION_SE), "", null,
        auxRectProps);

  }

  public void calculate(StringArray cipher_array, StringArray array) {
    String tmp, cipher, currentMessageBlock;
    messageMarker.move(0, null, null);
    cipherMarker.move(0, null, null);

    for (int i = 0; i < array.getLength(); i++) {
      messageMarker.move(i, null, null);

      pseudoCode.unhighlight(15);
      pseudoCode.highlight(7);
      lang.addLine("setGridValue \"table[" + (i + 1) + "][0]\" \"" + i + "\"");
      array.highlightCell(i, new TicksTiming(50), null);
      lang.addLine("setGridValue \"table[" + (i + 1) + "][1]\" \""
          + array.getData(i) + "\" after 200 ticks");
      lang.addLine("highlightGridCell \"table[" + (i + 1)
          + "][1]\" after 250 ticks");
      lang.addLine("unhighlightGridCell \"table[" + (i + 1)
          + "][1]\" after 500 ticks");
      lang.addLine("setGridValue \"table[" + (i + 1) + "][2]\" \"" + counter
          + "\" after 450 ticks");
      lang.addLine("highlightGridCell \"table[" + (i + 1)
          + "][2]\" after 450 ticks");
      lang.addLine("unhighlightGridCell \"table[" + (i + 1)
          + "][2]\" after 700 ticks");

      lang.nextStep("step1");

      pseudoCode.unhighlight(7);
      pseudoCode.highlight(9);
      tmp = permutate(counter);
      lang.addLine("setGridValue \"table[" + (i + 1) + "][3]\" \"" + tmp
          + "\" after 50 ticks");
      lang.addLine("highlightGridCell \"table[" + (i + 1)
          + "][3]\" after 50 ticks");
      square.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.YELLOW,
          new TicksTiming(50), new TicksTiming(300));
      square.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
          new TicksTiming(350), new TicksTiming(300));

      lang.addLine("unhighlightGridCell \"table[" + (i + 1)
          + "][3]\" after 550 ticks");

      lang.nextStep("step2");

      pseudoCode.unhighlight(9);
      pseudoCode.highlight(11);
      lang.addLine("highlightGridCell \"table[" + (i + 1)
          + "][3]\" after 50 ticks");
      lang.addLine("highlightGridCell \"table[" + (i + 1)
          + "][1]\" after 50 ticks");
      xor_circle.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.YELLOW,
          new TicksTiming(50), new TicksTiming(300));
      xor_circle.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
          new TicksTiming(350), new TicksTiming(300));
      xor_circle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, Color.YELLOW,
          new TicksTiming(50), new TicksTiming(300));
      xor_circle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, Color.WHITE,
          new TicksTiming(350), new TicksTiming(300));

      lang.addLine("unhighlightGridCell \"table[" + (i + 1)
          + "][3]\" after 300 ticks");
      lang.addLine("unhighlightGridCell \"table[" + (i + 1)
          + "][1]\" after 300 ticks");
      lang.addLine("highlightGridCell \"table[" + (i + 1)
          + "][4]\" after 350 ticks");
      cipherMarker.move(i, null, null);
      cipher_array.highlightCell(i, new TicksTiming(350), null);

      if (i == ((int) (array.getLength()) / 2)) {
        TrueFalseQuestionModel tf1 = new TrueFalseQuestionModel("tf1", true, 10);
        tf1.setPrompt("Is it possible to speed up the whole encryption by parallelizing the calculation of the cipher blocks?");
        tf1.setCorrectAnswer(true);
        tf1.setGroupID("qgroup");
        lang.addTFQuestion(tf1);
        lang.nextStep();
        lang.nextStep();
      }

      currentMessageBlock = array.getData(i);
      cipher = xor(num2arr(tmp), num2arr(currentMessageBlock));
      lang.addLine("setGridValue \"table[" + (i + 1) + "][4]\" \"" + cipher
          + "\" after 450 ticks");
      cipher_array.put(i, cipher, new TicksTiming(350), null);
      lang.addLine("unhighlightGridCell \"table[" + (i + 1)
          + "][4]\" after 600 ticks");

      if (i < array.getLength() - 1) {
        lang.nextStep("step 3");

        pseudoCode.unhighlight(11);

        pseudoCode.highlight(15);
        lang.addLine("highlightGridCell \"table[" + (i + 1)
            + "][2]\" after 50 ticks");

        incCounter(counter);
        init_counter.hide();
        init_counter = lang.newText(Node.convertToNode(new Point(420, 253)),
            counter, "", null, monoTextProps);

        lang.addLine("unhighlightGridCell \"table[" + (i + 1)
            + "][2]\" after 300 ticks");
        lang.addLine("highlightGridCell \"table[" + (i + 2)
            + "][2]\" after 300 ticks");
        lang.addLine("setGridValue \"table[" + (i + 2) + "][2]\" \"" + counter
            + "\" after 300 ticks");
        lang.addLine("unhighlightGridCell \"table[" + (i + 2)
            + "][2]\" after 550 ticks");

      }
      lang.nextStep("round " + i);

    }

  }

  public void incCounter(String counter) {
    int[] tmp = num2arr(counter);
    Integer num = 0;
    for (int i = tmp.length - 1; i >= 0; i--) {
      if (tmp[i] == 1) {
        num = num + (int) Math.pow(2, tmp.length - 1 - i);
      }
    }
    num += ctr_inc;
    String asdf = Integer.toBinaryString(num);
    this.counter = asdf.substring(0, counter.length());

  }

  public String permutate(String ctr) {
    char[] tmp = ctr.toCharArray();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < tmp.length; i++) {
      sb.append(tmp[postPerm[i]]);
    }
    return sb.toString();
  }

  public static long arr2num(int[] arr) {
    long result = 0;
    for (int i = arr.length - 1; i >= 0; i--) {
      assert (arr[i] == 0 || arr[i] == 1);
      if (arr[i] == 1) {
        result += Math.pow(2, arr.length - i - 1);
      }
    }
    return result;
  }

  public static int[] num2arr(String num) {
    // Write bits to ArrayList.
    char[] tmp = num.toCharArray();
    ArrayList<Integer> arr = new ArrayList<Integer>();
    for (int i = 0; i < tmp.length; i++) {
      if ((tmp[i] & 1) == 1) {

        arr.add(1);

      } else {
        arr.add(0);

      }
    }

    // Convert array list to Integer array.
    Integer[] tmp_arr = new Integer[arr.size()];
    arr.toArray(tmp_arr);

    // Convert Integer array to int array.
    int[] result = new int[arr.size()];
    for (int i = arr.size() - 1; i >= 0; i--) {
      result[arr.size() - i - 1] = tmp_arr[arr.size() - i - 1];
    }

    return result;
  }

  public static String xor(int bit1, int bit2) {
    assert (bit1 == 0 || bit1 == 1);
    assert (bit2 == 0 || bit2 == 1);
    return !(bit1 == bit2) ? "1" : "0";
  }

  public static String xor(int[] bitStr1, int[] bitStr2) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < bitStr2.length; i++) {
      sb.append(xor(bitStr1[i], bitStr2[i]));
    }
    return sb.toString();
  }

  public void drawTable() {
    lang.addLine("grid \"table\" (400,610) lines " + (array.length + 1)
        + " columns 5 style table cellWidth 100 cellHeight 20");
    lang.addLine("setGridValue \"table[0][0]\" \"i\"");
    lang.addLine("setGridValue \"table[0][1]\" \"blocks[i]\"");
    lang.addLine("setGridValue \"table[0][2]\" \"ctr\"");
    lang.addLine("setGridValue \"table[0][3]\" \"tmp\"");
    lang.addLine("setGridValue \"table[0][4]\" \"cipher[i]\"");

  }

  public void drawPermutation() {
    int tmp_n = postPerm.length;
    function = lang.newText(Node.convertToNode(new Point(5, 580)), "F(x) = ",
        "", null, monoTextBigProps);
    for (int i = 0; i < tmp_n; i++) {
      lang.addLine("text \"f[0]["
          + i
          + "]\" \""
          + i
          + "\" at ("
          + (60 + 20 * (i + 1))
          + ", 570) color black depth 1 font Monospaced size 16 after 250 ticks");
      lang.addLine("text \"f[1]["
          + i
          + "]\" \""
          + postPerm[i]
          + "\" at ("
          + (60 + 20 * (i + 1))
          + ", 590) color black depth 1 font Monospaced size 16 after 250 ticks");
    }
  }

  public void hidePermutation() {
    int tmp_n = postPerm.length;
    function.hide();
    for (int i = 0; i < tmp_n; i++) {
      lang.addLine("hide \"f[0][" + i + "]\" 1");
      lang.addLine("hide \"f[1][" + i + "]\" 1");
    }

  }

  public void padding(int pos, String s, int n, StringArray array) {

    Text padding1 = lang.newText(Node.convertToNode(new Point(10, 590)),
        "As you can see, the message is not long enough to be divided", "",
        null, italicRedProps);
    Text padding2 = lang.newText(Node.convertToNode(new Point(1, 610)),
        "into complete blocks with length of n. We will use a simple", "",
        null, italicRedProps);
    Text padding3 = lang.newText(Node.convertToNode(new Point(1, 630)),
        "padding strategy to solve this problem (Fill with zeros).", "", null,
        italicRedProps);
    paddedZeros = 0;
    String s2 = s;
    for (int i = s2.length(); i < n; i++) {
      s2 = s2 + "0";
      paddedZeros++;

      // lang.nextStep("padding step" + i);

    }
    MultipleChoiceQuestionModel mc1 = new MultipleChoiceQuestionModel("q1");
    mc1.setPrompt("How many zeros need to be padded to match the blocksize?");
    mc1.addAnswer("" + (paddedZeros + 1), 0, "Wrong, you need to add "
        + paddedZeros + " zeros");
    mc1.addAnswer("" + paddedZeros, 10, "Korrekt!");
    mc1.addAnswer("" + (paddedZeros + 2), 0, "Wrong, you need to add "
        + paddedZeros + " zeros");
    mc1.setGroupID("qgroup");
    lang.addMCQuestion(mc1);
    lang.nextStep();
    array.put(pos, s2, new TicksTiming(400), null);
    array.unhighlightCell(pos, new TicksTiming(600), null);
    padding1.hide(new TicksTiming(800));
    padding2.hide(new TicksTiming(800));
    padding3.hide(new TicksTiming(800));

  }

  public void fillArray(String m, String n, StringArray array) {
    int tmp_n = Integer.parseInt(n);
    double test = (double) m.length() / (double) tmp_n;
    // int tmp = array.getLength();
    int tmp = m.length() / tmp_n;
    if (test - tmp > 0) {
      tmp += 1;
    }
    for (int i = 0; i < tmp; i++) {
      if ((i + 1) * tmp_n < m.length()) {
        // String tmpstring = m.substring(i*tmp_n, (i+1)*tmp_n);
        array.put(i, m.substring(i * tmp_n, (i + 1) * tmp_n), null, null);
        array.highlightCell(i, null, null);
        lang.nextStep("Fillstep: " + i);
        array.unhighlightCell(i, null, null);

      } else {

        // array.put(i, m.substring(i* tmp_n), null, null);
        array.highlightCell(i, null, null);

        padding(i, m.substring(i * tmp_n), tmp_n, array);

        // array.put(i, padding(m.substring(i*tmp_n), tmp_n), null, null);
        // array[i] = padding(m.substring(i*tmp_n), tmp_n);

      }
    }
  }

  public String[] drawArray(String m, String n) {
    int tmp_n = Integer.parseInt(n);
    double test = (double) m.length() / (double) tmp_n;
    int tmp = m.length() / tmp_n;
    if (test - tmp > 0) {
      tmp += 1;
    }
    array = new String[tmp];
    for (int i = 0; i < tmp; i++) {
      for (int j = 0; j < tmp_n; j++) {
        if (array[i] != null) {
          array[i] = array[i] + "_";
        } else
          array[i] = "_";

      }

    }
    return array;

  }

}
