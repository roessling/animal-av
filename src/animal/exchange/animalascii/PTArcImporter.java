package animal.exchange.animalascii;

import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.io.StreamTokenizer;

import translator.AnimalTranslator;
import animal.exchange.AnimalASCIIImporter;
import animal.graphics.PTArc;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animalscript.core.AnimalParseSupport;

public class PTArcImporter extends PTGraphicObjectImporter {
  public Object importFrom(int version, StreamTokenizer stok) {
    PTArc arc = new PTArc();
    try {
      // 1. Check file versioning
      if (version > arc.getFileVersion())
        throw new StreamCorruptedException(
            AnimalASCIIImporter.translateMessage("fileVersionMismatch", new Object[] {
            String.valueOf(version), String.valueOf(arc.getFileVersion()),
            stok.toString() }));

      // 3. Check for "center"
      ParseSupport.parseMandatoryWord(stok, AnimalTranslator
          .translateMessage("arcCenter"), "center");

      // 4. Read in center coordinates
      arc.setCenter(ParseSupport.parseNode(stok, AnimalTranslator
          .translateMessage("arcCenter")));

      // 5. read in x radius keyword 'rX'
      ParseSupport.parseMandatoryWord(stok, AnimalASCIIImporter
          .translateMessage("otKw", new String[] { "Arc", "rX"}), "rX");

      // 6. read in x radius
      int x = ParseSupport.parseInt(stok, AnimalASCIIImporter
          .translateMessage("arcRadiusXY", "x"));

      // 7. read in y radius keyword 'rY'
      ParseSupport.parseMandatoryWord(stok, AnimalASCIIImporter
          .translateMessage("otKw", new String[] { "Move", "rY"}), "rY");

      int y = ParseSupport.parseInt(stok, AnimalASCIIImporter.translateMessage("arcRadiusXY", "y"));

      // 8. read in y radius
      arc.setRadius(new Point(x, y));

      // 9. read in the color
      arc.setColor(ParseSupport.parseColor(stok, "Arc"));

      // if fileversion >=5 Parse the Fillcolor
      if (version >= 5
          && ParseSupport.parseOptionalWord(stok, "Arc fillColor", "fillColor")) {
        stok.pushBack();
        arc.setFillColor(ParseSupport.parseColor(stok, "Arc fillColor",
            "fillColor"));
      }

      // 10. Read in "angle"
      ParseSupport.parseMandatoryWord(stok, AnimalASCIIImporter
          .translateMessage("otKw", new String[] { "Arc", "angle"}), "angle");

      // 11. Read in angle value
      arc.setTotalAngle(ParseSupport.parseInt(stok, "Arc angle value"));

      // 12. read in start angle
      ParseSupport.parseMandatoryWord(stok, AnimalASCIIImporter
          .translateMessage("otKw", new String[] { "Move", "starts"}), "starts");

      // 13. Read in angle value
      arc.setStartAngle(ParseSupport.parseInt(stok, "Arc angle start"));

      // 14. Check for optional keyword 'filled'
      arc.setFilled(ParseSupport
          .parseOptionalWord(stok, "Arc filled", "filled"));

      // 15. Check for optional keyword 'circle'
      arc.setCircle(ParseSupport
          .parseOptionalWord(stok, "Arc circle", "circle"));

      // 16. Check for optional keyword 'clockwise'
      arc.setClockwise(ParseSupport.parseOptionalWord(stok, "Arc clockwise",
          "clockwise"));

      // 17. Check for optional keyword 'fwArrow'
      arc.setFWArrow(ParseSupport.parseOptionalWord(stok, "Arc fwArrow",
          "fwArrow"));

      // 18. Check for optional keyword 'bwArrow'
      arc.setBWArrow(ParseSupport.parseOptionalWord(stok, "Arc bwArrow",
          "bwArrow"));

      // 19. Check for optional keyword 'closed'
      arc.setClosed(ParseSupport
          .parseOptionalWord(stok, "Arc closed", "closed"));

      // 20. Check for keyword 'text'
      ParseSupport.parseMandatoryWord(stok, "Arc keyword 'text'", "text");

      // 21. Check for ':' following 'text'
      ParseSupport.parseMandatoryChar(stok, "Arc keyword text ':'", ':');

      // 22. Read the text till EOL#
      arc.setText(AnimalParseSupport.parseText(stok, "Arc text component"));

      // 23. Parse the textColor
      arc.setTextColor(ParseSupport.parseColor(stok, "Arc textColor",
          "textColor"));

      // if fileversion >=5 Parse the Font
      if (version >= 5
          && ParseSupport.parseOptionalWord(stok, "Arc Font into", "Font")) {
        stok.pushBack();
        // Check for keyword Font
        ParseSupport.parseMandatoryWord(stok, "Arc keyword 'Font'", "Font");

        ParseSupport.parseMandatoryChar(stok, "Arc keyword text '{'", '{');

        // Parse the FontName
        String fontName = AnimalParseSupport.parseText(stok, "Arc FontName");

        // Parse the textStyle
        int fontStyle = ParseSupport.parseInt(stok, "Arc textStyle");

        // Parse the textSize
        int fontSize = ParseSupport.parseInt(stok, "Arc textSize");
        arc.getTextComponent().setFont(new Font(fontName, fontStyle, fontSize));

        ParseSupport.parseMandatoryChar(stok, "Arc keyword text '}'", '}');
      }
      // 24. if file version >= 4, parse the object depth
      if (version >= 4
          && ParseSupport.parseOptionalWord(stok, "Arc Depth into", "depth")) {
        stok.pushBack();

        // Check for keyword "depth"
        ParseSupport.parseMandatoryWord(stok, "Arc keyword 'depth'", "depth");

        // Parse the depth
        arc.setDepth(ParseSupport.parseInt(stok, "Arc depth"));
      }
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return arc;
  }
}
