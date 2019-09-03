package animalscript.extensions;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import animal.animator.ColorChanger;
import animal.animator.Move;
import animal.animator.SetFont;
import animal.animator.TimedShow;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.graphics.PTStringMatrix;
import animal.misc.MessageDisplay;
import animalscript.core.BasicParser;

/**
 * This class provides methods to build and modify grafical Elements representing
 * a Grid, based on attributes like number of columns, style or color.
 * Parsing-concerns are done in GridSupport and calculations in GridMath. The Task
 * of GridProducer is to manage creation and registration of graphical objects and
 * animators as well as keeping the objectProperties "up to date".
 * <br>
 * Its methods match the parsingmethods of GridSupport, so when ever a command is
 * parsed, there is a corresponding method in GridProduce which "knows how to do it".
 * 
 * @author <a href="mailto:here@christoph-preisser.de">Christoph Prei&szlig;er</a>
 * @version 1.00
 * @date 2007-01-25 
 */
/**
 * @author hmz
 * 
 */
public class MatrixProducer extends BasicParser {
  // grid <id> <nodeDefinition> [lines <n>] [colums <n>]
  // [cellwidth <n>] [maxcellwidth <n>] [fixedcellsize]
  // [color <color>] [textcolor <color>] [fillcolor <color>]
  // [highlight[text]color <color>] [highlightfillcolor <color>]
  // [matrixstyle|tablestyle|junctions]
  // [depth <n>] [delay <n>] [duration <n>]

  // static aliases (used in switches, public for use in methods)
  public static final int STYLE_PLAIN = 0;

  public static final int STYLE_MATRIX = 1;

  public static final int STYLE_TABLE = 2;

  // for further development
  // public static final int ALIGN_TOP_LEFT = 100;
  // public static final int ALIGN_TOP_CENTER = 101;
  // public static final int ALIGN_TOP_RIGHT = 102;

  public static final int ALIGN_LEFT = 110;

  public static final int ALIGN_CENTER = 111;

  public static final int ALIGN_RIGHT = 112;

  // for further development
  // static final int ALIGN_BOTTOM_LEFT = 120;
  // static final int ALIGN_BOTTOM_CENTER = 121;
  // static final int ALIGN_BOTTOM_RIGHT = 122;

  // these are NOT the default-values. These statics should be used
  // in method-calls, if GridProducer should set a default itself
  public static final int DEFAULT_INT = -1; // DO NOT CHANGE! else
                                            // cellidentification will fail

  public static final Color DEFAULT_COLOR = null;

  public static final Font DEFAULT_FONT = null;

  public static final String DEFAULT_UNIT = null;

  // Values depending on chosen style (such as borders, colors, etc)
  // are set in "makeGrid(...)"

  // Hashtable-Keys for Grid Properties -- gridspecific
  private static final String gridLocationKey(String gridName) {
    return gridName + ".location";
  }

  private static final String gridStyleKey(String gridName) {
    return gridName + ".style";
  }

  private static final String gridSizeKey(String gridName) {
    return gridName + ".size";
  }

//  private static final String gridDepthKey(String gridName) {
//    return gridName + ".depth";
//  }

  private static final String gridCellWidthKey(String gridName) {
    return gridName + ".cellWidth";
  }

  private static final String gridMaxCellWidthKey(String gridName) {
    return gridName + ".maxCellWidth";
  }

  private static final String gridCellHeightKey(String gridName) {
    return gridName + ".cellHeight";
  }

  private static final String gridMaxCellHeightKey(String gridName) {
    return gridName + ".maxCellHeight";
  }

  // Hashtable-Keys for Grid Properties -- cellspecific
  private static final String gridTextKey(String gridName, int line, int column) {
    return gridName + ".cell[" + line + "][" + column + "].text";
  }

  private static final String gridTextStringKey(String gridName, int line,
      int column) {
    return gridName + ".cell[" + line + "][" + column + "].text.string";
  }

  private static final String gridTextFontKey(String gridName, int line,
      int column) {
    return gridName + ".cell[" + line + "][" + column + "].text.font";
  }

  private static final String gridTextPositionKey(String gridName, int line,
      int column) {
    return gridName + ".cell[" + line + "][" + column + "].text.position";
  }

  private static final String gridBackKey(String gridName, int line, int column) {
    return gridName + ".cell[" + line + "][" + column + "].background";
  }

  private static final String gridBackPositionKey(String gridName, int line,
      int column) {
    return gridName + ".cell[" + line + "][" + column + "].background.position";
  }

  private static final String gridBackSizeKey(String gridName, int line,
      int column) {
    return gridName + ".cell[" + line + "][" + column + "].background.size";
  }

  private static final String gridBracketKey(String gridName, int part) {
    return gridName + ".bracket[" + part + "]";
  }

  private static final String gridBracketPositionKey(String gridName, int part) {
    return gridName + ".bracket[" + part + "].position";
  }

  private static final String gridTextAlignmentKey(String gridName, int line,
      int column) {
    return gridName + ".cell[" + line + "][" + column + "].text.alignment";
  }

  private static final String gridTextColorKey(String gridName, int line,
      int column) {
    return gridName + ".cell[" + line + "][" + column + "].textColor";
  }

  private static final String gridHighlightTextColorKey(String gridName,
      int line, int column) {
    return gridName + ".cell[" + line + "][" + column + "].hlTextColor";
  }

  private static final String gridFillColorKey(String gridName, int line,
      int column) {
    return gridName + ".cell[" + line + "][" + column + "].fillColor";
  }

  private static final String gridHighlightFillColorKey(String gridName,
      int line, int column) {
    return gridName + ".cell[" + line + "][" + column + "].hlFillColor";
  }

  private static final String gridBorderColorKey(String gridName, int line,
      int column) {
    return gridName + ".cell[" + line + "][" + column + "].borderColor";
  }

  private static final String gridHighlightBorderColorKey(String gridName,
      int line, int column) {
    return gridName + ".cell[" + line + "][" + column + "].hlBorderColor";
  }

  /**
   * Utility method to collect equal move animations.
   * 
   * @param objectID
   *          specifies the object wich should be moved
   * @param dx
   *          is the (relative) movement in x
   * @param dy
   *          is the (relative) movement in x
   * @param objects
   *          is a hashtable of vectors of objectIDs separated by parameters
   *          used as key.
   */
  private static void collectObjectMovements(int objectID, int dx, int dy,
      Hashtable<String, Vector<Integer>> objects) {
    if ((dx == 0) && (dy == 0))
      return;
    Vector<Integer> objectsToMove = new Vector<Integer>();

    if (objects.containsKey(dx + "," + dy)) {
      // moveop ist bereits vorhanden -> object ID zur hastable der zu
      // bewegenden objecte hinzufuegen
      // System.out.println("Diese MoveOp gibt es schon.\n ich fuege ID
      // "+objectID+" in den Vector unter '"+dx+","+dy+"' ein");
      objectsToMove = objects.get(dx + "," + dy);
    }
    objectsToMove.add(new Integer(objectID));
    objects.put(dx + "," + dy, objectsToMove);
  }

