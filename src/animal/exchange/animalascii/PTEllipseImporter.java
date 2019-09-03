package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTEllipse;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTEllipseImporter extends PTGraphicObjectImporter {
  public Object importFrom(
  int version, StreamTokenizer stok) {
    PTEllipse shape = new PTEllipse();
    try {
      // parse ellipse center
      shape.setCenter(ParseSupport.parseNode(stok, "Ellipse node"));

      // parse the keyword "radius"
      ParseSupport.parseMandatoryWord(stok, "Ellipse keyword 'radius'",
          "radius");

      // parse and assign the shape's size!
      shape.setRadius(ParseSupport.parseNode(stok, "Ellipse radius"));

      // parse the square color
      shape.setColor(ParseSupport.parseColor(stok, "Ellipse color", "color"));

      // parse optional fill attributes
      parseFillAttributes(stok, shape, "Ellipse");

      // parse common end attributes
      parseEndingValuesFrom(stok, shape, "Ellipse");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return shape;
  }
}
