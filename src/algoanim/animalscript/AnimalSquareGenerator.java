package algoanim.animalscript;

import algoanim.primitives.Square;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.SquareGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SquareProperties;

/**
 * @see algoanim.primitives.generators.SquareGenerator
 * @author Stephan Mehlhase
 */
public class AnimalSquareGenerator extends AnimalGenerator implements
		SquareGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalSquareGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.SquareGenerator
	 *      #create(algoanim.primitives.Square)
	 */
	public void create(Square s) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(s.getName()) || s.getName() == "") {
			s.setName("Square" + AnimalSquareGenerator.count);
			AnimalSquareGenerator.count++;
		}
		lang.addItem(s);

		StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		str.append("square \"").append(s.getName()).append("\" ");
		str.append(AnimalGenerator.makeNodeDef(s.getUpperLeft()));
		str.append(" ").append(s.getWidth());

		SquareProperties props = s.getProperties();
		addColorOption(props, str);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
		if (addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY, " filled ", str))
		  addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY, " fillColor ", str);
		str.append(AnimalGenerator.makeDisplayOptionsDef(s.getDisplayOptions(),
            props));
		lang.addLine(str);
	}
}
