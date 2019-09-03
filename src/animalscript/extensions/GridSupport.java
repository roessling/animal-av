/**
 * 
 */
package animalscript.extensions;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Hashtable;
import java.util.regex.Pattern;

import animal.animator.ColorChanger;
import animal.animator.SetFont;
import animal.animator.SetText;
import animal.animator.TimedShow;
import animal.graphics.PTStringMatrix;
import animal.graphics.meta.PTMatrix;
import animal.graphics.meta.PTMatrix.Alignment;
import animal.misc.ColorChoice;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animalscript.core.AnimalParseSupport;
import animalscript.core.AnimalScriptInterface;
import animalscript.core.BasicParser;

/**
 * This class provides an import filter for AnimalScript Grid commands. It
 * should work transparently, so it just parses and executes the GridProducer
 * with the parsed values. It does not fill any default values or checks
 * semantics of the parsed values. Those are tasks of the GridProducer. Values
 * not specified by the parsed script are set <code>null</code> resp
 * <code>0</code>, so the GridProducer can set them to the defaults. In this
 * way, all default values, layouts and semantic behaviours are defined in
 * GridProducer.
 * 
 * @author <a href="mailto:here@christoph-preisser.de">Christoph
 *         Prei&szlig;er</a>
 * @version 1.00 2007-01-25
 */
public class GridSupport extends BasicParser implements AnimalScriptInterface {

  // First sth complicated: a cell (or a line/column) is identified by this
  // syntax:
  // <name>[<line>][<column>]
  // to avoid crashes caused by invalid parameters we'll
  // match with a regular expression:
  // The first letter has to be out of 'a' to 'z', 'A' to 'Z', '_' or '@'
  // There may follow more of that kind or numbers
  // and at last there has to be a pair of brackets with
  // none or more digits in it:
  static final Pattern cellIdentifierPattern = Pattern.compile(
      "[a-zA-Z@_][a-zA-Z0-9@_]*\\s*\\[\\s*\\d*\\s*\\]\\s*\\[\\s*\\d*\\s*\\]");

  /**
   * Modification of parseAndSetColor. Check for a optional color component for
   * the current object passed. If available, store the color given in the
   * appropriate XProperty. If no color is present, null is returned.
   * 
   * @param aStok
   *          StreamTokenizer
   * @param objectType
   *          the type of the object(for exceptions)
   * @param propertyName
   *          the property name for storing the color.
   * @return Parsed Color or null if nothing is parsed
   * @throws IOException
   */
  public static Color parseAndSetColorOrDefault(StreamTokenizer aStok,
      String objectType, String propertyName, Color defaultColor)
      throws IOException {
    // parse optional color: is there such a definition in the stream?
    if (ParseSupport.parseOptionalWord(aStok,
        propertyName + " tag for " + objectType, propertyName)) {
      int token = aStok.nextToken();
      aStok.pushBack();
      String colorName = null;
      if (token == StreamTokenizer.TT_WORD) { // named color
        // parse the color name from the stream
        colorName = ParseSupport.parseWord(aStok,
            objectType + ' ' + propertyName);

        if (colorName.equalsIgnoreCase("light"))
          colorName = colorName + " "
              + ParseSupport.parseWord(aStok, objectType + ' ' + propertyName);
        // check whether the name is valid
        if (colorName == null || !ColorChoice.validColorName(colorName)) {
          colorName = null; // invalid - set to null, so Producer will default
                            // it
          return null; // nothing more to do here...
        }
      } else { // != TT_WORD but maybe another kind of color definition
        Color targetColor = ParseSupport.parseColorRaw(aStok, propertyName);
        if (targetColor != null)
          colorName = ColorChoice.getColorName(targetColor);
      }
      // return the color
      return ColorChoice.getColor(colorName);
    }
    return defaultColor;
  }

  /**
   * Constructor. Handled Keywords are set here.
   */
  public GridSupport() {
    handledKeywords = new Hashtable<String, Object>(); // Keywords and their
                                                       // corresponding parse
                                                       // methods
    handledKeywords.put("grid", "parseGridInput");
    // grid <id> <nodeDefinition> [lines <n>] [columns <n>]
    // [style = (plain|matrix|table|junctions)]
    // [cellwidth <n>] [maxcellwidth <n>]
    // [fixedcellsize]
    // [color <color>] [textcolor <color>] [fillcolor <color>]
    // [highlighttextcolor <color>] [highlightbackcolor <color>]
    // [matrixstyle|tablestyle|junctions]
    // handledKeywords.put("highlightgridcell")

    handledKeywords.put("setgridvalue", "parseSetGridValue");
    // setgridvalue <id>[<line>][<column>] <value>

    handledKeywords.put("setgridfont", "parseSetGridFont");
    // setgridvalue <id>[<line>][<column>] <value>

    handledKeywords.put("setgridcolor", "parseSetGridColor");
    // setgridcolor <id>[<line>][<column>] [color <color>] [textcolor <color>]
    // [fillcolor <color>] [highlighttextcolor <color>] [highlightbackcolor
    // <color>]

    handledKeywords.put("swapgridvalues", "parseSwapGridValues");
    // swapgridvalues "<id1>[<line1>][<column1>]" and
    // "<id2>[<line2>][<column2>]"

    handledKeywords.put("highlightgridcell", "parseHighlightGridCell");
    // highlightgridcell "<id>[<line>][<column>]" <boolean>

    handledKeywords.put("highlightgridelem", "parseHighlightGridElem");
    // highlightgridelem "<id>[<line>][<column>]" <boolean>

    handledKeywords.put("unhighlightgridcell", "parseUnhighlightGridCell");
    // unhighlightgridcell "<id>[<line>][<column>]" <boolean>

    handledKeywords.put("unhighlightgridelem", "parseUnhighlightGridElem");
    // unhighlightgridelem "<id>[<line>][<column>]" <boolean>

    handledKeywords.put("aligngridvalue", "parseAlignGridValue");
    // aligngridvalue <id>[<line>][<column>] "left"|"right"|"center"

  }

