package util;

import generators.framework.components.ColorChooserComboBox;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * Work around the "refresh-bug" which draws a "funny" grid and API
 * inconsistencies by just ignoring the Java Matrix Implementations and writing
 * my grid as needed with adding animal script lines directly. That isn't nice,
 * but I need a colorful grid and its an official recommendation for
 * setGridColor …
 */
public class KalnischkiesGrid<T> {
	private Language lang;
	private T matrix[][];
	private String name;
	private int offset_row;
	private int offset_col;
	private GridStyle style;
	private Color fillColor;

	public enum GridStyle {
		PLAIN, TABLE, CHESSBLACK, CHESSWHITE, MATRIX
	}

	public static final int BASE_GRIDSIZE = 16;

	public KalnischkiesGrid(Language lang, String name, Node position,
			String[] header_row, String[] header_col, int cellSize,
			GridStyle style, MatrixProperties prop) {
		initGrid(lang, name, position, header_row.length + 1,
				header_col.length + 1, cellSize, style, prop);
		setGridHeaders(header_row, header_col);
		this.matrix = createMatrix(header_row.length, header_col.length);
		initGridStyling();
	}

	public KalnischkiesGrid(Language lang, String name, Node position, int row,
			String[] header_col, int cellSize, GridStyle style,
			MatrixProperties prop) {
		initGrid(lang, name, position, row + 1, header_col.length, cellSize,
				style, prop);
		setGridHeaders(new String[0], header_col);
		this.matrix = createMatrix(row, header_col.length);
		initGridStyling();
	}

	public KalnischkiesGrid(Language lang, String name, Node position,
			String[] header_row, int col, int cellSize, GridStyle style,
			MatrixProperties prop) {
		initGrid(lang, name, position, header_row.length, col + 1, cellSize,
				style, prop);
		setGridHeaders(header_row, new String[0]);
		this.matrix = createMatrix(header_row.length, col);
		initGridStyling();
	}

	public KalnischkiesGrid(Language lang, String name, Node position, int row,
			int col, int cellSize, GridStyle style, MatrixProperties prop) {
		initGrid(lang, name, position, row, col, cellSize, style, prop);
		setGridHeaders(new String[0], new String[0]);
		this.matrix = createMatrix(row, col);
		initGridStyling();
	}

