package algoanim.util;

import java.awt.Point;

/**
 * This is an abstract representation of a coordinate within the animation
 * screen. Please use concrete instances, such as <em>Coordinates</em> for (x,y)
 * coordinates or <em>Offset</em> for nodes relative to another element.
 * 
 * @author Jens Pfau
 */
public abstract class Node {
	public static Coordinates convertToNode(Point p) {
		if (p == null)
			return new Coordinates(0, 0);
		return new Coordinates(p.x, p.y);
	}
}
