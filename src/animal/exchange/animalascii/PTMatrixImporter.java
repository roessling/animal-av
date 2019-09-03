package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.meta.PTMatrix;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public abstract class PTMatrixImporter extends PTGraphicObjectImporter {

  public Object importFrom(int version, StreamTokenizer stok) {
    PTMatrix matrix = createMatrix();

    try {
      // //parse Position
      matrix.setPosition(ParseSupport.parseNode(stok, "Matrix node"));
      // //parse row count
      ParseSupport.parseMandatoryWord(stok, "Matrix keyword 'rowCount'",
          "rowCount");
      int rowCount = ParseSupport.parseInt(stok, "Matrix row count", 1);
      matrix.setRowCount(rowCount);
      // //parse column count per row
      ParseSupport.parseMandatoryWord(stok, "Matrix keyword 'columnsPerRow'",
          "columnsPerRow");
      for (int row = 0; row < rowCount; ++row) {
        matrix.setColumnCount(row, ParseSupport.parseInt(stok,
            "Matrix column count", 0));
      }
      // //parse rows
      ParseSupport.parseMandatoryWord(stok, "Matrix keyword 'data'", "data");
      ParseSupport.parseChar(stok, "Matrix keyword '{'", '{');
      for (int r = 0; r < rowCount; ++r) {
        ParseSupport.parseChar(stok, "Matrix keyword '('", '(');
        for (int c = 0; c < matrix.getColumnCount(r); ++c) {
          setData(matrix, r, c, stok);
        }
        ParseSupport.parseChar(stok, "Matrix keyword ')'", ')');
      }
      ParseSupport.parseChar(stok, "Matrix keyword '}'", '}');
      // //parse cell status
      ParseSupport.parseMandatoryWord(stok, "Matrix keyword 'cellStatus'",
          "cellStatus");
      ParseSupport.parseChar(stok, "Matrix keyword '{'", '{');
      for (int r = 0; r < matrix.getRowCount(); ++r) {
        ParseSupport.parseChar(stok, "Matrix keyword '('", '(');
        for (int c = 0; c < matrix.getColumnCount(r); ++c) {
          matrix.setCellStatus((byte) ParseSupport.parseInt(stok,
              "Matrix cell status", Byte.MIN_VALUE, Byte.MAX_VALUE), r, c);
        }
        ParseSupport.parseChar(stok, "Matrix keyword ')'", ')');
      }
      ParseSupport.parseChar(stok, "Matrix keyword '}'", '}');
      // //parse colors
      matrix.setColor(ParseSupport.parseColor(stok, "Matrix color", "color"));
      matrix.setFillColor(ParseSupport.parseColor(stok, "Matrix fillColor",
          "fillColor"));
      matrix.setHighlightColor(ParseSupport.parseColor(stok,
          "Matrix highlightColor", "highlightColor"));
      matrix.setElemHighlightColor(ParseSupport.parseColor(stok,
          "Matrix elemHighlightColor", "elemHighlightColor"));
      matrix.setTextColor(ParseSupport.parseColor(stok, "Matrix textColor",
          "textColor"));
      // //parse fill status
      ParseSupport.parseMandatoryWord(stok, "Matrix keyword 'fillStatus'",
          "fillStatus");
      matrix.setFilled(1 == ParseSupport.parseInt(stok, "Matrix fill status",
          0, 1));

      // //parse margins
      ParseSupport.parseMandatoryWord(stok, "Matrix keyword 'margins'",
          "margins");
      matrix.setMargin(0, ParseSupport.parseInt(stok, "Matrix margin", 0));
      matrix.setMargin(1, ParseSupport.parseInt(stok, "Matrix margin", 0));
      matrix.setMargin(2, ParseSupport.parseInt(stok, "Matrix margin", 0));
      matrix.setMargin(3, ParseSupport.parseInt(stok, "Matrix margin", 0));
      // //parse alignment
      ParseSupport.parseMandatoryWord(stok, "Matrix keyword 'textAlignment'",
          "textAlignment");
      matrix.setTextAlignment(PTMatrix.Alignment.valueOf(ParseSupport
          .parseText(stok, "Matrix text alignment")));
      ParseSupport.parseMandatoryWord(stok, "Matrix keyword 'rowAlignment'",
          "rowAlignment");
      matrix.setRowAlignment(PTMatrix.Alignment.valueOf(ParseSupport.parseText(
          stok, "Matrix row alignment")));
      // //parse font settings
      matrix.setFont(ParseSupport.parseFontInformation(stok,
          "Matrix font settings"));
      // parse common end attributes
      parseEndingValuesFrom(stok, matrix, "Matrix");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return matrix;
  }

  abstract void setData(PTMatrix matrix, int r, int c, StreamTokenizer stok);

  abstract PTMatrix createMatrix();
}
