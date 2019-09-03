/*
 * DoubleArrayHandler.java
 * The handler for PTDoubleArray.
 *
 * Created on 12. August 2005, 17:17
 *
 * @author Michael Schmitt
 *
 * @version 0.3.2a
 * @date 2006-01-09
 */

package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import translator.AnimalTranslator;
import animal.animator.PutType;
import animal.animator.Swap;
import animal.animator.SwapType;
import animal.graphics.PTDoubleArray;
import animal.graphics.PTGraphicObject;
import animal.misc.MSMath;
import animal.misc.MessageDisplay;

/**
 * Handler for operations that can be performed on DoubleArrays.
 */
public class DoubleArrayHandler extends GraphicObjectHandler {

  /**
   * Get all the animation methods supported by a PTDoubleArray.
   * 
   * @param ptgo
   *          a graphic object, i.e. a PTDoubleArray
   * @param obj
   *          the type of the animation effect
   */
  public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>(12, 2);
		if (obj instanceof Boolean) { // animation types for "Show" / "TimedShow"
																	// animators
			result.addElement("show"); // show the array
			result.addElement("hide"); // hide the array
		}

		if (obj instanceof Color) {// animation types for ColorChanger
			result.addElement("bgColor"); // change the array's background color
			result.addElement("outlineColor"); // change the color of the cells' outline
			result.addElement("fontColor"); // change the font color of the array
			
			result.addElement("highlight elements"); // highlight the cell content
			result.addElement("highlight cells"); // highlight the cell background
			result.addElement("unhighlight elements"); // unhighlight the cell content
			result.addElement("unhighlight cells"); // unhighlight the cell background
		}

		if (obj instanceof PutType) {
			result.addElement("put"); // put a new content into the specified array cell
		}

		if (obj instanceof String) {
			result.addElement("setTextSwap"); // set a new content into the specified array cell
		}

		if (obj instanceof Point) // animation types for Move
			result.addElement("translate"); // move the array

		if (obj instanceof Swap) // animation type for Swap
			result.addElement("swap");

		if (obj instanceof double[]) {// animation types for Highlight
			result.addElement("deactivate cells"); // deactivate the cell
			result.addElement("activate cells"); // activate the cell
		}

		// add extension methods provided in other classes
		addExtensionMethodsFor(ptgo, obj, result);

		// return the vector of animation types
		return result;
	}

  /**
   * Transform the requested property change in method calls
   * 
   * @param ptgo
   *          the graphical primitive to modify
   * @param e
   *          the PropertyChangeEvent that encodes the information which
   *          property has to change how
   */
  @SuppressWarnings("unused")
