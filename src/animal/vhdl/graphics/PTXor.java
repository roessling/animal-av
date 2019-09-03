/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Point;

/**
 * This class implements a xor gate.
 * 
 * @author p_li
 * @version 2.0
 * 
 *          apply to ANIMAL 3.0
 * 
 */
public final class PTXor extends PTGate {

	/**
	 * The sequence number of an xor-gate. It defines the default object name of
	 * an xor-gate.
	 */
	private static int xorNr = 0;
	
	/**
	 * The type name of this gate.
	 */
	public static final String XOR_TYPE_LABEL = "Xor";

	/**
	 * Create a default xor-gate.
	 */
	public PTXor() {
		this(2);
	}

	/**
	 * Create an xor-gate from an amount of the inputs.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 */
	public PTXor(int inputPortAmount) {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y, inputPortAmount);
	}

	/**
	 * Create an xor-gate from an amount of the inputs and a location.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 * @param location
	 *            a point for the location.
	 */
	public PTXor(Point location, int inputPortAmount) {
		this(location.x, location.y, inputPortAmount);
	}

	/**
	 * Create an xor-gate from an amount of the inputs and two coordinates of
	 * the location.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 */
	public PTXor(int x, int y, int inputPortAmount) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, inputPortAmount);
	}

	/**
	 * Create an xor-gate from an amount of the inputs, two coordinates of the
	 * location, a width and a height.
	 * 
	 * @param inputPortAmount
	 *            an integer for the counter of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 * @param width
	 *            an integer for the width of the xor-gate.
	 * @param height
	 *            an integer for the height of the xor-gate.
	 */
	public PTXor(int x, int y, int width, int height, int inputPortAmount) {
		super(x, y, width, height, inputPortAmount);
		this.setObjectName("xor" + xorNr);
		xorNr++;
		elementSymbol.setText("=1");
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "Xor" };
	}

	public Object clone() {
		PTXor g = new PTXor();
		cloneCommonFeaturesInto(g);
		return g;
	}

	protected void cloneCommonFeaturesInto(PTXor gate) {
		super.cloneCommonFeaturesInto(gate);
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTXor.XOR_TYPE_LABEL;
	}

	public String toString() {
		return (toString(XOR_TYPE_LABEL));
	}

	/**
	 * returns the gate's object name, if the name was empty, create a new one.
	 * 
	 * @return the gate's object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("xor" + xorNr);
		return objectName;
	}
}
