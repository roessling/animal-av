package animal.exchange.animalascii;

import java.awt.Font;
import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.meta.PTMatrix;

public abstract class PTMatrixExporter extends PTGraphicObjectExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTMatrix matrix = (PTMatrix) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, matrix);

    // export Position
    exportNode(pw, matrix.getLocation());

    // export row count
    pw.print(" rowCount ");
    pw.print(matrix.getRowCount());

    // export column count per row
    pw.print(" columnsPerRow ");
    for (int row = 0; row < matrix.getRowCount(); ++row) {
      pw.print(matrix.getColumnCount(row));
      pw.print(" ");
    }
   
    // export rows
    pw.print("data {");
    for (int r = 0; r < matrix.getRowCount(); ++r) {
      pw.print("(");
      for (int c = 0; c < matrix.getColumnCount(r); ++c) {
        pw.print(getDataAt(r, c, matrix));
        if (c < matrix.getColumnCount(r) - 1)
          pw.print(" ");
      }
      pw.print(")");
      if (r < matrix.getRowCount() - 1)
        pw.print(" ");
    }
    pw.print("}");

    // export cell status
    pw.print(" cellStatus {");
    for (int r = 0; r < matrix.getRowCount(); ++r) {
      pw.print("(");
      for (int c = 0; c < matrix.getColumnCount(r); ++c) {
        pw.print(matrix.getCellStatus(r, c));
        if (c < matrix.getColumnCount(r) - 1)
          pw.print(" ");
      }
      pw.print(")");
      if (r < matrix.getRowCount() - 1)
        pw.print(" ");
    }
    pw.print("}");

    // export colors
    exportColor(pw, matrix.getColor(), " color");
    exportColor(pw, matrix.getFillColor(), "fillColor");
    exportColor(pw, matrix.getHighlightColor(), "highlightColor");
    exportColor(pw, matrix.getElemHighlightColor(), "elemHighlightColor");
    exportColor(pw, matrix.getTextColor(), "textColor");

    // export fill status

    pw.print(" fillStatus ");
    pw.print(matrix.isFilled() ? 1 : 0);

    // export margins
    pw.print(" margins ");
    pw.print(matrix.getMargin(0) + " ");
    pw.print(matrix.getMargin(1) + " ");
    pw.print(matrix.getMargin(2) + " ");
    pw.print(matrix.getMargin(3));

    // export alignment
    pw.print(" textAlignment \"");
    pw.print(matrix.getRowAlignment()); //TODO was matrix.getRowAlignment()
    pw.print("\" rowAlignment \"");
    pw.print(matrix.getRowAlignment() + "\" ");

    // export font settings
    Font font = matrix.getFont();
    // pw.print(" fontname ");
    pw.print(font.getName());
    pw.print(" style ");
    pw.print(font.getStyle());
    pw.print(" size ");
    pw.print(font.getSize());

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, matrix);
  }

  abstract String getDataAt(int r, int c, PTMatrix matrix);
}
