package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.tsigaridas.AVLNode;
import generators.helpers.tsigaridas.Schlange;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * 150, 100, 200, 80, 120, 180, 220, 60, 90, 110, 240, 40, -200, -80, -60
 * 
 * @author Ioannis Tsigaridas
 * 
 */
public class EffectiveAVLTree implements Generator {
  private AVLNode   rootOfTheTree;

  Schlange<AVLNode> q = new Schlange<AVLNode>();

  public enum TreeDirection {
    LEFT, RIGHT
  }

  private Language             lang;
  private Text                 info, entry, deletions;
  private TextProperties       textProps;

  private int[]                keys;
  private SourceCodeProperties titleProps;
  private CircleProperties     circ;

  public EffectiveAVLTree() {
  }

  /**
   * @return the root of the AVLTree
   */
  public AVLNode getRoot() {
    return rootOfTheTree;
  }

  /**
   * This method inserts a new key (BinaryNode) into the tree.
   * 
   * @param key
   *          is the key which will inserted into the tree
   */
  public void insert(int key) {
    if (key < 0) {
      System.err.println("Please don' t insert negative keys.");
    } else {
      // Proof if the root is empty
      if (rootOfTheTree == null) {
        this.rootOfTheTree = new AVLNode(key);
        rootOfTheTree.setFather(null);
        rootOfTheTree.setIncomingDirection(null);
        // rootOfTheTree.setHeight(1);
        rootOfTheTree.setNodeBalance(0);
        rootOfTheTree.createCircle(lang, key, 700, 100, 600, circ);
        // rootOfTheTree.createCircle(lang,key, 700, 40, 680);

        info = lang.newText(new Coordinates(50, 40),
            "Schlüssel " + Integer.toString(key) + " eingefügt", "Einfügetext",
            null, textProps);
        info.hide(new TicksTiming(70));
      }

      else {
        // If the root is greater than the current key (node)
        // then the current key or node belongs to left side otherwise to the
        // right side.
        if (rootOfTheTree.getKey() > key) {
          nextInsert(rootOfTheTree, key, null, TreeDirection.LEFT, 700, 100,
              600);
        } else
          nextInsert(rootOfTheTree, key, null, TreeDirection.RIGHT, 700, 100,
              600);
      }
    }
  }

  /**
   * Effective insert with recursion
   * 
   * @param currentNode
   *          is the current Node on which we operate
   * @param key
   *          is to be inserted
   * @param father
   *          is the node before the current Node
   * @param direction
   *          gives the information in which subtree the key has to be inserted
   */
  protected void nextInsert(AVLNode currentNode, int key, AVLNode father,
      TreeDirection direction, int x, int y, int w) {
    int halfW = w >> 1;

    if (currentNode == null) {

      AVLNode child = new AVLNode(key);
      child.setFather(father);

      if (direction == TreeDirection.LEFT) {
        child.setIncomingDirection(direction);
        father.setLeft(child);
        child.createCircle(lang, key, x, y, w, circ);
        father.setLeftEdge(lang, child, child.getNodeCircle(),
            father.getXcoordinate(), father.getYcoordinate());
      } else {
        father.setRight(child);
        child.setIncomingDirection(direction);
        child.createCircle(lang, key, x, y, w, circ);
        father.setRightEdge(lang, child, child.getNodeCircle(),
            father.getXcoordinate(), father.getYcoordinate());
      }
      info = lang.newText(new Coordinates(50, 40),
          "Schlüssel " + Integer.toString(key) + " eingefügt.", "Einfügetext",
          null, textProps);
      info.hide(new TicksTiming(70));
    } else {

      if (currentNode.getKey() > key) {
        nextInsert(currentNode.getLeft(), key, currentNode, TreeDirection.LEFT,
            x - halfW, y + 80, halfW);
      } else {
        nextInsert(currentNode.getRight(), key, currentNode,
            TreeDirection.RIGHT, x + halfW, y + 80, halfW);
      }
    }
    // getTreeBFS(rootOfTheTree);
    if (currentNode != null) {

      currentNode.setNodeBalance(getBalance(currentNode));
      // currentNode.setHeight(getHeight(currentNode));

      if (!isBalanced(currentNode)) {
        AVLNode tmp = updateNode(currentNode, true);

        tmp.setNodeBalance(getBalance(tmp));
        // tmp.setHeight(getHeight(tmp));

        if (father == null) {
          rootOfTheTree = tmp;
        } else {

          if (direction == TreeDirection.LEFT) {
            father.setLeft(null);
            father.setLeft(tmp);
          } else {
            father.setRight(null);
            father.setRight(tmp);
          }
        }
      }
    }
  }

  /**
   * With this method a node will be deleted. The procedure is first to find the
   * node (key) and if the node is in the tree the node will be deleted.
   */
  public void delete(int key) {
    // Current Node for deletion is found!!!
    if (rootOfTheTree != null) {
      if (rootOfTheTree.getKey() == key) {
        nextDelete(key, rootOfTheTree, null, null);
      } else {
        if (rootOfTheTree.getKey() > key) {
          nextDelete(key, rootOfTheTree, null, TreeDirection.LEFT);
        } else
          nextDelete(key, rootOfTheTree, null, TreeDirection.RIGHT);
      }
    }
  }

