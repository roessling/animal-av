package animal.animator;

import java.awt.Point;

/**
 * Interface for GraphicObjects along which another GraphicObject can move.
 * Possible GraphicObjects that could implement MoveBase: Polyline, Bezier,
 * Circle, Arc,... Designed for use with <code>Move</code> animator. The move
 * path must be an instance of <code>MoveBase</code>.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.2 2000-06-30
 * @see animal.animator.Move
 */
public interface MoveBase {
	// =================================================================
	//                                 ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Returns the total length of this GraphicObject. The length is defined as
	 * the number of pixels the object has to move when executed
	 * "slowly"(stepwise)
	 */
	public int getLength();

	/**
	 * returns the point at a certain length after length pixels. The point is
	 * determined after walking along the object for <em>length</em> pixels.
	 * 
	 * @param length
	 *            the length for which the point has to be determined, with 0
	 *            &lt;= length &lt;= getLength()
	 */
	public Point getPointAtLength(int length);

	/**
	 * Prepare this object to be used as a MoveBase. This could include options
	 * for displaying the move direction etc.
	 */
	public void useAsMoveBase();
}
