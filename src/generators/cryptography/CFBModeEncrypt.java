package generators.cryptography;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.ListElement;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.ListElementGenerator;
import algoanim.primitives.generators.RectGenerator;
import algoanim.primitives.generators.TextGenerator;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalListElementGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ListElementProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class CFBModeEncrypt implements Generator {
  private Language             lang;
  private SourceCodeProperties sourceCode;
  private int[]                permutationOrder;
  private String               initialVector;
  private String               plainText;
  private String               c;
  private Text                 textC;
  private String               r;

  public CFBModeEncrypt(Language l) {
    lang = l;
    lang.setStepMode(true);
    c = "";
  }

  public CFBModeEncrypt() {
    // TODO Auto-generated constructor stub
  }

  private void encrypt(String plain) {

    if (permutationOrder == null) {
      permutationOrder = new int[] { 1, 2, 3 };
    }

    TextGenerator textGen = new AnimalTextGenerator(lang);

    int xHeader = 20;
    int yHeader = 20;

    Node coodsTitle = new Coordinates(xHeader, yHeader);
    Timing time = new TicksTiming(0);
    TextProperties propTitle = new TextProperties();
    propTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));

    new Text(textGen, coodsTitle, "CFB-Mode Verschlüsselung", "title", time,
        propTitle);

    RectGenerator rectGen = new AnimalRectGenerator(lang);
    Node nwRect = new Coordinates(xHeader - 5, yHeader - 5);
    Node seRect = new Coordinates(310, 45);
    RectProperties rectProp = new RectProperties();
    rectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
        new Color(192, 192, 192));
    rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    new Rect(rectGen, nwRect, seRect, "titleRect", time, rectProp);

    TextProperties descrProp = new TextProperties();
    descrProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));

    Node nodeDecr01 = new Coordinates(15, 100);
    Text descr01 = new Text(
        textGen,
        nodeDecr01,
        "____________________________________________________________________________________",
        "descr01", time, descrProp);

    Node nodeDecr02 = new Coordinates(15, 125);
    Text descr02 = new Text(
        textGen,
        nodeDecr02,
        "Der CFB-Mode ist zum Verschlüsseln langer Nachrichten geeignet. Die Nachricht",
        "descr02", time, descrProp);

    Node nodeDecr03 = new Coordinates(15, 150);
    Text descr03 = new Text(
        textGen,
        nodeDecr03,
        "wird in Blöcke unterteilt und die einzelnen Blöcke werden entsprechend",
        "descr03", time, descrProp);

    Node nodeDecr04 = new Coordinates(15, 175);
    Text descr04 = new Text(textGen, nodeDecr04,
        "verschlüsselt. Die einzelnen verschlüsselten Blöcke ergeben den",
        "descr04", time, descrProp);

    Node nodeDecr05 = new Coordinates(15, 200);
    Text descr05 = new Text(textGen, nodeDecr05,
        "Geheimtext. Somit gehört der CFB-Mode zu den Blockverschlüsselungen.",
        "descr05", time, descrProp);

    Node nodeDecr06 = new Coordinates(15, 225);
    Text descr06 = new Text(textGen, nodeDecr06,
        "Dabei haben alle Blöcke eine feste gleiche Länge. ", "descr06", time,
        descrProp);

    Node nodeDecr07 = new Coordinates(15, 235);
    Text descr07 = new Text(
        textGen,
        nodeDecr07,
        "____________________________________________________________________________________",
        "descr07", time, descrProp);

    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 14));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode sc;
    if (sourceCode == null) {
      sc = lang.newSourceCode(new Coordinates(15, 400), "code", null, scProps);
    } else {
      sc = lang.newSourceCode(new Coordinates(15, 400), "code", null,
          sourceCode);
    }

    sc.addCodeLine("1. Festlegen des Initialisierungsvektors IV = {0, 1}^n",
        null, 1, null);
    sc.addCodeLine("2. Festlegen der Blockgröße r mit 1 <= r <= n", null, 1,
        null);
    sc.addCodeLine("3. Festlegen des Schlüssels E_k", null, 1, null);
    sc.addCodeLine("4. Für 1 <= j <= u :", null, 1, null);
    sc.addCodeLine("a) O_j = E_k(I_j)", null, 2, null);
    sc.addCodeLine("b) Bestimmen von t_j (die ersten r Bits von O_j)", null, 2,
        null);
    sc.addCodeLine("c) c_j = m_j XOR t_j", null, 2, null);
    sc.addCodeLine("d) I_j+1 = 2^r * I_j + c_j mod 2^n", null, 2, null);

    lang.nextStep("Einleitung");

    TextProperties properties = new TextProperties();
    properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));
    properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0, 0));

    int xPlain = 150;
    int yPlain = 90;

    Node nodePlain = new Coordinates(xPlain, yPlain);
    new Text(textGen, nodePlain, "Klartext (m): " + plain, "plainVector", time,
        properties);

    Node nodeLine01 = new Coordinates(xPlain - 130, yPlain + 15);
    new Text(textGen, nodeLine01,
        "__________________________________________________________________",
        "plainVector", time, properties);

    int xChiffrat = 150;
    int yChiffrat = 380;

    Node nodeLine03 = new Coordinates(xChiffrat - 130, yChiffrat - 25);
    new Text(textGen, nodeLine03,
        "__________________________________________________________________",
        "chiffratVector", time, properties);

    Node nodeG = new Coordinates(xChiffrat, yChiffrat);
    new Text(textGen, nodeG, "Geheimtext: ", "g", time, properties);

    Node nodeC = new Coordinates(xChiffrat + 100, yChiffrat);
    textC = new Text(textGen, nodeC, c, "cPlus", time, properties);

    descr01.hide();
    descr02.hide();
    descr03.hide();
    descr04.hide();
    descr05.hide();
    descr06.hide();
    descr07.hide();

    lang.nextStep();

    Timing wait = new TicksTiming(300);

    properties
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 0, 0));

    if (initialVector == null)
      initialVector = "101";

    Node nodeInitial = new Coordinates(30, 150);
    Text textInitial = new Text(textGen, nodeInitial, "IV: " + initialVector,
        "initialVector", wait, properties);

    sc.highlight(0);

    lang.nextStep();

    properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0, 0));
    new Text(textGen, nodeInitial, "IV: " + initialVector, "initialVector",
        wait, properties);

    textInitial.hide();

    lang.nextStep("Schritt 1");

    properties
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 0, 0));

    if (r == null)
      r = "2";

    Node nodeR = new Coordinates(200, 150);
    Text textR = new Text(textGen, nodeR, "r: " + r, "r", wait, properties);

    textR.hide();

    sc.unhighlight(0);
    sc.highlight(1);

    lang.nextStep();

    properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0, 0));
    new Text(textGen, nodeR, "r: " + r, "r", wait, properties);

    lang.nextStep("Schritt 2");

    int xKey = 400;
    int yKey = 150;

    properties
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 0, 0));

    Node nodeKey = new Coordinates(xKey, yKey);
    Text textKey = new Text(textGen, nodeKey, "E_k = (", "key", wait,
        properties);

    Node node1 = new Coordinates(xKey + 60, yKey - 10);
    Text text01 = new Text(textGen, node1, "1", "one", wait, properties);

    Node node2 = new Coordinates(xKey + 90, yKey - 10);
    Text text02 = new Text(textGen, node2, "2", "two", wait, properties);

    Node node3 = new Coordinates(xKey + 120, yKey - 10);
    Text text03 = new Text(textGen, node3, "3", "three", wait, properties);

    String nTo1 = String.valueOf(permutationOrder[0]);

    Node node4 = new Coordinates(xKey + 60, yKey + 10);
    Text text04 = new Text(textGen, node4, nTo1, "four", wait, properties);

    String nTo2 = String.valueOf(permutationOrder[1]);

    Node node5 = new Coordinates(xKey + 90, yKey + 10);
    Text text05 = new Text(textGen, node5, nTo2, "five", wait, properties);

    String nTo3 = String.valueOf(permutationOrder[2]);

    Node node6 = new Coordinates(xKey + 120, yKey + 10);
    Text text06 = new Text(textGen, node6, nTo3, "six", wait, properties);

    Node node7 = new Coordinates(xKey + 135, yKey);
    Text text07 = new Text(textGen, node7, ")", "seven", wait, properties);

    sc.unhighlight(1);
    sc.highlight(2);

    textR.hide();

    lang.nextStep();

    properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0, 0));

    new Text(textGen, nodeKey, "E_k = (", "key", wait, properties);
    new Text(textGen, node1, "1", "one", wait, properties);
    new Text(textGen, node2, "2", "two", wait, properties);
    new Text(textGen, node3, "3", "three", wait, properties);
    new Text(textGen, node4, nTo1, "four", wait, properties);
    new Text(textGen, node5, nTo2, "five", wait, properties);
    new Text(textGen, node6, nTo3, "six", wait, properties);
    new Text(textGen, node7, ")", "seven", wait, properties);

    textKey.hide();
    text01.hide();
    text02.hide();
    text03.hide();
    text04.hide();
    text05.hide();
    text06.hide();
    text07.hide();

    lang.nextStep("Schritt 3");

    Node nodeLine02 = new Coordinates(xPlain - 130, yKey + 30);
    new Text(textGen, nodeLine02,
        "-----------------------------------------------------------",
        "plainVector2", time, properties);

    lang.nextStep();

    sc.highlight(3);
    sc.unhighlight(2);

    lang.nextStep();

    sc.highlight(4);

    lang.nextStep();

    String output = plain;

    String iJ = initialVector;

    while (output.isEmpty() == false) {

      String next = "";

      int sizeR = Integer.parseInt(r);
      if (sizeR - output.length() == 0) {
        next = output;
        output = "";
      } else if (sizeR - output.length() == 1) {
        next = output + "0";
        output = "";
      } else if (sizeR - output.length() == 2) {
        next = output + "00";
        output = "";
      } else {
        next = output.substring(0, sizeR);
        output = output.substring(sizeR);
      }

      CheckpointUtils.checkpointEvent(this, "mSubstringEvent", new Variable(
          "mSubstring", next));

      iJ = nextStep(next, iJ, sizeR, sc, xChiffrat, yChiffrat);
    }

    sc.unhighlight(3);

    properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 18));
    properties
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 255, 0));

    Node nodeChiffrat = new Coordinates(xPlain - 100, yPlain + 200);
    new Text(textGen, nodeChiffrat, "Der verschüsselte Klartext: " + c,
        "chiffrat", time, properties);

    CheckpointUtils.checkpointEvent(this, "resultEvent", new Variable("result",
        c));

    lang.nextStep();

  }

  private String nextStep(String next, String iJ, int sizeR, SourceCode sc,
      int xChiffrat, int yChiffrat) {

    int xStep = 30;
    int yStep = 210;

    TextGenerator textGen = new AnimalTextGenerator(lang);

    Timing wait = new TicksTiming(300);
    TextProperties properties = new TextProperties();
    properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));
    properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0, 0));

    Node nodeNextIj = new Coordinates(xStep, yStep);
    Text nextIj = new Text(textGen, nodeNextIj, "I_j = " + iJ, "ij", wait,
        properties);

    lang.nextStep();

    CheckpointUtils.checkpointEvent(this, "nextIEvent", new Variable("nextI",
        iJ));

    String oJ = permutate(iJ);

    CheckpointUtils.checkpointEvent(this, "permutatedEvent", new Variable(
        "permutated", oJ));

    Node nodeNextOj = new Coordinates(xStep, yStep + 30);
    Text nextOj = new Text(textGen, nodeNextOj,
        "O_j = E_k(" + iJ + ") = " + oJ, "ij", wait, properties);

    lang.nextStep("Schritt 4.a");

    String tJ = oJ.substring(0, sizeR);

    CheckpointUtils.checkpointEvent(this, "permutateSubstringEvent",
        new Variable("permutatedSubstring", tJ));

    Node nodeNextTj = new Coordinates(xStep, yStep + 60);
    Text nextTj = new Text(textGen, nodeNextTj, "t_j = " + tJ, "tj", wait,
        properties);

    sc.highlight(5);
    sc.unhighlight(4);

    lang.nextStep("Schritt 4.b");

    Node nodeNextMj = new Coordinates(xStep, yStep + 90);
    Text nextMj = new Text(textGen, nodeNextMj, "m_j = " + next, "mj", wait,
        properties);

    sc.highlight(6);
    sc.unhighlight(5);

    lang.nextStep();

    String cJ = xor(next, tJ);

    CheckpointUtils.checkpointEvent(this, "xorResultEvent", new Variable(
        "xorResult", cJ));

    Node nodeNextCj = new Coordinates(xStep, yStep + 120);
    Text nextCj = new Text(textGen, nodeNextCj, "c_j = " + cJ, "cj", wait,
        properties);

    sc.highlight(6);
    sc.unhighlight(5);

    lang.nextStep("Schritt 4. c");

    textC.hide();

    lang.nextStep();

    c = c + cJ;

    Node nodeC = new Coordinates(xChiffrat + 100, yChiffrat);
    textC = new Text(textGen, nodeC, c, "cPlus", wait, properties);

    lang.nextStep("Aktuelles Chiffrat");

    ListElementGenerator listElementGenerator = new AnimalListElementGenerator(
        lang);
    Timing timeWait = new TicksTiming(300);
    ListElementProperties listElementProp = new ListElementProperties();
    listElementProp.set("boxFillColor", new Color(255, 255, 255));
    listElementProp.set("position", 4);
    listElementProp.set("pointerAreaFillColor", new Color(255, 255, 255));

    listElementProp.set("text", " " + String.valueOf(iJ.charAt(0)));

    Node coodsI01 = new Coordinates(xStep + 80, yStep - 10);
    ListElement i01 = new ListElement(listElementGenerator, coodsI01, 0, null,
        null, "i01", timeWait, listElementProp);

    listElementProp.set("text", " " + String.valueOf(iJ.charAt(1)));

    Node coodsI02 = new Coordinates(xStep + 110, yStep - 10);
    ListElement i02 = new ListElement(listElementGenerator, coodsI02, 0, null,
        null, "i02", timeWait, listElementProp);

    listElementProp.set("text", " " + String.valueOf(iJ.charAt(2)));

    Node coodsI03 = new Coordinates(xStep + 140, yStep - 10);
    ListElement i03 = new ListElement(listElementGenerator, coodsI03, 0, null,
        null, "i03", timeWait, listElementProp);

    sc.highlight(7);
    sc.unhighlight(6);

    lang.nextStep();

    i01.moveBy("translateWithFixedTip", 185, 0, new TicksTiming(250),
        new TicksTiming(250));
    i02.moveBy("translateWithFixedTip", 185, 0, new TicksTiming(250),
        new TicksTiming(250));
    i03.moveBy("translateWithFixedTip", 185, 0, new TicksTiming(250),
        new TicksTiming(250));

    lang.nextStep();

    properties
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0, 255));

    Node nodeDelete = new Coordinates(xStep + 360, yStep);
    Text textDelete = new Text(textGen, nodeDelete,
        "Die ersten r Bits löschen!", "delete", wait, properties);

    lang.nextStep();

    ListElement c01 = null;
    ListElement c02 = null;
    ListElement c03 = null;

    String iJplus1 = "";

    // ========================================== r = 2 ?

    if (r.compareTo("2") == 0) {
      i01.hide();
      i02.hide();

      lang.nextStep();

      textDelete.hide();

      lang.nextStep();

      listElementProp.set("text", " " + String.valueOf(cJ.charAt(0)));

      Node coodsC01 = new Coordinates(xStep + 80, yStep + 110);
      c01 = new ListElement(listElementGenerator, coodsC01, 0, null, null,
          "c01", timeWait, listElementProp);

      listElementProp.set("text", " " + String.valueOf(cJ.charAt(1)));

      Node coodsC02 = new Coordinates(xStep + 110, yStep + 110);
      c02 = new ListElement(listElementGenerator, coodsC02, 0, null, null,
          "c02", timeWait, listElementProp);

      lang.nextStep();

      c01.moveBy("translateWithFixedTip", 275, 0, new TicksTiming(250),
          new TicksTiming(250));
      c02.moveBy("translateWithFixedTip", 275, 0, new TicksTiming(250),
          new TicksTiming(250));

      lang.nextStep();

      textDelete = new Text(textGen, nodeDelete,
          "c_j wird jetzt an I_j hinten angehängt", "delete", wait, properties);

      lang.nextStep();

      i03.moveBy("translateWithFixedTip", 0, 60, new TicksTiming(250),
          new TicksTiming(250));
      c01.moveBy("translateWithFixedTip", 0, -60, new TicksTiming(250),
          new TicksTiming(250));
      c02.moveBy("translateWithFixedTip", 0, -60, new TicksTiming(250),
          new TicksTiming(250));

      lang.nextStep();

      iJplus1 = String.valueOf(iJ.charAt(2)) + cJ;

      // ========================================== r = 1 ?

    } else if (r.compareTo("1") == 0) {
      i01.hide();

      lang.nextStep();

      textDelete.hide();

      lang.nextStep();

      listElementProp.set("text", " " + String.valueOf(cJ.charAt(0)));

      Node coodsC01 = new Coordinates(xStep + 80, yStep + 110);
      c01 = new ListElement(listElementGenerator, coodsC01, 0, null, null,
          "c01", timeWait, listElementProp);

      lang.nextStep();

      c01.moveBy("translateWithFixedTip", 275, 0, new TicksTiming(250),
          new TicksTiming(250));

      lang.nextStep();

      textDelete = new Text(textGen, nodeDelete,
          "c_j wird jetzt an I_j hinten angehängt", "delete", wait, properties);

      lang.nextStep();

      i02.moveBy("translateWithFixedTip", 0, 60, new TicksTiming(250),
          new TicksTiming(250));
      i03.moveBy("translateWithFixedTip", 0, 60, new TicksTiming(250),
          new TicksTiming(250));
      c01.moveBy("translateWithFixedTip", 0, -60, new TicksTiming(250),
          new TicksTiming(250));

      lang.nextStep();

      iJplus1 = String.valueOf(iJ.charAt(1)) + iJ.charAt(2) + cJ;

      // ========================================== r = 3 ?
    } else if (r.compareTo("3") == 0) {

      i01.hide();
      i02.hide();
      i03.hide();

      lang.nextStep();

      textDelete.hide();

      lang.nextStep();

      listElementProp.set("text", " " + String.valueOf(cJ.charAt(0)));

      Node coodsC01 = new Coordinates(xStep + 80, yStep + 110);
      c01 = new ListElement(listElementGenerator, coodsC01, 0, null, null,
          "c01", timeWait, listElementProp);

      listElementProp.set("text", " " + String.valueOf(cJ.charAt(1)));

      Node coodsC02 = new Coordinates(xStep + 110, yStep + 110);
      c02 = new ListElement(listElementGenerator, coodsC02, 0, null, null,
          "c02", timeWait, listElementProp);

      listElementProp.set("text", " " + String.valueOf(cJ.charAt(2)));

      Node coodsC03 = new Coordinates(xStep + 140, yStep + 110);
      c03 = new ListElement(listElementGenerator, coodsC03, 0, null, null,
          "c03", timeWait, listElementProp);

      lang.nextStep();

      c01.moveBy("translateWithFixedTip", 275, 0, new TicksTiming(250),
          new TicksTiming(250));
      c02.moveBy("translateWithFixedTip", 275, 0, new TicksTiming(250),
          new TicksTiming(250));
      c03.moveBy("translateWithFixedTip", 275, 0, new TicksTiming(250),
          new TicksTiming(250));

      lang.nextStep();

      textDelete = new Text(textGen, nodeDelete,
          "c_j wird jetzt an I_j hinten angehängt", "delete", wait, properties);

      lang.nextStep();

      c01.moveBy("translateWithFixedTip", 0, -60, new TicksTiming(250),
          new TicksTiming(250));
      c02.moveBy("translateWithFixedTip", 0, -60, new TicksTiming(250),
          new TicksTiming(250));
      c03.moveBy("translateWithFixedTip", 0, -60, new TicksTiming(250),
          new TicksTiming(250));

      lang.nextStep();

      iJplus1 = cJ;
    }

    Text textNew = new Text(textGen, nodeDelete, "Neuer I_j+1", "textNew",
        wait, properties);

    textDelete.hide();

    lang.nextStep();

    textNew.hide();
    i02.hide();
    i03.hide();
    c01.hide();
    if (c02 != null)
      c02.hide();
    if (c03 != null)
      c03.hide();
    nextIj.hide();
    nextOj.hide();
    nextTj.hide();
    nextMj.hide();
    nextCj.hide();

    sc.unhighlight(7);

    lang.nextStep();

    return iJplus1;
  }

  private String xor(String next, String tJ) {

    String x = "";

    for (int i = 0; i < next.length(); i++) {
      int term1 = Integer.parseInt(String.valueOf(next.charAt(i)));
      int term2 = Integer.parseInt(String.valueOf(tJ.charAt(i)));
      int s = term1 ^ term2;
      x = x + String.valueOf(s);
    }

    return x;
  }

  private String permutate(String iJ) {
    String p = String.valueOf(iJ.charAt(permutationOrder[0] - 1))
        + iJ.charAt(permutationOrder[1] - 1)
        + iJ.charAt(permutationOrder[2] - 1);
    return p;
  }

  public void init() {
    lang = new AnimalScript("Cipher Feedback Mode Verschlüsselung",
        "Kristijan Madunic", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    permutationOrder = (int[]) primitives.get("permutationOrder");
    initialVector = (String) primitives.get("initialVector");
    plainText = (String) primitives.get("plainText");
    lang.setStepMode(true);
    c = "";
    encrypt(plainText);
    return lang.toString();
  }

  public String getName() {
    return "Cipher Feedback Mode Verschlüsselung";
  }

  public String getAlgorithmName() {
    return "Cipher Feedback Mode (CFB)";
  }

  public String getAnimationAuthor() {
    return "Kristijan Madunic";
  }

  public String getDescription() {
    return "Der CFB-Mode ist zum Verschl&uuml;sseln langer Nachrichten geeignet. Die Nachricht"
        + "\n"
        + "wird in Bl&ouml;cke unterteilt und die einzelnen Bl&ouml;cke werden entsprechend "
        + "\n"
        + "verschl&uuml;sselt. Die einzelnen verschl&uuml;sselten Bl&ouml;cke ergeben den"
        + "\n"
        + "Geheimtext. Somit geh&ouml;hrt der CFB-Mode zu den Blockverschl&uuml;sselungen."
        + "\n"
        + "Dabei haben alle Bl&ouml;cke eine feste gleiche L&auml;nge."
        + "\n";
  }

  public String getCodeExample() {
    return "1. Festlegen des Initialisierungsvektors IV = {0, 1}^n" + "\n"
        + "2. Festlegen der Blockgröße r mit 1 <= r <= n" + "\n"
        + "3. Festlegen des Schlüssels E_k" + "\n" + "4. Für 1 <= j <= u :"
        + "\n" + "4.a) O_j = E_k(I_j)" + "\n"
        + "4.b) t_j : die ersten r Bits von O_j" + "\n"
        + "4.c) c_j = m_j XOR t_j" + "\n"
        + "4.d) I_j+1 = 2^r * I_j + c_j mod 2^n";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public static void main(String[] args) {
    Language l = new AnimalScript("CFB-Mode Verschüsselung",
        "Kristijan Madunic", 640, 480);
    CFBModeEncrypt cfbMode = new CFBModeEncrypt(l);
    cfbMode.encrypt("101011001010");
    System.out.println(l);
  }
}