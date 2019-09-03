package algoanim.animalscript;

import algoanim.primitives.Ellipse;
import algoanim.primitives.generators.EllipseGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.EllipseProperties;

/**
 * @see algoanim.primitives.generators.EllipseGenerator
 * @author Stephan Mehlhase
 */
public class AnimalEllipseGenerator extends AnimalGenerator implements
		EllipseGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalEllipseGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.EllipseGenerator
	 *      #create(algoanim.primitives.Ellipse)
	 */
	public void create(Ellipse aellipse) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(aellipse.getName()) || aellipse.getName() == "") {
			aellipse.setName("Ellipse" + AnimalEllipseGenerator.count++);
//			AnimalEllipseGenerator.count++;
		}
		lang.addItem(aellipse);

		StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		str.append("ellipse \"").append(aellipse.getName()).append("\" ");
		str.append(AnimalGenerator.makeNodeDef(aellipse.getCenter()));
		str.append(" radius ").append(AnimalGenerator.makeNodeDef(aellipse.getRadius()));

		EllipseProperties props = aellipse.getProperties();
		addColorOption(props, str);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
		addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY, " filled ", str);
		addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY, " fillColor ", str);

		str.append(AnimalGenerator.makeDisplayOptionsDef(aellipse
				.getDisplayOptions(), props));
		lang.addLine(str);
	}
}
