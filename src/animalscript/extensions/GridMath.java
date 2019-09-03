package animalscript.extensions;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTOpenCircleSegment;
import animal.graphics.PTPolyline;
import animal.graphics.PTText;
import animal.main.Animal;

/**
 * This class provides a collection of methods, used to calculate locations of
 * graphical objects building a grid
 * 
 * @author <a href="mailto:here@christoph-preisser.de">Christoph Prei&szlig;er</a>
 * @version 1.00 2007-01-25
 */
public class GridMath {

  public GridMath() {
    // do nothing here
  }

  // Values depending on chosen style:
  static final int DEFAULT_MATRIX_CELL_SPACING = 12; // hrz. pixels between
                                                      // matrix

  // cells
  static final int DEFAULT_PLAIN_CELL_SPACING = 8; // hrz. pixels between plain
                                                    // cells

  static final int DEFAULT_TABLE_CELL_SPACING = 8; // hrz. pixels between table

  // cells

  static final int DEFAULT_MATRIX_BORDER = 10; // hrz. and vert. pixles around

  // matrix grid

  static final int DEFAULT_PLAIN_BORDER = 0; // hrz. and vert. pixles around

  // plain grid

  static final int DEFAULT_TABLE_BORDER = 0; // hrz. and vert. pixles around

  // table grid

  // static aliases (used in switches, same as in GridProducer)
  public static final int STYLE_PLAIN = GridProducer.STYLE_PLAIN;

  public static final int STYLE_MATRIX = GridProducer.STYLE_MATRIX;

  public static final int STYLE_TABLE = GridProducer.STYLE_TABLE;

  public static final int ALIGN_LEFT = GridProducer.ALIGN_LEFT;

  public static final int ALIGN_CENTER = GridProducer.ALIGN_CENTER;

  public static final int ALIGN_RIGHT = GridProducer.ALIGN_RIGHT;

  /**
   * Calculates the nett width of each column depending on its contents and the
   * <code>min</code> and <code>max</code> parameters. (Nett means not to
   * respect cellborders or paddings)
   * 
   * @param colWidths
   *          array to store the width of each column
   * @param content
   *          Gridcontent (2dim Array)
   * @param font
   *          Fonts used in the cells (2dim Array)
   * @param min
   *          minimum width of each cell
   * @param max
   *          maximum width of each cell
   * @param style
   *          does not affect width of the colums atm.
   */
  public static void calculateColumnWidths(int colWidths[], String content[][],
      Font font[][], int min, int max, int style) {
    // find the greatest width for each column, compare with min and max
    FontMetrics fm; // calculates size of a String
    int currentSize;
    int biggest = 0; // used for style "junctions"
    for (int i = 0; i < colWidths.length; i++)
      colWidths[i] = min;

    for (int col = 0; col < content[0].length; col++) {
      for (int line = 0; line < content.length; line++) {
        fm = Animal.getConcreteFontMetrics(font[line][col]);
        currentSize = fm.stringWidth(content[line][col]);
        if (currentSize > colWidths[col]) {
          colWidths[col] = currentSize;
          if ((colWidths[col] > max) && (max > 0)) {
            colWidths[col] = max;
            break; // maximum reached
          }// endif max
          if (colWidths[col] > biggest)
            biggest = colWidths[col];
        }// endif greater
      }// endfor lines
      if (colWidths[col] > biggest)
        biggest = colWidths[col];
    }// endfor cols
  }

  /**
   * Calculates the nett width of each column depending on its contents and the
   * <code>min</code> and <code>max</code> parameters. (Nett means not to
   * respect cellborders or paddings)
   * 
   * @param colWidths
   *          array to store the width of each column
   * @param content
   *          Gridcontent (2dim Array)
   * @param min
   *          minimum width of each cell
   * @param max
   *          maximum width of each cell
   * @param style
   *          does not affect width of the colums atm.
   */
  public static void calculateColumnWidths(int colWidths[], PTText content[][],
      int min, int max, int style) {
    // find the greatest width for each column, compare with min and max

    String[][] contentString = new String[content.length][content[0].length];
    Font[][] contentFont = new Font[content.length][content[0].length];
    for (int l = 0; l < content.length; l++)
      for (int c = 0; c < content[0].length; c++) {
        contentString[l][c] = content[l][c].getText();
        contentFont[l][c] = content[l][c].getFont();
      }
    calculateColumnWidths(colWidths, contentString, contentFont, min, max,
        style);
  }// end calculateColWidths

