package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.tsigaridas.HeapNode;
import generators.helpers.tsigaridas.Schlange;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
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
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.TicksTiming;

/**
 * 
 * @author Ioannis Tsigaridas
 * 
 */
public class HeapTree implements Generator {
  private HeapNode       rootOfTheTree;
  private TextProperties textProps;
  private HeapNode       nodeTree[];

  private int[]          keys;
  ArrayProperties        arrayProps;
  ArrayMarkerProperties  arrayMarkerProps;
  SourceCodeProperties   titleProps;
  CircleProperties       circ;

  public enum TreeDirection {
    LEFT, RIGHT
  }

  Schlange<HeapNode> q = new Schlange<HeapNode>();

  private Text       info;
  private Language   lang;

  public HeapTree() {
    // lang = l;
    //
    // lang.setStepMode(true);
    //
    // RectProperties rectProps = new RectProperties();
    // rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow);
    // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    //
    // Node upperLeft = new Coordinates(20, 30);
    // Node lowerRight = new Coordinates(120, 60);
    // Rect rect = lang.newRect(upperLeft, lowerRight, "BinaryTree", null,
    // rectProps);
    //
    //
    // SourceCodeProperties titleProps = new SourceCodeProperties();
    // titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("Monospaced",Font.BOLD, 16));
    // titleProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.green);
    //
    // SourceCode title = lang.newSourceCode(new Coordinates(30, 20),
    // "sourceCode", null, titleProps);
    //
    // title.addCodeLine("Heapsort", null, 0, null);
    // title.addCodeLine("", null, 0, null);
    // title.addCodeLine("Heapsort ist ein effizienter Sortieralgorithmus, da er eine Zeitkomplexität von O(n log n) besitzt.",
    // null, 0, null);
    // title.addCodeLine("Diese Komplexität ist sowohl im Best Case als auch im Worst Case vertreten.",
    // null, 0, null);
    // title.addCodeLine("Bei dem Algorithmus wird eine bestimmte binäre Baumstruktur genutzt, der sogenannte Heap.",
    // null, 0, null);
    // title.addCodeLine("Dabei ist der Ablauf in zwei Phasen unterteilt. Diese sind:",
    // null, 0, null);
    // title.addCodeLine("", null, 0, null);
    // title.addCodeLine("Phase 1: den Heap (Max-Heap) herstellen", null, 0,
    // null);
    // title.addCodeLine("Phase 2: den Heap (Max-Heap) bearbeiten", null, 0,
    // null);
    // title.addCodeLine("", null, 0, null);
    // title.addCodeLine("In Phase 1 wird aus den unsortierten Schlüsseln ein Max-Heap erstellt, so dass ",
    // null, 0, null);
    // title.addCodeLine("jeder Vaterknoten Kinder mit kleineren Schlüssel als der Vater selbst besitzt.",
    // null, 0, null);
    // title.addCodeLine("Dadurch enthält nach der Herstellung des Heaps die Wurzel des Baums den größten Schlüssel.",
    // null, 0, null);
    // title.addCodeLine("", null, 0, null);
    // title.addCodeLine("Nach Phase 1 läuft dann Phase 2 ab. In Phase 2 wird der größte Schlüssel - die Wurzel - ",
    // null, 0, null);
    // title.addCodeLine("mit dem letzten Schlüssel des Baums getauscht, so dass die Wurzel die endgültige Position erreicht.",
    // null, 0, null);
    // title.addCodeLine("Der letzte Schlüssel, der jetzt die Wurzelposition einnimmt, verletzt die Heap-Eigenschaften.",
    // null, 0, null);
    // title.addCodeLine("Die Heap-Eigenschaften müssen wieder hergestellt werden. Hierbei sickert dann der Schlüssel in der Wurzel",
    // null, 0, null);
    // title.addCodeLine("an der richtigen Stelle des Baums ein. Dieser Vorgang der 2. Phase wird dann n-1 mal durchgeführt, ",
    // null, 0, null);
    // title.addCodeLine("also bis alle Schlüssel endgültig sortiert sind.",
    // null, 0, null);
    //
    // lang.nextStep();
    // title.hide();
    // rect.hide();
    //
    // textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 14));
    //
    // info = lang.newText(new Coordinates(50, 20),
    // "Aktuelle Aktivitäten", "Info Text", null,textProps);
    // Font f = new Font("Monospaced", Font.BOLD, 16);
    // info.setFont(f, null, null);
  }

