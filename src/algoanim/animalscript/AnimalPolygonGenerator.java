package algoanim.animalscript;

import algoanim.primitives.Polygon;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.PolygonGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolygonProperties;
import algoanim.util.Node;

/**
 * @see algoanim.primitives.generators.PolygonGenerator
 * @author Stephan Mehlhase
 * 
 */
public class AnimalPolygonGenerator extends AnimalGenerator implements
		PolygonGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalPolygonGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.PolygonGenerator
	 *      #create(algoanim.primitives.Polygon)
	 */
	public void create(Polygon p) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(p.getName()) || p.getName() == "") {
			p.setName("Polygon" + AnimalPolygonGenerator.count);
			AnimalPolygonGenerator.count++;
		}
		lang.addItem(p);

		StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		str.append("polygon \"").append(p.getName()).append("\" ");

		Node[] nodes = p.getNodes();
		for (int i = 0; i < nodes.length; i++) {
			str.append(' ').append(AnimalGenerator.makeNodeDef(nodes[i]));
		}

		PolygonProperties props = p.getProperties();
		addColorOption(props, str);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
		if (addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY, " filled ", str))
		  addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY, " fillColor ", str);
		str.append(AnimalGenerator.makeDisplayOptionsDef(p.getDisplayOptions(), props));

		lang.addLine(str);
	}
}
