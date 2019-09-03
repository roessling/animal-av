package generators.misc.helpers;
import java.awt.Font;

import algoanim.primitives.generators.Language;

/**
 * Encapsulates the functionality of textbox-like outputs in animal.
 * @author chollubetz
 *
 */
public class TextBlock {

	private Console console;
	private int numberOfCharsInARow;

	public TextBlock(Position position, Language lang, int numberOfRows,
			int numberOfCharsInARow, Font font) {
		this.console = new Console(position, lang, numberOfRows, font);
		this.numberOfCharsInARow = numberOfCharsInARow;
	}

	/**
	 * Inserts a text, that may contain many lines, in the textbox.
	 * @param text the text to insert in the textbox
	 */
	public void insertText(String text) {
		String tmpLine = "";
		String[] lines = text.split("\n");
		for (String currentLine : lines) {
			for (String word : currentLine.split(" ")) {
				if (tmpLine.length() + word.length() >= numberOfCharsInARow) {
					console.writeLine(tmpLine);
					tmpLine = "";
				}
				tmpLine += word + " ";
			}
			console.writeLine(tmpLine);
			tmpLine = "";
		}
	}

	/**
	 * Hides the textbox.
	 */
	public void hide() {
		console.clear();
	}
}
