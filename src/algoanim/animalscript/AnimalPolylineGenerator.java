package algoanim.animalscript;

import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.PolylineGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.util.Node;

/**
 * @author stephan
 * 
 */
public class AnimalPolylineGenerator extends AnimalGenerator implements
		PolylineGenerator {

	private static int count = 1;

	public AnimalPolylineGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.PolylineGenerator
	 *      #create(algoanim.primitives.Polyline)
	 */
	public void create(Polyline poly) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(poly.getName()) || poly.getName() == "") {
			poly.setName("Polyline" + AnimalPolylineGenerator.count);
			AnimalPolylineGenerator.count++;
		}
		lang.addItem(poly);

		StringBuilder str = new StringBuilder();
		str.append("polyline \"").append(poly.getName()).append("\"");

		Node[] nodes = poly.getNodes();
		for (int i = 0; i < nodes.length; i++) {
			str.append(' ').append(AnimalGenerator.makeNodeDef(nodes[i]));
		}

		PolylineProperties props = poly.getProperties();
		addColorOption(props, str);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
		addBooleanOption(props, AnimationPropertiesKeys.FWARROW_PROPERTY, " fwArrow ", str);
    addBooleanOption(props, AnimationPropertiesKeys.BWARROW_PROPERTY, " bwArrow ", str);
		str.append(AnimalGenerator.makeDisplayOptionsDef(poly.getDisplayOptions(),
            props));
		lang.addLine(str);
	}

}
