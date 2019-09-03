package generators.maths;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.properties.items.AnimationPropertyItem;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class ExtEuclid {
  /**
   * The concrete language object used for creating output
   */
  private Language             lang;

  // private static final String LN = "\n";

  /**
   * The java code fragment shown beside the table for the extended euclidean
   * algorithm which is highlighted each step
   */

  public final static Color    DEFAULT_HEADER_COLOR           = new Color(240,
                                                                  235, 238);
  public final static Color    DEFAULT_HEADERBACKGROUND_COLOR = new Color(100,
                                                                  84, 80);
  public final static Color    DEFAULT_RESULT_COLOR           = Color.orange;
  public final static Color    DEFAULT_CODE_COLOR             = new Color(100,
                                                                  84, 80);
  public final static Color    DEFAULT_CODEHIGHTLIGHT_COLOR   = Color.black;
  public final static int      DEFAULT_FONTSIZE               = 14;
  public final static int      DEFAULT_A                      = 12;
  public final static int      DEFAULT_B                      = 34;

  /**
   * This default values color the header box including the name of the
   * algorithm, it's shadow and the background.
   */
  private Color                header                         = new Color(240,
                                                                  235, 238),
      headerBackground = new Color(100, 84, 80);

  /**
   * The color of the shadow depend on the chosen color for the background of
   * the header. This color should not be set by the user.
   */
  private Color                headerShadow                   = new Color(50,
                                                                  50, 50);

  /**
   * The result x and y is colored with
   */
  private Color                resultColor                    = Color.orange;

  private int                  tableFontSize                  = 14;

  private Text                 q, r, x, y, a, b, x2, x1, y2, y1;
  private Polyline             tableV1, tableV2, tableV3, tableV4, tableV5,
      tableV6, tableV7, tableV8, tableV9, tableH1;

  /**
   * This default color values are taken to color the java code fragment (left)
   * and the term showing the inverses a' and b' (top).
   */
  private Color                codeHighlight                  = Color.black,
      codeColor = new Color(100, 84, 80);

  /**
   * The two integers used as the inputs for the Extended Euclidean Algorithm.
   * In default case those got a default value to avoid that the algorithm got
   * nothing to calculate on.
   */
  private static int           inputA                         = 12,
      inputB = 34;

  private RectProperties       headerRectProps                = new RectProperties();
  // headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
  // headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
  // headerBackground);
  // headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

  private SourceCodeProperties codeProps                      = new SourceCodeProperties();
  // codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
  // Font.BOLD, 12));
  // codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
  // codeHighlight);
  // codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeColor);

  private TextProperties       headerProps                    = new TextProperties();

  // headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, header);
  // headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
  // Font("SansSerif", Font.BOLD, 24));
  // headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

  /**
   * Constructor for generating the script with modifiable parameters
   * 
   * @param l
   *          The language instance
   * @param headerRectProps
   *          Only the FILL_PROPERTY is modifiable
   * @param headerProps
   *          Only the FILL_PROPERTY is changeable
   */
  public ExtEuclid(Language l, RectProperties headerRectProps,
      TextProperties headerProps, SourceCodeProperties codeProps, int inputa,
      int inputb) {
    ExtEuclid.inputA = inputa;
    ExtEuclid.inputB = inputb;

    this.headerRectProps = headerRectProps;

    // Create a darker color instance for the shadow of the header.
    AnimationPropertyItem p = headerRectProps.getItem("color");
    Color current = (Color) p.get();
    if (!current.equals(headerBackground)) {
      // Set the color to make it usable for other properties
      headerBackground = current;

      // If the background still is dark enough the shadow will be black
      int r = current.getRed(), g = current.getGreen(), b = current.getBlue();
      if ((r - 40 > 0) && (g - 40 > 0) && (b - 40 > 0))
        headerShadow = new Color(r - 40, g - 40, b - 40);
      else
        headerShadow = Color.black;
    }

    this.headerProps = headerProps;
    // Set the color to make it usable for other properties
    AnimationPropertyItem p2 = headerProps.getItem("color");
    header = (Color) p2.get();

    this.codeProps = codeProps;

    lang = l;
    lang.setStepMode(true);

    this.createAnimation(inputa, inputb);
  }

  /**
   * Compounds all fragments of the animation to an AnimalScript
   * 
   * @param inputA
   * @param inputB
   */
  public void createAnimation(int inputA, int inputB) {
    createGraphicObjects(inputA, inputB);
  }

  /**
   * Creates step by step each frame of the Extended Euclidean Algorithm
   * animation.
   * 
   * @param inputA
   *          The input integer 1
   * @param inputB
   *          The input integer 2
   */
  private void createGraphicObjects(int inputA, int inputB) {
    /*
     * Frame 1
     */
    // TextProperties headerProps = new TextProperties();
    // headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, header);
    // headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30), "Extended Euclidean Algorithm",
        "header", null, headerProps);

    TextProperties headerShadowProps = new TextProperties();
    headerShadowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, headerShadow);
    headerShadowProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    headerShadowProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newText(new Offset(-2, 3, "header", AnimalScript.DIRECTION_NW),
        "Extended Euclidean Algorithm", "headerShadow", null, headerShadowProps);

    // RectProperties headerRectProps = new RectProperties();
    // headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
    // Boolean.TRUE);
    // headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
    // headerBackground);
    // headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "headerRect",
        null, headerRectProps);

    lang.nextStep();

    /*
     * Frame 2 description and definition plus it's rectangles
     */
    SourceCodeProperties definitionProps = new SourceCodeProperties();
    definitionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 10));
    definitionProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        codeHighlight);
    definitionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(50,
        50, 50));
    SourceCode definition = lang.newSourceCode(new Offset(0, 30, "header",
        AnimalScript.DIRECTION_SW), "definition", null, definitionProps);

    definition
        .addCodeLine(
            "Beside calculating the greatest common devisor as the Euclidean Algorithm does, the Extended Euclidean Algorithm finds",
            null, 0, null);
    definition
        .addCodeLine(
            "two integers x and y that conform to \t a * x + b * y = gcd( a, b ) \t where a and b are the input integers.",
            null, 0, null);
    definition
        .addCodeLine(
            "Since gcd( a, b ) = 1, x is the multiplicative inverse of a modulo b and y is the multiplicative inverse of b modulo a.",
            null, 0, null);

    RectProperties definitionRectProps = new RectProperties();
    definitionRectProps
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    Rect definitionRect1 = lang.newRect(new Offset(-5, -5, "definition",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "definition",
        AnimalScript.DIRECTION_SE), "definitionRect1", null,
        definitionRectProps);

    Rect definitionRect2 = lang.newRect(new Offset(-3, -3, "definition",
        AnimalScript.DIRECTION_NW), new Offset(3, 3, "definition",
        AnimalScript.DIRECTION_SE), "definitionRect2", null,
        definitionRectProps);

    lang.nextStep();
    /*
     * Frame 2 b
     */

    SourceCodeProperties descriptionProps = new SourceCodeProperties();
    descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
    descriptionProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        codeHighlight);
    descriptionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeColor);
    SourceCode description = lang.newSourceCode(new Offset(10, 90, "header",
        AnimalScript.DIRECTION_SW), "description", new TicksTiming(0),
        descriptionProps);
    description.addCodeLine("1. Initialize x2, x1, y2, y1.", null, 0,
        new TicksTiming(30));
    description.addCodeLine("2. Check whether b > 0", null, 0, new TicksTiming(
        60));
    description.addCodeLine(
        "3. calculate q and r with q = a div b and r = a mod b", null, 0,
        new TicksTiming(90));
    description.addCodeLine(
        "4. calculate x and y with x = x2 - q*x1 and y = y2 - q*y1", null, 0,
        new TicksTiming(120));
    description.addCodeLine("5. set a = b and b = r", null, 0, new TicksTiming(
        150));
    description.addCodeLine("6. set x1 = x, x2 = x1", null, 0, new TicksTiming(
        180));
    description.addCodeLine("7. set y1 = y, y2 = y1", null, 0, new TicksTiming(
        210));
    description.addCodeLine(
        "8. If b = 0, x, y and the gcd conform to: a*x + b*y = gcd(a,b)", null,
        0, new TicksTiming(240));

    lang.newText(new Coordinates(0, 0), "", "delay", new TicksTiming(800));

    lang.nextStep("Description");

    /*
     * Frame 3 Source code, horizontal separator, term phrase, table header and
     * the row/column separator
     */

    description.hide();
    definition.hide();
    definitionRect1.hide();
    definitionRect2.hide();

    // SourceCodeProperties codeProps = new SourceCodeProperties();
    // codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // codeHighlight);
    // codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeColor);
    codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 12));
    SourceCode code = lang.newSourceCode(new Coordinates(20, 150), "code",
        null, codeProps);

    code.addCodeLine("public int[] extEuclid(int a, int b) {", null, 0, null);
    code.addCodeLine("int q, r, x, y;", null, 1, null);
    code.addCodeLine("int x2 = 1, x1 = 0; y2 = 0, y1 = 0;", null, 1, null);
    code.addCodeLine("while (b > 0) {", null, 1, null);
    code.addCodeLine("q = a / b;", null, 2, null);
    code.addCodeLine("r = a % b;", null, 2, null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("x = x2 - q * x1;", null, 2, null);
    code.addCodeLine("y = y2 - q * y1;", null, 2, null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("a = b;", null, 2, null);
    code.addCodeLine("b = r;", null, 2, null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("x2 = x1;", null, 2, null);
    code.addCodeLine("x1 = x;", null, 2, null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("y2 = y1;", null, 2, null);
    code.addCodeLine("y1 = y;", null, 2, null);
    code.addCodeLine("}", null, 1, null);
    code.addCodeLine("x = x2; y = y2; gcd = a;", null, 1, null);
    code.addCodeLine("return new int[]{x, y, gcd};", null, 1, null);
    code.addCodeLine("}", null, 0, null);

    PolylineProperties horizontalSeparatorProps = new PolylineProperties();
    horizontalSeparatorProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.black);
    lang.newPolyline(new Node[] {
        new Offset(20, -5, "code", AnimalScript.DIRECTION_NE),
        new Offset(20, 5, "code", AnimalScript.DIRECTION_SE) },
        "horizontalSeparator", null, horizontalSeparatorProps);

    /*
     * Calculate the max length of the two inputs a and b, and adds 1 that also
     * a negative value with the same length fits in each column of the table.
     * It should also possible to generate a table for the maximum value of an
     * integer.
     * 
     * Also generate the offset which is needed to stretch each column for the
     * two inputs and the number of rows which are necessary to build the table.
     */
    final int TABLEDEPTH = getRowCount(inputA, inputB);
    final int ROWHEIGHT = this.tableFontSize * 2;

    final int INPUTLENGTH = (("" + inputA).length() > ("" + inputB).length()) ? ("" + inputA)
        .length() + 1 : ("" + inputB).length() + 1;

    String offset = new String();
    for (int i = 0; i <= INPUTLENGTH / 10; i++)
      offset += "          "; // default offset length = 10 to add on the final
                              // offset

    final String COLUMN_OFFSET = offset;

    TextProperties tableHeaderProps1 = new TextProperties();
    tableHeaderProps1.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeColor);
    tableHeaderProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, tableFontSize));
    TextProperties tableHeaderProps2 = new TextProperties();
    tableHeaderProps2
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, codeHighlight);
    tableHeaderProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, tableFontSize));
    q = lang.newText(new Offset(55, -22, "horizontalSeparator",
        AnimalScript.DIRECTION_NE),
        ("Quotient".length() > INPUTLENGTH) ? "Quotient"
            : ("Quotient" + COLUMN_OFFSET).substring(0, INPUTLENGTH), // (substring
                                                                      // - after
                                                                      // beginind.
                                                                      // until
                                                                      // endind.)
        "q", null, tableHeaderProps1);
    r = lang.newText(new Offset(10, 0, "q", AnimalScript.DIRECTION_NE),
        ("Remainder".length() > INPUTLENGTH) ? "Remainder"
            : ("Remainder" + COLUMN_OFFSET).substring(0, INPUTLENGTH), // (substring
                                                                       // -
                                                                       // after
                                                                       // beginind.
                                                                       // until
                                                                       // endind.)
        "r", null, tableHeaderProps1);
    x = lang.newText(
        new Offset(10, 0, "r", AnimalScript.DIRECTION_NE),
        ("x".length() > INPUTLENGTH) ? "x" : ("x" + COLUMN_OFFSET).substring(0,
            INPUTLENGTH), // (substring - after beginind. until endind.)
        "x", null, tableHeaderProps2);
    y = lang.newText(
        new Offset(10, 0, "x", AnimalScript.DIRECTION_NE),
        ("y".length() > INPUTLENGTH) ? "y" : ("y" + COLUMN_OFFSET).substring(0,
            INPUTLENGTH), // (substring - after beginind. until endind.)
        "y", null, tableHeaderProps2);
    a = lang.newText(
        new Offset(10, 0, "y", AnimalScript.DIRECTION_NE),
        ("a".length() > INPUTLENGTH) ? "a" : ("a" + COLUMN_OFFSET).substring(0,
            INPUTLENGTH), // (substring - after beginind. until endind.)
        "a", null, tableHeaderProps2);
    b = lang.newText(
        new Offset(10, 0, "a", AnimalScript.DIRECTION_NE),
        ("b".length() > INPUTLENGTH) ? "b" : ("b" + COLUMN_OFFSET).substring(0,
            INPUTLENGTH), // (substring - after beginind. until endind.)
        "b", null, tableHeaderProps2);
    x2 = lang.newText(
        new Offset(10, 0, "b", AnimalScript.DIRECTION_NE),
        ("x2".length() > INPUTLENGTH) ? "x2" : ("x2" + COLUMN_OFFSET)
            .substring(0, INPUTLENGTH), // (substring - after beginind. until
                                        // endind.)
        "x2", null, tableHeaderProps1);
    x1 = lang.newText(
        new Offset(10, 0, "x2", AnimalScript.DIRECTION_NE),
        ("x1".length() > INPUTLENGTH) ? "x1" : ("x1" + COLUMN_OFFSET)
            .substring(0, INPUTLENGTH), // (substring - after beginind. until
                                        // endind.)
        "x1", null, tableHeaderProps1);
    y2 = lang.newText(
        new Offset(10, 0, "x1", AnimalScript.DIRECTION_NE),
        ("y2".length() > INPUTLENGTH) ? "y2" : ("y2" + COLUMN_OFFSET)
            .substring(0, INPUTLENGTH), // (substring - after beginind. until
                                        // endind.)
        "y2", null, tableHeaderProps1);
    y1 = lang.newText(
        new Offset(10, 0, "y2", AnimalScript.DIRECTION_NE),
        ("y1".length() > INPUTLENGTH) ? "y1" : ("y1" + COLUMN_OFFSET)
            .substring(0, INPUTLENGTH), // (substring - after beginind. until
                                        // endind.)
        "y1", null, tableHeaderProps1);

    tableH1 = lang.newPolyline(new Node[] {
        new Offset(-10, 5, "q", AnimalScript.DIRECTION_SW),
        new Offset(10, 5, "y1", AnimalScript.DIRECTION_SE) }, "tableH1", null);

    tableV1 = lang.newPolyline(new Node[] {
        new Offset(5, 0, "q", AnimalScript.DIRECTION_NE),
        new Offset(5, ROWHEIGHT * TABLEDEPTH + ROWHEIGHT / 2, "q",
            AnimalScript.DIRECTION_NE) }, "tableV1", null);
    tableV2 = lang.newPolyline(new Node[] {
        new Offset(5, 0, "r", AnimalScript.DIRECTION_NE),
        new Offset(5, ROWHEIGHT * TABLEDEPTH + ROWHEIGHT / 2, "r",
            AnimalScript.DIRECTION_NE) }, "tableV2", null);
    tableV3 = lang.newPolyline(new Node[] {
        new Offset(5, 0, "x", AnimalScript.DIRECTION_NE),
        new Offset(5, ROWHEIGHT * TABLEDEPTH + ROWHEIGHT / 2, "x",
            AnimalScript.DIRECTION_NE) }, "tableV3", null);
    tableV4 = lang.newPolyline(new Node[] {
        new Offset(5, 0, "y", AnimalScript.DIRECTION_NE),
        new Offset(5, ROWHEIGHT * TABLEDEPTH + ROWHEIGHT / 2, "y",
            AnimalScript.DIRECTION_NE) }, "tableV4", null);
    tableV5 = lang.newPolyline(new Node[] {
        new Offset(5, 0, "a", AnimalScript.DIRECTION_NE),
        new Offset(5, ROWHEIGHT * TABLEDEPTH + ROWHEIGHT / 2, "a",
            AnimalScript.DIRECTION_NE) }, "tableV5", null);
    tableV6 = lang.newPolyline(new Node[] {
        new Offset(5, 0, "b", AnimalScript.DIRECTION_NE),
        new Offset(5, ROWHEIGHT * TABLEDEPTH + ROWHEIGHT / 2, "b",
            AnimalScript.DIRECTION_NE) }, "tableV6", null);
    tableV7 = lang.newPolyline(new Node[] {
        new Offset(5, 0, "x2", AnimalScript.DIRECTION_NE),
        new Offset(5, ROWHEIGHT * TABLEDEPTH + ROWHEIGHT / 2, "x2",
            AnimalScript.DIRECTION_NE) }, "tableV7", null);
    tableV8 = lang.newPolyline(new Node[] {
        new Offset(5, 0, "x1", AnimalScript.DIRECTION_NE),
        new Offset(5, ROWHEIGHT * TABLEDEPTH + ROWHEIGHT / 2, "x1",
            AnimalScript.DIRECTION_NE) }, "tableV8", null);
    tableV9 = lang.newPolyline(new Node[] {
        new Offset(5, 0, "y2", AnimalScript.DIRECTION_NE),
        new Offset(5, ROWHEIGHT * TABLEDEPTH + ROWHEIGHT / 2, "y2",
            AnimalScript.DIRECTION_NE) }, "tableV9", null);

    TextProperties termProps1 = new TextProperties();
    termProps1.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeColor);
    termProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 17));
    TextProperties termProps2 = new TextProperties();
    termProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeHighlight);
    termProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 17));
    lang.newText(new Offset(100 + INPUTLENGTH, 20, "headerRect",
        AnimalScript.DIRECTION_NE), "a * ", "term1", null, termProps1);
    lang.newText(new Offset(0, 0, "term1", AnimalScript.DIRECTION_NE), "x ",
        "term2", null, termProps2);
    lang.newText(new Offset(0, 0, "term2", AnimalScript.DIRECTION_NE),
        "+ b * ", "term3", null, termProps1);
    lang.newText(new Offset(0, 0, "term3", AnimalScript.DIRECTION_NE), "y ",
        "term4", null, termProps2);
    lang.newText(new Offset(0, 0, "term4", AnimalScript.DIRECTION_NE),
        "= gcd( a, b ) ", "term5", null, termProps1);

    lang.nextStep();

    createEuclidTable(inputA, inputB, code, TABLEDEPTH, ROWHEIGHT);

  }

  private void createEuclidTable(int aInput, int bInput, SourceCode code,
      final int TABLEDEPTH, final int ROWHEIGHT) {

    int inputA = aInput, inputB = bInput;

    /**
     * Column count = 10, row count = TABLEDEPTH
     * {@link ExtEuclid#getRowCount(int, int)}
     */
    Text[][] table = new Text[TABLEDEPTH][10];

    int row = 0, column = 4;

    /*
     * Frame 5
     */

    code.highlight(0);

    TextProperties inputProps = new TextProperties();
    inputProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeHighlight);
    inputProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 14));
    Text inputa = lang.newText(new Offset(295, -90, "code",
        AnimalScript.DIRECTION_NW), ("a = " + inputA), "inputa", null,
        inputProps);
    Text inputb = lang.newText(new Offset(295, -65, "code",
        AnimalScript.DIRECTION_NW), ("b = " + inputB), "inputb", null,
        inputProps);

    RectProperties inputRectProps = new RectProperties();
    inputRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, header);
    Rect aRect = lang.newRect(new Offset(-3, -3, "inputa",
        AnimalScript.DIRECTION_NW), new Offset(3, 3, "inputa",
        AnimalScript.DIRECTION_SE), "aRect", null, inputRectProps);
    Rect bRect = lang.newRect(new Offset(-3, -3, "inputb",
        AnimalScript.DIRECTION_NW), new Offset(3, 3, "inputb",
        AnimalScript.DIRECTION_SE), "bRect", null, inputRectProps);

    PolylineProperties inputArrowProps = new PolylineProperties();
    inputArrowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, header);
    inputArrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);
    PolylineProperties inputArrowProps2 = new PolylineProperties();
    inputArrowProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, header);
    inputArrowProps2.set(AnimationPropertiesKeys.FWARROW_PROPERTY,
        Boolean.FALSE);
    PolylineProperties inputArrowProps3 = new PolylineProperties();
    inputArrowProps3.set(AnimationPropertiesKeys.COLOR_PROPERTY, header);
    inputArrowProps3
        .set(AnimationPropertiesKeys.BWARROW_PROPERTY, Boolean.TRUE);
    Polyline aArrow1 = lang.newPolyline(new Node[] {
        new Offset(0, 10, "aRect", AnimalScript.DIRECTION_NW),
        new Offset(-100, 10, "aRect", AnimalScript.DIRECTION_NW),
        new Offset(-100, 80, "aRect", AnimalScript.DIRECTION_NW) }, "aArrow1",
        new TicksTiming(200), inputArrowProps);
    Polyline aArrow2 = lang.newPolyline(new Node[] {
        new Offset(0, 10, "aRect", AnimalScript.DIRECTION_NE),
        new Offset(5, 33, "term1", AnimalScript.DIRECTION_SW),
        new Offset(5, 0, "term1", AnimalScript.DIRECTION_SW) }, "aArrow2",
        new TicksTiming(200), inputArrowProps2);
    Polyline aArrow3 = lang.newPolyline(new Node[] {
        new Offset(5, 33, "term1", AnimalScript.DIRECTION_SW),
        new Offset(5, -52, "a", AnimalScript.DIRECTION_NW),
        new Offset(5, 0, "a", AnimalScript.DIRECTION_NW) }, "aArrow3",
        new TicksTiming(200), inputArrowProps);

    Polyline bArrow1 = lang.newPolyline(new Node[] {
        new Offset(0, 10, "bRect", AnimalScript.DIRECTION_NW),
        new Offset(-52, 10, "bRect", AnimalScript.DIRECTION_NW),
        new Offset(-52, 57, "bRect", AnimalScript.DIRECTION_NW) }, "bArrow1",
        new TicksTiming(200), inputArrowProps);
    Polyline bArrow2 = lang.newPolyline(new Node[] {
        new Offset(0, 10, "bRect", AnimalScript.DIRECTION_NE),
        new Offset(25, 56, "term3", AnimalScript.DIRECTION_SW),
        new Offset(25, 0, "term3", AnimalScript.DIRECTION_SW) }, "bArrow2",
        new TicksTiming(200), inputArrowProps2);
    Polyline bArrow3 = lang.newPolyline(new Node[] {
        new Offset(25, 56, "term3", AnimalScript.DIRECTION_SW),
        new Offset(5, -29, "b", AnimalScript.DIRECTION_NW),
        new Offset(5, 0, "b", AnimalScript.DIRECTION_NW) }, "bArrow3",
        new TicksTiming(200), inputArrowProps);

    PolylineProperties xyArrowProps = new PolylineProperties();
    xyArrowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeColor);
    xyArrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);
    Polyline xArrow = lang.newPolyline(new Node[] {
        new Offset(5, 0, "term2", AnimalScript.DIRECTION_SW),
        new Offset(5, 13, "term2", AnimalScript.DIRECTION_SW),
        new Offset(5, -72, "x", AnimalScript.DIRECTION_NW),
        new Offset(5, -5, "x", AnimalScript.DIRECTION_NW) }, "xArrow",
        new TicksTiming(400), xyArrowProps);
    Polyline yArrow = lang.newPolyline(new Node[] {
        new Offset(5, 0, "term4", AnimalScript.DIRECTION_SW),
        new Offset(5, 13, "term4", AnimalScript.DIRECTION_SW),
        new Offset(5, -72, "y", AnimalScript.DIRECTION_NW),
        new Offset(5, -5, "y", AnimalScript.DIRECTION_NW) }, "yArrow",
        new TicksTiming(400), xyArrowProps);

    lang.nextStep();

    /*
     * Frame 6
     */

    if (inputB == 0) {
      // x and y are already found. x = 1 and y = 0
      // These results are just need to be displayed
    } else {
      int q, r, x, y;
      int x2 = 1, x1 = 0, y2 = 0, y1 = 1;
      code.unhighlight(0);
      code.highlight(2);

      TextProperties elemProps1 = new TextProperties(); // Properties for the
                                                        // highlighted columns
                                                        // (a, b, x, y)
      elemProps1.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeHighlight);
      elemProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.BOLD, 14));
      TextProperties elemProps2 = new TextProperties(); // Properties for the
                                                        // unhighlighted columns
      elemProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeColor);
      elemProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.BOLD, 14));

      // Initialize first row in table
      table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
          "a", AnimalScript.DIRECTION_NW), ("" + inputA),
          ("elem" + row + "" + column), null, elemProps1);
      column++;
      table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
          "b", AnimalScript.DIRECTION_NW), ("" + inputB),
          ("elem" + row + "" + column), null, elemProps1);
      column++;

      table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
          "x2", AnimalScript.DIRECTION_NW), ("" + x2),
          ("elem" + row + "" + column), null, elemProps2);
      column++;
      table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
          "x1", AnimalScript.DIRECTION_NW), ("" + x1),
          ("elem" + row + "" + column), null, elemProps2);
      column++;
      table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
          "y2", AnimalScript.DIRECTION_NW), ("" + y2),
          ("elem" + row + "" + column), null, elemProps2);
      column++;
      table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
          "y1", AnimalScript.DIRECTION_NW), ("" + y1),
          ("elem" + row + "" + column), null, elemProps2);
      column++;

      lang.nextStep("init");

      /*
       * Frame 7
       */
      code.unhighlight(1);
      code.unhighlight(2);
      row++;
      column = 0;

      /*
       * Frames 8 until Frame X, when the loop and all steps of the algorithm
       * are done.
       */
      while (inputB > 0) {

        code.highlight(3);
        code.unhighlight(17);
        lang.nextStep("step 2");

        q = inputA / inputB;
        table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
            "q", AnimalScript.DIRECTION_NW), ("" + q),
            ("elem" + row + "" + column), null, elemProps2);
        column++;
        code.unhighlight(3);
        code.highlight(4);
        lang.nextStep();

        r = inputA % inputB;
        table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
            "r", AnimalScript.DIRECTION_NW), ("" + r),
            ("elem" + row + "" + column), null, elemProps2);
        column++;
        code.unhighlight(4);
        code.highlight(5);
        lang.nextStep("step 3");

        x = x2 - q * x1;
        table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
            "x", AnimalScript.DIRECTION_NW), ("" + x),
            ("elem" + row + "" + column), null, elemProps1);
        column++;
        code.unhighlight(5);
        code.highlight(7);
        lang.nextStep();

        y = y2 - q * y1;
        table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
            "y", AnimalScript.DIRECTION_NW), ("" + y),
            ("elem" + row + "" + column), null, elemProps1);
        column++;
        code.unhighlight(7);
        code.highlight(8);
        lang.nextStep("step 4");

        inputA = inputB;
        table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
            "a", AnimalScript.DIRECTION_NW), ("" + inputA),
            ("elem" + row + "" + column), null, elemProps1);
        column++;
        code.unhighlight(8);
        code.highlight(10);
        lang.nextStep();

        inputB = r;
        table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
            "b", AnimalScript.DIRECTION_NW), ("" + inputB),
            ("elem" + row + "" + column), null, elemProps1);
        column++;
        code.unhighlight(10);
        code.highlight(11);
        lang.nextStep("Step 5");

        x2 = x1;
        table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
            "x2", AnimalScript.DIRECTION_NW), ("" + x2),
            ("elem" + row + "" + column), null, elemProps2);
        column++;
        code.unhighlight(11);
        code.highlight(13);
        lang.nextStep();

        x1 = x;
        table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
            "x1", AnimalScript.DIRECTION_NW), ("" + x1),
            ("elem" + row + "" + column), null, elemProps2);
        column++;
        code.unhighlight(13);
        code.highlight(14);
        lang.nextStep("step 6");

        y2 = y1;
        table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
            "y2", AnimalScript.DIRECTION_NW), ("" + y2),
            ("elem" + row + "" + column), null, elemProps2);
        column++;
        code.unhighlight(14);
        code.highlight(16);
        lang.nextStep();

        y1 = y;
        table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
            "y1", AnimalScript.DIRECTION_NW), ("" + y1),
            ("elem" + row + "" + column), null, elemProps2);
        column++;
        code.unhighlight(16);
        code.highlight(17);
        lang.nextStep("step 7");

        // Last entry was made, continue with next row and first column
        row++;
        column = 0;
      }

      code.unhighlight(17);
      code.highlight(3);

      lang.nextStep();

      /*
       * Frame X + 1
       */
      column = 2;

      x = x2;
      table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
          "x", AnimalScript.DIRECTION_NW), ("" + x),
          ("elem" + row + "" + column), null, elemProps1);
      column++;

      y = y2;
      table[row][column] = lang.newText(new Offset(0, (row + 1) * ROWHEIGHT,
          "y", AnimalScript.DIRECTION_NW), ("" + y),
          ("elem" + row + "" + column), null, elemProps1);

      code.unhighlight(3);
      code.highlight(19);

      lang.nextStep();

      code.unhighlight(19);
      code.highlight(20);

      aArrow1.hide();
      aArrow2.hide();
      aArrow3.hide();
      bArrow1.hide();
      bArrow2.hide();
      bArrow3.hide();

      TextProperties resultProps = new TextProperties();
      resultProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, resultColor);
      resultProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.BOLD, 24));

      Text resultY = lang.newText(new Offset(50, 60, "tableV4",
          AnimalScript.DIRECTION_SE), ("y = " + y), "resultY", new TicksTiming(
          200), resultProps);
      Text resultX = lang.newText(new Offset(0, 10, "resultY",
          AnimalScript.DIRECTION_SW), ("x = " + x), "resultX", new TicksTiming(
          200), resultProps);
      Text resultGcd = lang.newText(new Offset(30, -29, "resultY",
          AnimalScript.DIRECTION_NE), ("gcd( a, b ) = " + inputA), "resultGcd",
          new TicksTiming(200), resultProps);

      RectProperties resultRectProps = new RectProperties();
      resultRectProps
          .set(AnimationPropertiesKeys.COLOR_PROPERTY, codeHighlight);
      Rect resultXRect = lang.newRect(new Offset(-3, -3, "resultX",
          AnimalScript.DIRECTION_NW), new Offset(3, 3, "resultX",
          AnimalScript.DIRECTION_SE), "resultXRect", new TicksTiming(200),
          resultRectProps);
      Rect resultYRect = lang.newRect(new Offset(-3, -3, "resultY",
          AnimalScript.DIRECTION_NW), new Offset(3, 3, "resultY",
          AnimalScript.DIRECTION_SE), "resultYRect", new TicksTiming(200),
          resultRectProps);
      Rect resultGcdRect = lang.newRect(new Offset(-3, -3, "resultGcd",
          AnimalScript.DIRECTION_NW), new Offset(3, 3, "resultGcd",
          AnimalScript.DIRECTION_SE), "resultGcdRect", new TicksTiming(200),
          resultRectProps);

      PolylineProperties resultArrowProps = new PolylineProperties();
      resultArrowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeColor);
      resultArrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY,
          Boolean.TRUE);

      Polyline resultArrowY = lang.newPolyline(new Node[] {
          new Offset(5, 0, ("elem" + row + "" + column),
              AnimalScript.DIRECTION_SW),
          new Offset(5, 64, ("elem" + row + "" + column),
              AnimalScript.DIRECTION_SW),
          new Offset(0, 16, "resultYRect", AnimalScript.DIRECTION_NW) },
          "resultArrowY", new TicksTiming(200), resultArrowProps);
      Polyline resultArrowX = lang.newPolyline(new Node[] {
          new Offset(5, 0, ("elem" + row + "" + (column - 1)),
              AnimalScript.DIRECTION_SW),
          new Offset(5, 99, ("elem" + row + "" + (column - 1)),
              AnimalScript.DIRECTION_SW),
          new Offset(0, 16, "resultXRect", AnimalScript.DIRECTION_NW) },
          "resultArrowX", new TicksTiming(200), resultArrowProps);
      Polyline resultArrowGcd = lang.newPolyline(new Node[] {
          new Offset(5, ROWHEIGHT, ("elem" + (row - 1) + "" + (column + 1)),
              AnimalScript.DIRECTION_SW),
          new Offset(5, 34 + ROWHEIGHT,
              ("elem" + (row - 1) + "" + (column + 1)),
              AnimalScript.DIRECTION_SW),
          new Offset(0, 15, "resultGcdRect", AnimalScript.DIRECTION_NW) },
          "resultArrowGcd", new TicksTiming(200), resultArrowProps);

      lang.nextStep("step 8");

      /*
       * Frame X + 2
       */

      for (int i = 0; i < table.length; i++)
        for (int j = 0; j < table[0].length; j++) {
          if (table[i][j] instanceof Text)
            table[i][j].hide();
        }
      this.q.hide();
      this.r.hide();
      this.x.hide();
      this.y.hide();
      this.a.hide();
      this.b.hide();
      this.x2.hide();
      this.x1.hide();
      this.y2.hide();
      this.y1.hide();

      tableV1.hide();
      tableV2.hide();
      tableV3.hide();
      tableV4.hide();
      tableV5.hide();
      tableV6.hide();
      tableV7.hide();
      tableV8.hide();
      tableV9.hide();
      tableH1.hide();

      xArrow.hide();
      yArrow.hide();
      resultArrowX.hide();
      resultArrowY.hide();

      resultXRect.hide();
      resultYRect.hide();
      aRect.hide();
      bRect.hide();

      resultGcdRect.hide();
      resultArrowGcd.hide();

      inputa.changeColor("color", resultColor, null, null);
      inputb.changeColor("color", resultColor, null, null);

      lang.nextStep();

      /*
       * Frame X + 3
       */
      TextProperties resultTermProps = new TextProperties();
      resultTermProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, resultColor);
      resultTermProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "SansSerif", Font.PLAIN, 24)); // Default value for list elements is
                                         // SansSerif 16

      lang.newText(new Offset(180, 100, "horizontalSeparator",
          AnimalScript.DIRECTION_NE), "" + ExtEuclid.inputA, "resultTermA",
          new TicksTiming(300), resultTermProps);
      lang.newText(new Offset(5, 0, "resultTermA", AnimalScript.DIRECTION_NE),
          " * ", "resultTermMul1", new TicksTiming(300), resultTermProps);
      lang.newText(
          new Offset(5, 0, "resultTermMul1", AnimalScript.DIRECTION_NE),
          (x > 0) ? ("" + x) : ("(" + x + ")"), "resultTermX", new TicksTiming(
              300), resultTermProps);
      lang.newText(new Offset(5, 0, "resultTermX", AnimalScript.DIRECTION_NE),
          " + ", "resultTermPlus", new TicksTiming(300), resultTermProps);
      lang.newText(
          new Offset(5, 0, "resultTermPlus", AnimalScript.DIRECTION_NE), ""
              + ExtEuclid.inputB, "resultTermB", new TicksTiming(300),
          resultTermProps);
      lang.newText(new Offset(5, 0, "resultTermB", AnimalScript.DIRECTION_NE),
          " * ", "resultTermMul2", new TicksTiming(300), resultTermProps);
      lang.newText(
          new Offset(5, 0, "resultTermMul2", AnimalScript.DIRECTION_NE),
          (y > 0) ? ("" + y) : ("(" + y + ")"), "resultTermY", new TicksTiming(
              300), resultTermProps);
      lang.newText(new Offset(5, 0, "resultTermY", AnimalScript.DIRECTION_NE),
          " = ", "resultTermEquals", new TicksTiming(300), resultTermProps);
      lang.newText(new Offset(5, 0, "resultTermEquals",
          AnimalScript.DIRECTION_NE), "gcd( a, b ) = " + inputA,
          "resultTermGcd", new TicksTiming(300), resultTermProps);

      resultGcd.hide(new TicksTiming(300));
      resultX.hide(new TicksTiming(300));
      resultXRect.hide(new TicksTiming(300));
      resultY.hide(new TicksTiming(300));
      resultYRect.hide(new TicksTiming(300));
      inputa.hide(new TicksTiming(300));
      aRect.hide(new TicksTiming(300));
      inputb.hide(new TicksTiming(300));
      bRect.hide(new TicksTiming(300));

      lang.nextStep();

      /*
       * Frame X + 4
       */
      code.unhighlight(20);

      resultTermProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "SansSerif", Font.PLAIN, 14)); // Default value for list elements is
                                         // SansSerif 16

      lang.newText(
          new Offset(-20, 30, "resultTermA", AnimalScript.DIRECTION_SW),
          "A valid solution was found for the two input integers a and b. ",
          "resultText", new TicksTiming(300), resultTermProps);
      lang.newText(new Offset(0, 3, "resultText", AnimalScript.DIRECTION_SW),
          "Now the integer x = " + x + " and y = " + y + " solve the term.",
          "resultText", new TicksTiming(300), resultTermProps);

      TriangleProperties triangleProps = new TriangleProperties();
      triangleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, resultColor);
      lang.newTriangle(new Offset(-30, 0, "resultText",
          AnimalScript.DIRECTION_NW), new Offset(-30, 0, "resultText",
          AnimalScript.DIRECTION_SW), new Offset(-16, 8, "resultText",
          AnimalScript.DIRECTION_NW), "resultTriangle", new TicksTiming(300),
          triangleProps);

      lang.nextStep("Conclusion");
    }

  }

  /**
   * Using the Euclidean Algorithm this method calculates the number of loops,
   * which are necessary to calculate the gcd of the two inputs a and b.
   * 
   * @param theA
   *          Input a
   * @param theB
   *          Input b
   * @return The number of operations, where one op. stands for the whole loop
   *         content.
   */
  private int getRowCount(int theA, int theB) {
    int a = theA, b = theB;
//    int q = 0;
    int r = 0, rowcount = 2; // initial value is 2, because of the header
                                    // and the
    // last line for the result

    while (b > 0) {
//      q = a / b;
      r = a % b;
      a = b;
      b = r;
      rowcount++;
    }
    return rowcount;
  }
}
