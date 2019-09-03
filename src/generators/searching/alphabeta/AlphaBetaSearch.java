package generators.searching.alphabeta;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.Graph;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class AlphaBetaSearch implements ValidatingGenerator {

  /**
   * tree node class to construct the search tree
   */
  public static final class TreeNode {
    /** the tree node label */
    public final String     label;

    /** the tree node children */
    public final TreeNode[] children;

    /** constructs a new the tree node with the given label */
    public TreeNode(String label) {
      this.label = label;
      this.children = null;
    }

    /**
     * constructs a new the tree node with the given label and the given tree
     * node children
     */
    public TreeNode(String label, TreeNode[] children) {
      this.label = label;
      this.children = children == null || children.length == 0 ? null
          : children;
    }

    /**
     * retrieves the count of all nodes in the tree whose root is this tree node
     */
    public int getNodeCount() {
      int count = 1;
      if (children != null)
        for (TreeNode child : children)
          count += child.getNodeCount();
      return count;
    }

    /** retrieves the depth of the tree whose root is this tree node */
    public int getDepth() {
      if (children != null) {
        int depth = 0;
        for (TreeNode child : children)
          depth = Math.max(depth, child.getDepth());
        return depth + 1;
      }
      return 1;
    }
  }

  private Language             lang;
  private SourceCodeProperties continuousTextProps;
  private SourceCodeProperties continuousExplanatoryTextProps;
  private TextProperties       valueTextProps;
  private SourceCodeProperties sourceCodeProps;
  private String               treeString;
  private TextProperties       titleProps;
  private GraphProperties      graphProps;
  private TextProperties       intervalTextProps;
  private TextProperties       statusTextProps;
  private PolygonProperties    cutoffAreaProps;
  private TextProperties       cutoffTextProps;

  public void init() {
    lang = new AnimalScript("Alpha-Beta-Suche", "Pascal Weisenburger", 800, 600);
    lang.setStepMode(true);
  }

  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    String treeString = (String) primitives.get("tree");
    TreeNode root = parseString(treeString);
    if (root == null)
      return false;

    if (!checkTreeLeafIntegerValue(root))
      return false;

    return true;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    continuousTextProps = (SourceCodeProperties) props
        .getPropertiesByName("continuousText");
    continuousExplanatoryTextProps = (SourceCodeProperties) props
        .getPropertiesByName("continuousExplanatoryText");
    valueTextProps = (TextProperties) props.getPropertiesByName("valueText");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    treeString = (String) primitives.get("tree");
    titleProps = (TextProperties) props.getPropertiesByName("title");
    graphProps = (GraphProperties) props.getPropertiesByName("tree");
    intervalTextProps = (TextProperties) props
        .getPropertiesByName("intervalText");
    statusTextProps = (TextProperties) props.getPropertiesByName("statusText");
    cutoffAreaProps = (PolygonProperties) props
        .getPropertiesByName("cutoffArea");
    cutoffTextProps = (TextProperties) props.getPropertiesByName("cutoffText");

    intervalTextProps.set(
        AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(((Font) intervalTextProps
            .get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(),
            Font.BOLD, 14));
    valueTextProps.set(
        AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(((Font) valueTextProps
            .get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(),
            Font.BOLD, 14));
    cutoffTextProps.set(
        AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(((Font) cutoffTextProps
            .get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(),
            Font.BOLD, 14));

    search(parseString(treeString));

    return lang.toString();
  }

  /**
   * Constructs are tree from a given string representation, e.g. a tree of the
   * following form
   * 
   * <pre>
   * <code> A
   * ├─ B
   * │  ├─ 5
   * │  └─ 2
   * ├─ C
   * │  ├─ 7
   * │  └─ 1
   * └─ 8</code>
   * </pre>
   * 
   * could be represented by the string <code>"A {B {5 2} C {7 1} 8}"</code>
   * 
   * @param string
   *          the given string representation
   * @return the tree's root node
   */
  public static TreeNode parseString(String string) {
    String string2 = string;
    int childrenStart = string2.indexOf('{');
    if (childrenStart == -1) {
      string2 = string2.trim();
      if (string2.indexOf(' ') != -1 || string2.isEmpty())
        return null;
      return new TreeNode(string2);
    }

    char ch;
    int pos = childrenStart + 1;
    boolean childrenComplete = false;
    List<TreeNode> children = new ArrayList<TreeNode>();
    for (int c = 0, i = pos; i < string2.length(); i++)
      switch (ch = string2.charAt(i)) {
        case ' ':
        case '\t':
          break;
        case '{':
          c++;
          break;
        case '}':
          if (--c >= 0)
            break;
          if (i < string2.length() - 1 && string2.substring(i).trim().isEmpty())
            return null;
          childrenComplete = true;
        default:
          if (ch != '}') {
            ch = string2.charAt(i - 1);
            if (ch != ' ' && ch != '\t' && ch != '{' && ch != '}')
              break;
          }

          if (c <= 0) {
            String childString = string2.substring(pos, i).trim();
            pos = i;
            if (childString.isEmpty())
              break;

            TreeNode child = parseString(childString);
            if (child == null)
              return null;
            children.add(child);
          }
      }

    if (!childrenComplete)
      return null;

    string2 = string2.substring(0, childrenStart).trim();
    if (string2.isEmpty())
      return null;

    return new TreeNode(string2,
        children.toArray(new TreeNode[children.size()]));
  }

  /**
   * checks if all leaf nodes of tree given by its root node are interpretable
   * as integer values
   */
  private static boolean checkTreeLeafIntegerValue(TreeNode node) {
    if (node.children == null) {
      try {
        Integer.parseInt(node.label);
      } catch (NumberFormatException e) {
        return false;
      }
    } else
      for (TreeNode child : node.children)
        if (!checkTreeLeafIntegerValue(child))
          return false;
    return true;
  }

  private int              graphNodeCount;
  private int[][]          adjacencyMatrix;
  private Coordinates[]    coords;
  private String[]         labels;
  private Text[]           intervals;
  private Text[]           values;
  private int[]            chosenPath;

  private Graph            graph;
  private SourceCode       maxCode;
  private SourceCode       minCode;

  private static final int GRAPH_HORIZONTAL_DISTANCE = 40;
  private static final int GRAPH_VERTICAL_DISTANCE   = 60;
  private static final int GRAPH_LABEL_CENTER        = 5;
  private static final int GRAPH_LABEL_ASIDE         = 25;
  private static final int ROUND_RECT_STEP_COUNT     = 5;

  /**
   * Computes the adjacency matrix, the coordinates of the graph nodes (given
   * the origin coordinates of the graph object) and the graph node labels for a
   * given tree. The nodes are indexed in pre-order (depth-first traversal of
   * the tree) starting with 0.
   * 
   * @param origin
   *          [in] the graph object origin coordinates
   * @param horizontalDistance
   *          [in] the minimum horizontal distance of each tree node
   * @param verticalDistance
   *          [in] the vertical distance of each level
   * @param node
   *          [in] the tree root node
   * @param nodeIndex
   *          [in] the current node index; root node must have index 0
   * @param depth
   *          [in] the tree depth
   * @param adjacencyMatrix
   *          [out] the graph adjacency matrix
   * @param coords
   *          [out] the coordinates of each node (node-index-based access)
   * @param labels
   *          [out] the labels of each node (node-index-based access)
   * @return the index following the last index of the descendant nodes
   */
  private static int buildGraph(final Coordinates origin,
      final int horizontalDistance, final int verticalDistance, TreeNode node,
      final int nodeIndex, final int depth, int[][] adjacencyMatrix,
      Coordinates[] coords, String[] labels) {
    int childIndex = nodeIndex + 1;
    if (node.children == null) {
      // leaf node
      Coordinates coord = null;
      for (int i = nodeIndex; i >= 0; i--)
        if (coords[i] != null) {
          // node is placed horizontally next to the previous leaf
          coord = new Coordinates(coords[i].getX() + horizontalDistance,
              origin.getY() + depth * verticalDistance);
          break;
        }
      if (coord == null)
        // node is the first leaf to be placed
        coord = new Coordinates(origin.getX(), origin.getY() + depth
            * verticalDistance);
      coords[nodeIndex] = coord;
    } else {
      // inner node
      int xcoord = 0, xcoordcount = 0;
      for (int i = 0; i < node.children.length; i++) {
        // build sub tree for each child
        int nextChildIndex = buildGraph(origin, horizontalDistance,
            verticalDistance, node.children[i], childIndex, depth + 1,
            adjacencyMatrix, coords, labels);

        // set node edge link to its children in the adjacency matrix
        adjacencyMatrix[nodeIndex][childIndex] = 1;
        adjacencyMatrix[childIndex][nodeIndex] = 1;

        // calculate node position centered over the middle child(ren)
        if (Math.abs(2 * i - node.children.length + 1) <= 1) {
          xcoord += coords[childIndex].getX();
          xcoordcount++;
        }
        childIndex = nextChildIndex;
      }
      coords[nodeIndex] = new Coordinates(xcoord / xcoordcount, origin.getY()
          + depth * verticalDistance);
    }

    labels[nodeIndex] = node.label;
    return childIndex;
  }

  /**
   * Searches the given tree
   * 
   * @param root
   *          the tree's root node
   */
  public void search(TreeNode root) {
    /*
     * create title
     */
    System.out.println(root);
    TextProperties headerText1Props = new TextProperties();
    headerText1Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 32));
    headerText1Props.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        titleProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    Primitive headerText1 = lang.newText(new Coordinates(20, 20),
        "Alpha-Beta-Suche", "headerText1", null, headerText1Props);

    TextProperties headerText2Props = new TextProperties();
    headerText2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 16));
    headerText2Props.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        titleProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    Primitive headerText2 = lang.newText(new Offset(0, -20, headerText1, "SW"),
        "Minimax-Suche mit Alpha-Beta-Pruning", "headerText2", null,
        headerText2Props);

    PolylineProperties headerProps = new PolylineProperties();
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        titleProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    Primitive header = lang.newPolyline(new Node[] {
        new Offset(-5, 5, headerText2, "SW"),
        new Offset(200, 5, headerText2, "SE") }, "header", null, headerProps);

    /*
     * description
     */
    SourceCode desc = lang.newSourceCode(new Offset(5, 15, header, "SW"),
        "description", null, continuousTextProps);
    desc.addCodeLine(
        "Die Alpha-Beta-Suche ist ein Algorithmus zur Ermittlung der optimalen",
        null, 0, null);
    desc.addCodeLine(
        "Spielstrategie für Nullsummenspiele, bei denen zwei gegnerische Spieler",
        null, 0, null);
    desc.addCodeLine("abwechselnd Züge ausführen.", null, 0, null);
    desc.addCodeLine(
        "Der MAX-Spieler versucht dabei, den Wert eines Knotens zu maximieren,",
        null, 0, null);
    desc.addCodeLine(
        "wohingegen der MIN-Spieler versucht, den Wert eines Knotens zu minimieren.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Während der Suche werden zwei Werte α und β aktualisiert, die angeben,",
        null, 0, null);
    desc.addCodeLine(
        "welches Ergebnis die Spieler bei optimaler Spielweise erzielen können.",
        null, 0, null);
    desc.addCodeLine(
        "Mit Hilfe dieser Werte kann entschieden werden, welche Teile des Suchbaumes",
        null, 0, null);
    desc.addCodeLine(
        "nicht untersucht werden müssen, weil sie das Ergebnis der Problemlösung",
        null, 0, null);
    desc.addCodeLine("nicht beeinflussen können (Pruning).", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Die Alpha-Beta-Suche ist eine optimierte Variante der Minimax-Suche,",
        null, 0, null);
    desc.addCodeLine("die ohne Pruning arbeitet.", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("http://de.wikipedia.org/wiki/Alpha-Beta-Suche", null, 0,
        null);
    desc.addCodeLine("http://de.wikipedia.org/wiki/Minimax-Algorithmus", null,
        0, null);

    lang.nextStep("Beschreibung");
    desc.hide();

    if (!(Boolean) continuousExplanatoryTextProps
        .get(AnimationPropertiesKeys.HIDDEN_PROPERTY)) {
      SourceCode descMinMax1 = lang.newSourceCode(new Offset(5, 15, header,
          "SW"), "description", null, continuousTextProps);
      descMinMax1.addCodeLine("Idee des Minimax-Algorithmus:", null, 0, null);
      lang.nextStep();
      descMinMax1.addCodeLine("", null, 0, null);
      descMinMax1
          .addCodeLine(
              "Der Algorithmus wird auf einen Suchbaum angewendet, bei dem jede Kante",
              null, 0, null);
      descMinMax1
          .addCodeLine(
              "zu einem Kindknoten einen möglichen Spielzug des Spielers beschreibt,",
              null, 0, null);
      descMinMax1.addCodeLine("der bei dem entsprechenden Knoten am Zug ist.",
          null, 0, null);
      descMinMax1.addCodeLine(
          "In der Regel werden Spiele betrachtet, bei denen zwei Spieler",
          null, 0, null);
      descMinMax1
          .addCodeLine(
              "abwechselnd am Zug sind. Den Blätter des Suchbaums wird ein Wert zugewiesen,",
              null, 0, null);
      descMinMax1
          .addCodeLine(
              "der angibt, welcher Spieler das Spiel bei einem entsprechenden Spielverlauf",
              null, 0, null);
      descMinMax1
          .addCodeLine(
              "(entlang des Pfads von der Wurzel bis zum Blatt) für sich entscheiden konnte.",
              null, 0, null);
      lang.nextStep();
      descMinMax1.addCodeLine("", null, 0, null);
      descMinMax1
          .addCodeLine(
              "Ein Blatt, das einen Gewinn des Spielers A angibt, könnte mit +∞ markiert werden,",
              null, 0, null);
      descMinMax1
          .addCodeLine(
              "während ein Blatt, das einen Gewinn des Spielers B angibt, mit -∞ markiert werden",
              null, 0, null);
      descMinMax1
          .addCodeLine(
              "könnte. Spieler A versucht, den Ergebniswert des Spiels zu maximieren",
              null, 0, null);
      descMinMax1
          .addCodeLine(
              "und wird deshalb MAX-Spieler genannt, wohingegen Spieler B versucht,",
              null, 0, null);
      descMinMax1
          .addCodeLine(
              "den Ergebniswert des Spiels zu minimieren und deshalb MIN-Spieler genannt wird.",
              null, 0, null);
      lang.nextStep();
      descMinMax1.addCodeLine("", null, 0, null);
      descMinMax1
          .addCodeLine(
              "In der Praxis ist der vollständige Aufbau eines Suchbaums bei den meisten Spielen",
              null, 0, null);
      descMinMax1
          .addCodeLine(
              "zu rechenaufwändig und daher oft nicht möglich. Deshalb begnügt man sich damit,",
              null, 0, null);
      descMinMax1.addCodeLine(
          "den Suchbaum nur bis zu einer vorgegebenen Suchtiefe aufzubauen.",
          null, 0, null);
      descMinMax1
          .addCodeLine(
              "Sehr gute Spielpositionen für A erhalten sehr hohe Werte, sehr gute",
              null, 0, null);
      descMinMax1.addCodeLine(
          "Spielpositionen für B erhalten sehr niedrige Werte.", null, 0, null);
      descMinMax1
          .addCodeLine(
              "Zur Ermittlung der Werte bedient man sich Heuristiken zur Schätzung.",
              null, 0, null);

      lang.nextStep("Idee des Minimax-Algorithmus I");
      descMinMax1.hide();

      SourceCode descMinMax2 = lang.newSourceCode(new Offset(5, 15, header,
          "SW"), "description", null, continuousTextProps);
      descMinMax2.addCodeLine("Idee des Minimax-Algorithmus:", null, 0, null);
      descMinMax2.addCodeLine("", null, 0, null);
      descMinMax2.addCodeLine(
          "Der Minimax-Algorithmus findet seine Anwendung bei Spielen,", null,
          0, null);
      descMinMax2.addCodeLine("die folgende Eigenschaften erfüllen:", null, 0,
          null);
      descMinMax2.addCodeLine("", null, 0, null);
      descMinMax2.addCodeLine(
          "‣ es handelt sich um Nullsummenspielen, also Spiele, bei denen",
          null, 1, null);
      descMinMax2.addCodeLine(
          "die Summe der Gewinne und Verluste aller Spieler zusammengenommen",
          null, 1, null);
      descMinMax2
          .addCodeLine(
              "gleich Null ist. Die Niederlage des Gegners ist dabei der eigene Gewinn.",
              null, 1, null);
      descMinMax2.addCodeLine("", null, 0, null);
      descMinMax2.addCodeLine("‣ Spiele, die nicht vom Zufall abhängig sind",
          null, 1, null);
      descMinMax2.addCodeLine("(im Gegensatz zu z. B. Würfelspiele)", null, 1,
          null);
      descMinMax2.addCodeLine("", null, 0, null);
      descMinMax2
          .addCodeLine(
              "‣ offene Spiele, also Spiele, bei denen beiden Spielern in jeder Spielsituation",
              null, 1, null);
      descMinMax2.addCodeLine(
          "alle Zugmöglichkeiten des jeweiligen Gegenspielers bekannt sind",
          null, 1, null);
      descMinMax2.addCodeLine("(im Gegensatz zu z. B. Kartenspielen)", null, 1,
          null);
      descMinMax2.addCodeLine("", null, 0, null);
      descMinMax2.addCodeLine("", null, 0, null);
      descMinMax2
          .addCodeLine(
              "Diese Voraussetzungen treffen auf Spiele wie Schach, Go, Reversi, Dame,",
              null, 0, null);
      descMinMax2
          .addCodeLine(
              "Mühle oder Vier gewinnt zu. Die optimale Spielstrategie ist dann gefunden,",
              null, 0, null);
      descMinMax2.addCodeLine(
          "wenn sie zum bestmöglichen Ergebnis für einen Spieler führt,", null,
          0, null);
      descMinMax2.addCodeLine(
          "wenn man von optimaler Spielweise des Gegners ausgeht.", null, 0,
          null);

      lang.nextStep("Idee des Minimax-Algorithmus II");
      descMinMax2.hide();

      SourceCode descAlphaBeta = lang.newSourceCode(new Offset(5, 15, header,
          "SW"), "description", null, continuousTextProps);
      descAlphaBeta.addCodeLine("Idee der Alpha-Beta-Suche:", null, 0, null);
      lang.nextStep();
      descAlphaBeta.addCodeLine("", null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "Die Alpha-Beta-Suche ist eine optimierte Variante des Minimax-Algorithmus.",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "Der Minimax-Algorithmus analysiert den vollständigen Suchbaum. Dabei werden",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "aber auch Knoten betrachtet, die für die Wahl des Spielzuges nicht relevant sind.",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "Die Alpha-Beta-Suche versucht, möglichst viele dieser Knoten zu ignorieren.",
              null, 0, null);
      descAlphaBeta.addCodeLine(
          "Dieses Abschneiden von Teilbäumen bezeichnet man als Pruning.",
          null, 0, null);
      lang.nextStep();
      descAlphaBeta.addCodeLine("", null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "Die Idee ist, dass zwei Werte α und β weitergereicht werden, die das",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "Worst-Case-Szenario der Spieler beschreiben. Der α-Wert ist das Ergebnis,",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "das der MAX-Spieler mindestens erreichen wird, der β-Wert ist das Ergebnis,",
              null, 0, null);
      descAlphaBeta.addCodeLine(
          "das der MIN-Spieler höchstens erreichen wird.", null, 0, null);
      lang.nextStep();
      descAlphaBeta.addCodeLine("", null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "Besitzt ein MAX-Knoten einen Zug, dessen Rückgabe den β-Wert überschreitet,",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "wird die Suche in diesem Knoten abgebrochen. Bei diesem Pruning handelt es sich",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "um einen β-Cutoff, denn der MIN-Spieler würde dem MAX-Spieler diese Variant",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "gar nicht erst anbieten, weil sie sein bisheriges Höchst-Zugeständnis",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "überschreiten würde. Liefert der Zug stattdessen ein Ergebnis, das den",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "momentanen α-Wert übersteigt, wird dieser entsprechend nach oben korrigiert.",
              null, 0, null);
      lang.nextStep();
      descAlphaBeta.addCodeLine("", null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "Analoges gilt für die MIN-Knoten, wobei bei Werten abgebrochen wird,",
              null, 0, null);
      descAlphaBeta
          .addCodeLine(
              "die kleiner als der α-Wert sind (α-Cutoff) und der β-Wert gegebenenfalls",
              null, 0, null);
      descAlphaBeta.addCodeLine("nach unten korrigiert wird.", null, 0, null);

      lang.nextStep("Idee der Alpha-Beta-Suche");
      descAlphaBeta.hide();
    }

    /*
     * create graph
     */
    graphNodeCount = root.getNodeCount();
    adjacencyMatrix = new int[graphNodeCount][graphNodeCount];
    coords = new Coordinates[graphNodeCount];
    labels = new String[graphNodeCount];
    intervals = new Text[graphNodeCount];
    values = new Text[graphNodeCount];
    buildGraph(new Coordinates(60, 100), GRAPH_HORIZONTAL_DISTANCE,
        GRAPH_VERTICAL_DISTANCE, root, 0, 0, adjacencyMatrix, coords, labels);

    graph = lang.newGraph("graph", adjacencyMatrix, coords, labels, null,
        graphProps);

    /*
     * alpha-beta-interval and node value labels
     */
    for (int i = 0; i < coords.length; i++) {
      intervals[i] = lang.newText(new Coordinates(coords[i].getX()
          + GRAPH_LABEL_CENTER, coords[i].getY() - GRAPH_LABEL_ASIDE),
          "-------", "interval_node" + i, null, intervalTextProps);

      values[i] = lang.newText(new Coordinates(coords[i].getX()
          + GRAPH_LABEL_CENTER, coords[i].getY() - GRAPH_LABEL_ASIDE), "-",
          "value_node" + i, null, valueTextProps);
    }

    /*
     * min/max level labels
     */
    TextProperties levelTextProps = new TextProperties();
    levelTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        continuousTextProps.get(AnimationPropertiesKeys.FONT_PROPERTY));
    levelTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        continuousTextProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    for (int i = 0, depth = root.getDepth(); i < depth - 1; i++)
      lang.newText(new Offset(-30, i * GRAPH_VERTICAL_DISTANCE
          + GRAPH_LABEL_CENTER, graph, "NW"), i % 2 == 0 ? "MAX" : "MIN",
          "level" + i + "_label", null, levelTextProps);

    /*
     * source code
     */
    maxCode = lang.newSourceCode(new Offset(20, -20, graph, "NE"), "max", null,
        sourceCodeProps);
    maxCode.addCodeLine("max(u: node, α, β: int)", null, 0, null);
    maxCode.addCodeLine("if not leaf(u)", null, 1, null);
    maxCode.addCodeLine("for each successor v of u", null, 2, null);
    maxCode.addCodeLine("α := maximum(α, min(v, α, β))", null, 3, null);
    maxCode.addCodeLine("if α ≥ β", null, 3, null);
    maxCode.addCodeLine("break", null, 4, null);
    maxCode.addCodeLine("value(u) := α", null, 2, null);

    minCode = lang.newSourceCode(new Offset(0, 10, maxCode, "SW"), "min", null,
        sourceCodeProps);
    minCode.addCodeLine("min(u: node, α, β: int)", null, 0, null);
    minCode.addCodeLine("if not leaf(u)", null, 1, null);
    minCode.addCodeLine("for each successor v of u", null, 2, null);
    minCode.addCodeLine("β := minimum(β, max(v, α, β))", null, 3, null);
    minCode.addCodeLine("if α ≥ β", null, 3, null);
    minCode.addCodeLine("break", null, 4, null);
    minCode.addCodeLine("value(u) := β", null, 2, null);

    /*
     * value initialization
     */
    lang.nextStep("Initialisierung");
    statusTextProps.set(
        AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(((Font) statusTextProps
            .get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(),
            Font.PLAIN, 12));
    Primitive init = lang.newText(new Offset(0, 50, graph, "SW"),
        "→ value(u) ist für alle Blätter u mit ihrem Wert initialisiert",
        "init", null, statusTextProps);

    findLeafs: for (int i = 0; i < graphNodeCount; i++) {
      for (int k = i; k < graphNodeCount; k++)
        if (adjacencyMatrix[i][k] == 1)
          continue findLeafs;

      values[i].setText(labels[i], null, null);
      values[i].show();
    }

    /*
     * run algorithm
     */
    lang.nextStep();
    Primitive start = lang.newText(new Offset(0, 10, init, "SW"),
        "→ starte mit max(" + labels[0] + ", -∞, +∞)", "start", null,
        statusTextProps);

    chosenPath = new int[graphNodeCount];
    for (int i = 0; i < graphNodeCount; i++)
      chosenPath[i] = -1;
    initVisitNodeText();

    max(0, Integer.MIN_VALUE, Integer.MAX_VALUE);

    lang.nextStep();
    for (int i = 0; chosenPath[i] != -1; i = chosenPath[i])
      graph.highlightEdge(i, chosenPath[i], null, null);
    lang.newText(new Offset(0, 10, start, "SW"),
        "→ optimale Spielstrategie gefunden", "end", null, statusTextProps);
    lang.nextStep("Optimale Spielstrategie gefunden");

    /*
     * description
     */
    lang.addLine("hideAll"); // lang.hideAllPrimitives creates a new step
    header.show();
    headerText1.show();
    headerText2.show();
    SourceCode conclusion = lang.newSourceCode(new Offset(5, 15, header, "SW"),
        "conclusion", null, continuousTextProps);
    conclusion
        .addCodeLine(
            "Mit einem durchschnittlichen Branching-Factor (Anzahl der Kinder eines Knotens) b",
            null, 0, null);
    conclusion.addCodeLine("und einer Suchtiefe n gilt:", null, 0, null);
    conclusion.addCodeLine("", null, 0, null);
    conclusion
        .addCodeLine(
            "Falls die Reihenfolge, in der die Kinder eines Knotens untersucht werden,",
            null, 0, null);
    conclusion
        .addCodeLine(
            "pessimal ist (worst-case), liegt die Anzahl besuchter Blattknoten in O(b^n),",
            null, 0, null);
    conclusion.addCodeLine("wie bei unoptimierter Minimax-Suche.", null, 0,
        null);
    conclusion.addCodeLine("", null, 0, null);
    conclusion
        .addCodeLine(
            "Falls die Reihenfolge, in der die Kinder eines Knotens untersucht werden,",
            null, 0, null);
    conclusion
        .addCodeLine(
            "optimal ist (best-case), also die besten Züge immer zuerst untersucht werden,",
            null, 0, null);
    conclusion.addCodeLine(
        "liegt die Anzahl besuchter Blattknoten in O(√(b^n)).", null, 0, null);
    conclusion
        .addCodeLine(
            "Das heißt, Alpha-Beta-Suche kann einen Suchbaum in einem festgelegten Zeitraum",
            null, 0, null);
    conclusion.addCodeLine(
        "doppelt so tief durchsuchen wie eine Minimax-Suche.", null, 0, null);
    lang.nextStep("Schlussbemerkung");
  }

  /**
   * Inserts a cutoff visualization
   * 
   * @param nodeIndex
   *          the node index whose children are (partially) cut off
   * @param childIndex
   *          the last child <em>not</em> cut off
   * @param string
   *          the string that should appear under the cutoff visualization
   */
  private void insertCutoff(int nodeIndex, int childIndex, String string) {
    // find the first child that is actually cut off
    // it is the first child of the node with nodeIndex
    // following the node with childIndex
    int childIndex2 = childIndex;
    for (int i = childIndex2 + 1; i <= graphNodeCount; i++)
      if (i == graphNodeCount)
        // no following children present that were cut off
        return;
      else if (adjacencyMatrix[nodeIndex][i] == 1) {
        childIndex2 = i;
        break;
      }

    // find the last node descending the node with nodeIndex
    // (that is cut off)
    // it is the last node that is not linked to a node
    // with a lower index than nodeIndex
    int childIndexLast = childIndex2 + 1;
    findLastCutoffNode: while (childIndexLast < graphNodeCount) {
      for (int i = 0; i < nodeIndex; i++)
        if (adjacencyMatrix[childIndexLast][i] == 1)
          break findLastCutoffNode;
      childIndexLast++;
    }
    childIndexLast--;

    // position round rectangle polygon centered over all nodes cut off
    int maxXcoord = Integer.MIN_VALUE, maxYcoord = Integer.MIN_VALUE;
    int minXcoord = Integer.MAX_VALUE, minYcoord = Integer.MAX_VALUE;
    for (int i = childIndex2; i <= childIndexLast; i++) {
      maxXcoord = Math.max(maxXcoord, coords[i].getX() + GRAPH_LABEL_ASIDE);
      maxYcoord = Math.max(maxYcoord, coords[i].getY() + GRAPH_LABEL_ASIDE);
      minXcoord = Math.min(minXcoord, coords[i].getX() - GRAPH_LABEL_ASIDE);
      minYcoord = Math.min(minYcoord, coords[i].getY() - GRAPH_LABEL_ASIDE);
    }

    Coordinates upperLeft = new Coordinates(minXcoord + GRAPH_LABEL_CENTER,
        minYcoord + 2 * GRAPH_LABEL_CENTER);
    Coordinates lowerRight = new Coordinates(maxXcoord + GRAPH_LABEL_CENTER,
        maxYcoord + 2 * GRAPH_LABEL_CENTER);

    List<Node> nodes = new LinkedList<Node>();
    for (int i = 0; i <= ROUND_RECT_STEP_COUNT; i++)
      nodes.add(new Coordinates(lowerRight.getX()
          + (int) (Math.cos(Math.PI / 2 * i / ROUND_RECT_STEP_COUNT)
              * GRAPH_LABEL_ASIDE - GRAPH_LABEL_ASIDE), upperLeft.getY()
          - (int) (Math.sin(Math.PI / 2 * i / ROUND_RECT_STEP_COUNT)
              * GRAPH_LABEL_ASIDE - GRAPH_LABEL_ASIDE)));
    for (int i = 0; i <= ROUND_RECT_STEP_COUNT; i++)
      nodes.add(new Coordinates(upperLeft.getX()
          + (int) (Math.sin(-Math.PI / 2 * i / ROUND_RECT_STEP_COUNT)
              * GRAPH_LABEL_ASIDE + GRAPH_LABEL_ASIDE), upperLeft.getY()
          - (int) (Math.cos(-Math.PI / 2 * i / ROUND_RECT_STEP_COUNT)
              * GRAPH_LABEL_ASIDE - GRAPH_LABEL_ASIDE)));
    for (int i = 0; i <= ROUND_RECT_STEP_COUNT; i++)
      nodes.add(new Coordinates(upperLeft.getX()
          + (int) (-Math.cos(Math.PI / 2 * i / ROUND_RECT_STEP_COUNT)
              * GRAPH_LABEL_ASIDE + GRAPH_LABEL_ASIDE), lowerRight.getY()
          - (int) (-Math.sin(Math.PI / 2 * i / ROUND_RECT_STEP_COUNT)
              * GRAPH_LABEL_ASIDE + GRAPH_LABEL_ASIDE)));
    for (int i = 0; i <= ROUND_RECT_STEP_COUNT; i++)
      nodes.add(new Coordinates(lowerRight.getX()
          + (int) (-Math.sin(-Math.PI / 2 * i / ROUND_RECT_STEP_COUNT)
              * GRAPH_LABEL_ASIDE - GRAPH_LABEL_ASIDE), lowerRight.getY()
          - (int) (-Math.cos(-Math.PI / 2 * i / ROUND_RECT_STEP_COUNT)
              * GRAPH_LABEL_ASIDE + GRAPH_LABEL_ASIDE)));

    try {
      lang.newPolygon(nodes.toArray(new Node[nodes.size()]), "cutoff_node"
          + nodeIndex, null, cutoffAreaProps);
    } catch (NotEnoughNodesException e) {
    }

    // position text beneath the ellipse
    lang.newText(new Coordinates(upperLeft.getX() / 2 + lowerRight.getX() / 2,
        lowerRight.getY() + 2), string, "cutoff_text" + nodeIndex, null,
        cutoffTextProps);
  }

  /**
   * the "max" function for the alpha-beta-search
   * 
   * @param nodeIndex
   *          the current node
   * @param alpha
   *          the alpha value
   * @param beta
   *          the beta value
   * @return the evaluation value for this node
   */
  private int max(int nodeIndex, int alpha, int beta) {
    // determine if node is a leaf
    boolean isLeaf = true;
    for (int i = nodeIndex; i < graphNodeCount; i++)
      if (adjacencyMatrix[nodeIndex][i] == 1) {
        isLeaf = false;
        break;
      }

    // show interval values
    graph.highlightNode(nodeIndex, null, null);
    int alpha2 = alpha;
    intervals[nodeIndex].setText(intervalText(alpha2, beta), null, null);
    intervals[nodeIndex].show();

    // line 0
    maxCode.highlight(0);
    lang.nextStep(visitNodeText(nodeIndex, isLeaf));

    // line 1
    maxCode.unhighlight(0);
    maxCode.highlight(1);
    lang.nextStep();

    if (isLeaf) {
      // leave function
      graph.unhighlightNode(nodeIndex, null, null);
      intervals[nodeIndex].hide();
      maxCode.unhighlight(1);
      try {
        return Integer.parseInt(labels[nodeIndex]);
      } catch (NumberFormatException e) {
        throw new NumberFormatException("leaf node must contain integer value");
      }
    }

    // line 2
    maxCode.unhighlight(1);
    maxCode.highlight(2);
    lang.nextStep();

    for (int i = nodeIndex; i <= graphNodeCount; i++)
      if (i == graphNodeCount)
        maxCode.unhighlight(2);
      else if (adjacencyMatrix[nodeIndex][i] == 1) {
        // line 3
        maxCode.unhighlight(2);
        maxCode.highlight(3);
        lang.nextStep();

        // call min function
        maxCode.unhighlight(3);
        graph.unhighlightNode(nodeIndex, null, null);
        int newalpha = min(i, alpha2, beta);
        if (newalpha > alpha2) {
          alpha2 = newalpha;
          chosenPath[nodeIndex] = i;
        }

        // line 3 (after returning from min function)
        graph.highlightNode(nodeIndex, null, null);
        maxCode.highlight(3);
        lang.nextStep(visitNodeText(nodeIndex, isLeaf));

        // line 4
        intervals[nodeIndex].setText(intervalText(alpha2, beta), null, null);
        maxCode.unhighlight(3);
        maxCode.highlight(4);
        lang.nextStep();

        maxCode.unhighlight(4);
        if (alpha2 >= beta) {
          // line 5
          maxCode.highlight(5);
          lang.nextStep();

          maxCode.unhighlight(5);
          insertCutoff(nodeIndex, i, "β-Cutoff");
          break;
        }

        // line 2 (repeat loop)
        maxCode.highlight(2);
        lang.nextStep();
      }

    // line 6
    maxCode.highlight(6);
    lang.nextStep();

    // leave function
    maxCode.unhighlight(6);
    graph.unhighlightNode(nodeIndex, null, null);
    values[nodeIndex].setText(String.valueOf(alpha2), null, null);
    values[nodeIndex].show();
    return alpha2;
  }

  /**
   * the "min" function for the alpha-beta-search
   * 
   * @param nodeIndex
   *          the current node
   * @param alpha
   *          the alpha value
   * @param beta
   *          the beta value
   * @return the evaluation value for this node
   */
  private int min(int nodeIndex, int alpha, int beta) {
    // determine if node is a leaf
    boolean isLeaf = true;
    for (int i = nodeIndex; i < graphNodeCount; i++)
      if (adjacencyMatrix[nodeIndex][i] == 1) {
        isLeaf = false;
        break;
      }

    // show interval values
    graph.highlightNode(nodeIndex, null, null);
    int beta2 = beta;
    intervals[nodeIndex].setText(intervalText(alpha, beta2), null, null);
    intervals[nodeIndex].show();

    // line 0
    minCode.highlight(0);
    lang.nextStep(visitNodeText(nodeIndex, isLeaf));

    // line 1
    minCode.unhighlight(0);
    minCode.highlight(1);
    lang.nextStep();

    if (isLeaf) {
      // leave function
      graph.unhighlightNode(nodeIndex, null, null);
      intervals[nodeIndex].hide();
      minCode.unhighlight(1);
      try {
        return Integer.parseInt(labels[nodeIndex]);
      } catch (NumberFormatException e) {
        throw new NumberFormatException("leaf node must contain integer value");
      }
    }

    // line 2
    minCode.unhighlight(1);
    minCode.highlight(2);
    lang.nextStep();

    for (int i = nodeIndex; i <= graphNodeCount; i++)
      if (i == graphNodeCount)
        minCode.unhighlight(2);
      else if (adjacencyMatrix[nodeIndex][i] == 1) {
        // line 3
        minCode.unhighlight(2);
        minCode.highlight(3);
        lang.nextStep();

        // call max function
        minCode.unhighlight(3);
        graph.unhighlightNode(nodeIndex, null, null);
        int newbeta = max(i, alpha, beta2);
        if (newbeta < beta2) {
          beta2 = newbeta;
          chosenPath[nodeIndex] = i;
        }

        // line 3 (after returning from max function)
        graph.highlightNode(nodeIndex, null, null);
        minCode.highlight(3);
        lang.nextStep(visitNodeText(nodeIndex, isLeaf));

        // line 4
        intervals[nodeIndex].setText(intervalText(alpha, beta2), null, null);
        minCode.unhighlight(3);
        minCode.highlight(4);
        lang.nextStep();

        minCode.unhighlight(4);
        if (alpha >= beta2) {
          // line 5
          minCode.highlight(5);
          lang.nextStep();

          minCode.unhighlight(5);
          insertCutoff(nodeIndex, i, "α-Cutoff");
          break;
        }

        // line 2 (repeat loop)
        minCode.highlight(2);
        lang.nextStep();
      }

    // line 6
    minCode.highlight(6);
    lang.nextStep();

    // leave function
    minCode.unhighlight(6);
    graph.unhighlightNode(nodeIndex, null, null);
    values[nodeIndex].setText(String.valueOf(beta2), null, null);
    values[nodeIndex].show();
    return beta2;
  }

  /**
   * Creates a string containing the given alpha and beta values
   */
  private String intervalText(int alpha, int beta) {
    String text = alpha == Integer.MIN_VALUE ? "-∞"
        : alpha < 0 || alpha > 9 ? String.valueOf(alpha) : " " + alpha;

    text += "   ";

    text += beta == Integer.MAX_VALUE ? "+∞" : beta < 0 || beta > 9 ? String
        .valueOf(beta) : beta + " ";

    return text;
  }

  /**
   * Creates a text expressing that the given inner node or leaf node is to be
   * examined. {@link #initVisitNodeText()} must be called when beginning
   * searching a new tree
   */
  private String visitNodeText(int nodeIndex, boolean isLeaf) {
    return isLeaf ? "Untersuche Blattknoten #" + (++visitedLeafsCount)
        : "Untersuche Knoten " + labels[nodeIndex];
  }

  /**
   * Initializes the text creation for expressing which node is to be examined.
   * This method must be called when beginning searching a new tree in order to
   * get correct result from {@link #visitNodeText(int, boolean)}
   */
  private void initVisitNodeText() {
    visitedLeafsCount = 0;
  }

  /** the count of already visited leaf nodes */
  private int visitedLeafsCount;

  public String getName() {
    return "Alpha-Beta-Suche";
  }

  public String getAlgorithmName() {
    return "Alpha-Beta-Suche";
  }

  public String getAnimationAuthor() {
    return "Pascal Weisenburger";
  }

  public String getDescription() {
    return "Die Alpha-Beta-Suche ist ein Algorithmus zur Ermittlung der optimalen\n"
        + "Spielstrategie f&uuml;r Nullsummenspiele, bei denen zwei gegnerische Spieler\n"
        + "abwechselnd Z&uuml;ge ausf&uuml;hren.\n"
        + "Der MAX-Spieler versucht dabei, den Wert eines Knotens zu maximieren,\n"
        + "wohingegen der MIN-Spieler versucht, den Wert eines Knotens zu minimieren.<br>\n"
        + "<br>\n"
        + "W&auml;hrend der Suche werden zwei Werte &alpha; und &beta; aktualisiert, die angeben,\n"
        + "welches Ergebnis die Spieler bei optimaler Spielweise erzielen k&ouml;nnen.\n"
        + "Mit Hilfe dieser Werte kann entschieden werden, welche Teile des Suchbaumes\n"
        + "nicht untersucht werden m&uuml;ssen, weil sie das Ergebnis der Probleml&ouml;sung\n"
        + "nicht beeinflussen k&ouml;nnen (Pruning).<br>\n"
        + "<br>\n"
        + "Die Alpha-Beta-Suche ist eine optimierte Variante der Minimax-Suche,\n"
        + "die ohne Pruning arbeitet.<br>\n"
        + "<br>\n"
        + "\n"
        + "<a href=\"http://de.wikipedia.org/wiki/Alpha-Beta-Suche\">http://de.wikipedia.org/wiki/Alpha-Beta-Suche</a><br>\n"
        + "<a href=\"http://de.wikipedia.org/wiki/Minimax-Algorithmus\">http://de.wikipedia.org/wiki/Minimax-Algorithmus</a>";
  }

  public String getCodeExample() {
    return "max(u: node, &alpha;, &beta;: int)\n" + "  if not leaf(u)\n"
        + "    for each successor v of u\n"
        + "      &alpha; := maximum(&alpha;, min(v, &alpha;, &beta;))\n"
        + "      if &alpha; &ge; &beta;\n" + "        break\n"
        + "    value(u) := &alpha;\n" + "\n"
        + "min(u: node, &alpha;, &beta;: int)\n" + "  if not leaf(u)\n"
        + "    for each successor v of u\n"
        + "      &beta; := minimum(&beta;, max(v, &alpha;, &beta;))\n"
        + "      if &alpha; &ge; &beta;\n" + "        break\n"
        + "    value(u) := &beta;";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }
}