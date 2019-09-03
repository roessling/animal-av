package animal.misc;

import java.util.Properties;

/**
 * Perform property lookup in shared properties
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.0 2001-11-30
 */
public class PropertyNameMapper {
	/**
	 * "forward" mapping of key to value
	 */
	private Properties mapping = new Properties();

	/**
	 * "reverse" mapping of value to key
	 */
	private Properties reverseMapping = new Properties();

	/**
	 * Insert a new mapping of key to mappedKey
	 * 
	 * @param key
	 *          the original key
	 * @param mappedKey
	 *          the actual key used for lookup
	 */
	public void insertMapping(String key, String mappedKey) {
		mapping.put(key, mappedKey);
		reverseMapping.put(mappedKey, key);
	}

	/**
	 * Retrieve an element at the key passed in "forward" lookup
	 * 
	 * @param key
	 *          the access key
	 * @return the property name at the key, else the key itself
	 */
	public String lookupMapping(String key) {
		if (mapping.containsKey(key))
			return mapping.getProperty(key);

		return key;
	}

	/**
	 * Retrieve an element at the key passed in "reverse" lookup
	 * 
	 * @param key
	 *          the access key
	 * @return the property name at the key, else the key itself
	 */
	public String lookupReverseMapping(String key) {
		if (reverseMapping.containsKey(key))
			return reverseMapping.getProperty(key);

		return key;
	}

	/**
	 * Remove the selected entry from both mappings
	 * 
	 * @param key
	 *          the key to remove
	 */
	public void removeMapping(String key) {
		if (mapping.containsKey(key)) {
			String value = lookupMapping(key);
			reverseMapping.remove(value);
			mapping.remove(key);
		}
	}
}
