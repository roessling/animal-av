package animal.exchange.animalascii;

import java.awt.Color;
import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;

public class PTPointExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTPoint point = (PTPoint) ptgo;
    pw.print(point.getFileVersion());
    pw.print(" object ");
    pw.print(point.getNum(false));

    // 2. write out the file version -- unneccessary!
    Color color = point.getColor();

    pw.print(" Point color (");
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());

    pw.print(") at (");
    pw.print(point.getX());
    pw.print(',');
    pw.print(point.getY());
    pw.print(')');

    pw.print(" depth ");
    pw.println(point.getDepth());
  }
}
