package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Polyline;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author ZHANG Qi
 * 
 */

public class TournamentSort implements Generator {

  private ArrayProperties     arrayProps;
  private Language            lang;
  private Text[]              nodes;
  private ArrayList<Polyline> lines;
  private int[]               tree;
  private IntArray            playerArray;
  private StringArray         treeArray;
  private SourceCode          sc;
  private SourceCode          sc2;
  private Text                txtBottomRowSize, txtTreeSize, txtLoadindex,
      txtIndex, txtj, txtm, txtl, txtIndexOfBottom, txtFinish;

  private static final String SOURCE_CODE = "\n public int[] sort(int[] player) {"
                                              + "\n	int[] tree;"
                                              + "\n	int n=player.length;"
                                              + "\n	int bottomRowSize = nearestPowerOfTwo(n);"
                                              + "\n	int TreeSize = 2 * bottomRowSize - 1;"
                                              + "\n	int loadindex = bottomRowSize - 1; "
                                              + "\n	tree = new int[TreeSize];"
                                              + "\n	for (int i = 0; i < TreeSize; i++) {"
                                              + "\n		tree[i] = Integer.MAX_VALUE;"
                                              + "\n	}"
                                              + "\n	int j = 0;"
                                              + "\n	for (int i = loadindex; i < TreeSize; i++) { "
                                              + "\n		if (j < n) {"
                                              + "\n			tree[i]=player[j++];"
                                              + "\n		} "
                                              + "\n		else {"
                                              + "\n			tree[i]=Integer.MAX_VALUE; "
                                              + "\n		}"
                                              + "\n	}"
                                              + "\n	int index = loadindex; "
                                              + "\n	while (index != 0) {"
                                              + "\n		j = index;"
                                              + "\n		while (j < 2 * index) { "
                                              + "\n			int playerLeft = tree[j];"
                                              + "\n			int playerRight = tree[j + 1];"
                                              + "\n			if (playerLeft < playerRight){"
                                              + "\n				tree[(j - 1) / 2] = playerLeft; "
                                              + "\n			}"
                                              + "\n			else{"
                                              + "\n				tree[(j - 1) / 2] = playerRight;"
                                              + "\n			}"
                                              + "\n			j += 2; "
                                              + "\n		}"
                                              + "\n		index = (index - 1) / 2; "
                                              + "\n	}"
                                              + "\n	for (i = 0; i < n - 1; i++) {"
                                              + "\n		player[i] = tree[0];"
                                              + "\n		int indexOfBottom=0;"
                                              + "\n		for(int k=0;k<tree.length;k++){"
                                              + "\n			if(tree[k]==player[i]){"
                                              + "\n				tree[k]=Integer.MAX_VALUE;"
                                              + "\n				indexOfBottom=k;"
                                              + "\n			}"
                                              + "\n		}"
                                              + "\n		UpdateTree(tree, indexOfBottom, nodes);"
                                              + "\n	}"
                                              + "\n	player[n - 1] = tree[0];"
                                              + "\n	return player;"
                                              + "\n }"
                                              + "\n public void UpdateTree(int[] tree, int m, Text[] nodes) {	"
                                              + "\n	if (m % 2 == 0){"
                                              + "\n		tree[(m - 1) / 2] = tree[m - 1]; "
                                              + "\n	}"
                                              + "\n	else{"
                                              + "\n		tree[(m - 1) / 2] = tree[m + 1]; "
                                              + "\n	}"
                                              + "\n	m = (m - 1) / 2;"
                                              + "\n	int l = 0;"
                                              + "\n	while (m != 0) {"
                                              + "\n		if (m % 2 == 0){"
                                              + "\n			l = m - 1; "
                                              + "\n		}"
                                              + "\n		else{"
                                              + "\n			l = m + 1;"
                                              + "\n		}"
                                              + "\n		if (tree[m] == Integer.MAX_VALUE || tree[l] == Integer.MAX_VALUE ) {"
                                              + "\n			if (tree[m] != Integer.MAX_VALUE ){"
                                              + "\n				tree[(m - 1) / 2] = tree[m];"
                                              + "\n			}"
                                              + "\n			else{"
                                              + "\n				tree[(m - 1) / 2] = tree[l];"
                                              + "\n			}"
                                              + "\n		}"
                                              + "\n		else{"
                                              + "\n			int playerRight = tree[m],playerLeft = tree[l]"
                                              + "\n			if (playerRight<playerLeft){"
                                              + "\n				tree[(m - 1) / 2] = tree[m];"
                                              + "\n			}"
                                              + "\n			else{"
                                              + "\n				tree[(m - 1) / 2] = tree[l];"
                                              + "\n			}"
                                              + "\n		}"
                                              + "\n		m = (m - 1) / 2; "
                                              + "\n	}" + "\n}";

