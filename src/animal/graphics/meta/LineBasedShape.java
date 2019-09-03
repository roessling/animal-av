package animal.graphics.meta;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.SwingUtilities;

import animal.animator.MoveBase;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.misc.MSMath;

/**
 * This abstract class models the common aspects of line-based shapes
 * such as lines, polylines or polygons
 * 
 * @author Dr. Guido R&ouml;&szlig;ling (roessling@acm.org>
 * @version 1.1 2008-10-24
 */

public abstract class LineBasedShape extends PTGraphicObject implements MoveBase {

  /**
   * The Vector containing the nodes making up this open line-based shape.
   * Note that this class does <em>not</em> provide means to add or remove
   * nodes to and from the Vector, respectively, to prevent users from
   * creating inappropriate structures. For example, a Line based on this
   * class may only have exactly two nodes.
   */
  protected Vector<PTPoint> nodes;

  protected void fillNodesVector(int size) {
  	//TODO create new Vector of "fitting" size. May not be elegant, but so what
  	nodes = new Vector<PTPoint>(size);
  	
  	// fill with default nodes
  	for (int i = 0; i < size; i++)
  		nodes.addElement(new PTPoint(0, 0));
	}	

  protected void fillNodesVector(int size, Vector<PTPoint> defaults) {
  	nodes = new Vector<PTPoint>(size); // create of appropriate size
  	int i = 0;
  	// fill Vector with the default elements
  	for (; i < defaults.size(); i++)
			nodes.addElement((PTPoint)defaults.elementAt(i).clone());
  	// if this is not enough, add new PTPoint instances for later "set" operations
  	for (i = defaults.size(); i < size; i++)
  		nodes.addElement(new PTPoint(0, 0));
	}

	/**
	 * Return the bounding box for the open line-based shape.
	 * 
	 * @return the bounding box of the open line-based shape
	 */
  public Rectangle getBoundingBox() {
    if (getNodeCount() == 0)
      return new Rectangle(0, 0, 0, 0);
    Rectangle r = new Rectangle(getNodeAt(0).toPoint());
    // add each node of the polygon to the rectangle, the result being
    // the rectangle containing all points
    for (int i = 1; i < getNodeCount(); i++) {
      Point p = getNodeAt(i).toPoint();
      r = SwingUtilities.computeUnion(p.x, p.y, 0, 0, r);
    }
    return r;
  }

  /**
   * implementing MoveBase, an OpenLineBasedShape must overwrite this method. It returns the
   * total length of the polyline, being the sum of the lengths of all edges.
   * 
   * @return the length of this shape
   */
  public int getLength() {
    int result = 0;
    // add all edges' length
    for (int i = 0; i < getNodeCount() - 1; i++) {
      result += MSMath.dist(getNodeAt(i), getNodeAt(i + 1));
    }
    return result;
  }

  /**
   * Access the total number of points
   * 
   * @return The number of nodes of this PTSquare
   */
  public int getNodeCount() {
    if (nodes == null)
      return 0;
    return nodes.size();
  }
  
  /**
   * returns the nodes at the selected index for this structure
   * 
   * @param index the target position of the nodes (should be <em>0 &lt;= index
   * &lt; getNodeCount()</em>
   * @return the node at position index, null if none such exists
   */
  public Point getNodeAsPoint(int index) {
  	PTPoint point = getNodeAt(index);
  	if (point != null)
  		return point.toPoint();
  	return null;
  }
  

  /**
   * Returns the node at index "index"
   * 
   * @param index the index of the node to be returned. Note: counting starts at 0
   * @return the node at the chosen position.
   */
	protected PTPoint getNodeAt(int index) {
		if (nodes == null || index < 0 || index > nodes.size())
			return new PTPoint(0, 0);
		return nodes.get(index);
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
    Point a, b;
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
    return getNodeAt(0).toPoint();
  }

	
	/**
	 * Returns a String representation of the given Point
	 * 
	 * @param node the point to be returned as a String
	 * @return the String representation
	 */
	public String nodeToString(Point node) {
		StringBuilder sb = new StringBuilder(32);
		if (node != null)
			sb.append("(").append(node.x).append(",").append(node.y).append(") ");
		return sb.toString();
	}

	
  /**
   * Sets a specific node (indicated by the index) to (x, y)
   * 
   * @param index the index of the node to be set. Note: counting starts at 0
   * @param x the x coordinate of the chosen node
   * @param y the y coordinate of the chosen node
   */
	protected void setNode(int index, int x, int y) {
		nodes.ensureCapacity(index);
		if (index >= nodes.size())
			nodes.add(new PTPoint(x, y));
		nodes.set(index, new PTPoint(x, y));
	}

  /**
   * Sets the square node to the give point
   * 
   * @param index the index of the node to be set. Note: counting starts at 0
   * @param p the new coordinate of the square node
   */
  public void setNode(int index, Point p) {
  	if (p != null)
  		setNode(index, p.x, p.y);
	}

