/**
 * 
 */
package algoanim.properties;

import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.meta.FontContainingAnimationProperties;
import algoanim.properties.meta.HighlightableAnimationProperties;

/**
 * Implements the properties for a generic "stack" shape. These objects possess
 * the standard properties (COLOR, DEPTH, HIDDEN), the "shared" properties of
 * their implemented interface(s) as listed below, and the following specific
 * properties:
 * <dl>
 * <dt>ALTERNATE_FILL</dt>
 * <dd>the fill color for each second ("alternating") cell</dd>
 * <dt>ALTERNATE_FILLED</dt>
 * <dd>determines whether each second ("alternating") cell shall be filled or not</dd>
 * <dt>CELLHIGHLIGHT</dt>
 * <dd>the highlight color for a cell</dd>
 * <dt>DIVIDINGLINE_COLOR</dt>
 * <dd>the color for the line between two cells</dd>
 * </dl>
 * 
 * @author Dima Vronskyi, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 * @see algoanim.properties.meta.FontContainingAnimationProperties
 * @see algoanim.properties.meta.OrientedAnimationProperties
 */
public class StackProperties extends AnimationProperties implements
    FontContainingAnimationProperties, HighlightableAnimationProperties {

  /**
   * Generates an unnamed <code>StackProperties</code> object.
   */
  public StackProperties() {
    this("unnamed stack property");
  }

  /**
   * Generates a named <code>StackProperties</code> object.
   * 
   * @param name
   *          the name for this <code>StackProperties</code>.
   */
  public StackProperties(String name) {
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
    data.put(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY,
        new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY,
        new BooleanPropertyItem());
    data.put(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.DIVIDINGLINE_COLOR_PROPERTY,
        new ColorPropertyItem());

//    // property entries for FontContainingAnimationProperties
//    data.put(AnimationPropertiesKeys.FONT_PROPERTY, new FontPropertyItem());
//
//    // property entries for HighlightableAnimationProperties
//    data.put(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
//        new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
//        new ColorPropertyItem());
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
