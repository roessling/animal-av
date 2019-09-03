package generators.helpers.kdTree;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * @author mateusz
 */
public class Message {

    @SuppressWarnings("unused")
    private Language lang;
    private Text message;
    private Timing textDelay = new TicksTiming(25);

    public Message(Language lang) {
	super();
	this.lang = lang;
	this.message = lang.newText(new Coordinates(200, 50), "",
		"VisualKdTreeMessage", null, getTextProperties());
    }

    private TextProperties getTextProperties() {
	TextProperties textProps = new TextProperties();
	textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
	return textProps;
    }

    public void setText(String message) {
	this.message.setText(message, this.textDelay, null);
    }

}
