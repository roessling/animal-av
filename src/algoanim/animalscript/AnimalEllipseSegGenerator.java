package algoanim.animalscript;

import algoanim.primitives.EllipseSeg;
import algoanim.primitives.generators.EllipseSegGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.EllipseSegProperties;

/**
 * @see algoanim.primitives.generators.EllipseSegGenerator
 * @author Stephan Mehlhase
 */
public class AnimalEllipseSegGenerator extends AnimalGenerator implements
		EllipseSegGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalEllipseSegGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 *      #create(animalscriptapi.primitives.EllipseSeg)
     * @see algoanim.primitives.generators.EllipseSegGenerator
	 */
	public void create(EllipseSeg aseg) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(aseg.getName()) || aseg.getName() == "") {
			aseg.setName("EllipseSeg" + AnimalEllipseSegGenerator.count++);
		}
		lang.addItem(aseg);

		StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		str.append("ellipseSeg \"").append(aseg.getName()).append("\" ");
		str.append(AnimalGenerator.makeNodeDef(aseg.getCenter()));
		str.append(" radius ").append(AnimalGenerator.makeNodeDef(aseg.getRadius()));

		EllipseSegProperties props = aseg.getProperties();
    addIntOption(props, AnimationPropertiesKeys.ANGLE_PROPERTY, " angle ", str);
    addIntOption(props, AnimationPropertiesKeys.STARTANGLE_PROPERTY, " starts ", str);
		addBooleanOption(props, AnimationPropertiesKeys.CLOCKWISE_PROPERTY, " clockwise ", str);
    addBooleanOption(props, AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY, 
        " counterclockwise ", str);
    addColorOption(props, str);
    addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
    if (addBooleanOption(props, AnimationPropertiesKeys.CLOSED_PROPERTY, 
        " closed ", str)) {
      if (addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY,
          " filled ", str))
        addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY, 
            " fillColor ", str);
    } else {
      addBooleanOption(props, AnimationPropertiesKeys.FWARROW_PROPERTY, 
          " fwArrow ", str);
      addBooleanOption(props, AnimationPropertiesKeys.BWARROW_PROPERTY, 
          " bwArrow ", str);
    }
		str.append(AnimalGenerator.makeDisplayOptionsDef(aseg.getDisplayOptions(), props));
		lang.addLine(str);
	}
}
