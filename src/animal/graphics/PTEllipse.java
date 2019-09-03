package animal.graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import animal.graphics.meta.ClosedArcBasedShape;
import animal.graphics.meta.EllipsoidShape;
import animal.misc.XProperties;

/**
 * Implements an ellipse object.
 * 
 * <strong>Important:</strong> "angles" in the ellipse are not "real" angles of
 * the points relative to the ellipse's center, but the values that have to be
 * used as parameter to the sin/cos-calls when calculating points.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>
 * @version 1.3 2008-10-24
 */
public class PTEllipse extends ClosedArcBasedShape implements
		EllipsoidShape {
	// =================================================================
	// CONSTANTS
	// =================================================================

	public static final String	ELLIPSE_TYPE				= "Ellipse";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long		serialVersionUID	= 4711724437359551059L;

	// =================================================================
	// ATTRIBUTES
	// =================================================================

	/**
	 * the ellipse's radius
	 */
	private Point								radius						= new Point(0, 0);

	// =================================================================
	// CONSTRUCTORS
	// =================================================================

	/**
	 * Public constructor required for serialization, inits text component
	 */
	public PTEllipse() {
		initializeWithDefaults(getType());
	}

	// =================================================================
	// ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * returns the "angle" relative to the Arc, i.e. the "angle" (i.e., the
	 * parameter that has to be inserted into the sin() and cos() calls) of the
	 * arc's point that is on the same line from the center as <em>p</em>.
	 * Thus,(x*cos(alpha),y*sin(alpha))*dist(point,center) would point from the
	 * center to <em>p</em>, unlike the normal getArcAngle function.
	 * 
	 * @return angle in degrees: 0 &lt;= angle &lt; 360, -1, if xRadius or yRadius
	 *         is 0.
	 */
	public int getAngle(Point p) {
		return getAngle(p, getXRadius(), getYRadius());
	}

	/**
	 * Return the bounding box for the arc if it had an angle of 360. This is
	 * "required" for dragging the center and the size. (It could be done without,
	 * but then the center would be outside the bounding box!)
	 * 
	 * @return the bounding box of the "whole" arc
	 */
	public Rectangle getBoundingBox() {
		return getBoundingBox(getXRadius(), getYRadius());
	}

	/**
	 * This constant is used for the file version. It must be incremented whenever
	 * the save format is changed.
	 * 
	 * Versions include:
	 * 
	 * <ol>
	 * <li>original release</li>
	 * </ol>
	 */
	public int getFileVersion() {
		return 1;
	}
	
	/**
	 * The length of the ellipse is only the amount of the ellipseangle. Thus the
	 * translation is not "linear", as this would require heavy-duty integrals.
	 * 
	 * @return the length of the circle as the total angle.
	 */
	public int getLength() {
		return 360; // degrees, not pixels!
	}

	/**
	 * returns the point on the ellipse in the "angle" alpha.
	 * 
	 * @param alpha
	 *          the angle to be examined
	 * 
	 * @return the point at the given angle
	 */
	public Point getPointAtAngle(int alpha) {
		Point localCenter = getCenter();
		double x = alpha * Math.PI / 180;
		return new Point((int) (localCenter.x + getXRadius() * Math.cos(x)),
				(int) (localCenter.y - getYRadius() * Math.sin(x)));
	}

	/**
	 * returns the point on the arc at "length" from the ellipse start
	 * 
	 * @param alpha the length to be examined
	 * 
	 * @return the point at the given length
	 */
	public Point getPointAtLength(int alpha) {
		return getPointAtLength(alpha, radius.x, radius.y);
	}

	/**
	 * Returns the ellipse's radius as a point.
	 * 
	 * @return the radius of this arc
	 */
	public Point getRadius() {
		return radius;
	}

	public String getType() {
		return PTEllipse.ELLIPSE_TYPE;
	}

	/**
	 * Returns the x radius of this arc.
	 * 
	 * @return the x radius of this arc
	 */
	public int getXRadius() {
		return getRadius().x;
	}

	/**
	 * Returns the y radius of this arc.
	 * 
	 * @return the y radius of this arc
	 */
	public int getYRadius() {
		return getRadius().y;
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "Ellipse" };
	}

	/**
	 * Set the ellipse's radius
	 * 
	 * @param targetRadius
	 *          the target value for the ellipse radius
	 */
	public void setRadius(Point targetRadius) {
		if (targetRadius != null)
			setRadius(targetRadius.x, targetRadius.y);
		else
			setRadius(0, 0);
	}

	/**
	 * Set the ellipse's radius
	 * 
	 * @param radiusX
	 *          the radius in x direction
	 * @param radiusY
	 *          the radius in y direction
	 */
	public void setRadius(int radiusX, int radiusY) {
		radius = new Point(radiusX, radiusY);
	}

	/**
	 * Set the ellipse's x radius
	 * 
	 * @param r
	 *          the x radius of the ellipse
	 */
	public void setXRadius(int r) {
		setRadius(r, getYRadius());
	}

	/**
	 * Set the ellipse's y radius
	 * 
	 * @param r
	 *          the y radius of the ellipse
	 */
	public void setYRadius(int r) {
		setRadius(getXRadius(), r);
	}

	/**
	 * Return a string describing the object
	 * 
	 * @return The String representation of the PTEllipse
	 */
	public String toString() {
		return toString("PTEllipse", false, getXRadius(), getYRadius());
	}
	
	/**
	 * Update the default properties for this object
	 * 
	 * @param defaultProperties
	 *          the properties to be updated
	 */
	public void updateDefaults(XProperties defaultProperties) {
		super.updateDefaults(defaultProperties);
		defaultProperties.put(getType() + ".radius", getRadius());
	}
	
	// =================================================================
	// GRAPHIC OPERATIONS
	// =================================================================

	/**
	 * Paint the specified arc including possible arrow(s)
	 * 
	 * @param g
	 *          the Graphics object used for painting
	 */
	public void paint(Graphics g) {
		paint(g, getXRadius(), getYRadius(), 0, 360);
	}

	// =================================================================
	// I/O
	// =================================================================

	/**
	 * Clones the current graphical object. Note that the method will per
	 * definition return Object, so the result has to be cast to the appropriate
	 * type.
	 * 
	 * @return a clone of the current object, statically typed as Object.
	 */
	public Object clone() {
		// create new object
		PTEllipse targetShape = new PTEllipse();

		// clone shared attributes
		cloneCommonFeaturesInto(targetShape);

		// clone anything else that is specific to this type
		// and its potential subtypes
		return targetShape;
	}

	/**
	 * Offers cloning support by cloning or duplicating the shared attributes
	 * 
	 * @param targetShape
	 *          the shape into which the values are to be copied. Note the
	 *          direction -- it is
	 *          "currentObject.cloneCommonFeaturesInto(newObject)", not vice
	 *          versa!
	 */
	protected void cloneCommonFeaturesInto(PTEllipse targetShape) {
		// clone features from PTGraphicsObject: color, depth, num, objectName
		super.cloneCommonFeaturesInto(targetShape);

		// clone anything else that is specific to this type
		// and its potential subtypes
		targetShape.setRadius(getXRadius(), getYRadius());
	}


	/**
	 * Reset the attributes for this primitive for a "clean memory" state.
	 */
	public void discard() {
		super.discard();
		radius = null;
	}
} // PTEllipse
