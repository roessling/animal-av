/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Point;

/**
 * This class implements a not gate.
 * 
 * @author p_li
 * @version 2.0
 * 
 *          apply to ANIMAL 3.0
 * 
 */
public final class PTNot extends PTNGate {

	/**
	 * The sequence number of an not-gate. It defines the default object name of
	 * an not-gate.
	 */
	private static int notNr = 0;

	/**
	 * The type name of this gate.
	 */
	public static final String NOT_TYPE_LABEL = "Not";

	/**
	 * Create a default not-gate.
	 */
	public PTNot() {
		this(DEFAULT_LOCATION);
	}

	/**
	 * Create an not-gate from a location.
	 * 
	 * @param location
	 *            a point for the location.
	 */
	public PTNot(Point location) {
		this(location.x, location.y);
	}

	/**
	 * Create an not-gate from two coordinates of the location.
	 * 
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 */
	public PTNot(int x, int y) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	/**
	 * Create an not-gate from two coordinates of the location, a width and a
	 * height.
	 * 
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 * @param width
	 *            an integer for the width of the not-gate.
	 * @param height
	 *            an integer for the height of the not-gate.
	 */
	public PTNot(int x, int y, int width, int height) {
		super(x, y, width, height, 1);
		this.setObjectName("not" + notNr);
		notNr++;
		elementSymbol.setText("1");
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "Not" };
	}

	public Object clone() {
		PTNot g = new PTNot();
		cloneCommonFeaturesInto(g);
		return g;
	}

	protected void cloneCommonFeaturesInto(PTNot gate) {
		super.cloneCommonFeaturesInto(gate);
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTNot.NOT_TYPE_LABEL;
	}

	public String toString() {
		return (toString(PTNot.NOT_TYPE_LABEL));
	}

	/**
	 * returns the gate's object name, if the name was empty, create a new one.
	 * 
	 * @return the gate's object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("not" + notNr);
		return objectName;
	}
}
