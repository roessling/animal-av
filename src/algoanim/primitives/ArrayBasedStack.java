package algoanim.primitives;

import java.util.EmptyStackException;
import java.util.List;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.generators.ArrayBasedStackGenerator;
import algoanim.properties.StackProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * Represents a stack which has an usual LIFO-functionality and will be
 * visualized using an array.<br>
 * The stored objects are of the generic data type T, so it is generally
 * possible to use <code>ArrayBasedStack</code> with any objects.
 * 
 * @author Dima Vronskyi, Dominik Fischer
 */
public class ArrayBasedStack<T> extends VisualStack<T> {

	// the capacity limit of the stack
	private final int capacity;

	// the amount of elements stored in the stack
	private int elemCount;

	/**
	 * The related <code>ArrayBasedStackGenerator</code>, which is responsible
	 * for generating the appropriate scriptcode for operations performed on
	 * this object.
	 */
	private final ArrayBasedStackGenerator<T> generator;

	/**
	 * Instantiates the <code>ArrayBasedStack</code> and calls the create()
	 * method of the associated <code>ArrayBasedStackGenerator</code>.
	 * 
	 * @param absg
	 *            the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *            the upper left corner of this <code>ArrayBasedStack</code>.
	 * @param content
	 *            the initial content of the <code>ArrayBasedStack</code>,
	 *            consisting of the elements of the generic type T.
	 * @param name
	 *            the name of this <code>ArrayBasedStack</code>.
	 * @param display
	 *            [optional] the <code>DisplayOptions</code> of this
	 *            <code>ArrayBasedStack</code>.
	 * @param sp
	 *            [optional] the properties of this <code>ArrayBasedStack</code>
	 *            .
	 * @param capacity
	 *            the capacity limit of this <code>ArrayBasedStack</code>; must
	 *            be nonnegative.
	 * @throws IllegalArgumentException
	 *             - if the given capacity is negative.
	 */
	public ArrayBasedStack(ArrayBasedStackGenerator<T> absg,
			Node upperLeftCorner, List<T> content, String name,
			DisplayOptions display, StackProperties sp, int capacity) {
		super(absg, upperLeftCorner, content, name, display, sp);
		generator = absg;
		if (capacity < 0)
			throw new IllegalArgumentException(
					"The capacity of the ArrayBasedStack"
							+ " must be nonnegative!");
		this.capacity = capacity;
		elemCount = content == null ? 0 : content.size();
		generator.create(this);
	}

  @Override
	public void push(T elem, Timing delay, Timing duration) {
		if (isFull(delay, duration))
			throw new IndexOutOfBoundsException(
					"The capacity limit of the array used"
							+ " by this stack has been exceeded!");
		generator.push(this, elem, delay, duration);
    super.push(elem, delay, duration);
    elemCount++;
	}

	@Override
  public T pop(Timing delay, Timing duration) {
		if (isEmpty(delay, duration))
			throw new EmptyStackException();
		generator.pop(this, delay, duration);
		notifyObservers(PrimitiveEnum.pop);
		elemCount--;
		return super.pop(delay, duration);
	}

  @Override
	public T top(Timing delay, Timing duration) {
		if (isEmpty(delay, duration))
			throw new EmptyStackException();
		generator.top(this, delay, duration);
		notifyObservers(PrimitiveEnum.top);
		return super.top(delay, duration);
	}

  @Override
	public boolean isEmpty(Timing delay, Timing duration) {
		generator.isEmpty(this, delay, duration);
		return super.isEmpty(delay, duration);
	}

	/**
	 * Tests if the stack is full.<br>
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 * @return <code>true</code> if and only if the stack is full;
	 *         <code>false</code> otherwise.
	 */
	public boolean isFull(Timing delay, Timing duration) {
		generator.isFull(this, delay, duration);
		return elemCount == capacity;
	}

	/**
	 * Returns the capacity limit of the stack.
	 * 
	 * @return The capacity limit of the stack.
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Highlights the top element of the stack.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void highlightTopElem(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.highlightTopElem(this, delay, duration);
	}

	/**
	 * Unhighlights the top element of the stack.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void unhighlightTopElem(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.unhighlightTopElem(this, delay, duration);
	}

	/**
	 * Highlights the cell which contains the top element of the stack.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void highlightTopCell(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.highlightTopCell(this, delay, duration);
	}

	/**
	 * Unhighlights the cell which contains the top element of the stack.
	 * 
	 * This is the delayed version as specified by <code>delay</code>. The
	 * <code>duration</code> of this operation may also be specified.
	 * 
	 * @param delay
	 *            [optional] the delay which shall be applied to the operation.
	 * @param duration
	 *            [optional] the duration this action needs.
	 */
	public void unhighlightTopCell(Timing delay, Timing duration) {
		if (!isEmpty())
			this.generator.unhighlightTopCell(this, delay, duration);
	}
}
