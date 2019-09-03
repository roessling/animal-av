package algoanim.primitives.generators;

import algoanim.primitives.Square;

/**
 * <code>SquareGenerator</code> offers methods to request the included 
 * Language object to
 * append square related script code lines to the output.
 * It is designed to be included by a <code>Square</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Square</code> primitive has to 
 * implement its own
 * <code>SquareGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface SquareGenerator extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>Square</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param s the <code>Square</code> for which the initiate script code
	 * shall be created. 
	 */
    public void create(Square s);
}
