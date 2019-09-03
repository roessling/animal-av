/*
 * PTStringArrayImporter.java
 * Importer for PTStringArrays.
 *
 * Created on 8. November 2005, 22:38
 *
 * @author Michael Schmitt
 * @version 1.2b
 * @date 2005-12-31
 */

package animal.exchange.animalascii;

import java.awt.Font;
import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTIntArray;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTIntArrayImporter extends PTGraphicObjectImporter {

  public Object importFrom(int version, StreamTokenizer stok) {
    // XProperties properties = new XProperties();
    PTIntArray array;
    try {
      array = new PTIntArray();
      // 1. check file versioning
      if (version > array.getFileVersion()) {
        ParseSupport.formatException2("fileVersionMismatch", new Object[] {
            String.valueOf(version), String.valueOf(array.getFileVersion()),
            stok.toString() });
      }

      // 2. read in the array size
      ParseSupport.parseMandatoryWord(stok, "IntArray keyword 'size'", "size");
      int size = ParseSupport.parseInt(stok, "size", 0);

      // 3. create new PTStringArray
      array = new PTIntArray(size);

      // 4. read in cell contents
      ParseSupport.parseMandatoryWord(stok, "IntArray keyword 'entries'",
          "entries");
      ParseSupport.parseMandatoryChar(stok, "{", '{');
      for (int i = 0; i < size; i++) {
        array.enterValue(i, ParseSupport.parseInt(stok, "IntArray[" + i + "]"));
      }
      ParseSupport.parseMandatoryChar(stok, "}", '}');

      // 5. read in array location ("origin")
      ParseSupport.parseMandatoryWord(stok, "IntArray location", "location");
      array.setOrigin(ParseSupport.parseNode(stok, "location"));

      // 6. read in the background color
      array.setBGColor(ParseSupport.parseColor(stok, "background color",
          "bgColor"));

      // 6. Check for 'font ('
      ParseSupport.parseMandatoryWord(stok, "IntArray keyword 'font'", "font");
      ParseSupport.parseMandatoryChar(stok, "IntArray keyword font '('", '(');

      // 6.1. read in the font name
      String fontName = ParseSupport.parseWord(stok, "font name");

      // 6.2. Check for ',' following the font name
      ParseSupport.parseMandatoryChar(stok, "IntArray keyword font ','", ',');

      // 6.3. read in the font size
      int fontSize = ParseSupport.parseInt(stok, "font size");
      ParseSupport.parseMandatoryChar(stok, "IntArray keyword font ')'", ')');

      // 6.4. create font
      Font font = new Font(fontName, Font.PLAIN, fontSize);

      // 6.5. set array font
      array.setFont(font);

      // 7. read in the font color
      array.setFontColor(ParseSupport.parseColor(stok, "IntArray font color",
          "fontColor"));

      // 8. read in the outline color
      array.setOutlineColor(ParseSupport.parseColor(stok,
          "IntArray outline color", "outlineColor"));

      // 9. read in the highlight color
      array.setHighlightColor(ParseSupport.parseColor(stok,
          "IntArray highlight color", "highlightColor"));

      // 10. read in the element highlight color
      if (version >= 3) {
        array.setElemHighlightColor(ParseSupport.parseColor(stok,
            "IntArray element highlight color", "elementHighlightColor"));
      }

      // 11. Parse optional keyword 'showIndices'
      if (version >= 2) {
        array.showIndices(ParseSupport.parseOptionalWord(stok,
            "Show cell indices?", "showIndices"));
      }

      // 12. Parse the object depth
      ParseSupport
          .parseMandatoryWord(stok, "IntArray keyword 'depth'", "depth");
      array.setDepth(ParseSupport.parseInt(stok, "IntArray depth", 2));

      // 13. parse the EOL
      // ParseSupport.consumeIncludingEOL(stok, "EOL of PTStringArray");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
      array = new PTIntArray();
    }
    return array;
  }
}