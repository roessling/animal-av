package algoanim.primitives;

import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.TextProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * Represents a text specified by an upper left position and a content.
 * 
 * @author Stephan Mehlhase
 */
public class Text extends AdvancedTextSupport {
	private TextGenerator generator = null;

	private String text = null;

	private Node upperLeft = null;

	private TextProperties properties = null;

	/**
	 * Instantiates the <code>Text</code> and calls the create() method of the
	 * associated <code>TextGenerator</code>.
	 * 
	 * @param tg
	 *          the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *          the upper left corner of this <code>Text</code> element.
	 * @param theText
	 *          the content of this <code>Text</code> element.
	 * @param name
	 *          the name of this <code>Text</code> element.
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> of this
	 *          <code>Text</code> element.
	 * @param tp
	 *          [optional] the properties of this <code>Text</code> element.
	 */
	public Text(TextGenerator tg, Node upperLeftCorner, String theText,
			String name, DisplayOptions display, TextProperties tp) {
		super(tg, display);

		properties = tp;
		generator = tg;
		upperLeft = upperLeftCorner;
		text = theText;
		setName(name);
		generator.create(this);
	}

	/**
	 * Returns the properties of this <code>Text</code> element.
	 * 
	 * @return the properties of this <code>Text</code> element.
	 */
	public TextProperties getProperties() {
		return properties;
	}
	
	@Override
	public void setText(String newText, Timing delay, Timing duration) {
	  this.text = newText;
	  super.setText(newText, delay, duration);
	}

	/**
	 * Returns the content of this <code>Text</code> element.
	 * 
	 * @return the content of this <code>Text</code> element.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Returns the upper left corner of this <code>Text</code> element.
	 * 
	 * @return the upper left corner of this <code>Text</code> element.
	 */
	public Node getUpperLeft() {
		return upperLeft;
	}

	/**
	 * @see algoanim.primitives.Primitive#setName(java.lang.String)
	 */
	public void setName(String newName) {
		properties.setName(newName);
		super.setName(newName);
	}
}
