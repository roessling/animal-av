package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Vector;

import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animalscript.core.AnimalParseSupport;

public class PTPolylineImporter extends PTGraphicObjectImporter {
  public Object importFrom(int version, StreamTokenizer stok) {
    PTPolyline shape = new PTPolyline();
//     boolean isClosed = false, isFilled = false;
//     Color fillColor = null;
    try {
      // 1. check file versioning
      if (version > shape.getFileVersion())
        ParseSupport.formatException2("fileVersionMismatch", new Object[] {
            String.valueOf(version), String.valueOf(shape.getFileVersion()),
            stok.toString() });

      // 3. read in the color
      shape.setColor(ParseSupport.parseColor(stok, "Polyline color"));

      // 4. Check for optional keyword 'fwArrow'
      shape.setFWArrow(ParseSupport.parseOptionalWord(stok, "Polyline fwArrow",
          "fwArrow"));

      // 5. Check for optional keyword 'bwArrow'
      shape.setBWArrow(ParseSupport.parseOptionalWord(stok, "Polyline bwArrow",
          "bwArrow"));

      // 6. Check for optional keyword 'closed'
//      isClosed = 
      ParseSupport.parseOptionalWord(stok, "Polyline closed",
          "closed");
      // 7. Check for optional keyword 'filled'
//      isFilled = 
      ParseSupport.parseOptionalWord(stok, "Polyline filled",
          "filled");

      // 8. Check for keyword 'nodes'
      ParseSupport
          .parseMandatoryWord(stok, "Polyline keyword 'nodes'", "nodes");

      // 9. Check for ':' following 'nodes'
      ParseSupport.parseMandatoryChar(stok, "Polyline keyword nodes ':'", ':');

      // 10. Parse the nodes as a polyline
      Vector<PTPoint> nodes = AnimalParseSupport.parsePolyline(stok,
          "Polyline nodes").getNodes();
      for (PTPoint node : nodes) {
        shape.addNode(node);
      }
      // 12. read in the Polyline fill color
      if (ParseSupport.parseOptionalWord(stok,
          "Compatibility: polyline fill color", "fillColor")) {
        stok.pushBack();
//        fillColor =
        ParseSupport.parseColor(stok, "Polyline", "fillColor");
      }

      // 13. if file version >=4 Parse the object depth
      if (version >= 4) {
        parseEndingValuesFrom(stok, shape, "Polyline");
      }

    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return shape; // thePolyline
  }
}
