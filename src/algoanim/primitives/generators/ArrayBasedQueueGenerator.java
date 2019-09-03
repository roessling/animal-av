package algoanim.primitives.generators;

import algoanim.primitives.ArrayBasedQueue;
import algoanim.util.Timing;

/**
 * <code>ArrayBasedQueueGenerator</code> offers methods to request the included 
 * Language object to append array-based queue related script code lines to the output.
 * It is designed to be included by an <code>ArrayBasedQueue</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering an <code>ArrayBasedQueue</code> primitive has to 
 * implement its own <code>ArrayBasedQueueGenerator</code>, which is then responsible
 * to create proper script code.  
 *
 * @author Dima Vronskyi
 */
public interface ArrayBasedQueueGenerator<T> extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>ArrayBasedQueue</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> for which the initiate script code 
	 * shall be created.
	 */
	public void create(ArrayBasedQueue<T> abq);
	
	/**
	 * Adds the element <code>elem</code> as the last element to the end of the given
	 * <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> to the end of which to add the element.
	 * @param elem the element to be added to the end of the queue.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void enqueue(ArrayBasedQueue<T> abq, T elem, Timing delay, Timing duration);
	
	/**
	 * Removes the first element of the given <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> from which to remove the first element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void dequeue(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Retrieves (without removing) the first element of the given <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> from which to retrieve the first element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void front(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Retrieves (without removing) the last element of the given <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> from which to retrieve the last element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void tail(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Tests if the given <code>ArrayBasedQueue</code> is empty.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> which is tested.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void isEmpty(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Tests if the given <code>ArrayBasedQueue</code> is full.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> which is tested.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void isFull(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Highlights the first element of the given <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightFrontElem(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the first element of the given <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightFrontElem(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Highlights the cell which contains the first element of the given 
	 * <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightFrontCell(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the cell which contains the first element of the given 
	 * <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightFrontCell(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Highlights the last element of the given <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTailElem(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the last element of the given <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTailElem(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Highlights the cell which contains the last element of the given 
	 * <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTailCell(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the cell which contains the last element of the given 
	 * <code>ArrayBasedQueue</code>.
	 * 
	 * @param abq the <code>ArrayBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTailCell(ArrayBasedQueue<T> abq, Timing delay, Timing duration);
}