  /**
   * @return the root of the AVLTree
   */
  public HeapNode getRoot() {
    return rootOfTheTree;
  }

  /**
   * HeapSort sorts the tree.
   */
  public void heapSort(int[] myTree, ArrayProperties arrayProps) {
    int[] tree = myTree;
    createTree(tree);

    // arrayProps = new ArrayProperties();
    // arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    // arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow);
    // arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
    // Color.green);
    // arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
    // Color.red);

    IntArray ia = lang.newIntArray(new Coordinates(50, 140), tree, "array",
        null, arrayProps);

    // arrayMarkerProps = new ArrayMarkerProperties();
    // arrayMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    // arrayMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);

    ArrayMarker iMarker = lang.newArrayMarker(ia, 0, "i", null,
        arrayMarkerProps);
    // arrayMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "child");
    ArrayMarker childMarker = lang.newArrayMarker(ia, 0, "child", null,
        arrayMarkerProps);

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    Text phase1 = lang.newText(new Coordinates(650, 20), " PHASE 1 ",
        "Info Text", null, textProps);
    Text phase2 = lang.newText(new Coordinates(650, 20),
        " PHASE 2: SORTIERUNG ", "Info Text", null, textProps);

    phase2.hide();

    iMarker.hide();
    childMarker.hide();

    // Phase 1: Heap(Max-Heap) herstellen
    info = lang
        .newText(
            new Coordinates(50, 40),
            "Phase 1: Aus den (unsortierten) Schlüsseln im Array wird ein Max-Heap erstellt. !!!",
            "Info Text", null, textProps);
    info.changeColor("color", Color.red, null, null);
    lang.nextStep("Beginn von PHASE 1");
    info.hide();
    for (int i = (tree.length / 2) - 1; i >= 0; i--) {
      percolateDown(tree, tree.length, i, ia, iMarker, childMarker);
    }
    phase1.hide();
    info = lang.newText(new Coordinates(50, 40),
        "Phase 1 ist abgeschlossen. Jetzt kann Phase 2 beginnen.", "Info Text",
        null, textProps);
    info.changeColor("color", Color.red, null, null);
    lang.nextStep();
    info.hide();
    phase2.show();
    info = lang
        .newText(
            new Coordinates(50, 40),
            "Phase 2: Nachdem der Max-Heap erstellt wurde, beginnt jetzt das Sortieren!!!",
            "Info Text", null, textProps);
    info.changeColor("color", Color.red, null, null);
    lang.nextStep("Phase2: Beginn der Sortierung");
    info.hide();
    // Phase 2: den Heap bearbeiten
    for (int i = tree.length - 1; i > 0; --i) {
      tree = swapFatherSon(tree, i, 0, ia, iMarker, childMarker, 1);
      HeapNode sorted = nodeTree[i];
      sorted.getNodeText().changeColor("color", Color.RED, null, null);
      info = lang.newText(new Coordinates(50, 40), "Knoten: " + sorted.getKey()
          + " ist an seine richtige Position sortiert!", "Info Text", null,
          textProps);
      info.changeColor("color", Color.green, null, null);
      ia.highlightElem(i, null, null); // was: highlightCell
      lang.nextStep();
      info.hide();
      if (i != 1) {
        // int childNew = (2*0)+1;
        // int rightChild = (2*0)+2;
        //
        //
        // if ( rightChild <= tree.length-1 && tree[rightChild] > tree[childNew]
        // ) {
        // childNew = rightChild;
        // }
        //
        // if (tree[0] < tree[childNew]) {
        nodeTree[0].getNodeCircle().changeColor("fillcolor", Color.red, null,
            new TicksTiming(20));
        info = lang.newText(new Coordinates(50, 40), "Die neue Wurzel: "
            + nodeTree[0].getKey()
            + " muss an die richtige stelle einsickern !", "Info Text", null,
            textProps);
        info.changeColor("color", Color.red, null, null);
        lang.nextStep();
        nodeTree[0].getNodeCircle().changeColor("fillcolor", Color.yellow,
            null, new TicksTiming(20));
        info.hide();
        // }
        // else {
        // nodeTree[0].getNodeCircle().changeColor("fillcolor", Color.green,
        // null, new TicksTiming(20));
        // info = lang.newText(new Coordinates(50, 40),
        // "Die neue Wurzel: "+nodeTree[0].getKey()+" erfüllt den Max-Heap !",
        // "Info Text", null,textProps);
        // info.changeColor("color", Color.green, null, null);
        // lang.nextStep();
        // nodeTree[0].getNodeCircle().changeColor("fillcolor", Color.yellow,
        // null, new TicksTiming(20));
        // info.hide();
        //
        // }
      }
      percolateDown(tree, i, 0, ia, iMarker, childMarker);

    }
    // Text End
    phase2.hide();
    lang.nextStep();
    phase2 = lang.newText(new Coordinates(650, 20), " PHASE 2 ENDE ",
        "Info Text", null, textProps);
    phase2.show();
    lang.nextStep("Ende von PHASE 2");
    phase2.hide();
    info = lang
        .newText(new Coordinates(50, 40),
            "Der Baum wurde vollständig sortiert !!!", "Info Text", null,
            textProps);
    info.changeColor("color", Color.green, null, new TicksTiming(30));
    // Tests
    // for (int k = 0; k <tree.length; k++) {
    // System.out.println("Stelle: "+k+" im Array ist: "+tree[k]);
    // }
    // for (int i=0; i < nodeTree.length; i++) {
    // System.out.println(nodeTree[i]);
    // }
  }

