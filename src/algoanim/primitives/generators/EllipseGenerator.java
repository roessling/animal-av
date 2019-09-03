package algoanim.primitives.generators;

import algoanim.primitives.Ellipse;

/**
 * <code>EllipseGenerator</code> offers methods to request the included 
 * Language object to
 * append ellipse related script code lines to the output.
 * It is designed to be included by an <code>Ellipse</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering an <code>Ellipse</code> primitive has to
 *  implement its own
 * <code>EllipseGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface EllipseGenerator extends GeneratorInterface {   
	/**
	 * Creates the originating script code for a given <code>Ellipse</code>,
	 * due to the fact that before a primitive can be worked with it has to be defined
	 * and made known to the script language.
	 * 
	 * @param e the <code>Ellipse</code> for which the initiate script code
	 * shall be created. 
	 */
    public void create(Ellipse e);
}
