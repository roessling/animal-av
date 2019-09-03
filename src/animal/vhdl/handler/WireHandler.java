package animal.vhdl.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import animal.animator.Rotation;
import animal.animator.ScaleParams;
import animal.graphics.PTGraphicObject;
import animal.handler.GraphicObjectHandler;
import animal.misc.MSMath;
import animal.misc.MessageDisplay;
import animal.vhdl.graphics.PTWire;

/**
 * This class provides the operations that can be performed on Wire.
 */
public class WireHandler extends GraphicObjectHandler {
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();

		// only works if the passed object is a PTWire!
		if (!(ptgo instanceof PTWire))
			return result;
		if (obj instanceof Point) {
			result.addElement("translate");
			result.addElement("translate #1");
			result.addElement("translate #2");
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
		PTWire shape = null;
		if (ptgo instanceof PTWire)
			shape = (PTWire) ptgo;
		String what = e.getPropertyName();
		if ("translate".equalsIgnoreCase(what)) {
			Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
			.diff(now, old);
			shape.translate(diff.x, diff.y);
		} else if ("translate #1".equalsIgnoreCase(what)) {
			Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(); 
			Point diff = MSMath.diff(now, old);
			shape.setFirstNode(old.x + diff.x, old.y + diff.y);
		} else if (what.equalsIgnoreCase("rotate")) {
			Rotation r = (Rotation) e.getNewValue();
			shape.rotate(r.getAngle() - ((Rotation) e.getOldValue()).getAngle(), r
					.getCenter());
		} else if ("scale".equalsIgnoreCase(what)) {
			ScaleParams s = (ScaleParams) e.getNewValue();
			double xFactor = s.getXScaleFactor()
			/ ((ScaleParams) e.getOldValue()).getXScaleFactor();
			double yFactor = s.getYScaleFactor()
			/ ((ScaleParams) e.getOldValue()).getYScaleFactor();
			if (xFactor != yFactor)
				MessageDisplay.errorMsg("Must scale by common factor for shape!",
						MessageDisplay.RUN_ERROR);
			else {
				shape.translate(-s.getCenter().getX(), -s.getCenter().getY());
				shape.scale(xFactor, yFactor);
				shape.translate(s.getCenter().getX(), s.getCenter().getY());
			}
		} else if (what.equalsIgnoreCase("color"))
			shape.setColor((Color) e.getNewValue());
		else
			super.propertyChange(ptgo, e);
	}
}
