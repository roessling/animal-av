package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import animal.animator.Rotation;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTText;
import animal.misc.MSMath;

/**
 * This class provides the operations that can be performed on texts.
 */
public class TextHandler extends GraphicObjectHandler {
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();

		if (obj instanceof Point) {
			result.addElement("translate");
		}
		if (obj instanceof Color) {
			result.addElement("color");
		}

		if (obj instanceof Rotation) {
			result.addElement("rotate");
		}

		if (obj instanceof Boolean) {
			result.addElement("show");
			result.addElement("hide");
		}
		addExtensionMethodsFor(ptgo, obj, result);
		return result;
	}

	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTText!
		PTText text = null;
		if (ptgo instanceof PTText)
			text = (PTText) ptgo;
		String what = e.getPropertyName();
		// moving is always relative, so calculate the difference between
		// the last value and the actual value and use this.
		if (what.equalsIgnoreCase("translate")) {
			Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
					.diff(now, old);
			text.translate(diff.x, diff.y);
		} else if (what.equalsIgnoreCase("color"))
			text.setColor((Color) e.getNewValue());
		else if (what.equalsIgnoreCase("rotate")) {
			Rotation r = (Rotation) e.getNewValue();
			text.rotate(r.getAngle(), r.getCenter());
		} else
			super.propertyChange(ptgo, e);
	}
}
