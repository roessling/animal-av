package generators.datastructures;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Timing;

public class TheFaultAlgorithmGenerator implements Generator {
  private Language         lang;
  private int              height;
  int              iterations;
  private int              delta;
  private int              width;
  private Color            lowColor;
  private Color            highColor;
  SquareProperties highlightSquareProperties;

  private int              screenwidth  = 1000;
  private int              screenheight = 800;

  public void init() {
    lang = new AnimalScript("The Fault Algorithm [EN]",
        "Daniel Thul, René Röpke", screenwidth, screenheight);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    height = (Integer) primitives.get("height");
    iterations = (Integer) primitives.get("iterations");
    delta = (Integer) primitives.get("delta");
    width = (Integer) primitives.get("width");
    lowColor = (Color) primitives.get("lowColor");
    highColor = (Color) primitives.get("highColor");
    highlightSquareProperties = (SquareProperties) props
        .getPropertiesByName("highlights");
    TheFaultAlgorithm algo = new TheFaultAlgorithm(lang, screenwidth,
        screenheight);
    algo.faultAlgorithm(width, height, iterations, delta, lowColor, highColor);
    return lang.toString();
  }

  public String getName() {
    return "The Fault Algorithm [EN]";
  }

  public String getAlgorithmName() {
    return "The Fault Algorithm";
  }

  public String getAnimationAuthor() {
    return "Daniel Thul, René Röpke";
  }

  public String getDescription() {
    return "The Fault Algorithm generates a kind of random number matrix that is suited to"
        + "\n"
        + "model things that can't be modeled through a truly random number matrix. A prominent"
        + "\n"
        + "example for this is using the number matrix as a height map that can be used as a"
        + "\n" + "randomly generated terrain in computer games.";
  }

