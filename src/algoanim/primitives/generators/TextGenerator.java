package algoanim.primitives.generators;

import algoanim.primitives.AdvancedTextGeneratorInterface;
import algoanim.primitives.Text;

/**
 * <code>TextGenerator</code> offers methods to request the included Language
 * object to append text related script code lines to the output. It is designed
 * to be included by a <code>Text</code> primitive, which just redirects
 * action calls to the generator. Each script language offering a
 * <code>Text</code> primitive has to implement its own
 * <code>TextGenerator</code>, which is then responsible to create proper
 * script code.
 * 
 * @author Stephan Mehlhase
 */
public interface TextGenerator 
extends GeneratorInterface, AdvancedTextGeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>Text</code>, due
	 * to the fact that before a primitive can be worked with it has to be defined
	 * and made known to the script language.
	 * 
	 * @param t
	 *          the <code>Text</code> for which the initiate script code shall
	 *          be created.
	 */
	public void create(Text t);

}
