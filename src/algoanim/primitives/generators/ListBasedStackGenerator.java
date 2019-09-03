package algoanim.primitives.generators;

import algoanim.primitives.ListBasedStack;
import algoanim.util.Timing;

/**
 * <code>ListBasedStackGenerator</code> offers methods to request the included 
 * Language object to append list-based stack related script code lines to the output.
 * It is designed to be included by a <code>ListBasedStack</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>ListBasedStack</code> primitive has to 
 * implement its own <code>ListBasedStackGenerator</code>, which is then responsible
 * to create proper script code.  
 *
 * @author Dima Vronskyi
 */
public interface ListBasedStackGenerator<T> extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>ListBasedStackGenerator</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param lbs the <code>ListBasedStack</code> for which the initiate script code 
	 * shall be created.
	 */
	public void create(ListBasedStack<T> lbs);
	
	/**
	 * Pushes the element <code>elem</code> onto the top of the given <code>ListBasedStack</code>.
	 * 
	 * @param lbs the <code>ListBasedStack</code> onto the top of which to push the element.
	 * @param elem the element to be pushed onto the stack.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void push(ListBasedStack<T> lbs, T elem, Timing delay, Timing duration);
	
	/**
	 * Removes the element at the top of the given <code>ListBasedStack</code>.
	 * 
	 * @param lbs the <code>ListBasedStack</code> from which to pop the element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void pop(ListBasedStack<T> lbs, Timing delay, Timing duration);
	
	/**
	 * Retrieves (without removing) the element at the top of the given <code>ListBasedStack</code>.
	 * 
	 * @param lbs the <code>ListBasedStack</code> from which to retrieve the top element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void top(ListBasedStack<T> lbs, Timing delay, Timing duration);
	
	/**
	 * Tests if the given <code>ListBasedStack</code> is empty.
	 * 
	 * @param lbs the <code>ListBasedStack</code> which is tested.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void isEmpty(ListBasedStack<T> lbs, Timing delay, Timing duration);
	
	/**
	 * Highlights the top element of the given <code>ListBasedStack</code>.
	 * 
	 * @param lbs the <code>ListBasedStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTopElem(ListBasedStack<T> lbs, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the top element of the given <code>ListBasedStack</code>.
	 * 
	 * @param lbs the <code>ListBasedStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTopElem(ListBasedStack<T> lbs, Timing delay, Timing duration);
	
	/**
	 * Highlights the cell which contains the top element of the given <code>ListBasedStack</code>.
	 * 
	 * @param lbs the <code>ListBasedStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTopCell(ListBasedStack<T> lbs, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the cell which contains the top element of the given <code>ListBasedStack</code>.
	 * 
	 * @param lbs the <code>ListBasedStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTopCell(ListBasedStack<T> lbs, Timing delay, Timing duration);
}
