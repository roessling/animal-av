package animal.exchange.animalscript2;

import algoanim.primitives.Point;
import algoanim.primitives.generators.Language;
import algoanim.properties.PointProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;

public class PTPointExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTPoint shape = (PTPoint) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node location = Node.convertToNode(shape.getLocation());
		PointProperties pp = new PointProperties();
		installStandardProperties(pp, shape, isVisible);
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		Point result = lang.newPoint(location, shape.getObjectName(), t, pp);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
