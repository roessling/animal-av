package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTIntMatrix;
import animal.graphics.meta.PTMatrix;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTIntMatrixImporter extends PTMatrixImporter {

  @Override
  PTMatrix createMatrix() {
    return new PTIntMatrix();
  }

  @Override
  void setData(PTMatrix matrix, int r, int c, StreamTokenizer stok) {
    if (matrix instanceof PTIntMatrix) {
      PTIntMatrix intMatrix = (PTIntMatrix) matrix;
      try {
        intMatrix.setDataAt(r, c, ParseSupport
            .parseInt(stok, "IntMatrix value"));
      } catch (IOException e) {
        MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
      }
    }

  }

}
