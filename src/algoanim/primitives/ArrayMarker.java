package algoanim.primitives;

import algoanim.primitives.generators.ArrayMarkerGenerator;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Timing;

/**
 * Represents a marker which points to a certain 
 * <code>array</code> index.
 * @author Stephan Mehlhase 
 */
public class ArrayMarker extends Primitive {
	private ArrayMarkerGenerator generator = null;
	private ArrayPrimitive belongsTo = null;
	private int position = 0;
	private ArrayMarkerProperties properties = null;
	
	/**
     * Instantiates the <code>ArrayMarker</code> and calls the create() 
     * method
     * of the associated <code>ArrayMarkerGenerator</code>.	 
	 * @param amg the appropriate code <code>Generator</code>.
	 * @param prim the <code>ArrayPrimitive</code> at which this marker
	 * 	shall point.
	 * @param index the index of the given <code>ArrayPrimitive</code>
	 * 	at which this marker shall point.
	 * @param name the name of this <code>ArrayMarker</code>.
	 * @param display [optional] the <code>DisplayOptions</code> of this
	 * <code>ArrayMarker</code>.
	 * @param ap [optional] the properties of this 
	 * <code>ArrayMarker</code>.
	 */
	public ArrayMarker(ArrayMarkerGenerator amg, ArrayPrimitive prim, 
			int index, String name, DisplayOptions display, 
			ArrayMarkerProperties ap) {
		super(amg, display);
		
		generator = amg;
		belongsTo = prim;
		this.properties = ap;
		this.setName(name);
		if (index >= 0 && index < prim.getLength()) {
			this.position = index;
		}
		this.generator.create(this);
	}
		
  /**
   * Decrements the given <code>ArrayMarker</code> by one position of the 
   * associated <code>ArrayPrimitive</code>.
   * 
   * @param delay the time to wait until the operation shall be performed.
   * @param duration the duration of the operation.
   */
  public void decrement(Timing delay, Timing duration) {
  	if (position > 0)
  		generator.decrement(this, delay, duration);
  	position--;
  }

  /**
   * Increments the given <code>ArrayMarker</code> by one position of the 
   * associated <code>ArrayPrimitive</code>.
   * 
   * shall be moved.
   * @param delay the time to wait until the operation shall be performed.
   * @param duration the duration of the operation.
   */
  public void increment(Timing delay, Timing duration) {
  	if (position < getArray().getLength() - 1)
  		generator.increment(this, delay, duration);
  	position++;
  }

	
	/**
	 * Moves the <code>ArrayMarker</code> to the index specified by
	 * <code>pos</code> after the offset <code>t</code>. The operation 
	 * will last as long as specified by the duration <code>d</code>.
	 * @param pos the index where to move the 
	 * <code>ArrayMarker</code>.
	 * @param t [optional] the offset until this operation starts.
	 * @param d [optional] the duration of this operation.
	 */
	public void move(int pos, Timing t, Timing d) {
		generator.move(this, pos, t, d);
		position = pos;
	}
	
  /**
   * Moves the <code>ArrayMarker</code> out of of the referenced 
   * <code>ArrayPrimitive</code> after the offset <code>t</code>. 
   * The operation will last as long as specified by the 
   * duration <code>d</code>.
   * 
   * @param t [optional] the offset until this operation starts.
   * @param d [optional] the duration of this operation.
   */ 
  public void moveBeforeStart(Timing t, Timing d) {
    generator.moveBeforeStart(this, t, d);
    position = -1;
  }
  
	/**
	 * Moves the <code>ArrayMarker</code> to the end of the referenced 
	 * <code>ArrayPrimitive</code> after the offset <code>t</code>. 
	 * The operation will last as long as specified by the 
	 * duration <code>d</code>.
	 * 
	 * @param t [optional] the offset until this operation starts.
	 * @param d [optional] the duration of this operation.
	 */	
	public void moveToEnd(Timing t, Timing d) {
		generator.moveToEnd(this, t, d);
		position = getArray().getLength();
	}

	/**
	 * Moves the <code>ArrayMarker</code> out of of the referenced 
	 * <code>ArrayPrimitive</code> after the offset <code>t</code>. 
	 * The operation will last as long as specified by the 
	 * duration <code>d</code>.
	 * 
	 * @param t [optional] the offset until this operation starts.
	 * @param d [optional] the duration of this operation.
	 */	
	public void moveOutside(Timing t, Timing d) {
		generator.moveOutside(this, t, d);
		position = getArray().getLength() + 1;
	}

	/**
	 * Returns the associated <code>ArrayPrimitive</code> of this
	 * <code>ArrayMarker</code>.
	 * @return the associated <code>ArrayPrimitive</code> of this
	 * <code>ArrayMarker</code>.
	 */
	public ArrayPrimitive getArray() {
		return belongsTo;
	}
	
	/**
	 * Returns the current index position of this <code>ArrayMarker</code>.
	 * @return the current index position of this 
	 * <code>ArrayMarker</code>.
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Returns the properties of this <code>ArrayMarker</code>.
	 * @return		the properties of this <code>ArrayMarker</code>.
	 */
	public ArrayMarkerProperties getProperties() {
		return properties;
	}
	
	/**
	 * @see algoanim.primitives.Primitive#setName(java.lang.String)
	 */
	public void setName(String newName) {
		properties.setName(newName);
		super.setName(newName);
	}	
}
