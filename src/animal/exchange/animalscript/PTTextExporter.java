package animal.exchange.animalscript;

import java.awt.Font;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTText;
import animal.misc.ColorChoice;

public class PTTextExporter extends PTGraphicObjectExporter
{
  public String getExportString(PTGraphicObject ptgo)
  {
    StringBuilder  sb = new StringBuilder(200);
    PTText text = (PTText)ptgo;
     if (getExportStatus(text))
       return "# previously exported: '" +text.getNum(false) +"/" +text.getObjectName();
     sb.append("text ").append("\"").append(text.getObjectName()).append("\" ");

    sb.append("\"").append(PTText.escapeText(text.getText())).append("\"");
    // write out this object's information
    sb.append(" (").append(text.getLocation().x);
    sb.append(',').append(text.getLocation().y).append(")");

    sb.append(" color ").append(ColorChoice.getColorName(text.getColor()));
    sb.append(" depth ").append(text.getDepth());

    Font font = text.getFont();
    sb.append(" font ");
    sb.append(font.getName());
    sb.append(" size ");
    sb.append(font.getSize());
    if (font.isBold())
      sb.append(" bold");
    if (font.isItalic())
      sb.append(" italic");
    hasBeenExported.put(text, text.getObjectName());
    return sb.toString();
  }
}
