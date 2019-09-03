package animal.exchange.animalscript;

import java.awt.Point;

import animal.graphics.PTArc;
import animal.graphics.PTGraphicObject;
import animal.misc.ColorChoice;

public class PTArcExporter extends PTGraphicObjectExporter {
	public String getExportString(PTGraphicObject ptgo) {
		StringBuilder sb = new StringBuilder(200);
		PTArc arc = (PTArc) ptgo;
		if (getExportStatus(arc))
			return "# previously exported: '" + arc.getNum(false) + "/"
					+ arc.getObjectName();

		if (arc.isCircular())
			if (arc.isClosed())
				sb.append("circle");
			else
				sb.append("ellipse");
		else
			sb.append("arc");

		sb.append(" \"").append(arc.getObjectName()).append("\" (");

		Point arcCenter = arc.getCenter();
		sb.append(arcCenter.x).append(", ").append(arcCenter.y).append(") radius ");
		if (arc.isCircular())
			sb.append(arc.getXRadius());
		else
			sb.append("(").append(arc.getXRadius()).append(", ").append(
					arc.getYRadius()).append(")");
		sb.append(" angle ").append(arc.getTotalAngle());
		sb.append(" starts ").append(arc.getStartAngle());
		sb.append((arc.isClockwise() ? " clockwise" : " counterclockwise"));
		sb.append(" color ").append(ColorChoice.getColorName(arc.getColor()));
		sb.append(" depth ").append(arc.getDepth());
		if (arc.isClosed()) {
			sb.append(" closed");
			if (arc.isFilled())
				sb.append(" filled fillColor ").append(
						ColorChoice.getColorName(arc.getColor()));
		} else {
			if (arc.hasFWArrow())
				sb.append(" fwArrow");
			if (arc.hasBWArrow())
				sb.append(" bwArrow");
		}
		hasBeenExported.put(arc, arc.getObjectName());
		return sb.toString();
	}
}
