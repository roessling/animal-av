package algoanim.properties;

import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.meta.FillableAnimationProperties;
import algoanim.properties.meta.FontContainingAnimationProperties;

/**
 * Implements the properties for a generic counter.
 * These objects possess the standard properties (COLOR, DEPTH, HIDDEN) and the
 * "shared" properties of their implemented interface(s) as listed below,
 * and the following specific properties:
 * <dl>
 * <dt>HIGHLIGHTCOLOR</dt><dd>the color used for highlighting the counter</dd>
 * </dl>
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 * @see algoanim.properties.meta.FontContainingAnimationProperties
 * @see algoanim.properties.meta.FillableAnimationProperties
 */
public class CounterProperties extends AnimationProperties implements
    FillableAnimationProperties, FontContainingAnimationProperties{

  /**
   * Generates an unnamed <code>CounterProperties</code> object.
   */
  public CounterProperties() {
   this("unnamed counter property");
  }

  /**
   * Generates a named <code>CounterProperties</code> object.
   * 
   * @param name
   *          the name of this <code>CounterProperties</code>.
   */

  public CounterProperties(String name) {
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
    data.put(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        new ColorPropertyItem());
//
//    // property entries for FillableAnimationProperties
//    data.put(AnimationPropertiesKeys.FILL_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.FILLED_PROPERTY, new BooleanPropertyItem());
//
//    // property entries for FontContainingAnimationProperties
//    data.put(AnimationPropertiesKeys.FONT_PROPERTY, new FontPropertyItem());
//
//    // default property entries
//    data.put(AnimationPropertiesKeys.COLOR_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.DEPTH_PROPERTY, new IntegerPropertyItem());
//    data.put(AnimationPropertiesKeys.HIDDEN_PROPERTY, new BooleanPropertyItem());
//    
//    // enter all additional values
//    fillAdditional();
  }

}
