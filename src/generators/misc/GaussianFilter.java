package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * The generator generating animal script code demonstrating gaussian blur.
 */
public class GaussianFilter implements Generator {

  private Language            lang;

  // private static final String AUTHORS =
  // "Martin Tschirsich, Tjark Vandommele et errator";

  // private static final String ALGORITHM = "Gaussian Filter";

  private static final String DESCRIPTION = "A Gaussian Filter is a common low pass filter. It is often used to reduce image noice and detail."
                                              + "This effect is known as image blur. Therefore, the Gaussian Filter is a type of image blurring filter,"
                                              + " that uses a Gaussian Function for calculating the transformation to apply to each pixel in the image.";

  private static final String SOURCE_CODE = "S : INPUT_SIGMA<br>"
                                              + "IN : INPUT_IMAGE<br>"
                                              + "OUT : OUTPUT_IMAGE<br>"
                                              + "<br>"
                                              + "K = BUILD_KERNEL(S)<br>"
                                              + "for each pixel P in IN:<br>"
                                              + "  apply K to P in IN and store result in OUT<br>"
                                              + "<br><br>You are visitor number <img src=\"http://usr.bplaced.de/PAVCounter/counter.php\">.";

  public GaussianFilter() {
  }

  public void init() {
  }

  /**
   * The script generating method.
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    lang = new AnimalScript("Gaussian Filter",
        "Martin Tschirsich, Tjark Vandommele", 5000, 5000);
    lang.setStepMode(true);

    // User-defined properties:
    Double standardDeviation = (Double) primitives.get("Standard deviation");

    // Draw header:
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    algoanim.primitives.Text h1 = lang.newText(new Coordinates(20, 15),
        "Gaussian Filter", "h1", null, textProps);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    lang.newRect(new Offset(-15, 5, h1, "SW"), new Offset(10, 10, h1, "SE"),
        "h1Rect1", null, rectProps);
    lang.newRect(new Offset(10, 0, h1, "NE"), new Offset(12, 10, h1, "SE"),
        "h1Rect2", null, rectProps);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    lang.newRect(new Offset(-15, 0, h1, "NW"), new Offset(12, 10, h1, "SE"),
        "h1Rect1", null, rectProps);

    // Draw introduction:
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.BOLD, 20));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    SourceCode sc0 = lang.newSourceCode(new Coordinates(10, 50), "Code", null,
        scProps);

    sc0.addCodeLine("Introduction:", null, 0, null);
    sc0.addCodeLine(" ", null, 0, null);
    sc0.addCodeLine("What is a Gaussian Filter?", null, 0, null);
    sc0.addCodeLine(" ", null, 1, null);
    sc0.addCodeLine(
        "1. A Gaussian Filter is a low pass filter often used in image noice reduction.",
        null, 1, null);
    sc0.addCodeLine(
        "   Convolving an image with a kernel of Gaussian values produces a blur effect.",
        null, 1, null);
    sc0.addCodeLine(
        "   Since the Gaussian function used to calculate the filter kernel is non-zero, the",
        null, 1, null);
    sc0.addCodeLine(
        "   entire image would need to be included in the calculations for each pixel.",
        null, 1, null);
    sc0.addCodeLine(
        "   In practice, kernel dimension is often limited to 6 * standard deviation.",
        null, 1, null);
    sc0.addCodeLine("   => trade-off between performance and precision.", null,
        1, null);
    lang.nextStep();

    sc0.addCodeLine(" ", null, 1, null);
    sc0.addCodeLine(
        "2. The Gaussian Filter is linearly separable, i.e. the process can be split in",
        null, 1, null);
    sc0.addCodeLine("   two passes, a vertical and a horizontal pass.", null,
        1, null);
    sc0.addCodeLine(
        "   In each pass, a one-dimensional kernel is used to blur in the current direction. ",
        null, 1, null);
    sc0.addCodeLine(
        "   This method requires fewer calculations than a traditional single pass. ",
        null, 1, null);
    lang.nextStep();

    sc0.addCodeLine(" ", null, 1, null);
    sc0.addCodeLine(
        "3. Discretization is achieved by sampling the Gaussian Filter kernel at discrete",
        null, 1, null);
    sc0.addCodeLine(
        "   points, normally at positions corresponding to the midpoints of each pixel.",
        null, 1, null);

    lang.nextStep();
    sc0.hide();

    SourceCode sc1 = lang.newSourceCode(new Coordinates(10, 50), "Code", null,
        scProps);
    sc1.addCodeLine("Introduction:", null, 0, null);
    sc1.addCodeLine(" ", null, 0, null);
    sc1.addCodeLine("Application of the Gaussian Filter:", null, 0, null);
    sc1.addCodeLine(" ", null, 1, null);
    sc1.addCodeLine(
        "1. Sampling the Gaussian Filter kernel produces a matrix.", null, 1,
        null);
    sc1.addCodeLine(
        "   The center element has the largest value, decreasing symmetrically",
        null, 1, null);
    sc1.addCodeLine("   as the distance from the center increases.", null, 1,
        null);
    lang.nextStep();

    // Create 2D/1D kernel matrices/vectors:
    int kernelSize = (int) Math.round(3 * standardDeviation);
    kernelSize = (kernelSize % 2 == 0) ? kernelSize + 1 : kernelSize;

    Double kernel2D[][] = new Double[kernelSize][kernelSize];
    Double kernel1D[] = new Double[kernelSize];

    double maxKernel = 0;
    double factor = 1 / (Math.sqrt(2 * Math.PI) * standardDeviation);
    for (int x = 0; x < kernelSize; ++x) {
      for (int y = 0; y < kernelSize; ++y) {
        kernel2D[x][y] = factor
            * Math
                .exp(-((x - kernelSize / 2) * (x - kernelSize / 2) + (y - kernelSize / 2)
                    * (y - kernelSize / 2))
                    / (2 * standardDeviation * standardDeviation));
        if (kernel2D[x][y] > maxKernel)
          maxKernel = kernel2D[x][y];
      }
    }

    for (int y = 0; y < kernelSize; ++y) {
      kernel1D[y] = factor
          * Math.exp(-((y - kernelSize / 2) * (y - kernelSize / 2))
              / (2 * standardDeviation * standardDeviation));
    }

    lang.addLine("grid \"kernel\" (35, 310) lines "
        + kernelSize
        + " columns "
        + kernelSize
        + " color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");

    for (int x = 0; x < kernelSize; x++) {
      for (int y = 0; y < kernelSize; y++) {
        lang.addLine("setGridColor \"kernel[" + y + "][" + x + "]\" textColor "
            + kernelValueToFontColor(kernel2D[x][y], maxKernel) + " fillColor "
            + kernelValueToFillColor(kernel2D[x][y], maxKernel));
        lang.addLine("setGridValue \"kernel[" + y + "][" + x + "]\" \""
            + Math.round(kernel2D[x][y] * 100) + "\"");
      }
    }

    lang.nextStep();

    sc1.addCodeLine(" ", null, 1, null);
    sc1.addCodeLine(
        "2. The two one-dimensional kernels correspond to the center column and row of the kernel matrix.",
        null, 1, null);

    lang.addLine("grid \"verticalKernel2\" ("
        + (35 + 28 * kernelSize + 10)
        + ", 310) lines "
        + kernelSize
        + " columns 1 color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");

    for (int y = 0; y < kernelSize; y++) {
      lang.addLine("setGridColor \"verticalKernel2[" + y + "][0]\" textColor "
          + kernelValueToFontColor(kernel1D[y], maxKernel) + " fillColor "
          + kernelValueToFillColor(kernel1D[y], maxKernel));
      lang.addLine("setGridValue \"verticalKernel2[" + y + "][0]\" \""
          + Math.round(kernel1D[y] * 100) + "\"");
    }

    lang.addLine("grid \"horizontalKernel2\" (35, "
        + (310 + 24 * kernelSize + 10)
        + ") lines 1 columns "
        + kernelSize
        + " color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");

    for (int x = 0; x < kernelSize; x++) {
      lang.addLine("setGridColor \"horizontalKernel2[" + 0 + "][" + x
          + "]\" textColor " + kernelValueToFontColor(kernel1D[x], maxKernel)
          + " fillColor " + kernelValueToFillColor(kernel1D[x], maxKernel));
      lang.addLine("setGridValue \"horizontalKernel2[" + 0 + "][" + x
          + "]\" \"" + Math.round(kernel1D[x] * 100) + "\"");
    }

    lang.nextStep();
    sc1.hide();
    lang.addLine("hide \"kernel\"");
    lang.addLine("hide \"verticalKernel2\"");
    lang.addLine("hide \"horizontalKernel2\"");

    // Create 1D kernel vector:
    Double verticalKernel[] = new Double[kernelSize];
    Double horizontalKernel[] = new Double[kernelSize];
    double maxVerticalKernelValue = 0;
    double maxHorizontalKernelValue = 0;

    for (int i = 0; i < kernelSize; i++) {
      verticalKernel[i] = factor
          * Math.exp(-(i - kernelSize / 2) * (i - kernelSize / 2)
              / (2 * standardDeviation * standardDeviation));
      if (verticalKernel[i] > maxVerticalKernelValue) {
        maxVerticalKernelValue = verticalKernel[i];
      }
    }

    for (int i = 0; i < kernelSize; i++) {
      horizontalKernel[i] = factor
          * Math.exp(-((i - kernelSize / 2) * (i - kernelSize / 2))
              / (2 * standardDeviation * standardDeviation));
      if (horizontalKernel[i] > maxHorizontalKernelValue) {
        maxHorizontalKernelValue = horizontalKernel[i];
      }
    }

    // create init grid
    double[][] initGrid = new double[10][10];
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        initGrid[y][x] = Math.round(Math.random() * 10) / 10.0;
      }
    }

    // calculate first result grid
    double[][] firstResultGrid = new double[10][10];
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        double result = 0;
        for (int i = 0; i < kernelSize; i++) {
          result += getValueFormGrid(initGrid,
              (int) (x - (Math.floor(kernelSize / 2) - i)), y)
              * verticalKernel[i];
        }
        firstResultGrid[y][x] = result;
      }
    }

    // calculate final grid
    double[][] finalGrid = new double[10][10];
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        double result = 0;
        for (int i = 0; i < kernelSize; i++) {
          result += getValueFormGrid(firstResultGrid, x,
              (int) (y - (Math.floor(kernelSize / 2) - i)))
              * horizontalKernel[i];
        }
        finalGrid[y][x] = result;
      }
    }

    // headline
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));
    Text headline = lang.newText(new Offset(20, 20, h1, "SW"),
        "Step 1: Horizontal pass", "Step1", null, textProps);

    // draw init grid
    lang.addLine("grid \"initGrid\" (35, 100) lines 10 columns 10 color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        lang.addLine("setGridColor \"initGrid[" + y + "][" + x
            + "]\" textColor " + kernelValueToFontColor(initGrid[y][x], 1.0)
            + " fillColor " + kernelValueToFillColor(initGrid[y][x], 1.0));
        lang.addLine("setGridValue \"initGrid[" + y + "][" + x + "]\" \""
            + Math.round(initGrid[y][x] * 100) + "\"");
      }
    }

    lang.nextStep();

    // draw "x"
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));
    Text multSymbol = lang.newText(new Offset(140, 205, h1, "NE"), "x",
        "mult Symbol", null, textProps);

    // draw horizontal kernel
    lang.addLine("grid \"horizontalKernel\" (360, 215) lines 1 columns "
        + kernelSize
        + " color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");
    for (int x = 0; x < kernelSize; x++) {
      lang.addLine("setGridColor \"horizontalKernel[0]["
          + x
          + "]\" textColor "
          + kernelValueToFontColor(horizontalKernel[x],
              maxHorizontalKernelValue)
          + " fillColor "
          + kernelValueToFillColor(horizontalKernel[x],
              maxHorizontalKernelValue));
      lang.addLine("setGridValue \"horizontalKernel[0][" + x + "]\" \""
          + Math.round(horizontalKernel[x] * 100) + "\"");
    }

    lang.nextStep();

    // draw "="
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));
    Text equalsSymbol = lang.newText(new Offset(40 + kernelSize * 28, 0,
        multSymbol, "NE"), "=", "equals Symbol", null, textProps);

    // draw first result grid
    lang.addLine("grid \"firstResultGrid\" ("
        + (410 + kernelSize * 28)
        + ", 100) lines 10 columns 10 color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        lang.addLine("setGridColor \"firstResultGrid[" + y + "][" + x
            + "]\" textColor "
            + kernelValueToFontColor(firstResultGrid[y][x], 1.0)
            + " fillColor "
            + kernelValueToFillColor(firstResultGrid[y][x], 1.0));
        lang.addLine("setGridValue \"firstResultGrid[" + y + "][" + x
            + "]\" \"" + Math.round(firstResultGrid[y][x] * 100) + "\"");
      }
    }

    lang.nextStep();

    // description
    SourceCode sc2 = lang.newSourceCode(new Offset(20, 300, h1, "SW"), "Code",
        null, scProps);
    sc2.addCodeLine(
        "The horizontal kernel is used to blur in horizontal direction.", null,
        0, null);
    sc2.addCodeLine(
        "Each value of the new matrix is determined by the average of the weighted horizontal neigbours.",
        null, 0, null);
    sc2.addCodeLine(
        "To achive this the weights are looked up in the kernel vector, each neigbour is multiplied",
        null, 0, null);
    sc2.addCodeLine(
        "with its weight, these values are added up and then the result is divided by the count",
        null, 0, null);
    sc2.addCodeLine("of the neigbours.", null, 0, null);
    sc2.addCodeLine(
        "The count of neigbours equals the kernel size which depends on the standard deviation.",
        null, 0, null);

    lang.nextStep();

    headline.hide();
    sc2.hide();
    lang.addLine("hide \"initGrid\"");
    multSymbol.hide();
    lang.addLine("hide \"horizontalKernel\"");
    equalsSymbol.hide();
    lang.addLine("hide \"firstResultGrid\"");

    // headline
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));
    headline = lang.newText(new Offset(20, 20, h1, "SW"),
        "Step 2: Vertical pass", "Step2", null, textProps);

    // draw first result grid for the second time
    lang.addLine("grid \"firstResultGrid2\" (35, 100) lines 10 columns 10 color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        lang.addLine("setGridColor \"firstResultGrid2[" + y + "][" + x
            + "]\" textColor "
            + kernelValueToFontColor(firstResultGrid[y][x], 1.0)
            + " fillColor "
            + kernelValueToFillColor(firstResultGrid[y][x], 1.0));
        lang.addLine("setGridValue \"firstResultGrid2[" + y + "][" + x
            + "]\" \"" + Math.round(firstResultGrid[y][x] * 100) + "\"");
      }
    }

    lang.nextStep();

    // draw "x"
    multSymbol.show();

    // draw vertical kernel
    int yOffset = (int) (220 - ((Math.floor(kernelSize / 2) * 24 + 12)));
    if (yOffset < 100) {
      yOffset = 100;
    }

    lang.addLine("grid \"verticalKernel\" (360, "
        + yOffset
        + ") lines "
        + kernelSize
        + " columns 1 color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");
    for (int y = 0; y < kernelSize; y++) {
      lang.addLine("setGridColor \"verticalKernel[" + y + "][0]\" textColor "
          + kernelValueToFontColor(verticalKernel[y], maxVerticalKernelValue)
          + " fillColor "
          + kernelValueToFillColor(verticalKernel[y], maxVerticalKernelValue));
      lang.addLine("setGridValue \"verticalKernel[" + y + "][0]\" \""
          + Math.round(verticalKernel[y] * 100) + "\"");
    }

    lang.nextStep();

    // draw "="
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));
    equalsSymbol = lang.newText(new Offset(68, 0, multSymbol, "NE"), "=",
        "equals Symbol", null, textProps);

    // draw final grid
    lang.addLine("grid \"finalGrid\" (438, 100) lines 10 columns 10 color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        lang.addLine("setGridColor \"finalGrid[" + y + "][" + x
            + "]\" textColor " + kernelValueToFontColor(finalGrid[y][x], 1.0)
            + " fillColor " + kernelValueToFillColor(finalGrid[y][x], 1.0));
        lang.addLine("setGridValue \"finalGrid[" + y + "][" + x + "]\" \""
            + Math.round(finalGrid[y][x] * 100) + "\"");
      }
    }

    lang.nextStep();

    // description
    SourceCode sc3 = lang.newSourceCode(new Offset(20, 300, h1, "SW"), "Code",
        null, scProps);
    sc3.addCodeLine(
        "The vertical one-dimensional kernel is used to blur in vertical direction.",
        null, 0, null);
    sc3.addCodeLine(
        "Again each value of the new matrix is determined by the average of the weighted horizontal neigbours.",
        null, 0, null);

    lang.nextStep();

    headline.hide();
    sc3.hide();
    multSymbol.hide();
    equalsSymbol.hide();
    lang.addLine("hide \"finalGrid\"");
    lang.addLine("hide \"verticalKernel\"");
    lang.addLine("hide \"firstResultGrid2\"");

    // headline
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));
    headline = lang.newText(new Offset(20, 20, h1, "SW"), "Result", "Result",
        null, textProps);

    // draw init grid again
    lang.addLine("grid \"initGrid2\" (35, 100) lines 10 columns 10 color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        lang.addLine("setGridColor \"initGrid2[" + y + "][" + x
            + "]\" textColor " + kernelValueToFontColor(initGrid[y][x], 1.0)
            + " fillColor " + kernelValueToFillColor(initGrid[y][x], 1.0));
        lang.addLine("setGridValue \"initGrid2[" + y + "][" + x + "]\" \""
            + Math.round(initGrid[y][x] * 100) + "\"");
      }
    }

    // draw "->"
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));
    lang.newText(new Offset(140, 205, h1, "NE"), "=>", "arrow", null, textProps);

    // draw final grid again
    lang.addLine("grid \"finalGrid2\" (375, 100) lines 10 columns 10 color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)  highlightBackColor (0, 0, 0) depth 1");
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        lang.addLine("setGridColor \"finalGrid2[" + y + "][" + x
            + "]\" textColor " + kernelValueToFontColor(finalGrid[y][x], 1.0)
            + " fillColor " + kernelValueToFillColor(finalGrid[y][x], 1.0));
        lang.addLine("setGridValue \"finalGrid2[" + y + "][" + x + "]\" \""
            + Math.round(finalGrid[y][x] * 100) + "\"");
      }
    }

    lang.nextStep();

    SourceCode sc4 = lang.newSourceCode(new Offset(20, 300, h1, "SW"), "Code",
        null, scProps);
    sc4.addCodeLine(
        "The new matrix is the result of the Gaussian Filter, applied to the initial",
        null, 0, null);
    sc4.addCodeLine("matrix on the left.", null, 0, null);

    return lang.toString();
  }

  private Double getValueFormGrid(double[][] grid, int x, int y) {
    try {
      return grid[y][x];
    } catch (ArrayIndexOutOfBoundsException e) {
      return (double) 0;
    }
  }

  private String colorToString(Color color) {
    return "(" + color.getRed() + ", " + color.getGreen() + ", "
        + color.getBlue() + ")";
  }

  public String getAlgorithmName() {
    return "Gaussian Filter";
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
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public String getName() {
    return "Gaussian filter";
  }

  private String kernelValueToFontColor(double kernelValue,
      double maxKernelValue) {
    int component = ((int) (kernelValue / maxKernelValue * 255) + 90) % 256;
    return colorToString(new Color(component, component, component));
  }

  private String kernelValueToFillColor(double kernelValue,
      double maxKernelValue) {
    int component = (int) (kernelValue / maxKernelValue * 255) % 256;
    return colorToString(new Color(component, component, component));
  }
}