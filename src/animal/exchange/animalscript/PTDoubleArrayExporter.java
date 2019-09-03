package animal.exchange.animalscript;

import java.awt.Font;

import animal.graphics.PTDoubleArray;
import animal.graphics.PTGraphicObject;
import animal.misc.ColorChoice;

public class PTDoubleArrayExporter extends PTGraphicObjectExporter
{
  public String getExportString(PTGraphicObject ptgo)
  {
    StringBuilder sb = new StringBuilder(200);
    // write out the information of the super object
    PTDoubleArray shape = (PTDoubleArray)ptgo;
    if (getExportStatus(ptgo))
      return "# previously exported: '" +shape.getNum(false) +"/" +shape.getObjectName();

    sb.append("array \"").append(shape.getObjectName()).append("\"");

    sb.append(" (");
    sb.append(shape.getLocation().x);
    sb.append(','); 
    sb.append(shape.getLocation().y);
    sb.append(')');

    sb.append(" color " +ColorChoice.getColorName(shape.getColor()));
    sb.append(" fillColor " +ColorChoice.getColorName(shape.getBGColor()));
    sb.append(" elementColor " +ColorChoice.getColorName(shape.getFontColor()));
    sb.append(" elemHighlight " +ColorChoice.getColorName(shape.getElemHighlightColor()));
    sb.append(" cellHighlight " +ColorChoice.getColorName(shape.getHighlightColor()));
    int size = shape.getSize();
    sb.append(" length " +size);
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
    sb.append(" depth " +shape.getDepth());
    hasBeenExported.put(shape, shape.getObjectName());
    return sb.toString();
  }
}
