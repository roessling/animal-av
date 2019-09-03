package generators.graphics;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
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
import algoanim.util.Offset;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

public class Quickhull implements Generator {

  /**
   * The concrete language object used for creating output
   */
  static Language              language;

  /**
   * Globally defined text properties
   */
  private TextProperties       textProps;
  static CircleProperties      blackPoint;
  static CircleProperties      redPoint;
  static CircleProperties      bluePoint;
  static int                   pointSize;
  static PolylineProperties    blueLine;
  static PolylineProperties    grayLine;
  static PolylineProperties    finalLine;
  RectProperties               rectProps   = new RectProperties();
  private SourceCodeProperties sourceCodeProps;
  DisplayOptions               noTime      = new MsTiming(0);
  static SourceCode            src;
  static Point                 offset      = new Point(20, 270);
  static int                   rec         = 0;
  static int                   step        = 0;

  private LinkedList<Point>    points;

  private final String         DESCRIPTION = "Der Quickhull Algorithmus berechnet für eine endlcihe Menge Punkte die konvexe Hülle. Hierbei geht er ähnlich wie der Quicksort Algorithmus vor indem er alle Punkte in innerhalb und auserhalb unterteilt und dann rekursiv fortfährt.";

  @Override
  public void init() {
    language = new AnimalScript("Quickhull", "Philipp Dürr", 800, 600);
    language.setStepMode(true);
    points = new LinkedList<Point>();
    language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    step = 0;
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    boolean generatePoints = (Boolean) primitives.get("hasRandomPoints");
    int randPoints = (Integer) primitives.get("numberOfRandomPoints");
    pointSize = (Integer) primitives.get("pointSize");
    blackPoint = (CircleProperties) props.getPropertiesByName("Points");
    redPoint = (CircleProperties) props
        .getPropertiesByName("highlightedPoints");
    bluePoint = (CircleProperties) props.getPropertiesByName("activePoints");
    blueLine = (PolylineProperties) props.getPropertiesByName("activeLine");
    grayLine = (PolylineProperties) props.getPropertiesByName("grayLine");
    finalLine = (PolylineProperties) props.getPropertiesByName("finalLine");

    if (generatePoints) {
      Random r = new Random();
      for (int i = 0; i < randPoints; i++) {
        points.add(new Point(r.nextInt(400) + 50, r.nextInt(400) + 50));
      }
    } else {
      int[][] xy = (int[][]) primitives.get("xy");
      for (int[] p : xy) {
        points.add(new Point(p[0], p[1]));
      }
    }
    language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    drawGrid();
    renderPoints();
    showStart();
    showCode();
    run();
    showEnd();
    language.finalizeGeneration();
    return language.toString();
  }

  private void drawGrid() {
    language.newPolyline(new Coordinates[] { new Point(500, 0).coords(),
        new Point(0, 0).coords(), new Point(0, 500).coords(),
        new Point(500, 500).coords(), new Point(500, 0).coords() }, "", noTime);

    language.newCircle(new Point(520, 50).coords(), pointSize, "pointlabel",
        noTime, blackPoint);
    language.newText(new Offset(6, -9, "pointlabel", AnimalScript.DIRECTION_C),
        "Punkt", "p", noTime);

    language.newCircle(new Offset(0, 15, "pointlabel", "S"), pointSize,
        "bpointlabel", noTime, bluePoint);
    language.newText(
        new Offset(6, -9, "bpointlabel", AnimalScript.DIRECTION_C),
        "aktuell betrachteter Punkt", "p", noTime);

    language.newCircle(new Offset(0, 15, "bpointlabel", "S"), pointSize,
        "rpointlabel", noTime, redPoint);
    language.newText(
        new Offset(6, -9, "rpointlabel", AnimalScript.DIRECTION_C),
        "Punkt auf Hüllkörper", "p", noTime);

    language.newPolyline(new Offset[] { new Offset(-5, 15, "rpointlabel", "S"),
        new Offset(5, 15, "rpointlabel", "S") }, "line", noTime);
    language.newText(new Offset(6, -9, "line", AnimalScript.DIRECTION_E),
        "Linie", "p", noTime);

    language.newPolyline(new Offset[] { new Offset(-5, 15, "line", "S"),
        new Offset(5, 15, "line", "S") }, "activeline", noTime, blueLine);
    language.newText(new Offset(6, -9, "activeline", AnimalScript.DIRECTION_E),
        "aktive Linie", "p", noTime);

    language.newPolyline(new Offset[] { new Offset(-5, 15, "activeline", "S"),
        new Offset(5, 15, "activeline", "S") }, "grayedline", noTime, grayLine);
    language.newText(new Offset(6, -9, "grayedline", AnimalScript.DIRECTION_E),
        "verworfene Linie", "lastlegend", noTime);

    language.newRect(
        new Offset(-5, -5, "pointlabel", AnimalScript.DIRECTION_NW),
        new Offset(60, 5, "lastlegend", "SE"), "hRect", null, rectProps);
  }

