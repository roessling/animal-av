package generators.graphics;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graphics.RamerDouglasPeucker.DouglasPeuckerAPI.Point;

public class RamerDouglasPeucker implements Generator {
  private Language lang;

  Color            stLineColor   = Color.BLACK;
  Color            mLineColor    = Color.GREEN;
  Color            stPointColor  = Color.BLACK;
  Color            mPointColor   = Color.RED;
  Color            accPointColor = Color.GREEN;
  private int[][]  pointsIn;

  public void init() {
    lang = new AnimalScript("RamerDouglasPeucker[EN]", "Ivaylo Petkov", 800,
        600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    mLineColor = (Color) primitives.get("highlighted_Line_Color");
    stLineColor = (Color) primitives.get("standart_Line_Color");
    stPointColor = (Color) primitives.get("standart_Point_Color");
    double eps = (Double) primitives.get("epsilon");
    accPointColor = (Color) primitives.get("accepted_Point_Color");
    pointsIn = (int[][]) primitives.get("points");
    mPointColor = (Color) primitives.get("highlighted_Point_Color");

    // pointsIn = (int[][])primitives.get("points");
    // stLineColor=(Color)primitives.get("standart_Line_Color");
    // mLineColor=(Color)primitives.get("highlighted_Line_Color");
    // stPointColor=(Color)primitives.get("standart_Point_Color");
    // mPointColor=(Color)primitives.get("highlighted_Point_Color");
    // accPointColor=(Color)primitives.get("accepted_Point_Color");
    DouglasPeuckerAPI dPeucker = new DouglasPeuckerAPI(lang, eps);
    DouglasPeuckerAPI.Point[] pointsA = new Point[pointsIn[0].length];
    //
    if (pointsIn.length >= 0)
      for (int i = 0; i < pointsIn[0].length; i++)
        pointsA[i] = dPeucker.new Point(pointsIn[0][i], pointsIn[1][i]);
    // Point[] points= new Point[8];
    // points[0]=dPeucker.new Point(-100,100);
    // points[1]=dPeucker.new Point(-90,-80);
    // points[2]=dPeucker.new Point(-30,-100);
    // points[3]=dPeucker.new Point(-30,-20);
    // points[4]=dPeucker.new Point(30,-10);
    // points[5]=dPeucker.new Point(50,-15);
    // points[6]=dPeucker.new Point(100,-30);
    // points[7]=dPeucker.new Point(140,0);
    dPeucker.start(pointsA);
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "RamerDouglasPeucker";
  }

  public String getName() {
    return "RamerDouglasPeucker[EN]";
  }

  public String getAnimationAuthor() {
    return "Ivaylo Petkov";
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

  public String getCodeExample() {
    String code = "";

    code += "public List<Integer> douglasPeucker(Point[] points, int startIndex, int endIndex) {\n";
    code += "		double dmax = 0;\n";
    code += "		int index = 0;\n";
    code += "\n";
    code += "		for(int i = startIndex + 1 ; i < endIndex; i++) {\n";
    code += "			double distance = this.perpendicularDistance( points[startIndex], points[endIndex],points[i]);\n";
    code += "			if(distance > dmax) {\n";
    code += "				index = i;\n";
    code += "				dmax = distance;\n";
    code += "			}\n";
    code += "		}\n";
    code += "		List<Integer> pointsToKeep = new ArrayList<Integer>();\n";
    code += "		if(dmax >= epsilon && index!=0) {\n";
    code += "			pointsToKeep.add(index);\n";
    code += "			pointsToKeep.addAll(douglasPeucker(points, startIndex, index));\n";
    code += "			pointsToKeep.addAll(douglasPeucker(points, index,endIndex));\n";

    code += "		}  \n";
    code += "		return pointsToKeep;\n";
    code += "}\n";

    return code;
  }

  public String getDescription() {
    return "  <p>The Douglas-Peucker algorithm is an algorithm for reducing the number \n"
        + "of points in a curve that is approximated by a series of points. The \n"
        + "initial form of the algorithm was independently suggested in 1972 by \n"
        + "Urs Ramer and 1973 by David Douglas and Thomas Peucker[1] and several\n"
        + "others in the following decade[2]. This algorithm is also known under \n"
        + "the following names: the Ramer-Douglas-Peucker algorithm, the iterative\n"
        + "end-point fit algorithm or the split-and-merge algorithm.</p>\n"

        + "</p>The purpose of the algorithm is, given a curve composed of line segments,\n"
        + "to find a similar curve with fewer points. The algorithm defines \n"
        + "'dissimilar' based on the maximum distance between the original curve \n"
        + "and the simplified curve. The simplified curve consists of a subset of \n"
        + "the points that defined the original curve.</p>\n"

        + "</p>The starting curve is an ordered set of points or lines and the distance \n"
        + "dimension e > 0. The original (unsmoothed) curve is shown in 0 and the \n"
        + "final output curve is shown in blue on row 4.\n"
        + "The algorithm recursively divides the line. Initially it is given all the\n"
        + "points between the first and last point. It automatically marks the first \n"
        + "and last point to be kept. It then finds the point that is furthest from \n"
        + "the line segment with the first and last points as end points (this point \n"
        + "is obviously furthest on the curve from the approximating line segment \n"
        + "between the end points). If the point is closer than e to the line segment \n"
        + "then any points not currently marked to keep can be discarded without the \n"
        + "smoothed curve being worse than e.\n"
        + "If the point furthest from the line segment is greater than e from the \n"
        + "approximation then that point must be kept. The algorithm recursively \n"
        + "calls itself with the first point and the worst point and then with the \n"
        + "worst point and the last point (which includes marking the worst point\n"
        + "being marked as kept).\n"
        + "When the recursion is completed a new output curve can be generated \n"
        + "consisting of all (and only) those points that have been marked as kept.</p>\n"

        + "</p>The algorithm is used for the processing of vector graphics and \n"
        + "cartographic generalization. The algorithm is widely used in robotics\n"
        + "to perform simplification and denoising of range data acquired by a \n"
        + "rotating range scanner, in this field it is called the split-and-merge\n"
        + "algorithm and is attributed to Duda and Hart.</p>\n"

        + "</p>The expected complexity of this algorithm can be described by the linear\n"
        + "recurrence T(n) = 2T(n/2) + O(n), which has the well-known solution \n"
        + "(via the Master Theorem). However, the worst-case complexity is O(n2).</p>\n"
        + "  <p>                               Source: Wikipedia (http://en.wikipedia.org/wiki/Graham_scan)\" to \"description\"</p>";

  }

  public class DouglasPeuckerAPI {
    double                 epsilon;
    // Animation Elements
    RectProperties         standadrRectProp;
    SourceCodeProperties   standartSourceCodeProperties;
    Language               language;
    Square                 coordSquare;
    Polyline               xAxis;
    Polyline               yAxis;
    Rect                   headerRect;
    Text                   headerText;

    Rect                   descriptionRect;
    SourceCode             description;
    SourceCode             description2;
    Rect                   closingRect;
    SourceCode             closing;
    Rect                   mainSCRect;
    SourceCode             mainSourceCode;
    HashMap<Point, Circle> coordMap;
    PolylineProperties     blackLineProp;
    PolylineProperties     greenLineProp;
    int                    lineIndex      = 0;
    int                    accPointsIndex = 0;
    Text                   pointsLabel;
    GridTable              pointsGrid;
    Text                   acceptedPointsLabel;
    GridTable              acceptedPointsGrid;
    GridTable              tempGrid;

    Stack<Polyline>        lineStack;

    public DouglasPeuckerAPI(Language language, double epsilon) {
      this.epsilon = epsilon;
      this.language = language;
      this.language.setStepMode(true);
      standadrRectProp = new RectProperties();
      standadrRectProp.set("fillColor", Color.LIGHT_GRAY);
      standadrRectProp.set("color", Color.BLACK);
      standadrRectProp.set("filled", true);
      standadrRectProp.set("depth", 2);
      standartSourceCodeProperties = new SourceCodeProperties();
      standartSourceCodeProperties.set("highlightColor", Color.BLACK);
      standartSourceCodeProperties.set("contextColor", Color.BLUE);

      standartSourceCodeProperties.set("color", Color.GRAY);
      standartSourceCodeProperties.set("font", new Font("SansSerif",
          Font.PLAIN, 12));
      standartSourceCodeProperties.set("depth", 1);

      blackLineProp = new PolylineProperties();
      blackLineProp.set("color", stLineColor);
      greenLineProp = new PolylineProperties();
      greenLineProp.set("color", mLineColor);

    }

    // class creating and managing grid table due to the incomplete
    // implementation of grid
    private class GridTable {
      private Language   language;
      private String[][] data;
      private String     name;
      private int        offsetX;
      private int        offsetY;
      private Color      borderColor   = Color.BLACK;
      private Color      color         = Color.BLACK;
      private Color      fillColor     = Color.GRAY;
      private Color      elementColor  = Color.BLUE;
      private Color      elemHighlight = Color.YELLOW;
      private int        cellWidth     = 30;
      private int        cellHeight    = 30;
      private boolean    fixedCellSize = true;
      private String     font          = "font SansSerif size 20";
      private String     offsetElem;
//      private boolean    temp;
      private boolean    indexed;
      private int        tempIndex     = 0;

      public GridTable(Language language, String[][] data, String name,
          int offsetX, int offsetY, String offsetElem, boolean temp,
          boolean indexed) {
        super();
        this.language = language;
        this.data = data;
        this.name = name;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetElem = offsetElem;
//        this.temp = temp;
        this.indexed = indexed;
        if (temp)
          cellWidth = 90;
        if (indexed)
          tempIndex = 1;
        createGrid();
      }

      // setGridValue "points[0][0]" "x"
      // setGridValue "points[1][0]" "y"

      public GridTable(Language language, int size, String name, int offsetX,
          int offsetY, String offsetElem, boolean temp, boolean indexed) {
        this.language = language;
        // this.data = data;
        this.name = name;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetElem = offsetElem;
//        this.temp = temp;
        this.indexed = indexed;
        if (temp)
          cellWidth = 90;
        if (indexed)
          tempIndex = 1;
        createEmptyGrid(size + 1);
      }

      public void setPointValues(int index, Point point) {
        setGridValue(0, index + 1, "" + point.x);
        setGridValue(1, index + 1, "" + point.y);
      }

      public void setGridValue(int x, int y, String value) {
        int x1 = x + tempIndex;
        language.addLine("setGridValue \"" + name + "[" + x1 + "]" + "[" + y
            + "]\" \"" + value + "\"");
      }

      private void fillGrid() {
        for (int i = 0; i < data[0].length; i++) {
          if (indexed)
            setGridValue(-1, i + 1, "" + i);
          setGridValue(0, i, data[0][i]);
          setGridValue(1, i, data[1][i]);
        }
      }

      private void createGrid() {
        int lines = data.length + tempIndex;
        String createLine = "grid \"" + name + "\" offset (" + offsetX + ","
            + offsetY + ") " + " from \"" + offsetElem + "\" SW " + "lines "
            + lines + " columns " + data[0].length + " style table cellWidth "
            + cellWidth + " cellHeight " + cellHeight;
        if (fixedCellSize)
          createLine += " fixedCellSize ";
        createLine += " color " + color + " borderColor " + borderColor
            + " fillColor " + fillColor + " elementColor " + elementColor
            + " elemHighlight " + elemHighlight + " " + font;
        language.addLine(createLine);
        fillGrid();
      }

      private void createEmptyGrid(int size) {
        int lines = 2 + tempIndex;
        String createLine = "grid \"" + name + "\" offset (" + offsetX + ","
            + offsetY + ") " + " from \"" + offsetElem + "\" SW " + "lines "
            + lines + " columns " + size + " style table cellWidth "
            + cellWidth + " cellHeight " + cellHeight;
        if (fixedCellSize)
          createLine += " fixedCellSize ";
        createLine += " color " + color + " borderColor " + borderColor
            + " fillColor " + fillColor + " elementColor " + elementColor
            + " elemHighlight " + elemHighlight + " " + font;
        language.addLine(createLine);
        setGridValue(0, 0, "x");
        setGridValue(1, 0, "y");
        // fillGrid();
      }

      // private String convertColor(Color color){
      // String
      // sColor="("+color.getRed()+","+color.getGreen()+","+color.getBlue()+")";
      // return sColor;
      // }
      public void unhighlightAll() {
        for (int i = 0; i < data[0].length - 1; i++)
          unhighlight(i);
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
        int x1 = x + tempIndex;
        language.addLine("highlightGridCell \"" + name + "[" + x1 + "]" + "["
            + y + "]\" ");
      }

      public void unhighlightGridCell(int x, int y) {
        int x1 = x + tempIndex;
        language.addLine("unhighlightGridCell \"" + name + "[" + x1 + "]" + "["
            + y + "]\" ");
      }

      public void highlightValue(int elemIndex) {
        setGridColor(0, elemIndex + 1, Color.GREEN);
        setGridColor(1, elemIndex + 1, Color.GREEN);
      }

      public void unhighlightValue(int elemIndex) {
        setGridColor(0, elemIndex + 1, Color.BLACK);
        setGridColor(1, elemIndex + 1, Color.BLACK);
      }

      public void unhighlightAllValue() {
        for (int i = 0; i < data[0].length - 1; i++)
          unhighlightValue(i);
      }

      public void setGridColor(int x, int y, Color color) {
        int x1 = x + tempIndex;
        language.addLine("setGridColor \"" + name + "[" + x1 + "]" + "[" + y
            + "]\" textColor " + color);
      }

      public void hide() {
        language.addLine("hide \"" + name + "\"");
      }

//      public void show() {
//        language.addLine("show \"" + name + "\"");
//      }

    }

    private String[][] getPointsData(Point[] points) {
      String[][] data = new String[2][points.length + 1];
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

    private void step() {
      language.nextStep();
    }

    private void step(String name) {
      language.nextStep(name);
    }

    private void createPointArrays(Point[] points) {
      TextProperties textProp = new TextProperties();
      textProp.set("color", Color.BLACK);
      textProp.set("font", new Font("SansSerif", Font.PLAIN, 14));
      pointsLabel = language.newText(new Offset(5, 15, coordSquare, "SW"),
          "Points :", "pointsLabel", null, textProp);

      String[][] gridData = getPointsData(points);
      pointsGrid = new GridTable(language, gridData, "points", 0, 10,
          pointsLabel.getName(), false, true);

      acceptedPointsLabel = language.newText(new Offset(5, 160, coordSquare,
          "SW"), "Accepted Points :", "acceptedPointsLabel", null, textProp);

      acceptedPointsGrid = new GridTable(language, points.length,
          "acceptedPointsGrid", 0, 10, acceptedPointsLabel.getName(), false,
          false);
      String[][] data2 = new String[2][5];

      data2[0][0] = "startIndex";
      data2[1][0] = "";
      data2[0][1] = "endIndex";
      data2[1][1] = "";
      data2[0][2] = "index";
      data2[1][2] = "";
      data2[0][3] = "dmax";
      data2[1][3] = "";
      data2[0][4] = "epsilon";
      data2[1][4] = "" + epsilon;
      tempGrid = new GridTable(language, data2, "tempGrid", 0, 115,
          acceptedPointsLabel.getName(), true, false);
      // tempGrid.setGridValue(0, 0, "startIndex");
      // tempGrid.setGridValue(0, 1, "");
      // tempGrid.setGridValue(0, 2, "startIndex");
      // tempGrid.setGridValue(0, 3, "endIndex");
      // tempGrid.setGridValue(0, 4, "index");
      // tempGrid.setGridValue(0, 5, "dmax");
      // tempGrid.setGridValue(0, 6, "epsilon");
    }

    private void createHeader() {

      headerRect = language.newRect(new Coordinates(5, 0), new Coordinates(243,
          40), "headerRectangle", null, standadrRectProp);
      // text "header" "Graham Scan" (10,5) color black depth 1 font SansSerif
      // size 20 bold
      TextProperties textProp = new TextProperties();
      textProp.set("color", Color.BLACK);
      textProp.set("font", new Font("SansSerif", Font.BOLD, 20));
      headerText = language.newText(new Coordinates(10, 5),
          "Ramer Douglas Peucker", "headerText", null, textProp);

    }

    private void createDescription() {
      SourceCodeProperties scProp = new SourceCodeProperties();
      scProp.set("color", Color.BLACK);
      scProp.set("font", new Font("SansSerif", Font.PLAIN, 17));
      scProp.set("depth", 1);
      descriptionRect = language.newRect(new Offset(0, 9, headerRect, "SW"),
          new Offset(647, 582, headerRect, "NW"), "descriptionRect", null,
          standadrRectProp);

      description = language.newSourceCode(new Offset(5, 5, descriptionRect,
          "NW"), "description", null, scProp);
      description
          .addCodeLine(
              "The Douglas Peucker algorithm is an algorithm for reducing the number ",
              null, 0, null);
      description
          .addCodeLine(
              "of points in a curve that is approximated by a series of points. The",
              null, 0, null);
      description
          .addCodeLine(
              "initial form of the algorithm was independently suggested in 1972 by ",
              null, 0, null);
      description
          .addCodeLine(
              "Urs Ramer and 1973 by David Douglas and Thomas Peucker[1] and several",
              null, 0, null);
      description
          .addCodeLine(
              "others in the following decade[2]. This algorithm is also known under",
              null, 0, null);
      description
          .addCodeLine(
              "the following names: the Ramer Douglas Peucker algorithm, the iterative",
              null, 0, null);
      description.addCodeLine(
          "end-point fit algorithm or the splitand merge algorithm.", null, 0,
          null);
      description.addCodeLine(" ", null, 0, null);
      description.addCodeLine(" ", null, 0, null);
      description
          .addCodeLine(
              "The purpose of the algorithm is, given a curve composed of line segments,",
              null, 0, null);
      description.addCodeLine(
          "to find a similar curve with fewer points. The algorithm defines",
          null, 0, null);
      description
          .addCodeLine(
              "dissimilar based on the maximum distance between the original curve",
              null, 0, null);
      description
          .addCodeLine(
              "and the simplified curve. The simplified curve consists of a subset of",
              null, 0, null);
      description.addCodeLine("the points that defined the original curve.",
          null, 0, null);
      description.addCodeLine(" ", null, 0, null);
      description.addCodeLine(" ", null, 0, null);
      description
          .addCodeLine(
              "                                Source: Wikipedia (http://en.wikipedia.org/wiki/Graham_scan)",
              null, 0, null);

      description.hide(new TicksTiming(800));
      step();
      description2 = language.newSourceCode(new Offset(5, 5, descriptionRect,
          "NW"), "description2", null, scProp);
      description2
          .addCodeLine(
              "The Douglas Peucker algorithm is an algorithm for reducing the number ",
              null, 0, null);
      description2
          .addCodeLine(
              "The starting curve is an ordered set of points or lines and the distance",
              null, 0, null);
      description2
          .addCodeLine(
              "dimension e > 0. The original (unsmoothed) curve is shown in 0 and the ",
              null, 0, null);
      description2.addCodeLine("final output curve is shown in blue on row 4.",
          null, 0, null);
      description2
          .addCodeLine(
              "The algorithm recursively divides the line. Initially it is given all the ",
              null, 0, null);
      description2
          .addCodeLine(
              "points between the first and last point. It automatically marks the first",
              null, 0, null);
      description2
          .addCodeLine(
              "and last point to be kept. It then finds the point that is furthest from ",
              null, 0, null);
      description2
          .addCodeLine(
              "the line segment with the first and last points as end points (this point",
              null, 0, null);
      description2
          .addCodeLine(
              "is obviously furthest on the curve from the approximating line segment ",
              null, 0, null);
      description2
          .addCodeLine(
              "between the end points). If the point is closer than e to the line segment",
              null, 0, null);
      description2
          .addCodeLine(
              "then any points not currently marked to keep can be discarded without the ",
              null, 0, null);
      description2.addCodeLine("smoothed curve being worse than e.", null, 0,
          null);
      description2
          .addCodeLine(
              "If the point furthest from the line segment is greater than e from the",
              null, 0, null);
      description2
          .addCodeLine(
              "approximation then that point must be kept. The algorithm recursively ",
              null, 0, null);
      description2
          .addCodeLine(
              "calls itself with the first point and the worst point and then with the ",
              null, 0, null);
      description2
          .addCodeLine(
              "worst point and the last point (which includes marking the worst point",
              null, 0, null);
      description2.addCodeLine("being marked as kept).", null, 0, null);
      description2
          .addCodeLine(
              "When the recursion is completed a new output curve can be generated ",
              null, 0, null);
      description2
          .addCodeLine(
              "consisting of all (and only) those points that have been marked as kept.",
              null, 0, null);
      description2.addCodeLine(" ", null, 0, null);
      description2.addCodeLine(" ", null, 0, null);
      description2
          .addCodeLine(
              "                                Source: Wikipedia (http://en.wikipedia.org/wiki/Graham_scan)",
              null, 0, null);
      step();
      description2.hide(new TicksTiming(800));
      descriptionRect.hide(new TicksTiming(800));
    }

    private void createClosing() {
      closingRect = language.newRect(new Offset(0, 9, headerRect, "SW"),
          new Offset(647, 582, headerRect, "NW"), "closingRect", null,
          standadrRectProp);

      SourceCodeProperties scProp = new SourceCodeProperties();
      scProp.set("color", Color.BLACK);
      scProp.set("font", new Font("SansSerif", Font.PLAIN, 15));
      scProp.set("depth", 1);
      closing = language.newSourceCode(new Offset(5, 5, descriptionRect, "NW"),
          "closing", null, scProp);

      closing.addCodeLine(
          "The algorithm is used for the processing of vector graphics and ",
          null, 0, null);
      closing
          .addCodeLine(
              "cartographic generalization. The algorithm is widely used in robotics",
              null, 0, null);
      closing
          .addCodeLine(
              "to perform simplification and denoising of range data acquired by a",
              null, 0, null);
      closing
          .addCodeLine(
              "rotating range scanner, in this field it is called the split-and-merge",
              null, 0, null);
      closing.addCodeLine("algorithm and is attributed to Duda and Hart.",
          null, 0, null);
      closing.addCodeLine(" ", null, 0, null);
      closing.addCodeLine(" ", null, 0, null);
      closing
          .addCodeLine(
              "The expected complexity of this algorithm can be described by the linear ",
              null, 0, null);
      closing
          .addCodeLine(
              "recurrence T(n) = 2T(n/2) + O(n), which has the well-known solution ",
              null, 0, null);
      closing
          .addCodeLine(
              "(via the Master Theorem). However, the worst-case complexity is O(n2).",
              null, 0, null);

      closing.addCodeLine(" ", null, 0, null);
      closing.addCodeLine(" ", null, 0, null);
      closing
          .addCodeLine(
              "                                Source: Wikipedia (http://en.wikipedia.org/wiki/Graham_scan)",
              null, 0, null);
    }

    private void createMainSourceCode() {
      mainSCRect = language.newRect(new Offset(355, 5, headerRect, "NW"),
          new Offset(910, 370, headerRect, "NW"), "codeRectangle", null,
          standadrRectProp);
      mainSourceCode = language.newSourceCode(new Coordinates(365, 0),
          "sourceCode", null, standartSourceCodeProperties);

      mainSourceCode
          .addCodeLine(
              "public List<Integer> douglasPeucker(Point[] points, int startIndex, int endIndex) {",
              null, 0, null);
      mainSourceCode.addCodeLine("double dmax = 0;", null, 1, null);
      mainSourceCode.addCodeLine("int index = 0;", null, 1, null);
      mainSourceCode.addCodeLine("", null, 0, null);
      mainSourceCode.addCodeLine(
          "for(int i = startIndex + 1 ; i < endIndex; i++) {", null, 1, null);
      mainSourceCode
          .addCodeLine(
              "double distance = this.perpendicularDistance( points[startIndex], points[endIndex],points[i]);",
              null, 2, null);
      mainSourceCode.addCodeLine("if(distance > dmax) {", null, 2, null);
      mainSourceCode.addCodeLine("index = i;", null, 3, null);
      mainSourceCode.addCodeLine("dmax = distance;", null, 3, null);
      mainSourceCode.addCodeLine("}", null, 2, null);
      mainSourceCode.addCodeLine("}", null, 1, null);
      mainSourceCode.addCodeLine(
          "List<Integer> pointsToKeep = new ArrayList<Integer>();", null, 1,
          null);
      mainSourceCode.addCodeLine("if(dmax >= epsilon && index!=0) {", null, 1,
          null);
      mainSourceCode.addCodeLine("pointsToKeep.add(index);", null, 2, null);
      mainSourceCode.addCodeLine(
          "pointsToKeep.addAll(douglasPeucker(points, startIndex, index));",
          null, 2, null);
      mainSourceCode.addCodeLine(
          "pointsToKeep.addAll(douglasPeucker(points, index,endIndex));", null,
          2, null);

      mainSourceCode.addCodeLine("}  ", null, 1, null);
      mainSourceCode.addCodeLine("return pointsToKeep;", null, 1, null);
      mainSourceCode.addCodeLine("}", null, 0, null);

    }

    // helper method for creating lines
    private Polyline createLine(Point point1, Point point2,
        PolylineProperties prop, String name) {
      // return language.newPolyline(
      // converetToNodes(mapXCoord(point1.x), mapYCoord(point1.y),
      // mapXCoord(point2.x), mapYCoord(point2.y)),
      // name, null,prop);
      return language.newPolyline(getNodes(point1, point2), name, null, prop);
    }

    // used for the visualization mapping
    private int mapXCoord(int x) {
      return 155 + x;
    }

    // used for the visualization mapping
    private int mapYCoord(int y) {
      return 195 - y;
    }

    private Node getNode(int x, int y) {
      return Node.convertToNode(new java.awt.Point(x, y));
    }

    private Node[] getNodes(Point p1, Point p2) {
      Node[] nodes = new Node[2];
      nodes[0] = coordMap.get(p1).getCenter();
      nodes[1] = coordMap.get(p2).getCenter();
      return nodes;
    }

    private Node[] convertToNodes(int x1, int y1, int x2, int y2) {
      Node[] nodes = new Node[2];
      nodes[0] = getNode(x1, y1);
      nodes[1] = getNode(x2, y2);
      return nodes;
    }

    // creates the visualized points and maps then to the point object
    private void mapCoodinates(List<Point> points) {
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

    private void createCoordinatesAndPoints(List<Point> points) {
      coordSquare = language.newSquare(new Coordinates(5, 45), 300,
          "backgroundCoord", null);
      PolylineProperties lineProp = new PolylineProperties();
      lineProp.set("color", Color.RED);
      xAxis = language.newPolyline(convertToNodes(5, 195, 305, 195),
          "lineXaxis", null, lineProp);
      yAxis = language.newPolyline(convertToNodes(155, 45, 155, 345),
          "lineYaxis", null, lineProp);
      mapCoodinates(points);

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

    private void acceptPointElement(Point point) {
      if (coordMap.containsKey(point)) {
        coordMap.get(point).changeColor("fillColor", accPointColor, null, null);
        coordMap.get(point).changeColor("color", accPointColor, null, null);

      }
    }

    private void hideAll() {

      coordSquare.hide();
      xAxis.hide();
      yAxis.hide();

      mainSCRect.hide();
      mainSourceCode.hide();
      pointsLabel.hide();
      pointsGrid.hide();
      acceptedPointsLabel.hide();
      acceptedPointsGrid.hide();
      tempGrid.hide();
      descriptionRect.hide();
      description.hide();

      // HashMap<Point, Circle> coordMap;
      for (Point point : coordMap.keySet())
        coordMap.get(point).hide();
      while (!lineStack.isEmpty())
        lineStack.pop().hide();

    }

    public double perpendicularDistance(Point Point1, Point Point2, Point Point) {
      // Area = |(1/2)(x1y2 + x2y3 + x3y1 - x2y1 - x3y2 - x1y3)| *Area of
      // triangle
      // Base = v((x1-x2)'+(x1-x2)') *Base of Triangle*
      // Area = .5*Base*H *Solve for height
      // Height = Area/.5/Base

      Double area = Math
          .abs(.5 * (Point1.getX() * Point2.getY() + Point2.getX()
              * Point.getY() + Point.getX() * Point1.getY() - Point2.getX()
              * Point1.getY() - Point.getX() * Point2.getY() - Point1.getX()
              * Point.getY()));
      Double bottom = Math.sqrt(Math.pow(Point1.getX() - Point2.getX(), 2)
          + Math.pow(Point1.getY() - Point2.getY(), 2));
      Double height = area / bottom * 2;

      return height;
    }

    //
    // public void refillAcceptedGrid(Point[] points, List<Integer>
    // acceptedPoints ){
    // int index=0;
    // for(int in:acceptedPoints)
    // acceptedPointsGrid.setPointValues(index++, points[in]);
    //
    // }

    public List<Integer> douglasPeucker(Point[] points, int startIndex,
        int endIndex) {
      mainSourceCode.highlight(0);
      mainSourceCode.unhighlight(15);
      mainSourceCode.unhighlight(16);
      mainSourceCode.unhighlight(14);
      mainSourceCode.unhighlight(17);

      tempGrid.setGridValue(1, 0, "" + startIndex);
      tempGrid.setGridValue(1, 1, "" + endIndex);

      Polyline indexLine = createLine(points[startIndex], points[endIndex],
          blackLineProp, "indexLine" + lineIndex++);
      pointsGrid.unhighlightAll();
      pointsGrid.unhighlightAllValue();
      pointsGrid.highlight(startIndex);
      pointsGrid.highlight(endIndex);
      tempGrid.setGridValue(1, 3, "0");
      step();

      double dmax = 0;
      mainSourceCode.highlight(0);
      mainSourceCode.highlight(0, 0, true);
      mainSourceCode.highlight(1);
      tempGrid.setGridValue(1, 2, "" + 0);
      step();

      int index = 0;

      mainSourceCode.unhighlight(1);
      mainSourceCode.highlight(2);
      step();

      mainSourceCode.unhighlight(2);
      mainSourceCode.highlight(4);
      step();
      DecimalFormat df = new DecimalFormat("#.#####");
      for (int i = startIndex + 1; i < endIndex; i++) {
        mainSourceCode.unhighlight(10);
        mainSourceCode.unhighlight(4);
        mainSourceCode.highlight(4, 0, true);
        mainSourceCode.highlight(5);

        pointsGrid.highlightValue(i);
        Polyline indexLine2 = createLine(points[startIndex], points[i],
            greenLineProp, "indexLine" + lineIndex++);
        Polyline indexLine3 = createLine(points[i], points[endIndex],
            greenLineProp, "indexLine" + lineIndex++);
        step();
        double distance = this.perpendicularDistance(points[startIndex],
            points[endIndex], points[i]);
        mainSourceCode.unhighlight(5);
        mainSourceCode.highlight(6);

        indexLine2.hide();
        indexLine3.hide();
        step();
        if (distance > dmax) {
          mainSourceCode.unhighlight(6);
          mainSourceCode.highlight(7);

          index = i;
          tempGrid.setGridValue(1, 2, "" + index);

          highlightPointElement(points[i]);
          unhighlightPointElement(points[index]);
          step();
          mainSourceCode.unhighlight(7);
          mainSourceCode.highlight(8);
          tempGrid.setGridValue(1, 3, "" + df.format(distance));
          step();
          dmax = distance;
          mainSourceCode.unhighlight(8);
          mainSourceCode.highlight(9);
          step();
        }

        mainSourceCode.unhighlight(6);
        mainSourceCode.unhighlight(9);
        mainSourceCode.highlight(10);
        pointsGrid.unhighlightValue(i);
        step();
      }
      mainSourceCode.unhighlight(4);
      mainSourceCode.unhighlight(10);
      mainSourceCode.highlight(11);
      step();
      List<Integer> pointsToKeep = new ArrayList<Integer>();
      mainSourceCode.unhighlight(11);
      mainSourceCode.highlight(12);
      indexLine.hide();
      step();
      if (dmax >= epsilon && index != 0) {
        mainSourceCode.unhighlight(12);
        mainSourceCode.highlight(13);
        step();
        pointsToKeep.add(index);
        mainSourceCode.unhighlight(13);
        mainSourceCode.highlight(14);
        acceptPointElement(points[index]);
        acceptedPointsGrid.setPointValues(accPointsIndex++, points[index]);
        step();
        pointsToKeep.addAll(douglasPeucker(points, startIndex, index));
        mainSourceCode.unhighlight(13);
        mainSourceCode.highlight(14);
        mainSourceCode.unhighlight(17);
        step();
        mainSourceCode.unhighlight(14);
        mainSourceCode.highlight(15);

        step();
        pointsToKeep.addAll(douglasPeucker(points, index, endIndex));
        mainSourceCode.unhighlight(14);
        mainSourceCode.highlight(15);
        mainSourceCode.unhighlight(17);
        step();
        mainSourceCode.unhighlight(15);
        mainSourceCode.highlight(16);

        step();

      } else {
        unhighlightPointElement(points[index]);
      }
      mainSourceCode.unhighlight(16);
      mainSourceCode.unhighlight(12);
      mainSourceCode.highlight(17);
      pointsGrid.unhighlightAll();
      pointsGrid.unhighlightAllValue();
      step();
      return pointsToKeep;
    }

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

    public void start(Point[] points) {
      // Create a new animation
      // name, author, screen width, screen height

      // Create input
      createHeader();
      step("Introdcution");

      createDescription();
      step("Algorithm");

      createCoordinatesAndPoints(Arrays.asList(points));
      createMainSourceCode();
      createPointArrays(points);
      step();

      // points[4]=dPeucker.new Point(-50,70);
      // points[5]=dPeucker.new Point(-70,-100);
      // points[6]=dPeucker.new Point(50,-70);
      // points[7]=dPeucker.new Point(-30,-40);
      // points[8]=dPeucker.new Point(0,0);
      List<Integer> resultIndex = new ArrayList<Integer>();
      resultIndex.addAll(douglasPeucker(points, 0, points.length - 1));

      step();
      resultIndex.add(0);
      resultIndex.add(points.length - 1);

      mainSourceCode.unhighlight(0);
      mainSourceCode.unhighlight(17);

      acceptPointElement(points[0]);
      acceptedPointsGrid.setPointValues(accPointsIndex++, points[0]);
      acceptPointElement(points[7]);
      acceptedPointsGrid.setPointValues(accPointsIndex++,
          points[points.length - 1]);
      step();

      lineStack = new Stack<Polyline>();
      Collections.sort(resultIndex);
      for (int index = 0; index < resultIndex.size() - 1; index++) {
        lineStack.add(createLine(points[resultIndex.get(index)],
            points[resultIndex.get(index + 1)], blackLineProp, "line" + index));
        acceptedPointsGrid
            .setPointValues(index, points[resultIndex.get(index)]);
      }
      acceptedPointsGrid.setPointValues(resultIndex.size() - 1,
          points[resultIndex.get(resultIndex.size() - 1)]);
      // points=grahamScan.grahamScan(points);
      // grahamScan.mapCoodinates(points);

      step();
      hideAll();
      step("Closing");
      createClosing();
    }
  }

}
