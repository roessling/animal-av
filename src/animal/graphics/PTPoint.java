package animal.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import animal.misc.XProperties;

/**
 * 
 * This class implements a single 2D point using homogenous coordinates, ie.
 * consisting of an x, y and W coordinate.
 * 
 * PTPoint uses homogenous coordinates, ie., a triplet of coordinates of the
 * form(x, y, W). Two PTPoints are then identical if there is a factor n so
 * that(x1, y1, W1) = (x2*n, y2*n, W2*n). Normally, we use W = 1. <br />
 * Note that the point(0, 0, 0) does not exist.
 * 
 * The advantage of homogenous coordinates lies in the fact that all 2D and 3D
 * coordinates can be represented by matrix multiplications.
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.1 31.10.2008
 */
public class PTPoint extends PTGraphicObject implements Cloneable {

  // =================================================================
  // CONSTANTS
  // =================================================================

	public static final PTPoint POINT_OF_ORIGIN = new PTPoint(0, 0);
	
  public static final String POINT_TYPE = "Point";

  /**
   * This constant contains the UID for serialization, <em>do not edit</em>!
   */
//  private static final long serialVersionUID = 5578116484265222226L;

  // =====================================================================
  // Fields
  // =====================================================================

  /**
   * The coordinates of the point. Since the exact coordinates need not be
   * visible outside this class(and should not!), this is private.
   */
  private double[] coord = new double[3];

  // ======================================================================
  // Constructors
  // ======================================================================

  /**
   * Constructs a Point with the default coordinates
   */
  public PTPoint() {
    this(0, 0, 0);
  }

  /**
   * Directly constructs a Point with the given coordinates
   * 
   * @param theX
   *          The desired x coordinate of the new PTPoint object
   * @param theY
   *          The desired y coordinate of the new PTPoint object
   * @param theW
   *          The desired w coordinate of the new PTPoint object
   */
  public PTPoint(int theX, int theY, int theW) {
    initializeWithDefaults(getType());
    set(theX, theY, theW);
  }

  /**
   * Directly constructs a Point with the given coordinates
   * 
   * @param x
   *          The desired x coordinate of the new PTPoint object
   * @param y
   *          The desired y coordinate of the new PTPoint object
   */
  public PTPoint(int x, int y) {
    this(x, y, 0);
  }

  /**
   * constructs a PTPoint from a Point
   */
  public PTPoint(PTPoint p) {
    this(p.getX(), p.getY(), p.getW());
  }

