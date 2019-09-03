package generators.helpers.candidateElimination;

import java.awt.Font;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class Title {

    public Title(Language lang) {
	TextProperties titleProps = new TextProperties("title");
	titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		"Monospaced", Font.BOLD, 24));

	@SuppressWarnings("unused")
	Text title = lang.newText(new Coordinates(41, 30),
		"Candidate Elimination Algorithm", "title", null, titleProps);
    }

}
