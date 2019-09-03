package generators.helpers.kdTree;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

/**
 * @author mateusz
 */
public class StringArray {

  // position
  private Coordinates                     arrayPos       = new Coordinates(300,
                                                             370);

  // timing
  private TicksTiming                     highlightDelay = new TicksTiming(50);
  // private TicksTiming markerDuration = new TicksTiming(50);

  private String                          name;
  private algoanim.primitives.StringArray array;
  ArrayMarkerProperties                   markerProps;
  private Language                        lang;

  // private ArrayMarker marker;

  private static int                      counter        = 0;

  // properties
  // private String propId = "SetProperties";
  private ArrayProperties                 arrayProps;

  // private AnimationPropertiesContainer animProps;

  public StringArray(String name, AnimationPropertiesContainer animProps,
      Language lang) {

    this.arrayProps = new ArrayProperties();
//    this.animProps = animProps;

    // setFromProp(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
    // setFromProp(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);

    this.lang = lang;
    this.name = name;

    this.arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.ORANGE);
    this.arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    this.arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    this.arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    this.arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.RED);

  }

  // /**
  // * Sets the property from the animationPropertyContainer
  // *
  // * @param animationPropertyKey
  // */
  // private void setFromProp(String animationPropertyKey) {
  // this.arrayProps.set(animationPropertyKey, this.animProps.get(
  // this.propId, animationPropertyKey));
  // }

  public void show(String[] stringArray) {
    // ANI
    if (this.array != null)
      this.array.hide();

    this.array = this.lang.newStringArray(this.arrayPos, stringArray, this.name
        + counter++, null, this.arrayProps);
  }

  /**
   * Highlights a region of the array.
   * 
   * @param from
   *          start point of region
   * @param to
   *          end point of region
   */
  public void highlight(int from, int to) {
    this.array.highlightCell(from, to, null, this.highlightDelay);
  }

  public void hide() {
    this.array.hide();
  }
}
