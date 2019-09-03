package algoanim.properties.items;

import java.awt.Color;
import java.awt.Font;

import algoanim.properties.Visitable;
import algoanim.properties.Visitor;

/**
 * This class defines the base for the distinct <code>PropertyItem</code>s, 
 * which are responsible to hold exactly one instance of one value.
 * The method set() provides a definition for each <code>PropertyItem</code>. 
 * The derived PropertyItem just overrides the method which belongs to its type.
 * Hence each try to set a PropertyItem to an invalid type results in
 * an IllegalArgumentException as preimplemented in this class.
 * 
 * @author Jens Pfau, Stephan Mehlhase, T. Ackermann
 */
public abstract class AnimationPropertyItem implements Cloneable, Visitable {

	/**
	 * Sets the internal value to a new one, if this object contains an 
	 * <code>int</code> value.
	 * @param value		a new <code>int</code> value.
	 * @return			whether the operation was successful.
	 * @throws IllegalArgumentException
	 */
	public boolean set(int value) throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}


	/**
	 * Sets the internal value to a new one, if this object contains a 
	 * <code>String</code> value.
	 * @param value		a new <code>String</code> value.
	 * @return			whether the operation was successful.
	 * @throws IllegalArgumentException
	 */
	public boolean set(String value) throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}


	/**
	 * Sets the internal value to a new one, if this object contains a 
	 * <code>boolean</code> value.
	 * @param value		a new <code>boolean</code> value.
	 * @return			whether the operation was successful.
	 * @throws IllegalArgumentException
	 */
	public boolean set(boolean value) throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}


	/**
	 * Sets the internal value to a new one, if this object contains a 
	 * <code>Color</code> value.
	 * @param value		a new <code>Color</code> value.
	 * @return			whether the operation was successful.
	 * @throws IllegalArgumentException
	 */
	public boolean set(Color value) throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}

	/**
	 * Sets the internal value to a new one, if this object contains a 
	 * <code>Font</code> value. 
	 * @param value		a new <code>Font</code> value.
	 * @return			whether the operation was successful.
	 * @throws IllegalArgumentException
	 */
	public boolean set(Font value) throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}
	
	/**
	 * Sets the internal value to a new one, using an Object.
	 * @param value 	the new value.
	 * @return 			true, if no errors occurred.
	 * @throws IllegalArgumentException if the item doesn't exist.
	 */
	public boolean set(Object value) throws IllegalArgumentException {
		
		if (value instanceof Integer)
			return set(((Integer)value).intValue());
		if (value instanceof String)
			return set((String)value);
		if (value instanceof Boolean)
			return set(((Boolean)value).booleanValue());
		if (value instanceof Color)
			return set((Color)value);
		if (value instanceof Font)
			return set((Font)value);
		
		throw new IllegalArgumentException(
				"No valid class found to cast value");
	}
	

	/**
	 * Returns a represantation of the internal value.
	 * @return A represantation of the internal value.
	 */
	public abstract Object get();
	
	
	/**
	 * we need this because we clone the default value from the "normal"
	 * data value.
	 * @see algoanim.properties.AnimationProperties#fillAdditional()
	 */
  public Object clone() {
    Object o = null;
    try {
      o = super.clone();
    } catch(CloneNotSupportedException cnse) {
      System.err.println("CNSE@" +getClass().getName());
    }
    return o;
  }
	/**
	 * @see algoanim.properties.Visitable#accept(algoanim.properties.Visitor)
	 */
	public abstract void accept(Visitor v);	
	
}
