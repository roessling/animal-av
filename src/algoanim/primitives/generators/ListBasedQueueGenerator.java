package algoanim.primitives.generators;

import algoanim.primitives.ListBasedQueue;
import algoanim.util.Timing;

/**
 * <code>ListBasedQueueGenerator</code> offers methods to request the included 
 * Language object to append list-based queue related script code lines to the output.
 * It is designed to be included by a <code>ListBasedQueue</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>ListBasedQueue</code> primitive has to 
 * implement its own <code>ListBasedQueueGenerator</code>, which is then responsible
 * to create proper script code.  
 * 
 * @author Dima Vronskyi
 */
public interface ListBasedQueueGenerator<T> extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>ListBasedQueue</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> for which the initiate script code 
	 * shall be created.
	 */
	public void create(ListBasedQueue<T> lbq);
	
	/**
	 * Adds the element <code>elem</code> as the last element to the end of the given
	 * <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> to the end of which to add the element.
	 * @param elem the element to be added to the end of the queue.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void enqueue(ListBasedQueue<T> lbq, T elem, Timing delay, Timing duration);
	
	/**
	 * Removes the first element of the given <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> from which to remove the first element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void dequeue(ListBasedQueue<T> lbq, Timing delay, Timing duration);
	
	/**
	 * Retrieves (without removing) the first element of the given <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> from which to retrieve the first element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void front(ListBasedQueue<T> lbq, Timing delay, Timing duration);

  public void hide();
  
  public void show();
  
  public void setAutoSteps(boolean autoSteps);
	
	/**
	 * Retrieves (without removing) the last element of the given <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> from which to retrieve the last element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void tail(ListBasedQueue<T> lbq, Timing delay, Timing duration);
	
	/**
	 * Tests if the given <code>ListBasedQueue</code> is empty.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> which is tested.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void isEmpty(ListBasedQueue<T> lbq, Timing delay, Timing duration);
	
	/**
	 * Highlights the first element of the given <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightFrontElem(ListBasedQueue<T> lbq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the first element of the given <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightFrontElem(ListBasedQueue<T> lbq, Timing delay, Timing duration);
	
	/**
	 * Highlights the cell which contains the first element of the given 
	 * <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightFrontCell(ListBasedQueue<T> lbq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the cell which contains the first element of the given 
	 * <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightFrontCell(ListBasedQueue<T> lbq, Timing delay, Timing duration);
	
	/**
	 * Highlights the last element of the given <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTailElem(ListBasedQueue<T> lbq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the last element of the given <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTailElem(ListBasedQueue<T> lbq, Timing delay, Timing duration);
	
	/**
	 * Highlights the cell which contains the last element of the given 
	 * <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTailCell(ListBasedQueue<T> lbq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the cell which contains the last element of the given 
	 * <code>ListBasedQueue</code>.
	 * 
	 * @param lbq the <code>ListBasedQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTailCell(ListBasedQueue<T> lbq, Timing delay, Timing duration);
}
