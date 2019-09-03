package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTText;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animalscript.core.AnimalParseSupport;

public class PTTextImporter extends PTGraphicObjectImporter {
  public Object importFrom(int version, StreamTokenizer stok) {
    PTText shape = new PTText();
    try {
      if (version > shape.getFileVersion())
        ParseSupport.formatException2("fileVersionMismatch", new Object[] {
            String.valueOf(version), String.valueOf(shape.getFileVersion()),
            stok.toString() });

      // 3. check for "at"
      ParseSupport.parseMandatoryWord(stok, "Text Coord 'at'", "at");

      // 4. set the location
      shape.setPosition(ParseSupport.parseNode(stok, "Text coordinates"));

      // 5. set the color
      shape.setColor(ParseSupport.parseColor(stok, "Text Color"));

      // 6. check for "font"
      ParseSupport.parseMandatoryWord(stok, "fontname 'font'", "font");

      // 7. parse the font
      shape.setFont(ParseSupport.parseFontInformation(stok, "font"));

      // 8. check for "text"
      ParseSupport.parseMandatoryWord(stok, "text 'text'", "text");

      // 9. check for ":"
      ParseSupport.parseMandatoryChar(stok, "text ':'", ':');

      // 10. get the text
      shape.setText(AnimalParseSupport.parseText(stok, "Text text"));

      // if fileID >= 2 then parse the objectdepth
      if (version >= 2) {
        // Check for keyword "depth"
        ParseSupport.parseMandatoryWord(stok, "Text keyword 'depth'", "depth");

        // Parse the depth
        shape.setDepth(ParseSupport.parseInt(stok, "Text depth"));
      }
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return shape;
  }
}
