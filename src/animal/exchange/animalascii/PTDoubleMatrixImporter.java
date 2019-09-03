package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTDoubleMatrix;
import animal.graphics.meta.PTMatrix;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTDoubleMatrixImporter extends PTMatrixImporter {

  @Override
  PTMatrix createMatrix() {
    return new PTDoubleMatrix();
  }

  @Override
  void setData(PTMatrix matrix, int r, int c, StreamTokenizer stok) {
    if (matrix instanceof PTDoubleMatrix) {
      PTDoubleMatrix dMatrix = (PTDoubleMatrix) matrix;
      try {
        dMatrix.setDataAt(r, c, ParseSupport.parseDouble(stok,
            "DoubleMatrix value"));
      } catch (IOException e) {
        MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
      }
    }
  }

}
