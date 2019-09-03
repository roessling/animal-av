/*
 * Erstellung: 06.12.2004
 *
 */
package algoanim.animalscript;

import algoanim.primitives.Circle;
import algoanim.primitives.generators.CircleGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;

/**
 * @see algoanim.primitives.generators.CircleGenerator
 * @author Stephan Mehlhase
 */
public class AnimalCircleGenerator extends AnimalGenerator implements
		CircleGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalCircleGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.CircleGenerator
	 *      #create(algoanim.primitives.Circle)
	 */
	public void create(Circle acircle) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(acircle.getName()) || acircle.getName() == "") {
			acircle.setName("Circle" + AnimalCircleGenerator.count);
			AnimalCircleGenerator.count++;
		}
		lang.addItem(acircle);

		StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		str.append("circle \"").append(acircle.getName()).append("\" ");
		str.append(AnimalGenerator.makeNodeDef(acircle.getCenter()));
		str.append(" radius ").append(acircle.getRadius());

		CircleProperties props = acircle.getProperties();
		addColorOption(props, AnimationPropertiesKeys.COLOR_PROPERTY, " color ", str);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
		if (addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY, " filled ", str))
		  addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY, " fillColor ", str);
		str.append(AnimalGenerator.makeDisplayOptionsDef(
            acircle.getDisplayOptions(), props));

		lang.addLine(str);
	}
}
