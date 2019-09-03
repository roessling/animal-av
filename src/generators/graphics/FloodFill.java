package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Polygon;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolygonProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author MT-TV <mt-tv@nonesuchmail.ne>
 * @version 1.000.000 / 1.0 final stable release
 * 
 */
public class FloodFill implements Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language            lang;

  // private static final String AUTHORS =
  // "Martin Tschirsich, Tjark Vandommele et errator";

  // private static final String ALGORITHM = "Flood Fill";

  private static final String DESCRIPTION    = "Flood fill, also called seed fill, is an algorithm that determines "
                                                 + "the area connected to a given node in a multi-dimensional array."
                                                 + "\n"
                                                 + "The flood fill algorithm takes three parameters: a start node, a "
                                                 + "target color, and a replacement color. The algorithm looks for all "
                                                 + "nodes in the array which are connected to the start node by a path "
                                                 + "of the target color and changes them to the replacement color."
                                                 + "<br><br>"
                                                 + "<i>Flood fill. (2009, April 22). In Wikipedia, The Free Encyclopedia."
                                                 + "Retrieved 16:11, April 22, 2009, from "
                                                 + "http://en.wikipedia.org/w/index.php?title=Flood_fill&oldid=285426343</i>";

  private static final String SOURCE_CODE    = "fill4(x, y) {<br><br>"
                                                 + " if (getPixel(x, y) == target color) {<br>"
                                                 + "  setPixel(x, y, fill color);<br><br>"
                                                 + "  fill4(x, y + 1);<br>"
                                                 + "  fill4(x, y - 1);<br>"
                                                 + "  fill4(x + 1, y);<br>"
                                                 + "  fill4(x - 1, y);<br><br>"
                                                 + " }<br>" + " return;<br>"
                                                 + "}";

  private Color               sourcePixel    = new Color(43, 130, 227);                                                       // Pixel
                                                                                                                               // to
                                                                                                                               // be
                                                                                                                               // filled
  private Color               standardPixel  = new Color(3, 90, 187);                                                         // Pixel
                                                                                                                               // not
                                                                                                                               // to
                                                                                                                               // be
                                                                                                                               // filled
  private Color               wrongPixel     = new Color(250, 0, 0);                                                          // Highlight:
                                                                                                                               // Not
                                                                                                                               // to
                                                                                                                               // be
                                                                                                                               // filled
  private Color               highlightPixel = new Color(250, 250, 0);                                                        // Highlight:
                                                                                                                               // To
                                                                                                                               // be
                                                                                                                               // filled
  private Color               filledPixel    = new Color(0, 250, 0);                                                          // Filled
                                                                                                                               // pixel

  private String colorToString(Color color) {
    return "(" + color.getRed() + ", " + color.getGreen() + ", "
        + color.getBlue() + ")";
  }

  private algoanim.primitives.Text currentText;

  private int                      depth;

  public void init() {
    lang = new AnimalScript("Flood Fill",
        "Martin Tschirsich, Tjark Vandommele", 640, 480);
    lang.setStepMode(true);
    currentText = null;
    depth = 0;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    init();

    // Parameters:
    String[][] a = { { " ", " ", " " }, { "1", "1", " " }, { " ", "1", " " } };
    Boolean randomArray = (Boolean) primitives.get("Random array?");

    standardPixel = (Color) primitives.get("Basic color");
    sourcePixel = (Color) primitives.get("Target color");
    filledPixel = (Color) primitives.get("Fill color");

    // Draw header:
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 28));
    algoanim.primitives.Text h1 = lang.newText(new Coordinates(20, 20),
        "Flood Fill (4-Neighbor)", "h1", null, textProps);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    lang.newRect(new Offset(-15, 5, h1, "SW"), new Offset(10, 10, h1, "SE"),
        "h1Rect1", null, rectProps);
    lang.newRect(new Offset(10, 0, h1, "NE"), new Offset(12, 10, h1, "SE"),
        "h1Rect2", null, rectProps);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    lang.newRect(new Offset(-15, 0, h1, "NW"), new Offset(12, 10, h1, "SE"),
        "h1Rect1", null, rectProps);

    // Draw field:
    PolygonProperties polyProps = new PolygonProperties();
    Coordinates[] p1coords = new Coordinates[6];
    p1coords[0] = new Coordinates(25, 165);
    p1coords[1] = new Coordinates(25, 75);
    p1coords[2] = new Coordinates(20, 75);
    p1coords[3] = new Coordinates(25, 65);
    p1coords[4] = new Coordinates(30, 75);
    p1coords[5] = new Coordinates(25, 75);
    Polygon p1 = null;
    try {
      p1 = lang.newPolygon(p1coords, "p1", null, polyProps);
    } catch (NotEnoughNodesException e) {
      e.printStackTrace();
    }

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    lang.newText(new Offset(-10, -20, p1, "NW"), "y", "t1", null, textProps);

    Coordinates[] p2coords = new Coordinates[6];
    p2coords[0] = new Coordinates(20, 160);
    p2coords[1] = new Coordinates(125, 160);
    p2coords[2] = new Coordinates(125, 165);
    p2coords[3] = new Coordinates(135, 160);
    p2coords[4] = new Coordinates(125, 155);
    p2coords[5] = new Coordinates(125, 160);
    Polygon p2 = null;
    try {
      p2 = lang.newPolygon(p2coords, "p2", null, polyProps);
    } catch (NotEnoughNodesException e) {
      e.printStackTrace();
    }

    lang.newText(new Offset(-5, 2, p2, "NE"), "x", "t2", null, textProps);

    // Der automatisch eingefuegte Refresh am Ende der Matrixdeklaration führt
    // zu Darstellungsfehlern:
    // MatrixProperties matrixProps = new MatrixProperties();
    // matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    // StringMatrix field = lang.newStringMatrix(new Coordinates(35, 80), a,
    // "field", null, matrixProps);

    // Workaround:
    // Create 2D-Array and initialize some fields to the source color:
    lang.addLine("grid \"field\" (35, 80) lines 3 columns 3 color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");

    // Initialize array with random values if specified:
    if (randomArray) {
      for (int x = 0; x <= 2; x++) {
        for (int y = 0; y <= 2; y++) {

          lang.addLine("setGridValue \"field[" + (2 - y) + "][" + x
              + "]\" \" \"");
          if (Math.random() > 0.5 || (x == 1 && y == 1)) {
            lang.addLine("setGridColor \"field[" + (2 - y) + "][" + x
                + "]\" fillColor " + colorToString(sourcePixel));
            a[x][y] = "1";
          } else {
            lang.addLine("setGridColor \"field[" + (2 - y) + "][" + x
                + "]\" fillColor " + colorToString(standardPixel));
            a[x][y] = "_";
          }
        }
      }
    } else {
      for (int x = 0; x <= 2; x++) {
        for (int y = 0; y <= 2; y++) {

          lang.addLine("setGridValue \"field[" + (2 - y) + "][" + x
              + "]\" \" \"");
          if (a[x][y] == "1") {
            lang.addLine("setGridColor \"field[" + (2 - y) + "][" + x
                + "]\" fillColor " + colorToString(sourcePixel));
          } else {
            lang.addLine("setGridColor \"field[" + (2 - y) + "][" + x
                + "]\" fillColor " + colorToString(standardPixel));
            a[x][y] = "_";
          }
        }
      }
    }

    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, sourcePixel);
    Rect fieldRect1 = lang.newRect(new Offset(150, 70, h1, "NW"), new Offset(
        170, 90, h1, "NW"), "fieldRect1", null, rectProps);

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));
    lang.newText(new Offset(10, 0, fieldRect1, "NE"), "= target color",
        "fieldRect1Text1", null, textProps);

    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, filledPixel);
    Rect fieldRect2 = lang.newRect(new Offset(150, 110, h1, "NW"), new Offset(
        170, 130, h1, "NW"), "fieldRect2", null, rectProps);

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));
    lang.newText(new Offset(10, 0, fieldRect2, "NE"), "= fill color",
        "fieldRect2Text1", null, textProps);

    lang.nextStep();

    // Show algorithm:
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    SourceCode sc1 = lang.newSourceCode(new Offset(0, 140, h1, "SW"), "code1",
        null, scProps);
    SourceCode sc2 = lang.newSourceCode(new Offset(380, 140, h1, "SW"),
        "code2", null, scProps);

    sc1.addCodeLine("fill4(x, y) {", null, 0, null);
    sc1.addCodeLine("", null, 0, null);

    sc2.addCodeLine("Fill at position (x, y):", null, 0, null);
    sc2.addCodeLine("", null, 0, null);

    sc1.highlight(0);
    sc2.highlight(0);

    lang.nextStep();

    sc1.unhighlight(0);
    sc2.unhighlight(0);

    sc1.addCodeLine("if (getPixel(x, y) == target color) {", null, 1, null);
    sc1.addCodeLine("", null, 1, null);
    sc1.addCodeLine("setPixel(x, y, fill color);", null, 2, null);
    sc1.addCodeLine("", null, 2, null);

    sc2.addCodeLine("If pixel color equals target color...", null, 1, null);
    sc2.addCodeLine("", null, 0, null);
    sc2.addCodeLine("...set pixel color to fill color", null, 2, null);
    sc2.addCodeLine("", null, 0, null);

    sc1.highlight(2);
    sc1.highlight(4);
    sc2.highlight(2);
    sc2.highlight(4);

    lang.nextStep();

    sc1.unhighlight(2);
    sc1.unhighlight(4);
    sc2.unhighlight(2);
    sc2.unhighlight(4);

    sc1.addCodeLine("fill4(x, y + 1);", null, 2, null);
    sc1.addCodeLine("fill4(x, y - 1);", null, 2, null);
    sc1.addCodeLine("fill4(x + 1, y);", null, 2, null);
    sc1.addCodeLine("fill4(x - 1, y);", null, 2, null);
    sc1.addCodeLine("", null, 2, null);
    sc1.addCodeLine("}", null, 1, null);
    sc1.addCodeLine("return;", null, 1, null);
    sc1.addCodeLine("}", null, 0, null);

    sc2.addCodeLine("...and fill adjacent neighbors", null, 2, null);
    sc2.addCodeLine("", null, 2, null);
    sc2.addCodeLine("", null, 2, null);
    sc2.addCodeLine("", null, 2, null);
    sc2.addCodeLine("", null, 2, null);
    sc2.addCodeLine("", null, 2, null);
    sc2.addCodeLine("", null, 2, null);
    sc2.addCodeLine("", null, 2, null);

    sc1.highlight(6);
    sc1.highlight(7);
    sc1.highlight(8);
    sc1.highlight(9);
    sc2.highlight(6);

    lang.nextStep();

    sc1.unhighlight(6);
    sc1.unhighlight(7);
    sc1.unhighlight(8);
    sc1.unhighlight(9);
    sc2.unhighlight(6);

    sc2.hide();

    // Run algorithm:
    floodFill(a, null, 1, 1);
    return lang.toString();
  }

  private void floodFill(String[][] a, SourceCode codeSupport, int x, int y) {

    // Set the basic text properties for new lines:
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 14));

    // Retrieve the last placed text-Element:
    if (currentText == null) {
      textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0, 0));
      lang.newText(new Coordinates(400, 10),
          "Run algorithm: Start at center position (1, 1)", "", null, textProps);
      currentText = lang.newText(new Coordinates(400, 30), "", "", null,
          textProps);
      lang.nextStep();
    }

    if (x > 2 || x < 0 || y > 2 || y < 0) {

      // Pixel x,y out of bounds:
      textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, wrongPixel);
      currentText = lang.newText(new Offset(0, 15, currentText, "NW"), "fill4("
          + x + ", " + y + ") - out of bounds", currentText.getName(), null,
          textProps);
      lang.nextStep();
    } else if (a[x][y] == "1") {

      // Pixel x,y has to be filled:
      a[x][y] = "x";

      textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, filledPixel);
      currentText = lang.newText(new Offset(0, 15, currentText, "NW"), "fill4("
          + x + ", " + y + ")", currentText.getName(), null, textProps);
      lang.addLine("setGridColor \"field[" + (2 - y) + "][" + x
          + "]\" fillColor " + colorToString(highlightPixel));

      lang.nextStep();
      lang.addLine("setGridColor \"field[" + (2 - y) + "][" + x
          + "]\" fillColor " + colorToString(filledPixel));

      lang.addLine("setGridValue \"field[" + (2 - y) + "][" + x + "]\" \""
          + depth++ + "\"");

      floodFill(a, codeSupport, x, y + 1);
      floodFill(a, codeSupport, x, y - 1);
      floodFill(a, codeSupport, x + 1, y);
      floodFill(a, codeSupport, x - 1, y);
    } else if (a[x][y] == "x") {

      // Pixel x,y was already filled:
      textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, wrongPixel);
      currentText = lang.newText(new Offset(0, 15, currentText, "NW"), "fill4("
          + x + ", " + y + ") - wrong color", currentText.getName(), null,
          textProps);
      lang.addLine("setGridColor \"field[" + (2 - y) + "][" + x
          + "]\" fillColor " + colorToString(highlightPixel));

      lang.nextStep();
      lang.addLine("setGridColor \"field[" + (2 - y) + "][" + x
          + "]\" fillColor " + colorToString(filledPixel));
    } else {

      // Pixel x,y is not to be filled:
      textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, wrongPixel);
      currentText = lang.newText(new Offset(0, 15, currentText, "NW"), "fill4("
          + x + ", " + y + ")", currentText.getName(), null, textProps);
      lang.addLine("setGridColor \"field[" + (2 - y) + "][" + x
          + "]\" fillColor " + colorToString(highlightPixel));

      lang.nextStep();
      lang.addLine("setGridColor \"field[" + (2 - y) + "][" + x
          + "]\" fillColor " + colorToString(standardPixel));
    }
    return;
  }

  public String getAlgorithmName() {
    return "Flood Fill";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public String getAnimationAuthor() {
    return "Martin Tschirsich, Tjark Vandommele";
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public String getName() {
    return "FloodFill (4-neighbor)";
  }
}
