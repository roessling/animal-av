package generators.compression.helpers;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class ShannonFanoAnim {

  /**
   * The concrete language object used for creating output
   */
  private Language                     lang;

  private SourceCode                   sourceCode;

  private String                input;

  private SourceCodeProperties  codeProp;

  private ArrayMarkerProperties markerProp;

  private ArrayProperties       array;

  private List<Element>                fannoList = new ArrayList<Element>();

  /**
   * Default constructor
   * 
   * @param lang
   *          the concrete language object used for creating output
   * @param array
   *          the ArrayProperties to be used
   * @param codeProp
   *          the SourceCodeProperties to be used
   * @param markerProp
   *          the ArrayMarkerProperties to be used
   * @param input
   *          the input String
   */
  public ShannonFanoAnim(Language lang, ArrayProperties array,
      SourceCodeProperties codeProp, ArrayMarkerProperties markerProp,
      String input) {
    // Store the language object
    this.lang = lang;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divided by a call of lang.nextStep();
    this.lang.setStepMode(true);

    this.input = new String(input);

    System.out.println("Input String: " + input);
    this.codeProp = codeProp;
    this.markerProp = markerProp;
    this.array = array;
    generateCode();
  }

  public void generateCode() {
    description();
    initializeAlgo();
  }

  public void description() {

    // 1
    TextProperties titleProp = new TextProperties();

    titleProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));

    Text title = new Text(new AnimalTextGenerator(lang),
        new Coordinates(20, 35), "Shannon-Fano Coding", "title", null,
        titleProp);
    lang.addItem(title);

    RectProperties headerRect = new RectProperties();
    headerRect.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    headerRect.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    headerRect.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(192, 192,
        192));

    lang.newRect(new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "title", AnimalScript.DIRECTION_SE), "headerRect",
        null, headerRect);

    TextProperties algDescriptionProp = new TextProperties();
    algDescriptionProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Serif", Font.BOLD, 22));

    Text algDescription = new Text(new AnimalTextGenerator(lang),
        new Coordinates(20, 100), "Is a variable-length compression algorithm",
        "alg_description", null, algDescriptionProp);

    lang.addItem(algDescription);

    // 2
    lang.nextStep("Introduction");

    algDescription.hide();

    Text description = new Text(new AnimalTextGenerator(lang), new Coordinates(
        20, 100), "Description", "description", null, algDescriptionProp);
    lang.addItem(description);

    // 3
    lang.nextStep();

    TextProperties textLinesProps = new TextProperties();
    textLinesProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));

    Text line0 = new Text(
        new AnimalTextGenerator(lang),
        new Offset(0, 100, "title", AnimalScript.DIRECTION_SW),
        "1. Extract all unique symbols from a text and count a symbol frequency in this text. ",
        "line0", null, textLinesProps);
    lang.addItem(line0);

    // 4
    lang.nextStep();

    Text line1 = new Text(
        new AnimalTextGenerator(lang),
        new Offset(0, 30, "line0", AnimalScript.DIRECTION_SW),
        "2. Sort the lists of symbols according to frequency in descending order",
        "line1", null, textLinesProps);
    lang.addItem(line1);

    Text line11 = new Text(
        new AnimalTextGenerator(lang),
        new Offset(0, 10, "line1", AnimalScript.DIRECTION_SW),
        "   (most occurring symbols are on the left and the least on the right)",
        "line11", null, textLinesProps);
    lang.addItem(line11);

    // 5
    lang.nextStep();

    Text line2 = new Text(
        new AnimalTextGenerator(lang),
        new Offset(0, 30, "line11", AnimalScript.DIRECTION_SW),
        "3. Now split this list into two sections, with the total frequency number ",
        "line2", null, textLinesProps);
    lang.addItem(line2);

    Text line21 = new Text(
        new AnimalTextGenerator(lang),
        new Offset(0, 10, "line2", AnimalScript.DIRECTION_SW),
        "   of the left section being as close as possible to the total of the right.",
        "line21", null, textLinesProps);
    lang.addItem(line21);

    // 6
    lang.nextStep();

    Text line3 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "line21", AnimalScript.DIRECTION_SW),
        "4. Assign the binary digit 0 to left section and 1 to another: ",
        "line3", null, textLinesProps);
    lang.addItem(line3);

    Text line31 = new Text(
        new AnimalTextGenerator(lang),
        new Offset(0, 10, "line3", AnimalScript.DIRECTION_SW),
        "   the symbols in the left part have codes starting with 0 and symbols in the second part with 1.",
        "line31", null, textLinesProps);
    lang.addItem(line31);

    // 7

    lang.nextStep();

    Text line4 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "line31", AnimalScript.DIRECTION_SW),
        "5. Recursively apply the steps 3 and 4 to each of the two sections,",
        "line4", null, textLinesProps);
    lang.addItem(line4);

    Text line41 = new Text(new AnimalTextGenerator(lang), new Offset(0, 10,
        "line4", AnimalScript.DIRECTION_SW),
        "   adding bits to the codes until no further split can be achieved.",
        "line41", null, textLinesProps);
    lang.addItem(line41);

    // 8
    lang.nextStep();

    description.hide();
    line0.hide();
    line1.hide();
    line11.hide();
    line2.hide();
    line21.hide();
    line3.hide();
    line31.hide();
    line4.hide();
    line41.hide();
  }

  private void stringTraversing(String input, ArrayMarker jMarker) {
    char[] chInput = input.toCharArray();
    for (int i = 1; i < chInput.length; i++) {
      lang.nextStep();
      jMarker.move(i, null, null);

      increaseCharFreqInGrid(chInput[i]);
    }

  }

  /**
   * 
   * @param ch
   */
  private void increaseCharFreqInGrid(char ch) {
    for (Element i : fannoList) {
      if (i.getName() == ch) {
        i.increaseCount();
        lang.addLine(i.searializeToAnim());
      }
    }
  }

  public Primitive drawNillNode(Primitive root, String text, int depth) {

    int x = (int) (-150 / (depth * depth));
    Circle node = lang.newCircle(new Offset(x, 50, root,
        AnimalScript.DIRECTION_SW), 20, text + "Node", null);
    lang.newPolyline(new Node[] {
        new Offset(6, -6, root, AnimalScript.DIRECTION_SW),
        new Offset(-6, 6, node, AnimalScript.DIRECTION_NE) }, "con0" + text,
        null);
    lang.newText(new Offset(-14, -15, node, AnimalScript.DIRECTION_NE), text,
        text + "Text", null);
    return node;

  }

  public Primitive drawOneNode(Primitive root, String text, int depth) {
    int x = (int) (150 / (depth * depth));
    Circle node = lang.newCircle(new Offset(x, 50, root,
        AnimalScript.DIRECTION_SE), 20, text + "Node", null);
    lang.newPolyline(new Node[] {
        new Offset(-6, -6, root, AnimalScript.DIRECTION_SE),
        new Offset(6, 6, node, AnimalScript.DIRECTION_NW) }, "con1" + text,
        null);
    lang.newText(new Offset(8, -15, node, AnimalScript.DIRECTION_NW), text,
        text + "Text", null);
    return node;
  }

  private Rect tempRect;

  private Text S1List    = null;

  private Text S2List    = null;

  private int  iteration = 0;

  private void fsSplit(List<Element> elements, Primitive prim, int depth) {

    sourceCode.unhighlight(17);
    sourceCode.highlight(6);
    iteration++;
    lang.nextStep("Iteration " + iteration);
    sourceCode.toggleHighlight(6, 7);

    lang.nextStep();
    sourceCode.toggleHighlight(7, 8);
    lang.nextStep();
    int myDepth = depth;
    if (elements.size() > 1) {

      int sum = ShannonFanoUtils.getSum(elements);
      lang.newText(new Offset(12, -30, prim, AnimalScript.DIRECTION_SW), "f="
          + sum, "rootName" + myDepth + sum, null);

      sourceCode.toggleHighlight(8, 9);
      lang.nextStep();
      sourceCode.toggleHighlight(9, 11);

      for (int i = 0; i < fannoList.size(); i++) {
        fannoList.get(i).showNeural();
      }

      if (S1List != null)
        S1List.hide();

      if (S2List != null)
        S2List.hide();
      Circle one = elements.get(0).getCircle();
      Circle two = elements.get(elements.size() - 1).getCircle();

      if (tempRect != null)
        tempRect.hide();
      tempRect = lang.newRect(
          new Offset(-2, -5, one, AnimalScript.DIRECTION_NW), new Offset(2, 5,
              two, AnimalScript.DIRECTION_SE), "main_rect", null);

      for (int i = 0; i < elements.size(); i++) {
        lang.addLine(elements.get(i).searializeToAnim(
            elements.get(i).getIndex() - 1));

      }

      lang.nextStep();
      sourceCode.toggleHighlight(11, 12);
      int divide = ShannonFanoUtils.splitArray(elements);

      myDepth++;
      Primitive node0 = drawNillNode(prim, "0", myDepth);
      Primitive node1 = drawOneNode(prim, "1", myDepth);

      List<Element> S1 = elements.subList(0, divide + 1);

      lang.nextStep();

      for (Element i : S1) {
        i.addNill();
        i.showS1();
        if (S1.get(0).equals(i))
          S1List = i.showS1Title(lang);
      }

      for (int i = 0; i < elements.size(); i++) {
        lang.addLine(elements.get(i).searializeToAnim(
            elements.get(i).getIndex() - 1));

      }

      sourceCode.toggleHighlight(12, 13);
      lang.nextStep();

      List<Element> S2 = elements.subList(divide + 1, elements.size());
      for (Element i : S2) {
        i.addOne();
        i.showS2();
        if (S2.get(0).equals(i))
          S2List = i.showS2Title(lang);
      }

      for (int i = 0; i < elements.size(); i++) {
        lang.addLine(elements.get(i).searializeToAnim(
            elements.get(i).getIndex() - 1));

      }

      // coef = coef*0.5;

      sourceCode.toggleHighlight(13, 14);
      lang.nextStep();
      sourceCode.unhighlight(14);
      fsSplit(S1, node0, myDepth);
      lang.nextStep();
      sourceCode.toggleHighlight(14, 15);
      lang.nextStep();
      sourceCode.unhighlight(15);
      fsSplit(S2, node1, myDepth);

    } else {
      lang.newText(new Offset(12, -30, prim, AnimalScript.DIRECTION_SW),
          elements.get(0).toString(), elements.get(0).toString() + "Text", null);
    }

    sourceCode.toggleHighlight(8, 17);

  }

  private void createCharGrid() {
    int count = fannoList.size() + 1;
    lang.addLine("grid \"grid1\" (15,80) lines " + count
        + " columns 3 style plain size 13 bold");
    lang.addLine("setGridValue \"grid1[0][0]\" \"Char\" refresh");
//    int j = 1;
    lang.addLine("setGridValue \"grid1[0][1]\" \"Frequency\" refresh");
    lang.addLine("setGridValue \"grid1[0][2]\" \"Code\" refresh");

  }

  public void initializeAlgo() {

    Set<Character> uniqueChars = ShannonFanoUtils
        .extractUniqueChars(this.input);

    for (Character i : uniqueChars) {
      fannoList.add(new Element(i));
    }
    createCharGrid();

    for (int i = 0; i < fannoList.size(); i++) {
      lang.addLine(fannoList.get(i).searializeToAnim(i));
    }

    //

    //
    sourceCode = lang.newSourceCode(new Offset(10, 20, "grid1",
        AnimalScript.DIRECTION_SW), "sourceCode", null, this.codeProp);

    sourceCode.addCodeLine("begin", null, 0, null);
    sourceCode.addCodeLine("count source units", null, 1, null);
    sourceCode.addCodeLine("sort source units to non-decreasing order", null,
        1, null);
    sourceCode.addCodeLine("SF-Split(S)", null, 1, null);
    sourceCode.addCodeLine("write output", null, 1, null);
    sourceCode.addCodeLine("end", null, 0, null);

    sourceCode.addCodeLine("procedure SF-Split(S)", null, 0, null);
    sourceCode.addCodeLine("begin", null, 0, null);
    sourceCode.addCodeLine("if (|S|>1) then", null, 1, null);
    sourceCode.addCodeLine("begin", null, 1, null);
    sourceCode.addCodeLine("//divide with about same count of units", null, 2,
        null);
    sourceCode.addCodeLine("divide S to S1 and S2 ", null, 2, null);
    sourceCode.addCodeLine("add 0 to codes in S1", null, 2, null);
    sourceCode.addCodeLine("add 1 to codes in S2", null, 2, null);
    sourceCode.addCodeLine("SF-Split(S1)", null, 2, null);
    sourceCode.addCodeLine("SF-Split(S2)", null, 2, null);
    sourceCode.addCodeLine("end", null, 1, null);
    sourceCode.addCodeLine("end", null, 0, null);
    sourceCode.highlight(0);

    //

    StringArray textArray = lang.newStringArray(new Coordinates(400, 80),
        ShannonFanoUtils.getStringArray(input), "textArray", null, this.array);

    // 9
    lang.nextStep("Pseudocode");
    sourceCode.toggleHighlight(0, 1);

    // 10
    lang.nextStep();

    ArrayMarker jMarker = lang.newArrayMarker(textArray, 0, "iMarker", null,
        this.markerProp);

    increaseCharFreqInGrid(input.charAt(0));

    // 11
    stringTraversing(this.input, jMarker);
    lang.nextStep("Initialisation");
    textArray.hide();
    jMarker.hide();
    sourceCode.toggleHighlight(1, 2);
    lang.nextStep();
    fannoList = ShannonFanoUtils.sortByWeight(fannoList);
    for (int i = 0; i < fannoList.size(); i++) {
      lang.addLine(fannoList.get(i).searializeToAnim(i));
    }

    lang.nextStep();
    sourceCode.toggleHighlight(2, 3);
    lang.nextStep("Algo execution");
    sourceCode.unhighlight(3);
    Circle rootNode = lang.newCircle(new Coordinates(600, 160), 20, "rootNode",
        null);

    TextProperties listsProp = new TextProperties();
    listsProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 15));

    Text SList = new Text(new AnimalTextGenerator(lang), new Coordinates(350,
        45), "S:", "lists", null, listsProp);
    lang.addItem(SList);

    CircleProperties props = new CircleProperties();
    props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    props.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(192, 192, 192));

    Circle firstCir = lang.newCircle(new Offset(10, 10, SList,
        AnimalScript.DIRECTION_NE), 20, Integer.toString(this.hashCode()),
        null, props);

    firstCir.hide();

    Primitive temp = null;
    for (int i = 0; i < fannoList.size(); i++) {
      if (i == 0) {
        temp = fannoList.get(i).serializeAsList(lang, firstCir);
      } else {
        temp = fannoList.get(i).serializeAsList(lang, temp);
      }
    }

    fsSplit(fannoList, rootNode, 0);
    tempRect.hide();

    S1List.hide();
    S2List.hide();

    SList.hide();
    // 26

    for (Element i : fannoList) {
      i.hideAll();
    }

    TextProperties outputProp = new TextProperties();
    outputProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    TextProperties outputProp2 = new TextProperties();
    outputProp2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));

    String outputStr = new String();
    for (Element i : fannoList) {
      outputStr += i.getOutput() + " ";
    }

    Text output = new Text(new AnimalTextGenerator(lang), new Coordinates(300,
        35), "Output: " + outputStr, "output", null, outputProp);

    lang.addItem(output);

    Text results = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "output", AnimalScript.DIRECTION_NW),
        "Avarage amount of bits for encoding one symbol is:", "results", null,
        outputProp2);
    lang.addItem(results);

    int total = 0;
    int totalSymb = 0;
    for (Element i : fannoList) {
      total += i.getCode().length() * i.getCount();
      totalSymb += i.getCount();
    }

    Text results2 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "results", AnimalScript.DIRECTION_NW),
        "Total_Amount_of_bits/Total_amount_of_symbols = " + total + "/"
            + totalSymb + " = " + (float) total / totalSymb, "results2", null,
        outputProp2);
    lang.addItem(results2);

    lang.nextStep("Results");
    output.hide();
    results.hide();
    results2.hide();
    sourceCode.toggleHighlight(4, 5);

    Text result1 = new Text(new AnimalTextGenerator(lang), new Coordinates(300,
        35), "Coded Text:", "result1", null, outputProp2);
    lang.addItem(result1);

    char[] inputCh = this.input.toCharArray();

    String outputSeq = new String();
    for (int i = 0; i < inputCh.length; i++) {
      for (Element j : fannoList) {
        if (j.getName() == inputCh[i]) {
          outputSeq += j.getCode() + " ";
        }
      }
    }

    Text results1 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "output", AnimalScript.DIRECTION_NW), outputSeq, "results1", null,
        outputProp2);
    lang.addItem(results1);

    Text results2_1 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "results1", AnimalScript.DIRECTION_NW),
        "Total amount of bits with Shannon coding: " + total + " bits",
        "results2_1", null, outputProp2);

    lang.addItem(results2_1);
    int amountOfBits = 0;
    int size = fannoList.size();

    while (size != 0) {
      size = size >> 1;
      amountOfBits++;
    }

    Text results3_1 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "results2_1", AnimalScript.DIRECTION_NW),
        "Total amount of bits without Shannon coding: " + totalSymb
            + " chars * " + amountOfBits + " bit= " + totalSymb * amountOfBits
            + " bits", "results3_1", null, outputProp2);

    lang.addItem(results3_1);

    Text results4_1 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "results3_1", AnimalScript.DIRECTION_NW), "" + totalSymb * amountOfBits
        + "-" + total + " = " + (totalSymb * amountOfBits - total)
        + ", we save " + (totalSymb * amountOfBits - total) + " bits",
        "results4_1", null, outputProp2);

    lang.addItem(results4_1);

    // lang.newGraph(name, graphAdjacencyMatrix, graphNodes, labels,
    // display);

  }

//  public static void main(String[] args) {
//
//    /*
//     * ArrayProperties textArrayProp = new ArrayProperties(); //
//     * textArrayProp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(192,
//     * 192, 192));
//     * textArrayProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new
//     * Color(255, 0, 0));
//     * textArrayProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new
//     * Color(255, 200, 0));
//     * textArrayProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
//     * "SansSerif", Font.BOLD, 16));
//     * 
//     * SourceCodeProperties scProps = new SourceCodeProperties();
//     * scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, new Color(0,
//     * 255, 255)); scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
//     * "Monospaced", Font.BOLD, 12));
//     * scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(
//     * 255, 0, 0)); // Create a new animation // name, author, screen width,
//     * screen height
//     */
//    Language l = new AnimalScript("Shannon-Fano Coding",
//        "Dieter Hofmann, Artem Vovk", 800, 600);
//    ShannonFanoAnim anim = new ShannonFanoAnim(l, array, codeProp, markerProp,
//        input);
//    // int[] a = { 7, 3, 2, 4, 1, 13, 52, 13, 5, 1 };
//    // s.sort(a);
//    anim.generateCode();
//    System.out.println(l);
//  }

}