  /**
   * Builds a Grid. Parameters control number of lines and columns, style (<b>plain</b>
   * without borders, <b>table</b> with borders and <b>matrix</b> with
   * brackets). Parameters which are supposed to be default, should be set to
   * DEFAULT_INT, DEFAULT_COLOR or DEFAULT_UNIT.
   * 
   * @param gridName
   *          Name of the grid as String. Used internally - its not a caption.
   * @param basePoint
   *          Location of the upper left corner. Consider that matrices are 20
   *          pixels bigger in height and width, as they have brackets.
   * @param lines
   *          The Number of lines. This cant be changed after creation. Instead
   *          of adding lines, you could hide them first.
   * @param cols
   *          The Number of columns. This cant be changed after creation.
   *          Instead of adding columns, you could hide them first.
   * @param cellWidth
   *          The minimum width of every cell in Pixels.
   * @param maxCellWidth
   *          The maximum width of every cell in Pixels. If zero, no limit is
   *          set.
   * @param cellHeight
   *          The minimum height of every cell in Pixels.
   * @param maxCellHeight
   *          The maximum height of every cell in Pixels. If zero, default is
   *          used.
   * @param fixedCellSize
   *          Sets maximum sizes to initial sizes.
   * @param genericColor
   *          This parameter is a fallback for the color attribute of the
   *          animalscript command for gridcreation. Its used only, if no other
   *          color parameter is set. Its recommendet to avoid the use of this
   *          parameter (leaving it null), as the other color parameters allow a
   *          more particular control of gridcomponents.
   * @param textColor
   *          Color of texts.
   * @param fillColor
   *          Color of backgroundpolygons behind the texts.
   * @param borderColor
   *          Color of cellborders.
   * @param hTextColor
   *          Color of texts in highlighted cells.
   * @param hFillColor
   *          Color of backgroundpolygons behind highlighted cells.
   * @param hBorderColor
   *          Color of borders of highlighted cells.
   * @param style
   *          Style coded as Integer: 0 = plain, 1 = matrix, 2 = table. This
   *          parameter controls weather matrixbrackets are shown and effects
   *          defaultvalues like colors. In default plain grids are black text
   *          on white background. Matrices are blue text and brackets on white
   *          background. Tables are black text on gray background with darker
   *          gray borders.
   * @param font
   *          Used font for texts.
   * @param alignment
   *          alignment of texts. Use GridProducer.ALIGN_***
   * @param depth
   *          depth of the grid. Backgroundelements lay in depth-1.
   * @param delay
   *          Time before the grid is shown.
   * @param duration
   *          This has no effect.
   */
  public static void makeGrid(String gridName, Point basePoint, int lines,
      int cols, int cellWidth, int maxCellWidth, int cellHeight,
      int maxCellHeight, boolean fixedCellSize, Color genericColor,
      Color textColor, Color fillColor, Color borderColor, Color hTextColor,
      Color hFillColor, Color hBorderColor, int style, Font font,
      int alignment, int depth, int delay, int duration, String unit) {
    PTStringMatrix matrix = new PTStringMatrix(lines, cols);
    matrix.setLocation(basePoint);
    matrix.setColor(genericColor);
    matrix.setDepth(depth);
    matrix.setFillColor(fillColor);
    matrix.setFilled(true);
    matrix.setFont(font);
    matrix.setHighlightColor(hFillColor);
    
    matrix.setTextColor(textColor);
    BasicParser.addGraphicObject(matrix, anim);
    getObjectIDs().put(gridName, matrix.getNum(false));
    System.err.println(getObjectIDs().getIntProperty(gridName));
    TimedShow showMatrix = new TimedShow(currentStep, matrix.getNum(false), 
        duration, "show", delay >= 0);
    showMatrix.setOffset(delay);
    if (unit == null)
      unit = "ticks";
    showMatrix.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
    matrix.setElementAt(0, 0, "Hello World");
    matrix.setElementAt(0, 1, "256");
    BasicParser.addAnimatorToAnimation(showMatrix, anim);
    return;
 /*   
     * Most likely, not all attributes are defined by the user. These are filled
     * with DEFAULT_XYZ, so we have to assign their defaults here. The defaults
     * depend on the style, so the style is checked first. The meaning of each
     * attribute should be selfexplainatory, but "genericColor" could be a
     * confusing fallback: Its value depends on the keyword "color" in
     * Animalscript, wich is supported for consitencially reasons only. It only
     * has an effect, if we have to fall back on defaultvalues, but the user
     * used "color". In this case (depending on style) this color is applied to
     * suitable parts of the grid. e.g. in a matrix the text and brackets are
     * colored like specified.
     
    if (style == DEFAULT_INT)
      style = STYLE_PLAIN; // Default style

    if (depth == DEFAULT_INT)
      depth = 5;
    if (delay == DEFAULT_INT)
      delay = 0;
    if (duration == DEFAULT_INT)
      duration = 0;
    if (unit == DEFAULT_UNIT)
      unit = "ms";
    if (fixedCellSize) {
      maxCellWidth = cellWidth;
      maxCellHeight = cellHeight;
    }
    switch (style) {
    case STYLE_PLAIN:
      if (cellWidth == DEFAULT_INT) {
        cellWidth = 20;
      }
      if (maxCellWidth == DEFAULT_INT) {
        maxCellWidth = 0;
      }
      if (cellHeight == DEFAULT_INT) {
        cellHeight = 10;
      }
      if (maxCellHeight == DEFAULT_INT) {
        maxCellHeight = 0;
      }
      if (genericColor == DEFAULT_COLOR) {
        genericColor = new Color(0.0f, 0.0f, 0.0f);
      }
      if (textColor == DEFAULT_COLOR) {
        textColor = genericColor;
      }
      if (fillColor == DEFAULT_COLOR) {
        fillColor = new Color(1.0f, 1.0f, 1.0f);
      }
      if (borderColor == DEFAULT_COLOR) {
        borderColor = fillColor;
      }
      if (hTextColor == DEFAULT_COLOR) {
        hTextColor = new Color(0.0f, 0.0f, 0.0f);
      }
      if (hFillColor == DEFAULT_COLOR) {
        hFillColor = new Color(1.0f, 1.0f, 0.5f);
      }
      if (hBorderColor == DEFAULT_COLOR) {
        hBorderColor = hFillColor;
      }
      if (alignment == DEFAULT_INT) {
        alignment = ALIGN_LEFT;
      }
      break;
    case STYLE_TABLE:
      if (cellWidth == DEFAULT_INT) {
        cellWidth = 20;
      }
      if (maxCellWidth == DEFAULT_INT) {
        maxCellWidth = 0;
      }
      if (cellHeight == DEFAULT_INT) {
        cellHeight = 10;
      }
      if (maxCellHeight == DEFAULT_INT) {
        maxCellHeight = 0;
      }
      if (genericColor == DEFAULT_COLOR)
        genericColor = new Color(0.0f, 0.0f, 0.0f);
      if (textColor == DEFAULT_COLOR)
        textColor = genericColor;
      if (fillColor == DEFAULT_COLOR)
        fillColor = new Color(0.8f, 0.8f, 0.8f);
      if (borderColor == DEFAULT_COLOR)
        borderColor = new Color(0.5f, 0.5f, 0.5f);
      if (hTextColor == DEFAULT_COLOR)
        hTextColor = new Color(0.0f, 0.0f, 0.0f);
      if (hFillColor == DEFAULT_COLOR)
        hFillColor = new Color(1.0f, 1.0f, 0.8f);
      if (hBorderColor == DEFAULT_COLOR)
        hBorderColor = new Color(1.0f, 1.0f, 0.5f);
      if (alignment == DEFAULT_INT) {
        alignment = ALIGN_LEFT;
      }
      break;
    case STYLE_MATRIX:
      if (cellWidth == DEFAULT_INT)
        cellWidth = 15;
      if (maxCellWidth == DEFAULT_INT)
        maxCellWidth = 0;
      if (cellHeight == DEFAULT_INT)
        cellHeight = 15;
      if (maxCellHeight == DEFAULT_INT)
        maxCellHeight = 0;
      if (genericColor == DEFAULT_COLOR)
        genericColor = new Color(0.0f, 0.0f, 1.0f);
      if (textColor == DEFAULT_COLOR)
        textColor = genericColor;
      if (fillColor == DEFAULT_COLOR)
        fillColor = new Color(1.0f, 1.0f, 1.0f);
      if (borderColor == DEFAULT_COLOR)
        borderColor = new Color(1.0f, 1.0f, 1.0f);
      if (hTextColor == DEFAULT_COLOR)
        hTextColor = new Color(0.0f, 0.0f, 0.5f);
      if (hFillColor == DEFAULT_COLOR)
        hFillColor = new Color(0.75f, 0.75f, 1.0f);
      if (hBorderColor == DEFAULT_COLOR)
        hBorderColor = new Color(0.5f, 0.5f, 0.75f);
      if (alignment == DEFAULT_INT) {
        alignment = ALIGN_CENTER;
      }
      break;
    }// end switch

    
     * to create an empty grid, we first have to know the width of each columns
     * and the height of each line, wich is quite simple, because every cell is
     * equal (empty). Nontheless we want to create a two Arrays in wich all
     * widths and heights are stored. This arrays are needed later for
     * ("calculate***position") methods wich calculate the positions of each
     * cell, its content and so on. For these methods its no difference weather
     * the grid is just created (and empty) or already filled (reuse of the
     * Code).
     
    int[] widths = new int[cols];
    int[] heights = new int[lines];
    GridMath.calculateColumnWidths(widths, cellWidth, font);
    GridMath.calculateLineHeights(heights, cellHeight, font);

    
     * Depending on sizes an chosen layout, the positions of the graphical
     * Elements can be calculated We start with the textObjects, so now its the
     * right time, to create them ;) The Creation of each text consists of
     * several steps - create the Object itself - assign an Identifier (<gridname>.cell[l][c].text) -
     * put its Identifier into "getObjectTypes" - assign to the animator
     * ("anim") - get a numeric Identifier (getNum()), and collect all of them.
     * collecting them, means to know how long the OID-Array has to be. While
     * generating the objects we store this info in "OIDAlength", which will be
     * increased according to the chosen size and style.
     
    int OIDAlength = 0;

    // ########## Generation of Objects ##############

    // ++++++++++ generate texts and calculate widths & heights ++++++++++++

    // we need lines*cols text-objects, and therefore the same number of OIDs:
    OIDAlength += (lines * cols);

    PTText[][] values = new PTText[lines][cols]; // A 2d-Array for the content
                                                  // of the grid
    int[][] alignments = new int[lines][cols];
    for (int line = 0; line < lines; line++)
      for (int col = 0; col < cols; col++) {
        alignments[line][col] = alignment;
        values[line][col] = new PTText();
        values[line][col].setFont(font);
        values[line][col].setDepth(depth);
        values[line][col].setColor(textColor);
        values[line][col].setText("");// ("+line + "|" +col+")");
        values[line][col].setObjectName(gridTextKey(gridName, line, col));
      }

    // now we calculate and set the positions of the text-Elements
    GridMath.calculateTextPositions(values, alignments, widths, heights,
        basePoint, style);

    // ++++++++++ generate brackets, if needed ++++++++++++
    PTGraphicObject[] brackets = new PTGraphicObject[6];
    if (style == STYLE_MATRIX) {
      // we need six elements to build the brackets (four arcs and two lines)
      OIDAlength += 6;
      GridMath.calculateBracketPositions(brackets, widths, heights, basePoint,
          style);
      for (int part = 0; part < 6; part++) {
        brackets[part].setColor(textColor);
        brackets[part].setDepth(depth);
        brackets[part].setObjectName(gridBracketKey(gridName, part));
//        brackets[part].setFwArrow(false);
      }

    }

    // ++++++++++++ Generate Background Elements +++++++++++++
    int[][][] bPos = new int[lines][cols][4];
    PTPolygon[][] backgroundRects = new PTPolygon[lines][cols];
    // We need as many background-elements as we have cells in our grid:
    OIDAlength += lines * cols;
    PTPolygon currentPL;
    GridMath.calculateBackgroundPositions(bPos, widths, heights, basePoint,
        style);
    
    for (int col = 0; col < cols; col++)
      for (int line = 0; line < lines; line++) {
        int nodeNr = 0;
        currentPL = new PTPolygon();
        // currentPL.setClosed(true);
        currentPL.setFilled(true);
        currentPL.setFillColor(fillColor);
        currentPL.setColor(borderColor);
        currentPL.setDepth(depth + 1);
        currentPL.setObjectName(gridBackKey(gridName, line, col));

        // a bPos[line][column] gives us {x,y,width,height}
        PTPoint a, b, c, d;
        a = new PTPoint(bPos[line][col][0], bPos[line][col][1]);
        b = new PTPoint(bPos[line][col][0], bPos[line][col][1]
            + bPos[line][col][3]);
        c = new PTPoint(bPos[line][col][0] + bPos[line][col][2],
            bPos[line][col][1] + bPos[line][col][3]);
        d = new PTPoint(bPos[line][col][0] + bPos[line][col][2],
            bPos[line][col][1]);
        currentPL.setNode(nodeNr++, a);
        currentPL.setNode(nodeNr++, b);
        currentPL.setNode(nodeNr++, c);
        currentPL.setNode(nodeNr++, d);
        backgroundRects[line][col] = currentPL;
      } // end bgrects-loop

    // ############ Registration of Objects ##############

    // ++++++++++ Register the text- and bgobjects +++++++
    int[] gridOIDs = new int[OIDAlength];
    int currentOID = 0;

    for (int line = 0; line < lines; line++)
      for (int col = 0; col < cols; col++) {
        BasicParser.addGraphicObject(values[line][col], anim);
        getObjectTypes().put(gridTextKey(gridName, line, col),
            getTypeIdentifier("text"));
        getObjectIDs().put(gridTextKey(gridName, line, col),
            values[line][col].getNum(true));
        getObjectProperties().put(gridTextAlignmentKey(gridName, line, col),
            alignment);
        getObjectProperties().put(gridTextStringKey(gridName, line, col),
            values[line][col].getText());
        getObjectProperties().put(gridTextFontKey(gridName, line, col), font);
        getObjectProperties().put(gridTextPositionKey(gridName, line, col),
            values[line][col].getLocation());
        getObjectProperties().put(
            gridHighlightTextColorKey(gridName, line, col), hTextColor);
        getObjectProperties().put(gridTextColorKey(gridName, line, col),
            textColor);
        gridOIDs[currentOID++] = values[line][col].getNum(true);

        BasicParser.addGraphicObject(backgroundRects[line][col], anim);
        getObjectTypes().put(gridBackKey(gridName, line, col),
            getTypeIdentifier("Polyline"));
        getObjectIDs().put(gridBackKey(gridName, line, col),
            backgroundRects[line][col].getNum(true));
        getObjectProperties().put(gridBackPositionKey(gridName, line, col),
            backgroundRects[line][col].getLocation());
        int[] boxSize = new int[2];
        boxSize[0] = backgroundRects[line][col].getNodeAsPoint(2).x
            - backgroundRects[line][col].getLocation().x;
        boxSize[1] = backgroundRects[line][col].getNodeAsPoint(2).x
            - backgroundRects[line][col].getLocation().y;
        getObjectProperties()
            .put(gridBackSizeKey(gridName, line, col), boxSize);
        getObjectProperties().put(gridBorderColorKey(gridName, line, col),
            borderColor);
        getObjectProperties().put(gridFillColorKey(gridName, line, col),
            fillColor);
        getObjectProperties().put(
            gridHighlightBorderColorKey(gridName, line, col), hBorderColor);
        getObjectProperties().put(
            gridHighlightFillColorKey(gridName, line, col), hFillColor);
        gridOIDs[currentOID++] = backgroundRects[line][col].getNum(true);
      }

    // ++++++++++++ Register the Brackets ++++++++++++++
    if (style == STYLE_MATRIX) { // loop not practical, because of different
                                  // TypeIdentifiers...
      // left bracket top arc
      BasicParser.addGraphicObject(brackets[0], anim);
      getObjectTypes().put(gridBracketKey(gridName, 0),
          getTypeIdentifier("Arc"));
      getObjectIDs().put(gridBracketKey(gridName, 0), brackets[0].getNum(true));
      getObjectProperties().put(gridBracketPositionKey(gridName, 0),
          ((PTOpenCircleSegment) brackets[0]).getCenter());
      gridOIDs[currentOID++] = brackets[0].getNum(true);

      // left bracket middle part
      BasicParser.addGraphicObject(brackets[1], anim);
      getObjectTypes().put(gridBracketKey(gridName, 1),
          getTypeIdentifier("Polyline"));
      getObjectIDs().put(gridBracketKey(gridName, 1), brackets[1].getNum(true));
      getObjectProperties().put(gridBracketPositionKey(gridName, 1),
          brackets[1].getLocation());
      gridOIDs[currentOID++] = brackets[1].getNum(true);

      // left bracket bottom arc
      BasicParser.addGraphicObject(brackets[2], anim);
      getObjectTypes().put(gridBracketKey(gridName, 2),
          getTypeIdentifier("Arc"));
      getObjectIDs().put(gridBracketKey(gridName, 2), brackets[2].getNum(true));
      getObjectProperties().put(gridBracketPositionKey(gridName, 2),
          ((PTOpenCircleSegment) brackets[2]).getCenter());
      gridOIDs[currentOID++] = brackets[2].getNum(true);

      // right bracket top arc
      BasicParser.addGraphicObject(brackets[3], anim);
      getObjectTypes().put(gridBracketKey(gridName, 3),
          getTypeIdentifier("Arc"));
      getObjectIDs().put(gridBracketKey(gridName, 3), brackets[3].getNum(true));
      getObjectProperties().put(gridBracketPositionKey(gridName, 3),
          ((PTOpenCircleSegment) brackets[3]).getCenter());
      gridOIDs[currentOID++] = brackets[3].getNum(true);

      // right bracket middle part
      BasicParser.addGraphicObject(brackets[4], anim);
      getObjectTypes().put(gridBracketKey(gridName, 4),
          getTypeIdentifier("Polyline"));
      getObjectIDs().put(gridBracketKey(gridName, 4), brackets[4].getNum(true));
      getObjectProperties().put(gridBracketPositionKey(gridName, 4),
          brackets[4].getLocation());
      gridOIDs[currentOID++] = brackets[4].getNum(true);

      // right bracket bottom arc
      BasicParser.addGraphicObject(brackets[5], anim);
      getObjectTypes().put(gridBracketKey(gridName, 5),
          getTypeIdentifier("Arc"));
      getObjectIDs().put(gridBracketKey(gridName, 5), brackets[5].getNum(true));
      getObjectProperties().put(gridBracketPositionKey(gridName, 5),
          ((PTOpenCircleSegment) brackets[5]).getCenter());
      gridOIDs[currentOID++] = brackets[5].getNum(true);
    }

    // ++++++++++++ Register some gridattributes ++++++++++++++
    getObjectProperties().put(gridLocationKey(gridName), basePoint);
    getObjectProperties().put(gridSizeKey(gridName), new int[] { lines, cols });
    getObjectProperties().put(gridDepthKey(gridName), depth);
    getObjectProperties().put(gridCellWidthKey(gridName), cellWidth);
    getObjectProperties().put(gridCellHeightKey(gridName), cellHeight);
    getObjectProperties().put(gridMaxCellWidthKey(gridName), maxCellWidth);
    getObjectProperties().put(gridMaxCellHeightKey(gridName), maxCellHeight);
    getObjectProperties().put(gridStyleKey(gridName), style);

    // at last, we need to create the "showeffect"
    getObjectIDs().put(gridName, gridOIDs);
    TimedShow showFX = new TimedShow(currentStep, gridOIDs, duration, "show",
        delay >= 0);
    showFX.setOffset(delay);
    showFX.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
    BasicParser.addAnimatorToAnimation(showFX, anim);*/
  }

