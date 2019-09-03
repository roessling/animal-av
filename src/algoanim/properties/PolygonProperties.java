package algoanim.properties;

import algoanim.properties.meta.FillableAnimationProperties;

/**
 * Implements the properties for a generic polygon shape.
 * These objects possess the standard properties (COLOR, DEPTH, HIDDEN) and the
 * "shared" properties of their implemented interface(s) as listed below.
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 * @see algoanim.properties.meta.FillableAnimationProperties
 */
public class PolygonProperties extends AnimationProperties implements
    FillableAnimationProperties {

  /**
   * Generates an unnamed <code>PolygonProperties</code> object.
   */
  public PolygonProperties() {
    this("unnamed polygon property");
  }

  /**
   * Generates a named <code>PolygonProperties</code> object.
   * 
   * @param name
   *          The name
   */
  public PolygonProperties(String name) {
    super(name);
    fillHashMap();
  }

//  /**
//   * @see algoanim.properties.AnimationProperties#fillHashMap()
//   */
//  protected void fillHashMap() {
//    // no specific entries for this object type  
//
//    // property entries for FillableAnimationProperties
//    data.put(AnimationPropertiesKeys.FILL_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.FILLED_PROPERTY, new BooleanPropertyItem());
//
//
//    // default property entries
//    data.put(AnimationPropertiesKeys.COLOR_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.DEPTH_PROPERTY, new IntegerPropertyItem());
//    data.put(AnimationPropertiesKeys.HIDDEN_PROPERTY, new BooleanPropertyItem());
//
//    // enter all additional values
//
//    fillAdditional();
//  }

}
