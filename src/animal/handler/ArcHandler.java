package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.StringTokenizer;
import java.util.Vector;

import translator.AnimalTranslator;
import animal.animator.Rotation;
import animal.graphics.PTArc;
import animal.graphics.PTGraphicObject;
import animal.misc.MSMath;
import animal.misc.MessageDisplay;

/**
 * This class provides the operations that can be performed on polylines.
 */
public class ArcHandler extends GraphicObjectHandler {
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();
		if (obj instanceof Point) {
			result.addElement("translate");
			result.addElement("translateRadius");
			result.addElement("translateAngle");
			result.addElement("translateStartAngle");
		}

		if (obj instanceof Rotation) {
			result.addElement("rotate");
		}

		if (obj instanceof Color) {
			result.addElement("color");
			result.addElement("textColor");
			result.addElement("fillColor");
			result.addElement("colors: color, textColor");
			result.addElement("colors: color, fillColor");
			result.addElement("colors: textColor, fillColor");
			result.addElement("colors: color, textColor, fillColor");
		}

		if (obj instanceof Integer) {
			result.addElement("depth");
		}

		if (obj instanceof Boolean) {
			result.addElement("show");
			result.addElement("hide");
		}

		if (obj instanceof String) {
			result.addElement("close");
			result.addElement("open");

			result.addElement("fill");
			result.addElement("unfill");

			result.addElement("fwArrow");
			result.addElement("noFwArrow");

			result.addElement("bwArrow");
			result.addElement("noBwArrow");
		}

		addExtensionMethodsFor(ptgo, obj, result);
		return result;
	}

	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTArc!
		PTArc arc = null;
		if (ptgo instanceof PTArc)
			arc = (PTArc) ptgo;
		String what = e.getPropertyName();
		// moving is always relative, so calculate the difference between
		// the last value and the actual value and use this. */
		if (what.startsWith("translate")) {
			Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
					.diff(now, old);
			if (what.equalsIgnoreCase("translate"))
				arc.translate(diff.x, diff.y);
			else if (what.equalsIgnoreCase("translateRadius")) {
				Point targetPoint = arc.getRadiusPoint();
				targetPoint.translate(diff.x, diff.y);
				arc.setRadius(targetPoint);
			} else if (what.equalsIgnoreCase("translateStartAngle")) {
				arc.setStartAngle(arc.getAngle(now));
			} else if (what.equalsIgnoreCase("translateAngle")) {
				arc.setTotalAngle(arc.getAngle(now));
			}
		} else if (what.equalsIgnoreCase("color"))
			arc.setColor((Color) e.getNewValue());
		else if (what.equalsIgnoreCase("rotate")) {
			Rotation r = (Rotation) e.getNewValue();
			arc.rotate(r.getAngle() - ((Rotation) e.getOldValue()).getAngle(), r
					.getCenter());
		} else if (what.equalsIgnoreCase("fillColor"))
			arc.setFillColor((Color) e.getNewValue());
		else if (what.equalsIgnoreCase("textColor"))
			arc.setTextColor((Color) e.getNewValue());
		else if (what.startsWith("colors:")) {
			StringTokenizer stringTokenizer = new StringTokenizer(what, ":, ");
			stringTokenizer.nextToken(); // skip "colors:" token!
			while (stringTokenizer.hasMoreTokens()) {
				String currentToken = stringTokenizer.nextToken();
				if (currentToken.equalsIgnoreCase("color"))
					arc.setColor((Color) e.getNewValue());
				else if (currentToken.equalsIgnoreCase("fillColor"))
					arc.setFillColor((Color) e.getNewValue());
				else if (currentToken.equalsIgnoreCase("textColor"))
					arc.setTextColor((Color) e.getNewValue());
				else
					MessageDisplay.message("unparsedHandlerToken", 
							new String[] {AnimalTranslator.translateMessage("arcColor"),
							currentToken });
			}
		} else if (what.equalsIgnoreCase("depth"))
			arc.setDepth(((Integer) e.getNewValue()).intValue());
		else if (what.equalsIgnoreCase("close"))
			arc.setClosed(true);
		else if (what.equalsIgnoreCase("open"))
			arc.setClosed(false);
		else if (what.equalsIgnoreCase("fill"))
			arc.setFilled(true);
		else if (what.equalsIgnoreCase("unfill"))
			arc.setFilled(false);
		else if (what.equalsIgnoreCase("fwArrow"))
			arc.setFWArrow(true);
		else if (what.equalsIgnoreCase("noFwArrow"))
			arc.setFWArrow(false);
		else if (what.equalsIgnoreCase("bwArrow"))
			arc.setBWArrow(true);
		else if (what.equalsIgnoreCase("noBwArrow"))
			arc.setBWArrow(false);
		else
			super.propertyChange(ptgo, e); // this displays an error message
	}
}
