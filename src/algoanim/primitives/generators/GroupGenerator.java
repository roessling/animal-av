package algoanim.primitives.generators;

import algoanim.primitives.Group;
import algoanim.primitives.Primitive;

/**
 * <code>GroupGenerator</code> offers methods to request the included 
 * Language object to
 * append group related script code lines to the output.
 * It is designed to be included by a <code>Group</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Group</code> primitive has to 
 * implement its own
 * <code>GroupGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface GroupGenerator extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>Group</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param g the <code>Group</code> for which the initiate script 
	 * code shall be created. 
	 */
	public void create(Group g);
	
	/**
	 * Removes an element from the given <code>Group</code>.
	 * 
	 * @param g the <code>Group</code>.
	 * @param p the element to remove.
	 */
	public void remove(Group g, Primitive p);
}
