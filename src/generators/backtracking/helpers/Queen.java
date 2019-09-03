package generators.backtracking.helpers;

/**
 * Class for queens. Queens store their position on the chess board as row and
 * column.
 */
public class Queen {

	private int row;
	private int column;

	/**
	 * Create a queen with a given position.
	 * 
	 * @param row
	 *            the row on the chess board
	 * @param column
	 *            the column on the chess board
	 */
	public Queen(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Queen at [");
		builder.append(row);
		builder.append(", ");
		builder.append(column);
		builder.append("]");
		return builder.toString();
	}
}
