/**
 * 
 */
package animal.vhdl.graphics;

import java.awt.Graphics;

import animal.graphics.PTCircle;


/**
 * Base class for all graphic vhdl-notgates.
 * 
 * @author Zheng Lu,p_li
 * 
 * @version 2.0 
 * 
 * apply to ANIMAL 3.0
 * 
 */
public abstract class PTNGate extends PTGate {
//	public static final String TYPE_LABEL = "NGate";
	PTCircle notSymbol= new PTCircle();
	
	public PTNGate(int x, int y, int width, int height, int InputPortCount) {
		super(x, y, width, height, InputPortCount);
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
		//outputPin.get(0).paint(g);
		
//		NotSymbol
		int radius=outputPins.get(0).getLength()/8;
		notSymbol.setRadius(radius);
		int x=outputPins.get(0).getFirstNode().getX()+2*radius;
		int y=outputPins.get(0).getFirstNode().getY();
		if (outputPins.get(0).getFirstNode().getX()<outputPins.get(0).getLastNode().getX()){
			notSymbol.setCenter(outputPins.get(0).getFirstNode().getX()+radius,
					outputPins.get(0).getFirstNode().getY());
		}
		else{
			notSymbol.setCenter(outputPins.get(0).getFirstNode().getX()-radius,
					outputPins.get(0).getFirstNode().getY());
			x=outputPins.get(0).getFirstNode().getX()-2*radius;
		}
		notSymbol.setFilled(elementBody.isFilled());
		notSymbol.setFillColor(elementBody.getFillColor());
		notSymbol.setColor(outputPins.get(0).getColor());
		
		
		outputPins.get(0).setFirstNode(x, y);
		outputPins.get(0).paint(g);
		notSymbol.paint(g);
		// gate symbol
		elementSymbol.setColor(color);
		elementSymbol.paint(g);
	}
	public String[] handledKeywords() {
		return new String[] { "NGate" };
	}


}
