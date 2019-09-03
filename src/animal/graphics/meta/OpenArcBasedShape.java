/*
 * Created on 01.08.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.graphics.meta;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import translator.AnimalTranslator;
import animal.graphics.PTPoint;
import animal.main.AnimalConfiguration;
import animal.misc.MSMath;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

/**
 * This abstract class models a closed arc-based shape used for
 * objects such as ellipses and circles.
 *
 * @author Guido Roessling (roessling@acm.org)
 * @version 1.1 2008-10.24
 */
public abstract class OpenArcBasedShape extends ArcBasedShape
implements ArrowablePrimitive {	

	/**
	 * determines if the shape has a backward arrow
	 */
	protected boolean hasBackwardArrow;
	
	/**
	 * determines if the shape has a forward arrow
	 */
	protected boolean hasForwardArrow;
	
  /**
   * determines if the orientation is clockwise or counterclockwise
   */
  protected boolean isClockwise = false;
  
  /** 
   * the open ellipse segment's start angle 
   */
  protected int startAngle = 0;
  
  /** 
   * the open ellipse segment's total angle 
   */
  protected int totalAngle = 45;
  
	/**
	 * Draw an arrow on the graphics context provided
	 * 
	 * @param g the Graphics context for the drawing operation
	 * @param angle the angle of the arc
	 * @param clockwise the mode of the arc: clockwise or counter-clockwise
	 */
	protected void drawArrow(Graphics g, double angle, int xRadius,
			int yRadius, boolean clockwise) {
		// an angle that determines a point shortly "before" angle
		double before = angle + (clockwise ? 1 : -1) * 1d / 100;
		// the arc's tip is the point at the given angle
		// doubles are required because of rounding errors
		double tipX = center.x + xRadius * Math.cos(angle);
		double tipY = center.y - yRadius * Math.sin(angle);
		// inclination is the vector to the point "before"
		double inclinationX = center.x + xRadius * Math.cos(before) - tipX;
		double inclinationY = center.y - yRadius * Math.sin(before) - tipY;
		double length = Math.sqrt(inclinationX * inclinationX + inclinationY
				* inclinationY);
		// have an appropriate distance between tip and from, such that
		// the arrow is not too small(except when the radius is
		// small.
		Point from = new Point((int) (tipX + inclinationX * xRadius / length),
				(int) (tipY + inclinationY * yRadius / length));
		drawArrow(g, new Point((int) tipX, (int) tipY), from);
	}
  
  /**
   * Draws an arrow. The arrow is the bigger(up to a limit) the more distant
   * <i>tip</i> and <i>tail</i> are. This is a "closed arrow with indented
   * butt"(XFig)
   * 
   * @param g
   *          the Graphics to draw into
   * @param tip
   *          where the arrow points to
   * @param tail
   *          where the arrow comes from
   */
  protected void drawArrow(Graphics g, Point tip, Point tail) {
    if (tip == null || tail == null) {
      MessageDisplay.errorMsg(AnimalTranslator.translateMessage(
          "internalError", new String[] { "tip / tail null",
              "PTPolyline.drawArrow" }), MessageDisplay.PROGRAM_ERROR);
      return;
    }
    int xDiff = tail.x - tip.x;
    int yDiff = tail.y - tip.y;
    int dist = MSMath.dist(tip, tail);
    if (dist > 0) { // else no sensible orientation
      if (dist < 20)// don't make the tip too big for a small line

        dist = 20;
      // adapt the arrow size to the line length
      xDiff = xDiff * 20 / dist;
      yDiff = yDiff * 20 / dist;
      // tip, right wing, center, left wing
      // t,t+2d+d',t+d,t+2d-d', while d= (xDiff,yDiff), d'= (-yDiff,xDiff)
      int[] x = { tip.x, tip.x + yDiff / 2 + xDiff, tip.x + xDiff / 2,
          tip.x - yDiff / 2 + xDiff };
      int[] y = { tip.y, tip.y - xDiff / 2 + yDiff, tip.y + yDiff / 2,
          tip.y + xDiff / 2 + yDiff };

      // ---------------------------------------------------------
      Graphics2D g2 = (Graphics2D) g;

      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      // to get the former version change g2 into g and remove the code above
      // under the line
      g2.fillPolygon(x, y, 4);
    }
  }
  

	/**
	 * The length of the shape is only the amount of the total angle. Thus the
	 * translation is not "linear", as this would require heavy-duty integrals.
	 * 
	 * @return the length of the circle as the total angle.
	 */
	public int getLength() {
		return Math.abs(getTotalAngle());
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
	 * determines if the shape has a backward arrow
	 * 
	 * @return true if the shape has a backward arrow
	 */
	public boolean hasBWArrow() {
		return hasBackwardArrow;
	}

	/**
	 * determines if the shape has a forward arrow
	 * 
	 * @return true if the shape has a forward arrow
	 */
	public boolean hasFWArrow() {
		return hasForwardArrow;
	}


	/**
	 * This method initializes the primitive with the primitive
	 * type's default values (looked up at the default properties)
	 * 
	 * @param primitiveName the name of the primitive to support
	 * inheritance, e.g. "Circle".
	 */
	public void initializeWithDefaults(String primitiveName) {
		super.initializeWithDefaults(primitiveName);
		center = new Point(0, 0);
		AnimalConfiguration config = 
			AnimalConfiguration.getDefaultConfiguration();
		hasBackwardArrow = config.getDefaultBooleanValue(primitiveName, 
				"bwArrow", false);
		isClockwise = config.getDefaultBooleanValue(getType(), 
				"clockwise", false);
		hasForwardArrow = config.getDefaultBooleanValue(primitiveName, 
				"fwArrow", false);
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
				setTotalAngle((360 + getTotalAngle()) % 360);
		}
		isClockwise = isOrientationClockwise;
	}


	/**
	 * sets if the shape shall have a backward arrow
	 * 
	 * @param shallHaveBackwardArrow if true, the shape will have
	 * a backward arrow
	 */

	public void setBWArrow(boolean shallHaveBackwardArrow) {
		hasBackwardArrow = shallHaveBackwardArrow;
	}
	
	
	/**
	 * sets if the shape shall have a backward arrow
	 * 
	 * @param shallHaveForwardArrow if true, the shape will have
	 * a backward arrow
	 */
	public void setFWArrow(boolean shallHaveForwardArrow) {
		hasForwardArrow = shallHaveForwardArrow;
	}
	/**
	 * sets the start angle of the component
	 * 
	 * @param angle the start angle of the component
	 */
	public void setStartAngle(int angle) {
		startAngle = angle;
	}
	
	/**
	 * sets the total angle of the component, which may also be negative
	 * 
	 * @param angle the angle of the component
	 */
	public void setTotalAngle(int angle) {
		totalAngle = angle;
	}

   // ======================================================================
  // Cloning support
  // ======================================================================

  /**
   * Offers cloning support by cloning or duplicating the shared attributes
   * 
   * @param targetShape the OpenArcBasedShape into which the values 
   * are to be copied. Note the direction -- it is 
   * "currentObject.cloneCommonFeaturesInto(newObject)", not vice versa!
   */
  protected void cloneCommonFeaturesInto(OpenArcBasedShape targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
    // from ArcBasedShape: center
    super.cloneCommonFeaturesInto(targetShape);
    targetShape.setBWArrow(hasBackwardArrow);
    targetShape.setClockwise(isClockwise);
    targetShape.setFWArrow(hasForwardArrow);
    targetShape.setStartAngle(getStartAngle());
    targetShape.setTotalAngle(getTotalAngle());
  }

	/**
	 * Paint the specified shape including possible arrow(s)
	 * 
	 * @param g
	 *          the Graphics object used for painting
	 * @param rx the x radius of the open arc-based shape
	 * @param ry the y radius of the open arc-based shape
	 * @param sa the start angle of the shape
	 * @param ta the total angle of the shape
	 */
	protected void paint(Graphics g, int rx, int ry) {
		if (center == null)
			return;

    // ---------------------------------------------------------
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    // to get the former version change g2 into g and remove the code above
    // under the line

    g2.setColor(color);

    g2.drawArc(center.x - rx, center.y - ry, rx * 2, ry * 2,
				startAngle, totalAngle);

		if (hasFWArrow()) {
			double angle = (startAngle + totalAngle) * Math.PI / 180;
			// the orientation of the arrow depends on whether the arc
			// is clockwise or not.
			drawArrow(g, angle, rx, ry, isClockwise());
		}

		if (hasBWArrow()) {
			double angle = startAngle * Math.PI / 180;
			drawArrow(g, angle, rx, ry, !isClockwise);
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
		// rotating arc-based shapes means: (i) rotate the center of the shape;
		// (ii) change start angle
		PTPoint centerNode = new PTPoint(center);
		centerNode.rotate(angle);
		center = centerNode.toPoint();
		setStartAngle((int) (getStartAngle() + Math.round(angle * 180 / Math.PI)));
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
	
	/**
	 * Update the default properties for this object
	 * @param defaultProperties the properties to be updated
	 */
	public void updateDefaults(XProperties defaultProperties) {
		super.updateDefaults(defaultProperties);
		defaultProperties.put(getType() +".bwArrow", hasBWArrow());
		defaultProperties.put(getType() +".clockwise", isClockwise());
		defaultProperties.put(getType() +".fwArrow", hasFWArrow());
		defaultProperties.put(getType() +".startAngle", getStartAngle());
		defaultProperties.put(getType() +".totalAngle", getTotalAngle());
	}
	
	/**
	 * Prepare this object for use as a move base. This means making it neither
	 * closed nor filled, and possessing only a forward arrow.
	 */
	public void useAsMoveBase() {
    // nothing to be done here
  }


	/**
	 * Return a string describing the object
	 * 
	 * @param typeName the name of the primitive to be used
	 * @param rx the x radius of the open arc-based shape
	 * @param ry the y radius of the open arc-based shape
	 * @return The String representation of the PTArc
	 */
	protected String toString(String typeName, boolean isCircular, int rx, int ry) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(typeName).append(" ");
		if (getObjectName() != null)
			sb.append("\"").append(getObjectName()).append("\" ");
		sb.append("center=(");
		sb.append(center.x).append(", ").append(center.y).append("), radius");
		if (isCircular)
			sb.append("=").append(rx);
		else
			sb.append(" (").append(rx).append(", ").append(ry).append(")");
		sb.append(" starts at ");
		sb.append(getStartAngle()).append(", angle=").append(getTotalAngle());
		if (isClockwise)
			sb.append(" clockwise");
		if (hasForwardArrow)
			sb.append(" fwArrow");
		if (hasBackwardArrow)
			sb.append(" bwArrow");
		return sb.toString();
	}
} // OpenArcBasedShape
