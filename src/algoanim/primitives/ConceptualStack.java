package algoanim.primitives;

import java.util.EmptyStackException;
import java.util.List;

import algoanim.primitives.generators.ConceptualStackGenerator;
import algoanim.properties.StackProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * Represents a stack which has an usual LIFO-functionality and will be
 * visualized as a conceptual stack.<br>
 * The stored objects are of the generic data type T, so it is generally
 * possible to use <code>ConceptualStack</code> with any objects.
 * 
 * @author Dima Vronskyi, Dominik Fischer
 */
public class ConceptualStack<T> extends VisualStack<T> {

	/**
	 * The related <code>ConceptualStackGenerator</code>, which is responsible
	 * for generating the appropriate scriptcode for operations performed on
	 * this object.
	 */
	private final ConceptualStackGenerator<T> generator;

	/**
	 * Instantiates the <code>ConceptualStack</code> and calls the create()
	 * method of the associated <code>ConceptualStackGenerator</code>.
	 * 
	 * @param csg
	 *            the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *            the upper left corner of this <code>ConceptualStack</code>.
	 * @param content
	 *            the initial content of the <code>ConceptualStack</code>,
	 *            consisting of the elements of the generic type T.
	 * @param name
	 *            the name of this <code>ConceptualStack</code>.
	 * @param display
	 *            [optional] the <code>DisplayOptions</code> of this
	 *            <code>ConceptualStack</code>.
	 * @param sp
	 *            [optional] the properties of this <code>ConceptualStack</code>
	 *            .
	 */
	public ConceptualStack(ConceptualStackGenerator<T> csg,
			Node upperLeftCorner, List<T> content, String name,
			DisplayOptions display, StackProperties sp) {
		super(csg, upperLeftCorner, content, name, display, sp);
		generator = csg;
		generator.create(this);
	}

  @Override
	public void push(T elem, Timing delay, Timing duration) {
		generator.push(this, elem, delay, duration);
    super.push(elem, delay, duration);
	}

  @Override
	public T pop(Timing delay, Timing duration) {
		if (isEmpty(delay, duration))
			throw new EmptyStackException();
		generator.pop(this, delay, duration);
		return super.pop(delay, duration);
	}

  @Override
	public T top(Timing delay, Timing duration) {
		if (isEmpty(delay, duration))
			throw new EmptyStackException();
		generator.top(this, delay, duration);
		return super.top(delay, duration);
	}

	@Override
	public boolean isEmpty(Timing delay, Timing duration) {
		generator.isEmpty(this, delay, duration);
		return super.isEmpty(delay, duration);
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
