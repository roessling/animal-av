package animal.exchange.animalscript2;

import algoanim.primitives.EllipseSeg;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.EllipseSegProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTClosedEllipseSegment;
import animal.graphics.PTGraphicObject;

public class PTClosedEllipseSegmentExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTClosedEllipseSegment shape = (PTClosedEllipseSegment) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node center = Node.convertToNode(shape.getCenter());
		Node radius = Node.convertToNode(shape.getRadius());
    // color, depth, hidden
		EllipseSegProperties esp = new EllipseSegProperties();
    installStandardProperties(esp, shape, isVisible); 
    esp.set(AnimationPropertiesKeys.ANGLE_PROPERTY, shape.getTotalAngle());
    esp.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, shape.getStartAngle());
    esp.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, shape.isClockwise());
    esp.set(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY, !shape.isClockwise());
    esp.set(AnimationPropertiesKeys.CLOSED_PROPERTY, true);
    esp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
    esp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
    esp.set(AnimationPropertiesKeys.FILLED_PROPERTY, shape.isFilled());
    if (shape.isFilled())
      esp.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getFillColor());
    Timing t = createTiming(lang, offset, timeUnitIsTicks);
    EllipseSeg result = lang.newEllipseSeg(center, radius, shape.getObjectName(), t, esp);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
