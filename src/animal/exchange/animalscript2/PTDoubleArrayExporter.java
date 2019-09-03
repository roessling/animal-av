package animal.exchange.animalscript2;

import java.awt.Font;

import algoanim.primitives.DoubleArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Node;
import animal.graphics.PTDoubleArray;
import animal.graphics.PTGraphicObject;
import animal.misc.ColorChoice;

public class PTDoubleArrayExporter extends PTGraphicObjectExporter {
  public String getExportString(PTGraphicObject ptgo) {
    StringBuilder sb = new StringBuilder(200);
    // write out the information of the super object
    PTDoubleArray shape = (PTDoubleArray) ptgo;
    if (getExportStatus(ptgo))
      return "# previously exported: '" + shape.getNum(false) + "/"
          + shape.getObjectName();

    sb.append("array \"").append(shape.getObjectName()).append("\"");

    sb.append(" (");
    sb.append(shape.getLocation().x);
    sb.append(',');
    sb.append(shape.getLocation().y);
    sb.append(')');

    sb.append(" color " + ColorChoice.getColorName(shape.getColor()));
    sb.append(" fillColor " + ColorChoice.getColorName(shape.getBGColor()));
    sb
        .append(" elementColor "
            + ColorChoice.getColorName(shape.getFontColor()));
    sb.append(" elemHighlight "
        + ColorChoice.getColorName(shape.getElemHighlightColor()));
    sb.append(" cellHighlight "
        + ColorChoice.getColorName(shape.getHighlightColor()));
    int size = shape.getSize();
    sb.append(" length " + size);
    for (int i = 0; i < size; i++)
      sb.append('\"').append(shape.getStringValueAt(i)).append("\" ");
    Font font = shape.getFont();
    sb.append(" font ");
    sb.append(font.getName());
    sb.append(" size ");
    sb.append(font.getSize());
    if (font.isBold())
      sb.append(" bold");
    if (font.isItalic())
      sb.append(" italic");
    sb.append(" depth " + shape.getDepth());
    // hasBeenExported.put(shape.getNum(false), shape);
    return sb.toString();
  }

  @Override
  public void export(Language lang, PTGraphicObject ptgo, boolean isVisible,
      int offset, int duration, boolean timeUnitIsTicks) {
    // write out the information of the super object
    PTDoubleArray shape = (PTDoubleArray) ptgo;
    if (getExportStatus(shape))
      lang.addLine("# previously exported: '" + shape.getNum(false) + "/"
          + shape.getObjectName());

    Node location = Node.convertToNode(shape.getLocation());
    ArrayProperties ap = new ArrayProperties();
    installStandardProperties(ap, shape, isVisible);
    ap.set(AnimationPropertiesKeys.FILL_PROPERTY, shape.getBGColor());
    ap.set(AnimationPropertiesKeys.COLOR_PROPERTY, shape.getColor());
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
    ap.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);
    ap.set(AnimationPropertiesKeys.CASCADED_PROPERTY, false);
    double[] values = new double[shape.getSize()];
    for (int i = 0; i < values.length; i++)
      values[i] = shape.getValue(i);
    ArrayDisplayOptions t = new ArrayDisplayOptions(null, null, false);
    DoubleArray result = lang.newDoubleArray(location, values, shape
        .getObjectName(), t, ap);
    hasBeenExported.put(shape.getNum(false), result);
  }
}
