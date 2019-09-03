package algoanim.properties;

import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.items.IntegerPropertyItem;
import algoanim.properties.meta.FontContainingAnimationProperties;

/**
 * Implements the properties for a generic source code shape. These objects
 * possess the standard properties (COLOR, DEPTH, HIDDEN), the "shared"
 * properties of their implemented interface(s) as listed below, and the
 * following specific properties:
 * <dl>
 * <dt>BOLD</dt>
 * <dd>whether the text of the source code is bold, <em>deprecated</em> - use
 * the FONT instead.</dd>
 * <dt>CONTEXTCOLOR</dt>
 * <dd>the color for source code elements marked as "context", e.g. the for loop
 * itself when a command in its body is being executed. Used at the animation
 * author's explicit request for a "context".</dd>
 * <dt>HIGHLIGHTCOLOR</dt>
 * <dd>the color for source code elements marked as "highlighted", usually the
 * currently executed command. Used at the animation author's explicit request
 * for a "highlighting".</dd>
 * <dt>INDENTATION</dt>
 * <dd>the indentation of the source code, <em>deprecated</em> - use associated
 * add* methods instead.</dd>
 * <dt>ITALIC</dt>
 * <dd>whether the text of the source code is italic, <em>deprecated</em> - use
 * the FONT instead.</dd>
 * <dt>ROW</dt>
 * <dd>the row number of the source code, <em>deprecated</em> - use associated
 * add* methods instead.</dd>
 * <dt>SIZE</dt>
 * <dd>the font size for the text of the source code, <em>deprecated</em> - use
 * the FONT instead.</dd>
 * <dt>STARTANGLE</dt>
 * <dd>the starting angle of the arc</dd>
 * </dl>
 * 
 * @author Stephan Mehlhase, Jens Pfau, Guido R&ouml;&szlig;ling
 * @version 1.1 20140206
 * @see algoanim.properties.AnimationProperties
 * @see algoanim.properties.meta.FontContainingAnimationProperties
 */
public class SourceCodeProperties extends AnimationProperties implements
    FontContainingAnimationProperties {

  /**
   * Generates an unnamed <code>SourceCodeProperties</code> object.
   */
  public SourceCodeProperties() {
    this("unnamed SourceCode property");
  }

  /**
   * Generates a named <code>SourceCodeProperties</code> object.
   * 
   * @param name
   *          the name for this SourceCodeProperties object.
   */
  public SourceCodeProperties(String name) {
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
    data.put(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        new ColorPropertyItem());
    
    // deprecated entries
    data.put(AnimationPropertiesKeys.BOLD_PROPERTY, new BooleanPropertyItem());
    data.put(AnimationPropertiesKeys.INDENTATION_PROPERTY,
        new IntegerPropertyItem());
    data.put(AnimationPropertiesKeys.ITALIC_PROPERTY, new BooleanPropertyItem());
    data.put(AnimationPropertiesKeys.ROW_PROPERTY, new IntegerPropertyItem());
    data.put(AnimationPropertiesKeys.SIZE_PROPERTY, new IntegerPropertyItem(10,
        6, 100));

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