  /**
   * Modifies the content of a single gridcell, a column, a line or the whole
   * grid. Modifing means updating the objectProperties Hashtable and creating
   * an setValueAnimator for the visual update if refresh is set to true.
   * 
   * @param gridName
   *          Name of the grid
   * @param line
   *          number of the line the cell is in. Numbering starts with zero. If
   *          the parameter is set to -1, all lines are selected, which means a
   *          whole column or the whole grid will be affected.
   * @param column
   *          number of the column the cell is in. Numbering starts with zero.
   *          If the parameter is set to -1, all columns are selected, which
   *          means a whole line or the whole grid will be affected.
   * @param value
   *          The value the cell(s) should be filled with
   * @param step
   *          The step in which the change happens
   * @param duration
   *          duration of the change (affects created setText)
   * @param offset
   *          Time before the change happens (affects created setText)
   * @param unit
   *          The unit duration and offset are measured in: "ticks" or "ms"
   * @param refresh
   *          If set to true refreshgrid() is called. The Grid will be
   *          recalculated and necessary resizing is made. As this can result in
   *          many Move-Animators, this parameter should not be set, if
   *          setGridValue is called right again another time.
   */
  public static void setGridValue(String gridName, int line, int column,
      String value, int step, int duration, int offset, String unit,
      boolean refresh) {
    //GR
    int gridID = getObjectIDs().getIntProperty(gridName);
    PTGraphicObject ptgo = animState.getCloneByNum(gridID);
    if (!(ptgo instanceof PTStringMatrix))
      return;
    
    PTStringMatrix matrix = (PTStringMatrix)ptgo;
    
    // assure that size is OK
    if (matrix.getMaxColumnCount() >= column && matrix.getRowCount() >= line) {
      matrix.setElementAt(line, column, value);
    }
 /*   // check if coordinates are valid
    int[] size = getObjectProperties().getIntArrayProperty(
        gridSizeKey(gridName));
    if ((line >= size[0]) || (column >= size[1])) {
      MessageDisplay.errorMsg("Grid index " + gridName + "[" + line + "]["
          + column + "] is out of bounds. Valid cells are [0.." + size[0]
          + "][0.." + size[1], MessageDisplay.RUN_ERROR);
      return;
    }

    // Collect all IDs of textobjects, we have to update.
    // Update of property-hashtable is done immediatly
    int[] cellValuesToUpdate;
    if (line == DEFAULT_INT)
      if (column == DEFAULT_INT) { // whole grid - loop over lines and columns
                                    // #####################
        cellValuesToUpdate = new int[size[0] * size[1]];
        for (int l = 0; l < size[0]; l++)
          for (int c = 0; c < size[1]; c++) {
            cellValuesToUpdate[l * size[1] + c] = getObjectIDs()
                .getIntProperty(gridTextKey(gridName, l, c));
            // update value in property-Hashtable
            getObjectProperties().put(gridTextStringKey(gridName, l, c), value);
          }// end loop
      }

      else { // (l<0 & c>0) #### whole column -> loop over lines
              // #################################
        cellValuesToUpdate = new int[size[0]];
        for (int l = 0; l < size[0]; l++) {
          cellValuesToUpdate[l] = getObjectIDs().getIntProperty(
              gridTextKey(gridName, l, column));
          // update value in property-Hashtable
          getObjectProperties().put(gridTextStringKey(gridName, l, column),
              value);
        }// end loop
      }

    else if (column == DEFAULT_INT) {// & (l>0) whole line -> loop over columns
                                      // #############################
      cellValuesToUpdate = new int[size[1]];
      for (int c = 0; c < size[1]; c++) {
        cellValuesToUpdate[c] = getObjectIDs().getIntProperty(
            gridTextKey(gridName, line, c));
        // update value in property-Hashtable
        getObjectProperties().put(gridTextStringKey(gridName, line, c), value);
      }
    } else { // (l>0 & c>0) ############ just one cell
              // ############################################
      cellValuesToUpdate = new int[1];
      cellValuesToUpdate[0] = getObjectIDs().getIntProperty(
          gridTextKey(gridName, line, column));
      // update value in property-Hashtable
      getObjectProperties().put(gridTextStringKey(gridName, line, column),
          value);
    }

    // Now the property-hashtable is up-to-date. Create the Animator
    SetText setValueAnimator = new SetText(step, cellValuesToUpdate, duration,
        offset, unit, value);
    BasicParser.addAnimatorToAnimation(setValueAnimator, anim);

    // now we're done... ...concerning the value(s).
    // The grid could be corrupted, a refresh is needed:
    if (refresh)
      MatrixProducer.refreshGrid(gridName, step, duration, offset, unit);*/
  }// end of setgridvalue

