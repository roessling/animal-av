package algoanim.primitives.generators;

import algoanim.primitives.CircleSeg;

/**
 * <code>CircleSegGenerator</code> offers methods to request the 
 * included Language object to
 * append circle segment related script code lines to the output.
 * It is designed to be included by a <code>CircleSeg</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>CircleSeg</code> primitive has 
 * to implement its own
 * <code>CircleSegGenerator</code>, which is then responsible to create
 *  proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface CircleSegGenerator extends GeneratorInterface {  
	/**
	 * Creates the originating script code for a given 
	 * <code>CircleSeg</code>, due to the fact that before a primitive can 
	 * be worked with it has to be defined and made known to the script 
	 * language.
	 * 
	 * @param cs the <code>CircleSeg</code> for which the initiate script 
	 * code shall be created. 
	 */
    public void create(CircleSeg cs);
}
