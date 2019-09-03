package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Ellipse;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

/*
 * Implementation of the Visualization of the Minmax Algorithm
 * @author paskuda, zoeller
 */

public class MinMax implements Generator {
  private Language lang;
  private int      Blatt2;
  private int      Blatt1;
  private int      Blatt4;
  private int      Blatt3;

  /*
   * init method
   */
  public void init() {
    lang = new AnimalScript("MinMax [DE]", "Barbara Zöller, Malte Paskuda",
        900, 768);
  }

  /*
   * generate
   * 
   * @param props, primitives
   * 
   * @return String AnimalScript of the visualization
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    Blatt2 = (Integer) primitives.get("Blatt2");
    Blatt1 = (Integer) primitives.get("Blatt1");
    Blatt4 = (Integer) primitives.get("Blatt4");
    Blatt3 = (Integer) primitives.get("Blatt3");

    int[] leafs = new int[] { Blatt1, Blatt2, Blatt3, Blatt4 };
    lang.setStepMode(true);
    this.miniMax(leafs);

    return lang.toString();
  }

  /*
   * miniMax: implementation of the visualization of the minimax Algorithm
   * 
   * @param leafs
   */
  public void miniMax(int[] leafs) {
    int minlValue;
    int minrValue;
    int rootValue;
    String path = "";
    String pathL = "";
    String pathR = "";

    // actual implementation
    // to get the values for the inner nodes
    if (leafs[0] < leafs[1]) {
      minlValue = leafs[0];
      pathL = "l1";
    } else {
      minlValue = leafs[1];
      pathL = "l2";
    }
    if (leafs[2] < leafs[3]) {
      minrValue = leafs[2];
      pathR = "r1";
    } else {
      minrValue = leafs[3];
      pathR = "r2";
    }

    if (minlValue < minrValue) {
      rootValue = minrValue;
      path = "r";
    } else {
      rootValue = minlValue;
      path = "l";
    }
    // header
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30), "MiniMax Animation",
        "header", new MsTiming(0), tp);

    Rect headerBackground = lang.newRect(new Offset(-10, -10, header, "NW"),
        new Offset(10, 10, header, "SE"), "headerBackground", new MsTiming(0));
    headerBackground.changeColor("color", Color.BLACK, null, null);

    // description
    Text intro1 = lang
        .newText(
            new Offset(0, 10, header, "South"),
            "Der Minimax Algorithmus wird bei Spielen zum Finden einer optimalen Strategie verwendet,",
            "description", new MsTiming(0));

    Text intro2 = lang
        .newText(
            new Offset(0, 10, intro1, "SW"),
            "die zu einem Ergebnis führt, das mindestens so gut ist wie jede andere Strategie,",
            "description", new MsTiming(0));
    Text intro3 = lang.newText(new Offset(0, 10, intro2, "SW"),
        "wenn man gegen einen unfehlbaren Gegner spielt.", "description",
        new MsTiming(0));
    Text intro4 = lang.newText(new Offset(0, 10, intro3, "SW"),
        " Dazu wird ein Spielbaum (Suchbaum) definiert:", "description",
        new MsTiming(0));
    Text intro5 = lang.newText(new Offset(0, 10, intro4, "SW"),
        " der Ausgangszustand gibt an, welcher Spieler am Zug ist;",
        "description", new MsTiming(0));
    Text intro6 = lang.newText(new Offset(0, 10, intro5, "SW"),
        " die möglichen Züge werden mit einer Nachfolgefunktion angegeben;",
        "description", new MsTiming(0));
    Text intro7 = lang
        .newText(
            new Offset(0, 10, intro6, "SW"),
            " die Nutzenfunktion bewertet die Endzustände des Spiels aus Sicht des Spielers, der am Zug ist.",
            "description", new MsTiming(0));
    Text intro8 = lang
        .newText(
            new Offset(0, 10, intro7, "SW"),
            " Die beiden Spieler verfolgen unterschiedliche Ziele, denn jeder möchte das Spiel gewinnen ",
            "description", new MsTiming(0));
    Text intro9 = lang.newText(new Offset(0, 10, intro8, "SW"),
        " und spielen daher gegeneienander.", "description", new MsTiming(0));
    Text intro10 = lang.newText(new Offset(0, 10, intro9, "SW"),
        " Dafür werden sie in Min- und Maxspieler unterteilt.", "description",
        new MsTiming(0));
    Text intro11 = lang
        .newText(
            new Offset(0, 10, intro10, "SW"),
            " Der Maxspieler möchte möglichst viele Punkte erreichen, der Minspieler hingegen",
            "description", new MsTiming(0));
    Text intro12 = lang.newText(new Offset(0, 10, intro11, "SW"),
        " möglichst wenige.", "description", new MsTiming(0));
    Text intro13 = lang
        .newText(
            new Offset(0, 10, intro12, "SW"),
            " Um die optimale Strategie zu finden, müssen beide Spieler optimal spielen,",
            "description", new MsTiming(0));
    Text intro14 = lang
        .newText(
            new Offset(0, 10, intro13, "SW"),
            " also immer den bestmöglichen Zug entsprechend ihres Zieles (minimieren bzw. maximieren)",
            "description", new MsTiming(0));
    Text intro15 = lang
        .newText(
            new Offset(0, 10, intro14, "SW"),
            " Der MiniMax-Algor. verwendet eine einfache rekursive Berechnung, die bis in die ",
            "description", new MsTiming(0));
    Text intro16 = lang
        .newText(
            new Offset(0, 10, intro15, "SW"),
            " Blätter (Endzustände) des Baumes verläuft und die Minimax-Werte durch den Baum nach",
            "description", new MsTiming(0));
    Text intro17 = lang.newText(new Offset(0, 10, intro16, "SW"),
        " oben zum Ausgangszustand weiterleitet.", "description", new MsTiming(
            0));
    Text intro18 = lang
        .newText(
            new Offset(0, 10, intro17, "SW"),
            " Somit kann z.B. für das Spiel Tic-Tac-Toe ein Suchbaum aus jeder beliebigen Spielposition",
            "description", new MsTiming(0));
    Text intro19 = lang.newText(new Offset(0, 10, intro18, "SW"),
        " aufgestellt werden.", "description", new MsTiming(0));
    Text intro20 = lang
        .newText(
            new Offset(0, 10, intro19, "SW"),
            " Dazu werden zunächst alle möglichen Züge ermittelt um dann von diesen wieder",
            "description", new MsTiming(0));
    Text intro21 = lang
        .newText(
            new Offset(0, 10, intro20, "SW"),
            " die darauffolgenden Zustände aufzustellen. Das wird solange fortgeführt, bis ",
            "description", new MsTiming(0));
    Text intro22 = lang
        .newText(
            new Offset(0, 10, intro21, "SW"),
            " Endzustände erreicht sind. Diese müssen noch bewertet werden (mit einer ",
            "description", new MsTiming(0));
    Text intro23 = lang
        .newText(
            new Offset(0, 10, intro22, "SW"),
            " Bewertungsunktion) um damit dann den MiniMax-Wert des Ausgangszustandes zu bewerten.",
            "description", new MsTiming(0));
    lang.nextStep("Ausgangssituation");
    // overwrites the description text
    intro1.setText("", null, null);
    intro2.setText("", null, null);
    intro3.setText("", null, null);
    intro4.setText("", null, null);
    intro5.setText("", null, null);
    intro6.setText("", null, null);
    intro7.setText("", null, null);
    intro8.setText("", null, null);
    intro9.setText("", null, null);
    intro10.setText("", null, null);
    intro11.setText("", null, null);
    intro12.setText("", null, null);
    intro13.setText("", null, null);
    intro14.setText("", null, null);
    intro15.setText("", null, null);
    intro16.setText("", null, null);
    intro17.setText("", null, null);
    intro18.setText("", null, null);
    intro19.setText("", null, null);
    intro20.setText("", null, null);
    intro21.setText("", null, null);
    intro22.setText("", null, null);
    intro23.setText("", null, null);

    // graph
    // triangles for the graph
    MsTiming basicDisplay = new MsTiming(0);
    Triangle root = lang.newTriangle(new Coordinates(300, 150),
        new Coordinates(250, 200), new Coordinates(350, 200), "root",
        basicDisplay);
    Triangle minl = lang.newTriangle(new Offset(-100, 150, root, "SW"),
        new Offset(-150, 100, root, "SW"), new Offset(-50, 100, root, "SW"),
        "minl", basicDisplay);
    Triangle minr = lang.newTriangle(new Offset(100, 150, root, "SE"),
        new Offset(150, 100, root, "SE"), new Offset(50, 100, root, "SE"),
        "minr", basicDisplay);
    // lines between root and min nodes
    Offset rootOffsetSouth = new Offset(-5, 5, root, "S");
    Offset minlOffsetNorth = new Offset(5, -5, minl, "N");
    Offset[] positions = new Offset[] { rootOffsetSouth, minlOffsetNorth };
    Polyline l = lang.newPolyline(positions, "l", basicDisplay);

    rootOffsetSouth = new Offset(5, 5, root, "S");
    Offset minrOffsetNorth = new Offset(-5, -5, minr, "N");
    positions = new Offset[] { rootOffsetSouth, minrOffsetNorth };
    Polyline r = lang.newPolyline(positions, "r", basicDisplay);
    // leafs
    Offset offsetLeft1Node = new Offset(-20, 50, minl, "S");
    Text leaf1l = lang.newText(offsetLeft1Node, "" + leafs[0], "leaf1l",
        basicDisplay);
    Offset offsetLeft2Node = new Offset(20, 50, minl, "S");
    Text leaf2l = lang.newText(offsetLeft2Node, "" + leafs[1], "leaf2l",
        basicDisplay);
    Offset offsetRight1Node = new Offset(-20, 50, minr, "S");
    Text leaf1r = lang.newText(offsetRight1Node, "" + leafs[2], "leaf3l",
        basicDisplay);
    Offset offsetRight2Node = new Offset(20, 50, minr, "S");
    Text leaf2r = lang.newText(offsetRight2Node, "" + leafs[3], "leaf4l",
        basicDisplay);
    // lines between min-nodes and leafs
    Offset minlOffsetSouthleft = new Offset(-5, 5, minl, "S");
    Offset minlOffsetSouthright = new Offset(5, 5, minl, "S");
    Offset minrOffsetSouthleft = new Offset(-5, 5, minr, "S");
    Offset minrOffsetSouthright = new Offset(5, 5, minr, "S");
    Offset leaf1lOffsetNorth = new Offset(5, -5, leaf1l, "N");
    Offset leaf2lOffsetNorth = new Offset(-5, -5, leaf2l, "N");
    Offset leaf1rOffsetNorth = new Offset(5, -5, leaf1r, "N");
    Offset leaf2rOffsetNorth = new Offset(-5, -5, leaf2r, "N");

    positions = new Offset[] { minlOffsetSouthleft, leaf1lOffsetNorth };
    Polyline l1 = lang.newPolyline(positions, "l1", basicDisplay);
    positions = new Offset[] { minlOffsetSouthright, leaf2lOffsetNorth };
    Polyline l2 = lang.newPolyline(positions, "l2", basicDisplay);
    positions = new Offset[] { minrOffsetSouthleft, leaf1rOffsetNorth };
    Polyline r1 = lang.newPolyline(positions, "r1", basicDisplay);
    positions = new Offset[] { minrOffsetSouthright, leaf2rOffsetNorth };
    Polyline r2 = lang.newPolyline(positions, "r2", basicDisplay);

    // declaration of the triangle symbols
    Triangle max = lang.newTriangle(new Coordinates(800, 50), new Coordinates(
        785, 65), new Coordinates(815, 65), "max", basicDisplay);
    Offset offsetMax = new Offset(10, -10, max, "E");
    lang.newText(offsetMax, "Max-Spieler", "Max-Spieler", basicDisplay);
    Triangle min = lang.newTriangle(new Offset(0, 50, max, "S"), new Offset(0,
        35, max, "SW"), new Offset(0, 35, max, "SE"), "min", basicDisplay);
    Offset offsetMin = new Offset(10, -10, min, "E");
    lang.newText(offsetMin, "Min-Spieler", "Min-Spieler", basicDisplay);

    lang.nextStep();
    SourceCode sc = this.paintSourceCode(root);

    // decision tree
    // nodes
    Rect blatt = lang.newRect(new Offset(40, 40, min, "S"), new Offset(90, 80,
        min, "S"), "Blatt?", basicDisplay);
    lang.newText(new Offset(-10, -2, blatt, "Center"), "Blatt?", "Blatt?",
        basicDisplay);
    Ellipse evaluate = lang.newEllipse(new Offset(-40, 80, blatt, "S"),
        new Coordinates(35, 10), "evaluate", basicDisplay);
    lang.newText(new Offset(-17, -6, evaluate, "Center"), "evaluate",
        "evaluate", basicDisplay);
    Rect nodeArt = lang.newRect(new Offset(30, 60, blatt, "S"), new Offset(110,
        100, blatt, "S"), "Spielertyp?", basicDisplay);
    lang.newText(new Offset(-30, -4, nodeArt, "Center"), "Spielertyp?",
        "Spielertyp?", basicDisplay);
    Ellipse maxNode = lang.newEllipse(new Offset(-70, 70, nodeArt, "S"),
        new Coordinates(60, 20), "max", basicDisplay);
    lang.newText(new Offset(-33, -5, maxNode, "Center"), "Größtes Kind", "max",
        basicDisplay);
    Ellipse minNode = lang.newEllipse(new Offset(70, 70, nodeArt, "S"),
        new Coordinates(60, 20), "min", basicDisplay);
    lang.newText(new Offset(-33, -5, minNode, "Center"), "Kleinstes Kind",
        "min", basicDisplay);

    Offset blattSW = new Offset(-5, 5, blatt, "S");
    Offset blattSE = new Offset(5, 5, blatt, "S");
    Offset nodeArtSW = new Offset(-5, 5, nodeArt, "S");
    Offset nodeArtSE = new Offset(5, 5, nodeArt, "S");

    Offset nodeArtN = new Offset(0, -5, nodeArt, "N");
    Offset evaluateN = new Offset(0, -5, evaluate, "N");
    Offset minNodeN = new Offset(0, -5, minNode, "N");
    Offset maxNodeN = new Offset(0, -5, maxNode, "N");
    // lines
    positions = new Offset[] { blattSW, evaluateN };
    Polyline leva = lang.newPolyline(positions, "leva", basicDisplay);
    positions = new Offset[] { blattSE, nodeArtN };
    Polyline lnodeArt = lang.newPolyline(positions, "lnodAart", basicDisplay);
    positions = new Offset[] { nodeArtSW, maxNodeN };
    Polyline lmaxNode = lang.newPolyline(positions, "lmaxNode", basicDisplay);
    positions = new Offset[] { nodeArtSE, minNodeN };
    Polyline lminNode = lang.newPolyline(positions, "lminNode", basicDisplay);
    // text
    lang.newText(new Offset(-12, -10, leva, "Center"), "Ja", "Ja", basicDisplay);
    lang.newText(new Offset(15, -2, lnodeArt, "Center"), "Nein", "Nein",
        basicDisplay);
    lang.newText(new Offset(-32, -5, lmaxNode, "Center"), "Max", "Max",
        basicDisplay);
    lang.newText(new Offset(17, -2, lminNode, "Center"), "Min", "Min",
        basicDisplay);

    // start of the visualization
    lang.nextStep("Start des Algorithmus");
    this.highlight(root);
    sc.highlight(1, 0, false);
    this.highlight(blatt);

    lang.nextStep();
    sc.unhighlight(1);
    sc.highlight(3, 0, false);
    this.unhighlight(blatt);
    this.highlight(nodeArt);

    lang.nextStep();
    sc.unhighlight(3);
    sc.highlight(4, 0, false);
    Offset offset = new Offset(10, 0, root, "E");
    // set initial value of V
    Text rootV = lang.newText(offset, "V: -unendlich", "V: -unendlich",
        new MsTiming(0));
    this.unhighlight(nodeArt);
    this.highlight(maxNode);

    lang.nextStep();
    sc.unhighlight(4);
    sc.highlight(5, 0, false);
    this.highlight(minl);
    this.highlight(minr);

    lang.nextStep();
    sc.unhighlight(5);
    sc.highlight(6, 0, false);
    this.unhighlight(minr);

    lang.nextStep();
    sc.unhighlight(6);
    sc.highlight(1, 0, false);
    this.unhighlight(root);
    this.unhighlight(maxNode);
    this.highlight(blatt);

    lang.nextStep();
    sc.unhighlight(1);
    sc.highlight(3, 0, false);
    this.unhighlight(blatt);
    this.highlight(nodeArt);

    lang.nextStep();
    sc.unhighlight(3);
    sc.highlight(9, 0, false);

    lang.nextStep();
    sc.unhighlight(9);
    sc.highlight(10, 0, false);
    offset = new Offset(10, 0, minl, "E");
    // set initial value of V
    Text minlV = lang.newText(offset, "V: +unendlich", "V: +unendlich",
        new MsTiming(0));
    this.unhighlight(nodeArt);
    this.highlight(minNode);

    lang.nextStep();
    sc.unhighlight(10);
    sc.highlight(11, 0, false);
    this.highlight(leaf1l);
    this.highlight(leaf2l);

    lang.nextStep();
    sc.unhighlight(11);
    sc.highlight(1, 0, false);
    this.unhighlight(minl);
    this.unhighlight(leaf2l);
    this.unhighlight(minNode);
    this.highlight(blatt);

    lang.nextStep();
    sc.unhighlight(1);
    sc.highlight(2, 0, false);
    this.unhighlight(blatt);
    this.highlight(evaluate);

    lang.nextStep();
    sc.unhighlight(2);
    sc.highlight(12, 0, false);
    this.unhighlight(leaf1l);
    this.highlight(minl);
    offset = new Offset(10, 20, minl, "E");
    // update value of V'
    Text Vtemp = lang.newText(offset, "V': " + leafs[0], "V': " + leafs[0],
        new MsTiming(0));
    this.unhighlight(evaluate);
    this.highlight(minNode);

    lang.nextStep();
    sc.unhighlight(12);
    sc.highlight(13, 0, false);

    lang.nextStep();
    sc.unhighlight(13);
    sc.highlight(14, 0, false);
    // update value of V
    minlV.setText("V: " + leafs[0], null, null);

    lang.nextStep();
    sc.unhighlight(14);
    sc.highlight(1, 0, false);
    this.unhighlight(minl);
    this.highlight(leaf2l);
    this.unhighlight(minNode);
    this.highlight(blatt);

    lang.nextStep();
    sc.unhighlight(1);
    sc.highlight(2, 0, false);
    offset = new Offset(10, 20, minl, "E");
    this.unhighlight(blatt);
    this.highlight(evaluate);

    lang.nextStep();
    sc.unhighlight(2);
    sc.highlight(12, 0, false);
    this.unhighlight(leaf2l);
    this.highlight(minl);
    // update value of V'
    Vtemp.setText("V': " + leafs[1], null, null);
    this.unhighlight(evaluate);
    this.highlight(minNode);

    lang.nextStep();
    sc.unhighlight(12);
    sc.highlight(13, 0, false);

    lang.nextStep();
    sc.unhighlight(13);
    // in this case its necessary to change the value of V
    if (leafs[1] < leafs[0]) {
      sc.highlight(14, 0, false);
      // update value of V and V'
      minlV.setText("V: " + minlValue, null, null);
      lang.nextStep();
    }
    this.unhighlight(minNode);
    this.highlight(maxNode);

    sc.unhighlight(14);
    sc.highlight(6, 0, false);
    offset = new Offset(10, 20, root, "E");
    // uzpdate value of V'
    Text RootVtemp = lang.newText(offset, "V': " + minlValue, "V': "
        + minlValue, new MsTiming(0));
    this.highlight(root);
    this.unhighlight(minl);

    lang.nextStep();
    sc.unhighlight(6);
    sc.highlight(7, 0, false);

    lang.nextStep();
    sc.unhighlight(7);
    sc.highlight(8, 0, false);
    // update value of V
    rootV.setText("V: " + minlValue, null, null);

    lang.nextStep("Wechsel in rechten Teilgraph");
    sc.unhighlight(8);
    sc.highlight(6, 0, false);
    this.highlight(minr);

    lang.nextStep();
    sc.unhighlight(6);
    sc.highlight(1, 0, false);
    this.unhighlight(root);
    this.unhighlight(maxNode);
    this.highlight(blatt);

    lang.nextStep();
    sc.unhighlight(1);
    sc.highlight(3, 0, false);
    this.unhighlight(blatt);
    this.highlight(nodeArt);

    lang.nextStep();
    sc.unhighlight(3);
    sc.highlight(9, 0, false);

    lang.nextStep();
    sc.unhighlight(9);
    sc.highlight(10, 0, false);
    offset = new Offset(10, 0, minr, "E");
    // set initial value of V
    Text minrV = lang.newText(offset, "V: +unendlich", "V: +unendlich",
        new MsTiming(0));
    this.unhighlight(nodeArt);
    this.highlight(minNode);

    lang.nextStep();
    sc.unhighlight(10);
    sc.highlight(11, 0, false);
    this.highlight(leaf1r);
    this.highlight(leaf2r);

    lang.nextStep();
    sc.unhighlight(11);
    sc.highlight(1, 0, false);
    this.unhighlight(minr);
    this.unhighlight(leaf2r);
    this.unhighlight(minNode);
    this.highlight(blatt);

    lang.nextStep();
    sc.unhighlight(1);
    sc.highlight(2, 0, false);
    this.unhighlight(blatt);
    this.highlight(evaluate);

    lang.nextStep();
    sc.unhighlight(2);
    sc.highlight(12, 0, false);
    this.unhighlight(leaf1r);
    this.highlight(minr);
    offset = new Offset(10, 20, minr, "E");
    // update value of V'
    Text minrVtemp = lang.newText(offset, "V': " + leafs[2], "V': " + leafs[2],
        new MsTiming(0));
    this.unhighlight(evaluate);
    this.highlight(minNode);

    lang.nextStep();
    sc.unhighlight(12);
    sc.highlight(13, 0, false);

    lang.nextStep();
    sc.unhighlight(13);
    sc.highlight(14, 0, false);
    // update value of V
    minrV.setText("V: " + leafs[2], null, null);

    lang.nextStep();
    sc.unhighlight(14);
    sc.highlight(1, 0, false);
    this.unhighlight(minr);
    this.highlight(leaf2r);
    this.unhighlight(minNode);
    this.highlight(blatt);

    lang.nextStep();
    sc.unhighlight(1);
    sc.highlight(2, 0, false);
    offset = new Offset(10, 20, minr, "E");
    this.unhighlight(blatt);
    this.highlight(evaluate);

    lang.nextStep();
    sc.unhighlight(2);
    sc.highlight(12, 0, false);
    this.unhighlight(leaf2r);
    this.highlight(minr);
    // update value of V'
    minrVtemp.setText("V': " + leafs[3], null, null);
    this.unhighlight(evaluate);
    this.highlight(minNode);

    lang.nextStep();
    sc.unhighlight(12);
    sc.highlight(13, 0, false);

    lang.nextStep();
    sc.unhighlight(13);
    // in this case its necessary to change the value of V
    if (leafs[3] < leafs[2]) {
      sc.highlight(14, 0, false);
      // update value of V
      minrV.setText("V: " + minrValue, null, null);
      lang.nextStep();
    }

    sc.unhighlight(14);
    sc.highlight(6, 0, false);
    offset = new Offset(10, 20, root, "E");
    // update value of V'
    RootVtemp.setText("V': " + minrValue, null, null);
    this.highlight(root);
    this.unhighlight(minr);
    this.unhighlight(minNode);
    this.highlight(maxNode);

    lang.nextStep();
    sc.unhighlight(6);
    sc.highlight(7, 0, false);
    // only in this case its necessary to change the value of V
    if (minlValue < minrValue) {
      lang.nextStep();
      sc.unhighlight(7);
      sc.highlight(8, 0, false);
      // update value of V
      rootV.setText("V: " + rootValue, null, null);
    }

    // end
    lang.nextStep("Ende - Lösung gefunden");
    // display text
    String end = "Das Ergebnis bei optimalen Spiel ist " + rootValue
        + ". Der Spieler am Zug wird den markierten Pfad wählen.";
    offset = new Offset(20, 20, leaf1l, "S");
    lang.newText(offset, end, "end", new MsTiming(0));
    // highlights the chosen path
    if (path.equals("l")) {
      if (pathL.equals("l1")) {
        this.highlight(l1);
        this.highlight(l);
      } else {
        this.highlight(l2);
        this.highlight(l);
      }
    } else {
      if (pathR.equals("r1")) {
        this.highlight(r1);
        this.highlight(r);
      } else {
        this.highlight(r2);
        this.highlight(r);
      }
    }
  }

  /*
   * highlight function for primitives
   * 
   * @param prim
   */
  private void highlight(Primitive prim) {
    prim.changeColor("color", Color.BLUE, null, null);
  }

  /*
   * unhighlight function for primitives
   * 
   * @param prim
   */
  private void unhighlight(Primitive prim) {
    prim.changeColor("color", Color.BLACK, null, null);
  }

  /*
   * method to paint the SourceCode
   * 
   * @param root
   * 
   * @return sourceCode
   */
  private SourceCode paintSourceCode(Primitive root) {
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    SourceCode sc = lang.newSourceCode(new Offset(200, -50, root, "NE"),
        "sourceCode", null, scProps);

    for (String line : this.getAlgorithmCode().split("\n")) {
      sc.addCodeLine(line, null, 0, null);
    }

    return sc;
  }

  /*
   * to get the name
   * 
   * @return String name of the Visualization
   */
  public String getName() {
    return "MinMax [DE]";
  }

  /*
   * to get the name
   * 
   * @return String name of the Visualization
   */
  public String getAlgorithmName() {
    return "MinMax";
  }

  private static final String SOURCE_CODE = "function minimax(n:node)"
                                              + "\n    if leaf(n)"
                                              + "\n        return evaluate(n)"
                                              + "\n    if n is max node"
                                              + "\n        v := -unendlich"
                                              + "\n        for each child of n"
                                              + "\n            v' = minimax(child)"
                                              + "\n            if v' > v"
                                              + "\n                v = v'"
                                              + "\n    if n is min node"
                                              + "\n        v := +unendlich"
                                              + "\n        for each child of n"
                                              + "\n            v' = minimax(child)"
                                              + "\n            if v' < v"
                                              + "\n                v = v'";

  /*
   * to get the SourceCode of the Algorithm
   * 
   * @return String SourceCode
   */
  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  /*
   * to get the names of authors of the Visualization
   * 
   * @return String names of authors
   */
  public String getAnimationAuthor() {
    return "Barbara Zöller, Malte Paskuda";
  }

  /*
   * to get the description of the visualization
   * 
   * @return String description
   */
  public String getDescription() {
    return "Der Minimax Algorithmus wird bei Spielen zum Finden einer optimalen Strategie verwendet,"
        + "\n"
        + "die zu einem Ergebnis f&uuml;hrt, das mindestens so gut ist wie jede andere Strategie,"
        + "\n"
        + "wenn man gegen einen unfehlbaren Gegner spielt."
        + "\n"
        + "Dazu wird ein Spielbaum (Suchbaum) definiert:"
        + "\n"
        + "der Ausgangszustand gibt an, welcher Spieler am Zug ist;"
        + "\n"
        + "die m&ouml;glichen Z&uuml;ge werden mit einer Nachfolgefunktion angegeben;"
        + "\n"
        + "die Nutzenfunktion bewertet die Endzust&auml;nde des Spiels aus Sicht des Spielers, der am Zug ist."
        + "\n"
        + "Die beiden Spieler verfolgen unterschiedliche Ziele, denn jeder m&ouml;chte das Spiel gewinnen "
        + "\n"
        + "und spielen daher gegeneienander."
        + "\n"
        + "Daf&uuml;r werden sie in Min- und Maxspieler unterteilt."
        + "\n"
        + "Der Maxspieler m&ouml;chte m&ouml;glichst viele Punkte erreichen, der Minspieler hingegen"
        + "\n"
        + "m&ouml;glichst wenige."
        + "\n"
        + "Um die optimale Strategie zu finden, m&uuml;ssen beide Spieler optimal spielen,"
        + "\n"
        + "also immer den bestm&ouml;glichen Zug entsprechend ihres Zieles (minimieren bzw. maximieren)"
        + "\n"
        + "Der MiniMax-Algor. verwendet eine einfache rekursive Berechnung, die bis in die "
        + "\n"
        + "Bl&auml;tter (Endzust&auml;nde) des Baumes verl&auml;uft und die Minimax-Werte durch den Baum nach"
        + "\n"
        + "oben zum Ausgangszustand weiterleitet."
        + "\n"
        + " Somit kann z.B. f&uuml;r das Spiel Tic-Tac-Toe ein Suchbaum aus jeder beliebigen Spielposition"
        + "\n"
        + "aufgestellt werden."
        + "\n"
        + "Dazu werden zun&auml;chst alle m&ouml;glichen Züge ermittelt um dann von diesen wieder"
        + "\n"
        + "die darauffolgenden Zust&auml;nde aufzustellen. Das wird solange fortgef&uuml;hrt, bis "
        + "\n"
        + "Endzust&auml;nde erreicht sind. Diese m&uuml;ssen noch bewertet werden (mit einer "
        + "\n"
        + " Bewertungsunktion) um damit dann den MiniMax-Wert des Ausgangszustandes zu bewerten.";
  }

  /*
   * to get the code example
   * 
   * @return String code example
   */
  public String getCodeExample() {
    return "function minimax(n:node)\" " + "\n" + "    if leaf(n)\"  " + "\n"
        + "        return evaluate(n)\" " + "\n" + "    if n is max node\" "
        + "\n" + "        v := -unendlich\" " + "\n"
        + "         for each child of n\" " + "\n"
        + "            v' = minimax(child)\" " + "\n"
        + "            if v' > v\" " + "\n" + "                v = v'\" "
        + "\n" + "    if n is min node\" " + "\n"
        + "        v := +unendlich\" " + "\n"
        + "        for each child of n\" " + "\n"
        + "            v' = minimax(child)\" " + "\n"
        + "            if v' < v\" " + "\n" + "                v = v'\" ;";
  }

  /*
   * to get the file extension
   * 
   * @return String file extension
   */
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /*
   * to get the locale
   * 
   * @return Locale
   */
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  /*
   * to get the Type of the Generator
   * 
   * @return GeneratorType
   */
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  /*
   * to get the Soutput language
   * 
   * @return String output language
   */
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}