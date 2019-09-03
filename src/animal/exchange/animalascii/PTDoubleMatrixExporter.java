package animal.exchange.animalascii;

import animal.graphics.meta.PTMatrix;

public class PTDoubleMatrixExporter extends PTMatrixExporter {

  @Override
  String getDataAt(int r, int c, PTMatrix matrix) {
    return matrix.getElementAt(r, c);
  }

}
