package generators.tree.helpers;

import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
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
public class BTreeDelete {

  /**
   * The concrete language object used for creating output
   */
  Language                            lang;

  /*
   * ================================= Variable Größen:
   */

  /**
   * Ordnung des B-Baumes
   */
  static int                          M                = 2;

  /**
   * Abstand zwischen zwei Knoten
   */
  int                                 offsetNode       = 50;

  /**
   * Breite eines Squares in PIXELN
   */
  int                                 offsetSq         = 25;

  /**
   * Abstand Knoten vertikal
   */
  int                                 offsetY          = 100;

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

  List<Primitive>                     except           = new ArrayList<Primitive>();

  private SourceCode                  sc;
  private SourceCode                  header;
  private Rect                        headerRect;

  private static SourceCodeProperties scProps;
  private static ArrayProperties      arrayProps;
  private static SourceCodeProperties headerProps;

  private IntArray                    deletions;
  private ArrayMarker                 markerDeletions;

  private Text                        cond1;
  private Text                        cond2;
  private Text                        cond2_1;
  private Text                        cond2_2;
  private Text                        cond3;
  private Text                        cond4;
  private Text                        cond4_1;
  private Text                        cond4_2;
  private Text                        cond5_1;
  private Text                        cond5_2;

  boolean                             found;
  TreeNode                            pStrich;

  // private static SquareProperties squareNode;
  private static SquareProperties     headerBack;

  private Text                        counter;

  TreeNode                            highlightedNode  = null;
  static boolean[]                    highlightedCells = new boolean[2 * M - 1];

  /**
     * 
     */
  TreeNode                            rootNode         = null;

  /**
   * Default constructor
   * 
   * @param l
   *          the concrete language object used for creating output
   */
  public BTreeDelete(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divided by a call of myNextStep();
    lang.setStepMode(true);
  }

  private void insert(int[] a) {
    try {
      // Start bTreeInsert
      for (int i = 0; i < a.length; i++) {
        bTreeInsert(a[i], rootNode);
      }
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  /**
     */
  public void delete(int[] b) {
    SourceCodeProperties textProps = new SourceCodeProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));

    header = lang.newSourceCode(new Coordinates(20, 0), "header", null,
        headerProps);
    header.addCodeLine("B-Baum: Löschen", null, 0, null);

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
            "- Die Ordnung des B-Baumes (t) gibt an wieviele Elemente maximal in einem Knoten",
            null, 1, null);
    explanation.addCodeLine("  enthalten sein können, nämlich 2*t-1.", null, 1,
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
            "- Sollte ein Knoten bereits 2*t-1 Elemente enthalten, so muss gesplittet werden.",
            null, 1, null);
    explanation
        .addCodeLine(
            "  Dabei wird das Element am Index t in den Elternknoten verschoben und",
            null, 1, null);
    explanation
        .addCodeLine(
            "  ein weiteres Kind rechts davon erzeugt. Alle Elemente links des Index t",
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
            "Im Folgenden wird anhand einer Menge von Zahlen das Löschen aus dem B-Baum erläutert!",
            null, 0, null);

    lang.nextStep("Einführung");
    explanation.hide();

    deletions = lang.newIntArray(new Coordinates(20, 150), b, "intArray", null,
        arrayProps);

    counter = lang.newText(new Coordinates(320, 150),
        "Geprüfte Bedingungen: 0", "counter", null);

    cond1 = lang.newText(new Coordinates(330, 218), "", "cond1", null);
    cond2 = lang.newText(new Coordinates(330, 249), "", "cond1", null);
    cond2_1 = lang.newText(new Coordinates(330, 267), "", "cond1", null);
    cond2_2 = lang.newText(new Coordinates(330, 286), "", "cond1", null);
    cond3 = lang.newText(new Coordinates(430, 330), "", "cond1", null);
    cond4 = lang.newText(new Coordinates(330, 361), "", "cond1", null);
    cond4_1 = lang.newText(new Coordinates(330, 376), "", "cond1", null);
    cond4_2 = lang.newText(new Coordinates(330, 409), "", "cond1", null);
    cond5_1 = lang.newText(new Coordinates(370, 505), "", "cond1", null);
    cond5_2 = lang.newText(new Coordinates(370, 540), "", "cond1", null);

