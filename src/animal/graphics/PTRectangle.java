package animal.graphics;

import java.awt.Dimension;
import java.awt.Point;

import animal.graphics.meta.PolygonalShape;
import animal.misc.XProperties;

/**
 * This class implements a single 2D rectangle using homogeneous coordinates,
 * ie. consisting of a set of x, y and W coordinates.
 * 
 * Based on <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>'s
 * PTPolyline
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 2007-08-29
 */
public class PTRectangle extends PolygonalShape {

  // =====================================================================
  // Public and / or private Constants
  // =====================================================================

  /**
   * the type label for this object, used e.g. for access properties
   */
  public static final String RECTANGLE_TYPE = "Rectangle";

  /**
   * the serialization UID for this object
   */
//  private static final long serialVersionUID = 5793164474737251118L;

  // =====================================================================
  // Fields
  // =====================================================================

  /**
   * the rectangle's height
   */
  private int height;

  /**
   * the rectangle's width
   */
  private int width;

  // ======================================================================
  // Constructors
  // ======================================================================

  /**
   * Create an empty rectangle
   */
  public PTRectangle() {
    this(10, 10, 2, 2);
  }

  /**
   * Create a PTPolyline from an array of x and y coordinates
   * 
   * @param node1
   *          the first node
   * @param node2
   *          the second node (absolute, not relative to node1!)
   */
  public PTRectangle(Point node1, Point node2) {
    this(node1.x, node1.y, node2.x - node1.x, node2.y - node1.y);
  }

  /**
   * Create a PTPolyline from an array of x and y coordinates
   * 
   * @param x
   *          the x coordinate of the rectangle
   * @param y
   *          the y coordinate of the rectangle
   * @param targetWidth
   *          the width of the rectangle
   * @param targetHeight
   *          the height of the rectangle
   */
  public PTRectangle(int x, int y, int targetWidth, int targetHeight) {
    // initialize with default attributes
    initializeWithDefaults(getType());
    fillNodesVector(4);
    width = targetWidth;
    height = targetHeight;
    // update nodes - create if not done yet
    setStartNode(x, y);
  }

  // ======================================================================
  // Attribute accessing
  // ======================================================================
 
  /**
   * returns the box's end point, i.e. the location + size
   * 
   * @return the end point of the box
   */
  public Point getEndNode() {
    PTPoint endPoint = getNodeAt(2);
    if (endPoint == null) {
    	updateShape();
    	endPoint = getNodeAt(2);
    }
    return endPoint.toPoint();
  }

  /**
   * file version history 1: basic format
   */
  public int getFileVersion() {
    return 1;
  }

  /**
   * Used to access the rectangle's height
   * 
   * @returns the rectangle's height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Used to access the rectangle's size as a pair of width and height
   * 
   * @returns the rectangle's size as a Dimension instance
   */
  public Dimension getSize() {
    return new Dimension(width, height);
  }

  /**
   * returns the box's end point, i.e. the location + size
   * 
   * @return the end point of the box
   */
  public Point getStartNode() {
    PTPoint endPoint = getNodeAt(0);
    if (endPoint == null) {
    	return new Point(0, 0);
    }
    return endPoint.toPoint();
  }

  /**
   * Used to access the rectangle's width
   * 
   * @returns the rectangle's width
   */
  public int getWidth() {
    return width;
  }


  /**
   * returns the type of this object as a String
   * 
   * @return this object's type as a String (i.e., "Rectangle")
   */
  public String getType() {
    return PTRectangle.RECTANGLE_TYPE;
  }

  /**
   * Returns the names of the structures this object can parse.
   * 
   * @return an array of Strings containing all handled keywords in the stream
   */
  public String[] handledKeywords() {
    return new String[] { "Rectangle" };
  }

  // ======================================================================
  // Attribute setting
  // ======================================================================

  /**
   * sets the rectangle's height to <em>newHeight</em>
   * 
   * @param newHeight the new height for the rectangle (may also be negative or 0).
   */
  public void setHeight(int newHeight) {
    height = newHeight;
    updateShape(); // update the nodes
  }

  /**
   * sets the rectangle's size to <em>newSize</em>
   * 
   * @param newSize the new size for the rectangle (may also be negative or 0).
   */
  public void setSize(Dimension newSize) {
    if (newSize == null) {
      width = 0;
      height = 0;
    } else {
      width = newSize.width;
      height = newSize.height;
    }
    updateShape(); // update nodes
  }
  
  /**
   * Sets the start node of this rectangle. Updating the start node also
   * affects two other nodes
   * 
   * @param x the new x coordinate of the point of the rectangle
   * @param y the new x coordinate of the point of the rectangle
   */
  public void setStartNode(int x, int y) {
  	nodes.set(0, new PTPoint(x, y)); // overwrite the first node
  	updateShape();
  }
  
