package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTSquare;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTSquareImporter extends PTGraphicObjectImporter {
  public Object importFrom(
  int version, StreamTokenizer stok) {
    PTSquare shape = new PTSquare();
    try {
      // parse square node
      shape.setSquareNode(ParseSupport.parseNode(stok, "Square node"));

      // parse the keyword "size"
      ParseSupport.parseMandatoryWord(stok, "Square keyword 'size'", "size");

      // parse and assign the shape's size!
      shape.setSize(ParseSupport.parseInt(stok, "Square side size"));

      // parse the square color
      shape.setColor(ParseSupport.parseColor(stok, "Square color", "color"));

      // parse optional fill attributes
      parseFillAttributes(stok, shape, "Square");

      // parse common end attributes
      parseEndingValuesFrom(stok, shape, "Square");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return shape;
  }
}