    // start a new step after the array was created
    rootNode.drawNode(rootPosition, 0, 0);
    // Source Code
    /* SourceCode */sc = lang.newSourceCode(new Coordinates(20, 190),
        "sourceCode", null, scProps);

    sc.addCodeLine("bTreeDelete(aktueller Knoten K)", null, 0, null); // 00
    sc.addCodeLine("1. Wenn ROOT leer:", null, 1, null);
    sc.addCodeLine("1. Gib false zurück", null, 2, null);
    sc.addCodeLine("2. Wenn aktueller Knoten ROOT und Blatt ist:", null, 1,
        null);
    sc.addCodeLine("1. Entferne K wenn in ROOT enthalten.", null, 2, null);
    sc.addCodeLine("2. Wenn ROOT jetzt leer ist:", null, 2, null);
    sc.addCodeLine("1. Leere den Baum.", null, 3, null);
    sc.addCodeLine("3. Terminiere und gib true zurück wenn K enthalten war.",
        null, 2, null);
    sc.addCodeLine(
        "3. Andernfalls, wenn aktueller Knoten ROOT ist und ein Element enthält:",
        null, 1, null);
    sc.addCodeLine("1. Ordne ROOT und seine zwei Kinder neu an.", null, 2, null);
    sc.addCodeLine("4. Wenn aktueller Knoten Blatt ist:", null, 1, null);
    sc.addCodeLine("1. Wenn K in diesem Knoten enthalten ist:", null, 2, null);
    sc.addCodeLine("1. Entferne K.", null, 3, null);
    sc.addCodeLine("2. Andernfalls, wenn K bereits gesehen wurde:", null, 2,
        null);
    sc.addCodeLine("1. überschreibe die gefundene Stelle mit seinem direkten",
        null, 3, null);
    sc.addCodeLine("      Vorgänger (letztes Element des aktuellen Knotens).",
        null, 3, null);
    sc.addCodeLine("3. Andernfalls: Terminiere und gib false zurück.", null, 2,
        null);
    sc.addCodeLine("5. Andernfalls:", null, 1, null);
    sc.addCodeLine(
        "1. Wähle index i so, dass K in Reichweite des i-ten Kindes liegt.",
        null, 2, null);
    sc.addCodeLine("2. Wenn gewähltes Kind lediglich M-1 Elemente enthält:",
        null, 2, null);
    sc.addCodeLine(
        "1. Ordne aktuellen Knoten und seine Kinder entsprechend an.", null, 3,
        null);
    sc.addCodeLine("3. Sollte sich K dadurch im aktuellen Knoten befinden",
        null, 2, null);
    sc.addCodeLine("1. Merke dir seine Position und fahre fort.", null, 3, null);
    sc.addCodeLine(
        "4. Berechne i erneut und rufe bTreeDelete mit i-tem Kind auf.", null,
        2, null); // 27

    lang.nextStep("Beginn Löschen");

    markerDeletions = lang.newArrayMarker(deletions, 0, "i", null);

