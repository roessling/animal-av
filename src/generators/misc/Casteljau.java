package generators.misc;

import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalCircleGenerator;
import algoanim.animalscript.AnimalPolylineGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * 
 * @author Philipp Dürr <phil@uselessbrickwall.com>
 * @version 0.1
 * 
 */
public class Casteljau implements Generator {

  /**
   * The concrete language object used for creating output
   */
  Language                     language;

  private Text                 header;
  /**
   * The rectangle around the headline
   */
  private Rect                 hRect;
  /**
   * Globally defined text properties
   */
  private TextProperties       textProps;
  private SourceCodeProperties sourceCodeProps;
  LinkedList<bezier>           stack;

  CircleProperties             redDot;
  CircleProperties             blackDot;
  PolylineProperties           blackLine;
  PolylineProperties           grayLine;
  DisplayOptions               noTime;
  private int                  steps;
  int                          pointSize;
  double                       t;
  double[]                     X = new double[4]; ;
  double[]                     Y = new double[4];

  public SourceCode            src;

  @Override
  public void init() {
    language = new AnimalScript("De Casteljau", "Philipp Dürr", 800, 600);
    language.setStepMode(true);
    stack = new LinkedList<bezier>();
    language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int[][] xy = (int[][]) primitives.get("xy");
    t = (Double) primitives.get("t");
    steps = (Integer) primitives.get("schritte");
    pointSize = (Integer) primitives.get("pointSize");
    X[0] = xy[0][0];
    X[1] = xy[0][1];
    X[2] = xy[0][2];
    X[3] = xy[0][3];
    Y[0] = xy[1][0];
    Y[1] = xy[1][1];
    Y[2] = xy[1][2];
    Y[3] = xy[1][3];
    redDot = (CircleProperties) props.getPropertiesByName("highlightedPoints");
    blackDot = (CircleProperties) props.getPropertiesByName("Points");
    blackLine = (PolylineProperties) props.getPropertiesByName("Line");
    grayLine = (PolylineProperties) props.getPropertiesByName("grayedLine");
    startScreen();
    language.finalizeGeneration();
    return language.toString();
  }

