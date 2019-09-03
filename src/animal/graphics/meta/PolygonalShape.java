/*
 * Created on 18.07.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.graphics.meta;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.Vector;

import animal.graphics.PTPoint;
import animal.main.AnimalConfiguration;
import animal.misc.MSMath;
import animal.misc.XProperties;

/**
 * This abstract class models the common aspects of polygonal shapes
 * such as squares, triangles etc.
 * 
 * @author Dr. Guido R&ouml;&szlig;ling (roessling@acm.org>
 * @version 1.1 2008-10-24
 */
public abstract class PolygonalShape extends LineBasedShape implements
		FillablePrimitive {

	/**
	 * the polygon's color
	 */
	protected Color	fillColor;


	/**
	 * determines if this object is filled (default: false)
	 */
	protected boolean	isFilled	= false;

	// ======================================================================
	// Attribute setting
	// ======================================================================

	/**
	 * returns the object's fill color. If no fill color was defined before, the
	 * fill color is first set to the default fill color for this object and then
	 * returned. Therefore, use isFilled() to query if the object is filled before
	 * accessing this method.
	 * 
	 * @return the object's fill color setting.
	 */
	public Color getFillColor() {
		if (fillColor == null) {
			// FIXME is that OK?
			fillColor = AnimalConfiguration.getDefaultConfiguration()
					.getDefaultColor(getType(), "fillColor", Color.BLACK);
		}
		return fillColor;
	}

  /**
   * implementing MoveBase, an OpenLineBasedShape must overwrite this method. It returns the
   * total length of the polyline, being the sum of the lengths of all edges.
   * 
   * @return the length of this shape
   */
  public int getLength() {
  	int result = super.getLength(); // length for an open shape
    // add the edge from the last to the first node
  	result += MSMath.dist(getNodeAt(getNodeCount() - 1), getNodeAt(0));
    return result;
  }
  
  /**
   * returns the point after having gone along the polyline for <i>length</i>
   * pixels.
   */
  public Point getPointAtLength(int length) {
    int currentLength = length;
    // length is decreased in this method and contains the length
    // that still has to be walked.
    int lineLength; // length of the current edge
    if (nodes == null || nodes.size() < 2)
    	return new Point(0, 0);
    Point a, b = null;
    for (int i = 0; i < getNodeCount() - 1; i++) {
    	// regard current line
    	a = getNodeAt(i).toPoint();
    	b = getNodeAt(i + 1).toPoint();
    	
    	// determine length of current line
      lineLength = MSMath.dist(a, b);
      
      // long enough to have reached the desired target length?
      if (lineLength >= currentLength) {
        // when walking this edge, the object has passed its target,
        // so calculate where it would be
        float percent = 1.0f * currentLength / lineLength;
        // interpolate fitting point
        return new Point((int) (a.x + (b.x - a.x) * percent),
            (int) (a.y + (b.y - a.y) * percent));
      }
      // subtract current lineLength from desired length
      currentLength -= lineLength;
    }
    a = getNodeAsPoint(0);
    if (b != null) {
    	lineLength = MSMath.dist(b, a);
    	// long enough to have reached the desired target length?
    	if (lineLength >= currentLength) {
    		// when walking this edge, the object has passed its target,
    		// so calculate where it would be
    		float percent = 1.0f * currentLength / lineLength;
    		// interpolate fitting point
    		return new Point((int) (b.x + (a.x - b.x) * percent),
    				(int) (b.y + (a.y - b.y) * percent));
    	}
    }
    return getNodeAt(0).toPoint();
  }

	/**
	 * This method initializes the primitive with the primitive type's default
	 * values (looked up at the default properties)
	 * 
	 * @param primitiveName
	 *          the name of the primitive to support inheritance, e.g. "Square".
	 */
	public void initializeWithDefaults(String primitiveName) {
		super.initializeWithDefaults(primitiveName);
		AnimalConfiguration config = AnimalConfiguration.getDefaultConfiguration();
		isFilled = config.getDefaultBooleanValue(primitiveName, "filled", false);
		fillColor = config.getDefaultColor(primitiveName, "fillColor", Color.BLACK);
	}

	/**
	 * returns whether the object is filled.
	 * 
	 * @return true if the object is filled, else false.
	 */
	public boolean isFilled() {
		return isFilled;
	}

	/**
	 * sets the object's fill color
	 * 
	 * @param c
	 *          the target fill color to use.
	 */
	public void setFillColor(Color c) {
		if (c != null)
			fillColor = c;
		else
			fillColor = Color.BLACK;
	}

	/**
	 * Sets whether the object is filled or not
	 * 
	 * @param filled
	 *          if true, the object is filled (by the fill color), else it is
	 *          unfilled.
	 */
	public void setFilled(boolean filled) {
		isFilled = filled;
	}

  // ======================================================================
  // (Textual) Representation
  // ======================================================================

  /**
   * Generate a String representing this object's state
   * 
   * @return The string representing the current PTTriangle.
   */
  protected String toString(String objectName) {
    // Start String by an object descriptor -- this is good style, since
    // it's next to impossible to tell the instances otherwise.
    StringBuilder result = new StringBuilder(120);
    result.append(objectName).append(' ');
    if (getObjectName() != null)
      result.append("\"").append(getObjectName()).append("\" ");

    for (PTPoint node: nodes) {
    	if (node != null)
    		result.append(nodeToString(node.toPoint()));
    }
    
    // return the string
    return result.toString();
  }

  /**
   * Converts the square into a move base by turning off the fill color (which
   * would only distract).
   */
  public void useAsMoveBase() {
    setFilled(false);
  }

  //======================================================================
	// Graphical operations
	// ======================================================================
  /**
   * This routine draws the PolygonalShape on the Graphics object passed as a
   * parameter. Drawing is simply done by converting the shape into a
   * java.awt.Polygon and using the <em>fillPolygon/drawPolygon</em> methods
   * provided in java.awt.Graphics.
   * 
   * @param g
   *          The platform-specific Graphics object used for all graphic
   *          operations.
   */
  public void paint(Graphics g) {
  	// convert the shape into a polygon
  	Polygon poly = toPolygon();

    // ---------------------------------------------------------
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    // to get the former version change g2 into g and remove the code above
    // under the line

  	// if filled, draw a filled polygon
  	if (isFilled()) {
      g2.setColor(getFillColor());
      g2.fillPolygon(poly);
  	}
  	
  	// now draw the polygon
    g2.setColor(getColor());
    g2.drawPolygon(poly);
  } // paint
    
	// ======================================================================
	// Cloning support
	// ======================================================================

	/**
	 * Update the default properties for this object
	 * 
	 * @param defaultProperties
	 *          the properties to be updated
	 */
	public void updateDefaults(XProperties defaultProperties) {
		super.updateDefaults(defaultProperties);
		defaultProperties.put(getType() + ".fillColor", getFillColor());
		defaultProperties.put(getType() + ".filled", isFilled());
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
	protected void cloneCommonFeaturesInto(PolygonalShape targetShape) {
		// clone features from PTGraphicsObject: color, depth, num, objectName
		super.cloneCommonFeaturesInto(targetShape);
		targetShape.setFilled(isFilled());
		targetShape.setFillColor(createColor(getFillColor()));
		if (nodes != null) {
			targetShape.nodes = new Vector<PTPoint>(nodes.size());
			for (PTPoint node: nodes)
				targetShape.nodes.add((PTPoint)node.clone());
		}
	}

	/**
	 * free the links to subobjects to make garbage collection easier
	 */
	public void discard() {
		super.discard();
		fillColor = null;
	}
}
