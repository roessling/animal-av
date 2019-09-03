/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Point;

/**
 * This class implements a nor gate.
 * 
 * @author p_li
 * @version 2.0
 * 
 *          apply to ANIMAL 3.0
 * 
 */
public final class PTNor extends PTNGate {

	/**
	 * The sequence number of an nor-gate. It defines the default object name
	 * of an nor-gate.
	 */
	private static int norNr = 0;

	/**
	 * The type name of this gate.
	 */
	public static final String NOR_TYPE_LABEL = "Nor";

	/**
	 * Create a default nor-gate.
	 */
	public PTNor() {
		this(2);
	}

	/**
	 * Create an nor-gate from an amount of the inputs.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 */
	public PTNor(int inputPortAmount) {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y, inputPortAmount);
	}

	/**
	 * Create an nor-gate from an amount of the inputs and a location.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 * @param location
	 *            a point for the location.
	 */
	public PTNor(Point location, int inputPortAmount) {
		this(location.x, location.y, inputPortAmount);
	}

	/**
	 * Create an nor-gate from an amount of the inputs and two coordinates of
	 * the location.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 */
	public PTNor(int x, int y, int inputPortAmount) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, inputPortAmount);
	}

	/**
	 * Create an nor-gate from an amount of the inputs, two coordinates of the
	 * location, a width and a height.
	 * 
	 * @param inputPortAmount
	 *            an integer for the counter of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 * @param width
	 *            an integer for the width of the nor-gate.
	 * @param height
	 *            an integer for the height of the nor-gate.
	 */
	public PTNor(int x, int y, int width, int height, int inputPortAmount) {
		super(x, y, width, height, inputPortAmount);
		this.setObjectName("nor" + norNr);
		norNr++;
		elementSymbol.setText("\u22651");
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "Nor" };
	}

	public Object clone() {
		PTNor g = new PTNor();
		cloneCommonFeaturesInto(g);
		return g;
	}

	protected void cloneCommonFeaturesInto(PTNor gate) {
		super.cloneCommonFeaturesInto(gate);
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTNor.NOR_TYPE_LABEL;
	}

	public String toString() {
		return (toString(PTNor.NOR_TYPE_LABEL));
	}

	/**
	 * returns the gate's object name, if the name was empty, create a new one.
	 * 
	 * @return the gate's object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("nor" + norNr);
		return objectName;
	}
}
