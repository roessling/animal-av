package algoanim.primitives;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.generators.GeneratorInterface;
import algoanim.properties.QueueProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * Base abstract class for all the (FIFO-)queues in
 * <code>animalscriptapi.primitives</code>.<br>
 * <code>VisualQueue</code> represents the common visual features of all the
 * subclasses and manges the actual data using an internal
 * <code>java.util.LinkedList</code>.<br>
 * The stored objects are of the generic data type T, so it is generally
 * possible to use <code>VisualQueue</code> with any objects.
 * 
 * @see algoanim.primitives.ConceptualQueue
 * @see algoanim.primitives.ArrayBasedQueue
 * @see algoanim.primitives.ListBasedQueue
 * @author Dima Vronskyi, Dominik Fischer
 */
public abstract class VisualQueue<T> extends CountablePrimitive {

	// the internal java.util.LinkedList to manage the actual data
	private final LinkedList<T> queue;

	// the initial content of the queue
	private final List<T> initContent;

	// the upper left corner of the queue primitive
	private final Node upperLeft;

	private final QueueProperties properties;

	
	public VisualQueue(GeneratorInterface g, Node upperLeftCorner,
			List<T> content, String name, DisplayOptions display,
			QueueProperties qp) {
		super(g, display);
		if (upperLeftCorner == null) {
			throw new IllegalArgumentException("The coordinate of the "
					+ "upper left Node shouldn't be null!");
		}
		upperLeft = upperLeftCorner;
		initContent = content;
		queue = new LinkedList<T>();
		if (initContent != null)
			queue.addAll(initContent);
		properties = qp;
		setName(name);
	}

  /**
   * Adds the element <code>elem</code> as the last element to the end of the
   * queue.<br>
   * This is the delayed version as specified by <code>delay</code>. The
   * <code>duration</code> of this operation may also be specified. If an
   * <code>CounterController</code> observes this <code>ArrayBasedQueue</code>
   * it is notified.
   * 
   * @param elem
   *            the element to be added to the end of the queue.
   * @param delay
   *            [optional] the delay which shall be applied to the operation.
   * @param duration
   *            [optional] the duration this action needs.
   * @throws IndexOutOfBoundsException
   *             - if the queue can be full and is full.
   */
  public void enqueue(T elem, Timing delay, Timing duration) {
    notifyObservers(PrimitiveEnum.enqueue);
    queue.offer(elem);
  }

  /**
	 * Adds the element <code>elem</code> as the last element to the end of the
	 * queue.
	 * 
	 * @param elem
	 *            the element to be added to the end of the queue.
	 * @see java.util.LinkedList#offer(Object)
   * @throws IndexOutOfBoundsException
   *             - if the queue can be full and is full.
	 */
	public void enqueue(T elem) {
		this.enqueue(elem, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

  /**
   * Removes and returns the first element of the queue.<br>
   * This is the delayed version as specified by <code>delay</code>. The
   * <code>duration</code> of this operation may also be specified. If an
   * <code>CounterController</code> observes this <code>ArrayBasedQueue</code>
   * it is notified.
   * 
   * @param delay
   *            [optional] the delay which shall be applied to the operation.
   * @param duration
   *            [optional] the duration this action needs.
   * @return The first element of the queue.
   * @throws NoSuchElementException
   *             - if the queue is empty.
   */
  public T dequeue(Timing delay, Timing duration) {
    notifyObservers(PrimitiveEnum.dequeue);
    return queue.poll();
  }

	/**
	 * Removes and returns the first element of the queue.
	 * 
	 * @return The first element of the queue.
	 * @see java.util.LinkedList#poll()
   * @throws NoSuchElementException
   *             - if the queue is empty.
	 */
	public T dequeue() {
		return this.dequeue(Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

  /**
   * Retrieves (without removing) the first element of the queue.<br>
   * This is the delayed version as specified by <code>delay</code>. The
   * <code>duration</code> of this operation may also be specified. If an
   * <code>CounterController</code> observes this <code>ArrayBasedQueue</code>
   * it is notified.
   * 
   * @param delay
   *            [optional] the delay which shall be applied to the operation.
   * @param duration
   *            [optional] the duration this action needs.
   * @return The first element of the queue.
   * @throws NoSuchElementException
   *             - if the queue is empty.
   */
  public T front(Timing delay, Timing duration) {
    notifyObservers(PrimitiveEnum.front);
    return queue.peek();
  }

	/**
	 * Retrieves (without removing) the first element of the queue.
	 * 
	 * @return The first element of the queue.
	 * @see java.util.LinkedList#peek()
   * @throws NoSuchElementException
   *             - if the queue is empty.
	 */
	public T front() {
		return this.front(Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

  /**
   * Retrieves (without removing) the last element of the queue.<br>
   * This is the delayed version as specified by <code>delay</code>. The
   * <code>duration</code> of this operation may also be specified.
   * 
   * @param delay
   *            [optional] the delay which shall be applied to the operation.
   * @param duration
   *            [optional] the duration this action needs.
   * @return The last element of the queue.
   * @throws NoSuchElementException
   *             - if the queue is empty.
   */
  public T tail(Timing delay, Timing duration) {
    notifyObservers(PrimitiveEnum.tail);
    return queue.getLast();
  }

	/**
	 * Retrieves (without removing) the last element of the queue.
	 * 
	 * @return The last element of the queue.
	 * @see java.util.LinkedList#getLast()
   * @throws NoSuchElementException
   *             - if the queue is empty.
	 */
	public T tail() {
		return this.tail(Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

  /**
   * Tests if the queue is empty.<br>
   * This is the delayed version as specified by <code>delay</code>. The
   * <code>duration</code> of this operation may also be specified.
   * 
   * @param delay
   *            [optional] the delay which shall be applied to the operation.
   * @param duration
   *            [optional] the duration this action needs.
   * @return <code>true</code> if and only if the queue contains no elements;
   *         <code>false</code> otherwise.
   */
  public boolean isEmpty(Timing delay, Timing duration) {
    return queue.isEmpty();
  }

  /**
	 * Tests if the queue is empty.
	 * 
	 * @return <code>true</code> if and only if the queue contains no elements;
	 *         <code>false</code> otherwise.
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	public boolean isEmpty() {
		return this.isEmpty(Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

	/**
	 * Returns the upper left corner of the queue.
	 * 
	 * @return the upper left corner of the queue.
	 */
	public Node getUpperLeft() {
		return upperLeft;
	}

	/**
	 * @see algoanim.primitives.Primitive#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		properties.setName(newName);
		super.setName(newName);
	}

	/**
	 * Returns the initial content of the queue.
	 * 
	 * @return The initial content of the queue.
	 */
	public List<T> getInitContent() {
		return initContent;
	}

	/**
	 * Returns the properties of the queue.
	 * 
	 * @return The properties of the queue.
	 */
	public QueueProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the internal queue as <code>java.util.LinkedList</code>.
	 * 
	 * @return The internal queue.
	 */
	public LinkedList<T> getQueue() {
		return queue;
	}
}
