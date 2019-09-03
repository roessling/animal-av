package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTClosedEllipseSegment;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTClosedEllipseSegmentImporter extends PTGraphicObjectImporter {
  public Object importFrom(
  int version, StreamTokenizer stok) {
    PTClosedEllipseSegment shape = new PTClosedEllipseSegment();
    try {
      // parse square node
      shape
          .setCenter(ParseSupport.parseNode(stok, "ClosedEllipseSegment node"));

      // parse the keyword "radius"
      ParseSupport.parseMandatoryWord(stok,
          "ClosedEllipseSegment keyword 'radius'", "radius");

      // parse and assign the shape's size!
      shape.setRadius(ParseSupport.parseNode(stok,
          "ClosedEllipseSegment radius"));

      // parse the square color
      shape.setColor(ParseSupport.parseColor(stok,
          "ClosedEllipseSegment color", "color"));

      // parse optional fill attributes
      parseFillAttributes(stok, shape, "ClosedEllipseSegment");

      // parse total angle
      ParseSupport.parseMandatoryWord(stok,
          "ClosedEllipseSegment keyword 'angle'", "angle");
      shape.setTotalAngle(ParseSupport.parseInt(stok,
          "ClosedEllipseSegment angle"));

      // parse start angle
      ParseSupport.parseMandatoryWord(stok,
          "ClosedEllipseSegment keyword 'starts'", "starts");
      ParseSupport.parseMandatoryWord(stok,
          "ClosedEllipseSegment keyword 'at'", "at");
      shape.setStartAngle(ParseSupport.parseInt(stok,
          "ClosedEllipseSegment start angle"));

      // parse common end attributes
      parseEndingValuesFrom(stok, shape, "ClosedEllipseSegment");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return shape;
  }
}