  /**
   * Effective deletion of the currentNode.
   * 
   * @param key
   *          to delete
   * @param myCurrentNode
   *          if found the key, the key will be the currentNode
   * @param father
   *          is the father of the currentNode
   * @param direction
   *          from which direction the currentNode comes.
   * @return the node which will replace the key.
   */
  protected AVLNode nextDelete(int key, AVLNode myCurrentNode, AVLNode father,
      TreeDirection direction) {
    AVLNode currentNode = myCurrentNode;
    AVLNode replace = null;
    // boolean leftSide = false;

    if (currentNode.getKey() == key) {

      if (currentNode.getLeft() != null) {
        System.out.println("findMax");
        replace = findMaxAndRemove(currentNode.getLeft());
      } else if (currentNode.getRight() != null) {
        System.out.println("findMin");
        replace = findMinAndRemove(currentNode.getRight());
        // if (getBalance(replace.getFather()) == 0)
        // replace = replace.getFather();
        System.out.println("REPLACE: " + replace);
      }
      if (replace != null) {

        // AvlNode tmp = currentNode;
        // System.out.println("currentNode"+tmp+" Father: "+father);
        // verschieben der Knoten
        translate(replace, currentNode, false);

        if (replace == currentNode.getLeft()) {
          System.out.println("Fall1");
          replace.setRight(currentNode.getRight());
          // replace.setNodeBalance(getBalance(replace));

          if (!isBalanced(replace)) {
            System.out.println("currentNode: " + currentNode.getKey()
                + " balance:  " + getBalance(currentNode));
            AVLNode testMe = updateNode(replace, true);
            if (father == null) {
              rootOfTheTree = testMe;
            } else {
              if (direction == TreeDirection.LEFT)
                father.setLeft(testMe);
              else
                father.setRight(testMe);
            }
            currentNode = testMe;
            // currentNode.setNodeBalance(getBalance(currentNode));
            return currentNode;
          }

          // if (!isBalanced(replace)) {
          // currentNode = updateNode(replace);
          // if (TreeDirection.RIGHT == currentNode.getIncomingDirection()) {
          // father.setRight(currentNode);
          // }
          // else {
          // father.setLeft(currentNode);
          // }
          // }
          // currentNode.setNodeBalance(getBalance(currentNode));
          // return currentNode;
        } else if (replace == currentNode.getRight()) {
          System.out.println("Fall2");
          currentNode.setRight(null);
        } else {
          System.out.println("Fall3");
          replace.setLeft(currentNode.getLeft());
          replace.setRight(currentNode.getRight());

          if (!isBalanced(replace)) {
            System.out.println("currentNode: " + currentNode.getKey()
                + " balance:  " + getBalance(currentNode));
            AVLNode testMe = updateNode(replace, false);
            if (father == null) {
              rootOfTheTree = testMe;
            } else {
              if (direction == TreeDirection.LEFT)
                father.setLeft(testMe);
              else
                father.setRight(testMe);
            }
            currentNode = testMe;
            // currentNode.setNodeBalance(getBalance(currentNode));
            return currentNode;
          }

        }
        if (currentNode == rootOfTheTree) {
          rootOfTheTree = replace;
          return rootOfTheTree;
        }
        return replace;
      } else {
        // delete leafs in Animal Language
        lang.nextStep("Blatt: " + key + " wird gelöscht");
        info = lang.newText(new Coordinates(50, 40),
            "Schlüssel " + Integer.toString(key) + " löschen.", "Löschtext",
            null, textProps);

        Text info1;
        info1 = lang.newText(new Coordinates(50, 60), Integer.toString(key)
            + " ist ein Blatt und kann einfach gelöscht werden.",
            "delete() Text", null, textProps);

        Circle leafNode = currentNode.getNodeCircle();
        leafNode.changeColor("fillcolor", Color.red, null, null);
        lang.nextStep("Rot");
        leafNode.hide();
        currentNode.getNodeText().hide();
        if (father != null) {
          // System.out.println("Father: "+ father.getKey());
          if (direction == TreeDirection.LEFT) {
            // System.out.println("father.getLeftEdge:  "+father.getLeftEdge().getName());
            father.getLeftEdge().hide();
            father.setLeft(null);
            father.leftEdge = null;
          } else {
            father.getRightEdge().hide();
            father.setRight(null);
            father.rightEdge = null;
          }
          // System.out.println("FatherBalanace: "+ getBalance(father));
          // System.out.println(father.getLeft());
          // System.out.println(father.getRight());
          // father.setNodeBalance(getBalance(father));
          // System.out.println("After delete Leaf, root has balance: "+getBalance(rootOfTheTree));
          // currentNode = null;
        }
        info.hide(new TicksTiming(50));
        info1.hide(new TicksTiming(50));
        // lang.nextStep();

        if (currentNode == rootOfTheTree) {
          rootOfTheTree = null;
        }
        return null;
      }
    } else {
      if (currentNode.getKey() > key && currentNode.getLeft() != null) {
        replace = nextDelete(key, currentNode.getLeft(), currentNode,
            TreeDirection.LEFT);
        currentNode.setLeft(replace);
        // leftSide = true;
      } else if (currentNode.getRight() != null) {
        replace = nextDelete(key, currentNode.getRight(), currentNode,
            TreeDirection.RIGHT);
        currentNode.setRight(replace);
        // leftSide = false;
      }

    }

    if (currentNode != null) {
      if (!isBalanced(currentNode)) {
        // System.out.println("IS NOT BALANCED");
        System.out.println("currentNode: " + currentNode.getKey()
            + " balance:  " + getBalance(currentNode));
        AVLNode testMe = updateNode(currentNode, true);
        if (father == null) {
          rootOfTheTree = testMe;
        } else {
          if (direction == TreeDirection.LEFT)
            father.setLeft(testMe);
          else
            father.setRight(testMe);
        }
        // if this was on the left side, father.setLeft(testMe); else
        // father.setRight(testMe);
        // System.out.println("After Up" +
        // "date currentNode: "+currentNode.getKey()+" balance:  "+getBalance(currentNode));
        // currentNode.setNodeBalance(getBalance(currentNode));
        // currentNode.setHeight(getHeight(currentNode));
        currentNode = testMe;
      }
    }
    // System.out.println("current Node in NextDelete: "+currentNode);
    return currentNode;
  }

  /**
   * findMaxAndRemove method finds the max BinaryNode from currentNode and
   * returns and removes it from the left subtree.
   * 
   * @param currentNode
   *          is the Node which the search for max Node begins
   * @return the max Node of the left subtree.
   */
  protected AVLNode findMaxAndRemove(AVLNode currentNode) {

    if (currentNode.getRight() != null) {
      AVLNode x = findMaxAndRemove(currentNode.getRight());
      // System.out.println("only currentNode: "+currentNode.getKey());
      // System.out.println("findMaxAndRemove, currentNode.getRight: "+currentNode.getRight().getKey());
      // System.out.println("x: "+x.getKey());
      if (currentNode.getRight() == x) {
        currentNode.setRight(x.getLeft());
        if (x.getLeft() != null) {
          currentNode.getRight().setFather(x.getFather());
          currentNode.getRight().setIncomingDirection(TreeDirection.RIGHT);
        }
        x.setLeft(null);
      }
      return x;
    } else
      return currentNode;
  }

  /**
   * findMinAndRemove method finds the max BinaryNode from currentNode and
   * returns and removes it from the right subtree.
   * 
   * @param currentNode
   *          is the Node which the search for max Node begins
   * @return the max Node of the right subtree.
   */
  protected AVLNode findMinAndRemove(AVLNode currentNode) {

    if (currentNode.getLeft() != null) {
      // System.out.println("getLeft:  "+currentNode.getLeft().getKey());
      AVLNode x = findMinAndRemove(currentNode.getLeft());
      if (currentNode.getLeft() == x) {
        currentNode.setLeft(x.getRight());
        if (x.getRight() != null) {
          currentNode.getLeft().setFather(x.getFather());
          currentNode.getLeft().setIncomingDirection(TreeDirection.LEFT);
        }
        x.setRight(null);
      }
      return x;
    } else
      return currentNode;
  }

