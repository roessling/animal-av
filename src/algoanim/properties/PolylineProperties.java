package algoanim.properties;

import algoanim.properties.meta.ArrowableAnimationProperties;

/**
 * Implements the properties for a generic "polyline" shape. These objects
 * possess the standard properties (COLOR, DEPTH, HIDDEN) and the "shared"
 * properties of their implemented interface(s) as listed below.
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 * @see algoanim.properties.meta.ArrowableAnimationProperties
 */
public class PolylineProperties extends AnimationProperties implements
    ArrowableAnimationProperties {

  /**
   * Generates an unnamed <code>PolylineProperties</code> object.
   */
  public PolylineProperties() {
    this("unnamed polyline property");
  }

  /**
   * Generates a named <code>PolylineProperties</code> object.
   * 
   * @param name
   *          the name for this PolylineProperties object
   */
  public PolylineProperties(String name) {
    super(name);
    fillHashMap();
  }

  // /**
  // * {@inheritDoc}
  // * @see algoanim.properties.AnimationProperties#fillHashMap()
  // */
  // protected void fillHashMap() {
  // // no specific entries for this object type
  //
  // // property entries for ArrowableAnimationProperties
  // data.put(AnimationPropertiesKeys.BWARROW_PROPERTY,
  // new BooleanPropertyItem());
  // data.put(AnimationPropertiesKeys.FWARROW_PROPERTY,
  // new BooleanPropertyItem());
  //
  // // default property entries
  // data.put(AnimationPropertiesKeys.COLOR_PROPERTY, new ColorPropertyItem());
  // data.put(AnimationPropertiesKeys.DEPTH_PROPERTY, new
  // IntegerPropertyItem());
  // data.put(AnimationPropertiesKeys.HIDDEN_PROPERTY, new
  // BooleanPropertyItem());
  //
  // // enter all additional values
  // fillAdditional();
  // }
}
