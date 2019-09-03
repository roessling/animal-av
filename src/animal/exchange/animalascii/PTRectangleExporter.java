package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTRectangle;

public class PTRectangleExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTRectangle shape = (PTRectangle) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape);

    // export node
    exportNode(pw, shape.getStartNode());

    // export size
    pw.print("size (");
    pw.print(shape.getWidth());
    pw.print(",");
    pw.print(shape.getHeight());
    pw.print(") ");

    // export color
    exportColor(pw, shape.getColor(), "color");

    // export fill attributes
    exportFillSettings(pw, shape);

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, shape);
  }
}
