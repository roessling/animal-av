package generators.misc.helpers;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

/**
 * Encapsulates the functionality for console-like outputs in animal.
 * @author chollubetz
 *
 */
public class Console {
	private Language lang;
	
	private List<Text> texts;
	private Position position;
	private int numberOfRows;
	private Font font;
	
	public Console(Position position, Language lang, int numberOfRows, Font font) {
		this.position = position;
		this.lang = lang;
		this.numberOfRows = numberOfRows;
		this.font = font;
		
		this.texts = new LinkedList<Text>();
	}
	
	/**
	 * writes a given text to the console
	 * @param text the text to write
	 */
	public void writeLine(String text) {
		if (texts.size() == numberOfRows)
			clear();
		
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
		
		lang.newText(new Coordinates(position.getX(), position.getY() + texts.size() * 20), "", "", null);
		texts.add(lang.newText(new Coordinates(position.getX(), position.getY() + texts.size() * 20), text, "", null, tp));
	}
	
	/**
	 * clears the console
	 */
	public void clear() {
		while (texts.size() != 0)
			texts.remove(0).hide();
	}
	
	/**
	 * hides the console
	 */
	public void hide() {
		clear();
	}
	
	/**
	 * writes a given test to the console and begins a new step after that
	 * @param text the text to write
	 */
	public void writeLineAndFinishCurrentStep(String text) {
		writeLine(text);
		lang.nextStep();
	}
}
