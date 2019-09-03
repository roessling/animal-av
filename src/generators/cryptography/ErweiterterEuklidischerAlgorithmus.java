package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class ErweiterterEuklidischerAlgorithmus implements Generator {
  private Language             lang;
  private RectProperties       rectColor;
  private int                  b;
  private int                  a;
  private SourceCodeProperties sourceCodeColor;
  private Text                 info;
  private Text                 header;
  private Rect                 hRect;

  // private Polyline line;

  public void init() {
    lang = new AnimalScript("Erweiterter Euklidischer Algorithmus [DE]",
        "Florian Schmidt, Katja Rabea Sonnenschein", 800, 600);
    lang.setStepMode(true);
  }

  public void QuestionIntro() {
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion" + UUID.randomUUID());
    mcq.setPrompt("Was ist der Unterschied vom Euklidischen Algorithmus zum Erweiterten Euklidischen Algorithmus?");
    mcq.addAnswer(
        "Der Euklidische Algorithmus berechnet nur den größten gemeinsamen Teiler",
        5, "Richtige Antwort!");
    mcq.addAnswer(
        "Der Erweiterte Euklidische Algortihmus berechnet nur den größten gemeinsamen Teiler",
        0,
        "Leider Falsch! Er berechnet auch zwei natürliche Zahlen, so dass ggT(a,b) = ax+by gilt.");
    mcq.setGroupID("First question group");
    lang.addMCQuestion(mcq);
  }

  public void Intro(RectProperties rectProps) {
    TextProperties textProps = new TextProperties();
    textProps.set("font", new Font("Serif", 1, 18));
    header = lang.newText(new Coordinates(20, 30),
        "Erweiterter Euklidischer Algorithmus", "header", null, textProps);
    rectProps.set("depth", 2);
    hRect = lang.newRect(
        new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5,
            5, "header", "SE"), "hRect", null, rectProps);
    lang.nextStep("Intro - Erklärung");

    TextProperties textProps1 = new TextProperties();
    textProps1.set("font", new Font("Serif", 1, 18));
    info = lang.newText(new Coordinates(20, 90), "Beschreibung des Alorithmus",
        "general", null, textProps1);

    TextProperties textProps2 = new TextProperties();
    textProps2.set("font", new Font("Serif", 0, 14));
    TextProperties textProps4 = new TextProperties();
    textProps4.set("font", new Font("Serif", 1, 14));
    info = lang
        .newText(
            new Coordinates(20, 119),
            "Der Erweitete Euklidische Algorithmus berechnet den groessten gemeinsamen Teiler d=ggT(a,b) zweier",
            "general1", null, textProps2);
    info = lang
        .newText(
            new Coordinates(20, 136),
            "natuerlicher Zahlen a und b und zwei ganze Zahlen x und y, sodass die folgende Gleichung erfuellt ist:",
            "general2", null, textProps2);
    info = lang.newText(new Coordinates(20, 152),
        "               d = ax+by bzw. ggT(a,b) = ax+by.", "general3", null,
        textProps2);
    info = lang
        .newText(
            new Coordinates(20, 178),
            "Er ist die Erweiterung des Euklidischen Algorithmus, der nur den groessten gemeinsamen",
            "general4", null, textProps2);
    info = lang.newText(new Coordinates(20, 194), "Teiler ggT(a,b) berechnet.",
        "general5", null, textProps2);
    info = lang.newText(new Coordinates(20, 220), "Input", "Input", null,
        textProps4);
    info = lang.newText(new Coordinates(20, 236),
        "Zwei natuerliche Zahlen a und b", "Input1", null, textProps2);
    info = lang.newText(new Coordinates(20, 262), "Output", "Output", null,
        textProps4);
    info = lang.newText(new Coordinates(20, 278),
        "Der groesste gemeinsame Teiler d=ggT(a,b) und", "Output1", null,
        textProps2);
    info = lang.newText(new Coordinates(20, 294),
        "zwei ganze Zahlen x und y, sodass ax+by=d (=ggT(a,b))", "Output2",
        null, textProps2);
    info = lang.newText(new Coordinates(20, 320), "Algorithmus", "Algo", null,
        textProps4);

    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    SourceCode src = lang.newSourceCode(new Coordinates(20, 326), "sourceCode",
        null, scProps);
    src.addCodeLine("public int[] erweiterteEuklid (int a, int b)", null, 0,
        null);
    src.addCodeLine("If b=0 then", null, 0, null);
    src.addCodeLine("d=a, x=1, y=0; return;", null, 1, null);
    src.addCodeLine("x2=1; x1=0; y2=0; y1=1;", null, 0, null);
    src.addCodeLine("while b>0", null, 0, null);
    src.addCodeLine("q=a/b;", null, 1, null);
    src.addCodeLine("r=a mod b;", null, 1, null);
    src.addCodeLine("x=x2-qx1;", null, 1, null);
    src.addCodeLine("y=y2-qy1;", null, 1, null);
    src.addCodeLine("a=b;", null, 1, null);
    src.addCodeLine("b=r;", null, 1, null);
    src.addCodeLine("x2=x1;", null, 1, null);
    src.addCodeLine("x1=x;", null, 1, null);
    src.addCodeLine("y2=y1;", null, 1, null);
    src.addCodeLine("y1=y;", null, 1, null);
    src.addCodeLine("end while", null, 0, null);
    src.addCodeLine("d=a; x=x2; y=y2;", null, 0, null);
    lang.nextStep();
    QuestionIntro();
    lang.nextStep("Initialisierung des Algorithmus");

  }

  public void table(Integer c, Integer d, SourceCodeProperties srcProps) {
    lang.addLine("hideAll");
    header.show();
    hRect.show();

    TextProperties textProps4 = new TextProperties();
    textProps4.set("font", new Font("Serif", 1, 16));
    TextProperties textProps5 = new TextProperties();
    textProps5.set("font", new Font("Serif", 0, 14));

    // Initialisierung
    String a1 = c.toString();
    String b1 = d.toString();
    info = lang.newText(new Coordinates(20, 80),
        "Loesen der Gleichung:    a * x + b * y = ggT (a , b)   -->  " + a1
            + " * x + " + b1 + " * y = ggT(a , b)", "Start", null, textProps5);

    // Tabelle zur Berechnung
    info = lang.newText(new Coordinates(50, 160), "q", "Tabelle", null,
        textProps5);
    info = lang.newText(new Coordinates(120, 160), "r", "Tabelle1", null,
        textProps5);
    info = lang.newText(new Coordinates(190, 160), "x", "Tabelle2", null,
        textProps5);
    info = lang.newText(new Coordinates(260, 160), "y", "Tabelle3", null,
        textProps5);
    info = lang.newText(new Coordinates(330, 160), "a", "Tabelle4", null,
        textProps5);
    info = lang.newText(new Coordinates(400, 160), "b", "Tabelle5", null,
        textProps5);
    info = lang.newText(new Coordinates(470, 160), "x2", "Tabelle6", null,
        textProps5);
    info = lang.newText(new Coordinates(540, 160), "x1", "Tabelle7", null,
        textProps5);
    info = lang.newText(new Coordinates(610, 160), "y2", "Tabelle8", null,
        textProps5);
    info = lang.newText(new Coordinates(680, 160), "y1", "Tabelle9", null,
        textProps5);

    // Kanten für die Tabelle
    // Horizontale Kante
    Node[] lineNodes = new Node[2];
    lineNodes[0] = new Coordinates(15, 180);
    lineNodes[1] = new Coordinates(715, 180);
    lang.newPolyline(lineNodes, "Line1", null);

    // Vertikale Kanten
    Table(240);

    SourceCode src = lang.newSourceCode(new Coordinates(800, 140),
        "sourceCode", null, srcProps);
    src.addCodeLine("public int[] erweiterteEuklid (int a, int b)", null, 0,
        null);
    src.addCodeLine("If b=0 then", null, 0, null);
    src.addCodeLine("d=a, x=1, y=0; return;", null, 1, null);
    src.addCodeLine("x2=1; x1=0; y2=0; y1=1;", null, 0, null);
    src.addCodeLine("while b>0", null, 0, null);
    src.addCodeLine("q=a/b;", null, 1, null);
    src.addCodeLine("r=a mod b;", null, 1, null);
    src.addCodeLine("x=x2-qx1;", null, 1, null);
    src.addCodeLine("y=y2-qy1;", null, 1, null);
    src.addCodeLine("a=b;", null, 1, null);
    src.addCodeLine("b=r;", null, 1, null);
    src.addCodeLine("x2=x1;", null, 1, null);
    src.addCodeLine("x1=x;", null, 1, null);
    src.addCodeLine("y2=y1;", null, 1, null);
    src.addCodeLine("y1=y;", null, 1, null);
    src.addCodeLine("end while", null, 0, null);
    src.addCodeLine("d=a; x=x2; y=y2;", null, 0, null);
    lang.nextStep();

    // Beginn im Code
    Integer a = c;
    Integer b = d;
    src.highlight(0, 0, false);
    info = lang.newText(new Coordinates(310, 185), a.toString(), "Tabelle-17",
        null, textProps5);
    info = lang.newText(new Coordinates(380, 185), b.toString(), "Tabelle-17",
        null, textProps5);
    lang.nextStep();
    src.highlight(0, 0, true);

    if (b == 0) {
      showResultsBisNull(a, src);
    } else {
      src.highlight(1, 0, false);
      lang.nextStep();
      src.highlight(3, 0, false);
      src.highlight(1, 0, true);
      info = lang.newText(new Coordinates(450, 185), "1", "Tabelle-17", null,
          textProps5);
      info = lang.newText(new Coordinates(520, 185), "0", "Tabelle-17", null,
          textProps5);
      info = lang.newText(new Coordinates(590, 185), "0", "Tabelle-17", null,
          textProps5);
      info = lang.newText(new Coordinates(660, 185), "1", "Tabelle-17", null,
          textProps5);
      lang.nextStep();
      src.highlight(3, 0, true);
      Integer q;
      Integer r;
      Integer x;
      Integer y;
      Integer x2 = 1;
      Integer x1 = 0;
      Integer y2 = 0;
      Integer y1 = 1;
      String Rechnung;
      int Coordy = 205;
      int laenge = 210;

      while (b > 0) {

        Table(laenge);
        src.highlight(5, 0, false);
        q = a / b;
        info = lang.newText(new Coordinates(30, Coordy), q.toString(),
            "Tabelle-q", null, textProps5);
        Rechnung = a.toString() + "/" + b.toString() + "=" + q.toString();
        Rechnung(Rechnung);
        src.highlight(5, 0, true);
        src.highlight(6, 0, false);
        r = a % b;
        info = lang.newText(new Coordinates(100, Coordy), r.toString(),
            "Tabelle-r", null, textProps5);
        Rechnung = a.toString() + "mod" + b.toString() + "=" + r.toString();
        Rechnung(Rechnung);
        src.highlight(6, 0, true);
        src.highlight(7, 0, false);
        x = x2 - (q * x1);
        info = lang.newText(new Coordinates(170, Coordy), x.toString(),
            "Tabelle-x", null, textProps5);
        Rechnung = x2.toString() + "-" + q.toString() + "*" + x1.toString()
            + "=" + x.toString();
        Rechnung(Rechnung);
        src.highlight(7, 0, true);
        src.highlight(8, 0, false);
        y = y2 - (q * y1);
        info = lang.newText(new Coordinates(240, Coordy), y.toString(),
            "Tabelle-y", null, textProps5);
        Rechnung = y2.toString() + "-" + q.toString() + "*" + y1.toString()
            + "=" + y.toString();
        Rechnung(Rechnung);

        src.highlight(8, 0, true);
        src.highlight(9, 0, false);
        a = b;
        info = lang.newText(new Coordinates(310, Coordy), a.toString(),
            "Tabelle-a", null, textProps5);
        lang.nextStep();
        src.highlight(9, 0, true);
        src.highlight(10, 0, false);
        b = r;
        info = lang.newText(new Coordinates(380, Coordy), b.toString(),
            "Tabelle-b", null, textProps5);
        lang.nextStep();
        src.highlight(10, 0, true);
        src.highlight(11, 0, false);
        x2 = x1;
        info = lang.newText(new Coordinates(450, Coordy), x2.toString(),
            "Tabelle-x2", null, textProps5);
        lang.nextStep();
        src.highlight(11, 0, true);
        src.highlight(12, 0, false);
        x1 = x;
        info = lang.newText(new Coordinates(520, Coordy), x1.toString(),
            "Tabelle-x1", null, textProps5);
        lang.nextStep();
        src.highlight(12, 0, true);
        src.highlight(13, 0, false);
        y2 = y1;
        info = lang.newText(new Coordinates(590, Coordy), y2.toString(),
            "Tabelle-y2", null, textProps5);
        lang.nextStep();
        src.highlight(13, 0, true);
        src.highlight(14, 0, false);
        y1 = y;
        info = lang.newText(new Coordinates(660, Coordy), y1.toString(),
            "Tabelle-y1", null, textProps5);
        lang.nextStep();
        src.highlight(14, 0, true);

        Coordy += 20;
        laenge += 30;

      }

      // Ergebnis anzeigen
      src.highlight(16, 0, false);
      info = lang.newText(new Coordinates(80, 450), "Ergebnis:", "Ergebnis",
          null, textProps4);
      String d_Erg = "d=a=" + a.toString();
      info = lang.newText(new Coordinates(80, 470), d_Erg, "Ergebnis1", null,
          textProps5);
      String x_Erg = "x=x2=" + x2.toString();
      info = lang.newText(new Coordinates(80, 490), x_Erg, "Ergebnis2", null,
          textProps5);
      String y_Erg = "y=y2=" + y2.toString();
      info = lang.newText(new Coordinates(80, 510), y_Erg, "Ergebnis3", null,
          textProps5);
      lang.nextStep("Outro - Abschlussfolie");
    }

  }

  public void showResultsBisNull(Integer a, SourceCode src) {
    // Ergebnis anzeigen
    TextProperties textProps5 = new TextProperties();
    textProps5.set("font", new Font("Serif", 0, 14));
    TextProperties textProps4 = new TextProperties();
    textProps4.set("font", new Font("Serif", 1, 14));
    src.highlight(1, 0, false);
    src.highlight(2, 0, false);
    info = lang.newText(new Coordinates(80, 450), "Ergebnis:", "Ergebnis",
        null, textProps4);
    String d_Erg = "d=a=" + a.toString();
    info = lang.newText(new Coordinates(80, 470), d_Erg, "Ergebnis1", null,
        textProps5);
    String x_Erg = "x=1";
    info = lang.newText(new Coordinates(80, 490), x_Erg, "Ergebnis2", null,
        textProps5);
    String y_Erg = "y=0";
    info = lang.newText(new Coordinates(80, 510), y_Erg, "Ergebnis3", null,
        textProps5);
    lang.nextStep();
  }

  public void Rechnung(String re) {
    TextProperties textProps6 = new TextProperties();
    textProps6.set("font", new Font("Serif", 0, 14));
    TextProperties textProps4 = new TextProperties();
    textProps4.set("font", new Font("Serif", 1, 14));
    info = lang.newText(new Coordinates(450, 450), "Nebenrechnung:",
        "Rechnung", null, textProps4);
    info = lang.newText(new Coordinates(450, 470), re, "Berechnung", null,
        textProps6);
    lang.nextStep();
    info.hide();
  }

  public void Table(int i) {
    Node[] lineNodes1 = new Node[2];
    lineNodes1[0] = new Coordinates(85, 150);
    lineNodes1[1] = new Coordinates(85, i);
    lang.newPolyline(lineNodes1, "Line2", null);

    Node[] lineNodes2 = new Node[2];
    lineNodes2[0] = new Coordinates(155, 150);
    lineNodes2[1] = new Coordinates(155, i);
    lang.newPolyline(lineNodes2, "Line3", null);

    Node[] lineNodes3 = new Node[2];
    lineNodes3[0] = new Coordinates(225, 150);
    lineNodes3[1] = new Coordinates(225, i);
    lang.newPolyline(lineNodes3, "Line4", null);

    Node[] lineNodes4 = new Node[2];
    lineNodes4[0] = new Coordinates(295, 150);
    lineNodes4[1] = new Coordinates(295, i);
    lang.newPolyline(lineNodes4, "Line5", null);

    Node[] lineNodes5 = new Node[2];
    lineNodes5[0] = new Coordinates(365, 150);
    lineNodes5[1] = new Coordinates(365, i);
    lang.newPolyline(lineNodes5, "Line6", null);

    Node[] lineNodes6 = new Node[2];
    lineNodes6[0] = new Coordinates(435, 150);
    lineNodes6[1] = new Coordinates(435, i);
    lang.newPolyline(lineNodes6, "Line7", null);

    Node[] lineNodes7 = new Node[2];
    lineNodes7[0] = new Coordinates(505, 150);
    lineNodes7[1] = new Coordinates(505, i);
    lang.newPolyline(lineNodes7, "Line8", null);

    Node[] lineNodes8 = new Node[2];
    lineNodes8[0] = new Coordinates(575, 150);
    lineNodes8[1] = new Coordinates(575, i);
    lang.newPolyline(lineNodes8, "Line9", null);

    Node[] lineNodes9 = new Node[2];
    lineNodes9[0] = new Coordinates(645, 150);
    lineNodes9[1] = new Coordinates(645, i);
    lang.newPolyline(lineNodes9, "Line10", null);
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
            "Das Haupteinsatzgebiet des erweiterten euklidischen Algorithmus ist die Berechnung der inversen Elemente in",
            "Outro", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 106),
            "ganzzahligen Restklassenringen. Denn wenn der Algorithmus das Tripel (d = ggT(a,b), s,t) ermittelt, ist entweder",
            "Outro1", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 122),
            "d=1 und damit 1 = t*b mod a, t ist also das multiplikative Inverse von b modulo a, oder aber d ungleich 1, was bedeutet,",
            "Outro2", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 138),
            "dass b modulo a kein Inverses hat. Dies ist die Grundlage fuer die Loesung von diophantischen Gleichungen oder allgemein zur Loesung",
            "Outro3", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 154),
            "von gannzzahligen linearen Gleichungssystemen. Ebenso ist die Bestimmung inverser Elemente eine Grundlage fuer den",
            "Outro4", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 170),
            "chinesischen Restsatz, welcher wiederum Grundlage des bedeutenden Tricks der kleinen Primzahlen in der berechnbaren Algebra ist.",
            "Outro5", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 186),
            "Einer der bekanntesten Einsatzgebiete ist bei der Loesung von RSA Verschluesselungen.",
            "Outro6", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 220),
            "Die Komplexitaet des Erweiterten Euklidischen Algorithmus ist bis auf einen Konstanten Faktor gleich zum Euklidischen",
            "Outro6", null, textProps6);
    info = lang
        .newText(
            new Coordinates(20, 236),
            "Algorithmus. Das heißt die Anzahl der rekursiven Aufrufe ist gleich. Die Anzahl der rekursiven Aufrufe",
            "Outro6", null, textProps6);
    info = lang.newText(new Coordinates(20, 252), "für a > b > 0 ist O(lg b).",
        "Outro6", null, textProps6);
  }

  public void Start(int a, int b, RectProperties rectProps,
      SourceCodeProperties srcProps) {
    Intro(rectProps);
    table(a, b, srcProps);
    Outro();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    rectColor = (RectProperties) props.getPropertiesByName("rectColor");
    b = (Integer) primitives.get("b");
    a = (Integer) primitives.get("a");
    sourceCodeColor = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeColor");

    Start(a, b, rectColor, sourceCodeColor);

    lang.finalizeGeneration();

    return lang.toString();
  }

  public String getName() {
    return "Erweiterter Euklidischer Algorithmus [DE]";
  }

  public String getAlgorithmName() {
    return "Erweiterter Euklidischer Algorithmus";
  }

  public String getAnimationAuthor() {
    return "Florian Schmidt, Katja Rabea Sonnenschein";
  }

  public String getDescription() {
    return "Der Erweitete Euklidische Algorithmus berechnet den groessten gemeinsamen Teiler d=ggT(a,b) zweier"
        + "\n"
        + "natuerlicher Zahlen a und b und zwei ganze Zahlen x und y, sodass die folgende Gleichung erfuellt ist:"
        + "\n" + "               d = ax+by bzw. ggT(a,b) = ax+by.";
  }

  public String getCodeExample() {
    return "public int[] erweiterteEuklid (int a, int b) {" + "\n"
        + "     If b=0 then" + "\n" + "          d=a, x=1, y=0; return;" + "\n"
        + "     x2=1; x1=0; y2=0; y1=1;" + "\n" + "     while b>0" + "\n"
        + "          q=a/b;" + "\n" + "          r=a mod b;" + "\n"
        + "          x=x2-qx1;" + "\n" + "          y=y2-qy1;" + "\n"
        + "          a;" + "\n" + "          b=r;" + "\n" + "          x2=x1;"
        + "\n" + "          x1=x;" + "\n" + "          y2=y1;" + "\n"
        + "          y1=y;" + "\n" + "     end while" + "\n"
        + "     d=a; x=x2; y=y2;" + "\n" + "}";
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
    return Generator.JAVA_OUTPUT;
  }

}