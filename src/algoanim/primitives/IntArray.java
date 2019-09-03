/*
 * Created on 24.11.2004 
 */
package algoanim.primitives;

import java.awt.Color;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.generators.IntArrayGenerator;
import algoanim.properties.ArrayProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * <code>IntArray</code> manages an internal array. Operations on objects of
 * <code>IntArray</code> are almost performed like on a simple array.
 * 
 * @author jens
 */
public class IntArray extends ArrayPrimitive {
	/**
	 * The <code>IntArray</code> is internally represented by a simple array.
	 */
	private final int[] data;

	/**
	 * The related <code>IntArrayGenerator</code>, which is responsible for
	 * generating the appropriate scriptcode for operations performed on this
	 * object.
	 */
	protected IntArrayGenerator generator;

	private ArrayProperties properties = null;
	private Node upperLeft = null;

	/**
	 * Instantiates the <code>IntArray</code> and calls the create() method of
	 * the associated <code>IntArrayGenerator</code>.
	 * 
	 * @param iag
	 *            the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *            the upper left corner of this <code>IntArray</code>.
	 * @param arrayData
	 *            the data of this <code>IntArray</code>.
	 * @param name
	 *            the name of this <code>IntArray</code>.
	 * @param display
	 *            [optional] the <code>DisplayOptions</code> of this
	 *            <code>IntArray</code>.
	 * @param iap
	 *            [optional] the properties of this <code>IntArray</code>.
	 */
	public IntArray(IntArrayGenerator iag, Node upperLeftCorner,
			int[] arrayData, String name, ArrayDisplayOptions display,
			ArrayProperties iap) {
		super(iag, display);

		if (upperLeftCorner == null) {
			throw new IllegalArgumentException("The coordinate of the "
					+ "upper left Node shouldn't be null!");
		}

		upperLeft = upperLeftCorner;
		length = arrayData.length;
		
		data = new int[length];
		System.arraycopy(arrayData, 0, data, 0, length);
		//GR Fix this modified the array passed in!
		//data = arrayData;

		properties = iap;
		setName(name);

		generator = iag;
		generator.create(this);
	}

	/**
	 * Puts the value <code>what</code> at position <code>where</code>. This is
	 * the delayed version as specified by <code>t</code>. The
	 * <code>duration</code> of this operation may also be specified. If an
	 * <code>CounterController</code> observes this <code>IntArray</code> it is
	 * notified.
	 * 
	 * @param where
	 *            the index of the element to write.
	 * @param what
	 *            the new value.
	 * @param t
	 *            [optional] the delay which shall be applied to the operation.
	 * @param d
	 *            [optional] the duration this action needs.
	 */
	public void put(int where, int what, Timing t, Timing d)
			throws IndexOutOfBoundsException {
		if (where < data.length && where >= 0) {
			notifyObservers(PrimitiveEnum.put);
			data[where] = what;
			generator.put(this, where, what, t, d);
		} else {
			throw new IndexOutOfBoundsException("Array has min Size 0 and "
					+ "max Size " + data.length);
		}
	}

	/**
	 * Swaps the elements at index <code>what</code> and <code>with</code>. This
	 * is the delayed version. The <code>duration</code> of this operation may
	 * also be specified. If an <code>CounterController</code> observes this
	 * <code>IntArray</code> it is notified.
	 * 
	 * @param what
	 *            first element to swap.
	 * @param with
	 *            second element to swap.
	 * @param t
	 *            [optional] the delay which shall be applied to the operation.
	 * @param d
	 *            [optional] the duration this action needs.
	 */
	@Override
	public void swap(int what, int with, Timing t, Timing d)
			throws IndexOutOfBoundsException {
		if (what < data.length && what >= 0 && with < data.length && with >= 0) {
			notifyObservers(PrimitiveEnum.swap);
			int tmp = data[what];
			data[what] = data[with];
			data[with] = tmp;
			generator.swap(this, what, with, t, d);
		} else {
			throw new IndexOutOfBoundsException("Array has min Size 0 "
					+ "and max Size " + data.length);
		}
	}

//	/**
//	 * Returns the internal <code>int array</code>.
//	 * 
//	 * @return the internal <code>int array</code>.
//	 */
//	private int[] getData() {
//		return data;
//	}

