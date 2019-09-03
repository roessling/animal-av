/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Graphics;

/**
 * @author p_li
 * 
 */
public class PTT extends PTFlipFlop {

	private static int tNr = 0;
	public static final String T_FLIPFLOP_TYPE_LABEL = "T";

	public PTT() {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y);
	}

	public PTT(int x, int y) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public PTT(int x, int y, int width, int height) {
		super(x, y, width, height, "T", null);
		this.setObjectName("t" + tNr);
		tNr++;
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
		PTT ff = new PTT();
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
		return tNr;
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTT.T_FLIPFLOP_TYPE_LABEL;
	}

	/**
	 * returns the flip flop's object name, if the name was empty, create a new
	 * one.
	 * 
	 * @return the flip flop's object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("t" + tNr);
		return objectName;
	}

}
