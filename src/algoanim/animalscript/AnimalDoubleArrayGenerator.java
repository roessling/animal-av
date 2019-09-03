package algoanim.animalscript;

import algoanim.primitives.DoubleArray;
import algoanim.primitives.generators.DoubleArrayGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Timing;

/**
 * @see algoanim.primitives.generators.DoubleArrayGenerator
 * @author Guido Roessling <roessling@acm.prg>
 */
public class AnimalDoubleArrayGenerator extends AnimalArrayGenerator implements
		DoubleArrayGenerator {
	private static int count = 1;

	/**
	 * @param as
	 *          the associated <code>Language</code> object.
	 */
	public AnimalDoubleArrayGenerator(AnimalScript as) {
		super(as);
	}

	/**
	 * @see algoanim.primitives.generators.IntArrayGenerator
	 *      #create(algoanim.primitives.IntArray)
	 */
	public void create(DoubleArray anArray) {
		if (this.isNameUsed(anArray.getName()) || anArray.getName() == "") {
			anArray.setName("DoubleArray" + AnimalDoubleArrayGenerator.count);
			AnimalDoubleArrayGenerator.count++;
		}
		lang.addItem(anArray);

		StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		def.append("array \"").append(anArray.getName()).append("\" ");
		def.append(AnimalGenerator.makeNodeDef(anArray.getUpperLeft()));
		def.append(' ');

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
		if (ado == null)
		  addBooleanOption(ap, AnimationPropertiesKeys.CASCADED_PROPERTY, " cascaded ", def);
		else {
		  Timing o = ado.getOffset();
			if (o != null) {
				def.append(' ').append(AnimalGenerator.makeOffsetTimingDef(o));
			}
			if (ado.getCascaded() == true) {
				def.append(" cascaded");
				Timing d = ado.getDuration();
				if (d != null) {
					def.append(AnimalGenerator.makeDurationTimingDef(d));
				}
			}

		}

		lang.addLine(def);
	}


	/**
	 * @see algoanim.primitives.generators.IntArrayGenerator #put(
	 *      algoanim.primitives.IntArray, int, int,
	 *      algoanim.util.Timing, algoanim.util.Timing)
	 */
	public void put(DoubleArray iap, int where, double what, Timing delay,
			Timing duration) {
		StringBuilder def = new StringBuilder(256);
		def.append("arrayPut \"").append(what).append("\" on \"");
		def.append(iap.getName()).append("\" position ").append(where);
		addWithTiming(def, delay, duration);
	}
	
	public void showIndices(DoubleArray iap, boolean show, Timing delay,
			Timing duration) {
	  StringBuilder sb = new StringBuilder(128);
	  sb.append("arrayIndices set visibility ").append(show).append(" on \"");
	  sb.append(iap.getName()).append("\"");
	  addWithTiming(sb, delay, duration);
	}
}
