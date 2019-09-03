package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author Rolf Egert, Andreas Pesak
 * @version 0.9 2012-08-23
 * 
 */

public class Brown implements Generator {

  /**
   * The concrete language object used for creating output
   */
  Language                     lang;

  private double               alpha;

  private int[]                Bt;

  private double[]             D1;

  private double[]             D2;

  private double               a;

  private double               b;

  private double[]             Bt1;

  private SourceCode           formula;

  private Rect                 calcRect;

  private SourceCode           calc;

  // properties
  private TextProperties       headerProps;
  private RectProperties       rectProps;
  private TextProperties       boldTextProps;
  private TextProperties       normalTextProps;
  private TextProperties       italicTextProps;
  private SourceCodeProperties formulaProps;
  private SourceCodeProperties calcProps;
  private RectProperties       calcRectProps;
  private PolylineProperties   Bt_polyProps;
  private PolylineProperties   Bt1_polyProps;

  public void init() {
    lang = new AnimalScript(
        "Exponentielle Glättung 2. Ordnung nach Brown [DE]",
        "Rolf Egert, Andreas Pesak", 800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    QuestionGroupModel groupInfo = new QuestionGroupModel("qgroup", 1);
    lang.addQuestionGroup(groupInfo);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    Bt = (int[]) primitives.get("Bt");
    alpha = (Double) primitives.get("alpha");
    rectProps = (RectProperties) props.getPropertiesByName("rectProps");
    calcRectProps = (RectProperties) props.getPropertiesByName("calcRectProps");
    Bt_polyProps = (PolylineProperties) props
        .getPropertiesByName("Bt_polyProps");
    Bt1_polyProps = (PolylineProperties) props
        .getPropertiesByName("Bt1_polyProps");

    // setting
    Bt1 = new double[Bt.length];
    D1 = new double[Bt.length];
    D2 = new double[Bt.length];

    // properties

    formulaProps = new SourceCodeProperties();
    formulaProps
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    formulaProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    formulaProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));

    headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 24));

    boldTextProps = new TextProperties();
    boldTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));

    normalTextProps = new TextProperties();
    normalTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));

    italicTextProps = new TextProperties();
    italicTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.ITALIC, 16));

    calcProps = new SourceCodeProperties();
    calcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    calcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    calcProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    drawDescription();

    drawTable();

    calc();

    drawEndDescription();

    lang.finalizeGeneration();

    return lang.toString();
  }

  public void drawDescription() {
    List<Text> texts = new LinkedList<Text>();
    // header
    lang.newText(new Coordinates(20, 20),
        "Exponentielle Glättung 2. Ordnung nach Brown", "header", null,
        headerProps);

    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRectCalc",
        null, rectProps);

    lang.nextStep("Description Part 1");

    // First page of description
    texts.add(lang.newText(new Coordinates(20, 50),
        "Allgemeine Beschreibung:", "", null, boldTextProps));
    lang.nextStep();
    texts.add(lang.newText(
        new Coordinates(20, 80),
        "Für die Bestimmung des zukünftigen Materialbedarfs können 2 Formen unterschieden werden.",
        "", null, normalTextProps));
    lang.nextStep();
    texts.add(lang.newText(
        new Coordinates(20, 100),
        "Einmal die deterministische Bedarfsermittlung, welche Stücklisten nutzt um den Materialbedarf abzuleiten,",
        "", null, normalTextProps));
    texts.add(lang.newText(
        new Coordinates(20, 120),
        "andererseits die stochastische Bedarfsermittlung, deren Prognose sich auf vergangene Verbräuche stützt.",
        "", null, normalTextProps));
    lang.nextStep();
    texts.add(lang.newText(
        new Coordinates(20, 140),
        "Der Vorteil der stochastischen Variante liegt in geringerem Aufwand, da lediglich die Bedarfszahlen der letzten Perioden",
        "", null, normalTextProps));
    texts.add(lang.newText(new Coordinates(20, 160),
        "betrachtet werden müssen.", "", null, normalTextProps));
    lang.nextStep();
    texts.add(lang.newText(
        new Coordinates(20, 200),
        "Die Exponentielle Glättung 2. Ordnung nach Brown ist ein spezielles Verfahren der stochastischen Form,",
        "", null, normalTextProps));
    texts.add(lang.newText(
        new Coordinates(20, 220),
        "welches Verwendung findet, wenn in der Zeitreihe der Verbräuche ein Trend (linearer Bedarfsverlauf) stark zu vermuten ist.",
        "", null, normalTextProps));
    lang.nextStep();
    texts.add(lang.newText(
        new Coordinates(20, 240),
        "Das Problem der Exponentiellen Glättung 1. Ordnung besteht darin, dass die Verbrauchswerte gleichmäßig gewichtet werden,",
        "", null, normalTextProps));
    texts.add(lang.newText(new Coordinates(20, 260),
        "obwohl neuere Werte eher einen Einfluss auf die Zukünftigen ausüben.",
        "", null, normalTextProps));
    lang.nextStep();
    MultipleChoiceQuestionModel mc1 = new MultipleChoiceQuestionModel("q1");
    mc1.setPrompt("Für welche Art von Zeitreihen ist die Glättung 1. Ordnung sinnvoll und findet meistens Anwendung?");
    mc1.addAnswer("beliebige Zeitreihen", 0,
        "Falsch! Die Glättung 1. Ordnung eignet sich für stationäre Zeitreihen.");
    mc1.addAnswer("stationäre Zeitreihen", 10, "Korrekt!");
    mc1.addAnswer("Zeitreihen mit saisonalen Schwankungen", 0,
        "Falsch! Die Glättung 1. Ordnung eignet sich für stationäre Zeitreihen.");
    mc1.setGroupID("qgroup");
    lang.addMCQuestion(mc1);
    lang.nextStep();
    texts.add(lang
        .newText(
            new Coordinates(20, 300),
            "Deshalb entwickelte Brown diese Art der Glättung 2. Ordnung um dieses Problem zu verringern,",
            "", null, normalTextProps));
    texts.add(lang.newText(new Coordinates(20, 320),
        "und um Zeitreihen mit Trends verwenden zu können.", "", null,
        normalTextProps));

    lang.nextStep("Description Part 2");
    // hide description part 1
    for (Text t : texts) {
      t.hide();
    }
    texts.clear();
//    desc1a.hide();
//    desc1b.hide();
//    desc1c.hide();
//    desc1d.hide();
//    desc1e.hide();
//    desc1f.hide();
//    desc1g.hide();
//    desc1h.hide();
//    desc1i.hide();
//    desc1j.hide();
//    desc1k.hide();
//    desc1l.hide();

    texts.add(lang.newText(new Coordinates(20, 60),
        "Verfahrensbeschreibung:", "", null, boldTextProps));
    lang.nextStep();
    texts.add(lang
        .newText(
            new Coordinates(20, 80),
            "Es ist ein Glättungsparamater alpha mit 0 <= alpha <= 1 anzugeben, welcher die Gewichtung bestimmt.",
            "", null, normalTextProps));
    texts.add(lang
        .newText(
            new Coordinates(20, 100),
            "Ist alpha = 1, so erfolgt keine Glättung; bei alpha = 0 ist die Glättung parallel zur x-Achse.",
            "", null, normalTextProps));
    texts.add(lang
        .newText(
            new Coordinates(20, 120),
            "In der Praxis hat es sich aber als sinnvoll erwiesen alpha <= 0,5 zu wählen.",
            "", null, normalTextProps));
    lang.nextStep();
    texts.add(lang.newText(new Coordinates(20, 160),
        "Zudem ist das Verfahren zu initiieren.", "", null, normalTextProps));
    texts.add(lang
        .newText(
            new Coordinates(20, 180),
            "Dies ist notwendig, da zu Beginn Mittelwerte 1. und 2. Ordnung benötitgt werden.",
            "", null, normalTextProps));
    texts.add(lang.newText(new Coordinates(20, 200),
        "Vereinfacht soll B_1 = D_1(1) = D_1(2) angenommen werden.", "", null,
        normalTextProps));
    lang.nextStep();
    texts.add(lang
        .newText(
            new Coordinates(20, 220),
            "Andernfalls können die Initialwerte auch mittels einer linearen Regression bestimmt werden, was jedoch sehr aufwendig sein kann,",
            "", null, italicTextProps));
    texts.add(lang.newText(new Coordinates(20, 240),
        "da nicht alle Regressoren bekannt sein müssen.", "", null,
        italicTextProps));
    lang.nextStep("Calculation Formula");
    for (Text t : texts) {
      t.hide();
    }
    texts.clear();
    // hide description part 2
