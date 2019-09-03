package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTEllipse;
import animal.graphics.PTGraphicObject;

public class PTEllipseExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTEllipse shape = (PTEllipse) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape);

    // export center
    exportNode(pw, shape.getCenter());

    // export center
    exportNode(pw, shape.getRadius(), "radius");

    // export color
    exportColor(pw, shape.getColor(), "color");

    // export fill settings (filled?, fillColor)
    exportFillSettings(pw, shape);

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, shape);
  }
}
