package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.tsigaridas.Schlange;
import generators.helpers.tsigaridas.TourNode;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
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
import algoanim.util.Node;

/**
 * 117, 22, 33, 7, 3, 2, 19, 31
 * 
 * @author Ioannis Tsigaridas
 */
public class TournamentSort implements Generator {
  private TourNode             rootOfTheTree;
  private TextProperties       textProps;
  // private ArrayProperties arrayProps;
  private SourceCodeProperties titleProps;
  private CircleProperties     circ;
  private TourNode             nodeTree[];
  private int                  keys[];

  public enum TreeDirection {
    LEFT, RIGHT
  }

  private Schlange<TourNode> q = new Schlange<TourNode>();
  private Text               info;
  private Language           lang;

  public TournamentSort() {
  }

  /**
   * The given keys will be sorted by the tournament algorithm.
   * 
   * @param keys
   *          to be sorted
   */
  public void tournamentSort(int keys[], CircleProperties circ) {

    if (keys.length > 1) {

      // ArrayProperties arrayProps = new ArrayProperties();
      // arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
      // Color.black);
      // arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
      // Color.white);
      // arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      // arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
      // Color.orange);
      // // arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
      // Color.white);
      // // IntArray ia = lang.newIntArray(new Coordinates(20,75), keys,
      // "array", null, arrayProps);

      textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.BOLD, 14));
      info = lang.newText(new Coordinates(50, 60), "Sortierte Schlüssel:",
          "SortierteSchlüssel", null, textProps);

