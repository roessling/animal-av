package animal.handler;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.StringTokenizer;
import java.util.Vector;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTPolyline;
import animal.misc.MSMath;

/**
 * This class provides the operations that can be performed on polylines.
 */
public class PolylineHandlerExtension extends GraphicObjectHandlerExtension {
	public PolylineHandlerExtension() {
		type = PTPolyline.POLYLINE_TYPE;
	}

	public Vector<String> getMethods( PTGraphicObject ptgo,
			Object obj) {
		Vector<String> result = new Vector<String>();

		if (obj instanceof Point) {
			result.addElement("translateNodes22..."); // NEW
		}
		return result;
	}

	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTPolyline!
		PTPolyline polyline = null;
		if (ptgo instanceof PTPolyline)
			polyline = (PTPolyline) ptgo;
		String what = e.getPropertyName();
		if (what.startsWith("translateNodes22 ")
				|| what.startsWith("translateWithFixedNodes ")) {
			boolean moveMode = (what.startsWith("translateNodes22"));
			StringTokenizer stringTok = new StringTokenizer(what
					.substring((moveMode) ? 17 : 26));
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
		}
	}
}