	private void initGrid(Language lang, String name, Node position, int row,
			int col, int cellSize, GridStyle style, MatrixProperties prop) {
		this.lang = lang;
		this.name = name;
		this.style = style;
		String grid = "grid \"" + this.name + "\" ";
		if (position instanceof Coordinates) {
			Coordinates p = (Coordinates) position;
			grid += "(" + p.getX() + ", " + p.getY() + ")";
		} else if (position instanceof Offset) {
			Offset o = (Offset) position;
			grid += "offset (" + o.getX() + ", " + o.getY() + ") from \""
					+ o.getBaseID() + "\" " + o.getDirection();
		} else
			throw new IllegalArgumentException(
					"Unsupported Node-subclass. Only Coordinates and Offset are implemented!");

		grid += " lines " + row + " columns " + col + " style ";
		switch (style) {
		case CHESSBLACK:
		case CHESSWHITE:
		case PLAIN:
			grid += "plain";
			break;
		case TABLE:
			grid += "table";
			break;
		case MATRIX:
			grid += "matrix";
			break;
		}

		Font font = (Font) prop.get(AnimationPropertiesKeys.FONT_PROPERTY);
		FontMetrics metrics = new FontMetrics(font) {
			private static final long serialVersionUID = 1L;
		};
		Rectangle2D bounds = metrics.getStringBounds("X", null);
		if (style == GridStyle.CHESSBLACK || style == GridStyle.CHESSWHITE) {
			int cell = (int) (bounds.getWidth() * cellSize);
			grid += " cellWidth " + cell + " cellHeight " + cell
					+ " fixedCellSize";
		} else
			grid += " cellWidth " + (int) (bounds.getWidth() * cellSize)
					+ " cellHeight " + (int) (bounds.getHeight() + 4)
					+ " fixedCellSize";
		Color color = (Color) prop.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		grid += " color " + getStringFromColor(color);
		Color textColor = (Color) prop
				.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
		grid += " textColor " + getStringFromColor(textColor);
		if ((Boolean) prop.get(AnimationPropertiesKeys.FILLED_PROPERTY)) {
			this.fillColor = (Color) prop
					.get(AnimationPropertiesKeys.FILL_PROPERTY);
			grid += " fillColor " + getStringFromColor(this.fillColor);
		} else {
			switch(this.style) {
			case CHESSBLACK:
			case CHESSWHITE:
			case TABLE:
				this.fillColor = Color.LIGHT_GRAY;
				break;
			case MATRIX:
			case PLAIN:
				this.fillColor = Color.WHITE;
				break;
			}
		}
		Color highlightTextColor = (Color) prop
				.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY);
		grid += " highlightTextColor " + getStringFromColor(highlightTextColor);
		if ((Boolean) prop.get(AnimationPropertiesKeys.FILLED_PROPERTY)) {
			Color highlightFillColor = (Color) prop
					.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
			grid += " highlightFillColor "
					+ getStringFromColor(highlightFillColor);
		}
		// FIXME: This would work for a chessboard, but it is not supported by
		// grid :(
		/*
		 * if ((boolean)
		 * prop.get(AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY)) { Color
		 * fillColor = (Color) prop
		 * .get(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY); grid +=
		 * " alternateFillColor " + getStringFromColor(fillColor); }
		 */
		// FIXME: Animal-Bug: unable to define border color
		/*
		 * Color borderColor = (Color)
		 * prop.get(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY); grid +=
		 * " borderColor " + getStringFromColor(borderColor); Color
		 * highlightBorderColor = (Color)
		 * prop.get(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY
		 * ); grid += " highlightBorderColor " +
		 * getStringFromColor(highlightBorderColor);
		 */
		// FIXME: Animal-Bug: setting alignment changes fontsize – and doesn't
		// align

