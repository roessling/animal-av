package generators.searching.topk;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import algoanim.animalscript.addons.bbcode.Code;
import algoanim.animalscript.addons.bbcode.H2;
import algoanim.animalscript.addons.bbcode.NetworkStyle;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

public class SearchingStyle extends NetworkStyle {

	// private static final Color HIGHLIGHT = new Color(236, 101, 0);

	private Map<String, AnimationProperties> map;

	public SearchingStyle(TextProperties h2Props, SourceCodeProperties plainProps,
			SourceCodeProperties codeProps, boolean usedForCode) {
		// init the map
		map = new HashMap<String, AnimationProperties>();

		h2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				((Font) h2Props.get("font")).getFamily(), Font.BOLD, 16));
		setProperties(H2.BB_CODE, h2Props);

		plainProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				((Font) plainProps.get("font")).getFamily(), Font.PLAIN, 12));
		setProperties("plain", plainProps);

		if (usedForCode)
			codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					"Monospaced", Font.PLAIN, 13));
		else
			codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					"SansSerif", Font.PLAIN, 13));
		setProperties(Code.BB_CODE, codeProps);
	}

	@Override
	public AnimationProperties getProperties(String primitive) {
		return map.get(primitive);
	}

	/**
	 * 
	 * Sets the properties for a specific (BBCode) primitive.
	 * 
	 * @param primitive
	 *            The BBCode String for the primitive the properties are set
	 *            for. This might also be primitive composed by the BBCode
	 *            classes and not a AnimalScript primitive
	 * @param properties
	 *            The properties to set for the primitive.
	 */
	private void setProperties(String primitive, AnimationProperties properties) {
		map.put(primitive, properties);
	}
}
