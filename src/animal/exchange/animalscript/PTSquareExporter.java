package animal.exchange.animalscript;

import java.awt.Color;
import java.awt.Point;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTSquare;
import animal.misc.ColorChoice;

public class PTSquareExporter extends PTGraphicObjectExporter {
	public String getExportString(PTGraphicObject ptgo) {
		// write out the information of the super object
		StringBuilder sb = new StringBuilder(200);
		PTSquare shape = (PTSquare) ptgo;
		if (getExportStatus(shape))
			return "# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName();

		sb.append("square \"").append("\"");
		sb.append(shape.getObjectName()).append("\" (");

		Point node = shape.getSquareNodeAsPoint();
		sb.append((int)node.getX()).append(", ").append((int)node.getY());
		sb.append(") ").append(shape.getSize());

		// write this object's information
		Color color = shape.getColor();
		sb.append(" color ").append(ColorChoice.getColorName(color));

		sb.append(" depth ").append(shape.getDepth());

		if (shape.isFilled()) {
			sb.append(" filled");
			sb.append(" fillColor ").append(
					ColorChoice.getColorName(shape.getFillColor()));
		}
		hasBeenExported.put(shape, shape.getObjectName());
		return sb.toString();
	}
}
