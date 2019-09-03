/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import animal.graphics.PTPoint;
import animal.graphics.PTRectangle;
import animal.graphics.PTText;

/**
 * @author p_li
 * 
 */
public abstract class PTFlipFlop extends PTVHDLElement {
	protected boolean synControl = false;
	protected boolean asynSR = false;
//	public static final String TYPE_LABEL = "FlipFlop";

	protected PTText textR = new PTText();
	protected PTText textS = new PTText();
	protected PTText textClk = new PTText();
	protected PTText textCE = new PTText();
	protected PTText textRD = new PTText();
	protected PTText textSD = new PTText();
	protected PTText textQ = new PTText();
	protected PTText textInvQ = new PTText();

	protected boolean dualIO = false;

	public PTFlipFlop(int x, int y, int width, int height, String set) {
		this(x, y, width, height, set, null);
	}

	public PTFlipFlop(int x, int y, int width, int height, String set,
			String res) {
		elementBody = new PTRectangle();
		// 2 input(RS and JK flip flop) or 1 input(T and D flip flop)
		inputPins = new ArrayList<PTPin>(2);
		inputPins.add(new PTPin("in" + set,true));
		// output Q and ^Q
		outputPins = new ArrayList<PTPin>(2);
		outputPins.add(new PTPin("outQ",false));
		if (res != null && res != "") {
			inputPins.add(new PTPin("in" + res,true));
			outputPins.add(new PTPin("out^Q",false));
			textR.setText(" "+res+" ");
			textInvQ.setText(" ^Q ");
			dualIO = true;
		}
		
		
		// control pins: synchronous control and asynchronous set/reset.
		controlPins = new ArrayList<PTPin>(4);
		controlPins.add(new PTPin("cSD",true));
		controlPins.add(new PTPin("cCE",true));
		controlPins.add(new PTPin("cClk",true));
		controlPins.add(new PTPin("cRD",true));
		// element symbol is empty, and
		elementSymbol = new PTText();
		// 8 PTText to indicate the type of the flip flop

		textS.setText(" "+set+" ");
		textRD.setText("CE");
		textSD.setText("SD");
		textClk.setText("Clk");
		textCE.setText("RD");
		textQ.setText(" Q ");

		initializeWithDefaults(getType());
		setStartNode(new Point(x, y));
		setHeight(height);
		setWidth(width);
	}

	public void paint(Graphics g) {
		setStartNode(getStartNode());
		int ffSize = Math.max(Math.abs(width), Math.abs(height));
		if (width != 0 && height != 0) {
			width = Math.abs(width) / width * ffSize;
			height = Math.abs(height) / height * ffSize;
		}

		setWidth(width);
		setHeight(height);

		// gate body
		elementBody.setFilled(isFilled());
		if (isFilled()) {
			elementBody.setFillColor(fillColor);
		}
		elementBody.setColor(color);
		elementBody.paint(g);

		// control pin
		if (synControl) {// Clk and CE
			controlPins.get(2).setColor(elementBody.getColor());
			controlPins.get(2).paint(g);
			controlPins.get(3).setColor(elementBody.getColor());
			controlPins.get(3).paint(g);
		}
		if (asynSR) {// RD and SD
			controlPins.get(0).setColor(elementBody.getColor());
			controlPins.get(0).paint(g);
			controlPins.get(1).setColor(elementBody.getColor());
			controlPins.get(1).paint(g);
		}

		// 8 indications
		textR.setColor(color);
		textR.paint(g);
		textS.setColor(color);
		textS.paint(g);

		textQ.setColor(color);
		textQ.paint(g);
		textInvQ.setColor(color);
		textInvQ.paint(g);

		if (asynSR) {
			textRD.setColor(color);
			textRD.paint(g);
			textSD.setColor(color);
			textSD.paint(g);
		}

		if (synControl) {
			textClk.setColor(color);
			textClk.paint(g);
			textCE.setColor(color);
			textCE.paint(g);
		}
	}

