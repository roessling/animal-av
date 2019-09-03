package animal.exchange.animalscript2;

import algoanim.primitives.IntArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTIntArray;

public class PTIntArrayExporter extends PTGraphicObjectExporter {

  @Override
  public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
      int offset, int duration, boolean timeUnitIsTicks) {
    PTIntArray shape = (PTIntArray) ptgo;
    if (getExportStatus(shape))
      lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
          + shape.getObjectName());

    // generate visual properties
    ArrayProperties ap = new ArrayProperties();
    installStandardProperties(ap, shape, isVisible); // color, depth, hidden

    ap.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getBGColor());
    ap.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    ap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, shape.getFontColor());
    ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, shape
        .getElemHighlightColor());
    ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, shape
        .getHighlightColor());
    /*
     * DIRECTION_PROPERTY == false means: direction == horizontal (standard) ==
     * true means: direction == vertical
     */
    ap.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, false);
    ap.set(AnimationPropertiesKeys.CASCADED_PROPERTY, false);

    // retrieve upper left corner
    Node upperLeft = Node.convertToNode(shape.getBoundingBox().getLocation());

    // retrieve timing information
    Timing t = createTiming(lang, offset, timeUnitIsTicks);
    ArrayDisplayOptions ado = new ArrayDisplayOptions(t, null, false);

    // create the primitive
    IntArray result = lang.newIntArray(upperLeft, shape.getValues(), shape
        .getNumericIDs(), ado, ap);
    hasBeenExported.put(shape.getNum(false), result);

  }
}
