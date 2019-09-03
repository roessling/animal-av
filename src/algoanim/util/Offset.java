package algoanim.util;

import algoanim.primitives.Primitive;

/**
 * This is a concrete kind of a <code>Node</code>. The coordinates are
 * measured from a given offset <code>Primitive</code>.
 * For example, to place an element 20 pixels to the right and 10 pixels
 * below the lower west point of object <em>a</em>, you would use the following:
 * <code>Node n = new Offset(20, 10, a, "SW");</code>
 * @author Jens Pfau
 */
public class Offset extends Node {
  /**
   * Constant for an offset based on a primitive
   */
  public static final int PRIMITIVE_REFERENCE = 1;

  /**
   * Constant for an offset based on a node
   */
  public static final int NODE_REFERENCE = 2;

  /**
   * Constant for an offset based on an identifier
   */
  public static final int ID_REFERENCE = 4;
  
  /**
   * Determines if the reference mode (based on a primitive) or the node mode
   * (based on a node) is used
   */
  private int referenceMode = PRIMITIVE_REFERENCE;

  /**
   * The x-axis offset.
   */
  private int x = 0;

  /**
   * The y-axis offset.
   */
  private int y = 0;

  /**
   * The ID from which to measure if <em>referenceMode == ID_REFERENCE</em>
   */
  private String baseID;
  
  /**
   * the Node from which to measure if <em>referenceMode == NODE_REFERENCE</em>
   */
  private Node node;

  /**
   * The reference from which to measure if <em>referenceMode == PRIMITIVE_REFERENCE</em>
   */
  private Primitive ref;

  /**
   * The relative placement of this <code>Node</code> seen from the reference.
   */
  private String direction;

  /**
   * Creates a new Offset instance at a distance of (xCoordinate, yCoordinate)
   * in direction "targetDirection" from the base primitive "reference".
   * @param xCoordinate
   *          the x-axis offset.
   * @param yCoordinate
   *          the y-axis offset.
   * @param reference
   *          the referenced <code>Primitive</code>
   * @param targetDirection
   *          the relative placement of this <code>Node</code> seen from the
   *          reference.
   */
  public Offset(int xCoordinate, int yCoordinate, Primitive reference,
      String targetDirection) {
    initialize(xCoordinate, yCoordinate, null, reference, null, targetDirection);
  }

  /**
   * Creates a new Offset instance at a distance of (xCoordinate, yCoordinate)
   * in direction "targetDirection" from the base Node "baseNode".
   * @param xCoordinate
   *          the x-axis offset.
   * @param yCoordinate
   *          the y-axis offset.
   * @param baseNode
   *          the referenced <code>Node</code>
   * @param targetDirection
   *          the relative placement of this <code>Node</code> seen from the
   *          reference.
   */
  public Offset(int xCoordinate, int yCoordinate, Node baseNode,
      String targetDirection) {
    initialize(xCoordinate, yCoordinate, baseNode, null, null, targetDirection);
  }

  /**
   * Creates a new Offset instance at a distance of (xCoordinate, yCoordinate)
   * in direction "targetDirection" from the base reference ID "baseIDRef".
   * @param xCoordinate
   *          the x-axis offset.
   * @param yCoordinate
   *          the y-axis offset.
   * @param baseIDRef
   *          the referenced <em>identifier</em>
   * @param targetDirection
   *          the relative placement of this <code>Node</code> seen from the
   *          reference.
   */
  public Offset(int xCoordinate, int yCoordinate, String baseIDRef,
      String targetDirection) {
    initialize(xCoordinate, yCoordinate, null, null, baseIDRef, targetDirection);
  }

  /**
   * Initializes the internal representation of the offset based on the information passed in
   * @param dx the x offset from the referenced object
   * @param dy the y offset from the referenced object
   * @param baseNode the base node from which the offset is measured (if the base is a node)
   * @param basePrimitive the base primitive from which the offset is measured (if the base is a primitive)
   * @param id the base ID from which the offset is measured (if the base is an ID)
   * @param dir the direction for the offset
   */
  private void initialize(int dx, int dy, Node baseNode, 
      Primitive basePrimitive, String id, String dir) {
    x = dx;
    y = dy;
    direction = dir;

    if (baseNode != null) {
      node = baseNode;
      referenceMode = NODE_REFERENCE;      
    } else if (id != null) {
      referenceMode = ID_REFERENCE;
      baseID = id;
    }
    else {
      referenceMode = PRIMITIVE_REFERENCE;
      ref = basePrimitive;
    }
  }
  
  /**
   * Returns the reference.
   * 
   * @return the reference.
   */
  public String getBaseID() {
    return baseID;
  }

  
  /**
   * Returns the direction.
   * 
   * @return the direction.
   */
  public String getDirection() {
    return direction;
  }

  /**
   * Returns the reference.
   * 
   * @return the reference.
   */
  public Node getNode() {
    return node;
  }

  
  /**
   * Returns the reference.
   * 
   * @return the reference.
   */
  public Primitive getRef() {
    return ref;
  }

  /**
   * Returns the reference mode
   * 
   * @return the reference mode (one of the constants ID_REFERENCE, NODE_REFERENCE,
   * PRIMITIVE_REFERENCE
   */
  public int getReferenceMode() {
    return referenceMode;
  }

  
  /**
   * Returns the x-axis offset.
   * 
   * @return the x-axis offset.
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the y-axis offset.
   * 
   * @return the y-axis offset.
   */
  public int getY() {
    return y;
  }
}
