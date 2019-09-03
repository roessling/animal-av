package algoanim.animalscript.addons.bbcode;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * Global styling used for all networking generators.
 *
 */
public class NetworkStyle implements Style {
	private static final Color HIGHLIGHT = new Color(236, 101, 0); // new Color(89, 143, 68);
	
	private Map<String, AnimationProperties> map;
	
	/**
	 * Define all styles on class creation
	 */
	public NetworkStyle() {
		// init the map
		map = new HashMap<String, AnimationProperties>();

		// text properties for Header 1
		TextProperties h1Props = new TextProperties();
		h1Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, HIGHLIGHT);
		h1Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		setProperties(H1.BB_CODE, h1Props);

		// text properties for Header 2
		TextProperties h2Props = new TextProperties();
		h2Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, HIGHLIGHT);
		h2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		setProperties(H2.BB_CODE, h2Props);

		// text properties for Header 2
		TextProperties h3Props = new TextProperties();
		h3Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		h3Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
		setProperties(H3.BB_CODE, h3Props);
		
		// text properties for Copyright information
		TextProperties copyProps = new TextProperties();
		copyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		copyProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 10));
		copyProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		setProperties(Copy.BB_CODE, copyProps);
		
		// source code properties for plain text
		SourceCodeProperties plainProps = new SourceCodeProperties();
		plainProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		plainProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
		setProperties(Plain.BB_CODE, plainProps);

		// source code properties for - well - source code
		SourceCodeProperties codeProps = new SourceCodeProperties();
		codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, HIGHLIGHT);
		codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 13));
		setProperties(Code.BB_CODE, codeProps);

		
		// graph properties
		GraphProperties graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
		graphProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, HIGHLIGHT);
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, HIGHLIGHT);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
		setProperties(Graph.BB_CODE, graphProps);
		
		// matrix properties
		MatrixProperties matrixProps = new MatrixProperties();
		matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, HIGHLIGHT);
		matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, HIGHLIGHT);
		matrixProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		setProperties(Matrix.BB_CODE, matrixProps);
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
