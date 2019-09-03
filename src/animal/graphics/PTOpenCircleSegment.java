package animal.graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import animal.graphics.meta.CircularShape;
import animal.graphics.meta.OpenArcBasedShape;
import animal.graphics.meta.OrientedPrimitive;
import animal.misc.XProperties;

/**
 * Implements a closed circle segment ("pie") object.
 * 
 * <strong>Important:</strong> "angles" in the circle are not "real" angles of
 * the points relative to the circle's center but the values that have to be
 * used as parameter to the sin/cos-calls when calculating points.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>
 * @version 1.2 2008-10-24
 */
public class PTOpenCircleSegment extends OpenArcBasedShape
implements CircularShape, OrientedPrimitive {
	// =================================================================
	// CONSTANTS
	// =================================================================

	public static final String	OPEN_CIRCLE_SEGMENT_TYPE = "OpenCircleSegment";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long		serialVersionUID	= 1311724437159551059L;

	// =================================================================
	// ATTRIBUTES
	// =================================================================

	/**
	 * the open circle segment's radius
	 */
	private int									radius;

	// =================================================================
	// CONSTRUCTORS
	// =================================================================

	/**
	 * Public constructor required for serialization, inits text component
	 */
	public PTOpenCircleSegment() {
		initializeWithDefaults(getType());
	}

	// =================================================================
	// ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * returns the "angle" relative to the shape, i.e. the "angle" (i.e. the
	 * parameter that has to be inserted into the sin() and cos() calls) of the
	 * circle's point that is on the same line from the center as <em>p</em>.
	 * Thus,(x*cos(alpha),y*sin(alpha))*dist(point,center) would point from the
	 * center to <em>p</em>, unlike the normal getArcAngle function.
	 * 
	 * @param p the point for which the angle is determined
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
	 * returns the point on the circle in the "angle" alpha.
	 * 
	 * @param alpha
	 *          the angle to be examined
	 * 
	 * @return the point at the given angle
	 */
	public Point getPointAtLength(int alpha) {
		return getPointAtLength(getStartAngle() + (isClockwise ? -alpha : alpha), 
				radius, radius);
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
		return PTOpenCircleSegment.OPEN_CIRCLE_SEGMENT_TYPE;
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "OpenCircleSegment" };
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
	// GRAPHIC OPERATIONS
	// =================================================================

	/**
	 * Paint the specified circle segment including possible arrow(s)
	 * 
	 * @param g
	 *          the Graphics object used for painting
	 */
	public void paint(Graphics g) {
		paint(g, radius, radius);
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
		PTOpenCircleSegment targetShape = new PTOpenCircleSegment();

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
	protected void cloneCommonFeaturesInto(PTOpenCircleSegment targetShape) {
		// clone features from PTGraphicsObject: color, depth, num, objectName
		// from ArcBasedShape: center
		// from OpenArcBasedShape: bwArrow, clockwise, fwArrow, startAngle,
		// totalAngle
		super.cloneCommonFeaturesInto(targetShape);

		// clone anything else that is specific to this type
		// and its potential subtypes
		targetShape.setRadius(getRadius());
	}
} // PTOpenCircleSegment
