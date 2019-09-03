package animal.exchange.animalascii;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.PrintWriter;
import java.util.Vector;

import animal.graphics.PTBoxPointer;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTLine;
import animal.graphics.PTPoint;
import animal.graphics.PTRectangle;
import animal.graphics.PTText;

public class PTBoxPointerExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    PTBoxPointer boxPointer = (PTBoxPointer) ptgo;
    PTLine tmpPointer = null; // small helper :-)

    int i, n;

    // write out the information of the super object
    pw.print(boxPointer.getFileVersion());
    pw.print(" object ");
    pw.print(boxPointer.getNum(false));

    // write this object's information
    Color color = boxPointer.getColor();

    pw.print(" BoxPointer color (");
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(") fillColor (");

    color = boxPointer.getTextBox().getFillColor();
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(") pointerAreaColor (");

    color = boxPointer.getPointerArea().getColor();
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(") pointerFillColor (");

    color = boxPointer.getPointerArea().getFillColor();
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(") textBox { ");

    PTRectangle box = boxPointer.getTextBox();
    int x = box.getStartNode().x, y = box.getStartNode().y;
    Point dummy = new Point(x, y);
    exportNode(pw, dummy); // top left
    dummy.setLocation(x + box.getWidth(), y);
    exportNode(pw, dummy); // top right
    dummy.setLocation(x + box.getWidth(), y + box.getHeight());
    exportNode(pw, dummy); // bottom right
    dummy.setLocation(x, y + box.getHeight());
    exportNode(pw, dummy); // bottom left
    dummy.setLocation(x, y);
    exportNode(pw, dummy); // top left

    // n = box.getNodeCount() - 1; // last corner not needed...!
    // for (i = 0; i < n; i++) {
    // node = box.getNode(i);
    // pw.print(" (");
    // pw.print(node.getX());
    // pw.print(",");
    // pw.print(node.getY());
    // pw.print(")");
    // }
    pw.print(" } pointerArea {");

    box = boxPointer.getPointerArea();
    x = box.getStartNode().x;
    y = box.getStartNode().y;
    dummy.setLocation(x, y);
    exportNode(pw, dummy); // top left
    dummy.setLocation(x + box.getWidth(), y);
    exportNode(pw, dummy); // top right
    dummy.setLocation(x + box.getWidth(), y + box.getHeight());
    exportNode(pw, dummy); // bottom right
    dummy.setLocation(x, y + box.getHeight());
    exportNode(pw, dummy); // bottom left
    dummy.setLocation(x, y);
    exportNode(pw, dummy); // top left
    // n = box.getNodeCount() - 1; // last corner not needed...!
    // for (i = 0; i < n; i++) {
    // node = box.getNode(i);
    // pw.print(" (");
    // pw.print(node.getX());
    // pw.print(",");
    // pw.print(node.getY());
    // pw.print(")");
    // }
    pw.print("} pointerPosition "); // POINTER_POSITION_NONE,
                                    // POINTER_POSITION_LEFT,
                                    // POINTER_POSITION_RIGHT,
                                    // POINTER_POSITION_TOP, BOTTOM
    pw.print(boxPointer.getPointerPosition());
    pw.print(" pointers {");

    Vector<PTLine> pointers = boxPointer.getPointers();
    if (pointers != null) {
      n = pointers.size();
      for (i = 0; i < n; i++) {
        tmpPointer = pointers.get(i);
        exportNode(pw, tmpPointer.getFirstNode(), tmpPointer.getLastNode());
      }
    }
    pw.print(" } text: \"");

    // PTText textComponent = boxPointer.getTextComponent();
    // pw.print(PTText.escapeText(textComponent.getText()));
    pw.print(PTText.escapeText(boxPointer.getTextComponent().getText()));

    color = boxPointer.getTextComponent().getColor();// textComponent.getColor();
    pw.print("\" textColor (");
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(")");

    Font font = boxPointer.getTextComponent().getFont(); // textComponent.getFont();
    pw.print(" Font {\"");
    pw.print(font.getName());
    pw.print("\" ");
    pw.print(font.getStyle());
    pw.print(" ");
    pw.print(font.getSize());
    pw.print(" }");

    pw.print(" depth ");
    pw.println(boxPointer.getDepth());
  }
  
  private void exportNode(PrintWriter pw, PTPoint p1, PTPoint p2) {
    pw.print(" { ");
    exportNode(pw, p1.getX(), p1.getY());
    exportNode(pw, p2.getX(), p2.getY());
    pw.print("}");
  }

  private void exportNode(PrintWriter pw, int x, int y) {
    pw.print("(");
    pw.print(x);
    pw.print(", ");
    pw.print(y);
    pw.print(") ");
  }
}
