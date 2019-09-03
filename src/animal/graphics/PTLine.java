package animal.graphics;

import java.awt.Point;

import animal.graphics.meta.OpenLineBasedShape;
import animal.misc.XProperties;

/**
 * This class implements a simple line Polygon using homogenous coordinates, ie.
 * consisting of a set of x, y and W coordinates.
 * 
 * Based on <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>'s
 * PTPolyline
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 2007-10-05
 */
public class PTLine extends OpenLineBasedShape {

  // =====================================================================
  // Public Constants
  // =====================================================================

  /**
   * the type label for this object, used e.g. for access properties
   */
  public static final String LINE_TYPE = "Line";

  /**
   * the serialization UID for this object
   */
//  private static final long serialVersionUID = 5293164474737251118L;

  // ======================================================================
  // Constructors
  // ======================================================================

  /**
   * Create an empty PTPolyline
   */
  public PTLine() {
    // initialize attributes to their default values
    initializeWithDefaults(getType());
    fillNodesVector(2);
  }

  /**
   * creates a line consisting of the set of coordiantes passed in
   */
  public PTLine(int x1, int y1, int x2, int y2) {
    this();
    nodes.set(0, new PTPoint(x1, y1));
    nodes.set(1, new PTPoint(x2, y2));
  }

  /**
   * creates a line consisting of the set of coordinates passed in
   */
  public PTLine(Point nodeA, Point nodeB) {
    this(nodeA.x, nodeA.y, nodeB.x, nodeB.y);
  }

  // ======================================================================
  // Attribute get/set
  // ======================================================================

  /**
	 * returns the type of this object
	 * 
	 * @return the type of the object
	 */
  public String getType() {
    return PTLine.LINE_TYPE;
  }

  // ====================================================================
  // Object attribute setting
  // ====================================================================

  /**
   * Returns the names of the structures this object can parse.
   * 
   * @return an array of Strings containing all handled keywords in the stream
   */
  public String[] handledKeywords() {
    return new String[] { getType() };
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
  	return toString(getType());
  }

  // ======================================================================
  // Drawing
  // ======================================================================

  /**
   * Update the default properties for this object
   * 
   * @param defaultProperties
   *          the properties to be updated
   */
  public void updateDefaults(XProperties defaultProperties) {
    super.updateDefaults(defaultProperties);
    defaultProperties.put(getType() + ".start", getFirstNode().toPoint());
    defaultProperties.put(getType() + ".end", getLastNode().toPoint());
  }

  /**
   * Clones the current graphical object. Note that the method will per
   * definition return Object, so the result has to be cast to the appropriate
   * type.
   * 
   * @return a clone of the current object, statically typed as Object.
   */
  public Object clone() {
    // create new object
    PTLine targetShape = new PTLine();

    // clone shared attributes
    // from PTGO: color, depth, num, objectName
    // from OpenLineBasedShape: fwArrow, bwArrow
    cloneCommonFeaturesInto(targetShape);
    
    return targetShape;
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
  protected void cloneCommonFeaturesInto(PTLine targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
    // from OpenLineBasedShape: hasBWArrow, hasFWArrow
    super.cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    targetShape.setFirstNode(getFirstNode().getX(), getFirstNode().getY());
    targetShape.setLastNode(getLastNode().getX(), getLastNode().getY());
  }

  // =================================================================
  // I/O
  // =================================================================

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
} // PTLine
