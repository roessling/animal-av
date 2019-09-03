package generators.backtracking.helpers;

import java.util.HashMap;

public class FieldMap<K, V> extends HashMap<Field, Field>{

	private static final long serialVersionUID = -2442357458721579159L;

	public Field getField(int x, int y){
		return getField(new Field(x, y));
	}
	/**
	 * Returns a Field on given Coordinates
	 * If Field doesn't exists it will be created,
	 * @param key
	 * @return the field for given Coordinates.
	 */
	public Field getField(Field key) {
		Field val = super.get(key);
		if(val != null){
			return val;
		}else{
			val = new Field(key.getX(), key.getY());
			super.put(key, val);
			return val;
		}
		
	}
}