  /**
   * If the currentNode is really balanced the result is true.
   * 
   * @param currentNode
   *          has to be checked of his balance
   * @return true if its absolute value is smaller than 2 otherwise false
   */
  protected boolean isBalanced(AVLNode currentNode) {

    if (Math.abs(getBalance(currentNode)) > 1) {
      return false;
    } else
      return true;
  }

  /**
   * The Balance is the height of the left subtree minus the right subtree.
   * 
   * @param x
   *          is the node for calculating the balance
   * @return the balance of the node x.
   */
  protected int getBalance(AVLNode x) {
    int a = getHeight(x.getLeft()), b = getHeight(x.getRight());
    return a - b;// getHeight(x.getLeft()) - getHeight(x.getRight());
  }

  /**
   * getHeight is a method for calculating the height of the given nodes
   * 
   * @param x
   *          is the node we want to calculate the height
   * @return the height of the node x
   */
  protected int getHeight(AVLNode x) {

    if (x == null) {
      return 0;
    }

    else {
      return 1 + max(getHeight(x.getLeft()), getHeight(x.getRight()));
    }
  }

  /**
   * Returns the maximum A or B
   * 
   * @param A
   *          int
   * @param B
   *          int
   * @return the maximum of the both parameters A or B
   */
  protected int max(int A, int B) {

    if (A < B)
      return B;

    else
      return A;
  }

  /**
   * This method draws a letter R or L near the edges.
   * 
   * @param up
   *          is the father node
   * @param down
   *          is the son
   * @param text
   *          is the Letter.
   */
  protected Text drawLetter(AVLNode up, AVLNode down, String text) {
    // Text t = lang.newText( new Coordinates(0,0), "", "", new Hidden());
    Text info1 = lang.newText(new Coordinates(0, 0), "", "", new Hidden());
    ;

    if (text == "R") {
      info1 = lang.newText((new Offset(20, 0, up.getRightEdge(),
          AnimalScript.DIRECTION_N)), text, "Info Text", null, textProps);
      return info1;
      // info1.hide(new TicksTiming(120));
    } else if (text == "L") {
      // Text info1 = lang.newText(new Coordinates(up.getXcoordinate()- x,
      // up.getYcoordinate()+y),
      // text, "Info Text",null,textProps);
      info1 = lang.newText((new Offset(-20, 0, up.getLeftEdge(),
          AnimalScript.DIRECTION_N)), text, "Info Text", null, textProps);
      return info1;
      // info1.hide(new TicksTiming(120));
    }
    return info1;
    //
  }

