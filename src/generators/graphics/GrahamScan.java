package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class GrahamScan implements Generator {
  private Language lang;
  private int[][]  points;
  boolean          questions    = true;
  Color            stLineColor  = Color.BLACK;
  Color            mLineColor   = Color.GREEN;
  Color            stPointColor = Color.BLACK;
  Color            mPointColor  = Color.RED;

  public void init() {
    lang = new AnimalScript("GrahamScan[EN]", "Ivaylo Petkov", 800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    mLineColor = (Color) primitives.get("highlighted_Line_Color");
    stLineColor = (Color) primitives.get("standart_Line_Color");
    stPointColor = (Color) primitives.get("standart_Point_Color");
    mPointColor = (Color) primitives.get("highlighted_Point_Color");

    points = (int[][]) primitives.get("points");
    questions = (Boolean) primitives.get("questions");
    GrahamScan_Alg grahamScan = new GrahamScan_Alg(lang);
    List<GrahamScan_Alg.Point> listPoints = new ArrayList<GrahamScan_Alg.Point>();
    if (points.length >= 0)
      for (int i = 0; i < points[0].length; i++)
        listPoints.add(grahamScan.new Point(points[0][i], points[1][i]));
    grahamScan.grahamScan(listPoints);
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Graham Scan";
  }

  public String getAlgorithmName() {
    return "Graham Scan";
  }

  public String getAnimationAuthor() {
    return "Ivaylo Petkov";
  }

  public String getDescription() {
    return "  <p>Graham's scan is a method of computing the convex hull of a finite set of points "
        + "\n"
        + "  in the plane with time complexity O(n log n). It is named after Ronald Graham, "
        + "\n"
        + "  who published the original algorithm in 1972. The algorithm finds all vertices "
        + "\n"
        + "  of the convex hull ordered along its boundary.</p>"
        + "\n"
        + "   "
        + "\n"
        + "   "
        + "\n"
        + "  <p>The first step in this algorithm is to find the point with the lowest y-coordinate. If"
        + "\n"
        + "  the lowest y-coordinate exists in more than one point in the set, the point with the"
        + "\n"
        + "  lowest x-coordinate out of the candidates should be chosen. Next, the set of points"
        + "\n"
        + "  must be sorted in increasing order of the angle they and the point P make with"
        + "\n"
        + "  the x-axis.</p>"
        + "\n"
        + "  <p>The algorithm proceeds by considering each of the points in the sorted array in"
        + "\n"
        + "  sequence. For each point, it is determined whether moving from the two previously"
        + "\n"
        + "  considered points to this point is a 'left turn' or a 'right turn'. If it is a 'right turn', this"
        + "\n"
        + "  means that the second-to-last point is not part of the convex hull and should be"
        + "\n"
        + "  removed from consideration. This process is continued for as long as the set of"
        + "\n"
        + "  the last three points is a 'right turn'. As soon as a 'left turn' is encountered, the"
        + "\n"
        + "  algorithm moves on to the next point in the sorted array.\" to \"description\""
        + "\n"
        + "  </p>"
        + "\n"
        + "  <p>                               Source: Wikipedia (http://en.wikipedia.org/wiki/Graham_scan)\" to \"description\"</p>";
  }

  public String getCodeExample() {
    return "	private static int selectPivotElement(List<Point> points){"
        + "\n"
        + "		int minIndex=0;"
        + "\n"
        + "		for(Point point:points)"
        + "\n"
        + "			if(point.getY()<points.get(minIndex).getY())         //if y  of point i is less than  y of point minIndex"
        + "\n"
        + "				minIndex=points.indexOf(point);                  // set minY index ot i"
        + "\n"
        + "			else"
        + "\n"
        + "				if(point.getY()==points.get(minIndex).getY())    //if both points have equal y"
        + "\n"
        + "					if(point.getX()<points.get(minIndex).getX()) //select the one with lower x"
        + "\n"
        + "						minIndex=points.indexOf(point);			"
        + "\n"
        + "		"
        + "\n"
        + "		return minIndex;"
        + "\n"
        + "	}"
        + "\n"
        + "	"
        + "\n"
        + "	"
        + "\n"
        + "	private static List<Point> sortPoints(Point pivot,List<Point> sorted){"
        + "\n"
        + "		for(int i=1; i<sorted.size()-1;i++){"
        + "\n"
        + "			int minIndex=i;"
        + "\n"
        + "			for(int j=i+1;j<sorted.size();j++)"
        + "\n"
        + "				if(findAngle(pivot, sorted.get(j))<findAngle(pivot, sorted.get(minIndex)))"
        + "\n"
        + "					minIndex=j;"
        + "\n"
        + "			Collections.swap(sorted, i, minIndex);"
        + "\n"
        + "		}"
        + "\n"
        + "		return sorted;"
        + "\n"
        + "	}"
        + "\n"
        + "	"
        + "\n"
        + "\n"
        + "	public static double findAngle(Point point1, Point point2)"
        + "\n"
        + "	{"
        + "\n"
        + "	    double deltaX=(double)(point2.getX()-point1.getX());"
        + "\n"
        + "	    double deltaY=(double)(point2.getY()-point1.getY());"
        + "\n"
        + "	    double angle;"
        + "\n"
        + "	 "
        + "\n"
        + "	    if (deltaX==0 && deltaY==0)"
        + "\n"
        + "	        return 0;"
        + "\n"
        + "	   "
        + "\n"
        + "	    angle=Math.atan2(deltaY,deltaX)*57.295779513082;"
        + "\n"
        + "	  "
        + "\n"
        + "	   if (angle < 0)"
        + "\n"
        + "	 	angle += 360.;"
        + "\n"
        + "\n"
        + "	    return angle;"
        + "\n"
        + "	}"
        + "\n"
        + "	private static boolean isLeftTurn(Point point1, Point point2, Point point3){"
        + "\n"
        + "		double alpha=findAngle(point1, point2);"
        + "\n"
        + "		double beta=findAngle(point2, point3);"
        + "\n"
        + "		if(beta>=alpha && beta< 360+alpha)"
        + "\n"
        + "			return true;"
        + "\n"
        + "		else "
        + "\n"
        + "			return false;"
        + "\n"
        + "	}"
        + "\n"
        + "	"
        + "\n"
        + "	private static List<Point> grahamScan(List<Point> points){"
        + "\n"
        + "		int pivotPointIndex=selectPivotElement(points);		// select pivot element with lowest Y"
        + "\n"
        + "		"
        + "\n"
        + "		List<Point> sortedPoints =new ArrayList<Point>(points);"
        + "\n"
        + "		Collections.swap(sortedPoints,pivotPointIndex,0);							// swap the the first point and the pivot point"
        + "\n"
        + "		"
        + "\n"
        + "		sortedPoints = sortPoints(sortedPoints.get(0),sortedPoints); 	// points are sorted in increasing order of the angle"
        + "\n"
        + "																		// they and the PIVOT point make with the x-axis."
        + "\n"
        + "														"
        + "\n"
        + "		List<Point> convexCandidates=new ArrayList<Point>();"
        + "\n"
        + "		convexCandidates.add(sortedPoints.get(0));"
        + "\n"
        + "		convexCandidates.add(sortedPoints.get(1));			// Place the first 2 points of the sorted points array in the convex candidates"
        + "\n"
        + "		int i=2;"
        + "\n"
        + "		while(i<sortedPoints.size()){"
        + "\n"
        + "			Point candidatePoint1=convexCandidates.get(convexCandidates.size()-2);"
        + "\n"
        + "			Point candidatePoint2=convexCandidates.get(convexCandidates.size()-1);"
        + "\n"
        + "			Point newCandidatePoint=sortedPoints.get(i);"
        + "\n"
        + "			"
        + "\n"
        + "			if(isLeftTurn(candidatePoint1,candidatePoint2,newCandidatePoint)){	// if left turn"
        + "\n"
        + "				convexCandidates.add(newCandidatePoint);						// add the new candidate to the convex candidate array"
        + "\n"
        + "				i++;"
        + "\n"
        + "			} else {															// if right turn"
        + "\n"
        + "				convexCandidates.remove(convexCandidates.size()-1);							// remove the last point of the candidate array"
        + "\n" + "			}" + "\n" + "		}" + "\n" + "		return convexCandidates;"
        + "\n" + "	}";
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
    return Generator.JAVA_OUTPUT;
  }

  public class GrahamScan_Alg {

    Language               language;

    // Animation Elements
    RectProperties         standadrRectProp;
    SourceCodeProperties   standartSourceCodeProperties;

    Rect                   headerRect;
    Text                   headerText;

    Rect                   descriptionRect;
    SourceCode             description;

    Rect                   closingRect;
    SourceCode             closing;

    Rect                   mainSCRect;
    SourceCode             mainSourceCode;
    Rect                   selectPivotRect;
    SourceCode             selectPivotCode;
    Rect                   sortPointsRect;
    SourceCode             sortPointsCode;

    Square                 coordSquare;
    Polyline               xAxis;
    Polyline               yAxis;

    HashMap<Point, Circle> coordMap;
    Stack<Polyline>        lineStack;

    Text                   pointsLabel;
    Text                   sortedLabel;
    Text                   convexCandidatesLabel;
    GridTable              convexCandidatesGrid;
    GridTable              pointsGrid;
    GridTable              sortedPointsGrid;
    private int            lineIndex = 0;
    PolylineProperties     redLineProp;
    PolylineProperties     blackLineProp;
    PolylineProperties     greenLineProp;

    // constructor
    public GrahamScan_Alg(Language language) {
      this.language = language;
      this.language.setStepMode(true);
      standadrRectProp = new RectProperties();
      standadrRectProp.set("fillColor", Color.LIGHT_GRAY);
      standadrRectProp.set("color", Color.BLACK);
      standadrRectProp.set("filled", true);
      standadrRectProp.set("depth", 2);
      standartSourceCodeProperties = new SourceCodeProperties();
      standartSourceCodeProperties.set("highlightColor", Color.black);
      standartSourceCodeProperties.set("contextColor", Color.blue);

      standartSourceCodeProperties.set("color", Color.gray);
      standartSourceCodeProperties.set("font", new Font("SansSerif",
          Font.PLAIN, 12));
      standartSourceCodeProperties.set("depth", 1);

      redLineProp = new PolylineProperties();
      redLineProp.set("color", mLineColor);
      blackLineProp = new PolylineProperties();
      blackLineProp.set("color", stLineColor);
      greenLineProp = new PolylineProperties();
      greenLineProp.set("color", Color.green);
    }

    // circleSeg
    public class AngleVisualization {
      private Circle   circle;
      private Polyline line1;
      private Polyline line2;
      private int      index;

      AngleVisualization(int x, int y, boolean leftTurn, int index) {
        this.index = index;

        CircleProperties circleProp = new CircleProperties();
        if (leftTurn) {
          circleProp.set("color", Color.green);
          createLeftLines(x, y);
        } else {
          circleProp.set("color", Color.red);
          createRigthLines(x, y);
        }
        circleProp.set("filled", false);

        circle = language.newCircle(getNode(mapXCoord(x), mapYCoord(y)), 10,
            "cirlce" + index, null, circleProp);
      }

      private void createLeftLines(int x, int y) {
        line1 = language.newPolyline(
            convertToNodes(mapXCoord(x + 10), mapYCoord(y), mapXCoord(x + 5),
                mapYCoord(y - 5)), "line1" + index, null, greenLineProp);
        line2 = language.newPolyline(
            convertToNodes(mapXCoord(x + 10), mapYCoord(y), mapXCoord(x + 15),
                mapYCoord(y - 5)), "line1" + index, null, greenLineProp);
      }

      private void createRigthLines(int x, int y) {
        line1 = language.newPolyline(
            convertToNodes(mapXCoord(x + 10), mapYCoord(y), mapXCoord(x + 5),
                mapYCoord(y + 5)), "line1" + index, null, redLineProp);
        line2 = language.newPolyline(
            convertToNodes(mapXCoord(x + 10), mapYCoord(y), mapXCoord(x + 15),
                mapYCoord(y + 5)), "line1" + index, null, redLineProp);
      }

      public void hide() {
        circle.hide();
        line1.hide();
        line2.hide();
      }
    }

    // class creating and managing grid table due to the incomplete
    // implementation of grid
    private class GridTable {
      private Language   language;
      private String[][] data;
      private String     name;
      private int        offsetX;
      private int        offsetY;
      private Color      borderColor   = Color.black;
      private Color      color         = Color.black;
      private Color      fillColor     = Color.gray;
      private Color      elementColor  = Color.green;
      private Color      elemHighlight = Color.yellow;
      private int        cellWidth     = 30;
      private int        cellHeight    = 30;
      private boolean    fixedCellSize = true;
      private String     font          = "font SansSerif size 20";
      private String     offsetElem;

      public GridTable(Language language, String[][] data, String name,
          int offsetX, int offsetY, String offsetElem) {
        super();
        this.language = language;
        this.data = data;
        this.name = name;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetElem = offsetElem;
        createGrid();
      }

      // setGridValue "points[0][0]" "x"
      // setGridValue "points[1][0]" "y"

      public GridTable(Language language, int size, String name, int offsetX,
          int offsetY, String offsetElem) {
        this.language = language;
        // this.data = data;
        this.name = name;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetElem = offsetElem;
        createEmptyGrid(size + 1);
      }

      public void setPointValues(int index, Point point) {
        setGridValue(0, index + 1, "" + point.x);
        setGridValue(1, index + 1, "" + point.y);
      }

      public void setGridValue(int x, int y, String value) {
        language.addLine("setGridValue \"" + name + "[" + x + "]" + "[" + y
            + "]\" \"" + value + "\"");
      }

      private void fillGrid() {
        for (int i = 0; i < data[0].length; i++) {
          setGridValue(0, i, data[0][i]);
          setGridValue(1, i, data[1][i]);
        }
      }

      private void createGrid() {
        String createLine = "grid \"" + name + "\" offset (" + offsetX + ","
            + offsetY + ") " + " from \"" + offsetElem + "\" SW " + "lines "
            + data.length + " columns " + data[0].length
            + " style table cellWidth " + cellWidth + " cellHeight "
            + cellHeight;
        if (fixedCellSize)
          createLine += " fixedCellSize ";
        createLine += " color " + convertColor(color) + " borderColor "
            + convertColor(borderColor) + " fillColor "
            + convertColor(fillColor) + " elementColor "
            + convertColor(elementColor) + " elemHighlight "
            + convertColor(elemHighlight) + " " + font;
        language.addLine(createLine);
        fillGrid();
      }

      private void createEmptyGrid(int size) {
        String createLine = "grid \"" + name + "\" offset (" + offsetX + ","
            + offsetY + ") " + " from \"" + offsetElem + "\" SW "
            + "lines 2 columns " + size + " style table cellWidth " + cellWidth
            + " cellHeight " + cellHeight;
        if (fixedCellSize)
          createLine += " fixedCellSize ";
        createLine += " color " + convertColor(color) + " borderColor "
            + convertColor(borderColor) + " fillColor "
            + convertColor(fillColor) + " elementColor "
            + convertColor(elementColor) + " elemHighlight "
            + convertColor(elemHighlight) + " " + font;
        language.addLine(createLine);
        setGridValue(0, 0, "x");
        setGridValue(1, 0, "y");
        // fillGrid();
      }

      private String convertColor(Color color) {
        String sColor = "(" + color.getRed() + "," + color.getGreen() + ","
            + color.getBlue() + ")";
        return sColor;
      }

      public void highlight(int elemIndex) {
        highlightGridCell(0, elemIndex + 1);
        highlightGridCell(1, elemIndex + 1);
      }

      public void unhighlight(int elemIndex) {
        unhighlightGridCell(0, elemIndex + 1);
        unhighlightGridCell(1, elemIndex + 1);
      }

      public void highlightGridCell(int x, int y) {
        language.addLine("highlightGridCell \"" + name + "[" + x + "]" + "["
            + y + "]\" ");
      }

      public void unhighlightGridCell(int x, int y) {
        language.addLine("unhighlightGridCell \"" + name + "[" + x + "]" + "["
            + y + "]\" ");
      }

      public void highlightValue(int elemIndex) {
        setGridColor(0, elemIndex + 1, Color.green);
        setGridColor(1, elemIndex + 1, Color.green);
      }

      public void unhighlightValue(int elemIndex) {
        setGridColor(0, elemIndex + 1, Color.black);
        setGridColor(1, elemIndex + 1, Color.black);
      }

      public void setGridColor(int x, int y, Color color) {
        language.addLine("setGridColor \"" + name + "[" + x + "]" + "[" + y
            + "]\" textColor " + convertColor(color));
      }

      public void hide() {
        language.addLine("hide \"" + name + "\"");
      }

      public void show() {
        language.addLine("show \"" + name + "\"");
      }

    }

    // graphic creation
    private void hideAll() {
      sortedPointsGrid.hide();
      descriptionRect.hide();
      description.hide();

      mainSCRect.hide();
      mainSourceCode.hide();
      selectPivotRect.hide();
      selectPivotCode.hide();
      sortPointsRect.hide();
      sortPointsCode.hide();

      coordSquare.hide();
      xAxis.hide();
      yAxis.hide();

      // HashMap<Point, Circle> coordMap;
      for (Point point : coordMap.keySet())
        coordMap.get(point).hide();
      while (!lineStack.isEmpty())
        lineStack.pop().hide();

      pointsLabel.hide();
      sortedLabel.hide();
      convexCandidatesLabel.hide();
      convexCandidatesGrid.hide();
      pointsGrid.hide();
    }

    private void createHeader() {

      headerRect = language.newRect(new Coordinates(5, 0), new Coordinates(148,
          36), "headerRectangle", null, standadrRectProp);
      // text "header" "Graham Scan" (10,5) color black depth 1 font SansSerif
      // size 20 bold
      TextProperties textProp = new TextProperties();
      textProp.set("color", Color.black);
      textProp.set("font", new Font("SansSerif", Font.BOLD, 20));
      headerText = language.newText(new Coordinates(10, 5), "Graham Scan",
          "headerText", null, textProp);

    }

    private void createClosing() {
      closingRect = language.newRect(new Offset(0, 9, headerRect, "SW"),
          new Offset(557, 482, headerRect, "NW"), "closingRect", null,
          standadrRectProp);

      SourceCodeProperties scProp = new SourceCodeProperties();
      scProp.set("color", Color.black);
      scProp.set("font", new Font("SansSerif", Font.PLAIN, 15));
      scProp.set("depth", 1);
      closing = language.newSourceCode(new Coordinates(15, 50), "closing",
          null, scProp);

      closing
          .addCodeLine(
              "Graham's scan is a method of computing the convex hull of a finite set of points ",
              null, 0, null);
      closing
          .addCodeLine(
              "In this case the sorting algortihm that is used is Selection Sort, it is chosen",
              null, 0, null);
      closing
          .addCodeLine(
              "only for visualization purposes. If an algorithm such as heapsort is selected,",
              null, 0, null);
      closing
          .addCodeLine(
              "the comlexity of the sorting would be O(n log n). While it may seem that the",
              null, 0, null);
      closing
          .addCodeLine(
              "time complexity of the loop is O(n2), because for each point it goes back to",
              null, 0, null);
      closing
          .addCodeLine(
              "check if any of the previous points make a right turn, it is actually O(n),",
              null, 0, null);
      closing
          .addCodeLine(
              "because each point is considered at most twice in some sense. The selection",
              null, 0, null);
      closing
          .addCodeLine(
              "of the lowest-y coordinates takes O(n). Thus the overall complexity is",
              null, 0, null);
      closing
          .addCodeLine(
              "O(n log n), since the time to sort dominates the time to actually compute the",
              null, 0, null);
      closing.addCodeLine("convex hull.", null, 0, null);
      closing.addCodeLine(" ", null, 0, null);
      closing.addCodeLine(" ", null, 0, null);
      closing
          .addCodeLine(
              "                                Source: Wikipedia (http://en.wikipedia.org/wiki/Graham_scan)",
              null, 0, null);
    }

    private void createDescription() {
      descriptionRect = language.newRect(new Offset(0, 9, headerRect, "SW"),
          new Offset(557, 482, headerRect, "NW"), "descriptionRect", null,
          standadrRectProp);

      SourceCodeProperties scProp = new SourceCodeProperties();
      scProp.set("color", Color.black);
      scProp.set("font", new Font("SansSerif", Font.PLAIN, 15));
      scProp.set("depth", 1);
      description = language.newSourceCode(new Coordinates(15, 50),
          "description", null, scProp);

      description
          .addCodeLine(
              "Graham's scan is a method of computing the convex hull of a finite set of points ",
              null, 0, null);
      description
          .addCodeLine(
              "in the plane with time complexity O(n log n). It is named after Ronald Graham, ",
              null, 0, null);
      description
          .addCodeLine(
              "who published the original algorithm in 1972. The algorithm finds all vertices ",
              null, 0, null);
      description.addCodeLine("of the convex hull ordered along its boundary.",
          null, 0, null);
      description.addCodeLine(" ", null, 0, null);
      description.addCodeLine(" ", null, 0, null);
      description
          .addCodeLine(
              "The first step in this algorithm is to find the point with the lowest y-coordinate. If",
              null, 0, null);
      description
          .addCodeLine(
              "the lowest y-coordinate exists in more than one point in the set, the point with the",
              null, 0, null);
      description
          .addCodeLine(
              "lowest x-coordinate out of the candidates should be chosen. Next, the set of points",
              null, 0, null);
      description
          .addCodeLine(
              "must be sorted in increasing order of the angle they and the point P make with",
              null, 0, null);
      description.addCodeLine("the x-axis.", null, 0, null);
      description
          .addCodeLine(
              "The algorithm proceeds by considering each of the points in the sorted array in",
              null, 0, null);
      description
          .addCodeLine(
              "sequence. For each point, it is determined whether moving from the two previously",
              null, 0, null);
      description
          .addCodeLine(
              "considered points to this point is a 'left turn' or a 'right turn'. If it is a 'right turn', this",
              null, 0, null);
      description
          .addCodeLine(
              "means that the second-to-last point is not part of the convex hull and should be",
              null, 0, null);
      description
          .addCodeLine(
              "removed from consideration. This process is continued for as long as the set of",
              null, 0, null);
      description
          .addCodeLine(
              "the last three points is a 'right turn'. As soon as a 'left turn' is encountered, the",
              null, 0, null);
      description.addCodeLine(
          "algorithm moves on to the next point in the sorted array.", null, 0,
          null);
      description.addCodeLine(" ", null, 0, null);
      description.addCodeLine(" ", null, 0, null);
      description
          .addCodeLine(
              "                                Source: Wikipedia (http://en.wikipedia.org/wiki/Graham_scan)",
              null, 0, null);
    }

    private void createMainSourceCode() {
      mainSCRect = language.newRect(new Offset(355, 5, headerRect, "NW"),
          new Offset(820, 375, headerRect, "NW"), "codeRectangle", null,
          standadrRectProp);
      mainSourceCode = language.newSourceCode(new Offset(10, -10, mainSCRect,
          "NW"), "sourceCode", null, standartSourceCodeProperties);

      mainSourceCode.addCodeLine(
          "private static List<Point> grahamScan(List<Point> points){", null,
          0, null);
      mainSourceCode.addCodeLine(
          "List<Point> sortedPoints =new ArrayList<Point>(points);", null, 1,
          null);
      mainSourceCode.addCodeLine(
          "int pivotPointIndex=selectPivotElement(points);", null, 1, null);
      mainSourceCode.addCodeLine("Collections.swap(points,pivotPointIndex,0);",
          null, 1, null);
      mainSourceCode.addCodeLine(
          "List<Point> sortedPoints = sortPoints(points.get(0),points);", null,
          1, null);
      mainSourceCode
          .addCodeLine("List<Point> convexCandidates=new ArrayList<Point>();",
              null, 1, null);
      mainSourceCode.addCodeLine("convexCandidates.add(sortedPoints.get(0));",
          null, 1, null);
      mainSourceCode.addCodeLine("convexCandidates.add(sortedPoints.get(1));",
          null, 1, null);
      mainSourceCode.addCodeLine("int i=2;", null, 1, null);
      mainSourceCode
          .addCodeLine("while(i<sortedPoints.size()){", null, 2, null);
      mainSourceCode
          .addCodeLine(
              "Point candidatePoint1=convexCandidates.get(convexCandidates.size()-2);",
              null, 3, null);
      mainSourceCode
          .addCodeLine(
              "Point candidatePoint2=convexCandidates.get(convexCandidates.size()-1);",
              null, 3, null);
      mainSourceCode.addCodeLine(
          "Point newCandidatePoint=sortedPoints.get(i);", null, 3, null);

      mainSourceCode.addCodeLine(
          "if(isLeftTurn(candidatePoint1,candidatePoint2,newCandidatePoint)){",
          null, 3, null);
      mainSourceCode.addCodeLine("convexCandidates.add(newCandidatePoint);",
          null, 4, null);
      mainSourceCode.addCodeLine("i++;", null, 4, null);
      mainSourceCode.addCodeLine("} else ", null, 3, null);
      mainSourceCode.addCodeLine(
          "convexCandidates.remove(convexCandidates.size()-1);", null, 4, null);
      mainSourceCode.addCodeLine("", null, 3, null);
      mainSourceCode.addCodeLine("}", null, 2, null);
      mainSourceCode.addCodeLine("return convexCandidates;", null, 1, null);
      mainSourceCode.addCodeLine("}", null, 0, null);
    }

    private void createSelectPivotCode() {
      standadrRectProp.set("depth", 1);
      selectPivotRect = language.newRect(new Offset(0, 10, mainSCRect, "SW"),
          new Offset(340, 230, mainSCRect, "SW"), "selectPivotRectangle", null,
          standadrRectProp);
      selectPivotCode = language.newSourceCode(new Offset(10, -10,
          selectPivotRect, "NW"), "selectPivotCode", null,
          standartSourceCodeProperties);
      selectPivotCode.addCodeLine("Selects the point with lowest Y", null, 0,
          null);
      selectPivotCode.addCodeLine("", null, 0, null);
      selectPivotCode.addCodeLine(
          "private static int selectPivotElement(List<Point> points){", null,
          0, null);
      selectPivotCode.addCodeLine("int minIndex=0", null, 1, null);
      ;
      selectPivotCode.addCodeLine("for(Point point:points)", null, 2, null);
      selectPivotCode.addCodeLine(
          "if(point.getY()<points.get(minIndex).getY()) ", null, 3, null);
      selectPivotCode.addCodeLine("minIndex=points.indexOf(point);", null, 4,
          null);
      selectPivotCode.addCodeLine("else", null, 3, null);
      selectPivotCode.addCodeLine(
          "if(point.getY()==points.get(minIndex).getY())", null, 4, null);
      selectPivotCode.addCodeLine(
          "if(point.getX()<points.get(minIndex).getX())", null, 5, null);
      selectPivotCode.addCodeLine("minIndex=points.indexOf(point);	", null, 6,
          null);
      selectPivotCode.addCodeLine("return minIndex;", null, 2, null);
      selectPivotCode.addCodeLine("}", null, 0, null);

    }

    private void createSortPointsCode() {
      standadrRectProp.set("depth", 1);
      sortPointsRect = language.newRect(new Offset(0, 10, mainSCRect, "SW"),
          new Offset(440, 240, mainSCRect, "SW"), "sortPointsRectangle", null,
          standadrRectProp);
      sortPointsCode = language.newSourceCode(new Offset(10, -10,
          sortPointsRect, "NW"), "sortPointsCode", null,
          standartSourceCodeProperties);
      sortPointsCode.addCodeLine("Selects the point with lowest Y", null, 0,
          null);
      sortPointsCode
          .addCodeLine(
              "Sorts the points in increasing order of the angle they and the pivot point make",
              null, 0, null);
      sortPointsCode
          .addCodeLine(
              "with the x-axis (For better visualization Selection Sort is chosen)",
              null, 0, null);
      sortPointsCode.addCodeLine("", null, 0, null);
      sortPointsCode
          .addCodeLine(
              "private static List<Point> sortPoints(Point pivot,List<Point> sorted){",
              null, 0, null);
      sortPointsCode.addCodeLine("for(int i=1; i<sorted.size()-1;i++){", null,
          1, null);
      sortPointsCode.addCodeLine("int minIndex=i;", null, 2, null);
      sortPointsCode.addCodeLine("for(int j=i+1;j<sorted.size();j++)", null, 2,
          null);
      sortPointsCode
          .addCodeLine(
              "if(findAngle(pivot, sorted.get(j))<findAngle(pivot, sorted.get(minIndex)))",
              null, 3, null);
      sortPointsCode.addCodeLine("minIndex=j;", null, 4, null);
      sortPointsCode.addCodeLine("Collections.swap(sorted, i, minIndex);",
          null, 2, null);
      sortPointsCode.addCodeLine("}", null, 1, null);
      sortPointsCode.addCodeLine("return sorted;", null, 1, null);
      sortPointsCode.addCodeLine("}", null, 0, null);

    }

    private void createCoordinatesAndPoints(List<Point> points) {
      coordSquare = language.newSquare(new Coordinates(5, 45), 300,
          "backgroundCoord", null);
      PolylineProperties lineProp = new PolylineProperties();
      lineProp.set("color", Color.red);
      xAxis = language.newPolyline(convertToNodes(5, 195, 305, 195),
          "lineXaxis", null, lineProp);
      yAxis = language.newPolyline(convertToNodes(155, 45, 155, 345),
          "lineYaxis", null, lineProp);
      mapCoodinates(points);

    }

    Node getNode(int x, int y) {
      return Node.convertToNode(new java.awt.Point(x, y));
    }

    Node[] convertToNodes(int x1, int y1, int x2, int y2) {
      Node[] nodes = new Node[2];
      nodes[0] = getNode(x1, y1);
      nodes[1] = getNode(x2, y2);
      return nodes;
    }

    private String[][] getPointsData(List<Point> points) {
      String[][] data = new String[2][points.size() + 1];
      data[0][0] = "x";
      data[1][0] = "y";
      int index = 1;
      for (Point point : points) {
        data[0][index] = Integer.toString(point.getX());
        data[1][index] = Integer.toString(point.getY());
        index++;
      }
      return data;
    }

    private void createPointArrays(List<Point> points) {
      TextProperties textProp = new TextProperties();
      textProp.set("color", Color.black);
      textProp.set("font", new Font("SansSerif", Font.PLAIN, 14));
      sortedLabel = language.newText(new Offset(5, 125, coordSquare, "SW"),
          "List<Point> sortedPoints :", "sortedLabel", null, textProp);
      convexCandidatesLabel = language.newText(new Offset(5, 235, coordSquare,
          "SW"), "List<Point> convexCandidates :", "convexCandidatesLabel",
          null, textProp);
      pointsLabel = language.newText(new Offset(5, 15, coordSquare, "SW"),
          "List<Point> points :", "pointsLabel", null, textProp);

      String[][] gridData = getPointsData(points);
      pointsGrid = new GridTable(language, gridData, "points", 0, 10,
          pointsLabel.getName());
      sortedPointsGrid = new GridTable(language, gridData, "sortedPoints", 0,
          10, sortedLabel.getName());
      sortedPointsGrid.hide();

      gridData = getPointsData(new ArrayList<Point>());
      convexCandidatesGrid = new GridTable(language, points.size(),
          "convexCandidates", 0, 10, convexCandidatesLabel.getName());
      convexCandidatesGrid.hide();

    }

    private void hideDescription(int time) {
      description.hide(new TicksTiming(time));
      descriptionRect.hide(new TicksTiming(time));
    }

    private void step() {
      language.nextStep();
    }

    private void step(String name) {
      language.nextStep(name);
    }

    private void highlightPointElement(Point point) {
      if (coordMap.containsKey(point)) {
        coordMap.get(point).changeColor("fillColor", mPointColor, null, null);
        coordMap.get(point).changeColor("color", mPointColor, null, null);

      }
    }

    private void unhighlightPointElement(Point point) {
      if (coordMap.containsKey(point)) {
        coordMap.get(point).changeColor("fillColor", stPointColor, null, null);
        coordMap.get(point).changeColor("color", stPointColor, null, null);

      }
    }

    // ************Helper methods*********************

    // helper method for creating lines
    Polyline createLine(Point point1, Point point2, PolylineProperties prop,
        String name) {
      return language.newPolyline(
          convertToNodes(mapXCoord(point1.x), mapYCoord(point1.y),
              mapXCoord(point2.x), mapYCoord(point2.y)), "pivotLine", null,
          prop);
    }

    // used for the visualization mapping
    int mapXCoord(int x) {
      return 155 + x;
    }

    // used for the visualization mapping
    int mapYCoord(int y) {
      return 195 - y;
    }

    // creates the visualized points and maps then to the point object
    void mapCoodinates(List<Point> points) {
      // int x0=155;
      // int y0=195;
      int index = 0;
      coordMap = new HashMap<Point, Circle>();
      CircleProperties circleProp = new CircleProperties();
      circleProp.set("color", stPointColor);
      circleProp.set("filled", true);
      circleProp.set("fillColor", stPointColor);
      for (Point p : points) {
        Circle point = language.newCircle(
            getNode(mapXCoord(p.x), mapYCoord(p.y)), 2, "p" + index++, null,
            circleProp);
        coordMap.put(p, point);
      }
    }

    // *************End of helper methods***************

    public class Point {// Point object
      int x;
      int y;

      public Point(int x, int y) {
        super();
        this.x = x;
        this.y = y;
      }

      public int getX() {
        return x;
      }

      // public void setX(int x) {
      // this.x = x;
      // }
      public int getY() {
        return y;
      }
      // public void setY(int y) {
      // this.y = y;
      // }
    }

    // *******************Graham scan Algorithm
    private int selectPivotElement(List<Point> points) {
      selectPivotCode.highlight(2);
      step();
      highlightPointElement(points.get(0));
      sortedPointsGrid.highlight(0);
      int lastHighlightedElem = 0;
      selectPivotCode.unhighlight(2);
      selectPivotCode.highlight(3);
      step();

      int minIndex = 0;

      // int lastHighlightedValue;

      for (Point point : points) {
        // lastHighlightedValue=points.indexOf(point);
        sortedPointsGrid.highlightValue(points.indexOf(point));

        selectPivotCode.unhighlight(3);
        selectPivotCode.highlight(4);
        step();

        selectPivotCode.unhighlight(4);
        selectPivotCode.highlight(4, 0, true);
        selectPivotCode.highlight(5);
        selectPivotCode.unhighlight(6);
        step();

        if (point.getY() < points.get(minIndex).getY()) { // if y of point i is
                                                          // less than y of
                                                          // point minIndex

          selectPivotCode.unhighlight(5);
          selectPivotCode.highlight(6);
          sortedPointsGrid.unhighlight(lastHighlightedElem);
          unhighlightPointElement(points.get(lastHighlightedElem));
          lastHighlightedElem = points.indexOf(point);
          sortedPointsGrid.highlight(lastHighlightedElem);
          highlightPointElement(point);
          step();

          minIndex = points.indexOf(point); // set minY index ot i
        } else {

          selectPivotCode.unhighlight(5);
          selectPivotCode.highlight(7);
          step();
          selectPivotCode.unhighlight(7);
          selectPivotCode.highlight(8);
          step();

          if (point.getY() == points.get(minIndex).getY()) { // if both points
                                                             // have equal y

            selectPivotCode.unhighlight(8);
            selectPivotCode.highlight(9);
            step();

            if (point.getX() < points.get(minIndex).getX()) { // select the one
                                                              // with lower x

              selectPivotCode.unhighlight(9);
              selectPivotCode.highlight(10);
              sortedPointsGrid.unhighlight(lastHighlightedElem);
              lastHighlightedElem = points.indexOf(point);
              sortedPointsGrid.highlight(lastHighlightedElem);
              step();

              minIndex = points.indexOf(point);
            }
            selectPivotCode.unhighlight(9);
            // selectPivotCode.highlight(10);
          }
          selectPivotCode.unhighlight(8);
          // selectPivotCode.highlight(11);
        }
        selectPivotCode.unhighlight(5);
        selectPivotCode.unhighlight(6);
        sortedPointsGrid.unhighlightValue(points.indexOf(point));
        step();
      }
      selectPivotCode.unhighlight(4);
      selectPivotCode.highlight(11);
      step();
      return minIndex;
    }

    private List<Point> sortPoints(Point pivot, List<Point> sorted) {
      sortedPointsGrid.unhighlight(0);
      PolylineProperties redLineProp = new PolylineProperties();
      redLineProp.set("color", Color.red);
      PolylineProperties blackLineProp = new PolylineProperties();
      blackLineProp.set("color", Color.black);
      PolylineProperties greenLineProp = new PolylineProperties();
      greenLineProp.set("color", Color.green);

      Polyline pivotLine = language.newPolyline(
          convertToNodes(5, mapYCoord(pivot.y), 305, mapYCoord(pivot.y)),
          "pivotLine", null, redLineProp);
      sortPointsCode.highlight(4);
      step();

      for (int i = 1; i < sorted.size() - 1; i++) {
        // sortedPointsGrid.highlight(i);
        sortPointsCode.highlight(5);
        sortPointsCode.unhighlight(4);
        step();

        int minIndex = i;

        Polyline indexLine = createLine(pivot, sorted.get(minIndex),
            greenLineProp, "indexLine" + lineIndex++);

        highlightPointElement(sorted.get(minIndex));
        sortPointsCode.unhighlight(5);
        sortPointsCode.highlight(5, 0, true);
        sortPointsCode.highlight(6);
        sortedPointsGrid.highlight(minIndex);
        step();

        for (int j = i + 1; j < sorted.size(); j++) {

          sortedPointsGrid.highlightValue(j);
          sortPointsCode.unhighlight(6);
          sortPointsCode.highlight(7);
          step();
          sortPointsCode.unhighlight(7);
          sortPointsCode.highlight(7, 0, true);
          sortPointsCode.highlight(8);
          Polyline newLine = createLine(pivot, sorted.get(j), blackLineProp,
              "newLine" + lineIndex++);
          step();

          if (findAngle(pivot, sorted.get(j)) < findAngle(pivot,
              sorted.get(minIndex))) {
            sortPointsCode.unhighlight(8);
            sortPointsCode.highlight(9);
            sortedPointsGrid.unhighlight(minIndex);
            unhighlightPointElement(sorted.get(minIndex));
            indexLine.hide();

            minIndex = j;

            indexLine = createLine(pivot, sorted.get(j), greenLineProp,
                "indexLine" + lineIndex++);
            highlightPointElement(sorted.get(minIndex));
            sortedPointsGrid.highlight(minIndex);
            step();
          }
          newLine.hide();
          sortPointsCode.unhighlight(8);
          sortPointsCode.unhighlight(9);
          sortedPointsGrid.unhighlightValue(j);
          step();
        }
        Collections.swap(sorted, i, minIndex);

        sortPointsCode.unhighlight(7);
        sortPointsCode.highlight(10);
        sortedPointsGrid.highlight(i);
        sortedPointsGrid.unhighlightValue(minIndex);
        step();
        sortedPointsGrid.setPointValues(i, sorted.get(i));
        // sortedPointsGrid.setPointValues(i, sorted.get(i));
        // sortedPointsGrid.setPointValues(minIndex, sorted.get(minIndex));
        sortedPointsGrid.setPointValues(minIndex, sorted.get(minIndex));

        step();

        indexLine.hide();
        sortedPointsGrid.unhighlight(i);
        sortedPointsGrid.unhighlight(minIndex);
        sortPointsCode.unhighlight(10);
        step();
      }
      sortPointsCode.unhighlight(5);
      sortPointsCode.highlight(12);
      pivotLine.hide();
      for (Point point : sorted)
        unhighlightPointElement(point);
      step();
      return sorted;
    }

    public double findAngle(Point point1, Point point2) {
      double deltaX = (double) (point2.getX() - point1.getX());
      double deltaY = (double) (point2.getY() - point1.getY());
      double angle;

      if (deltaX == 0 && deltaY == 0)
        return 0;

      angle = Math.atan2(deltaY, deltaX) * 57.295779513082;

      if (angle < 0)
        angle += 360.;

      return angle;
    }

    private boolean isLeftTurn(Point point1, Point point2, Point point3) {
      double alpha = findAngle(point1, point2);
      double beta = findAngle(point2, point3);
      if (beta >= alpha && beta < 360 + alpha)
        return true;
      else
        return false;
    }

    List<Point> grahamScan(List<Point> points) {
      createHeader();
      step("Introduction");
      createDescription();
      step();
      hideDescription(800);

      if (questions) {
        step("Question 1");
        QuestionGroupModel qGroup = new QuestionGroupModel("gr1");
        language.addQuestionGroup(qGroup);

        MultipleChoiceQuestionModel mc1 = new MultipleChoiceQuestionModel("mc1");
        mc1.setPrompt("The first step of the Graham Scan algorithm is: ");
        mc1.addAnswer(
            "The set of points is sorted in increasing order of the angle they and the pivot point make with the x axis",
            0,
            "False! The correct answer is: Find the point with the lowest y-coordinate.");
        mc1.addAnswer("Find the point with the lowest y-coordinate.", 10,
            "Congratulations! This is the correct answer.");
        mc1.addAnswer("Determine the convex hull", 0,
            "False! The correct answer is: Find the point with the lowest y-coordinate.");
        mc1.setGroupID("gr1");

        // TrueFalseQuestionModel tf1 = new TrueFalseQuestionModel("TF1", true,
        // 10);
        // tf1.setGroupID("gr1");
        // tf1.setPrompt("choose the right answer");
        // tf1.setFeedbackForAnswer(true,"gut");
        // tf1.setFeedbackForAnswer(false, "naja");
        // tf1.setCorrectAnswer(true);
        // language.addTFQuestion(tf1);

        language.addMCQuestion(mc1);

      }
      step("Alogrithm");
      createMainSourceCode();

      createCoordinatesAndPoints(points);
      createPointArrays(points);
      step();

      int lineIndex = 0;
      mainSourceCode.highlight(lineIndex++);
      step();

      List<Point> sortedPoints = new ArrayList<Point>(points);

      mainSourceCode.unhighlight(lineIndex - 1);
      mainSourceCode.highlight(lineIndex++);
      sortedPointsGrid.show();
      step();

      mainSourceCode.unhighlight(lineIndex - 1);
      mainSourceCode.highlight(lineIndex);
      step();

      mainSourceCode.highlight(lineIndex++, 0, true);
      createSelectPivotCode();
      step();

      int pivotPointIndex = selectPivotElement(sortedPoints); // select pivot
                                                              // element with
                                                              // lowest Y

      sortedPointsGrid.highlight(0);

      if (questions) {
        step("Question 2");
        MultipleChoiceQuestionModel mc2 = new MultipleChoiceQuestionModel("mc2");
        mc2.setPrompt("The second step of the Graham Scan algorithm is: ");
        mc2.addAnswer(
            "The set of points is sorted in increasing order of the angle they and the pivot point make with the x axis",
            10, "Congratulations! This is the correct answer.");
        mc2.addAnswer(
            "Find the point with the lowest y-coordinate.",
            0,
            "False! The correct answer is: The set of points is sorted in increasing order of the angle they and the pivot point make with the x axis.");
        mc2.addAnswer(
            "Determine the convex hull",
            0,
            "False! The correct answer is: The set of points is sorted in increasing order of the angle they and the pivot point make with the x axis.");
        mc2.setGroupID("gr1");
        language.addMCQuestion(mc2);

      }

      step();
      Collections.swap(sortedPoints, pivotPointIndex, 0); // swap the the first
                                                          // point and the pivot
                                                          // point

      selectPivotRect.hide();
      selectPivotCode.hide();
      mainSourceCode.unhighlight(lineIndex - 1);
      mainSourceCode.highlight(lineIndex++);
      sortedPointsGrid.highlight(0);
      step();
      // sortedPointsGrid.setPointValues(0, sortedPoints.get(0));
      sortedPointsGrid.setPointValues(0, sortedPoints.get(0));
      // sortedPointsGrid.setPointValues(pivotPointIndex,
      // sortedPoints.get(pivotPointIndex));
      sortedPointsGrid.setPointValues(pivotPointIndex,
          sortedPoints.get(pivotPointIndex));

      step();

      mainSourceCode.unhighlight(lineIndex - 1);
      mainSourceCode.highlight(lineIndex);
      sortedPointsGrid.unhighlight(pivotPointIndex);
      step();

      mainSourceCode.highlight(lineIndex++, 0, true);
      createSortPointsCode();
      step();

      sortedPoints = sortPoints(sortedPoints.get(0), sortedPoints); // points
                                                                    // are
                                                                    // sorted in
                                                                    // increasing
                                                                    // order of
                                                                    // the angle
      // they and the PIVOT point make with the x-axis.

      sortPointsCode.hide();
      sortPointsRect.hide();
      mainSourceCode.unhighlight(lineIndex - 1);
      mainSourceCode.highlight(lineIndex++);
      convexCandidatesGrid.show();
      step();

      List<Point> convexCandidates = new ArrayList<Point>();

      convexCandidatesGrid.setPointValues(0, sortedPoints.get(0));
      mainSourceCode.unhighlight(lineIndex - 1);
      mainSourceCode.highlight(lineIndex++);
      step();

      convexCandidates.add(sortedPoints.get(0));

      convexCandidatesGrid.setPointValues(1, sortedPoints.get(1));
      mainSourceCode.unhighlight(lineIndex - 1);
      mainSourceCode.highlight(lineIndex++);
      step();

      convexCandidates.add(sortedPoints.get(1)); // Place the first 2 points of
                                                 // the sorted points array in
                                                 // the convex candidates

      // sortedPointsGrid.highlight(2);
      mainSourceCode.unhighlight(lineIndex - 1);
      mainSourceCode.highlight(lineIndex++);

      int i = 2;

      if (questions) {

        step("Question 3");

        MultipleChoiceQuestionModel mc3 = new MultipleChoiceQuestionModel("mc3");
        mc3.setPrompt("The last step of the Graham Scan algorithm is: ");
        mc3.addAnswer(
            "The set of points is sorted in increasing order of the angle they and the pivot point make with the x axis",
            0, "False! The correct answer is: Determine the convex hull.");
        mc3.addAnswer("Find the point with the lowest y-coordinate.", 0,
            "False! The correct answer is: Determine the convex hull.");
        mc3.addAnswer("Determine the convex hull", 10,
            "Congratulations! This is the correct answer.");
        mc3.setGroupID("gr1");
        language.addMCQuestion(mc3);

      }
      step();

      lineStack = new Stack<Polyline>();
      Polyline newLine = language.newPolyline(
          convertToNodes(
              mapXCoord(convexCandidates.get(convexCandidates.size() - 2).x),
              mapYCoord(convexCandidates.get(convexCandidates.size() - 2).y),
              mapXCoord(convexCandidates.get(convexCandidates.size() - 1).x),
              mapYCoord(convexCandidates.get(convexCandidates.size() - 1).y)),
          "line" + lineIndex++, null, blackLineProp);
      lineStack.add(newLine);
      int turnindex = 0;
      AngleVisualization leftT = null;
      AngleVisualization rigthT = null;

      while (i < sortedPoints.size()) {
        sortedPointsGrid.highlight(i);
        mainSourceCode.unhighlight(8);
        mainSourceCode.highlight(9);
        mainSourceCode.unhighlight(19);
        step();
        mainSourceCode.highlight(10);
        mainSourceCode.unhighlight(9);
        mainSourceCode.highlight(9, 0, true);
        step();

        Point candidatePoint1 = convexCandidates
            .get(convexCandidates.size() - 2);

        Point candidatePoint2 = convexCandidates
            .get(convexCandidates.size() - 1);

        mainSourceCode.unhighlight(10);
        mainSourceCode.highlight(11);
        step();

        Point newCandidatePoint = sortedPoints.get(i);

        newLine = language.newPolyline(
            convertToNodes(mapXCoord(candidatePoint2.x),
                mapYCoord(candidatePoint2.y), mapXCoord(newCandidatePoint.x),
                mapYCoord(newCandidatePoint.y)), "line" + lineIndex++, null,
            greenLineProp);

        mainSourceCode.unhighlight(11);
        mainSourceCode.highlight(12);
        step();

        if (isLeftTurn(candidatePoint1, candidatePoint2, newCandidatePoint)) { // if
                                                                               // left
                                                                               // turn
          leftT = new AngleVisualization(candidatePoint2.x, candidatePoint2.y,
              true, turnindex++);
          mainSourceCode.unhighlight(12);
          mainSourceCode.highlight(13);
          step();

          mainSourceCode.unhighlight(13);
          mainSourceCode.highlight(14);
          convexCandidatesGrid.setPointValues(convexCandidates.size(),
              newCandidatePoint);
          newLine.changeColor("color", Color.black, null, null);
          lineStack.add(newLine);
          step();

          convexCandidates.add(newCandidatePoint); // add the new candidate to
                                                   // the convex candidate array

          sortedPointsGrid.unhighlight(i);

          i++;

          mainSourceCode.unhighlight(14);
          mainSourceCode.highlight(15);
          step();
        } else { // if right turn
          rigthT = new AngleVisualization(candidatePoint2.x, candidatePoint2.y,
              false, turnindex++);
          mainSourceCode.unhighlight(12);
          mainSourceCode.highlight(13);
          step();

          mainSourceCode.unhighlight(13);
          mainSourceCode.unhighlight(15);
          mainSourceCode.highlight(16);
          step();

          if (rigthT != null)
            rigthT.hide();
          mainSourceCode.unhighlight(16);
          mainSourceCode.highlight(17);
          lineStack.pop().hide();
          newLine.hide();
          convexCandidatesGrid.setGridValue(0, convexCandidates.size(), "");
          convexCandidatesGrid.setGridValue(1, convexCandidates.size(), "");
          step();
          convexCandidates.remove(convexCandidates.size() - 1); // remove the
                                                                // last point of
                                                                // the candidate
                                                                // array

        }
        if (rigthT != null)
          rigthT.hide();
        if (leftT != null)
          leftT.hide();
        mainSourceCode.unhighlight(17);
        mainSourceCode.unhighlight(15);
        mainSourceCode.highlight(19);
        step();

      }

      mainSourceCode.highlight(20);
      mainSourceCode.unhighlight(9);
      mainSourceCode.unhighlight(19);
      newLine = language.newPolyline(
          convertToNodes(
              mapXCoord(convexCandidates.get(convexCandidates.size() - 1).x),
              mapYCoord(convexCandidates.get(convexCandidates.size() - 1).y),
              mapXCoord(convexCandidates.get(0).x),
              mapYCoord(convexCandidates.get(0).y)), "line" + lineIndex++,
          null, blackLineProp);
      lineStack.add(newLine);

      if (questions) {
        step("Question 4");
        MultipleChoiceQuestionModel mc4 = new MultipleChoiceQuestionModel("mc4");
        mc4.setPrompt("What is the complexity of the last step of the algorithm? ");
        mc4.addAnswer("O(n log n)", 0, "False! The correct answer is: O( n )");
        mc4.addAnswer("O( n )", 10,
            "Congratulations! This is the correct answer.");
        mc4.addAnswer("O( n^2 )", 0, "False! The correct answer is: O( n )");
        mc4.setGroupID("gr1");
        language.addMCQuestion(mc4);

      }
      step("Closing");
      hideAll();
      createClosing();
      step();
      return convexCandidates;
    }

    // public static void main(String[] args) {
    // // Create a new animation
    // // name, author, screen width, screen height
    // Language language = new AnimalScript("Graham Scan", "Ivaylo Petkov", 640,
    // 480);
    // GrahamScan grahamScan=new GrahamScan(language);
    // //Create input
    // List<Point>points=new ArrayList<Point>();
    // points.add(grahamScan.new Point(30,70));
    // points.add(grahamScan.new Point(40,70));
    // points.add(grahamScan.new Point(20,60));
    // points.add(grahamScan.new Point(70,80));
    // points.add(grahamScan.new Point(-50,70));
    // points.add(grahamScan.new Point(-70,-100));
    // points.add(grahamScan.new Point(50,-70));
    // points.add(grahamScan.new Point(-30,-40));
    // points.add(grahamScan.new Point(0,0));
    // points=grahamScan.grahamScan(points);
    // //grahamScan.mapCoodinates(points);
    // System.out.println(language);
    // }

  }
}