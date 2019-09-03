package algoanim.primitives;

import java.util.List;
import java.util.NoSuchElementException;

import algoanim.primitives.generators.ArrayBasedQueueGenerator;
import algoanim.properties.QueueProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * Represents a queue which has an usual FIFO-functionality and will be
 * visualized as an array.<br>
 * The stored objects are of the generic data type T, so it is generally
 * possible to use <code>ArrayBasedQueue</code> with any objects.
 * 
 * @author Dima Vronskyi, Dominik Fischer
 */
public class ArrayBasedQueue<T> extends VisualQueue<T> {

	// the capacity limit of the queue
	private final int capacity;

	// the amount of elements stored in the queue
	private int elemCount;

	/**
	 * The related <code>ArrayBasedQueueGenerator</code>, which is responsible
	 * for generating the appropriate scriptcode for operations performed on
	 * this object.
	 */
	private final ArrayBasedQueueGenerator<T> generator;

	/**
	 * Instantiates the <code>ArrayBasedQueue</code> and calls the create()
	 * method of the associated <code>ArrayBasedQueueGenerator</code>.
	 * 
	 * @param abqg
	 *            the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *            the upper left corner of this <code>ArrayBasedQueue</code>.
	 * @param content
	 *            the initial content of the <code>ArrayBasedQueue</code>,
	 *            consisting of the elements of the generic type T.
	 * @param name
	 *            the name of this <code>ArrayBasedQueue</code>.
	 * @param display
	 *            [optional] the <code>DisplayOptions</code> of this
	 *            <code>ArrayBasedQueue</code>.
	 * @param qp
	 *            [optional] the properties of this <code>ArrayBasedQueue</code>
	 *            .
	 * @param capacity
	 *            the capacity limit of this <code>ArrayBasedQueue</code>; must
	 *            be nonnegative.
	 * @throws IllegalArgumentException
	 *             - if the given capacity is negative.
	 */
	public ArrayBasedQueue(ArrayBasedQueueGenerator<T> abqg,
			Node upperLeftCorner, List<T> content, String name,
			DisplayOptions display, QueueProperties qp, int capacity) {
		super(abqg, upperLeftCorner, content, name, display, qp);
		generator = abqg;
		if (capacity < 0)
			throw new IllegalArgumentException(
					"The capacity of the ArrayBasedQueue"
							+ " must be nonnegative!");
		this.capacity = capacity;
		elemCount = content == null ? 0 : content.size();
		generator.create(this);
	}

  @Override
	public void enqueue(T elem, Timing delay, Timing duration) {
		if (isFull(delay, duration))
			throw new IndexOutOfBoundsException(
					"The capacity limit of the array used"
							+ " by this queue has been exceeded!");
		generator.enqueue(this, elem, delay, duration);
    super.enqueue(elem, delay, duration);
    elemCount++;
	}

  @Override
	public T dequeue(Timing delay, Timing duration) {
		if (isEmpty(delay, duration))
			throw new NoSuchElementException(
					"The queue is empty, so there is no element to be dequeued!");
    elemCount--;
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
	 * Tests if the queue is full.<br>
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 * @return <code>true</code> if and only if the queue is full;
	 *         <code>false</code> otherwise.
	 */
	public boolean isFull(Timing delay, Timing duration) {
		generator.isFull(this, delay, duration);
		return elemCount == capacity;
	}

	/**
	 * Returns the capacity limit of the queue.
	 * 
	 * @return The capacity limit of the queue.
	 */
	public int getCapacity() {
		return capacity;
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
