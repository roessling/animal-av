package algoanim.primitives.generators;

import algoanim.primitives.Polygon;


/**
 * <code>PolygonGenerator</code> offers methods to request the 
 * included Language object to
 * append polygon related script code lines to the output.
 * It is designed to be included by a <code>Polygon</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Polygon</code> primitive has to 
 * implement its own
 * <code>PolygonGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface PolygonGenerator extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>Polygon</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param p the <code>Polygon</code> for which the initiate script 
	 * code shall be created. 
	 */
	public void create(Polygon p);
}
