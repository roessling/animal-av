/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Graphics;

/**
 * @author p_li
 * 
 */
public final class PTRS extends PTFlipFlop {
	private static int rsNr = 0;
	public static final String RS_FLIPFLOP_TYPE_LABEL = "RS";

	public PTRS() {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y);
	}

	public PTRS(int x, int y) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public PTRS(int x, int y, int width, int height) {
		super(x, y, width, height, "S", "R");
		this.setObjectName("rs" + rsNr);
		rsNr++;
	}

	public void paint(Graphics g) {
		super.paint(g);
		// input pin
		inputPins.get(0).setColor(elementBody.getColor());
		inputPins.get(0).paint(g);
		inputPins.get(1).setColor(elementBody.getColor());
		inputPins.get(1).paint(g);
		// output pin
		for (int i = 0; i < outputPins.size(); i++) {
			outputPins.get(i).setColor(elementBody.getColor());
			outputPins.get(i).paint(g);
		}
	}

	public Object clone() {
		PTRS ff = new PTRS();
		cloneCommonFeaturesInto(ff);
		return ff;
	}

	protected void cloneCommonFeaturesInto(PTRS ff) {
		super.cloneCommonFeaturesInto(ff);
		ff.setObjectName(getObjectName());
	}

	/**
	 * @return the rsNr
	 */
	public static int getRsNr() {
		return rsNr;
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTRS.RS_FLIPFLOP_TYPE_LABEL;
	}

	/**
	 * returns the flip flop's object name, if the name was empty, create a new
	 * one.
	 * 
	 * @return the flip flop's object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("rs" + rsNr);
		return objectName;
	}

}