  /**
   * Updates the currentNode
   * 
   * @param currentNode
   *          is the node which needs an update because the |balance| > 1
   */
  protected AVLNode updateNode(AVLNode currentNode, boolean toInsert) {

    lang.nextStep();

    Text unbalanced, sonOfUnbalanced;

    AVLNode rotation = currentNode;
    int localBalance = getBalance(currentNode);
    AVLNode node = currentNode.getLeft();
    if (node != null) {
      // LL --> rotateRight
      if (localBalance == 2 && getBalance(node) == 1) {
        info = lang.newText(new Coordinates(50, 40),
            "LL-Situation --> Rechtsrotation", "Info Text", null, textProps);
        currentNode.getLeftEdge().changeColor("color", Color.red, null, null);
        node.getLeftEdge().changeColor("color", Color.red, null, null);

        unbalanced = lang.newText(
            (new Offset(10, 0, currentNode.getNodeCircle(),
                AnimalScript.DIRECTION_NE)), Integer.toString(localBalance),
            "unbalanced 2", null, textProps);

        sonOfUnbalanced = lang.newText((new Offset(10, 0, node.getNodeCircle(),
            AnimalScript.DIRECTION_NE)), Integer.toString(getBalance(node)),
            "unbalanced 1", null, textProps);

        Text l1 = drawLetter(currentNode, node, "L");
        Text l2 = drawLetter(node, node.getLeft(), "L");
        lang.nextStep();
        info.hide();
        unbalanced.hide();
        sonOfUnbalanced.hide();
        l1.hide();
        l2.hide();
        rotationTranslate(node, currentNode, toInsert);
        rotation = rotateRight(currentNode);
      }
      // LL --> rotateRightAfterDelete
      else if (localBalance == 2 && getBalance(node) == 0) {
        info = lang.newText(new Coordinates(50, 40),
            "LL-Situation nach dem Löschen --> Rechtsrotation", "Info Text",
            null, textProps);
        currentNode.getLeftEdge().changeColor("color", Color.red, null, null);
        node.getLeftEdge().changeColor("color", Color.red, null, null);
        Text l1 = drawLetter(currentNode, node, "L");
        Text l2 = drawLetter(node, node.getLeft(), "L");
        unbalanced = lang.newText(
            (new Offset(10, 10, currentNode.getNodeCircle(),
                AnimalScript.DIRECTION_NE)), Integer.toString(localBalance),
            "unbalanced 2", null, textProps);

        sonOfUnbalanced = lang.newText((new Offset(10, 10,
            node.getNodeCircle(), AnimalScript.DIRECTION_NE)), Integer
            .toString(getBalance(node)), "unbalanced 1", null, textProps);
        lang.nextStep();
        info.hide();
        unbalanced.hide();
        sonOfUnbalanced.hide();
        l1.hide();
        l2.hide();
        rotationTranslate(node, currentNode, true);
        rotation = rotateRightAfterDelete(currentNode);
      }
      // LR --> rotateLeftRight
      else if (localBalance == 2 && getBalance(node) == -1) {
        info = lang.newText(new Coordinates(50, 40),
            "LR-Situation --> Linksrechtsrotation", "Info Text", null,
            textProps);
        if (currentNode.getLeftEdge() != null) {
          currentNode.getLeftEdge().changeColor("color", Color.red, null, null);
        }
        node.getRightEdge().changeColor("color", Color.red, null, null);
        Text l1 = drawLetter(currentNode, node, "L");
        Text l2 = drawLetter(node, node.getRight(), "R");
        unbalanced = lang.newText(
            (new Offset(10, 10, currentNode.getNodeCircle(),
                AnimalScript.DIRECTION_NE)), Integer.toString(localBalance),
            "unbalanced 2", null, textProps);

        sonOfUnbalanced = lang.newText((new Offset(10, 10,
            node.getNodeCircle(), AnimalScript.DIRECTION_NE)), Integer
            .toString(getBalance(node)), "unbalanced -1", null, textProps);
        lang.nextStep();
        info.hide();
        unbalanced.hide();
        sonOfUnbalanced.hide();
        l1.hide();
        l2.hide();
        rotationTranslate(node.getRight(), currentNode, toInsert);
        rotation = rotateLeftRight(currentNode);
      }
    }
    node = currentNode.getRight();
    if (node != null) {
      // RR --> rotateLeft
      if (localBalance == -2 && getBalance(node) == -1) {
        info = lang.newText(new Coordinates(50, 40),
            "RR-Situation --> Linksrotation", "Info Text", null, textProps);
        currentNode.getRightEdge().changeColor("color", Color.red, null, null);
        node.getRightEdge().changeColor("color", Color.red, null, null);
        Text l1 = drawLetter(currentNode, node, "R");
        Text l2 = drawLetter(node, node.getRight(), "R");
        unbalanced = lang.newText(
            (new Offset(10, 10, currentNode.getNodeCircle(),
                AnimalScript.DIRECTION_NE)), Integer.toString(localBalance),
            "unbalanced 2", null, textProps);

        sonOfUnbalanced = lang.newText((new Offset(10, 10,
            node.getNodeCircle(), AnimalScript.DIRECTION_NE)), Integer
            .toString(getBalance(node)), "unbalanced -1", null, textProps);
        lang.nextStep();
        info.hide();
        unbalanced.hide();
        sonOfUnbalanced.hide();
        l1.hide();
        l2.hide();
        rotationTranslate(node, currentNode, toInsert);
        rotation = rotateLeft(currentNode);
      }
      // RR --> rotateLeftAfterDelete
      else if (localBalance == -2 && getBalance(node) == 0) {
        System.out.println("//RR --> rotateLeftAfterDelete");
        info = lang.newText(new Coordinates(50, 40),
            "RR-Situation nach dem Löschen --> Linksrotation", "Info Text",
            null, textProps);
        currentNode.getRightEdge().changeColor("color", Color.red, null, null);
        node.getRightEdge().changeColor("color", Color.red, null, null);
        Text l1 = drawLetter(currentNode, node, "R");
        Text l2 = drawLetter(node, node.getRight(), "R");
        unbalanced = lang.newText(
            (new Offset(10, 10, currentNode.getNodeCircle(),
                AnimalScript.DIRECTION_NE)), Integer.toString(localBalance),
            "unbalanced -2", null, textProps);

        sonOfUnbalanced = lang.newText((new Offset(10, 10,
            node.getNodeCircle(), AnimalScript.DIRECTION_NE)), Integer
            .toString(getBalance(node)), "unbalanced 0", null, textProps);
        lang.nextStep();
        info.hide();
        unbalanced.hide();
        sonOfUnbalanced.hide();
        l1.hide();
        l2.hide();
        rotationTranslate(node, currentNode, true);
        rotation = rotateLeftAfterDelete(currentNode);
      }
      // RL --> rotateRightLeft
      else if (localBalance == -2 && getBalance(node) == 1) {
        info = lang.newText(new Coordinates(50, 40),
            "RL-Situation --> Rechtslinksrotation", "Info Text", null,
            textProps);
        currentNode.getRightEdge().changeColor("color", Color.red, null, null);
        node.getLeftEdge().changeColor("color", Color.red, null, null);
        Text l1 = drawLetter(currentNode, node, "R");
        Text l2 = drawLetter(node, node.getLeft(), "L");
        unbalanced = lang.newText(
            (new Offset(10, 10, currentNode.getNodeCircle(),
                AnimalScript.DIRECTION_NE)), Integer.toString(localBalance),
            "unbalanced -2", null, textProps);

        sonOfUnbalanced = lang.newText((new Offset(10, 10,
            node.getNodeCircle(), AnimalScript.DIRECTION_NE)), Integer
            .toString(getBalance(node)), "unbalanced 1", null, textProps);
        lang.nextStep();
        info.hide();
        unbalanced.hide();
        sonOfUnbalanced.hide();
        l1.hide();
        l2.hide();
        currentNode.getRightEdge()
            .changeColor("color", Color.BLACK, null, null);
        node.getLeftEdge().changeColor("color", Color.BLACK, null, null);
        rotationTranslate(node.getLeft(), currentNode, toInsert);
        rotation = rotateRightLeft(currentNode);
      }
    }
    info = lang.newText(new Coordinates(50, 40), "Rotation abgeschlossen.",
        "Info Text", null, textProps);
    lang.nextStep();
    info.hide();
    return rotation;
  }

  // MoveOperations m = new MoveOperations();
  protected void rotationTranslate(AVLNode childOfUnbalanced,
      AVLNode unbalanceNode, boolean toInsert) {
    translate(childOfUnbalanced, unbalanceNode, toInsert);
    moveDownSubtree(childOfUnbalanced, unbalanceNode);
  }

