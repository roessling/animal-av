/*
 * PTStringArrayExporter.java
 * Exporter for PTStringArrays.
 *
 * Created on 16. October 2005, 16:33
 *
 * @author Michael Schmitt
 * @version 0.4
 * @date 2005-11-30
 */

package animal.exchange.animalascii;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTStringArray;
import animal.graphics.PTText;

public class PTStringArrayExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTStringArray stringArray = (PTStringArray) ptgo;
    pw.print(stringArray.getFileVersion());
    pw.print(" object ");
    pw.print(stringArray.getNum(false));

    // write this object's information
    pw.print(" StringArray size ");
    int size = stringArray.getSize();
    pw.print(size);

    pw.print(" entries {");
    for (int i = 0; i < size; i++) {
      pw.print("\"");
      pw.print(PTText.escapeText(stringArray.getValue(i)));
      pw.print("\"");
      if (i < size - 1) {
        pw.print(" ");
      }
    }

    pw.print("} location (");
    Point p = stringArray.getLocation();
    pw.print(p.x);
    pw.print(", ");
    pw.print(p.y);
    pw.print(")");

    Color color = stringArray.getBGColor();
    pw.print(" bgColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(") ");

    Font font = stringArray.getFont();
    pw.print("font (");
    pw.print(font.getName());
    pw.print(", ");
    pw.print(font.getSize());

    color = stringArray.getFontColor();
    pw.print(") fontColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(") ");

    color = stringArray.getOutlineColor();
    pw.print("outlineColor (");
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(") ");

    color = stringArray.getHighlightColor();
    pw.print("highlightColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(") ");

    color = stringArray.getElemHighlightColor();
    pw.print("elementHighlightColor (");
    pw.print(color.getRed());
    pw.print(", ");
    pw.print(color.getGreen());
    pw.print(", ");
    pw.print(color.getBlue());
    pw.print(")");

    if (stringArray.indicesShown()) {
      pw.print(" showIndices");
    }

    pw.print(" depth ");
    pw.println(stringArray.getDepth());
  }
}