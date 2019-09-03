package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
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
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class JohnsonAlgoAPIGenerator implements Generator {
  private Language                 lang;
  private SourceCodeProperties     pseudoCodeProp;
  private SourceCodeProperties     informationProp;

  /**
   * Hilfsvariablen
   */
  private List<ArrayList<Integer>> helper = new ArrayList<ArrayList<Integer>>();
  private int[][]                  order;

  // private int timeOne;
  // private int timeTwo;

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   */
  public JohnsonAlgoAPIGenerator(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  public JohnsonAlgoAPIGenerator() {
    lang = new AnimalScript("Johnson [DE]", "Simone Baier", 800, 600);
    lang.setStepMode(true);

  }

  public void init() {
    lang = new AnimalScript("Johnson [DE]", "Simone Baier", 800, 600);
    lang.setStepMode(true);
    helper.clear();
    // timeOne = 0;
    // timeTwo = 0;

  }

  /**
   * such den minimalen Spannbaum für die Graph g
   * 
   * @param data
   *          Graph für den der minimale Spannbaum gefunden werden soll
   */
  public void sort(String[][] data) {

    // questions parts
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "First question group", 2);
    lang.addQuestionGroup(groupInfo);

    QuestionGroupModel groupInfoS = new QuestionGroupModel(
        "Second question group", 2);
    lang.addQuestionGroup(groupInfoS);

    // QuestionGroupModel groupInfoT = new QuestionGroupModel(
    // "Third question group", 3);
    // lang.addQuestionGroup(groupInfoT);

    Collections.sort(helper, new Comparator<ArrayList<Integer>>() {
      @Override
      public int compare(ArrayList<Integer> l1, ArrayList<Integer> l2) {
        if (l1.get(2) < l2.get(2))
          return -1;
        else if (l1.get(2) > l2.get(2))
          return 1;
        return 0;
      }
    });

    order = new int[3][data[0].length - 1];

    TextProperties textProps = new TextProperties();

    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));

    Text header = lang.newText(new Coordinates(20, 30), "Johnson-Algorithmus",
        "header", null, textProps);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    Rect hRect = lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5,
        5, header, "SE"), "hRect", null, rectProps);

    SourceCode scEin = lang.newSourceCode(new Offset(5, 50, hRect, "SW"),
        "scEin", null, informationProp);

    scEin
        .addCodeLine(
            "Der Johnson-Algorithmus ist ein Optimierungsverfahren für Warteschlangen in der Produktionswirtschaft.",
            null, 0, null); // 0
    scEin.addCodeLine("", null, 0, null); // 1
    scEin
        .addCodeLine(
            "Für eine Menge von Produktionsaufträgen, die jeweils zuerst auf Maschine M1 und danach auf Maschine M2",
            null, 0, null);// 2
    scEin
        .addCodeLine(
            "bearbeitet werden müssen, liefert der Algorithmus stets eine Reihenfolge mit kürzester Zykluszeit.",
            null, 0, null); // 3

    scEin.addCodeLine("", null, 0, null); // 4
    scEin
        .addCodeLine(
            "Die Zykluszeit ist dabei die Zeit, die eine Reihe von Produktionsaufträgen für die Produktion benötigt.",
            null, 0, null); // 5

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
            new String[] { " Hinweis: Bitte das Fenster ausreichend groß ziehen! Es muss im Laufe der Animation unten ein Diagramm zu erkennen sein!" },
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

    StringArray pseudo = lang.newStringArray(new Coordinates(20, 100),
        new String[] { " PSEUDO CODE: " }, "pseudo", null, apPseudo);

    SourceCode scPseudo = lang.newSourceCode(new Coordinates(20, 110),
        "scPseudo", null, pseudoCodeProp);

    scPseudo.addCodeLine("", null, 0, null); // 0
    scPseudo.addCodeLine("Solange noch nicht alle Aufträge zugeordnet sind",
        null, 0, null);// 1
    scPseudo
        .addCodeLine(
            "suche den Auftrag Ai mit der kürzesten (maschinenunabhängigen) absoluten Bearbeitungszeit",
            null, 2, null);// 2
    scPseudo
        .addCodeLine("falls die kürzeste Bearbeitungszeit auf Maschine 1 ist",
            null, 2, null); // 3
    scPseudo
        .addCodeLine(
            "dann ordne den Auftrag so weit vorne wie möglich in der Reihenfolge an",
            null, 4, null); // 4
    scPseudo
        .addCodeLine("falls die kürzeste Bearbeitungszeit auf Maschine 2 ist",
            null, 2, null); // 5
    scPseudo
        .addCodeLine(
            "dann ordne den Auftrag so weit hinten wie möglich in der Reihenfolge an ",
            null, 4, null); // 6
    scPseudo.addCodeLine("markiere den Auftrag als erfasst", null, 2, null); // 7
    scPseudo
        .addCodeLine(
            "Alle Aufträge sind derart sortiert, dass die Zykluszeit minimiert ist",
            null, 0, null); // 8

    lang.nextStep("Pseudo Code");

    // Question Start
    FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
        "fillInBlanksQuestion");
    fibq.setPrompt("Für wieviele Maschinen berechnet der Algorithmus die minimale Zykluszeit?");
    fibq.addAnswer("2", 10, "Korrekt...");
    fibq.addAnswer("zwei", 10, "Korrekt...");
    fibq.addAnswer("Zwei", 10, "Korrekt...");
    fibq.setGroupID("First question group");
    lang.addFIBQuestion(fibq);
    lang.nextStep();

    // Question End

    TextProperties tpVor = new TextProperties();
    tpVor.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    Text vor = lang
        .newText(
            new Offset(0, 200, pseudo, "SW"),
            "Ausgangssituation: unbestimmte Anzahl an Aufträgen, die jeweils auf zwei Maschinen nacheinander bearbeitet werden sollen",
            "vor", null, tpVor);

    MatrixProperties mpZeiten = new MatrixProperties();

    mpZeiten.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    mpZeiten.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.GREEN);
    mpZeiten.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    mpZeiten.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    mpZeiten.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
    mpZeiten.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GRAY);
    mpZeiten.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");
    mpZeiten.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

    StringMatrix zeiten = lang.newStringMatrix(
        new Offset(0, 80, scPseudo, "SW"), data, "zeiten", null, mpZeiten);

    zeiten.highlightCellColumnRange(0, 0, zeiten.getNrCols() - 1, null, null);
    zeiten.highlightCellColumnRange(1, 0, zeiten.getNrCols() - 1, null, null);
    zeiten.highlightCellColumnRange(2, 0, zeiten.getNrCols() - 1, null, null);

    lang.nextStep("Initialisierung");

    // Question Start
    int anzahl = data[0].length - 1;
    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion");
    mcq.setPrompt("Wieviele Interationen gibt es im konkreten Fall?");
    mcq.addAnswer(String.valueOf(anzahl - 1), 0,
        "Nein, es gibt soviele wie es Aufträge gibt!");
    mcq.addAnswer(String.valueOf(anzahl), 5, "Korrekt!");
    mcq.addAnswer(String.valueOf((anzahl + 8)), 0,
        "Falsch, soviele Aufträge gibt es nicht!");
    mcq.addAnswer(String.valueOf((anzahl + 1)), 0,
        "Falsch, soviele Aufträge gibt es nicht!");
    mcq.setGroupID("First question group");
    lang.addMCQuestion(mcq);

    // Question End

    Text ziel = lang
        .newText(
            new Offset(0, 60, zeiten, "SW"),
            "Gesucht: Reihenfolge in der die Aufträge bearbeitet werden, sodass die Zykluszeit minimiert wird",
            "ziel", null, tpVor);

    RectProperties zrProp = new RectProperties();
    zrProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    zrProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    zrProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    lang.newRect(new Offset(-10, -10, ziel, "NW"), new Offset(500, 800, ziel,
        "SE"), "zRect", null, zrProp);

    String[] rf = new String[data[0].length];
    rf[0] = " Reihenfolge: ";

    for (int c = 1; c < rf.length; c++) {
      rf[c] = "     ";
    }

    ArrayProperties rp = new ArrayProperties();
    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    rp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    rp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    rp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    rp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));

    StringArray reihe = lang.newStringArray(new Offset(0, 60, ziel, "SW"), rf,
        "reihe", null, rp);

    ArrayProperties apCounter = new ArrayProperties();
    apCounter.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    apCounter.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    apCounter.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    apCounter.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    apCounter.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    apCounter.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    apCounter.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));

    int ce = 0;
    int ca = data[0].length - 1;

    StringArray counter = lang.newStringArray(new Offset(0, -40, reihe, "NW"),
        new String[] { "  Aufträge:  " + ca,
            " Anzahl erfasste Aufträge: " + ce,
            " Anzahl noch zu erfassende Aufträge: " + (ca - ce) }, "counter",
        null, apCounter);

    lang.nextStep();

    int m1Pos = 1;
    int m2Pos = reihe.getLength() - 1;

    zeiten.unhighlightCellColumnRange(0, 0, zeiten.getNrCols() - 1, null, null);
    zeiten.unhighlightCellColumnRange(1, 0, zeiten.getNrCols() - 1, null, null);
    zeiten.unhighlightCellColumnRange(2, 0, zeiten.getNrCols() - 1, null, null);

    scPseudo.highlight(1);

    int itcounter = 1;

    while (!helper.isEmpty()) {

      counter.highlightCell(2, null, null);

      lang.nextStep("Iteration: " + itcounter);
      itcounter++;

      scPseudo.toggleHighlight(1, 2);

      counter.unhighlightCell(2, null, null);

      List<Integer> min = helper.remove(0);

      zeiten.highlightCellRowRange(0, 2, min.get(0) + 1, null, null);
      lang.nextStep();
      scPseudo.toggleHighlight(2, 3);
      zeiten.unhighlightCellRowRange(0, 2, min.get(0) + 1, null, null);

      if (min.get(1) == 1) {

        TrueFalseQuestionModel tfq = new TrueFalseQuestionModel("pos1", true, 5);
        tfq.setPrompt("Die Aussage: 'Der Auftrag wird soweit wie möglich vorne eingreiht' , ist...");

        // Feedback-nachrichten werden nicht angezeigt -->BUG?
        tfq.setFeedbackForAnswer(true,
            "Ja, denn die kürzeste Bearbeitungszeit ist auf Maschine 1!");
        tfq.setFeedbackForAnswer(false,
            "Falsch, denn die kürzeste Bearbeitungszeit ist auf Maschine 1!");
        tfq.setGroupID("Second question group");
        lang.addTFQuestion(tfq);

        zeiten.highlightElem(1, min.get(0) + 1, null, null);

        lang.nextStep();
        scPseudo.toggleHighlight(3, 4);
        reihe.highlightCell(m1Pos, null, null);
        reihe.put(m1Pos, zeiten.getElement(0, min.get(0) + 1), null, null);

        order[0][m1Pos - 1] = min.get(0);
        order[1][m1Pos - 1] = Integer.valueOf(zeiten.getElement(1,
            min.get(0) + 1));
        order[2][m1Pos - 1] = Integer.valueOf(zeiten.getElement(2,
            min.get(0) + 1));
        m1Pos++;
        ce++;
        counter.put(1, " Anzahl erfasste Aufträge: " + ce, null, null);
        counter.put(2, " Anzahl zu erfassende Aufträge: " + (ca - ce), null,
            null);
        lang.nextStep();

        reihe.unhighlightCell(m1Pos - 1, null, null);
        scPseudo.toggleHighlight(4, 7);

      } else {

        lang.nextStep();
        zeiten.highlightElem(2, min.get(0) + 1, null, null);
        scPseudo.toggleHighlight(3, 5);
        lang.nextStep();
        scPseudo.highlight(6);
        reihe.highlightCell(m2Pos, null, null);
        reihe.put(m2Pos, String.valueOf(zeiten.getElement(0, min.get(0) + 1)),
            null, null);
        m2Pos--;
        order[0][m2Pos] = min.get(0);
        order[1][m2Pos] = Integer.valueOf(zeiten.getElement(1, min.get(0) + 1));
        order[2][m2Pos] = Integer.valueOf(zeiten.getElement(2, min.get(0) + 1));

        ce++;
        counter.put(1, " Anzahl erfasste Aufträge: " + ce, null, null);
        counter.put(2, " Anzahl zu erfassende Aufträge: " + (ca - ce), null,
            null);

        lang.nextStep();

        reihe.unhighlightCell(m2Pos + 1, null, null);
        scPseudo.toggleHighlight(5, 7);
        scPseudo.unhighlight(6);

      }

      zeiten.getProperties().set(
          AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GRAY);
      zeiten.unhighlightCellRowRange(0, 2, min.get(0) + 1, null, null);
      zeiten.highlightElem(0, min.get(0) + 1, null, null);

      lang.nextStep();
      scPseudo.toggleHighlight(7, 1);

    }

    counter.highlightCell(2, null, null);
    lang.nextStep("Ausführungsreihenfolge");

    scPseudo.toggleHighlight(1, 8);
    reihe.highlightCell(0, reihe.getLength() - 1, null, null);
    counter.unhighlightCell(2, null, null);

    int i = 0;
    int j = 0;
    for (int k = 0; k < order[0].length; k++) {
      i += order[1][k];
      j = Math.max(i, j);
      j += order[2][k];
    }

    int gesamt = j;

    PolylineProperties pp = new PolylineProperties();
    pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);

    Node[] yAchse = { new Offset(100, 200, counter, "NW"),
        new Offset(100, 100, counter, "NW") };
    Polyline y = lang.newPolyline(yAchse, "y", new TicksTiming(200), pp);
    Node[] xAchse = { new Offset(0, 0, y, "SW"), new Offset(550, 0, y, "SW") };
    Polyline x = lang.newPolyline(xAchse, "x", new TicksTiming(200), pp);

    RectProperties rp1 = new RectProperties();
    rp1.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rp1.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rp1.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    TextProperties tp = new TextProperties();

    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));

    lang.newText(new Offset(-75, -35, x, "NW"), "Maschine 1", "ma1",
        new TicksTiming(200), tp);
    lang.newText(new Offset(-75, -70, x, "NW"), "Maschine 2", "ma2",
        new TicksTiming(200), tp);

    Color[] color = { Color.YELLOW, Color.RED, Color.LIGHT_GRAY, Color.CYAN,
        Color.ORANGE, Color.MAGENTA, Color.PINK };

    RectProperties[] rpCol = new RectProperties[order[0].length];
    for (int k = 0; k < order[0].length; k++) {
      rpCol[k] = new RectProperties();
      rpCol[k].set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
      rpCol[k].set(AnimationPropertiesKeys.FILL_PROPERTY, color[k
          % color.length]);
      rpCol[k].set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    }

    int t = 400;

    // erstelle den Ausführungsplan
    int l = 0;
    int l2 = 0;
    Rect[] bm1 = new Rect[order[0].length];
    Rect[] bm2 = new Rect[order[0].length];
    Text[] tm1 = new Text[order[0].length];
    Text[] tm2 = new Text[order[0].length];
    int k;
    for (k = 0; k < order[0].length; k++) {

      bm1[k] = lang.newRect(new Offset(l, -5, x, "NW"),
          new Offset((l + Math.round(((float) order[1][k] / gesamt * 500))),
              -40, x, "NW"), "bm1" + k, new TicksTiming(t * (k + 1)), rpCol[k]);

      l = l + Math.round(((float) order[1][k] / gesamt * 500));

      tm1[k] = lang.newText(
          new Offset(5, 5, bm1[k], "NW"),
          "A" + String.valueOf(order[0][k] + 1) + " ZE: "
              + String.valueOf(order[1][k]), "tm1" + k, new TicksTiming(t
              * (k + 1)), tp);

      l2 = Math.max(l, l2);
      bm2[k] = lang.newRect(new Offset(l2, -40, x, "NW"),
          new Offset((l2 + Math.round(((float) order[2][k] / gesamt * 500))),
              -80, x, "NW"), "bm2" + k, new TicksTiming(t * (k + 1)), rpCol[k]);
      tm2[k] = lang.newText(
          new Offset(5, 5, bm2[k], "NW"),
          "A" + String.valueOf(order[0][k] + 1) + " ZE: "
              + String.valueOf(order[2][k]), "tm1" + k, new TicksTiming(t
              * (k + 1)), tp);

      l2 = l2 + Math.round(((float) order[2][k] / gesamt * 500));

    }

    ArrayProperties apG = new ArrayProperties();
    apG.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    apG.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    apG.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    apG.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    apG.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    apG.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    apG.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));

    lang.newStringArray(new Offset(100, 30, x, "SW"),
        new String[] { "  Gesamte Zykluszeit:  " + gesamt
            + " Zeiteinheiten (ZE) " }, "ges", new ArrayDisplayOptions(
            new TicksTiming(t * (k + 1)), null, true), apG);

    lang.nextStep();

    counter.hide();
    pseudo.hide();
    scPseudo.hide();
    vor.hide();
    ziel.hide();
    zeiten.hide();

    lang.newStringArray(new Coordinates(20, 90), new String[] { " FAZIT: " },
        "fazit", null, apPseudo);

    SourceCode scEnde = lang.newSourceCode(new Offset(5, 50, hRect, "SW"),
        "scEnde", null, informationProp);

    scEnde
        .addCodeLine(
            "Der Algorithmus von Johnson berechnet für eine Menge an Produktionsaufträgen ",
            null, 0, null); // 0
    scEnde.addCodeLine(
        "die Bearbeitungsreihenfolge, die die Zykluszeit minimiert.", null, 0,
        null); // 1
    scEnde
        .addCodeLine(
            "Vorausgesetzt wird, dass die Aufträge jeweils nacheinander auf zwei Maschinen bearbeitet werden müssen.",
            null, 0, null);// 2
    scEnde
        .addCodeLine(
            "Der Algorithmus ist also auf den Anwendungsfall von zwei Maschinen beschränkt.",
            null, 0, null); // 3
    scEnde.addCodeLine("", null, 0, null);// 4
    scEnde.addCodeLine("Im gegebenen Beispiel wurde die Reihenfolge für "
        + order[0].length + " Aufträge bestimmt.", null, 0, null); // 5
    scEnde.addCodeLine("Die optimale Zykluszeit beträgt dabei " + gesamt
        + " Zeiteinheiten (ZE).", null, 0, null); // 6

    lang.nextStep("Fazit");

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    /*
     * setze die User-Spezifikationen
     */

    pseudoCodeProp = (SourceCodeProperties) props
        .getPropertiesByName("pseudoCode");
    informationProp = new SourceCodeProperties();
    informationProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    informationProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // falls nicht vorhanden setze Default-Werte
    if (pseudoCodeProp == null) {
      JOptionPane.showMessageDialog(null,
          "pseudoCodeProp==null ; Defaults are set");
      setDefaults();
    }

    if (primitives == null) {
      JOptionPane.showMessageDialog(null,
          "primitives are null; Defaults are set ");
    }

    // initialisiere Primitive
    int[] m1 = (int[]) primitives.get("BearbeitungszeitenMaschine_1");

    int[] m2 = (int[]) primitives.get("BearbeitungszeitenMaschine_2");

    String[] labels = (String[]) primitives.get("Auftragsnamen");

    if (!(m1.length == m2.length && m1.length == labels.length)) {
      JOptionPane
          .showMessageDialog(
              null,
              "Die Spaltenanzahl für \"BearbeitungszeitenMaschine_1\", \n \"BearbeitungszeitenMaschine_2\" und \"Auftragsnamen\" muss gleich sein!"
                  + "BM1: "
                  + m1.length
                  + " BM2: "
                  + m2.length
                  + " AN: "
                  + labels.length);
    }

    if (m1 != null && m2 != null) {
      // initialisiere Bearbeitungszeiten
      int size = Math.min(m1.length, m2.length);
      String[][] data = new String[3][size + 1];
      data[0][0] = "";
      data[1][0] = "Maschine 1";
      data[2][0] = "Maschine 2";

      for (int i = 0; i < size; i++) {
        ArrayList<Integer> m = new ArrayList<Integer>();
        m.add(0, i);
        if (m1[i] < m2[i]) {
          m.add(1, 1);
          m.add(2, m1[i]);
        } else {
          m.add(1, 2);
          m.add(2, m2[i]);
        }
        helper.add(m);
        data[0][i + 1] = labels[i];
        data[1][i + 1] = String.valueOf(m1[i]);
        data[2][i + 1] = String.valueOf(m2[i]);
//        timeOne += m1[i];
//        timeTwo += m2[i];
      }

      sort(data);
    }

    // falls keine verwendbaren Eingaben vorhanden, setzte Default-Graph
    else {
      JOptionPane.showMessageDialog(null,
          "primitives aren't avaiable; Defaults are set ");
      sort(getDefaultValues());
    }

    lang.finalizeGeneration();
    return lang.toString();
  }

  // public static void main(String[] args) {
  // // Create a new animation
  // // name, author, screen width, screen height
  // Language l = new AnimalScript("Kruskal Animation",
  // "Simone Baier <SimoneBaier@gmx.de>", 740, 580);
  // JohnsonAlgoAPIGenerator s = new JohnsonAlgoAPIGenerator(l);
  // s.setDefaults();
  // s.sort(s.getDefaultValues());
  // l.finalizeGeneration();
  // System.out.println(l);
  //
  // try { FileWriter f = new FileWriter("johnsonAlgo_APIGenerator.asu");
  // f.write(l.toString()); f.close(); } catch (IOException e) {
  // System.out.println("Fehler: " + e.toString()); }
  //
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

  private String[][] getDefaultValues() {

    int[] m1 = { 28, 71, 57, 31, 60 };
    int[] m2 = { 13, 94, 24, 68, 36 };
    // int[] m1 = {14, 12, 7, 13, 11};
    // int[] m2 = {4, 13, 8, 9, 14};
    String[] labels = { "A1", "A2", "A3", "A4", "A5" };

    int size = Math.min(m1.length, m2.length);
    String[][] data = new String[3][size + 1];
    data[0][0] = "";
    data[1][0] = "Maschine 1";
    data[2][0] = "Maschine 2";

    for (int i = 0; i < size; i++) {
      ArrayList<Integer> m = new ArrayList<Integer>();
      m.add(0, i);
      if (m1[i] < m2[i]) {
        m.add(1, 1);
        m.add(2, m1[i]);
      } else {
        m.add(1, 2);
        m.add(2, m2[i]);
      }
      helper.add(m);
      data[0][i + 1] = labels[i];
      data[1][i + 1] = String.valueOf(m1[i]);
      data[2][i + 1] = String.valueOf(m2[i]);
      // timeOne += m1[i];
//      timeTwo += m2[i];
    }

    return data;

  }

  public String getName() {
    return "Johnson [DE]";
  }

  public String getAlgorithmName() {
    return "Johnson [DE]";
  }

  public String getAnimationAuthor() {
    return "Simone Baier";
  }

  public String getDescription() {
    return "Der Johnson-Algorithmus berechnet für eine Menge von Produktionsauftr&auml;gen, die jeweils zuerst"
        + "\n"
        + "auf Maschine M1 und danach auf Maschine bearbeitet werden müssen, die Bearbeitungsreihenfolge"
        + "\n" + "mit k&uuml;rzester Zykluszeit.";
  }

  public String getCodeExample() {
    return "Solange noch nicht alle Aufträge zugeordnet sind"
        + "\n"
        + "	suche den Auftrag Ai mit der kürzesten (maschinenunabhängigen) absoluten Bearbeitungszeit"
        + "\n"
        + "	falls die kürzeste Bearbeitungszeit auf Maschine 1 ist"
        + "\n"
        + "		dann ordne den Auftrag so weit vorne wie möglich in der Reihenfolge an"
        + "\n"
        + "	falls die kürzeste Bearbeitungszeit auf Maschine 2 ist"
        + "\n"
        + "		dann ordne den Auftrag so weit hinten wie möglich in der Reihenfolge an"
        + "\n"
        + "	markiere den Auftrag als erfasst"
        + "\n"
        + "Alle Aufträge sind derart sortiert, dass die Zykluszeit minimiert ist";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}