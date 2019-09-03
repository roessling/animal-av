package algoanim.animalscript;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.generators.ArrayMarkerGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.util.Timing;

/**
 * @see algoanim.primitives.generators.ArrayMarkerGenerator
 * @author Stephan Mehlhase
 */
public class AnimalArrayMarkerGenerator extends AnimalGenerator implements
    ArrayMarkerGenerator {
  private static int count = 1;

  /**
   * @param aLang
   *          the associated <code>Language</code> object.
   */
  public AnimalArrayMarkerGenerator(Language aLang) {
    super(aLang);
  }

  /**
   * @see algoanim.primitives.generators.ArrayMarkerGenerator
   *      #create(algoanim.primitives.ArrayMarker)
   */
  public void create(ArrayMarker am) {
    if (this.isNameUsed(am.getName()) || am.getName() == "") {
      am.setName("ArrayMarker" + AnimalArrayMarkerGenerator.count);
      AnimalArrayMarkerGenerator.count++;
    }
    lang.addItem(am);

    StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
    def.append("arrayMarker \"").append(am.getName()).append("\" on \"");
    def.append(am.getArray().getName()).append("\" atIndex ");
    def.append(am.getPosition());

    ArrayMarkerProperties props = am.getProperties();
    String current = (String) props.get(AnimationPropertiesKeys.LABEL_PROPERTY);
    if (current != null && current.length() > 0) {
      def.append(" label \"")
          .append((String) props.get(AnimationPropertiesKeys.LABEL_PROPERTY))
          .append("\"");
    }
    if (!addBooleanOption(props, AnimationPropertiesKeys.LONG_MARKER_PROPERTY,
        " long ", def))
      addBooleanOption(props, AnimationPropertiesKeys.SHORT_MARKER_PROPERTY,
          " short ", def);

    addColorOption(props, def);
    addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", def);

    def.append(AnimalGenerator.makeDisplayOptionsDef(am.getDisplayOptions(),
        props));

    lang.addLine(def);
  }

  /**
   * @see algoanim.primitives.generators.ArrayMarkerGenerator
   *      #move(algoanim.primitives.ArrayMarker, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void move(ArrayMarker am, int to, Timing delay, Timing duration) {
    StringBuilder sb = new StringBuilder(256);
    sb.append("moveArrayMarker \"").append(am.getName());
    sb.append("\" to position ").append(to).append(" ");
    addWithTiming(sb, delay, duration);
  }

  /**
   * @see algoanim.primitives.generators.ArrayMarkerGenerator
   *      #moveBeforeStart(algoanim.primitives.ArrayMarker,
   *      algoanim.util.Timing, algoanim.util.Timing)
   */
  public void moveBeforeStart(ArrayMarker am, Timing delay, Timing duration) {
    StringBuilder sb = new StringBuilder(256);
    sb.append("moveArrayMarker \"").append(am.getName());
    sb.append("\" to outside ");
    addWithTiming(sb, delay, duration);
  }

  /**
   * @see algoanim.primitives.generators.ArrayMarkerGenerator
   *      #moveToEnd(algoanim.primitives.ArrayMarker, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void moveToEnd(ArrayMarker am, Timing delay, Timing duration) {
    StringBuilder sb = new StringBuilder(256);
    sb.append("moveArrayMarker \"").append(am.getName());
    sb.append("\" to arrayEnd ");
    addWithTiming(sb, delay, duration);
  }

  /**
   * @see algoanim.primitives.generators.ArrayMarkerGenerator
   *      #moveOutside(algoanim.primitives.ArrayMarker, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void moveOutside(ArrayMarker am, Timing delay, Timing duration) {
    StringBuilder sb = new StringBuilder(256);
    sb.append("moveArrayMarker \"").append(am.getName());
    sb.append("\" to outside ");
    addWithTiming(sb, delay, duration);
  }

  public void decrement(ArrayMarker am, Timing delay, Timing duration) {
    StringBuilder sb = new StringBuilder(256);
    sb.append("moveArrayMarker \"").append(am.getName());
    sb.append("\" to position ").append(am.getPosition() - 1);
    sb.append(" ").append(AnimalGenerator.makeOffsetTimingDef(delay));
    addWithTiming(sb, delay, duration);
  }

  public void increment(ArrayMarker am, Timing delay, Timing duration) {
    StringBuilder sb = new StringBuilder(256);
    sb.append("moveArrayMarker \"").append(am.getName());
    sb.append("\" to position ").append(am.getPosition() + 1);
    sb.append(" ").append(AnimalGenerator.makeOffsetTimingDef(delay));
    addWithTiming(sb, delay, duration);
  }
}
