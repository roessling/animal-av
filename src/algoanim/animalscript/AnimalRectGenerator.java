package algoanim.animalscript;

import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.RectGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;

/**
 * @see algoanim.primitives.generators.RectGenerator
 * @author Stephan Mehlhase
 */
public class AnimalRectGenerator extends AnimalGenerator implements
		RectGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalRectGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.RectGenerator
	 *      #create(algoanim.primitives.Rect)
	 */
	public void create(Rect arect) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(arect.getName()) || arect.getName() == "") {
			arect.setName("Rect" + AnimalRectGenerator.count);
			AnimalRectGenerator.count++;
		}
		lang.addItem(arect);

		StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);

		str.append("rectangle \"").append(arect.getName()).append("\" ");
		str.append(AnimalGenerator.makeNodeDef(arect.getUpperLeft()));
		str.append(" ").append(AnimalGenerator.makeNodeDef(arect.getLowerRight()));

		RectProperties props = arect.getProperties();
		addColorOption(props, str);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
		if (addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY, " filled ", str))
		  addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY, " fillColor ", str);
		str.append(AnimalGenerator.makeDisplayOptionsDef(arect.getDisplayOptions(), props));
		lang.addLine(str);
	}

}
