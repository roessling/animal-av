package algoanim.animalscript;

import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.StringArrayGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Timing;

/**
 * @see algoanim.primitives.generators.StringArrayGenerator
 * @author Stephan Mehlhase
 */
public class AnimalStringArrayGenerator extends AnimalArrayGenerator implements
		StringArrayGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalStringArrayGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.StringArrayGenerator
	 *      #create(algoanim.primitives.StringArray)
	 */
	public void create(StringArray anArray) {
		if (this.isNameUsed(anArray.getName()) || anArray.getName() == "") {
			anArray.setName("StringArray" + AnimalStringArrayGenerator.count);
			AnimalStringArrayGenerator.count++;
		}
		lang.addItem(anArray);

		StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		def.append("array \"").append(anArray.getName()).append("\" ");
		def.append(AnimalGenerator.makeNodeDef(anArray.getUpperLeft()));

		/* Properties */
		ArrayProperties ap = anArray.getProperties();
		addColorOption(ap, def);
		addColorOption(ap, AnimationPropertiesKeys.FILL_PROPERTY, " fillColor ", def);
    addColorOption(ap, AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, " elementColor ", def);
    addColorOption(ap, AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, " elemHighlight ", def);
    addColorOption(ap, AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, " cellHighlight ", def);
		addBooleanSwitch(ap, AnimationPropertiesKeys.DIRECTION_PROPERTY, " vertical ",
		    " horizontal ", def);
		def.append("length ").append(anArray.getLength()).append(' ');

		for (int i = 0; i < anArray.getLength(); i++) {
			def.append("\"").append(anArray.getData(i)).append("\" ");
		}

		addIntOption(ap, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", def);
		ArrayDisplayOptions ado = (ArrayDisplayOptions) anArray.getDisplayOptions();

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
		addBooleanOption(ap, AnimationPropertiesKeys.HIDDEN_PROPERTY, " hidden ", def);
		lang.addLine(def);
	}

	/**
	 * @see algoanim.primitives.generators.StringArrayGenerator #put(
	 *      algoanim.primitives.StringArray, int, java.lang.String,
	 *      algoanim.util.Timing, algoanim.util.Timing)
	 */
	public void put(StringArray sap, int where, String what, Timing delay,
			Timing duration) {
	  StringBuilder sb = new StringBuilder(128);
	  sb.append("arrayPut \"").append(what).append("\" on \"");
	  sb.append(sap.getName()).append("\" position ").append(where);
	  addWithTiming(sb, delay, duration);
	}
	
	public void showIndices(StringArray sap, boolean show, Timing delay,
			Timing duration) {
	  StringBuilder sb = new StringBuilder(128);
	  sb.append("arrayIndices set visibility ").append(show).append(" on \"");
	  sb.append(sap.getName()).append("\"");
	  addWithTiming(sb, delay, duration);
	}
}
