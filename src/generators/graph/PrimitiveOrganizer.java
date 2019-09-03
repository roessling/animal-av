package generators.graph;

import java.util.HashMap;
import java.util.UUID;

import algoanim.primitives.Primitive;

public class PrimitiveOrganizer {
	private HashMap<String, Primitive> objects;
	private HashMap<String, String> mapping;

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
	 * @return object from hashMap
	 */
	public Primitive get(String key) {
		if (keyExists(key))
			return this.objects.get(this.mapping.get(key));
		else
			return null;
	}
	
	public boolean keyExists(String key) {
		// TODO Auto-generated method stub
		return mapping.containsKey(key);
	}

	/**
	 * Tries to hide an object by key
	 * 
	 * @param key
	 */
	public void hide(String key) {
		if (mapping.containsKey(key)) {
			Primitive object = get(key);
			object.hide();
		}
	}
	
	public String getUUID() {
		return UUID.randomUUID().toString();
	}
	

		
}
