package animal.exchange.animalscript;

import java.awt.Point;

import animal.graphics.PTGraph;
import animal.graphics.PTGraphicObject;
import animal.misc.ColorChoice;

public class PTGraphExporter extends PTGraphicObjectExporter
{
  public String getExportString(PTGraphicObject ptgo)
  {
    StringBuilder sb = new StringBuilder(200);
    // write out the information of the super object
    PTGraph shape = (PTGraph)ptgo;
    if (getExportStatus(ptgo))
      return "# previously exported: '" +shape.getNum(false) +"/" +shape.getObjectName();

    sb.append("graph \"").append(shape.getObjectName()).append("\"");
    sb.append(" size ").append(shape.getSize());
//    sb.append(" (");
//    sb.append(shape.getLocation().x);
//    sb.append(','); 
//    sb.append(shape.getLocation().y);
//    sb.append(')');

    sb.append(" color " +ColorChoice.getColorName(shape.getColor()));
    sb.append(" bgColor " +ColorChoice.getColorName(shape.getBGColor()));
    sb.append(" highlightColor " +ColorChoice.getColorName(shape.getHighlightColor()));
//    sb.append(" fillColor " +ColorChoice.getColorName(shape.getBGColor()));
//    sb.append(" elementColor " +ColorChoice.getColorName(shape.getFontColor()));
    sb.append(" elemHighlightColor " +ColorChoice.getColorName(shape.getElemHighlightColor()));
    sb.append(" nodeFontColor " +ColorChoice.getColorName(shape.getNodeFontColor()));
    sb.append(" edgeFontColor " +ColorChoice.getColorName(shape.getEdgeFontColor()));
    int size = shape.getSize();
    sb.append(" nodes { " +size);
    Point node = null;
    for (int i = 0; i < size; i++) {
    	sb.append('\"').append(shape.getValueNode(i)).append("\" ");
    	if (shape.getNode(i) != null) {
    	  node = shape.getNode(i).getCenter();
    	  sb.append("(").append(node.x).append(", ").append(node.y).append(")");
    	} else sb.append("(50, 50)");
    	if (i < size - 1)
    	  sb.append(", ");
    }
    sb.append("} edges { ");
    boolean foundFirst = false;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (shape.getEdge(i, j) != null) {
          if (foundFirst)
            sb.append(", ");
          else
            foundFirst = true;
          sb.append("(").append(i).append(", ").append(j);
          sb.append(", \"").append(shape.getValueEdge(i, j)).append("\"");
        }
      }
    }
    sb.append(" }");
    sb.append(" depth " +shape.getDepth());
    hasBeenExported.put(shape, shape.getObjectName());
    return sb.toString();
  }
}
