package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTGraphicObject;
import animal.graphics.meta.FillablePrimitive;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTGraphicObjectImporter implements Importer {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param version
   *          the version of the graphical object to be parsed
   * @param stok
   *          the StreamTokenizer used for parsing the object
   */
  public Object importFrom(int version, StreamTokenizer stok) {
    MessageDisplay.errorMsg("invalidImportCall", new String[] {
        String.valueOf(stok.lineno()), String.valueOf(version),
        getClass().getName() }, MessageDisplay.RUN_ERROR);
    try {
      MessageDisplay.errorMsg("readErrorLine", new String[] { ParseSupport
          .consumeIncludingEOL(stok, "object") }, MessageDisplay.INFO);
    } catch (IOException e) {
      MessageDisplay.errorMsg("ioErrorImporting",
          new String[] { e.getMessage() }, MessageDisplay.RUN_ERROR);
    }
    return null;
  }

  public void parseEndingValuesFrom(StreamTokenizer stok,
      PTGraphicObject shape, String key) {
    try {
      // Check for keyword "depth"
      ParseSupport.parseMandatoryWord(stok, key + " keyword 'depth'", "depth");

      // Parse the depth
      shape.setDepth(ParseSupport.parseInt(stok, key + " depth"));

      if (ParseSupport.parseOptionalWord(stok, key + " keyword [name]", "name"))
        shape.setObjectName(ParseSupport.parseText(stok, "Object name"));

      // 14. parse the EOL
      // ParseSupport.consumeIncludingEOL(stok, "EOL of PTSquare");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
  }

  /**
   * Parse the fill attributes of a potentially filled shape
   * 
   * @param stok
   *          the StreamTokenizer used for parsing the object
   * @param shape
   *          the underlying shape to parse
   * @param key
   *          the key to be displayed in error messages
   */
  public void parseFillAttributes(StreamTokenizer stok,
      FillablePrimitive shape, String key) {
    try {
      // 1. Check for optional keyword 'filled'
      shape.setFilled(ParseSupport.parseOptionalWord(stok, key + " filled",
          "filled"));

      // 2. read in the square fill color
      if (shape.isFilled())
        shape.setFillColor(ParseSupport.parseColor(stok, key, "fillColor"));
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
  }
}
