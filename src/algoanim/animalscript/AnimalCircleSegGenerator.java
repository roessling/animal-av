package algoanim.animalscript;

import algoanim.primitives.CircleSeg;
import algoanim.primitives.generators.CircleSegGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleSegProperties;

/**
 * @see algoanim.primitives.generators.CircleSegGenerator
 * @author Stephan Mehlhase
 */
public class AnimalCircleSegGenerator extends AnimalGenerator implements
		CircleSegGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalCircleSegGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 *      #create(animalscriptapi.primitives.CircleSeg)
     * @see algoanim.primitives.generators.CircleSegGenerator
	 */
	public void create(CircleSeg aseg) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(aseg.getName()) || aseg.getName() == "") {
			aseg.setName("CircleSeg" + AnimalCircleSegGenerator.count++);
		}
		lang.addItem(aseg);

		StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
    str.append("circleSeg \"").append(aseg.getName()).append("\" ");
    str.append(AnimalGenerator.makeNodeDef(aseg.getCenter()));
    str.append(" radius ").append(aseg.getRadius());

    CircleSegProperties props = aseg.getProperties();
    addIntOption(props, AnimationPropertiesKeys.ANGLE_PROPERTY, " angle ", str);
    addIntOption(props, AnimationPropertiesKeys.STARTANGLE_PROPERTY,
        " starts ", str);
    if (!addBooleanOption(props, AnimationPropertiesKeys.CLOCKWISE_PROPERTY,
        " clockwise ", str))
      addBooleanOption(props,
          AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY,
          " counterclockwise ", str);
    addColorOption(props, str);
    addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
    if (addBooleanOption(props, AnimationPropertiesKeys.CLOSED_PROPERTY,
        " closed ", str)) {
      if (addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY,
          " filled ", str))
        addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY,
            " fillColor ", str);
    } else { // not closed
      addBooleanOption(props, AnimationPropertiesKeys.FWARROW_PROPERTY,
          " fwArrow ", str);
      addBooleanOption(props, AnimationPropertiesKeys.BWARROW_PROPERTY,
          " bwArrow ", str);
    }
		str.append(AnimalGenerator.makeDisplayOptionsDef(aseg.getDisplayOptions(), props));
		lang.addLine(str);
	}
}
