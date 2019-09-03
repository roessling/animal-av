package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTSquare;

public class PTSquareExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTSquare shape = (PTSquare) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape);

    // export node
    exportNode(pw, shape.getSquareNodeAsPoint());

    // export radius
    pw.print("size ");
    pw.print(shape.getSize());
    pw.print(' ');

    // export color
    exportColor(pw, shape.getColor(), "color");

    // export fill attributes
    exportFillSettings(pw, shape);

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, shape);
  }
}