  /**
   * Calculates the net width of each column. (Nett means not to respect
   * cellborders or paddings) This incarnation of
   * <code>calulateColumnWidth</code> is used for empty grids and therefore it
   * does nothing than fill <code>colWidths[]</code> with <code>min</code>.
   * 
   * @param colWidths
   *          array to store the width of each column
   * @param min
   *          minimum width of each cell
   */
  public static void calculateColumnWidths(int colWidths[], int min) {
    // calculate positions for an empty grid - max-width and style are not
    // relevant here
    for (int col = 0; col < colWidths.length; col++)
      colWidths[col] = min;
  }

  /**
   * Calculates the net width of each column. (Nett means not to respect
   * cellborders or paddings) This incarnation of
   * <code>calulateColumnWidth</code> is used for empty grids and therefore it
   * does nothing than fill <code>colWidths[]</code> with <code>min</code>.
   * 
   * @param colWidths
   *          array to store the width of each column
   * @param min
   *          minimum width of each cell
   * @param font
   *          the Font used for the elements
   */
  public static void calculateColumnWidths(int colWidths[], int min, Font font) {
    // calculate positions for an empty grid - max-width and style are not
    // relevant here
    for (int col = 0; col < colWidths.length; col++)
      colWidths[col] = min;
  }

  /**
   * Calculates the nett height of each line depending on its contents and the
   * <code>min</code> and <code>max</code> parameters.
   * 
   * @param lineHeights
   *          array to store the height of each line
   * @param font
   *          Font used in the cells (2dim Array)
   * @param min
   *          minimum height of each cell
   * @param max
   *          maximum height of each cell
   * @param style
   *          does not affect width of the colums atm.
   */
  public static void calculateLineHeights(int lineHeights[], Font font[][],
      int min, int max, int style) {
    // find the greatest height for each line, compare with min and max
    // Style only determines the way how an entry influences the heights.
    // The style-specific spacing between lines is NOT relevant here.
    // That is used in the "position" methods

    FontMetrics fm; // calculates size of a String
    int currentSize;
    int biggest = 0; // the highest element in the grid, used for "junctions"
    for (int i = 0; i < lineHeights.length; i++)
      lineHeights[i] = min;

    for (int line = 0; line < font.length; line++) {
      for (int col = 0; col < font[0].length; col++) {
        // System.out.print("l=" + line + "/" +font.length+ ", c=" + col+ "/"
        // +font[0].length);
        if (font[line][col] == null) {
          System.out.println("   = NULL! break!");
          return;
        }
        fm = Animal.getConcreteFontMetrics(font[line][col]);
        currentSize = fm.getHeight();
        if (currentSize > lineHeights[line]) {
          lineHeights[line] = currentSize;
          if ((lineHeights[line] > max) && (max > 0)) {
            lineHeights[line] = max;
            break; // maximum reached
          }// endif max
          if (lineHeights[line] > biggest)
            biggest = lineHeights[line];
        }// endif greater

      }// endfor cols
    }// endfor lines
  }

  /**
   * Calculates the nett height of each line depending on its contents and the
   * <code>min</code> and <code>max</code> parameters.
   * 
   * @param lineHeights
   *          array to store the height of each line
   * @param content
   *          Gridcontent (2dim Array)
   * @param min
   *          minimum height of each cell
   * @param max
   *          maximum height of each cell
   * @param style
   *          does not affect width of the colums atm.
   */
  public static void calculateLineHeights(int lineHeights[],
      PTText content[][], int min, int max, int style) {
    // find the greatest height for each line, compare with min and max
    // Style only determines the way, an entry influences the heights.
    // The style-specific spacing between lines is NOT relevant here.
    // Its flows in in the "position" methods
    Font[][] font = new Font[content.length][(content[0]).length];
    for (int l = 0; l < content.length; l++)
      for (int c = 0; c < content[0].length; c++) {
        // System.out.print("l = " + l + ", c = " + c);
        font[l][c] = content[l][c].getFont();
      }
    calculateLineHeights(lineHeights, font, min, max, style);
  }

  /**
   * Calculates the nett height of each line. Used for empty grids and therefor
   * it does nothing than fill <code>lineHeights[]
   * </code> with
   * <code>min</code>.
   * 
   * @param lineHeights
   *          array to store the height of each line
   * @param min
   *          minimum height of each cell
   */
  public static void calculateLineHeights(int lineHeights[], int min) {
    // calculate positions for an empty grid - max-Height and style are not
    // relevant here
    for (int line = 0; line < lineHeights.length; line++)
      lineHeights[line] = min;
  }

