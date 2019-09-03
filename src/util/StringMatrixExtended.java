package util;

import java.awt.Color;

import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * This extends a StringMatrix with features to set the color of each cell, its text or border.
 * 
 * @author Timo Baehr
 */
public class StringMatrixExtended {
	
	/* fields */

	private Language language;
	
	private StringMatrix matrix;
	
	private String identifier;
	
	private Color BACKGROUND_COLOR_DEFAULT = Color.YELLOW;
	private static final Color BORDER_COLOR_DEFAULT = Color.BLACK;
	
	public StringMatrixExtended(Language language, Node upperLeft, String[][] matrix, String identifier, DisplayOptions display, MatrixProperties props) {
		this.language = language;
		this.identifier = identifier;
		
		this.matrix = this.language.newStringMatrix(upperLeft, matrix, identifier, display, props);//matrixProperties);
		
		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix[0].length; column++) {
				setBackgroundColor(row, column, (Color) props.get(AnimationPropertiesKeys.FILL_PROPERTY));
				BACKGROUND_COLOR_DEFAULT = (Color) props.get(AnimationPropertiesKeys.FILL_PROPERTY);
				setTextColor(row, column, (Color) props.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
				setBorderColor(row, column, BORDER_COLOR_DEFAULT);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		for (int row = 0; row < matrix.getNrRows(); row++) {
			sb.append("{");
			for (int column = 0; column < matrix.getNrCols(); column++) {
				sb.append(matrix.getElement(row, column)).append(",");
			}
			sb = sb.deleteCharAt(sb.length()-1);
			sb.append("},\n");
		}
		sb = sb.deleteCharAt(sb.length()-1);
		sb = sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		
		return sb.toString();
	}
	
	
	/* getter and setter */
	
	public void unhighlightElem(int row, int column, Timing offset, Timing duration) {
		matrix.unhighlightElem(row, column, offset, duration);
	}
	
	public void highlightElem(int row, int column, Timing offset, Timing duration) {
		matrix.highlightElem(row, column, offset, duration);
	}
	
	public void unhighlightCell(int row, int column, Timing offset, Timing duration) {
		matrix.unhighlightCell(row, column, offset, duration);
		setBackgroundColor(row, column, BACKGROUND_COLOR_DEFAULT);
	}
	
	public void highlightCell(int row, int column, Timing offset, Timing duration) {
		matrix.highlightCell(row, column, offset, duration);
	}
	
	public void unhighlightColumn(int column, Timing offset, Timing duration) {
		for (int row = 0; row < matrix.getNrRows(); row++) {
			matrix.unhighlightCell(row, column, offset, duration);
		}
	}
	
	public void highlightColumn(int column, Timing offset, Timing duration) {
		for (int row = 0; row < matrix.getNrRows(); row++) {
			matrix.highlightCell(row, column, offset, duration);
		}
	}
	
	public int getNrRows() {
		return matrix.getNrRows();
	}
	
	public int getNrCols() {
		return matrix.getNrCols();
	}
	
	/**
	 * retrieves the element at position (row, col) if this is legal, else "".
	 * If an <code>CounterController</code> observes this
	 * <code>StringMatrix</code> it is notified.
	 * 
	 * @param row
	 *            the row of the element to be retrieved
	 * @param column
	 *            the column of the element to be retrieved
	 * @return "" if the position is invalid, else the element at that position
	 */
	public String getElement(int row, int column) {
		return matrix.getElement(row, column);
	}
	
	public void put(int row, int column, String data, Timing delay, Timing duration) {
		matrix.put(row, column, data, delay, duration);
	}
	
	public void hide() {
		matrix.hide();
	}
	
	public void show() {
		matrix.show();
	}
	
	public void moveTo(String direction, String moveType, Coordinates target, Timing delay, Timing duration) {
		matrix.moveTo(direction, moveType, target, delay, duration);
	}
	
	private int numberOfSubMatrixes = 0;
	
	/**
	 * This returns a submatrix from a beginning row to the last row.
	 * 
	 * @param rowBegin
	 * @param rowEnd
	 * @return
	 */
	public StringMatrixExtended getSubMatrix(int rowBegin, int rowEnd , Coordinates coords) {
		if (rowEnd < rowBegin)
			throw new IllegalArgumentException("The last row must be a row after the first row.");
		
		if (rowBegin < 0 || rowEnd < 0)
			throw new IllegalArgumentException("The argument must be a positive number.");
		
		String[][] subMatrix = new String[(rowEnd - rowBegin)+1][matrix.getNrCols()];
		for (int row = 0; row < subMatrix.length; row++) {
			for (int column = 0; column < subMatrix[0].length; column++) {
				subMatrix[row][column] = matrix.getElement(row+rowBegin, column);
			}			
		}
		
		StringMatrixExtended resultMatrix = new StringMatrixExtended(language, coords, subMatrix, identifier+numberOfSubMatrixes, null, matrix.getProperties());
		numberOfSubMatrixes++;
		
		return resultMatrix;
	}
	
	/* Background Color */
	
//	public void setBackgroundColor(Color backgroundColorDefault) {
//		BACKGROUND_COLOR_DEFAULT = backgroundColorDefault;
//	}
	
	public Color getBackgroundColor() {
		return BACKGROUND_COLOR_DEFAULT;
	}
	
//	public Color getBackgroundColor(int row, int column) {
//		return attributes[row][column].getBackgroundColor();
//	}
	
	public void setBackgroundColor(int row, int column, Color backgroundColor) {
		//attributes[row][column].setBackgroundColor(backgroundColor);
		String color = "("+backgroundColor.getRed()+", "+backgroundColor.getGreen()+", "+backgroundColor.getBlue()+")";
		language.addLine("setGridColor \""+identifier+"["+row+"]["+column+"]\" fillColor "+color);
	}
	
	/* Text Color */
	
//	public void setTextColor(Color textColorDefault) {
//		TEXT_COLOR_DEFAULT = textColorDefault;
//	}
	
//	public Color getTextColor() {
//		return TEXT_COLOR_DEFAULT;
//	}
	
//	public Color getTextColor(int row, int column) {
//		return attributes[row][column].getTextColor();
//	}
	
	public void setTextColor(int row, int column, Color textColor) {
		//attributes[row][column].setTextColor(textColor);
		String color = "("+textColor.getRed()+", "+textColor.getGreen()+", "+textColor.getBlue()+")";
		language.addLine("setGridColor \""+identifier+"["+row+"]["+column+"]\" textColor "+color);
	}
	
	/* Border Color */
	
//	public void setBorderColor(Color borderColorDefault) {
//		BORDER_COLOR_DEFAULT = borderColorDefault;
//	}
	
//	public Color getBorderColor() {
//		return BORDER_COLOR_DEFAULT;
//	}
	
//	public Color getBorderColor(int row, int column) {
//		return attributes[row][column].getBorderColor();
//	}
	
	public void setBorderColor(int row, int column, Color borderColor) {
		//attributes[row][column].setBorderColor(borderColor);
		String color = "("+borderColor.getRed()+", "+borderColor.getGreen()+", "+borderColor.getBlue()+")";
		language.addLine("setGridColor \""+identifier+"["+row+"]["+column+"]\" boderColor "+color);
	}
}