  // ===================================================================
  // Helper Methods
  // ===================================================================

  /**
   * Checks a String if it matches a pattern to validate its syntax.<br>
   * Pattern (java escaping):
   * "<code>[a-zA-Z@_][a-zA-Z0-9_@]*\\s*\\[\\s*\\d*\\s*\\]\\s*\\[\\s*\\d*\\s*\\]</code>"<br>
   * Examples: <code>gridname[1 ][12]</code>, <code>gr1dn4me [] [1]</code>,
   * <code>gridname[0][]</code>
   * 
   * @param cellIdentifier
   * @return Validity of the cellIdentifier
   */
  private boolean validateCellIdentifier(String cellIdentifier) {
    if (cellIdentifierPattern.matcher(cellIdentifier).matches())
      return true;
    return false;
  }

  /**
   * Takes a valid cellIdentifier and returnes the name of the identified
   * grid.<br>
   * Example: For <code>gridName[1][0]</code> as cellIdentifier,
   * <code>gridName</code> is returned.
   * 
   * @param cellIdentifier
   *          Valid cellIdentifier (see validateCellIdentifier(...))
   * @return Name of the grid (substring of the cellIdentifier)
   */
  private String extractNameFromCellIdentifier(String cellIdentifier) {
    // if pattern correct, we can extract the gridname
    // by splitting away '[<line>][<col>]':
    return cellIdentifier
        .split("\\s*\\[\\s*\\d*\\s*\\]\\s*\\[\\s*\\d*\\s*\\]")[0];
  }

  /**
   * Takes a valid cellIdentifier and returnes the coordinates of the identified
   * cell.<br>
   * Example: For <code>gridName[1][0]</code> as cellIdentifier,
   * <code>int[] {1,0}</code> is returned.
   * 
   * @param cellIdentifier
   *          Valid cellIdentifier (see validateCellIdentifier(...))
   * @return Coordinates as int[2]
   */
  private int[] extractLocationFromCellIdentifier(String cellIdentifier) {
    // split away the gridname from gridname[a][b]:
    String[] coordinates = cellIdentifier.split("[a-zA-Z@_][a-zA-Z0-9@_]*");
    // now: coordinates == { ,"[<line>][<col>]"}
    // split '][' or '[' or ']' but never numbers:
    coordinates = coordinates[1].split("[\\]\\[]"); // ("[^\\d*]");
    // the result is complicated:
    // input output
    // [12 ][34] { ,"12 ", ,"34"} //whitespaces are still in the string ->
    // trim()
    // [ ][ 34] { , , ," 34"}
    // [12][ ] { ,"12"} just 2 elements if col is not set
    // [ ][ ] {} empty array if none is set
    int[] cell = { GridProducer.DEFAULT_INT, GridProducer.DEFAULT_INT }; // -1
                                                                         // means
                                                                         // "unset",
                                                                         // so a
                                                                         // whole
                                                                         // line/column
                                                                         // is
                                                                         // selected
    if (coordinates.length > 1) // see explanation above
      if (!coordinates[1].trim().equals(""))
        cell[0] = Integer.parseInt(coordinates[1].trim());
    if (coordinates.length > 3) // see explanation above
      if (!coordinates[3].trim().equals(""))
        cell[1] = Integer.parseInt(coordinates[3].trim());
    // System.out.println("Coords:("+cell[0]+"/"+cell[1]+")");

    return cell;
  }

  // -------- Methods from AnimalScriptInterface -------
  /*
   * (non-Javadoc)
   * 
   * @see
   * animalscript.core.AnimalScriptInterface#generateNewStep(java.lang.String)
   */
  public boolean generateNewStep(String currentCommand) {
    return !sameStep; // aus ArraySupport abgeschaut
    // return false;
  }

  // ===================================================================
  // AnimalScript parse methods
  // ===================================================================

