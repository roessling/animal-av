/*
 * PTStringArrayExporter.java
 * Exporter for PTStringArrays.
 *
 * Created on 8. November 2005, 22:36
 *
 * @author Michael Schmitt
 * @version 1.3
 * @date 2005-11-30
 */

package animal.exchange.animalascii;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTIntArray;

public class PTIntArrayExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTIntArray intArray = (PTIntArray) ptgo;
    pw.print(intArray.getFileVersion());
    pw.print(" object ");
    pw.print(intArray.getNum(false));

    // write this object's information
    pw.print(" IntArray size ");
    pw.print(intArray.getSize());

    pw.print(" entries {");
    int size = intArray.getSize();
    for (int i = 0; i < size; i++) {
      pw.print(intArray.getValue(i));
      if (i < size - 1) {
        pw.print(" ");
      }
    }

    pw.print("} location (");
    Point p = intArray.getLocation();
    pw.print(p.x);
    pw.print(", ");
    pw.print(p.y);
    pw.print(")");

    Color color = intArray.getBGColor();
    pw.print(" bgColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(") ");

    Font font = intArray.getFont();
    pw.print("font (");
    pw.print(font.getName());
    pw.print(", ");
    pw.print(font.getSize());

    color = intArray.getFontColor();
    pw.print(") fontColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(") ");

    color = intArray.getOutlineColor();
    pw.print("outlineColor (");
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(") ");

    color = intArray.getHighlightColor();
    pw.print("highlightColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(") ");

    color = intArray.getElemHighlightColor();
    pw.print("elementHighlightColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(")");

    if (intArray.indicesShown()) {
      pw.print(" showIndices");
    }

    pw.print(" depth ");
    pw.println(intArray.getDepth());
  }
}