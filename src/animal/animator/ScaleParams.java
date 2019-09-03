package animal.animator;

import animal.graphics.PTPoint;

/**
 * Class needed for parameter passing from Scale to GraphicObjects
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class ScaleParams {
	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * The center point for the rotation
	 */
	PTPoint center;

	/**
	 * The x scale factor for the operation
	 */
	double xScale;

	/**
	 * The y scale factor for the operation
	 */
	double yScale;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Initialize the parameters from the values passed in.
	 * 
	 * @param centerPoint
	 *            the center for the scaling operation
	 * @param scaleX
	 *            the scaling factor to be used in x direction
	 * @param scaleY
	 *            the scaling factor to be used in y direction
	 */
	public ScaleParams(PTPoint centerPoint, double scaleX, double scaleY) {
		center = centerPoint;
		xScale = scaleX;
		yScale = scaleY;
	}

	// =================================================================
	//                                 ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Returns the scaling center point.
	 * 
	 * @return the center of the scale animator
	 */
	public PTPoint getCenter() {
		return center;
	}

	/**
	 * Returns the x scaling factor
	 * 
	 * @return the factor for the x scaling operation
	 */
	public double getXScaleFactor() {
		return xScale;
	}

	/**
	 * Returns the y scaling factor
	 * 
	 * @return the factor for the y scaling operation
	 */
	public double getYScaleFactor() {
		return yScale;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(150);
		sb.append("scale factor: (").append(getXScaleFactor()).append("x");
		sb.append(getXScaleFactor()).append(") ");
		if (getCenter() != null) {
			sb.append("center: (").append(getCenter().getX()).append(", ");
			sb.append(getCenter().getY()).append(")");
		}
		return sb.toString();
	}
}
