package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTOpenEllipseSegment;

public class PTOpenEllipseSegmentExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTOpenEllipseSegment shape = (PTOpenEllipseSegment) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape);

    // export center
    exportNode(pw, shape.getCenter());

    // export radius
    exportNode(pw, shape.getRadius(), "radius");

    // export color
    exportColor(pw, shape.getColor(), "color");

    // export start angle
    pw.print("angle ");
    pw.print(shape.getTotalAngle());
    pw.print(" starts at ");
    pw.print(shape.getStartAngle());
    pw.print(' ');

    // export arrow settings
    if (shape.hasFWArrow())
      pw.print("fwArrow ");

    if (shape.hasBWArrow())
      pw.print("bwArrow ");

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, shape);
  }
}
