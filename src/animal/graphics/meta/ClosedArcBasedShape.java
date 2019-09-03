/*
 * Created on 01.08.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.graphics.meta;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import animal.main.AnimalConfiguration;
import animal.misc.XProperties;

/**
 * This abstract class models a closed arc-based shape used for
 * objects such as ellipses and circles.
 *
 * @author Guido Roessling (roessling@acm.org)
 * @version 1.1 2008-10-24
 */
public abstract class ClosedArcBasedShape extends ArcBasedShape
implements FillablePrimitive {	

	/**
	 * The fill color of this arc-based shape. May be null if the
	 * shape is not filled (see the isFilled attribute)
	 * 
	 * @see isFilled
	 */
	protected Color fillColor;
	
	/**
	 * determines if the shape is filled.
	 */
	protected boolean isFilled;
	
	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		super.discard();
		fillColor = null;
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
		isFilled = config.getDefaultBooleanValue(primitiveName, 
				"filled", false);
		fillColor = config.getDefaultColor(primitiveName, "fillColor",
				Color.BLACK);
	}
	
  // ======================================================================
  // Cloning support
  // ======================================================================

  /**
   * Offers cloning support by cloning or duplicating the shared attributes
   * 
   * @param targetShape the ClosedArcBasedShape into which the values 
   * are to be copied. Note the direction -- it is 
   * "currentObject.cloneCommonFeaturesInto(newObject)", not vice versa!
   */
  protected void cloneCommonFeaturesInto(ClosedArcBasedShape targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
    // from ArcBasedShape: center
    super.cloneCommonFeaturesInto(targetShape);
    targetShape.setFilled(isFilled());
    targetShape.setFillColor(createColor(getFillColor()));
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
	 * Returns whether this arc is filled
	 * 
	 * @return <code>true</code> if the arc is filled, else <code>false</code>
	 */
	public boolean isFilled() {
		return isFilled;
	}
	
	/**
	 * Paint the specified circle including possible arrow(s)
	 * 
	 * @param g
	 *          the Graphics object used for painting
	 * @param rx the x radius of the closed arc-based shape
	 * @param ry the y radius of the closed arc-based shape
	 * @param sa the start angle of the paint operation
	 * @param ta the total angle of the paint operation         
	 */

  // added Antialiasing 19.4.18 Marian Hieke
	protected void paint(Graphics g, int rx, int ry, int sa, int ta) {
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

		if (isFilled()) {
      g2.setColor(fillColor);
      g2.fillArc(center.x - rx, center.y - ry, rx * 2,
					ry * 2, sa, ta);
		}
    g2.setColor(color);
    g2.drawArc(center.x - rx, center.y - ry, rx * 2, ry * 2,
				sa, ta);
	}
	
	/**
	 * Set whether this arc is filled or not
	 * 
	 * @param filled if <code>true</code>, the arc is filled
	 */
	public void setFilled(boolean filled) {
		isFilled = filled;
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
	 * Return a string describing the object
	 * 
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
		return sb.toString();
	}
	
	/**
	 * Update the default properties for this object
	 * @param defaultProperties the properties to be updated
	 */
	public void updateDefaults(XProperties defaultProperties) {
		super.updateDefaults(defaultProperties);
		defaultProperties.put(getType() +".filled", isFilled());
		defaultProperties.put(getType() +".fillColor", getFillColor());
	}

	/**
	 * Prepare this object for use as a move base. This means making it neither
	 * closed nor filled, and possessing only a forward arrow.
	 */
	public void useAsMoveBase() {
		setFilled(false);
	}
} // ClosedArcBasedShape
