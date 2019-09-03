package generators.graph.kruskal;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Kruskal2 implements Generator {
  private Language             lang;
  private RectProperties       rectColor;
  private int[]                KantenGewichte;
  private int[][]              Adjazenmatrix;
  private String[]             LabelName;
  private SourceCodeProperties sourceCodeColor;
  private Text                 info;
  private Text                 header;
  private Rect                 hRect;

  // private TextProperties textProps;
  // private Polyline line;

  public void init() {
    lang = new AnimalScript("Kruskal [DE]",
        "Florian Schmidt, Katja Rabea Sonnenschein", 800, 600);
    lang.setStepMode(true);
  }

  public void QuestionAnfang() {
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    TrueFalseQuestionModel tfq = new TrueFalseQuestionModel("trueFalseQuestion"
        + UUID.randomUUID(), true, 5);
    tfq.setPrompt("Ein minimaler Spannbaum verbindet alle Knoten des ursprünglichen Graphen mit den minimal gewichteten Kanten");
    tfq.setGroupID("Second question group");
    lang.addTFQuestion(tfq);
  }

  public void Intro(RectProperties rectProps) {
    TextProperties textProps = new TextProperties();
    textProps.set("font", new Font("Serif", 1, 18));
    header = lang.newText(new Coordinates(20, 30), "Algorithmus von Kruskal",
        "header", null, textProps);
    rectProps.set("filled", true);
    rectProps.set("depth", 2);
    hRect = lang.newRect(
        new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5,
            5, "header", "SE"), "hRect", null, rectProps);
    lang.nextStep("Intro - Allgemeines");

    TextProperties textProps1 = new TextProperties();
    textProps1.set("font", new Font("Serif", 1, 18));
    info = lang.newText(new Coordinates(20, 90), "Allgemeines", "general",
        null, textProps1);

    TextProperties textProps2 = new TextProperties();
    textProps2.set("font", new Font("Serif", 0, 14));
    TextProperties textProps3 = new TextProperties();
    textProps3.set("font", new Font("Serif", 2, 14));
    info = lang
        .newText(
            new Coordinates(20, 119),
            "Der Algorithmus von Kurskal ist ein Algorithmus der Graphentheorie zur Berechnung minimaler Spannbaeume.",
            "general1", null, textProps2);
    info = lang
        .newText(
            new Coordinates(20, 136),
            "Ein minimaler Spannbaum ist ein Teilgraph, der alle Knoten des Ursprungsgraphen enthaelt und sie mit minimalem Gewicht verbindet.",
            "general2", null, textProps2);
    info = lang
        .newText(
            new Coordinates(20, 152),
            "Der Ursprungsgraph muss zusammenhaengend, gewichtet und endlich sein.",
            "general3", null, textProps2);
    info = lang
        .newText(
            new Coordinates(20, 168),
            "Entwickelt wurde der Algorithmus von Joseph Kruskal im Jahre 1956. Er beschreibt den Algorithmus wie folgt:",
            "general4", null, textProps2);
    info = lang.newText(new Coordinates(20, 184),
        "    Fuehre den folgenden Schritt so oft wie moeglich aus:",
        "general5", null, textProps3);
    info = lang
        .newText(
            new Coordinates(20, 200),
            "          Waehle unter den noch nicht ausgewaehlten Kanten G (des Graphen) die kuerzeste Kante,",
            "general6", null, textProps3);
    info = lang.newText(new Coordinates(20, 216),
        "          die mit den schon gewaehlten Kanten keinen Kreis bildet.",
        "general7", null, textProps3);
    info = lang
        .newText(
            new Coordinates(20, 242),
            "Die kuerzeste Kante bezeichnet dabei jeweils die Kante mit dem kleinsten Kantengewicht. Nach Abschluss des",
            "general8", null, textProps2);
    info = lang
        .newText(
            new Coordinates(20, 258),
            "Algorithmus bilden die ausgewaehlten Kanten den minimalen Spannbaum des Graphen.",
            "general9", null, textProps2);

    lang.nextStep();
    QuestionAnfang();

    lang.nextStep("Intro - Beschreibung des Algorithmus");
    lang.addLine("hideAll");
    header.show();
    hRect.show();
    TextProperties textProps4 = new TextProperties();
    textProps4.set("font", new Font("Serif", 1, 14));
    info = lang.newText(new Coordinates(20, 90),
        "Beschreibung des Algorithmus", "Description", null, textProps1);
    info = lang
        .newText(
            new Coordinates(20, 119),
            "Die Grundidee besteht darin die Kanten in Reihenfolge aufsteigender Kantengewichte zu durchlaufen und jede Kante",
            "Description1", null, textProps2);
    info = lang
        .newText(
            new Coordinates(20, 136),
            "zur Loesung hinzuzufuegen, die mit allen zuvor gewaehlten Kanten keinen Zyklus bildet (sonst wird die Kante verworfen).",
            "Description2", null, textProps2);
    info = lang
        .newText(
            new Coordinates(20, 152),
            "Somit werden sukzessiv sogenannte Komponenten zum minimalen Spannbaum verbunden.",
            "Description3", null, textProps2);
    info = lang
        .newText(
            new Coordinates(20, 168),
            "Kruskals Algorithmus ist ein Greedy-Algorithmus, da er dem Wald in jedem Schritt eine Kante hinzufuegt,",
            "Description4", null, textProps2);
    info = lang.newText(new Coordinates(20, 184),
        "die das kleinste moegliche Gewicht hat.", "Description5", null,
        textProps2);
    info = lang.newText(new Coordinates(20, 210), "Input", "Input", null,
        textProps4);
    info = lang
        .newText(
            new Coordinates(20, 226),
            "Als Eingabe dient ein zusammenhaengender, gewichteter Graph G = (V,E,w). V bezeichnet die Menge der",
            "Input1", null, textProps2);
    info = lang
        .newText(
            new Coordinates(20, 242),
            "Knoten (vertices), E die Menge der Kanten (edges). Die Gewichtsfunktion w: E --> R ordnet jeder Kante",
            "Input2", null, textProps2);
    info = lang.newText(new Coordinates(20, 258), "ein Kantengewicht zu.",
        "Input3", null, textProps2);
    info = lang.newText(new Coordinates(20, 284), "Output", "Output", null,
        textProps4);
    info = lang
        .newText(
            new Coordinates(20, 300),
            "Der Algorithmus liefert einen minimalen Spannbaum M = (V,E') mit E' als Teilmenge von E",
            "Output1", null, textProps2);
    info = lang.newText(new Coordinates(20, 336), "Algorithmus", "Algo", null,
        textProps4);

    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    SourceCode src = lang.newSourceCode(new Coordinates(20, 352), "sourceCode",
        null, scProps);
    src.addCodeLine(
        "G = (V,E,w): ein zusammenhaengender, ungerichteter, kantengewichteter Graph",
        null, 0, null);
    src.addCodeLine("kruskal (G)", null, 0, null);
    src.addCodeLine(" E' <-- null", null, 1, null);
    src.addCodeLine(" L  <-- E", null, 1, null);
    src.addCodeLine(
        " Sortiere die Kanten in L aufsteigend nach ihrem Kantengewicht", null,
        1, null);
    src.addCodeLine(" Solange L ungleich null", null, 1, null);
    src.addCodeLine(
        " waehle eine Kante e aus der Menge L mit kleinstem Kantengewicht",
        null, 2, null);
    src.addCodeLine(
        " entferne die Kante e aus L, wenn der Graph (V, E' vereinigt {e}) keinen Zyklus enthaelt",
        null, 2, null);
    src.addCodeLine(" dann E' <-- E' vereinigt {e}", null, 2, null);
    src.addCodeLine("M = (v,E') ist ein minimaler Spannbaum von G", null, 1,
        null);
    lang.nextStep("Initialisierung - Aufbauen des Ursprungsgraphen");

  }

  public void graph(SourceCodeProperties scProps, int[] weights,
      String[] labels, int[][] graphAdjacencyMatrix) {
    lang.addLine("hideAll");
    header.show();
    hRect.show();

    TextProperties textProps5 = new TextProperties();
    textProps5.set("font", new Font("Serif", 0, 14));
    info = lang
        .newText(
            new Coordinates(20, 60),
            "Im Folgenden werden die Kanten des Graphen in aufsteigender Reihenfolge sortiert",
            "Erklaerung", null, textProps5);

    // Definiere die Kanten des Graphen
    // int[][] graphAdjacencyMatrix = new int[6][6];
    // for (int i=0; i < graphAdjacencyMatrix.length; i++)
    // for(int j=0; j < graphAdjacencyMatrix.length; j++)
    // graphAdjacencyMatrix[i][j] = 0;

    int z = 0;
    for (int x = 0; x < graphAdjacencyMatrix.length; x++) {
      for (int y = 0; y < graphAdjacencyMatrix.length; y++) {
        if (graphAdjacencyMatrix[x][y] == 1 && z < weights.length) {
          graphAdjacencyMatrix[x][y] = weights[z];
          graphAdjacencyMatrix[y][x] = 0;
          z++;
        }
      }
    }

    // Kantengewichte setzten
    // graphAdjacencyMatrix[0][1] = weights[0];
    // graphAdjacencyMatrix[0][2] = weights[1];
    // graphAdjacencyMatrix[1][2] = weights[2];
    // graphAdjacencyMatrix[1][3] = weights[3];
    // graphAdjacencyMatrix[2][3] = weights[4];
    // graphAdjacencyMatrix[2][4] = weights[5];
    // graphAdjacencyMatrix[3][5] = weights[6];
    // graphAdjacencyMatrix[4][5] = weights[7];

    // Definiere die Nodes und deren Position
    Node[] graphNodes = new Node[6];
    graphNodes[0] = new Coordinates(100, 80);
    graphNodes[1] = new Coordinates(170, 80);
    graphNodes[2] = new Coordinates(30, 150);
    graphNodes[3] = new Coordinates(240, 150);
    graphNodes[4] = new Coordinates(100, 220);
    graphNodes[5] = new Coordinates(170, 220);

    // Eigenschaften des Graphen
    GraphProperties graphProps = new GraphProperties();
    graphProps
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
    graphProps.set("weighted", true);
    graphProps.set("fillColor", Color.white);

    // Initialisierung des Graphen
    Graph g = lang.newGraph("Kru", graphAdjacencyMatrix, graphNodes, labels,
        null, graphProps);

    // Tabelle zur Kantesortierung
    // info = lang.newText(new Coordinates(300,100), "Kante:", "Tabelle",null,
    // textProps5);
    // info = lang.newText(new Coordinates(370,100), "Laenge:", "Tabelle1",null,
    // textProps5);
    // info = lang.newText(new Coordinates(440,100), "Auswahl:",
    // "Tabelle2",null, textProps5);
    //
    // Node [] lineNodes = new Node[2];
    // lineNodes[0] = new Coordinates(285, 112);
    // lineNodes[1] = new Coordinates(510, 112);
    // line = lang.newPolyline(lineNodes, "Line1",null);
    //
    // Node [] lineNodes1 = new Node[2];
    // lineNodes1[0] = new Coordinates(350, 90);
    // lineNodes1[1] = new Coordinates(350, 240);
    // line = lang.newPolyline(lineNodes1, "Line2",null);
    //
    // Node [] lineNodes2 = new Node[2];
    // lineNodes2[0] = new Coordinates(425, 90);
    // lineNodes2[1] = new Coordinates(425, 240);
    // line = lang.newPolyline(lineNodes2, "Line3",null);

    SourceCode src = lang.newSourceCode(new Coordinates(700, 40),
        "sourceCode1", null, scProps);
    src.addCodeLine(
        "G = (V,E,w): ein zusammenhaengender, ungerichteter, kantengewichteter Graph",
        null, 0, null);
    src.addCodeLine("kruskal (G)", null, 0, null);
    src.addCodeLine(" E' <-- null", null, 1, null);
    src.addCodeLine(" L  <-- E", null, 1, null);
    src.addCodeLine(
        " Sortiere die Kanten in L aufsteigend nach ihrem Kantengewicht", null,
        1, null);
    src.addCodeLine(" Solange L ungleich null", null, 1, null);
    src.addCodeLine(
        " waehle eine Kante e aus der Menge L mit kleinstem Kantengewicht",
        null, 2, null);
    src.addCodeLine(
        " entferne die Kante e aus L, wenn der Graph (V, E' vereinigt {e}) keinen Zyklus enthaelt",
        null, 2, null);
    src.addCodeLine(" dann E' <-- E' vereinigt {e}", null, 2, null);
    src.addCodeLine("M = (v,E') ist ein minimaler Spannbaum von G", null, 1,
        null);

    // Füllen der Tabelle mit den sortierten Kanten
    tableFuel(g, src, textProps5, graphAdjacencyMatrix, weights);

    // Beginn des Aufbaus des minimalen Spannbaums
    buildTree(src, textProps5, labels, graphAdjacencyMatrix, weights,
        graphProps);

  }

  public void buildTree(SourceCode src, TextProperties textProps5,
      String[] labels, int[][] graphAdjacencyMatrix, int[] weights,
      GraphProperties graphProps) {
    lang.nextStep();
    src.highlight(5, 0, false);
    src.highlight(6, 0, false);
    src.highlight(7, 0, false);
    src.highlight(8, 0, false);

    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);

    int[][] miniAdjacencyMatrix = new int[6][6];
    for (int i = 0; i < miniAdjacencyMatrix.length; i++)
      for (int j = 0; j < miniAdjacencyMatrix.length; j++)
        miniAdjacencyMatrix[i][j] = 0;

    Node[] miniNodes = new Node[6];
    miniNodes[0] = new Coordinates(100, 420);
    miniNodes[1] = new Coordinates(170, 420);
    miniNodes[2] = new Coordinates(30, 490);
    miniNodes[3] = new Coordinates(240, 490);
    miniNodes[4] = new Coordinates(100, 560);
    miniNodes[5] = new Coordinates(170, 560);

    Arrays.sort(weights);
    int x = 0;
    Integer o;
    int maxEdges = 5;
    int m = 1;
    int n = 115;
    Integer d = 1;
    Integer gesWeight = 0;
    int[][] adj = new int[6][6];
    for (int p = 0; p < 6; p++) {
      for (int q = 0; q < 6; q++) {
        adj[p][q] = 0;
      }
    }
    // Graph mini1;
    boolean c;
    while (x < weights.length) {
      for (int i = 0; i < 6; i++) {
        for (int j = 0; j < 6; j++) {
          o = graphAdjacencyMatrix[i][j];
          if (o != null && graphAdjacencyMatrix[i][j] == weights[x]
              && weights[x] != 0) {
            c = noCycle(adj, i, j);
            if (c && m <= maxEdges) {
              adj[i][j] = 1;
              adj[j][i] = 1;
              gesWeight += weights[x];
              questionKanteWirdHinzugefuegt(i + 1, j + 1);
              lang.nextStep();
              info = lang.newText(new Coordinates(700, 320), "Erklaerung:",
                  "Erklaer-1", null, textProps5);
              info = lang.newText(new Coordinates(460, n), d.toString(),
                  "Tabelle-17", null, textProps5);
              String erklaerung = "Auswahl der Kante (" + (i + 1) + ","
                  + (j + 1) + ") mit der Gewichtung " + weights[x];
              info = lang.newText(new Coordinates(715, 340), erklaerung,
                  "Erklaer-2", null, textProps5);
              miniAdjacencyMatrix[i][j] = graphAdjacencyMatrix[i][j];

              lang.newGraph("SpannTree", miniAdjacencyMatrix, miniNodes,
                  labels, null, graphProps);
              lang.nextStep();
              info.hide();
              m++;
              d++;
              n += 15;
            } else {
              if (m <= maxEdges) {
                questionKanteWirdNichtHinzugefuegt(i + 1, j + 1);
                lang.nextStep();
                info = lang.newText(new Coordinates(460, n), "Reject",
                    "Tabelle-17", null, textProps5);
                String erklaerung1 = "Auswahl der Kante ("
                    + (i + 1)
                    + ","
                    + (j + 1)
                    + ") fuehrt zu einem Zyklus, daher wird er verworfen (Reject) ";
                info = lang.newText(new Coordinates(715, 340), erklaerung1,
                    "Erklaer-2", null, textProps5);
                miniAdjacencyMatrix[i][j] = graphAdjacencyMatrix[i][j];
                // mini1 =
                // lang.newGraph("SpannTree",miniAdjacencyMatrix,miniNodes,labels,null,graphProps);
                grapherstellen1(i, j, miniAdjacencyMatrix, miniNodes, labels,
                    graphProps);
                n += 15;
                lang.nextStep();
                info.hide();

              }
            }

          }

        }
      }
      x++;
    }
    src.unhighlight(5, 0, false);
    src.unhighlight(6, 0, false);
    src.unhighlight(7, 0, false);
    src.unhighlight(8, 0, false);
    src.highlight(9, 0, false);
    info = lang.newText(new Coordinates(20, 600),
        "Die Laenge des minimalen Spannbaums betraegt " + gesWeight.toString(),
        "Solution", null, textProps5);
    lang.nextStep("Outro-Abschluss");

  }

  public void grapherstellen(int x, int[][] miniAdjacencyMatrix,
      Node[] miniNodes, String[] labels, GraphProperties graphProps, int i) {

    switch (x) {
      case 0:
        lang.newGraph("SpannTree", miniAdjacencyMatrix, miniNodes, labels,
            null, graphProps);
        break;
      case 1:
        lang.newGraph("SpannTree", miniAdjacencyMatrix, miniNodes, labels,
            null, graphProps);
        break;
      case 2:
        lang.newGraph("SpannTree", miniAdjacencyMatrix, miniNodes, labels,
            null, graphProps);
        break;
      case 3:
        lang.newGraph("SpannTree", miniAdjacencyMatrix, miniNodes, labels,
            null, graphProps);
        break;
      case 4:
        lang.newGraph("SpannTree", miniAdjacencyMatrix, miniNodes, labels,
            null, graphProps);
        break;
      case 5:
        lang.newGraph("SpannTree", miniAdjacencyMatrix, miniNodes, labels,
            null, graphProps);
        break;
      case 6:
        lang.newGraph("SpannTree", miniAdjacencyMatrix, miniNodes, labels,
            null, graphProps);
        break;
    }
  }

  public void grapherstellen1(int i, int j, int[][] miniAdjacencyMatrix,
      Node[] miniNodes, String[] labels, GraphProperties graphProps) {
    Graph mini7 = lang.newGraph("SpannTree", miniAdjacencyMatrix, miniNodes,
        labels, null, graphProps);
    mini7.highlightEdge(i, j, null, null);
    lang.nextStep();
    mini7.hideEdge(i, j, null, null);
    miniAdjacencyMatrix[i][j] = 0;
    mini7.hide();
  }

  // Berechnet, ob ein Zyklus entsteht
  public boolean noCycle(int[][] E, int from, int to) {
    int[][] adj = new int[6][6];
    for (int p = 0; p < 6; p++) {
      for (int q = 0; q < 6; q++) {
        adj[p][q] = 0;
      }
    }
    return noCycle1(E, adj, from, to);
  }

  public boolean noCycle1(int[][] E, int[][] R, int from, int to) {
    if (E[from][to] == 1) {
      return false;
    }
    for (int x = 0; x < 6; x++) {
      if (E[from][x] == 1 && R[from][x] != 1) {
        R[from][x] = 1;
        R[x][from] = 1;
        return noCycle1(E, R, x, to);
      }
    }
    return true;
  }

  public void tableFuel(Graph g, SourceCode src, TextProperties textProps5,
      int[][] graphAdjacencyMatrix, int[] weights) {
    info = lang.newText(new Coordinates(300, 100), "Kante:", "Tabelle", null,
        textProps5);
    info = lang.newText(new Coordinates(370, 100), "Laenge:", "Tabelle1", null,
        textProps5);
    info = lang.newText(new Coordinates(440, 100), "Auswahl:", "Tabelle2",
        null, textProps5);

    int laenge = 135;

    Node[] lineNodes = new Node[2];
    lineNodes[0] = new Coordinates(285, 112);
    lineNodes[1] = new Coordinates(510, 112);
    lang.newPolyline(lineNodes, "Line1", null);

    Table(laenge);

    Arrays.sort(weights);
    int x = 0;
    Integer o;
    int a = 115;
    int b = 115;
    while (x < weights.length) {
      for (int i = 0; i < 6; i++) {
        for (int j = 0; j < 6; j++) {
          o = graphAdjacencyMatrix[i][j];
          if (o != null && graphAdjacencyMatrix[i][j] == weights[x]
              && weights[x] != 0) {
            lang.nextStep();
            for (int c = 0; c < 6; c++) {
              for (int d = 0; d < 6; d++) {
                if (c != i && d != j) {
                  g.unhighlightEdge(c, d, null, null);
                }
              }
            }
            src.highlight(4, 0, false);
            Integer w = weights[x];
            String edge = "(" + (i + 1) + "," + (j + 1) + ")";
            g.highlightEdge(i, j, null, null);

            Table(laenge);
            laenge += 15;

            info = lang.newText(new Coordinates(300, a), edge, "Tabelle-" + x,
                null, textProps5);
            info = lang.newText(new Coordinates(380, b), w.toString(),
                "Tabelle" + x + 1, null, textProps5);
            a += 15;
            b += 15;
          }
        }
      }
      x++;
    }

    lang.nextStep("Aufbau des minimalen Spannbaums");
    g.unhighlightEdge(1, 3, null, null);
    src.unhighlight(4, 0, false);
    info = lang
        .newText(
            new Coordinates(20, 350),
            "Nun bauen wir mit Hilfe der in aufsteigender Reihenfolge sortierten Kanten den minimalen",
            "Erklaerung-1", null, textProps5);
    info = lang.newText(new Coordinates(20, 365),
        "Spannbaum. Dies sieht in den einzelnen Schritten wie folgt aus:",
        "Tabelle-16", null, textProps5);

    for (int c = 0; c < 6; c++) {
      for (int d = 0; d < 6; d++) {
        g.unhighlightEdge(c, d, null, null);
      }
    }
  }

  public void Table(int laenge) {
    Node[] lineNodes1 = new Node[2];
    lineNodes1[0] = new Coordinates(350, 90);
    lineNodes1[1] = new Coordinates(350, laenge);
    lang.newPolyline(lineNodes1, "Line2", null);

    Node[] lineNodes2 = new Node[2];
    lineNodes2[0] = new Coordinates(425, 90);
    lineNodes2[1] = new Coordinates(425, laenge);
    lang.newPolyline(lineNodes2, "Line3", null);
  }

  public void questionKanteWirdHinzugefuegt(int i, int j) {
    Integer a = i;
    Integer b = j;
    MultipleChoiceQuestionModel mcq2 = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion" + UUID.randomUUID());
    String qu = "Wird die Kante (" + a.toString() + "," + b.toString()
        + ") hinzugefuegt?";
    mcq2.setPrompt(qu);
    mcq2.addAnswer("Ja", 1,
        "Richtig, da die neue Kante einen neuen Knoten zur Menge hinzufuegt.");
    mcq2.addAnswer("Nein", 0, "Falsch, die Kante wird hinzugefuegt.");
    mcq2.setGroupID("Kante wird hinzugefuegt");
    lang.addMCQuestion(mcq2);
  }

  public void questionKanteWirdNichtHinzugefuegt(int i, int j) {
    Integer a = i;
    Integer b = j;
    MultipleChoiceQuestionModel mcq1 = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion1" + UUID.randomUUID());
    String qu = "Wird die Kante (" + a.toString() + "," + b.toString()
        + ") hinzugefuegt?";
    mcq1.setPrompt(qu);
    mcq1.addAnswer("Ja", 0, "Falsch, denn dann wuerde ein Zyklus entstehen.");
    mcq1.addAnswer(
        "Nein",
        1,
        "Richtig, da sonst ein Zyklus entstehen wuerde und dies die EIgenschaften eines Minimalen Spannbaums verletzt.");
    mcq1.setGroupID("Kante wird verworfen");
    lang.addMCQuestion(mcq1);
  }

  public void QuestionEnde1() {
    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion" + UUID.randomUUID());
    mcq.setPrompt("Bei welchem Algorithmus kann während der Ausführung ein Wald entstehen?");
    mcq.addAnswer("Kruskal", 5, "Richtige Antwort!");
    mcq.addAnswer("Prim", 0, "Leider Falsch. Prim hat immer einen Baum!");
    mcq.setGroupID("First question group");
    lang.addMCQuestion(mcq);
  }

  public void QuestionEnde2() {
    MultipleChoiceQuestionModel mcq1 = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion" + UUID.randomUUID());
    mcq1.setPrompt("Wie viel Kanten ein minimaler Spannbaum?");
    mcq1.addAnswer("Soviele wie es Knoten gibt", 0, "Falsch!");
    mcq1.addAnswer("AnzahlKnoten -1", 5, "Richtig!");
    mcq1.addAnswer("Soviele Kanten wie der Ursprungsgraph hat", 0,
        "Leider Falsch! Korrekt ist: AnzahlKnoten -1!");
    mcq1.addAnswer("Soviele wie es Knoten gibt", 0,
        "Leider Falsch! Korrekt ist: AnzahlKnoten -1 !");
    mcq1.setGroupID("First question group");
    lang.addMCQuestion(mcq1);
  }

  public void Outro() {
    lang.addLine("hideAll");
    header.show();
    hRect.show();
    TextProperties textProps6 = new TextProperties();
    textProps6.set("font", new Font("Serif", 0, 14));
    info = lang
        .newText(
            new Coordinates(20, 89),
            "Das Ergebnis des Algorithmus von Kruskal ist der minimale Spannbaum eines zusammenhaengenden, gewichteten und endlichen Graphs.",
            "Outro", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 106),
            "Eine Alternative zum Algorithmus von Kruskal ist der Prim Algorithmus, der zum gleichen Ergebnis fuehrt.",
            "Outro1", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 122),
            "Beim Prim Algorithmus wird in jedem Zwischenschritt ein Baum als Zwischenergebnis zurueckgegeben, waehrend bei Kruskal das Zwischenergebnis auch ein Wald sein kann.",
            "Outro2", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 148),
            "Bemerkung: Wird der Algorithmus auf unzusammenhaengenden Graphen angewendet, so berechnet er fuer jede Zusammenhangs-",
            "Outro3", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 164),
            "	                  komponente des Graphen einen minimalen Spannbaum. Diese Baeume bilden einen minimalen aufspannenden Wald.",
            "Outro4", null, textProps6);
    lang.nextStep();
    QuestionEnde1();
    lang.nextStep();
    QuestionEnde2();
  }

  public void Start(RectProperties rectColor,
      SourceCodeProperties sourceCodeColor, int[] KantenGewichte,
      String[] LabelName, int[][] Adjazenmatrix) {
    Intro(rectColor);
    graph(sourceCodeColor, KantenGewichte, LabelName, Adjazenmatrix);
    Outro();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    rectColor = (RectProperties) props.getPropertiesByName("rectColor");
    KantenGewichte = (int[]) primitives.get("KantenGewichte");
    Adjazenmatrix = (int[][]) primitives.get("Adjazenmatrix");
    LabelName = (String[]) primitives.get("LabelName");
    sourceCodeColor = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeColor");

    Start(rectColor, sourceCodeColor, KantenGewichte, LabelName, Adjazenmatrix);

    lang.finalizeGeneration();

    return lang.toString();
  }

  public String getName() {
    return "Kruskal [DE]";
  }

  public String getAlgorithmName() {
    return "Kruskal";
  }

  public String getAnimationAuthor() {
    return "Florian Schmidt, Katja Rabea Sonnenschein";
  }

  public String getDescription() {
    return "Der Algorithmus von Kurskal ist ein Algorithmus der Graphentheorie zur Berechnung minimaler Spannbaeume."
        + "\n"
        + "Ein minimaler Spannbaum ist ein Teilgraph, der alle Knoten des Ursprungsgraphen enthaelt und sie mit minimalem Gewicht verbindet."
        + "\n"
        + "Der Ursprungsgraph muss zusammenhaengend, gewichtet und endlich sein."
        + "\n"
        + "Entwickelt wurde der Algorithmus von Joseph Kruskal im Jahre 1956. Er beschreibt den Algorithmus wie folgt:"
        + "\n"
        + "    Fuehre den folgenden Schritt so oft wie moeglich aus:"
        + "\n"
        + "          Waehle unter den noch nicht ausgewaehlten Kanten G (des Graphen) die kuerzeste Kante,"
        + "\n"
        + "          die mit den schon gewaehlten Kanten keinen Kreis bildet."
        + "\n"
        + "Die kuerzeste Kante bezeichnet dabei jeweils die Kante mit dem kleinsten Kantengewicht. Nach Abschluss des"
        + "\n"
        + "Algorithmus bilden die ausgewaehlten Kanten den minimalen Spannbaum des Graphen.";
  }

  public String getCodeExample() {
    return "G = (V,E,w): ein zusammenhaengender, ungerichteter, kantengewichteter Graph"
        + "\n"
        + "kruskal (G)"
        + "\n"
        + "     E' <-- null"
        + "\n"
        + "     L  <-- E"
        + "\n"
        + "     Sortiere die Kanten in L aufsteigend nach ihrem Kantengewicht"
        + "\n"
        + "     Solange L ungleich null"
        + "\n"
        + "          waehle eine Kante e aus der Menge L mit kleinstem Kantengewicht"
        + "\n"
        + "          entferne die Kante e aus L, wenn der Graph (V, E' vereinigt {e}) keinen Zyklus enthaelt"
        + "\n"
        + "          dann E' <-- E' vereinigt {e}"
        + "\n"
        + "     M = (v,E') ist ein minimaler Spannbaum von G";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}