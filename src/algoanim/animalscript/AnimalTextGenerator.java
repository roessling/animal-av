package algoanim.animalscript;

import java.awt.Font;

import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Timing;

/**
 * @see algoanim.primitives.generators.TextGenerator
 * @author Stephan Mehlhase
 */
public class AnimalTextGenerator extends AnimalGenerator implements
		TextGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalTextGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.TextGenerator
	 *      #create(algoanim.primitives.Text)
	 */
	public void create(Text t) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(t.getName()) || t.getName() == "") {
			t.setName("Text" + AnimalTextGenerator.count);
			AnimalTextGenerator.count++;
		}
		lang.addItem(t);

		StringBuilder str = new StringBuilder();
		str.append("text \"").append(t.getName()).append("\" \"");
		str.append(t.getText()).append("\" ");
		str.append(AnimalGenerator.makeNodeDef(t.getUpperLeft()));

		TextProperties props = t.getProperties();
    addBooleanOption(props, AnimationPropertiesKeys.CENTERED_PROPERTY, " centered ", str);
		addBooleanOption(props, AnimationPropertiesKeys.RIGHT_PROPERTY, " right ", str);
		addColorOption(props, str);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
		addFontOption(props, AnimationPropertiesKeys.FONT_PROPERTY, str);

		str.append(AnimalGenerator.makeDisplayOptionsDef(t.getDisplayOptions(),
            props));

		lang.addLine(str);
	}

	/**
	 * updates the font of this text component (not supported by all primitives!).
	 * 
	 * @param p the <code>Primitive</code> to change.
	 * @param newFont the new text to be used
	 * @param delay the delay until the operation starts
	 * @param duration the duration for the operation
	 */
	public void setFont(Primitive p, Font newFont, Timing delay, Timing duration) {
		StringBuilder str = new StringBuilder();
		str.append("setFont of \"").append(p.getName()).append("\" to font ");
		str.append(newFont.getFamily()).append(" size ").append(newFont.getSize());
		if (newFont.isBold())
			str.append(" bold");
		if (newFont.isItalic())
			str.append(" italic");
		addWithTiming(str, delay, duration);
	}

	/**
	 * updates the text of this text component (not supported by all primitives!).
	 * 
	 * @param p the <code>Primitive</code> to change.
	 * @param newText the new text to be used
	 * @param delay the delay until the operation starts
	 * @param duration the duration for the operation
	 */

	public void setText(Primitive p, String newText, Timing delay, 
			Timing duration) {
		StringBuilder str = new StringBuilder();
		str.append("setText of \"").append(p.getName()).append("\" to \"");
		str.append(newText).append("\"");
		addWithTiming(str, delay, duration);
	}
}
