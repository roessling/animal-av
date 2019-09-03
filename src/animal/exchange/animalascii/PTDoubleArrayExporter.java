/*
 * PTDoubleArrayExporter.java
 * Exporter for PTDoubleArrays.
 *
 * Created on November 7, 2008
 *
 * @author Guido Roessling <roessling@acm.org>
 * @version 1.0
 * @date 2008-11-07
 */

package animal.exchange.animalascii;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.PrintWriter;

import animal.graphics.PTDoubleArray;
import animal.graphics.PTGraphicObject;

public class PTDoubleArrayExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTDoubleArray theArray = (PTDoubleArray) ptgo;
    pw.print(theArray.getFileVersion());
    pw.print(" object ");
    pw.print(theArray.getNum(false));

    // write this object's information
    pw.print(" DoubleArray size ");
    pw.print(theArray.getSize());

    pw.print(" entries {");
    int size = theArray.getSize();
    for (int i = 0; i < size; i++) {
      pw.print(theArray.getValue(i));
      if (i < size - 1) {
        pw.print(" ");
      }
    }

    pw.print("} location (");
    Point p = theArray.getLocation();
    pw.print(p.x);
    pw.print(", ");
    pw.print(p.y);
    pw.print(")");

    Color color = theArray.getBGColor();
    pw.print(" bgColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(") ");

    Font font = theArray.getFont();
    pw.print("font (");
    pw.print(font.getName());
    pw.print(", ");
    pw.print(font.getSize());

    color = theArray.getFontColor();
    pw.print(") fontColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(") ");

    color = theArray.getOutlineColor();
    pw.print("outlineColor (");
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(") ");

    color = theArray.getHighlightColor();
    pw.print("highlightColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(") ");

    color = theArray.getElemHighlightColor();
    pw.print("elementHighlightColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(")");

    if (theArray.indicesShown()) {
      pw.print(" showIndices");
    }

    pw.print(" depth ");
    pw.println(theArray.getDepth());
  }
}