  /**
   * Calculates the nett height of each line. Used for empty grids and therefor
   * it does nothing than fill <code>lineHeights[]
   * </code> with
   * <code>min</code>.
   * 
   * @param lineHeights
   *          array to store the height of each line
   * @param min
   *          minimum height of each cell
   * @param font
   *          the Font to be used
   */
  public static void calculateLineHeights(int lineHeights[], int min, Font font) {
    int actualMinSize = min;
    if (font != null && font.getSize() > min)
      actualMinSize = font.getSize();
    GridMath.calculateLineHeights(lineHeights, actualMinSize);
  }

  /**
   * Takes an array of <code>PTText</code> (the Content of the grid) an sets
   * the location-attribute for each Pttext depending on given attributes like
   * the grids location, its style, the alignment and the widths and hights of
   * the grids columns and lines.
   * 
   * @param values
   *          content wich has to be positioned
   * @param alignments
   *          alignment of each value
   * @param widths
   *          width of each column - see: <code>calculateColumnWidths</code>
   * @param heights
   *          height of each line - see: <code>calculateLineHeights</code>
   * @param location
   *          location of the grid (upper left corner)
   * @param style
   *          style of the grid (Table, Matrix, ...)
   */
  public static void calculateTextPositions(PTText[][] values,
      int[][] alignments, int[] widths, int[] heights, Point location, int style) {
    // This variant of calculateTextPositions is used for the Gridcreation,
    // where PTTexts are
    // accessible.
    // Because the location of a String determinenes its baseline instead of its
    // upper edge,
    // we need to shift it downwards "maxAscend()" pixels. So we need the
    // FontMetrics once more...
    FontMetrics fm; // calculates size of a String.

    int[] cursor = { location.x, location.y }; // we use this as marker to
                                                // remember,
    // where the next text has to be drawn

    int border = 0; // additional Space around the Grid for Lines or
                    // Matrix-Brackets
    int cellSpacing = 0; // space between Cells
    switch (style) {
    case STYLE_PLAIN: // "plain": no cellborders, brackets... just text
      border = DEFAULT_PLAIN_BORDER;
      cellSpacing = DEFAULT_PLAIN_CELL_SPACING;
      break;
    case STYLE_MATRIX: // "matrix": no cellborders, but brackets, so a border
                        // around the grid is needet
      border = DEFAULT_MATRIX_BORDER;
      cellSpacing = DEFAULT_MATRIX_CELL_SPACING;
      break;
    case STYLE_TABLE: // "table": cellborders, so we need space between the
                      // cells, and around the grid
      border = DEFAULT_TABLE_BORDER;
      cellSpacing = DEFAULT_TABLE_CELL_SPACING;
      break;
    }

    cursor[0] += border; // space for bracets or tableborders -- if there are
                          // any
    cursor[1] += border;
    int leftb = cursor[0];
    int x, y; // where the text should be placed
    // loop to set positions of each text-Object. (line-wise)
    // now, the cursor is set to the first position
    for (int line = 0; line < values.length; line++) {
      for (int col = 0; col < values[0].length; col++) {
        fm = Animal.getConcreteFontMetrics(values[line][col].getFont());
        if (alignments[line][col] == ALIGN_LEFT) // left
          // alignleft: cursor pos plus 1/2 of the spacing (the other half is
          // supposed to be on the right):
          x = cursor[0] + cellSpacing / 2;
        else if (alignments[line][col] == ALIGN_RIGHT) {// Right
          // alignRight cursor plus 1/2 spacing plus remaining space (colwidth
          // minus width of the String)
          x = cursor[0] + cellSpacing / 2
              + (widths[col] - fm.stringWidth(values[line][col].getText()));
//          System.err.println("right: was x=" +cursor[0] +", change to " +(cursor[0]+cellSpacing/2)
//              +" for left; cellSpacing: " +cellSpacing +", colW for col " +col +": " +widths[col]
//              +", sw for '" +values[line][col].getText()
//              + "': " +fm.stringWidth(values[line][col].getText()) +", adapt to 'right': " +x);
        }
        else {
          // alignCenter cursor pos plus 1/2 of the spacing plus 1/2 of the
          // remaining space
          x = cursor[0] + cellSpacing / 2
              + (widths[col] - fm.stringWidth(values[line][col].getText())) / 2;
        }
        // TODO a vertical alignment could be implemented here:
        y = cursor[1] + heights[line] - fm.getMaxDescent();
        values[line][col].setPosition(x, y); 
        cursor[0] += widths[col]; // "step right"
        cursor[0] += cellSpacing; // "jump over the column-border" (if any)
      } // endfor columns
      cursor[0] = leftb;
      cursor[1] += heights[line]; // "step down"
      cursor[1] += cellSpacing; // "jump over the cellspacing"
    } // endfor lines

  }

