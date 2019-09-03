package algoanim.primitives.generators;

import algoanim.primitives.Triangle;

/**
 * <code>TriangleGenerator</code> offers methods to request the 
 * included Language object to
 * append triangle related script code lines to the output.
 * It is designed to be included by a <code>Triangle</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Triangle</code> primitive has to 
 * implement its own
 * <code>TriangleGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface TriangleGenerator extends GeneratorInterface { 
	/**
	 * Creates the originating script code for a given <code>Triangle</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param t the <code>Triangle</code> for which the initiate script 
	 * code shall be created. 
	 */
    public void create(Triangle t);
}
