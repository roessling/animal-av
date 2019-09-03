package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTLine;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTLineImporter extends PTGraphicObjectImporter {
  public Object importFrom(
  int version, StreamTokenizer stok) {
    PTLine shape = new PTLine();
    try {
      // parse line start node
      shape.setFirstNode(ParseSupport.parseNode(stok, "Line start node"));

      // parse line end node
      shape.setLastNode(ParseSupport.parseNode(stok, "Line end node"));

      // parse the square color
      shape.setColor(ParseSupport.parseColor(stok, "Square color", "color"));

      shape.setFWArrow(ParseSupport.parseOptionalWord(stok,
          "Optional forward arrow for line", "fwArrow"));

      shape.setBWArrow(ParseSupport.parseOptionalWord(stok,
          "Optional backward arrow for line", "bwArrow"));

      // parse common end attributes
      parseEndingValuesFrom(stok, shape, "Square");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return shape;
  }
}