  /**
   * Parses Animalscript commands for creating grids from the description read
   * from the StreamTokenizer. Parsed Information are used to call
   * GridProducer.makeGrid(...)<br>
   * AnimalscriptSyntax for creating grids:<br>
   * <code> grid &lt;oid&gt; &lt;nodeDefinition&gt; lines &lt;n&gt; columns &lt;n&gt;<br>
   * [style (plain|matrix|table)] [cellwidth &lt;n&gt;] [maxcellwidth &lt;n&gt;]
   * [fixedcellsize]<br>[color &lt;color&gt;] [textcolor &lt;color&gt;]
   * [fillcolor &lt;color&gt;] [bordercolor &lt;color&gt;]<br> 
   * [highlighttextcolor &lt;color&gt;] [highlightfillcolor &lt;color&gt;]
   * [highlightbordercolor &lt;color&gt;]</code>
   */
  public void parseGridInput() throws IOException {
    // grid <oid> <nodeDefinition> lines <n> columns <n>
    // [style (plain|matrix|table)]
    // [cellwidth <n>] [maxcellwidth <n>]
    // [fixedcellsize]
    // [color <color>] [textcolor <color>] [fillcolor <color>] [bordercolor
    // <color>]
    // [highlighttextcolor <color>] [highlightfillcolor <color>]
    // [highlightbordercolor <color>]
    String gridName; // OID of the Grid
    Point location; // Upper left of the grid
    int nLines, nCols; // Number of lines an columns of the grid
    // optional attributes
    int cellWidth = GridProducer.DEFAULT_INT; // Initial (minimum) width of the
                                              // grid cells
    int maxCellWidth = GridProducer.DEFAULT_INT; // Maximum width of the grid
                                                 // cells
    int cellHeight = GridProducer.DEFAULT_INT;
    int maxCellHeight = GridProducer.DEFAULT_INT;
    boolean fixedCellSize = false; // If set, the cells never differ from their
    // initial size (maxvalue is ignored then)
    Color genericColor = GridProducer.DEFAULT_COLOR; // Generic color
                                                     // attribute, assigned by
    // "color" in Animalscript
    Color borderColor = GridProducer.DEFAULT_COLOR; // Color of cell borders
    Color textColor = GridProducer.DEFAULT_COLOR; // Color of the cell content
    Color fillColor = GridProducer.DEFAULT_COLOR; // Color, the cells are filled
                                                  // with (backgrnd)
    Color hTextColor = GridProducer.DEFAULT_COLOR; // Color of content in
                                                   // highlighted cells
    Color hFillColor = GridProducer.DEFAULT_COLOR; // Color of background in
                                                   // highlighted cells
    Color hBorderColor = GridProducer.DEFAULT_COLOR; // Color of highlighted
                                                     // grid lines
    Font font;
    Alignment alignment = Alignment.LEFT;
    int style = GridProducer.DEFAULT_INT;
    int depth = GridProducer.DEFAULT_INT;
    int delay = GridProducer.DEFAULT_INT;
    int duration = GridProducer.DEFAULT_INT;
    String unit = GridProducer.DEFAULT_UNIT;

    // first word - should be "grid"
    String first = ParseSupport.parseWord(stok, "type").toLowerCase();

    // read in OID (object name)
    gridName = AnimalParseSupport.parseText(stok, "Grid object name");

    // a helper to prevent too many String operations
    String basicFeedbackTag = first + " '" + gridName + "' ";

    // read in location (upper left corner of the grid)
    location = AnimalParseSupport.parseNodeInfo(stok, "location", null);

    // read in the number of lines ("lines <n>")
    ParseSupport.parseMandatoryWord(stok, basicFeedbackTag + "Keyword 'lines'",
        "lines");
    nLines = ParseSupport.parseInt(stok, basicFeedbackTag + "#lines", 1);

    // read in the number of columns ("columns <n>")
    ParseSupport.parseMandatoryWord(stok,
        basicFeedbackTag + "Keyword 'columns'", "columns");
    nCols = ParseSupport.parseInt(stok, basicFeedbackTag + "#columns", 1);

    // Now the optional attributes. The first on is "style", because it has the
    // most influence on the
    // appearance of the grid ("matrix", "table", "junctions" or even just the
    // "plain" text) and can
    // be overwritten by following specifications
    // plain = 0, matrix = 1, table = 2, junctions = 3

    if (ParseSupport.parseOptionalWord(stok,
        basicFeedbackTag + "opt. Keyword 'style'", "style")) {
      String parsedStyle = ParseSupport.parseWord(stok,
          basicFeedbackTag + "'style'");
      if (parsedStyle.equalsIgnoreCase("plain"))
        style = GridProducer.STYLE_PLAIN;
      else if (parsedStyle.equalsIgnoreCase("matrix"))
        style = GridProducer.STYLE_MATRIX;
      else if (parsedStyle.equalsIgnoreCase("table"))
        style = GridProducer.STYLE_TABLE;
      else
        MessageDisplay.errorMsg(
            "ParseError line " + stok.lineno()
                + ": Invalid value for attribute 'style':" + parsedStyle
                + "\n Valid styles are 'plain', 'matrix' or 'table'",
            MessageDisplay.RUN_ERROR);
      // System.err.println("Error: invalid value for attribute 'style':" +
      // parsedStyle);
    }
    if (ParseSupport.parseOptionalWord(stok,
        basicFeedbackTag + "opt. Keyword 'cellwidth'", "cellwidth"))
      cellWidth = ParseSupport.parseInt(stok, basicFeedbackTag + "'cellWidth'",
          1);

    // read in the maximum allowed width of a cell (0 means infinite)
    if (ParseSupport.parseOptionalWord(stok,
        basicFeedbackTag + "opt. Keyword 'maxcellwidth'", "maxcellwidth"))
      maxCellWidth = ParseSupport.parseInt(stok,
          basicFeedbackTag + "'maxcellwidth'", 0);

    if (ParseSupport.parseOptionalWord(stok,
        basicFeedbackTag + "opt. Keyword 'cellheight'", "cellheight"))
      cellHeight = ParseSupport.parseInt(stok,
          basicFeedbackTag + "'cellHeight'", 1);

    // read in the maximum allowed width of a cell (0 means infinite)
    if (ParseSupport.parseOptionalWord(stok,
        basicFeedbackTag + "opt. Keyword 'maxcellheight'", "maxcellheight"))
      maxCellHeight = ParseSupport.parseInt(stok,
          basicFeedbackTag + "'maxcellheight'", 0);

    // read in, whether the cellsizes are fixed. is equal to (cellWidth ==
    // maxCellWidth)
    if (ParseSupport.parseOptionalWord(stok,
        basicFeedbackTag + "opt. Flag 'fixedcellsize'", "fixedcellsize"))
      fixedCellSize = true;
    // todo cellheight and maxcellheight

    // parse and set the colors
    genericColor = parseAndSetColorOrDefault(stok, gridName, "color",
        GridProducer.DEFAULT_COLOR);

    textColor = parseAndSetColorOrDefault(stok, gridName, "elementColor",
        GridProducer.DEFAULT_COLOR);

    fillColor = parseAndSetColorOrDefault(stok, gridName, "fillcolor",
        GridProducer.DEFAULT_COLOR);
    if (fillColor != null) {
      fillColor = fillColor.equals(Color.BLACK) ? Color.WHITE : fillColor;
    }

    borderColor = parseAndSetColorOrDefault(stok, gridName, "bordercolor",
        GridProducer.DEFAULT_COLOR);

    hTextColor = parseAndSetColorOrDefault(stok, gridName, "highlightTextColor",
        GridProducer.DEFAULT_COLOR);

    hFillColor = parseAndSetColorOrDefault(stok, gridName, "highlightFillColor",
        GridProducer.DEFAULT_COLOR);
    if (hFillColor != null) {
      hFillColor = hFillColor.equals(Color.BLACK) ? Color.YELLOW : hFillColor;
    }

    hBorderColor = parseAndSetColorOrDefault(stok, gridName,
        "highlightBorderColor", GridProducer.DEFAULT_COLOR);

    font = AnimalParseSupport.parseFontInfo(stok, "opt. Keyword font");
    if (ParseSupport.parseOptionalWord(stok, "opt. Keyword 'alignment'",
        "align")) {
      String alignmentValue = ParseSupport.parseWord(stok, "Alignmentvalue");
      if (alignmentValue.equalsIgnoreCase("left"))
        alignment = Alignment.LEFT;
      else if (alignmentValue.equalsIgnoreCase("right"))
        alignment = Alignment.RIGHT;
      else if (alignmentValue.equalsIgnoreCase("center"))
        alignment = Alignment.CENTER;
      else {
        MessageDisplay.errorMsg(
            "ParseError Invalid argument for alignment: " + alignment
                + "\n  should be 'left', 'center' or 'right'",
            MessageDisplay.RUN_ERROR);
        System.err.println("Invalid alignment: " + alignment
            + "\n  should be 'left', 'center' or 'right'");
        return;
      }
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'depth'", "depth")) {
      depth = ParseSupport.parseInt(stok, "depth value", 1);
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
      delay = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    // duration has no effect, but is parsed for eventually further development
    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
      duration = ParseSupport.parseInt(stok, "duration value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    // give parsed data to gridproducer
    // System.out.println(gridName+","+ location + ","+ nLines + ","+ nCols+","+
    // cellWidth+","+ maxCellWidth+","+ cellHeight+","+ maxCellHeight+","+
    // fixedCellSize+","+ genericColor+","+ textColor+","+ fillColor+","+
    // borderColor+","+
    // hTextColor+","+hFillColor+","+ hBorderColor+","+
    // style+","+
    // font+","+ alignment+","+
    // depth+","+ delay+","+ duration+","+ unit);
    // Matrix
    PTMatrix matrix = new PTStringMatrix(nLines, nCols);
    matrix.setStyle(style);
    matrix.setColor(genericColor);
    matrix.setPosition(location);
    matrix.setFont(font);
    matrix.setFillColor(fillColor);
    matrix.setHighlightColor(hFillColor);
    matrix.setElemHighlightColor(hTextColor);
    matrix.setTextColor(textColor);
    matrix.setBorderColor(borderColor);
    matrix.setBorderHighlightColor(hBorderColor);
    matrix.setDepth(depth);
    matrix.setTextAlignment(alignment);
    BasicParser.addGraphicObject(matrix, anim);
    getObjectIDs().put(gridName, matrix.getNum(true));
    TimedShow ts = new TimedShow(currentStep, matrix.getNum(true), 0, "show",
        true);
    // insert the animator
    BasicParser.addAnimatorToAnimation(ts, anim);
    // GridProducer.makeGrid(gridName, location, nLines, nCols, cellWidth,
    // maxCellWidth, cellHeight, maxCellHeight, fixedCellSize, genericColor,
    // textColor, fillColor, borderColor, hTextColor, hFillColor,
    // hBorderColor, style, font, alignment, depth, delay, duration, unit);
  } // end parseGridInput()

  /**
   * Parses a command to set a cell, line, column or the whole grid to a given
   * value.<br>
   * The commands syntax is
   * <code>setgridvalue &lt;gridname&gt;"["&lt;line&gt;"]["&lt;column&gt;"]"
   * &lt;value&gt; [refresh] [after &lt;n&gt;] [within &lt;n&gt;]</code>
   * 
   * @throws IOException
   */
  public void parseSetGridValue() throws IOException {
    // setgridvalue gridname[line][column] value
    int duration = 0;
    int offset = 0;
    boolean refresh = false;
    String unit = "ticks";

    // first word - should be "setgridvalue"
    // String first =
    ParseSupport.parseWord(stok, "type").toLowerCase();

    // read in OID (object name) with Location of the cell
    String gridCell = ParseSupport.parseText(stok, "GridCell");

    String gridCellValue = AnimalParseSupport.parseText(stok, "GridCellValue");

    if (ParseSupport.parseOptionalWord(stok, "keyword 'refresh'", "refresh")) {
      refresh = true;
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
      offset = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
      duration = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (validateCellIdentifier(gridCell)) {
      String gridName = extractNameFromCellIdentifier(gridCell);
      int[] cell = extractLocationFromCellIdentifier(gridCell);

      // Matrix
      PTStringMatrix matrix = (PTStringMatrix) animState
          .getCloneByNum(getObjectIDs().getIntProperty(gridName));
      SetText setter = new SetText(currentStep, matrix.getNum(true), 0, 0,
          "ticks", cell[0] + " " + cell[1] + " " + gridCellValue);
      setter.setMethod("setGridValue");
      matrix.setTextDataAt(cell[0], cell[1], gridCellValue);
      BasicParser.addAnimatorToAnimation(setter, anim);
      // GridProducer.setGridValue(gridName, cell[0], cell[1], gridCellValue,
      // currentStep, duration, offset, unit, refresh);
    } // end if valid cellIdentifier
    else {
      MessageDisplay.errorMsg(
          "ParseError line " + stok.lineno()
              + ": setGridValue, Invalid Gridcell: " + gridCell
              + "\n  should be like \"gridcell[1][1]\"",
          MessageDisplay.RUN_ERROR);
    }
  } // end parseSetGridValue()

  /**
   * Parses a command to set colors of a cell, line, column or the whole grid to
   * given values.<br>
   * The commands syntax is
   * <code>setgridcolor &lt;gridname&gt;"["&lt;line&gt;"]["&lt;column&gt;"]"
   * [color &lt;color&gt;] [textcolor &lt;color&gt;]
   * [fillcolor &lt;color&gt;] [bordercolor &lt;color&gt;]<br> 
   * [highlighttextcolor &lt;color&gt;] [highlightfillcolor &lt;color&gt;]
   * [highlightbordercolor &lt;color&gt;]
   * <br> [after &lt;n&gt;] [within &lt;n&gt;]</code>
   * 
   * @throws IOException
   */
  public void parseSetGridColor() throws IOException {
    // setgridcolor gridname[line][column] <attribute> <color> ...
    int duration = 0;
    int offset = 0;
    String unit = "ticks";

    Color genericColor = GridProducer.DEFAULT_COLOR; // Generic color
                                                     // attribute, assigned by
                                                     // "color" in Animalscript
    Color borderColor = GridProducer.DEFAULT_COLOR; // Color of cell borders
    Color textColor = GridProducer.DEFAULT_COLOR; // Color of the cell content
    Color fillColor = GridProducer.DEFAULT_COLOR; // Color, the cells are filled
                                                  // with (background)
    Color highlightTextColor = GridProducer.DEFAULT_COLOR; // Color of content
                                                           // in highlighted
                                                           // cells
    Color highlightFillColor = GridProducer.DEFAULT_COLOR; // Color of
                                                           // background in
                                                           // highlighted cells
    Color highlightBorderColor = GridProducer.DEFAULT_COLOR; // Color of
                                                             // highlighted
                                                             // matrix
                                                             // brackets, grid
                                                             // lines, ...

    // first word - should be "setgridcolor"
    // String first =
    ParseSupport.parseWord(stok, "type").toLowerCase();

    // read in OID (object name) with Location of the cell
    String gridCell = ParseSupport.parseText(stok, "GridCell");

    // parse and set the colors
    genericColor = parseAndSetColorOrDefault(stok, gridCell, "color",
        GridProducer.DEFAULT_COLOR);

    textColor = parseAndSetColorOrDefault(stok, gridCell, "textcolor",
        GridProducer.DEFAULT_COLOR);

    fillColor = parseAndSetColorOrDefault(stok, gridCell, "fillcolor",
        GridProducer.DEFAULT_COLOR);

    borderColor = parseAndSetColorOrDefault(stok, gridCell, "bordercolor",
        GridProducer.DEFAULT_COLOR);

    highlightTextColor = parseAndSetColorOrDefault(stok, gridCell,
        "highlightTextColor", GridProducer.DEFAULT_COLOR);

    highlightFillColor = parseAndSetColorOrDefault(stok, gridCell,
        "highlightFillColor", GridProducer.DEFAULT_COLOR);

    highlightBorderColor = parseAndSetColorOrDefault(stok, gridCell,
        "highlightBorderColor", GridProducer.DEFAULT_COLOR);

    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
      offset = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
      duration = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (validateCellIdentifier(gridCell)) {
      String gridName = extractNameFromCellIdentifier(gridCell);
      int[] cell = extractLocationFromCellIdentifier(gridCell);
      PTStringMatrix matrix = (PTStringMatrix) animState
          .getCloneByNum(getObjectIDs().getIntProperty(gridName));
      if (genericColor != null) {
        ColorChanger colChanger = new ColorChanger(currentStep,
            matrix.getNum(true), 0, "setColor" + " " + cell[0] + " " + cell[1],
            genericColor);
        BasicParser.addAnimatorToAnimation(colChanger, anim);
        matrix.setColor(colChanger.getColor());
      }
      if (textColor != null) {
        ColorChanger colChanger = new ColorChanger(currentStep,
            matrix.getNum(true), 0,
            "setTextColor" + " " + cell[0] + " " + cell[1], textColor);
        BasicParser.addAnimatorToAnimation(colChanger, anim);
        matrix.setTextColor(cell[0], cell[1], colChanger.getColor());
      }
      if (fillColor != null) {
        ColorChanger colChanger = new ColorChanger(currentStep,
            matrix.getNum(true), 0,
            "setFillColor" + " " + cell[0] + " " + cell[1], fillColor);
        BasicParser.addAnimatorToAnimation(colChanger, anim);
        matrix.setFillColor(cell[0], cell[1], colChanger.getColor());
      }
      if (borderColor != null) {
        ColorChanger colChanger = new ColorChanger(currentStep,
            matrix.getNum(true), 0,
            "setBorderColor" + " " + cell[0] + " " + cell[1], borderColor);
        BasicParser.addAnimatorToAnimation(colChanger, anim);
        matrix.setBorderColor(cell[0], cell[1], colChanger.getColor());
      }
      if (highlightTextColor != null) {
        ColorChanger colChanger = new ColorChanger(currentStep,
            matrix.getNum(true), 0,
            "setHighlightTextColor" + " " + cell[0] + " " + cell[1],
            highlightTextColor);
        BasicParser.addAnimatorToAnimation(colChanger, anim);
        matrix.setElemHighlightColor(cell[0], cell[1], colChanger.getColor());
      }
      if (highlightFillColor != null) {
        ColorChanger colChanger = new ColorChanger(currentStep,
            matrix.getNum(true), 0,
            "setHighlightColor" + " " + cell[0] + " " + cell[1],
            highlightFillColor);
        BasicParser.addAnimatorToAnimation(colChanger, anim);
        matrix.setCellHighlightColor(cell[0], cell[1], colChanger.getColor());
      }
      if (highlightBorderColor != null) {
        ColorChanger colChanger = new ColorChanger(currentStep,
            matrix.getNum(true), 0,
            "setHighlightBorderColor" + " " + cell[0] + " " + cell[1],
            highlightBorderColor);
        BasicParser.addAnimatorToAnimation(colChanger, anim);
        matrix.setBorderHighlightColor(cell[0], cell[1], colChanger.getColor());
      }
      // MatrixProducer.setGridColor(gridName, cell[0], cell[1], genericColor,
      // textColor, fillColor, borderColor, highlightTextColor,
      // highlightFillColor, highlightBorderColor, currentStep, duration,
      // offset, unit);
    } // end if valid cellIdentifier
    else {
      MessageDisplay.errorMsg(
          "ParseError line " + stok.lineno()
              + ": setGridValue, Invalid Gridcell: " + gridCell
              + "\n  should be like \"gridcell[1][1]\"",
          MessageDisplay.RUN_ERROR);
    }
  } // end parseSetGridValue()

  /**
   * Parses a command to swap a cell, line, column or the whole grid with
   * another cell, line, column or grid.<br>
   * The commands syntax is
   * <code>swapgridvalues &lt;gridname1&gt;"["&lt;line1&gt;"]["&lt;column1&gt;"]" and
   * &lt;gridname2&gt;"["&lt;line2&gt;"]["&lt;column2&gt;"]"
   * [refresh] [after &lt;n&gt;] [within &lt;n&gt;]</code>
   * 
   * @throws IOException
   */
  public void parseSwapGridValues() throws IOException {
    // swapgridvalues <id1>[<line1>][<column1>] and <id2>[<line2>][<column2>]
    int duration = 0;
    int offset = 0;
    boolean refresh = false;
    String unit = "ticks";

    // first word - should be "swapGridValues"
    ParseSupport.parseWord(stok, "type");
    String cellID1 = AnimalParseSupport.parseText(stok, "Gridcell");
    ParseSupport.parseMandatoryWord(stok, "fill word 'and'", "and");
    String cellID2 = AnimalParseSupport.parseText(stok, "Gridcell");

    if (ParseSupport.parseOptionalWord(stok, "keyword 'refresh'", "refresh")) {
      refresh = true;
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
      offset = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
      duration = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (validateCellIdentifier(cellID1) && validateCellIdentifier(cellID2)) {
      String gridName1 = this.extractNameFromCellIdentifier(cellID1);
      String gridName2 = this.extractNameFromCellIdentifier(cellID2);
      int[] location1 = this.extractLocationFromCellIdentifier(cellID1);
      int[] location2 = this.extractLocationFromCellIdentifier(cellID2);
      int rowA = location1[0], colA = location1[1];
      int rowB = location2[0], colB = location2[1];
      if (gridName1.equals(gridName2)) {
        PTStringMatrix matrix = (PTStringMatrix) animState
            .getCloneByNum(getObjectIDs().getIntProperty(gridName1));
        SetText setter = null;
        
        if (colA == -1 && colB == -1) { // swap entire row?
          int nrCols = matrix.getColumnCount(Math.min(rowA, rowB));
          for (int i = 0; i < nrCols; i++) {
            matrix.swapTextData(rowA, i, rowB, i);
            setter = new SetText(currentStep, matrix.getNum(true), 0, 0,
                "ticks", rowA + " " +i + " " + rowB + " " +i);
            setter.setMethod("swapGridValue");
            BasicParser.addAnimatorToAnimation(setter, anim);
          }
        } else if (rowA == -1 && rowB == -1) { // swap entire column?
          int nrRows= matrix.getRowCount();
          for (int i = 0; i < nrRows; i++) {
            matrix.swapTextData(i, colA, i, colB);
            setter = new SetText(currentStep, matrix.getNum(true), 0, 0,
                "ticks", i + " " +colA + " " + i + " " + colB);
            setter.setMethod("swapGridValue");
            BasicParser.addAnimatorToAnimation(setter, anim);
          }
        } else {
          matrix.swapTextData(location1[0], location1[1], location2[0],
              location2[1]);
          setter = new SetText(currentStep, matrix.getNum(true), 0, 0,
              "ticks", rowA + " " +colA + " " + rowB + " " +colB);
          setter.setMethod("swapGridValue");
          BasicParser.addAnimatorToAnimation(setter, anim);
        }
      }
      // GridProducer.swapGridValues(gridName1, location1[0], location1[1],
      // gridName2, location2[0], location2[1], currentStep, duration, offset,
      // unit, refresh);
    } else {
      MessageDisplay.errorMsg(
          "ParseError line " + stok.lineno()
              + ": swapGridValues, invalid Gridcell: " + cellID1 + " and "
              + cellID2 + "\n  should be like \"gridcell[1][1]\"",
          MessageDisplay.RUN_ERROR);
    }
  }

  /**
   * Parses a command to highlight a cell, line, column or the whole grid.<br>
   * The commands syntax is
   * <code>highlightgridcell &lt;gridname&gt;"["&lt;line&gt;"]["&lt;column&gt;"]"
   * [after &lt;n&gt;] [within &lt;n&gt;]</code>
   * 
   * @throws IOException
   */
  public void parseHighlightGridElem() throws IOException {
    // highlightgridelem <id>[<line>][<column>] <timing>
    int duration = 0;
    int offset = 0;
    String unit = "ticks";

    // first word - should be "highlightgridelem"
    // String first =
    ParseSupport.parseWord(stok, "type").toLowerCase();

    // read in OID (object name) with Location of the cell
    String gridElem = AnimalParseSupport.parseText(stok, "GridElem");

    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
      offset = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
      duration = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (validateCellIdentifier(gridElem)) {
      String gridName = extractNameFromCellIdentifier(gridElem);
      int[] cell = extractLocationFromCellIdentifier(gridElem);
      PTStringMatrix matrix = (PTStringMatrix) animState
          .getCloneByNum(getObjectIDs().getIntProperty(gridName));
      SetText setter = new SetText(currentStep, matrix.getNum(true), 0, 0,
          "ticks", cell[0] + " " + cell[1]);
      setter.setMethod("highlightGridElem");
      matrix.setElementHighlighted(cell[0], cell[1], true);
      BasicParser.addAnimatorToAnimation(setter, anim);
      // GridProducer.highlightGridElem(gridName, cell[0], cell[1], currentStep,
      // duration, offset, unit);
    } else {
      MessageDisplay
          .errorMsg(
              "ParseError line " + stok.lineno() + ": Invalid GridElem: "
                  + gridElem + "\n  should be like gridElem[1][1]",
              MessageDisplay.RUN_ERROR);
    }
  } // end of parseHighlightGridCell()

  /**
   * Parses a command to highlight a cell, line, column or the whole grid.<br>
   * The commands syntax is
   * <code>highlightgridcell &lt;gridname&gt;"["&lt;line&gt;"]["&lt;column&gt;"]"
   * [after &lt;n&gt;] [within &lt;n&gt;]</code>
   * 
   * @throws IOException
   */
  public void parseHighlightGridCell() throws IOException {
    // highlightgridcell <id>[<line>][<column>]
    int duration = 0;
    int offset = 0;
    String unit = "ticks";

    // first word - should be "highlightgridcell"
    // String first =
    ParseSupport.parseWord(stok, "type").toLowerCase();

    // read in OID (object name) with Location of the cell
    String gridCell = AnimalParseSupport.parseText(stok, "GridCell");

    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
      offset = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
      duration = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (validateCellIdentifier(gridCell)) {
      String gridName = extractNameFromCellIdentifier(gridCell);
      int[] cell = extractLocationFromCellIdentifier(gridCell);
      PTStringMatrix matrix = (PTStringMatrix) animState
          .getCloneByNum(getObjectIDs().getIntProperty(gridName));
      SetText setter = new SetText(currentStep, matrix.getNum(true), 0, 0,
          "ticks", cell[0] + " " + cell[1]);
      setter.setMethod("highlightGridCell");
      matrix.setHighlighted(cell[0], cell[1], true);
      BasicParser.addAnimatorToAnimation(setter, anim);
      // GridProducer.highlightGridCell(gridName, cell[0], cell[1], currentStep,
      // duration, offset, unit);
    } else {
      MessageDisplay
          .errorMsg(
              "ParseError line " + stok.lineno() + ": Invalid Gridcell: "
                  + gridCell + "\n  should be like gridcell[1][1]",
              MessageDisplay.RUN_ERROR);
    }
  } // end of parseHighlightGridCell()

  /**
   * Parses a command to unhighlight a cell, line, column or the whole grid.<br>
   * The commands syntax is
   * <code>unhighlightgridcell &lt;gridname&gt;"["&lt;line&gt;"]["&lt;column&gt;"]"
   * [after &lt;n&gt;] [within &lt;n&gt;]</code>
   * 
   * @throws IOException
   */
  public void parseUnhighlightGridCell() throws IOException {
    // unhighlightgridcell <id>[<line>][<column>]
    int duration = 0;
    int offset = 0;
    String unit = "ticks";

    // first word - should be "unhighlightgridcell"
    // String first =
    ParseSupport.parseWord(stok, "type").toLowerCase();

    // read in OID (object name) with Location of the cell
    String gridCell = AnimalParseSupport.parseText(stok, "GridCell");

    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
      offset = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
      duration = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (validateCellIdentifier(gridCell)) {
      String gridName = extractNameFromCellIdentifier(gridCell);
      int[] cell = extractLocationFromCellIdentifier(gridCell);
      PTStringMatrix matrix = (PTStringMatrix) animState
          .getCloneByNum(getObjectIDs().getIntProperty(gridName));
      SetText setter = new SetText(currentStep, matrix.getNum(true), 0, 0,
          "ticks", cell[0] + " " + cell[1]);
      setter.setMethod("unhighlightGridCell");
      matrix.setHighlighted(cell[0], cell[1], false);
      BasicParser.addAnimatorToAnimation(setter, anim);
      // GridProducer.unhighlightGridCell(gridName, cell[0], cell[1],
      // currentStep,
      // duration, offset, unit);
    } else {
      MessageDisplay
          .errorMsg(
              "ParseError line " + stok.lineno() + ": Invalid Gridcell: "
                  + gridCell + "\n  should be like gridcell[1][1]",
              MessageDisplay.RUN_ERROR);
    }
  }// end of unhighlightGridCell()

  public void parseUnhighlightGridElem() throws IOException {
    // highlightgridelem <id>[<line>][<column>] <timing>
    int duration = 0;
    int offset = 0;
    String unit = "ticks";

    // first word - should be "highlightgridelem"
    // String first =
    ParseSupport.parseWord(stok, "type").toLowerCase();

    // read in OID (object name) with Location of the cell
    String gridElem = AnimalParseSupport.parseText(stok, "GridElem");

    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
      offset = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
      duration = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (validateCellIdentifier(gridElem)) {
      String gridName = extractNameFromCellIdentifier(gridElem);
      int[] cell = extractLocationFromCellIdentifier(gridElem);
      PTStringMatrix matrix = (PTStringMatrix) animState
          .getCloneByNum(getObjectIDs().getIntProperty(gridName));
      SetText setter = new SetText(currentStep, matrix.getNum(true), 0, 0,
          "ticks", cell[0] + " " + cell[1]);
      setter.setMethod("unhighlightGridElem");
      matrix.setElementHighlighted(cell[0], cell[1], false);
      BasicParser.addAnimatorToAnimation(setter, anim);
      // GridProducer.highlightGridElem(gridName, cell[0], cell[1], currentStep,
      // duration, offset, unit);
    } else {
      MessageDisplay
          .errorMsg(
              "ParseError line " + stok.lineno() + ": Invalid GridElem: "
                  + gridElem + "\n  should be like gridElem[1][1]",
              MessageDisplay.RUN_ERROR);
    }
  }

  /**
   * Parses a command to align the content of a line, column or the whole
   * grid.<br>
   * The commands syntax is
   * <code>aligngridvalue &lt;gridname&gt;"["&lt;line&gt;"]["&lt;column&gt;"]"
   * {left|right|center}
   * [refresh] [after &lt;n&gt;] [within &lt;n&gt;]...</code>
   * 
   * @throws IOException
   */
  public void parseAlignGridValue() throws IOException {// Testet(worked) -
                                                        // maybe delete
    // aligngridvalue <id>[<line>][<column>] "left"|"right"|"center" [refresh]
    // [within]...
    Alignment alignment = null;
    int duration = 0;
    int offset = 0;
    String unit = "ticks";

    // first word - should be "aligngridvalue"
    // String first =
    ParseSupport.parseWord(stok, "type").toLowerCase();

    // read in OID (object name) with Location of the cell
    String gridCell = AnimalParseSupport.parseText(stok, "GridCell");

    String align = ParseSupport.parseWord(stok, "alignment").toLowerCase();

    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
      offset = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
      duration = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (validateCellIdentifier(gridCell)) {
      String gridName = extractNameFromCellIdentifier(gridCell);
      int[] cell = extractLocationFromCellIdentifier(gridCell);
      if (align.equalsIgnoreCase("left"))
        alignment = Alignment.LEFT;
      else if (align.equalsIgnoreCase("right"))
        alignment = Alignment.RIGHT;
      else if (align.equalsIgnoreCase("center"))
        alignment = Alignment.CENTER;
      else {
        MessageDisplay.errorMsg(
            "ParseError Invalid argument for alignment: " + align
                + "\n  should be 'left', 'center' or 'right'",
            MessageDisplay.RUN_ERROR);
        System.err.println("Invalid alignment: " + align
            + "\n  should be 'left', 'center' or 'right'");
        return;
      }

      if (alignment != null) {
        PTStringMatrix matrix = (PTStringMatrix) animState
            .getCloneByNum(getObjectIDs().getIntProperty(gridName));
        SetText setter = new SetText(currentStep, matrix.getNum(true), 0, 0,
            "ticks", cell[0] + " " + cell[1] + " " + align);
        setter.setMethod("setAlignGridValue");
        matrix.setTextAlignment(alignment, cell[0], cell[1]);
        BasicParser.addAnimatorToAnimation(setter, anim);
      }

    } // end if valid cellIdentifier
    else {
      MessageDisplay
          .errorMsg(
              "ParseError line " + stok.lineno() + ": Invalid Gridcell: "
                  + gridCell + "\n  should be like gridcell[1][1]",
              MessageDisplay.RUN_ERROR);
    }
  }

  /**
   * Parses a command to set the font of a cell, line, column or the whole
   * grid.<br>
   * The commands syntax is
   * <code>setgridfont &lt;gridname&gt;"["&lt;line&gt;"]["&lt;column&gt;"]"
   * &lt;fontdefinition&gt; [refresh] [after &lt;n&gt;] [within &lt;n&gt;]</code>
   * 
   * @throws IOException
   */
  public void parseSetGridFont() throws IOException {
    // setgridfont <id>[<line>][<column>] <font> [refresh] <within...>
    int duration = 0;
    int offset = 0;
    String unit = "ticks";
    boolean refresh = false;

    // first word - should be "aligngridvalue"
    // String first =
    ParseSupport.parseWord(stok, "type").toLowerCase();

    // read in OID (object name) with Location of the cell
    String gridCell = AnimalParseSupport.parseText(stok, "GridCell");

    Font newFont = AnimalParseSupport.parseFontInfo(stok, "Gridcell");

    if (ParseSupport.parseOptionalWord(stok, "keyword 'refresh'", "refresh")) {
      refresh = true;
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
      offset = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
      duration = ParseSupport.parseInt(stok, "offset value", 0);
      unit = ParseSupport.parseWord(stok, "unit");
    }

    if (validateCellIdentifier(gridCell)) {
      String gridName = extractNameFromCellIdentifier(gridCell);
      int[] cell = extractLocationFromCellIdentifier(gridCell);
      PTStringMatrix matrix = (PTStringMatrix) animState
          .getCloneByNum(getObjectIDs().getIntProperty(gridName));
      SetFont setter = new SetFont(currentStep, matrix.getNum(true), 0, 0,
          "ticks", newFont);
      setter.setMethod("setFontGridValue " + cell[0] + " " + cell[1]);
      matrix.setCellFonts(cell[0], cell[1], newFont);
      BasicParser.addAnimatorToAnimation(setter, anim);
    } // end if valid cellIdentifier
    else {
      MessageDisplay
          .errorMsg(
              "ParseError line " + stok.lineno() + ": Invalid Gridcell: "
                  + gridCell + "\n  should be like gridcell[1][1]",
              MessageDisplay.RUN_ERROR);
    }

  }// end of parseSetGridFont()

}
