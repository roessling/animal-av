package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import animal.animator.Rotation;
import animal.animator.ScaleParams;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.misc.MSMath;

/**
 * Handler for operations that can be performed on points.
 */
public class PointHandler extends GraphicObjectHandler {
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();
		if (obj instanceof Boolean) { // animation types for "Show" / "TimedShow"
																	// animators
			result.addElement("show"); // show the point
			result.addElement("hide"); // hide the point
		}

		if (obj instanceof Color) // anim. types for ColorChanger
			result.addElement("color"); // change the point's color

		if (obj instanceof Point) // animation types for Move
			result.addElement("translate"); // move the point

		if (obj instanceof Rotation) // animation types for Rotate
			result.addElement("rotate"); // rotate the point

		if (obj instanceof ScaleParams) // anim. types for Scale
			result.addElement("scale"); // scale the point

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
	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTPoint!
		PTPoint point = null;
		if (ptgo instanceof PTPoint) {
			point = (PTPoint) ptgo; // convert to PTPoint
			String what = e.getPropertyName(); // retrieve property

			if (what.equalsIgnoreCase("color")) // set color
				point.setColor((Color) e.getNewValue());
			else if (what.equalsIgnoreCase("translate")) { // move the point
				Point old = (Point) e.getOldValue();
				Point now = (Point) e.getNewValue();
				Point diff = MSMath.diff(now, old);
				// translate by (new - old)
				point.translate(diff.x, diff.y);
			} else if (what.equalsIgnoreCase("rotate")) { // rotate the point
				Rotation r = (Rotation) e.getNewValue(); // angle
				// rotate by (new angle - old angle) around the given
				// rotation center
				point.rotate(r.getAngle() - ((Rotation) e.getOldValue()).getAngle(), r
						.getCenter());
			} else if (what.equalsIgnoreCase("scale")) { // scale the point
				ScaleParams s = (ScaleParams) e.getNewValue();
				// translate into point of origin
				point.translate(-s.getCenter().getX(), -s.getCenter().getY());
				// determine x and y scale factors
				double xFactor = s.getXScaleFactor()
						/ ((ScaleParams) e.getOldValue()).getXScaleFactor();
				double yFactor = s.getYScaleFactor()
						/ ((ScaleParams) e.getOldValue()).getYScaleFactor();

				// scale the point
				point.scale(xFactor, yFactor);

				// translate to original coordinates
				point.translate(s.getCenter().getX(), s.getCenter().getY());
			} else { // not handled here; pass up to superclass
				super.propertyChange(ptgo, e);
			}
		}
	}
}
