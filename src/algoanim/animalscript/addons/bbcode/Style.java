package algoanim.animalscript.addons.bbcode;

import algoanim.properties.AnimationProperties;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Provides an interface for all style definitions used in BBCode (and elsewhere)
 * to get a consistent look throughout the animation.
 */
public interface Style {
	/**
	 * Return all style properties for a given primitive identified by its BBCode.
	 * 
	 * @param primitive The primitive's BBCode
	 * @return All properties for the given primitive
	 */
	public AnimationProperties getProperties(String primitive);
}
