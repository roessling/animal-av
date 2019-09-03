/*
 * Created on 28.01.2005
 */
package de.ahrgr.animal.kohnert.asugen.property;

/**
 * @author ek
 */
public abstract class Property {

	protected String key;

	protected String description;

	public Property(String theKey, String theDescription) {
		key = theKey;
		description = theDescription;
	}

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

	public abstract void setToDefaultValue();
}