  /**
   * Takes arrays of <code>String</code> and <code>Font</code> (the Content
   * of the grid) and stores the location for each Cell depending on given
   * attributes like the grids location, its style, the alignment and the widths
   * and hights of the grids columns and lines.
   * 
   * @param textPosition
   *          3dim Array to store the location: [line][column]->{x,y}
   * @param values
   *          content wich has to be positioned
   * @param font
   *          font of the content wich has to be positioned
   * @param alignments
   *          alignment of each value
   * @param widths
   *          width of each column - see: <code>calculateColumnWidths</code>
   * @param heights
   *          height of each line - see: <code>calculateLineHeights</code>
   * @param location
   *          location of the grid
   * @param style
   *          style of the grid (Table, Matrix, ...)
   */
  public static void calculateTextPositions(int[][][] textPosition,
      String[][] values, Font[][] font, int[][] alignments, int[] widths,
      int[] heights, Point location, int style) {
    // Because the location of a String determinenes its baseline instead of its
    // upper edge,
    // we need to shift it downwards "maxAscend()" pixels. So we need the
    // FontMetrics once more...
    FontMetrics fm; // calculates size of a String.

    int[] cursor = { location.x, location.y }; // we use this as marker to
    // remember,
    // where the next text has to be drawn

    int border = 0; // additional Space around the Grid for Lines or
    // Matrix-Brackets
    int cellSpacing = 0; // space between Cells

    // style: plain = 0, matrix = 1, table = 2, junctions = 3
    switch (style) {
    case STYLE_PLAIN: // "plain": no cellborders, brackets... just text
      border = 0;
      cellSpacing = DEFAULT_PLAIN_CELL_SPACING;
      break;
    case STYLE_MATRIX: // "matrix": no cellborders, but brackets, so a border
      // around the grid is needet
      border = DEFAULT_MATRIX_BORDER;
      cellSpacing = DEFAULT_MATRIX_CELL_SPACING;
      break;
    case STYLE_TABLE: // "table": cellborders, so we need space between the
      // cells, and around the grid
      border = DEFAULT_TABLE_BORDER;
      cellSpacing = DEFAULT_TABLE_CELL_SPACING;
      break;
    }

    cursor[0] += border; // space for bracets or tableborders -- if there are
    // any
    cursor[1] += border;
    int leftb = cursor[0];
    // loop to set positions of each text-Object. (line-wise)
    // now, the cursor is set to the first position
    for (int line = 0; line < values.length; line++) {
      for (int col = 0; col < values[0].length; col++) {
        fm = Animal.getConcreteFontMetrics(font[line][col]);
        if (alignments[line][col] == ALIGN_LEFT) // left
          // alignleft: cursor pos plus 1/2 of the spacing (the other half is
          // supposed to be on the right):
          textPosition[line][col][0] = cursor[0] + cellSpacing / 2;
        else if (alignments[line][col] == ALIGN_RIGHT) // Right
          // alignRight cursor plus 1/2 spacing plus remaining space (colwidth
          // minus width of the String)
          textPosition[line][col][0] = cursor[0] + cellSpacing / 2
              + (widths[col] - fm.stringWidth(values[line][col]));
        else
          // alignCenter cursor pos plus 1/2 of the spacing plus 1/2 of the
          // remaining space
          textPosition[line][col][0] = cursor[0] + cellSpacing / 2
              + (widths[col] - fm.stringWidth(values[line][col])) / 2;
        textPosition[line][col][1] = cursor[1]; //+ fm.getMaxAscent();
        cursor[0] += widths[col]; // "step right"
        cursor[0] += cellSpacing; // "jump over the column-border" (if any)
      } // endfor columns
      cursor[0] = leftb;
      cursor[1] += heights[line]; // "step down"
      cursor[1] += cellSpacing; // "jump over the line-border" (if any)
    } // endfor lines

  }

