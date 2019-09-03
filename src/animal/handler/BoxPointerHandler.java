package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import animal.graphics.PTBoxPointer;
import animal.graphics.PTGraphicObject;
import animal.misc.MSMath;

/**
 * Provides operations that can be performed on list elements.
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.0 2001-11-15
 */
public class BoxPointerHandler extends GraphicObjectHandler {
	/**
	 * Generates a Vector of effect types for the primitive. The underlying
	 * animation effect is characterized by the second parameter, which may also
	 * encode relevant information. We may also have to examine the properties of
	 * the primitive, which is therefore passed in as a parameter.
	 * 
	 * @param ptgo
	 *          the graphical primitive that used in the animation effect. This
	 *          must be examined to offer appropriate methods for translating
	 *          individual pointers ("tips").
	 * @param obj
	 *          the object that characterizes the type of animation effect for
	 *          which the vector of effects has to be generated.
	 */
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();
		PTBoxPointer boxPointer = null;
		if (ptgo instanceof PTBoxPointer)
			boxPointer = (PTBoxPointer) ptgo;
		else
			return new Vector<String>();

		if (obj instanceof Point) {
			result.addElement("translate"); // full primitive
			int nrPointers = 0;
			if (boxPointer.getPointers() != null)
				nrPointers = boxPointer.getPointers().size();
			if (nrPointers > 0) {
				result.addElement("setTip"); // only (first) pointer
				result.addElement("translateWithFixedTip");
			}
		}
		if (obj instanceof Color) {
			result.addElement("text box frame & pointer color");
			result.addElement("fillColor");
			result.addElement("pointer box frame color");
			result.addElement("pointer background color");
			result.addElement("color");
			result.addElement("textcolor");
		}
		if (obj instanceof Boolean) {
			result.addElement("show");
			result.addElement("hide");
		}
		addExtensionMethodsFor(ptgo, obj, result);
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
	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTBoxPointer!
		PTBoxPointer boxPointer = null;
		if (ptgo instanceof PTBoxPointer) {
			boxPointer = (PTBoxPointer) ptgo;
			String what = e.getPropertyName();
			if (what.equalsIgnoreCase("setTip")) {
				Point old = (Point) e.getOldValue();
				Point now = (Point) e.getNewValue(), diff = MSMath.diff(now, old);
				if (boxPointer.getPointers() != null)
					boxPointer.setTip(0, MSMath.sum(boxPointer.getTip(0), diff));
			} else if (what.equalsIgnoreCase("translate")) {
				Point old = (Point) e.getOldValue();
				Point now = (Point) e.getNewValue(), diff = MSMath.diff(now, old);
				boxPointer.translate(diff.x, diff.y);
			} else if (what.equalsIgnoreCase("translateWithFixedTip")) {
				Point old = (Point) e.getOldValue();
				Point now = (Point) e.getNewValue(), diff = MSMath.diff(now, old);
				boxPointer.translateWithFixedTips(diff.x, diff.y);
			}
			/*
			 * else if (what.startsWith("translateWithFixedTip #")) { // translate
			 * only one of the vertices. int num =
			 * Integer.parseInt(what.substring(23)); boolean[] map = new
			 * boolean[boxPointer.getPointerCount()]; map[num-1] = true; Point old =
			 * (Point) e.getOldValue(); Point now = (Point) e.getNewValue(), diff =
			 * MSMath.diff(now, old); boxPointer.translateWithFixedTips(map, diff.x,
			 * diff.y); } else if (what.startsWith("setTip #")) { // translate only
			 * one of the vertices. int num = Integer.parseInt(what.substring(8));
			 * Point old = (Point) e.getOldValue(); Point now = (Point)
			 * e.getNewValue(), diff = MSMath.diff(now, old); boxPointer.setTip(num -
			 * 1, MSMath.sum(boxPointer.getTip(num - 1), diff)); } else if
			 * (what.startsWith("setTips ") || what.startsWith("translateWithFixedTips
			 * ")) { boolean setTipsMode = what.startsWith("setTips"); StringTokenizer
			 * stringTok = new StringTokenizer(what.substring((setTipsMode) ? 7:22));
			 * int nodeCount = boxPointer.getPointerCount(); boolean[] map = new
			 * boolean[nodeCount]; int currentNode = 0; while
			 * (stringTok.hasMoreTokens()) { currentNode =
			 * Integer.parseInt(stringTok.nextToken()); if (currentNode>0 &&
			 * currentNode<=nodeCount) map[currentNode-1] = true; } Point old =
			 * (Point) e.getOldValue(); Point now = (Point) e.getNewValue(), diff =
			 * MSMath.diff(now, old); if (boxPointer.getPointers() != null) { if
			 * (setTipsMode) { int a; for (a = 0; a < boxPointer.getPointers().length;
			 * a++) if (map[a]) boxPointer.setTip(a, MSMath.sum(boxPointer.getTip(a),
			 * diff)); } else boxPointer.translateWithFixedTips(map, diff.x, diff.y); } }
			 */
			else if (what.equalsIgnoreCase("text box frame & pointer color"))
				boxPointer.setColor((Color) e.getNewValue());
			else if (what.equalsIgnoreCase("fillColor"))
				boxPointer.getTextBox().setFillColor((Color) e.getNewValue());
			else if (what.equalsIgnoreCase("pointer box frame color"))
				boxPointer.getPointerArea().setColor((Color) e.getNewValue());
			else if (what.equalsIgnoreCase("pointer background color")
					|| what.equalsIgnoreCase("color"))
				boxPointer.getPointerArea().setFillColor((Color) e.getNewValue());
			else if (what.equalsIgnoreCase("textcolor"))
				boxPointer.getTextComponent().setColor((Color) e.getNewValue());
			else
				super.propertyChange(ptgo, e);
		}
	}
}
