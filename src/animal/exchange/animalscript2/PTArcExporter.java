package animal.exchange.animalscript2;

import algoanim.primitives.Arc;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArcProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTArc;
import animal.graphics.PTGraphicObject;

public class PTArcExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTArc shape = (PTArc) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node center = Node.convertToNode(shape.getCenter());
		Node radius = Node.convertToNode(shape.getRadiusPoint());
		ArcProperties ap = new ArcProperties();
		installStandardProperties(ap, shape, isVisible);
		ap.set(AnimationPropertiesKeys.FILLED_PROPERTY, shape.isFilled());
		if (shape.isFilled())
			ap.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getFillColor());
		ap.set(AnimationPropertiesKeys.ANGLE_PROPERTY, shape.getTotalAngle());
		ap.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, shape.getStartAngle());
		ap.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, shape.isClockwise());
		ap.set(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY, !shape.isClockwise());
		ap.set(AnimationPropertiesKeys.CLOSED_PROPERTY, shape.isClosed());
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.isFilled());
		if (shape.isFilled())
			ap.set(AnimationPropertiesKeys.FILLED_PROPERTY, shape.getFillColor());
		ap.set(AnimationPropertiesKeys.BWARROW_PROPERTY, shape.hasBWArrow());
		ap.set(AnimationPropertiesKeys.FWARROW_PROPERTY, shape.hasFWArrow());
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		Arc result = lang.newArc(center, radius, shape.getObjectName(), t, ap);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
