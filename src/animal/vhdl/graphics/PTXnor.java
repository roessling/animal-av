/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Point;

/**
 * This class implements a xnor gate.
 * 
 * @author p_li
 * @version 2.0
 * 
 *          apply to ANIMAL 3.0
 * 
 */
public final class PTXnor extends PTNGate {

	/**
	 * The sequence number of an xnor-gate. It defines the default object name
	 * of an xnor-gate.
	 */
	private static int xnorNr = 0;

	/**
	 * The type name of this gate.
	 */
	public static final String XNOR_TYPE_LABEL = "Xnor";

	/**
	 * Create a default xnor-gate.
	 */
	public PTXnor() {
		this(2);
	}

	/**
	 * Create an xnor-gate from an amount of the inputs.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 */
	public PTXnor(int inputPortAmount) {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y, inputPortAmount);
	}

	/**
	 * Create an xnor-gate from an amount of the inputs and a location.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 * @param location
	 *            a point for the location.
	 */
	public PTXnor(Point location, int inputPortAmount) {
		this(location.x, location.y, inputPortAmount);
	}

	/**
	 * Create an xnor-gate from an amount of the inputs and two coordinates of
	 * the location.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 */
	public PTXnor(int x, int y, int inputPortAmount) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, inputPortAmount);
	}

	/**
	 * Create an xnor-gate from an amount of the inputs, two coordinates of the
	 * location, a width and a height.
	 * 
	 * @param inputPortAmount
	 *            an integer for the counter of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 * @param width
	 *            an integer for the width of the xnor-gate.
	 * @param height
	 *            an integer for the height of the xnor-gate.
	 */
	public PTXnor(int x, int y, int width, int height, int inputPortAmount) {
		super(x, y, width, height, inputPortAmount);
		this.setObjectName("xnor" + xnorNr);
		xnorNr++;
		elementSymbol.setText("=1");
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "Xnor" };
	}

	public Object clone() {
		PTXnor g = new PTXnor();
		cloneCommonFeaturesInto(g);
		return g;
	}

	protected void cloneCommonFeaturesInto(PTXnor gate) {
		super.cloneCommonFeaturesInto(gate);
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTXnor.XNOR_TYPE_LABEL;
	}

	public String toString() {
		return (toString(XNOR_TYPE_LABEL));
	}

	/**
	 * returns the gate's object name, if the name was empty, create a new one.
	 * 
	 * @return the gate's object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("xnor" + xnorNr);
		return objectName;
	}
}