  /**
   * calculate Positions of the background rectangles depending on given
   * attributes like the grids location, its style, the alignment and the widths
   * and hights of the grids columns and lines.
   * 
   * @param backgroundPositions
   *          3dim Array to store the location:
   *          [line][column]->{x,y,width,height}
   * @param widths
   *          width of each column - see: <code>calculateColumnWidths</code>
   * @param heights
   *          height of each line - see: <code>calculateLineHeights</code>
   * @param location
   *          location of the grid
   * @param style
   *          style of the grid (Table, Matrix, ...)
   */
  public static void calculateBackgroundPositions(
      int[][][] backgroundPositions, int[] widths, int[] heights,
      Point location, int style) {
    /*
     * This Method obviously is used, when drawing Tables (cellboxes) But we
     * also use it to highlight cell entries in plain and matrix style.
     */
    // first determine border and spacings
    int border = 0;
    int spacing = 0;
    switch (style) {
    case STYLE_PLAIN:
      border = DEFAULT_PLAIN_BORDER;
      spacing = DEFAULT_PLAIN_CELL_SPACING;
      break;
    case STYLE_TABLE:
      border = DEFAULT_TABLE_BORDER;
      spacing = DEFAULT_TABLE_CELL_SPACING;
      break;
    case STYLE_MATRIX:
      border = DEFAULT_MATRIX_BORDER;
      spacing = DEFAULT_MATRIX_CELL_SPACING;
      break;
    }

    // ...then save coordinates
    int[] cursor = { location.x + border, location.y + border }; // steps
    // through the
    // grid

    for (int col = 0; col < widths.length; col++) {
      for (int line = 0; line < heights.length; line++) {
        backgroundPositions[line][col][0] = cursor[0]; // x
        backgroundPositions[line][col][1] = cursor[1]; // y
        backgroundPositions[line][col][2] = widths[col] + spacing - 1; // width
        backgroundPositions[line][col][3] = heights[line] + spacing - 1; // height
        // now step down one cell for the next run:
        cursor[1] += (heights[line] + spacing);
      }// end for lines
      // now step right for the next column and begin at the top
      cursor[0] += (widths[col] + spacing);
      cursor[1] = location.y + border;
    } // end for cols
  }

  private static void adjustOpenCircleSegmentForMatrix(PTOpenCircleSegment segment, int x, int y, int startAngle) {
    segment.setCenter(new Point(x, y));
    segment.setClockwise(true);
    segment.setRadius(DEFAULT_MATRIX_BORDER);
    segment.setStartAngle(startAngle); // startangle
    segment.setTotalAngle(90);
    segment.setFWArrow(false);
    segment.setBWArrow(false);
  }
  