  private void renderPoints() {
    for (Point p : points) {
      p.draw(language, blackPoint);
    }
  }

  private void showStart() {
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    language.newText(new Coordinates(20, 30), "Quickhull", "header", null,
        headerProps);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    language.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    Text h = language
        .newText(
            new Coordinates(10, 100),
            "Der Quickhull Algorithmus berechnet für eine endliche Menge Punkte die konvexe Hülle.",
            "description1", null, textProps);
    Text h1 = language
        .newText(
            new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
            "Hierbei geht er ähnlich wie der Quicksort Algorithmus vor indem er alle Punkte",
            "description2", null, textProps);
    Text h2 = language.newText(new Offset(0, 25, "description2",
        AnimalScript.DIRECTION_NW),
        "in innerhalb und auserhalb unterteilt und dann rekursiv fortfährt.",
        "description3", null, textProps);

    language.nextStep("Start");
    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "First question group", 1);

    language.addQuestionGroup(groupInfo);
    FillInBlanksQuestionModel m1 = new FillInBlanksQuestionModel("Gegenteil");
    m1.setPrompt("Was ist das Gegenteil von konvex?");
    m1.addAnswer("concav", 3,
        "Die Schreibweise ist entweder konkav, oder im englischen concave!");
    m1.addAnswer("concave", 5, "Richtig!");
    m1.addAnswer("konkav", 5, "Richtig!");
    m1.setGroupID("First question group");
    language.addFIBQuestion(m1);
    h.hide();
    h1.hide();
    h2.hide();
  }

  private void showCode() {
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);

    src = language.newSourceCode(new Coordinates(10, 50), "sourceCode", null,
        sourceCodeProps);
    src.addCodeLine("I Initialisierung", null, 0, null); // 0
    src.addCodeLine("    1. Finde 2 Punkte der Hülle(maximales/minimales y)",
        null, 0, null); // 1
    src.addCodeLine("    2. Verbinden der Punkte", null, 0, null); // 2
    src.addCodeLine(
        "    3. Aufruf des Rekursiven Parts für Hin- und Rückkante", null, 0,
        null); // 3
    src.addCodeLine("II Rekursion", null, 0, null); // 4
    src.addCodeLine(
        "    1. Finde den äußeren Punkt der am weitesten von der aktuellen Kante entfernten ist",
        null, 0, null); // 5
    src.addCodeLine(
        "    2. Gebe die Kante zurück wenn kein äußerer Punkt gefunden wurde",
        null, 0, null); // 6
    src.addCodeLine(
        "    3. Ersetze aktuelle Kante durch 2 neue Kanten über den Punkt",
        null, 0, null); // 7
    src.addCodeLine("    4. Rekursiver Aufruf für die neuen Kanten", null, 0,
        null); // 8
    language.nextStep("Zeige Code");
  }

  private void run() {
    src.highlight(0);
    src.highlight(1);
    Point min = points.getFirst(), max = points.getLast();
    for (Point c : points) {
      if (min.y > c.y)
        min = c;
      if (max.y < c.y)
        max = c;
    }
    language.newCircle(min.coords(), 3, "", noTime, redPoint);
    language.newCircle(max.coords(), 3, "", noTime, redPoint);
    language.nextStep("Initialisierung");
    src.toggleHighlight(1, 2);
    language.newPolyline(new Coordinates[] { max.coords(), min.coords() }, "",
        noTime);
    language.nextStep();
    src.toggleHighlight(2, 3);
    language.nextStep();
    src.unhighlight(3);
    src.toggleHighlight(0, 4);
    new quickhullObject(min, max, points);
    new quickhullObject(max, min, points);

  }

  private void showEnd() {
    MultipleChoiceQuestionModel mp = new MultipleChoiceQuestionModel("prinzip");
    mp.setPrompt("Nach welchem Prinzip geht der Algorithmus vor?");
    mp.addAnswer(
        "Greedy-Algorithmen",
        -2,
        "Greedy (gierige) Algorithmen wählen immer den Folgezustand aus der nach aktuellen Gesichtspunkten am den größten Gewinn verspricht.");
    mp.addAnswer(
        "Backtracking",
        -2,
        "Falsch! Beim Backtracking wird eine mögliche Lösung probiert, sollte man jedoch in einer Sackgasse landen so das absehbar ist dass keine Lösung mehr möglich ist, wird immer die vorherige Entscheidung revidiert bis man eine gültige Lösung erhält.");
    mp.addAnswer(
        "Teile & Herrsche",
        5,
        "Richtig! Die verbleibenden Punkte werden immer weiter in ausserhalb und innerhalb unterteilt, bis keine äußeren Punkte mehr existieren und die Lösung somit trivial ist.");
    mp.setGroupID("First question group");
    language.addMCQuestion(mp);
    src.hide();
    language
        .newText(
            new Coordinates(10, 100),
            "Die Komplexität liegt im Durchschnitt wie auch beim Quicksort bei O(n*log(n))",
            "description1", null, textProps);
    language
        .newText(
            new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
            "Der Worst-Case - jeder Punkt ist Teil der Hülle - hat eine Komplexität von O(n²)",
            "description2", null, textProps);
    language.newText(new Offset(0, 25, "description2",
        AnimalScript.DIRECTION_NW),
        "Alternative Algorithmen: Graham Scan, Jarvis-March", "description3",
        null, textProps);
    language.nextStep("Endergebnis");
  }

  @Override
  public String getAlgorithmName() {
    return "Quickhull";
  }

  @Override
  public String getAnimationAuthor() {
    return "Philipp Dürr";
  }

  @Override
  public String getCodeExample() {

    return "I Initialisierung\n"
        + "\t1. Finde 2 Punkte der Hülle(maximales/minimales y)\n"
        + "\t2. Verbinden der Punkte\n"
        + "\t3. Aufruf des Rekursiven Parts für Hin- und Rückkante\n"
        + "II Rekursion\n"
        + "\t1. Finde den äußeren Punkt der am weitesten von der aktuellen Kante entfernten ist\n"
        + "\t2. Gebe die Kante zurück wenn kein äußerer Punkt gefunden wurde\n"
        + "\t3. Ersetze aktuelle Kante durch 2 neue Kanten über den Punkt\n"
        + "\t4. Rekursiver Aufruf für die neuen Kanten\n";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  @Override
  public String getName() {
    return "Quickhull";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}

class quickhullObject {
  Point             a;
  Point             b;
  Point             n;
  double            d;
  LinkedList<Point> points;

  public quickhullObject(Point a, Point b, LinkedList<Point> points) {
    this.a = a;
    this.b = b;
    n = new Point(a.y - b.y, b.x - a.x);
    n.normalize();
    d = n.x * a.x + n.y * a.y;
    this.points = points;
    // System.out.println(n.x + "x\t+\t" + n.y +"y\t-\t" + d);
    run();
  }

  private void run() {
    Quickhull.language.newPolyline(
        new Coordinates[] { a.coords(), b.coords() }, "", new MsTiming(0),
        Quickhull.blueLine);
    double farest = 0;
    Point newP = null;
    LinkedList<Point> outer = new LinkedList<Point>();
    for (Point p : points) {
      double dis = distance(p);
      if (dis > 0.1) {
        outer.add(p);
        if (dis > farest) {
          newP = p;
          farest = dis;
          // System.out.println(p.x + "\t|\t" + p.y + "\t=\t" + dis);
        }
      }
    }
    for (Point p : outer) {
      p.draw(Quickhull.language, Quickhull.bluePoint);
    }
    if (newP != null)
      newP.draw(Quickhull.language, Quickhull.redPoint);
    Quickhull.src.highlight(5);
    Quickhull.language.nextStep("Rekursion " + Quickhull.step++);
    Quickhull.src.toggleHighlight(5, 6);
    if (newP != null) {
      Quickhull.language.nextStep();
      Quickhull.language.newPolyline(
          new Coordinates[] { a.coords(), newP.coords() }, "", new MsTiming(0));
      Quickhull.language.newPolyline(
          new Coordinates[] { newP.coords(), b.coords() }, "", new MsTiming(0));
      Quickhull.language.newPolyline(
          new Coordinates[] { a.coords(), b.coords() }, "", new MsTiming(0),
          Quickhull.grayLine);
      Quickhull.src.toggleHighlight(6, 7);
      for (Point p : outer) {
        p.draw(Quickhull.language, Quickhull.blackPoint);
      }
      newP.draw(Quickhull.language, Quickhull.redPoint);
      Quickhull.language.nextStep();
      Quickhull.src.toggleHighlight(7, 8);
      Quickhull.language.nextStep();
      Quickhull.src.unhighlight(8);
      if (outer.size() > 1) {
        new quickhullObject(a, newP, outer);
        new quickhullObject(newP, b, outer);
      }
    } else {
      Quickhull.language.newPolyline(
          new Coordinates[] { a.coords(), b.coords() }, "", new MsTiming(0),
          Quickhull.finalLine);
    }
  }

  private double distance(Point p) {
    // ax+by-c
    return n.x * p.x + n.y * p.y - d;
  }
}

class Point {
  float x;
  float y;

  public Point(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void normalize() {
    double length = Math.sqrt(x * x + y * y);
    x /= length;
    y /= length;
  }

  public Coordinates coords() {
    return new Coordinates((int) Math.floor(Quickhull.offset.x + x),
        (int) Math.floor(Quickhull.offset.y + y));
  }

  public void draw(Language lang, CircleProperties c) {
    Quickhull.language.newCircle(coords(), Quickhull.pointSize, "",
        new MsTiming(0), c);
  }
}
