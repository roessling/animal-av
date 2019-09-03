/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Graphics;

/**
 * @author p_li
 * 
 */
public final class PTJK extends PTFlipFlop {
	private static int jkNr = 0;
	public static final String JK_FLIPFLOP_TYPE_LABEL = "JK";

	public PTJK() {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y);
	}

	public PTJK(int x, int y) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public PTJK(int x, int y, int width, int height) {
		super(x, y, width, height, "K", "J");
		this.setObjectName("jk" + jkNr);
		jkNr++;
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
		PTJK ff = new PTJK();
		cloneCommonFeaturesInto(ff);
		return ff;
	}

	protected void cloneCommonFeaturesInto(PTJK ff) {
		super.cloneCommonFeaturesInto(ff);
		ff.setObjectName(getObjectName());
	}

	/**
	 * @return the jkNr
	 */
	public static int getRsNr() {
		return jkNr;
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTJK.JK_FLIPFLOP_TYPE_LABEL;
	}

	/**
	 * returns the flip flop's object name, if the name was empty, create a new
	 * one.
	 * 
	 * @return the flip flop's object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("jk" + jkNr);
		return objectName;
	}

}