  /**
   * Calculates the positions of the objects building the brackets of a
   * matrix-styled grid.
   * 
   * @param brackets
   *          The graphic Objects which need to be positioned (UpperLeft arc,
   *          left line, bottomLeft arc, upperRight arc, right line, bottomRight
   *          arc)
   * @param widths
   *          Columnwidths
   * @param heights
   *          Lineheights
   * @param location
   *          the Grids location (Upper left corner)
   * @param style
   *          has no affect atm.
   */
  public static void calculateBracketPositions(PTGraphicObject[] brackets,
      int[] widths, int[] heights, Point location, int style) {
    /*
     * another dumb method, just to abstract the proceedings in makeGrid(). Its
     * quite similar to position text, but we need this only when to draw a
     * matrix.
     */
    // this variant of calculateBracketPosition works directly on GraphicObjects
    // which are returned then
    int matrixContentHeight = 0; // height without borders and brackets
    for (int l = 0; l < heights.length; l++)
      matrixContentHeight += heights[l];
    // so the resulting Height is that of the content, n-1 Spacings and a border
    // above and under the Matrixentries:
    int matrixHeight = matrixContentHeight
        + ((heights.length) * DEFAULT_MATRIX_CELL_SPACING)
        + (DEFAULT_MATRIX_BORDER * 2);

    int matrixContentWidth = 0; // width without borders and brackets
    for (int c = 0; c < widths.length; c++)
      matrixContentWidth += widths[c];
    int matrixWidth = matrixContentWidth
        + ((widths.length) * DEFAULT_MATRIX_CELL_SPACING)
        + (DEFAULT_MATRIX_BORDER * 2);

    /*
     * now, that we know the measurements of our matrix, we can generate the
     * parts of the brackets, arcs and lines we store them in an simple
     * PTGraphicObject Array, because outside this method we dont need to know
     * which kind of Object they are, as long we can be drawed :)
     * 
     * 0.....3
     * ./...\
     * 1|...|4
     * .\.../.
     * 2..... 5
     * 
     */
    PTOpenCircleSegment part0 = new PTOpenCircleSegment();
    PTOpenCircleSegment part2 = new PTOpenCircleSegment();
    PTOpenCircleSegment part3 = new PTOpenCircleSegment();
    PTOpenCircleSegment part5 = new PTOpenCircleSegment();
    // PTArc part0 = new PTArc();
    // PTArc part2 = new PTArc();
    // PTArc part3 = new PTArc();
    // PTArc part5 = new PTArc();
    PTPolyline part1 = new PTPolyline(new int[] { location.x, location.x },
        new int[] { location.y + DEFAULT_MATRIX_BORDER,
            location.y + matrixHeight - DEFAULT_MATRIX_BORDER });
    PTPolyline part4 = new PTPolyline(new int[] { location.x + matrixWidth,
        location.x + matrixWidth }, new int[] {
        location.y + DEFAULT_MATRIX_BORDER,
        location.y + matrixHeight - DEFAULT_MATRIX_BORDER });
    part1.setFWArrow(false);
    part1.setBWArrow(false);
    part4.setFWArrow(false);
    part4.setBWArrow(false);
    GridMath.adjustOpenCircleSegmentForMatrix(part0, location.x + DEFAULT_MATRIX_BORDER,
        location.y + DEFAULT_MATRIX_BORDER, 90);
    GridMath.adjustOpenCircleSegmentForMatrix(part2, location.x + DEFAULT_MATRIX_BORDER,
        location.y + matrixHeight - DEFAULT_MATRIX_BORDER, 180);
    GridMath.adjustOpenCircleSegmentForMatrix(part3, location.x + matrixWidth - DEFAULT_MATRIX_BORDER,
        location.y + DEFAULT_MATRIX_BORDER, 0);
    GridMath.adjustOpenCircleSegmentForMatrix(part5, location.x + matrixWidth - DEFAULT_MATRIX_BORDER,
        location.y + matrixHeight - DEFAULT_MATRIX_BORDER, 270);
//    part0.setClockwise(true);
//    part0.setCenter(new Point(location.x + DEFAULT_MATRIX_BORDER, location.y
//        + DEFAULT_MATRIX_BORDER));
//    part0.setRadius(DEFAULT_MATRIX_BORDER);
//    // part0.setRadius(new Point(DefaultBorderMatrix, //radiusx
//    // DefaultBorderMatrix)); //radiusy
//    part0.setStartAngle(90); // startangle
//    part0.setTotalAngle(90);
//    part0.setFWArrow(false);
//    part0.setBWArrow(false);
//    // part0.setArcAngle(90); //arcangle

//    part2.setClockwise(false);
//    part2.setCenter(new Point(location.x + DEFAULT_MATRIX_BORDER, location.y
//        + matrixHeight - DEFAULT_MATRIX_BORDER));
//    part2.setRadius(DEFAULT_MATRIX_BORDER);
//    // part2.setRadius(new Point (DefaultBorderMatrix,
//    // DefaultBorderMatrix)); //radiusy
//    part2.setStartAngle(180); // startangle
//    // part2.setArcAngle(90); //arcangle
//    part2.setTotalAngle(90);

//    part3.setClockwise(false);
//    part3.setCenter(new Point(location.x + matrixWidth - DEFAULT_MATRIX_BORDER,
//        location.y + DEFAULT_MATRIX_BORDER));
//    // part3.setRadius(new Point (DefaultBorderMatrix,
//    // DefaultBorderMatrix)); //radiusy
//    part3.setRadius(DEFAULT_MATRIX_BORDER);
//    part3.setStartAngle(0); // startangle
//    // part3.setArcAngle(90); //arcangle
//    part3.setTotalAngle(90);

//    part5.setClockwise(true);
//    part5.setCenter(new Point(location.x + matrixWidth - DEFAULT_MATRIX_BORDER,
//        location.y + matrixHeight - DEFAULT_MATRIX_BORDER));
//    // part5.setRadius(new Point (DefaultBorderMatrix,
//    // DefaultBorderMatrix)); //radiusy
//    part5.setRadius(DEFAULT_MATRIX_BORDER);
//    part5.setStartAngle(0); // startangle
//    // part5.setArcAngle(90); //arcangle
//    part5.setTotalAngle(90);

    brackets[0] = part0;
    brackets[1] = part1;
    brackets[2] = part2;
    brackets[3] = part3;
    brackets[4] = part4;
    brackets[5] = part5;
  }