  /**
   * Sets the square node to the give point
   * 
   * @param index the index of the node to be set. Note: counting starts at 0
   * @param p the new coordinate of the square node
   */
  public void setNode(int index, PTPoint p) {
  	if (p != null)
  		setNode(index, p.getX(), p.getY());
	}

  /**
   * returns a <code>java.awt.Polygon</code> representing the open
   * line-based shape. Note that <em>only</em> the nodes are encoded,
   * no color etc.!
   * 
   * @return a polygon containing the nodes of this open line-based shape.
   */
  public Polygon toPolygon() {
    int numNodes = nodes.size();
    int i, counter;
    int[] xpoints = new int[numNodes + 1];
    int[] ypoints = new int[numNodes + 1];
    Point p;
    Point oldP = null;
    counter = 0;
    for (i = 0; i < numNodes; i++) {
      p = getNodeAt(i).toPoint();
      // in a polygon, no two adjacent points must be the same.
      // This may also happen due to scaling.
      if (!p.equals(oldP)) {
        xpoints[counter] = p.x;
        ypoints[counter] = p.y;
        counter++;
      }
      oldP = p;
    }
    return new Polygon(xpoints, ypoints, counter);
  }

  /**
   * <em>rotate(double)</em> performs a 2D rotation on the given shape,
   * using the coordinate(0, 0) of the current viewport as the rotation center.
   * 
   * 
   * If a different rotation center is desired, use
   * <em>rotate(double, center)</em> instead.
   * 
   * 
   * The formula needed for performing the transformation can be found in the PT
   * slides on page GTR-21 or in Foley/van Dam, page 206, formula 5.24
   * 
   * Non-integer coordinates resulting from a rotate operation will be rounded
   * to the nearest integer coordinates.
   * 
   * @param angle
   *          The rotation angle in polar measurement(0...2*pi)
   * 
   * @see PTPolyline#rotate(double, animal.graphics.PTPoint)
   */
  public void rotate(double angle) {
    // max_index is used to avoid unnecessary multiple accesses to nodes;
    // node_nr is used to access the current node
    int maxIndex = getNodeCount(), nodeNr;

    // Iterate on all nodes, telling each to rotate itself
    for (nodeNr = 0; nodeNr < maxIndex; nodeNr++)
      nodes.elementAt(nodeNr).rotate(angle);

    // All points are rotated => the shape is rotated.
  }

  /**
   * <em>rotate(double, PTPolyline center)</em> performs a 2D rotation on the
   * given shape, using the parameter <em>center</em> as the center of
   * the rotation.
   * 
   * If the rotation center to be used is equal to the viewport Polygon of
   * origin(i.e., coordinate(0, 0)), use <em>rotate(double)</em>.
   * 
   * 
   * The formula needed for performing the transformation can be found in the PT
   * slides on page GTR-21 or in Foley/van Dam, page 206, formula 5.24
   * 
   * Non-integer coordinates resulting from a rotate operation will be rounded
   * to the nearest integer coordinates.
   * 
   * @param angle
   *          The rotation angle in polar measurement(0...2*pi)
   * 
   * @param center
   *          The point considered as the rotation center.
   */
  public void rotate(double angle, PTPoint center) {
    // maxIndex is used to avoid unnecessary multiple accesses to nodes;
    // node_nr is used to access the current node
    int maxIndex = getNodeCount(), nodeNr;

    // Iterate on all nodes, telling each to rotate itself around center
    for (nodeNr = 0; nodeNr < maxIndex; nodeNr++)
      if (nodes.elementAt(nodeNr) != null)
        nodes.elementAt(nodeNr).rotate(angle, center);

    // All points are rotated around center => the shape is rotated.
  }

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
    if (nodes == null)
    	return;
    for (PTPoint node : nodes)
    	if (node != null)
    		node.scale(scaleX, scaleY);
  }

  /**
   * <em>translate(int, int)</em> performs a 2D translation on the given
   * PTSquare. The formula needed for performing the transformation can be found
   * in Foley/van Dam, page 205, formula 5.12
   * 
   * @param deltaX
   *          The translation coeeficient for the x direction. Since we only use
   *          integer coordinates, this is an integer, too.
   * 
   * @param deltaY
   *          The translation coeeficient for the y direction. Since we only use
   *          integer coordinates, this is an integer, too.
   */
  public void translate(int deltaX, int deltaY) {
  	// iterate over all nodes...
  	for (PTPoint aNode : nodes)
    	if (aNode != null) // is existing...
    		aNode.translate(deltaX, deltaY); // translate node
  	// all nodes translated -> shape is translated
  }
  
  /**
   * This method should be called whenever the nodes of the shape are
   * changed, e.g. by changing the size of a square or the width of a
   * rectangle. Some shapes may leave this method empty.
   */
  protected void updateShape() {
  	// nothing to be done here...
  }


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
    
    int nrNodes = getNodeCount();
    targetShape.fillNodesVector(nrNodes, nodes);
  }
  
	/**
	 * free the links to sub-objects to make garbage collection easier
	 */
	public void discard() {
		super.discard();
		nodes.clear();
		nodes = null;
		color = null;
	}
}
