package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class RungeKutta4Ordnung implements ValidatingGenerator {
  private Language             lang;
  private SourceCodeProperties sourceCode;

  private MatrixProperties     ErgebnissmatrixProps;
  private MatrixProperties     ButchermatrixProps;

  private PolylineProperties   polyProps;

  public void init() {

    lang = new AnimalScript("Runge-Kutta 4. Ordnung",
        "Benedikt Wartusch, Daniel Dieth", 800, 600);

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    ButchermatrixProps = new MatrixProperties();

    ButchermatrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        Color.BLACK);
    ButchermatrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    ButchermatrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.RED);

    lang.setStepMode(true);
  }

  public void run(double t_0, double t_dest, double y_0, double h,
      SourceCodeProperties scProps) {

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 24));
    textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    // textProps.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
    Text title = lang.newText(new Coordinates(20, 30),
        "Runge Kutta 4. Ordnung", "title", null, textProps);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, title, "NW"),
        new Offset(30, 5, title, "SE"), "titleRect", null, rectProps);

    SourceCodeProperties codeProps = new SourceCodeProperties();
    codeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
    codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));
    codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode description = lang.newSourceCode(
        new Offset(-140, 50, title, "N"), "description", null, codeProps);
    description.addCodeLine(
        "Beschreibung des Runge Kutta Verfahrens 4.Ordnung", null, 0, null);
    description
        .addCodeLine(
            "Das Runge Kutta Verfahren 4.Ordung, kurz RK4 ist, ist auch als das klassiche Runge Kutta Verfahren bekannt.",
            null, 0, null);
    description
        .addCodeLine(
            "Das RK4 ist ein Verfahren in der numerischen Mathemathik zur naeherungsweisen Loesung von Anfangswertproblemen",
            null, 0, null);
    description
        .addCodeLine(
            "Wie der Name schon sagt ist das RK4 ein Verfahren von Ordung 4. Das heisst, dass der Fehler in der Ordnung von O(h^4) liegt.",
            null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("Sei y_dot = f(t,y),  y(t_0) = y_0", null, 0, null);
    description
        .addCodeLine(
            "y ist die von der Zeit t abhaengige unbekannte Funktion, die wir annaehern wollen.",
            null, 0, null);
    description
        .addCodeLine(
            "Da die exakte Loesung y(t) meistens nicht oder nur sehr schwer zu berechnen ist,",
            null, 0, null);
    description.addCodeLine("genuegt eine Annaeherung y_n zu Zeitpunkt t_n.",
        null, 0, null);

    description
        .addCodeLine(
            "Sei in der folgenden Visualisierung die Anfangszeit t_0 gegeben, bei der der y-Wert y_0 gilt, welcher auch gegeben ist.",
            null, 0, null);
    description.addCodeLine("Weiterhin sei auch die Funktion f gegeben.", null,
        0, null);

    lang.nextStep("Anfang");
    description.hide();
    SourceCode allgemeineFormel = lang.newSourceCode(new Offset(-140, 50,
        title, "N"), "bedingungen", null, codeProps);

    allgemeineFormel.addCodeLine(
        "Die allegmeinen Runge Kutta Formeln koennen zeilenweise aus", null, 0,
        null);
    allgemeineFormel.addCodeLine(
        "dem Butcherschema fuer das spezielle Verfahren abgelesen werden",
        null, 0, null);
    allgemeineFormel.addCodeLine(
        "k_1= f(t_n + g_1*h, y_n + h*(a_11*k_1 + a_12*k_2 + ... + a_1r*k_r)",
        null, 0, null);
    allgemeineFormel.addCodeLine(
        "k_2= f(t_n + g_2*h, y_n + h*(a_21*k_1 + a_22*k_2 + ... + a_2r*k_r)",
        null, 0, null);
    allgemeineFormel.addCodeLine(
        "k_3= f(t_n + g_3*h, y_n + h*(a_31*k_1 + a_32*k_2 + ... + a_3r*k_r)",
        null, 0, null);
    allgemeineFormel.addCodeLine(":", null, 0, null);
    allgemeineFormel.addCodeLine(
        "k_r= f(t_n + g_r*h, y_n + h*(a_r1*k_1 + a_r2*k_2 + ... + a_rr*k_r)",
        null, 0, null);
    allgemeineFormel.addCodeLine(
        "y_n+1 = y_n + h(b_1*k_1 + b_2*k_2 + ... + b_r*k_r)", null, 0, null);

    SourceCode butcherTextallgemein = lang.newSourceCode(new Offset(430, 36,
        title, "E"), "butcherTextallgemein", null, codeProps);
    butcherTextallgemein.addCodeLine("Das allgemeine Butcher-Schema", null, 0,
        null);
    String[][] butcherAllgemein = { { "g_1", "a_11", "a_12", "...", "a_1r" },
        { "g_2", "a_21", "a_22", "...", "a_2r" }, { ":", ":", ":", ":", ":" },
        { "g_r", "a_r1", "a_r2", "...", "a_rr" },
        { "0", "b_1", "b_2", "...", "b_r" } };
    StringMatrix butcherSchemaAllgemein = lang.newStringMatrix(new Offset(430,
        80, title, "E"), butcherAllgemein, "matirx", null, ButchermatrixProps);

    Node[] l1 = { new Offset(35, 5, butcherSchemaAllgemein, "NW"),
        new Offset(35, 142, butcherSchemaAllgemein, "NW") };
    Polyline line1 = lang.newPolyline(l1, "line1", null, polyProps);
    Node[] l2 = { new Offset(0, 119, butcherSchemaAllgemein, "NW"),
        new Offset(205, 119, butcherSchemaAllgemein, "NW") };
    Polyline line2 = lang.newPolyline(l2, "line1", null, polyProps);

    lang.nextStep();

    SourceCode sourceCode = lang.newSourceCode(
        new Offset(-152, 300, title, "N"), "sourceCode", null, scProps);
    sourceCode.addCodeLine("solange t_n <= t_end", null, 0, null);
    sourceCode.addCodeLine("k_1 = f(t_n, y_n)", null, 1, null);
    sourceCode.addCodeLine("k_2 = f(t_n + 0.5*h, y_n + 0.5*h*k_1)", null, 1,
        null);
    sourceCode.addCodeLine("k_3 = f(t_n + 0.5*h, y_n + 0.5*h*k_2)", null, 1,
        null);
    sourceCode.addCodeLine("k_4 = f(t_n +h, y_n +h*k_3)", null, 1, null);
    sourceCode.addCodeLine("", null, 1, null);
    sourceCode.addCodeLine("y_n+1 = y_n + (h/6)(k_1 + 2*k_2 + 2*k_3 + k_4)",
        null, 1, null);
    sourceCode.addCodeLine("", null, 1, null);
    sourceCode.addCodeLine("t_n = t_n + h, n = n + 1", null, 1, null);

    TextProperties textProps2 = new TextProperties();
    textProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 17));
    Text exakteFormel = lang.newText(new Offset(0, -20, sourceCode, "NW"),
        "So ergeben sich aus dem Buchter-Schema fuer RK4 folgende Formeln",
        "exakteFormel", null, textProps2);

    SourceCode butcherText = lang.newSourceCode(
        new Offset(430, 263, title, "E"), "butcherText", null, codeProps);
    butcherText.addCodeLine(
        "Das Butcher-Schema des Runge Kutta Verfahrens 4. Ordnung", null, 0,
        null);
    String[][] butcher = { { "0", "0", "0", "0", "0" },
        { "1/2", "1/2", "0", "0", "0" }, { "1/2", "0", "1/2", "1", "0" },
        { "0", "0", "1", "0", "0" }, { "0", "1/6", "1/3", "1/3", "1/6" } };
    StringMatrix butcherSchema = lang.newStringMatrix(new Offset(430, 315,
        title, "E"), butcher, "matirx", null, ButchermatrixProps);

    Node[] l3 = { new Offset(32, 5, butcherSchema, "NW"),
        new Offset(32, 142, butcherSchema, "NW") };
    Polyline line3 = lang.newPolyline(l3, "line1", null, polyProps);
    Node[] l4 = { new Offset(0, 119, butcherSchema, "NW"),
        new Offset(145, 119, butcherSchema, "NW") };
    Polyline line4 = lang.newPolyline(l4, "line1", null, polyProps);

    this.RK4Aufstellen(allgemeineFormel, butcherSchemaAllgemein, sourceCode,
        butcherSchema);

    allgemeineFormel.hide();
    butcherSchemaAllgemein.hide();
    butcherTextallgemein.hide();
    line1.hide();
    line2.hide();
    exakteFormel.hide();

    Timing defaultTiming = new TicksTiming(0);
    Timing durationTiming = new TicksTiming(20);

    try {
      butcherText.moveTo("N", null, new Offset(430, 45, title, "E"),
          defaultTiming, durationTiming);
      // butcherSchema.moveTo("N", null, new Offset(430, 55, title, "E"),
      // defaultTiming, durationTiming);
      butcherSchema.moveTo("N", null, new Offset(430, -430, title, "E"),
          defaultTiming, durationTiming);
      line3.moveTo("N", null, new Offset(456, 65, title, "E"), defaultTiming,
          durationTiming);
      line4.moveTo("N", null, new Offset(432, 176, title, "E"), defaultTiming,
          durationTiming);
      sourceCode.moveTo("N", null, new Offset(-140, 280, title, "N"),
          defaultTiming, durationTiming);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    SourceCode bedingungen = lang.newSourceCode(
        new Offset(-140, 50, title, "N"), "bedingungen", null, codeProps);
    bedingungen.addCodeLine("Sei die Anfangszeit     t_0 = " + t_0 + ",", null,
        0, null);
    bedingungen.addCodeLine("der Anfangswert         y_0 = " + y_0 + ",", null,
        0, null);
    bedingungen.addCodeLine("der Endzeit             t_end = " + t_dest + ",",
        null, 0, null);
    bedingungen.addCodeLine("die Schrittweite        h = " + h, null, 0, null);
    bedingungen.addCodeLine(
        "und die Funktion        y_dot = sin((4/3)*t + 2) + 0.5*y(t) - 1",
        null, 0, null);

    lang.newText(new Offset(-140, 265, title, "N"), "Sei n Element von N",
        "Formel", null, textProps2);

    lang.nextStep("RK4 Beispiel durchrechnen");

    this.RK4(sourceCode, t_0, t_dest, y_0, h);

    MultipleSelectionQuestionModel frage = new MultipleSelectionQuestionModel(
        "frage");
    frage
        .setPrompt("Wird die Naeherung genauer, wenn man den Algorithmus laenger laufen laesst?");
    frage
        .addAnswer(
            "ja, dadurch wird die Iterationszahl und somit die Genauigkeit erhoeht.",
            0,
            "Falsch, wenn der Algortihmus laenger lauft erhaelt man eine Naeherung zu einem anderen Zeitpunkt.");
    frage
        .addAnswer(
            "Nein, wenn der Algortihmus laenger lauft erhaelt man eine Naeherung zu einem anderen Zeitpunkt.",
            1,
            "Richtig, denn pro Iteration wir die Naeherung für einen Zeitpunkt + Schrittweite berechnet.");
    frage
        .addAnswer(
            "ja, dadurch wird die Naeherung immer besser, denn pro Schritt wird der Fehler um h^4 kleiner.",
            0,
            "Falsch, wenn der Algortihmus laenger lauft erhaelt man eine Naeherung zu einem anderen Zeitpunkt.");
    lang.addMSQuestion(frage);

    // FillInBlanksQuestionModel fill = new
    // FillInBlanksQuestionModel("Frage1");
    // fill.setPrompt("welchen wert hat t_n nach der Iteration?");
    // fill.addAnswer("a", 1, "");
    // lang.addFIBQuestion(fill);

    lang.nextStep("Abschluss");
    SourceCode abschluss = lang.newSourceCode(new Offset(-55, 450, title, "N"),
        "abschluss", null, codeProps);
    abschluss.addCodeLine("Nun wurde ein Naeherung fuer y(" + t_dest
        + "), also von t = " + t_dest + " berechnet.", null, 0, null);
    abschluss
        .addCodeLine(
            "Beim Runge Kutta Verfahren 4. Ordnung ist wie bei fast allen nummerischen Verfahren zu beachten, dass man bei",
            null, 0, null);
    abschluss
        .addCodeLine(
            "der Groesse der Schrittweite aufpasst. Das heißt, es ist wichitg so viele Iterationen durchzufuehren, bis t und",
            null, 0, null);
    abschluss.addCodeLine("nicht n gleich der gewuenschten Endzeit ist.", null,
        0, null);
  }

  public void RK4(SourceCode sc, double t_0, double t_dest, double y_0, double h) {
    String ergString = "";
    int timeSpan = (int) ((t_dest - t_0) / h) + 1;
    String[][] tempY = new String[7][timeSpan];
    StringMatrix y = lang.newStringMatrix(new Offset(375, -146, sc, "E"),
        tempY, "matirx", null, ErgebnissmatrixProps);

    Timing defaultTiming = new TicksTiming(0);
    int n = 1;
    double t_n = t_0;
    double temp = t_n + h;

    for (int i = 0; i < 7; i++) {
      for (int j = 0; j < timeSpan; j++) {
        y.put(i, j, "", defaultTiming, defaultTiming);
      }
    }

    String[][] ntString = { { "" } };
    StringMatrix nt = lang.newStringMatrix(new Offset(100, 59, sc, "E"),
        ntString, "n1", null, ErgebnissmatrixProps);

    String[][] k1String = { { "" } };
    StringMatrix k1 = lang.newStringMatrix(new Offset(100, -81, sc, "E"),
        k1String, "k1", null, ErgebnissmatrixProps);

    String[][] k2String = { { "" } };
    StringMatrix k2 = lang.newStringMatrix(new Offset(100, -54, sc, "E"),
        k2String, "k2", null, ErgebnissmatrixProps);

    String[][] k3String = { { "" } };
    StringMatrix k3 = lang.newStringMatrix(new Offset(100, -27, sc, "E"),
        k3String, "k3", null, ErgebnissmatrixProps);

    String[][] k4String = { { "" } };
    StringMatrix k4 = lang.newStringMatrix(new Offset(100, 0, sc, "E"),
        k4String, "k4", null, ErgebnissmatrixProps);

    TwoValueCounter counter = lang.newCounter(y);
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);

    lang.newCounterView(counter, new Coordinates(37, 230), cp, true, true);

    TextProperties textP = new TextProperties();
    textP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));
    textP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    Text tippZ1 = lang.newText(new Offset(100, -110, sc, "E"), "Bei t= " + t_0
        + " muss das", "tippZ1", null, textP);
    Text tippZ2 = lang.newText(new Offset(100, -88, sc, "E"),
        "Verfahren nicht berechnet werden,", "tippZ2", null, textP);
    Text tippZ3 = lang.newText(new Offset(100, -66, sc, "E"),
        "denn hier ist der Anfangswert", "tippZ2", null, textP);
    Text tippZ4 = lang.newText(new Offset(100, -44, sc, "E"), "mit " + y_0
        + " gegeben", "tippZ2", null, textP);

    y.put(0, 0, "n = 0", defaultTiming, defaultTiming);
    y.put(1, 0, "t = " + t_n, defaultTiming, defaultTiming);
    y.put(6, 0, Double.toString(y_0), defaultTiming, defaultTiming);

    lang.nextStep();
    tippZ1.hide();
    tippZ2.hide();
    tippZ3.hide();
    tippZ4.hide();
    double zwischenErgebnis = 0.0;
    // double tempZeit = t_n;

    while (t_n < t_dest) {
      sc.highlight(0);
      // temp = temp + h;
      y.put(0, n, "n =" + n, defaultTiming, defaultTiming);
      y.put(1, n, "t = " + temp, defaultTiming, defaultTiming);
      nt.put(0, 0, "n =" + n + ", t = " + temp, defaultTiming, defaultTiming);
      lang.nextStep();
      sc.toggleHighlight(0, 1);
      sc.unhighlight(8);
      zwischenErgebnis = Double.parseDouble(y.getElement(6, n - 1));
      ergString = Double.toString(zwischenErgebnis);
      if (ergString.length() > 5) {
        ergString = ergString.substring(0, 5);
      }
      y.put(2, n, function(t_n, zwischenErgebnis), defaultTiming, defaultTiming);
      k1.put(0, 0, "k1= f(" + t_n + ", " + ergString + ")", defaultTiming,
          defaultTiming);
      lang.nextStep();

      sc.toggleHighlight(1, 2);
      zwischenErgebnis = Double.parseDouble(y.getElement(6, n - 1)) + 0.5 * h
          * Double.parseDouble(y.getElement(2, n));
      ergString = Double.toString(zwischenErgebnis);
      if (ergString.length() > 5) {
        ergString = ergString.substring(0, 5);
      }
      y.put(3, n, function(t_n + 0.5 * h, zwischenErgebnis), defaultTiming,
          defaultTiming);
      k2.put(0, 0, "k2= f(" + (t_n + 0.5 * h) + ", " + ergString + ")",
          defaultTiming, defaultTiming);
      lang.nextStep();

      sc.toggleHighlight(2, 3);
      zwischenErgebnis = Double.parseDouble(y.getElement(6, n - 1)) + 0.5 * h
          * Double.parseDouble(y.getElement(3, n));
      ergString = Double.toString(zwischenErgebnis);
      if (ergString.length() > 5) {
        ergString = ergString.substring(0, 5);
      }
      y.put(4, n, function(t_n + 0.5 * h, zwischenErgebnis), defaultTiming,
          defaultTiming);
      k3.put(0, 0, "k3= f(" + (t_n + 0.5 * h) + ", " + ergString + ")",
          defaultTiming, defaultTiming);
      lang.nextStep();

      sc.toggleHighlight(3, 4);
      zwischenErgebnis = Double.parseDouble(y.getElement(6, n - 1)) + h
          * Double.parseDouble(y.getElement(4, n));
      ergString = Double.toString(zwischenErgebnis);
      if (ergString.length() > 5) {
        ergString = ergString.substring(0, 5);
      }
      y.put(5, n, function(t_n + h, zwischenErgebnis), defaultTiming,
          defaultTiming);
      k4.put(0, 0, "k4= f(" + (t_n + h) + ", " + ergString + ")",
          defaultTiming, defaultTiming);
      lang.nextStep();

      sc.toggleHighlight(4, 6);
      ergString = Double.toString(Double.parseDouble(y.getElement(6, n - 1))
          + (h / 6)
          * (Double.parseDouble(y.getElement(2, n)) + 2
              * Double.parseDouble(y.getElement(3, n)) + 2
              * Double.parseDouble(y.getElement(4, n)) + Double.parseDouble(y
              .getElement(5, n))));
      if (ergString.length() > 5) {
        ergString = ergString.substring(0, 5);
      }
      y.put(6, n, ergString, defaultTiming, defaultTiming);
      t_n = t_n + h;
      n = n + 1;
      temp = temp + h;
      lang.nextStep();
      // sc.unhighlight(6);
      sc.toggleHighlight(6, 8);
      nt.put(0, 0, "n =" + n + ", t = " + temp, defaultTiming, defaultTiming);
      lang.nextStep();
      sc.unhighlight(8);
      k1.put(0, 0, "", defaultTiming, defaultTiming);
      k2.put(0, 0, "", defaultTiming, defaultTiming);
      k3.put(0, 0, "", defaultTiming, defaultTiming);
      k4.put(0, 0, "", defaultTiming, defaultTiming);
    }
  }

  public String function(double t, double y) {
    double erg = Math.sin((4d / 3d) * t + 2) + 0.5 * y - 1;
    String ergString = Double.toString(erg);
    if (ergString.length() > 5) {
      ergString = ergString.substring(0, 5);
    }
    return ergString;
  }

  public void RK4Aufstellen(SourceCode allgemeineFormel,
      StringMatrix butcherSchemaAllgemein, SourceCode sc,
      StringMatrix butcherSchema) {
    // Timing defaultTiming = new TicksTiming(0);
    lang.nextStep("Allgemeine RK4- Formel aufstellen");
    allgemeineFormel.highlight(2);
    sc.highlight(1);
    lang.nextStep();
    int n = butcherSchemaAllgemein.getNrRows();
    for (int i = 1; i < n - 1; i++) {
      if (i >= 3) {
        allgemeineFormel.toggleHighlight(i + 1, i + 3);
        allgemeineFormel.unhighlight(i + 2);
        sc.toggleHighlight(i, i + 1);
      } else {
        allgemeineFormel.toggleHighlight(i + 1, i + 2);
        sc.toggleHighlight(i, i + 1);
      }
      lang.nextStep();
    }

    allgemeineFormel.toggleHighlight(n, n + 2);
    allgemeineFormel.unhighlight(n + 1);
    sc.toggleHighlight(n - 1, n + 1);
    lang.nextStep();

    allgemeineFormel.unhighlight(n + 2);
    sc.unhighlight(butcherSchemaAllgemein.getNrRows() + 1);
  }

  // public static void main(String args[]) {
  // RungeKutta4Ordnung rk4 = new RungeKutta4Ordnung();
  //
  // AnimationPropertiesContainer props = new AnimationPropertiesContainer();
  // AnimationProperties sourceCode = new SourceCodeProperties();
  // sourceCode.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
  // Color.BLACK);
  // sourceCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
  // "Monospaced", Font.PLAIN, 17));
  // sourceCode.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
  // Color.RED);
  // sourceCode.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  // sourceCode.setName("sourceCode");
  //
  // props.add(sourceCode);
  //
  // // Hashtable<String, Object> primitives = new Hashtable<String, Object>();
  // // primitives.put("t_end", 3d);
  // // primitives.put("t_0", 0d);
  // // primitives.put("y_0", 2d);
  // // primitives.put("h", 1d);
  // // rk4.generate(props, primitives);
  //
  // System.out.println(rk4.lang.toString());
  // }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    double t_0;
    double t_end;
    double y_0;
    double h;
    t_end = (Double) primitives.get("t_end");
    t_0 = (Double) primitives.get("t_0");
    y_0 = (Double) primitives.get("y_0");
    h = (Double) primitives.get("h");

    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    polyProps = (PolylineProperties) props.getPropertiesByName("polyline");
    ErgebnissmatrixProps = (MatrixProperties) props
        .getPropertiesByName("ErgebnissmatrixProps");
    ButchermatrixProps = (MatrixProperties) props
        .getPropertiesByName("ButchermatrixProps");

    this.run(t_0, t_end, y_0, h, sourceCode);

    lang.finalizeGeneration();

    return lang.toString();
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // String errorMessage = "";
    boolean error = false;
    double validate = 0;

    double h = (Double) primitives.get("h");
    double t_end = (Double) primitives.get("t_end");
    double t_0 = (Double) primitives.get("t_0");

    validate = (t_end - t_0) % h;
    if (validate != 0) {
      showErrorWindow("Mit der eingegeben Kombination von Anfangswert, Endwert und Schrittweite kann der Endewert nicth angenaehert werden.");
      error = true;
    }
    return !error;
  }

  private void showErrorWindow(String message) {
    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message,
        "Fehler", JOptionPane.ERROR_MESSAGE);
  }

  public String getName() {
    return "Runge-Kutta 4. Ordnung";
  }

  public String getAlgorithmName() {
    return "Runge-Kutta 4. Ordnung";
  }

  public String getAnimationAuthor() {
    return "Benedikt Wartusch, Daniel Dieth";
  }

  public String getDescription() {
    return "Das Runge Kutta Verfahren 4.Ordung, kurz RK4 ist, ist auch als das klassiche Runge Kutta Verfahren"
        + "\n"
        + "bekannt. Das RK4 ist ein Verfahren in der numerischen Mathemathik zur naeherungsweisen Loesung "
        + "\n"
        + "von Anfangswertproblemen. Wie der Name schon sagt ist das RK4 ein Verfahren von Ordung 4."
        + "\n"
        + " Das heißt, dass der Fehler in der Ordnung von O(h^4) liegt."
        + "\n"
        + " y ist die von der Zeit t abhaengige unbekannte Funktion, die wir annaehern wollen."
        + "\n"
        + " Da die exakte Loesung y(t) meistens nicht oder nur sehr schwer zu berechnen ist, "
        + "\n"
        + " genuegt eine Annaeherung y_n zu Zeitpunkt t_n."
        + "\n"
        + " Sei in der folgenden Visualisierung die Anfangszeit t_0 gegeben, bei der der y-Wert y_0 gilt,"
        + "\n"
        + " welcher auch gegeben ist. Weiterhin sei auch die Funktion f gegeben.";
  }

  public String getCodeExample() {
    return "k_1 = f(t_n, y_n)" + "\n" + "k_2 = f(t_n + 0.5*h, y_n + 0.5*h*k_1)"
        + "\n" + "k_3 = f(t_n + 0.5*h, y_n + 0.5*h*k_2)" + "\n"
        + "k_4 = f(t_n +h, y_n +h*k_3)" + "\n"
        + "y_n+1 = y_n + (h/6)(k_1 + 2*k_2 + 2*k_3 + k_4)";
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