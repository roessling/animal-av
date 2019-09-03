package generators.sorting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import algoanim.primitives.Primitive;

/**
 * 
 * @author Steffen Frank Schmidt
 *
 */
public class PrimitiveOrganizer {
	private HashMap<String, Primitive> objects;
	private HashMap<String, String> mapping;

	/**
	 * Constructor
	 */
	public PrimitiveOrganizer() {
		objects = new HashMap<String, Primitive>();
		mapping = new HashMap<String, String>();
	}
	
	/**
	 * Sets object with key and id
	 * 
	 * @param object
	 * @param key
	 * @param id
	 */
	public void set(Primitive object, String key, String id) {
		this.mapping.put(key, id);
		this.objects.put(id, object);
	}
	
	/**
	 * Gets object by key
	 * 
	 * @param key
	 * @return {@link Primitive}
	 */
	public Primitive get(String key) {
		if (keyExists(key))
			return this.objects.get(this.mapping.get(key));
		else
			return null;
	}
	
	/**
	 * Returns whether key exists or not
	 * 
	 * @param key
	 * @return
	 */
	public boolean keyExists(String key) {
		return mapping.containsKey(key);
	}

	/**
	 * Tries to hide an object by key
	 * 
	 * @param key
	 */
	public void hide(String key) {
		if (keyExists(key)) {
			get(key).hide();
		}
	}
	
	/**
	 * Returns a unique ID
	 * 
	 * @return {@link String}
	 */
	public String getUUID() {
		return UUID.randomUUID().toString();
	}

	public void print() {
		Set<String> keys = mapping.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			if (string.contains("buckets_2")){
				System.out.println(string);
			}
		}
	}
		
}
