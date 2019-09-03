package generators.misc.helpers;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class ZBufferAnim {

  /**
   * The concrete language object used for creating output
   */
  private Language             lang;
  private SourceCode           sourceCode;
  private MatrixProperties     zbufferProp;
  private RectProperties       frameBufferBackground;
  private int                  PIXEL_SIZE = 30;
  private Rect[][]             rectArray;
  private MatrixProperties     polygon1Prop;
  private MatrixProperties     polygon2Prop;
  private SourceCodeProperties codeProp;
  private int[][]              polygonarray1;
  private int[][]              polygonarray2;

  // private static int matrixSize;

  public ZBufferAnim(Language lang, MatrixProperties polygon1Prop,
      MatrixProperties polygon2Prop, MatrixProperties zbufferProp,
      int[][] polygonarray1, int[][] polygonarray2,
      RectProperties frameBufferBackground) {

    System.out.println("#########################");
    System.out.println("Init");
    this.lang = lang;
    this.lang.setStepMode(true);

    this.polygon1Prop = polygon1Prop;
    this.polygon2Prop = polygon2Prop;

    this.polygonarray1 = polygonarray1;
    this.polygonarray2 = polygonarray2;
    this.zbufferProp = zbufferProp;
    this.frameBufferBackground = frameBufferBackground;
    generateCode();
  }

  public void generateCode() {
    description();
    initializeAlgo();
  }

  public void description() {

    // 1
    TextProperties titleProp = new TextProperties();

    titleProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));

    Text title = new Text(new AnimalTextGenerator(lang),
        new Coordinates(20, 35), "Z-Buffer", "title", null, titleProp);
    lang.addItem(title);

    RectProperties headerRect = new RectProperties();
    headerRect.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    headerRect.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    headerRect.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(192, 192,
        192));

    lang.newRect(new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "title", AnimalScript.DIRECTION_SE), "headerRect",
        null, headerRect);

    TextProperties algDescriptionProp = new TextProperties();
    algDescriptionProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Serif", Font.BOLD, 22));

    Text algDescription = new Text(
        new AnimalTextGenerator(lang),
        new Coordinates(20, 100),
        "In computer graphics, z-buffering is the management of image depth coordinates",
        "alg_description", null, algDescriptionProp);

    lang.addItem(algDescription);

    // 2
    lang.nextStep("Description");

    algDescription.hide();

    Text description = new Text(new AnimalTextGenerator(lang), new Coordinates(
        20, 100), "Description", "description", null, algDescriptionProp);
    lang.addItem(description);

    // 3
    lang.nextStep("Description");

    TextProperties textLinesProps = new TextProperties();
    textLinesProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));

    Text line0 = new Text(
        new AnimalTextGenerator(lang),
        new Offset(0, 100, "title", AnimalScript.DIRECTION_SW),
        "1. Each polygon has a matrix with depth values and a matrix with color values. ",
        "line0", null, textLinesProps);
    lang.addItem(line0);

    // 4
    lang.nextStep("Description");

    Text line1 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "line0", AnimalScript.DIRECTION_SW), "2. Initiate z-buffer with -2.",
        "line1", null, textLinesProps);
    lang.addItem(line1);

    // 5
    lang.nextStep("Description");

    Text line2 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "line1", AnimalScript.DIRECTION_SW),
        "3. Initiate frame-buffer with the background color.", "line2", null,
        textLinesProps);
    lang.addItem(line2);

    // 6
    lang.nextStep("Description");

    Text line3 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "line2", AnimalScript.DIRECTION_SW),
        "4. Compare depth value on position (x,y) of each polygon with value ",
        "line3", null, textLinesProps);
    lang.addItem(line3);

    Text line31 = new Text(new AnimalTextGenerator(lang), new Offset(0, 10,
        "line3", AnimalScript.DIRECTION_SW),
        "   on position (x,y) in z-buffer.", "line31", null, textLinesProps);
    lang.addItem(line31);

    // 7

    lang.nextStep("Description");

    Text line4 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "line31", AnimalScript.DIRECTION_SW),
        "5. If the value is closer to the screen as the value in z-buffer:",
        "line4", null, textLinesProps);
    lang.addItem(line4);

    Text line41 = new Text(
        new AnimalTextGenerator(lang),
        new Offset(0, 10, "line4", AnimalScript.DIRECTION_SW),
        "   update the z-buffer and the frame-buffer with the values of polygon.",
        "line41", null, textLinesProps);
    lang.addItem(line41);

    // 8
    lang.nextStep("Initialization");

    description.hide();
    line0.hide();
    line1.hide();
    line2.hide();
    line3.hide();
    line31.hide();
    line4.hide();
    line41.hide();
  }

  public void initializeAlgo() {

    TextProperties textLinesProps = new TextProperties();
    textLinesProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    if (!sameMatrixSize(polygonarray1, polygonarray2)) {

      TextProperties algDescriptionProp = new TextProperties();
      algDescriptionProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Serif", Font.BOLD, 22));

      Text algDescription = new Text(
          new AnimalTextGenerator(lang),
          new Coordinates(20, 100),
          "Polygon matrixes can not have different size, please generate a new animation",
          "error", null, algDescriptionProp);
      lang.addItem(algDescription);
      // /

      // POL1
      Text pol1 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
          "error", AnimalScript.DIRECTION_SW), "Polygon 1:", "pol1", null,
          textLinesProps);
      lang.addItem(pol1);

      lang.newIntMatrix(new Offset(0, 5, "pol1", AnimalScript.DIRECTION_SW),
          polygonarray1, "polygon1", null, this.polygon1Prop);

      // POL2

      lang.newIntMatrix(
          new Offset(10, 0, "polygon1", AnimalScript.DIRECTION_NE),
          polygonarray2, "polygon2", null, this.polygon2Prop);

      Text pol2 = new Text(new AnimalTextGenerator(lang), new Offset(0, -20,
          "polygon2", AnimalScript.DIRECTION_NW), "Polygon 2:", "pol2", null,
          textLinesProps);
      lang.addItem(pol2);

      // /
      return;
    }
    // CODE
    //
    codeProp = new SourceCodeProperties();
    codeProp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, new Color(0,
        255, 255));
    codeProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 12));
    codeProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(
        255, 0, 0));

    sourceCode = lang.newSourceCode(new Offset(10, 20, "title",
        AnimalScript.DIRECTION_SW), "sourceCode", null, this.codeProp);

    sourceCode.addCodeLine("initialize z-buffer with -2", null, 0, null);
    sourceCode.addCodeLine("initialize frame-buffer with background color",
        null, 0, null);
    sourceCode.addCodeLine("", null, 0, null);

    sourceCode.addCodeLine("for(each polygon P) do{", null, 0, null);
    sourceCode.addCodeLine("for(each pixel(x,y) that intersects P) do{", null,
        1, null);

    sourceCode.addCodeLine("If (P(x,y) < z-buffer[x,y]) then{", null, 2, null);
    sourceCode.addCodeLine("z-buffer[x,y]=z-depth;", null, 3, null);
    sourceCode.addCodeLine("frame-buffer(x,y)=color of P", null, 3, null);
    sourceCode.addCodeLine("}", null, 2, null);
    sourceCode.addCodeLine("}", null, 1, null);
    sourceCode.addCodeLine("}", null, 0, null);
    sourceCode.addCodeLine("display frame-buffer", null, 0, null);

    // POL1
    Text pol1 = new Text(new AnimalTextGenerator(lang), new Offset(0, 30,
        "sourceCode", AnimalScript.DIRECTION_SW), "Polygon 1:", "pol1", null,
        textLinesProps);
    lang.addItem(pol1);

    IntMatrix polygon1 = lang.newIntMatrix(new Offset(0, 5, "pol1",
        AnimalScript.DIRECTION_SW), polygonarray1, "polygon1", null,
        this.polygon1Prop);
    // highlightPolygon(polygon1);

    // POL2

    IntMatrix polygon2 = lang.newIntMatrix(new Offset(10, 0, "polygon1",
        AnimalScript.DIRECTION_NE), polygonarray2, "polygon2", null,
        this.polygon2Prop);

    Text pol2 = new Text(new AnimalTextGenerator(lang), new Offset(0, -20,
        "polygon2", AnimalScript.DIRECTION_NW), "Polygon 2:", "pol2", null,
        textLinesProps);
    lang.addItem(pol2);

    // highlightPolygon(polygon2);

    // 9
    lang.nextStep("Initialization");
    sourceCode.highlight(0);
    // z-buffer

    int[][] zbufferarray = new int[polygonarray1.length][polygonarray1.length];
    initMatrix(zbufferarray, -2);

    IntMatrix zbuffer = lang.newIntMatrix(new Offset(10, 0, "polygon2",
        AnimalScript.DIRECTION_NE), zbufferarray, "zbuffer", null,
        this.zbufferProp);
    lang.addItem(zbuffer);

    RectProperties white = new RectProperties();
    white.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    white.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    white.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 255, 255));
    lang.newRect(new Offset(10, 0, "zbuffer", AnimalScript.DIRECTION_NE),
        new Offset(11, 1, "zbuffer", AnimalScript.DIRECTION_NE), "pointX",
        null, white);

    Text zbufferText = new Text(new AnimalTextGenerator(lang), new Offset(0,
        -20, "zbuffer", AnimalScript.DIRECTION_NW), "Z-Buffer:", "zbuffertext",
        null, textLinesProps);

    lang.addItem(zbufferText);
    // 10
    lang.nextStep("Initialization");
    sourceCode.unhighlight(0);
    sourceCode.highlight(1);
    // frame-buffer

    drawEmptyFrameBuffer();

    Text framebufferText = new Text(new AnimalTextGenerator(lang), new Offset(
        0, -20, "pixel00", AnimalScript.DIRECTION_NW), "Frame-Buffer:",
        "framebuffertext", null, textLinesProps);
    // 11
    lang.nextStep("Initialization");
    sourceCode.unhighlight(1);
    sourceCode.highlight(3);
    pol1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 0,
        0), null, null);
    // 12
    lang.nextStep("Executation of Z-Buffer Algorithm");
    sourceCode.unhighlight(3);
    sourceCode.highlight(4);

    // ##############
    zBufferAlg(polygon1, polygon1Prop, zbuffer);
    lang.nextStep("Executation of Z-Buffer Algorithm");
    sourceCode.unhighlight(4);
    sourceCode.highlight(3);
    pol1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        new Color(0, 0, 0), null, null);

    pol2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 0,
        0), null, null);
    // 12
    lang.nextStep("Executation of Z-Buffer Algorithm");
    sourceCode.unhighlight(3);
    sourceCode.highlight(4);

    zBufferAlg(polygon2, polygon2Prop, zbuffer);
    lang.nextStep("Executation of Z-Buffer Algorithm");
    sourceCode.unhighlight(4);
    sourceCode.highlight(11);

    lang.nextStep("Result");
    sourceCode.hide();
    pol1.hide();
    polygon1.hide();
    pol2.hide();
    polygon2.hide();
    zbufferText.hide();
    zbuffer.hide();
    framebufferText.hide();
    TextProperties algDescriptionProp = new TextProperties();
    algDescriptionProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Serif", Font.BOLD, 22));
    Text result = new Text(new AnimalTextGenerator(lang), new Coordinates(20,
        100), "RESULTING FRAME-BUFFER:", "result", null, algDescriptionProp);

    lang.addItem(result);

    moveFrameBuffer();
  }

  private boolean sameMatrixSize(int[][] p1, int[][] p2) {
    if (p1.length != p2.length || p1.length == 0 || p2.length == 0)
      return false;
    if (p1[0].length != p2[0].length)
      return false;
    return true;
  }

  private void moveFrameBuffer() {
    try {
      // rectArray[0][0].moveTo(null, null, new Offset(0,20,
      // "result",AnimalScript.DIRECTION_SW), null, null);
      int size = polygonarray1.length;
      int xPos = 0;
      int yPos = 20;
      for (int x = 0; x < size; x++) {
        for (int y = 0; y < size; y++) {
          rectArray[x][y].moveTo(null, "translate", new Offset(xPos, yPos
              + PIXEL_SIZE, "result", AnimalScript.DIRECTION_SW), null,
              new Timing(500) {

                @Override
                public String getUnit() {
                  return null;
                }
              });
          yPos += PIXEL_SIZE;
        }
        yPos = 20;
        xPos += PIXEL_SIZE;
      }
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }
  }

  private void zBufferAlg(IntMatrix p, MatrixProperties pProp, IntMatrix zb) {
    for (int j = 0; j < p.getNrRows(); j++) {
      for (int i = 0; i < p.getNrCols(); i++) {
        // 12

        lang.nextStep();
        sourceCode.unhighlight(4);
        sourceCode.highlight(5);
        p.highlightCell(i, j, null, null);
        zb.highlightCell(i, j, null, null);

        if (p.getElement(i, j) > 0 && p.getElement(i, j) < zb.getElement(i, j)
            || zb.getElement(i, j) == -2 && p.getElement(i, j) > 0) {
          lang.nextStep();
          sourceCode.unhighlight(5);
          sourceCode.highlight(6);
          zb.put(i, j, p.getElement(i, j), null, null);

          lang.nextStep();
          sourceCode.unhighlight(6);
          sourceCode.highlight(7);
          highlightPixel(j, i,
              (Color) pProp.get(AnimationPropertiesKeys.FILL_PROPERTY));
          lang.nextStep();
          sourceCode.unhighlight(7);
          sourceCode.highlight(4);
        } else {
          lang.nextStep();
          sourceCode.unhighlight(5);
          sourceCode.highlight(4);

        }
        p.unhighlightCell(i, j, null, null);
        zb.unhighlightCell(i, j, null, null);
      }
    }

  }

  private void highlightPixel(int i, int j, Color color) {

    Offset lowerRight = (Offset) rectArray[i][j].getLowerRight();
    Offset upperLeft = (Offset) rectArray[i][j].getUpperLeft();
    rectArray[i][j].hide();
    RectProperties tProp = new RectProperties();
    tProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    tProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    tProp.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
    Rect r = lang.newRect(upperLeft, lowerRight, "pixel" + i + j, null, tProp);
    rectArray[i][j] = r;

  }

  private void drawEmptyFrameBuffer() {

    // frameBufferBackground
    int size = polygonarray1.length;
    // start point
    int startX = 0;
    int startY = 0;
    rectArray = new Rect[size][size];
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {

        rectArray[x][y] = lang.newRect(new Offset(startX, startY, "pointX",
            AnimalScript.DIRECTION_NE), new Offset(startX + PIXEL_SIZE, startY
            + PIXEL_SIZE, "pointX", AnimalScript.DIRECTION_NE),
            "pixel" + x + y, null, frameBufferBackground);
        startY += PIXEL_SIZE;
      }
      startY = 0;
      startX += PIXEL_SIZE;
    }

  }

  private void initMatrix(int[][] zbufferarray, int i) {
    for (int y = 0; y < zbufferarray.length; y++)
      for (int x = 0; x < zbufferarray.length; x++)
        zbufferarray[x][y] = i;

  }

  public static void main(String[] args) {

    RectProperties frameBufferBackground = new RectProperties();
    frameBufferBackground.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    frameBufferBackground.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    frameBufferBackground.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(
        0, 0, 0));

    MatrixProperties zbufferProp = new MatrixProperties();

    zbufferProp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 255,
        255));

    zbufferProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, new Color(0,
        0, 0));

    MatrixProperties polygon1Prop = new MatrixProperties();

    polygon1Prop.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 255,
        255));

    polygon1Prop.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, new Color(
        0, 0, 0));
    polygon1Prop.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(
        232, 0, 232));
    polygon1Prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 10));

    MatrixProperties polygon2Prop = new MatrixProperties();

    polygon2Prop.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 255,
        255));

    polygon2Prop.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, new Color(
        0, 0, 0));
    polygon2Prop.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(
        0, 190, 230));
    polygon2Prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 10));

    SourceCodeProperties code = new SourceCodeProperties();
    code.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, new Color(0, 255,
        255));
    code.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 12));
    code.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(255, 0,
        0));
    // Create a new animation
    // name, author, screen width, screen height

    // /POLYGONE1
    int[][] polygonarray1 = new int[3][3];
    int l = polygonarray1.length;

    for (int y = 0; y < polygonarray1.length; y++) {
      for (int x = 0; x < l; x++) {
        polygonarray1[y][x] = 5;

      }
      l--;
    }

    int[][] polygonarray2 = new int[5][4];
    l = 0;
    int value = 6;
    for (int y = 0; y < polygonarray2.length; y++) {
      for (int x = l; x < polygonarray2[0].length; x++) {
        polygonarray2[y][x] = value;

      }
      value--;
      l++;
    }

    Language lang = new AnimalScript("Z-Buffer", "Dieter Hofmann, Artem Vovk",
        800, 600);
    ZBufferAnim anim = new ZBufferAnim(lang, polygon1Prop, polygon2Prop,
        zbufferProp, polygonarray1, polygonarray2, frameBufferBackground);
    // int[] a = { 7, 3, 2, 4, 1, 13, 52, 13, 5, 1 };
    // s.sort(a);
    anim.generateCode();
    System.out.println(lang);
  }

}
