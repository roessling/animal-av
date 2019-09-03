package animal.graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import animal.graphics.meta.EllipsoidShape;
import animal.graphics.meta.OpenArcBasedShape;
import animal.graphics.meta.OrientedPrimitive;
import animal.misc.XProperties;

/**
 * Implements an open ellipse segment object.
 * 
 * <strong>Important:</strong> "angles" in the ellipse are not 
 * "real" angles of the points relative to the ellipse's center
 * but the values that have to be used as parameter to the 
 * sin/cos-calls when calculating points.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.2 2008-10-24
 */
public class PTOpenEllipseSegment extends OpenArcBasedShape
implements EllipsoidShape, OrientedPrimitive {
	// =================================================================
	// CONSTANTS
	// =================================================================

	public static final String OPEN_ELLIPSE_SEGMENT_TYPE = "OpenEllipseSegment";

  /**
   * This constant contains the UID for serialization, <em>do not edit</em>!
   */
//  private static final long serialVersionUID = 4711724437159551059L;

  // =================================================================
  // ATTRIBUTES
  // =================================================================

  /** 
   * the open ellipse segment's radius 
   */
  private Point radius = new Point(10, 10);


	// =================================================================
	// CONSTRUCTORS
	// =================================================================

	/**
	 * Public constructor required for serialization, inits text component
	 */
	public PTOpenEllipseSegment() {
		initializeWithDefaults(getType());
	}

	// =================================================================
	// ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * returns the "angle" relative to the Arc, i.e. the "angle" (i.e. the
	 * parameter that has to be inserted into the sin() and cos() calls) of the
	 * ellipse's point that is on the same line from the center as <em>p</em>.
	 * Thus,(x*cos(alpha),y*sin(alpha))*dist(point,center) would point from the
	 * center to <em>p</em>, unlike the normal getArcAngle function.
	 * 
	 * @param p the point for which the angle is measured
	 * @return angle in degrees: 0 &lt;= angle &lt; 360, -1, if xRadius or yRadius
	 *         is 0.
	 */
	public int getAngle(Point p) {
		return getAngle(p, radius.x, radius.y);
	}

	/**
	 * Return the bounding box for the ellipse if it had an angle of 360. This is
	 * "required" for dragging the center and the size.(could be done without, but
	 * then the center would be outside the BoundingBox.
	 * 
	 * @return the bounding box of the "whole" ellipse
	 */
	public Rectangle getBoundingBox() {
		return getBoundingBox(radius.x, radius.y);
	}

  /**
   * This constant is used for the file version. It must be incremented 
   * whenever the save format is changed.
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
	 * returns the point on the ellipse in the "angle" alpha.
	 * 
	 * @param alpha
	 *          the angle to be examined
	 * 
	 * @return the point at the given angle
	 */
	public Point getPointAtLength(int alpha) {
//		getStartAngle() + (isClockwise() ? -1 : 1) * x
//		System.err.println("get@alpha=" +alpha +": " + 
//				getPointAtLength(getStartAngle() + (isClockwise ? -alpha : alpha), radius.x, radius.y));
//		System.err.println("  alt: " +getPointAtLength(alpha, radius.x, radius.y));
////		getPointAtLength(getStartAngle() + (isClockwise() ? -alpha : ));
		return getPointAtLength(getStartAngle() + (isClockwise ? -alpha : alpha), 
				radius.x, radius.y);
//		return getPointAtLength(alpha, radius.x, radius.y);
	}

	/**
	 * Returns the ellipse's radius
	 * 
	 * @return the radius of this ellipse
	 */
	public Point getRadius() {
		return radius;
	}


	/**
	 * returns the type of this object
	 * 
	 * @return the type of the object
	 */
	public String getType() {
		return PTOpenEllipseSegment.OPEN_ELLIPSE_SEGMENT_TYPE;
	}


	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords 
	 * in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "OpenEllipseSegment" };
	}

	
	/**
	 * Set the <em>ellipse</em>'s radius by setting xRadius == yRadius
	 * 
	 * @param r the target value for the ellipse radius 
	 */
	public void setRadius(Point r) {
		if (r != null)
			setRadius(r.x, r.y);
		else if (radius == null)
		  setRadius(10, 20);
	}

	/**
	 * Set the <em>ellipse</em>'s radius by setting xRadius == yRadius
	 * 
	 * @param x the x radius of the open ellipse segment
	 * @param y the y radius of the open ellipse segment
	 */
	public void setRadius(int x, int y) {
		radius = new Point(x, y);
	}


	/**
	 * Return a string describing the object
	 * 
	 * @return The String representation of the PTArc
	 */
	public String toString() {
		return toString(getType(), false, radius.x, radius.y);
	}
	
	/**
	 * Update the default properties for this object
	 * @param defaultProperties the properties to be updated
	 */
	public void updateDefaults(XProperties defaultProperties) {
		super.updateDefaults(defaultProperties);
		defaultProperties.put(getType() +".radius", getRadius());
	}
	
	// =================================================================
	// GRAPHIC OPERATIONS
	// =================================================================

	/**
	 * Paint the specified ellipse including possible arrow(s)
	 * 
	 * @param g the Graphics object used for painting
	 */
	public void paint(Graphics g) {
		paint(g, radius.x, radius.y);
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
    PTOpenEllipseSegment targetShape = new PTOpenEllipseSegment();

    // clone shared attributes
    cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    return targetShape;
  }

  /**
   * Offers cloning support by cloning or duplicating the shared attributes
   * 
   * @param targetShape the shape into which the values are to be copied. Note
   * the direction -- it is "currentObject.cloneCommonFeaturesInto(newObject)", 
   * not vice versa!
   */
  protected void cloneCommonFeaturesInto(PTOpenEllipseSegment targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
  	// from ArcBasedShape: center
  	// from OpenArcBasedShape: bwArrow, clockwise, fwArrow, startAngle, totalAngle
    super.cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    targetShape.setRadius(new Point(getRadius().x, getRadius().y));
  }
} // PTOpenEllipseSegment
