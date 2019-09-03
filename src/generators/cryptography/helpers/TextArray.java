package generators.cryptography.helpers;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

public class TextArray {
	private Language lang;
	private ArrayList<Text> texts;
	private ArrayList<Rect> rects;
	private ArrayList<Color> colors;
	private TextProperties textProps;
	private Color highlight_color;
	private int x = 0, y = 0,
			// Ausma�e der Zellen des Arrays.
			cell_width = 20,
			height = 20,
			text_indent_x = 4,
			text_indent_y = 2;

	public TextArray(Language lang, Node node, int[] array, Font font,
			Color element_color, Color fill_color, Color highlight_color) {
		this.lang = lang;
		texts = new ArrayList<Text>();
		rects = new ArrayList<Rect>();
		colors = new ArrayList<Color>();
		this.highlight_color = highlight_color;
		x = GetPositionFromNode(node)[0];
		y = GetPositionFromNode(node)[1];
		for (int i = 0; i < array.length; i++) {
			RectProperties rectProps = new RectProperties();
			rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, fill_color);
			// Integrieren der neuen Attribute cell_width und height.
			rects.add(lang.newRect(new Coordinates(x + i * 20, y),
					new Coordinates(x + i * cell_width + cell_width, y + height), "", null,
					rectProps));
		}
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, element_color);
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
		for (int i = 0; i < rects.size(); i++) {
			// Integrieren der neuen Attribute cell_width, text_indent_x und text_indent_y.
			texts.add(lang.newText(new Coordinates(x + i * cell_width + text_indent_x, y + text_indent_y),
					String.valueOf(array[i]), "", null, textProps));
			colors.add(fill_color);
		}
	}

	private int[] GetPositionFromNode(Node node) {
		int x = 0, y = 0;
		if (node.getClass() == Coordinates.class) {
			x = ((Coordinates) node).getX();
			y = ((Coordinates) node).getY();
		} else if (node.getClass() == Offset.class) {
			x = ((Offset) node).getX();
			y = ((Offset) node).getY();
		}
		return new int[] { x, y };
	}

	public void move(String direction, Node node, MsTiming delay,
			MsTiming duration) {
		x = GetPositionFromNode(node)[0];
		y = GetPositionFromNode(node)[1];
		for (int i = 0; i < texts.size(); i++) {
			// Integrieren der neuen Attribute cell_width, text_indent_x und text_indent_y.
			rects.get(i).moveTo(AnimalScript.DIRECTION_NW, "translate",
					new Coordinates(x + i * cell_width, y), delay, duration);
			texts.get(i).moveTo(direction, "translate",
					new Coordinates(x + i * cell_width + text_indent_x, y + text_indent_y), delay, duration);
		}
	}

	public void hide() {
		hide(null);
	}

	public void hide(MsTiming ms_timing) {
		for (int i = 0; i < texts.size(); i++) {
			texts.get(i).hide(ms_timing);
			rects.get(i).hide(ms_timing);
		}
	}

	public void show() {
		show(null);
	}

	public void show(MsTiming ms_timing) {
		for (int i = 0; i < texts.size(); i++) {
			texts.get(i).show(ms_timing);
			rects.get(i).show(ms_timing);
		}
	}

	public void highlight(int position) {
		for (int i = 0; i < rects.size(); i++) {
			if (i == position) {
				rects.get(i).changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
						highlight_color, null, null);
				return;
			}
		}
	}

	public void unhighlight(int position) {
		for (int i = 0; i < rects.size(); i++) {
			if (i == position) {
				rects.get(i).changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
						colors.get(i), null, null);
				return;
			}
		}
	}

	public void setColorAtPosition(int position, Color color) {
		for (int i = 0; i < rects.size(); i++) {
			if (i == position) {
				rects.get(i).changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
						color, null, null);
				colors.set(i, color);
				return;
			}
		}
	}

	public Node getUpperLeft() {
		return rects.get(0).getUpperLeft();
	}

	public Node getLowerRight() {
		return rects.get(rects.size() - 1).getLowerRight();
	}

	public void put(int position, int value) {
		for (int i = 0; i < texts.size(); i++) {
			if (i == position) {
				texts.get(i).hide();
				// Integrieren der neuen Attribute cell_width, text_indent_x und text_indent_y.
				texts.set(
						i,
						lang.newText(new Coordinates(x + i * cell_width + text_indent_x, y + text_indent_y),
								String.valueOf(value), "", null, textProps));
				return;
			}
		}
	}

	public int[] getData() {
		int[] array = new int[texts.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = Integer.valueOf(texts.get(i).getText());
		}
		return array;
	}

	public int size() {
		return texts.size();
	}
}
