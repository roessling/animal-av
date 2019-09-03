package extras.lifecycle.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;

import extras.lifecycle.checkpoint.Checkpoint;
import extras.lifecycle.checkpoint.CheckpointedEvent;

/**
 * A Bean, which can contain many properties, some of them dynamically added.
 * At this time, it is exactly the same as <code>LazyDynaBean</code>
 * 
 * @author Mihail Mihaylov
 * @see LazyDynaBean
 */
public class PropertiesBean extends LazyDynaBean {
	
	final static public String NEWLINE =",";

	/* (non-Javadoc)
	 * @see org.apache.commons.beanutils.LazyDynaBean#getDynaClass()
	 */
	@Override
	public DynaClass getDynaClass() {
		// Fixes bug with DynaBeanPropertyPointer.getPropertyNames()
		// To omit the bug, a property named "class" should exist.
		set("class", this.dynaClass.getDynaProperty("class"));
		return super.getDynaClass();
	}

	/**
	 * Default serial number.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append('[');
	
		DynaProperty[] dynaProperties = this.getDynaClass().getDynaProperties();
	
		boolean comma = false;
		for (int i = 0, n = dynaProperties.length; i < n; i++) {
			String fName = dynaProperties[i].getName();
			if ("class".equals(fName))
				continue;
			Object fValue = get(fName);
	
			// We does not display checkpoints because it calls recursive
			// toString()
			if (fValue instanceof Checkpoint)
				fValue = "Checkpoint";
			else if (fValue instanceof CheckpointedEvent)
				fValue = "Event";
	
			if (comma)
				sb.append(NEWLINE);
	
			sb.append(fName + " = " + fValue);
	
			comma = true;
		}
		sb.append(']');
		return sb.toString();
	}
	
	public List<DynaProperty> getDynaProperties() {
		DynaProperty[] dynaProperties = this.getDynaClass().getDynaProperties();
		List<DynaProperty> propertiesList = new ArrayList<DynaProperty>(dynaProperties.length);
		Collections.addAll(propertiesList, dynaProperties);
		return propertiesList;
	}

}
