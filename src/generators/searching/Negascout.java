package generators.searching;

//Test
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

import generators.searching.helpers.Node;
import generators.searching.helpers.Parser;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Negascout implements ValidatingGenerator {

  private Color                      cutoffColor             = new Color(0, 0,
                                                                 0);
  private Color                      textColor               = new Color(0, 0,
                                                                 0);
  private SourceCodeProperties       sourceCodeProperties    = new SourceCodeProperties();
  private Color                      nodeValueColor          = new Color(0, 0,
                                                                 0);
  private Color                      failColor               = new Color(0, 0,
                                                                 0);
  private Color                      textHighlightColor      = new Color(0, 0,
                                                                 0);
  private Color                      nodeHighlightColor      = new Color(0, 0,
                                                                 0);
  private Color                      seenNodeValueColor      = new Color(0, 0,
                                                                 0);
  private TextProperties             descriptionProperties   = new TextProperties();
  private TextProperties             headerProperties        = new TextProperties();
  private Color                      nodeColor               = new Color(0, 0,
                                                                 0);
  private String                     treeDefinition;
  private RectProperties             headerRectProperties    = new RectProperties();
  private RectProperties             counterBarProperties    = new RectProperties();
  private Color                      nodeValueHighlightColor = new Color(0, 0,
                                                                 0);
  private Color                      seenNodeColor           = new Color(0, 0,
                                                                 0);
  private TextProperties             counterTextProperties   = new TextProperties();
  private Color                      edgeColor               = new Color(0, 0,
                                                                 0);
  private Color                      edgeHighlightColor      = new Color(0, 0,
                                                                 0);

  private Language                   lang;
  private HashMap<String, Primitive> pMap;
  private SourceCode                 code;
  private String                     treeText                = new String();

  Text                               explain;
  Text                               tPruned;
  Text                               tSeen;
  Text                               tFailed;

  String                             lastT;
  String                             lastP;

  private Node                       root;
  private int                        result;
  private int                        nodeSize;
  private int                        leafPositionX;
  private int                        leafPositionY;
  private int                        sourceCodeDistance;

  private int                        seen;
  private int                        failed;
  private int                        pruned;
  private boolean                    failHigh;
  private int                        cSeen;                                               // Counter
                                                                                           // für
                                                                                           // Rechtecke,
                                                                                           // die
                                                                                           // die
                                                                                           // Zahl
                                                                                           // der
                                                                                           // schon
                                                                                           // gesehenen
                                                                                           // Knoten
                                                                                           // repräsentieren
  private int                        cFailed;
  private int                        cPruned;
  private int                        barLeft;                                             // Für
                                                                                           // die
                                                                                           // Zeichnung
                                                                                           // der
                                                                                           // Balkendiagramme
  private int                        barRight;
  HashMap<String, Node>              prunedMap;
  private int                        nodesTotal;

  Rect                               rSeen;
  Rect                               rFailed;
  Rect                               rPruned;

  boolean                            beforeAlgo;

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    validateInput(props, primitives);
    cutoffColor = (Color) primitives.get("cutoffColor");
    textColor = (Color) primitives.get("textColor");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    nodeValueColor = (Color) primitives.get("nodeValueColor");
    failColor = (Color) primitives.get("failColor");
    textHighlightColor = (Color) primitives.get("textHighlightColor");
    nodeHighlightColor = (Color) primitives.get("nodeHighlightColor");
    seenNodeValueColor = (Color) primitives.get("seenNodeValueColor");
    nodeColor = (Color) primitives.get("nodeColor");
    treeDefinition = (String) primitives.get("treeDefinition");
    headerRectProperties = (RectProperties) props
        .getPropertiesByName("headerRectProperties");
    counterBarProperties = (RectProperties) props
        .getPropertiesByName("counterBarProperties");
    nodeValueHighlightColor = (Color) primitives.get("nodeValueHighlightColor");
    seenNodeColor = (Color) primitives.get("seenNodeColor");
    counterTextProperties = (TextProperties) props
        .getPropertiesByName("counterTextProperties");
    edgeColor = (Color) primitives.get("edgeColor");
    edgeHighlightColor = (Color) primitives.get("edgeHighlightColor");

    treeText = treeDefinition;

    init();
    introduction();
    algo();
    end();

    return lang.toString();
  }

  public String getName() {
    return "Negascout [DE]";
  }

  public String getAlgorithmName() {
    return "Negascout";
  }

  public String getAnimationAuthor() {
    return "Janine Höscher, Johannes Wagener";
  }

  public String getDescription() {
    return "Der Negascout-Algorithmus wird angewendet, um in einem Spiel zwischen zwei Parteien "
        + "\n"
        + "eine optimale Spielstrategie für eine der Parteien zu bestimmen. M&#246;glichen Z&#252;ge "
        + "\n"
        + "werden dazu in einer Baumstruktur dargestellt."
        + "\n"
        + "Negascout funktioniert so &#228;hnlich wie der Alpha-Beta-Algorithmus:"
        + "\n"
        + "Jeder Knoten wird mit einer unteren und einer oberen Grenze untersucht, die den"
        + "\n"
        + "zu diesem Zeitpunkt m&#246;glichen maximalen und minimalen Gewinn angibt."
        + "\n"
        + "Durch diese Begrenzung k&#246;nnen bestimmte Teilb&#228;ume abgeschnitten"
        + "\n"
        + "(gepruned) werden, da sie f&#252;r das Endergebnis irrelevant sind (z.B. Z&#252;ge,"
        + "\n"
        + "die dem Gegner zu viele Punkte bringen w&#252;rden)."
        + "\n"
        + "Die Vorsilbe 'Nega-' im Namen des Algorithmus weist darauf hin, dass das Punktefenster"
        + "\n"
        + "f&#252;r den n&#228;chsten (vom jeweils anderen Spieler ausgef&#252;hrten) "
        + "\n"
        + "Spielzug negiert wird. Eine Besonderheit von Negascout ist, dass der Algorithmus"
        + "\n"
        + "davon ausgeht, dass der erste betrachtete Zug der beste ist. Weitere m&#246;gliche"
        + "\n"
        + "Z&#252;ge werden anschlie&#223;end mit einem Nullwindow (obere und untere"
        + "\n"
        + "Grenze liegen nur um 1 auseinander) untersucht, um zu beweisen, dass sie"
        + "\n"
        + "tats&#228;chlich schlechter sind. Ist das nicht der Fall, tritt ein sogenanntes Fail"
        + "\n"
        + " High auf, das hei&#223;t der betrachtete Zug ist besser als erwartet. Dann "
        + "\n"
        + "muss dieser Knoten erneut mit dem vollst&#228;ndigen Wertefenster untersucht werden.";
  }

  public String getCodeExample() {
    return "int negascout (node n, int alpha, int beta){" + "\n"
        + "     if (node is a leaf)" + "\n" + "          return valueOf(node);"
        + "\n" + "     int a = alpha;" + "\n" + "     int b = beta;" + "\n"
        + "     for each child of node{" + "\n"
        + "           int score = -negascout (child, -b, -a);" + "\n"
        + "           if (a < score < beta and child is not first child)"
        + "\n" + "                score = -negascout (child, -beta, -score);"
        + "\n" + "           a=max(a, score);" + "\n"
        + "           if(a >= beta)" + "\n" + "                return a;"
        + "\n" + "           b = a+1;" + "\n" + "     }" + "\n"
        + "     return a;" + "\n" + "}";
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

  public void init() {
    lang = new AnimalScript("Negascout [DE]",
        "Janine Hoelscher, Johannes Wagener", 800, 600);
    lang.setStepMode(true);
    pMap = new HashMap<String, Primitive>();
    code = null;

    explain = null;
    tPruned = null;
    tSeen = null;
    tFailed = null;

    lastT = null;
    lastP = null;

    root = null;
    result = 0;
    nodeSize = 50;
    leafPositionX = 25;
    leafPositionY = 60;
    sourceCodeDistance = 50;

    seen = 0;
    failed = 0;
    pruned = 0;
    failHigh = false;
    cSeen = 0;
    cFailed = 0;
    cPruned = 0;
    barLeft = 180;
    barRight = 100;
    prunedMap = new HashMap<String, Node>();
    nodesTotal = 0;

    rSeen = null;
    rFailed = null;
    rPruned = null;

    beforeAlgo = true;

    descriptionProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 10));
    headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 24));
  }

  // Zeigt die Einfuehrung in den Algorithmus an und parst die Eingabesequenz
  protected void introduction() {

    // Titel

    Text title = lang.newText(new Coordinates(20, 25), "Negascout Algorithmus",
        "header", null, headerProperties);
    pMap.put("title", title);
    // Umrandung
    pMap.put("hRect", lang.newRect(new Coordinates(10, 15), new Coordinates(
        400, 50), "hRect", null, headerRectProperties));

    // Zeigt die Einleitung an
    SourceCode intro;
    intro = lang.newSourceCode(new Coordinates(30, 60), "intro", null,
        sourceCodeProperties);
    intro
        .addCodeLine(
            "Der Negascout-Algorithmus wird angewendet, um in einem Spiel zwischen zwei Parteien",
            "intro", 0, null);
    intro
        .addCodeLine(
            "eine optimale Spielstrategie f\u00FCr eine der Parteien zu bestimmen.",
            "intro", 0, null);
    intro
        .addCodeLine(
            "Die m\u00F6glichen Z\u00FCge werden dazu in einer Baumstruktur dargestellt.",
            "intro", 0, null);
    intro.addCodeLine("", "intro", 0, null);
    intro.addCodeLine("Dabei werden folgende Annahmen getroffen:", "intro", 0,
        null);
    intro
        .addCodeLine(
            "- es handelt sich um ein Nullsummenspiel, das hei\u00DFt positive Punkte des einen Spielers",
            "intro", 0, null);
    intro
        .addCodeLine(
            "  sind negative Punkte f\u00FCr den anderen, sodass alle Punkte in der Summe 0 ergeben",
            "intro", 0, null);
    intro.addCodeLine(
        "- beide Parteien spielen optimal und machen keine Fehler", "intro", 0,
        null);
    intro
        .addCodeLine(
            "- das Spiel ist deterministisch, also nicht von W\u00FCrfelgl\u00FCck o.\u00E4. abh\u00E4ngig",
            "intro", 0, null);
    intro
        .addCodeLine(
            "- es gibt keine verborgenen Informationen wie beispielsweise beim Kartenspiel",
            "intro", 0, null);
    intro.addCodeLine("", "intro", 0, null);
    intro
        .addCodeLine(
            "Negascout funktioniert so \u00E4hnlich wie der Alpha-Beta-Algorithmus:",
            "intro", 0, null);
    intro
        .addCodeLine(
            "Jeder Knoten wird mit einer unteren und einer oberen Grenze untersucht, die den zu diesem",
            "intro", 0, null);
    intro.addCodeLine(
        "Zeitpunkt m\u00F6glichen maximalen und minimalen Gewinn angibt.",
        "intro", 0, null);
    intro
        .addCodeLine(
            "Durch diese Begrenzung k\u00F6nnen bestimmte Teilb\u00E4ume abgeschnitten (gepruned) werden, da sie f\u00FCr ",
            "intro", 0, null);
    intro
        .addCodeLine(
            "das Endergebnis irrelevant sind (z.B. Z\u00FCge, die dem Gegner zu viele Punkte bringen w\u00FCrden).",
            "intro", 0, null);
    intro
        .addCodeLine(
            "Die Vorsilbe 'Nega-' im Namen des Algorithmus weist darauf hin, dass das Punktefenster f\u00FCr den",
            "intro", 0, null);
    intro
        .addCodeLine(
            "n\u00E4chsten (vom jeweils anderen Spieler ausgef\u00FChrten) Spielzug negiert wird.",
            "intro", 0, null);
    intro
        .addCodeLine(
            "Eine Besonderheit von Negascout ist, dass der Algorithmus davon ausgeht, dass der erste",
            "intro", 0, null);
    intro
        .addCodeLine(
            "betrachtete Zug der beste ist. Weitere m\u00F6gliche Z\u00FCge werden anschlie\u00DFend mit einem ",
            "intro", 0, null);
    intro
        .addCodeLine(
            "Nullwindow (obere und untere Grenze liegen nur um 1 auseinander) untersucht, um zu beweisen,",
            "intro", 0, null);
    intro
        .addCodeLine(
            "dass sie tats\u00E4chlich schlechter sind. Ist das nicht der Fall, tritt ein sogenanntes Fail High auf,",
            "intro", 0, null);
    intro
        .addCodeLine(
            "das hei\u00DFt der betrachtete Zug ist besser als erwartet. Dann muss dieser Knoten erneut mit dem ",
            "intro", 0, null);
    intro.addCodeLine("vollst\u00E4ndigen Wertefenster untersucht werden.",
        "intro", 0, null);
    lang.nextStep("Intro");
    intro.hide();

    // Zeigt den Code an
    code = lang.newSourceCode(new Coordinates(30, 60), "code", null,
        sourceCodeProperties);
    code.addCodeLine("int negascout (node n, int alpha, int beta){", "code", 0,
        null); // 0
    code.addCodeLine("     if (node is a leaf)", "code", 0, null); // 1
    code.addCodeLine("          return valueOf(node);", "code", 0, null); // 2
    code.addCodeLine("     int a=alpha;", "code", 0, null); // 3
    code.addCodeLine("     int b=beta;", "code", 0, null); // 4
    code.addCodeLine("     for each child of node{", "code", 0, null); // 5
    code.addCodeLine("           int score = -negascout (child, -b, -a);",
        "code", 0, null); // 6
    code.addCodeLine(
        "           if (a < score < beta and child is not first child)",
        "code", 0, null); // 7
    code.addCodeLine(
        "                score = -negascout (child, -beta, -score);", "code",
        0, null); // 8
    code.addCodeLine("           a=max(a, score);", "code", 0, null); // 9
    code.addCodeLine("           if(a >= beta)", "code", 0, null); // 10
    code.addCodeLine("                return a;", "code", 0, null); // 11
    code.addCodeLine("           b = a+1;", "code", 0, null); // 12
    code.addCodeLine("     }", "code", 0, null); // 13
    code.addCodeLine("     return a;", "code", 0, null); // 14
    code.addCodeLine("}", "code", 0, null); // 15
    // Erklaert die aktuelle Codezeile

    explain = lang.newText(new Offset(0, 30, "code", "SW"), "", "explain",
        null, descriptionProperties);
    tSeen = lang.newText(new Offset(0, 90, "code", "SW"), "", "tSeen", null,
        counterTextProperties);
    tSeen.setText("betrachtete Knoten: 0", null, null);
    tPruned = lang.newText(new Offset(0, 130, "code", "SW"), "", "tPruned",
        null, counterTextProperties);
    tPruned.setText("geprunte Knoten: 0", null, null);
    tFailed = lang.newText(new Offset(0, 110, "code", "SW"), "", "tFailed",
        null, counterTextProperties);
    tFailed.setText("erneut untersuchte Knoten: 0", null, null);
    pMap.put("explain", explain);
    pMap.put("tSeen", tSeen);
    pMap.put("tPruned", tPruned);
    pMap.put("tFailed", tFailed);

    rSeen = lang.newRect(new Offset(barLeft, -5, "tSeen", "NE"), new Offset(
        barLeft, 10, "tSeen", "NE"), "rSeen0", null, counterBarProperties);
    pMap.put("rSeen0", rSeen);
    rPruned = lang.newRect(new Offset(barLeft, -5, "tPruned", "NE"),
        new Offset(barLeft, 10, "tPruned", "NE"), "rPruned0", null,
        counterBarProperties);
    pMap.put("rPruned0", rPruned);
    rFailed = lang.newRect(new Offset(barLeft, -5, "tFailed", "NE"),
        new Offset(barLeft, 10, "tFailed", "NE"), "rFailed0", null,
        counterBarProperties);
    pMap.put("rFailed0", rFailed);

    // Parst den Eingabebaum und zeichnet ihn
    Parser p = new Parser();
    root = p.parseText(treeText);
    showTree(root);
    nodesTotal = countNodes(root);
  }

  // Erster Aufruf des Algorithmus mit einem Fenster von -Unendlich bis
  // +Unendlich
  protected int algo() {
    result = algo(root, -2000000000, 2000000000);

    // Nach Terminierung des Algorithmus
    lang.nextStep();
    setExplain(200, result, 0);
    colorNode(root, seenNodeColor);
    code.unhighlight(14);
    return result;
  }

  // Der Algorithmus inklusive Darstellungsoperationen
  protected int algo(Node node, int alpha, int beta) {
    int children = node.getChildren().size();
    // Zeile 0

    mark(0);
    if (!failHigh) {
      setSeen();
    }// Zaehlt fuer die Endauswertung
    else {
      if (prunedMap.containsKey(node.getId())) {
        setSeen();
        prunedMap.remove(node.getId());
      } else {
        failed++;
        setFailed(node);
      }
    }

    lightsOut(0);
    updateBorders(node, alpha, beta);
    setExplain(0, alpha, beta);
    highlightNode(node);

    // Zeile 1
    mark(1);
    code.highlight(1);
    setExplain(1, 0, 0);
    if (node.isLeaf()) {

      // Zeile 2
      mark(2);
      setExplain(2, node.getValue(), 0);
      lastT = "window" + node.getId();
      colorObject("tVal" + node.getId(), nodeValueHighlightColor);
      return node.getValue();

    }

    // Zeile 3
    int a = alpha;
    mark(3);
    code.unhighlight(1);
    setExplain(3, alpha, 0);

    // Zeile 4
    int b = beta;
    mark(4);
    setExplain(4, beta, 0);

    // Zeile 5
    mark(5);
    setExplain(5, 0, 0);
    colorChildren(node);
    for (int j = 0; j < children; j++) {
      Node child = node.getChildren().get(j);

      // Zeile 6
      mark(6);
      lightsOut(6);
      uncolorChildren(node);
      if (j == 0) {
        setExplain(60, 0, 0);
      } else {
        setExplain(61, 0, 0);
      }

      colorLine(child);
      int score = -algo(child, -b, -a);
      mark(6);
      lightsOut(6);
      if (child.isLeaf()) {
        pMap.get("tVal" + child.getId()).changeColor("", seenNodeValueColor,
            null, null);
      }
      setExplain(62, score, 0);
      seenNode(child);
      showReturn(child, score);

      // Zeile 7
      mark(7);
      hideReturn(child);
      setExplain(7, 0, 0);

      if (a < score && score < beta && j > 0) {

        // Zeile 8
        mark(8);
        setFailed(child);
        setExplain(8, 0, 0);
        drawFailHigh(node, j);
        cleanAfterFail(child);
        colorLine(child);
        failHigh = true;
        // Zaehlt FailHighs fuer den Endbericht
        score = -algo(child, -beta, -score);
        failHigh = false;

        mark(8);
        if (child.isLeaf()) {
          pMap.get("tVal" + child.getId()).changeColor("", seenNodeValueColor,
              null, null);
        }
        lightsOut(8);
        setExplain(62, score, 0);
        seenNode(child);
        showReturn(child, score);
      }

      // Zeile 9
      mark(9);
      lightsOut(9);
      hideReturn(child);
      if (a > score) {
        setExplain(90, a, score);
      } else {
        setExplain(91, a, score);
        a = score;
      }

      // Zeile 10
      mark(10);
      if (a >= beta) {
        setExplain(100, a, beta);
        // Zeile 11
        mark(11);
        setPruned(child);
        setPrunedMap(node, j);
        if (j == children - 1) {
          setExplain(111, 0, 0);
        } else {
          setExplain(110, 0, 0);

        }
        drawCut(node, j);
        return a;
      } else {
        setExplain(101, a, beta);
      }

      // Zeile 12
      b = a + 1;
      mark(12);
      setExplain(12, a + 1, 0);
      code.unhighlight(10);

    }
    // Zeile 14
    mark(14);
    code.unhighlight(12);
    setExplain(14, 0, 0);
    return a;

  }

  // Zum Schluss wird eine Zusammenfassung des Geschehenen angezeigt
  protected void end() {
    lang.nextStep();
    int total = seen + failed;

    hideAll();
    SourceCode end = lang.newSourceCode(new Offset(0, 50, pMap.get("hRect"),
        "SW"), "end", null, sourceCodeProperties);
    end.addCodeLine(
        "Das h\u00F6chste Ergebnis, dass der Spieler bei einem optimal spielenden Gegner erzielen kann, ist "
            + result + ".", "end", 0, null);
    if (seen == 1) {
      end.addCodeLine("Von den insgesamt " + nodesTotal + " Knoten wurde "
          + seen + " nur Knoten untersucht.", "end", 0, null);
    } else {
      end.addCodeLine("Von den insgesamt " + nodesTotal + " Knoten wurden "
          + seen + " verschiedene Knoten untersucht.", "end", 0, null);
    }
    if (pruned == 1) {
      end.addCodeLine("Es wurde " + pruned + " Knoten gepruned.", "end", 0,
          null);
    } else {
      end.addCodeLine("Es wurde " + pruned + " Knoten gepruned.", "end", 0,
          null);
    }
    if (failed == 1) {
      end.addCodeLine("Allerdings musste auch " + failed
          + " Knoten erneut untersucht werden, weil es zum Fail High kam.",
          "end", 0, null);
    } else {
      end.addCodeLine("Allerdings mussten auch " + failed
          + " Knoten erneut untersucht werden, weil es zum Fail High kam.",
          "end", 0, null);
    }
    end.addCodeLine("Damit wurde der Algorithmus insgesamt " + total
        + " Mal aufgerufen.", "end", 0, null);
    end.addCodeLine(
        "Im Vergleich dazu h\u00E4tte der Minimax-Algorithmus alle "
            + nodesTotal + " Knoten einmal untersucht.", "end", 0, null);
    end.addCodeLine("", "end", 0, null);
    end.addCodeLine(
        "Da der Algorithmus annimmt, dass jeweils der erste Kindknoten der beste ist ",
        "end", 0, null);
    end.addCodeLine(
        "und danach mit einem Nullwindow arbeitet, ist die Reihenfolge der Knoten sehr wichtig, ",
        "end", 0, null);
    end.addCodeLine(
        "damit m\u00F6glichst viele Knoten gepruned werden und m\u00F6glichst wenige erneut untersucht werden m\u00FCssen.",
        "end", 0, null);
    end.addCodeLine(
        "Es kann sich sogar zeitlich lohnen, die Knoten vorzusortieren.",
        "end", 0, null);
    end.addCodeLine(
        "Bei unsortierten Knoten ist der Negascout-Algorithmus langsamer als das Alpha-Beta-Pruning,",
        "end", 0, null);
    end.addCodeLine(
        "bei dem zwar weniger Knoten nicht untersucht werden, daf\u00FCr aber auch keine erneut untersucht werden m\u00FCssen.",
        "end", 0, null);
    end.addCodeLine(
        "Die Komplexit\u00E4t von Negascout betr\u00E4gt O(b^d) in der Landau-Notation,",
        "end", 0, null);
    end.addCodeLine(
        "wobei b f\u00FCr den Verzweigungsfaktor (branching factor) und d f\u00FCr die Baumtiefe (depth) steht",
        "end", 0, null);
    lang.nextStep("Conclusion");
  }

  // ***Zeichenmethoden***//

  // Zeichnet den Baum
  private void showTree(Node root) {
    int currentDepth = 0;
    int leftBorder = sourceCodeDistance;
    int nrOfNodesInTree = countNodes(root);
    HashMap<String, Node> nMap = new HashMap<String, Node>();
    nMap = fillNodeMap(root, nMap);

    Queue<Node> queue = new LinkedList<Node>();
    queue.add(root);
    while (!queue.isEmpty()) {
      Node node = queue.poll();
      if (currentDepth != node.getDepth()) {
        leftBorder = calculateLeftBorder(true, node, nrOfNodesInTree, nMap);
        currentDepth++;
      }
      int x;
      int y = (nodeSize + leafPositionY) * node.getDepth();

      if (node.isLeaf()) {
        x = leftBorder;
      } else {
        int totalWidth = (nodeSize + leafPositionX) * node.getLeafCount()
            - leafPositionX;

        x = (totalWidth / 2) + leftBorder - (nodeSize / 2);
      }
      drawNodes(x, y, node);

      if (node.isLeaf()) {
        leftBorder += calculateLeftBorder(false, node, nrOfNodesInTree, nMap);
      } else {
        leftBorder += (nodeSize + leafPositionX) * node.getLeafCount();
      }
      queue.addAll(node.getChildren());
    }
  }

  private int calculateLeftBorder(boolean lineStart, Node node,
      int nrOfNodesInTree, HashMap<String, Node> nMap) {
    int parentId = Integer.parseInt(node.getParent().getId()
        .replaceAll("[\\D]", ""));
    int parentDepth = node.getParent().getDepth();
    int countChildlessNodes = 0;
    LinkedList<Node> parentRow = new LinkedList<Node>();
    if (lineStart) {
      for (int i = parentId - 1; i > 0; i--) {
        Node aNode = nMap.get("n" + i);
        if (aNode.getDepth() == parentDepth
            || (aNode.getDepth() < parentDepth && aNode.isLeaf())) {
          parentRow.add(aNode);
        }
      }
    } else {
      int highestIdOfSiblings = 0;
      ArrayList<Node> siblings = (ArrayList<Node>) node.getParent()
          .getChildren();
      for (int i = 0; i < siblings.size(); i++) {
        int aId = Integer.parseInt(siblings.get(i).getId()
            .replaceAll("[\\D]", ""));
        if (aId > highestIdOfSiblings) {
          highestIdOfSiblings = aId;
        }
      }
      if (Integer.parseInt(node.getId().replaceAll("[\\D]", "")) < highestIdOfSiblings) {
        return nodeSize + leafPositionX;
      } else {
        for (int j = parentId + 1; j < nrOfNodesInTree; j++) {
          Node aNode = nMap.get("n" + j);
          if (aNode.getDepth() == parentDepth
              || (aNode.getDepth() < parentDepth && aNode.isLeaf())) {
            parentRow.add(aNode);
          }
        }
      }
    }
    for (int j = 0; j < parentRow.size(); j++) {
      if (parentRow.get(j).getChildren().size() == 0) {
        countChildlessNodes++;
      } else {
        break;
      }
    }
    if (lineStart) {
      return sourceCodeDistance + (nodeSize + leafPositionX)
          * countChildlessNodes;
    } else {
      return (nodeSize + leafPositionX) * (countChildlessNodes + 1);
    }
  }

  private HashMap<String, Node> fillNodeMap(Node root2,
      HashMap<String, Node> nMap) {
    nMap.put(root2.getId(), root2);
    ArrayList<Node> children = (ArrayList<Node>) root2.getChildren();
    for (int i = 0; i < children.size(); i++) {
      fillNodeMap(children.get(i), nMap);
    }
    return nMap;
  }

  // Zeichnet Verbindungslinien
  private void drawLine(Primitive a, Primitive b, String id) {
    Offset o1 = new Offset(0, 0, a, "S");
    Offset o2 = new Offset(0, 0, b, "N");
    Offset[] offsets = new Offset[] { o1, o2 };
    Polyline p = lang.newPolyline(offsets, "p" + id, null);
    p.changeColor("", edgeColor, null, null);
    pMap.put("p" + id, p);
  }

  // Zeichnet Min und Max Knoten
  private void drawNodes(int x, int y, Node node) {
    int yS = node.isMax() ? y : (y + nodeSize);
    int yD = node.isMax() ? y + nodeSize : y;
    Offset s = new Offset(x, yS, code, "NE");
    Offset dL = new Offset(x - (nodeSize / 2), yD, code, "NE");
    Offset dR = new Offset(x + (nodeSize / 2), yD, code, "NE");

    TriangleProperties nodeProps = new TriangleProperties();
    nodeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    Triangle tNode = lang.newTriangle(s, dL, dR, node.getId(), null, nodeProps);
    tNode.changeColor("fillColor", nodeColor, null, null);
    pMap.put(node.getId(), tNode);
    if (node.getParent() != null) {
      Primitive from = pMap.get(node.getParent().getId());
      drawLine(from, tNode, node.getId());
    }
    if (node.isLeaf()) {
      int valLength = String.valueOf(node.getValue()).length();
      Offset tO;
      if (node.isMax()) {
        tO = new Offset(-valLength * 3, -nodeSize / 30, tNode, "C");
      } else {
        tO = new Offset(-valLength * 3, -nodeSize / 3, tNode, "C");
      }
      Text tVal = lang.newText(tO, String.valueOf(node.getValue()), "tVal"
          + node.getId(), null);
      tVal.changeColor("", Color.white, null, null);
      pMap.put("tVal" + node.getId(), tVal);
    }
  }

  // ***Methoden, um die Darstellung zu veraendern***//

  // Primitives Objekt umfaerben. Achtung: nicht fuer Nodes verwenden
  private void colorObject(String node, Color color) {
    pMap.get(node).changeColor("", color, null, null);
  }

  // Zeichnet Cutoff ein
  private void drawCut(Node node, int child) {
    int children = node.getChildren().size();
    for (int i = child + 1; i < children; i++) {
      String id = node.getChildren().get(i).getId();
      if (pMap.containsKey("cutT" + id) && pMap.containsKey("cutP" + id)) {
        pMap.get("cutT" + id).show();
        pMap.get("cutP" + id).show();
      } else {
        Primitive p = pMap.get("p" + id);
        Offset[] o = new Offset[] { new Offset(-15, 15, p, "C"),
            new Offset(15, -15, p, "C") };
        Polyline pl = lang.newPolyline(o, "cutP" + id, null);
        pl.changeColor("", cutoffColor, null, null);
        pMap.put("cutP" + id, pl);
        Text tc;
        tc = lang.newText(new Offset(7, -15, pl, "NE"), "Cutoff", "cutT" + id,
            null);
        tc.changeColor("", cutoffColor, null, null);
        pMap.put("cutT" + id, tc);
      }
    }
  }

  // Zeichnet Fail High ein
  private void drawFailHigh(Node node, int child) {
    String id = node.getChildren().get(child).getId();
    if (pMap.containsKey("failT" + id) && pMap.containsKey("failP" + id)) {
      pMap.get("failT" + id).show();
      pMap.get("failP" + id).show();
    } else {
      Primitive p = pMap.get("p" + id);
      Offset[] o = new Offset[] { new Offset(-15, 15, p, "C"),
          new Offset(15, -15, p, "C") };
      Polyline pl = lang.newPolyline(o, "failP" + id, null);
      pl.changeColor("", failColor, null, null);
      pMap.put("failP" + id, pl);
      Text tc;
      tc = lang.newText(new Offset(7, -15, pl, "NE"), "Fail High",
          "failT" + id, null);
      tc.changeColor("", failColor, null, null);
      pMap.put("failT" + id, tc);
    }
  }

  private void cleanAfterFail(Node child2) {

    colorNode(child2, nodeColor);
    if (pMap.containsKey("window" + child2.getId())) {
      ((Text) pMap.get("window" + child2.getId())).setText("", null, null);
    }
    if (child2.isLeaf()) {
      if (pMap.containsKey("tVal" + child2.getId())) {
        pMap.get("tVal" + child2.getId()).changeColor("", nodeValueColor, null,
            null);
      }
    } else {
      int children = child2.getChildren().size();
      for (int i = 0; i < children; i++) {
        Node child = child2.getChildren().get(i);
        String id = child.getId();
        if (pMap.containsKey("cutP" + id)) { // Cuts löschen
          pMap.get("cutP" + id).hide();
          pMap.get("cutT" + id).hide();
        }
        if (pMap.containsKey("failP" + id)) { // Fails löschen
          pMap.get("failP" + id).hide();
          pMap.get("failT" + id).hide();
        }
        if (pMap.containsKey("tVal" + id)) {
          pMap.get("tVal" + id).changeColor("", nodeValueColor, null, null);
        }
        colorNode(child, nodeColor);
        if (pMap.containsKey("window" + id)) {
          ((Text) pMap.get("window" + id)).setText("", null, null);
        }
        cleanAfterFail(child);
      }
    }
  }

  // *Text*//

  // Aktuelle Zeile hervorheben und vorherige nicht mehr markieren sowie Texte
  // zuruecksetzen
  private void mark(int i) {
    // naechster Praesentationsschritt
    if (beforeAlgo) {
      beforeAlgo = false;
      lang.nextStep("Algorithm");
    } else {
      lang.nextStep();
    }
    if (i != 0)
      code.unhighlight(i - 1);
    code.highlight(i); // neuen Code markieren
    explain.setText("", null, null);
    if (lastT != null)
      colorObject(lastT, textColor);
    if (lastP != null)
      colorObject(lastP, edgeColor);

  }

  // Setzt den aktuellen Erklaerungstext
  protected void setExplain(int line, int one, int two) {
    String a = setToInfinity(one);
    String b = setToInfinity(two);
    String text;
    switch (line) {
      case 0:
        text = "Aufruf mit: negascout(" + a + "," + b + ")";
        break;
      case 1:
        text = "\u00DCberpr\u00FCfe, ob node ein Blatt ist";
        break;
      case 2:
        text = "Der Blattknoten wird zu " + a + " ausgewertet";
        break;
      case 3:
        text = "a = " + a;
        break;
      case 4:
        text = "b = " + a;
        break;
      case 5:
        text = "Iteriere \u00FCber alle Kindknoten.";
        break;
      case 60:
        text = "Negierter Aufruf auf dem ersten Kindknoten";
        break;
      case 61:
        text = "Negierter Aufruf mit minimalem Fenster";
        break;
      case 62:
        text = "score = " + a;
        break;
      case 7:
        text = "\u00FCberpr\u00FCfe, ob Fail High vorliegt";
        break;
      case 8:
        text = "Erneuter Aufruf mit dem gesamten Fenster";
        break;
      case 90:
        text = "alpha = max( " + a + ", " + b + " ) = " + a;
        break;
      case 91:
        text = "alpha = max( " + a + ", " + b + " ) = " + b;
        break;
      case 100:
        text = "alpha = " + a + " ist gr\u00F6\u00DFer als beta = " + b;
        break;
      case 101:
        text = "alpha = " + a + " ist kleiner als beta = " + b;
        break;
      case 110:
        text = "Es wird ein Cutoff durchgef\u00FChrt";
        break;
      case 111:
        text = "Der Cutoff ist irrelevant, weil es keine weiteren Kinder gibt.";
        break;
      case 12:
        text = "Setze neues minimales Fenster mit b =" + a;
        break;
      case 14:
        text = "Der letzte Kindknoten wurde betrachtet.";
        break;
      case 200:
        text = "Der Algorithmus gibt das Ergebnis " + a + " zur\u00FCck.";
        break;
      default:
        text = "";

    }
    explain.setText(text, null, null);
  }

  // Setzt in der Anzeige sehr grosse Werte auf Unendlich
  protected String setToInfinity(int a) {
    String x;
    if (a < -200000000) {
      x = "-\u221E";
    } else if (a > 200000000) {
      x = "\u221E";
    } else
      x = Integer.toString(a);
    return x;
  }

  // entfernt alle Markierungen von Codezeilen
  protected void lightsOut(int line) {
    for (int i = 0; i < 16; i++) {
      code.unhighlight(i);
    }
    code.highlight(line);
  }

  // *Knoten*//

  // Wenn der Knoten zum ersten Mal betrachtet wird (beim Absteigen im Baum)
  private void highlightNode(Node node) {
    // jetzigen Knoten hervorheben
    colorNode(node, nodeHighlightColor);
    // Elternknoten wieder normal faerben
    if (node.getParent() != null) {
      colorNode(node.getParent(), nodeColor);

    }
  }

  // Veraendere die Farbe eines Knotens
  private void colorNode(Node root2, Color c) {
    pMap.get(root2.getId()).changeColor("fillColor", c, null, null);
  }

  // faerbt beim Aufsteigen im Baum bereits gesehene Knoten und markiert den
  // aktuellen Knoten
  protected void seenNode(Node n) {
    colorNode(n, seenNodeColor);
    colorNode(n.getParent(), nodeHighlightColor);
  }

  // *Rueckgabewerte*//

  // Zeige Rueckgabewert an Elternknoten an
  private void showReturn(Node node, int x) {
    String a = setToInfinity(x);
    String in = "v" + node.getId();
    Text t = lang.newText(new Offset(-25, -30, pMap.get(node.getId()), "NE"),
        a, "i" + node.getId(), null);
    pMap.put(in, t);
    Text value = (Text) pMap.get(in);
    value.changeColor("", textHighlightColor, null, null);
  }

  // Verberge Rueckgabewert
  private void hideReturn(Node node) {
    Text returnValue = (Text) pMap.get("v" + node.getId());
    returnValue.setText("", null, null);
  }

  // Zeige das Wertfenster an, mit dem der Algorithmus auf dem Knoten
  // aufgerufen wird
  private void updateBorders(Node node, int alpha, int beta) {
    String a = setToInfinity(alpha);
    String b = setToInfinity(beta);

    String in = "window" + node.getId();
    if (pMap.get(in) == null) {
      Text t = lang.newText(new Offset(-10, -15, pMap.get(node.getId()), "NE"),
          "[" + a + "," + b + "]", "window" + node.getId(), null);
      t.changeColor("", textColor, null, null);
      pMap.put(in, t);

    } else {
      Text new_borders = (Text) pMap.get(in);
      new_borders.setText("[" + a + "," + b + "]", null, null);
    }
  }

  // *Kanten*//

  // Faerbt die Kanten zu allen Kindern
  protected void colorChildren(Node node) {
    int children = node.getChildren().size();
    for (int k = 0; k < children; k++) {
      colorObject("p" + node.getChildren().get(k).getId(), edgeHighlightColor);
    }
  }

  // Entfernt die Markierung aller Kanten zu allen Kindern
  protected void uncolorChildren(Node node) {
    int children = node.getChildren().size();
    for (int k = 0; k < children; k++) {
      colorObject("p" + node.getChildren().get(k).getId(), edgeColor);
    }
  }

  // faerbt eine einzelne Kante
  private void colorLine(Node child) {
    String id = child.getId();
    colorObject("p" + id, edgeHighlightColor);
    lastP = "p" + id;
  }

  // Verberge alle Elemente
  private void hideAll() {
    for (Primitive p : pMap.values()) {
      p.hide();
    }
    pMap.get("hRect").show();
    pMap.get("title").show();
    code.hide();
  }

  // ***Countermethoden***//
  protected void setSeen() {
    seen++;
    tSeen.setText("betrachtete Knoten: " + seen, null, null);
    pMap.get("rSeen" + cSeen).hide();
    cSeen++;
    pMap.put(
        "rSeen" + cSeen,
        lang.newRect(new Offset(barLeft, -5, "tSeen", "NW"), new Offset(barLeft
            + ((barRight / nodesTotal) * seen), 10, "tSeen", "NW"), "rSeen"
            + cSeen, null, counterBarProperties));
  }

  protected void setFailed(Node node) {
    tFailed.setText("erneut untersuchte Knoten: " + failed, null, null);
    pMap.get("rFailed" + cFailed).hide();
    cFailed++;
    pMap.put("rFailed" + cFailed, lang.newRect(new Offset(barLeft, -5,
        "tFailed", "NW"), new Offset(barLeft
        + ((barRight / nodesTotal) * failed), 10, "tFailed", "NW"), "rFailed"
        + cFailed, null, counterBarProperties));
  }

  protected void setPruned(Node n) {
    pruned = pruned + countNodes(n);
    tPruned.setText("geprunte Knoten: " + pruned, null, null);
    pMap.get("rPruned" + cPruned).hide();
    cPruned++;
    pMap.put("rPruned" + cPruned, lang.newRect(new Offset(barLeft, -5,
        "tPruned", "NW"), new Offset(barLeft
        + ((barRight / nodesTotal) * pruned), 10, "tPruned", "NW"), "rPruned"
        + cPruned, null, counterBarProperties));
  }

  // Speichert geprunte Knoten, damit sie bei erneuter Betrachtung richtig als
  // "vorher noch nicht gesehen" gezählt werden
  protected void setPrunedMap(Node n, int child) {
    int children = n.getChildren().size();
    for (int i = child + 1; i < children; i++) {
      Node childNode = n.getChildren().get(i);
      prunedMap.put(childNode.getId(), childNode);
      if (!childNode.isLeaf()) {
        setPrunedMap(childNode, -1);
      }
    }
  }

  // Zählt rekursiv alle Unterknoten inklusive dem, auf dem die Methode
  // aufgerufen wird
  protected int countNodes(Node n) {
    if (n.isLeaf()) {
      return 1;
    }
    int children = n.getChildren().size();
    int count = 1; // Elternknoten mitzaehlen
    for (int i = 0; i < children; i++) {
      count += countNodes(n.getChildren().get(i));
    }
    return count;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    String the_tree = (String) arg1.get("treeDefinition");
    if (the_tree.isEmpty()) {
      throw new IllegalArgumentException("The expression can not be empty.");
    } else {
      String tree = the_tree.replace(" ", "");
      String[] exp = tree.split("");
      for (int i = 1; i < exp.length; i++) {
        if (!exp[i].matches("[0-9A-Z\\{\\}\\-]")) {
          throw new IllegalArgumentException(
              "The expression can only contain the following characters: 0-9, -, A-Z, {, }");
        }
      }
    }
    return true;
  }

}