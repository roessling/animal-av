package generators.tree.binarySearchTree.api;

import java.util.HashMap;

import algoanim.animalscript.addons.bbcode.H2;
import algoanim.animalscript.addons.bbcode.Plain;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.properties.AnimationProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

/**
 * 
 * @author Sebastian Leipe
 *
 * A Slide Style used for the BinarySearchTree Animation.
 */
public class BinaryTreeStyle implements Style {

	private HashMap<String, AnimationProperties> propMap;
	
	public BinaryTreeStyle(TextProperties headerProps, SourceCodeProperties textProps){
		propMap = new HashMap<String, AnimationProperties>();
		
		this.setProperties(H2.BB_CODE, headerProps);
		this.setProperties(Plain.BB_CODE, textProps);
	}
	
	@Override
	public AnimationProperties getProperties(String primitive) {
		return this.propMap.get(primitive);
	}

	/**
	 * Used to set additional properties
	 * @param primitive BBCode String.
	 * @param properties The properties.
	 */
	public void setProperties(String primitive, AnimationProperties properties){
		propMap.put(primitive, properties);
	}
}
