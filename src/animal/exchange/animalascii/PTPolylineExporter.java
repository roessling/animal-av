package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;

public class PTPolylineExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTPolyline shape = (PTPolyline) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape);

    // write this object's information
    exportColor(pw, shape.getColor(), "color");

    if (shape.hasFWArrow())
      pw.print("fwArrow ");
    if (shape.hasBWArrow())
      pw.print("bwArrow ");

    pw.print("nodes: {");
    for (PTPoint node : shape.getNodes()) {
      exportNode(pw, node);
    }

    pw.print("} depth ");
    pw.println(shape.getDepth());
  }
}
