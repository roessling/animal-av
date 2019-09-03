package algoanim.properties;

import algoanim.properties.items.IntegerPropertyItem;
import algoanim.properties.meta.ArrowableAnimationProperties;
import algoanim.properties.meta.ClosableAnimationProperties;
import algoanim.properties.meta.FillableAnimationProperties;
import algoanim.properties.meta.OrientedAnimationProperties;

/**
 * Implements the properties for a generic "arc" shape. These objects possess
 * the standard properties (COLOR, DEPTH, HIDDEN), the "shared" properties of
 * their implemented interface(s) as listed below, and the following specific
 * properties:
 * <dl>
 * <dt>ANGLE</dt>
 * <dd>the total angle of the arc</dd>
 * <dt>STARTANGLE</dt>
 * <dd>the starting angle of the arc</dd>
 * </dl>
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 * @see algoanim.properties.meta.ArrowableAnimationProperties
 * @see algoanim.properties.meta.ClosableAnimationProperties
 * @see algoanim.properties.meta.FillableAnimationProperties
 * @see algoanim.properties.meta.OrientedAnimationProperties
 */
public class ArcProperties extends AnimationProperties implements
    ArrowableAnimationProperties, ClosableAnimationProperties,
    FillableAnimationProperties, OrientedAnimationProperties {

  /**
   * Generates an unnamed <code>ArcProperties</code> object.
   */
  public ArcProperties() {
    this("unnamed arc property");
  }

  /**
   * Generates a named <code>ArcProperties</code> object.
   * 
   * @param name
   *          the name of this <code>ArcProperties</code>.
   */
  public ArcProperties(String name) {
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
    data.put(AnimationPropertiesKeys.ANGLE_PROPERTY, new IntegerPropertyItem(0,
        0, 360));
    data.put(AnimationPropertiesKeys.STARTANGLE_PROPERTY,
        new IntegerPropertyItem(0, 0, 360));
//
//    // property entries for ArrowableAnimationProperties
//    data.put(AnimationPropertiesKeys.BWARROW_PROPERTY,
//        new BooleanPropertyItem());
//    data.put(AnimationPropertiesKeys.FWARROW_PROPERTY,
//        new BooleanPropertyItem());
//
//    // property entries for ClosableAnimationProperties
//    data.put(AnimationPropertiesKeys.CLOSED_PROPERTY, new BooleanPropertyItem());
//
//    // property entries for FillableAnimationProperties
//    data.put(AnimationPropertiesKeys.FILL_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.FILLED_PROPERTY, new BooleanPropertyItem());
//
//    // property entries for OrientedAnimationProperties
//    data.put(AnimationPropertiesKeys.CLOCKWISE_PROPERTY,
//        new BooleanPropertyItem());
//    data.put(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY,
//        new BooleanPropertyItem());
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
