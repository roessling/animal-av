package animal.animator;

import animal.graphics.PTPoint;

/**
 * Class needed for parameter passing from <strong>Rotate </strong> to
 * GraphicObjects.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class Rotation {
	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * The center point for the rotation
	 */
	PTPoint center;

	/**
	 * The degree for the rotation, usually -360...360
	 */
	double angle;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Initialize the Rotation object with the given center and rotation angle.
	 * 
	 * @param centerPoint
	 *            the center point for the rotation
	 * @param targetAngle
	 *            the angle as a float
	 */
	public Rotation(PTPoint centerPoint, double targetAngle) {
		center = centerPoint;
		angle = targetAngle;
	}

	// =================================================================
	//                                 ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Returns the rotation center point.
	 * 
	 * @return the center of the rotation
	 */
	public PTPoint getCenter() {
		return center;
	}

	/**
	 * Returns the rotation angle.
	 * 
	 * @return the angle of the rotation
	 */
	public double getAngle() {
		return angle;
	}
}
