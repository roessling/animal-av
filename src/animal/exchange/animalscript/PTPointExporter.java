package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.misc.ColorChoice;

public class PTPointExporter extends PTGraphicObjectExporter
{
  public String getExportString(PTGraphicObject ptgo)
  {
    StringBuilder sb = new StringBuilder(200);
    // write out the information of the super object
    PTPoint point = (PTPoint)ptgo;
    if (getExportStatus(ptgo))
      return "# previously exported: '" +point.getNum(false) +"/" +point.getObjectName();

    sb.append("point ");
    sb.append("\"" +point.getObjectName() +"\"");

    sb.append(" (");
    sb.append(point.getX());
    sb.append(','); 
    sb.append(point.getY());
    sb.append(')');

    sb.append(" color " +ColorChoice.getColorName(point.getColor()));
    sb.append(" depth " +point.getDepth());
    hasBeenExported.put(point, point.getObjectName());
    return sb.toString();
  }
}