      createTree(keys, circ);
      int i = 0;
      while (i < keys.length - 1) {
        int pos = searchElement(nodeTree);
        keys[i] = nodeTree[pos].getKey();

        info = lang.newText(new Coordinates(50 + (35 * i), 75), ""
            + nodeTree[pos].getKey() + ",", "Info Text", null, textProps);

        deleteElement(pos);
        rePlay(nodeTree, pos);
        i++;
      }
      keys[keys.length - 1] = nodeTree[0].getKey();
      info = lang.newText(new Coordinates(50 + (35 * (keys.length - 1)), 75),
          "" + nodeTree[0].getKey() + "", "Info Text", null, textProps);
    } else
      System.err
          .println("Tournament exists with two or more keys. Please try again with more keys.");

    // Test
    // for (int k=0; k < keys.length; k++) {
    // System.out.println("key["+k+"]: "+keys[k]);
    // }

    // Test
    // for (int k = 0; k < nodeTree.length; k++) {
    // if (nodeTree[k] != null) {
    // System.out.println("nodeTree["+k+"]: "+nodeTree[k].getKey()+"
    // NodeInfo: "+nodeTree[k]+" " +
    // " getNodeText: "+nodeTree[k].getNodeText().getText()+" TextName:
    // "+nodeTree[k].getNodeText().getName());
    // }
    // else System.out.println("nodeTree["+k+"]: null");
    // }
  }

  /**
   * Lösche das Blatt
   * 
   * @param pos
   */
  private void deleteElement(int pos) {
    TourNode elementForDelete = nodeTree[pos];

    // elementForDelete.getNodeCircle().changeColor("fillcolor", Color.red,
    // null, null);
    lang.nextStep();
    elementForDelete.getNodeText().hide();
    // elementForDelete.getNodeCircle().changeColor("fillcolor",
    // Color.yellow, null, null);
    nodeTree[pos] = null;
  }

  /**
   * Suche das kleinste Blatt ausgehen von der Wurzel bis zu den Blättern
   * 
   * @param nodeTree
   * @return
   */
  protected int searchElement(TourNode nodeTree[]) {

    // if (nodeTree[0] != null) {
    int element = nodeTree[0].getKey();
    // System.out.println("element "+element);
    int position = 0;
    for (int i = 0; i < nodeTree.length / 2; i++) {
      if (nodeTree[2 * i + 1] != null
          && nodeTree[2 * i + 1].getKey() == element
          && nodeTree[2 * i + 1].isLeaf()) {
        position = 2 * i + 1;
      }
      if (nodeTree[2 * i + 2] != null
          && nodeTree[2 * i + 2].getKey() == element
          && nodeTree[2 * i + 2].isLeaf()) {
        position = 2 * i + 2;
      }
    }
    return position;
    // }
    // else return 0;
  }

  /**
   * Methode passt nach dem Löschen an die neue Situation an
   * 
   * @param nodeTree
   * @param pos
   */
  protected void rePlay(TourNode nodeTree[], int pos) {
    int fatherPos = (pos - 1) / 2;

    for (int i = fatherPos; i > 0; i = (i - 1) / 2) {

      replayForI(nodeTree, i);
    }
    replayForI(nodeTree, 0);
  }

  private void replayForI(TourNode[] nodeTree, int i) {
    TourNode father;
    TourNode leftSon;
    TourNode rightSon;
    father = nodeTree[i];
    leftSon = nodeTree[2 * i + 1];
    rightSon = nodeTree[2 * i + 2];

    lang.nextStep("replay with:" + father.getKey() + " infoNode: " + father
        + " nodeText" + father.getNodeText().getName());
    // if (father != null)
    // System.out.println("father: "+father.getKey());
    // else System.out.println("father: null");
    // System.out.println("pos father: "+i);
    // if (leftSon != null)
    // System.out.println("leftSon: "+leftSon.getKey());
    // else System.out.println("leftSon: null");
    // System.out.println("pos left: "+(2*i+1));
    // if (rightSon != null)
    // System.out.println("rightSon: "+rightSon.getKey());
    // else System.out.println("rightSon: null");
    // System.out.println("pos right: "+(2*i+2));
    // System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

    int x = father.getXcoordinate();
    int y = father.getYcoordinate();
    int width = father.getWidth();

    if (leftSon == null && rightSon != null) {
      father.getNodeText().changeColor("color", Color.red, null, null);
      rightSon.getNodeText().changeColor("color", Color.red, null, null);
      lang.nextStep();
      TourNode newNode = new TourNode(rightSon.getKey());
      nodeTree[i] = newNode;

      father.getNodeCircle().hide();
      father.getNodeText().hide();
      rightSon.getNodeText().changeColor("color", Color.black, null, null);

      newNode.createCircle(lang, newNode.getKey(), x, y, width, circ);
      newNode.getNodeCircle().show();
      newNode.getNodeText().show();
    } else if (leftSon != null && rightSon == null) {
      father.getNodeText().changeColor("color", Color.red, null, null);
      leftSon.getNodeText().changeColor("color", Color.red, null, null);
      lang.nextStep();
      TourNode newNode = new TourNode(leftSon.getKey());
      nodeTree[i] = newNode;

      father.getNodeCircle().hide();
      father.getNodeText().hide();
      leftSon.getNodeText().changeColor("color", Color.black, null, null);

      newNode.createCircle(lang, newNode.getKey(), x, y, width, circ);
      newNode.getNodeCircle().show();
      newNode.getNodeText().show();
    } else if (leftSon != null && rightSon != null) {
      father.getNodeText().changeColor("color", Color.red, null, null);
      rightSon.getNodeText().changeColor("color", Color.red, null, null);
      leftSon.getNodeText().changeColor("color", Color.red, null, null);
      lang.nextStep();
      if (leftSon.getKey() < rightSon.getKey()) {
        nodeTree[i] = new TourNode(leftSon.getKey());
      } else {
        nodeTree[i] = new TourNode(rightSon.getKey());
      }
      father.getNodeCircle().hide();
      father.getNodeText().hide();
      rightSon.getNodeText().changeColor("color", Color.black, null, null);
      leftSon.getNodeText().changeColor("color", Color.black, null, null);

      nodeTree[i].createCircle(lang, nodeTree[i].getKey(), x, y, width, circ);
      nodeTree[i].getNodeCircle().show();
      nodeTree[i].getNodeText().show();
    } else if (leftSon == null && rightSon == null) {
      nodeTree[i].getNodeText().changeColor("color", Color.red, null, null);
      lang.nextStep();
      nodeTree[i].getNodeText().hide();
      // nodeTree[i].getNodeText().hide();
      nodeTree[i] = null;
    }
  }

  /**
   * Creates the tree with the given keys.
   * 
   * @param keys
   */
  protected void createTree(int keys[], CircleProperties circ) {

    TourNode leftPlayer, rightPlayer;
    int height = getHeight(keys.length);
    // System.out.println("Heigt of the Tree: " + height);
    int numberOfNodes = (int) Math.pow(2, getHeight(keys.length)) - 1;
    int positionOfFirstLeaf = (int) Math.pow(2, getHeight(keys.length) - 1) - 1;
    int levelNodeLength = keys.length;

    // The whole Tree with nodes which can be null
    nodeTree = new TourNode[numberOfNodes];
    for (int i = 0; i < keys.length; i++) {
      nodeTree[i + positionOfFirstLeaf] = new TourNode(keys[i]);
    }
    // System.out.println("positionOfLastLeaf: "+positionOfLastLeaf+" and
    // his value: "+nodeTree[positionOfLastLeaf].getKey());
    for (int h = height; h > 0; h--) {
      int level = h - 1;
      int firstPos = (int) Math.pow(2, level) - 1;
      int lastPos = firstPos + (keys.length - 1);
      if (h < height) {
        if (levelNodeLength % 2 == 0) {
          levelNodeLength = levelNodeLength / 2;
          lastPos = levelNodeLength - 1 + firstPos;
        } else {
          levelNodeLength = (levelNodeLength / 2) + 1;
          lastPos = levelNodeLength - 1 + firstPos;
        }
      }
      if (firstPos != lastPos) {
        // System.out.println("firstPos: "+firstPos+" lastPos:
        // "+lastPos);
        for (int i = firstPos; i <= lastPos; i = i + 2) {
          leftPlayer = nodeTree[i];
          rightPlayer = nodeTree[i + 1];

          if (leftPlayer != null && rightPlayer == null) {
            nodeTree[i / 2] = new TourNode(leftPlayer.getKey());
          } else {
            if (leftPlayer.getKey() < rightPlayer.getKey()) {
              nodeTree[i / 2] = new TourNode(leftPlayer.getKey());
            } else {
              nodeTree[i / 2] = new TourNode(rightPlayer.getKey());
            }
          }
        }
      }
    }
    // Testing the nodeTree
    // for (int k = 0; k < nodeTree.length; k++) {
    // if (nodeTree[k] != null)
    // System.out.println("node["+k+"] :"+nodeTree[k].getKey());
    // }
    createTreeUnvisible(nodeTree, circ);
    effectiveShowingTree(nodeTree, keys);
  }

  /**
   * This method shows the tree in a specific order, first left and then right
   * 
   * @param tree
   * @param keys
   * @param leftTree
   */
  private void effectiveShowingTree(TourNode tree[], int keys[]) {

    TourNode leftPlayer, rightPlayer, fatherPlayer;
    Text infoWinner;
    int height = getHeight(keys.length);
    int firstPos;
    int lastPos;
    // System.out.println("positionOfLastLeaf: "+positionOfLastLeaf+" and
    // his value: "+nodeTree[positionOfLastLeaf].getKey());
    int level = height - 1;
    firstPos = (int) Math.pow(2, level) - 1;
    lastPos = (int) (2 * (Math.pow(2, level) - 1));

    // At the beginning the level is maxLevel an this is the visualization
    // of this level
    for (int i = firstPos; i <= lastPos; i = i + 2) {
      leftPlayer = tree[i];
      rightPlayer = tree[i + 1];

      if (leftPlayer != null && rightPlayer == null) {
        lang.nextStep();
        leftPlayer.getNodeCircle().show();
        leftPlayer.getNodeText().show();
      } else {
        if (leftPlayer != null && rightPlayer != null) {
          lang.nextStep();
          info = lang.newText(
              new Coordinates(300, 20),
              "Spiel " + leftPlayer.getKey() + " mit " + rightPlayer.getKey(),
              "Vergleiche " + leftPlayer.getKey() + " mit "
                  + rightPlayer.getKey(), null, textProps);
          if (leftPlayer.getNodeCircle() == null)
            System.err
                .println("@" + i + ", leftPlayer: " + leftPlayer.getKey());
          leftPlayer.getNodeCircle().show();
          leftPlayer.getNodeText().show();
          rightPlayer.getNodeCircle().show();
          rightPlayer.getNodeText().show();
        }
      }
      // Winner(father) with his edges is the smallest of the both Nodes
      // left- and rightPlayer
      fatherPlayer = tree[i / 2];
      if (fatherPlayer != null) {
        lang.nextStep();
        if (leftPlayer != null && rightPlayer != null)
          infoWinner = lang.newText(new Coordinates(470, 20), "Gewinner: "
              + fatherPlayer.getKey(), "Gewinner: " + fatherPlayer.getKey(),
              null, textProps);
        else
          infoWinner = lang.newText(new Coordinates(470, 20), "Freilos: "
              + fatherPlayer.getKey(), "Freilos: " + fatherPlayer.getKey(),
              null, textProps);
        fatherPlayer.getNodeCircle().show();
        fatherPlayer.getNodeText().show();
        // Edges of the winner
        if (fatherPlayer.getLeft() != null)
          fatherPlayer.getLeftEdge().show();
        if (fatherPlayer.getRight() != null)
          fatherPlayer.getRightEdge().show();
        lang.nextStep();
        info.hide();
        infoWinner.hide();
      }
    }
    // here is the visualization of the maximum level-2 of the tree
    for (int h = height - 2; h > 0; h = h - 1) {
      level = h - 1;
      firstPos = (int) Math.pow(2, level) - 1;
      lastPos = (int) (2 * (Math.pow(2, level) - 1));

      if (firstPos != lastPos) {
        for (int i = firstPos; i <= lastPos; i = i + 2) {

          leftPlayer = tree[i];
          rightPlayer = tree[i + 1];

          if (leftPlayer != null && rightPlayer == null) {
            lang.nextStep();
            if (leftPlayer.getLeft() != null && leftPlayer.getRight() != null) {
              info = lang.newText(new Coordinates(300, 20), "Spiel "
                  + leftPlayer.getLeft().getKey() + " mit "
                  + leftPlayer.getRight().getKey(), "Vergleiche "
                  + leftPlayer.getLeft().getKey() + " mit "
                  + leftPlayer.getRight().getKey(), null, textProps);
            }

            leftPlayer.getNodeCircle().show();
            leftPlayer.getNodeText().show();
            // Edges of leftPlayer
            if (leftPlayer.getLeft() != null)
              leftPlayer.getLeftEdge().show();
            if (leftPlayer.getRight() != null)
              leftPlayer.getRightEdge().show();

            infoWinner = lang.newText(new Coordinates(470, 20), "Gewinner: "
                + leftPlayer.getKey(), "Gewinner: " + leftPlayer.getKey(),
                null, textProps);
            lang.nextStep();
            info.hide();
            infoWinner.hide();
          } else {
            if (leftPlayer != null && rightPlayer != null) {
              lang.nextStep();
              // Show leftPlayer
              if (leftPlayer.getLeft() != null && leftPlayer.getRight() != null) {
                info = lang.newText(new Coordinates(300, 20), "Spiel "
                    + leftPlayer.getLeft().getKey() + " mit "
                    + leftPlayer.getRight().getKey(), "Vergleiche "
                    + leftPlayer.getLeft().getKey() + " mit "
                    + leftPlayer.getRight().getKey(), null, textProps);
              }
              leftPlayer.getNodeCircle().show();
              leftPlayer.getNodeText().show();
              // Edges of leftPlayer
              if (leftPlayer.getLeft() != null)
                leftPlayer.getLeftEdge().show();
              if (leftPlayer.getRight() != null)
                leftPlayer.getRightEdge().show();
              infoWinner = lang.newText(new Coordinates(470, 20), "Gewinner: "
                  + leftPlayer.getKey(), "Gewinner: " + leftPlayer.getKey(),
                  null, textProps);
              lang.nextStep();
              info.hide();
              infoWinner.hide();

              lang.nextStep();
              // Show rightPlayer
              if (rightPlayer.getLeft() != null
                  && rightPlayer.getRight() != null) {
                info = lang.newText(new Coordinates(300, 20), "Spiel "
                    + rightPlayer.getLeft().getKey() + " mit "
                    + rightPlayer.getRight().getKey(), "Vergleiche "
                    + rightPlayer.getLeft().getKey() + " mit "
                    + rightPlayer.getRight().getKey(), null, textProps);
              }
              rightPlayer.getNodeCircle().show();
              rightPlayer.getNodeText().show();
              // Edges of rightPlayer
              if (rightPlayer.getLeft() != null)
                rightPlayer.getLeftEdge().show();
              if (rightPlayer.getRight() != null)
                rightPlayer.getRightEdge().show();
              if (rightPlayer.getLeft() != null
                  && rightPlayer.getRight() != null)
                infoWinner = lang.newText(new Coordinates(470, 20),
                    "Gewinner: " + rightPlayer.getKey(), "Gewinner: "
                        + rightPlayer.getKey(), null, textProps);
              else
                infoWinner = lang.newText(new Coordinates(470, 20), "Freilos: "
                    + rightPlayer.getKey(), "Freilos: " + rightPlayer.getKey(),
                    null, textProps);
              lang.nextStep();
              info.hide();
              infoWinner.hide();
            }
          }
        }
      }
      // Here is the Visualization of the root and his edges.
      else {
        TourNode root = nodeTree[0];
        // lang.nextStep();
        info = lang.newText(new Coordinates(300, 20), "Spiel "
            + root.getLeft().getKey() + " mit " + root.getRight().getKey(),
            "Vergleiche " + root.getLeft().getKey() + " mit "
                + root.getRight().getKey(), null, textProps);
        lang.nextStep();
        root.getNodeCircle().show();
        root.getNodeText().show();
        root.getLeftEdge().show();
        root.getRightEdge().show();
        rootOfTheTree = root;
        infoWinner = lang.newText(new Coordinates(470, 20),
            "Gewinner: " + root.getKey(), "Gewinner: " + root.getKey(), null,
            textProps);
        lang.nextStep();
        info.hide();
        infoWinner.hide();
      }
    }
  }

  /**
   * This Method creates the tree in a binary structure and creates the animal
   * script for visualization the tree.
   * 
   * @param tree
   *          with the keys
   */
  protected void createTreeUnvisible(TourNode tree[], CircleProperties circ) {
    if (tree.length >= 0) {
      int x, y, width, i;
      x = 700;
      y = 140;
      width = 680;
      i = 0;
      // Create the root
      if (rootOfTheTree == null) {
        rootOfTheTree = tree[0];
        rootOfTheTree.createCircle(lang, tree[i].getKey(), x, y, width, circ);
      }
      nextCreate(tree, rootOfTheTree, x, y, width, i, circ);
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
  protected void nextCreate(TourNode nodeTree[], TourNode currentNode,
      int newX, int newY, int newWidth, int i, CircleProperties circ) {
    int halfW = newWidth >> 1;

    boolean createRight = false;
    if (i <= (nodeTree.length / 2) - 1) {

      if (currentNode.getLeft() == null) {
        TourNode leftChild = nodeTree[2 * i + 1];
        currentNode.setLeft(leftChild);
        leftChild.setFather(currentNode);
        leftChild.setIncomingDirection(TreeDirection.LEFT);
        leftChild.createCircle(lang, nodeTree[2 * i + 1].getKey(),
            newX - halfW, newY + 80, halfW, circ);
        currentNode.setLeftEdge(lang, leftChild, leftChild.getNodeCircle(),
            currentNode.getXcoordinate(), currentNode.getYcoordinate());
      } else {
        if ((2 * i) + 2 != nodeTree.length) {
          TourNode rightChild = nodeTree[2 * i + 2];
          if (rightChild != null) {
            currentNode.setRight(rightChild);
            rightChild.setFather(currentNode);
            rightChild.setIncomingDirection(TreeDirection.RIGHT);
            rightChild.createCircle(lang, nodeTree[2 * i + 2].getKey(), newX
                + halfW, newY + 80, halfW, circ);
            currentNode.setRightEdge(lang, rightChild,
                rightChild.getNodeCircle(), currentNode.getXcoordinate(),
                currentNode.getYcoordinate());
          }
        }
        createRight = true;
      }
      if (createRight == false) {
        nextCreate(nodeTree, currentNode, newX, newY, newWidth, i, circ);
      } else {
        if (currentNode.getLeft() != null) {
          nextCreate(nodeTree, currentNode.getLeft(), newX - halfW, newY + 80,
              halfW, 2 * i + 1, circ);
        }
        if (currentNode.getRight() != null) {
          nextCreate(nodeTree, currentNode.getRight(), newX + halfW, newY + 80,
              halfW, 2 * i + 2, circ);
        }
      }
    }
  }

  /**
   * Method returns the height of the tree
   * 
   * @param numberOfLeaves
   * @return the height
   */
  private int getHeight(int numberOfLeaves) {
    return getLevel(numberOfLeaves) + 1;
  }

  /**
   * Method returns the maximum level
   * 
   * @param numberOfLeaves
   * @return maximum level
   */
  private int getLevel(int numberOfLeaves) {
    int level;
    int i = 0;

    while (numberOfLeaves > Math.pow(2, i)) {
      i++;
    }
    level = i;
    return level;
  }

  /**
   * getTreeDFS is a depth first search
   * 
   * @param getCNode
   *          is the node where the search begins
   */
  public void getTreeDFS(TourNode getCNode) {

    System.out.println(getCNode.getKey());

    if (getCNode.getLeft() != null) {
      getTreeDFS(getCNode.getLeft());
    }
    if (getCNode.getRight() != null) {
      getTreeDFS(getCNode.getRight());
    }
  }

  public void getBaumBFS(TourNode aktuellerKnoten) {
    System.out.println(aktuellerKnoten.getKey());
    if (aktuellerKnoten.getLeft() != null) {
      q.push(aktuellerKnoten.getLeft());
    }
    if (aktuellerKnoten.getRight() != null) {
      q.push(aktuellerKnoten.getRight());
    }
    if (!q.isEmpty()) {
      getBaumBFS(q.pop());
    }
  }

  private final static String DESCRIPTION = " Tournament Sort\n"
                                              + "\n"
                                              + "Tournament Sort ist Sortierverfahren, dass auf dem KO-Prinzip basiert.\n"
                                              + "Der Spieler mit der kleinsten Nummer gewinnt. \n"
                                              + "Der Algorithmus lässt sich in drei Phasen einteilen:\n"
                                              + "\n"
                                              + "Phase 1: Ein Turnier wird gespielt und als ein binärer Baum dargestellt.\n"
                                              + "\n"
                                              + "Phase 2: wird n-1 mal ausgeführt. Dabei werden folgende Schritte gemacht\n"
                                              + "   Schritt 2.1: Das Element an der Wurzel wird ausgegeben.\n"
                                              + "   Schritt 2.2: Dann wird abgestiegen von der Wurzel bis zum Blatt,\n"
                                              + "                welches das gleiche Element besitzt wie die Wurzel und wird gelöscht\n"
                                              + "   Schritt 2.3: Jetzt wird der gleiche Pfad hochgestiegen und dabei neu gespielt."
                                              + "\n"
                                              + "Phase 3: Das letzte Element der Wurzel wird ausgegeben"
                                              + "";

  @Override
  public String generate(AnimationPropertiesContainer properties,
      Hashtable<String, Object> primitives) {
    keys = (int[]) primitives.get("keys");
    for (int val : keys)
      System.err.println(val + "...");
    // SourceCodeProperties sourceProps = (SourceCodeProperties) properties
    // .getPropertiesByName("sourceCode");
    titleProps = (SourceCodeProperties) properties
        .getPropertiesByName("titleProps");
    circ = (CircleProperties) properties.getPropertiesByName("circ");
//    ArrayProperties arrayProps = (ArrayProperties) properties
//        .getPropertiesByName("arrayProps");

    // CircleProperties circ =
    // (CircleProperties)properties.getPropertiesByName("circ");

    myInit(titleProps);

    // SourceCodeProperties sourceProps = new SourceCodeProperties();
    // sourceProps.setName("sourceCode");
    // sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.RED);
    // sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("Monospaced",Font.BOLD, 14));
    //
    // properties.add(sourceProps);

    this.tournamentSort(keys, circ);

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Tournament Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Ioannis Tsigaridas";
  }

  @Override
  public String getCodeExample() {
    return "No example yet";
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
    return "Tournament Sort";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("Tournament Sort", "IT", 800, 640);
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

  public void myInit(SourceCodeProperties titleProps) {

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    Node upperLeft = new Coordinates(20, 30);
    Node lowerRight = new Coordinates(185, 60);
    Rect rect = lang.newRect(upperLeft, lowerRight, "BinaryTree", null,
        rectProps);

    // SourceCodeProperties titleProps = new SourceCodeProperties();
    // titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.BOLD, 16));
    // titleProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.green);

    SourceCode title = lang.newSourceCode(new Coordinates(30, 20),
        "sourceCode", null, titleProps);

    title.addCodeLine("Tournament Sort", null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "Tournament Sort ist Sortierverfahren, dass auf dem KO-Prinzip basiert.",
            null, 0, null);
    title.addCodeLine("Der Spieler mit der kleinsten Nummer gewinnt.", null, 0,
        null);
    title.addCodeLine("Der Algorithmus lässt sich in drei Phasen einteilen:",
        null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "Phase 1: Ein Turnier wird gespielt und als ein binärer Baum dargestellt.",
            null, 0, null);
    title.addCodeLine("", null, 0, null);
    title
        .addCodeLine(
            "Phase 2: wird n-1 mal ausgeführt. Dabei werden folgende Schritte gemacht",
            null, 0, null);
    title.addCodeLine(
        "   Schritt 2.1: Das Element an der Wurzel wird ausgegeben.", null, 0,
        null);
    title.addCodeLine(
        "   Schritt 2.2: Dann wird abgestiegen von der Wurzel bis zum Blatt, ",
        null, 0, null);
    title
        .addCodeLine(
            "				   welches das gleiche Element besitzt wie die Wurzel und wird gelöscht.",
            null, 0, null);
    title
        .addCodeLine(
            "   Schritt 2.3: Jetzt wird der gleiche Pfad hochgestiegen und dabei neu gespielt.",
            null, 0, null);
    title.addCodeLine("", null, 0, null);
    title.addCodeLine(
        "Phase 3: Das letzte Element der Wurzel wird ausgegeben.", null, 0,
        null);
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
}
