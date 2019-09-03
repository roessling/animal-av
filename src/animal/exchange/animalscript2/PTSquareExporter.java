package animal.exchange.animalscript2;

import java.awt.Point;

import algoanim.primitives.Square;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SquareProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTSquare;

public class PTSquareExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTSquare shape = (PTSquare) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Point node = shape.getSquareNodeAsPoint();
		Node upperLeft = new Coordinates((int)node.getX(), (int)node.getY());
		SquareProperties sp = new SquareProperties();
		installStandardProperties(sp, shape, isVisible);
		sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, shape.isFilled());
		if (shape.isFilled())
			sp.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getFillColor());
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		Square result = lang.newSquare(upperLeft, shape.getSize(), shape.getObjectName(), t, sp);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
