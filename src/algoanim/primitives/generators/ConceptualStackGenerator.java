package algoanim.primitives.generators;

import algoanim.primitives.ConceptualStack;
import algoanim.util.Timing;

/**
 * <code>ConceptualStackGenerator</code> offers methods to request the included 
 * Language object to append conceptual stack related script code lines to the output.
 * It is designed to be included by a <code>ConceptualStack</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>ConceptualStack</code> primitive has to 
 * implement its own <code>ConceptualStackGenerator</code>, which is then responsible
 * to create proper script code.  
 * 
 * @author Dima Vronskyi
 */
public interface ConceptualStackGenerator<T> extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>ConceptualStack</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param cs the <code>ConceptualStack</code> for which the initiate script code 
	 * shall be created.
	 */
	public void create(ConceptualStack<T> cs);
	
	/**
	 * Pushes the element <code>elem</code> onto the top of the given <code>ConceptualStack</code>.
	 * 
	 * @param cs the <code>ConceptualStack</code> onto the top of which to push the element.
	 * @param elem the element to be pushed onto the stack.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void push(ConceptualStack<T> cs, T elem, Timing delay, Timing duration);
	
	/**
	 * Removes the element at the top of the given <code>ConceptualStack</code>.
	 * 
	 * @param cs the <code>ConceptualStack</code> from which to pop the element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void pop(ConceptualStack<T> cs, Timing delay, Timing duration);
	
	/**
	 * Retrieves (without removing) the element at the top of the given <code>ConceptualStack</code>.
	 * 
	 * @param cs the <code>ConceptualStack</code> from which to retrieve the top element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void top(ConceptualStack<T> cs, Timing delay, Timing duration);
	
	/**
	 * Tests if the given <code>ConceptualStack</code> is empty.
	 * 
	 * @param cs the <code>ConceptualStack</code> which is tested.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void isEmpty(ConceptualStack<T> cs, Timing delay, Timing duration);
	
	/**
	 * Highlights the top element of the given <code>ConceptualStack</code>.
	 * 
	 * @param cs the <code>ConceptualStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTopElem(ConceptualStack<T> cs, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the top element of the given <code>ConceptualStack</code>.
	 * 
	 * @param cs the <code>ConceptualStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTopElem(ConceptualStack<T> cs, Timing delay, Timing duration);
	
	/**
	 * Highlights the cell which contains the top element of the given <code>ConceptualStack</code>.
	 * 
	 * @param cs the <code>ConceptualStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTopCell(ConceptualStack<T> cs, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the cell which contains the top element of the given <code>ConceptualStack</code>.
	 * 
	 * @param cs the <code>ConceptualStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTopCell(ConceptualStack<T> cs, Timing delay, Timing duration);
}
