package generators.tree.binarySearchTree.api;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.MultiPrimitiveAnim;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
/**
 * 
 * @author Sebastian Leipe
 * 
 * 	Original Info Box written by Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 	Displays a borderless box with multiple lines of text.
 * 	Slightly altered to have customizable text properties for the plain text.
 *
 */
public class MyInfoBox extends MultiPrimitiveAnim {

private Text hl;
	
	/**
	 * Create a new InfoBox to display.
	 * 
	 * @param lang The Language object to add the box to
	 * @param position The position of the box within the animation
	 * @param size The number of lines of normal text the box can display 
	 * @param headline The content of the the headline
	 */
	public MyInfoBox(Language lang, Node position, int size, String headline, TextProperties textProperties) {
		super(lang);

		TextProperties boxHeadProps = new TextProperties();
		boxHeadProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		boxHeadProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));

		hl = l.newText(position, headline, UUID.randomUUID().toString(), null, boxHeadProps);
		
		Text t = l.newText(new Offset(4, 0, hl, AnimalScript.DIRECTION_SW), "", UUID.randomUUID().toString(), null, textProperties);
		p.add(t);
		for(int i = 1; i < size; i++) {
			t = l.newText(new Offset(0, 0, t, AnimalScript.DIRECTION_SW), "", UUID.randomUUID().toString(), null, textProperties);
			p.add(t);
		}
	}
	
	/**
	 * Change headline
	 * 
	 * @param text The new headline
	 */
	public void setHeadline(String text) {
		hl.setText(text, null, null);
	}
	
	/**
	 * Change the text
	 * 
	 * @param text The new text elements.
	 */
	public void setText(List<String> text) {
		// find the minimum size of the two ArrayLists to iterate
		int size = Math.min(p.size(), text.size());
		for (int i = 0; i < size; i++) {
			Text t = (Text)p.get(i);
			t.setText(text.get(i), null, null);
			p.set(i, t);
		}
		
		// empty all remaining list entries
		for (int i = size; i < p.size(); i++) {
			Text t = (Text)p.get(i);
			t.setText("", null, null);
			p.set(i, t);			
		}
	}
	
	@Override
	public void show() {
		hl.show();
		super.show();
	}
	
	@Override
	public void hide() {
		hl.hide();
		super.hide();
	}
}
