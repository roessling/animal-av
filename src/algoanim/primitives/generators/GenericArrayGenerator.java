package algoanim.primitives.generators;

import algoanim.primitives.ArrayPrimitive;
import algoanim.util.Timing;

public interface GenericArrayGenerator extends GeneratorInterface {
  /**
   * Swaps to values in a given <code>ArrayPrimitive</code>.
   * 
   * @param iap
   *          the <code>ArrayPrimitive</code> in which to swap the two indizes.
   * @param what
   *          the first array element.
   * @param with
   *          the second array element.
   * @param delay
   *          the time to wait until the operation shall be performed.
   * @param duration
   *          the duration of the operation.
   */
  public void swap(ArrayPrimitive iap, int what, int with, Timing delay,
      Timing duration);

  /**
   * Highlights the array cell at a given position after a distinct offset of an
   * <code>ArrayPrimitive</code>.
   * 
   * @param position
   *          the position of the cell to highlight.
   * @param offset
   *          [optional] the offset after which the operation shall be started.
   * @param duration
   *          [optional] the duration this operation lasts.
   */
  public void highlightCell(ArrayPrimitive ia, int position, Timing offset,
      Timing duration);

  /**
   * Highlights a range of array cells of an <code>ArrayPrimitive</code>.
   * 
   * @param from
   *          the start of the interval to highlight.
   * @param to
   *          the end of the interval to highlight.
   * @param offset
   *          [optional] the offset after which the operation shall be started.
   * @param duration
   *          [optional] the duration this operation lasts.
   */
  public void highlightCell(ArrayPrimitive ia, int from, int to, Timing offset,
      Timing duration);

  /**
   * Unhighlights the array cell of an <code>ArrayPrimitive</code> at a given position
   * after a distinct offset.
   * 
   * @param ia
   *          the <code>ArrayPrimitive</code> to work on.
   * @param position
   *          the position of the cell to unhighlight.
   * @param offset
   *          [optional] the offset after which the operation shall be started.
   * @param duration
   *          [optional] the duration this operation lasts.
   */
  public void unhighlightCell(ArrayPrimitive ia, int position, Timing offset,
      Timing duration);

  /**
   * Unhighlights a range of array cells of an <code>ArrayPrimitive</code>.
   * 
   * @param ia
   *          the <code>ArrayPrimitive</code> to work on.
   * @param from
   *          the start of the interval to unhighlight.
   * @param to
   *          the end of the interval to unhighlight.
   * @param offset
   *          [optional] the offset after which the operation shall be started.
   * @param duration
   *          [optional] the duration this operation lasts.
   */
  public void unhighlightCell(ArrayPrimitive ia, int from, int to, Timing offset,
      Timing duration);

  /**
   * Highlights the array element of an <code>ArrayPrimitive</code> at a given
   * position after a distinct offset.
   * 
   * @param ia
   *          the <code>ArrayPrimitive</code> to work on.
   * @param position
   *          the position of the element to highlight.
   * @param offset
   *          [optional] the offset after which the operation shall be started.
   * @param duration
   *          [optional] the duration this operation lasts.
   */
  public void highlightElem(ArrayPrimitive ia, int position, Timing offset,
      Timing duration);

  /**
   * Highlights a range of array elements of an <code>ArrayPrimitive</code>.
   * 
   * @param ia
   *          the <code>ArrayPrimitive</code> to work on.
   * @param from
   *          the start of the interval to highlight.
   * @param to
   *          the end of the interval to highlight.
   * @param offset
   *          [optional] the offset after which the operation shall be started.
   * @param duration
   *          [optional] the duration this operation lasts.
   */
  public void highlightElem(ArrayPrimitive ia, int from, int to, Timing offset,
      Timing duration);

  /**
   * Unhighlights the array element of an <code>ArrayPrimitive</code> at a given
   * position after a distinct offset.
   * 
   * @param ia
   *          the <code>ArrayPrimitive</code> to work on.
   * @param position
   *          the position of the element to unhighlight.
   * @param offset
   *          [optional] the offset after which the operation shall be started.
   * @param duration
   *          [optional] the duration this operation lasts.
   */
  public void unhighlightElem(ArrayPrimitive ia, int position, Timing offset,
      Timing duration);

  /**
   * Unhighlights a range of array elements of an <code>ArrayPrimitive</code>.
   * 
   * @param ia
   *          the <code>ArrayPrimitive</code> to work on.
   * @param from
   *          the start of the interval to unhighlight.
   * @param to
   *          the end of the interval to unhighlight.
   * @param offset
   *          [optional] the offset after which the operation shall be started.
   * @param duration
   *          [optional] the duration this operation lasts.
   */
  public void unhighlightElem(ArrayPrimitive ia, int from, int to, Timing offset,
      Timing duration);

}
