package animal.graphics;

import java.awt.Point;
import java.util.Vector;

import animal.graphics.meta.PolygonalShape;

/**
 * 
 * This class implements a single 2D Polygon using homogeneous coordinates, i.e.
 * consisting of a set of x, y and W coordinates.
 * 
 * Based on <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>s
 * PTPolyline
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 24.08.1998
 */
public class PTPolygon extends PolygonalShape {
  // =================================================================
  // STATIC ATTRIBUTES
  // =================================================================
  public static final String POLYGON_TYPE = "Polygon";

//  private static final long serialVersionUID = 5793164474737251118L;

  // ======================================================================
  // Constructors
  // ======================================================================

  /**
   * Create an empty PTPolyline
   */
  public PTPolygon() {
    this(new PTPoint[]{});
  }

  /**
   * Create a PTPolyline from an array of x and y coordinates
   * 
   * @param xCoords
   *          An array of PTPoints for the x coordinates
   * @param yCoords
   *          An array of PTPoints for the y coordinates
   */
  public PTPolygon(int[] xCoords, int[] yCoords) {
    initializeWithDefaults(getType());
    nodes = new Vector<PTPoint>(20, 10);
    if (xCoords != null && yCoords != null 
        && xCoords.length == yCoords.length) {
    	nodes.ensureCapacity(xCoords.length);
    	int nrNodes =xCoords.length;      
      for (int nodeNr = 0; nodeNr < nrNodes; nodeNr++)
      	nodes.add(new PTPoint(xCoords[nodeNr], yCoords[nodeNr]));
//        setNode(nodeNr, new PTPoint(xCoords[nodeNr], yCoords[nodeNr]));
    }
  }

  /**
   * Create a PTPolyline from an array of PTPoints
   * 
   * @param edges
   *          An array of PTPoints
   */
  public PTPolygon(Point[] edges) {
    initializeWithDefaults(getType());
    int size = 20;
    Point[] myEdges = edges;
    if (myEdges == null)
      myEdges = new Point[0];
    size = myEdges.length;
    nodes = new Vector<PTPoint>(size, 10);
    // Simply copy all edges into the "nodes" vector
    for (Point p : myEdges)
      if (p != null)
        nodes.add(new PTPoint(p));
  }

  /**
   * Create a PTPolyline from an array of PTPoints
   * 
   * @param edges
   *          An array of PTPoints
   */
  public PTPolygon(PTPoint[] edges) {
    initializeWithDefaults(getType());
    int size = 20;
    PTPoint[] myEdges = edges;
    if (myEdges == null)
      myEdges = new PTPoint[0];
    size = myEdges.length;
    nodes = new Vector<PTPoint>(size, 10);
    // Simply copy all edges into the "nodes" vector
    for (PTPoint p : myEdges)
      if (p != null)
        nodes.add(p);
  }

  // ======================================================================
  // Attribute accessing
  // ======================================================================

  /**
   * file version history 
   */
  public int getFileVersion() {
    return 1;
  }

  /**
   * provides access to the nodes of this shape
   * 
   * @return the nodes of this shape
   */
  public Vector<PTPoint> getNodes() {
  	return nodes;
  }
  
  /**
	 * returns the type of this object
	 * 
	 * @return the type of the object
	 */
  public String getType() {
    return PTPolygon.POLYGON_TYPE;
  }

  /**
   * Returns the names of the structures this object can parse.
   * 
   * @return an array of Strings containing all handled keywords in the stream
   */
  public String[] handledKeywords() {
    return new String[] { "Polygon" };
  }

  // ======================================================================
  // Graphical Transforms
  // ======================================================================

  /**
   * <em>shear(double, double)</em> performs a 2D shearing on the given
   * PTPolyline, using the parameters as the shearing factors.
   * 
   * 
   * Use <em>shear(shear_x, 0)</em> for x-axis shearing and <em>shear(0,
   * shear_y)</em>
   * for y-axis shearing.
   * 
   * <strong>Caution:</strong> Usign <em>shear(shear_x, shear_y)</em> with
   * shear_x, shear_y != 0 results in a shearing that cannot be undone by other
   * shearing operations.
   * 
   * The formula needed for performing the transformation can be found in the PT
   * slides on page GTR-20 or in Foley/van Dam, page 206, formula 5.17
   * 
   * Non-integer coordinates resulting from a shear operation will be rounded to
   * the nearest integer coordinates.
   * 
   * @param shearX
   *          The shear factor in x-direction. This is a double value to support
   *          all kinds of shearing.
   * 
   * @param shearY
   *          The shear factor in y-direction. This is a double value to support
   *          all kinds of shearing.
   */
  public void shear(double shearX, double shearY) {
    // max_index is used to avoid unnecessary multiple accesses to nodes;
    // node_nr is used to access the current node
    int maxIndex = getNodeCount(), nodeNr;

    // Iterate on all nodes, telling each to shear itself
    for (nodeNr = 0; nodeNr < maxIndex; nodeNr++)
      if (nodes.elementAt(nodeNr) != null)
        nodes.elementAt(nodeNr).shear(shearX, shearY);

    // All points are sheared => PTPolyline is sheared.
  }


