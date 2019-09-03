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
public abstract class Location {
  public static final Point POINT_OF_ORIGIN = new Point(0, 0);
  public abstract Point getLocation();
  public abstract void setLocation(int x, int y);
  public abstract void setLocation(Point location);
  public abstract String printLocation();
  public abstract void translate(int dx, int dy);
  public abstract void translate(Point translateDelta);
}
