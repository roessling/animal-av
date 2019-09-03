package algoanim.properties;

import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.meta.FontContainingAnimationProperties;

/**
 * Implements the properties for a generic text shape. These objects possess the
 * standard properties (COLOR, DEPTH, HIDDEN), the
 * "shared" properties of their implemented interface(s) as listed below,
 * and the following specific properties:
 * <dl>
 * <dt>CENTERED</dt><dd>determines if the text shall be centered</dd>
 * </dl>
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 * @see algoanim.properties.meta.FontContainingAnimationProperties
 */
public class TextProperties extends AnimationProperties implements
    FontContainingAnimationProperties {

  /**
   * Generates an unnamed <code>TextProperties</code> object.
   */
  public TextProperties() {
    this("unnamed text property");
  }

  /**
   * Generates a named <code>TextProperties</code> object.
   * 
   * @param name
   *          the name for this TextProperties object.
   */
  public TextProperties(String name) {
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
    data.put(AnimationPropertiesKeys.CENTERED_PROPERTY,
       new BooleanPropertyItem());
    data.put(AnimationPropertiesKeys.RIGHT_PROPERTY,
        new BooleanPropertyItem());
    /*
    data.put(AnimationPropertiesKeys.CENTERED_PROPERTY,
        new BooleanPropertyItem());

    // property entries for FontContainingAnimationProperties
    data.put(AnimationPropertiesKeys.FONT_PROPERTY, new FontPropertyItem());

    // default property entries
    data.put(AnimationPropertiesKeys.COLOR_PROPERTY, new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.DEPTH_PROPERTY, new IntegerPropertyItem());
    data.put(AnimationPropertiesKeys.HIDDEN_PROPERTY, new BooleanPropertyItem());

    // enter all additional values
    fillAdditional();
    */   
  }

}
