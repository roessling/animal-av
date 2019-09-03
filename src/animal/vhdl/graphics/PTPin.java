/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import animal.graphics.PTLine;
import animal.graphics.PTText;

/**
 * class for pins on graphic vhdl-elements.
 * 
 * @author p_li
 * 
 * @version 1.1
 * 
 */
public final class PTPin extends PTLine {

	/**
	 * the type label
	 */
	public static final String PIN_TYPE = "Pin";

	/**
	 * The name of this pin.
	 * 
	 * Different pins with the same name will be connected by a wire.
	 */
	private String pinName;

	/**
	 * The value through this pin.
	 */
	private char pinValue = ' ';

	/**
	 * Create a default pin without pin name.
	 */
	
	private boolean inOrOut=true;
	public boolean isInOrOut() {
		return inOrOut;
	}

	public void setInOrOut(boolean inOrOut) {
		this.inOrOut = inOrOut;
	}

//	public PTPin() {
//		this("",true);
//		// TODO Auto-generated constructor stub
//	}
    public PTPin(boolean inout){
    	this("",inout);
    }
	/**
	 * Create a pin from coordinates of the start and the end point.
	 * 
	 * @param sX
	 *            the x coordinate of the start point.
	 * @param sY
	 *            the y coordinate of the start point.
	 * @param eX
	 *            the x coordinate of the end point.
	 * @param eY
	 *            the y coordinate of the end point.
	 */
	public PTPin(int sX, int sY, int eX, int eY,boolean inout) {
		super(sX, sY, eX, eY);
		this.setFWArrow(false);
		this.setInOrOut(inout);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create a pin from a start point and an end point.
	 * 
	 * @param sNode
	 *            the start point.
	 * @param eNode
	 *            the end point.
	 */
	public PTPin(Point sNode, Point eNode,boolean inout) {
		super(sNode, eNode);
		this.setFWArrow(false);
		this.setInOrOut(inout);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create a pin from a pin name with default location.
	 * 
	 * @param pinName
	 *            the pin name of this pin
	 */
	public PTPin(String pinName,boolean inout) {
		super();
		setPinName(pinName);
		this.setFWArrow(false);
		this.setInOrOut(inout);
	}

	/**
	 * return the pin name
	 * 
	 * @return the pin name
	 */
	public String getPinName() {
		if (pinName == null)
			return "";
		return pinName;
	}

	/**
	 * @param pinName
	 */
	public void setPinName(String pinName) {
		this.pinName = pinName;
	}

	public void paint(Graphics g) {
		super.paint(g);
		int fontSize = Math.max(getPinLength() / 4, 12);
		Font wireNameFont = new Font("Arial", Font.PLAIN, fontSize);
		PTText pinText = new PTText();
		pinText.setFont(wireNameFont);		
		String text = (getPinName() != null && getPinName() != "" && getPinValue() != ' ') ? getPinName()
				+ "=" + getPinValue()
				: getPinName() + getPinValue();
		pinText.setText(text);
		
		//pinText Location
//		int pinTextX;
//		int pinTextY;
		if(getFirstNode().getX()<getLastNode().getX());
//			pinTextX
		int posX=5;	
		if(this.isInOrOut())
			posX=posX+getLastNode().getX();
		else
			posX=posX+getFirstNode().getX();
		pinText.setLocation(new Point(posX
				, (this.getFirstNode().getY() + this
				.getLastNode().getY()) / 2));
		pinText.setColor(getColor());
		pinText.paint(g);
	}

	/**
	 * return the pin's absolute length.
	 * 
	 * @return the pinLenght
	 */

	public int getPinLength() {
		int XLenght = getFirstNode().getX() - getLastNode().getX();
		int YLenght = getFirstNode().getY() - getLastNode().getY();
		int lenght = Math.abs((XLenght ^ 2 + YLenght ^ 2) ^ (-2));
		return lenght;
	}

	/**
	 * return the pin value, if the value wasn't applied, return ' '
	 * 
	 * @return the pinValue
	 */
	public char getPinValue() {
		if (!isLegalPinValue(pinValue))
			return ' ';
		return pinValue;
	}

	private boolean isLegalPinValue(char value) {
		if (value == ' ' || value == '0' || value == '1' || value == 'x'
				|| value == 'z')
			return true;
		return false;
	}

	/**
	 * @param pinValue
	 *            the pinValue to set
	 */
	public void setPinValue(char pinValue) {
		if (isLegalPinValue(pinValue)) {
			this.pinValue = pinValue;
		} else {
			this.pinValue = ' ';
		}
	}

	/**
	 * returns the type of this object
	 * 
	 * @return the type of the object
	 */
	public String getType() {
		return PTPin.PIN_TYPE;
	}

	protected void cloneCommonFeaturesInto(PTPin targetShape) {
		// clone features from PTGraphicsObject: color, depth, num, objectName
		// from OpenLineBasedShape: hasBWArrow, hasFWArrow
		// from PTLine: firstNode, lastNode
		super.cloneCommonFeaturesInto(targetShape);

		targetShape.setPinName(new String(getPinName()));
		targetShape.setPinValue(getPinValue());

	}

	public Object clone() {
		PTPin targetShape = new PTPin(true);
		cloneCommonFeaturesInto(targetShape);
		return targetShape;
	}

}
