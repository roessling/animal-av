package animal.exchange.animalascii;

import java.awt.Color;
import java.awt.Font;
import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTText;

public class PTTextExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    PTText text = (PTText) ptgo;
    // write out the information of the super object
    pw.print(text.getFileVersion());
    pw.print(" object ");
    pw.print(text.getNum(false));

    // write out this object's information
    pw.print(" Text at (");
    pw.print(text.getPosition().x);
    pw.print(',');
    pw.print(text.getPosition().y);

    Color color = text.getColor();
    pw.print(") color (");
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());

    Font font = text.getFont();
    pw.print(") font ");
    pw.print(font.getName());
    pw.print(" style ");
    pw.print(font.getStyle());
    pw.print(" size ");
    pw.print(font.getSize());
    pw.print(" text: \"");
    pw.print(PTText.escapeText(text.getText()));
    pw.print('\"');

    pw.print(" depth ");
    pw.println(text.getDepth());
  }
}
