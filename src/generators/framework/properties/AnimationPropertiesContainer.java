/*
 * Created on 08.05.2005 by T. Ackermann
 */
package generators.framework.properties;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import algoanim.properties.AnimationProperties;

/**
 * This container stores AnimationProperties objects, and provides methods for
 * reading and writing the contents using an XML String.
 * 
 * @author T. Ackermann
 */
public class AnimationPropertiesContainer extends Vector<AnimationProperties> {

	/**
	 * a generated serial Version UID because AnimationPropertiesContainer is
	 * serializable.
	 */
	private static final long serialVersionUID = 3257004358599326006L;

	/** stores the Hashtable for the index that maps names to the Elements */
	private Hashtable<String, AnimationProperties> index;

	/**
	 * Constructor creates a new AnimationPropertiesContainer-Object.
	 */
	public AnimationPropertiesContainer() {
		super(23);
	}

	/**
	 * Constructor creates a new AnimationPropertiesContainer-Object.
	 * 
	 * @param initialCapacity
	 *          The initial size for the Container.
	 */
	public AnimationPropertiesContainer(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * get returns the Item that is contained in the AnimationProperties.
	 * 
	 * @param prop
	 *          The name of the AnimationProperties.
	 * @param item
	 *          The name of the Item.
	 * @return The value for the item.
	 * @throws IllegalArgumentException
	 *           if the the AnimationProperties don't exist, or the Item doesn't
	 *           exist.
	 */
	public Object get(String prop, String item) throws IllegalArgumentException {
		AnimationProperties props = getPropertiesByName(prop);
		return props.get(item);
	}

	/**
	 * getPropertiesByName returns the AnimationProperties with the given Name.
	 * 
	 * @param prop
	 *          The name of the AnimationProperties.
	 * @return The AnimationProperties with the given Name.
	 * @throws IllegalArgumentException
	 *           if the the AnimationProperties don't exist
	 */
	public AnimationProperties getPropertiesByName(String prop)
			throws IllegalArgumentException {
		if (index == null)
			updateIndex();
		AnimationProperties retVal = index.get(prop);
		if (retVal == null)
			updateIndex();
		retVal = index.get(prop);
		if (retVal == null)
			throw new IllegalArgumentException("The Properties '" + prop
					+ "' don't exist!");
		return retVal;
	}

	/**
	 * updateIndex updates the search index, so that accessing the Elements by the
	 * name is much faster. Therefore the HashTable that maps the names to the
	 * Elements is rebuild.
	 */
	public void updateIndex() {
		if (index == null)
			index = new Hashtable<String, AnimationProperties>(size() + 2);
		index.clear();

		AnimationProperties prop;
		Iterator<AnimationProperties> it = iterator();
		while (it.hasNext()) {
			prop = it.next();
			index.put((String) prop.get("name"), prop);
		}
	}
}
