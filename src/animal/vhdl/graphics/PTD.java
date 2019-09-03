/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Graphics;

/**
 * @author p_li
 * 
 */
public class PTD extends PTFlipFlop {

	private static int dNr = 0;
	public static final String D_FLIPFLOP_TYPE_LABEL = "D";

	public PTD() {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y);
	}

	public PTD(int x, int y) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public PTD(int x, int y, int width, int height) {
		super(x, y, width, height, "      D", null);
		this.setObjectName("d" + dNr);
		dNr++;
	}

	public void paint(Graphics g) {
		super.paint(g);
		// input pin
		inputPins.get(0).setColor(elementBody.getColor());
		inputPins.get(0).paint(g);
		// output pin
		outputPins.get(0).setColor(elementBody.getColor());
		outputPins.get(0).paint(g);

	}

	public Object clone() {
		PTD ff = new PTD();
		cloneCommonFeaturesInto(ff);
		return ff;
	}

	protected void cloneCommonFeaturesInto(PTRS ff) {
		super.cloneCommonFeaturesInto(ff);
		ff.setObjectName(getObjectName());
	}

	/**
	 * @return the dNr
	 */
	public static int getDNr() {
		return dNr;
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTD.D_FLIPFLOP_TYPE_LABEL;
	}

	/**
	 * returns the flip flop's object name, if the name was empty, create a new
	 * one.
	 * 
	 * @return the flip flop's object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("d" + dNr);
		return objectName;
	}

}
