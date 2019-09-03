package algoanim.properties;

import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.items.IntegerPropertyItem;
import algoanim.properties.items.StringPropertyItem;

/**
 * Implements the properties for a generic "list element" shape.
 * These objects possess the standard properties (COLOR, DEPTH, HIDDEN)
 * and the following specific properties:
 * <dl>
 * <dt>BOXFILLCOLOR</dt><dd>the fill color for the "value box"</dd>
 * <dt>POINTERAREACOLOR</dt><dd>the outline color for the "pointer box"</dd>
 * <dt>POINTERAREAFILLCOLOR</dt><dd>the fill color for the "pointer box"</dd>
 * <dt>POSITION</dt><dd>the position for the pointers (if any); possible value"</dd>
 * <dt>TEXT</dt><dd>the text value (deprecated)</dd>
 * <dt>TEXTCOLOR</dt><dd>the color for the text value</dd>
 * </dl>
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 */
public class ListElementProperties extends AnimationProperties {

	/**
	 * Generates an unnamed <code>ListElementProperties</code> object.
	 */
	public ListElementProperties() {
		this("unnamed listElement property");
	}

	/**
	 * Generates a named <code>ListElementProperties</code> object.
	 * @param name 		the name of this <code>ListElementProperties</code>.
	 */
	public ListElementProperties(String name) {
		super(name);
		fillHashMap();
	}
	
	
	/**
	 * @see algoanim.properties.AnimationProperties#fillHashMap()
	 */
  @Override
  protected void addTypeSpecificValues() {
//    super.fillHashMap();

    // specific entries for this object type  
    data.put(AnimationPropertiesKeys.BOXFILLCOLOR_PROPERTY, new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.POINTERAREACOLOR_PROPERTY, new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.POINTERAREAFILLCOLOR_PROPERTY, new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.POSITION_PROPERTY, new IntegerPropertyItem());
		data.put(AnimationPropertiesKeys.TEXT_PROPERTY, new StringPropertyItem());
		data.put(AnimationPropertiesKeys.TEXTCOLOR_PROPERTY, new ColorPropertyItem());		

//    // default property entries
//    data.put(AnimationPropertiesKeys.COLOR_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.DEPTH_PROPERTY, new IntegerPropertyItem());
//    data.put(AnimationPropertiesKeys.HIDDEN_PROPERTY, new BooleanPropertyItem());
//    
//    // enter all additional values
//		fillAdditional();
	}	
	
}
