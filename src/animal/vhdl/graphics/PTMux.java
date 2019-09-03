package animal.vhdl.graphics;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import animal.graphics.PTPolygon;
import animal.graphics.PTText;

public class PTMux extends PTMulti {

	/**
	 * The sequence number of a mux. It defines the default object name of a
	 * mux.
	 */
	private static int muxNr = 0;

	/**
	 * The type name of this mux.
	 */
	public static final String MUX_TYPE_LABEL = "Mux";

	/**
	 * Create a default mux.
	 */
	public PTMux() {
		this(2);
	}

	/**
	 * Create a mux from an amount of the inputs.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 */
	public PTMux(int inputPortAmount) {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y, inputPortAmount);
	}

	/**
	 * Create a mux from an amount of the inputs and two coordinates of the
	 * location.
	 * 
	 * @param inputPortAmount
	 *            an integer for the amount of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 */
	public PTMux(int x, int y, int inputPortAmount) {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y, DEFAULT_HEIGHT, DEFAULT_WIDTH,
				inputPortAmount);
	}

	/**
	 * Create a mux from an amount of the inputs, two coordinates of the
	 * location, a width and a height.
	 * 
	 * @param inputPortAmount
	 *            an integer for the counter of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 * @param width
	 *            an integer for the width of the mux.
	 * @param height
	 *            an integer for the height of the mux.
	 */
	public PTMux(int x, int y, int height, int width, int inputPortAmount) {

		elementBody = new PTPolygon();

		inputPins = new ArrayList<PTPin>(inputPortAmount);
		for (int i = 0; i < inputPortAmount; i++) {
			inputPins.add(new PTPin("in" + i,true));
		}
		outputPins = new ArrayList<PTPin>(1);
		outputPins.add(new PTPin("out",false));

		int controlPinAmount = getControlPinAmount(inputPortAmount);
		controlPins = new ArrayList<PTPin>(controlPinAmount);
		for (int i = 0; i < controlPinAmount; i++)
			controlPins.add(new PTPin("s" + i,true));

		elementSymbol = new PTText();
		elementSymbol.setText(getInputPins().size() + "-" + getType());

		selText = new PTText();
		selText.setText("SEL");

		this.setObjectName("mux" + muxNr);
		muxNr++;

		initializeWithDefaults(getType());
		setStartNode(new Point(x, y));
		setHeight(height);
		setWidth(width);
	}

	public void paint(Graphics g) {

		setStartNode(getStartNode());
		setWidth(width);
		setHeight(height);

		// mux body
		elementBody.setFilled(isFilled());
		if (isFilled()) {
			elementBody.setFillColor(fillColor);
		}
		elementBody.setColor(color);
		elementBody.paint(g);

		// input pin
		for (int i = 0; i < inputPins.size(); i++) {
			inputPins.get(i).setColor(elementBody.getColor());
			inputPins.get(i).paint(g);
		}
		// output pin
		outputPins.get(0).setColor(elementBody.getColor());
		outputPins.get(0).paint(g);

		// selection pin
		for (PTPin pin : controlPins) {
			pin.setColor(elementBody.getColor());
			pin.paint(g);
		}
		// mux symbol
		elementSymbol.setColor(color);

		elementSymbol.setText(getInputPins().size() + "-" + getType());
		
		elementSymbol.paint(g);

		selText.setColor(color);
		selText.paint(g);
	}

	protected void changeSize() {

		int hFactor = 8;

		int x = getStartNode().x;
		int y = getStartNode().y;

		PTPolygon eleBody = (PTPolygon) getElementBody();

		eleBody.setNode(0, new Point(x + width / 6, y));
		eleBody.setNode(1, new Point(x + width * 5 / 6, y + height / hFactor));
		eleBody.setNode(2, new Point(x + width * 5 / 6, y + (hFactor - 1)
				* height / hFactor));
		eleBody.setNode(3, new Point(x + width / 6, y + height));

		// inputWire
		int inputDistance = height / (inputPins.size() + 1);
		for (int i = 0; i < inputPins.size(); i++) {
			inputPins.get(i).setFirstNode(x, y + (i + 1) * inputDistance);
			inputPins.get(i).setLastNode(x + width / 6,
					y + (i + 1) * inputDistance);
		}

		// OutputWire
		if (outputPins.size() > 0) {
		  outputPins.get(0).setFirstNode(x + width * 5 / 6, y + height / 2);
		  outputPins.get(0).setLastNode(x + width, y + height / 2);
		}
		// Selector
		int controlDistance = width / 3 * 2 / (controlPins.size() + 1);
		for (int i = 0; i < controlPins.size(); i++) {
			int cX = eleBody.getNodes().get(2).getX() - controlDistance
					* (1 + i);
			controlPins.get(i).setFirstNode(cX, y + height);
			int cY = y
					+ (hFactor * (controlPins.size() + 1) - controlPins.size() + i)
					* height / ((controlPins.size() + 1) * hFactor);
			controlPins.get(i).setLastNode(cX, cY);

		}
		// MuxText
		int fontSize = Math.min(Math.abs(getWidth()), Math.abs(getHeight())) / 5;
		elementSymbol.setFont(new Font("Arial", 10, fontSize));
		// MuxTextLocation
		{
			int bodyX = getStartNode().x;
			int bodyY = getStartNode().y;
			int paintWidth = Math.abs(width), paintHeight = Math.abs(height);
			if (width < 0)
				bodyX += width;
			if (height < 0)
				bodyY += height;
			elementSymbol.setLocation(new Point(bodyX + paintWidth / 2
					- elementSymbol.getBoundingBox().width / 2, bodyY
					+ paintHeight / 2 - elementSymbol.getBoundingBox().height));
		}

		int selX = startNode.x + width / 2 - selText.getBoundingBox().width / 2;
		int selY = eleBody.getNodes().get(2).getY();
		if (height >= 0)
			selY -= selText.getBoundingBox().height;
		selText.setLocation(new Point(selX, selY));
	}

	public String[] handledKeywords() {
		return new String[] { "Mux" };
	}

	public String toString() {
		return (toString(PTMux.MUX_TYPE_LABEL));
	}

	public Object clone() {
		PTMux mux = new PTMux();
		cloneCommonFeaturesInto(mux);
		return mux;
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTMux.MUX_TYPE_LABEL;
	}

	/**
	 * returns the object name, if the name was empty, create a new one.
	 * 
	 * @return the object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("mux" + muxNr);
		return objectName;
	}

}
