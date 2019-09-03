/*
 * Created on 01.08.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.graphics.meta;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;

import translator.AnimalTranslator;
import animal.graphics.PTPoint;
import animal.main.AnimalConfiguration;
import animal.misc.MSMath;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

/**
 * This abstract class models a closed arc-based shape used for objects such as
 * ellipses and circles.
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 1.1 31.10.2008
 */
public abstract class OpenLineBasedShape extends LineBasedShape 
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
   * Draws an arrow. The arrow is the bigger(up to a limit) the more distant
   * <i>tip</i> and <i>tail</i> are. This is a "closed arrow with indented
   * butt"(XFig)
   * 
   * @param g the Graphics to draw into
   */
  public void drawArrows(Graphics g) {
  	if (getNodeCount() < 2)
  		return;
    if (hasForwardArrow && getLastNode() != null)
    	OpenLineBasedShape.drawArrow(g, getLastNode().toPoint(),
    			getNodeAsPoint(getNodeCount() - 2));
    if (hasBackwardArrow && getFirstNode() != null)
    	OpenLineBasedShape.drawArrow(g, getFirstNode().toPoint(),
    			getNodeAsPoint(1));
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
  public static void drawArrow(Graphics g, PTPoint tip, PTPoint tail) {
  	if (tip != null && tail != null)
  		drawArrow(g, tip.toPoint(), tail.toPoint());
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

  // added Antialiasing 19.4.18 Marian Hieke
  public static void drawArrow(Graphics g, Point tip, Point tail) {
    if (tip == null || tail == null) {
      MessageDisplay.errorMsg(AnimalTranslator.translateMessage(
          "internalError",
          new String[] { "tip / tail null", "PTLine.drawArrow" }),
          MessageDisplay.PROGRAM_ERROR);
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
   * Returns the first node in the Vector of nodes
   * 
   * @return the first node of this structure, if any; else null
   */
  public PTPoint getFirstNode() {
  	return getNodeAt(0);
  }
  
  /**
   * Returns the last node in the Vector of nodes
   * 
   * @return the last node of this structure, if any; else null
   */
  public PTPoint getLastNode() {
  	return getNodeAt(getNodeCount() - 1);
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
   * This method initializes the primitive with the primitive type's default
   * values (looked up at the default properties)
   * 
   * @param primitiveName
   *          the name of the primitive to support inheritance, e.g. "Circle".
   */
  public void initializeWithDefaults(String primitiveName) {
    super.initializeWithDefaults(primitiveName);
    AnimalConfiguration config = AnimalConfiguration.getDefaultConfiguration();
    hasBackwardArrow = config.getDefaultBooleanValue(primitiveName, "bwArrow",
        false);
    hasForwardArrow = config.getDefaultBooleanValue(primitiveName, "fwArrow",
        false);
  }

  /**
   * sets if the shape shall have a backward arrow
   * 
   * @param shallHaveBackwardArrow
   *          if true, the shape will have a backward arrow
   */

  public void setBWArrow(boolean shallHaveBackwardArrow) {
    hasBackwardArrow = shallHaveBackwardArrow;
  }

  /**
   * Sets the first node in the Vector of nodes
   * 
   * @param x the x value of the new first node
   * @param y the y value of the new first node
   */
  public void setFirstNode(int x, int y) {
  	setNode(0, x, y);
  }
  
  /**
   * Sets the first node in the Vector of nodes
   * 
   * @param p the value of the new first node. Note that the 
   * value will be copied, not simply referenced.
   */
  public void setFirstNode(Point p) {
  	setNode(0, p);
  }

  /**
   * Sets the first node in the Vector of nodes
   * 
   * @param p the value of the new first node. Note that the 
   * value will be copied, not simply referenced.
   */
  public void setFirstNode(PTPoint p) {
  	if (p != null)
  		setFirstNode(p.getX(), p.getY());
  }

  /**
   * Overwrites the last node in the Vector of nodes
   * 
   * @param x the x value of the new last node
   * @param y the y value of the new last node
   */
  public void setLastNode(int x, int y) {
  	nodes.set(getNodeCount() - 1, new PTPoint(x, y));
  }
  
  /**
   * Overwrites the last node in the Vector of nodes
   * 
   * @param p the value of the new last node. Note that the 
   * value will be copied, not simply referenced.
   */
  public void setLastNode(Point p) {
  	if (p != null)
  		setLastNode(p.x, p.y);
  }

  /**
   * Overwrites the last node in the Vector of nodes
   * 
   * @param p the value of the new last node. Note that the 
   * value will be copied, not simply referenced.
   */
  public void setLastNode(PTPoint p) {
  	if (p != null)
  		setLastNode(p.getX(), p.getY());
  }

  
  /**
   * sets if the shape shall have a backward arrow
   * 
   * @param shallHaveForwardArrow
   *          if true, the shape will have a backward arrow
   */
  public void setFWArrow(boolean shallHaveForwardArrow) {
    hasForwardArrow = shallHaveForwardArrow;
  }
  
  /**
   * Generate a String representing this object's state
   * 
   * @return The string representing the current PTPolyline.
   */
  protected String toString(String objectName) {
    // Start String by an object descriptor -- this is good style, since
    // it's next to impossible to tell the instances otherwise.
    StringBuilder result = new StringBuilder(256);
    result.append(objectName).append(" ");
    if (getObjectName() != null)
      result.append("\"").append(getObjectName()).append("\" ");
    for (int i = 0, nrNodes = getNodeCount(); i < nrNodes; i++) {
    	PTPoint currentNode = getNodeAt(i);
    	result.append("(").append(currentNode.getX()).append(", ");
    	result.append(currentNode.getY()).append(")");
    	if (i < nrNodes - 1)
    		result.append(", ");
    }

    // return the string
    return result.toString();
  }

  /**
   * Update the default properties for this object
   * 
   * @param defaultProperties
   *          the properties to be updated
   */
  public void updateDefaults(XProperties defaultProperties) {
    super.updateDefaults(defaultProperties);
    defaultProperties.put(getType() + ".bwArrow", hasBWArrow());
    defaultProperties.put(getType() + ".fwArrow", hasFWArrow());
  }
  
  /**
   * An open line-based shape that is to be used as a MoveBase must not be closed (then the
   * object would finally be where it started moving) nor have a backward arrow
   * but must have a forward arrow to indicate in what direction the object will
   * be moved.
   */
  public void useAsMoveBase() {
    setFWArrow(true);
    setBWArrow(false);
  }

  // ======================================================================
  // Graphical Transforms
  // ======================================================================

  /**
   * This routine draws the PTPolyline on the Graphics object passed as a
   * parameter. Drawing is simply done by drawing a line between adjacent
   * points, as well as between the last and the first point.
   * 
   * @param g
   *          The platform-specific Graphics object used for all graphic
   *          operations.
   */

  // added Antialiasing for the lines 19.4.18 Marian Hieke

  public void paint(Graphics g) {
    Polygon poly = toPolygon();
    // ---------------------------------------------------------
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    // to get the former version change g2 into g and remove the code above
    // under the line
    g2.setColor(getColor());
    g2.drawPolyline(poly.xpoints, poly.ypoints, poly.xpoints.length);
    

    int nrNodes = getNodeCount();
    if (hasForwardArrow)
    	OpenLineBasedShape.drawArrow(g, getNodeAsPoint(nrNodes - 1), 
    			getNodeAsPoint(nrNodes - 2));
    
    if (hasBackwardArrow)
    	OpenLineBasedShape.drawArrow(g, getNodeAsPoint(0), getNodeAsPoint(1));
  } // paint

  	/**
	 * <em>scale(double, double)</em> performs a 2D scaling on the given shape with
	 * the given parameters. The formula needed for performing the transformation
	 * can be found in the PT slides on page GTR-20 or in Foley/van Dam, page 206,
	 * formula 5.17
	 * 
	 * Non-integer coordinates resulting from a scale operation will be rounded to
	 * the nearest integer coordinates.
	 * 
	 * @param scaleX
	 *            The scale factor in x-direction(parallel to the x-axis). This is a
	 *            double value to support all kinds of scaling.
	 * 
	 * @param scaleY
	 *            The scale factor in y-direction(parallel to the y-axis). This is a
	 *            double value to support all kinds of scaling.
	 */
  public void scale(double scaleX, double scaleY) {
    // maxIndex is used to avoid unnecessary multiple accesses to nodes;
    // node_nr is used to access the current node
    int maxIndex = getNodeCount(), nodeNr;

    // Iterate on all nodes, telling each to scale itself
    for (nodeNr = 0; nodeNr < maxIndex; nodeNr++)
      if (nodes.elementAt(nodeNr) != null)
        nodes.elementAt(nodeNr).scale(scaleX, scaleY);

    // All points are scaled => the shape is scaled.
  }

  	/**
	 * <em>shear(double, double)</em> performs a 2D shearing on the given shape,
	 * using the parameters as the shearing factors.
	 * 
	 * 
	 * Use <em>shear(shearX, 0)</em> for x-axis shearing and <em>shear(0,
	 * shearY)</em> for y-axis shearing.
	 * 
	 * <strong>Caution:</strong> Using <em>shear(shearX, shearY)</em> with shearX,
	 * shearY != 0 results in a shearing that cannot be undone by other shearing
	 * operations.
	 * 
	 * The formula needed for performing the transformation can be found in the PT
	 * slides on page GTR-20 or in Foley/van Dam, page 206, formula 5.17
	 * 
	 * Non-integer coordinates resulting from a shear operation will be rounded to
	 * the nearest integer coordinates.
	 * 
	 * @param shearX
	 *            The shear factor in x-direction. This is a double value to support
	 *            all kinds of shearing.
	 * 
	 * @param shearY
	 *            The shear factor in y-direction. This is a double value to support
	 *            all kinds of shearing.
	 */
  public void shear(double shearX, double shearY) {
    // maxIndex is used to avoid unnecessary multiple accesses to nodes;
    // node_nr is used to access the current node
    int maxIndex = getNodeCount(), nodeNr;

    // Iterate on all nodes, telling each to shear itself
    for (nodeNr = 0; nodeNr < maxIndex; nodeNr++)
      if (nodes.elementAt(nodeNr) != null)
        nodes.elementAt(nodeNr).shear(shearX, shearY);

    // All points are sheared => the shape is sheared.
  }

  /**
   * <em>translate(int, int, int)</em> performs a 2D translation on the given
   * node of the open line-based shape. The formula needed for performing
   * the transformation can be found in the PT slides on page GTR-19 or
   * in Foley/van Dam, page 205, formula 5.12
   * 
   * @param deltaX
   *          The translation coefficient for the x-direction. Since we only use
   *          integer coordinates, this is an integer, too.
   * 
   * @param deltaY
   *          The translation coefficient for the y-direction. Since we only use
   *          integer coordinates, this is an integer, too.
   */
  public void translate(int aNum, int deltaX, int deltaY) {
    nodes.elementAt(aNum).translate(deltaX, deltaY);
  }


  // ======================================================================
  // Cloning support
  // ======================================================================

  /**
   * Offers cloning support by cloning or duplicating the shared attributes
   * 
   * @param targetShape
   *          the OpenArcBasedShape into which the values are to be copied. Note
   *          the direction -- it is
   *          "currentObject.cloneCommonFeaturesInto(newObject)", not vice
   *          versa!
   */
  protected void cloneCommonFeaturesInto(OpenLineBasedShape targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
    super.cloneCommonFeaturesInto(targetShape);

    targetShape.setFWArrow(hasForwardArrow);
    targetShape.setBWArrow(hasBackwardArrow);
  }
} // OpenLineBasedShape
