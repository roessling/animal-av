package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.StringTokenizer;
import java.util.Vector;

import animal.animator.Rotation;
import animal.animator.ScaleParams;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPolyline;
import animal.misc.MSMath;

/**
 * This class provides the operations that can be performed on polylines.
 */
public class PolylineHandler extends GraphicObjectHandler {
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();

		// only works if the passed object is a PTPolyline!
		PTPolyline polyline = null;
		if (ptgo instanceof PTPolyline)
			polyline = (PTPolyline) ptgo;
		else
			return result;
		if (obj instanceof Point) {
			result.addElement("translate");
			for (int a = 0; a < polyline.getNodeCount(); a++)
				result.addElement("translate #" + (a + 1));
			result.addElement("translateNodes..."); // NEW
			result.addElement("translateWithFixedNodes..."); // NEW
		}
		if (obj instanceof Rotation) {
			result.addElement("rotate");
		}
		if (obj instanceof ScaleParams) {
			result.addElement("scale");
		}
		if (obj instanceof Color) {
			result.addElement("color");
//			result.addElement("fillColor");
//			result.addElement("colors: color, fillColor");
		}
		if (obj instanceof Boolean) {
			result.addElement("show");
			result.addElement("hide");
		}

		if (obj instanceof String) {
//			result.addElement("close");
//			result.addElement("open");
//
//			result.addElement("fill");
//			result.addElement("unfill");
//
			result.addElement("fwArrow");
			result.addElement("noFwArrow");

			result.addElement("bwArrow");
			result.addElement("noBwArrow");

			result.addElement("addNode");
			result.addElement("removeNode");
		}
		addExtensionMethodsFor(ptgo, obj, result);
		return result;
	}

	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTPolyline!
		PTPolyline polyline = null;
		if (ptgo instanceof PTPolyline)
			polyline = (PTPolyline) ptgo;
		String what = e.getPropertyName();
		if ("translate".equalsIgnoreCase(what)) {
			Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
					.diff(now, old);
			polyline.translate(diff.x, diff.y);
		} else if (what.length() > 11
				&& what.substring(0, 11).equalsIgnoreCase("translate #")) {
			// translate only one of the vertices.
			int num = Integer.parseInt(what.substring(11));
			Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
					.diff(now, old);
			polyline.translate(num - 1, diff.x, diff.y);
		} else if (what.startsWith("translateNodes ")
				|| what.startsWith("translateWithFixedNodes ")) {
			boolean moveMode = (what.startsWith("translateNodes"));
			StringTokenizer stringTok = new StringTokenizer(what
					.substring((moveMode) ? 15 : 24));
			int nodeCount = polyline.getNodeCount();
			boolean[] map = new boolean[nodeCount];
			if (!moveMode)
				for (int i = 0; i < nodeCount; i++)
					map[i] = true;
			int currentNode = 0;
			while (stringTok.hasMoreTokens()) {
				currentNode = Integer.parseInt(stringTok.nextToken());
				if (currentNode > 0 && currentNode <= nodeCount)
					map[currentNode - 1] = moveMode;
			}
//			if (polyline.isClosed())
//				map[nodeCount - 1] = map[0]; // for closed polylines ONLY!

			Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
					.diff(now, old);
			polyline.translate(map, diff.x, diff.y);
		} else if (what.equalsIgnoreCase("rotate")) {
			Rotation r = (Rotation) e.getNewValue();
			polyline.rotate(r.getAngle() - ((Rotation) e.getOldValue()).getAngle(), r
					.getCenter());
		} else if (what.equalsIgnoreCase("scale")) {
			ScaleParams s = (ScaleParams) e.getNewValue();
			polyline.translate(-s.getCenter().getX(), -s.getCenter().getY());
			double xFactor = s.getXScaleFactor()
					/ ((ScaleParams) e.getOldValue()).getXScaleFactor();
			double yFactor = s.getYScaleFactor()
					/ ((ScaleParams) e.getOldValue()).getYScaleFactor();
			polyline.scale(xFactor, yFactor);
			polyline.translate(s.getCenter().getX(), s.getCenter().getY());
		} else if (what.equalsIgnoreCase("color"))
			polyline.setColor((Color) e.getNewValue());
//		else if (what.equalsIgnoreCase("fillColor"))
//			polyline.setFillColor((Color) e.getNewValue());
//		else if (what.startsWith("colors:")) {
//			StringTokenizer stringTokenizer = new StringTokenizer(what, ":, ");
//			stringTokenizer.nextToken(); // skip "colors:" token!
//			while (stringTokenizer.hasMoreTokens()) {
//				String currentToken = stringTokenizer.nextToken();
//				if (currentToken.equalsIgnoreCase("color"))
//					polyline.setColor((Color) e.getNewValue());
//				else if (currentToken.equalsIgnoreCase("fillColor"))
//					polyline.setFillColor((Color) e.getNewValue());
//				else
//					MessageDisplay.message("unparsedHandlerToken",
//							new String[] { AnimalTranslator.translateMessage("polylineColor"),
//							currentToken});
//			}
//		} 
//    else if (what.equalsIgnoreCase("close"))
//			polyline.setClosed(true);
//		else if (what.equalsIgnoreCase("open"))
//			polyline.setClosed(false);
//		else if (what.equalsIgnoreCase("fill"))
//			polyline.setFilled(true);
//		else if (what.equalsIgnoreCase("unfill"))
//			polyline.setFilled(false);
		else if (what.equalsIgnoreCase("fwArrow"))
			polyline.setFWArrow(true);
		else if (what.equalsIgnoreCase("noFwArrow"))
			polyline.setFWArrow(false);
		else if (what.equalsIgnoreCase("bwArrow"))
			polyline.setBWArrow(true);
		else if (what.equalsIgnoreCase("noBwArrow"))
			polyline.setBWArrow(false);
		else
			super.propertyChange(ptgo, e);
	}
}
