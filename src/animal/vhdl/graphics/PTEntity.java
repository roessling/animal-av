/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import animal.graphics.PTRectangle;
import animal.graphics.PTText;

/**
 * class for generic vhdl-elements.
 * 
 * @author p_li
 * 
 * @version 2.0 
 * 
 * apply to ANIMAL 3.0
 * 
 */
public class PTEntity extends PTVHDLElement {

	public static final String ENTITY_TYPE_LABEL = "Entity";
	
	private static int entityNr;

	public PTEntity() {
		this(DEFAULT_LOCATION.x, DEFAULT_LOCATION.y, DEFAULT_WIDTH, DEFAULT_HEIGHT,
				"");
	}

	public PTEntity(int x, int y, int width, int height, String entityName) {
		elementBody = new PTRectangle();

		inputPins = new ArrayList<PTPin>();
		outputPins = new ArrayList<PTPin>();
		inoutPins = new ArrayList<PTPin>();
		controlPins = new ArrayList<PTPin>();

		elementSymbol = new PTText();
		elementSymbol.setText(entityName);

		initializeWithDefaults(getType());
		setStartNode(new Point(x, y));
		setHeight(height);
		setWidth(width);

		this.setObjectName("entity" + entityNr);
		entityNr++;

	}

	public void paint(Graphics g) {
		setStartNode(getStartNode());
		setWidth(width);
		setHeight(height);

		// entity body
		elementBody.setFilled(isFilled());
		if (isFilled()) {
			elementBody.setFillColor(fillColor);
		}
		elementBody.setColor(color);
		elementBody.paint(g);

		// input pin
		if (inputPins != null)
			for (int i = 0; i < inputPins.size(); i++) {
				inputPins.get(i).setColor(elementBody.getColor());
				inputPins.get(i).paint(g);
			}
		// output pin
		if (outputPins != null)
			for (int i = 0; i < outputPins.size(); i++) {
				outputPins.get(i).setColor(elementBody.getColor());
				outputPins.get(i).paint(g);
			}
		// inoutput pin
		if (inoutPins != null)
			for (int i = 0; i < inoutPins.size(); i++) {
				inoutPins.get(i).setColor(elementBody.getColor());
				inoutPins.get(i).paint(g);
			}
		// control pin
		if (controlPins != null)
			for (int i = 0; i < controlPins.size(); i++) {
				controlPins.get(i).setColor(elementBody.getColor());
				controlPins.get(i).paint(g);
			}

		// entity symbol
		elementSymbol.setColor(color);
		elementSymbol.paint(g);
	}

	public void setHeight(int newHeight) {
		height = newHeight;
		changeSize();
	}

	public void setWidth(int newWidth) {
		width = newWidth;
		changeSize();
	}

	protected void changeSize() {
		PTRectangle eleBody = (PTRectangle) getElementBody();
		// element body size
		eleBody.setHeight(height * 5 / 6);
		eleBody.setWidth(width * 2 / 3);
		eleBody.setStartNode(getStartNode().x + width / 6, getStartNode().y
				+ height / 6);
		// input & in-output location
		if (inputPins != null || inoutPins != null) {
			int inCounter = 0;
			int inoutCounter = 0;
			if (inputPins != null)
				inCounter = inputPins.size();
			if (inoutPins != null)
				inoutCounter = inoutPins.size();
			if (inCounter + inoutCounter > 0) {
				int inputPortDistance = eleBody.getHeight()
						/ (inCounter + inoutCounter + 1);

				for (int i = 0; i < inCounter; i++) {
					inputPins.get(i).setFirstNode(
							eleBody.getStartNode().x - eleBody.getWidth() / 4,
							eleBody.getStartNode().y + (i + 1)
									* inputPortDistance+PORT_DIFFERENT);
					inputPins.get(i).setLastNode(
							eleBody.getStartNode().x,
							eleBody.getStartNode().y + (i + 1)
									* inputPortDistance+PORT_DIFFERENT);
				}
				
				for (int i = inCounter; i < inCounter + inoutCounter; i++) {
					inoutPins.get(i - inCounter).setLastNode(
							eleBody.getStartNode().x - eleBody.getWidth() / 4,
							eleBody.getStartNode().y + (i + 1)
									* inputPortDistance+PORT_DIFFERENT);
					inoutPins.get(i - inCounter).setFirstNode(
							eleBody.getStartNode().x,
							eleBody.getStartNode().y + (i + 1)
									* inputPortDistance+PORT_DIFFERENT);
				}
			}

		}
		// output location
		if (outputPins != null && outputPins.size() > 0) {
			int outPortDistance = eleBody.getHeight() / (outputPins.size() + 1);
			for (int i = 0; i < outputPins.size(); i++) {
				outputPins.get(i).setFirstNode(
						eleBody.getStartNode().x + eleBody.getWidth(),
						eleBody.getStartNode().y + (i + 1) * outPortDistance);
				outputPins.get(i).setLastNode(
						eleBody.getStartNode().x + 5 * eleBody.getWidth() / 4,
						eleBody.getStartNode().y + (i + 1) * outPortDistance);
			}
		}
		// control pin location
		if (controlPins != null && controlPins.size() > 0) {
			int controlPortDistance = eleBody.getWidth()
					/ (controlPins.size() + 1);
			for (int i = 0; i < controlPins.size(); i++) {
				controlPins.get(i).setFirstNode(
						eleBody.getStartNode().x + (i + 1)
								* controlPortDistance,
						eleBody.getStartNode().y - eleBody.getHeight() / 5);
				controlPins.get(i)
						.setLastNode(
								eleBody.getStartNode().x + (i + 1)
										* controlPortDistance,
								eleBody.getStartNode().y);
			}
		}

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
		return new String[] { "Entity" };
	}

	public String toString() {
		String result = toString("Entity-" + this.getElementSymbol().getText());
		if (inoutPins != null) {
			result += "output ";
			for (PTPin pin : outputPins) {
				result += pin.getPinName() + " ";
			}
		}
		return result;

	}

	public Object clone() {
		PTEntity e = new PTEntity();
		cloneCommonFeaturesInto(e);
		return e;
	}

	@SuppressWarnings("unchecked")
	protected void cloneCommonFeaturesInto(PTEntity targetElement) {
		super.cloneCommonFeaturesInto(targetElement);
		targetElement.setElementBody((PTRectangle) getElementBody().clone());
		if (inoutPins != null) {
			ArrayList<PTPin> newIOPin = new ArrayList<PTPin>();
			newIOPin = (ArrayList<PTPin>) inoutPins.clone();
			targetElement.setInoutPins(newIOPin);
		}
	}

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public void setSize(Dimension newSize) {
		if (newSize == null) {
			width = 0;
			height = 0;
		} else {
			setWidth(newSize.width);
			setHeight(newSize.height);
		}
	}

	public void translate(int deltaX, int deltaY) {
		getStartNode().translate(deltaX, deltaY);
	}

	/**
	 * @return the inoutPin
	 */
	public ArrayList<PTPin> getInoutPins() {
		return inoutPins;
	}

	/**
	 * @param inoutPin
	 *            the inoutPin to set
	 */
	public void setInoutPins(ArrayList<PTPin> inoutPin) {
		this.inoutPins = inoutPin;
	}

	/**
	 * @return the tYPE_LABEL
	 */
	public String getType() {
		return PTEntity.ENTITY_TYPE_LABEL;
	}

	/**
	 * returns the entity's object name, if the name was empty, create a new
	 * one.
	 * 
	 * @return the entity's object's name
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			setObjectName("entity" + entityNr);
		return objectName;
	}

}
