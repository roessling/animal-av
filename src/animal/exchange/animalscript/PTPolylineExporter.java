package animal.exchange.animalscript;

import java.awt.Color;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.misc.ColorChoice;

public class PTPolylineExporter extends PTGraphicObjectExporter
{
  public String getExportString(PTGraphicObject ptgo)
  {
    // write out the information of the super object
    StringBuilder sb = new StringBuilder(200);
    PTPolyline polyline = (PTPolyline)ptgo;
    if (getExportStatus(polyline))
      return "# previously exported: '" +polyline.getNum(false) +"/" +polyline.getObjectName();

    sb.append("polyline ");
//    sb.append((polyline.isClosed()) ? "polygon " : "polyline ");
    sb.append("\"").append(polyline.getObjectName()).append("\"");

    int n = polyline.getNodeCount();
    PTPoint node = null;
    for (int i = 0; i < n; i++)
    {
      node = polyline.getNodeAt(i);
      sb.append(" ("); 
      sb.append(node.getX());
      sb.append(",");
      sb.append(node.getY());
      sb.append(")");
    }

    // write this object's information
    Color color = polyline.getColor();
    sb.append(" color ").append(ColorChoice.getColorName(color));

    sb.append(" depth ").append(polyline.getDepth());

//    if (polyline.isClosed())
//    {
//      if (polyline.isFilled())
//        sb.append(" filled");
//      sb.append(" fillColor ").append(ColorChoice.getColorName(polyline.getFillColor()));
//    } 
//    else 
//    {
      if (polyline.hasFWArrow())
        sb.append(" fwArrow");
      if (polyline.hasBWArrow())
        sb.append(" bwArrow");
//    }
    hasBeenExported.put(polyline, polyline.getObjectName());
    return sb.toString();
  }
}
