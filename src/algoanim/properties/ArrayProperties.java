package algoanim.properties;

import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.meta.FillableAnimationProperties;
import algoanim.properties.meta.FontContainingAnimationProperties;
import algoanim.properties.meta.HighlightableAnimationProperties;

/**
 * Implements the properties for an array. These objects possess the standard
 * properties (COLOR, DEPTH, HIDDEN), the "shared" properties of their
 * implemented interface(s) as listed below, and the following specific
 * properties:
 * <dl>
 * <dt>CELLHIGHLIGHT</dt>
 * <dd>the highlight color used for the fill color of a "highlighted" cell</dd>
 * <dt>ELEMENTCOLOR</dt>
 * <dd>the color for the value inside an array cell</dd>
 * <dt>ELEMHIGHLIGHT</dt>
 * <dd>the highlight color for the value inside an array cell</dd>
 * </dl>
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 * @see algoanim.properties.meta.FillableAnimationProperties
 * @see algoanim.properties.meta.FontContainingAnimationProperties
 */
public class ArrayProperties extends AnimationProperties implements
    FillableAnimationProperties, FontContainingAnimationProperties, HighlightableAnimationProperties {

  /**
   * Generates an unnamed <code>ArrayProperties</code> object.
   */
  public ArrayProperties() {
   this("unnamed array property");
  }

  /**
   * Generates a named <code>ArrayProperties</code> object.
   * 
   * @param name
   *          the name for this <code>ArrayProperties</code>.
   */
  public ArrayProperties(String name) {
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
    // discouraged legacy properties
    data.put(AnimationPropertiesKeys.CASCADED_PROPERTY,
        new BooleanPropertyItem());
    data.put(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.DIRECTION_PROPERTY,
        new BooleanPropertyItem()); // direction: false = horizontal, true = vertical
//
//    for (String key : data.keySet())
//      System.err.println(key +" => " +data.get(key));
 /*
    // property entries for HighlightableAnimationProperties
    data.put(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        new ColorPropertyItem());

    // property entries for FillableAnimationProperties
    data.put(AnimationPropertiesKeys.FILL_PROPERTY, new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.FILLED_PROPERTY, new BooleanPropertyItem());

    // property entries for FontContainingAnimationProperties
    data.put(AnimationPropertiesKeys.FONT_PROPERTY, new FontPropertyItem());

    // default property entries
    data.put(AnimationPropertiesKeys.COLOR_PROPERTY, new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.DEPTH_PROPERTY, new IntegerPropertyItem());
    data.put(AnimationPropertiesKeys.HIDDEN_PROPERTY, new BooleanPropertyItem());

    // enter all additional values
    fillAdditional();*/
  }

}