  /**
   * This Method creates the tree in a binary structure and creates the animal
   * script for visualization the tree.
   * 
   * @param tree
   *          with the keys
   */
  public void createTree(int tree[]) {

    if (tree.length >= 0) {

      int x, y, width, i;
      x = 700;
      y = 140;
      width = 680;
      i = 0;
      nodeTree = new HeapNode[tree.length];

      // Create the root
      if (rootOfTheTree == null) {
        rootOfTheTree = new HeapNode(tree[i]);
        nodeTree[i] = rootOfTheTree;
        rootOfTheTree.createCircle(lang, tree[i], x, y, width, circ);
      }

      nextCreate(tree, nodeTree, rootOfTheTree, x, y, width, i);

    } else
      System.err.println("Please insert a tree !!!");
  }

  /**
   * Generates the whole tree
   * 
   * @param tree
   * @param nodeTree
   * @param currentNode
   * @param newX
   * @param newY
   * @param newWidth
   * @param i
   */
  protected void nextCreate(int tree[], HeapNode nodeTree[],
      HeapNode currentNode, int newX, int newY, int newWidth, int i) {
    int halfW = newWidth >> 1;

    boolean createRight = false;
    if (i <= (tree.length / 2) - 1) {

      if (currentNode.getLeft() == null) {
        HeapNode leftChild = new HeapNode(tree[2 * i + 1]);
        nodeTree[2 * i + 1] = leftChild;
        currentNode.setLeft(leftChild);
        leftChild.setFather(currentNode);
        leftChild.setIncomingDirection(TreeDirection.LEFT);
        leftChild.createCircle(lang, tree[2 * i + 1], newX - halfW, newY + 80,
            halfW, circ);
        currentNode.setLeftEdge(lang, leftChild, leftChild.getNodeCircle(),
            currentNode.getXcoordinate(), currentNode.getYcoordinate());
      } else {
        if ((2 * i) + 2 != tree.length) {
          HeapNode rightChild = new HeapNode(tree[2 * i + 2]);
          nodeTree[2 * i + 2] = rightChild;
          currentNode.setRight(rightChild);
          rightChild.setFather(currentNode);
          rightChild.setIncomingDirection(TreeDirection.RIGHT);
          rightChild.createCircle(lang, tree[2 * i + 2], newX + halfW,
              newY + 80, halfW, circ);
          currentNode.setRightEdge(lang, rightChild,
              rightChild.getNodeCircle(), currentNode.getXcoordinate(),
              currentNode.getYcoordinate());
        }
        createRight = true;
      }
      if (createRight == false) {
        nextCreate(tree, nodeTree, currentNode, newX, newY, newWidth, i);
      } else {
        if (currentNode.getLeft() != null) {
          nextCreate(tree, nodeTree, currentNode.getLeft(), newX - halfW,
              newY + 80, halfW, 2 * i + 1);
        }
        if (currentNode.getRight() != null) {
          nextCreate(tree, nodeTree, currentNode.getRight(), newX + halfW,
              newY + 80, halfW, 2 * i + 2);
        }
      }
    }
  }

