package animal.exchange.animalascii;

import animal.graphics.PTText;
import animal.graphics.meta.PTMatrix;

public class PTStringMatrixExporter extends PTMatrixExporter {

  @Override
  String getDataAt(int r, int c, PTMatrix matrix) {
    return "\"" + PTText.escapeText(matrix.getElementAt(r, c)) + "\"";
  }

}