  public String getCodeExample() {
    return new TheFaultAlgorithm().getCode();
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public class TheFaultAlgorithm {
    protected Language     lang;
    // private MatrixProperties props;
    private SourceCode     sourceCode;
    private Coordinates    sourceCodePos;
    private Coordinates    matrixPos;
    private final int      screenWidth;
    private final int      screenHeight;
    public final String    algo[]    = {
                                         "int[][] faultAlgorithm(int width, int height, int iterations, int delta) {",
                                         "	int[][] heights = new int[width][height];",
                                         "	while(iterations > 0) {",
                                         "		Point p1 = Point.getRandom(width, height);",
                                         "		Point p2 = Point.getRandom(width, height);",
                                         "		while(p1.equals(p2)) {",
                                         "			p2 = Point.getRandom(width, height);",
                                         "		}", "		Line l = new Line(p1, p2);",
                                         "		for(int y = 0; y < height; ++y) {",
                                         "			for(int x = 0; x < width; ++x) {",
                                         "				Point p3 = new Point(x, y);",
                                         "				if(l.getSide(p3) == LEFT) {",
                                         "					heights[x][y] -= delta;",
                                         "				}", "				else {",
                                         "					heights[x][y] += delta;",
                                         "				}", "			}", "		}",
                                         "		--iterations;", "	}",
                                         "	return heights;", "}" };
    public final String    preText[] = {
                                         "The Fault Algorithm can be used to produce a matrix of seemingly random",
                                         "numbers that is particularly suited to be used as a height map",
                                         "in the field of computer graphics.",
                                         "One can use the resulting matrix e.g. as a base for generating computationally",
                                         "generated landscapes.",
                                         "The algorithm is best visualised if you imagine an initally flat dot matrix floating",
                                         "in 3D space. This matrix of dots will iteratively be seperated in two halfs",
                                         "by a randomly generated line. The dots to one side of this seperating line",
                                         "will be raised by a small amount of height while the other half of the dots",
                                         "will be lowered respectively.",
                                         "After a specified number of these iterations the algorithm will return the",
                                         "resulting matrix that contains the final height of each dot.",
                                         "",
                                         "In the following animation the dot matrix will be shown from above with",
                                         "colors ranging from the high to the low color to indicate the current",
                                         "height of each dot." };
    private int            matrixCellSize;
    private SourceCode     preTextElem;
    private TextProperties infoTextProps;
    private Coordinates    infoTextPos;

    public String getCode() {
      StringBuilder code = new StringBuilder();
      for (String line : algo)
        code.append(line + "\n");
      return code.substring(0, code.length() - 1);
    }

    public TheFaultAlgorithm() {
      screenWidth = 880;
      screenHeight = 1720;
      lang = new AnimalScript("The Fault Algorithm", "Daniel Thul, René Röpke",
          screenWidth, screenHeight);
    }

    public TheFaultAlgorithm(Language lang, int screenWidth, int screenHeight) {
      // sourceCodePos = new Coordinates(10, 210);
      // matrixPos = new Coordinates(10, 10);
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;

      if (lang != null)
        this.lang = lang;
      else
        this.lang = new AnimalScript("The Fault Algorithm",
            "Daniel Thul, René Röpke", screenWidth, screenHeight);

      this.lang.setStepMode(true);

      /*
       * props = new MatrixProperties();
       * props.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 255,
       * 255)); props.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new
       * Color(0, 0, 0));
       * props.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, new Color(0,
       * 0, 0)); props.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new
       * Color(255, 255, 255)); props.set(AnimationPropertiesKeys.FILL_PROPERTY,
       * new Color(255, 255, 255));
       */

      infoTextProps = new TextProperties();
      infoTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(127,
          127, 255));

      preTextElem = initCode(preText);
      sourceCode = initCode(algo);
      sourceCode.hide();
      this.lang.newText(new Coordinates(screenWidth / 2 - 80, 10),
          "The Fault Algorithm", "title", null);
      try {
        preTextElem.moveTo(AnimalScript.DIRECTION_NW, "translate",
            new Coordinates(10, 50), null, new MsTiming(400));
      } catch (IllegalDirectionException e) {
        e.printStackTrace();
        return;
      }
      this.lang.nextStep("Introduction");
      preTextElem.hide();
    }

    /**
     * The core Fault Algorithm implementation augmented with Animal animation
     * code. Typically one would return a two-dimensional array of Integers as a
     * result of this algorithm but since it creates an Animal animation of
     * itself this is not necessary.
     * 
     * @param highColor
     * @param lowColor
     */
    public void faultAlgorithm(int width, int height, int initialIterations,
        int delta, Color lowColor, Color highColor) {
      iterations = initialIterations;
      calculateSizes(width, height);
      try {
        sourceCode.moveTo(AnimalScript.DIRECTION_NW, "translate",
            sourceCodePos, null, new MsTiming(0));
      } catch (IllegalDirectionException e1) {
        /*
         * Will absolutely never happen unless someone decides to take
         * AnimalScript.DIRECTION_NW out of the API.
         */
        e1.printStackTrace();
      }
      sourceCode.show();
      sourceCode.highlight(0);
      Text infoText = lang.newText(infoTextPos, String.format(
          "faultAlgorithm(%d, %d, %d, %d)", width, height, iterations, delta),
          "infoText", new MsTiming(0), infoTextProps);
      lang.nextStep();
      infoText.hide();
      sourceCode.unhighlight(0);
      sourceCode.highlight(1);
      infoText = lang.newText(infoTextPos,
          String.format("new int[%d][%d]", width, height), "infoText",
          new MsTiming(0), infoTextProps);
      MyMatrix m = new MyMatrix(matrixPos, matrixCellSize,
          new int[width][height], lowColor, highColor);
      lang.nextStep();
      infoText.hide();
      sourceCode.unhighlight(1);
      int counter = 1;
      while (showIterationCondition(iterations) && iterations > 0) {
        sourceCode.highlight(3);
        Point p1 = getRandomPoint(width, height);
        infoText = lang.newText(infoTextPos,
            String.format("p1 = new Point(%d, %d)", p1.x, p1.y), "infoText",
            new MsTiming(0), infoTextProps);
        m.highlight(p1);
        lang.nextStep(String.format("Iteration %d", counter));
        infoText.hide();
        sourceCode.unhighlight(3);
        sourceCode.highlight(4);
        Point p2 = getRandomPoint(width, height);
        infoText = lang.newText(infoTextPos,
            String.format("p2 = new Point(%d, %d)", p2.x, p2.y), "infoText",
            new MsTiming(0), infoTextProps);
        m.highlight(p2);
        lang.nextStep();
        infoText.hide();
        sourceCode.unhighlight(4);
        while (showPointsAreEqualCondition(p1, p2) && p1.equals(p2)) {
          sourceCode.highlight(6);
          m.unhighlight(p2);
          p2 = getRandomPoint(width, height);
          infoText = lang.newText(infoTextPos,
              String.format("p2 = new Point(%d, %d)", p2.x, p2.y), "infoText",
              new MsTiming(0), infoTextProps);
          m.highlight(p2);
          lang.nextStep();
          infoText.hide();
          sourceCode.unhighlight(6);
        }
        sourceCode.highlight(8);
        m.unhighlight(p1);
        m.unhighlight(p2);
        Line l = new Line(p1, p2);
        List<Point> pointsOnLine = getPointsOnLine(l, width, height);
        for (Point p : pointsOnLine)
          m.highlight(p);
        infoText = lang.newText(infoTextPos, String.format(
            "l = new Line( (%d, %d), (%d, %d) )", p1.x, p1.y, p2.x, p2.y),
            "infoText", new MsTiming(0), infoTextProps);
        lang.nextStep();
        infoText.hide();
        sourceCode.unhighlight(8);
        sourceCode.highlight(9);
        sourceCode.highlight(10);
        sourceCode.highlight(11);
        sourceCode.highlight(12);
        sourceCode.highlight(13);
        sourceCode.highlight(14);
        sourceCode.highlight(15);
        sourceCode.highlight(16);
        sourceCode.highlight(17);
        sourceCode.highlight(18);
        sourceCode.highlight(19);
        infoText = lang
            .newText(
                infoTextPos,
                "Points to one side of the line are raised while the others are lowered",
                "infoText", new MsTiming(0), infoTextProps);
        lang.nextStep();
        for (int y = 0; y < height; ++y) {
          for (int x = 0; x < width; ++x) {
            Point p3 = new Point(x, y);
            int elem = m.get(p3);
            if (l.getSide(p3) < 0) {
              elem -= delta;
            } else {
              elem += delta;
            }
            m.set(p3, elem);
          }
        }
        infoText.hide();
        m.colorize();
        for (Point p : pointsOnLine)
          m.unhighlight(p);
        lang.nextStep();

        sourceCode.unhighlight(9);
        sourceCode.unhighlight(10);
        sourceCode.unhighlight(11);
        sourceCode.unhighlight(12);
        sourceCode.unhighlight(13);
        sourceCode.unhighlight(14);
        sourceCode.unhighlight(15);
        sourceCode.unhighlight(16);
        sourceCode.unhighlight(17);
        sourceCode.unhighlight(18);
        sourceCode.unhighlight(19);
        sourceCode.highlight(20);
        --iterations;
        ++counter;
        infoText = lang.newText(infoTextPos,
            String.format("iterations = %d", iterations), "infoText",
            new MsTiming(0), infoTextProps);
        lang.nextStep();
        infoText.hide();
        sourceCode.unhighlight(20);
      }
      sourceCode.highlight(22);
      lang.nextStep();
      sourceCode.unhighlight(22);
      sourceCode.hide();
      showAfterText(m);
      lang.nextStep("Conclusion");
    }

    private void showAfterText(MyMatrix m) {
      int min = m.getMin();
      int max = m.getMax();
      String[] text = {
          String.format("Now that we have done %d iterations of", iterations),
          "the Fault Algorithm, we got our result matrix.", "",
          String.format("Its maximum value is %d, while its", max),
          String.format("minimum value is %d.", min), "",
          "The other colors shown are linearly interpolated",
          "values between the low and the high color." };
      SourceCode afterText = initCode(text);
      try {
        afterText.moveTo(AnimalScript.DIRECTION_NW, "translate", sourceCodePos,
            new MsTiming(0), new MsTiming(0));
      } catch (IllegalDirectionException e) {
        e.printStackTrace();
      }
    }

    private boolean showPointsAreEqualCondition(Point p1, Point p2) {
      sourceCode.highlight(5);
      Text infoText = lang.newText(infoTextPos, String.format(
          "(%d, %d).equals( (%d, %d) ): %b", p1.x, p1.y, p2.x, p2.y,
          p1.equals(p2)), "infoText", new MsTiming(0), infoTextProps);
      lang.nextStep();
      infoText.hide();
      sourceCode.unhighlight(5);
      return true;
    }

    private boolean showIterationCondition(int iterations) {
      sourceCode.highlight(2);
      Text infoText = lang.newText(infoTextPos,
          String.format("%d > 0: %b", iterations, iterations > 0), "infoText",
          new MsTiming(0), infoTextProps);
      lang.nextStep();
      infoText.hide();
      sourceCode.unhighlight(2);
      return true;
    }

    private void calculateSizes(int matrixCols, int matrixRows) {
      /*
       * Hacky and inaccurate way to determine the size that the source code
       * will take up. It would be nice to be able to query the actual size.
       */
      // Font font = (Font) sourceCode.getProperties().get("font");
      // FontRenderContext frc = new FontRenderContext(font.getTransform(),
      // true, true);
      // int textHeight = (int) (algo.length
      // * font.getLineMetrics("a", frc).getHeight() * 1.16);

      int availableHeightForMatrix = screenHeight - 10 /* margin top */
          - 30 /* title height */
          - 80 /* margin between title and matrix */
          // - 10 /* margin between matrix and source code */
          // - textHeight /* height of source code */
          - 10 /* margin bottom */;
      int availableWidthForMatrix = screenWidth - 10 /* margin left */
      - 10 /* margin right */
      - 380; /* approximate text width */
      int matrixCellSize_h = availableHeightForMatrix / matrixRows;
      int matrixCellSize_w = availableWidthForMatrix / matrixCols;
      matrixCellSize = Math.min(matrixCellSize_w, matrixCellSize_h);
      sourceCodePos = new Coordinates(10, 10 + 30 + 10 + 30);
      matrixPos = new Coordinates(380 + 10, 10 + 30 + 80);
      infoTextPos = new Coordinates(sourceCodePos.getX(),
          sourceCodePos.getY() - 30);
    }

    /**
     * Computes all points in a "width x height" big matrix that lie on line l.
     * 
     * @param l
     *          the line
     * @param width
     *          the width of the matrix
     * @param height
     *          the height of the matrix
     * @return all Points on the line
     */
    private List<Point> getPointsOnLine(Line l, int width, int height) {
      int xl = 0;
      int xr = width - 1;
      int yb = 0;
      int yt = height - 1;
      if (l.p1.x == l.p2.x) {
        Point p1 = new Point(l.p1.x, 0);
        Point p2 = new Point(l.p1.x, height - 1);
        return bresenham(p1, p2);
      }
      if (l.p1.y == l.p2.y) {
        Point p1 = new Point(0, l.p1.y);
        Point p2 = new Point(width - 1, l.p1.y);
        return bresenham(p1, p2);
      }
      // y = ax + b
      double a = ((double) (l.p2.y - l.p1.y)) / (l.p2.x - l.p1.x);
      double b = l.p1.y - a * l.p1.x;
      int tmp_y1 = (int) Math.round(a * 0 + b);
      int tmp_y2 = (int) Math.round(a * (width - 1) + b);
      // x = 1/a * y - b/a
      int tmp_x1 = (int) Math.round(1.0 / a * 0 - b / a);
      int tmp_x2 = (int) Math.round(1.0 / a * (height - 1) - b / a);

      xl = Math.max(Math.min(tmp_x1, tmp_x2), 0);
      xr = Math.min(Math.max(tmp_x1, tmp_x2), width - 1);
      yb = Math.max(Math.min(tmp_y1, tmp_y2), 0);
      yt = Math.min(Math.max(tmp_y1, tmp_y2), height - 1);

      Point p1;
      Point p2;

      if (a >= 0) {
        p1 = new Point(xl, yb);
        p2 = new Point(xr, yt);
      } else {
        p1 = new Point(xl, yt);
        p2 = new Point(xr, yb);
      }

      return bresenham(p1, p2);
    }

    /**
     * Standard Bresenham implementation taken from Wikipedia that computes all
     * Points that form a line between start and end (both included).
     * {@link https
     * ://en.wikipedia.org/wiki/Bresenham's_line_algorithm#Simplification}
     * 
     * @param start
     *          the start of the line
     * @param end
     *          the end of the line
     * @return all Points that make up the line
     */
    List<Point> bresenham(Point start, Point end) {
      int x1 = start.x;
      int y1 = start.y;
      int x2 = end.x;
      int y2 = end.y;

      List<Point> result = new ArrayList<Point>();

      int dx = Math.abs(x2 - x1);
      int dy = Math.abs(y2 - y1);

      int sx = (x1 < x2) ? 1 : -1;
      int sy = (y1 < y2) ? 1 : -1;

      int err = dx - dy;

      while (true) {
        result.add(new Point(x1, y1));

        if (x1 == x2 && y1 == y2) {
          break;
        }

        int e2 = 2 * err;

        if (e2 > -dy) {
          err = err - dy;
          x1 = x1 + sx;
        }

        if (e2 < dx) {
          err = err + dx;
          y1 = y1 + sy;
        }
      }

      return result;
    }

    /**
     * Creates an Animal SourceCode object out of a String array that contains
     * all source code lines.
     */
    protected SourceCode initCode(String text[]) {
      SourceCodeProperties scProps = new SourceCodeProperties();
      scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
          Font.PLAIN, 12));
      scProps.set(AnimationPropertiesKeys.INDENTATION_PROPERTY, 4);
      scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

