package algoanim.primitives;

import java.util.List;
import java.util.NoSuchElementException;

import algoanim.primitives.generators.ConceptualQueueGenerator;
import algoanim.properties.QueueProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * Represents a queue which has an usual FIFO-functionality and will be
 * visualized as a conceptual queue.<br>
 * The stored objects are of the generic data type T, so it is generally
 * possible to use <code>ConceptualQueue</code> with any objects.
 * 
 * @author Dima Vronskyi, Dominik Fischer
 */
public class ConceptualQueue<T> extends VisualQueue<T> {

	/**
	 * The related <code>ConceptualQueueGenerator</code>, which is responsible
	 * for generating the appropriate scriptcode for operations performed on
	 * this object.
	 */
	private final ConceptualQueueGenerator<T> generator;

	/**
	 * Instantiates the <code>ConceptualQueue</code> and calls the create()
	 * method of the associated <code>ConceptualQueueGenerator</code>.
	 * 
	 * @param cqg
	 *            the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *            the upper left corner of this <code>ConceptualQueue</code>.
	 * @param content
	 *            the initial content of the <code>ConceptualQueue</code>,
	 *            consisting of the elements of the generic type T.
	 * @param name
	 *            the name of this <code>ConceptualQueue</code>.
	 * @param display
	 *            [optional] the <code>DisplayOptions</code> of this
	 *            <code>ConceptualQueue</code>.
	 * @param qp
	 *            [optional] the properties of this <code>ConceptualQueue</code>
	 *            .
	 */
	public ConceptualQueue(ConceptualQueueGenerator<T> cqg,
			Node upperLeftCorner, List<T> content, String name,
			DisplayOptions display, QueueProperties qp) {
		super(cqg, upperLeftCorner, content, name, display, qp);
		generator = cqg;
		generator.create(this);
	}

  @Override
	public void enqueue(T elem, Timing delay, Timing duration) {
    generator.enqueue(this, elem, delay, duration);
		super.enqueue(elem, delay, duration);
	}

  @Override
	public T dequeue(Timing delay, Timing duration) {
		if (isEmpty(delay, duration))
			throw new NoSuchElementException(
					"The queue is empty, so there is no element to be dequeued!");
		generator.dequeue(this, delay, duration);
		return super.dequeue(delay, duration);
	}

  @Override
	public T front(Timing delay, Timing duration) {
		if (isEmpty(delay, duration))
			throw new NoSuchElementException(
					"The queue is empty, so there is no front element!");
		generator.front(this, delay, duration);
		return super.front(delay, duration);
	}

  @Override
	public T tail(Timing delay, Timing duration) {
		if (isEmpty(delay, duration))
			throw new NoSuchElementException(
					"The queue is empty, so there is no tail element!");
		generator.tail(this, delay, duration);
		return super.tail(delay, duration);
	}
  
  @Override
	public boolean isEmpty(Timing delay, Timing duration) {
		generator.isEmpty(this, delay, duration);
		return super.isEmpty(delay, duration);
	}

	/**
	 * Highlights the first element of the queue.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void highlightFrontElem(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.highlightFrontElem(this, delay, duration);
	}

	/**
	 * Unhighlights the first element of the queue.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void unhighlightFrontElem(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.unhighlightFrontElem(this, delay, duration);
	}

	/**
	 * Highlights the cell which contains the first element of the queue.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void highlightFrontCell(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.highlightFrontCell(this, delay, duration);
	}

	/**
	 * Unhighlights the cell which contains the first element of the queue.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void unhighlightFrontCell(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.unhighlightFrontCell(this, delay, duration);
	}

	/**
	 * Highlights the last element of the queue.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void highlightTailElem(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.highlightTailElem(this, delay, duration);
	}

	/**
	 * Unhighlights the last element of the queue.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void unhighlightTailElem(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.unhighlightTailElem(this, delay, duration);
	}

	/**
	 * Highlights the cell which contains the last element of the queue.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void highlightTailCell(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.highlightTailCell(this, delay, duration);
	}

	/**
	 * Unhighlights the cell which contains the last element of the queue.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void unhighlightTailCell(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.unhighlightTailCell(this, delay, duration);
	}
}
