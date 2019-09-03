package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTPoint;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTPointImporter extends PTGraphicObjectImporter {
  public Object importFrom(int version, StreamTokenizer stok) {
    PTPoint result = new PTPoint();
    try {
      if (version > result.getFileVersion())
        ParseSupport.formatException2("fileVersionMismatch", new Object[] {
            String.valueOf(version), String.valueOf(result.getFileVersion()),
            stok.toString() });

      // 4. set the location
      result.setColor(ParseSupport.parseColor(stok, "Point"));

      // 3. check for "at"
      ParseSupport.parseMandatoryWord(stok, "Point keyword 'at'", "at");
      result.set(ParseSupport.parseNode(stok, "Point coord"));

      // 6. read in the object depth
      if (version >= 2) {
        // Check for keyword "depth"
        ParseSupport.parseMandatoryWord(stok, "Point keyword 'depth'", "depth");

        // Parse the depth
        result.setDepth(ParseSupport.parseInt(stok, "Point depth"));
      }
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return result;
    // return new PTPoint(properties);
  }
}