	/**
	 * Returns the data at the given position of the internal array. If an
	 * <code>CounterController</code> observes this <code>IntArray</code> it is
	 * notified.
	 * 
	 * @param i
	 *            the position where to look for the data.
	 * @return the data at position <code>i</code> in the internal
	 *         <code>int array</code>.
	 */
	public int getData(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i >= this.length) {
			throw new IndexOutOfBoundsException("Array has indices [0, "
					+ (data.length - 1) + "]; you tried to access #" + i);
		}
		notifyObservers(PrimitiveEnum.getData);
		return data[i];
	}

	/**
	 * Returns the upper left corner of this array.
	 * 
	 * @return the upper left corner of this array.
	 */
	public Node getUpperLeft() {
		return upperLeft;
	}

	/**
	 * Returns the properties of this array.
	 * 
	 * @return the properties of this array.
	 */
	public ArrayProperties getProperties() {
		return properties;
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
	 * Highlights the array cell at a given position after a distinct offset.
	 * 
	 * @param position
	 *            the position of the cell to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void highlightCell(int position, Timing offset, Timing duration) {
		if (position >= 0 && position < getLength()) {
			this.generator.highlightCell(this, position, offset, duration);
		}
	}

	/**
	 * Highlights a range of array cells.
	 * 
	 * @param from
	 *            the start of the interval to highlight.
	 * @param to
	 *            the end of the interval to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void highlightCell(int from, int to, Timing offset, Timing duration) {
		if (from <= to && from >= 0 && from < getLength() && to >= 0
				&& to < getLength()) {
			this.generator.highlightCell(this, from, to, offset, duration);
		}
	}

	/**
	 * Unhighlights the array cell at a given position after a distinct offset.
	 * 
	 * @param position
	 *            the position of the cell to unhighlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void unhighlightCell(int position, Timing offset, Timing duration) {
		if (position >= 0 && position < getLength()) {
			this.generator.unhighlightCell(this, position, offset, duration);
		}
	}

	/**
	 * Unhighlights a range of array cells.
	 * 
	 * @param from
	 *            the start of the interval to unhighlight.
	 * @param to
	 *            the end of the interval to unhighlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void unhighlightCell(int from, int to, Timing offset, Timing duration) {
		if (from <= to && from >= 0 && from < getLength() && to >= 0
				&& to < getLength()) {
			this.generator.unhighlightCell(this, from, to, offset, duration);
		}
	}

	/**
	 * Highlights the array element at a given position after a distinct offset.
	 * 
	 * @param position
	 *            the position of the element to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void highlightElem(int position, Timing offset, Timing duration) {
		if (position >= 0 && position < getLength()) {
			this.generator.highlightElem(this, position, offset, duration);
		}
	}

	/**
	 * Highlights a range of array elements.
	 * 
	 * @param from
	 *            the start of the interval to highlight.
	 * @param to
	 *            the end of the interval to highlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void highlightElem(int from, int to, Timing offset, Timing duration) {
		if (from <= to && from >= 0 && from < getLength() && to >= 0
				&& to < getLength()) {
			this.generator.highlightElem(this, from, to, offset, duration);
		}
	}

	/**
	 * Unhighlights the array element at a given position after a distinct
	 * offset.
	 * 
	 * @param position
	 *            the position of the element to unhighlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void unhighlightElem(int position, Timing offset, Timing duration) {
		if (position >= 0 && position < getLength()) {
			this.generator.unhighlightElem(this, position, offset, duration);
		}
	}

	/**
	 * Unhighlights a range of array elements.
	 * 
	 * @param from
	 *            the start of the interval to unhighlight.
	 * @param to
	 *            the end of the interval to unhighlight.
	 * @param offset
	 *            [optional] the offset after which the operation shall be
	 *            started.
	 * @param duration
	 *            [optional] the duration this operation lasts.
	 */
	public void unhighlightElem(int from, int to, Timing offset, Timing duration) {
		if (from <= to && from >= 0 && from < getLength() && to >= 0
				&& to < getLength()) {
			this.generator.unhighlightElem(this, from, to, offset, duration);
		}
	}

	  /**
	   * Set the Visibility of the Indices of the <code>IntArray</code>.
	   * 
	   * @param show
	   *          visibility
	   * @param delay
	   *          the time to wait until the operation shall be performed.
	   * @param duration
	   *          the duration of the operation.
	   */
	public void showIndices(boolean show, Timing delay, Timing duration) {
		this.generator.showIndices(this, show, delay, duration);
	}

	  
	  public void setBorderColor(int position, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setBorderColor", position, c, offset, duration);
	  }
	  
	  public void setBorderColor(int from, int to, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setBorderColor", from, to, c, offset, duration);
	  }
	  
	  public void setFillColor(int position, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setFillColor", position, c, offset, duration);
	  }
	  
	  public void setFillColor(int from, int to, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setFillColor", from, to, c, offset, duration);
	  }
	  
	  public void setTextColor(int position, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setTextColor", position, c, offset, duration);
	  }
	  
	  public void setTextColor(int from, int to, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setTextColor", from, to, c, offset, duration);
	  }
	  
	  public void setHighlightBorderColor(int position, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setHighlightBorderColor", position, c, offset, duration);
	  }
	  
	  public void setHighlightBorderColor(int from, int to, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setHighlightBorderColor", from, to, c, offset, duration);
	  }
	  
	  public void setHighlightFillColor(int position, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setHighlightFillColor", position, c, offset, duration);
	  }
	  
	  public void setHighlightFillColor(int from, int to, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setHighlightFillColor", from, to, c, offset, duration);
	  }
	  
	  public void setHighlightTextColor(int position, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setHighlightTextColor", position, c, offset, duration);
	  }
	  
	  public void setHighlightTextColor(int from, int to, Color c, Timing offset,
		      Timing duration){
		  this.generator.setColorTyp(this, "setHighlightTextColor", from, to, c, offset, duration);
	  }

}
