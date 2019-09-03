/**
 * Midpoint Circle Algorithm (Aufgabe 5.2), Annotated revision:
 * 
 * - The algorithm rasters the circle with a given radius, point size and x0, y0 values
 *   as its center coordinates;
 * - Included MidpointCircleAlgorithm.xml file with updated options, where it's possible
 *   to tune the most properties of the animation such as the radius of the circle in points,
 *   the size of points in pixels, but also various visual properties.
 * - New .xml file represents now a better input logic for involved variables.
 * - Input variables are not limited in their values.
 */

package generators.graphics;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class MidpointCircleAlgorithm implements Generator {

  private CircleProperties     Circle;
  private PolylineProperties   RadiusPolyline;
  private RectProperties       CurrentPixel;
  private RectProperties       DynPixel;
  private CircleProperties     AngleEndpoint;
  private CircleProperties     CircleCenter;
  private TextProperties       DynTextProps;
  private SourceCodeProperties SourceCode;

  // inputs: x0, y0, radius, point_size.
  int                          x0            = 750;
  int                          y0            = 250;
  int                          radius        = 7;
  int                          size          = 20;

  int                          octant_points = 0;

  // dyn radius polyline.
  Polyline                     dyn_radius;

  // strings
  private final String         asHEADER      = "Midpoint Circle Algorithm";
  // private final String AUTHOR = "Viktor Kolokolov @ TUD2011";

  private Language             lang;
  private Text                 header;
  private SourceCode           code;
  TicksTiming                  defaultTiming;

  public void init() {
    lang = new AnimalScript("Midpoint Circle Algorithm [EN]",
        "Viktor Kolokolov", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();

    Circle = (CircleProperties) props.getPropertiesByName("CircleProps");
    RadiusPolyline = (PolylineProperties) props
        .getPropertiesByName("RadiusPolyline");
    CurrentPixel = (RectProperties) props.getPropertiesByName("CurrentPixel");
    DynPixel = (RectProperties) props.getPropertiesByName("DynPixel");
    AngleEndpoint = (CircleProperties) props
        .getPropertiesByName("AngleEndpoint");
    CircleCenter = (CircleProperties) props.getPropertiesByName("CircleCenter");
    SourceCode = (SourceCodeProperties) props.getPropertiesByName("SourceCode");
    DynTextProps = (TextProperties) props.getPropertiesByName("DynTextProps");

    radius = (Integer) primitives.get("radius");
    size = (Integer) primitives.get("point_size");
    x0 = (Integer) primitives.get("x0");
    y0 = (Integer) primitives.get("y0");
    octant_points = 0;

    gen_animalscript();
    // System.out.println(mca.lang.getAnimationCode());
    return lang.toString();
  }

  public void gen_animalscript() {
    defaultTiming = new TicksTiming(25);
    lang.setStepMode(true);
    header = gen_header();
    gen_header_box();
    gen_mca_pc();
    /**
     * Midpoint circle algorithm: mca
     */
    mca(x0, y0, radius * size);
    epilog();
  }

  private Text gen_header() {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    return lang.newText(new Coordinates(20, 20), asHEADER, "title", null,
        textProperties);
  }

  private Rect gen_header_box() {
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    return lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "titleFrame",
        null, rectProperties);
  }

  private void mca(int l_x0, int l_y0, int l_radius) {

    Text error = lang.newText(new Coordinates(250, 350), "Error f: ", "Error",
        null, DynTextProps);
    Text xstr = lang.newText(new Coordinates(250, 375), "X: ", "Error", null,
        DynTextProps);
    Text ystr = lang.newText(new Coordinates(250, 400), "Y: ", "Error", null,
        DynTextProps);
    Text astr = lang.newText(new Coordinates(250, 425), "Angle: ", "Angle",
        null, DynTextProps);

    double prev_degrees = 0;

    // Initial error
    int f = 1 - l_radius / size;
    error.setText("Error f: " + f, null, null);
    code.highlight(3);
    lang.nextStep();

    int ddF_x = 1;
    int ddF_y = -2 * l_radius / size;
    code.highlight(4);
    code.unhighlight(3);
    lang.nextStep();

    int y = 0;
    int x = l_radius / size;
    xstr.setText("X: " + x, null, null);
    ystr.setText("Y: " + y, null, null);
    code.highlight(5);
    code.unhighlight(4);
    lang.nextStep();

    int xtmp0;
    int ytmp0;
    int xtmp1;
    int ytmp1;
    int xtmp2;
    int ytmp2;
    int xtmp3;
    int ytmp3;
    int xtmp4;
    int ytmp4;
    int xtmp5;
    int ytmp5;
    int xtmp6;
    int ytmp6;
    int xtmp7;
    int ytmp7;

    lang.newCircle(new Coordinates(l_x0, l_y0), l_radius, "iCircle", null,
        Circle);

    Node[] bPolyline = new Node[2];
    bPolyline[0] = new Coordinates(l_x0, l_y0);
    bPolyline[1] = new Coordinates(l_x0 + l_radius, l_y0);
    Polyline r = lang.newPolyline(bPolyline, "radius", null, RadiusPolyline);

    Circle c = lang.newCircle(new Coordinates(l_x0, l_y0), 2, "", null,
        CircleCenter);
    Circle per = lang.newCircle(new Coordinates(l_x0 + l_radius, l_y0), 2, "",
        null, AngleEndpoint);
    Node center = c.getCenter();

    lang.newRect(new Coordinates(l_x0 + l_radius - (size / 2), l_y0
        - (size / 2)), new Coordinates(l_x0 + l_radius + (size / 2), l_y0
        + (size / 2)), "dync", null, CurrentPixel);
    lang.newRect(new Coordinates(l_x0 - l_radius - (size / 2), l_y0
        - (size / 2)), new Coordinates(l_x0 - l_radius + (size / 2), l_y0
        + (size / 2)), "dync", null, CurrentPixel);
    code.highlight(6);
    code.unhighlight(5);
    lang.nextStep();
    lang.newRect(new Coordinates(l_x0 - (size / 2), l_y0 + l_radius
        - (size / 2)), new Coordinates(l_x0 + (size / 2), l_y0 + l_radius
        + (size / 2)), "dync", null, CurrentPixel);
    lang.newRect(new Coordinates(l_x0 - (size / 2), l_y0 - l_radius
        - (size / 2)), new Coordinates(l_x0 + (size / 2), l_y0 - l_radius
        + (size / 2)), "dync", null, CurrentPixel);
    code.highlight(7);
    code.unhighlight(6);
    lang.nextStep();

    Rect cur_pixel = lang.newRect(new Coordinates(l_x0 + l_radius - (size / 2),
        l_y0 - (size / 2)), new Coordinates(l_x0 + l_radius + (size / 2), l_y0
        + (size / 2)), "dync", null, DynPixel);
    xtmp0 = l_x0 + l_radius - (size / 2);
    ytmp0 = l_y0 - (size / 2);
    xtmp1 = l_x0 - (size / 2);
    ytmp1 = l_y0 - l_radius - (size / 2);
    xtmp2 = l_x0 - l_radius - (size / 2);
    ytmp2 = l_y0 - (size / 2);
    xtmp3 = l_x0 - (size / 2);
    ytmp3 = l_y0 + l_radius - (size / 2);
    xtmp4 = l_x0 - l_radius - (size / 2);
    ytmp4 = l_y0 - (size / 2);
    xtmp5 = l_x0 - (size / 2);
    ytmp5 = l_y0 - l_radius - (size / 2);
    xtmp6 = l_x0 + l_radius - (size / 2);
    ytmp6 = l_y0 - (size / 2);
    xtmp7 = l_x0 - (size / 2);
    ytmp7 = l_y0 + l_radius - (size / 2);
    code.unhighlight(7);

    while (x - 1 > Math.abs(y)) {
      code.highlight(8);
      lang.nextStep();
      // ddF_x = 2 * x + 1;
      // ddF_y = -2 * y;
      // f = x*x + y*y - radius*radius + 2*x - y + 1;
      code.highlight(9);
      code.unhighlight(8);
      if (f > 0) {
        error.setText("Error f: " + f, null, null);
        lang.nextStep();

        code.highlight(10);
        code.unhighlight(9);
        x--;
        xstr.setText("X: " + x, null, null);
        cur_pixel.moveBy(null, -size, 0, null, new TicksTiming(150));
        per.moveBy(null, -size, 0, null, new TicksTiming(150));
        xtmp0 = xtmp0 - size;
        ytmp1 = ytmp1 + size;
        xtmp2 = xtmp2 + size;
        ytmp3 = ytmp3 - size;
        xtmp4 = xtmp4 + size;
        ytmp5 = ytmp5 + size;
        xtmp6 = xtmp6 - size;
        ytmp7 = ytmp7 - size;
        lang.nextStep();

        code.highlight(11);
        code.unhighlight(10);
        ddF_y += 2;
        lang.nextStep();

        code.highlight(12);
        code.unhighlight(11);
        f += ddF_y;
        error.setText("Error f: " + f, null, null);
      }
      lang.nextStep();
      code.highlight(14);
      code.unhighlight(12);
      code.unhighlight(9);
      cur_pixel.moveBy(null, 0, -size, null, new TicksTiming(150));
      per.moveBy(null, 0, -size, null, new TicksTiming(150));
      ytmp0 = ytmp0 - size;
      xtmp1 = xtmp1 + size;
      ytmp2 = ytmp2 - size;
      xtmp3 = xtmp3 + size;
      ytmp4 = ytmp4 + size;
      xtmp5 = xtmp5 - size;
      ytmp6 = ytmp6 + size;
      xtmp7 = xtmp7 - size;
      y--;
      ystr.setText("Y: " + y, null, null);

      int adjacent = xtmp0 - x0 + (size / 2);
      int oposite = y0 - (size / 2) - ytmp0;
      double hypotenuse = Math.sqrt(Math.pow(adjacent, 2)
          + Math.pow(oposite, 2));
      double cos = adjacent / hypotenuse;
      double radians = Math.acos(cos);
      double degrees = (radians * 180 / Math.PI);
      astr.setText(
          "Angle: " + degrees + " ~= " + (int) (int) Math.round(degrees)
              + "degrees", null, null);
      r.rotate(center,
          (int) Math.round(degrees) - (int) Math.round(prev_degrees), null,
          new TicksTiming(150));
      prev_degrees = degrees;
      lang.nextStep();

      code.highlight(15);
      code.unhighlight(14);
      ddF_x += 2;
      lang.nextStep();

      code.highlight(16);
      code.unhighlight(15);
      f += ddF_x;
      error.setText("Error f: " + f, null, null);
      lang.nextStep();

      code.highlight(17);
      code.highlight(18);
      code.highlight(19);
      code.highlight(20);
      code.highlight(21);
      code.highlight(22);
      code.highlight(23);
      code.highlight(24);
      code.unhighlight(16);
      lang.newRect(new Coordinates(xtmp0, ytmp0), new Coordinates(xtmp0
          + (size), ytmp0 + (size)), "dync", null, CurrentPixel);
      lang.newRect(new Coordinates(xtmp1, ytmp1), new Coordinates(xtmp1
          + (size), ytmp1 + (size)), "dync", null, CurrentPixel);
      lang.newRect(new Coordinates(xtmp2, ytmp2), new Coordinates(xtmp2
          + (size), ytmp2 + (size)), "dync", null, CurrentPixel);
      lang.newRect(new Coordinates(xtmp3, ytmp3), new Coordinates(xtmp3
          + (size), ytmp3 + (size)), "dync", null, CurrentPixel);
      lang.newRect(new Coordinates(xtmp4, ytmp4), new Coordinates(xtmp4
          + (size), ytmp4 + (size)), "dync", null, CurrentPixel);
      lang.newRect(new Coordinates(xtmp5, ytmp5), new Coordinates(xtmp5
          + (size), ytmp5 + (size)), "dync", null, CurrentPixel);
      lang.newRect(new Coordinates(xtmp6, ytmp6), new Coordinates(xtmp6
          + (size), ytmp6 + (size)), "dync", null, CurrentPixel);
      lang.newRect(new Coordinates(xtmp7, ytmp7), new Coordinates(xtmp7
          + (size), ytmp7 + (size)), "dync", null, CurrentPixel);
      octant_points++;
      lang.nextStep();
      code.unhighlight(17);
      code.unhighlight(18);
      code.unhighlight(19);
      code.unhighlight(20);
      code.unhighlight(21);
      code.unhighlight(22);
      code.unhighlight(23);
      code.unhighlight(24);
    }
  }

  // creates pseudo-code for mca.
  private void gen_mca_pc() {
    // create prolog: coordinates, name, display options, default properties
    // now, create the prolog entity
    SourceCode prolog = lang.newSourceCode(new Coordinates(15, 40), "prolog",
        null, SourceCode);

    // add the lines to the prolog object.
    // line, name, indentation, display delay
    prolog
        .addCodeLine(
            "In computer graphics, the midpoint circle algorithm is an algorithm used to determine the points needed for drawing a circle.",
            null, 0, null); // 0
    prolog
        .addCodeLine(
            "The algorithm is a variant of Bresenham's line algorithm, and is thus sometimes known as Bresenham's circle algorithm,",
            null, 0, null);
    prolog
        .addCodeLine(
            "although not actually invented by Bresenham. The algorithm is related to the work by Pitteway and Van Aken.",
            null, 0, null);
    prolog.addCodeLine(" ", null, 0, null);
    prolog
        .addCodeLine(
            "The algorithm starts accordingly with the circle equation x^2 + y^2 = r^2. The center of the circle is located at (0,0).",
            null, 0, null);
    prolog
        .addCodeLine(
            "It considers only the first octant and draws a curve which starts at point (r,0) and proceeds upwards and to the left,",
            null, 0, null);
    prolog
        .addCodeLine(
            "reaching the angle of approx. 45 degree. The fast direction here is the y direction. The algorithm always does a step in the positive",
            null, 0, null);
    prolog
        .addCodeLine(
            "y direction (upwards), and every now and then also has to do a step in the slow direction, the negative x direction.",
            null, 0, null);
    prolog
        .addCodeLine(
            "Additionally the algorithm needs to add the midpoint coordinates when setting a pixel.",
            null, 0, null);

    code = lang
        .newSourceCode(new Coordinates(15, 40), "code", null, SourceCode);
    code.addCodeLine("Input: (x0,y0) = " + "(" + x0 + "," + y0 + ")" + "; "
        + "radius = " + radius + "points; " + "point_size = " + size
        + "pixels.", null, 0, null);
    code.addCodeLine(" ", null, 1, null);
    code.addCodeLine("void rasterCircle(int x0, int y0, int radius){", null, 0,
        null);
    code.addCodeLine("int f = 1 - radius;", null, 1, null);
    code.addCodeLine("int ddF_x = 1; int ddF_y = -2 * radius;", null, 1, null);
    code.addCodeLine("int x = 0; int y = radius;", null, 1, null);
    code.addCodeLine("setPixel(x0 + radius, y0); setPixel(x0 - radius, y0);",
        null, 1, null);
    code.addCodeLine("setPixel(x0, y0 + radius); setPixel(x0, y0 - radius);",
        null, 1, null);
    code.addCodeLine("while(x-1 > |y|){", null, 1, null);
    code.addCodeLine("if(f > 0){", null, 2, null);
    code.addCodeLine("x--;", null, 3, null);
    code.addCodeLine("ddF_y += 2;", null, 3, null);
    code.addCodeLine("f += ddF_y;", null, 3, null);
    code.addCodeLine("}", null, 2, null);
    code.addCodeLine("y--;", null, 2, null);
    code.addCodeLine("ddF_x += 2;", null, 2, null);
    code.addCodeLine("f += ddF_x;", null, 2, null);
    code.addCodeLine("setPixel(x0 + x, y0 + y);", null, 2, null);
    code.addCodeLine("setPixel(x0 - x, y0 + y);", null, 2, null);
    code.addCodeLine("setPixel(x0 + x, y0 - y);", null, 2, null);
    code.addCodeLine("setPixel(x0 - x, y0 - y);", null, 2, null);
    code.addCodeLine("setPixel(x0 + y, y0 + x);", null, 2, null);
    code.addCodeLine("setPixel(x0 - y, y0 + x);", null, 2, null);
    code.addCodeLine("setPixel(x0 + y, y0 - x);", null, 2, null);
    code.addCodeLine("setPixel(x0 - y, y0 - x);", null, 2, null);
    code.addCodeLine("}", null, 1, null);
    code.addCodeLine("}", null, 0, null);

    SourceCode func_desc = lang.newSourceCode(new Coordinates(15, 200),
        "func_desc", null, SourceCode);
    func_desc.addCodeLine("Remarks:", null, 0, null);
    func_desc
        .addCodeLine(
            "There is a correlation between this algorithm and the sum of the first N odd numbers,",
            null, 0, null);
    func_desc
        .addCodeLine(
            "thus comparing the sum of N odd numbers to this algorithm we have following computations:",
            null, 0, null);
    func_desc.addCodeLine(" ", null, 0, null);
    func_desc.addCodeLine("f = - radius + 1; //Initial Error;", null, 0, null);
    func_desc
        .addCodeLine(
            "ddF_x = 1; //Since the difference between two consecutive odd numbers is 2.",
            null, 0, null);
    func_desc
        .addCodeLine(
            "ddF_y = -2 * radius; //Connected to the last member of sum of N odd numbers.",
            null, 0, null);
    func_desc.addCodeLine(
        "f += ddF_y; //Adding odd numbers from Nth to the 1st.", null, 0, null);
    func_desc.addCodeLine(
        "f += ddF_x; //Adding odd numbers from the 1st to Nth.", null, 0, null);

    func_desc.highlight(0);
    code.hide();
    lang.nextStep();

    code.show();
    func_desc.hide();
    prolog.hide();

    lang.nextStep();
  }

  // finalizes the animation by presenting results
  private void epilog() {
    code.highlight(8);
    lang.nextStep();
    code.unhighlight(8);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    Text result = lang.newText(new Coordinates(50, 485), "Result: ", "Result",
        null, textProps);
    result.setText("Result: ", null, null);
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    Text result_c = lang.newText(new Offset(55, 0, result,
        AnimalScript.DIRECTION_NW), "Result: ", "Result", null, textProps);
    result_c.setText("there has been " + octant_points
        + " points rasterized in the first octant!", null, null);
    lang.nextStep();
    Text result_c2 = lang.newText(new Offset(0, 20, result,
        AnimalScript.DIRECTION_NW), " ", "R", null, textProps);
    result_c2
        .setText(
            "The algorithm processes only the first octant and calculates points of other octants from the symmetry of the circle.",
            null, null);
    lang.nextStep();
  }

  private String desc        = "In computer graphics, the midpoint circle algorithm is an algorithm used to determine the points needed for drawing a circle.\n"
                                 + "The algorithm is a variant of Bresenham's line algorithm, and is thus sometimes known as Bresenham's circle algorithm,\n"
                                 + "although not actually invented by Bresenham. The algorithm is related to the work by Pitteway and Van Aken.\n"
                                 + "The algorithm starts accordingly with the circle equation x^2 + y^2 = r^2. The center of the circle is located at (0,0).\n"
                                 + " \n"
                                 + "It considers only the first octant and draws a curve which starts at point (r,0) and proceeds upwards and to the left,\n"
                                 + "reaching the angle of 45 degree. The fast direction here is the y direction. The algorithm always does a step in the positive\n"
                                 + "y direction (upwards), and every now and then also has to do a step in the slow direction, the negative x direction.\n"
                                 + "Additionally the algorithm needs to add the midpoint coordinates when setting a pixel. NOTE: the radius of the circle is given in points\n"
                                 + "and the size of points is given in pixels.\n"
                                 + " \n";

  private String pseudo_code = "void rasterCircle(int x0, int y0, int radius){\n"
                                 + "\t int f = 1 - radius;\n"
                                 + "\t int ddF_x = 1; int ddF_y = -2 * radius;\n"
                                 + "\t int x = 0; int y = radius;\n"
                                 + "\t setPixel(x0 + radius, y0); setPixel(x0 - radius, y0);\n"
                                 + "\t setPixel(x0, y0 + radius); setPixel(x0, y0 - radius);\n"
                                 + "\t while(x-1 > |y|){\n"
                                 + "\t\t if(f > 0){\n"
                                 + "\t\t x--;\n"
                                 + "\t\t ddF_y += 2;\n"
                                 + "\t\t f += ddF_y;\n"
                                 + "\t }\n"
                                 + "\t y--;\n"
                                 + "\t ddF_x += 2;\n"
                                 + "\t f += ddF_x;\n"
                                 + "\t setPixel(x0 + x, y0 + y);\n"
                                 + "\t setPixel(x0 - x, y0 + y);\n"
                                 + "\t setPixel(x0 + x, y0 - y);\n"
                                 + "\t setPixel(x0 - x, y0 - y);\n"
                                 + "\t setPixel(x0 + y, y0 + x);\n"
                                 + "\t setPixel(x0 - y, y0 + x);\n"
                                 + "\t setPixel(x0 + y, y0 - x);\n"
                                 + "\t setPixel(x0 - y, y0 - x);\n"
                                 + "\t }\n"
                                 + " }";

  @Override
  public String getAlgorithmName() {
    return "Midpoint Circle Algorithm";
  }

  @Override
  public String getName() {
    return asHEADER;
  }

  @Override
  public String getAnimationAuthor() {
    return "Viktor Kolokolov";
  }

  @Override
  public String getCodeExample() {
    return pseudo_code;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return desc;
  }

  @Override
  public String getFileExtension() {
    return ".asu";
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }
}