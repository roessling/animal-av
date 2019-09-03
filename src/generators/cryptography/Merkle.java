package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;

public class Merkle implements Generator {
  private Language lang;
  private Color    colorVerification;
  private Color    colorSigning;
  private Color    colorVerification2;
  private String[] leafNodes;
  private Color    colorHashHighlight;
  private Color    colorHash;
  private Color    colorSignature;

  // Authentification path nodes
  int[][]          aN       = { { 2, 5, 11 }, { 0, 5, 11 }, { 6, 1, 11 },
      { 4, 1, 11 },

      { 10, 13, 3 }, { 8, 13, 3 }, { 14, 9, 3 }, { 12, 9, 3 }

                            };

  // Pathes to root
  int[][]          aEdges   = { { 0, 1, 3 }, { 2, 1, 3 }, { 4, 5, 3 },
      { 6, 5, 3 }, { 8, 9, 11 }, { 10, 9, 11 }, { 12, 13, 11 }, { 14, 13, 11 }

                            };

  // Hash inorder
  String[]         hash     = { "h7", "h3", "h8", "h1", "h9", "h4", "h10",
      "h0", "h11", "h5", "h12", "h2", "h13", "h6", "h14" };

  // Nodes in levels
  int[]            yOffsets = { 3, 2, 3, 1, 3, 2, 3, 0, 3, 2, 3, 1, 3, 2, 3 };
  int[]            layer_3  = { 0, 2, 4, 6, 8, 10, 12, 14 };
  int[]            layer_2  = { 1, 5, 9, 13 };
  int[]            layer_1  = { 3, 11 };
  String[]         hash_3   = { "h7", "h8", "h9", "h10", "h11", "h12", "h13",
      "h14"                };
  String[]         hash_2   = { "h3", "h4", "h5", "h6" };
  String[]         hash_1   = { "h1", "h2" };
  int              root     = 7;

