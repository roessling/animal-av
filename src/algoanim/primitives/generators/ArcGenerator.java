package algoanim.primitives.generators;

import algoanim.primitives.Arc;

/**
 * <code>ArcGenerator</code> offers methods to request the included 
 * Language object to
 * append arc related script code lines to the output.
 * It is designed to be included by an <code>Arc</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering an <code>Arc</code> primitive has to 
 * implement its own
 * <code>ArcGenerator</code>, which is then responsible to create proper 
 * script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface ArcGenerator extends GeneratorInterface {  
	/**
	 * Creates the originating script code for a given <code>Arc</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param a the <code>Arc</code> for which the initiate script code
	 * shall be created. 
	 */
    public void create(Arc a);
}