  /**
   * This method percolates the rootNode of the tree to get his right position.
   * 
   * @param tree
   * @param length
   * @param i
   */
  protected void percolateDown(int[] myTree, int length, int myI, IntArray ia,
      ArrayMarker iMarker, ArrayMarker childMarker) {
    int[] tree = myTree;
    int i = myI;
    boolean heap = false;

    // Text unterbaum = lang.newText(new Coordinates(50, 60),
    // "Nach dem Vertauschen sind die Heap-Bedingungen im Unterbaum verletzt. Deshalb sickert der rot markierte Knoten an die richtige Position.",
    // "Info Text", null,textProps);
    // unterbaum.changeColor("color", Color.red, null, null);
    // unterbaum.hide();
    HeapNode tmp;

    while (!heap && (i <= (length / 2) - 1)) {
      Text unterbaum = lang
          .newText(
              new Coordinates(50, 60),
              "Nach dem Vertauschen sind die Heap-Bedingungen im Unterbaum verletzt. Deshalb sickert der rot markierte Knoten an die richtige Position.",
              "Info Text", null, textProps);
      unterbaum.changeColor("color", Color.red, null, null);
      unterbaum.hide();
      int child = (2 * i) + 1;
      // find out the maximum child
      if (child + 1 <= length - 1 && tree[child + 1] > tree[child]) {
        child = child + 1;
      }
      // swap the currentNode(Father) with the max. child
      // and if necessary percolate down to the right position
      if (tree[i] < tree[child]) {
        tree = swapFatherSon(tree, i, child, ia, iMarker, childMarker, 0);
        i = child;
        // Unterbaum nach Vertauschen von Vater und Kind verletzt
        // möglicherweise die Heap-Bedingungen. Deshalb richtig einsickern.
        if ((i <= (length / 2) - 1)) {

          int childNew = (2 * i) + 1;
          int rightChild = (2 * i) + 2;

          if (rightChild <= length - 1 && tree[rightChild] > tree[childNew]) {
            childNew = rightChild;
          }

          if (tree[i] < tree[childNew]) {
            unterbaum.show();
            tmp = nodeTree[i];
            tmp.getNodeCircle().changeColor("fillcolor", Color.red, null, null);
            lang.nextStep("Unterbaum verletzt");
            unterbaum.hide();
            tmp.getNodeCircle().changeColor("fillcolor", Color.yellow, null,
                null);
          }
        }
      }
      // if a heap structure(Max-Heap) is reached, then break off the process.
      else {
        info = lang.newText(new Coordinates(50, 40), "Max-Heap erfüllt !",
            "Info Text", null, textProps);
        info.changeColor("color", Color.green, null, null);

        HeapNode father = nodeTree[i];
        HeapNode leftChild = nodeTree[(2 * i) + 1];
        HeapNode rightChild = null;
        if (child + 1 <= length - 1) {
          rightChild = nodeTree[(2 * i) + 2];
          rightChild.getNodeCircle().changeColor("fillcolor", Color.green,
              null, null);
        }
        father.getNodeCircle()
            .changeColor("fillcolor", Color.green, null, null);
        leftChild.getNodeCircle().changeColor("fillcolor", Color.green, null,
            null);

        lang.nextStep();
        father.getNodeCircle().changeColor("fillcolor", Color.yellow, null,
            null);
        leftChild.getNodeCircle().changeColor("fillcolor", Color.yellow, null,
            null);
        if (rightChild != null) {
          rightChild.getNodeCircle().changeColor("fillcolor", Color.yellow,
              null, null);
        }
        info.hide();
        heap = true;
      }
    }
  }

