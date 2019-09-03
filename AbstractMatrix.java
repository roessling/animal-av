/*
 * Created on 24.11.2004 
 */
package algoanim.primitives;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalAbstractMatrixGenerator;
import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.generators.DoubleMatrixGenerator;
import algoanim.properties.MatrixProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * <code>DoubleMatrix</code> manages an internal matrix. Operations on objects
 * of <code>DoubleMatrix</code> are almost performed like on a simple matrix.
 * 
 * @author jens
 */
public class AbstractMatrix extends MatrixPrimitive {
	/**
	 * The <code>DoubleMatrix</code> is internally represented by a simple
	 * matrix.
	 */
	private final double[][] data;

	/**
	 * The related <code>DoubleMatrixGenerator</code>, which is responsible for
	 * generating the appropriate scriptcode for operations performed on this
	 * object.
	 */
	protected AnimalAbstractMatrixGenerator generator;

	/**
	 * the properties for a matrix
	 */
	private MatrixProperties properties = null;

	/**
	 * the upper left corner of the matrix, necessary for placing it on the
	 * screen
	 */
	private Node upperLeft = null;
  
  /**
   * Denotes if cell width and heights are set manually via properties (<code>false
   * </code>) or should be computed using <code>refresh</code> (<code>true</code>).
   */
  public final boolean scale; 

	/**
	 * Instantiates the <code>DoubleMatrix</code> and calls the create() method
	 * of the associated <code>DoubleMatrixGenerator</code>.
	 * 
	 * @param iag
	 *            the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *            the upper left corner of this <code>DoubleMatrix</code>.
	 * @param matrixData
	 *            the data of this <code>DoubleMatrix</code>.
	 * @param name
	 *            the name of this <code>DoubleMatrix</code>.
	 * @param display
	 *            [optional] the <code>DisplayOptions</code> of this
	 *            <code>DoubleMatrix</code>.
	 * @param iap
	 *            [optional] the properties of this <code>DoubleMatrix</code>.
	 */
	public AbstractMatrix(DoubleMatrixGenerator iag, Node upperLeftCorner,
			double[][] matrixData, String name, DisplayOptions display,
			MatrixProperties iap) {
		super(iag, display);

		if (upperLeftCorner == null) {
			throw new IllegalArgumentException("The coordinate of the "
					+ "upper left Node shouldn't be null!");
		}

		if (matrixData == null) {
			throw new IllegalArgumentException(
					"Null matrix passed in to DoubleMatrix, named " + name);
		}

		upperLeft = upperLeftCorner;
		if (matrixData != null) {
			nrRows = matrixData.length;
			if (matrixData[0] != null)
				nrCols = matrixData[0].length;
		}
    data = new double[nrRows][nrCols];
    for (int i = 0; i < nrRows; i++)
      System.arraycopy(matrixData[i], 0, data[i], 0, nrCols);
    //GR Fix this modified the array passed in!
    //data = matrixData;

		properties = iap;
		setName(name);

		generator = iag;
    scale = generator.create(this);
	}

	private boolean checkForValidPosition(int row, int col, String methodName) {
		if (row < data.length && row >= 0 && col < getNrCols() && col >= 0)
			return true;

		StringBuilder sb = new StringBuilder("Invalid position [");
		sb.append(row).append("][").append(col).append("] in method ");
		sb.append(methodName).append(" of class DoubleMatrix");
		throw new IllegalArgumentException(sb.toString());
	}

	/**
	 * Returns the data at the given position of the internal matrix. If an
	 * <code>CounterController</code> observes this <code>DoubleMatrix</code> it
	 * is notified.
	 * 
	 * @param row
	 *            the row where to look for the data.
	 * @param col
	 *            the column where to look for the data.
	 * @return the data at position <code>row</code>, <code>col</code> in the
	 *         internal <code>double matrix</code>.
	 */

	public double getElement(int row, int col) {
		if (checkForValidPosition(row, col, "getElement[" + row + "][" + col
				+ "]")) {
			notifyObservers(PrimitiveEnum.getElement);
			return data[row][col];
		}

		return 0;
	}

