package animal.exchange.animalascii;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.PrintWriter;

import animal.graphics.PTArc;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTText;

public class PTArcExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    PTArc arc = (PTArc) ptgo;

    // write out the information of the super object
    pw.print(arc.getFileVersion());
    pw.print(" object ");
    pw.print(arc.getNum(false));

    // write this object's information
    Point center = arc.getCenter();
    pw.print(" Arc center (");

    pw.print(center.x);
    pw.print(",");
    pw.print(center.y);
    pw.print(") rX ");
    pw.print(arc.getXRadius());
    pw.print(" rY ");
    pw.print(arc.getYRadius());
    pw.print(" color (");
    Color color = arc.getColor();
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(") fillColor (");
    color = arc.getFillColor();
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(")");
    pw.print(" angle ");
    pw.print(arc.getTotalAngle());
    pw.print(" starts ");
    pw.print(arc.getStartAngle());
    if (arc.isFilled())
      pw.print(" filled");
    if (arc.isCircular())
      pw.print(" circle");
    if (arc.isClockwise())
      pw.print(" clockwise");
    if (arc.hasFWArrow())
      pw.print(" fwArrow");
    if (arc.hasBWArrow())
      pw.print(" bwArrow");
    if (arc.isClosed())
      pw.print(" closed");
    pw.print(" text: \"");
    PTText textCompo = arc.getTextComponent();
    pw.print(PTText.escapeText(textCompo.getText()));
    pw.print("\"");

    color = textCompo.getColor();
    pw.print(" textColor (");
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(")");

    Font font = textCompo.getFont();
    pw.print(" Font {\"");

    pw.print(font.getName());
    pw.print("\" ");
    pw.print(font.getStyle());
    pw.print(" ");
    pw.print(font.getSize());
    pw.print(" }");
    pw.print(" depth ");
    pw.println(arc.getDepth());
  }
}
