package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Point;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class JarvisMarch implements ValidatingGenerator {
  private Language             lang;
  private CircleProperties     polygon;
  private int[]                ycoords;
  // private Color lineColor;
  private Color                lineHull;
  private Color                lineBackColor;
  private Color                circleHighlight;
  private Color                circleNormal;

  private TextProperties       standardText;
  private SourceCodeProperties sourceCode;
  private RectProperties       box;
  private int[]                xcoords;

  class PolarPoint {
    public int x;
    public int y;

    public PolarPoint(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public double polarDegree(PolarPoint p) {
      int faktor = 0;
      if (p.y < this.y) {
        faktor = 360;
      }
      return Math.toDegrees(Math.atan2(p.y - this.y, p.x - this.x)) + faktor;
    }

    public boolean equals(PolarPoint p) {
      return p.x == this.x && p.y == this.y;
    }
  }

  class Edge {
    public int start;
    public int end;

    public Edge(int start, int end) {
      this.start = start;
      this.end = end;
    }

    public boolean equals(Edge e) {
      return e.start == this.start && e.end == this.end ? true : false;
    }

    public String toString() {
      return this.start + " -> " + this.end;
    }
  }

  public void init() {
    lang = new AnimalScript("Jarvis' March [En]",
        "David Kaufmann, Holger Thies", 800, 600);
    lang.setStepMode(true);
  }

  public int findLowest(int[] xcoords, int[] ycoords) {
    int lowest = 0;
    for (int i = 1; i < xcoords.length; i++) {
      if (ycoords[i] < ycoords[lowest]
          || (ycoords[i] == ycoords[lowest] && xcoords[i] < xcoords[lowest])) {
        lowest = i;
      }
    }
    return lowest;
  }

  public int findHighest(int[] xcoords, int[] ycoords) {
    int highest = 0;
    for (int i = 1; i < xcoords.length; i++) {
      if (ycoords[i] > ycoords[highest]) {
        highest = i;
      }
    }
    return highest;
  }

  public void hideCircles(Circle[] points) {
    for (int i = 0; i < points.length; i++) {
      points[i].hide();
    }
  }

  public void showCircles(Circle[] points) {
    for (int i = 0; i < points.length; i++) {
      points[i].show();
    }
  }

  public void endScreen(int pointsInHull, int inside, int steps) {
    lang.newText(new Coordinates(440, 80), "Points in hull: " + pointsInHull,
        "points", null, standardText);
    lang.newText(new Coordinates(440, 100), "Points inside hull: " + inside,
        "inside", null, standardText);
    lang.newText(new Coordinates(440, 120), "Operations: " + steps, "steps",
        null, standardText);
    lang.newText(new Coordinates(40, 320),
        "As presumed Jarvis march needs nh operations", "conc1", null,
        standardText);
    lang.newText(new Coordinates(40, 340), (pointsInHull + inside)
        + " Points * " + pointsInHull + " Poins in hull = " + (steps)
        + " Operations", "conc2", null, standardText);
  }

  public void startScreen() {
    lang.newRect(new Coordinates(10, 20), new Coordinates(200, 49), "box",
        null, box);
    Text header = lang.newText(new Coordinates(20, 30), "Jarvis' March",
        "header", null, standardText);
    Font f = new Font("Sans Serif", Font.BOLD, 24);
    header.setFont(f, null, null);
    lang.nextStep("Start");
    Text startText1 = lang
        .newText(
            new Coordinates(20, 50),
            "The Jarvis' March algorithm is a simple algorithm to determine the convex hull of a set of points.",
            "startText1", null, standardText);
    lang.nextStep();
    Text startText2 = lang
        .newText(
            new Coordinates(20, 65),
            "The convex hull is the smallest convex polygon containing all the given points.",
            "startText2", null, standardText);
    lang.nextStep();
    Text startText3 = lang
        .newText(
            new Coordinates(20, 80),
            "The running time of the algorithm is in O(nh) where n is the number of given points and h is the number of corner points of the convex hull.",
            "startText3", null, standardText);
    lang.nextStep();
    Text descriptionText = lang.newText(new Coordinates(20, 100),
        "Description of the Algorithm:", "startText", null, standardText);
    lang.nextStep();
    Text step1 = lang.newText(new Coordinates(20, 120),
        "1) Find the lowest Point p.", "step1", null, standardText);
    lang.nextStep();
    Text step2 = lang
        .newText(
            new Coordinates(20, 140),
            "2) Search the Point with the smallest Polarangle from p. Add it to the hull. Ignore Points that already are in the hull.",
            "step2", null, standardText);
    lang.nextStep();
    Text step3 = lang.newText(new Coordinates(20, 160),
        "3) Mark an edge between the Points and set the last Point found as p",
        "step3", null, standardText);
    lang.nextStep("Introduction");
    Text step4 = lang
        .newText(
            new Coordinates(20, 180),
            "4) Repeat step 2-3 until the first and last point in the hull are identically",
            "step4", null, standardText);
    lang.nextStep();
    startText1.hide();
    startText2.hide();
    startText3.hide();
    descriptionText.hide();
    step1.hide();
    step2.hide();
    step3.hide();
    step4.hide();
  }

  private boolean allPointsOnLine(int[] xcoords, int[] ycoords) {
    boolean onXLine = true;
    boolean onYLine = true;
    for (int i = 1; i < xcoords.length; i++) {
      if (xcoords[i] != xcoords[i - 1])
        onXLine = false;
      if (ycoords[i] != ycoords[i - 1])
        onYLine = false;
    }
    return onXLine || onYLine;
  }

  private boolean isBetterHullPoint(PolarPoint currentPoint,
      PolarPoint lastPoint, PolarPoint minPolarPoint) {
    if (currentPoint.x <= lastPoint.x && currentPoint.y == lastPoint.y
        && minPolarPoint.x <= currentPoint.x)
      return true;
    if (currentPoint.x >= lastPoint.x && currentPoint.y == lastPoint.y
        && minPolarPoint.x >= currentPoint.x)
      return true;
    if (currentPoint.x == lastPoint.x && currentPoint.y <= lastPoint.y
        && minPolarPoint.y <= currentPoint.y)
      return true;
    if (currentPoint.x == lastPoint.x && currentPoint.y >= lastPoint.y
        && minPolarPoint.x >= currentPoint.x)
      return true;
    return false;
  }

  public void jarvis(Circle[] points, int[] xcoords, int[] ycoords,
      SourceCode sc) {
    hideCircles(points);
    startScreen();
    showCircles(points);
    lang.newPolyline(new Coordinates[] { new Coordinates(0, 300),
        new Coordinates(400, 300) }, "x-axis", null);
    lang.newPolyline(new Coordinates[] { new Coordinates(10, 70),
        new Coordinates(10, 310) }, "y-axis", null);
    sc.addCodeLine("Jarvis-March(Point[] p){", null, 0, null);
    sc.addCodeLine("currentPoint = findLowestPoint(p);", null, 1, null);
    sc.addCodeLine("hull = listOfPoints;", null, 1, null);
    sc.addCodeLine("hull.addFirst(currentPoint);", null, 1, null);
    sc.addCodeLine("do{", null, 1, null);
    sc.addCodeLine("minAngle = Double.MAX_VALUE;", null, 2, null);
    sc.addCodeLine("for(int i = 0; i < p.length; i++){", null, 2, null);
    sc.addCodeLine("//polarAngle takes a Point and calculates the Polarangle",
        null, 3, null);
    sc.addCodeLine("currentAngle = currentPoint.polarAngle(p[i])){", null, 3,
        null);
    sc.addCodeLine(
        "if(currentAngle < minAngle AND (highest Point not in hull OR currentPoint.y < lastPoint.y)){",
        null, 3, null);
    sc.addCodeLine("minAngle = currentPoint.polarAngle(p[i]);", null, 4, null);
    sc.addCodeLine("PolarPoint = p[i];", null, 4, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("currentPoint = PolarPoint;", null, 2, null);
    sc.addCodeLine("hull.addLast(currentPoint);", null, 2, null);
    sc.addCodeLine("} while(First Point in Hull != Last Point in Hull);", null,
        1, null);
    sc.addCodeLine("return hull;", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    Color textColor = (Color) sc.getProperties().get(
        AnimationPropertiesKeys.COLOR_PROPERTY);
    Color textHighlightColor = (Color) sc.getProperties().get(
        AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);

    int start = findLowest(xcoords, ycoords);
    int currentPoint = start;
    int posMinPolarPoint = 0;
    int numberPoints = 0;
    int steps = 0;
    boolean isNotBackEdge = true;
    boolean alreadyTriggered = false;
    HashMap<String, Polyline> lines = new HashMap<String, Polyline>();
    // edges in hull
    LinkedList<Edge> hull = new LinkedList<Edge>();
    LinkedList<Integer> vertexHull = new LinkedList<Integer>();

    PolarPoint lastPoint = new PolarPoint(xcoords[start], ycoords[start]);
    Text smallestAngleText = lang.newText(new Coordinates(440, 50),
        "Smallest PolarAngle: inf", "t", null);
    Text currentAngleText = lang.newText(new Coordinates(440, 70),
        "Current PolarAngle:", "t", null);
    Text text = lang.newText(new Coordinates(440, 90), "", "t", null);

    lang.nextStep("Start of Algorithm");
    points[currentPoint].changeColor(null, circleHighlight, null, null);
    sc.highlight(1);
    sc.highlight(2);
    sc.highlight(3);
    lang.nextStep();
    sc.unhighlight(1);
    sc.unhighlight(2);
    sc.unhighlight(3);
    do {
      numberPoints++;
      alreadyTriggered = false;
      double smallestDegree = Double.MAX_VALUE;
      smallestAngleText.setText("Smallest PolarAngle: inf", null, null);
      smallestAngleText.changeColor(null, textHighlightColor, null, null);
      currentAngleText.setText("Current PolarAngle:", null, null);
      currentAngleText.changeColor(null, textHighlightColor, null, null);
      sc.highlight(5);
      lang.nextStep("Iteration " + numberPoints);
      smallestAngleText.changeColor(null, textColor, null, null);
      currentAngleText.changeColor(null, textColor, null, null);
      sc.unhighlight(5);
      int highest = findHighest(xcoords, ycoords);
      for (int i = 0; i < xcoords.length; i++) {
        // zählt wieviele schritte zum finden
        // der konvexen Hülle benötigt wurden
        steps++;
        // nach jeder neu gefundenen kante(sobald eine gefunden wurde)
        // wird ein neuer step wegen der übersichtlichkeit eingefügt
        if (!alreadyTriggered && hull.size() != 0) {
          alreadyTriggered = true;
        }
        PolarPoint p = new PolarPoint(xcoords[i], ycoords[i]);
        Edge edge = new Edge(currentPoint, i);
        Edge backEdge = new Edge(i, currentPoint);
        // ist noch keine kante in der hülle?
        isNotBackEdge = hull.size() == 0 ? true : !hull.getFirst().equals(
            backEdge);
        if (!lastPoint.equals(p)) {
          double degree = lastPoint.polarDegree(p);
          String lineId = null;
          Polyline pl = null;
          // wenn keine Rückkante dann
          // erstelle neue linie und
          // ändere text
          if (isNotBackEdge) {
            lineId = edge.toString();
            Node[] line = { points[currentPoint].getCenter(),
                points[i].getCenter() };
            pl = lang.newPolyline(line, lineId, null);
            lines.put(lineId, pl);
            currentAngleText.setText("Current PolarAngle: " + degree, null,
                null);
            sc.highlight(8);
            if (degree < smallestDegree && isNotBackEdge
                && (vertexHull.contains(highest) && p.y > lastPoint.y)) {
              currentAngleText.setText(
                  "Current Polarpoint is higher than the last Polarpoint.",
                  null, null);
              pl.changeColor(null, lineBackColor, null, null);
            }
            currentAngleText.changeColor(null, textHighlightColor, null, null);
            lang.nextStep();
            // if(pl.getProperties().get("color") == lineBackColor)
            // pl.changeColor(null, textColor, null, null);
            currentAngleText.changeColor(null, textColor, null, null);
            sc.unhighlight(8);
            // ansonsten
            // hole linie, ändere farbe und
            // ändere text
          } else {
            lineId = backEdge.toString();
            pl = (Polyline) lines.get(lineId);
            pl.changeColor(null, lineBackColor, null, null);
            pl.show();
            currentAngleText.setText("Current PolarAngle: is the Backedge",
                null, null);
            sc.highlight(8);
            currentAngleText.changeColor(null, textHighlightColor, null, null);
            lang.nextStep();
            currentAngleText.changeColor(null, textColor, null, null);
            sc.unhighlight(8);
            pl.changeColor(null, lineHull, null, null);
          }
          // falls winkel der zwei punkte kleiner und keine rückkante
          // ändere text, farbe der linie und
          // markiere den punkt als den mit dem kleinsten polarwinkel

          if ((degree < smallestDegree || (degree == smallestDegree && isBetterHullPoint(
              new PolarPoint(xcoords[currentPoint], ycoords[currentPoint]),
              lastPoint, new PolarPoint(xcoords[posMinPolarPoint],
                  ycoords[posMinPolarPoint]))))
              && isNotBackEdge
              && (!vertexHull.contains(highest) || p.y < lastPoint.y || (degree == 0 && p.y == ycoords[start]))) {
            smallestDegree = degree;
            smallestAngleText.changeColor(null, textHighlightColor, null, null);
            smallestAngleText.setText("Smallest PolarAngle: " + smallestDegree,
                null, null);
            pl.changeColor(null, textHighlightColor, null, null);
            posMinPolarPoint = i;
            sc.highlight(10);
            sc.highlight(11);
            lang.nextStep();
            sc.unhighlight(10);
            sc.unhighlight(11);
            smallestAngleText.changeColor(null, textColor, null, null);
            // }
          } else {
            smallestAngleText.changeColor(null, textColor, null, null);
          }

          text.setText(edge.toString(), null, null);
          // hide line that was shown
          pl.hide();
        }
      }
      sc.highlight(14);
      sc.highlight(15);
      sc.highlight(16);
      points[currentPoint].changeColor(null, circleNormal, null, null);
      lastPoint = new PolarPoint(xcoords[posMinPolarPoint],
          ycoords[posMinPolarPoint]);
      hull.addFirst(new Edge(currentPoint, posMinPolarPoint));
      vertexHull.addFirst(posMinPolarPoint);
      currentPoint = posMinPolarPoint;
      points[currentPoint].changeColor(null, circleHighlight, null, null);
      for (Edge e : hull) {
        Polyline pLine = (Polyline) lines.get(e.toString());
        pLine.changeColor(null, lineHull, null, null);
        pLine.show();
      }
      lang.nextStep();
      sc.unhighlight(14);
      sc.unhighlight(15);
      sc.unhighlight(16);
    } while (start != currentPoint);
    for (Edge e : hull) {
      Polyline pl = (Polyline) lines.get(e.toString());
      pl.changeColor(null, lineHull, null, null);
      pl.show();
    }// */
    sc.highlight(17);
    points[start].changeColor(null, circleNormal, null, null);
    lang.nextStep();
    sc.hide();
    currentAngleText.hide();
    smallestAngleText.hide();
    text.hide();
    endScreen(numberPoints, points.length - numberPoints, steps);
    // hide lines
    /*
     * for (Edge e : hull){ Polyline pl = (Polyline) lines.get(e.toString());
     * pl.changeColor(null, lineHull, null, null); pl.hide(); }//
     */
    lang.nextStep("Conclusion");
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    polygon = (CircleProperties) props.getPropertiesByName("point");
    ycoords = (int[]) primitives.get("ycoords");
    // lineColor = (Color) primitives.get("lineColor");
    standardText = (TextProperties) props.getPropertiesByName("standardText");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    xcoords = (int[]) primitives.get("xcoords");
    lineHull = (Color) primitives.get("lineHull");
    lineBackColor = (Color) primitives.get("lineBackColor");
    circleHighlight = (Color) primitives.get("nodeHighlightColor");
    circleNormal = (Color) polygon.get(AnimationPropertiesKeys.COLOR_PROPERTY);
    box = (RectProperties) props.getPropertiesByName("box");
    SourceCode sc = lang.newSourceCode(new Coordinates(40, 320), "sourceCode",
        null, sourceCode);

    // set orign
    // DisplayOptions displayProps = new ArrayDisplayOptions(new Timing(0), new
    // Timing(0), false);
    PointProperties pProp = new PointProperties();
    Point origin = lang.newPoint(new Coordinates(20, 300), "origin", null,
        pProp);
    Circle[] points = new Circle[xcoords.length];

    for (int i = 0; i < xcoords.length; i++) {
      Node pos = new Offset(xcoords[i], -Math.min(ycoords[i], 200), origin,
          AnimalScript.DIRECTION_SW);
      pProp.setName("pProp");
      Circle p = lang.newCircle(pos, 3, "p" + i, null, polygon);
      // p.changeColor("fill", Color.BLUE, null, null);
      points[i] = p;
    }
    if (allPointsOnLine(xcoords, ycoords)) {
      lang.newRect(new Coordinates(10, 20), new Coordinates(200, 49), "box",
          null, box);
      Text header = lang.newText(new Coordinates(20, 30), "Jarvis' March",
          "header", null, standardText);
      Font f = new Font("Sans Serif", Font.BOLD, 24);
      header.setFont(f, null, null);
      lang.newText(new Coordinates(20, 300),
          "All points on a line! Convex hull cannot be computed.", "error",
          null, standardText);
    } else
      jarvis(points, xcoords, ycoords, sc);
    return lang.toString();
  }

  public String getName() {
    return "Jarvis' March";
  }

  public String getAlgorithmName() {
    return "Jarvis' March";
  }

  public String getAnimationAuthor() {
    return "David Kaufmann, Holger Thies";
  }

  public String getDescription() {
    return "The Jarvis' March algorithm is a simple algorithm to determine the convex hull of a set of points.<br>"
        + "\n"
        + "The convex hull is the smallest convex polygon containing all the given points. <br>"
        + "\n"
        + "The running time of the algorithm is in O(nh) where n is the number of given points and h is the number of corner points of the convex hull.<br>"
        + "\n"
        + "<br>"
        + "\n"
        + "Description of the algorithm: <br>"
        + "\n"
        + "	1) Set the current number of edges h to 0  <br>"
        + "\n"
        + "	2) Find point with lowest y-coordinate (p0). If there is more than one, take the leftmost point.  <br>"
        + "\n"
        + "	3) set last found point as edge <br>"
        + "\n"
        + "	4) Find the rightmost point from the last point found <br>"
        + "\n"
        + "	5) Increment h <br>"
        + "\n"
        + "	6) Repeat 3 - 6 until p0 is found again <br>" + "\n" + " " + "\n";
  }

  public String getCodeExample() {
    return "JarvisMarch(point[]){" + "\n" + "	h := 0" + "\n"
        + "  	p0 := findLowestPoint(point[])" + "\n" + "	nextEdge = 0;" + "\n"
        + " 	do {" + "\n" + "		EdgePoint[h] = point[nextEdge];" + "\n"
        + "		nextEdge = RightMostPointFrom(nextEdge);" + "\n" + "	}" + "\n"
        + "		" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    ycoords = (int[]) primitives.get("ycoords");
    xcoords = (int[]) primitives.get("xcoords");
    if (ycoords.length != xcoords.length)
      return false;
    return true;
  }

}