  /**
   * Constructs a Point identical to the argument passed.
   * 
   * @param aPoint
   *          A Point whose coordinates are copied into the new object.
   */
  public PTPoint(Point aPoint) {
    this(aPoint.x, aPoint.y, 0);
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
   * <li>points now possess a <em>depth</em></li>
   * </ol>
   */
  public int getFileVersion() {
    return 2;
  }

  /**
   * Return the type of this object
   * 
   * @return the type of the object
   */
  public String getType() {
    return PTPoint.POINT_TYPE;
  }

  /**
   * This routine should be used when trying to access the current x coordinate
   * 
   * @return The x coordinate of the current PTPoint
   */
  public int getX() {
    return (int) Math.round(coord[0]);
  }

  /**
   * This routine should be used when trying to access the current y coordinate
   * 
   * @return The y coordinate of the current PTPoint
   */
  public int getY() {
    return (int) Math.round(coord[1]);
  }

  /**
   * This routine should be used when trying to access the current w coordinate
   * 
   * @return The w coordinate of the current PTPoint
   */
  public int getW() {
    return (int) Math.round(coord[2]);
  }

  /**
   * Returns the names of the structures this object can parse.
   * 
   * @return an array of Strings containing all handled keywords in the stream
   */
  public String[] handledKeywords() {
    return new String[] { "Point" };
  }

  // ======================================================================
  // Attribute setting
  // ======================================================================

  /**
   * This routine should be used when trying to set the current coordinates to
   * another value unless the action is performed inside this class
   * 
   * @param x
   *          The desired x coordinate of the PTPoint
   * @param y
   *          The desired y coordinate of the PTPoint
   * @param w
   *          The desired w coordinate of the PTPoint
   */
  public void set(double x, double y, double w) {
    coord[0] = x;
    coord[1] = y;
    coord[2] = w;
  }

  /**
   * Set the point's location
   * 
   * @param p
   *          the location of the point
   */
  public void set(Point p) {
    set(p.x, p.y, 1);
  }

  /**
   * This routine should be used when trying to set the current w coordinate to
   * another value unless the action is performed inside this class
   * 
   * @param value
   *          The new value of the w coordinate
   */
  public void setW(int value) {
    coord[2] = value;
  }

  /**
   * This routine should be used when trying to set the current x coordinate to
   * another value unless the action is performed inside this class
   * 
   * @param value
   *          The new value of the x coordinate
   */
  public void setX(int value) {
    coord[0] = value;
  }

  /**
   * This routine should be used when trying to set the current y coordinate to
   * another value unless the action is performed inside this class
   * 
   * @param value
   *          The new value of the y coordinate
   */
  public void setY(int value) {
    coord[1] = value;
  }

  // ======================================================================
  // Drawing
  // ======================================================================

  /**
   * This routine draws the PTPoint on the Graphics object passed as a
   * parameter. Since Java does not provide a routine to draw a single pixel,
   * drawing is accomplished by drawing a line of length 1(=1 pixel)
   * 
   * @param g
   *          The platform-specific Graphics object used for all graphic
   *          operations.
   */
  public void paint(Graphics g) {
    // ---------------------------------------------------------
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    // to get the former version change g2 into g and remove the code above
    // under the line

    g2.setColor(getColor());
    g2.drawLine(getX(), getY(), getX(), getY());
  }

  // ======================================================================
  // Graphical Transforms
  // ======================================================================

  /**
   * <em>rotate(double)</em> performs a 2D rotation on the given PTPoint,
   * using the coordinate(0, 0) of the current viewport as the rotation center.
   * 
   * If a different rotation center is desired, use
   * <em>rotate(double, center)</em> instead.
   * 
   * The formula needed for performing the transformation can be found in
   * Foley/van Dam, page 206, formula 5.24
   * 
   * Non-integer coordinates resulting from a rotate operation will be rounded
   * to the nearest integer coordinates.
   * 
   * @param angle
   *          The rotation angle in polar measurement(0...2*pi)
   * 
   * @see PTPoint#rotate(double, PTPoint)
   */
  public void rotate(double angle) {
    // Indices for matrix access
    int i, j;
    double cosi = Math.cos(angle), sini = Math.sin(angle);

    // Variable for storing the current value. Necessary to avoid errors
    // occurring when using old and updated values in the same multiplication
    double[] tmp = { 0, 0, 0 };

    // Rotation matrix; values are taken from the sources given above
    double[][] rotationMatrix = { { cosi, -sini, 0 }, { sini, cosi, 0 },
        { 0, 0, 1 } };

    // Calculate the transformation
    for (i = 0; i < 3; i++)
      for (j = 0; j < 3; j++)
        tmp[i] += rotationMatrix[i][j] * coord[j];

    // Store results as new coordinates, rounding to int first, since we
    // only use integer coordinates.
    set(tmp[0], tmp[1], tmp[2]);
  }

  /**
   * <em>rotate(double, PTPoint center)</em> performs a 2D rotation on the
   * given PTPoint, using the parameter <em>center</em> as the center of the
   * rotation.
   * 
   * If the rotation center to be used is equal to the viewport point of origin
   * (i.e., coordinate(0, 0)), use <em>rotate(double)</em>.
   * 
   * The formula needed for performing the transformation can be found in
   * Foley/van Dam, page 206, formula 5.24
   * 
   * Non-integer coordinates resulting from a rotate operation will be rounded
   * to the nearest integer coordinates.
   * 
   * @param angle
   *          The rotation angle in polar measurement(0...2*pi)
   * 
   * @param center
   *          The point which is to be considered as the rotation center.
   */
  public void rotate(double angle, PTPoint center) {
    // Translate center of rotation to origin
    translate(-center.getX(), -center.getY());

    // Rotate(around point of origin)
    rotate(angle);

    // Translate back
    translate(center.getX(), center.getY());
  }

  /**
   * <em>fast_scale</em>, a quicker implementation of scale(but not quite as
   * clean as <em>scale</em>
   * 
   * @param xScaleFactor
   *          The scale coefficient in x direction
   * @param yScaleFactor
   *          The scale coefficient in y direction
   */
  public void scale(double xScaleFactor, double yScaleFactor) {
    // Set target coordinates explicitly
    set((coord[0] * xScaleFactor), (coord[1] * yScaleFactor), coord[2]);
  }

  /**
   * <em>fast_shear</em>, a quicker implementation of shearing(but not quite
   * as clean as <em>shear</em>
   * 
   * @param xShearFactor
   *          The shearing coefficient in x direction
   * @param yShearFactor
   *          The shearing coefficient in y direction
   */
  public void shear(double xShearFactor, double yShearFactor) {
    // Set target coordinates explicitly
    set(coord[0] + xShearFactor * coord[1], coord[1] + yShearFactor * coord[0],
        coord[2]);
  }

  /**
   * adapts the point by moving its x and y coordinates.
   * 
   * @param deltaX
   *          The translation coefficient in x direction
   * @param deltaY
   *          The translation coefficient in y direction
   */
  public void translate(int deltaX, int deltaY) {
    // Set target coordinates explicitly
    set(coord[0] + deltaX, coord[1] + deltaY, coord[2]);
  }

  /**
   * Generate a String representing this object's state.
   * 
   * @return The string representing the current PTPoint.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder(40);
    sb.append("Point ");
    if (getObjectName() != null)
      sb.append("\"").append(getObjectName()).append("\" ");
    sb.append("(").append(getX()).append(", ").append(getY()).append(")");
    return sb.toString();
  }

  /**
   * returns the java.awt.Point representing this PTPoint
   * 
   * @return a java.awt.Point object that has the same location as this object
   */
  public Point toPoint() {
    return new Point(getX(), getY());
  }

  /**
   * Update the default properties for this object
   * 
   * @param defaultProperties
   *          the properties to be updated
   */
  public void updateDefaults(XProperties defaultProperties) {
    super.updateDefaults(defaultProperties);
    defaultProperties.put(getType() + ".location", new Point(getX(), getY()));
  }

  /**
   * clones the primitive
   * 
   * @return a clone of the primitive
   */
  public Object clone() {
    // create new object
    PTPoint p = new PTPoint();
    // clone shared attributes
    // from PTGO: color, depth, num, objectName
    cloneCommonFeaturesInto(p);

    return p;
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
  protected void cloneCommonFeaturesInto(PTPoint targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
    super.cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    targetShape.coord = coord.clone();
  }

  /**
   * Determines the primitive's bounding box
   * 
   * @return the primitive's bounding box
   */
  public Rectangle getBoundingBox() {
    return new Rectangle(toPoint());
  }

  public void discard() {
    coord = null;
    super.discard();
  }
}
