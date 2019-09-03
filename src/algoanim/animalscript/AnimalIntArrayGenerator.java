package algoanim.animalscript;

import algoanim.primitives.IntArray;
import algoanim.primitives.generators.IntArrayGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Timing;

/**
 * @see algoanim.primitives.generators.IntArrayGenerator
 * @author jens
 */
public class AnimalIntArrayGenerator extends AnimalArrayGenerator implements
    IntArrayGenerator {
  private static int count = 1;

  /**
   * @param as
   *          the associated <code>Language</code> object.
   */
  public AnimalIntArrayGenerator(AnimalScript as) {
    super(as);
  }

  /**
   * @see algoanim.primitives.generators.IntArrayGenerator
   *      #create(algoanim.primitives.IntArray)
   */
  public void create(IntArray anArray) {
    if (this.isNameUsed(anArray.getName()) || anArray.getName() == "") {
      anArray.setName("IntArray" + AnimalIntArrayGenerator.count);
      AnimalIntArrayGenerator.count++;
    }
    lang.addItem(anArray);

    StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
    def.append("array \"").append(anArray.getName()).append("\" ");
    def.append(AnimalGenerator.makeNodeDef(anArray.getUpperLeft()));
    def.append(' ');

    /* Properties */
    ArrayProperties ap = anArray.getProperties();
    addColorOption(ap, def);
    addColorOption(ap, AnimationPropertiesKeys.FILL_PROPERTY, " fillColor ",
        def);
    addColorOption(ap, AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, " elementColor ",
        def);
    addColorOption(ap, AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        " elemHighlight ", def);
    addColorOption(ap, AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        " cellHighlight ", def);
    addBooleanSwitch(ap, AnimationPropertiesKeys.DIRECTION_PROPERTY,
        " vertical ", " horizontal ", def);

    def.append("length ").append(anArray.getLength()).append(' ');

    for (int i = 0; i < anArray.getLength(); i++) {
      def.append("\"").append(anArray.getData(i)).append("\" ");
    }

    addIntOption(ap, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", def);
    ArrayDisplayOptions ado = (ArrayDisplayOptions) anArray.getDisplayOptions();
    if (ado == null)
      addBooleanOption(ap, AnimationPropertiesKeys.CASCADED_PROPERTY,
          " cascaded ", def);

    if (ado != null) {
      Timing o = ado.getOffset();
      if (o != null) {
        def.append(" ").append(AnimalGenerator.makeOffsetTimingDef(o));
      }
      if (ado.getCascaded() == true) {
        def.append(" cascaded");
        Timing d = ado.getDuration();
        if (d != null) {
          def.append(AnimalGenerator.makeDurationTimingDef(d));
        }
      }
    }
    addFontOption(ap, AnimationPropertiesKeys.FONT_PROPERTY, " font ", def);

    lang.addLine(def);
  }

  /**
   * @see algoanim.primitives.generators.IntArrayGenerator
   *      #put(algoanim.primitives.IntArray, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void put(IntArray iap, int where, int what, Timing delay,
      Timing duration) {
    StringBuilder sb = new StringBuilder(256);
    sb.append("arrayPut \"").append(what).append("\" on \"");
    sb.append(iap.getName()).append("\" position ").append(where);
    addWithTiming(sb, delay, duration);
  }
	
	public void showIndices(IntArray iap, boolean show, Timing delay,
			Timing duration) {
	  StringBuilder sb = new StringBuilder(128);
	  sb.append("arrayIndices set visibility ").append(show).append(" on \"");
	  sb.append(iap.getName()).append("\"");
	  addWithTiming(sb, delay, duration);
	}

}
