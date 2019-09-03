/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import animal.animator.MoveBase;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTText;
import animal.graphics.meta.FillablePrimitive;
import animal.graphics.meta.PolygonalShape;
import animal.main.AnimalConfiguration;
import animal.misc.XProperties;

/**
 * Base class for all graphic vhdl-elements.
 * 
 * @author p_li
 * 
 * @version 2.0
 * 
 *          apply to ANIMAL 3.0
 * 
 */
public abstract class PTVHDLElement extends PTGraphicObject
implements FillablePrimitive, MoveBase {

	/**
	 * the main body of the VHDL element. a Rectangle for gate, a square for
	 * flip flop and a isosceles trapezoid for mux/demux
	 */
	protected PolygonalShape elementBody;
	protected static final Point DEFAULT_LOCATION = new Point(3, 3);
	protected final static int DEFAULT_HEIGHT = 6;
	protected final static int DEFAULT_WIDTH = 6;
	protected static final int PORT_DIFFERENT = 3;
	protected ArrayList<PTPin> inputPins;
	protected ArrayList<PTPin> outputPins;
	protected ArrayList<PTPin> inoutPins;
	protected int CodeLineNumberBegin;
	protected int CodeLineNumberEnd;

	protected Color defaultColor;
	protected Color defaultFillColor;

	/**
	 * for flip flop and mux/demux,
	 */
	protected ArrayList<PTPin> controlPins;

	/**
	 * the top left corner of the element in Animation
	 */
	protected Point animationLocation;// for Script
	/**
	 * the height of the element's bounding box, not element body's height
	 */
	protected int height;
	/**
	 * the width of the element's bounding box, not element body's width
	 */
	protected int width;

	/**
	 * the top left corner of the element's bounding box
	 */
	protected Point startNode = null;

	/**
	 * an auxiliary attribute to creating correct script, defined by analyzing
	 * VHDL codes 0 for total entity, 1 for component, 2 for base element, 3 for
	 * wire between black box and white box
	 * 
	 */
	protected byte entityType;

	/**
	 * the filled color of element's body
	 */
	protected Color fillColor;

	/**
	 * if element body is filled (default: false)
	 */
	protected boolean isFilled = false;

	protected PTText elementSymbol;
	
	public abstract void paint(Graphics g);

	/**
	 * Return the bounding box for the element.
	 * 
	 * @return the bounding box of the element
	 */
	public Rectangle getBoundingBox() {
		int x = startNode.x;
		int y = startNode.y;
		int w = width;
		int h = height;
		if (w < 0) {
			x += width;
			w = -w;
		}
		if (h < 0) {
			y += height;
			h = -h;
		}

		Rectangle boundingBox = new Rectangle(x, y, w, h);
		return boundingBox;
	}

	public void rotate(double angle) {
		elementBody.rotate(angle);
		if (inputPins != null) {
			for (PTPin p : inputPins) {
				p.rotate(angle);
			}
		}
		if (outputPins != null) {
			for (PTPin p : outputPins) {
				p.rotate(angle);
			}
		}
		if (controlPins != null) {
			for (PTPin p : controlPins) {
				p.rotate(angle);
			}
		}
		if (elementSymbol != null)
			elementSymbol.rotate(angle);
	}

	public void rotate(double angle, PTPoint center) {
		elementBody.rotate(angle, center);
		if (inputPins != null) {
			for (PTPin p : inputPins) {
				p.rotate(angle, center);
			}
		}
		if (outputPins != null) {
			for (PTPin p : outputPins) {
				p.rotate(angle, center);
			}
		}
		if (controlPins != null) {
			for (PTPin p : controlPins) {
				p.rotate(angle, center);
			}
		}
		if (elementSymbol != null)
			elementSymbol.rotate(angle, center);
	}

	public void scale(double scaleX, double scaleY) {
		elementBody.scale(scaleX, scaleY);
		if (inputPins != null) {
			for (PTPin p : inputPins) {
				p.scale(scaleX, scaleY);
			}
		}
		if (outputPins != null) {
			for (PTPin p : outputPins) {
				p.scale(scaleX, scaleY);
			}
		}
		if (controlPins != null) {
			for (PTPin p : controlPins) {
				p.scale(scaleX, scaleY);
			}
		}
		if (elementSymbol != null)
			elementSymbol.scale(scaleX, scaleY);
	}

	public void translate(int deltaX, int deltaY) {
		getStartNode().translate(deltaX, deltaY);
//		elementBody.translate(deltaX, deltaY);
//		if (inputPin != null) {
//			for (PTPin p : inputPin) {
//				p.translate(deltaX, deltaY);
//			}
//		}
//		if (outputPin != null) {
//			for (PTPin p : outputPin) {
//				p.translate(deltaX, deltaY);
//			}
//		}
//		if (controlPin != null) {
//			for (PTPin p : controlPin) {
//				p.translate(deltaX, deltaY);
//			}
//		}
//		if (elementSymbol != null)
//			elementSymbol.translate(deltaX, deltaY);
	}

	/**
	 * clone the first node, height, width, in/out/control-put pins into
	 * targetElement the element's body is not cloned here
	 * 
	 * @param targetElement
	 */
	@SuppressWarnings("unchecked")
  protected void cloneCommonFeaturesInto(PTVHDLElement targetElement) {
		// clone features from PTGraphicsObject: color, depth, num, objectName
		super.cloneCommonFeaturesInto(targetElement);

		targetElement.setStartNode(new Point(startNode));
		targetElement.setHeight(height);
		targetElement.setWidth(width);
		// use arraylist.clone()
		if (inputPins != null) {
			ArrayList<PTPin> newInputPin = new ArrayList<PTPin>();
			newInputPin = (ArrayList<PTPin>) inputPins.clone();
			targetElement.setInputPins(newInputPin);
		}
		
		// manual clone
		if (outputPins != null) {
			// ArrayList<PTPin> newOutputPin= new ArrayList<PTPin>();
			// newOutputPin=(ArrayList<PTPin>) outputPin.clone();
			// targetElement.setOutputPin(newOutputPin);
			int outpupPinCounter = getOutputPins().size();
			ArrayList<PTPin> newOutputPin = new ArrayList<PTPin>(
					outpupPinCounter);
			for (int i = 0; i < outpupPinCounter; i++) {
				PTPin pin = getOutputPins().get(i);
				PTPoint firstNode = pin.getFirstNode();
				PTPoint lastNode = pin.getLastNode();
				PTPin newPin = new PTPin(firstNode.getX(), firstNode.getY(),
						lastNode.getX(), lastNode.getY(),true);
				newPin.setPinName(pin.getPinName());
				newPin.setPinValue(pin.getPinValue());
				newOutputPin.add(newPin);
			}
			targetElement.setOutputPins(newOutputPin);
		}
		if (controlPins != null) {
			ArrayList<PTPin> newControlPin = new ArrayList<PTPin>();
			newControlPin = (ArrayList<PTPin>) controlPins.clone();
			targetElement.setControlPins(newControlPin);
		}
		targetElement.setFilled(isFilled());
		targetElement.setFillColor(createColor(getFillColor()));
		targetElement.setElementSymbol((PTText) elementSymbol.clone());
		targetElement.setDefaultColor(defaultColor);
		targetElement.setDefaultFillColor(defaultFillColor);
	}

	public void discard() {
		super.discard();
		elementBody.discard();
		inputPins.clear();
		inputPins = null;
		outputPins.clear();
		outputPins = null;
		controlPins.clear();
		controlPins = null;
		color = null;
		fillColor = null;
		elementSymbol.discard();
	}

	public void initializeWithDefaults(String primitiveName) {
		super.initializeWithDefaults(primitiveName);
		AnimalConfiguration config = AnimalConfiguration
				.getDefaultConfiguration();
		isFilled = config
				.getDefaultBooleanValue(primitiveName, "filled", false);
		fillColor = config.getDefaultColor(primitiveName, "fillColor",
				Color.BLUE);
	}

	public void updateDefaults(XProperties defaultProperties) {
		super.updateDefaults(defaultProperties);
		defaultProperties.put(getType() + ".fillColor", getFillColor());
		defaultProperties.put(getType() + ".filled", isFilled());
		defaultProperties.put(getType() + ".height", getHeight());
		defaultProperties.put(getType() + ".width", getWidth());
	}

	//
	public int getLength() {
		return elementBody.getLength();
	}

	//
	public Point getPointAtLength(int length) {

		return elementBody.getPointAtLength(length);
	}

	protected String toString(String elementType) {
		StringBuilder result = new StringBuilder(120);
		result.append(elementType).append(' ');
		if (getObjectName() != null)
			result.append("\"").append(getObjectName()).append("\" ");

		if (inputPins != null) {
			result.append("input ");
			for (PTPin pin : inputPins) {
				result.append(pin.getPinName()).append(" ");
			}
		}
		if (outputPins != null) {
			result.append("output ");
			for (PTPin pin : outputPins) {
				result.append(pin.getPinName()).append(" ");
			}
		}

		if (controlPins != null) {
			result.append("control ");
			for (PTPin pin : controlPins) {
				result.append(pin.getPinName()).append(" ");
			}
		}
		// return the string
		return result.toString();
	}

	public void useAsMoveBase() {
		setFilled(false);
	}

	/**
	 * @return the elementBody
	 */
	public PolygonalShape getElementBody() {
		return elementBody;
	}

	/**
	 * @param elementBody
	 *            the elementBody to set
	 */
	public void setElementBody(PolygonalShape elementBody) {
		this.elementBody = elementBody;
	}

	/**
	 * @return the inputPin
	 */
	public ArrayList<PTPin> getInputPins() {
		return inputPins;
	}

	/**
	 * @param inputPin
	 *            the inputPin to set
	 */
	public void setInputPins(ArrayList<PTPin> inputPin) {
		this.inputPins = inputPin;
	}

	/**
	 * @return the outputPin
	 */
	public ArrayList<PTPin> getOutputPins() {
		return outputPins;
	}

	/**
	 * @param outputPin
	 *            the outputPin to set
	 */
	public void setOutputPins(ArrayList<PTPin> outputPin) {
		this.outputPins = outputPin;
	}

	/**
	 * @return the controlPin
	 */
	public ArrayList<PTPin> getControlPins() {
		return controlPins;
	}

	/**
	 * @param controlPin
	 *            the controlPin to set
	 */
	public void setControlPins(ArrayList<PTPin> controlPin) {
		this.controlPins = controlPin;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
		changeSize();
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
		changeSize();
	}

	/**
	 * adjust the graph to new size
	 */

	protected abstract void changeSize();

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public void setSize(Dimension newSize) {
		if (newSize == null) {
			setWidth(0);
			setHeight(0);
		} else {
			setWidth(newSize.width);
			setHeight(newSize.height);
		}
	}

	/**
	 * @return the start Node
	 */
	public Point getStartNode() {
		return startNode;
	}

	/**
	 * @param firstNode
	 *            the start Node to set
	 */
	public void setStartNode(Point firstNode) {
		this.startNode = new Point(firstNode);
	}

	public void setStartNode(int x, int y) {
		setStartNode(new Point(x, y));
	}

	/**
	 * @return the fillColor
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * @param fillColor
	 *            the fillColor to set
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = new Color(fillColor.getRGB());
	}

	/**
	 * @return the isFilled
	 */
	public boolean isFilled() {
		return isFilled;
	}

	/**
	 * @param isFilled
	 *            the isFilled to set
	 */
	public void setFilled(boolean isFilled) {
		this.isFilled = isFilled;
	}

	/**
	 * @return the elementSymbol
	 */
	public PTText getElementSymbol() {
		return elementSymbol;
	}

	/**
	 * @param elementSymbol
	 *            the elementSymbol to set
	 */
	public void setElementSymbol(PTText elementSymbol) {
		this.elementSymbol = elementSymbol;
	}

	public void setElementSymbol(String elementSymbolText) {
		this.elementSymbol = new PTText(elementSymbolText, this.elementSymbol
				.getLocation());
	}

	/**
	 * @return the animationLocation
	 */
	public Point getAnimationLocation() {
		return animationLocation;
	}

	/**
	 * @param animationLocation
	 *            the animationLocation to set
	 */
	public void setAnimationLocation(Point animationLocation) {
		this.animationLocation = animationLocation;
	}

	/**
	 * @return the entityType
	 */
	public byte getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType
	 *            the entityType to set
	 */
	public void setEntityType(byte entityType) {
		this.entityType = entityType;
	}

	public int getCodeLineNumberBegin() {
		return CodeLineNumberBegin;
	}

	public void setCodeLineNumberBegin(int codeLineNumberBegin) {
		CodeLineNumberBegin = codeLineNumberBegin;
	}

	public int getCodeLineNumberEnd() {
		return CodeLineNumberEnd;
	}

	public void setCodeLineNumberEnd(int codeLineNumberEnd) {
		CodeLineNumberEnd = codeLineNumberEnd;
	}

	public ArrayList<PTPin> getInoutPins() {
		return inoutPins;
	}

	public void setInoutPins(ArrayList<PTPin> inoutPin) {
		this.inoutPins = inoutPin;
	}

	/**
	 * @return the defaultColor
	 */
	public Color getDefaultColor() {
		return defaultColor;
	}

	/**
	 * @param defaultColor
	 *            the defaultColor to set
	 */
	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}

	/**
	 * @return the defaultFillColor
	 */
	public Color getDefaultFillColor() {
		return defaultFillColor;
	}

	/**
	 * @param defaultFillColor
	 *            the defaultFillColor to set
	 */
	public void setDefaultFillColor(Color defaultFillColor) {
		this.defaultFillColor = defaultFillColor;
	}

}
