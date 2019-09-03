package generators.graphics.helpers;

import java.awt.Color;

import algoanim.animalscript.AnimalGenerator;
import algoanim.animalscript.AnimalIntMatrixGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;

/**
 * Custom implementation of the AnimalStringMatrixGenerator that enables us to
 * use the "table" style which is not working in the default implementation as
 * of 13-05-06.
 */
public class CustomIntMatrixGenerator extends AnimalIntMatrixGenerator {

  // the maximum possible grid cell size (which is assigned to a single cell on
  // n = 1)
  public static final int CELL_SIZE = 55;

  // the corresponding maximum grid cell font size
  public static final int FONT_SIZE = 45;

  // custom colors
  private Color           lineColor;
  private Color           cellColor;
  private Color           textColor;

  /**
   * Create a CustomStringMatrixGenerator with an AnimalScript reference.
   * 
   * @param as
   *          the AnimalScript reference
   * @param lineColor
   *          the default line color for grids
   * @param cellColor
   *          the default fill color for grid cells
   * @param textColor
   *          the default text color for grid cells
   */
  public CustomIntMatrixGenerator(AnimalScript as, Color lineColor,
      Color cellColor, Color textColor) {
    super(as);
    this.cellColor = cellColor;
    this.lineColor = lineColor;
    this.textColor = textColor;
  }

  @Override
  public boolean create(IntMatrix aMatrix) {
    lang.addItem(aMatrix);

    StringBuilder builder = new StringBuilder();
    builder.append("grid \"");
    builder.append(aMatrix.getName());
    builder.append("\" ");

    builder.append(AnimalGenerator.makeNodeDef(aMatrix.getUpperLeft()));

    int rows = aMatrix.getNrRows();
    int columns = aMatrix.getNrCols();

    // set the rows, columns and their widths
    builder.append(" lines ");
    builder.append(rows);
    builder.append(" columns ");
    builder.append(columns);
    builder.append(" style table");
    builder.append(" cellWidth ");
    builder.append(CELL_SIZE * 1.5f);
    builder.append(" cellHeight ");
    builder.append(CELL_SIZE);
    builder.append(" fixedCellSize ");

    // set the colors
    builder.append("textColor ");
    builder.append(AnimalUtilities.colorToString(textColor));
    builder.append(" fillColor ");
    builder.append(AnimalUtilities.colorToString(cellColor));
    builder.append(" highLightFillColor ");
    builder.append(AnimalUtilities.colorToString(cellColor));
    builder.append(" highLightBorderColor ");
    builder.append(AnimalUtilities.colorToString(lineColor));

    // set the font size
    builder.append(" font SansSerif size ");
    builder.append(FONT_SIZE);
    builder.append(" bold align left");

    // execute the grid creation
    lang.addLine(builder.toString());

    // initialize the matrix with empty entries
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {
        int value = aMatrix.getElement(r, c);
        put(aMatrix, r, c, value, null, null);
      }
    }
    return true;
  }
}