  private void startScreen() {
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    header = language.newText(new Coordinates(20, 30), "De Casteljau",
        "header", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    hRect = language.newRect(new Offset(-5, -5, "header",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
        null, rectProps);
    LinkedList<Primitive> head = new LinkedList<Primitive>();
    head.add(header);
    head.add(hRect);

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    Text h = language.newText(new Coordinates(10, 100),
        "Der Algorithmus von de Casteljau berechnet eine belibig genaue",
        "description1", null, textProps);
    Text h1 = language.newText(new Offset(0, 25, "description1",
        AnimalScript.DIRECTION_NW),
        "Näherungsdarstellung für Bezierkurven mittels Polylines.",
        "description2", null, textProps);
    Text h2 = language.newText(new Offset(0, 25, "description2",
        AnimalScript.DIRECTION_NW),
        "Je nach Parameter t lässt sich eine beliebige Stelle interpolieren",
        "description3", null, textProps);
    Text h3 = language.newText(new Offset(0, 25, "description3",
        AnimalScript.DIRECTION_NW),
        "oder bei Parametern grösser 1 extrapolieren.", "description4", null,
        textProps);
    language.nextStep("Start");
    language.hideAllPrimitivesExcept(head);
    h.hide();
    h1.hide();
    h2.hide();
    h3.hide();

    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);

    src = language.newSourceCode(new Coordinates(10, 50), "sourceCode", null,
        sourceCodeProps);
    src.addCodeLine("1. Finde gewichtetes Mittel (t = " + t
        + ") von jeweils zwei adjazenten Stützpunkten", null, 0, null); // 0
    src.addCodeLine(
        "2. Verbinde neue Knoten die auf benachbarten Kanten des alten Graphen liegen",
        null, 0, null); // 1
    src.addCodeLine("3. Wiederhole 1 und 2 bis nur ein neuer Knoten entsteht",
        null, 0, null); // 2
    src.addCodeLine(
        "4. Bilden von 2 neuen Bezierkurven mit letztem Knoten als Randknoten",
        null, 0, null); // 3
    src.addCodeLine(
        "5. Wiederhole 1 mit 4 fuer alle Bezierkurven bis die gewuenschte Genauigkeit erreicht wurde",
        null, 0, null); // 4

    // language.hideAllPrimitivesExcept(head);
    language.nextStep("Zeige Code");
    int count = 0;
    bezier bezzi = new bezier(new myPoint(X[count], Y[count++]), new myPoint(
        X[count], Y[count++]), new myPoint(X[count], Y[count++]), new myPoint(
        X[count], Y[count++]), this);
    bezzi.draw(blackLine);
    stack.add(bezzi);
    count = steps;
    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "First question group", 1);

    language.addQuestionGroup(groupInfo);
    MultipleChoiceQuestionModel m1 = new MultipleChoiceQuestionModel("Linear");
    m1.setPrompt("Welche Formel errechnet das nach t gewichtete Mittel von a und b?");
    m1.addAnswer("t*a + (1-t)*b", 5, "Richtig!");
    m1.addAnswer("sqrt(a²+b²)", 0,
        "Falsch! So berechnet man die Hypothenuse in einem rechtwinkligen Dreieck.");
    m1.addAnswer("(a+b)/2", 1, "Diese Formel stimmt nur für t = 0.5");
    m1.setGroupID("First question group");
    language.addMCQuestion(m1);
    int lastStep = count;
    while (stack.size() > 0) {
      bezier curr = stack.pop();
      if (curr.tiefe < count)
        curr.divide(t, lastStep);
      lastStep = curr.tiefe;
    }
    TrueFalseQuestionModel tf = new TrueFalseQuestionModel("mehr?", true, 5);
    tf.setPrompt("Kann der Algorithmus auch für Beziér-Kurven von höherem Grad verwendet werden?");
    tf.setFeedbackForAnswer(true, "Genau! Das Verfahren bleibt das selbe.");
    tf.setFeedbackForAnswer(false,
        "Doch! Hierbei erhöht sich natürlich die Laufzeit.");
    tf.setGroupID("First question group");
    language.addTFQuestion(tf);
    bezzi.draw(blackLine);
    src.hide();
    language.newText(new Coordinates(10, 100),
        "Der Algorithmus hat eine Komplexität von O(q²)", "endT", null,
        textProps);
    language.newText(new Offset(0, 25, "endT", AnimalScript.DIRECTION_NW),
        "Weiter ist er für Parameter zwischen 0 und 1 numerisch stabil.",
        "description2", null, textProps);
    language.nextStep("Endergebnis");

  }

  private static final String DESCRIPTION = "Der Algorithmus von de Casteljau berechnet eine belibig genaue Näherungsdarstellung fuer Bezierkurven mittels Polylines. Je nach Parameter lässt sich eine beliebige Stelle interpolieren oder bei Parametern grösser 1 extrapolieren.";