  protected void moveDownSubtree(AVLNode childOfUnbalanced,
      AVLNode unbalanceNode) {
    int x = unbalanceNode.getXcoordinate();
    int y = unbalanceNode.getYcoordinate();

    int newX = 0;
    if (unbalanceNode.getKey() > childOfUnbalanced.getKey()) {
      // new position of xy Node
      newX = unbalanceNode.getXcoordinate() + (unbalanceNode.getWidth() / 2);
    } else {
      newX = unbalanceNode.getXcoordinate() - (unbalanceNode.getWidth() / 2);
    }
    int newY = unbalanceNode.getYcoordinate() + 80;
    int newWidth = unbalanceNode.getWidth() / 2;

    Node[] nodes = new Node[2];
    nodes[0] = new Coordinates(x, y);
    nodes[1] = new Coordinates(newX, newY);

    Polyline moveLine = lang.newPolyline(nodes, "moveline", new Hidden());
    try {
      unbalanceNode.getNodeCircle().moveVia(null, "translate", moveLine, null,
          new TicksTiming(60));
      unbalanceNode.getNodeText().moveVia(null, "translate", moveLine, null,
          new TicksTiming(60));

    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    unbalanceNode.getNodeCircle().changeColor("fillcolor", Color.yellow, null,
        null);
    unbalanceNode.getNodeCircle().show();
    unbalanceNode.getNodeText().show();
    // neu Brauche ich das wirklich ???
    if (unbalanceNode.getLeftEdge() != null) {
      unbalanceNode.getLeftEdge().changeColor("color", Color.BLACK, null, null);
      unbalanceNode.getLeftEdge().show();
    }
    // neu
    unbalanceNode.setFather(childOfUnbalanced);
    unbalanceNode.setX(newX);
    unbalanceNode.setY(newY);
    unbalanceNode.setWidth(newWidth);

    if (unbalanceNode.getKey() > childOfUnbalanced.getKey()) {
      childOfUnbalanced.setRightEdge(lang, unbalanceNode,
          unbalanceNode.getNodeCircle(), childOfUnbalanced.getXcoordinate(),
          childOfUnbalanced.getYcoordinate());
      unbalanceNode.setIncomingDirection(TreeDirection.RIGHT);
      if (unbalanceNode.getLeftEdge() != null) {
        unbalanceNode.getLeftEdge().hide();
        unbalanceNode.leftEdge = null;
      }

      if (unbalanceNode.getRight() != null) {
        if (unbalanceNode.getRight().getXcoordinate() == unbalanceNode
            .getXcoordinate()) {
          moveDownSubtree(unbalanceNode, unbalanceNode.getRight());
          // NEU
          if (unbalanceNode.getRight().getLeft() != null) {
            moveSubtree(unbalanceNode.getRight().getLeft());
          }
        }
        if (unbalanceNode.getLeft() != null) {
          moveSubtree(unbalanceNode.getLeft());
        }
      }
    } else {
      childOfUnbalanced.setLeftEdge(lang, unbalanceNode,
          unbalanceNode.getNodeCircle(), childOfUnbalanced.getXcoordinate(),
          childOfUnbalanced.getYcoordinate());
      unbalanceNode.setIncomingDirection(TreeDirection.LEFT);
      if (unbalanceNode.getRightEdge() != null) {
        unbalanceNode.getRightEdge().hide();
        unbalanceNode.rightEdge = null;
      }

      if (unbalanceNode.getLeft() != null) {
        if (unbalanceNode.getLeft().getXcoordinate() == unbalanceNode
            .getXcoordinate()) {
          moveDownSubtree(unbalanceNode, unbalanceNode.getLeft());
          // NEU
          if (unbalanceNode.getLeft().getRight() != null) {
            moveSubtree(unbalanceNode.getLeft().getRight());
          }
        }
        if (unbalanceNode.getRight() != null) {
          moveSubtree(unbalanceNode.getRight());
        }
      }
    }

  }

  /**
   * Method rotateLeft is active when node x has a |balance| > 1, has right
   * child and the child has a right successor. (RR)
   * 
   * @param x
   *          is the node which has a |balance| > 1
   * @return the new root of the subtree.
   */
  protected AVLNode rotateLeft(AVLNode x) {

    AVLNode rsucc; // Rechter Nachfolger
    AVLNode tmp; // Knoten zur Zwischenspeicherung des linken Nachfolgers des
                 // rechten Nachfolgers

    rsucc = x.getRight(); // Merk dir rechten Nachfolger
    tmp = rsucc.getLeft(); // Merk dir linke Nachfolger des rechten Nachfolgers
    // rotationTranslate(rsucc, x);

    x.setRight(null); // Rechten Nachfolger aus Baum herauslöschen
    rsucc.setLeft(null); // Linken Nachfolger des rechten Nachfolgers aus Baum
                         // herauslöschen

    x.setRight(tmp); // Ehemaliger linker Nf des rechten Nf wird zu neuem
                     // rechten Nf
    rsucc.setLeft(x); // linker Nachfolger des rechten Nachfolgers wird der
                      // rotierte Knoten

    x.setFather(rsucc);

    if (tmp != null) {
      tmp.setFather(x);
      tmp.setIncomingDirection(TreeDirection.RIGHT);

      moveSubtree(tmp);
      if (rsucc.getRight() != null) {
        moveSubtree(rsucc.getRight());
      }
    }

    // x.setNodeBalance(getBalance(x));
    // x.setHeight(getHeight(x));

    return rsucc; // Knoten wird durch rechten Nachfolger ersetzt
  }

  /**
   * Method rotateRight is active when node x has a |balance| > 1, has left
   * child and the child has a left successor. (LL)
   * 
   * @param x
   *          is the node which has a |balance| > 1
   * @return the new root of the subtree.
   */
  protected AVLNode rotateRight(AVLNode unbalanceNode) {

    AVLNode lsucc; // Linker Nachfolger
    AVLNode tmp; // Knoten zur Zwischenspeicherung

    lsucc = unbalanceNode.getLeft(); // Merk dir linken Knoten
    tmp = lsucc.getRight(); // Merk dir rechten Nachfolger von Linken Knoten
    // rotationTranslate(lsucc, unbalanceNode);

    unbalanceNode.setLeft(null); // Linker Nachfolger herauslöschen
    lsucc.setRight(null); // Rechten Nachfolger von Linken Knoten herauslöschen

    unbalanceNode.setLeft(tmp); // Linker Nachfolger wird rechter Nachfolger von
                                // linken Knoten
    lsucc.setRight(unbalanceNode);// Knoten wird rechter Nachfolger des
                                  // ehemaligen linken Knotens
    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    // unbalanceNode.setFather(lsucc);
    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    if (tmp != null) {
      tmp.setFather(unbalanceNode);
      tmp.setIncomingDirection(TreeDirection.LEFT);

      moveSubtree(tmp);
      if (lsucc.getLeft() != null) {
        moveSubtree(lsucc.getLeft());
      }
    }

    // unbalanceNode.setNodeBalance(getBalance(unbalanceNode));
    // unbalanceNode.setHeight(getHeight(unbalanceNode));

    return lsucc;
  }

  /**
   * Method rotateRightLeft is active when node x has a |balance| > 1, has right
   * child and the child has a left successor. (RL)
   * 
   * @param x
   *          is the node which has a |balance| > 1
   * @return the new root of the subtree.
   */
  protected AVLNode rotateRightLeft(AVLNode x) {
    x.setRight(rotateRight(x.getRight()));
    return rotateLeft(x);
  }

  /**
   * Method rotateLeftRight is active when node x has a |balance| > 1, has left
   * child and the child has a right successor. (LR)
   * 
   * @param x
   *          is the node which has a |balance| > 1
   * @return the new root of the subtree.
   */
  protected AVLNode rotateLeftRight(AVLNode x) {
    x.setLeft(rotateLeft(x.getLeft()));
    return rotateRight(x);
  }

  /**
   * 
   * rotateRightAfterDelete(Node x) is a method which contains the case:
   * localBalance of node x == 2 && getBalance of the left child == 0
   * 
   * @param x
   *          is the current Node with balance 2
   * @return the new root of the subtree
   */
  protected AVLNode rotateRightAfterDelete(AVLNode x) {

    AVLNode lsucc;
    AVLNode tmp; // Knoten zur Zwischenspeicherung

    lsucc = x.getLeft(); // Left Node is the new subroot
    tmp = lsucc.getRight();

    lsucc.setRight(x);
    x.setLeft(tmp);

    if (tmp != null) {
      tmp.setFather(x);
      tmp.setIncomingDirection(TreeDirection.LEFT);

      moveSubtree(tmp);
      if (lsucc.getLeft() != null) {
        moveSubtree(lsucc.getLeft());
      }
    }

    return lsucc;
  }

  /**
   * rotateLeftAfterDelete(Node x) is a method which contains the case:
   * localBalance of node x == -2 && getBalance of the right child == 0
   * 
   * @param x
   *          is the current Node with balance -2
   * @return the new root of the subtree
   */
  protected AVLNode rotateLeftAfterDelete(AVLNode x) {

    AVLNode rsucc;
    AVLNode tmp;

    rsucc = x.getRight(); // Right Node is the new subroot
    tmp = rsucc.getLeft();

    rsucc.setLeft(x);
    x.setRight(tmp);
    // NEU NEU NEU
    if (tmp != null) {
      tmp.setFather(x);
      tmp.setIncomingDirection(TreeDirection.RIGHT);

      moveSubtree(tmp);
      if (rsucc.getRight() != null) {
        moveSubtree(rsucc.getRight());
      }
    }

    return rsucc;
  }

  /**
   * getTreeDFS is a depth first search
   * 
   * @param getCNode
   *          is the node where the search begins
   */
  public void getTreeDFS(AVLNode getCNode) {

    System.out.println(getCNode.getKey());

    if (getCNode.getLeft() != null) {
      getTreeDFS(getCNode.getLeft());
    }
    if (getCNode.getRight() != null) {
      getTreeDFS(getCNode.getRight());
    }
  }

  /**
   * getTreeBFS is a breadth first search
   * 
   * @param getCNode
   *          is the node where the search begins
   */
  public void getTreeBFS(AVLNode getCNode) {

    StringBuilder sb = new StringBuilder(2048);
    // TestMethode auf Parameter aller Knoten
    // Vector v = new Vector();
    sb.append("\nNode: ").append(getCNode.getKey()).append(" has x:");
    sb.append(getCNode.getXcoordinate()).append(" y: ")
        .append(getCNode.getYcoordinate());
    sb.append(" width: ").append(getCNode.getWidth());

    // System.out.println("Node: "+getCNode.getKey()+" has x:"+getCNode.getXcoordinate()+" y: "+getCNode.getYcoordinate()+" width: "+getCNode.getWidth());
    sb.append("\nNode: ").append(getCNode.getKey()).append(" has leftEdge: ");
    sb.append(getCNode.getLeftEdge()).append(" has rightEdge: ")
        .append(getCNode.getRightEdge());
    sb.append("\nNode: ").append(getCNode.getKey()).append(" has leftChild: ");
    sb.append(getCNode.getLeft()).append(" has rightChild: ")
        .append(getCNode.getRight());
    sb.append("\nNode: ").append(getCNode.getKey())
        .append(" has an incoming Direction: ");
    sb.append(getCNode.getIncomingDirection());
    sb.append("\nNode: ").append(getCNode.getKey()).append(" has balance: ");
    sb.append(getCNode.getNodeBalance());
    sb.append("\nNode: ").append(getCNode.getKey()).append(" has height: ");
    sb.append(getCNode.getHeight());
    if (getCNode.getFather() != null)
      sb.append("\nNode: ").append(getCNode.getKey()).append(" has father: ")
          .append(getCNode.getFather().getKey());
    sb.append("\nXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n");

    System.out.println(sb.toString());

    if (getCNode.getLeft() != null) {
      q.push(getCNode.getLeft());
    }
    if (getCNode.getRight() != null) {
      q.push(getCNode.getRight());
    }
    if (!q.isEmpty()) {
      getTreeBFS(q.pop());
    }
  }

  /**
   * translate method replaces the replace node with the delete node
   * 
   * @param repNode
   * @param toDelete
   * @return the replace node Circle with his new position
   */
  protected Circle translate(AVLNode repNode, AVLNode toDelete, boolean toInsert) {

    lang.nextStep();
    if (toInsert) {
      info = lang.newText(new Coordinates(50, 40),
          "Schlüssel " + Integer.toString(toDelete.getKey())
              + " rebalancieren.", "Rebalancierungstext", null, textProps);
    } else {
      info = lang.newText(new Coordinates(50, 40),
          "Schlüssel " + Integer.toString(toDelete.getKey()) + " löschen.",
          "Löschtext", null, textProps);
    }

    SourceCodeProperties titleProps = new SourceCodeProperties();
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 12));
    titleProps
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.green);

    SourceCode title = lang.newSourceCode(new Coordinates(50, 70),
        "sourceCode", null, titleProps);

    // if (toDelete.getLeft() != null) {
    // title.addCodeLine(Integer.toString(toDelete.getKey())+" ist ein innerer Knoten.",
    // null, 0, null);
    // title.addCodeLine("Da er einen linken Teilbaum oder nur linken Nachfolger besitzt, wird",
    // null, 0, null);
    // title.addCodeLine("der zu löschende Knoten mit den größten Schlüssel aus dem linken ",
    // null, 0, null);
    // title.addCodeLine("Teilbaum(kann auch nur ein Knoten sein) ersetzt werden.",
    // null, 0, null);
    // title.addCodeLine("Der zu ersetzende Knoten ist: "+Integer.toString(repNode.getKey()),
    // null, 0, null);
    // }
    // else if (toDelete.getLeft() == null && toDelete.getRight() != null) {
    // title.addCodeLine(Integer.toString(toDelete.getKey())+" ist ein innerer Knoten.",
    // null, 0, null);
    // title.addCodeLine("Da er nur einen rechten Teilbaum (oder nur einen rechten Nachfolger) besitzt, wird",
    // null, 0, null);
    // title.addCodeLine("der zu löschende Knoten mit den kleinsten Schlüssel aus dem rechten ",
    // null, 0, null);
    // title.addCodeLine("Teilbaum(kann auch nur ein Knoten sein) ersetzt werden.",
    // null, 0, null);
    // title.addCodeLine("Der zu ersetzende Knoten ist: "+Integer.toString(repNode.getKey()),
    // null, 0, null);
    // }

    lang.nextStep();
    toDelete.getNodeCircle().changeColor("fillcolor", Color.red, null, null);

    repNode.getNodeCircle().changeColor("fillcolor", Color.green, null, null);
    lang.nextStep();
    // position of repNode before translate
    int x = repNode.getXcoordinate();
    int y = repNode.getYcoordinate();
    TreeDirection oldRepDir = repNode.getIncomingDirection();
    // AVLNode oldRepNode = repNode;
    // BinaryNode oldLeftChild = repNode.getLeft();
    // BinaryNode oldRightChild = repNode.getRight();
    // boolean repNodeIsLeaf = repNode.isLeaf();
    // AVLNode oldFather = repNode.getFather();

    // new position for repNode
    int newX = toDelete.getXcoordinate();
    int newY = toDelete.getYcoordinate();
    int newWidth = toDelete.getWidth();
    AVLNode newFather = toDelete.getFather();

    Node[] nodes = new Node[2];
    nodes[0] = new Coordinates(x, y);
    nodes[1] = new Coordinates(newX, newY);

    Polyline moveLine = lang.newPolyline(nodes, "moveline", new Hidden());

    // Here we move repNode to the position of toDeleteNode
    try {
      repNode.getNodeCircle().moveVia(null, "translate", moveLine, null,
          new TicksTiming(60));
      repNode.getNodeText().moveVia(null, "translate", moveLine, null,
          new TicksTiming(60));

    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }
    // Father of repNode is repNodeFather
    AVLNode repNodeFather = repNode.getFather();

    // Nachdem repNode zu seiner neuen Position gelangt ist, versteckt der Vater
    // seine Kanten
    if (repNode.getIncomingDirection() == TreeDirection.LEFT) {
      repNodeFather.getLeftEdge().hide();
      repNodeFather.leftEdge = null;
    } else {
      repNodeFather.getRightEdge().hide();
      repNodeFather.rightEdge = null;
    }

    // toDelete.getNodeCircle().hide();
    // toDelete.getNodeText().hide();
    if (repNode.getLeftEdge() != null) {
      repNode.getLeftEdge().hide();
    }
    if (repNode.getRightEdge() != null) {
      repNode.getRightEdge().hide();
    }
    lang.nextStep();
    repNode.getNodeCircle().changeColor("fillcolor", Color.yellow, null, null);
    info.hide(new TicksTiming(50));
    title.hide(new TicksTiming(50));

    // The replace Node gets his new place where toDelete Node was.
    // Moreover he gets his new BinaryNode parameters: x, y, width,
    // incomingDirection,
    // father, his children get correct father(repNode) and he gets the correct
    // left/right Edge.
    // updateParametersNode(repNode, toDelete);
    repNode.setX(newX);
    repNode.setY(newY);
    repNode.setWidth(newWidth);
    repNode.setIncomingDirection(toDelete.getIncomingDirection());
    repNode.setFather(newFather);
    // Children of the already replaced Node/(Children of toDeleteNode) should
    // have the right father.
    if (toDelete.getLeft() != null) {
      if (toDelete.getLeft() == repNode) {
        toDelete.getLeft().setFather(toDelete.getFather());
      } else {
        toDelete.getLeft().setFather(repNode);
      }
    }
    if (toDelete.getRight() != null) {
      if (toDelete.getRight() == repNode) {
        toDelete.getRight().setFather(toDelete.getFather());
      } else
        toDelete.getRight().setFather(repNode);
    }
    // replace Node gets the leftEdge of toDelete Node if available
    if (toDelete.getLeftEdge() != null) {
      repNode.leftEdge = toDelete.getLeftEdge();

    }
    // replace Node gets the rightEdge of toDelete Node if available
    if (toDelete.getRightEdge() != null) {
      repNode.rightEdge = toDelete.getRightEdge();
    }

    toDelete.getNodeCircle().hide();
    toDelete.getNodeText().hide();

    // System.out.println("repNode after the move: "+repNode.getKey()+" direction: "+repNode.getIncomingDirection()+" old dir: "+oldRepDir);
    // System.out.println("oldRepNode: "+oldRepNode.getKey()+" leftChild:  "+oldRepNode.getLeftReplaceChild()+"rightChild:  "+oldRepNode.getRightReplaceChild());

    // if (!repNode.isLeaf()) {
    if (oldRepDir == TreeDirection.LEFT) {
      if (repNodeFather.getLeft() != null) {
        System.out.println("repNodeFather: " + repNodeFather.getKey()
            + "  repNodeFather.getLeft:  " + repNodeFather.getLeft().getKey());
        moveSubtree(repNodeFather.getLeft());
      }
    } else {
      if (repNodeFather.getRight() != null) {
        System.out.println("repNodeFather.getRight:  "
            + repNodeFather.getRight().getKey());
        moveSubtree(repNodeFather.getRight());
      }
    }
    // }
    return repNode.getNodeCircle();
  }

  /**
   * Moves a left or a right subtree.
   * 
   * @param rootOfSubtree
   */
  protected void moveSubtree(AVLNode rootOfSubtree) {

    // System.out.println("rootOfSubtree: "+rootOfSubtree.getKey());
    AVLNode dockingNode = rootOfSubtree.getFather();
    // System.out.println("dockingNode: "+dockingNode);
    if (dockingNode == null) {
      if (rootOfSubtree.getLeft() != null) {
        moveSubtree(rootOfSubtree.getLeft());
      } else if (rootOfSubtree.getRight() != null) {
        moveSubtree(rootOfSubtree.getRight());
      }
    }
    if (dockingNode != null) {
      // System.out.println("dockingNode: "+dockingNode.getKey());

      // old position of rootSubtree
      int x = rootOfSubtree.getXcoordinate();
      int y = rootOfSubtree.getYcoordinate();
      // int width = rootOfSubtree.getWidth();

      // calculate his new position
      int newX;
      if (rootOfSubtree.getIncomingDirection() == TreeDirection.LEFT) {
        newX = dockingNode.getXcoordinate() - (dockingNode.getWidth() / 2);
      } else {
        newX = dockingNode.getXcoordinate() + (dockingNode.getWidth() / 2);
      }
      int newY = dockingNode.getYcoordinate() + 80;
      int newWidth = dockingNode.getWidth() / 2;

      // Idea: If x position(old) has the same value as the newX, then don't
      // move Node x.
      // We don't need to move a node at the same position. Thats the reason why
      // we ask with an if.
      if (x != newX) {

        if (rootOfSubtree.getLeftEdge() != null) {
          rootOfSubtree.getLeftEdge().hide();
          rootOfSubtree.leftEdge = null;
        }
        if (rootOfSubtree.getRight() != null) {
          rootOfSubtree.getRightEdge().hide();
          rootOfSubtree.rightEdge = null;
        }

        // System.out.println("(newX: "+newX+", newY: "+newY+", newWidth: "+newWidth+")");

        Node[] nodes = new Node[2];
        nodes[0] = new Coordinates(x, y);
        nodes[1] = new Coordinates(newX, newY);

        Polyline moveLine = lang.newPolyline(nodes, "moveline", new Hidden());
        try {
          rootOfSubtree.getNodeCircle().moveVia(null, "translate", moveLine,
              null, new TicksTiming(60));
          rootOfSubtree.getNodeText().moveVia(null, "translate", moveLine,
              null, new TicksTiming(60));
        } catch (IllegalDirectionException e) {
          e.printStackTrace();
        }

        rootOfSubtree.setX(newX);
        rootOfSubtree.setY(newY);
        rootOfSubtree.setWidth(newWidth);

        if (rootOfSubtree.getIncomingDirection() == TreeDirection.LEFT) {
          dockingNode.setLeftEdge(lang, rootOfSubtree,
              rootOfSubtree.getNodeCircle(), dockingNode.getXcoordinate(),
              dockingNode.getYcoordinate());
        } else {
          dockingNode.setRightEdge(lang, rootOfSubtree,
              rootOfSubtree.getNodeCircle(), dockingNode.getXcoordinate(),
              dockingNode.getYcoordinate());
        }
      }
      if (rootOfSubtree.getLeft() != null) {
        moveSubtree(rootOfSubtree.getLeft());
      }
      if (rootOfSubtree.getRight() != null) {
        moveSubtree(rootOfSubtree.getRight());
      }
    }
  }

  public void myInit(SourceCodeProperties titleProps) {
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    Node upperLeft = new Coordinates(20, 30);
    Node lowerRight = new Coordinates(200, 60);
    Rect rect = lang.newRect(upperLeft, lowerRight, "AvlTree", null, rectProps);

    // SourceCodeProperties titleProps = new SourceCodeProperties();
    // titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("Monospaced",Font.BOLD, 16));
    // titleProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.green);

    SourceCode title = lang.newSourceCode(new Coordinates(30, 20),
        "sourceCode", null, titleProps);

    title.addCodeLine("AVL Baum", null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "Das besondere an ihm ist, dass er durch verschiedene Rotationsaktivitaeten balanciert bleibt.",
            null, 0, null);
    title
        .addCodeLine(
            "Alle linken und rechten Teilbaeume eines Knoten sind wiederum AVL Baeume und ",
            null, 0, null);
    title.addCodeLine(
        "haben hoechstens einen Hoehenunterschied im Absolutbetrag von 1.",
        null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "Die Hauptoperationen des AVL Baums sind: Suche, Einfügen und Loeschen.",
            null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "Die Balance eines Knotens wird durch die max. Hoehe seines linken Teilbaums minus der max. Hoehe seines rechten Teilbaums berechnet.",
            null, 0, null);
    title
        .addCodeLine(
            "Beim Einfügen und Loeschen kommen verschiedene Rotationstypen zum Einsatz, wenn der Balancefaktor im Absolutbetrag",
            null, 0, null);
    title
        .addCodeLine(
            "größer als 1 wird. Die Typen sind: Linksrotation, Rechtsrotation, Linksrechtsrotation und Rechtslinksrotation.",
            null, 0, null);
    title.addCodeLine("", null, 0, null);
    title.addCodeLine("Der AVL Baum besitzt eine Komplexitaet von O(log(n)).",
        null, 0, null);

    lang.nextStep();
    title.hide();
    rect.hide();

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));

    info = lang.newText(new Coordinates(50, 20),
        "Aktuelle Aktivitäten im AVL Baum", "Info Text", null, textProps);
    Font f = new Font("Monospaced", Font.BOLD, 16);
    info.setFont(f, null, null);
  }

  public static final String DESCRIPTION = "AVL Baum \n"
                                             + " \n"
                                             + "Das besondere an ihm ist, dass er durch verschiedene Rotationsaktivitaeten balanciert bleibt. \n"
                                             + "Alle linken und rechten Teilbaeume eines Knoten sind wiederum AVL Baeume und \n"
                                             + "haben hoechstens einen Hoehenunterschied im Absolutbetrag von 1. \n"
                                             + "\n"
                                             + "Die Hauptoperationen des AVL Baums sind: Suche, Einfügen und Loeschen. \n"
                                             + "\n"
                                             + "Die Balance eines Knotens wird durch die max. Hoehe seines linken Teilbaums minus der max. Hoehe seines rechten Teilbaums berechnet. \n"
                                             + "Beim Einfügen und Loeschen kommen verschiedene Rotationstypen zum Einsatz, wenn der Balancefaktor im Absolutbetrag \n"
                                             + "größer als 1 wird. Die Typen sind: Linksrotation, Rechtsrotation, Linksrechtsrotation und Rechtslinksrotation. \n"
                                             + " \n"
                                             + "Der AVL Baum besitzt eine Komplexitaet von O(log(n)).";

  @Override
  public String generate(AnimationPropertiesContainer properties,
      Hashtable<String, Object> primitives) {
    keys = (int[]) primitives.get("keys");

    titleProps = (SourceCodeProperties) properties
        .getPropertiesByName("titleProps");
    circ = (CircleProperties) properties.getPropertiesByName("circ");

    myInit(titleProps);

    entry = lang.newText(new Coordinates(550, 25), "Eingefügte Schlüssel:",
        "Inserted keys info", null, textProps);
    deletions = lang.newText(new Coordinates(550, 55), "Gelöschte Schlüssel:",
        "Deleted keys info", null, textProps);

    int k = 0;
    int y = 0;
    for (int i = 0; i < keys.length; i++) {

      if (keys[i] < 0) {
        int deleteKey = keys[i] * -1;
        deletions = lang
            .newText(new Coordinates(760 + (40 * k), 55), "" + deleteKey + ",",
                "Eintrag: " + deleteKey, new Hidden(), textProps);

        delete(deleteKey);
        deletions.show();
        k++;
      } else {
        entry = lang.newText(new Coordinates(760 + (40 * y), 25), "" + keys[i]
            + ",", "Eintrag: " + keys[i], new Hidden(), textProps);

        insert(keys[i]);
        entry.show();
        y++;
      }
    }

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "AVL-Baum";
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
    return "AVL-Tree";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("AVL-Tree", "Ioannis Tsigaridas", 800, 640);
    lang.setStepMode(true);

    // arrayProps = null;
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