	/**
	 * Puts the value <code>what</code> at position <code>[row][col]</code>.
	 * This is the delayed version as specified by <code>t</code>. The
	 * <code>duration</code> of this operation may also be specified. If an
	 * <code>CounterController</code> observes this <code>DoubleMatrix</code> it
	 * is notified.
	 * 
	 * @param row
	 *            the row position of the element to write.
	 * @param col
	 *            the column position of the element to write.
	 * @param what
	 *            the new value.
	 * @param t
	 *            [optional] the delay which shall be applied to the operation.
	 * @param d
	 *            [optional] the duration this action needs.
	 */
	public void put(int row, int col, double what, Timing t, Timing d)
			throws IndexOutOfBoundsException {
		if (checkForValidPosition(row, col, "put")) {
			notifyObservers(PrimitiveEnum.put);
			data[row][col] = what;
			generator.put(this, row, col, what, t, d);
		}
	}

	/**
	 * Swaps the elements at index <code>[sourceRow][sourceCol]</code> and
	 * <code>[targetRow][targetCol]</code>. This is the delayed version. The
	 * <code>duration</code> of this operation may also be specified. If an
	 * <code>CounterController</code> observes this <code>DoubleMatrix</code> it
	 * is notified.
	 * 
	 * @param sourceRow
	 *            the row position of the first element to swap.
	 * @param sourceCol
	 *            the column position of the first element to swap.
	 * @param targetRow
	 *            the row position of the second element to swap.
	 * @param targetCol
	 *            the column position of the second element to swap.
	 * @param t
	 *            [optional] the delay which shall be applied to the operation.
	 * @param d
	 *            [optional] the duration this action needs.
	 */
	public void swap(int sourceRow, int sourceCol, int targetRow,
			int targetCol, Timing t, Timing d) throws IndexOutOfBoundsException {
		if (checkForValidPosition(sourceRow, sourceCol, "swap param 1")
				&& checkForValidPosition(targetRow, targetCol, "swap param 2")) {
			notifyObservers(PrimitiveEnum.swap);
			double tmp = data[sourceRow][sourceCol];
			data[sourceRow][sourceCol] = data[targetRow][targetCol];
			data[targetRow][targetCol] = tmp;
			generator.swap(this, sourceRow, sourceCol, targetRow, targetCol, t,
					d);
		}
	}

//	/**
//	 * Returns the internal <code>double matrix</code>.
//	 * 
//	 * @return the internal <code>double matrix</code>.
//	 */
//	private double[][] getData() {
//		return data;
//	}
//
//	/**
//	 * Returns the data at the given position of the internal matrix.
//	 * 
//	 * @param row
//	 *            the position where to look for the data.
//	 * @return the data at position <code>i</code> in the internal
//	 *         <code>double matrix</code>.
//	 */
//	private double[] getRow(int row) throws IndexOutOfBoundsException {
//		if (row < 0 || row >= getNrRows()) {
//			throw new IndexOutOfBoundsException(
//					"Matrix has only row indices [0, " + (getNrRows() - 1)
//							+ ", but " + row + " was requested");
//		}
//		return data[row];
//	}

	/**
	 * Returns the upper left corner of this matrix.
	 * 
	 * @return the upper left corner of this matrix.
	 */
	public Node getUpperLeft() {
		return upperLeft;
	}

	/**
	 * Returns the properties of this matrix.
	 * 
	 * @return the properties of this matrix.
	 */
	public MatrixProperties getProperties() {
		return properties;
	}