  public static void calculateBracketPositionsWitoutMargin(PTGraphicObject[] brackets,
		  int matrixWidth, int matrixHeight, Point locationOld, int style) {
	  Point location = new Point(locationOld);
	  location.translate(-5, -5);
	  matrixWidth += 10;
	  matrixHeight += 10;

    /*
     * now, that we know the measurements of our matrix, we can generate the
     * parts of the brackets, arcs and lines we store them in an simple
     * PTGraphicObject Array, because outside this method we dont need to know
     * which kind of Object they are, as long we can be drawed :)
     * 
     * 0.....3
     * ./...\
     * 1|...|4
     * .\.../.
     * 2..... 5
     * 
     */
    PTOpenCircleSegment part0 = new PTOpenCircleSegment();
    PTOpenCircleSegment part2 = new PTOpenCircleSegment();
    PTOpenCircleSegment part3 = new PTOpenCircleSegment();
    PTOpenCircleSegment part5 = new PTOpenCircleSegment();
    // PTArc part0 = new PTArc();
    // PTArc part2 = new PTArc();
    // PTArc part3 = new PTArc();
    // PTArc part5 = new PTArc();
    PTPolyline part1 = new PTPolyline(new int[] { location.x, location.x },
        new int[] { location.y + DEFAULT_MATRIX_BORDER,
            location.y + matrixHeight - DEFAULT_MATRIX_BORDER });
    PTPolyline part4 = new PTPolyline(new int[] { location.x + matrixWidth,
        location.x + matrixWidth }, new int[] {
        location.y + DEFAULT_MATRIX_BORDER,
        location.y + matrixHeight - DEFAULT_MATRIX_BORDER });
    part1.setFWArrow(false);
    part1.setBWArrow(false);
    part4.setFWArrow(false);
    part4.setBWArrow(false);
    GridMath.adjustOpenCircleSegmentForMatrix(part0, location.x + DEFAULT_MATRIX_BORDER,
        location.y + DEFAULT_MATRIX_BORDER, 90);
    GridMath.adjustOpenCircleSegmentForMatrix(part2, location.x + DEFAULT_MATRIX_BORDER,
        location.y + matrixHeight - DEFAULT_MATRIX_BORDER, 180);
    GridMath.adjustOpenCircleSegmentForMatrix(part3, location.x + matrixWidth - DEFAULT_MATRIX_BORDER,
        location.y + DEFAULT_MATRIX_BORDER, 0);
    GridMath.adjustOpenCircleSegmentForMatrix(part5, location.x + matrixWidth - DEFAULT_MATRIX_BORDER,
        location.y + matrixHeight - DEFAULT_MATRIX_BORDER, 270);
//    part0.setClockwise(true);
//    part0.setCenter(new Point(location.x + DEFAULT_MATRIX_BORDER, location.y
//        + DEFAULT_MATRIX_BORDER));
//    part0.setRadius(DEFAULT_MATRIX_BORDER);
//    // part0.setRadius(new Point(DefaultBorderMatrix, //radiusx
//    // DefaultBorderMatrix)); //radiusy
//    part0.setStartAngle(90); // startangle
//    part0.setTotalAngle(90);
//    part0.setFWArrow(false);
//    part0.setBWArrow(false);
//    // part0.setArcAngle(90); //arcangle

//    part2.setClockwise(false);
//    part2.setCenter(new Point(location.x + DEFAULT_MATRIX_BORDER, location.y
//        + matrixHeight - DEFAULT_MATRIX_BORDER));
//    part2.setRadius(DEFAULT_MATRIX_BORDER);
//    // part2.setRadius(new Point (DefaultBorderMatrix,
//    // DefaultBorderMatrix)); //radiusy
//    part2.setStartAngle(180); // startangle
//    // part2.setArcAngle(90); //arcangle
//    part2.setTotalAngle(90);

//    part3.setClockwise(false);
//    part3.setCenter(new Point(location.x + matrixWidth - DEFAULT_MATRIX_BORDER,
//        location.y + DEFAULT_MATRIX_BORDER));
//    // part3.setRadius(new Point (DefaultBorderMatrix,
//    // DefaultBorderMatrix)); //radiusy
//    part3.setRadius(DEFAULT_MATRIX_BORDER);
//    part3.setStartAngle(0); // startangle
//    // part3.setArcAngle(90); //arcangle
//    part3.setTotalAngle(90);

//    part5.setClockwise(true);
//    part5.setCenter(new Point(location.x + matrixWidth - DEFAULT_MATRIX_BORDER,
//        location.y + matrixHeight - DEFAULT_MATRIX_BORDER));
//    // part5.setRadius(new Point (DefaultBorderMatrix,
//    // DefaultBorderMatrix)); //radiusy
//    part5.setRadius(DEFAULT_MATRIX_BORDER);
//    part5.setStartAngle(0); // startangle
//    // part5.setArcAngle(90); //arcangle
//    part5.setTotalAngle(90);

    brackets[0] = part0;
    brackets[1] = part1;
    brackets[2] = part2;
    brackets[3] = part3;
    brackets[4] = part4;
    brackets[5] = part5;
  }

