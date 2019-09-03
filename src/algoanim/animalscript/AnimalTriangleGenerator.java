package algoanim.animalscript;

import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.TriangleGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TriangleProperties;
import algoanim.util.Node;

/**
 * @see algoanim.primitives.generators.TriangleGenerator
 * @author Stephan Mehlhase
 */
public class AnimalTriangleGenerator extends AnimalGenerator implements
		TriangleGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalTriangleGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.TriangleGenerator
	 *      #create(algoanim.primitives.Triangle)
	 */
	public void create(Triangle t) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(t.getName()) || t.getName() == "") {
			t.setName("Triangle" + AnimalTriangleGenerator.count);
			AnimalTriangleGenerator.count++;
		}
		lang.addItem(t);

		StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		str.append("triangle \"").append(t.getName()).append("\" ");
		Node[] p = t.getNodes();
		str.append(AnimalGenerator.makeNodeDef(p[0]));
		str.append(" ").append(AnimalGenerator.makeNodeDef(p[1]));
		str.append(" ").append(AnimalGenerator.makeNodeDef(p[2]));

		TriangleProperties props = t.getProperties();
		addColorOption(props, str);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
		if (addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY, " filled ", str))
		  addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY, " fillColor ", str);

		str.append(AnimalGenerator.makeDisplayOptionsDef(t.getDisplayOptions(),
            props));

		lang.addLine(str);
	}
}
