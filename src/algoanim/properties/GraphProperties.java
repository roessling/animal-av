package algoanim.properties;

import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.meta.FillableAnimationProperties;
import algoanim.properties.meta.HighlightableAnimationProperties;

/**
 * Implements the properties for a generic graph shape. These objects possess
 * the standard properties (COLOR, DEPTH, HIDDEN), the "shared" properties of
 * their implemented interface(s) as listed below, and the following specific
 * properties:
 * <dl>
 * <dt>DIRECTED</dt>
 * <dd>determines whether the graph is directed or nor</dd>
 * <dt>EDGECOLOR</dt>
 * <dd>the color for an edge</dd>
 * <dt>HIGHLIGHTCOLOR</dt>
 * <dd>the highlight color for an element</dd>
 * <dt>NODECOLOR</dt>
 * <dd>the color for a node</dd>
 * <dt>WEIGHTED</dt>
 * <dd>determines whether the graph is weighted or nor</dd>
 * </dl>
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 * @see algoanim.properties.meta.FillableAnimationProperties
 * @see algoanim.properties.meta.HighlightableAnimationProperties
 */
public class GraphProperties extends AnimationProperties implements
    FillableAnimationProperties, HighlightableAnimationProperties {

  /**
   * Generates an unnamed <code>GraphProperties</code> object.
   */
  public GraphProperties() {
    this("unnamed graph property");
  }

  /**
   * Generates a named <code>GraphProperties</code> object.
   * 
   * @param name
   *          the name for this GraphProperties object
   */
  public GraphProperties(String name) {
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
    data.put(AnimationPropertiesKeys.DIRECTED_PROPERTY,
        new BooleanPropertyItem());
    data.put(AnimationPropertiesKeys.EDGECOLOR_PROPERTY,
        new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.NODECOLOR_PROPERTY,
        new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.WEIGHTED_PROPERTY,
        new BooleanPropertyItem());

//    // property entries for FillableAnimationProperties
//    data.put(AnimationPropertiesKeys.FILL_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.FILLED_PROPERTY, new BooleanPropertyItem());
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
