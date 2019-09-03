/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.ahrgr.animal.kohnert.asugen.property;



/**
 * @author ek
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColorProperty extends Property {

	protected String value;
	protected String defaultValue;
	
	public ColorProperty(String aKey, String aDescription, String defaultColor) {
		 super(aKey, aDescription);
		 value = defaultColor;
		 defaultValue = defaultColor;
	}
	
	/* (non-Javadoc)
	 * @see generatorgui.Property#setToDefaultValue()
	 */
	public void setToDefaultValue() {
		value = defaultValue;
	}
	
	public void setValue(String aValue) {
		value = aValue;
	}
	
	public String getValue() {
		return value;
	}
	
	public String toString() {
		return "\"" + getKey() + "\" = Color(" + getValue() + ")";
	}

}
