package algoanim.primitives.generators;

import algoanim.primitives.Point;


/**
 * <code>PointGenerator</code> offers methods to request the included 
 * Language object to
 * append point related script code lines to the output.
 * It is designed to be included by a <code>Point</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Point</code> primitive has to 
 * implement its own
 * <code>PointGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface PointGenerator extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>Point</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param p the <code>Point</code> for which the initiate script code
	 * shall be created. 
	 */
	public void create(Point p);
}