  // Adjacent matrix
  int[][]          adjMat   = {
      { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 },
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 } };

  /**
   * Calculates value of hash-function
   * 
   * @param bin
   * @return value of hash function
   */
  private String calcHash(String bin) {
    int n = Integer.parseInt(bin, 2);
    n = qs(n);
    String r = "000" + Integer.toBinaryString(n);
    return "" + r.charAt(r.length() - 3) + r.charAt(r.length() - 2)
        + r.charAt(r.length() - 1);
  }

  /**
   * Calculates sum of digits
   * 
   * @param n
   * @return
   */
  private int qs(int n) {
    int sum = 0;
    String s = "" + n;
    for (int i = 0; i < s.length(); i++)
      sum += Integer.parseInt("" + s.charAt(i));
    return sum;
  }

  /**
   * Generates nodes for Merkle tree
   * 
   * @param xOffset
   * @param yOffset
   * @param yOffsets
   * @param offsetFrom
   * @return nodes of Merkle tree
   */
  public Node[] generateNodes(int xOffset, int yOffset, int[] yOffsets,
      Primitive offsetFrom) {
    Node[] nodes = new Node[yOffsets.length];
    for (int i = 0; i < yOffsets.length; i++) {
      nodes[i] = new Offset(xOffset + i * 60, yOffset + yOffsets[i] * 40,
          offsetFrom, "SW");
    }
    return nodes;
  }

  /**
   * Calculates the binary four digit binary number of given int
   * 
   * @param i
   * @return
   */
  private String i2bin(int i) {
    String r = "0000";
    return r.substring(0, 4 - Integer.toBinaryString(i).length()).concat(
        Integer.toBinaryString(i));
  }

  /**
   * Checks if all digits are either 0 or 1
   * 
   * @param d
   * @return true if no wrong values
   */
  public boolean isInvalid(String[] d) {
    if (d.length != 8)
      return true;
    else
      for (int j = 0; j < d.length; j++)
        if (d[j].length() != 3)
          return true;
        else
          for (int i = 0; i < d[j].length(); i++)
            if (!(d[j].charAt(i) == '0' || d[j].charAt(i) == '1'))
              return true;
    return false;
  }

  public void init() {
    lang = new AnimalScript("Merkle-Verfahren", "Nikolaus Korfhage", 800, 600);
    lang.setStepMode(true); // step mode on
  }

  /**
   * The whole animation
   * 
   * @throws IllegalDirectionException
   */
  public void startAnimation() throws IllegalDirectionException {

    String[] leafNodesDefault = { "001", "101", "111", "010", "100", "110",
        "000", "010" };

    String[] hOfY = (!isInvalid(leafNodes) ? leafNodes : leafNodesDefault);
    String[] h2OfY = { "" + calcHash(hOfY[0] + hOfY[1]),
        "" + calcHash(hOfY[2] + hOfY[3]), "" + calcHash(hOfY[4] + hOfY[5]),
        "" + calcHash(hOfY[6] + hOfY[7]) };
    String[] h3OfY = { "" + calcHash(h2OfY[0] + h2OfY[1]),
        "" + calcHash(h2OfY[2] + h2OfY[3]) };
    String h4OfY = "" + calcHash(h3OfY[0] + h3OfY[1]);

    String[] hashValue = { hOfY[0], h2OfY[0], hOfY[1], h3OfY[0], hOfY[2],
        h2OfY[1], hOfY[3], h4OfY, hOfY[4], h2OfY[2], hOfY[5], h3OfY[1],
        hOfY[6], h2OfY[3], hOfY[7] };

    /*
     * Graph Labels
     */
    TextProperties graphLabelProp = new TextProperties();
    graphLabelProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 10));
    graphLabelProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorHash);

    RectProperties graphLabelRectProp = new RectProperties();
    graphLabelRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY,
        Boolean.TRUE);
    graphLabelRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphLabelRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect lFrame;
    Text lText;
    /*
     * Header for whole animation
     */
    TextProperties headerProp = new TextProperties();
    headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30), "Merkle-Verfahren",
        "header", null, headerProp);

    RectProperties hRectProp = new RectProperties();
    hRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    hRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    hRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header,
        "SE"), "hRect", null, hRectProp);

    /*
     * Intro
     */
    TextProperties descTextProp = new TextProperties();
    descTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    Text introText1 = lang
        .newText(
            new Offset(0, 100, header, "SW"),
            "Mit dem Merkle-Verfahren können Dokumente unter Verwendung eines Einmalsignaturverfahrens (z.B. LD-OTS)",
            "introText1", null, descTextProp);
    Text introText2 = lang.newText(new Offset(0, 5, introText1, "SW"),
        "signiert und verifierziert werden.", "introText1", null, descTextProp);
    Text introText3 = lang
        .newText(
            new Offset(0, 10, introText2, "SW"),
            "Dabei wird das Problem, dass bei Einmalsignaturverfahren für jede Signatur ein neues Schlüsselpaar benötigt wird, gelöst.",
            "introText1", null, descTextProp);
    Text introText4 = lang
        .newText(
            new Offset(0, 5, introText3, "SW"),
            "Mit dem Merkle-Verfahren können viele Signaturen mit einem einzigen öffentlichen Schlüssel verifiziert werden.",
            "introText1", null, descTextProp);
    Text introText5 = lang
        .newText(
            new Offset(0, 5, introText4, "SW"),
            "Dazu wird ein binärer Hashbaum berechnet, dessen Blätter Signaturen sind und dessen Wurzel der öffentliche Schlüssel ist.",
            "introText1", null, descTextProp);
    Text introText6 = lang
        .newText(
            new Offset(0, 10, introText5, "SW"),
            "Nach der Schlüsselgenerierung wird das Dokument zunächst 8 mal signiert und anschließend 8 mal verifiziert.",
            "introText1", null, descTextProp);
    Text introText7 = lang.newText(new Offset(0, 5, introText6, "SW"),
        "Die Hashfunktion im Beispiel arbeitet folgendermaßen:", "introText1",
        null, descTextProp);
    Text introText8 = lang
        .newText(
            new Offset(0, 5, introText7, "SW"),
            "Der Hashwert sind die drei letzten Bits der Binärdarstellung der Quersumme der übergebenen Dezimalzahl.",
            "introText1", null, descTextProp);
    Text introText9 = lang
        .newText(
            new Offset(0, 5, introText8, "SW"),
            "Wenn die Binärdarstellung zu kurz ist, werden führende Nullen ergänzt.",
            "introText1", null, descTextProp);

    lang.nextStep("Schlüsselerzeugung");
    introText1.hide();
    introText2.hide();
    introText3.hide();
    introText4.hide();
    introText5.hide();
    introText6.hide();
    introText7.hide();
    introText8.hide();
    introText9.hide();

    /*
     * Key generation
     */
    TextProperties captionText = new TextProperties();
    captionText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 18));
    Text caption = lang.newText(new Coordinates(20, 100), "Schlüsselerzeugung",
        "caption", null, captionText);
    lang.nextStep();

    /*
     * Description key generation
     */
    Text keyGenText0 = lang
        .newText(
            new Offset(0, 465, header, "SW"),
            "Zunächst werden die Blätter des binären Hashbaumes berechnet. Diese erhält man mit einem Einmal-Signaturverfahren (z.B. LD-OTS).",
            "keyGenText0", null, descTextProp);
    Text keyGenText1 = lang.newText(new Offset(0, 5, keyGenText0, "SW"),
        "Dabei ist xi der Signierschlüssel und yi der Verifikationsschlüssel.",
        "keyGenText1", null, descTextProp);
    Text keyGenText2 = lang
        .newText(
            new Offset(0, 5, keyGenText1, "SW"),
            "Die Nicht-Blattknoten sind die Hashwerte der Konkatenation der jeweiligen Kindknoten, H(k1||k2). Der Wurzelknoten ist der öffentliche Schlüssel.",
            "keyGenText2", null, descTextProp);

    /*
     * Merkle tree
     */
    GraphProperties merkleTreeProperties = new GraphProperties();
    merkleTreeProperties.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY,
        colorHashHighlight);
    merkleTreeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        colorSignature);
    merkleTreeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.DARK_GRAY);
    merkleTreeProperties.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY,
        Color.BLACK);
    merkleTreeProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Color.LIGHT_GRAY);
    merkleTreeProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
    Node[] n = generateNodes(70, 100, yOffsets, header);
    String[] l = { " ", " ", " ", " ", " ", " ", " ", "R", " ", " ", " ", " ",
        " ", " ", " " };
    Graph merkleTree = lang.newGraph("merkleTree", adjMat, n, l, null,
        merkleTreeProperties);

    /*
     * Create icons
     */
    // Create a (red) signing key icon
    CircleProperties keyCircleProp = new CircleProperties();
    keyCircleProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    Circle keyCircle = lang.newCircle(new Coordinates(20, 20), 5, "keyCircle",
        new Hidden(), keyCircleProp);
    RectProperties keyR1Prop = new RectProperties();
    keyR1Prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    Rect keyR1 = lang.newRect(new Offset(0, -2, keyCircle, "C"), new Offset(16,
        1, keyCircle, "C"), "keyR1", new Hidden(), keyR1Prop);
    Rect keyR2 = lang.newRect(new Offset(-6, 0, keyR1, "NE"), new Offset(-4, 6,
        keyR1, "NE"), "keyR2", new Hidden(), keyR1Prop);
    Rect keyR3 = lang.newRect(new Offset(-3, 0, keyR1, "NE"), new Offset(-2, 4,
        keyR1, "NE"), "keyR3", new Hidden(), keyR1Prop);
    Rect keyR4 = lang.newRect(new Offset(-1, 0, keyR1, "NE"), new Offset(1, 6,
        keyR1, "NE"), "keyR4", new Hidden(), keyR1Prop);
    LinkedList<Primitive> sigKeyList = new LinkedList<Primitive>();
    sigKeyList.add(keyCircle);
    sigKeyList.add(keyR1);
    sigKeyList.add(keyR2);
    sigKeyList.add(keyR3);
    sigKeyList.add(keyR4);

    Group sigKey = lang.newGroup(sigKeyList, "sigKey");
    sigKey.changeColor("color", colorSigning, null, null);
    sigKey.changeColor("fillColor", colorSigning, null, null);

    // Create a green verification key icon (no clone?)
    CircleProperties keyCirclePropV = new CircleProperties();
    keyCirclePropV.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    Circle keyCircleV = lang.newCircle(new Coordinates(20, 20), 5,
        "keyCircleV", new Hidden(), keyCircleProp);
    RectProperties keyR1PropV = new RectProperties();
    keyR1PropV.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    Rect keyR1V = lang.newRect(new Offset(0, -2, keyCircleV, "C"), new Offset(
        16, 1, keyCircleV, "C"), "keyR1", new Hidden(), keyR1PropV);
    Rect keyR2V = lang.newRect(new Offset(-6, 0, keyR1V, "NE"), new Offset(-4,
        6, keyR1V, "NE"), "keyR2", new Hidden(), keyR1PropV);
    Rect keyR3V = lang.newRect(new Offset(-3, 0, keyR1V, "NE"), new Offset(-2,
        4, keyR1V, "NE"), "keyR3", new Hidden(), keyR1PropV);
    Rect keyR4V = lang.newRect(new Offset(-1, 0, keyR1V, "NE"), new Offset(1,
        6, keyR1V, "NE"), "keyR4", new Hidden(), keyR1PropV);
    LinkedList<Primitive> sigKeyListV = new LinkedList<Primitive>();
    sigKeyListV.add(keyCircleV);
    sigKeyListV.add(keyR1V);
    sigKeyListV.add(keyR2V);
    sigKeyListV.add(keyR3V);
    sigKeyListV.add(keyR4V);
    Group verKey = lang.newGroup(sigKeyListV, "verKey");
    verKey.changeColor("color", colorVerification, null, null);
    verKey.changeColor("fillColor", colorVerification, null, null);

    // Create a dokument icon
    RectProperties docRectProp = new RectProperties();
    docRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    Rect docFrame = lang.newRect(new Coordinates(80, 80), new Coordinates(95,
        100), "docFrame", null, docRectProp);
    PolylineProperties docPolyLineProp = new PolylineProperties();
    docPolyLineProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

    Node[] docHeadlineArray = { new Offset(4, 5, docFrame, "NW"),
        new Offset(10, 5, docFrame, "NW") };
    Polyline docHeadline = lang.newPolyline(docHeadlineArray, "docHeadline",
        null, docPolyLineProp);
    Node[] docLine1Array = { new Offset(2, 10, docFrame, "NW"),
        new Offset(12, 10, docFrame, "NW") };
    Polyline docLine1 = lang.newPolyline(docLine1Array, "docLine1", null,
        docPolyLineProp);
    Node[] docLine2Array = { new Offset(0, 2, docLine1, "NW"),
        new Offset(10, 2, docLine1, "NW") };
    Polyline docLine2 = lang.newPolyline(docLine2Array, "docLine2", null,
        docPolyLineProp);
    Node[] docLine3Array = { new Offset(0, 2, docLine2, "NW"),
        new Offset(9, 2, docLine2, "NW") };
    Polyline docLine3 = lang.newPolyline(docLine3Array, "docLine3", null,
        docPolyLineProp);

    LinkedList<Primitive> docList = new LinkedList<Primitive>();
    docList.add(docFrame);
    docList.add(docHeadline);
    docList.add(docLine1);
    docList.add(docLine2);
    docList.add(docLine3);

    Group doc = lang.newGroup(docList, "doc");
    doc.hide();

    sigKey.moveTo("NW", "translate", new Offset(0, 265, header, "SW"), null,
        null);

    lang.nextStep();
    sigKey.show();

    // Lamport signing keys
    LinkedList<Primitive> xList = new LinkedList<Primitive>();
    TextProperties xProp = new TextProperties();
    xProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorSigning);
    xProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));
    Text x0 = lang.newText(new Offset(67, 255, header, "SW"), "x0", "x0", null,
        xProp);
    xList.add(x0);
    merkleTree.highlightNode(layer_3[0], null, null);

    lang.nextStep();

    // Lamport verification keys
    LinkedList<Primitive> yList = new LinkedList<Primitive>();
    TextProperties yProp = new TextProperties();
    yProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorVerification2);
    yProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));
    Text y0 = lang.newText(new Offset(67, 290, header, "SW"), "y0", "y0", null,
        yProp);
    yList.add(y0);
    merkleTree.highlightNode(layer_3[0], null, null);

    lang.nextStep();
    // Print lamport keys, mark nodes
    for (int i = 1; i < 8; i++) {
      merkleTree.unhighlightNode(layer_3[i - 1], null, null);
      merkleTree.highlightNode(layer_3[i], null, null);
      Text y = lang.newText(new Offset(i * 120, 0, y0, "NW"), "y" + i, "y" + i,
          null, yProp);
      yList.add(y);

      merkleTree.unhighlightNode(layer_3[i - 1], null, null);
      merkleTree.highlightNode(layer_3[i], null, null);
      Text x = lang.newText(new Offset(i * 120, 0, x0, "NW"), "x" + i, "x" + i,
          null, xProp);
      xList.add(x);

      lang.nextStep();
    }

    merkleTree.unhighlightNode(layer_3[layer_3.length - 1], null, null);

    // Hash function icon
    TextProperties hashIconProp = new TextProperties();
    hashIconProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.BOLD, 22));
    hashIconProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorHash);
    Text iconHash = lang.newText(new Offset(0, 327, header, "SW"), "H",
        "iconHash", null, hashIconProp);

    LinkedList<Primitive> hList = new LinkedList<Primitive>();
    TextProperties hProp = new TextProperties();
    hProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorHash);
    hProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));
    Text h0 = lang.newText(new Offset(67, 315, header, "SW"), "H(y0)="
        + hOfY[0], "H(y0)=" + hOfY[0], null, hProp);
    hList.add(h0);
    merkleTree.highlightNode(layer_3[0], null, null);

    /*
     * Hash values layer 3
     */
    LinkedList<Primitive> labelList = new LinkedList<Primitive>();
    lText = lang.newText(n[layer_3[0]], hash_3[0] + "=" + hOfY[0], hOfY[0],
        null, graphLabelProp);
    lFrame = lang.newRect(new Offset(-2, -2, lText, "NW"), new Offset(2, 2,
        lText, "SE"), "lText", null, graphLabelRectProp);
    labelList.add(lText);
    labelList.add(lFrame);

    lang.nextStep();
    for (int i = 1; i < 8; i++) {
      merkleTree.unhighlightNode(layer_3[i - 1], null, null);
      merkleTree.highlightNode(layer_3[i], null, null);
      Text h = lang.newText(new Offset(i * 120, 0, h0, "NW"), "H(y" + i + ")="
          + hOfY[i], "H(y" + i + ")=" + hOfY[i], null, hProp);
      hList.add(h);

      lText = lang.newText(n[layer_3[i]], hash_3[i] + "=" + hOfY[i], hOfY[i],
          null, graphLabelProp);
      lFrame = lang.newRect(new Offset(-2, -2, lText, "NW"), new Offset(2, 2,
          lText, "SE"), "lText", null, graphLabelRectProp);
      labelList.add(lText);
      labelList.add(lFrame);

      lang.nextStep();

    }
    merkleTree.unhighlightNode(layer_3[layer_3.length - 1], null, null);
    lang.nextStep();

    LinkedList<Primitive> h2List = new LinkedList<Primitive>();
    Text h20 = lang.newText(new Offset(85, 350, header, "SW"),
        "H(H(y0) || H(y1))=" + h2OfY[0], "H(H(y0),H(y1))=" + h2OfY[0], null,
        hProp);
    h2List.add(h20);
    merkleTree.highlightNode(layer_2[0], null, null);
    hList.get(0).changeColor("color", colorHashHighlight, null, null);
    hList.get(1).changeColor("color", colorHashHighlight, null, null);

    lText = lang.newText(n[layer_2[0]], hash_2[0] + "=" + h2OfY[0], h2OfY[0],
        null, graphLabelProp);
    lFrame = lang.newRect(new Offset(-2, -2, lText, "NW"), new Offset(2, 2,
        lText, "SE"), "lText", null, graphLabelRectProp);
    labelList.add(lText);
    labelList.add(lFrame);

    lang.nextStep();
    /*
     * Hash values layer 2
     */
    int j = 1;
    for (int i = 1; i < 4; i++) {
      merkleTree.unhighlightNode(layer_2[i - 1], null, null);
      merkleTree.highlightNode(layer_2[i], null, null);
      hList.get(j + 1).changeColor("color", colorHashHighlight, null, null);
      hList.get(j + 2).changeColor("color", colorHashHighlight, null, null);
      hList.get(j).changeColor("color", colorHash, null, null);
      hList.get(j - 1).changeColor("color", colorHash, null, null);
      Text h2 = lang.newText(new Offset(i * 240, 0, h20, "NW"), "H(H(y"
          + (j + 1) + ") || H(y" + (j + 2) + "))=" + h2OfY[i],
          "H(H(y0),H(y1))=" + h2OfY[i], null, hProp);
      h2List.add(h2);

      lText = lang.newText(n[layer_2[i]], hash_2[i] + "=" + h2OfY[i], h2OfY[i],
          null, graphLabelProp);
      lFrame = lang.newRect(new Offset(-2, -2, lText, "NW"), new Offset(2, 2,
          lText, "SE"), "lText", null, graphLabelRectProp);
      labelList.add(lText);
      labelList.add(lFrame);

      lang.nextStep();
      j = j + 2;
    }
    hList.get(7).changeColor("color", colorHash, null, null);
    hList.get(6).changeColor("color", colorHash, null, null);
    merkleTree.unhighlightNode(layer_2[layer_2.length - 1], null, null);
    lang.nextStep();

    /*
     * Hash values layer 1
     */
    LinkedList<Primitive> h3List = new LinkedList<Primitive>();
    Text h30 = lang.newText(new Offset(120, 375, header, "SW"),
        "H( H(H(y0)||H(y1)) || H(H(y2)||H(y3)) )=" + h3OfY[0], "Hy01y1y2y3="
            + h3OfY[0], null, hProp);
    h3List.add(h30);
    merkleTree.highlightNode(layer_1[0], null, null);
    h2List.get(0).changeColor("color", colorHashHighlight, null, null);
    h2List.get(1).changeColor("color", colorHashHighlight, null, null);

    lText = lang.newText(n[layer_1[0]], hash_1[0] + "=" + h3OfY[0], h3OfY[0],
        null, graphLabelProp);
    lFrame = lang.newRect(new Offset(-2, -2, lText, "NW"), new Offset(2, 2,
        lText, "SE"), "lText", null, graphLabelRectProp);
    labelList.add(lText);
    labelList.add(lFrame);

    lang.nextStep();

    h30 = lang.newText(new Offset(120 + 480, 375, header, "SW"),
        "H( H(H(y4)||H(y5)) || H(H(y6)||H(y7)) )=" + h3OfY[1], "Hy01y1y2y3="
            + h3OfY[1], null, hProp);
    h3List.add(h30);
    merkleTree.unhighlightNode(layer_1[0], null, null);
    merkleTree.highlightNode(layer_1[1], null, null);
    h2List.get(0).changeColor("color", colorHash, null, null);
    h2List.get(1).changeColor("color", colorHash, null, null);
    h2List.get(2).changeColor("color", colorHashHighlight, null, null);
    h2List.get(3).changeColor("color", colorHashHighlight, null, null);

    lText = lang.newText(n[layer_1[1]], hash_1[1] + "=" + h3OfY[1], h3OfY[1],
        null, graphLabelProp);
    lFrame = lang.newRect(new Offset(-2, -2, lText, "NW"), new Offset(2, 2,
        lText, "SE"), "lText", null, graphLabelRectProp);
    labelList.add(lText);
    labelList.add(lFrame);

    lang.nextStep();

    /*
     * Root, layer 0
     */
    h2List.get(2).changeColor("color", colorHash, null, null);
    h2List.get(3).changeColor("color", colorHash, null, null);
    merkleTree.unhighlightNode(layer_1[layer_1.length - 1], null, null);
    lang.nextStep();

    Text h40 = lang
        .newText(
            new Offset(240, 400, header, "SW"),
            "H(H(H(H(y0)||H(y1))||H(H(y2)||H(y3))) || H(H(H(y4)||H(y5))||H(H(y6)||H(y7))))="
                + h4OfY, "Hy01y1y2y3y4y5y6y7=" + h4OfY, null, hProp);
    merkleTree.highlightNode(root, null, null);
    h3List.get(0).changeColor("color", colorHashHighlight, null, null);
    h3List.get(1).changeColor("color", colorHashHighlight, null, null);

    lText = lang.newText(n[root], "h0=" + h4OfY, h4OfY, null, graphLabelProp);
    lFrame = lang.newRect(new Offset(-2, -2, lText, "NW"), new Offset(2, 2,
        lText, "SE"), "lText", null, graphLabelRectProp);
    labelList.add(lText);
    labelList.add(lFrame);

    lang.nextStep();

    h3List.get(0).changeColor("color", colorHash, null, null);
    h3List.get(1).changeColor("color", colorHash, null, null);

    verKey.moveTo("NW", "translate", new Offset(483, 75, header, "SW"), null,
        null);
    verKey.show();

    keyGenText0.hide();
    keyGenText1.hide();
    keyGenText2.hide();

    /*
     * Signing
     */
    lang.nextStep("Signierung");
    caption.setText("Signierung", null, null);
    merkleTree.unhighlightNode(root, null, null);
    hList.addAll(h2List);
    hList.addAll(h3List);
    hList.add(h40);
    Group yGroup = lang.newGroup(yList, "yGroup");
    Group hashGroup = lang.newGroup(hList, "hashGroup");
    iconHash.hide(new TicksTiming(5));
    hashGroup.hide(new TicksTiming(15));
    yGroup.hide(new TicksTiming(30));

    doc.moveTo("NW", "translate", new Offset(67, 287, header, "SW"), null, null);
    doc.show(new TicksTiming(45));

    TextProperties sigIconProp = new TextProperties();
    sigIconProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.ITALIC, 22));
    sigIconProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorSignature);
    Text iconSig = lang.newText(new Offset(0, 330, header, "SW"), "S",
        "iconSig", new Hidden(), sigIconProp);
    iconSig.show(new TicksTiming(60));

    lang.nextStep();

    TextProperties iconSignatureProp = new TextProperties();
    iconSignatureProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Serif", Font.ITALIC, 3));
    iconSignatureProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorSigning);
    Text iconSignature = lang.newText(new Offset(68, 284, header, "SW"),
        "Signed", "iconSignature", new Hidden(), iconSignatureProp);
    iconSignature.show(new TicksTiming(15));

    LinkedList<Primitive> sList = new LinkedList<Primitive>();
    TextProperties sProp = new TextProperties();
    sProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorSignature);
    sProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));
    Text s0 = lang.newText(new Offset(67, 330, header, "SW"), "s0", "s0",
        new Hidden(), sProp);
    s0.show(new TicksTiming(30));
    sList.add(s0);

    for (int i = 1; i < 8; i++) {
      lang.nextStep();
      iconSignature.hide();
      iconSignature.moveTo("NW", "translate", new Offset(120, 0, iconSignature,
          "NW"), null, null);
      doc.moveTo("NW", "translate", new Offset(120, 0, doc, "NWW"), null,
          new TicksTiming(30));
      iconSignature.show(new TicksTiming(45));
      Text s = lang.newText(new Offset(120 * i, 0, s0, "NW"), "s" + i, "s" + i,
          new Hidden(), sProp);
      s.show(new TicksTiming(60));
      sList.add(s);
    }
    lang.nextStep();
    iconSignature.hide();
    doc.hide();
    sList.add(iconSig);
    Group sGroup = lang.newGroup(sList, "sList");
    sGroup.moveTo("NW", "translate", new Offset(0, -30, sGroup, "NW"), null,
        new TicksTiming(30));

    /*
     * Complete signatures
     */
    LinkedList<Primitive> descriptionList = new LinkedList<Primitive>();

    TextProperties arrowCounterTextProp = new TextProperties();
    arrowCounterTextProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);

    TextProperties arrowSigTextProp = new TextProperties();
    arrowSigTextProp
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, colorSignature);

    TextProperties arrowLamportVerTextProp = new TextProperties();
    arrowLamportVerTextProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        colorVerification2);

    TextProperties arrowHashTextProp = new TextProperties();
    arrowHashTextProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorHash);

    PolylineProperties arrowCounterProp = new PolylineProperties();
    arrowCounterProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrowCounterProp
        .set(AnimationPropertiesKeys.BWARROW_PROPERTY, Boolean.TRUE);

    PolylineProperties arrowSigProp = new PolylineProperties();
    arrowSigProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorSignature);
    arrowSigProp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, Boolean.TRUE);

    PolylineProperties arrowLamportVerProp = new PolylineProperties();
    arrowLamportVerProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        colorVerification2);
    arrowLamportVerProp.set(AnimationPropertiesKeys.BWARROW_PROPERTY,
        Boolean.TRUE);

    PolylineProperties arrowHashProp = new PolylineProperties();
    arrowHashProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorHash);
    arrowHashProp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, Boolean.TRUE);

    Node[] arrowCounterArray = { new Coordinates(30, 400),
        new Coordinates(30, 520) };
    Polyline arrowCounter = lang.newPolyline(arrowCounterArray, "arrowCounter",
        new Hidden(), arrowCounterProp);
    descriptionList.add(arrowCounter);
    Node[] arrowSigArray = { new Offset(25, 0, arrowCounter, "NW"),
        new Offset(25, -20, arrowCounter, "SW") };
    Polyline arrowSig = lang.newPolyline(arrowSigArray, "arrowSig",
        new Hidden(), arrowSigProp);
    descriptionList.add(arrowSig);
    Node[] arrowLamportVerArray = { new Offset(30, 0, arrowSig, "NW"),
        new Offset(30, -20, arrowSig, "SW") };
    Polyline arrowLamportVer = lang.newPolyline(arrowLamportVerArray,
        "arrowLamportVer", new Hidden(), arrowLamportVerProp);
    descriptionList.add(arrowLamportVer);
    Node[] arrowHashArray = { new Offset(35, 0, arrowLamportVer, "NW"),
        new Offset(35, -20, arrowLamportVer, "SW") };
    Polyline arrowHash = lang.newPolyline(arrowHashArray, "arrowLamportVer",
        new Hidden(), arrowHashProp);
    descriptionList.add(arrowHash);
    Text arrowCounterText = lang.newText(
        new Offset(10, -10, arrowCounter, "SE"),
        "Zähler i (Zustand), wird bei jeder Signaturerzeugung hochgezählt",
        "arrowCounterText", new Hidden(), arrowCounterTextProp);
    descriptionList.add(arrowCounterText);
    Text arrowSigText = lang
        .newText(
            new Offset(10, -10, arrowSig, "SE"),
            "Signatur von Dokument d aus Einmalsignaturverfahren unter Nutzung von xi",
            "arrowSigText", new Hidden(), arrowSigTextProp);
    descriptionList.add(arrowSigText);
    Text arrowLamportVerText = lang.newText(new Offset(10, -10,
        arrowLamportVer, "SE"),
        "Verifikationsschlüssel des Einmalsignaturverfahrens",
        "arrowLamportVerText", new Hidden(), arrowLamportVerTextProp);
    descriptionList.add(arrowLamportVerText);
    Text arrowHashText = lang.newText(new Offset(10, -10, arrowHash, "SE"),
        "Authentisierungspfad im Hashbaum", "arrowHashText", new Hidden(),
        arrowHashTextProp);
    descriptionList.add(arrowHashText);

    Group descriptionGroup = lang.newGroup(descriptionList, "descriptionGroup");

    TextProperties sProp2 = new TextProperties();
    sProp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.DARK_GRAY);
    sProp2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 14));

    lang.nextStep();

    /*
     * Show decriptions
     */
    arrowCounterText.show(new TicksTiming(20));
    arrowCounter.show(new TicksTiming(20));
    arrowSigText.show(new TicksTiming(40));
    arrowSig.show(new TicksTiming(40));
    arrowLamportVerText.show(new TicksTiming(60));
    arrowLamportVer.show(new TicksTiming(60));
    arrowHashText.show(new TicksTiming(80));
    arrowHash.show(new TicksTiming(80));

    String str = "(0, s0, y0, (" + hash[aN[0][0]] + ", " + hash[aN[0][1]]
        + ", " + hash[aN[0][2]] + "))";
    LinkedList<Primitive> sigList = new LinkedList<Primitive>();
    Text sig0 = lang.newText(new Offset(0, 330, header, "SW"), str, "s0",
        new Hidden(), sProp2); // 67
    sig0.show(new TicksTiming(0));
    sigList.add(sig0);

    merkleTree.highlightNode(aN[0][0], null, new TicksTiming(80));
    merkleTree.highlightNode(aN[0][1], null, new TicksTiming(80));
    merkleTree.highlightNode(aN[0][2], null, new TicksTiming(80));
    merkleTree.highlightEdge(aEdges[0][0], aEdges[0][1], null, new TicksTiming(
        20));
    merkleTree.highlightEdge(aEdges[0][1], aEdges[0][2], null, new TicksTiming(
        40));
    merkleTree.highlightEdge(aEdges[0][2], root, null, new TicksTiming(60));
    lang.nextStep();

    int yOffset = 0;
    for (int i = 1; i < 8; i++) {
      // both directions
      merkleTree
          .unhighlightEdge(aEdges[i - 1][1], aEdges[i - 1][0], null, null);
      merkleTree
          .unhighlightEdge(aEdges[i - 1][2], aEdges[i - 1][1], null, null);
      merkleTree.unhighlightEdge(root, aEdges[i - 1][2], null, null);
      merkleTree.highlightEdge(aEdges[i][1], aEdges[i][0], null,
          new TicksTiming(20));
      merkleTree.highlightEdge(aEdges[i][2], aEdges[i][1], null,
          new TicksTiming(40));
      merkleTree.highlightEdge(root, aEdges[i][2], null, new TicksTiming(60));

      merkleTree
          .unhighlightEdge(aEdges[i - 1][0], aEdges[i - 1][1], null, null);
      merkleTree
          .unhighlightEdge(aEdges[i - 1][1], aEdges[i - 1][2], null, null);
      merkleTree.unhighlightEdge(aEdges[i - 1][2], root, null, null);
      merkleTree.highlightEdge(aEdges[i][0], aEdges[i][1], null,
          new TicksTiming(20));
      merkleTree.highlightEdge(aEdges[i][1], aEdges[i][2], null,
          new TicksTiming(40));
      merkleTree.highlightEdge(aEdges[i][2], root, null, new TicksTiming(60));

      str = "(" + i + ", y" + i + ", s" + i + ", (" + hash[aN[i][0]] + ", "
          + hash[aN[i][1]] + ", " + hash[aN[i][2]] + "))";
      Text sig = lang.newText(new Offset(0 + i * 120, 0 + 30 * (i % 2), sig0,
          "NW"), str, "s" + i, new Hidden(), sProp2);
      sig.show(new TicksTiming(0));
      sigList.add(sig);
      merkleTree.unhighlightNode(aN[i - 1][0], null, null);
      merkleTree.unhighlightNode(aN[i - 1][1], null, null);
      merkleTree.unhighlightNode(aN[i - 1][2], null, null);
      merkleTree.highlightNode(aN[i][0], null, new TicksTiming(80));
      merkleTree.highlightNode(aN[i][1], null, new TicksTiming(80));
      merkleTree.highlightNode(aN[i][2], null, new TicksTiming(80));

      yOffset = ((i % 2) == 0) ? -30 : 30;
      descriptionGroup.moveTo("NW", "translate", new Offset(120, yOffset,
          descriptionGroup, "NW"), null, new TicksTiming(30));

      lang.nextStep();

    }
    Group sigGroup = lang.newGroup(sigList, "sigGroup");

    // Document signed
    TextProperties docSignedProp = new TextProperties();
    docSignedProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 16));
    docSignedProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorSignature);
    Text docSigned = lang.newText(new Coordinates(420, 470),
        "8 Signaturen erzeugt", "docSigned", null, docSignedProp);
    RectProperties docSignedRectProp = new RectProperties();
    docSignedRectProp
        .set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    docSignedRectProp
        .set(AnimationPropertiesKeys.FILL_PROPERTY, colorSignature);
    docSignedRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        colorSignature);
    docSignedRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect docSignedRect = lang.newRect(new Offset(0, 2, docSigned, "SW"),
        new Offset(0, 4, docSigned, "SE"), "docSignedRect", null,
        docSignedRectProp);

    lang.nextStep();

    // Document verified
    TextProperties docVerifiedProp = new TextProperties();
    docVerifiedProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 16));
    docVerifiedProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        colorVerification);
    Text docVerified = lang.newText(new Coordinates(100, 600),
        "Dokument verifiziert", "docSigned", null, docVerifiedProp);
    RectProperties docVerifiedRectProp = new RectProperties();
    docVerifiedRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY,
        Boolean.TRUE);
    docVerifiedRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
        colorVerification);
    docVerifiedRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        colorVerification);
    docVerifiedRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect docVerifiedRect = lang.newRect(new Offset(0, 2, docVerified, "SW"),
        new Offset(0, 4, docVerified, "SE"), "docSignedRect", null,
        docVerifiedRectProp);
    docVerifiedRect.hide();
    docVerified.hide();

    lang.nextStep("Verifikation");

    merkleTree.unhighlightNode(aN[7][0], null, null);
    merkleTree.unhighlightNode(aN[7][1], null, null);
    merkleTree.unhighlightNode(aN[7][2], null, null);
    merkleTree.unhighlightEdge(aEdges[7][1], aEdges[7][0], null, null);
    merkleTree.unhighlightEdge(aEdges[7][2], aEdges[7][1], null, null);
    merkleTree.unhighlightEdge(root, aEdges[7][2], null, null);
    merkleTree.unhighlightEdge(aEdges[7][0], aEdges[7][1], null, null);
    merkleTree.unhighlightEdge(aEdges[7][1], aEdges[7][2], null, null);
    merkleTree.unhighlightEdge(aEdges[7][2], root, null, null);

    /*
     * Verification
     */
    Group xGroup = lang.newGroup(xList, "xGroup");
    sigKey.hide(new TicksTiming(70));
    xGroup.hide(new TicksTiming(70));
    sGroup.hide(new TicksTiming(65));
    docSignedRect.hide(new TicksTiming(60));
    docSigned.hide(new TicksTiming(60));
    sigGroup.hide(new TicksTiming(40));
    descriptionGroup.hide(new TicksTiming(20));
    caption.setText("Verifikation", null, null);

    String iBin = "";
    lang.nextStep();
    RectProperties vRectProp = new RectProperties();
    vRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    vRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    vRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    vRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);

    // Verification decription text
    Text verificationText0 = lang.newText(new Offset(650, 300, header, "SW"),
        "Zunächst wird si mit dem Verifikationsschlüssel yi verfiziert.",
        "verificationText0", null, descTextProp);
    Text verificationText1 = lang
        .newText(
            new Offset(0, 5, verificationText0, "SW"),
            "Bei Erfolg wird die Gültigkeit des Verifikationsschlüssel yi überprüft:",
            "verificationText1", null, descTextProp);
    Text verificationText2 = lang.newText(new Offset(0, 5, verificationText1,
        "SW"), "Dazu wird i (der Zustand) und der Authentisierungspfad",
        "verificationText2", null, descTextProp);
    Text verificationText3 = lang.newText(new Offset(0, 5, verificationText2,
        "SW"), "(ah, ..., a1) verwendet (wobei h die Höhe des Hashbaums ist).",
        "verificationText3", null, descTextProp);
    Text verificationText4 = lang.newText(new Offset(0, 10, verificationText3,
        "SW"), "Für die Position des Nachbarn für die Berechnung des nächsten",
        "verificationText4", null, descTextProp);
    Text verificationText5 = lang.newText(new Offset(0, 5, verificationText4,
        "SW"),
        "Vaterknotens b(j-1) gilt: In der Binärdarstellung i(0)...i(n) ist ",
        "verificationText5", null, descTextProp);
    Text verificationText6 = lang.newText(new Offset(0, 5, verificationText5,
        "SW"),
        "für i(j) = 0 der Nachbarknoten rechts, für i(j) = 1 ist er links.",
        "verificationText6", null, descTextProp);
    Rect vRect = lang.newRect(new Offset(-5, -5, verificationText0, "NW"),
        new Offset(58, 5, verificationText6, "SE"), "vRect", null, vRectProp);

    LinkedList<Primitive> verficationList = new LinkedList<Primitive>();
    TextProperties verLineProp = new TextProperties();
    verLineProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    verLineProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));

    // Steps
    for (int i = 0; i < 8; i++) {
      str = "(i, si, yi, (a3, a2, a1))   =   (0, s" + i + ", y" + i + ", (a3="
          + hashValue[aN[i][0]] + ", a2=" + hashValue[aN[i][1]] + ", a1="
          + hashValue[aN[i][2]] + "))";
      Text verLine0 = lang.newText(new Offset(60, 265, header, "SW"), str,
          "verLine0-" + i, null, verLineProp);
      verficationList.add(verLine0);
      lang.nextStep();
      str = "1) Verifiziere s" + i + " unter Verwendung von y" + i
          + " (LD-OTS)";
      Text verLine1 = lang.newText(new Offset(0, 20, verLine0, "SW"), str,
          "verLine1-" + i, null, verLineProp);
      verficationList.add(verLine1);
      lang.nextStep();
      iBin = i2bin(i);
      str = "2) Validiere Verifikationsschlüssel y" + i;
      Text verLine2 = lang.newText(new Offset(0, 10, verLine1, "SW"), str,
          "verLine2-" + i, null, verLineProp);
      verficationList.add(verLine2);
      lang.nextStep();
      str = "Binärentwicklung von i=" + i + ": " + iBin;
      Text verLine3 = lang.newText(new Offset(20, 10, verLine2, "SW"), str,
          "verLine3-" + i, null, verLineProp);
      verficationList.add(verLine3);
      lang.nextStep();
      merkleTree.highlightNode(aEdges[i][0], null, new TicksTiming(80));
      str = "b3  =  H(y" + i + ")  =  " + hashValue[aEdges[i][0]];
      Text verLine4 = lang.newText(new Offset(50, 10, verLine3, "SW"), str,
          "verLine4-" + i, null, verLineProp);
      verficationList.add(verLine4);
      lang.nextStep();
      str = "Ebene 3:  i3 = " + iBin.charAt(3) + "  :  "
          + (iBin.charAt(3) == '0' ? "rechter" : "linker")
          + " Nachbar  ->  b2 = H("
          + (iBin.charAt(3) == '0' ? "b3||a3" : "a3||b3") + ") = "
          + hashValue[aEdges[0][1]];
      merkleTree.highlightEdge(aEdges[i][1], aEdges[i][0], null,
          new TicksTiming(20));
      merkleTree.highlightEdge(aEdges[i][0], aEdges[i][1], null,
          new TicksTiming(20));
      Text verLine5 = lang.newText(new Offset(20, 10, verLine4, "SW"), str,
          "verLine5-" + i, null, verLineProp);
      merkleTree.highlightNode(aN[i][0], null, new TicksTiming(80));
      verficationList.add(verLine5);
      lang.nextStep();
      str = "Ebene 2:  i2 = " + iBin.charAt(2) + "  :  "
          + (iBin.charAt(2) == '0' ? "rechter" : "linker")
          + " Nachbar  ->  b1 = H("
          + (iBin.charAt(2) == '0' ? "b2||a2" : "a2||b2") + ") = "
          + hashValue[aEdges[0][2]];
      merkleTree.highlightNode(aEdges[i][1], null, new TicksTiming(80));
      merkleTree.highlightEdge(aEdges[i][1], aEdges[i][2], null,
          new TicksTiming(20));
      merkleTree.highlightEdge(aEdges[i][2], aEdges[i][1], null,
          new TicksTiming(20));
      Text verLine6 = lang.newText(new Offset(0, 10, verLine5, "SW"), str,
          "verLine6-" + i, null, verLineProp);
      merkleTree.highlightNode(aN[i][1], null, new TicksTiming(80));
      verficationList.add(verLine6);

      lang.nextStep();
      str = "Ebene 1:  i1 = " + iBin.charAt(1) + "  :  "
          + (iBin.charAt(1) == '0' ? "rechter" : "linker")
          + " Nachbar  ->  b0 = H("
          + (iBin.charAt(1) == '0' ? "b1||a1" : "a1||b1") + ") = "
          + hashValue[root];

      merkleTree.highlightNode(aEdges[i][2], null, new TicksTiming(80));
      merkleTree.highlightEdge(root, aEdges[i][2], null, new TicksTiming(20));
      merkleTree.highlightEdge(aEdges[i][2], root, null, new TicksTiming(20));
      Text verLine7 = lang.newText(new Offset(0, 10, verLine6, "SW"), str,
          "verLine7-" + i, null, verLineProp);
      merkleTree.highlightNode(aN[i][2], null, new TicksTiming(80));
      verficationList.add(verLine7);
      lang.nextStep();
      str = "R  =  b0 ?";
      Text verLine8 = lang.newText(new Offset(-50, 10, verLine7, "SW"), str,
          "verLine8-" + i, null, verLineProp);
      merkleTree.highlightNode(root, null, new TicksTiming(80));
      verficationList.add(verLine8);

      lang.nextStep();
      docVerifiedRect.show();
      docVerified.show();

      lang.nextStep();
      Group verficationGroup = lang.newGroup(verficationList,
          "verfificationGroup-" + i);
      verficationGroup.hide();
      docVerifiedRect.hide(new TicksTiming(20));
      docVerified.hide(new TicksTiming(20));

      merkleTree.unhighlightNode(aEdges[i][0], null, new TicksTiming(80));
      merkleTree.unhighlightEdge(aEdges[i][1], aEdges[i][0], null,
          new TicksTiming(20));
      merkleTree.unhighlightEdge(aEdges[i][0], aEdges[i][1], null,
          new TicksTiming(20));
      merkleTree.unhighlightNode(aN[i][0], null, new TicksTiming(80));
      merkleTree.unhighlightNode(aEdges[i][1], null, new TicksTiming(80));
      merkleTree.unhighlightEdge(aEdges[i][1], aEdges[i][2], null,
          new TicksTiming(20));
      merkleTree.unhighlightEdge(aEdges[i][2], aEdges[i][1], null,
          new TicksTiming(20));
      merkleTree.unhighlightNode(aN[i][1], null, new TicksTiming(80));
      merkleTree.unhighlightNode(aEdges[i][2], null, new TicksTiming(80));
      merkleTree.unhighlightEdge(root, aEdges[i][2], null, new TicksTiming(20));
      merkleTree.unhighlightEdge(aEdges[i][2], root, null, new TicksTiming(20));
      merkleTree.unhighlightNode(aN[i][2], null, new TicksTiming(80));
      merkleTree.unhighlightNode(root, null, new TicksTiming(80));

    }
    // Hide all
    verificationText0.hide();
    verificationText1.hide();
    verificationText2.hide();
    verificationText3.hide();
    verificationText4.hide();
    verificationText5.hide();
    verificationText6.hide();
    vRect.hide();

    TextProperties docAllVerProp = new TextProperties();
    docAllVerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 16));
    docAllVerProp
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, colorVerification);
    Text docAllVer = lang.newText(new Coordinates(420, 470),
        "Dokument von 8 Empfängern verifiziert", "docAllVer", null,
        docAllVerProp);
    RectProperties docAllVerRectProp = new RectProperties();
    docAllVerRectProp
        .set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    docAllVerRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
        colorVerification);
    docAllVerRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        colorVerification);
    docAllVerRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(0, 2, docAllVer, "SW"), new Offset(0, 4, docAllVer,
        "SE"), "docAllVerRect", null, docAllVerRectProp);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    colorVerification = (Color) primitives.get("colorVerification");
    colorSigning = (Color) primitives.get("colorSigning");
    colorVerification2 = (Color) primitives.get("colorVerification2");
    leafNodes = (String[]) primitives.get("leafNodes");
    colorHashHighlight = (Color) primitives.get("colorHashHighlight");
    colorHash = (Color) primitives.get("colorHash");
    colorSignature = (Color) primitives.get("colorSignature");

    try {
      startAnimation();
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }
    return lang.toString();
  }

  public String getName() {
    return "Merkle-Verfahren";
  }

  public String getAlgorithmName() {
    return "Merkle-Signatur";
  }

  public String getAnimationAuthor() {
    return "Nikolaus Korfhage";
  }

  public String getDescription() {
    return "Das Merkle-Verfahren ist ein Signaturverfahren, das auf Merkle-B&auml;umen sowie Einmalsignaturen (z.B. Lamport-Einmalsignaturen) basiert."
        + "\n"
        + "Es wurde von Ralph Merkle entwickelt und ist eine Alternative zu digitalen Signaturen wie dem Digital Signature Algorithm oder RSA-Kryptosystem."
        + "\n"
        + "Es wird f&uuml;r resistent gegen Angriffe durch Quantencomputer gehalten."
        + "\n"
        + "Das Merkle-Signaturverfahren h&auml;ngt nur von der Existenz sicherer Hashfunktionen ab und ist damit &auml;u&szlig;erst anpassbar und resistent gegen Quanten-Computing.";
  }

  public String getCodeExample() {
    return "1. Schl&uuml;sselerzeugung"
        + "\n"
        + "   Es k&ouml;nnen N = 2<SUP>n</SUP> Nachrichten mit einem einzigen &ouml;ffentlichen Schl&uuml;ssel signiert werden."
        + "\n"
        + "   Mit einem Einmalsignaturverfahren werden Signier- und Verifikationsschl&uuml;sselpaar (x<SUB>i</SUB>, y<SUB>i</SUB>) berechnet."
        + "\n"
        + "   Die Blattknoten sind H(y<SUB>i</SUB>) wobei y<SUB>i</SUB> der Verifikationsschl&uuml;ssel eines Einmalsignaturverfahren ist."
        + "\n"
        + "   Danach werden durch Konkatenation der Kindknoten die entsprechenden Vaterknoten berechnet,"
        + "\n"
        + "   bis zur Wurzel - diese ist der Public-Key."
        + "\n"
        + "\n"
        + "2. Signierung"
        + "\n"
        + "   Mit dem jeweiligen Signierschl&uuml;ssel x<SUB>i</SUB> wird eine Signatur s<SUB>i</SUB> des Dokumentes berechnet."
        + "\n"
        + "   Es wird ein Z&auml;hler i verwendet, der f&uuml;r jede Signatur erh&ouml;ht wird."
        + "\n"
        + "   Die Signatur ist dann (i, s<SUB>i</SUB>, y<SUB>i</SUB>, (a<SUB>h</SUB>, ..., a<SUB>1</SUB>)), wobei i die Nummer des Blattknoten und (a<SUB>h</SUB>, ..., a<SUB>1</SUB>) "
        + "\n"
        + "   der Authentifizierungspfad ist und h die H&ouml;he des Baumes."
        + "\n"
        + "   Dieser wird f&uuml;r die Verifikation ben&ouml;tigt und besteht aus den jeweiligen Nachbarknoten auf dem Pfad vom Blatt zur Wurzel."
        + "\n"
        + "\n"
        + "3. Verifikation"
        + "\n"
        + "   Es wird gepr&uuml;ft, ob s<SUB>i</SUB> eine g&uuml;ltige Signatur ist (mit dem Verifikationsschl&uuml;ssel y<SUB>i</SUB>)"
        + "\n"
        + "   Mit der Position i und dem Authentifizierungspfad (a<SUB>h</SUB>, ..., a<SUB>1</SUB>) wird verifiziert, ob y<SUB>i</SUB> ein g&uuml;ltiger "
        + "\n"
        + "   Verifikationsschl&uuml;ssel ist, indem der Pfad zur&uuml;ck zur Wurzel berechnet wird. Ist die Wurzel der Public-Key"
        + "\n" + "   wird die Signatur akzeptiert. ";
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

}