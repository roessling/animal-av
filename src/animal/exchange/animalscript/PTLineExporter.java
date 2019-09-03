package animal.exchange.animalscript;

import java.awt.Color;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTLine;
import animal.graphics.PTPoint;
import animal.misc.ColorChoice;

public class PTLineExporter extends PTGraphicObjectExporter {
  public String getExportString(PTGraphicObject ptgo) {
    // write out the information of the super object
    StringBuilder sb = new StringBuilder(200);
    PTLine line = (PTLine) ptgo;
    if (getExportStatus(line))
      return "# previously exported: '" + line.getNum(false) + "/"
          + line.getObjectName();

    sb.append("line ");
    // sb.append((polyline.isClosed()) ? "polygon " : "polyline ");
    sb.append("\"").append(line.getObjectName()).append("\"");

    PTPoint node = line.getFirstNode();
//    int n = line.getNodeCount();
//    PTPoint node = null;
//    for (int i = 0; i < n; i++) {
//      node = line.getNodeAt(i);
    sb.append(" (").append(node.getX()).append(",");
    sb.append(node.getY()).append(")");
//    }
    node = line.getLastNode();
    sb.append(" (").append(node.getX()).append(",");
    sb.append(node.getY()).append(")");

    // write this object's information
    Color color = line.getColor();
    sb.append(" color ").append(ColorChoice.getColorName(color));

    sb.append(" depth ").append(line.getDepth());

    // if (polyline.isClosed())
    // {
    // if (polyline.isFilled())
    // sb.append(" filled");
    // sb.append(" fillColor ").append(ColorChoice.getColorName(polyline.getFillColor()));
    // }
    // else
    // {
    if (line.hasFWArrow())
      sb.append(" fwArrow");
    if (line.hasBWArrow())
      sb.append(" bwArrow");
    // }
    hasBeenExported.put(line, line.getObjectName());
    return sb.toString();
  }
}