public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
    // only works if the passed object is a PTDoubleArray!
    PTDoubleArray array = null;
    if (ptgo instanceof PTDoubleArray) {
      array = (PTDoubleArray) ptgo; // convert to PTDoubleArray
      String what = e.getPropertyName(); // retrieve property

      if (what.equalsIgnoreCase("bgColor")) // set background color
			array.setBGColor((Color) e.getNewValue());
		else if (what.equalsIgnoreCase("fontColor"))
			array.setFontColor((Color) e.getNewValue());
		else if (what.equalsIgnoreCase("outlineColor"))
			array.setOutlineColor((Color) e.getNewValue());
		else if (what.equalsIgnoreCase("translate")) {
			Point old = (Point) e.getOldValue();
			Point now = (Point) e.getNewValue();
			Point diff = MSMath.diff(now, old);
			array.translate(diff.x, diff.y);
		} else if (what.equalsIgnoreCase("put")) {		// UNUSED
			PutType now = (PutType) e.getNewValue();
			PutType old = (PutType) e.getOldValue();
			if (now.hasFinished) {
				if (array.put(now.idx, now.newContent) < 0) {
					MessageDisplay.errorMsg(
						AnimalTranslator.translateMessage("invalidCallDuringPut"),
						MessageDisplay.RUN_ERROR, true);
				}
			}
		} else if (what.equalsIgnoreCase("swap")) {		// UNUSED
			SwapType old = (SwapType) e.getOldValue();
			SwapType now = (SwapType) e.getNewValue();
			int lower = now.getElements()[0];
			int upper = now.getElements()[1];
			if (now.done) {
				array.doSwap(lower, upper);
			}
		} else if (what.contains("highlight")) {
			Color color = (Color)e.getNewValue();		// UNUSED
			String[] whatArray = what.split(" ");
			if(whatArray.length==4){
				boolean highlight = !whatArray[0].startsWith("un");
				boolean elements = whatArray[1].endsWith("elements");
				int fromIndex = Integer.valueOf(whatArray[2]);
				int toIndex = Integer.valueOf(whatArray[3]);
				for (int i = fromIndex; i <= toIndex; i++) {
					if (elements) {
						//array.setElemHighlightColor(i, color);
						array.setElemHighlighted(i, highlight);
					} else {
						//array.setHighlightColorIndex(i, color);
						array.setHighlighted(i, highlight);
					}
				}
			}
		} else if (what.contains("activate")) {
			double[] states = (double[]) e.getNewValue();
			int size = array.getSize();
			for (int i = 0; i < size; i++) {
				if (states[i] >= 0.5)
					array.setActivated(i, !what.startsWith("de"));
			}
		} else if (what.startsWith("setText") && !what.startsWith("setTextColor")) {
			if(what.startsWith("setTextSwap")){//Swap
				String newContent = (String)e.getNewValue();
				array.doSwap(Integer.valueOf(newContent.split(" ")[0]), Integer.valueOf(newContent.split(" ")[1]));
			}else{//Put
				String newContent = (String)e.getNewValue();
				if (array.put(Integer.valueOf(what.split(" ")[1]), newContent) < 0) {
					MessageDisplay.errorMsg(
						AnimalTranslator.translateMessage("invalidCallDuringPut"),
						MessageDisplay.RUN_ERROR, true);
				}
			}
		} else if (what.startsWith("showIndices")) {
			Boolean visibility = ((String)e.getNewValue()).equals("true");
			array.showIndices(visibility);
		} else if (what.startsWith("setBorderColor") || what.startsWith("setFillColor") || what.startsWith("setTextColor")
				 || what.startsWith("setHighlightBorderColor") || what.startsWith("setHighlightFillColor") || what.startsWith("setHighlightTextColor")) {
			String method = what.split(" ")[0];
			Integer fromRange = Integer.valueOf(what.split(" ")[1]);
			Integer toRange = Integer.valueOf(what.split(" ")[2]);
			Color color = (Color)e.getNewValue();
			boolean modeSetBorderColor = method.toLowerCase().equals("setBorderColor".toLowerCase());
			boolean modeSetFillColor = method.toLowerCase().equals("setFillColor".toLowerCase());
			boolean modeSetTextColor = method.toLowerCase().equals("setTextColor".toLowerCase());
			boolean modeSetHighlightBorderColor = method.toLowerCase().equals("setHighlightBorderColor".toLowerCase());
			boolean modeSetHighlightFillColor = method.toLowerCase().equals("setHighlightFillColor".toLowerCase());
			boolean modeSetHighlightTextColor = method.toLowerCase().equals("setHighlightTextColor".toLowerCase());
			for (int i = fromRange; i <= toRange; i++) {
				if(modeSetBorderColor){
					array.setOutlineColor(i, color);
				}else if(modeSetFillColor){
					array.setBGColor(i, color);
				}else if(modeSetTextColor){
					array.setFontColor(i, color);
				}else if(modeSetHighlightBorderColor){
					array.setHighlightOutlineColor(i, color);
				}else if(modeSetHighlightFillColor){
					array.setHighlightColorIndex(i, color);
				}else if(modeSetHighlightTextColor){
					array.setElemHighlightColor(i, color);
				}
			}
		} else { // not handled here; pass up to superclass
			super.propertyChange(ptgo, e);
		}
    }
  }
}