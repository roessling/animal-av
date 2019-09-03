package generators.tree.helpers;

import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Dr. Guido R&ouml;&szlig;ling <roessling@acm.org>
 * @version 1.0 2007-05-30
 * 
 */
public class BTreeInsert {

  /**
   * The concrete language object used for creating output
   */
  Language                            lang;

  /*
   * ================================= Variable Groessen:
   */

  /**
   * Ordnung des B-Baumes
   */
  static int                          M;

  /**
   * Abstand zwischen zwei Knoten
   */
  int                                 offsetNode = 50;

  /**
   * Breite eines Squares in PIXELN
   */
  static int                          offsetSq   = 25;

  /**
   * Abstand Knoten vertikal
   */
  int                                 offsetY    = 100;

  /**
   * Die Position des ersten Knotens
   */
  Coordinates                         rootPosition;

  /**
   * Mittelpunkt rootNode (NN)
   */
  Coordinates                         mid;

  /*
   * =================================
   */

  private SourceCode                  sc;
  private SourceCode                  header;
  private Rect                        headerRect;

  private static SourceCodeProperties scProps;
  private static ArrayProperties      arrayProps;
  private static SourceCodeProperties headerProps;

  private IntArray                    elements;
  private ArrayMarker                 markerElements;

  private Text                        counter;
  private Text                        cond1;
  private Text                        cond2;
  private Text                        cond3;
  private Text                        cond4;
  private Text                        cond4_1;
  private Text                        cond4_2;

  TreeNode                            highlightedNode;
  static boolean[]                    highlightedCells;

  static SquareProperties             squareNode;
  private static SquareProperties     headerBack;

  /**
     * 
     */
  TreeNode                            rootNode   = null;

  /**
   * Default constructor
   * 
   * @param l
   *          the concrete language object used for creating output
   */
  public BTreeInsert(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divided by a call of myNextStep();
    lang.setStepMode(true);
  }

