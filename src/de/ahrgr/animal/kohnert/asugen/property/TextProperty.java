/*
 * Created on 28.01.2005
 */
package de.ahrgr.animal.kohnert.asugen.property;

/**
 * @author ek
 */
public class TextProperty extends Property {

	protected String value;

	protected String defaultValue;

	public TextProperty(String theKey, String theDescription,
			String theDefaultValue) {
		super(theKey, theDescription);
		this.defaultValue = theDefaultValue;
		this.value = theDefaultValue;
	}

	public String getValue() {
		return value;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setValue(String theValue) {
		value = theValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see generatorgui.Property#setToDefaultValue()
	 */
	public void setToDefaultValue() {
		value = defaultValue;
	}

	public String toString() {
		return "\"" + getKey() + "\" = Text(\"" + getValue() + "\")";
	}
}
