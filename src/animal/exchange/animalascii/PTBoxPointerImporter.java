package animal.exchange.animalascii;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Vector;

import animal.graphics.PTBoxPointer;
import animal.graphics.PTLine;
import animal.graphics.PTPolyline;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animalscript.core.AnimalParseSupport;

public class PTBoxPointerImporter extends PTGraphicObjectImporter {
  public Object importFrom(int version, StreamTokenizer stok) {
    // XProperties properties = new XProperties();
    // PTPolyline[] pointers = null;
    PTBoxPointer shape = new PTBoxPointer();
    shape.init();
    try {
      // 1. Check file versioning
      // PTBoxPointer dummy = new PTBoxPointer();
      if (version > shape.getFileVersion())
        ParseSupport.formatException2("fileVersionMismatch", new Object[] {
            String.valueOf(version), String.valueOf(shape.getFileVersion()),
            stok.toString() });

      // 3. read in the color
      shape.setColor(ParseSupport.parseColor(stok, "BoxPointer"));
      // properties.put(PTBoxPointer.BOXPOINTER_COLOR, ParseSupport.parseColor(
      // stok, "BoxPointer"));

      // 4. read in the box fill color
      Color fillColor = ParseSupport.parseColor(stok, "BoxPointer fill",
          "fillColor");
      // properties.put(PTBoxPointer.BOXPOINTER_FILL_COLOR, ParseSupport
      // .parseColor(stok, "BoxPointer fill", "fillColor"));

      shape.getTextBox().setFillColor(fillColor);

      // parse the pointerAreaColor
      Color pointerAreaColor = shape.getColor();
      if (version >= 5)
        pointerAreaColor = ParseSupport.parseColor(stok, "Pointer area color",
            "pointerAreaColor");
      // properties.put(PTBoxPointer.BOXPOINTER_POINTER_AREA_COLOR, ParseSupport
      // .parseColor(stok, "Pointer area color", "pointerAreaColor"));

      shape.getPointerArea().setFillColor(pointerAreaColor);
      // 5. read in the pointer area fill color keyword
      Color pointerAreaFillColor = ParseSupport.parseColor(stok,
          "BoxPointer area fill", "pointerFillColor");
      // properties.put(PTBoxPointer.BOXPOINTER_POINTER_AREA_FILL_COLOR,
      // ParseSupport.parseColor(stok, "BoxPointer area fill",
      // "pointerFillColor"));
      shape.getPointerArea().setFillColor(pointerAreaFillColor);
      // 6. Read in the 'textBox' keyword
      ParseSupport.parseMandatoryWord(stok, "BoxPointer keyword 'textBox'",
          "textBox");

      // 10. Read in the textbox nodes
      PTPolyline box = AnimalParseSupport.parsePolyline(stok,
          "BoxPointer text box");
      Rectangle bb = box.getBoundingBox();
      // PTRectangle textBox = new PTRectangle(bb.x, bb.y, bb.width, bb.height);
      shape.setLocation(new Point(bb.x, bb.y));
      // textBox = AnimalParseSupport.parsePolyline(stok, "BoxPointer text
      // box");
      // properties.put(PTBoxPointer.BOXPOINTER_LOCATION, textBox.getNode(0)
      // .toPoint());

      // 12. check for keyword 'pointerArea'
      ParseSupport.parseMandatoryWord(stok,
          "pointerArea of BoxPointer expected", "pointerArea");

      // 13. Read in the pointer area nodes
      // TODO Check if this was really useless code!
      // PTPolyline pointerArea =
      AnimalParseSupport.parsePolyline(stok, "BoxPointer pointer area");

      // 14. read in keyword 'pointerPosition'
      ParseSupport.parseMandatoryWord(stok, "BoxPtr keyword 'pointerPosition'",
          "pointerPosition");

      // 15. read in pointer position(POINTER_POSITION_NONE,
      // POINTER_POSITION_LEFT, POINTER_POSITION_RIGHT, POINTER_POSITION_TOP,
      // BOTTOM)
      shape.setPointerPosition(ParseSupport.parseInt(stok,
          "BoxPtr pointer position", PTBoxPointer.POINTER_POSITION_NONE,
          PTBoxPointer.POINTER_POSITION_BOTTOM));
      // properties.put(PTBoxPointer.BOXPOINTER_POINTER_POSITION, ParseSupport
      // .parseInt(stok, "BoxPtr pointer position",
      // PTBoxPointer.POINTER_POSITION_NONE,
      // PTBoxPointer.POINTER_POSITION_BOTTOM));

      // 16. read in keyword 'pointers'
      ParseSupport.parseMandatoryWord(stok, "BoxPtr keyword 'pointers'",
          "pointers");

      // 17. read in the pointer positions(have format: '{' polyline(s)
      // '}')
      ParseSupport.parseMandatoryChar(stok, "BoxPointer pointers open brace",
          '{');
      Vector<PTPolyline> pointers = new Vector<PTPolyline>(10, 5);
      int nrPointers = 0;
      // 18. parse the nodes until a '}' is found
      while (!ParseSupport.parseOptionalChar(stok,
          "BoxPointer pointers close brace", '}')) {
        // 19. parse the node...
        PTPolyline polyline = AnimalParseSupport.parsePolyline(stok,
            "BoxPointer pointer area");
        if (polyline != null) {
          Point node0 = polyline.getNodeAsPoint(0);
          Point node1 = polyline.getNodeAsPoint(1);
          node1.translate(-node0.x, -node0.y);
          pointers.addElement(polyline);
          // properties.put(PTBoxPointer.BOXPOINTER_TIP_POSITION + "_"
          // + nrPointers, node1);
          polyline.setFWArrow(true);
          nrPointers++;
        }
      }

      if (version <= 2) {
        // init(textBox.getNode(2).getX(), textBox.getNode(2).getY());
      }

      shape.setPointerCountWithoutReinitialization(nrPointers);
      for (int pos = 0; pos < pointers.size(); pos++) {
        PTPolyline currentPointer = pointers.elementAt(pos);
        shape.setTip(pos, currentPointer.getNodeAsPoint(1));
      }
      // properties.put(PTBoxPointer.BOXPOINTER_NR_POINTERS, nrPointers);
      ParseSupport.parseMandatoryWord(stok, "BoxPtr keyword text", "text");

      // 22. Check for ':' following 'text'
      ParseSupport.parseMandatoryChar(stok, "BoxPtr keyword text ':'", ':');

      // 23. Read the text till EOL
      shape.getTextComponent().setText(
          AnimalParseSupport.parseText(stok, "BoxPtr text component"));
      // properties.put(PTBoxPointer.BOXPOINTER_TEXT, AnimalParseSupport
      // .parseText(stok, "BoxPtr text component"));
      if (version >= 4) {
        // 24. parse the text color
        shape.getTextComponent().setColor(
            ParseSupport.parseColor(stok, "BoxPtr text color", "textColor"));
        // properties.put(PTBoxPointer.BOXPOINTER_TEXT_COLOR, ParseSupport
        // .parseColor(stok, "BoxPtr text color", "textColor"));
      } else { // FILE_VERSION <= 3
        shape.getTextComponent().setColor(Color.BLACK);
        // properties.put(PTBoxPointer.BOXPOINTER_TEXT_COLOR, properties
        // .getColorProperty(PTBoxPointer.BOXPOINTER_COLOR, Color.black));
      }

      // 25. if fileversion >=5 Parse the object depth and textfont
      if (version >= 5) {
        // Check for keyword Font
        ParseSupport.parseMandatoryWord(stok, "BoxPtr keyword 'Font'", "Font");

        ParseSupport.parseMandatoryChar(stok, "BoxPtr keyword text '{'", '{');

        String fn = AnimalParseSupport.parseText(stok, "BoxPtr FontName");
        // Parse the FontName
        // properties.put(PTBoxPointer.BOXPOINTER_FONT_NAME, fn);

        int fontStyle = ParseSupport.parseInt(stok, "BoxPtr textStyle");
        // Parse the textStyle
        // properties.put(PTBoxPointer.BOXPOINTER_FONT_STYLE, fontStyle);

        // Parse the textSize
        int fontSize = ParseSupport.parseInt(stok, "BoxPtr textSize");
        // properties.put(PTBoxPointer.BOXPOINTER_FONT_SIZE, fontSize);

        // let's test it...
        // properties.put(PTBoxPointer.BOXPOINTER_FONT, new Font(fn, fontStyle,
        // fontSize));
        shape.getTextComponent().setFont(new Font(fn, fontStyle, fontSize));
        ParseSupport.parseMandatoryChar(stok, "BoxPtr keyword text '}'", '}');

        // Check for keyword "depth"
        ParseSupport
            .parseMandatoryWord(stok, "BoxPtr keyword 'depth'", "depth");

        // Parse the depth
        int basicDepth = ParseSupport.parseInt(stok, "BoxPtr depth");
        shape.setDepth(basicDepth);
        shape.getPointerArea().setDepth(basicDepth + 1);
        if (shape.getPointers() != null)
        for (PTLine currentPointer : shape.getPointers())
          if (currentPointer != null)
            currentPointer.setDepth(basicDepth);
        shape.getTextBox().setDepth(basicDepth + 1);
        shape.getTextComponent().setDepth(basicDepth);
        // properties.put(
        // PTBoxPointer.TYPE_LABEL + ".depth",
        // ParseSupport.parseInt(stok, "BoxPtr depth"));
      }
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    // PTBoxPointer ptbp = new PTBoxPointer(properties);
    shape.init();
    return shape;
  }
}
