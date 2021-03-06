package algoanim.primitives.generators;

import algoanim.primitives.Rect;

/**
 * <code>RectGenerator</code> offers methods to request the included 
 * Language object to
 * append rectangle related script code lines to the output.
 * It is designed to be included by a <code>Rect</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Rect</code> primitive has to 
 * implement its own
 * <code>RectGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface RectGenerator extends GeneratorInterface {

	/**
	 * Creates the originating script code for a given <code>Rect</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param r the <code>Rect</code> for which the initiate script code
	 * shall be created. 
	 */
    public void create(Rect r);
}
