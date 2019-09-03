package generators.tree.knuthlayout;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.helpers.BinaryTreeNode;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class KnuthLayout implements Generator {
  private Language             lang;
  private String[][]           tree;

  private BinaryTreeNode       root;
  private int                  width;
  private int                  height;

  private int                  sRadius = 10;
  private int                  lRadius = 25;

  private ArrayList<Primitive> dontHide;
  private TwoValueView         tvv;
  private TwoValueCounter      recursionCounter;
  private SourceCode           scAlgo;
  private CircleProperties     cp;
  private SourceCodeProperties sourceCode;
  private Variables            vars;
  private Polyline             widthMarker;
  private Polyline             heightMarker;

  public void init() {
    lang = new AnimalScript("Knuth's Binary Tree-Layout Algorithmus",
        "Mohit Makhija, Eric Brüggemann", 1000, 800);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    tree = (String[][]) primitives.get("tree");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    /*
     * String tree = "root.left.left.left\n" + "root.left.left.right\n" +
     * "root.left.right\n" + "root.right\n" + "root.right.left";
     */

    begin();

    root = BinaryTreeNode.parse(tree);

    knuth_layout(root);

    drawSmallTree(root);
    lang.nextStep();

    // cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    cp = (CircleProperties) props.getPropertiesByName("graphNodes");

    knuth_layoutAnimal(root);

    lang.hideAllPrimitives();
    for (Primitive p : dontHide) {
      p.show();
    }
    tvv.show();
    end();
    lang.nextStep("Fazit");

    return lang.toString();
  }

  public String getName() {
    return "Knuth's Binary Tree-Layout Algorithm";
  }

  public String getAlgorithmName() {
    return "Knuth Algorithm";
  }

  public String getAnimationAuthor() {
    return "Mohit Makhija, Eric Brüggemann";
  }

  public String getDescription() {
    return "\"How shall we draw a tree?\"\n"
        + "\n"
        + "Diese Frage stellte Donald E. Knuth bereits 1970 in seinem Buch \"The Art of Computer Programming\".\n"
        + "\n"
        + "Er schlug vor, Bin&auml;rb&auml;ume Ebene f&uuml;r Ebene und von Links nach Rechts zu zeichnen.\n"
        + "\n"
        + "Diese Idee ist heute die wohl am h&auml;ufigsten genutzte Technik zum Zeichnen von Bin&auml;rb&auml;umen.\n"
        + "\n"
        + "\n"
        + "Mit dem Algorithmus lassen sich sehr effizient Koordinaten f&uuml;r Knoten in Bin&auml;rb&auml;umen berechnen."
        + "\n"
        + "\n"
        + "Dabei wird auf dem Baum eine Inorder-Traversierung durchgef&uuml;hrt. Beim R&uuml;ckweg werden jeweils"
        + "\n"
        + "die Knoten gezeichnet und die Kind-Knoten mit Kanten mit ihren Eltern-Knoten verbunden.";
  }

  public String getCodeExample() {
    return "int width;" + "\n"
        + "public void knuth_layout(BinaryTreeNode tree) {" + "\n"
        + "    width = 0;" + "\n" + "    knuth_layout(tree, 0);" + "\n" + "}"
        + "\n" + "private void knuth_layout(BinaryTreeNode tree, int depth) {"
        + "\n" + "    if (tree.hasLeftChild()) {" + "\n"
        + "       knuth_layout(tree.getLeft(), depth+1);" + "\n" + "    }"
        + "\n" + "    tree.setX(width);" + "\n" + "    tree.setY(depth);"
        + "\n" + "    drawNode(tree.getX(), tree.getY());" + "\n"
        + "    if (tree.hasLeftChild()) {" + "\n"
        + "        drawEdge(tree, tree.getLeft());" + "\n" + "    }	" + "\n"
        + "    width++;" + "\n" + "    if (tree.hasRightChild()) {" + "\n"
        + "        knuth_layout(tree.getRight(), depth+1);" + "\n"
        + "        drawEdge(tree, tree.getRight());" + "\n" + "    }" + "\n"
        + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  private void begin() {
    vars = lang.newVariables();
    // vars.hide();
    dontHide = new ArrayList<Primitive>();

    cp = new CircleProperties();
    cp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

    TextProperties tpHeader = new TextProperties();
    tpHeader.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    tpHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30),
        "Knuth's Binary Tree-Layout Algorithmus", "header", null, tpHeader);
    dontHide.add(header);

    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    Rect hRect = lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5,
        5, "header", "SE"), "hRect", null, rp);
    dontHide.add(hRect);

    TextProperties tpDescrHd = new TextProperties();
    tpDescrHd.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    tpDescrHd.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    Text descrHd = lang.newText(new Coordinates(20, 80),
        "Beschreibung des Algorithmus", "descrHd", null, tpDescrHd);

    SourceCodeProperties scpDescr = new SourceCodeProperties();
    scpDescr.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    scpDescr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    SourceCode scDescr = lang.newSourceCode(new Offset(0, 0, "descrHd", "SW"),
        "descr", null, scpDescr);
    scDescr.addCodeLine("\\\"How shall wie draw a tree?\\\"", null, 0,
        Timing.INSTANTEOUS);
    scDescr
        .addCodeLine(
            "Diese Frage stellte Donald E. Knuth bereits 1970 in seinem Buch \\\"The Art of Computer Programming\\\".",
            null, 0, Timing.INSTANTEOUS);
    scDescr
        .addCodeLine(
            "Er schlug vor, Binärbäume Ebene für Ebene und von Links nach Rechts zu zeichnen.",
            null, 0, Timing.INSTANTEOUS);
    scDescr
        .addCodeLine(
            "Diese Idee ist heute die wohl am häufigsten genutzte Technik zum Zeichnen von Binärbäumen.",
            null, 0, Timing.INSTANTEOUS);
    scDescr.addCodeLine("", null, 0, Timing.INSTANTEOUS);
    scDescr
        .addCodeLine(
            "Mit dem Algorithmus lassen sich sehr effizient Koordinaten für Knoten in Binärbäumen berechnen.",
            null, 0, Timing.INSTANTEOUS);
    scDescr.addCodeLine("", null, 0, Timing.INSTANTEOUS);
    scDescr
        .addCodeLine(
            "Dabei wird auf dem Baum eine Inorder-Traversierung durchgeführt. Beim Rückweg werden jeweils",
            null, 0, Timing.INSTANTEOUS);
    scDescr
        .addCodeLine(
            "die Knoten gezeichnet und die Kind-Knoten mit Kanten mit ihren Eltern-Knoten verbunden.",
            null, 0, Timing.INSTANTEOUS);
    scDescr.addCodeLine("", null, 0, Timing.INSTANTEOUS);
    scDescr
        .addCodeLine(
            "Im Folgenden werden im Quellcode die zwei Pseudo-Java-Methoden drawNode und drawEdge zum Zeichnen",
            null, 0, Timing.INSTANTEOUS);
    scDescr
        .addCodeLine(
            "verwendet, deren Implementierung vom verwendeten Zeichen-Framework abhängig ist.",
            null, 0, Timing.INSTANTEOUS);
    lang.nextStep();

    descrHd.hide();
    scDescr.hide();

    TextProperties tpAlgoHd = new TextProperties();
    tpAlgoHd.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    tpAlgoHd.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    lang.newText(new Offset(0, 0, "descrHd", "NW"), "Algorithmus:", "algoHd",
        null, tpAlgoHd);

    // SourceCodeProperties scpAlgo = new SourceCodeProperties();
    // scpAlgo.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    // scpAlgo.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // scpAlgo.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scpAlgo.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font(Font.MONOSPACED, Font.PLAIN, 14));
    scAlgo = lang.newSourceCode(new Offset(0, 0, "algoHd", "SW"), "algo", null,
        sourceCode);
    scAlgo.addCodeLine("int width;", null, 0, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("public void knuth_layout(BinaryTreeNode tree) {", null,
        0, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("width = 0;", null, 1, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("knuth_layout(tree, 0);", null, 1, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("}", null, 0, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("", null, 0, Timing.INSTANTEOUS);
    scAlgo.addCodeLine(
        "private void knuth_layout(BinaryTreeNode tree, int depth) {", null, 0,
        Timing.INSTANTEOUS);
    scAlgo.addCodeLine("if (tree.hasLeftChild()) {", null, 1,
        Timing.INSTANTEOUS);
    scAlgo.addCodeLine("knuth_layout(tree.getLeft(), depth+1);", null, 2,
        Timing.INSTANTEOUS);
    scAlgo.addCodeLine("}", null, 1, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("tree.setX(width);", null, 1, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("tree.setY(depth);", null, 1, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("drawNode(tree.getX(), tree.getY());", null, 1,
        Timing.INSTANTEOUS);
    scAlgo.addCodeLine("if (tree.hasLeftChild()) {", null, 1,
        Timing.INSTANTEOUS);
    scAlgo.addCodeLine("drawEdge(tree, tree.getLeft());", null, 2,
        Timing.INSTANTEOUS);
    scAlgo.addCodeLine("}", null, 1, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("width++;", null, 1, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("if (tree.hasRightChild()) {", null, 1,
        Timing.INSTANTEOUS);
    scAlgo.addCodeLine("knuth_layout(tree.getRight(), depth+1);", null, 2,
        Timing.INSTANTEOUS);
    scAlgo.addCodeLine("drawEdge(tree, tree.getRight());", null, 2,
        Timing.INSTANTEOUS);
    scAlgo.addCodeLine("}", null, 1, Timing.INSTANTEOUS);
    scAlgo.addCodeLine("}", null, 0, Timing.INSTANTEOUS);

    lang.newText(new Offset(0, 30, "algo", "SW"), "Baum:", "treeHd", null,
        tpAlgoHd);

    recursionCounter = new TwoValueCounter();
    CounterProperties cop = new CounterProperties();
    cop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
    cop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    String[] names = { "Rekursive Aufrufe", "Rekursionstiefe" };
    tvv = lang.newCounterView(recursionCounter, new Offset(50, 0, "header",
        "NE"), cop, true, true, names);

  }

  private void end() {
    SourceCodeProperties scpResult = new SourceCodeProperties();
    scpResult.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    scpResult.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    SourceCode scDescr = lang.newSourceCode(new Offset(0, 0, "descrHd", "NW"),
        "result", null, scpResult);
    scDescr.addCodeLine("Der Algorithmus berechnet Koordinaten für Knoten in",
        null, 0, Timing.INSTANTEOUS);
    scDescr.addCodeLine("Binärbäumen in linearer Zeit.", null, 0,
        Timing.INSTANTEOUS);
    scDescr.addCodeLine("", null, 0, Timing.INSTANTEOUS);
    scDescr.addCodeLine(
        "Mit den berechneten Koordinaten lassen sich ästhetische", null, 0,
        Timing.INSTANTEOUS);
    scDescr.addCodeLine("Binärbäume zeichnen. Einen kleinen Nachteil hat der",
        null, 0, Timing.INSTANTEOUS);
    scDescr.addCodeLine("Algorithmus jedoch: Unter Umständen kann er zu sehr",
        null, 0, Timing.INSTANTEOUS);
    scDescr.addCodeLine("stark in die Breite gezogenen Bäumen führen, die",
        null, 0, Timing.INSTANTEOUS);
    scDescr.addCodeLine("idealerweise wesentlich kompakter gezeichnet werden",
        null, 0, Timing.INSTANTEOUS);
    scDescr.addCodeLine("könnten.", null, 0, Timing.INSTANTEOUS);
    // scDescr.addCodeLine("", null, 0, Timing.INSTANTEOUS);
    // scDescr.addCodeLine("", null, 0, Timing.INSTANTEOUS);
    // scDescr.addCodeLine("", null, 0, Timing.INSTANTEOUS);
  }

  public void knuth_layout(BinaryTreeNode tree) {
    width = 0;
    height = 0;
    knuth_layout(tree, 0);
  }

  private void knuth_layout(BinaryTreeNode tree, int depth) {
    if (depth > height) {
      height = depth;
    }
    if (tree.hasLeftChild()) {
      knuth_layout(tree.getLeft(), depth + 1);
    }
    tree.setX(width);
    tree.setY(depth);
    width++;
    if (tree.hasRightChild()) {
      knuth_layout(tree.getRight(), depth + 1);
    }

  }

  public void knuth_layoutAnimal(BinaryTreeNode tree) {

    scAlgo.highlight(0);
    vars.declare("int", "width");
    // vars.show();
    lang.nextStep();
    vars.declare("string", "tree.hasLeftChild",
        String.valueOf(tree.hasLeftChild()));
    vars.declare("string", "tree.hasRightChild",
        String.valueOf(tree.hasRightChild()));
    vars.declare("int", "tree.x");
    vars.declare("int", "tree.y");
    scAlgo.unhighlight(0);
    scAlgo.highlight(1);
    lang.nextStep("Start");
    scAlgo.unhighlight(1);
    scAlgo.highlight(2);
    vars.set("width", "0");
    width = 0;
    lang.nextStep();
    scAlgo.unhighlight(2);
    scAlgo.highlight(3);
    lang.nextStep("Start (Rekursion)");
    scAlgo.unhighlight(3);

    PolylineProperties polyp = new PolylineProperties();
    polyp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    Offset[] verticesHeight = {
        new Offset(2 * lRadius, 2 * lRadius, "algo", "NE"),
        new Offset(3 * lRadius, 2 * lRadius, "algo", "NE") };
    heightMarker = lang.newPolyline(verticesHeight, "heightMarker", null /*
                                                                          * new
                                                                          * Hidden
                                                                          * ()
                                                                          */,
        polyp);
    Offset[] verticesWidth = {
        new Offset(5 * lRadius, height * 2 * lRadius + 5 * lRadius, "algo",
            "NE"),
        new Offset(5 * lRadius, height * 2 * lRadius + 4 * lRadius, "algo",
            "NE") };
    widthMarker = lang.newPolyline(verticesWidth, "widthMarker", null /*
                                                                       * new
                                                                       * Hidden
                                                                       * ()
                                                                       */,
        polyp);

    recursionCounter.assignmentsInc(1); // val 1
    recursionCounter.accessInc(1); // val 2
    vars.declare("int", "depth", "0");
    knuth_layoutAnimal(tree, 0);
    heightMarker.moveTo("S", "translate", new Offset(2 * lRadius, 2 * lRadius,
        "algo", "NE"), Timing.INSTANTEOUS, new TicksTiming(50));
    vars.set("tree.hasLeftChild", String.valueOf(tree.hasLeftChild()));
    vars.set("tree.hasRightChild", String.valueOf(tree.hasRightChild()));
    vars.set("tree.x", String.valueOf(tree.getX()));
    vars.set("tree.y", String.valueOf(tree.getY()));
    vars.discard("depth");
    recursionCounter.accessInc(-1);
    scAlgo.highlight(3);
    lang.nextStep("Ende (fertiger Baum)");
    scAlgo.unhighlight(3);
    scAlgo.highlight(4);
    lang.nextStep();
  }

  private void knuth_layoutAnimal(BinaryTreeNode tree, int depth) {
    heightMarker.moveTo("S", "translate", new Offset(2 * lRadius, 2 * lRadius
        + 2 * lRadius * depth, "algo", "NE"), Timing.INSTANTEOUS,
        new TicksTiming(50));
    vars.set("depth", String.valueOf(depth));
    vars.set("tree.hasLeftChild", String.valueOf(tree.hasLeftChild()));
    vars.set("tree.hasRightChild", String.valueOf(tree.hasRightChild()));
    vars.set("tree.x", String.valueOf(tree.getX()));
    vars.set("tree.y", String.valueOf(tree.getY()));
    scAlgo.highlight(6);
    lang.addLine("color \"N" + tree.getID() + "\" red");
    lang.nextStep();
    scAlgo.unhighlight(6);
    scAlgo.highlight(7);
    if (tree.hasLeftChild()) {
      lang.addLine("color \"E" + tree.getID() + "_" + tree.getLeft().getID()
          + "\" red");
      lang.nextStep();
      scAlgo.unhighlight(7);
      scAlgo.highlight(8);
      lang.nextStep();
      scAlgo.unhighlight(8);
      lang.addLine("color \"N" + tree.getID() + "\" black");
      lang.addLine("color \"E" + tree.getID() + "_" + tree.getLeft().getID()
          + "\" black");
      recursionCounter.assignmentsInc(1);
      recursionCounter.accessInc(1);
      knuth_layoutAnimal(tree.getLeft(), depth + 1);
      heightMarker.moveTo("S", "translate", new Offset(2 * lRadius, 2 * lRadius
          + 2 * lRadius * depth, "algo", "NE"), Timing.INSTANTEOUS,
          new TicksTiming(50));
      vars.set("depth", String.valueOf(depth));
      vars.set("tree.hasLeftChild", String.valueOf(tree.hasLeftChild()));
      vars.set("tree.hasRightChild", String.valueOf(tree.hasRightChild()));
      vars.set("tree.x", String.valueOf(tree.getX()));
      vars.set("tree.y", String.valueOf(tree.getY()));
      recursionCounter.accessInc(-1);
      lang.addLine("color \"N" + tree.getID() + "\" red");
      lang.addLine("color \"E" + tree.getID() + "_" + tree.getLeft().getID()
          + "\" red");
      scAlgo.highlight(8);
      lang.nextStep();
      scAlgo.unhighlight(8);
      scAlgo.highlight(9);
      lang.nextStep();
      scAlgo.unhighlight(9);
    } else {
      lang.nextStep();
      scAlgo.unhighlight(7);
      scAlgo.highlight(9);
      lang.nextStep();
      scAlgo.unhighlight(9);
    }
    scAlgo.highlight(10);
    lang.nextStep();
    tree.setX(width);
    vars.set("tree.x", String.valueOf(tree.getX()));
    scAlgo.unhighlight(10);
    scAlgo.highlight(11);
    lang.nextStep();
    tree.setY(depth);
    vars.set("tree.y", String.valueOf(tree.getY()));
    scAlgo.unhighlight(11);
    scAlgo.highlight(12);
    // drawNode
    Circle c = lang.newCircle(new Offset(tree.getX() * lRadius * 2 + 5
        * lRadius, tree.getY() * lRadius * 2 + 2 * lRadius, "algo", "NE"),
        lRadius, "lN" + tree.getID(), null, cp);
    dontHide.add(c);
    lang.nextStep("Knoten " + width + " zeichnen");
    scAlgo.unhighlight(12);
    scAlgo.highlight(13);
    if (tree.hasLeftChild()) {
      lang.nextStep();
      scAlgo.unhighlight(13);
      scAlgo.highlight(14);
      Offset[] vertices = {
          new Offset(lRadius, lRadius, "lN" + tree.getID(), "NW"),
          new Offset(lRadius, lRadius, "lN" + tree.getLeft().getID(), "NW") };
      Polyline pl = lang.newPolyline(vertices, "lE" + tree.getID() + "_"
          + tree.getLeft().getID(), null);
      dontHide.add(pl);
      lang.addLine("color \"E" + tree.getID() + "_" + tree.getLeft().getID()
          + "\" black");
      lang.nextStep();
      scAlgo.unhighlight(14);
    } else {
      lang.nextStep();
      scAlgo.unhighlight(13);
    }
    scAlgo.highlight(15);
    lang.nextStep();
    scAlgo.unhighlight(15);
    scAlgo.highlight(16);
    width++;
    widthMarker.moveTo("E", "translate", new Offset(5 * lRadius + 2 * lRadius
        * width, height * 2 * lRadius + 4 * lRadius, "algo", "NE"),
        Timing.INSTANTEOUS, new TicksTiming(50));
    lang.nextStep();

    vars.set("width", String.valueOf(width));
    scAlgo.unhighlight(16);
    scAlgo.highlight(17);
    if (tree.hasRightChild()) {
      lang.addLine("color \"E" + tree.getID() + "_" + tree.getRight().getID()
          + "\" red");
      lang.nextStep();
      scAlgo.unhighlight(17);
      scAlgo.highlight(18);
      lang.nextStep();
      scAlgo.unhighlight(18);
      lang.addLine("color \"N" + tree.getID() + "\" black");
      lang.addLine("color \"E" + tree.getID() + "_" + tree.getRight().getID()
          + "\" black");
      recursionCounter.assignmentsInc(1);
      recursionCounter.accessInc(1);
      knuth_layoutAnimal(tree.getRight(), depth + 1);
      heightMarker.moveTo("S", "translate", new Offset(2 * lRadius, 2 * lRadius
          + 2 * lRadius * depth, "algo", "NE"), Timing.INSTANTEOUS,
          new TicksTiming(50));
      vars.set("depth", String.valueOf(depth));
      vars.set("tree.hasLeftChild", String.valueOf(tree.hasLeftChild()));
      vars.set("tree.hasRightChild", String.valueOf(tree.hasRightChild()));
      vars.set("tree.x", String.valueOf(tree.getX()));
      vars.set("tree.y", String.valueOf(tree.getY()));
      recursionCounter.accessInc(-1);
      lang.addLine("color \"N" + tree.getID() + "\" red");
      lang.addLine("color \"E" + tree.getID() + "_" + tree.getRight().getID()
          + "\" red");
      scAlgo.highlight(18);
      lang.nextStep();
      scAlgo.unhighlight(18);
      scAlgo.highlight(19);
      // drawEdge
      Offset[] vertices = {
          new Offset(lRadius, lRadius, "lN" + tree.getID(), "NW"),
          new Offset(lRadius, lRadius, "lN" + tree.getRight().getID(), "NW") };
      Polyline pl = lang.newPolyline(vertices, "lE" + tree.getID() + "_"
          + tree.getRight().getID(), null);
      dontHide.add(pl);
      lang.addLine("color \"E" + tree.getID() + "_" + tree.getRight().getID()
          + "\" black");
      lang.nextStep();
      scAlgo.unhighlight(19);
    } else {
      lang.nextStep();
      scAlgo.unhighlight(17);
    }
    scAlgo.highlight(20);
    lang.nextStep();
    scAlgo.unhighlight(20);
    scAlgo.highlight(21);
    lang.nextStep();
    scAlgo.unhighlight(21);
    lang.addLine("color \"N" + tree.getID() + "\" black");
  }

  private void drawSmallTree(BinaryTreeNode tree) {
    if (tree.hasLeftChild()) {
      drawSmallTree(tree.getLeft());
    }
    lang.newCircle(new Offset(tree.getX() * sRadius * 2 + sRadius, tree.getY()
        * sRadius * 2 + sRadius, "treeHd", "SW"), sRadius, "N" + tree.getID(),
        null, cp);
    tree.setX(0);
    tree.setY(0);
    if (tree.hasLeftChild()) {
      Offset[] vertices = {
          new Offset(sRadius, sRadius, "N" + tree.getID(), "NW"),
          new Offset(sRadius, sRadius, "N" + tree.getLeft().getID(), "NW") };
      lang.newPolyline(vertices, "E" + tree.getID() + "_"
          + tree.getLeft().getID(), null);
    }
    if (tree.hasRightChild()) {
      drawSmallTree(tree.getRight());
      Offset[] vertices = {
          new Offset(sRadius, sRadius, "N" + tree.getID(), "NW"),
          new Offset(sRadius, sRadius, "N" + tree.getRight().getID(), "NW") };
      lang.newPolyline(vertices, "E" + tree.getID() + "_"
          + tree.getRight().getID(), null);
    }
  }
}