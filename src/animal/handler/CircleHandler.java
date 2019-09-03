package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.StringTokenizer;
import java.util.Vector;

import translator.AnimalTranslator;
import animal.graphics.PTCircle;
import animal.graphics.PTGraphicObject;
import animal.misc.MSMath;
import animal.misc.MessageDisplay;

/**
 * This class provides the operations that can be performed on polylines.
 */
public class CircleHandler extends GraphicObjectHandler {
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();

		// only works if the passed object is a PTPolyline!
		if (!(ptgo instanceof PTCircle))
			return result;
		if (obj instanceof Point) {
			result.addElement("translate");
      result.addElement("translateRadius");
		}
		if (obj instanceof Color) {
			result.addElement("color");
			result.addElement("fillColor");
			result.addElement("colors: color, fillColor");
		}
		if (obj instanceof Boolean) {
			result.addElement("show");
			result.addElement("hide");
		}

		if (obj instanceof String) {
			result.addElement("fill");
			result.addElement("unfill");
		}
		addExtensionMethodsFor(ptgo, obj, result);
		return result;
	}

	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTPolyline!
		PTCircle shape = null;
		if (ptgo instanceof PTCircle)
			shape = (PTCircle) ptgo;
		String what = e.getPropertyName();
		if ("translate".equalsIgnoreCase(what)) {
			Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
					.diff(now, old);
			shape.translate(diff.x, diff.y);
		} else if ("translateRadius".equalsIgnoreCase(what)) {
      Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
          .diff(now, old);
      shape.setRadius(now.x);
    }
		else if (what.equalsIgnoreCase("color"))
			shape.setColor((Color) e.getNewValue());
		else if (what.equalsIgnoreCase("fillColor"))
			shape.setFillColor((Color) e.getNewValue());
		else if (what.startsWith("colors:")) {
			StringTokenizer stringTokenizer = new StringTokenizer(what, ":, ");
			stringTokenizer.nextToken(); // skip "colors:" token!
			while (stringTokenizer.hasMoreTokens()) {
				String currentToken = stringTokenizer.nextToken();
				if (currentToken.equalsIgnoreCase("color"))
					shape.setColor((Color) e.getNewValue());
				else if (currentToken.equalsIgnoreCase("fillColor"))
					shape.setFillColor((Color) e.getNewValue());
				else
					MessageDisplay.message("unparsedHandlerToken",
							new String[] { AnimalTranslator.translateMessage("polylineColor"),
							currentToken});
			}
		} else if (what.equalsIgnoreCase("fill"))
			shape.setFilled(true);
		else if (what.equalsIgnoreCase("unfill"))
			shape.setFilled(false);
		else
			super.propertyChange(ptgo, e);
	}
}
