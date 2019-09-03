package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import animal.animator.Rotation;
import animal.animator.ScaleParams;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTLine;
import animal.misc.MSMath;

/**
 * This class provides the operations that can be performed on polylines.
 */
public class LineHandler extends GraphicObjectHandler {
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();

		// only works if the passed object is a PTLine!
		if (!(ptgo instanceof PTLine))
			return result;
		if (obj instanceof Point) {
			result.addElement("translate");
      result.addElement("translate start");
      result.addElement("translate end");
		}
		if (obj instanceof Rotation) {
			result.addElement("rotate");
		}
		if (obj instanceof ScaleParams) {
			result.addElement("scale");
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
			result.addElement("noFwArrow");

			result.addElement("bwArrow");
			result.addElement("noBwArrow");
		}
		addExtensionMethodsFor(ptgo, obj, result);
		return result;
	}

	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTLine!
		PTLine line = null;
		if (ptgo instanceof PTLine)
      line = (PTLine) ptgo;
		String what = e.getPropertyName();
		if (what.startsWith("translate")) {
		  Point old = (Point) e.getOldValue(), 
		  now = (Point) e.getNewValue(), 
		  diff = MSMath.diff(now, old);
		  if ("translate".equalsIgnoreCase(what)) {
		    line.translate(diff.x, diff.y);
		  } else if ("translate start".equalsIgnoreCase(what)) {
		    line.getFirstNode().translate(diff.x, diff.y);
		  } else if ("translate end".equalsIgnoreCase(what)) {
        line.getLastNode().translate(diff.x, diff.y);
		  } 
		}
		else if (what.equalsIgnoreCase("rotate")) {
		  Rotation r = (Rotation) e.getNewValue();
      line.rotate(r.getAngle() - ((Rotation) e.getOldValue()).getAngle(), 
          r.getCenter());
		} else if (what.equalsIgnoreCase("scale")) {
		  ScaleParams s = (ScaleParams) e.getNewValue();
      line.translate(-s.getCenter().getX(), -s.getCenter().getY());
			double xFactor = s.getXScaleFactor()
					/ ((ScaleParams) e.getOldValue()).getXScaleFactor();
			double yFactor = s.getYScaleFactor()
					/ ((ScaleParams) e.getOldValue()).getYScaleFactor();
      line.scale(xFactor, yFactor);
      line.translate(s.getCenter().getX(), s.getCenter().getY());
		} else if (what.equalsIgnoreCase("color"))
      line.setColor((Color) e.getNewValue());
		else if (what.equalsIgnoreCase("fwArrow"))
      line.setFWArrow(true);
		else if (what.equalsIgnoreCase("noFwArrow"))
      line.setFWArrow(false);
		else if (what.equalsIgnoreCase("bwArrow"))
      line.setBWArrow(true);
		else if (what.equalsIgnoreCase("noBwArrow"))
      line.setBWArrow(false);
		else
			super.propertyChange(ptgo, e);
	}
}
