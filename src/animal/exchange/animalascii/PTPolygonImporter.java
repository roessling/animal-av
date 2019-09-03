package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTPolygon;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTPolygonImporter extends PTGraphicObjectImporter {
  public Object importFrom(
  int version, StreamTokenizer stok) {
    PTPolygon shape = new PTPolygon();
    try {
      // parse the keyword "size"
      ParseSupport.parseMandatoryWord(stok, "Polygon keyword 'size'", "size");

      int nrNodes = ParseSupport.parseInt(stok, "Polygon nr nodes", 2);
      for (int i = 0; i < nrNodes; i++)
        shape.setNode(i, ParseSupport.parseNode(stok, "Polygon node # " + i));

      // parse the square color
      shape.setColor(ParseSupport.parseColor(stok, "Polygon color", "color"));

      // parse optional fill attributes
      parseFillAttributes(stok, shape, "Polygon");

      // parse common end attributes
      parseEndingValuesFrom(stok, shape, "Polygon");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }

    return shape;
  }
}
