package generators.backtracking.helpers;

import generators.graphics.helpers.AnimalUtilities;

import java.awt.Color;

import algoanim.animalscript.AnimalGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringMatrixGenerator;
import algoanim.primitives.StringMatrix;

/**
 * Custom implementation of the AnimalStringMatrixGenerator that enables us to
 * use the "table" style which is not working in the default implementation as
 * of 13-05-06.
 */
public class CustomStringMatrixGenerator extends AnimalStringMatrixGenerator {

	// the maximum possible grid cell size (which is assigned to a single cell
	// on n = 1)
	public static final int MAX_CELL_SIZE = 350;

	// the corresponding maximum grid cell font size
	public static final int MAX_FONT_SIZE = 325;

	// custom colors
	private Color queenColor;
	private Color threatenedQueenColor;
	private Color gridColor1;
	private Color gridColor2;
	private Color lineColor = Color.GRAY;

	/**
	 * Create a CustomStringMatrixGenerator with an AnimalScript reference.
	 * 
	 * @param as
	 *            the AnimalScript reference
	 */
	public CustomStringMatrixGenerator(AnimalScript as, Color queenColor,
			Color threatenedQueenColor, Color gridColor1, Color gridColor2) {
		super(as);
		this.queenColor = queenColor;
		this.threatenedQueenColor = threatenedQueenColor;
		this.gridColor1 = gridColor1;
		this.gridColor2 = gridColor2;
	}

	@Override
	public boolean create(StringMatrix aMatrix) {
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
		builder.append(MAX_CELL_SIZE / rows);
		builder.append(" cellHeight ");
		builder.append(MAX_CELL_SIZE / rows);
		builder.append(" fixedCellSize ");

		// set the colors
		builder.append("textColor ");
		builder.append(AnimalUtilities.colorToString(queenColor));
		builder.append(" fillColor ");
		builder.append(AnimalUtilities.colorToString(gridColor1));
		builder.append(" highLightTextColor ");
		builder.append(AnimalUtilities.colorToString(threatenedQueenColor));
		builder.append(" highLightFillColor ");
		builder.append(AnimalUtilities.colorToString(gridColor2));
		builder.append(" highLightBorderColor ");
		builder.append(AnimalUtilities.colorToString(lineColor));

		// set the font size
		builder.append(" font SansSerif size ");
		builder.append(MAX_FONT_SIZE / rows);
		builder.append(" bold align left");

		// execute the grid creation
		lang.addLine(builder.toString());

		// initialize the matrix with empty entries
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				String value = aMatrix.getElement(r, c);
				if (value != null && !value.isEmpty()) {
					put(aMatrix, r, c, value, null, null);
				}
			}
		}
		return true;
	}
}
