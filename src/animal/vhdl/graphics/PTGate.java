/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import animal.graphics.PTRectangle;
import animal.graphics.PTText;

/**
 * Base class for all graphic vhdl-gates.
 * 
 * @author p_li
 * 
 * @version 2.0 
 * 
 * apply to ANIMAL 3.0
 * 
 */
public abstract class PTGate extends PTVHDLElement {
//	public static final String TYPE_LABEL = "Gate";	
	public PTGate(int x, int y, int width, int height, int InputPortCount) {

		elementBody = new PTRectangle();
		inputPins = new ArrayList<PTPin>(InputPortCount);
		for (int i = 0; i < InputPortCount; i++) {
			inputPins.add(new PTPin("in" + i,true));
		}
		outputPins = new ArrayList<PTPin>(1);
		outputPins.add(new PTPin("out",false));

		elementSymbol = new PTText();
		elementSymbol.setText(getType());

		initializeWithDefaults(getType());
		setStartNode(new Point(x, y));
		setHeight(height);
		setWidth(width);
	}

	public void paint(Graphics g) {
		setStartNode(getStartNode());
		setWidth(width);
		setHeight(height);

		// gate body
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
		// gate symbol
		elementSymbol.setColor(color);
		elementSymbol.paint(g);
	}

	protected void changeSize() {
		PTRectangle eleBody = (PTRectangle) getElementBody();

		eleBody.setHeight(height);
		eleBody.setWidth(width * 2 / 3);
		eleBody.setStartNode(getStartNode().x + width / 6, getStartNode().y);

		int InputPortDistance = height / (inputPins.size() + 1);
		for (int i = 0; i < inputPins.size(); i++) {
			inputPins.get(i).setFirstNode(
					eleBody.getStartNode().x - eleBody.getWidth() / 4,
					eleBody.getStartNode().y + (i + 1) * InputPortDistance+PORT_DIFFERENT);
			inputPins.get(i).setLastNode(eleBody.getStartNode().x,
					eleBody.getStartNode().y + (i + 1) * InputPortDistance+PORT_DIFFERENT);
		}
		outputPins.get(0).setFirstNode(
				eleBody.getStartNode().x + eleBody.getWidth(),
				eleBody.getStartNode().y + eleBody.getHeight() / 2);
		outputPins.get(0).setLastNode(
				eleBody.getStartNode().x + 5 * eleBody.getWidth() / 4,
				eleBody.getStartNode().y + eleBody.getHeight() / 2);

		// GateSymbolLocation
		{
			int fontSize = Math.min(Math.abs(eleBody.getWidth()), Math
					.abs(eleBody.getHeight())) / 4;
			elementSymbol.setFont(new Font("Arial", 10, fontSize));
			int bodyX = eleBody.getStartNode().x;
			int bodyY = eleBody.getStartNode().y;
			int paintWidth = Math.abs(eleBody.getWidth());

			if (eleBody.getWidth() < 0)
				bodyX += eleBody.getWidth();
			if (eleBody.getHeight() < 0)
				bodyY += eleBody.getHeight();
			elementSymbol.setLocation(new Point(bodyX + paintWidth / 2
					- elementSymbol.getBoundingBox().width / 2, bodyY + 5));
		}
	}

	public String[] handledKeywords() {
		return new String[] { "Gate" };
	}
//
//	public String toString() {
//		return (toString(TYPE_LABEL));
//	}

	protected void cloneCommonFeaturesInto(PTVHDLElement targetElement) {
		super.cloneCommonFeaturesInto(targetElement);
		targetElement.setElementBody((PTRectangle) getElementBody().clone());
	}



}
