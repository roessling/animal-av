package algoanim.primitives.generators;

import algoanim.primitives.EllipseSeg;

/**
 * <code>EllipseSegGenerator</code> offers methods to request the 
 * included Language object to
 * append circle segment related script code lines to the output.
 * It is designed to be included by a <code>EllipseSeg</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>EllipseSeg</code> primitive has 
 * to implement its own
 * <code>EllipseSegGenerator</code>, which is then responsible to create
 *  proper script code.  
 * 
 * @author Guido Roessling
 */
public interface EllipseSegGenerator extends GeneratorInterface {  
	/**
	 * Creates the originating script code for a given 
	 * <code>EllipseSeg</code>, due to the fact that before a primitive can 
	 * be worked with it has to be defined and made known to the script 
	 * language.
	 * 
	 * @param cs the <code>EllipseSeg</code> for which the initiate script 
	 * code shall be created. 
	 */
    public void create(EllipseSeg cs);
}
