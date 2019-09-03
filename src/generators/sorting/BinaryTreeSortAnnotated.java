/**
 * 
 */
package generators.sorting;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author Tim Grube, Jan H. Post
 * 
 */
public class BinaryTreeSortAnnotated extends AnnotatedAlgorithm implements
    Generator {

  Language                    lang;

  private static final String algname             = "BinaryTreeSort";
  private static final String author              = "Jan H. Post, Tim Grube";
  // private final static String file_extension = "asu";

  SourceCode                  sc_insert;
  SourceCode                  sc_getinorder;
  SourceCode                  com_insert;
  SourceCode                  com_getinorder;
  SourceCode                  com_sort;

  GraphProperties             graphProps;
  Graph                       graphTree;
  ArrayProperties             arrayProps;

  int[][]                     adjacencyMatrix;
  Node[]                      nodes;
  String[]                    labels;

  List<Integer>               globalArray;

  // Eigenschaften fuer den Generator:
  private Color               array_cellhighlight = Color.YELLOW;
  private Color               sc_highlight        = Color.RED;

  private static final String code_example        = "public int[] binarytreesort(int[] array)\n"
                                                      + "{\n"
                                                      + "\t BinaryTree tree = new BinaryTree();"
                                                      + "\n"
                                                      + "\t for(int i = 0; i < array.length; i++)\n"
                                                      + "\t {\n"
                                                      + "\t\t insert(array[i], tree);\n"
                                                      + "\t }\n"
                                                      + "\n"
                                                      + "\t int[] sorted_array = tree.getInorder();\n"
                                                      + "\t return sorted_array;\n"
                                                      + "}\n"
                                                      + "\n\n"
                                                      + "private void insert(int insertkey, BinaryTree subtree)\n"
                                                      + "{\n"
                                                      + "\t if(subtree.key == null)\n"
                                                      + "\t {\n"
                                                      + "\t\t subtree.key = insertkey;\n"
                                                      + "\t }\n"
                                                      + "\t else\n"
                                                      + "\t {\n"
                                                      + "\t\t if(subtree.key < insertkey)\n"
                                                      + "\t\t {\n"
                                                      + "\t\t\t insert(insertkey, subtree.leftchild);\n"
                                                      + "\t\t }\n"
                                                      + "\t\t else\n"
                                                      + "\t\t {\n"
                                                      + "\t\t\t insert(insertkey, subtree.rightchild);\n"
                                                      + "\t\t }\n"
                                                      + "\t }\n"
                                                      + "}\n"
                                                      + "\n\n"
                                                      + "private int[] getInorder(BinaryTree subtree, List<int> list)\n"
                                                      + "{\n"
                                                      + "\t if(subtree.leftchild != null)\n"
                                                      + "\t {\n"
                                                      + "\t\t list = getInorder(subtree.leftchild, list);\n"
                                                      + "\t }\n"
                                                      + "\n"
                                                      + "\t list.add(subtree.key);\n"
                                                      + "\n"
                                                      + "\t if(subtree.rightchild != null)\n"
                                                      + "\t {\n"
                                                      + "\t\t list = getInorder(subtree.rightchild, list);\n"
                                                      + "\t }\n"
                                                      + "\t return list.toArray();\n"
                                                      + "}";

  private static final String description         = "Binary Tree Sort ist ein Sortieralgorithmus, "
                                                      + "der eine Menge von Elementen sortiert,\n"
                                                      + "indem er alle Elemente in einen binären Suchbaum einfuegt\n"
                                                      + "und diesen anschliessend in-order durchlaeuft.\n"
                                                      + "Dabei werden alle Elemente in sortierter Reihenfolge angetroffen.\n"
                                                      + "Hierzu werden folgende Eigenschaften genutzt:\n"
                                                      + "1. Binäresuchbaumeigenschaft: In jedem binären Suchbaum sind die Elemente gemäß einer totalen Ordnung angeordnet.\n"
                                                      + "\t Entspricht diese Ordnung der gewünschten Sortierordnung, so laesst sich mit Hilfe der In-Order-Traversierung die \n"
                                                      + "\t Eingabemenge in gewuenschter, sortierter Reihenfolge auslesen.\n"
                                                      + "2. In-Order-Traversierung: Die In-Order-Traversierung liest einen binären Suchbaum gemäß der verwendeten Ordnung aus.\n"
                                                      + "\t Hierzu wird bei verwendeter \"<\"-Ordnung so lange zum linken Kindknoten abgestiegen bis kein weiteres linkes Kind\n"
                                                      + "\t existiert. Dies ist das kleinste Element im Baum. Von diesem aus wird nach dem rechten Kind geguckt, ist dies\n"
                                                      + "\t vorhanden, so wird der rechte Kindbaum wieder nach linken Kindern durchsucht. Dies wird rekursiv durchgefuehrt, bis\n"
                                                      + "\t kein rechtes Kind mehr existiert. Dann wird wieder eine Ebene nach oben gestiegen, das Element in die angesammelte Liste\n"
                                                      + "\t eingefuegt und wieder zum rechten Kind abgestiegen.";

  private int[]               keys;

  private Font                header_font;
  private Color               sc_context;
  private Font                sc_font;
  private Color               sc_text;
  private Color               sc_com;

  private boolean             filtered_doubles;

  public BinaryTreeSortAnnotated(Language l) {
    lang = l;
    lang.setStepMode(true);
  }

  public BinaryTreeSortAnnotated() {
    this(new AnimalScript(algname, author, 640, 480));
  }

  /*
   * ******************************************************* *\ |
   * ******************************************************* | |
   * ******************************************************* | | ***** Animation
   * des Algorithmus ***** | |
   * ******************************************************* | |
   * ******************************************************* | \*
   * *******************************************************
   */

  /**
   * Bereitet die Animation vor und erstellt Sourcecode/Kommentarobjekte
   * 
   * @param key
   *          zu sortierendes array
   */
  private void prepareScreen(int[] keys) {

    // Definiere die Ueberschrift
    TextProperties txtProps = new TextProperties();
    txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, header_font);

    @SuppressWarnings("unused")
    Text txt = lang.newText(new Coordinates(40, 30), "Binary Tree Sort",
        "header", null, txtProps);

    // Rahmen um die Ueberschrift
    RectProperties rectProps = new RectProperties();
    @SuppressWarnings("unused")
    Rect rect = lang.newRect(new Offset(-5, -5, "header",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header",
        AnimalScript.DIRECTION_SE), "hRect", null, rectProps);

    // Definiere die Warnung bei doppelten Eintraegen

    // Definiere das Aussehen der Warnung
    SourceCodeProperties doubleProps = new SourceCodeProperties();
    doubleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, header_font);
    doubleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    // Codeobjekt für Insert
    SourceCode doubles = lang.newSourceCode(new Coordinates(40, 270), "double",
        null, doubleProps);

    doubles.addCodeLine("Doppelte Elemente wurden", null, 0, null); // 1
    doubles.addCodeLine("aufgund möglicher", null, 0, null);
    doubles.addCodeLine("Anzeigefehler herausgefiltert.", null, 0, null);

    if (!filtered_doubles) {
      doubles.hide();
    }

    // erstelle und positioniere code und kommentarobjekte
    create_code_comment();

    // fuelle code und kommentarobjekte
    fill_code_comment();

    lang.nextStep();

    if (filtered_doubles) {
      doubles.hide();
    }

    // erstelle eine Liste mit den zu sortierenden Objekten
    ArrayList<Integer> myList = new ArrayList<Integer>();
    for (int i : keys) {
      myList.add(i);
    }

    // sortiere und animiere den sortiervorgang
    sort(myList);

    lang.nextStep();

  }

  /**
   * Erstelle und positioniere Code und Kommentarobjekte
   */
  private void create_code_comment() {
    // Definiere das Aussehen des Source-Codes
    SourceCodeProperties scProps = new SourceCodeProperties();

    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, sc_context);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sc_font);
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, sc_highlight);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sc_text);

    // Codeobjekt für Insert
    sc_insert = lang.newSourceCode(new Coordinates(500, 270), "insert_code",
        null, scProps);

    // Codeobjekt für Inorder
    sc_getinorder = lang.newSourceCode(new Coordinates(900, 270),
        "inorder_code", null, scProps);

    // Definiere das Aussehen des Kommentar-Codes
    SourceCodeProperties comProps = new SourceCodeProperties();
    comProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, sc_context);
    comProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sc_font);
    comProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, sc_highlight);
    comProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sc_com);

    // Zeichne das Insert-Kommentar-Code Objekt
    com_insert = lang.newSourceCode(new Coordinates(500, 270),
        "comment_insert", null, comProps);
    // Zeichne das In-Order-Order-Kommentar-Code Objekt
    com_getinorder = lang.newSourceCode(new Coordinates(900, 270),
        "comment_inorder", null, comProps);
    // Zeichne das Sort-Kommentar-Code Objekt
    com_sort = lang.newSourceCode(new Coordinates(500, 50), "comment_sort",
        null, comProps);
  }

  /**
   * Fuellt die Code- und Kommentarobjekte
   */
  private void fill_code_comment() {
    // erstelle den Animationscode fuer Insert
    // text, name, indentation, display delay
    // mit /**/ beginnende zeilen sind leerzeilen in
    // denen ein kommentar steht

    /**/sc_insert.addCodeLine("", null, 0, null); // 0
    sc_insert.addCodeLine("public void insert(int insertKey) {", null, 0, null); // 1
    /**/sc_insert.addCodeLine("", null, 0, null); // 2
    sc_insert.addCodeLine("if (this == null) {", null, 1, null); // 3
    /**/sc_insert.addCodeLine("", null, 0, null); // 4
    sc_insert.addCodeLine("new BinaryTree(insertKey)", null, 2, null); // 5
    sc_insert.addCodeLine("} else {", null, 1, null); // 6
    /**/sc_insert.addCodeLine("", null, 0, null); // 7
    sc_insert.addCodeLine("if (insertKey < key) {", null, 2, null); // 8
    /**/sc_insert.addCodeLine("", null, 0, null); // 9
    sc_insert.addCodeLine("if (left == null);", null, 3, null); // 10
    sc_insert.addCodeLine("left = new BinaryTree(insertKey);", null, 4, null); // 11
    sc_insert.addCodeLine("} else {", null, 3, null); // 12
    sc_insert.addCodeLine("left.insert(insertKey);", null, 4, null); // 13
    sc_insert.addCodeLine("}", null, 3, null); // 14
    /**/sc_insert.addCodeLine("", null, 0, null); // 15
    sc_insert.addCodeLine("} else {", null, 2, null); // 16
    /**/sc_insert.addCodeLine("", null, 0, null); // 17
    sc_insert.addCodeLine("if (right == null);", null, 3, null); // 18
    sc_insert.addCodeLine("right = new BinaryTree(insertKey);", null, 4, null); // 19
    sc_insert.addCodeLine("} else {", null, 3, null); // 20
    sc_insert.addCodeLine("right.insert(insertKey);", null, 4, null); // 21
    sc_insert.addCodeLine("}", null, 3, null); // 22
    sc_insert.addCodeLine("}", null, 2, null); // 23
    sc_insert.addCodeLine("}", null, 1, null); // 24
    sc_insert.addCodeLine("}", null, 0, null); // 25

    // erstelle den Animationscode fuer Insert
    // text, name, indentation, display delay
    // mit /**/ beginnende zeilen sind leerzeilen in
    // denen ein kommentar steht

    /**/sc_getinorder.addCodeLine("", null, 0, null); // 0
    sc_getinorder.addCodeLine("private List<Integer> getInorder("
        + "List<Integer> orderedList) {", null, 0, null); // 1
    /**/sc_getinorder.addCodeLine("", null, 0, null); // 2
    sc_getinorder.addCodeLine("if (this == null) {", null, 1, null); // 3
    sc_getinorder.addCodeLine("return orderedList;", null, 2, null); // 4
    sc_getinorder.addCodeLine("} else {", null, 1, null); // 5
    /**/sc_getinorder.addCodeLine("", null, 0, null); // 6
    sc_getinorder.addCodeLine("if (left != null) {", null, 2, null); // 7
    sc_getinorder.addCodeLine("left.getInorder(orderedList);", null, 3, null); // 8
    sc_getinorder.addCodeLine("}", null, 2, null); // 9
    /**/sc_getinorder.addCodeLine("", null, 0, null); // 10
    sc_getinorder.addCodeLine("orderedList.add(key);", null, 2, null); // 11
    /**/sc_getinorder.addCodeLine("", null, 0, null); // 12
    sc_getinorder.addCodeLine("if (right != null) {", null, 2, null); // 13
    sc_getinorder.addCodeLine("right.getInorder(orderedList);", null, 3, null); // 14
    sc_getinorder.addCodeLine("}", null, 2, null); // 15
    sc_getinorder.addCodeLine("return orderedList;", null, 2, null); // 16
    sc_getinorder.addCodeLine("}", null, 1, null); // 17
    sc_getinorder.addCodeLine("}", null, 0, null); // 18

    // erstelle den Animationscode fuer Insert
    // text, name, indentation, display delay
    // mit /**/ beginnende zeilen sind leerzeilen in
    // denen ein kommentar steht

    /*
     * Die Kommentare werden, wie bei normaler Programmierung, zwischen den
     * Codezeilen eingefuegt
     */

    // Fuege Zeile fuer Zeile des Kommentar-Codes ein.
    // Line, name, indentation, display dealy
    com_insert.addCodeLine("// Element insertKey in den Binaerbaum einfuegen",
        null, 0, null); // 0
    com_insert.addCodeLine("", null, 0, null); // 1
    com_insert.addCodeLine("// wenn der Baum leer ist ...", null, 1, null); // 2
    com_insert.addCodeLine("", null, 0, null); // 3
    com_insert.addCodeLine("// fuege das Element hier ein.", null, 2, null); // 4
    com_insert.addCodeLine("", null, 0, null); // 5
    com_insert.addCodeLine("", null, 0, null); // 6
    com_insert
        .addCodeLine("// wenn das Element in den linken Teilbaum gehoert ...",
            null, 2, null); // 7
    com_insert.addCodeLine("", null, 0, null); // 8
    com_insert.addCodeLine("// fuege es dort ein", null, 3, null); // 9
    com_insert.addCodeLine("", null, 0, null); // 10
    com_insert.addCodeLine("", null, 0, null); // 11
    com_insert.addCodeLine("", null, 0, null); // 12
    com_insert.addCodeLine("", null, 0, null); // 13
    com_insert.addCodeLine("", null, 0, null); // 14
    com_insert.addCodeLine("// ansonsten ...", null, 2, null); // 15
    com_insert.addCodeLine("", null, 0, null); // 16
    com_insert.addCodeLine("// ... fuege es in den rechten Teilbeum ein", null,
        3, null); // 17
    com_insert.addCodeLine("", null, 0, null); // 18
    com_insert.addCodeLine("", null, 0, null); // 19
    com_insert.addCodeLine("", null, 0, null); // 20
    com_insert.addCodeLine("", null, 0, null); // 21
    com_insert.addCodeLine("", null, 0, null); // 22

    // Fuege Zeile fuer Zeile des Kommentar-Codes ein.
    // Line, name, indentation, display dealy
    com_getinorder.addCodeLine("// Binaerbaum in-order traversieren", null, 0,
        null); // 0
    com_getinorder.addCodeLine("", null, 0, null); // 1
    com_getinorder.addCodeLine("// auf leeren Baum pruefen", null, 1, null); // 2
    com_getinorder.addCodeLine("", null, 0, null); // 3
    com_getinorder.addCodeLine("", null, 0, null); // 4
    com_getinorder.addCodeLine("", null, 0, null); // 5
    com_getinorder.addCodeLine("// zuerst linken Teilbaum traversieren", null,
        2, null); // 6
    com_getinorder.addCodeLine("", null, 0, null); // 7
    com_getinorder.addCodeLine("", null, 0, null); // 8
    com_getinorder.addCodeLine("", null, 0, null); // 9
    com_getinorder.addCodeLine(
        "// Wert des aktuellen Knotens in die Liste schreiben", null, 2, null); // 10
    com_getinorder.addCodeLine("", null, 0, null); // 11
    com_getinorder.addCodeLine("// dann rechten Teilbaum traversieren", null,
        2, null); // 12

    // Fuege Zeile fuer Zeile des Kommentar-Codes ein.
    // Line, name, indentation, display dealy
    com_sort.addCodeLine("// Sortiere mit Binary-Tree-Sort", null, 0, null); // 0
    com_sort.addCodeLine("", null, 0, null); // 1
    com_sort.addCodeLine("// erzeuge einen leere Binaerbaum", null, 1, null); // 2
    com_sort.addCodeLine("", null, 0, null); // 3
    com_sort.addCodeLine("// fuege alle Elemente in den Baum ein", null, 1,
        null); // 4
    com_sort.addCodeLine("", null, 0, null); // 5
    com_sort.addCodeLine("", null, 0, null); // 6
    com_sort.addCodeLine("", null, 0, null); // 7
    com_sort.addCodeLine("// traversiere den Baum in-order", null, 1, null); // 8
  }

  /**
   * Sortiere die uebergebene Liste mit BinaryTreeSort und erstelle eine
   * passende Animal-Animation
   * 
   * @param array
   *          zu sortierende Liste
   * @return sortierte Liste
   * 
   */
  private List<Integer> sort(List<Integer> array) {

    globalArray = array;

    exec("header");
    int hll = 1;

    // graue Insert und Inorder aus, da derzeit nicht gebraucht
    sc_insert.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.LIGHT_GRAY, null, null);
    com_insert.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.LIGHT_GRAY, null, null);
    sc_getinorder.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.LIGHT_GRAY, null, null);
    com_getinorder.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.LIGHT_GRAY, null, null);

    // Definiere das Aussehen der Arrays
    ArrayProperties arrayProps = new ArrayProperties();

    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        array_cellhighlight);

    // Stringarrays zum Anzeigen der Informationen in der Animation
    IntArray unsorted = lang.newIntArray(new Coordinates(40, 100), keys,
        "unsortedArray", null, arrayProps);

    lang.nextStep();

    // Definiere das Aussehen eines Baums
    graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        array_cellhighlight);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        array_cellhighlight);

    // erstelle Objekt um den Baum aufzubauen
    BinaryTree tree = new BinaryTree();

    // Code highlighten
    exec("construct");
    hll = 3;

    // Initialisiere die AdjMatr fuer den Graphen
    adjacencyMatrix = new int[array.size()][array.size()];
    nodes = new Node[array.size()];
    labels = new String[array.size()];

    lang.nextStep();

    // Code highlighten

    exec("loop");
    hll = 5;

    lang.nextStep();

    // Baum aufbauen
    hll = insertToTree(array, hll, unsorted, tree);

    // lang.nextStep();

    lang.nextStep();

    // Code highlighten
    exec("inorder");
    hll = 9;

    // Graph graphTree = lang.newGraph("graphTree", adjacencyMatrix, nodes,
    // labels, null, graphProps);

    // InOrderTraversierung holen...
    List<Integer> sorted = tree.getInorder(new ArrayList<Integer>());

    lang.nextStep();

    // Code highlighten
    exec("return");
    hll = 10;

    // Liste in Array kopieren
    int[] sortedKeys = new int[keys.length];
    for (int i = 0; i < sorted.size(); i++) {
      sortedKeys[i] = sorted.get(i);
    }

    // Zeichne das sortierte Array in die Animation
    @SuppressWarnings("unused")
    IntArray sortedArray = lang.newIntArray(new Coordinates(40, 130),
        sortedKeys, "unsortedArray", null, arrayProps);

    return sorted;
  }

  /**
   * Fuegt die Elemente nacheinander in den binaeren Baum ein...
   * 
   * @param array
   *          einzufuegende Elemente
   * @param myHll
   *          gehighlightete Zeile
   * @param unsorted
   *          Unsortiertes Array der Animation
   * @param tree
   *          Binaerbaum
   * @return neue gehighlightete Zeile
   */
  private int insertToTree(List<Integer> array, int myHll, IntArray unsorted,
      BinaryTree tree) {
    int hll = myHll;
    // Code highlighten
    exec("insert");
    hll = 6;

    // fuege alle teile in den baum ein...
    // Graph graphTree;
    while (Integer.parseInt(vars.get("i")) < array.size()) {
      unsorted.unhighlightCell(0, array.size() - 1, null, null);
      unsorted.highlightCell(Integer.parseInt(vars.get("i")), null, null);
      tree.insert(array.get(Integer.parseInt(vars.get("i"))), array,
          array.get(Integer.parseInt(vars.get("i"))), hll);

      Integer count = Integer.parseInt(vars.get("i")) + 1;
      vars.set("i", count.toString());
    }
    unsorted.unhighlightCell(0, array.size() - 1, null, null);

    return hll;
  }

  /*
   * ******************************************************* *\ |
   * ******************************************************* | |
   * ******************************************************* | | *****
   * generate/init zur Generierung ***** | |
   * ******************************************************* | |
   * ******************************************************* | \*
   * *******************************************************
   */

  /**
   * initialisiert die Animation und startet diese...
   */
  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    // initialisieren um immer gleich zu starten
    init();

    if (primitives != null) {
      array_cellhighlight = (Color) primitives.get("Array-Markierung");
      keys = (int[]) primitives.get("Array");
    }

    keys = filter(keys);

    // sortierung animieren
    prepareScreen(keys);

    // animalscript welcher die animation enthaelt zurueckgeben
    return lang.toString();
  }

  /**
   * Filtert doppelte Keys aus dem Array, da diese zu Anzeigefehlern fuehren
   * 
   * @param myKeys
   *          zu sortierendes Array
   * @return gefiltertes Array
   */
  private int[] filter(int[] myKeys) {
    int[] key = myKeys;
    Vector<Integer> temporary = new Vector<Integer>();

    for (int k : key) {
      if (!temporary.contains(k))
        temporary.add(k);
    }

    if (key.length != temporary.size())
      filtered_doubles = true;

    key = new int[temporary.size()];

    for (int i = 0; i < temporary.size(); i++) {
      key[i] = temporary.get(i);
    }

    return key;
  }

  /**
   * initialisiert die Animation und parsed den Sourcecode mit Annotationen
   */
  @Override
  public void init() {
    super.init();

    // default werte...
    int[] k2 = { 15, 2, 19, 4, 2, 1, 9, 18, 22 };
    keys = k2;

    sc_context = Color.BLUE;
    sc_font = new Font("Monospaced", Font.PLAIN, 10);
    sc_text = Color.BLACK;
    sc_com = Color.BLUE;

    header_font = new Font("SansSerif", Font.BOLD, 24);

    // Sourcecodedesign
    SourceCodeProperties props = new SourceCodeProperties();
    props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
    props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 10));

    // instantiate source code primitive to work with
    sourceCode = lang.newSourceCode(new Coordinates(500, 50), "Code", null,
        props);

    // parsing anwerfen
    parse();
  }

  /*
   * ******************************************************* *\ |
   * ******************************************************* | |
   * ******************************************************* | | *****
   * "Main"-Methode zum testen ***** | |
   * ******************************************************* | |
   * ******************************************************* | \*
   * *******************************************************
   */

  /*
   * ******************************************************* *\ |
   * ******************************************************* | |
   * ******************************************************* | | *****
   * "Info"-Methoden zur Generierung ***** | |
   * ******************************************************* | |
   * ******************************************************* | \*
   * *******************************************************
   */

  /**
   * gibt den Namen des Algorithmus zurueck
   */
  @Override
  public String getAlgorithmName() {
    return "Sortierung mit Binärbaum";
  }

  /**
   * gibt die Autoren/den Autor der Animation zurueck
   */
  @Override
  public String getAnimationAuthor() {
    return author;
  }

  /**
   * Gibt das Codebeispiel zurueck
   */
  @Override
  public String getCodeExample() {
    return code_example;
  }

  /**
   * gibt die Sprache zureck
   */
  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  /**
   * Gibt die Beschreibung zurueck
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Gibt die Dateierweiterung zurueck
   */
  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /**
   * Gibt den Typ des Generators zurueck
   */
  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  /**
   * Gibt den Namen des Algorithmus zurueck
   */
  @Override
  public String getName() {
    return algname;
  }

  /**
   * genutzte Programmiersprache
   */
  @Override
  public String getOutputLanguage() {
    return generators.framework.Generator.JAVA_OUTPUT;
  }

  /**
   * nested Class for BinaryTree
   */
  public class BinaryTree {

    // final int nil = Integer.MIN_VALUE;

    private int        key;
    private BinaryTree left;
    private BinaryTree right;
    int                xPosition;
    int                yPosition;

    /**
     * Erstellt einen Knoten/Teilbaum der direkt einen Key bekommt
     * 
     * @param rootKey
     */
    public BinaryTree(int rootKey) {
      key = rootKey;
    }

    /**
     * Erstellt einen Knoten/Teilbaum ohne Key
     */
    public BinaryTree() {
    }

    /**
     * Fuege eienn Knoten mit Key insertKey in den Baum ein
     * 
     * @param insertKey
     *          einzufuegender Key
     * @param array
     * @param myPred
     */
    public void insert(int insertKey, List<Integer> array, int myPred,
        int old_hll) {
      int pred = myPred;
      // Blende den Code ein/aus
      com_sort.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.LIGHT_GRAY, null, null);

      exec("insert");
      sc_insert.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.BLACK, null, null);
      com_insert.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.BLUE, null, null);

      // Unhighlighte alle Knoten
      for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] != null) {
          graphTree.unhighlightNode(i, null, null);
        }
      }
      // Highlighte Knoten auf dem Insert durchgefuehrt wird
      if (insertKey != array.get(0)) {
        graphTree.highlightNode(array.indexOf(this.key), null, null);
      }

      sc_insert.highlight(1);
      int hll = 1;

      lang.nextStep();

      sc_insert.toggleHighlight(hll, 3);
      hll = 3;

      lang.nextStep();

      if (insertKey == array.get(0)) {
        sc_insert.toggleHighlight(hll, 5); // Code-Highlighting
        hll = 5;

        this.key = insertKey;// new BinaryTree(insertKey);
        this.xPosition = 250;
        this.yPosition = 200;
        nodes[0] = new Coordinates(xPosition, yPosition);
        labels[0] = Integer.toString(insertKey);

        // alle knoten des anfangs bereits vollen Arrays
        // uebereinanderlegen,
        // damit sie nicht den null-Knoten links oben erzeugen
        for (int i = 1; i < nodes.length; i++) {
          nodes[i] = nodes[0];
          labels[i] = labels[0];
        }

        graphTree = lang.newGraph("graphTree", adjacencyMatrix, nodes, labels,
            null, graphProps);
        graphTree.highlightNode(nodes.length - 1, null, null); // highlighte
        // hier
        // 1 und
        // nicht
        // 0,
        // weil
        // 1
        // über
        // 0
        // liegt!
        lang.nextStep();

      } else {
        sc_insert.toggleHighlight(hll, 6);
        hll = 6;
        lang.nextStep();
        sc_insert.toggleHighlight(hll, 8);
        hll = 8;
        lang.nextStep();
        if (insertKey < this.key) {
          sc_insert.toggleHighlight(hll, 10);
          hll = 10;
          lang.nextStep();
          pred = this.key;
          if (this.left == null) {

            sc_insert.toggleHighlight(hll, 11);
            hll = 11;

            this.left = new BinaryTree(insertKey);

            // Eintrag in AdjMatr.:
            adjacencyMatrix[array.indexOf(pred)][array.indexOf(insertKey)] = 1;
            // Knoten setzen:
            this.left.xPosition = this.xPosition - 300
                / ((this.yPosition / 100) + 1);
            this.left.yPosition = this.yPosition + 100;
            nodes[array.indexOf(insertKey)] = new Coordinates(
                this.left.xPosition, this.left.yPosition);
            // Label setzten:
            labels[array.indexOf(insertKey)] = Integer.toString(insertKey);

            graphTree = lang.newGraph("graphTree", adjacencyMatrix, nodes,
                labels, null, graphProps);
            lang.nextStep();
          } else {
            sc_insert.toggleHighlight(hll, 12);
            hll = 12;
            lang.nextStep();
            sc_insert.toggleHighlight(hll, 13);
            hll = 13;
            lang.nextStep();
            this.left.insert(insertKey, array, this.key, old_hll);
          }
        } else {
          sc_insert.toggleHighlight(hll, 16);
          hll = 16;
          lang.nextStep();
          pred = this.key;

          sc_insert.toggleHighlight(hll, 18);
          hll = 18;
          lang.nextStep();

          if (this.right == null) {

            this.right = new BinaryTree(insertKey);
            sc_insert.toggleHighlight(hll, 19);
            hll = 19;

            // Eintrag in die Adjazenzmatrix:
            adjacencyMatrix[array.indexOf(pred)][array.indexOf(insertKey)] = 1;
            // Knoten setzen:
            this.right.xPosition = this.xPosition + 300
                / ((this.yPosition / 100) + 1);
            this.right.yPosition = this.yPosition + 100;
            nodes[array.indexOf(insertKey)] = new Coordinates(
                this.right.xPosition, this.right.yPosition);
            // Label setzen:
            labels[array.indexOf(insertKey)] = Integer.toString(insertKey);

            graphTree = lang.newGraph("graphTree", adjacencyMatrix, nodes,
                labels, null, graphProps);
            lang.nextStep();
          } else {
            sc_insert.toggleHighlight(hll, 20);
            hll = 20;
            lang.nextStep();
            sc_insert.toggleHighlight(hll, 21);
            hll = 21;
            lang.nextStep();
            this.right.insert(insertKey, array, this.key, old_hll);
          }
        }
      }

      // Farben wiederherstellen

      sc_insert.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.LIGHT_GRAY, null, null);
      com_insert.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.LIGHT_GRAY, null, null);
      com_sort.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE,
          null, null);

    }

    // public List<Integer> getInorder(List<Integer> orderedList,
    // BinaryTree node) {
    //
    // // es existieren noch linke kinder...
    // if (node.left != null) {
    // getInorder(orderedList, node.left);
    // }
    //
    // // fuege jetzt aktuellen knoten ein da links nichts tieferes ex.
    // orderedList.add(node.key);
    //
    // // dann traversiere den rechten kindbaum
    // if (node.right != null) {
    // getInorder(orderedList, node.right);
    // }
    //
    // return orderedList;
    //
    // }

    public ArrayList<Integer> getInorder(ArrayList<Integer> orderedList) {

      exec("inorder");
      com_sort.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.LIGHT_GRAY, null, null);
      sc_getinorder.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.BLACK, null, null);
      com_getinorder.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.BLUE, null, null);

      sc_getinorder.highlight(1);
      int hll = 1;

      // Unhighlight
      for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] != null) {
          graphTree.unhighlightNode(i, null, null);
        }
      }
      // Highlight
      graphTree.highlightNode(globalArray.indexOf(this.key), null, null);

      lang.nextStep();

      sc_getinorder.toggleHighlight(hll, 3);
      hll = 3;
      lang.nextStep();
      if (this == null) {
        sc_getinorder.toggleHighlight(hll, 4);
        hll = 4;
        lang.nextStep();

      } else {
        sc_getinorder.toggleHighlight(hll, 5);
        hll = 5;
        lang.nextStep();
        sc_getinorder.toggleHighlight(hll, 7);
        hll = 7;
        lang.nextStep();
        if (this.left != null) {
          sc_getinorder.toggleHighlight(hll, 8);
          hll = 8;
          lang.nextStep();
          this.left.getInorder(orderedList);
        }
        sc_getinorder.toggleHighlight(hll, 11);
        hll = 11;
        lang.nextStep();
        orderedList.add(this.key);
        sc_getinorder.toggleHighlight(hll, 13);
        hll = 13;
        lang.nextStep();
        if (this.right != null) {
          sc_getinorder.toggleHighlight(hll, 14);
          hll = 14;
          lang.nextStep();
          this.right.getInorder(orderedList);
        }
        sc_getinorder.toggleHighlight(hll, 16);
        hll = 16;
        lang.nextStep();

      }

      // Farben wiederherstellen
      exec("inorder");
      com_sort.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.LIGHT_GRAY, null, null);
      sc_getinorder.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.BLACK, null, null);
      com_getinorder.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.BLUE, null, null);

      return orderedList;
    }

  }

  /**
   * Gibt den annotierten Code zurueck
   */
  @Override
  public String getAnnotatedSrc() {
    return "@label(\"leer01\") \n"
        + "public List<Integer> binarytreesort(List<Integer> array) { @label(\"header\") \n"
        + "@label(\"leer02\") \n"
        + "BinaryTree tree = new BinaryTree(); @label(\"construct\") \n"
        + "@label(\"leer03\") \n"
        + "for (int i : array) { @label(\"loop\") @declare(\"int\", \"i\", \"0\") \n"
        + "tree.insert(i); @label(\"insert\") \n"
        + "} @label(\"endloop\") \n"
        + "@label(\"leer04\") \n"
        + "List<Integer> sorted = tree.getInorder(new ArrayList<Integer>()); @label(\"inorder\") \n"
        + "return sorted; @label(\"return\") \n" + "} @label(\"end\") \n";
  }
}
