package animal.main;

import java.util.Enumeration;

import animal.misc.PropertyNameMapper;
import animal.misc.XProperties;

public class PropertiedObject {
	private XProperties properties;

	private PropertyNameMapper mapper = new PropertyNameMapper();

	public XProperties getProperties() {
		if (properties == null)
			properties = new XProperties();
		return properties;
	}

	public String mapKey(String key) {
		if (mapper != null)
			return mapper.lookupMapping(key);

		return key;
	}

	public String reverseMapKey(String key) {
		if (mapper != null)
			return mapper.lookupReverseMapping(key);

		return key;
	}

	public void setProperties(XProperties props) {
		if (props == null)
			properties = new XProperties();
		else
			properties = props;
	}

	public void setPropertyNameMapper(PropertyNameMapper aMapper) {
		// getProperties().mapper = aMapper;
		mapper = aMapper;
	}

	public void clonePropertiesFrom(XProperties props, boolean installNew) {
		if (props == null)
			return;
		if (installNew || properties == null)
			properties = new XProperties();
		Enumeration<?> propertyKeys = props.propertyNames();
		Object currentKey = null;
		while (propertyKeys.hasMoreElements()) {
			currentKey = propertyKeys.nextElement();
			Object element = props.get(currentKey);
			if (element instanceof String)
				properties.put(currentKey, new String((String) props.get(currentKey)));
		}
	}
}
