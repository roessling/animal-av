package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTCircle;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTCircleImporter extends PTGraphicObjectImporter {
  public Object importFrom(
  int version, StreamTokenizer stok) {
    PTCircle shape = new PTCircle();
    try {
      // parse square node
      shape.setCenter(ParseSupport.parseNode(stok, "Circle node"));

      // parse the keyword "radius"
      ParseSupport
          .parseMandatoryWord(stok, "Square keyword 'radius'", "radius");

      // parse and assign the shape's size!
      shape.setRadius(ParseSupport.parseInt(stok, "Circle radius"));

      // parse the square color
      shape.setColor(ParseSupport.parseColor(stok, "Circle color", "color"));

      // parse optional fill attributes
      parseFillAttributes(stok, shape, "Circle");

      // parse common end attributes
      parseEndingValuesFrom(stok, shape, "Circle");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return shape;
  }
}
