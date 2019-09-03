package animal.exchange.animalscript2;

import java.util.Vector;

import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Polygon;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolygonProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolygon;

public class PTPolygonExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTPolygon shape = (PTPolygon) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node[] vertices = new Node[shape.getNodeCount()];
		Vector<PTPoint> nodes = shape.getNodes();
		int pos = 0;
		for (PTPoint node: nodes)
			vertices[pos++] = Node.convertToNode(node.toPoint());
		PolygonProperties pp = new PolygonProperties();
		installStandardProperties(pp, shape, isVisible);
		pp.set(AnimationPropertiesKeys.FILLED_PROPERTY, shape.isFilled());
		if (shape.isFilled())
			pp.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getFillColor());
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		Polygon result = null;
		try {
		  result = lang.newPolygon(vertices, shape.getObjectName(), t, pp);
		  hasBeenExported.put(shape.getNum(false), result);
		} catch(NotEnoughNodesException nee) {
			lang.addLine("# could not export, not enough nodes: " +shape.getNodeCount());
		}
	}
}
