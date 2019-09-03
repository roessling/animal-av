package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTStringMatrix;
import animal.graphics.meta.PTMatrix;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTStringMatrixImporter extends PTMatrixImporter {

  @Override
  PTMatrix createMatrix() {
    return new PTStringMatrix();
  }

  @Override
  void setData(PTMatrix matrix, int r, int c, StreamTokenizer stok) {
    try {
      matrix.setElementAt(r, c, ParseSupport.parseText(stok, "StringMatrix["
          + r + ", " + c + "]"));
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }

  }

}
