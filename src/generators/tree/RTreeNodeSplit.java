package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.Point;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class RTreeNodeSplit implements Generator {
  private Language             lang;
  private ArrayProperties      NodeStyle;
  private SourceCodeProperties pseudoCode;
  private int[][]              NodeEntries2D;
  private Color                SelectedEntry;

  private int                  currentlySelectedEntry = -1;

  Text                         title;
  Text                         subtitle;
  Text[]                       coordinatesText;
  Text                         infoMinEntries;
  Text                         infoMaxEntries;
  Text                         infoMaxD;
  Text                         infoMinIncrease;
  Text                         infoMBRNode1;
  Text                         infoMBRNode2;
  Text                         info1;
  Text                         info2;
  Text                         infoNode1;
  Text                         infoNode2;

  int[][]                      node1;
  int[][]                      node2;
  int                          node1Idx               = 1;
  int                          node2Idx               = 1;

  StringArray                  iNode;
  StringArray                  iNode1;
  StringArray                  iNode2;
  SourceCode                   sc;
  SourceCode                   PickSeedsSC;
  SourceCode                   PickNextSC;

  public void init() {
    lang = new AnimalScript("RTree Node Spliting", "Dimitar Goshev", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    SelectedEntry = (Color) primitives.get("SelectedEntry");
    pseudoCode = (SourceCodeProperties) props.getPropertiesByName("pseudoCode");
    NodeStyle = (ArrayProperties) props.getPropertiesByName("NodeStyle");
    NodeEntries2D = (int[][]) primitives.get("NodeEntries2D");
    this.start(NodeEntries2D);

    return lang.toString();
  }

  private void start(int[][] node) {
    createIntro();
    startAlgo(node);
    createExitNote();
  }

  private void startAlgo(int[][] theNodes) {
    int[][] node = validate(theNodes);
    int minEntries = node.length / 2;
    int maxEntries = node.length - 1;

    String[] nodeNames = new String[node.length];
    for (int i = 0; i < nodeNames.length; i++) {
      nodeNames[i] = "R" + i;
    }
    String[] nodeNames1 = new String[node.length];
    for (int i = 0; i < nodeNames1.length; i++) {
      nodeNames1[i] = "  ";
    }
    String[] nodeNames2 = new String[node.length];
    for (int i = 0; i < nodeNames2.length; i++) {
      nodeNames2[i] = "  ";
    }

    Point p1 = lang.newPoint(new Coordinates(0, 70), "p1", null,
        new PointProperties());
    iNode = lang.newStringArray(new Offset(20, 20, p1, "SW"), nodeNames,
        "nodeNames", null, NodeStyle);
    iNode1 = lang.newStringArray(new Offset(20, 70, p1, "SW"), nodeNames1,
        "nodeNames1", null, NodeStyle);
    iNode2 = lang.newStringArray(new Offset(20, 95, p1, "SW"), nodeNames2,
        "nodeNames2", null, NodeStyle);

    TextProperties infoProps = new TextProperties();
    infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 12));
    info1 = lang.newText(new Offset(5, 0, p1, "SW"), "Node to be splitted:",
        "infoNode", null, infoProps);
    info2 = lang.newText(new Offset(5, 50, p1, "SW"), "Resulting nodes:",
        "infoNode", null, infoProps);

    infoNode1 = lang.newText(new Offset(7, 70, p1, "SW"), "1: ", "infoNode1",
        null, infoProps);
    infoNode2 = lang.newText(new Offset(7, 95, p1, "SW"), "2: ", "infoNode2",
        null, infoProps);

    Point p2 = lang.newPoint(new Coordinates(400, 10), "p1", null,
        new PointProperties());
    coordinatesText = new Text[node.length];
    for (int i = 0; i < node.length; i++) {
      coordinatesText[i] = lang.newText(new Offset(0, i * 20, p2, "SW"), "R"
          + i + " - " + "Top Left: " + node[i][0] + ", " + node[i][1]
          + " | Bottom Right: " + node[i][2] + ", " + node[i][3], "coordText"
          + i, null, infoProps);
    }
    lang.nextStep("Algorithm");

    // Source code
    Point p3 = lang.newPoint(new Coordinates(0, 200), "p1", null,
        new PointProperties());

    // first, set the visual properties for the source code

    // now, create the source code entity
    sc = lang.newSourceCode(new Offset(10, 0, p3, "SW"), "sourceCode", null,
        pseudoCode);
    sc.addCodeLine(
        "1) Apply algorithm PICK SEEDS to choose two entries to be the first elements of the resulting nodes.",
        null, 0, null);
    sc.addCodeLine("2) Check if all entries have been assigned.", null, 0, null);
    sc.addCodeLine(
        "3) Check if one node has so few entries that all the remaining entries must be assigned to",
        null, 0, null);
    sc.addCodeLine(
        "   it, in order for it to have theminimum number of entries.", null,
        0, null);
    sc.addCodeLine("   - In case 3) is true, assign them.", null, 0, null);
    sc.addCodeLine(
        "4)   Invoke algorithm PICK NEXT to choose the next entry to assign. Add it to the node, whose covering",
        null, 0, null);
    sc.addCodeLine(
        "     rectangle will have to be enlarged the least to accommodate it ",
        null, 0, null);

    Point p4 = lang.newPoint(new Coordinates(0, 350), "p1", null,
        new PointProperties());
    PickSeedsSC = lang.newSourceCode(new Offset(10, 0, p4, "SW"),
        "pickSeedsSC", null, pseudoCode);
    PickSeedsSC.addCodeLine("PICK SEEDS: ", null, 0, null);
    PickSeedsSC.addCodeLine("Calculate inefficiency of grouping entries", null,
        0, null);
    PickSeedsSC.addCodeLine("together. For each pair of entries Ri and Rj,",
        null, 0, null);
    PickSeedsSC.addCodeLine("compose a rectangle J including Ri and Rj.", null,
        0, null);
    PickSeedsSC.addCodeLine("Calculate d = area(J) - area(Ri) - area(Rj).",
        null, 0, null);
    PickSeedsSC.addCodeLine("Choose the pair with the largest d.", null, 0,
        null);
    PickSeedsSC.hide();

    PickNextSC = lang.newSourceCode(new Offset(10, 0, p4, "SW"), "pickNextSC",
        null, pseudoCode);

    PickNextSC.addCodeLine("PICK NEXT: ", null, 0, null);
    PickNextSC.addCodeLine("For each entry R not yet in a group, calculate",
        null, 0, null);
    PickNextSC.addCodeLine("the area increase required in the covering", null,
        0, null);
    PickNextSC.addCodeLine("rectangle of Node 1 to include Ri. Calculate ",
        null, 0, null);
    PickNextSC.addCodeLine("d2 slrmlarly for Node 2. Choose the entry, which",
        null, 0, null);
    PickNextSC.addCodeLine("requires minimum increase in one of the nodes.",
        null, 0, null);
    PickNextSC.hide();

    Point p5 = lang.newPoint(new Coordinates(400, 370), "p1", null,
        new PointProperties());
    infoMaxEntries = lang.newText(new Offset(10, 0, p5, "SW"),
        "Maximum Entries: " + maxEntries, "infoNode", null, infoProps);
    infoMinEntries = lang.newText(new Offset(10, 25, p5, "SW"),
        "Minimum Entries:" + minEntries, "infoNode", null, infoProps);
    infoMaxD = lang.newText(new Offset(10, 50, p5, "SW"),
        "Maximum d calculated:", "infoNode", null, infoProps);
    infoMBRNode1 = lang.newText(new Offset(10, 75, p5, "SW"),
        "Bounding rectangle of Node 1:", "infoNode", null, infoProps);
    infoMBRNode2 = lang.newText(new Offset(10, 100, p5, "SW"),
        "Bounding rectangle of Node 2:", "infoNode", null, infoProps);
    infoMinIncrease = lang.newText(new Offset(10, 125, p5, "SW"),
        "Maximum increase:", "infoNode", null, infoProps);
    infoMaxD.hide();
    infoMBRNode1.hide();
    infoMBRNode2.hide();
    infoMinIncrease.hide();

    lang.nextStep();

    // ALGO START

    // <
    sc.highlight(0);
    PickSeedsSC.show();
    // >

    node1 = new int[node.length][4];
    node2 = new int[node.length][4];
    int[] areas = new int[node.length];
    boolean[] assigned = new boolean[node.length];

    int seed1 = 0;
    int seed2 = 0;
    float diff;
    int i;
    int j;
    float maxDifference = Float.NEGATIVE_INFINITY;

    // Calculate the area of each entry
    for (i = 0; i < node.length; i++) {
      areas[i] = getArea(node[i]);
    }

    //
    for (i = 0; i < node.length; i++) {
      assigned[i] = false;
    }

    // PICK SEEDS
    for (i = 0; i < node.length; i++) {
      for (j = i + 1; j < node.length; j++) {
        // Area of the 2 entries combines...
        diff = (Math.max(node[i][2], node[j][2]) - Math.min(node[i][0],
            node[j][0]))
            * (Math.max(node[i][3], node[j][3]) - Math.min(node[i][1],
                node[j][1]));

        diff = diff - areas[i] - areas[j]; // ...minus their respective areas

        if (diff > maxDifference) {
          seed1 = i;
          seed2 = j;
          maxDifference = diff;
        }
      }
    }
    node1[0] = node[seed1];
    node2[0] = node[seed2];

    assigned[seed1] = true;
    assigned[seed2] = true;

    // <
    lang.nextStep();
    infoMaxD.show();
    infoMaxD.setText("Maximum d: " + (int) maxDifference, null, null);
    infoMaxD.changeColor(null, SelectedEntry, null, null);
    iNode.highlightElem(seed1, null, null);
    iNode.highlightElem(seed2, null, null);
    coordinatesText[seed1].changeColor(null, SelectedEntry, null, null);
    coordinatesText[seed2].changeColor(null, SelectedEntry, null, null);

    lang.nextStep();
    iNode.unhighlightElem(seed1, null, null);
    iNode.unhighlightElem(seed2, null, null);
    coordinatesText[seed1].changeColor(null, Color.BLACK, null, null);
    coordinatesText[seed2].changeColor(null, Color.BLACK, null, null);
    infoMaxD.changeColor(null, Color.BLACK, null, null);

    assignEntry(iNode1, seed1, 0);
    assignEntry(iNode2, seed2, 0);
    updateMBR();
    lang.nextStep();

    sc.unhighlight(0);
    sc.highlight(1);
    PickSeedsSC.hide();
    // >

    // END PICK SEEDS
    while (node1Idx + node2Idx < node.length) {

      // <
      sc.unhighlight(5);
      sc.unhighlight(6);
      PickNextSC.hide();
      sc.highlight(1);
      lang.nextStep();
      // >

      // <
      sc.unhighlight(1);
      sc.highlight(2);
      sc.highlight(3);
      lang.nextStep();
      // >

      // check if the 1st node has so few entries that all of the remaining
      // entries must be assigned to it
      if (node.length - node2Idx == minEntries) {
        // <
        sc.unhighlight(2);
        sc.unhighlight(3);
        sc.highlight(4);
        lang.nextStep();
        // >

        for (i = 0; i < node.length; i++) {
          if (assigned[i])
            continue;
          node1[node1Idx++] = node[i];
          assigned[i] = true;
          // <
          assignEntry(iNode1, i, node1Idx - 1);
          updateMBR();
          lang.nextStep();
          // >
        }
        return;
      }

      // check if the 2nd node has so few entries that all of the remaining
      // entries must be assigned to it
      if (node.length - node1Idx == minEntries) {
        // <
        sc.unhighlight(2);
        sc.unhighlight(3);
        sc.highlight(4);
        lang.nextStep();
        // >

        for (i = 0; i < node.length; i++) {
          if (assigned[i])
            continue;
          node2[node2Idx++] = node[i];
          assigned[i] = true;
          // <
          assignEntry(iNode2, i, node2Idx - 1);
          updateMBR();
          lang.nextStep();
          // >
        }
        return;
      }

      // PICK NEXT

      // <
      sc.unhighlight(2);
      sc.unhighlight(3);
      sc.highlight(5);
      sc.highlight(6);
      PickNextSC.show();
      // >

      int minExpansion = Integer.MAX_VALUE;
      int selectedEntry = 0;
      int selectedNode = 0;

      for (i = 0; i < node.length; i++) {
        if (assigned[i])
          continue;

        if (getExpansion(node1, node1Idx, node[i]) < minExpansion) {
          minExpansion = getExpansion(node1, node1Idx, node[i]);
          selectedEntry = i;
          selectedNode = 1;
        }
        if (getExpansion(node2, node2Idx, node[i]) < minExpansion) {
          selectedEntry = i;
          selectedNode = 2;
          minExpansion = getExpansion(node2, node2Idx, node[i]);
        }
      }

      // <
      infoMinIncrease.show();
      infoMinIncrease.setText("Minimum increase: " + minExpansion
          + "( when added to Node " + selectedNode + " )", null, null);
      infoMinIncrease.changeColor(null, SelectedEntry, null, null);
      selectEntry(selectedEntry);
      lang.nextStep();
      // >
      // END PICK NEXT

      // Assignment
      if (selectedNode == 1) {
        assigned[selectedEntry] = true;
        node1[node1Idx++] = node[selectedEntry];
        // <
        updateMBR();
        assignEntry(iNode1, selectedEntry, node1Idx - 1);
        // >
      } else {
        assigned[selectedEntry] = true;
        node2[node2Idx++] = node[selectedEntry];
        // <
        updateMBR();
        assignEntry(iNode2, selectedEntry, node2Idx - 1);
        // >
      }

      unselectEntry();
      infoMinIncrease.hide();
      lang.nextStep();
    }
  }

  private void assignEntry(StringArray iNewNode, int from, int to) {
    iNode.highlightCell(from, null, null);
    iNewNode.put(to, "R" + from, null, null);
    coordinatesText[from].changeColor(null, Color.LIGHT_GRAY, null, null);
  }

  private void updateMBR() {
    int[] node1MBR = getMBR(node1, node1Idx);
    int[] node2MBR = getMBR(node2, node2Idx);

    String mbr1 = "TL: " + node1MBR[0] + ", " + node1MBR[1] + " | BR: "
        + node1MBR[2] + ", " + node1MBR[3];
    String mbr2 = "TL: " + node2MBR[0] + ", " + node2MBR[1] + " | BR: "
        + node2MBR[2] + ", " + node2MBR[3];

    infoMBRNode1.setText("MBR of Node 1: " + mbr1, null, null);
    infoMBRNode2.setText("MBR of Node 2: " + mbr2, null, null);
    infoMBRNode1.show();
    infoMBRNode2.show();
  }

  private void selectEntry(int idx) {
    unselectEntry();
    iNode.highlightElem(idx, null, null);
    coordinatesText[idx].changeColor(null, SelectedEntry, null, null);
    currentlySelectedEntry = idx;
  }

  private void unselectEntry() {
    if (currentlySelectedEntry >= 0) {
      iNode.unhighlightElem(currentlySelectedEntry, null, null);
      coordinatesText[currentlySelectedEntry].changeColor(null,
          Color.LIGHT_GRAY, null, null);
    }
  }

  private int[][] validate(int[][] node) {
    // Make sure that the coordinates describe the top left and lower right
    // coordinates
    int temp;
    for (int i = 0; i < node.length; i++) {
      if (node[i][0] > node[i][2]) {
        temp = node[i][0];
        node[i][0] = node[i][2];
        node[i][2] = temp;
      }
      if (node[i][1] > node[i][3]) {
        temp = node[i][0];
        node[i][1] = node[i][3];
        node[i][3] = temp;
      }
    }

    return node;
  }

  private static int[] getMBR(int[][] node, int validElements) {
    int[] mbr = { Integer.MAX_VALUE, // left
        Integer.MAX_VALUE, // top
        Integer.MIN_VALUE, // right
        Integer.MIN_VALUE // bottom
    };

    for (int i = 0; i < validElements; i++) {
      if (node[i][0] < mbr[0])
        mbr[0] = node[i][0];
      if (node[i][1] < mbr[1])
        mbr[1] = node[i][1];
      if (node[i][2] > mbr[2])
        mbr[2] = node[i][2];
      if (node[i][3] > mbr[3])
        mbr[3] = node[i][3];
    }

    return mbr;
  }

  private int[] getExpandedMBR(int[][] node, int validElements, int[] candidate) {
    int[] mbr = getMBR(node, validElements);

    if (candidate[0] < mbr[0])
      mbr[0] = candidate[0];
    if (candidate[1] < mbr[1])
      mbr[1] = candidate[1];
    if (candidate[2] > mbr[2])
      mbr[2] = candidate[2];
    if (candidate[3] > mbr[3])
      mbr[3] = candidate[3];

    return mbr;
  }

  private int getExpandedMBRArea(int[][] node, int validElements,
      int[] candidate) {
    int[] exMBR = getExpandedMBR(node, validElements, candidate);
    return getArea(exMBR);

  }

  private int getExpansion(int[][] node, int validElements, int[] candidate) {
    return getExpandedMBRArea(node, validElements, candidate)
        - getArea(getMBR(node, validElements));
  }

  private int getArea(int[] entry) {
    return (entry[2] - entry[0]) * (entry[3] - entry[1]);
  }

  private void createIntro() {
    // STEP: Create the integer array and it's properties
    // TITLE
    TextProperties titleProps = new TextProperties();
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 18));
    TextProperties subtitleProps = new TextProperties();
    subtitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));
    title = lang.newText(new Coordinates(10, 10), "R-Tree: Node Spliting",
        "title", null, titleProps);
    subtitle = lang.newText(new Coordinates(10, 26),
        "Quadratic Split Algorithm", "title", null, subtitleProps);

    lang.nextStep("Introduction");

    // Introduction
    SourceCodeProperties introProps = new SourceCodeProperties();
    introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));
    SourceCode intro = lang.newSourceCode(new Coordinates(10, 50), "intro1",
        null, introProps);
    intro
        .addCodeLine(
            "R-trees are tree data structures that are similar to B-trees, but are used for",
            null, 0, null);
    intro.addCodeLine(
        "For example, the (X, Y) coordinates of geographical data.", null, 0,
        null);
    intro
        .addCodeLine(
            "The data structure splits space with hierarchically nested, and possibly ",
            null, 0, null);
    intro
        .addCodeLine(
            "overlapping, minimum bounding rectangles (MBRs) for 2D data, and bounding",
            null, 0, null);
    intro.addCodeLine("boxes when used for 3D data.", null, 0, null);
    lang.nextStep();
    intro.hide();

    SourceCode intro2 = lang.newSourceCode(new Coordinates(10, 50), "intro2",
        null, introProps);
    intro2
        .addCodeLine(
            "In order to add a new entry to a full node containg M entries, where M is",
            null, 0, null);
    intro2
        .addCodeLine(
            "the maximum number of entries per node, it is necessary to divide the collection",
            null, 0, null);
    intro2
        .addCodeLine(
            "of M+1 entries between 2 nodes. The division should be done in a way that makes",
            null, 0, null);
    intro2
        .addCodeLine(
            "it as unlikely as possible that both new nodes will need to be examined on",
            null, 0, null);
    intro2
        .addCodeLine(
            "subsequent searches. Since the decision whether to visit a node depends on",
            null, 0, null);
    intro2
        .addCodeLine(
            "whether its covering rectangle overlaps the search area, the total area of the",
            null, 0, null);
    intro2.addCodeLine(
        "two covering rectangles after a split should be minimized.", null, 0,
        null);
    lang.nextStep();
    intro2.hide();

    SourceCode intro3 = lang.newSourceCode(new Coordinates(10, 50), "intro3",
        null, introProps);
    intro3
        .addCodeLine(
            "The presented Quadratic-Cost algorithm attempts to find a small-area split,",
            null, 0, null);
    intro3.addCodeLine(
        "but is not guaranteed to find one with the smallest area possible.",
        null, 0, null);
    intro3.addCodeLine(
        "The cost is quadratic in M and linear in the number of dimensions.",
        null, 0, null);
    intro3
        .addCodeLine(
            "The presented tree node contains 2-dimensional objects for easier understanding.",
            null, 0, null);
    intro3.addCodeLine(
        "The principal remains the same for a higher number of dimensions.",
        null, 0, null);

    lang.nextStep("Conclusion");
    intro3.hide();
    subtitle.hide();
  }

  private void createExitNote() {
    hideEverything();
    title.show();
    subtitle.show();
    // Exit Note
    SourceCodeProperties introProps = new SourceCodeProperties();
    introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));
    SourceCode exitNote = lang.newSourceCode(new Coordinates(10, 50),
        "exitNote", null, introProps);
    exitNote
        .addCodeLine(
            "The most straightforward way to find the minimum area node split is to ",
            null, 0, null);
    exitNote
        .addCodeLine(
            "generate all possible groupings. However this method is extremely slow and",
            null, 0, null);
    exitNote.addCodeLine(
        "unpractical. Therefore Quadratic Split is used instead.", null, 0,
        null);
    exitNote
        .addCodeLine(
            "One of the alternative algorithms for node spliting, the Linear-Cost algorithm,",
            null, 0, null);
    exitNote
        .addCodeLine(
            "is very smilar to Quadratic Split, but uses different versions of PEEK SEEDS and PICK NEXT.",
            null, 0, null);
  }

  private void hideEverything() {
    // lang.hideAllPrimitives();

    for (int i = 0; i < coordinatesText.length; i++) {
      coordinatesText[i].hide();
    }
    infoMinEntries.hide();
    infoMaxEntries.hide();
    infoMaxD.hide();
    infoMinIncrease.hide();
    infoMBRNode1.hide();
    infoMBRNode2.hide();
    info1.hide();
    info2.hide();
    infoNode1.hide();
    infoNode2.hide();

    iNode.hide();
    iNode1.hide();
    iNode2.hide();
    sc.hide();
    PickSeedsSC.hide();
    PickNextSC.hide();
  }

  public String getName() {
    return "R-Tree Quadratic Node Spliting";
  }

  public String getAlgorithmName() {
    return "R-Tree Quadratic Node Spliting";
  }

  public String getAnimationAuthor() {
    return "Dimitar Goshev";
  }

  public String getDescription() {
    return "In order to add a new entry in a R-Tree to a full node containg M entries, where M is"
        + "\n"
        + "the maximum number of entries per node, it is necessary to divide the collection"
        + "\n"
        + "of M+1 entries between 2 nodes."
        + "\n"
        + "The presented Quadratic-Cost algorithm attempts to find a small-area split."
        + "\n"
        + "The cost is quadratic in M and linear in the number of dimensions."
        + "\n"
        + "The presented tree contains 2-dimensional objects for easier understanding."
        + "\n"
        + "The principal remains the same for a higher number of dimensions.";
  }

  public String getCodeExample() {
    return "1) Apply algorithm PICK SEEDS to choose two entries to be the first elements of the resulting nodes."
        + "\n"
        + "2) Check if all entries have been assigned."
        + "\n"
        + "3) Check if one node has so few entries that all the remaining entries must be assigned to"
        + "\n"
        + "   it, in order for it to have theminimum number of entries."
        + "\n"
        + "   - In case 3) is true, assign them."
        + "\n"
        + "4)   Invoke algorithm PICK NEXT to choose the next entry to assign. Add it to the node, whose covering"
        + "\n"
        + "     rectangle will have to be enlarged the least to accommodate it. GOTO 2."
        + "\n"
        + "    	"
        + "\n"
        + "Algorithm PICK SEEDS: "
        + "\n"
        + "Calculate inefficiency of grouping entries together. "
        + "\n"
        + "1) For each pair of entries Ri and Rj, compose a rectangle J including Ri and Rj."
        + "\n"
        + "2) Calculate d = area(J) - area(Ri) - area(Rj)."
        + "\n"
        + "3) Choose the pair with the largest d."
        + "\n"
        + "\n"
        + "Algorithm PICK NEXT: "
        + "\n"
        + "1) For each entry R not yet in a group, calculate the area increase required in "
        + "\n"
        + "   the covering rectangle of Node 1 to include Ri. "
        + "\n"
        + "2) Calculate d2 slrmlarly for Node 2. "
        + "\n"
        + "3) Choose the entry, which requires minimum increase in one of the nodes.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}