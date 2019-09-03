package animal.exchange.animalscript2;

import java.util.Vector;

import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;

public class PTPolylineExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTPolyline shape = (PTPolyline) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node[] vertices = new Node[shape.getNodeCount()];
		Vector<PTPoint> nodes = shape.getNodes();
		int pos = 0;
		for (PTPoint node: nodes)
			vertices[pos++] = Node.convertToNode(node.toPoint());
		PolylineProperties pp = new PolylineProperties();
		installStandardProperties(pp, shape, isVisible);
		pp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, shape.hasBWArrow());
		pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, shape.hasFWArrow());
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		Polyline result = lang.newPolyline(vertices, shape.getObjectName(), t, pp);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
