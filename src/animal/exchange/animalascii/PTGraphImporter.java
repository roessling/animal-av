/*
 * PTGraphImporter.java
 * Importer for PTStringArrays.
 *
 * Created on 27. September 2006
 *
 * @author Pierre Villette
 */

package animal.exchange.animalascii;

import java.awt.Font;
import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTGraph;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTGraphImporter extends PTGraphicObjectImporter {
  public Object importFrom(int version, StreamTokenizer stok) {
    // XProperties properties = new XProperties ();
    PTGraph graph;
    try {
      graph = new PTGraph();
      // 1. check file versioning
      if (version > graph.getFileVersion()) {
        ParseSupport.formatException2("fileVersionMismatch", new Object[] {
            String.valueOf(version), String.valueOf(graph.getFileVersion()),
            stok.toString() });
      }

      // 2. read in the array size
      ParseSupport.parseMandatoryWord(stok, "StringArray keyword 'size'",
          "size");
      int size = ParseSupport.parseInt(stok, "size", 0);

      // 3. create new PTStringArray
      graph.setSize(size);

      // 4. read in array location ("origin")
      ParseSupport.parseMandatoryWord(stok, "StringArray origin", "origin");
      graph.setOrigin(ParseSupport.parseNode(stok, "location"));

      // 5. read in cell contents
      ParseSupport.parseMandatoryWord(stok, "StringArray keyword 'nodes'",
          "nodes");
      ParseSupport.parseMandatoryChar(stok, "{", '{');
      for (int i = 0; i < size; i++) {
        graph.enterValueNode(i, ParseSupport
            .parseText(stok, "Graph[" + i + "]"));
        graph.getPTTextNode(i).setLocation(
            ParseSupport.parseNode(stok, "Text location " + i));
        graph.getNode(i).setCenter(
            ParseSupport.parseNode(stok, "Node location " + i));
        graph.getNode(i).setRadius(
            ParseSupport.parseInt(stok, "Node radius " + i));
      }
      ParseSupport.parseMandatoryChar(stok, "}", '}');

      ParseSupport.parseMandatoryWord(stok, "StringArray keyword 'edges'",
          "edges");
      ParseSupport.parseMandatoryChar(stok, "{", '{');
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          graph.enterValueEdge(i, j, ParseSupport.parseText(stok, "Graph[" + i
              + "," + j + "]"));
        }
      }
      ParseSupport.parseMandatoryChar(stok, "}", '}');

      // 6. Check for 'nodeFont ('
      ParseSupport.parseMandatoryWord(stok, "StringArray keyword 'nodeFont'",
          "nodeFont");
      ParseSupport.parseMandatoryChar(stok,
          "StringArray keyword node font '('", '(');

      // 6.1. read in the font name
      String fontName = ParseSupport.parseWord(stok, "node font name");

      // 6.2. Check for ',' following the font name
      ParseSupport.parseMandatoryChar(stok,
          "StringArray keyword node font ','", ',');

      // 6.3. read in the font size
      int fontSize = ParseSupport.parseInt(stok, "font size");
      ParseSupport.parseMandatoryChar(stok,
          "StringArray keyword node font ')'", ')');

      // 6.4. create font
      Font font = new Font(fontName, Font.PLAIN, fontSize);

      // 6.5. set array font
      graph.setNodeFont(font);

      // 6. Check for 'edgeFont ('
      ParseSupport.parseMandatoryWord(stok, "StringArray keyword 'edgeFont'",
          "edgeFont");
      ParseSupport.parseMandatoryChar(stok,
          "StringArray keyword edge font '('", '(');

      // 6.1. read in the font name
      fontName = ParseSupport.parseWord(stok, "edge font name");

      // 6.2. Check for ',' following the font name
      ParseSupport.parseMandatoryChar(stok,
          "StringArray keyword edge font ','", ',');

      // 6.3. read in the font size
      fontSize = ParseSupport.parseInt(stok, "font size");
      ParseSupport.parseMandatoryChar(stok,
          "StringArray keyword edge font ')'", ')');

      // 6.4. create font
      font = new Font(fontName, Font.PLAIN, fontSize);

      // 6.5. set array font
      graph.setEdgeFont(font);

      // 6. read in the background color
      graph.setBGColor(ParseSupport.parseColor(stok, "background color",
          "bgColor"));

      // 7. read in the font color
      graph.setNodeFontColor(ParseSupport.parseColor(stok,
          "Graph node font color", "nodeFontColor"));

      // 7. read in the font color
      graph.setEdgeFontColor(ParseSupport.parseColor(stok,
          "Graph edge font color", "edgeFontColor"));

      // 8. read in the outline color
      graph.setOutlineColor(ParseSupport.parseColor(stok,
          "Graph outline color", "outlineColor"));

      // 9. read in the highlight color
      graph.setHighlightColor(ParseSupport.parseColor(stok,
          "Graph highlight color", "highlightColor"));

      // 10. read in the element highlight color
      graph.setElemHighlightColor(ParseSupport.parseColor(stok,
          "Graph element highlight color", "elementHighlightColor"));

      // 11. Parse optional keywords
      graph.setDirection(ParseSupport.parseOptionalWord(stok, "Direction?",
          "direction"));
      graph
          .setWeight(ParseSupport.parseOptionalWord(stok, "Weight?", "weight"));
      graph.setIndices(ParseSupport.parseOptionalWord(stok,
          "Show cell indices?", "showIndices"));

      // 12. Parse the object depth
      ParseSupport.parseMandatoryWord(stok, "StringArray keyword 'depth'",
          "depth");
      graph.setDepth(ParseSupport.parseInt(stok, "StringArray depth", 2));

      // 13. Sets the edges
      graph.setEdges();

      // 14. parse the EOL
      // ParseSupport.consumeIncludingEOL(stok, "EOL of PTGraph");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
      graph = new PTGraph();
    }
    return graph;
  }
}