  private static final String SOURCE_CODE = "1. Finde gewichtetes (=> t) Mittel von jeweils zwei adjazenten Stützpunkten \n"
                                              + "2. Verbinde neue Knoten die auf benachbarten Kanten des alten Graphen liegen\n"
                                              + "3. Wiederhole 1 und 2 bis nur ein neuer Knoten entsteht\n"
                                              + "4. Bilden von 2 neuen Bezierkurven mit letztem Knoten als Randknoten\n"
                                              + "5. Wiederhole 1 mit 4 für alle Bezierkurven bis die gewünschte Genauigkeit erreicht wurde\n";                                                                                                                                       // 0

  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  public String getName() {
    return "De Casteljau";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getAlgorithmName() {
    return "De Casteljau";
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  @Override
  public String getAnimationAuthor() {
    return "Philipp Dürr";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}

class bezier {
  private myPoint         a, b, c, d;
  private bezier          sub1    = null, sub2 = null;
  private final Language  lang;
  private final Casteljau cas;
  boolean                 divided = false;
  int                     tiefe   = 0;

  public bezier(myPoint a, myPoint b, myPoint c, myPoint d, Casteljau cas) {
    super();
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.cas = cas;
    this.lang = cas.language;
  }

  public bezier divide(double t, int lastStep) {
    myPoint b1, b2, c1, c2, help, middle;
    b1 = a.interpolate(b, t);
    help = b.interpolate(c, t);
    c2 = c.interpolate(d, t);
    c1 = b1.interpolate(help, t);
    b2 = help.interpolate(c2, t);
    middle = c1.interpolate(b2, t);

    cas.src.toggleHighlight(3, 0);
    cas.src.toggleHighlight(4, 0);
    Circle circ1 = lang.newCircle(b1.asNode(), cas.pointSize, "", cas.noTime);
    Circle circ2 = lang.newCircle(help.asNode(), cas.pointSize, "", cas.noTime);
    Circle circ3 = lang.newCircle(c2.asNode(), cas.pointSize, "", cas.noTime);

    if (tiefe == 0)
      lang.nextStep("Algorithmus Anfang");
    else
      lang.nextStep();
    cas.src.toggleHighlight(0, 1);
    Node[] first = { b1.asNode(), help.asNode(), c2.asNode() };
    lang.addItem(new Polyline(new AnimalPolylineGenerator(lang), first, "poly"
        + lang.getStep(), cas.noTime, cas.blackLine));
    circ1.hide();
    circ2.hide();
    circ3.hide();

    lang.nextStep();
    cas.src.toggleHighlight(1, 2);

    lang.nextStep();
    cas.src.toggleHighlight(2, 0);
    Circle circ4 = lang.newCircle(c1.asNode(), cas.pointSize, "", cas.noTime);
    Circle circ5 = lang.newCircle(b2.asNode(), cas.pointSize, "", cas.noTime);

    lang.nextStep();
    cas.src.toggleHighlight(0, 1);
    circ4.hide();
    circ5.hide();
    Node[] second = { c1.asNode(), b2.asNode() };
    lang.addItem(new Polyline(new AnimalPolylineGenerator(lang), second,
        "2poly" + lang.getStep(), cas.noTime, cas.blackLine));

    lang.nextStep();
    cas.src.toggleHighlight(1, 2);
    lang.addItem(new Circle(new AnimalCircleGenerator(lang), middle.asNode(),
        cas.pointSize, "", cas.noTime, cas.redDot));

    lang.nextStep();
    cas.src.toggleHighlight(2, 3);
    lang.addItem(new Polyline(new AnimalPolylineGenerator(lang), first,
        "Overpoly" + lang.getStep(), new MsTiming(0), cas.grayLine));
    lang.addItem(new Polyline(new AnimalPolylineGenerator(lang), second,
        "Over2poly" + lang.getStep(), new MsTiming(0), cas.grayLine));
    lang.addItem(new Circle(new AnimalCircleGenerator(lang), middle.asNode(),
        cas.pointSize, "", new MsTiming(0), cas.redDot));
    sub1 = new bezier(a, b1, c1, middle, cas);
    sub1.tiefe = tiefe + 1;
    sub2 = new bezier(middle, b2, c2, d, cas);
    sub2.tiefe = tiefe + 1;
    draw(cas.grayLine);
    sub1.draw(cas.blackLine);
    sub2.draw(cas.blackLine);
    lang.nextStep();
    cas.src.toggleHighlight(3, 4);
    if (tiefe != lastStep)
      lang.nextStep("Tiefe: " + (1 + tiefe));
    else
      lang.nextStep("       ...");
    divided = true;
    cas.stack.add(sub1);
    cas.stack.add(sub2);
    return this;
  }

  public void draw(PolylineProperties pp) {
    Node[] first = { a.asNode(), b.asNode(), c.asNode(), d.asNode() };
    lang.addItem(new Polyline(new AnimalPolylineGenerator(lang), first, "poly"
        + lang.getStep(), new MsTiming(0), pp));
    lang.addItem(new Circle(new AnimalCircleGenerator(lang), a.asNode(),
        cas.pointSize, "", new MsTiming(0), cas.redDot));
    lang.addItem(new Circle(new AnimalCircleGenerator(lang), d.asNode(),
        cas.pointSize, "", new MsTiming(0), cas.redDot));
  }
}

class myPoint {
  private double x, y;

  public myPoint(double x, double y) {
    super();
    this.x = x;
    this.y = y;
  }

  public myPoint interpolate(myPoint that, double t) {
    double tINV = 1.0f - t;
    return new myPoint(this.x * t + that.getX() * tINV, this.y * t
        + that.getY() * tINV);
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public Node asNode() {
    return new Coordinates((int) x, (int) y);
    // return new Offset((int) Math.floor(x), (int) Math.floor(y), origin,
    // AnimalScript.DIRECTION_SE);
  }
}