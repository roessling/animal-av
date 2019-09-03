package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTOpenCircleSegment;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTOpenCircleSegmentImporter extends PTGraphicObjectImporter {
  public Object importFrom(
  int version, StreamTokenizer stok) {
    PTOpenCircleSegment shape = new PTOpenCircleSegment();
    try {
      // parse square node
      shape.setCenter(ParseSupport.parseNode(stok, "ClosedCircleSegment node"));

      // parse the keyword "radius"
      ParseSupport.parseMandatoryWord(stok,
          "ClosedCircleSegment keyword 'radius'", "radius");

      // parse and assign the shape's size!
      shape
          .setRadius(ParseSupport.parseInt(stok, "ClosedCircleSegment radius"));

      // parse the square color
      shape.setColor(ParseSupport.parseColor(stok, "ClosedCircleSegment color",
          "color"));

      // parse total angle
      ParseSupport.parseMandatoryWord(stok,
          "ClosedCircleSegment keyword 'angle'", "angle");
      shape.setTotalAngle(ParseSupport.parseInt(stok,
          "ClosedCircleSegment angle"));

      // parse start angle
      ParseSupport.parseMandatoryWord(stok,
          "ClosedCircleSegment keyword 'starts'", "starts");
      ParseSupport.parseMandatoryWord(stok, "ClosedCircleSegment keyword 'at'",
          "at");
      shape.setStartAngle(ParseSupport.parseInt(stok,
          "ClosedCircleSegment start angle"));

      shape.setFWArrow(ParseSupport.parseOptionalWord(stok,
          "Optional forward arrow for open circle segment", "fwArrow"));

      shape.setBWArrow(ParseSupport.parseOptionalWord(stok,
          "Optional backward arrow for open circle segment", "bwArrow"));

      // parse common end attributes
      parseEndingValuesFrom(stok, shape, "ClosedCircleSegment");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return shape;
  }
}