  private static final String DESCRIPTION = "Tournament sort is a sorting algorithm. The name comes from its similarity to a single-elimination tournament"
                                              + "\nwhere there are many players (or teams) that play in two-sided matches. Each match compares the players,"
                                              + "\nand the winning player is promoted to play at match at the next level up. The hierarchy continues until the final"
                                              + "\nmatch determines the ultimate winner. The tournament determines the best player, but the player who was"
                                              + "\nbeaten in the final match may not be the second best—he may be inferior to other players the winner bested."
                                              + "\n"
                                              + "\nIt takes O(n) operations to select the next element of n elements; in a tournament sort, it takes O(nlogn)"
                                              + "\noperations (after building the initial tournament in )."
                                              + "\n"
                                              + "\nTournament sort is a variation of heapsort.";

  public TournamentSort(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  public TournamentSort() {
    lang = new AnimalScript("Tournament Sort", "Qi ZHANG", 1280, 1024);
    lang.setStepMode(true);
  }

  public int[] sort(int[] player) {
    int n = player.length;
    int bottomRowSize = nearestPowerOfTwo(n);
    int treeSize = 2 * bottomRowSize - 1;
    int loadindex = bottomRowSize - 1;

    tree = new int[treeSize];
    init(player, tree);

    lang.nextStep();

    sc.highlight(1);
    sc.highlight(2);
    sc.highlight(3);
    sc.highlight(4);
    txtBottomRowSize.setText("bottomRowSize = " + bottomRowSize, null, null);
    txtTreeSize.setText("treeSize = " + treeSize, null, null);
    txtLoadindex.setText("loadindex = " + loadindex, null, null);
    txtj.setText("j = 0", null, null);

    lang.nextStep("Initialize");
    sc.unhighlight(1);
    sc.unhighlight(2);
    sc.unhighlight(3);
    sc.unhighlight(4);
    for (int i = 0; i < treeSize; i++) {
      tree[i] = Integer.MAX_VALUE;
    }

    int j = 0;

    ArrayMarker iMarker = lang.newArrayMarker(playerArray, 0, "marker", null);

    for (int i = loadindex; i < treeSize; i++) {
      sc.unhighlight(7);
      sc.unhighlight(9);
      sc.highlight(6);
      lang.nextStep();
      sc.unhighlight(6);
      if (j < n) {
        treeArray.put(i, String.valueOf(player[j]), null, null);
        nodes[i].setText(String.valueOf(player[j]), null, null);
        iMarker.move(j, null, null);
        tree[i] = player[j++];
        sc.highlight(7);

        lang.nextStep();
      } else {
        tree[i] = Integer.MAX_VALUE;
        iMarker.hide();
        nodes[i].setText("X", null, null);
        treeArray.put(i, "X", null, null);
        sc.highlight(9);

        lang.nextStep();
      }

      // System.out.println(tree[i].getIndex()+" "+ tree[i].getData());
    }
    iMarker.hide();

    int i = loadindex;

    sc.unhighlight(7);
    sc.unhighlight(9);
    sc.highlight(11);
    txtIndex.setText("index = " + i, null, null);
    lang.nextStep("The 1. Tournament");

    while (i != 0) {
      sc.unhighlight(22);
      j = i;
      sc.unhighlight(11);
      sc.highlight(12);
      sc.highlight(13);
      txtj.setText("j = " + j, null, null);

      lang.nextStep();
      sc.unhighlight(12);
      sc.unhighlight(13);
      while (j < 2 * i) {

        int playerLeft = tree[j];
        int playerRight = tree[j + 1];
        nodes[j].changeColor("Color", Color.RED, null, null);
        nodes[j + 1].changeColor("Color", Color.RED, null, null);
        treeArray.highlightCell(j, null, null);
        treeArray.highlightCell(j + 1, null, null);
        sc.unhighlight(20);
        sc.highlight(16);

        lang.nextStep();

        if (playerLeft < playerRight) {
          tree[(j - 1) / 2] = playerLeft;
          nodes[(j - 1) / 2].setText(String.valueOf(playerLeft), null, null);
          treeArray.put((j - 1) / 2, String.valueOf(playerLeft), null, null);
          sc.highlight(17);
        } else {
          tree[(j - 1) / 2] = playerRight;
          nodes[(j - 1) / 2].setText(String.valueOf(playerRight), null, null);
          treeArray.put((j - 1) / 2, String.valueOf(playerRight), null, null);
          sc.highlight(19);
        }

        // System.out.println(tree[(j - 1) / 2].getIndex()+" "+ tree[(j
        // - 1) / 2].getData());
        sc.unhighlight(16);
        nodes[j].changeColor("Color", Color.BLACK, null, null);
        nodes[j + 1].changeColor("Color", Color.BLACK, null, null);
        nodes[(j - 1) / 2].changeColor("Color", Color.RED, null, null);
        treeArray.highlightCell((j - 1) / 2, null, null);
        treeArray.unhighlightCell(j, null, null);
        treeArray.unhighlightCell(j + 1, null, null);

        lang.nextStep();
        nodes[(j - 1) / 2].changeColor("Color", Color.BLACK, null, null);
        treeArray.unhighlightCell((j - 1) / 2, null, null);
        j += 2;
        sc.unhighlight(17);
        sc.unhighlight(19);
        sc.highlight(20);
        txtj.setText("j = " + j, null, null);

        lang.nextStep();
      }
      sc.unhighlight(20);
      i = (i - 1) / 2;
      sc.highlight(22);
      txtIndex.setText("index = " + i, null, null);

      lang.nextStep();
    }
    sc.unhighlight(22);

    for (i = 0; i < n - 1; i++) {
      player[i] = tree[0];
      playerArray.put(i, tree[0], null, null);
      playerArray.highlightCell(i, null, null);
      sc.highlight(25);
      lang.nextStep();
      sc.unhighlight(25);
      sc.highlight(27);
      sc.highlight(28);
      // System.out.println(player[i]);
      lang.nextStep();
      int index = 0;
      for (int k = 0; k < tree.length; k++) {
        sc.unhighlight(27);
        sc.unhighlight(28);
        if (tree[k] == player[i]) {
          sc.highlight(29);
          tree[k] = Integer.MAX_VALUE;
          treeArray.put(k, "X", null, null);
          treeArray.highlightCell(k, null, null);
          nodes[k].setText("X", null, null);
          index = k;
        }

        lang.nextStep();

        treeArray.unhighlightCell(k, null, null);
        sc.unhighlight(29);
        sc.highlight(27);
        sc.highlight(28);
        lang.nextStep();
      }
      txtIndexOfBottom.setText("indexOfBottom = " + index, null, null);
      lang.nextStep();

      sc.unhighlight(27);
      sc.unhighlight(28);
      sc.unhighlight(29);
      sc.unhighlight(30);
      sc.unhighlight(31);
      sc.unhighlight(32);
      sc.highlight(33);
      for (int k = 0; k < tree.length; k++) {
        treeArray.unhighlightCell(k, null, null);
      }
      lang.nextStep("The " + (i + 2) + ". Tournament");
      updateTree(tree, index, nodes);
    }

    player[n - 1] = tree[0];
    playerArray.put(n - 1, tree[0], null, null);
    playerArray.highlightCell(n - 1, null, null);
    sc.highlight(35);

    lang.nextStep();

    sc.unhighlight(35);
    sc.highlight(36);
    txtFinish.show();
    lang.nextStep();
    finish();
    lang.nextStep("Summary");
    return player;

  }

  /**
	 * 
	 */
  private void finish() {
    sc.hide();
    sc2.hide();
    treeArray.hide();
    playerArray.hide();
    txtBottomRowSize.hide();
    txtTreeSize.hide();
    txtLoadindex.hide();
    txtIndex.hide();
    txtj.hide();
    txtm.hide();
    txtl.hide();
    txtIndexOfBottom.hide();
    txtFinish.hide();
    for (int ii = 0; ii < nodes.length; ii++) {
      nodes[ii].hide();
    }
    for (int ii = 0; ii < lines.size(); ii++) {
      lines.get(ii).hide();
    }
    // description
    SourceCodeProperties scProps1 = new SourceCodeProperties();
    scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    SourceCode summary = lang.newSourceCode(new Coordinates(20, 120),
        "summary", null, scProps1);
    summary.addCodeLine("Summary", null, 0, null);
    summary.addCodeLine("", null, 0, null);
    summary.addCodeLine("", null, 0, null);
    scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 18));
    SourceCode detail = lang.newSourceCode(new Offset(0, 30, summary,
        AnimalScript.DIRECTION_SW), "detail", null, scProps1);
    detail
        .addCodeLine(
            "The above algorithm does provide optimal mechanism to find Kth minimum. This can also be used",
            null, 0, null);
    detail
        .addCodeLine(
            "for partially sorting unsorted array. But, note that this does require additional memory (or the order",
            null, 0, null);
    detail
        .addCodeLine(
            "of original array size). Also, the calculations does not account for processing required for creating",
            null, 0, null);
    detail
        .addCodeLine(
            "tree or the copy operations. What if we are limited by memory and the whole can't be fit into memory.",
            null, 0, null);
    detail.addCodeLine("Obviously this solution will not work.", null, 0, null);
    detail.addCodeLine("", null, 0, null);
    detail.addCodeLine("In the other hand is the implementation too complex.",
        null, 0, null);
  }

  public void updateTree(int[] tree, int myM, Text[] nodes) {
    int m = myM;
    sc.unhighlight(33);
    sc2.highlight(1);
    lang.nextStep();
    txtm.setText("m = " + m, null, null);
    sc2.unhighlight(1);
    if (m % 2 == 0) {
      tree[(m - 1) / 2] = tree[m - 1];
      nodes[(m - 1) / 2].setText(tree[m - 1] == Integer.MAX_VALUE ? "X"
          : +tree[m - 1] + "", null, null);
      treeArray.put((m - 1) / 2, tree[m - 1] == Integer.MAX_VALUE ? "X"
          : +tree[m - 1] + "", null, null);

      sc2.highlight(2);
    } else {
      tree[(m - 1) / 2] = tree[m + 1];
      nodes[(m - 1) / 2].setText(tree[m + 1] == Integer.MAX_VALUE ? "X"
          : +tree[m + 1] + "", null, null);
      treeArray.put((m - 1) / 2, tree[m + 1] == Integer.MAX_VALUE ? "X"
          : +tree[m + 1] + "", null, null);
      sc2.highlight(4);
    }
    treeArray.highlightCell((m - 1) / 2, null, null);
    nodes[(m - 1) / 2].changeColor("Color", Color.RED, null, null);

    lang.nextStep();

    m = (m - 1) / 2;
    int l = 0;
    sc2.unhighlight(2);
    sc2.unhighlight(4);
    sc2.highlight(5);
    sc2.highlight(6);
    txtm.setText("m = " + m, null, null);
    txtl.setText("l = " + l, null, null);

    lang.nextStep();

    sc2.unhighlight(5);
    sc2.unhighlight(6);
    while (m != 0) {
      if (m % 2 == 0) {
        l = m - 1;
        sc2.highlight(9);
      } else {
        l = m + 1;
        sc2.highlight(11);
      }
      treeArray.highlightCell(l, null, null);
      txtl.setText("l = " + l, null, null);
      nodes[m].changeColor("Color", Color.RED, null, null);
      nodes[l].changeColor("Color", Color.RED, null, null);
      lang.nextStep();

      sc2.unhighlight(9);
      sc2.unhighlight(11);
      sc2.highlight(12);

      lang.nextStep();
      sc2.unhighlight(12);
      if (tree[m] == Integer.MAX_VALUE || tree[l] == Integer.MAX_VALUE) {
        if (tree[m] != Integer.MAX_VALUE) {
          tree[(m - 1) / 2] = tree[m];
          nodes[(m - 1) / 2].setText(tree[m] == Integer.MAX_VALUE ? "X"
              : +tree[m] + "", null, null);
          treeArray.put((m - 1) / 2, tree[m] == Integer.MAX_VALUE ? "X"
              : +tree[m] + "", null, null);
          sc2.highlight(14);
        } else {
          tree[(m - 1) / 2] = tree[l];
          nodes[(m - 1) / 2].setText(tree[l] == Integer.MAX_VALUE ? "X"
              : +tree[l] + "", null, null);
          treeArray.put((m - 1) / 2, tree[l] == Integer.MAX_VALUE ? "X"
              : +tree[l] + "", null, null);
          sc2.highlight(17);
          // System.out.println(nodes[(m - 1) / 2].getText());
        }

      } else {
        int playerRight = tree[m];
        int playerLeft = tree[l];
        // System.out.println(playerRight+"  "+playerLeft);
        if (playerRight < playerLeft) {
          tree[(m - 1) / 2] = tree[m];
          nodes[(m - 1) / 2].setText(tree[m] + "", null, null);
          treeArray.put((m - 1) / 2, tree[m] + "", null, null);
          sc2.highlight(23);
          // System.out.println(nodes[(m - 1) / 2].getText());
        } else {
          tree[(m - 1) / 2] = tree[l];
          nodes[(m - 1) / 2].setText(tree[l] + "", null, null);
          treeArray.put((m - 1) / 2, tree[l] + "", null, null);
          sc2.highlight(26);
          // System.out.println(nodes[(m - 1) / 2].getText());
        }
      }
      treeArray.highlightCell((m - 1) / 2, null, null);
      nodes[(m - 1) / 2].changeColor("Color", Color.RED, null, null);
      nodes[m].changeColor("Color", Color.BLACK, null, null);
      nodes[l].changeColor("Color", Color.BLACK, null, null);
      treeArray.unhighlightCell(m, null, null);
      treeArray.unhighlightCell(l, null, null);

      lang.nextStep();

      sc2.unhighlight(23);
      sc2.unhighlight(26);
      sc2.unhighlight(14);
      sc2.unhighlight(17);

      nodes[(m - 1) / 2].changeColor("Color", Color.BLACK, null, null);
      treeArray.unhighlightCell((m - 1) / 2, null, null);
      sc2.highlight(29);

      lang.nextStep();

      m = (m - 1) / 2;
      sc2.unhighlight(29);
    }
  }

  public static int nearestPowerOfTwo(int inputNumber) {
    int number = inputNumber;
    --number;
    number |= number >> 16;
    number |= number >> 8;
    number |= number >> 4;
    number |= number >> 2;
    number |= number >> 1;
    ++number;
    return number;
  }

  /**
   * @param tree
   * @param player
   * 
   */
  private void init(int[] player, int[] tree) {
    // 1. page
    // background
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(45, 166,
        218));
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        new Color(45, 166, 218));
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    Rect titleRect = lang.newRect(new Coordinates(10, 20), new Coordinates(200,
        50), "background", null, rectProps);
    // title
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    lang.newText(new Offset(10, 10, titleRect, AnimalScript.DIRECTION_NW),
        "Tournament Sort", "title", null, textProps);
    // description
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 20));
    SourceCode desc = lang.newSourceCode(new Offset(0, 50, titleRect,
        AnimalScript.DIRECTION_SW), "desc1", null, scProps);
    desc.addCodeLine(
        "Tournament sort is a sorting algorithm. The name comes from its similarity to a single-elimination tournament",
        null, 0, null);
    desc.addCodeLine(
        "where there are many players (or teams) that play in two-sided matches. Each match compares the players,",
        null, 0, null);
    desc.addCodeLine(
        "and the winning player is promoted to play at match at the next level up. The hierarchy continues until the final",
        null, 0, null);
    desc.addCodeLine(
        "match determines the ultimate winner. The tournament determines the best player, but the player who was",
        null, 0, null);
    desc.addCodeLine(
        "beaten in the final match may not be the second best—he may be inferior to other players the winner bested.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "it takes O(n) operations to select the next element of n elements; in a tournament sort, it takes O(nlogn)",
        null, 0, null);
    desc.addCodeLine("operations (after building the initial tournament in ).",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("Tournament sort is a variation of heapsort.", null, 0,
        null);

    lang.nextStep();

    desc.hide();
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "First question group", 1);
    lang.addQuestionGroup(groupInfo);

    TrueFalseQuestionModel tfq = new TrueFalseQuestionModel(
        "trueFalseQuestion", true, 1);
    tfq.setPrompt("Is Tournamentsort a variation of heapsort?");
    tfq.setGroupID("First question group");
    lang.addTFQuestion(tfq);
    lang.nextStep();
    scProps = new SourceCodeProperties();
    // now, create the source code entity
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 11));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    sc = lang.newSourceCode(new Offset(0, 10, titleRect,
        AnimalScript.DIRECTION_SW), "sourceCode", null, scProps);
    sc.addCodeLine("public int[] sort(int[] player) {", null, 0, null);
    sc.addCodeLine("int[] tree, j = 0;", null, 1, null);
    sc.addCodeLine("int n=player.length, bottomRowSize=nearestPowerOfTwo(n);",
        null, 1, null);
    sc.addCodeLine("TreeSize=2 *bottomRowSize-1, loadindex=bottomRowSize-1;",
        null, 1, null);
    sc.addCodeLine("init(tree)", null, 1, null);
    sc.addCodeLine("for (int i = loadindex; i < TreeSize; i++) { ", null, 1,
        null);
    sc.addCodeLine("if (j < n)", null, 2, null);
    sc.addCodeLine("tree[i]=player[j++];", null, 3, null);
    sc.addCodeLine("else", null, 2, null);
    sc.addCodeLine("tree[i]=Integer.MAX_VALUE; ", null, 3, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("int index = loadindex; ", null, 1, null);
    sc.addCodeLine("while (index != 0) {", null, 1, null);
    sc.addCodeLine("j = index;", null, 2, null);
    sc.addCodeLine("while (j < 2 * index) { ", null, 2, null);
    sc.addCodeLine("int playerLeft = tree[j], playerRight = tree[j + 1];",
        null, 3, null);
    sc.addCodeLine("if (playerLeft < playerRight)", null, 3, null);
    sc.addCodeLine("tree[(j - 1) / 2] = playerLeft; ", null, 4, null);
    sc.addCodeLine("else", null, 3, null);
    sc.addCodeLine("tree[(j - 1) / 2] = playerRight;", null, 4, null);
    sc.addCodeLine("j += 2; ", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("index = (index - 1) / 2; ", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("for (i = 0; i < n - 1; i++) {", null, 1, null);
    sc.addCodeLine("player[i] = tree[0];", null, 2, null);
    sc.addCodeLine("int indexOfBottom=0;", null, 2, null);
    sc.addCodeLine("for(int k=0;k<tree.length;k++){", null, 2, null);
    sc.addCodeLine("if(tree[k]==player[i]){", null, 3, null);
    sc.addCodeLine("tree[k]=Integer.MAX_VALUE;", null, 4, null);
    sc.addCodeLine("indexOfBottom=k;", null, 4, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("UpdateTree(tree, indexOfBottom, nodes);", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("player[n - 1] = tree[0];", null, 1, null);
    sc.addCodeLine("return player;", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    sc2 = lang.newSourceCode(new Offset(20, 0, sc, AnimalScript.DIRECTION_NE),
        "sourceCode2", null, scProps);
    sc2.addCodeLine(
        "public void UpdateTree(int[] tree, int m, Text[] nodes) {	", null, 0,
        null);
    sc2.addCodeLine("if (m % 2 == 0)", null, 1, null);
    sc2.addCodeLine("tree[(m - 1) / 2] = tree[m - 1]; ", null, 2, null);
    sc2.addCodeLine("else", null, 1, null);
    sc2.addCodeLine("tree[(m - 1) / 2] = tree[m + 1]; ", null, 2, null);
    sc2.addCodeLine("m = (m - 1) / 2;", null, 1, null);
    sc2.addCodeLine("int l = 0;", null, 1, null);
    sc2.addCodeLine("while (m != 0) {", null, 1, null);
    sc2.addCodeLine("if (m % 2 == 0)", null, 2, null);
    sc2.addCodeLine("l = m - 1; ", null, 3, null);
    sc2.addCodeLine("else", null, 2, null);
    sc2.addCodeLine("l = m + 1;", null, 3, null);
    sc2.addCodeLine(
        "if (tree[m] == Integer.MAX_VALUE || tree[l] == Integer.MAX_VALUE ) {",
        null, 2, null);
    sc2.addCodeLine("if (tree[m] != Integer.MAX_VALUE ){", null, 3, null);
    sc2.addCodeLine("tree[(m - 1) / 2] = tree[m];", null, 4, null);
    sc2.addCodeLine("}", null, 3, null);
    sc2.addCodeLine("else{", null, 3, null);
    sc2.addCodeLine("tree[(m - 1) / 2] = tree[l];", null, 4, null);
    sc2.addCodeLine("}", null, 3, null);
    sc2.addCodeLine("}", null, 2, null);
    sc2.addCodeLine("else{", null, 2, null);
    sc2.addCodeLine("int playerRight = tree[m],playerLeft = tree[l]", null, 3,
        null);
    sc2.addCodeLine("if (playerRight<playerLeft){", null, 3, null);
    sc2.addCodeLine("tree[(m - 1) / 2] = tree[m];", null, 4, null);
    sc2.addCodeLine("}", null, 3, null);
    sc2.addCodeLine("else{", null, 3, null);
    sc2.addCodeLine("tree[(m - 1) / 2] = tree[l];", null, 4, null);
    sc2.addCodeLine("}", null, 3, null);
    sc2.addCodeLine("}", null, 2, null);
    sc2.addCodeLine("m = (m - 1) / 2; ", null, 2, null);
    sc2.addCodeLine("}", null, 1, null);
    sc2.addCodeLine("}", null, 0, null);

    arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(45, 166,
        218));
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(
        255, 200, 0));
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 18));
    // now, create the IntArray object, linked to the properties
    playerArray = lang.newIntArray(new Offset(20, 0, sc2,
        AnimalScript.DIRECTION_NE), player, "intArray", null, arrayProps);
    String[] temp = new String[tree.length];
    for (int i = 0; i < temp.length; i++) {
      temp[i] = "---";
    }
    treeArray = lang.newStringArray(new Offset(0, 20, playerArray,
        AnimalScript.DIRECTION_SW), temp, "StringArray", null, arrayProps);
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 15));
    txtBottomRowSize = lang.newText(new Offset(0, 20, treeArray,
        AnimalScript.DIRECTION_SW), "bottomRowSize =", "bottomRowSize", null,
        textProps);
    txtTreeSize = lang.newText(new Offset(0, 10, txtBottomRowSize,
        AnimalScript.DIRECTION_SW), "treeSize =", "treeSize", null, textProps);
    txtLoadindex = lang
        .newText(new Offset(0, 10, txtTreeSize, AnimalScript.DIRECTION_SW),
            "loadindex =", "loadindex", null, textProps);
    txtIndex = lang.newText(new Offset(0, 10, txtLoadindex,
        AnimalScript.DIRECTION_SW), "index =", "index", null, textProps);
    txtj = lang.newText(new Offset(0, 10, txtIndex, AnimalScript.DIRECTION_SW),
        "j =", "j", null, textProps);
    txtm = lang.newText(new Offset(0, 10, txtj, AnimalScript.DIRECTION_SW),
        "m =", "m", null, textProps);
    txtl = lang.newText(new Offset(0, 10, txtm, AnimalScript.DIRECTION_SW),
        "l =", "l", null, textProps);
    txtIndexOfBottom = lang.newText(new Offset(0, 10, txtl,
        AnimalScript.DIRECTION_SW), "indexOfBottom =", "indexOfBottom", null,
        textProps);

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 15));

    int originalXcoordTree = 1000, originalYcoordTree = 450;
    nodes = new Text[tree.length];
    // create the nodes (with text) //
    for (int i = 0; i < nodes.length; i++) {
      double p = 0;
      if (i == 0) {
        nodes[i] = lang.newText(new Coordinates(originalXcoordTree,
            originalYcoordTree), tree[i] == Integer.MAX_VALUE ? "X" : tree[i]
            + "", tree[i] == Integer.MAX_VALUE ? "X" : tree[i] + "", null,
            textProps);
      } else if (i == 1 || i == 2) {
        p = 1.5;
        p = p - i;
        double x = originalXcoordTree - 160 * p;
        nodes[i] = lang.newText(new Coordinates((int) x,
            originalYcoordTree + 30), tree[i] == Integer.MAX_VALUE ? "X"
            : tree[i] + "", tree[i] == Integer.MAX_VALUE ? "X" : tree[i] + "",
            null, textProps);
      } else if (i >= 3 && i <= 6) {
        p = 4.5;
        p = p - i;
        double x = originalXcoordTree - 80 * p;
        nodes[i] = lang.newText(new Coordinates((int) x,
            originalYcoordTree + 60), tree[i] == Integer.MAX_VALUE ? "X"
            : tree[i] + "", tree[i] == Integer.MAX_VALUE ? "X" : tree[i] + "",
            null, textProps);
      } else if (i >= 7 && i <= 14) {
        p = 10.5;
        p = p - i;
        double x = originalXcoordTree - 40 * p;
        nodes[i] = lang.newText(new Coordinates((int) x,
            originalYcoordTree + 90), tree[i] == Integer.MAX_VALUE ? "X"
            : tree[i] + "", tree[i] == Integer.MAX_VALUE ? "X" : tree[i] + "",
            null, textProps);
      } else if (i >= 15 && i <= 30) {
        p = 22.5;
        p = p - i;
        double x = originalXcoordTree - 20 * p;
        nodes[i] = lang.newText(new Coordinates((int) x,
            originalYcoordTree + 120), tree[i] == Integer.MAX_VALUE ? "X"
            : tree[i] + "", tree[i] == Integer.MAX_VALUE ? "X" : tree[i] + "",
            null, textProps);
      }
    }

    // create the edges with polylines
    PolylineProperties pp = new PolylineProperties();
    pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);

    lines = new ArrayList<Polyline>();
    for (int i = 0; i < nodes.length; i++) {
      Polyline temp1;
      if (2 * i + 1 < nodes.length) {
        temp1 = lang.newPolyline(new Node[] { nodes[i].getUpperLeft(),
            nodes[2 * i + 1].getUpperLeft() }, "line1: " + i, null, pp);
        lines.add(temp1);
      }
      if (2 * i + 2 < nodes.length) {
        temp1 = lang.newPolyline(new Node[] { nodes[i].getUpperLeft(),
            nodes[2 * i + 2].getUpperLeft() }, "line2: " + i, null, pp);
        lines.add(temp1);
      }

    }
    txtFinish = lang.newText(new Offset(0, 200, txtIndexOfBottom,
        AnimalScript.DIRECTION_SW), "The Algorithm running finish, Thanks ^_^",
        "finish", null, textProps);
    txtFinish.hide();

    lang.nextStep();
    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion");
    mcq.setPrompt("What size of the array used for sorting n numbers should be allocated?");
    mcq.addAnswer("n", 5, "No, not really.");
    mcq.addAnswer("2n", 0, "No, not really.");
    mcq.addAnswer("(2 power x) -1, x=(log n)+1 ", 1, "Absolutely right!");
    mcq.setGroupID("First question group");
    lang.addMCQuestion(mcq);

    // a sample multiple selection question
    lang.nextStep();

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init(); // ensure all properties are set up

    // for (Iterator it = primitives.keySet().iterator(); it.hasNext(); ) {
    // String key = (String) it.next();
    // Object value = primitives.get(key);
    // System.out.println("key= "+key+"    value= "+value);
    // }

    int[] arrayData = (int[]) primitives.get("intArray");
    // arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
    // props.get("intArray",
    // AnimationPropertiesKeys.COLOR_PROPERTY));
    sort(arrayData);
    lang.finalizeGeneration();
    return lang.toString();
  }

  public static void main(String[] args) {
    // int [] list={7,5,6,1,2,8,10,13};
    int[] list = { 8, 63, 49, 21, 16, 25, 26 };
    TournamentSort c = new TournamentSort();
    // c.lang.finalizeGeneration();
    // int[] result =
    c.sort(list);
    c.lang.finalizeGeneration();
    System.out.println(c.lang);
    // for(int t:result)
    // System.out.print(t+" ");
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.framework.Generator#getAlgorithmName()
   */
  @Override
  public String getAlgorithmName() {
    return "Tournament Sort";
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.framework.Generator#getAnimationAuthor()
   */
  @Override
  public String getAnimationAuthor() {
    return "ZHANG Qi";
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.framework.Generator#getCodeExample()
   */
  @Override
  public String getCodeExample() {
    return SOURCE_CODE;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.framework.Generator#getContentLocale()
   */
  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.framework.Generator#getDescription()
   */
  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.framework.Generator#getFileExtension()
   */
  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.framework.Generator#getGeneratorType()
   */
  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.framework.Generator#getName()
   */
  @Override
  public String getName() {
    return "TournamentSort";
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.framework.Generator#getOutputLanguage()
   */
  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.framework.Generator#init()
   */
  @Override
  public void init() {
    lang = new AnimalScript("Tournament Sort", "Qi ZHANG", 1280, 1024);
    lang.setStepMode(true);
  }

}