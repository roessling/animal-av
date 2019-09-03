package algoanim.primitives;

import java.util.List;
import java.util.NoSuchElementException;

import algoanim.primitives.generators.ListBasedQueueGenerator;
import algoanim.properties.QueueProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * Represents a queue which has an usual FIFO-functionality and will be
 * visualized using a linked list.<br>
 * The stored objects are of the generic data type T, so it is generally
 * possible to use <code>ListBasedQueue</code> with any objects.
 * 
 * @author Dima Vronskyi, Dominik Fischer
 */
public class ListBasedQueue<T> extends VisualQueue<T> {

	/**
	 * The related <code>ListBasedQueueGenerator</code>, which is responsible
	 * for generating the appropriate scriptcode for operations performed on
	 * this object.
	 */
	private final ListBasedQueueGenerator<T> generator;

	/**
	 * Instantiates the <code>ListBasedQueue</code> and calls the create()
	 * method of the associated <code>ListBasedQueueGenerator</code>.
	 * 
	 * @param lbqg
	 *            the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *            the upper left corner of this <code>ListBasedQueue</code>.
	 * @param content
	 *            the initial content of the <code>ListBasedQueue</code>,
	 *            consisting of the elements of the generic type T.
	 * @param name
	 *            the name of this <code>ListBasedQueue</code>.
	 * @param display
	 *            [optional] the <code>DisplayOptions</code> of this
	 *            <code>ListBasedQueue</code>.
	 * @param qp
	 *            [optional] the properties of this <code>ListBasedQueue</code>.
	 */
	public ListBasedQueue(ListBasedQueueGenerator<T> lbqg,
			Node upperLeftCorner, List<T> content, String name,
			DisplayOptions display, QueueProperties qp) {
		super(lbqg, upperLeftCorner, content, name, display, qp);
		generator = lbqg;
		generator.create(this);
	}

  @Override
	public void enqueue(T elem, Timing delay, Timing duration) {
		super.enqueue(elem, delay, duration);
		generator.enqueue(this, elem, delay, duration);
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
  public void show() {
    generator.show();
  }
  
  @Override
  public void show(Timing t) {
    show();
  }
  
  @Override
  public void hide() {
    hide(new TicksTiming(0));
  }
  
  @Override
  public void hide(Timing t) {
    generator.hide();
  }
  
  public void setAutoSteps(boolean autoSteps) {
    generator.setAutoSteps(autoSteps);
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
