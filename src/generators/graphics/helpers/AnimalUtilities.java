package generators.graphics.helpers;

import java.awt.Color;

import algoanim.primitives.MatrixPrimitive;
import algoanim.primitives.SourceCode;

/**
 * Other utilities that simplify (or enable us at all) to use the glorious
 * AlgoAnim API.
 */
public class AnimalUtilities {

  /**
   * Returns a String for the setGridColor command which is not part of the
   * AlgoAnim API.<br>
   * Colors that are not needed can be omitted from the command by using null as
   * a parameter.
   * 
   * @param grid
   *          the StringMatrix command
   * @param row
   *          the row of the cell
   * @param col
   *          the column of the cell
   * @param color
   *          the color as a String or null
   * @param textColor
   *          the textColor as a String or null
   * @param fillColor
   *          the fillColor as a String or null
   * @param highlightTextColor
   *          the highlightTextColor as a String or null
   * @param highlightFillColor
   *          the highlightFillColor as a String or null
   * @return a String with the setGridColor command
   */
  public static String setGridColor(MatrixPrimitive grid, int row, int col,
      String color, String textColor, String fillColor,
      String highlightTextColor, String highlightFillColor) {
    StringBuilder builder = new StringBuilder();
    builder.append("setGridColor \"");
    builder.append(grid.getName());
    builder.append("[");
    builder.append(row);
    builder.append("][");
    builder.append(col);
    builder.append("]\"");
    if (color != null) {
      builder.append(" color ");
      builder.append(color);
    }
    if (textColor != null) {
      builder.append(" textColor ");
      builder.append(textColor);
    }
    if (fillColor != null) {
      builder.append(" fillColor ");
      builder.append(fillColor);
    }
    if (highlightTextColor != null) {
      builder.append(" highlightTextColor ");
      builder.append(highlightTextColor);
    }
    if (highlightFillColor != null) {
      builder.append(" highlightFillColor ");
      builder.append(highlightFillColor);
    }
    return builder.toString();
  }

  // the code line which was highlighted the last
  static int lastLineHighlight = -1;

  /**
   * This great method simplifies code highlighting by taking care of
   * unhighlighting the last line by itself. Terrific!
   * 
   * @param code
   *          the code which is to be highlighted
   * @param line
   *          the line of the code which is to be highlighted
   */
  public static void easyHighlight(SourceCode code, int line) {
    if (code != null) {
      if (lastLineHighlight != -1) {
        code.unhighlight(lastLineHighlight);
      }
      code.highlight(line);
      lastLineHighlight = line;
    }
  }

  /**
   * Create the Animal String representation of a color.
   * 
   * @param color
   *          the color
   * @return the color as "(r, g, b)"
   */
  public static String colorToString(Color color) {
    StringBuilder builder = new StringBuilder();
    builder.append("(");
    builder.append(color.getRed());
    builder.append(", ");
    builder.append(color.getGreen());
    builder.append(", ");
    builder.append(color.getBlue());
    builder.append(")");
    return builder.toString();
  }

  /**
   * Lighten a given color.
   * 
   * @param color
   *          the color
   * @return a lighter version of the given color
   */
  public static Color lightenColor(Color color) {
    int[] comp = { color.getRed(), color.getGreen(), color.getBlue() };
    for (int i = 0; i < comp.length; i++) {
      comp[i] = Math.min(255, comp[i] + 100);
    }
    return new Color(comp[0], comp[1], comp[2]);
  }

  /**
   * Returns a String for easily changing the fill colour of a cell (or multiple
   * cells) of a grid.
   * 
   * @param matrix
   *          the StringMatrix in which the colours are supposed to be changed
   * @param row
   *          the row
   * @param col
   *          the column
   * @param summed
   *          whether to fill only the given cell (row, col) or also every cell
   *          that lies in the range of (0, 0) - (row, col)
   * @param fillColor
   *          the colour that the specified cell(s) should filled with
   * @return a String with several setGridColor commands that do the job
   */
  public static String setSummedGridColor(MatrixPrimitive matrix, int row,
      int col, boolean summed, Color fillColor) {
    if (matrix != null && row < matrix.getNrRows() && col < matrix.getNrCols()) {
      StringBuilder builder = new StringBuilder();

      // fill the new cell(s)
      for (int iterRow = summed ? 0 : row; iterRow <= row; iterRow++) {
        for (int iterCol = summed ? 0 : col; iterCol <= col; iterCol++) {
          builder.append(setGridColor(matrix, iterRow, iterCol, null, null,
              colorToString(fillColor), null, null));
          builder.append("\n");
        }
      }

      return builder.toString();
    }
    return "";
  }
}
