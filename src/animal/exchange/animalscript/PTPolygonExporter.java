package animal.exchange.animalscript;

import java.awt.Color;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolygon;
import animal.misc.ColorChoice;

public class PTPolygonExporter extends PTGraphicObjectExporter {
  public String getExportString(PTGraphicObject ptgo) {
    // write out the information of the super object
    StringBuilder sb = new StringBuilder(200);
    PTPolygon shape = (PTPolygon) ptgo;
    if (getExportStatus(shape))
      return "# previously exported: '" + shape.getNum(false) + "/"
          + shape.getObjectName();

    sb.append("polygon \"").append(shape.getObjectName()).append("\" ");

    for (PTPoint node : shape.getNodes()) {
      sb.append('(').append(node.getX()).append(", ").append(node.getY());
      sb.append(") ");
    }

    // write this object's information
    Color color = shape.getColor();
    sb.append(" color ").append(ColorChoice.getColorName(color));

    sb.append(" depth ").append(shape.getDepth());

    if (shape.isFilled()) {
      sb.append(" filled");
      sb.append(" fillColor ").append(
          ColorChoice.getColorName(shape.getFillColor()));
    }
    hasBeenExported.put(shape, shape.getObjectName());
    return sb.toString();
  }
}
