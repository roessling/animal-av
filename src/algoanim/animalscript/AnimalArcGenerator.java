package algoanim.animalscript;

import algoanim.primitives.Arc;
import algoanim.primitives.generators.ArcGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArcProperties;

/**
 * @see algoanim.primitives.generators.ArcGenerator
 * @author Stephan Mehlhase
 */
public class AnimalArcGenerator extends AnimalGenerator implements ArcGenerator {
  private static int count = 1;

  /**
   * @param aLang
   *          the associated <code>Language</code> object.
   */
  public AnimalArcGenerator(Language aLang) {
    super(aLang);
  }

  /**
   * @see algoanim.primitives.generators.ArcGenerator
   *      #create(algoanim.primitives.Arc)
   */
  public void create(Arc ag) {
    // Check name, if used already, create a new one silently
    if (this.isNameUsed(ag.getName()) || ag.getName() == "") {
      ag.setName("Arc" + AnimalArcGenerator.count++);
    }
    lang.addItem(ag);

    StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
    str.append("arc \"").append(ag.getName()).append("\" ");
    str.append(AnimalGenerator.makeNodeDef(ag.getCenter()));
    str.append(" radius ").append(AnimalGenerator.makeNodeDef(ag.getRadius()));

    ArcProperties props = ag.getProperties();
    addIntOption(props, AnimationPropertiesKeys.ANGLE_PROPERTY, " angle ", str);
    addIntOption(props, AnimationPropertiesKeys.STARTANGLE_PROPERTY,
        " starts ", str);
    if (!addBooleanOption(props, AnimationPropertiesKeys.CLOCKWISE_PROPERTY,
        " clockwise ", str))
      addBooleanOption(props,
          AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY,
          " counterclockwise ", str);
    addColorOption(props, AnimationPropertiesKeys.COLOR_PROPERTY, " color ",
        str);
    addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", str);
    if (addBooleanOption(props, AnimationPropertiesKeys.CLOSED_PROPERTY,
        " closed ", str)) {
      if (addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY,
          "filled ", str))
        addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY,
            " fillColor ", str);
    } else {
      addBooleanOption(props, AnimationPropertiesKeys.FWARROW_PROPERTY,
          " fwArrow ", str);
      addBooleanOption(props, AnimationPropertiesKeys.BWARROW_PROPERTY,
          " bwArrow ", str);
    }

    str.append(AnimalGenerator.makeDisplayOptionsDef(ag.getDisplayOptions(),
        props));

    lang.addLine(str);
  }
}
