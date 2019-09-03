package animal.exchange.tikz;

import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;

public class PTPolylineExporter extends PTGraphicObjectExporter {

	@Override
	public void exportTo(PrintWriter writer, PTGraphicObject object) {
		writer.print("\\draw ");
		PTPolyline polyline = (PTPolyline)object;
		int nodeCount = polyline.getNodeCount();
		int cNode = 0;
		for (PTPoint node: polyline.getNodes()) {
			writer.print(convertCoordinate(node));
			cNode++;
			if (cNode < nodeCount)
				writer.print(" -- ");
		}
		writer.println(";");		
	}

}
