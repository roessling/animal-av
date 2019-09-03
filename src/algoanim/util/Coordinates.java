package algoanim.util;

/**
 * A concrete type of a <code>Node</code>. The coordinates of this one are
 * measured absolutely, i.e. by giving concrete (x, y) coordinates
 * 
 * @author Jens Pfau
 */
public class Coordinates extends Node {
	/**
	 * the x-axis coordinate.
	 */
	private int x = 0;

	/**
	 * the y-axis coordinate.
	 */
	private int y = 0;

	/**
	 * Creates a new coordinate by using the (x, y) values passed in
	 * @param xCoordinate
	 *          the x-axis coordinate.
	 * @param yCoordinate
	 *          the y-axis coordinate.
	 */
	public Coordinates(int xCoordinate, int yCoordinate) {
		if (xCoordinate > 0)
			x = xCoordinate;

		if (yCoordinate > 0)
			y = yCoordinate;
	}

	/**
	 * Returns the x-axis coordinate.
	 * 
	 * @return the x-axis coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y-axis coordinate.
	 * 
	 * @return the y-axis coordinate.
	 */
	public int getY() {
		return y;
	}
}
