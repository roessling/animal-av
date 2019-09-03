package algoanim.properties;

import java.awt.Color;
import java.util.Vector;

import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.items.EnumerationPropertyItem;
import algoanim.properties.items.IntegerPropertyItem;
import algoanim.properties.meta.FillableAnimationProperties;
import algoanim.properties.meta.FontContainingAnimationProperties;
import algoanim.properties.meta.HighlightableAnimationProperties;

/**
 * Implements the properties for a generic matrix shape. These objects possess
 * the standard properties (COLOR, DEPTH, HIDDEN), the "shared" properties of
 * their implemented interface(s) as listed below, and the following specific
 * properties:
 * <dl>
 * <dt>CELLHIGHLIGHT</dt>
 * <dd>the color used for highlighting matrix cells</dd>
 * <dt>GRID_ALIGN</dt>
 * <dd>the alignment of the matrix values; either "left", "center" or "right"</dd>
 * <dt>GRID_STYLE</dt>
 * <dd>the display style for the matrix; either "plain", "matrix" or "table"</dd>
 * </dl>
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 * @see algoanim.properties.meta.FillableAnimationProperties
 * @see algoanim.properties.meta.FontContainingAnimationProperties
 * @see algoanim.properties.meta.HighlightableAnimationProperties
 */
public class MatrixProperties extends AnimationProperties implements
    FillableAnimationProperties, FontContainingAnimationProperties,
    HighlightableAnimationProperties {
  
  public static Vector<String> alignOptions = new Vector<String>();
  public static Vector<String> styleOptions = new Vector<String>();

  static {
    alignOptions.add("left");
    alignOptions.add("center");
    alignOptions.add("right");

    styleOptions.add("plain");
    styleOptions.add("matrix");
    styleOptions.add("table");
  }

  /**
   * Generates an unnamed <code>MatrixProperties</code> object.
   */
  public MatrixProperties() {
    this("unnamed matrix property");
  }

  /**
   * Generates a named <code>MatrixProperties</code> object.
   * 
   * @param name
   *          the name for this <code>MatrixProperties</code>.
   */
  public MatrixProperties(String name) {
    super(name);
    fillHashMap();
  }

  /**
   * @see algoanim.properties.AnimationProperties#fillHashMap()
   */
  @Override
  protected void addTypeSpecificValues() {
//    super.fillHashMap();

    data.put(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        new ColorPropertyItem());
    // specific entries for this object type
    data.put(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY,
        new EnumerationPropertyItem(alignOptions));
    // new EnumerationPropertyItem("left", new String[] {"left", "center",
    // "right"}));
    data.put(AnimationPropertiesKeys.GRID_STYLE_PROPERTY,
        new EnumerationPropertyItem(styleOptions));
    // new EnumerationPropertyItem("plain", new String[] {"plain", "matrix",
    // "table"}));
    // missing:
    // * ENUM style (plain | matrix | table)
    // * boolean fixedcellsize
    // * ENUM align (left | center | right)

    data.put(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY,
            new ColorPropertyItem());

    data.put(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY,
            new ColorPropertyItem(Color.RED));

//    // property entries for FillableAnimationProperties
//    data.put(AnimationPropertiesKeys.FILL_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.FILLED_PROPERTY, new BooleanPropertyItem());
//
//    // property entries for FontContainingAnimationProperties
//    data.put(AnimationPropertiesKeys.FONT_PROPERTY, new FontPropertyItem());
//
//    // property entries for HighlightableAnimationProperties
//    data.put(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
//        new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        new ColorPropertyItem(Color.RED));
//
//    // default property entries
//    data.put(AnimationPropertiesKeys.COLOR_PROPERTY, new ColorPropertyItem());
//    data.put(AnimationPropertiesKeys.DEPTH_PROPERTY, new IntegerPropertyItem());
//    data.put(AnimationPropertiesKeys.HIDDEN_PROPERTY, new BooleanPropertyItem());
//
//    // enter all additional values
//    fillAdditional();
    
    data.put(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY,
        new IntegerPropertyItem(-1, -1, Integer.MAX_VALUE));
    data.put(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY,
        new IntegerPropertyItem(-1, -1, Integer.MAX_VALUE));
  }
}
