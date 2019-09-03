package animal.exchange.animalscript2;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTText;

public class PTTextExporter extends PTGraphicObjectExporter {
	@Override
	public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
			int offset, int duration, boolean timeUnitIsTicks) {
		// write out the information of the super object
		PTText shape = (PTText) ptgo;
		if (getExportStatus(shape))
			lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName());

		Node node = Node.convertToNode(shape.getPosition());
		TextProperties tp = new TextProperties();
		installStandardProperties(tp, shape, isVisible);
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, shape.getFont());
		Timing t = createTiming(lang, offset, timeUnitIsTicks);
		Text result = lang.newText(node, shape.getText(), shape.getObjectName(), t, tp);
		hasBeenExported.put(shape.getNum(false), result);
	}
}
