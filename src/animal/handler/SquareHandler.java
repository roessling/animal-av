package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.StringTokenizer;
import java.util.Vector;

import translator.AnimalTranslator;
import animal.animator.Rotation;
import animal.animator.ScaleParams;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTSquare;
import animal.misc.MSMath;
import animal.misc.MessageDisplay;

/**
 * This class provides the operations that can be performed on polylines.
 */
public class SquareHandler extends GraphicObjectHandler {
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();

		// only works if the passed object is a PTPolyline!
		if (!(ptgo instanceof PTSquare))
			return result;
		if (obj instanceof Point) {
			result.addElement("translate");
		}
		if (obj instanceof Rotation) {
			result.addElement("rotate");
		}
		if (obj instanceof ScaleParams) {
			result.addElement("scale");
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
		PTSquare square = null;
		if (ptgo instanceof PTSquare)
			square = (PTSquare) ptgo;
		String what = e.getPropertyName();
		if ("translate".equalsIgnoreCase(what)) {
			Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
					.diff(now, old);
			square.translate(diff.x, diff.y);
		} else if (what.equalsIgnoreCase("rotate")) {
			Rotation r = (Rotation) e.getNewValue();
			square.rotate(r.getAngle() - ((Rotation) e.getOldValue()).getAngle(), r
					.getCenter());
		} else if (what.equalsIgnoreCase("scale")) {
			ScaleParams s = (ScaleParams) e.getNewValue();
			double xFactor = s.getXScaleFactor()
					/ ((ScaleParams) e.getOldValue()).getXScaleFactor();
			double yFactor = s.getYScaleFactor()
					/ ((ScaleParams) e.getOldValue()).getYScaleFactor();
			if (xFactor != yFactor)
				MessageDisplay.errorMsg("Must scale by common factor for square!",
						MessageDisplay.RUN_ERROR);
			else {
				square.translate(-s.getCenter().getX(), -s.getCenter().getY());
				square.scale(xFactor, yFactor);
				square.translate(s.getCenter().getX(), s.getCenter().getY());
			}
		} else if (what.equalsIgnoreCase("color"))
			square.setColor((Color) e.getNewValue());
		else if (what.equalsIgnoreCase("fillColor"))
			square.setFillColor((Color) e.getNewValue());
		else if (what.startsWith("colors:")) {
			StringTokenizer stringTokenizer = new StringTokenizer(what, ":, ");
			stringTokenizer.nextToken(); // skip "colors:" token!
			while (stringTokenizer.hasMoreTokens()) {
				String currentToken = stringTokenizer.nextToken();
				if (currentToken.equalsIgnoreCase("color"))
					square.setColor((Color) e.getNewValue());
				else if (currentToken.equalsIgnoreCase("fillColor"))
					square.setFillColor((Color) e.getNewValue());
				else
					MessageDisplay.message("unparsedHandlerToken",
							new String[] { AnimalTranslator.translateMessage("polylineColor"),
							currentToken});
			}
		} else if (what.equalsIgnoreCase("fill"))
			square.setFilled(true);
		else if (what.equalsIgnoreCase("unfill"))
			square.setFilled(false);
		else
			super.propertyChange(ptgo, e);
	}
}
