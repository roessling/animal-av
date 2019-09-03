package algoanim.properties;


/**
 * Implements the properties for a generic "point" shape.
 * These objects possess only the standard properties (COLOR, DEPTH, HIDDEN).
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 */
public class PointProperties extends AnimationProperties {

	/**
	 * Generates an unnamed <code>PointProperties</code> object.
	 */
	public PointProperties() {
		this("unnamed point property");
	}
	
	/**
	 * Generates a named <code>PointProperties</code> object.
	 * 
	 * @param name		the name of this <code>PointProperties</code>.
	 */
	public PointProperties(String name) {
		super(name);
		fillHashMap();
	}
	
//	/**
//	 * @see algoanim.properties.AnimationProperties#fillHashMap()
//	 */
//	protected void fillHashMap() {
//    // default property entries
//    data.put(AnimationPropertiesKeys.COLOR_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.DEPTH_PROPERTY, new IntegerPropertyItem());
//    data.put(AnimationPropertiesKeys.HIDDEN_PROPERTY, new BooleanPropertyItem());
//    
//    // enter all additional values
//		fillAdditional();
//	}

}
