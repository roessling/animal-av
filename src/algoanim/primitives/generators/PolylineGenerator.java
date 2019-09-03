package algoanim.primitives.generators;

import algoanim.primitives.Polyline;

/**
 * <code>PolylineGenerator</code> offers methods to request the included 
 * Language object to
 * append polyline related script code lines to the output.
 * It is designed to be included by a <code>Polyline</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Polyline</code> primitive has to 
 * implement its own
 * <code>PolylineGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface PolylineGenerator extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>Polyline</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param poly the <code>Polyline</code> for which the initiate script 
	 * code shall be created. 
	 */
	public abstract void create(Polyline poly);
}