	protected void changeSize() {
		PTRectangle eleBody = (PTRectangle) getElementBody();
		int size = Math.max(Math.abs(width), Math.abs(height));
		int pHeight;
		int pWidth;
		if (height < 0)
			pHeight = -size;
		else
			pHeight = size;
		if (width < 0)
			pWidth = -size;
		else
			pWidth = size;

		eleBody.setHeight(pHeight * 2 / 3);
		eleBody.setWidth(pWidth * 2 / 3);
		eleBody.setStartNode(getStartNode().x + pWidth / 6, getStartNode().y
				+ pHeight / 6);

		int portDistance = eleBody.getHeight() / 3;
		int controlPortDistance = eleBody.getWidth() / 3;
		for (int i = 0; i < inputPins.size(); i++) {
			inputPins.get(i).setFirstNode(
					eleBody.getStartNode().x - eleBody.getWidth() / 4,
					eleBody.getStartNode().y + (i + 1) * portDistance+PORT_DIFFERENT);
			inputPins.get(i).setLastNode(eleBody.getStartNode().x,
					eleBody.getStartNode().y + (i + 1) * portDistance+PORT_DIFFERENT);
		}
		for (int i = 0; i < outputPins.size(); i++) {
			outputPins.get(i).setFirstNode(
					eleBody.getStartNode().x + eleBody.getWidth(),
					eleBody.getStartNode().y + (i + 1) * portDistance);
			outputPins.get(i).setLastNode(
					eleBody.getStartNode().x + 5 * eleBody.getWidth() / 4,
					eleBody.getStartNode().y + (i + 1) * portDistance);
		}
		{
			// SD: 0. control pin
			controlPins.get(0).setFirstNode(
					eleBody.getStartNode().x + controlPortDistance+PORT_DIFFERENT,
					eleBody.getStartNode().y - eleBody.getHeight() / 4);
			controlPins.get(0).setLastNode(
					eleBody.getStartNode().x + controlPortDistance+PORT_DIFFERENT,
					eleBody.getStartNode().y);
			// RD: 1. control pin
			controlPins.get(1).setFirstNode(
					eleBody.getStartNode().x + 2 * controlPortDistance+PORT_DIFFERENT,
					eleBody.getStartNode().y - eleBody.getHeight() / 4);
			controlPins.get(1).setLastNode(
					eleBody.getStartNode().x + 2 * controlPortDistance+PORT_DIFFERENT,
					eleBody.getStartNode().y);
			// Clk: 2. control pin
			controlPins.get(2).setFirstNode(
					eleBody.getStartNode().x + controlPortDistance,
					eleBody.getStartNode().y + 5 * eleBody.getHeight() / 4);
			controlPins.get(2).setLastNode(
					eleBody.getStartNode().x + controlPortDistance,
					eleBody.getStartNode().y + eleBody.getHeight());

			// CE: 3.control pin
			controlPins.get(3).setFirstNode(
					eleBody.getStartNode().x + 2 * controlPortDistance,
					eleBody.getStartNode().y + 5 * eleBody.getHeight() / 4);
			controlPins.get(3).setLastNode(
					eleBody.getStartNode().x + 2 * controlPortDistance,
					eleBody.getStartNode().y + eleBody.getHeight());

		}
		// 6 indications
		{
			// font
			int fontSize = Math.min(Math.abs(eleBody.getWidth()), Math
					.abs(eleBody.getHeight())) / 8;
			Font indicationFont = new Font("Arial", 10, fontSize);
			textR.setFont(indicationFont);
			textS.setFont(indicationFont);
			textRD.setFont(indicationFont);
			textSD.setFont(indicationFont);
			textClk.setFont(indicationFont);
			textCE.setFont(indicationFont);
			textQ.setFont(indicationFont);
			textInvQ.setFont(indicationFont);

			// locations
			PTPoint basePointTextS = inputPins.get(0).getLastNode();
			PTPoint basePointTextR = new PTPoint();
			if (dualIO)
				basePointTextR = inputPins.get(1).getLastNode();
			PTPoint basePointTextQ = outputPins.get(0).getFirstNode();
			PTPoint basePointTextInvQ = new PTPoint();
			if (dualIO)
				basePointTextInvQ = outputPins.get(1).getFirstNode();
			PTPoint basePointTextSD = controlPins.get(0).getLastNode();
			PTPoint basePointTextRD = controlPins.get(1).getLastNode();
			PTPoint basePointTextClk = controlPins.get(2).getLastNode();
			PTPoint basePointTextCE = controlPins.get(3).getLastNode();

			// the y-coordinates of textS, textR, textQ, textInvQ and
			// the x-coordinates of the rests
			// are not depend on positive value of width or height
			textS.getPosition().y = basePointTextS.getY()
					+ textS.getBoundingBox().height / 2;
			textR.getPosition().y = basePointTextR.getY()
					+ textR.getBoundingBox().height / 2;
			textQ.getPosition().y = basePointTextQ.getY()
					+ textQ.getBoundingBox().height / 2;
			textInvQ.getPosition().y = basePointTextInvQ.getY()
					+ textInvQ.getBoundingBox().height / 2;

			textSD.getPosition().x = basePointTextSD.getX()
					- textSD.getBoundingBox().width / 2;
			textRD.getPosition().x = basePointTextRD.getX()
					- textRD.getBoundingBox().width / 2;
			textClk.getPosition().x = basePointTextClk.getX()
					- textClk.getBoundingBox().width / 2;
			textCE.getPosition().x = basePointTextCE.getX()
					- textCE.getBoundingBox().width / 2;

			// the other coordinates have to depend on the value of height or
			// width
			if (width < 0) {
				textS.getPosition().x = basePointTextS.getX()
						- textS.getBoundingBox().width;
				textR.getPosition().x = basePointTextR.getX()
						- textR.getBoundingBox().width;
				textQ.getPosition().x = basePointTextQ.getX();
				textInvQ.getPosition().x = basePointTextInvQ.getX();
			} else {
				textS.getPosition().x = basePointTextS.getX();
				textR.getPosition().x = basePointTextR.getX();
				textQ.getPosition().x = basePointTextQ.getX()
						- textQ.getBoundingBox().width;
				textInvQ.getPosition().x = basePointTextInvQ.getX()
						- textInvQ.getBoundingBox().width;

			}
			if (height < 0) {
				textSD.getPosition().y = basePointTextSD.getY();
				textRD.getPosition().y = basePointTextRD.getY();
				textClk.getPosition().y = basePointTextClk.getY()
						+ textClk.getBoundingBox().height;
				textCE.getPosition().y = basePointTextCE.getY()
						+ textCE.getBoundingBox().height;
			} else {
				textSD.getPosition().y = basePointTextSD.getY()
						+ textSD.getBoundingBox().height;
				textRD.getPosition().y = basePointTextRD.getY()
						+ textRD.getBoundingBox().height;
				textClk.getPosition().y = basePointTextClk.getY();
				textCE.getPosition().y = basePointTextCE.getY();
			}

		}
	}

//	public String getType() {
//		return TYPE_LABEL;
//	}

