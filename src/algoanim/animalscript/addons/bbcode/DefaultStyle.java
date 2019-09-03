package algoanim.animalscript.addons.bbcode;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * A default style definition.
 *
 */
public class DefaultStyle implements Style {
	private static final Color HIGHLIGHT = new Color(89, 143, 68);
	
	private Map<String, AnimationProperties> map;
	
	public DefaultStyle() {
		// init the map
		map = new HashMap<String, AnimationProperties>();
	
		// default text properties for Header 2
		TextProperties h2Props = new TextProperties();
		h2Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, HIGHLIGHT);
		h2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		setProperties(H2.BB_CODE, h2Props);
		
		// default text properties for Copyright information
		TextProperties copyProps = new TextProperties();
		copyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		copyProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 10));
		copyProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		setProperties(Copy.BB_CODE, copyProps);
		
		// default source code properties for plain text
		SourceCodeProperties plainProps = new SourceCodeProperties();
		plainProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		plainProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
		setProperties(Plain.BB_CODE, plainProps);
	}
	
	@Override
	public AnimationProperties getProperties(String primitive) {
		return map.get(primitive);
	}
	
	/**
	 * 
	 * Sets the properties for a specific (BBCode) primitive.
	 * 
	 * @param primitive The BBCode String for the primitive the properties are set for. 
	 * This might also be primitive composed by the BBCode classes and not a AnimalScript primitive 
	 * @param properties The properties to set for the primitive.
	 */	
	private void setProperties(String primitive, AnimationProperties properties) {
		map.put(primitive, properties);
	}

}
