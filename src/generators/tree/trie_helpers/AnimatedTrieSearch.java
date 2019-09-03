package generators.tree.trie_helpers;

import java.awt.Color;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class AnimatedTrieSearch
{
	Language lang;
	Variables vars;
	TrieLayout layout;
	InformationDisplaySearch info_display;

	Color default_color;
	Color highlight_color;

	AnimatedTrieNode root_node;

	public AnimatedTrieSearch(Language lang_, Variables vars_, TrieLayout layout_, Color default_color_, Color highlight_color_, InformationDisplaySearch info_display_)
	{
		lang = lang_;
		vars = vars_;
		layout = layout_;
		default_color = default_color_;
		highlight_color = highlight_color_;
		root_node = new AnimatedTrieNode();
		info_display = info_display_;

		CircleProperties cp = new CircleProperties();
		root_node.key = " ";
		root_node.coords = layout.coordinates.get(root_node.key);
		root_node.circle = lang.newCircle(root_node.coords, layout.circle_radius, root_node.key, null, cp);

		lang.newText(new Coordinates(root_node.coords.getX() - 12, root_node.coords.getY() - 6), "root", "null", null);
	}

	public void init(String[] allStrings)
	{
		CircleProperties cp = new CircleProperties();
		PolylineProperties pp = new PolylineProperties();
		pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

		for (String string : allStrings) {
			AnimatedTrieNode node = root_node;
			for (int i = 0; i != string.length(); ++i)
			{
				char c = string.charAt(i);
				int char_index = c - 'a';
				AnimatedTrieNode child_node = node.children[char_index];

				if (child_node == null)
				{
					node.children[char_index] = new AnimatedTrieNode();
					child_node = node.children[char_index];

					child_node.key = c + node.key;
					child_node.coords = layout.coordinates.get(child_node.key);

					child_node.circle = lang.newCircle(child_node.coords, layout.circle_radius, child_node.key, null, cp);

					Node[] vertices =
					{
						new Offset(0, 0, node.key, AnimalScript.DIRECTION_S),
						new Offset(0, 0, child_node.key, AnimalScript.DIRECTION_N)
					};
					child_node.polyline = lang.newPolyline(vertices, "pl_" + child_node.key, null, pp);

					int tmp_x = child_node.coords.getX() - node.coords.getX();
					if (tmp_x < 0) // child to the left of it
					{
						tmp_x *= 0.5;
						tmp_x -= 8;
					}
					else
					{
						tmp_x *= 0.5;
						tmp_x += 5;
					}
					algoanim.util.Node upper_left = new Offset(tmp_x, layout.vertical_spacing / 2, node.key, AnimalScript.DIRECTION_N);
					TextProperties tp = new TextProperties();
					child_node.text = lang.newText(upper_left, String.valueOf(c), "null", null, tp);
				}

				node = child_node;
			}
			node.end_of_word = true;
			Offset inner_coords = new Offset(0, layout.circle_radius, node.key, AnimalScript.DIRECTION_N);
			Circle inner_circle =  lang.newCircle(inner_coords, layout.circle_radius / 2, "null", null, cp);
		}
	}

	public boolean search(String string)
	{
		AnimatedTrieNode node = root_node;

		info_display.src_code.highlight(0, 0, true);
		info_display.src_code.highlight(10, 0, true);
		info_display.src_code.highlight(1);

		node.circle.changeColor("", highlight_color, null, null);
		lang.nextStep("search(" + string + ")");

		info_display.src_code.unhighlight(1);
		info_display.src_code.highlight(2, 0, true);
		info_display.src_code.highlight(8, 0, true);

		vars.declare("int", "i", "0");
		vars.declare("String", "c", "");
		for (int i = 0; i != string.length(); ++i)
		{
			vars.set("i", String.valueOf(i));
			info_display.HighlightChar(i);

			char c = string.charAt(i);
			vars.set("c", String.valueOf(c));
			int char_index = c - 'a';
			AnimatedTrieNode child_node = node.children[char_index];
			info_display.src_code.highlight(3);
			lang.nextStep();

			info_display.src_code.unhighlight(3);
			info_display.src_code.highlight(4);
			lang.nextStep();
			info_display.src_code.unhighlight(4);

			info_display.IncrNumOfComparisons();
			if (child_node == null)
			{
				info_display.src_code.highlight(4, 0, true);
				info_display.src_code.highlight(6, 0, true);
				info_display.src_code.highlight(5);
				lang.nextStep();

				info_display.src_code.unhighlight(4);
				info_display.src_code.unhighlight(5);
				info_display.src_code.unhighlight(6);
				info_display.UnhighlightChar(i);
				node.circle.changeColor("", default_color, null, null);
				return false;
			}
			child_node.polyline.changeColor("", highlight_color, null, null);
			child_node.text.changeColor("", highlight_color, null, null);
			child_node.circle.changeColor("", highlight_color, null, null);
			node.circle.changeColor("", default_color, null, null);
			node = child_node;
			info_display.src_code.highlight(7);
			lang.nextStep();

			node.polyline.changeColor("", default_color, null, null);
			node.text.changeColor("", default_color, null, null);
			info_display.src_code.unhighlight(7);
			lang.nextStep();
			info_display.UnhighlightChar(i);
		}
		vars.discard("i");
		vars.discard("c");

		info_display.src_code.unhighlight(2);
		info_display.src_code.unhighlight(8);
		info_display.src_code.highlight(9);
		lang.nextStep();

		node.circle.changeColor("", default_color, null, null);
		info_display.src_code.unhighlight(0);
		info_display.src_code.unhighlight(9);
		info_display.src_code.unhighlight(10);
		return node.end_of_word;
	}

	class AnimatedTrieNode
	{
		boolean end_of_word;
		AnimatedTrieNode[] children;

		Coordinates coords;
		String key;
		Polyline polyline;
		Circle circle;
		Text text;

		AnimatedTrieNode()
		{
			children = new AnimatedTrieNode[26];
		}
	}
}
