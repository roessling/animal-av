package animal.vhdl.graphics;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import animal.graphics.PTPolygon;
import animal.graphics.PTText;

public class PTDemux extends PTMulti {

	/**
	 * The sequence number of a demux. It defines the default object name of a
	 * demux.
	 */
	private static int demuxNr = 0;

	/**
	 * The type name of this demux.
	 */
	public static final String DEMUX_TYPE_LABEL = "Demux";

	/**
	 * Create a default demux.
	 */
	public PTDemux() {
		this(2);
	}

	/**
	 * Create a demux from an amount of the inputs.
	 * 
	 * @param outputPortAmount
	 *            an integer for the amount of the inputs.
	 */
	public PTDemux(int outputPortAmount) {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y, outputPortAmount);
	}

	/**
	 * Create a demux from an amount of the inputs and two coordinates of the
	 * location.
	 * 
	 * @param outputPortAmount
	 *            an integer for the amount of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 */
	public PTDemux(int x, int y, int outputPortAmount) {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y, DEFAULT_HEIGHT, DEFAULT_WIDTH,
				outputPortAmount);
	}

	/**
	 * Create a demux from an amount of the inputs, two coordinates of the
	 * location, a width and a height.
	 * 
	 * @param outputPortAmount
	 *            an integer for the counter of the inputs.
	 * @param x
	 *            an integer for the x coordinate of the location.
	 * @param y
	 *            an integer for the y coordinate of the location.
	 * @param width
	 *            an integer for the width of the demux.
	 * @param height
	 *            an integer for the height of the demux.
	 */
	public PTDemux(int x, int y, int height, int width, int outputPortAmount) {

		elementBody = new PTPolygon();

		inputPins = new ArrayList<PTPin>(1);
		inputPins.add(new PTPin("in",true));
		outputPins = new ArrayList<PTPin>(outputPortAmount);
		for (int i = 0; i < outputPortAmount; i++) {
			outputPins.add(new PTPin("out" + i,false));
		}

		int controlPinAmount = getControlPinAmount(outputPortAmount);
		controlPins = new ArrayList<PTPin>(controlPinAmount);
		for (int i = 0; i < controlPinAmount; i++)
			controlPins.add(new PTPin("s" + i,true));

		elementSymbol = new PTText();
		elementSymbol.setText(outputPortAmount + "-" + getType());

		selText = new PTText();
		selText.setText("SEL");

		this.setObjectName("demux" + demuxNr);
		demuxNr++;

		initializeWithDefaults(getType());
		setStartNode(new Point(x, y));
		setHeight(height);
		setWidth(width);
	}

	public void paint(Graphics g) {

		setStartNode(getStartNode());
		setWidth(width);
		setHeight(height);

		// demux body
		elementBody.setFilled(isFilled());
		if (isFilled()) {
			elementBody.setFillColor(fillColor);
		}
		elementBody.setColor(color);
		elementBody.paint(g);

		// input pin
		inputPins.get(0).setColor(elementBody.getColor());
		inputPins.get(0).paint(g);

		// output pin
		for (int i = 0; i < outputPins.size(); i++) {
			outputPins.get(i).setColor(elementBody.getColor());
			outputPins.get(i).paint(g);
		}
		// selection pin
		for (PTPin pin : controlPins) {
			pin.setColor(elementBody.getColor());
			pin.paint(g);
		}
		// demux symbol
		elementSymbol.setColor(color);

		elementSymbol.setText(getOutputPins().size() + "-" + getType());
		elementSymbol.paint(g);

		selText.setColor(color);
		selText.paint(g);
	}

	protected void changeSize() {

		int hFactor = 8;

		int x = getStartNode().x;
		int y = getStartNode().y;

		PTPolygon eleBody = (PTPolygon) getElementBody();

		eleBody.setNode(0, new Point(x + width / 6, y + height / hFactor));
		eleBody.setNode(1, new Point(x + width * 5 / 6, y));
		eleBody.setNode(2, new Point(x + width * 5 / 6, y + height));
		eleBody.setNode(3, new Point(x + width / 6, y + (hFactor - 1) * height
				/ hFactor));

		// InputWire
		inputPins.get(0).setFirstNode(x, y + height / 2);
		inputPins.get(0).setLastNode(x + width / 6, y + height / 2);

		// outputWire
		int outputDistance = height / (outputPins.size() + 1);
		for (int i = 0; i < outputPins.size(); i++) {
			outputPins.get(i).setFirstNode(x + width * 5 / 6, y + (i + 1) * outputDistance);
			outputPins.get(i).setLastNode(x + width,
					y + (i + 1) * outputDistance);
		}

		// Selector
		int controlDistance = width / 3 * 2 / (controlPins.size() + 1);
		for (int i = 0; i < controlPins.size(); i++) {
			int cX = eleBody.getNodes().get(2).getX() -controlDistance
					* (1 + i);
			controlPins.get(i).setFirstNode(cX, y + height);
			int cY = y + (hFactor * (controlPins.size() + 1) - i - 1) * height
					/ ((controlPins.size() + 1) * hFactor);
			controlPins.get(i).setLastNode(cX, cY);

		}
		// DemuxText
		int fontSize = Math.min(Math.abs(getWidth()), Math.abs(getHeight())) / 8;
		elementSymbol.setFont(new Font("Arial", 10, fontSize));
		// DemuxTextLocation
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

//		int selFS = fontSize / 2;
//		selText.setFont(new Font("Arial", 10, selFS));
		int selX = startNode.x + width / 2 - selText.getBoundingBox().width / 2;
		int selY = eleBody.getNodes().get(3).getY();
		if (height >= 0)
			selY -= selText.getBoundingBox().height;
		selText.setLocation(new Point(selX, selY));
	}

	public String[] handledKeywords() {
		return new String[] { "Demux" };
	}

	public String toString() {
		return (toString(PTDemux.DEMUX_TYPE_LABEL));
	}

	public Object clone() {
		PTDemux demux = new PTDemux();
		cloneCommonFeaturesInto(demux);
		return demux;
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTDemux.DEMUX_TYPE_LABEL;
	}

	/**
	 * returns the object name, if the name was empty, create a new one.
	 * 
	 * @return the object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("demux" + demuxNr);
		return objectName;
	}

}