	/**
	 * @see algoanim.primitives.Primitive#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		properties.setName(newName);
		super.setName(newName);
	}

	/**
	 * Highlights the matrix cell at a given position after a distinct offset.
	 * 
	 * @param row
	 *            the row of the cell to highlight.
	 * @param col
	 *            the column of the cell to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void highlightCell(int row, int col, Timing offset, Timing duration) {
		if (checkForValidPosition(row, col, "highlightCell")) {
			generator.highlightCell(this, row, col, offset, duration);
		}
	}

	/**
	 * Highlights a range of array cells of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the row of the interval to highlight.
	 * @param startCol
	 *            the start column of the interval to highlight.
	 * @param endCol
	 *            the end column of the interval to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void highlightCellColumnRange(int row, int startCol, int endCol,
			Timing offset, Timing duration) {
		if (checkForValidPosition(row, startCol,
				"highlightCellColumnRange param 1")
				&& checkForValidPosition(row, endCol,
						"highlightCellColumnRange param 2")) {
			generator.highlightCellColumnRange(this, row, startCol, endCol,
					offset, duration);
		}
	}

	/**
	 * Highlights a range of array cells of an <code>DoubleMatrix</code>.
	 * 
	 * @param startRow
	 *            the start row of the interval to highlight.
	 * @param endRow
	 *            the end row of the interval to highlight.
	 * @param col
	 *            the column of the interval to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void highlightCellRowRange(int startRow, int endRow, int col,
			Timing offset, Timing duration) {
		if (checkForValidPosition(startRow, col,
				"highlightCellRowRange param 1")
				&& checkForValidPosition(endRow, col,
						"highlightCellRowRange param 2")) {
			generator.highlightCellRowRange(this, startRow, endRow, col,
					offset, duration);
		}
	}

	/**
	 * Unhighlights the array cell of an <code>DoubleMatrix</code> at a given
	 * position after a distinct offset.
	 * 
	 * @param row
	 *            the row position of the cell to unhighlight.
	 * @param col
	 *            the column position of the cell to unhighlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void unhighlightCell(int row, int col, Timing offset, Timing duration) {
		if (checkForValidPosition(row, col, "unhighlightCell"))
			generator.unhighlightCell(this, row, col, offset, duration);
	}

	/**
	 * Unhighlights a range of array cells of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the row of the interval to highlight.
	 * @param startCol
	 *            the start column of the interval to highlight.
	 * @param endCol
	 *            the end column of the interval to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void unhighlightCellColumnRange(int row, int startCol, int endCol,
			Timing offset, Timing duration) {
		if (checkForValidPosition(row, startCol,
				"unhighlightCellColumnRange param 1")
				&& checkForValidPosition(row, endCol,
						"unhighlightCellColumnRange param 2")) {
			generator.unhighlightCellColumnRange(this, row, startCol, endCol,
					offset, duration);
		}
	}

	/**
	 * Unhighlights a range of array cells of an <code>DoubleMatrix</code>.
	 * 
	 * @param startRow
	 *            the start row of the interval to highlight.
	 * @param endRow
	 *            the end row of the interval to highlight.
	 * @param col
	 *            the column of the interval to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void unhighlightCellRowRange(int startRow, int endRow, int col,
			Timing offset, Timing duration) {
		if (checkForValidPosition(startRow, col,
				"unhighlightCellRowRange param 1")
				&& checkForValidPosition(endRow, col,
						"unhighlightCellRowRange param 2")) {
			generator.unhighlightCellRowRange(this, startRow, endRow, col,
					offset, duration);
		}
	}

	/**
	 * Highlights the matrix element at a given position after a distinct
	 * offset.
	 * 
	 * @param row
	 *            the row of the element to highlight.
	 * @param col
	 *            the column of the element to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void highlightElem(int row, int col, Timing offset, Timing duration) {
		if (checkForValidPosition(row, col, "highlightElem")) {
			generator.highlightElem(this, row, col, offset, duration);
		}
	}

	/**
	 * Highlights a range of matrix elements.
	 * 
	 * @param row
	 *            the row of the interval to highlight.
	 * @param startCol
	 *            the start of the column interval to highlight.
	 * @param endCol
	 *            the end of the column interval to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void highlightElemColumnRange(int row, int startCol, int endCol,
			Timing offset, Timing duration) {
		if (checkForValidPosition(row, startCol,
				"highlightElemColumnRange param 1")
				&& checkForValidPosition(row, endCol,
						"highlightElemColumnRange param 2")) {
			generator.highlightElemColumnRange(this, row, startCol, endCol,
					offset, duration);
		}
	}

	/**
	 * Highlights a range of array elements of an <code>DoubleMatrix</code>.
	 * 
	 * @param startRow
	 *            the start of the row interval to highlight.
	 * @param endRow
	 *            the end of the row interval to highlight.
	 * @param col
	 *            the column interval to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void highlightElemRowRange(int startRow, int endRow, int col,
			Timing offset, Timing duration) {
		if (checkForValidPosition(startRow, col,
				"highlightElemRowRange param 1")
				&& checkForValidPosition(endRow, col,
						"highlightElemRowRange param 2")) {
			generator.highlightElemRowRange(this, startRow, endRow, col,
					offset, duration);
		}
	}

	/**
	 * Unhighlights the matrix element at a given position after a distinct
	 * offset.
	 * 
	 * @param row
	 *            the row of the element to unhighlight.
	 * @param col
	 *            the column of the element to unhighlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void unhighlightElem(int row, int col, Timing offset, Timing duration) {
		if (checkForValidPosition(row, col, "unhighlightElement")) {
			generator.unhighlightElem(this, row, col, offset, duration);
		}
	}

	/**
	 * Unhighlights a range of array elements of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the row of the interval to unhighlight.
	 * @param startCol
	 *            the start of the column interval to unhighlight.
	 * @param endCol
	 *            the end of the column interval to unhighlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void unhighlightElemColumnRange(int row, int startCol, int endCol,
			Timing offset, Timing duration) {
		if (checkForValidPosition(row, startCol,
				"unhighlightElemColumnRange param 1")
				&& checkForValidPosition(row, endCol,
						"unhighlightElemColumnRange param 2")) {
			generator.unhighlightElemColumnRange(this, row, startCol, endCol,
					offset, duration);
		}
	}

	/**
	 * Unhighlights a range of array elements of an <code>DoubleMatrix</code>.
	 * 
	 * @param startRow
	 *            the start row of the interval to unhighlight.
	 * @param endRow
	 *            the end row of the interval to unhighlight.
	 * @param col
	 *            the column interval to unhighlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void unhighlightElemRowRange(int startRow, int endRow, int col,
			Timing offset, Timing duration) {
		if (checkForValidPosition(startRow, col,
				"unhighlightElemRowRange param 1")
				&& checkForValidPosition(endRow, col,
						"unhighlightElemRowRange param 2")) {
			generator.unhighlightElemRowRange(this, startRow, endRow, col,
					offset, duration);
		}
	}


	/**
	 * Set the Color for a cell of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the start row of the interval to unhighlight.
	 * @param col
	 *            the column interval to unhighlight.
	 * @param color
	 *            the color to set.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void setGridColor(int row, int col, Color color,
			Timing offset, Timing duration){
		setGridColorKind(row, col, color, "color",
				offset, duration);
	}


	/**
	 * Set the TextColor for a cell of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the start row of the interval to unhighlight.
	 * @param col
	 *            the column interval to unhighlight.
	 * @param color
	 *            the color to set.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void setGridTextColor(int row, int col, Color color,
			Timing offset, Timing duration){
		setGridColorKind(row, col, color, "textcolor",
				offset, duration);
	}


	/**
	 * Set the FillColor for a cell of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the start row of the interval to unhighlight.
	 * @param col
	 *            the column interval to unhighlight.
	 * @param color
	 *            the color to set.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void setGridFillColor(int row, int col, Color color,
			Timing offset, Timing duration){
		setGridColorKind(row, col, color, "fillcolor",
				offset, duration);
	}


	/**
	 * Set the BorderColor for a cell of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the start row of the interval to unhighlight.
	 * @param col
	 *            the column interval to unhighlight.
	 * @param color
	 *            the color to set.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void setGridBorderColor(int row, int col, Color color,
			Timing offset, Timing duration){
		setGridColorKind(row, col, color, "bordercolor",
				offset, duration);
	}


	/**
	 * Set the HighlightTextColor for a cell of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the start row of the interval to unhighlight.
	 * @param col
	 *            the column interval to unhighlight.
	 * @param color
	 *            the color to set.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void setGridHighlightTextColor(int row, int col, Color color,
			Timing offset, Timing duration){
		setGridColorKind(row, col, color, "highlightTextColor",
				offset, duration);
	}


	/**
	 * Set the HighlightFillColor for a cell of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the start row of the interval to unhighlight.
	 * @param col
	 *            the column interval to unhighlight.
	 * @param color
	 *            the color to set.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void setGridHighlightFillColor(int row, int col, Color color,
			Timing offset, Timing duration){
		setGridColorKind(row, col, color, "highlightFillColor",
				offset, duration);
	}


	/**
	 * Set the HighlightBorderColor for a cell of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the start row of the interval to unhighlight.
	 * @param col
	 *            the column interval to unhighlight.
	 * @param color
	 *            the color to set.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void setGridHighlightBorderColor(int row, int col, Color color,
			Timing offset, Timing duration){
		setGridColorKind(row, col, color, "highlightBorderColor",
				offset, duration);
	}
	
	/**
	 * Set the Color for a cell of an <code>DoubleMatrix</code>.
	 * 
	 * @param row
	 *            the start row of the interval to unhighlight.
	 * @param col
	 *            the column interval to unhighlight.
	 * @param color
	 *            the color to set.
	 * @param kind
	 *            the kind of color to set.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	private void setGridColorKind(int row, int col, Color color, String kind,
			Timing offset, Timing duration){
		if (checkForValidPosition(row, col, "setGridColor param 1")) {
			generator.setGridColor(this, row, col, color, kind, offset, duration);
		}
	}
	  
	  public void setGridFont(int row, int col, Font font,
	      Timing offset, Timing duration){
		    if (checkForValidPosition(row, col, "setGridFont param 1")) {
		        generator.setGridFont(this, row, col, font, offset, duration);
		    }

	  }
}
