package animal.exchange.animalscript2;

import java.util.HashMap;

import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.DisplayOptions;
import algoanim.util.Hidden;
import algoanim.util.MsTiming;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;

public abstract class PTGraphicObjectExporter implements Exporter {
	static HashMap<Integer, Primitive> hasBeenExported =
		new HashMap<Integer, Primitive>(403);

	public static boolean getExportStatus(PTGraphicObject ptgo) {
		return hasBeenExported.containsKey(ptgo.getNum(false));
	}

	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param ptgo the graphical object to export; will cause an error in this
	 * class (must be used in subclasses!)
	 */
	public abstract void export(Language lang, PTGraphicObject ptgo, 
			boolean isVisible, int offset, int duration, boolean timeUnitIsTicks);
	
	protected Timing createTiming(Language lang, int offset,
			boolean timeUnitIsTicks) {
		if (offset == -1)
			return new TicksTiming(0);
//		return createTimingInformation(lang, -1, duration, timeUnitIsTicks);
//	}
//	
//	protected Timing createOffset(Language lang, int offset,
//			boolean timeUnitIsTicks) {
//		return createTimingInformation(lang, offset, -1, timeUnitIsTicks);
//	}
//	
//	private Timing createTimingInformation(Language lang,
//			int offset, int duration, boolean timeUnitIsTicks) {
		if (timeUnitIsTicks)
				return new TicksTiming(offset);
		return new MsTiming(offset);
	}
	
	protected DisplayOptions createDisplayOptions(Language lang,
			PTGraphicObject ptgo, boolean isVisible) {
		if (!isVisible)
			return new Hidden();
		return null;
	}
	
	protected void installStandardProperties(AnimationProperties p,
			PTGraphicObject shape, boolean isVisible) {
		p.set(AnimationPropertiesKeys.COLOR_PROPERTY, shape.getColor());
		p.set(AnimationPropertiesKeys.DEPTH_PROPERTY, shape.getDepth());
		p.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, !isVisible);
	}
}
