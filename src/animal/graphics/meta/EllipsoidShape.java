package animal.graphics.meta;

import java.awt.Point;

public interface EllipsoidShape {
	/**
	 * Returns the radius of the ellipsoid shape as a Point
	 * 
	 * @return the radius of this shape as an Point value
	 */
  public Point getRadius();
  
	/**
	 * Set the radius of the ellipsoid shape
	 * 
	 * @param r the target radius for the ellipsoid shape
	 */
  public void setRadius(Point r);

	/**
	 * Set the radius of the ellipsoid shape
	 * 
	 * @param x the x radius of the shape
	 * @param y the y radius of the shape
	 */
  public void setRadius(int x, int y);
} // EllipsoidShape
