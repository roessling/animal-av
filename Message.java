import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class Message {

//  private Language lang;
  private Text     message;
  private Timing   textDelay = new TicksTiming(25);

  public Message(Language lang) {
    super();
//    this.lang = lang;
    this.message = lang.newText(new Coordinates(350, 50), "ASD",
        "VisualKdTreeMessage", null, getTextProperties());
    System.err.println(lang.toString());
  }

  private TextProperties getTextProperties() {
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    return textProps;
  }

  public void setText(String message) {
    this.message.setText(message, this.textDelay, null);
  }

  public static void main(String[] args) {
    new Message(new AnimalScript("Demo", "Guido", 320, 240));
  }
}