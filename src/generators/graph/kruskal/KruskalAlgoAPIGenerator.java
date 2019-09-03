package generators.graph.kruskal;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class KruskalAlgoAPIGenerator implements Generator {
  private Language                 lang;
  private SourceCodeProperties     pseudoCodeProp;
  private SourceCodeProperties     informationProp;

  /**
   * Hilfsvariablen
   */
  private List<ArrayList<Integer>> visible   = new ArrayList<ArrayList<Integer>>();
  private List<ArrayList<Integer>> circle    = new ArrayList<ArrayList<Integer>>();
  private List<Integer>            helper    = new ArrayList<Integer>();
  private int                      kEnde;
  private int                      itcounter = 0;
  private int                      itcircle  = 0;

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   */
  public KruskalAlgoAPIGenerator(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  public KruskalAlgoAPIGenerator() {
    lang = new AnimalScript("Algorithmus von Kruskal [DE]", "Simone Baier",
        800, 600);
    lang.setStepMode(true);

  }

  public void init() {
    lang = new AnimalScript("Algorithmus von Kruskal [DE]", "Simone Baier",
        800, 600);
    lang.setStepMode(true);
    visible.clear();
    circle.clear();
    helper.clear();
    kEnde = 0;
    itcounter = 0;
    itcircle = 0;
  }

  /**
   * such den minimalen Spannbaum für die Graph g
   * 
   * @param g
   *          Graph für den der minimale Spannbaum gefunden werden soll
   */
  public void search(Graph g) {
    Graph graph = g;
    graph.hide();

    // questions parts
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "First question group", 1);
    lang.addQuestionGroup(groupInfo);

    QuestionGroupModel groupInfoS = new QuestionGroupModel(
        "Second question group", 3);
    lang.addQuestionGroup(groupInfoS);

    QuestionGroupModel groupInfoT = new QuestionGroupModel(
        "Third question group", 3);
    lang.addQuestionGroup(groupInfoT);

    TextProperties textProps = new TextProperties();

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));

    Text header = lang.newText(new Coordinates(20, 30),
        "Algorithmus von Kruskal", "header", null, textProps);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.orange);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    Rect hRect = lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5,
        5, header, "SE"), "hRect", null, rectProps);

    SourceCode scEin = lang.newSourceCode(new Offset(5, 50, hRect, "SW"),
        "scEin", null, informationProp);

    scEin
        .addCodeLine(
            "Der Algorithmus von Kruskal ist ein Algorithmus der Graphentheorie zur Berechnung minimaler Spannbäume",
            null, 0, null); // 0
    scEin.addCodeLine(
        "von bewerteten, endlichen, zusammenhängenden, ungerichteten Graphen.",
        null, 0, null);
    scEin.addCodeLine("", null, 0, null); // 3

    scEin
        .addCodeLine(
            "Die kürzesterzeste Kante bezeichnet dabei jeweils die Kante mit dem kleinsten Kantengewicht.",
            null, 0, null); // 4
    scEin
        .addCodeLine(
            "Die Grundidee ist, die Kanten in Reihenfolge aufsteigender Kantengewichte zu durchlaufen",
            null, 0, null); // 5
    scEin
        .addCodeLine(
            "und jede Kante zur Lösung hinzuzufügen, die mit allen zuvor gewählten Kanten keinen Kreis bildet.",
            null, 0, null); // 6
    scEin
        .addCodeLine(
            "Nach Abschluss des Algorithmus bilden die ausgewählten Kanten einen minimalen Spannbaum des Graphen.",
            null, 0, null); // 7
    scEin.addCodeLine("", null, 0, null); // 8
    scEin
        .addCodeLine(
            "Haben mehrere Kanten die gleichen Gewichte, ist der Algorithmus von Kruskal nicht-deterministisch,",
            null, 0, null); // 9
    scEin
        .addCodeLine(
            " d.h. er liefert unter Umständen beim wiederholten Ausführen unterschiedliche Ergebnisse.",
            null, 0, null); // 10

    ArrayProperties apInfo = new ArrayProperties();
    apInfo.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    apInfo.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    apInfo.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    apInfo.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED);
    apInfo.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));

    StringArray info = lang
        .newStringArray(
            new Offset(0, 100, scEin, "SW"),
            new String[] { " Hinweis: Bitte das Fenster ausreichend groß ziehen! Es müssen im Laufe der Animation zwei Graphen und darunter jeweils eine Liste zu erkennen sein." },
            "info", null, apInfo);

    lang.nextStep("Einleitung");

    info.hide();
    scEin.hide();

    ArrayProperties apPseudo = new ArrayProperties();
    apPseudo.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    apPseudo.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    apPseudo.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    apPseudo.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    StringArray pseudo = lang.newStringArray(new Coordinates(20, 115),
        new String[] { " PSEUDO CODE: " }, "pseudo", null, apPseudo);

    SourceCode scPseudo = lang.newSourceCode(new Coordinates(20, 120),
        "scPseudo", null, pseudoCodeProp);

    scPseudo.addCodeLine("", null, 0, null); // 0
    scPseudo.addCodeLine(
        "Sortiere alle Kanten aufsteigend nach ihrem Kantengwicht in Liste L",
        null, 0, null);
    scPseudo
        .addCodeLine(
            "Setze E':=Ø  und T:=[V,E'] (T zu Beginn: Teilgraph von G ohne Kanten)",
            null, 0, null);
    scPseudo.addCodeLine("solange L nicht leer", null, 0, null); // 3
    scPseudo.addCodeLine("wähle kleinste Kante in L", null, 2, null); // 4
    scPseudo.addCodeLine("entferne Kante aus L", null, 2, null); // 5
    scPseudo.addCodeLine(
        "wenn die Aufnahme der Kante in T=[V,E'] keinen Kreis erzeugt, ", null,
        2, null); // 6
    scPseudo.addCodeLine("füge die Kante zu E' hinzu, ", null, 4, null); // 7
    scPseudo.addCodeLine("wenn E' genau n - 1 Kanten enthält", null, 2, null); // 8
    scPseudo.addCodeLine("dann Abbruch", null, 4, null); // 9
    scPseudo.addCodeLine("T=[V,E'] ist ein minimaler Spannbaum von G", null, 0,
        null); // 10

    lang.nextStep("Pseudo Code");

    MultipleSelectionQuestionModel msq = new MultipleSelectionQuestionModel(
        "multipleSelectionQuestion");
    msq.setPrompt("Welche Voraussetzungen muss der Graph erfüllen?");
    msq.addAnswer("gerichtet", -1, "Nein, er muss ungerichtet sein...");
    msq.addAnswer("zusammenhängend", 1, "Korrekt!");
    msq.addAnswer("bewertet", 1, "Ja,....");
    msq.addAnswer("unendlich", -1, "Definitiv nicht....");
    msq.setGroupID("First question group");
    lang.addMSQuestion(msq);

    lang.nextStep();

    TextProperties tpVor = new TextProperties();
    tpVor.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.ITALIC, 16));
    Text vor = lang
        .newText(
            new Offset(5, 25, hRect, "SW"),
            "Voraussetzung: bewerteter, endlicher, zusammenhängender , ungerichteter Graph G = [V,E,w] mit n Knoten und m Kanten ",
            "vor", null, tpVor);

    graph.show();

    TextProperties tpGraphG = new TextProperties();

    tpGraphG.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    Text graphG = lang.newText(new Offset(0, 20, graph, "S"), " Graph G ",
        "graphG", null, tpGraphG);

    Rect gRect = lang.newRect(new Offset(-5, -5, graphG, "NW"), new Offset(5,
        5, graphG, "SE"), "gRect", null, rectProps);

    lang.nextStep("Initialisierung");

    scPseudo.highlight(1);

    int[][] data = graph.getAdjacencyMatrix();

    ArrayList<ArrayList<Integer>> kantenName = new ArrayList<ArrayList<Integer>>();

    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[0].length; j++) {
        ArrayList<Integer> kante = new ArrayList<Integer>();
        kante.add(0, i);
        kante.add(1, j);
        kante.add(2, data[i][j]);
        kantenName.add(kante);
      }
    }

    Collections.sort(kantenName, new Comparator<ArrayList<Integer>>() {
      @Override
      public int compare(ArrayList<Integer> l1, ArrayList<Integer> l2) {
        if (l1.get(2) < l2.get(2))
          return -1;
        else if (l1.get(2) > l2.get(2))
          return 1;
        return 0;
      }
    });

    while (kantenName.get(0).get(2) == 0) {
      kantenName.remove(0);
    }
    int kAnzahl = kantenName.size();

    String[][] kData = new String[3][kAnzahl + 1];

    for (int c = 1; c < kAnzahl + 1; c++) {
      kData[0][c] = "";
    }
    kData[0][0] = "Liste L";
    kData[1][0] = "Kanten";
    kData[2][0] = "Wert";

    int listPos = 1;
    for (int i = 0; i < kAnzahl; i++) {
      List<Integer> k = kantenName.get(i);
      if (k.get(2) != 0) {
        kData[1][listPos] = "[" + graph.getNodeLabel(k.get(0)) + ","
            + graph.getNodeLabel(k.get(1)) + "]";
        kData[2][listPos] = String.valueOf(k.get(2));
        listPos++;
      }
    }

    MatrixProperties mpKanten = new MatrixProperties();

    mpKanten.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    mpKanten.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.GREEN);
    mpKanten.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);

    mpKanten.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
    mpKanten.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    mpKanten.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");

    StringMatrix kanten = lang.newStringMatrix(new Offset(0, 100, graph, "SW"),
        kData, "kanten", null, mpKanten);

    kanten.highlightCellColumnRange(0, 0, kanten.getNrCols() - 1, null, null);
    kanten.highlightCellColumnRange(1, 0, kanten.getNrCols() - 1, null, null);
    kanten.highlightCellColumnRange(2, 0, kanten.getNrCols() - 1, null, null);

    lang.nextStep();

    // Es ist stets nur eine Frage vom typ MCQ möglich, fügt man weitere hinzu,
    // wird die vorige überschrieben -->BUG?
    // deshalb wird diese Frage ausgeklammert
    // // Question Start
    // MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
    // "multipleChoiceQuestion");
    // mcq.setPrompt("Wieviele Interationen gibt es maximal?");
    // mcq.addAnswer(String.valueOf(graph.getSize() - 1), 0,
    // "Nein, soviele muss es mindestens geben!");
    // mcq.addAnswer(String.valueOf(kAnzahl), 5,
    // "Korrekt!");
    // mcq.addAnswer(String.valueOf((kAnzahl + 8)), 0,
    // "Falsch, soviele Kanten gibt es nicht!");
    // mcq.addAnswer(String.valueOf((kAnzahl + 1)), 0,
    // "Falsch, soviele Kanten gibt es nicht!");
    // mcq.setGroupID("First question group");
    // lang.addMCQuestion(mcq);
    //
    // // Question End

    // Question Start
    // just for questions
    List<Integer> member = new ArrayList<Integer>();
    boolean isMember = false;
    for (ArrayList<Integer> ele : kantenName) {
      if (member.contains(ele.get(2))) {
        isMember = true;
        break;
      }
      member.add(ele.get(2));
    }
    if (isMember) {

      MultipleChoiceQuestionModel mcqS = new MultipleChoiceQuestionModel(
          "multipleChoiceQuestion");
      mcqS.setPrompt("Wäre auch eine andere Reihenfolge in der Liste L möglich gewesen?");
      mcqS.addAnswer("Ja", 10,
          "Richtig, denn mehrere Kanten haben das gleiche Gewicht!");
      mcqS.addAnswer("Nein", 0,
          "Falsch, denn mehrere Kanten haben das gleiche Gewicht!");
      mcqS.setGroupID("Second question group");
      lang.addMCQuestion(mcqS);
      lang.nextStep();

    }
    // Question End

    int size = graph.getSize();
    int x = 580 + 300 * (size / 7);
    int y = 270;
    int m = 250;

    String[] nodesMinLabels = new String[size];
    for (int i = 0; i < size; i++) {
      nodesMinLabels[i] = graph.getNodeLabel(i);
    }

    Node[] nodesMin = new Node[size];
    for (int i = 0; i < Math.ceil((double) size / 10); i++) {
      if (0 + 10 * i > size - 1)
        break;
      nodesMin[0 + 10 * i] = new Coordinates((x + 100), (y + 200 + m * i));
      if (1 + 10 * i > size - 1)
        break;
      nodesMin[1 + 10 * i] = new Coordinates(x + 180, y + 100 + m * i);
      if (2 + 10 * i > size - 1)
        break;
      nodesMin[2 + 10 * i] = new Coordinates(x + 250, y + 200 + m * i);
      if (3 + 10 * i > size - 1)
        break;
      nodesMin[3 + 10 * i] = new Coordinates(x + 180, y + 300 + m * i);
      if (4 + 10 * i > size - 1)
        break;
      nodesMin[4 + 10 * i] = new Coordinates(x + 400, y + 100 + m * i);
      if (5 + 10 * i > size - 1)
        break;
      nodesMin[5 + 10 * i] = new Coordinates(x + 400, y + 300 + m * i);
      if (6 + 10 * i > size - 1)
        break;
      nodesMin[6 + 10 * i] = new Coordinates(x + 500, y + 200 + m * i);
      if (7 + 10 * i > size - 1)
        break;
      nodesMin[7 + 10 * i] = new Coordinates(x + 640, y + 100 + m * i);
      if (8 + 10 * i > size - 1)
        break;
      nodesMin[8 + 10 * i] = new Coordinates(x + 640, y + 300 + m * i);
      if (9 + 10 * i > size - 1)
        break;
      nodesMin[9 + 10 * i] = new Coordinates(x + 800, y + 200 + m * i);
    }

    Graph graphMin = lang.newGraph("graphMin", graph.getAdjacencyMatrix(),
        nodesMin, nodesMinLabels, null, graph.getProperties());
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[0].length; j++) {
        if (data[i][j] != 0) {
          graphMin.hideEdge(graphMin.getNode(i), graphMin.getNode(j), null,
              null);
        }
      }
    }

    Text graphT = lang.newText(new Offset(0, 20, graphMin, "S"), " Graph T ",
        "graphT", null, tpGraphG);

    Rect tRect = lang.newRect(new Offset(-5, -5, graphT, "NW"), new Offset(5,
        5, graphT, "SE"), "tRect", null, rectProps);

    String[][] kminData = new String[2][kantenName.size() + 2];
    for (int c = 1; c < kantenName.size() + 2; c++) {
      kminData[0][c] = "";
      kminData[1][c] = "";
    }
    kminData[0][0] = "Kanten";
    kminData[1][0] = "Wert";
    StringMatrix kantenMin = lang.newStringMatrix(new Offset(270, 30, kanten,
        "NE"), kminData, "kantenMin", null, mpKanten);

    scPseudo.toggleHighlight(1, 2);
    kanten.unhighlightCellColumnRange(0, 0, kanten.getNrCols() - 1, null, null);
    kanten.unhighlightCellColumnRange(1, 0, kanten.getNrCols() - 1, null, null);
    kanten.unhighlightCellColumnRange(2, 0, kanten.getNrCols() - 1, null, null);

    for (int i = 0; i < graphMin.getSize(); i++) {
      graphMin.highlightNode(i, null, null);
    }

    kantenMin
        .highlightCellRowRange(0, kantenMin.getNrRows() - 1, 0, null, null);

    ArrayProperties apCounter = new ArrayProperties();
    apCounter.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    apCounter.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    apCounter.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    apCounter.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    apCounter.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    apCounter.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    apCounter.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));

    int listMinPos = 0;
    StringArray counter = lang
        .newStringArray(
            new Offset(0, -30, kantenMin, "NW"),
            new String[] { " E' :", " Anzahl: " + listMinPos,
                " n = " + String.valueOf(graphMin.getSize()),
                "  Anzahl untersuchte Kannten:  " + itcounter,
                "  Anzahl entdeckte Kreise:  " + itcircle }, "counter", null,
            apCounter);

    lang.nextStep();

    for (int i = 0; i < graphMin.getSize(); i++) {
      graphMin.unhighlightNode(i, null, null);
    }

    kantenMin.unhighlightCellRowRange(0, kantenMin.getNrRows() - 1, 0, null,
        null);
    scPseudo.toggleHighlight(2, 3);

    lang.nextStep("Iteration: " + itcounter);
    itcounter++;

    listPos = 1;

    while (listPos <= kantenName.size()) {
      ArrayList<Integer> kante = kantenName.get(listPos - 1);
      graphMin.showEdge(kante.get(0), kante.get(1), null, null);
      graphMin.highlightEdge(kante.get(0), kante.get(1), null, null);
      kanten.highlightCellRowRange(0, kanten.getNrRows() - 1, listPos, null,
          null);
      scPseudo.toggleHighlight(3, 4);

      lang.nextStep();

      scPseudo.toggleHighlight(4, 5);
      kanten.unhighlightCellRowRange(0, kanten.getNrRows() - 1, listPos, null,
          null);
      kanten.put(1, listPos, "", null, null);
      kanten.put(2, listPos, "", null, null);

      lang.nextStep();

      circle.clear();
      helper.clear();
      circle.add(kante);
      kEnde = kante.get(1);

      helper.add(kante.get(0));
      helper.add(kante.get(1));

      if (!hasCircle(kante, visible)) {

        visible.add(kante);
        listMinPos++;
        scPseudo.toggleHighlight(5, 6);
        scPseudo.highlight(7);
        graphMin.unhighlightEdge(kante.get(0), kante.get(1), null, null);
        kantenMin.put(0, listMinPos, "[" + graphMin.getNodeLabel(kante.get(0))
            + "," + graphMin.getNodeLabel(kante.get(1)) + "]", null, null);
        kantenMin.put(1, listMinPos, String.valueOf(kante.get(2)), null, null);
        kantenMin.highlightCellRowRange(0, kantenMin.getNrRows() - 1,
            listMinPos, null, null);
        counter.put(1, " Anzahl: " + listMinPos, null, null);
        counter.highlightElem(1, null, null);

        lang.nextStep();

        scPseudo.unhighlight(6);
        scPseudo.toggleHighlight(7, 8);
        counter.highlightElem(2, null, null);
        kantenMin.unhighlightCellRowRange(0, kantenMin.getNrRows() - 1,
            listMinPos, null, null);

      } else {

        TrueFalseQuestionModel tfq = new TrueFalseQuestionModel("hasCircle",
            false, 5);
        tfq.setPrompt("Die Aussage: 'Die Kante wird zum minimalen Spannbaum hinzugefügt' , ist...");

        // Feedback-nachrichten werden nicht angezeigt -->BUG?
        tfq.setFeedbackForAnswer(true,
            "Nein, denn die Kannte bildet einen Kreis!");
        tfq.setFeedbackForAnswer(false, "Korrekt!");
        tfq.setGroupID("Third question group");
        lang.addTFQuestion(tfq);
        lang.nextStep();

        itcircle++;
        counter.put(4, "  Anzahl entdeckte Kreise:  " + itcircle, null, null);

        // bestimme die Kanten die den Kreis bilden
        Collections.sort(helper);
        int c = helper.size();

        List<Integer> he = new ArrayList<Integer>();
        for (int i = 0; i < c;) {

          if (helper.get(0) != helper.get(1)) {
            he.add(helper.remove(0));
            i++;
          }

          else {
            helper.remove(0);
            helper.remove(0);
            i = i + 2;
          }
          if (helper.size() == 1) {
            he.add(helper.get(0));
            break;
          }

        }

        if (!he.isEmpty()) {
          for (int j = 0; j < circle.size(); j++) {
            if (circle.get(j).get(0) == he.get(0)
                && he.contains(circle.get(j).get(1))) {

              he.remove(0);
              he.remove(he.lastIndexOf(circle.get(j).get(1)));
              circle.remove(j);
              break;
            }
          }
        }

        while (!he.isEmpty()) {
          for (int j = 0; j < circle.size(); j++) {
            if (circle.get(j).get(1) == he.get(0)
                || circle.get(j).get(0) == he.get(0)) {
              he.remove(0);
              circle.remove(j);
              break;
            }
          }
        }

        // mache den Kreis kenntlich
        // da Änderungen von GraphProperties nicht umgesetzt werden, erstelle
        // neuen Graph um die Kreisfarbe zu ändern

        graphMin.hide();

        GraphProperties gpCircle = new GraphProperties();
        gpCircle.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.TRUE);
        gpCircle.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
        gpCircle.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLUE);
        gpCircle.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        gpCircle.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        gpCircle
            .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);

        Graph graphCircle = lang.newGraph("graphCirlce",
            graph.getAdjacencyMatrix(), nodesMin, nodesMinLabels, null,
            gpCircle);
        for (int i = 0; i < data.length; i++) {
          for (int j = 0; j < data[0].length; j++) {
            if (data[i][j] != 0) {
              graphCircle.hideEdge(graphMin.getNode(i), graphMin.getNode(j),
                  null, null);
            }
          }
        }

        for (List<Integer> ele : visible) {
          graphCircle.showEdge(ele.get(0), ele.get(1), null, null);
        }
        for (List<Integer> ele : circle) {
          graphCircle.showEdge(ele.get(0), ele.get(1), null, null);
          graphCircle.highlightEdge(ele.get(0), ele.get(1), null, null);
        }

        scPseudo.toggleHighlight(5, 6);

        lang.nextStep();

        // nehme wieder den Ursprungsgraphen
        graphCircle.hide();
        graphMin.show();
        for (int i = 0; i < data.length; i++) {
          for (int j = 0; j < data[0].length; j++) {
            if (data[i][j] != 0) {
              graphMin.hideEdge(graphMin.getNode(i), graphMin.getNode(j), null,
                  null);
            }
          }
        }

        for (List<Integer> ele : visible) {
          graphMin.showEdge(ele.get(0), ele.get(1), null, null);
        }

        lang.nextStep();

        counter.highlightElem(1, null, null);
        counter.highlightElem(2, null, null);
        scPseudo.toggleHighlight(6, 8);

      }

      counter.put(3, "  Anzahl untersuchte Kannten:  " + itcounter, null, null);
      lang.nextStep();
      if (listMinPos + 1 == graphMin.getSize())
        break;

      counter.unhighlightElem(1, null, null);
      counter.unhighlightElem(2, null, null);
      scPseudo.toggleHighlight(8, 3);

      lang.nextStep("Iteration: " + itcounter);
      itcounter++;
      listPos++;
    }

    counter.unhighlightElem(1, null, null);
    counter.unhighlightElem(2, null, null);
    scPseudo.toggleHighlight(8, 9);

    lang.nextStep();

    int weightAll = 0;
    for (List<Integer> ele : visible) {
      weightAll += ele.get(2);
    }

    scPseudo.toggleHighlight(9, 10);
    kantenMin.put(0, listMinPos + 2, "minimal spannender Baum", null, null);
    kantenMin.put(1, listMinPos + 2, String.valueOf(weightAll), null, null);
    kantenMin.highlightCellRowRange(0, kantenMin.getNrRows() - 1,
        listMinPos + 2, null, null);

    lang.nextStep("Fazit");

    kanten.hide();
    kantenMin.hide();
    graph.hide();
    graphMin.hide();
    graphG.hide();
    graphT.hide();
    counter.hide();
    pseudo.hide();
    scPseudo.hide();
    vor.hide();
    gRect.hide();
    tRect.hide();

    SourceCode scEnde = lang.newSourceCode(new Offset(5, 50, hRect, "SW"),
        "scEnde", null, informationProp);

    scEnde
        .addCodeLine(
            "Der Algorithmus von Kruskal berechnet einen minimalen Spannbaum für einen gegebenen Graphen.",
            null, 0, null); // 0
    scEnde
        .addCodeLine(
            "Dieser muss bewertetet, endlich, zusammenhängend, und ungerichtet sein.",
            null, 0, null);
    scEnde.addCodeLine("", null, 0, null);
    scEnde
        .addCodeLine(
            "Im gegebenen Beispiel erfolgt die Berechnung für einen Graphen bestehend aus "
                + graph.getSize() + " Knoten und " + kantenName.size()
                + " Kanten.", null, 0, null); // 3
    scEnde.addCodeLine("Der resultierende Spannbaum hat ein Gewicht von "
        + weightAll + ".", null, 0, null); // 4
    scEnde.addCodeLine("", null, 0, null); // 5
    scEnde.addCodeLine("Insgesamt wurden " + itcounter + " Kanten untersucht.",
        null, 0, null); // 6
    scEnde
        .addCodeLine(
            "Dabei kam es "
                + itcircle
                + " Mal zur Kreisbildung, sodass die betrachtete Kannte nicht zum minmalen Spannbaum hinzugefügt werden konnte.",
            null, 0, null); // 7

  }

  private boolean hasCircle(List<Integer> lastEdge,
      List<ArrayList<Integer>> visible) {

    if (visible.size() == 0) {
      return false;
    }

    int start = lastEdge.get(0);
    int end = lastEdge.get(1);

    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < visible.size(); j++) {
        if (visible.get(j).get(i) == start || visible.get(j).get(i) == end) {
          if (visible.get(j).get((i + 1) % 2) == kEnde) {
            helper.add(visible.get(j).get(0));
            helper.add(visible.get(j).get(1));

            circle.add(visible.get(j));
            return true;
          }
          List<ArrayList<Integer>> visibleReduce = new ArrayList<ArrayList<Integer>>();
          visibleReduce.addAll(visible);
          visibleReduce.remove(j);
          circle.add(visible.get(j));
          helper.add(visible.get(j).get(0));
          helper.add(visible.get(j).get(1));
          if ((hasCircle(visible.get(j), visibleReduce))) {
            return true;
          }
          circle.remove(visible.get(j));
          helper.remove(helper.lastIndexOf(visible.get(j).get(0)));
          helper.remove(helper.lastIndexOf(visible.get(j).get(1)));

        }
      }

    }

    circle.remove(lastEdge);
    return false;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    /*
     * setze die User-Spezifikationen
     */

    pseudoCodeProp = (SourceCodeProperties) props
        .getPropertiesByName("pseudoCodeProp");
    informationProp = (SourceCodeProperties) props
        .getPropertiesByName("informationProp");
    // Graph graph = (Graph) primitives.get("graph");

    // falls nicht vorhanden setze Default-Werte
    if (pseudoCodeProp == null || informationProp == null) {

      JOptionPane.showMessageDialog(null,
          "pseudoCodeProp==null || informationProp == null; Defaults are set");
      setDefaults();
    }

    if (primitives == null) {
      JOptionPane.showMessageDialog(null,
          "primitives are null; Defaults are set ");
    }

    // initialisiere Primitive
    int[][] ama = (int[][]) primitives.get("adjazenzMatrix");
    String[] labels = (String[]) primitives.get("knotenNamen");
    if (ama.length != ama[0].length) {
      JOptionPane.showMessageDialog(null,
          "adjazenzMatrix muss symmetrisch sein! ");
    }
    if (ama.length != labels.length) {
      JOptionPane
          .showMessageDialog(
              null,
              "adjazenzMatrix muss genauso viele Spalten wie knotenName haben und symmetrisch sein! + Anzahl Spalten"
                  + ama.length + "  Anzahl Knoten:" + labels.length);
    }

    // "baue" AdjazenzMatris des Graphen
    if (ama != null && labels != null) {
      int[][] am = new int[ama.length][ama[0].length];

      for (int i = 0; i < am.length; i++) {
        for (int j = 0; j < am[0].length; j++) {
          am[i][j] = 0;
        }
      }
      for (int i = 0; i < am.length; i++) {
        for (int j = i + 1; j < am[0].length; j++) {
          am[i][j] = ama[i][j];
        }
      }
      int x = 20;
      int y = 270;
      int m = 250;

      int size = labels.length;
      Node[] nodes = new Node[size];

      // initialisiere Knoten
      for (int i = 0; i < Math.ceil((double) size / 10); i++) {
        if (0 + 10 * i > size - 1)
          break;
        ;
        nodes[0 + 10 * i] = new Coordinates(x + 100, y + 200 + m * i);
        if (1 + 10 * i > size - 1)
          break;
        nodes[1 + 10 * i] = new Coordinates(x + 180, y + 100 + m * i);
        if (2 + 10 * i > size - 1)
          break;
        nodes[2 + 10 * i] = new Coordinates(x + 250, y + 200 + m * i);
        if (3 + 10 * i > size - 1)
          break;
        nodes[3 + 10 * i] = new Coordinates(x + 180, y + 300 + m * i);
        if (4 + 10 * i > size - 1)
          break;
        nodes[4 + 10 * i] = new Coordinates(x + 400, y + 100 + m * i);
        if (5 + 10 * i > size - 1)
          break;
        nodes[5 + 10 * i] = new Coordinates(x + 400, y + 300 + m * i);
        if (6 + 10 * i > size - 1)
          break;
        nodes[6 + 10 * i] = new Coordinates(x + 500, y + 200 + m * i);
        if (7 + 10 * i > size - 1)
          break;
        nodes[7 + 10 * i] = new Coordinates(x + 640, y + 100 + m * i);
        if (8 + 10 * i > size - 1)
          break;
        nodes[8 + 10 * i] = new Coordinates(x + 640, y + 300 + m * i);
        if (9 + 10 * i > size - 1)
          break;
        nodes[9 + 10 * i] = new Coordinates(x + 800, y + 200 + m * i);
      }

      GraphProperties graphProps = new GraphProperties();
      graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.TRUE);
      graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
      graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLUE);
      graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
      graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
          Color.GREEN);
      graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
          Color.GREEN);

      // erstelle Graph
      Graph graph = lang.newGraph("graph", am, nodes, labels, null, graphProps);

      search(graph);
    }

    // falls keine verwendbaren Eingaben vorhanden, setzte Default-Graph
    else {
      JOptionPane.showMessageDialog(null,
          "primitives aren't avaiable; Defaults are set ");
      search(getDefaultStartGraph());
    }
    lang.finalizeGeneration();
    return lang.toString();
  }

  // public static void main(String[] args) {
  // // Create a new animation
  // // name, author, screen width, screen height
  // Language l = new AnimalScript("Kruskal Animation",
  // "Simone Baier <SimoneBaier@gmx.de>", 740, 580);
  // KruskalAlgoAPIGenerator s = new KruskalAlgoAPIGenerator(l);
  // s.setDefaults();
  // s.search(s.getDefaultStartGraph());
  // l.finalizeGeneration();
  // System.out.println(l);
  //
  // try {
  // FileWriter f = new FileWriter("kruskalAlgo_APIGeneratorV2.asu");
  // f.write(l.toString());
  // f.close();
  // } catch (IOException e) {
  // System.out.println("Fehler: " + e.toString());
  // }
  //
  // }

  private void setDefaults() {

    informationProp = new SourceCodeProperties();
    informationProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    informationProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    pseudoCodeProp = new SourceCodeProperties();
    pseudoCodeProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    pseudoCodeProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    pseudoCodeProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
  }

  private Graph getDefaultStartGraph() {

    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.TRUE);
    graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLUE);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    graphProps
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);

    int x = 20;
    int y = 270;
    int m = 250;
    // String[] labels = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
    // "K", "L" };

    String[] labels = { "A", "B", "C", "D", "E", "F" };
    int size = labels.length;
    Node[] nodes = new Node[size];

    for (int i = 0; i < Math.ceil((double) size / 10); i++) {
      if (0 + 10 * i > size - 1)
        break;
      nodes[0 + 10 * i] = new Coordinates(x + 100, y + 200 + m * i);
      if (1 + 10 * i > size - 1)
        break;
      nodes[1 + 10 * i] = new Coordinates(x + 180, y + 100 + m * i);
      if (2 + 10 * i > size - 1)
        break;
      nodes[2 + 10 * i] = new Coordinates(x + 250, y + 200 + m * i);
      if (3 + 10 * i > size - 1)
        break;
      nodes[3 + 10 * i] = new Coordinates(x + 180, y + 300 + m * i);
      if (4 + 10 * i > size - 1)
        break;
      nodes[4 + 10 * i] = new Coordinates(x + 400, y + 100 + m * i);
      if (5 + 10 * i > size - 1)
        break;
      nodes[5 + 10 * i] = new Coordinates(x + 400, y + 300 + m * i);
      if (6 + 10 * i > size - 1)
        break;
      nodes[6 + 10 * i] = new Coordinates(x + 500, y + 200 + m * i);
      if (7 + 10 * i > size - 1)
        break;
      nodes[7 + 10 * i] = new Coordinates(x + 640, y + 100 + m * i);
      if (8 + 10 * i > size - 1)
        break;
      nodes[8 + 10 * i] = new Coordinates(x + 640, y + 300 + m * i);
      if (9 + 10 * i > size - 1)
        break;
      nodes[9 + 10 * i] = new Coordinates(x + 800, y + 200 + m * i);
    }

    int[][] am = { { 0, 4, 2, 5, 0, 0 }, { 0, 0, 4, 0, 3, 0 },
        { 0, 0, 0, 4, 3, 5 }, { 0, 0, 0, 0, 0, 6 }, { 0, 0, 0, 0, 0, 4 },
        { 0, 0, 0, 0, 0, 0 } };

    // int[][] am = { { 0, 4, 2, 5, 0, 0, 0, 0, 0, 0, 0, 0 },
    // { 0, 0, 4, 0, 3, 0, 0, 0, 0, 0, 0, 0 },
    // { 0, 0, 0, 4, 3, 5, 1, 2, 3, 4, 5, 6 },
    // { 0, 0, 0, 0, 0, 6, 1, 2, 3, 4, 5, 6 },
    // { 0, 0, 0, 0, 0, 4, 1, 2, 3, 4, 5, 6 },
    // { 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6 },
    // { 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 5, 6 },
    // { 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 5, 6 },
    // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 5, 6 },
    // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 6 },
    // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6 },
    // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    Graph graph = lang.newGraph("graph", am, nodes, labels, null, graphProps);

    return graph;

  }

  public String getName() {
    return "Kruskal";
  }

  public String getAlgorithmName() {
    return "Kruskal";
  }

  public String getAnimationAuthor() {
    return "Simone Baier";
  }

  public String getDescription() {
    return "Der Algorithmus von Kruskal ist ein Algorithmus der Graphentheorie zur Berechnung minimaler Spannb&auml;ume"
        + "\n"
        + "von bewerteten, endlichen, zusammenh&auml;ngenden, ungerichteten Graphen.";
  }

  public String getCodeExample() {
    return "Sortiere alle Kanten aufsteigend nach ihrem Kantengweicht in Liste L"
        + "\n"
        + "Setze E'=&empty; und T:=[V,E'] (T zu Beginn: Teilgraph von G ohne Kanten) "
        + "\n"
        + "solange L nicht leer "
        + "\n"
        + "          w&auml;hle kleinste Kante in L"
        + "\n"
        + "          entferne Kante aus L"
        + "\n"
        + "          wenn die Aufnahme der Kante in T=[V,E'] keinen Kreis erzeugt,"
        + "\n"
        + "                    f&uuml;ge die Kante zu E' hinzu,"
        + "\n"
        + "          wenn E' genau n - 1 Kanten enth&auml;lt"
        + "\n"
        + "                    dann Abbruch\" // 8"
        + "\n"
        + "T=[V,E'] ist ein minimaler Spannbaum von G";
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
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}