  /**
   * Sets the start node of this rectangle. Updating the start node also
   * affects two other nodes
   * 
   * @param p the new start point of the rectangle. Nothing will happen if
   * this is null.
   */
  public void setStartNode(Point p) {
  	if (p != null)
  		setStartNode(p.x, p.y);
  }

  /**
   * sets the rectangle's width to <em>newWidth</em>
   * 
   * @param newWidth the new width for the rectangle (may also be negative or 0).
   */
  public void setWidth(int newWidth) {
    width = newWidth;
    updateShape(); // update the nodes
  }

  /**
   * Update the rectangle, usually because a dimension has changed
   */
  protected void updateShape() {
  	Point node = getNodeAsPoint(0);
  	if (node == null)
  		node = new Point(0, 0);
  	int x = node.x, y = node.y;
  	nodes.set(1, new PTPoint(x + width, y)); // node 2: (x+w, y)
  	nodes.set(2, new PTPoint(x + width, y + height)); // node 3: (x+w, y+h)
  	nodes.set(3, new PTPoint(x, y + height)); // node 4: (x, y+h)
  }

  // ======================================================================
  // Graphical Transformations
  // ======================================================================

  /**
   * <em>scale(double, double)</em> performs a 2D scaling on the given
   * PTPolyline with the given parameters. The formula needed for performing the
   * transformation can be found in the PT slides on page GTR-20 or in Foley/van
   * Dam, page 206, formula 5.17
   * 
   * Non-integer coordinates resulting from a scale operation will be rounded to
   * the nearest integer coordinates.
   * 
   * @param scaleX
   *          The scale factor in x-direction(parallel to the x-axis). This is a
   *          double value to support all kinds of scaling.
   * 
   * @param scaleY
   *          The scale factor in y-direction(parallel to the y-axis). This is a
   *          double value to support all kinds of scaling.
   */
  public void scale(double scaleX, double scaleY) {
  	// scale all nodes
  	super.scale(scaleX, scaleY);
    width *= scaleX; // update width
    height *= scaleY; // update height
  }

  // ======================================================================
  // (Textual) Representation
  // ======================================================================

  /**
   * Generate a String representing this object's state
   * 
   * @return The string representing the current PTPolyline.
   */
  public String toString() {
    // Start String by an object descriptor -- this is good style, since
    // it's next to impossible to tell the instances otherwise.
    StringBuilder result = new StringBuilder(120);
    result.append("PTRectangle ");

    // display name (if any)
    if (getObjectName() != null)
      result.append("\"").append(getObjectName()).append("\" ");
    // add node
    result.append(nodeToString(getNodeAsPoint(0)));

    // add size
    result.append("size (").append(width).append(", ").append(height);
    result.append(")");

    // return the string
    return result.toString();
  }

  // ======================================================================
  // Drawing
  // ======================================================================

  /**
   * Clones the current graphical object. Note that the method will per
   * definition return Object, so the result has to be cast to the appropriate
   * type.
   * 
   * @return a clone of the current object, statically typed as Object.
   */
  public Object clone() {
    // create new object
    PTRectangle rectangle = new PTRectangle();
    // clone shared attributes
    // from PTGO: color, depth, num, objectName
    // from PolygonalShape: fillColor, filled, firstNode
    cloneCommonFeaturesInto(rectangle);
    return rectangle;
  }
  
  /**
   * Offers cloning support by cloning or duplicating the shared attributes
   * 
   * @param targetShape
   *          the PolygonalShape into which the values are to be copied. Note
   *          the direction -- it is
   *          "currentObject.cloneCommonFeaturesInto(newObject)", not vice
   *          versa!
   */
  protected void cloneCommonFeaturesInto(PTRectangle targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
    super.cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential sub-types
    targetShape.setHeight(getHeight());
    targetShape.setWidth(getWidth());
  }


  /**
   * implementing MoveBase, Rectangle must overwrite this method. It returns the
   * total length of the rectangle, being the sum of the lengths of all edges.
   * 
   * @return the length as defined above
   */
  public int getLength() {
    return (width + height) << 1; // four times the Rectangle size...
  }

  /**
   * Update the default properties for this object
   * 
   * @param defaultProperties
   *          the properties to be updated
   */
  public void updateDefaults(XProperties defaultProperties) {
    super.updateDefaults(defaultProperties);
    defaultProperties.put(getType() + ".height", getHeight());
    defaultProperties.put(getType() + ".width", getWidth());
  }
} // PTRectangle
