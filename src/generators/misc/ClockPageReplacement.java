package generators.misc;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class ClockPageReplacement extends AnnotatedAlgorithm {

  private int                          numberOfPages;

  private int[]                        pageAccesses;

  /**
   * A map from the name of a page to the place it is currently located.
   */
  private Map<Integer, Integer>        pageToPosition;

  /**
   * A map from the position to the name of a page i.e. allows to see what page
   * is at a specific position
   */
  private Map<Integer, Integer>        positionToPage;

  /**
   * A map from the name of a page to its reference bit
   */
  private Map<Integer, Boolean>        referenceBits;

  private int                          positionOfClockHandle;
  private double                       rotationRoundingError = 0.0;

  private AnimationPropertiesContainer properties;
  private Color                        cacheColor;
  private Color                        cacheHighlightColor;
  private Rect[]                       pageRects;

  private void start() {
    // build pages with reference bits
    Point center = new Point(400, 400);
    int radius = 150;
    pageRects = new Rect[numberOfPages];
    int pageRectSideLength = 50;
    Text[] pageTexts = new Text[numberOfPages];
    Text[] refBits = new Text[numberOfPages];
    List<Point> positions = layout(numberOfPages, center, radius);

    pageToPosition = new HashMap<Integer, Integer>(numberOfPages);
    positionToPage = new HashMap<Integer, Integer>(numberOfPages);
    referenceBits = new HashMap<Integer, Boolean>(numberOfPages);

    for (int i = 0; i < numberOfPages; i++) {
      Point upperLeft = positions.get(i);
      upperLeft.translate(-(pageRectSideLength / 2), -(pageRectSideLength / 2));
      Point lowerRight = new Point(upperLeft);
      lowerRight.translate(pageRectSideLength, pageRectSideLength);
      pageRects[i] = lang.newRect(Node.convertToNode(upperLeft),
          Node.convertToNode(lowerRight), "pageRect" + i, null);
      pageRects[i].changeColor(null, cacheColor, null, null);
      upperLeft.translate(4, 4);
      pageTexts[i] = lang.newText(Node.convertToNode(upperLeft), "page " + i,
          "pageText" + i, null);
      pageTexts[i].changeColor(null, cacheColor, null, null);
      pageToPosition.put(i, i);
      positionToPage.put(i, i);
      referenceBits.put(i, false);
      upperLeft.translate(0, 20);
      refBits[i] = lang.newText(Node.convertToNode(upperLeft), "refB: 0",
          "refBit" + i, null);
      refBits[i].changeColor(null, cacheColor, null, null);
    }
    Text pageAccesses = lang.newText(new Coordinates(500, 10),
        "page accesses:", "pageAccessesArrayLabel", null);

    ArrayProperties arrayProps = (ArrayProperties) properties
        .getPropertiesByName("array properties");
    IntArray pageAccessesIntArray = lang.newIntArray(new Offset(15, 40,
        "pageAccessesArrayLabel", AnimalScript.DIRECTION_NE),
        this.pageAccesses, "pageAccessesArray", null, arrayProps);

    // set the clock handle
    PolylineProperties plProps = new PolylineProperties();
    plProps.set("fwArrow", true); // set arrow
    Node[] clockHandleNodes = new Node[2];
    clockHandleNodes[0] = new Coordinates(center.x / 2, center.y / 2);
    clockHandleNodes[1] = new Coordinates(positions.get(0).x,
        positions.get(0).y);
    Polyline clockHandle = lang.newPolyline(clockHandleNodes, "clockHandle",
        null, plProps);
    positionOfClockHandle = 0;

    addSourcecode(new Offset(0, 100, pageAccesses, AnimalScript.DIRECTION_NW));

    lang.nextStep();
    doClockPageReplacement(pageTexts, refBits, clockHandle,
        pageAccessesIntArray);
  }

  private void addSourcecode(Node position) {
    SourceCodeProperties scProps = (SourceCodeProperties) properties
        .getPropertiesByName("source code properties");

    sourceCode = lang.newSourceCode(position, "source", null, scProps);
  }

  private void doClockPageReplacement(Text[] pageTexts, Text[] refBits,
      Polyline clockHandle, IntArray pageAccessesIntArray) {
    for (int i = 0; i < pageAccessesIntArray.getLength(); i++) {
      pageAccessesIntArray.highlightCell(i, null, null); // do not use an
                                                         // ArrayMarker, because
                                                         // they are broken
      exec("for-every");
      lang.nextStep();
      int currentPageAccess = pageAccessesIntArray.getData(i);

      exec("if");
      lang.nextStep();

      if (pageToPosition.containsKey(currentPageAccess)) { // no page miss, just
                                                           // update the refBit
        pageRects[pageToPosition.get(currentPageAccess)].changeColor(null,
            cacheHighlightColor, null, null);
        exec("then");

        setReferenceBit(refBits[currentPageAccess], currentPageAccess);
        lang.nextStep();
        pageRects[pageToPosition.get(currentPageAccess)].changeColor(null,
            cacheColor, null, null);

      } else { // page miss, find a place to page in
        exec("while");
        exec("set-bit");
        exec("adv-p");
        while (referenceBits.get(positionOfClockHandle)) {
          // refBit == 1 => rotate clock, find better place
          clearReferenceBit(refBits[positionOfClockHandle],
              positionOfClockHandle);
          advanceClockHandle(clockHandle);
          lang.nextStep();
        }
        // refBit == 0 => page at clock handle is not referenced, overwrite it

        exec("replace");
        replaceInBuffer(pageTexts[positionOfClockHandle],
            refBits[positionOfClockHandle], currentPageAccess,
            positionOfClockHandle);
        lang.nextStep();

        exec("advance-pointer");
        // also advance the clock, because the page we just paged in is the
        // freshest page
        advanceClockHandle(clockHandle);
        lang.nextStep();
      }
      pageAccessesIntArray.unhighlightCell(i, null, null);
    }
  }

  private void advanceClockHandle(Polyline clockHandle) {
    Node center = clockHandle.getNodes()[0];

    positionOfClockHandle++;
    positionOfClockHandle %= numberOfPages;

    double toRotate = -(360.0 / numberOfPages + rotationRoundingError); // clockwise,
                                                                        // so
                                                                        // negate
    rotationRoundingError = toRotate - Math.floor(toRotate);

    clockHandle.rotate(center, (int) toRotate, null, null);
  }

  private void setReferenceBit(Text referenceBitOfPage, Integer page) {
    referenceBits.put(page, true);
    referenceBitOfPage.setText("refB: 1", null, null);
  }

  private void clearReferenceBit(Text referenceBitOfPage, Integer page) {
    referenceBits.put(page, false);
    referenceBitOfPage.setText("refB: 0", null, null);
  }

  private void replaceInBuffer(Text spot, Text referenceBit, int newPage,
      int position) {
    // update internal maps
    int oldPage = positionToPage.get(positionOfClockHandle);
    pageToPosition.remove(oldPage);
    pageToPosition.put(newPage, position);
    positionToPage.put(position, newPage);
    spot.setText("page " + newPage, null, null);
    setReferenceBit(referenceBit, newPage);
  }

  private List<Point> layout(int numVertices, Point center, int radius) {
    List<Point> positions = new ArrayList<Point>(numVertices);
    for (int i = 0; i < numVertices; i++) {
      double angle = (2 * Math.PI * i) / numVertices;
      Point position = new Point();
      position.setLocation(Math.cos(angle) * radius + center.x / 2,
          Math.sin(angle) * radius + center.y / 2);
      positions.add(position);
    }
    return positions;
  }

  @Override
  public String generate(AnimationPropertiesContainer properties,
      Hashtable<String, Object> primitives) {
    this.properties = properties;
    init();

    vars.declare("int", "page-misses");
    vars.setGlobal("page-misses");
    Text text = lang.newText(new Coordinates(10, 10), "...", "page-misses",
        null);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Page misses: ");
    tu.addToken(vars.getVariable("page-misses"));
    tu.update();

    RectProperties colorProperties = (RectProperties) properties
        .getPropertiesByName("cache display");
    cacheColor = (Color) colorProperties
        .get(AnimationPropertiesKeys.COLOR_PROPERTY);
    cacheHighlightColor = (Color) colorProperties
        .get(AnimationPropertiesKeys.FILL_PROPERTY);

    numberOfPages = (Integer) primitives.get("number of pages");
    pageAccesses = (int[]) primitives.get("page accesses");

    start();

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Clock Page Replacement";
  }

  @Override
  public String getAnimationAuthor() {
    return "Thomas Pilot, Tobias Freudenreich";
  }

  @Override
  public String getCodeExample() {
    StringBuilder builder = new StringBuilder();
    builder.append("for every page access:\n");
    builder.append("\tif page is already in cache\n");
    builder.append("\tthen: set reference bit to 1\n");
    builder.append("\telse:\n");
    builder.append("\t\twhile page at pointer has reference bit 1\n");
    builder.append("\t\t\tset reference bit to 0\n");
    builder.append("\t\t\tadvance pointer\n");
    builder.append("\t\t// page at pointer has reference bit 0\n");
    builder
        .append("\t\treplace page at pointer with accessed page, set reference bit to 1\n");
    builder.append("\t\tadvance pointer\n");
    return builder.toString();
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return "Visualizes how the Clock Page Replacement (CPR) algorithm works which is used as a cache replacement strategy";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public String getName() {
    return "Clock Page Replacement [version with annotations]";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public String getAnnotatedSrc() {
    return "for every page access:                                              @label(\"for-every\")\n"
        + "if page is already in cache                                              @label(\"if\")\n"
        + "   then: set reference bit to 1                                          @label(\"then\")\n"
        + "   else:                                                                 @label(\"else\") @inc(\"page-misses\")\n"
        + "      while page at pointer has reference bit 1                          @label(\"while\")\n"
        + "         set reference bit to 0                                          @label(\"set-bit\")\n"
        + "         advance pointer                                                 @label(\"adv-p\")\n"
        + "      // page at pointer has reference bit 0                             @label(\"comment\")\n"
        + "      replace page at pointer with accessed page, set reference bit to 1 @label(\"replace\")\n"
        + "      advance pointer                                                    @label(\"advance-pointer\")\n";
  }

}
