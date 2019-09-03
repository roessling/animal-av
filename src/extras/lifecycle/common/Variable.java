/**
 * 
 */
package extras.lifecycle.common;

import java.io.Serializable;

/**
 * Defines a variable. It is used by default by all implementation of the <code>Box</code> interface.
 * 
 * @author Mihail Mihaylov
 *
 */
public class Variable implements Serializable {
	
	/**
	 * a simple serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Name of the variable.
	 */
	private String name;
	
	/**
	 * Value of the variable.
	 */
	private Object value;	
	
	/**
	 * Default empty constructor.
	 */
	public Variable() {
		super();
	}

	/**
	 * Creates a variable by a given name. Its value is null.
	 * 
	 * @param name of the variable
	 */
	public Variable(String name) {
		this(name, null);
	}

	/**
	 * Constructs a variable with a name and value.
	 * @param name of the variable
	 * @param value of the variable
	 */
	public Variable(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + name + "=" + value + ")";
	}

}
