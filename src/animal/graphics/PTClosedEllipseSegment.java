package animal.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import animal.graphics.meta.ClosedArcBasedShape;
import animal.graphics.meta.EllipsoidShape;
import animal.graphics.meta.OrientedPrimitive;
import animal.main.AnimalConfiguration;
import animal.misc.XProperties;

/**
 * Implements a closed ellipse segment ("pie") object.
 * 
 * <strong>Important:</strong> "angles" in the ellipse are not "real" angles of
 * the points relative to the ellipse center but the values that have to be used
 * as parameter to the sin/cos-calls when calculating points.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>
 * @version 1.1 2008-10-24
 */
public class PTClosedEllipseSegment extends ClosedArcBasedShape
implements EllipsoidShape, OrientedPrimitive {
	// =================================================================
	// CONSTANTS
	// =================================================================

	public static final String	CLOSED_ELLIPSE_SEGMENT_TYPE = "ClosedEllipseSegment";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long		serialVersionUID	= 4711724437159551079L;

	// =================================================================
	// ATTRIBUTES
	// =================================================================

	/**
	 * determines if the orientation is clockwise or counterclockwise
	 */
	private boolean							isClockwise				= false;

	/**
	 * the closed circle segment's radius
	 */
	private Point								radius						= new Point(0, 0);

	/**
	 * the closed circle segment's start angle
	 */
	private int									startAngle				= 0;

	/**
	 * the closed circle segment's total angle
	 */
	private int									totalAngle				= 45;

	// =================================================================
	// CONSTRUCTORS
	// =================================================================

	/**
	 * Public constructor required for serialization, inits text component
	 */
	public PTClosedEllipseSegment() {
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
		paint(g, radius.x, radius.y, startAngle, totalAngle);

    // ---------------------------------------------------------
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    // to get the former version change g2 into g and remove the code above
    // under the line
		if (totalAngle % 360 != 0) {
			Point a = getPointAtLength(startAngle);
      g2.drawLine(center.x, center.y, a.x, a.y);
			a = getPointAtLength(startAngle + totalAngle);
      g2.drawLine(center.x, center.y, a.x, a.y);
		}
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
		return getAngle(p, radius.x, radius.y);
	}

	/**
	 * Return the bounding box for the circle if it had an angle of 360. This is
	 * "required" for dragging the center and the size.(could be done without, but
	 * then the center would be outside the BoundingBox.
	 * 
	 * @return the bounding box of the "whole" circle
	 */
	public Rectangle getBoundingBox() {
		return getBoundingBox(radius.x, radius.y);
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
	 * The length of the circle is only the amount of the circleAngle. Thus the
	 * translation is not "linear", as this would require heavy-duty integrals.
	 * 
	 * @return the length of the circle as the total angle.
	 */
	public int getLength() {
		return Math.abs(getTotalAngle());
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
    return getPointAtLength((alpha + 360 % 360), radius.x, radius.y);
    // used to be:
//    return getPointAtLength(//getStartAngle() + 
//        (isClockwise ? -alpha : alpha), 
//        radius.x, radius.y);
	}

	/**
	 * Returns the circle's radius
	 * 
	 * @return the radius of this circle
	 */
	public Point getRadius() {
		return radius;
	}

	/**
	 * returns the start angle of the component
	 * 
	 * @return the start angle of the component
	 */
	public int getStartAngle() {
		return startAngle;
	}

	/**
	 * returns the total angle of the component, which may also be negative
	 * 
	 * @return the angle of the component
	 */
	public int getTotalAngle() {
		return totalAngle;
	}

	/**
	 * returns the type of this object
	 * 
	 * @return the type of the object
	 */
	public String getType() {
		return PTClosedEllipseSegment.CLOSED_ELLIPSE_SEGMENT_TYPE;
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "ClosedEllipseSegment" };
	}

	/**
	 * This method initializes the primitive with the primitive type's default
	 * values (looked up at the default properties)
	 * 
	 * @param primitiveName
	 *          the name of the primitive to support inheritance, e.g. "Circle".
	 */
	public void initializeWithDefaults(String primitiveName) {
		super.initializeWithDefaults(primitiveName);
		AnimalConfiguration config = AnimalConfiguration.getDefaultConfiguration();
		isClockwise = config.getDefaultBooleanValue(getType(), "clockwise", false);
	}

	/**
	 * returns if this object is oriented clockwise or counterclockwise
	 * 
	 * @return <em>true</em> if the object is oriented clockwise, else false (the
	 *         default) for counterclockwise.
	 */
	public boolean isClockwise() {
		return isClockwise;
	}

	/**
	 * sets if this object is oriented clockwise or counterclockwise
	 * 
	 * @param isOrientationClockwise
	 *          if <em>true</em>, then the object is oriented clockwise, else
	 *          counterclockwise.
	 */
	public void setClockwise(boolean isOrientationClockwise) {
		if (isClockwise != isOrientationClockwise) {
			if (isOrientationClockwise)
				setTotalAngle((getTotalAngle() - 360) % 360);
			else
				setTotalAngle((360 + getTotalAngle() % 360));
		}
		isClockwise = isOrientationClockwise;
	}

	/**
	 * Set the <em>circle</em>'s radius by setting xRadius == yRadius
	 * 
	 * @param r
	 *          the target value for the circle radius shared by x and y radius
	 */
	public void setRadius(Point r) {
		if (r != null)
			setRadius(r.x, r.y);
	}

	/**
	 * Set the <em>circle</em>'s radius by setting xRadius == yRadius
	 * 
	 * @param x the x radius of the open ellipse segment
	 * @param y the y radius of the open ellipse segment
	 */
	public void setRadius(int x, int y) {
		radius = new Point(Math.abs(x), Math.abs(y));
	}

	/**
	 * sets the start angle of the component
	 * 
	 * @param angle
	 *          the start angle of the component
	 */
	public void setStartAngle(int angle) {
		startAngle = angle;
	}

	/**
	 * sets the total angle of the component, which may also be negative
	 * 
	 * @param angle
	 *          the angle of the component
	 */
	public void setTotalAngle(int angle) {
		totalAngle = angle;
	}

	/**
	 * Update the default properties for this object
	 * 
	 * @param defaultProperties
	 *          the properties to be updated
	 */
	public void updateDefaults(XProperties defaultProperties) {
		super.updateDefaults(defaultProperties);
		defaultProperties.put(getType() + ".clockwise", isClockwise());
		defaultProperties.put(getType() + ".radius", getRadius());
		defaultProperties.put(getType() + ".startAngle", getStartAngle());
		defaultProperties.put(getType() + ".totalAngle", getTotalAngle());
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
		PTClosedEllipseSegment targetShape = new PTClosedEllipseSegment();

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
	protected void cloneCommonFeaturesInto(PTClosedEllipseSegment targetShape) {
		// clone features from PTGraphicsObject: color, depth, num, objectName
		// from ArcBasedShape: center
		// from ClosedArcBasedShape: isFilled, fillColor
		super.cloneCommonFeaturesInto(targetShape);

		// clone anything else that is specific to this type
		// and its potential subtypes
		targetShape.setClockwise(isClockwise());
		targetShape.setRadius(new Point(getRadius().x, getRadius().y));
		targetShape.setStartAngle(getStartAngle());
		targetShape.setTotalAngle(getTotalAngle());
	}


	/**
	 * Return a string describing the object
	 * 
	 * @return The String representation of the PTArc
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		String prefix = toString(getType(), false, radius.x, radius.y);
		sb.append(prefix);
		sb.append(", starts at ").append(getStartAngle());
		sb.append(", angle=").append(getTotalAngle());
		if (isClockwise)
			sb.append(" clockwise");
		return sb.toString();
	}
} // PTClosedEllipseSegment
