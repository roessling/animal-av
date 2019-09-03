package algoanim.primitives;

import java.util.EmptyStackException;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.generators.GeneratorInterface;
import algoanim.properties.StackProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * Base abstract class for all the (LIFO-)stacks in
 * <code>animalscriptapi.primitives</code>.<br>
 * <code>VisualStack</code> represents the common visual features of all the
 * subclasses and manges the actual data using an internal
 * <code>java.util.Stack</code>.<br>
 * The stored objects are of the generic data type T, so it is generally
 * possible to use <code>VisualStack</code> with any objects.
 * 
 * @see algoanim.primitives.ConceptualStack
 * @see algoanim.primitives.ArrayBasedStack
 * @see algoanim.primitives.ListBasedStack
 * 
 * @author Dima Vronskyi, Dominik Fischer
 */
public abstract class VisualStack<T> extends CountablePrimitive {

	// the internal java.util.Stack to manage the actual data
	private final Stack<T> stack;

	// the initial content of the stack
	private final List<T> initContent;

	// the upper left corner of the stack primitive
	private final Node upperLeft;

	private final StackProperties properties;

	/**
	 * Constructor of the <code>VisualStack</code>.
	 * 
	 * @param g
	 *            the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *            the upper left corner of this <code>VisualStack</code>.
	 * @param content
	 *            the initial content of the <code>VisualStack</code>,
	 *            consisting of the elements of the generic type T.
	 * @param name
	 *            the name of this <code>VisualStack</code>.
	 * @param display
	 *            [optional] the <code>DisplayOptions</code> of this
	 *            <code>VisualStack</code>.
	 * @param sp
	 *            [optional] the properties of this <code>VisualStack</code>.
	 */
	public VisualStack(GeneratorInterface g, Node upperLeftCorner,
			List<T> content, String name, DisplayOptions display,
			StackProperties sp) {
		super(g, display);
		if (upperLeftCorner == null) {
			throw new IllegalArgumentException("The coordinate of the "
					+ "upper left Node shouldn't be null!");
		}
		upperLeft = upperLeftCorner;
		initContent = content;
		stack = new Stack<T>();
		if (initContent != null)
			fillStack();
		properties = sp;
		setName(name);
	}

	/**
	 * Fills the stack with the initial content
	 */
	private void fillStack() {
		ListIterator<T> li = initContent.listIterator();
		while (li.hasNext())
			stack.push(li.next());
	}

  /**
   * Pushes the element <code>elem</code> onto the top of the stack. Unlike
   * the <code>push</code>-method of <code>java.util.Stack</code> it doesn't
   * return the element to be pushed.<br>
   * This is the delayed version as specified by <code>delay</code>. The
   * <code>duration</code> of this operation may also be specified. If an
   * <code>CounterController</code> observes this <code>ListBasedStack</code>
   * it is notified.
   * 
   * @param elem
   *            the element to be pushed onto the stack.
   * @param delay
   *            [optional] the delay which shall be applied to the operation.
   * @param duration
   *            [optional] the duration this action needs.
   */
  public void push(T elem, Timing delay, Timing duration) {
    notifyObservers(PrimitiveEnum.push);
    stack.push(elem);
  }
  
	/**
	 * Pushes the element <code>elem</code> onto the top of the stack. Unlike
	 * the <code>push</code>-method of <code>java.util.Stack</code> it doesn't
	 * return the element to be pushed.
	 * 
	 * @param elem
	 *            the element to be pushed onto the stack.
	 * @see java.util.Stack#push(Object)
	 */
	public void push(T elem) {
		this.push(elem, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

  /**
   * Removes and returns the element at the top of the stack.<br>
   * This is the delayed version as specified by <code>delay</code>. The
   * <code>duration</code> of this operation may also be specified. If an
   * <code>CounterController</code> observes this <code>ListBasedStack</code>
   * it is notified.
   * 
   * @param delay
   *            [optional] the delay which shall be applied to the operation.
   * @param duration
   *            [optional] the duration this action needs.
   * @return The element at the top of the stack.
   * @throws EmptyStackException
   *             - if the stack is empty.
   * @throws IndexOutOfBoundsException
   *             - if the stack can be full and is full.
   */
  public T pop(Timing delay, Timing duration) {
    notifyObservers(PrimitiveEnum.pop);
    return stack.pop();
  }
  
  /**
	 * Removes and returns the element at the top of the stack.
	 * 
	 * @return The element at the top of the stack.
	 * @see java.util.Stack#pop()
   * @throws EmptyStackException
   *             - if the stack is empty.
	 */
	public T pop() {
		return this.pop(Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

  /**
   * Retrieves (without removing) the element at the top of the stack.<br>
   * This is the delayed version as specified by <code>delay</code>. The
   * <code>duration</code> of this operation may also be specified. If an
   * <code>CounterController</code> observes this <code>ListBasedStack</code>
   * it is notified.
   * 
   * @param delay
   *            [optional] the delay which shall be applied to the operation.
   * @param duration
   *            [optional] the duration this action needs.
   * @return The element at the top of the stack.
   * @throws EmptyStackException
   *             - if the stack is empty.
   */
  public T top(Timing delay, Timing duration) {
    notifyObservers(PrimitiveEnum.top);
    return stack.peek();
  }

  /**
	 * Retrieves (without removing) the element at the top of the stack.
	 * 
	 * @return The element at the top of the stack.
	 * @see java.util.Stack#peek()
   * @throws EmptyStackException
   *             - if the stack is empty.
	 */
	public T top() {
		return this.top(Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

  /**
   * Tests if the stack is empty.<br>
   * This is the delayed version as specified by <code>delay</code>. The
   * <code>duration</code> of this operation may also be specified.
   * 
   * @param delay
   *            [optional] the delay which shall be applied to the operation.
   * @param duration
   *            [optional] the duration this action needs.
   * @return <code>true</code> if and only if the stack contains no elements;
   *         <code>false</code> otherwise.
   */
  public boolean isEmpty(Timing delay, Timing duration) {
    return stack.empty();
  }

  /**
	 * Tests if the stack is empty.
	 * 
	 * @return <code>true</code> if and only if the stack contains no elements;
	 *         <code>false</code> otherwise.
	 * @see java.util.Stack#empty()
	 */
	public boolean isEmpty() {
		return this.isEmpty(Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

	/**
	 * Returns the upper left corner of the stack.
	 * 
	 * @return The upper left corner of the stack.
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
	 * Returns the properties of the stack.
	 * 
	 * @return The properties of the stack.
	 */
	public StackProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the internal <code>java.util.Stack</code>.
	 * 
	 * @return The internal <code>java.util.Stack</code>.
	 */
	public Stack<T> getStack() {
		return stack;
	}

	/**
	 * Returns the initial content of the stack.
	 * 
	 * @return The initial content of the stack.
	 */
	public List<T> getInitContent() {
		return initContent;
	}

}