    try {
      // Start bTreeInsert
      for (int i = 0; i < deletions.getLength(); i++) {
        markerDeletions.move(i, null, null);
        bTreeDelete(deletions.getData(i), rootNode);
        deletions.highlightCell(i, null, null);
      }
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
    sc.hide();
    highlightedNode.unhighlightCell(0, 2 * M - 2);
    markerDeletions.changeColor("color", Color.WHITE, null, null);

    SourceCode endText = lang.newSourceCode(new Coordinates(20, 190),
        "endText", null, textProps);
    endText
        .addCodeLine(
            "Damit ist die Visualisierung des Löschens in einen B-Baum abgeschlossen.",
            null, 0, null);
    endText.addCodeLine("", null, 0, null);
    endText.addCodeLine(
        "Eine zusätzliche Information zur Komplexität des Löschens:", null, 0,
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

  private void myNextStep() {
    lang.hideAllPrimitives();
    if (rootNode != null)
      rootNode.drawNode(rootPosition, 0, 0);
    counter.setText("Geprüfte Bedingungen: " + pointerCounter, null, null);
    // Except-Liste:
    sc.show();
    header.show();
    headerRect.show();
    deletions.show();
    markerDeletions.show();
    counter.show();
    cond1.show();
    cond2.show();
    cond2_1.show();
    cond2_2.show();
    cond3.show();
    cond4.show();
    cond4_1.show();
    cond4_2.show();
    cond5_1.show();
    cond5_2.show();
    lang.nextStep();
  }

  /**
   * 
   * @param value
   * @param current
   * @param codeSupport
   * @throws LineNotExistsException
   */
  private void bTreeInsert(int value, TreeNode current)
      throws LineNotExistsException {
    if (current == null) {
      // Es gibt keinen Root, also muss dieser angelegt werden.
      rootNode = new TreeNode();
      insertIntoLeaf(value, rootNode);
    } else if (current.getCount() == 2 * M - 1) {
      // Split Root, weil voll
      TreeNode newRoot = new TreeNode();
      newRoot.setChild(rootNode, 0);
      rootNode = newRoot;
      splitIntoSiblings(rootNode, 0);
      bTreeInsert(value, rootNode);
    } else if (current.getChild(0) == null) {
      insertIntoLeaf(value, current);
    } else {
      // Den richtigen Kindknoten finden
      int index = 0;
      while (value > current.getValue(index) && current.getValue(index) != -1)
        index++;
      // Wenn der Kindknoten voll ist muss gesplittet werden
      if (current.getChild(index).getCount() == 2 * M - 1) {
        splitIntoSiblings(current, index);
        bTreeInsert(value, current.getChild(index + 1));
      } else {
        bTreeInsert(value, current.getChild(index));
      }
    }

  }

  /**
   * 
   * @param value
   * @param current
   * @return
   * @throws LineNotExistsException
   */
  private boolean bTreeDelete(int value, TreeNode current)
      throws LineNotExistsException {
    sc.highlight(0);
    current.highlightCell(0, 2 * M - 2);
    myNextStep();
    sc.unhighlight(0);
    if (rootNode == null) {
      cond1.setText("(rootNode == null) == true", null, null);
      cond1.changeColor("Color", Color.GREEN, null, null);
      rootNode.highlightCell(0, 2 * M - 2);
      sc.highlight(1);
      pointerCounter++;
      myNextStep();
      sc.unhighlight(1);

      sc.highlight(2);
      lang.nextStep();
      sc.unhighlight(2);
      cond1.setText("", null, null);
      return false;
    } else if (current == rootNode && current.getChild(0) == null) {
      pointerCounter++;
      sc.highlight(1);
      cond1.setText("(rootNode == null) == false", null, null);
      cond1.changeColor("Color", Color.RED, null, null);
      myNextStep();
      sc.unhighlight(1);

      pointerCounter++;
      sc.highlight(3);
      cond1.setText("", null, null);
      cond2.setText(
          "(current == rootNode && current.getChild(0) == null) == true", null,
          null);
      cond2.changeColor("Color", Color.GREEN, null, null);
      myNextStep();
      sc.unhighlight(3);
      cond2.setText("", null, null);

      if (current.contains(value)) {
        cond2.setText("", null, null);
        cond2_1.setText("current.contains(" + value + ") == true)", null, null);
        cond2_1.changeColor("Color", Color.GREEN, null, null);
        current.highlightCell(0, 2 * M - 2);
        sc.highlight(4);
        deleteInNode(value, current);
        myNextStep();
        sc.unhighlight(4);

        cond2_1.setText("", null, null);
        sc.highlight(5);
        pointerCounter++;
        myNextStep();

        if (rootNode.getCount() == 0) {
          sc.unhighlight(5);
          cond2_2.setText("(rootNode.getCount() == 0) == true", null, null);
          cond2_2.changeColor("Color", Color.GREEN, null, null);
          sc.highlight(6);
          rootNode = null;
          myNextStep();
          cond2_2.setText("", null, null);
          sc.unhighlight(6);
        }
        sc.highlight(7);
        lang.nextStep();
        sc.unhighlight(7);
        return true;
      } else {
        sc.highlight(7);
        lang.nextStep();
        sc.unhighlight(7);
        return false;
      }
    } else if (current == rootNode && current.getCount() == 1) {
      pointerCounter++;
      sc.highlight(1);
      cond1.setText("(rootNode == null) == false", null, null);
      cond1.changeColor("Color", Color.RED, null, null);
      myNextStep();
      sc.unhighlight(1);

      pointerCounter++;
      sc.highlight(3);
      cond1.setText("", null, null);
      cond2.setText(
          "(current == rootNode && current.getChild(0) == null) == false",
          null, null);
      cond2.changeColor("Color", Color.RED, null, null);
      myNextStep();
      sc.unhighlight(3);

      current.highlightCell(0, 2 * M - 2);

      pointerCounter++;
      sc.highlight(8);
      cond2.setText("", null, null);
      cond3.setText("(current == rootNode && current.getCount() == 1) == true",
          null, null);
      cond3.changeColor("Color", Color.GREEN, null, null);
      myNextStep();
      sc.unhighlight(8);

      sc.highlight(9);
      if (current.getChild(0).getCount() == M - 1
          && current.getChild(1).getCount() == M - 1) {
        current.setValue(current.getValue(0), M - 1);

        TreeNode p1 = current.getChild(0);
        TreeNode p2 = current.getChild(1);
        current.setChild(p1.getChild(0), 0);
        current.setChild(p2.getChild(0), M);

        for (int i = 1; i < M; i++) {
          current.getChild(0).highlightCell(0, 2 * M - 2);
          current.setValue(p1.getValue(i - 1), i - 1);
          current.setChild(p1.getChild(i), i);
          current.getChild(1).highlightCell(0, 2 * M - 2);
          current.setValue(p2.getValue(i - 1), M + i - 1);
          current.setChild(p2.getChild(i), M + i);
        }
        myNextStep();
        sc.unhighlight(9);

        /*
         * int index = 0; while(current.getValue(index) < value && index <
         * current.getCount()) index++;
         * 
         * if(current.contains(value)) { found = true; pStrich = current; return
         * bTreeDelete(value, current); }
         * 
         * return bTreeDelete(value, current.getChild(index));
         */
        cond3.setText("", null, null);
        return bTreeDelete(value, current);
      } else {
        if (value <= current.getValue(0)) {
          if (current.getChild(0).getCount() == M - 1)
            shiftKeyToSibling(current, 1, false);
          if (current.getValue(0) == value) {
            pStrich = current;
            found = true;
          }
          myNextStep();
          current.getChild(0).highlightCell(0, 2 * M - 2);
          sc.unhighlight(9);
          cond3.setText("", null, null);
          return bTreeDelete(value, current.getChild(0));
        } else {
          if (current.getChild(1).getCount() == M - 1)
            shiftKeyToSibling(current, 1, true);
          if (current.getValue(0) == value) {
            pStrich = current;
            found = true;
          }
          myNextStep();
          current.getChild(1).highlightCell(0, 2 * M - 2);
          sc.unhighlight(9);
          cond3.setText("", null, null);
          return bTreeDelete(value, current.getChild(1));
        }
      }
    } else {
      if (current.getChild(0) == null) {
        pointerCounter++;
        sc.highlight(1);
        cond1.setText("(rootNode == null) == false", null, null);
        cond1.changeColor("Color", Color.RED, null, null);
        myNextStep();
        sc.unhighlight(1);

        pointerCounter++;
        sc.highlight(3);
        cond1.setText("", null, null);
        cond2.setText(
            "(current == rootNode && current.getChild(0) == null) == false",
            null, null);
        cond2.changeColor("Color", Color.RED, null, null);
        myNextStep();
        sc.unhighlight(3);

        pointerCounter++;
        sc.highlight(8);
        cond2.setText("", null, null);
        cond3.setText(
            "(current == rootNode && current.getCount() == 1) == false", null,
            null);
        cond3.changeColor("Color", Color.RED, null, null);
        myNextStep();
        sc.unhighlight(8);

        pointerCounter++;
        sc.highlight(10);
        cond3.setText("", null, null);
        cond4.setText("(current.getChild(0) == null) == true", null, null);
        cond4.changeColor("Color", Color.GREEN, null, null);
        myNextStep();
        sc.unhighlight(10);
        sc.highlight(11);

        if (current.contains(value)) {
          FillInBlanksQuestionModel deleteYesNo = new FillInBlanksQuestionModel(
              "split");
          deleteYesNo
              .setPrompt("Kann der Wert ohne Verschieben gelöscht werden? Antworte mit Ja oder Nein");
          deleteYesNo
              .addAnswer("Ja", 1,
                  "Richtig! In diesem Fall kann der Wert problemlos gelöscht werden.");
          lang.addFIBQuestion(deleteYesNo);
          pointerCounter++;
          cond4_1
              .setText("current.contains(" + value + ") == true", null, null);
          cond4_1.changeColor("Color", Color.GREEN, null, null);
          myNextStep();
          sc.unhighlight(11);
          sc.highlight(12);
          current.highlightCell(0, 2 * M - 2);
          int index = 0;
          while (current.getValue(index) != value)
            index++;
          for (int i = index + 1; i < current.getCount(); i++) {
            current.setValue(current.getValue(i), i - 1);
          }
          current.setValue(-1, current.getCount() - 1);
          myNextStep();
          sc.unhighlight(12);
          cond4.setText("", null, null);
          cond4_1.setText("", null, null);
          return true;
        } else if (found) {
          cond4_1.setText("current.contains(" + value + ") == false", null,
              null);
          cond4_1.changeColor("Color", Color.RED, null, null);
          pointerCounter++;
          myNextStep();
          sc.unhighlight(11);
          sc.highlight(13);
          cond4_1.setText("", null, null);
          cond4_2.setText("found == true", null, null);
          cond4_2.changeColor("Color", Color.GREEN, null, null);
          pointerCounter++;
          myNextStep();
          sc.unhighlight(13);
          sc.highlight(14);
          sc.highlight(15);
          int index = 0;
          while (pStrich.getValue(index) != value)
            index++;
          pStrich.setValue(current.getValue(current.getCount() - 1), index);
          current.setValue(-1, current.getCount() - 1);
          found = false;
          myNextStep();
          sc.unhighlight(14);
          sc.unhighlight(15);
          cond4.setText("", null, null);
          cond4_2.setText("", null, null);
          return true;
        } else {
          cond4_1.setText("current.contains(" + value + ") == false", null,
              null);
          cond4_1.changeColor("Color", Color.RED, null, null);
          pointerCounter++;
          myNextStep();
          ;
          sc.unhighlight(11);
          sc.highlight(13);
          cond4_1.setText("", null, null);
          cond4_2.setText("found == false", null, null);
          cond4_2.changeColor("Color", Color.RED, null, null);
          pointerCounter++;
          myNextStep();
          cond4_2.setText("", null, null);
          sc.unhighlight(13);
          sc.highlight(16);
          myNextStep();
          cond4.setText("", null, null);
          return false;
        }
      } else {
        pointerCounter++;
        sc.highlight(1);
        cond1.setText("(rootNode == null) == false", null, null);
        cond1.changeColor("Color", Color.RED, null, null);
        myNextStep();
        sc.unhighlight(1);

        pointerCounter++;
        sc.highlight(3);
        cond1.setText("", null, null);
        cond2.setText(
            "(current == rootNode && current.getChild(0) == null) == false",
            null, null);
        cond2.changeColor("Color", Color.RED, null, null);
        myNextStep();
        sc.unhighlight(3);

        pointerCounter++;
        sc.highlight(8);
        cond2.setText("", null, null);
        cond3.setText(
            "(current == rootNode && current.getCount() == 1) == false", null,
            null);
        cond3.changeColor("Color", Color.RED, null, null);
        myNextStep();
        sc.unhighlight(8);

        pointerCounter++;
        sc.highlight(10);
        cond3.setText("", null, null);
        cond4.setText("(current.getChild(0) == null) == false", null, null);
        cond4.changeColor("Color", Color.RED, null, null);
        myNextStep();
        sc.unhighlight(10);

        sc.highlight(17);
        cond4.setText("", null, null);

        myNextStep();
        sc.unhighlight(17);

        sc.highlight(18);
        cond4.setText("", null, null);
        lang.nextStep();
        sc.unhighlight(18);
        int index = 0;
        while (current.getValue(index) < value && index < current.getCount())
          index++;

        if (current.getChild(index).getCount() == M - 1) {
          pointerCounter++;
          sc.highlight(19);
          cond5_1
              .setText("(current.getChild(index).getCount() == M-1) == true",
                  null, null);
          cond5_1.changeColor("Color", Color.GREEN, null, null);
          myNextStep();
          sc.unhighlight(19);
          sc.highlight(20);

          if (current.getChild(index + 1) == null)
            if (current.getChild(index - 1).getCount() == M - 1)
              mergeTwoSiblings(current, index);
            else
              shiftKeyToSibling(current, index, true);
          else {
            if (current.getChild(index + 1).getCount() == M - 1)
              mergeTwoSiblings(current, index + 1);
            else
              shiftKeyToSibling(current, index + 1, false);
          }
          myNextStep();
          sc.unhighlight(20);
        } else {
          sc.highlight(19);
          cond5_1.setText(
              "(current.getChild(index).getCount() == M-1) == false", null,
              null);
          cond5_1.changeColor("Color", Color.RED, null, null);
          myNextStep();
          sc.unhighlight(19);
        }

        cond5_1.setText("", null, null);

        if (current.contains(value)) {
          pointerCounter++;
          sc.highlight(21);
          cond5_2
              .setText("current.contains(" + value + ") == true", null, null);
          cond5_2.changeColor("Color", Color.GREEN, null, null);
          myNextStep();
          sc.unhighlight(21);
          sc.highlight(22);
          lang.nextStep();
          sc.unhighlight(22);
          found = true;
          pStrich = current;
        } else {
          pointerCounter++;
          sc.highlight(21);
          cond5_2.setText("current.contains(" + value + ") == false", null,
              null);
          cond5_2.changeColor("Color", Color.RED, null, null);
          myNextStep();
          sc.unhighlight(21);
        }
        cond5_2.setText("", null, null);
        sc.highlight(23);
        myNextStep();
        sc.unhighlight(23);
        index = 0;
        while (current.getValue(index) < value && index < current.getCount())
          index++;
        return bTreeDelete(value, current.getChild(index));
      }
    }
  }

  /**
   * 
   * @param value
   * @param current
   */
  private void deleteInNode(int value, TreeNode current) {
    int index = 0;
    while (value != current.getValue(index)) {
      index++;
    }
    int n = current.getCount();

    for (int i = index; i < n - 1; i++) {
      current.setValue(current.getValue(i + 1), i);
    }
    current.setValue(-1, n - 1);
  }

  /**
   * 
   * @param node
   * @param k
   * @param shiftRight
   */
  private void shiftKeyToSibling(TreeNode node, int k, boolean shiftRight) {
    if (shiftRight) {
      int end = node.getChild(k).getCount();
      for (int i = 0; i < end; i++) {
        node.getChild(k).setValue(node.getChild(k).getValue(i), i + 1);
        node.getChild(k).setChild(node.getChild(k).getChild(i), i + 1);
      }
      node.getChild(k).setValue(node.getValue(k - 1), 0);
      node.setValue(
          node.getChild(k - 1).getValue(node.getChild(k - 1).getCount() - 1),
          k - 1);

      node.getChild(k)
          .setChild(
              node.getChild(k - 1)
                  .getChild(node.getChild(k - 1).getCount() - 1), 0);
      // verschobene nullen
      node.getChild(k - 1).setChild(null, node.getChild(k - 1).getCount() - 1);
      node.getChild(k - 1).setValue(-1, node.getChild(k - 1).getCount() - 1);
    } else {
      node.getChild(k - 1).setValue(node.getValue(k - 1),
          node.getChild(k - 1).getCount());
      node.setValue(node.getChild(k).getValue(0), k - 1);

      node.getChild(k - 1).setChild(node.getChild(k).getChild(0),
          node.getChild(k - 1).getCount());

      int end = node.getChild(k).getCount();

      for (int i = 1; i < end; i++) {
        node.getChild(k).setValue(node.getChild(k).getValue(i), i - 1);
      }
      for (int i = 1; i <= end; i++) {
        node.getChild(k).setChild(node.getChild(k).getChild(i), i - 1);
      }
      node.getChild(k).setValue(-1, end - 1);
      node.getChild(k).setChild(null, end);
    }
  }

  /**
   * 
   * @param node
   * @param k
   */
  private void mergeTwoSiblings(TreeNode node, int k) {
    node.getChild(k - 1).setValue(node.getValue(k - 1), M - 1);
    node.getChild(k - 1).setChild(node.getChild(k).getChild(0), M - 1);

    for (int i = 1; i < M; i++) {
      node.getChild(k - 1)
          .setValue(node.getChild(k).getValue(i - 1), M + i - 1);
      node.getChild(k - 1).setChild(node.getChild(k).getChild(i), M + i);
    }

    int end = node.getCount();

    for (int i = k + 1; i <= end; i++) {
      node.setValue(node.getValue(i - 1), i - 2);
      node.setChild(node.getChild(i), i - 1);
    }
    node.setChild(null, node.getCount());
    node.setValue(-1, node.getCount() - 1);

  }

  public static void generate(Language lang, SourceCodeProperties sourceCode,
      ArrayProperties arrayElements, int m, SquareProperties node,
      int[] elements, int[] deletions, SourceCodeProperties headerProps1,
      SquareProperties headerBack1) {
    BTreeDelete t = new BTreeDelete(lang);

    M = m;
    scProps = sourceCode;
    arrayProps = arrayElements;
    highlightedCells = new boolean[2 * M - 1];
    // squareNode = node;
    headerProps = headerProps1;
    headerBack = headerBack1;

    t.insert(elements);
    t.delete(deletions);
    lang.finalizeGeneration();
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

    TreeNode newNode = new TreeNode();
    current.setChild(newNode, childIndex + 1);

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

  /**
   * 
   * @author Philipp
   * 
   */
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

    /**
     * 
     * @return the height of the tree
     */
    public int getHeigth() {
      if (this.getChild(0) == null) {
        return 0;
      } else
        return 1 + this.getChild(0).getHeigth();
    }

    /**
     * 
     * @param value
     * @return true if the value is contained in the B-Tree
     */
    public boolean contains(int value) {
      boolean contain = false;
      for (int item : elements) {
        if (item == value)
          contain = true;
      }
      return contain;
    }

    public boolean isEmpty() {
      boolean empty = true;
      for (int item : elements) {
        if (item != -1)
          empty = false;
      }
      return empty;
    }

    /**
     * 
     * @param from
     * @param to
     */
    public void highlightCell(int from, int to) {
      highlightedNode = this;
      for (int i = from; i <= to; i++)
        highlightedCells[i] = true;
    }

    /**
     * 
     * @param from
     * @param to
     */
    public void unhighlightCell(int from, int to) {
      highlightedNode = null;
      for (int i = from; i <= to; i++)
        highlightedCells[i] = false;
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
            temp.changeColor("color", Color.yellow, null, null);
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
              Square temp = lang.newSquare(ul_sq, offsetSq, "Square", null);
              if (highlightedNode == this.getChild(i) && highlightedCells[k]) {
                temp.changeColor("color", Color.yellow, null, null);
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