  /**
   * swapFatherSon swaps the father and the greatest(max) Son
   * 
   * @param tree
   *          is the array which represents the real tree
   * @param i
   *          index of the father
   * @param child
   *          index of the son
   * @return the tree after swap
   */
  private int[] swapFatherSon(int[] tree, int i, int childPos, IntArray array,
      ArrayMarker iMarker, ArrayMarker childMarker, int text) {
    lang.nextStep();

    HeapNode father = nodeTree[i];
    HeapNode child = nodeTree[childPos];

    if (father != null && child != null) {

      father.getNodeCircle().changeColor("fillcolor", Color.red, null, null);
      child.getNodeCircle().changeColor("fillcolor", Color.red, null, null);

      if (text == 0) {
        info = lang.newText(new Coordinates(50, 40),
            "Max-Heap nicht erfüllt, deswegen tausche Vater mit max. Kind!",
            "Info Text", null, textProps);
        // info.changeColor("color", Color.red, null, null);
      } else {
        info = lang.newText(new Coordinates(50, 40),
            "Tausche Wurzel mit letzten unsortierten Knoten !", "Info Text",
            null, textProps);
        info.changeColor("color", Color.red, null, null);
      }

      array.highlightCell(i, null, null);
      array.highlightCell(childPos, null, null);
      iMarker.move(i, null, null);
      childMarker.move(childPos, null, null);
      iMarker.show();
      childMarker.show();

      // Father has to get his new position
      Node[] nodesFromFatherToChild = new Node[2];
      Node[] nodesFromChildToFather = new Node[2];

      int x = father.getXcoordinate();
      int y = father.getYcoordinate();
      int iWidth = father.getWidth();

      int newX = child.getXcoordinate();
      int newY = child.getYcoordinate();
      int childWidth = child.getWidth();

      nodesFromFatherToChild[0] = new Coordinates(x, y);
      nodesFromFatherToChild[1] = new Coordinates(newX, newY);
      lang.nextStep("SWAP --> Vater: " + tree[i] + " mit max. Kind: "
          + tree[childPos]);
      Polyline moveLine = lang.newPolyline(nodesFromFatherToChild, "moveline",
          new Hidden());
      try {
        father.getNodeCircle().moveVia(null, "translate", moveLine, null,
            new TicksTiming(60));
        father.getNodeText().moveVia(null, "translate", moveLine, null,
            new TicksTiming(60));
      } catch (IllegalDirectionException e) {
        e.printStackTrace();
      }
      // child has to get his new position
      nodesFromChildToFather[0] = new Coordinates(newX, newY);
      nodesFromChildToFather[1] = new Coordinates(x, y);

      Polyline moveLineSecond = lang.newPolyline(nodesFromChildToFather,
          "moveline", new Hidden());
      try {
        child.getNodeCircle().moveVia(null, "translate", moveLineSecond, null,
            new TicksTiming(60));
        child.getNodeText().moveVia(null, "translate", moveLineSecond, null,
            new TicksTiming(60));
      } catch (IllegalDirectionException e) {
        e.printStackTrace();
      }

      // Here is the real swap of the array!
      array.swap(i, childPos, null, null);

      iMarker.hide();
      childMarker.hide();
      lang.nextStep();
      father.getNodeCircle().changeColor("fillcolor", Color.yellow, null, null);
      child.getNodeCircle().changeColor("fillcolor", Color.yellow, null, null);

      array.unhighlightCell(i, null, null);
      array.unhighlightCell(childPos, null, null);

      father.setX(newX);
      father.setY(newY);
      father.setWidth(childWidth);

      child.setX(x);
      child.setY(y);
      child.setWidth(iWidth);
    }
    HeapNode tmp = nodeTree[i];
    nodeTree[i] = nodeTree[childPos];
    nodeTree[childPos] = tmp;
    info.hide();
    return tree;
  }

  private final static String DESCRIPTION = "Heapsort"
                                              + ""
                                              + "Heapsort ist ein effizienter Sortieralgorithmus, da er eine Zeitkomplexität von O(n log n) besitzt."
                                              + "Diese Komplexität ist sowohl im Best Case als auch im Worst Case vertreten."
                                              + "Bei dem Algorithmus wird eine bestimmte binäre Baumstruktur genutzt, der sogenannte Heap."
                                              + "Dabei ist der Ablauf in zwei Phasen unterteilt. Diese sind:"
                                              + ""
                                              + "Phase 1: den Heap (Max-Heap) herstellen"
                                              + "Phase 2: den Heap (Max-Heap) bearbeiten"
                                              + ""
                                              + "In Phase 1 wird aus den unsortierten Schlüsseln ein Max-Heap erstellt, so dass "
                                              + "jeder Vaterknoten Kinder mit kleineren Schlüssel als der Vater selbst besitzt."
                                              + "Dadurch enthält nach der Herstellung des Heaps die Wurzel des Baums den größten Schlüssel."
                                              + ""
                                              + "Nach Phase 1 läuft dann Phase 2 ab. In Phase 2 wird der größte Schlüssel - die Wurzel - "
                                              + "mit dem letzten Schlüssel des Baums getauscht, so dass die Wurzel die endgültige Position erreicht."
                                              + "Der letzte Schlüssel, der jetzt die Wurzelposition einnimmt, verletzt die Heap-Eigenschaften."
                                              + "Die Heap-Eigenschaften müssen wieder hergestellt werden. Hierbei sickert dann der Schlüssel in der Wurzel"
                                              + "an der richtigen Stelle des Baums ein. Dieser Vorgang der 2. Phase wird dann n-1 mal durchgeführt, "
                                              + "also bis alle Schlüssel endgültig sortiert sind.";

