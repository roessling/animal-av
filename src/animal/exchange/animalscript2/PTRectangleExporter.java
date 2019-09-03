package animal.exchange.animalscript2;

import java.awt.Point;

import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTRectangle;

public class PTRectangleExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTRectangle shape = (PTRectangle) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Point node = shape.getStartNode();
		Node upperLeft = new Coordinates((int)node.getX(), (int)node.getY());
		Node lowerRight = new Coordinates((int)node.getX() + shape.getWidth(),
				(int)node.getY() + shape.getHeight());
		RectProperties rp = new RectProperties();
		installStandardProperties(rp, shape, isVisible);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, shape.isFilled());
		if (shape.isFilled())
			rp.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getFillColor());
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		Rect result = lang.newRect(upperLeft, lowerRight, shape.getObjectName(), t, rp);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
