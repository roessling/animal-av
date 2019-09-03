package animal.exchange.animalscript2;

import java.util.LinkedList;

import algoanim.primitives.ListElement;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ListElementProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTBoxPointer;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTLine;
import animal.graphics.PTRectangle;

public class PTBoxPointerExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTBoxPointer shape = (PTBoxPointer) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node upperLeft = Node.convertToNode(shape.getPosition());
		ListElementProperties lep = new ListElementProperties();
		installStandardProperties(lep, shape, isVisible);
		lep.set(AnimationPropertiesKeys.TEXT_PROPERTY, shape.getText());
		lep.set(AnimationPropertiesKeys.POSITION_PROPERTY, shape.getPointerPosition());
		lep.set(AnimationPropertiesKeys.BOXFILLCOLOR_PROPERTY, shape.getFillColor());
		PTRectangle pointerArea = shape.getPointerArea();
		if (pointerArea != null) {
			lep.set(AnimationPropertiesKeys.POINTERAREACOLOR_PROPERTY, pointerArea.getColor());
			lep.set(AnimationPropertiesKeys.POINTERAREAFILLCOLOR_PROPERTY, pointerArea.getFillColor());
		}
		if (shape.getTextComponent() != null)
			lep.set(AnimationPropertiesKeys.TEXTCOLOR_PROPERTY, shape.getTextComponent().getColor());		
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		LinkedList<Object> ptrLocations = new LinkedList<Object>();
		for (PTLine pointer : shape.getPointers())
			ptrLocations.add(pointer);
		ListElement result = lang.newListElement(upperLeft, shape.getPointerCount(), ptrLocations, 
				null, null, shape.getObjectName(), t, lep);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
