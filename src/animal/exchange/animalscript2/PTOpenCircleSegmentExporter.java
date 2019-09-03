package animal.exchange.animalscript2;

import algoanim.primitives.CircleSeg;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleSegProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTOpenCircleSegment;

public class PTOpenCircleSegmentExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
    PTOpenCircleSegment shape = (PTOpenCircleSegment) ptgo;
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
    cp.set(AnimationPropertiesKeys.CLOSED_PROPERTY, false);
    cp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, shape.hasFWArrow());
    cp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, shape.hasBWArrow());
    Timing t = createTiming(lang, offset, timeUnitIsTicks);
    CircleSeg result = lang.newCircleSeg(center, shape.getRadius(), shape.getObjectName(), t, cp);
    hasBeenExported.put(shape.getNum(false), result);
	}
}