  /**
   * <em>translate(int, int, int)</em> performs a 2D translation on the given
   * node. The formula needed for performing the transformation can be found
   * in Foley/van Dam, page 205, formula 5.12
   * 
   * @param index the index of the node to be translated (starts with 0)
   * @param deltaX
   *          The translation coeeficient for the x direction. Since we only use
   *          integer coordinates, this is an integer, too.
   * 
   * @param deltaY
   *          The translation coeeficient for the y direction. Since we only use
   *          integer coordinates, this is an integer, too.
   */
  public void translate(int index, int deltaX, int deltaY) {
  	// ensure that the index is valid and a node exists
  	if (index >= 0 && nodes.size() >= index && nodes.get(index) != null)
  		nodes.elementAt(index).translate(deltaX, deltaY); // translate node
  }

  /**
   * <em>translate(int, int)</em> performs a 2D translation on the given
   * PTPolyline. The formula needed for performing the transformation can be
   * found in the PT slides on page GTR-19 or in Foley/van Dam, page 205,
   * formula 5.12
   * 
   * @param moveTheseOnly
   *          an array indicating the nodes to be moved
   * @param deltaX
   *          The translation coeeficient for the x-direction. Since we only use
   *          integer coordinates, this is an integer, too.
   * 
   * @param deltaY
   *          The translation coeeficient for the y-direction. Since we only use
   *          integer coordinates, this is an integer, too.
   */
  public void translate(boolean[] moveTheseOnly, int deltaX, int deltaY) {
    // max_index is used to avoid unnecessary multiple accesses to nodes
    // nodeNr is used to access the current node
    int maxIndex = getNodeCount(), nodeNr;

    // Iterate on all nodes, telling each to translate itself
    for (nodeNr = 0; nodeNr < maxIndex; nodeNr++)
      if (nodes.elementAt(nodeNr) != null && moveTheseOnly[nodeNr])
        nodes.elementAt(nodeNr).translate(deltaX, deltaY);
    // All points are translated => PTPolyline is translated.
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
  	return toString("PTPolygon");
  }

  void removeNodes() {
    if (nodes == null)
      nodes = new Vector<PTPoint>(20, 10);
    else
      nodes.clear();
  }

  public void removeNode(int index) {
    nodes.removeElementAt(index);
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
    PTPolygon targetShape = new PTPolygon();

    // clone shared attributes
    // from PTGO: color, depth, num, objectName
    cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    return targetShape;
  }

  /**
   * This method will test the operation passed in for needing a node selection.
   * Most operations will not need this, but some require a selector -
   * especially the 'move selected / all but selected nodes' animators.
   * 
   * @param operation
   *          the name of the requested operation
   * @return true if the use of a NodeSelector is required, else false. Note
   *         that PTGraphicObject always returns <code>false</code>.
   */
  public boolean operationRequiresSpecialSelector(String operation) {
    return (operation != null && operation.indexOf("Nodes") != -1);
  }

  public String baseOperationName(String methodName) {
    if (methodName.endsWith("..."))
      return methodName.substring(0, methodName.indexOf('.'));
    else if (methodName.indexOf("Nodes") != 0)
      return methodName.substring(0, methodName.indexOf(' '));
    else
      return methodName;
  }

  /**
   * This will test the operation passed for allowing multiple node selection.
   * Most operations will not need this, but some require a selector -
   * especially the 'move selected / all but selected nodes' animators.
   * 
   * @param operation
   *          the name of the requested operation
   * @return true if the use of a NodeSelector is required, else false. Note
   *         that PTGraphicObject always returns <code>false</code>.
   */
  public boolean enableMultiSelectionFor(String operation) {
    return (operation != null && operation.indexOf("Nodes") != -1);
  }

  public boolean compatibleMethod(String method) {
    return (method != null && method.indexOf("Nodes") != -1);
  }
} // PTPolygon
