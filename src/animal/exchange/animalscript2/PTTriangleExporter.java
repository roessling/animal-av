package animal.exchange.animalscript2;

import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TriangleProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTTriangle;

public class PTTriangleExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTTriangle shape = (PTTriangle) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node node1 = Node.convertToNode(shape.getFirstNode());
		Node node2 = Node.convertToNode(shape.getSecondNode());
		Node node3 = Node.convertToNode(shape.getThirdNode());
		TriangleProperties tp = new TriangleProperties();
		installStandardProperties(tp, shape, isVisible);
		tp.set(AnimationPropertiesKeys.FILLED_PROPERTY, shape.isFilled());
		if (shape.isFilled())
			tp.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getFillColor());
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		Triangle result = lang.newTriangle(node1, node2, node3, shape.getObjectName(), t, tp);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
