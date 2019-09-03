package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolygon;

public class PTPolygonExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTPolygon shape = (PTPolygon) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape);
    pw.print("size ");
    pw.print(shape.getNodeCount());
    pw.print(" ");

    for (PTPoint node : shape.getNodes()) {
      exportNode(pw, node);
    }

    // export color
    exportColor(pw, shape.getColor(), "color");

    // export fill attributes
    exportFillSettings(pw, shape);

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, shape);
  }
}
