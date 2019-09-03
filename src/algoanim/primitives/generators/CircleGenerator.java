package algoanim.primitives.generators;

import algoanim.primitives.Circle;

/**
 * <code>CircleGenerator</code> offers methods to request the included 
 * Language object to
 * append circle related script code lines to the output.
 * It is designed to be included by a <code>Circle</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Circle</code> primitive has to
 *  implement its own
 * <code>CircleGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface CircleGenerator extends GeneratorInterface {    
	/**
	 * Creates the originating script code for a given <code>Circle</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param c the <code>Circle</code> for which the initiate script code
	 * shall be created. 
	 */
    public void create(Circle c);
}
