package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.tsigaridas.BinaryNode;
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
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.TicksTiming;

public class EffectiveBinaryTree implements Generator {
  private BinaryNode           rootOfTheTree;
  // private String[] keys;
  private int[]                keys;
  private SourceCodeProperties titleProps;
  // private SourceCode title;
  private CircleProperties     circ;

  Schlange<BinaryNode>         q = new Schlange<BinaryNode>();

  public enum TreeDirection {
    LEFT, RIGHT
  }

  private Language       lang;

  private Text           info, entry, deletions;
  private TextProperties textProps;

  public EffectiveBinaryTree() {
  }

  /**
   * @return the root of the AVLTree
   */
  public BinaryNode getRoot() {
    return rootOfTheTree;
  }

  /**
   * This method inserts a new key (BinaryNode) into the tree.
   * 
   * @param key
   *          is the key which will inserted into the tree
   */
  public void insert(int key, Text entry) {

    if (key < 0) {
      System.err.println("Please don 't insert negative keys.");
    } else {

      // Proof if the root is empty
      if (rootOfTheTree == null) {
        this.rootOfTheTree = new BinaryNode(key);
        rootOfTheTree.setFather(null);
        rootOfTheTree.setIncomingDirection(null);
        // rootOfTheTree.createCircle(lang,key, 700, 40, 374);
        rootOfTheTree.createCircle(lang, key, 700, 120, 680, circ);
        entry.show();
        info = lang.newText(new Coordinates(50, 40),
            "Schlüssel " + Integer.toString(key) + " eingefügt.",
            "Einfügetext", null, textProps);
        info.hide(new MsTiming(600));
      }

      else {
        // If the root is greater than the current key (node)
        // then the current key or node belongs to left side otherwise to the
        // right side.
        if (rootOfTheTree.getKey() > key) {
          nextInsert(rootOfTheTree, key, null, TreeDirection.LEFT, 700, 120,
              680, entry);
        } else
          nextInsert(rootOfTheTree, key, null, TreeDirection.RIGHT, 700, 120,
              680, entry);
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
  protected void nextInsert(BinaryNode currentNode, int key, BinaryNode father,
      TreeDirection direction, int x, int y, int w, Text entry) {
    int halfW = w >> 1;

    if (currentNode == null) {

      BinaryNode child = new BinaryNode(key);
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
      entry.show();
      info = lang.newText(new Coordinates(50, 40),
          "Schlüssel " + Integer.toString(key) + " eingefügt.",
          "insert() Text", null, textProps);
      // info.hide(new MsTiming(600));
      lang.nextStep();
      info.hide();
    } else {

      if (currentNode.getKey() > key) {
        nextInsert(currentNode.getLeft(), key, currentNode, TreeDirection.LEFT,
            x - halfW, y + 80, halfW, entry);
      } else {
        nextInsert(currentNode.getRight(), key, currentNode,
            TreeDirection.RIGHT, x + halfW, y + 80, halfW, entry);
      }
    }
  }

  /**
   * This is a method to find a key in the tree, based on depth first search
   * 
   * @param key
   *          is the node which we want to search in the tree
   * @return true if we found our Node in the tree otherwise false
   */
  public boolean search(int key) {
    boolean found = false;
    // System.out.println(rootOfTheTree.getKey());

    if (rootOfTheTree.getKey() == key) {
      found = true;
      System.out.println("You have found me! I'm the root of the tree");
    } else {
      if (rootOfTheTree.getLeft() != null) {// && found == false){
        nextSearch(key, rootOfTheTree, false);
      }
      if (rootOfTheTree.getRight() != null) {// && found == false){
        nextSearch(key, rootOfTheTree, false);
      }
    }
    return found;
  }

  /**
   * Effective search with recursion
   * 
   * @param key
   *          which has to be searched
   * @param currentNode
   *          on which we operate
   * @param myFound
   *          give us a boolean value for each node of the route to the key.
   * @return true: key is found otherwise false
   */
  protected boolean nextSearch(int key, BinaryNode currentNode, boolean myFound) {
    boolean found = myFound;
    if (currentNode.getKey() == key && found == false) {
      found = true;
      System.out.println("Gratuliere, Sie haben den Schlüssel"
          + currentNode.getKey() + " gefunden.");
    }

    if (key < currentNode.getKey() && currentNode.getLeft() != null
        && found == false) {
      nextSearch(key, currentNode.getLeft(), false);
    }

    if (key > currentNode.getKey() && currentNode.getRight() != null
        && found == false) {
      nextSearch(key, currentNode.getRight(), false);
    }

    return found;
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
   * @param currentNode
   *          if found the key, the key will be the currentNode
   * @param father
   *          is the father of the currentNode
   * @param direction
   *          from which direction the currentNode comes.
   * @return the node which will replace the key.
   */
  protected BinaryNode nextDelete(int key, BinaryNode currentNode,
      BinaryNode father, TreeDirection direction) {
    BinaryNode replace = null;

    if (currentNode.getKey() == key) {

      if (currentNode.getLeft() != null) {
        replace = findMaxAndRemove(currentNode.getLeft());

      } else if (currentNode.getRight() != null) {
        System.out.println("currentNode.getRight:  "
            + currentNode.getRight().getKey());
        replace = findMinAndRemove(currentNode.getRight());

      }
      if (replace != null) {

        // System.out.println("replace Node: "+replace.getKey());
        // if (replace.getLeft() != null) {
        // System.out.println("replace.getLeft(): "+replace.getLeft().getKey());
        // }
        // if (replace.getRight() != null){
        // System.out.println("replace.getRight(): "+replace.getRight().getKey());
        // }
        //
        // System.out.println("replace: "+replace.getKey()+" replace.getFather(): "+replace.getFather().getKey());
        // if (replace.getFather().getLeft() != null) {
        // System.out.println("replace.getFather.getLeft:"+replace.getFather().getLeft().getKey());
        // }

        // verschieben der Knoten
        translate(replace, currentNode);

        if (replace == currentNode.getLeft()) {
          replace.setRight(currentNode.getRight());
          // translate(replace.getLeft(), replace);
        } else if (replace == currentNode.getRight()) {
          currentNode.setRight(null);
        } else {
          // System.out.println("innerer Knoten, wobei links und rechts nicht replace steht");
          replace.setLeft(currentNode.getLeft());
          replace.setRight(currentNode.getRight());
        }
        if (currentNode == rootOfTheTree) {
          rootOfTheTree = replace;
        }
        return replace;
      } else {
        // delete leafs in Animal Language
        lang.nextStep();
        info = lang.newText(new Coordinates(50, 40),
            "Schlüssel " + Integer.toString(key) + " läschen.",
            "Blatt löschen", null, textProps);

        Text info1;
        info1 = lang.newText(new Coordinates(50, 60), Integer.toString(key)
            + " ist ein Blatt und kann einfach gelöscht werden.",
            "delete() Text", null, textProps);

        Circle leafNode = currentNode.getNodeCircle();
        leafNode.changeColor("fillcolor", Color.red, null, null);
        lang.nextStep();
        leafNode.hide();
        currentNode.getNodeText().hide();
        if (father != null) {
          System.out.println("Father: " + father.getKey());
          if (direction == TreeDirection.LEFT) {
            System.out.println("father.getLeftEdge:  "
                + father.getLeftEdge().getName());
            father.getLeftEdge().hide();
            father.leftEdge = null;
          } else {
            father.getRightEdge().hide();
            father.rightEdge = null;
          }
        }
        info.hide();
        info1.hide();

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
      } else if (currentNode.getRight() != null) {
        replace = nextDelete(key, currentNode.getRight(), currentNode,
            TreeDirection.RIGHT);
        currentNode.setRight(replace);
      }
      return currentNode;
    }
  }

  /**
   * translate method replaces the replace node with the delete node
   * 
   * @param repNode
   * @param toDelete
   * @return the replace node Circle with his new position
   */
  protected Circle translate(BinaryNode repNode, BinaryNode toDelete) {

    // lang.nextStep();
    info = lang.newText(new Coordinates(50, 40),
        "Schlüssel " + Integer.toString(toDelete.getKey()) + " löschen. ",
        "Löschtext", null, textProps);

    // SourceCodeProperties titleProps = new SourceCodeProperties();
    // titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("Monospaced",Font.BOLD, 12));
    // titleProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.green);

    SourceCode title = lang.newSourceCode(new Coordinates(50, 70),
        "sourceCode", null, titleProps);

    if (toDelete.getLeft() != null) {
      title.addCodeLine(Integer.toString(toDelete.getKey())
          + " ist ein innerer Knoten.", null, 0, null);
      title.addCodeLine(
          "Da er einen linken Teilbaum besitzt, wird der zu löschende Knoten",
          null, 0, null);
      title.addCodeLine(
          "mit dem größten Schlüssel aus dem linken Teilbaum ersetzt. ", null,
          0, null);
      title.addCodeLine("Der Knoten " + Integer.toString(repNode.getKey())
          + " wird die Position ", null, 0, null);
      title.addCodeLine("des zu löschenden Knotens einnehmen.", null, 0, null);
    } else if (toDelete.getLeft() == null && toDelete.getRight() != null) {
      title.addCodeLine(Integer.toString(toDelete.getKey())
          + " ist ein innerer Knoten.", null, 0, null);
      title
          .addCodeLine(
              "Da er nur einen rechten Teilbaum besitzt, wird der zu löschende Knoten",
              null, 0, null);
      title.addCodeLine(
          "mit dem kleinsten Schlüssel aus dem rechten Teilbaum ersetzt.",
          null, 0, null);
      title.addCodeLine("Der Knoten " + Integer.toString(repNode.getKey())
          + " wird die Position", null, 0, null);
      title.addCodeLine("des zu löschenden Knotens einnehmen.", null, 0, null);
    }

    lang.nextStep();
    toDelete.getNodeCircle().changeColor("fillcolor", Color.red, null, null);

    repNode.getNodeCircle().changeColor("fillcolor", Color.green, null, null);
    lang.nextStep();
    // position of repNode before translate
    int x = repNode.getXcoordinate();
    int y = repNode.getYcoordinate();
    TreeDirection oldRepDir = repNode.getIncomingDirection();
    // BinaryNode oldRepNode = repNode;
    // BinaryNode oldLeftChild = repNode.getLeft();
    // BinaryNode oldRightChild = repNode.getRight();
    // boolean repNodeIsLeaf = repNode.isLeaf();
    // BinaryNode oldFather = repNode.getFather();

    // new position for repNode
    int newX = toDelete.getXcoordinate();
    int newY = toDelete.getYcoordinate();
    int newWidth = toDelete.getWidth();
    BinaryNode newFather = toDelete.getFather();

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
    BinaryNode repNodeFather = repNode.getFather();

    // Nachdem repNode zu seiner neuen Position gelangt ist, versteckt der Vater
    // seine Kanten
    if (repNode.getIncomingDirection() == TreeDirection.LEFT) {
      repNodeFather.getLeftEdge().hide();
      repNodeFather.leftEdge = null;
    } else {
      repNodeFather.getRightEdge().hide();
      repNodeFather.rightEdge = null;
    }

    toDelete.getNodeCircle().hide();
    toDelete.getNodeText().hide();
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
  protected void moveSubtree(BinaryNode rootOfSubtree) {

    // System.out.println("rootOfSubtree: "+rootOfSubtree.getKey());
    BinaryNode dockingNode = rootOfSubtree.getFather();
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

  /**
   * findMaxAndRemove method finds the max BinaryNode from currentNode and
   * returns and removes it from the left subtree.
   * 
   * @param currentNode
   *          is the Node which the search for max Node begins
   * @return the max Node of the left subtree.
   */
  protected BinaryNode findMaxAndRemove(BinaryNode currentNode) {

    if (currentNode.getRight() != null) {
      BinaryNode x = findMaxAndRemove(currentNode.getRight());
      // System.out.println("only currentNode: "+currentNode.getKey());
      // System.out.println("findMaxAndRemove, currentNode.getRight: "+currentNode.getRight().getKey());
      // System.out.println("x: "+x.getKey());
      if (currentNode.getRight() == x) {
        // System.out.println("x: "+x.getKey()+" x.getLeft: "+x.getLeft().getKey());
        // x.setLeftReplaceChild(x.getLeft());
        // x.setRightReplaceChild(x.getRight());

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
  protected BinaryNode findMinAndRemove(BinaryNode currentNode) {

    if (currentNode.getLeft() != null) {
      System.out.println("getLeft:  " + currentNode.getLeft().getKey());
      BinaryNode x = findMinAndRemove(currentNode.getLeft());
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
   * Internal Method to show the search
   * 
   * @param key
   *          to search
   * @param rootNode
   *          the search begins from root
   * @return true if node Circle was found otherwise false
   */
  private boolean visibleSearchInternal(int key, BinaryNode rootNode,
      Text search) {

    boolean found = false;

    Circle circle = rootNode.getNodeCircle();
    if (rootNode.getKey() == key) {

      found = true;

      search.hide();
      circle.changeColor("fillColor", Color.GREEN, null, null); // got

      TextProperties tp = new TextProperties();
      tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
          Font.PLAIN, 16));
      tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
      tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);
      Text end = lang.newText(new Coordinates(260, 40),
          "Congratulations, you have found the key: " + key,
          "Congratulations, you have found the key: " + key,
          new TicksTiming(70), tp);
      lang.nextStep();
      circle.changeColor("fillColor", Color.YELLOW, null, null);

      end.hide();
      // System.out.println("Congratulations, you have found the key:"
      // + rootNode.getKey());
    } else {
      circle.changeColor("fillColor", Color.RED, null, null);
      lang.nextStep();

      if (key < rootNode.getKey() && rootNode.getLeft() != null && !found) {
        found = visibleSearchInternal(key, rootNode.getLeft(), search);
      } else if (key > rootNode.getKey() && rootNode.getRight() != null
          && !found) {
        found = visibleSearchInternal(key, rootNode.getRight(), search);
      }
      search.hide();
      circle.changeColor("fillColor", Color.YELLOW, null, null);
    }
    return found;
  }

  /**
   * Public Method to show the search
   * 
   * @param key
   *          to search
   * @param rootNode
   *          the search begins from root
   * @return true if node Circle was found otherwise false
   */
  protected boolean showSearch(int key, BinaryNode rootNode) {

    lang.nextStep();

    Text search = lang.newText(new Coordinates(50, 40), "Suche Schlüssel "
        + Integer.toString(key) + "", "search() Text", null, textProps);

    boolean success = visibleSearchInternal(key, rootNode, search);
    if (!success) {
      search = lang.newText(new Coordinates(50, 40),
          "The key: " + Integer.toString(key) + " was not found",
          "notFound() Text", null, textProps);
      search.hide(new TicksTiming(100));
      TextProperties tp = new TextProperties();
      tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
          Font.PLAIN, 16));
      tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
      tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);

    }
    lang.nextStep();
    // search.hide();

    return success;
  }

  /**
   * getTreeDFS is a depth first search
   * 
   * @param getCNode
   *          is the node where the search begins
   */
  public void getTreeDFS(BinaryNode getCNode) {

    System.out.println(getCNode.getKey());
    if (getCNode.getLeftEdge() != null || getCNode.getRightEdge() != null) {
      System.out.println("Node: " + getCNode.getKey() + " has animal line: "
          + getCNode.getLeftEdge());
    }

    if (getCNode.getLeft() != null) {
      // if (getCNode.getLeftEdge() != null){
      // System.out.println("leftNode: "+getCNode.getKey()+" has animal leftNode: "+getCNode.getLeftEdge());
      // }
      getTreeDFS(getCNode.getLeft());
    }

    if (getCNode.getRight() != null) {
      // if (getCNode.getRightEdge() != null) {
      // System.out.println("leftNode: "+getCNode.getKey()+" has animal leftNode: "+getCNode.getRightEdge().getName());
      // }
      getTreeDFS(getCNode.getRight());

    }
  }

  /**
   * getTreeBFS is a breadth first search
   * 
   * @param getCNode
   *          is the node where the search begins
   */
  public void getTreeBFS(BinaryNode getCNode) {
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
    if (getCNode.getFather() != null)
      sb.append("\nNode: ").append(getCNode.getKey()).append(" has father: ")
          .append(getCNode.getFather().getKey());
    sb.append("\nXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n");

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

  public void myInit(SourceCodeProperties titleProps) {

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    Node upperLeft = new Coordinates(20, 30);
    Node lowerRight = new Coordinates(200, 60);
    Rect rect = lang.newRect(upperLeft, lowerRight, "BinaryTree", null,
        rectProps);

    // Only a test for Offset
    // Circle c = lang.newCircle(new Offset(100, 100, rect,
    // AnimalScript.DIRECTION_E), 20, "c", null);

    //
    // SourceCodeProperties titleProps = new SourceCodeProperties();
    // titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("Monospaced",Font.BOLD, 16));
    // titleProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.green);

    SourceCode title = lang.newSourceCode(new Coordinates(30, 20),
        "sourceCode", null, titleProps);

    title.addCodeLine("Binärer Suchbaum", null, 0, null);
    title.addCodeLine("", null, 0, null);
    title.addCodeLine(
        "Ein binärer Suchbaum ist ein Binärbaum. Dieser kann leer sein oder",
        null, 0, null);
    title
        .addCodeLine(
            "jeder Knoten enthält einen Schlüssel. Alle Knoten im linken Teilbaum eines Knotens K ",
            null, 0, null);
    title
        .addCodeLine(
            "enthalten Schlüssel, die kleiner sind als K. Alle Knoten im rechten Teilbaum enthalten  ",
            null, 0, null);
    title.addCodeLine("Schlüssel die größer sind als K. ", null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "Die Hauptoperationen des binären Suchbaums sind: Suche, Einfügen und Loeschen.",
            null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "Die Komplexität der Operationen, also die Zugriffskosten betragen maximal O(n), ",
            null, 0, null);
    title
        .addCodeLine(
            "wobei n die Anzahl der Knoten im Baum sind. Minimal, also im besten Fall, beträgt ",
            null, 0, null);
    title.addCodeLine("die Komplexität O(log(n)).", null, 0, null);

    lang.nextStep();
    title.hide();
    rect.hide();

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));

    info = lang.newText(new Coordinates(50, 20),
        "Aktuelle Aktivitäten im binären Suchbaum", "Info Text", null,
        textProps);
    Font f = new Font("Monospaced", Font.BOLD, 16);
    info.setFont(f, null, null);

  }

  private final static String DESCRIPTION  = "Ein binärer Suchbaum ist ein Binärbaum. Dieser kann leer sein oder\n"
                                               + "jeder Knoten enthält einen Schlüssel. Alle Knoten im linken Teilbaum eines Knotens K\n"
                                               + "enthalten Schlüssel, die kleiner sind als K. Alle Knoten im rechten Teilbaum enthalten\n"
                                               + "Schlüssel die größer sind als K.\n"
                                               + "\n"
                                               + "Die Hauptoperationen des binären Suchbaums sind: Suche, Einfügen und Loeschen.\n"
                                               + "\n"
                                               + "Die Komplexität der Operationen, also die Zugriffskosten betragen maximal O(n),\n"
                                               + "wobei n die Anzahl der Knoten im Baum sind. Minimal, also im besten Fall, beträgt\n"
                                               + "die Komplexität O(log(n)).\n"
                                               + "";

  private final static String CODE_EXAMPLE = "//Notice: This is just an example and it is not completely!\n"
                                               + "public class BinarySearchTree { \n"
                                               + "\n"
                                               + "	public void insert(int insertKey, BinaryNode currentNode, BinaryNode father, TreeDirection direction) { \n"
                                               + "		if (currentNode == null) { \n"
                                               + ""
                                               + "			BinaryNode child = new BinaryNode(insertKey); \n"
                                               + "	 	 	child.setFather(father); \n"
                                               + ""
                                               + "			if (direction == TreeDirection.LEFT) \n"
                                               + "				father.setLeft(child); \n"
                                               + "		 	else \n"
                                               + "			 	father.setRight(child); \n"
                                               + "	 	} \n"
                                               + "	 	else {\n"
                                               + "		 	if (currentNode.getKey()> insertKey) \n"
                                               + "			 	insert(insertKey, currentNode.getLeft(), currentNode, TreeDirection.LEFT);\n"
                                               + "		 	else\n"
                                               + "			 	insert(insertKey,currentNode.getRight(), currentNode, TreeDirection.RIGHT);\n"
                                               + "  	 	}\n"
                                               + "	}"
                                               + " \n"
                                               + "	public boolean search(int key) { \n"
                                               + "		boolean found= false;\n"
                                               + " \n"
                                               + "		if (rootOfTheTree.getKey() == key)  \n"
                                               + "			found = true; \n "
                                               + "		else { \n"
                                               + "			if (rootOfTheTree.getLeft() != null && found == false) \n"
                                               + "				nextSearch(key,rootOfTheTree, false); \n"
                                               + "			if (rootOfTheTree.getRight() != null && found == false)\n"
                                               + "				nextSearch(key,rootOfTheTree, false); \n"
                                               + "		} \n"
                                               + "		return found; \n"
                                               + "	} \n"
                                               + "\n"
                                               + "	private boolean nextSearch(int key, Knoten currentNode, boolean found) {//remains private } \n"
                                               + "\n"
                                               + "	public void delete(int key) { \n"
                                               + "		//like search: if found then delete else key isn't in the tree. }\n"
                                               + "}";

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
        insert(keys[i], entry);
        // entry.show();
        y++;
      }
    }

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Bin\u00e4rer Suchbaum";
  }

  @Override
  public String getAnimationAuthor() {
    return "Ioannis Tsigaridas";
  }

  @Override
  public String getCodeExample() {
    return CODE_EXAMPLE;
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
    return "Binary Search Tree";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("Binary Search Tree", "Ioannis Tsigaridas", 800,
        640);
    lang.setStepMode(true);

    // arrayProps = null;
    // circ = null;
    // info = null;
    keys = null;
    // nodeTree = null;
    // q = null;
    rootOfTheTree = null;
    textProps = null;
    titleProps = null;
  }
}
