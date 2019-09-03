package generators.maths.newtonpolynomial;

import algoanim.animalscript.AnimalGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringMatrixGenerator;
import algoanim.primitives.StringMatrix;
import algoanim.properties.MatrixProperties;
import algoanim.util.Timing;

public class NoRefreshStringMatrixGenerator extends AnimalStringMatrixGenerator {

	private int cellWidth = 0;

	public NoRefreshStringMatrixGenerator(AnimalScript as) {
		super(as);
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public void setCellWidth(int cellWidth) {
		this.cellWidth = cellWidth;
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

		builder.append(" lines ");
		builder.append(rows);
		builder.append(" columns ");
		builder.append(columns);

		builder.append(" style plain");
		if (cellWidth != 0) {
			builder.append(" cellWidth ");
			builder.append(cellWidth);
		}

		MatrixProperties matrixProps = aMatrix.getProperties();
		addColorOption(matrixProps, builder);
		addColorOption(matrixProps, "elementColor", " elementColor ", builder);
		addColorOption(matrixProps, "fillColor", " fillColor ", builder);
		addColorOption(matrixProps, "elemHighlight", " highlightTextColor ", builder);
		addColorOption(matrixProps, "cellHighlight", " highlightBackColor ", builder);
		addIntOption(matrixProps, "depth", " depth ", builder);
		addColorOption(aMatrix.getProperties(), builder);

		lang.addLine(builder.toString());

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

	@Override
	public void put(StringMatrix intMatrix, int row, int col, String what, Timing delay, Timing duration) {
		StringBuilder builder = new StringBuilder();

		builder.append("setGridValue \"");
		builder.append(intMatrix.getName());
		builder.append("[");
		if (row >= 0) {
			builder.append(row);
		}
		builder.append("][");
		if (col >= 0) {
			builder.append(col);
		}
		builder.append("]\" \"");
		builder.append(what);
		builder.append("\" ");

		addWithTiming(builder, delay, duration);
	}

}