  /**
   * Calculates the positions of the objects building the brackets of a
   * matrix-styled grid.
   * 
   * @param bracketPositions
   *          The array the Positions are stored in (UpperLeft arc, left line,
   *          bottomLeft arc, upperRight arc, right line, bottomRight arc)<br>
   *          backetPostitions[part][coord]<br>
   *          How to read arc coordinates:<br>
   *          backetPostitions[0][0] is xCoord of upper left arc <br>
   *          backetPostitions[0][1] is yCoord of upper left arc <br>
   *          backetPostitions[0][2] is xRadius of upper left arc <br>
   *          backetPostitions[0][3] is yRadius of upper left arc <br>
   *          backetPostitions[0][4] is startAngle of upper left arc <br>
   *          backetPostitions[0][5] is arcAngle of upper left arc <br>
   *          How to read line Coordinates:<br>
   *          backetPostitions[0][0] is x1Coord of left line <br>
   *          backetPostitions[0][1] is y1Coord of left line <br>
   *          backetPostitions[0][2] is x2Coord of left line <br>
   *          backetPostitions[0][3] is y2Coord of left line <br>
   *          backetPostitions[0][4] is unused <br>
   *          backetPostitions[0][5] is unused <br>
   * 
   * @param widths
   *          Columnwidths
   * @param heights
   *          Lineheights
   * @param location
   *          the Grids location (Upper left corner)
   * @param style
   */
  public static void calculateBracketPositions(int[][] bracketPositions,
      int[] widths, int[] heights, Point location, int style) {
    /*
     * another dumb method, just to abstract the proceedings in makeGrid(). Its
     * quite similar to position text, but we need this only when to draw a
     * matrix.
     */
    int matrixContentHeight = 0; // height without borders and brackets
    for (int l = 0; l < heights.length; l++)
      matrixContentHeight += heights[l];
    // so the resulting Height is that of the content, n-1 Spacings and a border
    // above and under the Matrixentries:
    int matrixHeight = matrixContentHeight
        + ((heights.length) * DEFAULT_MATRIX_CELL_SPACING)
        + (DEFAULT_MATRIX_BORDER * 2);

    int matrixContentWidth = 0; // width without borders and brackets
    for (int c = 0; c < widths.length; c++)
      matrixContentWidth += widths[c];
    int matrixWidth = matrixContentWidth
        + ((widths.length) * DEFAULT_MATRIX_CELL_SPACING)
        + (DEFAULT_MATRIX_BORDER * 2);

    /*
     * now, that we know the measurements of our matrix, we can generate the
     * parts of the brackets, arcs and lines we store them in an simple
     * PTGraphicObject Array, because outside this method we dont need to know
     * which kind of Object they are, as long we can be drawed :)
     * 
     * 0 3 / \ 1 | | 4 \ / 2 5
     * 
     */

    bracketPositions[0][0] = location.x + DEFAULT_MATRIX_BORDER;
    bracketPositions[0][1] = location.y + DEFAULT_MATRIX_BORDER;
    bracketPositions[0][2] = DEFAULT_MATRIX_BORDER; // radiusx
    bracketPositions[0][3] = DEFAULT_MATRIX_BORDER; // radiusy
    bracketPositions[0][4] = 180; // startangle
    bracketPositions[0][5] = 90; // arcangle

    bracketPositions[2][0] = location.x + DEFAULT_MATRIX_BORDER;
    bracketPositions[2][1] = location.y + matrixHeight - DEFAULT_MATRIX_BORDER;
    bracketPositions[2][2] = DEFAULT_MATRIX_BORDER; // radiusx
    bracketPositions[2][3] = DEFAULT_MATRIX_BORDER; // radiusy
    bracketPositions[2][4] = 180; // startangle
    bracketPositions[2][5] = 90; // arcangle

    bracketPositions[3][0] = location.x + matrixWidth - DEFAULT_MATRIX_BORDER;
    bracketPositions[3][1] = location.y + DEFAULT_MATRIX_BORDER;
    bracketPositions[3][2] = DEFAULT_MATRIX_BORDER; // radiusx
    bracketPositions[3][3] = DEFAULT_MATRIX_BORDER; // radiusy
    bracketPositions[3][4] = 0;
    bracketPositions[3][5] = 90;

    bracketPositions[5][0] = location.x + matrixWidth - DEFAULT_MATRIX_BORDER;
    bracketPositions[5][1] = location.y + matrixHeight - DEFAULT_MATRIX_BORDER;
    bracketPositions[5][2] = DEFAULT_MATRIX_BORDER; // radiusx
    bracketPositions[5][3] = DEFAULT_MATRIX_BORDER; // radiusy
    bracketPositions[5][4] = 0; // startangle
    bracketPositions[5][5] = 90; // arcangle

    bracketPositions[1][0] = location.x;
    bracketPositions[1][1] = location.y + DEFAULT_MATRIX_BORDER;
    bracketPositions[1][2] = location.x;
    bracketPositions[1][3] = location.y + matrixHeight - DEFAULT_MATRIX_BORDER;

    bracketPositions[4][0] = location.x + matrixWidth;
    bracketPositions[4][1] = location.y + DEFAULT_MATRIX_BORDER;
    bracketPositions[4][2] = location.x + matrixWidth;
    bracketPositions[4][3] = location.y + matrixHeight - DEFAULT_MATRIX_BORDER;
  }

}
