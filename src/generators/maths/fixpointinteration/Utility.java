package generators.maths.fixpointinteration;

import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Utility {
	private final Language lang;
	private int lineOffset = 1;
	
	public void setLineOffset(int lineOffset) {
		this.lineOffset = lineOffset;
	}

	public Utility(Language l) {
		lang = l;
	}
	
	public Polyline drawLine(int x1, int y1, int x2, int y2) {
		PolylineProperties properties = new PolylineProperties();
		return drawLine(x1, y1, x2, y2, properties);
	}

	public Polyline drawLine(int x1, int y1, int x2, int y2,
			PolylineProperties properties) {
		Coordinates[] nodes = { new Coordinates(x1, y1),
				new Coordinates(x2, y2) };
		String name = "line-" + Integer.toString(x1) + ","
				+ Integer.toString(y1) + "-" + Integer.toString(x2) + ","
				+ Integer.toString(y2);
		return lang.newPolyline(nodes, name, null, properties);
	}
	
	public Text[] drawText(String string, int x, int y) {
		// set standard text properties
		TextProperties properties = new TextProperties();
		return drawText(string, x, y, properties);
	}

	public Text[] drawText(String string, int x, int y,
			TextProperties properties) {
		Node position = new Coordinates(x, y);
		return drawText(string, position, properties);
	}
	
	public Text[] drawText(String string, Node position,
			TextProperties properties) {
		String[] lines = string.split("\n");
		Text[] texts = new Text[lines.length];
		for (int i = 0; i < lines.length; ++i) {
			texts[i] = lang.newText(position, lines[i],
					"text-" + Integer.toString(i), null, properties);
			position = new Offset(0, lineOffset, texts[i], "SW");
		}
		return texts;
	}
	
}
