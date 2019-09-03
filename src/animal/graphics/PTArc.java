package animal.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import animal.animator.MoveBase;
import animal.graphics.meta.ArrowablePrimitive;
import animal.graphics.meta.FillablePrimitive;
import animal.graphics.meta.ImmediateTextContainer;
import animal.graphics.meta.OpenArcBasedShape;
import animal.graphics.meta.OrientedPrimitive;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.misc.XProperties;

/**
 * Implements an arc object.
 * 
 * <strong>Important:</strong> "angles" in the arc are not "real" angles of the
 * points relative to the arc's center but the values that have to be used as
 * parameter to the sin/cos-calls when calculating points.
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.2 2008-10-24
 */
public class PTArc extends OpenArcBasedShape 
implements ArrowablePrimitive, FillablePrimitive, ImmediateTextContainer, 
MoveBase, OrientedPrimitive {
	// =====================================================================
	// Public Constants
	// =====================================================================

	/**
	 * the type label for this object, used e.g. for access properties
	 */
	public static final String	TYPE_LABEL				= "Arc";

	/**
	 * the serialization UID for this object
	 */
//	private static final long		serialVersionUID	= 4711724437159551059L;

	// =====================================================================
	// Fields
	// =====================================================================

	/**
	 * The fill color of this arc-based shape. May be null if the shape is not
	 * filled (see the isFilled attribute)
	 * 
	 * @see isFilled
	 */
	private Color							fillColor;

	/**
	 * does the shape have a text component?
	 */
	private boolean						hasTextComponent	= false;

	/**
	 * does the shape have a circular shape, i.e., it is a circle or circle
	 * segment?
	 */
	private boolean						isCircular;

	/**
	 * is the shape closed?
	 */
	private boolean						isClosed;

	/**
	 * determines if the shape is filled.
	 */
	private boolean						isFilled;

	/**
	 * the arc's radius
	 */
	private Point							radius						= new Point(10, 10);

	/**
	 * The text of the component
	 */
	private PTText						textComponent;

	// =================================================================
	// CONSTRUCTORS
	// =================================================================

	/**
	 * Create an empty PTArc instance
	 */
	public PTArc() {
		// initialize attributes to their default values
		initializeWithDefaults(getType());
	}

	// =================================================================
	// GRAPHIC OPERATIONS
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
		PTArc targetShape = new PTArc();

		// clone shared attributes
		// from PTGO: color, depth, num, objectName
		cloneCommonFeaturesInto(targetShape);

		// clone anything else that is specific to this type
		// and its potential subtypes
		return targetShape;
	}
	

	// ======================================================================
	// Cloning support
	// ======================================================================

	/**
	 * Offers cloning support by cloning or duplicating the shared attributes
	 * 
	 * @param targetShape
	 *          the shape into which the values are to be copied. Note the
	 *          direction -- it is
	 *          "currentObject.cloneCommonFeaturesInto(newObject)", not vice
	 *          versa!
	 */
	protected void cloneCommonFeaturesInto(PTArc targetShape) {
		// clone features from PTGraphicsObject: color, depth, num, objectName
		// from ArcBasedShape: center
		// from OpenArcBasedShape: bwArrow, isClockwise, fwArrow, startAngle, totalAngle
		super.cloneCommonFeaturesInto(targetShape);

		// clone anything else that is specific to this type
		// and its potential subtypes
		targetShape.setCircle(isCircular);
		targetShape.setClosed(isClosed);
		targetShape.setFilled(isFilled());
		targetShape.setFillColor(createColor(getFillColor()));
		targetShape.setRadius(new Point(getXRadius(), getYRadius()));
		targetShape.setText(new String(getTextComponent().getText()));
		targetShape.setTextColor(createColor(getTextComponent().getColor()));
	}



	/**
	 * Paint the specified arc including possible arrow(s)
	 * 
	 * @param g
	 *          the Graphics object used for painting
	 */
	public void paint(Graphics g) {
		if (center == null || radius == null)
			return;
		// Color aColor = getColor();, fillColor = getFillColor();
		// // Point center = getCenter();
		// int arcAngle = getArcAngle(), startAngle = getStartAngle();
		int xRadius = getXRadius(), yRadius = getYRadius();
//		g.setColor(color);

//		if (center == null)
//			return;
    // ---------------------------------------------------------
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    // to get the former version change g2 into g and remove the code above
    // under the line

		if (isFilled()) {
      g2.setColor(fillColor);
      g2.fillArc(center.x - xRadius, center.y - yRadius, xRadius * 2,
					yRadius * 2, startAngle, totalAngle);
		}
    g2.setColor(color);
//			if (!fillColor.equals(color)) {
//				g.drawArc(center.x - xRadius, center.y - yRadius, xRadius * 2,
//						yRadius * 2, startAngle, totalAngle);
//				if (totalAngle % 360 != 0) {
//					g.setColor(color);
//					Point a = getPointAtAngle(startAngle);
//					g.drawLine(center.x, center.y, a.x, a.y);
//					a = getPointAtAngle(startAngle + totalAngle);
//					g.drawLine(center.x, center.y, a.x, a.y);
//				}
//			}
//		} else {
//			g.drawArc(center.x - xRadius, center.y - yRadius, xRadius * 2,
//					yRadius * 2, startAngle, totalAngle);
//			if (isClosed() && totalAngle % 360 != 0) {
//				Point a = getPointAtAngle(startAngle);
//				g.drawLine(center.x, center.y, a.x, a.y);
//				a = getPointAtAngle(startAngle + totalAngle);
//				g.drawLine(center.x, center.y, a.x, a.y);
//			}
//		}
		if ((isClosed || isFilled) && totalAngle % 360 != 0) {
			Point a = getPointAtAngle(startAngle);
      g2.drawLine(center.x, center.y, a.x, a.y);
			a = getPointAtAngle(startAngle + totalAngle);
      g2.drawLine(center.x, center.y, a.x, a.y);
		}
//		if (!(isClosed() || isFilled())) {
//			if (hasFWArrow()) {
//				double angle = (startAngle + totalAngle) * Math.PI / 180;
//				// the orientation of the arrow depends on whether the arc
//				// is clockwise or not.
//				drawArrow(g, angle, radius.x, radius.y, isClockwise());
//			}
//			if (hasBWArrow()) {
//				double angle = startAngle * Math.PI / 180;
//				drawArrow(g, angle, radius.x, radius.y, !isClockwise());
//			}
//		}
		// paint forward and backward arrow, as well as arc outline
		super.paint(g, radius.x, radius.y);

		// paint the text component, if any is present
		if (hasTextComponent) {
			getTextComponent().paint(g);
		}
	}


	/**
	 * Prepare this object for use as a move base. This means making it neither
	 * closed nor filled, and possessing only a forward arrow.
	 */
	public void useAsMoveBase() {
		setFilled(false);
		setClosed(false);
		setFWArrow(true);
		setBWArrow(false);
	}

	// =================================================================
	// ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * returns the "angle" relative to the Arc, i.e. the "angle" (i.e. the
	 * parameter that has to be inserted into the sin() and cos() calls) of the
	 * arc's point that is on the same line from the center as <em>p</em>.
	 * Thus,(x*cos(alpha),y*sin(alpha))*dist(point,center) would point from the
	 * center to <em>p</em>, unlike the normal getArcAngle function.
	 * 
	 * @param p the point used for determining the angle
	 * @return angle in degrees: 0 &lt;= angle &lt; 360, -1, if xRadius or yRadius
	 *         is 0.
	 */
	public int getAngle(Point p) {
		return getAngle(p, radius.x, radius.y);
	}

	/**
	 * Return the bounding box for the arc if it had an angle of 360. This is
	 * "required" for dragging the center and the size.(could be done without, but
	 * then the center would be outside the BoundingBox.
	 * 
	 * @return the bounding box of the "whole" arc
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
	 * <li>added "closed" attribute</li>
	 * <li>ASCII I/O</li>
	 * <li>depth</li>
	 * <li>fillColor for arcs</li>
	 * </ol>
	 */
	public int getFileVersion() {
		return 5;
	}

	/**
	 * Returns the fill color of this arc.
	 * 
	 * @return the fill color of this arc
	 */
	public Color getFillColor() {
		if (fillColor == null)
			fillColor = Color.BLACK;
		return fillColor;
	}

	/**
	 * returns the font of the arc's (optional) text component
	 * 
	 * @return the font of the optional text component of this arc
	 */
	public Font getFont() {
		return getTextComponent().getFont();
	}
	
	/**
	 * The length of the arc is only the amount of the arcAngle. Thus the
	 * translation is not "linear", as this would require heavy-duty integrals.
	 * 
	 * @return the length of the arc as the total angle.
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
	public Point getPointAtAngle(int alpha) {
		return getPointAtLength(alpha, radius.x, radius.y);
	}

	/**
	 * returns the point on the arc at "length" from the arc start
	 * 
	 * @param x
	 *          the length to be examined
	 * 
	 * @return the point at the given length
	 */
	public Point getPointAtLength(int x) {
		return getPointAtAngle(getStartAngle() + (isClockwise() ? -1 : 1) * x);
	}

	/**
	 * Returns the "circle"'s radius. If the object is a circle, its real radius
	 * is returned, if it is an ellipse, the average of its two radii.
	 * 
	 * @return the radius of this arc
	 */
	public int getRadius() {
		return (getXRadius() + getYRadius()) / 2;
	}

	/**
	 * Returns the "ellipse"'s radius. If the object is a ellipse, its real radius
	 * is returned, if it is an circle, a Point of value (radius, radius)
	 * 
	 * @return the radius of this arc
	 */
	public Point getRadiusPoint() {
		if (radius == null)
			radius = new Point(10, 10);
		return radius;
	}


	/**
	 * returns the text of the arc's (optional) text component
	 * 
	 * @return the text of the optional text component of this arc
	 */
	public String getText() {
		return getTextComponent().getText();
	}

	/**
	 * Returns the text component of this arc.
	 * 
	 * @return the text component of this arc
	 */
	public PTText getTextComponent() {
		if (textComponent == null) {
			initTextComponent();
		}
		hasTextComponent = true;
		return textComponent;
	}

	/**
	 * returns the type of this object
	 * 
	 * @return the type of the object
	 */
	public String getType() {
		return TYPE_LABEL;
	}

	/**
	 * Returns the x radius of this arc.
	 * 
	 * @return the x radius of this arc
	 */
	public int getXRadius() {
		return getRadiusPoint().x;
	}

	/**
	 * Returns the y radius of this arc.
	 * 
	 * @return the y radius of this arc
	 */
	public int getYRadius() {
		return getRadiusPoint().y;
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "Arc" };
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
		center = new Point(0, 0);
		AnimalConfiguration config = AnimalConfiguration.getDefaultConfiguration();
		isFilled = config.getDefaultBooleanValue(primitiveName, "filled", false);
		fillColor = config.getDefaultColor(primitiveName, "fillColor", Color.BLACK);
	}
	
	/**
	 * initializes the arc's (optional) text component to an empty text
	 */
	public void initTextComponent() {
		textComponent = new PTText(); // perhaps "(getProperties())"
		textComponent.setText("");
		textComponent.setPosition(center); // not quite, but...
	}

	/**
	 * determines whether an angle is "inside" the arc or not.
	 * 
	 * @param angle
	 *          the angle to be examined
	 */
	public boolean isAngleInside(int angle) {
		// int arcAngle = getArcAngle(), startAngle = getStartAngle();
		if (isClockwise()) {// then arcAngle is negative!
			if (-totalAngle < startAngle)
				return angle < startAngle && angle > startAngle + totalAngle;

			return angle < startAngle || angle > startAngle + totalAngle + 360;
		} else if (startAngle + totalAngle < 360)
			return angle > startAngle && angle < startAngle + totalAngle;

		return angle > startAngle || angle < startAngle + totalAngle - 360;
	}

	/**
	 * Query whether this arc is a circular (i.e., x radius == y radius)
	 * 
	 * @return <code>true</code> if the arc is circular, else <code>false</code>
	 */
	public boolean isCircular() {
		return isCircular;
	}

	/**
	 * Returns whether this arc is closed
	 * 
	 * @return <code>true</code> if the arc is closed, else <code>false</code>the
	 *         text component of this arc
	 */
	public boolean isClosed() {
		return isClosed;
	}
	
	/**
	 * Returns whether this arc is filled
	 * 
	 * @return <code>true</code> if the arc is filled, else <code>false</code>
	 */
	public boolean isFilled() {
		return isFilled;
	}

	/**
	 * Set whether this arc is a circle or an ellipse
	 * 
	 * @param b
	 *          if <code>true</code>, the arc is a circle, else an ellipse. Note:
	 *          changing this to "circular" will average the x and y radius, so
	 *          information will be lost if the two radii were different before!
	 */
	public void setCircle(boolean b) {
		isCircular = b;
		if (isCircular)
			setRadius(getRadius());
	}


	/**
	 * Set whether this arc is closed or not
	 * 
	 * @param closed
	 *          if <code>true</code>, the arc is closed.
	 */
	public void setClosed(boolean closed) {
		isClosed = closed;
	}

	/**
	 * Set the arc's fill color
	 * 
	 * @param targetFillColor the fill color of the arc
	 */
	public void setFillColor(Color targetFillColor) {
		fillColor = targetFillColor;
	}

	/**
	 * Set whether this arc is filled or not
	 * 
	 * @param filled
	 *          if <code>true</code>, the arc is filled
	 */
	public void setFilled(boolean filled) {
		isFilled = filled;
	}

	/**
	 * set the font of the arc's (optional) text component
	 * 
	 * @param targetFont the font of the optional text component of this arc
	 */
	public void setFont(Font targetFont) {
		getTextComponent().setFont(targetFont);
	}

	/**
	 * Set the <em>arc</em>'s radius by setting xRadius == yRadius
	 * 
	 * @param r
	 *          the target value for the arc radius shared by x and y radius
	 */
	public void setRadius(int r) {
		setRadius(new Point(r, r));
	}

	/**
	 * Set the <em>arc</em>'s radius
	 * 
	 * @param newRadius
	 *          the target value for the arc radius; if null, nothing will be
	 *          changed
	 */
	public void setRadius(Point newRadius) {
		if (newRadius != null) {
			if (radius == null)
				radius = new Point(newRadius.x, newRadius.y);
			else
				radius.setLocation(newRadius.x, newRadius.y);
		}
	}

	/**
	 * Set the text for the arc
	 * 
	 * @param newText the text of the arc
	 */
	public void setText(String newText) {
		if (newText != null) {
			getTextComponent().setText(newText);
			Font currentFont = textComponent.getFont();
			int width = Animal.getStringWidth(newText, currentFont);
			textComponent.setPosition(center.x - width / 2, center.y
					+ currentFont.getSize() / 2);
		}
	}
	
	/**
	 * Set the arc's text color
	 * 
	 * @param aTextColor
	 *          the color of the text component
	 */
	public void setTextColor(Color aTextColor) {
		getTextComponent().setColor(aTextColor);
	}

	/**
	 * Set the arc's x radius
	 * 
	 * @param r
	 *          the x radius of the arc
	 */
	public void setXRadius(int r) {
		if (radius == null)
			radius = new Point(r, r);
		else
			radius.x = r;
	}

	/**
	 * Set the arc's y radius
	 * 
	 * @param r
	 *          the y radius of the arc
	 */
	public void setYRadius(int r) {
		if (radius == null)
			radius = new Point(r, r);
		else
			radius.x = r;
	}

	/**
	 * Return a string describing the object
	 * 
	 * @return The String representation of the PTArc
	 */
	public String toString() {
		return toString(getType(), isCircular, radius.x, radius.y);
	}
	

	/**
	 * Update the default properties for this object
	 * 
	 * @param defaultProperties
	 *          the properties to be updated
	 */
	public void updateDefaults(XProperties defaultProperties) {
		super.updateDefaults(defaultProperties);
		defaultProperties.put(getType() + ".circle", isCircular());
		defaultProperties.put(getType() + ".closed", isClosed());
		defaultProperties.put(getType() + ".fillColor", fillColor);
		defaultProperties.put(getType() + ".filled", isFilled());
		if (isCircular())
			defaultProperties.put(getType() + ".radius", getRadius());
		else
			defaultProperties.put(getType() + ".radius", getRadiusPoint());
		if (textComponent != null) {
			defaultProperties.put(getType() + ".text", textComponent.getText());
			defaultProperties.put(getType() + ".textColor", textComponent.getColor());
			Font f = textComponent.getFont();
			defaultProperties.put(getType() + ".bold", f.isBold());
			defaultProperties.put(getType() + ".fontName", f.getFamily());
			defaultProperties.put(getType() + ".fontSize", f.getSize());
			defaultProperties.put(getType() + ".italic", f.isItalic());
		}
	}


//  /**
//   * Translate the object so that the upper left corner will be
//   * <code>targetPoint</code>.
//   * 
//   * @param targetPoint
//   *          the target coordinate of the object's upper left corner
//   */
//  public void setLocation(Point targetPoint) {
//    super.setLocation(targetPoint);
//    if (hasTextComponent) {
//      Point originPoint = getBoundingBox().getLocation();
//      int dx = targetPoint.x - originPoint.x;
//      int dy = targetPoint.y - originPoint.y;
//      getTextComponent().translate(dx, dy);
//    }
//  }
  

  /**
   * Implementation of the "translate" operation for the arc
   * 
   * @param x the x vector for the translation
   * @param y the y vector for the translation
   */
  public void translate(int x, int y) {
    center.translate(x, y);
    if (hasTextComponent)
      getTextComponent().translate(x, y);
  }

	
	// =================================================================
	// I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		super.discard();
		fillColor = null;
		radius = null;
		textComponent = null;
	}
} // PTArc