  /**
   * Modifies the color of a single gridcell, a column, a line or the whole
   * grid. Modifing means updating the objectProperties Hashtable and for
   * non-highlightcolors a colorchange as well (Therefor a ColorChange animator
   * is created). As future improvements, colorchanges should depend on whether
   * the cell is highlighted or not. A color is not modified, if the
   * corresponding parameter is set to GridProducer.DEFAULT_COLOR.
   * 
   * @param gridName
   *          Name of the grid
   * @param line
   *          number of the line the cell is in. Numbering starts with zero. If
   *          the parameter is set to -1, all lines are selected, which means a
   *          whole column or the whole grid will be affected.
   * @param column
   *          number of the column the cell is in. Numbering starts with zero.
   *          If the parameter is set to -1, all columns are selected, which
   *          means a whole line or the whole grid will be affected.
   * @param genericColor
   *          This parameter is a fallback for the color attribute of the
   *          animalscript command for gridcreation. Its used only, if no other
   *          color parameter is set. Its recommendet to avoid the use of this
   *          parameter, as the other color parameters allow particular control
   *          of gridcomponents.
   * @param textColor
   *          Color of texts.
   * @param fillColor
   *          Color of backgroundpolygons behind the texts.
   * @param borderColor
   *          Color of cellborders.
   * @param highlightTextColor
   *          Color of texts in highlighted cells.
   * @param highlightFillColor
   *          Color of backgroundpolygons behind highlighted cells.
   * @param highlightBorderColor
   *          Color of borders of highlighted cells.
   * @param step
   *          The step in which the change happens
   * @param duration
   *          duration of the change (affects created changeColor Animator)
   * @param offset
   *          Time before the change happens (affects created changeColor
   *          Animator)
   * @param unit
   *          The unit duration and offset are measured in: "ticks" or "ms"
   */
  public static void setGridColor(String gridName, int line, int column,
      Color genericColor, Color textColor, Color fillColor, Color borderColor,
      Color highlightTextColor, Color highlightFillColor,
      Color highlightBorderColor, int step, int duration, int offset,
      String unit) {
    // check if coordinates are valid
    // System.out.println("{"+step+"} setze farben
    // von:"+gridName+"["+line+"]["+column+"] c=" +genericColor +
    // " txt=" +textColor +
    // " fll=" +fillColor +
    // " brd=" +borderColor +
    // " htxt=" +highlightTextColor +
    // " hfll=" +highlightFillColor +
    // " hbrd=" +highlightBorderColor +
    // " dura=" + duration+
    // " offs=" + offset +
    // " unit=" +unit);
    int[] size = getObjectProperties().getIntArrayProperty(
        gridSizeKey(gridName));
    if ((line >= size[0]) || (column >= size[1])) {
      MessageDisplay.errorMsg("Grid index " + gridName + "[" + line + "]["
          + column + "] is out of bounds. Valid cells are [0.." + size[0]
          + "][0.." + size[1], MessageDisplay.RUN_ERROR);
      return;
    }
    if (textColor == null)
      textColor = genericColor; // this is the compatibility-hack known from
                                // makegrid()
    int[] textNumsToColor;
    int[] cellNumsToColor;
    // beacause there are so many attributes and usually just a few of them are
    // set, we first
    // check if an attribute is set, before we loop over the cells for this
    // attribute.
    // the other possibility was to loop just once over the cells and do the if
    // in every step.
    // This effort is made to create as few animators as possible.

    // First check wich cells have to be updated
    // Although this if-else-block is rather big, its structure is very similar
    // to
    // the ones in methods as setGridValue()

    if (line == DEFAULT_INT)
      if (column == DEFAULT_INT) {// ##################### whole grid
                                  // ############################
        // store here which obNum should be updated
        textNumsToColor = new int[size[0] * size[1]];
        cellNumsToColor = new int[size[0] * size[1]];
        for (int l = 0; l < size[0]; l++)
          for (int c = 0; c < size[1]; c++) {
            textNumsToColor[l * size[1] + c] = getObjectIDs().getIntProperty(
                gridTextKey(gridName, l, c));
            cellNumsToColor[l * size[1] + c] = getObjectIDs().getIntProperty(
                gridBackKey(gridName, l, c));
          }
        // now check each attribute. if set, update objectproperties and create
        // animator
        if (textColor != null) {
          for (int l = 0; l < size[0]; l++)
            for (int c = 0; c < size[1]; c++)
              getObjectProperties().put(gridTextColorKey(gridName, l, c),
                  textColor);
          // create animator for textcolor
          ColorChanger setColorAnimator = new ColorChanger(step,
              textNumsToColor, duration, "color", textColor);
          setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
          setColorAnimator.setOffset(offset);
          BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
        }// endif textColor
        if (fillColor != null) {
          for (int l = 0; l < size[0]; l++)
            for (int c = 0; c < size[1]; c++)
              getObjectProperties().put(gridFillColorKey(gridName, l, c),
                  fillColor);
          // create animator for fillcolor
          ColorChanger setColorAnimator = new ColorChanger(step,
              cellNumsToColor, duration, "fillcolor", fillColor);
          setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
          setColorAnimator.setOffset(offset);
          BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
        }// endif fillColor
        if (borderColor != DEFAULT_COLOR) {
          for (int l = 0; l < size[0]; l++)
            for (int c = 0; c < size[1]; c++)
              getObjectProperties().put(gridBorderColorKey(gridName, l, c),
                  borderColor);
          // create animator for bordercolor
          ColorChanger setColorAnimator = new ColorChanger(step,
              cellNumsToColor, duration, "color", borderColor);
          setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
          setColorAnimator.setOffset(offset);
          BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
        }// endif borderColor
        if (highlightTextColor != DEFAULT_COLOR) {
          for (int l = 0; l < size[0]; l++)
            for (int c = 0; c < size[1]; c++)
              getObjectProperties()
                  .put(gridHighlightTextColorKey(gridName, l, c),
                      highlightTextColor);
        }// endif htextColor
        if (highlightFillColor != DEFAULT_COLOR) {
          for (int l = 0; l < size[0]; l++)
            for (int c = 0; c < size[1]; c++)
              getObjectProperties()
                  .put(gridHighlightFillColorKey(gridName, l, c),
                      highlightFillColor);
        }// endif hfillColor
        if (highlightBorderColor != DEFAULT_COLOR) {
          for (int l = 0; l < size[0]; l++)
            for (int c = 0; c < size[1]; c++)
              getObjectProperties().put(
                  gridHighlightBorderColorKey(gridName, l, c),
                  highlightBorderColor);
        }// endif hborderColor
      }// endif column < 0, line is still < 0

      else { // l<0 & c>0 ################## whole column -> loop over lines
              // ######################
        // store obNum for later Update
        textNumsToColor = new int[size[0]];
        cellNumsToColor = new int[size[0]];
        for (int l = 0; l < size[0]; l++) {
          textNumsToColor[l] = getObjectIDs().getIntProperty(
              gridTextKey(gridName, l, column));
          cellNumsToColor[l] = getObjectIDs().getIntProperty(
              gridBackKey(gridName, l, column));
        }
        // now check each attribute
        if (textColor != DEFAULT_COLOR)
          for (int l = 0; l < size[0]; l++) {
            getObjectProperties().put(gridTextColorKey(gridName, l, column),
                textColor);
          }// endif textColor
        if (fillColor != DEFAULT_COLOR)
          for (int l = 0; l < size[0]; l++) {
            getObjectProperties().put(gridFillColorKey(gridName, l, column),
                fillColor);
          }// endif fillColor

        // now check each attribute. if set, update objectproperties and create
        // animator
        if (textColor != DEFAULT_COLOR) {
          for (int l = 0; l < size[0]; l++)
            getObjectProperties().put(gridTextColorKey(gridName, l, column),
                textColor);
          // create animator for textcolor
          ColorChanger setColorAnimator = new ColorChanger(step,
              textNumsToColor, duration, "color", textColor);
          setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
          setColorAnimator.setOffset(offset);
          BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
        }// endif textColor
        if (fillColor != DEFAULT_COLOR) {
          for (int l = 0; l < size[0]; l++)
            getObjectProperties().put(gridFillColorKey(gridName, l, column),
                fillColor);
          // create animator for fillcolor
          ColorChanger setColorAnimator = new ColorChanger(step,
              cellNumsToColor, duration, "fillcolor", fillColor);
          setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
          setColorAnimator.setOffset(offset);
          BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
        }// endif fillColor
        if (borderColor != DEFAULT_COLOR) {
          for (int l = 0; l < size[0]; l++)
            getObjectProperties().put(gridBorderColorKey(gridName, l, column),
                borderColor);
          // create animator for bordercolor
          ColorChanger setColorAnimator = new ColorChanger(step,
              cellNumsToColor, duration, "color", borderColor);
          setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
          setColorAnimator.setOffset(offset);
          BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
        }// endif borderColor
        if (highlightTextColor != DEFAULT_COLOR) {
          for (int l = 0; l < size[0]; l++)
            getObjectProperties().put(
                gridHighlightTextColorKey(gridName, l, column),
                highlightTextColor);
        }// endif htextColor
        if (highlightFillColor != DEFAULT_COLOR) {
          for (int l = 0; l < size[0]; l++)
            getObjectProperties().put(
                gridHighlightFillColorKey(gridName, l, column),
                highlightFillColor);
        }// endif hfillColor
        if (highlightBorderColor != DEFAULT_COLOR) {
          for (int l = 0; l < size[0]; l++)
            getObjectProperties().put(
                gridHighlightBorderColorKey(gridName, l, column),
                highlightBorderColor);
        }// endif hborderColor
      }// endelse, so l > 0

    else if (column == DEFAULT_INT) {// l>0 & c<0 ########### whole line ->
                                      // loop over columns ##############
      // store obNum for later Update
      textNumsToColor = new int[size[1]];
      cellNumsToColor = new int[size[1]];
      for (int c = 0; c < size[1]; c++) {
        textNumsToColor[c] = getObjectIDs().getIntProperty(
            gridTextKey(gridName, line, c));
        cellNumsToColor[c] = getObjectIDs().getIntProperty(
            gridBackKey(gridName, line, c));
      }
      // now check each attribute. if set, update objectproperties and create
      // animator
      if (textColor != DEFAULT_COLOR) {
        for (int c = 0; c < size[1]; c++)
          getObjectProperties().put(gridTextColorKey(gridName, line, c),
              textColor);
        // create animator for textcolor
        ColorChanger setColorAnimator = new ColorChanger(step, textNumsToColor,
            duration, "color", textColor);
        setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
        setColorAnimator.setOffset(offset);
        BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
      }// endif textColor
      if (fillColor != DEFAULT_COLOR) {
        for (int c = 0; c < size[1]; c++)
          getObjectProperties().put(gridFillColorKey(gridName, line, c),
              fillColor);
        // create animator for fillcolor
        ColorChanger setColorAnimator = new ColorChanger(step, cellNumsToColor,
            duration, "fillcolor", fillColor);
        setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
        setColorAnimator.setOffset(offset);
        BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
      }// endif fillColor
      if (borderColor != DEFAULT_COLOR) {
        for (int c = 0; c < size[1]; c++)
          getObjectProperties().put(gridBorderColorKey(gridName, line, c),
              borderColor);
        // create animator for bordercolor
        ColorChanger setColorAnimator = new ColorChanger(step, cellNumsToColor,
            duration, "color", borderColor);
        setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
        setColorAnimator.setOffset(offset);
        BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
      }// endif borderColor
      if (highlightTextColor != DEFAULT_COLOR) {
        for (int c = 0; c < size[1]; c++)
          getObjectProperties().put(
              gridHighlightTextColorKey(gridName, line, c), highlightTextColor);
      }// endif htextColor
      if (highlightFillColor != DEFAULT_COLOR) {
        for (int c = 0; c < size[1]; c++)
          getObjectProperties().put(
              gridHighlightFillColorKey(gridName, line, c), highlightFillColor);
      }// endif hfillColor
      if (highlightBorderColor != DEFAULT_COLOR) {
        for (int c = 0; c < size[1]; c++)
          getObjectProperties().put(
              gridHighlightBorderColorKey(gridName, line, c),
              highlightBorderColor);
      }// endif hborderColor
    }// endif c<0, line is still > 0

    else {// c>0 & l>0 ########################### just one cell
          // ###############################
      // store obNum for later Update
      textNumsToColor = new int[1];
      textNumsToColor[0] = getObjectIDs().getIntProperty(
          gridTextKey(gridName, line, column));
      cellNumsToColor = new int[1];
      cellNumsToColor[0] = getObjectIDs().getIntProperty(
          gridBackKey(gridName, line, column));
      // now check each attribute. if set, update objectproperties and create
      // animator
      if (textColor != DEFAULT_COLOR) {
        getObjectProperties().put(gridTextColorKey(gridName, line, column),
            textColor);
        // create animator for textcolor
        ColorChanger setColorAnimator = new ColorChanger(step, textNumsToColor,
            duration, "color", textColor);
        setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
        setColorAnimator.setOffset(offset);
        BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
      }// endif textColor
      if (fillColor != DEFAULT_COLOR) {
        getObjectProperties().put(gridFillColorKey(gridName, line, column),
            fillColor);
        // create animator for fillcolor
        ColorChanger setColorAnimator = new ColorChanger(step, cellNumsToColor,
            duration, "fillcolor", fillColor);
        setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
        setColorAnimator.setOffset(offset);
        BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
      }// endif fillColor
      if (borderColor != DEFAULT_COLOR) {
        getObjectProperties().put(gridBorderColorKey(gridName, line, column),
            borderColor);
        // create animator for bordercolor
        ColorChanger setColorAnimator = new ColorChanger(step, cellNumsToColor,
            duration, "color", borderColor);
        setColorAnimator.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
        setColorAnimator.setOffset(offset);
        BasicParser.addAnimatorToAnimation(setColorAnimator, anim);
      }// endif borderColor
      if (highlightTextColor != DEFAULT_COLOR) {
        getObjectProperties().put(
            gridHighlightTextColorKey(gridName, line, column),
            highlightTextColor);
      }// endif htextColor
      if (highlightFillColor != DEFAULT_COLOR) {
        getObjectProperties().put(
            gridHighlightFillColorKey(gridName, line, column),
            highlightFillColor);
      }// endif hfillColor
      if (highlightBorderColor != DEFAULT_COLOR) {
        getObjectProperties().put(
            gridHighlightBorderColorKey(gridName, line, column),
            highlightBorderColor);
      }// endif hborderColor
    }// endelse
    // end ifblock
  } // end setGridColor(...)

