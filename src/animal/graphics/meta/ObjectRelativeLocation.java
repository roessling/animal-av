/*
 * Created on 24.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package animal.graphics.meta;

import java.awt.Point;


/**
 * @author Guido
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ObjectRelativeLocation extends Location {
  private int dx;
  private int dy;
  private int orientation;
  public static final int NW = 1;
  public static final int N = 2;
  public static final int NE = 3;
  public static final int W = 4;
  public static final int C = 5;
  public static final int E = 6;
  public static final int SW = 7;
  public static final int S = 8;
  public static final int SE = 9;
  private int referenceID;

  public ObjectRelativeLocation(Point offset, int targetOrientation, int reference) {
  	if (offset != null)
  	  setOffset(offset.x, offset.y, targetOrientation, reference);
  	else
  	  setOffset(0, 0, targetOrientation, reference);
  }

  public ObjectRelativeLocation(int deltaX, int deltaY, 
  		int targetOrientation, int reference) {
   	setOffset(deltaX, deltaY, targetOrientation, reference);
  }

  private void setOffset(int deltaX, int deltaY, int targetOrientation, int reference) {
  	dx = deltaX;
  	dy = deltaY;
  	orientation = targetOrientation;
  	referenceID = reference;
  }
  
  /* (non-Javadoc)
   * @see animal.graphics.Location#getLocation()
   */
  public Point getLocation() {
//TODO something sensible here...
  	Point theLocation = new Point(0, 0);
  	return theLocation;
  }

  /* (non-Javadoc)
   * @see animal.graphics.Location#setLocation(int, int)
   */
  public void setLocation(int x, int y) {
    Point tmp = new Point(x, y);
    setLocation(tmp);
  }

  /* (non-Javadoc)
   * @see animal.graphics.Location#setLocation(java.awt.Point)
   */
  public void setLocation(Point location) {
  	if (location != null)
      setLocation(location.x, location.y);
  	else
  	  setLocation(0, 0);
  }

  /* (non-Javadoc)
   * @see animal.graphics.Location#printLocation()
   */
  public String printLocation() {
  	StringBuilder sb = new StringBuilder(32);
    sb.append("offset (").append(dx).append(", ");
    sb.append(dy).append(") from ").append(referenceID).append(" ").append(orientation);
    return sb.toString();
  }

  /* (non-Javadoc)
   * @see animal.graphics.Location#translate(int, int)
   */
  public void translate(int deltaX, int deltaY) {
  	dx += deltaX;
  	dy += deltaY;
  }

  /* (non-Javadoc)
   * @see animal.graphics.Location#translate(java.awt.Point)
   */
  public void translate(Point translateDelta) {
  	if (translateDelta != null) {
      translate(translateDelta.x, translateDelta.y);
  	}
  }
}
