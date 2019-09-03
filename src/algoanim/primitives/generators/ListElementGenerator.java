package algoanim.primitives.generators;

import algoanim.primitives.ListElement;
import algoanim.util.Timing;

/**
 * <code>ListElementGenerator</code> offers methods to request the 
 * included Language object to
 * append list element related script code lines to the output.
 * It is designed to be included by a <code>ListElement</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>ListElement</code> primitive has 
 * to implement its own
 * <code>ListElementGenerator</code>, which is then responsible to 
 * create proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface ListElementGenerator extends GeneratorInterface {	
	/**
	 * Creates the originating script code for a given 
	 * <code>ListElement</code>, due to the fact that before a primitive 
	 * can be worked with it has to be defined and made known to the script 
	 * language.
	 * 
	 * @param le the <code>ListElement</code> for which the initiate script 
	 * code shall be created. 
	 */
	public void create(ListElement le);
	
	/**
	 * Links the given <code>ListElement</code> to another one.
	 * 
	 * @param start the original <code>ListElement</code> of the link.	
	 * @param target the target <code>ListElement</code> of the link.
	 * @param linkno If the <code>ListElement</code> has more than one 
	 * link, it is always necessary to identify the link properly by providing
	 * the number of the link.
	 * @param t the time to wait until the operation shall be performed.
	 * @param d the duration of the operation.
	 */
	public void link(ListElement start, ListElement target, int linkno, Timing t,
			Timing d);
	
	/**
	 * Removes a link from the given <code>ListElement</code>.
	 * 
	 * @param start the original <code>ListElement</code> of the link.
	 * @param linknr If the <code>ListElement</code> has more than one 
	 * link, it is always necessary to identify the link properly by providing
	 * 	the number of the link.
	 * @param t the time to wait until the operation shall be performed.
	 * @param d the duration of the operation.
	 */
	public void unlink(ListElement start, int linknr, Timing t, Timing d);
}
