/*
 * Created on 01.08.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.graphics.meta;

import java.awt.Point;
import java.awt.Rectangle;

import animal.animator.MoveBase;
import animal.graphics.PTGraphicObject;
import animal.misc.MSMath;
import animal.misc.XProperties;

/**
 * This class models objects that are based on an arc shape, i.e., primarily
 * circles and ellipses or segments thereof.
 * 
 * @author Dr. Guido R&ouml;&szlig;ling (roessling@acm.org>
 * @version 1.1 2008-10-24
 */
public abstract class ArcBasedShape extends PTGraphicObject implements MoveBase {

  /**
   * The center point of this arc-based shape. Will always be a Point, whereas
   * the radius may be a point (for arc- and ellipse-based objects) or a int
   * (for circles and circle segments).
   */
  protected Point center;

	/**
	 * returns the "angle" relative to the Arc, i.e. the "angle" (i.e. the
	 * parameter that has to be inserted into the sin() and cos() calls) of the
	 * circle's point that is on the same line from the center as <em>p</em>.
	 * Thus,(x*cos(alpha),y*sin(alpha))*dist(point,center) would point from the
	 * center to <em>p</em>, unlike the normal getArcAngle function.
	 * 
	 * @param p the point to which the angle is measured
	 * @param xRadius the x radius of the underlying arc-based shape
	 * @param yRadius the y radius of the underlying arc-based shape
	 * @return angle in degrees: 0 &lt;= angle &lt; 360, -1, if xRadius or yRadius
	 *         is 0.
	 * @see #getArcAngle
	 */
	protected int getAngle(Point p, int xRadius, int yRadius) {
		if (p == null || xRadius == 0 || yRadius == 0)
			return -1;
		int xdist = p.x - center.x;
		int ydist = p.y - center.y;
		int angle = (int) (MSMath.getAngle(center, 
				new Point(center.x + xdist * 100 / xRadius, 
						center.y + ydist * 100 / yRadius)) 
						* 180 / Math.PI);
		if (angle < 0)
			angle += 360;
		return angle;
	}
	
	/**
	 * Return the bounding box for the circle if it had an angle of 360. This is
	 * "required" for dragging the center and the size. It could be done without, but
	 * then the center would be outside the BoundingBox.
	 * 
	 * @param rx the x radius
	 * @param ry the y radius
	 * @return the bounding box of the "whole" circle
	 */
	protected Rectangle getBoundingBox(int rx, int ry) {
		return new Rectangle(center.x - rx, center.y - ry, rx * 2, ry * 2);
	}

  /**
   * Returns the center of this arc-based shape.
   * 
   * @return the center of this arc-based shape
   */
  public Point getCenter() {
    if (center == null)
      center = new Point(10, 10);
    return center;
  }

	/**
	 * returns the point on the ellipse in the "angle" alpha.
	 * 
	 * @param alpha
	 *          the angle to be examined
	 * @param rx the x radius of the arc-based shape
	 * @param ry the y radius of the arc-based shape
	 * 
	 * @return the point at the given angle
	 */
	protected Point getPointAtLength(int alpha, int rx, int ry) {
		double x = alpha * Math.PI / 180;
		return new Point((int) (center.x + rx * Math.cos(x)),
				(int) (center.y - ry * Math.sin(x)));
	}
  /**
   * Set the arc-based shape's center point
   * 
   * @param x
   *          the x coordinate of the arc-based shape
   * @param y
   *          the y coordinate of the arc-based shape
   */
  public void setCenter(int x, int y) {
    center = new Point(x, y);
  }

  /**
   * Set the arc-based shape's center point by copying the values, not using it
   * as a reference. Changing the Point passed in will therefore <em>not</em>
   * affect the shape.
   * 
   * @param localCenter
   *          the center point of the arc
   */
  public void setCenter(Point localCenter) {
    setCenter(localCenter.x, localCenter.y);
  }

	/**
	 * Implementation of the "translate" operation for the arc
	 * 
	 * @param x the x vector for the translation
	 * @param y the y vector for the translation
	 */
	public void translate(int x, int y) {
		center.translate(x, y);
	}

  /**
   * Update the default properties for this object
   * 
   * @param defaultProperties
   *          the properties to be updated
   */
  public void updateDefaults(XProperties defaultProperties) {
    super.updateDefaults(defaultProperties);
    defaultProperties.put(getType() + ".center", getCenter());
  }

  // ======================================================================
  // Cloning support
  // ======================================================================

  /**
   * Offers cloning support by cloning or duplicating the shared attributes
   * 
   * @param targetShape
   *          the ClosedArcBasedShape into which the values are to be copied.
   *          Note the direction -- it is
   *          "currentObject.cloneCommonFeaturesInto(newObject)", not vice
   *          versa!
   */
  protected void cloneCommonFeaturesInto(ArcBasedShape targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
    super.cloneCommonFeaturesInto(targetShape);
    targetShape.setCenter(getCenter().x, getCenter().y);
  }
} // ArcBasedShape
