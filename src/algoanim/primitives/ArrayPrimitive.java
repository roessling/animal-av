package algoanim.primitives;

import algoanim.primitives.generators.GeneratorInterface;
import algoanim.util.DisplayOptions;
import algoanim.util.Timing;


/**
 * Base class for all concrete arrays.
 * @author Jens Pfau, Stephan Mehlhase
 */
public abstract class ArrayPrimitive extends CountablePrimitive {
	protected int length = 0;
	
	/**
	 * @param g the appropriate code <code>Generator</code>.
	 * @param display [optional] the <code>DisplayOptions</code> of this
	 * <code>ArrayPrimitive</code>.
	 */
	public ArrayPrimitive(GeneratorInterface g, DisplayOptions display) {
		super(g, display);
	}
	
	/**
	 * Returns the length of the internal array.
	 * @return the length of the internal array.
	 */
	public int getLength() {
		return this.length;
	}
	
  /**
   * Swaps the elements at index <code>what</code> and 
   * <code>with</code>.
   * This is the delayed version. The <code>duration</code> of this
   * operation may also be specified.
   * @param what first element to swap.
   * @param with second element to swap.
   * @param t [optional] the delay which shall be applied to the 
   *  operation.
   * @param d [optional] the duration this action needs.
   */    
  public abstract void swap(int what, int with, Timing t, Timing d)
  throws IndexOutOfBoundsException;

}