//    desc2a.hide();
//    desc2b.hide();
//    desc2c.hide();
//    desc2d.hide();
//    desc2e.hide();
//    desc2f.hide();
//    desc2g.hide();
//    desc2h.hide();
//    desc2i.hide();

    // formula
    formula = lang.newSourceCode(new Coordinates(20, 60),
        "formula", null, formulaProps);
    formula.addCodeLine(
        "Folgende mathematische Formeln bzw. Variablen finden Verwendung:",
        null, 0, null);
    formula.addCodeLine(" ", null, 0, null);
    lang.nextStep();
    formula.addCodeLine("Sei B_t der Verbrauch/Bedarf zum Zeitpunkt t", null,
        0, null);
    lang.nextStep();
    formula
        .addCodeLine(
            "Bestimmung des Durchschnitts 1. Ordnung: D_t(1) = alpha * B_t + (1-alpha) * D_t-1(1)",
            null, 1, null);
    lang.nextStep();
    formula
        .addCodeLine(
            "Bestimmung des Durchschnitts 2. Ordnung: D_t(2) = alpha * D_t(1) + (1-alpha) * D_t-1(2)",
            null, 2, null);
    lang.nextStep();
    formula.addCodeLine(
        "Schätzwert für den Achsenabschnitt: a_t = 2 * D_t(1)  - D_t(2)", null,
        3, null);
    lang.nextStep();
    formula
        .addCodeLine(
            "Schätzwert für den Steigungsparameter: b_t = ( alpha / (1-alpha) ) * ( D_t(1) - D_t(2) )",
            null, 4, null);
    lang.nextStep();
    formula
        .addCodeLine(
            "B_t,t+1 ist der Prognosewert für die nächste Periode, für den gilt B_t,t+1 = a_t + b_t",
            null, 5, null);
    lang.nextStep();
    formula.addCodeLine(" ", null, 0, null);
    formula.addCodeLine("Gewähltes alpha: " + alpha, null, 0, null);
    lang.nextStep("Calculation");
  }

  public void drawTable() {

    lang.addLine("grid \"table\" (20, 360) lines " + (Bt.length + 1)
        + " columns 7 style table cellWidth 100 cellHeight 20");
    lang.addLine("setGridValue \"table[0][0]\" \"t\" ");
    lang.addLine("setGridValue \"table[0][1]\" \"B_t\" ");
    lang.addLine("setGridValue \"table[0][2]\" \"D_t(1)\" ");
    lang.addLine("setGridValue \"table[0][3]\" \"D_t(2)\" ");
    lang.addLine("setGridValue \"table[0][4]\" \"a_t\" ");
    lang.addLine("setGridValue \"table[0][5]\" \"b_t\" ");
    lang.addLine("setGridValue \"table[0][6]\" \"B_t,t+1\" ");

    // put B_t's
    for (int i = 0; i < Bt.length; i++) {
      lang.addLine("setGridValue \"table[" + (i + 1) + "][0]\" \"" + (i + 1)
          + "\" ");
      lang.addLine("setGridValue \"table[" + (i + 1) + "][1]\" \"" + Bt[i]
          + "\" ");
    }

  }

  private void drawCalc() {
    calc = lang.newSourceCode(new Coordinates(20, 280),
        "", null, calcProps);
    calc.addCodeLine("Berechnung:", null, 0, null);
  }

  private void drawCalcRect() {
    calcRect = lang.newRect(new Offset(-5, -5, calc.getName(),
        AnimalScript.DIRECTION_NW), new Offset(5, 5, calc.getName(),
        AnimalScript.DIRECTION_SE), "hRectCalc", null, calcRectProps);
  }

  private void hideCalcAndRect() {
    calc.hide();
    calcRect.hide();
  }

  public void calc() {

    for (int i = 0; i < Bt.length; i++) {
      // D1
      if (i != 0) {
        D1[i] = alpha * Bt[i] + (1 - alpha) * D1[i - 1];
        D1[i] = Math.round(D1[i] * 100.) / 100.;
        drawCalc();
        calc.addCodeLine(alpha + " * " + Bt[i] + " + " + "(1-" + alpha + ") * "
            + D1[i - 1] + " = " + D1[i], null, 0, null);
        drawCalcRect();
        lang.addLine("highlightGridCell \"table[" + (i + 1) + "][2]\" ");
        lang.addLine("setGridValue \"table[" + (i + 1) + "][2]\" \"" + D1[i]
            + "\"  after 100 ticks");
        formula.highlight(3);
        lang.nextStep();
        hideCalcAndRect();
        lang.addLine("unhighlightGridCell \"table[" + (i + 1) + "][2]\" ");
        formula.unhighlight(3);
      } else {
        D1[i] = Bt[0];
        D1[i] = Math.round(D1[i] * 100.) / 100.;
        lang.addLine("setGridValue \"table[" + (i + 1) + "][2]\" \"" + D1[i]
            + "\" ");
      }

      // D2
      if (i != 0) {
        D2[i] = alpha * D1[i] + (1 - alpha) * D2[i - 1];
        D2[i] = Math.round(D2[i] * 100.) / 100.;
        drawCalc();
        calc.addCodeLine(alpha + " * " + D1[i] + " + " + "(1-" + alpha + ") * "
            + D2[i - 1] + " = " + D2[i], null, 0, null);
        drawCalcRect();
        lang.addLine("highlightGridCell \"table[" + (i + 1) + "][3]\" ");
        lang.addLine("setGridValue \"table[" + (i + 1) + "][3]\" \"" + D2[i]
            + "\"  after 100 ticks");
        formula.highlight(4);
        lang.nextStep();
        hideCalcAndRect();
        lang.addLine("unhighlightGridCell \"table[" + (i + 1) + "][3]\" ");
        formula.unhighlight(4);
      } else {
        D2[i] = Bt[0];
        D2[i] = Math.round(D2[i] * 100.) / 100.;
        lang.addLine("setGridValue \"table[" + (i + 1) + "][3]\" \"" + D2[i]
            + "\" ");
        lang.nextStep();
      }

      // a
      a = 2 * D1[i] - D2[i];
      a = Math.round(a * 100.) / 100.;

      drawCalc();
      calc.addCodeLine("2 * " + D1[i] + " - " + D2[i] + " = " + a, null, 0,
          null);
      drawCalcRect();
      lang.addLine("highlightGridCell \"table[" + (i + 1) + "][4]\" ");
      lang.addLine("setGridValue \"table[" + (i + 1) + "][4]\" \"" + a
          + "\"  after 100 ticks");
      formula.highlight(5);
      lang.nextStep();
      hideCalcAndRect();
      lang.addLine("unhighlightGridCell \"table[" + (i + 1) + "][4]\" ");
      formula.unhighlight(5);

      // b
      b = (alpha / (1 - alpha)) * (D1[i] - D2[i]);
      b = Math.round(b * 100.) / 100.;
      drawCalc();
      calc.addCodeLine("(" + alpha + " / " + "(1-" + alpha + ")) * ( " + D1[i]
          + " - " + D2[i] + ")" + " = " + b, null, 0, null);
      drawCalcRect();
      lang.addLine("highlightGridCell \"table[" + (i + 1) + "][5]\" ");
      lang.addLine("setGridValue \"table[" + (i + 1) + "][5]\" \"" + b
          + "\"  after 100 ticks");
      formula.highlight(6);
      lang.nextStep();
      hideCalcAndRect();
      lang.addLine("unhighlightGridCell \"table[" + (i + 1) + "][5]\" ");
      formula.unhighlight(6);

      // Bt1
      Bt1[i] = a + b;
      Bt1[i] = Math.round(Bt1[i] * 100.) / 100.;
      drawCalc();
      calc.addCodeLine(a + " + " + b + " = " + Bt1[i], null, 0, null);
      drawCalcRect();
      lang.addLine("highlightGridCell \"table[" + (i + 1) + "][6]\" ");
      lang.addLine("setGridValue \"table[" + (i + 1) + "][6]\" \"" + Bt1[i]
          + "\"  after 100 ticks");
      formula.highlight(7);
      lang.nextStep();
      hideCalcAndRect();
      lang.addLine("unhighlightGridCell \"table[" + (i + 1) + "][6]\" ");
      formula.unhighlight(7);
    }

    formula.hide();
    lang.addLine("hideCode \"table\" ");

    lang.nextStep("End-Description");

  }

  public void drawEndDescription() {
    MultipleChoiceQuestionModel mc2 = new MultipleChoiceQuestionModel("q2");
    mc2.setPrompt("Welche Aussage gilt für das alpha?");
    mc2.addAnswer("beliebig", 0, "Falsch! Es muss zwischen 0 und 1 liegen.");
    mc2.addAnswer("alpha > 0", 0, "Falsch! Es muss zwischen 0 und 1 liegen.");
    mc2.addAnswer("0 <= alpha <= 1", 10, "Korrekt!");
    mc2.setGroupID("qgroup");
    lang.addMCQuestion(mc2);
    lang.newText(new Coordinates(20, 60), "Ergebnis:", "",
        null, boldTextProps);
    lang.newText(
        new Coordinates(20, 80),
        "Mittels der Exponentiellen Glättung 2. Ordnung nach Brown haben wir nun einen Wert von "
            + Bt1[Bt1.length - 1]
            + " für die "
            + (Bt.length + 1)
            + ". Periode prognostiziert.", "", null, normalTextProps);
    lang.nextStep();

    Node[] y = new Node[2];
    y[0] = new Coordinates(50, 330);
    y[1] = new Coordinates(50, 120);

    PolylineProperties arrows = new PolylineProperties();
    arrows.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

    lang.newPolyline(y, "y", null, arrows);

    lang.newText(new Coordinates(20, 100), "B_t", "", null);

    Node[] x = new Node[2];
    x[0] = new Coordinates(50, 330);
    x[1] = new Coordinates(480, 330);

    lang.newPolyline(x, "x", null, arrows);

    lang.newText(new Coordinates(490, 330), "t", "", null);

    lang.nextStep();

    double max = Double.MIN_VALUE;
    for (double i : Bt)
      max = Math.max(max, i);
    for (double i : Bt1)
      max = Math.max(max, i);

    int x_steps = (int) Math.round(400.0 / (Bt.length + 1));
    int y_steps = (int) Math.round(200.0 / max);

    // Drawing x_steps / t
    for (int i = 0; i <= Bt.length; i++) {
      lang.newText(
          new Coordinates((i + 1) * x_steps + 50, 335),
          Integer.toString(i + 1), "", null);
    }

    // Nodes Bt
    Node[] Bt_poly = new Node[Bt.length];

    for (int i = 0; i < Bt.length; i++) {
      Bt_poly[i] = new Coordinates((i + 1) * x_steps + 50,
          330 - (y_steps * Bt[i]));
    }

    lang.newPolyline(Bt_poly, "Bt", null, Bt_polyProps);

    lang.nextStep();

    // Nodes Bt1
    Node[] Bt1_poly = new Node[Bt1.length];

    for (int i = 0; i < Bt1.length; i++) {
      Bt1_poly[i] = new Coordinates((i + 2) * x_steps + 50,
          330 - (y_steps * (int) Bt1[i]));
    }

    lang.newPolyline(Bt1_poly, "Bt1", null, Bt1_polyProps);

    lang.nextStep();
    lang.newText(new Coordinates(20, 370), "Zusatz:", "",
        null, boldTextProps);
    lang.nextStep();
    lang.newText(
        new Coordinates(20, 390),
        "Es ist noch zu erwähnen, dass dieses Verfahren aber nur wenig flexibel auf Bedarfsverlaufsänderungen reagiert.",
        "", null, normalTextProps);
    lang.newText(
        new Coordinates(20, 410),
        "Die Einführung eines weiteren Glättungsparamters beta könnte diesem Problem entgegenwirken.",
        "", null, normalTextProps);
    lang.newText(
        new Coordinates(20, 430),
        "Hierbei handelt es sich um die Exponentielle Glättung 2. Ordnung nach Holt, die aber logischerweise etwas rechenaufwendiger ist.",
        "", null, normalTextProps);
  }

  public String getName() {
    return "Exponentielle Glättung 2. Ordnung nach Brown [DE]";
  }

  public String getAlgorithmName() {
    return "Exponentielle Glättung 2. Ordnung nach Brown";
  }

  public String getAnimationAuthor() {
    return "Rolf Egert, Andreas Pesak";
  }

  public String getDescription() {
    return "Dieses Verfahren findet bei der Bestimmung des zuk&uuml;nftigen Materialbedarfs Anwendung. "
        + "\n"
        + "Die Exponentielle Gl&auml;ttung 2. Ordnung nach Brown ist ein spezielles Verfahren der stochastischen Form, "
        + "\n"
        + "welches Verwendung findet, wenn in der Zeitreihe der Verbr&auml;uche ein Trend (linearer Bedarfsverlauf) stark zu vermuten ist. "
        + "\n"
        + ""
        + "\n"
        + "Es ist ein Gl&auml;ttungsparamater alpha mit 0 &lt;= alpha &lt;= 1 anzugeben, welcher die Gewichtung der historischen Werte bestimmt. "
        + "\n"
        + "In der Praxis hat es sich aber als sinnvoll erwiesen alpha &lt;= 0.5 zu w&auml;hlen."
        + "\n";
  }

  public String getCodeExample() {
    return "Folgende mathematische Formeln bzw. Variablen finden Verwendung: "
        + "\n"
        + "Sei B_t der Verbrauch/Bedarf zum Zeitpunkt t "
        + "\n"
        + "     Bestimmung des Durchschnitts 1. Ordnung: D_t(1) = alpha * B_t + (1-alpha) * D_t-1(1) "
        + "\n"
        + "          Bestimmung des Durchschnitts 2. Ordnung: D_t(2) = alpha * D_t(1) + (1-alpha) * D_t-1(2) "
        + "\n"
        + "               Sch&auml;tzwert f&uuml;r den Achsenabschnitt: a_t = 2 * D_t(1)  - D_t(2) "
        + "\n"
        + "                    Sch&auml;tzwert f&uuml;r den Steigungsparameter: b_t = ( alpha / (1-alpha) ) * ( D_t(1) - D_t(2) ) "
        + "\n"
        + "                         B_t,t+1 ist der Prognosewert f&uuml;r die n&auml;chste Periode, f&uuml;r den gilt B_t,t+1 = a_t + b_t";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}
