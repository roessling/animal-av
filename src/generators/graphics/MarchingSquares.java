/*
 * MarchingSquares.java
 * Simon Heinrich, Philipp Nothvogel, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolygonProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graphics.marching_squares.MarchingSquaresCase;
import generators.graphics.marching_squares.MarchingSquaresCase.CaseType;
import generators.graphics.marching_squares.MarchingSquaresImageHelper;
import generators.graphics.marching_squares.MarchingSquaresPixel;
import java.awt.Color;
import java.awt.Font;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unused")
public class MarchingSquares implements ValidatingGenerator {

  private Language lang;
  private String imageUrl;
  private int threshold;
  protected int[][] data;
  protected MarchingSquaresPixel[][] pixels;
  protected MarchingSquaresCase[][] cases;
  protected Map<String, MarchingSquaresCase> caseMap;
  protected MarchingSquaresCase specialCaseFirstUnconnected;
  protected MarchingSquaresCase specialCaseFirstConnected;
  protected MarchingSquaresCase specialCaseSecondUnconnected;
  protected MarchingSquaresCase specialCaseSecondConnected;
  protected SourceCode pseudoCode;
  protected Rect titleRect;
  protected Rect upperCaseRect;
  protected int lastLine = 0;

  // design constants
  private static final int margin = 120;
  private static final int startX = 10;
  private static final int caseMargin = 110;
  private static final int rectWidth = 4 * caseMargin;

  public void init() {
    lang = new AnimalScript("Marching Squares", "Simon Heinrich, Philipp Nothvogel", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
    imageUrl = (String) primitives.get("imageUrl");
    threshold = (Integer) primitives.get("threshold");
    Color inBgHighlighColor = (Color) primitives.get("inBgHighlightColor");
    Color outBgHighlighColor = (Color) primitives.get("outBgHighlightColor");
    MarchingSquaresPixel.setInBgHighlightColor(inBgHighlighColor);
    MarchingSquaresPixel.setOutBgHighlightColor(outBgHighlighColor);

    this.march();
    return lang.toString();
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
    imageUrl = (String) primitives.get("imageUrl");
    threshold = (Integer) primitives.get("threshold");

    if ((threshold < -1) || (threshold > 255)) {
      throw new IllegalArgumentException("Threshold value is invalid. It needs to be between [-1,255].");
    }
    return true;
  }

  public enum LINE {
    Start(0, "1. focus next square and find case in table"),
    DrawCorrespLine(1, "2. draw corresponding line"),
    NoContour(2, "2a. no contour"),
    SingleSegment(3, "2b. single segment"),
    TwoSegmentSaddle(4, "2c. two-segment saddle"),
    CalculateAverage(5, "calculate average pixel value of square"),
    AverageGteThreshold(6, "if (avg >= threshold) then connect"),
    AverageLtThreshold(7, "else (avg < threshold) then seperate");

    private final int number;
    private final String text;

    private LINE(int number, String text) {
      this.number = number;
      this.text = text;
    }

    public int getNumber() {
      return number;
    }
  }


  protected void addIntroductionSlide() {
    // Create title.
    TextProperties textProperties = new TextProperties();
    textProperties
        .set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 46));
    textProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(203, 250, 100));

    titleRect = lang
        .newRect(new Coordinates(startX, startX), new Coordinates(startX + rectWidth, 70), "null",
            null, rectProperties);
    lang.newText(new Offset(0, -30, titleRect, "C"), "Marching Squares", "null", null,
        textProperties);

    // Add threshold
    TextProperties thresholdTextProps = new TextProperties();
    thresholdTextProps
        .set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 46));
    thresholdTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
    thresholdTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    Node offset = new Coordinates(startX + rectWidth + 20, startX);
    lang.newText(offset, "Threshold: " + threshold, "", null, thresholdTextProps);

    // Create intro slide.
    SourceCodeProperties sourceCodeProperties = new SourceCodeProperties();
    sourceCodeProperties
        .set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 30));

    SourceCode description = lang
        .newSourceCode(new Coordinates(10, 80), "null", null, sourceCodeProperties);
    description.addCodeLine(
        "The marching squares algorithm generates contours for two-dimensional fields of scalars.",
        "null", 0, null);
    description.addCodeLine("For example, you can generate isolines of an image.", "null", 0, null);
    description.addCodeLine(
        "You start by applying a threshold to the 2D field converting it to a binary image:",
        "null", 0, null);
    description.addCodeLine("1 (white) where the value is above the isovalue", "null", 1, null);
    description.addCodeLine("0 (black) where the value is below the isovalue", "null", 1, null);
    description.addCodeLine(
        "Each 2x2 block of pixels can be treated as a square. The isoline drawn in each square",
        "null", 0, null);
    description.addCodeLine(
        "does not depend on the rest of the image. You know which line to draw based on a lookup table.",
        "null", 0, null);
    description
        .addCodeLine("In the special case where only two pixels diagonal to each other are filled,",
            "null", 0, null);
    description
        .addCodeLine("you need to calculate the average of all pixels in this square to determine",
            "null", 0, null);
    description.addCodeLine("if the resulting isolines should:", "null", 0, null);
    description.addCodeLine("connect both pixels (avg >= threshold) or", "null", 1, null);
    description.addCodeLine("seperate both pixels (avg < threshold).", "null", 1, null);
    description.addCodeLine("There is a lookup table of all possible cases in the following steps.",
        "null", 0, null);
    lang.nextStep("Introduction");
    description.hide();
  }

  protected void addCaseTable() {
    cases = new MarchingSquaresCase[4][4];

    // Draw cases
    int innerCaseMargin = 42;
    int halfCaseMargin = caseMargin / 2;
    int baseX = startX;
    int upperBaseY = 120;
    // 10: radius of pixel
    int caseBaseX = 10 + baseX + halfCaseMargin / 2;
    int anotherOffset = (caseMargin - innerCaseMargin) / 2;

    caseMap = new HashMap<String, MarchingSquaresCase>(20);

    // Draw upper rect
    Coordinates baseCoords = new Coordinates(baseX, upperBaseY);
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    upperCaseRect = lang
        .newRect(baseCoords, new Offset(rectWidth, caseMargin, baseCoords, "C"), "null", null,
            rectProperties);

    caseMap.put("0000",
        new MarchingSquaresCase(lang, new Coordinates(caseBaseX, upperBaseY + anotherOffset), "0000",
            CaseType.NoContour, innerCaseMargin));
    caseMap.put("1111",
        new MarchingSquaresCase(lang, new Coordinates(caseBaseX + 3 * caseMargin, upperBaseY + anotherOffset),
            "1111", CaseType.NoContour, innerCaseMargin));

    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    textProperties
        .set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    lang.newText(new Coordinates(baseX + rectWidth / 2, upperBaseY + anotherOffset), "no contour",
        "null", null, textProperties);

    // Draw middle rect
    int middleBaseY = upperBaseY + caseMargin;
    int middleHeight = 3 * caseMargin + halfCaseMargin / 2;
    Rect middleRect = lang.newRect(new Coordinates(baseX, middleBaseY),
        new Coordinates(baseX + rectWidth, middleBaseY + middleHeight), "null", null,
        rectProperties);

    lang.newText(new Coordinates(baseX + rectWidth / 2, middleBaseY + anotherOffset / 2),
        "single segment", "null", null, textProperties);

    int caseNum = 1;
    int middleOffsetY = middleBaseY + halfCaseMargin + anotherOffset / 2;

    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 4; col++) {
        Coordinates coords = new Coordinates(caseBaseX + col * caseMargin,
            middleOffsetY + row * caseMargin);
        String binary = String.format("%4s", Integer.toBinaryString(caseNum)).replace(' ', '0');
        cases[row][col] = new MarchingSquaresCase(lang, coords, binary, CaseType.SingleSegment, 42);
        caseNum++;
        if (caseNum == 10 || caseNum == 5) {
          caseNum++;
        }

        MarchingSquaresCase current = cases[row][col];
        caseMap.put(binary, current);
        MarchingSquaresPixel northWest = current.getNorthWest();
        MarchingSquaresPixel northEast = current.getNorthEast();
        MarchingSquaresPixel southWest = current.getSouthWest();
        MarchingSquaresPixel southEast = current.getSouthEast();

        drawLine(binary, northWest, northEast, southWest, southEast, current.getMargin(), 2);
      }
    }

    // Draw lower rect
    int lowerBaseY = middleBaseY + middleHeight;
    int lowerHeight = middleHeight - caseMargin;
    int lowerOffsetY = lowerBaseY + halfCaseMargin + anotherOffset / 2;
    Rect lowerRect = lang.newRect(new Coordinates(baseX, lowerBaseY),
        new Coordinates(baseX + rectWidth, lowerBaseY + lowerHeight), "null", null, rectProperties);

    lang.newText(new Coordinates(baseX + rectWidth / 2, lowerBaseY + anotherOffset / 2),
        "two-segment saddle", "null", null, textProperties);

    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 4; col++) {
        if (col == 1) {
          // add arrow
          TextProperties arrowTextProperties = new TextProperties();
          arrowTextProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
          arrowTextProperties
              .set(AnimationPropertiesKeys.FONT_PROPERTY,
                  new Font(Font.SANS_SERIF, Font.PLAIN, 80));
          arrowTextProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
          Coordinates coords = new Coordinates(caseBaseX + col * caseMargin + 22,
              lowerOffsetY + row * caseMargin - 34);
          lang.newText(coords, "\u21D2", "null", null, arrowTextProperties);
          continue;
        }

        Coordinates coords = new Coordinates(caseBaseX + col * caseMargin,
            lowerOffsetY + row * caseMargin);
        String binary = String.format("%4s", Integer.toBinaryString((row + 1) * 5))
            .replace(' ', '0');
        MarchingSquaresCase current = new MarchingSquaresCase(lang, coords, binary, CaseType.TwoSegmentSaddle, 42);

        if (col < 2) {
          caseMap.put(binary, current);
          // don't draw the line.
          continue;
        }

        if (row == 0 && col == 2) {
          specialCaseFirstUnconnected = current;
        } else if (row == 0 && col == 3) {
          specialCaseFirstConnected = current;
        } else if (row == 1 && col == 2) {
          specialCaseSecondUnconnected = current;
        } else if (row == 1 && col == 3) {
          specialCaseSecondConnected = current;
        }

        MarchingSquaresPixel northWest = current.getNorthWest();
        MarchingSquaresPixel northEast = current.getNorthEast();
        MarchingSquaresPixel southWest = current.getSouthWest();
        MarchingSquaresPixel southEast = current.getSouthEast();

        if (col == 3) {
          // display second case
          northWest.multValue(2);
          northEast.multValue(2);
          southWest.multValue(2);
          southEast.multValue(2);
        }

        String caseString = pixelToString(northWest, northEast, southWest, southEast);
        drawLine(caseString, northWest, northEast, southWest, southEast, current.getMargin(), 2);
      }
    }

  }

  protected void addPseudoCode() {
    SourceCodeProperties sourceCodeProperties = new SourceCodeProperties();
    sourceCodeProperties
        .set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
    sourceCodeProperties
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(114, 140, 56));

    pseudoCode = lang.newSourceCode(new Offset(2 * startX, 0, upperCaseRect, "NE"), "null", null,
        sourceCodeProperties);
    pseudoCode.addCodeLine("1. focus next square and find case in table", "null", 0, null);
    pseudoCode.addCodeLine("2. draw corresponding line", "null", 0, null);
    pseudoCode.addCodeLine("2a. no contour", "null", 1, null);
    pseudoCode.addCodeLine("2b. single segment", "null", 1, null);
    pseudoCode.addCodeLine("2c. two-segment saddle", "null", 1, null);
    pseudoCode.addCodeLine("calculate average pixel value of square", "null", 2, null);
    pseudoCode.addCodeLine("if (avg >= threshold) then connect", "null", 2, null);
    pseudoCode.addCodeLine("else (avg < threshold) then seperate", "null", 2, null);
  }

  protected void highlightLine(LINE line) {
    int nextLine = line.getNumber();
    highlightLine(nextLine);
  }

  protected void highlightLine(int nextLine) {
    if (nextLine >= pseudoCode.length()) {
      nextLine = 0;
    }
    pseudoCode.toggleHighlight(lastLine, nextLine);

    lastLine = nextLine;
  }

  protected void addPixels() {
    pixels = new MarchingSquaresPixel[data.length][data[0].length];

    // creating pixels from data matrix
    for (int row = 0; row < pixels.length; row++) {
      for (int col = 0; col < pixels[0].length; col++) {
        int value = data[row][col];
        Offset coords = new Offset(5 * startX + col * margin, -100 + row * margin, pseudoCode,
            "NE");
        pixels[row][col] = new MarchingSquaresPixel(this.lang, coords, value, threshold, 30);
      }
    }
  }

  protected void addSummary() {
    SourceCodeProperties sourceCodeProperties = new SourceCodeProperties();
    sourceCodeProperties
            .set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
    SourceCode summary = lang.newSourceCode(new Offset(0, 20, pseudoCode, "SW"), "null", null, sourceCodeProperties);
    String summaryText = "Summary:\n" +
            "First, we converted the image to a binary\n" +
            "representation. Black pixels are below\n" +
            "the threshold and white pixels are\n" +
            "equal to or above the threshold.\n" +
            "We then looked at each 2x2 block of pixels\n" +
            "and drew the corresponding iso lines\n" +
            "based on the case lookup table. As a result\n" +
            "we extracted the contours (red) based on\n" +
            "the given threshold value for this image.";
    summary.addMultilineCode(summaryText, "null", null);
    lang.nextStep("Summary");
  }

  public int getDefaultThreshold(int[][] data) {
    List<Integer> values = new LinkedList<>();

    int sum = 0;
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[0].length; j++) {
        int value = data[i][j];
        values.add(value);
        sum += value;
      }
    }
    Collections.sort(values);
    int threshold = 0;
    int length = values.size();
    int average = sum / length;
    for (int i = 0; i < length/2; i++) {
      int value = values.get(i);
      threshold = value;
      if (value > average) {
        break;
      }
    }
    //System.out.printf("threshold: %d\n", threshold);
    return threshold;
  }

  public void march() {
    try {
      data = MarchingSquaresImageHelper.getPixelValuesFromUrl(imageUrl);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    if (threshold < 0) {
      threshold = getDefaultThreshold(data);
    }

    addIntroductionSlide();
    addCaseTable();
    addPseudoCode();
    addPixels();

    lang.nextStep("Start of the algorithm");

    // Start of marching cube algo
    for (int row = 0; row < data.length - 1; row++) {
      for (int col = 0; col < data[0].length - 1; col++) {
        MarchingSquaresPixel northWest = pixels[row][col];
        MarchingSquaresPixel northEast = pixels[row][col + 1];
        MarchingSquaresPixel southWest = pixels[row + 1][col];
        MarchingSquaresPixel southEast = pixels[row + 1][col + 1];

        northWest.highlight();
        northEast.highlight();
        southWest.highlight();
        southEast.highlight();
        highlightLine(LINE.Start);

        String caseString = pixelToString(northWest, northEast, southWest, southEast);
        MarchingSquaresCase currentCase = caseMap.get(caseString);
        currentCase.highlightCase();

        if (col == 0) {
          lang.nextStep(String.format("Rows %d and %d", row + 1, row + 2));
        } else {
          lang.nextStep();
        }
        highlightCaseLine(currentCase);

        //Special case
        if (currentCase.getCaseType().equals(CaseType.TwoSegmentSaddle)) {
          lang.nextStep();
          highlightLine(LINE.CalculateAverage);

          TextProperties textProperties = new TextProperties();
          textProperties
              .set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
          Node offset = new Offset(0, 30, pseudoCode, "SW");
          StringBuilder text = new StringBuilder();
          int valueNorthWest = northWest.getValue();
          int valueNorthEast = northEast.getValue();
          int valueSouthWest = southWest.getValue();
          int valueSouthEast = southEast.getValue();
          String plus = " + ";
          text.append("avg = (");
          text.append(valueNorthWest);
          text.append(plus);
          text.append(valueNorthEast);
          text.append(plus);
          text.append(valueSouthEast);
          text.append(plus);
          text.append(valueSouthWest);
          text.append(") / 4 = ");
          double avg = valueNorthWest + valueNorthEast + valueSouthEast + valueSouthWest;
          avg /= 4.0;
          text.append(String.format("%.1f", avg));

          Text avgCalculation = lang.newText(offset, text.toString(), "null", null, textProperties);
          lang.nextStep();
          if (caseString.equals("0101")) {
            if (avgGreaterThanThreshold(northWest, northEast, southWest, southEast)) {
              specialCaseFirstConnected.highlightCase();
              highlightLine(LINE.AverageGteThreshold);
            } else {
              specialCaseFirstUnconnected.highlightCase();
              highlightLine(LINE.AverageLtThreshold);
            }
          } else if (caseString.equals("1010")) {
            if (avgGreaterThanThreshold(northWest, northEast, southWest, southEast)) {
              specialCaseSecondConnected.highlightCase();
              highlightLine(LINE.AverageGteThreshold);
            } else {
              specialCaseSecondUnconnected.highlightCase();
              highlightLine(LINE.AverageLtThreshold);
            }
          }
          lang.nextStep();
          avgCalculation.hide();
        }
        drawLine(caseString, northWest, northEast, southWest, southEast, margin, 3);
        // highlightLine(LINE.DrawCorrespLine);
        lang.nextStep();

        currentCase.unhighlightCase();
        unhighlightSpecialCase();

        northWest.unHighlight();
        northEast.unHighlight();
        southWest.unHighlight();
        southEast.unHighlight();
      }
    }

    lang.nextStep("Result");
    addSummary();
  }

  private void highlightCaseLine(MarchingSquaresCase currentCase) {
    CaseType caseType = currentCase.getCaseType();

    if (caseType.equals(CaseType.NoContour)) {
      highlightLine(LINE.NoContour);
    } else if (caseType.equals(CaseType.SingleSegment)) {
      highlightLine(LINE.SingleSegment);
    } else if (caseType.equals(CaseType.TwoSegmentSaddle)) {
      highlightLine(LINE.TwoSegmentSaddle);
    }
  }

  private void unhighlightSpecialCase() {
    specialCaseFirstUnconnected.unhighlightCase();
    specialCaseFirstConnected.unhighlightCase();
    specialCaseSecondUnconnected.unhighlightCase();
    specialCaseSecondConnected.unhighlightCase();
  }


  String pixelToString(MarchingSquaresPixel northWest, MarchingSquaresPixel northEast, MarchingSquaresPixel southWest, MarchingSquaresPixel southEast) {
    boolean nw = northWest.isFilled();
    boolean ne = northEast.isFilled();
    boolean sw = southWest.isFilled();
    boolean se = southEast.isFilled();

    String result = "";
    if (nw) {
      result += "1";
    } else {
      result += "0";
    }
    if (ne) {
      result += "1";
    } else {
      result += "0";
    }
    if (se) {
      result += "1";
    } else {
      result += "0";
    }
    if (sw) {
      result += "1";
    } else {
      result += "0";
    }

    return result;
  }
  /*private boolean check(int row, int col) {
    if (row == 1 || row == 3 || row == 5 || row == 7) {
      if (col == 3 || col == 4 || col == 6 || col == 7) {
        return true;
      }
    }
    if (row == 2) {
      if (col == 1 || col == 3 || col == 5 || col == 7) {
        return true;
      }
    }
    if (row == 4) {
      if (col == 0 || col == 2 || col == 4 || col == 6) {
        return true;
      }
    }
    if (row == 6) {
      return true;
    }
    return false;
  }*/

  void drawLine(String cases, MarchingSquaresPixel northWest, MarchingSquaresPixel northEast, MarchingSquaresPixel southWest, MarchingSquaresPixel southEast,
                int margin, int lineThickness) {

    List<Node> points = new LinkedList<Node>();
    int lineOffset = margin / 2;

    int lower = lineOffset - lineThickness;
    int higher = lineOffset + lineThickness;
    //if ((!nw && !ne && !sw && !se) || (nw && ne && sw && se)) {
    if (cases.equals("0000") || cases.equals("1111")) {
      return;
    }
    //else if ((!nw && !ne && !sw && se) || (nw && ne && sw && !se)) {
    else if (cases.equals("0010") || cases.equals("1101")) {
      points.add(new Offset(lower, 0, southWest.getCircle(), "C"));
      points.add(new Offset(higher, 0, southWest.getCircle(), "C"));
      points.add(new Offset(margin, -lower, southWest.getCircle(), "C"));
      points.add(new Offset(margin, -higher, southWest.getCircle(), "C"));
    }
    //else if ((!nw && !ne && sw && !se) || (nw && ne && !sw && se)) {
    else if (cases.equals("0001") || cases.equals("1110")) {
      points.add(new Offset(lower, 0, southWest.getCircle(), "C"));
      points.add(new Offset(higher, 0, southWest.getCircle(), "C"));
      points.add(new Offset(0, -higher, southWest.getCircle(), "C"));
      points.add(new Offset(0, -lower, southWest.getCircle(), "C"));
    }
    //else if ((!nw && !ne && sw && se) || (nw && ne && !sw && !se)) {
    else if (cases.equals("0011") || cases.equals("1100")) {
      points.add(new Offset(0, -lower, southWest.getCircle(), "C"));
      points.add(new Offset(0, -higher, southWest.getCircle(), "C"));
      points.add(new Offset(margin, -higher, southWest.getCircle(), "C"));
      points.add(new Offset(margin, -lower, southWest.getCircle(), "C"));
    }
    //else if ((!nw && ne && !sw && !se) || (nw && !ne && sw && se)) {
    else if (cases.equals("0100") || cases.equals("1011")) {
      points.add(new Offset(lower, -margin, southWest.getCircle(), "C"));
      points.add(new Offset(higher, -margin, southWest.getCircle(), "C"));
      points.add(new Offset(margin, -higher, southWest.getCircle(), "C"));
      points.add(new Offset(margin, -lower, southWest.getCircle(), "C"));
    }
    //else if ((!nw && ne && !sw && se) || (nw && !ne && sw && !se)) {
    else if (cases.equals("0110") || cases.equals("1001")) {
      points.add(new Offset(lower, 0, southWest.getCircle(), "C"));
      points.add(new Offset(higher, 0, southWest.getCircle(), "C"));
      points.add(new Offset(higher, -margin, southWest.getCircle(), "C"));
      points.add(new Offset(lower, -margin, southWest.getCircle(), "C"));
    }
    //else if ((!nw && ne && sw && !se) || (nw && !ne && !sw && se)) {
    else if (cases.equals("0101") || cases.equals("1010")) {
      //Special case
      boolean avg = avgGreaterThanThreshold(northWest, northEast, southWest, southEast);
      if ((avg && cases.equals("0101")) || !avg && cases.equals("1010")) {
        points.add(new Offset(0, -lower, southWest.getCircle(), "C"));
        points.add(new Offset(0, -higher, southWest.getCircle(), "C"));
        points.add(new Offset(lower, -margin, southWest.getCircle(), "C"));
        points.add(new Offset(higher, -margin, southWest.getCircle(), "C"));

        PolygonProperties polygonProperties = new PolygonProperties();
        polygonProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        polygonProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
        try {
          lang.newPolygon(points.toArray(new Node[4]), "null", null, polygonProperties);
        } catch (NotEnoughNodesException e) {
          e.printStackTrace();
        }
        points.clear();
        //second polygon
        points.add(new Offset(lower, 0, southWest.getCircle(), "C"));
        points.add(new Offset(higher, 0, southWest.getCircle(), "C"));
        points.add(new Offset(margin, -lower, southWest.getCircle(), "C"));
        points.add(new Offset(margin, -higher, southWest.getCircle(), "C"));
      } else {
        points.add(new Offset(0, -lower, southWest.getCircle(), "C"));
        points.add(new Offset(0, -higher, southWest.getCircle(), "C"));
        points.add(new Offset(higher, 0, southWest.getCircle(), "C"));
        points.add(new Offset(lower, 0, southWest.getCircle(), "C"));

        PolygonProperties polygonProperties = new PolygonProperties();
        polygonProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        polygonProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
        try {
          lang.newPolygon(points.toArray(new Node[4]), "null", null, polygonProperties);
        } catch (NotEnoughNodesException e) {
          e.printStackTrace();
        }
        points.clear();
        //second polygon
        points.add(new Offset(lower, -margin, southWest.getCircle(), "C"));
        points.add(new Offset(higher, -margin, southWest.getCircle(), "C"));
        points.add(new Offset(margin, -higher, southWest.getCircle(), "C"));
        points.add(new Offset(margin, -lower, southWest.getCircle(), "C"));
      }
    }
    //else if ((!nw && ne && sw && se) || (nw && !ne && !sw && !se)) {
    else if (cases.equals("0111") || cases.equals("1000")) {
      points.add(new Offset(0, -lower, southWest.getCircle(), "C"));
      points.add(new Offset(0, -higher, southWest.getCircle(), "C"));
      points.add(new Offset(lower, -margin, southWest.getCircle(), "C"));
      points.add(new Offset(higher, -margin, southWest.getCircle(), "C"));
    }
    //else if (nw && !ne && !sw && !se) {
    // 1000
    //}
    //else if (nw && !ne && !sw && se) {
    // 1001
    //}
    //else if (nw && !ne && sw && !se) {
    // 1010
    //}
    //else if (nw && !ne && sw && se) {
    // 1011
    //}
    //else if (nw && ne && !sw && !se) {
    // 1100
    //}
    //else if (nw && ne && !sw && se) {
    // 1101
    //}
    //else if (nw && ne && sw && !se) {
    // 1110
    //}
    //else if (nw && ne && sw && se) {
    // 1111
    //return;
    //}

    if (points.size() != 4) {
      return;
    }
    PolygonProperties polygonProperties = new PolygonProperties();
    polygonProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    polygonProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
    try {
      lang.newPolygon(points.toArray(new Node[4]), "null", null, polygonProperties);
    } catch (NotEnoughNodesException e) {
      e.printStackTrace();
    }
  }

  private boolean avgGreaterThanThreshold(MarchingSquaresPixel northWest, MarchingSquaresPixel northEast, MarchingSquaresPixel southWest,
                                          MarchingSquaresPixel southEast) {
    double avg =
        northWest.getValue() + northEast.getValue() + southWest.getValue() + southEast.getValue();
    avg /= 4.0;
    boolean gteThreshold = (avg >= northEast.getThreshold());
    return gteThreshold;
  }

  public String getName() {
    return "Marching Squares";
  }

  public String getAlgorithmName() {
    return "Marching Squares";
  }

  public String getAnimationAuthor() {
    return "Simon Heinrich, Philipp Nothvogel";
  }

  public String getDescription() {
    return
        "The marching squares algorithm generates contours for two-dimensional fields of scalars."
            + "\n"
            + "For example, you can generate isolines of an image. "
            + "\n"
            + "You can set a custom image URL in the generator primitives.";
  }

  public String getCodeExample() {
    return "1. focus next square and find case in table"
        + "\n"
        + "2. draw corresponding line"
        + "\n"
        + "    2a. no contour"
        + "\n"
        + "    2b. single segment"
        + "\n"
        + "    2c. two-segment saddle"
        + "\n"
        + "        calculate average pixel value of square"
        + "\n"
        + "        if (avg >= threshold) then connect"
        + "\n"
        + "        else (avg < threshold) then seperate";
  }

  public String getFileExtension() {
    return "asu";
  }

  public Locale getContentLocale() {
    return Locale.ENGLISH;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}
