package animal.graphics;

import java.awt.Point;
import java.awt.Rectangle;

import animal.graphics.meta.PolygonalShape;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

/**
 * This class implements a single 2D square using homogeneous coordinates, ie.
 * consisting of a set of x, y and W coordinates.
 * 
 * Based on <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>s
 * PTSquare
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 2007-07-11
 */
public class PTSquare extends PolygonalShape {

  // =====================================================================
  // Public Constants
  // =====================================================================

  /**
   * the type label for this object, used e.g. for access properties
   */
  public static final String SQUARE_TYPE = "Square";

  /**
   * the serialization UID for this object
   */
//  private static final long serialVersionUID = 5793164474737251118L;

  // =====================================================================
  // Fields
  // =====================================================================

  /**
   * the size of a square is a single int
   */
  private int size = 0;

  // ======================================================================
  // Constructors
  // ======================================================================

  /**
   * Create an empty PTSquare
   */
  public PTSquare() {
    // initialize attributes to their default values
    this(0, 0, 5);
  }

  /**
   * Create a PTSquare from an array of x and y coordinates
   * 
   * @param node
   *          the square's first coordinate, <em>must not be null</em>
   * @param squareSize
   *          the size of the square
   */
  public PTSquare(Point node, int squareSize) {
    this(node.x, node.y, squareSize);
  }

  /**
   * Create a PTSquare from a x and y coordinate
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public PTSquare(int x, int y, int squareSize) {
    initializeWithDefaults(getType());
    fillNodesVector(4);
    size = squareSize;
    setSquareNode(x, y);
  }

	// ======================================================================
  // Attribute accessing
  // ======================================================================

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
   * Used to access the second node
   */
  public int getSize() {
    return size;
  }

  /**
   * returns the square node
   * 
   * @return the node defining the location of this square
   */
  public PTPoint getSquareNode() {
  	return getNodeAt(0);
  }

  /**
   * returns the square node as a java.awt.Point
   * 
   * @return the node defining the location of this square as a java.awt.Point
   */
  public Point getSquareNodeAsPoint() {
  	PTPoint node = getNodeAt(0);
  	if (node != null)
  		return node.toPoint();
  	return new Point(0, 0);
  }

  /**
   * returns the type of this object as a String
   * 
   * @return this object's type as a String (i.e., "Square")
   */
  public String getType() {
    return PTSquare.SQUARE_TYPE;
  }

  /**
   * Returns the names of the structures this object can parse.
   * 
   * @return an array of Strings containing all handled keywords in the stream
   */
  public String[] handledKeywords() {
    return new String[] { "Square" };
  }

  // ======================================================================
  // Attribute setting
  // ======================================================================

  /**
   * sets the square's size to <em>newSize</em>
   * 
   * @param newSize the new size for the square (may also be negative or 0).
   */
  public void setSize(int newSize) {
    size = newSize;
		updateShape(); // update the square
  }
 
  /**
   * Sets the node of the square to p
   * 
   * @param x the x coordinate of this square
   * @param y the y coordinate of this square
   */
  public void setSquareNode(int x, int y) {
		nodes.clear();
		nodes.setSize(4);
		nodes.set(0, new PTPoint(x, y)); // first node: at (x, y)
		updateShape(); // update the square
  }
  
  /**
   * Sets the node of the square to p
   * 
   * @param p the new node of this square, which should not be null
   */
  public void setSquareNode(Point p) {
  	if (p == null)
  		return;
  	setSquareNode(p.x, p.y);
  }
  
  /**
   * Sets the node of the square to p
   * 
   * @param p the new node of this square, which should not be null
   */
  public void setSquareNode(PTPoint p) {
  	if (p == null)
  		return;
  	setSquareNode(p.getX(), p.getY());
  }
  
  /**
   * Update the square, usually because the start node or size has changed
   */
  protected void updateShape() {
		// assign square nodes
  	Point node = getSquareNodeAsPoint();
  	int x = node.x, y = node.y;
		nodes.set(1, new PTPoint(x + size, y)); // second node: (x+size, y)
		nodes.set(2, new PTPoint(x + size, y + size)); // third node: (x+size, y+size)
		nodes.set(3, new PTPoint(x, y + size)); // fourth node: (x, y+size)
  }

  // ======================================================================
  // Graphical Transformations
  // ======================================================================

