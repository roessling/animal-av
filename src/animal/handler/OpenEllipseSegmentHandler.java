package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTOpenEllipseSegment;
import animal.misc.MSMath;

/**
 * This class provides the operations that can be performed on on ellipse segments.
 * 
 * @author Guido R&ouml;&szlig;ling (roessling@acm.org>
 * @version 0.8 2007-08-30
 */
public class OpenEllipseSegmentHandler extends GraphicObjectHandler {
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();

		// only works if the passed object is a PTPolyline!
		if (!(ptgo instanceof PTOpenEllipseSegment))
			return result;
		if (obj instanceof Point) {
			result.addElement("translate");
		}
		if (obj instanceof Color) {
			result.addElement("color");
		}
		if (obj instanceof Boolean) {
			result.addElement("show");
			result.addElement("hide");
		}

		if (obj instanceof String) {
			result.addElement("fwArrow");
			result.addElement("bwArrow");
			result.addElement("noFwArrow");
			result.addElement("noBwArrow");
		}
		addExtensionMethodsFor(ptgo, obj, result);
		return result;
	}

	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTPolyline!
		PTOpenEllipseSegment shape = null;
		if (ptgo instanceof PTOpenEllipseSegment)
			shape = (PTOpenEllipseSegment)ptgo;
		String what = e.getPropertyName();
		if ("translate".equalsIgnoreCase(what)) {
			Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue();
			Point diff = MSMath.diff(now, old);
			shape.translate(diff.x, diff.y);
		} else if (what.equalsIgnoreCase("color"))
			shape.setColor((Color) e.getNewValue());
		else if (what.equalsIgnoreCase("fwArrow"))
			shape.setFWArrow(true);
		else if (what.equalsIgnoreCase("noFwArrow"))
			shape.setFWArrow(true);
		else if (what.equalsIgnoreCase("bwArrow"))
			shape.setBWArrow(true);
		else if (what.equalsIgnoreCase("noBwArrow"))
			shape.setBWArrow(true);
		else
			super.propertyChange(ptgo, e);
	}
}
