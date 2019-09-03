package algoanim.primitives.generators;

import algoanim.primitives.ArrayBasedStack;
import algoanim.util.Timing;

/**
 * <code>ArrayBasedStackGenerator</code> offers methods to request the included 
 * Language object to append array-based stack related script code lines to the output.
 * It is designed to be included by an <code>ArrayBasedStack</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering an <code>ArrayBasedStack</code> primitive has to 
 * implement its own <code>ArrayBasedStackGenerator</code>, which is then responsible
 * to create proper script code.  
 * 
 * @author Dima Vronskyi
 */
public interface ArrayBasedStackGenerator<T> extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>ArrayBasedStack</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param abs the <code>ArrayBasedStack</code> for which the initiate script code 
	 * shall be created.
	 */
	public void create(ArrayBasedStack<T> abs);
	
	/**
	 * Pushes the element <code>elem</code> onto the top of the given <code>ArrayBasedStack</code>.
	 * 
	 * @param abs the <code>ArrayBasedStack</code> onto the top of which to push the element.
	 * @param elem the element to be pushed onto the stack.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void push(ArrayBasedStack<T> abs, T elem, Timing delay, Timing duration);
	
	/**
	 * Removes the element at the top of the given <code>ArrayBasedStack</code>.
	 * 
	 * @param abs the <code>ArrayBasedStack</code> from which to pop the element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void pop(ArrayBasedStack<T> abs, Timing delay, Timing duration);
	
	/**
	 * Retrieves (without removing) the element at the top of the given <code>ArrayBasedStack</code>.
	 * 
	 * @param abs the <code>ArrayBasedStack</code> from which to retrieve the top element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void top(ArrayBasedStack<T> abs, Timing delay, Timing duration);
	
	/**
	 * Tests if the given <code>ArrayBasedStack</code> is empty.
	 * 
	 * @param abs the <code>ArrayBasedStack</code> which is tested.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void isEmpty(ArrayBasedStack<T> abs, Timing delay, Timing duration);
	
	
	/**
	 * Tests if the given <code>ArrayBasedStack</code> is full.
	 * 
	 * @param abs the <code>ArrayBasedStack</code> which is tested.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void isFull(ArrayBasedStack<T> abs, Timing delay, Timing duration);
	
	/**
	 * Highlights the top element of the given <code>ArrayBasedStack</code>.
	 * 
	 * @param abs the <code>ArrayBasedStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTopElem(ArrayBasedStack<T> abs, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the top element of the given <code>ArrayBasedStack</code>.
	 * 
	 * @param abs the <code>ArrayBasedStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTopElem(ArrayBasedStack<T> abs, Timing delay, Timing duration);
	
	/**
	 * Highlights the cell which contains the top element of the given <code>ArrayBasedStack</code>.
	 * 
	 * @param abs the <code>ArrayBasedStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTopCell(ArrayBasedStack<T> abs, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the cell which contains the top element of the given <code>ArrayBasedStack</code>.
	 * 
	 * @param abs the <code>ArrayBasedStack</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTopCell(ArrayBasedStack<T> abs, Timing delay, Timing duration);
}
