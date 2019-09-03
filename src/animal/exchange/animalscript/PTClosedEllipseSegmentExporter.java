package animal.exchange.animalscript;

import java.awt.Color;
import java.awt.Point;

import animal.graphics.PTClosedEllipseSegment;
import animal.graphics.PTGraphicObject;
import animal.misc.ColorChoice;

public class PTClosedEllipseSegmentExporter extends PTGraphicObjectExporter {
	public String getExportString(PTGraphicObject ptgo) {
		// write out the information of the super object
		StringBuilder sb = new StringBuilder(200);
		PTClosedEllipseSegment shape = (PTClosedEllipseSegment) ptgo;
		if (getExportStatus(shape))
			return "# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName();

		sb.append("ellipsesegment \"").append(shape.getObjectName()).append("\" (");

		Point node = shape.getCenter(), radius = shape.getRadius();
		sb.append((int)node.getX()).append(", ").append((int)node.getY());
		sb.append(") radius (").append((int) radius.getX()).append(", ");
		sb.append((int)radius.getY()).append(") ");		
		
    sb.append(" angle ").append(shape.getTotalAngle());
    sb.append(" starts ").append(shape.getStartAngle());
    
    if (shape.isClockwise())
      sb.append(" clockwise ");
    else
      sb.append(" counterclockwise ");
  
    // write this object's information
    Color color = shape.getColor();
    sb.append(" color ").append(ColorChoice.getColorName(color));

    sb.append(" depth ").append(shape.getDepth());

    sb.append(" closed ");

		if (shape.isFilled()) {
			sb.append(" filled");
			sb.append(" fillColor ").append(
					ColorChoice.getColorName(shape.getFillColor()));
		}
		hasBeenExported.put(shape, shape.getObjectName());
		return sb.toString();
	}
}