	public String[] handledKeywords() {
		return new String[] { "Flip Flop" };
	}

	public String toString() {
		StringBuilder result = new StringBuilder(120);
		result.append(getType()).append(' ');
		if (getObjectName() != null)
			result.append("\"").append(getObjectName()).append("\" ");

		if (synControl)
			result.append("synchronous  ");
		if (asynSR)
			result.append("has asyn. set/reset ");

		return result.toString();
	}

	protected void cloneCommonFeaturesInto(PTFlipFlop targetElement) {
		super.cloneCommonFeaturesInto(targetElement);
		targetElement.setElementBody((PTRectangle) getElementBody().clone());
		targetElement.setSynControl(hasSynControl());
		targetElement.setAsynSR(hasAsynSR());

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
	 * @return the synchronous
	 */
	public boolean hasSynControl() {
		return synControl;
	}

	/**
	 * @param synControl
	 *            the synchronous to set
	 */
	public void setSynControl(boolean synControl) {
		this.synControl = synControl;
	}

	/**
	 * @return the asynSR
	 */
	public boolean hasAsynSR() {
		return asynSR;
	}

	/**
	 * @param hasAsynSR
	 *            the asynchronous SET/RESET to set
	 */
	public void setAsynSR(boolean hasAsynSR) {
		this.asynSR = hasAsynSR;
	}

	/**
//	 * @return the tYPE_LABEL
//	 */
//	public static String getTYPE_LABEL() {
//		return TYPE_LABEL;
//	}

}
