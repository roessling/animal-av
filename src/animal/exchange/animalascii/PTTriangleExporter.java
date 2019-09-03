package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTTriangle;

public class PTTriangleExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTTriangle shape = (PTTriangle) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape);

    // export node #1
    exportNode(pw, shape.getFirstNode());

    // export node #2
    exportNode(pw, shape.getSecondNode());

    // export node #3
    exportNode(pw, shape.getThirdNode());

    // export color
    exportColor(pw, shape.getColor(), "color");

    // export fill attributes
    exportFillSettings(pw, shape);

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, shape);
  }
}
