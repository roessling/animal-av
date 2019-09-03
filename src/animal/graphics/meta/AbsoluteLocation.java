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
public class AbsoluteLocation extends Location {
  private Point theLocation;

  /* (non-Javadoc)
   * @see animal.graphics.Location#getLocation()
   */
  public Point getLocation() {
  	if (theLocation == null)
  	  theLocation = new Point(0, 0);
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
  	if (theLocation != null)
      theLocation.setLocation(location);
  	else
  	  theLocation = new Point(location);
  }

  /* (non-Javadoc)
   * @see animal.graphics.Location#printLocation()
   */
  public String printLocation() {
  	StringBuilder sb = new StringBuilder(32);
  	Point localLoc = getLocation();
    sb.append("(").append(localLoc.x).append(", ");
    sb.append(localLoc.y).append(")");
    return sb.toString();
  }

  /* (non-Javadoc)
   * @see animal.graphics.Location#translate(int, int)
   */
  public void translate(int dx, int dy) {
  	if (theLocation == null)
        theLocation = new Point(0, 0);
      theLocation.translate(dx, dy);
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
