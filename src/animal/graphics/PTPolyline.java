package animal.graphics;

import java.awt.Point;
import java.util.Vector;

import animal.graphics.meta.OpenLineBasedShape;

/**
 * 
 * This class implements a single 2D Polygon using homogenous coordinates, ie.
 * consisting of a set of x, y and W coordinates.
 * 
 * Based on <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>s
 * PTPolyline
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 24.08.1998
 */
public class PTPolyline extends OpenLineBasedShape {
	
	/**
	 * The type name of this shape
	 */
  public static final String POLYLINE_TYPE = "Polyline";


  // =================================================================
  // STATIC ATTRIBUTES
  // =================================================================

//  private static final long serialVersionUID = 5793164474737251118L;
  
  // ======================================================================
  // Constructors
  // ======================================================================

  /**
   * Create an empty PTPolyline
   */
  public PTPolyline() {
    initializeWithDefaults(getType());
    nodes = new Vector<PTPoint>(20, 10);
  }

  /**
   * Create a PTPolyline from an array of x and y coordinates
   * 
   * @param xCoords
   *          An array of PTPoints for the x coordinates
   * @param yCoords
   *          An array of PTPoints for the y coordinates
   */
  public PTPolyline(int[] xCoords, int[] yCoords) {
    this();
    if (xCoords != null && yCoords != null && xCoords.length == yCoords.length) {
      int nrNodes = xCoords.length;
      for (int nodeNr = 0; nodeNr < nrNodes; nodeNr++)
        addNode(new PTPoint(xCoords[nodeNr], yCoords[nodeNr]));
    }
  }

  /**
   * Create a PTPolyline from an array of PTPoints
   * 
   * @param edges
   *          An array of PTPoints
   */
  public PTPolyline(Point[] edges) {
    this();
    // Simply copy all edges into the "nodes" vector
    for (int i = 0; i < edges.length; i++)
      addNode(new PTPoint(edges[i]));
  }

  /**
   * Create a PTPolyline from an array of PTPoints
   * 
   * @param edges
   *          An array of PTPoints
   */
  public PTPolyline(PTPoint[] edges) {
   this();
    // Simply copy all edges into the "nodes" vector
    for (int i = 0; i < edges.length; i++)
      addNode(edges[i]);
  }

  // ======================================================================
  // Attribute accessing
  // ======================================================================

  /**
   * file version history
   * <ol>
   * <li>basic format</li>
   * <li>double and int params, flipped and filename(all from XFig) left out</li>
   * <li>fillColor added</li>
   * <li>depth added</li>
   * <li>new format</li>
   * </ol>
   */
  public int getFileVersion() {
    return 5;
  }

  /**
	 * returns the type of this object
	 * 
	 * @return the type of the object
	 */
  public String getType() {
    return PTPolyline.POLYLINE_TYPE;
  }

  // ====================================================================
  // Object attribute setting
  // ====================================================================

 
  /**
   * Add a single node to the list of nodes
   */
  public void addNode(PTPoint node) {
    nodes.addElement(node);
  }
  
  /**
   * Add a single node to the list of nodes
   */
  public void addNode(Point node) {
    nodes.addElement(new PTPoint(node));
  }

  /**
   * Returns the names of the structures this object can parse.
   * 
   * @return an array of Strings containing all handled keywords in the stream
   */
  public String[] handledKeywords() {
    return new String[] { "Polyline" };
  }


  // ======================================================================
  // Graphical Transforms
  // ======================================================================

  /**
   * returns the number of different nodes, i.e. nodes that are not "==", though
   * they may be "equals". In a closed polygon the first point equals(at least
   * may equal) the last one, so don't operate on it twice!
   */
  public int getDifferentNodesCount() {
    int c = nodes.size();
    // check for equality of the pointers, not for equality of content!!!
    // Content is expected to be equal, if the Polyline is closed.
    if (c > 0 && nodes.elementAt(0) == nodes.elementAt(c - 1))
      return c - 1;

    return c;
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
    // maxIndex is used to avoid unnecessary multiple accesses to nodes
    // nodeNr is used to access the current node
    int maxIndex = getDifferentNodesCount(), nodeNr;

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
  	return toString(getType());
  }

  /**
   * provides access to the node vector. This is not offered in the super
   * class as some sub-classes may not allow an insertion of an arbitrary
   * number of elements (e.g., a line has exactly two nodes).
   * 
   * @return the vector of nodes
   */
  public Vector<PTPoint> getNodes() {
    return nodes;
  }

  /**
   * provides access to the node vector. This is not offered in the super
   * class as some sub-classes may not allow an insertion of an arbitrary
   * number of elements (e.g., a line has exactly two nodes).
   * 
   * @return the vector of nodes
   */
  public PTPoint getNodeAt(int index) {
    if (index < 0 || index >= nodes.size())
    	return new PTPoint(0, 0);
    PTPoint node = nodes.get(index);
    if (node == null)
    	return new PTPoint(0, 0);
    return node;
  }

  /**
   * provides access to the node vector. This is not offered in the super
   * class as some sub-classes may not allow an insertion of an arbitrary
   * number of elements (e.g., a line has exactly two nodes).
   * 
   * @return the vector of nodes
   */
  public Point getNodeAsPoint(int index) {
    return getNodeAt(index).toPoint();
  }

  /**
   * Assigns a new node vector
   * 
   * @param newNodes the new nodes for this shape
   */
  public void setNodes(Vector<PTPoint> newNodes) {
    nodes = newNodes;
  }

  /**
   * sets the <i>index</i>th node of the Polyline. If this is the first point
   * and the Polyline is closed, the last point is set, too. If the <i>index</i>th
   * node did not exist, it is added.
   */
  public void setNode(int index, PTPoint p) {
    if (index >= nodes.size()) {
      // must not be called when a point is moved!
      addNode(p);
    } else if (index == 0) {
      // important when moving points:
      // first EditPoint(0) must be above last EditPoint
      // (size()-1), when the edit points are drawn. Otherwise, not the
      // following statements are called, but the above ones(i.e. a node
      // is added!
      nodes.setElementAt(p, 0);
      nodes.setElementAt(p, nodes.size() - 1);
    } else
      nodes.setElementAt(p, index);
  }

  /**
   * drops all nodes from this shape
   */
  void removeNodes() {
    if (nodes == null)
      nodes = new Vector<PTPoint>(20, 10);
    else
      nodes.clear();
  }

  /**
   * drops the selected node from the shape
   * @param index the index of the node to be removed
   */
  public void removeNode(int index) {
    nodes.removeElementAt(index);
  }

  // ======================================================================
  // Drawing
  // ======================================================================
  public Object clone() {
    PTPolyline targetShape = new PTPolyline();
    cloneCommonFeaturesInto(targetShape);
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
} // PTPolyline
