package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.meta.PolygonalShape;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animalscript.core.AnimalParseSupport;

public abstract class PolygonalShapeImporter extends PTGraphicObjectImporter {
  public void parseStartingValuesFrom(StreamTokenizer stok,
      PolygonalShape shape, String key) {
    try {
      // 3. read in the color
      shape.setColor(ParseSupport.parseColor(stok, key + " color"));

      // parse the fill settings (if present)
      parseFillAttributes(stok, shape, key);

      // 6. Parse the nodes for the square
      shape.setNode(0, AnimalParseSupport.parseNodeInfo(stok, key + " node",
          null));
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
  }
}
