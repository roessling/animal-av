package animal.exchange.animalscript2;

import algoanim.primitives.CircleSeg;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleSegProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTClosedCircleSegment;
import animal.graphics.PTGraphicObject;

public class PTClosedCircleSegmentExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTClosedCircleSegment shape = (PTClosedCircleSegment) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node center = Node.convertToNode(shape.getCenter());
		CircleSegProperties cp = new CircleSegProperties();
		// color, depth, hidden
		installStandardProperties(cp, shape, isVisible); 
    cp.set(AnimationPropertiesKeys.ANGLE_PROPERTY, shape.getTotalAngle());
    cp.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, shape.getStartAngle());
    cp.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, shape.isClockwise());
    cp.set(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY, !shape.isClockwise());
    cp.set(AnimationPropertiesKeys.CLOSED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
    cp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, shape.isFilled());
		if (shape.isFilled())
			cp.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getFillColor());
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		CircleSeg result = lang.newCircleSeg(center, shape.getRadius(), shape.getObjectName(), t, cp);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
