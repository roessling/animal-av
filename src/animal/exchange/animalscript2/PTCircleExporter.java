package animal.exchange.animalscript2;

import algoanim.primitives.Circle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTCircle;
import animal.graphics.PTGraphicObject;

public class PTCircleExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTCircle shape = (PTCircle) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node center = Node.convertToNode(shape.getCenter());
		CircleProperties cp = new CircleProperties();
		installStandardProperties(cp, shape, isVisible);
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, shape.isFilled());
		if (shape.isFilled())
			cp.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getFillColor());
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		Circle result = lang.newCircle(center, shape.getRadius(), shape.getObjectName(), t, cp);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