  /**
   * <em>scale(double, double)</em> performs a 2D scaling on the given
   * PTSquare with the given parameters. The formula needed for performing the
   * transformation can be found in Foley/van Dam, page 206, formula 5.17
   * 
   * Non-integer coordinates resulting from a scale operation will be rounded to
   * the nearest integer coordinates.
   * 
   * @param scaleX
   *          The scale factor in x direction (parallel to the x-axis). This is
   *          a double value to support all kinds of scaling.
   * 
   * @param scaleY
   *          The scale factor in y direction (parallel to the y-axis). This is
   *          a double value to support all kinds of scaling.
   */
  public void scale(double scaleX, double scaleY) {
    if (scaleX != scaleY)
    	MessageDisplay.errorMsg("invalidScaleFactor", new Double[] {
          Double.valueOf(scaleX), Double.valueOf(scaleY) },
          MessageDisplay.RUN_ERROR);
    else {
    	super.scale(scaleX, scaleY);
    	size *= scaleX;
    }
  }

  // ======================================================================
  // (Textual) Representation
  // ======================================================================

  /**
   * Generate a String representing this object's state
   * 
   * @return The string representing the current PTSquare.
   */
  public String toString() {
    // Start String by an object descriptor -- this is good style, since
    // it's next to impossible to tell the instances otherwise.
    StringBuilder result = new StringBuilder(120);
    result.append("PTSquare ");

    // add name
    String name = getObjectName();
    if (name != null)
      result.append("\"").append(name).append("\" ");
    else
      result.append("\"").append(getNum(false)).append("\" ");

    // add node
    result.append(nodeToString(getSquareNodeAsPoint()));

    // append size
    result.append("size ").append(size);

    // return the string
    return result.toString();
  }


  // ======================================================================
  // Cloning support
  // ======================================================================

  /**
   * Clones the current graphical object. Note that the method will per
   * definition return Object, so the result has to be cast to the appropriate
   * type.
   * 
   * @return a clone of the current object, statically typed as Object.
   */
  public Object clone() {
    // Point node = getFirstNode();
    // if (node == null)
    // node = new Point(0, 0);
    // PTSquare p = new PTSquare(node.x, node.y, getSize());
    // create new object
    PTSquare square = new PTSquare();
    // clone shared attributes
    // from PTGO: color, depth, num, objectName
    // from PolygonalShape: fillColor, filled, firstNode
    cloneCommonFeaturesInto(square);
    return square;
  }

  /**
   * Offers cloning support by cloning or duplicating the shared attributes
   * 
   * @param targetShape
   *          the shape into which the values are to be copied. Note
   *          the direction -- it is
   *          "currentObject.cloneCommonFeaturesInto(newObject)", not vice
   *          versa!
   */
  protected void cloneCommonFeaturesInto(PTSquare targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
    super.cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    targetShape.setSize(size);
  }

  /**
   * implementing MoveBase, Polyline must overwrite this method. It returns the
   * total length of the polyline, being the sum of the lengths of all edges.
   * 
   * @return the length of the square (4 times the square size)
   */
  public int getLength() {
    return size * 4; // four times the square size...
  }

  /**
   * returns the point after having gone along the polyline for <em>length</em>
   * pixels.
   * 
   * @return the point at the given length
   */
  public Point getPointAtLength(int length) {
    // length is decreased in this method and contains the length
    // that still has to be walked.
    int remainingLength = length; // length of the current edge
    int nrSide = 0;

    while (remainingLength > size) {
      remainingLength -= size;
      nrSide++;
    }
    nrSide = nrSide % 4;
    Point node = getSquareNodeAsPoint();
    if (node == null)
      node = new Point(0, 0);
    switch (nrSide) {
    case 1:
      return new Point(node.x + size, node.y + remainingLength);
    case 2:
      return new Point(node.x + size - remainingLength, node.y);
    case 3:
      return new Point(node.x, node.y + size - remainingLength);
    default:
      return new Point(node.x + remainingLength, node.y);
    }
  }

  /**
   * returns the square's bounding box
   * 
   * @return the square's bounding box
   */
  public Rectangle getBoundingBox() {
    return new Rectangle(getSquareNodeAsPoint().x, getSquareNodeAsPoint().y,
    		size, size);
  }
  
  /**
   * Update the default properties for this object
   * 
   * @param defaultProperties
   *          the properties to be updated
   */
  public void updateDefaults(XProperties defaultProperties) {
    super.updateDefaults(defaultProperties);
    defaultProperties.put(getType() + ".size", getSize());
  }
} // PTSquare
