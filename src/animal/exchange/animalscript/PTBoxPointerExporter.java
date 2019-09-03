package animal.exchange.animalscript;

import java.awt.Color;
import java.awt.Point;

import animal.graphics.PTBoxPointer;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTText;
import animal.misc.ColorChoice;

public class PTBoxPointerExporter extends PTGraphicObjectExporter {
	public String getExportString(PTGraphicObject ptgo) {
		StringBuilder sb = new StringBuilder(200);
		PTBoxPointer boxPointer = (PTBoxPointer) ptgo;

		if (getExportStatus(boxPointer))
			return "# previously exported: '" + boxPointer.getNum(false) + "/"
					+ boxPointer.getObjectName();
		int i;

		// write out the information of the super object
		sb.append("listElement \"").append(boxPointer.getObjectName()).append("\"");

		PTText textComponent = boxPointer.getTextComponent();
		boolean hasText = textComponent.getText() != null;
		if (hasText)
			sb.append(" \"").append(PTText.escapeText(textComponent.getText()))
					.append("\"");

		int nrPointers = boxPointer.getPointerCount();
		sb.append(" pointers ").append(nrPointers);

		sb.append(" position ");
		switch (boxPointer.getPointerPosition()) {
		case PTBoxPointer.POINTER_POSITION_TOP:
			sb.append("top");
			break;
		case PTBoxPointer.POINTER_POSITION_LEFT:
			sb.append("left");
			break;
		case PTBoxPointer.POINTER_POSITION_RIGHT:
			sb.append("right");
			break;
		case PTBoxPointer.POINTER_POSITION_BOTTOM:
		default:
			sb.append("bottom");
			break;
		}

		// export pointer targets!
		Point lastPoint = null;
		for (i = 1; i <= nrPointers; i++) {
			sb.append(" ptr").append(i).append(" (");
			lastPoint = boxPointer.getTip(i - 1);
			sb.append(lastPoint.x).append(", ").append(lastPoint.y).append(")");
		}

		// export "prev" information!

		// write this object's information
		Color color = boxPointer.getColor();
		sb.append(" color ").append(ColorChoice.getColorName(color));

    
		color = boxPointer.getTextBox().getFillColor();
		sb.append(" boxFillColor ").append(ColorChoice.getColorName(color));

		color = boxPointer.getPointerArea().getColor();
		sb.append(" pointerAreaColor ").append(ColorChoice.getColorName(color));

		color = boxPointer.getPointerArea().getFillColor();
		sb.append(" pointerAreaFillColor ").append(ColorChoice.getColorName(color));

		color = textComponent.getColor();
		sb.append(" textColor ").append(ColorChoice.getColorName(color));

		sb.append(" depth ");
		sb.append(boxPointer.getDepth());
		hasBeenExported.put(boxPointer, boxPointer.getObjectName());
		return sb.toString();
	}
}
