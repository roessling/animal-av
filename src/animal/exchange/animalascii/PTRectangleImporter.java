package animal.exchange.animalascii;

import java.awt.Point;
import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTRectangle;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTRectangleImporter extends PTGraphicObjectImporter {
  public Object importFrom(
  int version, StreamTokenizer stok) {
    PTRectangle shape = new PTRectangle();
    try {
      // parse square node
      shape.setStartNode(ParseSupport.parseNode(stok, "Rectangle node"));

      // parse the keyword "size"
      ParseSupport.parseMandatoryWord(stok, "Rectangle keyword 'size'", "size");

      Point sizePoint = ParseSupport.parseNode(stok, "Rectangle node");
      // parse and assign the shape's size!
      if (sizePoint == null)
        sizePoint = new Point(0, 0);
      shape.setWidth(sizePoint.x);
      shape.setHeight(sizePoint.y);

      // parse the square color
      shape.setColor(ParseSupport.parseColor(stok, "Rectangle color", "color"));

      // parse optional fill attributes
      parseFillAttributes(stok, shape, "Rectangle");

      // parse common end attributes
      parseEndingValuesFrom(stok, shape, "Rectangle");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }

    return shape;
  }
}