      SourceCode sourceCode = lang.newSourceCode(new Coordinates(0, 0),
          "sourceCode", null, scProps);
      Pattern linePattern = Pattern.compile("(?<depth>\\t*)(?<code>.*)");
      for (String line : text) {
        Matcher matcher = linePattern.matcher(line);
        if (matcher.matches()) {
          int depth = matcher.group("depth").length();
          String code = matcher.group("code").trim();
          sourceCode.addCodeLine(code, null, depth, null);
        }
      }
      return sourceCode;
    }

    /**
     * Custom matrix implementation that draws a two-dimensional Integer array
     * as a grid of colored squares. The colors are automatically computed and
     * depend on the value of each cell where white means the maximum value,
     * black means the minimum value and grey a (linearly interpolated) value in
     * between.
     */
    class MyMatrix {
      int                            data[][];
      private Square[][]             squares;
      private Square[][]             highlights;
      private final SquareProperties standardProps = new SquareProperties();
      public final int               cellSize;
      public final int               rows;
      public final int               cols;
      private Color                  lowColor;
      private Color                  highColor;
      private Color                  middleColor;

      protected void setProps() {
        middleColor = new Color((lowColor.getRed() + highColor.getRed()) / 2,
            (lowColor.getGreen() + highColor.getGreen()) / 2,
            (lowColor.getBlue() + highColor.getBlue()) / 2);
        standardProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, middleColor);
        standardProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        standardProps.set(AnimationPropertiesKeys.FILL_PROPERTY, middleColor);
        standardProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
      }

      protected MyMatrix(Coordinates topLeft, int cellSize,
          int initial_data[][], Color lowColor, Color highColor) {
        data = initial_data.clone();
        this.cellSize = cellSize;
        this.rows = initial_data.length;
        this.cols = initial_data[0].length;
        this.lowColor = lowColor;
        this.highColor = highColor;
        setProps();
        squares = new Square[rows][cols];
        highlights = new Square[rows][cols];
        for (int y = 0; y < cols; ++y) {
          for (int x = 0; x < rows; ++x) {
            squares[x][y] = lang.newSquare(new Coordinates(x * cellSize
                + topLeft.getX(), y * cellSize + topLeft.getY()), cellSize,
                "square", null, standardProps);
            highlights[x][y] = lang.newSquare(new Coordinates(x * cellSize
                + topLeft.getX(), y * cellSize + topLeft.getY()), cellSize,
                "highlight", null, highlightSquareProperties);
          }
        }
      }

      protected void highlight(Point p) {
        highlight(p, new MsTiming(0));
      }

      protected void highlight(Point p, Timing t) {
        if (p.x >= rows || p.y >= cols) {
          throw new IndexOutOfBoundsException(
              "Can't highlight element that is out of bounds.");
        }
        highlights[p.x][p.y].show(t);
      }

      protected void unhighlight(Point p) {
        unhighlight(p, new MsTiming(0));
      }

      protected void unhighlight(Point p, Timing t) {
        if (p.x >= rows || p.y >= cols) {
          throw new IndexOutOfBoundsException(
              "Can't unhighlight element that is out of bounds.");
        }
        highlights[p.x][p.y].hide(t);
      }

      protected int getMin() {
        int min = Integer.MAX_VALUE;
        for (int y = 0; y < cols; ++y) {
          for (int x = 0; x < rows; ++x) {
            int tmp = data[x][y];
            if (tmp < min)
              min = tmp;
          }
        }
        return min;
      }

      protected int getMax() {
        int max = Integer.MIN_VALUE;
        for (int y = 0; y < cols; ++y) {
          for (int x = 0; x < rows; ++x) {
            int tmp = data[x][y];
            if (tmp > max)
              max = tmp;
          }
        }
        return max;
      }

      protected void colorize() {
        int min = getMin();
        int max = getMax();
        if (min == max) {
          // color everything grey
          for (int y = 0; y < cols; ++y) {
            for (int x = 0; x < rows; ++x) {
              squares[x][y].changeColor("fillColor", middleColor, new MsTiming(
                  0), new MsTiming(0));
            }
          }
          return;
        }
        // linearly interpolate between min (black) and max (white)
        float m = 1.0f / (max - min);
        float b = -min * m;
        for (int y = 0; y < cols; ++y) {
          for (int x = 0; x < rows; ++x) {
            float alpha = m * data[x][y] + b;
            Color interpColor = new Color((int) ((1 - alpha)
                * lowColor.getRed() + alpha * highColor.getRed()),
                (int) ((1 - alpha) * lowColor.getGreen() + alpha
                    * highColor.getGreen()), (int) ((1 - alpha)
                    * lowColor.getBlue() + alpha * highColor.getBlue()));
            squares[x][y].changeColor("fillColor", interpColor,
                new MsTiming(0), new MsTiming(0));
          }
        }
      }

      protected void set(Point p, int n) {
        if (p.x >= rows || p.y >= cols) {
          throw new IndexOutOfBoundsException(
              "Can't set element that is out of bounds.");
        }
        data[p.x][p.y] = n;
      }

      protected int get(Point p) {
        if (p.x >= rows || p.y >= cols) {
          throw new IndexOutOfBoundsException(
              "Can't set element that is out of bounds.");
        }
        return data[p.x][p.y];
      }
    }

    public class Line {
      public final Point p1;
      public final Point p2;

      public Line(Point p1, Point p2) {
        if (p1.equals(p2))
          throw new RuntimeException(
              "The two points of a line must be distinct.");
        this.p1 = p1;
        this.p2 = p2;
      }

      int getSide(Point p3) {
        int v12_x = p2.x - p1.x;
        int v12_y = p2.y - p1.y;

        int n12_x = v12_y;
        int n12_y = -v12_x;

        int v13_x = p3.x - p1.x;
        int v13_y = p3.y - p1.y;

        int p = (n12_x * v13_x) + (n12_y * v13_y);

        return p;
      }
    }

    public Point getRandomPoint(int max_x, int max_y) {
      Random rand = new Random();
      int x = rand.nextInt(max_x);
      int y = rand.nextInt(max_y);
      return new Point(x, y);
    }

    public class Point {
      public final int x;
      public final int y;

      public Point(int x, int y) {
        this.x = x;
        this.y = y;
      }

      @Override
      public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj)
          return true;
        if (obj == null)
          return false;
        if (getClass() != obj.getClass())
          return false;
        Point other = (Point) obj;
        if (x != other.x)
          return false;
        if (y != other.y)
          return false;
        return true;
      }
    }

  }

}