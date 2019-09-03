package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTLine;

public class PTLineExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTLine shape = (PTLine) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape);

    // export start node
    exportNode(pw, shape.getFirstNode());

    // export end node
    exportNode(pw, shape.getLastNode());

    // export color
    exportColor(pw, shape.getColor(), "color");

    // export arrows
    if (shape.hasFWArrow())
      pw.print("fwArrow ");

    if (shape.hasBWArrow())
      pw.print("bwArrow ");

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, shape);
  }
}