		grid += " font " + font.getName();
		// FIXME: Animal-Bug: do size setting last as it resets color settings
		// This way, the size setting doesn't work, but yeah…
		grid += " size " + font.getSize();
		if (font.isBold() == true)
			grid += " bold";
		if (font.isItalic())
			grid += " italic";
		this.lang.addLine(grid);
	}

	private void initGridStyling() {
		if (this.style == GridStyle.CHESSBLACK
				|| this.style == GridStyle.CHESSWHITE)
			for (int r = 0; r < this.matrix.length; ++r)
				for (int c = 0; c < this.matrix[0].length; ++c)
					setGridColor(r, c, getDefaultColorObject(r, c));
	}

	private void setGridHeaders(String[] header_row, String[] header_col) {
		this.offset_col = header_row.length == 0 ? 0 : 1;
		this.offset_row = header_col.length == 0 ? 0 : 1;
		for (int row = 0; row < header_row.length; ++row)
			this.lang.addLine("setGridValue \"" + this.name + "["
					+ (row + this.offset_row) + "][0]\" \"" + header_row[row]
					+ "\"");
		for (int col = 0; col < header_col.length; ++col)
			this.lang.addLine("setGridValue \"" + this.name + "[0]["
					+ (col + this.offset_col) + "]\" \"" + header_col[col]
					+ "\"");
	}

	public void setMatrix(T[][] matrix2) {
		for (int row = 0; row < matrix2.length; ++row)
			for (int col = 0; col < matrix2[row].length; ++col)
				if (this.matrix[row][col] != matrix2[row][col])
					setGridValue(row, col, matrix2[row][col]);
	}

	public T getElement(int row, int col) {
		return this.matrix[row][col];
	}

	public int findColumn(int row, T value) {
		for (int i = 0; i < this.matrix[row].length; ++i) {
			T e = getElement(row, i);
			if (e == null) {
				if (value == null)
					return i;
			} else if (e.equals(value))
				return i;
		}
		return -1;
	}

	public int getNumberOfColumns() {
		return this.matrix[0].length;
	}

	public int getNumberOfRows() {
		return this.matrix.length;
	}

	public String getRow(int row, T defvalue, String merge) {
		String str = "";
		for (int col = 0; col < this.matrix[row].length; ++col) {
			T value = this.matrix[row][col];
			if (value == null)
				value = defvalue;
			if (value.toString().isEmpty() == true)
				continue;
			if (str.isEmpty() == false)
				str += merge;
			str += value;
		}
		return str;
	}

	public void setGridValue(int row, int col, T value) {
		if (value == null)
			this.lang.addLine("setGridValue \"" + this.name + "["
					+ (row + this.offset_row) + "][" + (col + this.offset_col)
					+ "]\" \"\"");
		else
			this.lang.addLine("setGridValue \"" + this.name + "["
					+ (row + this.offset_row) + "][" + (col + this.offset_col)
					+ "]\" \"" + value + "\"");
		this.matrix[row][col] = value;
	}

	public void unhighlightCell(int row, int col) {
		this.lang.addLine("unhighlightGridCell \"" + this.name + "["
				+ (row + this.offset_row) + "][" + (col + this.offset_col)
				+ "]\"");
	}

	public void highlightCell(int row, int col) {
		this.lang.addLine("highlightGridCell \"" + this.name + "["
				+ (row + this.offset_row) + "][" + (col + this.offset_col)
				+ "]\"");
	}

	public void unhighlightRow(int row) {
		this.lang.addLine("unhighlightGridCell \"" + this.name + "["
				+ (row + this.offset_row) + "][]\"");
	}

	public void highlightRow(int row) {
		this.lang.addLine("highlightGridCell \"" + this.name + "["
				+ (row + this.offset_row) + "][]\"");
	}

	public void setGridColor(int row, int col, Color color) {
		String id = this.name + "[" + (row + this.offset_row) + "]["
				+ (col + this.offset_col) + "]";
		this.lang.addLine("setGridColor \"" + id + "\" fillColor "
				+ ColorChooserComboBox.getStringForColor(color));
	}

	public void resetGridColor(int row, int col) {
		setGridColor(row, col, getDefaultColorObject(row, col));
	}

	public void clear() {
		this.lang.addLine("setGridColor \"" + this.name + "[][]\" fillColor "
				+ getDefaultColor(0, 0));
		for (int row = 0; row < this.matrix.length; ++row)
			for (int col = 0; col < this.matrix[row].length; ++col)
				setGridValue(row, col, null);
	}

	public void hide() {
		this.lang.addLine("hide \"" + this.name + "\"");
	}

	public int[] getPositionFromTile(int tile) {
		int[] pos = new int[2];
		pos[1] = tile % this.matrix[0].length;
		pos[0] = (tile - pos[1]) / this.matrix[0].length;
		return pos;
	}

	private String getDefaultColor(int row, int col) {
		return getStringFromColor(getDefaultColorObject(row, col));
	}

	private static String getStringFromColor(Color color) {
		return ColorChooserComboBox.getStringForColor(color);
	}

	private Color getDefaultColorObject(int row, int col) {
		switch (this.style) {
		case CHESSBLACK:
			return ((this.matrix[0].length * row + col) % 2) == 0 ? this.fillColor
					: Color.WHITE;
		case CHESSWHITE:
			return ((this.matrix[0].length * row + col) % 2) == 0 ? Color.WHITE
					: this.fillColor;
		case PLAIN:
		case MATRIX:
		case TABLE:
			return this.fillColor;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private T[][] createMatrix(int row, int col) {
		/*
		 * simply creating a generic T array is not possible, so we do this
		 * casting-trick, which seems to work just as well
		 */
		return (T[][]) new Object[row][col];
	}
}