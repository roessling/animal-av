package animal.exchange.animalscript2;

import algoanim.primitives.EllipseSeg;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.EllipseSegProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTOpenEllipseSegment;

public class PTOpenEllipseSegmentExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
    PTOpenEllipseSegment shape = (PTOpenEllipseSegment) ptgo;
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
    esp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, shape.hasFWArrow());
    esp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, shape.hasBWArrow());
    Timing t = createTiming(lang, offset, timeUnitIsTicks);
    EllipseSeg result = lang.newEllipseSeg(center, radius, shape.getObjectName(), t, esp);
    hasBeenExported.put(shape.getNum(false), result);
	}
}
