package algoanim.primitives.generators;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.IntMatrix;
import algoanim.util.Timing;

/**
 * <code>IntMatrixGenerator</code> offers methods to request the included 
 * Language object to
 * append integer array related script code lines to the output.
 * It is designed to be included by an <code>IntMatrix</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering an <code>IntMatrix</code> primitive has to 
 * implement its own
 * <code>IntMatrixGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface IntMatrixGenerator extends GeneratorInterface {  
	/**
	 * Creates the originating script code for a given <code>IntMatrix</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param ia the <code>IntMatrix</code> for which the initiate script 
	 * code shall be created. 
   * @return <code>false</code> if cell width and heights are set manually via properties
   *         or <code>true</code> if they should be computed using <code>refresh</code>.
	 */
	public boolean create(IntMatrix ia);
	
	/**
	 * Inserts an <code>int</code> at certain position in the given 
	 * <code>IntMatrix</code>.
	 * 
	 * @param iap the <code>IntMatrix</code> in which to insert the value.
	 * @param row the row where the value shall be inserted.
	 * @param col the column where the value shall be inserted.
	 * @param what the <code>int</code> value to insert.
	 * @param delay the time to wait until the operation shall be performed.
	 * @param duration the duration of the operation.
	 */
	public void put(IntMatrix iap, int row, int col, int what, Timing delay, 
			Timing duration);
	
	/**
	 * Swaps to values in a given <code>IntMatrix</code>.
	 * 
	 * @param iap the <code>IntMatrix</code> in which to swap the two 
	 * indizes.	
	 * @param sourceRow the row of the first value to be swapped.
	 * @param sourceCol the column of the first value to be swapped.
	 * @param targetRow the row of the second value to be swapped.
	 * @param targetCol the column of the second value to be swapped.
	 * @param delay the time to wait until the operation shall be performed.
	 * @param duration the duration of the operation.
	 */
	public void swap(IntMatrix iap, int sourceRow, int sourceCol, 
			int targetRow, int targetCol, Timing delay, Timing duration);
	
	/**
	 * Highlights the array cell at a given position after a distinct offset of an
	 * <code>IntMatrix</code>.
	 * 
	 * @param row the row of the cell to highlight.
	 * @param col the column of the cell to highlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void highlightCell(IntMatrix ia, int row, int col, Timing offset, 
			Timing duration);
	
	/**
	 * Highlights a range of array cells of an <code>IntMatrix</code>.
	 * 
	 * @param row the row of the interval to highlight.
	 * @param startCol the start column of the interval to highlight.
	 * @param endCol the end column of the interval to highlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started. 
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void highlightCellColumnRange(IntMatrix ia, int row, int startCol,
			int endCol, Timing offset, Timing duration);
	
	/**
	 * Highlights a range of array cells of an <code>IntMatrix</code>.
	 * 
	 * @param startRow the start row of the interval to highlight.
	 * @param endRow the end row of the interval to highlight.
	 * @param column the column of the interval to highlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started. 
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void highlightCellRowRange(IntMatrix ia, int startRow, int endRow,
			int column, Timing offset, Timing duration);
	
	
	/**
	 * Unhighlights the array cell of an <code>IntMatrix</code> at a given position 
	 * after a distinct offset.
	 * 
	 * @param ia the <code>IntMatrix</code> to work on.
	 * @param row the row of the cell to unhighlight.
	 * @param col the column of the cell to unhighlight
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void unhighlightCell(IntMatrix ia, int row, int col, Timing offset, 
			Timing duration);
	
	/**
	 * Unhighlights a range of array cells of an <code>IntMatrix</code>.
	 * 
	 * @param row the row of the interval to highlight.
	 * @param startCol the start column of the interval to highlight.
	 * @param endCol the end column of the interval to highlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started. 
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void unhighlightCellColumnRange(IntMatrix ia, int row, 
			int startCol, int endCol, Timing offset, Timing duration);
	
	/**
	 * Unhighlights a range of array cells of an <code>IntMatrix</code>.
	 * 
	 * @param startRow the start row of the interval to highlight.
	 * @param endRow the end row of the interval to highlight.
	 * @param col the column of the interval to highlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started. 
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void unhighlightCellRowRange(IntMatrix ia, int startRow, 
			int endRow, int col, Timing offset, Timing duration);
	
	/**
	 * Highlights the array element of an <code>IntMatrix</code> at a given position 
	 * after a distinct offset.
	 * 
	 * @param ia the <code>IntMatrix</code> to work on.
	 * @param row the row of the element to highlight.
	 * @param col the column of the element to highlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void highlightElem(IntMatrix ia, int row, int col, Timing offset, 
			Timing duration);
	
	/**
	 * Highlights a range of array elements of an <code>IntMatrix</code>.
	 * 
	 * @param ia the <code>IntMatrix</code> to work on.
	 * @param row the row of the interval to highlight.
	 * @param startCol the start of the column interval to highlight.
	 * @param endCol the end of the column interval to highlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started. 
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void highlightElemColumnRange(IntMatrix ia, int row, 
			int startCol, int endCol, Timing offset, Timing duration);
	
	
	/**
	 * Highlights a range of array elements of an <code>IntMatrix</code>.
	 * 
	 * @param ia the <code>IntMatrix</code> to work on.
	 * @param startRow the start of the row interval to highlight.
	 * @param endRow the end of the row interval to highlight.
	 * @param col the column interval to highlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started. 
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void highlightElemRowRange(IntMatrix ia, int startRow, 
			int endRow, int col, Timing offset, Timing duration);
	
	/**
	 * Unhighlights the array element of an <code>IntMatrix</code> at a given position 
	 * after a distinct offset.
	 * 
	 * @param ia the <code>IntMatrix</code> to work on.
	 * @param row the row of the element to unhighlight.
	 * @param col the column of the element to unhighlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void unhighlightElem(IntMatrix ia, int row, int col, Timing offset, 
			Timing duration);
	
	/**
	 * Unhighlights a range of array elements of an <code>IntMatrix</code>.
	 * 
	 * @param ia the <code>IntMatrix</code> to work on.
	 * @param row the row of the interval to unhighlight.
	 * @param startCol the start of the column interval to unhighlight.
	 * @param endCol the end of the column interval to unhighlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started. 
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void unhighlightElemColumnRange(IntMatrix ia, int row, 
			int startCol, int endCol, Timing offset, Timing duration);


	/**
	 * Unhighlights a range of array elements of an <code>IntMatrix</code>.
	 * 
	 * @param ia the <code>IntMatrix</code> to work on.
	 * @param startRow the start row of the interval to unhighlight.
	 * @param endRow the end row of the interval to unhighlight.
	 * @param col the column interval to unhighlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started. 
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void unhighlightElemRowRange(IntMatrix ia, int startRow, 
			int endRow, int col, Timing offset, Timing duration);

	/**
	 * Set the Color for a cell of an <code>IntMatrix</code>.
	 * 
	 * @param ia
	 *          the <code>IntMatrix</code> to work on.
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
	public void setGridColor(IntMatrix ia, int row, int col, Color color, String kind,
			Timing offset, Timing duration);
	
	public void setGridFont(IntMatrix ia, int row, int col, Font font,
		      Timing offset, Timing duration);
}
