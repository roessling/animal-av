/**
 * Ramer-Douglas-Peucker, der verbesserte Generator (Aufgabe 5.1):
 * 
 * - the improved version of rdp generator gives now an end-user
 *   the possibility for manual placement of main points in the
 *   initial line.
 * - it is still possible, however, to generate points randomly.
 * - by default rdp uses points manually inputed in "PointArray(X.Y)";
 *   to activate the random arrangement of the points one should
 *   define "isRandom" boolean in "RandomPoints" folder as "true",
 *   in this case "PointArray(X.Y)" is ignored and points are going
 *   to be generated randomly with respect to the input variable "p".
 * - it should be also noted that manually created points must have
 *   valid coordinates, if some of them don't have valid coordinates
 *   these will be automatically (randomly) replaced with proper ones.
 *   
 * - added to the description of the generator valid ranges for X and Y
 *   coordinates of input points; a careful reader should be aware of
 *   this information after reading the description section.
 */

package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class RamerDouglasPeucker implements Generator {

  private PolylineProperties   iPolyline;
  private CircleProperties     iCircle;
  private PolylineProperties   rPolyline;
  private CircleProperties     rCircle;
  private PolylineProperties   orthPolyline;
  private PolylineProperties   bPolyline;
  private ArrayProperties      Array;
  private SourceCodeProperties SourceCode;
  private String[]             PointArray;
  private boolean              isRandom;

  // input integers n and e
  private int                  p                = 10;
  private int                  e                = 20;

  // plot zone & radius of the points
  int                          radius           = 4;
  int                          min_X            = 500, max_X = 950;
  int                          min_Y            = 70, max_Y = 220;

  int                          orth_X;
  int                          orth_Y;
  int                          orth_X_max;
  int                          orth_Y_max;

  int                          rec_calls;

  // lists, polylines, etc...
  ArrayList<Integer>           X_Coor           = new ArrayList<Integer>();
  ArrayList<Point>             points           = new ArrayList<Point>();
  ArrayList<Point>             ResultList       = new ArrayList<Point>();
  ArrayList<Circle>            CircleList       = new ArrayList<Circle>();
  ArrayList<ArrayMarker>       ArrayMarkerList  = new ArrayList<ArrayMarker>();
  Node[]                       bucketPolylineSE = new Node[3];
  Polyline                     poly_curr;
  Polyline                     poly_prev;
  Polyline                     poly_orth;
  Circle                       c_orth;
  Circle                       ch;

  // properties for "Scale"
  CircleProperties             cProps           = new CircleProperties();
  Text                         dmaxText;
  Text                         indexText;
  Text                         Result;

  // arrays
  StringArray                  r;
  // ArrayProperties arrayProps;
  ArrayMarker                  m_start;
  ArrayMarker                  m_end;
  ArrayMarker                  m_od_max;

  // strings
  // private final String asHEADER = "Ramer-Douglas-Peucker algorithm";
  // private final String AUTHOR = "Viktor Kolokolov @ TUD2011";

  private Language             lang;
  private Text                 header;
  private SourceCode           code;
  TicksTiming                  defaultTiming;

  public void init() {
    lang = new AnimalScript("Ramer-Douglas-Peucker algorithm [EN]",
        "Viktor Kolokolov", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    PointArray = (String[]) primitives.get("PointArray(X.Y)");
    orthPolyline = (PolylineProperties) props
        .getPropertiesByName("orthPolyline");
    rPolyline = (PolylineProperties) props.getPropertiesByName("rPolyline");
    rCircle = (CircleProperties) props.getPropertiesByName("rCircle");
    Array = (ArrayProperties) props.getPropertiesByName("Array");
    SourceCode = (SourceCodeProperties) props.getPropertiesByName("SourceCode");
    iPolyline = (PolylineProperties) props.getPropertiesByName("iPolyline");
    bPolyline = (PolylineProperties) props.getPropertiesByName("bPolyline");
    iCircle = (CircleProperties) props.getPropertiesByName("iCircle");

    e = (Integer) primitives.get("e");
    p = (Integer) primitives.get("p");
    isRandom = (Boolean) primitives.get("isRandom");

    if (isRandom != true) {
      p = PointArray.length;
    }
    // max 40 points
    if (p > 40) {
      p = 40;
    }
    // epsilon should be at least 1
    if (e < 1) {
      e = 20;
    } else if (e > 150) {
      e = 150;
    }

    gen_animalscript();

    return lang.toString();
  }

  public void gen_animalscript() {
    defaultTiming = new TicksTiming(25);
    lang.setStepMode(true);
    header = gen_header();
    gen_header_box();
    gen_rdp_pc();
    gen_poly_lyne(p);

    rec_calls = 0;
    /**
     * Ramer-Douglas-Peucker: pdp
     */
    pdp(points, 0, points.size() - 1, e);
    // System.out.println(ResultList);
    epilog();
  }

  private Text gen_header() {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    return lang.newText(new Coordinates(20, 20),
        "Ramer-Douglas-Peucker algorithm", "title", null, textProperties);
  }

  private Rect gen_header_box() {
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    return lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "titleFrame",
        null, rectProperties);
  }

  private void gen_poly_lyne(int nPoints) {
    int n_points = nPoints;
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Coordinates(min_X - 5, min_Y - 5), new Coordinates(
        max_X + 5, max_Y + 5), "PlotFrameI", null, rectProperties);

    // scale
    Node[] bPolyline = new Node[2];
    bPolyline[0] = new Coordinates(min_X - 10, min_Y - 4);
    bPolyline[1] = new Coordinates(min_X - 10, min_Y - 4 + e);
    lang.newPolyline(bPolyline, "epsilon", null);

    cProps = new CircleProperties();
    cProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    cProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    cProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    lang.newCircle(new Coordinates(min_X - 10, min_Y - 4), 2, "", null, cProps);
    lang.newCircle(new Coordinates(min_X - 10, min_Y - 4 + e), 2, "", null,
        cProps);

    RectProperties rectProperties2 = new RectProperties();
    rectProperties2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    rectProperties2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProperties2.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Coordinates(min_X - 5, min_Y + (max_Y - min_Y) + 20 - 5),
        new Coordinates(max_X + 5, max_Y + (max_Y - min_Y) + 20 + 5),
        "PlotFrameR", null, rectProperties2);

    /**
     * randomly creates points and save in "points" or uses manually inputed
     * points
     */
    X_Coor.clear();
    points.clear();
    if (isRandom == true) {
      Random rand = new Random();
      int c_tmp = 0;
      while (c_tmp != n_points) {
        int x_rand = rand.nextInt(max_X - min_X + 1) + min_X;
        int y_rand = rand.nextInt(max_Y - min_Y + 1) + min_Y;
        if (!X_Coor.contains(x_rand)) {
          X_Coor.add(x_rand);
          // Circle C_x = lang.newCircle(new Coordinates(x_rand, y_rand),
          // radius, "", null,cProps);
          // CircleList.add(C_x);
          points.add(new Point(x_rand, y_rand));
          c_tmp++;
        }
      }
    } else {
      String t = "";
      String xtmpstr = "";
      String ytmpstr = "";
      int xtmpint;
      int ytmpint;
      int l = PointArray.length;
      for (int i = 0; i < l; i++) {
        t = PointArray[i];
        int index = t.indexOf('.', 0);
        xtmpstr = t.substring(0, index);
        ytmpstr = t.substring(index + 1, t.length());
        xtmpint = Integer.parseInt(xtmpstr);
        ytmpint = Integer.parseInt(ytmpstr);
        if (!X_Coor.contains(xtmpint) && xtmpint > min_X && xtmpint < max_X
            && ytmpint > min_Y && ytmpint < max_Y) {
          X_Coor.add(xtmpint);
          points.add(new Point(xtmpint, ytmpint));
        } else {
          Random rand = new Random();
          int x_rand = rand.nextInt(max_X - min_X + 1) + min_X;
          int y_rand = rand.nextInt(max_Y - min_Y + 1) + min_Y;
          X_Coor.add(x_rand);
          points.add(new Point(x_rand, y_rand));
        }
      }
    }

    /**
     * sorts the points by the x coordinates
     */
    Collections.sort(points, new PointCompare());

    /**
     * create circles at a given point and save in "CircleList"
     */
    CircleList.clear();
    int c_tmp = 0;
    while (c_tmp != n_points) {
      Point point_tmp = points.get(c_tmp);
      Circle C_x = lang.newCircle(new Coordinates(point_tmp.x, point_tmp.y),
          radius, "", null, iCircle);
      CircleList.add(C_x);
      c_tmp++;
    }

    /**
     * draws the curve
     */
    n_points = points.size();
    int n_points_counter = 0;
    Node[] bucketPolyline = new Node[2];
    while (n_points != 1) {
      Point point_curr = points.get(n_points_counter); // get the n'th point
      Point point_next = points.get(n_points_counter + 1); // get the n+1'th
                                                           // point

      bucketPolyline[0] = new Coordinates(point_curr.x, point_curr.y);
      bucketPolyline[1] = new Coordinates(point_next.x, point_next.y);
      lang.newPolyline(bucketPolyline, "pl" + (n_points_counter + 1), null,
          iPolyline);
      n_points_counter++;
      n_points--;
    }

    lang.nextStep();

    /**
     * Add the first and the last points to the ResultList
     */
    ResultList.clear();
    ResultList.add(points.get(0));
    ResultList.add(points.get(points.size() - 1));

    Circle c_r = lang.newCircle(new Coordinates(points.get(0).x,
        points.get(0).y), radius, "", null, rCircle);
    c_r.moveBy(null, 0, (max_Y - min_Y) + 20, null, new TicksTiming(50));
    c_r = lang.newCircle(new Coordinates(points.get(points.size() - 1).x,
        points.get(points.size() - 1).y), radius, "", null, rCircle);
    c_r.moveBy(null, 0, (max_Y - min_Y) + 20, null, new TicksTiming(50));

    TextProperties textProps = new TextProperties();
    dmaxText = lang.newText(new Coordinates(15, 350),
        "max_orthogonal_distance: dmax = 0.0", "epsilon", null, textProps);
    indexText = lang.newText(new Coordinates(15, 370), "index = 0", "epsilon",
        null, textProps);

    code.highlight(2);
    lang.nextStep();

    create_p_array();

    code.unhighlight(2);
    code.highlight(3);
    lang.nextStep();

  }

  private void create_p_array() {

    ArrayMarkerProperties amp = new ArrayMarkerProperties();

    String[] point_table = new String[points.size()];
    int i = points.size();
    int j = 0;
    while (j != i) {
      point_table[j] = "P" + (j + 1);
      j++;
    }
    r = lang.newStringArray(new Coordinates(15, 475), point_table,
        "point_table_h", null, Array);

    ArrayMarkerList.clear();
    i = points.size();
    j = 0;
    while (j != i) {
      ArrayMarker m_x = lang.newArrayMarker(r, j, "point_table", null, amp);
      m_x.hide();
      ArrayMarkerList.add(m_x);
      j++;
    }
    m_start = ArrayMarkerList.get(0);
    m_start.show();
    m_end = ArrayMarkerList.get(points.size() - 1);
    m_end.show();
  }

  private ArrayList<Point> pdp(ArrayList<Point> points2, int start, int end,
      int epsilon) {
    rec_calls++;
    double dmax = 0;
    int index = 0;
    int i = start;
    Point point_last = points.get(end); // get the last point
    Point point_start = points.get(i); // get the start point

    bucketPolylineSE[0] = new Coordinates(point_start.x, point_start.y);
    bucketPolylineSE[1] = new Coordinates(point_last.x, point_last.y);
    poly_curr = lang.newPolyline(bucketPolylineSE, "pl" + (i + 1), null,
        bPolyline);
    poly_prev = poly_curr;
    while (i != end - 1) {
      double d = 0;
      Point point_next = points.get(i + 1); // get the i+1'th point
      d = orth_distance(point_next.x, point_next.y, point_start.x,
          point_start.y, point_last.x, point_last.y);
      // System.out.println(String.format("Distance from " + point_next.x + ","
      // + point_next.y +
      // " to the line " + point_start.x + "," + point_start.y
      // + "-" + point_last.x + "," + point_last.y + ": %f",
      // distanceToSegment(point_next.x, point_next.y, point_start.x,
      // point_start.y, point_last.x, point_last.y)));
      if (d > dmax) {
        orth_X_max = orth_X;
        orth_Y_max = orth_Y;
        index = i;
        dmax = d;
      }
      i++;
    }
    index++;
    // System.out.println("dmax: " + dmax);
    // System.out.println("i: " + index);

    // If max. distance is greater than epsilon - recursively simplify
    dmaxText.setText("max_orthogonal_distance: dmax = " + dmax, null, null);
    code.unhighlight(3);
    code.unhighlight(9);
    code.unhighlight(10);
    code.highlight(5);
    lang.nextStep();

    if (dmax != 0.0) {
      indexText.setText("index = " + (index + 1), null, null);
      Circle c = (Circle) this.CircleList.get(index);
      ch = lang.newCircle(
          new Coordinates(points.get(index).x, points.get(index).y), radius,
          "", null, rCircle);
      m_od_max = ArrayMarkerList.get(index);
      m_od_max.show();
      r.highlightCell(index, null, null);
      c_orth = lang.newCircle(new Coordinates(orth_X_max, orth_Y_max), radius,
          "", null, iCircle);
      c_orth.changeColor("Color", new Color(255, 0, 0), null, null);
      c_orth.changeColor("fillColor", new Color(255, 0, 0), null, null);
      bucketPolylineSE[0] = c.getCenter();
      bucketPolylineSE[1] = new Coordinates(orth_X_max, orth_Y_max);
      poly_orth = lang.newPolyline(bucketPolylineSE, "pl" + (i + 1), null,
          orthPolyline);
    } else {
      indexText.setText("index = none", null, null);
    }

    code.unhighlight(5);
    code.highlight(6);
    lang.nextStep();

    code.unhighlight(6);
    code.highlight(7);
    lang.nextStep();
    if (dmax >= epsilon) {
      ResultList.add(points.get(index));
      Circle c_r = lang.newCircle(
          new Coordinates(points.get(index).x, points.get(index).y), radius,
          "", null, rCircle);
      c_r.moveBy(null, 0, (max_Y - min_Y) + 20, null, new TicksTiming(50));
      code.unhighlight(7);
      code.highlight(8);
      lang.nextStep();

      // System.out.println(ResultList);
      r.unhighlightCell(index, null, null);
      m_od_max.hide();
      code.unhighlight(8);
      code.highlight(9);
      m_start.move(start, null, defaultTiming);
      m_end.move(index, null, defaultTiming);
      lang.nextStep();

      poly_orth.hide();
      c_orth.hide();
      poly_prev.hide();
      ch.hide();
      // Recursive call - left
      pdp(points, start, index, epsilon);
      code.unhighlight(9);
      code.highlight(10);
      m_start.move(index, null, defaultTiming);
      m_end.move(end, null, defaultTiming);
      lang.nextStep();
      // Recursive call - right
      pdp(points, index, end, epsilon);
    } else {
      r.unhighlightCell(index, null, null);
      m_od_max.hide();
      code.unhighlight(7);
      code.highlight(12);
      lang.nextStep();

      poly_orth.hide();
      c_orth.hide();
      poly_prev.hide();
      ch.hide();
      code.unhighlight(12);
    }
    // Return the result
    return ResultList;
  }

  /**
   * computes orthogonal distance
   */
  public double orth_distance(double x3, double y3, double x1, double y1,
      double x2, double y2) {
    final Point2D p3 = new Point2D.Double(x3, y3);
    final Point2D p1 = new Point2D.Double(x1, y1);
    final Point2D p2 = new Point2D.Double(x2, y2);
    return orth_distance(p1, p2, p3);
  }

  /**
   * returns the distance of p3 to the segment defined by p1 and p2
   */
  public double orth_distance(Point2D p1, Point2D p2, Point2D p3) {

    final double xDelta = p2.getX() - p1.getX();
    final double yDelta = p2.getY() - p1.getY();

    if ((xDelta == 0) && (yDelta == 0)) {
      throw new IllegalArgumentException("p1 and p2 cannot be the same point");
    }

    final double u = ((p3.getX() - p1.getX()) * xDelta + (p3.getY() - p1.getY())
        * yDelta)
        / (xDelta * xDelta + yDelta * yDelta);

    final Point2D closestPoint;
    if (u < 0) {
      closestPoint = p1;
    } else if (u > 1) {
      closestPoint = p2;
    } else {
      closestPoint = new Point2D.Double(p1.getX() + u * xDelta, p1.getY() + u
          * yDelta);
      orth_X = (int) closestPoint.getX();
      orth_Y = (int) closestPoint.getY();
    }
    return closestPoint.distance(p3);
  }

  public class PointCompare implements Comparator<Point> {
    public int compare(final Point a, final Point b) {
      if (a.x < b.x) {
        return -1;
      } else if (a.x > b.x) {
        return 1;
      } else {
        return 0;
      }
    }
  }

  // creates pseudo-code for rdp.
  private void gen_rdp_pc() {
    // create prolog: coordinates, name, display options, default properties
    // first, set the visual properties for the source code
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // now, create the prolog entity
    SourceCode prolog = lang.newSourceCode(new Coordinates(15, 40), "prolog",
        null, scProps);

    // add the lines to the prolog object.
    // line, name, indentation, display delay
    prolog
        .addCodeLine(
            "The Ramer-Douglas-Peucker algorithm is an algorithm for reducing the number",
            null, 0, null); // 0
    prolog.addCodeLine(
        "of points in a curve that is approximated by a series of points.",
        null, 0, null); // 1
    prolog
        .addCodeLine(
            "The initial form of the algorithm was independently suggested in 1972",
            null, 0, null); // 2
    prolog.addCodeLine(
        "by Urs Ramer and 1973 by David Douglas and Thomas Peucker.", null, 0,
        null); // 3
    prolog.addCodeLine(" ", null, 0, null); // 4
    prolog
        .addCodeLine(
            "The purpose of the algorithm is, given a curve composed of line segments,",
            null, 0, null); // 5
    prolog
        .addCodeLine(
            "to find a similar curve with fewer points. The algorithm defines 'dissimilarity'",
            null, 0, null); // 6
    prolog
        .addCodeLine(
            "based on the maximum distance between the original curve and the simplified curve.",
            null, 0, null); // 6
    prolog
        .addCodeLine(
            "The simplified curve consists of a subset of the points that defined the original curve.",
            null, 0, null); // 6
    prolog.addCodeLine(" ", null, 0, null); // 6
    prolog
        .addCodeLine(
            "The Ramer-Douglas-Peucker algorithm finds its application within processing of ",
            null, 0, null); // 6
    prolog
        .addCodeLine(
            "vector graphics and in cartographic generalization. One can find it also in robotics.",
            null, 0, null); // 6

    // create code entity
    code = lang
        .newSourceCode(new Coordinates(15, 40), "code", null, SourceCode);
    code.addCodeLine("Input: points=" + p + "; " + "epsilon=" + e + "; ", null,
        0, null);
    code.addCodeLine(" ", null, 1, null);
    code.addCodeLine("ResultList.add(PointList[1], PointList[end]);", null, 0,
        null);
    code.addCodeLine("pdp(PointList[],1,(PointList[].size()),epsilon);", null,
        0, null);
    code.addCodeLine("function rdp(lPointList[], start, end, l_epsilon) {",
        null, 0, null);
    code.addCodeLine(
        "dmax = OrthogonalMaxDistance(PointList[start+1...end-1]);", null, 1,
        null);
    code.addCodeLine("index = OrthogonalMaxDistancePointIndex();", null, 1,
        null);
    code.addCodeLine("if (dmax >= epsilon){", null, 1, null);
    code.addCodeLine(
        "ResultList.add(lPointList.get(index)); //add a split point", null, 2,
        null);
    code.addCodeLine(
        "pdp(lPointList[],start,index,l_epsilon); //go recursively left", null,
        2, null);
    code.addCodeLine(
        "pdp(lPointList[],index,end,l_epsilon); //go recursively right", null,
        2, null);
    code.addCodeLine("}else{", null, 1, null);
    code.addCodeLine("//dmax < l_epsilon: no new 'dissimilar' points to add",
        null, 2, null);
    code.addCodeLine("}endif", null, 1, null);
    code.addCodeLine("return ResultList[];", null, 1, null);
    code.addCodeLine("}", null, 0, null);

    SourceCodeProperties scPROPERTIES = new SourceCodeProperties();
    scPROPERTIES.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scPROPERTIES.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    scPROPERTIES
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scPROPERTIES.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode func_desc = lang.newSourceCode(new Coordinates(15, 245),
        "func_desc", null, scPROPERTIES);
    func_desc.addCodeLine("Remarks:", null, 0, null);
    func_desc.addCodeLine(
        "The algorithm makes use of the following functions:", null, 0, null);
    func_desc
        .addCodeLine(
            "OrthogonalMaxDistance() - computes orthogonal distance from a point to a line segment;",
            null, 0, null);
    func_desc
        .addCodeLine(
            "OrthogonalMaxDistancePointIndex() - retrieves the index and coordinates of the furtherst point.",
            null, 0, null);
    func_desc.addCodeLine("", null, 0, null);
    func_desc
        .addCodeLine(
            "There are different approaches how to compute orthogonal distances: e.g. least squares or least circles for 2D.",
            null, 0, null);
    func_desc.addCodeLine("", null, 0, null);
    func_desc
        .addCodeLine(
            "Practically it might be effective to combine both functions or even to embedd OrthogonalMaxDistancePointIndex()",
            null, 0, null);
    func_desc
        .addCodeLine(
            "directly within OrthogonalMaxDistance(), where OrthogonalMaxDistance() is executed against the current set",
            null, 0, null);
    func_desc
        .addCodeLine(
            "of points, and goes through every point in the set located within the given line segment computing their",
            null, 0, null);
    func_desc
        .addCodeLine(
            "orthogonal distances; here OrthogonalMaxDistancePointIndex() keeps track of the most distant point",
            null, 0, null);
    func_desc.addCodeLine("and retrieves its index and coordinates.", null, 0,
        null);

    func_desc.highlight(0);
    code.hide();
    lang.nextStep();

    code.show();
    func_desc.hide();
    prolog.hide();

    lang.nextStep();
  }

  // finalizes the animation by presenting results
  private void epilog() {

    code.highlight(14);
    /**
     * sorts the points in "ResultList" by the x coordinates
     */
    Collections.sort(ResultList, new PointCompare());

    /**
     * show the resulting line from ResultList
     */
    int n_points = ResultList.size();
    int n_points_counter = 0;
    Node[] bucketPolyline = new Node[2];
    while (n_points != 1) {
      Point point_curr = ResultList.get(n_points_counter); // get the n'th point
      Point point_next = ResultList.get(n_points_counter + 1); // get the n+1'th
                                                               // point

      bucketPolyline[0] = new Coordinates(point_curr.x, point_curr.y
          + (max_Y - min_Y) + 20);
      bucketPolyline[1] = new Coordinates(point_next.x, point_next.y
          + (max_Y - min_Y) + 20);
      lang.newPolyline(bucketPolyline, "r_pl" + (n_points_counter + 1), null,
          rPolyline);
      n_points_counter++;
      n_points--;
    }

    lang.nextStep();

    int i = ResultList.size();
    int j = points.size();
    TextProperties textProps = new TextProperties();
    Result = lang.newText(new Coordinates(min_X, 410), "Result", "epsilon",
        null, textProps);
    Result.setText("Result: the initial line has been reduced from " + j
        + " to " + (j - (j - i)) + " points.", null, null);
    Text rec_calls_t = lang.newText(new Coordinates(min_X, 425), "rec_calls",
        "rec_calls", null, textProps);
    rec_calls_t
        .setText(
            "There were "
                + (rec_calls - 1)
                + " recursive calls of rdp_func performed to process the initial line!",
            null, null);

    System.out.println(points);
    code.unhighlight(14);

    lang.nextStep();

  }

  private String desc        = "The Ramer-Douglas-Peucker algorithm is an algorithm for reducing the number\n"
                                 + "of points in a curve that is approximated by a series of points.\n"
                                 + "The initial form of the algorithm was independently suggested in 1972\n"
                                 + "by Urs Ramer and 1973 by David Douglas and Thomas Peucker.\n"
                                 + " \n"
                                 + "The purpose of the algorithm is, given a curve composed of line segments,\n"
                                 + "to find a similar curve with fewer points. The algorithm defines 'dissimilarity'\n"
                                 + "based on the maximum distance between the original curve and the simplified curve.\n"
                                 + "The simplified curve consists of a subset of the points that defined the original curve."
                                 + " \n"
                                 + "The Ramer-Douglas-Peucker algorithm finds its application within processing of\n"
                                 + "vector graphics and in cartographic generalization. One can find it also in robotics.\n"
                                 + " \n"
                                 + "NOTE: for inputing points in manual mode one should be aware of valid ranges for X and Y;\n"
                                 + "these are as follows: X from (500...950), Y from (70...220).\n"
                                 + "If the coordinates of some points happen to be out of this area - these will be replaced with proper ones automatically.";

  private String pseudo_code = "ResultList.add(PointList[1], PointList[end]);\n"
                                 + "pdp(PointList[],1,(PointList[].size()),epsilon);\n"
                                 + "function rdp(lPointList[], start, end, l_epsilon){\n"
                                 + "\t dmax = OrthogonalMaxDistance(PointList[start+1...end-1]);\n"
                                 + "\t index = OrthogonalMaxDistancePointIndex();\n"
                                 + "\t if (dmax >= epsilon){\n"
                                 + "\t\t ResultList.add(lPointList.get(index)); //add a split point\n"
                                 + "\t\t pdp(lPointList[],start,index,l_epsilon); //go recursively left\n"
                                 + "\t\t pdp(lPointList[],index,end,l_epsilon); //go recursively right\n"
                                 + "\t }else{\n"
                                 + "\t\t //dmax < l_epsilon: no new 'dissimilar' points to add\n"
                                 + "\t }endif\n"
                                 + "\t return ResultList[];\n"
                                 + " }";

  @Override
  public String getAlgorithmName() {
    return "Ramer-Douglas-Peucker";
  }

  @Override
  public String getName() {
    return "Ramer-Douglas-Peucker algorithm";
  }

  @Override
  public String getAnimationAuthor() {
    return "Viktor Kolokolov"; // @ TUD201";;
  }

  @Override
  public String getCodeExample() {
    return pseudo_code;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return desc;
  }

  @Override
  public String getFileExtension() {
    return ".asu";
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