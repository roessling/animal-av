package algoanim.animalscript.addons.bbcode;

import java.util.List;

import algoanim.animalscript.addons.MultiPrimitiveAnim;
import algoanim.primitives.Primitive;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * BBCode base element.
 */
public abstract class BBCode extends MultiPrimitiveAnim {
	protected Style s;
	
	public void setProperties(Style style) {
		s = style;
	}

	/**
	 * Get all Primitives created by the BBCode element as a List
	 * 
	 * @param text The Text within the BBCode tags 
	 * @param baseIDRef Alignment of the first primitive
	 * @return A List of Primitives
	 */
	public abstract List<Primitive> getPrimitives(String text, String baseIDRef);
}