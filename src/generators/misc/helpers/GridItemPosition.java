package generators.misc.helpers;

/**
 * Encapsulates the position of a grid item.
 * @author chollubetz
 *
 */
public class GridItemPosition {
	int row, column;
	
	/**
	 * Creates a new grid item position.
	 * @param row the row
	 * @param column the column
	 */
	public GridItemPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Returns the row.
	 * @return the row
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Return the column.
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
}
