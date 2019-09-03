package animal.handler;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.StringTokenizer;
import java.util.Vector;

import animal.graphics.PTBoxPointer;
import animal.graphics.PTGraphicObject;
import animal.misc.MSMath;

/**
 * Perform on list elements with multiple pointers
 */
public class BoxPointerMoveMultiplePointers extends
		GraphicObjectHandlerExtension {
	public BoxPointerMoveMultiplePointers() {
		type = PTBoxPointer.TYPE_LABEL;
	}

	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
		Vector<String> result = new Vector<String>();
		PTBoxPointer boxPointer = null;
		if (ptgo instanceof PTBoxPointer)
			boxPointer = (PTBoxPointer) ptgo;

		if (obj instanceof Point) {
			int nrPointers = 0, a;
			if (boxPointer.getPointers() != null)
				nrPointers = boxPointer.getPointers().size();
			if (nrPointers >= 1) {
				for (a = 0; a < nrPointers; a++)
					result.addElement("translateWithFixedTip #" + (a + 1));
				for (a = 0; a < nrPointers; a++)
					result.addElement("setTip #" + (a + 1));
				result.addElement("setTips...");
				result.addElement("translateWithFixedTips...");
			}
		}
		return result;
	}

	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTBoxPointer!
		PTBoxPointer boxPointer = null;
		if (ptgo instanceof PTBoxPointer)
			boxPointer = (PTBoxPointer) ptgo;
		String what = e.getPropertyName();
		if (what.startsWith("translateWithFixedTip #")) {
			// translate only one of the vertices.
			int num = Integer.parseInt(what.substring(23));
			boolean[] map = new boolean[boxPointer.getPointerCount()];
			map[num - 1] = true;
			Point old = (Point) e.getOldValue();
			Point now = (Point) e.getNewValue(), diff = MSMath.diff(now, old);
			boxPointer.translateWithFixedTips(map, diff.x, diff.y);
		} else if (what.startsWith("setTip #")) {
			// translate only one of the vertices.
			int num = Integer.parseInt(what.substring(8));
			Point old = (Point) e.getOldValue();
			Point now = (Point) e.getNewValue(), diff = MSMath.diff(now, old);
			boxPointer.setTip(num - 1, MSMath.sum(boxPointer.getTip(num - 1), diff));
		} else if (what.startsWith("setTips ")
				|| what.startsWith("translateWithFixedTips ")) {
			boolean setTipsMode = what.startsWith("setTips");
			StringTokenizer stringTok = new StringTokenizer(what
					.substring((setTipsMode) ? 7 : 22));
			int nodeCount = boxPointer.getPointerCount();
			boolean[] map = new boolean[nodeCount];
			int currentNode = 0;
			while (stringTok.hasMoreTokens()) {
				currentNode = Integer.parseInt(stringTok.nextToken());
				if (currentNode > 0 && currentNode <= nodeCount)
					map[currentNode - 1] = true;
			}
			Point old = (Point) e.getOldValue();
			Point now = (Point) e.getNewValue(), diff = MSMath.diff(now, old);
			if (boxPointer.getPointers() != null) {
				if (setTipsMode) {
					int a;
					for (a = 0; a < boxPointer.getPointers().size(); a++)
						if (map[a])
							boxPointer.setTip(a, MSMath.sum(boxPointer.getTip(a), diff));
				} else
					boxPointer.translateWithFixedTips(map, diff.x, diff.y);
			}
		}
	}
}
