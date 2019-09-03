/*
 * PTDoubleArrayImporter.java
 * Importer for PTDoubleArrays.
 *
 * Created on November 7, 2008
 *
 * @author Guido Roessling <roessling@acm.org>
 * @version 1.2b
 * @date 2008-11-07
 */

package animal.exchange.animalascii;

import java.awt.Font;
import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTDoubleArray;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTDoubleArrayImporter extends PTGraphicObjectImporter {

  public Object importFrom(int version, StreamTokenizer stok) {
    PTDoubleArray array;
    try {
      array = new PTDoubleArray();
      // 1. check file versioning
      if (version > array.getFileVersion()) {
        ParseSupport.formatException2("fileVersionMismatch", new Object[] {
            String.valueOf(version), String.valueOf(array.getFileVersion()),
            stok.toString() });
      }

      // 2. read in the array size
      ParseSupport.parseMandatoryWord(stok, "DoubleArray keyword 'size'", "size");
      int size = ParseSupport.parseInt(stok, "size", 0);

      // 3. create new PTStringArray
      array = new PTDoubleArray(size);

      // 4. read in cell contents
      ParseSupport.parseMandatoryWord(stok, "DoubleArray keyword 'entries'",
          "entries");
      ParseSupport.parseMandatoryChar(stok, "{", '{');
      for (int i = 0; i < size; i++) {
        array.enterValue(i, ParseSupport.parseDouble(stok, "DoubleArray[" + i + "]"));
      }
      ParseSupport.parseMandatoryChar(stok, "}", '}');

      // 5. read in array location ("origin")
      ParseSupport.parseMandatoryWord(stok, "DoubleArray location", "location");
      array.setOrigin(ParseSupport.parseNode(stok, "location"));

      // 6. read in the background color
      array.setBGColor(ParseSupport.parseColor(stok, "background color",
          "bgColor"));

      // 6. Check for 'font ('
      ParseSupport.parseMandatoryWord(stok, "DoubleArray keyword 'font'", "font");
      ParseSupport.parseMandatoryChar(stok, "DoubleArray keyword font '('", '(');

      // 6.1. read in the font name
      String fontName = ParseSupport.parseWord(stok, "font name");

      // 6.2. Check for ',' following the font name
      ParseSupport.parseMandatoryChar(stok, "DoubleArray keyword font ','", ',');

      // 6.3. read in the font size
      int fontSize = ParseSupport.parseInt(stok, "font size");
      ParseSupport.parseMandatoryChar(stok, "DoubleArray keyword font ')'", ')');

      // 6.4. create font
      Font font = new Font(fontName, Font.PLAIN, fontSize);

      // 6.5. set array font
      array.setFont(font);

      // 7. read in the font color
      array.setFontColor(ParseSupport.parseColor(stok, "DoubleArray font color",
          "fontColor"));

      // 8. read in the outline color
      array.setOutlineColor(ParseSupport.parseColor(stok,
          "DoubleArray outline color", "outlineColor"));

      // 9. read in the highlight color
      array.setHighlightColor(ParseSupport.parseColor(stok,
          "DoubleArray highlight color", "highlightColor"));

      // 10. read in the element highlight color
      if (version >= 3) {
        array.setElemHighlightColor(ParseSupport.parseColor(stok,
            "DoubleArray element highlight color", "elementHighlightColor"));
      }

      // 11. Parse optional keyword 'showIndices'
      if (version >= 2) {
        array.showIndices(ParseSupport.parseOptionalWord(stok,
            "Show cell indices?", "showIndices"));
      }

      // 12. Parse the object depth
      ParseSupport
          .parseMandatoryWord(stok, "DoubleArray keyword 'depth'", "depth");
      array.setDepth(ParseSupport.parseInt(stok, "DoubleArray depth", 2));

      // 13. parse the EOL
      // ParseSupport.consumeIncludingEOL(stok, "EOL of PTStringArray");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
      array = new PTDoubleArray();
    }
    return array;
  }
}