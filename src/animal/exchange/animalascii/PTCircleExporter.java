package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTCircle;
import animal.graphics.PTGraphicObject;

public class PTCircleExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTCircle shape = (PTCircle) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape);

    // export centger
    exportNode(pw, shape.getCenter());

    // export radius
    pw.print("radius ");
    pw.print(shape.getRadius());
    pw.print(' ');

    // export color
    exportColor(pw, shape.getColor(), "color");

    // export fill settings (filled?, fillColor)
    exportFillSettings(pw, shape);

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, shape);
  }
}
