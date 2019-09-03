package animal.exchange.animalscript2;

import algoanim.primitives.Ellipse;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.EllipseProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTEllipse;
import animal.graphics.PTGraphicObject;

public class PTEllipseExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTEllipse shape = (PTEllipse) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node center = Node.convertToNode(shape.getCenter());
		Node radius = Node.convertToNode(shape.getRadius());
		EllipseProperties cp = new EllipseProperties();
		installStandardProperties(cp, shape, isVisible);
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, shape.isFilled());
		if (shape.isFilled())
			cp.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getFillColor());
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		Ellipse result = lang.newEllipse(center, radius, shape.getObjectName(), t, cp);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