  /**
   * Sort the int array passed in
   * 
   * @param a
   *          the array to be sorted
   */
  public void insert(int[] a) {
    // Create Array: coordinates, data, name, display options,

    // Array Properties

    SourceCodeProperties textProps = new SourceCodeProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));

    header = lang.newSourceCode(new Coordinates(20, 0), "header", null,
        headerProps);
    header.addCodeLine("B-Baum: Einfügen", null, 0, null);

    RectProperties rectProperties = new RectProperties();
    rectProperties.set("filled", true);
    rectProperties.set("depth", 1);

    headerRect = lang.newRect(new Offset(-10, -5, "header",
        AnimalScript.DIRECTION_NW), new Offset(10, 5, "header",
        AnimalScript.DIRECTION_SE), "headerRect", null, rectProperties);
    headerRect.changeColor("fillColor", (Color) headerBack.get("fillColor"),
        null, null);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    SourceCode explanation = lang.newSourceCode(new Coordinates(20, 100),
        "explanation", null, textProps);
    explanation
        .addCodeLine(
            "Ein B-Baum ist eine Datenstruktur, die häufig in Datenbanken und Dateisystemen eingesetzt wird.",
            null, 0, null);
    explanation.addCodeLine(
        "In einem B-Baum kann ein Knoten mehr als zwei Kindknoten enthalten.",
        null, 0, null);
    explanation.addCodeLine("", null, 0, null);
    explanation.addCodeLine(
        "Für B-Bäume gelten im Allgemeinen folgende Regeln:", null, 0, null);
    explanation.addCodeLine("", null, 0, null);
    explanation
        .addCodeLine(
            "- Die Ordnung des B-Baumes (M) gibt an wieviele Elemente maximal in einem Knoten",
            null, 1, null);
    explanation.addCodeLine("  enthalten sein können, nämlich 2*M-1.", null, 1,
        null);
    explanation
        .addCodeLine(
            "- Alle Elemente des linken Teilbaums eines Elements, sind kleiner als dieses.",
            null, 1, null);
    explanation.addCodeLine(
        "  Ebenso gilt, dass alle Elemente des rechten Teilbaums größer sind.",
        null, 1, null);
    explanation
        .addCodeLine(
            "- Sollte ein Knoten bereits 2*M-1 Elemente enthalten, so muss gesplittet werden.",
            null, 1, null);
    explanation
        .addCodeLine(
            "  Dabei wird das Element am Index M in den Elternknoten verschoben und",
            null, 1, null);
    explanation
        .addCodeLine(
            "  ein weiteres Kind rechts davon erzeugt. Alle Elemente links des Index M",
            null, 1, null);
    explanation
        .addCodeLine(
            "  verbleiben dabei im alten Knoten, alle rechts davon landen im neuen Kindknoten.",
            null, 1, null);
    explanation.addCodeLine("", null, 0, null);
    explanation.addCodeLine("", null, 0, null);
    explanation.addCodeLine("", null, 0, null);
    explanation
        .addCodeLine(
            "Im Folgenden wird anhand einer Menge von Zahlen das Einfügen in den B-Baum erläutert!",
            null, 0, null);

    lang.nextStep("Einführung");

    explanation.hide();

    elements = lang.newIntArray(new Coordinates(20, 150), a, "intArray", null,
        arrayProps);

    counter = lang.newText(new Coordinates(320, 150),
        "Geprüfte Bedingungen: 0", "counter", null);
    cond1 = lang.newText(new Coordinates(330, 222), "", "cond1", null);
    cond2 = lang.newText(new Coordinates(330, 282), "", "cond1", null);
    cond3 = lang.newText(new Coordinates(330, 393), "", "cond1", null);
    cond4 = lang.newText(new Coordinates(330, 443), "", "cond1", null);
    cond4_1 = lang.newText(new Coordinates(330, 507), "", "cond1", null);
    cond4_2 = lang.newText(new Coordinates(330, 571), "", "cond1", null);
    // start a new step after the array was created
    lang.nextStep();

    // Create SourceCode: coordinates, name, display options,

    // Source Code
    /* SourceCode */sc = lang.newSourceCode(new Coordinates(20, 190),
        "sourceCode", null, scProps);

    sc.addCodeLine("public void bTreeInnsert(int value, TreeNode current) {",
        null, 0, null); // 00
    sc.addCodeLine("if(current == null) {", null, 1, null); // 01 if
    sc.addCodeLine("rootNode = new TreeNode();", null, 2, null); // 02 // 03
    sc.addCodeLine("insertIntoLeaf(value, rootNode);", null, 2, null); // 04
    sc.addCodeLine("}", null, 1, null); // 05
    sc.addCodeLine("else if(current.getCount() == 2*M-1) {", null, 1, null); // 06
                                                                             // if
    sc.addCodeLine("TreeNode newRoot = new TreeNode();", null, 2, null); // 07
    sc.addCodeLine("newRoot.setChild(rootNode, 0);", null, 2, null); // 08
    sc.addCodeLine("rootNode = newRoot;", null, 2, null); // 09
    sc.addCodeLine("splitIntoSiblings(rootNode, 0);", null, 2, null); // 10
    sc.addCodeLine("bTreeInsert(value, rootNode);", null, 2, null); // 11
    sc.addCodeLine("}", null, 1, null); // 12
    sc.addCodeLine("else if(current.getChild(0) == null) {", null, 1, null); // 13
                                                                             // if
    sc.addCodeLine("insertIntoLeaf(value, current);", null, 2, null); // 14
    sc.addCodeLine("}", null, 1, null); // 15
    sc.addCodeLine("else {", null, 1, null); // 16 else
    sc.addCodeLine("int index = 0;", null, 2, null); // 17
    sc.addCodeLine(
        "while(value > current.getValue(index) && current.getValue(index) != -1)",
        null, 2, null); // 18
    sc.addCodeLine("index++;", null, 3, null); // 19
    sc.addCodeLine("if(current.getChild(index).getCount() == 2*M-1) {", null,
        2, null); // 20
    sc.addCodeLine("splitIntoSiblings(current, index);", null, 3, null); // 21
    sc.addCodeLine("bTreeInsert(value, current.getChild(index+1));", null, 3,
        null); // 22
    sc.addCodeLine("}", null, 2, null); // 23
    sc.addCodeLine("else", null, 2, null); // 24
    sc.addCodeLine("bTreeInsert(value, current.getChild(index));", null, 3,
        null); // 25
    sc.addCodeLine("}", null, 1, null); // 26
    sc.addCodeLine("}", null, 0, null); // 27

    lang.nextStep("Beginn Einfügen");

    markerElements = lang.newArrayMarker(elements, 0, "i", null);

    try {
      // Start bTreeInsert
      for (int i = 0; i < elements.getLength(); i++) {
        markerElements.move(i, null, null);
        bTreeInsert(elements.getData(i), rootNode);
        elements.highlightCell(i, null, null);
      }
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
    sc.hide();
    highlightedNode.unhighlightCell(0, 2 * M - 2);
    markerElements.changeColor("color", Color.WHITE, null, null);

    SourceCode endText = lang.newSourceCode(new Coordinates(20, 190),
        "endText", null, textProps);
    endText
        .addCodeLine(
            "Damit ist die Visualisierung des Einfügens in einen B-Baum abgeschlossen.",
            null, 0, null);
    endText.addCodeLine("", null, 0, null);
    endText.addCodeLine(
        "Eine zusätzliche Information zur Komplexität des Einfügens:", null, 0,
        null);
    endText.addCodeLine("Im Worst-Case beträgt die Komplexität θ(log n)!",
        null, 1, null);

    lang.nextStep("Abschlussbemerkung");
  }

  /**
   * counter for the number of pointers
   * 
   */
  private int pointerCounter = 0;

  /**
   * 
   * @param value
   * @param current
   * @param codeSupport
   * @throws LineNotExistsException
   */
  private void bTreeInsert(int value, TreeNode current)
      throws LineNotExistsException {
    sc.highlight(0);
    lang.nextStep();
    sc.unhighlight(0);
    if (current == null) {
      cond1.setText("(current == null) == true", null, null);
      cond1.changeColor("Color", Color.GREEN, null, null);
      pointerCounter = pointerCounter + 1;
      sc.highlight(1);
      myNextStep();
      sc.unhighlight(1);

      // Es gibt keinen Root, also muss dieser angelegt werden.

      rootNode = new TreeNode();
      sc.highlight(2);
      myNextStep();
      sc.unhighlight(2);

      rootNode.highlightCell(0, 2 * M - 2);

      insertIntoLeaf(value, rootNode);
      sc.highlight(3);
      myNextStep();
      sc.unhighlight(3);
      cond1.setText("", null, null);
    } else if (current.getCount() == 2 * M - 1) {
      pointerCounter = pointerCounter + 1;
      sc.highlight(1);
      cond1.setText("(current == null) == false", null, null);
      cond1.changeColor("Color", Color.RED, null, null);
      myNextStep();
      sc.unhighlight(1);
      FillInBlanksQuestionModel splitYesNo = new FillInBlanksQuestionModel(
          "split");
      splitYesNo.setPrompt("Muss gesplittet werden? Antworte mit Ja oder Nein");
      splitYesNo.addAnswer("Ja", 1,
          "Richtig! In diesem Fall muss der Knoten gesplittet werden.");
      lang.addFIBQuestion(splitYesNo);

      pointerCounter = pointerCounter + 1;
      sc.highlight(5);
      cond1.setText("", null, null);
      cond2.setText("(" + current.getCount() + " == " + (2 * M - 1)
          + ") == true", null, null);
      cond2.changeColor("Color", Color.GREEN, null, null);
      myNextStep();
      sc.unhighlight(5);

      // Split Root, weil voll
      current.highlightCell(0, 2 * M - 2);
      TreeNode newRoot = new TreeNode();
      newRoot.setChild(rootNode, 0);
      rootNode = newRoot;
      sc.highlight(6);
      sc.highlight(7);
      sc.highlight(8);
      myNextStep();
      sc.unhighlight(6);
      sc.unhighlight(7);
      sc.unhighlight(8);

      rootNode.highlightCell(0, 2 * M - 2);
      sc.highlight(9);
      myNextStep();

      splitIntoSiblings(rootNode, 0);
      myNextStep();
      sc.unhighlight(9);
      sc.highlight(10);
      myNextStep();
      sc.unhighlight(10);

      cond2.setText("", null, null);
      bTreeInsert(value, rootNode);
      myNextStep();

    } else if (current.getChild(0) == null) {

      pointerCounter = pointerCounter + 1;
      sc.highlight(1);
      cond1.setText("(current == null) == false", null, null);
      cond1.changeColor("Color", Color.RED, null, null);
      myNextStep();
      sc.unhighlight(1);

      pointerCounter = pointerCounter + 1;
      sc.highlight(5);
      cond1.setText("", null, null);
      cond2.setText("(" + current.getCount() + " == " + (2 * M - 1)
          + ") == false", null, null);
      cond2.changeColor("Color", Color.RED, null, null);
      myNextStep();
      sc.unhighlight(5);

      pointerCounter = pointerCounter + 1;
      cond2.setText("", null, null);
      cond3.setText("(current.getChild(0) == null) == true", null, null);
      cond3.changeColor("Color", Color.GREEN, null, null);
      sc.highlight(12);
      myNextStep();
      sc.unhighlight(12);

      current.highlightCell(0, 2 * M - 2);

      sc.highlight(13);
      // Man ist bei einem Blatt angekommen und kann einfuegen.

      insertIntoLeaf(value, current);
      myNextStep();
      cond3.setText("", null, null);
      sc.unhighlight(13);
    } else {

      pointerCounter = pointerCounter + 1;
      sc.highlight(1);
      cond1.setText("(current == null) == false", null, null);
      cond1.changeColor("Color", Color.RED, null, null);
      myNextStep();
      sc.unhighlight(1);

      pointerCounter = pointerCounter + 1;
      sc.highlight(5);
      cond1.setText("", null, null);
      cond2.setText("(" + current.getCount() + " == " + (2 * M - 1)
          + ") == false", null, null);
      cond2.changeColor("Color", Color.RED, null, null);
      myNextStep();
      sc.unhighlight(5);

      pointerCounter = pointerCounter + 1;
      cond2.setText("", null, null);
      cond3.setText("(current.getChild(0) == null) == false", null, null);
      cond3.changeColor("Color", Color.RED, null, null);
      sc.highlight(12);
      myNextStep();
      sc.unhighlight(12);
      cond3.setText("", null, null);

      pointerCounter = pointerCounter + 1;
      current.highlightCell(0, 2 * M - 2);
      sc.highlight(15);
      myNextStep();
      sc.unhighlight(15);

      // Den richtigen Kindknoten finden
      int index = 0;
      while (value > current.getValue(index) && current.getValue(index) != -1)
        index++;
      // Wenn der Kindknoten voll ist muss gesplittet werden

      sc.highlight(16);
      sc.highlight(17);
      sc.highlight(18);
      myNextStep();
      sc.unhighlight(16);
      sc.unhighlight(17);
      sc.unhighlight(18);

      if (current.getChild(index).getCount() == 2 * M - 1) {
        pointerCounter = pointerCounter + 1;
        cond4_1.setText("(current.getChild(" + index + ").getCount() == "
            + (2 * M - 1) + ")== true", null, null);
        cond4_1.changeColor("Color", Color.GREEN, null, null);
        sc.highlight(19);
        myNextStep();
        sc.unhighlight(19);

        current.highlightCell(0, 2 * M - 2);
        sc.highlight(20);
        myNextStep();

        splitIntoSiblings(current, index);

        sc.unhighlight(20);

        current.getChild(index + 1).highlightCell(0, 2 * M - 2);

        sc.highlight(21);
        myNextStep();
        sc.unhighlight(21);
        cond4.setText("", null, null);
        cond4_1.setText("", null, null);
        bTreeInsert(value, current.getChild(index + 1));
      } else {
        pointerCounter = pointerCounter + 1;
        cond4_1.setText("(current.getChild(" + index + ").getCount() == "
            + (2 * M - 1) + ")== false", null, null);
        cond4_1.changeColor("Color", Color.RED, null, null);
        sc.highlight(19);
        myNextStep();
        cond4_1.setText("", null, null);
        sc.unhighlight(19);

        sc.highlight(23);
        myNextStep();
        sc.unhighlight(23);

        current.getChild(index).highlightCell(0, 2 * M - 2);

        sc.highlight(24);
        myNextStep();
        sc.unhighlight(24);
        cond4.setText("", null, null);
        cond4_1.setText("", null, null);
        bTreeInsert(value, current.getChild(index));
      }
    }

  }

  public static void generate(Language lang, SourceCodeProperties sourceCode,
      ArrayProperties arrayElements, int m, SquareProperties squareNode1,
      SourceCodeProperties headerProps1, SquareProperties headerBack1, int[] a) {
    BTreeInsert t = new BTreeInsert(lang);

    M = m;
    scProps = sourceCode;
    arrayProps = arrayElements;
    highlightedCells = new boolean[2 * M - 1];
    squareNode = squareNode1;
    headerProps = headerProps1;
    headerBack = headerBack1;

    t.insert(a);
    lang.finalizeGeneration();
  }

  private void myNextStep() {
    lang.hideAllPrimitives();
    if (rootNode != null)
      rootNode.drawNode(rootPosition, 0, 0);
    counter.setText("Geprüfte Bedingungen: " + pointerCounter, null, null);
    // Except-Liste:
    sc.show();
    header.show();
    headerRect.show();
    elements.show();
    markerElements.show();
    counter.show();
    cond1.show();
    cond2.show();
    cond3.show();
    cond4.show();
    cond4_1.show();
    cond4_2.show();
    lang.nextStep();
  }

  /**
   * Das Element wird in dem Blatt an der korrekten Stelle eingefuegt.
   * 
   * @param elem
   *          das einzufuegende Element
   * @param current
   *          der Knoten in den eingefuegt werden soll
   */
  private int insertIntoLeaf(int elem, TreeNode current) {
    current.highlightCell(0, 2 * M - 2);

    int index = 0;

    while (elem > current.getValue(index) && current.getValue(index) != -1)
      index++;
    // Platz fuer das neue Element machen und im Zweifel nach rechts shiften.
    if (index < current.getCount()) {
      int temp = current.getCount();
      while (temp > index) {
        current.setValue(current.getValue(temp - 1), temp);
        temp--;
      }
    }
    current.setValue(elem, index);

    return index;
  }

  /**
   * Das angegebene Kind ist voll und muss gesplittet werden.
   * 
   * @param current
   *          der aktuelle Knoten, dessen Kind voll ist
   * @param childIndex
   *          der Index des betroffenen Kindes
   */
  private void splitIntoSiblings(TreeNode current, int childIndex) {
    //
    for (int i = 2 * M - 2; i > childIndex; i--) {
      current.setValue(current.getValue(i - 1), i);
      current.setChild(current.getChild(i), i + 1);
    }
    current.setChild(null, childIndex + 1);

    current.setValue(current.getChild(childIndex).getValue(M - 1), childIndex);
    current.getChild(childIndex).setValue(-1, M - 1);

    myNextStep();

    TreeNode newNode = new TreeNode();
    current.setChild(newNode, childIndex + 1);

    myNextStep();

    current.getChild(childIndex + 1).setChild(
        current.getChild(childIndex).getChild(M), 0);
    current.getChild(childIndex).setChild(null, M);

    for (int i = 0; i < (M - 1); i++) {
      current.getChild(childIndex + 1).setValue(
          current.getChild(childIndex).getValue(M + i), i);
      current.getChild(childIndex).setValue(-1, M + i);
      current.getChild(childIndex + 1).setChild(
          current.getChild(childIndex).getChild(M + 1 + i), i + 1);
      current.getChild(childIndex).setChild(null, M + 1 + i);
    }
  }

  class TreeNode {
    /**
     * Elemente des Aktuellen Knotens.
     */
    private int[]      elements = new int[2 * M - 1];

    /**
     * Array mit Kindern des aktuellen Knotens.
     */
    private TreeNode[] children = new TreeNode[2 * M];

    public TreeNode() {
      for (int i = 0; i < 2 * M - 1; i++) {
        elements[i] = -1;
        children[i] = null;
      }
      children[2 * M - 1] = null;
    }

    /**
     * Einfuegen eines Values in den aktuellen Knoten.
     * 
     * @param value
     *          der einzufuegende Wert
     * @param pos
     *          Position an der eingefuegt werden soll.
     */
    public void setValue(int value, int pos) {
      elements[pos] = value;
    }

    /**
     * Liefert einen Value des aktuellen Knotens zurueck.
     * 
     * @param pos
     *          Position die zurueck geliefert werden soll.
     * @return Den Wert an der gegebenen Position
     */
    public int getValue(int pos) {
      return elements[pos];
    }

    /**
     * Einfuegen eines Kindes zu dem aktuellen Knoten.
     * 
     * @param child
     *          Der einzufuegende Kindknoten
     * @param pos
     *          Position an der eingefuegt werden soll.
     */
    public void setChild(TreeNode child, int pos) {
      children[pos] = child;
    }

    /**
     * Liefert einen Kindknoten des aktuellen Knotens zurueck.
     * 
     * @param pos
     *          Position die zurueck geliefert werden soll.
     * @return Den Kindknoten an der gegebenen Position
     */
    public TreeNode getChild(int pos) {
      return children[pos];
    }

    /**
     * Liefert die Fuellmenge des aktuellen Knotens zurueck.
     * 
     * @return Die Fuellmenge als Ineger
     */
    public int getCount() {
      int length = 0;
      for (int item : elements)
        if (item != -1)
          length++;
      return length;
    }

    public void highlightCell(int from, int to) {
      highlightedNode = this;
      for (int i = from; i <= to; i++)
        highlightedCells[i] = true;
    }

    public void unhighlightCell(int from, int to) {
      highlightedNode = null;
      for (int i = from; i <= to; i++)
        highlightedCells[i] = false;
    }

    public int getHeigth() {
      if (this.getChild(0) == null)
        return 0;
      else
        return 1 + this.getChild(0).getHeigth();
    }

    /**
     * Knoten Zeichnen
     * 
     * @param depth
     *          Tiefe des aktuellen Knotens
     * @param childIndex
     *          Stelle des aktuellen Knotens horizontal
     */
    public void drawNode(Coordinates left1, int depth, int childIndex) {
      Coordinates left = left1;
      if (depth == 0) {
        int amountChildren = (int) Math.pow(4, rootNode.getHeigth());
        int rootX = (int) ((((2 * M - 1) * offsetSq + offsetNode) * (amountChildren - 1)) / 2 + 10);
        if (rootX < 750)
          rootPosition = new Coordinates(750, 520);
        else
          rootPosition = new Coordinates(rootX, 520);
        mid = new Coordinates((int) (rootPosition.getX() + 0.5 * (2 * M - 1)
            * offsetSq), rootPosition.getY());

        left = rootPosition;
        for (int k = 0; k < 2 * M - 1; k++) {
          Coordinates ul_sq = new Coordinates(left.getX() + k * offsetSq,
              left.getY());
          Square temp = lang.newSquare(ul_sq, offsetSq, "Square", null);
          if (highlightedNode == this && highlightedCells[k]) {
            temp.changeColor("Color", (Color) squareNode.get("fillColor"),
                null, null);
          }
          if (elements[k] == -1)
            lang.newText(ul_sq, "  - -", "Text", null);
          else
            lang.newText(ul_sq, Integer.toString(elements[k]), "Text", null);

        }
      }

      if (this.getChild(0) != null) {
        int maxDepth = this.getHeigth();
        int maxChildren = (int) (Math.pow(2 * M, maxDepth));
        int amountChildren = (int) (maxChildren / (2 * M));
        int maxPivot = (int) (Math.pow(4, rootNode.getHeigth()) / 2);
        int childY = left.getY() + offsetY;
        int childX = 0;

        for (int i = 0; i < 2 * M; i++) {
          if (children[i] != null) {
            int leftChild = childIndex * maxChildren + i * amountChildren;
            int rightChild = maxChildren * childIndex + (amountChildren - 1)
                + i * amountChildren;
            int leftChildX;
            int rightChildX;

            if (leftChild < maxPivot) {
              leftChildX = (int) (mid.getX() + 0.5 * offsetNode)
                  - ((maxPivot - leftChild) * (offsetNode + (2 * M - 1)
                      * offsetSq));
            } else {
              leftChildX = (int) (mid.getX() + 0.5 * offsetNode)
                  + (leftChild - maxPivot)
                  * (offsetNode + (2 * M - 1) * offsetSq);
            }

            if (rightChild < maxPivot) {
              rightChildX = (int) (mid.getX() + 0.5 * offsetNode)
                  - ((maxPivot - rightChild) * (offsetNode + (2 * M - 1)
                      * offsetSq));

            } else {
              rightChildX = (int) (mid.getX() + 0.5 * offsetNode)
                  - ((maxPivot - rightChild) * (offsetNode + (2 * M - 1)
                      * offsetSq));
            }

            childX = (rightChildX + leftChildX) / 2;

            for (int k = 0; k < 2 * M - 1; k++) {
              Coordinates ul_sq = new Coordinates(childX + k * offsetSq, childY);
              Color col = (Color) squareNode.get("color");
              Square temp = lang.newSquare(ul_sq, offsetSq, "Square", null);
              temp.changeColor("color", col, null, null);

              if (highlightedNode == this.getChild(i) && highlightedCells[k]) {
                temp.changeColor("color", (Color) squareNode.get("fillColor"),
                    null, null);
              }
              String caption;
              if (children[i].elements[k] == -1)
                caption = "  - -";
              else
                caption = Integer.toString(children[i].elements[k]);
              lang.newText(ul_sq, caption, "Text", null);
            }

            // Koordinaten fuer Linie berechnen
            Coordinates[] vertices = new Coordinates[2];
            vertices[0] = new Coordinates(left.getX() + i * offsetSq,
                left.getY() + offsetSq);
            vertices[1] = new Coordinates((int) (childX + 0.5 * (2 * M - 1)
                * offsetSq), childY);

            lang.newPolyline(vertices, "Line", null);

            // Rekursiver Aufruf
            children[i].drawNode(new Coordinates(childX, childY), depth + 1,
                (2 * M) * childIndex + i);
          }
        }
      }
    }
  }

}
