/*
 * Erstellung: 25.11.2004
 *
 */
package algoanim.primitives;

import java.awt.Color;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.generators.StringArrayGenerator;
import algoanim.properties.ArrayProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * @author Jens Pfau, Stephan Mehlhase
 * 
 */
public class StringArray extends ArrayPrimitive {
	/**
	 * The <code>IntArray</code> is internally represented by a simple array.
	 */
	private final String[] data;

	/**
	 * The related <code>IntArrayGenerator</code>, which is responsible for
	 * generating the appropriate scriptcode for operations performed on this
	 * object.
	 */
	protected StringArrayGenerator generator;

	private ArrayProperties properties = null;

	private Node upperLeft = null;

	/**
	 * Instantiates the <code>StringArray</code> and calls the create() method
	 * of the associated <code>StringArrayGenerator</code>.
	 * 
	 * @param sag
	 *            the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *            the upper left corner of this <code>StringArray</code>.
	 * @param arrayData
	 *            the internal data of this <code>StringArray</code>.
	 * @param name
	 *            the name of this <code>StringArray</code>.
	 * @param display
	 *            [optional] the <code>DisplayOptions</code> for this
	 *            <code>StringArray</code>.
	 * @param iap
	 *            [optional] the properties of this <code>StringArray</code>.
	 */
	public StringArray(StringArrayGenerator sag, Node upperLeftCorner,
			String[] arrayData, String name, ArrayDisplayOptions display,
			ArrayProperties iap) {
		super(sag, display);

		if (upperLeftCorner == null) {
			throw new IllegalArgumentException("The coordinate of the upper "
					+ "left Node shouldn't be null!");
		}

		upperLeft = upperLeftCorner;
		length = arrayData.length;
    data = new String[length];
    System.arraycopy(arrayData, 0, data, 0, length);
    //GR Fix this modified the array passed in!
    //data = arrayData;

		properties = iap;
		setName(name);

		generator = sag;
		generator.create(this);
	}

	/**
	 * Puts the value <code>what</code> at position <code>where</code>. This is
	 * the delayed version as specified by <code>t</code>. Additionally the
	 * duration of this operation may also be specified. If an
	 * <code>CounterController</code> observes this <code>StringArray</code> it
	 * is notified.
	 * 
	 * @param where
	 *            the index of the element to write.
	 * @param what
	 *            the new value.
	 * @param t
	 *            [optional] the delay which shall be applied to the operation.
	 * @param d
	 *            [optional] the duration of this operation.
	 */
	public void put(int where, String what, Timing t, Timing d)
			throws IndexOutOfBoundsException {
		if (where < data.length && where >= 0) {
			notifyObservers(PrimitiveEnum.put);
			data[where] = what;
			generator.put(this, where, what, t, d);
		} else {
			throw new IndexOutOfBoundsException("Array has min Size 0 "
					+ "and max Size " + data.length);
		}
	}

	/**
	 * Swaps the elements at index <code>what</code> and <code>with</code>. This
	 * is the delayed version as specified by <code>t</code>. Additionally the
	 * duration of this operation may be specified. If an
	 * <code>CounterController</code> observes this <code>StringArray</code> it
	 * is notified.
	 * 
	 * @param what
	 *            first element to swap.
	 * @param with
	 *            second element to swap.
	 * @param t
	 *            [optional] the delay which shall be applied to the operation.
	 * @param d
	 *            [optional] the duration of this operation.
	 */
	@Override
	public void swap(int what, int with, Timing t, Timing d)
			throws IndexOutOfBoundsException {
		if (what < data.length && what >= 0 && with < data.length && with >= 0) {
			notifyObservers(PrimitiveEnum.swap);
			String tmp = data[what];
			data[what] = data[with];
			data[with] = tmp;
			generator.swap(this, what, with, t, d);
		} else {
			throw new IndexOutOfBoundsException("Array has min Size 0 "
					+ "and max Size " + data.length);
		}
	}

//	/**
//	 * Returns the internal data of this <code>StringArray</code>.
//	 * 
//	 * @return the internal data of this <code>StringArray</code>.
//	 */
//	// @jens Jetzt gebe ich zwar ne Kopie raus, aber ist das so nicht
//	// inperformant?
//	private String[] getData() {
//		String[] copy = new String[data.length];
//
//		for (int i = 0; i < data.length; i++) {
//			copy[i] = data[i];
//		}
//		return copy;
//	}

	/**
	 * Returns the data of the internal <code>array</code> at index
	 * <code>i</code>. If an <code>CounterController</code> observes this
	 * <code>StringArray</code> it is notified.
	 * 
	 * @param i
	 *            the index to access.
	 * @return the data at the specified index.
	 */
	public String getData(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i >= this.length) {
			throw new IndexOutOfBoundsException("Array has min Size 0 and "
					+ "max Size " + data.length);
		}
		notifyObservers(PrimitiveEnum.getData);
		return data[i];
	}

	/**
	 * Returns the upper left corner of this <code>StringArray</code>.
	 * 
	 * @return the upper left corner of this <code>StringArray</code>.
	 */
	public Node getUpperLeft() {
		return upperLeft;
	}

	/**
	 * Returns the properties of this <code>StringArray</code>.
	 * 
	 * @return the properties of this <code>StringArray</code>.
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
	   * Set the Visibility of the Indices of the <code>StringArray</code>.
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
