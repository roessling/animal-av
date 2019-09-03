package algoanim.animalscript;

import algoanim.primitives.Point;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.PointGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;

/**
 * @see algoanim.primitives.generators.PointGenerator
 * @author Administrator
 */
public class AnimalPointGenerator extends AnimalGenerator implements
		PointGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalPointGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.PointGenerator
	 *      #create(algoanim.primitives.Point)
	 */
	public void create(Point aPoint) {
		String name = aPoint.getName();
		if (name == null || name == "" || this.isNameUsed(name)) {
			aPoint.setName("Point" + AnimalPointGenerator.count);
			name = aPoint.getName();
			AnimalPointGenerator.count++;
		}
		lang.addItem(aPoint);

		StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		def.append("point \"").append(name).append("\" ");
		def.append(AnimalGenerator.makeNodeDef(aPoint.getCoords()));

		PointProperties props = aPoint.getProperties();
		addColorOption(props, def);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", def);

		def.append(AnimalGenerator
				.makeDisplayOptionsDef(aPoint.getDisplayOptions(), props));

		lang.addLine(def);
	}

}