  public void myInit(SourceCodeProperties titleProps) {
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    Node upperLeft = new Coordinates(20, 30);
    Node lowerRight = new Coordinates(120, 60);
    Rect rect = lang
        .newRect(upperLeft, lowerRight, "HeapTree", null, rectProps);

    // SourceCodeProperties titleProps = new SourceCodeProperties();
    // titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("Monospaced",Font.BOLD, 16));
    // titleProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.green);

    SourceCode title = lang.newSourceCode(new Coordinates(30, 20),
        "sourceCode", null, titleProps);

    title.addCodeLine("Heapsort", null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "Heapsort ist ein effizienter Sortieralgorithmus, da er eine Zeitkomplexität von O(n log n) besitzt.",
            null, 0, null);
    title
        .addCodeLine(
            "Diese Komplexität ist sowohl im Best Case als auch im Worst Case vertreten.",
            null, 0, null);
    title
        .addCodeLine(
            "Bei dem Algorithmus wird eine bestimmte binäre Baumstruktur genutzt, der sogenannte Heap.",
            null, 0, null);
    title.addCodeLine(
        "Dabei ist der Ablauf in zwei Phasen unterteilt. Diese sind:", null, 0,
        null);
    title.addCodeLine("", null, 0, null);
    title.addCodeLine("Phase 1: den Heap (Max-Heap) herstellen", null, 0, null);
    title.addCodeLine("Phase 2: den Heap (Max-Heap) bearbeiten", null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "In Phase 1 wird aus den unsortierten Schlüsseln ein Max-Heap erstellt, so dass ",
            null, 0, null);
    title
        .addCodeLine(
            "jeder Vaterknoten Kinder mit kleineren Schlüssel als der Vater selbst besitzt.",
            null, 0, null);
    title
        .addCodeLine(
            "Dadurch enthält nach der Herstellung des Heaps die Wurzel des Baums den größten Schlüssel.",
            null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "Nach Phase 1 läuft dann Phase 2 ab. In Phase 2 wird der größte Schlüssel - die Wurzel - ",
            null, 0, null);
    title
        .addCodeLine(
            "mit dem letzten Schlüssel des Baums getauscht, so dass die Wurzel die endgültige Position erreicht.",
            null, 0, null);
    title
        .addCodeLine(
            "Der letzte Schlüssel, der jetzt die Wurzelposition einnimmt, verletzt die Heap-Eigenschaften.",
            null, 0, null);
    title
        .addCodeLine(
            "Die Heap-Eigenschaften müssen wieder hergestellt werden. Hierbei sickert dann der Schlüssel in der Wurzel",
            null, 0, null);
    title
        .addCodeLine(
            "an der richtigen Stelle des Baums ein. Dieser Vorgang der 2. Phase wird dann n-1 mal durchgeführt, ",
            null, 0, null);
    title.addCodeLine("also bis alle Schlüssel endgültig sortiert sind.", null,
        0, null);

    lang.nextStep();
    title.hide();
    rect.hide();

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 14));

    info = lang.newText(new Coordinates(50, 20), "Aktuelle Aktivitäten",
        "Info Text", null, textProps);
    Font f = new Font("Monospaced", Font.BOLD, 16);
    info.setFont(f, null, null);
  }

  @Override
  public String generate(AnimationPropertiesContainer properties,
      Hashtable<String, Object> primitives) {

    keys = (int[]) primitives.get("keys");

    // sourceProps =
    // (SourceCodeProperties)properties.getPropertiesByName("sourceCode");
    titleProps = (SourceCodeProperties) properties
        .getPropertiesByName("titleProps");
    circ = (CircleProperties) properties.getPropertiesByName("circ");
    arrayProps = (ArrayProperties) properties.getPropertiesByName("arrayProps");
    arrayMarkerProps = (ArrayMarkerProperties) properties
        .getPropertiesByName("arrayMarkerProps");

    myInit(titleProps);

    this.heapSort(keys, arrayProps);

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Heap Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Ioannis Tsigaridas";
  }

  @Override
  public String getCodeExample() {
    return null;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
  }

  @Override
  public String getName() {
    return "Heap Sort";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("Tournament Sort", "IT", 800, 640);
    lang.setStepMode(true);

    arrayProps = null;
    // circ = null;
    // info = null;
    // keys = null;
    // nodeTree = null;
    // q = null;
    rootOfTheTree = null;
    textProps = null;
    titleProps = null;
  }
}
