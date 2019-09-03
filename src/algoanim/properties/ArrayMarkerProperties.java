package algoanim.properties;

import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.items.StringPropertyItem;

/**
 * Implements the properties for an "Array Marker" shape.
 * These objects possess the standard properties (COLOR, DEPTH, HIDDEN)
 * and the following specific properties:
 * <dl>
 * <dt>LABEL</dt><dd>the label of the marker</dd>
 * <dt>LONG_MARKER</dt><dd>whether to use a "longer" pointer to avoid visual collisions</dd>
 * <dt>SHORT_MARKER</dt><dd>whether to use a "shorter" pointer to avoid visual collisions</dd>
 * </dl>
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 */
public class ArrayMarkerProperties extends AnimationProperties {

	/**
	 * Generates an unnamed <code>ArrayMarkerProperties</code> object.
	 */
	public ArrayMarkerProperties() {
		this("unnamed ArrayMarker property");
	}

	/**
	 * Generates a named <code>ArrayMarkerProperties</code> object.
	 * @param name 		the name for this <code>ArrayMarkerProperties</code>.
	 */
	public ArrayMarkerProperties(String name) {
		super(name);
		fillHashMap();
	}
		
	/**
	 * @see algoanim.properties.AnimationProperties#fillHashMap()
	 */
  @Override
  protected void addTypeSpecificValues() {
//   super.fillHashMap();

    // specific entries for this object type  
		data.put(AnimationPropertiesKeys.LABEL_PROPERTY, new StringPropertyItem());		
		data.put(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, new BooleanPropertyItem());
    data.put(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, new BooleanPropertyItem());
//		
//    // default property entries
//    data.put(AnimationPropertiesKeys.COLOR_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.DEPTH_PROPERTY, new IntegerPropertyItem());
//    data.put(AnimationPropertiesKeys.HIDDEN_PROPERTY, new BooleanPropertyItem());
//
//    // enter all additional values
//		fillAdditional();
	}	
}