  /**
   * Swaps values of two cells, lines, columns or grids. If a duration greater
   * zero is given, the values move to their new position in this time. If
   * refresh is true, the grid will be resized if needed.
   * 
   * @param gridName1
   *          Name of the grid the first value is in
   * @param line1
   *          Line of the grid the first value is in. Numbering starts with
   *          zero. If the parameter is set to -1, all lines are selected, which
   *          means a whole column or the whole grid will be swapped.
   * @param col1
   *          Column of the grid the first value is in. Numbering starts with
   *          zero. If the parameter is set to -1, all columns are selected,
   *          which means a whole line or the whole grid will be swapped.
   * @param gridName2
   *          Name of the grid the first value is in
   * @param line2
   *          Line of the grid the first value is in. Numbering starts with
   *          zero. If the parameter is set to -1, all lines are selected, which
   *          means a whole column or the whole grid will be swapped.
   * @param col2
   *          Column of the grid the first value is in. Numbering starts with
   *          zero. If the parameter is set to -1, all columns are selected,
   *          which means a whole line or the whole grid will be swapped.
   * @param step
   *          The step in which the swap happens
   * @param duration
   *          duration of the swap visualisation (affects created move animator)
   * @param offset
   *          Time before the swap happens (affects created move animator)
   * @param unit
   *          The unit duration and offset are measured in: "ticks" or "ms"
   * @param refresh
   *          If set to truerefreshgrid() is called. The Grid will be
   *          recalculated and necessary resizing is made. As this can result in
   *          many Move-Animators, this parameter should not be set, if
   *          swapGridValue is called right again another time.
   */
  public static void swapGridValues(String gridName1, int line1, int col1,
      String gridName2, int line2, int col2, int step, int duration,
      int offset, String unit, boolean refresh) {
    int[] size1 = getObjectProperties().getIntArrayProperty(
        gridSizeKey(gridName1));
    int[] size2 = getObjectProperties().getIntArrayProperty(
        gridSizeKey(gridName2));
    if ((line1 >= size1[0]) || (col1 >= size1[1])) {
      MessageDisplay.errorMsg("Grid index " + gridName1 + "[" + line1 + "]["
          + col1 + "] is out of bounds. Valid cells are [0.." + size1[0]
          + "][0.." + size1[1], MessageDisplay.RUN_ERROR);
      return;
    }
     if ((line2 >= size2[0]) || (col2 >= size2[1])) {
      MessageDisplay.errorMsg("Grid index " + gridName2 + "[" + line2 + "]["
          + col2 + "] is out of bounds. Valid cells are [0.." + size2[0]
          + "][0.." + size2[1], MessageDisplay.RUN_ERROR);
      return;
    }

    String value1, value2;
    int[] objects1, objects2;
    // to swap two elements, each gets a move, a setValue Animation.
    // the user could try to swap a whole line or even grid (the function works
    // "inter-grid").
    // In this case, we needed one move, but multiple setgridvalues

    // So first we check that we only swap value with value, column with column,
    // line with
    // line and grid with grid.
    // Other cases not really make sense, because a "right" result wouldnt be
    // obvious.
    // As a trick to validate the inputs, we multiply the coordinates:
    // e.g. if line1 * line2 is less than zero, then one of them was meant to be
    // a column,
    // the other value addresses a cell, which is what we want to catch:

    if ((line1 * line2 < 0) || (col1 * col2 < 0)) {
      MessageDisplay
          .errorMsg(
              "Invalid coordinates for swapgridvalues. Maybe you try to swap a value and a column.",
              MessageDisplay.RUN_ERROR);
      return;
    }

    // now the big if-else-block as you might know it from other methods,
    // as setgridvalue or highlightgridcell...
    if (line1 == DEFAULT_INT)
      if (col1 == DEFAULT_INT) { // ######### whole grid ################
        // first check if grids are of equal sizes
        if ((size1[0] != size2[0]) || (size1[1] != size2[1])) {
          MessageDisplay.errorMsg("Cannot swap all values of '" + gridName1
              + "' and '" + gridName2 + "' because of different gridsizes",
              MessageDisplay.RUN_ERROR);
          return;
        }
        // as the grids are of equal sizes, we just can swap every single value
        objects1 = new int[size1[0] * size1[1]]; // we have to collect the
                                                  // objectNums for
        objects2 = new int[size1[0] * size1[1]]; // the move and setText
                                                  // animators
        for (int l = 0; l < size1[0]; l++)
          for (int c = 0; c < size1[1]; c++) {
            value1 = getObjectProperties().getProperty(
                gridTextStringKey(gridName1, l, c));
            value2 = getObjectProperties().getProperty(
                gridTextStringKey(gridName2, l, c));
            // gridname lin col value step duration offset unit refresh
            setGridValue(gridName1, l, c, value2, step, 0, duration + offset,
                unit, false);
            setGridValue(gridName2, l, c, value1, step, 0, duration + offset,
                unit, false);
            objects1[l + c * size1[0]] = getObjectIDs().getIntProperty(
                gridTextKey(gridName1, l, c));
            objects2[l + c * size1[0]] = getObjectIDs().getIntProperty(
                gridTextKey(gridName2, l, c));
          }
      }

      else { // (line == DEFAULT_INT) & (col1>0) ############# whole column
              // ###############################
        // first check if columns are of equal sizes
        if (size1[0] != size2[0]) {
          MessageDisplay.errorMsg("Cannot swap column '" + col1 + "' of '"
              + gridName1 + "' and column '" + col1 + "' of '" + gridName2
              + "' because of different sizes", MessageDisplay.RUN_ERROR);
          return;
        }
        objects1 = new int[size1[0]]; // we have to collect the objectNums for
        objects2 = new int[size1[0]]; // movement also
        for (int l = 0; l < size1[0]; l++) {
          value1 = getObjectProperties().getProperty(
              gridTextStringKey(gridName1, l, col1));
          value2 = getObjectProperties().getProperty(
              gridTextStringKey(gridName2, l, col2));
          setGridValue(gridName1, l, col1, value2, step, 0, duration + offset,
              unit, false);
          setGridValue(gridName2, l, col2, value1, step, 0, duration + offset,
              unit, false);
          objects1[l] = getObjectIDs().getIntProperty(
              gridTextKey(gridName1, l, col1));
          objects2[l] = getObjectIDs().getIntProperty(
              gridTextKey(gridName2, l, col2));
        }
      }

    else if (col1 == DEFAULT_INT) {// &(line>0) ################# whole line
                                    // ##############################
      // first check if lines are of equal sizes
      if (size1[1] != size2[1]) {
        MessageDisplay.errorMsg("Cannot swap line '" + line1 + "' of '"
            + gridName1 + "' and line '" + line1 + "' of '" + gridName2
            + "' because of different sizes", MessageDisplay.RUN_ERROR);
        return;
      }
      objects1 = new int[size1[1]]; // we have to collect the objectNums for
      objects2 = new int[size1[1]]; // movement also
      for (int c = 0; c < size1[1]; c++) {
        value1 = getObjectProperties().getProperty(
            gridTextStringKey(gridName1, line1, c));
        value2 = getObjectProperties().getProperty(
            gridTextStringKey(gridName2, line2, c));
        setGridValue(gridName1, line1, c, value2, step, 0, duration + offset,
            unit, false);
        setGridValue(gridName2, line2, c, value1, step, 0, duration + offset,
            unit, false);
        objects1[c] = getObjectIDs().getIntProperty(
            gridTextKey(gridName1, line1, c));
        objects2[c] = getObjectIDs().getIntProperty(
            gridTextKey(gridName2, line2, c));
      }

    }

    else {// (line>0) &(col>0) ################# just one cell
          // ##############################
      value1 = getObjectProperties().getProperty(
          gridTextStringKey(gridName1, line1, col1));
      value2 = getObjectProperties().getProperty(
          gridTextStringKey(gridName2, line2, col2));
      setGridValue(gridName1, line1, col1, value2, step, 0, duration + offset,
          unit, false);
      setGridValue(gridName2, line2, col2, value1, step, 0, duration + offset,
          unit, false);
      objects1 = new int[] { getObjectIDs().getIntProperty(
          gridTextKey(gridName1, line1, col1)) };
      objects2 = new int[] { getObjectIDs().getIntProperty(
          gridTextKey(gridName2, line2, col2)) };
    } // endelse

    // before the above created setGridValue-FX 'happen' we move the texts as if
    // we where
    // swapping them visually. (in fact its just an illusion, as the objects
    // instantly
    // move back after they reached their destination and change their values
    // instead
    // of their positions)
    // To move them, we create the movebases from the positions of the values.
    // but as their
    // coordinates could be <0 (whole line/column) we have to change those
    // values to zero
    // (which is a appropriate coordinate as it always exists, and the movement
    // is relative
    // anyway)

    if (line1 < 0)
      line1 = 0;
    if (line2 < 0)
      line2 = 0;
    if (col1 < 0)
      col1 = 0;
    if (col2 < 0)
      col2 = 0;

    if (duration > 0) {
      // duration > 0 -> create movebases from the values locations...
      PTPoint location1 = new PTPoint(getObjectProperties().getPointProperty(
          gridTextPositionKey(gridName1, line1, col1)));
      PTPoint location2 = new PTPoint(getObjectProperties().getPointProperty(
          gridTextPositionKey(gridName2, line2, col2)));
      PTPolyline moveBase1to2 = new PTPolyline();
      moveBase1to2.addNode(location1);
      moveBase1to2.addNode(location2);
      moveBase1to2.setColor(new Color(1.0f, 1.0f, 0.0f));
      moveBase1to2.setDepth(0);
      int mb12Num = moveBase1to2.getNum(true);
      getObjectIDs().put("mb12_step" + step, mb12Num);
      getObjectTypes().put("mb12_step" + step, "polyline");
      BasicParser.addGraphicObject(moveBase1to2, anim);

      PTPolyline moveBase2to1 = new PTPolyline();
      moveBase2to1.addNode(location2);
      moveBase2to1.addNode(location1);
      moveBase2to1.setColor(new Color(1.0f, 1.0f, 0.0f));
      moveBase2to1.setDepth(0);
      int mb21Num = moveBase2to1.getNum(true);
      getObjectIDs().put("mb21_step" + step, mb21Num);
      getObjectTypes().put("mb21_step" + step, "polyline");
      BasicParser.addGraphicObject(moveBase2to1, anim);

      // move the texts to their new positions - for fake...
      Move move1to2 = new Move(step, objects1, duration, "translate", mb12Num);
      move1to2.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      move1to2.setOffset(offset);
      Move move2to1 = new Move(step, objects2, duration, "translate", mb21Num);
      move2to1.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      move2to1.setOffset(offset);
      BasicParser.addAnimatorToAnimation(move1to2, anim);
      BasicParser.addAnimatorToAnimation(move2to1, anim);

      // ... as thats just a fake for vis, we move them back and set their new
      // values
      Move back2to1 = new Move(step, objects1, 0, "translate", mb21Num);
      back2to1.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      back2to1.setOffset(offset + duration);
      Move back1to2 = new Move(step, objects2, 0, "translate", mb12Num);
      back1to2.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      back1to2.setOffset(offset + duration);

      BasicParser.addAnimatorToAnimation(back1to2, anim);
      BasicParser.addAnimatorToAnimation(back2to1, anim);
    }

    if (refresh) {
      refreshGrid(gridName1, step, duration, offset, unit);
      refreshGrid(gridName2, step, duration, offset, unit);
    }
  }// endof swapgridvalues

