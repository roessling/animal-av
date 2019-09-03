package generators.helpers;

import java.awt.Font;
import java.awt.Point;
import java.util.HashMap;

import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.properties.PolylineProperties;
import algoanim.util.Coordinates;

/**
 * 
 * @author Dirk Kröhan, Kamil Erhard
 *
 */
public abstract class TextUtil {
	protected static PolylineProperties polylineProperties = new PolylineProperties();
	public static HashMap<Pair<Point, Point>, Polyline> hashMap = new HashMap<Pair<Point, Point>, Polyline>();
	public static boolean isAnimalLoaded = true;
	private static Font font;

	static {
		polylineProperties.set("hidden", true);
	}

	public static Polyline getVia(Language lang, int x1, int y1, int x2, int y2) {
		Pair<Point, Point> points = new Pair<Point, Point>(new Point(x1, y1), new Point(x2, y2));

		Polyline line = hashMap.get(points);

		if (line == null) {
			Coordinates[] coords = new Coordinates[] { new Coordinates(x1, y1), new Coordinates(x2, y2) };

			// line = ProxmapSort.this.lang.newPoint(coords, coords.toString(), ProxmapSort.this.display);
			line = lang.newPolyline(coords, coords.toString(), null, polylineProperties);
			line.hide();

			hashMap.put(points, line);
		}

		return line;
	}

	public static void setFont(Font font) {
		TextUtil.font = font;

	}

	public static Font getFont() {
		return font;
	}
}
