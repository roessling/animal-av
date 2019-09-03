package algoanim.primitives.generators;

import algoanim.primitives.ConceptualQueue;
import algoanim.util.Timing;

/**
 * <code>ConceptualQueueGenerator</code> offers methods to request the included 
 * Language object to append conceptual queue related script code lines to the output.
 * It is designed to be included by a <code>ConceptualQueue</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>ConceptualQueue</code> primitive has to 
 * implement its own <code>ConceptualQueueGenerator</code>, which is then responsible
 * to create proper script code.  
 *
 * @author Dima Vronskyi
 */
public interface ConceptualQueueGenerator<T> extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given <code>ConceptualQueue</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param cq the <code>ConceptualQueue</code> for which the initiate script code 
	 * shall be created.
	 */
	public void create(ConceptualQueue<T> cq);
	
	/**
	 * Adds the element <code>elem</code> as the last element to the end of the given
	 * <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> to the end of which to add the element.
	 * @param elem the element to be added to the end of the queue.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void enqueue(ConceptualQueue<T> cq, T elem, Timing delay, Timing duration);
	
	/**
	 * Removes the first element of the given <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> from which to remove the first element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void dequeue(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Retrieves (without removing) the first element of the given <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> from which to retrieve the first element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void front(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Retrieves (without removing) the last element of the given <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> from which to retrieve the last element.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void tail(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Tests if the given <code>ConceptualQueue</code> is empty.
	 * 
	 * @param cq the <code>ConceptualQueue</code> which is tested.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void isEmpty(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Highlights the first element of the given <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightFrontElem(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the first element of the given <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightFrontElem(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Highlights the cell which contains the first element of the given 
	 * <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightFrontCell(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the cell which contains the first element of the given 
	 * <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightFrontCell(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Highlights the last element of the given <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTailElem(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the last element of the given <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTailElem(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Highlights the cell which contains the last element of the given 
	 * <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void highlightTailCell(ConceptualQueue<T> cq, Timing delay, Timing duration);
	
	/**
	 * Unhighlights the cell which contains the last element of the given 
	 * <code>ConceptualQueue</code>.
	 * 
	 * @param cq the <code>ConceptualQueue</code> to work on.
	 * @param delay [optional] the time to wait until the operation shall be performed.
	 * @param duration [optional] the duration of the operation.
	 */
	public void unhighlightTailCell(ConceptualQueue<T> cq, Timing delay, Timing duration);
}
