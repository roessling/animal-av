package animal.graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import animal.graphics.meta.CircularShape;
import animal.graphics.meta.ClosedArcBasedShape;
import animal.misc.XProperties;

/**
 * Implements a circle object.
 * 
 * <strong>Important:</strong> "angles" in the circle are not "real" angles of
 * the points relative to the circle's center but the values that have to be
 * used as parameter to the sin/cos-calls when calculating points.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>
 * @version 1.3 2008-10-24
 */
public class PTCircle extends ClosedArcBasedShape 
implements CircularShape {
	// =================================================================
	// CONSTANTS
	// =================================================================

	public static final String CIRCLE_TYPE	= "Circle";

	// =================================================================
	// ATTRIBUTES
	// =================================================================

	private int									radius;

	/**
	 * This constant is used for the file version. It must be incremented whenever
	 * the save format is changed.
	 * 
	 * Versions include:
	 * <ol>
	 * <li>original release</li>
	 * </ol>
	 */
	public int getFileVersion() {
		return 1;
	}

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long	serialVersionUID	= 4711724437159551059L;

	// =================================================================
	// CONSTRUCTORS
	// =================================================================

	/**
	 * Public constructor required for serialization, inits text component
	 */
	public PTCircle() {
		initializeWithDefaults(getType());
	}

	// =================================================================
	// GRAPHIC OPERATIONS
	// =================================================================

	/**
	 * Paint the specified circle including possible arrow(s)
	 * 
	 * @param g
	 *          the Graphics object used for painting
	 */
	public void paint(Graphics g) {
		paint(g, radius, radius, 0, 360);
	}

	/**
	 * <em>rotate(double)</em> performs a 2D rotation on the given PTPoint, using
	 * the coordinate(0, 0) of the current viewport as the rotation center.
	 * 
	 * <p>
	 * If a different rotation center is desired, use
	 * <em>rotate(double, center)</em> instead.
	 * </p>
	 * 
	 * <p>
	 * The formula needed for performing the transformation can be found in the PT
	 * slides on page GTR-21 or in Foley/van Dam, page 206, formula 5.24
	 * </p>
	 * 
	 * Non-integer coordinates resulting from a rotate operation will be rounded
	 * to the nearest integer coordinates.
	 * 
	 * @param angle
	 *          The rotation angle in polar measurement(0...2*pi)
	 * 
	 * @see #rotate(double, animal.graphics.PTPoint)
	 */
	public void rotate(double angle) {
		// rotating circles means: (i) rotate circle's center;
		// (ii) change start angle
		PTPoint centerNode = new PTPoint(center);
		centerNode.rotate(angle);
		center = centerNode.toPoint();
	}

	/**
	 * <em>rotate(double, PTPoint center)</em> performs a 2D rotation on the given
	 * PTPoint, using the parameter <em>center</em> as the center of the rotation.
	 * 
	 * If the rotation center to be used is equal to the viewport point of origin
	 * (i.e., coordinate(0, 0)), use <em>rotate(double)</em>.
	 * 
	 * 
	 * The formula needed for performing the transformation can be found in
	 * Foley/van Dam, page 206, formula 5.24
	 * 
	 * Non-integer coordinates resulting from a rotate operation will be rounded
	 * to the nearest integer coordinates.
	 * 
	 * @param angle
	 *          The rotation angle in polar measurement(0...2*pi)
	 * 
	 * @param centerPoint
	 *          The point which is to be considered as the rotation center.
	 */
	public void rotate(double angle, PTPoint centerPoint) {
		// Translate center of rotation to origin
		translate(-centerPoint.getX(), -centerPoint.getY());

		// Rotate(around point of origin)
		rotate(angle);

		// Translate back
		translate(centerPoint.getX(), centerPoint.getY());
	}

	// =================================================================
	// ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * returns the "angle" relative to the Arc, i.e. the "angle" (i.e. the
	 * parameter that has to be inserted into the sin() and cos() calls) of the
	 * circle's point that is on the same line from the center as <em>p</em>.
	 * Thus,(x*cos(alpha),y*sin(alpha))*dist(point,center) would point from the
	 * center to <em>p</em>, unlike the normal getArcAngle function.
	 * 
	 * @return angle in degrees: 0 &lt;= angle &lt; 360, -1, if xRadius or yRadius
	 *         is 0.
	 */
	public int getAngle(Point p) {
		return getAngle(p, radius, radius);
	}

	/**
	 * Return the bounding box for the circle if it had an angle of 360. This is
	 * "required" for dragging the center and the size.(could be done without, but
	 * then the center would be outside the BoundingBox.
	 * 
	 * @return the bounding box of the "whole" circle
	 */
	public Rectangle getBoundingBox() {
		return getBoundingBox(radius, radius);
	}


	/**
	 * The length of the circle is only the amount of the total angle. Thus the
	 * translation is not "linear", as this would require heavy-duty integrals.
	 * 
	 * @return the length of the circle as the total angle.
	 */
	public int getLength() {
		return 360;
	}

	/**
	 * returns the point on the ellipse in the "angle" alpha.
	 * 
	 * @param alpha
	 *          the angle to be examined
	 * 
	 * @return the point at the given angle
	 */
	public Point getPointAtLength(int alpha) {
		return getPointAtLength(alpha, radius, radius);
	}

	/**
	 * Returns the circle's radius
	 * 
	 * @return the radius of this circle
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * returns the type of this object
	 * 
	 * @return the type of the object
	 */
	public String getType() {
		return PTCircle.CIRCLE_TYPE;
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "Circle" };
	}

	/**
	 * Set the <em>circle</em>'s radius by setting xRadius == yRadius
	 * 
	 * @param r
	 *          the target value for the circle radius shared by x and y radius
	 */
	public void setRadius(int r) {
		radius = r;
	}

	/**
	 * Return a string describing the object
	 * 
	 * @return The String representation of the PTArc
	 */
	public String toString() {
		return toString(getType(), true, radius, radius);
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
	// Cloning Support
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
		PTCircle targetShape = new PTCircle();

		// clone shared attributes
		// from PTGO: color, depth, num, objectName
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
	protected void cloneCommonFeaturesInto(PTCircle targetShape) {
		// clone features from PTGraphicsObject: color, depth, num, objectName
		// from ArcBasedShape: center
		// from ClosedArcBasedShape: isFilled, fillColor
		super.cloneCommonFeaturesInto(targetShape);

		// clone anything else that is specific to this type
		// and its potential subtypes
		targetShape.setRadius(radius);
	}
} // PTCircle
