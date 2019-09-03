package animal.graphics;

import java.awt.Point;

import animal.graphics.meta.PolygonalShape;
import animal.misc.XProperties;

/**
 * This class implements a single 2D triangle using homogeneous coordinates, ie.
 * consisting of a set of x, y and W coordinates.
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 2007-07-18
 */
public class PTTriangle extends PolygonalShape {

  // =====================================================================
  // Public and private constants
  // =====================================================================

  /**
   * the type label for this object, used e.g. for access properties
   */
  public static final String TRIANGLE_TYPE = "Triangle";

  /**
   * the serialization UID for this object
   */
//  private static final long serialVersionUID = 5793183274737251118L;

  // ======================================================================
  // Constructors
  // ======================================================================

  /**
   * Create an empty triangle
   */
  public PTTriangle() {
    this(4, 2, 0, 0, 0, 0);
  }

  /**
   * Create a PTTriangle from an array of x and y coordinates
   * 
   * @param x1
   *          the x coordinate of the triangle's first node
   * @param y1
   *          the y coordinate of the triangle's first node
   * @param x2
   *          the x coordinate of the triangle's second node
   * @param y2
   *          the y coordinate of the triangle's second node
   * @param x3
   *          the x coordinate of the triangle's third node
   * @param y3
   *          the y coordinate of the triangle's third node
   */
  public PTTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
    this(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3));
  }

  /**
   * Create a PTTriangle from an array of x and y coordinates
   * 
   * @param first
   *          the first node of the triangle
   * @param second
   *          the second node of the triangle
   * @param third
   *          the third node of the triangle
   */
  public PTTriangle(Point first, Point second, Point third) {
    initializeWithDefaults(getType());
    fillNodesVector(3);
    setNode(0, first);
    setNode(1, second);
    setNode(2, third);
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
   * Used to access the first node
   * 
   * @return the first node of the triangle
   */
  public Point getFirstNode() {
    return getNodeAsPoint(0);
  }

  /**
   * Used to access the second node
   * 
   * @return the second node of the triangle
   */
  public Point getSecondNode() {
    return getNodeAsPoint(1);
  }

  /**
   * Used to access the third node
   * 
   * @return the third node of the triangle
   */
  public Point getThirdNode() {
    return getNodeAsPoint(2);
  }

  /**
   * returns the type of this object as a String
   * 
   * @return this object's type as a String (i.e., "Triangle")
   */
  public String getType() {
    return PTTriangle.TRIANGLE_TYPE;
  }

  /**
   * Returns the names of the structures this object can parse.
   * 
   * @return an array of Strings containing all handled keywords in the stream
   */
  public String[] handledKeywords() {
    return new String[] { "Triangle" };
  }

  // ======================================================================
  // Attribute setting
  // ======================================================================

  /**
   * Sets the object's first node
   * 
   * @param x
   *          the x coordinate of the node
   * @param y
   *          the y coordinate of the node
   */
  public void setFirstNode(int x, int y) {
    setNode(0, x, y);
  }

  /**
   * Sets the object's first node
   * 
   * @param node the node to be used
   */
  public void setFirstNode(Point node) {
    setNode(0, node);
  }
  
  /**
   * Sets the object's second node
   * 
   * @param x
   *          the x coordinate of the node
   * @param y
   *          the y coordinate of the node
   */
  public void setSecondNode(int x, int y) {
    setNode(1, x, y);
  }

  /**
   * Sets the object's second node
   * 
   * @param node the node to be used for the second node
   */
  public void setSecondNode(Point node) {
    setNode(1, node);
  }

  /**
   * Sets the object's third node
   * 
   * @param x
   *          the x coordinate of the node
   * @param y
   *          the y coordinate of the node
   */
  public void setThirdNode(int x, int y) {
    setNode(2, x, y);
  }

  /**
   * Sets the object's third node
   * 
   * @param node the third node
   */
  public void setThirdNode(Point node) {
  	setNode(2, node);
  }

  // ======================================================================
  // (Textual) Representation
  // ======================================================================

  /**
   * Generate a String representing this object's state
   * 
   * @return The string representing the current PTTriangle.
   */
  public String toString() {
    return toString(getType());
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
    PTTriangle triangle = new PTTriangle();
    // clone shared attributes
    // from PTGO: color, depth, num, objectName
    // from PolygonalShape: fillColor, filled, firstNode
    cloneCommonFeaturesInto(triangle);
    return triangle;
  }

//  /**
//   * Offers cloning support by cloning or duplicating the shared attributes
//   * 
//   * @param targetShape
//   *          the shape into which the values are to be copied. Note the
//   *          direction -- it is
//   *          "currentObject.cloneCommonFeaturesInto(newObject)", not vice
//   *          versa!
//   */
//  protected void cloneCommonFeaturesInto(PTTriangle targetShape) {
//    // clone features from PTGraphicsObject: color, depth, num, objectName
//    super.cloneCommonFeaturesInto(targetShape);
//
//    // clone anything else that is specific to this type
//    // and its potential sub-types
//    targetShape.setFirstNode(getFirstNode());
//    targetShape.setSecondNode(getSecondNode());
//    targetShape.setThirdNode(getThirdNode());
//  }

  /**
   * A Polyline that is to be used as a MoveBase must not be closed (then the
   * object would finally be where it started moving) nor have a backward arrow
   * but must have a forward arrow to indicate in what direction the object will
   * be moved.
   */
  public void useAsMoveBase() {
    // nothing to be done here
  }


  /**
   * Update the default properties for this object
   * 
   * @param defaultProperties
   *          the properties to be updated
   */
  public void updateDefaults(XProperties defaultProperties) {
    super.updateDefaults(defaultProperties);
    defaultProperties.put(getType() + ".node2", getSecondNode());
    defaultProperties.put(getType() + ".node3", getThirdNode());
  }
} // PTTriangle
