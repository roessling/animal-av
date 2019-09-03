package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTClosedEllipseSegment;
import animal.graphics.PTGraphicObject;

public class PTClosedEllipseSegmentExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTClosedEllipseSegment shape = (PTClosedEllipseSegment) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape);

    // export center
    exportNode(pw, shape.getCenter());

    // export radius
    exportNode(pw, shape.getRadius(), "radius");

    // export color
    exportColor(pw, shape.getColor(), "color");

    // export fill settings (filled?, fillColor)
    exportFillSettings(pw, shape);

    // export start angle
    pw.print("angle ");
    pw.print(shape.getTotalAngle());
    pw.print(" starts at ");
    pw.print(shape.getStartAngle());
    pw.print(' ');

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, shape);
  }
}