  /**
   * Highlights a cell, line, column or the whole grid by change color of text,
   * background and border to the highlightcolors stored in the objectProperties
   * hashtable.
   * 
   * @param gridName
   *          Name of the grid
   * @param line
   *          number of the line the cell is in. Numbering starts with zero. If
   *          the parameter is set to -1, all lines are selected, which means a
   *          whole column or the whole grid will be highlighted.
   * @param column
   *          number of the column the cell is in. Numbering starts with zero.
   *          If the parameter is set to -1, all columns are selected, which
   *          means a whole line or the whole grid will be highlighted.
   * @param step
   *          The step in which the highlighting happens
   * @param duration
   *          duration of the highlighting (affects created colorChange
   *          animator)
   * @param offset
   *          Time before the highlighting happens (affects created colorChange
   *          animator)
   * @param unit
   *          The unit duration and offset are measured in: "ticks" or "ms"
   */
  public static void highlightGridCell(String gridName, int line, int column,
      int step, int duration, int offset, String unit) {
    // check if coordinates are valid
    int[] size = getObjectProperties().getIntArrayProperty(
        gridSizeKey(gridName));
    if ((line >= size[0]) || (column >= size[1])) {
      MessageDisplay.errorMsg("Grid index " + gridName + "[" + line + "]["
          + column + "] is out of bounds. Valid cells are [0.." + size[0]
          + "][0.." + size[1], MessageDisplay.RUN_ERROR);
       return;
    }

    // Before creating the animators, we collect all cells to Highlight, sorted
    // by their individual highlightcolor.
    // This way just as few Animators as possible are created.
    // in every case, we read the highlightcolors for text, background and
    // borders from the ObjectProberties. If
    // there is no vector collecting objects for this color, we have to create
    // one. After the big if-else-section,
    // we have a hashtable full of vectors of Integers. For every key we create
    // an animator which changes the
    // color of the objectNumbers stored in the vector assigned to this key.
    // The Key is a String which codes the color as rgb.
    //
    // This all ist needed, because one animator can just apply one color to an
    // array of Objects.

    Hashtable<String, Vector<Integer>> backgroundsToHighlight = new Hashtable<String, Vector<Integer>>();
    Hashtable<String, Vector<Integer>> bordersToHighlight = new Hashtable<String, Vector<Integer>>();
    String currentBackHLColor;
    String currentBorderHLColor;
    if (line == DEFAULT_INT)
      if (column == DEFAULT_INT) { // whole grid
        for (int l = 0; l < size[0]; l++)
          for (int c = 0; c < size[1]; c++) {
            currentBackHLColor = String.valueOf(getObjectProperties()
                .getColorProperty(gridHighlightFillColorKey(gridName, l, c))
                .getRGB());
            if (!backgroundsToHighlight.containsKey(currentBackHLColor))
              backgroundsToHighlight.put(currentBackHLColor,
                  new Vector<Integer>());
            backgroundsToHighlight.get(currentBackHLColor).add(
                Integer.valueOf(getObjectIDs().getIntProperty(
                    gridBackKey(gridName, l, c))));

            currentBorderHLColor = String.valueOf(getObjectProperties()
                .getColorProperty(gridHighlightBorderColorKey(gridName, l, c))
                .getRGB());
            if (!bordersToHighlight.containsKey(currentBorderHLColor))
              bordersToHighlight.put(currentBorderHLColor,
                  new Vector<Integer>());
            bordersToHighlight.get(currentBorderHLColor).add(
                Integer.valueOf(getObjectIDs().getIntProperty(
                    gridBackKey(gridName, l, c))));
          }
      } else { // whole column -> loop over lines
        for (int l = 0; l < size[0]; l++) {
          currentBackHLColor = String.valueOf(getObjectProperties()
              .getColorProperty(gridHighlightFillColorKey(gridName, l, column))
              .getRGB());
          if (!backgroundsToHighlight.containsKey(currentBackHLColor))
            backgroundsToHighlight.put(currentBackHLColor,
                new Vector<Integer>());
          backgroundsToHighlight.get(currentBackHLColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridBackKey(gridName, l, column))));

          currentBorderHLColor = String.valueOf(getObjectProperties()
              .getColorProperty(
                  gridHighlightBorderColorKey(gridName, l, column)).getRGB());
          if (!bordersToHighlight.containsKey(currentBorderHLColor))
            bordersToHighlight.put(currentBorderHLColor, new Vector<Integer>());
          bordersToHighlight.get(currentBorderHLColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridBackKey(gridName, l, column))));
        }
      }

    else {
      if (column == DEFAULT_INT) { // whole line -> loop over columns
        for (int c = 0; c < size[1]; c++) {
          currentBackHLColor = String.valueOf(getObjectProperties()
              .getColorProperty(gridHighlightFillColorKey(gridName, line, c))
              .getRGB());
          if (!backgroundsToHighlight.containsKey(currentBackHLColor))
            backgroundsToHighlight.put(currentBackHLColor,
                new Vector<Integer>());
          backgroundsToHighlight.get(currentBackHLColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridBackKey(gridName, line, c))));

          currentBorderHLColor = String.valueOf(getObjectProperties()
              .getColorProperty(gridHighlightBorderColorKey(gridName, line, c))
              .getRGB());
          if (!bordersToHighlight.containsKey(currentBorderHLColor))
            bordersToHighlight.put(currentBorderHLColor, new Vector<Integer>());
          bordersToHighlight.get(currentBorderHLColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridBackKey(gridName, line, c))));
        }
      } else { // just one cell
        currentBackHLColor = String
            .valueOf(getObjectProperties().getColorProperty(
                gridHighlightFillColorKey(gridName, line, column)).getRGB());
        if (!backgroundsToHighlight.containsKey(currentBackHLColor))
          backgroundsToHighlight.put(currentBackHLColor, new Vector<Integer>());
        backgroundsToHighlight.get(currentBackHLColor).add(
            Integer.valueOf(getObjectIDs().getIntProperty(
                gridBackKey(gridName, line, column))));

        currentBorderHLColor = String.valueOf(getObjectProperties()
            .getColorProperty(
                gridHighlightBorderColorKey(gridName, line, column)).getRGB());
        if (!bordersToHighlight.containsKey(currentBorderHLColor))
          bordersToHighlight.put(currentBorderHLColor, new Vector<Integer>());
        bordersToHighlight.get(currentBorderHLColor).add(
            Integer.valueOf(getObjectIDs().getIntProperty(
                gridBackKey(gridName, line, column))));
      }
    }

    // So now all cells are sorted into vectors of objects sharing the same
    // Highlightcolor. the Animators are created
    // within a loop over an enumeration of the keys, the vectors are assigned
    // to.
    Enumeration<String> collectedBackgroundHighlightColorKeys = backgroundsToHighlight
        .keys();
    while (collectedBackgroundHighlightColorKeys.hasMoreElements()) {
      String currentHLColor = collectedBackgroundHighlightColorKeys
          .nextElement();
      Vector<Integer> backgrounds = backgroundsToHighlight.get(currentHLColor);
      // Convert the Vector<Integer> to an int[]
      int[] backgroundIDs = new int[backgrounds.size()];
      for (int i = 0; i < backgroundIDs.length; i++)
        backgroundIDs[i] = backgrounds.get(i).intValue();

      ColorChanger backgroundHL = new ColorChanger(step, backgroundIDs,
          duration, "fillcolor", new Color(Integer.parseInt(currentHLColor)));
      backgroundHL.setOffset(offset);
      backgroundHL.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      BasicParser.addAnimatorToAnimation(backgroundHL, anim);
    }
    Enumeration<String> collectedBorderHighlightColorKeys = bordersToHighlight
        .keys();
    while (collectedBorderHighlightColorKeys.hasMoreElements()) {
      String currentHLColor = collectedBorderHighlightColorKeys.nextElement();
      Vector<Integer> borders = bordersToHighlight.get(currentHLColor);
      // Convert the Vector<Integer> to an int[]
      int[] borderIDs = new int[borders.size()];
      for (int i = 0; i < borderIDs.length; i++)
        borderIDs[i] = borders.get(i).intValue();

      ColorChanger borderHL = new ColorChanger(step, borderIDs, duration,
          "color", new Color(Integer.parseInt(currentHLColor)));
      borderHL.setOffset(offset);
      borderHL.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      BasicParser.addAnimatorToAnimation(borderHL, anim);
    }
  }// endof highlightgridcell

  /**
   * Highlights a cell, line, column or the whole grid by change color of text,
   * background and border to the highlightcolors stored in the objectProperties
   * hashtable.
   * 
   * @param gridName
   *          Name of the grid
   * @param line
   *          number of the line the cell is in. Numbering starts with zero. If
   *          the parameter is set to -1, all lines are selected, which means a
   *          whole column or the whole grid will be highlighted.
   * @param column
   *          number of the column the cell is in. Numbering starts with zero.
   *          If the parameter is set to -1, all columns are selected, which
   *          means a whole line or the whole grid will be highlighted.
   * @param step
   *          The step in which the highlighting happens
   * @param duration
   *          duration of the highlighting (affects created colorChange
   *          animator)
   * @param offset
   *          Time before the highlighting happens (affects created colorChange
   *          animator)
   * @param unit
   *          The unit duration and offset are measured in: "ticks" or "ms"
   */
  public static void highlightGridElem(String gridName, int line, int column,
      int step, int duration, int offset, String unit) {
    // check if coordinates are valid
    int[] size = getObjectProperties().getIntArrayProperty(
        gridSizeKey(gridName));
    if ((line >= size[0]) || (column >= size[1])) {
      MessageDisplay.errorMsg("Grid index " + gridName + "[" + line + "]["
          + column + "] is out of bounds. Valid cells are [0.." + size[0]
          + "][0.." + size[1], MessageDisplay.RUN_ERROR);
       return;
    }

    // Before creating the animators, we collect all cells to Highlight, sorted
    // by their individual highlightcolor.
    // This way just as few Animators as possible are created.
    // in every case, we read the highlightcolors for text, background and
    // borders from the ObjectProberties. If
    // there is no vector collecting objects for this color, we have to create
    // one. After the big if-else-section,
    // we have a hashtable full of vectors of Integers. For every key we create
    // an animator which changes the
    // color of the objectNumbers stored in the vector assigned to this key.
    // The Key is a String which codes the color as rgb.
    //
    // This all ist needed, because one animator can just apply one color to an
    // array of Objects.

    Hashtable<String, Vector<Integer>> textObjectsToHighlight = new Hashtable<String, Vector<Integer>>();
    String currentTextHLColor;
    if (line == DEFAULT_INT)
      if (column == DEFAULT_INT) { // whole grid
        for (int l = 0; l < size[0]; l++)
          for (int c = 0; c < size[1]; c++) {
            currentTextHLColor = String.valueOf(getObjectProperties()
                .getColorProperty(gridHighlightTextColorKey(gridName, l, c))
                .getRGB());
            if (!textObjectsToHighlight.containsKey(currentTextHLColor))
              textObjectsToHighlight.put(currentTextHLColor,
                  new Vector<Integer>());
            textObjectsToHighlight.get(currentTextHLColor).add(
                Integer.valueOf(getObjectIDs().getIntProperty(
                    gridTextKey(gridName, l, c))));
          }
      } else { // whole column -> loop over lines
        for (int l = 0; l < size[0]; l++) {
          currentTextHLColor = String.valueOf(getObjectProperties()
              .getColorProperty(gridHighlightTextColorKey(gridName, l, column))
              .getRGB());
          if (!textObjectsToHighlight.containsKey(currentTextHLColor))
            textObjectsToHighlight.put(currentTextHLColor,
                new Vector<Integer>());
          textObjectsToHighlight.get(currentTextHLColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridTextKey(gridName, l, column))));
        }
      }

    else {
      if (column == DEFAULT_INT) { // whole line -> loop over columns
        for (int c = 0; c < size[1]; c++) {
          currentTextHLColor = String.valueOf(getObjectProperties()
              .getColorProperty(gridHighlightTextColorKey(gridName, line, c))
              .getRGB());
          if (!textObjectsToHighlight.containsKey(currentTextHLColor))
            textObjectsToHighlight.put(currentTextHLColor,
                new Vector<Integer>());
          textObjectsToHighlight.get(currentTextHLColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridTextKey(gridName, line, c))));
        }
      } else { // just one cell
        currentTextHLColor = String
            .valueOf(getObjectProperties().getColorProperty(
                gridHighlightTextColorKey(gridName, line, column)).getRGB());
        if (!textObjectsToHighlight.containsKey(currentTextHLColor))
          textObjectsToHighlight.put(currentTextHLColor, new Vector<Integer>());
        textObjectsToHighlight.get(currentTextHLColor).add(
            Integer.valueOf(getObjectIDs().getIntProperty(
                gridTextKey(gridName, line, column))));
      }
    }

    // So now all cells are sorted into vectors of objects sharing the same
    // Highlightcolor. the Animators are created
    // within a loop over an enumeration of the keys, the vectors are assigned
    // to.
    Enumeration<String> collectedTextObjectHighlightColorKeys = textObjectsToHighlight
        .keys();
    while (collectedTextObjectHighlightColorKeys.hasMoreElements()) {
      String currentHLColor = collectedTextObjectHighlightColorKeys
          .nextElement();
      Vector<Integer> textObjects = textObjectsToHighlight.get(currentHLColor);
      // Convert the Vector<Integer> to an int[]
      int[] textObjectIDs = new int[textObjects.size()];
      for (int i = 0; i < textObjectIDs.length; i++)
        textObjectIDs[i] = textObjects.get(i).intValue();

      ColorChanger textObjectHL = new ColorChanger(step, textObjectIDs,
          duration, "color", new Color(Integer.parseInt(currentHLColor)));
      textObjectHL.setOffset(offset);
      textObjectHL.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      BasicParser.addAnimatorToAnimation(textObjectHL, anim);
    }
  }// endof highlightgridcell

  /**
   * Unhighlights a cell, line, column or the whole grid by change color of
   * text, background and border to the colors stored in the objectProperties
   * hashtable.
   * 
   * @param gridName
   *          Name of the grid
   * @param line
   *          number of the line the cell is in. Numbering starts with zero. If
   *          the parameter is set to -1, all lines are selected, which means a
   *          whole column or the whole grid will be unhighlighted.
   * @param column
   *          number of the column the cell is in. Numbering starts with zero.
   *          If the parameter is set to -1, all columns are selected, which
   *          means a whole line or the whole grid will be unhighlighted.
   * @param step
   *          The step in which the unhighlighting happens
   * @param duration
   *          duration of the unhighlighting (affects created colorChange
   *          animator)
   * @param offset
   *          Time before the unhighlighting happens (affects created
   *          colorChange animator)
   * @param unit
   *          The unit duration and offset are measured in: "ticks" or "ms"
   */
  public static void unhighlightGridCell(String gridName, int line, int column,
      int step, int duration, int offset, String unit) {
    // check if coordinates are valid
    int[] size = getObjectProperties().getIntArrayProperty(
        gridSizeKey(gridName));
    if ((line >= size[0]) || (column >= size[1])) {
      MessageDisplay.errorMsg("Grid index " + gridName + "[" + line + "]["
          + column + "] is out of bounds. Valid cells are [0.." + size[0]
          + "][0.." + size[1], MessageDisplay.RUN_ERROR);
      return;
    }

    // Before creating the animators, we collect all cells to unhighlight,
    // sorted by their individual highlightcolor.
    // This way just as few animators as possible are created.
    // in every case, we read the normal (unhighlighted) colors for text,
    // background and borders from the
    // ObjectProberties. If there is not already a vector collecting objects for
    // this color, we have to create one.
    // After the big if-else-section, we have a hashtable full of vectors of
    // Integers.
    // For every key we create an animator which changes the color of the
    // objectNumbers stored in the
    // vector assigned to this key.
    // The Key is a String which codes the color as rgb.
    //
    // This all ist needed, because one animator can just apply one color to an
    // array of Objects.

    Hashtable<String, Vector<Integer>> backgroundsToUnhighlight = new Hashtable<String, Vector<Integer>>();
    Hashtable<String, Vector<Integer>> textObjectsToUnhighlight = new Hashtable<String, Vector<Integer>>();
    Hashtable<String, Vector<Integer>> bordersToUnhighlight = new Hashtable<String, Vector<Integer>>();
    String currentFillColor;
    String currentTextColor;
    String currentBorderColor;
    if (line == DEFAULT_INT)
      if (column == DEFAULT_INT) { // whole grid
        for (int l = 0; l < size[0]; l++)
          for (int c = 0; c < size[1]; c++) {
            currentFillColor = String.valueOf(getObjectProperties()
                .getColorProperty(gridFillColorKey(gridName, l, c)).getRGB());
            if (!backgroundsToUnhighlight.containsKey(currentFillColor))
              backgroundsToUnhighlight.put(currentFillColor,
                  new Vector<Integer>());
            backgroundsToUnhighlight.get(currentFillColor).add(
                Integer.valueOf(getObjectIDs().getIntProperty(
                    gridBackKey(gridName, l, c))));

            currentTextColor = String.valueOf(getObjectProperties()
                .getColorProperty(gridTextColorKey(gridName, l, c)).getRGB());
            if (!textObjectsToUnhighlight.containsKey(currentTextColor))
              textObjectsToUnhighlight.put(currentTextColor,
                  new Vector<Integer>());
            textObjectsToUnhighlight.get(currentTextColor).add(
                Integer.valueOf(getObjectIDs().getIntProperty(
                    gridTextKey(gridName, l, c))));

            currentBorderColor = String.valueOf(getObjectProperties()
                .getColorProperty(gridBorderColorKey(gridName, l, c)).getRGB());
            if (!bordersToUnhighlight.containsKey(currentBorderColor))
              bordersToUnhighlight.put(currentBorderColor,
                  new Vector<Integer>());
            bordersToUnhighlight.get(currentBorderColor).add(
                Integer.valueOf(getObjectIDs().getIntProperty(
                    gridBackKey(gridName, l, c))));
          }
      } else { // whole column -> loop over lines
        for (int l = 0; l < size[0]; l++) {
          currentFillColor = String
              .valueOf(getObjectProperties().getColorProperty(
                  gridFillColorKey(gridName, l, column)).getRGB());
          if (!backgroundsToUnhighlight.containsKey(currentFillColor))
            backgroundsToUnhighlight.put(currentFillColor,
                new Vector<Integer>());
          backgroundsToUnhighlight.get(currentFillColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridBackKey(gridName, l, column))));

          currentTextColor = String
              .valueOf(getObjectProperties().getColorProperty(
                  gridTextColorKey(gridName, l, column)).getRGB());
          if (!textObjectsToUnhighlight.containsKey(currentTextColor))
            textObjectsToUnhighlight.put(currentTextColor,
                new Vector<Integer>());
          textObjectsToUnhighlight.get(currentTextColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridTextKey(gridName, l, column))));

          currentBorderColor = String.valueOf(getObjectProperties()
              .getColorProperty(gridBorderColorKey(gridName, l, column))
              .getRGB());
          if (!bordersToUnhighlight.containsKey(currentBorderColor))
            bordersToUnhighlight.put(currentBorderColor, new Vector<Integer>());
          bordersToUnhighlight.get(currentBorderColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridBackKey(gridName, l, column))));
        }
      }

    else {
      if (column == DEFAULT_INT) { // whole line -> loop over columns
        for (int c = 0; c < size[1]; c++) {
          currentFillColor = String.valueOf(getObjectProperties()
              .getColorProperty(gridFillColorKey(gridName, line, c)).getRGB());
          if (!backgroundsToUnhighlight.containsKey(currentFillColor))
            backgroundsToUnhighlight.put(currentFillColor,
                new Vector<Integer>());
          backgroundsToUnhighlight.get(currentFillColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridBackKey(gridName, line, c))));

          currentTextColor = String.valueOf(getObjectProperties()
              .getColorProperty(gridTextColorKey(gridName, line, c)).getRGB());
          if (!textObjectsToUnhighlight.containsKey(currentTextColor))
            textObjectsToUnhighlight.put(currentTextColor,
                new Vector<Integer>());
          textObjectsToUnhighlight.get(currentTextColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridTextKey(gridName, line, c))));

          currentBorderColor = String
              .valueOf(getObjectProperties().getColorProperty(
                  gridBorderColorKey(gridName, line, c)).getRGB());
          if (!bordersToUnhighlight.containsKey(currentBorderColor))
            bordersToUnhighlight.put(currentBorderColor, new Vector<Integer>());
          bordersToUnhighlight.get(currentBorderColor).add(
              Integer.valueOf(getObjectIDs().getIntProperty(
                  gridBackKey(gridName, line, c))));
        }
      } else { // just one cell
        currentFillColor = String.valueOf(getObjectProperties()
            .getColorProperty(gridFillColorKey(gridName, line, column))
            .getRGB());
        if (!backgroundsToUnhighlight.containsKey(currentFillColor))
          backgroundsToUnhighlight.put(currentFillColor, new Vector<Integer>());
        backgroundsToUnhighlight.get(currentFillColor).add(
            Integer.valueOf(getObjectIDs().getIntProperty(
                gridBackKey(gridName, line, column))));

        currentTextColor = String.valueOf(getObjectProperties()
            .getColorProperty(gridTextColorKey(gridName, line, column))
            .getRGB());
        if (!textObjectsToUnhighlight.containsKey(currentTextColor))
          textObjectsToUnhighlight.put(currentTextColor, new Vector<Integer>());
        textObjectsToUnhighlight.get(currentTextColor).add(
            Integer.valueOf(getObjectIDs().getIntProperty(
                gridTextKey(gridName, line, column))));

        currentBorderColor = String.valueOf(getObjectProperties()
            .getColorProperty(gridBorderColorKey(gridName, line, column))
            .getRGB());
        if (!bordersToUnhighlight.containsKey(currentBorderColor))
          bordersToUnhighlight.put(currentBorderColor, new Vector<Integer>());
        bordersToUnhighlight.get(currentBorderColor).add(
            Integer.valueOf(getObjectIDs().getIntProperty(
                gridBackKey(gridName, line, column))));
      }
    }

    // So now all cells are sorted into vectors of objects sharing the same
    // Highlightcolor. the Animators are created
    // within a loop over an enumeration of the keys, the vectors are assigned
    // to.
    Enumeration<String> collectedBackgroundHighlightColorKeys = backgroundsToUnhighlight
        .keys();
    while (collectedBackgroundHighlightColorKeys.hasMoreElements()) {
      String currentHLColor = collectedBackgroundHighlightColorKeys
          .nextElement();
      Vector<Integer> backgrounds = backgroundsToUnhighlight
          .get(currentHLColor);
      // Convert the Vector<Integer> to an int[]
      int[] backgroundIDs = new int[backgrounds.size()];
      for (int i = 0; i < backgroundIDs.length; i++)
        backgroundIDs[i] = backgrounds.get(i).intValue();

      ColorChanger backgroundUnHL = new ColorChanger(step, backgroundIDs,
          duration, "fillcolor", new Color(Integer.parseInt(currentHLColor)));
      backgroundUnHL.setOffset(offset);
      backgroundUnHL.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      BasicParser.addAnimatorToAnimation(backgroundUnHL, anim);
    }
    Enumeration<String> collectedTextObjectHighlightColorKeys = textObjectsToUnhighlight
        .keys();
    while (collectedTextObjectHighlightColorKeys.hasMoreElements()) {
      String currentHLColor = collectedTextObjectHighlightColorKeys
          .nextElement();
      Vector<Integer> textObjects = textObjectsToUnhighlight
          .get(currentHLColor);
      // Convert the Vector<Integer> to an int[]
      int[] textObjectIDs = new int[textObjects.size()];
      for (int i = 0; i < textObjectIDs.length; i++)
        textObjectIDs[i] = textObjects.get(i).intValue();

      ColorChanger textObjectUnHL = new ColorChanger(step, textObjectIDs,
          duration, "color", new Color(Integer.parseInt(currentHLColor)));
      textObjectUnHL.setOffset(offset);
      textObjectUnHL.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      BasicParser.addAnimatorToAnimation(textObjectUnHL, anim);
    }
    Enumeration<String> collectedBorderHighlightColorKeys = bordersToUnhighlight
        .keys();
    while (collectedBorderHighlightColorKeys.hasMoreElements()) {
      String currentHLColor = collectedBorderHighlightColorKeys.nextElement();
      Vector<Integer> borders = bordersToUnhighlight.get(currentHLColor);
      // Convert the Vector<Integer> to an int[]
      int[] borderIDs = new int[borders.size()];
      for (int i = 0; i < borderIDs.length; i++)
        borderIDs[i] = borders.get(i).intValue();

      ColorChanger borderUnHL = new ColorChanger(step, borderIDs, duration,
          "color", new Color(Integer.parseInt(currentHLColor)));
      borderUnHL.setOffset(offset);
      borderUnHL.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      BasicParser.addAnimatorToAnimation(borderUnHL, anim);
    }
  }// endof unhighlightgridcell

  /**
   * Aligns the content of a cell, a line, column or grid. Possible alignments
   * are ALIGN_LEFT ALIGN_RIGHT and ALIGN_CENTER
   * 
   * @param gridName
   *          Name of the grid
   * @param line
   *          number of the line the cell is in. Numbering starts with zero. If
   *          the parameter is set to -1, all lines are selected, which means a
   *          whole column or the whole grid will be aligned.
   * @param column
   *          number of the column the cell is in. Numbering starts with zero.
   *          If the parameter is set to -1, all columns are selected, which
   *          means a whole line or the whole grid will be aligned.
   * @param alignment
   *          Alignment. Use GridProducer.ALIGN_***
   * @param step
   *          The step in which the aligning happens
   * @param duration
   *          duration of the aligning (affects created move animator)
   * @param offset
   *          Time before the aligning happens (affects created move animator)
   * @param unit
   *          The unit duration and offset are measured in: "ticks" or "ms"
   */
  public static void alignGridValue(String gridName, int line, int column,
      int alignment, int step, int duration, int offset, String unit) {
    // check if coordinates are valid
    int[] size = getObjectProperties().getIntArrayProperty(
        gridSizeKey(gridName));
    if ((line >= size[0]) || (column >= size[1])) {
      MessageDisplay.errorMsg("Grid index " + gridName + "[" + line + "]["
          + column + "] is out of bounds. Valid cells are [0.." + size[0]
          + "][0.." + size[1], MessageDisplay.RUN_ERROR);
      return;
    }

    // Different to other set-methods, we dont create an animator to align the
    // Value.
    // Because of this, we dont need to collect the IDs of the text objects
    // either.
    // The necessary movement is done with a grid-refresh at the end of the
    // method.

    if (line == DEFAULT_INT)
      if (column == DEFAULT_INT) {// ## whole grid -> loop over all cells
                                  // #####################
        for (int l = 0; l < size[0]; l++)
          for (int c = 0; c < size[1]; c++)
            getObjectProperties().put(gridTextAlignmentKey(gridName, l, c),
                alignment);
      } else {// (l<0) & (c>0) ######### whole column -> loop over lines
              // #######################
        for (int l = 0; l < size[0]; l++)
          getObjectProperties().put(gridTextAlignmentKey(gridName, l, column),
              alignment);
      }

    else if (column == DEFAULT_INT) {// & (l>0) # whole line -> loop over
                                      // columns ########################
      for (int c = 0; c < size[1]; c++)
        getObjectProperties().put(gridTextAlignmentKey(gridName, line, c),
            alignment);
    }

    else { // (l>0) & (c>0) ######## just one cell
            // #########################################
      getObjectProperties().put(gridTextAlignmentKey(gridName, line, column),
          alignment);
    }

    refreshGrid(gridName, step, duration, offset, unit);
  }// endof aligngridvalue

  /**
   * Modifies the Font used in a single gridcell, a column, a line or the whole
   * grid. Modifing means updating the objectProperties Hashtable and creating
   * an setFontAnimator for the visual update. If refresh is set to true the
   * grid is recalculated afterwards.
   * 
   * The Grid will be recalculated and necessary resizing is made. As this can
   * result in many Move-Animators, this parameter should not be set, if
   * setGridValue is called right again another time.
   * 
   * @param gridName
   *          Name of the grid
   * @param line
   *          number of the line the cell is in. Numbering starts with zero. If
   *          the parameter is set to -1, all lines are selected, which means a
   *          whole column or the whole grid will be affected.
   * @param column
   *          number of the column the cell is in. Numbering starts with zero.
   *          If the parameter is set to -1, all columns are selected, which
   *          means a whole line or the whole grid will be affected.
   * @param newFont
   *          The font to switch to
   * @param step
   *          The step in which the change happens
   * @param duration
   *          the duration of the change (affects possible refresh)
   * @param offset
   *          Time before the change happens (affects possible refresh)
   * @param unit
   *          The unit duration and offset are measured in: "ticks" or "ms"
   * @param refresh
   *          If set to true refreshgrid() is called.
   */
  public static void setGridFont(String gridName, int line, int column,
      Font newFont, int step, int duration, int offset, String unit,
      boolean refresh) {
    // validate coordinates
    int[] size = getObjectProperties().getIntArrayProperty(
        gridSizeKey(gridName));
    if ((line >= size[0]) || (column >= size[1])) {
      MessageDisplay.errorMsg("Grid index " + gridName + "[" + line + "]["
          + column + "] is out of bounds. Valid cells are [0.." + size[0]
          + "][0.." + size[1], MessageDisplay.RUN_ERROR);
      return;
    }

    int[] cellFontsToUpdate;
    if (line == DEFAULT_INT)
      if (column == DEFAULT_INT) { // whole grid
                                    // #############################################
        cellFontsToUpdate = new int[size[0] * size[1]];
        for (int l = 0; l < size[0]; l++)
          for (int c = 0; l < size[1]; c++) {
            cellFontsToUpdate[l * size[1] + c] = getObjectIDs().getIntProperty(
                gridTextKey(gridName, l, c));
            getObjectProperties().put(gridTextFontKey(gridName, l, c), newFont);
          }
      } else {// ##################### whole column -> loop over lines
              // ########################
        cellFontsToUpdate = new int[size[0]];
        for (int l = 0; l < size[0]; l++) {
          getObjectProperties().put(gridTextFontKey(gridName, l, column),
              newFont);
          cellFontsToUpdate[l] = getObjectIDs().getIntProperty(
              gridTextKey(gridName, l, column));
        }
      }

    else if (column == DEFAULT_INT) { // ####### whole line -> loop over columns
                                      // ########################
      cellFontsToUpdate = new int[size[1]];
      for (int c = 0; c < size[1]; c++) {
        getObjectProperties().put(gridTextFontKey(gridName, line, c), newFont);
        cellFontsToUpdate[c] = getObjectIDs().getIntProperty(
            gridTextKey(gridName, line, c));
      }
    } else { // ##################### just one cell
              // ##########################################
      cellFontsToUpdate = new int[] { getObjectIDs().getIntProperty(
          gridTextKey(gridName, line, column)) };
      getObjectProperties().put(gridTextFontKey(gridName, line, column),
          newFont);
    }

    // Create setFontAnimator for collected IDs
    SetFont setFont = new SetFont(step, cellFontsToUpdate, 0,
        offset + duration, unit, newFont);
    BasicParser.addAnimatorToAnimation(setFont, anim);

    if (refresh)
      refreshGrid(gridName, step, duration, offset, unit);
  }// endof setgridfont

  /**
   * Recalculates and repairs the grid (if necessary) by creating several move
   * animators to shifht and resize cells.
   * 
   * @param gridName
   *          Name of the grid
   * @param step
   *          Step in which the grid should be refreshed
   * @param duration
   *          Time the resizes and shifts should take
   * @param offset
   *          Time before the grid resizes
   * @param unit
   *          The unit duration and offset are measured in: "ticks" or "ms"
   */
  public static void refreshGrid(String gridName, int step, int duration,
      int offset, String unit) {
    // REFRESH: get properties, calculate new coordinates, create moves, update
    // properties

    Point location = getObjectProperties().getPointProperty(
        gridLocationKey(gridName));
    int style = getObjectProperties().getIntProperty(gridStyleKey(gridName));
    int minHeight = getObjectProperties().getIntProperty(
        gridCellHeightKey(gridName));
    int maxHeight = getObjectProperties().getIntProperty(
        gridMaxCellHeightKey(gridName));
    int minWidth = getObjectProperties().getIntProperty(
        gridCellWidthKey(gridName));
    int maxWidth = getObjectProperties().getIntProperty(
        gridMaxCellWidthKey(gridName));
    int[] size = getObjectProperties().getIntArrayProperty(
        gridSizeKey(gridName));

    int[] lineHeights = new int[size[0]];
    int[] colWidths = new int[size[1]];
    String[][] values = new String[size[0]][size[1]];
    Font[][] fonts = new Font[size[0]][size[1]];
    int[][] alignments = new int[size[0]][size[1]];
    int[][][] textPositions = new int[size[0]][size[1]][2]; // line, column,
                                                            // coords
    int[][][] backPositions = new int[size[0]][size[1]][4]; // line->column->x,y,width,height
    int[][] bracketPositions = new int[6][6]; // part, coords&size

    // read values:
    Font currentFont;
    String currentValue;
    for (int l = 0; l < size[0]; l++)
      for (int c = 0; c < size[1]; c++) {
        currentFont = getObjectProperties().getFontProperty(
            gridTextFontKey(gridName, l, c));
        currentValue = getObjectProperties().getProperty(
            gridTextStringKey(gridName, l, c));
        if (currentFont == null) {
          System.err.println("Font unter Key '"
              + gridTextFontKey(gridName, l, c) + "' gleich null");
          System.err.println("Existiert Key? "
              + getObjectProperties().containsKey(
                  gridTextFontKey(gridName, l, c)));
          // currentFont = new Font("SansSerif",Font.PLAIN,26);
        }
        fonts[l][c] = currentFont; // new Font(currentFont,Font.PLAIN,26);
        values[l][c] = currentValue;
        alignments[l][c] = getObjectProperties().getIntProperty(
            gridTextAlignmentKey(gridName, l, c));
      } // end for-loops

    // reclalc dimensions

    GridMath.calculateLineHeights(lineHeights, fonts, minHeight, maxHeight,
        style);
    GridMath.calculateColumnWidths(colWidths, values, fonts, minWidth,
        maxWidth, style);
    GridMath.calculateTextPositions(textPositions, values, fonts, alignments,
        colWidths, lineHeights, location, style);
    GridMath.calculateBackgroundPositions(backPositions, colWidths,
        lineHeights, location, style);
    GridMath.calculateBracketPositions(bracketPositions, colWidths,
        lineHeights, location, style);

    // position text via move

    // int[] obNums = new int[size[0] * size[1]];
    // int obNumIndex=0;
    Hashtable<String, Vector<Integer>> collectedMoves = new Hashtable<String, Vector<Integer>>();
    Hashtable<String, Vector<Integer>> collectedResizes = new Hashtable<String, Vector<Integer>>();
    // collected Resizes wouldnt be needet, if the PolylineNodes could be
    // accessed
    // by an objectID. In that case we had collected them as the other Objects.
    // The solution used atm, is to collect them in a second hashtable, on its
    // Elements another move-method is applied ("translate 2 3" to resize the
    // boxes)

    // XProperties collectedMoves = new XProperties();
    Point currentCoords;
    for (int l = 0; l < size[0]; l++)
      for (int c = 0; c < size[1]; c++) {
        // shift text
        currentCoords = getObjectProperties().getPointProperty(
            gridTextPositionKey(gridName, l, c));
        collectObjectMovements(getObjectIDs().getIntProperty(
            gridTextKey(gridName, l, c)), textPositions[l][c][0]
            - currentCoords.x, textPositions[l][c][1] - currentCoords.y,
            collectedMoves);
        getObjectProperties().put(gridTextPositionKey(gridName, l, c),
            new Point(textPositions[l][c][0], textPositions[l][c][1]));
        // shift box
        currentCoords = getObjectProperties().getPointProperty(
            gridBackPositionKey(gridName, l, c));
        collectObjectMovements(getObjectIDs().getIntProperty(
            gridBackKey(gridName, l, c)),// +"vert")[0],
            backPositions[l][c][0] - currentCoords.x, backPositions[l][c][1]
                - currentCoords.y, collectedMoves);
        getObjectProperties().put(gridBackPositionKey(gridName, l, c),
            new Point(backPositions[l][c][0], backPositions[l][c][1]));
        // resize box
        currentCoords = getObjectProperties().getPointProperty(
            gridBackSizeKey(gridName, l, c));
        collectObjectMovements(getObjectIDs().getIntProperty(
            gridBackKey(gridName, l, c)), backPositions[l][c][2]
            - currentCoords.x, backPositions[l][c][3] - currentCoords.y,
            collectedResizes);
        getObjectProperties().put(gridBackSizeKey(gridName, l, c),
            new Point(backPositions[l][c][2], backPositions[l][c][3]));

      } // endfor

    if (style == STYLE_MATRIX) {
      // position brackets via move and scale

      // before moving the parts, we calc the height-difference:
      // dy = new Center of part1 - old Center of part2
      int dy = bracketPositions[2][1]
          - getObjectProperties().getPointProperty(
              gridBracketPositionKey(gridName, 2)).y;

      // move the parts
      for (int p = 0; p < 6; p++) {
        currentCoords = getObjectProperties().getPointProperty(
            gridBracketPositionKey(gridName, p));
        collectObjectMovements(getObjectIDs().getIntProperty(
            gridBracketKey(gridName, p)), bracketPositions[p][0]
            - currentCoords.x, bracketPositions[p][1] - currentCoords.y,
            collectedMoves);
        getObjectProperties().put(gridBracketPositionKey(gridName, p),
            new Point(bracketPositions[p][0], bracketPositions[p][1]));
      }
      // strech the brackets (we dont need a hashtable here and we create the
      // move just here)
      if (dy != 0) {
        PTPolyline bracketStrechMoveBase = new PTPolyline(new int[] { 0, 0 },
            new int[] { 0, dy });
        Move bracketStrech = new Move(step, new int[] {
            getObjectIDs().getIntProperty(gridBracketKey(gridName, 1)),
            getObjectIDs().getIntProperty(gridBracketKey(gridName, 4)) },
            duration, "translateNodes 2", bracketStrechMoveBase.getNum(true));
        bracketStrech.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
        BasicParser.addGraphicObject(bracketStrechMoveBase, anim);
        BasicParser.addAnimatorToAnimation(bracketStrech, anim);
      }

    }// endif matrix

    // now create Moves for each vector of objectnums

    Enumeration<String> collectedMoveKeys = collectedMoves.keys();
    while (collectedMoveKeys.hasMoreElements()) {
      String currentMovementS = collectedMoveKeys.nextElement();
      Vector<Integer> objectIDsToMoveV = collectedMoves.get(currentMovementS);
      // we have to extract the integer-Values from the movement describing
      // String:
      String[] currentMovementArray = currentMovementS.split(",");

      PTPolyline currentMoveBase = new PTPolyline(new int[] { 0,
          Integer.parseInt(currentMovementArray[0]) }, new int[] { 0,
          Integer.parseInt(currentMovementArray[1]) }); // array of xcoords and
                                                        // array of ycoords
      currentMoveBase.setDepth(10);
      currentMoveBase.getNum(true);
      int[] objectIDsToMoveA = new int[objectIDsToMoveV.size()];
      for (int i = 0; i < objectIDsToMoveA.length; i++)
        objectIDsToMoveA[i] = objectIDsToMoveV.get(i).intValue();
      // objectIDsToMoveA[i]=(int)((Integer)objectIDsToMoveV.get(i));
      Move currentMoveAnimation = new Move(step, objectIDsToMoveA, duration,
          "translate", currentMoveBase.getNum(true));
      currentMoveAnimation.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));

      BasicParser.addGraphicObject(currentMoveBase, anim);
      BasicParser.addAnimatorToAnimation(currentMoveAnimation, anim);
      // BasicParser.addAnimatorToAnimation(new
      // TimedShow(step,currentMoveBase.getNum(true), 0, null, true));
    }

    // because resizing uses another method (and the polylinevertices are not
    // accessible nativly)
    // we have to use a second hashtable to resize. Here we have to create two
    // moves: one in x and one in y.
    // the x-move works on nodes 3 and 4, the y-move on the nodes 2 and 3
    //
    // 1 ------ 4 The nodes of the Polyline are numbered
    // | | from 1 to 4 (not 0 to 3!)
    // 2 ------ 3
    Enumeration<String> collectedResizeKeys = collectedResizes.keys();
    while (collectedResizeKeys.hasMoreElements()) {
      String currentMovementS = collectedResizeKeys.nextElement();
      Vector<Integer> objectIDsToMoveV = collectedResizes.get(currentMovementS);
      // we have to extract the integer-Values from the movement describing
      // String:
      String[] currentMovementArray = currentMovementS.split(",");

      PTPolyline currentMoveBaseX = new PTPolyline(new int[] { 0,
          Integer.parseInt(currentMovementArray[0]) }, // array of xcoords
          new int[] { 2, 2 }); // array of ycoords
      currentMoveBaseX.setDepth(10);
      currentMoveBaseX.getNum(true);
      currentMoveBaseX.setColor(new Color(0.5f, 0.75f, 0.25f));

      PTPolyline currentMoveBaseY = new PTPolyline(new int[] { 3, 3 }, // array
                                                                        // of
                                                                        // xcoords
          new int[] { 4, 4 + Integer.parseInt(currentMovementArray[1]) }); // array
                                                                            // of
                                                                            // ycoords
      currentMoveBaseY.setDepth(10);
      currentMoveBaseY.getNum(true);
      currentMoveBaseY.setColor(new Color(0.5f, 0.75f, 0.25f));

      // Convert the Vector<Integer> to an int[]
      int[] objectIDsToMoveA = new int[objectIDsToMoveV.size()];
      for (int i = 0; i < objectIDsToMoveA.length; i++)
        objectIDsToMoveA[i] = objectIDsToMoveV.get(i).intValue();

      Move currentMoveAnimationX = new Move(step, objectIDsToMoveA, duration,
          "translateNodes 3 4", currentMoveBaseX.getNum(true));
      Move currentMoveAnimationY = new Move(step, objectIDsToMoveA, duration,
          "translateNodes 2 3", currentMoveBaseY.getNum(true));
      currentMoveAnimationX.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      currentMoveAnimationY.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      BasicParser.addGraphicObject(currentMoveBaseX, anim);
      BasicParser.addGraphicObject(currentMoveBaseY, anim);
      BasicParser.addAnimatorToAnimation(currentMoveAnimationX, anim);
      BasicParser.addAnimatorToAnimation(currentMoveAnimationY, anim);
      // BasicParser.addAnimatorToAnimation(new
      // TimedShow(step,currentMoveBaseX.getNum(true), 0, null, true));
      // BasicParser.addAnimatorToAnimation(new
      // TimedShow(step,currentMoveBaseY.getNum(true), 0, null, true));
    }

  }// endof refreshgrid
}