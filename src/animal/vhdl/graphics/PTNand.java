/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Point;

/**
 * This class implements a nand gate.
 * 
 * @author p_li
 * @version 2.0
 * 
 *          apply to ANIMAL 3.0
 * 
 */
public final class PTNand extends PTNGate {

	/**
	 * The sequence number of an nand-gate. It defines the default object name
	 * of an nand-gate.
	 */
	private static int nandNr = 0;

	/**
	 * The type name of this gate.
	 */
	public static final String NAND_TYPE_LABEL = "Nand";

	/**
	 * Create a default nand-gate.
	 */
	public PTNand() {
		this(2);
	}

	/**
	 * Create an nand-gate from an amount of the inputs.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 */
	public PTNand(int inputPortAmount) {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y, inputPortAmount);
	}

	/**
	 * Create an nand-gate from an amount of the inputs and a location.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 * @param location
	 *            a point for the location.
	 */
	public PTNand(Point location, int inputPortAmount) {
		this(location.x, location.y, inputPortAmount);
	}

	/**
	 * Create an nand-gate from an amount of the inputs and two coordinates of
	 * the location.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 */
	public PTNand(int x, int y, int inputPortAmount) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, inputPortAmount);
	}

	/**
	 * Create an nand-gate from an amount of the inputs, two coordinates of the
	 * location, a width and a height.
	 * 
	 * @param inputPortAmount
	 *            an integer for the counter of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 * @param width
	 *            an integer for the width of the nand-gate.
	 * @param height
	 *            an integer for the height of the nand-gate.
	 */
	public PTNand(int x, int y, int width, int height, int inputPortAmount) {
		super(x, y, width, height, inputPortAmount);
		this.setObjectName("nand" + nandNr);
		nandNr++;
		elementSymbol.setText("&");
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public String[] handledKeywords() {
		return new String[] { "Nand" };
	}

	public Object clone() {
		PTNand g = new PTNand();
		cloneCommonFeaturesInto(g);
		return g;
	}

	protected void cloneCommonFeaturesInto(PTNand gate) {
		super.cloneCommonFeaturesInto(gate);
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTNand.NAND_TYPE_LABEL;
	}

	public String toString() {
		return (toString(PTNand.NAND_TYPE_LABEL));
	}

	/**
	 * returns the gate's object name, if the name was empty, create a new one.
	 * 
	 * @return the gate's object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("nand" + nandNr);
		return objectName;
	}
}
